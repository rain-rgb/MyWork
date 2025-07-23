package org.jeecg.modules.job.frontDeviceDatajob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.trtm.iot.clgl.entity.FrontDeviceHistorydata;
import com.trtm.iot.clgl.service.IClxxRealdataService;
import com.trtm.iot.clgl.service.IFrontDeviceHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName FrontDeviceDataJob：获取中铁十五局合新铁路车辆信息数据定时任务
 * @Description TODO
 * @Author zml
 * @Date 2022/05/12 13:27
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class FrontDeviceDataJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IFrontDeviceRealdataService frontDeviceRealdataService;
    @Autowired
    private IFrontDeviceHistorydataService frontDeviceHistorydataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.FRONTDEVICEDATA_ZHONGTIESHIWUJU);//获取中铁十五局合新铁路车辆信息数据定时任务
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到获取中铁十五局合新铁路车辆信息数据定时任务的配置信息" + DateUtils.now());
            return;
        }
        String token = "65a0bc07-cc43-11ec-a83f-00163e23d620";
        String url = "https://app-v2.bboxiot.com/company-web/query/machine";
        String body = HttpRequest.get(url)
                .header("Content-Type", "application/json")
                .header("access-token", token)
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(body);
        String codes = (String) jsonObject1.get("message");
        if ("SUCCESS".equals(codes)) {
            JSONArray data = (JSONArray) jsonObject1.get("data");
            if (data.size() > 0) {
                for (Object object : data) {
                    JSONObject jsonObject = JSONUtil.parseObj(object);
                    FrontDeviceRealdata frontDeviceRealdata = new FrontDeviceRealdata();
                    FrontDeviceHistorydata frontDeviceHistorydata = new FrontDeviceHistorydata();
                    String shebeino = (String) jsonObject.get("assetNumber");
                    String deviceType = (String) jsonObject.get("machineCategoryName");
                    FrontDeviceRealdata queryone = frontDeviceRealdataService.queryone(shebeino);
                    frontDeviceRealdata.setShebeiNo(shebeino);
                    frontDeviceHistorydata.setShebeiNo(shebeino);
                    if ("装载机".equals(deviceType)) {
                        frontDeviceRealdata.setDeviceType("E");
                        frontDeviceHistorydata.setDeviceType("E");
                    }
                    if ("混凝土搅拌车".equals(deviceType)) {
                        frontDeviceRealdata.setDeviceType("F");
                        frontDeviceHistorydata.setDeviceType("F");
                    }
                    frontDeviceRealdata.setSpeed((double) 0);
                    frontDeviceHistorydata.setSpeed(BigDecimal.valueOf(0));
                    String lat = String.valueOf(jsonObject.get("gcjLat"));
                    String lng = String.valueOf(jsonObject.get("gcjLng"));
                    double lat1 = 0.0;
                    double lng1 = 0.0;
                    if (!"null".equals(lat)) {
                        lat1 = Double.parseDouble(String.format("%.7f", Double.parseDouble(lat)));
                        frontDeviceRealdata.setLatitude(lat1);
                        frontDeviceHistorydata.setLatitude(BigDecimal.valueOf(lat1));
                    }
                    if (!"null".equals(lng)) {
                        lng1 = Double.parseDouble(String.format("%.7f", Double.parseDouble(lng)));
                        frontDeviceRealdata.setLongitude(lng1);
                        frontDeviceHistorydata.setLongitude(BigDecimal.valueOf(lng1));
                    }
                    frontDeviceHistorydata.setDatatime(new Date());
                    if ((boolean) jsonObject.get("online")) {
                        frontDeviceRealdata.setShebeistatus(1);
                    } else {
                        frontDeviceRealdata.setShebeistatus(0);
                    }
                    frontDeviceRealdata.setDatatime(new Date());
                    String workingState = String.valueOf(jsonObject.get("workingState"));
                    if ("STATIC".equals(workingState)){
                        frontDeviceRealdata.setIfid("静止");
                    }
                    if ("IDLING".equals(workingState)){
                        frontDeviceRealdata.setIfid("怠速");
                    }
                    if ("IDLING".equals(workingState)){
                        frontDeviceRealdata.setIfid("怠速");
                    }
                    if ("RUNNING".equals(workingState)){
                        frontDeviceRealdata.setIfid("工作");
                    }
                    if ("MOVING".equals(workingState)){
                        frontDeviceRealdata.setIfid("行驶");
                    }
                    if ("OTHER".equals(workingState)){
                        frontDeviceRealdata.setIfid("其他");
                    }
                    if (queryone == null) {
                        boolean save = frontDeviceRealdataService.save(frontDeviceRealdata);
                        if (save) {
                            log.info("中铁十五局合新铁路车辆信息实时数据添加成功!!" + shebeino + "设备" + DateUtils.now());
                        } else {
                            log.info("中铁十五局合新铁路车辆信息实时数据添加失败!!" + shebeino + "设备" + DateUtils.now());
                        }
                    } else {
                        frontDeviceRealdata.setId(queryone.getId());
                        boolean update = frontDeviceRealdataService.updateById(frontDeviceRealdata);
                        if (update) {
                            log.info("中铁十五局合新铁路车辆信息实时数据修改成功!!" + shebeino + "设备" + DateUtils.now());
                        } else {
                            log.info("中铁十五局合新铁路车辆信息实时数据修改失败!!" + shebeino + "设备" + DateUtils.now());
                        }
                    }
                    FrontDeviceHistorydata frontDeviceHistorydata1 = frontDeviceHistorydataService.selectnewData(shebeino,lng1,lat1);
                    if (lng1>0&&lat1>0) {
                        if (frontDeviceHistorydata1==null) {
                            boolean b = frontDeviceHistorydataService.save(frontDeviceHistorydata);
                            if (b) {
                                log.info("中铁十五局合新铁路车辆信息轨迹数据添加成功!!" + shebeino + "设备" + DateUtils.now());
                            } else {
                                log.info("中铁十五局合新铁路车辆信息轨迹数据添加失败!!" + shebeino + "设备" + DateUtils.now());
                            }
                        }
                    }else {
                        frontDeviceHistorydata.setId(frontDeviceHistorydata1.getId());
                        boolean update = frontDeviceHistorydataService.updateById(frontDeviceHistorydata);
                        if (update) {
                            log.info("中铁十五局合新铁路车辆信息历史数据修改成功!!" + shebeino + "设备" + DateUtils.now());
                        } else {
                            log.info("中铁十五局合新铁路车辆信息历史数据修改失败!!" + shebeino + "设备" + DateUtils.now());
                        }
                    }
                }
            } else {
                log.info("暂无中铁十五局合新铁路车辆实时数据!");
            }
        } else {
            log.info("获取中铁十五局合新铁路车辆实时数据失败!");
        }
    }
}
