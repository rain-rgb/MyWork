package org.jeecg.modules.job.wbshebeijob;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wbshebeihistory.entity.WbshebeiHistory;
import com.trtm.iot.wbshebeihistory.service.IWbshebeiHistoryService;
import com.trtm.iot.wbshebeireal.entity.WbshebeiReal;
import com.trtm.iot.wbshebeireal.service.IWbshebeiRealService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.HttpRequestUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.spring.web.json.Json;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WbshebeiJob implements Job {
    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IWbshebeiRealService wbshebeiRealService;
    @Autowired
    private IWbshebeiHistoryService wbshebeiHistoryService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.WBSHEBEI);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到获取电子锁实时和历史数据定时任务配置信息" + DateUtils.now()));
            return;
        }
        String teidarr = "";
        String te_type = "hhd808";
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sbtype", 55);
        queryWrapper.eq("shebei_status",1);
        List<ShebeiInfo> list = shebeiInfoService.list(queryWrapper);
        if (list.size() > 0) {
            for (ShebeiInfo shebeiInfo : list) {
                if ("".equals(teidarr)) {
                    teidarr = shebeiInfo.getSbjno();
                } else {
                    teidarr = teidarr + "," + shebeiInfo.getSbjno();
                }
            }
        } else {
            return;
        }
        try{
            String token = jobUtil.getWbshebeiToken();
            if (token != null) {
                Map<String, String> param = new HashMap<>();
                param.put("token", token);
                param.put("te_type", te_type);
                param.put("teidarr", teidarr);
                JSONObject wbshebei = HttpRequestUtil.sendPost("http://gpselock.vip:80/tracklast/list", param);
                if ((int) wbshebei.get("code") == 0) {
                    String data1 = wbshebei.getString("data");
                    JSONArray data = JSONUtil.parseArray(data1);
                    if (data.size()>0){
                        for (Object object : data){
                            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(object);
                            String teid = (String) jsonObject.get("teid");
                            if (teid!=null) {
                                QueryWrapper<WbshebeiReal> queryWrapper1 = new QueryWrapper<>();
                                queryWrapper1.eq("teid", teid);
                                WbshebeiReal one = wbshebeiRealService.getOne(queryWrapper1);
                                String baseStation=(String) jsonObject.get("base_station");
                                Integer alarmState= ((Integer) jsonObject.get("alarm_state"));
                                //double battery_vol = (double) jsonObject.get("battery_vol");
                                Double battery_vol=Double.valueOf(String.valueOf((Object) jsonObject.get("battery_vol")));
                                String business_id = (String) jsonObject.get("business_id");
                                Integer car_state = (Integer) jsonObject.get("car_state");
                                Integer direction = (Integer) jsonObject.get("direction");
                                Integer fuel = (Integer) jsonObject.get("fuel");
                                Integer height = (Integer) jsonObject.get("height");
                                Integer humidity = (Integer) jsonObject.get("humidity");
                                Double lat = (Double) jsonObject.get("lat");
                                Double lng = (Double) jsonObject.get("lng");
                                Integer mileage = (Integer) jsonObject.get("mileage");
                                String  packet_type =(String) jsonObject.get("packet_type");
                                String sublock = (String) jsonObject.get("sublock");
                                String rfid = (String) jsonObject.get("rfid");
                                Integer temp = (Integer) jsonObject.get("temp");
                                Integer time = (Integer) jsonObject.get("time");
                                Integer te_state = (Integer) jsonObject.get("te_state");
                                long times = time;
                                Date date = new Date(times*1000);
                                if (one==null){
                                    WbshebeiReal wbshebeiReal = new WbshebeiReal();
                                    wbshebeiReal.setTeid(teid);
                                    wbshebeiReal.setAlarmState(Double.valueOf(alarmState));
                                    wbshebeiReal.setBaseStation(baseStation);
                                    wbshebeiReal.setBatteryVol(battery_vol);
                                    wbshebeiReal.setBusinessId(business_id);
                                    wbshebeiReal.setCarState(Double.valueOf(car_state));
                                    wbshebeiReal.setDirection(direction);
                                    wbshebeiReal.setFuel(Double.valueOf(fuel));
                                    wbshebeiReal.setHeight(Double.valueOf(height));
                                    wbshebeiReal.setHumidity(Double.valueOf(humidity));
                                    wbshebeiReal.setLat(String.valueOf(lat));
                                    wbshebeiReal.setLng(String.valueOf(lng));
                                    wbshebeiReal.setMileage(Double.valueOf(mileage));
                                    wbshebeiReal.setPacketType(packet_type);
                                    wbshebeiReal.setRfid(rfid);
                                    wbshebeiReal.setSublock(sublock);
                                    wbshebeiReal.setTemp(temp);
                                    wbshebeiReal.setTeState(Double.valueOf(te_state));
                                    wbshebeiReal.setTime(date);
                                    boolean save = wbshebeiRealService.save(wbshebeiReal);
                                    if(save){
                                        log.info(String.format("电子锁实时数据添加成功!!"+teid+"锁号"+ DateUtils.now()));
                                    }else{
                                        log.info(String.format("电子锁实时数据添加失败!!"+teid+"锁号"+ DateUtils.now()));
                                    }
                                }else {
                                    WbshebeiHistory wbshebeiHistory = new WbshebeiHistory();
                                    BeanUtils.copyProperties(one, wbshebeiHistory);
                                    boolean save = wbshebeiHistoryService.save(wbshebeiHistory);
                                    if(save){
                                        log.info(String.format("电子锁历史数据添加成功!!"+one.getTeid()+"锁号"+ DateUtils.now()));
                                    }else{
                                        log.info(String.format("电子锁历史数据添加失败!!"+one.getTeid()+"锁号"+ DateUtils.now()));
                                    }
                                    WbshebeiReal wbshebeiReal = new WbshebeiReal();
                                    wbshebeiReal.setTeid(teid);
                                    wbshebeiReal.setAlarmState(Double.valueOf(alarmState));
                                    wbshebeiReal.setBaseStation(baseStation);
                                    wbshebeiReal.setBatteryVol(battery_vol);
                                    wbshebeiReal.setBusinessId(business_id);
                                    wbshebeiReal.setCarState(Double.valueOf(car_state));
                                    wbshebeiReal.setDirection(direction);
                                    wbshebeiReal.setFuel(Double.valueOf(fuel));
                                    wbshebeiReal.setHeight(Double.valueOf(height));
                                    wbshebeiReal.setHumidity(Double.valueOf(humidity));
                                    wbshebeiReal.setLat(String.valueOf(lat));
                                    wbshebeiReal.setLng(String.valueOf(lng));
                                    wbshebeiReal.setMileage(Double.valueOf(mileage));
                                    wbshebeiReal.setPacketType(packet_type);
                                    wbshebeiReal.setRfid(rfid);
                                    wbshebeiReal.setSublock(sublock);
                                    wbshebeiReal.setTemp(temp);
                                    wbshebeiReal.setTeState(Double.valueOf(te_state));
                                    wbshebeiReal.setTime(date);
                                    wbshebeiReal.setId(one.getId());
                                    boolean b = wbshebeiRealService.updateById(wbshebeiReal);
                                    if(b){
                                        log.info(String.format("电子锁实时数据修改成功!!"+teid+"锁号"+ DateUtils.now()));
                                    }else{
                                        log.info(String.format("电子锁实时数据修改失败!!"+teid+"锁号"+ DateUtils.now()));
                                    }
                                }
                            }
                        }
                    }else {
                        log.info("暂无电子锁数据!");
                    }
                } else {
                    log.info("获取物镖平台电子锁数据失败!");
                }
            } else {
                log.info("获取物镖平台token失败!");
            }
            log.info("获取电子锁实时数据和历史数据定时任务"+DateUtils.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
