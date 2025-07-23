package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonObject;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthtm.service.ITrHnthtMService;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.trtm.iot.trhnthts.service.ITrHnthtSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RChnthtJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrHnthtMService trHnthtMService;
    @Autowired
    private ITrHnthtSService trHnthtSService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCHNTHT);//瑞仓混凝土回弹数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓混凝土回弹数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓混凝土回弹数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrHnthtM> list = trHnthtMService.selectList(curid,shebeilist);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓混凝土回弹未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrHnthtM trHnthtM : list){
            id = trHnthtM.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("point",trHnthtM.getPoint());
            jsonObject1.set("testerpeople",trHnthtM.getTesterpeople());
            jsonObject1.set("checkTime",trHnthtM.getChecktime());
            jsonObject1.set("checkLocation",trHnthtM.getChecklocation());
            jsonObject1.set("testId",trHnthtM.getTestid());
            jsonObject1.set("place",trHnthtM.getPlace());
            jsonObject1.set("strength",trHnthtM.getStrength());
            jsonObject1.set("testingCount",trHnthtM.getTestingcount());
            jsonObject1.set("carbonizeDepth",trHnthtM.getCarbonizedepth());
            jsonObject1.set("detectionSurface",trHnthtM.getDetectionsurface());
            jsonObject1.set("pouringSurface",trHnthtM.getPouringsurface());
            jsonObject1.set("detectionAngle",trHnthtM.getDetectionangle());
            jsonObject1.set("isPumping",trHnthtM.getIspumping());
            jsonObject1.set("detectionStandard",trHnthtM.getDetectionstandard());
            jsonObject1.set("pouringDate",trHnthtM.getPouringdate());
            jsonObject1.set("testingMin",trHnthtM.getTestingmin());
            jsonObject1.set("testingAverage",trHnthtM.getTestingaverage());
            jsonObject1.set("standardDeviation",trHnthtM.getStandarddeviation());
            jsonObject1.set("estimatedValue",trHnthtM.getEstimatedvalue());
            jsonObject1.set("showMin",trHnthtM.getShowmin());
            jsonObject1.set("showAverage",trHnthtM.getShowaverage());
            jsonObject1.set("showStandardDev",trHnthtM.getShowstandarddev());
            jsonObject1.set("showEstimatedVal",trHnthtM.getShowestimatedval());
            jsonObject1.set("code",trHnthtM.getCode());
            jsonObject1.set("shebei_no",trHnthtM.getShebeiNo());
            jsonObject1.set("projectName",trHnthtM.getProjectname());
            jsonObject1.set("sgbw",trHnthtM.getSgbw());
            jsonObject1.set("istuisong",trHnthtM.getIstuisong());
            List<TrHnthtS> trHnthtSList = trHnthtSService.selectHntHtList(trHnthtM.getTestid());
            JSONArray jsonArray1 = new JSONArray();
            for (TrHnthtS trHnthtS :trHnthtSList){
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.set("testId",trHnthtS.getTestid());
                jsonObject2.set("number",trHnthtS.getNumber());
                jsonObject2.set("average",trHnthtS.getAverage());
                jsonObject2.set("carbonize",trHnthtS.getCarbonize());
                jsonObject2.set("strcar",trHnthtS.getStrcar());
                jsonObject2.set("calsurface",trHnthtS.getCalsurface());
                jsonObject2.set("calangle",trHnthtS.getCalangle());
                jsonObject2.set("pouringsurface",trHnthtS.getPouringsurface());
                jsonObject2.set("surface",trHnthtS.getSurface());
                jsonObject2.set("carbonization",trHnthtS.getCarbonization());
                jsonObject2.set("carbonizetwo",trHnthtS.getCarbonizetwo());
                jsonObject2.set("carbonizethree",trHnthtS.getCarbonizethree());
                jsonObject2.set("ispumping",trHnthtS.getIspumping());
                jsonObject2.set("standardid",trHnthtS.getStandardid());
                jsonObject2.set("flagcarbonization",trHnthtS.getFlagcarbonization());
                jsonArray1.add(jsonObject2);
            }
//            jsonObject1.set("detail",jsonArray1);
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/stjc/hnthtMs/concreteReboundValueMainUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓混凝土回弹数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCHNTHT, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else if(code == 402){
                log.info(String.format("瑞仓混凝土回弹数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCHNTHT, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓混凝土回弹数据推送失败!" + id));
            }
        }
    }
}
