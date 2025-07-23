package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.safetyTunnelCarHistory.entity.SafetyTunnelCarHistory;
import com.trtm.iot.safetyTunnelCarHistory.service.ISafetyTunnelCarHistoryService;
import com.trtm.iot.safetyTunnelCarReal.entity.SafetyTunnelCarReal;
import com.trtm.iot.safetyTunnelCarReal.service.ISafetyTunnelCarRealService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName CLMJJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/8 16:10
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CLMJJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISafetyTunnelCarHistoryService safetyTunnelCarRealService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_CLMJ);//瑞苍车辆门禁
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞苍车辆门禁定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传瑞苍车辆门禁的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<SafetyTunnelCarHistory> safetyTunnelCarReals = safetyTunnelCarRealService.selectLists(shebeilist, curid);
        if (null == safetyTunnelCarReals || safetyTunnelCarReals.size() == 0) {
            log.info(String.format("暂无瑞苍车辆门禁未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (SafetyTunnelCarHistory safetyTunnelCarReal : safetyTunnelCarReals) {
            JSONObject sendObject = JSONUtil.createObj();
            id = safetyTunnelCarReal.getId();
            sendObject.set("card", safetyTunnelCarReal.getCard());
            sendObject.set("enterTime", safetyTunnelCarReal.getEnterTime());
            sendObject.set("outTime", safetyTunnelCarReal.getOutTime());
            sendObject.set("shebeino", safetyTunnelCarReal.getShebeino());
            sendObject.set("guid", safetyTunnelCarReal.getGuid());
            sendObject.set("lorr", safetyTunnelCarReal.getLorr());
            sendObject.set("ioflag", safetyTunnelCarReal.getIoflag());
            System.out.println(sendObject);
            String url = "https://zgj20.cncico.com/wlwpt/safetyTunnelCarHistory/safetyTunnelCarHistory/addOpen";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = (Integer) jsonObject1.get("code");
            if (codes == 200) {
                log.info(String.format("瑞苍车辆门禁推送成功!" + id + "Json数据" + sendObject));
            } else {
                log.info(String.format("瑞苍车辆门禁推送失败!" + id + "Json数据" + sendObject));
            }
            sysConfigService.updateSysConfig(JobUtil.RC_CLMJ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
