package org.jeecg.modules.job.taicangProjectjob;

import cn.hutool.db.nosql.mongo.MongoFactory;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicemixpilehistorydata.entity.DeviceMixpileHistorydata;
import com.trtm.iot.devicemixpilehistorydata.service.IDeviceMixpileHistorydataService;
import com.trtm.iot.devicemixpilehistorydatapart.entity.DeviceMixpileHistorydataPart;
import com.trtm.iot.devicemixpilehistorydatapart.service.IDeviceMixpileHistorydataPartService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * \* @author: zml
 * \* Date: 2022/08/01
 * \* Time: 16:50
 * \* Description:  256太仓搅拌桩数据推送
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class mixpileJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceMixpileHistorydataService deviceMixpileHistorydataService;
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    tcUtil tcUtil;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.TAICANG_MIXPLIE);//搅拌桩数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到太仓搅拌桩数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info("没有配置要传输太仓搅拌桩的设备" + DateUtils.now());
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydata = deviceMixpileHistorydataOneService.selectLists(shebeilist, curid);
        if (null == deviceMixpileHistorydata || deviceMixpileHistorydata.size() == 0) {
            log.info("暂无太仓太仓搅拌桩未推送数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (DeviceMixpileHistorydataOne deviceMixpileHistorydataOne : deviceMixpileHistorydata) {
            String shebeino = deviceMixpileHistorydataOne.getShebeino();
            String pileno = deviceMixpileHistorydataOne.getPileNo();
            String piletime = deviceMixpileHistorydataOne.getPileTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String data = tcUtil.getdata(deviceMixpileHistorydataOne);
            try {
                if (!StringUtils.isEmpty(data) && !"null".equals(data)) {
                    Socket s = new Socket("139.196.182.234", 10721); //底层会将地址字符串转换成InetAddress对象
                    //获取输出流
                    OutputStream os = s.getOutputStream();
                    os.write(data.getBytes(StandardCharsets.UTF_8));
                    InputStream ins = s.getInputStream();
                    byte[] bt = new byte[1024];
                    // 接收服务端发来的数据
                    int len = ins.read(bt); // 阻塞，接收到数据才往下走
                    if (len == -1) {
                        s.close();
                        log.info("太仓256项目水泥水泥搅拌桩" + shebeino + data + "数据推送失败");
                        DeviceMixpileHistorydataOne deviceMixpileHistorydataOne2 = new DeviceMixpileHistorydataOne();
                        deviceMixpileHistorydataOne2.setId(deviceMixpileHistorydataOne.getId());
                        deviceMixpileHistorydataOne2.setTaicangpush(40);
                        deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne2);
                        continue;
                    }
                    System.out.println(new String(bt, 0, len));
//                    PublicMethod.closeAll(ins, os, s);
                    s.close();
                    DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 = deviceMixpileHistorydataOneService.selectone(shebeino, pileno, piletime);
                    if (null == deviceMixpileHistorydataOne1) {

                    } else {
                        DeviceMixpileHistorydataOne deviceMixpileHistorydataOne2 = new DeviceMixpileHistorydataOne();
                        deviceMixpileHistorydataOne2.setId(deviceMixpileHistorydataOne1.getId());
                        deviceMixpileHistorydataOne2.setTaicangpush(1);
                        boolean update = deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne2);
                        if (update) {
                            log.info("太仓256项目水泥搅拌桩" + shebeino + data + "数据推送成功");
                        } else {
                            log.info("太仓256项目水泥搅拌桩" + shebeino + data + "数据推送失败");
                        }
                    }
                    DeviceMixpileHistorydataOne deviceMixpileHistorydataOne2 = new DeviceMixpileHistorydataOne();
                    deviceMixpileHistorydataOne2.setId(deviceMixpileHistorydataOne.getId());
                    deviceMixpileHistorydataOne2.setTaicangpush(1);
                    boolean update = deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne2);
                    if (update) {
                        log.info("太仓256项目水泥搅拌桩" + shebeino + data + "数据推送成功");
                    } else {
                        log.info("太仓256项目水泥搅拌桩" + shebeino + data + "数据推送失败");
                    }
                } else {
                    log.info("太仓256项目水泥搅拌桩暂无推送数据");
                    DeviceMixpileHistorydataOne deviceMixpileHistorydataOne2 = new DeviceMixpileHistorydataOne();
                    deviceMixpileHistorydataOne2.setId(deviceMixpileHistorydataOne.getId());
                    deviceMixpileHistorydataOne2.setTaicangpush(10);
                    deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne2);
                }
            } catch (IOException e) {
                log.info("太仓256项目水泥搅拌桩" + shebeino + data + "数据推送失败");
                DeviceMixpileHistorydataOne deviceMixpileHistorydataOne2 = new DeviceMixpileHistorydataOne();
                deviceMixpileHistorydataOne2.setId(deviceMixpileHistorydataOne.getId());
                deviceMixpileHistorydataOne2.setTaicangpush(40);
                deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne2);
                e.printStackTrace();
            }
        }
    }
}
