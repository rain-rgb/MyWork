package com.trtm.iot.bhzlqjipeistatistics.controller;

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
import com.trtm.iot.bhzlqjipeistatistics.entity.BhzLqJipeiStatistics;
import com.trtm.iot.bhzlqjipeistatistics.service.IBhzLqJipeiStatisticsService;

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
 * @Description: 沥青级配统计信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
@Api(tags="沥青级配统计信息表")
@RestController
@RequestMapping("/bhzlqjipeistatistics/bhzLqJipeiStatistics")
@Slf4j
public class BhzLqJipeiStatisticsController extends JeecgController<BhzLqJipeiStatistics, IBhzLqJipeiStatisticsService> {
	@Autowired
	private IBhzLqJipeiStatisticsService bhzLqJipeiStatisticsService;

	/**
	 * 分页列表查询
	 *
	 * @param bhzLqJipeiStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "沥青级配统计信息表-分页列表查询")
	@ApiOperation(value="沥青级配统计信息表-分页列表查询", notes="沥青级配统计信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzLqJipeiStatistics bhzLqJipeiStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzLqJipeiStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqJipeiStatistics, req.getParameterMap());
		Page<BhzLqJipeiStatistics> page = new Page<BhzLqJipeiStatistics>(pageNo, pageSize);
		IPage<BhzLqJipeiStatistics> pageList = bhzLqJipeiStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param bhzLqJipeiStatistics
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "沥青级配统计信息表-分页列表查询")
	 @ApiOperation(value="沥青级配统计信息表-分页列表查询", notes="沥青级配统计信息表-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(BhzLqJipeiStatistics bhzLqJipeiStatistics,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 List<BhzLqJipeiStatistics> pageList = bhzLqJipeiStatisticsService.getList1(bhzLqJipeiStatistics.getBaseid());
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param bhzLqJipeiStatistics
	 * @return
	 */
	@AutoLog(value = "沥青级配统计信息表-添加")
	@ApiOperation(value="沥青级配统计信息表-添加", notes="沥青级配统计信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzLqJipeiStatistics bhzLqJipeiStatistics) {
		bhzLqJipeiStatisticsService.save(bhzLqJipeiStatistics);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param bhzLqJipeiStatistics
	 * @return
	 */
	@AutoLog(value = "沥青级配统计信息表-编辑")
	@ApiOperation(value="沥青级配统计信息表-编辑", notes="沥青级配统计信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzLqJipeiStatistics bhzLqJipeiStatistics) {
		bhzLqJipeiStatisticsService.updateById(bhzLqJipeiStatistics);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "沥青级配统计信息表-通过id删除")
	@ApiOperation(value="沥青级配统计信息表-通过id删除", notes="沥青级配统计信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzLqJipeiStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "沥青级配统计信息表-批量删除")
	@ApiOperation(value="沥青级配统计信息表-批量删除", notes="沥青级配统计信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzLqJipeiStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "沥青级配统计信息表-通过id查询")
	@ApiOperation(value="沥青级配统计信息表-通过id查询", notes="沥青级配统计信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzLqJipeiStatistics bhzLqJipeiStatistics = bhzLqJipeiStatisticsService.getById(id);
		if(bhzLqJipeiStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzLqJipeiStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzLqJipeiStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzLqJipeiStatistics bhzLqJipeiStatistics) {
        return super.exportXls(request, bhzLqJipeiStatistics, BhzLqJipeiStatistics.class, "沥青级配统计信息表");
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
        return super.importExcel(request, response, BhzLqJipeiStatistics.class);
    }

}
