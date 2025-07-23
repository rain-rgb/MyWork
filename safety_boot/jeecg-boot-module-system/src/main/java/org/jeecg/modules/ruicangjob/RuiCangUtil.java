package org.jeecg.modules.ruicangjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.job.jobutil.ShandongUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * \* @author: Xx
 * \* Date: 2022/1/5
 * \* Time: 13:40
 * \* Description: 瑞苍高速定时任务推送工具类
 * \
 */
@Component
public class RuiCangUtil {
    private static RuiCangUtil ruiCangUtil;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @PostConstruct
    public void init() {
        ruiCangUtil = this;
    }

    public String GETToken() {//瑞苍试验平台获取token
        String token = null;
        String get = HttpRequest.get("http://121.40.163.88:8084/CATDPS/rest/tokens?username=test&password=123456").execute().body();
        // 测试接口
      //  String get = HttpRequest.get("http://118.178.125.20:8091/CATDPS-ZJ/rest/tokens?username=test&password=123456").execute().body();
        JSONObject jsonObject = new JSONObject(get);
        JSONObject data1 = jsonObject.getJSONObject("obj");
        String accessToken = String.valueOf(data1.get("token"));
        String codes = (String) jsonObject.get("success");
        if (codes.equals("0")) {
            token = accessToken;//redisUtil.set("NBaccessToken",accessToken,5400);//取消缓存 直接每次请求更新
        } else {
            token = null;
        }
        return token;
    }

    /**
     * 瑞仓混凝土回弹数据推送
     *
     * @param data
     * @param token
     * @return
     */

    public Integer HntHtTuiSong(JSONObject data, String token) {
        Integer code = 400;
        System.out.println(String.valueOf(data));
        String datas = String.valueOf(data);
        System.out.println(datas);
        String bodys = HttpRequest.post("http://121.40.163.88:8084/CATDPS/rest/WzReceivingController/hntwtadd")
                .header("Content-Type", "application/json")
                .header("X-AUTH-TOKEN", token)
                .body(datas)
                .execute()
                .body();
        /**
         * 测试接口
         */
/*        String bodys = HttpRequest.post("http://118.178.125.20:8091/CATDPS-ZJ/rest/WzReceivingController/hntwtadd")
                .header("Content-Type", "application/json")
                .header("X-AUTH-TOKEN", token)
                .body(datas)
                .execute()
                .body();*/
        pushandreturnService.saveData(0, String.valueOf(datas), "瑞苍回弹试验系统", bodys);
        JSONObject jsonObject = new JSONObject(bodys);
        String codes = (String) jsonObject.get("success");
        if (codes.equals("0")) {
            code = 200;
        } else {
            code = 400;
        }
        return code;
    }

    /**
     * 瑞仓钢筋保护层数据推送
     *
     * @param data
     * @param token
     * @return
     */
    public Integer GangJingTuiSong(JSONObject data, String token) {
        Integer code = 400;
        System.out.println(String.valueOf(data));
        String datas = String.valueOf(data);
        System.out.println(datas);
        String bodys = HttpRequest.post("http://121.40.163.88:8084/CATDPS/rest/WzReceivingController/gjwtadd")
                .header("Content-Type", "application/json")
                .header("X-AUTH-TOKEN", token)
                .body(datas)
                .execute()
                .body();
        pushandreturnService.saveData(0, String.valueOf(datas), "瑞苍钢筋试验系统", bodys);
        JSONObject jsonObject = new JSONObject(bodys);
        String codes = (String) jsonObject.get("success");
        if (codes.equals("0")) {
            code = 200;
        } else {
            code = 400;
        }
        return code;
    }
}
