package org.jeecg.modules.job.stprojectjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.jeecg.modules.job.jobutil.BysUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * \* @author: Xx
 * \* Date: 2022/4/26
 * \* Time: 11:41
 * \* Description:
 * \
 */
@Component
public class STUtil {

    private static STUtil stUtil;

    @PostConstruct
    public void init() {
        stUtil = this;
    }

    /**
     * 苏台推送标养室数据
     * @param jsonArray
     * @return
     */
    public Integer PostSTBysList(JSONArray jsonArray) {//String Guid,String EquipCode,String Data
        Integer code = 0;
        String post = HttpRequest.post("http://36.140.131.110:8867/api/construction-url/solutionsMarkingRoom/insertBacth")
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

    /**
     * 苏台推送拌合站数据
     * @param jsonArray
     * @return
     */
    public Integer PostSTTBHZList(JSONArray jsonArray) {//String Guid,String EquipCode,String Data
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

    /**
     * 苏台推送压力机数据
     * @param jsonArray
     * @return
     */
    public Integer PostSTYljList(JSONArray jsonArray) {//String Guid,String EquipCode,String Data
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

    /**
     * 苏台推送压力机数据
     * @param jsonArray
     * @return
     */
    public Integer PostSTZHYDList(JSONArray jsonArray) {//String Guid,String EquipCode,String Data
        Integer code = 0;
        String post = HttpRequest.post("http://36.140.131.110:8867/api/construction-url/solutionsPowerUtilization/insertBacth")
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

    /**
     * 苏台推送环境监测数据
     * @param jsonArray
     * @return
     */
    public Integer PostSTHjjcList(JSONArray jsonArray) {//String Guid,String EquipCode,String Data
        Integer code = 0;
        String post = HttpRequest.post("http://36.140.131.110:8867/api/construction-url/solutionsEnvMonitor/insertBacth")
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

    /**
     * 苏台推送龙门吊数据
     * @param jsonArray
     * @return
     */
    public Integer PostSTLMDList(JSONArray jsonArray) {//String Guid,String EquipCode,String Data
        Integer code = 0;
        String post = HttpRequest.post("http://36.140.131.110:8867/api/construction-url/solutionsGantryCrane/insertBacth")
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
