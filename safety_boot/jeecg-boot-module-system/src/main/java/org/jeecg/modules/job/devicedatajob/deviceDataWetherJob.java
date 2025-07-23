package org.jeecg.modules.job.devicedatajob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.device.entity.Device;
import com.trtm.iot.device.service.IDeviceService;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.entity.DevicehistoryWether;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.devicehistory.service.IDevicehistoryWetherService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;


/**
 * \* @author: zml
 * \* Date: 2022/04/08
 * \* Time: 13:40
 * \* Description:  环境云平台获取环境监测数据
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class deviceDataWetherJob implements Job {

    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IDevicehistoryWetherService devicehistoryService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.DEVICE);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到获取环境监测数据定时任务配置信息" + DateUtils.now());
            return;
        }
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sbtype", 82);
        queryWrapper.eq("shebei_status", 1);
        List<ShebeiInfo> list = shebeiInfoService.list(queryWrapper);
        String groupId = "";
        if (list.size() > 0) {
            for (ShebeiInfo shebeiInfo : list) {
                if ("".equals(groupId)) {
                    groupId = shebeiInfo.getSbjno();
                } else {
                    groupId = groupId + "," + shebeiInfo.getSbjno();
                }
            }
        } else {
            return;
        }
        try {
            String token = jobUtil.getDeviceToken();
            if (token != null) {
                String url1 = "http://www.0531yun.com/api/data/getRealTimeDataByDeviceAddr?deviceAddrs=" + groupId;
                StringBuilder sb = null;
                try {
                    HttpURLConnection connection = null;// 打开连接
                    URL url2 = new URL(url1);    //把字符串转换为URL请求地址
                    connection = (HttpURLConnection) url2.openConnection();
                    connection.setRequestProperty("authorization", token);
                    connection.connect();// 连接会话
                    // 获取输入流
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    String line;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {// 循环读取流
                        sb.append(line);
                    }
                    br.close();// 关闭流
                    connection.disconnect();// 断开连接
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (sb != null) {
                    String s = sb.toString();
                    JSONObject deviceDtata = new JSONObject(s);
                    int code = (int) deviceDtata.get("code");
                    if (code == 1000) {
                        JSONArray data = (JSONArray) deviceDtata.get("data");
                        if (data.size() > 0) {
                            for (Object object : data) {
                                JSONObject jsonObject = JSONUtil.parseObj(object);
                                Integer deviceAddr = (Integer) jsonObject.get("deviceAddr");
//                                String datalist1 = (String) jsonObject.get("dataItem");
//                                if (datalist1 != null) {
                                if (!StringUtils.isEmpty(deviceAddr)) {
                                    Device device = new Device();
                                    DevicehistoryWether devicehistory = new DevicehistoryWether();
                                    device.setDevaddr(String.valueOf(deviceAddr));
                                    device.setDevkey(deviceAddr);
                                    devicehistory.setDevaddr(String.valueOf(deviceAddr));
                                    device.setDevname((String) jsonObject.get("deviceName"));
                                    device.setDevlat(BigDecimal.valueOf((Double) jsonObject.get("lat")));
                                    device.setDevlng(BigDecimal.valueOf((Double) jsonObject.get("lng")));
                                    if ("normal".equals(jsonObject.get("deviceStatus"))) {
                                        Long temp = (Long) jsonObject.get("timeStamp");
                                        Timestamp ts = new Timestamp(temp);
                                        device.setTimevalue(ts);
                                        devicehistory.setTimevalue(ts);
                                        device.setDevstatus("true");
                                        JSONArray datalist = (JSONArray) jsonObject.get("dataItem");
                                        if (datalist.size() > 0) {
                                            for (Object object1 : datalist) {
                                                JSONObject jsonObjects = JSONUtil.parseObj(object1);
                                                JSONArray datalists = (JSONArray) jsonObjects.get("registerItem");
                                                int nodeId = (int) jsonObjects.get("nodeId");
                                                if (datalists.size() > 0) {
                                                    for (Object o : datalists) {
                                                        boolean boo = true;
                                                        QueryWrapper<Device> queryWrapper1 = new QueryWrapper<>();
                                                        queryWrapper1.eq("DevAddr", deviceAddr);
                                                        JSONObject jsonObjectss = JSONUtil.parseObj(o);
                                                        String value = (String) jsonObjectss.get("data");
                                                        int registerId = (int) jsonObjectss.get("registerId");
                                                        int alarmLevel = (int) jsonObjectss.get("alarmLevel");
                                                        device.setDevtempvalue(value);
                                                        if (alarmLevel == 0) {
                                                            device.setTempstatus(0);
                                                        } else if (alarmLevel == 1 || alarmLevel == 2) {
                                                            device.setTempstatus(1);
                                                        } else {
                                                            device.setTempstatus(2);
                                                        }
                                                        if (nodeId == 3) {
                                                            if (registerId == 1) {
                                                                device.setDevtempname("雷达水位计空高");
                                                                queryWrapper1.eq("DevTempName", "雷达水位计空高");
                                                                devicehistory.setFluviograph(value);
                                                            }

                                                        } else if (nodeId == 11) {
                                                            if (registerId == 1) {
                                                                device.setDevtempname("温度");
                                                                queryWrapper1.eq("DevTempName", "温度");
                                                                devicehistory.setTemperature(value);
                                                            } else {
                                                                device.setDevtempname("湿度");
                                                                queryWrapper1.eq("DevTempName", "湿度");
                                                                devicehistory.setHumidity(value);
                                                            }
                                                        } else if (nodeId == 1) {
                                                            if (registerId == 1) {
                                                                device.setDevtempname("风力");
                                                                queryWrapper1.eq("DevTempName", "风力");
                                                                devicehistory.setWind(value);
                                                            } else {
                                                                device.setDevtempname("风速");
                                                                queryWrapper1.eq("DevTempName", "风速");
                                                                devicehistory.setWindspeed(value);
                                                            }
                                                        } else if (nodeId == 2) {
                                                            if (registerId == 1) {
                                                                device.setDevtempname("风向");
                                                                queryWrapper1.eq("DevTempName", "风向");
                                                                devicehistory.setWinddirection(value);
                                                            } else {
                                                                boo = false;
                                                            }
                                                        } else if (nodeId == 20) {
                                                            if (registerId == 5) {
                                                                device.setDevtempname("累积雨量");
                                                                queryWrapper1.eq("DevTempName", "累积雨量");
                                                                devicehistory.setAcrainfall(value);
                                                            } else {
                                                                boo = false;
                                                            }
                                                        }else if (nodeId == 4) {
                                                            if (registerId == 1) {
                                                                device.setDevtempname("雷达水位计液位高");
                                                                queryWrapper1.eq("DevTempName", "雷达水位计液位高");
                                                                devicehistory.setRadar(value);
                                                            } else {
                                                                boo = false;
                                                            }
                                                        }else if (nodeId == 21) {
                                                            if (registerId == 1) {
                                                                device.setDevtempname("瞬时雨量");
                                                                queryWrapper1.eq("DevTempName", "瞬时雨量");
                                                                devicehistory.setInsrainfall(value);
                                                            } else {
                                                                device.setDevtempname("当前雨量（今日雨量）");
                                                                queryWrapper1.eq("DevTempName", "当前雨量（今日雨量）");
                                                                devicehistory.setOverproofStatus(value);
                                                            }
                                                        }
                                                        else if (nodeId == 22) {
                                                            if (registerId == 2) {
                                                                device.setDevtempname("日雨量（昨日雨量）");
                                                                queryWrapper1.eq("DevTempName", "日雨量（昨日雨量）");
                                                                devicehistory.setIstuisong(value);
                                                            } else {
                                                                boo = false;
                                                            }
                                                        }
                                                        else{
                                                            boo = false;
                                                        }
                                                        if (boo) {
                                                            Device one = deviceService.getOne(queryWrapper1);
                                                            if (one == null) {
                                                                deviceService.save(device);
                                                            } else {
                                                                device.setId(one.getId());
                                                                deviceService.updateById(device);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            devicehistoryService.save(devicehistory);
                                        }
                                    } else if ("offline".equals(jsonObject.get("deviceStatus"))) {
                                        device.setDevstatus("false");
                                        QueryWrapper<Device> queryWrapper1 = new QueryWrapper<>();
                                        queryWrapper1.eq("DevAddr", deviceAddr);
                                        List<Device> list1 = deviceService.list(queryWrapper1);
                                        if (list1.size()>0){
                                            for (Device device1:list1){
                                                device.setId(device1.getId());
                                                deviceService.updateById(device);
                                            }
                                        }else {
                                            log.info("环境监测设备"+deviceAddr+"离线");
                                        }
                                    }
                                }
                            }
                        } else {
                            log.info("暂无环境监测数据!");
                        }
                    } else {
                        log.info(String.valueOf(deviceDtata.get("message")));
                    }
                }
            } else {
                log.info("获取环境云平台token失败!");
            }
            log.info("获取环境监测数据定时任务" + DateUtils.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDate(QueryWrapper<Device> queryWrapper1, Device device) {
    }
}
