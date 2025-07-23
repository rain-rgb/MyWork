package com.trtm.iot.inventory.controller;

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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.inventory.entity.Wzinventory;
import com.trtm.iot.inventory.service.IWzinventoryService;

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
 * @Description: wzinventory
 * @Author: jeecg-boot
 * @Date: 2023-10-20
 * @Version: V1.0
 */
@Api(tags = "wzinventory")
@RestController
@RequestMapping("/inventory/wzinventory")
@Slf4j
public class WzinventoryController extends JeecgController<Wzinventory, IWzinventoryService> {
    @Autowired
    private IWzinventoryService wzinventoryService;

    /**
     * 分页列表查询
     *
     * @param wzinventory
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzinventory-分页列表查询")
    @ApiOperation(value = "wzinventory-分页列表查询", notes = "wzinventory-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Wzinventory wzinventory,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        wzinventory.setWzName("*" + wzinventory.getWzName() + "*");
        QueryWrapper<Wzinventory> queryWrapper = QueryGenerator.initQueryWrapper(wzinventory, req.getParameterMap());
        Page<Wzinventory> page = new Page<Wzinventory>(pageNo, pageSize);
        IPage<Wzinventory> pageList = wzinventoryService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wzinventory
     * @return
     */
    @AutoLog(value = "wzinventory-添加")
    @ApiOperation(value = "wzinventory-添加", notes = "wzinventory-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Wzinventory wzinventory) {
        if (ObjectUtil.isEmpty(wzinventory.getDanwei())) {
            wzinventory.setDanwei("t");
        }
        wzinventoryService.save(wzinventory);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wzinventory
     * @return
     */
    @AutoLog(value = "wzinventory-编辑")
    @ApiOperation(value = "wzinventory-编辑", notes = "wzinventory-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Wzinventory wzinventory) {
        wzinventoryService.updateById(wzinventory);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzinventory-通过id删除")
    @ApiOperation(value = "wzinventory-通过id删除", notes = "wzinventory-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzinventoryService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wzinventory-批量删除")
    @ApiOperation(value = "wzinventory-批量删除", notes = "wzinventory-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzinventoryService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzinventory-通过id查询")
    @ApiOperation(value = "wzinventory-通过id查询", notes = "wzinventory-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Wzinventory wzinventory = wzinventoryService.getById(id);
        if (wzinventory == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzinventory);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzinventory
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzinventory wzinventory) {
        return super.exportXls(request, wzinventory, Wzinventory.class, "物资盘点登记记录");
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
        return super.importExcel(request, response, Wzinventory.class);
    }

}
