package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresultsTPNY;
import com.trtm.iot.hc_constructionresults.service.IHcConstructionresultsService;
import com.trtm.iot.hc_constructionresults_ny.entity.HcConstructionresultsNy;
import com.trtm.iot.hc_constructionresults_ny.service.IHcConstructionresultsNyService;
import com.trtm.iot.hc_constructionresults_tp.entity.HcConstructionresultsTp;
import com.trtm.iot.hc_constructionresults_tp.service.IHcConstructionresultsTpService;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName constructionResultsJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/4/25 11:31
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class constructionResultsJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/pavement/constructionResults.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcConstructionresultsService sultsService;
    @Autowired
    private IHcConstructionresultsTpService tpService;
    @Autowired
    private IHcConstructionresultsNyService nyService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<HcSection> sectionList = sectionService.list();
        for (HcSection hcSection : sectionList) {
            String pjid = hcSection.getPjid();
            String sectionid = hcSection.getSectionid();

            QueryWrapper<HcToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.eq("projectid", pjid);
            HcToken hcToken = tokenService.getOne(tokenQueryWrapper);
            if (null == hcToken){
                continue;
            }
            String token = hcToken.getToken();
            int pageSize = 100;
            int pageNo = 1;

            JSONObject sendDate = new JSONObject();

            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);
            sendDate.put("pageSize", pageSize);
            sendDate.put("pageNo", pageNo);

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
            String data = jsonObject.getString("data");
            //获取总条数
            JSONObject jsonObjectData = JSONObject.parseObject(data);
            Integer rowCount = jsonObjectData.getInteger("rowCount");

            //获取已存条数
            QueryWrapper<HcConstructionresults> resultsQueryWrapper = new QueryWrapper<>();
            resultsQueryWrapper.eq("projectid", pjid);
            resultsQueryWrapper.eq("sectionid", sectionid);
            Integer count = sultsService.count(resultsQueryWrapper);

            // 没有保存的数据总数
            if (rowCount - count <= 0) {
                continue; // 所有数据都已保存，退出循环
            }

            pageNo = count /100 + 1;

            for (int i = pageNo; i <= rowCount/100+1; i++) {
                sendDate.put("pageNo", pageNo);

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
                    String id = object.getString("id");
                    QueryWrapper<HcConstructionresults> resultsQueryWrapper1 = new QueryWrapper<>();
                    resultsQueryWrapper1.eq("id", id);
                    Integer count1 = sultsService.count(resultsQueryWrapper1);

                    if (count1>0){
                        continue;
                    }

                    HcConstructionresultsTPNY hcConstructionresultsTPNY = JSONObject.toJavaObject(object, HcConstructionresultsTPNY.class);

                    HcConstructionresults hcConstructionresults = new HcConstructionresults();
                    BeanUtils.copyProperties(hcConstructionresultsTPNY,hcConstructionresults);
                    hcConstructionresults.setProjectid(pjid);
                    hcConstructionresults.setSectionid(sectionid);
                    sultsService.save(hcConstructionresults);

                    List<HcConstructionresultsTp> paver = hcConstructionresultsTPNY.getPaver();
                    for (HcConstructionresultsTp hcConstructionresultsTp : paver) {
                        hcConstructionresultsTp.setCrid(hcConstructionresults.getId());
                        tpService.save(hcConstructionresultsTp);
                    }

                    List<HcConstructionresultsNy> rollerList = hcConstructionresultsTPNY.getRollerList();
                    for (HcConstructionresultsNy hcConstructionresultsNy : rollerList) {
                        hcConstructionresultsNy.setCrid(hcConstructionresults.getId());
                        nyService.save(hcConstructionresultsNy);
                    }
                }
                log.info("施工成果数据保存成功！当前页："+ pageNow + "，总页数："+pageCount);
            }

        }
    }

}
