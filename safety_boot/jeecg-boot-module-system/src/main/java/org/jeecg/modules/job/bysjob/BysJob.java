package org.jeecg.modules.job.bysjob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.byscfg.entity.BysCfg;
import com.trtm.iot.byscfg.service.IBysCfgService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.BysUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 标养室温湿度预警定时任务
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BysJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IBysRealService bysRealService;

    @Autowired
    private IBysCfgService bysCfgService;

    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    private IBhzPhoneService bhzPhoneService;

    @Autowired
    private BysUtil bysUtil;

    @Autowired
    private ISysMessageService sysMessageService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BYS_CFG);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到标养室预警定时任务配置信息" + DateUtils.now()));
            return;
        }

        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<BysReal> selectbysones = bysRealService.selectbysone(curid,0);
        if (null == selectbysones || selectbysones.size() == 0) {
            log.info(String.format("暂无标养室未判断的数据"));
            return;
        }
        int id = 0;
        for (BysReal selectbysone : selectbysones) {
            id = selectbysone.getId();
            Integer is_call = 0; //是否报警  1不报警 0报警
            String zhydUid = "";//bys_cfg 的标养室预警电话号配置
            String zhydPhones = "";//bys_cfg 的标养室预警电话号
            StringBuilder final_content = new StringBuilder();
            int final_over_level = 0;
            String gatherdate = DateUtil.format(selectbysone.getGatherdate(), "yyyy-MM-dd HH:mm:ss");//数据上传时间
            //设备编号
            String shebeino = selectbysone.getShebeino();
            try {
                BysCfg selectbyscallone = bysCfgService.selectbyscallone(shebeino);
//                List<BysCfg> selectbyslist = bysCfgService.selectbyslist(shebeino);
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeino);
                //判断是否有设备
                if (null == selectshebeione) {
                    log.info(String.format("暂无标养室的设备" + DateUtils.now()));
                    continue;
                }
//                //根据设备编号查询标养室数据表
//                List<BysReal> selectbysbaselist = bysRealService.selectbysbaselist(shebeino);
//                if (selectbysbaselist.size() == 0){
//                    bysRealService.updatebysone(id,40);
//                    log.info(String.format("暂无该标养室设备数据信息"+DateUtils.now()));
//                    continue;
//                }
                if (null != selectbyscallone) {
                    is_call = selectbyscallone.getIsCall();//是否报警
                }
                //相对应的超标
                if (null != selectbyscallone){
                    if (null != selectbyscallone.getBysPhones()){
                        zhydUid = selectbyscallone.getBysPhones();
                    }
                }
                if (StrUtil.isNotBlank(zhydUid)){
                    BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(zhydUid);
                    if (null != bhzPhone){
                        zhydPhones = bhzPhone.getPhones();
//                        primaryNames = bhzPhone.getNames();
                    }
                }
                //判断标养室温湿度是否超标
                Map map = bysUtil.bysjudgment(selectbysone,selectshebeione,is_call);
                int status = (int) map.get("status");//状态（1：合格，2：不合格）
                final_content.append(map.get("final_content"));//标养室预警信息
                if (status > final_over_level){
                    final_over_level = status;
                }
                if (0 == final_over_level || final_content.length() == 0){
                    final_over_level = 1;
                    bysRealService.updateStatus(id,final_over_level);
                    bysRealService.updatebysone(id,1);//修改bys_real的状态(是否进行超标判断)
                    continue;
                }
                JSONObject obj = new JSONObject();
                obj.put("sbname", selectshebeione.getSbname());
                obj.put("time", gatherdate);
                obj.put("content", final_content);
                if (!zhydPhones.equals("")) {
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setEsTitle("标养室温湿度预警");
                    sysMessage.setEsType("1");//短信类型  1短信
                    sysMessage.setEsReceiver(zhydPhones);//手机号
                    sysMessage.setEsContent(obj.toString());//短信内容
                    sysMessage.setRemark(shebeino);
                    if (is_call == 0) {
                        sysMessage.setEsSendStatus("0");//推送状态0未推送
                        sysMessage.setEsSendNum(0);//推送总次数
                    } else {
                        sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
                        sysMessage.setEsSendNum(0);//推送总次数
                        sysMessage.setEsResult("不需要推送");
                    }
                    sysMessage.setCreateTime(new Date());
                    sysMessageService.save(sysMessage);
                }
                BysReal bysReal = new BysReal();
                bysReal.setId(id);
                bysReal.setStatus(final_over_level);
                bysReal.setAlertstate(1);
                bysRealService.updateById(bysReal);  //智慧用电修改超标状态以及检测过状态
            }catch (Exception e){
                e.printStackTrace();
                bysRealService.updatebysone(id,40);
            }
            log.info(String.format("标养室温湿度预警！   时间" +DateUtils.now(),"当前判断到"+id));
        }
        sysConfigService.updateSysConfig(JobUtil.BYS_CFG,id);
    }
}
