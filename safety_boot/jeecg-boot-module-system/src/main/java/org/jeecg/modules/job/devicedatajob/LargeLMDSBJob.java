package org.jeecg.modules.job.devicedatajob;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.largesbconfigure.entity.LargesbConfigure;
import com.trtm.iot.largesbconfigure.entity.LargesbHistory;
import com.trtm.iot.largesbconfigure.service.ILargesbConfigureService;
import com.trtm.iot.largesbconfigure.service.ILargesbHistoryService;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * \* @author: zxy
 * \* Date: 2023/09/19
 * \* Time: 10:07
 * \* Description:  大型设备龙门吊预警数据短信配置
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LargeLMDSBJob implements Job {

    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ILargesbConfigureService largesbConfigureService;
    @Autowired
    private ILargesbHistoryService largesbHistoryService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LARGELMDSB_YUJING);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到龙门吊设备的定时任务配置信息" + DateUtils.now());
            return;
        }

        List<Map<String, Object>> yjList = deviceCraneHistorydataService.getYjList();
        for (Map<String, Object> map : yjList) {
            String str = getStr(map);
            if (!"".equals(str)) {
                if (str.length() > 30) {
                    String[] split = str.split(",", 3);
                    str = split[0] + "," + split[1];
                }
                String deviceCode = (String) map.get("device_code");
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(deviceCode);
                LargesbHistory largesbHistory = new LargesbHistory();
                largesbHistory.setSblx(shebeiInfo.getSbtype());
                largesbHistory.setShebeiNo(deviceCode);
                largesbHistory.setYujingInfo(str);
                largesbHistory.setTime(new Date());
                largesbHistoryService.save(largesbHistory);
                log.info("大型设备短信预警历史数据添加成功");

                QueryWrapper<LargesbConfigure> largesbConfigureQueryWrapper = new QueryWrapper<>();
                largesbConfigureQueryWrapper.eq("shebei_no", deviceCode);
                LargesbConfigure largesb = largesbConfigureService.getOne(largesbConfigureQueryWrapper);
                if (ObjectUtil.isNotEmpty(largesb)) {
                    if (largesb.getYesornot() == 0) {
                        BhzPhone bhzPhone = bhzPhoneService.getBhzPhone(largesb.getNames());
                        if (ObjectUtil.isNotEmpty(bhzPhone)) {
                            SysMessageCore sysSms = new SysMessageCore();
                            sysSms.setEsTitle("龙门吊设备预警");
                            sysSms.setEsType("1");
                            sysSms.setEsReceiver(bhzPhone.getPhones());
                            JSONObject obj = new JSONObject();
                            obj.put("sbname", shebeiInfo.getSbname());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date now = new Date();
                            obj.put("time", sdf.format(now));
                            obj.put("content", str);
                            sysSms.setEsContent(obj.toString());
                            sysSms.setEsSendStatus("0");
                            sysSms.setEsSendNum(0);
                            sysSms.setRemark(shebeiInfo.getSbjno());
                            sysMessageCoreService.save(sysSms);
                            log.info("挂篮短信预警数据添加成功");
                        }
                    }
                }
            }
        }
    }

    private String getStr(Map<String, Object> map) {
        Integer mainHookSta = 0, viceHookSta = 0, windSpeed = 0, bigCar = 0, mainVice = 0,
                over1Height1 = 0, over2Height2 = 0, over3Speed1 = 0, over4Speed2 = 0;
        mainHookSta = Integer.parseInt(map.get("mainHookSta").toString());
        viceHookSta = Integer.parseInt(map.get("viceHookSta").toString());
        windSpeed = Integer.parseInt(map.get("windSpeed").toString());
        bigCar = Integer.parseInt(map.get("bigCar").toString());
        mainVice = Integer.parseInt(map.get("mainVice").toString());
        over1Height1 = Integer.parseInt(map.get("over1Height1").toString());
        over2Height2 = Integer.parseInt(map.get("over2Height2").toString());
        over3Speed1 = Integer.parseInt(map.get("over3Speed1").toString());
        over4Speed2 = Integer.parseInt(map.get("over4Speed2").toString());

        String str = "";
        String mainHookStatus = null, viceHookStatus = null, windSpeedAlam = null, bigCarAlm = null, mainViceAlam = null,
                overval1Height1 = null, overval2Height2 = null, overval3Speed1 = null, overval4Speed2 = null;
        int sjl = Integer.parseInt(map.get("sjl").toString());
        if (mainHookSta >= sjl) {
            mainHookStatus = "主钩载荷超重96T预警";
            str += mainHookStatus;
        }
        if (viceHookSta >= sjl) {
            viceHookStatus = "副钩载荷超重预警";
            if ("".equals(str)) {
                str += viceHookStatus;
            } else {
                str = str + "," + viceHookStatus;
            }
        }
        if (windSpeed >= sjl) {
            windSpeedAlam = "5级风速预警";
            if ("".equals(str)) {
                str += windSpeedAlam;
            } else {
                str = str + "," + windSpeedAlam;
            }
        }
        if (bigCar >= sjl) {
            bigCarAlm = "支腿0.2米偏差报警";
            if ("".equals(str)) {
                str += bigCarAlm;
            } else {
                str = str + "," + bigCarAlm;
            }
        }
        if (mainVice >= sjl) {
            mainViceAlam = "主副钩吊重预警";
            if ("".equals(str)) {
                str += mainViceAlam;
            } else {
                str = str + "," + mainViceAlam;
            }
        }
        if (over1Height1 >= sjl) {
            overval1Height1 = "主钩距离冲顶0.2m超高预警";
            if ("".equals(str)) {
                str += overval1Height1;
            } else {
                str = str + "," + overval1Height1;
            }
        }
        if (over2Height2 >= sjl) {
            overval2Height2 = "副钩距离冲顶0.2m超高预警";
            if ("".equals(str)) {
                str += overval2Height2;
            } else {
                str = str + "," + overval2Height2;
            }
        }
        if (over3Speed1 >= sjl) {
            overval3Speed1 = "主钩1.1倍额定速度超速预警";
            if ("".equals(str)) {
                str += overval3Speed1;
            } else {
                str = str + "," + overval3Speed1;
            }
        }
        if (over4Speed2 >= sjl) {
            overval4Speed2 = "副钩1.1倍额定速度超速预警";
            if ("".equals(str)) {
                str += overval4Speed2;
            } else {
                str = str + "," + overval4Speed2;
            }
        }
        return str;
    }
}
