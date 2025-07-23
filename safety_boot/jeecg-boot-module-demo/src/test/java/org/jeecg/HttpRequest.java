package org.jeecg;



import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.codec.Base64;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Chengqb
 * @Date: Created in 20:19 2019/5/28
 * @ClassName: HttpRequest
 * @Description: 发起HTTP请求第三方路径（页面、接口等）
 */
public class HttpRequest {

    //在实际开发中，main方法可以删除；直接在所需的位置调用HttpRequest.sendGet()或者HttpRequest.sendPost()方法
    public static void main(String[] args) {

        //发送 GET 请求，第一个参数为url，第二个参数为请求参数
//        String s=HttpRequest.sendGet("http://v.qq.com/x/cover/kvehb7okfxqstmc.html?vid=e01957zem6o", "");
//        System.out.println(s);

        //发送 POST 请求

      //  String a = String.format("下钻水泥用量为%1$.2f小于设计值%2$.0f；",1.1234,1.7894);
        String a =  "b8k0-036.000+b8k0+076.000_ 16-8";
        if(a.contains("_")){
            a = a.substring(a.indexOf("_")+1,a.length());
        }
        System.out.println(a);
//
//       Map<String,String> param = new HashMap();
//        param.put("appId","1001");
//        param.put("authToken","eyJhbGciOiJSUzI1NiJ9.eyJ1c2VybmFtZSI6IueOi-W_l-S6kSIsImV4cCI6MTYzOTQ3MTYzN30.RKse0kHpohEOhyxQ9kfTXuvHsipAub8es4BiCelROZ7dtiFZHY6kXhX-sZlJ8bKhMbvxbV3wB5pF3GxXoui_d9nhNSVzRXrzJTEmrXwjTArDE0Bf3aplq8RFtNBXwmOjADbc2kRaIwxfO7OYiQyJNdLw3mLZRh4PES0zspcFYiXcng4nR5WlbqkeAHch-3MALM7rh7wn1LCAXPPyVs1o4qZ5Mbhf9idL_Xj0K61NmPSOPElrnGQPRbK_H-gK5UgVp3RykyRH4jgkMyrtKng2QxWU-5rQPb9mb25C-E6KA65AyVUW2zdEfvSzDJYSPOYzrPWCrdbKmBxKItlToVRcwQ");
//
//        JSONObject sr=HttpRequest.sendPost("http://47.100.92.98:8182/gateway/auth-server/auth/sign/token", param);
//        byte [] raw= Base64.decode("7jLRh9h1odUQ8GamGCllGA==");



    }

    /**
     * 向指定URL发送GET方法的请求
     * @param url：发送请求的URL
     * @param param：请求参数，请求参数应该是 name1=value1&name2=value2&name3=value3 的形式。
     * @return String[result]：所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);

            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立实际的连接
            connection.connect();

            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();

            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }

        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url：发送请求的 URL
     * @param param：请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return String[result]：所代表远程资源的响应结果
     */
    public static String sendPost1(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);

            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 发送请求参数
            out.print(param);

            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }

        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static JSONObject sendPost(String url1,Map<String, String> param) {
        String Json = JSONObject.toJSONString(param);
        String result = "";
        String line = "";
        BufferedReader reader = null;
        String urlPath=url1;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("accept","application/json");
            if (Json != null && !StringUtil.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();
                conn.getResponseCode();
            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSONObject.parseObject(result);
    }

}

