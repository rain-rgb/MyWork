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
import com.trtm.iot.inventory.entity.WzinventoryTaizhang;
import com.trtm.iot.inventory.service.IWzinventoryTaizhangService;

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
 * @Description: wzinventory_taizhang
 * @Author: jeecg-boot
 * @Date: 2023-10-20
 * @Version: V1.0
 */
@Api(tags = "wzinventory_taizhang")
@RestController
@RequestMapping("/inventory/wzinventoryTaizhang")
@Slf4j
public class WzinventoryTaizhangController extends JeecgController<WzinventoryTaizhang, IWzinventoryTaizhangService> {
    @Autowired
    private IWzinventoryTaizhangService wzinventoryTaizhangService;

    /**
     * 分页列表查询
     *
     * @param wzinventoryTaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzinventory_taizhang-分页列表查询")
    @ApiOperation(value = "wzinventory_taizhang-分页列表查询", notes = "wzinventory_taizhang-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WzinventoryTaizhang wzinventoryTaizhang,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        wzinventoryTaizhang.setWzName("*" + wzinventoryTaizhang.getWzName() + "*");
        QueryWrapper<WzinventoryTaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wzinventoryTaizhang, req.getParameterMap());
        Page<WzinventoryTaizhang> page = new Page<WzinventoryTaizhang>(pageNo, pageSize);
        IPage<WzinventoryTaizhang> pageList = wzinventoryTaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wzinventoryTaizhang
     * @return
     */
    @AutoLog(value = "wzinventory_taizhang-添加")
    @ApiOperation(value = "wzinventory_taizhang-添加", notes = "wzinventory_taizhang-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WzinventoryTaizhang wzinventoryTaizhang) {
        if (ObjectUtil.isEmpty(wzinventoryTaizhang.getDanwei())) {
            wzinventoryTaizhang.setDanwei("吨");
        }
        wzinventoryTaizhangService.save(wzinventoryTaizhang);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wzinventoryTaizhang
     * @return
     */
    @AutoLog(value = "wzinventory_taizhang-编辑")
    @ApiOperation(value = "wzinventory_taizhang-编辑", notes = "wzinventory_taizhang-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WzinventoryTaizhang wzinventoryTaizhang) {
        wzinventoryTaizhangService.updateById(wzinventoryTaizhang);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzinventory_taizhang-通过id删除")
    @ApiOperation(value = "wzinventory_taizhang-通过id删除", notes = "wzinventory_taizhang-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzinventoryTaizhangService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wzinventory_taizhang-批量删除")
    @ApiOperation(value = "wzinventory_taizhang-批量删除", notes = "wzinventory_taizhang-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzinventoryTaizhangService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzinventory_taizhang-通过id查询")
    @ApiOperation(value = "wzinventory_taizhang-通过id查询", notes = "wzinventory_taizhang-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WzinventoryTaizhang wzinventoryTaizhang = wzinventoryTaizhangService.getById(id);
        if (wzinventoryTaizhang == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzinventoryTaizhang);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzinventoryTaizhang
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzinventoryTaizhang wzinventoryTaizhang) {
        return super.exportXls(request, wzinventoryTaizhang, WzinventoryTaizhang.class, "物资盘点台账");
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
        return super.importExcel(request, response, WzinventoryTaizhang.class);
    }

}
