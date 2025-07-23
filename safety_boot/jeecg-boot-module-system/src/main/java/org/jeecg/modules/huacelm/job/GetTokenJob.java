package org.jeecg.modules.huacelm.job;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @ClassName GetTokenJob：
 * @Description 获取华测登录token
 * @Author 55314
 * @Date 2023/3/28 9:08
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GetTokenJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JSONObject getToken = JSONUtil.createObj();
        getToken.set("appId", "YDGS");
        getToken.set("pw", "123456789");

        String tokenUrl = "https://dp.huace.cn/digitalPlatform/open/api/token/get.shtml";
        String tokenres = HttpRequest.post(tokenUrl)
                .body(String.valueOf(getToken))
                .execute()
                .body();

        JSONObject jsonObject = JSONUtil.parseObj(tokenres);
        JSONObject jsarray = jsonObject.getJSONObject("data");
        String token = String.valueOf(jsarray.get("token"));
    }
}
