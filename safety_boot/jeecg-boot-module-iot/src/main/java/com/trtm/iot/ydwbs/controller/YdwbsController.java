package com.trtm.iot.ydwbs.controller;

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
import com.trtm.iot.ydwbs.entity.Ydwbs;
import com.trtm.iot.ydwbs.service.IYdwbsService;

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
 * @Description: ydwbs
 * @Author: jeecg-boot
 * @Date:   2021-09-14
 * @Version: V1.0
 */
@Api(tags="ydwbs")
@RestController
@RequestMapping("/ydwbs/ydwbs")
@Slf4j
public class YdwbsController extends JeecgController<Ydwbs, IYdwbsService> {
	@Autowired
	private IYdwbsService ydwbsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ydwbs
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "ydwbs-分页列表查询")
	@ApiOperation(value="ydwbs-分页列表查询", notes="ydwbs-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Ydwbs ydwbs,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Ydwbs> queryWrapper = QueryGenerator.initQueryWrapper(ydwbs, req.getParameterMap());
		Page<Ydwbs> page = new Page<Ydwbs>(pageNo, pageSize);
		IPage<Ydwbs> pageList = ydwbsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ydwbs
	 * @return
	 */
	@AutoLog(value = "ydwbs-添加")
	@ApiOperation(value="ydwbs-添加", notes="ydwbs-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Ydwbs ydwbs) {
		ydwbsService.save(ydwbs);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ydwbs
	 * @return
	 */
	@AutoLog(value = "ydwbs-编辑")
	@ApiOperation(value="ydwbs-编辑", notes="ydwbs-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Ydwbs ydwbs) {
		ydwbsService.updateById(ydwbs);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ydwbs-通过id删除")
	@ApiOperation(value="ydwbs-通过id删除", notes="ydwbs-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ydwbsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ydwbs-批量删除")
	@ApiOperation(value="ydwbs-批量删除", notes="ydwbs-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ydwbsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ydwbs-通过id查询")
	@ApiOperation(value="ydwbs-通过id查询", notes="ydwbs-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Ydwbs ydwbs = ydwbsService.getById(id);
		if(ydwbs==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ydwbs);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ydwbs
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Ydwbs ydwbs) {
        return super.exportXls(request, ydwbs, Ydwbs.class, "ydwbs");
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
        return super.importExcel(request, response, Ydwbs.class);
    }

}
