package org.jeecg.modules.job.kezhujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthtm.service.ITrHnthtMService;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.trtm.iot.trhnthts.service.ITrHnthtSService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName huitanJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/2/2 17:09
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class huitanJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrHnthtMService trHnthtMService;
    @Autowired
    private ITrHnthtSService trHnthtSService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 根据定时任务配置表获取一条配置信息
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.KZ_HTY); // 柯诸回弹仪
        // 如果配置为空则退出方法
        if (config == null) {
            log.info(String.format("未获取到柯诸钢保数据推送定时任务的配置信息：%s", DateUtils.now()));
            return;
        }

        // 当前数据检测到的目标id
        Integer curId = config.getCurid();
        String extra = config.getExtra();
        JSONObject extraJson = JSONUtil.parseObj(extra);
        if (extraJson.isEmpty()) {
            log.info(String.format("没有配置要传输柯诸钢保数据的设备：%s", DateUtils.now()));
            return;
        }

        // 从配置中获取需要处理的设备列表
        String deviceList = extraJson.getStr("shebeilist");
        List<TrHnthtM> trHnthtMList = trHnthtMService.selectHntHtList(deviceList, curId);
        if (trHnthtMList == null || trHnthtMList.isEmpty()) {
            log.info(String.format("暂无柯诸钢保未推送数据：%s", DateUtils.now()));
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 对每个设备的每次测试数据进行处理
        for (TrHnthtM trHnthtM : trHnthtMList) {
            int id = trHnthtM.getId();
            String projectname = trHnthtM.getProjectname();
            String userId = projectname.contains("TJ01") ? "13302947" : "13302949";
            String url = String.format("http://112.95.76.11:8000/gapp/userlogin.action?userId=%s&userPass=d7b92ac90e93355ffeba35186268f43b&loginTag=0", userId);

            // 获取用户信息
            JSONObject userJson = user(url);

            String cookieStr = "";
            try {
                // 发送POST请求获取Cookie值
                Map<String, List<String>> responseHeader = sendPostGetCookie(url);
                List<String> cookieList = responseHeader.getOrDefault("Set-Cookie", new ArrayList<>());
                cookieStr = StringUtils.join(cookieList, ";");
                cookieStr = cookieStr.replaceAll("Path=/gapp;HttpOnly;", "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 基本信息
            JSONObject basicJson = new JSONObject();
            basicJson.set("dbName", userJson.get("dbName"));
            basicJson.set("spTypeId", 4);
            Date checktime = trHnthtM.getChecktime();
            basicJson.set("testDate", sdf.format(checktime));
            basicJson.set("projId", userJson.get("projSubId"));
            basicJson.set("wbsId", 0);

            String sgbw = trHnthtM.getSgbw();

            basicJson.set("projectName", sgbw);
            basicJson.set("addUnitId", userJson.get("deptID"));
            basicJson.set("addUnitName", userJson.get("deptName"));
            basicJson.set("tableId", "194801");
            basicJson.set("version", "4.0");
            basicJson.set("listId", "1001005176");
            basicJson.set("addWay", "1");
            basicJson.set("d01", trHnthtM.getPoint());
            basicJson.set("d03", trHnthtM.getStandarddeviation());
            basicJson.set("TestdataKid", trHnthtM.getTestid());

            String basicResponse = HTTPUtil.doPostForm("http://112.95.76.11:8000/gapp/qrtestsourceadd.action", basicJson, cookieStr);

            pushandreturnService.saveData(id, String.valueOf(basicJson), config.getRemark(), basicResponse);
            trHnthtM.setIstuisong(trHnthtM.getIstuisong()+1);
            trHnthtMService.saveOrUpdate(trHnthtM);

            JSONObject basicResult = new JSONObject(basicResponse);
            JSONObject dataObj = basicResult.getJSONObject("data");
            String kid = dataObj.getStr("kid");

            // 详情
            QueryWrapper<TrHnthtS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("testid", trHnthtM.getTestid());
            List<TrHnthtS> detailList = trHnthtSService.list(queryWrapper);

            for (TrHnthtS trHnthtS : detailList) {
                JSONObject detailJson = new JSONObject();
                detailJson.set("dbName", userJson.get("dbName"));
                detailJson.set("parent", kid);
                detailJson.set("spTypeId", "4");
                detailJson.set("testDate", sdf.format(checktime));
                detailJson.set("projId", userJson.get("projSubId"));
                detailJson.set("projectName", sgbw);
                detailJson.set("addUnitId", userJson.get("deptID"));
                detailJson.set("addUnitName", userJson.get("deptName"));
                detailJson.set("tableId", "194801");
                detailJson.set("version", "4.0");
                detailJson.set("listId", "1001005178");
                detailJson.set("addWay", "1");
                detailJson.set("wbsId", 0);
                detailJson.set("TestdataKid", trHnthtM.getTestid()+trHnthtS.getId());

                // 处理详情数据
                String number = trHnthtS.getNumber();
                String[] split = number.split(",");
                for (int i = 0; i < split.length; i++) {
                    if (i < 8) {
                        detailJson.set("d0" + (i + 2), split[i]);
                    } else if (i < 18) {
                        detailJson.set("d" + (i + 2), split[i]);
                    } else if (i == 18) {
                        detailJson.set("d20", split[i]);
                    }
                }
                detailJson.set("d18", trHnthtM.getCarbonizedepth());
                String detectionAngleStr = trHnthtM.getDetectionangle();
                if (detectionAngleStr.length() >= 3) {
                    detectionAngleStr = detectionAngleStr.substring(2);
                }
                detailJson.set("d19", detectionAngleStr);
                int isbengsong = "是".equals(trHnthtM.getDetectionangle()) ? 1 : 0;
                detailJson.set("d20", isbengsong);

                // 将处理后的数据添加到服务器
                String detailResponse = HTTPUtil.doPostForm("http://112.95.76.11:8000/gapp/qrtestsourceadd.action", detailJson, cookieStr);

                pushandreturnService.saveData(id, String.valueOf(detailJson), config.getRemark(), detailResponse);
                JSONObject detailResult = new JSONObject(detailResponse);
                String msg = detailResult.getStr("msg");
                if (msg.contains("true")) {
                    log.info(String.format("柯诸回弹仪推送成功！id=%s，JSON数据：%s", id, detailResponse));
                } else {
                    log.info(String.format("柯诸回弹仪推送失败！id=%s，JSON数据：%s", id, detailResponse));
                }

            }
            // 保存处理完的数据
            sysConfigService.updateSysConfig(JobUtil.KZ_HTY, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }

    // 发送POST请求获取Cookie
    public Map<String, List<String>> sendPostGetCookie(String urlPath) throws Exception {
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(10900);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.flush();
            out.close();
            return conn.getHeaderFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    // 获取用户信息
    public JSONObject user(String url) {
        HttpResponse response = HttpRequest.get(url)
                .timeout(10000)
                .execute();
        if (response.getStatus() != 200) {
            throw new RuntimeException("请求失败，状态码：" + response.getStatus());
        }
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(response.body());
        return jsonObject.getJSONObject("data");
    }

}
