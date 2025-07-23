package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.suchingcarpeiz.entity.SuchingCarpeiz;
import com.trtm.iot.suchingcarpeiz.service.ISuchingCarpeizService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 审核超两小时预警
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class fachedJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISchedulingCarService schedulingCarService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private ISuchingCarpeizService suchingCarpeizService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.FCD_CSYJ);//发车单超时预警=304
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到砼拌合站定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        int id = 0;
        QueryWrapper<SchedulingCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("id",curid);
        queryWrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(create_at) = 0");//查询当天的数据
        List<SchedulingCar> list = schedulingCarService.list(queryWrapper);
        if (list.size() > 0){
            for (SchedulingCar l :list){
                if (l.getDattim() != null && l.getQianshoutime() != null){
                    QueryWrapper<ShebeiInfo> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.likeRight("sys_org_code",l.getSysOrgCode());
                    queryWrapper1.eq("sbname",l.getVehicle());
                    ShebeiInfo one = shebeiInfoService.getOne(queryWrapper1);
                    if (one != null){
                        QueryWrapper<SuchingCarpeiz> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.eq("imei",one.getSbjno());
                        SuchingCarpeiz one1 = suchingCarpeizService.getOne(queryWrapper2);
                        if(one1 != null){
                            if (one1.getIsCall() == 0){
                                //计算时间是否超过三小时
                                Date date = new Date();
                                long seconds = 0;
                                int j = 0;
                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format2 = dateFormat.format(date);
                                if (l.getQianshoutime()!=null){
                                    String format = dateFormat.format(l.getDattim());
                                    String format1 = dateFormat.format(l.getQianshoutime());
                                    // 解析起始时间字符串
                                    LocalDateTime startTime = LocalDateTime.parse(format);
                                    LocalDateTime endTime = LocalDateTime.parse(format1);
                                    Duration duration = Duration.between(startTime, endTime);
                                    seconds = duration.toHours();
                                    j = 1;
                                }else {
                                    String format = dateFormat.format(l.getDattim());
                                    // 解析起始时间字符串
                                    LocalDateTime startTime = LocalDateTime.parse(format);
                                    LocalDateTime endTime = LocalDateTime.parse(format2);
                                    Duration duration = Duration.between(startTime, endTime);
                                    seconds = duration.toHours();
                                    j = 2;
                                }
                                if (seconds > 3){
                                    if (j == 1){
                                        try {
                                            fsyjxx(one1,format2,one.getSbjno(),"签收超时预警，请注意时间。");
                                        } catch (ClientException e) {
                                            e.printStackTrace();
                                        }
                                    }else {
                                        try {
                                            fsyjxx(one1,format2,one.getSbjno(),"超时未签收，请及时签收。");
                                        } catch (ClientException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }else {
                            log.info("该设备没有配置预警信息！！！");
                        }
                    }
                }else {

                }
            }
        }
        sysConfigService.updateSysConfigs(JobUtil.FCD_CSYJ, id,0);//根据传过来的唯一值来修改当前判断到的数据id
    }

    // 处置人推送
    public void fsyjxx(SuchingCarpeiz one1,String product_datetime,String getSbjno,String overReason) throws ClientException {
        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(one1.getZhydPhones());
        JSONObject obj = new JSONObject();
        obj.put("sbname", getSbjno);
        obj.put("time", product_datetime);
        obj.put("content", overReason);
        SysMessage sysMessage3 = new SysMessage();
        sysMessage3.setEsTitle(getSbjno+"签收超时预警");
        sysMessage3.setEsType("1");//短信类型  1短信
        sysMessage3.setEsReceiver(bhzPhone.getPhones());//手机号
        sysMessage3.setEsContent(obj.toString());//短信内容
        sysMessage3.setEsSendStatus("0");//推送状态0未推送
        sysMessage3.setEsSendNum(0);//推送总次数
        sysMessage3.setRemark(getSbjno);
        sysMessage3.setCreateTime(new Date());

        boolean b = DySmsHelper.sendSms(one1.getZhydPhones(), obj, DySmsEnum.SMS_CB_CODE);
        if (b){
            sysMessage3.setEsSendNum(1);
            log.info("拌合站超时预警短信发送成功"+obj);
        }else {
            sysMessage3.setEsSendNum(2);
            log.info("拌合站超时预警短信发送失败"+obj);
        }
        sysMessageService.save(sysMessage3);
    }
}
