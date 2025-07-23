package org.jeecg.modules.job.fkclJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class fkjspUtil {

    @Autowired
    private static org.jeecg.modules.job.fkclJob.fkjspUtil fkjspUtil;

    @PostConstruct
    public void init() {
        fkjspUtil = this;
    }

    // 获取token
    public synchronized static JSONObject gettoken(String LoginName,String LoginPassword) {
        String l = null;
        l = "?LoginName="+LoginName+"&LoginPassword="+LoginPassword+"&LoginType=ENTERPRISE"+"&language=cn"+"&timeZone=+08"+"&apply=APP";
        String url = "http://api.588gps.net/GetDateServices.asmx/loginSystem"+l;
        System.out.println(url);
        String body = HttpRequest.get(url)
                .execute()
                .body();

        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer codes = (Integer) jsonObject1.get("errorCode");
        if (codes==200){
            return jsonObject1;
        }
        return null;
    }

    public synchronized static JSONArray getdate(String mds,String objectid) {
        JSONArray jsonObject = new JSONArray();
        String l = null;
        l = "?mapType=GAODE"+"&mds="+mds+"&method=getUserAndGpsInfoByIDsUtc"+"&option=cn"+"&user_id="+objectid;
        String url = "http://api.588gps.net/GetDateServices.asmx/GetDate"+l;
        System.out.println(url);
        String body = HttpRequest.get(url)
                .execute()
                .body();

        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        if (jsonObject1.size() > 0){
            jsonObject = jsonObject1.getJSONArray("records");
            if (jsonObject.size()>0) {
                return jsonObject;
            }else {
                return null;
            }
        }
        return null;
    }

    //获取总里程
    public synchronized static JSONArray getshebeimil(String mds) {
        JSONArray jsonObject = new JSONArray();
        String l = "?method=report&showZeroMil=true"+"&mds="+mds;
        String url = "http://www.588gps.net/GetDataService.aspx"+l;
        System.out.println(url);
        String body = HttpRequest.get(url)
                .execute()
                .body();

        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        String errorCode =(String) jsonObject1.get("errorCode");
        if (errorCode.equals("200")){
            jsonObject = jsonObject1.getJSONArray("rows");
            if (jsonObject.size()>0) {
                return jsonObject;
            }else {
                return null;
            }
        }
        return null;
    }
    // 获取设备id
    public synchronized static JSONArray getshebeiid(String mds) {
        JSONArray jsonObject = new JSONArray();
        String l = "?method=getDeviceList"+"&mds="+mds;
        String url = "http://api.588gps.net/GetDateServices.asmx/GetDate"+l;
        System.out.println(url);
        String body = HttpRequest.get(url)
                .execute()
                .body();

        JSONObject jsonObject1 = new JSONObject(body);
        String errorCode =(String) jsonObject1.get("errorCode");
        if (errorCode.equals("200")){
            jsonObject = jsonObject1.getJSONArray("rows");
            if (jsonObject.size()>0) {
                System.out.println(jsonObject);
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
