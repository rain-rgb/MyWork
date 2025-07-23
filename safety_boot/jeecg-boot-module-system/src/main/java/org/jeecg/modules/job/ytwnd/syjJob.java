package org.jeecg.modules.job.ytwnd;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFWangnjService;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import com.trtm.iot.syjoverhandler.service.ISyjOverHandlerService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName syjJob：
 * @Description TODO
 * @Author 55314
 * @Date 2024/3/26 16:44
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class syjJob implements Job {

    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private ITSyjzbService syjzbService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private ISyjOverHandlerService overHandlerService;
    @Autowired
    private IFWangnjService wangnjService;
    @Autowired
    private IFYalijiService yalijiService;

    private String ylurl = "http://183.247.216.148:7030/tz_server/wlw_ylj/pushWlwYlj";
    private String wnurl = "http://183.247.216.148:7030/tz_server/wlw_wnsyj/pushWlwWnsyj";
    private String snurl = "http://183.247.216.148:7030/tz_server/wlw_kzkysyj/pushWlwKzkysyj";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LambdaQueryWrapper<ShebeiInfo> shebeiInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shebeiInfoLambdaQueryWrapper.likeRight(ShebeiInfo::getSysOrgCode, "A05A01A12A12A01")
                .eq(ShebeiInfo::getStatus1, 1);
        List<ShebeiInfo> list = shebeiInfoService.list(shebeiInfoLambdaQueryWrapper);

        // 提取 List 中的 sbno 属性
        List<String> sbnoList = list.stream()
                .map(ShebeiInfo::getSbjno)
                .collect(Collectors.toList());

        // 将 sbnoList 转换为逗号分隔的字符串
        String inClause = sbnoList.stream()
                .map(s -> "'" + s + "'")  // 如果 sbno 是字符串类型，需要加上单引号
                .collect(Collectors.joining(","));

        List<TSyjzb> tSyjzbs = syjzbService.selectSyjListytwnd(inClause);
        if (null == tSyjzbs || tSyjzbs.size() == 0) {
            log.info(String.format("暂无试验机未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (TSyjzb syjzb : tSyjzbs) {
            id = syjzb.getId();
            List sendList = new ArrayList();
            JSONObject sendDate = new JSONObject();
            int sjsl = Integer.valueOf(syjzb.getSjsl());

            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "SYLX");
            SysDict one2 = sysDictService.getOne(sysDictQueryWrapper);
            String id1 = one2.getId();

            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id", id1);
            sysDictItemQueryWrapper.eq("item_value", syjzb.getSylx());
            SysDictItem one3 = sysDictItemService.getOne(sysDictItemQueryWrapper);

            sendDate.set("project_code", "TZ001");
            sendDate.set("equipId", syjzb.getSbbh());

            String wtbh = syjzb.getWtbh();
            String code = "";
            if (StringUtil.isNotEmpty(wtbh)) {
                if (wtbh.contains("-")) {
                    code = syjzb.getWtbh();
                } else {
                    code = syjzb.getSjbh();
                }
            } else {
                code = syjzb.getSjbh();
            }

            sendDate.set("code", code);
            sendDate.set("testname", one3.getItemText());
            String sksjqd = syjzb.getSksjqd();
            String sjqd = syjzb.getSjqd();
            if (!StringUtil.isNotEmpty(sksjqd) && StringUtil.isNotEmpty(sjqd)) {
                String result = sjqd.replaceAll("[^0-9.]", "");
                sksjqd = result;
            }
            sendDate.set("betlev", sksjqd);
            sendDate.set("strength", syjzb.getQddbz());
            sendDate.set("samplenum", syjzb.getSjsl());
            sendDate.set("testdate", syjzb.getSyrq());
            sendDate.set("operator", syjzb.getCzry());
            String pdjg = syjzb.getPdjg();
            int result = 1;
            if ("不合格".equals(pdjg) || "无效".equals(pdjg)) {
                result = 0;
            }
            sendDate.set("result", result);

            QueryWrapper<SyjOverHandler> overHandlerQueryWrapper = new QueryWrapper<>();
            overHandlerQueryWrapper.eq("baseid", syjzb.getSyjid());
            SyjOverHandler one4 = overHandlerService.getOne(overHandlerQueryWrapper);

            int alarmStatus = 0;
            if (one4 != null) {
                alarmStatus = 1;
                sendDate.set("opinion", one4.getHandleWay());
                sendDate.set("closeTime", sdf.format(one4.getHandleTime()));
                sendDate.set("closer", one4.getHandlePerson());
                sendDate.set("attachment", one4.getFilePath2());
            }
            sendDate.set("alarmStatus", alarmStatus);

            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
            shebeiInfoQueryWrapper.eq("sbjno", syjzb.getSbbh());
            ShebeiInfo one1 = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
            Integer sbtype = one1.getSbtype();

            List detailList = new ArrayList();

            if (3 == sbtype) {

                String cjmc = syjzb.getCjmc();
                if (!StringUtil.isNotEmpty(cjmc)) {
                    cjmc = "/";
                }
                sendDate.set("conspos", cjmc);
                sendDate.set("samplebatch", "");
                sendDate.set("betlev", syjzb.getPzbm());
                sendDate.set("strength", syjzb.getQddbz());
                sendDate.set("samplename", "钢筋");
                QueryWrapper<FWangnj> wangnjQueryWrapper = new QueryWrapper<>();
                wangnjQueryWrapper.eq("syjid", syjzb.getSyjid());
                List<FWangnj> FWangnjlist = wangnjService.list(wangnjQueryWrapper);
                if (sjsl > FWangnjlist.size()) {
                    syjzb.setIssend(3);
                    syjzbService.updateById(syjzb);
                    continue;
                }
                for (FWangnj wangnj : FWangnjlist) {
                    JSONObject sonObject = new JSONObject();
                    sonObject.set("code", code);
                    sonObject.set("sjxh", wangnj.getSjxh());
                    sonObject.set("wsbz", wangnj.getWsbz());
                    sonObject.set("dhbz", wangnj.getDhbz());
                    sonObject.set("lz", wangnj.getLz());
                    sonObject.set("lzqd", wangnj.getLzqd());
                    sonObject.set("qflz", wangnj.getQflz());
                    sonObject.set("qfqd", wangnj.getQfqd());
                    sonObject.set("scl", wangnj.getScl());
                    sonObject.set("zdlzscl", wangnj.getZdlzscl());
                    sonObject.set("sysj", wangnj.getSysj());
                    sonObject.set("wcsj", wangnj.getWcsj());
                    sonObject.set("alarmRule", "");

                    detailList.add(sonObject);
                }
                sendDate.set("detailList", detailList);
                String body = HttpRequest.post(wnurl)
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();
                if (body.contains("成功")) {
                    syjzb.setIssend(1);
                } else {
                    syjzb.setIssend(2);
                }
                syjzbService.updateById(syjzb);
                pushandreturnService.saveData(id, String.valueOf(sendDate), "甬台温南段试验机", body);
            }
            if (4 == sbtype) {

                String cjmc = syjzb.getCjmc();
                if (!StringUtil.isNotEmpty(cjmc)) {
                    cjmc = "/";
                }
                sendDate.set("part", cjmc);
                sendDate.set("age", syjzb.getLq());
                sendDate.set("skbh", syjzb.getSjqd());
                sendDate.set("samplename", "水泥混凝土试块");
                QueryWrapper<FsYaliji> yalijiQueryWrapper = new QueryWrapper<>();
                yalijiQueryWrapper.eq("syjid", syjzb.getSyjid());
                List<FsYaliji> FsYalijilist = yalijiService.list(yalijiQueryWrapper);
                if (FsYalijilist.size() == 0 || sjsl > FsYalijilist.size()) {
                    syjzb.setIssend(3);
                    syjzbService.updateById(syjzb);
                    continue;
                }
                String qrcode = FsYalijilist.get(0).getQrcode();
                if (StringUtil.isNotEmpty(qrcode)) {
                    String[] split = qrcode.split("#");
                    if (split.length > 1) {
                        qrcode = split[0];
                    }
                    String url = "http://121.40.163.88:8085/CATDPS/samp.do?getQrCode&qrcode=" + qrcode;
                    String body = GET(url);
                    if (body.contains("成功")) {
                        JSONObject jsonObject = new JSONObject(body);
                        JSONObject obj = jsonObject.getJSONObject("obj");
                        String syzs = obj.getStr("sampleShiYanZuShu");
                        sendDate.set("syzs", syzs);
                        String qyrq = obj.getStr("sampleDate");
                        String sampleName = obj.getStr("sampleName");
                        if (StringUtil.isNotEmpty(sampleName)) {
                            sendDate.set("samplename", sampleName);
                        }
                        sendDate.set("qyrq", qyrq.trim());
                        sendDate.set("yhts", syjzb.getLq());
                        sendDate.set("ckrq", syjzb.getSyrq());
                        sendDate.set("rkrq", qyrq);
                    }
                }
                sendDate.set("qrcode", qrcode);
                for (FsYaliji yaliji : FsYalijilist) {
                    JSONObject sonObject = new JSONObject();
                    sonObject.set("code", code);
                    sonObject.set("sjxh", yaliji.getSjxh());
                    sonObject.set("kylz", yaliji.getKylz());
                    sonObject.set("kyqd", yaliji.getKyqd());
                    sonObject.set("sysj", yaliji.getSysj());
                    sonObject.set("wcsj", yaliji.getWcsj());
                    sonObject.set("alarmRule", "");

                    detailList.add(sonObject);
                }

                sendDate.set("detailList", detailList);
                String body = HttpRequest.post(ylurl)
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();
                if (body.contains("成功")) {
                    syjzb.setIssend(1);
                } else {
                    syjzb.setIssend(2);
                }
                syjzbService.updateById(syjzb);
                pushandreturnService.saveData(id, String.valueOf(sendDate), "甬台温南段试验机", body);
            }
            if (12 == sbtype) {

                String cjmc = syjzb.getCjmc();
                if (!StringUtil.isNotEmpty(cjmc)) {
                    cjmc = "/";
                }
                sendDate.set("part", cjmc);
                sendDate.set("age", syjzb.getLq());
                sendDate.set("skbh", syjzb.getSjqd());
                sendDate.set("samplename", "水泥");
                QueryWrapper<FsYaliji> yalijiQueryWrapper = new QueryWrapper<>();
                yalijiQueryWrapper.eq("syjid", syjzb.getSyjid());
                List<FsYaliji> FsYalijilist = yalijiService.list(yalijiQueryWrapper);
                if (FsYalijilist.size() == 0 || sjsl > FsYalijilist.size()) {
                    syjzb.setIssend(3);
                    syjzbService.updateById(syjzb);
                    continue;
                }
                String qrcode = FsYalijilist.get(0).getQrcode();
                if (StringUtil.isNotEmpty(qrcode)) {
                    String[] split = qrcode.split("#");
                    if (split.length > 1) {
                        qrcode = split[0];
                    }
                    String url = "http://121.40.163.88:8085/CATDPS/samp.do?getQrCode&qrcode=" + qrcode;
                    String body = GET(url);
                    if (body.contains("成功")) {
                        JSONObject jsonObject = new JSONObject(body);
                        JSONObject obj = jsonObject.getJSONObject("obj");
                        String syzs = obj.getStr("sampleShiYanZuShu");
                        sendDate.set("syzs", syzs);
                        String qyrq = obj.getStr("sampleDate");
                        String sampleName = obj.getStr("sampleName");
                        if (StringUtil.isNotEmpty(sampleName)) {
                            sendDate.set("samplename", sampleName);
                        }
                        sendDate.set("qyrq", qyrq.trim());
                        sendDate.set("yhts", syjzb.getLq());
                        sendDate.set("ckrq", syjzb.getSyrq());
                        sendDate.set("rkrq", qyrq);
                    }
                }
                sendDate.set("qrcode", qrcode);
                for (FsYaliji yaliji : FsYalijilist) {
                    JSONObject sonObject = new JSONObject();
                    sonObject.set("code", code);
                    sonObject.set("sjxh", yaliji.getSjxh());
                    sonObject.set("kylz", yaliji.getKylz());
                    sonObject.set("kyqd", yaliji.getKyqd());
                    sonObject.set("sysj", yaliji.getSysj());
                    sonObject.set("wcsj", yaliji.getWcsj());
                    sonObject.set("alarmRule", "");

                    detailList.add(sonObject);
                }

                sendDate.set("detailList", detailList);
                String body = HttpRequest.post(snurl)
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();
                if (body.contains("成功")) {
                    syjzb.setIssend(1);
                } else {
                    syjzb.setIssend(2);
                }
                syjzbService.updateById(syjzb);
                pushandreturnService.saveData(id, String.valueOf(sendDate), "甬台温南段试验机", body);
            }
        }

    }

    public static String GET(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);

            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立实际的连接
            connection.connect();

            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();

            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }

        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
