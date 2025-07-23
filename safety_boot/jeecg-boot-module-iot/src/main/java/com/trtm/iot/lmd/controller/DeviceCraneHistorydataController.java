package com.trtm.iot.lmd.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

import com.trtm.api.utils.crypt.MD5;
import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicecranerealdata.service.IDeviceCraneRealdataService;
import com.trtm.iot.system.entity.ShebeiInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;

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
import reactor.core.publisher.Mono;

/**
 * @Description: 龙门吊表信息
 * @Author: jeecg-boot
 * @Date: 2021-07-28
 * @Version: V1.0
 */
@Api(tags = "龙门吊表信息")
@RestController
@RequestMapping("/lmd/deviceCraneHistorydata")
@Slf4j
public class DeviceCraneHistorydataController extends JeecgController<DeviceCraneHistorydata, IDeviceCraneHistorydataService> {
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;
    @Autowired
    private IDeviceCraneRealdataService deviceCraneRealdataService;
    @Autowired
    private RedisUtil redisUtil;

    @AutoLog(value = "龙门吊表信息-分页列表查询")
    @ApiOperation(value = "龙门吊表信息-分页列表查询", notes = "龙门吊表信息-分页列表查询")
    @GetMapping(value = "/list0")
    public Result<?> queryPageList0(DeviceCraneHistorydata deviceCraneHistorydata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (deviceCraneHistorydata.getDeviceCode() == null) {
            if (!"null".equals(shebei)) {
                deviceCraneHistorydata.setDeviceCode(shebei);
            } else {
                deviceCraneHistorydata.setDeviceCode("空");
            }
        }
        QueryWrapper<DeviceCraneHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneHistorydata, req.getParameterMap());
        Page<DeviceCraneHistorydata> page = new Page<DeviceCraneHistorydata>(pageNo, pageSize);
        IPage<DeviceCraneHistorydata> pageList = deviceCraneHistorydataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param deviceCraneHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "龙门吊表信息-分页列表查询")
    @ApiOperation(value = "龙门吊表信息-分页列表查询", notes = "龙门吊表信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceCraneHistorydata deviceCraneHistorydata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (deviceCraneHistorydata.getDeviceCode() == null) {
            if (!"null".equals(shebei)) {
                deviceCraneHistorydata.setDeviceCode(shebei);
            } else {
                deviceCraneHistorydata.setDeviceCode("空");
            }
        }
        deviceCraneHistorydata.setDeviceType(2);
        QueryWrapper<DeviceCraneHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneHistorydata, req.getParameterMap());
        Page<DeviceCraneHistorydata> page = new Page<DeviceCraneHistorydata>(pageNo, pageSize);
        IPage<DeviceCraneHistorydata> pageList = deviceCraneHistorydataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param deviceCraneHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "架桥机表信息-分页列表查询")
    @ApiOperation(value = "架桥机表信息-分页列表查询", notes = "架桥机表信息-分页列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(DeviceCraneHistorydata deviceCraneHistorydata,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (deviceCraneHistorydata.getDeviceCode() == null) {
            if (!"null".equals(shebei)) {
                deviceCraneHistorydata.setDeviceCode(shebei);
            } else {
                deviceCraneHistorydata.setDeviceCode("空");
            }
        }
        deviceCraneHistorydata.setDeviceType(3);
        QueryWrapper<DeviceCraneHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneHistorydata, req.getParameterMap());
        Page<DeviceCraneHistorydata> page = new Page<DeviceCraneHistorydata>(pageNo, pageSize);
        IPage<DeviceCraneHistorydata> pageList = deviceCraneHistorydataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param deviceCraneHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "提梁机表信息-分页列表查询")
    @ApiOperation(value = "提梁机表信息-分页列表查询", notes = "提梁机表信息-分页列表查询")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(DeviceCraneHistorydata deviceCraneHistorydata,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (deviceCraneHistorydata.getDeviceCode() == null) {
            if (!"null".equals(shebei)) {
                deviceCraneHistorydata.setDeviceCode(shebei);
            } else {
                deviceCraneHistorydata.setDeviceCode("空");
            }
        }
        deviceCraneHistorydata.setDeviceType(4);
        QueryWrapper<DeviceCraneHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneHistorydata, req.getParameterMap());
        Page<DeviceCraneHistorydata> page = new Page<DeviceCraneHistorydata>(pageNo, pageSize);
        IPage<DeviceCraneHistorydata> pageList = deviceCraneHistorydataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param deviceCraneHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "汽车吊表信息-分页列表查询")
    @ApiOperation(value = "汽车吊表信息-分页列表查询", notes = "汽车吊表信息-分页列表查询")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(DeviceCraneHistorydata deviceCraneHistorydata,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (deviceCraneHistorydata.getDeviceCode() == null) {
            if (!"null".equals(shebei)) {
                deviceCraneHistorydata.setDeviceCode(shebei);
            } else {
                deviceCraneHistorydata.setDeviceCode("空");
            }
        }
        deviceCraneHistorydata.setDeviceType(5);
        QueryWrapper<DeviceCraneHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneHistorydata, req.getParameterMap());
        Page<DeviceCraneHistorydata> page = new Page<DeviceCraneHistorydata>(pageNo, pageSize);
        IPage<DeviceCraneHistorydata> pageList = deviceCraneHistorydataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 预警统计
     *
     * @param deviceCraneHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "龙门吊表信息-预警统计")
    @ApiOperation(value = "龙门吊表信息-预警统计", notes = "龙门吊表信息-预警统计")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(DeviceCraneHistorydata deviceCraneHistorydata,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (deviceCraneHistorydata.getDeviceCode() == null) {
            if (!"null".equals(shebei)) {
                deviceCraneHistorydata.setDeviceCode(shebei);
            } else {
                deviceCraneHistorydata.setDeviceCode("空");
            }
        }
        QueryWrapper<DeviceCraneHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneHistorydata, req.getParameterMap());
        queryWrapper.select("count(status) as wind_speedalarm");
        queryWrapper.eq("status", 2);
        DeviceCraneHistorydata one = deviceCraneHistorydataService.getOne(queryWrapper);
        Map map = new HashMap();
        map.put("alarmnum", one.getWindSpeedalarm());
        return Result.OK(map);
    }

    /**
     * 添加
     *
     * @param deviceCraneHistorydata
     * @return
     */
    @AutoLog(value = "龙门吊表信息-添加")
    @ApiOperation(value = "龙门吊表信息-添加", notes = "龙门吊表信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceCraneHistorydata deviceCraneHistorydata) {
        deviceCraneHistorydataService.save(deviceCraneHistorydata);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceCraneHistorydata
     * @return
     */
    @AutoLog(value = "龙门吊表信息-编辑")
    @ApiOperation(value = "龙门吊表信息-编辑", notes = "龙门吊表信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceCraneHistorydata deviceCraneHistorydata) {
        deviceCraneHistorydataService.updateById(deviceCraneHistorydata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "龙门吊表信息-通过id删除")
    @ApiOperation(value = "龙门吊表信息-通过id删除", notes = "龙门吊表信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceCraneHistorydataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "龙门吊表信息-批量删除")
    @ApiOperation(value = "龙门吊表信息-批量删除", notes = "龙门吊表信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceCraneHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "龙门吊表信息-通过id查询")
    @ApiOperation(value = "龙门吊表信息-通过id查询", notes = "龙门吊表信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceCraneHistorydata deviceCraneHistorydata = deviceCraneHistorydataService.getById(id);
        if (deviceCraneHistorydata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceCraneHistorydata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceCraneHistorydata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceCraneHistorydata deviceCraneHistorydata) {
        return super.exportXls(request, deviceCraneHistorydata, DeviceCraneHistorydata.class, "龙门吊表信息");
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
        return super.importExcel(request, response, DeviceCraneHistorydata.class);
    }

    /***/
    @AutoLog(value = "龙门吊表信息-通过orgCode查询龙门吊设备信息，获取该设备上传时间")
    @ApiOperation(value = "龙门吊表信息-通过id查询", notes = "龙门吊表信息-通过id查询")
    @GetMapping(value = "/getLmdUploadTime")
    public Result<?> getLmdUploadTime(String orgCode) {
        List<ShebeiInfo> sbList = deviceCraneHistorydataService.getShebei(21, orgCode);
        Integer num = 0;
        for (ShebeiInfo shebeiInfo : sbList) {
            Date createTime = shebeiInfo.getCreateTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(createTime);
            String[] split = format.split("-");
            LocalDate start = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])); // 设定开始日期
            LocalDate end = LocalDate.now(); // 获取当前日期
            long days = ChronoUnit.DAYS.between(start, end); // 计算两个日期之间相差的天数
            Integer day = (int) days;
            num += day;
        }
        return Result.OK(num);
    }

    /**
     * 龙门吊新增历史数据时，根据设备编号判断实时表是否有该设备数据，如果有，就更新为该设备的最新数据，如果没有，就新增该设备的数据
     *
     * @param deviceCraneHistorydata
     * @return
     */
    @AutoLog(value = "device_crane_historydata-添加")
    @ApiOperation(value = "device_crane_historydata-添加", notes = "device_crane_historydata-添加")
    @PostMapping(value = "/insertLMDSyncData")
    public Result<?> insertLMDSyncData(@RequestBody DeviceCraneHistorydata deviceCraneHistorydata) {
        deviceCraneHistorydata.setId(null);
        deviceCraneHistorydataService.save(deviceCraneHistorydata);
        QueryWrapper<DeviceCraneRealdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_code", deviceCraneHistorydata.getDeviceCode());
        DeviceCraneRealdata one = deviceCraneRealdataService.getOne(queryWrapper);

        DeviceCraneRealdata deviceCraneRealdata = new DeviceCraneRealdata();
        BeanUtils.copyProperties(deviceCraneHistorydata, deviceCraneRealdata);
        if (ObjectUtil.isNotEmpty(one)) {//one不为空，更新数据
            deviceCraneRealdata.setId(one.getId());
            deviceCraneRealdataService.updateById(deviceCraneRealdata);
            return Result.OK("历史数据添加成功，实时数据更新成功！");
        } else {//one为空，新增数据
            deviceCraneRealdataService.save(deviceCraneRealdata);
            return Result.OK("历史数据添加成功，实时数据添加成功！");
        }
    }

}
