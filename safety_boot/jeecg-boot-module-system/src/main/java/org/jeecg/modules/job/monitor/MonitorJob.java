package org.jeecg.modules.job.monitor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.byscfg.entity.BysCfg;
import com.trtm.iot.byscfg.service.IBysCfgService;
import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.trtm.iot.devicemixpilerealdata.service.IDeviceMixpileRealdataService;
import com.trtm.iot.monitor.entity.Monitor;
import com.trtm.iot.monitor.service.IMonitorService;
import com.trtm.iot.monitor.tool.ArtemisTool;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.BysUtil;
import org.jeecg.modules.job.jobutil.HttpRequestUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.trtm.iot.util.GPSUtil.*;
import static com.trtm.iot.util.GPSUtil.retain6;

/**
 * 标养室温湿度预警定时任务
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MonitorJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private HttpRequestUtil httpRequest;

    @Autowired
    private IMonitorService monitorService;

    @Autowired
    private ISysMessageService sysMessageService;

    @Autowired
    private IDeviceMixpileRealdataService deviceMixpileRealdataService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.MONITOR_CFG);//摄像头离线判断定时任务=36
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到摄像头任务的配置信息" + DateUtils.now()));
            return;
        }

        QueryWrapper<Monitor> queryWrapper = new QueryWrapper<>();
        List<Monitor> monitors = monitorService.list(queryWrapper);
        Map<String,String> param = new HashMap();
        param.put("apiid","1");
        param.put("apisceret","CB093DD1D932456C9D33B2E25CD9CFF5");
        JSONObject sr=HttpRequestUtil.sendPost("http://js.traiot.cn:8081/dataprovider/gettoken", param);
        JSONObject data =JSONObject.parseObject(sr.getString("data")) ;
        String token =data.getString("token");
         String url1 = "http://47.97.173.113:9271/api/VideoLive/";
        if((int)sr.get("code")==200){
            for(Monitor monitor : monitors){
                if(monitor.getCamtype() == 0){
                    String  url= url1 + monitor.getId();
                    JSONObject str=HttpRequestUtil.sendGet(url, "token="+token);
                    if ((int)str.get("code")==0){
                        monitor.setWorkstate(0);
                        monitor.setVaddress((String) str.get("data"));
                    }else{
                        monitor.setWorkstate(1);
                    }
                }else if(monitor.getCamtype() == 1){
                    try {
                        String video = (String) ArtemisTool.getPreviewURLs(monitor.getVerificationcode(),"hls");
                        monitor.setVaddress(video);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else{

                }

                // 水泥搅拌桩摄像头位置随搅拌桩移动
                if(monitor.getUsetype() != null && ( monitor.getUsetype() == 501 || monitor.getUsetype() == 502)){
                    Map<String, Object> columnMap = new HashMap<>();
                    columnMap.put("shebeino",monitor.getRemark());
                    List<DeviceMixpileRealdata> deviceMixpileRealdata = deviceMixpileRealdataService.listByMap(columnMap);
                    if(deviceMixpileRealdata.size()>0){
                        if(deviceMixpileRealdata.get(0).getLtdfloat()!=null && deviceMixpileRealdata.get(0).getLgdfloat()!=null) {
                            double lon = Double.parseDouble(deviceMixpileRealdata.get(0).getLgdfloat());
                            double lat = Double.parseDouble(deviceMixpileRealdata.get(0).getLtdfloat());
                            double lat1 = formatLnglat(lat);
                            double lon1 = formatLnglat(lon);
                            double[] zuobiao = gps84_To_Gcj02(lat1, lon1);
                            Double lat2 = retain6(zuobiao[0]);
                            Double lon2 = retain6(zuobiao[1]);
                            monitor.setLongitude(String.valueOf(lon2));
                            monitor.setLatitude(String.valueOf(lat2));
                        }
                    }
                }
                monitorService.updateById(monitor);
                log.info(String.format("摄像头是否离线判断！   时间" +DateUtils.now(),"摄像头名称："+monitor.getMonitorname()));
            }
        }
        sysConfigService.updateSysConfig(JobUtil.MONITOR_CFG,selectsysconfigone.getCurid()+1);

    }
}
