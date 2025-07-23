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
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName sectionJob：
 * @Description 获取华测标段信息
 * @Author 55314
 * @Date 2023/4/23 13:41
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class sectionJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/base/section.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcSectionService sectionService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        List<HcProject> list = projectService.list();
        for (HcProject hcProject : list) {
            //项目id
            String pjid = hcProject.getPjid();
            String orgcode = hcProject.getOrgcode();

            QueryWrapper<HcToken> hcTokenQueryWrapper = new QueryWrapper<>();
            hcTokenQueryWrapper.like("projectid", pjid);
            HcToken hcToken = tokenService.getOne(hcTokenQueryWrapper);

            String token = hcToken.getToken();

            JSONObject sendDate = new JSONObject();

            sendDate.put("projectId", pjid);

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

                HcSection hcSection = new HcSection();

                String sectionid = object.getString("id");
                QueryWrapper<HcSection> hcSectionQueryWrapper = new QueryWrapper<>();
                hcSectionQueryWrapper.eq("sectionid",sectionid);
                HcSection one = sectionService.getOne(hcSectionQueryWrapper);
                if (null != one){
                    hcSection.setId(one.getId());
                }

                hcSection.setSectionid(sectionid);
                hcSection.setSectionname(object.getString("sectionName"));
                hcSection.setOffsettype(object.getString("offsetType"));
                hcSection.setTimestype(object.getString("timesType"));
                hcSection.setStratifymode(object.getString("stratifyMode"));
                hcSection.setSectionboundary(object.getString("sectionBoundary"));
                hcSection.setSectionwgsboundary(object.getString("sectionWgsBoundary"));
                hcSection.setSectionstartdate(object.getDate("sectionStartDate"));
                hcSection.setSectionsupcompanyid(object.getString("sectionSupCompanyId"));
                hcSection.setSectionsupcompanyname(object.getString("sectionSupCompanyName"));
                hcSection.setSectionbuildercompanyid(object.getString("sectionBuilderCompanyId"));
                hcSection.setSectionbuildercompanyname(object.getString("sectionBuilderCompanyName"));
//                hcSection.setOrgcode(orgcode);
                hcSection.setPjid(pjid);

                sectionService.saveOrUpdate(hcSection);
            }
        }
    }
}
