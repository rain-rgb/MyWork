package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * \* @author: Xx
 * \* Date: 2022/5/27
 * \* Time: 10:18
 * \* Description:
 * \
 */
@Slf4j
public class ceshijob implements Job {
    @Autowired
    private JobUtil jobUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("任务1111111定时任务 执行: " + Thread.currentThread() + "-" + DateTime.now());
        int i=1;
        jobUtil.ceshi(i);
    }
}
