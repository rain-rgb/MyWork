package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzSupervisionOrder.entity.BhzSupervisionOrder;
import com.trtm.iot.bhzSupervisionOrder.service.IBhzSupervisionOrderService;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 审核超两小时预警
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bhzChaoShiJobRC implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzSupervisionOrderService bhzSupervisionOrderService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysAnnouncementSendService sysAnnouncementSendService;
    @Autowired
    private ISysAnnouncementService sysAnnouncementService;
    @Autowired
    private ISysUserService sysUserService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_CSYJ);//拌合站超时预警=204
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到砼拌合站定时任务的配置信息" + DateUtils.now());
            return;
        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formats = dateFormat.format(date);
        String formats1 = dateFormat1.format(date);
        Date parse = null;
        try {
            parse = dateFormat1.parse(formats1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = formats + "%";
        int id = 0;
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selectBhzChao2(0, format, 0, 1);
        if (selecthntbhzones.size() > 0) {
            for (BhzCementBase selecthntbhzone : selecthntbhzones) {
                id = selecthntbhzone.getId();
                Date collectDatetime = selecthntbhzone.getCollectDatetime();
                assert parse != null;
                long l = parse.getTime() - collectDatetime.getTime();
                String shebeiNo = selecthntbhzone.getShebeiNo();
                BhzCallCfg selectbhzcallone = bhzCallCfgService.selectbhzcallone(shebeiNo);// 查询配置表
                if (selectbhzcallone != null) {
                    if (selectbhzcallone.getCsyj() != null && selectbhzcallone.getCsyj() == 0) {
                        if (selectbhzcallone.getCssj() != null && selectbhzcallone.getCssjdw() != null) {
                            Integer cssj = selectbhzcallone.getCssj();//超时时长
                            Integer cssjdw = selectbhzcallone.getCssjdw();//超时单位0分钟1小时
                            long i = 0;
                            if (cssjdw == 0) {
                                i = cssj * 60 * 1000;
                            } else {
                                i = cssj * 60 * 60 * 1000;
                            }
                            if (l > i) {
                                try {
                                    cssysmessage(selecthntbhzone, selectbhzcallone);
                                } catch (ClientException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    log.info("该设备未进行拌合站配置!" + DateUtils.now());
                }
            }
        } else {
            log.info("未获取到砼拌合站超时为审核数据!" + DateUtils.now());
        }
        sysConfigService.updateSysConfigs(JobUtil.HNTBHZ_CFG_NEW, id, 0);//根据传过来的唯一值来修改当前判断到的数据id
    }

    public void cssysmessage(BhzCementBase selecthntbhzone, BhzCallCfg selectbhzcallone) throws ClientException, ParseException {
        if (selectbhzcallone.getCzperson() != null) {
            //发送未闭合短信
            fsyjxx(selectbhzcallone, selecthntbhzone);
            //生成监理指令单
            Integer scjlzld = scjlzld(selecthntbhzone, selectbhzcallone);
            //存入数据到sys_announcement表和sys_announcement_send表
            savedata(selecthntbhzone, selectbhzcallone,scjlzld);
        }

    }

    //存入数据到sys_announcement表和sys_announcement_send表
    private void savedata(BhzCementBase selecthntbhzone, BhzCallCfg selectbhzcallone, Integer scjlzld) throws ParseException {
        String busId = "{id:"+scjlzld+"}";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(selecthntbhzone.getProductDatetime());
        Date date = format.parse(format1);
        SysAnnouncement sysAnnouncement = new SysAnnouncement();
        sysAnnouncement.setTitile("物联网指令单");
        sysAnnouncement.setMsgContent(selecthntbhzone.getOverReason());
        sysAnnouncement.setStartTime(date);
        if (selecthntbhzone.getOverLevel() == 2) {
            sysAnnouncement.setPriority("M");
        } else if (selecthntbhzone.getOverLevel() == 3) {
            sysAnnouncement.setPriority("H");
        }
        sysAnnouncement.setMsgType("USER");
        sysAnnouncement.setBusId(busId);
        sysAnnouncement.setSendStatus("1");
        sysAnnouncement.setOpenType("component");
        sysAnnouncement.setOpenPage("bhz/bhzSupervisionOrder/modules/InstructModelLone");
        sysAnnouncement.setOrderNo(selecthntbhzone.getBatchNo());
        boolean save = sysAnnouncementService.save(sysAnnouncement);
        if (save) {
            QueryWrapper<SysAnnouncement> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("titile", "物联网指令单");
            queryWrapper.eq("msg_content", selecthntbhzone.getOverReason());
            queryWrapper.eq("start_time", date);
            if (selecthntbhzone.getOverLevel() == 2) {
                queryWrapper.eq("priority", "M");
            } else if (selecthntbhzone.getOverLevel() == 3) {
                queryWrapper.eq("priority", "H");
            }
            SysAnnouncement one = sysAnnouncementService.getOne(queryWrapper);
            if (one != null) {
                SysAnnouncementSend send = new SysAnnouncementSend();
                send.setAnntId(one.getId());
                SysUser user = sysUserService.getUserByName(selectbhzcallone.getShperson());
                if (user != null){
                    send.setUserId(user.getId());
                }
                send.setReadFlag("0");
                sysAnnouncementSendService.save(send);
            }
        }

        SysAnnouncementSend send = new SysAnnouncementSend();

    }

    //生成监理指令单
    private Integer scjlzld(BhzCementBase selecthntbhzone, BhzCallCfg selectbhzcallone) {
        String bhjno = selecthntbhzone.getShebeiNo();
        ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(bhjno);
//        SysDepart depart = sysDepartService.selectbyorgcode(shebeiInfo.getSysOrgCode(), 7);
        SysDepart depart = sysDepartService.selectByCode(shebeiInfo.getSysOrgCode());
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String format = dateFormat.format(date);
        String Num = "ZLD-" + depart.getDepartName() + "-" + format + "-";
        Integer count = bhzSupervisionOrderService.selectCountByNo(Num + "%") + 1;
        Format fm = new DecimalFormat("000");
        Num = Num + fm.format(count);
        Calendar cale = Calendar.getInstance();
        Date nowTime = cale.getTime();
        cale.setTime(selecthntbhzone.getProductDatetime());
        if (selectbhzcallone.getCssjdw() == 0) {
            cale.add(Calendar.MINUTE, selectbhzcallone.getCssj());
        } else if (selectbhzcallone.getCssjdw() == 1) {
            cale.add(Calendar.HOUR, selectbhzcallone.getCssj());
        }
        Date time = cale.getTime();
        BhzSupervisionOrder bhzSupervisionOrder = new BhzSupervisionOrder();
        bhzSupervisionOrder.setBhzNo(selecthntbhzone.getBatchNo());
        bhzSupervisionOrder.setBatchNo(Num);
        bhzSupervisionOrder.setOverReason(selecthntbhzone.getOverReason());
        bhzSupervisionOrder.setReceiver(selectbhzcallone.getCzperson());
        bhzSupervisionOrder.setProductDatetime(selecthntbhzone.getProductDatetime());
        bhzSupervisionOrder.setShebeiNo(bhjno);
        bhzSupervisionOrder.setExpectedDatetime(time);
        bhzSupervisionOrder.setSaveDatetime(nowTime);
        bhzSupervisionOrder.setDepartName(depart.getDepartName());
        bhzSupervisionOrder.setProjectName(depart.getDepartNameAbbr());
        bhzSupervisionOrder.setConstructionUnit(depart.getConstructionUnit());
        bhzSupervisionOrder.setSupervisionUnit(depart.getSupervisionUnit());
        boolean save = bhzSupervisionOrderService.save(bhzSupervisionOrder);
        if (save) {
            selecthntbhzone.setIsorder(1);
            bhzCementBaseService.updateById(selecthntbhzone);
        }
        QueryWrapper<BhzSupervisionOrder> queryWrapper=  new QueryWrapper<>();
        queryWrapper.eq("batch_no",Num);
        BhzSupervisionOrder one = bhzSupervisionOrderService.getOne(queryWrapper);
        return one.getId();

    }

    public String userphone(String people) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", people);
        SysUser one = userService.getOne(queryWrapper);
        if (one != null) {
            if (one.getPhone() != null) {
                return one.getPhone();
            } else {
                log.info("未配置手机号");
                return null;
            }
        } else {
            log.info("查询不到用户名");
            return null;
        }
    }

    // 处置人推送
    public void fsyjxx(BhzCallCfg selectbhzcallone, BhzCementBase selecthntbhzone) throws ClientException {
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(selectbhzcallone.getBhjno());
        if (selectbhzcallone.getCzperson() != null) {
            JSONObject obj = new JSONObject();
            if (selectbhzcallone.getBhjno() == null) {
                obj.put("sbname", "平台未注册设备");
            } else {
                obj.put("sbname", selectshebeione.getSbname());
            }
            obj.put("time", DateUtil.format(selecthntbhzone.getProductDatetime(), "yyyy-MM-dd HH:mm:ss"));
            obj.put("content", selecthntbhzone.getOverReason() + "超时未闭合，请查看监理指令单");
            SysMessage sysMessage3 = new SysMessage();
            String userphone = userphone(selectbhzcallone.getCzperson());
            if (selectbhzcallone.getCsyj() == 0) {
                sysMessage3.setEsTitle("砼拌合站超时未闭合");
                sysMessage3.setEsType("1");//短信类型  1短信
                sysMessage3.setEsReceiver(userphone);//手机号
                sysMessage3.setEsContent(obj.toString());//短信内容
                sysMessage3.setEsSendStatus("0");//推送状态0未推送
                sysMessage3.setEsSendNum(0);//推送总次数
                sysMessage3.setRemark(selectbhzcallone.getBhjno());
                sysMessage3.setCreateTime(new Date());
            } else {
                sysMessage3.setEsTitle("砼拌合站超时未闭合");
                sysMessage3.setEsType("1");
                sysMessage3.setEsReceiver(userphone);
                sysMessage3.setEsContent(obj.toString());
                sysMessage3.setEsSendStatus("-1");//推送状态-1 不需要推送
                sysMessage3.setEsSendNum(0);//推送总次数
                sysMessage3.setRemark(selectbhzcallone.getBhjno());
                sysMessage3.setEsResult("不需要推送");
                sysMessage3.setCreateTime(new Date());
            }

            boolean b = DySmsHelper.sendSms(userphone, obj, DySmsEnum.SMS_CB_CODE);
            if (b) {
                sysMessage3.setEsSendNum(1);
                log.info("拌合站超时预警短信发送成功" + obj);
            } else {
                sysMessage3.setEsSendNum(2);
                log.info("拌合站超时预警短信发送失败" + obj);
            }
            sysMessageService.save(sysMessage3);
        }
    }
}
