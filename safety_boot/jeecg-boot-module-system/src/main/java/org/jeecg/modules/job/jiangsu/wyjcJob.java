package org.jeecg.modules.job.jiangsu;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.xkcoding.http.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @ClassName wycdJob
 * @Author alalei
 * @Date 2024/12/25 14:06
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class wyjcJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;

    private static final String url = "www.iot-monitor.cn:8080";
    private static final String clientId = "SOyt-4UfSNGL06EDuDYZdw";
    private static final String clientKey = "vzXNqVr0Q7ewLwa5fgcnWQ";

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.WYCD_GETDATA);//围堰测点数据获取
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到围堰测点获取数据定时任务的配置信息" + DateUtils.now()));
            return;
        }

        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //登录获取token
        String token = this.getToken();
        if (StringUtil.isNotEmpty(token)) {
            //获取设备列表信息
            List<Map> deviceList = this.getDeviceList(token);
            if (ObjectUtil.isNotEmpty(deviceList)) {
                for (Map map : deviceList) {
                    String deviceId = map.get("deviceId").toString();
                    //根据设备获取监测索引
                    List<Map> listMonitors = this.getListMonitors(token, deviceId);
                    System.out.println(listMonitors);
                    List<Map> listMonitorCode = this.getListMonitorCode(token);
                    System.out.println(listMonitorCode);
                };
            }
        }

    }

    private String getToken() {
        String result = HttpRequest.post(url + "/iot/oauth/token")
                .header("clientId", clientId)
                .header("clientKey", clientKey)
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if (code == 200) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // 将 JSON 字符串转换为 JsonNode
                JsonNode rootNode = objectMapper.readTree(String.valueOf(jsonObject));
                JsonNode tokenNode = rootNode.path("data").path("access_token");
                return tokenNode.asText();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private List<Map> getDeviceList(String token) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.set("limit", 100);
        jsonObj.set("offset", 0);
        jsonObj.set("page", 1);
        JSONObject search = new JSONObject();
        jsonObj.set("search", search);
        String jsonString = jsonObj.toString();
        String result = HttpRequest.post(url + "/iot/api/device/list")
                .header("access_token", token)
                .body(jsonString)
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if (code == 200) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // 将 JSON 字符串转换为 JsonNode
                JsonNode rootNode = objectMapper.readTree(String.valueOf(jsonObject));
                JsonNode listNode = rootNode.path("data").path("list");
                if (ObjectUtil.isNotEmpty(listNode)) {
                    Iterator<JsonNode> elements = listNode.elements();
                    List<Map> mapList = new ArrayList<>();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        Map map = new HashMap<>();
                        map.put("deviceId", element.get("deviceId").asText());
                        map.put("deviceKey", element.get("deviceKey").asText());
                        mapList.add(map);
                    }
                    return mapList;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private List<Map> getListMonitors(String token, String deviceId) {
        String result = HttpRequest.post(url + "/iot/api/device/list/monitors?deviceId="+deviceId)
                .header("access_token", token)
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if (code == 200) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // 将 JSON 字符串转换为 JsonNode
                JsonNode rootNode = objectMapper.readTree(jsonObject.toString());
                JsonNode dataNode = rootNode.path("data");
                if (dataNode.isArray() && dataNode.size() > 0) {
                    Iterator<JsonNode> elements = dataNode.elements();
                    List<Map> mapList = new ArrayList<>();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        Map map = objectMapper.convertValue(element, Map.class);
                        mapList.add(map);
                    }
                    return mapList;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private List<Map> getListMonitorCode(String token) {
        String result = HttpRequest.post(url + "/iot/api/monitor/code")
                .header("access_token", token)
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if (code == 200) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // 将 JSON 字符串转换为 JsonNode
                JsonNode rootNode = objectMapper.readTree(jsonObject.toString());
                JsonNode dataNode = rootNode.path("data");
                if (dataNode.isArray() && dataNode.size() > 0) {
                    Iterator<JsonNode> elements = dataNode.elements();
                    List<Map> mapList = new ArrayList<>();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        Map map = objectMapper.convertValue(element, Map.class);
                        mapList.add(map);
                    }
                    return mapList;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


}
