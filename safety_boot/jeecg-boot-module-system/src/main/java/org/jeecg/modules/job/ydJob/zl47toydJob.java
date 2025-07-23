package org.jeecg.modules.job.ydJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @ClassName zl47toydJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/9/20 9:58
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zl47toydJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaService;

    private String url = "http://47.97.173.113:8081/yd/SaveSWZNZLDataInfaZLController.do?upload";

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YD_ZL47);//杭绍甬张拉推送阳光工程
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到张拉定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = zhanglaXxbService.selectzl47toyd(shebeilist, curid);
        if (null == zhanglaXxbs || zhanglaXxbs.size() == 0) {
            log.info(String.format("暂无张拉未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrZhanglaXxb zhanglaXxb : zhanglaXxbs) {
            id = zhanglaXxb.getId();
            JSONObject sendObject = JSONUtil.createObj();
            sendObject.set("syjid",zhanglaXxb.getSyjid());
            sendObject.set("gcmc",zhanglaXxb.getGcmc());
            sendObject.set("yzlc",zhanglaXxb.getYzlc());
            sendObject.set("gjbh",zhanglaXxb.getGjbh());
            sendObject.set("shebeibianhao",zhanglaXxb.getShebeibianhao());
            sendObject.set("gjmc",zhanglaXxb.getGjmc());
            sendObject.set("snsjqd",zhanglaXxb.getSnsjqd());
            sendObject.set("sgsj",zhanglaXxb.getSgsj());
            sendObject.set("snskqd",zhanglaXxb.getSnskqd());
            sendObject.set("zly1",zhanglaXxb.getZly1());
            sendObject.set("ybbh1",zhanglaXxb.getYbbh1());
            sendObject.set("bdrq1",zhanglaXxb.getBdrq1());
            sendObject.set("zly2",zhanglaXxb.getZly2());
            sendObject.set("ybbh2",zhanglaXxb.getYbbh2());
            sendObject.set("bdrq2",zhanglaXxb.getBdrq2());
            sendObject.set("zlcsu",zhanglaXxb.getZlcsu());
            sendObject.set("zlcsk",zhanglaXxb.getZlcsk());
            sendObject.set("zlcsep",zhanglaXxb.getZlcsep());
            sendObject.set("zlbwpic",zhanglaXxb.getZlbwpic());
            sendObject.set("kualiang",zhanglaXxb.getKualiang());
            sendObject.set("scljss",zhanglaXxb.getScljss());
            sendObject.set("fmqkms",zhanglaXxb.getFmqkms());
            sendObject.set("cbunit",zhanglaXxb.getCbunit());
            sendObject.set("jlunit",zhanglaXxb.getJlunit());
            sendObject.set("htbh",zhanglaXxb.getHtbh());
            sendObject.set("memo",zhanglaXxb.getMemo());
            sendObject.set("status",1);

            JSONObject zlxxsendObject = JSONUtil.createObj();
            List listxx = new ArrayList();
            listxx.add(sendObject);
            zlxxsendObject.set("Method","SWZLXXB");
            zlxxsendObject.set("data",listxx);
            String zlxxsendObjectBase64 = Base64.getEncoder().encodeToString(zlxxsendObject.toString().getBytes(StandardCharsets.UTF_8));


            String back = post(zlxxsendObjectBase64);
            if (back.contains("true")){
                log.info("47推送义东老平台成功！");
                zhanglaXxb.setIssend(1);
            }else {
                log.info("47推送义东老平台失败！");
                zhanglaXxb.setIssend(2);
            }
            zhanglaXxbService.updateById(zhanglaXxb);
            QueryWrapper<TrZhanglaM> zhanglaMQueryWrapper = new QueryWrapper<>();
            zhanglaMQueryWrapper.eq("syjid", zhanglaXxb.getSyjid());
            List<TrZhanglaM> zhanglaMList = zhanglaMService.list(zhanglaMQueryWrapper);
            List listm = new ArrayList();
            for (TrZhanglaM trZhanglaM : zhanglaMList) {
                JSONObject zhanglaMObject = JSONUtil.createObj();
                zhanglaMObject.set("f_guid",trZhanglaM.getFguid());
                zhanglaMObject.set("syjid",trZhanglaM.getSyjid());
                zhanglaMObject.set("shebeibianhao",trZhanglaM.getShebeibianhao());
                zhanglaMObject.set("zlsj",trZhanglaM.getZlsj());
                zhanglaMObject.set("gsbh",trZhanglaM.getGsbh());
                zhanglaMObject.set("gsgs",trZhanglaM.getGsgs());
                zhanglaMObject.set("txml",trZhanglaM.getTxml());
                zhanglaMObject.set("sjzll",trZhanglaM.getSjzll());
                zhanglaMObject.set("htl",trZhanglaM.getHtl());
                zhanglaMObject.set("zscl",trZhanglaM.getZscl());
                zhanglaMObject.set("llscl",trZhanglaM.getLlscl());
                zhanglaMObject.set("yxpc",trZhanglaM.getYxpc());
                zhanglaMObject.set("sclper",trZhanglaM.getSclper());
                zhanglaMObject.set("wt",trZhanglaM.getWt());
                zhanglaMObject.set("clqk",trZhanglaM.getClqk());
                zhanglaMObject.set("memo",trZhanglaM.getMemo());
                zhanglaMObject.set("hege",trZhanglaM.getHege());
                zhanglaMObject.set("yzlb",trZhanglaM.getYzlb());
                zhanglaMObject.set("czlb",trZhanglaM.getCzlb());
                zhanglaMObject.set("zzlb",trZhanglaM.getZzlb());
                zhanglaMObject.set("jlsj",trZhanglaM.getJlsj());
                zhanglaMObject.set("holeid",trZhanglaM.getHoleid());
                zhanglaMObject.set("status",1);
                sendObject.set("isbzb","1");

                listm.add(zhanglaMObject);

                List zlsList = new ArrayList();
                QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
                zhanglaSQueryWrapper.eq("fguid", trZhanglaM.getFguid());
                List<TrZhanglaS> zhanglaSList = zhanglaService.list(zhanglaSQueryWrapper);
                for (TrZhanglaS trZhanglaS : zhanglaSList) {
                    JSONObject zhanglaSObject = JSONUtil.createObj();

                    zhanglaSObject.set("sid",trZhanglaS.getSid());
                    zhanglaSObject.set("f_guid",trZhanglaS.getFguid());
                    zhanglaSObject.set("syjid",trZhanglaS.getSyjid());
                    zhanglaSObject.set("gsbh",trZhanglaS.getGsbh());
                    zhanglaSObject.set("JDBFB",trZhanglaS.getJdbfb());
                    zhanglaSObject.set("YBDS",trZhanglaS.getYbds());
                    zhanglaSObject.set("JDZLL",trZhanglaS.getJdzll());
                    zhanglaSObject.set("JDSCL",trZhanglaS.getJdscl());
                    zhanglaSObject.set("DH",trZhanglaS.getDh());
                    zhanglaSObject.set("ljhsl",trZhanglaS.getLjhsl());
                    zhanglaSObject.set("chsj",trZhanglaS.getChsj());
                    zhanglaSObject.set("dcscl",trZhanglaS.getDcscl());
                    zhanglaSObject.set("status",1);

                    zlsList.add(zhanglaSObject);
                }
                JSONObject zlssendObject = JSONUtil.createObj();
                zlssendObject.set("Method","SWZLSY_S");
                zlssendObject.set("data",zlsList);
                String zlssendObjectBase64 = Base64.getEncoder().encodeToString(zlssendObject.toString().getBytes(StandardCharsets.UTF_8));
                String backs = post(zlssendObjectBase64);
                if (backs.contains("true")){
                    log.info("47推送义东老平台成功！");
                }else {
                    log.info("47推送义东老平台失败！");
                }
            }
                JSONObject zlmsendObject = JSONUtil.createObj();
                zlmsendObject.set("Method","SWZLSY_M");
                zlmsendObject.set("data",listm);
                String zlmsendObjectBase64 = Base64.getEncoder().encodeToString(zlmsendObject.toString().getBytes(StandardCharsets.UTF_8));

            String backm = post(zlmsendObjectBase64);
            if (backm.contains("true")){
                log.info("47推送义东老平台成功！");
            }else {
                log.info("47推送义东老平台失败！");
            }

            sysConfigService.updateSysConfig(JobUtil.YD_ZL47, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }

    public String post(String jsonBody) throws Exception {

        // 将 JSON 转换为字符串
        String jsonString = jsonBody;

        // 创建连接
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // 设置请求方法为 POST
        con.setRequestMethod("POST");

        // 设置 Content-Type 为 application/json
        con.setRequestProperty("Content-Type", "application/json");

        // 开启输出流，以便向接口发送数据
        con.setDoOutput(true);

        // 获取输出流
        OutputStream os = con.getOutputStream();

        // 向输出流写入 JSON 字符串
        os.write(jsonString.getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();

        // 发起请求并获取响应结果
        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // 输出响应结果
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response: " + response.toString());
        return response.toString();
    }
}
