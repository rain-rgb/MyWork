package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCTokenJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCTOKEN);//瑞仓获取TOKEN
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓获取TOKEN定时任务的配置信息" + DateUtils.now()));
            return;
        }
        String clientSecret = RSAUtil.main();
        String back = HttpRequest.post("http://47.96.161.157:1080/api/login/appToken")
                .header("clientid", "app-ext-iot")
                .header("clientSecret", clientSecret)
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(back);
        String code = (String) jsonObject.get("code");
        if (code.equals("00000")) {
            log.info(String.format("瑞仓获取TOKEN成功!"));
            JSONObject model = (JSONObject) jsonObject.get("model");
            String token = (String) model.get("access_token");
            sysConfigService.updateSysConfigToken(JobUtil.RCBHZTS, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCZHANGLA, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCYAJIANG, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCBHZCBCL, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCZLYJXX, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCYCLSL, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCZHYL_HIS, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCZHYL_REAL, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCHNTHT, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCGBTS, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCBYS, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCMJKQ_HIS, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCCLMJ_HIS, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCQTJC_REAL, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCGZTS, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCBHZ_JZL, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCBHZ_JZL_PHB, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCWZTZ, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCQTJC_HIS, token);//根据传过来的唯一值来修改token
            sysConfigService.updateSysConfigToken(JobUtil.RCSBTS, token);//根据传过来的唯一值来修改token
            log.info(String.format("瑞仓推送定时任务TOKEN更新成功!"));
        } else {
            log.info(String.format("瑞仓获取TOKEN成功!"));
        }
    }
}
