package org.jeecg.modules.quartz.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName RunDayJob
 * @Author
 * @Date 2024/11/28 10:49
 * @Version
 * @Description 每天执行一次，防止定时任务停止
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RunDayJob implements Job {
    @Autowired
    private IQuartzJobService quartzJobService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LambdaQueryWrapper<QuartzJob> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuartzJob::getStatus,0);
        List<QuartzJob> list = quartzJobService.list(queryWrapper);
        for (QuartzJob quartzJob : list) {
            String jobClassName = quartzJob.getJobClassName();
            if (jobClassName.contains("wbs")){
                continue;
            }
            try {
                quartzJobService.execute(quartzJob);
                Thread.sleep(5000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}