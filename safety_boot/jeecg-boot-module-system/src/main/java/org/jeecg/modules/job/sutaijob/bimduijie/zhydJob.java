package org.jeecg.modules.job.sutaijob.bimduijie;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicepowerhistorydata.entity.DevicePowerHistorydata;
import com.trtm.iot.devicepowerhistorydata.service.IDevicePowerHistorydataService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName zhydJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/11/6 13:31
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zhydJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicePowerHistorydataService devicePowerHistorydataService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "http://122.226.22.70:8867/prod-api/iot/STEQ";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_BIMZHYD);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台智慧用电定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台智慧用电的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DevicePowerHistorydata> devicePowerHistorydataList = devicePowerHistorydataService.selectListbim(shebeilist, curid);
        if (null == devicePowerHistorydataList || devicePowerHistorydataList.size() == 0) {
            log.info(String.format("暂无苏台智慧用电未推送数据" + DateUtils.now()));
            return;
        }else {
            log.info(String.format("苏台智慧用电开始推送！" + DateUtils.now()));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        for (DevicePowerHistorydata devicePowerHistorydata : devicePowerHistorydataList) {
            id = devicePowerHistorydata.getId();
            JSONObject sendBysDate = new JSONObject();
            sendBysDate.set("imei", devicePowerHistorydata.getImei());
            sendBysDate.set("powerdate", sdf.format(devicePowerHistorydata.getPowerdate()));
            sendBysDate.set("devicestatus", devicePowerHistorydata.getDevicestatus());
            sendBysDate.set("signal1", devicePowerHistorydata.getSignal1());
            sendBysDate.set("t1", devicePowerHistorydata.getT1());
            sendBysDate.set("t2", devicePowerHistorydata.getT2());
            sendBysDate.set("t3", devicePowerHistorydata.getT3());
            sendBysDate.set("t4", devicePowerHistorydata.getT4());
            sendBysDate.set("t5", devicePowerHistorydata.getT5());
            sendBysDate.set("t6", devicePowerHistorydata.getT6());
            sendBysDate.set("t7", devicePowerHistorydata.getT7());
            sendBysDate.set("t8", devicePowerHistorydata.getT8());
            sendBysDate.set("t9", devicePowerHistorydata.getT9());
            sendBysDate.set("t10", devicePowerHistorydata.getT10());
            sendBysDate.set("t11", devicePowerHistorydata.getT11());
            sendBysDate.set("t12", devicePowerHistorydata.getT12());
            sendBysDate.set("t13", devicePowerHistorydata.getT13());
            sendBysDate.set("t14", devicePowerHistorydata.getT14());
            sendBysDate.set("t15", devicePowerHistorydata.getT15());
            sendBysDate.set("t16", devicePowerHistorydata.getT16());
            sendBysDate.set("t17", devicePowerHistorydata.getT17());
            sendBysDate.set("t18", devicePowerHistorydata.getT18());
            sendBysDate.set("t19", devicePowerHistorydata.getT19());
            sendBysDate.set("t20", devicePowerHistorydata.getT20());
            sendBysDate.set("t21", devicePowerHistorydata.getT21());
            sendBysDate.set("t22", devicePowerHistorydata.getT22());
            sendBysDate.set("t23", devicePowerHistorydata.getT23());
            sendBysDate.set("t24", devicePowerHistorydata.getT24());
            sendBysDate.set("t25", devicePowerHistorydata.getT25());
            sendBysDate.set("t26", devicePowerHistorydata.getT26());
            sendBysDate.set("t27", devicePowerHistorydata.getT27());
            sendBysDate.set("t28", devicePowerHistorydata.getT28());
            sendBysDate.set("t29", devicePowerHistorydata.getT29());
            sendBysDate.set("t30", devicePowerHistorydata.getT30());
            sendBysDate.set("t31", devicePowerHistorydata.getT31());
            sendBysDate.set("t32", devicePowerHistorydata.getT32());
            sendBysDate.set("t33", devicePowerHistorydata.getT33());
            sendBysDate.set("t34", devicePowerHistorydata.getT34());
            sendBysDate.set("t35", devicePowerHistorydata.getT35());
            sendBysDate.set("t36", devicePowerHistorydata.getT36());
            sendBysDate.set("t37", devicePowerHistorydata.getT37());
            sendBysDate.set("t38", devicePowerHistorydata.getT38());
            sendBysDate.set("t39", devicePowerHistorydata.getT39());
            sendBysDate.set("t40", devicePowerHistorydata.getT40());
            sendBysDate.set("t41", devicePowerHistorydata.getT41());
            sendBysDate.set("t42", devicePowerHistorydata.getT42());
            sendBysDate.set("t43", devicePowerHistorydata.getT43());
            sendBysDate.set("t44", devicePowerHistorydata.getT44());
            sendBysDate.set("t45", devicePowerHistorydata.getT45());
            sendBysDate.set("t46", devicePowerHistorydata.getT46());
            sendBysDate.set("t47", devicePowerHistorydata.getT47());
            sendBysDate.set("t48", devicePowerHistorydata.getT48());
            sendBysDate.set("t49", devicePowerHistorydata.getT49());
            sendBysDate.set("t50", devicePowerHistorydata.getT50());
            sendBysDate.set("t51", devicePowerHistorydata.getT51());
            sendBysDate.set("t52", devicePowerHistorydata.getT52());
            sendBysDate.set("t53", devicePowerHistorydata.getT53());
            sendBysDate.set("t54", devicePowerHistorydata.getT54());
            sendBysDate.set("t55", devicePowerHistorydata.getT55());
            sendBysDate.set("t56", devicePowerHistorydata.getT56());
            sendBysDate.set("t57", devicePowerHistorydata.getT57());
            sendBysDate.set("t58", devicePowerHistorydata.getT58());
            sendBysDate.set("t59", devicePowerHistorydata.getT59());
            sendBysDate.set("t60", devicePowerHistorydata.getT60());
            sendBysDate.set("t61", devicePowerHistorydata.getT61());
            sendBysDate.set("t62", devicePowerHistorydata.getT62());

            JSONObject sendDate = new JSONObject();
            sendDate.set("iotType","智慧用电");
            sendDate.set("externalId",String.valueOf(id));
            sendDate.set("jsonData",sendBysDate);
            sendDate.set("deviceCode",devicePowerHistorydata.getImei());
            sendDate.set("createTime",sdf.format(devicePowerHistorydata.getPowerdate()));
            sendDate.set("warning",0);

            String result = HttpRequest.post(url)
                    .body(String.valueOf(sendDate))
                    .timeout(20000)
                    .execute()
                    .body();
            if (result.contains("200")){
                log.info("苏台智慧用电推送bim成功！");
            }else {
                log.info("苏台智慧用电推送bim失败！");
            }
            sysConfigService.updateSysConfig(JobUtil.ST_BIMZHYD,id);
        }
    }
}
