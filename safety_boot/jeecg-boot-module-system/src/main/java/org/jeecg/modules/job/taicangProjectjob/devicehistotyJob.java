package org.jeecg.modules.job.taicangProjectjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \* @author: zml
 * \* Date: 2022/07/28
 * \* Time: 17:30
 * \* Description:  256太仓环境监测数据推送
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class devicehistotyJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicehistoryService devicehistoryService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.TAICANG_DEVICE);//环境监测数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到太仓环境监测数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info("没有配置要传输环境监测的设备" + DateUtils.now());
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Devicehistory> devicehistoryList = devicehistoryService.selectlistdata(shebeilist,curid);
        if (null == devicehistoryList || devicehistoryList.size() == 0) {
            log.info("暂无太仓环境监测未推送数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (Devicehistory devicehistory:devicehistoryList){
            JSONObject sendObject = JSONUtil.createObj();
            id = devicehistory.getId();
            sendObject.set("devid",devicehistory.getDevaddr());
            Map<String,Object>map = new HashMap<>();
            map.put("pm25",devicehistory.getPm25());
            map.put("tmp",devicehistory.getTemperature());
            map.put("rh",devicehistory.getHumidity());
            map.put("pm10",devicehistory.getPm10());
            map.put("zs",devicehistory.getNoise());
            map.put("wp",devicehistory.getWind());
            map.put("ws",devicehistory.getWindspeed());
            map.put("wd",devicehistory.getWinddirection());
            sendObject.set("data",map);
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1NTY2MDAyNzYwODQsInVpZCI6MSwiaWF0IjoxNTU2NTEzODc2MDg0fQ.J034Wc5GgMTbLOsWGEp3qGnxSjf_IZHQZmv1kf2Gkfk";
            String source = "gkxt-api";
            String url = "http://new.djzhgd.com:10724/gkxt-api/api/other/jcjz";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .header("token",token)
                    .header("source",source)
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            String codes = (String) jsonObject1.get("code");
            if ("200".equals(codes)) {
                log.info("太仓环境监测数据推送成功!" + id + "Json数据" + sendObject + DateUtils.now());
                sysConfigService.updateSysConfig(JobUtil.TAICANG_DEVICE, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info("太仓环境监测数据推送失败!" + id + "Json数据" + sendObject+ DateUtils.now());
            }
        }
    }
}
