package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @ClassName projectJob：
 * @Description 获取华测项目信息
 * @Author 55314
 * @Date 2023/4/23 10:38
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class projectJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/base/project.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<HcToken> list = tokenService.list();
        for (HcToken hcToken : list) {
            //获取token
            String token = hcToken.getToken();
            String orgcode = hcToken.getOrgcode();

            String result = HttpRequest.post(url)
                    .header("token",token)
                    .execute()
                    .body();

            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);

                String pjId = object.getString("id");

                HcProject hcProject = new HcProject();

                QueryWrapper<HcProject> hcProjectQueryWrapper = new QueryWrapper<>();
                hcProjectQueryWrapper.eq("pjId",pjId);
                HcProject one = projectService.getOne(hcProjectQueryWrapper);
                if (null != one){
                    hcProject.setId(one.getId());
                }

                hcProject.setPjid(pjId);
                hcProject.setPjname(object.getString("pjName"));
                hcProject.setPjstationprefix(object.getString("pjStationPrefix"));
                hcProject.setPjdescription(object.getString("pjDescription"));
                hcProject.setPjstartdate(object.getDate("pjStartDate"));
                hcProject.setPjshapetype(object.getString("pjShapeType"));
                hcProject.setPjownercompanyid(object.getString("pjOwnerCompanyId"));
                hcProject.setPjownercompany(object.getString("pjOwnerCompany"));
                hcProject.setOrgcode(orgcode);

                projectService.saveOrUpdate(hcProject);
            }
        }
    }
}
