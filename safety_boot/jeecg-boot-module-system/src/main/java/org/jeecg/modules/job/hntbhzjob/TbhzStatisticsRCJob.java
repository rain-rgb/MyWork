package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.NumberUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TbhzStatisticsRCJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private JobUtil jobUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_CB_TJ_RC);//拌合站超标统计瑞仓
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到拌合站超标统计瑞仓项目定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当时数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        Integer isworking = selectsysconfigone.getIsworking();
//        if (isworking == 1) {
//            log.info(String.format("拌合站超标统计瑞仓项目定时任务正在运行中！！" + DateUtils.now()));
//            return;
//        }
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selecthntbhzRC(curid, 0);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无砼拌合站未判断的数据" + DateUtils.now()));
            return;
        }
//        sysConfigService.updateSysConfigs(JobUtil.BHZ_CB_TJ_RC, curid, 1);
        int id = curid;
        for (BhzCementBase selecthntbhzone : selecthntbhzones) {
            if (selecthntbhzone.getOverLevel() == null) {
                sysConfigService.updateSysConfigs(JobUtil.BHZ_CB_TJ_RC, id, 0);//根据传过来的唯一值来修改当前判断到的数据id
                return;
            }
            int final_over_level = 0;
            String shebeiNo = selecthntbhzone.getShebeiNo();
            id = selecthntbhzone.getId();
            String batchNo = selecthntbhzone.getBatchNo();
            if (selecthntbhzone.getOverLevel() != null) {
                final_over_level = Convert.toInt(selecthntbhzone.getOverLevel(), 0);
            }
            if (batchNo == null) {
                bhzCementBaseService.updatehntbhzonestatics(batchNo, 20);
                log.info(String.format("无唯一标识" + DateUtils.now()));
                continue;
            }
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(batchNo);
//            if (selectcementlist.size() == 0) {//如果为空呢 就把此数据更改为异常状态
//                bhzCementBaseService.updatehntbhzonestatics(batchNo, 4);
//                log.info(String.format("暂无砼拌合站的材料信息" + DateUtils.now()));
//                continue;
//            }
            RCSattistics(selecthntbhzone, selectcementlist, final_over_level, shebeiNo);//统计
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
        sysConfigService.updateSysConfigs(JobUtil.BHZ_CB_TJ_RC, id, 0);//根据传过来的唯一值来修改当前判断到的数据id

    }

    /**
     * 砼统计表加信息（拌合机生产统计）
     *
     * @param selecthntbhzone
     * @param selectcementlist
     * @param level
     * @param sbno
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Boolean RCSattistics(BhzCementBase selecthntbhzone, List<BhzCementDetail> selectcementlist, int level, String sbno) {
        float estimate_number = NumberUtil.strToFloat(String.valueOf(selecthntbhzone.getEstimateNumber()));
        BhzCementStatistics bhzCementStatistics = new BhzCementStatistics();
        int SattisticsId = 0;
        Date productDatetime = selecthntbhzone.getProductDatetime();
        String datanyr = NumberUtil.Stringnyr(productDatetime);//格式化后的时间
        Date datanyr1 = NumberUtil.datanyr(datanyr);
        String projectName = selecthntbhzone.getProjectName();
        String poureLocation = selecthntbhzone.getPoureLocation();
        String jobLocation = selecthntbhzone.getJobLocation();
        String formulaNo = selecthntbhzone.getFormulaNo();
        String strengthRank = selecthntbhzone.getStrengthRank();
        String shebeiNo = selecthntbhzone.getShebeiNo();
        BhzCementStatistics selectlimit = bhzCementStatisticsService.selectlimit(datanyr1, projectName, poureLocation, jobLocation, formulaNo, strengthRank, shebeiNo);
        if (null != selectlimit) {
//            log.info(String.format("拌合站统计累加获取统计数据" + selectlimit.getId()));
            boolean b = count(selectlimit, level, estimate_number);//超标盘数以及方量累加
            if (b) {
                log.info(String.format("拌合站统计累加成功！"));
                BhzCementBase bhzCementBase222 = new BhzCementBase();
                bhzCementBase222.setId(selecthntbhzone.getId());
                bhzCementBase222.setCbStatistics(1);
                bhzCementBase222.setBhStatus(0);
                bhzCementBaseService.updateById(bhzCementBase222);
            } else {
//                log.info(String.format("拌合站统计累加失败！" + selecthntbhzone.getId()));
                return b;
            }

            SattisticsId = selectlimit.getId();
        } else {
            int addsattistics = addsattistics(selecthntbhzone);// 添加一条统计信息
            if (addsattistics == -1) {
                return false;
            }
            SattisticsId = addsattistics;
            BhzCementBase bhzCementBase222 = new BhzCementBase();
            bhzCementBase222.setId(selecthntbhzone.getId());
            bhzCementBase222.setCbStatistics(1);
            bhzCementBase222.setBhStatus(0);
            bhzCementBaseService.updateById(bhzCementBase222);
        }

        //查询统计表中匹配的数据
        BhzCementStatistics selectlimit2 = bhzCementStatisticsService.selectlimit(datanyr1, projectName, poureLocation, jobLocation, formulaNo, strengthRank, shebeiNo);
        if (selectlimit2 != null) {
            if (selecthntbhzone.getOverLevel() != null) {
                if (selecthntbhzone.getOverLevel() != 0 && selecthntbhzone.getOverproofStatus() != null && selecthntbhzone.getOverproofStatus() >= 10) {
                    //已处置，添加处置盘数
                    BhzCementStatistics statistics = new BhzCementStatistics();
                    statistics.setId(selectlimit2.getId());
                    if (selectlimit2.getAllHandleDish() == null) {
                        statistics.setAllHandleDish(1);
                    } else {
                        int num = selectlimit2.getAllHandleDish() + 1;
                        statistics.setAllHandleDish(num);
                    }
                    if (selecthntbhzone.getOverLevel() == 1) {
                        if (selectlimit2.getPrimaryHandle() == null) {
                            statistics.setPrimaryHandle(1);
                        } else {
                            int num = selectlimit2.getPrimaryHandle() + 1;
                            statistics.setPrimaryHandle(num);
                        }
                    } else if (selecthntbhzone.getOverLevel() == 2) {
                        if (selectlimit2.getMiddleHandle() == null) {
                            statistics.setMiddleHandle(1);
                        } else {
                            int num = selectlimit2.getMiddleHandle() + 1;
                            statistics.setMiddleHandle(num);
                        }
                    } else if (selecthntbhzone.getOverLevel() == 3) {
                        if (selectlimit2.getAdvancedHandle() == null) {
                            statistics.setAdvancedHandle(1);
                        } else {
                            int num = selectlimit2.getAdvancedHandle() + 1;
                            statistics.setAdvancedHandle(num);
                        }
                    }
                    bhzCementStatisticsService.updateById(statistics);
                    //更改主表中统计状态
                    BhzCementBase bhzCementBase = new BhzCementBase();
                    bhzCementBase.setId(selecthntbhzone.getId());
                    bhzCementBase.setCbStatistics(1);
                    bhzCementBase.setBhStatus(1);
                    bhzCementBaseService.updateById(bhzCementBase);
                } else if (selecthntbhzone.getOverproofStatus() != null && selecthntbhzone.getOverproofStatus() == 0) {
                    //更改主表中统计状态
                    if (selecthntbhzone.getOverLevel() == 0) {
                        BhzCementBase bhzCementBase = new BhzCementBase();
                        bhzCementBase.setId(selecthntbhzone.getId());
                        bhzCementBase.setCbStatistics(1);
                        bhzCementBase.setBhStatus(1);
                        bhzCementBaseService.updateById(bhzCementBase);
                    }
                }
            }
        }

        if (selectcementlist != null && selectcementlist.size() > 0) {
            jobUtil.detailStatistics(selectcementlist, sbno, SattisticsId, datanyr1);
        }
        /**
         * 料仓库存统计未写
         */
        return true;
    }

    /**
     * 把总盘数加1  如果超标级别为1 在初级超标盘数中加1 如果超标级别为2 在中 级超标盘数中加1 如果超标级别为3 在高 级超标盘数中加1
     *
     * @param Sattistics
     * @param level
     */
    public boolean count(BhzCementStatistics Sattistics, int level, float estimateNumber) {
        BhzCementStatistics statistics1 = new BhzCementStatistics();
        int id = Sattistics.getId();
        Double estimateNumbers = Sattistics.getEstimateNumber();
        Integer allDish = Sattistics.getAllDish();
        Integer primaryDish = Sattistics.getPrimaryDish();//初级超标盘数
        Integer middleDish = Sattistics.getMiddleDish();//中级超标盘数
        Integer advancedDish = Sattistics.getAdvancedDish();//高级超标盘数
        Integer allOverproofDish = Sattistics.getAllOverproofDish();//总超标盘数
        Integer allsum = allDish + 1;
        double sumfl = estimateNumbers + estimateNumber;
        Integer cjsum = 0;
        Integer allsums = 0;
        Integer zjsum = 0;
        Integer gjsum = 0;
        if (level == 1) {
            cjsum = primaryDish + 1;
            allsums = allOverproofDish + 1;
            zjsum = middleDish;
            gjsum = advancedDish;
        } else if (level == 2) {
            allsums = allOverproofDish + 1;
            zjsum = middleDish + 1;
            cjsum = primaryDish;
            gjsum = advancedDish;
        } else if (level == 3) {
            allsums = allOverproofDish + 1;
            gjsum = advancedDish + 1;
            zjsum = middleDish;
            cjsum = primaryDish;
        } else {
            cjsum = primaryDish;
            allsums = allOverproofDish;
            zjsum = middleDish;
            gjsum = advancedDish;
        }
        boolean b = bhzCementStatisticsService.updatestatisticsone(id, allsum, sumfl, cjsum, allsums, zjsum, gjsum);//总盘数加1
        return b;
    }

    /**
     * 添加一条统计信息
     *
     * @param selecthntbhzone
     */
    public int addsattistics(BhzCementBase selecthntbhzone) {
        String shebeiNo = selecthntbhzone.getShebeiNo();
        String projectName = selecthntbhzone.getProjectName();
        String jobLocation = selecthntbhzone.getJobLocation();
        String poureLocation = selecthntbhzone.getPoureLocation();
        String formulaNo = selecthntbhzone.getFormulaNo();
        String strengthRank = selecthntbhzone.getStrengthRank();
        Date productDatetime = selecthntbhzone.getProductDatetime();
        Integer overLevel = selecthntbhzone.getOverLevel();
        String stringnyr = NumberUtil.Stringnyr(productDatetime);
        Date datanyr = NumberUtil.datanyr(stringnyr);
        Double estimateNumber = selecthntbhzone.getEstimateNumber();
        BhzCementStatistics bhzCementStatistics = new BhzCementStatistics();
        bhzCementStatistics.setShebeiNo(shebeiNo);
        bhzCementStatistics.setProjectName(projectName);
        bhzCementStatistics.setJobLocation(jobLocation);
        bhzCementStatistics.setPoureLocation(poureLocation);
        bhzCementStatistics.setFormulaNo(formulaNo);
        bhzCementStatistics.setStrengthRank(strengthRank);
        bhzCementStatistics.setStatisticsTime(datanyr);
        if (overLevel == 1) {
            bhzCementStatistics.setAllOverproofDish(1);
            bhzCementStatistics.setPrimaryDish(1);
            bhzCementStatistics.setMiddleDish(0);
            bhzCementStatistics.setAdvancedDish(0);
        } else if (overLevel == 2) {
            bhzCementStatistics.setPrimaryDish(0);
            bhzCementStatistics.setMiddleDish(1);
            bhzCementStatistics.setAdvancedDish(0);
            bhzCementStatistics.setAllOverproofDish(1);
        } else if ((overLevel == 3)) {
            bhzCementStatistics.setPrimaryDish(0);
            bhzCementStatistics.setMiddleDish(0);
            bhzCementStatistics.setAdvancedDish(1);
            bhzCementStatistics.setAllOverproofDish(1);
        } else {
            bhzCementStatistics.setPrimaryDish(0);
            bhzCementStatistics.setMiddleDish(0);
            bhzCementStatistics.setAdvancedDish(0);
            bhzCementStatistics.setAllOverproofDish(0);
        }

//        if (overLevel == 2) {
//            bhzCementStatistics.setMiddleDish(1);
//            bhzCementStatistics.setAllOverproofDish(1);
//        } else {
//            bhzCementStatistics.setMiddleDish(0);
//            bhzCementStatistics.setAllOverproofDish(0);
//        }
//        if (overLevel == 3) {
//            bhzCementStatistics.setAdvancedDish(1);
//            bhzCementStatistics.setAllOverproofDish(1);
//        } else {
//            bhzCementStatistics.setAdvancedDish(0);
//            bhzCementStatistics.setAllOverproofDish(0);
//        }
        bhzCementStatistics.setPrimaryHandle(0);
        bhzCementStatistics.setPrimaryPercent(0.0);
        bhzCementStatistics.setMiddleHandle(0);
        bhzCementStatistics.setMiddlePercent(0.0);
        bhzCementStatistics.setAdvancedHandle(0);
        bhzCementStatistics.setAdvancedPercent(0.0);
        bhzCementStatistics.setAllDish(1);
        bhzCementStatistics.setAllHandleDish(0);
        bhzCementStatistics.setAllPercent(0.0);
        bhzCementStatistics.setEstimateNumber(estimateNumber);
        BhzCementStatistics selectlimitxxx = bhzCementStatisticsService.selectlimit(bhzCementStatistics.getStatisticsTime(), bhzCementStatistics.getProjectName(), bhzCementStatistics.getPoureLocation(), bhzCementStatistics.getJobLocation(), bhzCementStatistics.getFormulaNo(), bhzCementStatistics.getStrengthRank(), bhzCementStatistics.getShebeiNo());
        if (selectlimitxxx != null) {
            return -1;
        }
        boolean save = bhzCementStatisticsService.save(bhzCementStatistics);

        if (save) {
            log.info(String.format("拌合站统计新添加数据" + bhzCementStatistics.toString()));
        } else {
            log.error(String.format("拌合站统计新添加数据错误" + bhzCementStatistics.toString()));
            return -1;
        }
        BhzCementStatistics selectlimit2 = bhzCementStatisticsService.selectlimit(bhzCementStatistics.getStatisticsTime(), bhzCementStatistics.getProjectName(), bhzCementStatistics.getPoureLocation(), bhzCementStatistics.getJobLocation(), bhzCementStatistics.getFormulaNo(), bhzCementStatistics.getStrengthRank(), bhzCementStatistics.getShebeiNo());
        Integer id = selectlimit2.getId();
        return id;
    }

}
