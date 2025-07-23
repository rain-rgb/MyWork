package com.trtm.sy.sysb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sysb.entity.SyDpsJcShebei;
import com.trtm.sy.sysb.service.ISyDpsJcShebeiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: sy_dps_jc_shebei
 * @Author: jeecg-boot
 * @Date: 2023-10-16
 * @Version: V1.0
 */
@Api(tags = "sy_dps_jc_shebei")
@RestController
@RequestMapping("/sysb/syDpsJcShebei")
@Slf4j
public class SyDpsJcShebeiController extends JeecgController<SyDpsJcShebei, ISyDpsJcShebeiService> {
    @Autowired
    private ISyDpsJcShebeiService syDpsJcShebeiService;

    /**
     * 分页列表查询
     *
     * @param syDpsJcShebei
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei-分页列表查询")
    @ApiOperation(value = "sy_dps_jc_shebei-分页列表查询", notes = "sy_dps_jc_shebei-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyDpsJcShebei syDpsJcShebei, String sys_depart_orgcode,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        syDpsJcShebei.setOrgcode(sys_depart_orgcode + "*");
        if (oConvertUtils.isNotEmpty(syDpsJcShebei.getShebeiname())) {
            syDpsJcShebei.setShebeiname("*" + syDpsJcShebei.getShebeiname() + "*");
        }
        QueryWrapper<SyDpsJcShebei> queryWrapper = QueryGenerator.initQueryWrapper(syDpsJcShebei, req.getParameterMap());
        queryWrapper.lambda().orderByDesc(SyDpsJcShebei::getShebeigouzhiriqi);
        Page<SyDpsJcShebei> page = new Page<SyDpsJcShebei>(pageNo, pageSize);
        IPage<SyDpsJcShebei> pageList = syDpsJcShebeiService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syDpsJcShebei
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei-添加")
    @ApiOperation(value = "sy_dps_jc_shebei-添加", notes = "sy_dps_jc_shebei-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyDpsJcShebei syDpsJcShebei) {
        syDpsJcShebeiService.add(syDpsJcShebei);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syDpsJcShebei
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei-编辑")
    @ApiOperation(value = "sy_dps_jc_shebei-编辑", notes = "sy_dps_jc_shebei-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyDpsJcShebei syDpsJcShebei) {
        syDpsJcShebeiService.updateById(syDpsJcShebei);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei-通过id删除")
    @ApiOperation(value = "sy_dps_jc_shebei-通过id删除", notes = "sy_dps_jc_shebei-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syDpsJcShebeiService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei-批量删除")
    @ApiOperation(value = "sy_dps_jc_shebei-批量删除", notes = "sy_dps_jc_shebei-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syDpsJcShebeiService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_jc_shebei-通过id查询")
    @ApiOperation(value = "sy_dps_jc_shebei-通过id查询", notes = "sy_dps_jc_shebei-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyDpsJcShebei syDpsJcShebei = syDpsJcShebeiService.getById(id);
        if (syDpsJcShebei == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syDpsJcShebei);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syDpsJcShebei
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsJcShebei syDpsJcShebei) {
        return super.exportXls(request, syDpsJcShebei, SyDpsJcShebei.class, "sy_dps_jc_shebei");
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
        return super.importExcel(request, response, SyDpsJcShebei.class);
    }

}
