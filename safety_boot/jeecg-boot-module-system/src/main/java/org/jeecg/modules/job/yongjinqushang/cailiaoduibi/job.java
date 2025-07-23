package org.jeecg.modules.job.yongjinqushang.cailiaoduibi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @ClassName job
 * @Author
 * @Date 2025/3/3 9:06
 * @Version
 * @Description 对比材料是否一致
 */
public class job implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementDetailService BhzCementDetailService;
    @Autowired
    private IBhzCementBaseService BhzCementBaseService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LambdaQueryWrapper<BhzCementBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BhzCementBase::getShebeiNo, "sszwbh01_01", "sszwbh01_02")
                .ge(BhzCementBase::getProductDatetime,"2024-08-01 00:20:57")
                .le(BhzCementBase::getProductDatetime,"2025-03-05 09:00:04")
                .eq(BhzCementBase::getIsyz,10);
        List<BhzCementBase> list = BhzCementBaseService.list(lambdaQueryWrapper);
        for (BhzCementBase bhzCementBase : list) {
            String batchNo = bhzCementBase.getBatchNo();
            Date productDatetime = bhzCementBase.getProductDatetime();
            String projectName = bhzCementBase.getProjectName();
            LambdaQueryWrapper<BhzCementBase> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(BhzCementBase::getProductDatetime,productDatetime)
                    .eq(BhzCementBase::getProjectName,projectName)
                    .eq(BhzCementBase::getShebeiNo,"sszwbh01_01-test")
                    .last("limit 1");
            BhzCementBase bhzCementBase1 = BhzCementBaseService.getOne(lambdaQueryWrapper1);
            if (bhzCementBase1 == null) {
                bhzCementBase.setIsyz(3);
                BhzCementBaseService.updateById(bhzCementBase);
                continue;
            }
            String caijino = bhzCementBase1.getBatchNo();

            LambdaQueryWrapper<BhzCementDetail> BhzCementDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            BhzCementDetailLambdaQueryWrapper.eq(BhzCementDetail::getBatchNo,batchNo);
            List<BhzCementDetail> bhzCementDetailList = BhzCementDetailService.list(BhzCementDetailLambdaQueryWrapper);
            int i = 0;
            for (BhzCementDetail bhzCementDetail : bhzCementDetailList) {
                String materialeName = bhzCementDetail.getMaterialeName();
                LambdaQueryWrapper<BhzCementDetail> BhzCementDetailLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                BhzCementDetailLambdaQueryWrapper1.eq(BhzCementDetail::getBatchNo,caijino)
                        .eq(BhzCementDetail::getMaterialeName,materialeName).last("limit 1");
                BhzCementDetail bhzCementDetail1 = BhzCementDetailService.getOne(BhzCementDetailLambdaQueryWrapper1);
                if (bhzCementDetail1 != null) {
                    Double realityNumber1 = bhzCementDetail1.getRealityNumber();
                    Double theoryNumber1 = bhzCementDetail1.getTheoryNumber();
                    bhzCementDetail.setRealityNumber1(realityNumber1);
                    bhzCementDetail.setTheoryNumber1(theoryNumber1);
                    Double realityNumber = bhzCementDetail.getRealityNumber();
                    Double theoryNumber = bhzCementDetail.getTheoryNumber();
                    if (!realityNumber.equals(realityNumber1)||!theoryNumber.equals(theoryNumber1)) {
                        i = 1;
                    }
                    BhzCementDetailService.updateById(bhzCementDetail);
                }
            }
            if (i == 1) {
                bhzCementBase.setIsyz(1);
            } else {
                bhzCementBase.setIsyz(0);
            }
            bhzCementBase1.setIsyz(10);
            BhzCementBaseService.updateById(bhzCementBase1);
            BhzCementBaseService.updateById(bhzCementBase);
        }
    }
}