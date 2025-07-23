package org.jeecg.modules.job.kezhujob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName biaoyangshiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/1/4 10:38
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class biaoyangshiJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBysRealService bysRealService;

    private static BigDecimal generateRandomTemperature(BigDecimal lowerBound, BigDecimal upperBound) {
        Random random = new Random();
        return lowerBound.add(new BigDecimal(random.nextDouble())
                .multiply(upperBound.subtract(lowerBound)))
                .setScale(1, BigDecimal.ROUND_HALF_UP)
                .setScale(2);
    }

    private static BigDecimal generateRandomHumidity() {
        BigDecimal lowerBound = new BigDecimal("97");
        BigDecimal upperBound = new BigDecimal("99");
        Random random = new Random();
        return lowerBound.add(new BigDecimal(random.nextDouble())
                .multiply(upperBound.subtract(lowerBound)))
                .setScale(0, BigDecimal.ROUND_HALF_UP)
                .setScale(2);
    }

    @Override
    public void execute(JobExecutionContext context) {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.KZ_BYS);//柯诸标养室
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到柯诸标养室定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输柯诸标养室的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BysReal> bysReals = bysRealService.selectBysListkz(curid, shebeilist);
        if (null == bysReals || bysReals.size() == 0) {
            log.info(String.format("暂无柯诸标养室未推送数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        //循环
        List sendList = new ArrayList();
        for (BysReal bysReal : bysReals) {
            Map<String, Object> sendDateMap = new LinkedHashMap<>();
            id = bysReal.getId();
            String shebeino = bysReal.getShebeino();
            String equimentId = "";
            if ("1111".equals(shebeino)) {
                equimentId = "SJMkJ1eXS5er5fPorK9GKw";
            } else if ("0401".equals(shebeino)) {
                equimentId = "fORftj4bRmGhXJscfb1cyQ";
            } else if ("4401".equals(shebeino)) {
                equimentId = "x9IZM654QxCwrBN-txgoGQ";
            } else {
                equimentId = "JdNDBLEXQiuQrwGKCx80mg";
            }
            BigDecimal temperature = bysReal.getTemperature();
            BigDecimal humidity = bysReal.getHumidity();

            // 设置参数到 LinkedHashMap 中，保持顺序
            sendDateMap.put("equimentId", equimentId);
            sendDateMap.put("temperature", temperature);
            sendDateMap.put("humidity", humidity);
            sendDateMap.put("occurDate", sdf.format(bysReal.getDatatime()));

            // 将 LinkedHashMap 转换为 JSONObject
            JSONObject sendDate = new JSONObject(sendDateMap);

            sendList.add(sendDate);
        }
        String paramJsonStr = String.valueOf(sendList);
        String body = null;
        try {
            body = post(paramJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonObject1 = new JSONObject(body);
        if (jsonObject1.toString().contains("操作成功")) {
            log.info(String.format("柯诸标养室推送成功!" + id + "Json数据" + sendList));
        } else {
            log.info(String.format("柯诸标养室推送失败!" + id + "Json数据" + sendList));
        }
        sysConfigService.updateSysConfig(JobUtil.KZ_BYS, id);//根据传过来的唯一值来修改当前判断到的数据id
    }

    public String post(String paramJsonStr) throws Exception {

        URL url = new URL("https://www.weepal.cn/wpscs/zj_kz/production/tempHum/add");
        String key = "707E67BD-A18B-430B-82BF-8B00A51BBFA7";
        String appId = "1260BD67-9056-477C-AB96-E65D83A6912C";
        String str = paramJsonStr + key;
        String signature = DigestUtils.md5Hex(str).toUpperCase();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法为 POST
        connection.setRequestMethod("POST");

        // 添加 Header 信息
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("appId", appId);
        connection.setRequestProperty("signature", signature);

        // 开始写入请求体
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = paramJsonStr.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        String body = "";
        // 读取响应
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            body = response.toString();
            // 处理响应内容
        }

        // 关闭连接
        connection.disconnect();
        return body;
    }
}
