package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.zhangla.service.ITrZhanglaService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.SignUtil;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName zlZJZLJob：瑞苍数据推送32221
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/19 10:22
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zlZJZLJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaRenwudanService zhanglaRenwudanService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_ZJZLTS);//瑞苍张拉质检资料推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓张拉定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = zhanglaXxbService.selectrczlList(shebeilist, curid);
        if (null == zhanglaXxbs || zhanglaXxbs.size() == 0) {
            log.info(String.format("暂无瑞仓张拉未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrZhanglaXxb zhanglaXxb : zhanglaXxbs) {
            JSONObject sendObject = JSONUtil.createObj();
            id = zhanglaXxb.getId();

            QueryWrapper<TrZhanglaRenwudan> zhanglaRenwudanQueryWrapper = new QueryWrapper<>();
            zhanglaRenwudanQueryWrapper.eq("uuid", zhanglaXxb.getUuid());
            TrZhanglaRenwudan zhanglaRenwudan = zhanglaRenwudanService.getOne(zhanglaRenwudanQueryWrapper);
            if (zhanglaRenwudan != null) {
                if (zhanglaRenwudan.getSgbwuuid() != null) {
                    QueryWrapper<SysDepartproject> sysDepartprojectQueryWrapper = new QueryWrapper<>();
                    sysDepartprojectQueryWrapper.eq("org_code", zhanglaRenwudan.getSgbwuuid());

                    SysDepartproject sysDepartproject = sysDepartprojectService.getOne(sysDepartprojectQueryWrapper);

                    if (null != sysDepartproject) {
                        String orgType = sysDepartproject.getOrgType();
                        if (!orgType.equals("13")) {
                            String id1 = sysDepartproject.getId();
                            QueryWrapper<SysDepartproject> sysDepartprojectQueryWrapper1 = new QueryWrapper<>();
                            sysDepartprojectQueryWrapper1.eq("parent_id", id1)
                                    .like("depart_name", "张拉")
                                    .like("depart_name", "加工")
                                    .eq("org_type","13")
                                    .last("limit 1");
                            sysDepartproject = sysDepartprojectService.getOne(sysDepartprojectQueryWrapper1) !=null? sysDepartprojectService.getOne(sysDepartprojectQueryWrapper1):sysDepartproject;
                        }

                        sendObject.set("treeid", sysDepartproject.getTreeid());
                        String treeid = zhanglaRenwudan.getTreeid();
                        if (treeid == null){
                            zhanglaXxb.setIssend(3);
                            zhanglaXxbService.saveOrUpdate(zhanglaXxb);
                            continue;

                        }
                        sendObject.set("index_id", sysDepartproject.getId());
                        sendObject.set("old_id", sysDepartproject.getOldid());

                        String description = sysDepartproject.getDescription();
                        String[] split = description.split("/");

                        sendObject.set("fbgc", split[0]);
                        sendObject.set("fxgc", split[1]);

                        sendObject.set("dwgc", sysDepartproject.getDepartName());
                    }
                }else{
                    zhanglaXxb.setIssend(3);
                    zhanglaXxbService.saveOrUpdate(zhanglaXxb);
                    continue;
                }
            }
            QueryWrapper<TrZhanglaM> zhanglaMQueryWrapper = new QueryWrapper<>();
            zhanglaMQueryWrapper.eq("syjid", zhanglaXxb.getSyjid());
            List<TrZhanglaM> list = zhanglaMService.list(zhanglaMQueryWrapper);
            if (list.size()==0){
                zhanglaXxb.setIssend(3);
                zhanglaXxbService.saveOrUpdate(zhanglaXxb);
                continue;
            }
            sendObject.set("createdate", zhanglaXxb.getSgsj());
            sendObject.set("updatedate", zhanglaXxb.getSgsj());
            sendObject.set("gjmc", zhanglaXxb.getGjmc());
            if (list.size() > 0) {
                sendObject.set("zlrq", list.get(0).getZlsj());
            } else {
                sendObject.set("zlrq", zhanglaXxb.getSgsj());
            }
            sendObject.set("zlqw", "");
            sendObject.set("jzrq", zhanglaXxb.getSgsj());
            sendObject.set("zlsqd", zhanglaXxb.getSnsjqd());
            sendObject.set("zltq", "");
            sendObject.set("yylgczlgg", "");
            sendObject.set("yylgccdpp", "");
            sendObject.set("yylgcbh", "");
            sendObject.set("mjjzlgg", "");
            sendObject.set("mjjcdpp", "");
            sendObject.set("mjjbh", "");
            sendObject.set("mdbzlgg", "");
            sendObject.set("mdbcdpp", "");
            sendObject.set("mdbbh", "");
            sendObject.set("zlsxjcx", "");
            sendObject.set("gsjbh", "");
            sendObject.set("lbgd", "");
            sendObject.set("scsgd", zhanglaXxb.getZlcsep());
            sendObject.set("fcsgd", "");
            sendObject.set("hntsjqd", zhanglaXxb.getSnskqd());
            String sjzll = list.get(0).getSjzll();
            if (StringUtil.isEmpty(sjzll)){
                sjzll = "0";
            }
            sendObject.set("zlylzb", sjzll);
            sendObject.set("zllwc", "");
            SecureRandom random = new SecureRandom();
            String s = String.valueOf(random.nextInt());
            String time = String.valueOf(System.currentTimeMillis());

            List listsend = new ArrayList();
            listsend.add(sendObject);
            String encode = "";
            try {
                encode = URLEncoder.encode(listsend.toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String shebeibianhao = zhanglaXxb.getShebeibianhao();
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(shebeibianhao);
            String signature = SignUtil.signature("e6540129236a44639778678879ce47bb", s, time);
            String zhijianid = sbjwd.getZhijianid();
            String http = "http://fjhmtd.com:"+zhijianid;
            if (zhijianid.equals("32907")||zhijianid.equals("32621")){
                http = "https://fjhmtd.com:"+zhijianid;
            }
            String url = http+"/glaf/website/ws/execute/api/crud?useId=34130ef204cd4427aaaa209b3311cb97&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params="+encode;
            String back = HttpRequest.post(url)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();

            pushandreturnService.saveData(id, String.valueOf(listsend), selectsysconfigone.getRemark(), back);
            if (back.contains("操作成功")) {
                zhanglaXxb.setIssend(1);
            } else {
                zhanglaXxb.setIssend(2);
            }
            zhanglaXxbService.saveOrUpdate(zhanglaXxb);
            sysConfigService.updateSysConfig(JobUtil.RC_ZJZLTS, id);//根据传过来的唯一值来修改当前判断到的数据id


            cn.hutool.json.JSONObject jsonObject2 = JSONUtil.parseObj(back);
            Object data = jsonObject2.get("data");
            List<Object> listarr = JSON.parseArray(String.valueOf(data));
            String dataItem = String.valueOf(listarr.get(0));
            cn.hutool.json.JSONObject jsonObject3 = JSONUtil.parseObj(dataItem);
            String sid = String.valueOf(jsonObject3.get("id"));

            List listsendson = new ArrayList();
            for (TrZhanglaM zhanglaM : list) {
                JSONObject sendObjectson = JSONUtil.createObj();
                QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
                zhanglaSQueryWrapper.eq("fguid", zhanglaM.getFguid());
                zhanglaSQueryWrapper.orderByAsc("jdbfb");
                zhanglaSQueryWrapper.orderByAsc("jdzll");
                List<TrZhanglaS> zhanglaSList = zhanglaService.list(zhanglaSQueryWrapper);
                for (TrZhanglaS zhanglaS : zhanglaSList) {
                    String dh = zhanglaS.getDh();
                    String jdbfb = zhanglaS.getJdbfb();
                    if (dh == null || jdbfb == null){
                        zhanglaXxb.setIssend(3);
                        zhanglaXxbService.saveOrUpdate(zhanglaXxb);
                        continue;
                    }
                    if (dh.equals("1") && jdbfb.equals("10")) {
                        sendObjectson.set("cylsclqd", zhanglaS.getJdscl());
                    }
                    if (dh.equals("2") && jdbfb.equals("10")) {
                        sendObjectson.set("cylsclhd", zhanglaS.getJdscl());
                    }
                    if (dh.equals("1") && jdbfb.equals("20")) {
                        sendObjectson.set("2bcylscqd", zhanglaS.getJdscl());
                    }
                    if (dh.equals("2") && jdbfb.equals("20")) {
                        sendObjectson.set("2bcylschd", zhanglaS.getJdscl());
                    }
                    if (dh.equals("1") && jdbfb.equals("100")) {
                        sendObjectson.set("mgsclqd", zhanglaS.getJdscl());
                    }
                    if (dh.equals("2") && jdbfb.equals("100")) {
                        sendObjectson.set("mgsclhd", zhanglaS.getJdscl());
                    }

                }
                sendObjectson.set("topid", sid);
                sendObjectson.set("createdate", zhanglaM.getZlsj());
                sendObjectson.set("updatedate", zhanglaM.getZlsj());
                sendObjectson.set("gsbh", zhanglaM.getGsbh());
                sendObjectson.set("zscl", zhanglaM.getZscl());
                sendObjectson.set("llscl", zhanglaM.getLlscl());
                sendObjectson.set("sclwc", zhanglaM.getSclper());

                QueryWrapper<TrZhanglaS> hslqd = new QueryWrapper<>();
                hslqd.select("dh, max(ljhsl) ljhsl");
                hslqd.eq("fguid", zhanglaM.getFguid());
                hslqd.groupBy("dh");
                List<TrZhanglaS> hslqds = zhanglaService.list(hslqd);
                for (TrZhanglaS trZhanglaS : hslqds){
                    String dh = trZhanglaS.getDh();
                    if (dh == null){
                        zhanglaXxb.setIssend(3);
                        zhanglaXxbService.saveOrUpdate(zhanglaXxb);
                        continue;
                    }
                    if (dh.equals("1")){
                        sendObjectson.set("hslqd", trZhanglaS.getLjhsl());
                    }
                    if (dh.equals("2")){
                        sendObjectson.set("hslhd", trZhanglaS.getLjhsl());
                    }
                }
                sendObjectson.set("dhsqk", zhanglaM.getWt());

                listsendson.add(sendObjectson);

            }

            String encodeson = "";
            try {
                encodeson = URLEncoder.encode(listsendson.toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String urlson = http+"/glaf/website/ws/execute/api/crud?useId=8bf27d328786493598cf9d9075b744d6&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params="+encodeson;
            String backon = HttpRequest.post(urlson)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();

            pushandreturnService.saveData(id, String.valueOf(listsendson), selectsysconfigone.getRemark(), backon);

            if (backon.contains("操作成功")) {
            } else {
                zhanglaXxb.setIssend(10);
                zhanglaXxbService.saveOrUpdate(zhanglaXxb);
            }
        }
    }
}
