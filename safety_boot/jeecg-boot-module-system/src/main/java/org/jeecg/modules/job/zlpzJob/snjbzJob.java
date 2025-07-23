package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zlpz.entity.Zlpz;
import com.trtm.iot.zlpz.service.IZlpzService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName snjbzJob：
 * @Description TODO
 * @Author 55314
 * @Date 2024/3/1 14:39
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class snjbzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    private IZlpzService zlpzService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_BHZ);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到疏港砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输疏港砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = deviceMixpileHistorydataOneService.selectListzlpz(shebeilist, curid);
        if (null == deviceMixpileHistorydataOnes || deviceMixpileHistorydataOnes.size() == 0) {
            log.info(String.format("暂无疏港拌合站未推送数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        for (DeviceMixpileHistorydataOne deviceMixpileHistorydataOne : deviceMixpileHistorydataOnes) {
            QueryWrapper<Zlpz> queryWrapperZlpz = new QueryWrapper<>();
            queryWrapperZlpz.eq("shebeino", deviceMixpileHistorydataOne.getShebeino());
            Zlpz one = zlpzService.getOne(queryWrapperZlpz);
            String shebeiid = one.getShebeiid();
            String project = one.getProject();

            id = deviceMixpileHistorydataOne.getId();
            JSONObject sendDate = new JSONObject();
            List sendList = new ArrayList();
            sendDate.set("id", project + "_" + id);
            sendDate.set("equipId", shebeiid);
            sendDate.set("constructionStakeNum", deviceMixpileHistorydataOne.getPileNo());//桩号
            sendDate.set("startTime", deviceMixpileHistorydataOne.getPileStarttime());//开始时间
            sendDate.set("endTime", deviceMixpileHistorydataOne.getPileTime());//结束时间
            sendDate.set("constructionStakeLong", deviceMixpileHistorydataOne.getPileRealdep());//施工桩长
            sendDate.set("designStakeLong", deviceMixpileHistorydataOne.getPileDesigndep());//设计桩长
            sendDate.set("runningSpeed", deviceMixpileHistorydataOne.getPileSpeed());//下钻速度
            sendDate.set("designRunningSpeed", "");//设计下钻速度
            sendDate.set("liftingSpeed", deviceMixpileHistorydataOne.getPileUspeed());//提钻速度
            sendDate.set("designLiftingSpeed", "");//设计提钻速度
            sendDate.set("meterVolume", "");//每延米掺量
            sendDate.set("designMeterVolume", "");//设计每延米掺量

            QueryWrapper<BhzCementOverHandler> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("baseId", deviceMixpileHistorydataOne.getShebeino());
            BhzCementOverHandler one1 = bhzCementOverHandlerService.getOne(queryWrapper);
            int alarmStatus = 0;
            if (null != one1) {
                alarmStatus = 1;
                sendDate.set("opinion", one1.getHandleWay());
                sendDate.set("closeTime", sdf.format(one1.getHandleTime()));
                sendDate.set("closer", one1.getHandlePerson());
                sendDate.set("attachment", one1.getFilePath2());//附件
            }
            sendList.add(sendDate);

            JSONObject sendJsonObject = new JSONObject();
            sendJsonObject.set("serviceName", "ZLPZ_ZX_SNJBZ");
            sendJsonObject.set("token", "93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull", "true");
            sendJsonObject.set("param", sendList);
            System.out.println(sendJsonObject);
            String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
            String back = HttpRequest.post(url)
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();

            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                deviceMixpileHistorydataOne.setIssend(alarmStatus == 0 ? 3 : 1);
                log.info(String.format("浙路品质砼拌合站推送成功！%s", id));
            } else {
                deviceMixpileHistorydataOne.setIssend(2);
                log.info(String.format("浙路品质砼拌合站推送失败！%s", id));
            }
            deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne);
            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), back);
        }
    }
}
