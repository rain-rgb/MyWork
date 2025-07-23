package org.jeecg.modules.job.zhydjob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhydcfg.entity.DeviceElectricCfg;
import com.trtm.iot.zhydcfg.service.IDeviceElectricCfgService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.ZhydJobUntil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 智慧用电历史数据超标检测定时任务
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ZhydHistoryJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IDeviceElectricHistorydataService deviceElectricHistorydataService;

    @Autowired
    private IDeviceElectricCfgService deviceElectricCfgService;

    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    private IBhzPhoneService bhzPhoneService;

    @Autowired
    private ZhydJobUntil zhydJobUntil;

    @Autowired
    private ISysMessageService sysMessageService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(String.format(" Jeecg-Boot 智慧用电定时任务 SampleJob !  时间:" + DateUtils.getTimestamp()));
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZHYDHISTORY_CFG);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到智慧用电定时任务配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<DeviceElectricHistorydata> selectzhydhistoryones = deviceElectricHistorydataService.selectzhydhistoryone(curid,0);
        if (null == selectzhydhistoryones || selectzhydhistoryones.size() == 0) {
            log.info(String.format("暂无智慧用电未判断的数据"));
            return;
        }
        int id = 0;
        for (DeviceElectricHistorydata selectzhydhistoryone : selectzhydhistoryones) {
            id = selectzhydhistoryone.getId();
            Integer is_call = 0; //是否报警  1不报警 0报警
            String zhydUid = "";//DEVICE_ELECTRIC_CFG 的智慧用电超标电话号配置
            String zhydPhones = "";//DEVICE_ELECTRIC_CFG 的智慧用电超标电话号
            StringBuilder final_content = new StringBuilder();
            int final_over_level = 0;
            String elecdate = DateUtil.format(selectzhydhistoryone.getElecdate(), "yyyy-MM-dd HH:mm:ss");//数据上传时间
            String imei = selectzhydhistoryone.getImei();//设备编号
            try {
                DeviceElectricCfg selectzhydcallone = deviceElectricCfgService.selectzhydcallone(imei);
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(imei);
                //判断是否有设备
                if (null == selectshebeione) {
                    log.info(String.format("暂无智慧用电的设备" + DateUtils.now()));
                    continue;
                }
                if (null != selectzhydcallone) {
                    is_call = selectzhydcallone.getIsCall();//是否报警
                }
                //相对应的超标
                if (null != selectzhydcallone){
                    if (null != selectzhydcallone.getZhydPhones()){
                        zhydUid = selectzhydcallone.getZhydPhones();
                    }
                }
                if (StrUtil.isNotBlank(zhydUid)){
                    BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(zhydUid);
                    if (null != bhzPhone){
                        zhydPhones = bhzPhone.getPhones();
//                        primaryNames = bhzPhone.getNames();
                    }
                }
                //判断智慧用电的电压电流频率温度等各个指标是否超标
                Map map = zhydJobUntil.zhydHistoryOver(selectzhydhistoryone,selectshebeione,is_call);
                int status = (int) map.get("status");//状态（1：合格，2：不合格）
                final_content.append(map.get("final_content"));//智慧用电各指标超标信息
                final_over_level = status;
                if (0 == final_over_level || final_content.length() == 0){
                    final_over_level = 1;
                    deviceElectricHistorydataService.updateStatus(id,final_over_level,1);
                    continue;
                }

                JSONObject obj = new JSONObject();
                obj.put("sbname", selectshebeione.getSbname());
                obj.put("time",elecdate);
                obj.put("content", final_content);   //
                if (!zhydPhones.equals("")) {
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setEsTitle("智慧用电超标检测");
                    sysMessage.setEsType("1");//短信类型  1短信
                    sysMessage.setEsReceiver(zhydPhones);//手机号
                    sysMessage.setEsContent(obj.toString());//短信内容
                    sysMessage.setRemark(imei);
                    if (is_call == 0) {
                        sysMessage.setEsSendStatus("0");//推送状态0未推送
                        sysMessage.setEsSendNum(0);//推送总次数
                    } else {
                        sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage.setEsSendNum(0);//推送总次数
                        sysMessage.setEsResult("不需要推送");
                    }
                    sysMessage.setCreateTime(new Date());
                    sysMessageService.save(sysMessage);
                }

                DeviceElectricHistorydata deviceElectricHistorydata = new DeviceElectricHistorydata();
                deviceElectricHistorydata.setId(id);
                deviceElectricHistorydata.setStatus(final_over_level);
                deviceElectricHistorydata.setAlertstate(1);
                deviceElectricHistorydataService.updateById(deviceElectricHistorydata);  //智慧用电修改超标状态以及检测过状态
            }catch (Exception e){
                e.printStackTrace();
                deviceElectricHistorydataService.updatezhydone(id,40);
            }
            log.info(String.format("智慧用电历史数据表超标检测！   时间" +DateUtils.now(),"当前判断到"+id));
        }
        sysConfigService.updateSysConfig(JobUtil.ZHYDHISTORY_CFG,id);
    }
}
