package org.jeecg.modules.job.jobutil;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * \* @author: Xx
 * \* Date: 2022/3/10
 * \* Time: 16:09
 * \* Description:
 * \
 */
@Component
public class RuiCangBhzUtil {
    private static RuiCangBhzUtil ruiCangBhzUtil;
    @PostConstruct
    public void init() {
        ruiCangBhzUtil = this;
    }


    /**
     * 拌合站生产数据推送到瑞仓内网
     * @param data
     * @return
     */
    public  Integer PostRuiCangHntBhz(JSONObject data){
        int code=0;
        String post = HttpRequest.post("https://zgj20.cncico.com/wlwpt/hntbhz/bhzCementBase/addOpen")
                .header("Content-Type", "application/json")
                .body(String.valueOf(data))
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        Integer codes = (Integer) jsonObject.get("code");
        if(codes == 200){
            code=200;
        }else{
            code=400;
        }
        return code;
    }
    /**
     * 拌合站统计数据发送瑞仓内网
     * @param data
     * @return
     */
    public  Integer PostRuiCangHntBhzStatistics(JSONObject data){
        int code=0;
        String post = HttpRequest.post("https://zgj20.cncico.com/wlwpt/bhzStatistics/bhzCementStatistics/addOpen")
                .header("Content-Type", "application/json")
                .body(String.valueOf(data))
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        Integer codes = (Integer) jsonObject.get("code");
        if(codes == 200){
            code=200;
        }else{
            code=400;
        }
        return code;
    }
}
