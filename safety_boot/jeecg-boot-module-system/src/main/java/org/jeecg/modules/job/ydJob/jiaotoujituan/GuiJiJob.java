package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_truck.entity.HcTruck;
import com.trtm.iot.hc_truck.service.IHcTruckService;
import com.trtm.iot.skip_car.entity.SkipCar;
import com.trtm.iot.skip_car.service.ISkipCarService;
import com.trtm.iot.skip_car_device.entity.SkipCarDevice;
import com.trtm.iot.skip_car_device.service.ISkipCarDeviceService;
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
public class GuiJiJob implements Job {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISpreadandcrushCarService spreadandcrushCarService;
    @Autowired
    private IHcTruckService truckService;
    @Autowired
    private ISkipCarDeviceService skipCarDeviceService;
    @Autowired
    private ISkipCarService skipCarService;
    private String url = "http://47.110.39.210:6023/api/lqProduct/saveLqProductTransportTrack";

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

    /**
     * 将度分格式的经纬度转换为十进制度数
     * @param degree 分离度分格式的度部分
     * @param minute 分离度分格式的分部分
     * @return 十进制度数
     */
    public static double convertDMS2Decimal(double degree, double minute) {
        return degree + minute / 60.0;
    }

    /**
     * 将度分格式的经纬度转换为ddmm.mmmmmmmmmmm格式
     * @param degree 分离度分格式的度部分
     * @param minute 分离度分格式的分部分
     * @return 转换后的ddmm.mmmmmmmmmmm格式的经纬度
     */
    public static String convertDMS2DDMM(double degree, double minute) {
        // 计算度分部分对应的十进制度数
        double decimalDegree = convertDMS2Decimal(degree, minute);
        // 将十进制度数转换为ddmm.mmmmmmmmmmm格式
        int dd = (int) decimalDegree;                  // 取整数部分为度数部分
        double mm = (decimalDegree - dd) * 60.0;       // 将小数部分转换为分数部分
        String f;
        if (mm < 10) {
            f = String.format("%.11f", (float) mm);     // 使用 float 类型进行格式化
            f = "0" + f;
        } else {
            f = String.format("%.11f", (float) mm);     // 使用 float 类型进行格式化
        }
        String result = String.format("%02d%s", dd, f);
        return result;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_LSGJ);//运输车
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
        List<SpreadandcrushCar> spreadandcrushCarList = spreadandcrushCarService.getListjt(shebeilist, curid);
        if (null == spreadandcrushCarList || spreadandcrushCarList.size() == 0) {
            log.info(String.format("暂无义东运输车未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (SpreadandcrushCar spreadandcrushCar : spreadandcrushCarList) {
            id = spreadandcrushCar.getId();
            JSONObject sendDate = new JSONObject();

            sendDate.set("deviceNum", "YD_TJ01_" + spreadandcrushCar.getTruckid());//设备编号
            Double speed = spreadandcrushCar.getSpeed();
            speed = speed / 1000.0 * 60.0;
            sendDate.set("speed", speed);//速度 单位：km/h

            QueryWrapper<HcTruck> truckQueryWrapper = new QueryWrapper<>();
            truckQueryWrapper.eq("truckId", spreadandcrushCar.getTruckid());
            HcTruck one = truckService.getOne(truckQueryWrapper);

            QueryWrapper<SkipCarDevice> skipCarDeviceQueryWrapper = new QueryWrapper<>();
            skipCarDeviceQueryWrapper.eq("car_number", one.getLicenseplate());
            SkipCarDevice one1 = skipCarDeviceService.getOne(skipCarDeviceQueryWrapper);

            String devicenum = one1.getDevicenum();
            QueryWrapper<SkipCar> skipCarQueryWrapper = new QueryWrapper<>();
            skipCarQueryWrapper.eq("deviceId", devicenum)
                    .notLike("temperature", "?")
                    .ne("temperature", "")
                    .orderByAsc("ABS(TIMESTAMPDIFF(SECOND, collectionTime, '" + sdf.format(spreadandcrushCar.getLoctime()) + "'))")
                    .last("LIMIT 1");
            SkipCar one2 = skipCarService.getOne(skipCarQueryWrapper);
            double temp = 0.0;
            if (one2 != null) {
                temp = Double.parseDouble(one2.getTemperature());
            }
            sendDate.set("temp", temp);//单位：℃

            String longitude = spreadandcrushCar.getLongitude();
            String latitude = spreadandcrushCar.getLatitude();

            int longitudeDegree = (int) Double.parseDouble(longitude);// 取整数部分为度数部分
            double longitudeMinute = (Double.parseDouble(longitude) - longitudeDegree) * 60.0;  // 将小数部分转换为分数部分
            String longitudeDDMM = convertDMS2DDMM(longitudeDegree, longitudeMinute);    // 转换为ddmm.mmmmmmmmmmm格式

            int latitudeDegree = (int) Double.parseDouble(latitude);                  // 取整数部分为度数部分
            double latitudeMinute = (Double.parseDouble(latitude) - latitudeDegree) * 60.0;    // 将小数部分转换为分数部分
            String latitudeDDMM = convertDMS2DDMM(latitudeDegree, latitudeMinute);   // 转换为ddmm.mmmmmmmmmmm格式

            sendDate.set("longitude", longitudeDDMM);
            sendDate.set("latitude", latitudeDDMM);
            sendDate.set("time", sdf.format(spreadandcrushCar.getLoctime()));

            String md5 = DigestUtils.md5DigestAsHex(String.valueOf(sendDate).getBytes(StandardCharsets.UTF_8));
            long reqTimes = System.currentTimeMillis();
            //对data进行签名
            String sign = getSign(String.join(":", String.valueOf(reqTimes), md5), SECRET_KEY);
            String s = ACCESS_KEY + ":" + sign;

            String back = HttpRequest.post(url)
                    .header("request-time", String.valueOf(reqTimes))
                    .header("request-auth", s)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();
            if (back.contains("200")) {
                log.info(String.format("义东运输车推送成功!" + id));

            } else {
                log.info(String.format("义东运输车推送失败!" + id + "Json数据" + sendDate));
            }
            sysConfigService.updateSysConfig(JobUtil.JTJT_LSGJ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
