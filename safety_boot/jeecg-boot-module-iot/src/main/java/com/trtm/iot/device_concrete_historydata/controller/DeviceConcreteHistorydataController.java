package com.trtm.iot.device_concrete_historydata.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.device_concrete_historydata.entity.DeviceConcreteHistorydata;
import com.trtm.iot.device_concrete_historydata.service.IDeviceConcreteHistorydataService;

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
 * @Description: 混凝土成熟度监测历史表
 * @Author: jeecg-boot
 * @Date: 2024-10-22
 * @Version: V1.0
 */
@Api(tags = "混凝土成熟度监测历史表")
@RestController
@RequestMapping("/device_concrete_historydata/deviceConcreteHistorydata")
@Slf4j
public class DeviceConcreteHistorydataController extends JeecgController<DeviceConcreteHistorydata, IDeviceConcreteHistorydataService> {
    @Autowired
    private IDeviceConcreteHistorydataService deviceConcreteHistorydataService;

    /**
     * 分页列表查询
     *
     * @param deviceConcreteHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土成熟度监测历史表-分页列表查询")
    @ApiOperation(value = "混凝土成熟度监测历史表-分页列表查询", notes = "混凝土成熟度监测历史表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceConcreteHistorydata deviceConcreteHistorydata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<DeviceConcreteHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceConcreteHistorydata, req.getParameterMap());
        Page<DeviceConcreteHistorydata> page = new Page<DeviceConcreteHistorydata>(pageNo, pageSize);
        IPage<DeviceConcreteHistorydata> pageList = deviceConcreteHistorydataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param deviceConcreteHistorydata
     * @return
     */
    @AutoLog(value = "混凝土成熟度监测历史表-添加")
    @ApiOperation(value = "混凝土成熟度监测历史表-添加", notes = "混凝土成熟度监测历史表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceConcreteHistorydata deviceConcreteHistorydata) {
        deviceConcreteHistorydataService.save(deviceConcreteHistorydata);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceConcreteHistorydata
     * @return
     */
    @AutoLog(value = "混凝土成熟度监测历史表-编辑")
    @ApiOperation(value = "混凝土成熟度监测历史表-编辑", notes = "混凝土成熟度监测历史表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceConcreteHistorydata deviceConcreteHistorydata) {
        deviceConcreteHistorydataService.updateById(deviceConcreteHistorydata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "混凝土成熟度监测历史表-通过id删除")
    @ApiOperation(value = "混凝土成熟度监测历史表-通过id删除", notes = "混凝土成熟度监测历史表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceConcreteHistorydataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "混凝土成熟度监测历史表-批量删除")
    @ApiOperation(value = "混凝土成熟度监测历史表-批量删除", notes = "混凝土成熟度监测历史表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceConcreteHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "混凝土成熟度监测历史表-通过id查询")
    @ApiOperation(value = "混凝土成熟度监测历史表-通过id查询", notes = "混凝土成熟度监测历史表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceConcreteHistorydata deviceConcreteHistorydata = deviceConcreteHistorydataService.getById(id);
        if (deviceConcreteHistorydata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceConcreteHistorydata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceConcreteHistorydata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceConcreteHistorydata deviceConcreteHistorydata) {
        return super.exportXls(request, deviceConcreteHistorydata, DeviceConcreteHistorydata.class, "混凝土成熟度监测历史表");
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
        return super.importExcel(request, response, DeviceConcreteHistorydata.class);
    }

    /**
     * 通过设备查询,用于绘制曲线
     *
     * @param sbbh
     * @return
     */
    @AutoLog(value = "混凝土成熟度监测历史表-通过设备查询")
    @ApiOperation(value = "混凝土成熟度监测历史表-通过设备查询", notes = "混凝土成熟度监测历史表-通过设备查询")
    @GetMapping(value = "/queryBySbbh")
    public Result<?> queryBySbbh(@RequestParam(name = "sbbh", required = true) String sbbh) {
        List<DeviceConcreteHistorydata> deviceConcreteHistorydataList = deviceConcreteHistorydataService.getBySbbh(sbbh);
        JSONObject jsonObject = new JSONObject();
        List time = new ArrayList();
        List maturityList = new ArrayList();
        List maturityoutsideList = new ArrayList();
        List strengthList = new ArrayList();
        List strengthoutsideList = new ArrayList();
        List temperatureList = new ArrayList();
        List temperatureoutsideList = new ArrayList();

        for (DeviceConcreteHistorydata deviceConcreteHistorydata : deviceConcreteHistorydataList) {
            time.add(deviceConcreteHistorydata.getUpdatetime());
            maturityList.add(deviceConcreteHistorydata.getMaturity());
            maturityoutsideList.add(deviceConcreteHistorydata.getMaturityoutside());
            strengthList.add(deviceConcreteHistorydata.getStrength());
            strengthoutsideList.add(deviceConcreteHistorydata.getStrengthoutside());
            temperatureList.add(deviceConcreteHistorydata.getTemperature());
            temperatureoutsideList.add(deviceConcreteHistorydata.getTemperatureoutside());

        }
        jsonObject.put("time",time);
        jsonObject.put("maturity",maturityList);
        jsonObject.put("maturityoutside",maturityoutsideList);
        jsonObject.put("strength",strengthList);
        jsonObject.put("strengthoutside",strengthoutsideList);
        jsonObject.put("temperature",temperatureList);
        jsonObject.put("temperatureoutside",temperatureoutsideList);
        return Result.OK(jsonObject);
    }
}
