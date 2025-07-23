package com.trtm.iot.devicepipepilestatistics.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepipepilestatistics.entity.DevicePipepileStatistics;
import com.trtm.iot.devicepipepilestatistics.service.IDevicePipepileStatisticsService;

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
 * @Description: device_pipepile_statistics
 * @Author: jeecg-boot
 * @Date:   2022-09-20
 * @Version: V1.0
 */
@Api(tags="device_pipepile_statistics")
@RestController
@RequestMapping("/devicepipepilestatistics/devicePipepileStatistics")
@Slf4j
public class DevicePipepileStatisticsController extends JeecgController<DevicePipepileStatistics, IDevicePipepileStatisticsService> {
	@Autowired
	private IDevicePipepileStatisticsService devicePipepileStatisticsService;
	 @Autowired
	 private IDevicePipepileHistorydataOneService devicePipepileHistorydataOneService;

	 @Autowired
	 RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param devicePipepileStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_pipepile_statistics-分页列表查询")
	@ApiOperation(value="device_pipepile_statistics-分页列表查询", notes="device_pipepile_statistics-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicePipepileStatistics devicePipepileStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevicePipepileStatistics> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileStatistics, req.getParameterMap());
		Page<DevicePipepileStatistics> page = new Page<DevicePipepileStatistics>(pageNo, pageSize);
		IPage<DevicePipepileStatistics> pageList = devicePipepileStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param devicePipepileStatistics
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_pipepile_statistics-分页列表查询")
	 @ApiOperation(value="device_pipepile_statistics-分页列表查询", notes="device_pipepile_statistics-分页列表查询")
	 @GetMapping(value = "/listXq")
	 public Result<?> queryPageListXq(DevicePipepileStatistics devicePipepileStatistics,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (devicePipepileStatistics.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 devicePipepileStatistics.setShebeino(shebei);
			 } else {
				 devicePipepileStatistics.setShebeino("空");
			 }
		 }
		 QueryWrapper<DevicePipepileStatistics> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileStatistics, req.getParameterMap());
		 queryWrapper.select("id,count(*) mileageid,stadate,sum(pilecount) pilecount,sum(worklength) worklength");
		 queryWrapper.groupBy("stadate");
		 Page<DevicePipepileStatistics> page = new Page<DevicePipepileStatistics>(pageNo, pageSize);
		 IPage<DevicePipepileStatistics> pageList = devicePipepileStatisticsService.page(page, queryWrapper);
		 List<DevicePipepileStatistics> records = pageList.getRecords();
		 for (DevicePipepileStatistics record :records){
			 String stadate = record.getStadate();
			 List<DevicePipepileHistorydataOne> list = devicePipepileHistorydataOneService.stadate(stadate+"%");
			 if (list.size() > 0){
				 int i = 0;
				 String sj = null;
				 for (DevicePipepileHistorydataOne l :list){
					 if (l.getPileWorktime().contains("时")){
						 String s = l.getPileWorktime().substring(0, 2);
						 String f = l.getPileWorktime().substring(3, 5);
						 String m = l.getPileWorktime().substring(6, 8);

						 int i1 = Integer.parseInt(s);
						 int i2 = Integer.parseInt(f);
						 int i3 = Integer.parseInt(m);

						 i = i1*60*60 + i2*60 + i3 + i;
					 }else {
						 String f = l.getPileWorktime().substring(0, 2);
						 String m = l.getPileWorktime().substring(3, 5);

						 int i1 = Integer.parseInt(f);
						 int i2 = Integer.parseInt(m);

						 i = i1*60 + i2 + i;
					 }
				 }
				 if (i >= 3600){
					 int i1 = i / 3600;
					 int i2 = i % 3600;

					 int i3 = i2 / 60;
					 int i4 = i2 % 60;
					 sj = i1 + "时" + i3 + "分" + i4 + "秒";
				 }else {
					 int i3 = i / 60;
					 int i4 = i % 60;
					 sj = i3 + "分" + i4 + "秒";
				 }
				 record.setUtilname(sj);
			 }
		 }
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param devicePipepileStatistics
	 * @return
	 */
	@AutoLog(value = "device_pipepile_statistics-添加")
	@ApiOperation(value="device_pipepile_statistics-添加", notes="device_pipepile_statistics-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicePipepileStatistics devicePipepileStatistics) {
		devicePipepileStatisticsService.save(devicePipepileStatistics);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devicePipepileStatistics
	 * @return
	 */
	@AutoLog(value = "device_pipepile_statistics-编辑")
	@ApiOperation(value="device_pipepile_statistics-编辑", notes="device_pipepile_statistics-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicePipepileStatistics devicePipepileStatistics) {
		devicePipepileStatisticsService.updateById(devicePipepileStatistics);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_pipepile_statistics-通过id删除")
	@ApiOperation(value="device_pipepile_statistics-通过id删除", notes="device_pipepile_statistics-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicePipepileStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_pipepile_statistics-批量删除")
	@ApiOperation(value="device_pipepile_statistics-批量删除", notes="device_pipepile_statistics-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicePipepileStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_pipepile_statistics-通过id查询")
	@ApiOperation(value="device_pipepile_statistics-通过id查询", notes="device_pipepile_statistics-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicePipepileStatistics devicePipepileStatistics = devicePipepileStatisticsService.getById(id);
		if(devicePipepileStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicePipepileStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicePipepileStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePipepileStatistics devicePipepileStatistics) {
        return super.exportXls(request, devicePipepileStatistics, DevicePipepileStatistics.class, "device_pipepile_statistics");
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
        return super.importExcel(request, response, DevicePipepileStatistics.class);
    }

}
