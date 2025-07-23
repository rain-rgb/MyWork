package org.jeecg.modules.job.smsJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.RCJob.RSAUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 集团短信定时任务类，用于将预警消息推送给集团
 *
 * @author [你的名字]
 * @version 1.0
 * @since 2025-04-21
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class smsJob implements Job {

    @Autowired
    private ISysMessageService sysMessageService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String cachedToken;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<SysMessage> sysMessages = sysMessageService.SelectListToJT();

        pushandreturnService.saveData(0, "预警消息推送开始！", "预警消息推送集团", "");
        if (cachedToken == null) {
            cachedToken = getToken();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());

        for (SysMessage sysMessage : sysMessages) {
            try {
                // 处理消息内容
                String content = processMessageContent(sysMessage, sdf);

                // 计算时间差
                int timeDifference = calculateTimeDifference(sysMessage, currentTime);

                pushandreturnService.saveData(0, "推送开始！时间相差：" + timeDifference, "预警消息推送集团", "");
                if (timeDifference < 60) {
                    sendMessages(sysMessage, content, timeDifference);
                } else {
                    sysMessage.setEsSendStatus(SendMsgStatusEnum.TIME_OUT.getCode());
                }
            } catch (Exception e) {
                log.error("处理消息时出错: {}", sysMessage.getId(), e);
                sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
            }
            // 发送结果回写到数据库
            sysMessageService.updateById(sysMessage);
        }
    }

    private String processMessageContent(SysMessage sysMessage, SimpleDateFormat sdf) {
        String content = sysMessage.getEsContent();
        JSONObject contentJson = new JSONObject(content);
        Object sbname = contentJson.get("sbname");
        Object time = contentJson.get("time");
        Object text = contentJson.get("content");

        StringBuilder contentBuilder = new StringBuilder();
        if (sbname != null) {
            contentBuilder.append(sbname).append("：");
        }
        if (time != null) {
            contentBuilder.append(time);
        }
        if (text != null) {
            contentBuilder.append(text);
        }
        contentBuilder.append("。");
        return contentBuilder.toString();
    }

    private int calculateTimeDifference(SysMessage sysMessage, String currentTime) {
        Object time = new JSONObject(sysMessage.getEsContent()).get("time");
        int timeDifference = 0;
        if (time != null) {
            try {
                timeDifference = DateUtils.differMinutes(String.valueOf(time), currentTime);
            } catch (Exception e) {
                log.error("计算时间差时出错: {}", sysMessage.getId(), e);
            }
        }
        return timeDifference;
    }

    private void sendMessages(SysMessage sysMessage, String content, int timeDifference) {
        JSONObject jsonObject = new JSONObject();
        String number = sysMessage.getEsReceiver();
        String createPersonName = sysMessage.getCreateBy();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String fromWhere = "重点工艺和数字试验";
        String siYao = "ef84hcv129dfc";

        jsonObject.set("fromWhere", fromWhere);
        jsonObject.set("timestamp", timestamp);
        jsonObject.set("createPersonName", createPersonName);
        jsonObject.set("content", content);

        Object text = new JSONObject(sysMessage.getEsContent()).get("content");
        int type = 0;
        if (text != null) {
            type = determineAlertLevel(text);
        }
        if (type == 1) {
            number = "15906858689";
        } else if (type == 2) {
            number = "15314598196";
        }

        String[] phoneNumber = number.split(",");
        for (String p : phoneNumber) {
            if (p == null || p.isEmpty()) {
                continue;
            }
            jsonObject.set("number", p);
            String data = String.format("%s%s%s%s%s%s", createPersonName, p, content, fromWhere, siYao, timestamp);
            String encryptData = "";
            try {
                encryptData = MD5Util.encryptMD5(data);
            } catch (Exception e) {
                log.error("加密数据时出错: {}", sysMessage.getId(), e);
                sysMessage.setEsSendStatus(SendMsgStatusEnum.ENCRYPT_FAIL.getCode());
                pushandreturnService.saveData(0, "加密数据", "预警消息推送集团", "加密失败！");
                continue;
            }
            jsonObject.set("encryptData", encryptData);

            String back1 = sendRequest(jsonObject);
            pushandreturnService.saveData(0, String.valueOf(jsonObject), "预警消息推送集团", back1);

            if (back1.contains("操作成功") && !back1.contains("发送失败")) {
                Integer sendNum = sysMessage.getEsSendNum();
                sysMessage.setEsSendNum(++sendNum);
                sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
            } else {
                sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
            }
        }
    }

    private String sendRequest(JSONObject jsonObject) {
        try (cn.hutool.http.HttpResponse response = HttpRequest.post("https://yggc.cncico.com:1080/api/basic/smss/publicSendSms")
                .header("Authorization", String.format("%s %s", "Bearer", cachedToken))
                .body(String.valueOf(jsonObject))
                .execute()) {
            return response.body();
        }
    }

    private int determineAlertLevel(Object text) {
        String textStr = text.toString();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+\\.?\\d*)%");
        java.util.regex.Matcher matcher = pattern.matcher(textStr);
        int maxLevel = 0;
        while (matcher.find()) {
            String percentageStr = matcher.group(1);
            try {
                double percentage = Double.parseDouble(percentageStr);
                if (percentage >= 40) {
                    maxLevel = 2;
                    break;
                } else if (percentage >= 30) {
                    maxLevel = 1;
                }
            } catch (NumberFormatException e) {
                log.error("百分比数值转换失败: {}", percentageStr, e);
            }
        }
        return maxLevel;
    }

    private String getToken() {
        String token = null;
        String clientSecret = RSAUtil.queryById22("KJXOVlay6Bw0SDgp");
        try (cn.hutool.http.HttpResponse response = HttpRequest.post("https://yggc.cncico.com:1080/api/login/appToken")
                .header("clientid", "app-ext-iot")
                .header("clientSecret", clientSecret)
                .execute()) {
            String back1 = response.body();
            JSONObject jsonObject1 = new JSONObject(back1);
            String code1 = (String) jsonObject1.get("code");
            if ("00000".equals(code1)) {
                JSONObject model = (JSONObject) jsonObject1.get("model");
                token = (String) model.get("access_token");
            }
        }
        return token;
    }
}