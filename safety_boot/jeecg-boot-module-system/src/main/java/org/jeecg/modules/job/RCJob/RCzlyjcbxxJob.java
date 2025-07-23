package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCzlyjcbxxJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IZhanglaYajiangOverHandlerService zhanglaYajiangOverHandlerService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCZLYJXX);//瑞仓张拉压浆超标信息推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓张拉压浆超标信息定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓张拉压浆超标信息的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<ZhanglaYajiangOverHandler> list = zhanglaYajiangOverHandlerService.listToday(shebeilist);
        for(ZhanglaYajiangOverHandler zhanglaYajiangOverHandler : list){
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("baseId",zhanglaYajiangOverHandler.getBaseid());
            jsonObject1.set("problem_reasons",zhanglaYajiangOverHandler.getProblemReasons());
            jsonObject1.set("handle_way",zhanglaYajiangOverHandler.getHandleWay());
            jsonObject1.set("handle_result",zhanglaYajiangOverHandler.getHandleResult());
            jsonObject1.set("handle_time",zhanglaYajiangOverHandler.getHandleTime());
            jsonObject1.set("handle_person",zhanglaYajiangOverHandler.getHandlePerson());
            jsonObject1.set("supervisor_approval",zhanglaYajiangOverHandler.getSupervisorApproval());
            jsonObject1.set("supervisor_result",zhanglaYajiangOverHandler.getSupervisorResult());
            jsonObject1.set("supervisor_handle_time",zhanglaYajiangOverHandler.getSupervisorHandleTime());
            jsonObject1.set("approval_person",zhanglaYajiangOverHandler.getApprovalPerson());
            jsonObject1.set("remark",zhanglaYajiangOverHandler.getRemark());
            jsonObject1.set("file_path",zhanglaYajiangOverHandler.getFilePath());
            jsonObject1.set("file_path2",zhanglaYajiangOverHandler.getFilePath2());
            jsonObject1.set("overproof_status",zhanglaYajiangOverHandler.getOverproofStatus());
            jsonObject1.set("shebei_no",zhanglaYajiangOverHandler.getShebeiNo());
            System.out.println(jsonObject1);

            String back = HttpRequest.post("http://47.96.161.157:1080/api/zlyj/yajiangOverHandlers/tensionAndMudJackingHandelUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓张拉压浆超标处理信息数据推送成功!"));
            } else {
                log.info(String.format("瑞仓张拉压浆超标处理信息数据推送失败!"));
            }
        }
    }
}
