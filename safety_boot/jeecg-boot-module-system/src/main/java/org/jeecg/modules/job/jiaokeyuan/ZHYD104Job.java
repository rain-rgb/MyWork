package org.jeecg.modules.job.jiaokeyuan;


import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qiyuesuo.sample.api.utils.crypt.MD5;
import com.trtm.iot.devicepowerhistorydata.entity.DevicePowerHistorydata;
import com.trtm.iot.devicepowerhistorydata.service.IDevicePowerHistorydataService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.stprojectjob.STUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2022/6/29
 * \* Time: 15:18
 * \* Description:
 * \
 */
@Slf4j
public class ZHYD104Job implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDevicePowerHistorydataService devicePowerHistorydataService;
    @Autowired
    private IPushandreturnService pushandreturnService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SY_ZHYD_JKY);//智慧用电数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到104智慧用电推送交科院定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置104智慧用电推送交科院的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<DevicePowerHistorydata> selecthntbhzones = devicePowerHistorydataService.selectListToSHYJ(shebeilist, curid);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无104智慧用电推送交科院数据" + DateUtils.now()));
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String userKey = "100036";
        String secret = "4023nia8ch3q9hc89hxnoqihdc8q9ncc";
        String timestamp = sdf.format(new Date());
        String signature = MD5.toMD5(userKey + secret + timestamp );


        int id = 0;
        for (DevicePowerHistorydata selecthntbhzone : selecthntbhzones) {
            id=selecthntbhzone.getId();
            JSONObject saveDTOList=new JSONObject();
            String deviceCode = selecthntbhzone.getImei();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceCode);
            saveDTOList.set("equipmentNumber",selecthntbhzone.getImei());//设备编号
            saveDTOList.set("dateInfo",selecthntbhzone.getPowerdate());//时间信息
            saveDTOList.set("distributionBoxGrade","二级箱");//配电箱等级
            saveDTOList.set("distributionBoxAzwz",selectshebeione.getSbname());//配电箱安装位置
            saveDTOList.set("aphaseVoltage",selecthntbhzone.getT9());//A相电压
            saveDTOList.set("bphaseVoltage",selecthntbhzone.getT10());//B相电压
            saveDTOList.set("cphaseVoltage",selecthntbhzone.getT11());//C相电压
            saveDTOList.set("aphaseCurrent",selecthntbhzone.getT6());//A相电流
            saveDTOList.set("bphaseCurrent",selecthntbhzone.getT7());//B相电流
            saveDTOList.set("cphaseCurrent",selecthntbhzone.getT8());//C相电流
            saveDTOList.set("lxdlOrLdl",Double.parseDouble(String.format("%.2f",selecthntbhzone.getT1()/1000))  );//零线电路或漏电流
            saveDTOList.set("aphasePower",selecthntbhzone.getT25());//A相功率
            saveDTOList.set("bphasePower",selecthntbhzone.getT26());//B相功率
            saveDTOList.set("cphasePower",selecthntbhzone.getT27());//C相功率
            saveDTOList.set("aphaseDnss",selecthntbhzone.getT54());//A相电能示数
            saveDTOList.set("bphaseDnss",selecthntbhzone.getT56());//B相电能示数
            saveDTOList.set("cphaseDnss",selecthntbhzone.getT58());//C相电能示数
            saveDTOList.set("zdnss",selecthntbhzone.getT60());//总电能示数
            saveDTOList.set("aphaseCableTemperature",selecthntbhzone.getT2());//A相电缆温度
            saveDTOList.set("bphaseCableTemperature",selecthntbhzone.getT3());//B相电缆温度
            saveDTOList.set("cphaseCableTemperature",selecthntbhzone.getT4());//C相电缆温度
            saveDTOList.set("alarmInfo","");//预警信息
            saveDTOList.set("longitudeData","");//经度数据
            saveDTOList.set("latitudeData","");//纬度数据
            saveDTOList.set("controlInstruction","");//控制指令
            saveDTOList.set("guardTourData","");//巡更数据
            saveDTOList.set("controlStatus","");//控制状态
            saveDTOList.set("distributionBoxNo",selecthntbhzone.getImei());//配电箱编号
            saveDTOList.set("closedLoopProcessing","");//闭环处理
            saveDTOList.set("projectType","");//项目类型
            saveDTOList.set("equipType","");//设备类型
            saveDTOList.set("controlNode","");//控制指令

           // jsonArray.add(saveDTOList);


            String post = HttpRequest.post("http://122.224.169.83:9888/iot-service/api/smartelectricity/data/add")
                    .header("Content-Type", "application/json")
                    .header("userKey",userKey)
                    .header("time",timestamp)
                    .header("sign",signature)
                    .body(String.valueOf(saveDTOList))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(post);
//            Boolean success = (Boolean) jsonObject.get("success");
//
//            if(success){
//                log.info(String.format("104智慧用电推送交科院成功!" + id));
//                sysConfigService.updateSysConfig(JobUtil.SY_ZHYD_JKY, id);//根据传过来的唯一值来修改当前判断到的数据id
//            }else{
//                log.info(String.format("104智慧用电推送交科院失败!" + id));
//                sysConfigService.updateSysConfig(JobUtil.SY_ZHYD_JKY, id);//根据传过来的唯一值来修改当前判断到的数据id
//            }
            sysConfigService.updateSysConfig(JobUtil.SY_ZHYD_JKY, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(saveDTOList), selectsysconfigone.getRemark(), String.valueOf(jsonObject1));


        }
    }
}
