package com.trtm.sy.symainconf.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.symainconf.entity.SyMainConf;
import com.trtm.sy.symainconf.service.ISyMainConfService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

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
 * @Description: sy_main_conf
 * @Author: jeecg-boot
 * @Date: 2022-09-08
 * @Version: V1.0
 */
@Api(tags = "sy_main_conf")
@RestController
@RequestMapping("/syMainConf/syMainConf")
@Slf4j
public class SyMainConfController extends JeecgController<SyMainConf, ISyMainConfService> {
    @Autowired
    private ISyMainConfService syMainConfService;

    /**
     * 分页列表查询
     *
     * @param syMainConf
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_main_conf-分页列表查询")
    @ApiOperation(value = "sy_main_conf-分页列表查询", notes = "sy_main_conf-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyMainConf syMainConf,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyMainConf> queryWrapper = QueryGenerator.initQueryWrapper(syMainConf, req.getParameterMap());
        queryWrapper.groupBy("test_id");
        Page<SyMainConf> page = new Page<SyMainConf>(pageNo, pageSize);
        IPage<SyMainConf> pageList = syMainConfService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syMainConf
     * @return
     */
    @AutoLog(value = "sy_main_conf-添加")
    @ApiOperation(value = "sy_main_conf-添加", notes = "sy_main_conf-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyMainConf syMainConf) {
        syMainConfService.save(syMainConf);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syMainConf
     * @return
     */
    @AutoLog(value = "sy_main_conf-编辑")
    @ApiOperation(value = "sy_main_conf-编辑", notes = "sy_main_conf-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyMainConf syMainConf) {
        syMainConfService.updateById(syMainConf);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过uuid删除
     *
     * @param uuid
     * @return
     */
    @AutoLog(value = "通过uuid删除sy_main_conf表中记录")
    @ApiOperation(value = "sy_main_conf-通过id删除", notes = "sy_main_conf-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "uuid") String uuid) {
        QueryWrapper<SyMainConf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid);
        syMainConfService.remove(queryWrapper);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_main_conf-批量删除")
    @ApiOperation(value = "sy_main_conf-批量删除", notes = "sy_main_conf-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syMainConfService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过uuid查询
     *
     * @param uuid
     * @return
     */
    @AutoLog(value = "通过uuid查询sy_main_conf表中记录")
    @ApiOperation(value = "sy_main_conf-通过id查询", notes = "sy_main_conf-通过id查询")
    @GetMapping(value = "/queryByUuId")
    public Result<?> queryById(@RequestParam(name = "uuid") String uuid) {
        QueryWrapper<SyMainConf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid);
        SyMainConf data = syMainConfService.getOne(queryWrapper);
        if (data == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(data);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syMainConf
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyMainConf syMainConf) {
        return super.exportXls(request, syMainConf, SyMainConf.class, "sy_main_conf");
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
        return super.importExcel(request, response, SyMainConf.class);
    }

}
