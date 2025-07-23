package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.trtm.iot.jtwbs.service.IJtwbsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.dao.DataAccessException;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.impl.SysDepartprojectServiceImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCWbsJob2 implements Job {
    @Autowired
    private SysDepartprojectServiceImpl sysDepartprojectService;
    @Autowired
    private IJtwbsService jtwbsService;


//    public static void main(String[] args) {
//        System.out.println(("----- selectAll method test ------"+String.valueOf(UUID.randomUUID())));
//    }

    //  曹宅项目id 1589527033623478272
    // 甬金衢上  1625043312800243712
//
//    标段id
//            甬金衢上
//1630127688098680832 SG01
//1630128171756457984 SG02
//1630128975036977152 SG03
//            曹宅
//1589533624112381952 TJ01


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {


        List<Jtwbs> list1 = jtwbsService.list();
        for (Jtwbs section : list1) {

        String projectId = section.getProjectid() ; // 瑞苍： 1568184095016534016【TJ01：1569875717703254016; TJ02	1569876118934568960;TJ03 1569876523689099264 TJ04:1569876856767168512】；
        String sectionId = section.getSectionid();// "1589533624112381952";
            String sysorgcode = section.getSysOrgCode();
            String token = null;
            //---------- 获取token
            String clientSecret = RSAUtil.queryById22("KJXOVlay6Bw0SDgp");
            String back1 = HttpRequest.post("https://yggc.cncico.com:1080/api/login/appToken")
                    .header("clientid", "app-ext-iot")
                    .header("clientSecret", clientSecret)
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back1);
            String code1 = (String) jsonObject1.get("code");
            if (code1.equals("00000")) {
                JSONObject model = (JSONObject) jsonObject1.get("model");
                token = (String) model.get("access_token");
            }

            //--------- 请求数据传参
            JSONObject postobj=new JSONObject();
            postobj.set("projectId",projectId);
            postobj.set("sectionId",sectionId);

            Integer total = 0;

            for (int i = 0; i < total+1; i++) {
                //--------- 请求数据
                String back = HttpRequest.post("https://yggc.cncico.com:1080/api/tasks/transferTasks/aboutWbs/actions/execute?upperLimit=5000")
//                .header("clientid", "app-ext-iot")
//                .header("clientSecret", clientSecret)
                        .header("Content-Type", "application/json")
                        .header("Authorization", String.format("%s %s", "Bearer", token))
                        .body(String.valueOf(postobj))
                        .execute()
                        .body();
                JSONObject jsonObject = new JSONObject(back);
                JSONObject model = (JSONObject) jsonObject.get("model");
                if(i==0){
                    total = (Integer) model.get("beUpdatedTotal")/5000;
                }

                String confirmCode = (String) model.get("confirmCode");

                String code = (String) jsonObject.get("code");
                if (code.equals("00000")) {
                    List<?> list = (List<?>) model.get("data");
                    if (list.equals("[]") || list.size()==0) {
                        log.info("无WBS数据！");
                        continue;
                    }
                    Boolean saveAndUpdate = save(list, sysorgcode);
                    if(saveAndUpdate){
                        // -----------成功就进行数据确认
                        String confirm = HttpRequest.post("https://yggc.cncico.com:1080/api/tasks//transferTasks/aboutWbs/actions/confirm/"+confirmCode)
                                .header("Content-Type", "application/json")
                                .header("Authorization", String.format("%s %s", "Bearer", token))
                                .body(String.valueOf(postobj))
                                .execute()
                                .body();
                    }else{
                        i=i-1;
                        // ----------------失败就进行 重置
                        String confirm = HttpRequest.post("https://yggc.cncico.com:1080/api/tasks//transferTasks/aboutWbs/actions/cancel/"+confirmCode)
                                .header("Content-Type", "application/json")
                                .header("Authorization", String.format("%s %s", "Bearer", token))
                                .body(String.valueOf(postobj))
                                .execute()
                                .body();
                        return;
                    }
                }
            }

            log.info("WBS数据更新结束！--------"+section.getName());
            jtwbsService.updateById(section);

    }
    }


    Boolean save(List<?> data,String sysorgcode) throws JobExecutionException {
        JSONObject jsonObject = new JSONObject();
        try {
            for (Object object : data) {
                 jsonObject = (JSONObject) object;

                SysDepartproject sysDepartproject = new SysDepartproject();

                String projectType = jsonObject.getStr("projectType");
                String match = matchList.get(projectType);
                if (StringUtils.isEmpty(match) || Integer.valueOf(jsonObject.getStr("isDeleted")) == 1) {// 过滤掉 评定和工序,和删除的数据
                    continue;
                }
                sysDepartproject.setOrgType(match);
                String id = jsonObject.getStr("nodeId");
                sysDepartproject.setId(id);
                sysDepartproject.setParentId(jsonObject.getStr("parentId"));
                sysDepartproject.setDepartName(jsonObject.getStr("name"));

                Integer sort = 1;
                if(StringUtils.isNotBlank(jsonObject.getStr("sort"))){
                    sort  = Integer.valueOf(jsonObject.getStr("sort"));
                }

                Format fm = new DecimalFormat("0000");

                // 截取id后四位
                String treeid = jsonObject.getStr("longIds");
                if(treeid.contains("||")){
                    continue;
                }
                String[] split = treeid.split("\\|");
                String orgcode ="";
                try {
                     orgcode = Arrays.stream(split).map(String::toString).map(s->s="A"+fm.format(Integer.valueOf(s.length()>4?s.substring(s.length() - 4):s))).collect(Collectors.joining());
                }catch (Exception e){
                    log.info("添加wbs报错！  " + jsonObject);
                    continue;
                }


                sysDepartproject.setDelFlag(jsonObject.getStr("isDeleted"));
                sysDepartproject.setDepartOrder(sort);
                sysDepartproject.setDescription(jsonObject.getStr("shortName"));
                sysDepartproject.setCreateTime(jsonObject.getDate("createTime"));
                sysDepartproject.setUpdatime(jsonObject.getDate("updateTime"));
                sysDepartproject.setTreeid(treeid);
                sysDepartproject.setOrgCodes(sysorgcode);
                if(StringUtils.isNotBlank(jsonObject.getStr("wbsStructureType"))){
                    sysDepartproject.setWbsStructureType(jsonObject.getStr("wbsStructureType"));
                }
                boolean save = false;
                boolean update = false;
                QueryWrapper<SysDepartproject> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id", jsonObject.getStr("nodeId"));
                sysDepartproject.setOrgCode(orgcode);
                List<SysDepartproject> list1 = sysDepartprojectService.list(queryWrapper1);
                if (list1.size() > 0) {
                    continue;
//                    if(sysDepartproject.getParentId() == "-1"){
//                        // 父id=-1 就跳过更新
//                        continue;
//                    }
//                    update = sysDepartprojectService.update(sysDepartproject, queryWrapper1);
                } else {

                    try {
                        save = sysDepartprojectService.save(sysDepartproject);
                    } catch (Exception e) {
                        Throwable cause = e.getCause();
                        log.info("添加数据库 wbs报错！！！！！！！！！！！！！！！！！！！！！！！！！  " + jsonObject);
                        if (cause instanceof SQLIntegrityConstraintViolationException) {
                            StringBuilder sb = new StringBuilder(sysDepartproject.getOrgCode());
                            sb.replace(sb.length()-5, sb.length()-4, "B");
                            sysDepartproject.setOrgCode(sb.toString());
                            save = sysDepartprojectService.save(sysDepartproject);
                            log.info("添加wbs重置Orgcode！！！！！！！！！！！！！！！！！！！！！！！！！  " + sysDepartproject.getOrgCode());
                        }

//                        return false;
                    }
                }
                if (save) {
                    log.info("添加成功！  " + sysDepartproject.getDepartName() + "----------------" + sysDepartproject.getOrgCode());
                }
                if (update) {
                    log.info("修改成功！  " + sysDepartproject.getDepartName() + "----------------" + sysDepartproject.getOrgCode());
                }
            }
        }catch (Exception e){
            log.info("添加wbs报错！  " + jsonObject);
            return false;

        }return true;
    }

    private static final HashMap<String, String> matchList = new HashMap<String, String>() {{
        put("10", "1");// 工程项目
        put("20", "2");// 施工合同段
        put("30", "3");// 单位工程
//        put("32", "4");// 单位工程评定
        put("35", "5");// 子单位工程
        put("40", "6");// 分部工程
//        put("42", "7");// 分部工程评定
        put("45", "8");// 子分部工程
        put("50", "9");// 分项工程
//        put("52", "10");// 分项工程评定
//        put("53", "");// 分项工程开工报告
        put("55", "11");// 子分项工程
        put("60", "12");// 具体部位
//        put("70", "13");// 工序


    }};

    String match1(String projectType) {
        String org_category = null;
        if (projectType.equals("10")) {
            org_category = "1";//
        } else if (projectType.equals("20")) {
            org_category = "2";//
        } else if (projectType.equals("30")) {
            org_category = "3";//
        } else if (projectType.equals("32")) {
            org_category = "4";//
        } else if (projectType.equals("35")) {
            org_category = "5";//
        } else if (projectType.equals("40")) {
            org_category = "6";//
        } else if (projectType.equals("42")) {
            org_category = "7";//
        } else if (projectType.equals("45")) {
            org_category = "8";//
        } else if (projectType.equals("50")) {
            org_category = "9";//
        } else if (projectType.equals("52")) {
            org_category = "10";//
        } else if (projectType.equals("55")) {
            org_category = "11";//
        } else if (projectType.equals("60")) {
            org_category = "12";//
        } else if (projectType.equals("70")) {
            org_category = "13";//
        }
        return org_category;
    }
}
