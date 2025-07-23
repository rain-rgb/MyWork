package org.jeecg.modules.job.lab.push.job;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;

import org.jeecg.modules.job.lab.push.entity.PotVo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * 初始化机组罐体存储物 (料仓) 品茗
 *
 * @author lis1
 * @date 2023/1/9
 * @time 9:06
 */
@Slf4j
public class InitPot implements Job {
    // 义东一标梁场
    private final static String YD_CODE = "A05A01A03A01A01A02A01";

    private final static String I_TYPE = "initPot";

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IWzliaocangService service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.PM_INIT_POT); //料仓初始化 品茗
        if (config == null) {
            log.info(String.format("未获取到 粉料仓初始化(品茗) 的配置信息" + DateUtils.now()));
            return;
        }
        List<Wzliaocang> list = service.selectByOrgCodeToPM(YD_CODE);
        if (list == null || list.size() < 1) {
            log.info(String.format("暂无粉料仓初始化(品茗)未推送数据" + DateUtils.now()));
        }

        List<PotVo> data = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Wzliaocang item : list) {
                item.getName();
                PotVo potVo = resultToVo(item);
                data.add(potVo);
            }
        }
        BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
        String result = baseHttpRequest.requestDataProcessing(data, I_TYPE, 0);
        Boolean success = baseHttpRequest.resultProcessing(result);
        if (success) {
            log.info(String.format("机组罐体存储物初始化成功，时间：" + DateUtils.now()));
        } else {
            log.error(String.format("机组罐体存储物初始化失败，时间：" + DateUtils.now()));
        }
    }


    private PotVo resultToVo(Wzliaocang data) {
        PotVo potVo = new PotVo();
        potVo.setPotId(data.getGuid());
        potVo.setMixingId(data.getSysOrgCode());
        String name = data.getName();
        String substring = name.substring(0, 2);
        if ("1-" .equals(substring)) {
            potVo.setCrewId("1号机组");
        } else if ("2-" .equals(substring)) {
            potVo.setCrewId("2号机组");
        } else if ("3-" .equals(substring)) {
            potVo.setCrewId("3号机组");
        } else if ("4-" .equals(substring)) {
            potVo.setCrewId("4号机组");
        } else {
            return null;
        }
        potVo.setPotName(data.getName());
        potVo.setStorageName(data.getCailiaoname());
        potVo.setStorageCapacity(data.getPicizhong());
        String status = data.getLiaocangStatus();
        if ("1" .equals(status) || "2" .equals(status))
            potVo.setStorageStatus("0");
        if ("3" .equals(status))
            potVo.setStorageStatus("1");
        if ("4" .equals(status))
            potVo.setStorageStatus("2");
        potVo.setCreateId(data.getCreateBy());
        return potVo;
    }
}
