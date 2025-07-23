package org.jeecg.modules.job.binhuaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * \* @author: zml
 * \* Date: 2022/08/17
 * \* Time: 13:41
 * \* Description:
 * \
 */
@Component
@Slf4j
public class binhuaiUtil {

    @Autowired


    private static org.jeecg.modules.job.binhuaijob.binhuaiUtil binhuaiUtil;

    @PostConstruct
    public void init() {
        binhuaiUtil = this;
    }

    public synchronized static JSONArray getdate(String shebeino) {
        JSONArray jsonObject = new JSONArray();
        String url = "https://wlwjc.e-jt.cn/zhjc_pc/bios/iot/task/data?deviceNo="+shebeino;
        String body = HttpRequest.get(url)
                .header("Content-Type", "application/json")
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer codes = (Integer) jsonObject1.get("code");
        if (codes==0){
            jsonObject = jsonObject1.getJSONArray("data");
            if (jsonObject.size()>0) {
                return jsonObject;
            }else {
                return null;
            }
        }
        return null;
    }
    public static int getCode(JSONObject sendObject, String url) {
        String body = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        return (Integer) jsonObject1.get("code");
    }
}
