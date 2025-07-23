package com.trtm.iot.devicehighformwork.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicehighformwork.entity.DeviceHighFormwork;
import com.trtm.iot.devicehighformwork.service.IDeviceHighFormworkService;

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
 * @Description: device_high_formwork
 * @Author: jeecg-boot
 * @Date:   2023-11-21
 * @Version: V1.0
 */
@Api(tags="device_high_formwork")
@RestController
@RequestMapping("/devicehighformwork/deviceHighFormwork")
@Slf4j
public class DeviceHighFormworkController extends JeecgController<DeviceHighFormwork, IDeviceHighFormworkService> {
	@Autowired
	private IDeviceHighFormworkService deviceHighFormworkService;
	 @Autowired
	 private RedisUtil redisUtil;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceHighFormwork
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_high_formwork-分页列表查询")
	@ApiOperation(value="device_high_formwork-分页列表查询", notes="device_high_formwork-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceHighFormwork deviceHighFormwork,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (StringUtils.isBlank(deviceHighFormwork.getSnbh()) ) {
			if (!"null".equals(shebei)) {
				deviceHighFormwork.setSnbh(shebei);
			} else {
				deviceHighFormwork.setSnbh("空");
			}
		}
		QueryWrapper<DeviceHighFormwork> queryWrapper = QueryGenerator.initQueryWrapper(deviceHighFormwork, req.getParameterMap());
		Page<DeviceHighFormwork> page = new Page<DeviceHighFormwork>(pageNo, pageSize);
		IPage<DeviceHighFormwork> pageList = deviceHighFormworkService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceHighFormwork
	 * @return
	 */
	@AutoLog(value = "device_high_formwork-添加")
	@ApiOperation(value="device_high_formwork-添加", notes="device_high_formwork-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceHighFormwork deviceHighFormwork) {
		deviceHighFormworkService.save(deviceHighFormwork);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param deviceHighFormwork
	 * @return
	 */
	@AutoLog(value = "device_high_formwork-编辑")
	@ApiOperation(value="device_high_formwork-编辑", notes="device_high_formwork-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceHighFormwork deviceHighFormwork) {
		deviceHighFormworkService.updateById(deviceHighFormwork);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_high_formwork-通过id删除")
	@ApiOperation(value="device_high_formwork-通过id删除", notes="device_high_formwork-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceHighFormworkService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_high_formwork-批量删除")
	@ApiOperation(value="device_high_formwork-批量删除", notes="device_high_formwork-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceHighFormworkService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_high_formwork-通过id查询")
	@ApiOperation(value="device_high_formwork-通过id查询", notes="device_high_formwork-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceHighFormwork deviceHighFormwork = deviceHighFormworkService.getById(id);
		if(deviceHighFormwork==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceHighFormwork);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceHighFormwork
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceHighFormwork deviceHighFormwork) {
        return super.exportXls(request, deviceHighFormwork, DeviceHighFormwork.class, "device_high_formwork");
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
        return super.importExcel(request, response, DeviceHighFormwork.class);
    }

}
