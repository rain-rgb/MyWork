package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
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

/**
 * @ClassName yjZJZLJob：瑞苍数据推送32221
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/22 10:20
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yjZJZLJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private ITrYajiangRenwudanService yajiangRenwudanService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_ZJYJTS);//瑞苍压浆质检资料推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓压浆定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> trYajiangMS = yajiangMService.selectrcyjList(shebeilist, curid);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无瑞仓压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrYajiangM yajiangM : trYajiangMS) {
            id = yajiangM.getId();

            JSONObject sendObject = JSONUtil.createObj();

            QueryWrapper<TrYajiangRenwudan> yajiangRenwudanQueryWrapper = new QueryWrapper<>();
            yajiangRenwudanQueryWrapper.eq("uuid", yajiangM.getUuid());
            TrYajiangRenwudan yajiangRenwudan = yajiangRenwudanService.getOne(yajiangRenwudanQueryWrapper);

            if (yajiangRenwudan != null) {
                String sjyl = yajiangRenwudan.getSjyl();
                if (StringUtil.isEmpty(sjyl)){
                    sjyl = "0";
                }else {
                    if (sjyl.contains("~")) {
                        sjyl = sjyl.split("~")[0];
                    }
                    // 只保留数字部分
                    sjyl = sjyl.replaceAll("[^0-9.]", "");
                    // 如果处理后字符串为空，设置为 "0"
                    if (sjyl.isEmpty()) {
                        sjyl = "0";
                    }
                }
                sendObject.set("sbgdzdyl", sjyl);
                if (yajiangRenwudan.getSgbwuuid() != null) {

                    QueryWrapper<SysDepartproject> sysDepartprojectQueryWrapper = new QueryWrapper<>();
                    sysDepartprojectQueryWrapper.eq("org_code", yajiangRenwudan.getSgbwuuid());
                    SysDepartproject sysDepartproject = sysDepartprojectService.getOne(sysDepartprojectQueryWrapper);

                    if (null != sysDepartproject) {
                        String orgType = sysDepartproject.getOrgType();
                        if (!orgType.equals("13")) {
                            String id1 = sysDepartproject.getId();
                            QueryWrapper<SysDepartproject> sysDepartprojectQueryWrapper1 = new QueryWrapper<>();
                            sysDepartprojectQueryWrapper1.eq("parent_id", id1)
                                    .like("depart_name", "封锚")
                                    .like("depart_name", "压浆")
                                    .eq("org_type","13")
                                    .last("limit 1");
                            sysDepartproject = sysDepartprojectService.getOne(sysDepartprojectQueryWrapper1)!=null?sysDepartprojectService.getOne(sysDepartprojectQueryWrapper1):sysDepartproject;
                        }

                        sendObject.set("treeid", sysDepartproject.getTreeid());
                        sendObject.set("index_id", sysDepartproject.getId());
                        sendObject.set("old_id", sysDepartproject.getOldid());

                        String description = sysDepartproject.getDescription();
                        String[] split = description.split("/");

                        sendObject.set("fbgc", split[0]);
                        sendObject.set("fxgc", split[1]);

                        sendObject.set("dwgc", sysDepartproject.getDepartName());
                    }
                }
            }

            sendObject.set("gjtsgrq", "");
            sendObject.set("updatedate", yajiangM.getYjsj());
            sendObject.set("createdate", yajiangM.getYjsj());
            sendObject.set("jgwmc", yajiangM.getLblx());
            sendObject.set("lczh", yajiangM.getLianghao());
            sendObject.set("gjmc", yajiangM.getLianghao());
            sendObject.set("gjbh", yajiangM.getGjbh());
            sendObject.set("snpz", yajiangM.getSnmc());
            sendObject.set("snbh", "");
            String yajiangji = yajiangM.getYajiangji();
            if (StringUtil.isEmpty(yajiangji)){
                yajiangji = "掺减水";
            }
            sendObject.set("wjjmc", yajiangji);
            sendObject.set("wjjyl", yajiangM.getCjsjl());
            sendObject.set("zlrq", yajiangM.getYjsj());
            sendObject.set("sjb", yajiangM.getShuijiaobi());
            sendObject.set("yjff", yajiangM.getYajiangguoc());
            sendObject.set("yjsgrq", yajiangM.getYjsj());
            sendObject.set("jbjmc", "");
            String yjsbbh = yajiangM.getYjsbbh();
            ShebeiInfo list = shebeiInfoService.SBJWD(yjsbbh);
            if (null != list) {
                sendObject.set("yjjmc", list.getSbname());
            }
            sendObject.set("sbgcd", "");
            sendObject.set("qdsjz", "");
            sendObject.set("qdjcjg", "");
            sendObject.set("cdsjz", "");
            sendObject.set("cdjcjg", "");
            sendObject.set("mslzdsjz", "");
            sendObject.set("mslzdjcjg", "");
            sendObject.set("msl3hjcjg", "");
            sendObject.set("msl24hsjz", "");
            sendObject.set("msl24hjcjg", yajiangM.getMsl());

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
            String signature = SignUtil.signature("e6540129236a44639778678879ce47bb", s, time);
            String zhijianid = list.getZhijianid();
            String http = "http://fjhmtd.com:"+zhijianid;
            if (zhijianid.equals("32907")||zhijianid.equals("32621")){
                http = "https://fjhmtd.com:"+zhijianid;
            }
            String url = http+"/glaf/website/ws/execute/api/crud?useId=dcf20b25d5e04a1bad5e5e3ced2eb380&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params="+encode;
            String back = HttpRequest.post(url)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();

            pushandreturnService.saveData(id, String.valueOf(listsend), selectsysconfigone.getRemark(), back);
            if (back.contains("操作成功")) {
                yajiangM.setIsmt(1);
            } else {
                yajiangM.setIsmt(2);
            }
            yajiangMService.saveOrUpdate(yajiangM);
            sysConfigService.updateSysConfig(JobUtil.RC_ZJYJTS, id);//根据传过来的唯一值来修改当前判断到的数据id

            JSONObject backjsonObject = JSONUtil.parseObj(back);
            JSONArray dataArray = backjsonObject.getJSONArray("data");
            JSONObject dataObject = dataArray.getJSONObject(0);
            String sid = dataObject.getStr("id");

            List listsendson = new ArrayList();
            QueryWrapper<TrYajiangS> yajiangSQueryWrapper = new QueryWrapper<>();
            yajiangSQueryWrapper.eq("syjid", yajiangM.getSyjid());
            List<TrYajiangS> trYajiangSs = yajiangSService.list(yajiangSQueryWrapper);
            for (TrYajiangS yajiangS : trYajiangSs) {
                JSONObject sendObjectson = JSONUtil.createObj();
                sendObjectson.set("topid",sid);
                sendObjectson.set("createdate",yajiangS.getYajiangsj());
                sendObjectson.set("updatedate",yajiangS.getYajiangsj());
                sendObjectson.set("kdbh",yajiangS.getKongdao());
                sendObjectson.set("kdcd","");
                sendObjectson.set("yjsx",yajiangM.getYajiangguoc());
                sendObjectson.set("yjkwz","");
                sendObjectson.set("pqkwz","");
                sendObjectson.set("yjzdyl",yajiangS.getJinjiangyal());
                sendObjectson.set("pqkycqk",yajiangS.getMjqk());
                listsendson.add(sendObjectson);
            }

            String encodeson = "";
            try {
                encodeson = URLEncoder.encode(listsendson.toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String urlson = http+"/glaf/website/ws/execute/api/crud?useId=bf2141e82b0147518ee25716444927a0&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params="+encodeson;
            String backon = HttpRequest.post(urlson)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();

            pushandreturnService.saveData(id, String.valueOf(listsendson), selectsysconfigone.getRemark(), backon);
            if (!backon.contains("操作成功")) {
                yajiangM.setIsmt(10);
                yajiangMService.saveOrUpdate(yajiangM);
            }
        }
    }
}
