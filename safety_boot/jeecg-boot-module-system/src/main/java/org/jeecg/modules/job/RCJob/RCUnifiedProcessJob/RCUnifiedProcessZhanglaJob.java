package org.jeecg.modules.job.RCJob.RCUnifiedProcessJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tryajiangconfigure.entity.TrYajiangConfigure;
import com.trtm.iot.tryajiangconfigure.service.ITrYajiangConfigureService;
import com.trtm.iot.trzhanglaconfigure.entity.TrZhanglaConfigure;
import com.trtm.iot.trzhanglaconfigure.service.ITrZhanglaConfigureService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.RCJob.RSAUtil;
import org.jeecg.modules.job.RCJob.unifiedProcessReqVO.unifiedProcessAddReqVO;
import org.jeecg.modules.job.RCJob.unifiedProcessReqVO.unifiedProcessUpdateReqVO;
import org.jeecg.modules.job.RCJob.unifiedProcessReqVO.unifiedTaskAddReqVO;
import org.jeecg.modules.job.RCJob.unifiedProcessReqVO.unifiedTaskUpdateReqVO;
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
@RequestMapping("/RCUnifiedProcessZhanglaJob")
public class RCUnifiedProcessZhanglaJob implements Job {
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IZhanglaYajiangOverHandlerService zhanglaYajiangOverHandlerService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private ITrZhanglaXxbService trZhanglaXxbService;
    @Autowired
    private ITrYajiangMService trYajiangMService;
    @Autowired
    private ITrZhanglaMService trZhanglaMService;
    @Autowired
    private ITrYajiangSService trYajiangSService;
    @Autowired
    private ITrZhanglaConfigureService zhanglaConfigureService;
    @Autowired
    private ITrYajiangConfigureService yajiangConfigureService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {


        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZL_UNIDIED_PROCESS);//张拉统一待办
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到张拉统一待办定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输张拉统一待办推送的设备" + DateUtils.now()));
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
            log.info(String.format("暂无张拉统一待办推送的设备：没有配置代办人的信息" + DateUtils.now()));
            return;
        }
        Integer alertstate = 1;
        Integer overLevel = 0;
        List<TrZhanglaXxb> zhanglaXxbList = trZhanglaXxbService.selectUnifiedProcess(curid, alertstate, shebeiList, overLevel);
        if (null == zhanglaXxbList || zhanglaXxbList.size() == 0) {
            log.info(String.format("暂无张拉统一待办的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrZhanglaXxb zhanglaXxb : zhanglaXxbList) {
            id = zhanglaXxb.getId();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            unifiedProcessAddReqVO unifiedProcessAddReqVO = new unifiedProcessAddReqVO();
//            unifiedProcessAddReqVO.setStartUserId(0000);
            unifiedProcessAddReqVO.setStartUserName("无");
            unifiedProcessAddReqVO.setStartTime(simpleDateFormat.format(new Date()));
            unifiedProcessAddReqVO.setProcessId(zhanglaXxb.getSyjid());
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(zhanglaXxb.getShebeibianhao());
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
            BhzCallCfg bhzCallCfg =  bhzCallCfgService.selectbhzcallone(zhanglaXxb.getShebeibianhao());

            unifiedProcessAddReqVO.setPcUrl("暂无");

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
                    chuzhi.setTaskId(zhanglaXxb.getSyjid() + "-10");
                    chuzhi.setProjectId(queryone.getProjectid());
                    chuzhi.setSectionId(queryone.getSectionid());
                    chuzhi.setProcessId(zhanglaXxb.getSyjid());
                    chuzhi.setProcessName(selectshebeione.getSbname() + "超标预警");
                    chuzhi.setStartTime(simpleDateFormat.format(new Date()));
                    chuzhi.setPcRedirectUrl("暂无");
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
                    jianliSP.setTaskId(zhanglaXxb.getSyjid() + "-40");
                    jianliSP.setProjectId(queryone.getProjectid());
                    jianliSP.setSectionId(queryone.getSectionid());
                    jianliSP.setProcessId(zhanglaXxb.getSyjid());
                    jianliSP.setProcessName(selectshebeione.getSbname() + "超标预警");
                    jianliSP.setStartTime(simpleDateFormat.format(new Date()));
                    jianliSP.setPcRedirectUrl("暂无");
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
                    zhbSP.setTaskId(zhanglaXxb.getSyjid() + "-50");
                    zhbSP.setProjectId(queryone.getProjectid());
                    zhbSP.setSectionId(queryone.getSectionid());
                    zhbSP.setProcessId(zhanglaXxb.getSyjid());
                    zhbSP.setProcessName(selectshebeione.getSbname() + "超标预警");
                    zhbSP.setStartTime(simpleDateFormat.format(new Date()));
                    zhbSP.setPcRedirectUrl("暂无");
                    zhbSP.setTaskStatus(0);
                    zhbSP.setSourceId(20230523);
                    zhbSP.setSourceName("测试");
                    zhbSP.setEndTime(null);
                    zhbSP.setTaskName("指挥部审批");
                    zhbSP.setTaskStatus(0);
                    switch (zhanglaXxb.getOverLevel()) {
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
            sysConfigService.updateSysConfig(JobUtil.ZL_UNIDIED_PROCESS, id);
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
            log.info(String.format("张拉统一任务待办添加成功!" + unifiedTaskAddReqVO));
        } else {
            log.info(String.format("张拉统一任务待办添加失败!" + unifiedTaskAddReqVO));
        }
    }
    /**
     * 根据id添加或者修改处置信息
     */
    @AutoLog(value = "拌合站处置信息添加或者修改")
    @ApiOperation(value = "拌合站处置信息添加或者修改", notes = "拌合站处置信息添加或者修改")
    @GetMapping(value = "/HBZCZAddOrUpdate2")
    public Result<?> HBZCZAddOrUpdate2(ZhanglaYajiangOverHandler zhanglaYajiangOverHandler,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String people = String.valueOf(loginUser.getRealname());
        String zhbyj = request.getParameter("zhbyj");   //指挥部审批
        String zhbbh = request.getParameter("zhbbh");   //指挥部驳回
        String zhbjg = request.getParameter("zhbjg");   //指挥部结果
        String spyj = request.getParameter("spyj");   //监理审批
        String jlbh = request.getParameter("jlbh");   //监理驳回
        String spjg = request.getParameter("spjg");   //审批结果
        String wtyy = request.getParameter("wtyy");  //问题原因
        String clfs = request.getParameter("clfs");  //处理方式
        String cljg = request.getParameter("cljg");  //处理结果
        String bizPath = request.getParameter("fileList");  //图片
        String baseid = request.getParameter("baseid");   //id
        String holeid = request.getParameter("holeid");  //孔道id
        int type = Integer.parseInt(request.getParameter("type"));//0：张拉；1：压浆
        int level = Integer.parseInt(request.getParameter("level"));//等级
        int status = Integer.parseInt(request.getParameter("status"));//状态值
        Integer i = null;
        Integer unifiedStatus = status;
        if (type == 0) {//张拉
            LambdaQueryWrapper<TrZhanglaXxb> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(TrZhanglaXxb::getSyjid, baseid);
            List<TrZhanglaXxb> trZhanglaXxbs = trZhanglaXxbService.list(lambdaQueryWrapper);
            for (TrZhanglaXxb trZhanglaXxb : trZhanglaXxbs) {
                TrZhanglaXxb trZhanglaXxb1 = new TrZhanglaXxb();
                trZhanglaXxb1.setId(trZhanglaXxb.getId());
                if (level == 1) {
                    if (status == 10) {
                        trZhanglaXxb1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = zhanglaYajiangOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, baseid, bizPath, people, 20,holeid);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 2) {
                    if (status == 10) {//处置
                        trZhanglaXxb1.setOverproofStatus(10);
                        i = zhanglaYajiangOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, baseid, bizPath, people, 10, holeid);
                    } else if (status == 30) {//监理驳回
                        trZhanglaXxb1.setOverproofStatus(30);
                        i = zhanglaYajiangOverHandlerService.supervisorBohui(jlbh, baseid, people, 30,holeid);
                    } else if (status == 40) {//监理审核
                        trZhanglaXxb1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = zhanglaYajiangOverHandlerService.supervisorAddOrUpdate(spyj, spjg, baseid, bizPath, people, 20,holeid);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 3) {
                    if (status == 10) {//处置
                        trZhanglaXxb1.setOverproofStatus(10);
                        i = zhanglaYajiangOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, baseid, bizPath, people, 10, holeid);
                    } else if (status == 30) {//监理驳回
                        trZhanglaXxb1.setOverproofStatus(30);
                        i = zhanglaYajiangOverHandlerService.supervisorBohui(jlbh, baseid, people, 30, holeid);
                    } else if (status == 40) {//监理审核
                        trZhanglaXxb1.setOverproofStatus(40);
                        i = zhanglaYajiangOverHandlerService.supervisorAddOrUpdate(spyj, spjg, baseid, bizPath, people, 40, holeid);
                    } else if (status == 50) {//指挥部审核
                        trZhanglaXxb1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = zhanglaYajiangOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, baseid, bizPath, people, 20,holeid);
                    } else if (status == 60) {//指挥部驳回
                        trZhanglaXxb1.setOverproofStatus(60);
                        i = zhanglaYajiangOverHandlerService.headquartersBohui(zhbbh, baseid, people, 60,holeid);
                    } else {
                        return Result.error("错误");
                    }
                }
                trZhanglaXxbService.updateById(trZhanglaXxb1);
                Integer status1 = trZhanglaXxb1.getOverproofStatus();
                LambdaQueryWrapper<TrZhanglaM> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TrZhanglaM::getSyjid,baseid);
                List<TrZhanglaM> list = trZhanglaMService.list(queryWrapper);
                if(list.size()>0){
                    for (TrZhanglaM trZhanglaM : list) {
                        trZhanglaM.setOverproofStatus(status1);
                    }
                    trZhanglaMService.updateBatchById(list);
                }
                if (i > 0) {
                    try {
                        QueryWrapper<TrZhanglaConfigure> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.eq("bhjno", trZhanglaXxb.getShebeibianhao());
                        TrZhanglaConfigure one = zhanglaConfigureService.getOne(queryWrapper1);
                        if (one.getCzperson() != null && !one.getCzperson().contains("未设处置人")
                                && one.getShperson() != null && !one.getShperson().contains("未设审核人")
                                && one.getSpperson() != null && !one.getSpperson().contains("未设审批人")) {
                            unifiedUpdateZl(trZhanglaXxb, unifiedStatus, status);
                        }else{
                            log.info(String.format("该设备"+trZhanglaXxb.getShebeibianhao()+"未设置相关负责人"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        return Result.OK(i);
                    }
                }
            }
        } else if (type == 1) {//压浆
            if (level == 1) {
                LambdaQueryWrapper<TrYajiangM> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(TrYajiangM::getSyjid, baseid);
                List<TrYajiangM> trYajiangMS = trYajiangMService.list(lambdaQueryWrapper);
                TrYajiangM trYajiangM1 = new TrYajiangM();
                for (TrYajiangM trYajiangM : trYajiangMS) {
                    trYajiangM1.setId(trYajiangM.getId());
                    if (status == 10) {//处置
                        trYajiangM1.setOverproofStatus(10);
                        i = zhanglaYajiangOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, baseid, bizPath, people, 10, holeid);
                    } else if (status == 30) {//监理驳回
                        trYajiangM1.setOverproofStatus(30);
                        i = zhanglaYajiangOverHandlerService.supervisorBohui(jlbh, baseid, people, 30, holeid);
                    } else if (status == 40) {//监理审核
                        trYajiangM1.setOverproofStatus(40);
                        i = zhanglaYajiangOverHandlerService.supervisorAddOrUpdate(spyj, spjg, baseid, bizPath, people, 40, holeid);
                    } else if (status == 50) {//指挥部审核
                        trYajiangM1.setOverproofStatus(20);
                        unifiedStatus = 20;
                        i = zhanglaYajiangOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, baseid, bizPath, people, 20, holeid);
                    } else if (status == 60) {//指挥部驳回
                        trYajiangM1.setOverproofStatus(60);
                        i = zhanglaYajiangOverHandlerService.headquartersBohui(zhbbh, baseid, people, 60, holeid);
                    } else {
                        return Result.error("错误");
                    }
                    trYajiangMService.updateById(trYajiangM1);
                    if (i > 0) {
                        try {
                            QueryWrapper<TrYajiangConfigure> queryWrapper1 = new QueryWrapper<>();
                            queryWrapper1.eq("bhjno", trYajiangM.getYjsbbh());
                            TrYajiangConfigure one = yajiangConfigureService.getOne(queryWrapper1);
                            if (one.getCzperson() != null && !one.getCzperson().contains("未设处置人")
                                    && one.getShperson() != null && !one.getShperson().contains("未设审核人")
                                    && one.getSpperson() != null && !one.getSpperson().contains("未设审批人")) {
                                unifiedUpdateYj(trYajiangM, unifiedStatus, status);
                            }else{
                                log.info(String.format("该设备"+trYajiangM.getYjsbbh()+"未设置相关负责人"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            return Result.OK(i);
                        }
                    }
                }
                Integer overproofStatus = trYajiangM1.getOverproofStatus();
                LambdaQueryWrapper<TrYajiangS> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TrYajiangS::getSyjid,baseid);
                List<TrYajiangS> list = trYajiangSService.list(queryWrapper);
                if(list.size()>0){
                    for (TrYajiangS trYajiangS : list) {
                        trYajiangS.setOverproofStatus(overproofStatus);
                    }
                    trYajiangSService.updateBatchById(list);
                }

            }
        }

        return Result.OK(i);
    }

    private void unifiedUpdateYj(TrYajiangM trYajiangM, Integer unifiedStatus, int status) throws JsonProcessingException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //任务
        unifiedTaskUpdateReqVO unifiedTaskUpdateReqVO = new unifiedTaskUpdateReqVO();
//        unifiedTaskUpdateReqVO.setStartTime(simpleDateFormat.format(bhzCementBase.getProductDatetime()));
        unifiedTaskUpdateReqVO.setEndTime(simpleDateFormat.format(new Date()));
        unifiedTaskUpdateReqVO.setProcessId(trYajiangM.getSyjid());
        unifiedTaskUpdateReqVO.setSourceId(20230523);
        unifiedTaskUpdateReqVO.setSourceName("测试");
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(trYajiangM.getYjsbbh());
        Map map = bhzCementBaseService.selectSYSdepartId(selectshebeione.getSysOrgCode());
        if (map != null) {
            unifiedTaskUpdateReqVO.setSectionId(map.get("sectionid").toString());
            unifiedTaskUpdateReqVO.setProjectId(map.get("projectid").toString());
        }
        unifiedTaskUpdateReqVO.setTaskId(trYajiangM.getSyjid() +"-"+ status);
        unifiedTaskUpdateReqVO.setTaskStatus(1);
        if (unifiedStatus == 20) {
            //进程
            unifiedProcessUpdateReqVO ProcessUpdateReqVO = new unifiedProcessUpdateReqVO();
            ProcessUpdateReqVO.setProcessId(trYajiangM.getSyjid());
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

    private void unifiedUpdateZl(TrZhanglaXxb trZhanglaXxb, Integer unifiedStatus, int status) throws JsonProcessingException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //任务
        unifiedTaskUpdateReqVO unifiedTaskUpdateReqVO = new unifiedTaskUpdateReqVO();
//        unifiedTaskUpdateReqVO.setStartTime(simpleDateFormat.format(bhzCementBase.getProductDatetime()));
        unifiedTaskUpdateReqVO.setEndTime(simpleDateFormat.format(new Date()));
        unifiedTaskUpdateReqVO.setProcessId(trZhanglaXxb.getSyjid());
        unifiedTaskUpdateReqVO.setSourceId(20230523);
        unifiedTaskUpdateReqVO.setSourceName("测试");
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(trZhanglaXxb.getShebeibianhao());
        Map map = bhzCementBaseService.selectSYSdepartId(selectshebeione.getSysOrgCode());
        if (map != null) {
            unifiedTaskUpdateReqVO.setSectionId(map.get("sectionid").toString());
            unifiedTaskUpdateReqVO.setProjectId(map.get("projectid").toString());
        }
        unifiedTaskUpdateReqVO.setTaskId(trZhanglaXxb.getSyjid() +"-"+ status);
        unifiedTaskUpdateReqVO.setTaskStatus(1);
        if (unifiedStatus == 20) {
            //进程
            unifiedProcessUpdateReqVO ProcessUpdateReqVO = new unifiedProcessUpdateReqVO();
            ProcessUpdateReqVO.setProcessId(trZhanglaXxb.getSyjid());
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
