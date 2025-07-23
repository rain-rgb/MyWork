package org.jeecg.modules.job.stprojectjob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * \* @author: Xx
 * \* Date: 2022/5/30
 * \* Time: 16:08
 * \* Description:苏台二期 环境监测
 * \
 */
@Slf4j
public class STHjjcJob implements Job {
    @Autowired
    private STUtil stUtil;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicehistoryService devicehistoryService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_HJJC);//苏台环境监测
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台环境监测定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台环境监测的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<Devicehistory> devicehistories = new ArrayList<>();
        for (String shebeino : strsToList1) {
            Devicehistory devicehistory = devicehistoryService.selectNewByDev(shebeino);
            if (null != devicehistory && devicehistory.getIstuisong() != null && devicehistory.getIstuisong() == 0) {
                devicehistories.add(devicehistory);
                devicehistoryService.upadteIstuisong(devicehistory.getId(),1);
            }
        }
         if (null == devicehistories || devicehistories.size() == 0) {
            log.info(String.format("暂无苏台环境监测的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        JSONArray jsonArray=new JSONArray();
        for (Devicehistory devicehistory : devicehistories) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONObject saveDTOList=new JSONObject();
            String devaddr = devicehistory.getDevaddr();
            String humidity = devicehistory.getHumidity();
            id=devicehistory.getId();
            String noise = devicehistory.getNoise();
            String pm10 = devicehistory.getPm10();
            String pm25 = devicehistory.getPm25();
            String temperature = devicehistory.getTemperature();
            Date timevalue = devicehistory.getTimevalue();
            String wind = devicehistory.getWind();
            String winddirection = devicehistory.getWinddirection();
            String windspeed = devicehistory.getWindspeed();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(devaddr);
            saveDTOList.set("atmosphericPressure",null);//大气压力
            saveDTOList.set("equipmentName",selectshebeione.getSbname());//设备名称
            saveDTOList.set("humidity",humidity);//湿度
            saveDTOList.set("noise",noise);//噪声
            saveDTOList.set("pm10",pm10);//pm10（ug/m³)
            saveDTOList.set("pm25",pm25);//pm2.5(ug/m³)
            saveDTOList.set("poisonousGas",null);//有害气体
            saveDTOList.set("sectionType",selectshebeione.getProcode());//1 一标 2 二标 3 三标 4 四标 5 五标
            saveDTOList.set("temperature",temperature);//温度
            saveDTOList.set("tsp",null);//TSP
            String format = f.format(timevalue);
            saveDTOList.set("uploadTime",format);//TSP


            if(winddirection==null){
                saveDTOList.set("windDirection",null);//风向
            }else{
                saveDTOList.set("windDirection",winddirection);//风向
            }
            if(windspeed==null){
                saveDTOList.set("windSpeed",null);//风速
            }else{
                saveDTOList.set("windSpeed",windspeed);//风速
            }
            jsonArray.add(saveDTOList);
        }
        if(jsonArray.size()>0){
            Integer integer = stUtil.PostSTHjjcList(jsonArray);
            if(integer==200){
                log.info(String.format("苏台环境监测数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.ST_HJJC, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else{
                log.info(String.format("苏台环境监测数据推送失败!" + id));
            }
        }else{
            log.info(String.format("苏台拌合站当前数据属于一期" + id));
            sysConfigService.updateSysConfig(JobUtil.ST_HJJC, id);//根据传过来的唯一值来修改当前判断到的数据id
        }

    }
}
