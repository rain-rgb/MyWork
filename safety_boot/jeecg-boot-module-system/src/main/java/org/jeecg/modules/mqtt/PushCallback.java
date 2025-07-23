package org.jeecg.modules.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.aiwarnmsg.entity.AiWarnMsg;
import com.trtm.iot.aiwarnmsg.service.IAiWarnMsgService;
import com.trtm.iot.aiwarnmsgcfg.entity.AiWarnMsgCfg;
import com.trtm.iot.aiwarnmsgcfg.service.IAiWarnMsgCfgService;
import com.trtm.iot.aiwarnmsgs.service.IAiWarnMsgsService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PushCallback implements MqttCallback {


    private MqttPushClient client;
    private MqttConfiguration mqttConfiguration;


    public PushCallback(MqttPushClient client, MqttConfiguration mqttConfiguration) {
        this.client = client;
        this.mqttConfiguration = mqttConfiguration;
    }

    @Override
    public void connectionLost(Throwable cause) {
        /** 连接丢失后，一般在这里面进行重连 **/
        if (client != null) {
            while (true) {
                try {
                    log.info("==============》》》[MQTT] 连接断开，5S之后尝试重连...");
                    Thread.sleep(5000);
                    MqttPushClient mqttPushClient = new MqttPushClient();
                    mqttPushClient.connect(mqttConfiguration);
                    if (MqttPushClient.getClient().isConnected()) {
                        log.info("=============>>重连成功");
                    }
                    break;
                } catch (Exception e) {
                    log.error("=============>>>[MQTT] 连接断开，重连失败！<<=============");
                    continue;
                }
            }
        }
        log.info(cause.getMessage());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //publish后会执行到这里
        log.info("publish后会执行到这里");
        log.info("pushComplete==============>>>" + token.isComplete());
    }


    /**
     * 监听对应的主题消息
     *
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        String Payload = new String(message.getPayload());

        log.info("============》》接收消息主题 : " + topic);
        log.info("============》》接收消息Qos : " + message.getQos());
        log.info("============》》接收消息内容 : " + Payload);
        log.info("============》》接收ID : " + message.getId());
        log.info("接收数据结束 下面可以执行数据处理操作");


        //将json转map,方便读取数据
        JSONObject json = JSONObject.parseObject(Payload);
        switch (json.get("MsgType").toString()) {
            case "Alarm"://实时报警
                Map<String, Object> MapJson = json.getInnerMap();
                Map<String, Object> data = (Map<String, Object>) MapJson.get("Data");
                String alarmTime1 = data.get("AlarmTime1").toString();//报警时间

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse = formatter.parse(alarmTime1);
                String ts = String.valueOf(parse.getTime());

                String url = "http://bova.createiot.com/";
                url = url + data.get("FileName").toString();//图片或视频文件路径
                String AlarmJson = data.get("AlarmJson").toString();
                Map<String, Object> alarmJson = JSONObject.parseObject(AlarmJson, new TypeReference<Map<String, Object>>() {
                });
                String Parameter = alarmJson.get("Parameter").toString();
                Map<String, Object> parameter = JSONObject.parseObject(Parameter, new TypeReference<Map<String, Object>>() {
                });
                String alias = parameter.get("alias").toString();//报警信息
                String label = parameter.get("label").toString();
                AiWarnMsg aiWarnMsg = new AiWarnMsg();
                if (label.equals("clothes")) {
                    aiWarnMsg.setAlgtype("2");
                } else if (label.equals("head")) {
                    aiWarnMsg.setAlgtype("1");
                }
                aiWarnMsg.setPicurls(url);

                aiWarnMsg.setWarntime(ts);
                String deviceNo = data.get("DeviceNo").toString();
//                if (deviceNo.equals("202212010008")) {
//                    deviceNo = "A05A05A01A01A01A01A01_MR_10451";
//                }
                IAiWarnMsgCfgService aiWarnMsgCfgService = SpringUtil.getBean(IAiWarnMsgCfgService.class);
                IAiWarnMsgService aiWarnMsgService = SpringUtil.getBean(IAiWarnMsgService.class);
                QueryWrapper<AiWarnMsgCfg> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeiid", deviceNo);
                AiWarnMsgCfg aiWarnMsgCfg = aiWarnMsgCfgService.getOne(queryWrapper);
                if (aiWarnMsgCfg != null) {
                    aiWarnMsg.setCid(aiWarnMsgCfg.getCid());
                    aiWarnMsg.setCname(aiWarnMsgCfg.getLocal());
                    aiWarnMsg.setWarnmsg(aiWarnMsgCfg.getLocal() + "发现有人" + alias);
                } else {
                    System.out.println("未找到摄像头配置");
                    break;
                }
                QueryWrapper<AiWarnMsg> aiWarnMsgQueryWrapper = new QueryWrapper<>();
                aiWarnMsgQueryWrapper.eq("cid", aiWarnMsg.getCid());
                aiWarnMsgQueryWrapper.eq("cname", aiWarnMsgCfg.getLocal());
                aiWarnMsgQueryWrapper.eq("warntime", ts);
                aiWarnMsgQueryWrapper.eq("algType", aiWarnMsg.getAlgtype());
                aiWarnMsgQueryWrapper.eq("picurls", url);
                List<AiWarnMsg> list = aiWarnMsgService.list(aiWarnMsgQueryWrapper);
                if (list == null || list.size() == 0) {
                    boolean save = aiWarnMsgService.save(aiWarnMsg);
                    if (save) {
                        System.out.println("添加成功" + aiWarnMsg.toString());
                    } else {
                        System.out.println("添加失败" + aiWarnMsg.toString());
                    }
                }

                break;


            case "HTTP_onEvent"://设备事件
                // do something
                break;
            default:
                System.out.println("数据类型错误");
        }


    }
}
