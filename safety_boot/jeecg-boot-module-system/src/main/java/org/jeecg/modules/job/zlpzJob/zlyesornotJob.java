package org.jeecg.modules.job.zlpzJob;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglaconfigure.entity.TrZhanglaConfigure;
import com.trtm.iot.trzhanglaconfigure.service.ITrZhanglaConfigureService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * 张拉超标检测定时任务
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zlyesornotJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaSService;
    @Autowired
    private ITrZhanglaConfigureService zhanglaConfigureService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZL_CBJC);//张拉超标检测
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到张拉超标检测定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<TrZhanglaXxb> zhanglalist = zhanglaXxbService.selectbigid(curid);
        int id = 0;
        Double elongationChu = 0.0;
        Double elongationZhong = 0.0;
        Double elongationGao = 0.0;
        Double tensionChu = 0.0;
        Double tensionZhong = 0.0;
        Double tensionGao = 0.0;
        if (zhanglalist.size() != 0){
            for (TrZhanglaXxb zhanglaXxb :zhanglalist){
                id = zhanglaXxb.getId();
                String shebeibianhao = zhanglaXxb.getShebeibianhao();
                String syjid = zhanglaXxb.getSyjid();
                List<TrZhanglaM> trZhanglaMS = zhanglaMService.selectmList(syjid);
                if (trZhanglaMS.size() > 0){
                    // 查询配置表，如果没有配置则直接不判断超标
                    TrZhanglaConfigure trZhanglaConfigure = zhanglaConfigureService.selectbyshebei(shebeibianhao);
                    if (trZhanglaConfigure == null){
                        continue;
                    }
                    for (TrZhanglaM zhanglaM :trZhanglaMS){
                        String fguid = zhanglaM.getFguid();
                        double zscl = 0;//总伸长量
                        double llscl = 0;//理论伸长量
                        try {
                            zscl = parseDouble(zhanglaM.getZscl());
                            llscl = parseDouble(zhanglaM.getLlscl());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        double v = (zscl - llscl) * 100 / llscl;
                        double a = Math.abs(v);
                        double sjzll = 0.0;
                        double jdzll = 0.0;
                        try {
                            sjzll = Double.parseDouble(zhanglaM.getSjzll());//设计张拉力
                            List<TrZhanglaS> trZhanglaS = zhanglaSService.selelctList(syjid, fguid);
                            if (trZhanglaS.size() > 0){
                                for (TrZhanglaS trZhangla :trZhanglaS){
                                    if (parseDouble(trZhangla.getJdzll()) > jdzll){
                                        jdzll = parseDouble(trZhangla.getJdzll());//最大张拉力
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (jdzll == 0.0){
                            continue;
                        }
                        double v1 = (jdzll - sjzll) * 100 / sjzll;
                        double b = Math.abs(v1);
                        Double abs = (double) (Math.round(a * 100) / 100);
                        Double abs1 = (double) (Math.round(b * 100) / 100);
                        // 伸长量超标判断
                        if (trZhanglaConfigure.getAlertOrNot().equals("1")){
                            continue;
                        }
                        //伸长量，初中高
                        elongationChu = trZhanglaConfigure.getElongationChu();
                        elongationZhong = trZhanglaConfigure.getElongationZhong();
                        elongationGao = trZhanglaConfigure.getElongationGao();
                        //张拉力，初中高
                        tensionChu = trZhanglaConfigure.getTensionChu();
                        tensionZhong = trZhanglaConfigure.getTensionZhong();
                        tensionGao = trZhanglaConfigure.getTensionGao();
                        if (abs < elongationChu){
                            if (abs1 < tensionChu){
                                zhanglaXxb.setOverLevel(0);
                                zhanglaM.setOverLevel(0);
                                zhanglaM.setHege("1");
                            }else if (abs1 < tensionZhong){
                                zhanglaXxb.setOverLevel(1);
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"张拉力初级预警，超"+Math.round(abs1*100)/100+"%");
                                zhanglaM.setOverLevel(1);
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("张拉力初级预警，超"+Math.round(abs1*100)/100+"%");
                            }else if (abs1 < tensionGao){
                                zhanglaXxb.setOverLevel(2);
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"张拉力中级预警，超"+Math.round(abs1*100)/100+"%");
                                zhanglaM.setOverLevel(2);
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("张拉力中级预警，超"+Math.round(abs1*100)/100+"%");
                            }else {
                                zhanglaXxb.setOverLevel(3);
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"张拉力高级预警，超"+Math.round(abs1*100)/100+"%");
                                zhanglaM.setOverLevel(3);
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("张拉力高级预警，超"+Math.round(abs1*100)/100+"%");
                            }
                        }else if (abs < elongationZhong){
                            zhanglaXxb.setOverLevel(1);
                            zhanglaM.setOverLevel(1);
                            if (abs1 < tensionChu){
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量初级预警，超"+abs+"%");
                                zhanglaM.setOverReason("伸长量初级预警，超"+abs+"%");
                                zhanglaM.setHege("0");
                            }else if (abs1 < tensionZhong){
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量初级预警，超"+abs+"% | 张拉力初级预警，超"+abs1+"%");
                                zhanglaM.setOverReason("伸长量初级预警，超"+abs+"% | 张拉力初级预警，超"+abs1+"%");
                                zhanglaM.setHege("0");
                            }else if (abs1 < tensionGao){
                                zhanglaXxb.setOverLevel(2);
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量初级预警，超"+abs+"% | 张拉力中级预警，超"+abs1+"%");
                                zhanglaM.setOverLevel(2);
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量初级预警，超"+abs+"% | 张拉力中级预警，超"+abs1+"%");
                            }else {
                                zhanglaXxb.setOverLevel(3);
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量初级预警，超"+abs+"% | 张拉力高级预警，超"+abs1+"%");
                                zhanglaM.setOverLevel(3);
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量初级预警，超"+abs+"% | 张拉力高级预警，超"+abs1+"%");
                            }
                        }else if (abs < elongationGao){
                            zhanglaXxb.setOverLevel(2);
                            zhanglaM.setOverLevel(2);
                            if (abs1 < tensionChu){
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量中级预警，超"+abs+"%");
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量中级预警，超"+abs+"%");
                            }else if (abs1 < tensionZhong){
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量中级预警，超"+abs+"% | 张拉力初级预警，超"+abs1+"%");
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量中级预警，超"+abs+"% | 张拉力初级预警，超"+abs1+"%");
                            }else if (abs1 < tensionGao){
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量中级预警，超"+abs+"% | 张拉力中级预警，超"+abs1+"%");
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量中级预警，超"+abs+"% | 张拉力中级预警，超"+abs1+"%");
                            }else {
                                zhanglaXxb.setOverLevel(3);
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量中级预警，超"+abs+"% | 张拉力高级预警，超"+abs1+"%");
                                zhanglaM.setOverLevel(3);
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量中级预警，超"+abs+"% | 张拉力高级预警，超"+abs1+"%");
                            }
                        }else {
                            zhanglaXxb.setOverLevel(3);
                            zhanglaM.setOverLevel(3);
                            if (abs1 < tensionChu){
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量高级预警，超"+abs+"%");
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量高级预警，超"+abs+"%");
                            }else if (abs1 < tensionZhong){
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量高级预警，超"+abs+"% | 张拉力初级预警，超"+abs1+"%");
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量高级预警，超"+abs+"% | 张拉力初级预警，超"+abs1+"%");
                            }else if (abs1 < tensionGao){
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量高级预警，超"+abs+"% | 张拉力中级预警，超"+abs1+"%");
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量高级预警，超"+abs+"% | 张拉力中级预警，超"+abs1+"%");
                            }else {
                                zhanglaXxb.setOverReason(zhanglaM.getGsbh()+"伸长量高级预警，超"+abs+"% | 张拉力高级预警，超"+abs1+"%");
                                zhanglaM.setHege("0");
                                zhanglaM.setOverReason("伸长量高级预警，超"+abs+"% | 张拉力高级预警，超"+abs1+"%");
                            }
                        }
                        //发送短信
                        if (zhanglaXxb.getOverLevel() > 0) {
                            if (trZhanglaConfigure.getYesornot() == 0) {
                                messages(trZhanglaConfigure,zhanglaXxb);
                            }
                        }
                        // 更新m表
                        zhanglaMService.updateById(zhanglaM);
                        // 更新xxb表
                        QueryWrapper<TrZhanglaXxb> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id",id);
                        TrZhanglaXxb one = zhanglaXxbService.getOne(queryWrapper);
                        String l = null;
                        if (one.getOverReason() != null){
                            if (zhanglaXxb.getOverReason() != null){
                                String overReason = one.getOverReason();
                                String overReason1 = zhanglaXxb.getOverReason();
                                l = overReason + "|" + overReason1;
                                zhanglaXxb.setOverReason(l);
                                zhanglaXxbService.updateById(zhanglaXxb);
                            }
                        }else {
                            zhanglaXxbService.updateById(zhanglaXxb);
                        }
                    }
                }else {
                    log.info(String.format("未获取到张拉m表的配置信息" + DateUtils.now()));
                }
                sysConfigService.updateSysConfig(JobUtil.ZL_CBJC, id);//根据传过来的唯一值来修改当前判断到的数据id
            }
        }
    }

    private void messages(TrZhanglaConfigure trZhanglaConfigure,TrZhanglaXxb zhanglaXxb){
        JSONObject obj = new JSONObject();
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(trZhanglaConfigure.getShebeiNo());
        BhzPhone bhzPhone = null;
        if (selectshebeione != null){
            if (zhanglaXxb.getOverLevel() == 1){
                bhzPhone = bhzPhoneService.selectBhzPhone(trZhanglaConfigure.getNamescj());
            }else if (zhanglaXxb.getOverLevel() == 2){
                bhzPhone = bhzPhoneService.selectBhzPhone(trZhanglaConfigure.getNameszj());
            }else {
                bhzPhone = bhzPhoneService.selectBhzPhone(trZhanglaConfigure.getNamesgj());
            }

            SysMessageCore sysMessageCore = new SysMessageCore();
            sysMessageCore.setEsTitle("张拉超标提醒");
            sysMessageCore.setEsType("1");
            sysMessageCore.setEsSendStatus("0");
            sysMessageCore.setEsSendNum(0);
            sysMessageCore.setEsReceiver(bhzPhone.getPhones());
            JSONObject obj1 = new JSONObject();
//                obj1.put("sbname", syshrwd.getMudidi());
            obj1.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            obj1.put("content", zhanglaXxb.getOverReason() + "，请前往确认");
            obj1.put("sbname", selectshebeione.getSbname());
            sysMessageCore.setEsContent(obj1.toString());
            boolean save = sysMessageCoreService.save(sysMessageCore);
            if (save) {
                log.info(String.format("张拉超标短信提醒信息发送成功" + DateUtils.now()));
            } else {
                log.info(String.format("张拉超标短信提醒信息发送失败" + DateUtils.now()));
            }
        }

    }
}