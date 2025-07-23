package com.trtm.iot.devicehistorystatic.controller;

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
import com.trtm.iot.devicehistorystatic.entity.DevicehistoryStatic;
import com.trtm.iot.devicehistorystatic.service.IDevicehistoryStaticService;

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
 * @Description: 环境监测数据统计表
 * @Author: jeecg-boot
 * @Date:   2022-07-09
 * @Version: V1.0
 */
@Api(tags="环境监测数据统计表")
@RestController
@RequestMapping("/devicehistorystatic/devicehistoryStatic")
@Slf4j
public class DevicehistoryStaticController extends JeecgController<DevicehistoryStatic, IDevicehistoryStaticService> {
	@Autowired
	private IDevicehistoryStaticService devicehistoryStaticService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param devicehistoryStatic
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "环境监测数据统计表-分页列表查询")
	@ApiOperation(value="环境监测数据统计表-分页列表查询", notes="环境监测数据统计表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicehistoryStatic devicehistoryStatic,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (devicehistoryStatic.getDevaddr() == null) {
			if (!"null".equals(shebei)) {
				devicehistoryStatic.setDevaddr(shebei);
			} else {
				devicehistoryStatic.setDevaddr("空");
			}
		}
		QueryWrapper<DevicehistoryStatic> queryWrapper = QueryGenerator.initQueryWrapper(devicehistoryStatic, req.getParameterMap());
		Page<DevicehistoryStatic> page = new Page<DevicehistoryStatic>(pageNo, pageSize);
		IPage<DevicehistoryStatic> pageList = devicehistoryStaticService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 统计超标数
	  *
	  * @param devicehistoryStatic
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "环境监测数据统计表-统计超标数")
	 @ApiOperation(value = "环境监测数据统计表-统计超标数", notes = "环境监测数据统计表-统计超标数")
	 @GetMapping(value = "/liststa")
	 public Result<?> queryPageListsta(DevicehistoryStatic devicehistoryStatic,
									   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									   HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (devicehistoryStatic.getDevaddr() == null) {
			 if (!"null".equals(shebei)) {
				 devicehistoryStatic.setDevaddr(shebei);
			 } else {
				 devicehistoryStatic.setDevaddr("空");
			 }
		 }
		 int pm25chao = 0;
		 int pm10chao = 0;
		 int noisechao = 0;
		 int temchao = 0;
		 int humchao = 0;
		 QueryWrapper<DevicehistoryStatic> queryWrapper = QueryGenerator.initQueryWrapper(devicehistoryStatic, req.getParameterMap());
		 queryWrapper.select("sum(pm25chao) pm25chao,sum(pm10chao) pm10chao,sum(noisechao) noisechao,sum(temchao) temchao,sum(humchao)");
		 DevicehistoryStatic pageList = devicehistoryStaticService.getOne(queryWrapper);
		 if (pageList != null) {
			 if (pageList.getPm25chao() != null) {
				 pm25chao = pageList.getPm25chao();
			 }
			 if (pageList.getPm10chao() != null) {
				 pm10chao = pageList.getPm10chao();
			 }
			 if (pageList.getNoisechao() != null) {
				 noisechao = pageList.getNoisechao();
			 }
			 if (pageList.getTemchao() != null) {
				 temchao = pageList.getTemchao();
			 }
			 if (pageList.getHumchao() != null) {
				 humchao = pageList.getHumchao();
			 }
		 }
		 Map<String, Object> map = new HashMap<>();
		 map.put("pm25chao", pm25chao);
		 map.put("pm10chao", pm10chao);
		 map.put("noisechao", noisechao);
		 map.put("temchao", temchao);
		 map.put("humchao", humchao);
		 return Result.OK(map);
	 }

	/**
	 *   添加
	 *
	 * @param devicehistoryStatic
	 * @return
	 */
	@AutoLog(value = "环境监测数据统计表-添加")
	@ApiOperation(value="环境监测数据统计表-添加", notes="环境监测数据统计表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicehistoryStatic devicehistoryStatic) {
		devicehistoryStaticService.save(devicehistoryStatic);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param devicehistoryStatic
	 * @return
	 */
	@AutoLog(value = "环境监测数据统计表-编辑")
	@ApiOperation(value="环境监测数据统计表-编辑", notes="环境监测数据统计表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicehistoryStatic devicehistoryStatic) {
		devicehistoryStaticService.updateById(devicehistoryStatic);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "环境监测数据统计表-通过id删除")
	@ApiOperation(value="环境监测数据统计表-通过id删除", notes="环境监测数据统计表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicehistoryStaticService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "环境监测数据统计表-批量删除")
	@ApiOperation(value="环境监测数据统计表-批量删除", notes="环境监测数据统计表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicehistoryStaticService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "环境监测数据统计表-通过id查询")
	@ApiOperation(value="环境监测数据统计表-通过id查询", notes="环境监测数据统计表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicehistoryStatic devicehistoryStatic = devicehistoryStaticService.getById(id);
		if(devicehistoryStatic==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicehistoryStatic);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicehistoryStatic
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicehistoryStatic devicehistoryStatic) {
        return super.exportXls(request, devicehistoryStatic, DevicehistoryStatic.class, "环境监测数据统计表");
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
        return super.importExcel(request, response, DevicehistoryStatic.class);
    }

}
