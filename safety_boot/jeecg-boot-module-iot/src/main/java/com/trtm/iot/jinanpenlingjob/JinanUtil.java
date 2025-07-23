package com.trtm.iot.jinanpenlingjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class JinanUtil {

    @Autowired


    private static JinanUtil jinanUtil;

    @PostConstruct
    public void init() {
        jinanUtil = this;
    }

    public synchronized static JSONArray getdate(String token) {
        JSONArray jsonObject = new JSONArray();
        String url = "https://spray.jstajian.top/prod-api/device/lists";
        String body = HttpRequest.get(url)
                .header("Authorization", token)
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer codes = (Integer) jsonObject1.get("code");
        if (codes==200){
            jsonObject = jsonObject1.getJSONArray("rows");
            if (jsonObject.size()>0) {
                return jsonObject;
            }else {
                return null;
            }
        }
        return null;
    }

    //根据设备ID查询设备下的路线状态
    public synchronized static JSONArray getdatexq(String token,Integer deviceId) {
        JSONArray jsonObject = new JSONArray();
        String url = "https://spray.jstajian.top/prod-api/device/status?deviceId="+deviceId;
        String body = HttpRequest.get(url)
                .header("Authorization", token)
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer codes = (Integer) jsonObject1.get("code");
        JSONObject jsonObject2 = (JSONObject) jsonObject1.get("data");
        if (codes==200){
            jsonObject = jsonObject2.getJSONArray("paths");
            if (jsonObject.size()>0) {
                return jsonObject;
            }else {
                return null;
            }
        }
        return null;
    }

    public synchronized static Integer getdateal(String token,Integer deviceId,Integer path,Integer status) {
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.set("deviceId",deviceId);
        jsonObject3.set("path",path);
        if (status == 1){
            jsonObject3.set("status",2);
        }else {
            jsonObject3.set("status",1);
        }
        JSONArray jsonObject = new JSONArray();
        String url = "https://spray.jstajian.top/prod-api/path/control";
        String body = HttpRequest.put(url)
                .header("Authorization", token)
                .body(String.valueOf(jsonObject3))
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer codes = (Integer) jsonObject1.get("code");
        if (codes==200){
            return codes;
        }
        return null;
    }

    public static String getCode(JSONObject sendObject, String url) {
        String body = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        return (String) jsonObject1.get("token");
    }
}