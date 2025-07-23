package com.trtm.iot.devicehammeringhistorydatapart.controller;

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
import com.xkcoding.http.util.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicehammeringhistorydatapart.entity.DeviceHammeringHistorydataPart;
import com.trtm.iot.devicehammeringhistorydatapart.service.IDeviceHammeringHistorydataPartService;

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
 * @Description: device_hammering_historydata_part
 * @Author: jeecg-boot
 * @Date:   2024-03-08
 * @Version: V1.0
 */
@Api(tags="device_hammering_historydata_part")
@RestController
@RequestMapping("/devicehammeringhistorydatapart/deviceHammeringHistorydataPart")
@Slf4j
public class DeviceHammeringHistorydataPartController extends JeecgController<DeviceHammeringHistorydataPart, IDeviceHammeringHistorydataPartService> {
	@Autowired
	private IDeviceHammeringHistorydataPartService deviceHammeringHistorydataPartService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceHammeringHistorydataPart
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_part-分页列表查询")
	@ApiOperation(value="device_hammering_historydata_part-分页列表查询", notes="device_hammering_historydata_part-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceHammeringHistorydataPart deviceHammeringHistorydataPart,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceHammeringHistorydataPart> queryWrapper = QueryGenerator.initQueryWrapper(deviceHammeringHistorydataPart, req.getParameterMap());
		Page<DeviceHammeringHistorydataPart> page = new Page<DeviceHammeringHistorydataPart>(pageNo, pageSize);
		IPage<DeviceHammeringHistorydataPart> pageList = deviceHammeringHistorydataPartService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceHammeringHistorydataPart
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_part-添加")
	@ApiOperation(value="device_hammering_historydata_part-添加", notes="device_hammering_historydata_part-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceHammeringHistorydataPart deviceHammeringHistorydataPart) {
		if (StringUtil.isNotEmpty(deviceHammeringHistorydataPart.getShebeino()) && StringUtil.isNotEmpty(deviceHammeringHistorydataPart.getPileNo())) {
			DeviceHammeringHistorydataPart one = null;
			try {
				QueryWrapper<DeviceHammeringHistorydataPart> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("shebeino", deviceHammeringHistorydataPart.getShebeino());
				queryWrapper.eq("pile_no", deviceHammeringHistorydataPart.getPileNo());
				queryWrapper.eq("pile_mileage", deviceHammeringHistorydataPart.getPileMileage());
				one = deviceHammeringHistorydataPartService.getOne(queryWrapper);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (one != null) {
				deviceHammeringHistorydataPart.setId(one.getId());
				deviceHammeringHistorydataPartService.updateById(deviceHammeringHistorydataPart);
				return Result.OK("修改！");
			} else {
				deviceHammeringHistorydataPartService.save(deviceHammeringHistorydataPart);
				return Result.OK("添加成功！");
			}
		} else {
			return Result.error("记录上传失败！请填入设备编号和桩号");
		}
	}
	
	/**
	 *  编辑
	 *
	 * @param deviceHammeringHistorydataPart
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_part-编辑")
	@ApiOperation(value="device_hammering_historydata_part-编辑", notes="device_hammering_historydata_part-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceHammeringHistorydataPart deviceHammeringHistorydataPart) {
		deviceHammeringHistorydataPartService.updateById(deviceHammeringHistorydataPart);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_part-通过id删除")
	@ApiOperation(value="device_hammering_historydata_part-通过id删除", notes="device_hammering_historydata_part-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceHammeringHistorydataPartService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_part-批量删除")
	@ApiOperation(value="device_hammering_historydata_part-批量删除", notes="device_hammering_historydata_part-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceHammeringHistorydataPartService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_hammering_historydata_part-通过id查询")
	@ApiOperation(value="device_hammering_historydata_part-通过id查询", notes="device_hammering_historydata_part-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceHammeringHistorydataPart deviceHammeringHistorydataPart = deviceHammeringHistorydataPartService.getById(id);
		if(deviceHammeringHistorydataPart==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceHammeringHistorydataPart);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceHammeringHistorydataPart
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceHammeringHistorydataPart deviceHammeringHistorydataPart) {
        return super.exportXls(request, deviceHammeringHistorydataPart, DeviceHammeringHistorydataPart.class, "device_hammering_historydata_part");
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
        return super.importExcel(request, response, DeviceHammeringHistorydataPart.class);
    }

}
