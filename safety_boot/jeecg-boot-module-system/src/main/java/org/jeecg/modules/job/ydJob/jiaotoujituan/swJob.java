package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName swJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/5/26 9:34
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class swJob implements Job {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzSwBasesService swBasesService;
    @Autowired
    private IBhzSwCailiaoService swCailiaoService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    private String url = "http://47.110.39.210:6023/api/swProduct/saveSwProductHistory";

    /**
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data 被签名的字符串
     * @param key  密钥
     * @return 加密后的字符串
     */
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

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_SWLSSJ);//水稳历史数据推送交投集团
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东水稳的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东水稳的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzSwBases> bhzSwBasesList = swBasesService.selectListToJTJT(shebeilist, curid);
        if (null == bhzSwBasesList || bhzSwBasesList.size() == 0) {
            log.info(String.format("暂无义东水稳未推送数据" + DateUtils.now()));
            return;
        }

        int id = 0;
        DecimalFormat df = new DecimalFormat("#.00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BhzSwBases bhzSwBases : bhzSwBasesList) {
            id = bhzSwBases.getId();
            JSONObject sendJSONObject = new JSONObject();
            String shebeibianhao = bhzSwBases.getShebeibianhao();
            if (shebeibianhao.equals("yd1bsw_01")){
                sendJSONObject.set("deviceNum", "YD_TJ01_"+shebeibianhao); //设备编号
            }else {
                sendJSONObject.set("deviceNum", "HYFX_S5_"+shebeibianhao); //设备编号
            }
            String chuliaoshijian = bhzSwBases.getChuliaoshijian();
            Date date = sdf.parse(chuliaoshijian);

            sendJSONObject.set("productTime", sdf.format(date) ); //生产时间
            sendJSONObject.set("formulaNum", bhzSwBases.getFormulaNo()); // 配方编号 string 是
            sendJSONObject.set("formulaName", bhzSwBases.getFormulaNo()); // 配方名称 string 是
            sendJSONObject.set("mixTime", bhzSwBases.getBanheshijian()); // 搅拌时间 int 是 单位：s
            sendJSONObject.set("alarmLevel", bhzSwBases.getChaobiaodengji()); // 本锅报警等级 int 是 0:正常;1:低;2:中;3:高

            QueryWrapper<BhzSwCailiao> swCailiaoQueryWrapper = new QueryWrapper<>();
            swCailiaoQueryWrapper.eq("base_guid",bhzSwBases.getGuid());
            List<BhzSwCailiao> cailiaoList = swCailiaoService.list(swCailiaoQueryWrapper);

            Double weight = 0.0;
            for (BhzSwCailiao bhzSwCailiao : cailiaoList) {
                Double shijiyongliang = bhzSwCailiao.getShijiyongliang();
                weight += shijiyongliang;
                String cailiaoming = bhzSwCailiao.getCailiaoming();

                if ("料1".equals(cailiaoming)){
                    sendJSONObject.set("glAmountSj1", bhzSwCailiao.getShijiyongliang()); // 骨料 1 实际用量 double 否 单位：t
                    sendJSONObject.set("glAmountLl1", bhzSwCailiao.getTheoryNumber()); // 骨料 1 理论用量 double 否 单位：t
                    sendJSONObject.set("glPstSj1", bhzSwCailiao.getShijipb()); // 骨料 1 实际配比 double 否 单位：%
                    sendJSONObject.set("glPstLl1", bhzSwCailiao.getLilunpb()); // 骨料 1 理论配比 double 否 单位：%
                    Double theoryNumber = bhzSwCailiao.getTheoryNumber();
                    if (theoryNumber==null){
                        theoryNumber = 0.0;
                    }
                    sendJSONObject.set("glMis1", df.format(bhzSwCailiao.getShijiyongliang()-theoryNumber)); // 骨料 1 用量误差 double 否 单位：t
                    sendJSONObject.set("glMisPst1", df.format(bhzSwCailiao.getWucha())); // 骨料 1 配比误差 double 否 单位：%
                    sendJSONObject.set("glAlarmLevel1", bhzSwCailiao.getChaobiaodengji()); // 骨料 1 报警等级 int 否 0:正常;1:低;2:中;3:高
                }
                if ("料2".equals(cailiaoming)){
                    sendJSONObject.set("glAmountSj2", bhzSwCailiao.getShijiyongliang());
                    sendJSONObject.set("glAmountLl2", bhzSwCailiao.getTheoryNumber());
                    sendJSONObject.set("glPstSj2", bhzSwCailiao.getShijipb());
                    sendJSONObject.set("glPstLl2", bhzSwCailiao.getLilunpb());
                    Double theoryNumber = bhzSwCailiao.getTheoryNumber();
                    if (theoryNumber==null){
                        theoryNumber = 0.0;
                    }
                    sendJSONObject.set("glMis2", df.format(bhzSwCailiao.getShijiyongliang()-theoryNumber));
                    sendJSONObject.set("glMisPst2", df.format(bhzSwCailiao.getWucha()));
                    sendJSONObject.set("glAlarmLevel2", bhzSwCailiao.getChaobiaodengji());
                }
                if ("料3".equals(cailiaoming)){
                    sendJSONObject.set("glAmountSj3", bhzSwCailiao.getShijiyongliang());
                    sendJSONObject.set("glAmountLl3", bhzSwCailiao.getTheoryNumber());
                    sendJSONObject.set("glPstSj3", bhzSwCailiao.getShijipb());
                    sendJSONObject.set("glPstLl3", bhzSwCailiao.getLilunpb());
                    Double theoryNumber = bhzSwCailiao.getTheoryNumber();
                    if (theoryNumber==null){
                        theoryNumber = 0.0;
                    }
                    sendJSONObject.set("glMis3", df.format(bhzSwCailiao.getShijiyongliang()-theoryNumber));
                    sendJSONObject.set("glMisPst3", df.format(bhzSwCailiao.getWucha()));
                    sendJSONObject.set("glAlarmLevel3", bhzSwCailiao.getChaobiaodengji());
                }
                if ("料4".equals(cailiaoming)){
                    sendJSONObject.set("glAmountSj4", bhzSwCailiao.getShijiyongliang());
                    sendJSONObject.set("glAmountLl4", bhzSwCailiao.getTheoryNumber());
                    sendJSONObject.set("glPstSj4", bhzSwCailiao.getShijipb());
                    sendJSONObject.set("glPstLl4", bhzSwCailiao.getLilunpb());
                    Double theoryNumber = bhzSwCailiao.getTheoryNumber();
                    if (theoryNumber==null){
                        theoryNumber = 0.0;
                    }
                    sendJSONObject.set("glMis4", df.format(bhzSwCailiao.getShijiyongliang()-theoryNumber));
                    sendJSONObject.set("glMisPst4", df.format(bhzSwCailiao.getWucha()));
                    sendJSONObject.set("glAlarmLevel4", bhzSwCailiao.getChaobiaodengji());
                }
                if ("料5".equals(cailiaoming)){
                    sendJSONObject.set("glAmountSj5", bhzSwCailiao.getShijiyongliang());
                    sendJSONObject.set("glAmountLl5", bhzSwCailiao.getTheoryNumber());
                    sendJSONObject.set("glPstSj5", bhzSwCailiao.getShijipb());
                    sendJSONObject.set("glPstLl5", bhzSwCailiao.getLilunpb());
                    Double theoryNumber = bhzSwCailiao.getTheoryNumber();
                    if (theoryNumber==null){
                        theoryNumber = 0.0;
                    }
                    sendJSONObject.set("glMis5", df.format(bhzSwCailiao.getShijiyongliang()-theoryNumber));
                    sendJSONObject.set("glMisPst5", df.format(bhzSwCailiao.getWucha()));
                    sendJSONObject.set("glAlarmLevel5", bhzSwCailiao.getChaobiaodengji());
                }
                if ("料6".equals(cailiaoming)){
                    sendJSONObject.set("glAmountSj6", bhzSwCailiao.getShijiyongliang());
                    sendJSONObject.set("glAmountLl6", bhzSwCailiao.getTheoryNumber());
                    sendJSONObject.set("glPstSj6", bhzSwCailiao.getShijipb());
                    sendJSONObject.set("glPstLl6", bhzSwCailiao.getLilunpb());
                    Double theoryNumber = bhzSwCailiao.getTheoryNumber();
                    if (theoryNumber==null){
                        theoryNumber = 0.0;
                    }
                    sendJSONObject.set("glMis6", df.format(bhzSwCailiao.getShijiyongliang()-theoryNumber));
                    sendJSONObject.set("glMisPst6", df.format(bhzSwCailiao.getWucha()));
                    sendJSONObject.set("glAlarmLevel6", bhzSwCailiao.getChaobiaodengji());
                }
                if ("料7".equals(cailiaoming)){
                    sendJSONObject.set("cementAmountSj", bhzSwCailiao.getShijiyongliang());//shuini
                    sendJSONObject.set("cementAmountLl", bhzSwCailiao.getTheoryNumber());
                    sendJSONObject.set("cementPstSj", bhzSwCailiao.getShijipb());
                    sendJSONObject.set("cementPstLl", bhzSwCailiao.getLilunpb());
                    Double theoryNumber = bhzSwCailiao.getTheoryNumber();
                    if (theoryNumber==null){
                        theoryNumber = 0.0;
                    }
                    sendJSONObject.set("cementMis", df.format(bhzSwCailiao.getShijiyongliang()-theoryNumber));
                    sendJSONObject.set("cementMisPst", df.format(bhzSwCailiao.getWucha()));
                    sendJSONObject.set("cementAlarmLevel", bhzSwCailiao.getChaobiaodengji());
                }
                if ("料8".equals(cailiaoming)){
                    sendJSONObject.set("waterAmountSj", bhzSwCailiao.getShijiyongliang()); // 水实际用量 double 是 单位：t
                    sendJSONObject.set("waterAmountLl", bhzSwCailiao.getTheoryNumber()); // 水理论用量 double 是 单位：t
                    sendJSONObject.set("waterPstSj", bhzSwCailiao.getShijipb()); // 水实际配比 double 是 单位：%
                    sendJSONObject.set("waterPstLl", bhzSwCailiao.getLilunpb()); // 水理论配比 double 是 单位：%
                    Double theoryNumber = bhzSwCailiao.getTheoryNumber();
                    if (theoryNumber==null){
                        theoryNumber = 0.0;
                    }
                    sendJSONObject.set("waterMis", df.format(bhzSwCailiao.getShijiyongliang()-theoryNumber)); // 水用量误差 double 是 单位：t
                    sendJSONObject.set("waterMisPst", df.format(bhzSwCailiao.getWucha())); // 水配比误差 double 是 单位：%
                    sendJSONObject.set("waterAlarmLevel", bhzSwCailiao.getChaobiaodengji()); // 水报警等级 int 是 0:正常;1:低;2:中;3:高
                }
            }

            sendJSONObject.set("weight", weight); // 本锅重量 double 是 单位：t

            String md5 = DigestUtils.md5DigestAsHex(String.valueOf(sendJSONObject).getBytes(StandardCharsets.UTF_8));
            long reqTimes = System.currentTimeMillis();
            //对data进行签名
            String sign = getSign(String.join(":", String.valueOf(reqTimes), md5), SECRET_KEY);
            String s = ACCESS_KEY + ":" + sign;

            String back = HttpRequest.post(url)
                    .header("request-time", String.valueOf(reqTimes))
                    .header("request-auth", s)
                    .body(String.valueOf(sendJSONObject))
                    .execute()
                    .body();
            pushandreturnService.saveData(id,String.valueOf(sendJSONObject),selectsysconfigone.getRemark(),back);
            JSONObject jsonObject1 = new JSONObject(back);
            String data = jsonObject1.get("code").toString();
            if ("200".equals(data)) {
                log.info(String.format("义东水稳站推送成功!" + id + "Json数据" + sendJSONObject));
            } else {
                log.info(String.format("义东水稳站推送失败!" + id + "Json数据" + sendJSONObject));
            }
            sysConfigService.updateSysConfig(JobUtil.JTJT_SWLSSJ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
