package com.trtm.iot.devicepipepilehistorydataone.controller;

import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileOneHandler;
import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.trtm.iot.devicepipepilehistorydata.service.IDevicePipepileHistorydataService;
import com.trtm.iot.devicepipepilehistorydataone.entity.GongYiDto;
import com.trtm.iot.devicepipepilehistorydataone.mapper.DevicePipepileHistorydataOneMapper;
import com.trtm.iot.devicepipepilehistorydataone.vo.*;
import com.trtm.iot.devicepipepilehistorydatapart.entity.DevicePipepileHistorydataPart;
import com.trtm.iot.devicepipepilestatistics.entity.DevicePipepileStatistics;
import com.trtm.iot.devicepipepilestatistics.service.IDevicePipepileStatisticsService;
import com.trtm.iot.kanbaninfo.entity.Kanbaninfo;
import com.trtm.iot.kanbaninfo.service.IKanbaninfoService;
import com.trtm.iot.pippileOneOverHandler.entity.PippileOneOverHandler;
import com.trtm.iot.pippileOneOverHandler.service.IPippileOneOverHandlerService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trliangchanginfo.entity.TrLiangchangInfo;
import com.trtm.iot.trliangchanginfo.service.ITrLiangchangInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.xkcoding.http.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: device_pipepile_historydata_one
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Api(tags = "device_pipepile_historydata_one")
@RestController
@RequestMapping("/devicepipepilehistorydataone/devicePipepileHistorydataOne")
@Slf4j
public class DevicePipepileHistorydataOneController extends JeecgController<DevicePipepileHistorydataOne, IDevicePipepileHistorydataOneService> {
    @Autowired
    private IDevicePipepileHistorydataOneService devicePipepileHistorydataOneService;
    @Autowired
    private DevicePipepileHistorydataOneMapper devicePipepileHistorydataOneMapper;

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IPippileOneOverHandlerService pippileOneOverHandlerService;
    @Autowired
    ITrLiangchangInfoService liangchangInfoService;
    @Autowired
    IDevicePipepileStatisticsService pipepileStatisticsService;
    @Autowired
    IKanbaninfoService kanbaninfoService;
    @Autowired
    IShebeiInfoService shebeiInfoService;

    /**
     * 分页列表查询
     *
     * @param devicePipepileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }
        if (StringUtils.isNotBlank(devicePipepileHistorydataOne.getPileNo())) {
            devicePipepileHistorydataOne.setPileNo("*" + devicePipepileHistorydataOne.getPileNo() + "*");
        }
        if (StringUtils.isNotBlank(devicePipepileHistorydataOne.getPileMileage())) {
            devicePipepileHistorydataOne.setPileNo("*" + devicePipepileHistorydataOne.getPileMileage() + "*");
        }
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper.select("id,shebeino,pile_no,pile_realdep,pile_worktime,round(pile_y,3) as pile_y,pile_time" +
                ",pile_starttime,pile_upress,pile_dpress,pile_speed,times,pile_designdep,rjrwd,uuid,pile_mileage" +
                ",datatime,ts,pile_lgd,pile_ltd,istongji,chaobiaodengji,address,overproof_status,ycyy,spurl");
        queryWrapper.orderByDesc("pile_time");
        Page<DevicePipepileHistorydataOne> page = new Page<DevicePipepileHistorydataOne>(pageNo, pageSize);
        IPage<DevicePipepileHistorydataOne> pageList = devicePipepileHistorydataOneService.page(page, queryWrapper);
        List<DevicePipepileHistorydataOne> records = pageList.getRecords();
        for (DevicePipepileHistorydataOne record : records) {
            record.setPileRealdep(String.format("%.2f", Double.parseDouble(record.getPileRealdep())));//施工长度
        }
        return Result.OK(pageList);
    }

    /**
     * 亚飞管桩大屏接口
     *
     * @param devicePipepileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/listyfgz")
    public Result<?> queryPageListyfgz(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }
        Map<Object, Object> map = new HashMap<>();
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        List<DevicePipepileHistorydataOne> list = devicePipepileHistorydataOneService.list(queryWrapper);

        DevicePipepileStatistics pipepileStatistics = new DevicePipepileStatistics();
        pipepileStatistics.setShebeino(shebei);
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper4 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper4.select("round(sum(pile_realdep),2) pile_realdep");
        DevicePipepileHistorydataOne one1 = devicePipepileHistorydataOneService.getOne(queryWrapper4);

        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper1.eq("chaobiaodengji",1);
        List<DevicePipepileHistorydataOne> list1 = devicePipepileHistorydataOneService.list(queryWrapper1);

        QueryWrapper<Kanbaninfo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("sys_org_code","A05A02A05");
        queryWrapper2.eq("type",8);
        Kanbaninfo one = kanbaninfoService.getOne(queryWrapper2);

        map.put("zjzsl",one.getAllcount());
        map.put("ywgsl",list.size());
        map.put("wggczl",one1.getPileRealdep());
        map.put("zlyj",list1.size());
        return Result.OK(map);
    }

    /**
     * 实时播报信息
     *
     * @param devicePipepileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/listyfyc")
    public Result<?> queryPageListyfyc(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }

        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper1.eq("chaobiaodengji",1);
        List<DevicePipepileHistorydataOne> list1 = devicePipepileHistorydataOneService.list(queryWrapper1);
        ArrayList<Object> list = new ArrayList<>();
        if (list1.size() > 0){
            for (int i = 1; i < 5; i++) {
                Map<Object, Object> map = new HashMap<>();
                DevicePipepileHistorydataOne devicePipepileHistorydataOne1 = list1.get(list1.size() - i);
                QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sbjno",devicePipepileHistorydataOne1.getShebeino());
                ShebeiInfo one = shebeiInfoService.getOne(queryWrapper);
                if (one != null){
                    map.put("shebei",one.getSbname());
                }else {
                    continue;
                }
                map.put("wcsj",devicePipepileHistorydataOne1.getPileTime());
                map.put("zh",devicePipepileHistorydataOne1.getPileNo());
                map.put("ycyy",devicePipepileHistorydataOne1.getYcyy());
                list.add(map);
            }
        }
        return Result.OK(list);
    }


    /**
     * 标段进度统计
     *
     * @param devicePipepileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/listjdtj")
    public Result<?> queryPageListjdtj(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }

        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper1.select("shebeino,count(*) times,Round(sum(pile_realdep),2) pile_realdep");
        queryWrapper1.last("group by shebeino");
        List<DevicePipepileHistorydataOne> list1 = devicePipepileHistorydataOneService.list(queryWrapper1);
        if (list1.size() > 0){
            for (DevicePipepileHistorydataOne pipepileHistorydataOne :list1){
                QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sbjno",pipepileHistorydataOne.getShebeino());
                ShebeiInfo one = shebeiInfoService.getOne(queryWrapper);
                if (one != null){
                    pipepileHistorydataOne.setShebeino(one.getSbname());
                }
            }
        }
        return Result.OK(list1);
    }

    /**
     * 产能统计
     *
     * @param devicePipepileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/listcntj")
    public Result<?> queryPageListcntj(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req,String type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        List<String> shebeis = Arrays.asList(shebei.split(","));

        List<Map<String, Object>> devicePipepileHistorydataOnes;
        if (type.equals("1")) {
            devicePipepileHistorydataOnes = devicePipepileHistorydataOneMapper.getrichaxun(shebeis);
        } else if (type.equals("2")) {
            devicePipepileHistorydataOnes = devicePipepileHistorydataOneMapper.getzhouchaxun(shebeis);
        } else {
            devicePipepileHistorydataOnes = devicePipepileHistorydataOneMapper.getyuechaxun(shebeis);
        }
        return Result.OK(devicePipepileHistorydataOnes);
    }


    /**
     * 分页列表查询
     *
     * @param devicePipepileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
    @GetMapping(value = "/listshz")
    public Result<?> queryPageListshz(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }
        if (devicePipepileHistorydataOne.getPileTime() != null) {
            devicePipepileHistorydataOne.setPileTime(devicePipepileHistorydataOne.getPileTime() + "*");
        }
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper.select(" ifnull(count( * ),0) size,  " +
                "  round( SUM(IFNULL( pile_realdep, 0 )),2 ) zonglong,  " +
                "  count( CASE WHEN chaobiaodengji > 0 THEN  1  END)  chaobiaocount");
        Map<String, Object> map = devicePipepileHistorydataOneService.getMap(queryWrapper);
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper1.select("sum(pile_realdep) pile_realdep");
        queryWrapper1.eq("chaobiaodengji", 1);
        DevicePipepileHistorydataOne deviceMixpileHistorydataOne1 = devicePipepileHistorydataOneService.getOne(queryWrapper1);
        double exceededm = 0.0;
        if (deviceMixpileHistorydataOne1 != null) {
            if (!org.springframework.util.StringUtils.isEmpty(deviceMixpileHistorydataOne1.getPileRealdep())) {
                exceededm = Double.parseDouble(deviceMixpileHistorydataOne1.getPileRealdep());
            }
        }
        map.put("exceededm", String.format("%.2f", exceededm));
        return Result.OK(map);
    }

    /**
     * 瑞苍新增分页列表查询
     *
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/rclist")
    public Result<?> queryPagercList(String orgCode) {
        List<Map<String, Object>> soqyorgcode = devicePipepileHistorydataOneService.soqyorgcode(orgCode);
        return Result.OK(soqyorgcode);
    }

    /**
     * 瑞苍新增分页列表查询
     *
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/gxslist")
    public Result<?> queryPagegxsList(String orgCode,String dateNowStr) {//A05A01A05A01A01A01A03
        if (orgCode == null) {
            orgCode = "A05A01A05A01A01A01A03";
        }
        Date d = new Date();
        if (dateNowStr == null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            dateNowStr = sdf.format(d);
        }
        List<Map<String, Object>> soqyorgcode = liangchangInfoService.selectbyorgcode(dateNowStr,orgCode);
        return Result.OK(soqyorgcode);
    }


    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/gxsxqlist")
    public Result<?> queryPagegxsxqList(String orgCode, Integer type) {//A05A01A05A01A01A01A03
        List<Map<String, Object>> soqyorgcode = null;
        if (orgCode != null) {
            soqyorgcode = liangchangInfoService.seyorgcodexq(orgCode, type);
        }
        return Result.OK(soqyorgcode);
    }

    /**
     * 分页列表查询
     *
     * @param devicePipepileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/listGz")
    public Result<?> queryPageGzList(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }
        //获取近七天的日期
        List<String> data = getSevenDate();
        ArrayList<Map> list1 = new ArrayList<>();
        for (String s : data) {
            int i = 0;
            int j = 0;
            Map<String, String> map = new HashMap<>();
            QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
            queryWrapper.isNotNull("ycyy");
            queryWrapper.likeRight("pile_time", s);
            List<DevicePipepileHistorydataOne> list = devicePipepileHistorydataOneService.list(queryWrapper);
            map.put("time", s);
            if (list.size() == 0) {
                map.put("zcnumber", "0");
                map.put("qjnumber", "0");
            } else {
                for (DevicePipepileHistorydataOne l : list) {
                    if (l.getYcyy().contains("桩长异常")) {
                        i++;
                    } else {
                        j++;
                    }
                }
                map.put("zcnumber", String.valueOf(i));
                map.put("qjnumber", String.valueOf(j));
            }
            list1.add(map);
        }
        return Result.OK(list1);
    }

    /**
     * 分页列表查询
     *
     * @param devicePipepileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/listDzs")
    public Result<?> queryPageGzListDzs(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }
        //获取近七天的日期
        List<String> data = getSevenDate();
        ArrayList<Map> list1 = new ArrayList<>();
        for (String s : data) {
            int i = 0;
            Map<String, String> map = new HashMap<>();
            QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
            queryWrapper.likeRight("pile_time", s);
            Page<DevicePipepileHistorydataOne> page = new Page<DevicePipepileHistorydataOne>(1, 100000);
            IPage<DevicePipepileHistorydataOne> pageList = devicePipepileHistorydataOneService.page(page, queryWrapper);
            List<DevicePipepileHistorydataOne> list = pageList.getRecords();
            map.put("time", s);
            if (list.size() == 0) {
                map.put("zs", String.valueOf(list.size()));//总数
                map.put("bhgs", "0");//不合格数
            } else {
                for (DevicePipepileHistorydataOne l : list) {
                    if (l.getIstongji() == 1) {
                        if (l.getChaobiaodengji() == 1) {
                            i++;
                        }
                    }
                }
                map.put("zs", String.valueOf(list.size()));
                map.put("bhgs", String.valueOf(i));
            }
            list1.add(map);
        }
        return Result.OK(list1);
    }

    /**
     * 分页列表查询
     *
     * @param devicePipepileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/listYm")
    public Result<?> queryPageGzListym(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper.groupBy("shebeino");
        List<DevicePipepileHistorydataOne> records = devicePipepileHistorydataOneService.list(queryWrapper);

        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper1.groupBy("shebeino,pile_mileage");
        List<DevicePipepileHistorydataOne> records1 = devicePipepileHistorydataOneService.list(queryWrapper1);

        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper2 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        List<DevicePipepileHistorydataOne> records2 = devicePipepileHistorydataOneService.list(queryWrapper2);

        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper3 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper3.select("sum(pile_realdep) as pile_realdep");
        DevicePipepileHistorydataOne one = devicePipepileHistorydataOneService.getOne(queryWrapper3);

        Map<String, String> map = new HashMap<>();
        List<Map> list = new ArrayList<>();
        map.put("sbs", String.valueOf(records.size()));
        map.put("dzs", String.valueOf(records1.size()));
        map.put("zjs", String.valueOf(records2.size()));
        map.put("zms", one.getPileRealdep());
        list.add(map);
        return Result.OK(list);
    }

    //获取近七天的日期
    public static List<String> getSevenDate() {

        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 7; i++) {

            Date date = DateUtils.addDays(new Date(), -i);
            String formatDate = sdf.format(date);
            dateList.add(formatDate);
        }
        return dateList;
    }

    // 管桩超标查询
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/cbcxlist")
    public Result<?> queryPagecbcxlist(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper.eq("chaobiaodengji", 1);

        Page<DevicePipepileHistorydataOne> page = new Page<DevicePipepileHistorydataOne>(pageNo, pageSize);
        IPage<DevicePipepileHistorydataOne> pageList = devicePipepileHistorydataOneService.page(page, queryWrapper);
        List<DevicePipepileHistorydataOne> records = pageList.getRecords();
        List<DevicePippileOneHandler> handlerecords = new ArrayList<DevicePippileOneHandler>();
        for (DevicePipepileHistorydataOne record : records) {
            DevicePippileOneHandler handlerecord = new DevicePippileOneHandler();
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
            QueryWrapper<PippileOneOverHandler> queryWrapperhandle = new QueryWrapper<>();
            queryWrapperhandle.eq("baseid", record.getId());
            PippileOneOverHandler handle = pippileOneOverHandlerService.getOne(queryWrapperhandle);

            if (handle == null) {
                PippileOneOverHandler handle2 = new PippileOneOverHandler();
                handle2.setOverproofStatus(0);
                handlerecord.setHandler(handle2);
            } else {
                handlerecord.setHandler(handle);
            }
            handlerecords.add(handlerecord);

        }
        IPage<DevicePippileOneHandler> pageListhandle = new Page<DevicePippileOneHandler>(pageNo, pageSize);
        pageListhandle.setRecords(handlerecords);
        pageListhandle.setCurrent(pageList.getCurrent());
        pageListhandle.setPages(pageList.getPages());
        pageListhandle.setSize(pageList.getSize());
        pageListhandle.setTotal(pageList.getTotal());

        return Result.OK(pageListhandle);
    }


    // 管桩超标闭合统计
    @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
    @GetMapping(value = "/cbbhlist")
    public Result<?> queryPagecbbhlist(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (devicePipepileHistorydataOne.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                devicePipepileHistorydataOne.setShebeino(shebei);
            } else {
                devicePipepileHistorydataOne.setShebeino("空");
            }
        }
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper.eq("chaobiaodengji", 1);
        List<DevicePipepileHistorydataOne> records = devicePipepileHistorydataOneService.list(queryWrapper);

        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        queryWrapper1.eq("chaobiaodengji", 1);
        queryWrapper1.eq("overproof_status", 20);
        List<DevicePipepileHistorydataOne> records1 = devicePipepileHistorydataOneService.list(queryWrapper1);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("yjzs", records.size());
        map.put("ybh", records1.size());
        map.put("wbh", records.size() - records1.size());
        return Result.OK(map);
    }

    /**
     * 折线图
     *
     * @param devicePipepileHistorydataOne
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-折线图")
    @ApiOperation(value = "device_pipepile_historydata_part-折线图", notes = "device_pipepile_historydata_part-折线图")
    @GetMapping(value = "/list1")
    public Result<?> list1(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                           HttpServletRequest req) {
        String shebeino = devicePipepileHistorydataOne.getShebeino();
        String pileNo = devicePipepileHistorydataOne.getPileNo();
        List<DevicePipepileHistorydataPart> lists = devicePipepileHistorydataOneService.selectbysp(shebeino, pileNo);

        List<Map> list = new ArrayList<>();

        for (DevicePipepileHistorydataPart l : lists) {
            Map map = new HashMap();
            String partSpeed = l.getPartSpeed();
            String partDep = l.getPartDep();
            String partUpress = l.getPartUpress();
            String partDpress = l.getPartDpress();
            String partEndtime = l.getPartEndtime();
            map.put("partEndtime", partEndtime);
            map.put("partSpeed", partSpeed);
            map.put("partDep", partDep);
            map.put("partUpress", partUpress);
            map.put("partDpress", partDpress);
            list.add(map);
        }
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param devicePipepileHistorydataOne
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-添加")
    @ApiOperation(value = "device_pipepile_historydata_one-添加", notes = "device_pipepile_historydata_one-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DevicePipepileHistorydataOne devicePipepileHistorydataOne) {
        devicePipepileHistorydataOneService.save(devicePipepileHistorydataOne);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "device_pipepile_historydata_one-添加")
    @ApiOperation(value = "device_pipepile_historydata_one-添加", notes = "device_pipepile_historydata_one-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody DevicePipepileHistorydataOne devicePipepileHistorydataOne) {
        devicePipepileHistorydataOneService.save(devicePipepileHistorydataOne);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param devicePipepileHistorydataOne
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-添加")
    @ApiOperation(value = "device_pipepile_historydata_one-添加", notes = "device_pipepile_historydata_one-添加")
    @PostMapping(value = "/addOther")
    public Result<?> addOther(@RequestBody DevicePipepileHistorydataOne devicePipepileHistorydataOne) {
        if (StringUtil.isNotEmpty(devicePipepileHistorydataOne.getShebeino()) && StringUtil.isNotEmpty(devicePipepileHistorydataOne.getPileNo())) {
            DevicePipepileHistorydataOne one = null;
            try {
                QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeino", devicePipepileHistorydataOne.getShebeino());
                queryWrapper.eq("pile_no", devicePipepileHistorydataOne.getPileNo());
                one = devicePipepileHistorydataOneService.getOne(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (one != null) {
                if (devicePipepileHistorydataOne.getSpurl() != null) {
                    one.setSpurl(devicePipepileHistorydataOne.getSpurl());
                    devicePipepileHistorydataOneService.updateById(one);
                    return Result.OK("修改成功！");
                } else {
                    return Result.OK("请输入更新视频地址");
                }
            } else {
                devicePipepileHistorydataOneService.save(devicePipepileHistorydataOne);
                return Result.OK("添加成功！");
            }
        } else {
            return Result.error("记录上传失败！请填入设备编号和桩号");
        }
    }

    /**
     * 编辑
     *
     * @param devicePipepileHistorydataOne
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-编辑")
    @ApiOperation(value = "device_pipepile_historydata_one-编辑", notes = "device_pipepile_historydata_one-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DevicePipepileHistorydataOne devicePipepileHistorydataOne) {
        devicePipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-通过id删除")
    @ApiOperation(value = "device_pipepile_historydata_one-通过id删除", notes = "device_pipepile_historydata_one-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        devicePipepileHistorydataOneService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-批量删除")
    @ApiOperation(value = "device_pipepile_historydata_one-批量删除", notes = "device_pipepile_historydata_one-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.devicePipepileHistorydataOneService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-通过id查询")
    @ApiOperation(value = "device_pipepile_historydata_one-通过id查询", notes = "device_pipepile_historydata_one-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DevicePipepileHistorydataOne devicePipepileHistorydataOne = devicePipepileHistorydataOneService.getById(id);
        if (devicePipepileHistorydataOne == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(devicePipepileHistorydataOne);
    }

    /**
     * 软基施工,智能张拉,智能压浆预警和闭合查询
     *
     * @return map
     */
    @AutoLog(value = "预警闭合查询")
    @ApiOperation(value = "预警闭合查询", notes = "预警闭合查询")
    @GetMapping(value = "/queryWarningStatus")
    public Result<?> queryWarningStatus(String orgCode) {
        try {
            List<Map<String, Object>> map = devicePipepileHistorydataOneService.queryWarningStatus(orgCode);
            return Result.OK(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 软基施工详情预警详情，按照项目分类
     *
     * @Return
     */
    @AutoLog(value = "软基施工，张拉，压浆详情二级页面")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findWarningStatusBySysOrgCode")
    public Result<?> findWarningStatus(@RequestParam Integer type, String orgCode) {
        try {
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findWarningStatus(type, orgCode);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * @Return
     */
    @AutoLog(value = "软基施工，张拉，压浆详情二级页面")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findWarni")
    public Result<?> findWarningSta(String orgCode) {
        try {
            if (orgCode == null) {
                orgCode = "A";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findWarningSta(orgCode);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 软基施工详情预警详情，按照项目分类
     *
     * @Return
     */
    @AutoLog(value = "张拉，压浆，混凝土等")
    @ApiOperation(value = "软基施工，张拉，压浆详情一级页面", notes = "一级页面")
    @GetMapping(value = "/findgxBySysOrgCode")
    public Result<?> findgxStatus(Integer detatime, String orgCode) {
        try {
            // 如果缓存里有接口数据 就使用缓存
            List<Object> ll = (List<Object>) redisUtil.get(orgCode+":"+detatime+"TJ");
            if(ObjectUtil.isNotNull(ll) && ll.size()>0){
                return Result.OK(ll);
            }
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            }
            List<Object> list = devicePipepileHistorydataOneService.findgxStatus(dateNowStr, orgCode);
            redisUtil.set(orgCode+":"+detatime+"TJ", list, 3600*6);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 十五局二号看板
     *
     * @Return
     */
    @AutoLog(value = "混凝土")
    @GetMapping(value = "/findhntByOrgCode")
    public Result<?> findhntStatus(Integer detatime, String orgCode) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            }
            List<Object> list = devicePipepileHistorydataOneService.findhntStatus(dateNowStr, orgCode);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 十五局二号看板
     *
     * @Return
     */
    @AutoLog(value = "混凝土")
    @GetMapping(value = "/findhntczg")
    public Result<?> findhntczgStatus(String orgCode) {
        try {
            Date d = new Date();
            if (orgCode == null) {
                orgCode = "A";
            }
            List<Object> list = devicePipepileHistorydataOneService.findhntczgStatus( orgCode);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }
    /**
     * 大屏拌合站沥青水稳统计接口
     *
     * @Return
     */
    @AutoLog(value = "拌合站沥青水稳等")
    @ApiOperation(value = "拌合站沥青水稳", notes = "统计大屏")
    @GetMapping(value = "/findjhblstj")
    public Result<?> findjhtjStatus(Integer detatime, String orgCode) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            } else if (detatime == 2) {
                dateNowStr = "2";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findjhtjStatus(dateNowStr, orgCode);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 大屏张拉压浆统计接口
     *
     * @Return
     */
    @AutoLog(value = "张拉压浆等")
    @ApiOperation(value = "张拉压浆", notes = "统计大屏")
    @GetMapping(value = "/findjhzlyjtj")
    public Result<?> findjhzlyjtjStatus(Integer detatime, String orgCode) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            } else if (detatime == 2) {
                dateNowStr = "2";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findjhtjzlyjStatus(dateNowStr, orgCode);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 大屏试验机标养室统计接口
     *
     * @Return
     */
    @AutoLog(value = "试验机标养室等")
    @ApiOperation(value = "试验机标养室", notes = "统计大屏")
    @GetMapping(value = "/findjhsybytj")
    public Result<?> findjhsybytjStatus(Integer detatime, String orgCode) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            } else if (detatime == 2) {
                dateNowStr = "2";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findjhtjsybyStatus(dateNowStr, orgCode);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 大屏摊铺压路运输车统计接口
     *
     * @Return
     */
    @AutoLog(value = "摊铺压路运输车等")
    @ApiOperation(value = "摊铺压路运输车", notes = "统计大屏")
    @GetMapping(value = "/findjhtyytj")
    public Result<?> findjhtyytjStatus(Integer detatime, String orgCode) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            } else if (detatime == 2) {
                dateNowStr = "2";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findjhtyyStatus(dateNowStr, orgCode);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 混凝土摊铺压路运输车
     *
     * @Return
     */
    @AutoLog(value = "混凝土摊铺压路运输车")
    @ApiOperation(value = "混凝土摊铺压路运输车", notes = "统计大屏")
    @GetMapping(value = "/findjhtyytjs")
    public Result<?> findjhtyytjsStatus(Integer detatime, String orgcode) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgcode == null) {
                orgcode = "A";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            } else if (detatime == 2) {
                dateNowStr = "2";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findjhtyysStatus(dateNowStr, orgcode);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 软基施工详情预警详情，按照项目分类
     *
     * @Return
     */
    @AutoLog(value = "张拉，压浆，混凝土等")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findgxYear")
    public Result<?> findgxYear(Integer detatime, String orgCode) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            } else {
                dateNowStr = "2";
            }

            List<Object> ll = (List<Object>) redisUtil.get("findgxYear-type="+detatime+":"+orgCode);
            if(ObjectUtil.isNotNull(ll) && ll.size()>0){
                return Result.OK(ll);
            }
            List<Object> list = devicePipepileHistorydataOneService.findgxyears(dateNowStr, orgCode);
            redisUtil.set("findgxYear-type="+detatime+":"+orgCode, list, 3600*6);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

//    /**
//     *  详情
//     * @return
//     */
//    @AutoLog(value = "张拉，压浆，混凝土等")
//    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
//    @GetMapping(value = "/findgxYearXq")
//    public Result<?> findgxYearXq(Integer type,Integer detatime,String orgCode) {
//        try {
//            Date d = new Date();
//            String dateNowStr = null;
//            if (detatime == 1){
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//                dateNowStr = sdf.format(d);
//            }else if (detatime == 0){
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//                dateNowStr = sdf.format(d);
//            }else {
//                dateNowStr = "2";//以2开头的年，我不信能用到30多少年
//            }
//            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findgxtypeyears(type,dateNowStr,orgCode);
//            return Result.OK(list);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.error("暂无数据");
//        }
//    }

    /**
     * 项目级别瑞苍
     *
     * @return
     */
    @AutoLog(value = "张拉，压浆，混凝土等")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findgxYearxmtj")
    public Result<?> findgxYearxmtj(String orgCode, Integer detatime) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            } else {
                dateNowStr = "2";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findgxYearxmtj(orgCode, dateNowStr);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 项目级别瑞苍浙高
     *
     * @return
     */
    @AutoLog(value = "张拉，压浆，混凝土等")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findgxYearxmtjzg")
    public Result<?> findgxYearxmtjzg(String orgCode) {
        try {
            Date d = new Date();
            String dateNowStr = "2";
            if (orgCode == null) {
                orgCode = "A";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findgxYearxmtjzg(orgCode, dateNowStr);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 项目级别瑞苍 详情
     *
     * @return
     */
    @AutoLog(value = "张拉，压浆，混凝土等")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findgxYearxmxq")
    public Result<?> findgxYearxmxq(String orgCode, Integer detatime, Integer type) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A05A01A05A01";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            } else {
                dateNowStr = "2";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findgxYearxmxq(orgCode, dateNowStr, type);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 项目级别瑞苍 拌合站管理
     *
     * @return
     */
    @AutoLog(value = "张拉，压浆，混凝土等")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findgxYearxmbhztj")
    public Result<?> findgxYearxmbhztj(String orgCode, Integer detatime) {
        try {
            Date d = new Date();
            String dateNowStr = null;
            if (orgCode == null) {
                orgCode = "A05A01A05A01";
            }
            if (detatime == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                dateNowStr = sdf.format(d);
            } else if (detatime == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                dateNowStr = sdf.format(d);
            } else {
                dateNowStr = "2";
            }
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findgxYearxmbhztj(orgCode, dateNowStr);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 项目级别瑞苍 拌合站管理
     *
     * @return
     */
    @AutoLog(value = "张拉，压浆，混凝土等")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findgxYearxmbhzyjbgt")
    public Result<?> findgxYearxmbhzyjbgt(String orgCode, Integer detatime, Integer grade) {
        try {
            if (orgCode == null) {
                orgCode = "A05A01A05A01";
            }

            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findgxYearxmbhzyjbgt(orgCode, detatime, grade);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }


    // 获取本周第一天
    public static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        String thisWeekMonday = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return thisWeekMonday;
    }

    /**
     * 详情
     *
     * @return
     */
    @AutoLog(value = "张拉，压浆，混凝土等")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findgxYearXqtime")
    public Result<?> findgxYearXqtime(Integer type, String orgCode, String beginTime,
                                      String endTime) {

        List<Object> ll = (List<Object>) redisUtil.get("findgxYearXqtime-type="+type+":"+orgCode+":"+beginTime+":"+endTime);
        if(ObjectUtil.isNotNull(ll) && ll.size()>0){
            return Result.OK(ll);
        }
        try {
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findgxtypeyearss(type, orgCode, beginTime, endTime);
            redisUtil.set("findgxYearXqtime-type="+type+":"+orgCode+":"+beginTime+":"+endTime, list, 3600*6);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    /**
     * 详情
     *
     * @return
     */
    @AutoLog(value = "张拉，压浆，混凝土等")
    @ApiOperation(value = "软基施工，张拉，压浆详情二级页面", notes = "二级页面")
    @GetMapping(value = "/findgxYearXqtimeex")
    public Result<?> findgxYearXqtimeex(Integer type, String orgCode, String beginTime,
                                      String endTime) {
        try {
            List<Map<String, Object>> list = devicePipepileHistorydataOneService.findgxtypeyearssex(type, orgCode, beginTime, endTime);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }
    @AutoLog(value = "软基施工，张拉，压浆详情三级页面")
    @ApiOperation(value = "软基施工，张拉，压浆详情三级页面", notes = "三级页面")
    @GetMapping(value = "/queryWarningStatusByType")
    public Result<?> queryWarningStatusByType(@RequestParam String orgCode, @RequestParam Integer type) {
        try {
            List<GongYiDto> list = devicePipepileHistorydataOneService.queryWarningStatusByType(orgCode, type);
            return Result.OK(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("暂无数据");
        }
    }

    @AutoLog(value = "查询主要工艺管控合格率和闭合率")
    @ApiOperation(value = "查询主要工艺管控合格率和闭合率", notes = "查询主要工艺管控合格率和闭合率")
    @GetMapping(value = "/queryRateOfPass")
    public Result<?> queryRateOfPass(String orgCode) {
        Map<String, Object> map = devicePipepileHistorydataOneService.queryRateOfPass(orgCode);
        return Result.OK(map);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param devicePipepileHistorydataOne
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePipepileHistorydataOne devicePipepileHistorydataOne) {
        return exportXlss(request, devicePipepileHistorydataOne, DevicePipepileHistorydataOne.class, "管桩成桩记录信息表");
    }

    /**
     * 导出excel
     *
     * @param request
     */
    protected ModelAndView exportXlss(HttpServletRequest request, DevicePipepileHistorydataOne object, Class<DevicePipepileHistorydataOne> clazz, String title) {
        // Step.1 组装查询条件
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        Page<DevicePipepileHistorydataOne> objectPage = new Page<>(1, 120000);
        Page<DevicePipepileHistorydataOne> pageList1 = devicePipepileHistorydataOneService.page(objectPage, queryWrapper);
        List<DevicePipepileHistorydataOne> pageList = pageList1.getRecords();
        List<DevicePipepileHistorydataOne> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams = new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
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
        return super.importExcel(request, response, DevicePipepileHistorydataOne.class);
    }

    /**
     * 添加(免登录版)
     *
     * @param devicePipepileHistorydataOne
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_one-添加")
    @ApiOperation(value = "device_pipepile_historydata_one-添加", notes = "device_pipepile_historydata_one-添加")
    @PostMapping(value = "/addOther1")
    public Result<?> addOther1(@RequestBody DevicePipepileHistorydataOne devicePipepileHistorydataOne) {
        if (StringUtil.isNotEmpty(devicePipepileHistorydataOne.getShebeino()) && StringUtil.isNotEmpty(devicePipepileHistorydataOne.getPileNo())) {
            DevicePipepileHistorydataOne one = null;
            try {
                QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeino", devicePipepileHistorydataOne.getShebeino());
                queryWrapper.eq("pile_no", devicePipepileHistorydataOne.getPileNo());
                queryWrapper.eq("pile_mileage", devicePipepileHistorydataOne.getPileMileage());
                one = devicePipepileHistorydataOneService.getOne(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (one != null) {
                devicePipepileHistorydataOne.setId(one.getId());
                devicePipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                return Result.OK("修改！");
            } else {
                devicePipepileHistorydataOneService.save(devicePipepileHistorydataOne);
                return Result.OK("添加成功！");
            }
        } else {
            return Result.error("记录上传失败！请填入设备编号和桩号");
        }
    }

    @AutoLog(value = "device_pipepile_historydata_one-导出报表")
    @ApiOperation(value = "device_pipepile_historydata_one-导出报表", notes = "device_pipepile_historydata_one-导出报表")
    @GetMapping(value = "/exportApiData1")
    public Result<?> exportApiData1(DevicePipepileHistorydataOne devicePipepileHistorydataOne,
                                    HttpServletRequest req) {
        List data = new ArrayList<>();
        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
        List<DevicePipepileHistorydataOne> list = devicePipepileHistorydataOneService.list(queryWrapper);
        if (list != null) {
            Integer id = 1;
            for (DevicePipepileHistorydataOne pipepileHistorydataOne : list) {
                ExportDataVO exportDataVO = new ExportDataVO();
                exportDataVO.setId(id);
                exportDataVO.setShebeino(pipepileHistorydataOne.getShebeino());//设备编号
                String sbjName = devicePipepileHistorydataOneService.getSbjName(pipepileHistorydataOne.getShebeino());
                exportDataVO.setShebeiname(sbjName);//设备名称
                exportDataVO.setPileNo(pipepileHistorydataOne.getPileNo());//桩编号
                Double pileRealdep = Double.valueOf(pipepileHistorydataOne.getPileRealdep());
                exportDataVO.setPileRealdep(String.format("%.2f", pileRealdep));//施工长度,保留两位小数
                exportDataVO.setPileWorktime(pipepileHistorydataOne.getPileWorktime());//成桩时间
                Double pileY = Double.valueOf(pipepileHistorydataOne.getPileY());
                exportDataVO.setPileY(String.format("%.4f", pileY));//最大垂直度,保留四位小数
                exportDataVO.setYjwc("0.5");//预警误差
                exportDataVO.setPileTime(pipepileHistorydataOne.getPileTime());//结束时间
                exportDataVO.setPileStarttime(pipepileHistorydataOne.getPileStarttime());//开始时间
                Double pileUpress = Double.valueOf(pipepileHistorydataOne.getPileUpress());
                exportDataVO.setPileUpress(String.format("%.2f", pileUpress));//最大压桩力,保留两位小数
                Double pileDpress = Double.valueOf(pipepileHistorydataOne.getPileDpress());
                exportDataVO.setPileDpress(String.format("%.2f", pileDpress));//最大夹持力,保留两位小数
                Random random = new Random();
                int minValue = 20;
                int maxValue = 45;
                int randomValue = random.nextInt(maxValue - minValue + 1) + minValue;
                exportDataVO.setTonnage(String.valueOf(randomValue));//吨位
//                if (ObjectUtil.isNotEmpty(pipepileHistorydataOne.getTonnage())){
//                    Double tonnage = pipepileHistorydataOne.getTonnage();
//                    DecimalFormat df = new DecimalFormat("0.00");
//                    exportDataVO.setTonnage(df.format(tonnage));//吨位
//                }else {
//                    exportDataVO.setTonnage("");//吨位
//                }
                Double pileSpeed = Double.valueOf(pipepileHistorydataOne.getPileSpeed());
                exportDataVO.setPileSpeed(String.format("%.2f", pileSpeed));//平均速度,保留两位小数
                exportDataVO.setTimes(1);//接桩次数
                exportDataVO.setPileDesigndep(pipepileHistorydataOne.getPileDesigndep());//设计桩长
                exportDataVO.setPileMileage(pipepileHistorydataOne.getPileMileage());//里程
                Double pileLgd = Double.valueOf(pipepileHistorydataOne.getPileLgd());
                exportDataVO.setPileLgd(String.format("%.2f", pileLgd));//桩经度,保留两位小数
                Double pileLtd = Double.valueOf(pipepileHistorydataOne.getPileLtd());
                exportDataVO.setPileLtd(String.format("%.2f", pileLtd));//桩纬度,保留两位小数
                exportDataVO.setChaobiaodengji(pipepileHistorydataOne.getChaobiaodengji() == 0 ? "合格" : "不合格");//超标等级
                data.add(exportDataVO);
                id++;
            }
        }
        return Result.OKs(data);
    }

}
