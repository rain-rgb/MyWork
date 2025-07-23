package org.jeecg.modules.job.jiaobanzhuangjob;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicemixpileonecfg.entity.DeviceMixpileOneCfg;
import com.trtm.iot.devicemixpileonecfg.service.IDeviceMixpileOneCfgService;
import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.trtm.iot.devicemixpilestatic.service.IDeviceMixpileStaticService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.MessageUtil;
import org.jeecg.modules.job.jobutil.NumberUtil;
import org.jeecg.modules.job.jobutil.ZhydJobUntil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JbzJob implements Job {

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

    int overLevel = 0;
    StringBuilder content = new StringBuilder();
    String message = "";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JBZ_CFG);//水泥搅拌桩定时任务=33
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到水泥搅拌桩定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<DeviceMixpileHistorydataOne> jbzs = deviceMixpileHistorydataOneService.selectjbzzone(curid, 0);
        if (null == jbzs || jbzs.size() == 0) {
            log.info(String.format("暂无水泥搅拌桩未判断的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceMixpileHistorydataOne jbzone : jbzs) {
            Integer is_call = 0; //是否报警  1不报警 0报警
            String phoneUid = "";//水泥搅拌桩电话号配置
            String phones = "";//水泥搅拌桩电话号
            StringBuilder final_content = new StringBuilder();
            int final_over_level = 0;
            id = jbzone.getId();
            DeviceMixpileHistorydataOne jbz = new DeviceMixpileHistorydataOne();
            jbz.setId(id);
            try {
                String shebeibianhao = jbzone.getShebeino();//设备编号
                QueryWrapper<DeviceMixpileOneCfg> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeino", shebeibianhao);
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
                DeviceMixpileOneCfg cfg = deviceMixpileOneCfgService.getOne(queryWrapper); // 查询水泥搅拌桩超标配置
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


//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date piletime = new Date((Long.valueOf(jbzone.getPileTime())- 28800)*1000);// 成桩时间减8小时
                JSONObject obj = new JSONObject();
                obj.put("sbname", selectshebeione.getSbname());
                obj.put("time", jbzone.getPileTime());
                if (final_content.length() > 20) {
                    obj.put("content", final_content.substring(0, 20) + "...详情请看平台");
                } else {
                    obj.put("content", final_content);
                }
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

            } catch (Exception e) {
                e.printStackTrace();
                jbz.setAlertstate(40);
                jbz.setProblem(final_content.toString());
                deviceMixpileHistorydataOneService.updateById(jbz);
            }
            log.info(String.format("软基成桩记录超标判断！   时间" + DateUtils.now() + "当前判断到" + id));

        }
        sysConfigService.updateSysConfig(JobUtil.JBZ_CFG, id);
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
            if (StrUtil.isNotBlank(jbz.getPileDesigndep())) {
                // 设计长度
                designdep = Double.valueOf(jbz.getPileDesigndep());
                message = "桩深";
                if (Double.valueOf(jbz.getPileDesigndep()) > Double.valueOf(jbz.getPileRealdep())) {
                    content.append(String.format("实际桩深%1$.2f小于设计桩深%2$.2f；", Double.valueOf(jbz.getPileRealdep()), Double.valueOf(jbz.getPileDesigndep())));
                    overLevel = 1;

                }
            } else if (null != cfg.getDesigndep()) {
                message = "桩深";
                if (Double.valueOf(cfg.getDesigndep()) > Double.valueOf(jbz.getPileRealdep())) {
                    content.append(String.format("实际桩深%1$.2f小于设计桩深%2$.2f。", Double.valueOf(jbz.getPileRealdep()), Double.valueOf(cfg.getDesigndep())));
                    overLevel = 1;
                }
                designdep = Double.valueOf(cfg.getDesigndep());
            } else {
                designdep = 0.0;
                content.append("终端未上传设计桩长;");
            }

            // 总水泥用量
            if (StrUtil.isNotBlank(cfg.getCement())) {
                message = "总水泥用量";
                if (Double.valueOf(cfg.getCement()) * designdep > Double.valueOf(jbz.getPileCement())) {
                    content.append(String.format("总水泥用量%1$.0f低于设计值%2$.0f；", Double.valueOf(jbz.getPileCement()), Double.valueOf(cfg.getCement()) * designdep));
                    overLevel = 1;
                }
            }
            // 提钻水泥浆用量下限
            if (StrUtil.isNotBlank(cfg.getUpbetonx())) {
                message = "提钻水泥浆用量";
                Double up = Double.valueOf(jbz.getPileUobeton()) * Double.valueOf(jbz.getPileDensity()) / (1 + Double.valueOf(jbz.getPileRatio()) / 100) * 1000;
                Double upcfg = Double.valueOf(cfg.getUpbetonx()) * designdep;
                if (up < upcfg) {
                    content.append(String.format("提钻水泥用量%1$.0f低于设计值%2$.0f；", up, upcfg));
                    overLevel = 1;
                }

            }
            // 下钻钻水泥浆用量下限
            if (StrUtil.isNotBlank(cfg.getDownbetonx())) {
                message = "下钻水泥浆用量";
                Double down = Double.valueOf(jbz.getPileDownbeton()) * Double.valueOf(jbz.getPileDensity()) / (1 + Double.valueOf(jbz.getPileRatio()) / 100) * 1000;
                Double downcfg = Double.valueOf(cfg.getDownbetonx()) * designdep;
                if (down < downcfg) {
                    content.append(String.format("下钻水泥用量%1$.0f小于设计值%2$.0f；", down, downcfg));
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
                content.append(String.format(m + "%1$.2f高于设计值%2$.2f；", Double.valueOf(jbz), Double.valueOf(cfg)));
                overLevel = 1;
            }
        }

    }

    // 下限判断
    void minJudge(String m, String cfg, String jbz) {
        message = m;
        if (StrUtil.isNotBlank(cfg)) {
            if (Double.valueOf(jbz) < Double.valueOf(cfg)) {
                content.append(String.format(m + "%1$.2f低于设计值%2$.2f；", Double.valueOf(jbz), Double.valueOf(cfg)));
                overLevel = 1;
            }
        }

    }
}
