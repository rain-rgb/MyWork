package com.trtm.iot.zlpz.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ZLPZTestController：
 * @Description 线上IP为白名单，测试接口用
 * @Author 55314
 * @Date 2023/3/20 16:08
 * @Version 1.0
 **/
@Api(tags = "zlpz_test")
@RestController
@RequestMapping("/zlpz/ZLPZTestController")
@Slf4j
public class ZLPZTestController {
    @PostMapping(value = "/posttest")
    public Result<?> test(@RequestBody String args) {
        com.alibaba.fastjson.JSONObject body = com.alibaba.fastjson.JSONObject.parseObject(args);
        String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
        String back = HttpRequest.post(url)
                .body(String.valueOf(body))
                .timeout(20000)
                .execute().body();
        return Result.OK(back);
    }

    @PostMapping(value = "/posttestold")
    public Result<?> posttestold(@RequestBody String args) {
        com.alibaba.fastjson.JSONObject body = com.alibaba.fastjson.JSONObject.parseObject(args);
        String url = "http://223.4.78.87:21086/dtas-server/api/service/push";
        String back = HttpRequest.post(url)
                .body(String.valueOf(body))
                .timeout(20000)
                .execute().body();
        return Result.OK(back);
    }

    @PostMapping(value = "/getxmid")
    public Result<?> getxmid(HttpServletRequest request) {
        String back = HttpRequest.get("https://sjsn.jtyst.zj.gov.cn:8888/SPECIVALI_API/ZJROADQUALITY_API/ZLPZ_XMIDCX?token=66feb11c-cbf1-4fea-aabf-5958f686263f")
                .timeout(20000)
                .execute().body();
        return Result.OK(back);
    }

    @PostMapping(value = "/getbdid")
    public Result<?> getbdid(HttpServletRequest request) {
        String xmid = request.getParameter("xmid");
        String back = HttpRequest.get("https://sjsn.jtyst.zj.gov.cn:8888/SPECIVALI_API/ZJROADQUALITY_API/ZLPZ_BDCX?token=66feb11c-cbf1-4fea-aabf-5958f686263f&projectId=" + xmid)
                .timeout(20000)
                .execute().body();
        return Result.OK(back);
    }

    @PostMapping(value = "/getsbid")
    public Result<?> getsbid(HttpServletRequest request) {
        String xmid = request.getParameter("xmid");
        String bdid = request.getParameter("bdid");
        String equipmentNumber = request.getParameter("sbbh");

        JSONObject sendDate = new JSONObject();
        List sendList = new ArrayList();

        sendDate.set("equipmentNumber", equipmentNumber);
        sendDate.set("projectId", xmid);
        sendDate.set("sectionId", bdid);

        sendList.add(sendDate);

        JSONObject sendJsonObject = new JSONObject();
        sendJsonObject.set("serviceName", "ZLPZ_ZX_WLWSBCX");
        sendJsonObject.set("token", "93cd2c6567594107a16b51a65bcd5a37");
        sendJsonObject.set("updateNull", "true");
        sendJsonObject.set("param", sendList);

        String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
        String back = HttpRequest.post(url)
                .body(String.valueOf(sendJsonObject))
                .timeout(20000)
                .execute().body();
        return Result.OK(back);
    }

    @PostMapping(value = "/sbzc")
    public Result<?> sbzc(HttpServletRequest request) {
        String projectId = request.getParameter("projectId");
        String sectionId = request.getParameter("sectionId");
        String equipmentNumber = request.getParameter("sbbh");
        String id = request.getParameter("id");
        String equipType = request.getParameter("equipType");
        String equipmentName = request.getParameter("equipmentName");

        JSONObject sendDate = new JSONObject();
        List sendList = new ArrayList();

        sendDate.set("id", id);
        sendDate.set("equipType", equipType);
        sendDate.set("equipmentNumber", equipmentNumber);
        sendDate.set("equipmentName", equipmentName);
        sendDate.set("projectId", projectId);
        sendDate.set("sectionId", sectionId);

        sendList.add(sendDate);

        JSONObject sendJsonObject = new JSONObject();
        sendJsonObject.set("serviceName", "ZLPZ_ZX_WLWDGSBZC");
        sendJsonObject.set("token", "93cd2c6567594107a16b51a65bcd5a37");
        sendJsonObject.set("updateNull", "true");
        sendJsonObject.set("param", sendList);

        String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
        String back = HttpRequest.post(url)
                .body(String.valueOf(sendJsonObject))
                .timeout(20000)
                .execute().body();
        return Result.OK(back);
    }

    @PostMapping(value = "/sbzcold")
    public Result<?> sbzcold(HttpServletRequest request) {
        String projectId = request.getParameter("projectId");
        String sectionId = request.getParameter("sectionId");
        String equipmentNumber = request.getParameter("sbbh");
        String id = request.getParameter("id");
        String equipType = request.getParameter("equipType");
        String equipmentName = request.getParameter("equipmentName");

        JSONObject sendDate = new JSONObject();
        List sendList = new ArrayList();

        sendDate.set("id", id);
        sendDate.set("equipType", equipType);
        sendDate.set("equipmentNumber", equipmentNumber);
        sendDate.set("equipmentName", equipmentName);
        sendDate.set("projectId", projectId);
        sendDate.set("sectionId", sectionId);

        sendList.add(sendDate);

        JSONObject sendJsonObject = new JSONObject();
        sendJsonObject.set("serviceName", "ZLPZ_ZX_WLWDGSBZC");
        sendJsonObject.set("token", "93cd2c6567594107a16b51a65bcd5a37");
        sendJsonObject.set("updateNull", "true");
        sendJsonObject.set("param", sendList);

        String url = "http://223.4.78.87:21086/dtas-server/api/service/push";
        String back = HttpRequest.post(url)
                .body(String.valueOf(sendJsonObject))
                .timeout(20000)
                .execute().body();
        return Result.OK(back);
    }
}
