package com.trtm.sy.sycspz.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.sycspz.entity.SyMParamRatioAggregate;
import com.trtm.sy.sycspz.service.ISyMParamRatioAggregateService;

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
 * @Description: sy_m_param_ratio_aggregate
 * @Author: jeecg-boot
 * @Date: 2023-12-07
 * @Version: V1.0
 */
@Api(tags = "sy_m_param_ratio_aggregate")
@RestController
@RequestMapping("/sycspz/syMParamRatioAggregate")
@Slf4j
public class SyMParamRatioAggregateController extends JeecgController<SyMParamRatioAggregate, ISyMParamRatioAggregateService> {
    @Autowired
    private ISyMParamRatioAggregateService syMParamRatioAggregateService;


    @RequestMapping(value = "/mparamratioaggregeteList", method = RequestMethod.GET)
    public Result<?> mparamratioaggregeteList(HttpServletRequest request, HttpServletResponse response,
                                              @RequestParam(value = "offset", required = false) Integer offset,
                                              @RequestParam(value = "limit", required = false) Integer limit,
                                              @RequestParam(value = "GatherUse", required = false) String GatherUse,
                                              @RequestParam(value = "MixType", required = false) String MixType) {
        IPage<Map<String, Object>> list = syMParamRatioAggregateService.mparamratioaggregeteList(offset, limit, GatherUse, MixType);
        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param syMParamRatioAggregate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_m_param_ratio_aggregate-分页列表查询")
    @ApiOperation(value = "sy_m_param_ratio_aggregate-分页列表查询", notes = "sy_m_param_ratio_aggregate-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyMParamRatioAggregate syMParamRatioAggregate,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyMParamRatioAggregate> queryWrapper = QueryGenerator.initQueryWrapper(syMParamRatioAggregate, req.getParameterMap());
        Page<SyMParamRatioAggregate> page = new Page<SyMParamRatioAggregate>(pageNo, pageSize);
        IPage<SyMParamRatioAggregate> pageList = syMParamRatioAggregateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syMParamRatioAggregate
     * @return
     */
    @AutoLog(value = "sy_m_param_ratio_aggregate-添加")
    @ApiOperation(value = "sy_m_param_ratio_aggregate-添加", notes = "sy_m_param_ratio_aggregate-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyMParamRatioAggregate syMParamRatioAggregate) {
        syMParamRatioAggregateService.save(syMParamRatioAggregate);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syMParamRatioAggregate
     * @return
     */
    @AutoLog(value = "sy_m_param_ratio_aggregate-编辑")
    @ApiOperation(value = "sy_m_param_ratio_aggregate-编辑", notes = "sy_m_param_ratio_aggregate-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyMParamRatioAggregate syMParamRatioAggregate) {
        syMParamRatioAggregateService.updateById(syMParamRatioAggregate);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_m_param_ratio_aggregate-通过id删除")
    @ApiOperation(value = "sy_m_param_ratio_aggregate-通过id删除", notes = "sy_m_param_ratio_aggregate-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syMParamRatioAggregateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_m_param_ratio_aggregate-批量删除")
    @ApiOperation(value = "sy_m_param_ratio_aggregate-批量删除", notes = "sy_m_param_ratio_aggregate-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syMParamRatioAggregateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_m_param_ratio_aggregate-通过id查询")
    @ApiOperation(value = "sy_m_param_ratio_aggregate-通过id查询", notes = "sy_m_param_ratio_aggregate-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyMParamRatioAggregate syMParamRatioAggregate = syMParamRatioAggregateService.getById(id);
        if (syMParamRatioAggregate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syMParamRatioAggregate);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syMParamRatioAggregate
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyMParamRatioAggregate syMParamRatioAggregate) {
        return super.exportXls(request, syMParamRatioAggregate, SyMParamRatioAggregate.class, "sy_m_param_ratio_aggregate");
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
        return super.importExcel(request, response, SyMParamRatioAggregate.class);
    }

}
