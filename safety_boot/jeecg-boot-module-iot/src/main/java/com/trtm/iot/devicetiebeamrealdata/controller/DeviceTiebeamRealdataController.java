package com.trtm.iot.devicetiebeamrealdata.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicecranerealdata.service.IDeviceCraneRealdataService;
import com.trtm.iot.devicetiebeamhistorydata.entity.DeviceTiebeamHistorydata;
import com.trtm.iot.devicetiebeamhistorydata.service.IDeviceTiebeamHistorydataService;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicetiebeamrealdata.entity.DeviceTiebeamRealdata;
import com.trtm.iot.devicetiebeamrealdata.service.IDeviceTiebeamRealdataService;

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
 * @Description: 提梁机实时数据信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Api(tags="提梁机实时数据信息表")
@RestController
@RequestMapping("/devicetiebeamrealdata/deviceTiebeamRealdata")
@Slf4j
public class DeviceTiebeamRealdataController extends JeecgController<DeviceTiebeamRealdata, IDeviceTiebeamRealdataService> {
	@Autowired
	private IDeviceTiebeamRealdataService deviceTiebeamRealdataService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IDeviceCraneRealdataService craneRealdataService;
	 @Autowired
	 private IDeviceCraneHistorydataService deviceCraneHistorydataService;
	 @Autowired
	 private IDeviceTiebeamHistorydataService deviceTiebeamHistorydataService;
	/**
	 * 分页列表查询
	 *
	 * @param deviceTiebeamRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "提梁机实时数据信息表-分页列表查询")
	@ApiOperation(value="提梁机实时数据信息表-分页列表查询", notes="提梁机实时数据信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceTiebeamRealdata deviceTiebeamRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if(deviceTiebeamRealdata.getDevno()==null){
			if (shebei != null) {
				deviceTiebeamRealdata.setDevno(shebei);
			}
		}
		deviceTiebeamRealdata.setDevtype(1);
		QueryWrapper<DeviceTiebeamRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTiebeamRealdata, req.getParameterMap());
		Page<DeviceTiebeamRealdata> page = new Page<DeviceTiebeamRealdata>(pageNo, pageSize);
		IPage<DeviceTiebeamRealdata> pageList = deviceTiebeamRealdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 /**
	  * 分页列表查询
	  *
	  * @param deviceTiebeamRealdata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "架桥机实时数据信息表-分页列表查询")
	 @ApiOperation(value="架桥机实时数据信息表-分页列表查询", notes="架桥机实时数据信息表-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(DeviceTiebeamRealdata deviceTiebeamRealdata,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if(deviceTiebeamRealdata.getDevno()==null){
			 if (shebei != null) {
				 deviceTiebeamRealdata.setDevno(shebei);
			 }
		 }
		 deviceTiebeamRealdata.setDevtype(2);
		 QueryWrapper<DeviceTiebeamRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTiebeamRealdata, req.getParameterMap());
		 Page<DeviceTiebeamRealdata> page = new Page<DeviceTiebeamRealdata>(pageNo, pageSize);
		 IPage<DeviceTiebeamRealdata> pageList = deviceTiebeamRealdataService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param deviceTiebeamRealdata
	 * @return
	 */
	@AutoLog(value = "提梁机实时数据信息表-添加")
	@ApiOperation(value="提梁机实时数据信息表-添加", notes="提梁机实时数据信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTiebeamRealdata deviceTiebeamRealdata) {
		deviceTiebeamRealdataService.save(deviceTiebeamRealdata);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加 对外接口
	  *
	  * @param deviceTiebeamRealdata
	  * @return
	  */
	 @AutoLog(value = "提梁机实时数据信息表-添加")
	 @ApiOperation(value="提梁机实时数据信息表-添加", notes="提梁机实时数据信息表-添加")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody DeviceTiebeamRealdata deviceTiebeamRealdata) {
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 	QueryWrapper<DeviceCraneRealdata> queryWrapper1=new QueryWrapper<>();
		 queryWrapper1.eq("device_code",deviceTiebeamRealdata.getDevno());
		 DeviceCraneRealdata one = craneRealdataService.getOne(queryWrapper1);
	 	boolean boo = false;
	 	boolean boo1 = false;
		 DeviceCraneRealdata deviceCraneRealdata=new DeviceCraneRealdata();
		 deviceCraneRealdata.setDeviceType(deviceTiebeamRealdata.getDevtype());
		 deviceCraneRealdata.setDeviceCode(deviceTiebeamRealdata.getDevno());
		 double Bigcardistance=Double.valueOf(deviceTiebeamRealdata.getBigcardistance()/100);
		 deviceCraneRealdata.setBigCarroute(Bigcardistance);
		 Date date = DateUtils.TimestampToDate(deviceTiebeamRealdata.getTime());
		 deviceCraneRealdata.setCranedate(date);
		 double Hook1height=Double.valueOf(deviceTiebeamRealdata.getHook1height()/100);
		 deviceCraneRealdata.setMainHookheight(Hook1height);
		 double Hook2height=Double.valueOf(deviceTiebeamRealdata.getHook2height()/100);
		 deviceCraneRealdata.setReservedVicehookheight(Hook2height);
		 double Hook1weight=Double.valueOf(deviceTiebeamRealdata.getHook1weight()/100);
		 deviceCraneRealdata.setMainHookload(Hook1weight);
		 double Hook2weight=Double.valueOf(deviceTiebeamRealdata.getHook2weight()/100);
		 deviceCraneRealdata.setReservedVicehookload(Hook2weight);
		 double Frontcardistance=Double.valueOf(deviceTiebeamRealdata.getFrontcardistance()/100);
		 deviceCraneRealdata.setSmallCarroute(Frontcardistance);
		 double Backcardistance=Double.valueOf(deviceTiebeamRealdata.getBackcardistance()/100);
		 deviceCraneRealdata.setReservedSmallcarroute(Backcardistance);
		 double Windspeed=Double.valueOf(deviceTiebeamRealdata.getWindspeed()/100);
		 deviceCraneRealdata.setWindSpeed(Windspeed);
		 int Hook1frontalweight= (int) (deviceTiebeamRealdata.getHook1frontalweight()/100);
		 deviceCraneRealdata.setReservedOne(Hook1frontalweight);
		 int Hook2frontalweight=(int) (deviceTiebeamRealdata.getHook2frontalweight()/100);
		 deviceCraneRealdata.setReservedTwo(Hook2frontalweight);
		 deviceCraneRealdata.setBigx(deviceTiebeamRealdata.getBigx());
		 deviceCraneRealdata.setBigy(deviceTiebeamRealdata.getBigy());
		 deviceCraneRealdata.setVerticalx(deviceTiebeamRealdata.getVerticalx());
		 deviceCraneRealdata.setVerticaly(deviceTiebeamRealdata.getVerticaly());
		 double slr2 = deviceTiebeamRealdata.getSlr2()/100;
		 deviceCraneRealdata.setSlr2((float) slr2);
		 double slr1 = deviceTiebeamRealdata.getSlr1()/100;
		 deviceCraneRealdata.setSlr1((float) slr1);
	 	if (one == null) {
			boo= craneRealdataService.save(deviceCraneRealdata);
		}else {
			deviceCraneRealdata.setId(one.getId());
		 	boo = craneRealdataService.updateById(deviceCraneRealdata);
	 	}
		 DeviceCraneHistorydata deviceCraneHistorydata=new DeviceCraneHistorydata();
		 BeanUtils.copyProperties(deviceCraneRealdata, deviceCraneHistorydata);
		 boo1 = deviceCraneHistorydataService.save(deviceCraneHistorydata);
	 	if (boo&&boo1) {
	 		return Result.OK("添加成功！");
	 	}else {
	 		return Result.OK("添加失败！");
	 	}
	 }

	/**
	 *  编辑
	 *
	 * @param deviceTiebeamRealdata
	 * @return
	 */
	@AutoLog(value = "提梁机实时数据信息表-编辑")
	@ApiOperation(value="提梁机实时数据信息表-编辑", notes="提梁机实时数据信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceTiebeamRealdata deviceTiebeamRealdata) {
		deviceTiebeamRealdataService.updateById(deviceTiebeamRealdata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "提梁机实时数据信息表-通过id删除")
	@ApiOperation(value="提梁机实时数据信息表-通过id删除", notes="提梁机实时数据信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceTiebeamRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "提梁机实时数据信息表-批量删除")
	@ApiOperation(value="提梁机实时数据信息表-批量删除", notes="提梁机实时数据信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTiebeamRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "提梁机实时数据信息表-通过id查询")
	@ApiOperation(value="提梁机实时数据信息表-通过id查询", notes="提梁机实时数据信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTiebeamRealdata deviceTiebeamRealdata = deviceTiebeamRealdataService.getById(id);
		if(deviceTiebeamRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceTiebeamRealdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceTiebeamRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceTiebeamRealdata deviceTiebeamRealdata) {
        return super.exportXls(request, deviceTiebeamRealdata, DeviceTiebeamRealdata.class, "提梁机实时数据信息表");
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
        return super.importExcel(request, response, DeviceTiebeamRealdata.class);
    }

}
