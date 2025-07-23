package org.jeecg.modules.job.jiaobanzhuangjob;

import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;
import com.trtm.iot.bhzStatistics.controller.BhzCementStatisticsController;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.bhzyuanliaoStatistic.entity.BhzYuanliaoStatistic;
import com.trtm.iot.bhzyuanliaoStatistic.service.IBhzYuanliaoStatisticService;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BhzStatisticJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzYuanliaoStatisticService bhzYuanliaoStatisticService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailStatisticsService bhzCementDetailStatisticsService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZYCLSTASTIC);//拌合站材料按照设备统计
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到拌合站材料按照设备统计定时任务的配置信息" + DateUtils.now());
            return;
        }

        // 瑞苍项目所有的拌合站编号
        List<ShebeiInfo> selectshebei = shebeiInfoService.selectBhzshebei();
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<BhzCementStatistics> jbzs = null;
        for(ShebeiInfo shebeiInfo :selectshebei){
            String sbjno = shebeiInfo.getSbjno();
            jbzs = bhzCementStatisticsService.selectjbzzones(sbjno,curid);
            if (null == jbzs || jbzs.size() == 0) {
                log.info("暂无拌合站材料按照设备统计的数据" + DateUtils.now());
                continue;
            }
            int id = 0;
            for (BhzCementStatistics bhzCementStatistics:jbzs){
                id = bhzCementStatistics.getId();
                String shebeiNo = bhzCementStatistics.getShebeiNo();
                try {
                    List<BhzCementDetailStatistics> bhzCementDetailStatistics =  bhzCementDetailStatisticsService.selectdetaillist(id);

                    if(bhzCementDetailStatistics.size()!=0){
                        for (BhzCementDetailStatistics bhz :bhzCementDetailStatistics){
                            String materialeName = bhz.getMaterialeName();
                            BhzYuanliaoStatistic bhzYuanliaoStatistic = bhzYuanliaoStatisticService.selectByShebei(shebeiNo, materialeName);

                            if(bhzYuanliaoStatistic != null){
                                bhzYuanliaoStatistic.setRealityNumber(bhzYuanliaoStatistic.getRealityNumber() + bhz.getRealityNumber());
                                bhzYuanliaoStatistic.setTheoryNumber(bhzYuanliaoStatistic.getTheoryNumber() + bhz.getTheoryNumber());
                                bhzYuanliaoStatisticService.updateById(bhzYuanliaoStatistic);
                            } else {
                                BhzYuanliaoStatistic bhzYuanliaoStatistic1 = new BhzYuanliaoStatistic();
                                bhzYuanliaoStatistic1.setShebeiNo(shebeiNo);
                                bhzYuanliaoStatistic1.setMaterialeName(materialeName);
                                bhzYuanliaoStatistic1.setRealityNumber(bhz.getRealityNumber());
                                bhzYuanliaoStatistic1.setTheoryNumber(bhz.getTheoryNumber());
                                bhzYuanliaoStatisticService.save(bhzYuanliaoStatistic1);
                            }
                        }
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("管桩统计!   时间:" + DateUtils.now());
            }
            sysConfigService.updateSysConfig(JobUtil.BHZYCLSTASTIC, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
