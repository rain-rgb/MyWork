package com.trtm.sy.sylxdps.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.sylxdps.entity.SyDpsSySampleView2;
import com.trtm.sy.sylxdps.service.ISyDpsSySampleView2Service;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;

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
 * @Description: sy_dps_sy_sample_view2
 * @Author: jeecg-boot
 * @Date:   2023-01-30
 * @Version: V1.0
 */
@Api(tags="sy_dps_sy_sample_view2")
@RestController
@RequestMapping("/sylxdps/syDpsSySampleView2")
@Slf4j
public class SyDpsSySampleView2Controller extends JeecgController<SyDpsSySampleView2, ISyDpsSySampleView2Service> {
	@Autowired
	private ISyDpsSySampleView2Service syDpsSySampleView2Service;

	/**
	 * 分页列表查询
	 *
	 * @param syDpsSySampleView2
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_sample_view2-分页列表查询")
	@ApiOperation(value="sy_dps_sy_sample_view2-分页列表查询", notes="sy_dps_sy_sample_view2-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDpsSySampleView2 syDpsSySampleView2,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyDpsSySampleView2> queryWrapper = QueryGenerator.initQueryWrapper(syDpsSySampleView2, req.getParameterMap());
		Page<SyDpsSySampleView2> page = new Page<SyDpsSySampleView2>(pageNo, pageSize);
		IPage<SyDpsSySampleView2> pageList = syDpsSySampleView2Service.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param syDpsSySampleView2
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_sample_view2-添加")
	@ApiOperation(value="sy_dps_sy_sample_view2-添加", notes="sy_dps_sy_sample_view2-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDpsSySampleView2 syDpsSySampleView2) {
		syDpsSySampleView2Service.save(syDpsSySampleView2);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param syDpsSySampleView2
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_sample_view2-编辑")
	@ApiOperation(value="sy_dps_sy_sample_view2-编辑", notes="sy_dps_sy_sample_view2-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDpsSySampleView2 syDpsSySampleView2) {
		syDpsSySampleView2Service.updateById(syDpsSySampleView2);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_sample_view2-通过id删除")
	@ApiOperation(value="sy_dps_sy_sample_view2-通过id删除", notes="sy_dps_sy_sample_view2-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDpsSySampleView2Service.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_sample_view2-批量删除")
	@ApiOperation(value="sy_dps_sy_sample_view2-批量删除", notes="sy_dps_sy_sample_view2-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDpsSySampleView2Service.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_sample_view2-通过id查询")
	@ApiOperation(value="sy_dps_sy_sample_view2-通过id查询", notes="sy_dps_sy_sample_view2-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyDpsSySampleView2 syDpsSySampleView2 = syDpsSySampleView2Service.getById(id);
		if(syDpsSySampleView2==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDpsSySampleView2);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syDpsSySampleView2
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsSySampleView2 syDpsSySampleView2) {
        return super.exportXls(request, syDpsSySampleView2, SyDpsSySampleView2.class, "sy_dps_sy_sample_view2");
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
        return super.importExcel(request, response, SyDpsSySampleView2.class);
    }

}
