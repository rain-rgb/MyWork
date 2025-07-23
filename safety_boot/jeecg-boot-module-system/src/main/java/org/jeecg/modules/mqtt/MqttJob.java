package org.jeecg.modules.mqtt;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @author lis1
 * @date 2023/6/7
 * @time 8:43
 */
@Slf4j
public class MqttJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private MqttPushClient mqttPushClient;
    @Autowired
    private MqttConfiguration mqttConfiguration;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.MQTT_ON_OFF);//MQTT开关
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到MQTT开关定时任务的配置信息" + DateUtils.now()));
            return;
        }
        try{
            MqttController mqttController = new MqttController();
            Integer isworking = selectsysconfigone.getIsworking();
            int ResultWorking = 0;
            if (isworking == 0) {
                MqttPushClient mqttPushClient = new MqttPushClient();
                String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
                mqttConfiguration.setClientId(uuid);
                mqttPushClient.connect(mqttConfiguration);
                String[] topics = {mqttConfiguration.getTopics()};
                int[] qos = {mqttConfiguration.getQos()};
                mqttPushClient.subscribe(topics, qos);
                mqttPushClient.setCallback(new PushCallback(mqttPushClient,mqttConfiguration));
                log.info(String.format("MQTT服务已开启" + DateUtils.now()));
                ResultWorking = 1;
            }else if(isworking == 1) {
                MqttClient client = MqttPushClient.getClient();
                String[] topics = {mqttConfiguration.getTopics()};
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
                log.info(String.format("MQTT服务已关闭" + DateUtils.now()));
            }else{
                throw new JeecgBootException("MQTT未正常关闭或启动");
            }
            sysConfigService.updateSysConfigs(JobUtil.MQTT_ON_OFF, 0, ResultWorking);//根据传过来的唯一值来修改当前判断到的数据id
        }catch (Exception e){
            throw new JeecgBootException("MQTT未正常关闭或启动");
        }


    }
}
