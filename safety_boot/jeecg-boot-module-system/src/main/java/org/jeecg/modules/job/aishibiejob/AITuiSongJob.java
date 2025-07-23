package org.jeecg.modules.job.aishibiejob;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.aiwarnmsg.entity.AiWarnMsg;
import com.trtm.iot.aiwarnmsg.service.IAiWarnMsgService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.RCJob.RSAUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
public class AITuiSongJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IAiWarnMsgService aiWarnMsgService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.AI_WARN_MSG);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置需要推送试验机数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<AiWarnMsg> aiWarnMsgList = aiWarnMsgService.selectWarnData(curid, shebeilist);
        if (ObjectUtil.isEmpty(aiWarnMsgList)) {
            log.info(String.format("没有配置需要推送的AI检测设备" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (AiWarnMsg aiWarnMsg : aiWarnMsgList) {
            id = aiWarnMsg.getId();
            JSONObject sendJsonObject = new JSONObject();
            sendJsonObject.set("cid", aiWarnMsg.getCid());
            sendJsonObject.set("cname", aiWarnMsg.getCname());
            sendJsonObject.set("warntime", aiWarnMsg.getWarntime());
            sendJsonObject.set("warnmsg", aiWarnMsg.getWarnmsg());
            sendJsonObject.set("algType", aiWarnMsg.getAlgtype());
            sendJsonObject.set("picurls", aiWarnMsg.getPicurls());
            sendJsonObject.set("warnent", aiWarnMsg.getWarnent());
            sendJsonObject.set("enttype", aiWarnMsg.getEnttype());
            sendJsonObject.set("traceId", aiWarnMsg.getTraceId());
            System.out.println(sendJsonObject);

            String token = null;
            String clientSecret = RSAUtil.queryById22("KJXOVlay6Bw0SDgp");
            String back1 = HttpRequest.post("https://yggc.cncico.com:1080/api/login/appToken")
                    .header("Content-Type", "application/json")
                    .header("clientid", "app-ext-iot")
                    .header("clientSecret", clientSecret)
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back1);
            String code1 = (String) jsonObject1.get("code");
            if ("00000".equals(code1)) {
                JSONObject model = (JSONObject) jsonObject1.get("model");
                token = (String) model.get("access_token");
                String post = HttpRequest.post("https://yggc.cncico.com:1080/api/iot/tunnel/warnMsgs")
                        .header("Content-Type", "application/json")
                        .header("Authorization", String.format("%s %s", "Bearer", token))
                        .body(String.valueOf(sendJsonObject))
                        .execute()
                        .body();
                JSONObject jsonObject2 = new JSONObject(post);
                String code2 = (String) jsonObject2.get("code");
                if ("00000".equals(code2)) {
                    log.info(String.format("AI预警信息数据推送成功!" + sendJsonObject));
                    sysConfigService.updateSysConfig(JobUtil.AI_WARN_MSG, id);//根据传过来的唯一值来修改当前判断到的数据id
                } else {
                    log.info(String.format("AI预警信息数据推送失败!" + sendJsonObject));
                }
            }
        }
    }
}
