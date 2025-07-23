package com.trtm.iot.zhanglassrealdata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangssrealdata.entity.TrYajiangSSRealdata;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zhanglassrealdata.entity.TrZhanglaSSRealdata;
import com.trtm.iot.zhanglassrealdata.service.ITrZhanglaSSRealdataService;

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
 * @Description: tr_zhangla_s_s_realdata
 * @Author: jeecg-boot
 * @Date: 2023-05-12
 * @Version: V1.0
 */
@Api(tags = "tr_zhangla_s_s_realdata")
@RestController
@RequestMapping("/zhanglassrealdata/trZhanglaSSRealdata")
@Slf4j
public class TrZhanglaSSRealdataController extends JeecgController<TrZhanglaSSRealdata, ITrZhanglaSSRealdataService> {
    @Autowired
    private ITrZhanglaSSRealdataService trZhanglaSSRealdataService;

    /**
     * 分页列表查询
     *
     * @param trZhanglaSSRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "tr_zhangla_s_s_realdata-分页列表查询")
    @ApiOperation(value = "tr_zhangla_s_s_realdata-分页列表查询", notes = "tr_zhangla_s_s_realdata-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrZhanglaSSRealdata trZhanglaSSRealdata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrZhanglaSSRealdata> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaSSRealdata, req.getParameterMap());
        Page<TrZhanglaSSRealdata> page = new Page<TrZhanglaSSRealdata>(pageNo, pageSize);
        IPage<TrZhanglaSSRealdata> pageList = trZhanglaSSRealdataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trZhanglaSSRealdata
     * @return
     */
    @AutoLog(value = "tr_zhangla_s_s_realdata-添加")
    @ApiOperation(value = "tr_zhangla_s_s_realdata-添加", notes = "tr_zhangla_s_s_realdata-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrZhanglaSSRealdata trZhanglaSSRealdata) {
        if (trZhanglaSSRealdata.getShebeibianhao() != null && !"".equals(trZhanglaSSRealdata.getShebeibianhao())) {
//            List<String> sbList = trZhanglaSSRealdataService.getSbList();
            QueryWrapper<TrZhanglaSSRealdata> ssRealdataQueryWrapper = new QueryWrapper<>();
            ssRealdataQueryWrapper.eq("shebeibianhao",trZhanglaSSRealdata.getShebeibianhao());
            List<TrZhanglaSSRealdata> list = trZhanglaSSRealdataService.list(ssRealdataQueryWrapper);

            if (list.size()>0){
                trZhanglaSSRealdata.setId(list.get(0).getId());
                trZhanglaSSRealdataService.updateById(trZhanglaSSRealdata);
                return Result.OK("修改成功!");
            } else {
                trZhanglaSSRealdataService.save(trZhanglaSSRealdata);
                return Result.OK("添加成功!");
            }
        } else {
            return Result.error("设备编号为空，添加失败!");
        }
    }

    /**
     * 编辑
     *
     * @param trZhanglaSSRealdata
     * @return
     */
    @AutoLog(value = "tr_zhangla_s_s_realdata-编辑")
    @ApiOperation(value = "tr_zhangla_s_s_realdata-编辑", notes = "tr_zhangla_s_s_realdata-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrZhanglaSSRealdata trZhanglaSSRealdata) {
        trZhanglaSSRealdataService.updateById(trZhanglaSSRealdata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "tr_zhangla_s_s_realdata-通过id删除")
    @ApiOperation(value = "tr_zhangla_s_s_realdata-通过id删除", notes = "tr_zhangla_s_s_realdata-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trZhanglaSSRealdataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "tr_zhangla_s_s_realdata-批量删除")
    @ApiOperation(value = "tr_zhangla_s_s_realdata-批量删除", notes = "tr_zhangla_s_s_realdata-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trZhanglaSSRealdataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "tr_zhangla_s_s_realdata-通过id查询")
    @ApiOperation(value = "tr_zhangla_s_s_realdata-通过id查询", notes = "tr_zhangla_s_s_realdata-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrZhanglaSSRealdata trZhanglaSSRealdata = trZhanglaSSRealdataService.getById(id);
        if (trZhanglaSSRealdata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trZhanglaSSRealdata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trZhanglaSSRealdata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrZhanglaSSRealdata trZhanglaSSRealdata) {
        return super.exportXls(request, trZhanglaSSRealdata, TrZhanglaSSRealdata.class, "tr_zhangla_s_s_realdata");
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
        return super.importExcel(request, response, TrZhanglaSSRealdata.class);
    }

}
