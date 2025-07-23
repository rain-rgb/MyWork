package com.trtm.iot.wbdestination.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wbdestination.entity.WbDestination;
import com.trtm.iot.wbdestination.service.IWbDestinationService;

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
 * @Description: 电子锁目的地数据信息
 * @Author: jeecg-boot
 * @Date: 2022-02-23
 * @Version: V1.0
 */
@Api(tags = "电子锁目的地数据信息")
@RestController
@RequestMapping("/wbdestination/wbDestination")
@Slf4j
public class WbDestinationController extends JeecgController<WbDestination, IWbDestinationService> {
    @Autowired
    private IWbDestinationService wbDestinationService;

    /**
     * 分页列表查询
     *
     * @param wbDestination
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁目的地数据信息-分页列表查询")
    @ApiOperation(value = "电子锁目的地数据信息-分页列表查询", notes = "电子锁目的地数据信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WbDestination wbDestination,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode == null) {
            wbDestination.setSysOrgCode(loginUser.getOrgCode() + "*");
        } else {
            wbDestination.setSysOrgCode(sys_depart_orgcode + "*");
        }
        if (wbDestination.getDepartname() != null) {
            wbDestination.setDepartname("*" + wbDestination.getDepartname() + "*");
        }
        QueryWrapper<WbDestination> queryWrapper = QueryGenerator.initQueryWrapper(wbDestination, req.getParameterMap());
        Page<WbDestination> page = new Page<WbDestination>(pageNo, pageSize);
        IPage<WbDestination> pageList = wbDestinationService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    @AutoLog(value = "电子锁目的地数据信息-分页列表查询")
    @ApiOperation(value = "电子锁目的地数据信息-分页列表查询", notes = "电子锁目的地数据信息-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(WbDestination wbDestination,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            wbDestination.setSysOrgCode(loginUser.getOrgCode() + "*");
        QueryWrapper<WbDestination> queryWrapper = QueryGenerator.initQueryWrapper(wbDestination, req.getParameterMap());
        List<WbDestination> list = wbDestinationService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param wbDestination
     * @return
     */
    @AutoLog(value = "电子锁目的地数据信息-添加")
    @ApiOperation(value = "电子锁目的地数据信息-添加", notes = "电子锁目的地数据信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WbDestination wbDestination) {
        wbDestinationService.save(wbDestination);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wbDestination
     * @return
     */
    @AutoLog(value = "电子锁目的地数据信息-编辑")
    @ApiOperation(value = "电子锁目的地数据信息-编辑", notes = "电子锁目的地数据信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WbDestination wbDestination) {
        wbDestinationService.updateById(wbDestination);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "电子锁目的地数据信息-通过id删除")
    @ApiOperation(value = "电子锁目的地数据信息-通过id删除", notes = "电子锁目的地数据信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wbDestinationService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "电子锁目的地数据信息-批量删除")
    @ApiOperation(value = "电子锁目的地数据信息-批量删除", notes = "电子锁目的地数据信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wbDestinationService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "电子锁目的地数据信息-通过id查询")
    @ApiOperation(value = "电子锁目的地数据信息-通过id查询", notes = "电子锁目的地数据信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WbDestination wbDestination = wbDestinationService.getById(id);
        if (wbDestination == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wbDestination);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wbDestination
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WbDestination wbDestination) {
        return super.exportXls(request, wbDestination, WbDestination.class, "电子锁目的地数据信息");
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
        return super.importExcel(request, response, WbDestination.class);
    }

}
