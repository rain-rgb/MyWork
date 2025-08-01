package com.trtm.iot.projoverview.controller;

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
import com.trtm.iot.projoverview.entity.ProjOverview;
import com.trtm.iot.projoverview.service.IProjOverviewService;

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
 * @Description: proj_overview
 * @Author: jeecg-boot
 * @Date: 2023-11-03
 * @Version: V1.0
 */
@Api(tags = "proj_overview")
@RestController
@RequestMapping("/projoverview/projOverview")
@Slf4j
public class ProjOverviewController extends JeecgController<ProjOverview, IProjOverviewService> {
    @Autowired
    private IProjOverviewService projOverviewService;

    /**
     * 分页列表查询
     *
     * @param projOverview
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "proj_overview-分页列表查询")
    @ApiOperation(value = "proj_overview-分页列表查询", notes = "proj_overview-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ProjOverview projOverview,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        projOverview.setProjName("*" + projOverview.getProjName() + "*");
        QueryWrapper<ProjOverview> queryWrapper = QueryGenerator.initQueryWrapper(projOverview, req.getParameterMap());
        Page<ProjOverview> page = new Page<ProjOverview>(pageNo, pageSize);
        IPage<ProjOverview> pageList = projOverviewService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param projOverview
     * @return
     */
    @AutoLog(value = "proj_overview-添加")
    @ApiOperation(value = "proj_overview-添加", notes = "proj_overview-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ProjOverview projOverview) {
        projOverviewService.save(projOverview);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param projOverview
     * @return
     */
    @AutoLog(value = "proj_overview-编辑")
    @ApiOperation(value = "proj_overview-编辑", notes = "proj_overview-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ProjOverview projOverview) {
        projOverviewService.updateById(projOverview);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "proj_overview-通过id删除")
    @ApiOperation(value = "proj_overview-通过id删除", notes = "proj_overview-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        projOverviewService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "proj_overview-批量删除")
    @ApiOperation(value = "proj_overview-批量删除", notes = "proj_overview-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.projOverviewService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "proj_overview-通过id查询")
    @ApiOperation(value = "proj_overview-通过id查询", notes = "proj_overview-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ProjOverview projOverview = projOverviewService.getById(id);
        if (projOverview == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(projOverview);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param projOverview
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjOverview projOverview) {
        return super.exportXls(request, projOverview, ProjOverview.class, "proj_overview");
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
        return super.importExcel(request, response, ProjOverview.class);
    }

}
