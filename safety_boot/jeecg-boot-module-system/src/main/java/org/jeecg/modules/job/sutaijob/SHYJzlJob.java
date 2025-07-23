package org.jeecg.modules.job.sutaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhangla.service.ITrZhanglaService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import com.trtm.iot.zhanglass.service.ITrZhanglaSSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.utils.Base64Utils;
import org.apache.logging.log4j.util.Base64Util;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SHYJzlJob：
 * @Description 上海有间张拉推送
 * @Author 55314
 * @Date 2023/2/21 14:57
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SHYJzlJob implements Job {

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
    private IPushandreturnService pushandreturnService;

    private String url = "https://api.ilabx.cn/iot-service/interface/tongWang/DataInfa/SWZLYJ/SaveSWZNZLDataInfa";

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SHYJ_ZL);//上海有间张拉数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到上海有间张拉的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输上海有间张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = zhanglaXxbService.selectListshyj(shebeilist, curid);
        if (null == zhanglaXxbs || zhanglaXxbs.size() == 0) {
            log.info(String.format("暂无上海有间张拉未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        //张拉信息表
        for (TrZhanglaXxb zhanglaXxb : zhanglaXxbs) {
            id = zhanglaXxb.getId();
            JSONObject sendJson = new JSONObject();
            JSONObject SWZLXXBJson = new JSONObject();
            List list = new ArrayList();

            SWZLXXBJson.set("syjid",zhanglaXxb.getSyjid());
            SWZLXXBJson.set("gcmc",zhanglaXxb.getGjmc());
            SWZLXXBJson.set("yzlc",zhanglaXxb.getYzlc());
            SWZLXXBJson.set("gjbh",zhanglaXxb.getGjbh());
            SWZLXXBJson.set("shebeibianhao",zhanglaXxb.getShebeibianhao());
            SWZLXXBJson.set("gjmc",zhanglaXxb.getGjmc());
            SWZLXXBJson.set("snsjqd",zhanglaXxb.getSnsjqd());
            SWZLXXBJson.set("sgsj",zhanglaXxb.getSgsj());
            SWZLXXBJson.set("snskqd",zhanglaXxb.getSnskqd());
            SWZLXXBJson.set("zly1",zhanglaXxb.getZly1());
            SWZLXXBJson.set("ybbh1",zhanglaXxb.getYbbh1());
            SWZLXXBJson.set("bdrq1",zhanglaXxb.getBdrq1());
            SWZLXXBJson.set("zly2",zhanglaXxb.getZly2());
            SWZLXXBJson.set("ybbh2",zhanglaXxb.getYbbh2());
            SWZLXXBJson.set("bdrq2",zhanglaXxb.getBdrq2());
            SWZLXXBJson.set("zlcsu",zhanglaXxb.getZlcsu());
            SWZLXXBJson.set("zlcsk",zhanglaXxb.getZlcsk());
            SWZLXXBJson.set("zlcsep",zhanglaXxb.getZlcsep());
            SWZLXXBJson.set("zlbwpic",zhanglaXxb.getZlbwpic());
            SWZLXXBJson.set("kualiang",zhanglaXxb.getKualiang());
            SWZLXXBJson.set("scljss",zhanglaXxb.getScljss());
            SWZLXXBJson.set("fmqkms",zhanglaXxb.getFmqkms());
            SWZLXXBJson.set("cbunit",zhanglaXxb.getCbunit());
            SWZLXXBJson.set("jlunit",zhanglaXxb.getJlunit());
            SWZLXXBJson.set("htbh",zhanglaXxb.getHtbh());
            SWZLXXBJson.set("memo",zhanglaXxb.getMemo());
            SWZLXXBJson.set("status",zhanglaXxb.getStatus());

            list.add(SWZLXXBJson);
            sendJson.set("Method","SWZLXXB");
            sendJson.set("data",list);
//            String encode = encodeToBase64(sendJson.toString());
//            //推送
//            String result = HttpRequest.post(url)
//                    .header("Content-Type", "text/html")
//                    .body(encode)
//                    .execute()
//                    .body();
            String s = encodeToBase64(String.valueOf(sendJson));
            String result = doPost(s);
            pushandreturnService.saveData(id,String.valueOf(sendJson),selectsysconfigone.getRemark(),result);

            sysConfigService.updateSysConfig(JobUtil.SHYJ_ZL, id);//根据传过来的唯一值来修改当前判断到的数据id
            //张拉主表
            QueryWrapper<TrZhanglaM> zhanglaMQueryWrapper = new QueryWrapper<>();
            zhanglaMQueryWrapper.eq("syjid",zhanglaXxb.getSyjid());

            List<TrZhanglaM> TrZhanglaMList = zhanglaMService.list(zhanglaMQueryWrapper);
            List Mlist = new ArrayList();
            JSONObject sendMJson = new JSONObject();

            for (TrZhanglaM zhanglaM : TrZhanglaMList){
                JSONObject ZhanglaMJson = new JSONObject();
                ZhanglaMJson.set("f_guid",zhanglaM.getFguid());
                ZhanglaMJson.set("syjid",zhanglaM.getSyjid());
                ZhanglaMJson.set("shebeibianhao",zhanglaM.getShebeibianhao());
                ZhanglaMJson.set("zlsj",zhanglaM.getZlsj());
                ZhanglaMJson.set("gsbh",zhanglaM.getGsbh());
                ZhanglaMJson.set("gsgs",zhanglaM.getGsgs());
                ZhanglaMJson.set("txml",zhanglaM.getTxml());
                ZhanglaMJson.set("sjzll",zhanglaM.getSjzll());
                ZhanglaMJson.set("htl",zhanglaM.getHtl());
                ZhanglaMJson.set("zscl",zhanglaM.getZscl());
                ZhanglaMJson.set("llscl",zhanglaM.getLlscl());
                ZhanglaMJson.set("yxpc",zhanglaM.getYxpc());
                ZhanglaMJson.set("sclper",zhanglaM.getSclper());
                ZhanglaMJson.set("wt",zhanglaM.getWt());
                ZhanglaMJson.set("clqk",zhanglaM.getClqk());
                ZhanglaMJson.set("memo",zhanglaM.getMemo());
                ZhanglaMJson.set("hege",zhanglaM.getHege());
                ZhanglaMJson.set("yzlb",zhanglaM.getYzlb());
                ZhanglaMJson.set("czlb",zhanglaM.getCzlb());
                ZhanglaMJson.set("zzlb",zhanglaM.getZzlb());
                ZhanglaMJson.set("jlsj",zhanglaM.getJlsj());
                ZhanglaMJson.set("holeid",zhanglaM.getHoleid());
                ZhanglaMJson.set("status",zhanglaM.getStatus());
                Mlist.add(ZhanglaMJson);

                QueryWrapper<TrZhanglaSS> zhanglaSSQueryWrapper = new QueryWrapper<>();
                zhanglaSSQueryWrapper.eq("holeid",zhanglaM.getHoleid());

                List<TrZhanglaSS> TrZhanglaSSList = zhanglaSSService.list(zhanglaSSQueryWrapper);
                if (TrZhanglaSSList.size()>0){
                    List SSlist = new ArrayList();
                    JSONObject sendSSJson = new JSONObject();
                    for (TrZhanglaSS trZhanglaSS : TrZhanglaSSList){
                        JSONObject ZhanglaSSJson = new JSONObject();
                        ZhanglaSSJson.set("ssid",trZhanglaSS.getSsid());
                        ZhanglaSSJson.set("shebeibianhao",trZhanglaSS.getShebeibianhao());
                        ZhanglaSSJson.set("jlsj",trZhanglaSS.getJlsj());
                        ZhanglaSSJson.set("holeid",trZhanglaSS.getHoleid());
                        ZhanglaSSJson.set("zlcs",trZhanglaSS.getZlcs());
                        ZhanglaSSJson.set("zt1",trZhanglaSS.getZt1());
                        ZhanglaSSJson.set("zll1",trZhanglaSS.getZll1());
                        ZhanglaSSJson.set("yy1",trZhanglaSS.getYy1());
                        ZhanglaSSJson.set("dxc1",trZhanglaSS.getDxc1());
                        ZhanglaSSJson.set("scl1",trZhanglaSS.getScl1());
                        ZhanglaSSJson.set("zt2",trZhanglaSS.getZt2());
                        ZhanglaSSJson.set("zll2",trZhanglaSS.getZll2());
                        ZhanglaSSJson.set("yy2",trZhanglaSS.getYy2());
                        ZhanglaSSJson.set("dxc2",trZhanglaSS.getDxc2());
                        ZhanglaSSJson.set("scl2",trZhanglaSS.getScl2());
                        SSlist.add(ZhanglaSSJson);
                    }

                    sendSSJson.set("Method","SWZLSY_S_S");
                    sendSSJson.set("data",SSlist);
//                    String SSencode = encodeToBase64(sendSSJson.toString());
//                    //推送
//                    String SSresult = HttpRequest.post(url)
//                            .header("Content-Type", "text/html")
//                            .body(SSencode)
//                            .execute()
//                            .body();
                    String ss = encodeToBase64(String.valueOf(sendSSJson));
                    String SSresult = doPost(ss);
                    pushandreturnService.saveData(id,String.valueOf(sendSSJson),selectsysconfigone.getRemark(),SSresult);
                }else {
                    System.out.println("无过程数据");
                }
            }

            sendMJson.set("Method","SWZLSY_M");
            sendMJson.set("data",Mlist);
//            String Mencode = encodeToBase64(sendMJson.toString());
//            //推送
//            String Mresult = HttpRequest.post(url)
//                    .header("Content-Type", "text/html")
//                    .body(Mencode)
//                    .execute()
//                    .body();
            String m = encodeToBase64(String.valueOf(sendMJson));
            String Mresult = doPost(m);
            pushandreturnService.saveData(id,String.valueOf(sendMJson),selectsysconfigone.getRemark(),Mresult);

            //张拉子表
            QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
            zhanglaSQueryWrapper.eq("syjid",zhanglaXxb.getSyjid());

            List<TrZhanglaS> TrZhanglaSList = zhanglaSService.list(zhanglaSQueryWrapper);
            List Slist = new ArrayList();
            JSONObject sendSJson = new JSONObject();

            for (TrZhanglaS zhanglaS : TrZhanglaSList){
                JSONObject ZhanglaSJson = new JSONObject();
                ZhanglaSJson.set("sid",zhanglaS.getSid());
                ZhanglaSJson.set("f_guid",zhanglaS.getFguid());
                ZhanglaSJson.set("syjid",zhanglaS.getSyjid());
                ZhanglaSJson.set("gsbh",zhanglaS.getGsbh());
                ZhanglaSJson.set("JDBFB",zhanglaS.getJdbfb());
                ZhanglaSJson.set("YBDS",zhanglaS.getYbds());
                ZhanglaSJson.set("JDZLL",zhanglaS.getJdzll());
                ZhanglaSJson.set("JDSCL",zhanglaS.getJdscl());
                ZhanglaSJson.set("DH",zhanglaS.getDh());
                ZhanglaSJson.set("ljhsl",zhanglaS.getLjhsl());
                ZhanglaSJson.set("chsj",zhanglaS.getChsj());
                ZhanglaSJson.set("dcscl",zhanglaS.getDcscl());
                ZhanglaSJson.set("status",zhanglaS.getStatus());
                Slist.add(ZhanglaSJson);
            }

            sendSJson.set("Method","SWZLSY_S");
            sendSJson.set("data",Slist);
//            String Sencode = encodeToBase64(sendSJson.toString());
//            //推送
//            String Sresult = HttpRequest.post(url)
//                    .header("Content-Type", "text/html")
//                    .body(Sencode)
//                    .execute()
//                    .body();

            String sss = encodeToBase64(String.valueOf(sendSJson));
            String Sresult = doPost(sss);
            pushandreturnService.saveData(id,String.valueOf(sendSJson),selectsysconfigone.getRemark(),Sresult);

        }
    }

    private static String encodeToBase64(String jsonString) throws UnsupportedEncodingException {
        byte[] bytes = jsonString.getBytes("UTF-8");
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String doPostForm(Map params) throws IOException {
        HttpURLConnection connection = null;
        OutputStream os = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = "";
        try {
            URL url = new URL("https://api.ilabx.cn/iot-service/interface/tongWang/DataInfa/SWZLYJ/SaveSWZNZLDataInfa");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("Content-Type", "text/html");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            os = connection.getOutputStream();
            os.write(params.toString().getBytes(StandardCharsets.UTF_8));
            is = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                result += line + "\n";
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param httpUrl
     *            发送请求的 URL
     * @param param
     *            请求参数应该是{"key":"==g43sEvsUcbcunFv3mHkIzlHO4iiUIT R7WwXuSVKTK0yugJnZSlr6qNbxsL8OqCUAFyCDCoRKQ882m6cTTi0q9uCJsq JJvxS+8mZVRP/7lWfEVt8/N9mKplUA68SWJEPSXyz4MDeFam766KEyvqZ99d"}的形式。
     * @return 所表明远程资源的响应结果
     */
    public static String doPost(String param) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL("https://api.ilabx.cn/iot-service/interface/tongWang/DataInfa/SWZLYJ/SaveSWZNZLDataInfa");
            // 经过远程url链接对象打开链接
            connection = (HttpURLConnection) url.openConnection();
            // 设置链接请求方式
            connection.setRequestMethod("POST");
            // 设置链接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);
            // 默认值为：false，当向远程服务器传送数据/写数据时，须要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数无关紧要
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 "text/html" 的形式。
            connection.setRequestProperty("Content-Type", "text/html");
            // 经过链接对象获取一个输出流
            os = connection.getOutputStream();
            // 经过输出流对象将参数写出去/传输出去,它是经过字节数组写出的
            os.write(param.getBytes());
            // 经过链接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工做项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的链接
            connection.disconnect();
        }
        return result;
    }
}
