package com.trtm.sy.sysbjdjz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sysbjdjz.entity.SbJianDingJiaoZhunResponse;
import com.trtm.sy.sysbjdjz.entity.SyDpsJcShebeiJiandingjiaozhun;
import com.trtm.sy.sysbjdjz.service.ISyDpsJcShebeiJiandingjiaozhunService;
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
import java.util.List;

/**
 * @Description: sy_dps_jc_shebei_jiandingjiaozhun
 * @Author: jeecg-boot
 * @Date: 2023-10-17
 * @Version: V1.0
 */
@Api(tags = "sy_dps_jc_shebei_jiandingjiaozhun")
@RestController
@RequestMapping("/sysbjdjz/syDpsJcShebeiJiandingjiaozhun")
@Slf4j
public class SyDpsJcShebeiJiandingjiaozhunController extends JeecgController<SyDpsJcShebeiJiandingjiaozhun, ISyDpsJcShebeiJiandingjiaozhunService> {
    @Autowired
    private ISyDpsJcShebeiJiandingjiaozhunService syDpsJcShebeiJiandingjiaozhunService;

    /**
     * 分页列表查询
     *
     * @param syDpsJcShebeiJiandingjiaozhun
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei_jiandingjiaozhun-分页列表查询")
    @ApiOperation(value = "sy_dps_jc_shebei_jiandingjiaozhun-分页列表查询", notes = "sy_dps_jc_shebei_jiandingjiaozhun-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyDpsJcShebeiJiandingjiaozhun syDpsJcShebeiJiandingjiaozhun,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyDpsJcShebeiJiandingjiaozhun> queryWrapper = QueryGenerator.initQueryWrapper(syDpsJcShebeiJiandingjiaozhun, req.getParameterMap());
        Page<SyDpsJcShebeiJiandingjiaozhun> page = new Page<SyDpsJcShebeiJiandingjiaozhun>(pageNo, pageSize);
        IPage<SyDpsJcShebeiJiandingjiaozhun> pageList = syDpsJcShebeiJiandingjiaozhunService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syDpsJcShebeiJiandingjiaozhun
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei_jiandingjiaozhun-添加")
    @ApiOperation(value = "sy_dps_jc_shebei_jiandingjiaozhun-添加", notes = "sy_dps_jc_shebei_jiandingjiaozhun-添加")
    @PostMapping(value = "/addOrUpdate")
    public Result<?> addOrUpdate(@RequestBody SyDpsJcShebeiJiandingjiaozhun syDpsJcShebeiJiandingjiaozhun) {
        try {
            syDpsJcShebeiJiandingjiaozhunService.addOrUpdate(syDpsJcShebeiJiandingjiaozhun);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败");
        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syDpsJcShebeiJiandingjiaozhun
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei_jiandingjiaozhun-编辑")
    @ApiOperation(value = "sy_dps_jc_shebei_jiandingjiaozhun-编辑", notes = "sy_dps_jc_shebei_jiandingjiaozhun-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyDpsJcShebeiJiandingjiaozhun syDpsJcShebeiJiandingjiaozhun) {
        syDpsJcShebeiJiandingjiaozhunService.updateById(syDpsJcShebeiJiandingjiaozhun);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei_jiandingjiaozhun-通过id删除")
    @ApiOperation(value = "sy_dps_jc_shebei_jiandingjiaozhun-通过id删除", notes = "sy_dps_jc_shebei_jiandingjiaozhun-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syDpsJcShebeiJiandingjiaozhunService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei_jiandingjiaozhun-批量删除")
    @ApiOperation(value = "sy_dps_jc_shebei_jiandingjiaozhun-批量删除", notes = "sy_dps_jc_shebei_jiandingjiaozhun-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syDpsJcShebeiJiandingjiaozhunService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei_jiandingjiaozhun-通过id查询")
    @ApiOperation(value = "sy_dps_jc_shebei_jiandingjiaozhun-通过id查询", notes = "sy_dps_jc_shebei_jiandingjiaozhun-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyDpsJcShebeiJiandingjiaozhun syDpsJcShebeiJiandingjiaozhun = syDpsJcShebeiJiandingjiaozhunService.getById(id);
        if (syDpsJcShebeiJiandingjiaozhun == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syDpsJcShebeiJiandingjiaozhun);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syDpsJcShebeiJiandingjiaozhun
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsJcShebeiJiandingjiaozhun syDpsJcShebeiJiandingjiaozhun) {
        return super.exportXls(request, syDpsJcShebeiJiandingjiaozhun, SyDpsJcShebeiJiandingjiaozhun.class, "sy_dps_jc_shebei_jiandingjiaozhun");
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
        return super.importExcel(request, response, SyDpsJcShebeiJiandingjiaozhun.class);
    }

    @GetMapping("/getJdJz")
    public Result<?> getJdJz(@RequestParam String sheBeiIds, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime,
                             @RequestParam(required = false) Integer type, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        IPage<SbJianDingJiaoZhunResponse> list = syDpsJcShebeiJiandingjiaozhunService.getJdJz(sheBeiIds, startTime, endTime, type, pageNo, pageSize);
        return Result.OK(list);
    }

}
