package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
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
import java.util.Date;
import java.util.List;

/**
 * @ClassName lmdJob
 * @Author
 * @Date 2024/12/23 9:26
 * @Version
 * @Description
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lmdJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    private String url = "http://115.236.10.10:8081/zjjg-iot/crane/saveCraneData";
//    private String url = "http://121.41.26.120:8081/zjjg-iot-test/crane/saveCraneData";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJJG_LMD);
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
        List<DeviceCraneHistorydata> deviceCraneHistorydatas = deviceCraneHistorydataService.selectListtoJG(shebeilist, curid);
        if (null == deviceCraneHistorydatas || deviceCraneHistorydatas.size() == 0) {
            log.info(String.format("暂无湖州龙门吊的数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        for (DeviceCraneHistorydata deviceCraneHistorydatum : deviceCraneHistorydatas) {
            id = deviceCraneHistorydatum.getId();
            JSONObject sendDate = new JSONObject();
            String deviceCode = deviceCraneHistorydatum.getDeviceCode();
            sendDate.set("equipmentCode", deviceCode);
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(deviceCode);
            sendDate.set("orgCode", sbjwd.getTunnelId());
            sendDate.set("orgName", sbjwd.getTunnelName());
            sendDate.set("equipmentName", sbjwd.getSbname());
            sendDate.set("onlineState", 1);
            Date cranedate = deviceCraneHistorydatum.getCranedate();
            long timestamp = cranedate.getTime() / 1000;
            sendDate.set("uploadTime", timestamp);
            sendDate.set("manufacturer", sbjwd.getMillname());
            sendDate.set("client", sbjwd.getMillname());
            sendDate.set("mainhook1Load", deviceCraneHistorydatum.getMainHookheight());
            sendDate.set("mainhook1Depth", 0);
            sendDate.set("mainhook1CycCnt", 0);
            sendDate.set("mainhook1Speed", 0);
            sendDate.set("crab1Speed", 0);
            sendDate.set("crab1Distance", deviceCraneHistorydatum.getSmallCarroute());
            sendDate.set("mainhook2Load", deviceCraneHistorydatum.getReservedVicehookload());
            sendDate.set("mainhook2Depth", 0);
            sendDate.set("mainhook2CycCnt", 0);
            sendDate.set("mainhook2Speed", 0);
            sendDate.set("crab2Speed", 0);
            sendDate.set("crab2Distance", deviceCraneHistorydatum.getReservedSmallcarroute());
            sendDate.set("craneDistance", deviceCraneHistorydatum.getBigCarroute());
            sendDate.set("craneSpeed", deviceCraneHistorydatum.getReservedAnalogquantityone());
            sendDate.set("craneSkew", 0);
            sendDate.set("windSpeed", deviceCraneHistorydatum.getWindSpeed());
            sendDate.set("windLevel", 0);
            sendDate.set("lat", "/");
            sendDate.set("lng", "/");
            sendDate.set("ljczcs", 0);
            sendDate.set("ljkfzycs", 0);
            sendDate.set("gzsc", 0);
            sendDate.set("dzcs", deviceCraneHistorydatum.getAllTimes());
            sendDate.set("tggdxx", 0);
            sendDate.set("tggdsx", deviceCraneHistorydatum.getHookUplimit());
            sendDate.set("xcxcsx", 0);
            System.out.println(sendDate);
            String result = HttpRequest.post(url)
                    .header("client-code", "gaoxun")
                    .body(String.valueOf(sendDate))
                    .timeout(25000)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), result);
            sysConfigService.updateSysConfig(JobUtil.ZJJG_LMD,id);
        }
    }
}