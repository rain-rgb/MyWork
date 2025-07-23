package com.trtm.iot.clgl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.clgl.vo.ClxxRealdataPage;
import com.trtm.iot.hntbhz.vo.BhzCementBasePage;
import com.trtm.iot.monitor.entity.Monitor;
import com.trtm.iot.monitor.service.IMonitorService;
import com.trtm.iot.soslist.entity.SosList;
import com.trtm.iot.soslist.service.ISosListService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.util.GPSUtil;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import com.trtm.iot.yclcl.service.IWzyclchuchanggbService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.trtm.iot.clgl.service.IClxxRealdataService;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static com.trtm.iot.util.GPSUtil.*;
import static com.trtm.iot.util.GPSUtil.retain6;
import static org.jeecg.common.util.DateUtils.differHours;

/**
 * @Description: 车辆信息实时数据表
 * @Author: jeecg-boot
 * @Date: 2021-05-13
 * @Version: V1.0
 */
@Api(tags = "车辆信息实时数据表")
@RestController
@RequestMapping("/clgl/clxxRealdata")
@Slf4j
public class ClxxRealdataController extends JeecgController<ClxxRealdata, IClxxRealdataService> {
    @Autowired
    private IClxxRealdataService clxxRealdataService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private IWzyclchuchanggbService wzyclchuchanggbService;
    @Autowired
    private ICarmilesService carmilesService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IMonitorService monitorService;
    @Autowired
    private ISosListService sosListService;

    /**
     * 分页列表查询
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "车辆信息实时数据表-分页列表查询", notes = "车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ClxxRealdata clxxRealdata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 35);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.in("shebei_no", list);
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeiNo());
            record.setProjectId(clxxRealdataService.getDepartName(selectshebeione.getSysOrgCode()));
        }
//		for (ClxxRealdata record : records) {
//			Double latitude = record.getLatitude();
//			Double longitude = record.getLongitude();
//			double v = formatLnglat(longitude);
//			double v1 = formatLnglat(latitude);
//			double[] doubles = gps84_To_Gcj02(v1, v);
//			String result = String .format("%.6f", doubles[0]);
//			String result1 = String .format("%.6f", doubles[1]);
//			Double aDouble = Double.valueOf(result);
//			Double aDouble1 = Double.valueOf(result1);
//			record.setLatitude(aDouble);
//			record.setLongitude(aDouble1);
//			Date dattim = record.getDatatime();
//			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String format = ft.format(dattim);
//			String format1 = ft.format(new Date());
//			Integer df = differHours(format,format1);
//			if(df>2){
//				record.setStatus(2);
//			}else {
//				record.setStatus(1);
//			}
//		}
        return Result.OK(pageList);
    }

    /**
     * 15局接口 运输车，材料，视频
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "车辆信息实时数据表-分页列表查询", notes = "车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/listysc")
    public Result<?> queryPageListysc(ClxxRealdata clxxRealdata,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        clxxRealdata.setDeviceType("C");
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        List<ClxxRealdata> list = clxxRealdataService.list(queryWrapper);

        QueryWrapper<ClxxRealdata> queryWrapper1 = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper1.isNotNull("longitude");
        queryWrapper1.isNotNull("latitude");
        queryWrapper1.ne("status", 3);
        List<ClxxRealdata> list1 = clxxRealdataService.list(queryWrapper1);

        Wzycljinchanggb wzycljinchanggb = new Wzycljinchanggb();
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }

        QueryWrapper<Wzycljinchanggb> queryWrapper2 = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        queryWrapper2.select("round(sum(jingzhongT),2) jingzhongT");
        Wzycljinchanggb one = wzycljinchanggbService.getOne(queryWrapper2);

        BhzCementStatistics bhzCementStatistics = new BhzCementStatistics();
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementStatistics> queryWrapper12 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper12.select("ifnull(sum(estimate_number),0) as estimateNumber");
        queryWrapper12.isNotNull("strength_rank");
        queryWrapper12.ne("strength_rank", "");
        BhzCementStatistics one1 = bhzCementStatisticsService.getOne(queryWrapper12);

        Monitor monitor = new Monitor();
        monitor.setOrgcode(loginUser.getOrgCode() + "*");
        monitor.setIsdel(0);
        QueryWrapper<Monitor> queryWrapper4 = QueryGenerator.initQueryWrapper(monitor, req.getParameterMap());
        List<Monitor> list2 = monitorService.list(queryWrapper4);

        QueryWrapper<Monitor> queryWrapper5 = QueryGenerator.initQueryWrapper(monitor, req.getParameterMap());
        queryWrapper5.eq("workstate", 1);
        List<Monitor> list3 = monitorService.list(queryWrapper5);

        Map<String, Object> map = new HashMap<>();
        map.put("ysc", list.size());
        map.put("ysczx", list1.size());
        if (one != null) {
            map.put("clzl", Double.parseDouble(one.getJingzhongt()));
            map.put("kc", one1.getEstimateNumber());
        } else {
            map.put("clzl", 0);
            map.put("kc", 0);
        }
        map.put("sp", list2.size());
        map.put("spzx", list3.size());
        return Result.OK(map);
    }

    /**
     * 分页列表查询
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "车辆信息实时数据表-分页列表查询", notes = "车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/listView")
    public Result<?> queryPageListView(ClxxRealdata clxxRealdata,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
//        clxxRealdata.setDeviceType("C");
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.in("device_type","C","G","H");
//		queryWrapper.orderByDesc("project_id");//按照里程排名
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
            String shebeiNo = record.getShebeiNo();
            Carmiles getones = carmilesService.getones(shebeiNo);
            if (getones != null) {
                Float miles = getones.getMiles();
                record.setProjectId(String.valueOf(miles));
            } else {
                record.setProjectId("0");
            }
//			if(record.getSignaltime() != null){
//				Date date = new Date(record.getSignaltime());
//				record.setDatatime(date);
//			}
        }
        Collections.sort(pageList.getRecords(), new Comparator<ClxxRealdata>() {
            @Override
            public int compare(ClxxRealdata o1, ClxxRealdata o2) {
                return o1.getProjectId().compareTo(o2.getProjectId());
            }

        });
//		records = records.stream().sorted(Comparator.comparing((ClxxRealdata::getProjectId)).reversed()).collect(Collectors.toList());
        return Result.OK(pageList);
    }

    /**
     * 新疆自治区修改车辆状态接口
     */
    @AutoLog(value = "车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "车辆信息实时数据表-分页列表查询", notes = "车辆信息实时数据表-分页列表查询")
    @PostMapping(value = "/updatebycardnumber")
    public Result<?> queryPageListView(@RequestBody ClxxRealdata clxxRealdata) throws Exception {
        if (clxxRealdata.getCardnumber() != null) {
            QueryWrapper<ClxxRealdata> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("cardnumber", clxxRealdata.getCardnumber());
            ClxxRealdata one = clxxRealdataService.getOne(queryWrapper);

            if (one != null) {
                clxxRealdata.setId(one.getId());
                clxxRealdataService.updateById(clxxRealdata);
            } else {
                return Result.error("未找到卡号对应设备。");
            }
        } else {
            return Result.error("基站卡号为null，请确认后再上传。");
        }
        return Result.OK();
    }

    /**
     * 分页列表查询(装载机/混凝土搅拌车)
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "车辆信息实时数据表-分页列表查询", notes = "车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/zzjlist")
    public Result<?> queryPagezzjList(ClxxRealdata clxxRealdata,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        clxxRealdata.setDeviceType("E,F");
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
//			Double latitude = record.getLatitude();
//			Double longitude = record.getLongitude();
//			double v = formatLnglat(longitude);
//			double v1 = formatLnglat(latitude);
//			double[] doubles = gps84_To_Gcj02(v1, v);
//			String result = String .format("%.6f", doubles[0]);
//			String result1 = String .format("%.6f", doubles[1]);
//			Double aDouble = Double.valueOf(result);
//			Double aDouble1 = Double.valueOf(result1);
//			record.setLatitude(aDouble);
//			record.setLongitude(aDouble1);
            Date dattim = record.getDatatime();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = ft.format(dattim);
            String format1 = ft.format(new Date());
            Integer df = differHours(format, format1);
            if (df > 2) {
                record.setStatus(2);
            } else {
                record.setStatus(1);
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "车辆信息实时数据表-分页列表查询", notes = "车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(ClxxRealdata clxxRealdata,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        clxxRealdata.setDeviceType("C");
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
            Double latitude = record.getLatitude();
            Double longitude = record.getLongitude();
            double v = formatLnglat(longitude);
            double v1 = formatLnglat(latitude);
            double[] doubles = gps84_To_Gcj02(v1, v);
            String result = String.format("%.6f", doubles[0]);
            String result1 = String.format("%.6f", doubles[1]);
            Double aDouble = Double.valueOf(result);
            Double aDouble1 = Double.valueOf(result1);
            record.setLatitude(aDouble);
            record.setLongitude(aDouble1);
            Date dattim = record.getDatatime();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = ft.format(dattim);
            String format1 = ft.format(new Date());
            Integer df = differHours(format, format1);
            if (df > 2) {
                record.setStatus(2);
            } else {
                record.setStatus(1);
            }
        }
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "车辆信息实时数据表-分页列表查询", notes = "车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(ClxxRealdata clxxRealdata,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        clxxRealdata.setDeviceType("D");
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
            Double latitude = record.getLatitude();
            Double longitude = record.getLongitude();
            double v = formatLnglat(longitude);
            double v1 = formatLnglat(latitude);
            double[] doubles = gps84_To_Gcj02(v1, v);
            String result = String.format("%.6f", doubles[0]);
            String result1 = String.format("%.6f", doubles[1]);
            Double aDouble = Double.valueOf(result);
            Double aDouble1 = Double.valueOf(result1);
            record.setLatitude(aDouble);
            record.setLongitude(aDouble1);
            Date dattim = record.getDatatime();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = ft.format(dattim);
            String format1 = ft.format(new Date());
            Integer df = differHours(format, format1);
            if (df > 2) {
                record.setStatus(2);
            } else {
                record.setStatus(1);
            }
        }
        return Result.OK(pageList);
    }


    /**
     * 列表查询
     *
     * @param clxxRealdata
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-列表查询")
    @ApiOperation(value = "车辆信息实时数据表-列表查询", notes = "车辆信息实时数据表-列表查询")
    @GetMapping(value = "/querylist")
    public Result<?> queryPageList1(ClxxRealdata clxxRealdata, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 35);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.in("shebei_no", list);
        List<ClxxRealdata> records = clxxRealdataService.list(queryWrapper);
        List<ClxxRealdataPage> pageList = new ArrayList<ClxxRealdataPage>();
        for (ClxxRealdata record : records) {
            double lon = Double.parseDouble(String.valueOf(record.getLongitude()));
            double lat = Double.parseDouble(String.valueOf(record.getLatitude()));
            Integer integer = Integer.valueOf((int) lon);
            String sa = "" + integer;
            if (sa.length() > 3) {
                double lat1 = formatLnglat(lat);
                double lon1 = formatLnglat(lon);
                double[] doubles = gps84_To_Gcj02(lat1, lon1);
                double lat2 = retain6(doubles[0]);
                double lon2 = retain6(doubles[1]);
                record.setLatitude(lat2);
                record.setLongitude(lon2);
            }
            String shebeiNo = record.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            ClxxRealdataPage clxxRealdataPage = new ClxxRealdataPage();
            clxxRealdataPage.setDatatime(record.getDatatime());
            Date datatime = record.getDatatime();
            long time1 = datatime.getTime();//
            Long s = (System.currentTimeMillis() - time1) / (1000 * 60);
            if (s > 30) {
                clxxRealdataPage.setShebeistatus(0);
            } else {
                clxxRealdataPage.setShebeistatus(1);
            }
            clxxRealdataPage.setSpeed(record.getSpeed());
            clxxRealdataPage.setLatitude(record.getLatitude());
            clxxRealdataPage.setLongitude(record.getLongitude());
            clxxRealdataPage.setShebeiNo(shebeiNo);
            clxxRealdataPage.setShebeiname(selectshebeione.getSbname());
            pageList.add(clxxRealdataPage);
        }
        return Result.OK(pageList);
    }


    /**
     * 列表查询
     *
     * @param clxxRealdata
     * @param req
     * @return
     */
    @AutoLog(value = "推土机实时数据表-列表查询")
    @ApiOperation(value = "车辆信息实时数据表-列表查询", notes = "车辆信息实时数据表-列表查询")
    @GetMapping(value = "/querylist1s")
    public Result<?> queryPageList1s(ClxxRealdata clxxRealdata, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        String str = "";
        if (StringUtils.isNotBlank(clxxRealdata.getShebeiNo())) {
            str = clxxRealdata.getShebeiNo();
        } else {
            List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 71);
            if (ObjectUtil.isEmpty(list)) {
                return Result.error("暂无设备数据!");
            }
            str = list.stream().collect(Collectors.joining("','"));
        }


        QueryWrapper<ClxxRealdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(" f.dataTime, f.shebei_no,f.longitude,f.latitude,f.speed, f.ifid,h.shibiepic cardnumber,h.over_level  signaltime ");
        queryWrapper.last(" f left JOIN  hc_lijinshibie_real h ON f.shebei_no  =  h.shebeino  " +
                " where f.longitude is NOT NULL " +
                " AND f.latitude is not null" +
                " and shebei_no in ( '" + str + "')");

//        queryWrapper.isNotNull("longitude");
//        queryWrapper.isNotNull("latitude");
//        queryWrapper.in("shebei_no", list);
        List<ClxxRealdata> records = clxxRealdataService.list(queryWrapper);
        List<ClxxRealdataPage> pageList = new ArrayList<ClxxRealdataPage>();
        for (ClxxRealdata record : records) {
            double lon = Double.parseDouble(String.valueOf(record.getLongitude()));
            double lat = Double.parseDouble(String.valueOf(record.getLatitude()));
            Integer integer = Integer.valueOf((int) lon);
            String sa = "" + integer;
            if (sa.length() > 3) {
                double lat1 = formatLnglat(lat);
                double lon1 = formatLnglat(lon);
                double[] doubles = gps84_To_Gcj02(lat1, lon1);
                double lat2 = retain6(doubles[0]);
                double lon2 = retain6(doubles[1]);
                record.setLatitude(lat2);
                record.setLongitude(lon2);
            }
            String shebeiNo = record.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            ClxxRealdataPage clxxRealdataPage = new ClxxRealdataPage();
            clxxRealdataPage.setDatatime(record.getDatatime());
            Date datatime = record.getDatatime();
            long time1 = datatime.getTime();//
            Long s = (System.currentTimeMillis() - time1) / (1000 * 60);
            if (s > 30) {
                clxxRealdataPage.setShebeistatus(0);
            } else {
                clxxRealdataPage.setShebeistatus(1);
            }
            clxxRealdataPage.setSpeed(record.getSpeed());
            clxxRealdataPage.setLatitude(record.getLatitude());
            clxxRealdataPage.setLongitude(record.getLongitude());
            clxxRealdataPage.setCardnumber(record.getCardnumber());
            clxxRealdataPage.setSignaltime(record.getSignaltime());
            clxxRealdataPage.setShebeiNo(shebeiNo);
            clxxRealdataPage.setShebeiname(selectshebeione.getSbname());
            pageList.add(clxxRealdataPage);
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询 洒水车
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "洒水车车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "洒水车车辆信息实时数据表-分页列表查询", notes = "洒水车车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/listSSC")
    public Result<?> queryPageListSSC(ClxxRealdata clxxRealdata,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 70);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.eq("device_type", "C");
        queryWrapper.in("shebei_no", list);
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeiNo());
            record.setProjectId(clxxRealdataService.getDepartName(selectshebeione.getSysOrgCode()));
        }
        return Result.OK(pageList);
    }

    /**
     * 列表查询
     *
     * @param clxxRealdata
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-列表查询")
    @ApiOperation(value = "车辆信息实时数据表-列表查询", notes = "车辆信息实时数据表-列表查询")
    @GetMapping(value = "/queryListSSC")
    public Result<?> queryListSSC(ClxxRealdata clxxRealdata, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 70);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.eq("device_type", "C");
        queryWrapper.in("shebei_no", list);
        List<ClxxRealdata> records = clxxRealdataService.list(queryWrapper);
        List<ClxxRealdataPage> pageList = new ArrayList<ClxxRealdataPage>();
        for (ClxxRealdata record : records) {
            double lon = Double.parseDouble(String.valueOf(record.getLongitude()));
            double lat = Double.parseDouble(String.valueOf(record.getLatitude()));
            Integer integer = Integer.valueOf((int) lon);
            String sa = "" + integer;
            if (sa.length() > 3) {
                double lat1 = formatLnglat(lat);
                double lon1 = formatLnglat(lon);
                double[] doubles = gps84_To_Gcj02(lat1, lon1);
                double lat2 = retain6(doubles[0]);
                double lon2 = retain6(doubles[1]);
                record.setLatitude(lat2);
                record.setLongitude(lon2);
            }
            String shebeiNo = record.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            ClxxRealdataPage clxxRealdataPage = new ClxxRealdataPage();
            clxxRealdataPage.setDatatime(record.getDatatime());
            Date datatime = record.getDatatime();
            long time1 = datatime.getTime();//
            Long s = (System.currentTimeMillis() - time1) / (1000 * 60);
            if (s > 30) {
                clxxRealdataPage.setShebeistatus(0);
            } else {
                clxxRealdataPage.setShebeistatus(1);
            }
            clxxRealdataPage.setSpeed(record.getSpeed());
            clxxRealdataPage.setLatitude(record.getLatitude());
            clxxRealdataPage.setLongitude(record.getLongitude());
            clxxRealdataPage.setShebeiNo(shebeiNo);
            clxxRealdataPage.setShebeiname(selectshebeione.getSbname());
            pageList.add(clxxRealdataPage);
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询 推土机
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "推土机车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "推土机车辆信息实时数据表-分页列表查询", notes = "推土机车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/listTTJ")
    public Result<?> queryPageListTTJ(ClxxRealdata clxxRealdata,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 71);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.in("shebei_no", list);
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeiNo());
            record.setProjectId(clxxRealdataService.getDepartName(selectshebeione.getSysOrgCode()));
        }
        return Result.OK(pageList);
    }

    /**
     * 列表查询
     *
     * @param clxxRealdata
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-列表查询")
    @ApiOperation(value = "车辆信息实时数据表-列表查询", notes = "车辆信息实时数据表-列表查询")
    @GetMapping(value = "/queryListTTJ")
    public Result<?> queryListTTJ(ClxxRealdata clxxRealdata, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 71);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.in("shebei_no", list);
        List<ClxxRealdata> records = clxxRealdataService.list(queryWrapper);
        List<ClxxRealdataPage> pageList = new ArrayList<ClxxRealdataPage>();
        for (ClxxRealdata record : records) {
            double lon = Double.parseDouble(String.valueOf(record.getLongitude()));
            double lat = Double.parseDouble(String.valueOf(record.getLatitude()));
            Integer integer = Integer.valueOf((int) lon);
            String sa = "" + integer;
            if (sa.length() > 3) {
                double lat1 = formatLnglat(lat);
                double lon1 = formatLnglat(lon);
                double[] doubles = gps84_To_Gcj02(lat1, lon1);
                double lat2 = retain6(doubles[0]);
                double lon2 = retain6(doubles[1]);
                record.setLatitude(lat2);
                record.setLongitude(lon2);
            }
            String shebeiNo = record.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            ClxxRealdataPage clxxRealdataPage = new ClxxRealdataPage();
            clxxRealdataPage.setDatatime(record.getDatatime());
            Date datatime = record.getDatatime();
            long time1 = datatime.getTime();//
            Long s = (System.currentTimeMillis() - time1) / (1000 * 60);
            if (s > 30) {
                clxxRealdataPage.setShebeistatus(0);
            } else {
                clxxRealdataPage.setShebeistatus(1);
            }
            clxxRealdataPage.setSpeed(record.getSpeed());
            clxxRealdataPage.setLatitude(record.getLatitude());
            clxxRealdataPage.setLongitude(record.getLongitude());
            clxxRealdataPage.setShebeiNo(shebeiNo);
            clxxRealdataPage.setShebeiname(selectshebeione.getSbname());
            pageList.add(clxxRealdataPage);
        }
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询 挖掘机
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "挖掘机车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "挖掘机车辆信息实时数据表-分页列表查询", notes = "挖掘机车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/listWJJ")
    public Result<?> queryPageListWJJ(ClxxRealdata clxxRealdata,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 72);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.in("shebei_no", list);
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeiNo());
            record.setProjectId(clxxRealdataService.getDepartName(selectshebeione.getSysOrgCode()));
        }
        return Result.OK(pageList);
    }

    /**
     * 列表查询
     *
     * @param clxxRealdata
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-列表查询")
    @ApiOperation(value = "车辆信息实时数据表-列表查询", notes = "车辆信息实时数据表-列表查询")
    @GetMapping(value = "/queryListWJJ")
    public Result<?> queryListWJJ(ClxxRealdata clxxRealdata, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 72);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.in("shebei_no", list);
        List<ClxxRealdata> records = clxxRealdataService.list(queryWrapper);
        List<ClxxRealdataPage> pageList = new ArrayList<ClxxRealdataPage>();
        for (ClxxRealdata record : records) {
            double lon = Double.parseDouble(String.valueOf(record.getLongitude()));
            double lat = Double.parseDouble(String.valueOf(record.getLatitude()));
            Integer integer = Integer.valueOf((int) lon);
            String sa = "" + integer;
            if (sa.length() > 3) {
                double lat1 = formatLnglat(lat);
                double lon1 = formatLnglat(lon);
                double[] doubles = gps84_To_Gcj02(lat1, lon1);
                double lat2 = retain6(doubles[0]);
                double lon2 = retain6(doubles[1]);
                record.setLatitude(lat2);
                record.setLongitude(lon2);
            }
            String shebeiNo = record.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            ClxxRealdataPage clxxRealdataPage = new ClxxRealdataPage();
            clxxRealdataPage.setDatatime(record.getDatatime());
            Date datatime = record.getDatatime();
            long time1 = datatime.getTime();//
            Long s = (System.currentTimeMillis() - time1) / (1000 * 60);
            if (s > 30) {
                clxxRealdataPage.setShebeistatus(0);
            } else {
                clxxRealdataPage.setShebeistatus(1);
            }
            clxxRealdataPage.setSpeed(record.getSpeed());
            clxxRealdataPage.setLatitude(record.getLatitude());
            clxxRealdataPage.setLongitude(record.getLongitude());
            clxxRealdataPage.setShebeiNo(shebeiNo);
            clxxRealdataPage.setShebeiname(selectshebeione.getSbname());
            pageList.add(clxxRealdataPage);
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询 清扫车
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "清扫车车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "清扫车车辆信息实时数据表-分页列表查询", notes = "清扫车车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/listQSC")
    public Result<?> queryPageListQSC(ClxxRealdata clxxRealdata,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 73);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.in("shebei_no", list);
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeiNo());
            record.setProjectId(clxxRealdataService.getDepartName(selectshebeione.getSysOrgCode()));
        }
        return Result.OK(pageList);

    }

    /**
     * 列表查询
     *
     * @param clxxRealdata
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-列表查询")
    @ApiOperation(value = "车辆信息实时数据表-列表查询", notes = "车辆信息实时数据表-列表查询")
    @GetMapping(value = "/queryListQSC")
    public Result<?> queryListQSC(ClxxRealdata clxxRealdata, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 73);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.in("shebei_no", list);
        List<ClxxRealdata> records = clxxRealdataService.list(queryWrapper);
        List<ClxxRealdataPage> pageList = new ArrayList<ClxxRealdataPage>();
        for (ClxxRealdata record : records) {
            double lon = Double.parseDouble(String.valueOf(record.getLongitude()));
            double lat = Double.parseDouble(String.valueOf(record.getLatitude()));
            Integer integer = Integer.valueOf((int) lon);
            String sa = "" + integer;
            if (sa.length() > 3) {
                double lat1 = formatLnglat(lat);
                double lon1 = formatLnglat(lon);
                double[] doubles = gps84_To_Gcj02(lat1, lon1);
                double lat2 = retain6(doubles[0]);
                double lon2 = retain6(doubles[1]);
                record.setLatitude(lat2);
                record.setLongitude(lon2);
            }
            String shebeiNo = record.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            ClxxRealdataPage clxxRealdataPage = new ClxxRealdataPage();
            clxxRealdataPage.setDatatime(record.getDatatime());
            Date datatime = record.getDatatime();
            long time1 = datatime.getTime();//
            Long s = (System.currentTimeMillis() - time1) / (1000 * 60);
            if (s > 30) {
                clxxRealdataPage.setShebeistatus(0);
            } else {
                clxxRealdataPage.setShebeistatus(1);
            }
            clxxRealdataPage.setSpeed(record.getSpeed());
            clxxRealdataPage.setLatitude(record.getLatitude());
            clxxRealdataPage.setLongitude(record.getLongitude());
            clxxRealdataPage.setShebeiNo(shebeiNo);
            clxxRealdataPage.setShebeiname(selectshebeione.getSbname());
            pageList.add(clxxRealdataPage);
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询 雾炮车
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "雾炮车车辆信息实时数据表-分页列表查询")
    @ApiOperation(value = "雾炮车车辆信息实时数据表-分页列表查询", notes = "雾炮车车辆信息实时数据表-分页列表查询")
    @GetMapping(value = "/listWPC")
    public Result<?> queryPageListWPC(ClxxRealdata clxxRealdata,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 74);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.eq("device_type", "C");
        queryWrapper.in("shebei_no", list);
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        for (ClxxRealdata record : records) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeiNo());
            record.setProjectId(clxxRealdataService.getDepartName(selectshebeione.getSysOrgCode()));
        }
        return Result.OK(pageList);

    }

    /**
     * 列表查询
     *
     * @param clxxRealdata
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-列表查询")
    @ApiOperation(value = "车辆信息实时数据表-列表查询", notes = "车辆信息实时数据表-列表查询")
    @GetMapping(value = "/queryListWPC")
    public Result<?> queryListWPC(ClxxRealdata clxxRealdata, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 74);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.eq("device_type", "C");
        queryWrapper.in("shebei_no", list);
        List<ClxxRealdata> records = clxxRealdataService.list(queryWrapper);
        List<ClxxRealdataPage> pageList = new ArrayList<ClxxRealdataPage>();
        for (ClxxRealdata record : records) {
            double lon = Double.parseDouble(String.valueOf(record.getLongitude()));
            double lat = Double.parseDouble(String.valueOf(record.getLatitude()));
            Integer integer = Integer.valueOf((int) lon);
            String sa = "" + integer;
            if (sa.length() > 3) {
                double lat1 = formatLnglat(lat);
                double lon1 = formatLnglat(lon);
                double[] doubles = gps84_To_Gcj02(lat1, lon1);
                double lat2 = retain6(doubles[0]);
                double lon2 = retain6(doubles[1]);
                record.setLatitude(lat2);
                record.setLongitude(lon2);
            }
            String shebeiNo = record.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            ClxxRealdataPage clxxRealdataPage = new ClxxRealdataPage();
            clxxRealdataPage.setDatatime(record.getDatatime());
            Date datatime = record.getDatatime();
            long time1 = datatime.getTime();//
            Long s = (System.currentTimeMillis() - time1) / (1000 * 60);
            if (s > 30) {
                clxxRealdataPage.setShebeistatus(0);
            } else {
                clxxRealdataPage.setShebeistatus(1);
            }
            clxxRealdataPage.setSpeed(record.getSpeed());
            clxxRealdataPage.setLatitude(record.getLatitude());
            clxxRealdataPage.setLongitude(record.getLongitude());
            clxxRealdataPage.setShebeiNo(shebeiNo);
            clxxRealdataPage.setShebeiname(selectshebeione.getSbname());
            pageList.add(clxxRealdataPage);
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询 人员定位
     *
     * @param clxxRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "人员定位信息实时数据表-分页列表查询")
    @ApiOperation(value = "人员定位信息实时数据表-分页列表查询", notes = "人员定位信息实时数据表-分页列表查询")
    @GetMapping(value = "/listRYDW")
    public Result<?> queryPagelistRYDW(ClxxRealdata clxxRealdata,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> strings = new ArrayList<>();
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 40);
        List<String> list1 = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 84);
        strings.addAll(list);
        strings.addAll(list1);
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.eq("device_type", "Y");
        queryWrapper.in("shebei_no", strings);
        Page<ClxxRealdata> page = new Page<ClxxRealdata>(pageNo, pageSize);
        IPage<ClxxRealdata> pageList = clxxRealdataService.page(page, queryWrapper);
        List<ClxxRealdata> records = pageList.getRecords();
        if (ObjectUtil.isNotEmpty(records)) {
            for (ClxxRealdata record : records) {
                Date date = new Date();
                if (record.getStatus() != 0) {
                    Date datatime = record.getDatatime();
                    if (datatime != null) {
                        long timeDifferenceMillis = date.getTime() - datatime.getTime();  // 计算时间差（毫秒）
                        // 判断时间差是否小于等于 30 分钟（10 * 60 * 1000 毫秒）
                        if (Math.abs(timeDifferenceMillis) >= 30 * 60 * 1000) {
                            clxxRealdataService.updateStatusById(record.getId());
                        }
                    }
                }
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 列表查询
     *
     * @param clxxRealdata
     * @param req
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-列表查询")
    @ApiOperation(value = "车辆信息实时数据表-列表查询", notes = "车辆信息实时数据表-列表查询")
    @GetMapping(value = "/queryListRYDW")
    public Result<?> queryListRYDW(ClxxRealdata clxxRealdata, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (clxxRealdata.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                clxxRealdata.setShebeiNo(shebei);
            } else {
                clxxRealdata.setShebeiNo("空");
            }
        }
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 40);
        if (ObjectUtil.isEmpty(list)) {
            return Result.error("暂无设备数据!");
        }
        List<String> strings = new ArrayList<>();
        List<String> listSb1 = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 40);
        List<String> listSb2 = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 84);
        strings.addAll(listSb1);
        strings.addAll(listSb2);
        QueryWrapper<ClxxRealdata> queryWrapper = QueryGenerator.initQueryWrapper(clxxRealdata, req.getParameterMap());
        queryWrapper.isNotNull("longitude");
        queryWrapper.isNotNull("latitude");
        queryWrapper.eq("device_type", "Y");
        queryWrapper.in("shebei_no", strings);
        List<ClxxRealdata> records = clxxRealdataService.list(queryWrapper);
        List<ClxxRealdataPage> pageList = new ArrayList<ClxxRealdataPage>();
        for (ClxxRealdata record : records) {
            double lon = Double.parseDouble(String.valueOf(record.getLongitude()));
            double lat = Double.parseDouble(String.valueOf(record.getLatitude()));
            Integer integer = Integer.valueOf((int) lon);
            String sa = "" + integer;
            if (sa.length() > 3) {
                double lat1 = formatLnglat(lat);
                double lon1 = formatLnglat(lon);
                double[] doubles = gps84_To_Gcj02(lat1, lon1);
                double lat2 = retain6(doubles[0]);
                double lon2 = retain6(doubles[1]);
                record.setLatitude(lat2);
                record.setLongitude(lon2);
            }
            String shebeiNo = record.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            ClxxRealdataPage clxxRealdataPage = new ClxxRealdataPage();
            clxxRealdataPage.setDatatime(record.getDatatime());
            Date datatime = record.getDatatime();
            Date date = new Date();
            long timeDifferenceMillis = date.getTime() - datatime.getTime();  // 计算时间差（毫秒）
            // 判断时间差是否小于等于 30 分钟（10 * 60 * 1000 毫秒）
            if (Math.abs(timeDifferenceMillis) >= 30 * 60 * 1000) {
                clxxRealdataPage.setShebeistatus(0);
            } else {
                clxxRealdataPage.setShebeistatus(1);
            }
            clxxRealdataPage.setSpeed(record.getSpeed());
            clxxRealdataPage.setLatitude(record.getLatitude());
            clxxRealdataPage.setLongitude(record.getLongitude());
            clxxRealdataPage.setShebeiNo(shebeiNo);
            clxxRealdataPage.setShebeiname(selectshebeione.getSbname());
            pageList.add(clxxRealdataPage);
        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param clxxRealdata
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-添加")
    @ApiOperation(value = "车辆信息实时数据表-添加", notes = "车辆信息实时数据表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ClxxRealdata clxxRealdata) {
        clxxRealdataService.save(clxxRealdata);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param clxxRealdata
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-编辑")
    @ApiOperation(value = "车辆信息实时数据表-编辑", notes = "车辆信息实时数据表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ClxxRealdata clxxRealdata) {
        clxxRealdataService.updateById(clxxRealdata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-通过id删除")
    @ApiOperation(value = "车辆信息实时数据表-通过id删除", notes = "车辆信息实时数据表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        clxxRealdataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-批量删除")
    @ApiOperation(value = "车辆信息实时数据表-批量删除", notes = "车辆信息实时数据表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.clxxRealdataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "车辆信息实时数据表-通过id查询")
    @ApiOperation(value = "车辆信息实时数据表-通过id查询", notes = "车辆信息实时数据表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ClxxRealdata clxxRealdata = clxxRealdataService.getById(id);
        if (clxxRealdata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(clxxRealdata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param clxxRealdata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ClxxRealdata clxxRealdata) {
        return super.exportXls(request, clxxRealdata, ClxxRealdata.class, "车辆信息实时数据表");
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
        return super.importExcel(request, response, ClxxRealdata.class);
    }

}
