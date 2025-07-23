package com.trtm.sy.sylxdps.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.sylxdps.entity.SyDpsJcTestitem;
import com.trtm.sy.sylxdps.service.ISyDpsJcTestitemService;
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
 * @Description: sy_dps_jc_testitem
 * @Author: jeecg-boot
 * @Date: 2023-01-10
 * @Version: V1.0
 */
@Api(tags = "sy_dps_jc_testitem")
@RestController
@RequestMapping("/sylxdps/syDpsJcTestitem")
@Slf4j
public class SyDpsJcTestitemController extends JeecgController<SyDpsJcTestitem, ISyDpsJcTestitemService> {
    @Autowired
    private ISyDpsJcTestitemService syDpsJcTestitemService;

    /**
     * 分页列表查询
     *
     * @param syDpsJcTestitem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_dps_jc_testitem-分页列表查询")
    @ApiOperation(value = "sy_dps_jc_testitem-分页列表查询", notes = "sy_dps_jc_testitem-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyDpsJcTestitem syDpsJcTestitem,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyDpsJcTestitem> queryWrapper = QueryGenerator.initQueryWrapper(syDpsJcTestitem, req.getParameterMap());
        Page<SyDpsJcTestitem> page = new Page<SyDpsJcTestitem>(pageNo, pageSize);
        IPage<SyDpsJcTestitem> pageList = syDpsJcTestitemService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syDpsJcTestitem
     * @return
     */
    @AutoLog(value = "sy_dps_jc_testitem-添加")
    @ApiOperation(value = "sy_dps_jc_testitem-添加", notes = "sy_dps_jc_testitem-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyDpsJcTestitem syDpsJcTestitem) {
        syDpsJcTestitemService.save(syDpsJcTestitem);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syDpsJcTestitem
     * @return
     */
    @AutoLog(value = "sy_dps_jc_testitem-编辑")
    @ApiOperation(value = "sy_dps_jc_testitem-编辑", notes = "sy_dps_jc_testitem-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyDpsJcTestitem syDpsJcTestitem) {
        syDpsJcTestitemService.updateById(syDpsJcTestitem);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_jc_testitem-通过id删除")
    @ApiOperation(value = "sy_dps_jc_testitem-通过id删除", notes = "sy_dps_jc_testitem-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syDpsJcTestitemService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_dps_jc_testitem-批量删除")
    @ApiOperation(value = "sy_dps_jc_testitem-批量删除", notes = "sy_dps_jc_testitem-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syDpsJcTestitemService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_jc_testitem-通过id查询")
    @ApiOperation(value = "sy_dps_jc_testitem-通过id查询", notes = "sy_dps_jc_testitem-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyDpsJcTestitem syDpsJcTestitem = syDpsJcTestitemService.getById(id);
        if (syDpsJcTestitem == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syDpsJcTestitem);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syDpsJcTestitem
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsJcTestitem syDpsJcTestitem) {
        return super.exportXls(request, syDpsJcTestitem, SyDpsJcTestitem.class, "sy_dps_jc_testitem");
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
        return super.importExcel(request, response, SyDpsJcTestitem.class);
    }


    /**
     * 根据id查询表格数据
     */
    @GetMapping("/findById")
    public Result<?> findById(@RequestParam(value = "id", required = false) String id,
                              @RequestParam(value = "tiNo", required = false) String tiNo,
                              @RequestParam(value = "departid", required = false) String departid) {
        Map map = syDpsJcTestitemService.findById(id);
        return Result.OK(map);
    }

    /**
     * 根据id查询样品参数信息
     */
    @GetMapping("/findSamp/{id}")
    public Result<?> findSamp(@PathVariable("id") String id) {
        Map map = syDpsJcTestitemService.findSamp(id);
        return Result.OK(map);
    }

}
