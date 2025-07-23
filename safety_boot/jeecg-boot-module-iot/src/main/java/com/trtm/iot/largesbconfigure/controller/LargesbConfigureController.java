package com.trtm.iot.largesbconfigure.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.largesbconfigure.entity.LargesbConfigure;
import com.trtm.iot.largesbconfigure.service.ILargesbConfigureService;

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
 * @Description: largesb_configure
 * @Author: jeecg-boot
 * @Date: 2023-09-12
 * @Version: V1.0
 */
@Api(tags = "largesb_configure")
@RestController
@RequestMapping("/largesbconfigure/largesbConfigure")
@Slf4j
public class LargesbConfigureController extends JeecgController<LargesbConfigure, ILargesbConfigureService> {
    @Autowired
    private ILargesbConfigureService largesbConfigureService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;

    /**
     * 分页列表查询
     *
     * @param largesbConfigure
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "largesb_configure-分页列表查询")
    @ApiOperation(value = "largesb_configure-分页列表查询", notes = "largesb_configure-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LargesbConfigure largesbConfigure,
                                   String sys_depart_orgcode,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {
//            largesbConfigure.setSysOrgCode(sys_depart_orgcode + "*");
//        }
        QueryWrapper<LargesbConfigure> queryWrapper = QueryGenerator.initQueryWrapper(largesbConfigure, req.getParameterMap());
        Page<LargesbConfigure> page = new Page<LargesbConfigure>(pageNo, pageSize);
        IPage<LargesbConfigure> pageList = largesbConfigureService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param largesbConfigure
     * @return
     */
    @AutoLog(value = "largesb_configure-添加")
    @ApiOperation(value = "largesb_configure-添加", notes = "largesb_configure-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody LargesbConfigure largesbConfigure) {
        ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(largesbConfigure.getShebeiNo());
        largesbConfigure.setSblx(shebeiInfo.getSbtype());
        QueryWrapper<BhzPhone> bhzPhoneQueryWrapper = new QueryWrapper<>();
        bhzPhoneQueryWrapper.eq("uid", largesbConfigure.getNames());
        BhzPhone bhzPhone = bhzPhoneService.getOne(bhzPhoneQueryWrapper);
        if (ObjectUtil.isNotEmpty(bhzPhone)) {
            largesbConfigure.setNames(bhzPhone.getNames());
            largesbConfigure.setPhones(bhzPhone.getPhones());
        }
        largesbConfigureService.save(largesbConfigure);
        largesbConfigureService.saveDoorSbj(largesbConfigure.getSysOrgCode(),largesbConfigure.getShebeiNo(),largesbConfigure.getDoorSbjno());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param largesbConfigure
     * @return
     */
    @AutoLog(value = "largesb_configure-编辑")
    @ApiOperation(value = "largesb_configure-编辑", notes = "largesb_configure-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody LargesbConfigure largesbConfigure) {
        QueryWrapper<BhzPhone> bhzPhoneQueryWrapper = new QueryWrapper<>();
        bhzPhoneQueryWrapper.eq("uid", largesbConfigure.getNames());
        BhzPhone bhzPhone = bhzPhoneService.getOne(bhzPhoneQueryWrapper);
        if (ObjectUtil.isNotEmpty(bhzPhone)){
            largesbConfigure.setPhones(bhzPhone.getPhones());
            largesbConfigure.setNames(bhzPhone.getNames());
        }
        largesbConfigureService.updateById(largesbConfigure);
        largesbConfigureService.saveDoorSbj(largesbConfigure.getSysOrgCode(),largesbConfigure.getShebeiNo(),largesbConfigure.getDoorSbjno());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "largesb_configure-通过id删除")
    @ApiOperation(value = "largesb_configure-通过id删除", notes = "largesb_configure-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        largesbConfigureService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "largesb_configure-批量删除")
    @ApiOperation(value = "largesb_configure-批量删除", notes = "largesb_configure-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.largesbConfigureService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "largesb_configure-通过id查询")
    @ApiOperation(value = "largesb_configure-通过id查询", notes = "largesb_configure-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        LargesbConfigure largesbConfigure = largesbConfigureService.getById(id);
        if (largesbConfigure == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(largesbConfigure);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param largesbConfigure
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LargesbConfigure largesbConfigure) {
        return super.exportXls(request, largesbConfigure, LargesbConfigure.class, "largesb_configure");
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
        return super.importExcel(request, response, LargesbConfigure.class);
    }

}
