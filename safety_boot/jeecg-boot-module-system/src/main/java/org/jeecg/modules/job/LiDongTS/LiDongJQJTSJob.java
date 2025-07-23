package org.jeecg.modules.job.LiDongTS;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeHistorydata;
import com.trtm.iot.jiaqiaoji.service.IDeviceBridgeHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LiDongJQJTSJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceBridgeHistorydataService deviceBridgeHistorydataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LIDONG_JQJ_TUISONG);//黎东高速项目推送
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
            log.info(String.format("没有配置要传输黎东高速项目推送的架桥机设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceBridgeHistorydata> deviceBridgeHistorydataList = deviceBridgeHistorydataService.selectJQJList(shebeilist, curid);
        if (null == deviceBridgeHistorydataList || deviceBridgeHistorydataList.size() == 0) {
            log.info(String.format("暂无黎东高速架桥机的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceBridgeHistorydata deviceBridgeHistorydata : deviceBridgeHistorydataList) {
            id = deviceBridgeHistorydata.getId();
            JSONObject jsonObj = new JSONObject();
            jsonObj.set("ts", deviceBridgeHistorydata.getBridgedate());//数据上传时间
            if (deviceBridgeHistorydata.getWindAntiskidstatus() != null) {
                if (deviceBridgeHistorydata.getWindAntiskidstatus() == 0) {
                    jsonObj.set("wind_status", "1");//抗风防滑状态（1：防护，2：未防护）
                } else {
                    jsonObj.set("wind_status", "2");//抗风防滑状态（1：防护，2：未防护）
                }
            }
            jsonObj.set("position", deviceBridgeHistorydata.getDoorLimit());//门限位（1：打开，2：关闭）
            jsonObj.set("lateral_travel", deviceBridgeHistorydata.getBigTransverseCarroute());//整车横移行程（m）
            jsonObj.set("vertical_travel", deviceBridgeHistorydata.getBigLongitudinalCarroute());//整车纵移行程（m）
            jsonObj.set("lateral_travel1", deviceBridgeHistorydata.getTransverseCarroute1());//小车1横移行程（m）
            jsonObj.set("vertical_travel1", deviceBridgeHistorydata.getLongitudinalCarroute1());//小车1纵移行程（m）
            jsonObj.set("lift_weight1", deviceBridgeHistorydata.getCraneCrane1());//小车1起重重量（t）
            jsonObj.set("lift_height1", deviceBridgeHistorydata.getCraneHeight1());//小车1起升高度（m）
            jsonObj.set("front1", deviceBridgeHistorydata.getCarfrontlimit1());//小车1前限位（0：正常，1：异常）
            jsonObj.set("behind1", deviceBridgeHistorydata.getCarbacklimit1());//小车1后限位（0：正常，1：异常）
            jsonObj.set("left1", deviceBridgeHistorydata.getCarleftlimit1());//小车1左限位（0：正常，1：异常）
            jsonObj.set("right1", deviceBridgeHistorydata.getCarrightlimit1());//小车1右限位（0：正常，1：异常）
            jsonObj.set("lateral_travel2", deviceBridgeHistorydata.getTransverseCarroute2());// 小车2横移行程（m）
            jsonObj.set("vertical_travel2", deviceBridgeHistorydata.getLongitudinalCarroute2());//小车2纵移行程（m）
            jsonObj.set("lift_weight2", deviceBridgeHistorydata.getCraneCrane2());//小车2起重重量（t）
            jsonObj.set("lift_height2", deviceBridgeHistorydata.getCraneHeight2());//小车2起升高度（m）
            jsonObj.set("front2", deviceBridgeHistorydata.getCarfrontlimit2());//小车2前限位（0：正常，1：异常）
            jsonObj.set("behind2", deviceBridgeHistorydata.getCarbacklimit2());//小车2后限位（0：正常，1：异常）
            jsonObj.set("left2", deviceBridgeHistorydata.getCarleftlimit2());//小车2左限位（0：正常，1：异常）
            jsonObj.set("right2", deviceBridgeHistorydata.getCarrightlimit2());//小车2右限位（0：正常，1：异常）
            System.out.println(jsonObj);
            String back1 = HttpRequest.post("https://iot.e-jt.cn/broker/device/v1/" + deviceBridgeHistorydata.getDeviceCode() + "/attributes/report")
                    .header("username", deviceBridgeHistorydata.getDeviceCode())
                    .header("password", deviceBridgeHistorydata.getDeviceCode())
                    .body(String.valueOf(jsonObj))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back1);
            Integer code1 = (Integer) jsonObject1.get("code");
            if (code1 == 0) {
                log.info(String.format("黎东高速项目推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.LIDONG_JQJ_TUISONG, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("黎东高速项目推送失败!" + id));
            }
        }
    }
}
