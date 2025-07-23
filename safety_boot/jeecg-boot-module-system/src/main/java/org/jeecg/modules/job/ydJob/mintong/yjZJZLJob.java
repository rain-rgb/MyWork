package org.jeecg.modules.job.ydJob.mintong;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
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
 * @ClassName yjZJZLJob：义东数据推送32812
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YD_ZJYJTS);//义东压浆质检资料推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东压浆定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> trYajiangMS = yajiangMService.selectydyjList(shebeilist, curid);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无义东压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrYajiangM yajiangM : trYajiangMS) {
            id = yajiangM.getId();

            JSONObject sendObject = JSONUtil.createObj();

//            sendObject.set("sbgdzdyl", yajiangRenwudan.getSjyl());

            sendObject.set("gjtsgrq", "");
            sendObject.set("jtbw", yajiangM.getLianghao());
            sendObject.set("updatedate", yajiangM.getYjsj());
            sendObject.set("createdate", yajiangM.getYjsj());
            sendObject.set("jgwmc", yajiangM.getLblx());
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
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
            shebeiInfoQueryWrapper.eq("sbjno", yjsbbh);
            ShebeiInfo list = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
            if (null != list) {
                sendObject.set("yjjmc", list.getSbname());
            }
            String sysOrgCode = list.getSysOrgCode();
            if (sysOrgCode.contains("A05A01A05A02A01A02A01A02")){
                sendObject.set("old_id", "701");
            }else {
                sendObject.set("old_id", "601");
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
            String url = "http://fjhmtd.com:32812/glaf/website/ws/execute/api/crud?useId=dcf20b25d5e04a1bad5e5e3ced2eb380&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params="+encode;
            String back = HttpRequest.post(url)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();

            pushandreturnService.saveData(id, String.valueOf(listsend), selectsysconfigone.getRemark(), back);

            JSONObject jsonObject1 = new JSONObject(back);
            String codes = jsonObject1.get("status").toString();
            if (codes.equals("200")) {
                log.info(String.format("义东压浆推送成功!" + id + "Json数据" + back));
            } else {
                log.info(String.format("义东压浆推送失败!" + id + "Json数据" + back));
            }
            sysConfigService.updateSysConfig(JobUtil.YD_ZJYJTS, id);//根据传过来的唯一值来修改当前判断到的数据id

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
                sendObjectson.set("wysj",yajiangS.getChixushijia());
                sendObjectson.set("pqkycqk",yajiangS.getMjqk());
                listsendson.add(sendObjectson);
            }

            String encodeson = "";
            try {
                encodeson = URLEncoder.encode(listsendson.toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String urlson = "http://fjhmtd.com:32812/glaf/website/ws/execute/api/crud?useId=bf2141e82b0147518ee25716444927a0&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params="+encodeson;
            String backon = HttpRequest.post(urlson)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();

            pushandreturnService.saveData(id, String.valueOf(listsendson), selectsysconfigone.getRemark(), back);

            JSONObject jsonObjecton = new JSONObject(backon);
            String codeson = jsonObjecton.get("status").toString();
            if (codeson.equals("200")) {
                log.info(String.format("义东压浆从表推送成功!" + id + "Json数据" + backon));
            } else {
                log.info(String.format("义东压浆从表推送失败!" + id + "Json数据" + backon));
            }

        }
    }
}
