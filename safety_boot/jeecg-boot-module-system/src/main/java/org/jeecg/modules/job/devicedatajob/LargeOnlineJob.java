package org.jeecg.modules.job.devicedatajob;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.gualan.entity.GualanBase;
import com.trtm.iot.gualan.service.IGualanBaseService;
import com.trtm.iot.gualanreal.entity.GualanBaseReal;
import com.trtm.iot.gualanreal.service.IGualanBaseRealService;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeHistorydata;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeRealdata;
import com.trtm.iot.jiaqiaoji.service.IDeviceBridgeHistorydataService;
import com.trtm.iot.jiaqiaoji.service.IDeviceBridgeRealdataService;
import com.trtm.iot.largesbconfigure.entity.LargesbConfigure;
import com.trtm.iot.largesbconfigure.entity.LargesbHistory;
import com.trtm.iot.largesbconfigure.service.ILargesbConfigureService;
import com.trtm.iot.largesbconfigure.service.ILargesbHistoryService;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tadiao.entity.DeviceTowerHistorydata;
import com.trtm.iot.tadiao.service.IDeviceTowerHistorydataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Calendar;


/**
 * \* @author: zxy
 * \* Date: 2023/09/13
 * \* Time: 14:07
 * \* Description:  大型设备挂篮预警数据短信配置
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LargeOnlineJob implements Job {

    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IGualanBaseService gualanBaseService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceBridgeHistorydataService deviceBridgeHdataService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;
    @Autowired
    private IDeviceTowerHistorydataService deviceTowerHistorydataService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.DXSB_ZXZT);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("大型设备在线状态判断 未配置" + DateUtils.now());
            return;
        }

        log.info("大型设备在线状态判断" + DateUtils.now());
        List<ShebeiInfo> ShebeiInfoList = shebeiInfoService.usershebeiList("A05", Arrays.asList("22", "23", "51", "21"));


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);
        Date mbdate =calendar.getTime();
        String pattern = "yyyy-MM-dd HH:mm:ss";
        for(ShebeiInfo shebeiInfo : ShebeiInfoList){
            // 挂篮
            int i = 0;
             if(shebeiInfo.getSbtype() == 22){
                 QueryWrapper<GualanBase> queryWrapper = new QueryWrapper<>();
                 queryWrapper.eq("shebeibanhao", shebeiInfo.getSbjno());
                 queryWrapper.gt("datatime", mbdate);
                 queryWrapper.last(" limit 2 ");
                 List<GualanBase> list = gualanBaseService.list(queryWrapper);
                  i = list.size();
                 // 架桥机
             }else if(shebeiInfo.getSbtype() == 23){
                 QueryWrapper<DeviceBridgeHistorydata> queryWrapper = new QueryWrapper<>();
                 queryWrapper.eq("device_code", shebeiInfo.getSbjno());
                 queryWrapper.gt("bridgedate", mbdate);
                 queryWrapper.last(" limit 2 ");
                 List<DeviceBridgeHistorydata> list = deviceBridgeHdataService.list(queryWrapper);
                 i = list.size();
                 // 龙门吊
             }else if(shebeiInfo.getSbtype() == 21){
                 QueryWrapper<DeviceCraneHistorydata> queryWrapper = new QueryWrapper<>();
                 queryWrapper.eq("device_code", shebeiInfo.getSbjno());
                 queryWrapper.gt("cranedate", mbdate);
                 queryWrapper.last(" limit 2 ");
                 List<DeviceCraneHistorydata> list = deviceCraneHistorydataService.list(queryWrapper);
                 i = list.size();

                 //塔吊
             }else if(shebeiInfo.getSbtype() == 51){
                 QueryWrapper<DeviceTowerHistorydata> queryWrapper = new QueryWrapper<>();
                 queryWrapper.eq("device_code", shebeiInfo.getSbjno());
                 queryWrapper.gt("towerdate", mbdate);
                 queryWrapper.last(" limit 2 ");
                 List<DeviceTowerHistorydata> list = deviceTowerHistorydataService.list(queryWrapper);
                 i = list.size();
             }

            if(i>0){
                shebeiInfoService.updatestatus(shebeiInfo.getSbjno(),3);
            }else{
                shebeiInfoService.updatestatus(shebeiInfo.getSbjno(),1);
            }
        }

        sysConfigService.updateSysConfigToken(JobUtil.DXSB_ZXZT,"token");

    }




}
