package com.trtm.iot.bhzlqshaifenshiyan.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.lqbhz.vo.BhzlqBaseCailiao;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzlqshaifenshiyan.entity.BhzLqShaifenShiyan;
import com.trtm.iot.bhzlqshaifenshiyan.service.IBhzLqShaifenShiyanService;

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
 * @Description: 沥青筛分试验数据信息表
 * @Author: jeecg-boot
 * @Date: 2022-05-16
 * @Version: V1.0
 */
@Api(tags = "沥青筛分试验数据信息表")
@RestController
@RequestMapping("/bhzlqshaifenshiyan/bhzLqShaifenShiyan")
@Slf4j
public class BhzLqShaifenShiyanController extends JeecgController<BhzLqShaifenShiyan, IBhzLqShaifenShiyanService> {
    @Autowired
    private IBhzLqShaifenShiyanService bhzLqShaifenShiyanService;

    /**
     * 分页列表查询
     *
     * @param bhzLqShaifenShiyan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "沥青筛分试验数据信息表-分页列表查询")
    @ApiOperation(value = "沥青筛分试验数据信息表-分页列表查询", notes = "沥青筛分试验数据信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzLqShaifenShiyan bhzLqShaifenShiyan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BhzLqShaifenShiyan> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqShaifenShiyan, req.getParameterMap());
        Page<BhzLqShaifenShiyan> page = new Page<BhzLqShaifenShiyan>(pageNo, pageSize);
        IPage<BhzLqShaifenShiyan> pageList = bhzLqShaifenShiyanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
    /**
     * 添加
     *
     * @param bhzLqShaifenShiyan
     * @return
     */
    @AutoLog(value = "沥青筛分试验数据信息表-添加")
    @ApiOperation(value = "沥青筛分试验数据信息表-添加", notes = "沥青筛分试验数据信息表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzLqShaifenShiyan bhzLqShaifenShiyan) {
        bhzLqShaifenShiyanService.save(bhzLqShaifenShiyan);
        return Result.OK("添加成功！");
    }
    /**
     * 编辑
     *
     * @param bhzLqShaifenShiyan
     * @return
     */
    @AutoLog(value = "沥青筛分试验数据信息表-编辑")
    @ApiOperation(value = "沥青筛分试验数据信息表-编辑", notes = "沥青筛分试验数据信息表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzLqShaifenShiyan bhzLqShaifenShiyan) {
        bhzLqShaifenShiyanService.updateById(bhzLqShaifenShiyan);
        return Result.OK("编辑成功!");
    }
    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "沥青筛分试验数据信息表-通过id删除")
    @ApiOperation(value = "沥青筛分试验数据信息表-通过id删除", notes = "沥青筛分试验数据信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzLqShaifenShiyanService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "沥青筛分试验数据信息表-批量删除")
    @ApiOperation(value = "沥青筛分试验数据信息表-批量删除", notes = "沥青筛分试验数据信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzLqShaifenShiyanService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "沥青筛分试验数据信息表-通过id查询")
    @ApiOperation(value = "沥青筛分试验数据信息表-通过id查询", notes = "沥青筛分试验数据信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzLqShaifenShiyan bhzLqShaifenShiyan = bhzLqShaifenShiyanService.getById(id);
        if (bhzLqShaifenShiyan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzLqShaifenShiyan);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzLqShaifenShiyan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzLqShaifenShiyan bhzLqShaifenShiyan) {
        return super.exportXls(request, bhzLqShaifenShiyan, BhzLqShaifenShiyan.class, "沥青筛分试验数据信息表");
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
        return super.importExcel(request, response, BhzLqShaifenShiyan.class);
    }

}
