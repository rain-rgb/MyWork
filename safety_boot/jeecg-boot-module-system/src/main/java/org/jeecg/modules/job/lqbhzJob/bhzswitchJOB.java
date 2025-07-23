package org.jeecg.modules.job.lqbhzJob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.switchmachinePhone.entity.SwitchmachinePhone;
import com.trtm.iot.switchmachinePhone.service.ISwitchmachinePhoneService;
import com.trtm.iot.switchmachineStatistics.entity.SwitchingmachineStatistics;
import com.trtm.iot.switchmachineStatistics.service.ISwitchingmachineStatisticsService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bhzswitchJOB implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysMessageService sysMessageService;
    @Autowired
    private ISwitchingmachineStatisticsService switchingmachineStatisticsService;
    @Autowired
    private ISwitchmachinePhoneService switchmachinePhoneService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_KAIGUANJI);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到拌合站开关机定时任务配置信息" + DateUtils.now());
            return;
        }
        // 查询状态为 开机 的设备
        List<SwitchingmachineStatistics> statistics = switchingmachineStatisticsService.getlists();
        if (0 == statistics.size()) {
            log.info("暂无开关机判断的拌合站设备" + DateUtils.now());
            return;
        }
        for (SwitchingmachineStatistics statistics1 : statistics) {
            String shebeibianhao = statistics1.getShebeiNo();
            // 查询单个拌合站 预警号码配置表
            List<SwitchmachinePhone> switchmachinePhone = switchmachinePhoneService.getons(shebeibianhao);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
            int iscall = 0;
            String phones = "";
            String phonenum = "";
            if (switchmachinePhone.size()>0) {
                iscall = switchmachinePhone.get(0).getIsCall();
                phones = switchmachinePhone.get(0).getYujingPhones();
            }
            if (StrUtil.isNotBlank(phones)) {
                BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(phones);
                if (null != bhzPhone) {
                    phonenum = bhzPhone.getPhones();
                }
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String chuliaoshijian1 = statistics1.getChuliaoshijian();
            Date date = format.parse(chuliaoshijian1);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, 10);
            date = cal.getTime();
            String format1 = format.format(date);
            Date times = format.parse(format1);//开关机的出料时间加间隔时间（10分钟）
            Date sysdate = DateUtil.date();//系统时间
            JSONObject obj = new JSONObject();
            obj.put("sbname", selectshebeione.getSbname());
            obj.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            obj.put("content", "该设备停止生产,请关注现场收尾工作！");
            if (sysdate.after(times)) {///after  两个时间相比较 前者大于后者 ==true
                statistics1.setStatus(2); // 10分钟内没有生产数据 则表示 该设备以停止工作
                statistics1.setChuliaoshijian(format.format(new Date()));
                statistics1.setJieshushijian(format.format(new Date()));
                switchingmachineStatisticsService.updateById(statistics1);
                if (!"".equals(phonenum)) {
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setEsTitle("拌合站开关机提醒");
                    sysMessage.setEsType("1");
                    sysMessage.setRemark(shebeibianhao);
                    sysMessage.setEsReceiver(phonenum);
                    sysMessage.setEsSendNum(0);
                    sysMessage.setEsContent(obj.toString());
                    if (iscall == 0) {
                        sysMessage.setEsSendStatus("0");//推送状态0未推送
                    } else {
                        sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage.setEsResult("不需要推送");
                    }
                    sysMessage.setCreateTime(new Date());
                    sysMessageService.save(sysMessage);
                }
            }
        }
    }
}
