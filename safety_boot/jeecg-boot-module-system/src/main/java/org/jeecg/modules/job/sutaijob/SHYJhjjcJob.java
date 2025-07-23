package org.jeecg.modules.job.sutaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName SHYJhjjcJob：
 * @Description 上海有间环境检测数据推送
 * @Author 55314
 * @Date 2023/2/24 9:35
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SHYJhjjcJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicehistoryService devicehistoryService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "https://api.ilabx.cn/iot-service/interface/dltengyi/dustnoise";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SHYJ_HJJC);//上海有间环境检测数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到上海有间压浆的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输上海有间压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Devicehistory> devicehistories = devicehistoryService.selectlistToSHYJ(shebeilist, curid);
        if (null == devicehistories || devicehistories.size() == 0) {
            log.info(String.format("暂无上海有间压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (Devicehistory devicehistory : devicehistories){
            id = devicehistory.getId();

            JSONObject sendJson = new JSONObject();

            sendJson.set("code",devicehistory.getDevaddr());
            sendJson.set("pm10",devicehistory.getPm10());
            sendJson.set("pm25",devicehistory.getPm25());
            sendJson.set("noise",devicehistory.getNoise());
            sendJson.set("temperature",devicehistory.getTemperature());
            sendJson.set("humidity",devicehistory.getHumidity());
            sendJson.set("windPower",devicehistory.getWind());
            sendJson.set("windSpeed",devicehistory.getWindspeed());
            String wd = "0";
            String winddirection = devicehistory.getWinddirection();
            if ("北风".equals(winddirection)){
                wd = "0";
            }
            if ("东北风".equals(winddirection)){
                wd = "45";
            }
            if ("东风".equals(winddirection)){
                wd = "90";
            }
            if ("东南风".equals(winddirection)){
                wd = "135";
            }
            if ("南风".equals(winddirection)){
                wd = "180";
            }
            if ("西南风".equals(winddirection)){
                wd = "225";
            }
            if ("西风".equals(winddirection)){
                wd = "270";
            }
            if ("西北风".equals(winddirection)){
                wd = "315";
            }
            sendJson.set("wd",wd);
            sendJson.set("creatTime",devicehistory.getTimevalue());
            sendJson.set("tsp",devicehistory.getTsp());
            sendJson.set("airPressure",devicehistory.getAtmpressure());

            //推送
            String result = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendJson))
                    .execute()
                    .body();
            sysConfigService.updateSysConfig(JobUtil.SHYJ_HJJC, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
