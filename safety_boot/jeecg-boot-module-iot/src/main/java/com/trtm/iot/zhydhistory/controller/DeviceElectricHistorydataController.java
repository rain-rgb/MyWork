package com.trtm.iot.zhydhistory.controller;

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
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;

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
 * @Description: 智慧用电历史数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-16
 * @Version: V1.0
 */
@Api(tags="智慧用电历史数据表")
@RestController
@RequestMapping("/zhydhistory/deviceElectricHistorydata")
@Slf4j
public class DeviceElectricHistorydataController extends JeecgController<DeviceElectricHistorydata, IDeviceElectricHistorydataService> {
	@Autowired
	private IDeviceElectricHistorydataService deviceElectricHistorydataService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param deviceElectricHistorydata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据表-分页列表查询")
	@ApiOperation(value="智慧用电历史数据表-分页列表查询", notes="智慧用电历史数据表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceElectricHistorydata deviceElectricHistorydata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (deviceElectricHistorydata.getImei() == null) {
			if (!"null".equals(shebei)) {
				deviceElectricHistorydata.setImei(shebei);
			}else {
				deviceElectricHistorydata.setImei("空");
			}
		}
		QueryWrapper<DeviceElectricHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceElectricHistorydata, req.getParameterMap());
		Page<DeviceElectricHistorydata> page = new Page<DeviceElectricHistorydata>(pageNo, pageSize);
		IPage<DeviceElectricHistorydata> pageList = deviceElectricHistorydataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 预警数据分页列表查询
	  *
	  * @param deviceElectricHistorydata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "智慧用电历史数据表-预警数据分页列表查询")
	 @ApiOperation(value="智慧用电历史数据表-预警数据分页列表查询", notes="智慧用电历史数据表-预警数据分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(DeviceElectricHistorydata deviceElectricHistorydata,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (deviceElectricHistorydata.getImei() == null) {
			 if (!"null".equals(shebei)) {
				 deviceElectricHistorydata.setImei(shebei);
			 }else {
				 deviceElectricHistorydata.setImei("空");
	 	 	 }
		 }
		 deviceElectricHistorydata.setStatus(2);
		 QueryWrapper<DeviceElectricHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceElectricHistorydata, req.getParameterMap());
		 Page<DeviceElectricHistorydata> page = new Page<DeviceElectricHistorydata>(pageNo, pageSize);
		 IPage<DeviceElectricHistorydata> pageList = deviceElectricHistorydataService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param deviceElectricHistorydata
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据表-添加")
	@ApiOperation(value="智慧用电历史数据表-添加", notes="智慧用电历史数据表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceElectricHistorydata deviceElectricHistorydata) {
		deviceElectricHistorydataService.save(deviceElectricHistorydata);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param deviceElectricHistorydata
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据表-编辑")
	@ApiOperation(value="智慧用电历史数据表-编辑", notes="智慧用电历史数据表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceElectricHistorydata deviceElectricHistorydata) {
		deviceElectricHistorydataService.updateById(deviceElectricHistorydata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据表-通过id删除")
	@ApiOperation(value="智慧用电历史数据表-通过id删除", notes="智慧用电历史数据表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceElectricHistorydataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据表-批量删除")
	@ApiOperation(value="智慧用电历史数据表-批量删除", notes="智慧用电历史数据表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceElectricHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "智慧用电历史数据表-通过id查询")
	@ApiOperation(value="智慧用电历史数据表-通过id查询", notes="智慧用电历史数据表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceElectricHistorydata deviceElectricHistorydata = deviceElectricHistorydataService.getById(id);
		if(deviceElectricHistorydata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceElectricHistorydata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceElectricHistorydata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceElectricHistorydata deviceElectricHistorydata) {
        return super.exportXls(request, deviceElectricHistorydata, DeviceElectricHistorydata.class, "智慧用电历史数据表");
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
        return super.importExcel(request, response, DeviceElectricHistorydata.class);
    }

}
