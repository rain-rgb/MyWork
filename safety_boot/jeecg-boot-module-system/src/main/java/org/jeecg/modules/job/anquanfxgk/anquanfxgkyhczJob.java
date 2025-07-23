package org.jeecg.modules.job.anquanfxgk;

import com.trtm.iot.anquanfxgk.service.IAnquanFxaqjcInfoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class anquanfxgkyhczJob implements Job {
    //风险-隐患处置-每天根据时间判断状态
    @Autowired
    private IAnquanFxaqjcInfoService anquanFxaqjcInfoService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        anquanFxaqjcInfoService.modifyFxaqjcTypeByZhenggaiTime();
    }
}
