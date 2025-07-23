package org.jeecg.modules.job.lab.push.job;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实验室温度
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class labTempJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBysRealService bysRealService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HUN_LABTEMP);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到实验室温度推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        if (null == selectsysconfigone.getExtra()) {
            log.info(String.format("没有配置要传输实验室温度推送数据的设备" + DateUtils.now()));
            return;
        }
        JSONObject jsonObject = JSONUtil.parseObj(selectsysconfigone.getExtra());
        String shebeilist = jsonObject.getStr("shebeilist");//查询所有的设备
        String[] split = shebeilist.split(",");
        for (String shebei : split) {
            List<BysReal> list = bysRealService.queryListBySheBeis(shebei, selectsysconfigone.getCurid());
            for (BysReal bysReal : list) {
                Map map = new HashMap<>();
                map.put("labId", bysReal.getSysOrgCode());
                map.put("temphumId", bysReal.getGuid());
                map.put("measureTime", bysReal.getDatatime());
                map.put("temp", bysReal.getTemperature());
                map.put("humidity", bysReal.getHumidity());
                map.put("modifyId", "system");
                List tlist = new ArrayList();
                tlist.add(map);
                BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
                String openApi = baseHttpRequest.requestDataProcessing(tlist, "labTemp", 0);
                JSONObject jsonObject2 = new JSONObject(openApi);
                Boolean success = (Boolean) jsonObject2.get("success");
                if (success) {
                    log.info(String.format("实验室温度数据推送成功!" + bysReal.getId()));
                } else {
                    log.info(String.format("实验室温度数据推送失败!" + bysReal.getId()));
                }
                sysConfigService.updateSysConfig(JobUtil.HUN_LABTEMP, bysReal.getId());
            }
        }
    }
}
