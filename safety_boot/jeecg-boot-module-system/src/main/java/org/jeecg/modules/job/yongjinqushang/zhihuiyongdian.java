package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.trtm.iot.devicepowerhistorydata.entity.DevicePowerHistorydata;
import com.trtm.iot.devicepowerhistorydata.service.IDevicePowerHistorydataService;
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
 * @ClassName zhihuiyongdian
 * @Author
 * @Date 2024/12/23 16:52
 * @Version
 * @Description
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zhihuiyongdian implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicePowerHistorydataService devicePowerHistorydataService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    private String url = "http://115.236.10.10:8081/zjjg-iot/v1/electric-data/saveElectric";
//    private String url = "http://121.41.26.120:8081/zjjg-iot-test/v1/electric-data/saveElectric";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJJG_ZHYD);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到湖州龙门吊定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输湖州龙门吊的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DevicePowerHistorydata> devicePowerHistorydatas = devicePowerHistorydataService.selectListtoJG(shebeilist, curid);
        if (null == devicePowerHistorydatas || devicePowerHistorydatas.size() == 0) {
            log.info(String.format("暂无湖州龙门吊的数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        for (DevicePowerHistorydata devicePowerHistorydatum : devicePowerHistorydatas) {
            id = devicePowerHistorydatum.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("time", devicePowerHistorydatum.getPowerdate());
            String imei = devicePowerHistorydatum.getImei();
            sendDate.set("deviceMac", imei);//设备编号
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(imei);
            sendDate.set("deviceName", sbjwd.getSbname());//设备名称
            sendDate.set("online", "1");//在线状态
            sendDate.set("orgCode", sbjwd.getTunnelId());//机构编码
            sendDate.set("orgName", sbjwd.getTunnelName());
            sendDate.set("lineNo", "0");//线路号
            sendDate.set("open", "");//当前开闸/合闸
            sendDate.set("temp", "0");//温度
            sendDate.set("vol", "0");//电压
            sendDate.set("current", "0");//电流
            sendDate.set("power", "0");//总功率
            sendDate.set("remainCurrent", "0");//剩余电流
            Integer devicestatus = devicePowerHistorydatum.getDevicestatus();
            if (devicestatus==2){
                sendDate.set("isAlarm", true);//是否故障
            }else {
                sendDate.set("isAlarm", false);
            }
            sendDate.set("aTemp", devicePowerHistorydatum.getT2()+"℃");//A相温度（需带单位）
            sendDate.set("aVol", devicePowerHistorydatum.getT9()+"V");//A相电压（需带单位）
            sendDate.set("aCurrent", devicePowerHistorydatum.getT6()+"A");//A相电流（需带单位）
            sendDate.set("bTemp", devicePowerHistorydatum.getT3()+"℃");//B相温度（需带单位）
            sendDate.set("bVol", devicePowerHistorydatum.getT10()+"V");//B相电压（需带单位）
            sendDate.set("bCurrent", devicePowerHistorydatum.getT7()+"A");//B相电流（需带单位）
            sendDate.set("cTemp", devicePowerHistorydatum.getT4()+"℃");//C相温度（需带单位）
            sendDate.set("cVol", devicePowerHistorydatum.getT11()+"V");//C相电压（需带单位）
            sendDate.set("cCurrent", devicePowerHistorydatum.getT8()+"A");//C相电流（需带单位）
            sendDate.set("nTemp", devicePowerHistorydatum.getT5()+"℃");//零线温度（需带单位）
            sendDate.set("nCurrent", "0");//零线电流（需带单位）
            sendDate.set("type", "");//类型
            sendDate.set("insertTime", sdf.format(devicePowerHistorydatum.getPowerdate()));//插入时间
            sendDate.set("cumulativeElectricity", "0");//累计用电量（需带单位）

            String jsonString = JSON.toJSONString(sendDate);
            List<String> list = new ArrayList<>();
            list.add(jsonString);
            String result = HttpRequest.post(url)
                    .header("client-code", "gaoxun")
                    .body(list.toString())
                    .timeout(25000)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), result);
            sysConfigService.updateSysConfig(JobUtil.ZJJG_ZHYD,id);
        }
    }
}