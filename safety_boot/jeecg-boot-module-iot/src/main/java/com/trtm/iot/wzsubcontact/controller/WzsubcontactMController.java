package com.trtm.iot.wzsubcontact.controller;

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
import com.trtm.iot.wzcontact.entity.WzcontactM;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzsubcontact.entity.WzsubcontactM;
import com.trtm.iot.wzsubcontact.service.IWzsubcontactMService;

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
 * @Description: wzsubcontact_m
 * @Author: jeecg-boot
 * @Date: 2023-10-16
 * @Version: V1.0
 */
@Api(tags = "wzsubcontact_m")
@RestController
@RequestMapping("/wzsubcontact/wzsubcontactM")
@Slf4j
public class WzsubcontactMController extends JeecgController<WzsubcontactM, IWzsubcontactMService> {
    @Autowired
    private IWzsubcontactMService wzsubcontactMService;

    /**
     * 分页列表查询
     *
     * @param wzsubcontactM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzsubcontact_m-分页列表查询")
    @ApiOperation(value = "wzsubcontact_m-分页列表查询", notes = "wzsubcontact_m-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WzsubcontactM wzsubcontactM,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WzsubcontactM> queryWrapper = QueryGenerator.initQueryWrapper(wzsubcontactM, req.getParameterMap());
        Page<WzsubcontactM> page = new Page<WzsubcontactM>(pageNo, pageSize);
        IPage<WzsubcontactM> pageList = wzsubcontactMService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wzsubcontactM
     * @return
     */
    @AutoLog(value = "wzsubcontact_m-添加")
    @ApiOperation(value = "wzsubcontact_m-添加", notes = "wzsubcontact_m-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WzsubcontactM wzsubcontactM) {
        wzsubcontactMService.save(wzsubcontactM);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wzsubcontactM
     * @return
     */
    @AutoLog(value = "wzsubcontact_m-编辑")
    @ApiOperation(value = "wzsubcontact_m-编辑", notes = "wzsubcontact_m-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WzsubcontactM wzsubcontactM) {
        wzsubcontactMService.updateById(wzsubcontactM);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzsubcontact_m-通过id删除")
    @ApiOperation(value = "wzsubcontact_m-通过id删除", notes = "wzsubcontact_m-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzsubcontactMService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wzsubcontact_m-批量删除")
    @ApiOperation(value = "wzsubcontact_m-批量删除", notes = "wzsubcontact_m-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzsubcontactMService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzsubcontact_m-通过id查询")
    @ApiOperation(value = "wzsubcontact_m-通过id查询", notes = "wzsubcontact_m-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WzsubcontactM wzsubcontactM = wzsubcontactMService.getById(id);
        if (wzsubcontactM == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzsubcontactM);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzsubcontactM
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(WzsubcontactM wzsubcontactM,
                                  @RequestParam(value = "signingDate_begin", required = false) String start,
                                  @RequestParam(value = "signingDate_end", required = false) String end,
                                  HttpServletRequest request) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<WzsubcontactM> queryWrapper = QueryGenerator.initQueryWrapper(wzsubcontactM, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //Step.2 获取导出数据
        List<WzsubcontactM> queryList = wzsubcontactMService.list(queryWrapper);
        if (ObjectUtil.isEmpty(end) || "undefined".equals(end)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end = simpleDateFormat.format(date);
        }
        // 过滤选中数据
        String selections = request.getParameter("selections");
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "分包方合同管理台账数据列表");
        mv.addObject(NormalExcelConstants.CLASS, WzsubcontactM.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("分包方合同管理台账数据列表", "时间范围:" + start + "~" + end, "分包方合同管理台账"));
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
        return super.importExcel(request, response, WzsubcontactM.class);
    }

}
