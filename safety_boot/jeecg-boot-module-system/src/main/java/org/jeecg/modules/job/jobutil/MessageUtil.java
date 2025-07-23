package org.jeecg.modules.job.jobutil;

import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.message.entity.SysMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class MessageUtil {

    private static MessageUtil messageUtil;

    @PostConstruct
    public void init() {
        messageUtil = this;
    }

    public SysMessage setMessage(String status,String title,String type,String phones,String content,String remark){

        SysMessage sysMessage = new SysMessage();
        sysMessage.setEsTitle(title);
        sysMessage.setEsType(type);//短信类型  1短信
        sysMessage.setEsReceiver(phones);//手机号
        sysMessage.setEsContent(content);//短信内容
        sysMessage.setRemark(remark);
        sysMessage.setEsSendNum(0);
        sysMessage.setEsSendStatus(status);//推送状态-1 不需要推送;0未推送
        sysMessage.setCreateTime(new Date());


        return  sysMessage;
    }
}
