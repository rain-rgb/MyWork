package org.jeecg.modules.job.fkclJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class zjwzUtil {

    @Autowired
    private static zjwzUtil zjwzUtil;

    @PostConstruct
    public void init() {
        zjwzUtil = this;
    }

    private final static String LoginName = "b24df1c2-2635-40ea-a441-e8e1ed41e371";

    private final static String LoginPassword = "CECDE8142D3DB80BE58038278BF64E75";

    // 获取token
    public synchronized static String gettoken() {
        String url = "http://interface.wazert.com/api/gps/service/user/getToken?appscrect="+LoginPassword+"&appkey="+LoginName;
        String body = HttpRequest.post(url)
                .execute()
                .body();

        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer code = (Integer) jsonObject1.get("code");
        if (code==1){
            String token = (String) jsonObject1.get("data");
            return token;
        }
        return null;
    }

    // 获取设备列表
    public synchronized static JSONArray getshebeilist() {
        String url = "https://jzgj.crfeb.com.cn/api/deviceList?app_key=4F732CFB9020E908190B&app_secret=7E1173B4D41804DFC329";
        String body = HttpRequest.get(url)
                .execute()
                .body();

        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        JSONArray data = (JSONArray) jsonObject1.get("data");
        return data;
    }

    // 获取设备位置
    public synchronized static JSONArray getshebeilistwz(String projectid) {
        String url = "https://jzgj.crfeb.com.cn/api/getLocation?app_key=4F732CFB9020E908190B&app_secret=7E1173B4D41804DFC329&"+"project_id="+projectid;
        String body = HttpRequest.get(url)
                .execute()
                .body();

        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        JSONArray data = (JSONArray) jsonObject1.get("data");
        return data;
    }

    //所有车辆的实时数据
    public synchronized static JSONArray getdate(String token) {
        JSONArray jsonObject = new JSONArray();
        String url = "http://interface.wazert.com/api/gps/service/bus/getPosition?token="+token;
        String body = HttpRequest.post(url)
                .execute()
                .body();

        JSONObject jsonObject1 = new JSONObject(body);
        Integer code = (Integer) jsonObject1.get("code");
        if (code==1){
            jsonObject = jsonObject1.getJSONArray("data");
            if (jsonObject.size()>0) {
                return jsonObject;
            }else {
                return null;
            }
        }
        return null;
    }

    //所有车辆的里程
    public synchronized static JSONArray getshebeimil(String token,String busLicplate,String endTime) {
        String startTime = "2022-12-03 09:34:00";
        JSONArray jsonObject = new JSONArray();
        String url = "http://interface.wazert.com/api/gps/service/bus/getBusMileage?token="+token+"&busLicplate="+busLicplate+"&startTime="+startTime+"&endTime="+endTime;
        String body = HttpRequest.post(url)
                .execute()
                .body();

        JSONObject jsonObject1 = new JSONObject(body);
        Integer code = (Integer) jsonObject1.get("code");
        if (code==1){
            jsonObject = jsonObject1.getJSONArray("data");
            if (jsonObject.size()>0) {
                return jsonObject;
            }else {
                return null;
            }
        }
        return null;
    }
}
