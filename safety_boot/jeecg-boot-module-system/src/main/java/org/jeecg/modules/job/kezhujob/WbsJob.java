package org.jeecg.modules.job.kezhujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName WbsJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/2/15 10:20
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WbsJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String WbsId = "";
        QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,org_category,org_code,memo");
        queryWrapper.eq("memo", "10")
                .or()
                .eq("memo", "0");
        List<SysDepartproject> list = sysDepartprojectService.list(queryWrapper);
        for (SysDepartproject sysDepartproject : list) {
            String id = sysDepartproject.getId();
            int orgCategory = Integer.parseInt(sysDepartproject.getOrgCategory());
            WbsId = id.substring(2);
            //接口合同段id
            String projId = "81501";
            String substring = sysDepartproject.getOrgCode().substring(0, 3);
            if (substring.equals("A34")) {
                projId = "81504";
            }
            //org_category为6
            if (sysDepartproject.getMemo().equals("0")) {
                HttpRequestm(WbsId, orgCategory + 1, projId);
            } else {
                HttpRequest(WbsId, orgCategory + 1, projId);
            }
        }
    }

    //获取wbs数据
    void HttpRequest(String WbsId, int org_category, String projId) {
        userlogin();

        String url = "http://112.95.76.11:8000/gapp/openuserprojwbsbyid.action?dbName=zj_kzPallas&projId=" + projId + "&WbsId=" + WbsId;//一标（2415001）四标（2415004）
        String body = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(body);
        String data = String.valueOf(jsonObject.get("data"));
        if ("[]".equals(data)) {
            SysDepartproject sysDepartproject = new SysDepartproject();
            sysDepartproject.setId("kz" + WbsId);
            sysDepartproject.setMemo("-1");
            sysDepartprojectService.updateById(sysDepartproject);
            return;
        }
        save(WbsId, data, org_category, projId);
    }

    //获取wbs末节点数据
    void HttpRequestm(String WbsId, int org_category, String projId) {
        userlogin();

        String url = "http://112.95.76.11:8000/gapp/projcellquerybywbsId.action?dbName=zj_kzPallas&WbsId=" + WbsId;//一标（2415001）四标（2415004）
        String body = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(body);
        String success = String.valueOf(jsonObject.get("success"));
        String data = String.valueOf(jsonObject.get("data"));
        if ("false".equals(success)) {
            SysDepartproject sysDepartproject = new SysDepartproject();
            sysDepartproject.setId("kz" + WbsId);
            sysDepartproject.setMemo("-1");
            sysDepartprojectService.updateById(sysDepartproject);
            return;
        }
        savem(WbsId, data, org_category, projId);
    }

    //wbs与末节点为两个接口，返回字段不一样分别保存数据
    //保存wbs数据
    void save(String WbsId, String wbs, int org_category, String projId) {
        JSONArray deptobjects = JSONUtil.parseArray(wbs);
        for (Object object : deptobjects) {
            JSONObject jsonObject = (JSONObject) object;
            SysDepartproject sysDepartproject = new SysDepartproject();
            String id = "kz" + jsonObject.getStr("wbs_id");
            LambdaQueryWrapper<SysDepartproject> sysDepartprojectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysDepartprojectLambdaQueryWrapper.eq(SysDepartproject::getId, id);
            SysDepartproject one = sysDepartprojectService.getOne(sysDepartprojectLambdaQueryWrapper);
            if (one != null) {
                continue;
            }
            sysDepartproject.setId(id);
            sysDepartproject.setParentId("kz" + jsonObject.getStr("parent_wbs_id"));
            sysDepartproject.setDepartName(jsonObject.getStr("wbs_name"));
            sysDepartproject.setOrgCategory(String.valueOf(org_category));

            //获取上级数据
            QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", "kz" + jsonObject.getStr("parent_wbs_id"));
            List<SysDepartproject> list = sysDepartprojectService.list(queryWrapper);
            String orgCode1 = list.get(0).getOrgCode();

            //获取向同级数量
            QueryWrapper<SysDepartproject> queryWrappertj = new QueryWrapper<>();
            queryWrappertj.eq("parent_id", "kz" + jsonObject.getStr("parent_wbs_id"));
            List<SysDepartproject> listtj = sysDepartprojectService.list(queryWrappertj);
            int size = listtj.size();
            String orgcode = "";
            if (size >= 0 && size < 10) {
                orgcode = orgCode1 + "A0" + size;
            } else if (size >= 10 || size < 100) {
                orgcode = orgCode1 + "A" + size;
            } else if (size >= 100 && size < 110) {
                size = size - 100;
                orgcode = orgCode1 + "B0" + size;
            } else if (size >= 110 && size < 200) {
                size = size - 100;
                orgcode = orgCode1 + "B" + size;
            } else if (size >= 200 && size < 210) {
                size = size - 200;
                orgcode = orgCode1 + "C0" + size;
            } else if (size >= 210 && size < 300) {
                size = size - 200;
                orgcode = orgCode1 + "C" + size;
            } else if (size >= 300 && size < 310) {
                size = size - 300;
                orgcode = orgCode1 + "D0" + size;
            } else if (size >= 310 && size < 400) {
                size = size - 300;
                orgcode = orgCode1 + "D" + size;
            }
            sysDepartproject.setOrgCode(orgcode);
            sysDepartproject.setCreateTime(jsonObject.getDate("addDate"));
            String child_nodes = jsonObject.getStr("child_nodes");
            if (child_nodes.equals("0")) {
                sysDepartproject.setMemo("0");//0表示有子节点
            } else {
                sysDepartproject.setMemo("10");//0表示有子节点
            }
            sysDepartproject.setDelFlag("0");
            sysDepartproject.setCreateBy("王凯");
            String cogcodes = "A05A01A04A10A01A01";
            if (projId.equals("81504")) {
                cogcodes = "A05A01A04A10A02A01";
            }
            sysDepartproject.setOrgCodes(cogcodes);
            sysDepartprojectService.save(sysDepartproject);
            System.out.println("保存成功！" + jsonObject.getStr("cellName") + "----------" + orgcode);
            SysDepartproject sysDepartproject1 = new SysDepartproject();
            sysDepartproject1.setId("kz" + jsonObject.getStr("parent_wbs_id"));
            sysDepartproject1.setMemo("-1");
            sysDepartprojectService.updateById(sysDepartproject1);
        }
    }

    //保存wbs末节点数据
    void savem(String WbsId, String wbs, int org_category, String projId) {
        JSONArray deptobjects = JSONUtil.parseArray(wbs);
        for (Object object : deptobjects) {
            JSONObject jsonObject = (JSONObject) object;
            SysDepartproject sysDepartproject = new SysDepartproject();
            String id = "kz" + jsonObject.getStr("cellId");
            LambdaQueryWrapper<SysDepartproject> sysDepartprojectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysDepartprojectLambdaQueryWrapper.eq(SysDepartproject::getId, id);
            SysDepartproject one = sysDepartprojectService.getOne(sysDepartprojectLambdaQueryWrapper);
            if (one != null) {
                continue;
            }
            sysDepartproject.setId(id);
            sysDepartproject.setParentId("kz" + WbsId);
            sysDepartproject.setDepartName(jsonObject.getStr("cellName"));
            sysDepartproject.setOrgCategory(String.valueOf(org_category));

            //获取上级数据
            QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", "kz" + WbsId);
            List<SysDepartproject> list = sysDepartprojectService.list(queryWrapper);
            String orgCode1 = list.get(0).getOrgCode();

            //获取向同级数量
            QueryWrapper<SysDepartproject> queryWrappertj = new QueryWrapper<>();
            queryWrappertj.eq("parent_id", "kz" + WbsId);
            List<SysDepartproject> listtj = sysDepartprojectService.list(queryWrappertj);
            int size = listtj.size();
            String orgcode = "";
            if (size >= 0 && size < 10) {
                orgcode = orgCode1 + "A0" + size;
            } else if (size >= 10 || size < 100) {
                orgcode = orgCode1 + "A" + size;
            } else if (size >= 100 && size < 110) {
                size = size - 100;
                orgcode = orgCode1 + "B0" + size;
            } else if (size >= 110 && size < 200) {
                size = size - 100;
                orgcode = orgCode1 + "B" + size;
            } else if (size >= 200 && size < 210) {
                size = size - 200;
                orgcode = orgCode1 + "C0" + size;
            } else if (size >= 210 && size < 300) {
                size = size - 200;
                orgcode = orgCode1 + "C" + size;
            } else if (size >= 300 && size < 310) {
                size = size - 300;
                orgcode = orgCode1 + "D0" + size;
            } else if (size >= 310 && size < 400) {
                size = size - 300;
                orgcode = orgCode1 + "D" + size;
            }
            sysDepartproject.setOrgCode(orgcode);
            sysDepartproject.setCreateTime(jsonObject.getDate("addDate"));
            sysDepartproject.setMemo("-1");//0表示无子节点
            sysDepartproject.setDelFlag("0");
            sysDepartproject.setCreateBy("王凯");
            String cogcodes = "A05A01A04A10A01A01";
            if (projId.equals("81504")) {
                cogcodes = "A05A01A04A10A02A01";
            }
            sysDepartproject.setOrgCodes(cogcodes);
            sysDepartprojectService.save(sysDepartproject);
            System.out.println("保存成功！" + jsonObject.getStr("cellName") + "----------" + orgcode);
            SysDepartproject sysDepartproject1 = new SysDepartproject();
            sysDepartproject1.setId("kz" + WbsId);
            sysDepartproject1.setMemo("-1");
            sysDepartprojectService.updateById(sysDepartproject1);
        }
    }

    //用户登录
    private void userlogin() {
        String url1 = "http://112.95.76.11:8000/gapp/userlogin.action?userId=13302948&userPass=d7b92ac90e93355ffeba35186268f43b&loginTag=0";
        HttpRequest.post(url1)
                .header("Content-Type", "application/json")
                .execute()
                .body();
    }
}
