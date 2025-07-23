package org.jeecg.modules.job.sutaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicepowerhistorydata.entity.DevicePowerHistorydata;
import com.trtm.iot.devicepowerhistorydata.service.IDevicePowerHistorydataService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName SHYJzhydJob：
 * @Description 上海有间智慧用电数据推送
 * @Author 55314
 * @Date 2023/2/24 10:51
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SHYJzhydJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicePowerHistorydataService devicePowerHistorydataService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "https://api.ilabx.cn/iot-service/interface/electricityMonitoring/saveElectricityMonitoring";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SHYJ_ZHYD);//上海有间智慧用电数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到上海有间智慧用电的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输上海有间智慧用电的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DevicePowerHistorydata> deviceElectricHistorydata = devicePowerHistorydataService.selectListToSHYJ(shebeilist, curid);
        if (null == deviceElectricHistorydata || deviceElectricHistorydata.size() == 0) {
            log.info(String.format("暂无上海有间龙门吊的数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id=0;
        for (DevicePowerHistorydata electricHistorydata : deviceElectricHistorydata){
            id= electricHistorydata.getId();

            JSONObject sendJson = new JSONObject();

            sendJson.set("deviceId",electricHistorydata.getImei());
            sendJson.set("creatTime",electricHistorydata.getPowerdate());
            sendJson.set("deviceStatus",electricHistorydata.getDevicestatus());
            sendJson.set("signal",electricHistorydata.getSignal1());
            sendJson.set("voltageA",electricHistorydata.getT1());
            sendJson.set("voltageB",electricHistorydata.getT2());
            sendJson.set("voltageC",electricHistorydata.getT3());
            sendJson.set("frequencyA",electricHistorydata.getT4());
            sendJson.set("frequencyB",electricHistorydata.getT5());
            sendJson.set("frequencyC",electricHistorydata.getT6());
            sendJson.set("currentA",electricHistorydata.getT7());
            sendJson.set("currentB",electricHistorydata.getT8());
            sendJson.set("currentC",electricHistorydata.getT9());
            sendJson.set("leakageCurrent",electricHistorydata.getT10());
            sendJson.set("temperatureA",electricHistorydata.getT11());
            sendJson.set("temperatureB",electricHistorydata.getT12());
            sendJson.set("temperatureC",electricHistorydata.getT13());
            sendJson.set("temperatureN",electricHistorydata.getT14());
            sendJson.set("voltagePhaseAngleA",electricHistorydata.getT15());
            sendJson.set("voltagePhaseAngleB",electricHistorydata.getT16());
            sendJson.set("voltagePhaseAngleC",electricHistorydata.getT17());
            sendJson.set("currentPhaseAngleA",electricHistorydata.getT18());
            sendJson.set("currentPhaseAngleB",electricHistorydata.getT19());
            sendJson.set("currentPhaseAngleC",electricHistorydata.getT20());
            sendJson.set("powerFactorA",electricHistorydata.getT21());
            sendJson.set("powerFactorB",electricHistorydata.getT22());
            sendJson.set("powerFactorC",electricHistorydata.getT23());
            sendJson.set("combinedPowerFactor",electricHistorydata.getT24());
            sendJson.set("activePowerA",electricHistorydata.getT25());
            sendJson.set("activePowerB",electricHistorydata.getT26());
            sendJson.set("activePowerC",electricHistorydata.getT27());
            sendJson.set("combinedActivePower",electricHistorydata.getT28());
            sendJson.set("reactivePowerA",electricHistorydata.getT29());
            sendJson.set("reactivePowerB",electricHistorydata.getT30());
            sendJson.set("reactivePowerC",electricHistorydata.getT31());
            sendJson.set("combinedReactivePower",electricHistorydata.getT32());
            sendJson.set("apparentPowerA",electricHistorydata.getT33());
            sendJson.set("apparentPowerB",electricHistorydata.getT34());
            sendJson.set("apparentPowerC",electricHistorydata.getT35());
            sendJson.set("combinedApparentPower",electricHistorydata.getT36());
            sendJson.set("highActivePowerA",electricHistorydata.getT37());
            sendJson.set("lowActiveEnergyA",electricHistorydata.getT38());
            sendJson.set("highActivePowerB",electricHistorydata.getT39());
            sendJson.set("lowActiveEnergyB",electricHistorydata.getT40());
            sendJson.set("highActivePowerC",electricHistorydata.getT41());
            sendJson.set("lowActiveEnergyC",electricHistorydata.getT42());
            sendJson.set("combineHighActivePower",electricHistorydata.getT43());
            sendJson.set("combineLowActiveEnergy",electricHistorydata.getT44());
            sendJson.set("highReactivePowerA",electricHistorydata.getT45());
            sendJson.set("reactiveEnergyLowA",electricHistorydata.getT46());
            sendJson.set("highReactivePowerB",electricHistorydata.getT47());
            sendJson.set("reactiveEnergyLowB",electricHistorydata.getT48());
            sendJson.set("highReactivePowerC",electricHistorydata.getT49());
            sendJson.set("reactiveEnergyLowC",electricHistorydata.getT50());
            sendJson.set("combineHighReactivePower",electricHistorydata.getT51());
            sendJson.set("combineReactiveEnergyLow",electricHistorydata.getT52());
            sendJson.set("apparentHighPowerA",electricHistorydata.getT53());
            sendJson.set("apparentLowPowerA",electricHistorydata.getT54());
            sendJson.set("apparentHighPowerB",electricHistorydata.getT55());
            sendJson.set("apparentLowPowerB",electricHistorydata.getT56());
            sendJson.set("apparentHighPowerC",electricHistorydata.getT57());
            sendJson.set("apparentLowPowerC",electricHistorydata.getT58());
            sendJson.set("combineApparentHighPower",electricHistorydata.getT59());
            sendJson.set("combineApparentLowPower",electricHistorydata.getT60());
            sendJson.set("currentBalance",electricHistorydata.getT61());
            sendJson.set("voltageBalance",electricHistorydata.getT62());


            //推送
            String result = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendJson))
                    .execute()
                    .body();
            sysConfigService.updateSysConfig(JobUtil.SHYJ_ZHYD, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
