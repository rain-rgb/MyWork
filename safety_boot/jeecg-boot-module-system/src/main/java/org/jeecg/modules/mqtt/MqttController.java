package org.jeecg.modules.mqtt;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;


@Controller
@Slf4j
@ResponseBody
@RequestMapping("/mqtt")
//@Component
public class MqttController {


    //发送逻辑
    @Autowired
    private MqttSender mqttSender;
    //订阅逻辑
    @Autowired
    private MqttPushClient mqttPushClient;
    @Autowired
    private MqttConfiguration mqttConfiguration;

//    @Bean
    @RequestMapping("/subscriptionmqtt")
    public String subscriptionmqtt(){
        MqttPushClient mqttPushClient = new MqttPushClient();
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        mqttConfiguration.setClientId(uuid);
        mqttPushClient.connect(mqttConfiguration);
        String[] topics = {mqttConfiguration.getTopics()};
        int[] qos = {mqttConfiguration.getQos()};
        mqttPushClient.subscribe(topics, qos);
        mqttPushClient.setCallback(new PushCallback(mqttPushClient,mqttConfiguration));
        return "订阅主题";
    }

    /**
     * *
     * 注销
     */
    @RequestMapping("/destroy")
    public void destroy() {
        MqttClient client = MqttPushClient.getClient();
        String[] topics = {mqttConfiguration.getTopics()};
        int[] qos = {mqttConfiguration.getQos()};
        if (client == null) {
            return;
        }

        try {
            if (topics != null && topics.length > 0) {
                client.unsubscribe(topics);
            }

            client.disconnect();
            client.setCallback(null);
            client = null;
            topics = null;
            log.info("---------注销client---------");
        } catch (MqttException ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
    }
//    @RequestMapping("/sendmqtt")
//    public String sendmqtt() {
//        String TOPIC1 = mqttConfiguration.getTopics();
//        String JSON = "{\n" +
//                "\"MsgType\":\"Alarm\",\n" +
//                "\"DeviceNo\":\"202212010008\",\n" +
//                "\"Data\":{\"DeviceNo\":\"202212010008\",\"DeviceName\":\"202212010008\",\"AlarmType\":102,\"Longitude\":0,\"Latitude\":0,\"Longitude1\":0,\"Latitude1\":0,\"Longitude2\":0,\"Latitude2\":0,\"Speed\":0,\"Direction\":0,\"Mode\":\"\",\"AlarmTime\":\"2023-02-24T10:45:01.766364728Z\",\"AlarmTime1\":\"2023-02-24 10:45:01\",\"GpsTime\":\"2023-02-24T10:45:01.766362869Z\",\"GpsTime1\":\"2023-02-24 10:45:01\",\"AlarmAddress\":\"\",\"AlertInfo\":\"\",\"AlarmJson\":\"{\\\"Tag\\\":\\\"\\\",\\\"Parameter\\\":\\\"{\\\\\\\"id\\\\\\\":2,\\\\\\\"label\\\\\\\":\\\\\\\"clothes\\\\\\\",\\\\\\\"alias\\\\\\\":\\\\\\\"未穿反光衣\\\\\\\",\\\\\\\"event\\\\\\\":0,\\\\\\\"time\\\\\\\":0,\\\\\\\"type\\\\\\\":0,\\\\\\\"count\\\\\\\":1,\\\\\\\"threshold\\\\\\\":0}\\\"}\",\"OfflineTimeout\":0,\"FileName\":\"file/Media/20230224/202212010008_c333140fd2b3763e_20230224024506.jpg\"}\n" +
//                "}";
//        log.info("    本机主题:" + TOPIC1 + " 发送数据为:" + JSONObject.toJSONString(JSON));
//        mqttSender.send(TOPIC1, JSON);
//        log.info("     发送结束");
//        return "发送结束";
//    }
}
