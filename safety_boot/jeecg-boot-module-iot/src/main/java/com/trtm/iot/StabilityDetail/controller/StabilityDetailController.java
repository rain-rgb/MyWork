package com.trtm.iot.StabilityDetail.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.StabilityDetail.entity.StabilityDetail;
import com.trtm.iot.StabilityDetail.service.IStabilityDetailService;

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
 * @Description: 稳定度子表
 * @Author: jeecg-boot
 * @Date: 2022-12-07
 * @Version: V1.0
 */
@Api(tags = "稳定度子表")
@RestController
@RequestMapping("/StabilityDetail/stabilityDetail")
@Slf4j
public class StabilityDetailController extends JeecgController<StabilityDetail, IStabilityDetailService> {
    @Autowired
    private IStabilityDetailService stabilityDetailService;

    /**
     * 分页列表查询
     *
     * @param stabilityDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "稳定度子表-分页列表查询")
    @ApiOperation(value = "稳定度子表-分页列表查询", notes = "稳定度子表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(StabilityDetail stabilityDetail,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<StabilityDetail> queryWrapper = QueryGenerator.initQueryWrapper(stabilityDetail, req.getParameterMap());
        Page<StabilityDetail> page = new Page<StabilityDetail>(pageNo, pageSize);
        IPage<StabilityDetail> pageList = stabilityDetailService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param stabilityDetail
     * @return
     */
    @AutoLog(value = "稳定度子表-添加")
    @ApiOperation(value = "稳定度子表-添加", notes = "稳定度子表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody StabilityDetail stabilityDetail) {
        stabilityDetailService.save(stabilityDetail);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param stabilityDetail
     * @return
     */
    @AutoLog(value = "稳定度子表-编辑")
    @ApiOperation(value = "稳定度子表-编辑", notes = "稳定度子表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody StabilityDetail stabilityDetail) {
        stabilityDetailService.updateById(stabilityDetail);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "稳定度子表-通过id删除")
    @ApiOperation(value = "稳定度子表-通过id删除", notes = "稳定度子表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        stabilityDetailService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "稳定度子表-批量删除")
    @ApiOperation(value = "稳定度子表-批量删除", notes = "稳定度子表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.stabilityDetailService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过guid查询
     *
     * @param guid
     * @return
     */
    @AutoLog(value = "稳定度子表-通过guid查询")
    @ApiOperation(value = "稳定度子表-通过guid查询", notes = "稳定度子表-通过guid查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "guid", required = true) String guid) {
        QueryWrapper<StabilityDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("F_GUID",guid);
        StabilityDetail stabilityDetail = stabilityDetailService.getOne(queryWrapper);
//        StabilityDetail stabilityDetail = stabilityDetailService.getById(id);
        if (stabilityDetail == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(stabilityDetail);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param stabilityDetail
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StabilityDetail stabilityDetail) {
        return super.exportXls(request, stabilityDetail, StabilityDetail.class, "稳定度子表");
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
        return super.importExcel(request, response, StabilityDetail.class);
    }

}
