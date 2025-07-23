package com.trtm.iot.lqbhzcailiaoStatistics.controller;

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
import com.trtm.iot.lqbhzcailiaoStatistics.entity.BhzLqCailiaoStatistics;
import com.trtm.iot.lqbhzcailiaoStatistics.service.IBhzLqCailiaoStatisticsService;

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
 * @Description: bhz_lq_cailiao_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Api(tags="bhz_lq_cailiao_statistics")
@RestController
@RequestMapping("/lqbhzcailiaoStatistics/bhzLqCailiaoStatistics")
@Slf4j
public class BhzLqCailiaoStatisticsController extends JeecgController<BhzLqCailiaoStatistics, IBhzLqCailiaoStatisticsService> {
	@Autowired
	private IBhzLqCailiaoStatisticsService bhzLqCailiaoStatisticsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bhzLqCailiaoStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_lq_cailiao_statistics-分页列表查询")
	@ApiOperation(value="bhz_lq_cailiao_statistics-分页列表查询", notes="bhz_lq_cailiao_statistics-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzLqCailiaoStatistics bhzLqCailiaoStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzLqCailiaoStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqCailiaoStatistics, req.getParameterMap());
		Page<BhzLqCailiaoStatistics> page = new Page<BhzLqCailiaoStatistics>(pageNo, pageSize);
		IPage<BhzLqCailiaoStatistics> pageList = bhzLqCailiaoStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bhzLqCailiaoStatistics
	 * @return
	 */
	@AutoLog(value = "bhz_lq_cailiao_statistics-添加")
	@ApiOperation(value="bhz_lq_cailiao_statistics-添加", notes="bhz_lq_cailiao_statistics-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzLqCailiaoStatistics bhzLqCailiaoStatistics) {
		bhzLqCailiaoStatisticsService.save(bhzLqCailiaoStatistics);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bhzLqCailiaoStatistics
	 * @return
	 */
	@AutoLog(value = "bhz_lq_cailiao_statistics-编辑")
	@ApiOperation(value="bhz_lq_cailiao_statistics-编辑", notes="bhz_lq_cailiao_statistics-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzLqCailiaoStatistics bhzLqCailiaoStatistics) {
		bhzLqCailiaoStatisticsService.updateById(bhzLqCailiaoStatistics);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_lq_cailiao_statistics-通过id删除")
	@ApiOperation(value="bhz_lq_cailiao_statistics-通过id删除", notes="bhz_lq_cailiao_statistics-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzLqCailiaoStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_lq_cailiao_statistics-批量删除")
	@ApiOperation(value="bhz_lq_cailiao_statistics-批量删除", notes="bhz_lq_cailiao_statistics-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzLqCailiaoStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_lq_cailiao_statistics-通过id查询")
	@ApiOperation(value="bhz_lq_cailiao_statistics-通过id查询", notes="bhz_lq_cailiao_statistics-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzLqCailiaoStatistics bhzLqCailiaoStatistics = bhzLqCailiaoStatisticsService.getById(id);
		if(bhzLqCailiaoStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzLqCailiaoStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzLqCailiaoStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzLqCailiaoStatistics bhzLqCailiaoStatistics) {
        return super.exportXls(request, bhzLqCailiaoStatistics, BhzLqCailiaoStatistics.class, "bhz_lq_cailiao_statistics");
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
        return super.importExcel(request, response, BhzLqCailiaoStatistics.class);
    }

}
