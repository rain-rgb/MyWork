package org.jeecg.modules.job.figps;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.trtm.iot.clgl.service.IFrontDeviceHistorydataService;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * 风控在线GPS定时任务(运料车)
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class figpsJob implements Job {

    @Autowired
    private IFrontDeviceHistorydataService frontService;

    @Autowired
    private IFrontDeviceRealdataService realService;
    private static final String dataurl = "http://open.figps.com/api/";
    private static final String authstr="{" +
            "\"appid\":\"17398007785\"," +
            "\"time\":1635386457," +
            "\"signature\":\"193386bbc84e8a45d7599706e865bc31\"" +
            "}";

    private static final String statusurl = "device/status?accessToken=%s&account=17398007785";

    private String accessToken = "";

    private void doAuth() {
        JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.post(dataurl+"auth", authstr));
        if (jsonObject.getInt("code") == 0) {
            accessToken = jsonObject.getStr("accessToken");
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (StrUtil.isBlank(accessToken)) {
            doAuth();
        }
        if (StrUtil.isNotBlank(accessToken)) {
            JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.get(dataurl+String.format(statusurl,accessToken)));
            if (jsonObject != null && jsonObject.getInt("code") == 0) {
                JSONArray jsarray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsarray.size(); i++) {
                    JSONObject json = jsarray.getJSONObject(i);
                    realService.updateFrontdeviceData(json);
                    /*if (realService.updateFrontdeviceData(json)) {
                        frontService.insertGpsdata(json);
                    }*/
                }
            } else {
                accessToken = "";
            }
        }
    }
}
