package org.jeecg.modules.mqtt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
public class MqttConfiguration {


    private String host = "tcp://bova.createiot.com:1883";

    private String clientId;

    private String username = "hangzhougaoxun";

    private String password = "797919";

    private int timeout = 3000;

    private int KeepAlive = 30;

    private String topics ="Follow/User/hangzhougaoxun";

    private int qos =1;

}
