package com.trtm.iot.syLeixing.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.syLeixing.entity.SyLeixingTreeModel;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.syLeixing.entity.SyLeixing;
import com.trtm.iot.syLeixing.service.ISyLeixingService;

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
 * @Description: 试验类型
 * @Author: jeecg-boot
 * @Date: 2022-03-09
 * @Version: V1.0
 */
@Api(tags = "试验类型")
@RestController
@RequestMapping("/syLeixing/syLeixing")
@Slf4j
public class SyLeixingController extends JeecgController<SyLeixing, ISyLeixingService> {
    @Autowired
    private ISyLeixingService syLeixingService;

    /**
     * 分页列表查询
     *
     * @param syLeixing
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "试验类型-分页列表查询")
    @ApiOperation(value = "试验类型-分页列表查询", notes = "试验类型-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyLeixing syLeixing,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyLeixing> queryWrapper = QueryGenerator.initQueryWrapper(syLeixing, req.getParameterMap());
        Page<SyLeixing> page = new Page<SyLeixing>(pageNo, pageSize);
        IPage<SyLeixing> pageList = syLeixingService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syLeixing
     * @return
     */
    @AutoLog(value = "试验类型-添加")
    @ApiOperation(value = "试验类型-添加", notes = "试验类型-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyLeixing syLeixing) {
        syLeixingService.modifyStorageObject(syLeixing);
        boolean save = syLeixingService.save(syLeixing);
        if (save) {
            return  Result.OK("添加成功！");
        }else {
            return Result.error("添加失败！");
        }
    }

    /**
     * 编辑
     *
     * @param syLeixing
     * @return
     */
    @AutoLog(value = "试验类型-编辑")
    @ApiOperation(value = "试验类型-编辑", notes = "试验类型-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyLeixing syLeixing) {
        syLeixingService.updateById(syLeixing);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "试验类型-通过id删除")
    @ApiOperation(value = "试验类型-通过id删除", notes = "试验类型-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syLeixingService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "试验类型-批量删除")
    @ApiOperation(value = "试验类型-批量删除", notes = "试验类型-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syLeixingService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "试验类型-通过id查询")
    @ApiOperation(value = "试验类型-通过id查询", notes = "试验类型-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyLeixing syLeixing = syLeixingService.getById(id);
        if (syLeixing == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syLeixing);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syLeixing
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyLeixing syLeixing) {
        return super.exportXls(request, syLeixing, SyLeixing.class, "试验类型");
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
        return super.importExcel(request, response, SyLeixing.class);
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public List queryTreeList() {
        QueryWrapper<SyLeixing> queryWrapper = new QueryWrapper();
        queryWrapper.select("cailiaoname,cailiaono");
        queryWrapper.eq("isdel", 0);
        queryWrapper.eq("nodetype", 0);
        List<Map<String, Object>> list = syLeixingService.listMaps(queryWrapper);
        for (int i = 0; i < list.size(); i++) {
            Object cailiaono = list.get(i).get("cailiaono");
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.select("cailiaoname,cailiaono");
            queryWrapper1.eq("isdel", 0);
            queryWrapper1.eq("parentno", cailiaono);
            List<Map<String, Object>> list1 = syLeixingService.listMaps(queryWrapper1);
            List list2 = new ArrayList();
            for (Map map1:list1){
                list2.add(map1);
            }
            list.get(i).put("list",list2);
        }
            return list;
    }
}
