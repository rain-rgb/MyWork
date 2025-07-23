package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardRecordService;
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
public class RCmjkqHisJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IEntranceGuardRecordService entranceGuardRecordService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCMJKQ_HIS);//瑞仓门禁考勤历史数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓门禁考勤历史数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓门禁考勤数据历史数据推送的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<EntranceGuardRecord> list = entranceGuardRecordService.selectLists(shebeilist, curid);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓门禁考勤未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (EntranceGuardRecord entranceGuardRecord : list){
            id = entranceGuardRecord.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("RecordID",entranceGuardRecord.getRecordid());
            jsonObject1.set("Types",entranceGuardRecord.getTypes());
            jsonObject1.set("OpenID",entranceGuardRecord.getOpenid());
            jsonObject1.set("OpenTime",entranceGuardRecord.getOpentime());
            jsonObject1.set("DoorID",entranceGuardRecord.getDoorid());
            jsonObject1.set("Info",entranceGuardRecord.getInfo());
            jsonObject1.set("AdminID",entranceGuardRecord.getAdminid());
            jsonObject1.set("Pic",entranceGuardRecord.getPic());
            jsonObject1.set("AddDate",entranceGuardRecord.getAdddate());
            jsonObject1.set("IsOffLine",entranceGuardRecord.getIsoffline());
            jsonObject1.set("CapturePic",entranceGuardRecord.getCapturepic());
            jsonObject1.set("IsOpen",entranceGuardRecord.getIsopen());
            jsonObject1.set("serialNo",entranceGuardRecord.getSerialno());
            jsonObject1.set("IsUpload",entranceGuardRecord.getIsupload());
            jsonObject1.set("UploadDate",entranceGuardRecord.getUploaddate());
            jsonObject1.set("Temperature",entranceGuardRecord.getTemperature());
            jsonObject1.set("Standard",entranceGuardRecord.getStandard());
            jsonObject1.set("CID",entranceGuardRecord.getCid());
            jsonObject1.set("Names",entranceGuardRecord.getNames());
            jsonObject1.set("shebeino",entranceGuardRecord.getShebeino());
            jsonObject1.set("DepartmentID",entranceGuardRecord.getDepartmentid());
            jsonObject1.set("DepartName",entranceGuardRecord.getDepartname());
            jsonObject1.set("status",entranceGuardRecord.getStatus());
//            jsonObject1.set("age",entranceGuardRecord.);
//            jsonObject1.set("runtime",entranceGuardRecord.);
//            jsonObject1.set("longitude",entranceGuardRecord.);
//            jsonObject1.set("latitude",entranceGuardRecord.);
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/anquan/guardRecords/checkingInHistoryUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓门禁考勤历史数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCMJKQ_HIS, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else if(code == 402){
                log.info(String.format("瑞仓门禁考勤历史数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCMJKQ_HIS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓门禁考勤历史数据推送失败!" + id));
            }
        }
    }
}
