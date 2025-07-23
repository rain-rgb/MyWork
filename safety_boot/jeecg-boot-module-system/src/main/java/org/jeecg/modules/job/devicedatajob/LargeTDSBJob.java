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
import com.trtm.iot.tadiao.entity.DeviceTowerHistorydata;
import com.trtm.iot.tadiao.service.IDeviceTowerHistorydataService;
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
 * \* Time: 11:07
 * \* Description:  大型设备塔吊预警数据短信配置
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LargeTDSBJob implements Job {

    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private IDeviceTowerHistorydataService deviceTowerHistorydataService;
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LARGETDSB_YUJING);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到龙门吊设备的定时任务配置信息" + DateUtils.now());
            return;
        }

        List<Map<String, Object>> yjList = deviceTowerHistorydataService.getYjList();
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
                            sysSms.setEsTitle("塔吊设备预警");
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
        Integer mainHookSta1 = 0, viceHookSta1 = 0, mainHookSta2 = 0, viceHookSta2 = 0, collision = 0,
                over1Wind1 = 0, over3Vice1 = 0, over4Vice2 = 0, over5Wind2 = 0, over6 = 0;
        mainHookSta1 = Integer.parseInt(map.get("mainHookSta1").toString());
        viceHookSta1 = Integer.parseInt(map.get("viceHookSta1").toString());
        mainHookSta2 = Integer.parseInt(map.get("mainHookSta2").toString());
        viceHookSta2 = Integer.parseInt(map.get("viceHookSta2").toString());
        collision = Integer.parseInt(map.get("collision").toString());
        over1Wind1 = Integer.parseInt(map.get("over1Wind1").toString());
        over3Vice1 = Integer.parseInt(map.get("over3Vice1").toString());
        over4Vice2 = Integer.parseInt(map.get("over4Vice2").toString());
        over5Wind2 = Integer.parseInt(map.get("over5Wind2").toString());
        over6 = Integer.parseInt(map.get("over6").toString());

        String str = "";
        String mainHookStatus1 = null, viceHookStatus1 = null, mainHookStatus2 = null, viceHookStatus2 = null, collisionHazard = null,
                overval1Wind = null, overval3Vicemoment1 = null, overval4Vicemoment2 = null, overval5Wind = null, overval6Tilt = null;
        int sjl = Integer.parseInt(map.get("sjl").toString());
        if (mainHookSta1 >= 1) {
            mainHookStatus1 = "主钩载荷预警";
            str += mainHookStatus1;
        }
        if (viceHookSta1 >= 1) {
            viceHookStatus1 = "副钩载荷预警";
            if ("".equals(str)) {
                str += viceHookStatus1;
            } else {
                str = str + "," + viceHookStatus1;
            }
        }
        if (mainHookSta2 >= 1) {
            mainHookStatus2 = "主钩吊钩载荷超重4.8T预警";
            if ("".equals(str)) {
                str += mainHookStatus2;
            } else {
                str = str + "," + mainHookStatus2;
            }
        }
        if (viceHookSta2 >= 1) {
            viceHookStatus2 = "副钩吊钩载荷超重预警";
            if ("".equals(str)) {
                str += viceHookStatus2;
            } else {
                str = str + "," + viceHookStatus2;
            }
        }
        if (collision >= 1) {
            collisionHazard = "相邻塔相差10度预警碰撞危险";
            if ("".equals(str)) {
                str += collisionHazard;
            } else {
                str = str + "," + collisionHazard;
            }
        }
        if (over1Wind1 >= 1) {
            overval1Wind = "5级风速预警1";
            if ("".equals(str)) {
                str += overval1Wind;
            } else {
                str = str + "," + overval1Wind;
            }
        }
        if (over3Vice1 >= 1) {
            overval3Vicemoment1 = "主力矩额定力矩85%预警";
            if ("".equals(str)) {
                str += overval3Vicemoment1;
            } else {
                str = str + "," + overval3Vicemoment1;
            }
        }
        if (over4Vice2 >= 1) {
            overval4Vicemoment2 = "副力矩额定力矩85%预警";
            if ("".equals(str)) {
                str += overval4Vicemoment2;
            } else {
                str = str + "," + overval4Vicemoment2;
            }
        }
        if (over5Wind2 >= 1) {
            overval5Wind = "5级风速预警2";
            if ("".equals(str)) {
                str += overval5Wind;
            } else {
                str = str + "," + overval5Wind;
            }
        }
        if (over6 >= 1) {
            overval6Tilt = "塔体垂直度6度偏差预警";
            if ("".equals(str)) {
                str += overval6Tilt;
            } else {
                str = str + "," + overval6Tilt;
            }
        }
        return str;
    }
}
