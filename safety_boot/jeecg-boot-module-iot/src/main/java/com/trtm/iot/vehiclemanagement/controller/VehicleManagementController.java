package com.trtm.iot.vehiclemanagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.system.entity.ShebeiInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.vehiclemanagement.entity.VehicleManagement;
import com.trtm.iot.vehiclemanagement.service.IVehicleManagementService;

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
 * @Description: 车牌号管理
 * @Author: jeecg-boot
 * @Date: 2022-02-21
 * @Version: V1.0
 */
@Api(tags = "车牌号管理")
@RestController
@RequestMapping("/vehiclemanagement/vehicleManagement")
@Slf4j
public class VehicleManagementController extends JeecgController<VehicleManagement, IVehicleManagementService> {
    @Autowired
    private IVehicleManagementService vehicleManagementService;

    /**
     * 分页列表查询
     *
     * @param vehicleManagement
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "车牌号管理-分页列表查询")
    @ApiOperation(value = "车牌号管理-分页列表查询", notes = "车牌号管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(VehicleManagement vehicleManagement,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode == null) {
            vehicleManagement.setSysOrgCode(loginUser.getOrgCode() + "*");
        } else {
            vehicleManagement.setSysOrgCode(sys_depart_orgcode + "*");
        }
        if (vehicleManagement.getNumberPlate() != null) {
            vehicleManagement.setNumberPlate("*" + vehicleManagement.getNumberPlate() + "*");
        }
        if (vehicleManagement.getDriverName() != null) {
            vehicleManagement.setDriverName("*" + vehicleManagement.getDriverName() + "*");
        }
        QueryWrapper<VehicleManagement> queryWrapper = QueryGenerator.initQueryWrapper(vehicleManagement, req.getParameterMap());
        Page<VehicleManagement> page = new Page<VehicleManagement>(pageNo, pageSize);
        IPage<VehicleManagement> pageList = vehicleManagementService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param vehicleManagement
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "车牌号管理-分页列表查询")
    @ApiOperation(value = "车牌号管理-分页列表查询", notes = "车牌号管理-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(VehicleManagement vehicleManagement,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        vehicleManagement.setSysOrgCode(loginUser.getOrgCode() + "*");
        String numberPlate = vehicleManagement.getNumberPlate();
        if(numberPlate!=null){
            vehicleManagement.setNumberPlate("*"+numberPlate+"*");
        }
        QueryWrapper<VehicleManagement> queryWrapper = QueryGenerator.initQueryWrapper(vehicleManagement, req.getParameterMap());
        List<VehicleManagement> list = vehicleManagementService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param vehicleManagement
     * @return
     */
    @AutoLog(value = "车牌号管理-添加")
    @ApiOperation(value = "车牌号管理-添加", notes = "车牌号管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody VehicleManagement vehicleManagement) {
        vehicleManagementService.save(vehicleManagement);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param vehicleManagement
     * @return
     */
    @AutoLog(value = "车牌号管理-编辑")
    @ApiOperation(value = "车牌号管理-编辑", notes = "车牌号管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody VehicleManagement vehicleManagement) {
        vehicleManagementService.updateById(vehicleManagement);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "车牌号管理-通过id删除")
    @ApiOperation(value = "车牌号管理-通过id删除", notes = "车牌号管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        vehicleManagementService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "车牌号管理-批量删除")
    @ApiOperation(value = "车牌号管理-批量删除", notes = "车牌号管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.vehicleManagementService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "车牌号管理-通过id查询")
    @ApiOperation(value = "车牌号管理-通过id查询", notes = "车牌号管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        VehicleManagement vehicleManagement = vehicleManagementService.getById(id);
        if (vehicleManagement == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(vehicleManagement);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param vehicleManagement
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VehicleManagement vehicleManagement) {
        return super.exportXls(request, vehicleManagement, VehicleManagement.class, "车牌号管理");
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
        return super.importExcel(request, response, VehicleManagement.class);
    }

}
