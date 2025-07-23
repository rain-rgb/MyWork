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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.sy.sylxdps.entity.SyDpsSyReportM;
import com.trtm.sy.sylxdps.service.ISyDpsSyReportMService;

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
 * @Description: sy_dps_sy_report_m
 * @Author: jeecg-boot
 * @Date:   2023-02-10
 * @Version: V1.0
 */
@Api(tags="sy_dps_sy_report_m")
@RestController
@RequestMapping("/sylxdps/syDpsSyReportM")
@Slf4j
public class SyDpsSyReportMController extends JeecgController<SyDpsSyReportM, ISyDpsSyReportMService> {
	@Autowired
	private ISyDpsSyReportMService syDpsSyReportMService;

	/**
	 * 分页列表查询
	 *
	 * @param syDpsSyReportM
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_report_m-分页列表查询")
	@ApiOperation(value="sy_dps_sy_report_m-分页列表查询", notes="sy_dps_sy_report_m-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDpsSyReportM syDpsSyReportM,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyDpsSyReportM> queryWrapper = QueryGenerator.initQueryWrapper(syDpsSyReportM, req.getParameterMap());
		Page<SyDpsSyReportM> page = new Page<SyDpsSyReportM>(pageNo, pageSize);
		IPage<SyDpsSyReportM> pageList = syDpsSyReportMService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param syDpsSyReportM
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_report_m-添加")
	@ApiOperation(value="sy_dps_sy_report_m-添加", notes="sy_dps_sy_report_m-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDpsSyReportM syDpsSyReportM) {
		syDpsSyReportMService.save(syDpsSyReportM);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param syDpsSyReportM
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_report_m-编辑")
	@ApiOperation(value="sy_dps_sy_report_m-编辑", notes="sy_dps_sy_report_m-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDpsSyReportM syDpsSyReportM) {
		syDpsSyReportMService.updateById(syDpsSyReportM);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_report_m-通过id删除")
	@ApiOperation(value="sy_dps_sy_report_m-通过id删除", notes="sy_dps_sy_report_m-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDpsSyReportMService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_report_m-批量删除")
	@ApiOperation(value="sy_dps_sy_report_m-批量删除", notes="sy_dps_sy_report_m-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDpsSyReportMService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_report_m-通过id查询")
	@ApiOperation(value="sy_dps_sy_report_m-通过id查询", notes="sy_dps_sy_report_m-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyDpsSyReportM syDpsSyReportM = syDpsSyReportMService.getById(id);
		if(syDpsSyReportM==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDpsSyReportM);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syDpsSyReportM
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsSyReportM syDpsSyReportM) {
        return super.exportXls(request, syDpsSyReportM, SyDpsSyReportM.class, "sy_dps_sy_report_m");
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
        return super.importExcel(request, response, SyDpsSyReportM.class);
    }

}
