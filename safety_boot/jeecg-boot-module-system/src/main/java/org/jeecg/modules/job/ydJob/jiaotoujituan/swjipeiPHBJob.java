package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzSwJipeiFanwei.entity.BhzSwJipeiFanwei;
import com.trtm.iot.bhzSwJipeiFanwei.service.IBhzSwJipeiFanweiService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName jipeiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/11/9 9:22
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class swjipeiPHBJob implements Job {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzSwJipeiFanweiService swJipeiFanweiService;
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_SWPHB);//沥青历史数据推送交投集团
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到水稳级配的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输水稳级配的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzSwJipeiFanwei> selectlist = swJipeiFanweiService.selectlist(shebeilist, curid);
        if (null == selectlist || selectlist.size() == 0) {
            log.info(String.format("暂无水稳级配未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        DecimalFormat df = new DecimalFormat("#.00");
        for (BhzSwJipeiFanwei bhzSwJipeiFanwei : selectlist) {
            id = bhzSwJipeiFanwei.getId();
            JSONObject sendJSONObject = JSONUtil.createObj();
            String shebeibianhao = bhzSwJipeiFanwei.getSbjno();
            sendJSONObject.set("deviceNum", "HYFX_S5_" + shebeibianhao);

            String poureLocation = bhzSwJipeiFanwei.getPoureLocation();
            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "hhllx");
            SysDict one = sysDictService.getOne(sysDictQueryWrapper);
            String SysDictId = one.getId();
            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id", SysDictId);
            sysDictItemQueryWrapper.eq("item_value", poureLocation);
            SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
            String itemText = one1.getItemText();

            sendJSONObject.set("formulaNum", itemText);
            Date creattime = bhzSwJipeiFanwei.getCreattime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedTime = sdf.format(creattime);

            sendJSONObject.set("effectTime", formattedTime);
            sendJSONObject.set("tagValueMax1", bhzSwJipeiFanwei.getSk0075shangxian());
            sendJSONObject.set("tagValueMin1", bhzSwJipeiFanwei.getSk0075xiaxian());
            sendJSONObject.set("tagValueMax2", bhzSwJipeiFanwei.getSk015shangxian());
            sendJSONObject.set("tagValueMin2", bhzSwJipeiFanwei.getSk015xiaxian());
            sendJSONObject.set("tagValueMax3", bhzSwJipeiFanwei.getSk03shangxian());
            sendJSONObject.set("tagValueMin3", bhzSwJipeiFanwei.getSk03xiaxian());
            sendJSONObject.set("tagValueMax4", bhzSwJipeiFanwei.getSk06shangxian());
            sendJSONObject.set("tagValueMin4", bhzSwJipeiFanwei.getSk06xiaxian());
            sendJSONObject.set("tagValueMax5", bhzSwJipeiFanwei.getSk118shangxian());
            sendJSONObject.set("tagValueMin5", bhzSwJipeiFanwei.getSk118xiaxian());
            sendJSONObject.set("tagValueMax6", bhzSwJipeiFanwei.getSk236shangxian());
            sendJSONObject.set("tagValueMin6", bhzSwJipeiFanwei.getSk236xiaxian());
            sendJSONObject.set("tagValueMax7", bhzSwJipeiFanwei.getSk475shangxian());
            sendJSONObject.set("tagValueMin7", bhzSwJipeiFanwei.getSk475xiaxian());
            sendJSONObject.set("tagValueMax8", bhzSwJipeiFanwei.getSk95shangxian());
            sendJSONObject.set("tagValueMin8", bhzSwJipeiFanwei.getSk95xiaxian());
            sendJSONObject.set("tagValueMax9", bhzSwJipeiFanwei.getSk132shangxian());
            sendJSONObject.set("tagValueMin9", bhzSwJipeiFanwei.getSk132xiaxian());
            sendJSONObject.set("tagValueMax10", bhzSwJipeiFanwei.getSk16shangxian());
            sendJSONObject.set("tagValueMin10", bhzSwJipeiFanwei.getSk16xiaxian());
            sendJSONObject.set("tagValueMax11", bhzSwJipeiFanwei.getSk19shangxian());
            sendJSONObject.set("tagValueMin11", bhzSwJipeiFanwei.getSk19xiaxian());
            sendJSONObject.set("tagValueMax12", bhzSwJipeiFanwei.getSk265shangxian());
            sendJSONObject.set("tagValueMin12", bhzSwJipeiFanwei.getSk265shangxian());
            sendJSONObject.set("tagValueMax13", bhzSwJipeiFanwei.getSk315shangxian());
            sendJSONObject.set("tagValueMin13", bhzSwJipeiFanwei.getSk315shangxian());
            sendJSONObject.set("tagValueMax14", bhzSwJipeiFanwei.getSk375shangxian());
            sendJSONObject.set("tagValueMin14", bhzSwJipeiFanwei.getSk375shangxian());
            sendJSONObject.set("tagValueMax15", bhzSwJipeiFanwei.getSk53shangxian());
            sendJSONObject.set("tagValueMin15", bhzSwJipeiFanwei.getSk53shangxian());

            String md5 = DigestUtils.md5DigestAsHex(String.valueOf(sendJSONObject).getBytes(StandardCharsets.UTF_8));
            long reqTimes = System.currentTimeMillis();
            //对data进行签名
            String sign = getSign(String.join(":", String.valueOf(reqTimes), md5), SECRET_KEY);
            String s = ACCESS_KEY + ":" + sign;

            String back = HttpRequest.post("http://47.110.39.210:6023/api/swProduct/saveSwProductJpPb")
                    .header("request-time", String.valueOf(reqTimes))
                    .header("request-auth", s)
                    .body(String.valueOf(sendJSONObject))
                    .timeout(20000)
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back);
            String data = jsonObject1.get("code").toString();
            if ("200".equals(data)) {
                log.info(String.format("义东水稳级配推送成功!" + id + "Json数据" + sendJSONObject));
            } else {
                log.info(String.format("义东水稳级配推送失败!" + id + "Json数据" + sendJSONObject));
            }
            sysConfigService.updateSysConfig(JobUtil.JTJT_SWPHB, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(sendJSONObject), selectsysconfigone.getRemark(), back);
        }
    }
}
