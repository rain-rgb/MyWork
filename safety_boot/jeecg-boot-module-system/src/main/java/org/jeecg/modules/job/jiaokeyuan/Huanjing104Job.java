package org.jeecg.modules.job.jiaokeyuan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiyuesuo.sample.api.utils.crypt.MD5;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * @ClassName huangjingJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/11/4 14:34
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class Huanjing104Job implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicehistoryService devicehistoryService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private ISysDictItemService sysDictItemService;

    private static final HashMap<String, Integer> windDirection = new HashMap<String, Integer>() {{

        put("北风", 0);
        put("东北风", 1);
        put("东风", 2);
        put("东南风", 3);
        put("南风", 4);
        put("西南风", 5);
        put("西风", 6);
        put("西北风", 7);
         }};

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SY_HJJC_JKY);//柯诸环境检测
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到环境104推送交科院配置信息" + DateUtils.now()));
            return;
        }

        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置环境104推送交科院的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        //环境检测主表
        List<Devicehistory> selectlistdata = devicehistoryService.selectlistdatatop1(shebeilist,curid);
        if (null == selectlistdata || selectlistdata.size() == 0) {
            log.info(String.format("暂无环境104推送交科院未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String userKey = "100036";
        String secret = "4023nia8ch3q9hc89hxnoqihdc8q9ncc";
        String timestamp = sdf.format(new Date());
        String signature = MD5.toMD5(userKey + secret + timestamp );


        for (Devicehistory devicehistory : selectlistdata) {
            id = devicehistory.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("deviceId", devicehistory.getDevaddr()); // String	设备ID
            sendDate.set("dateTime", devicehistory.getTimevalue()); // Date	时间 （yyyy-MM-dd HH:mm:ss）
            sendDate.set("pm25", devicehistory.getPm25()); // Double	pm25
            sendDate.set("pm10", devicehistory.getPm10()); // string	pm10
            sendDate.set("tsp", devicehistory.getTsp()); // Double	总悬浮微粒
            sendDate.set("noise", devicehistory.getNoise()); // Double	噪声
            sendDate.set("temperature", devicehistory.getTemperature()); // Double	温度
            sendDate.set("humidity", devicehistory.getHumidity()); // Double	湿度
            sendDate.set("windSpeed", devicehistory.getWindspeed()); // String	风速
            sendDate.set("windDirection", windDirection.get(devicehistory.getWinddirection()) ); // Double	分向 8 方位，取值 0-7,0 表示北风，1 东北风
            sendDate.set("windPower", devicehistory.getWindspeed()); // Double	风力
            sendDate.set("pressure", devicehistory.getAtmpressure()); // Double	大气压
            sendDate.set("live", "在线");// String	是否在线
//            List sendList = new ArrayList();
//            sendList.add(sendDate);
            String body = HttpRequest.post("http://122.224.169.83:9888/iot-service/api/environment/realtime/add")
                    .header("Content-Type", "application/json")
                    .header("userKey",userKey)
                    .header("time",timestamp)
                    .header("sign",signature)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            JSONObject jsonObject1 = new JSONObject(body);
//            Boolean success = (Boolean) jsonObject.get("success");
//            if (success) {
//                log.info(String.format("环境104推送交科院推送成功!" + id + "Json数据" + sendDate));
//            } else {
//                log.info(String.format("环境104推送交科院推送失败!" + id + "Json数据" + sendDate));
//            }
            sysConfigService.updateSysConfig(JobUtil.SY_HJJC_JKY, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), String.valueOf(jsonObject1));
        }
    }

}
