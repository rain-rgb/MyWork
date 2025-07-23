package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.impl.SysDepartprojectServiceImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName wbsbendiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/4 14:34
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class wbsbendiJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private SysDepartprojectServiceImpl sysDepartprojectService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("MAX(updatime) AS update_time, MAX(create_time) AS create_time");
        queryWrapper.like("org_code", "A20");
        SysDepartproject one = sysDepartprojectService.getOne(queryWrapper);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date updateTime = one.getUpdateTime();
        Date createTime = one.getCreateTime();
        String update_time = sdf.format(updateTime);
        String create_time = sdf.format(createTime);
        String TIME_ = "2022-01-20 09:21:59";
        int i1 = update_time.compareTo(create_time);
        if (i1<0){
            TIME_ = create_time;
        }else {
            TIME_ = update_time;
        }
        String url = "http://fjhmtd.com:32221/glaf/website/ws/dataset/api/json?id=2873dab868964b808142bffd884cbcd0&TIME_=" + TIME_ + "&page=1";
        String body = HttpRequest.get(url)
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(body);
        Integer total = (Integer) jsonObject1.get("total");

        int i = 0;
        if (total % 10 != 0) {
            i = total / 10 + 1;
        }else {
            i = total / 10;
        }
        if (i > 0) {
            for (int j = 1; j < i + 1; j++) {
                String url1 = "http://fjhmtd.com:32221/glaf/website/ws/dataset/api/json?id=2873dab868964b808142bffd884cbcd0&TIME_=" + TIME_ + "&page=" + j + "";
                String body1 = HttpRequest.get(url1)
                        .header("Content-Type", "application/json")
                        .execute()
                        .body();

                JSONObject jsonObject2 = new JSONObject(body1);
                String rows = String.valueOf(jsonObject2.get("rows"));
                if (rows.equals("[]")) {
                    log.info("无瑞苍WBS数据！");
                    return;
                }
                System.out.println("页码-----------------------------------------------------------"+j);
                save(rows);
            }
        }
    }

    void save(String wbs) {
        JSONArray deptobjects = JSONUtil.parseArray(wbs);
        for (Object object : deptobjects) {
            JSONObject jsonObject = (JSONObject) object;
            SysDepartproject sysDepartproject = new SysDepartproject();
            sysDepartproject.setId("rc" + jsonObject.getStr("id"));
            sysDepartproject.setParentId("rc" + jsonObject.getStr("parent_id"));
            sysDepartproject.setDepartName(jsonObject.getStr("depart_name"));
            sysDepartproject.setOrgType(jsonObject.getStr("org_category"));
            QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", "rc" + jsonObject.getStr("parent_id"));
            List<SysDepartproject> list = sysDepartprojectService.list(queryWrapper);
            String orgCode1 = "";
            String orgCodes  = "";
            if (list.size() == 0) {
                orgCode1 = String.valueOf(UUID.randomUUID());
            } else {
                orgCodes = list.get(0).getOrgCodes();
                orgCode1 = list.get(0).getOrgCode();
            }
            String wbsindexid = jsonObject.getStr("wbsindexid");
            Integer strh = Integer.parseInt(wbsindexid.substring(wbsindexid.length() - 3));
            String orgcode = "";
            if (strh < 100) {
                orgcode = orgCode1 + "A" + wbsindexid.substring(wbsindexid.length() - 2);
            } else if (strh >= 100 && strh < 200){
                orgcode = orgCode1 + "B" + wbsindexid.substring(wbsindexid.length() - 2);
            }else if (strh >= 200 && strh < 300){
                orgcode = orgCode1 + "C" + wbsindexid.substring(wbsindexid.length() - 2);
            }else  if (strh >= 300 && strh < 400){
                orgcode = orgCode1 + "D" + wbsindexid.substring(wbsindexid.length() - 2);
            }else  if (strh >= 400 && strh < 500){
                orgcode = orgCode1 + "E" + wbsindexid.substring(wbsindexid.length() - 2);
            }else  if (strh >= 500 && strh < 600){
                orgcode = orgCode1 + "F" + wbsindexid.substring(wbsindexid.length() - 2);
            }
            sysDepartproject.setDelFlag(jsonObject.getStr("del_flag"));
            sysDepartproject.setCreateBy(jsonObject.getStr("creater_by"));
            sysDepartproject.setUpdateBy(jsonObject.getStr("updater_by"));
            sysDepartproject.setCreateTime(jsonObject.getDate("creater_time"));
            sysDepartproject.setUpdatime(jsonObject.getDate("update_time"));
            sysDepartproject.setOldid(jsonObject.getStr("org_id"));
            sysDepartproject.setTreeid(jsonObject.getStr("treeid"));
            sysDepartproject.setOrgCodes(orgCodes);
            boolean save = false;
            boolean update = false;
            QueryWrapper<SysDepartproject> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id", "rc" + jsonObject.getStr("id"));
            List<SysDepartproject> list1 = sysDepartprojectService.list(queryWrapper1);
            if (list1.size()>0){
                update = sysDepartprojectService.update(sysDepartproject, queryWrapper1);
            }else {
                sysDepartproject.setOrgCode(orgcode);
                try {
                    save = sysDepartprojectService.save(sysDepartproject);
                } catch (Exception e) {
                    QueryWrapper<SysDepartproject> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.likeRight("org_code",orgCode1);
                    queryWrapper2.eq("org_type",list.get(0).getOrgType()+1);
                    List<SysDepartproject> list2 = sysDepartprojectService.list(queryWrapper2);
                    int size = list2.size()+1;
                    String orgcode1 = "";
                    if (size < 10) {
                        orgcode1 = orgCode1 + "A0" + size;
                    } else if (size >= 10 && size < 100){
                        orgcode1 = orgCode1 + "A" + size;
                    }
                    sysDepartproject.setOrgCode(orgcode1);
                    try {
                        save = sysDepartprojectService.save(sysDepartproject);
                    } catch (Exception exception) {
                        System.out.println(sysDepartproject);
                    }
                    System.out.println(jsonObject);
                }
            }
            if (save) {
                System.out.println("添加成功！  " + sysDepartproject.getDepartName() + "----------------" + sysDepartproject.getOrgCode());
            }
            if (update) {
                System.out.println("修改成功！  " + sysDepartproject.getDepartName() + "----------------" + sysDepartproject.getOrgCode());
            }
        }
    }
}
