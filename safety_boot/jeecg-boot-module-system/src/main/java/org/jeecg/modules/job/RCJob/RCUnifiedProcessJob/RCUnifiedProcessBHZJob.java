package org.jeecg.modules.job.RCJob.RCUnifiedProcessJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.RCJob.RSAUtil;
import org.jeecg.modules.job.RCJob.unifiedProcessReqVO.*;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@RestController
@RequestMapping("/RCUnifiedProcessBHZJob")
public class RCUnifiedProcessBHZJob implements Job {
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserService sysUserService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {


        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_UNIDIED_PROCESS);//拌合站统一待办
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到拌合站统一待办定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输拌合站统一待办推送的设备" + DateUtils.now()));
            return;
        }
        String orgCodeList = jsonObject.getStr("orgCodeList");
        String[] split = orgCodeList.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<String> shebeiList = new ArrayList<>();
        for (String s : strsToList1) {
            List<String> shebei = shebeiInfoService.selectSBListByCodeList(s);
            if (shebei != null || shebeiList.size() != 0) {
                shebeiList.addAll(shebei);
            }
        }
        if (shebeiList == null || shebeiList.size() == 0) {
            log.info(String.format("暂无拌合站统一待办推送的设备：没有配置代办人的信息" + DateUtils.now()));
            return;
        }
        Integer alertstate = 1;
        Integer overLevel = 0;
        List<BhzCementBase> bhzCementBaseList = bhzCementBaseService.selectBHZUnifiedProcess(curid, alertstate, shebeiList, overLevel);
        if (null == bhzCementBaseList || bhzCementBaseList.size() == 0) {
            log.info(String.format("暂无拌合站统一待办的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzCementBase bhzCementBase : bhzCementBaseList) {
            id = bhzCementBase.getId();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            unifiedProcessAddReqVO unifiedProcessAddReqVO = new unifiedProcessAddReqVO();
//            unifiedProcessAddReqVO.setStartUserId(0000);
            unifiedProcessAddReqVO.setStartUserName("无");
            unifiedProcessAddReqVO.setStartTime(simpleDateFormat.format(new Date()));
            unifiedProcessAddReqVO.setProcessId(bhzCementBase.getBatchNo());
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(bhzCementBase.getShebeiNo());
            if (selectshebeione != null) {
                unifiedProcessAddReqVO.setProcessName(selectshebeione.getSbname() + "超标预警");
            }
            unifiedProcessAddReqVO.setProcessStatus(0);
            SysDepart queryone = sysDepartService.queryone(selectshebeione.getSysOrgCode());
            if (queryone != null) {
                unifiedProcessAddReqVO.setSectionId(queryone.getSectionid());
                unifiedProcessAddReqVO.setProjectId(queryone.getProjectid());
            }

            unifiedProcessAddReqVO.setSourceId(20230523);
            unifiedProcessAddReqVO.setSourceName("测试");
//            unifiedProcessAddReqVO.setPostId(0000);
//            unifiedProcessAddReqVO.setPostName("无");
            unifiedProcessAddReqVO.setProcessType("超标预警");
            BhzCallCfg bhzCallCfg =  bhzCallCfgService.selectbhzcallone(bhzCementBase.getShebeiNo());

            unifiedProcessAddReqVO.setPcUrl("http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/loginSs/bhzxx?username=iot&content="+bhzCementBase.getBatchNo());

            // 使用 Jackson 库将实体类转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(unifiedProcessAddReqVO);
            System.out.println(json);
            String token = null;
            String clientSecret = RSAUtil.queryById22("KJXOVlay6Bw0SDgp");
            String back1 = HttpRequest.post("http://47.96.161.157:1080/api/login/appToken")
                    .header("Content-Type", "application/json")
                    .header("clientid", "app-ext-iot")
                    .header("clientSecret", clientSecret)
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back1);
            String code1 = (String) jsonObject1.get("code");
            if (code1.equals("00000")) {

                JSONObject model = (JSONObject) jsonObject1.get("model");
                token = (String) model.get("access_token");
                String post = HttpRequest.post("http://47.96.161.157:1080/api/unified/processes")
                        .header("Content-Type", "application/json")
                        .header("Authorization", String.format("%s %s", "Bearer", token))
                        .body(json)
                        .execute()
                        .body();
                JSONObject jsonObject2 = new JSONObject(post);
                String code2 = (String) jsonObject2.get("code");
                if (code2.equals("00000")) {
                    unifiedTaskAddReqVO chuzhi = new unifiedTaskAddReqVO();
                    SysUser userByName = sysUserService.getUserByName(bhzCallCfg.getCzperson());
                    chuzhi.setAssigneeId(Long.parseLong(userByName.getGxid()));
                    chuzhi.setAssigneeName(userByName.getRealname());
                    chuzhi.setTaskId(bhzCementBase.getBatchNo() + "-10");
                    chuzhi.setProjectId(queryone.getProjectid());
                    chuzhi.setSectionId(queryone.getSectionid());
                    chuzhi.setProcessId(bhzCementBase.getBatchNo());
                    chuzhi.setProcessName(selectshebeione.getSbname() + "超标预警");
                    chuzhi.setStartTime(simpleDateFormat.format(new Date()));
                    chuzhi.setPcRedirectUrl("http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/loginSs/bhzcz?username="+userByName.getUsername()+"&content="+bhzCementBase.getBatchNo());
                    chuzhi.setSourceId(20230523);
                    chuzhi.setSourceName("测试");
                    chuzhi.setTaskStatus(0);
                    chuzhi.setEndTime(null);
                    chuzhi.setTaskName("施工处置");
                    chuzhi.setTaskStatus(0);
                    unifiedTaskAddReqVO jianliSP = new unifiedTaskAddReqVO();
                    SysUser userByName2 = sysUserService.getUserByName(bhzCallCfg.getShperson());
                    jianliSP.setAssigneeId(Long.parseLong(userByName2.getGxid()));
                    jianliSP.setAssigneeName(userByName2.getRealname());
                    jianliSP.setTaskId(bhzCementBase.getBatchNo() + "-40");
                    jianliSP.setProjectId(queryone.getProjectid());
                    jianliSP.setSectionId(queryone.getSectionid());
                    jianliSP.setProcessId(bhzCementBase.getBatchNo());
                    jianliSP.setProcessName(selectshebeione.getSbname() + "超标预警");
                    jianliSP.setStartTime(simpleDateFormat.format(new Date()));
                    jianliSP.setPcRedirectUrl("http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/loginSs/bhzsh?username="+userByName.getUsername()+"&content="+bhzCementBase.getBatchNo());
                    jianliSP.setTaskStatus(0);
                    jianliSP.setSourceId(20230523);
                    jianliSP.setSourceName("测试");
                    jianliSP.setEndTime(null);
                    jianliSP.setTaskName("监理审核");
                    jianliSP.setTaskStatus(0);
                    unifiedTaskAddReqVO zhbSP = new unifiedTaskAddReqVO();
                    SysUser userByName3 = sysUserService.getUserByName(bhzCallCfg.getSpperson());
                    zhbSP.setAssigneeId(Long.parseLong(userByName3.getGxid()));
                    zhbSP.setAssigneeName(userByName3.getRealname());
                    zhbSP.setTaskId(bhzCementBase.getBatchNo() + "-50");
                    zhbSP.setProjectId(queryone.getProjectid());
                    zhbSP.setSectionId(queryone.getSectionid());
                    zhbSP.setProcessId(bhzCementBase.getBatchNo());
                    zhbSP.setProcessName(selectshebeione.getSbname() + "超标预警");
                    zhbSP.setStartTime(simpleDateFormat.format(new Date()));
                    zhbSP.setPcRedirectUrl("http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/loginSs/bhzsp?username="+userByName.getUsername()+"&content="+bhzCementBase.getBatchNo());
                    zhbSP.setTaskStatus(0);
                    zhbSP.setSourceId(20230523);
                    zhbSP.setSourceName("测试");
                    zhbSP.setEndTime(null);
                    zhbSP.setTaskName("指挥部审批");
                    zhbSP.setTaskStatus(0);
                    switch (bhzCementBase.getOverLevel()) {
                        case 1:
                            addUnifiedTask(chuzhi, token);
                            break;
                        case 2:
                            addUnifiedTask(jianliSP, token);
                            addUnifiedTask(chuzhi, token);
                            break;
                        case 3:
                            addUnifiedTask(chuzhi, token);
                            addUnifiedTask(jianliSP, token);
                            addUnifiedTask(zhbSP, token);
                            break;
                    }
                }
            }
            sysConfigService.updateSysConfig(JobUtil.BHZ_UNIDIED_PROCESS, id);
        }


    }


    public void addUnifiedTask(unifiedTaskAddReqVO unifiedTaskAddReqVO, String token) throws JsonProcessingException {
        // 使用 Jackson 库将实体类转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(unifiedTaskAddReqVO);
        String task = HttpRequest.post("http://47.96.161.157:1080/api//unified/tasks")
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("%s %s", "Bearer", token))
                .body(json)
                .execute()
                .body();
        JSONObject taskObj = new JSONObject(task);
        String code2 = (String) taskObj.get("code");
        if (code2.equals("00000")) {
            log.info(String.format("拌合站统一任务待办添加成功!" + unifiedTaskAddReqVO));
        } else {
            log.info(String.format("拌合站统一任务待办添加失败!" + unifiedTaskAddReqVO));
        }
    }

    /**
     * 根据id添加或者修改处置信息
     */
    @AutoLog(value = "拌合站处置信息添加或者修改")
    @ApiOperation(value = "拌合站处置信息添加或者修改", notes = "拌合站处置信息添加或者修改")
    @GetMapping(value = "/HBZCZAddOrUpdate2")
    public Result<?> HBZCZAddOrUpdate2(BhzCementOverHandler bhzCementOverHandler,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String people = String.valueOf(loginUser.getRealname());
        String zhbyj = request.getParameter("zhbyj");   //指挥部意见
        String zhbbh = request.getParameter("zhbbh");   //指挥部驳回
        String zhbjg = request.getParameter("zhbjg");   //指挥部结果
        String spyj = request.getParameter("spyj");   //审批意见
        String jlbh = request.getParameter("jlbh");   //监理驳回
        String spjg = request.getParameter("spjg");   //审批结果
        String wtyy = request.getParameter("wtyy");  //问题原因
        String clfs = request.getParameter("clfs");  //处理方式
        String cljg = request.getParameter("cljg");  //处理结果
        String hntbatch = request.getParameter("hntbatch");   //id
        String bizPath = request.getParameter("fileList");  //图片
        int bhz = Integer.parseInt(request.getParameter("bhz"));
        int level = Integer.parseInt(request.getParameter("level"));
        int status = Integer.parseInt(request.getParameter("status"));
        Integer i = 0;
        int unifiedStatus = 0;
        if (bhz == 0) {
            LambdaQueryWrapper<BhzCementBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzCementBase::getBatchNo, hntbatch);
            List<BhzCementBase> list = bhzCementBaseService.list(lambdaQueryWrapper);
            for (BhzCementBase bhzCementBase : list) {
                BhzCementBase bhzCementBase1 = new BhzCementBase();
                bhzCementBase1.setId(bhzCementBase.getId());
                if (level == 1) {
                    if (status == 10) {
                        bhzCementBase1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 20);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 2) {
                    if (status == 10) {//处置
                        bhzCementBase1.setOverproofStatus(10);
                        unifiedStatus = 10;
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzCementBase1.setOverproofStatus(30);
                        unifiedStatus = 30;
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzCementBase1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 20);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 3) {
                    if (status == 10) {//处置
                        bhzCementBase1.setOverproofStatus(10);
                        unifiedStatus = 10;
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzCementBase1.setOverproofStatus(30);
                        unifiedStatus = 30;
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzCementBase1.setOverproofStatus(40);
                        unifiedStatus = 40;
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 40);
                    } else if (status == 50) {//指挥部审核
                        bhzCementBase1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = bhzCementOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, hntbatch, bizPath, people, 20);
                    } else if (status == 60) {//指挥部驳回
                        bhzCementBase1.setOverproofStatus(60);
                        unifiedStatus = 60;
                        i = bhzCementOverHandlerService.headquartersBohui(zhbbh, hntbatch, people, 60);
                    } else {
                        return Result.error("错误");
                    }
                }
                bhzCementBaseService.updateById(bhzCementBase1);
                if (i == 1) {
                    try {
                        QueryWrapper<BhzCallCfg> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("bhjno", bhzCementBase.getShebeiNo());
                        BhzCallCfg one = bhzCallCfgService.getOne(queryWrapper);
                        if (one.getCzperson() != null && !one.getCzperson().contains("未设处置人")
                                && one.getShperson() != null && !one.getShperson().contains("未设审核人")
                                && one.getSpperson() != null && !one.getSpperson().contains("未设审批人")) {
                            unifiedUpdate(bhzCementBase, unifiedStatus, status);
                        }else{
                            log.info(String.format("该设备"+bhzCementBase.getShebeiNo()+"未设置相关负责人"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        return Result.OK(i);
                    }
                }
            }
        }
        if (bhz == 1) {
            LambdaQueryWrapper<BhzLqBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzLqBases::getGuid, hntbatch);
            List<BhzLqBases> list = bhzLqBasesService.list(lambdaQueryWrapper);
            for (BhzLqBases bhzLqBases : list) {
                BhzLqBases bhzLqBases1 = new BhzLqBases();
                bhzLqBases1.setId(bhzLqBases.getId());
                if (level == 1) {
                    if (status == 10) {
                        bhzLqBases1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 20);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 2) {
                    if (status == 10) {//处置
                        bhzLqBases1.setOverproofStatus(10);
                        unifiedStatus = 10;
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzLqBases1.setOverproofStatus(30);
                        unifiedStatus = 30;
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzLqBases1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 20);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 3) {
                    if (status == 10) {//处置
                        bhzLqBases1.setOverproofStatus(10);
                        unifiedStatus = 10;
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzLqBases1.setOverproofStatus(30);
                        unifiedStatus = 30;
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzLqBases1.setOverproofStatus(40);
                        unifiedStatus = 40;
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 40);
                    } else if (status == 50) {//指挥部审核
                        bhzLqBases1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = bhzCementOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, hntbatch, bizPath, people, 20);
                    } else if (status == 60) {//指挥部驳回
                        bhzLqBases1.setOverproofStatus(60);
                        unifiedStatus = 60;
                        i = bhzCementOverHandlerService.headquartersBohui(zhbbh, hntbatch, people, 60);
                    } else {
                        return Result.error("错误");
                    }
                }
                bhzLqBasesService.updateById(bhzLqBases1);
                if (i == 1) {
                    try {
                        QueryWrapper<BhzCallCfg> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("bhjno", bhzLqBases.getShebeibianhao());
                        BhzCallCfg one = bhzCallCfgService.getOne(queryWrapper);
                        if (one.getCzperson() != null && !one.getCzperson().contains("未设处置人")
                                && one.getShperson() != null && !one.getShperson().contains("未设审核人")
                                && one.getSpperson() != null && !one.getSpperson().contains("未设审批人")) {
                            unifiedUpdatelq(bhzLqBases, unifiedStatus, status);
                        }else{
                            log.info(String.format("该设备"+bhzLqBases.getShebeibianhao()+"未设置相关负责人"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        return Result.OK(i);
                    }
                }
            }
        }
        if (bhz == 2) {
            LambdaQueryWrapper<BhzSwBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzSwBases::getGuid, hntbatch);
            List<BhzSwBases> list = bhzSwBasesService.list(lambdaQueryWrapper);
            for (BhzSwBases bhzSwBases : list) {
                BhzSwBases bhzSwBases1 = new BhzSwBases();
                bhzSwBases1.setGuid(bhzSwBases.getGuid());
                if (level == 1) {
                    if (status == 10) {
                        bhzSwBases1.setOverproofStatus(20);
                        unifiedStatus = 10;
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 20);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 2) {
                    if (status == 10) {//处置
                        bhzSwBases1.setOverproofStatus(10);
                        unifiedStatus = 10;
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzSwBases1.setOverproofStatus(30);
                        unifiedStatus = 30;
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzSwBases1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 20);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 3) {
                    if (status == 10) {//处置
                        bhzSwBases1.setOverproofStatus(10);
                        unifiedStatus = 10;
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzSwBases1.setOverproofStatus(30);
                        unifiedStatus = 30;
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzSwBases1.setOverproofStatus(40);
                        unifiedStatus = 40;
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 40);
                    } else if (status == 50) {//指挥部审核
                        bhzSwBases1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = bhzCementOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, hntbatch, bizPath, people, 20);
                    } else if (status == 60) {//指挥部驳回
                        bhzSwBases1.setOverproofStatus(60);
                        unifiedStatus = 60;
                        i = bhzCementOverHandlerService.headquartersBohui(zhbbh, hntbatch, people, 60);
                    } else {
                        return Result.error("错误");
                    }
                }
                bhzSwBasesService.updateByGuid(bhzSwBases1);
                if (i == 1) {
                    try {
                        QueryWrapper<BhzCallCfg> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("bhjno", bhzSwBases.getShebeibianhao());
                        BhzCallCfg one = bhzCallCfgService.getOne(queryWrapper);
                        if (one.getCzperson() != null && !one.getCzperson().contains("未设处置人")
                                && one.getShperson() != null && !one.getShperson().contains("未设审核人")
                                && one.getSpperson() != null && !one.getSpperson().contains("未设审批人")) {
                            unifiedUpdateSw(bhzSwBases, unifiedStatus, status);
                        }else{
                            log.info(String.format("该设备"+bhzSwBases.getShebeibianhao()+"未设置相关负责人"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        return Result.OK(i);
                    }
                }
            }
        }

        return Result.OK(i);
    }

    private void unifiedUpdateSw(BhzSwBases bhzSwBases, int unifiedStatus, int status) throws JsonProcessingException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //任务
        unifiedTaskUpdateReqVO unifiedTaskUpdateReqVO = new unifiedTaskUpdateReqVO();
//        unifiedTaskUpdateReqVO.setStartTime(simpleDateFormat.format(bhzCementBase.getProductDatetime()));
        unifiedTaskUpdateReqVO.setEndTime(simpleDateFormat.format(new Date()));
        unifiedTaskUpdateReqVO.setProcessId(bhzSwBases.getGuid());
        unifiedTaskUpdateReqVO.setSourceId(20230523);
        unifiedTaskUpdateReqVO.setSourceName("测试");
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(bhzSwBases.getShebeibianhao());
        Map map = bhzCementBaseService.selectSYSdepartId(selectshebeione.getSysOrgCode());
        if (map != null) {
            unifiedTaskUpdateReqVO.setSectionId(map.get("sectionid").toString());
            unifiedTaskUpdateReqVO.setProjectId(map.get("projectid").toString());
        }
        unifiedTaskUpdateReqVO.setTaskId(bhzSwBases.getGuid() +"-"+ status);
        unifiedTaskUpdateReqVO.setTaskStatus(1);
        if (unifiedStatus == 20) {
            //进程
            unifiedProcessUpdateReqVO ProcessUpdateReqVO = new unifiedProcessUpdateReqVO();
            ProcessUpdateReqVO.setProcessId(bhzSwBases.getGuid());
            ProcessUpdateReqVO.setEndTime(simpleDateFormat.format(new Date()));
            ProcessUpdateReqVO.setProcessStatus(1);
            ProcessUpdateReqVO.setSourceId(20230523);
            ProcessUpdateReqVO.setSourceName("测试");
            //更新进程
            unifiedProcessUpdate(ProcessUpdateReqVO);
        }
        //更新任务
        unifiedTaskUpdate(unifiedTaskUpdateReqVO);

    }

    private void unifiedUpdatelq(BhzLqBases bhzLqBases, int unifiedStatus, int status) throws JsonProcessingException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //任务
        unifiedTaskUpdateReqVO unifiedTaskUpdateReqVO = new unifiedTaskUpdateReqVO();
//        unifiedTaskUpdateReqVO.setStartTime(simpleDateFormat.format(bhzCementBase.getProductDatetime()));
        unifiedTaskUpdateReqVO.setEndTime(simpleDateFormat.format(new Date()));
        unifiedTaskUpdateReqVO.setProcessId(bhzLqBases.getGuid());
        unifiedTaskUpdateReqVO.setSourceId(20230523);
        unifiedTaskUpdateReqVO.setSourceName("测试");
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(bhzLqBases.getShebeibianhao());
        Map map = bhzCementBaseService.selectSYSdepartId(selectshebeione.getSysOrgCode());
        if (map != null) {
            unifiedTaskUpdateReqVO.setSectionId(map.get("sectionid").toString());
            unifiedTaskUpdateReqVO.setProjectId(map.get("projectid").toString());
        }
        unifiedTaskUpdateReqVO.setTaskId(bhzLqBases.getGuid() +"-"+ status);
        unifiedTaskUpdateReqVO.setTaskStatus(1);
        if (unifiedStatus == 20) {
            //进程
            unifiedProcessUpdateReqVO ProcessUpdateReqVO = new unifiedProcessUpdateReqVO();
            ProcessUpdateReqVO.setProcessId(bhzLqBases.getGuid());
            ProcessUpdateReqVO.setEndTime(simpleDateFormat.format(new Date()));
            ProcessUpdateReqVO.setProcessStatus(1);
            ProcessUpdateReqVO.setSourceId(20230523);
            ProcessUpdateReqVO.setSourceName("测试");
            //更新进程
            unifiedProcessUpdate(ProcessUpdateReqVO);
        }
        //更新任务
        unifiedTaskUpdate(unifiedTaskUpdateReqVO);

    }

    public void unifiedUpdate(BhzCementBase bhzCementBase, int unifiedStatus, int status) throws JsonProcessingException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //任务
        unifiedTaskUpdateReqVO unifiedTaskUpdateReqVO = new unifiedTaskUpdateReqVO();
//        unifiedTaskUpdateReqVO.setStartTime(simpleDateFormat.format(bhzCementBase.getProductDatetime()));
        unifiedTaskUpdateReqVO.setEndTime(simpleDateFormat.format(new Date()));
        unifiedTaskUpdateReqVO.setProcessId(bhzCementBase.getBatchNo());
        unifiedTaskUpdateReqVO.setSourceId(20230523);
        unifiedTaskUpdateReqVO.setSourceName("测试");
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(bhzCementBase.getShebeiNo());
        Map map = bhzCementBaseService.selectSYSdepartId(selectshebeione.getSysOrgCode());
        if (map != null) {
            unifiedTaskUpdateReqVO.setSectionId(map.get("sectionid").toString());
            unifiedTaskUpdateReqVO.setProjectId(map.get("projectid").toString());
        }
        unifiedTaskUpdateReqVO.setTaskId(bhzCementBase.getBatchNo() +"-"+ status);
        unifiedTaskUpdateReqVO.setTaskStatus(1);
        if (unifiedStatus == 20) {
            //进程
            unifiedProcessUpdateReqVO ProcessUpdateReqVO = new unifiedProcessUpdateReqVO();
            ProcessUpdateReqVO.setProcessId(bhzCementBase.getBatchNo());
            ProcessUpdateReqVO.setEndTime(simpleDateFormat.format(new Date()));
            ProcessUpdateReqVO.setProcessStatus(1);
            ProcessUpdateReqVO.setSourceId(20230523);
            ProcessUpdateReqVO.setSourceName("测试");
            //更新进程
            unifiedProcessUpdate(ProcessUpdateReqVO);
        }
        //更新任务
        unifiedTaskUpdate(unifiedTaskUpdateReqVO);

    }

    //更新进程
    public void unifiedProcessUpdate(unifiedProcessUpdateReqVO unifiedProcessUpdateReqVO) throws JsonProcessingException {
        unifiedProcessUpdateReqVO.setProcessStatus(1);
        // 使用 Jackson 库将实体类转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(unifiedProcessUpdateReqVO);
        String token = null;
        String clientSecret = RSAUtil.queryById22("KJXOVlay6Bw0SDgp");
        String back1 = HttpRequest.post("http://47.96.161.157:1080/api/login/appToken")
                .header("clientid", "app-ext-iot")
                .header("clientSecret", clientSecret)
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back1);
        String code1 = (String) jsonObject1.get("code");
        if (code1.equals("00000")) {
            JSONArray jsonArray = new JSONArray();
            JSONObject model = (JSONObject) jsonObject1.get("model");
            token = (String) model.get("access_token");
            String post = HttpRequest.put("http://47.96.161.157:1080/api/unified/processes")
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(json)
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(post);
            String code2 = (String) jsonObject2.get("code");
            if (code2.equals("00000")) {
                log.info(String.format("拌合站统一待办进程更新成功!" + unifiedProcessUpdateReqVO));
            } else {
                log.info(String.format("拌合站统一待办进程更新失败!" + unifiedProcessUpdateReqVO));
            }
        }
    }

    //更新任务
    public void unifiedTaskUpdate(unifiedTaskUpdateReqVO unifiedTaskUpdateReqVO) throws JsonProcessingException {
        // 使用 Jackson 库将实体类转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(unifiedTaskUpdateReqVO);
        System.out.println(json);
        String token = null;
        String clientSecret = RSAUtil.queryById22("KJXOVlay6Bw0SDgp");
        String back1 = HttpRequest.post("http://47.96.161.157:1080/api/login/appToken")
                .header("clientid", "app-ext-iot")
                .header("clientSecret", clientSecret)
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back1);
        String code1 = (String) jsonObject1.get("code");
        if (code1.equals("00000")) {
            JSONArray jsonArray = new JSONArray();
            JSONObject model = (JSONObject) jsonObject1.get("model");
            token = (String) model.get("access_token");
            String post = HttpRequest.put("http://47.96.161.157:1080/api/unified/tasks")
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(json)
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(post);
            String code2 = (String) jsonObject2.get("code");
            if (code2.equals("00000")) {
                log.info(String.format("拌合站统一待办进程更新成功!" + unifiedTaskUpdateReqVO));
            } else {
                log.info(String.format("拌合站统一待办进程更新失败!" + unifiedTaskUpdateReqVO));
            }
        }
    }
}
