package com.trtm.iot.devicetunnelpositiondatareal.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.trtm.iot.devicetunnelposition.entity.DeviceTunnelPositiondata;
import com.trtm.iot.devicetunnelpositiondatareal.entity.DeviceTunnelPositiondataRealDto;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicetunnelpositiondatareal.entity.DeviceTunnelPositiondataReal;
import com.trtm.iot.devicetunnelpositiondatareal.service.IDeviceTunnelPositiondataRealService;

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
 * @Description: 人员定位实时表
 * @Author: jeecg-boot
 * @Date:   2021-09-27
 * @Version: V1.0
 */
@Api(tags="人员定位实时表")
@RestController
@RequestMapping("/devicetunnelpositiondatareal/deviceTunnelPositiondataReal")
@Slf4j
public class DeviceTunnelPositiondataRealController extends JeecgController<DeviceTunnelPositiondataReal, IDeviceTunnelPositiondataRealService> {
	@Autowired
	private IDeviceTunnelPositiondataRealService deviceTunnelPositiondataRealService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;
	 /**
	  * 济新人员定位-左右洞明细-门禁
	  *
	  * @param deviceTunnelPositiondataReal
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "济新人员定位-左右洞明细-门禁")
	 @ApiOperation(value="济新人员定位-左右洞明细-门禁", notes="济新人员定位-左右洞明细-门禁")
	 @GetMapping(value = "/queryJiXinList")
	 public Result<?> queryJiXinList(DeviceTunnelPositiondataReal deviceTunnelPositiondataReal,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 String orgCode = loginUser.getOrgCode();
		 System.out.println(orgCode);
		 //查询到他的设备编号
		 if(deviceTunnelPositiondataReal.getShebeino()==null){
			 if (shebei != null) {
				 deviceTunnelPositiondataReal.setShebeino(shebei);
			 }
		 }
		 if(deviceTunnelPositiondataReal.getJzwz()!=null){
			 deviceTunnelPositiondataReal.setJzwz("*"+deviceTunnelPositiondataReal.getJzwz()+"*");
		 }
		 List<DeviceTunnelPositiondataReal> list = deviceTunnelPositiondataRealService.queryJiXinList(deviceTunnelPositiondataReal.getShebeino());
		 for (DeviceTunnelPositiondataReal tunnelPositiondataReal : list) {
			 String shebeino = tunnelPositiondataReal.getShebeino();
			 ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeino);
			 String sbname = selectshebeione.getSbname();
			 tunnelPositiondataReal.setGuid(sbname);
		 }
		 return Result.OK(list);
	 }
	 /**
	  * 济新人员定位-左右洞班组
	  *
	  * @param deviceTunnelPositiondataReal
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "济新人员定位-左右洞班组")
	 @ApiOperation(value="济新人员定位-左右洞班组", notes="济新人员定位-左右洞班组")
	 @GetMapping(value = "/queryJiXinBanZuList")
	 public Result<?> queryJiXinBanZuList(DeviceTunnelPositiondataReal deviceTunnelPositiondataReal,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 //根据设备区分权限代码
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceTunnelPositiondataReal.getShebeino() == null) {
			 if (shebei != null) {
				 deviceTunnelPositiondataReal.setShebeino(shebei);
			 }
		 }
		 if (deviceTunnelPositiondataReal.getJzwz() != null) {
			 deviceTunnelPositiondataReal.setJzwz("*" + deviceTunnelPositiondataReal.getJzwz() + "*");
		 }
		 if (deviceTunnelPositiondataReal.getJz() != null) {
			 deviceTunnelPositiondataReal.setJz("*" + deviceTunnelPositiondataReal.getJz() + "*");
		 }

		 List<DeviceTunnelPositiondataReal> list = deviceTunnelPositiondataRealService.queryJiXinBanZuList(deviceTunnelPositiondataReal.getShebeino());
		 return Result.OK(list);
	 }
	 /**
	  * 济新门禁考勤-当天出勤人数
	  *
	  * @param deviceTunnelPositiondataReal
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "济新门禁考勤-当天出勤人数")
	 @ApiOperation(value="济新门禁考勤-当天出勤人数", notes="济新门禁考勤-当天出勤人数")
	 @GetMapping(value = "/querylistAttendance")
	 public Result<?> querylistAttendance(DeviceTunnelPositiondataReal deviceTunnelPositiondataReal,
										  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										  HttpServletRequest req) {
		 //根据设备区分权限代码
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceTunnelPositiondataReal.getShebeino() == null) {
			 if (shebei != null) {
				 deviceTunnelPositiondataReal.setShebeino(shebei);
			 }
		 }
		 if (deviceTunnelPositiondataReal.getJzwz() != null) {
			 deviceTunnelPositiondataReal.setJzwz("*" + deviceTunnelPositiondataReal.getJzwz() + "*");
		 }
		 if (deviceTunnelPositiondataReal.getJz() != null) {
			 deviceTunnelPositiondataReal.setJz("*" + deviceTunnelPositiondataReal.getJz() + "*");
		 }
		 List<DeviceTunnelPositiondataReal> list = deviceTunnelPositiondataRealService.querylistAttendance(deviceTunnelPositiondataReal.getShebeino());
		 return Result.OK(list);
	 }

	/**
	 * 分页列表查询
	 *
	 * @param deviceTunnelPositiondataReal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "人员定位实时表-分页列表查询")
	@ApiOperation(value="人员定位实时表-分页列表查询", notes="人员定位实时表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceTunnelPositiondataReal deviceTunnelPositiondataReal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if(deviceTunnelPositiondataReal.getShebeino()==null){
			if (shebei != null) {
				deviceTunnelPositiondataReal.setShebeino(shebei);
			}
		}
		QueryWrapper<DeviceTunnelPositiondataReal> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondataReal, req.getParameterMap());
		Page<DeviceTunnelPositiondataReal> page = new Page<DeviceTunnelPositiondataReal>(pageNo, pageSize);
		IPage<DeviceTunnelPositiondataReal> pageList = deviceTunnelPositiondataRealService.page(page, queryWrapper);
		return Result.OK(pageList);
	}



	 /**
	  * 列表查询查询每个人的最新数据
	  *
	  * @param deviceTunnelPositiondataReal
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "人员定位实时表-查询每个人的最新数据")
	 @ApiOperation(value="人员定位实时表-查询每个人的最新数据", notes="人员定位实时表-查询每个人的最新数据")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(DeviceTunnelPositiondataReal deviceTunnelPositiondataReal,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 String orgCode = loginUser.getOrgCode();
		 System.out.println(orgCode);
		 //查询到他的设备编号
		 if(deviceTunnelPositiondataReal.getShebeino()==null){
			 if (shebei != null) {
				 deviceTunnelPositiondataReal.setShebeino(shebei);
			 }
		 }
		 if(deviceTunnelPositiondataReal.getJzwz()!=null){
			 deviceTunnelPositiondataReal.setJzwz("*"+deviceTunnelPositiondataReal.getJzwz()+"*");
		 }
		 QueryWrapper<DeviceTunnelPositiondataReal> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondataReal, req.getParameterMap());
		 List<DeviceTunnelPositiondataReal> list = deviceTunnelPositiondataRealService.list(queryWrapper);
		 for (DeviceTunnelPositiondataReal tunnelPositiondataReal : list) {
			 String shebeino = tunnelPositiondataReal.getShebeino();
			 ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeino);
			 String sbname = selectshebeione.getSbname();
			 tunnelPositiondataReal.setGuid(sbname);
		 }
		 return Result.OK(list);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param deviceTunnelPositiondataReal
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "隧道人员定位表信息-班组统计")
	 @ApiOperation(value="隧道人员定位表信息-班组统计", notes="隧道人员定位表信息-班组统计")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(DeviceTunnelPositiondataReal deviceTunnelPositiondataReal,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
	     //根据设备区分权限代码
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (deviceTunnelPositiondataReal.getShebeino() == null) {
			 if (shebei != null) {
				 deviceTunnelPositiondataReal.setShebeino(shebei);
			 }
		 }
		 if (deviceTunnelPositiondataReal.getJzwz() != null) {
			 deviceTunnelPositiondataReal.setJzwz("*" + deviceTunnelPositiondataReal.getJzwz() + "*");
		 }
		 if (deviceTunnelPositiondataReal.getJz() != null) {
			 deviceTunnelPositiondataReal.setJz("*" + deviceTunnelPositiondataReal.getJz() + "*");
		 }
		 QueryWrapper<DeviceTunnelPositiondataReal> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondataReal, req.getParameterMap());
		 queryWrapper.select("count(DISTINCT(username)) as DepartmentID", "DepartName", "jzwz");
		 queryWrapper.orderByDesc("uploadTime");
		 queryWrapper.groupBy("DepartName");
		 List<DeviceTunnelPositiondataReal> pageList = deviceTunnelPositiondataRealService.list(queryWrapper);
		 return Result.OK(pageList);
	 }


	 /**
	  * 分页列表查询
	  *
	  * @param deviceTunnelPositiondataReal
	  * @param pageNo
	  * @param pageSize
	  * @param req  左右洞门禁考勤数据  只限问腰隧道 lhgswydw01  这个设备
	  * @return
	  */
	 @AutoLog(value = "隧道人员定位表信息-问腰隧道门禁考勤当做人员定位再用-门禁考勤信息")
	 @ApiOperation(value="隧道人员定位表信息-问腰隧道门禁考勤当做人员定位再用-门禁考勤信息")
	 @GetMapping(value = "/list4")
	 public Result<?> queryPageList4(DeviceTunnelPositiondataRealDto deviceTunnelPositiondataReal,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if(deviceTunnelPositiondataReal.getShebeino()==null){
			 if (shebei != null) {
				 deviceTunnelPositiondataReal.setShebeino(shebei);
			 }
		 }
		 if(deviceTunnelPositiondataReal.getJzwz()!=null){
			 deviceTunnelPositiondataReal.setJzwz("*"+deviceTunnelPositiondataReal.getJzwz()+"*");
		 }
		 if(deviceTunnelPositiondataReal.getJz()!=null){
			 deviceTunnelPositiondataReal.setJz("*"+deviceTunnelPositiondataReal.getJz()+"*");
		 }
		 QueryWrapper<DeviceTunnelPositiondataReal> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelPositiondataReal, req.getParameterMap());
		 //按照定位时间排序
		 queryWrapper.select().orderByDesc("dvtime");
		 queryWrapper.groupBy("username");
		 List<DeviceTunnelPositiondataReal> pageList = deviceTunnelPositiondataRealService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param deviceTunnelPositiondataReal
	 * @return
	 */
	@AutoLog(value = "人员定位实时表-添加")
	@ApiOperation(value="人员定位实时表-添加", notes="人员定位实时表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTunnelPositiondataReal deviceTunnelPositiondataReal) {
		deviceTunnelPositiondataRealService.save(deviceTunnelPositiondataReal);
		return Result.OK("添加成功！");
	}
	 /**
	  *   添加(对外)
	  *
	  * @param deviceTunnelPositiondataReal
	  * @return
	  */
	 @AutoLog(value = "人员定位实时表-添加")
	 @ApiOperation(value="人员定位实时表-添加", notes="人员定位实时表-添加")
	 @PostMapping(value = "/addOpen")
	 public Result<?> addOpen(@RequestBody DeviceTunnelPositiondataReal deviceTunnelPositiondataReal) {
		 deviceTunnelPositiondataRealService.save(deviceTunnelPositiondataReal);
		 return Result.OK("添加成功！");
	 }
	 /**
	  *   批量添加(对外)
	  *
	  * @param deviceTunnelPositiondataReals
	  * @return
	  */
	 @AutoLog(value = "人员定位实时表-添加")
	 @ApiOperation(value="人员定位实时表-添加", notes="人员定位实时表-添加")
	 @PostMapping(value = "/addOpenList")
	 public Result<?> addOpenList(@RequestBody List<DeviceTunnelPositiondataReal> deviceTunnelPositiondataReals) {
		 deviceTunnelPositiondataRealService.saveBatch(deviceTunnelPositiondataReals);
		 return Result.OK("批量添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param deviceTunnelPositiondataReal
	 * @return
	 */
	@AutoLog(value = "人员定位实时表-编辑")
	@ApiOperation(value="人员定位实时表-编辑", notes="人员定位实时表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceTunnelPositiondataReal deviceTunnelPositiondataReal) {
		deviceTunnelPositiondataRealService.updateById(deviceTunnelPositiondataReal);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "人员定位实时表-通过id删除")
	@ApiOperation(value="人员定位实时表-通过id删除", notes="人员定位实时表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceTunnelPositiondataRealService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "人员定位实时表-批量删除")
	@ApiOperation(value="人员定位实时表-批量删除", notes="人员定位实时表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTunnelPositiondataRealService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "人员定位实时表-通过id查询")
	@ApiOperation(value="人员定位实时表-通过id查询", notes="人员定位实时表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTunnelPositiondataReal deviceTunnelPositiondataReal = deviceTunnelPositiondataRealService.getById(id);
		if(deviceTunnelPositiondataReal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceTunnelPositiondataReal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceTunnelPositiondataReal
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceTunnelPositiondataReal deviceTunnelPositiondataReal) {
        return super.exportXls(request, deviceTunnelPositiondataReal, DeviceTunnelPositiondataReal.class, "人员定位实时表");
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
        return super.importExcel(request, response, DeviceTunnelPositiondataReal.class);
    }

}
