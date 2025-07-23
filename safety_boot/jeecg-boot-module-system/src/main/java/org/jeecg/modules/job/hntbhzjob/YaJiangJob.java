package org.jeecg.modules.job.hntbhzjob;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tryajiangstatistics.entity.TrYajiangStatistics;
import com.trtm.iot.tryajiangstatistics.service.ITrYajiangStatisticsService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.NumberUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 压浆统计
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class YaJiangJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ITrYajiangMService yajiangMService;

    @Autowired
    private ITrYajiangSService yajiangSService;

    @Autowired
    private IShebeiInfoService iShebeiInfoService;

    @Autowired
    private ITrYajiangStatisticsService yajiangStatisticsService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJ_HJJC);

        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到压浆定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();

        List<TrYajiangSM> trYajiangSM = yajiangMService.selectListstatistics(curid, 0);

        if (null == trYajiangSM || trYajiangSM.size() == 0) {
            log.info(String.format("暂无压浆未判断的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for(TrYajiangSM trYajiang : trYajiangSM){
            id = trYajiang.getId();
            String syjid = trYajiang.getSyjid();
            String sbjno = trYajiang.getYjsbbh();
            if(sbjno == null){
                log.info(String.format("无唯一标识" + DateUtils.now()));
                continue;
            }
            // 查子表中的合格情况
            List<TrYajiangSM> yajiangs = yajiangSService.findBysyjid(syjid);
            if(yajiangs == null){
                //如果为空呢 就把此数据更改为异常状态
                yajiangMService.updateStatistics(sbjno,4);
                log.info(String.format("暂无合格信息" + DateUtils.now()));
                continue;
            }

            // 查找设备所属部门
            ShebeiInfo shebeiInfo = iShebeiInfoService.SBJWD(sbjno);
            if (shebeiInfo == null) {
                //如果为空呢 就把此数据更改为异常状态
                yajiangMService.updateStatistics(sbjno,4);
                log.info(String.format("暂无所属部门信息" + DateUtils.now()));
                continue;
            }
            // 拿到当前设备所属部门
            String infoSbjno = shebeiInfo.getSysOrgCode();

            for(TrYajiangSM yajiang : yajiangs){
                trYajiang.setHege(yajiang.getHege());
                this.Sattistics(trYajiang,infoSbjno);// 调用统计方法，完成统计
            }

            TrYajiangM trYajiangM = new TrYajiangM();
            trYajiangM.setId(id);
            trYajiangM.setStatistics(1);
            boolean b = yajiangMService.updateById(trYajiangM);
            if(b){
                log.info(String.format("压浆统计修改状态成功！！！"+id + DateUtils.now()));
            }else{
                log.info(String.format("压浆统计修改状态失败！！！"+id  + DateUtils.now()));
            }
        }
        sysConfigService.updateSysConfigs(JobUtil.YJ_HJJC, id,0);//根据传过来的唯一值来修改当前判断到的数据id
    }

    /**
     * 压浆统计方法
     * @param trYajiangSM
     * @param infoSbjno 设备部门名称
     */
    public synchronized void Sattistics(TrYajiangSM trYajiangSM, String infoSbjno){
        // 创建压浆统计表对象
        TrYajiangStatistics statistics = new TrYajiangStatistics();

        Date sqsj = trYajiangSM.getYjsj();
        String datanyr = NumberUtil.Stringnyr(sqsj);//格式化后的时间
        Date datanyr1 = NumberUtil.datanyr(datanyr);

        String htbh = trYajiangSM.getHtbh();
        String gcmc = trYajiangSM.getGcmc();
        String zhbw = trYajiangSM.getZhbw();
        String sgbw = trYajiangSM.getSgbw();
        String gjjg = trYajiangSM.getGjjg();
        String gjbh = trYajiangSM.getGjbh();
        String yjsbbh = trYajiangSM.getYjsbbh();
        String lblx = trYajiangSM.getLblx();

        TrYajiangStatistics selectlimit = yajiangStatisticsService.selectlimit(datanyr1, htbh, gcmc, zhbw, sgbw, gjjg, gjbh, yjsbbh, lblx, infoSbjno);
        if(null != selectlimit){
            log.info(String.format("压浆统计累加获取统计数据"+selectlimit.toString()));

            if(trYajiangSM.getHege().equals("合格") || trYajiangSM.getHege().equals("1")){
                selectlimit.setTrYajiangSum(selectlimit.getTrYajiangSum() + 1);
                boolean b = yajiangStatisticsService.updateById(selectlimit);
                if(b){
                    log.info(String.format("压浆统计修改状态成功！！！"+ DateUtils.now()));
                }else{
                    log.info(String.format("压浆统计修改状态失败！！！"+ DateUtils.now()));
                }
            }else {
                selectlimit.setTrYajiangSum(selectlimit.getTrYajiangSum() + 1);
                selectlimit.setTrYajaingOversum(selectlimit.getTrYajaingOversum() + 1);
                boolean b = yajiangStatisticsService.updateById(selectlimit);
                if(b){
                    log.info(String.format("压浆统计修改状态成功！！！"+ DateUtils.now()));
                }else{
                    log.info(String.format("压浆统计修改状态失败！！！"+ DateUtils.now()));
                }
            }

        } else {
            if(trYajiangSM.getHege().equals("不合格") || trYajiangSM.getHege().equals("0")){
                statistics.setHtbh(htbh);
                statistics.setGcmc(gcmc);
                statistics.setYjsj(datanyr1);
                statistics.setZhbw(zhbw);
                statistics.setSgbw(sgbw);
                statistics.setGjjg(gjjg);
                statistics.setGjbh(gjbh);
                statistics.setYjsbbh(yjsbbh);
                statistics.setLblx(lblx);
                statistics.setSysOrgCode(infoSbjno);
                statistics.setTrYajiangSum(1);
                statistics.setTrYajaingOversum(1);
                yajiangStatisticsService.insert(statistics);
            }else {
                statistics.setHtbh(htbh);
                statistics.setGcmc(gcmc);
                statistics.setYjsj(datanyr1);
                statistics.setZhbw(zhbw);
                statistics.setSgbw(sgbw);
                statistics.setGjjg(gjjg);
                statistics.setGjbh(gjbh);
                statistics.setYjsbbh(yjsbbh);
                statistics.setLblx(lblx);
                statistics.setSysOrgCode(infoSbjno);
                statistics.setTrYajiangSum(1);
                statistics.setTrYajaingOversum(0);
                yajiangStatisticsService.insert(statistics);
            }
        }
    }

}
