package com.trtm.iot.bhzSwJipeiStatistics.controller;

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
import com.trtm.iot.bhzSwJipeiStatistics.entity.BhzSwJipeiStatistics;
import com.trtm.iot.bhzSwJipeiStatistics.service.IBhzSwJipeiStatisticsService;

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
 * @Description: bhz_sw_jipei_statistics
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
@Api(tags="bhz_sw_jipei_statistics")
@RestController
@RequestMapping("/bhzSwJipeiStatistics/bhzSwJipeiStatistics")
@Slf4j
public class BhzSwJipeiStatisticsController extends JeecgController<BhzSwJipeiStatistics, IBhzSwJipeiStatisticsService> {
	@Autowired
	private IBhzSwJipeiStatisticsService bhzSwJipeiStatisticsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bhzSwJipeiStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_statistics-分页列表查询")
	@ApiOperation(value="bhz_sw_jipei_statistics-分页列表查询", notes="bhz_sw_jipei_statistics-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzSwJipeiStatistics bhzSwJipeiStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzSwJipeiStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwJipeiStatistics, req.getParameterMap());
		Page<BhzSwJipeiStatistics> page = new Page<BhzSwJipeiStatistics>(pageNo, pageSize);
		IPage<BhzSwJipeiStatistics> pageList = bhzSwJipeiStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bhzSwJipeiStatistics
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_statistics-添加")
	@ApiOperation(value="bhz_sw_jipei_statistics-添加", notes="bhz_sw_jipei_statistics-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzSwJipeiStatistics bhzSwJipeiStatistics) {
		bhzSwJipeiStatisticsService.save(bhzSwJipeiStatistics);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bhzSwJipeiStatistics
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_statistics-编辑")
	@ApiOperation(value="bhz_sw_jipei_statistics-编辑", notes="bhz_sw_jipei_statistics-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzSwJipeiStatistics bhzSwJipeiStatistics) {
		bhzSwJipeiStatisticsService.updateById(bhzSwJipeiStatistics);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_statistics-通过id删除")
	@ApiOperation(value="bhz_sw_jipei_statistics-通过id删除", notes="bhz_sw_jipei_statistics-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzSwJipeiStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_statistics-批量删除")
	@ApiOperation(value="bhz_sw_jipei_statistics-批量删除", notes="bhz_sw_jipei_statistics-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzSwJipeiStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_statistics-通过id查询")
	@ApiOperation(value="bhz_sw_jipei_statistics-通过id查询", notes="bhz_sw_jipei_statistics-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzSwJipeiStatistics bhzSwJipeiStatistics = bhzSwJipeiStatisticsService.getById(id);
		if(bhzSwJipeiStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzSwJipeiStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzSwJipeiStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzSwJipeiStatistics bhzSwJipeiStatistics) {
        return super.exportXls(request, bhzSwJipeiStatistics, BhzSwJipeiStatistics.class, "bhz_sw_jipei_statistics");
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
        return super.importExcel(request, response, BhzSwJipeiStatistics.class);
    }

}
