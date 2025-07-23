package org.jeecg.modules.job.sutaijob.bimduijie;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeHistorydata;
import com.trtm.iot.jiaqiaoji.service.IDeviceBridgeHistorydataService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName jqjJob：
 * @Description TODO
 * @Author 55314
 * @Date 2024/3/14 9:07
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class jqjJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IDeviceBridgeHistorydataService deviceBridgeHistorydataService;

    private String url = "http://122.226.22.70:8867/prod-api/iot/STEQ";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_BIMJQJ);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台架桥机定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台架桥机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceBridgeHistorydata> deviceBridgeHistorydata = deviceBridgeHistorydataService.selectJQJList(shebeilist, curid);
        if (null == deviceBridgeHistorydata || deviceBridgeHistorydata.size() == 0) {
            log.info(String.format("暂无苏台架桥机未推送数据！" + DateUtils.now()));
            return;
        }else {
            log.info(String.format("苏台架桥机开始推送！" + DateUtils.now()));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        for (DeviceBridgeHistorydata deviceBridgeHistorydatum : deviceBridgeHistorydata) {
            id = deviceBridgeHistorydatum.getId();
            JSONObject sendBysDate = new JSONObject();
            sendBysDate.set("device_code", deviceBridgeHistorydatum.getDeviceCode());
            sendBysDate.set("device_type", deviceBridgeHistorydatum.getDeviceType());
            sendBysDate.set("big_transverse_carroute", deviceBridgeHistorydatum.getBigTransverseCarroute());
            sendBysDate.set("bridgedate", sdf.format(deviceBridgeHistorydatum.getBridgedate()));
            sendBysDate.set("big_longitudinal_carroute", deviceBridgeHistorydatum.getBigLongitudinalCarroute());
            sendBysDate.set("verticality_leg", deviceBridgeHistorydatum.getVerticalityLeg());
            sendBysDate.set("car_levelness", deviceBridgeHistorydatum.getCarLevelness());
            sendBysDate.set("crane_height_1", deviceBridgeHistorydatum.getCraneHeight1());
            sendBysDate.set("crane_crane_1", deviceBridgeHistorydatum.getCraneCrane1());
            sendBysDate.set("transverse_carroute_1", deviceBridgeHistorydatum.getTransverseCarroute1());
            sendBysDate.set("longitudinal_carroute_1", deviceBridgeHistorydatum.getLongitudinalCarroute1());
            sendBysDate.set("hookstatus_1", deviceBridgeHistorydatum.getHookstatus1());
            sendBysDate.set("hookbrake1status_1", deviceBridgeHistorydatum.getHookbrake1status1());
            sendBysDate.set("hookbrake2status_1", deviceBridgeHistorydatum.getHookbrake2status1());
            sendBysDate.set("carleftlimit_1", deviceBridgeHistorydatum.getCarleftlimit1());
            sendBysDate.set("carrightlimit_1", deviceBridgeHistorydatum.getCarrightlimit1());
            sendBysDate.set("carfrontlimit_1", deviceBridgeHistorydatum.getCarfrontlimit1());
            sendBysDate.set("carbacklimit_1", deviceBridgeHistorydatum.getCarbacklimit1());
            sendBysDate.set("door_limit", deviceBridgeHistorydatum.getDoorLimit());
            sendBysDate.set("wind_antiskidstatus", deviceBridgeHistorydatum.getWindAntiskidstatus());
            sendBysDate.set("wire_drumstatus", deviceBridgeHistorydatum.getWireDrumstatus());
            sendBysDate.set("All_time", deviceBridgeHistorydatum.getAllTime());
            sendBysDate.set("All_times", deviceBridgeHistorydatum.getAllTimes());
            sendBysDate.set("reserved_one", deviceBridgeHistorydatum.getReservedOne());
            sendBysDate.set("reserved_two", deviceBridgeHistorydatum.getReservedTwo());
            sendBysDate.set("crane_height_2", deviceBridgeHistorydatum.getCraneHeight2());
            sendBysDate.set("crane_crane_2", deviceBridgeHistorydatum.getCraneCrane2());
            sendBysDate.set("transverse_carroute_2", deviceBridgeHistorydatum.getTransverseCarroute2());
            sendBysDate.set("longitudinal_carroute_2", deviceBridgeHistorydatum.getLongitudinalCarroute2());
            sendBysDate.set("hookstatus_2", deviceBridgeHistorydatum.getHookstatus2());
            sendBysDate.set("hookbrake1status_2", deviceBridgeHistorydatum.getHookbrake1status2());
            sendBysDate.set("hookbrake2status_2", deviceBridgeHistorydatum.getHookbrake2status2());
            sendBysDate.set("carleftlimit_2", deviceBridgeHistorydatum.getCarleftlimit2());
            sendBysDate.set("carrightlimit_2", deviceBridgeHistorydatum.getCarrightlimit2());
            sendBysDate.set("carfrontlimit_2", deviceBridgeHistorydatum.getCarfrontlimit2());
            sendBysDate.set("carbacklimit_2", deviceBridgeHistorydatum.getCarbacklimit2());

            Integer overval1Height1 = deviceBridgeHistorydatum.getOverval1Height1();
            Integer overval1Height2 = deviceBridgeHistorydatum.getOverval2Height2();
            Integer overval3Speed1 = deviceBridgeHistorydatum.getOverval3Speed1();
            Integer overval4Speed2 = deviceBridgeHistorydatum.getOverval4Speed2();
            Integer overval5Wind = deviceBridgeHistorydatum.getOverval5Wind();
            Integer overval6Verticality = deviceBridgeHistorydatum.getOverval6Verticality();
            int max = Integer.MIN_VALUE;
            int[] values = {overval1Height1, overval1Height2, overval3Speed1, overval4Speed2, overval5Wind, overval6Verticality};

            for (int value : values) {
                if (value > max) {
                    max = value;
                }
            }


            sendBysDate.set("overval1_height1", overval1Height1);
            sendBysDate.set("overval2_height2", overval1Height2);
            sendBysDate.set("overval3_speed1", overval3Speed1);
            sendBysDate.set("overval4_speed2", overval4Speed2);
            sendBysDate.set("overval5_wind", overval5Wind);
            sendBysDate.set("overval6_verticality", overval6Verticality);

            JSONObject sendDate = new JSONObject();
            sendDate.set("iotType","架桥机");
            sendDate.set("externalId",String.valueOf(id));
            sendDate.set("jsonData",sendBysDate);
            sendDate.set("deviceCode",deviceBridgeHistorydatum.getDeviceCode());
            sendDate.set("createTime",sdf.format(deviceBridgeHistorydatum.getBridgedate()));
            sendDate.set("warning",max);

            String result = HttpRequest.post(url)
                    .body(String.valueOf(sendDate))
                    .timeout(20000)
                    .execute()
                    .body();
            if (result.contains("200")){
                log.info("苏台架桥机推送bim成功！");
            }else {
                log.info("苏台架桥机推送bim失败！");
            }
            sysConfigService.updateSysConfig(JobUtil.ST_BIMJQJ,id);
        }
    }
}
