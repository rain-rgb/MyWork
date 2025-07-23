package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicetunnelenvironmentdata.entity.DeviceTunnelEnvironmentdata;
import com.trtm.iot.devicetunnelenvironmentdata.service.IDeviceTunnelEnvironmentdataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Shigongphb;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName QTJCJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/7/11 16:07
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QTJCJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceTunnelEnvironmentdataService deviceTunnelEnvironmentdataService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_QTJC);//瑞仓内网智慧用电数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓气体检测定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓气体检测的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceTunnelEnvironmentdata> deviceTunnelEnvironmentdatas = deviceTunnelEnvironmentdataService.selectLists(shebeilist, curid);
        if (null == deviceTunnelEnvironmentdatas || deviceTunnelEnvironmentdatas.size() == 0) {
            log.info(String.format("暂无瑞仓气体检测未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceTunnelEnvironmentdata deviceTunnelEnvironmentdata : deviceTunnelEnvironmentdatas) {
            JSONObject sendObject = JSONUtil.createObj();
            id = deviceTunnelEnvironmentdata.getId();
            sendObject.set("site", deviceTunnelEnvironmentdata.getSite());
            sendObject.set("shebeino", deviceTunnelEnvironmentdata.getShebeino());
            sendObject.set("datatime", deviceTunnelEnvironmentdata.getDatatime());
            sendObject.set("alarmType", deviceTunnelEnvironmentdata.getAlarmtype());
            sendObject.set("oxygen", deviceTunnelEnvironmentdata.getOxygen());
            sendObject.set("methane", deviceTunnelEnvironmentdata.getMethane());
            sendObject.set("hsulfide", deviceTunnelEnvironmentdata.getHSulfide());
            sendObject.set("cdioxide", deviceTunnelEnvironmentdata.getCDioxide());
            sendObject.set("cmonoxide", deviceTunnelEnvironmentdata.getCMonoxide());
            sendObject.set("windspeed", deviceTunnelEnvironmentdata.getWindspeed());
            sendObject.set("uploadTime", deviceTunnelEnvironmentdata.getUploadtime());
            sendObject.set("temperature", deviceTunnelEnvironmentdata.getTemperature());
            sendObject.set("guid", deviceTunnelEnvironmentdata.getGuid());
            String url = "https://zgj20.cncico.com/wlwpt/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/addOpen";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = (Integer) jsonObject1.get("code");
            if (codes == 200) {
                log.info(String.format("瑞仓气体检测推送成功!" + id + "Json数据" + sendObject));
            } else {
                log.info(String.format("瑞仓气体检测推送失败!" + id + "Json数据" + sendObject));
            }
            sysConfigService.updateSysConfig(JobUtil.RC_QTJC, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
