package org.jeecg.modules.job.lab.push.job;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.entity.SandStorageVo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lis1
 * @date 2023/1/10
 * @time 15:13
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InitSandStorage implements Job {

    private final static String I_TYPE = "initSandStorage";

    private final static String YD_CODE = "A05A01A03A01A01A02A01";

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IWzliaocangService service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.PM_INIT_SAND_STORAGE); //沙石料仓初始化 品茗
        if (config == null) {
            log.info(String.format("未获取到 砂石料仓初始化(品茗) 的配置信息" + DateUtils.now()));
            return;
        }

        List<Wzliaocang> list = service.getSandStorageDataToPM(YD_CODE);
        if (list == null || list.size() < 1) {
            log.info(String.format("暂无砂石料仓初始化(品茗)未推送数据" + DateUtils.now()));
        }

        List<SandStorageVo> data = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Wzliaocang item : list) {
                item.getName();
                SandStorageVo sandStorageVo = resultToVo(item);
                data.add(sandStorageVo);
            }
        }
        BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
        String result = baseHttpRequest.requestDataProcessing(data, I_TYPE, 0);
        Boolean success = baseHttpRequest.resultProcessing(result);
        if (success) {
            log.info(String.format("沙石料仓初始化成功，时间：" + DateUtils.now()));
        } else {
            log.error(String.format("沙石料仓初始化失败，时间：" + DateUtils.now()));
        }

    }

    private SandStorageVo resultToVo(Wzliaocang data) {
        SandStorageVo sandStorageVo = new SandStorageVo();
        sandStorageVo.setSandId(data.getGuid());
        sandStorageVo.setMixingId(data.getSysOrgCode());
        sandStorageVo.setSandName(data.getName());
        String storage = data.getPicizhong();
        if (storage != null) {
            sandStorageVo.setStorage(Integer.parseInt(storage));
        } else {
            sandStorageVo.setStorage(0);
        }
        String status = data.getLiaocangStatus();
        if ("3" .equals(status)) {
            sandStorageVo.setStorageStatus("1");
        } else if ("4" .equals(status)) {
            sandStorageVo.setStorageStatus("2");
        } else {
            sandStorageVo.setStorageStatus("0");
        }
        sandStorageVo.setCreateId(data.getCreateBy());
        return sandStorageVo;
    }
}
