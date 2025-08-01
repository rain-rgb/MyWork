package com.trtm.iot.gscsToolboxTalk.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.trtm.iot.gscsToolboxTalk.entity.GscsToolboxTalk;
import com.trtm.iot.gscsToolboxTalk.service.IGscsToolboxTalkService;

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
 * @Description: 班组安全管控系统班组会表
 * @Author: jeecg-boot
 * @Date: 2022-01-25
 * @Version: V1.0
 */
@Api(tags = "班组安全管控系统班组会表")
@RestController
@RequestMapping("/gscsToolboxTalk/gscsToolboxTalk")
@Slf4j
public class GscsToolboxTalkController extends JeecgController<GscsToolboxTalk, IGscsToolboxTalkService> {
    @Autowired
    private IGscsToolboxTalkService gscsToolboxTalkService;

    /**
     * 分页列表查询
     *
     * @param gscsToolboxTalk
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组会表-分页列表查询")
    @ApiOperation(value = "班组安全管控系统班组会表-分页列表查询", notes = "班组安全管控系统班组会表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GscsToolboxTalk gscsToolboxTalk,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GscsToolboxTalk> queryWrapper = QueryGenerator.initQueryWrapper(gscsToolboxTalk, req.getParameterMap());
        Page<GscsToolboxTalk> page = new Page<GscsToolboxTalk>(pageNo, pageSize);
        IPage<GscsToolboxTalk> pageList = gscsToolboxTalkService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gscsToolboxTalk
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组会表-添加")
    @ApiOperation(value = "班组安全管控系统班组会表-添加", notes = "班组安全管控系统班组会表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GscsToolboxTalk gscsToolboxTalk) {
        gscsToolboxTalkService.save(gscsToolboxTalk);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gscsToolboxTalk
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组会表-编辑")
    @ApiOperation(value = "班组安全管控系统班组会表-编辑", notes = "班组安全管控系统班组会表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GscsToolboxTalk gscsToolboxTalk) {
        gscsToolboxTalkService.updateById(gscsToolboxTalk);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组会表-通过id删除")
    @ApiOperation(value = "班组安全管控系统班组会表-通过id删除", notes = "班组安全管控系统班组会表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gscsToolboxTalkService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组会表-批量删除")
    @ApiOperation(value = "班组安全管控系统班组会表-批量删除", notes = "班组安全管控系统班组会表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gscsToolboxTalkService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "班组安全管控系统班组会表-通过id查询")
    @ApiOperation(value = "班组安全管控系统班组会表-通过id查询", notes = "班组安全管控系统班组会表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GscsToolboxTalk gscsToolboxTalk = gscsToolboxTalkService.getById(id);
        if (gscsToolboxTalk == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gscsToolboxTalk);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gscsToolboxTalk
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GscsToolboxTalk gscsToolboxTalk) {
        return super.exportXls(request, gscsToolboxTalk, GscsToolboxTalk.class, "班组安全管控系统班组会表");
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
        return super.importExcel(request, response, GscsToolboxTalk.class);
    }

}
