package com.trtm.iot.syLeixingExp.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.trtm.iot.syLeixingExp.entity.SyLeixingExp;
import com.trtm.iot.syLeixingExp.service.ISyLeixingExpService;

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
 * @Description: 试验类型和试验关联表
 * @Author: jeecg-boot
 * @Date: 2022-03-22
 * @Version: V1.0
 */
@Api(tags = "试验类型和试验关联表")
@RestController
@RequestMapping("/syLeixingExp/syLeixingExp")
@Slf4j
public class SyLeixingExpController extends JeecgController<SyLeixingExp, ISyLeixingExpService> {
    @Autowired
    private ISyLeixingExpService syLeixingExpService;

    /**
     * 分页列表查询
     *
     * @param syLeixingExp
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "试验类型和试验关联表-分页列表查询")
    @ApiOperation(value = "试验类型和试验关联表-分页列表查询", notes = "试验类型和试验关联表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyLeixingExp syLeixingExp,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyLeixingExp> queryWrapper = QueryGenerator.initQueryWrapper(syLeixingExp, req.getParameterMap());
        Page<SyLeixingExp> page = new Page<SyLeixingExp>(pageNo, pageSize);
        IPage<SyLeixingExp> pageList = syLeixingExpService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syLeixingExp
     * @return
     */
    @AutoLog(value = "试验类型和试验关联表-添加")
    @ApiOperation(value = "试验类型和试验关联表-添加", notes = "试验类型和试验关联表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyLeixingExp syLeixingExp) {
        syLeixingExpService.save(syLeixingExp);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syLeixingExp
     * @return
     */
    @AutoLog(value = "试验类型和试验关联表-编辑")
    @ApiOperation(value = "试验类型和试验关联表-编辑", notes = "试验类型和试验关联表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyLeixingExp syLeixingExp) {
        syLeixingExpService.updateById(syLeixingExp);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "试验类型和试验关联表-通过id删除")
    @ApiOperation(value = "试验类型和试验关联表-通过id删除", notes = "试验类型和试验关联表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syLeixingExpService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "试验类型和试验关联表-批量删除")
    @ApiOperation(value = "试验类型和试验关联表-批量删除", notes = "试验类型和试验关联表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syLeixingExpService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "试验类型和试验关联表-通过id查询")
    @ApiOperation(value = "试验类型和试验关联表-通过id查询", notes = "试验类型和试验关联表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyLeixingExp syLeixingExp = syLeixingExpService.getById(id);
        if (syLeixingExp == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syLeixingExp);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syLeixingExp
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyLeixingExp syLeixingExp) {
        return super.exportXls(request, syLeixingExp, SyLeixingExp.class, "试验类型和试验关联表");
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
        return super.importExcel(request, response, SyLeixingExp.class);
    }

    /**
     * 跟据类型id查询试验
     *
     * @param typeId
     */
    @AutoLog(value = "跟据类型id查询试验")
    @ApiOperation(value = "试验类型和试验关联表-分页列表查询", notes = "试验类型和试验关联表-分页列表查询")
    @GetMapping(value = "/getExpByTypeId")
    public Result<?> getExpByTypeId(@RequestParam(name = "typeId", required = true) String typeId) {
        List expList = syLeixingExpService.getExpList(typeId);
        return Result.OK(expList);
    }

}
