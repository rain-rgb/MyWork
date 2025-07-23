package org.jeecg.modules.job.lab.push.job;

import com.trtm.iot.hntbhz.service.IBhzCementBaseService;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.entity.ConcreteUseVo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 初始化混凝土用量
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InitConcreteUseJob implements Job {

    private final static String I_TYPE = "initConcreteUse";

    // 滨淮组织机构
    private final static String BH_CODE = "A05A07A01A02A01%";

    private final static String YD_CODE = "A05A03A08A01%";

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IBhzCementBaseService service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig config = sysConfigService.selectsysconfigone(JobUtil.PM_INIT_CONCRETE_USE); //初始化混凝土用量 品茗
        if (config == null) {
            log.info(String.format("未获取到 混凝土用量初始化(品茗) 的配置信息" + DateUtils.now()));
            return;
        }
        Integer lastId = config.getCurid();
        ArrayList<String> list1 = new ArrayList<>();
//        list1.add("A05A01A06A17%");
//        list1.add("A05A03A08A01%");
        list1.add("A05A17A02A01%");
        List<Map> list = new ArrayList<>();
        int id = 0;
        for (String l : list1){
            list = service.getDataToInitPM(l,lastId);
            int size = list.size();
            if (list.size() == 0) {
                log.info(String.format("暂无未初始化的混凝土用量数据，时间：" + DateUtils.now()));
                return;
            }
            id  = (Integer) list.get(size - 1).get("id");

            List<ConcreteUseVo> data = new ArrayList<>();
            for (Map map : list) {
                data.add(resultToVo(map));
            }
            BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
            String result = baseHttpRequest.requestDataProcessing(data, I_TYPE, 0);
            Boolean success = baseHttpRequest.resultProcessing(result);
            if (success) {
                log.info(String.format("混凝土用量数据初始化成功，时间：" + DateUtils.now()));
            } else {
                log.error(String.format("混凝土用量数据初始化失败，时间：" + DateUtils.now()));
            }
        }
        sysConfigService.updateSysConfig(JobUtil.PM_INIT_CONCRETE_USE, id);

    }

    private ConcreteUseVo resultToVo(Map map) {
        ConcreteUseVo concreteuseVo = new ConcreteUseVo();
        concreteuseVo.setUseId(map.get("useId").toString());
        concreteuseVo.setTaskId(map.get("taskId").toString());
        concreteuseVo.setMixId(map.get("mixId").toString());
        concreteuseVo.setDeviceName(map.get("deviceName").toString());
        concreteuseVo.setPosition(map.get("posit1on").toString());
        concreteuseVo.setCapacity(Double.parseDouble(map.get("capacity").toString()));
        concreteuseVo.setStrength(map.get("strength").toString());
        concreteuseVo.setDischargeTime(map.get("dischargeTime").toString());
        concreteuseVo.setCreateId(map.get("createId").toString());
        return concreteuseVo;
    }
}
