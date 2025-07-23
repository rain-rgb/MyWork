package org.jeecg.modules.message.job;

import java.util.Date;
import java.util.List;

import com.trtm.iot.pushandreturn.entity.Pushandreturn;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.ISendMsgHandle;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.handle.enums.SendMsgTypeEnum;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.service.impl.SysBaseApiImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * 发送消息任务
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SendMsgJob implements Job {

    @Autowired
    private ISysMessageService sysMessageService;
    @Autowired
    private SysBaseApiImpl sysBaseApi;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Value("${server.jobswitch}")
    private String jobswitch;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format(" Jeecg-Boot 发送消息任务 SendMsgJob !  时间:" + DateUtils.getTimestamp()));

        if (!jobswitch.equals("true")){
            log.info(String.format("跳出短信发送定时任务" + DateUtils.now()));
           return;
        }


        // 1.读取消息中心数据，只查询未发送的和发送失败不超过次数的 使用mapper查询防止缓存问题
//		QueryWrapper<SysMessage> queryWrapper = new QueryWrapper<SysMessage>();
//		queryWrapper.eq("es_send_status", SendMsgStatusEnum.WAIT.getCode())
//				.or(i -> i.eq("es_send_status", SendMsgStatusEnum.FAIL.getCode()).lt("es_send_num", 2));
        List<SysMessage> sysMessages = sysMessageService.SelectLists();
        System.out.println(sysMessages);
        // 2.根据不同的类型走不通的发送实现类
        for (SysMessage sysMessage : sysMessages) {
            //短信
            Pushandreturn pushandreturn = new Pushandreturn();
            pushandreturn.setPushdataid(sysMessage.getId());
            pushandreturn.setPushname("短信推送");
            pushandreturn.setPushjson(sysMessage.getEsContent());
            pushandreturn.setPushdate(new Date());
            pushandreturnService.save(pushandreturn);

            ISendMsgHandle sendMsgHandle = null;
            try {
                if (sysMessage.getEsType().equals(SendMsgTypeEnum.EMAIL.getType())) {
                    sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.EMAIL.getImplClass()).newInstance();
                } else if (sysMessage.getEsType().equals(SendMsgTypeEnum.SMS.getType())) {
                    sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.SMS.getImplClass()).newInstance();
                } else if (sysMessage.getEsType().equals(SendMsgTypeEnum.WX.getType())) {
                    sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.WX.getImplClass()).newInstance();
                } else if (sysMessage.getEsType().equals("4")) {//发送系统消息
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setContent(sysMessage.getEsContent());
                    messageDTO.setFromUser(sysMessage.getCreateBy());
                    messageDTO.setToAll(true);
                    messageDTO.setTitle(sysMessage.getEsTitle());
                    messageDTO.setToUser(sysMessage.getEsReceiver());
                    sysBaseApi.sendSysAnnouncement(messageDTO);
                    sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
                    Integer sendNum = sysMessage.getEsSendNum();
                    sysMessage.setEsSendNum(++sendNum);
                    // 发送结果回写到数据库
                    sysMessageService.updateById(sysMessage);
                    continue;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            Integer sendNum = sysMessage.getEsSendNum();
            try {
                String[] split = sysMessage.getEsContent().split(",");
                String substring = split[1].substring(11, split[1].length() - 2);
                if (substring.length() > 34) {
                    String s = split[0] + "," + split[1].substring(0, 11) + substring.substring(0, 28) + "......" + split[1].substring(split[1].length()-1,split[1].length()) + "," + split[2];
                    sysMessage.setEsContent(s);
                }
                sendMsgHandle.SendMsg(sysMessage.getEsReceiver(), sysMessage.getEsTitle(),
                        sysMessage.getEsContent().toString());
                log.info("SendMsgJob:sendSUCCESS------ "+sysMessage.getEsReceiver()+sysMessage.getEsContent());
                // 发送消息成功
                sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
            } catch (Exception e) {
                e.printStackTrace();
                // 发送消息出现异常
                sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
            }
            sysMessage.setEsSendNum(++sendNum);
            // 发送结果回写到数据库
            sysMessageService.updateById(sysMessage);
        }

    }

}
