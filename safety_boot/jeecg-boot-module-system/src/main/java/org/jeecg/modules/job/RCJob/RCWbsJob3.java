package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
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

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCWbsJob3 implements Job {
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
            String sectionID = section.getSectionid();// "1589533624112381952";
            String sysorgcode = section.getSysOrgCode();
            String token = null;
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
            String back = HttpRequest.get("https://yggc.cncico.com:1080/api/measure/wbs/examples?projectId=" + projectId + "&sectionId=" + sectionID +"&pageSize=5000"+ "&pageNum=1")
                    .header("clientid", "app-ext-iot")
                    .header("clientSecret", clientSecret)
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .execute()
                    .body();
            JSONObject jsonObject = new JSONObject(back);
            JSONObject model = (JSONObject) jsonObject.get("model");
            Integer total = (Integer) model.get("pages");

            int i = total;

            if (i > 0) {
                for (int j = 1; j < i + 1; j++) {
                    String back2 = HttpRequest.get("https://yggc.cncico.com:1080/api/measure/wbs/examples?projectId=" + projectId + "&sectionId=" + sectionID +"&pageSize=5000"+ "&pageNum=" + j)
                            .header("clientid", "app-ext-iot")
                            .header("clientSecret", clientSecret)
                            .header("Authorization", String.format("%s %s", "Bearer", token))
                            .execute()
                            .body();
                    JSONObject jsonObject2 = new JSONObject(back2);
                    String code = (String) jsonObject2.get("code");

                    if (code.equals("00000")) {
                        JSONObject model2 = (JSONObject) jsonObject2.get("model");
                        List<?> list = (List<?>) model2.get("list");
                        if (list.equals("[]")) {
                            log.info("无WBS数据！");
                            return;
                        }
                        save(list,sysorgcode,sectionID);
                    }
                    log.info("J:"+j+"------------------------------------------i:"+i);
                }
                jtwbsService.updateById(section);

            }
        }
    }


    Boolean save(List<?> data,String sysorgcode,String section) throws JobExecutionException {
        JSONObject jsonObject = new JSONObject();
        try {
            for (Object object : data) {

                 jsonObject = (JSONObject) object;
//                log.info("---" + jsonObject);
                SysDepartproject sysDepartproject = new SysDepartproject();

                String projectType = jsonObject.getStr("projectType");
                String match = matchList.get(projectType);
                if (StringUtils.isEmpty(match) ) {// 过滤掉 评定和工序,和删除的数据 || Integer.valueOf(jsonObject.getStr("isDeleted")) == 1
                    continue;
                }
                sysDepartproject.setOrgType(match);
                String id = jsonObject.getStr("nodeId");
                String parentId = jsonObject.getStr("parentId");
                sysDepartproject.setId(id);
                sysDepartproject.setParentId(parentId);
                sysDepartproject.setDepartName(jsonObject.getStr("name"));
                sysDepartproject.setOldid(section);

                Integer sort = 1;
                if(StringUtils.isNotBlank(jsonObject.getStr("sort"))){
                    sort  = Integer.valueOf(jsonObject.getStr("sort"));
                }

                Format fm = new DecimalFormat("0000");
                String treeid = jsonObject.getStr("longIds");

                SysDepartproject byId = sysDepartprojectService.getById(parentId);
                String orgcode = "";
                if(byId != null ){
                    orgcode = byId.getOrgCode()+"A"+fm.format(Integer.valueOf(id.length()>4?id.substring(id.length() - 4):id));
                }else{
                    if(treeid.contains("||")){
                        continue;
                    }
                    String[] split = treeid.split("\\|");
                    // 截取id后四位
                     orgcode = Arrays.stream(split).map(String::toString).map(s->s="A"+fm.format(Integer.valueOf(s.length()>4?s.substring(s.length() - 4):s))).collect(Collectors.joining());
                }


                sysDepartproject.setDelFlag(jsonObject.getStr("isDeleted"));
                sysDepartproject.setDepartOrder(sort);
                sysDepartproject.setDescription(jsonObject.getStr("shortName"));
                sysDepartproject.setCreateTime(jsonObject.getDate("createTime"));
                sysDepartproject.setUpdatime(jsonObject.getDate("updateTime"));
                sysDepartproject.setTemplateid(jsonObject.getStr("templateId"));
                sysDepartproject.setFullsort(jsonObject.getStr("fullSort"));
                sysDepartproject.setTreeid(treeid);
                sysDepartproject.setOrgCodes(sysorgcode);
                if(StringUtils.isNotBlank(jsonObject.getStr("wbsStructureType"))){
                    sysDepartproject.setWbsStructureType(jsonObject.getStr("wbsStructureType"));
                }
                boolean save = false;
                boolean update = false;
                QueryWrapper<SysDepartproject> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id", jsonObject.getStr("nodeId"));
                List<SysDepartproject> list1 = sysDepartprojectService.list(queryWrapper1);
                sysDepartproject.setOrgCode(orgcode);
                if (list1.size() > 0) {

                    if(sysDepartproject.getParentId().equals("-1" ) ){
                        // 父id=-1 就跳过更新
                        log.info("wbs 父id=-1 跳过更新！！！！！！！！！！！！！！！！！！！！！  " + jsonObject.getStr("nodeId"));
                        continue;
                    }else{

                        update = updateReSetCode(sysDepartproject, queryWrapper1);
//                        try {
//                            update = sysDepartprojectService.update(sysDepartproject, queryWrapper1);
//                        } catch (Exception e) {
//                            Throwable cause = e.getCause();
//                            if (cause instanceof SQLIntegrityConstraintViolationException) {
//                                StringBuilder sb = new StringBuilder(sysDepartproject.getOrgCode());
//                                sb.replace(sb.length()-5, sb.length()-4, "B");
//                                sysDepartproject.setOrgCode(sb.toString());
//                                update = sysDepartprojectService.update(sysDepartproject, queryWrapper1);
//                                log.info("修改wbs重置Orgcode！！！！！！！！！！！！！！！！！！！！！！！！！  " + sysDepartproject.getOrgCode());
//                            }else{
//                                log.info("更新wbs报错！ jsonObject "+jsonObject );
//                                log.info("更新wbs报错！ sysDepartproject  "+sysDepartproject );
//                            }
//
//                        }


                    }

                } else {
                    save = saveReSetCode(sysDepartproject);

//                    try {
//                        save = sysDepartprojectService.save(sysDepartproject);
//                    } catch (Exception e) {
//                        Throwable cause = e.getCause();
//                        log.info("添加数据库 wbs报错！！！！！！！！！！！！！！！！！！！！！！！！！  " + jsonObject);
//                        if (cause instanceof SQLIntegrityConstraintViolationException) {
//                            StringBuilder sb = new StringBuilder(sysDepartproject.getOrgCode());
//
//                            sb.replace(sb.length()-5, sb.length()-4, "B");
//                            sysDepartproject.setOrgCode(sb.toString());
//                            save = sysDepartprojectService.save(sysDepartproject);
//                            log.info("添加wbs重置Orgcode！！！！！！！！！！！！！！！！！！！！！！！！！  " + sysDepartproject.getOrgCode());
//                        }
//
////                        return false;
//                    }
                }
                if (save) {
                    log.info("添加成功！  " + sysDepartproject.getDepartName() + "----------------" + sysDepartproject.getOrgCode());
                }
                if (update) {
                    log.info("修改成功！  " + sysDepartproject.getDepartName() + "----------------" + sysDepartproject.getOrgCode());
                }
            }
        }catch (Exception e){
            log.info("添加wbs报错！  "+jsonObject );


        }return true;
    }

    Boolean saveReSetCode( SysDepartproject sysDepartproject){

            try {
                return  sysDepartprojectService.save(sysDepartproject);
            }catch (Exception e){
                Throwable cause = e.getCause();
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    StringBuilder sb = new StringBuilder(sysDepartproject.getOrgCode());
                    char c = sb.charAt(sb.length()-5);
                    int b = (int)c+1;
//            int value =  sb.substring(sb.length()-5, sb.length()-4);
                    sb.replace(sb.length()-5, sb.length()-4, String.valueOf((char)b));
                    sysDepartproject.setOrgCode(sb.toString());
                    log.info("添加数据库 wbs报错！！！！！！！！！！！！！！！！！！！！！！！！！  " + sysDepartproject);
                  return  saveReSetCode(sysDepartproject);
                }else{
                    log.info("添加 wbs报错 其他！！！！！！！！！！！！！！！！！！！！！！！！！  " + sysDepartproject);
                    return false;
                }

        }

    }

    Boolean updateReSetCode( SysDepartproject sysDepartproject,QueryWrapper<SysDepartproject> queryWrapper1){

        try {
            return  sysDepartprojectService.update(sysDepartproject, queryWrapper1);
        }catch (Exception e){
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                StringBuilder sb = new StringBuilder(sysDepartproject.getOrgCode());
                char c = sb.charAt(sb.length()-5);
                int b = (int)c+1;
//            int value =  sb.substring(sb.length()-5, sb.length()-4);
                sb.replace(sb.length()-5, sb.length()-4, String.valueOf((char)b));
                sysDepartproject.setOrgCode(sb.toString());
                log.info("更新数据库 wbs报错！！！！！！！！！！！！！！！！！！！！！！！！！  " + sysDepartproject);
                return  updateReSetCode(sysDepartproject,queryWrapper1);
            }else{
                log.info("更新 wbs报错 其他！！！！！！！！！！！！！！！！！！！！！！！！！  " + sysDepartproject);
                return false;
            }

        }

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
        put("70", "13");// 工序


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
