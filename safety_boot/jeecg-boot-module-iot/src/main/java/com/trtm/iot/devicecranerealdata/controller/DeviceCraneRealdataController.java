package com.trtm.iot.devicecranerealdata.controller;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.api.utils.crypt.MD5;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.xkcoding.http.util.StringUtil;
import com.trtm.iot.tadiao.entity.DeviceTowerHistorydata;
import com.trtm.iot.tadiao.entity.DeviceTowerRealdata;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicecranerealdata.service.IDeviceCraneRealdataService;

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
 * @Description: 龙门吊实时数据
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
@Api(tags="龙门吊实时数据")
@RestController
@RequestMapping("/devicecranerealdata/deviceCraneRealdata")
@Slf4j
public class DeviceCraneRealdataController extends JeecgController<DeviceCraneRealdata, IDeviceCraneRealdataService> {
	@Autowired
	private IDeviceCraneRealdataService deviceCraneRealdataService;
	 @Autowired
	 private IDeviceCraneHistorydataService deviceCraneHistorydataService;
	 @Autowired
	 private RedisUtil redisUtil;

	 private static final String alias = "ZD2023201";
	 private static final String comid = "2";
	 private static final String compvtkey ="589625dce895454dbff9782c299db156";
	 private static final String password="21218cca77804d2ba1922c33e0151105";
	 private static final String key="f1cd9351930d4e589922edbcf3b09a7c";

	 private static final String url = "http://api.v-box.net/box-data/api/we-data/login?alias="+alias+"&password="+password;//登陆
	 private static final String urlboxs = "http://api.v-box.net/box-data/api/we-data/boxs";//盒子列表（旧，单级分组）
	 private static final String urlboxlist = "http://api.v-box.net/box-data/api/we-data/boxlist";//盒子列表（新，多级分组）
	 private static final String urlrealgroups = "http://api.v-box.net/box-data/api/we-data/realgroups";//实时监控点分组列表
	 private static final String urlrealcfgs = "http://api.v-box.net/box-data/api/we-data/realcfgs";//实时监控点配置列表
	 private static final String urlrealdata = "http://api.v-box.net/box-data/api/we-data/realdata";//实时监控点数据列表
	 private static final String urlupdrealdata = "http://api.v-box.net/box-data/api/we-data/updrealdata";//修改实时监控点值
	 private static final String urlmonitors = "http://api.v-box.net/box-data/api/we-data/monitors";//历史监控点列表

	 /**
	  * 龙门吊测试,盒子列表（旧，单级分组）
	  */
	 @GetMapping(value = "/urlboxs")
	 public JSONObject queryurlboxs(long time, String sid) {
		 //String sid = getCode(time);//登陆拿到sid
		 String signing = "comid="+comid+"&compvtkey="+compvtkey+"&sid="+sid+"&ts="+time+"&key="+key;
		 String sign = MD5.toMD5(signing);
		 JSONObject sendObject = JSONUtil.createObj();
		 sendObject.set("comid", comid);
		 sendObject.set("compvtkey", compvtkey);
		 sendObject.set("sign", sign);
		 sendObject.set("ts", time);
		 sendObject.set("sid", sid);
		 JSONObject geturlboxs = geturlboxs(sendObject, urlboxs);
		 return geturlboxs;
	 }

	 /**
	  * 盒子列表（新，多级分组）
	  */
	 @GetMapping(value = "/urlboxlist")
	 public JSONObject queryurlboxlist(long time,String sid) {
//        String sid = getCode(time);//登陆拿到sid
		 String signing = "comid="+comid+"&compvtkey="+compvtkey+"&sid="+sid+"&ts="+time+"&key="+key;
		 String sign = MD5.toMD5(signing);
		 JSONObject sendObject = JSONUtil.createObj();
		 sendObject.set("comid", comid);
		 sendObject.set("compvtkey", compvtkey);
		 sendObject.set("sign", sign);
		 sendObject.set("ts", time);
		 sendObject.set("sid", sid);
		 JSONObject geturlboxs = geturlboxs(sendObject, urlboxlist);
		 return geturlboxs;
	 }

	 /**
	  * 实时监控点分组列表
	  */
	 @GetMapping(value = "/urlrealgroups")
	 public JSONObject queryurlrealgroups(String sid,long time,Integer boxId) {
//        Date date = new Date();
//        long time = date.getTime();
//        String sid = getCode(time);//登陆拿到sid
//        JSONObject queryurlboxlist = queryurlboxlist(time,sid);
//        JSONObject result = (JSONObject) queryurlboxlist.get("result");
//        JSONArray list = (JSONArray) result.get("list");
//        JSONObject o = (JSONObject) list.get(0);
//        JSONArray list1 = (JSONArray) o.get("boxList");
//        JSONObject o1 = (JSONObject) list1.get(0);
//        Integer boxId = (Integer) o1.get("boxId");

		 String signing = "boxId="+boxId+"&comid="+comid+"&compvtkey="+compvtkey+"&sid="+sid+"&ts="+time+"&key="+key;
		 String sign = MD5.toMD5(signing);
		 JSONObject sendObject = JSONUtil.createObj();
		 sendObject.set("comid", comid);
		 sendObject.set("compvtkey", compvtkey);
		 sendObject.set("sign", sign);
		 sendObject.set("ts", time);
		 sendObject.set("sid", sid);
		 String urlrealgroup = urlrealgroups + "?boxId="+boxId;
		 JSONObject geturlboxs = geturlboxs(sendObject, urlrealgroup);
		 return geturlboxs;
	 }

	 /**
	  * 实时监控点配置列表
	  */
	 @GetMapping(value = "/urlrealcfgs")
	 public JSONObject queryurlrealcfgs() {
		 Date date = new Date();
		 long time = date.getTime();
		 String sid = getCode(time);//登陆拿到sid
		 JSONObject queryurlboxlist = queryurlboxlist(time,sid);
		 JSONObject result = (JSONObject) queryurlboxlist.get("result");
		 JSONArray list = (JSONArray) result.get("list");
		 JSONObject o = (JSONObject) list.get(0);
		 JSONArray list1 = (JSONArray) o.get("boxList");
		 JSONObject o1 = (JSONObject) list1.get(0);
		 Integer boxId = (Integer) o1.get("boxId");

		 JSONObject queryurlrealgroups = queryurlrealgroups(sid, time, boxId);
		 System.out.println(queryurlrealgroups);

		 String signing = "boxId="+boxId+"&comid="+comid+"&compvtkey="+compvtkey+"&devType="+1+"&groupId="+2+"&sid="+sid+"&ts="+time+"&key="+key;
		 String sign = MD5.toMD5(signing);
		 JSONObject sendObject = JSONUtil.createObj();
		 sendObject.set("comid", comid);
		 sendObject.set("compvtkey", compvtkey);
		 sendObject.set("sign", sign);
		 sendObject.set("ts", time);
		 sendObject.set("sid", sid);
		 String urlrealgroup = urlrealcfgs + "?boxId="+boxId+"&groupId="+2+"&devType="+1;
		 JSONObject geturlboxs = geturlboxs(sendObject, urlrealgroup);
		 return geturlboxs;
	 }

	 /**
	  * 实时监控点数据列表
	  */
	 @GetMapping(value = "/urlrealdata")
	 public JSONObject queryurlrealdata() {
		 Date date = new Date();
		 long time = date.getTime();
		 String sid = getCode(time);//登陆拿到sid
		 JSONObject queryurlboxlist = queryurlboxlist(time,sid);
		 JSONObject result = (JSONObject) queryurlboxlist.get("result");
		 JSONArray list = (JSONArray) result.get("list");
		 JSONObject o = (JSONObject) list.get(0);
		 JSONArray list1 = (JSONArray) o.get("boxList");
		 JSONObject o1 = (JSONObject) list1.get(0);
		 Integer boxId = (Integer) o1.get("boxId");

		 JSONObject queryurlrealgroups = queryurlrealgroups(sid, time, boxId);
		 System.out.println(queryurlrealgroups);

		 String signing = "boxId="+boxId+"&comid="+comid+"&compvtkey="+compvtkey+"&devType="+1+"&groupId="+2+"&sid="+sid+"&ts="+time+"&key="+key;
		 String sign = MD5.toMD5(signing);
		 JSONObject sendObject = JSONUtil.createObj();
		 sendObject.set("comid", comid);
		 sendObject.set("compvtkey", compvtkey);
		 sendObject.set("sign", sign);
		 sendObject.set("ts", time);
		 sendObject.set("sid", sid);
		 String urlrealgroup = urlrealdata + "?boxId="+boxId+"&groupId="+2+"&devType="+1;
		 JSONObject geturlboxs = geturlboxs(sendObject, urlrealgroup);
		 return geturlboxs;
	 }

	 /**
	  * 放605省道3标梁场1#龙门吊里
	  */
	 public void inqueryurlrealdata() {
		 JSONObject queryurlrealdata = queryurlrealdata();
		 JSONObject result = (JSONObject) queryurlrealdata.get("result");
		 JSONArray list = (JSONArray) result.get("list");
		 QueryWrapper<DeviceCraneRealdata> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("device_code",2023062701);
		 DeviceCraneRealdata one = deviceCraneRealdataService.getOne(queryWrapper);
		 one.setCranedate(new Date());
		 for (Object record : list) {
			 JSONObject object = new JSONObject(record);
			 if (object.get("monitorId").toString().equals("20001")){
				 one.setWindSpeed(Double.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20002")){
				 one.setReservedAnalogquantityone(Double.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20003")){
				 one.setBigCarroute(Double.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20004")){
				 one.setSmallCarroute(Double.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20005")){
				 one.setBigCarleftlimit(Integer.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20006")){
				 one.setBigCarrightlimit(Integer.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20007")){
				 one.setMainHookload(Double.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20008")){
				 one.setReservedVicehookload(Double.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20009")){
				 one.setMainHookheight(Double.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20010")){
				 one.setReservedVicehookheight(Double.valueOf(object.get("value").toString()));
			 }else if (object.get("monitorId").toString().equals("20011")){
				 one.setSmallCar1brake1(Integer.valueOf(object.get("value").toString()));
			 }
		 }
		 deviceCraneRealdataService.updateById(one);
	 }
	 /**
	  * 修改实时监控点值
	  */
	 @GetMapping(value = "/urlupdrealdata")
	 public JSONObject queryurlupdrealdata() {
		 Date date = new Date();
		 long time = date.getTime();
		 String sid = getCode(time);//登陆拿到sid
		 JSONObject queryurlboxlist = queryurlboxlist(time,sid);
		 JSONObject result = (JSONObject) queryurlboxlist.get("result");
		 JSONArray list = (JSONArray) result.get("list");
		 JSONObject o = (JSONObject) list.get(0);
		 JSONArray list1 = (JSONArray) o.get("boxList");
		 JSONObject o1 = (JSONObject) list1.get(0);
		 Integer boxId = (Integer) o1.get("boxId");

		 JSONObject queryurlrealgroups = queryurlrealgroups(sid, time, boxId);
		 System.out.println(queryurlrealgroups);

		 String signing = "boxId="+boxId+"&comid="+comid+"&compvtkey="+compvtkey+"&devType="+1+"&monitorId="+20001+"&sid="+sid+"&ts="+time+"&value="+2+"&key="+key;
		 String sign = MD5.toMD5(signing);
		 JSONObject sendObject = JSONUtil.createObj();
		 sendObject.set("comid", comid);
		 sendObject.set("compvtkey", compvtkey);
		 sendObject.set("sign", sign);
		 sendObject.set("ts", time);
		 sendObject.set("sid", sid);
		 String urlrealgroup = urlupdrealdata + "?boxId="+boxId+"&devType="+1+"&monitorId="+20001+"&value="+2;
		 JSONObject geturlboxs = geturlboxs(sendObject, urlrealgroup);
		 return geturlboxs;
	 }

	 /**
	  * 历史监控点列表
	  */
	 @GetMapping(value = "/urlmonitors")
	 public JSONObject queryurlmonitors() {
		 Date date = new Date();
		 long time = date.getTime();
		 String sid = getCode(time);//登陆拿到sid
		 JSONObject queryurlboxlist = queryurlboxlist(time,sid);
		 JSONObject result = (JSONObject) queryurlboxlist.get("result");
		 JSONArray list = (JSONArray) result.get("list");
		 JSONObject o = (JSONObject) list.get(0);
		 JSONArray list1 = (JSONArray) o.get("boxList");
		 JSONObject o1 = (JSONObject) list1.get(0);
		 Integer boxId = (Integer) o1.get("boxId");

		 JSONObject queryurlrealgroups = queryurlrealgroups(sid, time, boxId);
		 System.out.println(queryurlrealgroups);

		 String signing = "boxId="+boxId+"&comid="+comid+"&compvtkey="+compvtkey+"&devType="+1+"&sid="+sid+"&ts="+time+"&key="+key;
		 String sign = MD5.toMD5(signing);
		 JSONObject sendObject = JSONUtil.createObj();
		 sendObject.set("comid", comid);
		 sendObject.set("compvtkey", compvtkey);
		 sendObject.set("sign", sign);
		 sendObject.set("ts", time);
		 sendObject.set("sid", sid);
		 String urlrealgroup = urlmonitors + "?boxId="+boxId+"&devType="+1;
		 JSONObject geturlboxs = geturlboxs(sendObject, urlrealgroup);
		 return geturlboxs;
	 }
	 // 登陆
	 public static String getCode(long time) {
		 String signing = "alias="+alias+"&comid="+comid+"&compvtkey="+compvtkey+"&password="+password+"&ts="+time+"&key="+key;
		 String sign = MD5.toMD5(signing);
		 System.out.println(sign);

		 JSONObject sendObject = JSONUtil.createObj();
		 sendObject.set("comid", comid);
		 sendObject.set("compvtkey", compvtkey);
		 sendObject.set("sign", sign);
		 sendObject.set("ts", time);

		 String body = HttpRequest.post(url)
				 .header("common", String.valueOf(sendObject))
				 .execute()
				 .body();
		 System.out.println(body);
		 JSONObject jsonObject1 = new JSONObject(body);
		 Integer integer = (Integer) jsonObject1.get("code");
		 if (integer == 200){
			 JSONObject result = (JSONObject) jsonObject1.get("result");
			 return (String) result.get("sid");
		 }else {
			 return "登录失败！！！";
		 }
	 }

	 public static JSONObject geturlboxs(JSONObject sendObject,String url) {
		 String body = HttpRequest.post(url)
				 .header("common", String.valueOf(sendObject))
				 .execute()
				 .body();
		 System.out.println(body);
		 JSONObject jsonObject1 = new JSONObject(body);
		 return jsonObject1;
	 }
	 /**
	  * 分页列表查询
	  *
	  * @param deviceCraneRealdata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "龙门吊实时数据-分页列表查询")
	 @ApiOperation(value="龙门吊实时数据-分页列表查询", notes="龙门吊实时数据-分页列表查询")
	 @GetMapping(value = "/list0")
	 public Result<?> queryPageList0(DeviceCraneRealdata deviceCraneRealdata,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (deviceCraneRealdata.getDeviceCode() == null) {
			 if (!"null".equals(shebei)) {
				 deviceCraneRealdata.setDeviceCode(shebei);
			 }else {
				 deviceCraneRealdata.setDeviceCode("空");
			 }
		 }
//		inqueryurlrealdata();//放605省道3标梁场1#龙门吊里
// 		 deviceCraneRealdata.setDeviceType(2);
		 QueryWrapper<DeviceCraneRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneRealdata, req.getParameterMap());
		 Page<DeviceCraneRealdata> page = new Page<DeviceCraneRealdata>(pageNo, pageSize);
		 IPage<DeviceCraneRealdata> pageList = deviceCraneRealdataService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	/**
	 * 分页列表查询
	 *
	 * @param deviceCraneRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "龙门吊实时数据-分页列表查询")
	@ApiOperation(value="龙门吊实时数据-分页列表查询", notes="龙门吊实时数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceCraneRealdata deviceCraneRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (deviceCraneRealdata.getDeviceCode() == null) {
			if (!"null".equals(shebei)) {
				deviceCraneRealdata.setDeviceCode(shebei);
			}else {
				deviceCraneRealdata.setDeviceCode("空");
			}
		}
//		inqueryurlrealdata();//放605省道3标梁场1#龙门吊里
		deviceCraneRealdata.setDeviceType(2);
		QueryWrapper<DeviceCraneRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneRealdata, req.getParameterMap());
		Page<DeviceCraneRealdata> page = new Page<DeviceCraneRealdata>(pageNo, pageSize);
		IPage<DeviceCraneRealdata> pageList = deviceCraneRealdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param deviceCraneRealdata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "架桥机实时数据-分页列表查询")
	 @ApiOperation(value="架桥机实时数据-分页列表查询", notes="架桥机实时数据-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(DeviceCraneRealdata deviceCraneRealdata,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (deviceCraneRealdata.getDeviceCode() == null) {
			 if (!"null".equals(shebei)) {
				 deviceCraneRealdata.setDeviceCode(shebei);
			 }else {
				 deviceCraneRealdata.setDeviceCode("空");
			 }
		 }
		 deviceCraneRealdata.setDeviceType(3);
		 QueryWrapper<DeviceCraneRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneRealdata, req.getParameterMap());
		 Page<DeviceCraneRealdata> page = new Page<DeviceCraneRealdata>(pageNo, pageSize);
		 IPage<DeviceCraneRealdata> pageList = deviceCraneRealdataService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param deviceCraneRealdata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "提梁机实时数据-分页列表查询")
	 @ApiOperation(value="提梁机实时数据-分页列表查询", notes="提梁机实时数据-分页列表查询")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(DeviceCraneRealdata deviceCraneRealdata,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (deviceCraneRealdata.getDeviceCode() == null) {
			 if (!"null".equals(shebei)) {
				 deviceCraneRealdata.setDeviceCode(shebei);
			 }else {
				 deviceCraneRealdata.setDeviceCode("空");
			 }
		 }
		 deviceCraneRealdata.setDeviceType(4);
		 QueryWrapper<DeviceCraneRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneRealdata, req.getParameterMap());
		 Page<DeviceCraneRealdata> page = new Page<DeviceCraneRealdata>(pageNo, pageSize);
		 IPage<DeviceCraneRealdata> pageList = deviceCraneRealdataService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param deviceCraneRealdata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "汽车吊实时数据-分页列表查询")
	 @ApiOperation(value="汽车吊实时数据-分页列表查询", notes="汽车吊实时数据-分页列表查询")
	 @GetMapping(value = "/list3")
	 public Result<?> queryPageList3(DeviceCraneRealdata deviceCraneRealdata,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (deviceCraneRealdata.getDeviceCode() == null) {
			 if (!"null".equals(shebei)) {
				 deviceCraneRealdata.setDeviceCode(shebei);
			 }else {
				 deviceCraneRealdata.setDeviceCode("空");
			 }
		 }
		 deviceCraneRealdata.setDeviceType(5);
		 QueryWrapper<DeviceCraneRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneRealdata, req.getParameterMap());
		 Page<DeviceCraneRealdata> page = new Page<DeviceCraneRealdata>(pageNo, pageSize);
		 IPage<DeviceCraneRealdata> pageList = deviceCraneRealdataService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param deviceCraneRealdata
	 * @return
	 */
	@AutoLog(value = "龙门吊实时数据-添加")
	@ApiOperation(value="龙门吊实时数据-添加", notes="龙门吊实时数据-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceCraneRealdata deviceCraneRealdata) {
		deviceCraneRealdataService.save(deviceCraneRealdata);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param deviceCraneRealdata
	 * @return
	 */
	@AutoLog(value = "龙门吊实时数据-编辑")
	@ApiOperation(value="龙门吊实时数据-编辑", notes="龙门吊实时数据-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceCraneRealdata deviceCraneRealdata) {
		deviceCraneRealdataService.updateById(deviceCraneRealdata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "龙门吊实时数据-通过id删除")
	@ApiOperation(value="龙门吊实时数据-通过id删除", notes="龙门吊实时数据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceCraneRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "龙门吊实时数据-批量删除")
	@ApiOperation(value="龙门吊实时数据-批量删除", notes="龙门吊实时数据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceCraneRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "龙门吊实时数据-通过id查询")
	@ApiOperation(value="龙门吊实时数据-通过id查询", notes="龙门吊实时数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceCraneRealdata deviceCraneRealdata = deviceCraneRealdataService.getById(id);
		if(deviceCraneRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceCraneRealdata);
	}


	 /**
	  * 通过id查询
	  *
	  * @param command
	  * @return
	  */
	 @AutoLog(value = "龙门吊- 控制器接口")
	 @ApiOperation(value="龙门吊- 控制器接口", notes="龙门吊- 控制器接口")
	 @GetMapping(value = "/controlByWeb")
	 public Result<?> controlByWeb(@RequestParam(name="command",required=true) String command) {
		 // 验证连接发送命令：登录：设备编号|login
		 String send = send(command);
		 //控制命令：设备编号|@,1,1,0,1,0,1,0,1,0,1,#

		 return Result.OK(send);
	 }

	 public String send(String message){
		 // 服务器地址和端口
		 String serverAddress = "192.168.0.179"; // 替换为服务器 IP
		 int serverPort = 8897; // 替换为服务器端口
		// Socket socket = null;
		 String msg = "0";
		 // 创建 Socket 并连接服务器
		 try( Socket socket = new Socket(serverAddress, serverPort);)  {

			 System.out.println("已连接到服务器: " + serverAddress + ":" + serverPort);
			 // 获取输出流
			 OutputStream outputStream = socket.getOutputStream();
			 // 发送数据
			 byte[] data = message.getBytes(StandardCharsets.UTF_8);
			 outputStream.write(data);
			 outputStream.flush();
			 System.out.println("数据已发送: " + message);
			 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 // 4. 接收响应
			 msg = reader.readLine();
			 socket.close();
		 } catch (IOException e) {
			 System.err.println("连接或发送数据时发生错误: " + e.getMessage());
		 }
//		 finally {
//			 // 确保 socket 被关闭
//			 if (socket != null && !socket.isClosed()) {
//				 try {
//					 socket.close();
//					 System.out.println("socket 已关闭！");
//				 } catch (IOException e) {
//					 e.printStackTrace();
//				 }
//			 }
//
//		 }
		 return msg;
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param deviceCraneRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceCraneRealdata deviceCraneRealdata) {
        return super.exportXls(request, deviceCraneRealdata, DeviceCraneRealdata.class, "龙门吊实时数据");
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
        return super.importExcel(request, response, DeviceCraneRealdata.class);
    }




}
