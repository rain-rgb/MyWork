package com.trtm.iot.wzcontact.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.system.entity.ShebeiInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzcontact.entity.WzcontactM;
import com.trtm.iot.wzcontact.service.IWzcontactMService;

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
 * @Description: wzcontact_m
 * @Author: jeecg-boot
 * @Date: 2023-10-09
 * @Version: V1.0
 */
@Api(tags = "wzcontact_m")
@RestController
@RequestMapping("/wzcontact/wzcontactM")
@Slf4j
public class WzcontactMController extends JeecgController<WzcontactM, IWzcontactMService> {
    @Autowired
    private IWzcontactMService wzcontactMService;

    /**
     * 分页列表查询
     *
     * @param wzcontactM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzcontact_m-分页列表查询")
    @ApiOperation(value = "wzcontact_m-分页列表查询", notes = "wzcontact_m-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WzcontactM wzcontactM,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WzcontactM> queryWrapper = QueryGenerator.initQueryWrapper(wzcontactM, req.getParameterMap());
        Page<WzcontactM> page = new Page<WzcontactM>(pageNo, pageSize);
        IPage<WzcontactM> pageList = wzcontactMService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wzcontactM
     * @return
     */
    @AutoLog(value = "wzcontact_m-添加")
    @ApiOperation(value = "wzcontact_m-添加", notes = "wzcontact_m-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WzcontactM wzcontactM) {
        wzcontactMService.save(wzcontactM);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wzcontactM
     * @return
     */
    @AutoLog(value = "wzcontact_m-编辑")
    @ApiOperation(value = "wzcontact_m-编辑", notes = "wzcontact_m-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WzcontactM wzcontactM) {
        wzcontactMService.updateById(wzcontactM);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzcontact_m-通过id删除")
    @ApiOperation(value = "wzcontact_m-通过id删除", notes = "wzcontact_m-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzcontactMService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wzcontact_m-批量删除")
    @ApiOperation(value = "wzcontact_m-批量删除", notes = "wzcontact_m-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzcontactMService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzcontact_m-通过id查询")
    @ApiOperation(value = "wzcontact_m-通过id查询", notes = "wzcontact_m-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WzcontactM wzcontactM = wzcontactMService.getById(id);
        if (wzcontactM == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzcontactM);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzcontactM
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(WzcontactM wzcontactM,
                                  @RequestParam(value = "signingDate_begin", required = false) String start,
                                  @RequestParam(value = "signingDate_end", required = false) String end,
                                  HttpServletRequest request) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<WzcontactM> queryWrapper = QueryGenerator.initQueryWrapper(wzcontactM, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //Step.2 获取导出数据
        List<WzcontactM> queryList = wzcontactMService.list(queryWrapper);
        if (ObjectUtil.isEmpty(end) || "undefined".equals(end)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end = simpleDateFormat.format(date);
        }
        // 过滤选中数据
        String selections = request.getParameter("selections");
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "供应方合同管理台账数据列表");
        mv.addObject(NormalExcelConstants.CLASS, WzcontactM.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应方合同管理台账数据列表", "时间范围:" + start + "~" + end, "供应方合同管理台账"));
        mv.addObject(NormalExcelConstants.DATA_LIST, queryList);
        return mv;
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
        return super.importExcel(request, response, WzcontactM.class);
    }

}
