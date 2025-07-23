package com.trtm.iot.devicepipepilerealdata.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.trtm.iot.devicepipepilehistorydata.entity.DevicePipepileHistorydata;
import com.trtm.iot.devicepipepilehistorydata.service.IDevicePipepileHistorydataService;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.devicepipepilereport.entity.DevicePipepileReport;
import com.trtm.iot.kanbaninfo.entity.Kanbaninfo;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepipepilerealdata.entity.DevicePipepileRealdata;
import com.trtm.iot.devicepipepilerealdata.service.IDevicePipepileRealdataService;

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
 * @Description: device_pipepile_realdata
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Api(tags="device_pipepile_realdata")
@RestController
@RequestMapping("/devicepipepilerealdata/devicePipepileRealdata")
@Slf4j
public class DevicePipepileRealdataController extends JeecgController<DevicePipepileRealdata, IDevicePipepileRealdataService> {
	@Autowired
	private IDevicePipepileRealdataService devicePipepileRealdataService;
	 @Autowired
	 private IDevicePipepileHistorydataOneService devicePipepileHistorydataOneService;
	@Autowired
	RedisUtil redisUtil;

	@Autowired
	private IDevicePipepileHistorydataService devicePipepileHistorydataService;
	/**
	 * 分页列表查询
	 *
	 * @param devicePipepileRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_pipepile_realdata-分页列表查询")
	@ApiOperation(value="device_pipepile_realdata-分页列表查询", notes="device_pipepile_realdata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevicePipepileRealdata devicePipepileRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="100") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (devicePipepileRealdata.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				devicePipepileRealdata.setShebeino(shebei);
			} else {
				devicePipepileRealdata.setShebeino("空");
			}
		}
		QueryWrapper<DevicePipepileRealdata> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileRealdata, req.getParameterMap());
		queryWrapper.select("id,shebeino,designdep,pile_y,pile_upress,pile_dpress,pile_aveupress,pile_avedpress,pile_starttime,datatime,speed,piletime,pile_effdep,pile_worktime,times,worksta,ts,pileno,LTDFloat,LGDFloat,mileage,elml,round(curdep,5) curdep,round(grounddep,5) grounddep");
		Page<DevicePipepileRealdata> page = new Page<DevicePipepileRealdata>(pageNo, pageSize);
		IPage<DevicePipepileRealdata> pageList = devicePipepileRealdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 /**
	  * 分页列表查询
	  *
	  * @param devicePipepileRealdata
	  * @return
	  */
	 @AutoLog(value = "device_pipepile_realdata-分页列表查询")
	 @ApiOperation(value="device_pipepile_realdata-分页列表查询", notes="device_pipepile_realdata-分页列表查询")
	 @GetMapping(value = "/listtanc")
	 public Result<?> queryPageListtanc(DevicePipepileRealdata devicePipepileRealdata,HttpServletRequest req) {
		 QueryWrapper<DevicePipepileRealdata> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileRealdata, req.getParameterMap());
		 DevicePipepileRealdata one = devicePipepileRealdataService.getOne(queryWrapper);

		 DevicePipepileHistorydataOne devicePipepileHistorydataOne = new DevicePipepileHistorydataOne();
		 devicePipepileHistorydataOne.setShebeino(devicePipepileRealdata.getShebeino());

		 Map<Object, Object> map = new HashMap<>();
		 QueryWrapper<DevicePipepileHistorydataOne> queryWrapper2 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
		 List<DevicePipepileHistorydataOne> list = devicePipepileHistorydataOneService.list(queryWrapper2);

		 QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
		 queryWrapper1.eq("chaobiaodengji",1);
		 List<DevicePipepileHistorydataOne> list1 = devicePipepileHistorydataOneService.list(queryWrapper1);


		 map.put("wggczl",list.size());
		 map.put("zlyj",list1.size());
		 map.put("dqzcd",one.getCurdep());
		 map.put("gzzt",one.getWorksta());
		 map.put("zh",one.getPileno());
		 map.put("zdzyl",one.getPileUpress());
		 map.put("zdjcl",one.getPileDpress());
		 return Result.OK(map);
	 }
	 /**
	  * 查询设备离线在线
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_pipepile_historydata_one-分页列表查询")
	 @ApiOperation(value = "device_pipepile_historydata_one-分页列表查询", notes = "device_pipepile_historydata_one-分页列表查询")
	 @GetMapping(value = "/listSb")
	 public Result<?> queryPageGzListSb(DevicePipepileRealdata devicePipepileRealdata,
										@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
										@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
										HttpServletRequest req) throws ParseException {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (devicePipepileRealdata.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 devicePipepileRealdata.setShebeino(shebei);
			 } else {
				 devicePipepileRealdata.setShebeino("空");
			 }
		 }
		 DevicePipepileHistorydataOne devicePipepileHistorydataOne = new DevicePipepileHistorydataOne();
		 devicePipepileHistorydataOne.setShebeino(shebei);
		 //获取近七天的日期
		 Date date = new Date();
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 String format = dateFormat.format(date);
		 QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(devicePipepileHistorydataOne, req.getParameterMap());
		 queryWrapper1.likeRight("pile_time",format);
		 List<DevicePipepileHistorydataOne> list1 = devicePipepileHistorydataOneService.list(queryWrapper1);

		 int lx = 0;
		 int zx = 0;
		 HashMap<String, Integer> map = new HashMap<>();
		 QueryWrapper<DevicePipepileRealdata> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileRealdata, req.getParameterMap());
		 List<DevicePipepileRealdata> list = devicePipepileRealdataService.list(queryWrapper);
		 if (list.size() > 0){
			 for (DevicePipepileRealdata l:list){
				 if (l.getWorksta().equals("离线")){
					 lx++;
				 }else {
					 zx++;
				 }
			 }
		 }
		 map.put("lx",lx);
		 map.put("zx",zx);
		 map.put("zs",zx+lx);
		 map.put("dzs",list1.size());
		 return Result.OK(map);
	 }

	 /**
	  * 分页列表查询
	  * @return
	  */
	 @AutoLog(value = "device_pipepile_report-分页列表查询")
	 @ApiOperation(value="device_pipepile_report-分页列表查询", notes="device_pipepile_report-分页列表查询")
	 @GetMapping(value = "/listGz")
	 public Result<?> queryPageListGz(String shebeino,String pileNo) {
		 if (pileNo.equals("无")){
			 DevicePipepileReport devicePipepileReport = new DevicePipepileReport();
			 return Result.OK(devicePipepileReport);
		 }else {
			 QueryWrapper<DevicePipepileRealdata> queryWrapper = new QueryWrapper<>();
			 queryWrapper.eq("shebeino",shebeino);
			 queryWrapper.eq("pileno",pileNo);
			 DevicePipepileRealdata one = devicePipepileRealdataService.getOne(queryWrapper);
			 if (!"离线".equals(one.getWorksta())){
			 	one.setSpeed("270");
			 }
//			 if (one != null){
//				 String speed = one.getSpeed();
//				 String pileAveupress = one.getPileAveupress();
//				 String pileUpress = one.getPileUpress();
//				 Double aDouble = Double.parseDouble(speed) * 6000;
//				 Double aveupress = Double.parseDouble(pileAveupress);
//				 Double pileupress = Double.parseDouble(pileUpress);
//				 one.setSpeed(aDouble.toString());
//				 one.setPileAveupress(aveupress.toString());
//				 one.setPileUpress(pileupress.toString());
//
//			 }
			 return Result.OK(one);
		 }
	 }
	/**
	 *   添加
	 *
	 * @param devicePipepileRealdata
	 * @return
	 */
	@AutoLog(value = "device_pipepile_realdata-添加")
	@ApiOperation(value="device_pipepile_realdata-添加", notes="device_pipepile_realdata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevicePipepileRealdata devicePipepileRealdata) {
		devicePipepileRealdataService.save(devicePipepileRealdata);
		return Result.OK("添加成功！");
	}


	 /**
	  *   实时数据更新
	  *
	  * @param devicePipepileRealdata
	  * @return
	  */
	 @AutoLog(value = "device_pipepile_realdata-实时数据更新")
	 @ApiOperation(value="device_pipepile_realdata-实时数据更新", notes="device_pipepile_realdata-实时数据更新")
	 @PostMapping(value = "/addOther")
	 public Result<?> addOther(@RequestBody DevicePipepileRealdata devicePipepileRealdata,HttpServletRequest req) {

		 if(StringUtil.isNotEmpty(devicePipepileRealdata.getShebeino())){
			 DevicePipepileRealdata realdata = new DevicePipepileRealdata();
			 realdata.setShebeino(devicePipepileRealdata.getShebeino());
			 // 更新历史表
			 DevicePipepileHistorydata devicePipepileHistorydata = new DevicePipepileHistorydata();
			 BeanUtils.copyProperties(devicePipepileRealdata, devicePipepileHistorydata);
			 devicePipepileHistorydataService.save(devicePipepileHistorydata);

			 QueryWrapper<DevicePipepileRealdata> queryWrapper = QueryGenerator.initQueryWrapper(realdata, req.getParameterMap());
			 List<DevicePipepileRealdata> ones = devicePipepileRealdataService.list(queryWrapper);
			 if(ones.size()>0){
				 devicePipepileRealdata.setId(ones.get(0).getId());
				 devicePipepileRealdataService.updateById(devicePipepileRealdata);
				 return Result.OK("软基设备实时数据更新成功!",devicePipepileRealdata);
			 }else{
				 devicePipepileRealdataService.save(devicePipepileRealdata);
				 return Result.OK("软基设备实时数据上传成功！",devicePipepileRealdata);
			 }
		 }else{
			 return Result.error("设备编号为空请重新上传！",devicePipepileRealdata);
		 }
	 }

	/**
	 *  编辑
	 *
	 * @param devicePipepileRealdata
	 * @return
	 */
	@AutoLog(value = "device_pipepile_realdata-编辑")
	@ApiOperation(value="device_pipepile_realdata-编辑", notes="device_pipepile_realdata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevicePipepileRealdata devicePipepileRealdata) {
		devicePipepileRealdataService.updateById(devicePipepileRealdata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_pipepile_realdata-通过id删除")
	@ApiOperation(value="device_pipepile_realdata-通过id删除", notes="device_pipepile_realdata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicePipepileRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_pipepile_realdata-批量删除")
	@ApiOperation(value="device_pipepile_realdata-批量删除", notes="device_pipepile_realdata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicePipepileRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_pipepile_realdata-通过id查询")
	@ApiOperation(value="device_pipepile_realdata-通过id查询", notes="device_pipepile_realdata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevicePipepileRealdata devicePipepileRealdata = devicePipepileRealdataService.getById(id);
		if(devicePipepileRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicePipepileRealdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devicePipepileRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePipepileRealdata devicePipepileRealdata) {
        return super.exportXls(request, devicePipepileRealdata, DevicePipepileRealdata.class, "device_pipepile_realdata");
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
        return super.importExcel(request, response, DevicePipepileRealdata.class);
    }

	 /**
	  *   实时数据更新(免登录版)
	  *
	  * @param devicePipepileRealdata
	  * @return
	  */
	 @AutoLog(value = "device_pipepile_realdata-实时数据更新")
	 @ApiOperation(value="device_pipepile_realdata-实时数据更新", notes="device_pipepile_realdata-实时数据更新")
	 @PostMapping(value = "/addOther1")
	 public Result<?> addOther1(@RequestBody DevicePipepileRealdata devicePipepileRealdata,HttpServletRequest req) {

		 if(StringUtil.isNotEmpty(devicePipepileRealdata.getShebeino())){
			 DevicePipepileRealdata realdata = new DevicePipepileRealdata();
			 realdata.setShebeino(devicePipepileRealdata.getShebeino());
			 // 更新历史表
			 DevicePipepileHistorydata devicePipepileHistorydata = new DevicePipepileHistorydata();
			 BeanUtils.copyProperties(devicePipepileRealdata, devicePipepileHistorydata);
			 devicePipepileHistorydataService.save(devicePipepileHistorydata);

			 QueryWrapper<DevicePipepileRealdata> queryWrapper = QueryGenerator.initQueryWrapper(realdata, req.getParameterMap());
			 List<DevicePipepileRealdata> ones = devicePipepileRealdataService.list(queryWrapper);
			 if(ones.size()>0){
				 devicePipepileRealdata.setId(ones.get(0).getId());
				 devicePipepileRealdataService.updateById(devicePipepileRealdata);
				 return Result.OK("软基设备实时数据更新成功!",devicePipepileRealdata);
			 }else{
				 devicePipepileRealdataService.save(devicePipepileRealdata);
				 return Result.OK("软基设备实时数据上传成功！",devicePipepileRealdata);
			 }
		 }else{
			 return Result.error("设备编号为空请重新上传！",devicePipepileRealdata);
		 }
	 }

}
