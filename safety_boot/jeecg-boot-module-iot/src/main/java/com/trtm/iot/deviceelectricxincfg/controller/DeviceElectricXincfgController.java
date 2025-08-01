package com.trtm.iot.deviceelectricxincfg.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.deviceelectricxincfg.entity.DeviceElectricXincfg;
import com.trtm.iot.deviceelectricxincfg.service.IDeviceElectricXincfgService;

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
 * @Description: device_electric_xincfg
 * @Author: jeecg-boot
 * @Date:   2023-06-25
 * @Version: V1.0
 */
@Api(tags="device_electric_xincfg")
@RestController
@RequestMapping("/deviceelectricxincfg/deviceElectricXincfg")
@Slf4j
public class DeviceElectricXincfgController extends JeecgController<DeviceElectricXincfg, IDeviceElectricXincfgService> {
	@Autowired
	private IDeviceElectricXincfgService deviceElectricXincfgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceElectricXincfg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_electric_xincfg-分页列表查询")
	@ApiOperation(value="device_electric_xincfg-分页列表查询", notes="device_electric_xincfg-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceElectricXincfg deviceElectricXincfg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceElectricXincfg> queryWrapper = QueryGenerator.initQueryWrapper(deviceElectricXincfg, req.getParameterMap());
		Page<DeviceElectricXincfg> page = new Page<DeviceElectricXincfg>(pageNo, pageSize);
		IPage<DeviceElectricXincfg> pageList = deviceElectricXincfgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceElectricXincfg
	 * @return
	 */
	@AutoLog(value = "device_electric_xincfg-添加")
	@ApiOperation(value="device_electric_xincfg-添加", notes="device_electric_xincfg-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceElectricXincfg deviceElectricXincfg) {
		String uuid = UUID.randomUUID().toString().replaceAll("-","");
		deviceElectricXincfg.setUid(uuid);
		deviceElectricXincfgService.save(deviceElectricXincfg);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param deviceElectricXincfg
	 * @return
	 */
	@AutoLog(value = "device_electric_xincfg-编辑")
	@ApiOperation(value="device_electric_xincfg-编辑", notes="device_electric_xincfg-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceElectricXincfg deviceElectricXincfg) {
		deviceElectricXincfgService.updateById(deviceElectricXincfg);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_electric_xincfg-通过id删除")
	@ApiOperation(value="device_electric_xincfg-通过id删除", notes="device_electric_xincfg-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceElectricXincfgService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_electric_xincfg-批量删除")
	@ApiOperation(value="device_electric_xincfg-批量删除", notes="device_electric_xincfg-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceElectricXincfgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_electric_xincfg-通过id查询")
	@ApiOperation(value="device_electric_xincfg-通过id查询", notes="device_electric_xincfg-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceElectricXincfg deviceElectricXincfg = deviceElectricXincfgService.getById(id);
		if(deviceElectricXincfg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceElectricXincfg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceElectricXincfg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceElectricXincfg deviceElectricXincfg) {
        return super.exportXls(request, deviceElectricXincfg, DeviceElectricXincfg.class, "device_electric_xincfg");
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
        return super.importExcel(request, response, DeviceElectricXincfg.class);
    }

}
