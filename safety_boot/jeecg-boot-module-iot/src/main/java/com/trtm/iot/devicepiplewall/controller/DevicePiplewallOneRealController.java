package com.trtm.iot.devicepiplewall.controller;

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
import com.trtm.iot.devicepiplewall.entity.DevicePiplewallOneReal;
import com.trtm.iot.devicepiplewall.service.IDevicePiplewallOneRealService;

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
 * @Description: device_piplewall_one_real
 * @Author: jeecg-boot
 * @Date:   2024-12-28
 * @Version: V1.0
 */
@Api(tags="device_piplewall_one_real")
@RestController
@RequestMapping("/devicepiplewall/devicePiplewallOneReal")
@Slf4j
public class DevicePiplewallOneRealController extends JeecgController<DevicePiplewallOneReal, IDevicePiplewallOneRealService> {
	@Autowired
	private IDevicePiplewallOneRealService devicePiplewallOneRealService;
	
	/**
	 * 分页列表查询
	 *
	 * @param devicePiplewallOneReal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one_real-分页列表查询")
	@ApiOperation(value="device_piplewall_one_real-分页列表查询", notes="device_piplewall_one_real-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicePiplewallOneReal devicePiplewallOneReal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevicePiplewallOneReal> queryWrapper = QueryGenerator.initQueryWrapper(devicePiplewallOneReal, req.getParameterMap());
		Page<DevicePiplewallOneReal> page = new Page<DevicePiplewallOneReal>(pageNo, pageSize);
		IPage<DevicePiplewallOneReal> pageList = devicePiplewallOneRealService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param devicePiplewallOneReal
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one_real-添加")
	@ApiOperation(value="device_piplewall_one_real-添加", notes="device_piplewall_one_real-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicePiplewallOneReal devicePiplewallOneReal) {
		devicePiplewallOneRealService.save(devicePiplewallOneReal);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devicePiplewallOneReal
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one_real-编辑")
	@ApiOperation(value="device_piplewall_one_real-编辑", notes="device_piplewall_one_real-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicePiplewallOneReal devicePiplewallOneReal) {
		devicePiplewallOneRealService.updateById(devicePiplewallOneReal);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one_real-通过id删除")
	@ApiOperation(value="device_piplewall_one_real-通过id删除", notes="device_piplewall_one_real-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicePiplewallOneRealService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one_real-批量删除")
	@ApiOperation(value="device_piplewall_one_real-批量删除", notes="device_piplewall_one_real-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicePiplewallOneRealService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one_real-通过id查询")
	@ApiOperation(value="device_piplewall_one_real-通过id查询", notes="device_piplewall_one_real-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicePiplewallOneReal devicePiplewallOneReal = devicePiplewallOneRealService.getById(id);
		if(devicePiplewallOneReal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicePiplewallOneReal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicePiplewallOneReal
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePiplewallOneReal devicePiplewallOneReal) {
        return super.exportXls(request, devicePiplewallOneReal, DevicePiplewallOneReal.class, "device_piplewall_one_real");
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
        return super.importExcel(request, response, DevicePiplewallOneReal.class);
    }

}
