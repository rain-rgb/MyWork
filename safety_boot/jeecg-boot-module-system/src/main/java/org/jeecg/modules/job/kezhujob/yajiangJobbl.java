package org.jeecg.modules.job.kezhujob;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.xkcoding.http.util.StringUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName yajiangJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/4/11 10:40
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yajiangJobbl implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.KZ_YJBL);//柯诸压浆
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到柯诸压浆的配置信息" + DateUtils.now()));
            return;
        }
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (jsonObject == null || jsonObject.isEmpty()) {
            log.info("没有配置要传输柯诸压浆的设备：{}", DateUtils.now());
            return;
        }

        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> trYajiangMS = yajiangMService.selectyjblList(shebeilist, selectsysconfigone.getCurid());
        if (CollectionUtil.isEmpty(trYajiangMS)) {
            log.info("暂无柯诸压浆未推送数据：{}", DateUtils.now());
            return;
        }
        for (TrYajiangM trYajiangM : trYajiangMS) {
            int id = trYajiangM.getId();

            // 获取设备所属机构
            String shebeibianhao = trYajiangM.getYjsbbh();
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
            shebeiInfoQueryWrapper.select("sys_org_code");
            shebeiInfoQueryWrapper.eq("sbjno", shebeibianhao);
            ShebeiInfo shebeiInfo = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
            String sysOrgCode = shebeiInfo.getSysOrgCode();

            // 根据机构编码选择用户进行登录
            String userId = "13302948";
            if (sysOrgCode.contains("A05A01A04A10A01A01")) {
                userId = "13302945";
            }

            // URL
            String url = String.format("http://112.95.76.11:8000/gapp/userlogin.action?userId=%s&userPass=d7b92ac90e93355ffeba35186268f43b&loginTag=0", userId);

            // 获取用户信息
            JSONObject userJson = user(url);

            String cookieStr = "";
            try {
                // 发送POST请求获取Cookie值
                Map<String, List<String>> responseHeader = sendPostGetCookie(url);
                List<String> cookieList = responseHeader.getOrDefault("Set-Cookie", new ArrayList<>());
                cookieStr = StringUtils.join(cookieList, ";");
                cookieStr = cookieStr.replace(" Path=/gapp; HttpOnly;", "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject basicJson = new JSONObject();
            basicJson.set("dbName", userJson.get("dbName"));
            basicJson.set("spTypeId", "4");

            String originalDateStr = trYajiangM.getYjsj();

            // 将格式化后的日期字符串设置到 basicJson 中
            basicJson.set("testDate", originalDateStr);
            basicJson.set("projId", userJson.get("projSubId"));
            basicJson.set("wbsId", 0);
            basicJson.set("projectName", trYajiangM.getLianghao());
            basicJson.set("addUnitId", userJson.get("deptID"));
            basicJson.set("addUnitName", userJson.get("deptName"));
            basicJson.set("tableId", "423516");
            basicJson.set("version", "1.0");
            basicJson.set("listId", "1001005180");
            basicJson.set("Addway", "1");
            basicJson.set("d02", trYajiangM.getLianghao());
            basicJson.set("d03", trYajiangM.getLblx());
            basicJson.set("d04", trYajiangM.getGjjg());
            basicJson.set("d05", trYajiangM.getGjbh());
            basicJson.set("d06", trYajiangM.getSnmc());
            basicJson.set("d07", trYajiangM.getSnmc());
            basicJson.set("d09", trYajiangM.getYajiangji());
            basicJson.set("TestdataKid", trYajiangM.getSyjid());
            String cjsjl = trYajiangM.getCjsjl();
            String cpzjl = trYajiangM.getCpzjl();
            double sum = 0.0;
            try {
                boolean containsNumber = cjsjl.matches(".*\\d+.*");
                boolean containsNumber1 = cpzjl.matches(".*\\d+.*");
                if (containsNumber) {
                    sum += Double.parseDouble(cjsjl);
                }
                if (containsNumber1) {
                    sum += Double.parseDouble(cpzjl);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            basicJson.set("d10", sum);
            basicJson.set("d11", trYajiangM.getZlsj());
            basicJson.set("d12", trYajiangM.getShuijiaobi());
            basicJson.set("d13", trYajiangM.getYajiangbuzh());
            basicJson.set("d14", trYajiangM.getYjsj());

            String basicResponse = HTTPUtil.doPostForm("http://112.95.76.11:8000/gapp/qrtestsourceadd.action", basicJson, cookieStr);

            pushandreturnService.saveData(id, String.valueOf(basicJson), selectsysconfigone.getRemark(), basicResponse);

            trYajiangM.setIsmt(trYajiangM.getIsmt()+1);
            yajiangMService.saveOrUpdate(trYajiangM);

            JSONObject basicResult = new JSONObject(basicResponse);
            JSONObject dataObj = basicResult.getJSONObject("data");
            String kid = dataObj.getStr("kid");

            QueryWrapper<TrYajiangS> yajiangSQueryWrapper = new QueryWrapper<>();
            yajiangSQueryWrapper.eq("syjid",trYajiangM.getSyjid());
            List<TrYajiangS> yajiangSList = yajiangSService.list(yajiangSQueryWrapper);
            for (TrYajiangS trYajiangS : yajiangSList) {
                JSONObject detailJson = new JSONObject();
                detailJson.set("dbName", userJson.get("dbName"));
                detailJson.set("parent", kid);
                detailJson.set("spTypeId", "4");
                String zlsj = trYajiangM.getYjsj();

                detailJson.set("testDate", zlsj);
                detailJson.set("projId", userJson.get("projSubId"));
                detailJson.set("wbsId", 0);
                detailJson.set("projectName", trYajiangM.getLianghao());
                detailJson.set("addUnitId", userJson.get("deptID"));
                detailJson.set("addUnitName", userJson.get("deptName"));
                detailJson.set("tableId", "423516");
                detailJson.set("version", "1.0");
                detailJson.set("listId", "1001005181");
                detailJson.set("Addway", "1");
                detailJson.set("d01", trYajiangS.getKongdao());
                detailJson.set("d02", trYajiangM.getYajiangguoc());
                detailJson.set("d03", trYajiangS.getStarttime());
                detailJson.set("d04", trYajiangS.getEndtime());
                detailJson.set("d05", trYajiangS.getJinjiangyal());
                detailJson.set("d06", trYajiangS.getTongguo());
                detailJson.set("d07", trYajiangS.getMjqk());

                String chixushijia = trYajiangS.getChixushijia();
                double chixuMinutes = 0.0;
                if (StringUtil.isNotEmpty(chixushijia)){
                    int chixuSeconds = Integer.parseInt(trYajiangS.getChixushijia());
                    chixuMinutes = chixuSeconds / 60.0;
                }

                detailJson.set("d08", chixuMinutes);
                detailJson.set("d13", trYajiangS.getJiaobansj());
                detailJson.set("d14", trYajiangS.getJinjiangshu());
                detailJson.set("d15", trYajiangS.getJinjiangyal());
                detailJson.set("TestdataKid", trYajiangS.getFguid());

                String back = HTTPUtil.doPostForm("http://112.95.76.11:8000/gapp/qrtestsourceadd.action", detailJson, cookieStr);
                JSONObject detailResult = new JSONObject(back);
                String msg = detailResult.getStr("msg");
                trYajiangS.setIszl(trYajiangS.getIszl()+1);
                yajiangSService.saveOrUpdate(trYajiangS);

                sysConfigService.updateSysConfig(JobUtil.KZ_YJBL, id);
                pushandreturnService.saveData(id, String.valueOf(detailJson), selectsysconfigone.getRemark(), back);
            }
        }
    }

    public Map<String, List<String>> sendPostGetCookie(String urlpath) throws Exception {
        try {
            URL url = new URL(urlpath);
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


    public JSONObject user(String url) {
        HttpResponse response = HttpRequest.get(url)
                .timeout(10000)
                .execute();
        if (response.getStatus() != 200) {
            throw new RuntimeException("请求失败，状态码：" + response.getStatus());
        }
        JSONObject jsonObject = JSONUtil.parseObj(response.body());
        return jsonObject.getJSONObject("data");
    }
}

