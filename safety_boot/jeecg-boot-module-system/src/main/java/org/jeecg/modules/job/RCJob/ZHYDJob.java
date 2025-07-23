package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import com.trtm.iot.zhydreal.entity.DeviceElectricRealdata;
import com.trtm.iot.zhydreal.service.IDeviceElectricRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.SignUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ZHYDJob：
 * @Description TODO
 * @Author zml
 * @Date 2022/04/25 16:52
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ZHYDJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceElectricHistorydataService deviceElectricHistorydataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZHYF_RCRUISONG);//瑞仓内网智慧用电数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓智慧用电定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓智慧用电的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<DeviceElectricHistorydata> selecthntbhzones = deviceElectricHistorydataService.selectLists(strsToList1, curid);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无瑞仓智慧用电未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceElectricHistorydata deviceElectricHistorydata : selecthntbhzones) {
            JSONObject sendObject = JSONUtil.createObj();
            id = deviceElectricHistorydata.getId();
            sendObject.set("imei", deviceElectricHistorydata.getImei());
            sendObject.set("voltagea",deviceElectricHistorydata.getVoltagea());
            sendObject.set("voltageb",deviceElectricHistorydata.getVoltageb());
            sendObject.set("voltagec",deviceElectricHistorydata.getVoltagec());
            sendObject.set("frequencya",deviceElectricHistorydata.getFrequencya());
            sendObject.set("frequencyb",deviceElectricHistorydata.getFrequencyb());
            sendObject.set("frequencyc",deviceElectricHistorydata.getFrequencyc());
            sendObject.set("electricitya",deviceElectricHistorydata.getElectricitya());
            sendObject.set("electricityb",deviceElectricHistorydata.getElectricityb());
            sendObject.set("electricityc",deviceElectricHistorydata.getElectricityc());
            sendObject.set("seepelectricity",deviceElectricHistorydata.getSeepelectricity());
            sendObject.set("tempa",deviceElectricHistorydata.getTempa());
            sendObject.set("tempb",deviceElectricHistorydata.getTempb());
            sendObject.set("tempc",deviceElectricHistorydata.getTempc());
            sendObject.set("tempn",deviceElectricHistorydata.getTempn());
            sendObject.set("energya",deviceElectricHistorydata.getEnergya());
            sendObject.set("energyb",deviceElectricHistorydata.getEnergyb());
            sendObject.set("energyc",deviceElectricHistorydata.getEnergyc());
            sendObject.set("elecdate",deviceElectricHistorydata.getElecdate());
            sendObject.set("datatime",deviceElectricHistorydata.getDatatime());
            sendObject.set("status",deviceElectricHistorydata.getStatus());
            sendObject.set("alertstate",deviceElectricHistorydata.getAlertstate());
            sendObject.set("vaStatus",deviceElectricHistorydata.getVaStatus());
            sendObject.set("vbStatus",deviceElectricHistorydata.getVbStatus());
            sendObject.set("vcStatus",deviceElectricHistorydata.getVcStatus());
            sendObject.set("faStatus",deviceElectricHistorydata.getFaStatus());
            sendObject.set("fbStatus",deviceElectricHistorydata.getFbStatus());
            sendObject.set("fcStatus",deviceElectricHistorydata.getFcStatus());
            sendObject.set("eaStatus",deviceElectricHistorydata.getEaStatus());
            sendObject.set("ebStatus",deviceElectricHistorydata.getEbStatus());
            sendObject.set("ecStatus",deviceElectricHistorydata.getEcStatus());
            sendObject.set("seStatus",deviceElectricHistorydata.getSeStatus());
            sendObject.set("taStatus",deviceElectricHistorydata.getTaStatus());
            sendObject.set("tbStatus",deviceElectricHistorydata.getTbStatus());
            sendObject.set("tcStatus",deviceElectricHistorydata.getTcStatus());
            String url = "https://zgj20.cncico.com/wlwpt/zhydreal/deviceElectricRealdata/addOpen";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = (Integer) jsonObject1.get("code");
            if (codes == 200) {
                log.info(String.format("瑞仓智慧用电历史数据推送成功!" + id + "Json数据" + sendObject));
            } else {
                log.info(String.format("瑞仓智慧用电历史数据推送失败!" + id + "Json数据" + sendObject));
            }
            sysConfigService.updateSysConfig(JobUtil.ZHYF_RCRUISONG, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
