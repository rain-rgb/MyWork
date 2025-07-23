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
import com.trtm.iot.tryajiangconfigure.entity.TrYajiangConfigure;
import com.trtm.iot.tryajiangconfigure.service.ITrYajiangConfigureService;
import com.trtm.iot.trzhanglaconfigure.entity.TrZhanglaConfigure;
import com.trtm.iot.trzhanglaconfigure.service.ITrZhanglaConfigureService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
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
 * 压浆超标检测定时任务
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yjyesornotJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrYajiangConfigureService yajiangConfigureService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJ_CBJC);//压浆超标检测
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到压浆超标检测定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<TrYajiangM> yajiangMList = yajiangMService.selectbigid(curid);
        int id = 0;
        Double yajiangCmin = 0.0;
        Double yajiangCmax = 0.0;
        Double yajiangYlmin = 0.0;
        Double yajiangYlmax = 0.0;
        if (yajiangMList.size() != 0){
            for (TrYajiangM yajiangM :yajiangMList){
                id = yajiangM.getId();
                String shebeibianhao = yajiangM.getYjsbbh();
                String syjid = yajiangM.getSyjid();
                Double yjwd = null;//压浆温度
                try {
                    yjwd = Double.valueOf(yajiangM.getYjwd());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                List<TrYajiangS> yajiangSList = yajiangSService.selectmList(syjid);
                if (yajiangSList.size() > 0){
                    // 查询配置表，如果没有配置则，伸长率按照m表中给的范围计算
                    TrYajiangConfigure yajiangConfigure = yajiangConfigureService.selectbyshebei(shebeibianhao);
                    if (yajiangConfigure != null ){

                        yajiangCmin = Double.valueOf(yajiangConfigure.getYajiangCmin());
                        yajiangCmax = Double.valueOf(yajiangConfigure.getYajiangCmax());
                        yajiangYlmin = Double.valueOf(yajiangConfigure.getYajiangYlmin());
                        yajiangYlmax = Double.valueOf(yajiangConfigure.getYajiangYlmax());
                        try {
                            for (TrYajiangS yajiangS :yajiangSList){
                                String kongdao = yajiangS.getKongdao();//孔道号
                                Double jinjiangyal = null;//进浆压力
                                try {
                                    jinjiangyal = Double.valueOf(yajiangS.getJinjiangyal());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (yjwd > yajiangCmax || yjwd < yajiangCmin){
                                    yajiangM.setIsOverLevel(1);
                                    yajiangS.setIsOverLevel(1);
                                    if (jinjiangyal > yajiangYlmax || jinjiangyal < yajiangYlmin){
                                        yajiangM.setIsOverLevel(1);
                                        yajiangS.setIsOverLevel(1);
                                        yajiangM.setOverReason("压浆温度||压力预警");
                                        yajiangS.setOverReason("压浆温度||压力预警");
                                        yajiangS.setHege("0");
                                        yajiangSService.updateById(yajiangS);
                                        yajiangMService.updateById(yajiangM);
                                        continue;
                                    }else {
                                        yajiangM.setIsOverLevel(1);
                                        yajiangS.setIsOverLevel(1);
                                        yajiangM.setOverReason("压浆温度预警");
                                        yajiangS.setOverReason("压浆温度预警");
                                        yajiangS.setHege("0");
                                        yajiangSService.updateById(yajiangS);
                                        yajiangMService.updateById(yajiangM);
                                    }
                                }else if (jinjiangyal > yajiangYlmax || jinjiangyal < yajiangYlmin){
                                    yajiangM.setIsOverLevel(1);
                                    yajiangS.setIsOverLevel(1);
                                    yajiangM.setOverReason("压浆压力预警");
                                    yajiangS.setOverReason("压浆压力预警");
                                    yajiangS.setHege("0");
                                    yajiangSService.updateById(yajiangS);
                                    yajiangMService.updateById(yajiangM);
                                    continue;
                                }else {
                                    yajiangM.setIsOverLevel(0);
                                    yajiangS.setIsOverLevel(0);
                                    yajiangS.setHege("1");
                                    yajiangSService.updateById(yajiangS);
                                    yajiangMService.updateById(yajiangM);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //如果等于1，则不报警
                        if (!yajiangConfigure.getAlertOrNot().equals("1")){
                            //短信预警
                            if (yajiangM.getIsOverLevel() > 0) {
                                assert yajiangConfigure != null;
                                if (yajiangConfigure.getYesornot() == 0) {
                                    messages(yajiangConfigure,yajiangM);
                                }
                            }
                        }

                    }
                }else {
                    log.info(String.format("未获取压浆m表的配置信息" + DateUtils.now()));
                }
                   sysConfigService.updateSysConfig(JobUtil.YJ_CBJC, id);//根据传过来的唯一值来修改当前判断到的数据id
            }
        }
    }

    private void messages(TrYajiangConfigure yajiangConfigure,TrYajiangM yajiangM){
        JSONObject obj = new JSONObject();
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(yajiangConfigure.getShebeino());
        if (selectshebeione != null){
            BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(yajiangConfigure.getNamescj());
            obj.put("content", yajiangM.getOverReason() + "，请前往确认");
            obj.put("sbname", selectshebeione.getSbname());
            String toString = obj.toString().substring(1,obj.toString().length()-1);
            SysMessageCore selectOne = sysMessageCoreService.selectOne(toString,bhzPhone.getPhones(),"压浆超标提醒");
            if(selectOne == null){
                SysMessageCore sysMessageCore = new SysMessageCore();
                sysMessageCore.setEsTitle("压浆超标提醒");
                sysMessageCore.setEsType("1");
                sysMessageCore.setEsSendStatus("0");
                sysMessageCore.setEsSendNum(0);
                sysMessageCore.setEsReceiver(bhzPhone.getPhones());
                JSONObject obj1 = new JSONObject();
//                obj1.put("sbname", syshrwd.getMudidi());
                obj1.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                obj1.put("content", yajiangM.getOverReason() + "，请前往确认");
                obj1.put("sbname", selectshebeione.getSbname());
                sysMessageCore.setEsContent(obj1.toString());
                boolean save = sysMessageCoreService.save(sysMessageCore);
                if (save) {
                    log.info(String.format("压浆超标短信提醒信息发送成功" + DateUtils.now()));
                } else {
                    log.info(String.format("压浆超标短信提醒信息发送失败" + DateUtils.now()));
                }
            }
        }
    }
}