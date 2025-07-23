package com.trtm.iot.devicehammeringhistorydataone.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.devicehammeringhistorydatapart.entity.DeviceHammeringHistorydataPart;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicehammeringhistorydataone.entity.DeviceHammeringHistorydataOne;
import com.trtm.iot.devicehammeringhistorydataone.service.IDeviceHammeringHistorydataOneService;

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
 * @Description: device_hammering_historydata_one
 * @Author: jeecg-boot
 * @Date:   2024-03-08
 * @Version: V1.0
 */
@Api(tags="device_hammering_historydata_one")
@RestController
@RequestMapping("/devicehammeringhistorydataone/deviceHammeringHistorydataOne")
@Slf4j
public class DeviceHammeringHistorydataOneController extends JeecgController<DeviceHammeringHistorydataOne, IDeviceHammeringHistorydataOneService> {
	@Autowired
	private IDeviceHammeringHistorydataOneService deviceHammeringHistorydataOneService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param deviceHammeringHistorydataOne
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_one-分页列表查询")
	@ApiOperation(value="device_hammering_historydata_one-分页列表查询", notes="device_hammering_historydata_one-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceHammeringHistorydataOne deviceHammeringHistorydataOne,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (deviceHammeringHistorydataOne.getShebeino() == null || deviceHammeringHistorydataOne.getShebeino().isEmpty()) {
			if (!"null".equals(shebei)) {
				deviceHammeringHistorydataOne.setShebeino(shebei);
			} else {
				deviceHammeringHistorydataOne.setShebeino("空");
			}
		}
		QueryWrapper<DeviceHammeringHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceHammeringHistorydataOne, req.getParameterMap());
		Page<DeviceHammeringHistorydataOne> page = new Page<DeviceHammeringHistorydataOne>(pageNo, pageSize);
		IPage<DeviceHammeringHistorydataOne> pageList = deviceHammeringHistorydataOneService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param deviceHammeringHistorydataOne
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_hammering_historydata_one-分页列表查询")
	 @ApiOperation(value="device_hammering_historydata_one-分页列表查询", notes="device_hammering_historydata_one-分页列表查询")
	 @GetMapping(value = "/listcbcx")
	 public Result<?> queryPageListcbcx(DeviceHammeringHistorydataOne deviceHammeringHistorydataOne,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceHammeringHistorydataOne.getShebeino() == null || deviceHammeringHistorydataOne.getShebeino().isEmpty()) {
			 if (!"null".equals(shebei)) {
				 deviceHammeringHistorydataOne.setShebeino(shebei);
			 } else {
				 deviceHammeringHistorydataOne.setShebeino("空");
			 }
		 }
		 QueryWrapper<DeviceHammeringHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceHammeringHistorydataOne, req.getParameterMap());
		 queryWrapper.eq("chaobiaodengji",1);
		 Page<DeviceHammeringHistorydataOne> page = new Page<DeviceHammeringHistorydataOne>(pageNo, pageSize);
		 IPage<DeviceHammeringHistorydataOne> pageList = deviceHammeringHistorydataOneService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param deviceHammeringHistorydataOne
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_hammering_historydata_one-分页列表查询")
	 @ApiOperation(value="device_hammering_historydata_one-分页列表查询", notes="device_hammering_historydata_one-分页列表查询")
	 @GetMapping(value = "/listcbcl")
	 public Result<?> queryPageListcbcl(DeviceHammeringHistorydataOne deviceHammeringHistorydataOne,
										@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceHammeringHistorydataOne.getShebeino() == null || deviceHammeringHistorydataOne.getShebeino().isEmpty()) {
			 if (!"null".equals(shebei)) {
				 deviceHammeringHistorydataOne.setShebeino(shebei);
			 } else {
				 deviceHammeringHistorydataOne.setShebeino("空");
			 }
		 }
		 QueryWrapper<DeviceHammeringHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceHammeringHistorydataOne, req.getParameterMap());
		 queryWrapper.eq("chaobiaodengji",1);
		 queryWrapper.ne("overproof_status",20);
		 Page<DeviceHammeringHistorydataOne> page = new Page<DeviceHammeringHistorydataOne>(pageNo, pageSize);
		 IPage<DeviceHammeringHistorydataOne> pageList = deviceHammeringHistorydataOneService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	/**
	 *   添加
	 *
	 * @param deviceHammeringHistorydataOne
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_one-添加")
	@ApiOperation(value="device_hammering_historydata_one-添加", notes="device_hammering_historydata_one-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceHammeringHistorydataOne deviceHammeringHistorydataOne) {
		deviceHammeringHistorydataOneService.save(deviceHammeringHistorydataOne);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加
	  *
	  * @param deviceHammeringHistorydataOne
	  * @return
	  */
	 @AutoLog(value = "device_hammering_historydata_one-添加")
	 @ApiOperation(value="device_hammering_historydata_one-添加", notes="device_hammering_historydata_one-添加")
	 @PostMapping(value = "/addother")
	 public Result<?> addother(@RequestBody DeviceHammeringHistorydataOne deviceHammeringHistorydataOne) {
		 if (StringUtil.isNotEmpty(deviceHammeringHistorydataOne.getShebeino()) && StringUtil.isNotEmpty(deviceHammeringHistorydataOne.getPileNo())) {
			 DeviceHammeringHistorydataOne one = null;
			 try {
				 QueryWrapper<DeviceHammeringHistorydataOne> queryWrapper = new QueryWrapper<>();
				 queryWrapper.eq("shebeino", deviceHammeringHistorydataOne.getShebeino());
				 queryWrapper.eq("pile_no", deviceHammeringHistorydataOne.getPileNo());
				 queryWrapper.eq("pile_mileage", deviceHammeringHistorydataOne.getPileMileage());
				 one = deviceHammeringHistorydataOneService.getOne(queryWrapper);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
			 if (one != null) {
				 deviceHammeringHistorydataOne.setId(one.getId());
				 deviceHammeringHistorydataOneService.updateById(deviceHammeringHistorydataOne);
				 return Result.OK("修改！");
			 } else {
				 deviceHammeringHistorydataOneService.save(deviceHammeringHistorydataOne);
				 return Result.OK("添加成功！");
			 }
		 } else {
			 return Result.error("记录上传失败！请填入设备编号和桩号");
		 }
	 }
	/**
	 *  编辑
	 *
	 * @param deviceHammeringHistorydataOne
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_one-编辑")
	@ApiOperation(value="device_hammering_historydata_one-编辑", notes="device_hammering_historydata_one-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceHammeringHistorydataOne deviceHammeringHistorydataOne) {
		deviceHammeringHistorydataOneService.updateById(deviceHammeringHistorydataOne);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_one-通过id删除")
	@ApiOperation(value="device_hammering_historydata_one-通过id删除", notes="device_hammering_historydata_one-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceHammeringHistorydataOneService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_one-批量删除")
	@ApiOperation(value="device_hammering_historydata_one-批量删除", notes="device_hammering_historydata_one-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceHammeringHistorydataOneService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_one-通过id查询")
	@ApiOperation(value="device_hammering_historydata_one-通过id查询", notes="device_hammering_historydata_one-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceHammeringHistorydataOne deviceHammeringHistorydataOne = deviceHammeringHistorydataOneService.getById(id);
		if(deviceHammeringHistorydataOne==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceHammeringHistorydataOne);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceHammeringHistorydataOne
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceHammeringHistorydataOne deviceHammeringHistorydataOne) {
        return super.exportXls(request, deviceHammeringHistorydataOne, DeviceHammeringHistorydataOne.class, "device_hammering_historydata_one");
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
        return super.importExcel(request, response, DeviceHammeringHistorydataOne.class);
    }

}
