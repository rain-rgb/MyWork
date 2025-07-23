package com.trtm.iot.devicemixpilehistorydata.controller;

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

import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicemixpilehistorydata.entity.DeviceMixpileHistorydata;
import com.trtm.iot.devicemixpilehistorydata.service.IDeviceMixpileHistorydataService;

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
import static com.trtm.iot.util.GPSUtil.retain6;

/**
 * @Description: device_mixpile_historydata
 * @Author: jeecg-boot
 * @Date:   2021-10-20
 * @Version: V1.0
 */
@Api(tags="device_mixpile_historydata")
@RestController
@RequestMapping("/devicemixpilehistorydata/deviceMixpileHistorydata")
@Slf4j
public class DeviceMixpileHistorydataController extends JeecgController<DeviceMixpileHistorydata, IDeviceMixpileHistorydataService> {
	@Autowired
	private IDeviceMixpileHistorydataService deviceMixpileHistorydataService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param deviceMixpileHistorydata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_mixpile_historydata-分页列表查询")
	@ApiOperation(value="device_mixpile_historydata-分页列表查询", notes="device_mixpile_historydata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceMixpileHistorydata deviceMixpileHistorydata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (deviceMixpileHistorydata.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				deviceMixpileHistorydata.setShebeino(shebei);
			}else {
				deviceMixpileHistorydata.setShebeino("空");
			}
		}
		if(deviceMixpileHistorydata.getX()!=null){
			deviceMixpileHistorydata.setX("!"+0);
		}
		if(deviceMixpileHistorydata.getY()!=null){
			deviceMixpileHistorydata.setY("!"+0);
		}
		QueryWrapper<DeviceMixpileHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydata, req.getParameterMap());

//		if(deviceMixpileHistorydata.getElec()!=null){
//			queryWrapper.gt("elec",0);
//		}
//		if(deviceMixpileHistorydata.getGrouting()!=null){
//			queryWrapper.gt("grouting",0);
//		}
//		if(deviceMixpileHistorydata.getGrouting()!=null){
//			queryWrapper.gt("grouting",0);
//		}
//		if(deviceMixpileHistorydata.getFlowlst()!=null){
//			queryWrapper.gt("flowlst",0);
//		}
//		if(deviceMixpileHistorydata.getSpeed()!=null){
//			queryWrapper.gt("speed",0);
//		}
		Page<DeviceMixpileHistorydata> page = new Page<DeviceMixpileHistorydata>(pageNo, pageSize);
		IPage<DeviceMixpileHistorydata> pageList = deviceMixpileHistorydataService.page(page, queryWrapper);
		List<DeviceMixpileHistorydata> records = pageList.getRecords();
		for (DeviceMixpileHistorydata record : records) {
			if(record.getLgdfloat()!=null&&record.getLtdfloat()!=null){
				Double lon = Double.valueOf(String.valueOf(record.getLgdfloat()));
				Double lat = Double.valueOf(String.valueOf(record.getLtdfloat()));
				Double lat1 = formatLnglat(lat);
				Double lon1 = formatLnglat(lon);
				double [] zuobiao = gps84_To_Gcj02(lat1,lon1);
				Double lat2 = retain6(zuobiao[0]);
				Double lon2 = retain6(zuobiao[1]);
				record.setLgdfloat(String.valueOf(lon2));
				record.setLtdfloat(String.valueOf(lat2));
			}
		}
		return Result.OK(pageList);
	}

	@AutoLog(value = "device_mixpile_historydata-列表查询")
	@ApiOperation(value="device_mixpile_historydata-列表查询", notes="device_mixpile_historydata-列表查询")
	@GetMapping(value = "/list1")
	public Result<?> queryPageList1(DeviceMixpileHistorydata deviceMixpileHistorydata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (deviceMixpileHistorydata.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				deviceMixpileHistorydata.setShebeino(shebei);
			}else {
				deviceMixpileHistorydata.setShebeino("空");
			}
		}
//		if (StringUtil.isEmpty(deviceMixpileHistorydata.getPileno())){
//			deviceMixpileHistorydata.setPileno(URLEncoder.encode(deviceMixpileHistorydata.getPileno(), "UTF-8"));
//		}
//		if(deviceMixpileHistorydata.getX()!=null){
//			deviceMixpileHistorydata.setX("!"+0);
//		}
//		if(deviceMixpileHistorydata.getY()!=null){
//			deviceMixpileHistorydata.setY("!"+0);
//		}
		QueryWrapper<DeviceMixpileHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydata, req.getParameterMap());

		if(deviceMixpileHistorydata.getElec()!=null){
			queryWrapper.gt("elec",0);
		}
//		if(deviceMixpileHistorydata.getGrouting()!=null){
//			queryWrapper.gt("grouting",0);
//		}
		if(deviceMixpileHistorydata.getGrouting()!=null){
			queryWrapper.gt("grouting",0);
		}
		if(deviceMixpileHistorydata.getFlowlst()!=null){
			queryWrapper.gt("flowlst",0);
		}
		if(deviceMixpileHistorydata.getSpeed()!=null){
			queryWrapper.gt("speed",0);
		}
//		Page<DeviceMixpileHistorydata> page = new Page<DeviceMixpileHistorydata>(pageNo, pageSize);
		List<DeviceMixpileHistorydata> pageList = deviceMixpileHistorydataService.list(queryWrapper);
//		List<DeviceMixpileHistorydata> records = pageList.getRecords();
//		for (DeviceMixpileHistorydata record : records) {
//			if(record.getLgdfloat()!=null&&record.getLtdfloat()!=null){
//				Double lon = Double.valueOf(String.valueOf(record.getLgdfloat()));
//				Double lat = Double.valueOf(String.valueOf(record.getLtdfloat()));
//				Double lat1 = formatLnglat(lat);
//				Double lon1 = formatLnglat(lon);
//				double [] zuobiao = gps84_To_Gcj02(lat1,lon1);
//				Double lat2 = retain6(zuobiao[0]);
//				Double lon2 = retain6(zuobiao[1]);
//				record.setLgdfloat(String.valueOf(lon2));
//				record.setLtdfloat(String.valueOf(lat2));
//			}
//		}
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param deviceMixpileHistorydata
	 * @return
	 */
	@AutoLog(value = "device_mixpile_historydata-添加")
	@ApiOperation(value="device_mixpile_historydata-添加", notes="device_mixpile_historydata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceMixpileHistorydata deviceMixpileHistorydata) {
		deviceMixpileHistorydataService.save(deviceMixpileHistorydata);
		return Result.OK("添加成功！");
	}

	/**
	 *   添加
	 *
	 * @param deviceMixpileHistorydata
	 * @return
	 */
	@AutoLog(value = "device_mixpile_historydata-数据上传")
	@ApiOperation(value="device_mixpile_historydata-数据上传", notes="device_mixpile_historydata-数据上传")
	@PostMapping(value = "/addOther")
	public Result<?> addOther(@RequestBody DeviceMixpileHistorydata deviceMixpileHistorydata) {
		if(StringUtil.isNotEmpty(deviceMixpileHistorydata.getShebeino()) && StringUtil.isNotEmpty(deviceMixpileHistorydata.getPileno())){
			deviceMixpileHistorydataService.save(deviceMixpileHistorydata);
			return Result.OK("数据上传成功！");
		}else{
			return Result.error("软基成桩记录上传失败！请填入设备编号和桩号");
		}

	}

	/**
	 *  编辑
	 *
	 * @param deviceMixpileHistorydata
	 * @return
	 */
	@AutoLog(value = "device_mixpile_historydata-编辑")
	@ApiOperation(value="device_mixpile_historydata-编辑", notes="device_mixpile_historydata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceMixpileHistorydata deviceMixpileHistorydata) {
		deviceMixpileHistorydataService.updateById(deviceMixpileHistorydata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_mixpile_historydata-通过id删除")
	@ApiOperation(value="device_mixpile_historydata-通过id删除", notes="device_mixpile_historydata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceMixpileHistorydataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_mixpile_historydata-批量删除")
	@ApiOperation(value="device_mixpile_historydata-批量删除", notes="device_mixpile_historydata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceMixpileHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_mixpile_historydata-通过id查询")
	@ApiOperation(value="device_mixpile_historydata-通过id查询", notes="device_mixpile_historydata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceMixpileHistorydata deviceMixpileHistorydata = deviceMixpileHistorydataService.getById(id);
		if(deviceMixpileHistorydata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceMixpileHistorydata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceMixpileHistorydata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileHistorydata deviceMixpileHistorydata) {
        return super.exportXls(request, deviceMixpileHistorydata, DeviceMixpileHistorydata.class, "device_mixpile_historydata");
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
        return super.importExcel(request, response, DeviceMixpileHistorydata.class);
    }

}
