package org.jeecg.modules.job.jobutil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * \* @author: Xx
 * \* Date: 2021/6/29
 * \* Time: 11:57
 * \* Description:电子围栏工具类
 * \
 */
@Component
public class WeiLanUtil {
    private static WeiLanUtil weiLanUtil;
    @PostConstruct
    public void init() {
        weiLanUtil = this;
    }

    private HttpClient httpClient = null;
    public static String KEY="97730ddb8e13615086b884bbde11c254";//高德地图KEY
    //POST
    public static String addWEILan="https://tsapi.amap.com/v1/track/geofence/add/circle";//高德地图创建圆形电子围栏
    //POST
    public static String editWEILan="https://tsapi.amap.com/v1/track/geofence/update/circle";//更新围栏
    //POST
    public static String delWEILan="https://tsapi.amap.com/v1/track/geofence/delete";//删除围栏
    //POST
    public static String addTerminal="https://tsapi.amap.com/v1/track/terminal/add";//创建终端(每次调用此接口，可以在指定 Service 下创建1个终端 默认最大支持创建100000个。)
    //POST
    public static String delTerminal="https://tsapi.amap.com/v1/track/terminal/delete";//删除终端
    //POST
    public static String addDetectionObject="https://tsapi.amap.com/v1/track/geofence/terminal/bind";//增加监测对象
    //POST
    public static String delDetectionObject="https://tsapi.amap.com/v1/track/geofence/terminal/unbind";//删除检测对象
    //GET
    public static String DetectionRelationship="https://tsapi.amap.com/v1/track/geofence/status/location";//查询指定坐标与围栏关系

    /**
     * 地球半径
     */
    private static final double EARTH_RADIUS = 6378137;
    /**
     * 弧度
     */
    private static final double RAD = Math.PI / 180.0;


    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米 *
     */
    public  Double getDistance(Double lng1, Double lat1, Double lng2, Double lat2) {
        double radLat1 = lat1 * RAD;
        double radLat2 = lat2 * RAD;
        double a = radLat1 - radLat2;
        Double b = (lng1 - lng2) * RAD;
        Double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000.0;
        return s;
    }

    public static double getDistances(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);

        double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) +
                        Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

        return distance;
    }

    /**
     *
     * @param url
     * @param key
     * @return    高德地图创建圆形电子围栏
     */
    public Integer PostaddWEILan(String url, String key,String sid,String name,String center,Integer radius,String desc) {
        //创建HttpClient对象
        Integer codes = 500;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //声明List集合，封装表单中的参数
        JSONObject jsonObject = new JSONObject();
        //围栏名称
        jsonObject.put("name",name);
        //围栏经纬度
        jsonObject.put("center",center);
        //地图key
        jsonObject.put("key",key);
        //sid为猎鹰service唯一编号
        jsonObject.put("sid",sid);
        //半径
        jsonObject.put("radius",radius);
        //备注
        if (desc != null){
            jsonObject.put("desc",desc);
        }
        String s = jsonToURL(jsonObject.toString());
        //创建HttpPost对象，设置url访问地址   param 的请求方式
        HttpPost httpPost = new HttpPost(url+"?"+s);
        // 执行http的post请求
        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JSONObject jsonObject1 = new JSONObject(result);
            Integer code = (Integer) jsonObject1.get("errcode");
            if(code==10000){
                JSONObject jsonArray = (JSONObject) jsonObject1.get("data");
                Integer gfid = (Integer) jsonArray.get("gfid");
                codes=gfid;
                return codes;
            }else{
                codes=500;
                return codes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codes;
    }

    /**
     *
     * @param url
     * @param key
     * @return    高德地图删除圆形电子围栏
     */
    public String PostdelWEILan(String url, String key,String sid,Integer gfids) {
        //创建HttpClient对象
        String codes = "500";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //声明List集合，封装表单中的参数
        JSONObject jsonObject = new JSONObject();
        //地图key
        jsonObject.put("key",key);
        //sid为猎鹰service唯一编号
        jsonObject.put("sid",sid);
        //半径
        jsonObject.put("gfids",gfids);
        String s = jsonToURL(jsonObject.toString());
        //创建HttpPost对象，设置url访问地址   param 的请求方式
        HttpPost httpPost = new HttpPost(url+"?"+s);
        // 执行http的post请求
        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JSONObject jsonObject1 = new JSONObject(result);
            Integer code = (Integer) jsonObject1.get("errcode");
            if(code==10000){
                codes="200";
                return codes;
            }else{
                codes="500";
                return codes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codes;
    }


    /**
     *
     * @param url
     * @param key
     * @return    高德地图创建终端
     */
    public Map PostaddTerminal(String url, String key,String sid,String name,String desc) {
        //创建HttpClient对象
        String codes = null;
        Map map=new HashMap();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //声明List集合，封装表单中的参数
        JSONObject jsonObject = new JSONObject();
        //服务名称
        jsonObject.put("name",name);
        //地图key
        jsonObject.put("key",key);
        //sid为猎鹰service唯一编号
        jsonObject.put("sid",sid);
        //备注
        if (desc != null){
            jsonObject.put("desc",desc);
        }
        String s = jsonToURL(jsonObject.toString());
        //创建HttpPost对象，设置url访问地址   param 的请求方式
        HttpPost httpPost = new HttpPost(url+"?"+s);
        // 执行http的post请求
        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JSONObject jsonObject1 = new JSONObject(result);
            Integer code = (Integer) jsonObject1.get("errcode");
            if(code==10000){
                JSONObject jsonArray = (JSONObject) jsonObject1.get("data");
                Integer tid = (Integer) jsonArray.get("tid");
                String name1 = (String) jsonArray.get("name");
                map.put("tid",tid);
                map.put("name",name1);
                return map;
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     *
     * @param url
     * @param key
     * @return    高德地图删除终端
     */
    public String PostdelTerminal(String url, String key,String sid,Integer tid) {
        //创建HttpClient对象
        String codes = "500";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //声明List集合，封装表单中的参数
        JSONObject jsonObject = new JSONObject();
        //地图key
        jsonObject.put("key",key);
        //sid为猎鹰service唯一编号
        jsonObject.put("sid",sid);
        //终端id
        jsonObject.put("tid",tid);
        String s = jsonToURL(jsonObject.toString());
        //创建HttpPost对象，设置url访问地址   param 的请求方式
        HttpPost httpPost = new HttpPost(url+"?"+s);
        // 执行http的post请求
        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JSONObject jsonObject1 = new JSONObject(result);
            Integer code = (Integer) jsonObject1.get("errcode");
            if(code==10000){
                codes="200";
                return codes;
            }else{
                codes="500";
                return codes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codes;
    }



    /**
     *
     * @param url
     * @param key
     * @return    高德地图增加监测对象
     */
    public String PostaddDetectionObject(String url, String key,String sid,Integer gfid,Integer tids) {
        //创建HttpClient对象
        String codes = "500";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //声明List集合，封装表单中的参数
        JSONObject jsonObject = new JSONObject();
        //围栏id
        jsonObject.put("gfid",gfid);
        //地图key
        jsonObject.put("key",key);
        //服务唯一编号
        jsonObject.put("sid",sid);
        //终端id
        jsonObject.put("tids",tids);
        String s = jsonToURL(jsonObject.toString());
        //创建HttpPost对象，设置url访问地址   param 的请求方式
        HttpPost httpPost = new HttpPost(url+"?"+s);
        // 执行http的post请求
        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JSONObject jsonObject1 = new JSONObject(result);
            Integer code = (Integer) jsonObject1.get("errcode");
            if(code==10000){
                codes="200";
                return codes;
            }else{
                codes="500";
                return codes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codes;
    }



    /**
     *
     * @param url
     * @param key
     * @return    高德地图删除监测对象
     */
    public String PostdelDetectionObject(String url, String key,String sid,Integer tids,Integer gfid) {
        //创建HttpClient对象
        String codes = "500";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //声明List集合，封装表单中的参数
        JSONObject jsonObject = new JSONObject();
        //地图key
        jsonObject.put("key",key);
        //sid为猎鹰service唯一编号
        jsonObject.put("sid",sid);
        //终端id
        jsonObject.put("tids",tids);
        jsonObject.put("gfid",gfid);
        String s = jsonToURL(jsonObject.toString());
        //创建HttpPost对象，设置url访问地址   param 的请求方式
        HttpPost httpPost = new HttpPost(url+"?"+s);
        // 执行http的post请求
        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JSONObject jsonObject1 = new JSONObject(result);
            Integer code = (Integer) jsonObject1.get("errcode");
            if(code==10000){
                codes="200";
                return codes;
            }else{
                codes="500";
                return codes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codes;
    }


    /**
     * @param url
     * @param key
     * @return 高德地图 查询检测对象与围栏关系
     */
    public Integer GetDetectionRelationship(String url, String key,String sid,String location,String gfids){
        //创建HttpClient对象
        Integer codes = 500;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //声明List集合，封装表单中的参数
        JSONObject jsonObject = new JSONObject();
        //地图key
        jsonObject.put("key",key);
        //sid为猎鹰service唯一编号
        jsonObject.put("sid",sid);
        jsonObject.put("gfids",gfids);
        //终端id
        jsonObject.put("location",location);
        String s = jsonToURL(jsonObject.toString());
        //创建HttpPost对象，设置url访问地址   param 的请求方式
        HttpGet httpPost = new HttpGet(url+"?"+s);
        // 执行http的post请求
        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JSONObject jsonObject1 = new JSONObject(result);
            Integer code = (Integer) jsonObject1.get("errcode");
            if(code==10000){
                JSONObject jsonArray = (JSONObject) jsonObject1.get("data");
                JSONArray results = (JSONArray) jsonArray.get("results");
                JSONObject first = results.getJSONObject(0);
                Integer in = (Integer) first.get("in");
                codes=in;
                return codes;
            }else{
                codes=500;
                return codes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codes;
    }



    /**
     * 编译json数据转换成地址栏需要的数据
     * @param jsonString
     * @return
     */
    public static String jsonToURL(String jsonString) {
        String url = "";
        try {
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(jsonString);
            Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
            StringBuffer sb = new StringBuffer();
            while (it.hasNext()) {
                Map.Entry entry = it.next();
                sb.append(entry.getKey().toString() + "=");
                String value = entry.getValue().toString();
                if (value == null || value.isEmpty() || value.length() == 0) {
                    sb.append("&");
                } else {
                    sb.append(URLEncoder.encode(value,"utf-8") + "&");
//                    sb.append(value + "&");
                }
            }
            url = sb.toString().substring(0, sb.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }


}
