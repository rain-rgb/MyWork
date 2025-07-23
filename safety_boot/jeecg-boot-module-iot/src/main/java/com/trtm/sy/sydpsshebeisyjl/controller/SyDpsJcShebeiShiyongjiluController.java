package com.trtm.sy.sydpsshebeisyjl.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sydpsshebeisyjl.entity.SheBeiSyJlResponse;
import com.trtm.sy.sydpsshebeisyjl.entity.SyDpsJcShebeiShiyongjilu;
import com.trtm.sy.sydpsshebeisyjl.service.ISyDpsJcShebeiShiyongjiluService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: dps_jc_shebei_shiyongjilu
 * @Author: jeecg-boot
 * @Date: 2023-02-27
 * @Version: V1.0
 */
@Api(tags = "dps_jc_shebei_shiyongjilu")
@RestController
@RequestMapping("/sydpsshebeisyjl/SyDpsJcShebeiShiyongjilu")
@Slf4j
public class SyDpsJcShebeiShiyongjiluController extends JeecgController<SyDpsJcShebeiShiyongjilu, ISyDpsJcShebeiShiyongjiluService> {
    @Autowired
    private ISyDpsJcShebeiShiyongjiluService dpsJcShebeiShiyongjiluService;

    /**
     * 分页列表查询
     *
     * @param dpsJcShebeiShiyongjilu
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "dps_jc_shebei_shiyongjilu-分页列表查询")
    @ApiOperation(value = "dps_jc_shebei_shiyongjilu-分页列表查询", notes = "dps_jc_shebei_shiyongjilu-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyDpsJcShebeiShiyongjilu dpsJcShebeiShiyongjilu,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyDpsJcShebeiShiyongjilu> queryWrapper = QueryGenerator.initQueryWrapper(dpsJcShebeiShiyongjilu, req.getParameterMap());
        Page<SyDpsJcShebeiShiyongjilu> page = new Page<SyDpsJcShebeiShiyongjilu>(pageNo, pageSize);
        IPage<SyDpsJcShebeiShiyongjilu> pageList = dpsJcShebeiShiyongjiluService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param dpsJcShebeiShiyongjilu
     * @return
     */
    @AutoLog(value = "dps_jc_shebei_shiyongjilu-添加")
    @ApiOperation(value = "dps_jc_shebei_shiyongjilu-添加", notes = "dps_jc_shebei_shiyongjilu-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyDpsJcShebeiShiyongjilu dpsJcShebeiShiyongjilu) {
        dpsJcShebeiShiyongjiluService.save(dpsJcShebeiShiyongjilu);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param dpsJcShebeiShiyongjilu
     * @return
     */
    @AutoLog(value = "dps_jc_shebei_shiyongjilu-编辑")
    @ApiOperation(value = "dps_jc_shebei_shiyongjilu-编辑", notes = "dps_jc_shebei_shiyongjilu-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyDpsJcShebeiShiyongjilu dpsJcShebeiShiyongjilu) {
        dpsJcShebeiShiyongjiluService.updateById(dpsJcShebeiShiyongjilu);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "dps_jc_shebei_shiyongjilu-通过id删除")
    @ApiOperation(value = "dps_jc_shebei_shiyongjilu-通过id删除", notes = "dps_jc_shebei_shiyongjilu-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        dpsJcShebeiShiyongjiluService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "dps_jc_shebei_shiyongjilu-批量删除")
    @ApiOperation(value = "dps_jc_shebei_shiyongjilu-批量删除", notes = "dps_jc_shebei_shiyongjilu-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.dpsJcShebeiShiyongjiluService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "dps_jc_shebei_shiyongjilu-通过id查询")
    @ApiOperation(value = "dps_jc_shebei_shiyongjilu-通过id查询", notes = "dps_jc_shebei_shiyongjilu-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyDpsJcShebeiShiyongjilu dpsJcShebeiShiyongjilu = dpsJcShebeiShiyongjiluService.getById(id);
        if (dpsJcShebeiShiyongjilu == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(dpsJcShebeiShiyongjilu);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param dpsJcShebeiShiyongjilu
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsJcShebeiShiyongjilu dpsJcShebeiShiyongjilu) {
        return super.exportXls(request, dpsJcShebeiShiyongjilu, SyDpsJcShebeiShiyongjilu.class, "dps_jc_shebei_shiyongjilu");
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
        return super.importExcel(request, response, SyDpsJcShebeiShiyongjilu.class);
    }


    @GetMapping("/getSbSyJl")
    public Result<?> getSbSyJl(@RequestParam String sheBeiIds, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime,
                               @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        IPage<SheBeiSyJlResponse> list = dpsJcShebeiShiyongjiluService.getSbSyJl(sheBeiIds, startTime, endTime, pageNo, pageSize);
        return Result.OK(list);
    }

}
