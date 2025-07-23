package org.jeecg.modules.job.scgedulingcarjob;

import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.service.IRenwudanScheduleService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.jeecg.common.util.DateUtils.differHours;

/**
 * \* @author: Xx
 * \* Date: 2021/6/16
 * \* Time: 17:17
 * \* Description:根据任务单的任务编号去查询拌合站的数据去计算进度以及给新的表添加数据
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SchedulingCarJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IRenwudanScheduleService renwudanScheduleService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISchedulingCarService schedulingCarService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SCHEDULING_CAR_ALL);//查询所有的发车单信息判断发车状态
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到发车单定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<SchedulingCar> selecthntbhzones = schedulingCarService.querylist(0,curid);//没有进行过进度统计的数据
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
//            log.info(String.format("暂无发车单数据" + DateUtils.now()));
//            return;
            SysConfig selectsysconfigone1 = sysConfigService.selectsysconfigone(JobUtil.SCHEDULING_CAR_ONE);//查询所有的发车单信息判断发车状态
            if (null == selectsysconfigone1) {
                log.info(String.format("未获取到发车单定时任务的配置信息" + DateUtils.now()));
                return;
            }
            Integer curid1 = selectsysconfigone1.getCurid();
            List<SchedulingCar> selecthntbhzone = schedulingCarService.querylist(1,curid1);//没有进行过进度统计的数据
            int id1=0;
            for (SchedulingCar schedulingCar : selecthntbhzone) {
                try {
                    id1 = schedulingCar.getId();
                    Date dattim = schedulingCar.getDattim();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String format = ft.format(dattim);
                    String format1 = ft.format(new Date());
                    Integer df = differHours(format,format1);
                    if(df>2){
                        SchedulingCar schedulingCar1=new SchedulingCar();
                        schedulingCar1.setId(id1);
                        schedulingCar1.setStatus1(2);
                        boolean b = schedulingCarService.updateById(schedulingCar1);
                        if(b){
                            log.info(String.format("修改发车单状态信息成功" + DateUtils.now()));
                        }else{
                            log.info(String.format("修改发车单状态信息失败" + DateUtils.now()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sysConfigService.updateSysConfig(JobUtil.SCHEDULING_CAR_ONE, id1);//根据传过来的唯一值来修改当前判断到的数据id
        }
        int id=0;
        for (SchedulingCar selecthntbhzone : selecthntbhzones) {
            id = selecthntbhzone.getId();
            try{
                Date dattim = selecthntbhzone.getDattim();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = ft.format(dattim);
                String format1 = ft.format(new Date());
                Integer df = differHours(format,format1);
                if(df>2){
                    SchedulingCar schedulingCar=new SchedulingCar();
                    schedulingCar.setId(id);
                    schedulingCar.setStatus1(2);
                    boolean b = schedulingCarService.updateById(schedulingCar);
                    if(b){
                        log.info(String.format("修改发车单状态信息成功" + DateUtils.now()));
                    }else{
                        log.info(String.format("修改发车单状态信息失败" + DateUtils.now()));
                    }
                }else {
                    SchedulingCar schedulingCar=new SchedulingCar();
                    schedulingCar.setId(id);
                    schedulingCar.setStatus1(1);
                    boolean b = schedulingCarService.updateById(schedulingCar);
                    if(b){
                        log.info(String.format("修改发车单状态信息成功" + DateUtils.now()));
                    }else{
                        log.info(String.format("修改发车单状态信息失败" + DateUtils.now()));
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info(String.format("发车单状态判断!   时间:" + DateUtils.now()));
        }
        sysConfigService.updateSysConfig(JobUtil.SCHEDULING_CAR_ALL, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
