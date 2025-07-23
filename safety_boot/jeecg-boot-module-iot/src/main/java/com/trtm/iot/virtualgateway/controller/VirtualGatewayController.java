package com.trtm.iot.virtualgateway.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.virtualgateway.entity.VirtualGateway;
import com.trtm.iot.virtualgateway.service.IVirtualGatewayService;

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
 * @Description: virtual_gateway
 * @Author: jeecg-boot
 * @Date: 2023-05-24
 * @Version: V1.0
 */
@Api(tags = "virtual_gateway")
@RestController
@RequestMapping("/virtualgateway/virtualGateway")
@Slf4j
public class VirtualGatewayController extends JeecgController<VirtualGateway, IVirtualGatewayService> {
    @Autowired
    private IVirtualGatewayService virtualGatewayService;

    /**
     * 分页列表查询
     *
     * @param virtualGateway
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "virtual_gateway-分页列表查询")
    @ApiOperation(value = "virtual_gateway-分页列表查询", notes = "virtual_gateway-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(VirtualGateway virtualGateway,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<VirtualGateway> queryWrapper = QueryGenerator.initQueryWrapper(virtualGateway, req.getParameterMap());
        Page<VirtualGateway> page = new Page<VirtualGateway>(pageNo, pageSize);
        IPage<VirtualGateway> pageList = virtualGatewayService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param virtualGateway
     * @return
     */
    @AutoLog(value = "virtual_gateway-添加")
    @ApiOperation(value = "virtual_gateway-添加", notes = "virtual_gateway-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody VirtualGateway virtualGateway) {
        virtualGateway.setK(("true".equals(virtualGateway.getK()) || "TRUE".equals(virtualGateway.getK())) ? "1" : "0");
        virtualGateway.setSos(("true".equals(virtualGateway.getSos()) || "TRUE".equals(virtualGateway.getSos())) ? "1" : "0");
        virtualGatewayService.save(virtualGateway);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param virtualGateway
     * @return
     */
    @AutoLog(value = "virtual_gateway-编辑")
    @ApiOperation(value = "virtual_gateway-编辑", notes = "virtual_gateway-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody VirtualGateway virtualGateway) {
        virtualGatewayService.updateById(virtualGateway);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "virtual_gateway-通过id删除")
    @ApiOperation(value = "virtual_gateway-通过id删除", notes = "virtual_gateway-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        virtualGatewayService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "virtual_gateway-批量删除")
    @ApiOperation(value = "virtual_gateway-批量删除", notes = "virtual_gateway-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.virtualGatewayService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "virtual_gateway-通过id查询")
    @ApiOperation(value = "virtual_gateway-通过id查询", notes = "virtual_gateway-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        VirtualGateway virtualGateway = virtualGatewayService.getById(id);
        if (virtualGateway == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(virtualGateway);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param virtualGateway
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VirtualGateway virtualGateway) {
        return super.exportXls(request, virtualGateway, VirtualGateway.class, "virtual_gateway");
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
        return super.importExcel(request, response, VirtualGateway.class);
    }

}
