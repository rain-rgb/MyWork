package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.trtm.iot.hc_pavement_alarm.service.IHcPavementAlarmService;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * @ClassName roadbedAlarmJob：
 * @Description 土方压实预警
 * @Author 55314
 * @Date 2023/10/26 16:36
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class roadbedAlarmJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/roadbed/alarm.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcPavementAlarmService roadbedAlarmService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<HcSection> sectionList = sectionService.list();
        for (HcSection hcSection : sectionList) {
            String pjid = hcSection.getPjid();
            String sectionid = hcSection.getSectionid();

            QueryWrapper<HcToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.like("projectid", pjid);
            HcToken hcToken = tokenService.getOne(tokenQueryWrapper);
            String token = hcToken.getToken();

            QueryWrapper<HcPavementAlarm> hcRoadbedAlarmQueryWrapper = new QueryWrapper<>();
            hcRoadbedAlarmQueryWrapper.select("max(dataTime) as dataTime");
            hcRoadbedAlarmQueryWrapper.eq("projectId", pjid);
            hcRoadbedAlarmQueryWrapper.eq("sectionid", sectionid);
            HcPavementAlarm one = roadbedAlarmService.getOne(hcRoadbedAlarmQueryWrapper);
            Date endtime = null;
            if (null != one) {
                Instant instant = one.getDatatime().toInstant(); // 将Date对象转换为Instant对象
                instant = instant.plus(Duration.ofSeconds(1)); // 将时间加上一秒钟
                endtime = Date.from(instant); // 将Instant转换为Date对象
            }

            JSONObject sendDate = new JSONObject();

            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);
            sendDate.put("startTime", endtime);

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
            JSONArray records = jsonObject.getJSONObject("data").getJSONArray("records");
            for (int i = 0; i < records.size(); i++) {
                JSONObject record = records.getJSONObject(i);
                HcPavementAlarm hcRoadbedAlarm = jsonObject.toJavaObject(record, HcPavementAlarm.class);
                hcRoadbedAlarm.setProjectid(pjid);
                hcRoadbedAlarm.setSectionid(sectionid);
                hcRoadbedAlarm.setSectionid(sectionid);
                hcRoadbedAlarm.setType(2);
                roadbedAlarmService.save(hcRoadbedAlarm);
                log.info("土方压实预警保存成功！");
            }
        }
    }
}
