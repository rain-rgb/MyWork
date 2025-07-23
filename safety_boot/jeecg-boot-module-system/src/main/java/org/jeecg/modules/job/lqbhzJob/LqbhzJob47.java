package org.jeecg.modules.job.lqbhzJob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.switchmachinePhone.entity.SwitchmachinePhone;
import com.trtm.iot.switchmachinePhone.service.ISwitchmachinePhoneService;
import com.trtm.iot.switchmachineStatistics.entity.SwitchingmachineStatistics;
import com.trtm.iot.switchmachineStatistics.service.ISwitchingmachineStatisticsService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.LqJobUntil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LqbhzJob47 implements Job {
    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IBhzLqBasesService bhzLqBasesService;

    @Autowired
    private IBhzCallCfgService bhzCallCfgService;

    @Autowired
    private IBhzChaobiaoCfgService bhzChaobiaoCfgService;

    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    private IBhzLqCailiaoService bhzLqCailiaoService;

    @Autowired
    private LqJobUntil lqJobUntil;

    @Autowired
    private IBhzPhoneService bhzPhoneService;

    @Autowired
    private ISysMessageService sysMessageService;
    @Autowired
    private ISwitchingmachineStatisticsService switchingmachineStatisticsService;
    @Autowired
    private ISwitchmachinePhoneService switchmachinePhoneService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LQBHZ_CFG);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到沥青拌合站定时任务配置信息" + DateUtils.now()));
            return;
        }

        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(extra);
        List<BhzLqBases> selectlqbhzones = bhzLqBasesService.selectlqbhzones(curid, 0);
        if (null == selectlqbhzones || selectlqbhzones.size() == 0) {
            log.info(String.format("暂无沥青拌合站未判断的数据"));
            return;
        }

        int id = 0;
        for (BhzLqBases selectlqbhzone : selectlqbhzones) {
            if (selectlqbhzone.getShebeibianhao().equals("ydlm1blq_01") && (!selectlqbhzone.getHunheliaoid().equals("33") && !selectlqbhzone.getHunheliaoid().equals("34") && !selectlqbhzone.getHunheliaoid().equals("36") && !selectlqbhzone.getHunheliaoid().equals("75"))) {
                continue;
            }
            id = selectlqbhzone.getId();
            Integer is_call = 0; //是否报警  1不报警 0报警
            Integer wenduchaobiao = 0;
            Integer stir_datetime_design = 120; //默认搅拌时间预警值
            Integer timeLevel = 0; //如果不超标只返回搅拌时间超标级别，如果超标的话会多返回超标了多少时间
            String primaryUid = "";//BHZ_CALL_CFG 的初级超标电话号配置
            String middleUid = "";//BHZ_CALL_CFG 的中级超标电话号配置
            String advancedUid = "";//BHZ_CALL_CFG 的高级超标电话号配置
            String wenduyujingUid = "";//BHZ_CALL_CFG 的温度预警电话号配置
            String primaryPhones = "";//BHZ_CALL_CFG 的初级超标电话号
            String middlePhones = "";//BHZ_CALL_CFG 的中级超标电话号
            String advancedPhones = "";//BHZ_CALL_CFG 的高级超标电话号
            String wenduyujingPhones = "";//BHZ_CALL_CFG 的温度电话号
            String primaryNames = "";//BHZ_CALL_CFG 的初级超标电话号接收人
            String middleNames = "";//BHZ_CALL_CFG 的中级超标电话号接收人
            String advancedNames = "";//BHZ_CALL_CFG 的高级超标电话号接收人
            Integer banheTime = 0;
            StringBuilder final_content = new StringBuilder();
            int final_over_level = 0;
            int final_time_over_level = 0;
            //设备编号
            String shebeibianhao = selectlqbhzone.getShebeibianhao();
            //唯一标识
            String guid = selectlqbhzone.getGuid();
            //施工部位
            String shigongbuwei = selectlqbhzone.getPoureLocation();
            //强度等级
            String qddj = selectlqbhzone.getStrengthRank();
            //理论油石比
            String llysb = selectlqbhzone.getLlysb();
            //搅拌时间
            int banheshijian = selectlqbhzone.getBanheshijian();
            //总产量
            double zongchanliang = selectlqbhzone.getZongchanliang();
            //出料时间
            String chuliaoshijian = selectlqbhzone.getChuliaoshijian();
            try {
                BhzCallCfg selectbhzcallone = bhzCallCfgService.selectbhzcallone(shebeibianhao);
                // 拌合站各材料预警阈值
                List<BhzChaobiaoCfg> selectchaobiaolist = bhzChaobiaoCfgService.selectchaobiaolist(shebeibianhao,selectbhzcallone.getUid());
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
                //判断是否有设备
//                if (null == selectshebeione){
//                    log.info(String.format("暂无沥青拌合站数据"+DateUtils.now()));
//                    return;
//                }
                //根据唯一id查询沥青材料表
                List<BhzLqCailiao> selectcailiaolist = bhzLqCailiaoService.selectcailiaolist(guid);
                if (selectcailiaolist.size() == 0) {
                    bhzLqBasesService.updatelqbhzone(guid, 40);
                    log.info(String.format("暂无拌合站材料信息" + DateUtils.now()));
                    return;
                }
                if (null != selectbhzcallone) {
                    is_call = selectbhzcallone.getIsCall();//是否报警
                }
                //判断搅拌时间是否超标
//                if (null != selectbhzcallone && null != selectcailiaolist && null != selectbhzcallone.getStirDatetimeDesign()){
//                    stir_datetime_design = selectbhzcallone.getStirDatetimeDesign();  //搅拌时间预警值
//                    Map map = jobUtil.HNTJboverproof(banheshijian, stir_datetime_design, selectchaobiaolist);
//                    timeLevel = (Integer) map.get("TimeLevel");
//                }else {  //如果配置表为空就使用默认标准时间进行判断
//                    Map map = jobUtil.HNTJboverproof(banheshijian, stir_datetime_design, null);
//                    timeLevel = (Integer) map.get("TimeLevel");
//                }


                //相对对应的超标
                if (null != selectbhzcallone) {
                    if (null != selectbhzcallone.getPrimaryPhones()) {
                        primaryUid = selectbhzcallone.getPrimaryPhones();
                    }
                    if (null != selectbhzcallone.getMiddlePhones()) {
                        middleUid = selectbhzcallone.getMiddlePhones();
                    }
                    if (null != selectbhzcallone.getAdvancedPhones()) {
                        advancedUid = selectbhzcallone.getAdvancedPhones();
                    }
                    if (null != selectbhzcallone.getWenduyujingPhones()) {
                        wenduyujingUid = selectbhzcallone.getWenduyujingPhones();
                    }
                }
                if (StrUtil.isNotBlank(primaryUid)) {
                    BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(primaryUid);
                    if (null != bhzPhone) {
                        primaryPhones = bhzPhone.getPhones();
                        primaryNames = bhzPhone.getNames();
                    }
                }
                if (StrUtil.isNotBlank(middleUid)) {
                    BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(middleUid);
                    if (null != bhzPhone) {
                        middlePhones = bhzPhone.getPhones();
                        middleNames = bhzPhone.getNames();
                    }
                }
                if (StrUtil.isNotBlank(advancedUid)) {
                    BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(advancedUid);
                    if (null != bhzPhone) {
                        advancedPhones = bhzPhone.getPhones();
                        advancedNames = bhzPhone.getNames();
                    }
                }
                if (StrUtil.isNotBlank(wenduyujingUid)) {
                    BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(wenduyujingUid);
                    if (null != bhzPhone) {
                        wenduyujingPhones = bhzPhone.getPhones();
                    }
                }

                //判断材料超标------------------------沥青材料表------------材料超标预警阈值------是否报警---设备编号----lqbhzguid--总产量----------沥青设备表信息-----lq拌合生成数据----出料时间
                Map map = lqJobUntil.lqCailiaoOver47(selectcailiaolist, selectchaobiaolist, is_call, shebeibianhao, guid, zongchanliang, selectshebeione, selectlqbhzone, chuliaoshijian);
                int level = (int) map.get("level");
                String hhllx = String.valueOf(map.get("hhllx"));
                final_content.append(map.get("final_content"));
                if (level > final_over_level) {
                    final_over_level = level;
                }

                // 判断温度是否超标
                if (null != selectbhzcallone && hhllx != null) {
                    if (StringUtils.isNotBlank(selectbhzcallone.getClwdtp()) && Integer.parseInt(hhllx) >= 50) {
                        wenduchaobiao = lqJobUntil.lqtemperatureOver(selectbhzcallone.getClwdtp(), selectlqbhzone.getChuliaowd());
                        banheTime = 45;
                    }
                    if (StringUtils.isNotBlank(selectbhzcallone.getClwdgx()) && Integer.parseInt(hhllx) < 50) {
                        wenduchaobiao = lqJobUntil.lqtemperatureOver(selectbhzcallone.getClwdgx(), selectlqbhzone.getChuliaowd());
                        banheTime = 60;
                    }
                }

                if (null != selectbhzcallone && null != selectcailiaolist && null != selectbhzcallone.getStirDatetimeDesign()) {
                    stir_datetime_design = selectbhzcallone.getStirDatetimeDesign();
                    lqJobUntil.lqTimeOver(banheshijian, stir_datetime_design, guid);
                }

                SwitchingmachineStatistics statistics = switchingmachineStatisticsService.getones(shebeibianhao);
                List<SwitchmachinePhone> switchmachinePhone = switchmachinePhoneService.getons(shebeibianhao);
                int iscall = 0;
                String phones = "";
                String phonenum = "";
                if (switchmachinePhone.size() > 0) {
                    iscall = switchmachinePhone.get(0).getIsCall();
                    phones = switchmachinePhone.get(0).getYujingPhones();
                }
                if (StrUtil.isNotBlank(phones)) {
                    BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(phones);
                    if (null != bhzPhone) {
                        phonenum = bhzPhone.getPhones();
                    }
                }
                if (null == statistics) {
                    SwitchingmachineStatistics statistics1 = new SwitchingmachineStatistics();
                    statistics1.setChuliaoshijian(chuliaoshijian);
                    statistics1.setShebeiNo(shebeibianhao);
                    statistics1.setStatus(1);
                    statistics1.setKaijishijian(chuliaoshijian);
                    switchingmachineStatisticsService.save(statistics1);
                    if (null != switchmachinePhone) {
                        JSONObject obj = new JSONObject();
                        obj.put("sbname", selectshebeione.getSbname());
                        obj.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        obj.put("content", "该设备已开机");
                        if (!"".equals(phonenum)) {
                            send(iscall, phonenum, obj, shebeibianhao);
                        }
                    }
                } else {
                    int status = statistics.getStatus();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String chuliaoshijian1 = statistics.getChuliaoshijian();
                    Date date = format.parse(chuliaoshijian1);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.MINUTE, 10);
                    date = cal.getTime();
                    String format1 = format.format(date);
                    Date times = format.parse(format1);//开关机的出料时间加间隔时间（60分钟）
                    Date parse = format.parse(chuliaoshijian);//最新一条数据的出料时间
                    Date sysdate = DateUtil.date();//系统时间
                    if (status == 2) {//关机
                        JSONObject obj = new JSONObject();
                        obj.put("sbname", selectshebeione.getSbname());
                        obj.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        obj.put("content", "该设备已开机");
                        if (parse.after(times)) {//最新一条数据的时间大于开关机表中的时间+上间隔时间那么久发送开机短信
                            statistics.setStatus(1);
                            statistics.setChuliaoshijian(chuliaoshijian);
                            statistics.setKaijishijian(chuliaoshijian);
                            statistics.setJieshushijian("");
                            switchingmachineStatisticsService.updateById(statistics);
                            if (!"".equals(phonenum)) {
                                send(iscall, phonenum, obj, shebeibianhao);
                            }
                        }
                    }
                    if (status == 1) {//开机
                        statistics.setChuliaoshijian(chuliaoshijian);
                        switchingmachineStatisticsService.updateById(statistics);
                    }
                }

                JSONObject obj = new JSONObject();
                if (selectshebeione == null) {
                    obj.put("sbname", "平台未注册设备");
                } else {
                    obj.put("sbname", selectshebeione.getSbname());
                }
                obj.put("time", chuliaoshijian);

                if (!wenduyujingPhones.equals("") && wenduchaobiao > 0) {
                    SysMessage sysMessage2 = new SysMessage();
                    sysMessage2.setEsTitle("沥青拌合站温度超标检测");
                    sysMessage2.setEsType("1");//短信类型  1短信
                    sysMessage2.setEsReceiver(wenduyujingPhones);//
                    if (wenduchaobiao == 1) {
                        obj.put("content", "温度" + selectlqbhzone.getChuliaowd() + "℃预警");
                    } else {
                        obj.put("content", "温度" + selectlqbhzone.getChuliaowd() + "℃报废");
                    }
                    sysMessage2.setEsContent(obj.toString());//短信内容

                    if (is_call == 0) {
                        sysMessage2.setEsSendStatus("0");//推送状态0未推送
                        sysMessage2.setRemark(shebeibianhao);
                        sysMessage2.setEsSendNum(0);//推送总次数
                    } else {
                        sysMessage2.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage2.setEsSendNum(0);//推送总次数
                        sysMessage2.setRemark(shebeibianhao);
                        sysMessage2.setEsResult("不需要推送");
                    }
                    sysMessage2.setCreateTime(new Date());
                    sysMessageService.save(sysMessage2);
                }

                if (0 == final_over_level || final_content.length() == 0) {
                    bhzLqCailiaoService.updateOver_level(id, final_over_level);
                    bhzLqBasesService.updateBaseStatus(id, 1);
                    lqJobUntil.Sattistics(selectlqbhzone, bhzLqCailiaoService.selectByMainId(guid), final_over_level, shebeibianhao, hhllx);
                    continue;
                }

                // 材料超标
                obj.put("time", chuliaoshijian);
                obj.put("content", final_content);

                //
                if (!primaryPhones.equals("") && final_over_level == 1) {
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setEsTitle("沥青拌合站初级超标检测");
                    sysMessage.setEsType("1");//短信类型  1短信
                    sysMessage.setEsReceiver(primaryPhones);//手机号
                    sysMessage.setEsContent(obj.toString());//短信内容
                    if (is_call == 0) {
                        sysMessage.setEsSendStatus("0");//推送状态0未推送
                        sysMessage.setRemark(shebeibianhao);
                        sysMessage.setEsSendNum(0);//推送总次数
                    } else {
                        sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage.setEsSendNum(0);//推送总次数
                        sysMessage.setRemark(shebeibianhao);
                        sysMessage.setEsResult("不需要推送");
                    }
                    sysMessage.setCreateTime(new Date());
                    sysMessageService.save(sysMessage);
                }
                if (!middlePhones.equals("") && final_over_level == 2) {
                    SysMessage sysMessage1 = new SysMessage();
                    sysMessage1.setEsTitle("沥青拌合站中级超标检测");
                    sysMessage1.setEsType("1");//短信类型  1短信
                    sysMessage1.setEsReceiver(middlePhones);//手机号
                    sysMessage1.setEsContent(obj.toString());//短信内容
                    if (is_call == 0) {
                        sysMessage1.setEsSendStatus("0");//推送状态0未推送
                        sysMessage1.setEsSendNum(0);//推送总次数
                        sysMessage1.setRemark(shebeibianhao);
                    } else {
                        sysMessage1.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage1.setEsSendNum(0);//推送总次数
                        sysMessage1.setRemark(shebeibianhao);
                        sysMessage1.setEsResult("不需要推送");
                    }
                    sysMessage1.setCreateTime(new Date());
                    sysMessageService.save(sysMessage1);
                }
                if (!advancedPhones.equals("") && final_over_level == 3) {
                    SysMessage sysMessage2 = new SysMessage();
                    sysMessage2.setEsTitle("沥青拌合站高级超标检测");
                    sysMessage2.setEsType("1");//短信类型  1短信
                    sysMessage2.setEsReceiver(advancedPhones);//手机号
                    sysMessage2.setEsContent(obj.toString());//短信内容
                    if (is_call == 0) {
                        sysMessage2.setEsSendStatus("0");//推送状态0未推送
                        sysMessage2.setRemark(shebeibianhao);
                        sysMessage2.setEsSendNum(0);//推送总次数
                    } else {
                        sysMessage2.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage2.setEsSendNum(0);//推送总次数
                        sysMessage2.setRemark(shebeibianhao);
                        sysMessage2.setEsResult("不需要推送");
                    }
                    sysMessage2.setCreateTime(new Date());
                    sysMessageService.save(sysMessage2);
                }


                BhzLqBases bhzLqBases = new BhzLqBases();
                bhzLqBases.setId(id);
                bhzLqBases.setAlertstate(1);
                bhzLqBases.setChaobiaodengji(final_over_level);
                bhzLqBases.setTimechaobiao(final_time_over_level);
                bhzLqBases.setWenduchaobiao(wenduchaobiao);
                bhzLqBases.setBanheshijian(banheTime);
                bhzLqBases.setOverReason(String.valueOf(final_content));
                bhzLqBasesService.updateById(bhzLqBases);  //拌合站修改超标等级以及拌合时间超标登记以及检测过状态
                lqJobUntil.Sattistics(selectlqbhzone, bhzLqCailiaoService.selectByMainId(guid), final_over_level, shebeibianhao, hhllx);
            } catch (Exception e) {
                e.printStackTrace();
                bhzLqBasesService.updatelqbhzone(guid, 40);
            }
            log.info(String.format("沥青拌合站超标表检测！   时间" + DateUtils.now()));
            sysConfigService.updateSysConfig(JobUtil.LQBHZ_CFG, id);
        }
    }

    private void send(int iscall, String phonenum, JSONObject obj, String shebeibianhao) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setEsTitle("拌合站开关机提醒");
        sysMessage.setEsType("1");
        sysMessage.setRemark(shebeibianhao);
        sysMessage.setEsReceiver(phonenum);
        sysMessage.setEsSendNum(0);
        sysMessage.setEsContent(obj.toString());
        if (iscall == 0) {
            sysMessage.setEsSendStatus("0");//推送状态0未推送
        } else {
            sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
            sysMessage.setEsResult("不需要推送");
        }
        sysMessage.setCreateTime(new Date());
        sysMessageService.save(sysMessage);
    }
}
