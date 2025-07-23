package org.jeecg.modules.job.sutaijob.bimduijie;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
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
 * @ClassName lmdJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/11/6 13:56
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lmdJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;

    private String url = "http://122.226.22.70:8867/prod-api/iot/STEQ";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_BIMLMD);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台龙门吊定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台龙门吊的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceCraneHistorydata> deviceCraneHistorydata = deviceCraneHistorydataService.selectListbim(shebeilist, curid);
        if (null == deviceCraneHistorydata || deviceCraneHistorydata.size() == 0) {
            log.info(String.format("暂无苏台龙门吊未推送数据！" + DateUtils.now()));
            return;
        }else {
            log.info(String.format("苏台龙门吊开始推送！" + DateUtils.now()));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        for (DeviceCraneHistorydata deviceCraneHistorydatum : deviceCraneHistorydata) {
            id = deviceCraneHistorydatum.getId();
            JSONObject sendBysDate = new JSONObject();
            sendBysDate.set("device_code", deviceCraneHistorydatum.getDeviceCode());
            sendBysDate.set("device_type", deviceCraneHistorydatum.getDeviceType());
            sendBysDate.set("big_carroute", deviceCraneHistorydatum.getBigCarroute());
            sendBysDate.set("cranedate", sdf.format(deviceCraneHistorydatum.getCranedate()));
            sendBysDate.set("main_hookheight", deviceCraneHistorydatum.getMainHookheight());
            sendBysDate.set("reserved_vicehookheight", deviceCraneHistorydatum.getReservedVicehookheight());
            sendBysDate.set("main_hookload", deviceCraneHistorydatum.getMainHookload());
            sendBysDate.set("reserved_vicehookload", deviceCraneHistorydatum.getReservedVicehookload());
            sendBysDate.set("small_carroute", deviceCraneHistorydatum.getSmallCarroute());
            sendBysDate.set("reserved_smallcarroute", deviceCraneHistorydatum.getReservedSmallcarroute());
            sendBysDate.set("wind_speed", deviceCraneHistorydatum.getWindSpeed());
            sendBysDate.set("reserved_analogquantityone", deviceCraneHistorydatum.getReservedAnalogquantityone());
            sendBysDate.set("reserved_analogquantitytwo", deviceCraneHistorydatum.getReservedAnalogquantitytwo());
            sendBysDate.set("main_hookstatus", deviceCraneHistorydatum.getMainHookstatus());
            sendBysDate.set("reserved_vicehookstatus", deviceCraneHistorydatum.getReservedVicehookstatus());
            sendBysDate.set("wind_speedalarm", deviceCraneHistorydatum.getWindSpeedalarm());
            sendBysDate.set("big_carleftlimit", deviceCraneHistorydatum.getBigCarleftlimit());
            sendBysDate.set("big_carrightlimit", deviceCraneHistorydatum.getBigCarrightlimit());
            sendBysDate.set("small_carfrontlimit", deviceCraneHistorydatum.getSmallCarfrontlimit());
            sendBysDate.set("small_carbacklimit", deviceCraneHistorydatum.getSmallCarbacklimit());
            sendBysDate.set("reserved_smallcarfrontlimit", deviceCraneHistorydatum.getReservedSmallcarfrontlimit());
            sendBysDate.set("reserved_smallcarbacklimit", deviceCraneHistorydatum.getReservedSmallcarbacklimit());
            sendBysDate.set("hook_uplimit", deviceCraneHistorydatum.getHookUplimit());
            sendBysDate.set("reserved_vicehookuplimit", deviceCraneHistorydatum.getReservedVicehookuplimit());
            sendBysDate.set("door_limit", deviceCraneHistorydatum.getDoorLimit());
            sendBysDate.set("wind_antiskidstatus", deviceCraneHistorydatum.getWindAntiskidstatus());
            sendBysDate.set("wire_drumstatus", deviceCraneHistorydatum.getWireDrumstatus());
            sendBysDate.set("Small_car1brake1", deviceCraneHistorydatum.getSmallCar1brake1());
            sendBysDate.set("Small_car1brake2", deviceCraneHistorydatum.getSmallCar1brake2());
            sendBysDate.set("Small_car2brake1", deviceCraneHistorydatum.getSmallCar2brake1());
            sendBysDate.set("Small_car2brake2", deviceCraneHistorydatum.getSmallCar2brake2());
            sendBysDate.set("big_carLegdeviation", deviceCraneHistorydatum.getBigCarlegdeviation());
            sendBysDate.set("All_time", deviceCraneHistorydatum.getAllTime());
            sendBysDate.set("All_times", deviceCraneHistorydatum.getAllTimes());
            sendBysDate.set("Bigcar_ALM", deviceCraneHistorydatum.getBigcarAlm());
            sendBysDate.set("reserved_one", deviceCraneHistorydatum.getReservedOne());
            sendBysDate.set("reserved_two", deviceCraneHistorydatum.getReservedTwo());
            sendBysDate.set("bigx", deviceCraneHistorydatum.getBigx());
            sendBysDate.set("bigy", deviceCraneHistorydatum.getBigy());
            sendBysDate.set("verticalx", deviceCraneHistorydatum.getVerticalx());
            sendBysDate.set("verticaly", deviceCraneHistorydatum.getVerticaly());
            sendBysDate.set("armangle", deviceCraneHistorydatum.getArmangle());
            sendBysDate.set("armlength", deviceCraneHistorydatum.getArmlength());
            sendBysDate.set("mainmoment", deviceCraneHistorydatum.getMainmoment());
            sendBysDate.set("vicemoment", deviceCraneHistorydatum.getVicemoment());
            sendBysDate.set("slr1", deviceCraneHistorydatum.getSlr1());
            sendBysDate.set("slr2", deviceCraneHistorydatum.getSlr2());
            sendBysDate.set("alertstate", deviceCraneHistorydatum.getAlertstate());
            sendBysDate.set("slralarm", deviceCraneHistorydatum.getSlralarm());
            sendBysDate.set("status", deviceCraneHistorydatum.getStatus());
            sendBysDate.set("main_vicealarm", deviceCraneHistorydatum.getMainVicealarm());
            sendBysDate.set("overval1_height1", deviceCraneHistorydatum.getOverval1Height1());
            sendBysDate.set("overval2_height2", deviceCraneHistorydatum.getOverval2Height2());
            sendBysDate.set("overval3_speed1", deviceCraneHistorydatum.getOverval3Speed1());
            sendBysDate.set("overval4_speed2", deviceCraneHistorydatum.getOverval4Speed2());

            JSONObject sendDate = new JSONObject();
            sendDate.set("iotType","龙门吊");
            sendDate.set("externalId",String.valueOf(id));
            sendDate.set("jsonData",sendBysDate);
            sendDate.set("deviceCode",deviceCraneHistorydatum.getDeviceCode());
            sendDate.set("createTime",sdf.format(deviceCraneHistorydatum.getCranedate()));
            sendDate.set("warning",deviceCraneHistorydatum.getAlertstate());

            String result = HttpRequest.post(url)
                    .body(String.valueOf(sendDate))
                    .timeout(20000)
                    .execute()
                    .body();
            if (result.contains("200")){
                log.info("苏台龙门吊推送bim成功！");
            }else {
                log.info("苏台龙门吊推送bim失败！");
            }
            sysConfigService.updateSysConfig(JobUtil.ST_BIMLMD,id);
        }
    }
}
