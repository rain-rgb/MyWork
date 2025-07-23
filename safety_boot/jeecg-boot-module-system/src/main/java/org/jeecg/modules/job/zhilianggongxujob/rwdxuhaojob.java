package org.jeecg.modules.job.zhilianggongxujob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class rwdxuhaojob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private IZhiliangGongxuService zhiliangGongxuService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RWDXUHAOJOB);//制梁任务单工序统计
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到管桩统计定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isdel", 0);
        queryWrapper.eq("status",1);
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        if (null == list || list.size() == 0) {
            log.info("暂无管桩未统计的数据" + DateUtils.now());
            return;
        }
        for (Zhiliangrenwudan l:list){
            try {
                QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("uuid",l.getUuid());
                List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper1);
                for(ZhiliangGongxu l1 :list1){
                    if(l1.getStatus() == 2){
                        l.setXuhao(l1.getXuhao().toString());
                    }
                }
                zhiliangrenwudanService.updateById(l);
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("管桩统计!   时间:" + DateUtils.now());
        }
    }
}
