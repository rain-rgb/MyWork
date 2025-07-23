package com.trtm.iot.deviceTunnelEnvironmentdataReal.controller;

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
import com.trtm.iot.deviceTunnelEnvironmentdataReal.entity.DeviceTunnelEnvironmentdataReal;
import com.trtm.iot.deviceTunnelEnvironmentdataReal.service.IDeviceTunnelEnvironmentdataRealService;

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
 * @Description: device_tunnel_environmentdata_real
 * @Author: jeecg-boot
 * @Date:   2022-08-18
 * @Version: V1.0
 */
@Api(tags="device_tunnel_environmentdata_real")
@RestController
@RequestMapping("/deviceTunnelEnvironmentdataReal/deviceTunnelEnvironmentdataReal")
@Slf4j
public class DeviceTunnelEnvironmentdataRealController extends JeecgController<DeviceTunnelEnvironmentdataReal, IDeviceTunnelEnvironmentdataRealService> {
	@Autowired
	private IDeviceTunnelEnvironmentdataRealService deviceTunnelEnvironmentdataRealService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceTunnelEnvironmentdataReal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_tunnel_environmentdata_real-分页列表查询")
	@ApiOperation(value="device_tunnel_environmentdata_real-分页列表查询", notes="device_tunnel_environmentdata_real-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceTunnelEnvironmentdataReal deviceTunnelEnvironmentdataReal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceTunnelEnvironmentdataReal> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelEnvironmentdataReal, req.getParameterMap());
		Page<DeviceTunnelEnvironmentdataReal> page = new Page<DeviceTunnelEnvironmentdataReal>(pageNo, pageSize);
		IPage<DeviceTunnelEnvironmentdataReal> pageList = deviceTunnelEnvironmentdataRealService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceTunnelEnvironmentdataReal
	 * @return
	 */
	@AutoLog(value = "device_tunnel_environmentdata_real-添加")
	@ApiOperation(value="device_tunnel_environmentdata_real-添加", notes="device_tunnel_environmentdata_real-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTunnelEnvironmentdataReal deviceTunnelEnvironmentdataReal) {
		deviceTunnelEnvironmentdataRealService.save(deviceTunnelEnvironmentdataReal);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param deviceTunnelEnvironmentdataReal
	 * @return
	 */
	@AutoLog(value = "device_tunnel_environmentdata_real-编辑")
	@ApiOperation(value="device_tunnel_environmentdata_real-编辑", notes="device_tunnel_environmentdata_real-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceTunnelEnvironmentdataReal deviceTunnelEnvironmentdataReal) {
		deviceTunnelEnvironmentdataRealService.updateById(deviceTunnelEnvironmentdataReal);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_tunnel_environmentdata_real-通过id删除")
	@ApiOperation(value="device_tunnel_environmentdata_real-通过id删除", notes="device_tunnel_environmentdata_real-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceTunnelEnvironmentdataRealService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_tunnel_environmentdata_real-批量删除")
	@ApiOperation(value="device_tunnel_environmentdata_real-批量删除", notes="device_tunnel_environmentdata_real-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTunnelEnvironmentdataRealService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_tunnel_environmentdata_real-通过id查询")
	@ApiOperation(value="device_tunnel_environmentdata_real-通过id查询", notes="device_tunnel_environmentdata_real-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTunnelEnvironmentdataReal deviceTunnelEnvironmentdataReal = deviceTunnelEnvironmentdataRealService.getById(id);
		if(deviceTunnelEnvironmentdataReal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceTunnelEnvironmentdataReal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceTunnelEnvironmentdataReal
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceTunnelEnvironmentdataReal deviceTunnelEnvironmentdataReal) {
        return super.exportXls(request, deviceTunnelEnvironmentdataReal, DeviceTunnelEnvironmentdataReal.class, "device_tunnel_environmentdata_real");
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
        return super.importExcel(request, response, DeviceTunnelEnvironmentdataReal.class);
    }

}
