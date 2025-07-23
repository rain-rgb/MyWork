package org.jeecg.modules.job.LHgsJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonArray;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * \* @author: Xx
 * \* Date: 2022/5/19
 * \* Time: 9:10
 * \* Description:黎霍高速 龙门吊数据上传
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LongMenDiaoJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LH_LONGMENDIAO);//黎霍高速龙门吊
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到黎霍高速龙门吊定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String curdate = selectsysconfigone.getCurdate();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输黎霍高速龙门吊的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1= Arrays.asList(split);
        List<DeviceCraneHistorydata> deviceCraneHistorydata = deviceCraneHistorydataService.selectListsHistoryList(strsToList1, curid, 1);
        if (null == deviceCraneHistorydata || deviceCraneHistorydata.size() == 0) {
            log.info(String.format("暂无黎霍高速龙门吊的数据" + DateUtils.now()));
            return;
        }
        int id=0;

        for (DeviceCraneHistorydata deviceCraneHistorydatum : deviceCraneHistorydata) {
            JSONArray jsonArray=new JSONArray();
            JSONObject saveDTOList=new JSONObject();
            id= deviceCraneHistorydatum.getId();
            String deviceCode = deviceCraneHistorydatum.getDeviceCode();//设备编号
            Double bigCarroute = deviceCraneHistorydatum.getBigCarroute();//大车行程
            Date cranedate = deviceCraneHistorydatum.getCranedate();//数据上传时间
            Double mainHookheight = deviceCraneHistorydatum.getMainHookheight();//主钩高度
            Double reservedVicehookheight = deviceCraneHistorydatum.getReservedVicehookheight();//预留副钩高度
            Double mainHookload = deviceCraneHistorydatum.getMainHookload();//主钩吊重
            Double reservedVicehookload = deviceCraneHistorydatum.getReservedVicehookload();//预留副钩吊重
            Double smallCarroute = deviceCraneHistorydatum.getSmallCarroute();//小车行程
            Double reservedSmallcarroute = deviceCraneHistorydatum.getReservedSmallcarroute();//预留小车2行程
            Double windSpeed = deviceCraneHistorydatum.getWindSpeed();//风速
            Double reservedAnalogquantityone = deviceCraneHistorydatum.getReservedAnalogquantityone();//预留模拟量
            Double reservedAnalogquantitytwo = deviceCraneHistorydatum.getReservedAnalogquantitytwo();//预留模拟量
            Integer mainHookstatus = deviceCraneHistorydatum.getMainHookstatus();//主钩载荷状态
            Integer reservedVicehookstatus = deviceCraneHistorydatum.getReservedVicehookstatus();//预留副钩载荷状态
            Integer windSpeedalarm = deviceCraneHistorydatum.getWindSpeedalarm();//风速报警
            Integer bigCarleftlimit = deviceCraneHistorydatum.getBigCarleftlimit();//大车左限位
            Integer bigCarrightlimit = deviceCraneHistorydatum.getBigCarrightlimit();//大车右限位
            Integer smallCarfrontlimit = deviceCraneHistorydatum.getSmallCarfrontlimit();//小车前限位
            Integer smallCarbacklimit = deviceCraneHistorydatum.getSmallCarbacklimit();//小车后限位
            Integer reservedSmallcarfrontlimit = deviceCraneHistorydatum.getReservedSmallcarfrontlimit();//预留小车2前限位
            Integer reservedSmallcarbacklimit = deviceCraneHistorydatum.getReservedSmallcarbacklimit();//预留小车2后限位
            Integer hookUplimit = deviceCraneHistorydatum.getHookUplimit();//吊钩上限位
            Integer reservedVicehookuplimit = deviceCraneHistorydatum.getReservedVicehookuplimit();//预留副吊钩上限位
            Integer doorLimit = deviceCraneHistorydatum.getDoorLimit();//门限位
            Integer windAntiskidstatus = deviceCraneHistorydatum.getWindAntiskidstatus();//抗风防滑状态
            Integer wireDrumstatus = deviceCraneHistorydatum.getWireDrumstatus();//电缆卷筒状态
            Integer smallCar1brake1 = deviceCraneHistorydatum.getSmallCar1brake1();
            Integer smallCar1brake2 = deviceCraneHistorydatum.getSmallCar1brake2();
            Integer smallCar2brake1 = deviceCraneHistorydatum.getSmallCar2brake1();
            Integer smallCar2brake2 = deviceCraneHistorydatum.getSmallCar2brake2();
            Double bigCarlegdeviation = deviceCraneHistorydatum.getBigCarlegdeviation();
            Double allTime = deviceCraneHistorydatum.getAllTime();
            Integer allTimes = deviceCraneHistorydatum.getAllTimes();
            Integer bigcarAlm = deviceCraneHistorydatum.getBigcarAlm();
            Integer reservedOne = deviceCraneHistorydatum.getReservedOne();
            Integer reservedTwo = deviceCraneHistorydatum.getReservedTwo();
            String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceCode);
            saveDTOList.set("id",uuid);
            if(reservedSmallcarroute!=null){
                saveDTOList.set("reserved_smallcarroute",reservedSmallcarroute);
            }else{
                saveDTOList.set("reserved_smallcarroute","");
            }
            if(smallCarfrontlimit!=null){
                saveDTOList.set("small_carfrontlimit",smallCarfrontlimit);
            }else{
                saveDTOList.set("small_carfrontlimit","");
            }
            if(wireDrumstatus!=null){
                saveDTOList.set("wire_drumstatus",wireDrumstatus);
            }else{
                saveDTOList.set("wire_drumstatus","");
            }
            if(windSpeed!=null){
                saveDTOList.set("wind_speed",windSpeed);
            }else{
                saveDTOList.set("wind_speed","");
            }
            saveDTOList.set("deviceid",deviceCode);
            if(smallCarbacklimit!=null){
                saveDTOList.set("small_carbacklimit",smallCarbacklimit);
            }else{
                saveDTOList.set("small_carbacklimit","");
            }
            if(smallCar1brake1!=null){
                saveDTOList.set("small_car1brake1",smallCar1brake1);
            }else{
                saveDTOList.set("small_car1brake1","");
            }
            if(reservedAnalogquantityone!=null){
                saveDTOList.set("reserved_analogquantityone",reservedAnalogquantityone);
            }else{
                saveDTOList.set("reserved_analogquantityone","");
            }
            saveDTOList.set("devicetime",cranedate);
            if(reservedSmallcarfrontlimit!=null){
                saveDTOList.set("reserved_smallcarfrontlimit",reservedSmallcarfrontlimit);
            }else{
                saveDTOList.set("reserved_smallcarfrontlimit","");
            }
            if(smallCar1brake2!=null){
                saveDTOList.set("small_car1brake2",smallCar1brake2);
            }else{
                saveDTOList.set("small_car1brake2","");
            }
            if(bigCarroute!=null){
                saveDTOList.set("big_carroute",bigCarroute);
            }else{
                saveDTOList.set("big_carroute","");
            }
            if(reservedAnalogquantitytwo!=null){
                saveDTOList.set("reserved_analogquantitytwo",reservedAnalogquantitytwo);
            }else{
                saveDTOList.set("reserved_analogquantitytwo","");
            }
            if(smallCar2brake1!=null){
                saveDTOList.set("small_car2brake1",smallCar2brake1);
            }else{
                saveDTOList.set("small_car2brake1","");
            }
            if(mainHookheight!=null){
                saveDTOList.set("main_hookheight",mainHookheight);
            }else{
                saveDTOList.set("main_hookheight","");
            }
            if(mainHookstatus!=null){
                saveDTOList.set("main_hookstatus",mainHookstatus);
            }else{
                saveDTOList.set("main_hookstatus","");
            }
            if(reservedSmallcarbacklimit!=null){
                saveDTOList.set("reserved_smallcarbacklimit",reservedSmallcarbacklimit);
            }else{
                saveDTOList.set("reserved_smallcarbacklimit","");
            }
            if(smallCar2brake2!=null){
                saveDTOList.set("small_car2brake2",smallCar2brake2);
            }else{
                saveDTOList.set("small_car2brake2","");
            }
            if(reservedVicehookheight!=null){
                saveDTOList.set("reserved_vicehookheight",reservedVicehookheight);
            }else{
                saveDTOList.set("reserved_vicehookheight","");
            }
            if(reservedVicehookstatus!=null){
                saveDTOList.set("reserved_vicehookstatus",reservedVicehookstatus);
            }else{
                saveDTOList.set("reserved_vicehookstatus","");
            }
            if(hookUplimit!=null){
                saveDTOList.set("hook_uplimit",hookUplimit);
            }else{
                saveDTOList.set("hook_uplimit","");
            }
            if(allTime!=null){
                saveDTOList.set("all_time",allTime);
            }else{
                saveDTOList.set("all_time","");
            }
            if(mainHookload!=null){
                saveDTOList.set("main_hookload",mainHookload);
            }else{
                saveDTOList.set("main_hookload","");
            }
            if(windSpeedalarm!=null){
                saveDTOList.set("wind_speedalarm",windSpeedalarm);
            }else{
                saveDTOList.set("wind_speedalarm","");
            }
            if(reservedVicehookuplimit!=null){
                saveDTOList.set("reserved_vicehookuplimit",reservedVicehookuplimit);
            }else{
                saveDTOList.set("reserved_vicehookuplimit","");
            }
            if(allTimes!=null){
                saveDTOList.set("all_times",allTimes);
            }else{
                saveDTOList.set("all_times","");
            }
            if(reservedVicehookload!=null){
                saveDTOList.set("reserved_vicehookload",reservedVicehookload);
            }else{
                saveDTOList.set("reserved_vicehookload","");
            }
            if(bigCarleftlimit!=null){
                saveDTOList.set("big_carleftlimit",bigCarleftlimit);
            }else{
                saveDTOList.set("big_carleftlimit","");
            }
            if(doorLimit!=null){
                saveDTOList.set("door_limit",doorLimit);
            }else{
                saveDTOList.set("door_limit","");
            }
            if(reservedOne!=null){
                saveDTOList.set("reserved_one",reservedOne);
            }else{
                saveDTOList.set("reserved_one","");
            }
            if(smallCarroute!=null){
                saveDTOList.set("small_carroute",smallCarroute);
            }else{
                saveDTOList.set("small_carroute","");
            }
            if(bigCarrightlimit!=null){
                saveDTOList.set("big_carrightlimit",bigCarrightlimit);
            }else{
                saveDTOList.set("big_carrightlimit","");
            }
            if(windAntiskidstatus!=null){
                saveDTOList.set("wind_antiskidstatus",windAntiskidstatus);
            }else{
                saveDTOList.set("wind_antiskidstatus","");
            }
            if(reservedTwo!=null){
                saveDTOList.set("reserved_two",reservedTwo);
            }else{
                saveDTOList.set("reserved_two","");
            }
            saveDTOList.set("devicetype","龙门吊");
            saveDTOList.set("devicename",selectshebeione.getSbname());

            jsonArray.add(saveDTOList);
            String datavalue = Base64.getEncoder().encodeToString(jsonArray.toString().getBytes());

            JSONObject saveDTOList1=new JSONObject();
            saveDTOList1.set("datatype",160);
            saveDTOList1.set("datavalue",datavalue);
            String url = "http://112.16.169.54:11885/jhgdzhgd";
            String body = HttpRequest.post(url)
                    .header("auth",curdate)
                    .body(String.valueOf(saveDTOList1))
                    .execute().body();
            System.out.println(body);

            JSONArray jsonArray1=new JSONArray(body);
            Integer codes = Integer.parseInt(jsonArray1.getJSONObject(0).getStr("code"));
            if(codes==1){
                sysConfigService.updateSysConfig(JobUtil.LH_LONGMENDIAO, id);//根据传过来的唯一值来修改当前判断到的数据id
                log.info(String.format("黎霍高速龙门吊推送成功!" + id+"Json数据"+saveDTOList1));
            }else{
                log.info(String.format("黎霍高速龙门吊推送失败!" + id+"Json数据"+saveDTOList1));
            }
        }
        sysConfigService.updateSysConfig(JobUtil.LH_LONGMENDIAO, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
