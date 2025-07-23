package org.jeecg.modules.job.jiaobanzhuangjob;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.deviceHammeringonecfg.entity.DeviceHammeringOneCfg;
import com.trtm.iot.deviceHammeringonecfg.service.IDeviceHammeringOneCfgService;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicehammeringhistorydataone.entity.DeviceHammeringHistorydataOne;
import com.trtm.iot.devicehammeringhistorydataone.service.IDeviceHammeringHistorydataOneService;
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
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.MessageUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class cjgzJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IDeviceHammeringHistorydataOneService deviceHammeringHistorydataOneService;
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
    private IDeviceHammeringOneCfgService deviceHammeringOneCfgService;
    @Autowired
    private DeviceMixpileHistorydataPartController deviceMixpileHistorydataPartController;

    int overLevel = 0;
    StringBuilder content = new StringBuilder();
    String message = "";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.CJGZ_RNYJ);//申述浙晚 锤击管桩预警任务
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到锤击桩定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<DeviceHammeringHistorydataOne> jbzs = deviceHammeringHistorydataOneService.selectjbzzone(curid, 0);
 //       List<DeviceHammeringHistorydataOne> jbzs = deviceHammeringHistorydataOneService.selectjbzzonelisj(0, "cs12345678");
        if (null == jbzs || jbzs.size() == 0) {
            log.info(String.format("暂无锤击桩未判断的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceHammeringHistorydataOne jbzone : jbzs) {

            QueryWrapper<DeviceHammeringOneCfg> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shebeino",jbzone.getShebeino());
            DeviceHammeringOneCfg cfg = deviceHammeringOneCfgService.getOne(queryWrapper1);
            if (cfg == null){
                continue;
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(cfg.getShebeino());
            id = jbzone.getId();
            int final_over_level = 0;
            StringBuilder final_content = new StringBuilder();
            Integer is_call = 0; //是否报警  1不报警 0报警
            String phoneUid = "";//水泥搅拌桩电话号配置
            String phones = "";//水泥搅拌桩电话号
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

            is_call = cfg.getIsCall();//是否报警
            if (null != cfg.getJbzphones()) {
                phoneUid = cfg.getJbzphones();
            }
            if (StrUtil.isNotBlank(phoneUid)) {
                BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(phoneUid);
                if (null != bhzPhone) {
                    phones = bhzPhone.getPhones();
//                        primaryNames = bhzPhone.getNames();
                }
            }
            String shebeibianhao = jbzone.getShebeino();//设备编号
            if (StrUtil.isNotBlank(phones) && final_over_level == 1) {
                SysMessage sysMessage = new SysMessage();
                if (is_call == 0) {
                    sysMessage = messageUtil.setMessage("0", "锤击桩超标预警桩" + jbzpileno, "1", phones, obj.toString(), shebeibianhao);
                } else {
                    sysMessage = messageUtil.setMessage("-1", "锤击桩超标预警桩" + jbzpileno, "1", phones, obj.toString(), shebeibianhao);
                }
                sysMessageService.save(sysMessage);
            }

            jbzone.setAlertstate(alt);
            jbzone.setChaobiaodengji(final_over_level);
            jbzone.setProblem(final_content.toString());
            jbzone.setIstongji(jbzone.getIstongji());
            deviceHammeringHistorydataOneService.updateById(jbzone);


            log.info(String.format("软基成桩记录超标判断！   时间" + DateUtils.now() + "当前判断到" + id));

        }
        sysConfigService.updateSysConfig(JobUtil.CJGZ_RNYJ, id);
    }


    public Map overJudgment(DeviceHammeringHistorydataOne jbz, DeviceHammeringOneCfg cfg, String sbname, ShebeiInfo selectshebeione) {
        Map map = new HashMap();
        overLevel = 0;
        int alt = 1;
        content = new StringBuilder();
        message = "";

        try {
            // 桩长
            if (jbz.getPileDesigndep() != null) {
                // 设计长度
                message = "桩深";
                if (jbz.getPileDesigndep() > Double.parseDouble(jbz.getPileRealdep())) {
                    content.append(String.format("实际桩深%1$.2f小于设计桩深%2$.2f；", Double.valueOf(jbz.getPileRealdep()), jbz.getPileDesigndep()));
                    overLevel = 1;

                }
            } else if (null != cfg.getDesigndep()) {
                message = "桩深";
                if (Double.parseDouble(cfg.getDesigndep()) > Double.parseDouble(jbz.getPileRealdep())) {
                    content.append(String.format("实际桩深%1$.2f小于设计桩深%2$.2f。", Double.valueOf(jbz.getPileRealdep()), Double.valueOf(cfg.getDesigndep())));
                    overLevel = 1;
                }
            } else {
                content.append("终端未上传设计桩长;");
            }

            // 锤击数
            if (null != cfg.getPileSecond()) {
                message = "锤击数";
                if (cfg.getPileSecond() > jbz.getPileSecond()) {
                    content.append(String.format("实际锤击数%1$.2f小于设计桩深%2$.2f。",Double.valueOf(jbz.getPileSecond()),Double.valueOf(cfg.getPileSecond())));
                    overLevel = 1;
                }
            } else {
                content.append("终端未上传设计桩长;");
            }
            // 上限
            maxJudge("倾角", cfg.getXs(), jbz.getPileY());

            // 下限
            minJudge("倾角", cfg.getXx(), jbz.getPileY());

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
