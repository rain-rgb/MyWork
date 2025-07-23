package com.trtm.iot.devicepiplewall.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.devicepiplewall.entity.DevicePiplewallOnetj;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepiplewall.entity.DevicePiplewallOne;
import com.trtm.iot.devicepiplewall.service.IDevicePiplewallOneService;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: device_piplewall_one
 * @Author: jeecg-boot
 * @Date:   2024-12-28
 * @Version: V1.0
 */
@Api(tags="device_piplewall_one")
@RestController
@RequestMapping("/devicepiplewall/devicePiplewallOne")
@Slf4j
public class DevicePiplewallOneController extends JeecgController<DevicePiplewallOne, IDevicePiplewallOneService> {
	@Autowired
	private IDevicePiplewallOneService devicePiplewallOneService;

	 @Autowired
	 private RedisUtil redisUtil;
	 @Value("${jeecg.path.upload}")
	 private String upLoadPath;

	/**
	 * 分页列表查询
	 *
	 * @param devicePiplewallOne
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one-分页列表查询")
	@ApiOperation(value="device_piplewall_one-分页列表查询", notes="device_piplewall_one-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicePiplewallOne devicePiplewallOne,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,

								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (devicePiplewallOne.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				devicePiplewallOne.setShebeino(shebei);
			} else {
				devicePiplewallOne.setShebeino("空");
			}
		}
		QueryWrapper<DevicePiplewallOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePiplewallOne, req.getParameterMap());
		Page<DevicePiplewallOne> page = new Page<DevicePiplewallOne>(pageNo, pageSize);
		IPage<DevicePiplewallOne> pageList = devicePiplewallOneService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "device_piplewall_one-分页列表查询")
	 @ApiOperation(value="device_piplewall_one-分页列表查询", notes="device_piplewall_one-分页列表查询")
	 @GetMapping(value = "/listtj")
	 public Result<?> queryPageListtj(DevicePiplewallOne devicePiplewallOne,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (devicePiplewallOne.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 devicePiplewallOne.setShebeino(shebei);
			 } else {
				 devicePiplewallOne.setShebeino("空");
			 }
		 }
		 int type = devicePiplewallOne.getJiegantimes();
		 devicePiplewallOne.setJiegantimes(null);
		 String permate = " '总计' AS pile_endtime, ";

		 QueryWrapper<DevicePiplewallOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePiplewallOne, req.getParameterMap());

		 if(1 == type){ // 按日统计
			 permate = " DATE_FORMAT( pile_endtime, '%Y-%m-%d' ) AS pile_endtime, ";
			 queryWrapper.groupBy("DATE_FORMAT( pile_endtime, '%Y-%m-%d' )");
		 }else if( 2 == type){// 按月统计
			 permate = " DATE_FORMAT( pile_endtime, '%Y-%m' ) AS pile_endtime, ";
			 queryWrapper.groupBy("DATE_FORMAT( pile_endtime, '%Y-%m' )");
		 }else if(3 == type){// 按年
			 permate = " DATE_FORMAT( pile_endtime, '%Y' ) AS pile_endtime, ";
			 queryWrapper.groupBy("DATE_FORMAT( pile_endtime, '%Y' )");
		 }
		 else{
			 // 不分组统计总数
		 }
		 queryWrapper.select(permate  +
				 " ROUND(SUM( pile_realdep ),2) AS pile_realdep,   " +
				 " count( 1 ) AS down_speed, " +
				 " ROUND(SUM( zongm ),2) AS zongm, " +
				 " count(CASE WHEN chaobiaodengji = 1 THEN 1 END) up_speed");

		 Page<DevicePiplewallOne> page = new Page<DevicePiplewallOne>(pageNo, pageSize);
		 IPage<DevicePiplewallOne> pageList = devicePiplewallOneService.page(page, queryWrapper);
		// List<Map<String, Object>> maps = devicePiplewallOneService.listMaps(queryWrapper);
		 return Result.OK(page);
	 }

	/**
	 *   添加
	 *
	 * @param devicePiplewallOne
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one-添加")
	@ApiOperation(value="device_piplewall_one-添加", notes="device_piplewall_one-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicePiplewallOne devicePiplewallOne) {
		devicePiplewallOneService.save(devicePiplewallOne);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param devicePiplewallOne
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one-编辑")
	@ApiOperation(value="device_piplewall_one-编辑", notes="device_piplewall_one-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicePiplewallOne devicePiplewallOne) {
		devicePiplewallOneService.updateById(devicePiplewallOne);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one-通过id删除")
	@ApiOperation(value="device_piplewall_one-通过id删除", notes="device_piplewall_one-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicePiplewallOneService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one-批量删除")
	@ApiOperation(value="device_piplewall_one-批量删除", notes="device_piplewall_one-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicePiplewallOneService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_piplewall_one-通过id查询")
	@ApiOperation(value="device_piplewall_one-通过id查询", notes="device_piplewall_one-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicePiplewallOne devicePiplewallOne = devicePiplewallOneService.getById(id);
		if(devicePiplewallOne==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicePiplewallOne);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicePiplewallOne
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePiplewallOne devicePiplewallOne) {
        return super.exportXls(request, devicePiplewallOne, DevicePiplewallOne.class, "device_piplewall_one");
    }

	 @RequestMapping(value = "/exportXlstj")
	 public ModelAndView exportXlstj(HttpServletRequest request, DevicePiplewallOne devicePiplewallOne) {
		 return exportXls(request, devicePiplewallOne, DevicePiplewallOnetj.class, "地连墙台账");
	 }
	 protected ModelAndView exportXls(HttpServletRequest request, DevicePiplewallOne devicePiplewallOne, Class<DevicePiplewallOnetj> clazz, String title) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(sysUser.getId() + "change"));
		 //查询到他的设备编号
		 if (devicePiplewallOne.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 devicePiplewallOne.setShebeino(shebei);
			 } else {
				 devicePiplewallOne.setShebeino("空");
			 }
		 }
		 int type = devicePiplewallOne.getJiegantimes();
		 devicePiplewallOne.setJiegantimes(null);
		 String permate = " '总计' AS pile_endtime, ";

		 QueryWrapper<DevicePiplewallOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePiplewallOne, request.getParameterMap());

		 if(1 == type){ // 按日统计
			 permate = " DATE_FORMAT( pile_endtime, '%Y-%m-%d' ) AS pile_endtime, ";
			 queryWrapper.groupBy("DATE_FORMAT( pile_endtime, '%Y-%m-%d' )");
		 }else if( 2 == type){// 按月统计
			 permate = " DATE_FORMAT( pile_endtime, '%Y-%m' ) AS pile_endtime, ";
			 queryWrapper.groupBy("DATE_FORMAT( pile_endtime, '%Y-%m' )");
		 }else if(3 == type){// 按年
			 permate = " DATE_FORMAT( pile_endtime, '%Y' ) AS pile_endtime, ";
			 queryWrapper.groupBy("DATE_FORMAT( pile_endtime, '%Y' )");
		 }
		 else{
			 // 不分组统计总数
		 }
		 queryWrapper.select(permate  +
				 " ROUND(SUM( pile_realdep ),2) AS pile_realdep,   " +
				 " count( 1 ) AS down_speed, " +
				 " ROUND(SUM( zongm ),2) AS zongm, " +
				 " count(CASE WHEN chaobiaodengji = 1 THEN 1 END) up_speed");

		 Page<DevicePiplewallOne> page = new Page<DevicePiplewallOne>(1, 5000);
		 IPage<DevicePiplewallOne> pageList = devicePiplewallOneService.page(page, queryWrapper);
		 List<DevicePiplewallOne> exportList = pageList.getRecords();
		 List<DevicePiplewallOnetj> exportList1 =new ArrayList<DevicePiplewallOnetj>();
		 for(DevicePiplewallOne item :exportList){
			 DevicePiplewallOnetj one = new DevicePiplewallOnetj();
			 BeanUtils.copyProperties(item, one);
			 exportList1.add(one);
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, clazz);
		 //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
		 ExportParams  exportParams=new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
		 exportParams.setImageBasePath(upLoadPath);
		 //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
		 mv.addObject(NormalExcelConstants.PARAMS,exportParams);
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList1);
		 return mv;
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
        return super.importExcel(request, response, DevicePiplewallOne.class);
    }

}
