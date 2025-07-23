package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzSwJipeiStatistics.entity.BhzSwJipeiStatistics;
import com.trtm.iot.bhzSwJipeiStatistics.service.IBhzSwJipeiStatisticsService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @ClassName jipeiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/11/9 9:43
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class swjipeiJob implements Job {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzSwJipeiStatisticsService bhzSwJipeiStatisticsService;
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private IPushandreturnService pushandreturnService;

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

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_SWJP);//沥青历史数据推送交投集团
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东沥青的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东沥青的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzSwJipeiStatistics> bhzSwJipeiStatistics = bhzSwJipeiStatisticsService.selectList1(shebeilist, curid);
        int id = 0;
        DecimalFormat df = new DecimalFormat("#.00");
        for (BhzSwJipeiStatistics bhzSwJipeiStatistic : bhzSwJipeiStatistics) {
            id = bhzSwJipeiStatistic.getId();
            String baseid1 = bhzSwJipeiStatistic.getBaseid();

            List<BhzSwJipeiStatistics> list = bhzSwJipeiStatisticsService.getList1(baseid1);

            JSONObject sendJSONObject = JSONUtil.createObj();

            String shebeibianhao = list.get(0).getSbjno();
            sendJSONObject.set("deviceNum", "HYFX_S5_" + shebeibianhao);

            String chuliaoshijian = list.get(0).getChuliaoshijian();
            sendJSONObject.set("productTime", chuliaoshijian);

            String baseid = list.get(0).getBaseid();
            QueryWrapper<BhzSwBases> bhzSwBasesQueryWrapper = new QueryWrapper<>();
            bhzSwBasesQueryWrapper.eq("guid", baseid);
            BhzSwBases one = bhzSwBasesService.getOne(bhzSwBasesQueryWrapper);
            String formulaNo = one.getFormulaNo();

            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "hhllx");
            SysDict one2 = sysDictService.getOne(sysDictQueryWrapper);
            String SysDictId = one2.getId();
            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id", SysDictId);
            sysDictItemQueryWrapper.eq("item_value", formulaNo);
            SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
            String itemText = one1.getItemText();

            sendJSONObject.set("formulaNum", itemText);
            sendJSONObject.set("formulaName", itemText);

            sendJSONObject.set("isAlarm", 0);
            for (BhzSwJipeiStatistics swJipeiStatistic : list) {

                String shaikong = swJipeiStatistic.getShaikong();
                if ("0.075".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj1", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl1", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis1", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm1", 0);
                }
                if ("0.15".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj2", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl2", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis2", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm2", 0);
                }
                if ("0.3".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj3", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl3", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis3", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm3", 0);
                }
                if ("0.6".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj4", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl4", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis4", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm4", 0);
                }
                if ("1.18".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj5", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl5", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis5", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm5", 0);
                }
                if ("2.36".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj6", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl6", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis6", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm6", 0);
                }
                if ("4.75".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj7", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl7", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis7", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm7", 0);
                }
                if ("9.5".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj8", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl8", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis8", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm8", 0);
                }
                if ("13.2".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj9", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl9", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis9", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm9", 0);
                }
                if ("16".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj10", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl10", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis10", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm10", 0);
                }
                if ("19".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj11", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl11", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis11", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm11", 0);
                }
                if ("26.5".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj12", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl12", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis12", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm12", 0);
                }
                if ("37.5".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj13", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl13", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis13", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm13", 0);
                }
                if ("53".equals(shaikong)) {
                    sendJSONObject.set("tagValueSj14", swJipeiStatistic.getZhongzhi());
                    sendJSONObject.set("tagValueLl14", swJipeiStatistic.getHechengjipei());
                    sendJSONObject.set("tagMis14", df.format(Double.parseDouble(swJipeiStatistic.getZhongzhi()) - swJipeiStatistic.getHechengjipei()));
                    sendJSONObject.set("tagAlarm14", 0);
                }
            }

            String md5 = DigestUtils.md5DigestAsHex(String.valueOf(sendJSONObject).getBytes(StandardCharsets.UTF_8));
            long reqTimes = System.currentTimeMillis();
            //对data进行签名
            String sign = getSign(String.join(":", String.valueOf(reqTimes), md5), SECRET_KEY);
            String s = ACCESS_KEY + ":" + sign;

            String back = HttpRequest.post("http://47.110.39.210:6023/api/swProduct/saveSwProductJpHistory")
                    .header("request-time", String.valueOf(reqTimes))
                    .header("request-auth", s)
                    .body(String.valueOf(sendJSONObject))
                    .timeout(20000)
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back);
            String data = jsonObject1.get("code").toString();
            if ("200".equals(data)) {
                log.info(String.format("义东沥青站推送成功!" + id + "Json数据" + sendJSONObject));
            } else {
                log.info(String.format("义东沥青站推送失败!" + id + "Json数据" + sendJSONObject));
            }
            sysConfigService.updateSysConfig(JobUtil.JTJT_SWJP, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(sendJSONObject), selectsysconfigone.getRemark(), back);
        }
    }
}
