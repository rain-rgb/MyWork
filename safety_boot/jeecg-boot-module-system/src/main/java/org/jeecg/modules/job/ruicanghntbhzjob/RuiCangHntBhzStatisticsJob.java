package org.jeecg.modules.job.ruicanghntbhzjob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.RuiCangBhzUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2022/3/11
 * \* Time: 13:44
 * \* Description:瑞苍内网拌合站数据统计推送
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RuiCangHntBhzStatisticsJob implements Job {
    @Autowired
    private RuiCangBhzUtil ruiCangBhzUtil;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementStatisticsService iBhzCementStatisticsService;
    @Autowired
    private IBhzCementDetailStatisticsService iBhzCementDetailStatisticsService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
         //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RUICANG_HNTBHZSTATICSTIC);//砼拌合站数据推送内网瑞苍
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
        List<String> strsToList1 = Arrays.asList(split);
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String format = sdf.format(date);
//        Date parse=null;
//        try {
//            parse= sdf.parse(format);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        List<BhzCementStatistics> bhzCementStatistics = iBhzCementStatisticsService.selectBhzStatisticsLists(curid, strsToList1);
        if (null == bhzCementStatistics || bhzCementStatistics.size() == 0) {
            log.info(String.format("暂无砼拌合站统计的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (BhzCementStatistics bhzCementStatistic : bhzCementStatistics) {
            JSONObject publicPitchList1 =new JSONObject();
            id = bhzCementStatistic.getId();
            publicPitchList1.set("shebeiNo",bhzCementStatistic.getShebeiNo());
            publicPitchList1.set("projectName",bhzCementStatistic.getProjectName());
            publicPitchList1.set("jobLocation",bhzCementStatistic.getJobLocation());
            publicPitchList1.set("poureLocation",bhzCementStatistic.getPoureLocation());
            publicPitchList1.set("formulaNo",bhzCementStatistic.getFormulaNo());
            publicPitchList1.set("strengthRank",bhzCementStatistic.getStrengthRank());
            publicPitchList1.set("primaryDish",bhzCementStatistic.getPrimaryDish());
            publicPitchList1.set("primaryHandle",bhzCementStatistic.getPrimaryHandle());
            publicPitchList1.set("primaryPercent",bhzCementStatistic.getPrimaryPercent());
            publicPitchList1.set("middleDish",bhzCementStatistic.getMiddleDish());
            publicPitchList1.set("middleHandle",bhzCementStatistic.getMiddleHandle());
            publicPitchList1.set("middlePercent",bhzCementStatistic.getMiddlePercent());
            publicPitchList1.set("advancedDish",bhzCementStatistic.getAdvancedDish());
            publicPitchList1.set("advancedHandle",bhzCementStatistic.getAdvancedHandle());
            publicPitchList1.set("advancedPercent",bhzCementStatistic.getAdvancedPercent());
            publicPitchList1.set("allDish",bhzCementStatistic.getAllDish());
            publicPitchList1.set("allOverproofDish",bhzCementStatistic.getAllOverproofDish());
            publicPitchList1.set("allHandleDish",bhzCementStatistic.getAllHandleDish());
            publicPitchList1.set("allPercent",bhzCementStatistic.getAllPercent());
            publicPitchList1.set("estimateNumber",bhzCementStatistic.getEstimateNumber());
            publicPitchList1.set("statisticsTime",bhzCementStatistic.getStatisticsTime());
            publicPitchList1.set("istongji",bhzCementStatistic.getIstongji());
            List<BhzCementDetailStatistics> selectdetaillist = iBhzCementDetailStatisticsService.selectdetaillist(id);
            if(selectdetaillist.size()>0){
                JSONArray publicMixerMoreList=new JSONArray();
                for (BhzCementDetailStatistics bhzCementDetailStatistics : selectdetaillist) {
                    JSONObject publicPitchList2 =new JSONObject();
                    publicPitchList2.set("csId",bhzCementDetailStatistics.getCsId());
                    publicPitchList2.set("materialeType",bhzCementDetailStatistics.getMaterialeType());
                    publicPitchList2.set("materialeName",bhzCementDetailStatistics.getMaterialeName());
                    publicPitchList2.set("realityNumber",bhzCementDetailStatistics.getRealityNumber());
                    publicPitchList2.set("theoryNumber",bhzCementDetailStatistics.getTheoryNumber());
                    publicPitchList2.set("primaryNumber",bhzCementDetailStatistics.getPrimaryNumber());
                    publicPitchList2.set("middleNumber",bhzCementDetailStatistics.getMiddleNumber());
                    publicPitchList2.set("advancedNumber",bhzCementDetailStatistics.getAdvancedNumber());
                    publicPitchList2.set("cailiaoid",bhzCementDetailStatistics.getCailiaoid());
                    publicMixerMoreList.add(publicPitchList2);
                }
                publicPitchList1.set("bhzCementDetailStatisticsList",publicMixerMoreList);
            }else{
                log.info(String.format("暂无砼拌合站统计的材料数据" + DateUtils.now()));
            }
            //System.out.println("推送数据"+publicPitchList1);
            Integer integer = ruiCangBhzUtil.PostRuiCangHntBhzStatistics(publicPitchList1);
            if(integer==200){
                sysConfigService.updateSysConfig(JobUtil.RUICANG_HNTBHZSTATICSTIC, id);//根据传过来的唯一值来修改当前判断到的数据id
                log.info(String.format("推送瑞仓拌合站统计信息成功" + DateUtils.now())+id);
            }else{
                log.info(String.format("推送瑞仓拌合站统计信息失败" + DateUtils.now())+id);
            }
        }



    }
}
