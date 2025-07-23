package com.trtm.iot.wbsquality.controller;

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
import com.trtm.iot.wbsquality.entity.WbsQuality;
import com.trtm.iot.wbsquality.service.IWbsQualityService;

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
 * @Description: wbs_quality
 * @Author: jeecg-boot
 * @Date:   2024-11-27
 * @Version: V1.0
 */
@Api(tags="wbs_quality")
@RestController
@RequestMapping("/wbsquality/wbsQuality")
@Slf4j
public class WbsQualityController extends JeecgController<WbsQuality, IWbsQualityService> {
	@Autowired
	private IWbsQualityService wbsQualityService;

	/**
	 * 分页列表查询
	 *
	 * @param wbsQuality
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wbs_quality-分页列表查询")
	@ApiOperation(value="wbs_quality-分页列表查询", notes="wbs_quality-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WbsQuality wbsQuality,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WbsQuality> queryWrapper = QueryGenerator.initQueryWrapper(wbsQuality, req.getParameterMap());
		Page<WbsQuality> page = new Page<WbsQuality>(pageNo, pageSize);
		IPage<WbsQuality> pageList = wbsQualityService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param wbsQuality
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "wbs_quality-分页列表查询")
	 @ApiOperation(value="wbs_quality-分页列表查询", notes="wbs_quality-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(WbsQuality wbsQuality,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<WbsQuality> queryWrapper = QueryGenerator.initQueryWrapper(wbsQuality, req.getParameterMap());
		//  Page<WbsQuality> page = new Page<WbsQuality>(pageNo, pageSize);
		 List<WbsQuality> list = wbsQualityService.list(queryWrapper);
		 return Result.OK(list);
	 }

	/**
	 *   添加
	 *
	 * @param wbsQuality
	 * @return
	 */
	@AutoLog(value = "wbs_quality-添加")
	@ApiOperation(value="wbs_quality-添加", notes="wbs_quality-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WbsQuality wbsQuality) {
		wbsQualityService.save(wbsQuality);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wbsQuality
	 * @return
	 */
	@AutoLog(value = "wbs_quality-编辑")
	@ApiOperation(value="wbs_quality-编辑", notes="wbs_quality-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WbsQuality wbsQuality) {
		wbsQualityService.updateById(wbsQuality);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wbs_quality-通过id删除")
	@ApiOperation(value="wbs_quality-通过id删除", notes="wbs_quality-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wbsQualityService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wbs_quality-批量删除")
	@ApiOperation(value="wbs_quality-批量删除", notes="wbs_quality-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wbsQualityService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wbs_quality-通过id查询")
	@ApiOperation(value="wbs_quality-通过id查询", notes="wbs_quality-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WbsQuality wbsQuality = wbsQualityService.getById(id);
		if(wbsQuality==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wbsQuality);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wbsQuality
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WbsQuality wbsQuality) {
        return super.exportXls(request, wbsQuality, WbsQuality.class, "wbs_quality");
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
        return super.importExcel(request, response, WbsQuality.class);
    }

}
