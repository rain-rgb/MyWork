package com.trtm.iot.largesbdoorswitch.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.largesbdoorswitch.entity.LargesbDoorSwitch;
import com.trtm.iot.largesbdoorswitch.service.ILargesbDoorSwitchService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: largesb_door_switch
 * @Author: jeecg-boot
 * @Date: 2023-12-26
 * @Version: V1.0
 */
@Api(tags = "largesb_door_switch")
@RestController
@RequestMapping("/largesbdoorswitch/largesbDoorSwitch")
@Slf4j
public class LargesbDoorSwitchController extends JeecgController<LargesbDoorSwitch, ILargesbDoorSwitchService> {
    @Autowired
    private ILargesbDoorSwitchService largesbDoorSwitchService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    /**
     * 分页列表查询
     *
     * @param largesbDoorSwitch
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "largesb_door_switch-分页列表查询")
    @ApiOperation(value = "largesb_door_switch-分页列表查询", notes = "largesb_door_switch-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LargesbDoorSwitch largesbDoorSwitch,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<LargesbDoorSwitch> queryWrapper = QueryGenerator.initQueryWrapper(largesbDoorSwitch, req.getParameterMap());
        Page<LargesbDoorSwitch> page = new Page<LargesbDoorSwitch>(pageNo, pageSize);
        IPage<LargesbDoorSwitch> pageList = largesbDoorSwitchService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param largesbDoorSwitch
     * @return
     */
    @AutoLog(value = "largesb_door_switch-添加")
    @ApiOperation(value = "largesb_door_switch-添加", notes = "largesb_door_switch-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody LargesbDoorSwitch largesbDoorSwitch) {
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(largesbDoorSwitch.getSbjNo());
        if (ObjectUtil.isNotEmpty(selectshebeione)) {
            largesbDoorSwitch.setSysOrgCode(selectshebeione.getSysOrgCode());
        }
        largesbDoorSwitchService.save(largesbDoorSwitch);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param largesbDoorSwitch
     * @return
     */
    @AutoLog(value = "largesb_door_switch-编辑")
    @ApiOperation(value = "largesb_door_switch-编辑", notes = "largesb_door_switch-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody LargesbDoorSwitch largesbDoorSwitch) {
        largesbDoorSwitchService.updateById(largesbDoorSwitch);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "largesb_door_switch-通过id删除")
    @ApiOperation(value = "largesb_door_switch-通过id删除", notes = "largesb_door_switch-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        largesbDoorSwitchService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "largesb_door_switch-批量删除")
    @ApiOperation(value = "largesb_door_switch-批量删除", notes = "largesb_door_switch-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.largesbDoorSwitchService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "largesb_door_switch-通过id查询")
    @ApiOperation(value = "largesb_door_switch-通过id查询", notes = "largesb_door_switch-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        LargesbDoorSwitch largesbDoorSwitch = largesbDoorSwitchService.getById(id);
        if (largesbDoorSwitch == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(largesbDoorSwitch);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param largesbDoorSwitch
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LargesbDoorSwitch largesbDoorSwitch) {
        return super.exportXls(request, largesbDoorSwitch, LargesbDoorSwitch.class, "largesb_door_switch");
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
        return super.importExcel(request, response, LargesbDoorSwitch.class);
    }

}
