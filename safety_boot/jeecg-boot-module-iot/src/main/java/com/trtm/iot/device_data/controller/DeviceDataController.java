package com.trtm.iot.device_data.controller;

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
import com.trtm.iot.device_data.entity.DeviceData;
import com.trtm.iot.device_data.service.IDeviceDataService;

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
 * @Description: 搅拌车监控
 * @Author: jeecg-boot
 * @Date:   2025-05-06
 * @Version: V1.0
 */
@Api(tags="搅拌车监控")
@RestController
@RequestMapping("/device_data/deviceData")
@Slf4j
public class DeviceDataController extends JeecgController<DeviceData, IDeviceDataService> {
	@Autowired
	private IDeviceDataService deviceDataService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "搅拌车监控-分页列表查询")
	@ApiOperation(value="搅拌车监控-分页列表查询", notes="搅拌车监控-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceData deviceData,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceData> queryWrapper = QueryGenerator.initQueryWrapper(deviceData, req.getParameterMap());
		Page<DeviceData> page = new Page<DeviceData>(pageNo, pageSize);
		IPage<DeviceData> pageList = deviceDataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceData
	 * @return
	 */
	@AutoLog(value = "搅拌车监控-添加")
	@ApiOperation(value="搅拌车监控-添加", notes="搅拌车监控-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceData deviceData) {
		deviceDataService.save(deviceData);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param deviceData
	 * @return
	 */
	@AutoLog(value = "搅拌车监控-编辑")
	@ApiOperation(value="搅拌车监控-编辑", notes="搅拌车监控-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceData deviceData) {
		deviceDataService.updateById(deviceData);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "搅拌车监控-通过id删除")
	@ApiOperation(value="搅拌车监控-通过id删除", notes="搅拌车监控-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceDataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "搅拌车监控-批量删除")
	@ApiOperation(value="搅拌车监控-批量删除", notes="搅拌车监控-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceDataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "搅拌车监控-通过id查询")
	@ApiOperation(value="搅拌车监控-通过id查询", notes="搅拌车监控-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceData deviceData = deviceDataService.getById(id);
		if(deviceData==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceData);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceData
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceData deviceData) {
        return super.exportXls(request, deviceData, DeviceData.class, "搅拌车监控");
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
        return super.importExcel(request, response, DeviceData.class);
    }

}
