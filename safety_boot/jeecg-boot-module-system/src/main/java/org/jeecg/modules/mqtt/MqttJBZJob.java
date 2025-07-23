package org.jeecg.modules.mqtt;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileHistorydataOnePage;
import com.trtm.iot.devicemixpilehistorydatapart.entity.DeviceMixpileHistorydataPart;
import com.trtm.iot.devicemixpilehistorydatapart.service.IDeviceMixpileHistorydataPartService;
import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.trtm.iot.devicemixpilerealdata.service.IDeviceMixpileRealdataService;
import com.trtm.iot.devicemixpilerwd.entity.DeviceMixpileRwdMqtt;
import com.trtm.iot.devicemixpilerwd.service.IDeviceMixpileRwdMqttService;
import com.trtm.iot.devicemixpilerwdlog.entity.DeviceMixpileRwdLog;
import com.trtm.iot.devicemixpilerwdlog.service.IDeviceMixpileRwdLogService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lis1
 * @date 2023/6/7
 * @time 8:43
 */
@Slf4j
public class MqttJBZJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceMixpileRealdataService deviceMixpileRealdataService;
    @Autowired
    private IDeviceMixpileHistorydataPartService deviceMixpileHistorydataPartService;
    @Autowired
    private IDeviceMixpileRwdMqttService deviceMixpileRwdMqttService;

    @Autowired
    private IDeviceMixpileRwdLogService deviceMixpileRwdLogService;

    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;


    // 实时数据 公共部分 版本号	实时时间 设备号 接入编号 桩机号 桩起始时间
    private  static  int[] len1R ={4,8,16,16,32,8};
    private  static String[] sTypeR1={"char","long","char","char","char","long"};
    private  static int[] len2R={8,   32,   8,   8,   8,   1,   1,   1,   1,   4,   4,   4,   4,   4,   4,   4,   4,   4,   4,   4,   4,   4,   4};
    private  static String[] sType2={ "longlong","char","char","double","double ","char","char","char","char","float ","float ","float ","float ","float ","float ","float ","float ","float ","float ","float ","float ","float ","float "
    };
    // 历史数据
    private  static int[] len2H={
            //总包数  当前包 |桩号|版本号|设备号|接入编号|桩机号|桩号描述|起始时间|结束时间|工艺类型|设备经度坐标|设备纬度坐标|桩孔经度|桩孔纬度|桩长|初搅深度|复搅深度|空搅深度|水灰比|平均下钻速度|平均提钻速度|累计浆量|平均浆量|累计灰量|平均灰量|设计桩长|设计灰量|平均压力|钻速预警|提速预警|左右倾角|前后倾角|扩大头桩长|扩大头浆量|下部桩浆量|扩大桩灰量|下部桩灰量|段总数|段序号|段时长|段深度|段浆量|段电流|段喷压|段密度|段钻/提速|段灰量
            4,      4,      8,  4,      16,    16,  32,    32,     8,       8,     8,     8,        8,          8,     8,   4,     4,     4,    4,       4,     4,       4,          4,      4,      4,     4,    4,      4,     4,      4,      4,      4,     4,   4,         4,         4,       4,      4,      4,   4,     4,   4,   4,     4,     4,     4,   4,    4
    };

    private  static String typeH[] = {
            "long","long","longlong","char","char","char","char","char","longlong","longlong","char","double","double","double","double ","float","float","float","float","float","float","float","float","float ","float ","float","float","float","float","float","float","float","float","float","float","float","float","float","long","long","long","long","float","float","float","float","float","float"
    };

    private  static int[] len2P={
            4,4,4,4,4,4,4,4,4
    };
    private  static String typeP[] = {
           "long","long","long","float","float","float","float","float","float"
    };
    private  static int[] len2P1={ 4,4,8 };
    private  static String typeP1[] = { "long","long","longlong"};
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJQS_MQTT_SNJBZ);//MQTT开关
        String broker = "";
        String topic = "";
        String clientId = "MQTT_SUB_Producer";
//        String extra = selectsysconfigone.getExtra();
//        JSONObject jsonObject = JSONUtil.parseObj(extra);
//        String shebeilist = jsonObject.getStr("shebeilist");
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到MQTT开关定时任务的配置信息" + DateUtils.now()));
            return;
        }


        // 查询需要推送的设备
        String extra = selectsysconfigone.getExtra();
        String[] shebeis = extra.split(",");

        for(String s :shebeis){
        QueryWrapper<DeviceMixpileRwdMqtt> mqtt = new QueryWrapper<>();
        mqtt.eq("shebeino", s);
        DeviceMixpileRwdMqtt rwdMqtt = deviceMixpileRwdMqttService.getOne(mqtt);
        if (rwdMqtt != null) {
            // 获取下发配置
            broker = rwdMqtt.getQbroker();
            topic = rwdMqtt.getTopicAll();
            clientId = rwdMqtt.getClientid();
            String sbbh = rwdMqtt.getOrgcode();


//            // 查询需要推送的实时数据
//            List<DeviceMixpileRealdata> realdata = deviceMixpileRealdataService.queryones(s);
//            // 转换类型进行推送,映射对应设备编号
//            String realdata16 =   topx(realdata,clientId);
//           // System.out.println(realdata16);
//            MqttMessage mqttMessage = new MqttMessage(hexStringToByteArray(realdata16));
//            try {
//                MqttClient subClient = getMqttClient(broker, clientId);
//              if (subClient != null) {
//                  // 数据推送
//                subClient.publish(topic, mqttMessage);
//                subClient.disconnect();
//                // 并进行记录
//                DeviceMixpileRwdLog deviceMixpileRwdLog1 = new DeviceMixpileRwdLog();
//                deviceMixpileRwdLog1.setQbroker(broker);
//                deviceMixpileRwdLog1.setTopicAll(topic);
//                deviceMixpileRwdLog1.setPushjson(JSONUtil.toJsonStr(realdata16));
//                deviceMixpileRwdLog1.setShebeino(s+"-cjreal");
//                deviceMixpileRwdLog1.setIssuedtime(new Date());
//                deviceMixpileRwdLogService.save(deviceMixpileRwdLog1);
//               }
//            } catch (MqttException e) {
//                throw new RuntimeException(e);
//            }
            try {
                // 查询需要推送的成桩数据【以及段数剧】
                DeviceMixpileHistorydataOne pageList  = listCX(s,selectsysconfigone.getCurid());
                MqttClient subClient1 = getMqttClient(broker, clientId);
                //转换类型
                int his1 =  topx2Histroy(pageList,subClient1,rwdMqtt.getTopic1(),rwdMqtt.getQbroker(),clientId);
                DeviceMixpileHistorydataOne up = new DeviceMixpileHistorydataOne();
                if(his1 == 0){
                    up.setId(pageList.getId());up.setTaicangpush(40);
                }else{
                    up.setId(pageList.getId());up.setTaicangpush(1);
                }
                deviceMixpileHistorydataOneService.updateById(up);
                sysConfigService.updateSysConfig(JobUtil.YJQS_MQTT_SNJBZ,pageList.getId() );//根据传过来的唯一值来修改当前判断到的数据id
                subClient1.disconnect();
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }catch (ParseException e) {
                throw new RuntimeException(e);
            }

         }
        }
    }

    private static MqttClient getMqttClient(String broker, String clientId) {
        try {
            MqttClient pubClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(false);
            connectOptions.setUserName("civillan");
            connectOptions.setPassword("b2v89RpP".toCharArray());
            //System.out.println("Connecting to broker: " + broker);
            pubClient.connect(connectOptions);
            return pubClient;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String topx(List<DeviceMixpileRealdata> realdata,String sbbh){
        String topx ="";
        //MX02707012341234
        String[] data1 = {"2.03",String.valueOf(new Date().getTime()).substring(0,10),sbbh,sbbh,sbbh,String.valueOf(new Date().getTime()).substring(0,10) };
        // 公共部分
        topx =convertF(len1R,sTypeR1,data1);
        for(DeviceMixpileRealdata re : realdata){
            String[] data2 = {
                    re.getPileno(), // 桩号
                    re.getMileage()+"_"+ re.getPileno(), // 桩号描述
                    "0", // 工艺类型
                    re.getLgdfloat(), // 桩孔实时经度
                    re.getLtdfloat(), // 桩孔实时纬度
                    String.valueOf(re.getWorksta()!= "0"?1:2), // 记录状态
                    String.valueOf( re.getWorksta()=="1" && re.getGzcount() == 1?1:re.getWorksta()=="2" && re.getGzcount() == 2?4:re.getWorksta()=="1" && re.getGzcount() == 2?1:3), // 钻杆状态
                     String.valueOf(re.getWorksta()!= "0"?1:0), // 喷浆状态
                    "0", // 扩大头状态
                    re.getRatio(), // 水灰比
                    re.getCurdep(), // 实时深度
                    re.getDesigndep(), // 最大深度
                    re.getSpeed(), // 实时速度
                    re.getX(), // 左右倾角
                    re.getY(), // 前后倾角
                    re.getFlowlst(), // 实时流量
                    "1.79", // 实时密度
                    re.getElec(), // 实时电流
                    re.getPress(), // 实时压力
                    "0.0", // 段浆量
                    "0.0", // 段灰量
                    re.getFlowmeter(), // 当前累计浆量
                    re.getGrouting() // 当前累计灰量
            };
            topx = topx+ convertF(len2R,sType2,data2);
        }

        return topx;
    }

    private int topx2Histroy(DeviceMixpileHistorydataOne his,MqttClient subClient1,String topic,String broker,String sbbh ) throws MqttException {
        String topx ="";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

         Boolean partsize = his.getDeviceMixpileHistorydataPartsList().size()>0?true:false;
        DeviceMixpileHistorydataPart  part1 = new DeviceMixpileHistorydataPart();
         if(partsize){ //端数据大于0
               part1 = his.getDeviceMixpileHistorydataPartsList().get(0);
         }
           //累计浆量
           Double jl =Double.parseDouble(his.getPileCement()) *(1+ (Double.parseDouble(his.getPileRatio()) / 100))/ Double.parseDouble(his.getPileDensity());

            String[] data2 = new String[0];
            try {
                data2 = new String[]{
                        "1", //总包数
                        "1", //当前包
                        his.getPileNo(), //桩号
                        "2.03", //版本号
                        sbbh, //设备号
                        sbbh, //接入编号
                        sbbh, //桩机号
                        his.getPileNo()+"_"+ his.getPileMileage(), //桩号描述
                        String.valueOf(formatter.parse(his.getPileTime()).getTime()- Integer.valueOf(his.getPileWorktime())*1000).substring(0,10), // 8起始时间
                        String.valueOf( formatter.parse(his.getPileTime()).getTime()).substring(0,10) , // 8结束时间
                        "", // 8工艺类型
                        his.getPileLtd(), //设备经度坐标
                        his.getPileLgd(), //设备纬度坐标
                        his.getPileLgd(), //桩孔经度
                        his.getPileLtd(), //桩孔纬度
                        his.getPileRealdep(), //桩长
                        his.getDeviceMixpileHistorydataOneList().get(0).getPileRealdep(), //初搅深度
                        his.getDeviceMixpileHistorydataOneList().size()>1?his.getDeviceMixpileHistorydataOneList().get(1).getPileRealdep():"0", //复搅深度
                        "", //空搅深度
                        his.getPileRatio(), //水灰比
                        String.valueOf(Double.parseDouble(his.getPileDspeed())*100), //平均下钻速度
                        String.valueOf(Double.parseDouble(his.getPileUspeed())*100), //平均提钻速度
                        String.valueOf(jl) , //累计浆量
                        String.valueOf(jl/Double.parseDouble(his.getPileRealdep())), //平均浆量
                        his.getPileCement(), //累计灰量
                        String.valueOf(Double.parseDouble(his.getPileCement()) /Double.parseDouble(his.getPileRealdep())), //平均灰量
                        his.getPileDesigndep(), //设计桩长
                        his.getPileCement(), //设计灰量
                        his.getPileDpress(), //平均压力
                        "", //钻速预警
                        "", //提速预警
                        his.getPileX(), //左右倾角
                        his.getPileY(), //前后倾角
                        "", //扩大头桩长
                        "", //扩大头浆量
                        "", //下部桩浆量
                        "", //扩大桩灰量
                        "", //下部桩灰量
                        his.getDeviceMixpileHistorydataPartsList() != null ? String.valueOf(his.getDeviceMixpileHistorydataPartsList().size()) :"0", //段总数
                        String.valueOf(1), // 段序号
                        partsize==true?part1.getPartTime():"0", // 段时长
                        partsize==true?String.valueOf((int)(Double.parseDouble(part1.getPartDep())*100)):"0",// 段深度
                        partsize==true?part1.getPartBeton():"0",// 段浆量
                        partsize==true?part1.getPartElectricity():"0",// 段电流
                        partsize==true?part1.getPartPress():"0",// 段喷压
                        partsize==true?part1.getPileDensity():"0",// 段密度
                        partsize==true?part1.getPartState() == "1" ? "+" +Double.parseDouble( part1.getPartSpeed())*100 : "-" + Double.parseDouble( part1.getPartSpeed())*100:"0",// 段钻/提速
                        partsize==true?String.valueOf(Double.parseDouble(part1.getPartBeton()) * Double.parseDouble(part1.getPileDensity()) / (1 + Double.parseDouble(his.getPileRatio()) / 100)):"0"// 段灰量



                };
            } catch (ParseException e) {

                System.out.println(e) ;
                return  0;
            }

        // 总包数  1. 304+13*52 2.19*52

        int all = (int) Math.ceil( his.getDeviceMixpileHistorydataPartsList().size()<=20?1:(Double.parseDouble(String.valueOf(his.getDeviceMixpileHistorydataPartsList().size())) - 20) / 27+1);
        data2[0] = String.valueOf(all);
        topx = topx+ convertF(len2H,typeH,data2);

        int i = 1;
       if(partsize){
        for( int j = 1;j<his.getDeviceMixpileHistorydataPartsList().size();j++ ){
            DeviceMixpileHistorydataPart  part = his.getDeviceMixpileHistorydataPartsList().get(j);
            String[] data3 = new String[0];
            data3= new String[]{
//                    String.valueOf(all),    // 总包数
//                    String.valueOf(i),  // 当前包
//                    part.getPileNo(),  // 桩号
                    String.valueOf(j+1), // 段序号
                    part.getPartTime(), // 段时长
                    String.valueOf((int)(Double.parseDouble(part.getPartDep())*100)),// 段深度
                    part.getPartBeton(),// 段浆量
                    part.getPartElectricity(),// 段电流
                    part.getPartPress(),// 段喷压
                    part.getPileDensity(),// 段密度
                    part.getPartState() .equals("1") ? "+" + Double.parseDouble( part.getPartSpeed())*100 : "-" + Double.parseDouble( part.getPartSpeed())*100,// 段钻/提速
                    String.valueOf(Double.parseDouble(part.getPartBeton()) * Double.parseDouble(part.getPileDensity()) / (1 + Double.parseDouble(his.getPileRatio()) / 100))// 段灰量
            };
            topx = topx+ convertF(len2P,typeP,data3);
            if( topx.length() == 1024*2 || j == his.getDeviceMixpileHistorydataPartsList().size() -1 ){
                // 进行分包推送
                MqttMessage mqttMessagehis = new MqttMessage(hexStringToByteArray(topx));
                if (subClient1 != null) {
                    // 数据推送
                    subClient1.publish(topic, mqttMessagehis);
                    // 并进行记录
                    DeviceMixpileRwdLog deviceMixpileRwdLog1 = new DeviceMixpileRwdLog();
                    deviceMixpileRwdLog1.setQbroker(broker);
                    deviceMixpileRwdLog1.setTopicAll(topic);
                    deviceMixpileRwdLog1.setPushjson(JSONUtil.toJsonStr(topx));
                    deviceMixpileRwdLog1.setShebeino(his.getShebeino()+"-cjhis"+i);
                    deviceMixpileRwdLog1.setIssuedtime(new Date());
                    deviceMixpileRwdLogService.save(deviceMixpileRwdLog1);
                    i = i+1;
                    topx ="";
                    String[]  data4= new String[]{
                            String.valueOf(all),    // 总包数
                            String.valueOf(i),  // 当前包
                            part.getPileNo()};// 桩号
                    topx = topx+ convertF(len2P1,typeP1,data4);

                    }

            }

         }

        //   subClient1.disconnect();
           return all;
       }else{
        //   subClient1.disconnect();
           return 0;
       }

    }

    // 转换后进行拼接
    public static String convertF( int[] len1, String type[],Object[] data1) {
        Object[] a1 = new Object[100];
        String covertStr ="";
        for(int i = 0;i<len1.length;i++){
            if(StringUtils.isNotBlank((CharSequence) data1[i]) && type[i].contains("long") ){
                covertStr = covertStr+long2Hex(Long.parseLong(((String) data1[i]).replaceAll("[a-zA-Z]","").trim()) ,len1[i]);
                a1[i] = Long.parseLong(((String) data1[i]).replaceAll("[a-zA-Z]","").trim());
            }else if( StringUtils.isNotBlank((CharSequence) data1[i]) &&  type[i].contains("double")){
                covertStr = covertStr+double2Hex(Double.parseDouble((String) data1[i]) ,len1[i]);
                a1[i] = Double.parseDouble((String) data1[i]);
            }else if(StringUtils.isNotBlank((CharSequence) data1[i]) &&  type[i].contains("float")){
                covertStr = covertStr+ float2Hex(Float.valueOf((String) data1[i]),len1[i]) ;
                a1[i] = Float.parseFloat((String) data1[i]);
            }else{
                  covertStr = covertStr+stringToHex((String) data1[i],len1[i]);
                a1[i] = data1[i];

            }

        }
        return  covertStr;
    }

    // 转换后进行拼接
    public static String convert( int[] len1, String[] data1) {
        String covertStr ="";
        for(int i = 0;i<len1.length;i++){
            covertStr = covertStr+stringToHex(data1[i],len1[i]);
        }
       return  covertStr;
    }

    // 把16进制转字符串
    public static String hexToStr(String hex) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hex.length() - 1; i += 2) {
            int decimal = Integer.parseInt(hex.substring(i, i + 2), 16);
            // 如果需要处理Unicode编码（UTF-8）则取消注释下面这行代码
            // char c = (char) decimal;
            sb.append((char) decimal);
        }

        return sb.toString();
    }
    // 将字符串转为16进制
    public static String stringToHex(String str, Integer len) {

        String plstr ="" ;
        if( StringUtils.isBlank(str)){
            str = "";
        }
        if(str.length()>len){
            str = str.substring(0,len);
        }else{
          int pl =  len- str.length();
            for (int i = 0; i < pl; i++) {
                plstr = plstr+"00"; // 在每次循环中追加原始字符串到StringBuilder对象上
            }
        }
        byte[] byteArray = str.getBytes();
        BigInteger bigInt = new BigInteger(1, byteArray);
        String hexString = bigInt.toString(16).toUpperCase();
        if(hexString.equals("0")){
            hexString="";
        }
        System.out.println(str+"转换后的十六进制字符串：" + hexString+plstr);
        return   hexString+plstr;
    }

    /**
     * 将 long 值转换为指定长度的十六进制字符串
     */
    private static String long2Hex(long value, Integer length) {
        String hex = Long.toHexString(value);
        if(hex.length()< length*2){
           int i0 = length*2 - hex.length();
           for (int i=0;i<i0;i++) hex = "0"+hex;
        }
        System.out.println(value+"转换后的十六进制字符串：" + hex);
        return hex; // 返回最后 length 位的子字符串作为结果
    }
    private static String float2Hex(float value, Integer length) {
        String hex = Integer.toHexString(Float.floatToIntBits(value));;
        if(hex.length()< length*2){
            int i0 = length*2 - hex.length();
            for (int i=0;i<i0;i++) hex = "0"+hex;
        }

        System.out.println(value+"转换后的十六进制字符串：" + hex);
        return hex; // 返回最后 length 位的子字符串作为结果
    }

    /**
     * 将 double 值转换为指定长度的十六进制字符串
     */
    private static String double2Hex(Double value, int length) {
        // 使用String.format()将double转换为16进制字符串
        String hexValue = String.format("%0" + length + "X", Double.doubleToRawLongBits(value));
        System.out.println(value+"转换后的十六进制字符串：" + hexValue);
        return hexValue;
    }

    public static String formatString(String input) {
        StringBuilder formatted = new StringBuilder();
        int length = 0;
        for (char c : input.toCharArray()) {
            formatted.append(c);
            length++;
            // 每4个字符后添加一个空格
            if (length % 4 == 0 && length % 32 != 0 ) {
                formatted.append(' ');
            }
            // 每32个字符后添加一个换行符
            if (length % 32 == 0) {
                formatted.append('\n');
            }
        }
        return formatted.toString();
    }
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    public DeviceMixpileHistorydataOne listCX(String  sbbh,int curid ) throws ParseException {

        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeino", sbbh);
        queryWrapper.eq("gzcount",1);
        queryWrapper.eq("taicangpush",0); //甬台温市站推送
//        queryWrapper.gt("id",curid);
        queryWrapper.groupBy("pile_mileage,pile_no");
        queryWrapper.last(" limit 1");
        DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 = deviceMixpileHistorydataOneService.getOne(queryWrapper);
        // 查询初复搅数据
            List<DeviceMixpileHistorydataOnePage> dolist = deviceMixpileHistorydataOneService.selectJbList(deviceMixpileHistorydataOne1.getShebeino(), deviceMixpileHistorydataOne1.getPileMileage(), deviceMixpileHistorydataOne1.getPileNo());
            for (DeviceMixpileHistorydataOnePage record : dolist) {
                record.setGzcount(2);   //第一条是初搅，其余都是复搅
                if (Double.parseDouble(record.getPileDensity()) > 1000) {
                    record.setPileDensity(String.format("%.3f", Double.parseDouble(record.getPileDensity()) / 1000));//水泥浆比重
                }
                double mkg = 0.0;
                Double pileCement = Double.parseDouble(record.getPileCement());
                Double pileRatio = Double.parseDouble(record.getPileRatio()) / 100;
                Double pileDensity = Double.parseDouble(record.getPileDensity());
                Double pileRealdep = Double.parseDouble(record.getPileRealdep());
                if (pileRealdep > 0) {

                    if (pileDensity > 3) {
                        mkg = pileCement / (pileRealdep - 0.25);
                    } else {
                        mkg = pileCement / (pileRealdep - 0.25);
                    }
                }
                record.setPileMinelec(String.format("%.3f", mkg));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (!org.springframework.util.StringUtils.isEmpty(record.getPileTime())) {
                    Date end = dateFormat.parse(record.getPileTime());
                    if (!org.springframework.util.StringUtils.isEmpty(record.getPileDowntime()) && !org.springframework.util.StringUtils.isEmpty(record.getPileUptime())) {
                        int time = Integer.parseInt(record.getPileDowntime()) + Integer.parseInt(record.getPileUptime());
                        Long starttimes = end.getTime() - time * 1000;
                        record.setPileStarttime(dateFormat.format(starttimes));
                    }
                }

            }
            DeviceMixpileHistorydataOnePage deviceMixpileHistorydataOnePage = dolist.get(0);
            deviceMixpileHistorydataOnePage.setGzcount(1);   //第一条是初搅，其余都是复搅
            deviceMixpileHistorydataOne1.setDeviceMixpileHistorydataOneList(dolist);
            // 查询段数局
        QueryWrapper<DeviceMixpileHistorydataPart> queryWrapperPart = new QueryWrapper<>();
        queryWrapperPart.eq("shebeino", deviceMixpileHistorydataOne1.getShebeino());
        queryWrapperPart.eq("pile_no", deviceMixpileHistorydataOne1.getPileNo());
        queryWrapperPart.eq("pile_mileage", deviceMixpileHistorydataOne1.getPileMileage());
        queryWrapperPart.last(" limit 200");
        List<DeviceMixpileHistorydataPart> list = deviceMixpileHistorydataPartService.list(queryWrapperPart);
        deviceMixpileHistorydataOne1.setDeviceMixpileHistorydataPartsList(list);

        return deviceMixpileHistorydataOne1;
    }
}
