package com.trtm.iot.gscsDept.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.trtm.iot.gscsDept.entity.GscsDept;
import com.trtm.iot.gscsDept.service.IGscsDeptService;

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
 * @Description: 班组安全管控系统班组表
 * @Author: jeecg-boot
 * @Date: 2022-01-25
 * @Version: V1.0
 */
@Api(tags = "班组安全管控系统班组表")
@RestController
@RequestMapping("/gscsDept/gscsDept")
@Slf4j
public class GscsDeptController extends JeecgController<GscsDept, IGscsDeptService> {
    @Autowired
    private IGscsDeptService gscsDeptService;

    /**
     * 分页列表查询
     *
     * @param gscsDept
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组表-分页列表查询")
    @ApiOperation(value = "班组安全管控系统班组表-分页列表查询", notes = "班组安全管控系统班组表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GscsDept gscsDept,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GscsDept> queryWrapper = QueryGenerator.initQueryWrapper(gscsDept, req.getParameterMap());
        Page<GscsDept> page = new Page<GscsDept>(pageNo, pageSize);
        IPage<GscsDept> pageList = gscsDeptService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gscsDept
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组表-添加")
    @ApiOperation(value = "班组安全管控系统班组表-添加", notes = "班组安全管控系统班组表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GscsDept gscsDept) {
        gscsDeptService.save(gscsDept);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gscsDept
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组表-编辑")
    @ApiOperation(value = "班组安全管控系统班组表-编辑", notes = "班组安全管控系统班组表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GscsDept gscsDept) {
        gscsDeptService.updateById(gscsDept);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组表-通过id删除")
    @ApiOperation(value = "班组安全管控系统班组表-通过id删除", notes = "班组安全管控系统班组表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gscsDeptService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组表-批量删除")
    @ApiOperation(value = "班组安全管控系统班组表-批量删除", notes = "班组安全管控系统班组表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gscsDeptService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组表-通过id查询")
    @ApiOperation(value = "班组安全管控系统班组表-通过id查询", notes = "班组安全管控系统班组表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GscsDept gscsDept = gscsDeptService.getById(id);
        if (gscsDept == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gscsDept);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gscsDept
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GscsDept gscsDept) {
        return super.exportXls(request, gscsDept, GscsDept.class, "班组安全管控系统班组表");
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
        return super.importExcel(request, response, GscsDept.class);
    }

}
