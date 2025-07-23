package org.jeecg.modules.job.hntbhzjob;

import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.NumberUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TbhzStatisticsRCJob2 implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private JobUtil jobUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_BH_TJ_RC);//拌合站处置统计瑞仓项目
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到拌合站处置统计瑞仓项目定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当时数据检测到的目标id
        Integer isworking = selectsysconfigone.getIsworking();
//        if (isworking == 1) {
//            log.info(String.format("拌合站处置统计瑞仓项目定时任务正在运行中！！" + DateUtils.now()));
//            return;
//        }
        List<BhzCementBase> bhzCementBaseList = bhzCementBaseService.selecthntbhzbhRC(1, 0);
        if (null == bhzCementBaseList || bhzCementBaseList.size() == 0) {
            log.info(String.format("暂无砼拌合站未闭合统计的数据" + DateUtils.now()));
            return;
        }
        sysConfigService.updateSysConfigs(JobUtil.BHZ_BH_TJ_RC, 0, 1);
        for (BhzCementBase bhzCementBase : bhzCementBaseList) {
            Date productDatetime = bhzCementBase.getProductDatetime();
            String datanyr = NumberUtil.Stringnyr(productDatetime);//格式化后的时间
            Date datanyr1 = NumberUtil.datanyr(datanyr);
            String projectName = bhzCementBase.getProjectName();
            String poureLocation = bhzCementBase.getPoureLocation();
            String jobLocation = bhzCementBase.getJobLocation();
            String formulaNo = bhzCementBase.getFormulaNo();
            String strengthRank = bhzCementBase.getStrengthRank();
            String shebeiNo = bhzCementBase.getShebeiNo();
            //查询统计表中匹配的数据
            BhzCementStatistics selectlimit = bhzCementStatisticsService.selectlimit(datanyr1, projectName, poureLocation, jobLocation, formulaNo, strengthRank, shebeiNo);
            //未处置、审核跳出循环
            if (bhzCementBase.getOverproofStatus() == 0) {
                continue;
            }
            if(selectlimit == null){
                continue;
            }
            //已处置，添加处置盘数
            if (bhzCementBase.getOverLevel() != null) {
                BhzCementStatistics statistics = new BhzCementStatistics();
                statistics.setId(selectlimit.getId());
                if (selectlimit.getAllHandleDish() == null) {
                    statistics.setAllHandleDish(1);
                } else {
                    int num = selectlimit.getAllHandleDish() + 1;
                    statistics.setAllHandleDish(num);
                }
                if (bhzCementBase.getOverLevel() == 1) {
                    if (selectlimit.getPrimaryHandle() == null) {
                        statistics.setPrimaryHandle(1);
                    } else {
                        int num = selectlimit.getPrimaryHandle() + 1;
                        statistics.setPrimaryHandle(num);
                    }
                } else if (bhzCementBase.getOverLevel() == 2) {
                    if (selectlimit.getMiddleHandle() == null) {
                        statistics.setMiddleHandle(1);
                    } else {
                        int num = selectlimit.getMiddleHandle() + 1;
                        statistics.setMiddleHandle(num);
                    }
                } else if (bhzCementBase.getOverLevel() == 3) {
                    if (selectlimit.getAdvancedHandle() == null) {
                        statistics.setAdvancedHandle(1);
                    } else {
                        int num = selectlimit.getAdvancedHandle() + 1;
                        statistics.setAdvancedHandle(num);
                    }
                }
                bhzCementStatisticsService.updateById(statistics);
                //更改主表中处置统计状态
                BhzCementBase cementBase = new BhzCementBase();
                cementBase.setId(bhzCementBase.getId());
                cementBase.setCbStatistics(1);
                cementBase.setBhStatus(1);
                bhzCementBaseService.updateById(cementBase);
            }
        }

        sysConfigService.updateSysConfigs(JobUtil.BHZ_BH_TJ_RC, 0, 0);//根据传过来的唯一值来修改当前判断到的数据id

    }
}
