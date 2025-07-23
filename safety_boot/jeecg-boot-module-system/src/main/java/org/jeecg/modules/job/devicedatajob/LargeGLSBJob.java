package org.jeecg.modules.job.devicedatajob;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.gualan.entity.GualanBase;
import com.trtm.iot.gualan.service.IGualanBaseService;
import com.trtm.iot.largesbconfigure.entity.LargesbConfigure;
import com.trtm.iot.largesbconfigure.entity.LargesbHistory;
import com.trtm.iot.largesbconfigure.service.ILargesbConfigureService;
import com.trtm.iot.largesbconfigure.service.ILargesbHistoryService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * \* @author: zxy
 * \* Date: 2023/09/13
 * \* Time: 14:07
 * \* Description:  大型设备挂篮预警数据短信配置
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LargeGLSBJob implements Job {

    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IGualanBaseService gualanBaseService;
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LARGEGLSB_YUJING);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到挂篮设备的定时任务配置信息" + DateUtils.now());
            return;
        }

        List<Map<String, Object>> yjList = gualanBaseService.getYjList();
        for (Map<String, Object> map : yjList) {
            String shebeibanhao = (String) map.get("shebeibanhao");
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeibanhao);
            QueryWrapper<LargesbConfigure> largesbConfigureQueryWrapper = new QueryWrapper<>();
            largesbConfigureQueryWrapper.eq("shebei_no", shebeibanhao);
            LargesbConfigure largesb = largesbConfigureService.getOne(largesbConfigureQueryWrapper);
            BhzPhone bhzPhone = bhzPhoneService.getBhzPhone(largesb.getNames());
            String str = getStr(map);
            // 2端预警判断
            String portInfo = get2PortInfo(shebeiInfo);
            if (!"".equals(str)) {
                if (str.length() > 30) {
                    String[] split = str.split(",", 3);
                    str = split[0] + "," + split[1];
                }
                LargesbHistory largesbHistory = new LargesbHistory();
                largesbHistory.setSblx(shebeiInfo.getSbtype());
                largesbHistory.setShebeiNo(shebeibanhao);
                largesbHistory.setYujingInfo(str);
                largesbHistory.setTime(new Date());
                largesbHistoryService.save(largesbHistory);
                log.info("大型设备短信预警历史数据添加成功");
                if (ObjectUtil.isNotEmpty(largesb)) {
                    if (largesb.getYesornot() == 0) {
                        if (ObjectUtil.isNotEmpty(bhzPhone)) {
                            saveMessage(bhzPhone, shebeiInfo, str);

                        }
                    }
                }
            }

            if (StringUtils.isNotBlank(portInfo)) {
                if (ObjectUtil.isNotEmpty(largesb)) {
                    if (largesb.getYesornot() == 0) {
                        if (ObjectUtil.isNotEmpty(bhzPhone)) {
                            saveMessage(bhzPhone, shebeiInfo, portInfo);

                        }
                    }
                }

            }


        }
    }


    private String getStr(Map<String, Object> map) {
        Integer overweight1 = 0, overweight2 = 0, overweight3 = 0, overweight4 = 0,
                over1 = 0, over2 = 0, over3 = 0, over4 = 0, over5 = 0, windwarn = 0;
        windwarn = Integer.parseInt(map.get("windspeedwarn").toString());
        overweight1 = Integer.parseInt(map.get("overweight1").toString());
        overweight2 = Integer.parseInt(map.get("overweight2").toString());
        overweight3 = Integer.parseInt(map.get("overweight3").toString());
        overweight4 = Integer.parseInt(map.get("overweight4").toString());
        over1 = Integer.parseInt(map.get("overval1").toString());
        over2 = Integer.parseInt(map.get("overval2").toString());
        over3 = Integer.parseInt(map.get("overval3").toString());
        over4 = Integer.parseInt(map.get("overval4").toString());
        over5 = Integer.parseInt(map.get("overval5").toString());

        String str = "";
        String overweight1Info = null, overweight2Info = null, overweight3Info = null, overweight4Info = null,
                over1Info = null, over2Info = null, over3Info = null, over4Info = null, over5Info = null, windwarnInfo = "";
        int sjl = Integer.parseInt(map.get("sjl").toString()) - 8;
        if (overweight1 >= sjl) {
            overweight1Info = "1#螺纹钢超重预警";
            str += overweight1Info;
        }
        if (overweight2 >= sjl) {
            overweight2Info = "2#螺纹钢超重预警";
            if ("".equals(str)) {
                str += overweight2Info;
            } else {
                str = str + "," + overweight2Info;
            }
        }
        if (overweight3 >= sjl) {
            overweight3Info = "3#螺纹钢超重预警";
            if ("".equals(str)) {
                str += overweight3Info;
            } else {
                str = str + "," + overweight3Info;
            }
        }
        if (overweight4 >= sjl) {
            overweight4Info = "4#螺纹钢超重预警";
            if ("".equals(str)) {
                str += overweight4Info;
            } else {
                str = str + "," + overweight4Info;
            }
        }
        if (over1 >= sjl) {
            over1Info = "螺纹钢承重预警额定拉力85%预警";
            if ("".equals(str)) {
                str += over1Info;
            } else {
                str = str + "," + over1Info;
            }
        }
        if (over2 >= sjl) {
            over2Info = "超重预警额定重量100%预警";
            if ("".equals(str)) {
                str += over2Info;
            } else {
                str = str + "," + over2Info;
            }
        }
        if (over3 >= sjl) {
            over3Info = "前横梁水平度预警偏差3度预警";
            if ("".equals(str)) {
                str += over3Info;
            } else {
                str = str + "," + over3Info;
            }
        }
        if (over4 >= sjl) {
            over4Info = "横梁左右偏差3度预警倾斜预警偏差3度预警";
            if ("".equals(str)) {
                str += over4Info;
            } else {
                str = str + "," + over4Info;
            }
        }
        if (over5 >= sjl) {
            over5Info = "纵梁变形预警偏差3度预警";
            if ("".equals(str)) {
                str += over5Info;
            } else {
                str = str + "," + over5Info;
            }
        }
        if (windwarn >= sjl) {
            windwarnInfo = "风速预警";
            if ("".equals(str)) {
                str += windwarnInfo;
            } else {
                str = str + "," + windwarnInfo;
            }
        }
        return str;
    }


    String get2PortInfo(ShebeiInfo largesb) {
        Map<String, Object> infoMap = gualanBaseService.getMap2Port("", "");
        Double wucha = Math.abs(Double.parseDouble(infoMap.get("chazhi").toString()));
        if (wucha > 10.0) {
            String info = "不平衡浇筑预警差值为" + String.format("%.2f", wucha) + "吨";
            LargesbHistory largesbHistory = new LargesbHistory();
            largesbHistory.setSblx(largesb.getSbtype());
            largesbHistory.setShebeiNo(largesb.getSbjno());
            largesbHistory.setYujingInfo(info);
            largesbHistory.setTime(new Date());
            largesbHistory.setPort1(infoMap.get("ida").toString());
            largesbHistory.setPort2(infoMap.get("idb").toString());
            largesbHistoryService.save(largesbHistory);
            log.info("大型设备短信预警历史数据添加成功");
            return info;
        }
        return "";
    }

    void saveMessage(BhzPhone bhzPhone, ShebeiInfo shebeiInfo, String str) {
        SysMessageCore sysSms = new SysMessageCore();
        sysSms.setEsTitle("挂篮设备预警");
        sysSms.setEsType("1");
        sysSms.setEsReceiver(bhzPhone.getPhones());
        com.alibaba.fastjson.JSONObject obj = new JSONObject();
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
