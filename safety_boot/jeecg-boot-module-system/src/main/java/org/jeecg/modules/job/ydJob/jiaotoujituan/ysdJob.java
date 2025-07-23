package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.trtm.iot.hc_constructionresults.service.IHcConstructionresultsService;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkage;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkagePave;
import com.trtm.iot.hc_datalinkage.service.IHcDatalinkagePaveService;
import com.trtm.iot.hc_datalinkage.service.IHcDatalinkageService;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_machine.service.IHcMachineService;
import com.trtm.iot.hc_truck.entity.HcTruck;
import com.trtm.iot.hc_truck.service.IHcTruckService;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import com.xkcoding.http.util.StringUtil;
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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @ClassName hhlysJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/5/18 11:03
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ysdJob implements Job {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IHcDatalinkageService transportrecordsService;
    @Autowired
    private IHcDatalinkagePaveService transportrecordsPaveService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IOpenapigpsdatavoService openapigpsdatavoService;
    @Autowired
    private IHcConstructionresultsService constructionresultsService;
    @Autowired
    private IHcTruckService truckService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IHcMachineService machineService;
    private String url = "http://47.110.39.210:6023/api/lqProduct/saveLqProductTransport";

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

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_YSD);//运输车
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
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"); // 时间格式化器
        String shebeilist = jsonObject.getStr("shebeilist");
        List<HcDatalinkage> transportrecordsList = transportrecordsService.getListjt(shebeilist, curid);
        if (null == transportrecordsList || transportrecordsList.size() == 0) {
            log.info(String.format("暂无义东运输车未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (HcDatalinkage transportrecords : transportrecordsList) {
            JSONObject sendDate = new JSONObject();
            id = transportrecords.getId();
            QueryWrapper<HcDatalinkagePave> transportrecordsPaveQueryWrapper = new QueryWrapper<>();
            transportrecordsPaveQueryWrapper.eq("zbid",transportrecords.getId());
            List<HcDatalinkagePave> transportrecordsPaveList = transportrecordsPaveService.list(transportrecordsPaveQueryWrapper);
            for (HcDatalinkagePave hcTransportrecordsPave : transportrecordsPaveList) {
                sendDate.set("deviceNum", "YD_LM01_ydlm1blq_01"); // 设备编号
                String machineid = hcTransportrecordsPave.getMachineid();

                QueryWrapper<HcMachine> machineQueryWrapper = new QueryWrapper<>();
                machineQueryWrapper.eq("machineId",machineid);
                HcMachine one = machineService.getOne(machineQueryWrapper);

                sendDate.set("paverNum", "YD_TJ01_" + one.getTerminalsncode()); // 摊铺机编号
                sendDate.set("carNum", "YD_TJ01_" + transportrecords.getTruckid()); // 运输车编号
                sendDate.set("loadEndTime", sdf.format(sdf.parse(transportrecords.getOutstationtime()))); // 接料结束时间

                Date formattedStartPaveTime = sdf.parse(hcTransportrecordsPave.getStartpavetime());
                Date formattedEndPaveTime = sdf.parse(hcTransportrecordsPave.getEndpavetime());
                sendDate.set("paveStartTime", sdf.format(formattedStartPaveTime)); // 摊铺开始时间
                sendDate.set("paveEndTime", sdf.format(formattedEndPaveTime)); // 摊铺结束时间

                QueryWrapper<Openapigpsdatavo> openapigpsdatavoQueryWrapper = new QueryWrapper<>();
                openapigpsdatavoQueryWrapper.eq("machine_id",machineid);
                openapigpsdatavoQueryWrapper.eq("gps_time",formattedStartPaveTime);
                List<Openapigpsdatavo> StartList = openapigpsdatavoService.list(openapigpsdatavoQueryWrapper);
                String pviStartGeox = "0.0";
                String pviStartGeoy = "0.0";
                String pviEndGeox = "0.0";
                String pviEndGeoy = "0.0";
                String offset = "0";
                if (StartList.size()>0){
                    String lat = StartList.get(0).getLat();
                    String lon = StartList.get(0).getLon();
                    double offset1 = Double.parseDouble(StartList.get(0).getOffset());
                    if (offset1>0){
                        offset = "1";
                    }

                    int longitudeDegree = (int) Double.parseDouble(lon);// 取整数部分为度数部分
                    double longitudeMinute = (Double.parseDouble(lon) - longitudeDegree) * 60.0;  // 将小数部分转换为分数部分
                    pviStartGeox = convertDMS2DDMM(longitudeDegree, longitudeMinute);    // 转换为ddmm.mmmmmmmmmmm格式

                    int latitudeDegree = (int) Double.parseDouble(lat);                  // 取整数部分为度数部分
                    double latitudeMinute = (Double.parseDouble(lat) - latitudeDegree) * 60.0;    // 将小数部分转换为分数部分
                    pviStartGeoy = convertDMS2DDMM(latitudeDegree, latitudeMinute);   // 转换为ddmm.mmmmmmmmmmm格式
                }

                QueryWrapper<Openapigpsdatavo> openapigpsdatavoQueryWrapper1 = new QueryWrapper<>();
                openapigpsdatavoQueryWrapper1.eq("machine_id",machineid);
                openapigpsdatavoQueryWrapper1.eq("gps_time",formattedEndPaveTime);
                List<Openapigpsdatavo> EndList = openapigpsdatavoService.list(openapigpsdatavoQueryWrapper1);
                if (EndList.size()>0){
                    String lat = EndList.get(0).getLat();
                    String lon = EndList.get(0).getLon();
                    double offset1 = Double.parseDouble(EndList.get(0).getOffset());
                    if (offset1>0){
                        offset = "1";
                    }

                    int longitudeDegree = (int) Double.parseDouble(lon);// 取整数部分为度数部分
                    double longitudeMinute = (Double.parseDouble(lon) - longitudeDegree) * 60.0;  // 将小数部分转换为分数部分
                    pviEndGeox = convertDMS2DDMM(longitudeDegree, longitudeMinute);    // 转换为ddmm.mmmmmmmmmmm格式

                    int latitudeDegree = (int) Double.parseDouble(lat);                  // 取整数部分为度数部分
                    double latitudeMinute = (Double.parseDouble(lat) - latitudeDegree) * 60.0;    // 将小数部分转换为分数部分
                    pviEndGeoy = convertDMS2DDMM(latitudeDegree, latitudeMinute);   // 转换为ddmm.mmmmmmmmmmm格式
                }

                sendDate.set("paveStartLon", pviStartGeox); // 摊铺开始经度
                sendDate.set("paveStartLat", pviStartGeoy); // 摊铺开始纬度
                sendDate.set("paveEndLon", pviEndGeox); // 摊铺结束经度
                sendDate.set("paveEndLat", pviEndGeoy); // 摊铺结束纬度
                String startpavestation = hcTransportrecordsPave.getStartpavestation();
                String paveEndStation = hcTransportrecordsPave.getEndpavestation();
                if ("范围外施工".equals(startpavestation)){
                    startpavestation = "0.0";
                }else {
                    startpavestation = startpavestation.replaceAll("[K+]", "");
                }
                if ("范围外施工".equals(paveEndStation)){
                    paveEndStation = "0.0";
                }else {
                    paveEndStation = paveEndStation.replaceAll("[K+]", "");
                }
                sendDate.set("paveStartStation", startpavestation); // 摊铺开始桩号
                sendDate.set("paveEndStation", paveEndStation); // 摊铺结束桩号

                sendDate.set("paveSide", offset); // 摊铺左右幅

                QueryWrapper<HcTruck> truckQueryWrapper = new QueryWrapper<>();
                truckQueryWrapper.eq("truckId",transportrecords.getTruckid());
                HcTruck hcTruck = truckService.getOne(truckQueryWrapper);

                QueryWrapper<Wzycljinchanggb> wzycljinchanggbQueryWrapper = new QueryWrapper<>();
                wzycljinchanggbQueryWrapper.eq("qianchepai", hcTruck.getLicenseplate())
                        .isNotNull("chuchangshijian")
                        .orderByAsc("ABS(TIMESTAMPDIFF(SECOND, chuchangshijian, '"+ sdf.parse(transportrecords.getOutstationtime())+"'))")
                        .last("LIMIT 1");
                Wzycljinchanggb wzycljinchanggbServiceOne = wzycljinchanggbService.getOne(wzycljinchanggbQueryWrapper);



                sendDate.set("paveLayer", ""); // 0：下面层；1 中面层；2：上面层；3 其它
                String jingzhong = wzycljinchanggbServiceOne.getJingzhong();
                if (StringUtil.isEmpty(jingzhong)){
                    jingzhong = "0";
                }
                sendDate.set("weight", jingzhong); // 运输重量

                String loadmixturetime = transportrecords.getLoadmixturetime();//接料时长

                // 将字符串类型的时间转换成日期类型
                Date loadEndTime = sdf.parse(transportrecords.getOutstationtime());

                // 计算接料开始时间
                int loadTime = 0;
                if (loadmixturetime.contains("小时")) {
                    // 处理带小时的情况，如 "1小时2分钟18秒"
                    String[] timeParts = loadmixturetime.split("小时|分钟");
                    int hours = Integer.parseInt(timeParts[0]);
                    int minutes = Integer.parseInt(timeParts[1]);
                    int seconds = Integer.parseInt(timeParts[2].replace("秒", ""));
                    loadTime = hours * 3600 + minutes * 60 + seconds;
                } else {
                    // 处理不带小时的情况，如 "12分钟6秒"
                    String[] timeParts = loadmixturetime.split("分钟");
                    int minutes = Integer.parseInt(timeParts[0]);
                    int seconds = Integer.parseInt(timeParts[1].replace("秒", ""));
                    loadTime = minutes * 60 + seconds;
                }
                long loadStartTimeMillis = loadEndTime.getTime() - loadTime * 1000;
                Date loadStartTime = new Date(loadStartTimeMillis);
                sendDate.set("loadStartTime", sdf.format(loadStartTime));

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
                pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),back);
                if (back.contains("200")) {
                    log.info(String.format("义东混合料运输推送成功!" + id));
                } else {
                    log.info(String.format("义东混合料运输推送失败!" + id + "Json数据" + sendDate));
                }
                sysConfigService.updateSysConfig(JobUtil.JTJT_YSD, id);//根据传过来的唯一值来修改当前判断到的数据id
            }
        }
    }
}
