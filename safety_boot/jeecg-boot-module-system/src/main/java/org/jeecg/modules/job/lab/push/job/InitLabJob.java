package org.jeecg.modules.job.lab.push.job;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.entity.LabVo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 试验室初始化定时任务 （品茗）
 *
 * @author lis1
 * @date 2022/12/13
 * @time 9:53
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InitLabJob implements Job {
    // itype
    private final static String I_TYPE = "initLab";

    // 品茗企业id
    private final static int COMPANY_ID = 104287;

    // 品茗项目id
    private final static int PROJECT_ID = 42252;

    // 设备类型 实验室设备
    private final static int[] DEVICE_TYPE = {3, 4, 12};

    // 滨淮组织机构
    private final static String BH_CODE = "A05A01A06%";

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IShebeiInfoService service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.PM_INIT_LAB); //委外试验推送 品茗
        if (config == null) {
            log.info(String.format("未获取到 实验室初始化(品茗) 的配置信息" + DateUtils.now()));
            return;
        }
        List<Map> list = service.getDataToInitPM(BH_CODE, DEVICE_TYPE);
        if (list == null || list.size() == 0) {
            log.info(String.format("暂无未初始化的实验室，时间：" + DateUtils.now()));
            return;
        }
        List<LabVo> data = new ArrayList<>();
        for (Map item : list) {
            LabVo labVo = resultToVo(item);
            data.add(labVo);
        }
        System.out.println("试验室数据：" + data);
        BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
        String result = baseHttpRequest.requestDataProcessing(data, I_TYPE, 0);
        Boolean success = baseHttpRequest.resultProcessing(result);
        if (success) {
            log.info(String.format("实验室初始化成功，时间：" + DateUtils.now()));
        } else {
            log.error(String.format("实验室初始化失败，时间：" + DateUtils.now()));
        }
    }

    private LabVo resultToVo(Map map) {
        LabVo labVo = new LabVo();
        labVo.setLabId(map.get("orgCode").toString());
        labVo.setLabName(map.get("departName").toString());
        labVo.setCompanyId(COMPANY_ID);
        labVo.setProjectId(PROJECT_ID);
        labVo.setGmtCreate(map.get("createTime").toString());
        labVo.setCreateId(map.get("createId").toString());
        return labVo;
    }
}
