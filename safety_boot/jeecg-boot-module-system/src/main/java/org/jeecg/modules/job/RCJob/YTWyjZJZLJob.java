package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
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
 * @ClassName yjZJZLJob：数据推送甬台温的是32702
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/22 10:20
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class YTWyjZJZLJob implements Job {
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

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YTW_ZJYJTS);//甬台温压浆质检资料推送
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
        List<TrYajiangM> trYajiangMS = yajiangMService.selectyjList(shebeilist, curid);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无瑞仓压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrYajiangM yajiangM : trYajiangMS) {
            JSONObject sendObject = JSONUtil.createObj();
            id = yajiangM.getId();
            QueryWrapper<TrYajiangRenwudan> yajiangRenwudanQueryWrapper = new QueryWrapper<>();
            yajiangRenwudanQueryWrapper.eq("uuid", yajiangM.getUuid());
            TrYajiangRenwudan yajiangRenwudan = yajiangRenwudanService.getOne(yajiangRenwudanQueryWrapper);
            if (yajiangRenwudan != null) {
                if (yajiangRenwudan.getSgbwuuid() != null) {
                    QueryWrapper<SysDepartproject> sysDepartprojectQueryWrapper = new QueryWrapper<>();
                    sysDepartprojectQueryWrapper.eq("org_code", yajiangRenwudan.getSgbwuuid());
                    List<SysDepartproject> list1 = sysDepartprojectService.list(sysDepartprojectQueryWrapper);
                    if (list1.size() > 0) {
                        sendObject.set("treeid", list1.get(0).getTreeid());
                        if (list1.get(0).getOrgType().equals("4")) {
                            sendObject.set("index_id", list1.get(0).getId().substring(2));
                            sendObject.set("old_id", list1.get(0).getOldid());

                            QueryWrapper<SysDepartproject> fbfxq = new QueryWrapper<>();
                            fbfxq.eq("id", list1.get(0).getParentId());
                            List<SysDepartproject> fbfx = sysDepartprojectService.list(fbfxq);
                            sendObject.set("fxgc", fbfx.get(0).getDepartName() + "、" + list1.get(0).getDepartName());

                            QueryWrapper<SysDepartproject> fbfxq1 = new QueryWrapper<>();
                            fbfxq1.eq("id", fbfx.get(0).getParentId());
                            List<SysDepartproject> fbfx1 = sysDepartprojectService.list(fbfxq1);
                            sendObject.set("fbgc", fbfx1.get(0).getDepartName());

                            QueryWrapper<SysDepartproject> fbfxq2 = new QueryWrapper<>();
                            fbfxq2.eq("id", fbfx1.get(0).getParentId());
                            List<SysDepartproject> fbfx2 = sysDepartprojectService.list(fbfxq2);
                            sendObject.set("dwgc", fbfx2.get(0).getDepartName());
                        }
                        if (list1.get(0).getOrgType().equals("6")) {
                            sendObject.set("index_id", list1.get(0).getId().substring(2));
                            sendObject.set("old_id", list1.get(0).getOldid());

                            QueryWrapper<SysDepartproject> fbfxq = new QueryWrapper<>();
                            fbfxq.eq("id", list1.get(0).getParentId());
                            List<SysDepartproject> fbfx = sysDepartprojectService.list(fbfxq);
                            sendObject.set("fxgc", fbfx.get(0).getDepartName() + "、" + list1.get(0).getDepartName());

                            QueryWrapper<SysDepartproject> fbfxq1 = new QueryWrapper<>();
                            fbfxq1.eq("id", fbfx.get(0).getParentId());
                            List<SysDepartproject> fbfx1 = sysDepartprojectService.list(fbfxq1);


                            QueryWrapper<SysDepartproject> fbfxq2 = new QueryWrapper<>();
                            fbfxq2.eq("id", fbfx1.get(0).getParentId());
                            List<SysDepartproject> fbfx2 = sysDepartprojectService.list(fbfxq2);
                            sendObject.set("fbgc", fbfx1.get(0).getDepartName() + "、" + fbfx2.get(0).getDepartName());

                            QueryWrapper<SysDepartproject> fbfxq3 = new QueryWrapper<>();
                            fbfxq3.eq("id", fbfx2.get(0).getParentId());
                            List<SysDepartproject> fbfx3 = sysDepartprojectService.list(fbfxq3);
                            sendObject.set("dwgc", fbfx3.get(0).getDepartName());
                        }
                    }
                }
            }
            sendObject.set("updatedate", yajiangM.getYjsj());
            sendObject.set("createdate", yajiangM.getYjsj());
            sendObject.set("jgwmc", yajiangM.getGjjg());
            sendObject.set("lczh", yajiangM.getZhbw());
            sendObject.set("gjmc", yajiangM.getGcmc());
            sendObject.set("gjbh", yajiangM.getGjbh());
            sendObject.set("snpz", yajiangM.getSnmc());
            sendObject.set("snbh", "");
            sendObject.set("gjtsgrq", "");
            sendObject.set("wjjmc", "掺减水");
            sendObject.set("wjjyl", yajiangM.getCjsjl());
            sendObject.set("zlrq", yajiangM.getYjsj());
            sendObject.set("sjb", yajiangM.getShuijiaobi());
            sendObject.set("yjff", yajiangM.getYajiangguoc());
            sendObject.set("yjsgrq", yajiangM.getYjsj());
            sendObject.set("jbjmc", "");
            String yjsbbh = yajiangM.getYjsbbh();
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
            shebeiInfoQueryWrapper.eq("sbjno", yjsbbh);
            List<ShebeiInfo> list = shebeiInfoService.list(shebeiInfoQueryWrapper);
            if (list.size() > 0) {
                sendObject.set("yjjmc", list.get(0).getSbname());
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
            sendObject.set("msl24hjcjg", "");
            sendObject.set("sbgdzdyl", "");

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
            String url = "http://fjhmtd.com:32702/glaf/website/ws/execute/api/crud?useId=dcf20b25d5e04a1bad5e5e3ced2eb380&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params="+encode;
            String back = HttpRequest.post(url)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();
            JSONObject jsonObject1 = new JSONObject(back);
            String codes = jsonObject1.get("status").toString();
            if (codes.equals("200")) {
                log.info(String.format("瑞仓压浆推送成功!" + id + "Json数据" + back));
            } else {
                log.info(String.format("瑞仓压浆推送失败!" + id + "Json数据" + back));
            }
            sysConfigService.updateSysConfig(JobUtil.RC_ZJYJTS, id);//根据传过来的唯一值来修改当前判断到的数据id

            JSONObject jsonObject2 = JSONUtil.parseObj(back);
            Object data = jsonObject2.get("data");
            List<Object> listarr = JSON.parseArray(String.valueOf(data));
            String dataItem = String.valueOf(listarr.get(0));
            JSONObject jsonObject3 = JSONUtil.parseObj(dataItem);
            String sid = String.valueOf(jsonObject3.get("id"));

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
            String urlson = "http://fjhmtd.com:32702/glaf/website/ws/execute/api/crud?useId=bf2141e82b0147518ee25716444927a0&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params="+encodeson;
            String backon = HttpRequest.post(urlson)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();
            JSONObject jsonObjecton = new JSONObject(backon);
            String codeson = jsonObjecton.get("status").toString();
            if (codeson.equals("200")) {
                log.info(String.format("瑞仓压浆从表推送成功!" + id + "Json数据" + back));
            } else {
                log.info(String.format("瑞仓压浆从表推送失败!" + id + "Json数据" + back));
            }
        }
    }
}
