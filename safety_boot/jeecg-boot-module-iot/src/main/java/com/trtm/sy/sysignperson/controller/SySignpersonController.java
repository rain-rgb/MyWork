package com.trtm.iot.sysignperson.controller;

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
import com.trtm.iot.sysignperson.entity.SySignperson;
import com.trtm.iot.sysignperson.service.ISySignpersonService;

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
 * @Description: sy_signperson
 * @Author: jeecg-boot
 * @Date:   2023-09-22
 * @Version: V1.0
 */
@Api(tags="sy_signperson")
@RestController
@RequestMapping("/sysignperson/sySignperson")
@Slf4j
public class SySignpersonController extends JeecgController<SySignperson, ISySignpersonService> {
	@Autowired
	private ISySignpersonService sySignpersonService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sySignperson
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_signperson-分页列表查询")
	@ApiOperation(value="sy_signperson-分页列表查询", notes="sy_signperson-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SySignperson sySignperson,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SySignperson> queryWrapper = QueryGenerator.initQueryWrapper(sySignperson, req.getParameterMap());
		Page<SySignperson> page = new Page<SySignperson>(pageNo, pageSize);
		IPage<SySignperson> pageList = sySignpersonService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sySignperson
	 * @return
	 */
	@AutoLog(value = "sy_signperson-添加")
	@ApiOperation(value="sy_signperson-添加", notes="sy_signperson-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SySignperson sySignperson) {
		sySignpersonService.save(sySignperson);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sySignperson
	 * @return
	 */
	@AutoLog(value = "sy_signperson-编辑")
	@ApiOperation(value="sy_signperson-编辑", notes="sy_signperson-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SySignperson sySignperson) {
		sySignpersonService.updateById(sySignperson);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_signperson-通过id删除")
	@ApiOperation(value="sy_signperson-通过id删除", notes="sy_signperson-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sySignpersonService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_signperson-批量删除")
	@ApiOperation(value="sy_signperson-批量删除", notes="sy_signperson-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sySignpersonService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_signperson-通过id查询")
	@ApiOperation(value="sy_signperson-通过id查询", notes="sy_signperson-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SySignperson sySignperson = sySignpersonService.getById(id);
		if(sySignperson==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sySignperson);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sySignperson
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SySignperson sySignperson) {
        return super.exportXls(request, sySignperson, SySignperson.class, "sy_signperson");
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
        return super.importExcel(request, response, SySignperson.class);
    }

}
