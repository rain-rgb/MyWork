package com.trtm.iot.devicetunnelposition.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicetunnelposition.entity.DeviceTunnelPositiondata;
import com.trtm.iot.devicetunnelposition.service.IDeviceTunnelPositiondataService;

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
 * @Description: 隧道人员定位表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-26
 * @Version: V1.0
 */
@Api(tags="隧道人员定位表信息")
@RestController
@RequestMapping("/devicetunnelposition/deviceTunnelPositiondata")
@Slf4j
public class DeviceTunnelPositiondataController extends JeecgController<DeviceTunnelPositiondata, IDeviceTunnelPositiondataService> {
	@Autowired
	private IDeviceTunnelPositiondataService deviceTunnelPositiondataService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;
	/**
	 * 分页列表查询
	 *
	 * @param deviceTunnelPositiondata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "隧道人员定位表信息-分页列表查询")
	@ApiOperation(value="隧道人员定位表信息-分页列表查询", notes="隧道人员定位表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceTunnelPositiondata deviceTunnelPositiondata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (deviceTunnelPositiondata.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				deviceTunnelPositiondata.setShebeino(shebei);
			}else {
				deviceTunnelPositiondata.setShebeino("空");
			}
		}
		QueryWrapper<DeviceTunnelPositiondata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondata, req.getParameterMap());
		Page<DeviceTunnelPositiondata> page = new Page<DeviceTunnelPositiondata>(pageNo, pageSize);
		IPage<DeviceTunnelPositiondata> pageList = deviceTunnelPositiondataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param deviceTunnelPositiondata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "隧道人员定位表信息-查询每个人的最新数据")
	 @ApiOperation(value="隧道人员定位表信息-查询每个人的最新数据", notes="隧道人员定位表信息-查询每个人的最新数据")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(DeviceTunnelPositiondata deviceTunnelPositiondata,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 String[] split = shebei.split(",");
		 String she="";
		 if (split.length>0){
			 she="'"+StringUtils.join(split, "','")+"'";//数据格式   '','',''
		 }
		 String jzwz=null;
		 if(deviceTunnelPositiondata.getJzwz()!=null){
			 jzwz=deviceTunnelPositiondata.getJzwz();
		 }
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String format = sdf.format(new Date());
		 String dataTime= format+" 00:00:00";
		 List<DeviceTunnelPositiondata> deviceTunnelPositiondata1 = deviceTunnelPositiondataService.queryLists(jzwz,she,dataTime);
		 return Result.OK(deviceTunnelPositiondata1);
	 }

	 /**
	  * 人员统计
	  *
	  * @param deviceTunnelPositiondata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "隧道人员定位表信息-人员统计")
	 @ApiOperation(value="隧道人员定位表信息-人员统计", notes="隧道人员定位表信息-人员统计")
	 @GetMapping(value = "/tjlist")
	 public Result<?> queryPagetjList(DeviceTunnelPositiondata deviceTunnelPositiondata,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
//		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//		 //查询到他的设备编号
//		 if (deviceTunnelPositiondata.getShebeino() == null) {
//			 if (!"null".equals(shebei)) {
//				 deviceTunnelPositiondata.setShebeino(shebei);
//			 }else {
//				 deviceTunnelPositiondata.setShebeino("空");
//			 }
//		 }
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 String[] split = shebei.split(",");
		 List<String> shebeilist = new ArrayList<>();
		 Collections.addAll(shebeilist, split);
		 deviceTunnelPositiondata.setJzwz("*" + deviceTunnelPositiondata.getJzwz() + "*");
		 QueryWrapper<DeviceTunnelPositiondata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondata, req.getParameterMap());
		 queryWrapper.select("count(DISTINCT(username)) as username");
		 queryWrapper.in("shebeino",shebeilist);//用户权限下设备编号
		 List<DeviceTunnelPositiondata> pageList = deviceTunnelPositiondataService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param deviceTunnelPositiondata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "隧道人员定位表信息-查询每个人的最新数据")
	 @ApiOperation(value="隧道人员定位表信息-查询每个人的最新数据", notes="隧道人员定位表信息-查询每个人的最新数据")
	 @GetMapping(value = "/list3")
	 public Result<?> queryPageList3(DeviceTunnelPositiondata deviceTunnelPositiondata,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 String[] split = shebei.split(",");
		 String she="";
		 if (split.length>0){
			 she="'"+StringUtils.join(split, "','")+"'";//数据格式   '','',''
		 }
		 String jzwz=null;
		 if(deviceTunnelPositiondata.getJzwz()!=null){
			 jzwz=deviceTunnelPositiondata.getJzwz();
		 }
		 List records1=new ArrayList<>();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String format = sdf.format(new Date());
		 String dataTime= format+" 00:00:00";
		 List<DeviceTunnelPositiondata> deviceTunnelPositiondata1 = deviceTunnelPositiondataService.queryLists(jzwz,she,dataTime);
		 for (DeviceTunnelPositiondata deviceTunnelPositiondata2 : deviceTunnelPositiondata1){
		 	DeviceTunnelPositiondata deviceTunnelPositiondata3 = new DeviceTunnelPositiondata();
			 Integer id = deviceTunnelPositiondata2.getId();
			 String username = deviceTunnelPositiondata2.getUsername();
			 String card = deviceTunnelPositiondata2.getCard();
			 Date datatime = deviceTunnelPositiondata2.getDatatime();
			 Double dv = deviceTunnelPositiondata2.getDv();
			 Date dvtime = deviceTunnelPositiondata2.getDvtime();
			 String jz = deviceTunnelPositiondata2.getJz();
			 Double jzdv = deviceTunnelPositiondata2.getJzdv();
			 Double jzkou = deviceTunnelPositiondata2.getJzkou();
			 String shebeino = deviceTunnelPositiondata2.getShebeino();
			 ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeino);
			 String jzwz1 = deviceTunnelPositiondata2.getJzwz();
			 String departmentid = deviceTunnelPositiondata2.getDepartmentid();
			 String departname = deviceTunnelPositiondata2.getDepartname();
			 Double nx = deviceTunnelPositiondata2.getNx();
			 Double ny = deviceTunnelPositiondata2.getNy();
			 Double voltages = deviceTunnelPositiondata2.getVoltages();
			 Date uploadtime = deviceTunnelPositiondata2.getUploadtime();
			 deviceTunnelPositiondata3.setId(id);
			 deviceTunnelPositiondata3.setShebeino(shebeiInfo.getSbname());
			 deviceTunnelPositiondata3.setUsername(username);
			 deviceTunnelPositiondata3.setCard(card);
			 deviceTunnelPositiondata3.setDatatime(datatime);
			 deviceTunnelPositiondata3.setDv(dv);
			 deviceTunnelPositiondata3.setDvtime(dvtime);
			 deviceTunnelPositiondata3.setJz(jz);
			 deviceTunnelPositiondata3.setJzdv(jzdv);
			 deviceTunnelPositiondata3.setJzkou(jzkou);
			 deviceTunnelPositiondata3.setJzwz(jzwz1);
			 deviceTunnelPositiondata3.setDepartmentid(departmentid);
			 deviceTunnelPositiondata3.setDepartname(departname);
			 deviceTunnelPositiondata3.setNx(nx);
			 deviceTunnelPositiondata3.setNy(ny);
			 deviceTunnelPositiondata3.setVoltages(voltages);
			 deviceTunnelPositiondata3.setUploadtime(uploadtime);
			 records1.add(deviceTunnelPositiondata3);
		 }
		 return Result.OK(records1);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param deviceTunnelPositiondata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "隧道人员定位表信息-班组统计")
	 @ApiOperation(value="隧道人员定位表信息-班组统计", notes="隧道人员定位表信息-班组统计")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(DeviceTunnelPositiondata deviceTunnelPositiondata,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
//		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//		 //查询到他的设备编号
//		 if (deviceTunnelPositiondata.getShebeino() == null) {
//			 if (!"null".equals(shebei)) {
//				 deviceTunnelPositiondata.setShebeino(shebei);
//			 }else {
//				 deviceTunnelPositiondata.setShebeino("空");
//			 }
//		 }
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 String[] split = shebei.split(",");
		 List<String> shebeilist = new ArrayList<>();
		 Collections.addAll(shebeilist, split);
		 if(deviceTunnelPositiondata.getJzwz()!=null){
			 deviceTunnelPositiondata.setJzwz("*"+deviceTunnelPositiondata.getJzwz()+"*");
		 }
		 if(deviceTunnelPositiondata.getJz()!=null){
			 deviceTunnelPositiondata.setJz("*"+deviceTunnelPositiondata.getJz()+"*");
		 }
		 QueryWrapper<DeviceTunnelPositiondata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondata, req.getParameterMap());
		 queryWrapper.select("count(DISTINCT(username)) as DepartmentID","DepartName","jzwz");
		 queryWrapper.in("shebeino",shebeilist);//用户权限下设备编号
		 queryWrapper.groupBy("DepartName");
		 List<DeviceTunnelPositiondata> pageList = deviceTunnelPositiondataService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param deviceTunnelPositiondata
	  * @param pageNo
	  * @param pageSize
	  * @param req  左右洞门禁考勤数据  只限问腰隧道 lhgswydw01  这个设备
	  * @return
	  */
	 @AutoLog(value = "隧道人员定位表信息-问腰隧道门禁考勤当做人员定位再用-门禁考勤信息")
	 @ApiOperation(value="隧道人员定位表信息-问腰隧道门禁考勤当做人员定位再用-门禁考勤信息")
	 @GetMapping(value = "/list4")
	 public Result<?> queryPageList4(DeviceTunnelPositiondata deviceTunnelPositiondata,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceTunnelPositiondata.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 deviceTunnelPositiondata.setShebeino(shebei);
			 }else {
				 deviceTunnelPositiondata.setShebeino("空");
			 }
		 }
		 if(deviceTunnelPositiondata.getJzwz()!=null){
			 deviceTunnelPositiondata.setJzwz("*"+deviceTunnelPositiondata.getJzwz()+"*");
		 }
		 if(deviceTunnelPositiondata.getJz()!=null){
			 deviceTunnelPositiondata.setJz("*"+deviceTunnelPositiondata.getJz()+"*");
		 }
		 QueryWrapper<DeviceTunnelPositiondata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondata, req.getParameterMap());
		 queryWrapper.groupBy("username");
		 List<DeviceTunnelPositiondata> pageList = deviceTunnelPositiondataService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param deviceTunnelPositiondata
	  * @param pageNo
	  * @param pageSize
	  * @param req  门禁考勤左右洞 总人数 以及出勤人数 只限问腰隧道 lhgswydw01  这个设备
	  * @return
	  */
	 @AutoLog(value = "隧道人员定位表信息-问腰门禁考勤")
	 @ApiOperation(value="隧道人员定位表信息-问腰门禁考勤", notes="隧道人员定位表信息-问腰门禁考勤")
	 @GetMapping(value = "/list5")
	 public Result<?> queryPageList5(DeviceTunnelPositiondata deviceTunnelPositiondata,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceTunnelPositiondata.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 deviceTunnelPositiondata.setShebeino(shebei);
			 }else {
				 deviceTunnelPositiondata.setShebeino("空");
			 }
		 }
		 if(deviceTunnelPositiondata.getJzwz()!=null){
			 deviceTunnelPositiondata.setJzwz("*"+deviceTunnelPositiondata.getJzwz()+"*");
		 }
		 if(deviceTunnelPositiondata.getJz()!=null){
			 deviceTunnelPositiondata.setJz("*"+deviceTunnelPositiondata.getJz()+"*");
		 }
		 QueryWrapper<DeviceTunnelPositiondata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondata, req.getParameterMap());
		 queryWrapper.select("count(DISTINCT(username)) as DepartmentID");
		 List<DeviceTunnelPositiondata> pageList = deviceTunnelPositiondataService.list(queryWrapper);
		 return Result.OK(pageList);
	 }


	 /**
	  * 分页列表查询
	  *
	  * @param deviceTunnelPositiondata
	  * @param pageNo
	  * @param pageSize 问腰隧道 门禁考勤班组  只限问腰隧道 lhgswydw01  这个设备
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "隧道人员定位表信息-班组统计")
	 @ApiOperation(value="隧道人员定位表信息-班组统计", notes="隧道人员定位表信息-班组统计")
	 @GetMapping(value = "/list6")
	 public Result<?> queryPageList6(DeviceTunnelPositiondata deviceTunnelPositiondata,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceTunnelPositiondata.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 deviceTunnelPositiondata.setShebeino(shebei);
			 }else {
				 deviceTunnelPositiondata.setShebeino("空");
			 }
		 }
		 if(deviceTunnelPositiondata.getJzwz()!=null){
			 deviceTunnelPositiondata.setJzwz("*"+deviceTunnelPositiondata.getJzwz()+"*");
		 }
		 if(deviceTunnelPositiondata.getJz()!=null){
			 deviceTunnelPositiondata.setJz("*"+deviceTunnelPositiondata.getJz()+"*");
		 }
		 QueryWrapper<DeviceTunnelPositiondata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondata, req.getParameterMap());
		 queryWrapper.select("count(DISTINCT(username)) as DepartmentID","DepartName","jz");
		 queryWrapper.groupBy("DepartName");
		 List<DeviceTunnelPositiondata> pageList = deviceTunnelPositiondataService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param deviceTunnelPositiondata
	 * @return
	 */
	@AutoLog(value = "隧道人员定位表信息-添加")
	@ApiOperation(value="隧道人员定位表信息-添加", notes="隧道人员定位表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTunnelPositiondata deviceTunnelPositiondata) {
		deviceTunnelPositiondataService.save(deviceTunnelPositiondata);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param deviceTunnelPositiondata
	 * @return
	 */
	@AutoLog(value = "隧道人员定位表信息-编辑")
	@ApiOperation(value="隧道人员定位表信息-编辑", notes="隧道人员定位表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceTunnelPositiondata deviceTunnelPositiondata) {
		deviceTunnelPositiondataService.updateById(deviceTunnelPositiondata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "隧道人员定位表信息-通过id删除")
	@ApiOperation(value="隧道人员定位表信息-通过id删除", notes="隧道人员定位表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceTunnelPositiondataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "隧道人员定位表信息-批量删除")
	@ApiOperation(value="隧道人员定位表信息-批量删除", notes="隧道人员定位表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTunnelPositiondataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "隧道人员定位表信息-通过id查询")
	@ApiOperation(value="隧道人员定位表信息-通过id查询", notes="隧道人员定位表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTunnelPositiondata deviceTunnelPositiondata = deviceTunnelPositiondataService.getById(id);
		if(deviceTunnelPositiondata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceTunnelPositiondata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceTunnelPositiondata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceTunnelPositiondata deviceTunnelPositiondata) {
        return super.exportXls(request, deviceTunnelPositiondata, DeviceTunnelPositiondata.class, "隧道人员定位表信息");
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
        return super.importExcel(request, response, DeviceTunnelPositiondata.class);
    }

}
