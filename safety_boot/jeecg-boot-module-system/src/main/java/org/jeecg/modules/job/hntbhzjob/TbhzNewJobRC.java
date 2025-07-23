package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * \* @author: Xx
 * \* Date: 2022/4/3
 * \* Time: 14:09
 * \* Description: 砼拌合站超标检测定时任务 新数据
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TbhzNewJobRC implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzChaobiaoCfgService bhzChaobiaoCfgService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private JobUtil jobUtil;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HNTBHZ_CFG_NEW);//拌合站超标监测=48
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        Integer isworking = selectsysconfigone.getIsworking();
        Integer id1 = selectsysconfigone.getId();
        if(isworking==1){
            log.info(String.format("拌合站实时超标检测正在运行中！！" + DateUtils.now()));
            return;
        }
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selecthntbhzones(curid, 0);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无砼拌合站未判断的数据" + DateUtils.now()));
            return;
        }
        sysConfigService.updateSysConfigs(JobUtil.HNTBHZ_CFG_NEW, curid,1);
        int id=0;
        for (BhzCementBase selecthntbhzone : selecthntbhzones) {
            Integer is_call = 0; //是否报警  1不报警 0报警
            Integer stir_datetime_design = 120; //默认搅拌时间预警值
            Integer timeLevel = 0; //如果不超标只返回搅拌时间超标级别，如果超标的话会多返回超标了多少时间
            String primaryUid = "";//BHZ_CALL_CFG 的初级超标电话号配置
            String middleUid = "";//BHZ_CALL_CFG 的中级超标电话号配置
            String advancedUid = "";//BHZ_CALL_CFG 的高级超标电话号配置
            String primaryPhones = "";//BHZ_CALL_CFG 的初级超标电话号
            String middlePhones = "";//BHZ_CALL_CFG 的中级超标电话号
            String advancedPhones = "";//BHZ_CALL_CFG 的高级超标电话号
            String primaryNames = "";//BHZ_CALL_CFG 的初级超标电话号接收人
            String middleNames = "";//BHZ_CALL_CFG 的中级超标电话号接收人
            String advancedNames = "";//BHZ_CALL_CFG 的高级超标电话号接收人
            StringBuilder final_content = new StringBuilder();
            int final_over_level = 0;
            int final_time_over_level = 0;
            String batchNo = selecthntbhzone.getBatchNo();
            Integer statistics = selecthntbhzone.getStatistics();
            id = selecthntbhzone.getId();
            String shebeiNo = selecthntbhzone.getShebeiNo();
            Integer stirDatetime = selecthntbhzone.getStirDatetime();//搅拌时间
            String poure_location = StrUtil.nullToDefault(selecthntbhzone.getPoureLocation(), "");//浇筑部位
            String strength_rank = StrUtil.nullToDefault(selecthntbhzone.getStrengthRank(), "");//强度等级
            String product_datetime = DateUtil.format(selecthntbhzone.getProductDatetime(), "yyyy-MM-dd HH:mm:ss");//出料时间

            try {
                BhzCallCfg selectbhzcallone = bhzCallCfgService.selectbhzcallone(shebeiNo);//查询拌合站超标配置信息
                List<BhzChaobiaoCfg> selectchaobiaolist = bhzChaobiaoCfgService.selectchaobiaolist(shebeiNo,selectbhzcallone.getUid());
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
//                if (null == selectshebeione) {
//                    log.info(String.format("暂无砼拌合站的设备" + DateUtils.now()));
//                    continue;
//                }
                List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(batchNo);
                if (selectcementlist.size() == 0) {//如果为空呢 就把此数据更改为异常状态
                    bhzCementBaseService.updatehntbhzone(batchNo, 40);
                    BhzCementBase bhzCementBase = new BhzCementBase();
                    bhzCementBase.setId(selecthntbhzone.getId());
                    bhzCementBase.setOverLevel(0);
                    bhzCementBaseService.updateById(bhzCementBase);
                    log.info(String.format("暂无砼拌合站的材料信息" + DateUtils.now()));
                    continue;
                }
                if (null != selectbhzcallone) {
                    is_call = selectbhzcallone.getIsCall();//是否报警
                }
                //判断搅拌时间是否超标
                if (null != selectbhzcallone && null != selectchaobiaolist && null != selectbhzcallone.getStirDatetimeDesign()) {
                    stir_datetime_design = selectbhzcallone.getStirDatetimeDesign();//搅拌时间预警值
                    Map map = jobUtil.HNTJboverproof(stirDatetime, stir_datetime_design, selectchaobiaolist);
                    timeLevel = (Integer) map.get("TimeLevel");
                } else {
                    //如果配置表为空就使用默认标准时间进行判断
                    Map map = jobUtil.HNTJboverproof(stirDatetime, stir_datetime_design, null);
                    timeLevel = (Integer) map.get("TimeLevel");
                }
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
                //判断材料是否超标
                Map map = jobUtil.JudgeOverweight(selectcementlist, selectchaobiaolist, is_call, shebeiNo);
                int level = (int) map.get("level");//超标等级
                final_content.append(map.get("final_content"));//材料超标信息
                if (level > final_over_level) {
                    final_over_level = level;
                }
                if (0 == final_over_level || final_content.length() == 0) {
                    BhzCementBase bhzCementBase = new BhzCementBase();
                    bhzCementBase.setId(id);
                    bhzCementBase.setOverLevel(final_over_level);
                    bhzCementBase.setTimeOverLevel(final_time_over_level);
                    bhzCementBase.setAlertstate(1);
//                    if(statistics==0){
//                        jobUtil.Sattistics(selecthntbhzone, selectcementlist, final_over_level, shebeiNo);
//                        bhzCementBase.setStatistics(1);
//                    }
                    bhzCementBaseService.updateById(bhzCementBase);//拌合站修改超标等级以及拌合时间超标登记以及检测过状态
                    continue;
                }
                JSONObject obj = new JSONObject();
                if(selectshebeione==null){
                    obj.put("sbname", "平台未注册设备");
                }else{
                    obj.put("sbname", selectshebeione.getSbname());
                }
                obj.put("time", product_datetime);
                obj.put("content", final_content);

                if (!primaryPhones.equals("")&&final_over_level==1) {
                    SysMessage sysMessage = new SysMessage();
                    if (is_call == 0) {
                        sysMessage.setEsTitle("砼拌合站初级超标检测");
                        sysMessage.setEsType("1");//短信类型  1短信
                        sysMessage.setEsReceiver(primaryPhones);//手机号
                        sysMessage.setEsContent(obj.toString());//短信内容
                        sysMessage.setEsSendStatus("0");//推送状态0未推送
                        sysMessage.setEsSendNum(0);//推送总次数
                        sysMessage.setRemark(shebeiNo);
                        sysMessage.setCreateTime(new Date());
                    } else {
                        sysMessage.setEsTitle("砼拌合站初级超标检测");
                        sysMessage.setEsType("1");
                        sysMessage.setEsReceiver(primaryPhones);
                        sysMessage.setEsContent(obj.toString());
                        sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage.setEsSendNum(0);//推送总次数
                        sysMessage.setRemark(shebeiNo);
                        sysMessage.setEsResult("不需要推送");
                        sysMessage.setCreateTime(new Date());
                    }
                    sysMessageService.save(sysMessage);
                }
                if (!middlePhones.equals("")&&final_over_level==2) {
                    SysMessage sysMessage1 = new SysMessage();
                    if (is_call == 0) {
                        sysMessage1.setEsTitle("砼拌合站中级超标检测");
                        sysMessage1.setEsType("1");//短信类型  1短信
                        sysMessage1.setEsReceiver(middlePhones);//手机号
                        sysMessage1.setEsContent(obj.toString());//短信内容
                        sysMessage1.setEsSendStatus("0");//推送状态0未推送
                        sysMessage1.setEsSendNum(0);//推送总次数
                        sysMessage1.setRemark(shebeiNo);
                        sysMessage1.setCreateTime(new Date());
                    } else {
                        sysMessage1.setEsTitle("砼拌合站中级超标检测");
                        sysMessage1.setEsType("1");
                        sysMessage1.setEsReceiver(middlePhones);
                        sysMessage1.setEsContent(obj.toString());
                        sysMessage1.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage1.setEsSendNum(0);//推送总次数
                        sysMessage1.setRemark(shebeiNo);
                        sysMessage1.setEsResult("不需要推送");
                        sysMessage1.setCreateTime(new Date());
                    }
                    sysMessageService.save(sysMessage1);
                }
                if (!advancedPhones.equals("")&&final_over_level==3) {
                    SysMessage sysMessage2 = new SysMessage();
                    if (is_call == 0) {
                        sysMessage2.setEsTitle("砼拌合站高级超标检测");
                        sysMessage2.setEsType("1");//短信类型  1短信
                        sysMessage2.setEsReceiver(advancedPhones);//手机号
                        sysMessage2.setEsContent(obj.toString());//短信内容
                        sysMessage2.setEsSendStatus("0");//推送状态0未推送
                        sysMessage2.setEsSendNum(0);//推送总次数
                        sysMessage2.setRemark(shebeiNo);
                        sysMessage2.setCreateTime(new Date());
                    } else {
                        sysMessage2.setEsTitle("砼拌合站高级超标检测");
                        sysMessage2.setEsType("1");
                        sysMessage2.setEsReceiver(advancedPhones);
                        sysMessage2.setEsContent(obj.toString());
                        sysMessage2.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage2.setEsSendNum(0);//推送总次数
                        sysMessage2.setRemark(shebeiNo);
                        sysMessage2.setEsResult("不需要推送");
                        sysMessage2.setCreateTime(new Date());
                    }
                    sysMessageService.save(sysMessage2);
                }
                BhzCementBase bhzCementBase = new BhzCementBase();
                bhzCementBase.setId(id);
                bhzCementBase.setOverLevel(final_over_level);
                bhzCementBase.setTimeOverLevel(final_time_over_level);
                bhzCementBase.setOverReason(final_content.toString());
                if(!primaryPhones.equals("")&&final_over_level==1){
                    bhzCementBase.setOverproofStatus(20);
                }
                bhzCementBase.setAlertstate(1);
//                if(statistics==0){
//                    jobUtil.Sattistics(selecthntbhzone, selectcementlist, final_over_level, shebeiNo);//统计
//                    bhzCementBase.setStatistics(1);
//                }
                bhzCementBaseService.updateById(bhzCementBase);//拌合站修改超标等级以及拌合时间超标登记以及检测过状态
            } catch (Exception e) {
                e.printStackTrace();
                bhzCementBaseService.updatehntbhzone(batchNo, 40);
            }
            log.info(String.format("砼拌合站超标检测!   时间:" + DateUtils.now()));
        }
        sysConfigService.updateSysConfigs(JobUtil.HNTBHZ_CFG_NEW, id,0);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
