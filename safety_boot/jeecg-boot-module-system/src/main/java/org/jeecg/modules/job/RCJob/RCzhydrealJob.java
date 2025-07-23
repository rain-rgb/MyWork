package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import com.trtm.iot.zhydreal.service.IDeviceElectricRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;



@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCzhydrealJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceElectricRealdataService deviceElectricRealdataService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCZHYL_REAL);//瑞仓智慧用电实时数据
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓智慧用电实时数据定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓智慧用电实时数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceElectricHistorydata> list = deviceElectricRealdataService.selectRealList(curid,shebeilist);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓智慧用电实时未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceElectricHistorydata deviceElectricHistorydata : list){
            id = deviceElectricHistorydata.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("imei",deviceElectricHistorydata.getImei());
            jsonObject1.set("voltagea",deviceElectricHistorydata.getVoltagea());
            jsonObject1.set("voltageb",deviceElectricHistorydata.getVoltageb());
            jsonObject1.set("voltagec",deviceElectricHistorydata.getVoltagec());
            jsonObject1.set("frequencya",deviceElectricHistorydata.getFrequencya());
            jsonObject1.set("frequencyb",deviceElectricHistorydata.getFrequencyb());
            jsonObject1.set("frequencyc",deviceElectricHistorydata.getFrequencyc());
            jsonObject1.set("electricitya",deviceElectricHistorydata.getElectricitya());
            jsonObject1.set("electricityb",deviceElectricHistorydata.getElectricityb());
            jsonObject1.set("electricityc",deviceElectricHistorydata.getElectricityc());
            jsonObject1.set("seepelectricity",deviceElectricHistorydata.getSeepelectricity());
            jsonObject1.set("tempa",deviceElectricHistorydata.getTempa());
            jsonObject1.set("tempb",deviceElectricHistorydata.getTempb());
            jsonObject1.set("tempc",deviceElectricHistorydata.getTempc());
            jsonObject1.set("tempn",deviceElectricHistorydata.getTempn());
            jsonObject1.set("energya",deviceElectricHistorydata.getEnergya());
            jsonObject1.set("energyb",deviceElectricHistorydata.getEnergyb());
            jsonObject1.set("energyc",deviceElectricHistorydata.getEnergyc());
            jsonObject1.set("elecdate",deviceElectricHistorydata.getElecdate());
            jsonObject1.set("dataTime",deviceElectricHistorydata.getDatatime());
            jsonObject1.set("status",deviceElectricHistorydata.getStatus());
            jsonObject1.set("alertstate",deviceElectricHistorydata.getAlertstate());
            jsonObject1.set("va_status",deviceElectricHistorydata.getVaStatus());
            jsonObject1.set("vb_status",deviceElectricHistorydata.getVbStatus());
            jsonObject1.set("vc_status",deviceElectricHistorydata.getVcStatus());
            jsonObject1.set("fa_status",deviceElectricHistorydata.getFaStatus());
            jsonObject1.set("fb_status",deviceElectricHistorydata.getFbStatus());
            jsonObject1.set("fc_status",deviceElectricHistorydata.getFcStatus());
            jsonObject1.set("ea_status",deviceElectricHistorydata.getEaStatus());
            jsonObject1.set("eb_status",deviceElectricHistorydata.getEbStatus());
            jsonObject1.set("ec_status",deviceElectricHistorydata.getEcStatus());
            jsonObject1.set("se_status",deviceElectricHistorydata.getSeStatus());
            jsonObject1.set("ta_status",deviceElectricHistorydata.getTaStatus());
            jsonObject1.set("tb_status",deviceElectricHistorydata.getTbStatus());
            jsonObject1.set("tc_status",deviceElectricHistorydata.getTcStatus());
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/anquan/electricHistorydatas/electricityHistoryUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓智慧用电实时数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCZHYL_REAL, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else if(code == 402){
                log.info(String.format("瑞仓智慧用电实时数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCZHYL_REAL, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else {
                log.info(String.format("瑞仓智慧用电实时数据推送失败!" + id));
            }
        }
    }
}

