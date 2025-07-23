package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.safetyTunnelCarHistory.entity.SafetyTunnelCarHistory;
import com.trtm.iot.safetyTunnelCarHistory.service.ISafetyTunnelCarHistoryService;
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
public class RCclmjHisJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISafetyTunnelCarHistoryService safetyTunnelCarHistoryService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCCLMJ_HIS);//瑞仓车辆门禁历史数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓车辆门禁历史数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓车辆门禁历史数据推送的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<SafetyTunnelCarHistory> list = safetyTunnelCarHistoryService.selectLists(shebeilist, curid);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓车辆门禁未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (SafetyTunnelCarHistory safetyTunnelCarHistory : list){
            id = safetyTunnelCarHistory.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("card",safetyTunnelCarHistory.getCard());
            jsonObject1.set("enter_time",safetyTunnelCarHistory.getEnterTime());
            jsonObject1.set("out_time",safetyTunnelCarHistory.getOutTime());
            jsonObject1.set("shebeino",safetyTunnelCarHistory.getShebeino());
            jsonObject1.set("guid",safetyTunnelCarHistory.getGuid());
            jsonObject1.set("lorr",safetyTunnelCarHistory.getLorr());
            jsonObject1.set("ioflag",safetyTunnelCarHistory.getIoflag());
            jsonObject1.set("TagId",safetyTunnelCarHistory.getTagid());
            jsonObject1.set("Type",safetyTunnelCarHistory.getType());
            jsonObject1.set("Owner",safetyTunnelCarHistory.getOwner());
            jsonObject1.set("TunnelName",safetyTunnelCarHistory.getTunnelname());
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/anquan/tunnelCarHistorys/tunnelCarHistoryUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓车辆门禁历史数据推送成功!" ));
                sysConfigService.updateSysConfig(JobUtil.RCCLMJ_HIS, id);//根据传过来的唯一值来修改当前判断到的数据id

            }else if(code == 402){
                log.info(String.format("瑞仓车辆门禁历史数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCCLMJ_HIS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓车辆门禁历史数据推送失败!" ));
            }
        }
    }
}
