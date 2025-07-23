package com.trtm.iot.devicepipepilereport.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkcoding.http.util.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepipepilereport.entity.DevicePipepileReport;
import com.trtm.iot.devicepipepilereport.service.IDevicePipepileReportService;

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
 * @Description: device_pipepile_report
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Api(tags="device_pipepile_report")
@RestController
@RequestMapping("/devicepipepilereport/devicePipepileReport")
@Slf4j
public class DevicePipepileReportController extends JeecgController<DevicePipepileReport, IDevicePipepileReportService> {
	@Autowired
	private IDevicePipepileReportService devicePipepileReportService;

	/**
	 * 分页列表查询
	 *
	 * @param devicePipepileReport
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_pipepile_report-分页列表查询")
	@ApiOperation(value="device_pipepile_report-分页列表查询", notes="device_pipepile_report-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicePipepileReport devicePipepileReport,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevicePipepileReport> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileReport, req.getParameterMap());
		Page<DevicePipepileReport> page = new Page<DevicePipepileReport>(pageNo, pageSize);
		IPage<DevicePipepileReport> pageList = devicePipepileReportService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	/**
	 *   添加
	 *
	 * @param devicePipepileReport
	 * @return
	 */
	@AutoLog(value = "device_pipepile_report-添加")
	@ApiOperation(value="device_pipepile_report-添加", notes="device_pipepile_report-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicePipepileReport devicePipepileReport) {
		devicePipepileReportService.save(devicePipepileReport);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加
	  *
	  * @param devicePipepileReport
	  * @return
	  */
	 @AutoLog(value = "device_pipepile_report-添加")
	 @ApiOperation(value="device_pipepile_report-添加", notes="device_pipepile_report-添加")
	 @PostMapping(value = "/addOther")
	 public Result<?> addOther(@RequestBody DevicePipepileReport devicePipepileReport) {
		 if(StringUtil.isNotEmpty(devicePipepileReport.getShebeino()) && StringUtil.isNotEmpty(devicePipepileReport.getPileno())) {
			 try {
				 QueryWrapper<DevicePipepileReport> queryWrapper = new QueryWrapper<>();
				 queryWrapper.eq("shebeino",devicePipepileReport.getShebeino());
				 queryWrapper.eq("pileno",devicePipepileReport.getPileno());
				 DevicePipepileReport one = devicePipepileReportService.getOne(queryWrapper);
				 if (one != null){
					 DevicePipepileReport devicePipepileReport1 = devicePipepileReport.setId(one.getId());
					 devicePipepileReportService.updateById(devicePipepileReport1);
				 }else {
					 devicePipepileReportService.save(devicePipepileReport);
				 }
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }else{
			 return Result.error("记录上传失败！请填入设备编号和桩号");
		 }
		 return Result.OK("添加成功！");
	 }
	/**
	 *  编辑
	 *
	 * @param devicePipepileReport
	 * @return
	 */
	@AutoLog(value = "device_pipepile_report-编辑")
	@ApiOperation(value="device_pipepile_report-编辑", notes="device_pipepile_report-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicePipepileReport devicePipepileReport) {
		devicePipepileReportService.updateById(devicePipepileReport);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_pipepile_report-通过id删除")
	@ApiOperation(value="device_pipepile_report-通过id删除", notes="device_pipepile_report-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicePipepileReportService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_pipepile_report-批量删除")
	@ApiOperation(value="device_pipepile_report-批量删除", notes="device_pipepile_report-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicePipepileReportService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_pipepile_report-通过id查询")
	@ApiOperation(value="device_pipepile_report-通过id查询", notes="device_pipepile_report-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicePipepileReport devicePipepileReport = devicePipepileReportService.getById(id);
		if(devicePipepileReport==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicePipepileReport);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicePipepileReport
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePipepileReport devicePipepileReport) {
        return super.exportXls(request, devicePipepileReport, DevicePipepileReport.class, "device_pipepile_report");
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
        return super.importExcel(request, response, DevicePipepileReport.class);
    }

	 /**
	  *   添加(免登录版)
	  *
	  * @param devicePipepileReport
	  * @return
	  */
	 @AutoLog(value = "device_pipepile_report-添加")
	 @ApiOperation(value="device_pipepile_report-添加", notes="device_pipepile_report-添加")
	 @PostMapping(value = "/addOther1")
	 public Result<?> addOther1(@RequestBody DevicePipepileReport devicePipepileReport) {
		 if(StringUtil.isNotEmpty(devicePipepileReport.getShebeino()) && StringUtil.isNotEmpty(devicePipepileReport.getPileno())) {
			 try {
				 QueryWrapper<DevicePipepileReport> queryWrapper = new QueryWrapper<>();
				 queryWrapper.eq("shebeino",devicePipepileReport.getShebeino());
				 queryWrapper.eq("pileno",devicePipepileReport.getPileno());
				 DevicePipepileReport one = devicePipepileReportService.getOne(queryWrapper);
				 if (one != null){
					 DevicePipepileReport devicePipepileReport1 = devicePipepileReport.setId(one.getId());
					 devicePipepileReportService.updateById(devicePipepileReport1);
				 }else {
					 devicePipepileReportService.save(devicePipepileReport);
				 }
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }else{
			 return Result.error("记录上传失败！请填入设备编号和桩号");
		 }
		 return Result.OK("添加成功！");
	 }

}
