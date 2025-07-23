package org.jeecg.modules.job.devicedatajob;

import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * \* @author: zml
 * \* Date: 2022/09/09
 * \* Time: 13:40
 * \* Description:  存梁台座关灯定时任务
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class cunliangtaizuoswitchJob implements Job {
    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.CUN_TAIZUO_SWITCH);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到存梁台座关灯定时任务配置信息" + DateUtils.now());
            return;
        }
        List<CunliangProcedureConfig> procedureConfigList = cunliangProcedureConfigService.selectprocedureList();
        if (0 == procedureConfigList.size()) {
            log.info("暂无存梁台座开灯数据" + DateUtils.now());
        }
        try {
            for (CunliangProcedureConfig cunliangProcedureConfig : procedureConfigList) {
                Date switchtime = cunliangProcedureConfig.getSwitchtime();
                long l = (new Date().getTime() - switchtime.getTime()) / 1000;
                if (l > 10) {
                    String token = jobUtil.getDeviceToken();
                    if (null != token) {
                        boolean boo = false;
                        int codes = jobUtil.getcode(token, cunliangProcedureConfig.getUuid(), 1);
                        List<CunliangProcedureConfig> configList = cunliangProcedureConfigService.selectprocedureLists(cunliangProcedureConfig.getUuid());
                        if (configList.size() > 0) {
                            jobUtil.getupdate(configList, 0);
                            boo = true;
                        }
                        if (codes == 0 && boo) {
                            log.info("暂无存梁台座关灯成功" + DateUtils.now());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
