package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
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
public class RCbhzczJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCBHZCBCL);//瑞仓拌合站超标处理推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓拌合站超标处理信息定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓拌合站超标处理信息的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementOverHandler> list = bhzCementOverHandlerService.listToday(shebeilist);
        for(BhzCementOverHandler bhzCementOverHandler : list){
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("baseId",bhzCementOverHandler.getBaseid());
            jsonObject1.set("problem_reasons",bhzCementOverHandler.getProblemReasons());
            jsonObject1.set("handle_way",bhzCementOverHandler.getHandleWay());
            jsonObject1.set("handle_result",bhzCementOverHandler.getHandleResult());
            jsonObject1.set("handle_time",bhzCementOverHandler.getHandleTime());
            jsonObject1.set("handle_person",bhzCementOverHandler.getHandlePerson());
            jsonObject1.set("supervisor_approval",bhzCementOverHandler.getSupervisorApproval());
            jsonObject1.set("supervisor_result",bhzCementOverHandler.getSupervisorResult());
            jsonObject1.set("supervisor_handle_time",bhzCementOverHandler.getSupervisorHandleTime());
            jsonObject1.set("approval_person",bhzCementOverHandler.getApprovalPerson());
            jsonObject1.set("remark",bhzCementOverHandler.getRemark());
            jsonObject1.set("file_path",bhzCementOverHandler.getFilePath());
            jsonObject1.set("file_path2",bhzCementOverHandler.getFilePath2());
            jsonObject1.set("overproof_status",bhzCementOverHandler.getOverproofStatus());
            jsonObject1.set("shebei_no",bhzCementOverHandler.getShebeiNo());
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/hnt/cementOverHandlers/cementHandelUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓拌合站超标处理信息数据推送成功!" ));
            } else {
                log.info(String.format("瑞仓拌合站超标处理信息数据推送失败!" ));
            }
        }

    }
}
