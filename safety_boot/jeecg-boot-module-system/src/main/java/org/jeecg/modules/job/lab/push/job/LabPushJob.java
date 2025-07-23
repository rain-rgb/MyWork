package org.jeecg.modules.job.lab.push.job;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.syj.entity.SyjzbVo;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.*;

import org.apache.commons.httpclient.methods.PostMethod;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.utils.PMUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 /**
 * 实验室初始化（实验室基本数据推送 品茗）
 *
 * @author lis1
 * @date 2022/12/9
 * @time 9:52
  */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LabPushJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService itSyjzbService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HUN_NINGT);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到混凝土试验推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        if (null == selectsysconfigone.getExtra()) {
            log.info(String.format("没有配置要传输混凝土抗压试验推送数据的设备" + DateUtils.now()));
            return;
        }
        JSONObject jsonObject = JSONUtil.parseObj(selectsysconfigone.getExtra());
        String shebeilist = jsonObject.getStr("shebeilist");//查询所有的设备
        List<SyjzbVo> list = itSyjzbService.queryLists("100014",shebeilist, selectsysconfigone.getCurid());//查询所有需要传输的数据
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无混凝土抗压试验未推送数据" + DateUtils.now()));
            return;
        }

        for (SyjzbVo tSyjzb : list) {
            Map map = new HashMap<>();
            map.put("trialId", tSyjzb.getSYJID());
            map.put("labId", tSyjzb.getSysOrgCode());
            map.put("designStrength", tSyjzb.getSJQD());
            map.put("deviceName", tSyjzb.getSbname());
            map.put("trialType", tSyjzb.getSYLX());
            if (null == tSyjzb.getGCMC() && null != tSyjzb.getCJMC()) {
                map.put("engineeringName", tSyjzb.getCJMC());
            } else if (null != tSyjzb.getGCMC() && null == tSyjzb.getCJMC()) {
                map.put("engineeringName", tSyjzb.getGCMC());
            } else if (null != tSyjzb.getGCMC() && null != tSyjzb.getCJMC()) {
                map.put("engineeringName", tSyjzb.getGCMC());
            } else {
                map.put("engineeringName", null);
            }
            if (null == tSyjzb.getSJBH()){
                map.put("reagentNo", tSyjzb.getSBBH()+"_"+tSyjzb.getId().toString());
            } else {
                map.put("reagentNo", tSyjzb.getSJBH());
            }
            map.put("age", tSyjzb.getLQ());
            map.put("designValue", tSyjzb.getQDDBZ());
            map.put("reagentSize", tSyjzb.getSJCC());
            map.put("reagentNum", Integer.valueOf(tSyjzb.getSJSL()));
            map.put("trialTime", tSyjzb.getSYRQ());
            map.put("trialResult", Integer.valueOf(tSyjzb.getPDJG()));
            if (null == tSyjzb.getCZRY()){
                map.put("createId","system");
            } else {
                map.put("createId", tSyjzb.getCZRY());
            }
            List tlist = new ArrayList();
            tlist.add(map);
            BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
            String openApi = baseHttpRequest.requestDataProcessing(tlist, "initTrial", 0);
            JSONObject jsonObject2 = new JSONObject(openApi);
            Boolean success = (Boolean) jsonObject2.get("success");
            if (success) {
                log.info(String.format("混凝土抗压试验数据推送成功!" + tSyjzb.getId()));
            } else {
                log.info(String.format("混凝土抗压试验数据推失败!" + tSyjzb.getId()));
            }
            sysConfigService.updateSysConfig(JobUtil.HUN_NINGT, tSyjzb.getId());
        }
    }
}



