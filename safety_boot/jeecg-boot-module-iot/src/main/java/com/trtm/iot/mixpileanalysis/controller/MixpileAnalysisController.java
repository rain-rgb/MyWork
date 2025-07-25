package com.trtm.iot.mixpileanalysis.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.devicemixpileonecfg.entity.DeviceMixpileOneCfg;
import com.trtm.iot.devicemixpileonecfg.service.IDeviceMixpileOneCfgService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.mixpileanalysis.entity.MixpileAnalysis;
import com.trtm.iot.mixpileanalysis.service.IMixpileAnalysisService;

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
 * @Description: 水泥搅拌桩桩记录分析表
 * @Author: jeecg-boot
 * @Date:   2024-01-24
 * @Version: V1.0
 */
@Api(tags="水泥搅拌桩桩记录分析表")
@RestController
@RequestMapping("/mixpileanalysis/mixpileAnalysis")
@Slf4j
public class MixpileAnalysisController extends JeecgController<MixpileAnalysis, IMixpileAnalysisService> {
	@Autowired
	private IMixpileAnalysisService mixpileAnalysisService;
	 @Autowired
	 private IDeviceMixpileOneCfgService mixpileOneCfgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param mixpileAnalysis
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩桩记录分析表-分页列表查询")
	@ApiOperation(value="水泥搅拌桩桩记录分析表-分页列表查询", notes="水泥搅拌桩桩记录分析表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MixpileAnalysis mixpileAnalysis,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MixpileAnalysis> queryWrapper = QueryGenerator.initQueryWrapper(mixpileAnalysis, req.getParameterMap());
		Page<MixpileAnalysis> page = new Page<MixpileAnalysis>(pageNo, pageSize);
		IPage<MixpileAnalysis> pageList = mixpileAnalysisService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @return
	  */
	 @AutoLog(value = "水泥搅拌桩桩记录分析表-分页列表查询")
	 @ApiOperation(value="水泥搅拌桩桩记录分析表-分页列表查询", notes="水泥搅拌桩桩记录分析表-分页列表查询")
	 @GetMapping(value = "/lists")
	 public Result<?> queryPageLists(String shebeino,String pileno,String pileMileage,String partEndtime) {
		 QueryWrapper<DeviceMixpileOneCfg> queryWrapper1 = new QueryWrapper<>();
		 queryWrapper1.eq("shebeino",shebeino);
		 DeviceMixpileOneCfg one = mixpileOneCfgService.getOne(queryWrapper1);


		 QueryWrapper<MixpileAnalysis> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("shebeino",shebeino);
		 queryWrapper.eq("pileno",pileno);
		 if (pileMileage != null){
			 queryWrapper.eq("pile_mileage",pileMileage);
		 }else {
			 queryWrapper.likeRight("part_endtime",partEndtime);
		 }
		 List<MixpileAnalysis> list = mixpileAnalysisService.list(queryWrapper);
		 for (MixpileAnalysis l :list){
			 l.setCreateBy(one.getPileMinelec());
		 }
		 return Result.OK(list);
	 }

	/**
	 *   添加
	 *
	 * @param mixpileAnalysis
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩桩记录分析表-添加")
	@ApiOperation(value="水泥搅拌桩桩记录分析表-添加", notes="水泥搅拌桩桩记录分析表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MixpileAnalysis mixpileAnalysis) {
		mixpileAnalysisService.save(mixpileAnalysis);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param mixpileAnalysis
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩桩记录分析表-编辑")
	@ApiOperation(value="水泥搅拌桩桩记录分析表-编辑", notes="水泥搅拌桩桩记录分析表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MixpileAnalysis mixpileAnalysis) {
		mixpileAnalysisService.updateById(mixpileAnalysis);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩桩记录分析表-通过id删除")
	@ApiOperation(value="水泥搅拌桩桩记录分析表-通过id删除", notes="水泥搅拌桩桩记录分析表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		mixpileAnalysisService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩桩记录分析表-批量删除")
	@ApiOperation(value="水泥搅拌桩桩记录分析表-批量删除", notes="水泥搅拌桩桩记录分析表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.mixpileAnalysisService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "水泥搅拌桩桩记录分析表-通过id查询")
	@ApiOperation(value="水泥搅拌桩桩记录分析表-通过id查询", notes="水泥搅拌桩桩记录分析表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MixpileAnalysis mixpileAnalysis = mixpileAnalysisService.getById(id);
		if(mixpileAnalysis==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(mixpileAnalysis);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param mixpileAnalysis
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MixpileAnalysis mixpileAnalysis) {
        return super.exportXls(request, mixpileAnalysis, MixpileAnalysis.class, "水泥搅拌桩桩记录分析表");
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
        return super.importExcel(request, response, MixpileAnalysis.class);
    }

}
