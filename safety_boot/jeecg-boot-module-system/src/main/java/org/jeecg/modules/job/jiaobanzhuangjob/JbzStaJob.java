package org.jeecg.modules.job.jiaobanzhuangjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicemixpileonecfg.entity.DeviceMixpileOneCfg;
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
public class JbzStaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JBZ_STA);//水泥搅拌桩定时任务=71
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到水泥搅拌桩统计定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<DeviceMixpileHistorydataOne> jbzs = deviceMixpileHistorydataOneService.selectjbzzones(curid, 0);
        if (null == jbzs || jbzs.size() == 0) {
            log.info("暂无水泥搅拌桩未统计的数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (DeviceMixpileHistorydataOne deviceMixpileHistorydataOne:jbzs){
            id = deviceMixpileHistorydataOne.getId();
            try {
                String shebeibianhao = deviceMixpileHistorydataOne.getShebeino();//设备编号
                QueryWrapper<DeviceMixpileOneCfg> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeino",shebeibianhao);
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
                jobUtil.sattistics(deviceMixpileHistorydataOne,selectshebeione);
                DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 = new DeviceMixpileHistorydataOne();
                deviceMixpileHistorydataOne1.setId(id);
                deviceMixpileHistorydataOne1.setIstongji(1);
                deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne1);
            } catch (Exception e) {
                DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 = new DeviceMixpileHistorydataOne();
                deviceMixpileHistorydataOne1.setId(id);
                deviceMixpileHistorydataOne1.setIstongji(40);
                deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne1);
                e.printStackTrace();
            }
            log.info("搅拌桩统计!   时间:" + DateUtils.now());
        }
        sysConfigService.updateSysConfig(JobUtil.JBZ_STA, id);//根据传过来的唯一值来修改当前判断到的数据id
    }

}
