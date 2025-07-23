package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
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
 * @Date 2022/8/8 14:28
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GZJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicePipepileHistorydataOneService devicePipepileHistorydataOneService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_GZ);//瑞苍管桩
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞苍管桩定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传瑞苍管桩的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DevicePipepileHistorydataOne> devicePipepileHistorydataOnes = devicePipepileHistorydataOneService.selectLists(shebeilist, curid);
        if (null == devicePipepileHistorydataOnes || devicePipepileHistorydataOnes.size() == 0) {
            log.info(String.format("暂无瑞苍管桩未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DevicePipepileHistorydataOne devicePipepileHistorydataOne : devicePipepileHistorydataOnes) {
            JSONObject sendObject = JSONUtil.createObj();
            id = devicePipepileHistorydataOne.getId();
            sendObject.set("shebeino", devicePipepileHistorydataOne.getShebeino());
            sendObject.set("pileNo", devicePipepileHistorydataOne.getPileNo());
            sendObject.set("pileRealdep", devicePipepileHistorydataOne.getPileRealdep());
            sendObject.set("pileWorktime", devicePipepileHistorydataOne.getPileWorktime());
            sendObject.set("pileY", devicePipepileHistorydataOne.getPileY());
            sendObject.set("pileTime", devicePipepileHistorydataOne.getPileTime());
            sendObject.set("pileStarttime", devicePipepileHistorydataOne.getPileStarttime());
            sendObject.set("pileUpress", devicePipepileHistorydataOne.getPileUpress());
            sendObject.set("pileDpress", devicePipepileHistorydataOne.getPileDpress());
            sendObject.set("pileSpeed", devicePipepileHistorydataOne.getPileSpeed());
            sendObject.set("times", devicePipepileHistorydataOne.getTimes());
            sendObject.set("pileDesigndep", devicePipepileHistorydataOne.getPileDesigndep());
            sendObject.set("rjrwd", devicePipepileHistorydataOne.getRjrwd());
            sendObject.set("uuid", devicePipepileHistorydataOne.getUuid());
            sendObject.set("pileMileage", devicePipepileHistorydataOne.getPileMileage());
            sendObject.set("datatime", devicePipepileHistorydataOne.getDatatime());
            sendObject.set("ts", devicePipepileHistorydataOne.getTs());
            sendObject.set("pileLgd", devicePipepileHistorydataOne.getPileLgd());
            sendObject.set("pileLtd", devicePipepileHistorydataOne.getPileLtd());
            System.out.println(sendObject);
            String url = "https://zgj20.cncico.com/wlwpt/devicepipepilehistorydataone/devicePipepileHistorydataOne/addOpen";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = (Integer) jsonObject1.get("code");
            if (codes == 200) {
                log.info(String.format("瑞苍管桩推送成功!" + id + "Json数据" + sendObject));
            } else {
                log.info(String.format("瑞苍管桩推送失败!" + id + "Json数据" + sendObject));
            }
            sysConfigService.updateSysConfig(JobUtil.RC_GZ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
