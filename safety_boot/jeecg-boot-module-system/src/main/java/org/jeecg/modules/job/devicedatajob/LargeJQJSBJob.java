package org.jeecg.modules.job.devicedatajob;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeHistorydata;
import com.trtm.iot.jiaqiaoji.service.IDeviceBridgeHistorydataService;
import com.trtm.iot.largesbconfigure.entity.LargesbConfigure;
import com.trtm.iot.largesbconfigure.entity.LargesbHistory;
import com.trtm.iot.largesbconfigure.service.ILargesbConfigureService;
import com.trtm.iot.largesbconfigure.service.ILargesbHistoryService;
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
 * \* Date: 2023/09/18
 * \* Time: 14:07
 * \* Description:  大型设备架桥机预警数据短信配置
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LargeJQJSBJob implements Job {

    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private IDeviceBridgeHistorydataService deviceBridgeHistorydataService;
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LARGEJQJSB_YUJING);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到架桥机设备的定时任务配置信息" + DateUtils.now());
            return;
        }

        List<Map<String, Object>> yjList = deviceBridgeHistorydataService.getYjList();
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
                            sysSms.setEsTitle("架桥机设备预警");
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
                            log.info("架桥机短信预警数据添加成功");
                        }
                    }
                }
            }
        }

    }

    private String getStr(Map<String, Object> map) {
        Integer hookSta1 = 0, hookSta2 = 0, over1Height1 = 0, over2Height2 = 0,
                over3Speed1 = 0, over4Speed2 = 0, over5Wind = 0, over6Verticality = 0;
        hookSta1 = Integer.parseInt(map.get("hookSta1").toString());
        hookSta2 = Integer.parseInt(map.get("hookSta2").toString());
        over1Height1 = Integer.parseInt(map.get("over1Height1").toString());
        over2Height2 = Integer.parseInt(map.get("over2Height2").toString());
        over3Speed1 = Integer.parseInt(map.get("over3Speed1").toString());
        over4Speed2 = Integer.parseInt(map.get("over4Speed2").toString());
        over5Wind = Integer.parseInt(map.get("over5Wind").toString());
        over6Verticality = Integer.parseInt(map.get("over6Verticality").toString());

        String str = "";
        String hookStatus1 = null, hookStatus2 = null, overval1Height1 = null, overval2Height2 = null,
                overval3Speed1 = null, overval4Speed2 = null, overval5Wind = null, overval6Verticality = null;
        int sjl = Integer.parseInt(map.get("sjl").toString());
        if (hookSta1 >= sjl) {
            hookStatus1 = "1#天车吊钩载荷预警值180T超重预警";
            str += hookStatus1;
        }
        if (hookSta2 >= sjl) {
            hookStatus2 = "2#天车吊钩载荷预警值180T超重预警";
            if ("".equals(str)) {
                str += hookStatus2;
            } else {
                str = str + "," + hookStatus2;
            }
        }
        if (over1Height1 >= sjl) {
            overval1Height1 = "1#距离冲顶0.2m超高预警";
            if ("".equals(str)) {
                str += overval1Height1;
            } else {
                str = str + "," + overval1Height1;
            }
        }
        if (over2Height2 >= sjl) {
            overval2Height2 = "2#距离冲顶0.2m超高预警";
            if ("".equals(str)) {
                str += overval2Height2;
            } else {
                str = str + "," + overval2Height2;
            }
        }
        if (over3Speed1 >= sjl) {
            overval3Speed1 = "1.1倍额定速度1#超速预警";
            if ("".equals(str)) {
                str += overval3Speed1;
            } else {
                str = str + "," + overval3Speed1;
            }
        }
        if (over4Speed2 >= sjl) {
            overval4Speed2 = "1.1倍额定速度2#超速预警";
            if ("".equals(str)) {
                str += overval4Speed2;
            } else {
                str = str + "," + overval4Speed2;
            }
        }
        if (over5Wind >= sjl) {
            overval5Wind = "5级风速预警";
            if ("".equals(str)) {
                str += overval5Wind;
            } else {
                str = str + "," + overval5Wind;
            }
        }
        if (over6Verticality >= sjl) {
            overval6Verticality = "前支腿垂直度6度偏差预警";
            if ("".equals(str)) {
                str += overval6Verticality;
            } else {
                str = str + "," + overval6Verticality;
            }
        }
        return str;
    }
}
