package org.jeecg.modules.job.syjjob;

import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class SyjStatisticsjob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService itSyjzbService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private JobUtil jobUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SYJ_STATIXTICS);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到试验机定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<TSyjzb> selectsyjones = itSyjzbService.selectsyjonesstatistics(curid,0);
        if (null == selectsyjones || selectsyjones.size() == 0) {
            log.info(String.format("暂无试验机未判断的数据" + DateUtils.now()));
            return;
        }
        int id =0;
        String pdjg;
        for (TSyjzb selectsyjone: selectsyjones
             ) {
            String sjqd = selectsyjone.getSjqd();
            if (null==sjqd){
                selectsyjone.setSjqd("无");
            }
            id = selectsyjone.getId();
            String syjid = selectsyjone.getSyjid();
            pdjg = selectsyjone.getPdjg();
            String shebeiNo = selectsyjone.getSbbh();
            if(syjid==null){
                log.info(String.format("无唯一标识" + DateUtils.now()));
                continue;
            }
            ShebeiInfo shebeiInfo = shebeiInfoService.SBJWD(shebeiNo);
            if (shebeiInfo == null || pdjg == null) {//如果为空呢 就把此数据更改为异常状态
                itSyjzbService.updateSyjzbOneStatistics(syjid, 4);
                log.info(String.format("暂无试验机的设备信息" + DateUtils.now()));
                continue;
            }
            jobUtil.SyjSatistics(selectsyjone,shebeiInfo,pdjg,shebeiNo);
            TSyjzb tSyjzb = new TSyjzb();
            tSyjzb.setId(id);
            tSyjzb.setStatistics(1);
            boolean b = itSyjzbService.updateById(tSyjzb);
            if(b){
                log.info(String.format("试验机拌合站统计修改状态成功！！！"+id + DateUtils.now()));
            }else{
                log.info(String.format("试验机统计修改状态失败！！！"+id  + DateUtils.now()));
            }
        }
        sysConfigService.updateSysConfigs(JobUtil.SYJ_STATIXTICS,id,0);
    }
}
