package org.jeecg.modules.job.lab.push.job;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.entity.CrewVo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化拌合机（品茗）
 *
 * @author lis1
 * @date 2022/12/15
 * @time 11:01
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InitCrew implements Job {
    // itype
    private final static String I_TYPE = "initCrew";

    // 品茗测试库企业id
    private final static int COMPANY_ID = 104287;

    // 品茗测试库项目id
    private final static int PROJECT_ID = 42252;

    // 滨淮组织机构
    private final static String BH_CODE = "A05A07A01A02A01%";

    private final static String YD_CODE = "A05A03A08A01";
    @Autowired
    private IShebeiInfoService service;

    @Autowired
    private ISysConfigService sysConfigService;

    // 设备类型 拌合站设备
    private final static int[] DEVICE_TYPE = {0, 1, 2, 3, 4, 12};

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.PM_INIT_CREW); //机组初始化(设备初始化) 品茗
        if (config == null) {
            log.info(String.format("未获取到 机组初始化(品茗) 的配置信息" + DateUtils.now()));
            return;
        }
        List<ShebeiInfo> list = service.getDataToInitCrew(YD_CODE, DEVICE_TYPE);
        if (list == null || list.size() < 0) {
            log.info(String.format("暂无未初始化的机组，时间：" + DateUtils.now()));
            return;
        }
        List<CrewVo> data = new ArrayList<>();
        for (ShebeiInfo item : list) {
            CrewVo crewVo = resultToVo(item);
            data.add(crewVo);
        }
        BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
        String result = baseHttpRequest.requestDataProcessing(data, I_TYPE, 0);
        Boolean success = baseHttpRequest.resultProcessing(result);
        if (success) {
            log.info(String.format("机组初始化成功，时间：" + DateUtils.now()));
        } else {
            log.error(String.format("机组初始化失败，时间：" + DateUtils.now()));
        }
    }

    private CrewVo resultToVo(ShebeiInfo device) {
        CrewVo crewvo = new CrewVo();
        crewvo.setCrewId(device.getSbjno());
        crewvo.setCrewName(device.getSbname());
        crewvo.setMixingId(device.getSysOrgCode());
        crewvo.setCompanyId(COMPANY_ID);
        crewvo.setProjectId(PROJECT_ID);
        crewvo.setCreateId(device.getCreateBy());
        return crewvo;
    }
}
