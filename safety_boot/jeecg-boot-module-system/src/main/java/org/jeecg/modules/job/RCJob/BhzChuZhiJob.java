package org.jeecg.modules.job.RCJob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2022/5/18
 * \* Time: 13:27
 * \* Description: 瑞苍拌合站超标数据 如初级超标 超过48小时外 未处置的 自动升级为 中级超标 依次升级 超标等级
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BhzChuZhiJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_BHZ_CHAOBIAO_UPGRADE);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞苍砼拌合站配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selecthntbhzChaobiaoList( 1,0);//所有的数据
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无砼拌合站未判断的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzCementBase selecthntbhzone : selecthntbhzones) {
            Date productDatetime = selecthntbhzone.getProductDatetime();
            Integer id1 = selecthntbhzone.getId();
            Integer overLevel = selecthntbhzone.getOverLevel();
            String overReason = selecthntbhzone.getOverReason();
            Integer overproofStatus = selecthntbhzone.getOverproofStatus();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(new Date());//当前时间
            String format1 = sdf.format(productDatetime);
            int i=0;
            try {
                i = DateUtils.differHours(format1,format);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(overLevel==1 && overproofStatus==0){
                if(i>curid){
                BhzCementBase bhzCementBase=new BhzCementBase();
                    bhzCementBase.setId(id1);
                    bhzCementBase.setOverLevel(2);
                    if(overReason!=null){
                        bhzCementBase.setOverReason(overReason+","+"等级提升到中级");
                    }else{
                        bhzCementBase.setOverReason("等级提升到中级");
                    }
                    boolean b = bhzCementBaseService.updateById(bhzCementBase);
                    if(b){
                        log.info(String.format("等级提升到中级成功" + DateUtils.now())+id);
                    }else{
                        log.info(String.format("等级提升到中级失败" + DateUtils.now())+id);
                    }
                }
            } else if(overLevel==2 && overproofStatus==0){
                if(i>curid){
                    BhzCementBase bhzCementBase=new BhzCementBase();
                    bhzCementBase.setId(id1);
                    bhzCementBase.setOverLevel(3);
                    if(overReason!=null){
                        bhzCementBase.setOverReason(overReason+","+"等级提升到高级");
                    }else{
                        bhzCementBase.setOverReason("等级提升到高级");
                    }
                    boolean b = bhzCementBaseService.updateById(bhzCementBase);
                    if(b){
                        log.info(String.format("等级提升到高级成功" + DateUtils.now())+id);
                    }else{
                        log.info(String.format("等级提升到高级失败" + DateUtils.now())+id);
                    }
                }
            }
        }
    }
}
