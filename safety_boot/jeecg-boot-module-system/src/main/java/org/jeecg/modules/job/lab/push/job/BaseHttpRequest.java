package org.jeecg.modules.job.lab.push.job;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jeecg.modules.job.lab.push.utils.PMUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author lis1
 * @date 2022/12/12
 * @time 9:59
 */
@Slf4j
public class BaseHttpRequest {

    /**
     * URL地址是固定不变的 发送请求时的itype参数用于区分接口
     */
    // 外网地址
    private final static String INTERNET_URL = "https://openapi-test05-open.pinming.org/api/openapi/gddnSystemReceive";

    // 外网测试地址2
    //private final static String INTERNET_URL = "https://openapi-test05-open.pinming.org/api/openapi/gddnSystemReceive-lh";

    // 内网地址 推送地址
    private final static String INTRANET_URL = "https://openapi.pinming.cn/api/openapi/gddnSystemReceive";

    // 密钥
    private final static String SECRET_KEY = "ef3cf4a0cc9cf73eb970abece9523e5b";

    // 开发者key
    private final static String DEV_KEY = "2c908a9e737edd6601737edd666e0000";

    // COOM
    private final static String COOM = "10373533";


    /**
     * @param data 数据列表
     * @param env  环境控制变量 如果值为 1 使用外网地址 反之使用内网地址
     * @return
     * @Description HTTP POST 请求
     */
    private String httpPostData(NameValuePair[] data, Integer env) {

        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("UTF-8");
        PostMethod postMethod;
        if (env == 1) {
            postMethod = new PostMethod(INTERNET_URL);
        } else {
            postMethod = new PostMethod(INTRANET_URL);
        }

        //将表单的值放入postMethod中
        postMethod.setRequestBody(data);

        //执行postMethod
        int statusCode = 0;

        try {
            statusCode = httpClient.executeMethod(postMethod);
        } catch (HttpException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        String str = "";

        //   HttpClient对于要求接收后继服务的请求，象POST和PUT等不能自动处理转发
        //   301或者302
        if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
            //   从头中取出转向的地址
            Header locationHeader = postMethod.getResponseHeader("location");
            String location = null;
            if (locationHeader != null) {
                location = locationHeader.getValue();
                System.out.println("The page was redirected to:" + location);
            } else {
                System.err.println("Location field value is null.");
            }
            return "";
        } else {
            System.out.println(postMethod.getStatusLine());

            try {
                //	获取返回值
                str = postMethod.getResponseBodyAsString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        postMethod.releaseConnection();

        return str;
    }

    /**
     * 请求参数处理并发送post请求
     *
     * @param obj  数据对象
     * @param type 指定访问具体接口
     * @param env  环境（内外网 1为外网，其他为内网）
     * @return
     */
    public String requestDataProcessing(Object obj, String type, Integer env) {
        String now = PMUtil.getData();
        System.out.println("now:" + now);

        //2.先对数据进行utf-8转码
        String data = toJson(obj);
        System.out.println("原数据：" + data);

        String dataEncode = PMUtil.encode(data);
        System.out.println("数据转string：" + dataEncode);

        //4.加密后的业务数据
        String aesData = PMUtil.aesEncrypt(SECRET_KEY, dataEncode);
        System.out.println("加密后的业务数据：" + aesData);

        //MD5签名
        String sign = PMUtil.md5Encrypt(type + now + COOM + aesData + SECRET_KEY);
        System.out.println("签名：" + sign);

        NameValuePair[] params = {
                new NameValuePair("itype", type),
                new NameValuePair("time", now),
                new NameValuePair("cono", COOM),
                new NameValuePair("data", aesData),
                new NameValuePair("sign", sign),
                new NameValuePair("etype", "1"),            //1-AES 	2-BASE64
                new NameValuePair("dtype", "1"),
        };
//        new NameValuePair("dkey", DEV_KEY),
        String result = httpPostData(params, env);
        System.out.println(result);
        return result;
    }

    /**
     * @param obj
     * @return
     * @Description 把object转换成JSON格式的字符串
     */
    public static String toJson(Object obj) {
        Writer writer = new StringWriter();
        String result = "";
        try {
            ObjectMapper om = new ObjectMapper();
            om.writeValue(writer, obj);
            result = writer.toString();
            if (log.isDebugEnabled()) {
                System.out.println(result);
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                    writer = null;
                } catch (IOException e) {
                    if (log.isDebugEnabled()) {
                        log.debug(e.getMessage(), e);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 请求结果处理
     *
     * @param result 请求结果
     */
    public Boolean resultProcessing(String result) {
        System.out.println("请求结果：" + result);
        JSONObject object = JSONUtil.parseObj(result);
        Boolean success = (Boolean) object.get("success");
        return success;
    }
}
