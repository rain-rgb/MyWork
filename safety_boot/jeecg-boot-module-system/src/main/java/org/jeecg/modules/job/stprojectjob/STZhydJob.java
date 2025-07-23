package org.jeecg.modules.job.stprojectjob;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2022/6/29
 * \* Time: 15:18
 * \* Description:
 * \
 */
@Slf4j
public class STZhydJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceElectricHistorydataService deviceElectricHistorydataService;

    @Autowired
    private STUtil stUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.STTWO_ZHYD);//智慧用电数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台智慧用电定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台智慧用电的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<DeviceElectricHistorydata> selecthntbhzones = deviceElectricHistorydataService.selectLists(strsToList1, curid);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无瑞仓苏台用电未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        JSONArray jsonArray=new JSONArray();
        for (DeviceElectricHistorydata selecthntbhzone : selecthntbhzones) {
            id=selecthntbhzone.getId();
            JSONObject saveDTOList=new JSONObject();
            String deviceCode = selecthntbhzone.getImei();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceCode);
            saveDTOList.set("electricityA",selecthntbhzone.getElectricitya());//电流A
            saveDTOList.set("electricityB",selecthntbhzone.getElectricityb());//
            saveDTOList.set("electricityC",selecthntbhzone.getElectricityc());//
            saveDTOList.set("equipmentName",selectshebeione.getSbname());//设备名称
            saveDTOList.set("frequencyA",selecthntbhzone.getFrequencya());//频率A（hz）
            saveDTOList.set("frequencyB",selecthntbhzone.getFrequencyb());//
            saveDTOList.set("frequencyC",selecthntbhzone.getFrequencyc());//
            saveDTOList.set("kwhA",selecthntbhzone.getEnergya());//电能A（KW.H）
            saveDTOList.set("kwhB",selecthntbhzone.getEnergyb());//
            saveDTOList.set("kwhC",selecthntbhzone.getEnergyc());//
            saveDTOList.set("leakCurrent",selecthntbhzone.getSeepelectricity());//漏电流（mA）
            Integer status = selecthntbhzone.getStatus();
            if(status==1){
                saveDTOList.set("pass",1);//是否合格 0否 1是
            }else{
                saveDTOList.set("pass",0);//是否合格 0否 1是
            }
            saveDTOList.set("sectionType",1);//1 一标 2 二标 3 三标 4 四标 5 五标
            saveDTOList.set("temperatureA",selecthntbhzone.getTempa());//温度A（℃）
            saveDTOList.set("temperatureB",selecthntbhzone.getTempb());//
            saveDTOList.set("temperatureC",selecthntbhzone.getTempc());//
            saveDTOList.set("temperatureN",selecthntbhzone.getTempn());//
            saveDTOList.set("uploadTime",selecthntbhzone.getElecdate());//数据上传时间
            saveDTOList.set("voltageA",selecthntbhzone.getVoltagea());//电压A（V）
            saveDTOList.set("voltageB",selecthntbhzone.getVoltageb());//
            saveDTOList.set("voltageC",selecthntbhzone.getVoltagec());//
            jsonArray.add(saveDTOList);
        }
        if(jsonArray.size()>0){
            Integer integer = stUtil.PostSTZHYDList(jsonArray);
            if(integer==200){
                log.info(String.format("苏台智慧用电数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.STTWO_ZHYD, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else{
                log.info(String.format("苏台智慧用电数据推送失败!" + id));
            }
        }else{
            sysConfigService.updateSysConfig(JobUtil.STTWO_ZHYD, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
