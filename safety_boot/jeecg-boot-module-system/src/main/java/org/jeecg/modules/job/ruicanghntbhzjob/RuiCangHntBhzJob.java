package org.jeecg.modules.job.ruicanghntbhzjob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonArray;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
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
import org.jeecg.modules.job.jobutil.RuiCangBhzUtil;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2022/3/10
 * \* Time: 15:49
 * \* Description:瑞仓混凝土拌合站数据推送
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RuiCangHntBhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzChaobiaoCfgService bhzChaobiaoCfgService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private RuiCangBhzUtil ruiCangBhzUtil;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RUICANG_HNTBHZ);//砼拌合站数据推送内网瑞苍
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到砼拌合站推送内网瑞苍配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1= Arrays.asList(split);
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selecthntbhzList(curid, 1,strsToList1);//所有的数据
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无砼拌合站未判断的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (BhzCementBase selecthntbhzone : selecthntbhzones) {
            JSONObject publicPitchList1 =new JSONObject();
            String shebeiNo = selecthntbhzone.getShebeiNo();
            id = selecthntbhzone.getId();
            publicPitchList1.set("batchNo",selecthntbhzone.getBatchNo());
            publicPitchList1.set("shebeiNo",shebeiNo);
            publicPitchList1.set("workNo",selecthntbhzone.getWorkNo());
            publicPitchList1.set("handlers",selecthntbhzone.getHandlers());
            publicPitchList1.set("projectName",selecthntbhzone.getProjectName());
            publicPitchList1.set("jobLocation",selecthntbhzone.getJobLocation());
            publicPitchList1.set("poureLocation",selecthntbhzone.getPoureLocation());
            publicPitchList1.set("cementVariety",selecthntbhzone.getCementVariety());
            publicPitchList1.set("additiveVariety",selecthntbhzone.getAdditiveVariety());
            publicPitchList1.set("formulaNo",selecthntbhzone.getFormulaNo());
            publicPitchList1.set("strengthRank",selecthntbhzone.getStrengthRank());
            publicPitchList1.set("stirDatetime",selecthntbhzone.getStirDatetime());
            publicPitchList1.set("saveDatetime",selecthntbhzone.getSaveDatetime());
            publicPitchList1.set("clientNo",selecthntbhzone.getClientNo());
            publicPitchList1.set("status",selecthntbhzone.getStatus());
            publicPitchList1.set("collectDatetime",selecthntbhzone.getCollectDatetime());
            publicPitchList1.set("estimateNumber",selecthntbhzone.getEstimateNumber());
            publicPitchList1.set("productDatetime",selecthntbhzone.getProductDatetime());
            publicPitchList1.set("slump",selecthntbhzone.getSlump());
            publicPitchList1.set("overLevel",selecthntbhzone.getOverLevel());
            publicPitchList1.set("alertstate",selecthntbhzone.getAlertstate());
            publicPitchList1.set("formulaId",selecthntbhzone.getFormulaId());
            publicPitchList1.set("timeOverLevel",selecthntbhzone.getTimeOverLevel());
            publicPitchList1.set("sysOrgCode",selecthntbhzone.getSysOrgCode());
            publicPitchList1.set("overproofStatus",selecthntbhzone.getOverproofStatus());
            publicPitchList1.set("renwudanstatus",selecthntbhzone.getRenwudanstatus());
            publicPitchList1.set("jiaozhustatus",selecthntbhzone.getJiaozhustatus());
            publicPitchList1.set("chuliaowd",selecthntbhzone.getChuliaowd());
            publicPitchList1.set("overReason",selecthntbhzone.getOverReason());
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(selecthntbhzone.getBatchNo());
            if(selectcementlist.size()>0){
                JSONArray publicMixerMoreList=new JSONArray();
                for (BhzCementDetail bhzCementDetail : selectcementlist) {
                    JSONObject publicPitchList2 =new JSONObject();
                    publicPitchList2.set("materialeType",bhzCementDetail.getMaterialeType());
                    publicPitchList2.set("materialeId",bhzCementDetail.getMaterialeId());
                    publicPitchList2.set("materialeName",bhzCementDetail.getMaterialeName());
                    publicPitchList2.set("realityNumber",bhzCementDetail.getRealityNumber());
                    publicPitchList2.set("theoryNumber",bhzCementDetail.getTheoryNumber());
                    publicPitchList2.set("batchNo",bhzCementDetail.getBatchNo());
                    publicPitchList2.set("errorValue",bhzCementDetail.getErrorValue());
                    publicPitchList2.set("overValue",bhzCementDetail.getOverValue());
                    publicPitchList2.set("materialeOverLevel",bhzCementDetail.getMaterialeOverLevel());
                    publicPitchList2.set("overPrimarySetvalue",bhzCementDetail.getOverPrimarySetvalue());
                    publicPitchList2.set("overMiddleSetvalue",bhzCementDetail.getOverMiddleSetvalue());
                    publicPitchList2.set("overAdvancedSetvalue",bhzCementDetail.getOverAdvancedSetvalue());
                    publicMixerMoreList.add(publicPitchList2);
                }
                publicPitchList1.set("bhzCementDetailList",publicMixerMoreList);
            }else{
                log.info(String.format("推送瑞仓拌合站没有材料信息" + DateUtils.now()));
            }
            BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(selecthntbhzone.getBatchNo());
            if(selectlist!=null){
                JSONObject publicPitchList3 =new JSONObject();
                publicPitchList3.set("baseid",selectlist.getBaseid());
                publicPitchList3.set("problemReasons",selectlist.getProblemReasons());
                publicPitchList3.set("handleWay",selectlist.getHandleWay());
                publicPitchList3.set("handleResult",selectlist.getHandleResult());
                publicPitchList3.set("handleTime",selectlist.getHandleTime());
                publicPitchList3.set("handlePerson",selectlist.getHandlePerson());
                publicPitchList3.set("supervisorApproval",selectlist.getSupervisorApproval());
                publicPitchList3.set("supervisorResult",selectlist.getSupervisorResult());
                publicPitchList3.set("supervisorHandleTime",selectlist.getSupervisorHandleTime());
                publicPitchList3.set("approvalPerson",selectlist.getApprovalPerson());
                publicPitchList3.set("remark",selectlist.getRemark());
                publicPitchList3.set("filePath",selectlist.getFilePath());
                publicPitchList3.set("filePath2",selectlist.getFilePath2());
                publicPitchList3.set("overproofStatus",selectlist.getOverproofStatus());
                publicPitchList1.set("bhzCementOverHandler",publicPitchList3);
            }else{
                log.info(String.format("推送瑞仓拌合站没有处置信息" + DateUtils.now()));
            }
            Integer integer = ruiCangBhzUtil.PostRuiCangHntBhz(publicPitchList1);
            if(integer==200){
                log.info(String.format("推送瑞仓拌合站信息成功" + DateUtils.now())+id);
            }else{
                log.info(String.format("推送瑞仓拌合站信息失败" + DateUtils.now())+id);
            }
            sysConfigService.updateSysConfig(JobUtil.RUICANG_HNTBHZ, id);//根据传过来的唯一值来修改当前判断到的数据id


        }
    }
}
