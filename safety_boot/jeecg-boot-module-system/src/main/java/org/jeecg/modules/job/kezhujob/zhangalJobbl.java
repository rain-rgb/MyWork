package org.jeecg.modules.job.kezhujob;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.shebeiinfo.service.SheBeiInfoService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import com.trtm.iot.zhanglass.service.ITrZhanglaSSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import com.xkcoding.http.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName zhangalJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/10/31 15:03
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zhangalJobbl implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaSService;
    @Autowired
    private ITrZhanglaSSService zhanglaSSService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        int configName = JobUtil.KZ_ZL;
        SysConfig sysConfig = sysConfigService.selectsysconfigone(configName);
        if (sysConfig == null) {
            log.info("未获取到柯诸张拉的配置信息：{}", DateUtils.now());
            return;
        }

        String extra = sysConfig.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (jsonObject == null || jsonObject.isEmpty()) {
            log.info("没有配置要传输柯诸张拉的设备：{}", DateUtils.now());
            return;
        }

        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = zhanglaXxbService.selectListsss(shebeilist, sysConfig.getCurid());
        if (CollectionUtil.isEmpty(zhanglaXxbs)) {
            log.info("暂无柯诸张拉未推送数据：{}", DateUtils.now());
            return;
        }

        for (TrZhanglaXxb zhanglaXxb : zhanglaXxbs) {

            int id = zhanglaXxb.getId();

            QueryWrapper<TrZhanglaM> zhanglaMQueryWrapper = new QueryWrapper<>();
            zhanglaMQueryWrapper.eq("syjid", zhanglaXxb.getSyjid());
            List<TrZhanglaM> zhanglaMList = zhanglaMService.list(zhanglaMQueryWrapper);

            if (zhanglaMList.size() > 0) {
                // 获取设备所属机构
                String shebeibianhao = zhanglaXxb.getShebeibianhao();
                QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
                shebeiInfoQueryWrapper.select("sys_org_code");
                shebeiInfoQueryWrapper.eq("sbjno", shebeibianhao);
                ShebeiInfo shebeiInfo = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
                String sysOrgCode = shebeiInfo.getSysOrgCode();

                // 根据机构编码选择用户进行登录
                String userId = "13302948";
                String proid = "81504";
                if (sysOrgCode.contains("A05A01A04A10A01A01")) {
                    userId = "13302945";
                    proid = "81501";
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
                    cookieStr = cookieStr.replaceAll(" Path=/gapp; HttpOnly;", "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONObject basicJson = new JSONObject();
                basicJson.set("dbName", userJson.get("dbName"));
                basicJson.set("spTypeId", "4");

                String originalDateStr = zhanglaXxb.getSgsj();

                // 将格式化后的日期字符串设置到 basicJson 中
                basicJson.set("testDate", originalDateStr);
                basicJson.set("projId", proid);
                basicJson.set("wbsId", 0);
                basicJson.set("projectName", zhanglaXxb.getGjbh());
                basicJson.set("addUnitId", userJson.get("deptID"));
                basicJson.set("addUnitName", userJson.get("deptName"));
                basicJson.set("tableId", "423515");
                basicJson.set("version", "1.0");
                basicJson.set("listId", "1001004975");
                basicJson.set("Addway", "1");
                basicJson.set("d02", zhanglaXxb.getGcmc());
                String substring = originalDateStr.substring(0, 10);
                basicJson.set("d03", substring);
                basicJson.set("d05", zhanglaXxb.getSnskqd());
                basicJson.set("d08", zhanglaXxb.getSnsjqd());
                basicJson.set("d09", zhanglaMList.get(0).getSjzll());
                basicJson.set("d10", zhanglaMList.get(0).getSclper());
                basicJson.set("TestdataKid", zhanglaXxb.getSyjid());

                String basicResponse = HTTPUtil.doPostForm("http://112.95.76.11:8000/gapp/qrtestsourceadd.action", basicJson, cookieStr);

                pushandreturnService.saveData(id, String.valueOf(basicJson), sysConfig.getRemark(), basicResponse);
                JSONObject basicResult = new JSONObject(basicResponse);
                JSONObject dataObj = basicResult.getJSONObject("data");
                String kid = dataObj.getStr("kid");

                zhanglaXxb.setIszl(kid);
                zhanglaXxbService.saveOrUpdate(zhanglaXxb);
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
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(response.body());
        return jsonObject.getJSONObject("data");
    }
}
