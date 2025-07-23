package com.trtm.iot.appmessage.controller;

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
import com.trtm.iot.appmessage.entity.Appmessage;
import com.trtm.iot.appmessage.service.IAppmessageService;

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
 * @Description: appmessage
 * @Author: jeecg-boot
 * @Date:   2021-12-08
 * @Version: V1.0
 */
@Api(tags="appmessage")
@RestController
@RequestMapping("/appmessage/appmessage")
@Slf4j
public class AppmessageController extends JeecgController<Appmessage, IAppmessageService> {
	@Autowired
	private IAppmessageService appmessageService;

	/**
	 * 分页列表查询
	 *
	 * @param appmessage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "appmessage-分页列表查询")
	@ApiOperation(value="appmessage-分页列表查询", notes="appmessage-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Appmessage appmessage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Appmessage> queryWrapper = QueryGenerator.initQueryWrapper(appmessage, req.getParameterMap());
		Page<Appmessage> page = new Page<Appmessage>(pageNo, pageSize);
		IPage<Appmessage> pageList = appmessageService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param appmessage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "appmessage-分页列表查询")
	 @ApiOperation(value="appmessage-分页列表查询", notes="appmessage-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(Appmessage appmessage,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<Appmessage> queryWrapper = QueryGenerator.initQueryWrapper(appmessage, req.getParameterMap());
		 Appmessage one = appmessageService.getOne(queryWrapper);
		 return Result.OK(one);
	 }

	/**
	 *   添加
	 *
	 * @param appmessage
	 * @return
	 */
	@AutoLog(value = "appmessage-添加")
	@ApiOperation(value="appmessage-添加", notes="appmessage-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Appmessage appmessage) {
		appmessageService.save(appmessage);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param appmessage
	 * @return
	 */
	@AutoLog(value = "appmessage-编辑")
	@ApiOperation(value="appmessage-编辑", notes="appmessage-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Appmessage appmessage) {
		appmessageService.updateById(appmessage);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "appmessage-通过id删除")
	@ApiOperation(value="appmessage-通过id删除", notes="appmessage-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		appmessageService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "appmessage-批量删除")
	@ApiOperation(value="appmessage-批量删除", notes="appmessage-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.appmessageService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "appmessage-通过id查询")
	@ApiOperation(value="appmessage-通过id查询", notes="appmessage-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Appmessage appmessage = appmessageService.getById(id);
		if(appmessage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(appmessage);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param appmessage
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Appmessage appmessage) {
        return super.exportXls(request, appmessage, Appmessage.class, "appmessage");
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
        return super.importExcel(request, response, Appmessage.class);
    }

}
