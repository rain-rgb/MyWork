package com.trtm.iot.tadiao.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.tadiao.entity.DeviceTowerRealdata;
import com.trtm.iot.tadiao.service.IDeviceTowerRealdataService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.tadiao.entity.DeviceTowerHistorydata;
import com.trtm.iot.tadiao.service.IDeviceTowerHistorydataService;

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
 * @Description: device_tower_historydata
 * @Author: jeecg-boot
 * @Date: 2023-02-27
 * @Version: V1.0
 */
@Api(tags = "device_tower_historydata")
@RestController
@RequestMapping("/tadiao/deviceTowerHistorydata")
@Slf4j
public class DeviceTowerHistorydataController extends JeecgController<DeviceTowerHistorydata, IDeviceTowerHistorydataService> {
    @Autowired
    private IDeviceTowerHistorydataService deviceTowerHistorydataService;
    @Autowired
    private IDeviceTowerRealdataService deviceTowerRealdataService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param deviceTowerHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_tower_historydata-分页列表查询")
    @ApiOperation(value = "device_tower_historydata-分页列表查询", notes = "device_tower_historydata-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceTowerHistorydata deviceTowerHistorydata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (deviceTowerHistorydata.getDeviceCode() == null) {
            if ("null".equals(shebei)) {
                deviceTowerHistorydata.setDeviceCode("空");
            }
            deviceTowerHistorydata.setDeviceCode(shebei);
        }
        QueryWrapper<DeviceTowerHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTowerHistorydata, req.getParameterMap());
        Page<DeviceTowerHistorydata> page = new Page<DeviceTowerHistorydata>(pageNo, pageSize);
        IPage<DeviceTowerHistorydata> pageList = deviceTowerHistorydataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param deviceTowerHistorydata
     * @return
     */
    @AutoLog(value = "device_tower_historydata-添加")
    @ApiOperation(value = "device_tower_historydata-添加", notes = "device_tower_historydata-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceTowerHistorydata deviceTowerHistorydata) {
        deviceTowerHistorydataService.save(deviceTowerHistorydata);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceTowerHistorydata
     * @return
     */
    @AutoLog(value = "device_tower_historydata-编辑")
    @ApiOperation(value = "device_tower_historydata-编辑", notes = "device_tower_historydata-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceTowerHistorydata deviceTowerHistorydata) {
        deviceTowerHistorydataService.updateById(deviceTowerHistorydata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_tower_historydata-通过id删除")
    @ApiOperation(value = "device_tower_historydata-通过id删除", notes = "device_tower_historydata-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceTowerHistorydataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "device_tower_historydata-批量删除")
    @ApiOperation(value = "device_tower_historydata-批量删除", notes = "device_tower_historydata-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceTowerHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_tower_historydata-通过id查询")
    @ApiOperation(value = "device_tower_historydata-通过id查询", notes = "device_tower_historydata-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceTowerHistorydata deviceTowerHistorydata = deviceTowerHistorydataService.getById(id);
        if (deviceTowerHistorydata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceTowerHistorydata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceTowerHistorydata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceTowerHistorydata deviceTowerHistorydata) {
        return super.exportXls(request, deviceTowerHistorydata, DeviceTowerHistorydata.class, "device_tower_historydata");
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
        return super.importExcel(request, response, DeviceTowerHistorydata.class);
    }

    /**
     * 塔吊新增历史数据时，根据设备编号判断实时表是否有该设备数据，如果有，就更新为该设备的最新数据，如果没有，就新增该设备的数据
     *
     * @param deviceTowerHistorydata
     * @return
     */
    @AutoLog(value = "device_tower_historydata-添加")
    @ApiOperation(value = "device_tower_historydata-添加", notes = "device_tower_historydata-添加")
    @PostMapping(value = "/insertSyncData")
    public Result<?> insertSyncData(@RequestBody DeviceTowerHistorydata deviceTowerHistorydata) {
        deviceTowerHistorydata.setId(null);
        deviceTowerHistorydataService.save(deviceTowerHistorydata);
        QueryWrapper<DeviceTowerRealdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_code", deviceTowerHistorydata.getDeviceCode());
        DeviceTowerRealdata one = deviceTowerRealdataService.getOne(queryWrapper);

        DeviceTowerRealdata deviceTowerRealdata = new DeviceTowerRealdata();
        BeanUtils.copyProperties(deviceTowerHistorydata, deviceTowerRealdata);
        if (ObjectUtil.isNotEmpty(one)) {//one不为空，更新数据
            deviceTowerRealdata.setId(one.getId());
            deviceTowerRealdataService.updateById(deviceTowerRealdata);
            return Result.OK("历史数据添加成功，实时数据更新成功！");
        } else {//one为空，新增数据
            deviceTowerRealdataService.save(deviceTowerRealdata);
            return Result.OK("历史数据添加成功，实时数据添加成功！");
        }
    }

}
