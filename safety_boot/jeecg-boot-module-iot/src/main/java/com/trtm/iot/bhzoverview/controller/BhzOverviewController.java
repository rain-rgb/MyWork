package com.trtm.iot.bhzoverview.controller;

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
import com.trtm.iot.bhzoverview.entity.BhzOverview;
import com.trtm.iot.bhzoverview.service.IBhzOverviewService;

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
 * @Description: bhz_overview
 * @Author: jeecg-boot
 * @Date: 2023-11-03
 * @Version: V1.0
 */
@Api(tags = "bhz_overview")
@RestController
@RequestMapping("/bhzoverview/bhzOverview")
@Slf4j
public class BhzOverviewController extends JeecgController<BhzOverview, IBhzOverviewService> {
    @Autowired
    private IBhzOverviewService bhzOverviewService;

    /**
     * 分页列表查询
     *
     * @param bhzOverview
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_overview-分页列表查询")
    @ApiOperation(value = "bhz_overview-分页列表查询", notes = "bhz_overview-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzOverview bhzOverview,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        bhzOverview.setBhzName("*" + bhzOverview.getBhzName() + "*");
        QueryWrapper<BhzOverview> queryWrapper = QueryGenerator.initQueryWrapper(bhzOverview, req.getParameterMap());
        Page<BhzOverview> page = new Page<BhzOverview>(pageNo, pageSize);
        IPage<BhzOverview> pageList = bhzOverviewService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bhzOverview
     * @return
     */
    @AutoLog(value = "bhz_overview-添加")
    @ApiOperation(value = "bhz_overview-添加", notes = "bhz_overview-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzOverview bhzOverview) {
        bhzOverviewService.save(bhzOverview);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bhzOverview
     * @return
     */
    @AutoLog(value = "bhz_overview-编辑")
    @ApiOperation(value = "bhz_overview-编辑", notes = "bhz_overview-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzOverview bhzOverview) {
        bhzOverviewService.updateById(bhzOverview);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_overview-通过id删除")
    @ApiOperation(value = "bhz_overview-通过id删除", notes = "bhz_overview-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzOverviewService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "bhz_overview-批量删除")
    @ApiOperation(value = "bhz_overview-批量删除", notes = "bhz_overview-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzOverviewService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_overview-通过id查询")
    @ApiOperation(value = "bhz_overview-通过id查询", notes = "bhz_overview-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzOverview bhzOverview = bhzOverviewService.getById(id);
        if (bhzOverview == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzOverview);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzOverview
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzOverview bhzOverview) {
        return super.exportXls(request, bhzOverview, BhzOverview.class, "bhz_overview");
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
        return super.importExcel(request, response, BhzOverview.class);
    }

}
