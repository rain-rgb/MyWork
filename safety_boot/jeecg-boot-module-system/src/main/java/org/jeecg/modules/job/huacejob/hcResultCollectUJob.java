package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_result_collect.entity.HcResultCollect;
import com.trtm.iot.hc_result_collect.service.IHcResultCollectService;
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
 * @ClassName hcResultCollectUJob：
 * @Description 摊铺里程
 * @Author 55314
 * @Date 2023/4/25 16:13
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class hcResultCollectUJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/pavement/result/collect.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcResultCollectService resultCollectService;

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

            QueryWrapper<HcResultCollect> resultCollectQueryWrapper = new QueryWrapper<>();
            resultCollectQueryWrapper.select("max(endTime) as endtime");
            resultCollectQueryWrapper.eq("projectId",pjid);
            resultCollectQueryWrapper.eq("sectionid",sectionid);
            HcResultCollect one = resultCollectService.getOne(resultCollectQueryWrapper);
            Date endtime = null;
            if (null != one){
                Instant instant = one.getEndtime().toInstant(); // 将Date对象转换为Instant对象
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
            if (0 != code){
                continue;
            }
            JSONArray  layerResults = jsonObject.getJSONObject("data").getJSONArray("layerResults");
            for (int i = 0; i < layerResults.size(); i++) {
                JSONObject layerResult = layerResults.getJSONObject(i);
                JSONArray workDatas = layerResult.getJSONArray("workDatas");
                for (int j = 0; j < workDatas.size(); j++) {
                    JSONObject workData = workDatas.getJSONObject(j);
                    HcResultCollect hcResultCollect = jsonObject.toJavaObject(workData, HcResultCollect.class);
                    resultCollectService.save(hcResultCollect);
                    log.info("摊铺里程分布数据保存成功！");
                }
            }
        }
    }
}
