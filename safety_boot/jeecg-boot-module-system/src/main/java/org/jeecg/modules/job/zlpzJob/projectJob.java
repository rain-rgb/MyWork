package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.zlpz.entity.ZlpzProject;
import com.trtm.iot.zlpz.entity.ZlpzTenders;
import com.trtm.iot.zlpz.service.IZlpzProjectService;
import com.trtm.iot.zlpz.service.IZlpzTendersService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.MD5Util;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName projectJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/3/20 11:28
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class projectJob implements Job {

    @Autowired
    private IZlpzProjectService zlpzProjectService;
    @Autowired
    private IZlpzTendersService zlpzTendersService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        String url = "http://z.traiot.cn/jeecg-boot/zlpz/ZLPZTestController/getxmid";
        String back = HttpRequest.post(url)
                .timeout(20000)
                .execute().body();
        JSONObject jsonObject = new JSONObject(back);
        String resultJson = jsonObject.getStr("result");

        JSONObject resultObject = new JSONObject(resultJson);
        JSONArray resultList = resultObject.getJSONArray("result");

        for (Object object : resultList) {
            JSONObject data = (JSONObject) object;
            String id = data.getStr("id");

            LambdaQueryWrapper<ZlpzProject> zlpzProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            zlpzProjectLambdaQueryWrapper.eq(ZlpzProject::getXmid,id);
            ZlpzProject one = zlpzProjectService.getOne(zlpzProjectLambdaQueryWrapper);

            ZlpzProject zlpzProject = new ZlpzProject();
            zlpzProject.setAreaCode(data.getStr("areaCode"));
            zlpzProject.setConstruction(data.getStr("construction"));
            zlpzProject.setXmid(id);
            zlpzProject.setName(data.getStr("name"));
            zlpzProject.setType(data.getStr("type"));
            zlpzProject.setTypeName(data.getStr("typeName"));
            if (one==null) {
                zlpzProjectService.save(zlpzProject);
            }else {
                zlpzProject.setId(one.getId());
                zlpzProjectService.updateById(zlpzProject);
            }

            String bdurl = "http://z.traiot.cn/jeecg-boot/zlpz/ZLPZTestController/getbdid?xmid="+id;
            String bdback = HttpRequest.post(bdurl)
                    .timeout(20000)
                    .execute().body();

            JSONObject bdjsonObject = null;
            try {
                bdjsonObject = new JSONObject(bdback);
            } catch (Exception e) {
                System.out.println(bdback);
            }
            String bdresultJson = bdjsonObject.getStr("result");

            JSONObject bdresultObject = new JSONObject(bdresultJson);
            JSONArray bdresultList = bdresultObject.getJSONArray("result");

            for (Object bdobject : bdresultList) {
                JSONObject bddata = (JSONObject) bdobject;
                String bdid = bddata.getStr("id");
                ZlpzTenders zlpzTenders = new ZlpzTenders();
                zlpzTenders.setBdid(bdid);
                zlpzTenders.setXmid(id);
                zlpzTenders.setName(bddata.getStr("name"));
                zlpzTenders.setSectiontype(bddata.getStr("sectionType"));
                zlpzTenders.setOrgname(bddata.getStr("org_name"));

                LambdaQueryWrapper<ZlpzTenders> zlpzTendersLambdaQueryWrapper = new LambdaQueryWrapper<>();
                zlpzTendersLambdaQueryWrapper.eq(ZlpzTenders::getBdid,bdid);
                ZlpzTenders ZlpzTenderdata = zlpzTendersService.getOne(zlpzTendersLambdaQueryWrapper);
                if (ZlpzTenderdata==null){
                    zlpzTendersService.save(zlpzTenders);
                }else {
                    zlpzTenders.setId(ZlpzTenderdata.getId());
                    zlpzTendersService.updateById(zlpzTenders);
                }

            }
        }
    }
}
