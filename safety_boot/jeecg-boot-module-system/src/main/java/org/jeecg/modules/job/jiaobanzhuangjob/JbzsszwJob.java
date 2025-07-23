package org.jeecg.modules.job.jiaobanzhuangjob;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicemixpilehistorydatapart.controller.DeviceMixpileHistorydataPartController;
import com.trtm.iot.devicemixpileonecfg.entity.DeviceMixpileOneCfg;
import com.trtm.iot.devicemixpileonecfg.service.IDeviceMixpileOneCfgService;
import com.trtm.iot.devicemixpilestatic.service.IDeviceMixpileStaticService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.MessageUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JbzsszwJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    private IDeviceMixpileOneCfgService deviceMixpileOneCfgService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private MessageUtil messageUtil;
    @Autowired
    private IDeviceMixpileStaticService deviceMixpileStaticService;
    @Autowired
    private DeviceMixpileHistorydataPartController deviceMixpileHistorydataPartController;

    int overLevel = 0;
    StringBuilder content = new StringBuilder();
    String message = "";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SSZW);//申述浙晚 水泥搅拌桩定时任务=323
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到水泥搅拌桩定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<DeviceMixpileHistorydataOne> jbzs = deviceMixpileHistorydataOneService.selectjbzzone(curid, 0);
 //      List<DeviceMixpileHistorydataOne> jbzs = deviceMixpileHistorydataOneService.selectjbzzonelisj(0, "2024032003");
        if (null == jbzs || jbzs.size() == 0) {
            log.info(String.format("暂无水泥搅拌桩未判断的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceMixpileHistorydataOne jbzone : jbzs) {
            id = jbzone.getId();
            try {
                if (jbzone.getPileUspeed() == null){
                    jbzone.setPileUspeed("0");
                }
                double v = Double.parseDouble(jbzone.getPileUspeed());
                if (v < 0.0){
                    double abs = Math.abs(v);
                    jbzone.setPileUspeed(String.valueOf(abs));
                    deviceMixpileHistorydataOneService.updateById(jbzone);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            // 桩数据查询，根据设备编号，桩号，里程查询初复搅
            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shebeino",jbzone.getShebeino());
            queryWrapper1.eq("pile_no",jbzone.getPileNo());
            queryWrapper1.eq("pile_mileage",jbzone.getPileMileage());
            List<DeviceMixpileHistorydataOne> list = deviceMixpileHistorydataOneService.list(queryWrapper1);

            String shebeibianhao1 = jbzone.getShebeino();//设备编号
            QueryWrapper<DeviceMixpileOneCfg> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("shebeino", shebeibianhao1);
            ShebeiInfo selectshebeione1 = shebeiInfoService.selectshebeione(shebeibianhao1);
            DeviceMixpileOneCfg cfg1 = deviceMixpileOneCfgService.getOne(queryWrapper2); // 查询水泥搅拌桩超标配置
            if (cfg1 == null || null == selectshebeione1){
                continue;
            }
            double mi = 0.0;
            try {
                //cfg.getSggy() == 0 四搅两喷
                if (list.size() == 1 && cfg1.getSggy() == 0){
                    DeviceMixpileHistorydataOne deviceMixpileHistorydataOne = list.get(0);
                    deviceMixpileHistorydataOne.setAlertstate(1);
                    deviceMixpileHistorydataOne.setChaobiaodengji(1);
                    deviceMixpileHistorydataOne.setProblem("不满足四搅两喷工艺");
                    deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne);
                    continue;
                }else {
                    for (DeviceMixpileHistorydataOne l :list){
                        double pjdep = 0.0;
                        if (!StringUtil.isEmpty(l.getPileMinelec()) && !"0".equals(l.getPileMinelec())) {
                            if (Double.parseDouble(l.getPileMinelec()) >= 55) {
                                pjdep = Double.parseDouble(l.getPileMinelec());
                            } else if (!StringUtil.isEmpty(l.getPileCement()) && !StringUtil.isEmpty(l.getPileRealdep())) {
                                double pileCement = Double.parseDouble(l.getPileCement());
                                double pileRealdep = Double.parseDouble(l.getPileRealdep());
                                if (pileRealdep != 0) {
                                    pjdep = pileCement / (pileRealdep - 0.25);
                                }
                            }
                        } else if (!StringUtil.isEmpty(l.getPileCement()) && !StringUtil.isEmpty(l.getPileRealdep())) {
                            double pileCement = Double.parseDouble(l.getPileCement());
                            double pileRealdep = Double.parseDouble(l.getPileRealdep());
                            if (pileRealdep != 0) {
                                pjdep = pileCement / (pileRealdep - 0.25);
                            }
                        }
                        mi = mi + pjdep;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            jbzone.setPileMinelec(String.valueOf(mi));
            Integer is_call = 0; //是否报警  1不报警 0报警
            String phoneUid = "";//水泥搅拌桩电话号配置
            String phones = "";//水泥搅拌桩电话号
            StringBuilder final_content = new StringBuilder();
            String finalcontent = null;
            int final_over_level = 0;

            DeviceMixpileHistorydataOne jbz = new DeviceMixpileHistorydataOne();
            jbz.setId(id);
            try {
                String shebeibianhao = jbzone.getShebeino();//设备编号
                QueryWrapper<DeviceMixpileOneCfg> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeino", shebeibianhao);
                DeviceMixpileOneCfg cfg = deviceMixpileOneCfgService.getOne(queryWrapper); // 查询水泥搅拌桩超标配置
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
                if (null == selectshebeione) {
                    final_content.append(String.format("设备编号为" + shebeibianhao + "未在平台注册" + DateUtils.now()));
                    log.info(final_content.toString());
                    jbz.setProblem(final_content.toString());
                    jbz.setAlertstate(1);
                    jbz.setChaobiaodengji(final_over_level);
                    deviceMixpileHistorydataOneService.updateById(jbz);
                    continue;
                }
                if (null == cfg) {
                    if (jbzone.getIstongji() == 0) {
                        jobUtil.sattistics(jbzone, selectshebeione);
                    }
//                    log.info(String.format("设备编号为" + shebeibianhao + "水泥搅拌桩设备无预警配置" + DateUtils.now()));
                    jbz.setAlertstate(1);
                    jbz.setChaobiaodengji(final_over_level);
                    jbz.setProblem("设备编号为" + shebeibianhao + "搅拌桩设备无预警配置");
                    deviceMixpileHistorydataOneService.updateById(jbz);
                    continue;
                }

                if (null != cfg) {
                    is_call = cfg.getIsCall();//是否报警
                    if (null != cfg.getJbzphones()) {
                        phoneUid = cfg.getJbzphones();
                    }
                }
                if (StrUtil.isNotBlank(phoneUid)) {
                    BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(phoneUid);
                    if (null != bhzPhone) {
                        phones = bhzPhone.getPhones();
//                        primaryNames = bhzPhone.getNames();
                    }
                }

                //超标判断
                Map map = overJudgment(jbzone, cfg, selectshebeione.getSbname(), selectshebeione);
                final_over_level = (int) map.get("overLevel");// 0合格；1不合格
                final_content = (StringBuilder) map.get("content");
                int alt = (int) map.get("alt");

                if (final_over_level != 1){
                    //计算分数
                    int i = jbzone.getPileTime().indexOf(" ");
                    String format = jbzone.getPileTime().substring(0,i);
                    try {
                        deviceMixpileHistorydataPartController.queryPageListpj(jbzone.getShebeino(),jbzone.getPileNo(),format,jbzone.getPileMileage(),jbzone.getPileRatio(),jbzone.getPileDensity(),jbzone.getPileDesigndep());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date piletime = new Date((Long.valueOf(jbzone.getPileTime())- 28800)*1000);// 成桩时间减8小时
                JSONObject obj = new JSONObject();
                obj.put("sbname", selectshebeione.getSbname());
                obj.put("time", jbzone.getPileTime());
                obj.put("startstation", jbzone.getPileNo());//施工桩号

                //超标原因
                finalcontent = final_content.toString();
                String[] split = finalcontent.split(";");
                int length = split.length;
                obj = objects(finalcontent, obj,length,split);

                String jbzpileno = jbzone.getPileNo();
                if (jbzpileno.contains("_")) {
                    jbzpileno = jbzpileno.substring(jbzpileno.indexOf("_") + 1, jbzpileno.length());
                }
                if (StrUtil.isNotBlank(phones) && final_over_level == 1) {
                    SysMessage sysMessage = new SysMessage();
                    if (is_call == 0) {
                        sysMessage = messageUtil.setMessage("0", "软基超标预警桩" + jbzpileno, "1", phones, obj.toString(), shebeibianhao);
                    } else {
                        sysMessage = messageUtil.setMessage("-1", "软基超标预警桩" + jbzpileno, "1", phones, obj.toString(), shebeibianhao);
                    }

                    boolean b = false;
                    if(is_call == 0){
                        if (length == 1){
                            b = DySmsHelper.sendSms(phones, obj, DySmsEnum.SMS_CB_TFYSS);
                        }else if (length == 2){
                            b = DySmsHelper.sendSms(phones, obj, DySmsEnum.SMS_CB_TFYSS2);
                        }else if (length == 3){
                            b = DySmsHelper.sendSms(phones, obj, DySmsEnum.SMS_CB_TFYSS3);
                        }else if (length == 4){
                            b = DySmsHelper.sendSms(phones, obj, DySmsEnum.SMS_CB_TFYSS4);
                        }else if (length == 5){
                            b = DySmsHelper.sendSms(phones, obj, DySmsEnum.SMS_CB_TFYSS5);
                        }else if (length == 6){
                            b = DySmsHelper.sendSms(phones, obj, DySmsEnum.SMS_CB_TFYSS6);
                        }else if (length == 7){
                            b = DySmsHelper.sendSms(phones, obj, DySmsEnum.SMS_CB_TFYSS7);
                        }
                        if (b){
                            sysMessage.setEsSendNum(1);
                            log.info("拌合站超时预警短信发送成功"+obj);
                        }else {
                            sysMessage.setEsSendNum(2);
                            log.info("拌合站超时预警短信发送失败"+obj);
                        }
                    }
                    sysMessageService.save(sysMessage);
                }



                jbz.setAlertstate(alt);
                jbz.setChaobiaodengji(final_over_level);
                jbz.setProblem(final_content.toString());
                jbz.setIstongji(jbzone.getIstongji());
                deviceMixpileHistorydataOneService.updateById(jbz);
                if (jbz.getIstongji() == 0) {
                    jobUtil.sattistics(jbz, selectshebeione);
                }

                if(final_over_level > 0){
                    //两条都修改
                    for (DeviceMixpileHistorydataOne l :list){
                        l.setAlertstate(alt);
                        l.setChaobiaodengji(final_over_level);
                        l.setProblem(final_content.toString());
                        l.setIstongji(jbzone.getIstongji());
                        deviceMixpileHistorydataOneService.updateById(l);
                    }
                }else {
                    if (list.size() == 1){
                        continue;
                    }
                    DeviceMixpileHistorydataOne deviceMixpileHistorydataOne = list.get(0);
                    String problem = deviceMixpileHistorydataOne.getProblem();
                    if (problem != null){
                        if (list.get(0).getProblem().equals("不满足四搅两喷工艺")){
                            //两条都修改
                            deviceMixpileHistorydataOne.setPileMinelec(String.valueOf(mi));
                            //超标判断
                            Map map1 = overJudgment(deviceMixpileHistorydataOne, cfg, selectshebeione.getSbname(), selectshebeione);
                            final_over_level = (int) map1.get("overLevel");// 0合格；1不合格
                            final_content = (StringBuilder) map1.get("content");
                            int alt1 = (int) map1.get("alt");


//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date piletime = new Date((Long.valueOf(jbzone.getPileTime())- 28800)*1000);// 成桩时间减8小时
                            JSONObject obj1 = new JSONObject();
                            obj1.put("sbname", selectshebeione.getSbname());
                            obj1.put("time", deviceMixpileHistorydataOne.getPileTime());
                            obj1.put("startstation", jbzone.getPileNo());//施工桩号
                            //超标原因
                            finalcontent = final_content.toString();
                            String[] split1 = finalcontent.split(";");
                            int length1 = split1.length;
                            obj1 = objects(finalcontent, obj1,length1,split1);

                            String jbzpileno1 = deviceMixpileHistorydataOne.getPileNo();
                            if (jbzpileno1.contains("_")) {
                                jbzpileno1 = jbzpileno1.substring(jbzpileno1.indexOf("_") + 1, jbzpileno1.length());
                            }
                            if (StrUtil.isNotBlank(phones) && final_over_level == 1) {
                                SysMessage sysMessage = new SysMessage();
                                if (is_call == 0) {
                                    sysMessage = messageUtil.setMessage("0", "软基超标预警桩" + jbzpileno1, "1", phones, obj.toString(), shebeibianhao);
                                } else {
                                    sysMessage = messageUtil.setMessage("-1", "软基超标预警桩" + jbzpileno1, "1", phones, obj.toString(), shebeibianhao);
                                }

                                if (is_call == 0){
                                    boolean b = false;
                                    if (length1 == 1){
                                        b = DySmsHelper.sendSms(phones, obj1, DySmsEnum.SMS_CB_TFYSS);
                                    }else if (length1 == 2){
                                        b = DySmsHelper.sendSms(phones, obj1, DySmsEnum.SMS_CB_TFYSS2);
                                    }else if (length1 == 3){
                                        b = DySmsHelper.sendSms(phones, obj1, DySmsEnum.SMS_CB_TFYSS3);
                                    }else if (length1 == 4){
                                        b = DySmsHelper.sendSms(phones, obj1, DySmsEnum.SMS_CB_TFYSS4);
                                    }else if (length1 == 5){
                                        b = DySmsHelper.sendSms(phones, obj1, DySmsEnum.SMS_CB_TFYSS5);
                                    }else if (length1 == 6){
                                        b = DySmsHelper.sendSms(phones, obj1, DySmsEnum.SMS_CB_TFYSS6);
                                    }else if (length1 == 7){
                                        b = DySmsHelper.sendSms(phones, obj1, DySmsEnum.SMS_CB_TFYSS7);
                                    }
                                    if (b){
                                        sysMessage.setEsSendNum(1);
                                        log.info("拌合站超时预警短信发送成功"+obj1);
                                    }else {
                                        sysMessage.setEsSendNum(2);
                                        log.info("拌合站超时预警短信发送失败"+obj1);
                                    }
                                }
                                sysMessageService.save(sysMessage);
                            }

                            deviceMixpileHistorydataOne.setAlertstate(alt1);
                            deviceMixpileHistorydataOne.setChaobiaodengji(final_over_level);
                            deviceMixpileHistorydataOne.setProblem(final_content.toString());
                            deviceMixpileHistorydataOne.setIstongji(jbzone.getIstongji());
                            deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne);
                            if(final_over_level > 0){
                                //两条都修改
                                for (DeviceMixpileHistorydataOne l :list){
                                    l.setAlertstate(alt);
                                    l.setChaobiaodengji(final_over_level);
                                    l.setProblem(final_content.toString());
                                    l.setIstongji(jbzone.getIstongji());
                                    deviceMixpileHistorydataOneService.updateById(l);
                                }
                            }
                        }
                    } else {
                        jbzone.setAlertstate(alt);
                        jbzone.setChaobiaodengji(deviceMixpileHistorydataOne.getChaobiaodengji());
                        jbzone.setProblem(deviceMixpileHistorydataOne.getProblem());
                        jbzone.setIstongji(deviceMixpileHistorydataOne.getIstongji());
                        deviceMixpileHistorydataOneService.updateById(jbzone);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                jbz.setAlertstate(40);
                jbz.setProblem(final_content.toString());
                deviceMixpileHistorydataOneService.updateById(jbz);
            }
            log.info(String.format("软基成桩记录超标判断！   时间" + DateUtils.now() + "当前判断到" + id));

        }
        sysConfigService.updateSysConfig(JobUtil.SSZW, id);
    }

    private JSONObject objects(String finalcontent,JSONObject obj,int length,String[] split){
        if (length == 1){
            obj.put("content", finalcontent);
        }else if (length == 2){
            int i = 0;
            for (String s : split) {
                if (i == 0){
                    obj.put("content1", s);
                }else {
                    obj.put("content2", s);
                }
                i++;
            }
        }else if (length == 3){
            int i = 0;
            for (String s : split) {
                if (i == 0){
                    obj.put("content1", s);
                }else if (i == 1){
                    obj.put("content2", s);
                }else {
                    obj.put("content3", s);
                }
                i++;
            }
        }else if (length == 4){
            int i = 0;
            for (String s : split) {
                if (i == 0){
                    obj.put("content1", s);
                }else if (i == 1){
                    obj.put("content2", s);
                }else if (i == 2){
                    obj.put("content3", s);
                }else {
                    obj.put("content4", s);
                }
                i++;
            }
        }else if (length == 5){
            int i = 0;
            for (String s : split) {
                if (i == 0){
                    obj.put("content1", s);
                }else if (i == 1){
                    obj.put("content2", s);
                }else if (i == 2){
                    obj.put("content3", s);
                }else if (i == 3){
                    obj.put("content4", s);
                }else {
                    obj.put("content5", s);
                }
                i++;
            }
        }else if (length == 6){
            int i = 0;
            for (String s : split) {
                if (i == 0){
                    obj.put("content1", s);
                }else if (i == 1){
                    obj.put("content2", s);
                }else if (i == 2){
                    obj.put("content3", s);
                }else if (i == 3){
                    obj.put("content4", s);
                }else if (i == 4){
                    obj.put("content5", s);
                }else {
                    obj.put("content6", s);
                }
                i++;
            }
        }else if (length == 7){
            int i = 0;
            for (String s : split) {
                if (i == 0){
                    obj.put("content1", s);
                }else if (i == 1){
                    obj.put("content2", s);
                }else if (i == 2){
                    obj.put("content3", s);
                }else if (i == 3){
                    obj.put("content4", s);
                }else if (i == 4){
                    obj.put("content5", s);
                }else if (i == 5){
                    obj.put("content6", s);
                }else {
                    obj.put("content7", s);
                }
                i++;
            }
        }
        return obj;
    }

    public Map overJudgment(DeviceMixpileHistorydataOne jbz, DeviceMixpileOneCfg cfg, String sbname, ShebeiInfo selectshebeione) {
        Map map = new HashMap();
        overLevel = 0;
        int alt = 1;
        content = new StringBuilder();
        message = "";

        try {
            Double designdep = 0.0;
            // 桩长
            if (null != cfg.getDesigndep()) {
                message = "桩深";
                if (Double.valueOf(cfg.getDesigndep()) > Double.valueOf(jbz.getPileRealdep())) {
                    content.append(String.format("桩深%1$.2f小于设计桩深%2$.2f;", Double.valueOf(jbz.getPileRealdep()), Double.valueOf(cfg.getDesigndep())));
                    overLevel = 1;
                }
                designdep = Double.valueOf(cfg.getDesigndep());
            } else if (StrUtil.isNotBlank(jbz.getPileDesigndep())) {
                // 设计长度
                designdep = Double.valueOf(jbz.getPileDesigndep());
                message = "桩深";
                if (Double.valueOf(jbz.getPileDesigndep()) > Double.valueOf(jbz.getPileRealdep())) {
                    content.append(String.format("桩深%1$.2f小于设计桩深%2$.2f;", Double.valueOf(jbz.getPileRealdep()), Double.valueOf(jbz.getPileDesigndep())));
                    overLevel = 1;
                }
            } else{
                designdep = 0.0;
                content.append("终端未上传设计桩长;");
            }

            // 总水泥用量
            if (StrUtil.isNotBlank(cfg.getCement())) {
                message = "总水泥用量";
                if (Double.valueOf(cfg.getCement()) * designdep > Double.valueOf(jbz.getPileCement())) {
                    content.append(String.format("总水泥用量%1$.0f低于设计值%2$.0f;", Double.valueOf(jbz.getPileCement()), Double.valueOf(cfg.getCement()) * designdep));
                    overLevel = 1;
                }
            }
            // 提钻水泥浆用量下限
            if (StrUtil.isNotBlank(cfg.getUpbetonx())) {
                message = "提钻水泥浆用量";
                Double up = Double.valueOf(jbz.getPileUobeton()) * Double.valueOf(jbz.getPileDensity()) / (1 + Double.valueOf(jbz.getPileRatio()) / 100) * 1000;
                Double upcfg = Double.valueOf(cfg.getUpbetonx()) * designdep;
                if (up < upcfg) {
                    content.append(String.format("提钻水泥用量%1$.0f低于设计值%2$.0f;", up, upcfg));
                    overLevel = 1;
                }

            }
            // pile_density 水泥浆比重 平均密度
            if (StrUtil.isNotBlank(cfg.getPileDensity())) {
                if (Double.parseDouble(jbz.getPileDensity()) > 1000) {
                    jbz.setPileDensity(String.format("%.3f", Double.parseDouble(jbz.getPileDensity()) / 1000));//水泥浆比重
                }
                message = "平均密度";
                Double up = Double.valueOf(jbz.getPileDensity());
                Double upcfg = Double.valueOf(cfg.getPileDensity());
                if (up < upcfg) {
                    content.append(String.format("平均密度%1$.2f低于设计值%2$.2f;", up, upcfg));
                    overLevel = 1;
                }

            }
            // pile_minelec 每米水泥用量 平均灰量
            if (StrUtil.isNotBlank(cfg.getPileMinelec())) {
                message = "平均灰量";
                Double up = Double.valueOf(jbz.getPileMinelec());
                Double upcfg = Double.valueOf(cfg.getPileMinelec());
                if (up < upcfg) {
                    content.append(String.format("平均灰量%1$.2f低于设计值%2$.2f;", up, upcfg));
                    overLevel = 1;
                }

            }
            // 下钻钻水泥浆用量下限
            if (StrUtil.isNotBlank(cfg.getDownbetonx())) {
                message = "下钻水泥浆用量";
                Double down = Double.valueOf(jbz.getPileDownbeton()) * Double.valueOf(jbz.getPileDensity()) / (1 + Double.valueOf(jbz.getPileRatio()) / 100) * 1000;
                Double downcfg = Double.valueOf(cfg.getDownbetonx()) * designdep;
                if (down < downcfg) {
                    content.append(String.format("下钻水泥用量%1$.0f小于设计值%2$.0f;", down, downcfg));
                    overLevel = 1;
                }
            }
            // 上限
            maxJudge("倾角", cfg.getXs(), jbz.getPileX());
            maxJudge("下钻压力", cfg.getDpress(), jbz.getPileDpress());
            maxJudge("提钻压力", cfg.getUpress(), jbz.getPileUpress());
            maxJudge("下钻速度", cfg.getDspeeds(), jbz.getPileDspeed());
            maxJudge("提钻速度", cfg.getUspeeds(), jbz.getPileUspeed());

            // 下限
            minJudge("倾角", cfg.getXx(), jbz.getPileX());
            minJudge("下钻压力", cfg.getDpressx(), jbz.getPileDpress());
            minJudge("提钻压力", cfg.getUpressx(), jbz.getPileUpress());
            minJudge("下钻速度", cfg.getDspeedx(), jbz.getPileDspeed());
            minJudge("提钻速度", cfg.getUspeedx(), jbz.getPileUspeed());

//            if(overLevel == 1){
//             content.insert(0,String.format("桩"+jbz.getPileNo()+":"));
//            }
//            int pilecount = 0;
//            int chaobiaototal = 0;
//            double worklength = 0.0;
//            double allcement = 0.0;
//            double allbeton = 0.0;
//            if (StrUtil.isNotBlank(jbz.getPileMileage())){
//                String pileTime = jbz.getPileTime();
//                Date datanyr = NumberUtil.datanyr(pileTime);//格式化后的时间
//                String datanyr1 = NumberUtil.Stringnyr(datanyr);//格式化后的时间
//                DeviceMixpileStatic deviceMixpileStatic = deviceMixpileStaticService.selectone(datanyr1,jbz.getShebeino(),jbz.getPileMileage());
//                DeviceMixpileStatic deviceMixpileStatic1 = new DeviceMixpileStatic();
//                deviceMixpileStatic1.setShebeino(jbz.getShebeino());
//                deviceMixpileStatic1.setMileage(jbz.getPileMileage());
//                deviceMixpileStatic1.setStadate(datanyr1);
//                deviceMixpileStatic1.setOrgcode(selectshebeione.getSysOrgCode());
//                if (deviceMixpileStatic!=null){
//                    pilecount = Integer.parseInt(deviceMixpileStatic.getPilecount())+1;
//                    if (overLevel == 1){
//                        chaobiaototal = deviceMixpileStatic.getChaobiaototal()+1;
//                    }
//                    deviceMixpileStatic1.setPilecount(String.valueOf(pilecount));
//                    deviceMixpileStatic1.setChaobiaototal(chaobiaototal);
//                    worklength = Double.parseDouble(jbz.getPileRealdep())+ Double.parseDouble(deviceMixpileStatic.getPilecount());
//                    allcement = Double.parseDouble(jbz.getPileCement()) + Double.parseDouble(deviceMixpileStatic.getAllcement());
//                    allbeton = Double.parseDouble(jbz.getPileDownbeton()) + Double.parseDouble(jbz.getPileUobeton()) + Double.parseDouble(deviceMixpileStatic.getAllbeton());
//                    deviceMixpileStatic1.setId(deviceMixpileStatic.getId());
//                    deviceMixpileStatic1.setWorklength(String.valueOf(worklength));
//                    deviceMixpileStatic1.setAllbeton(String.valueOf(allbeton));
//                    deviceMixpileStatic1.setAllcement(String.valueOf(allcement));
//                    deviceMixpileStaticService.updateById(deviceMixpileStatic1);
//                }else {
//                    worklength = Double.parseDouble(jbz.getPileRealdep());
//                    allbeton = Double.parseDouble(jbz.getPileDownbeton()) + Double.parseDouble(jbz.getPileUobeton());
//                    allcement = Double.parseDouble(jbz.getPileCement());
//                    pilecount += 1;
//                    if (overLevel == 1){
//                        chaobiaototal +=1;
//                    }
//                    deviceMixpileStatic1.setPilecount(String.valueOf(pilecount));
//                    deviceMixpileStatic1.setChaobiaototal(chaobiaototal);
//                    deviceMixpileStatic1.setWorklength(String.valueOf(worklength));
//                    deviceMixpileStatic1.setAllbeton(String.valueOf(allbeton));
//                    deviceMixpileStatic1.setAllcement(String.valueOf(allcement));
//                    deviceMixpileStaticService.save(deviceMixpileStatic1);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            alt = 40;
            content.append(String.format("终端%1$s数据异常，判断出错", message));
        }

        map.put("overLevel", overLevel);
        map.put("content", content);
        map.put("alt", alt);

        return map;
    }

    // 上限判断
    void maxJudge(String m, String cfg, String jbz) {
        message = m;
        if (StrUtil.isNotBlank(cfg)) {
            if (Double.valueOf(jbz) > Double.valueOf(cfg)) {
                content.append(String.format(m + "%1$.2f高于设计值%2$.2f;", Double.valueOf(jbz), Double.valueOf(cfg)));
                overLevel = 1;
            }
        }

    }

    // 下限判断
    void minJudge(String m, String cfg, String jbz) {
        message = m;
        if (StrUtil.isNotBlank(cfg)) {
            if (Double.valueOf(jbz) < Double.valueOf(cfg)) {
                content.append(String.format(m + "%1$.2f低于设计值%2$.2f;", Double.valueOf(jbz), Double.valueOf(cfg)));
                overLevel = 1;
            }
        }

    }
}
