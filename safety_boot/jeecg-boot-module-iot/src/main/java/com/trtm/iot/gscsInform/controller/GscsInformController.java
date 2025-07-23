package com.trtm.iot.gscsInform.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.gscsInformUser.entity.GscsInformUser;
import com.trtm.iot.gscsInformUser.service.IGscsInformUserService;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.trtm.iot.gscsInform.entity.GscsInform;
import com.trtm.iot.gscsInform.service.IGscsInformService;

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
 * @Description: 班组安全管控系统通知详情表
 * @Author: jeecg-boot
 * @Date: 2022-01-25
 * @Version: V1.0
 */
@Api(tags = "班组安全管控系统通知详情表")
@RestController
@RequestMapping("/gscsInform/gscsInform")
@Slf4j
public class GscsInformController extends JeecgController<GscsInform, IGscsInformService> {
    @Autowired
    private IGscsInformService gscsInformService;
    @Autowired
    private IGscsInformUserService gscsInformUserService;

    /**
     * 分页列表查询
     *
     * @param gscsInform
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "班组安全管控系统通知详情表-分页列表查询")
    @ApiOperation(value = "班组安全管控系统通知详情表-分页列表查询", notes = "班组安全管控系统通知详情表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GscsInform gscsInform,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GscsInform> queryWrapper = QueryGenerator.initQueryWrapper(gscsInform, req.getParameterMap());
        Page<GscsInform> page = new Page<GscsInform>(pageNo, pageSize);
        IPage<GscsInform> pageList = gscsInformService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 获取考试次数接口
     *
     * @return
     */
    @AutoLog(value = "获取考试次数接口")
    @ApiOperation(value = "获取考试次数接口", notes = "获取考试次数接口")
    @GetMapping(value = "/getTestCount")
    public Result<?> getTestCount() {
        Integer testCount = gscsInformService.getTestCount();
        return Result.OK(testCount);
    }

    /**
     * 新增通知
     *
     * @param gscsInform
     * @return
     */
    @AutoLog(value = "新增通知")
    @ApiOperation(value = "新增通知", notes = "新增通知")
    @PostMapping(value = "/addInform")
    public Result<?> addInform(@RequestBody GscsInform gscsInform) {
        String informTitle = gscsInform.getInformTitle();
        Integer count = gscsInformService.selectCount(informTitle);
        if(count>0){
            return Result.error("通知标题已存在，请更换标题后下发");
        }
        gscsInformService.save(gscsInform);
        String informToList = gscsInform.getInformTo();
        String[] userList = informToList.split(",");
        String informId = gscsInformService.getIdByTitle(gscsInform.getInformTitle());
        //遍历接收通知人列表
        if (userList != null && userList.length > 0) {
            for (int i = 0; i < userList.length; i++) {
                String user = userList[i];
                GscsInformUser informUser = new GscsInformUser();
                informUser.setInformId(informId);
                informUser.setInformTo(user);
                gscsInformUserService.save(informUser);
            }
        }
        return Result.OK("通知下发成功！");
    }

    /**
     * 添加
     *
     * @param gscsInform
     * @return
     */
    @AutoLog(value = "班组安全管控系统通知详情表-添加")
    @ApiOperation(value = "班组安全管控系统通知详情表-添加", notes = "班组安全管控系统通知详情表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GscsInform gscsInform) {
        gscsInformService.save(gscsInform);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gscsInform
     * @return
     */
    @AutoLog(value = "班组安全管控系统通知详情表-编辑")
    @ApiOperation(value = "班组安全管控系统通知详情表-编辑", notes = "班组安全管控系统通知详情表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GscsInform gscsInform) {
        gscsInformService.updateById(gscsInform);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "班组安全管控系统通知详情表-通过id删除")
    @ApiOperation(value = "班组安全管控系统通知详情表-通过id删除", notes = "班组安全管控系统通知详情表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gscsInformService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "班组安全管控系统通知详情表-批量删除")
    @ApiOperation(value = "班组安全管控系统通知详情表-批量删除", notes = "班组安全管控系统通知详情表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gscsInformService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "班组安全管控系统通知详情表-通过id查询")
    @ApiOperation(value = "班组安全管控系统通知详情表-通过id查询", notes = "班组安全管控系统通知详情表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GscsInform gscsInform = gscsInformService.getById(id);
        if (gscsInform == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gscsInform);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gscsInform
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GscsInform gscsInform) {
        return super.exportXls(request, gscsInform, GscsInform.class, "班组安全管控系统通知详情表");
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
        return super.importExcel(request, response, GscsInform.class);
    }

}
