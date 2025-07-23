package org.jeecg.modules.job.RCJob.RCUnifiedProcessJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.RCJob.RSAUtil;
import org.jeecg.modules.job.RCJob.unifiedProcessReqVO.unifiedProcessAddReqVO;
import org.jeecg.modules.job.RCJob.unifiedProcessReqVO.unifiedTaskAddReqVO;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@RestController
@RequestMapping("/RCUnifiedProcessLQJob")
public class RCUnifiedProcessLQJob implements Job {
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IBhzLqBasesService BhzLqBasesService;
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LQ_UNIDIED_PROCESS);//沥青拌合站统一待办
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到沥青拌合站统一待办定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输沥青拌合站统一待办推送的设备" + DateUtils.now()));
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
            log.info(String.format("暂无沥青拌合站统一待办推送的设备：没有配置代办人的信息" + DateUtils.now()));
            return;
        }
        Integer alertstate = 1;
        Integer overLevel = 0;
        List<BhzLqBases> bhzLqBasesList = BhzLqBasesService.selectBHZUnifiedProcess(curid, alertstate, shebeiList, overLevel);
        if (null == bhzLqBasesList || bhzLqBasesList.size() == 0) {
            log.info(String.format("暂无沥青拌合站统一待办的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzLqBases BhzLqBases : bhzLqBasesList) {
            id = BhzLqBases.getId();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            unifiedProcessAddReqVO unifiedProcessAddReqVO = new unifiedProcessAddReqVO();
//            unifiedProcessAddReqVO.setStartUserId(0000);
            unifiedProcessAddReqVO.setStartUserName("无");
            unifiedProcessAddReqVO.setStartTime(simpleDateFormat.format(new Date()));
            unifiedProcessAddReqVO.setProcessId(BhzLqBases.getGuid());
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(BhzLqBases.getShebeibianhao());
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
            BhzCallCfg bhzCallCfg =  bhzCallCfgService.selectbhzcallone(BhzLqBases.getShebeibianhao());

            unifiedProcessAddReqVO.setPcUrl("http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/loginSs/bhzxx?username=iot&content="+BhzLqBases.getGuid());

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
                    chuzhi.setTaskId(BhzLqBases.getGuid() + "-10");
                    chuzhi.setProjectId(queryone.getProjectid());
                    chuzhi.setSectionId(queryone.getSectionid());
                    chuzhi.setProcessId(BhzLqBases.getGuid());
                    chuzhi.setProcessName(selectshebeione.getSbname() + "超标预警");
                    chuzhi.setStartTime(simpleDateFormat.format(new Date()));
                    chuzhi.setPcRedirectUrl("http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/loginSs/bhzcz?username="+userByName.getUsername()+"&content="+BhzLqBases.getGuid());
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
                    jianliSP.setTaskId(BhzLqBases.getGuid() + "-40");
                    jianliSP.setProjectId(queryone.getProjectid());
                    jianliSP.setSectionId(queryone.getSectionid());
                    jianliSP.setProcessId(BhzLqBases.getGuid());
                    jianliSP.setProcessName(selectshebeione.getSbname() + "超标预警");
                    jianliSP.setStartTime(simpleDateFormat.format(new Date()));
                    jianliSP.setPcRedirectUrl("http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/loginSs/bhzsh?username="+userByName.getUsername()+"&content="+BhzLqBases.getGuid());
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
                    zhbSP.setTaskId(BhzLqBases.getGuid() + "-50");
                    zhbSP.setProjectId(queryone.getProjectid());
                    zhbSP.setSectionId(queryone.getSectionid());
                    zhbSP.setProcessId(BhzLqBases.getGuid());
                    zhbSP.setProcessName(selectshebeione.getSbname() + "超标预警");
                    zhbSP.setStartTime(simpleDateFormat.format(new Date()));
                    zhbSP.setPcRedirectUrl("http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/loginSs/bhzsp?username="+userByName.getUsername()+"&content="+BhzLqBases.getGuid());
                    zhbSP.setTaskStatus(0);
                    zhbSP.setSourceId(20230523);
                    zhbSP.setSourceName("测试");
                    zhbSP.setEndTime(null);
                    zhbSP.setTaskName("指挥部审批");
                    zhbSP.setTaskStatus(0);
                    switch (BhzLqBases.getChaobiaodengji()) {
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
            sysConfigService.updateSysConfig(JobUtil.LQ_UNIDIED_PROCESS, id);
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
            log.info(String.format("沥青拌合站统一任务待办添加成功!" + unifiedTaskAddReqVO));
        } else {
            log.info(String.format("沥青拌合站统一任务待办添加失败!" + unifiedTaskAddReqVO));
        }
    }


}
