package org.jeecg.modules.job.ydJob.dongjiao;

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
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsPave;
import com.trtm.iot.hc_transportrecords.service.IHcTransportrecordsPaveService;
import com.trtm.iot.hc_transportrecords.service.IHcTransportrecordsService;
import com.trtm.iot.hc_truck.entity.HcTruck;
import com.trtm.iot.hc_truck.service.IHcTruckService;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.CoordinateTransformUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
public class hhlysJob implements Job {
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.DJ_YSC);//运输车
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
        List<HcDatalinkage> transportrecordsList = transportrecordsService.getList(shebeilist, curid);
        if (null == transportrecordsList || transportrecordsList.size() == 0) {
            log.info(String.format("暂无义东运输车未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (HcDatalinkage transportrecords : transportrecordsList) {
            JSONObject sendDate = new JSONObject();

            QueryWrapper<HcDatalinkagePave> transportrecordsPaveQueryWrapper = new QueryWrapper<>();
            transportrecordsPaveQueryWrapper.eq("zbid",transportrecords.getId());
            List<HcDatalinkagePave> transportrecordsPaveList = transportrecordsPaveService.list(transportrecordsPaveQueryWrapper);
            for (HcDatalinkagePave hcTransportrecordsPave : transportrecordsPaveList) {
                sendDate.set("equNum", "ydlm1blq_01"); // 拌和站编号
                String machineid = hcTransportrecordsPave.getMachineid();
                sendDate.set("pviEquNum", machineid); // 摊铺机编号
                sendDate.set("vehicleNo", transportrecords.getTruckid()); // 运输车编号

                String startpavetime = hcTransportrecordsPave.getStartpavetime();
                String endpavetime = hcTransportrecordsPave.getEndpavetime();
                sendDate.set("pviStartTime", startpavetime); // 摊铺开始时间
                sendDate.set("pviEndTime", endpavetime); // 摊铺结束时间

                QueryWrapper<Openapigpsdatavo> openapigpsdatavoQueryWrapper = new QueryWrapper<>();
                openapigpsdatavoQueryWrapper.eq("machine_id",machineid);
                openapigpsdatavoQueryWrapper.eq("gps_Time",startpavetime);
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
                openapigpsdatavoQueryWrapper1.eq("gps_Time",endpavetime);
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

                sendDate.set("pviStartGeox", pviStartGeox); // 摊铺开始经度
                sendDate.set("pviStartGeoy", pviStartGeoy); // 摊铺开始纬度
                sendDate.set("pviEndGeox", pviEndGeox); // 摊铺结束经度
                sendDate.set("pviEndGeoy", pviEndGeoy); // 摊铺结束纬度
                sendDate.set("pviStartPile", zhuanghao(hcTransportrecordsPave.getStartpavestation())); // 摊铺开始桩号
                sendDate.set("pviEndPile", zhuanghao(hcTransportrecordsPave.getEndpavestation())); // 摊铺结束桩号

                sendDate.set("leftRight", offset); // 摊铺左右幅

                QueryWrapper<HcTruck> truckQueryWrapper = new QueryWrapper<>();
                truckQueryWrapper.eq("truckId",transportrecords.getTruckid());
                HcTruck hcTruck = truckService.getOne(truckQueryWrapper);

                QueryWrapper<Wzycljinchanggb> wzycljinchanggbQueryWrapper = new QueryWrapper<>();
                wzycljinchanggbQueryWrapper.eq("qianchepai", hcTruck.getLicenseplate())
                        .isNotNull("chuchangshijian")
                        .orderByAsc("ABS(TIMESTAMPDIFF(SECOND, chuchangshijian, '"+ transportrecords.getOutstationtime()+"'))")
                        .last("LIMIT 1");
                Wzycljinchanggb wzycljinchanggbServiceOne = wzycljinchanggbService.getOne(wzycljinchanggbQueryWrapper);

                sendDate.set("weight", wzycljinchanggbServiceOne.getJingzhong()); // 运输重量


                sendDate.set("loadEndTime", transportrecords.getOutstationtime()); // 接料结束时间
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

                String body = HttpRequest.post("http://123.60.37.16:443/receive/dataReceive/transport")
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();

                if (body.contains("200")) {
                    log.info(String.format("义东混合料运输推送成功!" + id));
                    transportrecords.setIsdj(1);
                } else {
                    log.info(String.format("义东混合料运输推送失败!" + id + "Json数据" + sendDate));
                    transportrecords.setIsdj(2);
                }
                transportrecordsService.saveOrUpdate(transportrecords);
                pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),body);
            }
        }
    }

    public String zhuanghao(String roadstation){

        if (!roadstation.contains("+")) {

            roadstation = "K0+000";
        }
        return roadstation;
    }
}
