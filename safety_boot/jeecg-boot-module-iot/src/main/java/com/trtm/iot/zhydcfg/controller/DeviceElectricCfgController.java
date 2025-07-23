package com.trtm.iot.zhydcfg.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import com.trtm.iot.zhydcfg.entity.DeviceElectricCfg;
import com.trtm.iot.zhydcfg.service.IDeviceElectricCfgService;

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
 * @Description: 智慧用电配置主表
 * @Author: jeecg-boot
 * @Date:   2021-05-18
 * @Version: V1.0
 */
@Api(tags="智慧用电配置主表")
@RestController
@RequestMapping("/zhydcfg/deviceElectricCfg")
@Slf4j
public class DeviceElectricCfgController extends JeecgController<DeviceElectricCfg, IDeviceElectricCfgService> {
	@Autowired
	private IDeviceElectricCfgService deviceElectricCfgService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param deviceElectricCfg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "智慧用电配置主表-分页列表查询")
	@ApiOperation(value="智慧用电配置主表-分页列表查询", notes="智慧用电配置主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceElectricCfg deviceElectricCfg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if(deviceElectricCfg.getImei()==null){
			if (shebei != null) {
				deviceElectricCfg.setImei(shebei);
			}
		}
		QueryWrapper<DeviceElectricCfg> queryWrapper = QueryGenerator.initQueryWrapper(deviceElectricCfg, req.getParameterMap());
		queryWrapper.eq("isdel",0);
		Page<DeviceElectricCfg> page = new Page<DeviceElectricCfg>(pageNo, pageSize);
		IPage<DeviceElectricCfg> pageList = deviceElectricCfgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param deviceElectricCfg
	 * @return
	 */
	@AutoLog(value = "智慧用电配置主表-添加")
	@ApiOperation(value="智慧用电配置主表-添加", notes="智慧用电配置主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceElectricCfg deviceElectricCfg) {
		String uuid = UUID.randomUUID().toString().replaceAll("-","");
		deviceElectricCfg.setUid(uuid);
		deviceElectricCfgService.save(deviceElectricCfg);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param deviceElectricCfg
	 * @return
	 */
	@AutoLog(value = "智慧用电配置主表-编辑")
	@ApiOperation(value="智慧用电配置主表-编辑", notes="智慧用电配置主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceElectricCfg deviceElectricCfg) {
		deviceElectricCfgService.updateById(deviceElectricCfg);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "智慧用电配置主表-通过id删除")
	@ApiOperation(value="智慧用电配置主表-通过id删除", notes="智慧用电配置主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id ) {
		DeviceElectricCfg deviceElectricCfg = deviceElectricCfgService.getById(id);
		Integer isdel = 1;
		deviceElectricCfg.setIsdel(isdel);
		deviceElectricCfgService.updateById(deviceElectricCfg);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "智慧用电配置主表-批量删除")
	@ApiOperation(value="智慧用电配置主表-批量删除", notes="智慧用电配置主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceElectricCfgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "智慧用电配置主表-通过id查询")
	@ApiOperation(value="智慧用电配置主表-通过id查询", notes="智慧用电配置主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceElectricCfg deviceElectricCfg = deviceElectricCfgService.getById(id);
		if(deviceElectricCfg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceElectricCfg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceElectricCfg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceElectricCfg deviceElectricCfg) {
        return super.exportXls(request, deviceElectricCfg, DeviceElectricCfg.class, "智慧用电配置主表");
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
        return super.importExcel(request, response, DeviceElectricCfg.class);
    }

}
