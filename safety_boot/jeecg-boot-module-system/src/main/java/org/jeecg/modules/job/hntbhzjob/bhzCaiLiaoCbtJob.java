package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcailiaocbtj.entity.BhzCailiaoCbtj;
import com.trtm.iot.bhzcailiaocbtj.service.IBhzCailiaoCbtjService;
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
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bhzCaiLiaoCbtJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IBhzCailiaoCbtjService bhzCailiaoCbtjService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_CLCBTJ);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();

        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selectcailiao(curid, 0);
        //如果他等于空
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无砼拌合站未判断的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzCementBase selecthntbhzone : selecthntbhzones) {
            String shebeiNo = selecthntbhzone.getShebeiNo();
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(shebeiNo);
            if (sbjwd == null){
                log.info(String.format("没有设备编号" + DateUtils.now()));
                continue;
            }
            String batchNo = selecthntbhzone.getBatchNo();
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist1(batchNo);
            if (selectcementlist.size() == 0) {//如果为空呢 就把此数据更改为异常状态
                bhzCementBaseService.selectcailiaostatics(batchNo, 4);
                log.info(String.format("暂无砼拌合站的材料信息" + DateUtils.now()));
                continue;
            }
            for (BhzCementDetail b :selectcementlist){
                BhzCailiaoCbtj cbtj = bhzCailiaoCbtjService.selectclOne(shebeiNo,b.getMaterialeType(),b.getMaterialeName());
                if (cbtj == null){
                    BhzCailiaoCbtj bhzCailiaoCbtj = new BhzCailiaoCbtj();
                    bhzCailiaoCbtj.setShebeiNo(shebeiNo);
                    bhzCailiaoCbtj.setSysOrgCode(sbjwd.getSysOrgCode());
                    bhzCailiaoCbtj.setMaterialeType(b.getMaterialeType());
                    bhzCailiaoCbtj.setMaterialeName(b.getMaterialeName());
                    if (b.getMaterialeOverLevel() == 1){
                        bhzCailiaoCbtj.setOverPrimarySetvalue(1);
                        bhzCailiaoCbtj.setOverMiddleSetvalue(0);
                        bhzCailiaoCbtj.setOverAdvancedSetvalue(0);
                    }else if (b.getMaterialeOverLevel() == 2){
                        bhzCailiaoCbtj.setOverPrimarySetvalue(0);
                        bhzCailiaoCbtj.setOverMiddleSetvalue(1);
                        bhzCailiaoCbtj.setOverAdvancedSetvalue(0);
                    }else {
                        bhzCailiaoCbtj.setOverPrimarySetvalue(0);
                        bhzCailiaoCbtj.setOverMiddleSetvalue(0);
                        bhzCailiaoCbtj.setOverAdvancedSetvalue(1);
                    }
                    bhzCailiaoCbtjService.save(bhzCailiaoCbtj);
                }else {
                    if (b.getMaterialeOverLevel() == 1){
                        cbtj.setOverPrimarySetvalue(cbtj.getOverPrimarySetvalue()+1);
                    }else if (b.getMaterialeOverLevel() == 2){
                        cbtj.setOverMiddleSetvalue(cbtj.getOverMiddleSetvalue()+1);
                    }else {
                        cbtj.setOverAdvancedSetvalue(cbtj.getOverAdvancedSetvalue()+1);
                    }
                    bhzCailiaoCbtjService.updateById(cbtj);
                }
            }
        }
        sysConfigService.updateSysConfigs(JobUtil.BHZ_CLCBTJ, id, 0);//根据传过来的唯一值来修改当前判断到的数据id
    }
}