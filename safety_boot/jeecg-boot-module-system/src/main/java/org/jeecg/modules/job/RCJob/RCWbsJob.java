package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.alibaba.druid.sql.visitor.functions.Char;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.trtm.iot.jtwbs.service.IJtwbsService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.common.value.qual.StaticallyExecutable;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.impl.SysDepartprojectServiceImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCWbsJob implements Job {
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
            String clientSecret = RSAUtil.main();
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
        String back = HttpRequest.get("https://yggc.cncico.com:1080/api/measure/wbs/examples?projectId=" + projectId + "&sectionId=" + sectionID + "&pageNum=1")
                .header("clientid", "app-ext-iot")
                .header("clientSecret", clientSecret)
                .header("Authorization", String.format("%s %s", "Bearer", token))
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(back);
        JSONObject model = (JSONObject) jsonObject.get("model");
        Integer total = (Integer) model.get("total");

        int i = 0;
        if (total % 20 != 0) {
            i = total / 20 + 1;
        } else {
            i = total / 20;
        }
        if (i > 0) {
            for (int j = 1; j < i + 1; j++) {
                String back2 = HttpRequest.get("https://yggc.cncico.com:1080/api/measure/wbs/examples?projectId=" + projectId + "&sectionId=" + sectionID + "&pageNum=" + j)
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
                    save(list,sysorgcode);
                }
            }


        }
    }
    }


    void save(List<?> data,String sysorgcode) {
        for (Object object : data) {
            JSONObject jsonObject = (JSONObject) object;
//            if (jsonObject.getStr("nodeId").equals("1613344818111578112")) {
//                System.out.println(jsonObject.getStr("nodeId"));
//            }if (jsonObject.getStr("nodeId").equals("1613344819072073728")) {
//                System.out.println(jsonObject.getStr("nodeId"));
//            }

            SysDepartproject sysDepartproject = new SysDepartproject();
            sysDepartproject.setId(jsonObject.getStr("nodeId"));
            sysDepartproject.setParentId(jsonObject.getStr("parentId"));
            sysDepartproject.setDepartName(jsonObject.getStr("name"));
            String projectType = jsonObject.getStr("projectType");
            String match = matchList.get(projectType);
            if (match == null ) {// 过滤掉 评定和工序
                continue;
            }
            sysDepartproject.setOrgType(match);
            QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", jsonObject.getStr("parentId"));
            // 查找其父节点
            List<SysDepartproject> list = sysDepartprojectService.list(queryWrapper);
            // 查找同类有多少
            String orgCode1 = "";
//            String orgCodes = "";
            if (list.size() == 0) {
                orgCode1 = "";
            } else {
                // 所属用户组织机构
//                orgCodes = list.get(0).getOrgCodes();
                // 分部分项
                orgCode1 = list.get(0).getOrgCode();
            }
            String id = jsonObject.getStr("id");
            Integer sort = Integer.valueOf(jsonObject.getStr("sort"));
            Format fm = new DecimalFormat("000");

            // 截取id后三位
            String orgcode = orgCode1 + "A" + fm.format(Integer.valueOf(id.substring(id.length() - 3)));


            sysDepartproject.setDelFlag(jsonObject.getStr("isDeleted"));
            sysDepartproject.setDepartOrder(sort);
            sysDepartproject.setDescription(jsonObject.getStr("longName"));
            sysDepartproject.setCreateBy(jsonObject.getStr("createPerson"));
            sysDepartproject.setUpdateBy(jsonObject.getStr("updatePerson"));
            sysDepartproject.setCreateTime(jsonObject.getDate("createTime"));
            sysDepartproject.setUpdatime(jsonObject.getDate("updateTime"));
//            sysDepartproject.setOldid(jsonObject.getStr("org_id"));
//            sysDepartproject.setTreeid(jsonObject.getStr("treeid"));
            sysDepartproject.setOrgCodes(sysorgcode);
            boolean save = false;
            boolean update = false;
            QueryWrapper<SysDepartproject> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id", jsonObject.getStr("nodeId"));
            List<SysDepartproject> list1 = sysDepartprojectService.list(queryWrapper1);
            if (list1.size() > 0) {
                update = sysDepartprojectService.update(sysDepartproject, queryWrapper1);
            } else {
                sysDepartproject.setOrgCode(orgcode);
                try {
                    save = sysDepartprojectService.save(sysDepartproject);
                } catch (Exception e) {
                    QueryWrapper<SysDepartproject> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.likeRight("org_code", orgCode1);
                    queryWrapper2.eq("org_type", match);
                    List<SysDepartproject> list2 = sysDepartprojectService.list(queryWrapper2);
                    int size = list2.size() + 1;
                    String orgcode1 = "";
                    int i = size / 100;
                    char ch = (char)(65 + i);
                    int remainder = size % 100;
                    String orgcodeEnd = "";
                    if (String.valueOf(remainder).length()>1){
                        orgcodeEnd = "" + ch + size;
                    }else {
                        orgcodeEnd = ch + "0" + size;
                    }
                    orgcode1 = orgcode1 + orgcodeEnd;
//                    if (size < 10) {
//                        orgcode1 = orgCode1 + "A0" + size;
//                    } else if (size >= 10 && size < 100) {
//                        orgcode1 = orgCode1 + "A" + size;
//                    }
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
