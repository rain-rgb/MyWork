package org.jeecg.modules.job.jiaobanzhuangjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicemixpileonecfg.entity.DeviceMixpileOneCfg;
import com.trtm.iot.devicepipepilehistorydataone.controller.DevicePipepileHistorydataOneController;
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
public class GzStaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IDevicePipepileHistorydataOneService pipepileHistorydataOneService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.GZSTAJOB);//管桩预警统计GZSTAJOB=113
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到管桩统计定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<DevicePipepileHistorydataOne> jbzs = pipepileHistorydataOneService.selectjbzzones(curid, 0);
        if (null == jbzs || jbzs.size() == 0) {
            log.info("暂无管桩未统计的数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (DevicePipepileHistorydataOne devicePipepileHistorydataOne:jbzs){
            id = devicePipepileHistorydataOne.getId();
            try {
                String shebeibianhao = devicePipepileHistorydataOne.getShebeino();//设备编号
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
                jobUtil.gzSattistics(devicePipepileHistorydataOne,selectshebeione);
                DevicePipepileHistorydataOne devicePipepileHistorydataOne1 = new DevicePipepileHistorydataOne();
                devicePipepileHistorydataOne1.setId(id);
                devicePipepileHistorydataOne1.setIstongji(1);
                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne1);
            } catch (Exception e) {
                DevicePipepileHistorydataOne devicePipepileHistorydataOne1 = new DevicePipepileHistorydataOne();
                devicePipepileHistorydataOne1.setId(id);
                devicePipepileHistorydataOne1.setIstongji(40);
                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne1);
                e.printStackTrace();
            }
            log.info("管桩统计!   时间:" + DateUtils.now());
        }
        sysConfigService.updateSysConfig(JobUtil.GZSTAJOB, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
