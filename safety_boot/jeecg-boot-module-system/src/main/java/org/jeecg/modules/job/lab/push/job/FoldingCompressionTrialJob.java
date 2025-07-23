package org.jeecg.modules.job.lab.push.job;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.syj.entity.SyjzbVo;
import com.trtm.iot.syj.service.ITSyjzbService;
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
 * 实验室抗折抗压实验
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class FoldingCompressionTrialJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService itSyjzbService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HUN_NINGTCOMPRESSION);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到抗折抗压实验推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        if (null == selectsysconfigone.getExtra()) {
            log.info(String.format("没有配置要传输抗折抗压实验推送数据的设备" + DateUtils.now()));
            return;
        }
        JSONObject jsonObject = JSONUtil.parseObj(selectsysconfigone.getExtra());
        String shebeilist = jsonObject.getStr("shebeilist");//查询所有的设备
        List<SyjzbVo> list = itSyjzbService.queryLists("100137",shebeilist, selectsysconfigone.getCurid());//查询所有需要传输的数据
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无抗折抗压实验未推送数据" + DateUtils.now()));
            return;
        }
        for (SyjzbVo syjzbVo : list) {
            Map map = new HashMap<>();
            map.put("trialId", syjzbVo.getSYJID());
            map.put("labId", syjzbVo.getSysOrgCode());
            map.put("designStrength", syjzbVo.getSJQD());
            map.put("deviceName", syjzbVo.getSbname());
            map.put("trialType", syjzbVo.getSYLX());
            if (null == syjzbVo.getGCMC() && null != syjzbVo.getCJMC()) {
                map.put("engineeringName", syjzbVo.getCJMC());
            } else if (null != syjzbVo.getGCMC() && null == syjzbVo.getCJMC()) {
                map.put("engineeringName", syjzbVo.getGCMC());
            } else if (null != syjzbVo.getGCMC() && null != syjzbVo.getCJMC()) {
                map.put("engineeringName", syjzbVo.getGCMC());
            } else {
                map.put("engineeringName", null);
            }
            map.put("reagentNo", syjzbVo.getSJBH());
            map.put("age", syjzbVo.getLQ());
            map.put("designValue", syjzbVo.getQDDBZ());
            map.put("reagentSize", syjzbVo.getSJCC());
            map.put("reagentNum", Integer.valueOf(syjzbVo.getSJSL()));
            map.put("trialTime", syjzbVo.getSYRQ());
            map.put("trialResult", Integer.valueOf(syjzbVo.getPDJG()));
            if (null == syjzbVo.getCZRY()){
                map.put("createId","system");
            } else {
                map.put("createId", syjzbVo.getCZRY());
            }

            List tlist = new ArrayList();
            tlist.add(map);
            BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
            String openApi = baseHttpRequest.requestDataProcessing(tlist, "initTrial", 0);
            baseHttpRequest.resultProcessing(openApi);
            JSONObject jsonObject2 = new JSONObject(openApi);
            Boolean success = (Boolean) jsonObject2.get("success");
            if (success) {
                log.info(String.format("抗折抗压实验数据推送成功!" + syjzbVo.getId()));
            } else {
                log.info(String.format("抗折抗压实验数据推送失败!" + syjzbVo.getId()));
            }
            sysConfigService.updateSysConfig(JobUtil.HUN_NINGTCOMPRESSION, syjzbVo.getId());
        }
    }
}
