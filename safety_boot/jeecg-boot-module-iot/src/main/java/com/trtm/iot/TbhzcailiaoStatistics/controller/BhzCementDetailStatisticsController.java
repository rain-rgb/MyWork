package com.trtm.iot.TbhzcailiaoStatistics.controller;

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
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;

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
 * @Description: bhz_cement_detail_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Api(tags="bhz_cement_detail_statistics")
@RestController
@RequestMapping("/TbhzcailiaoStatistics/bhzCementDetailStatistics")
@Slf4j
public class BhzCementDetailStatisticsController extends JeecgController<BhzCementDetailStatistics, IBhzCementDetailStatisticsService> {
	@Autowired
	private IBhzCementDetailStatisticsService bhzCementDetailStatisticsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bhzCementDetailStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_cement_detail_statistics-分页列表查询")
	@ApiOperation(value="bhz_cement_detail_statistics-分页列表查询", notes="bhz_cement_detail_statistics-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzCementDetailStatistics bhzCementDetailStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzCementDetailStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementDetailStatistics, req.getParameterMap());
		Page<BhzCementDetailStatistics> page = new Page<BhzCementDetailStatistics>(pageNo, pageSize);
		IPage<BhzCementDetailStatistics> pageList = bhzCementDetailStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bhzCementDetailStatistics
	 * @return
	 */
	@AutoLog(value = "bhz_cement_detail_statistics-添加")
	@ApiOperation(value="bhz_cement_detail_statistics-添加", notes="bhz_cement_detail_statistics-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzCementDetailStatistics bhzCementDetailStatistics) {
		bhzCementDetailStatisticsService.save(bhzCementDetailStatistics);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bhzCementDetailStatistics
	 * @return
	 */
	@AutoLog(value = "bhz_cement_detail_statistics-编辑")
	@ApiOperation(value="bhz_cement_detail_statistics-编辑", notes="bhz_cement_detail_statistics-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzCementDetailStatistics bhzCementDetailStatistics) {
		bhzCementDetailStatisticsService.updateById(bhzCementDetailStatistics);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_cement_detail_statistics-通过id删除")
	@ApiOperation(value="bhz_cement_detail_statistics-通过id删除", notes="bhz_cement_detail_statistics-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzCementDetailStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_cement_detail_statistics-批量删除")
	@ApiOperation(value="bhz_cement_detail_statistics-批量删除", notes="bhz_cement_detail_statistics-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzCementDetailStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_cement_detail_statistics-通过id查询")
	@ApiOperation(value="bhz_cement_detail_statistics-通过id查询", notes="bhz_cement_detail_statistics-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzCementDetailStatistics bhzCementDetailStatistics = bhzCementDetailStatisticsService.getById(id);
		if(bhzCementDetailStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzCementDetailStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzCementDetailStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzCementDetailStatistics bhzCementDetailStatistics) {
        return super.exportXls(request, bhzCementDetailStatistics, BhzCementDetailStatistics.class, "bhz_cement_detail_statistics");
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
        return super.importExcel(request, response, BhzCementDetailStatistics.class);
    }

}
