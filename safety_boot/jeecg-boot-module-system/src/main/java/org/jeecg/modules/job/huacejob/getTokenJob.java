package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @ClassName getTokenJob：
 * @Description 获取华测token
 * @Author 55314
 * @Date 2023/4/23 10:46
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class getTokenJob implements Job {
    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/token/get.shtml";
    @Autowired
    private IHcTokenService tokenService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        List<HcToken> list = tokenService.list();

        for (HcToken hcToken : list) {
            JSONObject sendDate = new JSONObject();
            //用户名
            String appid = hcToken.getAppid();
            //密码
            String pw = hcToken.getPw();

            sendDate.set("appId",appid);
            sendDate.set("pw",pw);

            String result = HttpRequest.post(url)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            JSONObject jsonObject = new JSONObject(result);

            JSONObject dataObj = jsonObject.getJSONObject("data");
            String token = dataObj.getStr("token");

            hcToken.setToken(token);
            hcToken.setGettime(new Date());

            tokenService.saveOrUpdate(hcToken);
            log.info("获取华测token");
        }
    }
}
