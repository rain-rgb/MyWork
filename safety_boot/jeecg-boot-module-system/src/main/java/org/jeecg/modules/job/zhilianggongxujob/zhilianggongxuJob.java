package org.jeecg.modules.job.zhilianggongxujob;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.liangGongxuPeople.entity.LiangGongxuPeople;
import com.trtm.iot.liangGongxuPeople.service.ILiangGongxuPeopleService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.trtm.iot.zhiliangtaizuocfg.service.IZhiliangTaizuoCfgService;
import com.trtm.sy.syshrwd.entity.Syshrwd;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * \* @author: zml
 * \* Date: 2022/9/13
 * \* Time: 10:08
 * \* Description: 制梁工序自动确认
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zhilianggongxuJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private IZhiliangGongxuService zhiliangGongxuService;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;
    @Autowired
    private IZhiliangTaizuoCfgService zhiliangTaizuoCfgService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private ILiangGongxuPeopleService liangGongxuPeopleService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZHILIANGGONGXU);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到制梁工序自动确认定时任务配置信息" + DateUtils.now());
            return;
        }
        List<Zhiliangrenwudan> zhiliangrenwudans = zhiliangrenwudanService.getzhilianglist();
        if (0 == zhiliangrenwudans.size()) {
            log.info("暂无制梁工序需要确认" + DateUtils.now());
            return;
        }
        Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
        try {
            for (Zhiliangrenwudan zhiliangrenwudan1 : zhiliangrenwudans) {
                String person = "工序自动确认";
                boolean boo1 = false;
                ZhiliangTaizuoCfg zhiliangTaizuoCfg = null;
                List<ZhiliangGongxu> list = new ArrayList<>();
                if (!StringUtils.isEmpty(zhiliangrenwudan1.getTaizuono())) {
                    zhiliangTaizuoCfg = zhiliangTaizuoCfgService.selectzltaizuo(zhiliangrenwudan1.getTaizuono());
                    if (null != zhiliangTaizuoCfg) {
                        boo1 = true;
                    }
                }

                List<ZhiliangGongxu> zhiliangGongxuList = zhiliangGongxuService.selectgongxu(zhiliangrenwudan1.getUuid());
                if (zhiliangGongxuList.size() > 0) {
                    for (ZhiliangGongxu zhiliangGongxu : zhiliangGongxuList) {
                        list.add(zhiliangGongxu);
                        int i = list.indexOf(zhiliangGongxu);
                        boolean boo = false;
                        int status = 0;
                        int times = 0;
                        if (zhiliangGongxu.getUnit()==0) {
                            times = 1000 * 60;
                        } else if (zhiliangGongxu.getUnit() == 1) {
                            times = 1000 * 60 * 60;
                        } else if (zhiliangGongxu.getUnit() == 2) {
                            times = 1000 * 60 * 60 * 24;
                        } else {
                            continue;
                        }
                        if (i == 0 && boo1) {
                            if (zhiliangGongxu.getStatus() != 2) {
                                long df = (new Date().getTime() - zhiliangrenwudan1.getProductiontime().getTime()) / times;
                                if (df > 0 && !StringUtils.isEmpty(zhiliangGongxu.getRemark())) {
                                    if (df > Double.parseDouble(zhiliangGongxu.getRemark())) {
                                        status = 2;
                                    } else {
                                        status = 1;
                                    }
                                }
                            }
                        } else if (i == 0){
                            if (zhiliangGongxu.getStatus() != 2) {
                                long df = (new Date().getTime() - zhiliangrenwudan1.getProductiontime().getTime()) / times;
                                if (df > 0 && !StringUtils.isEmpty(zhiliangGongxu.getRemark())) {
                                    if (df > Double.parseDouble(zhiliangGongxu.getRemark())) {
                                        status = 2;
                                    } else {
                                        status = 1;
                                    }
                                }
                            }
                        }
                        if (i > 0) {
                            if (zhiliangGongxu.getXuhao() != 7 && zhiliangGongxu.getStatus() == 0) {
                                List<ZhiliangGongxu> zhiliangGongxuList1 = subIndex(list, i);
                                if (zhiliangGongxuList1.size() > 0) {
                                    for (ZhiliangGongxu zhiliangGongxu2 : zhiliangGongxuList1) {
                                        int i1 = zhiliangGongxuList.indexOf(zhiliangGongxu2);
                                        if (zhiliangGongxu.getXuhao() != 8) {
                                            if (i1 == (i - 1)) {
                                                List<ZhiliangGongxu> zhiliangGongxu1 = zhiliangGongxuService.selectuuidlist(zhiliangGongxu.getUuid(), zhiliangGongxu2.getXuhao());
                                                if (zhiliangGongxu1.size() == 1) {
                                                    ZhiliangGongxu zhiliangGongxu3 = zhiliangGongxuService.selectuuid(zhiliangGongxu.getUuid(), zhiliangGongxu2.getXuhao());
                                                    if (null != zhiliangGongxu3) {
                                                        if (StringUtil.isNotEmpty(String.valueOf(zhiliangGongxu3.getFinishtime())) && !"null".equals(String.valueOf(zhiliangGongxu3.getFinishtime()))) {
                                                            long df = (new Date().getTime() - zhiliangGongxu3.getFinishtime().getTime()) / times;
                                                            if (zhiliangGongxu2.getXuhao() == 2 && zhiliangGongxu2.getStatus() == 0) {
                                                                Bhzrenwudan bhzrenwudan = bhzrenwudanService.selectorderno(zhiliangrenwudan1.getCode());
                                                                long date = new Date().getTime();
                                                                if (null != bhzrenwudan) {
                                                                    if (StringUtil.isNotEmpty(String.valueOf(bhzrenwudan.getEndtim())) && !"null".equals(String.valueOf(bhzrenwudan.getEndtim()))) {
                                                                        long date1 = bhzrenwudan.getEndtim().getTime();
                                                                        if (date >= date1) {
                                                                            status = 2;
                                                                        }
                                                                    } else {
                                                                        if (df >= 1) {
                                                                            status = 2;
                                                                        }
                                                                    }
                                                                } else {
                                                                    if (!StringUtils.isEmpty(zhiliangGongxu.getRemark()) && pattern.matcher(zhiliangGongxu.getRemark()).matches()) {
                                                                        if (df >= Double.parseDouble(zhiliangGongxu.getRemark())) {
                                                                            status = 2;
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if (!StringUtils.isEmpty(zhiliangGongxu.getRemark())) {
                                                                    if (df >= Double.parseDouble(zhiliangGongxu.getRemark())) {
                                                                        status = 2;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    zhiliangrenwudan1.setNote(zhiliangrenwudan1.getCode() + zhiliangGongxu2.getXuhao() + "工序配置重复");
                                                    zhiliangrenwudanService.updateById(zhiliangrenwudan1);
                                                    log.info(zhiliangrenwudan1.getCode() + "该梁片工序重复" + DateUtils.now());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (status > 0) {
                            zhiliangGongxu.setStatus(status);
                            if (status == 2) {
                                float i1 = Float.parseFloat(zhiliangGongxu.getRemark());
                                float v = i1 * 2;
                                System.out.println(i1);
                                long l = new Double(Double.parseDouble(String.valueOf(v))).longValue() * times;
                               // long l = Long.parseLong(String.valueOf(v)) * times;
                                if(i == 0){
                                    long time = zhiliangrenwudan1.getProductiontime().getTime();
                                    Date date= new Date((time + l/2));
                                    zhiliangGongxu.setFinishtime(date);
                                }else {
                                    ZhiliangGongxu zhiliangGongxu1 = list.get(i - 1);
                                    long time = zhiliangGongxu1.getFinishtime().getTime();
                                    Date date= new Date((time + l/2));
                                    zhiliangGongxu.setFinishtime(date);
                                }
                                zhiliangGongxu.setResponsible(person);
                                LiangGongxuPeople s = liangGongxuPeopleService.selectResponsible(zhiliangrenwudan1.getSysOrgCode(), zhiliangGongxu.getXuhao());
                                if(s != null && StringUtil.isNotEmpty(s.getResponsible())){
                                    zhiliangGongxu.setResponsible(s.getResponsible());
                                    if (s.getPermission() == 0 && s.getResponsiblephone() != null){
                                        message(zhiliangrenwudan1,zhiliangGongxu);
                                    }
                                }
                            }
                            zhiliangrenwudan1.setXuhao(String.valueOf(zhiliangGongxu.getXuhao()));
                            zhiliangrenwudan1.setCunliangstatus(1);
                            zhiliangrenwudanService.updateById(zhiliangrenwudan1);
                            zhiliangGongxuService.updateById(zhiliangGongxu);
                            boo = true;
                        }
                        if (boo && boo1) {
                            zhiliangTaizuoCfg.setStatus(1);
                            zhiliangTaizuoCfg.setXuhao(zhiliangGongxu.getXuhao());
                            zhiliangTaizuoCfg.setBeamno(zhiliangrenwudan1.getCode());
                            zhiliangTaizuoCfgService.updateById(zhiliangTaizuoCfg);
                        }
                    }
                } else {
                    log.info(zhiliangrenwudan1.getCode() + "该梁片没有工序" + DateUtils.now());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<ZhiliangGongxu> subIndex(List<ZhiliangGongxu> list, int i) {
        List<ZhiliangGongxu> list1 = new ArrayList<>();
        for (ZhiliangGongxu zhiliangGongxu : list) {
            if (list.indexOf(zhiliangGongxu) <= i) {
                list1.add(zhiliangGongxu);
            }
        }
        return list1;
    }

    public void message(Zhiliangrenwudan zhiliangrenwudan1,ZhiliangGongxu zhiliangGongxu) {

            JSONObject obj = new JSONObject();
            LiangGongxuPeople phones = liangGongxuPeopleService.selectResponsible(zhiliangrenwudan1.getSysOrgCode(), zhiliangGongxu.getXuhao());
            String phone = phones.getResponsiblephone();
            String s = null;
            if(zhiliangGongxu.getXuhao() == 1){
                s= "钢筋绑扎";
            }else if (zhiliangGongxu.getXuhao() == 2){
                s= "混凝土浇筑";
            }else if (zhiliangGongxu.getXuhao() == 3){
                s= "收面静置";
            }else if (zhiliangGongxu.getXuhao() == 4){
                s= "养生";
            }else if (zhiliangGongxu.getXuhao() == 5){
                s= "张拉";
            }else if (zhiliangGongxu.getXuhao() == 6){
                s= "封端";
            }else if (zhiliangGongxu.getXuhao() == 7){
                s= "存梁/取梁";
            }else if (zhiliangGongxu.getXuhao() == 8){
                s= "提梁";
            }else if (zhiliangGongxu.getXuhao() == 9){
                s= "蒸养";
            }else if (zhiliangGongxu.getXuhao() == 10){
                s= "压浆";
            }else if (zhiliangGongxu.getXuhao() == 11){
                s= "模板安装";
            }else {
                s= "蒸养二区";
            }
            obj.put("content", "" + zhiliangrenwudan1.getCode() + "（" + s + "）已完成");
            obj.put("sbname", zhiliangrenwudan1.getTaizuono());
            String toString = obj.toString().substring(1,obj.toString().length()-1);
            SysMessageCore selectOne = sysMessageCoreService.selectOne(toString,phone,"自动工序确认提醒");
            if(selectOne == null){
                SysMessageCore sysMessageCore = new SysMessageCore();
                sysMessageCore.setEsTitle("自动工序确认提醒");
                sysMessageCore.setEsType("1");
                sysMessageCore.setEsSendStatus("0");
                sysMessageCore.setEsSendNum(0);
                sysMessageCore.setEsReceiver(phone);
                JSONObject obj1 = new JSONObject();
                obj1.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                obj1.put("content", "" + zhiliangrenwudan1.getCode() + "（" + s + "）已完成");
                obj1.put("sbname", zhiliangrenwudan1.getTaizuono());
                sysMessageCore.setEsContent(obj1.toString());
                boolean save = sysMessageCoreService.save(sysMessageCore);
                if (save) {
                    log.info(String.format("工序确认提醒信息发送成功" + DateUtils.now()));
                } else {
                    log.info(String.format("工序确认提醒信息发送失败" + DateUtils.now()));
                }
            }
        }

}
