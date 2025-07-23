package com.trtm.iot.wzliaocang.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.util.Timeout;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class WgWebapi {
    //wg提供的外网服务器及控制器  可切换为本地的
//    public static  String testUrl = "http://webapi.wiegand.com.cn:61080";;
//    public static  int testControllerSN = 253118302;  //253118302

    //本地电脑
    public static String testUrl = "http://z.traiot.cn:61080/";


    //url: 1001服务器域名IP及端口
    //cmd: 发送信息
    //timeoutMs: 接收超时 默认是3000毫秒
    public String PushToWebWithjson(String url, String cmdBody, int timeoutMs)  //默认超时3000ms
    {
        String retInfo = "";
        int timeout = 3000;
        try {
            if (timeoutMs > timeout) {
                timeout = timeoutMs;
            }
            Content content2 = Request.post(url)
                    .connectTimeout(Timeout.ofMilliseconds(5000))
                    .responseTimeout(Timeout.ofMilliseconds(timeout))
                    .setHeader("Accept", "application/json")
                    .setHeader("Content-Type", "application/json")//设置header信息
                    .bodyString(cmdBody, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent();
            retInfo = content2.asString();
        } catch (IOException ioException) {

        }
        return retInfo;
    }

    public String PushToWebWithjson(String url, String body) {
        return PushToWebWithjson(url, body, 3000);
    }

    public boolean successIsTrue(String strResult) {
        boolean bRet = false;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(strResult);

            JsonNode result = jsonNode.get("result");
            if (result != null) {
                JsonNode success = result.get("success");
                if (success != null) {
                    if (success.asBoolean()) {
                        bRet = true;
                    }
                }
            }
        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
        }
        return bRet;
    }

    //bShowTime 表示是否显示当前时间
    public static void logInfoWithTime(String info, boolean bShowTime) //日志信息
    {
        Calendar cal = null;
        if (bShowTime) {
            cal = (Calendar.getInstance());
            System.out.print(cal.getTime().toString());
            System.out.print(" ");
        }
        System.out.println(info);
        String filepath = "wglog.log";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath, true));
            if (bShowTime) {
                out.write(cal.getTime().toString());
                out.write(" ");
            }
            out.write(info);
            out.newLine();
            out.close();
        } catch (IOException e) {

        }
    }

    public static void logInfo(String info) //日志信息
    {
        Calendar cal = (Calendar.getInstance());
        System.out.print(cal.getTime().toString());
        System.out.print(" ");
        System.out.println(info);
        String filepath = "wglog.log";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath, true));
            out.write(cal.getTime().toString());
            out.write(" ");
            out.write(info);
            out.newLine();
            out.close();
        } catch (IOException e) {

        }
    }

    public static void logInfoInJson(String info) //日志信息
    {
        try {
            logInfo((new ObjectMapper()).readTree(info).toPrettyString());
        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
        }
    }

    public static void logInfoInJsonWithoutTime(String info) //日志信息
    {
        try {
            logInfoWithTime((new ObjectMapper()).readTree(info).toPrettyString(), false);
        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
        }
    }
}
