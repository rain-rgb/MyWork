package com.trtm.iot.devicepowerstatusdata.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.zhydstatistics.entity.DeviceElectrcStatistics;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepowerstatusdata.entity.DevicePowerStatusdata;
import com.trtm.iot.devicepowerstatusdata.service.IDevicePowerStatusdataService;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: device_power_statusdata
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
@Api(tags="device_power_statusdata")
@RestController
@RequestMapping("/devicepowerstatusdata/devicePowerStatusdata")
@Slf4j
public class DevicePowerStatusdataController extends JeecgController<DevicePowerStatusdata, IDevicePowerStatusdataService> {
	@Autowired
	private IDevicePowerStatusdataService devicePowerStatusdataService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param devicePowerStatusdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_power_statusdata-分页列表查询")
	@ApiOperation(value="device_power_statusdata-分页列表查询", notes="device_power_statusdata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicePowerStatusdata devicePowerStatusdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (devicePowerStatusdata.getImei() == null) {
			if (!"null".equals(shebei)) {
				devicePowerStatusdata.setImei(shebei);
			} else {
				devicePowerStatusdata.setImei("空");
			}
		}
		QueryWrapper<DevicePowerStatusdata> queryWrapper = QueryGenerator.initQueryWrapper(devicePowerStatusdata, req.getParameterMap());
		Page<DevicePowerStatusdata> page = new Page<DevicePowerStatusdata>(pageNo, pageSize);
		IPage<DevicePowerStatusdata> pageList = devicePowerStatusdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param devicePowerStatusdata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_power_statusdata-分页列表查询")
	 @ApiOperation(value="device_power_statusdata-分页列表查询", notes="device_power_statusdata-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(DevicePowerStatusdata devicePowerStatusdata,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (devicePowerStatusdata.getImei() == null) {
			 if (!"null".equals(shebei)) {
				 devicePowerStatusdata.setImei(shebei);
			 } else {
				 devicePowerStatusdata.setImei("空");
			 }
		 }
		 QueryWrapper<DevicePowerStatusdata> queryWrapper = QueryGenerator.initQueryWrapper(devicePowerStatusdata, req.getParameterMap());
		 queryWrapper.select("sum(t1 = 21) t1,count(t2) t2,count(t3) t3,count(t4) t4,count(t5) t5,count(t6) t6,count(t7) t7,count(t8) t8,count(t9) t9,count(t10) t10,count(t11) t11");
		 DevicePowerStatusdata pageList = devicePowerStatusdataService.getOne(queryWrapper);
		 Map map = new HashMap();
		 Integer vchaobiaosum = 0;//电压超标总数
		 Integer echaobiaosum = 0;//电流超标总数
		 Integer schaobiaosum = 0;//漏电流超标总数
		 Integer tchaobiaosum = 0;//温度超标总数
		 if (pageList!=null) {
			 if (!StringUtils.isEmpty(pageList.getT1())) {
				 schaobiaosum = pageList.getT1();
			 }
			 if (!StringUtils.isEmpty(pageList.getT6())&&!StringUtils.isEmpty(pageList.getT7())&&!StringUtils.isEmpty(pageList.getT8())) {
				 echaobiaosum = pageList.getT6()+pageList.getT7()+pageList.getT8();
			 }
			 if (!StringUtils.isEmpty(pageList.getT9())&&!StringUtils.isEmpty(pageList.getT10())&&!StringUtils.isEmpty(pageList.getT11())) {
				 vchaobiaosum = pageList.getT9()+pageList.getT10()+pageList.getT11();
			 }
			 if (!StringUtils.isEmpty(pageList.getT2())&&!StringUtils.isEmpty(pageList.getT3())&&!StringUtils.isEmpty(pageList.getT4())&&!StringUtils.isEmpty(pageList.getT5())) {
				 tchaobiaosum = pageList.getT2()+pageList.getT3()+pageList.getT4()+pageList.getT5();
			 }
		 }
		 map.put("vchaobiaosum",vchaobiaosum);
		 map.put("echaobiaosum",echaobiaosum);
		 map.put("schaobiaosum",18);
		 map.put("tchaobiaosum",21);
		 return Result.OK(map);
	 }

	/**
	 *   添加
	 *
	 * @param devicePowerStatusdata
	 * @return
	 */
	@AutoLog(value = "device_power_statusdata-添加")
	@ApiOperation(value="device_power_statusdata-添加", notes="device_power_statusdata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicePowerStatusdata devicePowerStatusdata) {
		devicePowerStatusdataService.save(devicePowerStatusdata);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devicePowerStatusdata
	 * @return
	 */
	@AutoLog(value = "device_power_statusdata-编辑")
	@ApiOperation(value="device_power_statusdata-编辑", notes="device_power_statusdata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicePowerStatusdata devicePowerStatusdata) {
		devicePowerStatusdataService.updateById(devicePowerStatusdata);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_power_statusdata-通过id删除")
	@ApiOperation(value="device_power_statusdata-通过id删除", notes="device_power_statusdata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicePowerStatusdataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_power_statusdata-批量删除")
	@ApiOperation(value="device_power_statusdata-批量删除", notes="device_power_statusdata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicePowerStatusdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_power_statusdata-通过id查询")
	@ApiOperation(value="device_power_statusdata-通过id查询", notes="device_power_statusdata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicePowerStatusdata devicePowerStatusdata = devicePowerStatusdataService.getById(id);
		if(devicePowerStatusdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicePowerStatusdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicePowerStatusdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePowerStatusdata devicePowerStatusdata) {
        return super.exportXls(request, devicePowerStatusdata, DevicePowerStatusdata.class, "device_power_statusdata");
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
        return super.importExcel(request, response, DevicePowerStatusdata.class);
    }

}
