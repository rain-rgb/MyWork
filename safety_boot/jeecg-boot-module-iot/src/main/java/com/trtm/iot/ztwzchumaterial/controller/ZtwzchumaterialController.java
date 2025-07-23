package com.trtm.iot.ztwzchumaterial.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.ztwzchumaterial.entity.Ztwzchumaterial;
import com.trtm.iot.ztwzchumaterial.service.IZtwzchumaterialService;

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
 * @Description: 中铁出厂子表
 * @Author: jeecg-boot
 * @Date:   2024-04-07
 * @Version: V1.0
 */
@Api(tags="中铁出厂子表")
@RestController
@RequestMapping("/ztwzchumaterial/ztwzchumaterial")
@Slf4j
public class ZtwzchumaterialController extends JeecgController<Ztwzchumaterial, IZtwzchumaterialService> {
	@Autowired
	private IZtwzchumaterialService ztwzchumaterialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ztwzchumaterial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "中铁出厂子表-分页列表查询")
	@ApiOperation(value="中铁出厂子表-分页列表查询", notes="中铁出厂子表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Ztwzchumaterial ztwzchumaterial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Ztwzchumaterial> queryWrapper = QueryGenerator.initQueryWrapper(ztwzchumaterial, req.getParameterMap());
		Page<Ztwzchumaterial> page = new Page<Ztwzchumaterial>(pageNo, pageSize);
		IPage<Ztwzchumaterial> pageList = ztwzchumaterialService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ztwzchumaterial
	 * @return
	 */
	@AutoLog(value = "中铁出厂子表-添加")
	@ApiOperation(value="中铁出厂子表-添加", notes="中铁出厂子表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Ztwzchumaterial ztwzchumaterial) {
		ztwzchumaterialService.save(ztwzchumaterial);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ztwzchumaterial
	 * @return
	 */
	@AutoLog(value = "中铁出厂子表-编辑")
	@ApiOperation(value="中铁出厂子表-编辑", notes="中铁出厂子表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Ztwzchumaterial ztwzchumaterial) {
		ztwzchumaterialService.updateById(ztwzchumaterial);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "中铁出厂子表-通过id删除")
	@ApiOperation(value="中铁出厂子表-通过id删除", notes="中铁出厂子表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ztwzchumaterialService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "中铁出厂子表-批量删除")
	@ApiOperation(value="中铁出厂子表-批量删除", notes="中铁出厂子表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ztwzchumaterialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "中铁出厂子表-通过id查询")
	@ApiOperation(value="中铁出厂子表-通过id查询", notes="中铁出厂子表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Ztwzchumaterial ztwzchumaterial = ztwzchumaterialService.getById(id);
		if(ztwzchumaterial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ztwzchumaterial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ztwzchumaterial
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Ztwzchumaterial ztwzchumaterial) {
        return super.exportXls(request, ztwzchumaterial, Ztwzchumaterial.class, "中铁出厂子表");
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
        return super.importExcel(request, response, Ztwzchumaterial.class);
    }

}
