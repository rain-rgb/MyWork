package com.trtm.iot.gscsInformUser.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.trtm.iot.gscsInformUser.entity.GscsInformUser;
import com.trtm.iot.gscsInformUser.service.IGscsInformUserService;

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
 * @Description: 安全班组会通知与用户关联表
 * @Author: jeecg-boot
 * @Date: 2022-02-14
 * @Version: V1.0
 */
@Api(tags = "安全班组会通知与用户关联表")
@RestController
@RequestMapping("/gscsInformUser/gscsInformUser")
@Slf4j
public class GscsInformUserController extends JeecgController<GscsInformUser, IGscsInformUserService> {
    @Autowired
    private IGscsInformUserService gscsInformUserService;

    /**
     * 分页列表查询
     *
     * @param gscsInformUser
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "安全班组会通知与用户关联表-分页列表查询")
    @ApiOperation(value = "安全班组会通知与用户关联表-分页列表查询", notes = "安全班组会通知与用户关联表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GscsInformUser gscsInformUser,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GscsInformUser> queryWrapper = QueryGenerator.initQueryWrapper(gscsInformUser, req.getParameterMap());
        Page<GscsInformUser> page = new Page<GscsInformUser>(pageNo, pageSize);
        IPage<GscsInformUser> pageList = gscsInformUserService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 查询参加考试总人数
     *
     * @return
     */
    @AutoLog(value = "查询参加考试总人数")
    @ApiOperation(value = "查询参加考试总人数", notes = "查询参加考试总人数")
    @GetMapping(value = "/getJoinTestCount")
    public Result<?> getJoinTestCount() {
        Integer turnout = gscsInformUserService.selectJoinTestCount();
        return Result.OK(turnout);
    }

    /**
     * 添加
     *
     * @param gscsInformUser
     * @return
     */
    @AutoLog(value = "安全班组会通知与用户关联表-添加")
    @ApiOperation(value = "安全班组会通知与用户关联表-添加", notes = "安全班组会通知与用户关联表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GscsInformUser gscsInformUser) {
        gscsInformUserService.save(gscsInformUser);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gscsInformUser
     * @return
     */
    @AutoLog(value = "安全班组会通知与用户关联表-编辑")
    @ApiOperation(value = "安全班组会通知与用户关联表-编辑", notes = "安全班组会通知与用户关联表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GscsInformUser gscsInformUser) {
        gscsInformUserService.updateById(gscsInformUser);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "安全班组会通知与用户关联表-通过id删除")
    @ApiOperation(value = "安全班组会通知与用户关联表-通过id删除", notes = "安全班组会通知与用户关联表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gscsInformUserService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "安全班组会通知与用户关联表-批量删除")
    @ApiOperation(value = "安全班组会通知与用户关联表-批量删除", notes = "安全班组会通知与用户关联表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gscsInformUserService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "安全班组会通知与用户关联表-通过id查询")
    @ApiOperation(value = "安全班组会通知与用户关联表-通过id查询", notes = "安全班组会通知与用户关联表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GscsInformUser gscsInformUser = gscsInformUserService.getById(id);
        if (gscsInformUser == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gscsInformUser);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gscsInformUser
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GscsInformUser gscsInformUser) {
        return super.exportXls(request, gscsInformUser, GscsInformUser.class, "安全班组会通知与用户关联表");
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
        return super.importExcel(request, response, GscsInformUser.class);
    }

}
