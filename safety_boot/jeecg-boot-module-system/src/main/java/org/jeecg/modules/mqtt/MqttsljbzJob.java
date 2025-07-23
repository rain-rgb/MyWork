package org.jeecg.modules.mqtt;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicemixpilehistorydatapart.entity.DeviceMixpileHistorydataPart;
import com.trtm.iot.devicemixpilehistorydatapart.service.IDeviceMixpileHistorydataPartService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lis1
 * @date 2023/6/7
 * @time 8:43
 */
@Slf4j
public class MqttsljbzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceMixpileHistorydataOneService mixpileHistorydataOneService;
    @Autowired
    private IDeviceMixpileHistorydataPartService mixpileHistorydataPartService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.MQTTSLJBZ_JOB);//MQTT集团软基处理数据接入对接

        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到MQTT集团软基处理数据接入对接的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneService.selectListss(shebeilist, curid);
        int ids = 0;
        for (DeviceMixpileHistorydataOne deviceMixpileHistorydataOne :deviceMixpileHistorydataOnes){
            int id = deviceMixpileHistorydataOne.getId();
            ids ++;
            toPush(deviceMixpileHistorydataOne,deviceMixpileHistorydataOnes.size(),ids);
            sysConfigService.updateSysConfig(JobUtil.MQTTSLJBZ_JOB, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }

    public void toPush(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,int size,int ids){
        String transmit = null;
        try {
            MqttClient mqttClient = new MqttClient("tcp://114.55.65.177:1883", "client", new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("jtjcgs");
            options.setPassword("jtjcgs".toCharArray());
            mqttClient.connect(options);
            String totalPackage = byteArrayToFrame("01".getBytes());//总包数
            String currentPackage = byteArrayToFrame("01".getBytes());//当前包
            String version = byteArrayToFrame("1.00".getBytes());//版本号
            //设备编号命名
            String shbei = setshebei(deviceMixpileHistorydataOne.getShebeino());
            String devicekey = fillZero(byteArrayToFrame(shbei.getBytes()), 32);//设备号

            String machinekey = fillZero(byteArrayToFrame(deviceMixpileHistorydataOne.getPileMileage().getBytes()), 64);//桩机号
            String pileId = fillZero(Long.toHexString(deviceMixpileHistorydataOne.getId()), 16);//桩号唯一标识
            String pileName = fillZero(byteArrayToFrame(deviceMixpileHistorydataOne.getPileNo().getBytes()), 64);//桩号
            //开始时间计算
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String pileTime = deviceMixpileHistorydataOne.getPileTime();
            Date end = dateFormat.parse(pileTime);
            String downtimeStr = deviceMixpileHistorydataOne.getPileDowntime();
            String uptimeStr = deviceMixpileHistorydataOne.getPileUptime();
            Date date = new Date();
            try {
                double downtime = Double.parseDouble(downtimeStr);
                double uptime = Double.parseDouble(uptimeStr);

                // 根据需求选择取整方式，这里依然采用下取整
                int downtimeInt = (int) Math.floor(downtime);
                int uptimeInt = (int) Math.floor(uptime);

                int time = downtimeInt + uptimeInt;
                Long starttimes = end.getTime() - time * 1000;
                date = new Date(starttimes);
            } catch (NumberFormatException e) {
                // 即便前面已经做了检查，但理论上这里仍然可能捕获到异常，例如数字超出整型范围
                System.err.println("Unexpected error during conversion: " + e.getMessage());
            }
            String beginTime = fillZero(Long.toHexString(date.getTime() / 1000), 16);//起始时间
            Date parse = dateFormat.parse(pileTime);
            String endTime = fillZero(Long.toHexString(parse.getTime() / 1000), 16);//结束时间

            String processType = fillZero(byteArrayToFrame("pt".getBytes()), 16);//工艺类型

            String pileLgd = deviceMixpileHistorydataOne.getPileLgd();
            String pileLtd = deviceMixpileHistorydataOne.getPileLtd();
            String longgitude = fillZero(Long.toHexString(Double.doubleToLongBits(Double.parseDouble(pileLgd))), 16);//设备经度坐标
            String latitude = fillZero(Long.toHexString(Double.doubleToLongBits(Double.parseDouble(pileLtd))), 16);//设备纬度坐标
            String holeLongitude = fillZero(Long.toHexString(Double.doubleToLongBits(Double.parseDouble(pileLgd))), 16);//桩孔经度
            String holeLatitude = fillZero(Long.toHexString(Double.doubleToLongBits(Double.parseDouble(pileLtd))), 16);//桩孔纬度

            float aFloat = Float.parseFloat(deviceMixpileHistorydataOne.getPileRealdep());
            String depth = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat)), 8);//桩长
            String fristDepth = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat)), 8);//初搅深度
            String repeatDepth = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat)), 8);//复搅深度
            String emptyDepth = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat)), 8);//空搅深度
            float ratio = Float.parseFloat(deviceMixpileHistorydataOne.getPileRatio());
            String waterCementRatio = fillZero(Integer.toHexString(Float.floatToIntBits(ratio)), 8);//水灰比
            float dspeed = Float.parseFloat(deviceMixpileHistorydataOne.getPileDspeed());
            String downSpeed = fillZero(Integer.toHexString(Float.floatToIntBits(dspeed)), 8);//平均下钻速度
            float aFloat1 = Float.parseFloat(deviceMixpileHistorydataOne.getPileUspeed());
            String upSpeed = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat1)), 8);//平均提钻速度
            float aFloat2 = Float.parseFloat(deviceMixpileHistorydataOne.getPileFlowtotal());
            String cumulativePulp = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat2)), 8);//累计浆量
            float aFloat3 = aFloat2/aFloat;
            String averagePulp = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat3)), 8);//平均浆量
            float mixwb = mixwb(aFloat2, aFloat, ratio);
            String cumulativeAsh = fillZero(Integer.toHexString(Float.floatToIntBits(mixwb)), 8);//累计灰量
            float v = mixwb / aFloat;
            String averageAsh = fillZero(Integer.toHexString(Float.floatToIntBits(v)), 8);//平均灰量
            float aFloat4 = 0.0F;
            if (deviceMixpileHistorydataOne.getPileMaxelec() != null){
                 aFloat4 = Float.parseFloat(deviceMixpileHistorydataOne.getPileMaxelec());
            }
            String maxCurrent = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat4)), 8);//最大钻杆电流
            float aFloat5 = 0.0F;
            if (deviceMixpileHistorydataOne.getPileUelectr() != null){
                aFloat5 = Float.parseFloat(deviceMixpileHistorydataOne.getPileUelectr());
            }
            String averageCurrent = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat5)), 8);//平均电流
            float aFloat6 = 0.0F;
            if (deviceMixpileHistorydataOne.getPileDspeed() != null){
                aFloat6 = Float.parseFloat(deviceMixpileHistorydataOne.getPileDspeed());
            }
            String maxDownSpeed = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat6)), 8);//最大钻速
            float aFloat7 = 0.0F;
            if (deviceMixpileHistorydataOne.getPileUspeed() != null){
                aFloat7 = Float.parseFloat(deviceMixpileHistorydataOne.getPileUspeed());
            }
            String maxUpSpeed = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat7)), 8);//最大提速
            float aFloat8 = 0.0F;
            if (deviceMixpileHistorydataOne.getPileX() != null){
                if (deviceMixpileHistorydataOne.getPileX().equals("-")){
                    String substring = deviceMixpileHistorydataOne.getPileX().substring(1);//去掉-
                    aFloat8 = Float.parseFloat(substring);
                }else {
                    aFloat8 = Float.parseFloat(deviceMixpileHistorydataOne.getPileX());
                }
            }
            String maxSlope = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat8)), 8);//最大斜度
            float aFloat9 = 0.0F;
            if (deviceMixpileHistorydataOne.getPileWorktime() != null){
                aFloat9 = Float.parseFloat(deviceMixpileHistorydataOne.getPileWorktime());
            }
            String sprayed_time = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat9)), 8);//喷浆时间
            float density = 0.0F;
            if (deviceMixpileHistorydataOne.getPileDensity() != null){
                density = Float.parseFloat(deviceMixpileHistorydataOne.getPileDensity());
            }
            String averageDensity = fillZero(Integer.toHexString(Float.floatToIntBits(density)), 8);//平均密度
            float aFloat10 = 0.0F;
            if (deviceMixpileHistorydataOne.getPileDesigndep() != null){
                aFloat10 = Float.parseFloat(deviceMixpileHistorydataOne.getPileDesigndep());
            }
            String designDepth = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat10)), 8);//设计深度
            String emptyPulp = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat3)), 8);//空搅浆量
            String emptyAsh = fillZero(Integer.toHexString(Float.floatToIntBits(v)), 8);//空搅灰量
            String bottom_part_ash = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat2)), 8);//下部桩浆量

            QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebeino",deviceMixpileHistorydataOne.getShebeino());
            queryWrapper.eq("pile_no",deviceMixpileHistorydataOne.getPileNo());
            queryWrapper.eq("pile_mileage",deviceMixpileHistorydataOne.getPileMileage());
            List<DeviceMixpileHistorydataPart> list = mixpileHistorydataPartService.list(queryWrapper);
            if (list.size() == 0){
                QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("shebeino",deviceMixpileHistorydataOne.getShebeino());
                queryWrapper1.eq("pile_no",deviceMixpileHistorydataOne.getPileNo());
                list = mixpileHistorydataPartService.list(queryWrapper1);
            }
            String partCount = fillZero(Long.toHexString(list.size()), 8);//段总数

            transmit = totalPackage + currentPackage + version + devicekey + machinekey + pileId + pileName + beginTime + endTime +
                    processType + longgitude + latitude + holeLongitude + holeLatitude + depth + fristDepth + repeatDepth + emptyDepth +
                    waterCementRatio + downSpeed + upSpeed + cumulativePulp + averagePulp + cumulativeAsh + averageAsh + maxCurrent + averageCurrent +
                    maxDownSpeed + maxUpSpeed + maxSlope + sprayed_time + averageDensity + designDepth + emptyPulp + emptyAsh + bottom_part_ash + partCount;
            int i = 0;
            for (DeviceMixpileHistorydataPart l :list){
                i ++;
                String part_id = fillZero(Long.toHexString(i), 8);//段序号
                int aFloat11 = 0;
                if (l.getPartTime() != null){
                    aFloat11 = Integer.parseInt(l.getPartTime());
                }
                String p_time = fillZero(Long.toHexString(aFloat11), 8);//段时长
                float aFloat12 = 0;
                if (l.getPartDep() != null){
                    aFloat12 = Float.parseFloat(l.getPartDep());
                }
                String p_deep = fillZero(Long.toHexString((long) aFloat12), 8);//段深度
                float aFloat13 = 0;
                if (l.getPartBeton() != null){
                    aFloat13 = Float.parseFloat(l.getPartBeton());
                }
                String p_pulp = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat13)), 8);//段浆量
                float aFloat14 = 0;
                if (l.getPartElectricity() != null){
                    aFloat14 = Float.parseFloat(l.getPartElectricity());
                }
                String p_current = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat14)), 8);//段电流

                String p_pressure = fillZero(Integer.toHexString(Float.floatToIntBits(1.23f)), 8);//段喷压
                float aFloat15 = 0;
                if (l.getPileDensity() != null){
                    aFloat15 = Float.parseFloat(l.getPileDensity());
                }
                String p_density = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat15)), 8);//段密度
                int aFloat16 = 0;
                if (l.getPartState() != null){
                    aFloat16 = Integer.parseInt(l.getPartState());
                }
                String p_down_speed = fillZero(Integer.toHexString(Float.floatToIntBits(aFloat16)), 8);//段钻/提速
                transmit = transmit + part_id + p_time + p_deep + p_pulp + p_current + p_pressure + p_density + p_down_speed;
            }
            System.out.println(transmit);
            MqttMessage message = new MqttMessage(hexStringToByteArray(transmit));
            mqttClient.publish("civtcp/h/"+shbei, message);
            mqttClient.disconnect();
            mqttClient.close();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    //计算灰量
    public float mixwb(Float aFloat2, Float pileDensity, Float pileRatio) {
        if (pileRatio > 1){
            pileRatio = pileRatio / 100;
        }
        float wb = (aFloat2 * pileDensity)/(1 + pileRatio);
        return wb;
    }

    //设备
    public String setshebei(String shebei) {
        if (shebei.equals("ytw3bsnjbz02")){
            shebei = "MX028081tw3b2";
        }else if (shebei.equals("ytw3bsnjbz01")){
            shebei = "MX028081tw3b1";
        }else if (shebei.equals("sszw20240401")){
            shebei = "MX028081lj2d1";
        }else if (shebei.equals("2024032003")){
            shebei = "MX028081lj1d1";
        }else if (shebei.equals("2024032002")){
            shebei = "MX028081lj2d6";
        }else if (shebei.equals("2024032001")){
            shebei = "MX028081lj3d7";
        }else if (shebei.equals("2024030701")){
            shebei = "MX028081cs1";
        }else if (shebei.equals("cs12345678")){
            shebei = "MX028081cs2";
        }else if (shebei.equals("2023112508")){
            shebei = "MX028081tj2d3";
        }else if (shebei.equals("2023112507")){
            shebei = "MX028081tj2d2";
        }else if (shebei.equals("SNJBZ_11276")){
            shebei = "MX028081lj3bz";
        }else if (shebei.equals("2023102803")){
            shebei = "MX028081tj3d1";
        }else if (shebei.equals("sszw20231019")){
            shebei = "MX028081tj1d1";
        }else if (shebei.equals("cs20231210")){
            shebei = "MX028081ml1";
        }else if (shebei.equals("2023111304")){
            shebei = "MX028081tj1d2";
        }
        return shebei;
    }

    public static byte[] hexStringToByteArray(String string) {
        if (!StringUtils.isEmpty(string)) {
            byte[] bytes = new byte[string.length() / 2];
            for (int i = 0; i < string.length() / 2 * 2; i += 2) {
                String hex = string.substring(i, i + 2);
                bytes[i / 2] = (byte) Short.parseShort(hex, 16);
            }
            return bytes;
        }
        return null;
    }

    public static String byteArrayToFrame(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            if (b < 0) {
                buffer.append(Integer.toHexString(256 + b));
            } else if (b < 16) {
                buffer.append("0" + Integer.toHexString(b));
            } else {
                buffer.append(Integer.toHexString(b));
            }
        }
        return buffer.toString();
    }

    public static String fillZero(String src, int length) {
        if (src.length() >= length) {
            return src;
        }
        String result = src;
        for (int i = 0; i < length - src.length(); i++) {
            result = "0" + result;
        }
        return result;
    }

}
