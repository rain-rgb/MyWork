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
 * 实验室钢筋试验
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RebarTrialJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService itSyjzbService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HUN_REBARTRIAL);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到钢筋试验推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        if (null == selectsysconfigone.getExtra()) {
            log.info(String.format("没有配置要传输钢筋试验推送数据的设备" + DateUtils.now()));
            return;
        }
        JSONObject jsonObject = JSONUtil.parseObj(selectsysconfigone.getExtra());
        String shebeilist = jsonObject.getStr("shebeilist");//查询所有的设备
//        String shebeilist = "'jngywnj01'";
        List<SyjzbVo> list = itSyjzbService.queryLists("100047", shebeilist, selectsysconfigone.getCurid());//查询所有需要传输的数据
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无钢筋试验未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        List tlist = new ArrayList();
        for (SyjzbVo syjzbVo : list) {
            if (id < syjzbVo.getId()){
                id = syjzbVo.getId();
            }
            Map map = new HashMap<>();
            if (syjzbVo.getSYJID() != null){
                map.put("trialId", syjzbVo.getSYJID());
            }else {
                map.put("trialId", "--");
            }
            if (syjzbVo.getSysOrgCode() != null){
                map.put("labId", syjzbVo.getSysOrgCode());
            }else {
                map.put("labId", "--");
            }
            if(syjzbVo.getSBBH().equals("jngywnj01")){//工业北路
                map.put("companyId", 104387);
                map.put("projectId", 42290);
            }else {//杭甬
                map.put("companyId", 91093);
                map.put("projectId", 26740);
            }
//            工业北路：companyId: 104387  projectId: 42290
//            宁淮铁路：companyId: 104287  projectId: 42252
            if (syjzbVo.getSJQD() != null){
                map.put("designStrength", syjzbVo.getSJQD());
            }else {
                map.put("designStrength", "--");
            }
            if (syjzbVo.getSbname() != null){
                map.put("deviceName", syjzbVo.getSbname());
            }else {
                map.put("deviceName", "--");
            }
            if (syjzbVo.getSYLX() != null){
                map.put("trialType", syjzbVo.getSYLX());
            }else {
                map.put("trialType", "--");
            }
            if (null == syjzbVo.getGCMC() && null != syjzbVo.getCJMC()) {
                map.put("engineeringName", syjzbVo.getCJMC());
            } else if (null != syjzbVo.getGCMC() && null == syjzbVo.getCJMC()) {
                map.put("engineeringName", syjzbVo.getGCMC());
            } else if (null != syjzbVo.getGCMC() && null != syjzbVo.getCJMC()) {
                map.put("engineeringName", syjzbVo.getGCMC());
            } else {
                map.put("engineeringName", "--");
            }
            if (syjzbVo.getSJBH() != null){
                map.put("reagentNo", syjzbVo.getSJBH());
            }else {
                map.put("reagentNo", "--");
            }
            if (syjzbVo.getLQ() != null){
                map.put("age", syjzbVo.getLQ());
            }else {
                map.put("age", 0);
            }
            if (syjzbVo.getPZBM() != null){
                map.put("designValue", syjzbVo.getPZBM());
            }else {
                map.put("designValue", "--");
            }
            if (syjzbVo.getGCZJ() != null){
                map.put("reagentSize", syjzbVo.getGCZJ());
            }else {
                map.put("reagentSize", "--");
            }
            if (syjzbVo.getSJSL() != null){
                map.put("reagentNum", Integer.valueOf(syjzbVo.getSJSL()));
            }else {
                map.put("reagentNum", 0);
            }
            if (syjzbVo.getSYRQ() != null){
                map.put("trialTime", syjzbVo.getSYRQ());
            }else {
                map.put("trialTime", "--");
            }
            if (syjzbVo.getPDJG() != null && syjzbVo.getPDJG().equals("合格")){
                map.put("trialResult", 0);
            }else if (syjzbVo.getPDJG() == null || syjzbVo.getPDJG().equals("1")){
                map.put("trialResult", 0);
            }else {
                map.put("trialResult", syjzbVo.getPDJG());
            }
            if (null == syjzbVo.getCZRY()){
                map.put("createId","system");
            } else {
                map.put("createId", syjzbVo.getCZRY());
            }
            tlist.add(map);
        }
            BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
            String openApi = baseHttpRequest.requestDataProcessing(tlist, "initTrial", 0);
            JSONObject jsonObject2 = new JSONObject(openApi);
            Boolean success = (Boolean) jsonObject2.get("success");
            if (success) {
                log.info(String.format("钢筋试验数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.HUN_REBARTRIAL, id);
            } else {
                log.info(String.format("钢筋试验数据推送失败!" + id));
            }
    }
}
