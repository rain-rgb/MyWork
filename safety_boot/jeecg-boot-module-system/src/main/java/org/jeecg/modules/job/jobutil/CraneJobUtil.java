package org.jeecg.modules.job.jobutil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.byscfg.entity.BysCfg;
import com.trtm.iot.byscfg.service.IBysCfgService;
import com.trtm.iot.devicecranecfg.entity.DeviceCraneCfg;
import com.trtm.iot.devicecranecfg.service.IDeviceCraneCfgService;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.system.entity.ShebeiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CraneJobUtil {

    @Autowired
    private IDeviceCraneCfgService deviceCraneCfgService;

    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;

    private static CraneJobUtil craneJobUtil;

    @PostConstruct
    public void init() {
        craneJobUtil = this;
    }

    /**
     * 判断标养室温湿度数据是否超标
     *
     * @param deviceCraneHistorydata
     * @param
     * @param is_call
     * @return
     */
    public Map cranehistoryjudgment(DeviceCraneHistorydata deviceCraneHistorydata, ShebeiInfo selectshebeione, Integer is_call) {

        Map map = new HashMap<>();

        int final_over_level = 0;
        StringBuilder final_content = new StringBuilder();
        double mainHookloadlv = 0;//主钩吊重预警值
        double vicehookloadlv = 0;//副钩吊重预警值
        double windSpeedalarm = 0;//风速预警值
        double slr = 0;//激光测距预警值
        double mainVicelv = 0;//主副钩预警值
        String shebeino = selectshebeione.getSbjno();
        //获取大型设备历史数据数据
            double mainhookload = 0.0;
            double vicehookload = 0.0;
            double reservedone = 0.0;
            double reservedtwo = 0.0;
            double windSpeed = 0.0;
            double slr1 = 0.0;
            double slr2 = 0.0;
            if (null != deviceCraneHistorydata.getMainHookload()) {
                mainhookload = deviceCraneHistorydata.getMainHookload();
            }
            if (null != deviceCraneHistorydata.getReservedVicehookload()) {
                vicehookload = deviceCraneHistorydata.getReservedVicehookload();
            }
            if (null != deviceCraneHistorydata.getWindSpeed()) {
                windSpeed = deviceCraneHistorydata.getWindSpeed();
            }
            if (null != deviceCraneHistorydata.getSlr1()) {
                slr1 = deviceCraneHistorydata.getSlr1();
            }
            if (null != deviceCraneHistorydata.getSlr2()) {
                slr2 = deviceCraneHistorydata.getSlr2();
            }
            if (null != deviceCraneHistorydata.getReservedOne()) {
                reservedone = deviceCraneHistorydata.getReservedOne();
            }
            if (null != deviceCraneHistorydata.getReservedTwo()) {
                reservedtwo = deviceCraneHistorydata.getReservedTwo();
            }
            QueryWrapper<DeviceCraneCfg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_code",shebeino);
            DeviceCraneCfg deviceCraneCfg = deviceCraneCfgService.getOne(queryWrapper);
            if (null != deviceCraneCfg) {
                mainHookloadlv = deviceCraneCfg.getMainHookloadlv();
                vicehookloadlv = deviceCraneCfg.getVicehookloadlv();
                windSpeedalarm = deviceCraneCfg.getWindSpeedalarm();
                slr = deviceCraneCfg.getSlr();
                mainVicelv = deviceCraneCfg.getMainVicelv();
            } else  {
                mainHookloadlv = 30;
                vicehookloadlv = 30;
                windSpeedalarm = 10.8;
                slr = 20;
                mainVicelv = 1;
            }
            int slralarm = 0;
            int mainHookloadalarm = 0;
            int viceHookloadalarm = 0;
            int windSpeedstatus = 0;
            int mainVicealarm = 0;
            int status = 0;
            String massage = "";
            double mainchazhi = 0.0;
            double vicechazhi = 0.0;
            double mainVicechazhi = 0.0;
            double mainVicechazhilv = 0.0;
            mainVicechazhi = mainhookload - vicehookload;
            mainchazhi = Double.parseDouble(String.format("%.2f",(mainhookload - reservedone) * 100 / reservedone));
            if (null != deviceCraneHistorydata.getMainHookstatus()){
                mainHookloadalarm = deviceCraneHistorydata.getMainHookstatus();
                if (mainHookloadalarm>0){
                    status = 2;
                    massage = massage + "，" + "主钩载荷" + mainchazhi + "%";
                }
            } else {
                if (mainchazhi > mainHookloadlv){
                    mainHookloadalarm = 1;
                    status = 2;
                    massage = massage + "，" + "主钩载荷" + mainchazhi + "%";
                }
            }
            vicechazhi = Double.parseDouble(String.format("%.2f",(vicehookload - reservedtwo) * 100 / reservedtwo));
            if (null != deviceCraneHistorydata.getReservedVicehookstatus()){
                viceHookloadalarm = deviceCraneHistorydata.getReservedVicehookstatus();
                if (viceHookloadalarm>0){
                    status = 2;
                    massage = massage + "，" + "副钩载荷" + vicechazhi + "%";
                }
            }else {
                if (vicechazhi > vicehookloadlv) {
                    viceHookloadalarm = 1;
                    status = 2;
                    massage = massage + "，" + "副钩载荷" + vicechazhi + "%";
                }
            }
            if (null != deviceCraneHistorydata.getWindSpeedalarm()){
                windSpeedstatus = deviceCraneHistorydata.getWindSpeedalarm();
                if (windSpeedstatus > 0){
                    status = 2;
                    massage = massage + "，" + "风速超六级" ;
                }
            }else{
                if (windSpeed > windSpeedalarm){
                    windSpeedstatus = 1;
                    status = 2;
                    massage = massage + "，" + "风速超六级" ;
                }
            }
            double slrchazhi = slr1 - slr2;
            if (slrchazhi < 0){
                slrchazhi = - slrchazhi;//当激光测距差值为负时转为正
            }
            if (slrchazhi > slr){
                slralarm = 1;
                status = 2;
                massage = massage + "，" + "激光测距异常" ;
            }
            if (mainVicechazhi<0){
                mainVicechazhi = - mainVicechazhi;
            }
            mainVicechazhilv = mainVicechazhi*100/mainhookload;
            if (mainVicechazhilv > mainVicelv){
                mainVicealarm = 1;
                status = 2;
                massage = massage + "," + "主副钩吊重失衡";
            }
            if (status > final_over_level) {
                final_over_level = status;
            }
            DeviceCraneHistorydata deviceCraneHistorydata1 = new DeviceCraneHistorydata();
            deviceCraneHistorydata1.setMainHookstatus(mainHookloadalarm);
            deviceCraneHistorydata1.setReservedVicehookstatus(viceHookloadalarm);
            deviceCraneHistorydata1.setWindSpeedalarm(windSpeedstatus);
            deviceCraneHistorydata1.setSlralarm(slralarm);
            deviceCraneHistorydata1.setMainVicealarm(mainVicealarm);
            deviceCraneHistorydata1.setId(deviceCraneHistorydata.getId());
            deviceCraneHistorydataService.updateById(deviceCraneHistorydata1);
            final_content.append(String.format("%1$s", massage));
        map.put("status",final_over_level);
        map.put("final_content", final_content.toString());
        return map;
    }
}
