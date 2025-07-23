package com.trtm.iot.devicepowerrealdata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepowerrealdata.entity.DevicePowerRealdata;
import com.trtm.iot.devicepowerrealdata.service.IDevicePowerRealdataService;

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
 * @Description: device_power_realdata
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
@Api(tags="device_power_realdata")
@RestController
@RequestMapping("/devicepowerrealdata/devicePowerRealdata")
@Slf4j
public class DevicePowerRealdataController extends JeecgController<DevicePowerRealdata, IDevicePowerRealdataService> {
	@Autowired
	private IDevicePowerRealdataService devicePowerRealdataService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param devicePowerRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_power_realdata-分页列表查询")
	@ApiOperation(value="device_power_realdata-分页列表查询", notes="device_power_realdata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicePowerRealdata devicePowerRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (devicePowerRealdata.getImei() == null) {
			if (!"null".equals(shebei)) {
				devicePowerRealdata.setImei(shebei);
			} else {
				devicePowerRealdata.setImei("空");
			}
		}
		QueryWrapper<DevicePowerRealdata> queryWrapper = QueryGenerator.initQueryWrapper(devicePowerRealdata, req.getParameterMap());
		Page<DevicePowerRealdata> page = new Page<DevicePowerRealdata>(pageNo, pageSize);
		IPage<DevicePowerRealdata> pageList = devicePowerRealdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param devicePowerRealdata
	 * @return
	 */
	@AutoLog(value = "device_power_realdata-添加")
	@ApiOperation(value="device_power_realdata-添加", notes="device_power_realdata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicePowerRealdata devicePowerRealdata) {
		devicePowerRealdataService.save(devicePowerRealdata);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devicePowerRealdata
	 * @return
	 */
	@AutoLog(value = "device_power_realdata-编辑")
	@ApiOperation(value="device_power_realdata-编辑", notes="device_power_realdata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicePowerRealdata devicePowerRealdata) {
		devicePowerRealdataService.updateById(devicePowerRealdata);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_power_realdata-通过id删除")
	@ApiOperation(value="device_power_realdata-通过id删除", notes="device_power_realdata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicePowerRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_power_realdata-批量删除")
	@ApiOperation(value="device_power_realdata-批量删除", notes="device_power_realdata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicePowerRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_power_realdata-通过id查询")
	@ApiOperation(value="device_power_realdata-通过id查询", notes="device_power_realdata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicePowerRealdata devicePowerRealdata = devicePowerRealdataService.getById(id);
		if(devicePowerRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicePowerRealdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicePowerRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePowerRealdata devicePowerRealdata) {
        return super.exportXls(request, devicePowerRealdata, DevicePowerRealdata.class, "device_power_realdata");
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
        return super.importExcel(request, response, DevicePowerRealdata.class);
    }

}
