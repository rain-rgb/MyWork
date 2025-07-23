package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkage;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkagePave;
import com.trtm.iot.hc_datalinkage.service.IHcDatalinkagePaveService;
import com.trtm.iot.hc_datalinkage.service.IHcDatalinkageService;
import com.trtm.iot.hc_datalinkage.vo.HcDatalinkagePage;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import com.trtm.iot.hc_truck.service.IHcTruckService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName dataGuanLianJob：
 * @Description 数据联动
 * @Author 55314
 * @Date 2023/6/13 13:40
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class dataGuanLianJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/pavement/dataLinkage.shtml";
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcDatalinkageService datalinkageService;
    @Autowired
    private IHcDatalinkagePaveService datalinkagePaveService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<HcSection> sectionList = sectionService.list();
        for (HcSection hcSection : sectionList) {
            String pjid = hcSection.getPjid();
            String sectionid = hcSection.getSectionid();

            QueryWrapper<HcToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.eq("projectid", pjid);
            HcToken hcToken = tokenService.getOne(tokenQueryWrapper);
            String token = hcToken.getToken();

            JSONObject sendDate = new JSONObject();
            int pageSize = 100;
            int pageNo = 1;

            QueryWrapper<HcDatalinkage> datalinkageQueryWrapper = new QueryWrapper<>();
            datalinkageQueryWrapper.select("max(outStationTime) outStationTime");
            HcDatalinkage hcDatalinkage = datalinkageService.getOne(datalinkageQueryWrapper);
            String outstationtime = "";
            if (hcDatalinkage != null){
                outstationtime = hcDatalinkage.getOutstationtime();
                if (outstationtime != null && !outstationtime.isEmpty()) {
                    // 将字符串时间转成Date类型，加一秒后再转回字符串
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date datetime = dateFormat.parse(outstationtime);
                        datetime.setTime(datetime.getTime() + 1000L);
                        outstationtime = dateFormat.format(datetime);
                    } catch (Exception e) {
                        // 解析时间失败的异常处理
                        e.printStackTrace();
                    }
                }
            }else {
                outstationtime = "2023-05-01 00:00:00";
            }
            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);
            sendDate.put("materialType", "2");
            sendDate.put("startTime", outstationtime);
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
                    HcDatalinkagePage hcDatalinkagePage = jsonObject.toJavaObject(jsonObject1, HcDatalinkagePage.class);

                    HcDatalinkage hcDatalinkage1 = new HcDatalinkage();
                    BeanUtils.copyProperties(hcDatalinkagePage,hcDatalinkage1);
                    hcDatalinkage1.setMaterialtype(2);
                    datalinkageService.save(hcDatalinkage1);
                    log.info("数据联动运输数据保存成功！");

                    List<HcDatalinkagePave> paveList = hcDatalinkagePage.getPaveList();
                    for (HcDatalinkagePave hcDatalinkagePave : paveList) {
                        hcDatalinkagePave.setZbid(hcDatalinkage1.getId());
                        datalinkagePaveService.save(hcDatalinkagePave);
                        log.info("数据联动摊铺数据保存成功！");
                    }
                }
            }
        }
    }
}
