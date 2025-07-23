package com.trtm.iot.devicemixpilestatic.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tsyjzbStatistics.entity.TSyjzbStatistics;
import com.trtm.iot.tsyjzbStatistics.vo.statisticsVO;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.trtm.iot.devicemixpilestatic.service.IDeviceMixpileStaticService;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: device_mixpile_static
 * @Author: jeecg-boot
 * @Date:   2022-01-24
 * @Version: V1.0
 */
@Api(tags="device_mixpile_static")
@RestController
@RequestMapping("/devicemixpilestatic/deviceMixpileStatic")
@Slf4j
public class DeviceMixpileStaticController extends JeecgController<DeviceMixpileStatic, IDeviceMixpileStaticService> {
	@Autowired
	private IDeviceMixpileStaticService deviceMixpileStaticService;
	 @Autowired
	 private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param deviceMixpileStatic
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_mixpile_static-分页列表查询")
	@ApiOperation(value="device_mixpile_static-分页列表查询", notes="device_mixpile_static-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceMixpileStatic deviceMixpileStatic,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (deviceMixpileStatic.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				deviceMixpileStatic.setShebeino(shebei);
			} else {
				deviceMixpileStatic.setShebeino("空");
			}
		}
		QueryWrapper<DeviceMixpileStatic> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileStatic, req.getParameterMap());
		Page<DeviceMixpileStatic> page = new Page<DeviceMixpileStatic>(pageNo, pageSize);
		IPage<DeviceMixpileStatic> pageList = deviceMixpileStaticService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 每周设备合格率排名统计
	  *
	  * @param deviceMixpileStatic
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_mixpile_static-每周设备合格率排名统计")
	 @ApiOperation(value="device_mixpile_static-每周设备合格率排名统计", notes="device_mixpile_static-每周设备合格率排名统计")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(DeviceMixpileStatic deviceMixpileStatic,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceMixpileStatic.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 deviceMixpileStatic.setShebeino(shebei);
			 } else {
				 deviceMixpileStatic.setShebeino("空");
			 }
		 }
		 QueryWrapper<DeviceMixpileStatic> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileStatic, req.getParameterMap());
		 queryWrapper.select("(sum(pilecount)-sum(chaobiaototal))/sum(pilecount)*100 allash,shebeino");
		 queryWrapper.last(" and YEARWEEK(date_format(stadate,'%Y-%m-%d')) = YEARWEEK(now())-1 " +
				 "group by shebeino order by allash desc");
		 Page<DeviceMixpileStatic> page = new Page<DeviceMixpileStatic>(pageNo, pageSize);
		 IPage<DeviceMixpileStatic> pageList = deviceMixpileStaticService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 每周设备工作量排名统计
	  *
	  * @param deviceMixpileStatic
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_mixpile_static-每周设备工作量排名统计")
	 @ApiOperation(value="device_mixpile_static-每周设备工作量排名统计", notes="device_mixpile_static-每周设备工作量排名统计")
	 @GetMapping(value = "/list3")
	 public Result<?> queryPageList3(DeviceMixpileStatic deviceMixpileStatic,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceMixpileStatic.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 deviceMixpileStatic.setShebeino(shebei);
			 } else {
				 deviceMixpileStatic.setShebeino("空");
			 }
		 }
		 QueryWrapper<DeviceMixpileStatic> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileStatic, req.getParameterMap());
		 queryWrapper.select("sum(pilecount) pilecount,sum(chaobiaototal) chaobiaototal,sum(worklength) worklength,shebeino");
		 queryWrapper.last(" and YEARWEEK(date_format(stadate,'%Y-%m-%d')) = YEARWEEK(now())-1 " +
				 "group by shebeino order by pilecount desc,worklength desc");
		 Page<DeviceMixpileStatic> page = new Page<DeviceMixpileStatic>(pageNo, pageSize);
		 IPage<DeviceMixpileStatic> pageList = deviceMixpileStaticService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 设备工效统计
	  *
	  * @param deviceMixpileStatic
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_mixpile_static-设备工效统计")
	 @ApiOperation(value="device_mixpile_static-设备工效统计", notes="device_mixpile_static-设备工效统计")
	 @GetMapping(value = "/list4")
	 public Result<?> queryPageList4(DeviceMixpileStatic deviceMixpileStatic,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceMixpileStatic.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 deviceMixpileStatic.setShebeino(shebei);
			 } else {
				 deviceMixpileStatic.setShebeino("空");
			 }
		 }
		 QueryWrapper<DeviceMixpileStatic> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileStatic, req.getParameterMap());
		 queryWrapper.select("sum(pilecount) pilecount,sum(chaobiaototal) chaobiaototal,sum(worklength) worklength,shebeino");
		 queryWrapper.last(" and YEARWEEK(date_format(stadate,'%Y-%m-%d')) = YEARWEEK(now())-1 " +
				 "group by shebeino order by pilecount desc,worklength desc");
		 Page<DeviceMixpileStatic> page = new Page<DeviceMixpileStatic>(pageNo, pageSize);
		 IPage<DeviceMixpileStatic> pageList = deviceMixpileStaticService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	 @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
	 @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
	 @GetMapping(value = "/stalist")
	 @PermissionData(pageComponent = "snjbz/devicemixpilestatic/DeviceMixpileSheBeiStaticList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
	 public Result<?> queryPagestaList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
									   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									   HttpServletRequest req, String sys_depart_orgcode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String sysOrgCode = null;
		 if (!StringUtils.isEmpty(sys_depart_orgcode)) {
			 sysOrgCode = sys_depart_orgcode;
		 } else {
			 sysOrgCode = loginUser.getOrgCode();
		 }
		 List<ShebeiInfo> shebeiInfoList = shebeiInfoService.usershebeiList(sysOrgCode, Arrays.asList("16", "19", "53", "54"));
		 List list = new ArrayList();
		 if (shebeiInfoList.size() > 0) {
			 for (ShebeiInfo shebeiInfo : shebeiInfoList) {
				 list.add(shebeiInfo.getSbjno());
			 }
		 }
		 QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
		 queryWrapper.select("id,shebeino,round( sum(pile_realdep), 2 ) pile_realdep,round( sum(pile_downbeton)+sum(pile_uobeton), 2 ) pile_uobeton,round( sum(pile_cement), 2 ) pile_cement,COUNT(shebeino) alertstate");
		 if (list.size() > 0) {
			 queryWrapper.in("shebeino", list);
		 }
		 queryWrapper.groupBy("shebeino");
		 Page<DeviceMixpileHistorydataOne> page = new Page<DeviceMixpileHistorydataOne>(pageNo, pageSize);
		 IPage<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.page(page, queryWrapper);
		 return Result.OK(pageList);

	 }

	 /**
	  * 按照设备统计和标段统计
	  *
	  * @param deviceMixpileStatic
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_mixpile_static-按照设备统计和标段统计")
	 @ApiOperation(value="device_mixpile_static-按照设备统计和标段统计", notes="device_mixpile_static-按照设备统计和标段统计")
	 @GetMapping(value = "/list6")
	 public Result<?> queryPageList6(DeviceMixpileStatic deviceMixpileStatic,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req,String sys_depart_orgcode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String sysOrgCode = null;
		 if (!StringUtils.isEmpty(sys_depart_orgcode)) {
			 sysOrgCode = sys_depart_orgcode;
		 } else {
			 sysOrgCode = loginUser.getOrgCode();
		 }
		 List<ShebeiInfo> shebeiInfoList = shebeiInfoService.usershebeiList(sysOrgCode, Arrays.asList("16", "19", "53", "54"));
		 List list = new ArrayList();
		 if (shebeiInfoList.size() > 0) {
			 for (ShebeiInfo shebeiInfo : shebeiInfoList) {
				 list.add(shebeiInfo.getSbjno());
			 }
		 }
		 QueryWrapper<DeviceMixpileStatic> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileStatic, req.getParameterMap());
		 queryWrapper.select("id, "+"shebeino , "  +
				 " FLOOR(SUM( pilecount )) pilecount, " +
				 " round( SUM( IFNULL( allbeton, 0 )), 2 ) allbeton, " +
				 " round( SUM( IFNULL( worklength, 0 )), 2 ) worklength, " +
				 " round( SUM( IFNULL( allcement, 0 )), 2 ) allcement");
		 if (list.size() > 0) {
			 queryWrapper.in("shebeino", list);
		 }
		 queryWrapper.groupBy("shebeino");
		 Page<DeviceMixpileStatic> page = new Page<DeviceMixpileStatic>(pageNo, pageSize);
		 IPage<DeviceMixpileStatic> pageList = deviceMixpileStaticService.page(page, queryWrapper);

		 return Result.OK(pageList);
	 }

	 /**
	  * 按照设备统计和标段统计
	  *
	  * @param deviceMixpileStatic
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_mixpile_static-按照设备统计和标段统计")
	 @ApiOperation(value="device_mixpile_static-按照设备统计和标段统计", notes="device_mixpile_static-按照设备统计和标段统计")
	 @GetMapping(value = "/listtj6")
	 public Result<?> queryPageListtj6(DeviceMixpileStatic deviceMixpileStatic,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req,String sys_depart_orgcode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String sysOrgCode = null;
		 if (!StringUtils.isEmpty(sys_depart_orgcode)) {
			 sysOrgCode = sys_depart_orgcode;
		 } else {
			 sysOrgCode = loginUser.getOrgCode();
		 }
		 List<ShebeiInfo> shebeiInfoList = shebeiInfoService.usershebeiList(sysOrgCode, Arrays.asList("16", "19", "53", "54"));
		 List list = new ArrayList();
		 if (shebeiInfoList.size() > 0) {
			 for (ShebeiInfo shebeiInfo : shebeiInfoList) {
				 list.add(shebeiInfo.getSbjno());
			 }
		 }
		 QueryWrapper<DeviceMixpileStatic> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileStatic, req.getParameterMap());
		 queryWrapper.select("id, "+"shebeino , "  +
				 " FLOOR(SUM( pilecount )) pilecount, " +
				 " round( SUM( IFNULL( allbeton, 0 )), 2 ) allbeton, " +
				 " round( SUM( IFNULL( worklength, 0 )), 2 ) worklength, " +
				 " round( SUM( IFNULL( allcement, 0 )), 2 ) allcement");
		 if (list.size() > 0) {
			 queryWrapper.in("shebeino", list);
		 }
		 queryWrapper.groupBy("shebeino");
		 Page<DeviceMixpileStatic> page = new Page<DeviceMixpileStatic>(pageNo, pageSize);
		 IPage<DeviceMixpileStatic> pageList = deviceMixpileStaticService.page(page, queryWrapper);

		 return Result.OK(pageList);
	 }
	 @AutoLog(value = "device_mixpile_historydata_one-分页列表查询")
	 @ApiOperation(value = "device_mixpile_historydata_one-分页列表查询", notes = "device_mixpile_historydata_one-分页列表查询")
	 @GetMapping(value = "/stalists")
	 public Result<?> queryPagestaLists(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
									   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									   HttpServletRequest req, String sys_depart_orgcode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String sysOrgCode = null;
		 if (!StringUtils.isEmpty(sys_depart_orgcode)) {
			 sysOrgCode = sys_depart_orgcode;
		 } else {
			 sysOrgCode = loginUser.getOrgCode();
		 }
		 List<ShebeiInfo> shebeiInfoList = shebeiInfoService.usershebeiList(sysOrgCode, Arrays.asList("16", "19", "53", "54"));
		 List list = new ArrayList();
		 if (shebeiInfoList.size() > 0) {
			 for (ShebeiInfo shebeiInfo : shebeiInfoList) {
				 list.add(shebeiInfo.getSbjno());
			 }
		 }
		 QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
		 queryWrapper.select("id,shebeino,round( sum(pile_realdep), 2 ) pile_realdep,round( sum(pile_downbeton)+sum(pile_uobeton), 2 ) pile_uobeton,round( sum(pile_cement), 2 ) pile_cement,COUNT(shebeino) alertstate");
		 if (list.size() > 0) {
			 queryWrapper.in("shebeino", list);
		 }
		 queryWrapper.groupBy("shebeino");
		 List<DeviceMixpileHistorydataOne> data = deviceMixpileHistorydataOneService.list(queryWrapper);
		 for (DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 :data){
			 ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceMixpileHistorydataOne1.getShebeino());
			 deviceMixpileHistorydataOne1.setShebeino(selectshebeione.getSbname());
		 }
		 return Result.OKs(data);

	 }
	 /**
	  * 分页列表查询
	  *
	  * @param deviceMixpileStatic
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "device_mixpile_static-分页列表查询")
	 @ApiOperation(value="device_mixpile_static-分页列表查询", notes="device_mixpile_static-分页列表查询")
	 @GetMapping(value = "/list1")

	 public Result<?> queryPageList1(DeviceMixpileStatic deviceMixpileStatic,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {

		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 DeviceMixpileHistorydataOne deviceMixpileHistorydataOne = new DeviceMixpileHistorydataOne();
		 if (deviceMixpileHistorydataOne.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 deviceMixpileHistorydataOne.setShebeino(shebei);
			 }else {
				 deviceMixpileHistorydataOne.setShebeino("空");
			 }
		 }

		 QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());;
		 queryWrapper.select("pile_mileage mileage, " +
				 " COUNT( 1 ) pilecount, " +
				 " round( SUM( IFNULL( pile_downbeton, 0 ) + IFNULL( pile_uobeton, 0 ) ), 2 ) allash, " +
				 " round( SUM( IFNULL( pile_realdep, 0 )), 2 ) worklength, " +
				 " round( SUM( IFNULL( pile_cement, 0 )), 2 ) allcement,shebeino");
		 if(StringUtil.isNotEmpty(req.getParameter("mileage"))){
			 queryWrapper.like("pile_mileage",req.getParameter("mileage"));
		 }
		 if(StringUtil.isNotEmpty(req.getParameter("stadate_begin"))){
			 queryWrapper.ge("pile_time",req.getParameter("stadate_begin"));
		 }
		 if(StringUtil.isNotEmpty(req.getParameter("stadate_end"))){
			 queryWrapper.le("pile_time",req.getParameter("stadate_end"));
		 }
		 queryWrapper.groupBy("pile_mileage","shebeino");
		// List<DeviceMixpileStatic> maps = deviceMixpileHistorydataOneService.listMaps(queryWrapper);

		 Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
		 IPage<Map<String, Object>> pageList = deviceMixpileHistorydataOneService.pageMaps(page, queryWrapper);

		 return Result.OK(pageList);
	 }


	/**
	 *   添加
	 *
	 * @param deviceMixpileStatic
	 * @return
	 */
	@AutoLog(value = "device_mixpile_static-添加")
	@ApiOperation(value="device_mixpile_static-添加", notes="device_mixpile_static-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceMixpileStatic deviceMixpileStatic) {
		deviceMixpileStaticService.save(deviceMixpileStatic);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param deviceMixpileStatic
	 * @return
	 */
	@AutoLog(value = "device_mixpile_static-编辑")
	@ApiOperation(value="device_mixpile_static-编辑", notes="device_mixpile_static-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceMixpileStatic deviceMixpileStatic) {
		deviceMixpileStaticService.updateById(deviceMixpileStatic);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_mixpile_static-通过id删除")
	@ApiOperation(value="device_mixpile_static-通过id删除", notes="device_mixpile_static-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceMixpileStaticService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_mixpile_static-批量删除")
	@ApiOperation(value="device_mixpile_static-批量删除", notes="device_mixpile_static-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceMixpileStaticService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_mixpile_static-通过id查询")
	@ApiOperation(value="device_mixpile_static-通过id查询", notes="device_mixpile_static-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceMixpileStatic deviceMixpileStatic = deviceMixpileStaticService.getById(id);
		if(deviceMixpileStatic==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceMixpileStatic);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceMixpileStatic
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileStatic deviceMixpileStatic) {

        return super.exportXls(request, deviceMixpileStatic, DeviceMixpileStatic.class, "device_mixpile_static");
    }
	 /**
	  * 导出excel
	  *
	  * @param request
	  * @param
	  */
	 @RequestMapping(value = "/exportXlsTJ")
	 public ModelAndView exportXlsTJ(HttpServletRequest request, DeviceMixpileHistorydataOne deviceMixpileStatic, String sys_depart_orgcode) {
		 // Step.1 组装查询条件查询数据
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String shebei = String.valueOf(redisUtil.get(sysUser.getId() + "change"));
		 if (deviceMixpileStatic.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 deviceMixpileStatic.setShebeino(shebei);
			 }else {
				 deviceMixpileStatic.setShebeino("空");
			 }
		 }
		 QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileStatic, request.getParameterMap());
		 queryWrapper.select("pile_mileage mileage, " +
				 " COUNT( 1 ) pilecount, " +
				 " round( SUM( IFNULL( pile_downbeton, 0 ) + IFNULL( pile_uobeton, 0 ) ), 2 ) allash, " +
				 " round( SUM( IFNULL( pile_realdep, 0 )), 2 ) worklength, " +
				 " round( SUM( IFNULL( pile_cement, 0 )), 2 ) allcement");
		 queryWrapper.groupBy("pile_mileage");
		 //Step.2 获取导出数据
		 List<Map<String,Object>> queryList = deviceMixpileHistorydataOneService.listMaps(queryWrapper);
		 List<DeviceMixpileStatic> list=new ArrayList<>();
		 if(queryList.size()>0){
		 for (Map<String, Object> stringObjectMap : queryList) {
			 DeviceMixpileStatic deviceMixpileStatic1=new DeviceMixpileStatic();
			 String mileage = String.valueOf(stringObjectMap.get("mileage"));
			 String pilecount = String.valueOf(stringObjectMap.get("pilecount"));
			 String allash = String.valueOf(stringObjectMap.get("allash"));
			 String worklength = String.valueOf(stringObjectMap.get("worklength"));
			 String allcement = String.valueOf(stringObjectMap.get("allcement"));
			 deviceMixpileStatic1.setAllash(allash);
			 deviceMixpileStatic1.setAllcement(allcement);
			 deviceMixpileStatic1.setMileage(mileage);
			 deviceMixpileStatic1.setWorklength(worklength);
			 deviceMixpileStatic1.setPilecount(pilecount);
			 list.add(deviceMixpileStatic1);
		 }
		 }

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 // Step.4 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "软基数据统计分析列表");
		 mv.addObject(NormalExcelConstants.CLASS, DeviceMixpileStatic.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("软基数据统计分析列表", "导出人:" + sysUser.getRealname(), "拌合站主表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, list);
		 return mv;
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
        return super.importExcel(request, response, DeviceMixpileStatic.class);
    }

}
