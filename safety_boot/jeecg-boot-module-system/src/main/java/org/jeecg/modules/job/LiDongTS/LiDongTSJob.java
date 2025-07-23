package org.jeecg.modules.job.LiDongTS;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicecranerealdata.service.IDeviceCraneRealdataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LiDongTSJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceCraneRealdataService deviceCraneRealdataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LIDONG_TS);//黎东高速项目推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到黎东高速项目推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输黎东高速项目推送的设备" + DateUtils.now()));
            return;
        }
        String orgCodeList = jsonObject.getStr("orgCodeList");
        String[] split = orgCodeList.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<String> shebeiList = new ArrayList<>();
        for (String s : strsToList1) {
            QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("sys_org_code", s);
            queryWrapper.eq("sbtype", 21);
            List<ShebeiInfo> shebei = shebeiInfoService.list(queryWrapper);
            if (shebei != null || shebeiList.size() != 0) {
                for (ShebeiInfo shebeiInfo : shebei) {
                    shebeiList.add(shebeiInfo.getSbjno());
                }

            }
        }
        List<DeviceCraneRealdata> realdataList = deviceCraneRealdataService.selectListBySB(shebeiList);
        if (null == realdataList || realdataList.size() == 0) {
            log.info(String.format("暂无黎东高速项目推送的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceCraneRealdata deviceCraneRealdata : realdataList) {
            JSONObject sendObject = new JSONObject();
            id=deviceCraneRealdata.getId();
            Double mainHookload = deviceCraneRealdata.getMainHookload();
            Double reservedVicehookheight = deviceCraneRealdata.getReservedVicehookheight();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sendObject.set("ts", simpleDateFormat.format(deviceCraneRealdata.getCranedate()));//时间（2020-07-22 12:12:12.100）
            sendObject.set("wind_speed", deviceCraneRealdata.getWindSpeed());//风速（m/s)
            if(deviceCraneRealdata.getWindAntiskidstatus()!=null){
                if(deviceCraneRealdata.getWindAntiskidstatus()==0){
                    sendObject.set("wind_status", "1");//抗风防滑状态（1：防护，2：未防护）
                }else if(deviceCraneRealdata.getWindAntiskidstatus()==1){
                    sendObject.set("wind_status", "2");//抗风防滑状态（1：防护，2：未防护）
                }
            }
            sendObject.set("position", deviceCraneRealdata.getDoorLimit());//门限位（1：打开，2：关闭）
            sendObject.set("crane_travel", mathabs(deviceCraneRealdata.getBigCarroute()));//大车行程（m）
            sendObject.set("crane_deviation", deviceCraneRealdata.getBigCarlegdeviation());//大车行程偏差（m）
            sendObject.set("crane_speed", 0);//大车速度（m/min）
            if(mainHookload <0){
                mainHookload =0.0;
            }
            sendObject.set("trolley_weight1", mainHookload);//小车1吊重（t）
            sendObject.set("trolley_height1", mathabs(deviceCraneRealdata.getMainHookheight()));//小车1吊钩提升高度（m）
            sendObject.set("trolley_speed1", 0);//小车1吊钩提升速度（m/min）
            sendObject.set("trolley_travel1", mathabs(deviceCraneRealdata.getSmallCarroute()));//小车1行程（m
            if(reservedVicehookheight <0){
                reservedVicehookheight =0.0;
            }
            sendObject.set("trolley_weight2", reservedVicehookheight);//小车2吊重（t）
            sendObject.set("trolley_height2", 0);//小车2吊钩提升高度（m）
            sendObject.set("trolley_speed2", 0);//小车2吊钩提升速度（m/min）
            sendObject.set("trolley_travel2", mathabs(deviceCraneRealdata.getReservedSmallcarroute()));//小车2行程（m） false
            sendObject.set("trolley_distance", 0);//小车1、2距离（m）
            sendObject.set("warning", deviceCraneRealdata.getWindSpeedalarm());//风速预警（0：正常，1：异常）
            sendObject.set("crane_left", deviceCraneRealdata.getBigCarleftlimit());//大车左限位（0：正常，1：异常） f
            sendObject.set("crane_right", deviceCraneRealdata.getBigCarrightlimit());//大车右限位（0：正常，1：异常
            sendObject.set("deviation", 0);//大车行程偏差（0：正常，1：异常）
            sendObject.set("weight1", deviceCraneRealdata.getMainHookstatus());//小车1吊重（0：正常，1：异常）
            sendObject.set("front1", deviceCraneRealdata.getSmallCarfrontlimit());//小车1前限位（0：正常，1：异常）
            sendObject.set("behind1", deviceCraneRealdata.getSmallCarbacklimit());//小车1后限位（0：正常，1：异常）
            sendObject.set("weight2", deviceCraneRealdata.getReservedVicehookstatus());//小车2吊重（0：正常，1：异常）
            sendObject.set("front2", deviceCraneRealdata.getReservedSmallcarfrontlimit());//小车2前限位（0：正常，1：异常）
            sendObject.set("behind2", deviceCraneRealdata.getReservedSmallcarbacklimit());//小车2后限位（0：正常，1：异常）
            sendObject.set("brake", 0);//制动器状态（0：正常，1：异常）
            sendObject.set("cable_drum", deviceCraneRealdata.getWireDrumstatus());//电缆卷筒状态（0：正常，1：异常）
            sendObject.set("over_speed", 0);//超速保护状态（0：正常，1：异常）
            System.out.println(sendObject);
            String back1 = HttpRequest.post("https://iot.e-jt.cn/broker/device/v1/" + deviceCraneRealdata.getDeviceCode() + "/attributes/report")
                    .header("username", deviceCraneRealdata.getDeviceCode())
                    .header("password", deviceCraneRealdata.getDeviceCode())
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back1);
            Integer code1 = (Integer) jsonObject1.get("code");
            if (code1 == 0){
                log.info(String.format("黎东高速项目推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.LIDONG_TS, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else{
                log.info(String.format("黎东高速项目推送失败!" + id));
            }
        }

    }
    public static double mathabs(double x) {
        return x < 0 ? -x : x;
    }
}
