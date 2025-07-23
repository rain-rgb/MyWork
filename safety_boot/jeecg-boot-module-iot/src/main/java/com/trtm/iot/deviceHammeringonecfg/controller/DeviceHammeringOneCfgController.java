package com.trtm.iot.deviceHammeringonecfg.controller;

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
import com.trtm.iot.deviceHammeringonecfg.entity.DeviceHammeringOneCfg;
import com.trtm.iot.deviceHammeringonecfg.service.IDeviceHammeringOneCfgService;

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
 * @Description: device_hammering_one_cfg
 * @Author: jeecg-boot
 * @Date:   2024-03-13
 * @Version: V1.0
 */
@Api(tags="device_hammering_one_cfg")
@RestController
@RequestMapping("/deviceHammeringonecfg/deviceHammeringOneCfg")
@Slf4j
public class DeviceHammeringOneCfgController extends JeecgController<DeviceHammeringOneCfg, IDeviceHammeringOneCfgService> {
	@Autowired
	private IDeviceHammeringOneCfgService deviceHammeringOneCfgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceHammeringOneCfg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_hammering_one_cfg-分页列表查询")
	@ApiOperation(value="device_hammering_one_cfg-分页列表查询", notes="device_hammering_one_cfg-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceHammeringOneCfg deviceHammeringOneCfg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceHammeringOneCfg> queryWrapper = QueryGenerator.initQueryWrapper(deviceHammeringOneCfg, req.getParameterMap());
		Page<DeviceHammeringOneCfg> page = new Page<DeviceHammeringOneCfg>(pageNo, pageSize);
		IPage<DeviceHammeringOneCfg> pageList = deviceHammeringOneCfgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceHammeringOneCfg
	 * @return
	 */
	@AutoLog(value = "device_hammering_one_cfg-添加")
	@ApiOperation(value="device_hammering_one_cfg-添加", notes="device_hammering_one_cfg-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceHammeringOneCfg deviceHammeringOneCfg) {
		QueryWrapper<DeviceHammeringOneCfg> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("shebeino",deviceHammeringOneCfg.getShebeino());
		DeviceHammeringOneCfg one = deviceHammeringOneCfgService.getOne(queryWrapper);
		if (one != null){
			deviceHammeringOneCfg.setId(one.getId());
			deviceHammeringOneCfgService.updateById(deviceHammeringOneCfg);
		}else {
			deviceHammeringOneCfgService.save(deviceHammeringOneCfg);
		}
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param deviceHammeringOneCfg
	 * @return
	 */
	@AutoLog(value = "device_hammering_one_cfg-编辑")
	@ApiOperation(value="device_hammering_one_cfg-编辑", notes="device_hammering_one_cfg-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceHammeringOneCfg deviceHammeringOneCfg) {
		deviceHammeringOneCfgService.updateById(deviceHammeringOneCfg);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_hammering_one_cfg-通过id删除")
	@ApiOperation(value="device_hammering_one_cfg-通过id删除", notes="device_hammering_one_cfg-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceHammeringOneCfgService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_hammering_one_cfg-批量删除")
	@ApiOperation(value="device_hammering_one_cfg-批量删除", notes="device_hammering_one_cfg-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceHammeringOneCfgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_hammering_one_cfg-通过id查询")
	@ApiOperation(value="device_hammering_one_cfg-通过id查询", notes="device_hammering_one_cfg-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceHammeringOneCfg deviceHammeringOneCfg = deviceHammeringOneCfgService.getById(id);
		if(deviceHammeringOneCfg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceHammeringOneCfg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceHammeringOneCfg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceHammeringOneCfg deviceHammeringOneCfg) {
        return super.exportXls(request, deviceHammeringOneCfg, DeviceHammeringOneCfg.class, "device_hammering_one_cfg");
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
        return super.importExcel(request, response, DeviceHammeringOneCfg.class);
    }

}
