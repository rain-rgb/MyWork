package org.jeecg.modules.job.devicedatajob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.devicehistorystatic.entity.DevicehistoryStatic;
import com.trtm.iot.devicehistorystatic.service.IDevicehistoryStaticService;
import com.trtm.iot.devicehistoryyujing.entity.DevicehistoryYujing;
import com.trtm.iot.devicehistoryyujing.service.IDevicehistoryYujingService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.NumberUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * \* @author: zml
 * \* Date: 2022/06/24
 * \* Time: 14:07
 * \* Description:  环境监测数据超标监测
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class deviceChaobiao implements Job {

    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IDevicehistoryService devicehistoryService;
    @Autowired
    private IDevicehistoryYujingService devicehistoryYujingService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private ISysMessageService sysMessageService;
    @Autowired
    private IDevicehistoryStaticService devicehistoryStaticService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.DEVICE_CHAOBIAO);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到获取环境监测数据超标监测定时任务配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<Devicehistory> selectdeviceones = devicehistoryService.selectdeviceone(curid, 0);
        if (null == selectdeviceones || selectdeviceones.size() == 0) {
            log.info("暂无环境监测未判断的数据");
            return;
        }
        int id = 0;
        for (Devicehistory devicehistory : selectdeviceones) {
            id = devicehistory.getId();
            Integer is_call = 0; //是否报警  1不报警 0报警
            String primaryUid = "";//初级超标电话号配置
            String middleUid = "";//中级超标电话号配置
            String advancedUid = "";//高级超标电话号配置
            String primaryPhones = "";//初级超标电话号
            String middlePhones = "";//中级超标电话号
            String advancedPhones = "";//高级超标电话号
            StringBuilder final_content = new StringBuilder();
            int final_over_level = 0;
            String elecdate = DateUtil.format(devicehistory.getTimevalue(), "yyyy-MM-dd HH:mm:ss");//数据上传时间
            String devaddr = devicehistory.getDevaddr();//设备编号
            try {
                DevicehistoryYujing selectone = devicehistoryYujingService.selectone(devaddr);
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(devaddr);
                //判断是否有设备
                if (null == selectshebeione) {
                    log.info("暂无环境监测的设备" + DateUtils.now());
                    continue;
                }
                if (selectone != null) {
                    if (selectone.getIsCall() != null) {
                        is_call = selectone.getIsCall();
                    }
                    if (selectone.getPrimaryPhones() != null) {
                        primaryUid = selectone.getPrimaryPhones();
                    }
                    if (selectone.getMiddlePhones() != null) {
                        middleUid = selectone.getPrimaryPhones();
                    }
                    if (selectone.getAdvancePhones() != null) {
                        advancedUid = selectone.getPrimaryPhones();
                    }
                    if (StrUtil.isNotBlank(primaryUid)) {
                        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(primaryUid);
                        if (null != bhzPhone) {
                            primaryPhones = bhzPhone.getPhones();
                        }
                    }
                    if (StrUtil.isNotBlank(middleUid)) {
                        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(middleUid);
                        if (null != bhzPhone) {
                            middlePhones = bhzPhone.getPhones();
                        }
                    }
                    if (StrUtil.isNotBlank(advancedUid)) {
                        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(advancedUid);
                        if (null != bhzPhone) {
                            advancedPhones = bhzPhone.getPhones();
                        }
                    }
                }
                Map<String, Object> map = deviceHistoryOver(devicehistory, selectshebeione, is_call, selectone);
                int status = (int) map.get("status");//状态（0：合格，1：初级超标 2：中级超标 3:高级超标）
                final_content.append(map.get("final_content"));//超标信息
                final_over_level = status;
                if (0 == final_over_level || final_content.length() == 0) {
                    continue;
                }
                JSONObject obj = new JSONObject();
                obj.put("sbname", selectshebeione.getSbname());
                obj.put("time", elecdate);
                obj.put("content", final_content);   //
                SysMessage sysMessage = new SysMessage();
                sysMessage.setEsTitle("环境超标检测");
                sysMessage.setEsType("1");//短信类型  1短信
                if (!primaryPhones.equals("") && final_over_level == 1) {
                    sysMessage.setEsReceiver(primaryPhones);//手机号
                }
                if (!advancedPhones.equals("") && final_over_level == 3) {
                    sysMessage.setEsReceiver(advancedPhones);//手机号
                }
                if (!middlePhones.equals("") && final_over_level == 2) {
                    sysMessage.setEsReceiver(middlePhones);//手机号
                }
                sysMessage.setEsContent(obj.toString());//短信内容
                sysMessage.setRemark(devaddr);
                if (is_call == 0) {
                    sysMessage.setEsSendStatus("0");//推送状态0未推送
                    sysMessage.setEsSendNum(0);//推送总次数
                } else {
                    sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
                    sysMessage.setEsSendNum(0);//推送总次数
                    sysMessage.setEsResult("不需要推送");
                }
                sysMessage.setCreateTime(new Date());
                if (sysMessage.getEsReceiver() != null) {
                    sysMessageService.save(sysMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("环境监测超标检测!   时间:" + DateUtils.now());
        }
        sysConfigService.updateSysConfig(JobUtil.DEVICE_CHAOBIAO, id);//根据传过来的唯一值来修改当前判断到的数据id
    }

    private Map<String, Object> deviceHistoryOver(Devicehistory devicehistory, ShebeiInfo selectshebeione, Integer is_call, DevicehistoryYujing selectone) {
        Map<String, Object> map = new HashMap<>();
        int final_over_level1 = 0;
        StringBuilder final_content = new StringBuilder();
        double pm10 = 0.0;
        double pm25 = 0.0;
        double noise = 0.0;
        double pm10y = 100.0;
        double pm10l = 125.0;
        double pm10c = 150.0;
        double pm25y = 50.0;
        double pm25l = 65.0;
        double pm25c = 70.0;
        double noiseyn = 55.0;
        double noiseln = 65.0;
        double noisecn = 75.0;
        double noisey = 70.0;
        double noisel = 80.0;
        double noisec = 90.0;
        String massage = null;
        int elestatus = 0;
        int elestatus1 = 0;
        int elestatus2 = 0;
        Date time = devicehistory.getTimevalue();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String hour = format.format(time);
        String datanyr = NumberUtil.Stringnyr(time);//格式化后的时间
        Date date = NumberUtil.datanyr(datanyr);
        Devicehistory devicehistory1 = new Devicehistory();
        if (!StrUtil.isBlank(devicehistory.getPm25())) {
            pm25 = Double.parseDouble(devicehistory.getPm25());
        }
        if (!StrUtil.isBlank(devicehistory.getPm10())) {
            pm10 = Double.parseDouble(devicehistory.getPm10());
        }
        if (!StrUtil.isBlank(devicehistory.getNoise())) {
            noise = Double.parseDouble(devicehistory.getNoise());
        }
        if (selectone != null) {
            if (!StrUtil.isBlank(selectone.getPm10y())) {
                pm10y = Double.parseDouble(selectone.getPm10y());
            }
            if (!StrUtil.isBlank(selectone.getPm10l())) {
                pm10l = Double.parseDouble(selectone.getPm10l());
            }
            if (!StrUtil.isBlank(selectone.getPm10l())) {
                pm10c = Double.parseDouble(selectone.getPm10c());
            }
            if (!StrUtil.isBlank(selectone.getPm25y())) {
                pm25y = Double.parseDouble(selectone.getPm25y());
            }
            if (!StrUtil.isBlank(selectone.getPm25l())) {
                pm25l = Double.parseDouble(selectone.getPm25l());
            }
            if (!StrUtil.isBlank(selectone.getPm25l())) {
                pm25c = Double.parseDouble(selectone.getPm25c());
            }
            if (!StrUtil.isBlank(selectone.getNoisey())) {
                noisey = Double.parseDouble(selectone.getNoisey());
            }
            if (!StrUtil.isBlank(selectone.getNoisel())) {
                noisel = Double.parseDouble(selectone.getNoisel());
            }
            if (!StrUtil.isBlank(selectone.getNoisec())) {
                noisec = Double.parseDouble(selectone.getNoisec());
            }
            if (!StrUtil.isBlank(selectone.getNoiseyn())) {
                noiseyn = Double.parseDouble(selectone.getNoiseyn());
            }
            if (!StrUtil.isBlank(selectone.getNoiseln())) {
                noiseln = Double.parseDouble(selectone.getNoiseln());
            }
            if (!StrUtil.isBlank(selectone.getNoisecn())) {
                noisecn = Double.parseDouble(selectone.getNoisecn());
            }
        }
        if (pm25 > 0 && pm25 <= pm25y) {
            devicehistory1.setPm25status(0);
        } else if (pm25 > pm25y && pm25 <= pm25l) {
            devicehistory1.setPm25status(1);
            elestatus = 1;
            massage = "pm2.5初级超标";
        } else if (pm25 > pm25l && pm25 <= pm25c) {
            devicehistory1.setPm25status(2);
            elestatus = 2;
            massage = "pm2.5中级超标";
        } else {
            devicehistory1.setPm25status(3);
            elestatus = 3;
            massage = "pm2.5高级超标";
        }
        if (pm10 > 0 && pm10 <= pm10y) {
            devicehistory1.setPm10status(0);
        } else if (pm10 > pm10y && pm10 <= pm10l) {
            devicehistory1.setPm10status(1);
            elestatus1 = 1;
            if (massage == null) {
                massage = "pm10初级超标";
            } else {
                massage = massage + ",pm10初级超标";
            }
        } else if (pm10 > pm10l && pm10 <= pm10c) {
            devicehistory1.setPm10status(2);
            elestatus1 = 2;
            if (massage == null) {
                massage = "pm10中级超标";
            } else {
                massage = massage + ",pm10中级超标";
            }
        } else {
            devicehistory1.setPm10status(3);
            elestatus1 = 3;
            if (massage == null) {
                massage = "pm10高级超标";
            } else {
                massage = massage + ",pm10高级超标";
            }
        }
        boolean boo = DateUtils.compTime("22:00:00", hour);
        boolean boo1 = DateUtils.compTime(hour, "06:00:00");
        if (boo && boo1) {
            if (noise >= 0 && noise <= noisey) {
                devicehistory1.setNoisestatus(0);
            } else if (noise >= noisey && noise <= noisel) {
                devicehistory1.setNoisestatus(1);
                elestatus2 = 1;
                if (massage == null) {
                    massage = "白天噪声超标";
                } else {
                    massage = massage + ",白天噪声超标";
                }
            } else if (noise >= noisel && noise <= noisec) {
                devicehistory1.setNoisestatus(2);
                elestatus2 = 2;
                if (massage == null) {
                    massage = "白天噪声超标";
                } else {
                    massage = massage + ",白天噪声超标";
                }
            } else {
                devicehistory1.setNoisestatus(3);
                elestatus2 = 3;
                if (massage == null) {
                    massage = "白天噪声超标";
                } else {
                    massage = massage + ",白天噪声超标";
                }
            }
        } else {
            if (noise >= 0 && noise <= noiseyn) {
                devicehistory1.setNoisestatus(0);
            } else if (noise >= noiseyn && noise <= noiseln) {
                devicehistory1.setNoisestatus(1);
                elestatus2 = 1;
                if (massage == null) {
                    massage = "夜间噪声超标";
                } else {
                    massage = massage + ",夜间噪声超标";
                }
            } else if (noise >= noiseln && noise <= noisecn) {
                devicehistory1.setNoisestatus(2);
                elestatus2 = 2;
                if (massage == null) {
                    massage = "夜间噪声超标";
                } else {
                    massage = massage + ",夜间噪声超标";
                }
            } else {
                devicehistory1.setNoisestatus(3);
                elestatus2 = 3;
                if (massage == null) {
                    massage = "夜间噪声超标";
                } else {
                    massage = massage + ",夜间噪声超标";
                }
            }
        }
        int max = Math.max(elestatus, elestatus1);
        int max1 = Math.max(elestatus2, max);
        if (final_over_level1 < max1) {
            final_over_level1 = max1;
        }
        devicehistory1.setStatus(final_over_level1);
        devicehistory1.setId(devicehistory.getId());
        devicehistory1.setAlertstate(1);
        countchaobiao(devicehistory1, date, devicehistory.getDevaddr());
        final_content.append(String.format("%1$s", massage));
        devicehistory1.setStatus(final_over_level1);
        map.put("status", final_over_level1);
        map.put("final_content", final_content.toString());
        devicehistory1.setOverproofInfo(final_content.toString());
        devicehistoryService.updateById(devicehistory1);
        return map;
    }

    private void countchaobiao(Devicehistory devicehistory1, Date date, String devaddr) {
        DevicehistoryStatic devicehistoryStatic = devicehistoryStaticService.selectone(devaddr, date);
        DevicehistoryStatic devicehistoryStatic1 = new DevicehistoryStatic();
        devicehistoryStatic1.setTemchao(0);
        devicehistoryStatic1.setHumchao(0);
        devicehistoryStatic1.setWinchao(0);
        devicehistoryStatic1.setWinsdchao(0);
        int chaototal = 0;
        int primaryTotal = 0;
        int middleTotal = 0;
        int advanceTotal = 0;
        int total = 0;
        int pm10chao = 0;
        int pm25chao = 0;
        int noisechao = 0;
        int pm25primary = 0;
        int pm25middle = 0;
        int pm25advance = 0;
        int pm10primary = 0;
        int pm10middle = 0;
        int pm10advance = 0;
        int noiseprimary = 0;
        int noisemiddle = 0;
        int noiseadvance = 0;
        total += 1;
        if (devicehistory1.getStatus() > 0) {
            chaototal += 1;
        }
        if (devicehistory1.getStatus() == 1) {
            primaryTotal += 1;
        } else if (devicehistory1.getStatus() == 2) {
            middleTotal += 1;
        } else if (devicehistory1.getStatus() == 3) {
            advanceTotal += 1;
        }
        if (devicehistory1.getPm10status() > 0) {
            pm10chao += 1;
        }
        if (devicehistory1.getPm10status() == 1) {
            pm10primary += 1;
        } else if (devicehistory1.getPm10status() == 2) {
            pm10middle += 1;
        } else if (devicehistory1.getPm10status() == 3) {
            pm10advance += 1;
        }
        if (devicehistory1.getPm25status() > 0) {
            pm25chao += 1;
        }
        if (devicehistory1.getPm25status() == 1) {
            pm25primary += 1;
        } else if (devicehistory1.getPm25status() == 2) {
            pm25middle += 1;
        } else if (devicehistory1.getPm25status() == 3) {
            pm25advance += 1;
        }
        if (devicehistory1.getNoisestatus() > 0) {
            noisechao += 1;
        }
        if (devicehistory1.getNoisestatus() == 1) {
            noiseprimary += 1;
        } else if (devicehistory1.getNoisestatus() == 2) {
            noisemiddle += 1;
        } else if (devicehistory1.getNoisestatus() == 3) {
            noiseadvance += 1;
        }
        devicehistoryStatic1.setStatisticsTime(date);
        devicehistoryStatic1.setDevaddr(devaddr);
        if (devicehistoryStatic == null) {
            devicehistoryStatic1.setTotal(total);
            devicehistoryStatic1.setChaototal(chaototal);
            devicehistoryStatic1.setPrimaryTotal(primaryTotal);
            devicehistoryStatic1.setMiddleTotal(middleTotal);
            devicehistoryStatic1.setAdvanceTotal(advanceTotal);
            devicehistoryStatic1.setPm10chao(pm10chao);
            devicehistoryStatic1.setPm10primary(pm10primary);
            devicehistoryStatic1.setPm10middle(pm10middle);
            devicehistoryStatic1.setPm10advance(pm10advance);
            devicehistoryStatic1.setPm25chao(pm25chao);
            devicehistoryStatic1.setPm25primary(pm25primary);
            devicehistoryStatic1.setPm25middle(pm25middle);
            devicehistoryStatic1.setPm25advance(pm25advance);
            devicehistoryStatic1.setNoisechao(noisechao);
            devicehistoryStatic1.setNoiseprimary(noiseprimary);
            devicehistoryStatic1.setNoiseprimary(noiseprimary);
            devicehistoryStatic1.setNoisemiddle(noisemiddle);
            devicehistoryStatic1.setNoiseadvance(noiseadvance);
            devicehistoryStaticService.save(devicehistoryStatic1);
        } else {
            devicehistoryStatic1.setTotal(devicehistoryStatic.getTotal() + total);
            devicehistoryStatic1.setChaototal(devicehistoryStatic.getChaototal() + chaototal);
            devicehistoryStatic1.setPrimaryTotal(devicehistoryStatic.getPrimaryTotal() + primaryTotal);
            devicehistoryStatic1.setMiddleTotal(devicehistoryStatic.getMiddleTotal() + middleTotal);
            devicehistoryStatic1.setAdvanceTotal(devicehistoryStatic.getAdvanceTotal() + advanceTotal);
            devicehistoryStatic1.setPm10chao(devicehistoryStatic.getPm10chao() + pm10chao);
            devicehistoryStatic1.setPm10primary(devicehistoryStatic.getPm10primary() + pm10primary);
            devicehistoryStatic1.setPm10middle(devicehistoryStatic.getPm10middle() + pm10middle);
            devicehistoryStatic1.setPm10advance(devicehistoryStatic.getPm10advance() + pm10advance);
            devicehistoryStatic1.setPm25chao(devicehistoryStatic.getPm25chao() + pm25chao);
            devicehistoryStatic1.setPm25primary(devicehistoryStatic.getPm25primary() + pm25primary);
            devicehistoryStatic1.setPm25middle(devicehistoryStatic.getPm25middle() + pm25middle);
            devicehistoryStatic1.setPm25advance(devicehistoryStatic.getPm25advance() + pm25advance);
            devicehistoryStatic1.setNoisechao(devicehistoryStatic.getNoisechao() + noisechao);
            devicehistoryStatic1.setNoiseprimary(devicehistoryStatic.getNoiseprimary() + noiseprimary);
            devicehistoryStatic1.setNoiseprimary(devicehistoryStatic.getNoiseprimary() + noiseprimary);
            devicehistoryStatic1.setNoisemiddle(devicehistoryStatic.getNoisemiddle() + noisemiddle);
            devicehistoryStatic1.setNoiseadvance(devicehistoryStatic.getNoiseadvance() + noiseadvance);
            devicehistoryStatic1.setId(devicehistoryStatic.getId());
            devicehistoryStaticService.updateById(devicehistoryStatic1);
        }
    }
}
