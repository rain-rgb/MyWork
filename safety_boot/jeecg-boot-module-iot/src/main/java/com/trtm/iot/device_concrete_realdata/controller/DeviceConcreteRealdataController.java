package com.trtm.iot.device_concrete_realdata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.device_concrete_historydata.entity.DeviceConcreteHistorydata;
import com.trtm.iot.device_concrete_historydata.service.IDeviceConcreteHistorydataService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.device_concrete_realdata.entity.DeviceConcreteRealdata;
import com.trtm.iot.device_concrete_realdata.service.IDeviceConcreteRealdataService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: 混凝土成熟度监测
 * @Author: jeecg-boot
 * @Date:   2024-10-22
 * @Version: V1.0
 */
@Api(tags="混凝土成熟度监测")
@RestController
@RequestMapping("/device_concrete_realdata/deviceConcreteRealdata")
@Slf4j
public class DeviceConcreteRealdataController extends JeecgController<DeviceConcreteRealdata, IDeviceConcreteRealdataService> {
	@Autowired
	private IDeviceConcreteRealdataService deviceConcreteRealdataService;
	 @Autowired
	 private IDeviceConcreteHistorydataService deviceConcreteHistorydataService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceConcreteRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "混凝土成熟度监测-分页列表查询")
	@ApiOperation(value="混凝土成熟度监测-分页列表查询", notes="混凝土成熟度监测-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceConcreteRealdata deviceConcreteRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceConcreteRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceConcreteRealdata, req.getParameterMap());
		Page<DeviceConcreteRealdata> page = new Page<DeviceConcreteRealdata>(pageNo, pageSize);
		IPage<DeviceConcreteRealdata> pageList = deviceConcreteRealdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceConcreteRealdata
	 * @return
	 */
	@AutoLog(value = "混凝土成熟度监测-添加")
	@ApiOperation(value="混凝土成熟度监测-添加", notes="混凝土成熟度监测-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceConcreteRealdata deviceConcreteRealdata) {
		deviceConcreteRealdataService.save(deviceConcreteRealdata);
		return Result.OK("添加成功！");
	}

	 @AutoLog(value = "混凝土强度监测实时数据-添加")
	 @ApiOperation(value="混凝土强度监测实时数据-添加", notes="混凝土强度监测实时数据-添加")
	 @PostMapping(value = "/addOpen")
	 public Result<?> addOpen(@RequestBody DeviceConcreteRealdata deviceConcreteRealdata) {

		 String sbbh = deviceConcreteRealdata.getSbbh();

		 LambdaQueryWrapper<DeviceConcreteRealdata> queryWrapper = new LambdaQueryWrapper<>();
		 queryWrapper.eq(DeviceConcreteRealdata::getSbbh,sbbh);
		 DeviceConcreteRealdata one = deviceConcreteRealdataService.getOne(queryWrapper);

		 if (one!=null){
			 deviceConcreteRealdataService.removeById(one.getId());
		 }
		 deviceConcreteRealdataService.save(deviceConcreteRealdata);

		 //使用BeanUtils.copyProperties复制
		 DeviceConcreteHistorydata deviceConcreteHistorydata = new DeviceConcreteHistorydata();
		 BeanUtils.copyProperties(deviceConcreteRealdata,deviceConcreteHistorydata);
		 deviceConcreteHistorydataService.save(deviceConcreteHistorydata);

		 return Result.OK("添加成功！");
	 }

	 /**
	 *  编辑
	 *
	 * @param deviceConcreteRealdata
	 * @return
	 */
	@AutoLog(value = "混凝土成熟度监测-编辑")
	@ApiOperation(value="混凝土成熟度监测-编辑", notes="混凝土成熟度监测-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceConcreteRealdata deviceConcreteRealdata) {
		deviceConcreteRealdataService.updateById(deviceConcreteRealdata);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土成熟度监测-通过id删除")
	@ApiOperation(value="混凝土成熟度监测-通过id删除", notes="混凝土成熟度监测-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceConcreteRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "混凝土成熟度监测-批量删除")
	@ApiOperation(value="混凝土成熟度监测-批量删除", notes="混凝土成熟度监测-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceConcreteRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土成熟度监测-通过id查询")
	@ApiOperation(value="混凝土成熟度监测-通过id查询", notes="混凝土成熟度监测-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceConcreteRealdata deviceConcreteRealdata = deviceConcreteRealdataService.getById(id);
		if(deviceConcreteRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceConcreteRealdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceConcreteRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceConcreteRealdata deviceConcreteRealdata) {
        return super.exportXls(request, deviceConcreteRealdata, DeviceConcreteRealdata.class, "混凝土成熟度监测");
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
        return super.importExcel(request, response, DeviceConcreteRealdata.class);
    }

}
