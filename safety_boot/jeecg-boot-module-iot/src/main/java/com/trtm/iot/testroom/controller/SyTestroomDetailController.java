package com.trtm.iot.testroom.controller;

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
import com.trtm.iot.testroom.entity.SyTestroomDetail;
import com.trtm.iot.testroom.service.ISyTestroomDetailService;

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
 * @Description: sy_testroom_detail
 * @Author: jeecg-boot
 * @Date:   2024-09-24
 * @Version: V1.0
 */
@Api(tags="sy_testroom_detail")
@RestController
@RequestMapping("/testroom/syTestroomDetail")
@Slf4j
public class SyTestroomDetailController extends JeecgController<SyTestroomDetail, ISyTestroomDetailService> {
	@Autowired
	private ISyTestroomDetailService syTestroomDetailService;

	/**
	 * 分页列表查询
	 *
	 * @param syTestroomDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_testroom_detail-分页列表查询")
	@ApiOperation(value="sy_testroom_detail-分页列表查询", notes="sy_testroom_detail-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyTestroomDetail syTestroomDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyTestroomDetail> queryWrapper = QueryGenerator.initQueryWrapper(syTestroomDetail, req.getParameterMap());
		Page<SyTestroomDetail> page = new Page<SyTestroomDetail>(pageNo, pageSize);
		IPage<SyTestroomDetail> pageList = syTestroomDetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 /**
	  * 分页列表查询
	  *
	  * @param syTestroomDetail
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "sy_testroom_detail-分页列表查询")
	 @ApiOperation(value="sy_testroom_detail-分页列表查询", notes="sy_testroom_detail-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(SyTestroomDetail syTestroomDetail,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<SyTestroomDetail> queryWrapper = QueryGenerator.initQueryWrapper(syTestroomDetail, req.getParameterMap());
		 Page<SyTestroomDetail> page = new Page<SyTestroomDetail>(pageNo, pageSize);
		 IPage<SyTestroomDetail> pageList = syTestroomDetailService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param syTestroomDetail
	 * @return
	 */
	@AutoLog(value = "sy_testroom_detail-添加")
	@ApiOperation(value="sy_testroom_detail-添加", notes="sy_testroom_detail-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyTestroomDetail syTestroomDetail) {
		syTestroomDetailService.save(syTestroomDetail);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param syTestroomDetail
	 * @return
	 */
	@AutoLog(value = "sy_testroom_detail-编辑")
	@ApiOperation(value="sy_testroom_detail-编辑", notes="sy_testroom_detail-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyTestroomDetail syTestroomDetail) {
		syTestroomDetailService.updateById(syTestroomDetail);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_testroom_detail-通过id删除")
	@ApiOperation(value="sy_testroom_detail-通过id删除", notes="sy_testroom_detail-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syTestroomDetailService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_testroom_detail-批量删除")
	@ApiOperation(value="sy_testroom_detail-批量删除", notes="sy_testroom_detail-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syTestroomDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_testroom_detail-通过id查询")
	@ApiOperation(value="sy_testroom_detail-通过id查询", notes="sy_testroom_detail-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyTestroomDetail syTestroomDetail = syTestroomDetailService.getById(id);
		if(syTestroomDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syTestroomDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syTestroomDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyTestroomDetail syTestroomDetail) {
        return super.exportXls(request, syTestroomDetail, SyTestroomDetail.class, "sy_testroom_detail");
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
        return super.importExcel(request, response, SyTestroomDetail.class);
    }

}
