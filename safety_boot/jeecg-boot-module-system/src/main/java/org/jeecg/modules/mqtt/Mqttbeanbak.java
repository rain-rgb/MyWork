package org.jeecg.modules.mqtt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Mqttbeanbak {
    @Autowired
    private MqttConfiguration mqttConfiguration;
    @Bean("mqttPushClient")
    public MqttPushClient getMqttPushClient() {
        MqttPushClient mqttPushClient = new MqttPushClient();
        return mqttPushClient;
    }
}
