package org.jeecg.modules.job.lab.push.job;

import com.trtm.iot.outsource.entity.FTrial;
import com.trtm.iot.outsource.service.IFTrialService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 实验室-委外试验推送（新增或更新 品茗）
 *
 * @author lis1
 * @date 2022/12/9
 * @time 11:06
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OutsourcePushJob implements Job {

    // itype
    private final static String I_TYPE = "initTrial";

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IFTrialService service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.SYS_WYSY); //委外试验推送 品茗
        if (config == null) {
            log.info(String.format("未获取到 委外试验推送 品茗 的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer lastId = config.getCurid();
        List<FTrial> list = service.getDataToPush(lastId);
        int size = list.size();
        if (list == null || size == 0) {
            log.info(String.format("暂无未推送的委外试验数据" + DateUtils.now()));
            return;
        }
        final Integer currentId = list.get(size - 1).getId();

        for (FTrial item : list) {
            item.setId(null);
            item.setTrialType("委外试验");
        }
        BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
        String result = baseHttpRequest.requestDataProcessing(list, I_TYPE, 0);
        Boolean success = baseHttpRequest.resultProcessing(result);
        if (success) {
            log.info(String.format("委外试验数据初推送成功，时间：" + DateUtils.now()));
            sysConfigService.updateSysConfig(JobUtil.SYS_WYSY, currentId);//根据传过来的唯一值来修改当前判断到的数据id
        } else {
            log.error("委外试验数据推送失败" + DateUtils.now());
        }
    }
}
