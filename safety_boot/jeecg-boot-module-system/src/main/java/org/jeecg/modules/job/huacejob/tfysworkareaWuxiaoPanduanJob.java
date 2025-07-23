package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.trtm.iot.hc_pavement_alarm.service.IHcPavementAlarmService;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_tfstationdetail.entity.HcTfstationdetail;
import com.trtm.iot.hc_tfstationdetail.service.IHcTfstationdetailService;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkarea;
import com.trtm.iot.hc_tfysworkarea.service.IHcTfysworkareaService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import com.trtm.iot.hctfysworkareaconfiguration.entity.HcTfysworkareaConfiguration;
import com.trtm.iot.hctfysworkareaconfiguration.service.IHcTfysworkareaConfigurationService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @ClassName tfysworkareaJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/9/27 16:38
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class tfysworkareaWuxiaoPanduanJob implements Job {
    @Autowired
    private IHcTfysworkareaService tfysworkareaService;
    @Autowired
    private IHcTfstationdetailService tfstationdetailService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LambdaQueryWrapper<HcTfysworkarea> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HcTfysworkarea::getWorkstat, "0");
        // 获取当前时间的毫秒值
        long currentTimeMillis = System.currentTimeMillis();

        // 一天的毫秒值（24小时 * 60分钟 * 60秒 * 1000毫秒）
        long oneDayInMillis = 24 * 60 * 60 * 1000L;

        // 计算当前时间减去一天的时间
        long oneDayBeforeMillis = currentTimeMillis - oneDayInMillis;

        // 将毫秒值转换为 Date 对象
        Date oneDayBefore = new Date(oneDayBeforeMillis);

        // 添加条件：starttime小于当前时间一天
        wrapper.lt(HcTfysworkarea::getStarttime, oneDayBefore);
        tfysworkareaService.remove(wrapper);
    }

}
