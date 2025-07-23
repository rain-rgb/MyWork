package org.jeecg.modules.message.handle.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.message.handle.ISendMsgHandle;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class SmsSendMsgHandle implements ISendMsgHandle {
	@SneakyThrows
	@Override
	public void SendMsg(String es_receiver, String es_title, String es_content) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = JSONObject.parseObject(es_content);
		String[] split = es_receiver.split(",");
		for (String s : split) {
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format = sdf.format(new Date());//当前时间
			String time = String.valueOf(jsonObject.get("time"));
			int i = DateUtils.differMinutes(time, format);
			if(i<30){
				log.info("发短信手机号,内容,模板"+s,jsonObject, DySmsEnum.SMS_CB_CODE);
				DySmsHelper.sendSms(s,jsonObject, DySmsEnum.SMS_CB_CODE);
			}else{
				log.info("发短信手机号,内容,模板,短信超出30分钟不发送"+s,jsonObject, DySmsEnum.SMS_CB_CODE);
			}


		}
	}

}
