package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.devicepipepilehistorydatapart.entity.DevicePipepileHistorydataPart;
import com.trtm.iot.devicepipepilehistorydatapart.service.IDevicePipepileHistorydataPartService;
import com.trtm.iot.devicepipepilereport.entity.DevicePipepileReport;
import com.trtm.iot.devicepipepilereport.service.IDevicePipepileReportService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCgzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicePipepileHistorydataOneService devicePipepileHistorydataOneService;
    @Autowired
    private IDevicePipepileHistorydataPartService devicePipepileHistorydataPartService;
    @Autowired
    private IDevicePipepileReportService devicePipepileReportService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCGZTS);//瑞仓管桩数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓管桩数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓管桩数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DevicePipepileHistorydataOne> list = devicePipepileHistorydataOneService.selectLists(shebeilist, curid);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓管桩未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DevicePipepileHistorydataOne devicePipepileHistorydataOne : list) {
            id = devicePipepileHistorydataOne.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("shebeino", devicePipepileHistorydataOne.getShebeino());
            jsonObject1.set("pile_no", devicePipepileHistorydataOne.getPileNo());
            jsonObject1.set("pile_realdep", devicePipepileHistorydataOne.getPileRealdep());
            jsonObject1.set("pile_worktime", devicePipepileHistorydataOne.getPileWorktime());
            jsonObject1.set("pile_y", devicePipepileHistorydataOne.getPileY());
            jsonObject1.set("pile_time", devicePipepileHistorydataOne.getPileTime());
            jsonObject1.set("pile_starttime", devicePipepileHistorydataOne.getPileStarttime());
            jsonObject1.set("pile_upress", devicePipepileHistorydataOne.getPileUpress());
            jsonObject1.set("pile_dpress", devicePipepileHistorydataOne.getPileDpress());
            jsonObject1.set("pile_speed", devicePipepileHistorydataOne.getPileSpeed());
            jsonObject1.set("times", devicePipepileHistorydataOne.getTimes());
            jsonObject1.set("pile_designdep", devicePipepileHistorydataOne.getPileDesigndep());
            jsonObject1.set("rjrwd", devicePipepileHistorydataOne.getRjrwd());
            jsonObject1.set("uuid", devicePipepileHistorydataOne.getUuid());
            jsonObject1.set("pile_mileage", devicePipepileHistorydataOne.getPileMileage());
            jsonObject1.set("datatime", devicePipepileHistorydataOne.getDatatime());
            jsonObject1.set("ts", devicePipepileHistorydataOne.getTs());
            jsonObject1.set("pile_lgd", devicePipepileHistorydataOne.getPileLgd());
            jsonObject1.set("pile_ltd", devicePipepileHistorydataOne.getPileLtd());
            jsonObject1.set("istongji", devicePipepileHistorydataOne.getIstongji());
            jsonObject1.set("chaobiaodengji", devicePipepileHistorydataOne.getChaobiaodengji());
            jsonObject1.set("address", devicePipepileHistorydataOne.getAddress());
            List<DevicePipepileHistorydataPart> partList = devicePipepileHistorydataPartService.selectListwcx(devicePipepileHistorydataOne.getShebeino(), devicePipepileHistorydataOne.getPileNo());
            JSONArray jsonArray = new JSONArray();
            if(null != partList || partList.size()>0) {
                for (DevicePipepileHistorydataPart part : partList) {
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.set("shebeino", part.getShebeino());
                    jsonObject2.set("pile_no", part.getPileNo());
                    jsonObject2.set("part_pilec", part.getPartPilec());
                    jsonObject2.set("part_dep", part.getPartDep());
                    jsonObject2.set("part_speed", part.getPartSpeed());
                    jsonObject2.set("part_y", part.getPartY());
                    jsonObject2.set("part_upress", part.getPartUpress());
                    jsonObject2.set("part_dpress", part.getPartDpress());
                    jsonObject2.set("part_state", part.getPartState());
                    jsonObject2.set("part_endtime", part.getPartEndtime());
                    jsonObject2.set("datatime", part.getDatatime());
                    jsonObject2.set("part_ts", part.getPartTs());
                    jsonObject2.set("uuid", part.getUuid());
                    jsonArray.add(jsonObject2);
                }
            }
            jsonObject1.set("part", jsonArray);
            DevicePipepileReport report = devicePipepileReportService.selectone(devicePipepileHistorydataOne.getShebeino(), devicePipepileHistorydataOne.getPileNo());
            if(null != report){
                JSONObject jsonObject3 = new JSONObject();
                jsonObject3.set("shebeino", report.getShebeino());
                jsonObject3.set("pileno", report.getPileno());
                jsonObject3.set("part_pilec", report.getPartPilec());
                jsonObject3.set("pile_worktime", report.getPileWorktime());
                jsonObject3.set("piletime", report.getPiletime());
                jsonObject3.set("speed", report.getSpeed());
                jsonObject3.set("pile_y", report.getPileY());
                jsonObject3.set("pile_upress", report.getPileUpress());
                jsonObject3.set("pile_dpress", report.getPileDpress());
                jsonObject3.set("pile_dep", report.getPileDep());
                jsonObject3.set("datatime", report.getDatatime());
                jsonObject3.set("ts", report.getTs());
                jsonObject1.set("report", jsonObject3);
            }

            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/gzsj/pipepileHistorydataOnes/PipepileHistorydataOneUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓管桩数据推送成功!" ));
                sysConfigService.updateSysConfig(JobUtil.RCGZTS, id);//根据传过来的唯一值来修改当前判断到的数据id

            }else if(code == 402){
                log.info(String.format("瑞仓管桩数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCGZTS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓管桩数据推送失败!" ));
            }
        }

    }
}
