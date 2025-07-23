package org.jeecg.modules.job.aishibiejob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.aiwarnmsgs.entity.AiWarnMsgs;
import com.trtm.iot.aiwarnmsgs.service.IAiWarnMsgsService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.SignUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName AishibieJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/4/25 9:45
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AishibieJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IAiWarnMsgsService aiWarnMsgsService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.AISB_ZHYD);//AI识别  智慧义东
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东AI识别定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东AI识别的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<AiWarnMsgs> selecthntbhzones = aiWarnMsgsService.getlists1(shebeilist, curid);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无义东AI识别未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        JSONObject mapError = JSONUtil.createObj();
        mapError.set("useId", "1608aaf8b57d46b4a36322208c6d3f90");
        mapError.set("appId", "57278670aad04769a9fd14e43e91a356");
        mapError.set("type", "cu");
        for (AiWarnMsgs aiWarnMsgs : selecthntbhzones) {
            List list = new ArrayList();
            id = aiWarnMsgs.getId();
            StringBuffer send = new StringBuffer();
            send.append("{ID_:\"" + aiWarnMsgs.getId() + "\",");
            send.append("cid:\"" + aiWarnMsgs.getShebeiid() + "\",");
            send.append("cname:\"" + aiWarnMsgs.getCname() + "\",");
            send.append("warntime:\"" + aiWarnMsgs.getWarntime() + "\",");
            send.append("warnmsg:\"" + aiWarnMsgs.getWarnmsg() + "\",");
            send.append("algtype:\"" + aiWarnMsgs.getAlgtype() + "\",");
            send.append("picurls:\"" + aiWarnMsgs.getPicurls() + "\",");
            send.append("warnent:\"" + aiWarnMsgs.getWarnent() + "\",");
            send.append("enttype:\"" + aiWarnMsgs.getEnttype() + "\",");
            send.append("traceid:\"" + aiWarnMsgs.getTraceId() + "\"}");
            list.add(send);
            SecureRandom random = new SecureRandom();
            String s = String.valueOf(random.nextInt());
            String time = String.valueOf(System.currentTimeMillis());
            String signature = SignUtil.signature("aa9d107db67844268a9339a8f75491b8", s, time);
            String encode = null;
            try {
                encode = URLEncoder.encode(list.toString(), "UTF-8");
                String url = "https://fjhmtd.com:15131/glaf/website/ws/execute/api/crud?useId=1608aaf8b57d46b4a36322208c6d3f90&appId=57278670aad04769a9fd14e43e91a356&type=cu&params=" + encode;
                String back = HttpRequest.post(url)
                        .header("x-rio-seq", s)
                        .header("signature", signature)
                        .header("timestamp", time)
                        .timeout(20000)
                        .execute().body();
                JSONObject jsonObject1 = new JSONObject(back);
                Integer codes = Integer.parseInt(jsonObject1.get("statusCode").toString());
                if (codes == 200) {
                    log.info("义东AI识别数据推送成功!" + id + "Json数据" + back);
                } else {
                    log.info("义东AI识别数据推送失败!" + id + "Json数据" + back);
                }
                sysConfigService.updateSysConfig(JobUtil.AISB_ZHYD, id);//根据传过来的唯一值来修改当前判断到的数据id
            } catch (UnsupportedEncodingException e) {
                log.info("义东AI识别数据推送失败!");
                continue;
            }
        }
    }
}
