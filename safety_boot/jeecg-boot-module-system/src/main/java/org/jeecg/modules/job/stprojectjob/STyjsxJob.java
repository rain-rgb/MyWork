package org.jeecg.modules.job.stprojectjob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class STyjsxJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJ_SX);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台压浆定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> yajiangMList = yajiangMService.selectBySBList(shebeilist, curid);
        if (null == yajiangMList || yajiangMList.size() == 0) {
            log.info(String.format("暂无苏台压浆的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrYajiangM yajiangM : yajiangMList) {
            if (yajiangM.getLianghao().contains("二期")) {
                id = yajiangM.getId();
                yajiangM.setYjsbbh("stgseqtjybyj01");//苏台高速二期土建一标一号压浆机
                boolean b = yajiangMService.updateById(yajiangM);
                if (b) {
                    sysConfigService.updateSysConfig(JobUtil.YJ_SX, id);//根据传过来的唯一值来修改当前判断到的数据id
                    log.info("苏台高速二期土建一标一号压浆机数据保存成功!!!");
                } else {
                    log.info("苏台高速二期土建一标一号压浆机数据保存失败...");
                }
            } else {
                id = yajiangM.getId();
                sysConfigService.updateSysConfig(JobUtil.YJ_SX, id);//根据传过来的唯一值来修改当前判断到的数据id
                log.info("未筛选到需要的压浆机数据...");
            }
        }
    }
}
