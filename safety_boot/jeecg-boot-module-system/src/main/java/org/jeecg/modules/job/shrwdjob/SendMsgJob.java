package org.jeecg.modules.job.shrwdjob;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.sy.syshrwd.entity.Syshrwd;
import com.trtm.sy.syshrwd.service.ISyshrwdService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SendMsgJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISyshrwdService syshrwdService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SHDXTX);//查询送货短信提醒状态
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到送货短信提醒定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<Syshrwd> syshrwdList = syshrwdService.sendMsg();
        if (null == syshrwdList || syshrwdList.size() == 0) {
            log.info("暂无送货短信提醒未发送的数据");
            return;
        }
        int id = 0;
        for (Syshrwd syshrwd : syshrwdList) {
            id = syshrwd.getId();
            Integer integer = syshrwdService.updateIschaoshiqueren(syshrwd.getId());
            if (integer > 0) {
                log.info(String.format("是否超时确认状态修改成功" + DateUtils.now()));
            }
            JSONObject obj = new JSONObject();

            obj.put("content", "送货单（" + syshrwd.getShrwd() + "）超过24h未确认，请前往确认");
            obj.put("sbname", syshrwd.getMudidi());
            String toString = obj.toString().substring(1,obj.toString().length()-1);
            SysMessageCore selectOne = sysMessageCoreService.selectOne(toString,syshrwd.getPhone(),"送货确认提醒");
            if(selectOne == null){
                SysMessageCore sysMessageCore = new SysMessageCore();
                sysMessageCore.setEsTitle("送货确认提醒");
                sysMessageCore.setEsType("1");
                sysMessageCore.setEsSendStatus("0");
                sysMessageCore.setEsSendNum(0);
                sysMessageCore.setEsReceiver(syshrwd.getPhone());
                JSONObject obj1 = new JSONObject();
//                obj1.put("sbname", syshrwd.getMudidi());
                obj1.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                obj1.put("content", "送货单（" + syshrwd.getShrwd() + "）超过24h未确认，请前往智慧建设app确认");
                obj1.put("sbname", syshrwd.getMudidi());
                sysMessageCore.setEsContent(obj1.toString());
                boolean save = sysMessageCoreService.save(sysMessageCore);
                if (save) {
                    log.info(String.format("送货短信提醒信息发送成功" + DateUtils.now()));
                } else {
                    log.info(String.format("送货短信提醒信息发送失败" + DateUtils.now()));
                }
            }


        }
//        sysConfigService.updateSysConfig(JobUtil.SHDXTX, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
//    //判断两个时间相差多少小时
//    private static int getDifferHour(Date startDate, Date endDate) {
//        long dayM = 1000 * 24 * 60 * 60;
//        long hourM = 1000 * 60 * 60;
//        long differ = endDate.getTime() - startDate.getTime();
//        long hour = differ % dayM / hourM + 24 * (differ / dayM);
//        return Integer.parseInt(String.valueOf(hour));
//    }
}
