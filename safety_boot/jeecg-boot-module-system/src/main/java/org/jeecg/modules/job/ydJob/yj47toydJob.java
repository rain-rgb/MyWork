package org.jeecg.modules.job.ydJob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
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
 * @ClassName yj47toydJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/9/20 14:03
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yj47toydJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private ITrYajiangSService yajiangSService;

    private String url = "http://47.97.173.113:8081/yd/SaveSWZNYJDataInfaYJController.do?upload";

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YD_YJ47);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到压浆定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> trYajiangMS = yajiangMService.selectyj47toyd(shebeilist, curid);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrYajiangM yajiangM : trYajiangMS) {
            JSONObject sendObject = JSONUtil.createObj();
            id = yajiangM.getId();
            sendObject.set("syjid",yajiangM.getSyjid());
            sendObject.set("shebeibianhao",yajiangM.getYjsbbh());
            sendObject.set("sgdw",yajiangM.getSgdw());
            sendObject.set("jldw",yajiangM.getJldw());
            sendObject.set("htbh",yajiangM.getHtbh());
            sendObject.set("gcmc",yajiangM.getGcmc());
            sendObject.set("yjsj",yajiangM.getYjsj());
            sendObject.set("zhbw",yajiangM.getZhbw());
            sendObject.set("sgbw",yajiangM.getSgbw());
            sendObject.set("gjjg",yajiangM.getGjjg());
            sendObject.set("gjbh",yajiangM.getGjbh());
            sendObject.set("qw",yajiangM.getQw());
            sendObject.set("cjsjl",yajiangM.getCjsjl());
            sendObject.set("cpzjl",yajiangM.getCpzjl());
            sendObject.set("sw",yajiangM.getSw());
            sendObject.set("shuijiaobi",yajiangM.getShuijiaobi());
            sendObject.set("snyl",yajiangM.getSnyl());
            sendObject.set("yjwd",yajiangM.getYjwd());
            sendObject.set("msl",yajiangM.getMsl());
            sendObject.set("beiyong",yajiangM.getBeiyong());
            sendObject.set("yjsbbh",yajiangM.getYjsbbh());
            sendObject.set("lblx",yajiangM.getLblx());
            sendObject.set("lianghao",yajiangM.getLianghao());
            sendObject.set("zlsj",yajiangM.getZlsj());
            sendObject.set("yajiangji",yajiangM.getYajiangji());
            sendObject.set("snmc",yajiangM.getSnmc());
            sendObject.set("kongdaoshu",yajiangM.getKongdaoshu());
            sendObject.set("yajiangfangxiang",yajiangM.getYajiangfang());
            sendObject.set("yajiangbuzhou",yajiangM.getYajiangbuzh());
            sendObject.set("yajiangguocheng",yajiangM.getYajiangguoc());
            sendObject.set("chushisudu",yajiangM.getChushisudu());
            sendObject.set("liudongdu",yajiangM.getLiudongdu());
            sendObject.set("memo",yajiangM.getMemo());
            sendObject.set("status",1);
            sendObject.set("isbzb","1");

            JSONObject zlxxsendObject = JSONUtil.createObj();
            List listxx = new ArrayList();
            listxx.add(sendObject);
            zlxxsendObject.set("Method","SWYJSY_M");
            zlxxsendObject.set("data",listxx);
            String zlxxsendObjectBase64 = Base64.getEncoder().encodeToString(zlxxsendObject.toString().getBytes(StandardCharsets.UTF_8));


            String back = post(zlxxsendObjectBase64);
            if (back.contains("true")){
                log.info("47推送义东老平台成功！");
                yajiangM.setIsmt(1);
            }else {
                log.info("47推送义东老平台失败！");
                yajiangM.setIsmt(2);
            }
            yajiangMService.updateById(yajiangM);

            List listsendson = new ArrayList();
            QueryWrapper<TrYajiangS> yajiangSQueryWrapper = new QueryWrapper<>();
            yajiangSQueryWrapper.eq("syjid", yajiangM.getSyjid());
            List<TrYajiangS> trYajiangSs = yajiangSService.list(yajiangSQueryWrapper);
            for (TrYajiangS yajiangS : trYajiangSs) {
                JSONObject sendObjectson = JSONUtil.createObj();
                sendObjectson.set("f_guid",yajiangS.getFguid());
                sendObjectson.set("syjid",yajiangS.getSyjid());
                sendObjectson.set("yajiangsj",yajiangS.getYajiangsj());
                sendObjectson.set("kongdao",yajiangS.getKongdao());
                sendObjectson.set("yajiangmoshi",yajiangS.getYajiangsj());
                sendObjectson.set("peihebi",yajiangS.getPeihebi());
                sendObjectson.set("shuijiaobi",yajiangS.getShuijiaobi());
                sendObjectson.set("jiaobansj",yajiangS.getJiaobansj());
                sendObjectson.set("starttime",yajiangS.getStarttime());
                sendObjectson.set("endtime",yajiangS.getEndtime());
                sendObjectson.set("jinjiangyali",yajiangS.getJinjiangyal());
                sendObjectson.set("fanjiangyali",yajiangS.getFanjiangyal());
                sendObjectson.set("chixushijian",yajiangS.getChixushijia());
                sendObjectson.set("jinjiangshuliang",yajiangS.getJinjiangshu());
                sendObjectson.set("fanjiangliang",yajiangS.getFanjianglia());
                sendObjectson.set("zkyl",yajiangS.getZkyl());
                sendObjectson.set("tongguo",yajiangS.getTongguo());
                sendObjectson.set("yjcs",yajiangS.getYjcs());
                sendObjectson.set("hege",yajiangS.getHege());
                sendObjectson.set("holeid",yajiangS.getHoleid());
                sendObjectson.set("status",1);
                listsendson.add(sendObjectson);
            }
            JSONObject yjssendObject = JSONUtil.createObj();
            yjssendObject.set("Method","SWYJSY_S");
            yjssendObject.set("data",listsendson);
            String yjssendObjectBase64 = Base64.getEncoder().encodeToString(yjssendObject.toString().getBytes(StandardCharsets.UTF_8));


            String backs = post(yjssendObjectBase64);
            if (backs.contains("true")){
                log.info("47推送义东老平台成功！");
            }else {
                log.info("47推送义东老平台失败！");
            }

            sysConfigService.updateSysConfig(JobUtil.YD_YJ47, id);//根据传过来的唯一值来修改当前判断到的数据id
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
