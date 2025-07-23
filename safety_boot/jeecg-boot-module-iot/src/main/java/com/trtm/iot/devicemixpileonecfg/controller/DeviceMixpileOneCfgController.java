package com.trtm.iot.devicemixpileonecfg.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.devicemixpileonecfg.entity.DeviceMixpileOneCfg;
import com.trtm.iot.devicemixpileonecfg.service.IDeviceMixpileOneCfgService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 水泥搅拌桩设备配置
 * @Author: jeecg-boot
 * @Date:   2021-12-01
 * @Version: V1.0
 */
@Api(tags="水泥搅拌桩设备配置")
@RestController
@RequestMapping("/devicemixpileoneCfg/deviceMixpileOneCfg")
@Slf4j
public class DeviceMixpileOneCfgController extends JeecgController<DeviceMixpileOneCfg, IDeviceMixpileOneCfgService> {
	@Autowired
	private IDeviceMixpileOneCfgService deviceMixpileOneCfgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceMixpileOneCfg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩设备配置-分页列表查询")
	@ApiOperation(value="水泥搅拌桩设备配置-分页列表查询", notes="水泥搅拌桩设备配置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceMixpileOneCfg deviceMixpileOneCfg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceMixpileOneCfg> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileOneCfg, req.getParameterMap());
		Page<DeviceMixpileOneCfg> page = new Page<DeviceMixpileOneCfg>(pageNo, pageSize);
		IPage<DeviceMixpileOneCfg> pageList = deviceMixpileOneCfgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceMixpileOneCfg
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩设备配置-添加")
	@ApiOperation(value="水泥搅拌桩设备配置-添加", notes="水泥搅拌桩设备配置-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceMixpileOneCfg deviceMixpileOneCfg) {
		deviceMixpileOneCfgService.save(deviceMixpileOneCfg);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param deviceMixpileOneCfg
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩设备配置-编辑")
	@ApiOperation(value="水泥搅拌桩设备配置-编辑", notes="水泥搅拌桩设备配置-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceMixpileOneCfg deviceMixpileOneCfg) {
		deviceMixpileOneCfgService.updateById(deviceMixpileOneCfg);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩设备配置-通过id删除")
	@ApiOperation(value="水泥搅拌桩设备配置-通过id删除", notes="水泥搅拌桩设备配置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceMixpileOneCfgService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩设备配置-批量删除")
	@ApiOperation(value="水泥搅拌桩设备配置-批量删除", notes="水泥搅拌桩设备配置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceMixpileOneCfgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩设备配置-通过id查询")
	@ApiOperation(value="水泥搅拌桩设备配置-通过id查询", notes="水泥搅拌桩设备配置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceMixpileOneCfg deviceMixpileOneCfg = deviceMixpileOneCfgService.getById(id);
		if(deviceMixpileOneCfg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceMixpileOneCfg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceMixpileOneCfg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileOneCfg deviceMixpileOneCfg) {
        return super.exportXls(request, deviceMixpileOneCfg, DeviceMixpileOneCfg.class, "水泥搅拌桩设备配置");
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
        return super.importExcel(request, response, DeviceMixpileOneCfg.class);
    }

}
