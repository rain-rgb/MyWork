package org.jeecg.modules.job.hntbhzjob;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglastatistics.entity.TrZhanglaStatistics;
import com.trtm.iot.trzhanglastatistics.service.ITrZhanglaStatisticsService;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxbMStatistics;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.NumberUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 张压统计
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ZhangLaMJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ITrZhanglaXxbService iTrZhanglaXxbMService;

    @Autowired
    private ITrZhanglaMService zhanglaMService;

    @Autowired
    private ITrZhanglaStatisticsService iTrZhanglaStatisticsService;

    @Autowired
    private IShebeiInfoService iShebeiInfoService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLM_HJJC);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到张压定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();

        List<TrZhanglaXxbMStatistics> trZhanglaXxbs = iTrZhanglaXxbMService.selectxxbListstatistics(curid,0);
        if (null == trZhanglaXxbs || trZhanglaXxbs.size() == 0) {
            log.info(String.format("暂无张压未判断的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (TrZhanglaXxbMStatistics trZhanglaXxb : trZhanglaXxbs){

            id = trZhanglaXxb.getId();
            String syjid = trZhanglaXxb.getSyjid();
            String sbjno = trZhanglaXxb.getShebeibianhao();
            if(sbjno == null){
                log.info(String.format("无唯一标识" + DateUtils.now()));
                continue;
            }
            // 查子表中的合格情况
            List<TrZhanglaXxbMStatistics> zhanglas = zhanglaMService.findBysyjid(syjid);
            if(zhanglas == null){
                //如果为空呢 就把此数据更改为异常状态
                iTrZhanglaXxbMService.updateStatistics(sbjno,4);
                log.info(String.format("暂无合格信息" + DateUtils.now()));
                continue;
            }

            // 设备所属部门
            ShebeiInfo shebeiInfo = iShebeiInfoService.SBJWD(sbjno);
            if (shebeiInfo == null) {
                //如果为空呢 就把此数据更改为异常状态
                iTrZhanglaXxbMService.updateStatistics(sbjno,4);
                log.info(String.format("暂无所属部门信息" + DateUtils.now()));
                continue;
            }
            String infoSbjno = shebeiInfo.getSysOrgCode();

            for (TrZhanglaXxbMStatistics zhangla : zhanglas){
                trZhanglaXxb.setHege(zhangla.getHege());
                this.Sattistics(trZhanglaXxb,infoSbjno);// 调用统计方法，完成统计
            }

            TrZhanglaXxb trZhanglaXxb1 = new TrZhanglaXxb();
            trZhanglaXxb1.setId(id);
            trZhanglaXxb1.setStatistics(1);
            boolean b = iTrZhanglaXxbMService.updateById(trZhanglaXxb1);
            if(b){
                log.info(String.format("张拉统计修改状态成功！！！"+id + DateUtils.now()));
            }else{
                log.info(String.format("张拉统计修改状态失败！！！"+id  + DateUtils.now()));
            }
        }
        sysConfigService.updateSysConfigs(JobUtil.ZLM_HJJC, id,0);//根据传过来的唯一值来修改当前判断到的数据id
    }


    /**
     * 张压统计方法
     * @param trZhanglaXxb
     * @param infoSbjno 设备部门名称
     */
    public synchronized void Sattistics(TrZhanglaXxbMStatistics trZhanglaXxb,String infoSbjno){
        //封装对象
        TrZhanglaStatistics statistics = new TrZhanglaStatistics();

        Date sqsj = trZhanglaXxb.getSgsj();
        String datanyr = NumberUtil.Stringnyr(sqsj);//格式化后的时间
        Date datanyr1 = NumberUtil.datanyr(datanyr);

        String gcmc = trZhanglaXxb.getGcmc();
        String yzlc = trZhanglaXxb.getYzlc();
        String gjbh = trZhanglaXxb.getGjbh();
        String shebeibianhao = trZhanglaXxb.getShebeibianhao();
        String gjmc = trZhanglaXxb.getGjmc();
        String snsjqd = trZhanglaXxb.getSnsjqd();

        TrZhanglaStatistics selectlimit = iTrZhanglaStatisticsService.selectlimit(datanyr1, gcmc, yzlc, gjbh, shebeibianhao, gjmc, snsjqd, infoSbjno);
        if(null != selectlimit){
            log.info(String.format("张拉统计累加获取统计数据"+selectlimit.toString()));

            if(trZhanglaXxb.getHege().equals("合格") || trZhanglaXxb.getHege().equals("1")){
                selectlimit.setTrZhanglaSum(selectlimit.getTrZhanglaSum() + 1);
                boolean b = iTrZhanglaStatisticsService.updateById(selectlimit);
                if(b){
                    log.info(String.format("张拉统计修改状态成功！！！"+ DateUtils.now()));
                }else{
                    log.info(String.format("张拉统计修改状态失败！！！"+ DateUtils.now()));
                }
            }else {
                selectlimit.setTrZhanglaSum(selectlimit.getTrZhanglaSum() + 1);
                selectlimit.setTrZhanglaOversum(selectlimit.getTrZhanglaOversum() + 1);
                boolean b = iTrZhanglaStatisticsService.updateById(selectlimit);
                if(b){
                    log.info(String.format("张拉统计修改状态成功！！！"+ DateUtils.now()));
                }else{
                    log.info(String.format("张拉统计修改状态失败！！！"+ DateUtils.now()));
                }
            }

        } else {
            if(trZhanglaXxb.getHege().equals("不合格") || trZhanglaXxb.getHege().equals("0")){
                statistics.setGcmc(gcmc);
                statistics.setYzlc(yzlc);
                statistics.setGjbh(gjbh);
                statistics.setShebeibianhao(shebeibianhao);
                statistics.setGjmc(gjmc);
                statistics.setSnsjqd(snsjqd);
                statistics.setSqsj(datanyr1);
                statistics.setSysOrgCode(infoSbjno);
                statistics.setTrZhanglaSum(1);
                statistics.setTrZhanglaOversum(1);
                iTrZhanglaStatisticsService.insert(statistics);
            }else {
                statistics.setGcmc(gcmc);
                statistics.setYzlc(yzlc);
                statistics.setGjbh(gjbh);
                statistics.setShebeibianhao(shebeibianhao);
                statistics.setGjmc(gjmc);
                statistics.setSnsjqd(snsjqd);
                statistics.setSqsj(datanyr1);
                statistics.setSysOrgCode(infoSbjno);
                statistics.setTrZhanglaSum(1);
                statistics.setTrZhanglaOversum(0);
                iTrZhanglaStatisticsService.insert(statistics);
            }
        }
    }
    

}
