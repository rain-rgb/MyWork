package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_machine.service.IHcMachineService;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName gpsDataJob：
 * @Description 设备定位
 * @Author 55314
 * @Date 2023/5/12 14:49
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class gpsDataJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/base/gpsData.shtml";
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcMachineService machineService;
    @Autowired
    private IOpenapigpsdatavoService gpsDataService;
    @Autowired
    private IQuartzJobService jobService;
    @Autowired
    private IHcSectionService hcSectionService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {


        //获取所有设备
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<HcMachine> list = machineService.list();

        List machineIds = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getSectionid().equals("jishu03@20220926172651")) {
//                machineIds.add(list.get(i).getMachineid());
//            }\
            if (list.get(i).getSectionid().equals("jishu03@20250310174045")){
                machineIds.add(list.get(i).getMachineid());
            }
        }
        if (machineIds.size() > 0) {
            String pjid = list.get(0).getPjid();
            String orgcode = list.get(0).getOrgcode();
            String sectionid = list.get(0).getSectionid();

            QueryWrapper<HcToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.eq("orgcode", orgcode);
            HcToken hcToken = tokenService.getOne(tokenQueryWrapper);
            String token = hcToken.getToken();

            JSONObject sendDate = new JSONObject();
            int pageSize = 1000;
            int pageNo = 1;

            QueryWrapper<Openapigpsdatavo> openapigpsdatavoQueryWrapper = new QueryWrapper<>();
            openapigpsdatavoQueryWrapper.select("max(gps_Time) gpsTime");
            openapigpsdatavoQueryWrapper.in("machine_Id", machineIds);
            Openapigpsdatavo one = gpsDataService.getOne(openapigpsdatavoQueryWrapper);
            Date gpstime;

            if (one != null) {
                // 如果 one 不为空，则获取其中的 gpstime
                Instant instant = one.getGpsTime().toInstant(); // 将Date对象转换为Instant对象
                instant = instant.plus(Duration.ofSeconds(1)); // 将时间加上一秒钟
                gpstime = Date.from(instant); // 将Instant转换为Date对象
            } else {
                // 如果 one 为空，则将 gpstime 设为今天的最小时间
                LocalDate today = LocalDate.now();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime newLdt = LocalDateTime.of(today, LocalTime.MIN);
                gpstime = Date.from(newLdt.atZone(zoneId).toInstant());
            }

            // 判断 gpstime 是否为当天日期（即年月日是否和当前时间相同）
            Instant instant = gpstime.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, zoneId);
            LocalDate date2 = ldt.toLocalDate();
            LocalDate today = LocalDate.now();

            LocalDate newDate = LocalDate.of(today.getYear(), today.getMonth(), today.getDayOfMonth());
            if (!date2.equals(today)) {
                // 如果 gpstime 不是今天，则将其设为今天的最小时间（即 00:00:00）
                LocalDateTime newLdt = LocalDateTime.of(newDate, LocalTime.MIN);
                gpstime = Date.from(newLdt.atZone(zoneId).toInstant());
            }
            LocalDateTime newLdt = LocalDateTime.of(newDate, LocalTime.MAX);
            Date endTime = Date.from(newLdt.atZone(zoneId).toInstant());
            String startTimeStr = sdf.format(gpstime);
            String endTimeStr = sdf.format(endTime);

//            List<HcSection> list1 = hcSectionService.list();
//            List<HcSection> list1 = new ArrayList<>();
//            for (HcSection hcSection : list1) {
                sendDate.put("projectId", pjid);
//                sendDate.put("sectionId", hcSection.getSectionid());
                sendDate.put("sectionId", sectionid);
                sendDate.put("startTime", startTimeStr);
                sendDate.put("endTime", endTimeStr);
//            sendDate.put("startTime", "2023-08-04 00:00:01");
//            sendDate.put("endTime", "2023-08-03 12:00:00");
                sendDate.put("machineIds", machineIds);
                sendDate.put("pageSize", pageSize);
                sendDate.put("pageNo", pageNo);

                String result = HttpRequest.post(url)
                        .header("token", token)
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();

                JSONObject jsonObject = JSONObject.parseObject(result);
                Integer code = jsonObject.getInteger("code");
                log.info(String.valueOf(code));
                if (0 == code) {
                    JSONObject data1 = jsonObject.getJSONObject("data");
                    //当前页
                    int pageCount = data1.getInteger("pageCount");

                    for (int i = pageCount; i >= 1; i--) {
                        sendDate.put("pageNo", i);
                        String back = HttpRequest.post(url)
                                .header("token", token)
                                .body(String.valueOf(sendDate))
                                .execute()
                                .body();

                        JSONObject backObject = JSONObject.parseObject(back);
                        Integer code1 = backObject.getInteger("code");
                        if (code1 != 0){
                            continue;
                        }
                        JSONArray records = backObject.getJSONObject("data").getJSONArray("records");
                        for (int j = 0; j < records.size(); j++) {
                            JSONObject jsonObject1 = records.getJSONObject(j);
                            Openapigpsdatavo openapigpsdatavo = jsonObject.toJavaObject(jsonObject1, Openapigpsdatavo.class);
                            Double velocity = Double.parseDouble(openapigpsdatavo.getVelocity());
                            openapigpsdatavo.setVelocity(String.valueOf(velocity * 60));
                            try {
                                gpsDataService.save(openapigpsdatavo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            log.info("保存成功！桩号：" + openapigpsdatavo.getRoadStation());
                        }
                    }
                }
//            }
        }
    }
}
