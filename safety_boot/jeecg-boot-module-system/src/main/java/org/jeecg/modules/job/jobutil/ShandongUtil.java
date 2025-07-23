package org.jeecg.modules.job.jobutil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.apache.commons.collections4.Get;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * \* @author: Xx
 * \* Date: 2021/6/2
 * \* Time: 14:04
 * \* Description:推送到山东陆科的工具类
 * \
 */
@Component
public class ShandongUtil {
    private static ShandongUtil shandongUtil;

    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        shandongUtil = this;
    }

    private HttpClient httpClient = null;
    //刷新token令牌 post form-data  参数 refreshtoken 过期提示 "code": 1, 已经刷新token 未过期提示"code": 107, token未过期不需要请求
    public static String GetToken = "http://60.208.32.122:61601/updateAccessTokenSysInterfaceEnterprise.action";
    //推送多条数据接口 POST form-data 参数 access_token token令牌 type 业务分类编码 data json数组
    public static String TuiSong = "http://60.208.32.122:61601/saveDataListRoadInterfaces.action";
    public static String MORENToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJvdGhlciIsImlzcyI6ImhvbmdxaSIsImV4cCI6MTYyMzQ2ODAwNSwiaWF0IjoxNjIyNjA0MDA1LCJpbmZvIjoie1wib3JnTmFtZVwiOlwi5p2t5bee6auY6K6v54mp6IGU572R56eR5oqA5pyJ6ZmQ5YWs5Y-4KOa1i-ivlSlcIixcIm9yZ0lkXCI6XCJlZmY4N2JjNS1kMTRkLTRhYzEtOWMwZC0wODE0Y2NkYjU4NjNcIixcImFjY291bnRcIjpcImh6Z3hcIn0ifQ.i-EAktHIL6FAUOSI-Jl-tHj8JBb8am6u1EI2p1_nYXE";
    public static String publicProjectList = "publicProjectList";//项目业务编码
    public static String publicMixingStationList = "publicMixingStationList";//拌合站业务编码
    public static String publicMixingList = "publicMixingList";//拌合机业务编码
    public static String publicMixerList = "publicMixerList";//混凝土实时数据业务编码
    public static String publicMixerMoreList = "publicMixerMoreList";//混凝土超标数据业务编码
    public static String publicPitchList = "publicPitchList";//沥青实时数据业务编码
    public static String publicPitchMoreList = "publicPitchMoreList";//沥青超标数据业务编码
    public static String publicWaterList = "publicWaterList";//水稳实时数据业务编码
    public static String publicWaterMoreList = "publicWaterMoreList";//水稳超标数据业务编码

    //刷新请求token
    public String GetTokenpost(String url, String token) {
        //创建HttpClient对象
        String codes = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpPost对象，设置url访问地址
        HttpPost httpPost = new HttpPost(url);
        //声明List集合，封装表单中的参数
        List params = new ArrayList();
        //设置请求地址是：http://yun.itheima.com/search?keys=Java
        params.add(new BasicNameValuePair("refreshtoken", token));
        //创建表单的Entity对象,第一个参数就是封装好的表单数据，第二个参数就是编码
        UrlEncodedFormEntity formEntity = null;
        try {
            formEntity = new UrlEncodedFormEntity(params, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置表单的Entity对象到Post请求中
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpPost);
            //解析响应
            //if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity(), "utf8");
            System.out.println(content.length());
            JSONObject jsonObject = new JSONObject(content);
            Integer code = (Integer) jsonObject.get("code");
            JSONObject jsonArray = (JSONObject) jsonObject.get("data");
            String token1 = (String) jsonArray.get("token");
            //String token1=null;
//            for (int j = 0; j < jsonArray.size(); j++) {
//                JSONObject jsonObject2 = jsonArray.getJSONObject(j);
//                token1 = jsonObject2.getStr("token");
//            }
            if (code == 107) {//107 TOKEN尚未过期，请不要重复刷新
                codes = "107";
            } else if (code == 1) {//操作成功
                redisUtil.set("ShanDongLuKeToken", token1);
                codes = "200";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return codes;
        }

    }

    //根据业务分类编码 批量推送
    public String TuisongList(String url, String token, String type, String data) {

        //创建HttpClient对象
        String codes = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpPost对象，设置url访问地址
        HttpPost httpPost = new HttpPost(url);
        //声明List集合，封装表单中的参数
        List params = new ArrayList();
        //设置请求地址
        params.add(new BasicNameValuePair("access_token", token));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("data", data));
        //创建表单的Entity对象,第一个参数就是封装好的表单数据，第二个参数就是编码
        UrlEncodedFormEntity formEntity = null;
        try {
            formEntity = new UrlEncodedFormEntity(params, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置表单的Entity对象到Post请求中
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpPost);
            //解析响应
            //if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity(), "utf8");
            System.out.println(content.length());
            JSONObject jsonObject = new JSONObject(content);
            Integer code = (Integer) jsonObject.get("code");
            if (code == 1) {//操作成功
                codes = "200";
            } else {//操作失败
                codes = "0";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return codes;
        }
    }
}
