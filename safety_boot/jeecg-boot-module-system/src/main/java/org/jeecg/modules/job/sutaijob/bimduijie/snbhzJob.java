package org.jeecg.modules.job.sutaijob.bimduijie;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName snbhzJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/11/6 9:44
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class snbhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "http://122.226.22.70:8867/prod-api/iot/STEQ";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_BIMBHZ);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectListstbim(shebeilist, curid);
        if (null == bhzCementBases || bhzCementBases.size() == 0) {
            log.info(String.format("暂无苏台拌合站未推送数据" + DateUtils.now()));
            return;
        }else {
            log.info(String.format("苏台拌合站开始推送！" + DateUtils.now()));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        //循环
        for (BhzCementBase bhzCementBase : bhzCementBases){
            id = bhzCementBase.getId();
            JSONObject sendBhzDate = new JSONObject();
            sendBhzDate.set("batch_no", bhzCementBase.getBatchNo());
            sendBhzDate.set("shebei_no", bhzCementBase.getShebeiNo());
            sendBhzDate.set("work_no", bhzCementBase.getWorkNo());
            sendBhzDate.set("handlers", bhzCementBase.getHandlers());
            sendBhzDate.set("project_name", bhzCementBase.getProjectName());
            sendBhzDate.set("job_location", bhzCementBase.getJobLocation());
            sendBhzDate.set("poure_location", bhzCementBase.getPoureLocation());
            sendBhzDate.set("cement_variety", bhzCementBase.getCementVariety());
            sendBhzDate.set("additive_variety", bhzCementBase.getAdditiveVariety());
            sendBhzDate.set("formula_no", bhzCementBase.getFormulaNo());
            sendBhzDate.set("strength_rank", bhzCementBase.getStrengthRank());
            sendBhzDate.set("stir_datetime", bhzCementBase.getStirDatetime());
            sendBhzDate.set("save_datetime", sdf.format(bhzCementBase.getSaveDatetime()));
            sendBhzDate.set("collect_datetime", sdf.format(bhzCementBase.getCollectDatetime()));
            sendBhzDate.set("estimate_number", bhzCementBase.getEstimateNumber());
            sendBhzDate.set("product_datetime", sdf.format(bhzCementBase.getProductDatetime()));
            sendBhzDate.set("slump", bhzCementBase.getSlump());
            sendBhzDate.set("over_level", bhzCementBase.getOverLevel());
            sendBhzDate.set("alertstate", bhzCementBase.getAlertstate());
            sendBhzDate.set("chuliaowd", bhzCementBase.getChuliaowd());
            sendBhzDate.set("over_reason", bhzCementBase.getOverReason());

            QueryWrapper<BhzCementDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("batch_no",bhzCementBase.getBatchNo());
            List<BhzCementDetail> list1 = bhzCementDetailService.list(queryWrapper);
            List sonList = new ArrayList();
            for (BhzCementDetail bhzCementDetail : list1) {
                JSONObject sendSonDate = new JSONObject();
                sendSonDate.set("materiale_type", bhzCementDetail.getMaterialeType());
                sendSonDate.set("materiale_name", bhzCementDetail.getMaterialeName());
                sendSonDate.set("reality_number", bhzCementDetail.getRealityNumber());
                sendSonDate.set("theory_number", bhzCementDetail.getTheoryNumber());
                sendSonDate.set("batch_no", bhzCementDetail.getBatchNo());
                sendSonDate.set("error_value", bhzCementDetail.getErrorValue());
                sendSonDate.set("over_value", bhzCementDetail.getOverValue());
                sendSonDate.set("materiale_over_level", bhzCementDetail.getMaterialeOverLevel());
                sendSonDate.set("over_primary_setvalue", bhzCementDetail.getOverPrimarySetvalue());
                sendSonDate.set("over_middle_setvalue", bhzCementDetail.getOverMiddleSetvalue());
                sendSonDate.set("over_advanced_setvalue", bhzCementDetail.getOverAdvancedSetvalue());

                sonList.add(sendSonDate);
            }
            sendBhzDate.set("sonList", sonList);

            JSONObject sendDate = new JSONObject();
            sendDate.set("iotType","混泥土搅拌站");
            sendDate.set("externalId",String.valueOf(id));
            sendDate.set("jsonData",sendBhzDate);
            sendDate.set("deviceCode",bhzCementBase.getShebeiNo());
            sendDate.set("createTime",sdf.format(bhzCementBase.getProductDatetime()));
            sendDate.set("warning",bhzCementBase.getOverLevel());

            String result = HttpRequest.post(url)
                    .body(String.valueOf(sendDate))
                    .timeout(20000)
                    .execute()
                    .body();
            if (result.contains("200")){
                log.info("苏台砼拌合站推送bim成功！");
            }else {
                log.info("苏台砼拌合站推送bim失败！");
            }
            sysConfigService.updateSysConfig(JobUtil.ST_BIMBHZ,id);
        }
    }
}
