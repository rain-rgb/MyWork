package org.jeecg.modules.job.ydJob.dongjiao;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.spreadandcrush_car.entity.SpreadandcrushCar;
import com.trtm.iot.spreadandcrush_car.service.ISpreadandcrushCarService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
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
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName GuiJiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/4/20 16:26
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class hyGuiJiJob implements Job {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISpreadandcrushCarService spreadandcrushCarService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    public static String zbzh(double tude) {

        double degrees = Math.floor(tude);
        double minutes = (tude - degrees) * 60;
        double seconds = (minutes - Math.floor(minutes)) * 60;

        String formattedLongitude = String.format("%d%02d.%06d", (int)degrees, (int)Math.floor(minutes), (int)(seconds * 1e4));
        return formattedLongitude;
    }
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HY_YSC);//运输车
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东运输车定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东运输车的设备" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        String shebeilist = jsonObject.getStr("shebeilist");
        List<SpreadandcrushCar> spreadandcrushCarList = spreadandcrushCarService.getList(shebeilist,curid);
        if (null == spreadandcrushCarList || spreadandcrushCarList.size() == 0) {
            log.info(String.format("暂无义东运输车未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (SpreadandcrushCar spreadandcrushCar : spreadandcrushCarList) {
            id = spreadandcrushCar.getId();
            JSONObject sendDate = new JSONObject();

            sendDate.set("deviceNum",spreadandcrushCar.getTruckid());
            double speedKmh = spreadandcrushCar.getSpeed() * 0.06;

            sendDate.set("speed",speedKmh);//速度

            String longitude = spreadandcrushCar.getLongitude();
            String latitude = spreadandcrushCar.getLatitude();
            String longitudeDDMM = zbzh(Double.parseDouble(longitude));
            String latitudeDDMM = zbzh(Double.parseDouble(latitude));

            sendDate.set("longitude",longitudeDDMM);
            sendDate.set("latitude",latitudeDDMM);
            sendDate.set("temp","");
            sendDate.set("time",sdf.format(spreadandcrushCar.getLoctime()));

            System.out.println(sendDate);
            String md5 = DigestUtils.md5DigestAsHex(String.valueOf(sendDate).getBytes(StandardCharsets.UTF_8));
            long reqTimes = System.currentTimeMillis();
            //对data进行签名
            String sign = getSign(String.join(":", String.valueOf(reqTimes), md5), SECRET_KEY);
            String s = ACCESS_KEY + ":" + sign;

            String back = HttpRequest.post("http://47.110.39.210:6023/api/lqProduct/saveLqProductTransportTrack")
                    .header("request-time", String.valueOf(reqTimes))
                    .header("request-auth", s)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();
            spreadandcrushCarService.saveOrUpdate(spreadandcrushCar);
            sysConfigService.updateSysConfig(JobUtil.HY_YSC, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id,String.valueOf(sendDate),"运输车",back);
        }
    }
}
