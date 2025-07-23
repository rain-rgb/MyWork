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
public class zhangalJob implements Job {
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
        List<TrZhanglaM> trZhanglaMS = zhanglaXxbService.selectListm(shebeilist, sysConfig.getCurid());
        if (CollectionUtil.isEmpty(trZhanglaMS)) {
            log.info("暂无柯诸张拉未推送数据：{}", DateUtils.now());
            return;
        }

        for (TrZhanglaM trZhanglaM : trZhanglaMS) {

            int id = trZhanglaM.getId();

            QueryWrapper<TrZhanglaXxb> zhanglaMQueryWrapper = new QueryWrapper<>();
            zhanglaMQueryWrapper.eq("syjid", trZhanglaM.getSyjid())
                    .last("limit 1");
            TrZhanglaXxb zhanglaXxb = zhanglaXxbService.getOne(zhanglaMQueryWrapper);
            String iszl = zhanglaXxb.getIszl();
            if (iszl.equals("0")) {
                continue;
            }
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

            JSONObject detailJson = new JSONObject();
            detailJson.set("dbName", userJson.get("dbName"));
            detailJson.set("parent", iszl);
            detailJson.set("spTypeId", "4");

            String zlsj = trZhanglaM.getZlsj();

            detailJson.set("testDate", zlsj);
            detailJson.set("projId", proid);
            detailJson.set("wbsId", 0);
            detailJson.set("projectName", zhanglaXxb.getGjbh());
            detailJson.set("addUnitId", userJson.get("deptID"));
            detailJson.set("addUnitName", userJson.get("deptName"));
            detailJson.set("tableId", "423515");
            detailJson.set("version", "1.0");
            detailJson.set("listId", "1001005179");
            detailJson.set("Addway", "1");
            String gsbh = trZhanglaM.getGsbh();
            String replace = gsbh.replace("%", "");
            detailJson.set("d10", replace);
            detailJson.set("d11", zhanglaXxb.getZly1());
            detailJson.set("d12", zhanglaXxb.getYbbh1());
            detailJson.set("d13", zhanglaXxb.getBdrq1());
            detailJson.set("d21", zhanglaXxb.getZly2());
            detailJson.set("d22", zhanglaXxb.getYbbh2());
            detailJson.set("d23", zhanglaXxb.getBdrq2());
            detailJson.set("d31", trZhanglaM.getZscl());
            detailJson.set("d32", trZhanglaM.getLlscl());
            detailJson.set("d33", trZhanglaM.getSclper());
            detailJson.set("d34", trZhanglaM.getHtl());
            detailJson.set("d35", trZhanglaM.getWt());
            detailJson.set("d36", trZhanglaM.getSjzll());
            detailJson.set("TestdataKid", trZhanglaM.getFguid());

            QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
            zhanglaSQueryWrapper.eq("fguid", trZhanglaM.getFguid());
            List<TrZhanglaS> zhanglaSList = zhanglaSService.list(zhanglaSQueryWrapper);

            String qdh = "";
            for (TrZhanglaS zhanglaS : zhanglaSList) {
                String dh = zhanglaS.getDh();
                String jdbfb = zhanglaS.getJdbfb();
                String jdzll = zhanglaS.getJdzll();
                String jdscl2 = zhanglaS.getJdscl();
                if (jdzll==null || jdzll.contains("/")) {
                    jdzll = "0";
                }
                if (jdscl2==null || jdscl2.contains("/")) {
                    jdscl2 = "0";
                }
                if (StringUtil.isEmpty(qdh) || qdh.equals(dh)) {
                    qdh = dh;
                    if ("10".equals(jdbfb)) {
                        detailJson.set("d14", jdzll);
                        detailJson.set("d18", jdscl2);
                    }
                    if ("20".equals(jdbfb)) {
//                                detailJson.set("d15", jdzll);
                        detailJson.set("d19", jdscl2);
                    }
                    if ("100".equals(jdbfb)) {
                        detailJson.set("d15", jdzll);
                        detailJson.set("d20", jdscl2);
                    }
                } else {
                    if ("10".equals(jdbfb)) {
                        detailJson.set("d24", jdzll);
                        detailJson.set("d28", jdscl2);
                    }
                    if ("20".equals(jdbfb)) {
//                                detailJson.set("d25", jdzll);
                        detailJson.set("d29", jdscl2);
                    }
                    if ("100".equals(jdbfb)) {
                        detailJson.set("d25", jdzll);
                        detailJson.set("d30", jdscl2);
                    }
                }
            }

            QueryWrapper<TrZhanglaSS> zhanglaSSQueryWrapper = new QueryWrapper<>();
            zhanglaSSQueryWrapper.eq("holeid", trZhanglaM.getHoleid());
            List<TrZhanglaSS> zhanglaSSList = zhanglaSSService.list(zhanglaSSQueryWrapper);
            String zll1 = "";
            String sj1 = "";
            String zll2 = "";
            for (TrZhanglaSS trZhanglaSS : zhanglaSSList) {
                zll1 += trZhanglaSS.getZll1() + ",";
                zll2 += trZhanglaSS.getZll2() + ",";
                sj1 += trZhanglaSS.getJlsj() + ",";
            }

            // 去掉末尾的逗号，如果有的话
            if (!"".equals(zll1)) {
                zll1 = zll1.substring(0, zll1.length() - 1);
            }
            if (!"".equals(zll2)) {
                zll2 = zll2.substring(0, zll2.length() - 1);
            }
            if (!"".equals(sj1)) {
                sj1 = sj1.substring(0, sj1.length() - 1);
            }
            detailJson.set("d95", sj1);
            detailJson.set("d96", zll1);
            detailJson.set("DLineTime", sj1);
            detailJson.set("DLineValue", zll2);

            String back = HTTPUtil.doPostForm("http://112.95.76.11:8000/gapp/qrtestsourceadd.action", detailJson, cookieStr);

            pushandreturnService.saveData(id, String.valueOf(detailJson), sysConfig.getRemark(), back);
            if (back.contains("操作成功") || back.contains("数据已覆盖")) {
                trZhanglaM.setIszl(trZhanglaM.getIszl() +1);
            }else {
                trZhanglaM.setIszl(3);
            }
            zhanglaMService.saveOrUpdate(trZhanglaM);
            sysConfigService.updateSysConfig(JobUtil.KZ_ZL, id);
        }
    }

    private void pdzhangla(TrZhanglaXxb zhanglaXxb) {
        String syjid = zhanglaXxb.getSyjid();

        List<TrZhanglaM> zhanglaMS = zhanglaMService.selectmList(syjid);

        for (TrZhanglaM zhanglaM : zhanglaMS) {

            String fguid = zhanglaM.getFguid();
            String gsbh = zhanglaM.getGsbh();
            QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
            zhanglaSQueryWrapper.eq("fguid", fguid);
            zhanglaSQueryWrapper.eq("gsbh", gsbh);
            List<TrZhanglaS> list = zhanglaSService.list(zhanglaSQueryWrapper);
            if (list.size() > 10) {
                for (int i = 0; i < list.size() / 3 - 1; i++) {
                    TrZhanglaM trZhanglaM = new TrZhanglaM();
                    BeanUtils.copyProperties(zhanglaM, trZhanglaM);
                    String fguidupdate = trZhanglaM.getFguid() + i;
                    trZhanglaM.setFguid(fguidupdate);
                    zhanglaMService.save(trZhanglaM);

                    QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper1 = new QueryWrapper<>();
                    zhanglaSQueryWrapper1.eq("fguid", fguid);
                    zhanglaSQueryWrapper1.eq("gsbh", gsbh);
                    zhanglaSQueryWrapper1.last("LIMIT 3 ,3");
                    List<TrZhanglaS> zhanglaSList = zhanglaSService.list(zhanglaSQueryWrapper1);
                    for (TrZhanglaS zhanglaS : zhanglaSList) {
                        zhanglaS.setFguid(fguidupdate);
                        zhanglaSService.updateById(zhanglaS);
                    }
                }
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
