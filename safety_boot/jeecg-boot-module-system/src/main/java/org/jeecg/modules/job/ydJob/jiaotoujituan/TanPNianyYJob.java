package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.trtm.iot.hc_constructionresults.service.IHcConstructionresultsService;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_machine.service.IHcMachineService;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
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
 * @ClassName TanPNianyYJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/4/20 14:14
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TanPNianyYJob implements Job {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IHcConstructionresultsService constructionresultsService;
    @Autowired
    private IOpenapigpsdatavoService openapigpsdatavoService;
    @Autowired
    private IHcMachineService machineService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    private String url = "http://47.110.39.210:6023/api/lqProduct/saveLqProductQcRawData";

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
            f = String.format("%.5f", (float) mm);     // 使用 float 类型进行格式化
            f = "0" + f;
        } else {
            f = String.format("%.5f", (float) mm);     // 使用 float 类型进行格式化
        }
        String result = String.format("%02d%s", dd, f);
        return result;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_TPNY);//东交（浙高建）摊铺碾压
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东摊铺碾压定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东摊铺碾压的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Openapigpsdatavo> OpenapigpsdatavoList = openapigpsdatavoService.getListjt(shebeilist, curid);
        if (null == OpenapigpsdatavoList || OpenapigpsdatavoList.size() == 0) {
            log.info(String.format("暂无义东摊铺碾压未推送数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"); // 时间格式化器
        int id = 0;
        for (Openapigpsdatavo Openapigpsdatavo : OpenapigpsdatavoList) {
            JSONObject sendDate = new JSONObject();
            id = Openapigpsdatavo.getSjid();

            String machineid = Openapigpsdatavo.getMachineId();
            QueryWrapper<HcMachine> machineQueryWrapper = new QueryWrapper<>();
            machineQueryWrapper.eq("machineid",machineid);
            HcMachine one = machineService.getOne(machineQueryWrapper);

//            sendDate.set("deviceNum","YD_TJ01_"+one.getTerminalsncode());  //心跳(设备编号)
            sendDate.set("deviceNum",one.getTerminalsncode());  //心跳(设备编号)
            String temperature = Openapigpsdatavo.getTemperature();
            String[] split = temperature.split(";");
            double[] temperatureArray = new double[split.length];  // 新建 double 类型的数组
            for (int i = 0; i < split.length; i++) {
                temperatureArray[i] = Double.parseDouble(split[i]);  // 将字符串转换为 double 类型并存储到数组中
            }
            String T = split[0];
            double v = Double.parseDouble(Openapigpsdatavo.getVelocity());
            Integer machinetypecode = one.getMachinetypecode();
            Integer deviceType = 0;
            if (machinetypecode==5){
                deviceType = 1;
            }
            if (machinetypecode==3||machinetypecode==4||machinetypecode==0){
                deviceType = 2;
                v = v / 1000.0 * 60.0;
            }
            sendDate.set("deviceType",deviceType);  //设备类型1：摊铺机；2：压路机
            sendDate.set("temp",T);  //温度


            sendDate.set("temps",temperatureArray);  //全部摊铺温度
            sendDate.set("speed",v);  //速度摊铺机：m/min；压路机：km/h

            String longitude = Openapigpsdatavo.getLon();
            String latitude = Openapigpsdatavo.getLat();
            int longitudeDegree = (int) Double.parseDouble(longitude);// 取整数部分为度数部分
            double longitudeMinute = (Double.parseDouble(longitude) - longitudeDegree) * 60.0;  // 将小数部分转换为分数部分
            String longitudeDDMM = convertDMS2DDMM(longitudeDegree, longitudeMinute);    // 转换为ddmm.mmmmmmmmmmm格式

            int latitudeDegree = (int) Double.parseDouble(latitude);                  // 取整数部分为度数部分
            double latitudeMinute = (Double.parseDouble(latitude) - latitudeDegree) * 60.0;    // 将小数部分转换为分数部分
            String latitudeDDMM = convertDMS2DDMM(latitudeDegree, latitudeMinute);   // 转换为ddmm.mmmmmmmmmmm格式

            sendDate.set("longitude",longitudeDDMM);  //经度
            sendDate.set("latitude",latitudeDDMM);  //纬度
            sendDate.set("stationPre","K");  //桩号前缀
            String roadstation = Openapigpsdatavo.getRoadStation();
            if (roadstation.equals("0.0")){
                roadstation = "0";
            }
            sendDate.set("station",roadstation);  //桩号

            String gpstime = sdf.format(Openapigpsdatavo.getGpsTime());
            String ymd = sdf1.format(Openapigpsdatavo.getGpsTime());

            QueryWrapper<HcConstructionresults> constructionresultsQueryWrapper = new QueryWrapper<>();
            constructionresultsQueryWrapper.ge("endTime",gpstime);
            constructionresultsQueryWrapper.le("beginTime",gpstime);
            constructionresultsQueryWrapper.eq("projectid",one.getPjid());
            List<HcConstructionresults> list = constructionresultsService.list(constructionresultsQueryWrapper);
            int newOffset = 0;
            int layer = 0;
            if (list.size()>0) {
                String offset = list.get(0).getOffset();
                newOffset = offset.equals("1") ? 1 : 0;
                layer = Integer.parseInt(list.get(0).getLayerindex());
                if (layer>2){
                    layer = 3;
                }
            }
            sendDate.set("side",newOffset);  //左右幅
            sendDate.set("layer",layer);  //sup-20   1   sma-13   2
            sendDate.set("time",gpstime);  //时间

            String stage = "0";
//            String machinename = one.getMachinename();
//            if (machinename.contains("初压")){
//                stage = "0";
//            }
//            if (machinename.contains("复压")){
//                stage = "1";
//            }
//            if (machinename.contains("终压")){
//                stage = "2";
//            }
            sendDate.set("stage",stage);  //压实阶段0：初压；1：复压；2：终压    摊铺机默认传 0

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
                log.info(String.format("义东摊铺碾压推送成功!" + id +"body: "+ back));
            } else {
                log.info(String.format("义东摊铺碾压推送失败!" + id + "Json数据" + sendDate));
            }
            sysConfigService.updateSysConfig(JobUtil.JTJT_TPNY, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
