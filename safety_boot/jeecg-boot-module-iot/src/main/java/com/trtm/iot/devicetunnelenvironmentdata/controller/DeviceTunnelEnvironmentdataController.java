package com.trtm.iot.devicetunnelenvironmentdata.controller;

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

import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicetunnelenvironmentdata.entity.DeviceTunnelEnvironmentdata;
import com.trtm.iot.devicetunnelenvironmentdata.service.IDeviceTunnelEnvironmentdataService;

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
 * @Description: 隧道环境（有害气体）监测主表
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Api(tags="隧道环境（有害气体）监测主表")
@RestController
@RequestMapping("/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata")
@Slf4j
public class DeviceTunnelEnvironmentdataController extends JeecgController<DeviceTunnelEnvironmentdata, IDeviceTunnelEnvironmentdataService> {
	@Autowired
	private IDeviceTunnelEnvironmentdataService deviceTunnelEnvironmentdataService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;
	/**
	 * 分页列表查询
	 *
	 * @param deviceTunnelEnvironmentdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "隧道环境（有害气体）监测主表-分页列表查询")
	@ApiOperation(value="隧道环境（有害气体）监测主表-分页列表查询", notes="隧道环境（有害气体）监测主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceTunnelEnvironmentdata deviceTunnelEnvironmentdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (deviceTunnelEnvironmentdata.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				deviceTunnelEnvironmentdata.setShebeino(shebei);
			}else {
				deviceTunnelEnvironmentdata.setShebeino("空");
			}
		}
		QueryWrapper<DeviceTunnelEnvironmentdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelEnvironmentdata, req.getParameterMap());
		Page<DeviceTunnelEnvironmentdata> page = new Page<DeviceTunnelEnvironmentdata>(pageNo, pageSize);
		IPage<DeviceTunnelEnvironmentdata> pageList = deviceTunnelEnvironmentdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 列表查询(对外开放对接接口)
	  *
	  * @param deviceTunnelEnvironmentdata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "隧道环境（有害气体）监测主表-列表查询(对外开放对接接口)")
	 @ApiOperation(value="隧道环境（有害气体）监测主表-列表查询(对外开放对接接口)", notes="隧道环境（有害气体）监测主表-列表查询(对外开放对接接口)")
	 @GetMapping(value = "/listOpen")
	 public Result<?> queryPageListOpen(DeviceTunnelEnvironmentdata deviceTunnelEnvironmentdata,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<DeviceTunnelEnvironmentdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTunnelEnvironmentdata, req.getParameterMap());
         Page<DeviceTunnelEnvironmentdata> page = new Page<DeviceTunnelEnvironmentdata>(pageNo, pageSize);
         IPage<DeviceTunnelEnvironmentdata> pageList = deviceTunnelEnvironmentdataService.page(page, queryWrapper);
         return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param deviceTunnelEnvironmentdata
	 * @return
	 */
	@AutoLog(value = "隧道环境（有害气体）监测主表-添加")
	@ApiOperation(value="隧道环境（有害气体）监测主表-添加", notes="隧道环境（有害气体）监测主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTunnelEnvironmentdata deviceTunnelEnvironmentdata) {
		deviceTunnelEnvironmentdataService.save(deviceTunnelEnvironmentdata);
		return Result.OK("添加成功！");
	}


	/**
	 *   添加
	 *
	 * @param deviceTunnelEnvironmentdata
	 * @return
	 */
	@AutoLog(value = "隧道环境（有害气体）监测主表-添加")
	@ApiOperation(value="隧道环境（有害气体）监测主表-添加", notes="隧道环境（有害气体）监测主表-添加")
	@PostMapping(value = "/addOpen")
	public Result<?> adaddOpend(@RequestBody DeviceTunnelEnvironmentdata deviceTunnelEnvironmentdata) {
		deviceTunnelEnvironmentdataService.save(deviceTunnelEnvironmentdata);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param deviceTunnelEnvironmentdata
	 * @return
	 */
	@AutoLog(value = "隧道环境（有害气体）监测主表-编辑")
	@ApiOperation(value="隧道环境（有害气体）监测主表-编辑", notes="隧道环境（有害气体）监测主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceTunnelEnvironmentdata deviceTunnelEnvironmentdata) {
		deviceTunnelEnvironmentdataService.updateById(deviceTunnelEnvironmentdata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "隧道环境（有害气体）监测主表-通过id删除")
	@ApiOperation(value="隧道环境（有害气体）监测主表-通过id删除", notes="隧道环境（有害气体）监测主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceTunnelEnvironmentdataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "隧道环境（有害气体）监测主表-批量删除")
	@ApiOperation(value="隧道环境（有害气体）监测主表-批量删除", notes="隧道环境（有害气体）监测主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTunnelEnvironmentdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "隧道环境（有害气体）监测主表-通过id查询")
	@ApiOperation(value="隧道环境（有害气体）监测主表-通过id查询", notes="隧道环境（有害气体）监测主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTunnelEnvironmentdata deviceTunnelEnvironmentdata = deviceTunnelEnvironmentdataService.getById(id);
		if(deviceTunnelEnvironmentdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceTunnelEnvironmentdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceTunnelEnvironmentdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceTunnelEnvironmentdata deviceTunnelEnvironmentdata) {
        return super.exportXls(request, deviceTunnelEnvironmentdata, DeviceTunnelEnvironmentdata.class, "隧道环境（有害气体）监测主表");
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
        return super.importExcel(request, response, DeviceTunnelEnvironmentdata.class);
    }

}
