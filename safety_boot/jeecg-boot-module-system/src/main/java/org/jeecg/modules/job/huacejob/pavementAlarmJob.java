package org.jeecg.modules.job.huacejob;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.trtm.iot.hc_pavement_alarm.service.IHcPavementAlarmService;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_result_collect.entity.HcResultCollect;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.system.message.service.impl.SysMessageCoreServiceImpl;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * @ClassName pavementAlarmJob：
 * @Description 摊铺碾压预警
 * @Author 55314
 * @Date 2023/4/26 9:26
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class pavementAlarmJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/pavement/alarm.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcPavementAlarmService pavementAlarmService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;
    @Autowired
    ISysConfigService sysConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HC_TPNY_YUJING);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到获取华测摊铺碾压设备的定时任务配置信息" + DateUtils.now());
            return;
        }
        List<HcSection> sectionList = sectionService.list();
        for (HcSection hcSection : sectionList) {
            String pjid = hcSection.getPjid();
            String sectionid = hcSection.getSectionid();

            QueryWrapper<HcToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.like("projectid", pjid);
            HcToken hcToken = tokenService.getOne(tokenQueryWrapper);
            String token = hcToken.getToken();

            QueryWrapper<HcPavementAlarm> pavementAlarmQueryWrapper = new QueryWrapper<>();
            pavementAlarmQueryWrapper.select("max(dataTime) as dataTime");
            pavementAlarmQueryWrapper.eq("projectId", pjid);
            pavementAlarmQueryWrapper.eq("sectionid", sectionid);
            HcPavementAlarm one = pavementAlarmService.getOne(pavementAlarmQueryWrapper);
            Date endtime = null;
            if (null != one) {
                Instant instant = one.getDatatime().toInstant(); // 将Date对象转换为Instant对象
                instant = instant.plus(Duration.ofSeconds(1)); // 将时间加上一秒钟
                endtime = Date.from(instant); // 将Instant转换为Date对象
            }

            JSONObject sendDate = new JSONObject();

            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);
            sendDate.put("startTime", endtime);

            String result = HttpRequest.post(url)
                    .header("token", token)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            JSONObject jsonObject = JSONObject.parseObject(result);
            Integer code = jsonObject.getInteger("code");
            if (0 != code) {
                continue;
            }
            JSONArray records = jsonObject.getJSONObject("data").getJSONArray("records");
            for (int i = 0; i < records.size(); i++) {
                JSONObject record = records.getJSONObject(i);
                HcPavementAlarm hcPavementAlarm = jsonObject.toJavaObject(record, HcPavementAlarm.class);
                Integer id = hcPavementAlarm.getId();
                HcPavementAlarm byId = pavementAlarmService.getById(id);
                if (null != byId) {
                    continue;
                }
                hcPavementAlarm.setProjectid(pjid);
                hcPavementAlarm.setSectionid(sectionid);
                hcPavementAlarm.setType(1);
                pavementAlarmService.save(hcPavementAlarm);
                log.info("摊铺碾压预警数据保存成功！");

                HcMachine hcMachine = pavementAlarmService.getHcMachine(hcPavementAlarm.getMachineid());
                if (ObjectUtil.isNotEmpty(hcMachine)) {
                    if (hcMachine.getIscall() == 0 && ObjectUtil.isNotEmpty(hcMachine.getPhone())) {
                        SysMessageCore sysSms = new SysMessageCore();
                        sysSms.setEsTitle("路面摊铺碾压预警");
                        sysSms.setEsType("1");
                        sysSms.setEsReceiver(hcMachine.getPhone());
                        JSONObject obj = new JSONObject();
                        obj.put("sbname", hcMachine.getMachinename());
                        obj.put("time", new Date());
                        obj.put("content", hcPavementAlarm.getAlarminfo());
                        sysSms.setEsContent(obj.toString());
                        sysSms.setEsSendStatus("0");
                        sysSms.setEsSendNum(0);
                        sysSms.setRemark(hcPavementAlarm.getMachineid());
                        sysMessageCoreService.save(sysSms);
                        log.info("摊铺碾压短信预警数据添加成功");
                    }
                }
            }
        }
    }
}
