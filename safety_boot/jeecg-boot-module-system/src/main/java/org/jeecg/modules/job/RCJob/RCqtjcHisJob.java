package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.deviceTunnelEnvironmentdataReal.entity.DeviceTunnelEnvironmentdataReal;
import com.trtm.iot.deviceTunnelEnvironmentdataReal.service.IDeviceTunnelEnvironmentdataRealService;
import com.trtm.iot.devicetunnelenvironmentdata.entity.DeviceTunnelEnvironmentdata;
import com.trtm.iot.devicetunnelenvironmentdata.service.IDeviceTunnelEnvironmentdataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCqtjcHisJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceTunnelEnvironmentdataService deviceTunnelEnvironmentdataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCQTJC_HIS);//瑞仓气体监测历史数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓气体监测历史数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓气体监测历史数据推送的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceTunnelEnvironmentdata> list = deviceTunnelEnvironmentdataService.selectLists(shebeilist, curid);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓气体监测历史未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceTunnelEnvironmentdata deviceTunnelEnvironmentdata : list) {
            id = deviceTunnelEnvironmentdata.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("site",deviceTunnelEnvironmentdata.getSite());
            jsonObject1.set("shebeino",deviceTunnelEnvironmentdata.getShebeino());
            jsonObject1.set("dataTime",deviceTunnelEnvironmentdata.getDatatime());
            jsonObject1.set("alarmType",deviceTunnelEnvironmentdata.getAlarmtype());
            jsonObject1.set("oxygen",deviceTunnelEnvironmentdata.getOxygen());
            jsonObject1.set("methane",deviceTunnelEnvironmentdata.getMethane());
            jsonObject1.set("H_sulfide",deviceTunnelEnvironmentdata.getHSulfide());
            jsonObject1.set("C_dioxide",deviceTunnelEnvironmentdata.getCDioxide());
            jsonObject1.set("C_monoxide",deviceTunnelEnvironmentdata.getCMonoxide());
            jsonObject1.set("windspeed",deviceTunnelEnvironmentdata.getWindspeed());
            jsonObject1.set("uploadTime",deviceTunnelEnvironmentdata.getUploadtime());
            jsonObject1.set("temperature",deviceTunnelEnvironmentdata.getTemperature());
            jsonObject1.set("guid",deviceTunnelEnvironmentdata.getGuid());
            jsonObject1.set("dust",deviceTunnelEnvironmentdata.getDust());
            jsonObject1.set("noise",deviceTunnelEnvironmentdata.getNoise());
            jsonObject1.set("PM1",deviceTunnelEnvironmentdata.getPm1());
            jsonObject1.set("PM2",deviceTunnelEnvironmentdata.getPm2());
            jsonObject1.set("PM10",deviceTunnelEnvironmentdata.getPm10());
//            jsonObject1.set("warningCount",deviceTunnelEnvironmentdataReal.getwar);
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/anquan/tunnelEnvironmentdatas/environmentDataUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓气体监测历史数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCQTJC_HIS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else if(code == 201){
                log.info(String.format("瑞仓气体监测历史数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCQTJC_HIS, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else {
                log.info(String.format("瑞仓气体监测历史数据推送失败!" + id));
            }
        }
    }
}
