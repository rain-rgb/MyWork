package org.jeecg.modules.job.sgsanbiaojob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName banHeZhanJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/12/13 13:20
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class banHeZhanJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;

    private static String url = "https://www.lhgldq.com:9888/iot-liuheng-service/api/mix/dataReceive/pieceListDataBase";
    private static String url1 = "https://www.lhgldq.com:9888/iot-liuheng-service/api/mix/dataReceive/produceUpdateDataBase";

    private static String blender = "YKLC";
    private static String userKey = "10003";
    private static String secret = "4028e5aa811326dd018136676d790609";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LH_BHZ);//六横拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到六横拌合站的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输六横拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectLists2(shebeilist, curid);
        if (null == bhzCementBases || bhzCementBases.size() == 0) {
            log.info(String.format("暂无六横拌合站未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BhzCementBase bhzCementBase : bhzCementBases) {
            id = bhzCementBase.getId();

            //任务单
            JSONObject rwdDate = new JSONObject();
            rwdDate.set("id", "YKLC"+bhzCementBase.getId());
            rwdDate.set("machineId", "YKLC");
            rwdDate.set("taskNo", bhzCementBase.getId());
            rwdDate.set("dataTime", sdf.format(bhzCementBase.getProductDatetime()));
            rwdDate.set("projName", bhzCementBase.getProjectName());
            rwdDate.set("projAddr", bhzCementBase.getJobLocation());
//            rwdDate.set("distance", bhzCementBase);
            rwdDate.set("part", bhzCementBase.getPoureLocation());
            rwdDate.set("pour", "自卸");
            rwdDate.set("betLevel", bhzCementBase.getStrengthRank());
            rwdDate.set("mixLast", bhzCementBase.getStirDatetime());
            rwdDate.set("prodTimb", sdf.format(bhzCementBase.getProductDatetime()));
            rwdDate.set("prodTime", sdf.format(bhzCementBase.getProductDatetime()));
            rwdDate.set("cementType", bhzCementBase.getCementVariety());
            rwdDate.set("mete", 0);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmssSSS");
            String time = sdf1.format(new Date());
            String sign = MD5Util.MD5Encode(userKey + secret + time, "utf-8");

            String result = HttpRequest.post(url1)
                    .header("userKey", userKey)
                    .header("time", time)
                    .header("sign", sign)
                    .body(String.valueOf(rwdDate))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(result);
            System.out.println(jsonObject1);


            JSONObject sendDate = new JSONObject();
            sendDate.set("prodId", "YKLC"+bhzCementBase.getId());    //	生产记录id。
            sendDate.set("serial", bhzCementBase.getId());    //	序列号。第几盘
            sendDate.set("blender", blender);    //	搅拌机id，平台提供
            sendDate.set("dattim", sdf.format(bhzCementBase.getProductDatetime()));    //	生产时刻（yyyy-MM-dd HH:mm:ss）
            sendDate.set("mixLast", bhzCementBase.getStirDatetime());    //	搅拌时长
            sendDate.set("pieAmnt", bhzCementBase.getEstimateNumber());    //	盘方量
            sendDate.set("lands", bhzCementBase.getSlump());    //	盘坍落度
            sendDate.set("temper", bhzCementBase.getChuliaowd());    //	盘温度
            sendDate.set("pieErr", "");    //	盘误差
            sendDate.set("data", "");    //	附加数据

            QueryWrapper<BhzCementDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("batch_no", bhzCementBase.getBatchNo());
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.list(queryWrapper);
            int gltype = 0;
            int sntype = 0;
            int fmhtype = 0;
            int kftype = 0;
            int wjjtype = 0;
            for (BhzCementDetail bhzCementDetail : bhzCementDetails) {
                Integer materialeType = bhzCementDetail.getMaterialeType();
                if (materialeType == 1) {
                    sendDate.set("gl1FactAmnt", bhzCementDetail.getRealityNumber());    //	骨料1实际用量，可为空
                    sendDate.set("gl1PlanAmnt", bhzCementDetail.getTheoryNumber());    //	骨料1理论用量，可为空
                }
                if (materialeType == 4 && 0 == gltype) {
                    gltype = 1;
                    sendDate.set("gl2FactAmnt", bhzCementDetail.getRealityNumber());    //	骨料2实际用量，可为空
                    sendDate.set("gl2PlanAmnt", bhzCementDetail.getTheoryNumber());    //	骨料2理论用量，可为空
                }
                if (materialeType == 4 && 1 == gltype) {
                    gltype = 2;
                    sendDate.set("gl3FactAmnt", bhzCementDetail.getRealityNumber());    //	骨料3实际用量，可为空
                    sendDate.set("gl3PlanAmnt", bhzCementDetail.getTheoryNumber());    //	骨料3理论用量，可为空
                }
                if (materialeType == 4 && 2 == gltype) {
                    gltype = 3;
                    sendDate.set("gl4FactAmnt", bhzCementDetail.getRealityNumber());    //	骨料4实际用量，可为空
                    sendDate.set("gl4PlanAmnt", bhzCementDetail.getTheoryNumber());    //	骨料4理论用量，可为空
                }
                if (materialeType == 4 && 3 == gltype) {
                    gltype = 4;
                    sendDate.set("gl5FactAmnt", bhzCementDetail.getRealityNumber());    //	骨料5实际用量，可为空
                    sendDate.set("gl5PlanAmnt", bhzCementDetail.getTheoryNumber());    //	骨料5理论用量，可为空
                }
                if (materialeType == 6 && 0 == sntype) {
                    sntype = 1;
                    sendDate.set("sn1FactAmnt", bhzCementDetail.getRealityNumber());    //	水泥1实际用量，可为空
                    sendDate.set("sn1PlanAmnt", bhzCementDetail.getTheoryNumber());    //	水泥1理论用量，可为空
                }
                if (materialeType == 6 && 1 == sntype) {
                    sntype = 2;
                    sendDate.set("sn2FactAmnt", bhzCementDetail.getRealityNumber());    //	水泥2实际用量，可为空
                    sendDate.set("sn2PlanAmnt", bhzCementDetail.getTheoryNumber());    //	水泥2理论用量，可为空
                }
                if (materialeType == 8 && 0 == fmhtype) {
                    fmhtype = 1;
                    sendDate.set("fmh1FactAmnt", bhzCementDetail.getRealityNumber());    //	粉煤灰1实际用量，可为空
                    sendDate.set("fmh1PlanAmnt", bhzCementDetail.getTheoryNumber());    //	粉煤灰1理论用量，可为空
                }
                if (materialeType == 8 && 1 == fmhtype) {
                    fmhtype = 2;
                    sendDate.set("fmh2FactAmnt", bhzCementDetail.getRealityNumber());    //	粉煤灰2实际用量，可为空
                    sendDate.set("fmh2PlanAmnt", bhzCementDetail.getTheoryNumber());    //	粉煤灰2理论用量，可为空
                }
                if (materialeType == 7 && 0 == kftype) {
                    kftype = 1;
                    sendDate.set("kf1FactAmnt", bhzCementDetail.getRealityNumber());    //	矿粉1实际用量，可为空
                    sendDate.set("kf1PlanAmnt", bhzCementDetail.getTheoryNumber());    //	矿粉1理论用量，可为空
                }
                if (materialeType == 7 && 1 == kftype) {
                    kftype = 2;
                    sendDate.set("kf2FactAmnt", bhzCementDetail.getRealityNumber());    //	矿粉2实际用量，可为空
                    sendDate.set("kf2PlanAmnt", bhzCementDetail.getTheoryNumber());    //	矿粉2理论用量，可为空
                }
                if (materialeType == 5) {
                    sendDate.set("shui1FactAmnt", bhzCementDetail.getRealityNumber());    //	水1实际用量，可为空
                    sendDate.set("shui1PlanAmnt", bhzCementDetail.getTheoryNumber());    //	水1理论用量，可为空
                }
                if (materialeType == 9) {
                    wjjtype = 1;
                    sendDate.set("wj1FactAmnt", bhzCementDetail.getRealityNumber());    //	外加剂1实际用量，可为空
                    sendDate.set("wj1PlanAmnt", bhzCementDetail.getTheoryNumber());    //	外加剂1理论用量，可为空
                }
                if (materialeType == 10 && wjjtype == 1) {
                    wjjtype = 2;
                    sendDate.set("wj2FactAmnt", bhzCementDetail.getRealityNumber());    //	外加剂1实际用量，可为空
                    sendDate.set("wj2PlanAmnt", bhzCementDetail.getTheoryNumber());    //	外加剂1理论用量，可为空
                }
                if (materialeType == 10 && wjjtype == 2) {
                    wjjtype = 3;
                    sendDate.set("wj3FactAmnt", bhzCementDetail.getRealityNumber());    //	外加剂1实际用量，可为空
                    sendDate.set("wj3PlanAmnt", bhzCementDetail.getTheoryNumber());    //	外加剂1理论用量，可为空
                }
            }

            Integer overLevel = bhzCementBase.getOverLevel();
            int isAlarm = overLevel >= 1 ? 1 : 0;
            sendDate.set("isAlarm", isAlarm);    //	是否预警（0：否，1：是）
            sendDate.set("alarmInfo", bhzCementBase.getOverReason());    //	预警信息

            List list = new ArrayList();
            list.add(sendDate);
            System.out.println(list);
            String result1 = HttpRequest.post(url)
                    .header("userKey", userKey)
                    .header("time", time)
                    .header("sign", sign)
                    .body(String.valueOf(list))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(result1);
            String success = jsonObject2.get("success").toString();
            if ("true".equals(success)) {
                log.info(String.format("六横拌合站推送中铁成功!" + id + "返回" + result1));
            } else {
                log.info(String.format("六横拌合站推送中铁失败!" + id + "Json数据" + result1));
            }
            sysConfigService.updateSysConfig(JobUtil.LH_BHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
