package org.jeecg.modules.job.stprojectjob;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.trtm.iot.hc_pavement_alarm.service.IHcPavementAlarmService;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkarea;
import com.trtm.iot.hc_tfysworkarea.service.IHcTfysworkareaService;
import com.trtm.iot.hctfysworkareaconfiguration.entity.HcTfysworkareaConfiguration;
import com.trtm.iot.hctfysworkareaconfiguration.service.IHcTfysworkareaConfigurationService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
public class STtufangysJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IHcTfysworkareaService hcTfysworkareaService;
    @Autowired
    private IHcTfysworkareaConfigurationService hcTfysworkareaConfigurationService;
    @Autowired
    private IHcPavementAlarmService hcPavementAlarmService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcSectionService hcSectionService;
    @Autowired
    private IHcProjectService hcProjectService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务阈值表获取一条阈值信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_TFYS);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到土方压实定时任务的阈值信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        QueryWrapper<HcTfysworkarea> queryWrapper = new QueryWrapper<>();
//        queryWrapper.gt("id",curid);
        queryWrapper.eq("workstat", "1");
        queryWrapper.eq("standard", "0");
        queryWrapper.gt("thickavg", "30");
//        queryWrapper.last("limit 100");
        List<HcTfysworkarea> list = hcTfysworkareaService.list(queryWrapper);
        int id = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size() > 0) {
            for (HcTfysworkarea l : list) {
                id = l.getId();
                int vibrateratio = 0;
                int timesavg = 0;
                int thickavg = 0;

                // 超标预警配置表
                QueryWrapper<HcTfysworkareaConfiguration> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("projectid", l.getProjectid());
                queryWrapper1.eq("sectionid", l.getSectionid());
                HcTfysworkareaConfiguration one = hcTfysworkareaConfigurationService.getOne(queryWrapper1);

                QueryWrapper<HcSection> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("sectionId", l.getSectionid());
                HcSection one1 = hcSectionService.getOne(queryWrapper2);
                QueryWrapper<HcProject> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("pjId", l.getProjectid());
                HcProject one2 = hcProjectService.getOne(queryWrapper3);
                if (one != null) {
                    if (one.getVibrateratioMax() != null && one.getVibrateratioMin() != null && one.getVibrateratioZon() != null) {
                        double vibrateratio1 = Double.parseDouble(l.getVibrateratio());

                        double vibrateratio4 = Double.parseDouble(one.getVibrateratioMax());
                        double vibrateratio3 = Double.parseDouble(one.getVibrateratioZon());
                        double vibrateratio2 = Double.parseDouble(one.getVibrateratioMin());
                        if (vibrateratio1 >= vibrateratio4) {
                            vibrateratio = 3;
                        } else if (vibrateratio1 >= vibrateratio3) {
                            vibrateratio = 2;
                        } else if (vibrateratio1 >= vibrateratio2) {
                            vibrateratio = 1;
                        }
                    }
                    if (one.getTimesavgMax() != null && one.getTimesavgMin() != null) {
                        double vibrateratio1 = Double.parseDouble(l.getTimesavg());

                        double vibrateratio4 = Double.parseDouble(one.getTimesavgMax());
                        double vibrateratio3 = Double.parseDouble(one.getTimesavgZon());
                        double vibrateratio2 = Double.parseDouble(one.getTimesavgMin());
                        if (vibrateratio1 >= vibrateratio4) {
                            timesavg = 3;
                        } else if (vibrateratio1 >= vibrateratio3) {
                            timesavg = 2;
                        } else if (vibrateratio1 >= vibrateratio2) {
                            timesavg = 1;
                        }
                    }
                    if (one.getThickavgMax() != null && one.getThickavgMin() != null) {
                        double vibrateratio1 = Double.parseDouble(l.getThickavg());

                        double vibrateratio4 = Double.parseDouble(one.getThickavgMax());
                        double vibrateratio3 = Double.parseDouble(one.getThickavgZon());
                        double vibrateratio2 = Double.parseDouble(one.getThickavgMin());
                        if (vibrateratio1 >= vibrateratio4) {
                            thickavg = 3;
                        } else if (vibrateratio1 >= vibrateratio3) {
                            thickavg = 2;
                        } else if (vibrateratio1 >= vibrateratio2) {
                            thickavg = 1;
                        }
                    }

                    if (vibrateratio == 0 && timesavg == 0 && thickavg == 0) {
                        continue;
                    } else {
                        String yuanying = null;
                        if (vibrateratio == 3 || timesavg == 3 || thickavg == 3) {
                            l.setStandard("3");
                            yuanying = "高级超标：";
                        } else if (vibrateratio == 2 || timesavg == 2 || thickavg == 2) {
                            l.setStandard("2");
                            yuanying = "中级超标：";
                        } else {
                            l.setStandard("1");
                            yuanying = "初级超标：";
                        }
                        if (vibrateratio > 0) {
                            yuanying = "开振占比:" + l.getVibrateratio() + "超阈值范围。";
                        }
                        if (timesavg > 0) {
                            if (yuanying == null) {
                                yuanying = "平均碾压遍数:" + l.getTimesavg() + "超阈值范围。";
                            } else {
                                yuanying = yuanying + "平均碾压遍数:" + l.getTimesavg() + "超阈值范围。";
                            }
                        }
                        if (thickavg > 0) {
                            if (yuanying == null) {
                                yuanying = "平均厚度:" + l.getThickavg() + "超阈值范围。";
                            } else {
                                yuanying = yuanying + "平均厚度:" + l.getThickavg() + "超阈值范围。";
                            }
                        }
                        l.setReason(yuanying);
                        hcTfysworkareaService.updateById(l);
                        HcPavementAlarm hcPavementAlarm = new HcPavementAlarm();
                        hcPavementAlarm.setType(2);
                        hcPavementAlarm.setAlarminfo(yuanying);
                        hcPavementAlarm.setAlarmlevel(2);
                        hcPavementAlarm.setSncode(l.getBlockid());//关联
                        hcPavementAlarm.setProjectid(l.getProjectid());
                        hcPavementAlarm.setSectionid(l.getSectionid());
                        hcPavementAlarmService.saveOrUpdate(hcPavementAlarm);
                        String format = dateFormat.format(l.getEndtime());
                        String startstation1 = getStartstation(l.getStartstation());
                        String endstation1 = getEndstation(l.getEndstation());
                        String getSbjno = startstation1 + "-" + endstation1;
                        if (one.getStauts().equals("0")) {
                            try {
                                fsyjxx(one, one2.getPjname() + one1.getSectionname(), getSbjno, yuanying, dateFormat.format(l.getStarttime()) + "-" + dateFormat.format(l.getEndtime()).substring(11), l.getStandard(), l.getEndtime());
                            } catch (ClientException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    log.info("该项目标段未阈值预警参数！！！");
                }
            }
            sysConfigService.updateSysConfig(JobUtil.ST_TFYS, id);//根据传过来的唯一值来修改当前判断到的数据id
        } else {
            log.info("暂无未检测数据。。。");
        }
    }

//    // 处置人推送
//    public void fsyjxx(HcTfysworkareaConfiguration one1, String product_datetime, String getSbjno, String overReason) throws ClientException {
//        Date date = new Date();
//        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        QueryWrapper<HcProject> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("pjId",one1.getProjectid());
//        HcProject one = projectService.getOne(queryWrapper);
//        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(one1.getPhone());
//        com.alibaba.fastjson.JSONObject obj = new JSONObject();
//        obj.put("sbname", getSbjno);
//        obj.put("time", sdf.format(date));
//        obj.put("content", overReason);
//        SysMessage sysMessage3 = new SysMessage();
//        sysMessage3.setEsTitle(one.getPjname()+"超标预警");
//        sysMessage3.setEsType("1");//短信类型  1短信
//        sysMessage3.setEsReceiver(bhzPhone.getPhones());//手机号
//        sysMessage3.setEsContent(obj.toString());//短信内容
//        sysMessage3.setEsSendStatus("0");//推送状态0未推送
//        sysMessage3.setEsSendNum(0);//推送总次数
//        sysMessage3.setRemark(one.getPjname());
//        sysMessage3.setCreateTime(new Date());
//
//        sysMessageService.save(sysMessage3);
//    }

    // 处置人推送
    public void fsyjxx(HcTfysworkareaConfiguration one1, String name, String getSbjno, String overReason, String sgsj, String standard, Date getEndtime) throws ClientException {
        //超过三十五
        if (overReason.length() > 35) {
            overReason = overReason.substring(0, 32) + "...";
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<HcProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pjId", one1.getProjectid());
        HcProject one = projectService.getOne(queryWrapper);
        com.alibaba.fastjson.JSONObject obj = new JSONObject();
        obj.put("sbname", name);//标段名称
//        obj.put("time", sdf.format(date));//推送时间
        obj.put("time", sdf.format(getEndtime));//推送时间
        obj.put("startstation", getSbjno);//施工桩号
        obj.put("content", overReason);
        obj.put("interval", sgsj);//施工时间
        SysMessage sysMessage3 = new SysMessage();
        sysMessage3.setEsTitle(one.getPjname() + "超标预警");
        sysMessage3.setEsType("1");//短信类型  1短信
        BhzPhone bhzPhone = new BhzPhone();
        if (standard.equals("1")) {
            bhzPhone = bhzPhoneService.selectBhzPhone(one1.getPrimaryPhones());
        } else if (standard.equals("2")) {
            bhzPhone = bhzPhoneService.selectBhzPhone(one1.getMiddlePhones());
        } else {
            bhzPhone = bhzPhoneService.selectBhzPhone(one1.getAdvancedPhones());
        }
        sysMessage3.setEsReceiver(bhzPhone.getPhones());//手机号
        sysMessage3.setEsContent(obj.toString());//短信内容
        sysMessage3.setEsSendStatus("0");//推送状态0未推送
        sysMessage3.setEsSendNum(0);//推送总次数
        sysMessage3.setRemark(one.getPjname());
        sysMessage3.setCreateTime(new Date());
        boolean b = false;
        Long days = null;
        try {
            Date currentTime = sdf.parse(sdf.format(new Date()));//现在系统当前时间
            long diff = currentTime.getTime() - getEndtime.getTime();
            days = diff / (1000 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (days > 30) {//如果大于30分钟，不推送
            b = true;
        } else {
            b = DySmsHelper.sendSms(bhzPhone.getPhones(), obj, DySmsEnum.SMS_CB_TFYS);
        }
        if (b) {
            sysMessage3.setEsSendStatus("1");//推送状态0未推送
            sysMessage3.setEsSendNum(1);
            log.info("拌合站超时预警短信发送成功" + obj);
        } else {
            sysMessage3.setEsSendStatus("1");//推送状态0未推送
            sysMessage3.setEsSendNum(2);
            log.info("拌合站超时预警短信发送失败" + obj);
        }
        sysMessageService.save(sysMessage3);
    }

    public String getStartstation(String startstation) {
        String startsta = null;
        if (startstation != null) {
            if (startstation.length() == 2) {
                startsta = "K0+0" + startstation;
            } else if (startstation.length() == 3) {
                startsta = "K0+" + startstation;
            } else if (startstation.length() > 3) {
                String substring = startstation.substring(0, startstation.length() - 3);
                String substring1 = startstation.substring(startstation.length() - 3);
                startsta = "K" + substring + "+" + substring1;
            }
        }
        return startsta;
    }

    public String getEndstation(String endstation) {
        String startsta = null;
        if (endstation != null) {
            if (endstation.length() == 2) {
                startsta = "K0+0" + endstation;
            } else if (endstation.length() == 3) {
                startsta = "K0+" + endstation;
            } else if (endstation.length() > 3) {
                String substring = endstation.substring(0, endstation.length() - 3);
                String substring1 = endstation.substring(endstation.length() - 3);
                startsta = "K" + substring + "+" + substring1;
            }
        }
        return startsta;
    }
}
