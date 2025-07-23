package com.trtm.iot.devicehistory.controller;

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
import com.trtm.iot.devicehistory.entity.DevicehistoryWether;
import com.trtm.iot.devicehistory.service.IDevicehistoryWetherService;

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
 * @Description: devicehistory_wether
 * @Author: jeecg-boot
 * @Date:   2024-07-29
 * @Version: V1.0
 */
@Api(tags="devicehistory_wether")
@RestController
@RequestMapping("/devicehistory/devicehistoryWether")
@Slf4j
public class DevicehistoryWetherController extends JeecgController<DevicehistoryWether, IDevicehistoryWetherService> {
	@Autowired
	private IDevicehistoryWetherService devicehistoryWetherService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param devicehistoryWether
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "devicehistory_wether-分页列表查询")
	@ApiOperation(value="devicehistory_wether-分页列表查询", notes="devicehistory_wether-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicehistoryWether devicehistoryWether,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (devicehistoryWether.getDevaddr() == null) {
			if (!"null".equals(shebei)) {
				devicehistoryWether.setDevaddr(shebei);
			}else {
				devicehistoryWether.setDevaddr("空");
			}
		}
		QueryWrapper<DevicehistoryWether> queryWrapper = QueryGenerator.initQueryWrapper(devicehistoryWether, req.getParameterMap());
		Page<DevicehistoryWether> page = new Page<DevicehistoryWether>(pageNo, pageSize);
		IPage<DevicehistoryWether> pageList = devicehistoryWetherService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param devicehistoryWether
	 * @return
	 */
	@AutoLog(value = "devicehistory_wether-添加")
	@ApiOperation(value="devicehistory_wether-添加", notes="devicehistory_wether-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicehistoryWether devicehistoryWether) {
		devicehistoryWetherService.save(devicehistoryWether);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param devicehistoryWether
	 * @return
	 */
	@AutoLog(value = "devicehistory_wether-编辑")
	@ApiOperation(value="devicehistory_wether-编辑", notes="devicehistory_wether-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicehistoryWether devicehistoryWether) {
		devicehistoryWetherService.updateById(devicehistoryWether);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "devicehistory_wether-通过id删除")
	@ApiOperation(value="devicehistory_wether-通过id删除", notes="devicehistory_wether-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicehistoryWetherService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "devicehistory_wether-批量删除")
	@ApiOperation(value="devicehistory_wether-批量删除", notes="devicehistory_wether-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicehistoryWetherService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "devicehistory_wether-通过id查询")
	@ApiOperation(value="devicehistory_wether-通过id查询", notes="devicehistory_wether-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicehistoryWether devicehistoryWether = devicehistoryWetherService.getById(id);
		if(devicehistoryWether==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicehistoryWether);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicehistoryWether
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicehistoryWether devicehistoryWether) {
        return super.exportXls(request, devicehistoryWether, DevicehistoryWether.class, "devicehistory_wether");
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
        return super.importExcel(request, response, DevicehistoryWether.class);
    }

}
