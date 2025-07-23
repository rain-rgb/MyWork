package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonArray;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcm.service.ITrGangjinbhcMService;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trgangjinbhcs.service.ITrGangjinbhcSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCgbJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrGangjinbhcMService trGangjinbhcMService;
    @Autowired
    private ITrGangjinbhcSService trGangjinbhcSService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCGBTS);//瑞仓钢保数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓钢保数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓钢保数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrGangjinbhcM> list = trGangjinbhcMService.selectGbList(curid,shebeilist);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓钢保未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrGangjinbhcM trGangjinbhcM : list){
            id = trGangjinbhcM.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("point",trGangjinbhcM.getPoint());
            jsonObject1.set("type",trGangjinbhcM.getType());
            jsonObject1.set("testId",trGangjinbhcM.getTestid());
            jsonObject1.set("targetType",trGangjinbhcM.getTargettype());
            jsonObject1.set("zoneCount",trGangjinbhcM.getZonecount());
            jsonObject1.set("passRate",trGangjinbhcM.getPassrate());
            jsonObject1.set("checkLocation",trGangjinbhcM.getChecklocation());
            jsonObject1.set("testerId",trGangjinbhcM.getTesterid());
            jsonObject1.set("checkTime",trGangjinbhcM.getChecktime());
            jsonObject1.set("masterDiameter",trGangjinbhcM.getMasterdiameter());
            jsonObject1.set("masterSpacing",trGangjinbhcM.getMasterspacing());
            jsonObject1.set("designThickness",trGangjinbhcM.getDesignthickness());
            jsonObject1.set("zdesignThickness",trGangjinbhcM.getZdesignthickness());
            jsonObject1.set("subDiameter",trGangjinbhcM.getSubdiameter());
            jsonObject1.set("subSpacing",trGangjinbhcM.getSubspacing());
            jsonObject1.set("curvedDiameter",trGangjinbhcM.getCurveddiameter());
            jsonObject1.set("Code",trGangjinbhcM.getCode());
            jsonObject1.set("shebei_no",trGangjinbhcM.getShebeiNo());
            jsonObject1.set("projectname",trGangjinbhcM.getProjectname());
            jsonObject1.set("sgbw",trGangjinbhcM.getSgbw());
            jsonObject1.set("istuisong",trGangjinbhcM.getIstuisong());
            List<TrGangjinbhcS> trGangjinbhcSList = trGangjinbhcSService.selectGangjinSList(trGangjinbhcM.getTestid());
            JSONArray jsonArray = new JSONArray();
            for (TrGangjinbhcS trGangjinbhcS : trGangjinbhcSList){
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.set("testId",trGangjinbhcS.getTestid());
                jsonObject2.set("thickness",trGangjinbhcS.getThickness());
                jsonObject2.set("distance",trGangjinbhcS.getDistance());
                jsonObject2.set("strthickness",trGangjinbhcS.getStrthickness());
                jsonObject2.set("strdistance",trGangjinbhcS.getStrdistance());
                jsonObject2.set("beforedistance",trGangjinbhcS.getBeforedistance());
                jsonObject2.set("flagpassrate",trGangjinbhcS.getFlagpassrate());
                jsonObject2.set("signals",trGangjinbhcS.getSignals());
                jsonObject2.set("designthickness",trGangjinbhcS.getDesignthickness());
                jsonObject2.set("masterspacing",trGangjinbhcS.getMasterspacing());
                jsonObject2.set("direction",trGangjinbhcS.getDirection());
                jsonArray.add(jsonObject2);
            }
            jsonObject1.set("detail",jsonArray);
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/stjc/gangjinbhcMs/reinforcementCoverUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓钢保数据推送成功!" ));
                sysConfigService.updateSysConfig(JobUtil.RCGBTS, id);//根据传过来的唯一值来修改当前判断到的数据id

            }else if(code == 402){
                log.info(String.format("瑞仓钢保数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCGBTS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓钢保数据推送失败!" ));
            }
        }
    }
}
