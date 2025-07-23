package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * \* @author: Xx
 * \* Date: 2022/5/28
 * \* Time: 13:06
 * \* Description:
 * \
 */
@Slf4j
public class ceshi1job implements Job {
    @Autowired
    private JobUtil jobUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("任务222222222定时任务 执行: " + Thread.currentThread() + "-" + DateTime.now());
        int i=11;
        jobUtil.ceshi(i);
    }
}
