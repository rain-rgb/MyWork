package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
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
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2022/5/25
 * \* Time: 20:54
 * \* Description:拌合站统计（拌合站数据id3371179-3394334的数据）
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BhzStatisticsJobhistory implements Job {
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
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private JobUtil jobUtil;
    @Value("${server.jobswitch}")
    private String jobswitch;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        if (!jobswitch.equals("true")){
//            log.info(String.format("跳出拌合站统计任务" + DateUtils.now()));
//           return;
//        }
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZSTAHIS);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到砼拌合站统计(历史数据)定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        Integer isworking = selectsysconfigone.getIsworking();
        Integer id1 = selectsysconfigone.getId();
        Integer curdate = Integer.parseInt(selectsysconfigone.getCurdate());
//        if(isworking==1){
//            log.info(String.format("拌合站统计任务正在运行中" + DateUtils.now()));
//            return;
//        }
//        SysConfig selectsysconfigone1 = sysConfigService.selectsysconfigone(JobUtil.TBHZ_FILTER); //砼拌合站数据过滤
//        String shebeilist = null;
//        Integer curid1 = null;
//        if (null != selectsysconfigone1) {
//            curid1 = selectsysconfigone1.getCurid();
//            String extra = selectsysconfigone1.getExtra();
//            JSONObject jsonObject = JSONUtil.parseObj(extra);
//            if (null != jsonObject || !jsonObject.isEmpty()) {
//                shebeilist = jsonObject.getStr("shebeilist");
//            }
//        }
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selecthntbhzonesstatistics1(curid, 0, curdate);
        //如果他等于空
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无砼拌合站未判断的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        int final_over_level = 0;
//        sysConfigService.updateSysConfigs(JobUtil.BHZ_STATISTICS, curid,1);
        for (BhzCementBase selecthntbhzone : selecthntbhzones) {
//            if (selecthntbhzone.getAlertstate() == 0) {
//                SysConfig selectsysconfigone2 = sysConfigService.selectsysconfigone(JobUtil.HNTBHZ_CFG_NEW);//拌合站超标监测=48
//                if (null != selectsysconfigone2) {
//                    if (selectsysconfigone2.getIsworking() == 1) {
//                        sysConfigService.updateSysConfigs(JobUtil.HNTBHZ_CFG_NEW, selectsysconfigone2.getCurid(), 0);
//                    }
//                }
//                log.info("该拌合站数据没有超标检测" + DateUtils.now());
//                return;
//            }
            Integer alertstate = selecthntbhzone.getAlertstate();
            String projectName = selecthntbhzone.getProjectName();
            String poureLocation = selecthntbhzone.getPoureLocation();
            String jobLocation = selecthntbhzone.getJobLocation();
            String formulaNo = selecthntbhzone.getFormulaNo();
            String strengthRank = selecthntbhzone.getStrengthRank();
            String shebeiNo = selecthntbhzone.getShebeiNo();
            id = selecthntbhzone.getId();
//            if (null != shebeilist && null != curid1) {
//                if (shebeilist.contains(shebeiNo) && id > curid1) {
//                    log.info("该拌合站数据没有数据过滤" + DateUtils.now());
//                    return;
//                }
//            }
//            if (shebeiNo.contains("FEIQI")) {
//                log.info("该拌合站数据已过滤" + DateUtils.now());
//                selecthntbhzone.setStatistics(40);
//                bhzCementBaseService.updateById(selecthntbhzone);
//                continue;
//            }
            if (alertstate == 40) {
                selecthntbhzone.setOverLevel(0);
            }
            if (null == projectName || "".equals(projectName)) {
                selecthntbhzone.setProjectName("无");
            }
            if (null == poureLocation || "".equals(poureLocation)) {
                selecthntbhzone.setPoureLocation("无");
            }
            if (null == strengthRank || "".equals(strengthRank)) {
                selecthntbhzone.setStrengthRank("无");
            }
            if (null == jobLocation || "".equals(jobLocation)) {
                selecthntbhzone.setJobLocation("无");
            }
            if (null == formulaNo || "".equals(formulaNo)) {
                selecthntbhzone.setFormulaNo("无");
            }
            String batchNo = selecthntbhzone.getBatchNo();
            final_over_level = selecthntbhzone.getOverLevel();
            if (batchNo == null) {
                log.info(String.format("无唯一标识" + DateUtils.now()));
                continue;
            }
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(batchNo);
            if (selectcementlist.size() == 0) {//如果为空呢 就把此数据更改为异常状态
                bhzCementBaseService.updatehntbhzonestatics(batchNo, 4);
                log.info(String.format("暂无砼拌合站的材料信息" + DateUtils.now()));
                continue;
            }
            jobUtil.Sattistics(selecthntbhzone, selectcementlist, final_over_level, shebeiNo);//统计
            BhzCementBase bhzCementBase = new BhzCementBase();
            bhzCementBase.setId(id);
            bhzCementBase.setStatistics(1);
            boolean b = bhzCementBaseService.updateById(bhzCementBase);
            if (b) {
                log.info(String.format("拌合站统计修改状态成功！！！" + id + DateUtils.now()));
            } else {
                log.info(String.format("拌合站统计修改状态失败！！！" + id + DateUtils.now()));
            }
        }
        sysConfigService.updateSysConfigs(JobUtil.BHZSTAHIS, id, 0);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
