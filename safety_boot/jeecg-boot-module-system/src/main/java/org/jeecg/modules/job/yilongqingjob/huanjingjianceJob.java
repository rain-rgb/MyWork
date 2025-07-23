package org.jeecg.modules.job.yilongqingjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName huanjingjianceJob
 * @Author
 * @Date 2025/3/6 17:03
 * @Version
 * @Description 义龙庆推送至云检
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class huanjingjianceJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDevicehistoryService devicehistoryService;

    private String url = "http://1.14.103.201:8401/IOTDataAcquisition/brid/EnvMonitorDataReception";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YLQ_HJJC);//甬金衢上拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义龙庆环境监测定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义龙庆环境监测的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Devicehistory> devicehistories = devicehistoryService.selectlistYLQ(shebeilist, curid);
        if (null == devicehistories || devicehistories.size() == 0) {
            log.info(String.format("暂无甬金衢上环境监测的数据" + DateUtils.now()));
            return;
        } else {
            log.info(String.format("义龙庆环境监测数据推送开始！" + DateUtils.now()));
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Devicehistory devicehistory : devicehistories) {
            List list = new ArrayList();
            id = devicehistory.getId();
            JSONObject sendObject = new JSONObject();

            sendObject.set("emId", id);
            sendObject.set("companyId", "2626ca43-5936-4cbc-a407-c3596a27e8e5");
            sendObject.set("companyName", "TJ05");
            String devaddr = devicehistory.getDevaddr();
            sendObject.set("deviceNum", "YLQLS-TJ05-BHZ-01");
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(devaddr);
            sendObject.set("deviceName", "义龙庆（丽水段）TJ05标1号拌合站");
            sendObject.set("collectionDates", sdf.format(devicehistory.getTimevalue()));
            sendObject.set("pm10", devicehistory.getPm10());
            sendObject.set("pm25", devicehistory.getPm25());
            sendObject.set("noise", devicehistory.getNoise());
            sendObject.set("temperature", devicehistory.getTemperature());
            sendObject.set("humidity", devicehistory.getHumidity());
            sendObject.set("windpower", devicehistory.getWind());
            sendObject.set("windspeed", devicehistory.getWindspeed());
            sendObject.set("windDirection", devicehistory.getWinddirection());
            sendObject.set("tsp", devicehistory.getTsp());
            sendObject.set("atmosphericPressure", devicehistory.getAtmpressure());
            sendObject.set("note", "");

            list.add(sendObject);
            System.out.println(list);
            String result = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(list))
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendObject), selectsysconfigone.getRemark(), result);
            if (result.contains("ok")) {
                log.info("义龙庆环境监测推送成功！");
                devicehistory.setIstuisong(1);
            } else {
                log.info("义龙庆环境监测推送失败！");
                devicehistory.setIstuisong(2);
            }
            devicehistoryService.updateById(devicehistory);
            sysConfigService.updateSysConfig(JobUtil.YLQ_HJJC, id);
        }
    }
}