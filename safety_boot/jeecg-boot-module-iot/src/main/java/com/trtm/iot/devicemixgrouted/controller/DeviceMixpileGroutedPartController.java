package com.trtm.iot.devicemixgrouted.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.devicemixgrouted.entity.DeviceMixpileGroutedPart;
import com.trtm.iot.devicemixgrouted.service.IDeviceMixpileGroutedPartService;

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
 * @Description: 灌注桩子表
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Api(tags="灌注桩子表")
@RestController
@RequestMapping("/grouted/deviceMixpileGroutedPart")
@Slf4j
public class DeviceMixpileGroutedPartController extends JeecgController<DeviceMixpileGroutedPart, IDeviceMixpileGroutedPartService> {
	@Autowired
	private IDeviceMixpileGroutedPartService deviceMixpileGroutedPartService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceMixpileGroutedPart
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "灌注桩子表-分页列表查询")
	@ApiOperation(value="灌注桩子表-分页列表查询", notes="灌注桩子表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceMixpileGroutedPart deviceMixpileGroutedPart,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceMixpileGroutedPart> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileGroutedPart, req.getParameterMap());
		Page<DeviceMixpileGroutedPart> page = new Page<DeviceMixpileGroutedPart>(pageNo, pageSize);
		IPage<DeviceMixpileGroutedPart> pageList = deviceMixpileGroutedPartService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceMixpileGroutedPart
	 * @return
	 */
	@AutoLog(value = "灌注桩子表-添加")
	@ApiOperation(value="灌注桩子表-添加", notes="灌注桩子表-添加")
	@PostMapping(value = "/addGroutedPartOpen")
	public Result<?> addPart(@RequestBody DeviceMixpileGroutedPart deviceMixpileGroutedPart) {
		deviceMixpileGroutedPartService.save(deviceMixpileGroutedPart);
		return Result.OK("添加成功！");
	}

	 @AutoLog(value = "灌注桩子表-添加")
	 @ApiOperation(value="灌注桩子表-添加", notes="灌注桩子表-添加")
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody DeviceMixpileGroutedPart deviceMixpileGroutedPart) {
		 deviceMixpileGroutedPartService.save(deviceMixpileGroutedPart);
		 return Result.OK("添加成功！");
	 }
	
	/**
	 *  编辑
	 *
	 * @param deviceMixpileGroutedPart
	 * @return
	 */
	@AutoLog(value = "灌注桩子表-编辑")
	@ApiOperation(value="灌注桩子表-编辑", notes="灌注桩子表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceMixpileGroutedPart deviceMixpileGroutedPart) {
		deviceMixpileGroutedPartService.updateById(deviceMixpileGroutedPart);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "灌注桩子表-通过id删除")
	@ApiOperation(value="灌注桩子表-通过id删除", notes="灌注桩子表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceMixpileGroutedPartService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "灌注桩子表-批量删除")
	@ApiOperation(value="灌注桩子表-批量删除", notes="灌注桩子表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceMixpileGroutedPartService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "灌注桩子表-通过id查询")
	@ApiOperation(value="灌注桩子表-通过id查询", notes="灌注桩子表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceMixpileGroutedPart deviceMixpileGroutedPart = deviceMixpileGroutedPartService.getById(id);
		if(deviceMixpileGroutedPart==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceMixpileGroutedPart);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceMixpileGroutedPart
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileGroutedPart deviceMixpileGroutedPart) {
        return super.exportXls(request, deviceMixpileGroutedPart, DeviceMixpileGroutedPart.class, "灌注桩子表");
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
        return super.importExcel(request, response, DeviceMixpileGroutedPart.class);
    }

}
