package com.trtm.iot.rebarFactory.controller;

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
import com.trtm.iot.rebarFactory.entity.RebarFactory;
import com.trtm.iot.rebarFactory.service.IRebarFactoryService;

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
 * @Description: rebar_factory
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
@Api(tags="rebar_factory")
@RestController
@RequestMapping("/rebarFactory/rebarFactory")
@Slf4j
public class RebarFactoryController extends JeecgController<RebarFactory, IRebarFactoryService> {
	@Autowired
	private IRebarFactoryService rebarFactoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param rebarFactory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "rebar_factory-分页列表查询")
	@ApiOperation(value="rebar_factory-分页列表查询", notes="rebar_factory-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RebarFactory rebarFactory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RebarFactory> queryWrapper = QueryGenerator.initQueryWrapper(rebarFactory, req.getParameterMap());
		Page<RebarFactory> page = new Page<RebarFactory>(pageNo, pageSize);
		IPage<RebarFactory> pageList = rebarFactoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	
	/**
	 *   添加
	 *
	 * @param rebarFactory
	 * @return
	 */
	@AutoLog(value = "rebar_factory-添加")
	@ApiOperation(value="rebar_factory-添加", notes="rebar_factory-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RebarFactory rebarFactory) {
		rebarFactoryService.save(rebarFactory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param rebarFactory
	 * @return
	 */
	@AutoLog(value = "rebar_factory-编辑")
	@ApiOperation(value="rebar_factory-编辑", notes="rebar_factory-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RebarFactory rebarFactory) {
		rebarFactoryService.updateById(rebarFactory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_factory-通过id删除")
	@ApiOperation(value="rebar_factory-通过id删除", notes="rebar_factory-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		rebarFactoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "rebar_factory-批量删除")
	@ApiOperation(value="rebar_factory-批量删除", notes="rebar_factory-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.rebarFactoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_factory-通过id查询")
	@ApiOperation(value="rebar_factory-通过id查询", notes="rebar_factory-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RebarFactory rebarFactory = rebarFactoryService.getById(id);
		if(rebarFactory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(rebarFactory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param rebarFactory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RebarFactory rebarFactory) {
        return super.exportXls(request, rebarFactory, RebarFactory.class, "rebar_factory");
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
        return super.importExcel(request, response, RebarFactory.class);
    }

}
