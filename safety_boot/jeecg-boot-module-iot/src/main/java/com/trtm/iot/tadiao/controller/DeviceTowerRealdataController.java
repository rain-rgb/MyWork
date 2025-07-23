package com.trtm.iot.tadiao.controller;

import java.util.Arrays;
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
import com.trtm.iot.tadiao.entity.DeviceTowerRealdata;
import com.trtm.iot.tadiao.service.IDeviceTowerRealdataService;

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
 * @Description: device_tower_realdata
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Api(tags="device_tower_realdata")
@RestController
@RequestMapping("/tadiao/deviceTowerRealdata")
@Slf4j
public class DeviceTowerRealdataController extends JeecgController<DeviceTowerRealdata, IDeviceTowerRealdataService> {
	@Autowired
	private IDeviceTowerRealdataService deviceTowerRealdataService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param deviceTowerRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_tower_realdata-分页列表查询")
	@ApiOperation(value="device_tower_realdata-分页列表查询", notes="device_tower_realdata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceTowerRealdata deviceTowerRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (deviceTowerRealdata.getDeviceCode() == null) {
			if ("null".equals(shebei) ) {
				shebei = "空";
			}
			deviceTowerRealdata.setDeviceCode(shebei);
		}
		QueryWrapper<DeviceTowerRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceTowerRealdata, req.getParameterMap());
		Page<DeviceTowerRealdata> page = new Page<DeviceTowerRealdata>(pageNo, pageSize);
		IPage<DeviceTowerRealdata> pageList = deviceTowerRealdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param deviceTowerRealdata
	 * @return
	 */
	@AutoLog(value = "device_tower_realdata-添加")
	@ApiOperation(value="device_tower_realdata-添加", notes="device_tower_realdata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTowerRealdata deviceTowerRealdata) {
		deviceTowerRealdataService.save(deviceTowerRealdata);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param deviceTowerRealdata
	 * @return
	 */
	@AutoLog(value = "device_tower_realdata-编辑")
	@ApiOperation(value="device_tower_realdata-编辑", notes="device_tower_realdata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceTowerRealdata deviceTowerRealdata) {
		deviceTowerRealdataService.updateById(deviceTowerRealdata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_tower_realdata-通过id删除")
	@ApiOperation(value="device_tower_realdata-通过id删除", notes="device_tower_realdata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceTowerRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_tower_realdata-批量删除")
	@ApiOperation(value="device_tower_realdata-批量删除", notes="device_tower_realdata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTowerRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_tower_realdata-通过id查询")
	@ApiOperation(value="device_tower_realdata-通过id查询", notes="device_tower_realdata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTowerRealdata deviceTowerRealdata = deviceTowerRealdataService.getById(id);
		if(deviceTowerRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceTowerRealdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceTowerRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceTowerRealdata deviceTowerRealdata) {
        return super.exportXls(request, deviceTowerRealdata, DeviceTowerRealdata.class, "device_tower_realdata");
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
        return super.importExcel(request, response, DeviceTowerRealdata.class);
    }

}
