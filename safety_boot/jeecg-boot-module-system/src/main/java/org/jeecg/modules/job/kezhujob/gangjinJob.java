package org.jeecg.modules.job.kezhujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcm.service.ITrGangjinbhcMService;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trgangjinbhcs.service.ITrGangjinbhcSService;
import com.xkcoding.http.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.directive.Foreach;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.jeecg.modules.job.kezhujob.HTTPUtil.doPostForm;



/**
 * @ClassName gangjinJob：
 * @Description 柯诸钢筋仪推送
 * @Author 55314
 * @Date 2022/12/30 15:01
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class gangjinJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrGangjinbhcMService trGangjinbhcMService;
    @Autowired
    private ITrGangjinbhcSService trGangjinbhcSService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.KZ_GJY);//柯诸钢保数据推送
        if (selectsysconfigone == null) {
            log.info("未获取到柯诸钢保数据推送定时任务的配置信息：" + DateUtils.now());
            return;
        }

        JSONObject extra = JSONUtil.parseObj(selectsysconfigone.getExtra());
        if (extra == null || extra.isEmpty() || extra.get("shebeilist") == null) {
            log.info("没有配置要传输柯诸钢保数据的设备：" + DateUtils.now());
            return;
        }
        // 柯诸钢筋仪设备列表
        String shebeilist = extra.getStr("shebeilist");

        // 获取当前设备最新一条记录的id
        Integer curid = selectsysconfigone.getCurid();
        // 查询未推送的柯诸钢保数据
        List<TrGangjinbhcM> gangJinBaoHeChas = trGangjinbhcMService.selectGbList(curid, shebeilist);
        if (gangJinBaoHeChas == null || gangJinBaoHeChas.isEmpty()) {
            log.info("暂无柯诸钢保未推送数据：" + DateUtils.now());
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (TrGangjinbhcM trGangjinbhcM : gangJinBaoHeChas) {
            int id = trGangjinbhcM.getId();
            String projectname = trGangjinbhcM.getProjectname();
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

            // 构造 post 请求体1
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("dbName", userJson.get("dbName"));
            jsonObject1.set("spTypeId", 4);
            Date checktime = trGangjinbhcM.getChecktime();
            jsonObject1.set("testDate", sdf.format(checktime));
            jsonObject1.set("projId", userJson.get("projSubId"));
            jsonObject1.set("wbsId", 0);
            jsonObject1.set("projectName", trGangjinbhcM.getSgbw());
            jsonObject1.set("addUnitId", userJson.get("deptID"));
            jsonObject1.set("addUnitName", userJson.get("deptName"));
            jsonObject1.set("tableId", "194406");
            jsonObject1.set("version", "4.0");
            jsonObject1.set("listId", "1001005174");
            jsonObject1.set("addWay", "1");
            jsonObject1.set("d05", trGangjinbhcM.getShebeiNo());
            jsonObject1.set("TestdataKid", trGangjinbhcM.getTestid());

            String response1 = doPostForm("http://112.95.76.11:8000/gapp/qrtestsourceadd.action", jsonObject1, cookieStr);

            pushandreturnService.saveData(id, String.valueOf(jsonObject1), selectsysconfigone.getRemark(), response1);

                trGangjinbhcM.setIstuisong(trGangjinbhcM.getIstuisong()+1);
            trGangjinbhcMService.saveOrUpdate(trGangjinbhcM);
            JSONObject basicResult = new JSONObject(response1);
            JSONObject dataObj = basicResult.getJSONObject("data");
            String kid = dataObj.getStr("kid");
            // 构造 post 请求体2
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.set("dbName", userJson.get("dbName"));
            jsonObject2.set("parent", kid);
            jsonObject2.set("spTypeId", 4);
            jsonObject2.set("testDate", sdf.format(checktime));
            jsonObject2.set("projId", userJson.get("projSubId"));
            jsonObject2.set("projectName", trGangjinbhcM.getSgbw());
            jsonObject2.set("addUnitId", userJson.get("deptID"));
            jsonObject2.set("addUnitName", userJson.get("deptName"));
            jsonObject2.set("tableId", "194406");
            jsonObject2.set("version", "4.0");
            jsonObject2.set("listId", "1001005175");
            jsonObject2.set("addWay", "1");
            jsonObject2.set("wbsId", 0);
            jsonObject2.set("d23", trGangjinbhcM.getPoint());

            QueryWrapper<TrGangjinbhcS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("testId", trGangjinbhcM.getTestid());
            List<TrGangjinbhcS> list1 = trGangjinbhcSService.list(queryWrapper);
            jsonObject2.set("d01", trGangjinbhcM.getDesignthickness());
            jsonObject2.set("d02", trGangjinbhcM.getTargettype());

            int d = 3;
            int num = 0;
            int pushid =1;
            for (TrGangjinbhcS trGangjinbhcS : list1) {
                num++;
                String strthickness1 = trGangjinbhcS.getStrthickness();
                if (StringUtil.isNotEmpty(strthickness1)) {
                    Integer thickness = trGangjinbhcS.getThickness();
                    String strthickness = trGangjinbhcS.getStrthickness();
                    if (!StringUtils.isNotEmpty(strthickness)) {
                        strthickness = String.valueOf(thickness);
                    }
                    String[] split = strthickness.split(",");
                    String d0 = d < 10 ? "d0" + d : "d" + d;
                    d = d + 1;
                    String d1 = d < 10 ? "d0" + d : "d" + d;

                    jsonObject2.set(d0, split[0]);
                    if (split.length > 1) {
                        jsonObject2.set(d1, split[1]);
                    } else {
                        jsonObject2.set(d1, split[0]);
                    }

                    if ("d22".equals(d1) || num == list1.size()) {

                        jsonObject2.set("TestdataKid", trGangjinbhcM.getTestid()+"-"+pushid);
                        String response2 = doPostForm("http://112.95.76.11:8000/gapp/qrtestsourceadd.action", jsonObject2, cookieStr);

                        pushandreturnService.saveData(id, String.valueOf(jsonObject2), selectsysconfigone.getRemark(), response2);
                        JSONObject detailResult = new JSONObject(response2);
                        String msg = detailResult.getStr("msg");
                        if (msg.contains("true")) {
                            log.info(String.format("柯诸钢筋仪推送成功！id=%s，JSON数据：%s", id, detailResult));
                        } else {
                            log.info(String.format("柯诸钢筋仪推送失败！id=%s，JSON数据：%s", id, detailResult));
                        }
                        pushandreturnService.saveData(id, String.valueOf(jsonObject2), selectsysconfigone.getRemark(), response2);

                        d = 3;

                        for (int i = 3; i <= 22; i++) {
                            String key = "d" + (i < 10 ? "0" + i : i);
                            jsonObject2.remove(key);
                        }
                        pushid++;
                    } else {
                        d++;
                    }
                }else {
                    Integer thickness = trGangjinbhcS.getThickness();
                    String d0 = d < 10 ? "d0" + d : "d" + d;
                    jsonObject2.set(d0, thickness);

                    if ("d22".equals(d0) || num == list1.size()) {

                        jsonObject2.set("TestdataKid", trGangjinbhcM.getTestid()+"-"+pushid);
                        String response2 = doPostForm("http://112.95.76.11:8000/gapp/qrtestsourceadd.action", jsonObject2, cookieStr);
                        JSONObject detailResult = new JSONObject(response2);
                        String msg = detailResult.getStr("msg");
                        if (msg.contains("操作成功")) {
                            log.info(String.format("柯诸钢筋仪推送成功！id=%s，JSON数据：%s", id, detailResult));
                        } else {
                            log.info(String.format("柯诸钢筋仪推送失败！id=%s，JSON数据：%s", id, detailResult));
                        }
                        pushandreturnService.saveData(id, String.valueOf(jsonObject2), selectsysconfigone.getRemark(), response2);

                        d = 3;

                        for (int i = 3; i <= 22; i++) {
                            String key = "d" + (i < 10 ? "0" + i : i);
                            jsonObject2.remove(key);
                        }
                        pushid++;

                    } else {
                        d++;
                    }
                }
            }
            sysConfigService.updateSysConfig(JobUtil.KZ_GJY, id);//根据传过来的唯一值来修改当前判断到的数据id
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
