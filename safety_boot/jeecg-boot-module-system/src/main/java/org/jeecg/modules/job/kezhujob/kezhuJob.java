package org.jeecg.modules.job.kezhujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @ClassName kezhuJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/10/31 14:32
 * @Version 1.0
 **/
public class kezhuJob {

    private static String userId = "1003445";
    private static String userPass = "f315e6c9cd060ed2ab78296f7604b483";
    private static String loginTag = "0";

    public static String login(){
        String url = "http://121.12.120.220:8080/gapp/userlogin.action?userId="+userId+"&userPass="+userPass+"&loginTag="+loginTag;
        String back = HttpRequest.post("url")
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(back);
        cn.hutool.json.JSONObject data = jsonObject.getJSONObject("data");
        String userID = String.valueOf(data.get("userID"));
        return userID;
    }

    public static String getProject(String userID){
        String url = "http://121.12.120.220:8080/gapp/openuserprojquery.action?userId="+userID;
        String back = HttpRequest.post(url)
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(back);
        JSONArray data = jsonObject.getJSONArray("data");
        List<Map<String,Object>> list = (List)data;
        String pactId = String.valueOf(list.get(0).get("pactId"));
        return pactId;
    }

    public static String getProjId(String pactId){
        String url = "http://121.12.120.220:8080/gapp/openuserpactprojquery.action?pactId="+pactId;
        String back = HttpRequest.post(url)
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(back);
        JSONArray data = jsonObject.getJSONArray("data");
        List<Map<String,Object>> list = (List)data;
        String projId = String.valueOf(list.get(0).get("projId"));
        return projId;
    }

    public static void getProjId() {
    }
}
