package com.trtm.iot.devicehammeringhistorydata.controller;

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
import com.trtm.iot.devicehammeringhistorydata.entity.DeviceHammeringHistorydata;
import com.trtm.iot.devicehammeringhistorydata.service.IDeviceHammeringHistorydataService;

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
 * @Description: device_hammering_historydata
 * @Author: jeecg-boot
 * @Date:   2024-03-08
 * @Version: V1.0
 */
@Api(tags="device_hammering_historydata")
@RestController
@RequestMapping("/devicehammeringhistorydata/deviceHammeringHistorydata")
@Slf4j
public class DeviceHammeringHistorydataController extends JeecgController<DeviceHammeringHistorydata, IDeviceHammeringHistorydataService> {
	@Autowired
	private IDeviceHammeringHistorydataService deviceHammeringHistorydataService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param deviceHammeringHistorydata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata-分页列表查询")
	@ApiOperation(value="device_hammering_historydata-分页列表查询", notes="device_hammering_historydata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceHammeringHistorydata deviceHammeringHistorydata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (deviceHammeringHistorydata.getShebeino() == null || deviceHammeringHistorydata.getShebeino().isEmpty()) {
			if (!"null".equals(shebei)) {
				deviceHammeringHistorydata.setShebeino(shebei);
			} else {
				deviceHammeringHistorydata.setShebeino("空");
			}
		}
		QueryWrapper<DeviceHammeringHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceHammeringHistorydata, req.getParameterMap());
		Page<DeviceHammeringHistorydata> page = new Page<DeviceHammeringHistorydata>(pageNo, pageSize);
		IPage<DeviceHammeringHistorydata> pageList = deviceHammeringHistorydataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceHammeringHistorydata
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata-添加")
	@ApiOperation(value="device_hammering_historydata-添加", notes="device_hammering_historydata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceHammeringHistorydata deviceHammeringHistorydata) {
		deviceHammeringHistorydataService.save(deviceHammeringHistorydata);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param deviceHammeringHistorydata
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata-编辑")
	@ApiOperation(value="device_hammering_historydata-编辑", notes="device_hammering_historydata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceHammeringHistorydata deviceHammeringHistorydata) {
		deviceHammeringHistorydataService.updateById(deviceHammeringHistorydata);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata-通过id删除")
	@ApiOperation(value="device_hammering_historydata-通过id删除", notes="device_hammering_historydata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceHammeringHistorydataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata-批量删除")
	@ApiOperation(value="device_hammering_historydata-批量删除", notes="device_hammering_historydata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceHammeringHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata-通过id查询")
	@ApiOperation(value="device_hammering_historydata-通过id查询", notes="device_hammering_historydata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceHammeringHistorydata deviceHammeringHistorydata = deviceHammeringHistorydataService.getById(id);
		if(deviceHammeringHistorydata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceHammeringHistorydata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceHammeringHistorydata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceHammeringHistorydata deviceHammeringHistorydata) {
        return super.exportXls(request, deviceHammeringHistorydata, DeviceHammeringHistorydata.class, "device_hammering_historydata");
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
        return super.importExcel(request, response, DeviceHammeringHistorydata.class);
    }

}
