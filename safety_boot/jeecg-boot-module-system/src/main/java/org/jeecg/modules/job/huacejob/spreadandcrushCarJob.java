package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_machine.service.IHcMachineService;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import com.trtm.iot.hc_truck.entity.HcTruck;
import com.trtm.iot.hc_truck.service.IHcTruckService;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import com.trtm.iot.spreadandcrush_car.entity.SpreadandcrushCar;
import com.trtm.iot.spreadandcrush_car.service.ISpreadandcrushCarService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName spreadandcrushCarJob：
 * @Description 运输轨迹
 * @Author 55314
 * @Date 2023/5/15 10:19
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class spreadandcrushCarJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/transport/hisLoc.shtml";
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcTruckService truckService;
    @Autowired
    private ISpreadandcrushCarService spreadandcrushCarService;

    /**
     * 根据经纬度计算两点之间的距离
     * @param lat1 第一点纬度
     * @param lng1 第一点经度
     * @param lat2 第二点纬度
     * @param lng2 第二点经度
     * @return 两点距离（单位：米）
     */
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);

        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s *= 6378137.0; // 地球半径

        return s * 60;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //获取所有设备
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<HcTruck> list = truckService.list();
        for (HcTruck hcTruck : list) {
            String truckId = hcTruck.getTruckid();
            String pjid = hcTruck.getPjid();
            String sectionid = hcTruck.getSectionid();
            String orgcode = hcTruck.getOrgcode();

            QueryWrapper<HcToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.eq("orgcode", orgcode);
            HcToken hcToken = tokenService.getOne(tokenQueryWrapper);
            String token = hcToken.getToken();

            JSONObject sendDate = new JSONObject();
            int pageSize = 100;
            int pageNo = 1;

            QueryWrapper<SpreadandcrushCar> spreadandcrushCarQueryWrapper = new QueryWrapper<>();
            spreadandcrushCarQueryWrapper.select("max(locTime) locTime");
            spreadandcrushCarQueryWrapper.eq("truckId", truckId);
            SpreadandcrushCar one = spreadandcrushCarService.getOne(spreadandcrushCarQueryWrapper);
            Date gpstime;

            LocalDate today = LocalDate.now();
            if (one != null) {
                // 如果 one 不为空，则获取其中的 gpstime
                Instant instant = one.getLoctime().toInstant(); // 将Date对象转换为Instant对象
                instant = instant.plus(Duration.ofSeconds(1)); // 将时间加上一秒钟
                gpstime = Date.from(instant); // 将Instant转换为Date对象
            } else {
                // 如果 one 为空，则将 gpstime 设为今天的最小时间00:00:01
                LocalTime startTime = LocalTime.of(0, 0, 1); // 设置开始时间为 00:00:01
                LocalDateTime startDateTime = LocalDateTime.of(today, startTime);
                gpstime = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            }

            // 判断 gpstime 是否为当天日期（即年月日是否和当前时间相同）
            Instant instant = gpstime.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, zoneId);
            LocalDate date2 = ldt.toLocalDate();

            LocalDate newDate = LocalDate.of(today.getYear(), today.getMonth(), today.getDayOfMonth());
            if (!date2.equals(newDate)) {
                // 如果 gpstime 不是今天，则将其设为今天的最小时间（即 00:00:00）
                LocalTime startTime = LocalTime.of(0, 0, 1); // 设置开始时间为 00:00:01
                LocalDateTime startDateTime = LocalDateTime.of(today, startTime);
                gpstime = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            }
            LocalDateTime endTime = LocalDateTime.of(today, LocalTime.of(23, 59, 59)); // 设置结束时间为 23:59:59
            Date endTimeDate = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());

            String startTimeStr = sdf.format(gpstime);
            String endTimeStr = sdf.format(endTimeDate);

            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);
            sendDate.put("startTime", startTimeStr);
            sendDate.put("endTime", endTimeStr);

            sendDate.put("truckId", truckId);
            sendDate.put("pageSize", pageSize);
            sendDate.put("pageNo", pageNo);

            String result = HttpRequest.post(url)
                    .header("token", token)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            JSONObject jsonObject = JSONObject.parseObject(result);
            Integer code = jsonObject.getInteger("code");
            if (0 != code) {
                continue;
            }
            JSONObject data1 = jsonObject.getJSONObject("data");
            //当前页
            int pageCount = data1.getInteger("pageCount");

            SpreadandcrushCar preSpreadAndCrushCar = null;
            for (int i = 1; i <= pageCount; i++) {
                token = hcToken.getToken();
                sendDate.put("pageNo", i);
                String back = HttpRequest.post(url)
                        .header("token", token)
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();

                JSONObject backObject = JSONObject.parseObject(back);
                Integer code1 = jsonObject.getInteger("code");
                if (0 != code1){
                    continue;
                }
                JSONArray records = backObject.getJSONObject("data").getJSONArray("records");
                for (int j = 0; j < records.size(); j++) {
                    JSONObject jsonObject1 = records.getJSONObject(j);
                    SpreadandcrushCar spreadandcrushCar = jsonObject.toJavaObject(jsonObject1, SpreadandcrushCar.class);
                    //如果当前数据不是第一条数据，则计算出速度并设置到当前数据中
                    if (preSpreadAndCrushCar != null) {
                        double distance = calculateDistance(Double.parseDouble(preSpreadAndCrushCar.getLatitude()), Double.parseDouble(preSpreadAndCrushCar.getLongitude()), Double.parseDouble(spreadandcrushCar.getLatitude()), Double.parseDouble(spreadandcrushCar.getLongitude()));
                        long timeInterval = (spreadandcrushCar.getLoctime().getTime() - preSpreadAndCrushCar.getLoctime().getTime()) / 1000;
                        double speed = distance / timeInterval;
                        spreadandcrushCar.setSpeed(speed);
                    }else {
                        spreadandcrushCar.setSpeed(0.00);
                    }

                    //将当前数据保存到数据库中
                    try {
                        spreadandcrushCarService.save(spreadandcrushCar);
                    } catch (Exception e) {
                        log.info("数据异常！");
                    }

                    //将当前数据赋值给preSpreadAndCrushCar
                    preSpreadAndCrushCar = spreadandcrushCar;
                }
            }

        }
    }

}
