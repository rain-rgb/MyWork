package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.SignUtil;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName yjZJZLJob：数据推送甬台温的是32702
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/22 10:20
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HJJCJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicehistoryService devicehistoryService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_HJJC);//金仓湖互通项目环境监测设备定时任务
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到金仓湖互通项目环境监测设备定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Devicehistory> devicehistories = devicehistoryService.selectyjList(shebeilist, curid);
        if (null == devicehistories || devicehistories.size() == 0) {
            log.info(String.format("暂无金仓湖互通项目环境监测设备未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (Devicehistory devicehistory :devicehistories){
            Map<String,Object> map = new HashMap<>();
            if (devicehistory.getTsp() == null){
                map.put("tsp","0");
            }else {
                map.put("tsp",devicehistory.getTsp());
            }
            if (devicehistory.getAtmpressure() == null){
                map.put("pa","0");
            }else {
                map.put("pa",devicehistory.getAtmpressure());
            }
            if (devicehistory.getPm25() == null){
                map.put("pm25","0");
            }else {
                map.put("pm25",devicehistory.getPm25());
            }
            if (devicehistory.getTemperature() == null){
                map.put("tmp","0");
            }else {
                map.put("tmp",devicehistory.getTemperature());
            }
            if (devicehistory.getHumidity() == null){
                map.put("rh","0");
            }else {
                map.put("rh",devicehistory.getHumidity());
            }
            if (devicehistory.getPm10() == null){
                map.put("pm10","0");
            }else {
                map.put("pm10",devicehistory.getPm10());
            }
            if (devicehistory.getNoise() == null){
                map.put("zs","0");
            }else {
                map.put("zs",devicehistory.getNoise());
            }
            if (devicehistory.getWind() == null){
                map.put("wp","0");
            }else {
                map.put("wp",devicehistory.getWind());
            }
            if (devicehistory.getWindspeed() == null){
                map.put("ws","0");
            }else {
                map.put("ws",devicehistory.getWindspeed());
            }
            if (devicehistory.getWinddirection() == null){
                map.put("wd","0");
            }else {
                map.put("wd",devicehistory.getWinddirection());
            }
            JSONObject sendObject = JSONUtil.createObj();
            sendObject.set("devid", devicehistory.getDevaddr());
            sendObject.set("time", dateFormat.format(devicehistory.getTimevalue()));
            sendObject.set("data",map);
            System.out.println(sendObject);
            String url = "http://new.djzhgd.com:10724/gkxt-api/api/other/jcjz";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .header("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjU5LCJpYXQiOjE1NTg1ODA5OTU1MjQsImV4dCI6MTU1ODY2NzM5NTUyNH0.P2tDiaGvLkMoiEAkktUAnMSQvbP-_1mjAGupc5YIEWA")
                    .header("source", "djgk-hzlq")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(body);
            String codes = (String) jsonObject1.get("code");
            if (codes.equals("200")) {
                log.info(String.format("金仓湖互通项目环境监测设备推送成功!" + id + "Json数据" + sendObject));
                id = devicehistory.getId();
                sysConfigService.updateSysConfig(JobUtil.JTJT_HJJC, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("金仓湖互通项目环境监测设备推送失败!" + id + "Json数据" + sendObject));
            }
        }
    }
}
