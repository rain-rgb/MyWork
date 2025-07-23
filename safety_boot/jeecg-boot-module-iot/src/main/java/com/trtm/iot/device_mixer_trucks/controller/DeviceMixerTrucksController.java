package com.trtm.iot.device_mixer_trucks.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.clgl.service.IClxxRealdataService;
import com.trtm.iot.device_mixer_trucks.entity.DeviceMixerTrucksvo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.device_mixer_trucks.entity.DeviceMixerTrucks;
import com.trtm.iot.device_mixer_trucks.service.IDeviceMixerTrucksService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 搅拌车监控
 * @Author: jeecg-boot
 * @Date:   2025-05-06
 * @Version: V1.0
 */
@Api(tags="搅拌车监控")
@RestController
@RequestMapping("/device_mixer_trucks/deviceMixerTrucks")
@Slf4j
public class DeviceMixerTrucksController extends JeecgController<DeviceMixerTrucks, IDeviceMixerTrucksService> {
    @Autowired
    private IDeviceMixerTrucksService deviceMixerTrucksService;
    @Autowired
    private IClxxRealdataService clxxRealdataService;

    /**
     * 分页列表查询
     *
     * @param deviceMixerTrucks
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "搅拌车监控-分页列表查询")
    @ApiOperation(value="搅拌车监控-分页列表查询", notes="搅拌车监控-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceMixerTrucks deviceMixerTrucks,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<DeviceMixerTrucks> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixerTrucks, req.getParameterMap());
        Page<DeviceMixerTrucks> page = new Page<DeviceMixerTrucks>(pageNo, pageSize);
        IPage<DeviceMixerTrucks> pageList = deviceMixerTrucksService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param deviceMixerTrucks
     * @return
     */
    @AutoLog(value = "搅拌车监控-添加")
    @ApiOperation(value="搅拌车监控-添加", notes="搅拌车监控-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceMixerTrucks deviceMixerTrucks) {
        deviceMixerTrucksService.save(deviceMixerTrucks);
        return Result.OK("添加成功！");
    }

    /**
     *   添加
     *
     * @param deviceMixerTrucks
     * @return
     */
    @AutoLog(value = "搅拌车监控-添加")
    @ApiOperation(value="搅拌车监控-添加", notes="搅拌车监控-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody DeviceMixerTrucks deviceMixerTrucks) {
        deviceMixerTrucksService.save(deviceMixerTrucks);
        clxxRealdataService.saveClxxRealdata(deviceMixerTrucks);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param deviceMixerTrucks
     * @return
     */
    @AutoLog(value = "搅拌车监控-编辑")
    @ApiOperation(value="搅拌车监控-编辑", notes="搅拌车监控-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceMixerTrucks deviceMixerTrucks) {
        deviceMixerTrucksService.updateById(deviceMixerTrucks);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "搅拌车监控-通过id删除")
    @ApiOperation(value="搅拌车监控-通过id删除", notes="搅拌车监控-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        deviceMixerTrucksService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "搅拌车监控-批量删除")
    @ApiOperation(value="搅拌车监控-批量删除", notes="搅拌车监控-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.deviceMixerTrucksService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "搅拌车监控-通过id查询")
    @ApiOperation(value="搅拌车监控-通过id查询", notes="搅拌车监控-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        DeviceMixerTrucks deviceMixerTrucks = deviceMixerTrucksService.getById(id);
        if(deviceMixerTrucks==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceMixerTrucks);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceMixerTrucks
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixerTrucks deviceMixerTrucks) {
        return super.exportXls(request, deviceMixerTrucks, DeviceMixerTrucks.class, "搅拌车监控");
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
        return super.importExcel(request, response, DeviceMixerTrucks.class);
    }
}
