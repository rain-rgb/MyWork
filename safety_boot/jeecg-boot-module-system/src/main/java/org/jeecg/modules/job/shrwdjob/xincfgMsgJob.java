package org.jeecg.modules.job.shrwdjob;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.deviceelectricxincfg.entity.DeviceElectricXincfg;
import com.trtm.iot.deviceelectricxincfg.service.IDeviceElectricXincfgService;
import com.trtm.iot.devicepowerhistorydata.entity.DevicePowerHistorydata;
import com.trtm.iot.devicepowerhistorydata.service.IDevicePowerHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.sy.syshrwd.entity.Syshrwd;
import com.trtm.sy.syshrwd.service.ISyshrwdService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class xincfgMsgJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;
    @Autowired
    private IDevicePowerHistorydataService devicePowerHistorydataService;
    @Autowired
    private IDeviceElectricXincfgService deviceElectricXincfgService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.XINCFG_SHDXTX);//新智慧用电预警短信提示
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到新智慧用电预警短信提醒定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<DevicePowerHistorydata> devicePowerHistorydata = devicePowerHistorydataService.sendMsg(curid);
        if (null == devicePowerHistorydata || devicePowerHistorydata.size() == 0) {
            log.info("暂无新智慧用电预警短信提醒未发送的数据");
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (DevicePowerHistorydata powerHistorydata : devicePowerHistorydata) {
            id = powerHistorydata.getId();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(powerHistorydata.getImei());
            JSONObject obj = new JSONObject();
            DeviceElectricXincfg deviceElectricXincfg = deviceElectricXincfgService.selectbyshebeione(powerHistorydata.getImei());
            if(deviceElectricXincfg == null){
                continue;
            }
            BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(deviceElectricXincfg.getZhydPhones());

            obj.put("content", selectshebeione.getSbname() +"漏电"+powerHistorydata.getT1()+ "mA,预警");
            obj.put("sbname", selectshebeione.getSbname());
            String toString = obj.toString().substring(1,obj.toString().length()-1);
            SysMessageCore selectOne = sysMessageCoreService.selectOne(toString,bhzPhone.getPhones(),"智慧用电");
            if(selectOne == null){
                SysMessageCore sysMessageCore = new SysMessageCore();
                sysMessageCore.setEsTitle("用电设备预警提醒");
                sysMessageCore.setEsType("1");
                sysMessageCore.setEsSendStatus("0");
                sysMessageCore.setEsSendNum(0);
                sysMessageCore.setEsReceiver(bhzPhone.getPhones());
                JSONObject obj1 = new JSONObject();
//                obj1.put("sbname", syshrwd.getMudidi());
                obj1.put("time", sdf.format(powerHistorydata.getPowerdate()));
                obj1.put("content",  selectshebeione.getSbname() +"漏电"+powerHistorydata.getT1()+ "mA,预警");
                obj1.put("sbname", selectshebeione.getSbname());
                sysMessageCore.setEsContent(obj1.toString());
                boolean save = sysMessageCoreService.save(sysMessageCore);
                if (save) {
                    log.info(String.format("新智慧用电设备短信提醒信息发送成功" + DateUtils.now()));
                } else {
                    log.info(String.format("新智慧用电设备短信提醒信息发送失败" + DateUtils.now()));
                }
            }


        }
        sysConfigService.updateSysConfig(JobUtil.XINCFG_SHDXTX, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
//    //判断两个时间相差多少小时
//    private static int getDifferHour(Date startDate, Date endDate) {
//        long dayM = 1000 * 24 * 60 * 60;
//        long hourM = 1000 * 60 * 60;
//        long differ = endDate.getTime() - startDate.getTime();
//        long hour = differ % dayM / hourM + 24 * (differ / dayM);
//        return Integer.parseInt(String.valueOf(hour));
//    }
}
