package com.trtm.iot.devicehighformwork.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.devicehighformwork.entity.DeviceHighFormwork;
import com.trtm.iot.devicehighformwork.service.IDeviceHighFormworkService;
import com.trtm.iot.jikeng.service.IJikengWeiyCfgService;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicehighformwork.entity.DeviceHighFormworkReal;
import com.trtm.iot.devicehighformwork.service.IDeviceHighFormworkRealService;

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
 * @Description: device_high_formwork_real
 * @Author: jeecg-boot
 * @Date:   2023-11-21
 * @Version: V1.0
 */
@Api(tags="device_high_formwork_real")
@RestController
@RequestMapping("/devicehighformwork/deviceHighFormworkReal")
@Slf4j
public class DeviceHighFormworkRealController extends JeecgController<DeviceHighFormworkReal, IDeviceHighFormworkRealService> {
	@Autowired
	private IDeviceHighFormworkRealService deviceHighFormworkRealService;
	 @Autowired
	 private IDeviceHighFormworkService deviceHighFormworkService;

	 @Autowired
	 private IJikengWeiyCfgService jikengWeiyCfgService;

	/**
	 * 分页列表查询
	 *
	 * @param deviceHighFormworkReal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_high_formwork_real-分页列表查询")
	@ApiOperation(value="device_high_formwork_real-分页列表查询", notes="device_high_formwork_real-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceHighFormworkReal deviceHighFormworkReal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceHighFormworkReal> queryWrapper = QueryGenerator.initQueryWrapper(deviceHighFormworkReal, req.getParameterMap());
		Page<DeviceHighFormworkReal> page = new Page<DeviceHighFormworkReal>(pageNo, pageSize);
		IPage<DeviceHighFormworkReal> pageList = deviceHighFormworkRealService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param deviceHighFormworkReal
	 * @return
	 */
	@AutoLog(value = "device_high_formwork_real-添加")
	@ApiOperation(value="device_high_formwork_real-添加", notes="device_high_formwork_real-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceHighFormworkReal deviceHighFormworkReal) {
		deviceHighFormworkRealService.save(deviceHighFormworkReal);
		return Result.OK("添加成功！");
	}

	 @AutoLog(value = "device_high_formwork_real-添加")
	 @ApiOperation(value="device_high_formwork_real-添加", notes="device_high_formwork_real-添加")
	 @PostMapping(value = "/addOpen")
	 public Result<?> addOpen(@RequestBody DeviceHighFormworkReal deviceHighFormworkReal) {
		 deviceHighFormworkReal.setSnbh(deviceHighFormworkReal.getSntype()+"-"+deviceHighFormworkReal.getSnbh());
		// 查询是否存在设备编号
		 QueryWrapper<DeviceHighFormworkReal> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq(StringUtils.isNotBlank(deviceHighFormworkReal.getSnbh()),"snbh",deviceHighFormworkReal.getSnbh());
		 queryWrapper.eq(StringUtils.isNotBlank(deviceHighFormworkReal.getSntype()),"sntype",deviceHighFormworkReal.getSntype());
		 List<DeviceHighFormworkReal> list = deviceHighFormworkRealService.list(queryWrapper);
		 if(list.size()>0){
			 DeviceHighFormworkReal a = deviceHighFormworkReal;
			 a.setId(list.get(0).getId());
		 }
		 deviceHighFormworkRealService.saveOrUpdate(deviceHighFormworkReal);
		 DeviceHighFormwork  deviceHighFormwork = new DeviceHighFormwork();
		 BeanUtils.copyProperties(deviceHighFormworkReal, deviceHighFormwork);
		 deviceHighFormwork.setId(null);
		 deviceHighFormworkService.save(deviceHighFormwork);
		 updateCfg(deviceHighFormworkReal);// 更新安全监测表数据
		 return Result.OK("添加成功！");
	 }

	 public String updateCfg(DeviceHighFormworkReal real){
		String msg ="";
		 JSONObject json = new JSONObject();

		if("691".equals(real.getSntype())){ // 位移设备
		 	json.put("位移X",String.format("%.1f", Double.parseDouble(real.getAbparam1()) )+"mm");
			json.put("位移Y",String.format("%.1f", Double.parseDouble(real.getAbparam2()) )+"mm");
			json.put("沉降",String.format("%.1f", Double.parseDouble(real.getAbparam3()) )+"mm");
		}else if("692".equals(real.getSntype())){
			json.put("重量",String.format("%.1f", Double.parseDouble(real.getAbparam1()) )+"t");
		} else if ("693".equals(real.getSntype())) {
			json.put("倾角X",String.format("%.1f", Double.parseDouble(real.getAbparam1()) )+"°");
			json.put("倾角Y",String.format("%.1f", Double.parseDouble(real.getAbparam2()) )+"°");
		}else{
			msg= "设备类型不匹配系统";
		}
		 jikengWeiyCfgService.updateByShbeino(real.getSnbh(),json.toJSONString());
		return msg;
	 }

	/**
	 *  编辑
	 *
	 * @param deviceHighFormworkReal
	 * @return
	 */
	@AutoLog(value = "device_high_formwork_real-编辑")
	@ApiOperation(value="device_high_formwork_real-编辑", notes="device_high_formwork_real-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceHighFormworkReal deviceHighFormworkReal) {
		deviceHighFormworkRealService.updateById(deviceHighFormworkReal);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_high_formwork_real-通过id删除")
	@ApiOperation(value="device_high_formwork_real-通过id删除", notes="device_high_formwork_real-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceHighFormworkRealService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_high_formwork_real-批量删除")
	@ApiOperation(value="device_high_formwork_real-批量删除", notes="device_high_formwork_real-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceHighFormworkRealService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_high_formwork_real-通过id查询")
	@ApiOperation(value="device_high_formwork_real-通过id查询", notes="device_high_formwork_real-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceHighFormworkReal deviceHighFormworkReal = deviceHighFormworkRealService.getById(id);
		if(deviceHighFormworkReal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceHighFormworkReal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceHighFormworkReal
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceHighFormworkReal deviceHighFormworkReal) {
        return super.exportXls(request, deviceHighFormworkReal, DeviceHighFormworkReal.class, "device_high_formwork_real");
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
        return super.importExcel(request, response, DeviceHighFormworkReal.class);
    }

}
