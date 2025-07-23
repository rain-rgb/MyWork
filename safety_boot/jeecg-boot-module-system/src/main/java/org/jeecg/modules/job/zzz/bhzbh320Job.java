package org.jeecg.modules.job.zzz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import groovy.util.logging.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName bhzbh320Job
 * @Author
 * @Date 2024/11/18 9:24
 * @Version
 * @Description 320拌合站超标自动闭合
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bhzbh320Job implements Job {

    @Autowired
    private IBhzCementBaseService bhzCementBaseService;

    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LambdaQueryWrapper<BhzCementBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.gt(BhzCementBase::getOverLevel, 0)
                .likeRight(BhzCementBase::getShebeiNo, "320")
                .eq(BhzCementBase::getOverproofStatus,0);
        List<BhzCementBase> bhzCementBaseList = bhzCementBaseService.list(queryWrapper);
        for (BhzCementBase bhzCementBase : bhzCementBaseList) {
            String batchNo = bhzCementBase.getBatchNo();

            BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
            bhzCementOverHandler.setBaseid(batchNo);
            bhzCementOverHandler.setOverproofStatus(10);
            bhzCementOverHandler.setHandleResult("已处理");
            bhzCementOverHandler.setHandlePerson("实验室");
            bhzCementOverHandlerService.saveOrUpdate(bhzCementOverHandler);


            bhzCementBase.setOverproofStatus(10);
            bhzCementBaseService.updateById(bhzCementBase);
        }
    }
}