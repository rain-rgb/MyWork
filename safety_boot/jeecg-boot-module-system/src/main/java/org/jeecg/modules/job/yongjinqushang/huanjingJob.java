package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName huanjingJob：
 * @Description TODO
 * @Author 55314
 * @Date 2024/2/28 14:39
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class huanjingJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDevicehistoryService devicehistoryService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "http://115.236.10.10:8081/zjjg-iot/dust-monitoring/saveDustMonitoring";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJQS_HJJC);//甬金衢上拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到甬金衢上砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输甬金衢上砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Devicehistory> devicehistories = devicehistoryService.selectlistYJQS(shebeilist, curid);
        if (null == devicehistories || devicehistories.size() == 0) {
            log.info(String.format("暂无甬金衢上环境监测的数据" + DateUtils.now()));
            return;
        } else {
            log.info(String.format("甬金衢上砼环境监测数据推送开始！" + DateUtils.now()));
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Devicehistory devicehistory : devicehistories) {
            id = devicehistory.getId();
            String format = sdf.format(devicehistory.getTimevalue());
            JSONObject sendObject = new JSONObject();
            sendObject.set("id", "yjqs" + devicehistory.getId());//	唯一id
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(devicehistory.getDevaddr());
            sendObject.set("equipmentName", sbjwd.getSbname());//	设备名称
            sendObject.set("equipmentNumber", devicehistory.getDevaddr());//	设备编号
            sendObject.set("createDate", format);//	创建时间
            sendObject.set("dataTime", format);//	数据区里的数据时间信息
            sendObject.set("equipmentType", "19");//	设备类型，扬尘设备：19
            sendObject.set("orgCode", "1007057");
            sendObject.set("orgName", "甬金衢上高速3标");
            List childList = new ArrayList();
            Map wdMap = new HashMap();

            wdMap.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            wdMap.put("code","a01001");//	编码（字典值）
            wdMap.put("rtd",devicehistory.getTemperature());//	实时监测数据
            wdMap.put("flag","N");//	实时监测数据标记
            wdMap.put("createDate",format);//	创建时间
            wdMap.put("receivedDate",format);//	接收日期2022-04-07
            wdMap.put("updateTime",format);//	更新时间
            childList.add(wdMap);

            Map sdMap = new HashMap();
            sdMap.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            sdMap.put("code","a01002");//	编码（字典值）
            sdMap.put("rtd",devicehistory.getHumidity());//	实时监测数据
            sdMap.put("flag","N");//	实时监测数据标记
            sdMap.put("createDate",format);//	创建时间
            sdMap.put("receivedDate",format);//	接收日期2022-04-07
            sdMap.put("updateTime",format);//	更新时间
            childList.add(sdMap);

            Map fssdMap = new HashMap();
            fssdMap.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            fssdMap.put("code","a01007");//	编码（字典值）
            fssdMap.put("rtd",devicehistory.getWindspeed());//	实时监测数据
            fssdMap.put("flag","N");//	实时监测数据标记
            fssdMap.put("createDate",format);//	创建时间
            fssdMap.put("receivedDate",format);//	接收日期2022-04-07
            fssdMap.put("updateTime",format);//	更新时间
            childList.add(fssdMap);

            Map fxsdMap = new HashMap();
            fxsdMap.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            fxsdMap.put("code","a01008");//	编码（字典值）
            fxsdMap.put("rtd",devicehistory.getWinddirection());//	实时监测数据
            fxsdMap.put("flag","N");//	实时监测数据标记
            fxsdMap.put("createDate",format);//	创建时间
            fxsdMap.put("receivedDate",format);//	接收日期2022-04-07
            fxsdMap.put("updateTime",format);//	更新时间
            childList.add(fxsdMap);

            Map pm10sdMap = new HashMap();
            pm10sdMap.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            pm10sdMap.put("code","a34002");//	编码（字典值）
            pm10sdMap.put("rtd",devicehistory.getPm10());//	实时监测数据
            pm10sdMap.put("flag","N");//	实时监测数据标记
            pm10sdMap.put("createDate",format);//	创建时间
            pm10sdMap.put("receivedDate",format);//	接收日期2022-04-07
            pm10sdMap.put("updateTime",format);//	更新时间
            childList.add(pm10sdMap);

            Map pm25Map = new HashMap();
            pm25Map.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            pm25Map.put("code","a34004");//	编码（字典值）
            pm25Map.put("rtd",devicehistory.getPm25());//	实时监测数据
            pm25Map.put("flag","N");//	实时监测数据标记
            pm25Map.put("createDate",format);//	创建时间
            pm25Map.put("receivedDate",format);//	接收日期2022-04-07
            pm25Map.put("updateTime",format);//	更新时间
            childList.add(pm25Map);

            Map zsMap = new HashMap();
            zsMap.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            zsMap.put("code","a50001");//	编码（字典值）
            zsMap.put("rtd",devicehistory.getNoise());//	实时监测数据
            zsMap.put("flag","N");//	实时监测数据标记
            zsMap.put("createDate",format);//	创建时间
            zsMap.put("receivedDate",format);//	接收日期2022-04-07
            zsMap.put("updateTime",format);//	更新时间
            childList.add(zsMap);

            Map flMap = new HashMap();
            flMap.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            flMap.put("code","wp");//	编码（字典值）
            flMap.put("rtd",devicehistory.getWind());//	实时监测数据
            flMap.put("flag","N");//	实时监测数据标记
            flMap.put("createDate",format);//	创建时间
            flMap.put("receivedDate",format);//	接收日期2022-04-07
            flMap.put("updateTime",format);//	更新时间
            childList.add(flMap);

            Map tspMap = new HashMap();
            tspMap.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            tspMap.put("code","a40005");//	编码（字典值）
            tspMap.put("rtd",devicehistory.getTsp());//	实时监测数据
            tspMap.put("flag","N");//	实时监测数据标记
            tspMap.put("createDate",format);//	创建时间
            tspMap.put("receivedDatetspMap",format);//	接收日期2022-04-07
            tspMap.put("updateTime",format);//	更新时间
            childList.add(tspMap);

            Map dqyMap = new HashMap();
            dqyMap.put("monitoringId","yjqs" + devicehistory.getId());//	监测表ID，主表ID
            dqyMap.put("code","a40006");//	编码（字典值）
            dqyMap.put("rtd",devicehistory.getAtmpressure());//	实时监测数据
            dqyMap.put("flag","N");//	实时监测数据标记
            dqyMap.put("createDate",format);//	创建时间
            dqyMap.put("receivedDatetspMap",format);//	接收日期2022-04-07
            dqyMap.put("updateTime",format);//	更新时间
            childList.add(dqyMap);

            sendObject.set("childList", childList);//	详细数据

            String result = HttpRequest.post(url)
                    .header("client-code","gaoxun")
                    .body(String.valueOf(sendObject))
                    .timeout(20000)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendObject),selectsysconfigone.getRemark(),result);
            if (result.contains("200")){
                log.info("甬金衢上高速3标环境监测推送成功！");
            }else{
                log.info("甬金衢上高速3标环境监测推送失败！");
            }
            sysConfigService.updateSysConfig(JobUtil.YJQS_HJJC,id);
        }
    }
}
