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
import com.trtm.iot.hc_truck.entity.HcTruck;
import com.trtm.iot.hc_truck.service.IHcTruckService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName hctruckJob：
 * @Description 获取华测运输车
 * @Author 55314
 * @Date 2023/4/23 15:32
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class hctruckJob implements Job {

    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcTruckService truckService;

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/base/truck.shtml";

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
            String bdorgcode = hcToken.getOrgcode();
            JSONObject sendDate = new JSONObject();

            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);

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
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);

                String truckId = object.getString("truckId");

                HcTruck hcTruck = new HcTruck();

                QueryWrapper<HcTruck> hcTruckQueryWrapper = new QueryWrapper<>();
                hcTruckQueryWrapper.eq("truckId",truckId);
                HcTruck one = truckService.getOne(hcTruckQueryWrapper);
                if (null != one){
                    hcTruck.setId(one.getId());
                }

                hcTruck.setTruckid(truckId);
                hcTruck.setMmit(object.getString("mmit"));
                hcTruck.setRfid(object.getString("rfid"));
                hcTruck.setLicenseplate(object.getString("licensePlate"));
                hcTruck.setDrivername(object.getString("driverName"));
                hcTruck.setDriverphone(object.getString("driverPhone"));
                hcTruck.setTrucktype(object.getInteger("truckType"));
                hcTruck.setPjid(pjid);
                hcTruck.setSectionid(sectionid);
                hcTruck.setOrgcode(bdorgcode);

                truckService.saveOrUpdate(hcTruck);
                log.info("华测运输车数据保存成功！");
            }
        }
    }
}
