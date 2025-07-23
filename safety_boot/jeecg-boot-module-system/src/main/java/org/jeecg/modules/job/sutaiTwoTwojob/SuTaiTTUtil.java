package org.jeecg.modules.job.sutaiTwoTwojob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Synchronization;

@Component
public class SuTaiTTUtil {
    private static SuTaiTTUtil sTThiUtil;

    @PostConstruct
    public void init() {
        sTThiUtil = this;
    }
    /**
     * 苏台推送拌合站数据
     * @param jsonArray
     * @return
     */
    public synchronized Integer  PostSTTHIBHZList(JSONArray jsonArray) {
        Integer code = 0;
        String post = HttpRequest.post("http://36.140.131.110:8867/api/construction-url/solutionsConcreteStand/insertBacth")
                .header("Content-Type", "application/json")
                .body(String.valueOf(jsonArray))
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        Boolean success = (Boolean) jsonObject.get("data");
        if (success) {
            code = 200;
        } else {
            code = 400;
        }
        return code;
    }

    public Integer PostSTSyjList(JSONArray jsonArray) {
        Integer code = 0;
        String post = HttpRequest.post("http://36.140.131.110:8867/api/construction-url/solutionsPress/insertBacth")
                .header("Content-Type", "application/json")
                .body(String.valueOf(jsonArray))
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        Boolean success = (Boolean) jsonObject.get("data");
        if (success) {
            code = 200;
        } else {
            code = 400;
        }
        return code;
    }
}
