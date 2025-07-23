package com.trtm.iot.car.controller;

import java.text.SimpleDateFormat;
import java.util.*;
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
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.car.entity.CarDispatch;
import com.trtm.iot.car.service.ICarDispatchService;

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
 * @Description: car_dispatch
 * @Author: jeecg-boot
 * @Date:   2021-11-30
 * @Version: V1.0
 */
@Api(tags="car_dispatch")
@RestController
@RequestMapping("/car/carDispatch")
@Slf4j
public class CarDispatchController extends JeecgController<CarDispatch, ICarDispatchService> {
	@Autowired
	private ICarDispatchService carDispatchService;

	/**
	 * 分页列表查询
	 *
	 * @param carDispatch
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "car_dispatch-分页列表查询")
	@ApiOperation(value="car_dispatch-分页列表查询", notes="car_dispatch-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CarDispatch carDispatch,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,String sys_depart_orgcode) {
		QueryWrapper<CarDispatch> queryWrapper = QueryGenerator.initQueryWrapper(carDispatch, req.getParameterMap());
		if(!"null".equals(sys_depart_orgcode)){
			queryWrapper.likeRight("sys_org_code",sys_depart_orgcode);
		}
		Page<CarDispatch> page = new Page<CarDispatch>(pageNo, pageSize);
		IPage<CarDispatch> pageList = carDispatchService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param carDispatch
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "car_dispatch-分页列表查询")
	 @ApiOperation(value="car_dispatch-分页列表查询", notes="car_dispatch-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(CarDispatch carDispatch,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req,String sys_depart_orgcode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 QueryWrapper<CarDispatch> queryWrapper = QueryGenerator.initQueryWrapper(carDispatch, req.getParameterMap());
		 queryWrapper.eq("update_by",loginUser.getUsername());
		 Page<CarDispatch> page = new Page<CarDispatch>(pageNo, pageSize);
		 IPage<CarDispatch> pageList = carDispatchService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }


	 /**
	  * 列表查询
	  *
	  * @param carDispatch
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "列表查询")
	 @ApiOperation(value="列表查询", notes="列表查询")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(CarDispatch carDispatch,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req,String sys_depart_orgcode) {
		 QueryWrapper<CarDispatch> queryWrapper = QueryGenerator.initQueryWrapper(carDispatch, req.getParameterMap());
		 queryWrapper.groupBy("mileage");
		 queryWrapper.groupBy("update_by");
		 List<CarDispatch> list = carDispatchService.list(queryWrapper);
		 return Result.OK(list);
	 }


	 /**
	  * 列表查询
	  *
	  * @param carDispatch
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "列表查询")
	 @ApiOperation(value="列表查询", notes="列表查询")
	 @GetMapping(value = "/list3")
	 public Result<?> queryPageList3(CarDispatch carDispatch,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req,String sys_depart_orgcode) {
		 QueryWrapper<CarDispatch> queryWrapper = QueryGenerator.initQueryWrapper(carDispatch, req.getParameterMap());
		 queryWrapper.select("count(*) as mileage,update_by");
		 queryWrapper.groupBy("update_by");
		 List<CarDispatch> list = carDispatchService.list(queryWrapper);
		 return Result.OK(list);
	 }

	 /**
	  * 列表查询
	  *
	  * @param carDispatch
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "列表查询")
	 @ApiOperation(value="列表查询", notes="列表查询")
	 @GetMapping(value = "/list4")
	 public Result<?> queryPageList4(CarDispatch carDispatch,
									 HttpServletRequest req,String sys_depart_orgcode) {
		 QueryWrapper<CarDispatch> queryWrapper = QueryGenerator.initQueryWrapper(carDispatch, req.getParameterMap());
		 if(!"null".equals(sys_depart_orgcode)){
			 queryWrapper.likeRight("sys_org_code",sys_depart_orgcode);
		 }
		 List<CarDispatch> list = carDispatchService.list(queryWrapper);
		 return Result.OK(list);
	 }

	 /**
	  * 月统计列表查询
	  *
	  * @param carDispatch
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "月统计列表查询")
	 @ApiOperation(value="月统计列表查询", notes="月统计列表查询")
	 @GetMapping(value = "/list5")
	 public Result<?> queryPageList5(CarDispatch carDispatch,
									 HttpServletRequest req,String sys_depart_orgcode) {
		 QueryWrapper<CarDispatch> queryWrapper = QueryGenerator.initQueryWrapper(carDispatch, req.getParameterMap());
		 queryWrapper.select("count(*) as mileage,DATE_FORMAT(create_time,'%Y-%m')as uuid,update_by");
		 if(!"null".equals(sys_depart_orgcode)){
			 queryWrapper.likeRight("sys_org_code",sys_depart_orgcode);
		 }
		 queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m'),update_by");
		 List<CarDispatch> list = carDispatchService.list(queryWrapper);
		 return Result.OK(list);
	 }


	 /**
	  * 周统计列表查询
	  *
	  * @param carDispatch
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "周统计列表查询")
	 @ApiOperation(value="周统计列表查询", notes="周统计列表查询")
	 @GetMapping(value = "/list6")
	 public Result<?> queryPageList6(CarDispatch carDispatch,
									 HttpServletRequest req,String sys_depart_orgcode) {
		 QueryWrapper<CarDispatch> queryWrapper = QueryGenerator.initQueryWrapper(carDispatch, req.getParameterMap());
		 queryWrapper.select("count(*) as mileage,DATE_FORMAT(create_time,'%u')as uuid,update_by");
		 if(!"null".equals(sys_depart_orgcode)){
			 queryWrapper.likeRight("sys_org_code",sys_depart_orgcode);
		 }
		 queryWrapper.groupBy("DATE_FORMAT(create_time,'%u'),update_by");
		 List<CarDispatch> list = carDispatchService.list(queryWrapper);
		 return Result.OK(list);
	 }
	/**
	 *   添加
	 *
	 * @param carDispatch
	 * @return
	 */
	@AutoLog(value = "car_dispatch-添加")
	@ApiOperation(value="car_dispatch-添加", notes="car_dispatch-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CarDispatch carDispatch) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		carDispatch.setUuid(uuid);
		carDispatchService.save(carDispatch);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param carDispatch
	 * @return
	 */
	@AutoLog(value = "car_dispatch-编辑")
	@ApiOperation(value="car_dispatch-编辑", notes="car_dispatch-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CarDispatch carDispatch) {
		carDispatchService.updateById(carDispatch);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "car_dispatch-通过id删除")
	@ApiOperation(value="car_dispatch-通过id删除", notes="car_dispatch-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		carDispatchService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "car_dispatch-批量删除")
	@ApiOperation(value="car_dispatch-批量删除", notes="car_dispatch-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.carDispatchService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "car_dispatch-通过id查询")
	@ApiOperation(value="car_dispatch-通过id查询", notes="car_dispatch-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CarDispatch carDispatch = carDispatchService.getById(id);
		if(carDispatch==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(carDispatch);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param carDispatch
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CarDispatch carDispatch) {
        return super.exportXls(request, carDispatch, CarDispatch.class, "car_dispatch");
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
        return super.importExcel(request, response, CarDispatch.class);
    }

}
