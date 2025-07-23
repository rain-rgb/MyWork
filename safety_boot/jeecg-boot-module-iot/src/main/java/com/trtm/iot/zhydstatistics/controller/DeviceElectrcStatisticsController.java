package com.trtm.iot.zhydstatistics.controller;

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

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zhydstatistics.entity.DeviceElectrcStatistics;
import com.trtm.iot.zhydstatistics.service.IDeviceElectrcStatisticsService;

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
 * @Description: 智慧用电历史数据统计表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-02
 * @Version: V1.0
 */
@Api(tags="智慧用电历史数据统计表信息")
@RestController
@RequestMapping("/zhydstatistics/deviceElectrcStatistics")
@Slf4j
public class DeviceElectrcStatisticsController extends JeecgController<DeviceElectrcStatistics, IDeviceElectrcStatisticsService> {
	@Autowired
	private IDeviceElectrcStatisticsService deviceElectrcStatisticsService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param deviceElectrcStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据统计表信息-分页列表查询")
	@ApiOperation(value="智慧用电历史数据统计表信息-分页列表查询", notes="智慧用电历史数据统计表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceElectrcStatistics deviceElectrcStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (deviceElectrcStatistics.getImei() == null) {
			if (!"null".equals(shebei)) {
				deviceElectrcStatistics.setImei(shebei);
			}else {
				deviceElectrcStatistics.setImei("空");
			}
		}
		QueryWrapper<DeviceElectrcStatistics> queryWrapper = QueryGenerator.initQueryWrapper(deviceElectrcStatistics, req.getParameterMap());
		Page<DeviceElectrcStatistics> page = new Page<DeviceElectrcStatistics>(pageNo, pageSize);
		IPage<DeviceElectrcStatistics> pageList = deviceElectrcStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 超标统计
	  *
	  * @param deviceElectrcStatistics
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "智慧用电历史数据统计表信息-超标统计")
	 @ApiOperation(value="智慧用电历史数据统计表信息-超标统计", notes="智慧用电历史数据统计表信息-超标统计")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(DeviceElectrcStatistics deviceElectrcStatistics,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (deviceElectrcStatistics.getImei() == null) {
			 if (!"null".equals(shebei)) {
				 deviceElectrcStatistics.setImei(shebei);
			 }else {
				 deviceElectrcStatistics.setImei("空");
			 }
		 }
		 QueryWrapper<DeviceElectrcStatistics> queryWrapper = QueryGenerator.initQueryWrapper(deviceElectrcStatistics, req.getParameterMap());
		 queryWrapper.select("sum(voltage) voltage,sum(electricity) electricity,sum(seepelectricity) seepelectricity,sum(temp) temp");
		 DeviceElectrcStatistics pageList = deviceElectrcStatisticsService.getOne(queryWrapper);
		 Map map = new HashMap();
		 Integer vchaobiaosum = 0;//电压超标总数
		 Integer echaobiaosum = 0;//电流超标总数
		 Integer schaobiaosum = 0;//漏电流超标总数
		 Integer tchaobiaosum = 0;//温度超标总数
		 if (pageList!=null) {
		 	if (!StringUtils.isEmpty(pageList.getVoltage())) {
				vchaobiaosum = pageList.getVoltage();
			}
		 	if (!StringUtils.isEmpty(pageList.getElectricity())) {
				echaobiaosum = pageList.getElectricity();
			}
		 	if (!StringUtils.isEmpty(pageList.getSeepelectricity())) {
				schaobiaosum = pageList.getSeepelectricity();
			}
		 	if (!StringUtils.isEmpty(pageList.getTemp())) {
				tchaobiaosum = pageList.getTemp();
			}
		 }
		 map.put("vchaobiaosum",vchaobiaosum);
		 map.put("echaobiaosum",echaobiaosum);
		 map.put("schaobiaosum",schaobiaosum);
		 map.put("tchaobiaosum",tchaobiaosum);
		 return Result.OK(map);
	 }

	/**
	 *   添加
	 *
	 * @param deviceElectrcStatistics
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据统计表信息-添加")
	@ApiOperation(value="智慧用电历史数据统计表信息-添加", notes="智慧用电历史数据统计表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceElectrcStatistics deviceElectrcStatistics) {
		deviceElectrcStatisticsService.save(deviceElectrcStatistics);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param deviceElectrcStatistics
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据统计表信息-编辑")
	@ApiOperation(value="智慧用电历史数据统计表信息-编辑", notes="智慧用电历史数据统计表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceElectrcStatistics deviceElectrcStatistics) {
		deviceElectrcStatisticsService.updateById(deviceElectrcStatistics);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据统计表信息-通过id删除")
	@ApiOperation(value="智慧用电历史数据统计表信息-通过id删除", notes="智慧用电历史数据统计表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceElectrcStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据统计表信息-批量删除")
	@ApiOperation(value="智慧用电历史数据统计表信息-批量删除", notes="智慧用电历史数据统计表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceElectrcStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据统计表信息-通过id查询")
	@ApiOperation(value="智慧用电历史数据统计表信息-通过id查询", notes="智慧用电历史数据统计表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceElectrcStatistics deviceElectrcStatistics = deviceElectrcStatisticsService.getById(id);
		if(deviceElectrcStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceElectrcStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceElectrcStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceElectrcStatistics deviceElectrcStatistics) {
        return super.exportXls(request, deviceElectrcStatistics, DeviceElectrcStatistics.class, "智慧用电历史数据统计表信息");
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
        return super.importExcel(request, response, DeviceElectrcStatistics.class);
    }

}
