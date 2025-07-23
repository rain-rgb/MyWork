package org.jeecg.modules.job.sutaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName SHYJbhzJob：
 * @Description 上海有间拌合站数据推送
 * @Author 55314
 * @Date 2023/2/24 11:07
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SHYJbhzJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SHYJ_BHZ);//上海有间拌合站数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到上海有间智慧用电的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输上海有间智慧用电的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectListToSHYJ(shebeilist, curid);
        if (null == bhzCementBases || bhzCementBases.size() == 0) {
            log.info(String.format("暂无上海有间龙门吊的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (BhzCementBase bhzCementBase : bhzCementBases){
            id = bhzCementBase.getId();

            JSONObject sendJson = new JSONObject();

            sendJson.set("groupId",bhzCementBase.getBatchNo());
            sendJson.set("deviceId",bhzCementBase.getShebeiNo());
            sendJson.set("workOrderNumber",bhzCementBase.getWorkNo());
            sendJson.set("operator",bhzCementBase.getHandlers());
            sendJson.set("projectName",bhzCementBase.getProjectName());
            sendJson.set("constructionSite",bhzCementBase.getJobLocation());
            sendJson.set("castingPosition",bhzCementBase.getPoureLocation());
            sendJson.set("cementVariety",bhzCementBase.getCementVariety());
            sendJson.set("admixtureVariety",bhzCementBase.getAdditiveVariety());
            sendJson.set("formulaNumber",bhzCementBase.getFormulaNo());
            sendJson.set("intensityClass",bhzCementBase.getStrengthRank());
            sendJson.set("stirringTime",bhzCementBase.getStirDatetime());
            sendJson.set("storageTime",bhzCementBase.getSaveDatetime());
            sendJson.set("departureNumber",bhzCementBase.getClientNo());
            sendJson.set("state",bhzCementBase.getStatus());
            sendJson.set("acquisitionTime",bhzCementBase.getCollectDatetime());
            sendJson.set("squareQuantity",bhzCementBase.getEstimateNumber());
            sendJson.set("dischargeTime",bhzCementBase.getProductDatetime());
            sendJson.set("slump",bhzCementBase.getSlump());
            sendJson.set("superstandardGrade",bhzCementBase.getOverLevel());
            sendJson.set("isSuperstandard",bhzCementBase.getAlertstate());
            sendJson.set("recipeUuid",bhzCementBase.getFormulaId());
            sendJson.set("stirringTimeSGrade",bhzCementBase.getTimeOverLevel());
            sendJson.set("controlAuthority",bhzCementBase.getSysOrgCode());
            sendJson.set("auditStatus",bhzCementBase.getOverproofStatus());
            sendJson.set("dischargeTemperature",bhzCementBase.getChuliaowd());
            sendJson.set("earlyWarningMsg",bhzCementBase.getOverReason());
            sendJson.set("closure",bhzCementBase.getBhStatus());

            //推送
            String result = HttpRequest.post("https://api.ilabx.cn/iot-service/interface/mixingStation/saveMixingStationRecord")
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendJson))
                    .execute()
                    .body();

            pushandreturnService.saveData(id,String.valueOf(sendJson),selectsysconfigone.getRemark(),result);

            QueryWrapper<BhzCementDetail> bhzCementDetailQueryWrapper = new QueryWrapper<>();
            bhzCementDetailQueryWrapper.eq("batch_no",bhzCementBase.getBatchNo());
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.list(bhzCementDetailQueryWrapper);
            for (BhzCementDetail bhzCementDetail : bhzCementDetails){

                JSONObject sendCaiLiaoJson = new JSONObject();

                sendCaiLiaoJson.set("groupId",bhzCementDetail.getBatchNo());
                sendCaiLiaoJson.set("deviceId",bhzCementBase.getShebeiNo());
                sendCaiLiaoJson.set("materialType",bhzCementDetail.getMaterialeType());
                sendCaiLiaoJson.set("materialName",bhzCementDetail.getMaterialeName());
                sendCaiLiaoJson.set("actualConsumption",bhzCementDetail.getRealityNumber());
                sendCaiLiaoJson.set("theoreticalDosage",bhzCementDetail.getTheoryNumber());
                sendCaiLiaoJson.set("errorValue",bhzCementDetail.getErrorValue());
                sendCaiLiaoJson.set("superscalarValue",bhzCementDetail.getOverValue());
                sendCaiLiaoJson.set("superstandardGrade",bhzCementDetail.getMaterialeOverLevel());
                sendCaiLiaoJson.set("earlySuperscalar",bhzCementDetail.getOverPrimarySetvalue());
                sendCaiLiaoJson.set("middleSuperscalar",bhzCementDetail.getOverMiddleSetvalue());
                sendCaiLiaoJson.set("highSuperscalar",bhzCementDetail.getOverAdvancedSetvalue());
                sendCaiLiaoJson.set("hopperNumber",bhzCementDetail.getLiaodouhao());

                //推送
                String result1 = HttpRequest.post("https://api.ilabx.cn/iot-service/interface/mixingStation/saveMixingStationMaterial")
                        .header("Content-Type", "application/json")
                        .body(String.valueOf(sendCaiLiaoJson))
                        .execute()
                        .body();
                pushandreturnService.saveData(id,String.valueOf(sendJson),selectsysconfigone.getRemark(),result1);
            }
            sysConfigService.updateSysConfig(JobUtil.SHYJ_BHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
