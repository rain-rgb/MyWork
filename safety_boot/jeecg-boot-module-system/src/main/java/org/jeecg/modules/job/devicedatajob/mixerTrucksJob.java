package org.jeecg.modules.job.devicedatajob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.device_data.entity.DeviceData;
import com.trtm.iot.device_data.service.IDeviceDataService;
import org.jeecg.common.util.MD5Util;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName mixerTrucksJob
 * @Author
 * @Date 2025/5/6 11:29
 * @Version
 * @Description 搅拌车数据
 */
public class mixerTrucksJob implements Job {
    @Autowired
    private IDeviceDataService DeviceDataService;
    private static final String LOGIN_URL = "https://www.gps51.com/webapi?action=login";
    private static final String POSITION_URL = "https://www.gps51.com/webapi?action=lastposition&token=";
    private static final String DEVICELIST_URL = "https://www.gps51.com/webapi?action=querymonitorlist&token=";
    private String cachedToken;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (cachedToken == null) {
            cachedToken = getToken();
            //获取设备
            String jsonStr = HttpRequest.post(DEVICELIST_URL + cachedToken)
                    .execute()
                    .body();
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray groups = jsonObject.getJSONArray("groups");

            for (int i = 0; i < groups.size(); i++) {
                JSONObject group = groups.getJSONObject(i);
                JSONArray devices = group.getJSONArray("devices");
                for (int j = 0; j < devices.size(); j++) {
                    JSONObject device = devices.getJSONObject(j);
                    String deviceId = device.getStr("deviceid");

                    JSONObject json = new JSONObject();
                    List<String> deviceIdsList = new ArrayList<>();
                    deviceIdsList.add(deviceId);
                    json.put("deviceids", deviceIdsList);

                    LambdaQueryWrapper<DeviceData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(DeviceData::getDeviceid, deviceId)
                            .orderByDesc(DeviceData::getUpdatetime)
                            .last("limit 1");
                    DeviceData deviceMixerTruckOne = DeviceDataService.getOne(lambdaQueryWrapper);
                    if (deviceMixerTruckOne != null) {
                        json.put("lastquerypositiontime", deviceMixerTruckOne.getUpdatetime());
                    } else {
                        json.put("lastquerypositiontime", 0);
                    }

                    String body = HttpRequest.post(POSITION_URL + cachedToken)
                            .body(String.valueOf(json))
                            .execute()
                            .body();

                    JSONObject jsonObject1 = new JSONObject(body);
                    if (jsonObject1.get("status").equals(0)) {
                        JSONObject jsonObjectPOSITION = new JSONObject(jsonObject1);
                        JSONArray records = jsonObjectPOSITION.getJSONArray("records");
                        for (int k = 0; k < records.size(); k++) {
                            JSONObject record = records.getJSONObject(k);
                            DeviceData DeviceData = JSON.parseObject(record.toString(), DeviceData.class);
                            DeviceDataService.save(DeviceData);
                        }
                    }
                }
            }
        }
    }

    private static String getToken() {
        // 构建请求体 JSON
        JSONObject json = new JSONObject();
        json.put("type", "USER");
        json.put("from", "web");
        json.put("username", "演示测试组");
        json.put("password", MD5Util.encrypt32MD5("Ys2024"));
        json.put("browser", "Chrome/104.0.0.0");

        // 构建请求
        String body1 = HttpRequest.post(LOGIN_URL)
                .body(String.valueOf(json))
                .execute()
                .body();

        JSONObject jsonObject = new JSONObject(body1);
        if (jsonObject.get("status").equals(0)) {
            return jsonObject.get("token").toString();
        }
        return null;
    }
}