package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_machine_onlinerecord.entity.HcMachineOnlinerecord;
import com.trtm.iot.hc_machine_onlinerecord.service.IHcMachineOnlinerecordService;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * @ClassName machineOnlinerecordJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/4/26 14:08
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class machineOnlinerecordJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/machine/onlineRecord.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcMachineOnlinerecordService machineOnlinerecordService;

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

            QueryWrapper<HcMachineOnlinerecord> machineOnlinerecordQueryWrapper = new QueryWrapper<>();
            machineOnlinerecordQueryWrapper.select("max(loginTime) as loginTime");
            machineOnlinerecordQueryWrapper.eq("projectId", pjid);
            machineOnlinerecordQueryWrapper.eq("sectionid", sectionid);
            HcMachineOnlinerecord one = machineOnlinerecordService.getOne(machineOnlinerecordQueryWrapper);
            Date endtime = null;
            if (null != one) {
                Instant instant = one.getLogintime().toInstant(); // 将Date对象转换为Instant对象
                instant = instant.plus(Duration.ofSeconds(1)); // 将时间加上一秒钟
                endtime = Date.from(instant); // 将Instant转换为Date对象
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONObject sendDate = new JSONObject();

            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);
            if (endtime!=null) {
                sendDate.put("startTime", sdf.format(endtime));
            }else {
                sendDate.put("startTime", "2023-01-01 00:00:00");
            }
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
            //总页数
            JSONObject data = jsonObject.getJSONObject("data");
            Integer pageCount = data.getInteger("pageCount");
            for (int i = pageCount; i > 0; i--) {
                sendDate.put("pageNo", i);

                String back = HttpRequest.post(url)
                        .header("token", token)
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();

                JSONObject backObject = JSONObject.parseObject(back);
                if (backObject.getJSONObject("data")==null){
                    continue;
                }
                JSONArray records = backObject.getJSONObject("data").getJSONArray("records");
                for (int j = 0; j < records.size(); j++) {
                    JSONObject record = records.getJSONObject(j);
                    Date loginTime = record.getDate("loginTime");
                    Date logoffTime = record.getDate("logoffTime");
                    String snCode = record.getString("snCode");

                    QueryWrapper<HcMachineOnlinerecord> hcMachineOnlinerecordQueryWrapper = new QueryWrapper<>();
                    hcMachineOnlinerecordQueryWrapper.eq("loginTime",loginTime);
                    hcMachineOnlinerecordQueryWrapper.eq("logoffTime",logoffTime);
                    hcMachineOnlinerecordQueryWrapper.eq("snCode",snCode);
                    List<HcMachineOnlinerecord> list = machineOnlinerecordService.list(hcMachineOnlinerecordQueryWrapper);
                    if (list.size()>0){
                        continue;
                    }

                    HcMachineOnlinerecord hcMachineOnlinerecord = jsonObject.toJavaObject(record, HcMachineOnlinerecord.class);
                    hcMachineOnlinerecord.setProjectid(pjid);
                    hcMachineOnlinerecord.setSectionid(sectionid);
                    machineOnlinerecordService.save(hcMachineOnlinerecord);
                    log.info("设备上下线数据保存成功！");
                }
            }

        }
    }
}
