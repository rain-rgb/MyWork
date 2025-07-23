package com.trtm.iot.deviceMixpileHistorydataOne.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.mapper.DeviceMixpileHistorydataOneMapper;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileHistorydataOnePage;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileOneHandler;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileOneHandlerPage;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.OneVO;
import com.trtm.iot.devicemixpilehistorydatapart.service.IDeviceMixpileHistorydataPartService;
import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.trtm.iot.devicemixpileoneoverhandler.service.IMixpileOneOverHandlerService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.trtm.iot.util.GPSUtil.*;

/**
 * @Description: device_mixpile_historydata_one
 * @Author: jeecg-boot
 * @Date: 2021-10-24
 * @Version: V1.0
 */
@Api(tags = "device_mixpile_historydata_one")
@RestController
@RequestMapping("/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne")
@Slf4j
public class DeviceMixpileHistorydataOneController extends JeecgController<DeviceMixpileHistorydataOne, IDeviceMixpileHistorydataOneService> {
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    private IDeviceMixpileHistorydataPartService deviceMixpileHistorydataPartService;
    @Autowired
    DeviceMixpileHistorydataOneMapper deviceMixpileHistorydataOneMapper;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IMixpileOneOverHandlerService mixpileOneOverHandlerService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param deviceMixpileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/list")
//    @PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceMixpileHistorydataOneList")
    public Result<?> queryPageList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        if (!StringUtils.isEmpty(deviceMixpileHistorydataOne.getPileMileage())) {
            deviceMixpileHistorydataOne.setPileMileage("*" + deviceMixpileHistorydataOne.getPileMileage() + "*");
        }

        //deviceMixpileHistorydataOne.setPiletype(0);
        deviceMixpileHistorydataOne.setPileNo("*" + deviceMixpileHistorydataOne.getPileNo() + "*");
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.notLike("pile_no", "-0");
        if (loginUser.getOrgCode().contains("A05A01A08A16A03")) {
            queryWrapper.orderByAsc("pile_mileage").orderByAsc("CAST(pile_no AS SIGNED)");
        }
        Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
        List<DeviceMixpileHistorydataOne> records = pageList.getRecords();
        for (DeviceMixpileHistorydataOne record : records) {
            record.setPileCement((record.getPileCement() != null) ? String.format("%.2f", Double.parseDouble(record.getPileCement())) : "");

            record.setPileRealdep((record.getPileRealdep() != null) ? String.format("%.2f", Double.parseDouble(record.getPileRealdep())) : "");

            record.setPileDownbeton((record.getPileDownbeton() != null) ? String.format("%.2f", Double.parseDouble(record.getPileDownbeton())) : "");

            record.setPileDspeed((record.getPileDspeed() != null) ? String.format("%.2f", Double.parseDouble(record.getPileDspeed())) : "");

            record.setPileDelectr((record.getPileDelectr() != null) ? String.format("%.2f", Double.parseDouble(record.getPileDelectr())) : "");

            record.setPileUobeton((record.getPileUobeton() != null) ? String.format("%.2f", Double.parseDouble(record.getPileUobeton())) : "");

            record.setPileUspeed((record.getPileUspeed() != null) ? String.format("%.2f", Double.parseDouble(record.getPileUspeed())) : "");

            record.setPileUelectr((record.getPileUelectr() != null) ? String.format("%.2f", Double.parseDouble(record.getPileUelectr())) : "");

            if (record.getPileRatio() != null) {
                record.setPileRatio(String.format("%.2f", Double.parseDouble(record.getPileRatio())));//水灰比
            } else {
                record.setPileRatio(String.format("%.2f", 50.00));//水灰比
            }
            record.setPileLgd((record.getPileLgd() != null && !record.getPileLgd().isEmpty()) ? String.format("%.2f", Double.parseDouble(record.getPileLgd())) : "");

            record.setPileLtd((record.getPileLtd() != null && !record.getPileLtd().isEmpty()) ? String.format("%.2f", Double.parseDouble(record.getPileLtd())) : "");

            record.setPileX((record.getPileX() != null && !record.getPileX().isEmpty()) ? String.format("%.2f", Double.parseDouble(record.getPileX())) : "");

            record.setPileY((record.getPileY() != null && !record.getPileY().isEmpty()) ? String.format("%.2f", Double.parseDouble(record.getPileY())) : "");

            if (ObjectUtil.isEmpty(record.getPileMaxelec())) {
                if (ObjectUtil.isNotEmpty(record.getPileMaxelectr())) {
                    record.setPileMaxelec(String.format("%.2f", Double.parseDouble(record.getPileMaxelectr())));//峰值电流
                }
            } else {
                record.setPileMaxelec(String.format("%.2f", Double.parseDouble(record.getPileMaxelec())));//峰值电流
            }
            if (Double.parseDouble(record.getPileDensity()) > 1000) {
                record.setPileDensity(String.format("%.3f", Double.parseDouble(record.getPileDensity()) / 1000));//水泥浆比重
            }
            double pjdep = 0.0;
            if (!StringUtil.isEmpty(record.getPileMinelec()) && !"0".equals(record.getPileMinelec())) {
                if (Double.parseDouble(record.getPileMinelec()) >= 55) {
                    pjdep = Double.parseDouble(record.getPileMinelec());
                } else if (!StringUtil.isEmpty(record.getPileCement()) && !StringUtil.isEmpty(record.getPileRealdep())) {
                    double pileCement = Double.parseDouble(record.getPileCement());
                    double pileRealdep = Double.parseDouble(record.getPileRealdep());
                    if (pileRealdep != 0) {
                        if(record.getShebeino().equals("ytwnd1bsnjbz02") || record.getShebeino().equals("ytwnd1bsnjbz01")){
                            pjdep = pileCement / pileRealdep;
                        }else if (!StringUtils.isEmpty(record.getEmptydep())) {
                            pjdep = pileCement / (pileRealdep - 0.25 - Double.parseDouble(record.getEmptydep()));
                        } else {
                            pjdep = pileCement / (pileRealdep - 0.25);
                        }

                    }
                }
            } else if (!StringUtil.isEmpty(record.getPileCement()) && !StringUtil.isEmpty(record.getPileRealdep())) {
                double pileCement = Double.parseDouble(record.getPileCement());
                double pileRealdep = Double.parseDouble(record.getPileRealdep());
                if (pileRealdep != 0) {
                    if(record.getShebeino().equals("ytwnd1bsnjbz02") || record.getShebeino().equals("ytwnd1bsnjbz01")){
                        pjdep = pileCement / pileRealdep;
                    }else if (!StringUtils.isEmpty(record.getEmptydep())) { // 空搅工艺
                        pjdep = pileCement / (pileRealdep - 0.25 - Double.parseDouble(record.getEmptydep()));
                    } else {
                        pjdep = pileCement / (pileRealdep - 0.25);
                    }
                }
            }
            record.setPjdep(String.format("%.3f", pjdep));
//            if (record.getPileLgd() != null && record.getPileLtd() != null) {
//                double lon = Double.parseDouble(record.getPileLgd());
//                double lat = Double.parseDouble(record.getPileLtd());
//                double lat1 = formatLnglat(lat);
//                double lon1 = formatLnglat(lon);
//                double[] zuobiao = gps84_To_Gcj02(lat1, lon1);
//                Double lat2 = retain6(zuobiao[0]);
//                Double lon2 = retain6(zuobiao[1]);
//                record.setPileLgd(String.valueOf(lon2));
//                record.setPileLtd(String.valueOf(lat2));
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String pileTime = record.getPileTime();
            if (!StringUtils.isEmpty(pileTime)) {
                if (pileTime.contains("/")) {
                    try {
                        // 将原始时间字符串解析为Date对象
                        Date date = originalFormat.parse(pileTime);
                        // 将Date对象格式化为目标格式的字符串
                        pileTime = dateFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Date end = dateFormat.parse(pileTime);
                if (!StringUtils.isEmpty(record.getPileDowntime()) && !StringUtils.isEmpty(record.getPileUptime())) {
                    String downtimeStr = record.getPileDowntime();
                    String uptimeStr = record.getPileUptime();

                    // 使用正则表达式简单检查字符串是否符合double的格式
                    boolean isDowntimeValid = downtimeStr.matches("-?\\d+(\\.\\d+)?");
                    boolean isUptimeValid = uptimeStr.matches("-?\\d+(\\.\\d+)?");

                    if (isDowntimeValid && isUptimeValid) {
                        try {
                            double downtime = Double.parseDouble(downtimeStr);
                            double uptime = Double.parseDouble(uptimeStr);

                            // 根据需求选择取整方式，这里依然采用下取整
                            int downtimeInt = (int) Math.floor(downtime);
                            int uptimeInt = (int) Math.floor(uptime);

                            int time = downtimeInt + uptimeInt;
                            Long starttimes = end.getTime() - time * 1000;
                            record.setPileStarttime(dateFormat.format(starttimes));
                        } catch (NumberFormatException e) {
                            // 即便前面已经做了检查，但理论上这里仍然可能捕获到异常，例如数字超出整型范围
                            System.err.println("Unexpected error during conversion: " + e.getMessage());
                        }
                    } else {
                        System.err.println("Invalid input for downtime or uptime: " + downtimeStr + ", " + uptimeStr);
                    }

                }
            }
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/hlist")
    @PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceHMixpileHistorydataOneList")
    public Result<?> queryPageHList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        deviceMixpileHistorydataOne.setPiletype(1);
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
//        List<DeviceMixpileHistorydataOne> records = pageList.getRecords();
//        for (DeviceMixpileHistorydataOne record : records) {
//
//            if (record.getPileLgd() != null && record.getPileLtd() != null) {
//                double lon = Double.parseDouble(record.getPileLgd());
//                double lat = Double.parseDouble(record.getPileLtd());
//                double lat1 = formatLnglat(lat);
//                double lon1 = formatLnglat(lon);
//                double[] zuobiao = gps84_To_Gcj02(lat1, lon1);
//                Double lat2 = retain6(zuobiao[0]);
//                Double lon2 = retain6(zuobiao[1]);
//                record.setPileLgd(String.valueOf(lon2));
//                record.setPileLtd(String.valueOf(lat2));
//            }
//        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/stalist")
    public Result<?> queryPagestaList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String sysOrgCode = null;
        if (!StringUtils.isEmpty(sys_depart_orgcode)) {
            sysOrgCode = sys_depart_orgcode;
        } else {
            sysOrgCode = loginUser.getOrgCode();
        }
        List<ShebeiInfo> shebeiInfoList = shebeiInfoService.usershebeiList(sysOrgCode, Arrays.asList("16", "19", "53", "54"));
        List list = new ArrayList();
        if (shebeiInfoList.size() > 0) {
            for (ShebeiInfo shebeiInfo : shebeiInfoList) {
                list.add(shebeiInfo.getSbjno());
            }
        }
//        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        //查询到他的设备编号
//        if (deviceMixpileHistorydataOne.getShebeino() == null) {
//            if (!"null".equals(shebei)) {
//                deviceMixpileHistorydataOne.setShebeino(shebei);
//            } else {
//                deviceMixpileHistorydataOne.setShebeino("空");
//            }
//        }
        // round( sum(pile_cement), 2 ) allbeton,
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.select("id,shebeino,round( sum(pile_realdep), 2 ) pile_realdep,round( sum(pile_cement), 2 ) pile_cement,COUNT(shebeino) alertstate");
        if (list.size() > 0) {
            queryWrapper.in("shebeino", list);
        }
        queryWrapper.groupBy("shebeino");
        Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
//        List<DeviceMixpileHistorydataOne> records = pageList.getRecords();
//        for (DeviceMixpileHistorydataOne record : records) {
//
//            if (record.getPileLgd() != null && record.getPileLtd() != null) {
//                double lon = Double.parseDouble(record.getPileLgd());
//                double lat = Double.parseDouble(record.getPileLtd());
//                double lat1 = formatLnglat(lat);
//                double lon1 = formatLnglat(lon);
//                double[] zuobiao = gps84_To_Gcj02(lat1, lon1);
//                Double lat2 = retain6(zuobiao[0]);
//                Double lon2 = retain6(zuobiao[1]);
//                record.setPileLgd(String.valueOf(lon2));
//                record.setPileLtd(String.valueOf(lat2));
//            }
//        }
        return Result.OK(pageList);
    }


    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/cblist")
    //@PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceMixpileHistorydataOneList")
    public Result<?> queryPageCBList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null || deviceMixpileHistorydataOne.getShebeino().isEmpty()) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        Map<Object, Object> map = new HashMap<>();
        List<Integer> piletypeList = new ArrayList<>();
        if (deviceMixpileHistorydataOne.getPiletype() == 0) {
            piletypeList.add(0);
            piletypeList.add(2);
        }
        if (deviceMixpileHistorydataOne.getPiletype() == 1) {
            piletypeList.add(1);
            piletypeList.add(3);
        }
        deviceMixpileHistorydataOne.setPiletype(null);
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.in("piletype", piletypeList);
        queryWrapper.notLike("pile_no", "-0");
        Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
        List<DeviceMixpileHistorydataOne> records = pageList.getRecords();
        List<DeviceMixpileOneHandler> handlerecords = new ArrayList<DeviceMixpileOneHandler>();
        for (DeviceMixpileHistorydataOne record : records) {
            DeviceMixpileOneHandler handlerecord = new DeviceMixpileOneHandler();
//            if (record.getPileLgd() != null && record.getPileLtd() != null) {
//                double lon = Double.parseDouble(record.getPileLgd());
//                double lat = Double.parseDouble(record.getPileLtd());
//                double lat1 = formatLnglat(lat);
//                double lon1 = formatLnglat(lon);
//                double[] zuobiao = gps84_To_Gcj02(lat1, lon1);
//                Double lat2 = retain6(zuobiao[0]);
//                Double lon2 = retain6(zuobiao[1]);
//                record.setPileLgd(String.valueOf(lon2));
//                record.setPileLtd(String.valueOf(lat2));
//            }
            BeanUtils.copyProperties(record, handlerecord);
            // 关联处置表
            QueryWrapper<MixpileOneOverHandler> queryWrapperhandle = new QueryWrapper<>();
            queryWrapperhandle.eq("baseid", record.getId());
            MixpileOneOverHandler handle = mixpileOneOverHandlerService.getOne(queryWrapperhandle);

            if (handle == null) {
                MixpileOneOverHandler handle2 = new MixpileOneOverHandler();
                handle2.setOverproofStatus(0);
                handlerecord.setHandler(handle2);
            } else {
                handlerecord.setHandler(handle);
            }
            handlerecords.add(handlerecord);

        }
        IPage<DeviceMixpileOneHandler> pageListhandle = new Page<DeviceMixpileOneHandler>(pageNo, pageSize);
        pageListhandle.setRecords(handlerecords);
        pageListhandle.setCurrent(pageList.getCurrent());
        pageListhandle.setPages(pageList.getPages());
        pageListhandle.setSize(pageList.getSize());
        pageListhandle.setTotal(pageList.getTotal());

        return Result.OK(pageListhandle);
    }

    public Result<?> queryPageCBDataList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null || deviceMixpileHistorydataOne.getShebeino().isEmpty()) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        Map<Object, Object> map = new HashMap<>();
        List<Integer> piletypeList = new ArrayList<>();
        if (deviceMixpileHistorydataOne.getPiletype() == 0) {
            piletypeList.add(0);
            piletypeList.add(2);
        }
        if (deviceMixpileHistorydataOne.getPiletype() == 1) {
            piletypeList.add(1);
            piletypeList.add(3);
        }
        deviceMixpileHistorydataOne.setPiletype(null);
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.in("piletype", piletypeList);
//        queryWrapper.eq("gzcount", 1);
        queryWrapper.notLike("pile_no", "-0");
        queryWrapper.groupBy("shebeino,pile_mileage,pile_no");
        Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
        List<DeviceMixpileHistorydataOne> records = pageList.getRecords();
        List<DeviceMixpileOneHandler> handleRecords = new ArrayList<DeviceMixpileOneHandler>();
        for (DeviceMixpileHistorydataOne oneRecord : records) {//超标数据
            DeviceMixpileOneHandler deviceMixpileOneHandler = new DeviceMixpileOneHandler();
            BeanUtils.copyProperties(oneRecord, deviceMixpileOneHandler);
            List<DeviceMixpileOneHandlerPage> deviceMixpileOneHandlerPages = new ArrayList<>();//新建handlePage数组
            DeviceMixpileOneHandlerPage handlePage1 = new DeviceMixpileOneHandlerPage();//新建handlePage对象
            BeanUtils.copyProperties(oneRecord, handlePage1);//将one数据复制给handlePage对象
            // 关联处置表
            QueryWrapper<MixpileOneOverHandler> queryWrapperhandle = new QueryWrapper<>();
            queryWrapperhandle.eq("baseid", handlePage1.getId());
            MixpileOneOverHandler handle = mixpileOneOverHandlerService.getOne(queryWrapperhandle);
            if (ObjectUtil.isEmpty(handle)) {
                MixpileOneOverHandler handle2 = new MixpileOneOverHandler();
                handle2.setOverproofStatus(0);
                handlePage1.setHandler(handle2);
            } else {
                handlePage1.setHandler(handle);
            }
            deviceMixpileOneHandlerPages.add(handlePage1);
            if (oneRecord.getGzcount() == 1) {
                List<DeviceMixpileHistorydataOne> fujiaos = deviceMixpileHistorydataOneService.getCBFJ(oneRecord, 2);//查询超标复搅数据
                if (fujiaos.size() > 0) {
                    for (DeviceMixpileHistorydataOne fujiao : fujiaos) {
                        DeviceMixpileOneHandlerPage handlePage2 = new DeviceMixpileOneHandlerPage();
                        BeanUtils.copyProperties(fujiao, handlePage2);
                        // 关联处置表
                        QueryWrapper<MixpileOneOverHandler> queryWrapperhandle2 = new QueryWrapper<>();
                        queryWrapperhandle2.eq("baseid", handlePage2.getId());
                        MixpileOneOverHandler handle3 = mixpileOneOverHandlerService.getOne(queryWrapperhandle2);
                        if (ObjectUtil.isEmpty(handle3)) {
                            MixpileOneOverHandler handle4 = new MixpileOneOverHandler();
                            handle4.setOverproofStatus(0);
                            handlePage2.setHandler(handle4);
                        } else {
                            handlePage2.setHandler(handle3);
                        }
                        deviceMixpileOneHandlerPages.add(handlePage2);
                    }
                }
            }
            deviceMixpileOneHandler.setDeviceMixpileOneHandlerPages(deviceMixpileOneHandlerPages);
            handleRecords.add(deviceMixpileOneHandler);
        }
        IPage<DeviceMixpileOneHandler> pageListhandle = new Page<DeviceMixpileOneHandler>(pageNo, pageSize);
        pageListhandle.setRecords(handleRecords);
        pageListhandle.setCurrent(pageList.getCurrent());
        pageListhandle.setPages(pageList.getPages());
        pageListhandle.setSize(pageList.getSize());
        pageListhandle.setTotal(pageList.getTotal());
        return Result.OK(pageListhandle);
    }

    @AutoLog(value = "device_mixpile_historydata_one-超标查询详情")
    @ApiOperation(value = "device_mixpile_historydata_one-超标查询详情", notes = "device_mixpile_historydata_one-超标查询详情")
    @GetMapping(value = "/queryInfoList")
    public Result<?> queryInfoList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                   String ids,
                                   HttpServletRequest req) {
        List<Integer> id = new ArrayList<>();
        String[] split = ids.split(",");
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            id.add(Integer.parseInt(s));
        }
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", id);
        List<DeviceMixpileHistorydataOne> list = deviceMixpileHistorydataOneService.list(queryWrapper);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (DeviceMixpileHistorydataOne one : list) {
            one.setShebeino(shebeiInfoService.selectshebeione(one.getShebeino()).getSbname());
            one.setPileRealdep(String.format("%.2f", Double.parseDouble(one.getPileRealdep())));//实际桩长
            one.setPileDspeed(String.format("%.2f", Double.parseDouble(one.getPileDspeed())));//下钻平均速度
            one.setPileDelectr(String.format("%.2f", Double.parseDouble(one.getPileDelectr())));//下钻平均电流
            one.setPileUspeed(String.format("%.2f", Double.parseDouble(one.getPileUspeed())));//上钻平均速度
            one.setPileUelectr(String.format("%.2f", Double.parseDouble(one.getPileUelectr())));//上钻平均速度
            one.setPileY(String.format("%.2f", Double.parseDouble(one.getPileY())));//下钻平均速度
            if (ObjectUtil.isEmpty(one.getPileMaxelec())) {//峰值电流
                if (ObjectUtil.isNotEmpty(one.getPileMaxelectr())) {
                    one.setPileMaxelec(String.format("%.2f", Double.parseDouble(one.getPileMaxelectr())));
                }
            } else {
                one.setPileMaxelec(String.format("%.2f", Double.parseDouble(one.getPileMaxelec())));
            }
            if (ObjectUtil.isNotEmpty(one.getPileCement())) {//每米水泥用量
                one.setPileMinelec(String.format("%.2f", Double.parseDouble(one.getPileCement()) / Double.parseDouble(one.getPileRealdep())));
            }
            if (!StringUtils.isEmpty(one.getPileTime())) {//桩开始时间
                try {
                    Date end = dateFormat.parse(one.getPileTime());
                    if (!StringUtils.isEmpty(one.getPileDowntime()) && !StringUtils.isEmpty(one.getPileUptime())) {
                        int time = Integer.parseInt(one.getPileDowntime()) + Integer.parseInt(one.getPileUptime());
                        Long starttimes = end.getTime() - time * 1000;
                        one.setPileStarttime(dateFormat.format(starttimes));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK(list);
    }

    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/cblistdc")
    //@PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceMixpileHistorydataOneList")
    public Result<?> queryPageCBListdc(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null || deviceMixpileHistorydataOne.getShebeino().isEmpty()) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        Map<Object, Object> map = new HashMap<>();
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.notLike("pile_no", "-0");
        List<DeviceMixpileHistorydataOne> records = deviceMixpileHistorydataOneService.list(queryWrapper);
        for (DeviceMixpileHistorydataOne record : records) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeino());
            record.setShebeino(selectshebeione.getSbname());
            if (Double.parseDouble(record.getPileDensity()) > 1000) {
                record.setPileDensity(String.format("%.3f", Double.parseDouble(record.getPileDensity()) / 1000));//水泥浆比重
            }
            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileRealdep())));//实际桩长(m)
            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileDspeed())));//下钻平均速度(m/min)
            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileDelectr())));//下钻平均电流(A)
            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileDownbeton())));//下钻浆量(方)
            if (record.getPileUspeed() != null) {
                record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileUspeed())));//提钻平均速度(m/min)
            } else {
                record.setPileDensity(String.format("%.2f", 0.0));//提钻平均速度(m/min)
            }

            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileUelectr())));//提钻平均电流(A)
            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileUobeton())));//提钻浆量(方)
            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileDensity())));//水泥浆比重
            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileCement())));//水泥用量(KG)
            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileRealdep()) / 100));//水灰比(%)
            record.setPileDensity(String.format("%.3f", Double.parseDouble(record.getPileRealdep())));//倾角
            double pjdep = 0.0;
            if (!StringUtil.isEmpty(record.getPileMinelec()) && !"0".equals(record.getPileMinelec())) {
                if (Double.parseDouble(record.getPileMinelec()) >= 55) {
                    pjdep = Double.parseDouble(record.getPileMinelec());
                } else if (!StringUtil.isEmpty(record.getPileCement()) && !StringUtil.isEmpty(record.getPileRealdep())) {
                    double pileCement = Double.parseDouble(record.getPileCement());
                    double pileRealdep = Double.parseDouble(record.getPileRealdep());
                    if (pileRealdep != 0) {
                        if (!StringUtils.isEmpty(record.getEmptydep())) { // 空搅工艺
                            pjdep = pileCement / (pileRealdep - 0.25 - Double.parseDouble(record.getEmptydep()));
                        } else {
                            pjdep = pileCement / (pileRealdep - 0.25);
                        }
                    }
                }
            } else if (!StringUtil.isEmpty(record.getPileCement()) && !StringUtil.isEmpty(record.getPileRealdep())) {
                double pileCement = Double.parseDouble(record.getPileCement());
                double pileRealdep = Double.parseDouble(record.getPileRealdep());
                if (pileRealdep != 0) {
                    if (!StringUtils.isEmpty(record.getEmptydep())) { // 空搅工艺
                        pjdep = pileCement / (pileRealdep - 0.25 - Double.parseDouble(record.getEmptydep()));
                    } else {
                        pjdep = pileCement / (pileRealdep - 0.25);
                    }
                }
            }
            record.setPjdep(String.format("%.3f", pjdep));

        }

        return Result.OKs(records);
    }

    @AutoLog(value = "预警处置流程信息查询")
    @ApiOperation(value = "预警处置流程信息查询", notes = "预警处置流程信息查询")
    @GetMapping(value = "/handlelist")
    public Result<?> handlelist(
            @RequestParam(name = "baseid") String baseid,
            HttpServletRequest req) throws ParseException {


        List<Map<String, Object>> handleprosess = new ArrayList<>();
        DeviceMixpileHistorydataOne one = deviceMixpileHistorydataOneService.getById(baseid);

        Map<String, Object> yujin = new HashMap<>();
        yujin.put("tile", "预警");
        Map<String, Object> chuzhi = new HashMap<>();
        chuzhi.put("tile", "处置");
        Map<String, Object> bohui = new HashMap<>();
        bohui.put("tile", "驳回");
        Map<String, Object> checked = new HashMap<>();
        checked.put("tile", "审核");
        Map<String, Object> wancheng = new HashMap<>();
        if (one.getChaobiaodengji() > 0) {

            yujin.put("time", one.getPileTime());
            yujin.put("person", "");
            yujin.put("way", "");
            yujin.put("reason", one.getProblem());
            yujin.put("file", "");
            //handleprosess.add(yujin);

        }

        // 处置信息
        QueryWrapper<MixpileOneOverHandler> queryWrapper = new QueryWrapper();
        queryWrapper.eq("baseId", baseid);
        MixpileOneOverHandler handle = mixpileOneOverHandlerService.getOne(queryWrapper);
        if (null == handle) {
            handle = new MixpileOneOverHandler();
        }

        chuzhi.put("time", handle.getHandleTime());
        chuzhi.put("person", handle.getHandlePerson());
        chuzhi.put("way", handle.getHandleWay());
        chuzhi.put("reason", handle.getHandleResult());
        chuzhi.put("file", handle.getFilePath2());
        //handleprosess.add(chuzhi);

        if (handle.getOverproofStatus() != null && handle.getOverproofStatus() == 30) {

            bohui.put("time", handle.getSupervisorHandleTime());
            bohui.put("person", handle.getApprovalPerson());
            bohui.put("way", "");
            bohui.put("reason", handle.getRemark());
            bohui.put("file", "");
            wancheng.put("tile", "完成");
            wancheng.put("time", null);
            handleprosess.add(wancheng);
            handleprosess.add(bohui);
        } else {

            checked.put("time", handle.getSupervisorHandleTime());
            checked.put("person", handle.getApprovalPerson());
            checked.put("way", handle.getSupervisorApproval());
            checked.put("reason", handle.getSupervisorResult());
            checked.put("file", handle.getFilePath());
            wancheng.putAll(checked);
            wancheng.put("tile", "完成");
            handleprosess.add(wancheng);
            handleprosess.add(checked);
        }

        handleprosess.add(chuzhi);
        handleprosess.add(yujin);


        return Result.OK(handleprosess);
    }

    /**
     * 分页列表查询
     *
     * @param deviceMixpileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/lists")
//    @PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceMixpileHistorydataOneList")
    public Result<?> queryPageLists(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        if (!StringUtils.isEmpty(deviceMixpileHistorydataOne.getPileMileage())) {
            deviceMixpileHistorydataOne.setPileMileage("*" + deviceMixpileHistorydataOne.getPileMileage() + "*");
        }

        //deviceMixpileHistorydataOne.setPiletype(0);
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.notLike("pile_no", "-0");
        Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
//        List<DeviceMixpileHistorydataOne> records = pageList.getRecords();
//        for (DeviceMixpileHistorydataOne record : records) {
//
//            if (record.getPileLgd() != null && record.getPileLtd() != null) {
//                double lon = Double.parseDouble(record.getPileLgd());
//                double lat = Double.parseDouble(record.getPileLtd());
//                double lat1 = formatLnglat(lat);
//                double lon1 = formatLnglat(lon);
//                double[] zuobiao = gps84_To_Gcj02(lat1, lon1);
//                Double lat2 = retain6(zuobiao[0]);
//                Double lon2 = retain6(zuobiao[1]);
//                record.setPileLgd(String.valueOf(lon2));
//                record.setPileLtd(String.valueOf(lat2));
//            }
//        }
        return Result.OK(pageList);
    }

    // 水泥搅拌桩超标查询
    @GetMapping(value = "/cbcxlist")
//    @PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceMixpileHistorydataOneCBCXList")
    public Result<?> queryPageCBCXList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        deviceMixpileHistorydataOne.setPiletype(0);
        deviceMixpileHistorydataOne.setChaobiaodengji(1);
        return queryPageCBDataList(deviceMixpileHistorydataOne, pageNo, pageSize, req);

    }

    // 水泥搅拌桩超标处理
    @GetMapping(value = "/cbcllist")
//    @PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceMixpileHistorydataOneCBCLList")
    public Result<?> queryPageCBCLList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        deviceMixpileHistorydataOne.setPiletype(0);
        deviceMixpileHistorydataOne.setChaobiaodengji(1);
        return queryPageCBDataList(deviceMixpileHistorydataOne, pageNo, pageSize, req);

    }

    // 水泥搅拌桩超标处理导出
    @GetMapping(value = "/cbcllistdc")
//    @PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceMixpileHistorydataOneCBCLList")
    public Result<?> queryPageCBCLListdc(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {

        deviceMixpileHistorydataOne.setChaobiaodengji(1);
        return queryPageCBListdc(deviceMixpileHistorydataOne, pageNo, pageSize, req);

    }

    // 高压旋喷桩桩超标查询
    @GetMapping(value = "/cbcxhlist")
    @PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceHMixpileHistorydataOneCBCXList")
    public Result<?> queryPageCBCXHList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) {
        deviceMixpileHistorydataOne.setPiletype(1);
        deviceMixpileHistorydataOne.setChaobiaodengji(1);
        return queryPageCBList(deviceMixpileHistorydataOne, pageNo, pageSize, req);

    }

    // 高压旋喷桩超标处理
    @GetMapping(value = "/cbclhlist")
    @PermissionData(pageComponent = "snjbz/devicemixpilehistorydataone/DeviceHMixpileHistorydataOneCBCLList")
    public Result<?> queryPageCBCLHList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) {

        deviceMixpileHistorydataOne.setPiletype(1);
        deviceMixpileHistorydataOne.setChaobiaodengji(1);
        return queryPageCBList(deviceMixpileHistorydataOne, pageNo, pageSize, req);

    }


    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/listAll")
    public Result<?> listAll(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                             HttpServletRequest req) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        deviceMixpileHistorydataOne.setPileNo('*' + deviceMixpileHistorydataOne.getPileNo() + '*');
        //deviceMixpileHistorydataOne.setPiletype(0);
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
        List<DeviceMixpileHistorydataOne> records = pageList.getRecords();
        for (DeviceMixpileHistorydataOne record : records) {

            if (record.getPileLgd() != null && record.getPileLtd() != null) {
                double lon = Double.parseDouble(record.getPileLgd());
                double lat = Double.parseDouble(record.getPileLtd());
                double lat1 = formatLnglat(lat);
                double lon1 = formatLnglat(lon);
                double[] zuobiao = gps84_To_Gcj02(lat1, lon1);
                Double lat2 = retain6(zuobiao[0]);
                Double lon2 = retain6(zuobiao[1]);
                record.setPileLgd(String.valueOf(lon2));
                record.setPileLtd(String.valueOf(lat2));
            }
            if (!StringUtils.isEmpty(record.getPileDowntime()) && !StringUtils.isEmpty(record.getPileUptime()) && record.getPileTime() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int time = Integer.parseInt(record.getPileDowntime()) + Integer.parseInt(record.getPileDowntime());
                Date end = dateFormat.parse(record.getPileTime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(end);
                calendar.add(Calendar.SECOND, -time);
                record.setPileStarttime(dateFormat.format(calendar.getTime()));
            }
        }
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param deviceMixpileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        if (deviceMixpileHistorydataOne.getPileTime() != null) {
            deviceMixpileHistorydataOne.setPileTime(deviceMixpileHistorydataOne.getPileTime() + "*");
        }
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.select(" ifnull(count( * ),0) size,  " +
                "  round( SUM(IFNULL( pile_realdep, 0 )),2 ) zonglong,  " +
                "  round( SUM(IFNULL( pile_downbeton, 0 ) + IFNULL( pile_uobeton, 0 ) ),2 ) sumzjl, " +
                "  round( SUM(IFNULL( pile_cement, 0 )),2 ) cementall,  " +
                "  count( CASE WHEN chaobiaodengji > 0 THEN  1  END)  chaobiaocount");
        Map<String, Object> map = deviceMixpileHistorydataOneService.getMap(queryWrapper);
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper1.select("sum(pile_realdep) pile_realdep");
        queryWrapper1.eq("chaobiaodengji", 1);
        DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 = deviceMixpileHistorydataOneService.getOne(queryWrapper1);
        double exceededm = 0.0;
        if (deviceMixpileHistorydataOne1 != null) {
            if (!StringUtils.isEmpty(deviceMixpileHistorydataOne1.getPileRealdep())) {
                exceededm = Double.parseDouble(deviceMixpileHistorydataOne1.getPileRealdep());
            }
        }
        map.put("exceededm", String.format("%.2f", exceededm));
//		List<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.list(queryWrapper);
//		Map map=new HashMap();
//		Double sumzjl=0.0;
//		Double zonglong=0.0;
//		Integer size=0;
//		Integer chaobiaocount = 0;
//		String shebeis=null;
//		for (DeviceMixpileHistorydataOne record : pageList) {
//			String shebei1 = record.getShebeino();
//			size+=1;
//			if(record.getPileRealdep()!=null){
//				zonglong+= Double.valueOf(record.getPileRealdep());
//			}
//			if(shebeis==null){
//				shebeis = record.getShebeino();
//				if(record.getPileFlowtotal()!=null){
//					sumzjl+=  Double.valueOf(record.getPileFlowtotal());
//				}
//			}else if(shebeis!=null&&!shebeis.equals(shebei1)&&record.getPileFlowtotal()!=null){
//				sumzjl+=  Double.valueOf(record.getPileFlowtotal());
//			}
//			if(record.getChaobiaodengji()!=null &&  record.getChaobiaodengji()>0){
//				chaobiaocount+=1;
//			}
//		}
//		map.put("sumzjl",sumzjl);
//		map.put("zonglong",zonglong);
//		map.put("size",size);
//		map.put("chaobiaocount",chaobiaocount);
        return Result.OK(map);
    }

    /**
     * 按里程统计超标率查询
     *
     * @param deviceMixpileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-按里程统计超标率查询")
    @ApiOperation(value = "device_mixpile_historydata_one-按里程统计超标率查询", notes = "device_mixpile_historydata_one-按里程统计超标率查询")
    @GetMapping(value = "/stalvlist")
    public Result<?> queryPagestalvList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
//		if (deviceMixpileHistorydataOne.getShebeino() == null) {
//			if (!"null".equals(shebei)) {
//				deviceMixpileHistorydataOne.setShebeino(shebei);
//			}else {
//				deviceMixpileHistorydataOne.setShebeino("空");
//			}
//		}
        String[] split = shebei.split(",");
        String shebeis = "'" + org.apache.commons.lang.StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.select("( ifnull( SUM( CASE WHEN chaobiaodengji = 1 THEN 1 END ), 0 ) / ifnull( count(*), 0 ) )* 100 AS overlv,"
                + "ifnull( SUM( CASE WHEN chaobiaodengji = 1 THEN 1 END ), 0 ) as buhege,"
                + "ifnull( count(*), 0 ) as zong," +
                "pile_mileage,c.depart_name");
        queryWrapper.last("a JOIN shebei_info b ON a.shebeino = b.sbjno " +
                "JOIN sys_depart c ON b.sys_org_code = c.org_code " +
                "WHERE pile_mileage IS NOT NULL OR pile_mileage != '' and a.shebeino in (" + shebeis + ")" +
                "GROUP BY pile_mileage ORDER BY overlv DESC " +
                "LIMIT 7");
        List<Map<String, Object>> pageList = deviceMixpileHistorydataOneService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 按设备统计超标率查询
     *
     * @param deviceMixpileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-按设备统计超标率查询")
    @ApiOperation(value = "device_mixpile_historydata_one-按设备统计超标率查询", notes = "device_mixpile_historydata_one-按设备统计超标率查询")
    @GetMapping(value = "/stalvlists")
    public Result<?> queryPagestalvLists(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
//		if (deviceMixpileHistorydataOne.getShebeino() == null) {
//			if (!"null".equals(shebei)) {
//				deviceMixpileHistorydataOne.setShebeino(shebei);
//			}else {
//				deviceMixpileHistorydataOne.setShebeino("空");
//			}
//		}
        String[] split = shebei.split(",");
        String shebeis = "'" + org.apache.commons.lang.StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.select("( ifnull( SUM( CASE WHEN chaobiaodengji = 1 THEN 1 END ), 0 ) / ifnull( count(*), 0 ) )* 100 AS overlv,"
                + "ifnull( SUM( CASE WHEN chaobiaodengji = 1 THEN 1 END ), 0 ) as buhege,"
                + "ifnull( count(*), 0 ) as zong,a.shebeino,b.sbname");
        queryWrapper.last("a JOIN shebei_info b ON a.shebeino = b.sbjno " +
                "WHERE pile_mileage IS NOT NULL OR pile_mileage != '' and a.shebeino in (" + shebeis + ")" +
                "GROUP BY a.shebeino ORDER BY overlv DESC " +
                "LIMIT 7");
        List<Map<String, Object>> pageList = deviceMixpileHistorydataOneService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 按时间统计桩数和桩长
     *
     * @param deviceMixpileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-按时间统计桩数和桩长")
    @ApiOperation(value = "device_mixpile_historydata_one-按时间统计桩数和桩长", notes = "device_mixpile_historydata_one-按时间统计桩数和桩长")
    @GetMapping(value = "/stalists")
    public Result<?> queryPagestaLists(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req, Integer date, String pileTime_begin, String pileTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//		查询到他的设备编号
//		if (deviceMixpileHistorydataOne.getShebeino() == null) {
//			if (!"null".equals(shebei)) {
//				deviceMixpileHistorydataOne.setShebeino(shebei);
//			}else {
//				deviceMixpileHistorydataOne.setShebeino("空");
//			}
//		}
        String[] split = shebei.split(",");
        String shebeis = "'" + org.apache.commons.lang.StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        if (org.apache.commons.lang.StringUtils.isNotBlank(pileTime_begin) && org.apache.commons.lang.StringUtils.isNotBlank(pileTime_end)) {
            try {
                Date parse = ft.parse(pileTime_begin);
                Date parse1 = ft.parse(pileTime_end);
                queryWrapper.ge("pile_time", parse);
                queryWrapper.le("pile_time", parse1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            queryWrapper.eq("1", 1);
        }
        if (date == 0) {//按年
            queryWrapper.select("ROUND(ifnull( SUM(pile_realdep), 0 ),2) AS pileLength," +
                    "ifnull( count(*), 0 ) as pileNum,DATE_FORMAT(pile_time,'%Y') as pileTime");
            queryWrapper.last("and shebeino in (" + shebeis + ") and pile_time is not null GROUP BY (SELECT DATE_FORMAT(pile_time,'%Y'))");
        } else if (date == 1) {//按月
            queryWrapper.select("ROUND(ifnull( SUM(pile_realdep), 0 ),2) AS pileLength," +
                    "ifnull( count(*), 0 ) as pileNum,DATE_FORMAT(pile_time,'%Y-%m') as pileTime");
            queryWrapper.last("and shebeino in (" + shebeis + ") and pile_time like '" + format2 + "%' GROUP BY (SELECT DATE_FORMAT(pile_time,'%Y-%m'))");
        } else if (date == 2) {//按天
            queryWrapper.select("ROUND(ifnull( SUM(pile_realdep), 0 ),2) AS pileLength," +
                    "ifnull( count(*), 0 ) as pileNum,DATE_FORMAT(pile_time,'%Y-%m-%d') as pileTime");
            queryWrapper.last("and shebeino in (" + shebeis + ") GROUP BY (SELECT DATE_FORMAT(pile_time,'%Y-%m-%d')) order by pileTime desc limit 7");
        }
        List<Map<String, Object>> pageList = deviceMixpileHistorydataOneService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 添加
     *
     * @param deviceMixpileHistorydataOne
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-添加")
    @ApiOperation(value = "device_mixpile_historydata_one-添加", notes = "device_mixpile_historydata_one-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceMixpileHistorydataOne deviceMixpileHistorydataOne) {
        deviceMixpileHistorydataOneService.save(deviceMixpileHistorydataOne);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param deviceMixpileHistorydataOne
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-数据上传")
    @ApiOperation(value = "device_mixpile_historydata_one-数据上传", notes = "device_mixpile_historydata_one-数据上传")
    @PostMapping(value = "/addOther")
    public Result<?> addOther(@RequestBody DeviceMixpileHistorydataOne
                                      deviceMixpileHistorydataOne, HttpServletRequest req) {

        DeviceMixpileHistorydataOne historydataOne = new DeviceMixpileHistorydataOne();
        historydataOne.setShebeino(deviceMixpileHistorydataOne.getShebeino());
        historydataOne.setPileNo(deviceMixpileHistorydataOne.getPileNo());
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(historydataOne, req.getParameterMap());
        List<DeviceMixpileHistorydataOne> ones = deviceMixpileHistorydataOneService.list(queryWrapper);
        if (StringUtil.isNotEmpty(deviceMixpileHistorydataOne.getShebeino()) && StringUtil.isNotEmpty(deviceMixpileHistorydataOne.getPileNo())) {
            if (ones.size() > 0) {
                return Result.error("软基成桩记录上传失败，数据重复！", deviceMixpileHistorydataOne);
            } else {
                //deviceMixpileHistorydataOne.setPileTime(String.valueOf(Long.valueOf(deviceMixpileHistorydataOne.getPileTime())+28800));
                deviceMixpileHistorydataOneService.save(deviceMixpileHistorydataOne);
                return Result.OK("软基成桩记录上传成功！设备编号：" + deviceMixpileHistorydataOne.getShebeino() + ",桩号：" + deviceMixpileHistorydataOne.getPileNo());
            }
        } else {
            return Result.error("软基成桩记录上传失败！请填入设备编号和桩号");
        }


    }

    /**
     * 编辑
     *
     * @param deviceMixpileHistorydataOne
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-编辑")
    @ApiOperation(value = "device_mixpile_historydata_one-编辑", notes = "device_mixpile_historydata_one-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceMixpileHistorydataOne deviceMixpileHistorydataOne) {
        deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-通过id删除")
    @ApiOperation(value = "device_mixpile_historydata_one-通过id删除", notes = "device_mixpile_historydata_one-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceMixpileHistorydataOneService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-批量删除")
    @ApiOperation(value = "device_mixpile_historydata_one-批量删除", notes = "device_mixpile_historydata_one-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceMixpileHistorydataOneService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-通过id查询")
    @ApiOperation(value = "device_mixpile_historydata_one-通过id查询", notes = "device_mixpile_historydata_one-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceMixpileHistorydataOne deviceMixpileHistorydataOne = deviceMixpileHistorydataOneService.getById(id);
        if (deviceMixpileHistorydataOne == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceMixpileHistorydataOne);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceMixpileHistorydataOne
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileHistorydataOne
            deviceMixpileHistorydataOne) {
        return super.exportXls(request, deviceMixpileHistorydataOne, DeviceMixpileHistorydataOne.class, "水泥搅拌桩成桩记录信息");
    }
    /**
     * 导出水泥搅拌桩成桩记录信息表 excel(积木模板数据接口：sys/systems/sysMessages/mixpileexportapiXls)
     *
     * @param request
     * @param deviceMixpileHistorydataOne
     */

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DeviceMixpileHistorydataOne.class);
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcels", method = RequestMethod.POST)
    public Result<?> importExcels(HttpServletRequest request, HttpServletResponse response) {
        return importExcelsy(request, response, DeviceMixpileHistorydataOne.class);
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @param clazz
     * @return
     */
    protected Result<?> importExcelsy(HttpServletRequest request, HttpServletResponse response, Class<DeviceMixpileHistorydataOne> clazz) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<DeviceMixpileHistorydataOne> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                deviceMixpileHistorydataOneService.saveBatchs(list);
                //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
                //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                //update-end-author:taoyan date:20190528 for:批量插入数据
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 搅拌桩大屏接口--产能统计
     *
     * @param type
     * @return
     */
    @GetMapping("/outputSta")
    public Result<?> outputSta(String type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        List<String> shebeis = Arrays.asList(shebei.split(","));
//		//查询到他的设备编号
//		if (deviceMixpileHistorydataOne.getShebeino() == null) {
//			if (!"null".equals(shebei)) {
//				deviceMixpileHistorydataOne.setShebeino(shebei);
//			}else {
//				deviceMixpileHistorydataOne.setShebeino("空");
//			}
//		}

        List<Map<String, Object>> deviceMixpileHistorydataOnes;
        if (type.equals("1")) {
            deviceMixpileHistorydataOnes = deviceMixpileHistorydataOneMapper.getrichaxun(shebeis);
        } else if (type.equals("2")) {
            deviceMixpileHistorydataOnes = deviceMixpileHistorydataOneMapper.getzhouchaxun(shebeis);
        } else {
            deviceMixpileHistorydataOnes = deviceMixpileHistorydataOneMapper.getyuechaxun(shebeis);
        }
        return Result.OK(deviceMixpileHistorydataOnes);
    }


    /**
     * 搅拌桩大屏接口--今日数据
     *
     * @param deviceMixpileHistorydataOne
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-今日统计")
    @ApiOperation(value = "device_mixpile_historydata_one-今日统计", notes = "device_mixpile_historydata_one-今日统计")
    @GetMapping(value = "/queryToday")
    public Result<?> queryToday(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne, HttpServletRequest
            req, String shebeino, String date) {
//        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        //查询到他的设备编号
//        if (deviceMixpileHistorydataOne.getShebeino() == null) {
//            if (shebei != null) {
//                deviceMixpileHistorydataOne.setShebeino(shebei);
//            }
//        }
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count( pile_no ) finishcount, " +
                " IfNULL( round( SUM( pile_realdep ), 2 ), 0 ) longlen, " +
                " IfNULL( round( SUM( pile_cement ), 2 ), 0 ) pilecement, " +
                " IfNULL( round( SUM( pile_uobeton ), 2 ), 0 ) upcement, " +
                " IfNULL( round( SUM( pile_downbeton ), 2 ), 0 ) downcement," +
                "sum(case when chaobiaodengji = 1 then 1 else 0 end ) chaobiao"
        );
        queryWrapper.eq("shebeino", deviceMixpileHistorydataOne.getShebeino());
        //queryWrapper.eq("TO_DAYS( datatime )","TO_DAYS(now())");
        if (StringUtils.isEmpty(date)) {
            queryWrapper.last("and TO_DAYS( pile_time ) = TO_DAYS(now()) ");
        } else {
            queryWrapper.last("and pile_time like '" + date + "%' ");
        }

        Map<String, Object> todayInfo = deviceMixpileHistorydataOneService.getMap(queryWrapper);

        return Result.OK(todayInfo);
    }

    /**
     * 搅拌桩大屏接口--标段进度统计
     *
     * @param deviceMixpileHistorydataOne
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-搅拌桩大屏标段进度统计")
    @ApiOperation(value = "device_mixpile_historydata_one-搅拌桩大屏标段进度统计", notes = "device_mixpile_historydata_one-搅拌桩大屏标段进度统计")
    @GetMapping(value = "/progressSta")
    public Result<?> progressSta(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne, HttpServletRequest
            req, String orgs) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//
        String sysorg = loginUser.getOrgCode();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (StringUtils.isEmpty(orgs)) {
            orgs = "18";
        }
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null) {
            if (shebei != null) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            }
        }
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(" d.depart_name, " +
                " count(1) jbzcount, " +
                " IfNULL( round( SUM( m.pile_realdep ), 2 ), 0 ) longlen, " +
                " IfNULL( round( SUM( m.pile_cement ), 2 ), 0 ) pilecement, " +
                " IfNULL( round( SUM( m.pile_uobeton ), 2 ), 0 ) upcement, " +
                " IfNULL( round( SUM( m.pile_downbeton ), 2 ), 0 ) downcement"
        );
        queryWrapper.last(" m LEFT JOIN shebei_info s on m.shebeino=s.sbjno LEFT JOIN sys_depart d ON s.sys_org_code = d.org_code  WHERE d.org_code LIKE '" + sysorg + "%' GROUP BY left(d.org_code, " + orgs + ") ");
        List<Map<String, Object>> todayInfo = deviceMixpileHistorydataOneService.listMaps(queryWrapper);
        return Result.OK(todayInfo);
    }


    /**
     * 搅拌桩大屏接口--标段进度统计
     *
     * @param deviceMixpileHistorydataOne
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-搅拌桩大屏设备进行统计")
    @ApiOperation(value = "device_mixpile_historydata_one-搅拌桩大屏标段进度统计", notes = "device_mixpile_historydata_one-搅拌桩大屏按设备进行统计")
    @GetMapping(value = "/progressSta1")
    public Result<?> progressSta1(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne, HttpServletRequest
            req, String orgs) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//
        String sysorg = loginUser.getOrgCode();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null) {
            if (shebei != null) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            }
        }
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(" s.sbname AS depart_name , " +
                " count(1) jbzcount, " +
                " IfNULL( round( SUM( m.pile_realdep ), 2 ), 0 ) longlen, " +
                " IfNULL( round( SUM( m.pile_cement ), 2 ), 0 ) pilecement, " +
                " IfNULL( round( SUM( m.pile_uobeton ), 2 ), 0 ) upcement, " +
                " IfNULL( round( SUM( m.pile_downbeton ), 2 ), 0 ) downcement"
        );
        queryWrapper.last(" m INNER JOIN shebei_info s on m.shebeino=s.sbjno WHERE s.sys_org_code LIKE '" + sysorg + "%'    GROUP BY m.shebeino  ");
        List<Map<String, Object>> todayInfo = deviceMixpileHistorydataOneService.listMaps(queryWrapper);
        return Result.OK(todayInfo);
    }


    /**
     * 软基当月超标统计
     *
     * @return
     */
    @GetMapping(value = "/countList")
    public Result<?> countList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return Result.OK(deviceMixpileHistorydataOneService.countList());
    }

    @GetMapping(value = "/apiData")
    public Result<?> apiData(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                             Integer id,
                             HttpServletRequest req) {
        DeviceMixpileHistorydataOne data = new DeviceMixpileHistorydataOne();
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.eq("id", id);
        DeviceMixpileHistorydataOne one = deviceMixpileHistorydataOneService.getOne(queryWrapper);
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(one.getShebeino());
        data.setShebeino(selectshebeione.getSbname());
        data.setPileNo(one.getPileNo());
        data.setPileDesigndep(one.getPileDesigndep());
        data.setPileRatio(one.getPileRatio());
        return Result.OKs(data);
    }

    /**
     * 水泥搅拌桩导出详情接口-分段数据（暂时没有用到）
     *
     * @param deviceMixpileHistorydataOne
     * @param req
     * @return
     */
//    @GetMapping(value = "/dataSource")
//    public Result<?> dataSource(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
//                                Integer id,
//                                HttpServletRequest req) {
//        List data = new ArrayList<>();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
//        queryWrapper.eq("id", id);
//        DeviceMixpileHistorydataOne one = deviceMixpileHistorydataOneService.getOne(queryWrapper);
//        try {
//            if (!StringUtils.isEmpty(one.getPileTime())) {
//                Date end = dateFormat.parse(one.getPileTime());
//                if (!StringUtils.isEmpty(one.getPileDowntime()) && !StringUtils.isEmpty(one.getPileUptime())) {
//                    int time = Integer.parseInt(one.getPileDowntime()) + Integer.parseInt(one.getPileUptime());
//                    Long starttimes = end.getTime() - time * 1000;
//                    one.setPileStarttime(dateFormat.format(starttimes));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        QueryWrapper<DeviceMixpileHistorydataPart> queryPart1 = new QueryWrapper<>();
//        queryPart1.eq("shebeino", one.getShebeino());
//        queryPart1.eq("pile_no", one.getPileNo());
//        queryPart1.eq("part_state", 1);
//        queryPart1.orderByAsc("datatime");
//        List<DeviceMixpileHistorydataPart> dataDown = deviceMixpileHistorydataPartService.list(queryPart1);
//        QueryWrapper<DeviceMixpileHistorydataPart> queryPart2 = new QueryWrapper<>();
//        queryPart2.eq("shebeino", one.getShebeino());
//        queryPart2.eq("pile_no", one.getPileNo());
//        queryPart2.eq("part_state", 2);
//        queryPart2.orderByAsc("datatime");
//        List<DeviceMixpileHistorydataPart> dataUp = deviceMixpileHistorydataPartService.list(queryPart2);
//
//        int ids = 0;
//        String endTime = "";
//        String startTime = "";
//        for (int i = 0; i < Math.max(dataDown.size(), dataUp.size()); i++) {
//            OneVO oneVO = new OneVO();
//            ids = i + 1;
//            oneVO.setId(ids);
//            oneVO.setPileNo(one.getPileNo());
//            if (ids == 1) {
//                oneVO.setPartStartDownTime(one.getPileStarttime());
//                try {
//                    Date end = dateFormat.parse(dataUp.get(i).getPartEndtime());
//                    int time = Integer.parseInt(dataUp.get(i).getPartTime());
//                    Long starttimes = end.getTime() - time * 1000;
//                    oneVO.setPartStartUpTime(dateFormat.format(starttimes));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                if (i > (dataDown.size() - 1)) {
//                    endTime = "";
//                }
//                if (i > (dataUp.size() - 1)) {
//                    startTime = "";
//                }
//                oneVO.setPartStartDownTime(endTime);
//                oneVO.setPartStartUpTime(startTime);
//            }
//            for (int k = i; k < dataDown.size(); k++) {
//                oneVO.setPartDownTime(dataDown.get(i).getPartTime());
//                oneVO.setPartDownSpeed(dataDown.get(i).getPartSpeed());
//                oneVO.setPileDelectr(one.getPileDelectr());
//                oneVO.setPartDownBeton(dataDown.get(i).getPartBeton());
//                oneVO.setPileDpress(one.getPileDpress());
//                endTime = dataDown.get(i).getPartEndtime();
//                break;
//            }
//            for (int j = i; j < dataUp.size(); j++) {
//                oneVO.setPartUpTime(dataUp.get(i).getPartTime());
//                oneVO.setPartUpSpeed(dataUp.get(i).getPartSpeed());
//                oneVO.setPileUelectr(one.getPileDelectr());
//                oneVO.setPartUpBeton(dataUp.get(i).getPartBeton());
//                oneVO.setPileUpress(one.getPileUpress());
//                startTime = dataUp.get(i).getPartEndtime();
//                break;
//            }
//            oneVO.setPileY(one.getPileY());
//            oneVO.setPileRealdep(one.getPileRealdep());
//            oneVO.setPileCement(one.getPileCement());
//            oneVO.setPileTime(String.valueOf(Integer.parseInt(one.getPileDowntime()) + Integer.parseInt(one.getPileUptime())));
//            data.add(oneVO);
//        }
//        return Result.OKs(data);
//    }


    /**
     * 水泥搅拌桩导出详情接口-分桩数据1
     *
     * @param deviceMixpileHistorydataOne
     * @param req
     * @return
     */
    @GetMapping(value = "/dataApi1")
    public Result<?> dataApi1(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                              HttpServletRequest req) {
        List data = new ArrayList<>();
        DeviceMixpileHistorydataOne one = new DeviceMixpileHistorydataOne();
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.groupBy("pile_mileage");
        List<DeviceMixpileHistorydataOne> list = deviceMixpileHistorydataOneService.list(queryWrapper);
        String pile_mileage = "";
        for (int i = 0; i < list.size(); i++) {
            pile_mileage = pile_mileage + list.get(i).getPileMileage();
            if (i != (list.size() - 1)) {
                pile_mileage = pile_mileage + ",";
            }
        }
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(list.get(0).getShebeino());
        one.setShebeino(selectshebeione.getSbname());
        one.setPileMileage(pile_mileage);
        one.setPileDesigndep(list.get(0).getPileDesigndep());
        Double pileRatio = Double.valueOf(list.get(0).getPileRatio());
        one.setPileRatio(String.format("%.2f", pileRatio));
        data.add(one);
        return Result.OKs(data);
    }

    /**
     * 水泥搅拌桩导出详情接口-分桩数据2
     *
     * @param deviceMixpileHistorydataOne
     * @param req
     * @return
     */
    @GetMapping(value = "/dataApi2")
    public Result<?> dataApi2(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                              HttpServletRequest req) {
        List data = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormatSF = new SimpleDateFormat("HH:mm");
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.eq("gzcount", 1);
        List<DeviceMixpileHistorydataOne> list = deviceMixpileHistorydataOneService.list(queryWrapper);

        Integer id = 1;
        for (DeviceMixpileHistorydataOne one : list) {
            //如果大于一除1000
            double v = Double.parseDouble(one.getPileUobeton());
            if (v > 1) {
                one.setPileUobeton(String.valueOf(v / 1000));
            }
            OneVO oneVO = new OneVO();
            oneVO.setId(id);
            oneVO.setPileNo(one.getPileNo());//桩号
            if (one.getGzcount() == 1) {//初搅
                if (!StringUtils.isEmpty(one.getPileTime())) {
                    try {
                        Date end = dateFormat.parse(one.getPileTime());
                        if (!StringUtils.isEmpty(one.getPileDowntime()) && !StringUtils.isEmpty(one.getPileUptime())) {
                            double downtime = Double.parseDouble(one.getPileDowntime());
                            double uptime = Double.parseDouble(one.getPileUptime());

                            int time = (int) (downtime + uptime);
                            Long starttimes = end.getTime() - time * 1000;
                            Date startTime = dateFormat.parse(dateFormat.format(starttimes));
                            one.setPileStarttime(dateFormatSF.format(startTime));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                oneVO.setPartStartDownTime1(one.getPileStarttime());//初搅下钻开始时间
                oneVO.setPartDownTime1(one.getPileDowntime());//初搅下钻时长
                String pileDspeed = one.getPileDspeed();
                if (pileDspeed != null) {
                    Double pileDspeed1 = Double.valueOf(pileDspeed);
                    oneVO.setPartDownSpeed1(String.format("%.2f", pileDspeed1)); // 初搅下钻速度
                } else {
                    oneVO.setPartDownSpeed1("0.00"); // 设置默认值为0.00
                }
                String pileDelectr = one.getPileDelectr();
                if (pileDelectr != null) {
                    Double pileDelectr1 = Double.valueOf(pileDelectr);
                    oneVO.setPileDelectr1(String.format("%.2f", pileDelectr1)); // 初搅下钻电流
                } else {
                    oneVO.setPileDelectr1("0.00"); // 设置默认值为0.00
                }
                Double pileDownbeton1 = Double.valueOf(one.getPileDownbeton());
                oneVO.setPartDownBeton1(String.format("%.0f", pileDownbeton1 * 1000));//初搅下钻喷浆量
                if (one.getPileDpress() != null && !"".equals(one.getPileDpress())) {
                    oneVO.setPileDpress1(one.getPileDpress());//初搅下钻压力
                } else {
                    oneVO.setPileDpress1("0");
                }
                if (!StringUtils.isEmpty(one.getPileTime())) {
                    try {
                        Date end = dateFormat.parse(one.getPileTime());
                        if (!StringUtils.isEmpty(one.getPileUptime())) {
                            double uptime = Double.parseDouble(one.getPileUptime()); // Parses the string as a double
                            int time = (int) uptime;
                            Long startUptimes = end.getTime() - time * 1000;
                            Date startUpTime = dateFormat.parse(dateFormat.format(startUptimes));
                            one.setProblem(dateFormatSF.format(startUpTime));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                oneVO.setPartStartUpTime1(one.getProblem());//初搅提钻开始时间
                oneVO.setPartUpTime1(one.getPileUptime());//初搅提钻时长
                String pileUspeedStr = one.getPileUspeed();
                Double pileUspeed1 = 0.0;
                if (pileUspeedStr != null && !pileUspeedStr.isEmpty()) {
                    pileUspeed1 = Double.valueOf(pileUspeedStr);
                }
                oneVO.setPartUpSpeed1(String.format("%.2f", pileUspeed1)); // 初搅提钻速度

                String pileUelectrStr = one.getPileUelectr();
                Double pileUelectr1 = 0.0;
                if (pileUelectrStr != null && !pileUelectrStr.isEmpty()) {
                    pileUelectr1 = Double.valueOf(pileUelectrStr);
                }
                oneVO.setPileUelectr1(String.format("%.2f", pileUelectr1)); // 初搅提钻电流

                String pileUobetonStr = one.getPileUobeton();
                Double pileUobeton1 = 0.0;
                if (pileUobetonStr != null && !pileUobetonStr.isEmpty()) {
                    pileUobeton1 = Double.valueOf(pileUobetonStr);
                }
                oneVO.setPartUpBeton1(String.format("%.0f", pileUobeton1 * 1000)); // 初搅提钻喷浆量
                if (one.getPileUpress() != null && !"".equals(one.getPileUpress())) {
                    oneVO.setPileUpress1(one.getPileUpress());//初搅提钻压力
                } else {
                    oneVO.setPileUpress1("0");
                }

                DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 = deviceMixpileHistorydataOneService.getFJ(one, 2);
                if (ObjectUtil.isNotEmpty(deviceMixpileHistorydataOne1)) {//复搅
                    if (!StringUtils.isEmpty(deviceMixpileHistorydataOne1.getPileTime())) {
                        try {
                            Date end = dateFormat.parse(deviceMixpileHistorydataOne1.getPileTime());
                            if (!StringUtils.isEmpty(deviceMixpileHistorydataOne1.getPileDowntime()) && !StringUtils.isEmpty(deviceMixpileHistorydataOne1.getPileUptime())) {
                                double pileDowntime = Double.parseDouble(deviceMixpileHistorydataOne1.getPileDowntime());
                                double pileUptime = Double.parseDouble(deviceMixpileHistorydataOne1.getPileUptime());
                                int time = (int) (pileDowntime + pileUptime); // 将和转换为整数
                                Long starttimes = end.getTime() - time * 1000;
                                Date startTime1 = dateFormat.parse(dateFormat.format(starttimes));
                                deviceMixpileHistorydataOne1.setPileStarttime(dateFormatSF.format(startTime1));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    oneVO.setPartStartDownTime2(deviceMixpileHistorydataOne1.getPileStarttime());//复搅下钻开始时间
                    oneVO.setPartDownTime2(deviceMixpileHistorydataOne1.getPileDowntime());//复搅下钻时长
                    String pileDspeedStr = deviceMixpileHistorydataOne1.getPileDspeed();
                    Double pileDspeed2 = 0.0;
                    if (pileDspeedStr != null && !pileDspeedStr.isEmpty()) {
                        pileDspeed2 = Double.valueOf(pileDspeedStr);
                    }
                    oneVO.setPartDownSpeed2(String.format("%.2f", pileDspeed2)); //复搅下钻速度

                    String pileDelectrStr = deviceMixpileHistorydataOne1.getPileDelectr();
                    Double pileDelectr2 = 0.0;
                    if (pileDelectrStr != null && !pileDelectrStr.isEmpty()) {
                        pileDelectr2 = Double.valueOf(pileDelectrStr);
                    }
                    oneVO.setPileDelectr2(String.format("%.2f", pileDelectr2)); //复搅下钻电流

                    String pileDownbetonStr = deviceMixpileHistorydataOne1.getPileDownbeton();
                    Double pileDownbeton2 = 0.0;
                    if (pileDownbetonStr != null && !pileDownbetonStr.isEmpty()) {
                        pileDownbeton2 = Double.valueOf(pileDownbetonStr);
                    }
                    oneVO.setPartDownBeton2(String.format("%.0f", pileDownbeton2 * 1000)); //复搅下钻喷浆量
                    if (deviceMixpileHistorydataOne1.getPileDpress() != null && !"".equals(deviceMixpileHistorydataOne1.getPileDpress())) {
                        oneVO.setPileDpress2(deviceMixpileHistorydataOne1.getPileDpress());//复搅下钻压力
                    } else {
                        oneVO.setPileDpress2("0");
                    }
                    if (!StringUtils.isEmpty(deviceMixpileHistorydataOne1.getPileTime())) {
                        try {
                            Date end = dateFormat.parse(deviceMixpileHistorydataOne1.getPileTime());
                            if (!StringUtils.isEmpty(deviceMixpileHistorydataOne1.getPileUptime())) {
                                int time = (int) Double.parseDouble(deviceMixpileHistorydataOne1.getPileUptime());
                                Long startUptimes = end.getTime() - time * 1000;
                                Date startUpTime1 = dateFormat.parse(dateFormat.format(startUptimes));
                                deviceMixpileHistorydataOne1.setProblem(dateFormatSF.format(startUpTime1));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    oneVO.setPartStartUpTime2(deviceMixpileHistorydataOne1.getProblem());//复搅提钻开始时间
                    oneVO.setPartUpTime2(deviceMixpileHistorydataOne1.getPileUptime());//复搅提钻时长
                    String pileUspeed = deviceMixpileHistorydataOne1.getPileUspeed();
                    Double pileUspeed2 = 0.00;
                    if (pileUspeed != null && !pileUspeed.isEmpty()) {
                        pileUspeed2 = Double.valueOf(pileUspeed);
                    }
                    oneVO.setPartUpSpeed2(String.format("%.2f", pileUspeed2));

                    String pileUelectr = deviceMixpileHistorydataOne1.getPileUelectr();
                    Double pileUelectr2 = 0.00;
                    if (pileUelectr != null && !pileUelectr.isEmpty()) {
                        pileUelectr2 = Double.valueOf(pileUelectr);
                    }
                    oneVO.setPileUelectr2(String.format("%.2f", pileUelectr2));

                    String pileUobeton = deviceMixpileHistorydataOne1.getPileUobeton();
                    Double pileUobeton2 = 0.00;
                    if (pileUobeton != null && !pileUobeton.isEmpty()) {
                        pileUobeton2 = Double.valueOf(pileUobeton);
                    }
                    oneVO.setPartUpBeton2(String.format("%.0f", pileUobeton2 * 1000));

                    if (deviceMixpileHistorydataOne1.getPileUpress() != null && !"".equals(deviceMixpileHistorydataOne1.getPileUpress())) {
                        oneVO.setPileUpress2(deviceMixpileHistorydataOne1.getPileUpress());//复搅提钻压力
                    } else {
                        oneVO.setPileUpress2("0");
                    }
                }
                Double pileY1 = Double.valueOf(one.getPileY());
                Double pileY2 = 0.00;
                Double pileCement2 = 0.00;
                Double pileRealdep2 = 0.00;
                int pileDt = 0;
                int pileUt = 0;
                if (ObjectUtil.isNotEmpty(deviceMixpileHistorydataOne1)) {
                    pileY2 = Double.valueOf(deviceMixpileHistorydataOne1.getPileY());
                    pileCement2 = Double.valueOf(deviceMixpileHistorydataOne1.getPileCement());
                    pileRealdep2 = Double.valueOf(deviceMixpileHistorydataOne1.getPileRealdep());
                    pileDt = (int) Double.parseDouble(deviceMixpileHistorydataOne1.getPileDowntime());
                    pileUt = (int) Double.parseDouble(deviceMixpileHistorydataOne1.getPileUptime());
                }
                oneVO.setPileY(String.format("%.2f", ((pileY1 + pileY2) / 2)));//平均垂直度
                Double pileCement1 = Double.valueOf(one.getPileCement());
                oneVO.setPileCement(String.format("%.2f", (pileCement1 + pileCement2)));//实际水泥用量
                Double pileRealdep1 = Double.valueOf(one.getPileRealdep());
                oneVO.setPileRealdep(pileRealdep1 > pileRealdep2 ? String.format("%.2f", pileRealdep1) : String.format("%.2f", pileRealdep2));//钻孔长度
                String secondTime = String.valueOf((int) Double.parseDouble(one.getPileDowntime()) + (int) Double.parseDouble(one.getPileUptime()) + pileDt + pileUt);
                Double minTime = Double.parseDouble(secondTime) / 60;
                oneVO.setPileTime(String.format("%.1f", minTime));//施工总时间(单位:分钟)
                data.add(oneVO);
            }
            id++;
        }
        return Result.OKs(data);
    }

    /**
     * 水泥搅拌桩数据合并分页列表查询
     *
     * @param deviceMixpileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/listCX")
    public Result<?> listCX(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                            HttpServletRequest req) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        if (!StringUtils.isEmpty(deviceMixpileHistorydataOne.getPileMileage())) {
            deviceMixpileHistorydataOne.setPileMileage("*" + deviceMixpileHistorydataOne.getPileMileage() + "*");
        }

        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.notLike("pile_no", "-0");
//        queryWrapper.eq("gzcount", 1);
        queryWrapper.groupBy("pile_mileage,pile_no");
        Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
        List<DeviceMixpileHistorydataOne> records = pageList.getRecords();
        for (DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 : records) {
            List<DeviceMixpileHistorydataOnePage> dolist = deviceMixpileHistorydataOneService.selectJbList(deviceMixpileHistorydataOne1.getShebeino(), deviceMixpileHistorydataOne1.getPileMileage(), deviceMixpileHistorydataOne1.getPileNo());
            for (DeviceMixpileHistorydataOnePage record : dolist) {
                record.setGzcount(2);   //第一条是初搅，其余都是复搅
                if (Double.parseDouble(record.getPileDensity()) > 1000) {
                    record.setPileDensity(String.format("%.3f", Double.parseDouble(record.getPileDensity()) / 1000));//水泥浆比重
                }
                double mkg = 0.0;
                Double pileCement = Double.parseDouble(record.getPileCement());
                Double pileRatio = Double.parseDouble(record.getPileRatio()) / 100;
                Double pileDensity = Double.parseDouble(record.getPileDensity());
                Double pileRealdep = Double.parseDouble(record.getPileRealdep());
                if (pileRealdep > 0) {
//                    if (pileDensity > 3) {
//                        mkg = (pileCement * pileRatio * (pileDensity / 1000)) / pileRealdep;
//                    } else {
//                        mkg = (pileCement * pileRatio * pileDensity) / pileRealdep;
//                    }
                    if (pileDensity > 3) {
                        mkg = pileCement / (pileRealdep - 0.25 - Double.parseDouble(StringUtils.isEmpty(record.getEmptydep()) ? "0.00" : record.getEmptydep()));
                    } else {
                        mkg = pileCement / (pileRealdep - 0.25 - Double.parseDouble(StringUtils.isEmpty(record.getEmptydep()) ? "0.00" : record.getEmptydep()));
                    }
                }
//            if (!StringUtil.isEmpty(record.getPileMinelec()) && !"0".equals(record.getPileMinelec())) {
//                if (Double.parseDouble(record.getPileMinelec()) >= 55) {
//                    pjdep = Double.parseDouble(record.getPileMinelec());
//                } else if (!StringUtil.isEmpty(record.getPileCement()) && !StringUtil.isEmpty(record.getPileRealdep())) {
//                    double pileCement = Double.parseDouble(record.getPileCement());
//                    double pileRealdep = Double.parseDouble(record.getPileRealdep());
//                    if (pileRealdep != 0) {
//                        pjdep = pileCement / (pileRealdep - 0.25);
//                    }
//                }
//            } else if (!StringUtil.isEmpty(record.getPileCement()) && !StringUtil.isEmpty(record.getPileRealdep())) {
//                double pileCement = Double.parseDouble(record.getPileCement());
//                double pileRealdep = Double.parseDouble(record.getPileRealdep());
//                if (pileRealdep != 0) {
//                    pjdep = pileCement / (pileRealdep - 0.25);
//                }
//            }
                if (ObjectUtil.isEmpty(record.getPileMaxelec())) {
                    if (ObjectUtil.isNotEmpty(record.getPileMaxelectr())) {
                        record.setPileMaxelec(String.format("%.2f", Double.parseDouble(record.getPileMaxelectr())));//峰值电流
                    }
                } else {
                    record.setPileMaxelec(String.format("%.2f", Double.parseDouble(record.getPileMaxelec())));//峰值电流
                }
                record.setPileMinelec(String.format("%.3f", mkg));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (!StringUtils.isEmpty(record.getPileTime())) {
                    Date end = dateFormat.parse(record.getPileTime());
                    if (!StringUtils.isEmpty(record.getPileDowntime()) && !StringUtils.isEmpty(record.getPileUptime())) {
                        int time = Integer.parseInt(record.getPileDowntime()) + Integer.parseInt(record.getPileUptime());
                        Long starttimes = end.getTime() - time * 1000;
                        record.setPileStarttime(dateFormat.format(starttimes));
                    }
                }

            }
            DeviceMixpileHistorydataOnePage deviceMixpileHistorydataOnePage = dolist.get(0);
            deviceMixpileHistorydataOnePage.setGzcount(1);   //第一条是初搅，其余都是复搅
            deviceMixpileHistorydataOne1.setDeviceMixpileHistorydataOneList(dolist);
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/importCX")
    public Result<?> importCX(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
        }
        if (!StringUtils.isEmpty(deviceMixpileHistorydataOne.getPileMileage())) {
            deviceMixpileHistorydataOne.setPileMileage("*" + deviceMixpileHistorydataOne.getPileMileage() + "*");
        }

        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
        queryWrapper.notLike("pile_no", "-0");
        queryWrapper.eq("gzcount", 1);
        Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
        List<DeviceMixpileHistorydataOne> records = pageList.getRecords();
        for (DeviceMixpileHistorydataOne record : records) {
            List<DeviceMixpileHistorydataOnePage> dolist = deviceMixpileHistorydataOneService.selectJbList(record.getShebeino(), record.getPileMileage(), record.getPileNo());
            record.setDeviceMixpileHistorydataOneList(dolist);
            double pjdep = 0.0;
            if (!StringUtil.isEmpty(record.getPileMinelec()) && !"0".equals(record.getPileMinelec())) {
                if (Double.parseDouble(record.getPileMinelec()) >= 55) {
                    pjdep = Double.parseDouble(record.getPileMinelec());
                } else if (!StringUtil.isEmpty(record.getPileCement()) && !StringUtil.isEmpty(record.getPileRealdep())) {
                    double pileCement = Double.parseDouble(record.getPileCement());
                    double pileRealdep = Double.parseDouble(record.getPileRealdep());
                    if (pileRealdep != 0) {
                        pjdep = pileCement / (pileRealdep - 0.25 - Double.parseDouble(StringUtils.isEmpty(record.getEmptydep()) ? "0.00" : record.getEmptydep()));
                    }
                }
            } else if (!StringUtil.isEmpty(record.getPileCement()) && !StringUtil.isEmpty(record.getPileRealdep())) {
                double pileCement = Double.parseDouble(record.getPileCement());
                double pileRealdep = Double.parseDouble(record.getPileRealdep());
                if (pileRealdep != 0) {
                    pjdep = pileCement / (pileRealdep - 0.25 - Double.parseDouble(StringUtils.isEmpty(record.getEmptydep()) ? "0.00" : record.getEmptydep()));
                }
            }
            record.setPjdep(String.format("%.3f", pjdep));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!StringUtils.isEmpty(record.getPileTime())) {
                Date end = dateFormat.parse(record.getPileTime());
                if (!StringUtils.isEmpty(record.getPileDowntime()) && !StringUtils.isEmpty(record.getPileUptime())) {
                    int time = Integer.parseInt(record.getPileDowntime()) + Integer.parseInt(record.getPileUptime());
                    Long starttimes = end.getTime() - time * 1000;
                    record.setPileStarttime(dateFormat.format(starttimes));
                }
            }
        }
        return Result.OKs(pageList.getRecords());
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @SneakyThrows
    @RequestMapping(value = "/importExcel1", method = RequestMethod.POST)
    public Result<?> importExcel1(HttpServletRequest request, HttpServletResponse response) {
        if (!(request instanceof MultipartHttpServletRequest)) {
            return Result.error("请求类型错误");
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        int batchCount = 0; // 批量处理计数器
        //时间格式化
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int importedCount = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();
            if (!file.isEmpty()) {
                InputStream inputStream = file.getInputStream();
                // TODO: 在此处编写读取Excel的代码逻辑，将数据导入到 DeviceMixpileHistorydataOne 对象中

                // 示例：使用 Apache POI 进行读取
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0); // 假设导入的是第一个工作表
                Iterator<Row> rowIterator = sheet.iterator();
                // 跳过表头，这里简化为一次操作
                while (rowIterator.hasNext() && batchCount < 3) {
                    rowIterator.next();
                    batchCount++;
                }
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    short lastCellNum = row.getLastCellNum();
                    if (lastCellNum == -1) {
                        System.out.println("当前行为空行");
                        continue;
                    }
                    DeviceMixpileHistorydataOne deviceMixpileHistorydataOne = new DeviceMixpileHistorydataOne();
                    // 读取每一行的单元格数据，根据需要进行处理
                    Cell shebeino = row.getCell(0);
                    String value = getdata(shebeino);
                    if (StringUtils.isEmpty(value)) {
                        return Result.OK("已成功导入 " + importedCount + " 条数据。", importedCount);
                    }
                    Cell pile_no = row.getCell(1);
                    Cell pile_designdep = row.getCell(2);
                    Cell pile_realdep = row.getCell(3);
                    Cell pile_worktime = row.getCell(4);
                    Cell pile_downtime = row.getCell(5);
                    double pile_speed = row.getCell(6).getNumericCellValue();
                    Cell pile_delectr = row.getCell(7);
                    Cell pile_downbeton = row.getCell(8);
                    Cell pile_uptime = row.getCell(9);
                    double pile_uspeed = row.getCell(10).getNumericCellValue();
                    Cell pile_uelectr = row.getCell(11);
                    Cell pile_uobeton = row.getCell(12);
                    Cell pile_density = row.getCell(13);
                    Cell pile_cement = row.getCell(14);
                    Cell pile_maxelectr = row.getCell(15);
                    Cell pile_ratio = row.getCell(16);
                    Cell pile_x = row.getCell(17);
                    Cell pile_y = row.getCell(18);
                    Cell pile_lgd = row.getCell(19);
                    Cell pile_ltd = row.getCell(20);
                    Cell pile_dpress = row.getCell(21);
                    Cell pile_upress = row.getCell(22);
                    Cell pile_flowtotal = row.getCell(23);
                    Cell pile_mileage = row.getCell(24);
                    Date pile_starttime = null;
                    if (row.getCell(25) != null) {
                        pile_starttime = row.getCell(25).getDateCellValue();
                    }
                    Cell gzcount = row.getCell(26);
                    Cell cell27 = row.getCell(27);
                    Date pile_time = null;
                    if (cell27 != null) {
                        pile_time = row.getCell(27).getDateCellValue();
                    }

                    deviceMixpileHistorydataOne.setShebeino(getdata1(shebeino));
                    double pileNoValue = Double.parseDouble(getdata(pile_no)); // 假设返回的是 Double 类型
                    int pileNoIntValue = (int) pileNoValue;
                    deviceMixpileHistorydataOne.setPileNo(String.valueOf(pileNoIntValue));
                    deviceMixpileHistorydataOne.setPileDesigndep(getdata(pile_designdep));
                    deviceMixpileHistorydataOne.setPileRealdep(getdata(pile_realdep));
                    deviceMixpileHistorydataOne.setPileWorktime(String.valueOf(pile_worktime)); // 已在getdata中转换为字符串
                    deviceMixpileHistorydataOne.setPileDowntime(getdata(pile_downtime));
                    deviceMixpileHistorydataOne.setPileDspeed(String.valueOf(pile_speed));
                    deviceMixpileHistorydataOne.setPileDelectr(getdata(pile_delectr));
                    deviceMixpileHistorydataOne.setPileDownbeton(getdata(pile_downbeton));
                    deviceMixpileHistorydataOne.setPileUptime(getdata(pile_uptime));
                    deviceMixpileHistorydataOne.setPileUspeed(String.valueOf(pile_uspeed)); // 假定pile_uspeed为数值类型，已在getdata中转换为字符串
                    deviceMixpileHistorydataOne.setPileUelectr(getdata(pile_uelectr));
                    deviceMixpileHistorydataOne.setPileUobeton(getdata(pile_uobeton));
                    deviceMixpileHistorydataOne.setPileDensity(getdata(pile_density));
                    deviceMixpileHistorydataOne.setPileCement(getdata(pile_cement));
                    deviceMixpileHistorydataOne.setPileMaxelectr(getdata(pile_maxelectr));
                    deviceMixpileHistorydataOne.setPileRatio(getdata(pile_ratio));
                    deviceMixpileHistorydataOne.setPileX(getdata(pile_x));
                    deviceMixpileHistorydataOne.setPileY(getdata(pile_y));
                    deviceMixpileHistorydataOne.setPileLgd(getdata(pile_lgd));
                    deviceMixpileHistorydataOne.setPileLtd(getdata(pile_ltd));
                    deviceMixpileHistorydataOne.setPileDpress(getdata(pile_dpress));
                    deviceMixpileHistorydataOne.setPileUpress(getdata(pile_upress));
                    deviceMixpileHistorydataOne.setPileFlowtotal(getdata(pile_flowtotal));
                    deviceMixpileHistorydataOne.setPileMileage(getdata(pile_mileage));

                    if (pile_starttime != null) {
                        String formattedDate = dateFormat.format(pile_starttime);
                        deviceMixpileHistorydataOne.setPileStarttime(formattedDate);
                    } else {
                        deviceMixpileHistorydataOne.setPileStarttime("");
                    }
                    deviceMixpileHistorydataOne.setGzcount(Integer.valueOf(getdata(gzcount)));
                    if (pile_time != null) {
                        String formattedDate = dateFormat.format(pile_time);
                        deviceMixpileHistorydataOne.setPileTime(formattedDate);
                    } else {
                        deviceMixpileHistorydataOne.setPileTime("");
                    }

                    LambdaQueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(DeviceMixpileHistorydataOne::getPileNo,pileNoIntValue)
                                    .eq(DeviceMixpileHistorydataOne::getPileMileage,getdata(pile_mileage))
                            .eq(DeviceMixpileHistorydataOne::getGzcount,Integer.valueOf(getdata(gzcount)));
                    deviceMixpileHistorydataOneService.remove(queryWrapper);
                    deviceMixpileHistorydataOneService.save(deviceMixpileHistorydataOne);
                    importedCount++;
                }
                inputStream.close();
            }
        }
        return Result.OK("已成功导入 " + importedCount + " 条数据。", importedCount);
    }

    private String getdata(Cell cell) {

        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case STRING:
                    return cell.getStringCellValue();
                // 其他类型如BLANK, BOOLEAN, FORMULA等的处理可以根据实际情况添加
                default:
                    System.out.println("Unexpected Cell Type: " + cell.getCellType());
                    // 可能需要设定默认值或抛出异常等处理方式
                    return null;
            }
        }
        return null;
    }
    private String getdata1(Cell cell) {

        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0); // 如果需要控制小数位数，可以在这里设置
                    return df.format(cell.getNumericCellValue());
                case STRING:
                    return cell.getStringCellValue();
                // 其他类型如BLANK, BOOLEAN, FORMULA等的处理可以根据实际情况添加
                default:
                    System.out.println("Unexpected Cell Type: " + cell.getCellType());
                    // 可能需要设定默认值或抛出异常等处理方式
                    return null;
            }
        }
        return null;
    }
}
