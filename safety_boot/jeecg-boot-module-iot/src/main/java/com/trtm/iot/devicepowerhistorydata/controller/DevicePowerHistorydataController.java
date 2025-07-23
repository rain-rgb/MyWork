package com.trtm.iot.devicepowerhistorydata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicepowerrealdata.entity.DevicePowerRealdata;
import com.trtm.iot.devicepowerrealdata.service.IDevicePowerRealdataService;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepowerhistorydata.entity.DevicePowerHistorydata;
import com.trtm.iot.devicepowerhistorydata.service.IDevicePowerHistorydataService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: device_power_historydata
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
@Api(tags="device_power_historydata")
@RestController
@RequestMapping("/devicepowerhistorydata/devicePowerHistorydata")
@Slf4j
public class DevicePowerHistorydataController extends JeecgController<DevicePowerHistorydata, IDevicePowerHistorydataService> {
	@Autowired
	private IDevicePowerHistorydataService devicePowerHistorydataService;
	@Autowired
	private IDevicePowerRealdataService devicePowerRealdataService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param devicePowerHistorydata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_power_historydata-分页列表查询")
	@ApiOperation(value="device_power_historydata-分页列表查询", notes="device_power_historydata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicePowerHistorydata devicePowerHistorydata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (devicePowerHistorydata.getImei() == null) {
			if (!"null".equals(shebei)) {
				devicePowerHistorydata.setImei(shebei);
			} else {
				devicePowerHistorydata.setImei("空");
			}
		}
		QueryWrapper<DevicePowerHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(devicePowerHistorydata, req.getParameterMap());
		Page<DevicePowerHistorydata> page = new Page<DevicePowerHistorydata>(pageNo, pageSize);
		IPage<DevicePowerHistorydata> pageList = devicePowerHistorydataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param devicePowerHistorydata
	 * @return
	 */
	@AutoLog(value = "device_power_historydata-添加")
	@ApiOperation(value="device_power_historydata-添加", notes="device_power_historydata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicePowerHistorydata devicePowerHistorydata) {
		devicePowerHistorydataService.save(devicePowerHistorydata);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param devicePowerHistorydata
	 * @return
	 */
	@AutoLog(value = "device_power_historydata-编辑")
	@ApiOperation(value="device_power_historydata-编辑", notes="device_power_historydata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicePowerHistorydata devicePowerHistorydata) {
		devicePowerHistorydataService.updateById(devicePowerHistorydata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_power_historydata-通过id删除")
	@ApiOperation(value="device_power_historydata-通过id删除", notes="device_power_historydata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicePowerHistorydataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_power_historydata-批量删除")
	@ApiOperation(value="device_power_historydata-批量删除", notes="device_power_historydata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicePowerHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_power_historydata-通过id查询")
	@ApiOperation(value="device_power_historydata-通过id查询", notes="device_power_historydata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicePowerHistorydata devicePowerHistorydata = devicePowerHistorydataService.getById(id);
		if(devicePowerHistorydata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicePowerHistorydata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicePowerHistorydata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePowerHistorydata devicePowerHistorydata) {
        return super.exportXls(request, devicePowerHistorydata, DevicePowerHistorydata.class, "device_power_historydata");
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
        return super.importExcel(request, response, DevicePowerHistorydata.class);
    }

	 /**
	  * 智慧用电新增历史数据时，根据设备编号判断实时表是否有该设备数据，如果有，就更新为该设备的最新数据，如果没有，就新增该设备的数据
	  *
	  * @param devicePowerHistorydata
	  * @return
	  */
	 @AutoLog(value = "device_power_historydata-添加")
	 @ApiOperation(value = "device_power_historydata-添加", notes = "device_power_historydata-添加")
	 @PostMapping(value = "/insertZHYDSyncData")
	 public Result<?> insertZHYDSyncData(@RequestBody DevicePowerHistorydata devicePowerHistorydata) {
		 devicePowerHistorydata.setId(null);
		 devicePowerHistorydataService.save(devicePowerHistorydata);
		 QueryWrapper<DevicePowerRealdata> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("imei", devicePowerHistorydata.getImei());
		 DevicePowerRealdata one = devicePowerRealdataService.getOne(queryWrapper);

		 DevicePowerRealdata devicePowerRealdata = new DevicePowerRealdata();
		 BeanUtils.copyProperties(devicePowerHistorydata, devicePowerRealdata);
		 if (ObjectUtil.isNotEmpty(one)) {//one不为空，更新数据
			 devicePowerRealdata.setId(one.getId());
			 devicePowerRealdataService.updateById(devicePowerRealdata);
			 return Result.OK("历史数据添加成功，实时数据更新成功！");
		 } else {//one为空，新增数据
			 devicePowerRealdataService.save(devicePowerRealdata);
			 return Result.OK("历史数据添加成功，实时数据添加成功！");
		 }
	 }

}
