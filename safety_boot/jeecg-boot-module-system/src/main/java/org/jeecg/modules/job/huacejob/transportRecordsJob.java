package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsPave;
import com.trtm.iot.hc_transportrecords.service.IHcTransportrecordsPaveService;
import com.trtm.iot.hc_transportrecords.service.IHcTransportrecordsService;
import com.trtm.iot.hc_transportrecords.vo.HcTransportrecordsPage;
import com.trtm.iot.hc_truck.entity.HcTruck;
import com.trtm.iot.hc_truck.service.IHcTruckService;
import com.trtm.iot.spreadandcrush_car.entity.SpreadandcrushCar;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName transportRecordsJob：
 * @Description 运输记录
 * @Author 55314
 * @Date 2023/4/27 14:11
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class transportRecordsJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/transport/transportRecords.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcTruckService truckService;
    @Autowired
    private IHcTransportrecordsService transportrecordsService;
    @Autowired
    private IHcTransportrecordsPaveService transportrecordsPaveService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<HcTruck> truckList = truckService.list();
        for (HcTruck hcTruck : truckList) {
            String pjid = hcTruck.getPjid();
            String sectionid = hcTruck.getSectionid();
            String truckid = hcTruck.getTruckid();

            QueryWrapper<HcProject> projectQueryWrapper = new QueryWrapper<>();
            projectQueryWrapper.like("pjid", pjid);
            HcProject hcProject = projectService.getOne(projectQueryWrapper);
            String orgcode = hcProject.getOrgcode();

            QueryWrapper<HcToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.eq("orgcode", orgcode);
            HcToken hcToken = tokenService.getOne(tokenQueryWrapper);
            String token = hcToken.getToken();

            JSONObject sendDate = new JSONObject();
            int pageSize = 100;
            int pageNo = 1;
            QueryWrapper<HcTransportrecords> transportrecordsQueryWrapper = new QueryWrapper<>();
            transportrecordsQueryWrapper.select("max(outStationTime) outStationTime");
            transportrecordsQueryWrapper.eq("truckId", truckid);
            HcTransportrecords one = transportrecordsService.getOne(transportrecordsQueryWrapper);
            Date startTime = null;
            String endTime = sdf.format(new Date());
            if (one != null) {
                Instant instant = one.getOutstationtime().toInstant(); // 将Date对象转换为Instant对象
                instant = instant.plus(Duration.ofSeconds(1)); // 将时间加上一秒钟
                startTime = Date.from(instant); // 将Instant转换为Date对象
            }else {
                try {
                    startTime = sdf.parse("2023-05-01 00:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            List truckIds = new ArrayList();
            truckIds.add(truckid);

            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);
            sendDate.put("truckIds", truckIds);
            sendDate.put("startTime", sdf.format(startTime));
            sendDate.put("endTime", endTime);
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

            for (int i = 1; i <= pageCount; i++) {
                sendDate.put("pageNo", i);
                String back = HttpRequest.post(url)
                        .header("token", token)
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();

                JSONObject backObject = JSONObject.parseObject(back);
                JSONArray records = backObject.getJSONObject("data").getJSONArray("records");
                for (int j = 0; j < records.size(); j++) {
                    JSONObject jsonObject1 = records.getJSONObject(j);
                    HcTransportrecordsPage hcTransportrecordsPage = jsonObject.toJavaObject(jsonObject1, HcTransportrecordsPage.class);

                    HcTransportrecords transportrecords = new HcTransportrecords();
                    BeanUtils.copyProperties(hcTransportrecordsPage,transportrecords);
                    transportrecords.setProjectid(pjid);
                    transportrecords.setSectionid(sectionid);
                    transportrecordsService.save(transportrecords);

                    List<HcTransportrecordsPave> hcTransportrecordsPaveList = hcTransportrecordsPage.getPaveList();
                    for (HcTransportrecordsPave hcTransportrecordsPave : hcTransportrecordsPaveList) {
                        hcTransportrecordsPave.setZbid(transportrecords.getId());
                        transportrecordsPaveService.save(hcTransportrecordsPave);
                    }
                    log.info("运输记录保存成功！");
                }
            }
        }
    }
}
