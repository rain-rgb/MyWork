package com.trtm.iot.clgl.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.clgl.entity.FrontDeviceHistorydata;
import com.trtm.iot.clgl.service.IFrontDeviceHistorydataService;

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

import static com.trtm.iot.util.GPSUtil.*;

/**
 * @Description: 车辆信息历史数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-13
 * @Version: V1.0
 */
@Api(tags="车辆信息历史数据表")
@RestController
@RequestMapping("/clgl/frontDeviceHistorydata")
@Slf4j
public class FrontDeviceHistorydataController extends JeecgController<FrontDeviceHistorydata, IFrontDeviceHistorydataService> {
	@Autowired
	private IFrontDeviceHistorydataService frontDeviceHistorydataService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param frontDeviceHistorydata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "车辆信息历史数据表-分页列表查询")
	@ApiOperation(value="车辆信息历史数据表-分页列表查询", notes="车辆信息历史数据表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FrontDeviceHistorydata frontDeviceHistorydata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if(frontDeviceHistorydata.getShebeiNo()==null){
			if (shebei != null) {
				frontDeviceHistorydata.setShebeiNo(shebei);
			}
		}
		QueryWrapper<FrontDeviceHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(frontDeviceHistorydata, req.getParameterMap());
		queryWrapper.isNotNull("longitude");
		queryWrapper.isNotNull("latitude");
		Page<FrontDeviceHistorydata> page = new Page<FrontDeviceHistorydata>(pageNo, pageSize);
		IPage<FrontDeviceHistorydata> pageList = frontDeviceHistorydataService.page(page,queryWrapper);
		List<FrontDeviceHistorydata> records= pageList.getRecords();
		for(FrontDeviceHistorydata frontDeviceHistorydata1 : records){
			double lon = Double.parseDouble(String.valueOf(frontDeviceHistorydata1.getLongitude()));
			double lat = Double.parseDouble(String.valueOf(frontDeviceHistorydata1.getLatitude()));
			double lat1 = formatLnglat(lat);
			double lon1 = formatLnglat(lon);
			double[] zuobiao = gps84_To_Gcj02(lat1, lon1);
			double lat2 = retain6(zuobiao[0]);
			double lon2 = retain6(zuobiao[1]);
			frontDeviceHistorydata1.setLongitude(BigDecimal.valueOf(lon2));
			frontDeviceHistorydata1.setLatitude(BigDecimal.valueOf(lat2));
		}
		return Result.OK(pageList);
	}
	/**
	 * 列表查询
	 *
	 * @param frontDeviceHistorydata
	 * @param
	 * @param
	 * @param req
	 * @return
	 */
	@AutoLog(value = "车辆信息历史数据表-分页列表查询")
	@ApiOperation(value="车辆信息历史数据表-分页列表查询", notes="车辆信息历史数据表-分页列表查询")
	@GetMapping(value = "/lists")
	public Result<?> queryPageList1(FrontDeviceHistorydata frontDeviceHistorydata,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if(frontDeviceHistorydata.getShebeiNo()==null){
			if (shebei != null) {
				frontDeviceHistorydata.setShebeiNo(shebei);
			}
		}
		QueryWrapper<FrontDeviceHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(frontDeviceHistorydata, req.getParameterMap());
		queryWrapper.isNotNull("longitude");
		queryWrapper.isNotNull("latitude");
		queryWrapper.ne("latitude",0);
		queryWrapper.ne("longitude",0);
		List<FrontDeviceHistorydata> list = frontDeviceHistorydataService.list(queryWrapper);
		for(FrontDeviceHistorydata frontDeviceHistorydata1 : list){
			double lon = Double.parseDouble(String.valueOf(frontDeviceHistorydata1.getLongitude()));
			double lat = Double.parseDouble(String.valueOf(frontDeviceHistorydata1.getLatitude()));
			Integer integer = Integer.valueOf((int) lon);
			String sa=""+integer;
			if(sa.length()>3){
				double lat1 = formatLnglat(lat);
				double lon1 = formatLnglat(lon);
				double [] zuobiao = gps84_To_Gcj02(lat1,lon1);
				double lat2 = retain6(zuobiao[0]);
				double lon2 = retain6(zuobiao[1]);
				frontDeviceHistorydata1.setLongitude(BigDecimal.valueOf(lon2));
				frontDeviceHistorydata1.setLatitude(BigDecimal.valueOf(lat2));
			}
		}
		return Result.OK(list);
	}

	/**
	 * 列表查询
	 *
	 * @param frontDeviceHistorydata
	 * @param
	 * @param
	 * @param req
	 * @return
	 */
	@AutoLog(value = "车辆信息历史数据表-分页列表查询")
	@ApiOperation(value="车辆信息历史数据表-分页列表查询", notes="车辆信息历史数据表-分页列表查询")
	@GetMapping(value = "/listData")
	public Result<?> queryPageListData(FrontDeviceHistorydata frontDeviceHistorydata,
									HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if(frontDeviceHistorydata.getShebeiNo()==null){
			if (shebei != null) {
				frontDeviceHistorydata.setShebeiNo(shebei);
			}
		}
		QueryWrapper<FrontDeviceHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(frontDeviceHistorydata, req.getParameterMap());
		queryWrapper.isNotNull("longitude");
		queryWrapper.isNotNull("latitude");
		queryWrapper.ne("latitude",0);
		queryWrapper.ne("longitude",0);
		List<FrontDeviceHistorydata> list = frontDeviceHistorydataService.list(queryWrapper);
//		for(FrontDeviceHistorydata frontDeviceHistorydata1 : list){
//			double lon = Double.parseDouble(String.valueOf(frontDeviceHistorydata1.getLongitude()));
//			double lat = Double.parseDouble(String.valueOf(frontDeviceHistorydata1.getLatitude()));
//			Integer integer = Integer.valueOf((int) lon);
//			String sa=""+integer;
//			if(sa.length()>3){
//				double lat1 = formatLnglat(lat);
//				double lon1 = formatLnglat(lon);
//				double [] zuobiao = gps84_To_Gcj02(lat1,lon1);
//				double lat2 = retain6(zuobiao[0]);
//				double lon2 = retain6(zuobiao[1]);
//				frontDeviceHistorydata1.setLongitude(BigDecimal.valueOf(lon2));
//				frontDeviceHistorydata1.setLatitude(BigDecimal.valueOf(lat2));
//			}
//		}
		return Result.OK(list);
	}

	@AutoLog(value = "车辆轨迹请求token")
	@ApiOperation(value="车辆轨迹请求token", notes="车辆轨迹请求token")
	@PostMapping(value = "/tokens")
	public Result<?> queryPageListtoken(@RequestBody FrontDeviceHistorydata frontDeviceHistorydata,
									HttpServletRequest req) {
	//	List<Map> list = frontDeviceHistorydataService.selectgetHistoryMByMUtc(frontDeviceHistorydata.getShebeiNo(), frontDeviceHistorydata.getProjectId(), frontDeviceHistorydata.getUploadtime());
		Map map = frontDeviceHistorydataService.selectToken(frontDeviceHistorydata.getShebeiNo(),frontDeviceHistorydata.getProjectId(),frontDeviceHistorydata.getUploadtime());
		return Result.OK(map);
	}

	@AutoLog(value = "车辆轨迹新看板")
	@ApiOperation(value="车辆轨迹新看板", notes="车辆轨迹新看板")
	@PostMapping(value = "/tokenlsyl")
	public Result<?> queryPageListtokenlsyl(@RequestBody FrontDeviceHistorydata frontDeviceHistorydata,
										HttpServletRequest req) {
		List<Map> list = frontDeviceHistorydataService.selectgetHistoryMByMUtc(frontDeviceHistorydata.getShebeiNo(), frontDeviceHistorydata.getProjectId(), frontDeviceHistorydata.getUploadtime());
		return Result.OK(list);
	}

	/**
	 * 分页列表查询
	 *
	 * @param frontDeviceHistorydata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "摊铺碾压数据监测主表-分页列表查询")
	@ApiOperation(value="摊铺碾压数据监测主表-分页列表查询", notes="摊铺碾压数据监测主表-分页列表查询")
	@GetMapping(value = "/list1")
	public Result<?> queryPageList1(FrontDeviceHistorydata frontDeviceHistorydata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if(frontDeviceHistorydata.getShebeiNo()==null) {
			if (shebei != null) {
				frontDeviceHistorydata.setShebeiNo(shebei);
			}
		}
		QueryWrapper<FrontDeviceHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(frontDeviceHistorydata, req.getParameterMap());
		queryWrapper.inSql("device_type","select device_type from front_device_realdata where device_type='A' or device_type='B'");
		Page<FrontDeviceHistorydata> page = new Page<FrontDeviceHistorydata>(pageNo, pageSize);
		IPage<FrontDeviceHistorydata> pageList = frontDeviceHistorydataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	/**
	 *   添加
	 *
	 * @param frontDeviceHistorydata
	 * @return
	 */
	@AutoLog(value = "车辆信息历史数据表-添加")
	@ApiOperation(value="车辆信息历史数据表-添加", notes="车辆信息历史数据表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FrontDeviceHistorydata frontDeviceHistorydata) {
		frontDeviceHistorydataService.save(frontDeviceHistorydata);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param frontDeviceHistorydata
	 * @return
	 */
	@AutoLog(value = "车辆信息历史数据表-编辑")
	@ApiOperation(value="车辆信息历史数据表-编辑", notes="车辆信息历史数据表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FrontDeviceHistorydata frontDeviceHistorydata) {
		frontDeviceHistorydataService.updateById(frontDeviceHistorydata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车辆信息历史数据表-通过id删除")
	@ApiOperation(value="车辆信息历史数据表-通过id删除", notes="车辆信息历史数据表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		frontDeviceHistorydataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "车辆信息历史数据表-批量删除")
	@ApiOperation(value="车辆信息历史数据表-批量删除", notes="车辆信息历史数据表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.frontDeviceHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车辆信息历史数据表-通过id查询")
	@ApiOperation(value="车辆信息历史数据表-通过id查询", notes="车辆信息历史数据表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FrontDeviceHistorydata frontDeviceHistorydata = frontDeviceHistorydataService.getById(id);
		if(frontDeviceHistorydata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(frontDeviceHistorydata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param frontDeviceHistorydata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FrontDeviceHistorydata frontDeviceHistorydata) {
        return super.exportXls(request, frontDeviceHistorydata, FrontDeviceHistorydata.class, "车辆信息历史数据表");
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
        return super.importExcel(request, response, FrontDeviceHistorydata.class);
    }

}
