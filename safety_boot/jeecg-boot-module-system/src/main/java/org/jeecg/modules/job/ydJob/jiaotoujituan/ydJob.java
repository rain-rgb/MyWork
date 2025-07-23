package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.ydcx.entity.WYanduM;
import com.trtm.iot.ydcx.service.IWYanduMService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Service
public class ydJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IWYanduMService yanduMService;

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_YD);//延度数据推送交投集团
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到延度数据的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输延度数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<WYanduM> wYanduMS = yanduMService.getListjt(shebeilist, curid);
        if (null == wYanduMS || wYanduMS.isEmpty()) {
            log.info(String.format("没有数据要传输延度数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        DecimalFormat df = new DecimalFormat("#.00");
        for (WYanduM wYanduM : wYanduMS) {
            id = wYanduM.getId();
            JSONObject sendJSONObject = JSONUtil.createObj();
            String fsbbh = wYanduM.getFsbbh();
            String deviceNum = "RC_TJ01_YD_001";
            if (fsbbh.equals("rcgs04yd01")) {
                deviceNum = "RC_TJ04_YD_001";
            } else if (fsbbh.equals("ytwbdzsysd001")) {
                deviceNum = "YTW_TJ03_LQSDSY02_Y";
            }
            sendJSONObject.put("deviceNum", deviceNum);
            Integer sampleType = 1;
            Integer lqtype = wYanduM.getLqtype();
            //0:改性沥青;1:基质沥青;2:乳化沥青
            if (lqtype == 2) {
                sampleType = 0;
            } else if (lqtype == 3) {
                sampleType = 2;
            }
            sendJSONObject.put("sampleType", sampleType);
            sendJSONObject.put("sampleType", sampleType);
            sendJSONObject.put("sampleName", wYanduM.getSamplename());
            sendJSONObject.put("sampleNum", wYanduM.getSampleno());
            sendJSONObject.put("sampleDes", wYanduM.getSamplems());
            sendJSONObject.put("testTime", wYanduM.getIsTesttime());
            sendJSONObject.put("capacityValue", "");
            sendJSONObject.put("injectionTime", "");
            sendJSONObject.put("testTemp", wYanduM.getTeststatus());


            if (wYanduM.getSamplename().contains("老化")){
                sendJSONObject.put("testType", 2);
            }
            double biaozhunzhi1 = getData(wYanduM.getBiaozhunzhi1());
            double biaozhunzhi2 = getData(wYanduM.getBiaozhunzhi2());
            double[] refValues = new double[]{biaozhunzhi1, biaozhunzhi2};
            sendJSONObject.put("refValues", refValues);

            double yandu1 = getData(wYanduM.getYandu11());
            double yandu2 = getData(wYanduM.getYandu12());
            double yandu3 = getData(wYanduM.getYandu13());
            double[] values = new double[]{yandu1, yandu2, yandu3};
            sendJSONObject.put("values", values);

            sendJSONObject.put("avgValue", getData(wYanduM.getYdavg()));
            String isqualified = wYanduM.getIsqualified();
            if (isqualified.equals("1") || isqualified.equals("不合格")) {
                sendJSONObject.put("isQualified", 1);
            } else {
                sendJSONObject.put("isQualified", 0);
            }
            sendJSONObject.put("proSection", wYanduM.getGcbuwei());
            sendJSONObject.put("testPerson", "");
            sendJSONObject.put("factoryNum", "NBZHK");
            sendJSONObject.put("checkType", "2");
            sendJSONObject.put("carNum", wYanduM.getCarnum());
            sendJSONObject.put("remark", "");

            String md5 = DigestUtils.md5DigestAsHex(String.valueOf(sendJSONObject).getBytes(StandardCharsets.UTF_8));
            long reqTimes = System.currentTimeMillis();
            //对data进行签名
            String sign = getSign(String.join(":", String.valueOf(reqTimes), md5), SECRET_KEY);
            String s = ACCESS_KEY + ":" + sign;

            String body = HttpRequest.post("http://47.110.39.210:6023/api/asphaltExperiment/saveAsphaltDuctility")
                    .header("request-time", String.valueOf(reqTimes))
                    .header("request-auth", s)
                    .body(String.valueOf(sendJSONObject))
                    .execute()
                    .body();
            if (body.contains("成功")) {
                log.info(String.format("推送成功!" + id + "Json数据" + sendJSONObject));
                wYanduM.setIsjtjt(1);
            } else {
                log.info(String.format("推送失败!" + id + "Json数据" + sendJSONObject));
                wYanduM.setIsjtjt(2);
            }
            yanduMService.updateById(wYanduM);
            sysConfigService.updateSysConfig(JobUtil.JTJT_YD, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(sendJSONObject), selectsysconfigone.getRemark(), body);
        }
    }

    public Double getData(BigDecimal data) {
        DecimalFormat df = new DecimalFormat("#.00");
        Double v = Double.valueOf(String.valueOf(data));
        if (v > 200) {
            v = v * 0.1;
        }
        v = Double.parseDouble(df.format(v));
        return v;
    }

    public static String getSign(String data, String key) {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return new String(result);
        } else {
            return null;
        }
    }
}
