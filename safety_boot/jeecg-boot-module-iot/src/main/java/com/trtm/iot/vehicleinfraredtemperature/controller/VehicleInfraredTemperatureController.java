package com.trtm.iot.vehicleinfraredtemperature.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.vehicleinfraredtemperature.entity.VehicleInfraredTemperature;
import com.trtm.iot.vehicleinfraredtemperature.service.IVehicleInfraredTemperatureService;

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
 * @Description: 车载红外测温
 * @Author: jeecg-boot
 * @Date: 2023-08-11
 * @Version: V1.0
 */
@Api(tags = "vehicle_infrared_temperature")
@RestController
@RequestMapping("/vehicleinfraredtemperature/vehicleInfraredTemperature")
@Slf4j
public class VehicleInfraredTemperatureController extends JeecgController<VehicleInfraredTemperature, IVehicleInfraredTemperatureService> {
    @Autowired
    private IVehicleInfraredTemperatureService vehicleInfraredTemperatureService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param vehicleInfraredTemperature
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "vehicle_infrared_temperature-分页列表查询")
    @ApiOperation(value = "vehicle_infrared_temperature-分页列表查询", notes = "vehicle_infrared_temperature-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(VehicleInfraredTemperature vehicleInfraredTemperature,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (vehicleInfraredTemperature.getDeviceId() == null) {
            if (shebei != null) {
                vehicleInfraredTemperature.setDeviceId(shebei);
            }
        }
        QueryWrapper<VehicleInfraredTemperature> queryWrapper = QueryGenerator.initQueryWrapper(vehicleInfraredTemperature, req.getParameterMap());
        Page<VehicleInfraredTemperature> page = new Page<VehicleInfraredTemperature>(pageNo, pageSize);
        IPage<VehicleInfraredTemperature> pageList = vehicleInfraredTemperatureService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param vehicleInfraredTemperature
     * @return
     */
    @AutoLog(value = "vehicle_infrared_temperature-添加")
    @ApiOperation(value = "vehicle_infrared_temperature-添加", notes = "vehicle_infrared_temperature-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody VehicleInfraredTemperature vehicleInfraredTemperature) {
        // 需要转存到z的数据
        String shebeis = "869701075886096,863482069849145";
        if(shebeis.contains(vehicleInfraredTemperature.getDeviceId())){
            ObjectMapper m = new ObjectMapper();
            String sendObject = "";
            try {
                sendObject = m.writeValueAsString(vehicleInfraredTemperature);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String post = HttpRequest.post("http://47.97.173.113:8026/jeecg-boot/vehicleinfraredtemperature/vehicleInfraredTemperature/add")
                    .header("Content-Type", "application/json")
                    .body(sendObject)
                    .execute()
                    .body();

        }else{
            vehicleInfraredTemperatureService.save(vehicleInfraredTemperature);
        }

        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param vehicleInfraredTemperature
     * @return
     */
    @AutoLog(value = "vehicle_infrared_temperature-编辑")
    @ApiOperation(value = "vehicle_infrared_temperature-编辑", notes = "vehicle_infrared_temperature-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody VehicleInfraredTemperature vehicleInfraredTemperature) {
        vehicleInfraredTemperatureService.updateById(vehicleInfraredTemperature);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "vehicle_infrared_temperature-通过id删除")
    @ApiOperation(value = "vehicle_infrared_temperature-通过id删除", notes = "vehicle_infrared_temperature-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        vehicleInfraredTemperatureService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "vehicle_infrared_temperature-批量删除")
    @ApiOperation(value = "vehicle_infrared_temperature-批量删除", notes = "vehicle_infrared_temperature-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.vehicleInfraredTemperatureService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "vehicle_infrared_temperature-通过id查询")
    @ApiOperation(value = "vehicle_infrared_temperature-通过id查询", notes = "vehicle_infrared_temperature-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        VehicleInfraredTemperature vehicleInfraredTemperature = vehicleInfraredTemperatureService.getById(id);
        if (vehicleInfraredTemperature == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(vehicleInfraredTemperature);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param vehicleInfraredTemperature
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VehicleInfraredTemperature vehicleInfraredTemperature) {
        return super.exportXls(request, vehicleInfraredTemperature, VehicleInfraredTemperature.class, "vehicle_infrared_temperature");
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
        return super.importExcel(request, response, VehicleInfraredTemperature.class);
    }

}
