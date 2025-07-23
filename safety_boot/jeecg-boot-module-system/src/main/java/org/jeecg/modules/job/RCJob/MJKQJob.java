package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardRecordService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName MJKQJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/7/8 21:56
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MJKQJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IEntranceGuardRecordService entranceGuardRecordService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_MJKQ);//瑞仓内网压浆数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓内网门禁考勤数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞门禁考勤设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<EntranceGuardRecord> entranceGuardRecords = entranceGuardRecordService.selectLists(shebeilist, curid);
        if (null == entranceGuardRecords || entranceGuardRecords.size() == 0) {
            log.info("暂无瑞仓内网门禁考勤未推送数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (EntranceGuardRecord entranceGuardRecord : entranceGuardRecords){
            id = entranceGuardRecord.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("recordid",entranceGuardRecord.getRecordid());
            sendDate.set("types",entranceGuardRecord.getTypes());
            sendDate.set("openid",entranceGuardRecord.getOpenid());
            sendDate.set("opentime",entranceGuardRecord.getOpentime());
            sendDate.set("doorid",entranceGuardRecord.getDoorid());
            sendDate.set("info",entranceGuardRecord.getInfo());
            sendDate.set("adminid",entranceGuardRecord.getAdminid());
            sendDate.set("pic",entranceGuardRecord.getPic());
            sendDate.set("adddate",entranceGuardRecord.getAdddate());
            sendDate.set("isoffline",entranceGuardRecord.getIsoffline());
            sendDate.set("capturepic",entranceGuardRecord.getCapturepic());
            sendDate.set("isopen",entranceGuardRecord.getIsopen());
            sendDate.set("serialno",entranceGuardRecord.getSerialno());
            sendDate.set("isupload",entranceGuardRecord.getIsupload());
            sendDate.set("uploaddate",entranceGuardRecord.getUploaddate());
            sendDate.set("temperature",entranceGuardRecord.getTemperature());
            sendDate.set("standard",entranceGuardRecord.getStandard());
            sendDate.set("cid",entranceGuardRecord.getCid());
            sendDate.set("names",entranceGuardRecord.getNames());
            sendDate.set("shebeino",entranceGuardRecord.getShebeino());
            sendDate.set("departmentid",entranceGuardRecord.getDepartmentid());
            sendDate.set("departname",entranceGuardRecord.getDepartname());
            sendDate.set("status",entranceGuardRecord.getStatus());

            System.out.println(sendDate);
            String url = "https://zgj20.cncico.com/wlwpt/entranceGuardRecord/entranceGuardRecord/addOpen";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = (Integer) jsonObject1.get("code");
            if (codes == 200) {
                log.info(String.format("瑞仓门禁考勤推送成功!" + id + "Json数据" + sendDate));
            } else {
                log.info(String.format("瑞仓门禁考勤推送失败!" + id + "Json数据" + sendDate));
            }
            sysConfigService.updateSysConfig(JobUtil.RC_MJKQ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
