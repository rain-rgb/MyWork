package org.jeecg.modules.job.ydzhongtiejob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFWangnjService;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.ydzhongtiejob.Util.CryptoUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName shiYanJiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/10 9:31
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class shiYanJiJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService itSyjzbService;
    @Autowired
    private IFYalijiService yalijiService;
    @Autowired
    private IFWangnjService wangnjService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private static final String encryptKey = "wz2cFJhkM1qrIdkMqNY8cw==";
    private static final String iv = "wza41thrBplFGTLPE15G6g==";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YB_GTSYS);//义东试验室
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东试验室的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东试验室的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TSyjzb> tSyjzbs = itSyjzbService.selectLists(shebeilist, curid);
        if (null == tSyjzbs || tSyjzbs.size() == 0) {
            log.info(String.format("暂无义东试验室未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (TSyjzb tSyjzb : tSyjzbs) {
            id = tSyjzb.getId();
            JSONObject sendDate = new JSONObject();
            String sylx = tSyjzb.getSylx();
            String sbbh = tSyjzb.getSbbh();
            if ("YB-084".equals(sbbh)){
                sendDate.set("deviceCode", sbbh);
                sendDate.set("checkCode", "wbbylqhn");
            }else if ("YB-085".equals(sbbh)){
                sendDate.set("deviceCode", sbbh);
                sendDate.set("checkCode", "ft7h8i7i");
            }else if ("YB-086".equals(sbbh)){
                sendDate.set("deviceCode", sbbh);
                sendDate.set("checkCode", "zquoy7ii");
            }else if ("YB-087".equals(sbbh)){
                sendDate.set("deviceCode", sbbh);
                sendDate.set("checkCode", "b2b1aprx");
            }else if ("YB-088".equals(sbbh)){
                sendDate.set("deviceCode", "YB-088");
                sendDate.set("checkCode", "x12hi8x1");
            }else if ("YB-061".equals(sbbh)){
                sendDate.set("deviceCode", "YB-089");
                sendDate.set("checkCode", "if1aibx3");
            }
            if ("100047".equals(sylx)) {
                sendDate.set("engineerPartUse", tSyjzb.getCjmc());
                String pdjg = tSyjzb.getPdjg();
                if ("合格".equals(pdjg)) {
                    sendDate.set("evaluation", 10);
                }
                if ("不合格".equals(pdjg)) {
                    sendDate.set("evaluation", 20);
                }
                sendDate.set("outId", tSyjzb.getSyjid());
                sendDate.set("recordResult", "");
                sendDate.set("sampleCode", tSyjzb.getSjbh());
                String syrq = tSyjzb.getSyrq();
                if (syrq.length()<15){
                    syrq += " 00:00:00";
                }
                sendDate.set("testDate", syrq);
                sendDate.set("testType", "A001");

                QueryWrapper<FWangnj> queryWrapper = new QueryWrapper();
                queryWrapper.eq("syjid", tSyjzb.getSyjid());
                List<FWangnj> wangnjs = wangnjService.list(queryWrapper);
                List yalijisson = new ArrayList();
                for (FWangnj wangnj : wangnjs) {
                    JSONObject sendDatesson = new JSONObject();
                    sendDatesson.set("afterBreakGauge", wangnj.getDhbz());
                    sendDatesson.set("bendAngle", "");
                    sendDatesson.set("bendDiameter", "");
                    sendDatesson.set("bendTestResults", "");
                    sendDatesson.set("elongationAfterBreak", wangnj.getScl());
                    sendDatesson.set("limitLoad", wangnj.getLz());
                    sendDatesson.set("limitStrength", wangnj.getLzqd());
                    sendDatesson.set("nominalDiameter", tSyjzb.getGczj());
                    sendDatesson.set("originalGauge", wangnj.getWsbz());
                    sendDatesson.set("outId", wangnj.getFGuid());
                    sendDatesson.set("yieldLoad", wangnj.getQflz());
                    sendDatesson.set("yieldStrength", wangnj.getQfqd());

//                    QueryWrapper<fya>
                    List yalisson = new ArrayList();
                    sendDatesson.set("itemDataList", yalisson);

                    yalijisson.add(sendDatesson);
                }
                sendDate.set("itemList", yalijisson);
            }
            if ("100048".equals(sylx) || "100049".equals(sylx)) {
                sendDate.set("brand", "");
                sendDate.set("engineerPartUse", tSyjzb.getCjmc());
                String pdjg = tSyjzb.getPdjg();
                if ("合格".equals(pdjg)) {
                    sendDate.set("evaluation", 10);
                }
                if ("不合格".equals(pdjg)) {
                    sendDate.set("evaluation", 20);
                }
                sendDate.set("outId", tSyjzb.getSyjid());
                sendDate.set("recordResult", "");
                sendDate.set("sampleCode", tSyjzb.getSjbh());
                String syrq = tSyjzb.getSyrq();
                if (syrq.length()<15){
                    syrq += " 00:00:00";
                }
                sendDate.set("testDate", syrq);
                if ("100048".equals(sylx)) {
                    sendDate.set("testType", "A002");
                } else {
                    sendDate.set("testType", "A003");
                }


                QueryWrapper<FWangnj> queryWrapper = new QueryWrapper();
                queryWrapper.eq("syjid", tSyjzb.getSyjid());
                List<FWangnj> wangnjs = wangnjService.list(queryWrapper);
                List yalijisson = new ArrayList();
                for (FWangnj wangnj : wangnjs) {
                    JSONObject sendDatesson = new JSONObject();
                    sendDatesson.set("bendAngle", "");
                    sendDatesson.set("bendDiameter", "");
                    sendDatesson.set("bendTestResults", "");
                    sendDatesson.set("fractureLocation", "");
                    sendDatesson.set("fractureProperties", "");
                    sendDatesson.set("jointLength", "");
                    sendDatesson.set("limitLoad", wangnj.getLz());
                    sendDatesson.set("limitStrength", wangnj.getLzqd());
                    sendDatesson.set("nominalDiameter", tSyjzb.getGczj());
                    sendDatesson.set("outId", wangnj.getFGuid());
//                    QueryWrapper<fya>
                    List yalisson = new ArrayList();
                    sendDatesson.set("itemDataList", yalisson);

                    yalijisson.add(sendDatesson);
                }
                sendDate.set("itemList", yalijisson);
            }
            if ("100134".equals(sylx) || "100138".equals(sylx)) {
                sendDate.set("brand", "");
                sendDate.set("engineerPartUse", tSyjzb.getCjmc());
                String pdjg = tSyjzb.getPdjg();
                if ("合格".equals(pdjg)) {
                    sendDate.set("evaluation", 10);
                }
                if ("不合格".equals(pdjg)) {
                    sendDate.set("evaluation", 20);
                }
                sendDate.set("maintenanceWay", "");
                sendDate.set("outId", tSyjzb.getSyjid());
                sendDate.set("recordResult", "");
                sendDate.set("sampleCode", tSyjzb.getSjbh());
                sendDate.set("strengthLevel", tSyjzb.getSjqd());
                String syrq = tSyjzb.getSyrq();
                if (syrq.length()<15){
                    syrq += " 00:00:00";
                }
                sendDate.set("testDate", syrq);
                if ("100134".equals(sylx)) {
                    sendDate.set("testType", "A008");
                } else {
                    sendDate.set("testType", "A009");
                }
                QueryWrapper<FsYaliji> queryWrapper = new QueryWrapper();
                queryWrapper.eq("syjid", tSyjzb.getSyjid());
                List<FsYaliji> wangnjs = yalijiService.list(queryWrapper);
                List yalijisson = new ArrayList();
                for (FsYaliji wangnj : wangnjs) {
                    JSONObject sendDatesson = new JSONObject();
                    sendDatesson.set("age", tSyjzb.getLq());
                    sendDatesson.set("bendLoad", wangnj.getKylz());
                    sendDatesson.set("bendStrength", wangnj.getKyqd());
                    sendDatesson.set("compressionArea", tSyjzb.getSjmj());

                    String zzrq = tSyjzb.getZzrq();
                    if (zzrq.length()<15){
                        zzrq += " 00:00:00";
                    }
                    String syrq1 = tSyjzb.getSyrq();
                    if (syrq1.length()<15){
                        syrq1 += " 00:00:00";
                    }
                    sendDatesson.set("manufactureDate", zzrq);
                    sendDatesson.set("pressureDate", syrq1);
                    sendDatesson.set("serialNumber", tSyjzb.getSjbh());
                    sendDatesson.set("testCompressiveStrength", "");
                    sendDatesson.set("outId", wangnj.getFGuid());
//                    QueryWrapper<fya>
                    List yalisson = new ArrayList();
                    sendDatesson.set("itemDataList", yalisson);

                    yalijisson.add(sendDatesson);
                }
                sendDate.set("itemList", yalijisson);
            }
            if ("100131".equals(sylx) || "100133".equals(sylx)) {
                sendDate.set("cementType", "");
                sendDate.set("engineerPartUse", tSyjzb.getCjmc());
                String pdjg = tSyjzb.getPdjg();
                if ("合格".equals(pdjg)) {
                    sendDate.set("evaluation", 10);
                }
                if ("不合格".equals(pdjg)) {
                    sendDate.set("evaluation", 20);
                }
                sendDate.set("maintenanceWay", "");
                sendDate.set("manufactureInstructions", "");
                sendDate.set("mortarVariety", "");
                sendDate.set("outId", tSyjzb.getSyjid());
                sendDate.set("recordResult", "");
                sendDate.set("sampleCode", tSyjzb.getSjbh());
                sendDate.set("strengthLevel", tSyjzb.getSjqd());
                String syrq = tSyjzb.getSyrq();
                if (syrq.length()<15){
                    syrq += " 00:00:00";
                }
                sendDate.set("testDate", syrq);

                sendDate.set("testType", "A013");
                QueryWrapper<FsYaliji> queryWrapper = new QueryWrapper();
                queryWrapper.eq("syjid", tSyjzb.getSyjid());
                List<FsYaliji> wangnjs = yalijiService.list(queryWrapper);
                List yalijisson = new ArrayList();
                for (FsYaliji wangnj : wangnjs) {
                    JSONObject sendDatesson = new JSONObject();
                    sendDatesson.set("age", tSyjzb.getLq());
                    sendDatesson.set("compressiveLoad", wangnj.getKylz());
                    sendDatesson.set("compressiveStrength", wangnj.getKyqd());
                    sendDatesson.set("compressionArea", tSyjzb.getSjmj());
                    String zzrq = tSyjzb.getZzrq();
                    if (zzrq.length()<15){
                        zzrq += " 00:00:00";
                    }
                    String syrq1 = tSyjzb.getSyrq();
                    if (syrq1.length()<15){
                        syrq1 += " 00:00:00";
                    }
                    sendDatesson.set("manufactureDate", zzrq);
                    sendDatesson.set("pressureDate", syrq1);
                    sendDatesson.set("serialNumber", tSyjzb.getSjbh());
                    sendDatesson.set("testCompressiveStrength", "");
                    sendDatesson.set("outId", wangnj.getFGuid());
//                    QueryWrapper<fya>
                    List yalisson = new ArrayList();
                    sendDatesson.set("itemDataList", yalisson);

                    yalijisson.add(sendDatesson);
                }
                sendDate.set("itemList", yalijisson);
            }
            if ("100135".equals(sylx) || "100136".equals(sylx)) {
                sendDate.set("breed", "");
                sendDate.set("engineerPartUse", tSyjzb.getCjmc());
                String pdjg = tSyjzb.getPdjg();
                if ("合格".equals(pdjg)) {
                    sendDate.set("evaluation", 10);
                }
                if ("不合格".equals(pdjg)) {
                    sendDate.set("evaluation", 20);
                }
                sendDate.set("maintenanceWay", "");
                sendDate.set("manufactureInstructions", "");
                sendDate.set("outId", tSyjzb.getSyjid());
                sendDate.set("recordResult", "");
                sendDate.set("sampleCode", tSyjzb.getSjbh());
                sendDate.set("strengthLevel", tSyjzb.getSjqd());
                String syrq = tSyjzb.getSyrq();
                if (syrq.length()<15){
                    syrq += " 00:00:00";
                }
                sendDate.set("testDate", syrq);
                if ("100135".equals(sylx)) {
                    sendDate.set("testType", "A011");
                } else {
                    sendDate.set("testType", "A012");
                }
                QueryWrapper<FsYaliji> queryWrapper = new QueryWrapper();
                queryWrapper.eq("syjid", tSyjzb.getSyjid());
                List<FsYaliji> wangnjs = yalijiService.list(queryWrapper);
                List yalijisson = new ArrayList();
                for (FsYaliji wangnj : wangnjs) {
                    JSONObject sendDatesson = new JSONObject();
                    sendDatesson.set("age", tSyjzb.getLq());
                    sendDatesson.set("compressiveLoad", wangnj.getKylz());
                    sendDatesson.set("compressiveStrength", wangnj.getKyqd());
                    sendDatesson.set("compressionArea", tSyjzb.getSjmj());
                    String zzrq = tSyjzb.getZzrq();
                    if (zzrq.length()<15){
                        zzrq += " 00:00:00";
                    }
                    String syrq1 = tSyjzb.getSyrq();
                    if (syrq1.length()<15){
                        syrq1 += " 00:00:00";
                    }
                    sendDatesson.set("manufactureDate", zzrq);
                    sendDatesson.set("pressureDate", syrq1);
                    sendDatesson.set("serialNumber", tSyjzb.getSjbh());
                    sendDatesson.set("testCompressiveStrength", "");
                    sendDatesson.set("outId", wangnj.getFGuid());
//                    QueryWrapper<fya>
                    List yalisson = new ArrayList();
                    sendDatesson.set("itemDataList", yalisson);

                    yalijisson.add(sendDatesson);
                }
                sendDate.set("itemList", yalijisson);
            }
            if ("100014".equals(sylx)||"100554".equals(sylx)) {
                sendDate.set("concreteType", "");
                sendDate.set("engineerPartUse", tSyjzb.getCjmc());
                String pdjg = tSyjzb.getPdjg();
                if ("合格".equals(pdjg)) {
                    sendDate.set("evaluation", 10);
                }
                if ("不合格".equals(pdjg)) {
                    sendDate.set("evaluation", 20);
                }
                sendDate.set("maintenanceWay", "");
                sendDate.set("manufactureInstructions", "");
                sendDate.set("mixWay", "");
                sendDate.set("outId", tSyjzb.getSyjid());
                sendDate.set("recordResult", "");
                sendDate.set("sampleCode", tSyjzb.getSjbh());
                sendDate.set("strengthLevel", tSyjzb.getSjqd());
                String syrq = tSyjzb.getSyrq();
                if (syrq.length()<15){
                    syrq += " 00:00:00";
                }
                sendDate.set("testDate", syrq);
                if ("100014".equals(sylx)) {
                    sendDate.set("testType", "A007");
                }else {
                    sendDate.set("testType", "A005");
                }
                QueryWrapper<FsYaliji> queryWrapper = new QueryWrapper();
                queryWrapper.eq("syjid", tSyjzb.getSyjid());
                List<FsYaliji> wangnjs = yalijiService.list(queryWrapper);
                List yalijisson = new ArrayList();
                for (FsYaliji wangnj : wangnjs) {
                    JSONObject sendDatesson = new JSONObject();
                    sendDatesson.set("age", tSyjzb.getLq());
                    sendDatesson.set("compressiveLoad", wangnj.getKylz());
                    sendDatesson.set("compressiveStrength", wangnj.getKyqd());
                    sendDatesson.set("designStrengthPercent", "");
                    if ("合格".equals(pdjg)) {
                        sendDatesson.set("evaluation", 10);
                    }
                    if ("不合格".equals(pdjg)) {
                        sendDatesson.set("evaluation", 20);
                    }
                    String zzrq = tSyjzb.getZzrq();
                    if (zzrq.length()<15){
                        zzrq += " 00:00:00";
                    }
                    String syrq1 = tSyjzb.getSyrq();
                    if (syrq1.length()<15){
                        syrq1 += " 00:00:00";
                    }
                    sendDatesson.set("manufactureDate", zzrq);
                    sendDatesson.set("pressureDate", syrq1);
                    sendDatesson.set("serialNumber", tSyjzb.getSjbh());
                    sendDatesson.set("size", tSyjzb.getSjcc());
                    sendDatesson.set("standardCompressiveStrength", "");
                    sendDatesson.set("outId", wangnj.getFGuid());
//                    QueryWrapper<fya>
                    List yalisson = new ArrayList();
                    sendDatesson.set("itemDataList", yalisson);

                    yalijisson.add(sendDatesson);
                }
                sendDate.set("itemList", yalijisson);
            }

            String s = "";
            try {
                s = CryptoUtils.encryptSymmetrically(encryptKey, iv, String.valueOf(sendDate), CryptoUtils.Algorithm.AES_CBC_PKCS5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String timeStr1= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String s1 = MD5Util.MD5Encode(encryptKey + iv + timeStr1, "utf-8");

            JSONObject sendDates = new JSONObject();
            sendDates.set("text",s);
            sendDates.set("encryptKey",encryptKey);
            sendDates.set("date",timeStr1);
            sendDates.set("sign",s1);

            String back = HttpRequest.post("https://cdentity.tgmis.cn:7004/admin/new_lab_data_main/push_data")
                    .body(String.valueOf(sendDates))
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), back);

            JSONObject jsonObject1 = new JSONObject(back);
            String codes = jsonObject1.get("code").toString();
            if ("200".equals(codes)) {
                log.info(String.format("永壁试验室推送中铁成功!" + id + "Json数据" + sendDate));
            } else {
                log.info(String.format("永壁试验室推送中铁失败!" + id + "Json数据" + sendDate));
            }
            sysConfigService.updateSysConfig(JobUtil.YB_GTSYS, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
