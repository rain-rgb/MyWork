package com.trtm.iot.car.controller;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.car.entity.ExcelCar;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileHistorydataOneXls;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.car.entity.CarMiles;
import com.trtm.iot.car.service.ICarMilesService;

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

import static org.jeecg.common.util.DateUtils.date2Str;
import static org.jeecg.common.util.DateUtils.dateformat;

/**
 * @Description: car_miles
 * @Author: jeecg-boot
 * @Date:   2022-03-16
 * @Version: V1.0
 */
@Api(tags="car_miles")
@RestController
@RequestMapping("/car/carMiles")
@Slf4j
public class CarMilesController extends JeecgController<CarMiles, ICarMilesService> {
	@Autowired
	private ICarMilesService carMilesService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IShebeiInfoService shebeiInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param carMiles
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "car_miles-分页列表查询")
	@ApiOperation(value="car_miles-分页列表查询", notes="car_miles-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CarMiles carMiles,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (carMiles.getShebeiNo()== null) {
			if (!"null".equals(shebei)) {
				carMiles.setShebeiNo(shebei);
			} else {
				carMiles.setShebeiNo("空");
			}
		}
		QueryWrapper<CarMiles> queryWrapper = QueryGenerator.initQueryWrapper(carMiles, req.getParameterMap());
		Page<CarMiles> page = new Page<CarMiles>(pageNo, pageSize);
		IPage<CarMiles> pageList = carMilesService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	@AutoLog(value = "car_miles-分页列表查询")
	@ApiOperation(value="car_miles-分页列表查询", notes="car_miles-分页列表查询")
	@GetMapping(value = "/list1")
	public Result<?> queryPageList1(CarMiles carMiles,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (carMiles.getShebeiNo()== null) {
			if (!"null".equals(shebei)) {
				carMiles.setShebeiNo(shebei);
			} else {
				carMiles.setShebeiNo("空");
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
		String day1 = format.format(c.getTime());
		Date parse = null;//本月第一天
		try {
			parse = format.parse(day1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		QueryWrapper<CarMiles> queryWrapper = QueryGenerator.initQueryWrapper(carMiles, req.getParameterMap());
		queryWrapper.select("sum(miles)as miles,sum(runTime) as runTime,shebei_no");
		queryWrapper.gt("rundate",parse);
		queryWrapper.groupBy("shebei_no");
		queryWrapper.orderByDesc("miles");
		List<CarMiles> pageList = carMilesService.list(queryWrapper);
		for (CarMiles miles : pageList) {
			String shebeiNo = miles.getShebeiNo();
			ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
			miles.setShebeiNo(selectshebeione.getSbname());
			miles.setDriverPic(selectshebeione.getVideolive());
		}
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param carMiles
	 * @return
	 */
	@AutoLog(value = "car_miles-添加")
	@ApiOperation(value="car_miles-添加", notes="car_miles-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CarMiles carMiles) {
		carMilesService.save(carMiles);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param carMiles
	 * @return
	 */
	@AutoLog(value = "car_miles-编辑")
	@ApiOperation(value="car_miles-编辑", notes="car_miles-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CarMiles carMiles) {
		carMilesService.updateById(carMiles);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "car_miles-通过id删除")
	@ApiOperation(value="car_miles-通过id删除", notes="car_miles-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		carMilesService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "car_miles-批量删除")
	@ApiOperation(value="car_miles-批量删除", notes="car_miles-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.carMilesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "car_miles-通过id查询")
	@ApiOperation(value="car_miles-通过id查询", notes="car_miles-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CarMiles carMiles = carMilesService.getById(id);
		if(carMiles==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(carMiles);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param carMiles
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CarMiles carMiles) {
        return super.exportXls(request, carMiles, CarMiles.class, "car_miles");
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
        return super.importExcel(request, response, CarMiles.class);
    }

	/**
	 * 导出gps车辆检测记录信息表 excel
	 *
	 */
	@GetMapping(value = "/mixpileexportapiXls")
	public Result<?> exportapiXls(String start, String end, String shebeino) throws ParseException, UnsupportedEncodingException {
		//最终返回
		ExcelCar excelCar = new ExcelCar();

//		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//查询到他的设备编号
//		String[] split = shebei.split(",");
//		List<String> shebeilist = new ArrayList<>();
//		Collections.addAll(shebeilist, split);
		QueryWrapper<CarMiles> queryWrapper = new QueryWrapper<>();
		if (!"undefined".equals(shebeino)&&!"".equals(shebeino)&&shebeino!=null){
			queryWrapper.eq("shebei_no",shebeino);
			ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeino);
			excelCar.setSbName(selectshebeione.getSbname());
			excelCar.setSbType("运输车");
		}




		List data=new ArrayList<>();

		if(!org.springframework.util.StringUtils.isEmpty(start) && !org.springframework.util.StringUtils.isEmpty(end) && !"undefined".equals(start) && !"undefined".equals(end) ){
			queryWrapper.between("rundate",sdf.parse(start),sdf.parse(end));
		}
		List<CarMiles> pageList = carMilesService.list(queryWrapper);

		int runday = 0;//启用天数
		int runtime = 0;//运转时间（s）
		Double runkm = 0.0;//运转时间（s）
		for(CarMiles one:pageList){
			runday=runday+1;
			runtime=runtime+one.getRuntime();
			runkm=runkm+one.getMiles();


		}
		excelCar.setRunDay(runday);//启用天数
		excelCar.setRunTime((runtime/3600));//运转时间（h）
		excelCar.setRunKm(runkm);//运转公里（KM）
		int day = (int)((sdf.parse(end).getTime()-sdf.parse(start).getTime())/(1000*3600*24));
		String chuqinlv=(runtime/3600)/day*8*100+"%";
		excelCar.setChuQinLv(chuqinlv);
		String liyonglv="0%";
		if(runday!=0){
			liyonglv=((runtime/3600)/24/runday*100)+"%";
		}
		excelCar.setLiYongLv(liyonglv);
		excelCar.setStart(start);
		excelCar.setEnd(end);
		excelCar.setLiYongLv(liyonglv);
		data.add(excelCar);
		return Result.OKs(data);
	}

}
