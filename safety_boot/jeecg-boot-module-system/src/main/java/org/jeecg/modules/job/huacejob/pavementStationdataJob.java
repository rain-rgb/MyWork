package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdata;
import com.trtm.iot.hc_pavement_stationdata.service.IHcPavementStationdataService;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @ClassName pavementStationdataJob：
 * @Description 逐桩数据
 * @Author 55314
 * @Date 2023/4/24 16:48
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class pavementStationdataJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/pavement/stationData.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcPavementStationdataService pavementStationdataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<HcSection> sectionList = sectionService.list();
        for (HcSection hcSection : sectionList) {
            String pjid = hcSection.getPjid();
            String sectionid = hcSection.getSectionid();

            QueryWrapper<HcToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.like("projectid", pjid);
            HcToken hcToken = tokenService.getOne(tokenQueryWrapper);
            String token = hcToken.getToken();
            String bdorgcode = hcToken.getOrgcode();

            QueryWrapper<HcPavementStationdata> pavementStationdataQueryWrapper1 = new QueryWrapper<>();
            pavementStationdataQueryWrapper1.select("max(baocundate) baocundate")
                    .eq("sectionid",sectionid);
            HcPavementStationdata one = pavementStationdataService.getOne(pavementStationdataQueryWrapper1);
            Date gpstime;
            LocalDate today = LocalDate.now();
            if (one == null){
                // 如果 one 为空，则将 gpstime 设为今天的最小时间00:00:01
                LocalTime startTime = LocalTime.of(0, 0, 1); // 设置开始时间为 00:00:01
                LocalDateTime startDateTime = LocalDateTime.of(today, startTime);
                gpstime = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            }else {
                gpstime = one.getBaocundate();
            }
            LocalDateTime endTime = LocalDateTime.of(today, LocalTime.of(23, 59, 59)); // 设置结束时间为 23:59:59
            Date endTimeDate = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());

            String startTimeStr = sdf.format(gpstime);
            String endTimeStr = sdf.format(endTimeDate);

            int pageSize = 100;
            int pageNo = 1;

            JSONObject sendDate = new JSONObject();

            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);
            sendDate.put("pageSize", pageSize);
            sendDate.put("pageNo", pageNo);
            sendDate.put("startTime", startTimeStr);
            sendDate.put("endTime", endTimeStr);

            String result = HttpRequest.post(url)
                    .header("token", token)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            JSONObject jsonObject = JSONObject.parseObject(result);
            Integer code = jsonObject.getInteger("code");
            if (0 != code){
                continue;
            }
            JSONObject data = jsonObject.getJSONObject("data");
            //当前页
            int pageCount1 = data.getInteger("pageCount");
            for (int i = pageNo; i <= pageCount1; i++) {
                sendDate.put("pageNo", i);

                String back = HttpRequest.post(url)
                        .header("token", token)
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();
                JSONObject backJson = JSONObject.parseObject(back);
                JSONObject data1 = backJson.getJSONObject("data");
                int pageNow = data1.getInteger("pageNow");
                int pageCount = data1.getInteger("pageCount");
                JSONArray recordsArr = data1.getJSONArray("records");
                for (int j = 0; j < recordsArr.size(); j++) {
                    JSONObject object = recordsArr.getJSONObject(j);

                    HcPavementStationdata hcPavementStationdata = jsonObject.toJavaObject(object, HcPavementStationdata.class);
                    hcPavementStationdata.setProjectid(pjid);
                    hcPavementStationdata.setSectionid(sectionid);
                    hcPavementStationdata.setBaocundate(new Date());

                    try {
                        pavementStationdataService.save(hcPavementStationdata);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    log.info("逐桩数据已保存成功！当前页："+ pageNow + "，总页数："+pageCount);
                }
            }
        }
    }
}
