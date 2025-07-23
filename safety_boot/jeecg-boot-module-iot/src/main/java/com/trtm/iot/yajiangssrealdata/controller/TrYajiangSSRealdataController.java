package com.trtm.iot.yajiangssrealdata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ArrayUtil;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.xkcoding.http.util.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.yajiangssrealdata.entity.TrYajiangSSRealdata;
import com.trtm.iot.yajiangssrealdata.service.ITrYajiangSSRealdataService;

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
 * @Description: tr_yajiang_s_s_realdata
 * @Author: jeecg-boot
 * @Date: 2023-05-11
 * @Version: V1.0
 */
@Api(tags = "tr_yajiang_s_s_realdata")
@RestController
@RequestMapping("/yajiangssrealdata/trYajiangSSRealdata")
@Slf4j
public class TrYajiangSSRealdataController extends JeecgController<TrYajiangSSRealdata, ITrYajiangSSRealdataService> {
    @Autowired
    private ITrYajiangSSRealdataService trYajiangSSRealdataService;

    /**
     * 分页列表查询
     *
     * @param trYajiangSSRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "tr_yajiang_s_s_realdata-分页列表查询")
    @ApiOperation(value = "tr_yajiang_s_s_realdata-分页列表查询", notes = "tr_yajiang_s_s_realdata-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrYajiangSSRealdata trYajiangSSRealdata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrYajiangSSRealdata> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangSSRealdata, req.getParameterMap());
        Page<TrYajiangSSRealdata> page = new Page<TrYajiangSSRealdata>(pageNo, pageSize);
        IPage<TrYajiangSSRealdata> pageList = trYajiangSSRealdataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trYajiangSSRealdata
     * @return
     */
    @AutoLog(value = "tr_yajiang_s_s_realdata-添加")
    @ApiOperation(value = "tr_yajiang_s_s_realdata-添加", notes = "tr_yajiang_s_s_realdata-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrYajiangSSRealdata trYajiangSSRealdata) {
        if (trYajiangSSRealdata.getShebeibianh() != null && trYajiangSSRealdata.getShebeibianh() != "") {
//            List<String> sbList = trYajiangSSRealdataService.getSbList();
            QueryWrapper<TrYajiangSSRealdata> yajiangSSRealdataQueryWrapper = new QueryWrapper<>();
            yajiangSSRealdataQueryWrapper.eq("shebeibianh",trYajiangSSRealdata.getShebeibianh());
            List<TrYajiangSSRealdata> list = trYajiangSSRealdataService.list(yajiangSSRealdataQueryWrapper);

            if (list.size()>0) {
                trYajiangSSRealdata.setSid(list.get(0).getSid());
                trYajiangSSRealdataService.updateById(trYajiangSSRealdata);
                return Result.OK("修改成功！");
            } else {
                trYajiangSSRealdataService.save(trYajiangSSRealdata);
                return Result.OK("添加成功！");
            }
        } else {
            return Result.error("设备编号为空，添加失败！");
        }
    }

    /**
     * 编辑
     *
     * @param trYajiangSSRealdata
     * @return
     */
    @AutoLog(value = "tr_yajiang_s_s_realdata-编辑")
    @ApiOperation(value = "tr_yajiang_s_s_realdata-编辑", notes = "tr_yajiang_s_s_realdata-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrYajiangSSRealdata trYajiangSSRealdata) {
        trYajiangSSRealdataService.updateById(trYajiangSSRealdata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "tr_yajiang_s_s_realdata-通过id删除")
    @ApiOperation(value = "tr_yajiang_s_s_realdata-通过id删除", notes = "tr_yajiang_s_s_realdata-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trYajiangSSRealdataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "tr_yajiang_s_s_realdata-批量删除")
    @ApiOperation(value = "tr_yajiang_s_s_realdata-批量删除", notes = "tr_yajiang_s_s_realdata-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trYajiangSSRealdataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "tr_yajiang_s_s_realdata-通过id查询")
    @ApiOperation(value = "tr_yajiang_s_s_realdata-通过id查询", notes = "tr_yajiang_s_s_realdata-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrYajiangSSRealdata trYajiangSSRealdata = trYajiangSSRealdataService.getById(id);
        if (trYajiangSSRealdata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trYajiangSSRealdata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trYajiangSSRealdata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrYajiangSSRealdata trYajiangSSRealdata) {
        return super.exportXls(request, trYajiangSSRealdata, TrYajiangSSRealdata.class, "tr_yajiang_s_s_realdata");
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
        return super.importExcel(request, response, TrYajiangSSRealdata.class);
    }

}
