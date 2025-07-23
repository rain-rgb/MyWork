package com.trtm.iot.wzcailiaonamedictall.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzcailiaonamedictall.entity.WzcailiaonamedictAll;
import com.trtm.iot.wzcailiaonamedictall.service.IWzcailiaonamedictAllService;

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

import static org.jeecg.common.util.DateUtils.getTimestampten;

/**
 * @Description: wzcailiaonamedict_all
 * @Author: jeecg-boot
 * @Date: 2023-09-06
 * @Version: V1.0
 */
@Api(tags = "wzcailiaonamedict_all")
@RestController
@RequestMapping("/wzcailiaonamedictall/wzcailiaonamedictAll")
@Slf4j
public class WzcailiaonamedictAllController extends JeecgController<WzcailiaonamedictAll, IWzcailiaonamedictAllService> {
    @Autowired
    private IWzcailiaonamedictAllService wzcailiaonamedictAllService;

    /**
     * 分页列表查询
     *
     * @param wzcailiaonamedictAll
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_all-分页列表查询")
    @ApiOperation(value = "wzcailiaonamedict_all-分页列表查询", notes = "wzcailiaonamedict_all-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WzcailiaonamedictAll wzcailiaonamedictAll,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WzcailiaonamedictAll> queryWrapper = QueryGenerator.initQueryWrapper(wzcailiaonamedictAll, req.getParameterMap());
        Page<WzcailiaonamedictAll> page = new Page<WzcailiaonamedictAll>(pageNo, pageSize);
        IPage<WzcailiaonamedictAll> pageList = wzcailiaonamedictAllService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "wzcailiaonamedict_all-库别查询")
    @ApiOperation(value = "wzcailiaonamedict_all-库别查询", notes = "wzcailiaonamedict_all-库别查询")
    @GetMapping(value = "/getKubie")
    public Result<?> getKubie(WzcailiaonamedictAll wzcailiaonamedictAll,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
        QueryWrapper<WzcailiaonamedictAll> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("parentNode");
        queryWrapper.groupBy("parentNode");
        List<Map<String, Object>> pageList = wzcailiaonamedictAllService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "wzcailiaonamedict_all-类别查询")
    @ApiOperation(value = "wzcailiaonamedict_all-类别查询", notes = "wzcailiaonamedict_all-类别查询")
    @GetMapping(value = "/getTypeNode")
    public Result<?> getTypeNode(WzcailiaonamedictAll wzcailiaonamedictAll,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 HttpServletRequest req) {
        QueryWrapper<WzcailiaonamedictAll> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("chandi");
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getParentnode()), "parentNode", wzcailiaonamedictAll.getParentnode());
        queryWrapper.groupBy("chandi");
        List<Map<String, Object>> pageList = wzcailiaonamedictAllService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "wzcailiaonamedict_all-材料名称查询")
    @ApiOperation(value = "wzcailiaonamedict_all-材料名称查询", notes = "wzcailiaonamedict_all-材料名称查询")
    @GetMapping(value = "/getCaiLiaoNameList")
    public Result<?> getCaiLiaoNameList(WzcailiaonamedictAll wzcailiaonamedictAll,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) {
        QueryWrapper<WzcailiaonamedictAll> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("cailiaoName");
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getParentnode()), "parentNode", wzcailiaonamedictAll.getParentnode());
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getChandi()), "chandi", wzcailiaonamedictAll.getChandi());
        queryWrapper.groupBy("cailiaoName");
        List<Map<String, Object>> pageList = wzcailiaonamedictAllService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "wzcailiaonamedict_all-型号查询")
    @ApiOperation(value = "wzcailiaonamedict_all-型号查询", notes = "wzcailiaonamedict_all-型号查询")
    @GetMapping(value = "/getXinghaoList")
    public Result<?> getXinghaoList(WzcailiaonamedictAll wzcailiaonamedictAll,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        QueryWrapper<WzcailiaonamedictAll> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("guigexinghao");
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getParentnode()), "parentNode", wzcailiaonamedictAll.getParentnode());
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getChandi()), "chandi", wzcailiaonamedictAll.getChandi());
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getCailiaoname()), "cailiaoname", wzcailiaonamedictAll.getCailiaoname());
        List<Map<String, Object>> pageList = wzcailiaonamedictAllService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "wzcailiaonamedict_all-规格查询")
    @ApiOperation(value = "wzcailiaonamedict_all-规格查询", notes = "wzcailiaonamedict_all-规格查询")
    @GetMapping(value = "/getGuigeList")
    public Result<?> getGuigeList(WzcailiaonamedictAll wzcailiaonamedictAll,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest req) {
        QueryWrapper<WzcailiaonamedictAll> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("zhengpiancha");
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getParentnode()), "parentNode", wzcailiaonamedictAll.getParentnode());
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getChandi()), "chandi", wzcailiaonamedictAll.getChandi());
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getCailiaoname()), "cailiaoname", wzcailiaonamedictAll.getCailiaoname());
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getGuigexinghao()), "guigexinghao", wzcailiaonamedictAll.getGuigexinghao());
        queryWrapper.groupBy("zhengpiancha");
        List<Map<String, Object>> pageList = wzcailiaonamedictAllService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "wzcailiaonamedict_all-辅助规格查询")
    @ApiOperation(value = "wzcailiaonamedict_all-辅助规格查询", notes = "wzcailiaonamedict_all-辅助规格查询")
    @GetMapping(value = "/getFuzhuGuigeList")
    public Result<?> getFuzhuGuigeList(WzcailiaonamedictAll wzcailiaonamedictAll,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        QueryWrapper<WzcailiaonamedictAll> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("miaoshu");
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getParentnode()), "parentNode", wzcailiaonamedictAll.getParentnode());
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getChandi()), "chandi", wzcailiaonamedictAll.getChandi());
        queryWrapper.eq(StringUtils.isNotBlank(wzcailiaonamedictAll.getCailiaoname()), "cailiaoname", wzcailiaonamedictAll.getCailiaoname());
        queryWrapper.groupBy("miaoshu");
        List<Map<String, Object>> pageList = wzcailiaonamedictAllService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wzcailiaonamedictAll
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_all-添加")
    @ApiOperation(value = "wzcailiaonamedict_all-添加", notes = "wzcailiaonamedict_all-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WzcailiaonamedictAll wzcailiaonamedictAll) {
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        wzcailiaonamedictAll.setGuid(uuid);
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        wzcailiaonamedictAll.setTs(ts);
        QueryWrapper<WzcailiaonamedictAll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cailiaoName",wzcailiaonamedictAll.getCailiaoname());
        queryWrapper.eq("guigexinghao",wzcailiaonamedictAll.getGuigexinghao());
        queryWrapper.eq("parentNode",wzcailiaonamedictAll.getNodetype());
        List<WzcailiaonamedictAll> list = wzcailiaonamedictAllService.list(queryWrapper);
        if(list.size()>0){
            return Result.error("材料已存在，请勿重复登记");
        }

        wzcailiaonamedictAllService.save(wzcailiaonamedictAll);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wzcailiaonamedictAll
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_all-编辑")
    @ApiOperation(value = "wzcailiaonamedict_all-编辑", notes = "wzcailiaonamedict_all-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WzcailiaonamedictAll wzcailiaonamedictAll) {
        wzcailiaonamedictAllService.updateById(wzcailiaonamedictAll);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_all-通过id删除")
    @ApiOperation(value = "wzcailiaonamedict_all-通过id删除", notes = "wzcailiaonamedict_all-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzcailiaonamedictAllService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_all-批量删除")
    @ApiOperation(value = "wzcailiaonamedict_all-批量删除", notes = "wzcailiaonamedict_all-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzcailiaonamedictAllService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_all-通过id查询")
    @ApiOperation(value = "wzcailiaonamedict_all-通过id查询", notes = "wzcailiaonamedict_all-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WzcailiaonamedictAll wzcailiaonamedictAll = wzcailiaonamedictAllService.getById(id);
        if (wzcailiaonamedictAll == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzcailiaonamedictAll);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzcailiaonamedictAll
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzcailiaonamedictAll wzcailiaonamedictAll) {
        return super.exportXls(request, wzcailiaonamedictAll, WzcailiaonamedictAll.class, "wzcailiaonamedict_all");
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
        return super.importExcel(request, response, WzcailiaonamedictAll.class);
    }

}
