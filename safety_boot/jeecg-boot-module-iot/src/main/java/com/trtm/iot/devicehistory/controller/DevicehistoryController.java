package com.trtm.iot.devicehistory.controller;

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

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;

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
 * @Description: 环境监测历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-15
 * @Version: V1.0
 */
@Api(tags="环境监测历史记录")
@RestController
@RequestMapping("/devicehistory/devicehistory")
@Slf4j
public class DevicehistoryController extends JeecgController<Devicehistory, IDevicehistoryService> {
	@Autowired
	private IDevicehistoryService devicehistoryService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param devicehistory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "环境监测历史记录-分页列表查询")
	@ApiOperation(value="环境监测历史记录-分页列表查询", notes="环境监测历史记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Devicehistory devicehistory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (devicehistory.getDevaddr() == null) {
			if (!"null".equals(shebei)) {
				devicehistory.setDevaddr(shebei);
			}else {
				devicehistory.setDevaddr("空");
			}
		}
		QueryWrapper<Devicehistory> queryWrapper = QueryGenerator.initQueryWrapper(devicehistory, req.getParameterMap());
		queryWrapper.orderByDesc("TimeValue");
		Page<Devicehistory> page = new Page<Devicehistory>(pageNo, pageSize);
		IPage<Devicehistory> pageList = devicehistoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 设备最新数据查询
	  *
	  * @param devicehistory
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "环境监测历史记录-设备最新数据查询")
	 @ApiOperation(value="环境监测历史记录-设备最新数据查询", notes="环境监测历史记录-设备最新数据查询")
	 @GetMapping(value = "/lists")
	 public Result<?> queryPageLists(Devicehistory devicehistory,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (devicehistory.getDevaddr() == null) {
			 if (!"null".equals(shebei)) {
				 devicehistory.setDevaddr(shebei);
			 }else {
				 devicehistory.setDevaddr("空");
			 }
		 }
		 QueryWrapper<Devicehistory> queryWrapper = QueryGenerator.initQueryWrapper(devicehistory, req.getParameterMap());
		 queryWrapper.last("and id in (SELECT MAX(id) as id FROM devicehistory GROUP BY DevAddr)");
		 Page<Devicehistory> page = new Page<Devicehistory>(pageNo, pageSize);
		 IPage<Devicehistory> pageList = devicehistoryService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 按小时统计噪声/PM2.5/PM10（24小时）
	  *
	  * @param devicehistory
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "环境监测历史记录-按小时统计噪声/PM2.5/PM10（24小时）")
	 @ApiOperation(value="环境监测历史记录-按小时统计噪声/PM2.5/PM10（24小时）", notes="环境监测历史记录-按小时统计噪声/PM2.5/PM10（24小时）")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(Devicehistory devicehistory,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		 String format = ft.format(new Date());
		 //查询到他的设备编号
		 if (devicehistory.getDevaddr() == null) {
			 if (!"null".equals(shebei)) {
				 devicehistory.setDevaddr(shebei);
			 }else {
				 devicehistory.setDevaddr("空");
			 }
		 }
		 QueryWrapper<Devicehistory> queryWrapper = QueryGenerator.initQueryWrapper(devicehistory, req.getParameterMap());
		 queryWrapper.select("DATE_FORMAT(TimeValue,'%H') hours,sum(Noise) as Noise,sum(Pm10) as Pm10,sum(Pm25) as Pm25");
		 queryWrapper.likeRight("TimeValue",format);
		 queryWrapper.last(" GROUP BY (SELECT DATE_FORMAT(TimeValue,'%Y%m%d%H'))");
		 List<Map<String, Object>> map = devicehistoryService.listMaps(queryWrapper);
		 return Result.OK(map);
	 }

	/**
	 *   添加
	 *
	 * @param devicehistory
	 * @return
	 */
	@AutoLog(value = "环境监测历史记录-添加")
	@ApiOperation(value="环境监测历史记录-添加", notes="环境监测历史记录-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Devicehistory devicehistory) {
		devicehistoryService.save(devicehistory);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param devicehistory
	 * @return
	 */
	@AutoLog(value = "环境监测历史记录-编辑")
	@ApiOperation(value="环境监测历史记录-编辑", notes="环境监测历史记录-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Devicehistory devicehistory) {
		devicehistoryService.updateById(devicehistory);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "环境监测历史记录-通过id删除")
	@ApiOperation(value="环境监测历史记录-通过id删除", notes="环境监测历史记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devicehistoryService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "环境监测历史记录-批量删除")
	@ApiOperation(value="环境监测历史记录-批量删除", notes="环境监测历史记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devicehistoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "环境监测历史记录-通过id查询")
	@ApiOperation(value="环境监测历史记录-通过id查询", notes="环境监测历史记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Devicehistory devicehistory = devicehistoryService.getById(id);
		if(devicehistory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devicehistory);
	}

	 @AutoLog(value = "手动测试推送一条数据到122平台")
	 @ApiOperation(value="手动测试推送一条数据到122平台", notes="手动测试推送一条数据到122平台")
	 @GetMapping(value = "/push")
	 public void push()  throws IOException {
		 Devicehistory devicehistory = devicehistoryService.getById("843140");
		 devicehistoryService.push(devicehistory);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param devicehistory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Devicehistory devicehistory) {
        return super.exportXls(request, devicehistory, Devicehistory.class, "环境监测历史记录");
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
        return super.importExcel(request, response, Devicehistory.class);
    }

}
