package org.jeecg.modules.job.jiangsu;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trtm.iot.soslist.common.MyWebSocketClient;
import com.trtm.iot.soslist.service.ISosListService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.java_websocket.enums.ReadyState.OPEN;

/**
 * @ClassName yrdwJob
 * @Author alalei
 * @Date 2024/12/18 14:06
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yrdwJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IFrontDeviceRealdataService frontDeviceRealdataService;
    @Autowired
    private ISosListService sosListService;

    @SneakyThrows
    @Transactional
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RYDW_GETDATA);//人员定位数据获取
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到人员定位获取数据定时任务的配置信息" + DateUtils.now()));
            return;
        }

        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.getSosList();
        JsonNode dataNode = this.AnQuanMao();
        if (ObjectUtil.isNotEmpty(dataNode)) {
            // 遍历 "data" 数组中的每个元素
            Iterator<JsonNode> elements = dataNode.elements();
            while (elements.hasNext()) {
                JsonNode deviceNode = elements.next();
                // 获取子节点
                JsonNode userInfo = deviceNode.get("user_info");
                JsonNode locationInfo = deviceNode.get("location_info");
                // 获取值
                String userId = userInfo.get("user_id").asText();
                String deviceId = userInfo.get("device_id").asText();
                String sim = userInfo.get("sim").asText();
                int caLastLoginTime = userInfo.get("ca_last_login_time").asInt();
                String userImg = userInfo.get("user_img").asText();
                String xPoint = locationInfo.get("x_point").asText();
                String yPoint = locationInfo.get("y_point").asText();
                String inUse = locationInfo.get("in_use").asText();
                FrontDeviceRealdata frontDeviceRealdata = new FrontDeviceRealdata();
                frontDeviceRealdata.setDeviceType("Y");
                frontDeviceRealdata.setProjectId(userId);
                frontDeviceRealdata.setShebeiNo(deviceId);
                frontDeviceRealdata.setSim(sim);
                frontDeviceRealdata.setDatatime(new Date());
                frontDeviceRealdata.setEndtime(caLastLoginTime);
                frontDeviceRealdata.setLatitude(Double.valueOf(xPoint));
                frontDeviceRealdata.setLongitude(Double.valueOf(yPoint));
                frontDeviceRealdata.setPositiontype("GPS");
                frontDeviceRealdata.setCardnumber(userImg);
                frontDeviceRealdata.setStatus(Integer.parseInt(inUse));
                frontDeviceRealdataService.saveOrUpdateByProjectId(frontDeviceRealdata);
            }
        }

    }

    private JsonNode AnQuanMao() throws JsonProcessingException {
        Map loginMap = getLoginMap();
        // 创建 JsonParser 实例
        JsonParser parser = new JsonParser();
        // 解析字符串为 JsonObject
        JsonObject loginMessage = parser.parse(loginMap.get("loginMessage").toString()).getAsJsonObject();
        // 获取对象
        JsonObject adminInfo = loginMessage.getAsJsonObject("admin_info");
        // 获取字段
        String adminId = adminInfo.get("admin_id").getAsString();

        // 创建 ObjectMapper 实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 解析 JSON 字符串为 JsonNode 对象
        JsonNode rootNode = objectMapper.readTree(loginMap.get("deviceMessage").toString());
        // 获取 "data" 数组
        JsonNode dataNode = rootNode.get("data");
        System.out.println(dataNode);
        return dataNode;
    }

    public void getSosList() throws JsonProcessingException {
        String pkey = getPkey();
        String token = getToken(pkey);
        Map loginMap = getLoginMap();
        // 创建 JsonParser 实例
        JsonParser parser = new JsonParser();
        // 解析字符串为 JsonObject
        JsonObject loginMessage = parser.parse(loginMap.get("loginMessage").toString()).getAsJsonObject();
        // 获取对象
        JsonObject adminInfo = loginMessage.getAsJsonObject("admin_info");
        // 获取字段
        String adminId = adminInfo.get("admin_id").getAsString();

        // 创建 ObjectMapper 实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 解析 JSON 字符串为 JsonNode 对象
        JsonNode rootNode = objectMapper.readTree(loginMap.get("deviceMessage").toString());
        // 获取 "data" 数组
        JsonNode dataNode = rootNode.get("data");
        if (ObjectUtil.isNotEmpty(dataNode)) {
            // 遍历 "data" 数组中的每个元素
            Iterator<JsonNode> elements = dataNode.elements();
            List<String> list = new ArrayList<>();
            while (elements.hasNext()) {
                JsonNode deviceNode = elements.next();
                // 获取 user_info 子节点
                JsonNode userInfoNode = deviceNode.get("user_info");
                // 获取 user_id 值
                String userId = userInfoNode.get("user_id").asText();
                list.add(userId);
            }
            List<Object> objList = new ArrayList<>();
            for (String userId : list) {
                Map map = new HashMap();
                map.put("admin_id", adminId);
                map.put("user_id", userId);
                map.put("p", 1);
                map.put("sos_type", "");
                map.put("token", token);
                String post = HttpRequest.post("https://caps.runde.pro/api/index.php??ctl=task&act=get_sos_list")
                        .form(map)
                        .execute()
                        .body();
                JSONObject jsonObjects = new JSONObject(post);
                String data1 = jsonObjects.getStr("data");
                ObjectMapper objectMapper1 = new ObjectMapper();
                JsonNode dataObj = objectMapper1.readTree(data1);
                sosListService.saveOrUpdateBySid(dataObj);
                objList.add(dataObj);
            }
        }
    }

    private Map getLoginMap() {
        String loginMessage = null;
        String deviceMessage = null;
        Map map1 = new HashMap<>();
        try {
            //登录
            String pkey = getPkey();
            //获取token
            String token = getToken(pkey);
            URI serverUri = new URI("wss://caps.runde.pro/wss");
            MyWebSocketClient client = new MyWebSocketClient(serverUri);
            client.connect();
            System.out.println(client.getReadyState());
            while (!client.getReadyState().equals(OPEN)) {
                System.out.println("连接中···");
                Thread.sleep(2000);
            }
            String a = "{\n" +
                    "\"act\": \"ma_login\",\n" +
                    "\"user_name\": \"入海水道\",\n" +
                    "\"access_token\": \"" + token + "\"\n" +
                    "}";
            client.sendMessage(a);
            //等待WebSocket服务端响应
            while ((loginMessage = client.getLoginMessage()) == null) {
                System.out.println("等待中...");
                Thread.sleep(1000);
            }
            System.out.println("成功获取管理员登录数据：" + loginMessage);
            map1.put("loginMessage", loginMessage);
            String b = "{\n" +
                    "\"act\": \"ma_get_active_devices\"\n" +
                    "}";
            client.sendMessage(b);
            //等待WebSocket服务端响应
            while ((deviceMessage = client.getDeviceMessage()) == null) {
                System.out.println("等待中...");
                Thread.sleep(1000);
            }
            //打印服务端返回的数据
            System.out.println("成功获取心跳包数据：" + deviceMessage);
            map1.put("deviceMessage", deviceMessage);
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map1;
    }

    private static String getPkey() {
        Map map = new HashMap();
        map.put("user_name", "入海水道");
        map.put("pwd", "Ab@123456");
        String post = HttpRequest.post("https://caps.runde.pro/api/index.php?ctl=tool&act=get_pkey")
                .form(map)
                .execute()
                .body();
        JSONObject jsonObjects = new JSONObject(post);
        String data = jsonObjects.getStr("data");
        return data;
    }

    private static String getToken(String pkey) {
        Map map = new HashMap();
        map.put("user_name", "入海水道");
        map.put("pkey", pkey);
        String post = HttpRequest.post("https://caps.runde.pro/api/index.php?ctl=tool&act=get_token")
                .form(map)
                .execute()
                .body();
        JSONObject jsonObjects = new JSONObject(post);
        String token = jsonObjects.getStr("token");
        return token;
    }


}
