package com.trtm.iot.carconsumption.controller;

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
import com.trtm.iot.carconsumption.entity.CarConsumption;
import com.trtm.iot.carconsumption.service.ICarConsumptionService;

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
 * @Description: 车辆油费消耗信息表
 * @Author: jeecg-boot
 * @Date:   2022-01-12
 * @Version: V1.0
 */
@Api(tags="车辆油费消耗信息表")
@RestController
@RequestMapping("/carconsumption/carConsumption")
@Slf4j
public class CarConsumptionController extends JeecgController<CarConsumption, ICarConsumptionService> {
	@Autowired
	private ICarConsumptionService carConsumptionService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param carConsumption
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "车辆油费消耗信息表-分页列表查询")
	@ApiOperation(value="车辆油费消耗信息表-分页列表查询", notes="车辆油费消耗信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CarConsumption carConsumption,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (carConsumption.getShebeiNo() == null) {
			if (!"null".equals(shebei)) {
				carConsumption.setShebeiNo(shebei);
			}else {
				carConsumption.setShebeiNo("空");
			}
		}
		QueryWrapper<CarConsumption> queryWrapper = QueryGenerator.initQueryWrapper(carConsumption, req.getParameterMap());
		Page<CarConsumption> page = new Page<CarConsumption>(pageNo, pageSize);
		IPage<CarConsumption> pageList = carConsumptionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "车辆油费消耗信息表-累计金额/油量")
	 @ApiOperation(value="车辆油费消耗信息表-累计金额/油量", notes="车辆油费消耗信息表-累计金额/油量")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(CarConsumption carConsumption,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (carConsumption.getShebeiNo() == null) {
			 if (!"null".equals(shebei)) {
				 carConsumption.setShebeiNo(shebei);
			 }else {
				 carConsumption.setShebeiNo("空");
			 }
		 }
		 QueryWrapper<CarConsumption> queryWrapper = QueryGenerator.initQueryWrapper(carConsumption, req.getParameterMap());
		 queryWrapper.select("ifnull(sum(quantity),0) as quantity,ifnull(sum(money),0) as money,ifnull(sum(mileage),0) as mileage");
		 CarConsumption pageList = carConsumptionService.getOne(queryWrapper);
		 return Result.OK(pageList);
	 }

	 @AutoLog(value = "车辆油费消耗信息表-累计金额/油量按月统计")
	 @ApiOperation(value="车辆油费消耗信息表-累计金额/油量按月统计", notes="车辆油费消耗信息表-累计金额/油量按月统计")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(CarConsumption carConsumption,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (carConsumption.getShebeiNo() == null) {
			 if (!"null".equals(shebei)) {
				 carConsumption.setShebeiNo(shebei);
			 }else {
				 carConsumption.setShebeiNo("空");
			 }
		 }
		 QueryWrapper<CarConsumption> queryWrapper = QueryGenerator.initQueryWrapper(carConsumption, req.getParameterMap());
		 queryWrapper.select("ifnull(sum(quantity),0) as quantity,ifnull(sum(money),0) as money,ifnull(sum(mileage),0) as mileage,DATE_FORMAT(create_time,'%Y-%m') as update_by");
		 queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m')");
		 queryWrapper.orderByDesc("DATE_FORMAT(create_time,'%Y-%m')");
		 List<CarConsumption> pageList = carConsumptionService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	 @AutoLog(value = "车辆油费消耗信息表-累计金额/油量按月统计")
	 @ApiOperation(value="车辆油费消耗信息表-累计金额/油量按月统计", notes="车辆油费消耗信息表-累计金额/油量按月统计")
	 @GetMapping(value = "/list3")
	 public Result<?> queryPageList3(CarConsumption carConsumption,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (carConsumption.getShebeiNo() == null) {
			 if (!"null".equals(shebei)) {
				 carConsumption.setShebeiNo(shebei);
			 }else {
				 carConsumption.setShebeiNo("空");
			 }
		 }
		 QueryWrapper<CarConsumption> queryWrapper = QueryGenerator.initQueryWrapper(carConsumption, req.getParameterMap());
		 queryWrapper.select("ifnull(sum(quantity),0) as quantity,ifnull(sum(money),0) as money,ifnull(sum(mileage),0) as mileage,DATE_FORMAT(create_time,'%Y-%m-%d') as update_by");
		 queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m-%d')");
		 queryWrapper.orderByDesc("DATE_FORMAT(create_time,'%Y-%m-%d')");
		 queryWrapper.last("limit 7");
		 List<CarConsumption> pageList = carConsumptionService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param carConsumption
	 * @return
	 */
	@AutoLog(value = "车辆油费消耗信息表-添加")
	@ApiOperation(value="车辆油费消耗信息表-添加", notes="车辆油费消耗信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CarConsumption carConsumption) {
		carConsumptionService.save(carConsumption);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param carConsumption
	 * @return
	 */
	@AutoLog(value = "车辆油费消耗信息表-编辑")
	@ApiOperation(value="车辆油费消耗信息表-编辑", notes="车辆油费消耗信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CarConsumption carConsumption) {
		carConsumptionService.updateById(carConsumption);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车辆油费消耗信息表-通过id删除")
	@ApiOperation(value="车辆油费消耗信息表-通过id删除", notes="车辆油费消耗信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		carConsumptionService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "车辆油费消耗信息表-批量删除")
	@ApiOperation(value="车辆油费消耗信息表-批量删除", notes="车辆油费消耗信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.carConsumptionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车辆油费消耗信息表-通过id查询")
	@ApiOperation(value="车辆油费消耗信息表-通过id查询", notes="车辆油费消耗信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CarConsumption carConsumption = carConsumptionService.getById(id);
		if(carConsumption==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(carConsumption);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param carConsumption
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CarConsumption carConsumption) {
        return super.exportXls(request, carConsumption, CarConsumption.class, "车辆油费消耗信息表");
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
        return super.importExcel(request, response, CarConsumption.class);
    }

}
