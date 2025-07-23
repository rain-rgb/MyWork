package org.jeecg.modules.job.lab.push.job;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.TaskVo;
import com.trtm.iot.system.service.IBhzrenwudanService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 分部分项初始化定时任务 （品茗）
 *
 * @author lis1
 * @date 2022/12/13
 * @time 16:08
 */
@Slf4j
public class InitConcreteTask implements Job {

    private final static String I_TYPE = "initConcreteTask";

    private final static String BH_CODE = "A05A07A01A02A01%";

    private final static String YD_CODE = "A05A03A08A01%";

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IBhzrenwudanService service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.PM_INIT_CONCRETETASK); //分部分项推送 品茗
        if (config == null) {
            log.info(String.format("未获取到 分部分项初始化(品茗) 的配置信息" + DateUtils.now()));
            return;
        }

        List<TaskVo> list = service.getTaskToPM(YD_CODE);
        if (list == null || list.size() == 0) {
            log.info(String.format("暂无未初始化的分部分项" + DateUtils.now()));
            return;
        }
        BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
        String result = baseHttpRequest.requestDataProcessing(list, I_TYPE, 0);
        Boolean success = baseHttpRequest.resultProcessing(result);
        if (success) {
            log.info("分部分项初始化成功！");
        } else {
            log.error("分部分项初始化失败！");
        }
    }
}
