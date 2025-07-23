package com.trtm.iot.jiaqiaoji.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeRealdata;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeHistorydata;
import com.trtm.iot.jiaqiaoji.service.IDeviceBridgeHistorydataService;

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

/**
 * @Description: device_bridge_historydata
 * @Author: jeecg-boot
 * @Date: 2023-03-13
 * @Version: V1.0
 */
@Api(tags = "device_bridge_historydata")
@RestController
@RequestMapping("/jiaqiaoji/deviceBridgeHistorydata")
@Slf4j
public class DeviceBridgeHistorydataController extends JeecgController<DeviceBridgeHistorydata, IDeviceBridgeHistorydataService> {
    @Autowired
    private IDeviceBridgeHistorydataService deviceBridgeHistorydataService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param deviceBridgeHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_bridge_historydata-分页列表查询")
    @ApiOperation(value = "device_bridge_historydata-分页列表查询", notes = "device_bridge_historydata-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceBridgeHistorydata deviceBridgeHistorydata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (deviceBridgeHistorydata.getDeviceCode() == null) {
            if ("null".equals(shebei)) {
                shebei = "空";
            }
            deviceBridgeHistorydata.setDeviceCode(shebei);
        }
        QueryWrapper<DeviceBridgeHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceBridgeHistorydata, req.getParameterMap());
        Page<DeviceBridgeHistorydata> page = new Page<DeviceBridgeHistorydata>(pageNo, pageSize);
        IPage<DeviceBridgeHistorydata> pageList = deviceBridgeHistorydataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "device_bridge_realdata-通过id查询")
    @ApiOperation(value = "device_bridge_realdata-通过id查询", notes = "device_bridge_realdata-通过id查询")
    @GetMapping(value = "/queryByDeviceCode")
    public Result<?> queryByDeviceCode(DeviceBridgeHistorydata deviceBridgeHistorydata,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "30") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (deviceBridgeHistorydata.getDeviceCode() == null) {
            if ("null".equals(shebei)) {
                shebei = "空";
            }
            deviceBridgeHistorydata.setDeviceCode(shebei);
        }
        QueryWrapper<DeviceBridgeHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceBridgeHistorydata, req.getParameterMap());
        Page<DeviceBridgeHistorydata> page = new Page<DeviceBridgeHistorydata>(pageNo, pageSize);
        IPage<DeviceBridgeHistorydata> pageList = deviceBridgeHistorydataService.page(page, queryWrapper);

        for( DeviceBridgeHistorydata one : pageList.getRecords()){
          //  if( one.getLongitudinalCarroute1()>=20 ){
                double tanValue1 = Math.abs(one.getCraneHeight1()-one.getCraneHeight2());
                double tanValue2 = Math.abs(one.getLongitudinalCarroute1()-one.getLongitudinalCarroute2());
                double tanValue = tanValue1/tanValue2;
                double atanValue = Math.atan(tanValue)*180/Math.PI;
                atanValue = one.getCraneHeight1()-one.getCraneHeight2()>0?atanValue:-atanValue;
                one.setJiaodu(atanValue);
           // }

        }

        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param deviceBridgeHistorydata
     * @return
     */
    @AutoLog(value = "device_bridge_historydata-添加")
    @ApiOperation(value = "device_bridge_historydata-添加", notes = "device_bridge_historydata-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceBridgeHistorydata deviceBridgeHistorydata) {
        deviceBridgeHistorydataService.save(deviceBridgeHistorydata);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceBridgeHistorydata
     * @return
     */
    @AutoLog(value = "device_bridge_historydata-编辑")
    @ApiOperation(value = "device_bridge_historydata-编辑", notes = "device_bridge_historydata-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceBridgeHistorydata deviceBridgeHistorydata) {
        deviceBridgeHistorydataService.updateById(deviceBridgeHistorydata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_bridge_historydata-通过id删除")
    @ApiOperation(value = "device_bridge_historydata-通过id删除", notes = "device_bridge_historydata-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceBridgeHistorydataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "device_bridge_historydata-批量删除")
    @ApiOperation(value = "device_bridge_historydata-批量删除", notes = "device_bridge_historydata-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceBridgeHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_bridge_historydata-通过id查询")
    @ApiOperation(value = "device_bridge_historydata-通过id查询", notes = "device_bridge_historydata-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceBridgeHistorydata deviceBridgeHistorydata = deviceBridgeHistorydataService.getById(id);
        if (deviceBridgeHistorydata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceBridgeHistorydata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceBridgeHistorydata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceBridgeHistorydata deviceBridgeHistorydata) {
        return super.exportXls(request, deviceBridgeHistorydata, DeviceBridgeHistorydata.class, "device_bridge_historydata");
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
        return super.importExcel(request, response, DeviceBridgeHistorydata.class);
    }

}
