package org.jeecg.modules.job.bh.job;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * 滨淮1标-6标 混凝土拌合站数据推送
 *
 * @author lis1
 * @date 2022/12/20
 * @time 10:36
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MixingStation implements Job {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IBhzCementBaseService service;

    @Autowired
    private IBhzCementDetailService detailService;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig config = configService.selectsysconfigone(JobUtil.BH1_TO_BH6_BHZ);//瑞苍拌合站
        //如果他等于空
        if (null == config) {
            log.info(String.format("未获取到滨淮混凝土拌合站定时任务的配置信息，时间：" + DateUtils.now()));
            return;
        }
        Integer lastId = config.getCurid();
        String extra = config.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输滨淮混凝土拌合站的设备，时间：" + DateUtils.now()));
            return;
        }
        String devices = jsonObject.getStr("shebeilist");
        String[] split = devices.split(",");
        List<String> params = new ArrayList<>();
        for (String param : split) {
            params.add(param);
        }
        List<BhzCementBase> list = service.getBHAllDataToUpload(lastId, params);
        if (list == null || list.size() < 0) {
            log.info(String.format("暂无滨淮混凝土拌合站未推送数据，时间：" + DateUtils.now()));
            return;
        }
//        Integer currentId = list.get(list.size() - 1).getId();
        for (BhzCementBase item : list) {
            List<JSONObject> data = new ArrayList<>();
            String batchNo = item.getBatchNo();
            List<BhzCementDetail> details = detailService.selectByBatchNo(batchNo);
            JSONObject object = dataProcessing(item, details);
            data.add(object);
            String result = sendHttpPostRequest(data, item.getShebeiNo());
            Integer currentId = item.getId();
            resultProcessing(result, currentId);
        }
    }


    public static JSONObject dataProcessing(BhzCementBase base, List<BhzCementDetail> list) {
        JSONObject data = new JSONObject();
        data.set("qddj", base.getStrengthRank() == null ? "" : base.getStrengthRank().toUpperCase());
        data.set("ts", sdf.format(base.getCollectDatetime()));
        data.set("rwdh", base.getWorkNo());
        data.set("jzdd", base.getJobLocation());
        data.set("gcbw", base.getPoureLocation());
        data.set("cph", "/");
        data.set("tld", base.getSlump());
        data.set("gcmc", base.getProjectName());
        data.set("tfl", base.getEstimateNumber());
        data.set("panci", base.getClientNo());
        data.set("jbsj", base.getStirDatetime());

        int type3 = 0;  //骨料
        int type5 = 0;  //水
        int type6 = 0;  //水泥
        int type8 = 0;  //煤灰
        int type9 = 0;  //添加剂

        for (BhzCementDetail detail : list) {
            if (detail.getMaterialeType() == 1 || detail.getMaterialeType() == 2 || detail.getMaterialeType() == 3 || detail.getMaterialeType() == 4) {
                data.set("gl" + (type3 + 1) + "mbz", detail.getTheoryNumber());
                data.set("gl" + (type3 + 1) + "scz", detail.getRealityNumber());
                type3++;
            }
            if (detail.getMaterialeType() == 5) {
                data.set("shui" + (type5 + 1) + "mbz", detail.getTheoryNumber());
                data.set("shui" + (type5 + 1) + "scz", detail.getRealityNumber());
                type5++;
            }
            if (detail.getMaterialeType() == 6) {
                data.set("sn" + (type6 + 1) + "mbz", detail.getTheoryNumber());
                data.set("sn" + (type6 + 1) + "scz", detail.getRealityNumber());
                type6++;
            }
            if (detail.getMaterialeType() == 8) {
                data.set("mh" + (type8 + 1) + "mbz", detail.getTheoryNumber());
                data.set("mh" + (type8 + 1) + "scz", detail.getRealityNumber());
                type8++;
            }
            if (detail.getMaterialeType() == 9) {
                data.set("tjj" + (type9 + 1) + "mbz", detail.getTheoryNumber());
                data.set("tjj" + (type9 + 1) + "scz", detail.getRealityNumber());
                type9++;
            }
        }

        return data;
    }

    /**
     * 发送Post请求
     *
     * @param list 数据列表
     * @return 请求结果
     */
    public static String sendHttpPostRequest(List list, String device) {
        String url1 = "https://iot.e-jt.cn/broker/device/v1/";
        String url2 = "/attributes/report";
        String result = HttpRequest.post(url1 + device + url2)
                .header("username", device)
                .header("password", device)
                .header("keepOnlineTimeoutSeconds", "300")
                .body(String.valueOf(list))
                .timeout(20000)
                .execute()
                .body();
        return result;
    }

    /**
     * 请求结果处理
     *
     * @param result
     */
    private void resultProcessing(String result, Integer currentId) {
        JSONObject jsonObject = new JSONObject(result);
        System.out.println("请求结果1：" + jsonObject.get("msg").toString());
        log.info("请求结果1：" + jsonObject.get("msg").toString());
        int success = Integer.parseInt(jsonObject.get("code").toString());
        if (success == 0) {
            log.info(String.format("滨淮拌合站推送成功!"));
            configService.updateSysConfig(JobUtil.BH1_TO_BH6_BHZ, currentId);
        } else {
            log.info(String.format("滨淮拌合站推送失败!"));
        }
    }


    /**
     * 加密算法
     *
     * @param clientSecret
     * @param clientId
     * @param currentDate
     * @return
     */
    public static String buildSignature(String clientSecret, String clientId, String currentDate) {
        byte[] result = null;
        try {
            List<String> list = new ArrayList<>();
            list.add(clientSecret);
            list.add(clientId);
            list.add(currentDate);
            StringBuilder content = new StringBuilder();
            Collections.sort(list);
            for (String str : list) {
                content.append(str);
            }
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signInKey = new
                    SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(signInKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(content.toString().getBytes(StandardCharsets.UTF_8));
            result = Base64.getEncoder().encode(rawHmac);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
