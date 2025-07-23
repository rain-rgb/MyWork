package com.trtm.iot.switchmachineStatistics.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.switchmachineStatistics.entity.SwitchingmachineStatistics;
import com.trtm.iot.switchmachineStatistics.service.ISwitchingmachineStatisticsService;

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
 * @Description: 拌合站设备开关机统计表
 * @Author: jeecg-boot
 * @Date:   2022-08-22
 * @Version: V1.0
 */
@Api(tags="拌合站设备开关机统计表")
@RestController
@RequestMapping("/switchmachineStatistics/switchingmachineStatistics")
@Slf4j
public class SwitchingmachineStatisticsController extends JeecgController<SwitchingmachineStatistics, ISwitchingmachineStatisticsService> {
	@Autowired
	private ISwitchingmachineStatisticsService switchingmachineStatisticsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param switchingmachineStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "拌合站设备开关机统计表-分页列表查询")
	@ApiOperation(value="拌合站设备开关机统计表-分页列表查询", notes="拌合站设备开关机统计表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SwitchingmachineStatistics switchingmachineStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SwitchingmachineStatistics> queryWrapper = QueryGenerator.initQueryWrapper(switchingmachineStatistics, req.getParameterMap());
		Page<SwitchingmachineStatistics> page = new Page<SwitchingmachineStatistics>(pageNo, pageSize);
		IPage<SwitchingmachineStatistics> pageList = switchingmachineStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param switchingmachineStatistics
	 * @return
	 */
	@AutoLog(value = "拌合站设备开关机统计表-添加")
	@ApiOperation(value="拌合站设备开关机统计表-添加", notes="拌合站设备开关机统计表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SwitchingmachineStatistics switchingmachineStatistics) {
		switchingmachineStatisticsService.save(switchingmachineStatistics);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param switchingmachineStatistics
	 * @return
	 */
	@AutoLog(value = "拌合站设备开关机统计表-编辑")
	@ApiOperation(value="拌合站设备开关机统计表-编辑", notes="拌合站设备开关机统计表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SwitchingmachineStatistics switchingmachineStatistics) {
		switchingmachineStatisticsService.updateById(switchingmachineStatistics);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拌合站设备开关机统计表-通过id删除")
	@ApiOperation(value="拌合站设备开关机统计表-通过id删除", notes="拌合站设备开关机统计表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		switchingmachineStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "拌合站设备开关机统计表-批量删除")
	@ApiOperation(value="拌合站设备开关机统计表-批量删除", notes="拌合站设备开关机统计表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.switchingmachineStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拌合站设备开关机统计表-通过id查询")
	@ApiOperation(value="拌合站设备开关机统计表-通过id查询", notes="拌合站设备开关机统计表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SwitchingmachineStatistics switchingmachineStatistics = switchingmachineStatisticsService.getById(id);
		if(switchingmachineStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(switchingmachineStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param switchingmachineStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SwitchingmachineStatistics switchingmachineStatistics) {
        return super.exportXls(request, switchingmachineStatistics, SwitchingmachineStatistics.class, "拌合站设备开关机统计表");
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
        return super.importExcel(request, response, SwitchingmachineStatistics.class);
    }

}
