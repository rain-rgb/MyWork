package org.jeecg.modules.job.swbhzjob;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * \* @author: Xx
 * \* Date: 2021/3/29
 * \* Time: 15:32
 * \* Description:水稳拌合站超标检测判断
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SwbhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzChaobiaoCfgService bhzChaobiaoCfgService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzSwCailiaoService bhzSwCailiaoService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private ISysMessageService sysMessageService;//消息

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SWBHZ_CFG);//水稳拌合站超标监测=6
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到水稳拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<BhzSwBases> selectswbhzone = bhzSwBasesService.selectswbhzones(curid, 0);
        if (null == selectswbhzone || selectswbhzone.size() == 0) {
            log.info(String.format("暂无水稳拌合站未判断的数据" + DateUtils.now()));
            return;
        }

        for (BhzSwBases bhzSwBases : selectswbhzone) {
            Integer is_call = 0; //是否报警  1不报警 0报警
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
            String guid = bhzSwBases.getGuid();//唯一标识
            BhzSwBases bhzSwBases2 = new BhzSwBases();
            bhzSwBases2.setGuid(guid);
            bhzSwBases2.setAlertstate(1);
            bhzSwBasesService.updateByGuid(bhzSwBases2);
            //总产量
            float zongchanliang = Convert.toFloat(bhzSwBases.getZongchanliang(), 0f);
            BigDecimal bg5 = new BigDecimal(zongchanliang);
            Double zongchanliang2 = bg5.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String shebeibianhao = bhzSwBases.getShebeibianhao();//设备编号

            String shigongbuwei = bhzSwBases.getPoureLocation(); //施工部位
            String qddj = bhzSwBases.getStrengthRank();//强度等级
            String chuliaoshijian = bhzSwBases.getChuliaoshijian();//出料时间
            try {
                BhzCallCfg selectbhzcallone = bhzCallCfgService.selectbhzcallone(shebeibianhao);//查询拌合站超标配置信息
                List<BhzChaobiaoCfg> selectchaobiaolist = bhzChaobiaoCfgService.selectchaobiaolist(shebeibianhao,selectbhzcallone.getUid());//查询当前设备的超标配置
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);//查询当前的设备
                // 水稳拌合站 短信 设备开关机 判断
                jobUtil.switchMessage(bhzSwBases.getChuliaoshijian(),shebeibianhao,selectshebeione.getSbname()) ;
//                if (null == selectshebeione) {
//                    log.info(String.format("暂无水稳拌合站的设备" + DateUtils.now()));
//                    continue;
//                }
                List<BhzSwCailiao> selectswbhzcailiao = bhzSwCailiaoService.selectswbhzcailiao(guid);
                if (selectswbhzcailiao.size() == 0) {//如果为空呢 就把此数据更改为异常状态
                    log.info("水稳拌和站超标统计跳过,无子表数据：" + guid);
                    bhzSwBasesService.updateswbhzone(guid, 40);// 修改状态为异常
                    continue;
                }
                if (null != selectbhzcallone) {
                    is_call = selectbhzcallone.getIsCall();//是否报警
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
                //材料超标判断
                Map map = jobUtil.JudgeMaterial(bhzSwBases,selectswbhzcailiao, selectchaobiaolist, is_call, shebeibianhao, guid, zongchanliang);
                selectswbhzcailiao = bhzSwCailiaoService.selectswbhzcailiao(guid);//修改了selectswbhzcailiao中的超标等级，需要重新查询
                int level = (int) map.get("level");
                final_content.append(map.get("final_content"));
                final_over_level = level;
                Double guliao = (Double) map.get("guliao");
                //BigDecimal bg6 = new BigDecimal(gulia);
                //Double guliao = bg6.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                Double fenliao = 0.0;
                if (zongchanliang2 > 0) {
                    fenliao = zongchanliang2 - guliao;
                }
                if (0 == final_over_level || final_content.length() == 0) {
                    bhzSwBasesService.updateswbhzdengji(guid, final_over_level);//修改主表超标等级
                    bhzSwBasesService.updateswbhzone(guid, 1);// 修改状态为检测过
                    //材料，盘数 统计
                    jobUtil.swSattistics(bhzSwBases, selectswbhzcailiao, final_over_level, shebeibianhao);
                    continue;
                }
                JSONObject obj = new JSONObject();
                if(selectshebeione==null){
                    obj.put("sbname", "平台未注册此设备");
                }else{
                    obj.put("sbname", selectshebeione.getSbname());
                }

                obj.put("time", chuliaoshijian);
                obj.put("content", final_content);
                if (!primaryPhones.equals("") && level == 1) {
                    SysMessage sysMessage = new SysMessage();
                    if (is_call == 0) {
                        sysMessage.setEsTitle("水稳拌合站初级超标检测");
                        sysMessage.setEsType("1");//短信类型  1短信
                        sysMessage.setEsReceiver(primaryPhones);//手机号
                        sysMessage.setEsContent(obj.toString());//短信内容
                        sysMessage.setEsSendStatus("0");//推送状态0未推送
                        sysMessage.setRemark(shebeibianhao);
                        sysMessage.setEsSendNum(0);//推送总次数
                        sysMessage.setCreateTime(new Date());
                    } else {
                        sysMessage.setEsTitle("水稳拌合站初级超标检测");
                        sysMessage.setEsType("1");
                        sysMessage.setEsReceiver(primaryPhones);
                        sysMessage.setEsContent(obj.toString());
                        sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage.setEsSendNum(0);//推送总次数
                        sysMessage.setRemark(shebeibianhao);
                        sysMessage.setEsResult("不需要推送");
                        sysMessage.setCreateTime(new Date());
                    }
                    sysMessageService.save(sysMessage);
                }
                if (!middlePhones.equals("") && level == 2) {
                    SysMessage sysMessage1 = new SysMessage();
                    if (is_call == 0) {
                        sysMessage1.setEsTitle("水稳拌合站中级超标检测");
                        sysMessage1.setEsType("1");//短信类型  1短信
                        sysMessage1.setEsReceiver(middlePhones);//手机号
                        sysMessage1.setEsContent(obj.toString());//短信内容
                        sysMessage1.setEsSendStatus("0");//推送状态0未推送
                        sysMessage1.setRemark(shebeibianhao);
                        sysMessage1.setEsSendNum(0);//推送总次数
                        sysMessage1.setCreateTime(new Date());
                    } else {
                        sysMessage1.setEsTitle("水稳拌合站中级超标检测");
                        sysMessage1.setEsType("1");
                        sysMessage1.setEsReceiver(middlePhones);
                        sysMessage1.setEsContent(obj.toString());
                        sysMessage1.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage1.setEsSendNum(0);//推送总次数
                        sysMessage1.setRemark(shebeibianhao);
                        sysMessage1.setEsResult("不需要推送");
                        sysMessage1.setCreateTime(new Date());
                    }
                    sysMessageService.save(sysMessage1);
                }
                if (!advancedPhones.equals("") && level == 3) {
                    SysMessage sysMessage2 = new SysMessage();
                    if (is_call == 0) {
                        sysMessage2.setEsTitle("水稳拌合站高级超标检测");
                        sysMessage2.setEsType("1");//短信类型  1短信
                        sysMessage2.setEsReceiver(advancedPhones);//手机号
                        sysMessage2.setEsContent(obj.toString());//短信内容
                        sysMessage2.setEsSendStatus("0");//推送状态0未推送
                        sysMessage2.setRemark(shebeibianhao);
                        sysMessage2.setEsSendNum(0);//推送总次数
                        sysMessage2.setCreateTime(new Date());
                    } else {
                        sysMessage2.setEsTitle("水稳拌合站高级超标检测");
                        sysMessage2.setEsType("1");
                        sysMessage2.setEsReceiver(advancedPhones);
                        sysMessage2.setEsContent(obj.toString());
                        sysMessage2.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage2.setEsSendNum(0);//推送总次数
                        sysMessage2.setRemark(shebeibianhao);
                        sysMessage2.setEsResult("不需要推送");
                        sysMessage2.setCreateTime(new Date());
                    }
                    sysMessageService.save(sysMessage2);
                }



                BhzSwBases bhzSwBases1 = new BhzSwBases();
                bhzSwBases1.setGuid(guid);
                bhzSwBases1.setChaobiaodengji(final_over_level);
                bhzSwBases1.setOverReason(final_content.toString());
                bhzSwBasesService.updateByGuid(bhzSwBases1);// 修改状态为已经进行了超标检测和是否超标
                jobUtil.swSattistics(bhzSwBases, selectswbhzcailiao, final_over_level, shebeibianhao);//统计
            } catch (Exception e) {
                e.printStackTrace();
                bhzSwBasesService.updateswbhzone(guid, 40);
            }
            log.info(String.format("水稳拌合站超标检测!   时间:" + DateUtils.now()));
        }
        sysConfigService.updateSysConfig(JobUtil.SWBHZ_CFG, 0);//根据传过来的唯一值来修改当前判断到的数据id

    }
}
