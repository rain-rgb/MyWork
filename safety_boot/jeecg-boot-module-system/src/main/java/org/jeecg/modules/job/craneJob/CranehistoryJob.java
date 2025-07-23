package org.jeecg.modules.job.craneJob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.byscfg.entity.BysCfg;
import com.trtm.iot.byscfg.service.IBysCfgService;
import com.trtm.iot.devicecranecfg.entity.DeviceCraneCfg;
import com.trtm.iot.devicecranecfg.service.IDeviceCraneCfgService;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.BysUtil;
import org.jeecg.modules.job.jobutil.CraneJobUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 大型设备(提梁机/架桥机/龙门吊)历史数据预警定时任务
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CranehistoryJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;

    @Autowired
    private IDeviceCraneCfgService deviceCraneCfgService;

    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    private IBhzPhoneService bhzPhoneService;

    @Autowired
    private CraneJobUtil craneJobUtil;

    @Autowired
    private ISysMessageService sysMessageService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.CRANEHISTORY_CFG);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到大型设备历史数据预警定时任务配置信息" + DateUtils.now()));
            return;
        }

        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<DeviceCraneHistorydata> selectcranehistoryones = deviceCraneHistorydataService.selectcranehistoryone(curid,0);
        if (null == selectcranehistoryones || selectcranehistoryones.size() == 0) {
            log.info("暂无大型设备未判断的历史数据数据");
            return;
        }
        int id = 0;
        for (DeviceCraneHistorydata selectcranehistoryone : selectcranehistoryones) {
            id = selectcranehistoryone.getId();
            Integer is_call = 0; //是否报警  1不报警 0报警
            String craneUid = "";//cranehistory_cfg 的大型设备预警电话号配置
            String cranePhones = "";//cranehistory_cfg 的大型设备预警号码组
            StringBuilder final_content = new StringBuilder();
            int final_over_level = 0;
            String cranedate = DateUtil.format(selectcranehistoryone.getCranedate(), "yyyy-MM-dd HH:mm:ss");//数据上传时间
            //设备编号
            String shebeino = selectcranehistoryone.getDeviceCode();
            try {
                DeviceCraneCfg selectcranecallone = deviceCraneCfgService.selectcranecallone(shebeino);
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeino);
                //判断是否有设备
                if (null == selectshebeione) {
                    log.info("暂无该大型设备" + DateUtils.now());
                    continue;
                }
                if (null != selectcranecallone) {
                    is_call = selectcranecallone.getIsCall();//是否报警
                }
                //相对应的配置信息
                if (null != selectcranecallone){
                    if (null != selectcranecallone.getCranePhones()){
                        craneUid = selectcranecallone.getCranePhones();
                    }
                }
                if (StrUtil.isNotBlank(craneUid)){
                    BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(craneUid);
                    if (null != bhzPhone){
                        cranePhones = bhzPhone.getPhones();
//                        primaryNames = bhzPhone.getNames();
                    }
                }
                //判断大型设备吊重/风速/激光测距是否异常
                Map map = craneJobUtil.cranehistoryjudgment(selectcranehistoryone,selectshebeione,is_call);
                int status = (int) map.get("status");//状态（1：合格，2：不合格）
                final_content.append(map.get("final_content"));//大型设备预警信息
                if (status > final_over_level){
                    final_over_level = status;
                }
                if (0 == final_over_level || final_content.length() == 0){
                    final_over_level = 1;
                    deviceCraneHistorydataService.updateStatus(id,final_over_level);
                    deviceCraneHistorydataService.updatebysone(id,1);//修改device_crane_historydata的状态(是否进行超标判断)
                    continue;
                }
                JSONObject obj = new JSONObject();
                obj.put("sbname", selectshebeione.getSbname());
                obj.put("time", cranedate);
                obj.put("content", final_content);
                if (!cranePhones.equals("")) {
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setEsTitle("大型设备预警");
                    sysMessage.setEsType("1");//短信类型  1短信
                    sysMessage.setEsReceiver(cranePhones);//手机号
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
                DeviceCraneHistorydata deviceCraneHistorydata = new DeviceCraneHistorydata();
                deviceCraneHistorydata.setId(id);
                deviceCraneHistorydata.setStatus(final_over_level);
                deviceCraneHistorydata.setAlertstate(1);
                deviceCraneHistorydataService.updateById(deviceCraneHistorydata);  //智慧用电修改超标状态以及检测过状态
            }catch (Exception e){
                e.printStackTrace();
                deviceCraneHistorydataService.updatebysone(id,40);
            }
            log.info(String.format("大型设备预警！   时间" +DateUtils.now(),"当前判断到"+id));
        }
        sysConfigService.updateSysConfig(JobUtil.CRANEHISTORY_CFG,id);
    }
}
