package org.jeecg.modules.job.stprojectjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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

import java.util.List;

/**
 * @ClassName STBHZJob：
 * @Description TODO
 * @Author zml
 * @Date 2022/3/25 14:16
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SuTaiLmdTuiSongJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private STUtil stUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SUTAI_LMD_TUISONG);//苏台龙门吊数据推送tim
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台龙门吊定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台龙门吊的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceCraneHistorydata> deviceCraneHistorydata = deviceCraneHistorydataService.selectLmdList(shebeilist, curid);
        if (null == deviceCraneHistorydata || deviceCraneHistorydata.size() == 0) {
            log.info(String.format("暂无苏台龙门吊的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        JSONArray jsonArray = new JSONArray();
        JSONObject saveDTOList = new JSONObject();
        for (DeviceCraneHistorydata deviceCraneHistoryOne : deviceCraneHistorydata) {
            id = deviceCraneHistoryOne.getId();
            String deviceCode = deviceCraneHistoryOne.getDeviceCode();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceCode);
            saveDTOList.set("analogQuantity", String.valueOf(deviceCraneHistoryOne.getReservedAnalogquantityone()));//预留模拟量
            saveDTOList.set("cycleIndex", deviceCraneHistoryOne.getAllTimes());//循环使用次数
            saveDTOList.set("deputyHeight", String.valueOf(deviceCraneHistoryOne.getReservedVicehookheight()));//预留副钩高度
            saveDTOList.set("deputyLoadType", deviceCraneHistoryOne.getReservedVicehookstatus());//预留副钩载荷状态 0:正常,1:预警
            saveDTOList.set("deputyUpperLimitOfHook", String.valueOf(deviceCraneHistoryOne.getReservedVicehookuplimit()));//预留副吊钩上限位
            saveDTOList.set("deputyWeight", String.valueOf(deviceCraneHistoryOne.getReservedVicehookload()));//预留副钩吊重(t)
            saveDTOList.set("deviationAlarm", deviceCraneHistoryOne.getBigcarAlm());//支腿偏差报警 0:正常,1:预警
            saveDTOList.set("doorLimit", String.valueOf(deviceCraneHistoryOne.getDoorLimit()));//门限位
            saveDTOList.set("equipmentName", selectshebeione.getSbname());//设备名称
            saveDTOList.set("equipmentNo", deviceCode);//设备编号
            saveDTOList.set("sectionType",selectshebeione.getProcode());//标段
            saveDTOList.set("mainHeight", String.valueOf(deviceCraneHistoryOne.getMainHookheight()));//主钩高度(m)
            saveDTOList.set("mainLoadType", deviceCraneHistoryOne.getMainHookstatus());//主钩载荷状态 0：正常,1：预警
            saveDTOList.set("mainUpperLimitOfHook", String.valueOf(deviceCraneHistoryOne.getHookUplimit()));//吊钩上限位
            saveDTOList.set("mainWeight", String.valueOf(deviceCraneHistoryOne.getMainHookload()));//主钩吊重(t)
            saveDTOList.set("maxCarDeviation", String.valueOf(deviceCraneHistoryOne.getBigCarlegdeviation()));//大车支腿偏差
            saveDTOList.set("maxDistance", String.valueOf(deviceCraneHistoryOne.getBigCarroute()));//大车行程(m)
            saveDTOList.set("maxLeftLimit", String.valueOf(deviceCraneHistoryOne.getBigCarleftlimit()));//大车左限位
            saveDTOList.set("maxRightLimit", String.valueOf(deviceCraneHistoryOne.getBigCarrightlimit()));//大车右限位
            saveDTOList.set("minBrakingOne", String.valueOf(deviceCraneHistoryOne.getSmallCar1brake1()));//小车1制动器1
            saveDTOList.set("minBrakingTwo", String.valueOf(deviceCraneHistoryOne.getSmallCar1brake2()));//小车1制动器2
            saveDTOList.set("minDistanceOne", String.valueOf(deviceCraneHistoryOne.getSmallCarroute()));//小车行程(m)
            saveDTOList.set("minDistanceTwo", String.valueOf(deviceCraneHistoryOne.getReservedSmallcarroute()));//预留小车2行程(m)
            saveDTOList.set("minDownLimit", String.valueOf(deviceCraneHistoryOne.getSmallCarbacklimit()));//小车后限位
            saveDTOList.set("minDownLimitTwo", String.valueOf(deviceCraneHistoryOne.getReservedSmallcarbacklimit()));//预留小车2后限位
            saveDTOList.set("minTwoBrakingOne", String.valueOf(deviceCraneHistoryOne.getSmallCar2brake1()));//小车2制动器1
            saveDTOList.set("minTwoBrakingTwo", String.valueOf(deviceCraneHistoryOne.getSmallCar2brake2()));//小车2制动器2
            saveDTOList.set("minUpLimit", String.valueOf(deviceCraneHistoryOne.getSmallCarfrontlimit()));//小车前限位
            saveDTOList.set("minUpLimitTwo", String.valueOf(deviceCraneHistoryOne.getReservedSmallcarfrontlimit()));//预留小车2前限位
            saveDTOList.set("nonSlipType", deviceCraneHistoryOne.getWindAntiskidstatus());//抗风防滑状态
            saveDTOList.set("reelType", deviceCraneHistoryOne.getWireDrumstatus());//电缆卷筒状态
            saveDTOList.set("sumTime", (int) Math.ceil(deviceCraneHistoryOne.getAllTime()));//累计时间
            saveDTOList.set("uploadTime", deviceCraneHistoryOne.getCranedate());//数据上传时间
            saveDTOList.set("windSpeed", String.valueOf(deviceCraneHistoryOne.getWindSpeed()));//风速(m/s)
            saveDTOList.set("windSpeedAlarm", deviceCraneHistoryOne.getWindSpeedalarm());//风速是否报警 0:否,1:是
            jsonArray.add(saveDTOList);
        }
        System.out.println(jsonArray);
        if (jsonArray.size() > 0) {
            Integer integer = stUtil.PostSTLMDList(jsonArray);
            if (integer == 200) {
                log.info(String.format("苏台龙门吊数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.SUTAI_LMD_TUISONG, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("苏台龙门吊数据推送失败!" + id));
            }
        } else {
            sysConfigService.updateSysConfig(JobUtil.SUTAI_LMD_TUISONG, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
