package org.jeecg.modules.job.sutaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhydreal.entity.DeviceElectricRealdata;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName STBHZJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/3/25 14:16
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class STBHZJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService iBhzCementDetailService;
    @Autowired
    private IBhzCementBaseService iBhzCementBaseService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DecimalFormat df = new DecimalFormat("#.0");
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SUTAI_HNTBHZ);//苏台拌合站
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
        List<BhzCementBase> bhzCementBasess = iBhzCementBaseService.selectListssutai(shebeilist, curid);
        if (null == bhzCementBasess || bhzCementBasess.size() == 0) {
            log.info(String.format("暂无苏台砼拌合站的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (BhzCementBase bhzCementBases : bhzCementBasess){
            id = bhzCementBases.getId();
            List list = new ArrayList();
            JSONObject sendmap = JSONUtil.createObj();
            sendmap.set("Bidsection",bhzCementBases.getProjectName());
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(bhzCementBases.getShebeiNo());
            sendmap.set("mix",selectshebeione.getSbname());
            sendmap.set("mixStation",selectshebeione.getSbname());
            sendmap.set("datauploadTime",bhzCementBases.getSaveDatetime());
            sendmap.set("startTime",bhzCementBases.getCollectDatetime());
            sendmap.set("dischargeTime",bhzCementBases.getProductDatetime());
            sendmap.set("mixCode",bhzCementBases.getShebeiNo());
            sendmap.set("outterId",bhzCementBases.getBatchNo());
            sendmap.set("proName",bhzCementBases.getProjectName());
            sendmap.set("workOrderNum",bhzCementBases.getWorkNo());
            sendmap.set("consRatioNum",bhzCementBases.getFormulaNo());
            sendmap.set("strengthGrade",bhzCementBases.getStrengthRank());
            sendmap.set("pouringPart",bhzCementBases.getPoureLocation());
            sendmap.set("mileage",bhzCementBases.getJobLocation());
            sendmap.set("num",df.format(bhzCementBases.getEstimateNumber()));
            sendmap.set("cementVarieties",bhzCementBases.getCementVariety());
            sendmap.set("agitation",bhzCementBases.getStirDatetime());
            sendmap.set("admixtureBrand",bhzCementBases.getAdditiveVariety());
            sendmap.set("oper",bhzCementBases.getHandlers());

            List<BhzCementDetail> bhzCementDetails = iBhzCementDetailService.selectcementlist(bhzCementBases.getBatchNo());
            for (BhzCementDetail bhzCementDetail : bhzCementDetails){
                JSONObject sendmapson = JSONUtil.createObj();
                sendmapson.set("type",String.valueOf(bhzCementDetail.getMaterialeType()));
                sendmapson.set("name",bhzCementDetail.getMaterialeName());
                sendmapson.set("actValue",bhzCementDetail.getRealityNumber());
                sendmapson.set("stoichiometric",bhzCementDetail.getTheoryNumber());
                sendmapson.set("proValue",bhzCementDetail.getMaterialeType());
                sendmapson.set("carriedUpperLimit","");
                sendmapson.set("carriedLowerLimit","");
                sendmapson.set("standardUpperLimit",bhzCementDetail.getOverPrimarySetvalue());
                sendmapson.set("standardLowerLimit",bhzCementDetail.getOverPrimarySetvalue());
                sendmapson.set("targetValue","");
                sendmapson.set("moistureRatio","");
                sendmapson.set("StoneRatio","");
                sendmapson.set("error",bhzCementDetail.getErrorValue());
                sendmapson.set("errorRate",bhzCementDetail.getOverValue());
                Integer materialeOverLevel = bhzCementDetail.getMaterialeOverLevel();
                String overproof = "否";
                if (materialeOverLevel>0){
                    overproof = "是";
                }
                sendmapson.set("overproof",overproof);
                list.add(sendmapson);
            }
            sendmap.set("childrenList",list);
            List sendlist = new ArrayList();
            sendlist.add(sendmap);
            String url = "https://sutai.jtzjy.cn/api/ExtApi2/STNXD/EXAPP-00024/hntbhz/getMessage?_pk=cKBNpCSvDTWm1KqO";
            String back = HttpRequest.post(url)
                    .header("Content-Type","application/json")
                    .body(String.valueOf(sendlist))
                    .timeout(20000)
                    .execute().body();
            JSONObject jsonObject1 = new JSONObject(back);
            Integer codes = Integer.parseInt(jsonObject1.get("status").toString());
            if(codes==1){
                log.info(String.format("苏台砼拌合站数据推送成功!" + id+"Json数据"+sendmap));
            }else{
                log.info(String.format("苏台砼拌合站数据推送失败!" + id+"Json数据"+sendmap));
            }
            sysConfigService.updateSysConfig(JobUtil.SUTAI_HNTBHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
