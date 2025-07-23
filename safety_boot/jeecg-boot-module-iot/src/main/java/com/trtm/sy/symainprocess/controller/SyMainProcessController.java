package com.trtm.sy.symainprocess.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.symainprocess.entity.SyMainProcess;
import com.trtm.sy.symainprocess.service.ISyMainProcessService;

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
 * @Description: sy_main_process
 * @Author: jeecg-boot
 * @Date: 2022-09-08
 * @Version: V1.0
 */
@Api(tags = "sy_main_process")
@RestController
@RequestMapping("/syMainProcess/syMainProcess")
@Slf4j
public class SyMainProcessController extends JeecgController<SyMainProcess, ISyMainProcessService> {
    @Autowired
    private ISyMainProcessService syMainProcessService;

    /**
     * 分页列表查询
     *
     * @param syMainProcess
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_main_process-分页列表查询")
    @ApiOperation(value = "sy_main_process-分页列表查询", notes = "sy_main_process-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyMainProcess syMainProcess,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyMainProcess> queryWrapper = QueryGenerator.initQueryWrapper(syMainProcess, req.getParameterMap());
        Page<SyMainProcess> page = new Page<SyMainProcess>(pageNo, pageSize);
        IPage<SyMainProcess> pageList = syMainProcessService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syMainProcess
     * @return
     */
    @AutoLog(value = "sy_main_process-添加")
    @ApiOperation(value = "sy_main_process-添加", notes = "sy_main_process-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyMainProcess syMainProcess) {
        syMainProcessService.save(syMainProcess);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syMainProcess
     * @return
     */
    @AutoLog(value = "sy_main_process-编辑")
    @ApiOperation(value = "sy_main_process-编辑", notes = "sy_main_process-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyMainProcess syMainProcess) {
        syMainProcessService.updateById(syMainProcess);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param uuid
     * @return
     */
    @AutoLog(value = "通过uuid删除sy_main_process表中记录")
    @ApiOperation(value = "删除单个", notes = "通过uuid删除sy_main_process表中记录")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "uuid") String uuid) {
        QueryWrapper<SyMainProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid);
        syMainProcessService.remove(queryWrapper);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_main_process-批量删除")
    @ApiOperation(value = "sy_main_process-批量删除", notes = "sy_main_process-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syMainProcessService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 跟据uuid查询
     *
     * @param uuid
     * @return
     */
    @AutoLog(value = "跟据uuid查询sy_main_process")
    @ApiOperation(value = "跟据uuid查询", notes = "跟据uuid查询")
    @GetMapping(value = "/queryByUuid")
    public Result<?> queryById(@RequestParam(name = "uuid") String uuid) {
        QueryWrapper<SyMainProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid);
        SyMainProcess data = syMainProcessService.getOne(queryWrapper);
        if (data == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(data);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syMainProcess
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyMainProcess syMainProcess) {
        return super.exportXls(request, syMainProcess, SyMainProcess.class, "sy_main_process");
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
        return super.importExcel(request, response, SyMainProcess.class);
    }

}
