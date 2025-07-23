package org.jeecg.modules.job.lab.push.job;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.entity.MixingPlantVo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 拌合站初始化定时任务 （品茗）
 *
 * @author lis1
 * @date 2022/12/14
 * @time 9:28
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InitMixingPlant implements Job {

    private final static String I_TYPE = "initMixingPlant";

    // 品茗测试库企业id
    private final static int COMPANY_ID = 91093;

    // 品茗测试库项目id
    private final static int PROJECT_ID = 26740;

    // 设备类型 拌合站设备
    private final static int[] DEVICE_TYPE = {0, 1, 2, 3, 4, 12};

    // 滨淮组织机构
    private final static String BH_CODE = "A05A07A01A02A01%";

    private final static String YD_CODE = "A05A01A06A17";


    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IShebeiInfoService service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.PM_INIT_MIXINGPLANT); //拌合站初始化 品茗
        if (config == null) {
            log.info(String.format("未获取到 拌合站初始化(品茗) 的配置信息" + DateUtils.now()));
            return;
        }
        List<Map> list = service.getDataToInitPM(YD_CODE, DEVICE_TYPE);
        if (list == null || list.size() == 0) {
            log.info(String.format("暂无未初始化的拌合站，时间：" + DateUtils.now()));
            return;
        }
        List<MixingPlantVo> data = new ArrayList<>();
        for (Map item : list) {
            MixingPlantVo vo = resultToVo(item);
            data.add(vo);
        }
        BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
        String result = baseHttpRequest.requestDataProcessing(data, I_TYPE, 0);
        Boolean success = baseHttpRequest.resultProcessing(result);
        if (success) {
            log.info(String.format("拌合站初始化成功，时间：" + DateUtils.now()));
        } else {
            log.error(String.format("拌合站初始化失败，时间：" + DateUtils.now()));
        }
    }

    private MixingPlantVo resultToVo(Map map) {
        MixingPlantVo mixingPlantVo = new MixingPlantVo();
        mixingPlantVo.setMixId(map.get("orgCode").toString());
        mixingPlantVo.setMixName(map.get("departName").toString());
        mixingPlantVo.setCompanyId(COMPANY_ID);
        mixingPlantVo.setProjectId(PROJECT_ID);
        mixingPlantVo.setCreateId(map.get("createId").toString());
        return mixingPlantVo;
    }
}
