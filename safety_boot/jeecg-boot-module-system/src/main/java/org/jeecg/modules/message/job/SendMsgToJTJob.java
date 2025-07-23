package org.jeecg.modules.message.job;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.qiyuesuo.sample.api.exception.BizException;
import com.trtm.iot.pushandreturn.entity.Pushandreturn;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.RCJob.RSAUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.ISendMsgHandle;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.handle.enums.SendMsgTypeEnum;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.service.impl.SysBaseApiImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 发送消息任务
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SendMsgToJTJob implements Job {

    @Autowired
    private ISysMessageService sysMessageService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format(" Jeecg-Boot 发送消息任务 SendMsgToJTJob !  时间:" + DateUtils.getTimestamp()));

        List<SysMessage> sysMessages = sysMessageService.SelectListToJT();

        for (SysMessage sysMessage : sysMessages) {
            //内容EsContent 转为json格式
            String content = sysMessage.getEsContent();
            JSONObject contentJson = new JSONObject(content);
            Object sbname = contentJson.get("sbname");
            Object time = contentJson.get("time");
            Object text = contentJson.get("content");
            content = sbname + "：" + time + text + "。";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(new Date());//当前时间
            int i = 0;
            try {
                i = DateUtils.differMinutes(String.valueOf(time), format);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (i < 30) {
                JSONObject jsonObject = new JSONObject();
                //手机号
                String number = sysMessage.getEsReceiver();

                //创建人
                String createPersonName = sysMessage.getCreateBy();
                //获取十位时间戳timestamp
                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                //加密发送的信息
                String fromWhere = "重点工艺和数字试验";
                String siYao = "ef84hcv129dfc";
                jsonObject.put("fromWhere", fromWhere);
                jsonObject.put("timestamp", timestamp);
                jsonObject.put("createPersonName", createPersonName);
                jsonObject.put("content", content);

//                报警层级设置:超30%到张栋15906858689，超40%到杨海鹏15314598196
                int type = 0;
                if (text != null) {
                    type = panduan(text);
                }
                if (type == 1) {
                    number = "18222169180";
                }
                if (type == 2) {
                    number = "18222169180";
                }

                String token = getToken();
                System.out.println("开始推送--------------------------------");
                String[] phoneNumber = number.split(",");
                for (String p : phoneNumber) {
                    if (p==null||p.equals("")){
                        continue;
                    }
                    jsonObject.put("number", p);
                    String data = String.format("%s%s%s%s%s%s", createPersonName, p, content, fromWhere, siYao, timestamp);
                    String encryptData = "";
                    try {
                        encryptData = MD5Util.encryptMD5(data);
                    } catch (Exception e) {
                        throw new BizException("加密失败");
                    }
                    jsonObject.put("encryptData", encryptData);

                    String back1 = HttpRequest.post("https://yggc.cncico.com:1080/api/basic/smss/publicSendSms")
                            .header("Authorization", String.format("%s %s", "Bearer", token))
                            .body(String.valueOf(jsonObject))
                            .execute()
                            .body();
                    pushandreturnService.saveData(0, String.valueOf(jsonObject), "预警消息推送集团", back1);

                    if (back1.contains("操作成功") && !back1.contains("发送失败")) {
                        Integer sendNum = sysMessage.getEsSendNum();
                        sysMessage.setEsSendNum(++sendNum);
                        sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
                    } else {
                        sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
                    }
                }
                System.out.println("推送结束--------------------------------");
            } else {
                sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
            }
            // 发送结果回写到数据库
            sysMessageService.updateById(sysMessage);
        }

    }

    private int panduan(Object text) {
        // 将 text 转换为字符串
        String textStr = text.toString();
        // 使用正则表达式匹配所有百分比数值
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+\\.?\\d*)%");
        java.util.regex.Matcher matcher = pattern.matcher(textStr);
        while (matcher.find()) {
            // 提取匹配到的百分比数值
            String percentageStr = matcher.group(1);
            try {
                // 将百分比数值转换为 double 类型
                double percentage = Double.parseDouble(percentageStr);
                if (percentage >= 30&&percentage<40) {
                   return 1;
                }
                if (percentage >= 40) {
                    return 2;
                }
            } catch (NumberFormatException e) {
                log.error("百分比数值转换失败: {}", percentageStr, e);
            }
        }
        return 0;
    }

    String getToken() {

        String token = null;
        String clientSecret = RSAUtil.queryById22("KJXOVlay6Bw0SDgp");
        String back1 = HttpRequest.post("https://yggc.cncico.com:1080/api/login/appToken")
                .header("clientid", "app-ext-iot")
                .header("clientSecret", clientSecret)
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back1);
        String code1 = (String) jsonObject1.get("code");
        if (code1.equals("00000")) {
            JSONObject model = (JSONObject) jsonObject1.get("model");
            token = (String) model.get("access_token");
        }
        return token;
    }
}
