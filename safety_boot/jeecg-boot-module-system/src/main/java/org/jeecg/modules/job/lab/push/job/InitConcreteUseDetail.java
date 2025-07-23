package org.jeecg.modules.job.lab.push.job;

import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.entity.CementDetailVo;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 初始化混凝土用量
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InitConcreteUseDetail implements Job {

    private final static String I_TYPE = "initConcreteUseDetail";

    // 滨淮组织机构
    private final static String BH_CODE = "A05A07A01A02A01%";

    private final static String YD_CODE = "A05A03A08A01%";

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IBhzCementBaseService service;

    @Autowired
    private IBhzCementDetailService cementDetailService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.PM_INIT_CONCRETE_USE_DETAIL); //混凝土用量详情初始化 品茗
        if (config == null) {
            log.info(String.format("未获取到 混凝土用量详情初始化(品茗) 的配置信息" + DateUtils.now()));
            return;
        }
        Integer lastId = config.getCurid();
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("A05A01A06A17%");
        list1.add("A05A03A08A01%");
        list1.add("A05A17A02A01%");
        List<BhzCementBase> list = new ArrayList<>();
        int id = 0;
        for (String l : list1){
            list = service.getBatchNoByOrgCode(l,lastId);
            if (list.size() == 0){
                log.info(String.format("暂无未混凝土用量详情初始化(品茗)数据，时间：" + DateUtils.now()));
                return;
            }
            List<String> batchList = new ArrayList();
            for (BhzCementBase item : list) {
                batchList.add(item.getBatchNo());
            }
            id = list.get(list.size() - 1).getId();
            List<Map> details = cementDetailService.selectByBatchList(batchList);
            if (details.size() == 0) {
                log.info(String.format("暂无未推送的混凝土用量详情数据" + DateUtils.now()));
                return;
            }

            List<CementDetailVo> data = new ArrayList<>();
            for (Map map : details) {
                data.add(resultToVo(map));
            }
            BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
            String result = baseHttpRequest.requestDataProcessing(data, I_TYPE, 0);
            Boolean success = baseHttpRequest.resultProcessing(result);
            if (success) {
                log.info(String.format("混凝土用量数据详情初始化成功，时间：" + DateUtils.now()));
            } else {
                log.error(String.format("混凝土用量数据详情初始化失败，时间：" + DateUtils.now()));
            }
        }
        sysConfigService.updateSysConfig(JobUtil.PM_INIT_CONCRETE_USE_DETAIL, id);

    }

    private CementDetailVo resultToVo(Map map) {
        CementDetailVo vo = new CementDetailVo();
        vo.setTaskdatailId(map.get("taskdatailId").toString());
        vo.setUseId(map.get("useId").toString());
        if (map.get("mixId") != null){
            ShebeiInfo mixId = shebeiInfoService.selectshebeione(map.get("mixId").toString());
            vo.setMixId(mixId.getSysOrgCode());
        }
        vo.setMaterial(map.get("material").toString());
        vo.setTheoreticalDosage(Double.parseDouble(map.get("theoreticalDosage").toString()));
        vo.setActualDosage(Double.parseDouble(map.get("actualDosage").toString()));
        if (map.get("errorValue") != null){
            vo.setErrorValue(Double.parseDouble(map.get("errorValue").toString()));
        }else {
            vo.setErrorValue(0.0);
        }
        if (map.get("exceedanceValue") != null){
            vo.setExceedanceValue(Double.parseDouble(map.get("exceedanceValue").toString()));
        }else {
            vo.setExceedanceValue(0.0);
        }
        if (map.get("exceedanceLevel") != null){
            vo.setExceedanceLevel(map.get("exceedanceLevel").toString());
        }else {
            vo.setExceedanceLevel("1");
        }
        if (map.get("createId") != null){
            vo.setCreateId(map.get("createId").toString());
        }else {
            vo.setCreateId("");
        }
        return vo;
    }
}
