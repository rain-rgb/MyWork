package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.http.HttpRequest;
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
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName bhzJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/11/8 14:06
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService BhzCementDetailService;
    @Autowired
    private IBhzCementBaseService BhzCementBaseService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "http://115.236.10.10:8081/zjjg-iot/mixing_station/saveMixingStationData";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJQS_BHZ);//甬金衢上拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到甬金衢上砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输甬金衢上砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBasess = BhzCementBaseService.selectListyjqs(shebeilist, curid);
        if (null == bhzCementBasess || bhzCementBasess.size() == 0) {
            log.info(String.format("暂无甬金衢上砼拌合站的数据" + DateUtils.now()));
            return;
        }else {
            log.info(String.format("甬金衢上砼拌合站数据推送开始！" + DateUtils.now()));
        }
        int id=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BhzCementBase bhzCementBases : bhzCementBasess) {
            id = bhzCementBases.getId();
            JSONObject sendObject = new JSONObject();
            sendObject.set("batchNo",bhzCementBases.getBatchNo());
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(bhzCementBases.getShebeiNo());
            sendObject.set("deviceName", sbjwd.getSbname()); //设备名称
            sendObject.set("shebeiNo", bhzCementBases.getShebeiNo());
            sendObject.set("workNo", bhzCementBases.getWorkNo());
            sendObject.set("handlers", bhzCementBases.getHandlers());
            sendObject.set("projectName", bhzCementBases.getProjectName());
            sendObject.set("jobLocation", bhzCementBases.getJobLocation());
            sendObject.set("poureLocation", bhzCementBases.getPoureLocation());
            sendObject.set("cementVariety", bhzCementBases.getCementVariety());
            sendObject.set("additiveVariety", bhzCementBases.getAdditiveVariety());
            sendObject.set("formulaNo", bhzCementBases.getFormulaNo());
            sendObject.set("strengthRank", bhzCementBases.getStrengthRank());
            sendObject.set("stirDatetime", sdf.format(bhzCementBases.getStirDatetime()));
            sendObject.set("saveDatetime", sdf.format(bhzCementBases.getSaveDatetime()));
            sendObject.set("clientNo", bhzCementBases.getClientNo());
            sendObject.set("status", bhzCementBases.getStatus());
            sendObject.set("collectDatetime", sdf.format(bhzCementBases.getCollectDatetime()));
            sendObject.set("estimateNumber", bhzCementBases.getEstimateNumber());
            sendObject.set("productDatetime", sdf.format(bhzCementBases.getProductDatetime()));
            sendObject.set("slump", bhzCementBases.getSlump());
            sendObject.set("overLevel", bhzCementBases.getOverLevel());
            sendObject.set("alertstate", bhzCementBases.getAlertstate());
            sendObject.set("formulaId", bhzCementBases.getFormulaId());
            sendObject.set("timeOverLevel", bhzCementBases.getTimeOverLevel());

            String shebeiNo = bhzCementBases.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            sendObject.set("orgCode", selectshebeione.getTunnelId());
            sendObject.set("orgName", selectshebeione.getTunnelName());

            List list = new ArrayList();
            QueryWrapper<BhzCementDetail> bhzCementDetailQueryWrapper = new QueryWrapper<>();
            bhzCementDetailQueryWrapper.eq("batch_no",bhzCementBases.getBatchNo());
            List<BhzCementDetail> bhzCementDetailList = BhzCementDetailService.list(bhzCementDetailQueryWrapper);

            for (BhzCementDetail bhzCementDetail : bhzCementDetailList) {
                JSONObject sendSonObject = new JSONObject();
                sendSonObject.set("batchNo",bhzCementDetail.getBatchNo());
                sendSonObject.set("errorValue",bhzCementDetail.getErrorValue());
                sendSonObject.set("id",bhzCementDetail.getId());
                sendSonObject.set("liaodouhao",bhzCementDetail.getLiaodouhao());
                sendSonObject.set("materialeId",bhzCementDetail.getMaterialeId());
                sendSonObject.set("materialeName",bhzCementDetail.getMaterialeName());
                sendSonObject.set("materialeOverLevel",bhzCementDetail.getMaterialeOverLevel());
                sendSonObject.set("materialeType",bhzCementDetail.getMaterialeType());
                sendSonObject.set("overAdvancedSetvalue",bhzCementDetail.getOverAdvancedSetvalue());
                sendSonObject.set("overMiddleSetvalue",bhzCementDetail.getOverMiddleSetvalue());
                sendSonObject.set("overPrimarySetvalue",bhzCementDetail.getOverPrimarySetvalue());
                sendSonObject.set("overValue",bhzCementDetail.getOverValue());
                sendSonObject.set("realityNumber",bhzCementDetail.getRealityNumber());
                sendSonObject.set("theoryNumber",bhzCementDetail.getTheoryNumber());

                list.add(sendSonObject);
            }

            sendObject.set("bhzCementDetailList", list);
            String result = HttpRequest.post(url)
                    .header("client-code","gaoxun")
                    .body(String.valueOf(sendObject))
                    .timeout(20000)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendObject),selectsysconfigone.getRemark(),result);
            if (result.contains("200")){
                log.info("甬金衢上高速3标拌合站推送成功！");
            }else{
                log.info("甬金衢上高速3标拌合站推送失败！");
            }
            sysConfigService.updateSysConfig(JobUtil.YJQS_BHZ,id);
        }
    }
}
