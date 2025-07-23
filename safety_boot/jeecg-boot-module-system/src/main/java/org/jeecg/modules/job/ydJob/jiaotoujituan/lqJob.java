package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @ClassName lqJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/11/3 14:08
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lqJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IBhzLqCailiaoService bhzLqCailiaoService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";

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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_LQLSSJ);//沥青历史数据推送交投集团
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
        List<BhzLqBases> bhzLqBases = bhzLqBasesService.selectList2(shebeilist, curid);
        if (null == bhzLqBases || bhzLqBases.size() == 0) {
            log.info(String.format("暂无义东沥青未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        DecimalFormat df = new DecimalFormat("#.00");
        for (BhzLqBases bhzLqBase : bhzLqBases) {
            id = bhzLqBase.getId();
            JSONObject sendJSONObject = JSONUtil.createObj();
            String shebeibianhao = bhzLqBase.getShebeibianhao();
            if (shebeibianhao.equals("hyfxs5lq01")){
                sendJSONObject.set("deviceNum", "HYFX_S5_" + shebeibianhao);
            }else if (shebeibianhao.equals("ddhylq01")){
                sendJSONObject.set("deviceNum", "ST_SG02_DDHY_LQ ");
            }else {
                sendJSONObject.set("deviceNum", "YD_LM01_" + shebeibianhao);
            }
            sendJSONObject.set("productTime", bhzLqBase.getChuliaoshijian());
            String hunheliaoid = bhzLqBase.getHunheliaoid();
            String formulaNo = bhzLqBase.getFormulaNo();
            if ("54".equals(hunheliaoid)){
                formulaNo = "20";
            }
            if ("52".equals(hunheliaoid)){
                formulaNo = "13";
            }
            sendJSONObject.set("formulaNum", formulaNo);
            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "hhllx");
            SysDict one = sysDictService.getOne(sysDictQueryWrapper);
            String id1 = one.getId();

            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id", id1);
            sysDictItemQueryWrapper.eq("item_value", hunheliaoid);
            SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
            if (one1 == null) {
                sendJSONObject.set("formulaName", bhzLqBase.getFormulaNo());
            } else {
                sendJSONObject.set("formulaName", one1.getItemText());
            }
            sendJSONObject.set("mixTime", bhzLqBase.getBanheshijian());
            String liqingwd = bhzLqBase.getLiqingwd();
            if(StringUtils.isNotBlank(liqingwd)){
                int asphaltTemp = Double.valueOf(liqingwd).intValue();
                sendJSONObject.set("asphaltTemp", asphaltTemp);
            }

            String guliaowd = bhzLqBase.getGuliaowd();
            if(StringUtils.isNotBlank(guliaowd)){
                int slTemp = Double.valueOf(guliaowd).intValue();
                sendJSONObject.set("slTemp", slTemp);
            }

            String chuliaowd = bhzLqBase.getChuliaowd();
            int mixTemp = Double.valueOf(chuliaowd).intValue();
            sendJSONObject.set("mixTemp", mixTemp);
            sendJSONObject.set("weight", bhzLqBase.getZongchanliang());
            sendJSONObject.set("alarmLevel", bhzLqBase.getChaobiaodengji());
            sendJSONObject.set("osbMisPstSj", bhzLqBase.getYoushibi());
            sendJSONObject.set("osbMisPstLl", bhzLqBase.getLlysb());
            Double d = 0.0;
            if(StringUtils.isNotBlank(bhzLqBase.getYoushibi()) && StringUtils.isNotBlank(bhzLqBase.getLlysb())){
                d = Double.valueOf(bhzLqBase.getYoushibi()) - Double.valueOf(bhzLqBase.getLlysb());
            }
            sendJSONObject.set("osbMisPst", df.format(d));
            sendJSONObject.set("osbAlarmLevel", 0);

            QueryWrapper<BhzLqCailiao> bhzLqCailiaoQueryWrapper = new QueryWrapper<>();
            bhzLqCailiaoQueryWrapper.eq("base_guid", bhzLqBase.getGuid());
            List<BhzLqCailiao> list = bhzLqCailiaoService.list(bhzLqCailiaoQueryWrapper);
            for (BhzLqCailiao bhzLqCailiao : list) {
                String cailiaoming = bhzLqCailiao.getCailiaoming();
                double v = bhzLqCailiao.getTheoryNumber() - bhzLqCailiao.getShijiyongliang();
                String format = df.format(v);
                if (cailiaoming.equals("沥青")) {
                    sendJSONObject.set("lqAmountSj", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("lqAmountLl", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("lqPstSj", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("lqPstLl", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("lqMis", format);
                    sendJSONObject.set("lqMisPst", bhzLqCailiao.getWucha());
                    sendJSONObject.set("lqAlarmLevel", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("骨料1") || cailiaoming.equals("石料1") ) {
                    sendJSONObject.set("slAmountSj1", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("slAmountLl1", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("slPstSj1", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("slPstLl1", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("slMis1", format);
                    sendJSONObject.set("slMisPst1", bhzLqCailiao.getWucha());
                    sendJSONObject.set("slAlarmLevel1", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("骨料2") || cailiaoming.equals("石料2") ) {
                    sendJSONObject.set("slAmountSj2", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("slAmountLl2", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("slPstSj2", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("slPstLl2", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("slMis2", format);
                    sendJSONObject.set("slMisPst2", bhzLqCailiao.getWucha());
                    sendJSONObject.set("slAlarmLevel2", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("骨料3") || cailiaoming.equals("石料3")) {
                    sendJSONObject.set("slAmountSj3", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("slAmountLl3", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("slPstSj3", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("slPstLl3", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("slMis3", format);
                    sendJSONObject.set("slMisPst3", bhzLqCailiao.getWucha());
                    sendJSONObject.set("slAlarmLevel3", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("骨料4") || cailiaoming.equals("石料4")) {
                    sendJSONObject.set("slAmountSj4", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("slAmountLl4", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("slPstSj4", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("slPstLl4", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("slMis4", format);
                    sendJSONObject.set("slMisPst4", bhzLqCailiao.getWucha());
                    sendJSONObject.set("slAlarmLevel4", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("骨料5") || cailiaoming.equals("石料5")) {
                    sendJSONObject.set("slAmountSj5", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("slAmountLl5", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("slPstSj5", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("slPstLl5", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("slMis5", format);
                    sendJSONObject.set("slMisPst5", bhzLqCailiao.getWucha());
                    sendJSONObject.set("slAlarmLevel5", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("骨料6") || cailiaoming.equals("石料6")) {
                    sendJSONObject.set("slAmountSj6", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("slAmountLl6", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("slPstSj6", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("slPstLl6", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("slMis6", format);
                    sendJSONObject.set("slMisPst6", bhzLqCailiao.getWucha());
                    sendJSONObject.set("slAlarmLevel6", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("骨料7") || cailiaoming.equals("石料7")) {
                    sendJSONObject.set("slAmountSj7", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("slAmountLl7", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("slPstSj7", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("slPstLl7", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("slMis7", format);
                    sendJSONObject.set("slMisPst7", bhzLqCailiao.getWucha());
                    sendJSONObject.set("slAlarmLevel7", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("粉料1") || cailiaoming.equals("石粉1")) {
                    sendJSONObject.set("flAmountSj1", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("flAmountLl1", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("flPstSj1", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("flPstLl1", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("flMis1", format);
                    sendJSONObject.set("flMisPst1", bhzLqCailiao.getWucha());
                    sendJSONObject.set("flAlarmLevel1", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("粉料2") || cailiaoming.equals("石粉2")) {
                    sendJSONObject.set("flAmountSj2", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("flAmountLl2", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("flPstSj2", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("flPstLl2", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("flMis2", format);
                    sendJSONObject.set("flMisPst2", bhzLqCailiao.getWucha());
                    sendJSONObject.set("flAlarmLevel2", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("粉料3") || cailiaoming.equals("石粉3")) {
                    sendJSONObject.set("flAmountSj3", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("flAmountLl3", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("flPstSj3", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("flPstLl3", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("flMis3", format);
                    sendJSONObject.set("flMisPst3", bhzLqCailiao.getWucha());
                    sendJSONObject.set("flAlarmLevel3", bhzLqCailiao.getChaobiaodengji());
                }
                if (cailiaoming.equals("外加剂")) {
                    sendJSONObject.set("wjjAmountSj", bhzLqCailiao.getShijiyongliang());
                    sendJSONObject.set("wjjAmountLl", bhzLqCailiao.getTheoryNumber());
                    sendJSONObject.set("wjjPstSj", bhzLqCailiao.getShijipb());
                    sendJSONObject.set("wjjPstLl", bhzLqCailiao.getLilunpb());
                    sendJSONObject.set("wjjMis", format);
                    sendJSONObject.set("wjjMisPst", bhzLqCailiao.getWucha());
                    sendJSONObject.set("wjjAlarmLevel", bhzLqCailiao.getChaobiaodengji());
                }
            }

            String md5 = DigestUtils.md5DigestAsHex(String.valueOf(sendJSONObject).getBytes(StandardCharsets.UTF_8));
            long reqTimes = System.currentTimeMillis();
            //对data进行签名
            String sign = getSign(String.join(":", String.valueOf(reqTimes), md5), SECRET_KEY);
            String s = ACCESS_KEY + ":" + sign;
            System.out.println(sendJSONObject);
            String back = HttpRequest.post("http://47.110.39.210:6023/api/lqProduct/saveLqProductHistory")
                    .header("request-time", String.valueOf(reqTimes))
                    .header("request-auth", s)
                    .body(String.valueOf(sendJSONObject))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back);
            String data = jsonObject1.get("code").toString();
            if ("200".equals(data)) {
                log.info(String.format("义东沥青站推送成功!" + id + "Json数据" + sendJSONObject));
            } else {
                log.info(String.format("义东沥青站推送失败!" + id + "Json数据" + sendJSONObject));
            }
            sysConfigService.updateSysConfig(JobUtil.JTJT_LQLSSJ, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id,String.valueOf(sendJSONObject),selectsysconfigone.getRemark(),back);
        }
    }
}
