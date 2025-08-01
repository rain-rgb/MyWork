package com.trtm.iot.lmd.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.lmd.entity.DeviceCraneTb;
import com.trtm.iot.lmd.service.IDeviceCraneTbService;

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
 * @Description: device_crane_tb
 * @Author: jeecg-boot
 * @Date:   2025-04-28
 * @Version: V1.0
 */
@Api(tags="device_crane_tb")
@RestController
@RequestMapping("/lmd/deviceCraneTb")
@Slf4j
public class DeviceCraneTbController extends JeecgController<DeviceCraneTb, IDeviceCraneTbService> {
	@Autowired
	private IDeviceCraneTbService deviceCraneTbService;

	/**
	 * 分页列表查询
	 *
	 * @param deviceCraneTb
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_crane_tb-分页列表查询")
	@ApiOperation(value="device_crane_tb-分页列表查询", notes="device_crane_tb-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceCraneTb deviceCraneTb,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceCraneTb> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneTb, req.getParameterMap());
		Page<DeviceCraneTb> page = new Page<DeviceCraneTb>(pageNo, pageSize);
		IPage<DeviceCraneTb> pageList = deviceCraneTbService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	/**
	 *   添加
	 *
	 * @param deviceCraneTb
	 * @return
	 */
	@AutoLog(value = "device_crane_tb-添加")
	@ApiOperation(value="device_crane_tb-添加", notes="device_crane_tb-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceCraneTb deviceCraneTb) {
		deviceCraneTbService.save(deviceCraneTb);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param deviceCraneTb
	 * @return
	 */
	@AutoLog(value = "device_crane_tb-编辑")
	@ApiOperation(value="device_crane_tb-编辑", notes="device_crane_tb-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceCraneTb deviceCraneTb) {
		deviceCraneTbService.updateById(deviceCraneTb);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_crane_tb-通过id删除")
	@ApiOperation(value="device_crane_tb-通过id删除", notes="device_crane_tb-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceCraneTbService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_crane_tb-批量删除")
	@ApiOperation(value="device_crane_tb-批量删除", notes="device_crane_tb-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceCraneTbService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_crane_tb-通过id查询")
	@ApiOperation(value="device_crane_tb-通过id查询", notes="device_crane_tb-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceCraneTb deviceCraneTb = deviceCraneTbService.getById(id);
		if(deviceCraneTb==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceCraneTb);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceCraneTb
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceCraneTb deviceCraneTb) {
        return super.exportXls(request, deviceCraneTb, DeviceCraneTb.class, "device_crane_tb");
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
        return super.importExcel(request, response, DeviceCraneTb.class);
    }

}
