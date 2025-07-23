package org.jeecg.modules.job.sutaijob.bimduijie;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName bysJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/11/6 13:07
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bysJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBysRealService bysRealService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "http://122.226.22.70:8867/prod-api/iot/STEQ";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_BIMBYS);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台标养室定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台标养室的设备" + DateUtils.now()));
            return;
        }else {
            log.info(String.format("苏台标养室开始推送！" + DateUtils.now()));
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BysReal> bysReals = bysRealService.queryListbim(shebeilist, curid);
        if (null == bysReals || bysReals.size() == 0) {
            log.info(String.format("暂无苏台标养室未推送数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        for (BysReal bysReal : bysReals) {
            id = bysReal.getId();
            JSONObject sendBysDate = new JSONObject();
            sendBysDate.set("gatherDate", sdf.format(bysReal.getGatherdate()));
            sendBysDate.set("shebeiNo", bysReal.getShebeino());
            sendBysDate.set("temperature", bysReal.getTemperature());
            sendBysDate.set("humidity", bysReal.getHumidity());
            sendBysDate.set("temstatus", bysReal.getTemstatus());
            sendBysDate.set("humstatus", bysReal.getHumstatus());

            JSONObject sendDate = new JSONObject();
            sendDate.set("iotType","标养室");
            sendDate.set("externalId",String.valueOf(id));
            sendDate.set("jsonData",sendBysDate);
            sendDate.set("deviceCode",bysReal.getShebeino());
            sendDate.set("createTime",sdf.format(bysReal.getGatherdate()));
            sendDate.set("warning",bysReal.getAlertstate());

            String result = HttpRequest.post(url)
                    .body(String.valueOf(sendDate))
                    .timeout(20000)
                    .execute()
                    .body();
            if (result.contains("200")){
                log.info("苏台标养室推送bim成功！");
            }else {
                log.info("苏台标养室推送bim失败！");
            }
            sysConfigService.updateSysConfig(JobUtil.ST_BIMBYS,id);
        }
    }
}
