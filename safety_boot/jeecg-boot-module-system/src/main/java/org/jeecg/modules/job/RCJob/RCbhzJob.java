package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
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
public class RCbhzJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCBHZTS);//瑞仓拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓混凝土拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓混凝土拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBasess = bhzCementBaseService.selectHntbhzList(curid, 1, shebeilist);
        if (null == bhzCementBasess || bhzCementBasess.size() == 0) {
            log.info(String.format("暂无瑞仓混凝土拌合站的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzCementBase bhzCementBases : bhzCementBasess) {
            id = bhzCementBases.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("batch_no", bhzCementBases.getBatchNo());
            jsonObject1.set("shebei_no", bhzCementBases.getShebeiNo());
            jsonObject1.set("work_no", bhzCementBases.getWorkNo());
            jsonObject1.set("handlers", bhzCementBases.getHandlers());
            jsonObject1.set("project_name", bhzCementBases.getProjectName());
            jsonObject1.set("job_location", bhzCementBases.getJobLocation());
            jsonObject1.set("poure_location", bhzCementBases.getPoureLocation());
            jsonObject1.set("cement_variety", bhzCementBases.getCementVariety());
            jsonObject1.set("additive_variety", bhzCementBases.getAdditiveVariety());
            jsonObject1.set("formula_no", bhzCementBases.getFormulaNo());
            jsonObject1.set("strength_rank", bhzCementBases.getStrengthRank());
            jsonObject1.set("stir_datetime", bhzCementBases.getStirDatetime());
            jsonObject1.set("save_datetime", bhzCementBases.getSaveDatetime());
            jsonObject1.set("client_no", bhzCementBases.getClientNo());
            jsonObject1.set("status", bhzCementBases.getStatus());
            jsonObject1.set("collect_datetime", bhzCementBases.getCollectDatetime());
            jsonObject1.set("estimate_number", bhzCementBases.getEstimateNumber());
            jsonObject1.set("product_datetime", bhzCementBases.getProductDatetime());
            jsonObject1.set("slump", bhzCementBases.getSlump());
            jsonObject1.set("over_level", bhzCementBases.getOverLevel());
            jsonObject1.set("alertstate", bhzCementBases.getAlertstate());
            jsonObject1.set("formula_id", bhzCementBases.getFormulaId());
            jsonObject1.set("time_over_level", bhzCementBases.getTimeOverLevel());
            jsonObject1.set("sys_org_code", bhzCementBases.getSysOrgCode());
            jsonObject1.set("overproof_status", bhzCementBases.getOverproofStatus());
            jsonObject1.set("renwudanstatus", bhzCementBases.getRenwudanstatus());
            jsonObject1.set("jiaozhustatus", bhzCementBases.getJiaozhustatus());
            jsonObject1.set("chuliaowd", bhzCementBases.getChuliaowd());
            jsonObject1.set("over_reason", bhzCementBases.getOverReason());
            jsonObject1.set("statistics", bhzCementBases.getStatistics());
            jsonObject1.set("isfilter", bhzCementBases.getIsfilter());
            jsonObject1.set("issend", bhzCementBases.getIssend());
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.selectcementlist(bhzCementBases.getBatchNo());
            JSONArray jsonArray1 = new JSONArray();
            for (BhzCementDetail bhzCementDetail : bhzCementDetails) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.set("materiale_type", bhzCementDetail.getMaterialeType());
                jsonObject2.set("materiale_id", bhzCementDetail.getMaterialeId());
                jsonObject2.set("materiale_name", bhzCementDetail.getMaterialeName());
                jsonObject2.set("reality_number", bhzCementDetail.getRealityNumber());
                jsonObject2.set("theory_number", bhzCementDetail.getTheoryNumber());
                jsonObject2.set("batch_no", bhzCementDetail.getBatchNo());
                jsonObject2.set("error_value", bhzCementDetail.getErrorValue());
                jsonObject2.set("over_value", bhzCementDetail.getOverValue());
                jsonObject2.set("materiale_over_level", bhzCementDetail.getMaterialeOverLevel());
                jsonObject2.set("over_primary_setvalue", bhzCementDetail.getOverPrimarySetvalue());
                jsonObject2.set("over_middle_setvalue", bhzCementDetail.getOverMiddleSetvalue());
                jsonObject2.set("over_advanced_setvalue", bhzCementDetail.getOverAdvancedSetvalue());
                jsonArray1.add(jsonObject2);
            }
            jsonObject1.set("detail", jsonArray1);
//            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/hnt/cementBases/concreteBaseUpload")
//            String back = HttpRequest.post("http://150.158.5.156:8088/iot/hnt/cementBases/concreteBaseUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓混凝土拌合站数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCBHZTS, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else if(code == 201){
                log.info(String.format("瑞仓混凝土拌合站数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCBHZTS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓混凝土拌合站数据推送失败!" + id));
            }
        }


    }

}
