package com.trtm.iot.devicemixpilerwd.controller;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.trtm.iot.devicemixpileoneoverhandler.service.IMixpileOneOverHandlerService;
import com.trtm.iot.devicemixpilerwd.entity.DeviceMixpileRwdMqtt;
import com.trtm.iot.devicemixpilerwd.service.IDeviceMixpileRwdMqttService;
import com.trtm.iot.devicemixpilerwdlog.entity.DeviceMixpileRwdLog;
import com.trtm.iot.devicemixpilerwdlog.service.IDeviceMixpileRwdLogService;
import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.trtm.iot.devicemixpilestatic.service.IDeviceMixpileStaticService;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import org.apache.shiro.SecurityUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.devicemixpilerwd.entity.DeviceMixpileRwd;
import com.trtm.iot.devicemixpilerwd.service.IDeviceMixpileRwdService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 软基工单表
 * @Author: jeecg-boot
 * @Date: 2022-02-22
 * @Version: V1.0
 */
@Api(tags = "软基工单表")
@RestController
@RequestMapping("/devicemixpilerwd/deviceMixpileRwd")
@Slf4j
public class DeviceMixpileRwdController extends JeecgController<DeviceMixpileRwd, IDeviceMixpileRwdService> {

    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    private IDevicePipepileHistorydataOneService devicePipepileHistorydataOneService;
    @Autowired
    private IDeviceMixpileRwdService deviceMixpileRwdService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IDeviceMixpileRwdLogService deviceMixpileRwdLogService;
    @Autowired
    private IDeviceMixpileStaticService deviceMixpileStaticService;
    @Autowired
    private IMixpileOneOverHandlerService mixpileOneOverHandlerService;
    @Autowired
    private IDeviceMixpileRwdMqttService deviceMixpileRwdMqttService;


    /**
     * 分页列表查询
     *
     * @param deviceMixpileRwd
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "软基工单表-分页列表查询")
    @ApiOperation(value="软基工单表-分页列表查询", notes="软基工单表-分页列表查询")
    @GetMapping(value = "/addOpen")
    public Result<?> queryPageListRwd(DeviceMixpileRwd deviceMixpileRwd,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<DeviceMixpileRwd> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileRwd, req.getParameterMap());
        Page<DeviceMixpileRwd> page = new Page<DeviceMixpileRwd>(pageNo, pageSize);
        IPage<DeviceMixpileRwd> pageList = deviceMixpileRwdService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param deviceMixpileRwd
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "软基工单表-分页列表查询")
    @ApiOperation(value = "软基工单表-分页列表查询", notes = "软基工单表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceMixpileRwd deviceMixpileRwd,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileRwd.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileRwd.setShebeino(shebei);
            } else {
                deviceMixpileRwd.setShebeino("空");
            }
        }
        if (!StringUtils.isEmpty(deviceMixpileRwd.getRjrwd())) {
            deviceMixpileRwd.setRjrwd("*" + deviceMixpileRwd.getRjrwd() + "*");
        }
        if (!StringUtils.isEmpty(deviceMixpileRwd.getOrgcode())) {
            deviceMixpileRwd.setOrgcode(deviceMixpileRwd.getOrgcode() + "*");
        }
        if (!StringUtils.isEmpty(deviceMixpileRwd.getMileage())) {
            deviceMixpileRwd.setMileage("*" + deviceMixpileRwd.getMileage() + "*");
        }

        QueryWrapper<DeviceMixpileRwd> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileRwd, req.getParameterMap());
        Page<DeviceMixpileRwd> page = new Page<DeviceMixpileRwd>(pageNo, pageSize);
        IPage<DeviceMixpileRwd> pageList = deviceMixpileRwdService.page(page, queryWrapper);
        List<DeviceMixpileRwd> records = pageList.getRecords();
        int ready = 0;
        int doing = 0;
        int finish = 0;
        int timeout = 0;
        Map<String, Object> map = new HashMap<>();
        for (DeviceMixpileRwd deviceMixpileRwd1 : records) {
            QueryWrapper<DeviceMixpileStatic> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("sum(pilecount) pilecount,sum(worklength) worklength,sum(chaobiaototal) chaobiaototal");
            queryWrapper1.eq("shebeino", deviceMixpileRwd1.getShebeino());
            queryWrapper1.eq("mileage", deviceMixpileRwd1.getMileage());
            DeviceMixpileStatic deviceMixpileStatic = deviceMixpileStaticService.getOne(queryWrapper1);
            QueryWrapper<MixpileOneOverHandler> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.select("sum(case when overproof_status > 0 then 1 else 0 end) pileno," +
                    "sum(case when overproof_status = 20 then 1 else 0 end) overproof_status");
            queryWrapper2.eq("sbbh", deviceMixpileRwd1.getShebeino());
            queryWrapper2.eq("pile_mileage", deviceMixpileRwd1.getMileage());
            MixpileOneOverHandler one = mixpileOneOverHandlerService.getOne(queryWrapper2);
            if (deviceMixpileStatic != null) {
                double piletotal = 0;
                if (!StringUtils.isEmpty(deviceMixpileStatic.getPilecount())) {
                    piletotal = Double.parseDouble(deviceMixpileStatic.getPilecount());
                    deviceMixpileRwd.setPiletotal((int) piletotal);
                }
                if (!StringUtils.isEmpty(deviceMixpileStatic.getWorklength())) {
                    // 获得里程数并进行取整
                    deviceMixpileRwd.setMileagetotal(Integer.valueOf(deviceMixpileStatic.getWorklength().split("\\.")[0]));
                }
                if (!StringUtils.isEmpty(deviceMixpileStatic.getChaobiaototal())) {
                    deviceMixpileRwd.setChaobiaototal(deviceMixpileStatic.getChaobiaototal());
                }
              //
                double descount = deviceMixpileRwd1.getDescount()==null?0.0:deviceMixpileRwd1.getDescount();
                if (piletotal < descount) {
                    long endtime = 0;
                    if( deviceMixpileRwd1.getEndtime() != null){
                        endtime = deviceMixpileRwd1.getEndtime().getTime();
                    }
                    long date = new Date().getTime();
                    if (piletotal == 0) {
                        deviceMixpileRwd.setStatus(0);
                        deviceMixpileRwd.setTotalpro("0");
                        ready += 1;
                    } else {
                        if (  date > endtime) {
                            deviceMixpileRwd.setStatus(1);
                            doing += 1;
                        } else {
                            deviceMixpileRwd.setStatus(3);
                            timeout += 1;


                        }
                        deviceMixpileRwd.setTotalpro(String.valueOf(piletotal / descount * 100));
                    }
                } else {
                    deviceMixpileRwd.setStatus(2);
                    finish += 1;
                    deviceMixpileRwd.setTotalpro("100");
                }
            }
            if (one != null) {
                if (!StringUtils.isEmpty(one.getPileno())) {
                    deviceMixpileRwd.setHandled(Integer.valueOf(one.getPileno()));
                }
                if (!StringUtils.isEmpty(one.getOverproofStatus())) {
                    deviceMixpileRwd.setHandled(one.getOverproofStatus());
                }
            }
        }

        // QueryWrapper<DeviceMixpileRwd> querytj = new QueryWrapper();// QueryGenerator.initQueryWrapper(deviceMixpileRwd, req.getParameterMap());
        queryWrapper.select(" sum( CASE WHEN status = 0 THEN 1 else 0 END) as ready, SUM( CASE WHEN status = 1 THEN 1 else 0 END) as doing, " +
                "SUM( CASE WHEN status = 2 THEN 1 else 0 END)  as finish, SUM( CASE WHEN status = 3 THEN 1 else 0 END) as timeout ");
     //   if (ready == 0 && doing == 0 && finish == 0 && timeout == 0) {
            map = deviceMixpileRwdService.getMap(queryWrapper);
//        } else {
//            map.put("ready", ready);
//            map.put("doing", doing);
//            map.put("finish", finish);
//            map.put("timeout", timeout);
//        }
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setResult(pageList);
        r.setData(map);
        return r;
    }

    /**
     * 分页列表查询
     *
     * @param deviceMixpileRwd
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "软基工单表-分页列表查询")
    @ApiOperation(value = "软基工单表-分页列表查询", notes = "软基工单表-分页列表查询")
    @GetMapping(value = "/listjd")
    public Result<?> queryPageListjd(DeviceMixpileRwd deviceMixpileRwd,
                                     String sys_depart_project,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileRwd.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileRwd.setShebeino(shebei);
            } else {
                deviceMixpileRwd.setShebeino("空");
            }
        }
        if (!StringUtils.isEmpty(deviceMixpileRwd.getRjrwd())) {
            deviceMixpileRwd.setRjrwd("*" + deviceMixpileRwd.getRjrwd() + "*");
        }
        if (!StringUtils.isEmpty(deviceMixpileRwd.getOrgcode())) {
            deviceMixpileRwd.setOrgcode(deviceMixpileRwd.getOrgcode() + "*");
        }
        if (!StringUtils.isEmpty(deviceMixpileRwd.getMileage())) {
            deviceMixpileRwd.setMileage("*" + deviceMixpileRwd.getMileage() + "*");
        }
        if (!StringUtils.isEmpty(sys_depart_project)) {
            deviceMixpileRwd.setMileageid("*" + sys_depart_project + "*");//分部分项查询
        }
        QueryWrapper<DeviceMixpileRwd> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileRwd, req.getParameterMap());
        Page<DeviceMixpileRwd> page = new Page<DeviceMixpileRwd>(pageNo, pageSize);
        IPage<DeviceMixpileRwd> pageList = deviceMixpileRwdService.page(page, queryWrapper);
        List<DeviceMixpileRwd> records = pageList.getRecords();
        for (DeviceMixpileRwd deviceMixpileRwd1 : records) {
            Integer descount = deviceMixpileRwd1.getDescount();
            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("gzcount",1);
            queryWrapper1.eq("rjrwd",deviceMixpileRwd1.getRjrwd());
            // queryWrapper1.groupBy("rjrwd");
            List<DeviceMixpileHistorydataOne> list1 = deviceMixpileHistorydataOneService.list(queryWrapper1);

            QueryWrapper<DevicePipepileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("rjrwd",deviceMixpileRwd1.getRjrwd());
            // queryWrapper2.groupBy("rjrwd");
            List<DevicePipepileHistorydataOne> list2 = devicePipepileHistorydataOneService.list(queryWrapper2);

            if (list1.size() != 0 || list2.size() != 0){
                int i = list1.size() + list2.size();
                int i1 = i * 100 / descount;
                if (i1 > 100){
                    i1 = 100;
                    deviceMixpileRwd1.setStatus(2);
                }else {
                    deviceMixpileRwd1.setStatus(1);
                }
                if (i > deviceMixpileRwd1.getDescount()){
                    deviceMixpileRwd1.setHandled(deviceMixpileRwd1.getDescount());
                }else {
                    deviceMixpileRwd1.setHandled(i);
                }
                deviceMixpileRwd1.setTotalpro(String.valueOf(i1));
            }else {
                deviceMixpileRwd1.setHandled(0);
                deviceMixpileRwd1.setTotalpro("0");
                deviceMixpileRwd1.setStatus(0);
            }
            deviceMixpileRwdService.updateById(deviceMixpileRwd1);
        }
        return Result.OK(pageList);
    }
    // 终端获取软基工单信息
    @GetMapping(value = "/zdlist")
    public Result<?> queryPageList1(DeviceMixpileRwd deviceMixpileRwd,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {

        QueryWrapper<DeviceMixpileRwd> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileRwd, req.getParameterMap());
        Page<DeviceMixpileRwd> page = new Page<DeviceMixpileRwd>(pageNo, pageSize);
        IPage<DeviceMixpileRwd> pageList = deviceMixpileRwdService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    // 软基工单下发
    @GetMapping(value = "/issuedlist")
    public Result<?> queryPageList2(DeviceMixpileRwd deviceMixpileRwd, DeviceMixpileRwdLog deviceMixpileRwdLog, HttpServletRequest req) {
        QueryWrapper<DeviceMixpileRwd> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileRwd, req.getParameterMap());
        DeviceMixpileRwd one = deviceMixpileRwdService.getOne(queryWrapper);
        String broker = null;
        String topic = null;
        Integer usecount = 3;
        String clientId = "MQTT_SUB_Producer";
        QueryWrapper<DeviceMixpileRwdMqtt> mqtt = new QueryWrapper<>();
        mqtt.eq("shebeino",one.getShebeino());
        DeviceMixpileRwdMqtt rwdMqtt = deviceMixpileRwdMqttService.getOne(mqtt);
        if(rwdMqtt != null){
            // 获取下发配置
            broker= rwdMqtt.getQbroker();
            topic = rwdMqtt.getTopicAll();
            clientId = rwdMqtt.getClientid();
            // 限制发送次数
            usecount = rwdMqtt.getUsecount();
        }else if(one.getShebeino().contains("HSKJ")){
            broker= "tcp://47.97.158.215:8881";
            topic = "mixingDock/g/" + one.getShebeino();
        }else {
            broker = "tcp://47.97.158.215:8876";
            topic = "/ZMXX/" + one.getShebeino();

        }

        MqttClient subClient = getMqttClient(broker, clientId);
        QueryWrapper<DeviceMixpileRwdLog> queryWrapper1 = QueryGenerator.initQueryWrapper(deviceMixpileRwdLog, req.getParameterMap());
        DeviceMixpileRwdLog one1 = deviceMixpileRwdLogService.getOne(queryWrapper1);
        QueryWrapper<DeviceMixpileRwdLog> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("status", 0);
        queryWrapper2.eq("shebeino", one.getShebeino());
        List<DeviceMixpileRwdLog> list = deviceMixpileRwdLogService.list(queryWrapper2);
        if (list.size() < usecount) {
            try {
//                System.out.println(JSONUtil.toJsonStr(one));
                MqttMessage mqttMessage = new MqttMessage(JSONUtil.toJsonStr(one).getBytes(StandardCharsets.UTF_8));
                if (subClient != null) {
                    subClient.publish(topic, mqttMessage);
                    subClient.disconnect();
                    DeviceMixpileRwdLog deviceMixpileRwdLog1 = new DeviceMixpileRwdLog();
                    deviceMixpileRwdLog1.setQbroker(broker);
                    deviceMixpileRwdLog1.setTopicAll(topic);
                    deviceMixpileRwdLog1.setPushjson(JSONUtil.toJsonStr(one));
                    if (one1 == null) {
                        deviceMixpileRwdLog1.setRjrwd(one.getRjrwd());
                        deviceMixpileRwdLog1.setShebeino(one.getShebeino());
                        deviceMixpileRwdLog1.setIssuedtime(new Date());
                        deviceMixpileRwdLogService.save(deviceMixpileRwdLog1);
                    } else {
                        deviceMixpileRwdLog1.setId(one1.getId());
                        deviceMixpileRwdLog1.setIssuedtime(new Date());
                        deviceMixpileRwdLogService.updateById(deviceMixpileRwdLog1);
                    }
                    return Result.OK("工单下发成功！");
                } else {
                    return Result.error("工单下发失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("工单下发失败！");
            }
        } else {
            return Result.OK("下发工单数已达上限！");
        }
    }

    private static MqttClient getMqttClient(String broker, String clientId) {
        try {
            MqttClient pubClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(false);
            //System.out.println("Connecting to broker: " + broker);
            pubClient.connect(connectOptions);
            return pubClient;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 软基工单超标率统计
    @GetMapping(value = "/stalist")
    public Result<?> queryPageList2(DeviceMixpileRwd deviceMixpileRwd,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        QueryWrapper<DeviceMixpileRwd> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileRwd, req.getParameterMap());
        List<Map<String, Object>> pageList = deviceMixpileRwdService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param deviceMixpileRwd
     * @return
     */
    @AutoLog(value = "软基工单表-添加")
    @ApiOperation(value = "软基工单表-添加", notes = "软基工单表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceMixpileRwd deviceMixpileRwd) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
        deviceMixpileRwd.setRjrwd("RJRWD-" + format.format(date));
        if (StrUtil.isNotBlank(deviceMixpileRwd.getMsgperson())) {
            BhzPhone rjrwPhone = bhzPhoneService.selectBhzPhone(deviceMixpileRwd.getMsgperson());
            if (null != rjrwPhone) {
                String phones = rjrwPhone.getPhones();
//                        primaryNames = bhzPhone.getNames();
            }
        }
        //	SysMessage sysMessage = new SysMessage();

        deviceMixpileRwdService.save(deviceMixpileRwd);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceMixpileRwd
     * @return
     */
    @AutoLog(value = "软基工单表-编辑")
    @ApiOperation(value = "软基工单表-编辑", notes = "软基工单表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceMixpileRwd deviceMixpileRwd) {
        deviceMixpileRwdService.updateById(deviceMixpileRwd);
        return Result.OK("编辑成功!");
    }

    @AutoLog(value = "软基工单表-编辑")
    @ApiOperation(value = "软基工单表-编辑", notes = "软基工单表-编辑")
    @PostMapping(value = "/editRwd")
    public Result<?> editRwd(@RequestBody DeviceMixpileRwd deviceMixpileRwd) {
        QueryWrapper<DeviceMixpileRwd> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rjrwd",deviceMixpileRwd.getRjrwd());
        DeviceMixpileRwd one = deviceMixpileRwdService.getOne(queryWrapper);
        deviceMixpileRwd.setId(one.getId());
        deviceMixpileRwdService.updateById(deviceMixpileRwd);
        return Result.OK("编辑成功!");
    }

    /**
     * 修改软基工单状态接口（对外开放）
     *
     * @param deviceMixpileRwd
     * @return
     */
    @AutoLog(value = "软基工单表-修改软基工单状态接口")
    @ApiOperation(value = "软基工单表-修改软基工单接口", notes = "软基工单表-修改软基工单状态接口")
    @PutMapping(value = "/editOpen")
    public Result<?> editOpen(@RequestBody DeviceMixpileRwd deviceMixpileRwd) {
        if (!StringUtils.isEmpty(deviceMixpileRwd.getRjrwd())) {
            QueryWrapper<DeviceMixpileRwd> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("rjrwd", deviceMixpileRwd.getRjrwd());
            DeviceMixpileRwd deviceMixpileRwd1 = deviceMixpileRwdService.getOne(queryWrapper);
            if (deviceMixpileRwd1 != null) {
                deviceMixpileRwd.setId(deviceMixpileRwd1.getId());
                deviceMixpileRwdService.updateById(deviceMixpileRwd);
                return Result.OK("软基工单状态修改成功!");
            } else {
                return Result.error("暂无该软基工单！");
            }
        } else {
            return Result.error("软基工单单号为空!");
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "软基工单表-通过id删除")
    @ApiOperation(value = "软基工单表-通过id删除", notes = "软基工单表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceMixpileRwdService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "软基工单表-批量删除")
    @ApiOperation(value = "软基工单表-批量删除", notes = "软基工单表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceMixpileRwdService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "软基工单表-通过id查询")
    @ApiOperation(value = "软基工单表-通过id查询", notes = "软基工单表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceMixpileRwd deviceMixpileRwd = deviceMixpileRwdService.getById(id);
        if (deviceMixpileRwd == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceMixpileRwd);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceMixpileRwd
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileRwd deviceMixpileRwd) {
        return super.exportXls(request, deviceMixpileRwd, DeviceMixpileRwd.class, "软基工单表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DeviceMixpileRwd.class);
    }

}
