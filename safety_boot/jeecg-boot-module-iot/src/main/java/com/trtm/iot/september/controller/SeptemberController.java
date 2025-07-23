package com.trtm.iot.september.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.september.entity.September;
import com.trtm.iot.september.service.ISeptemberService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: GPS历史轨迹
 * @Author: jeecg-boot
 * @Date:   2022-04-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags="GPS历史轨迹")
@RestController
@RequestMapping("/com.trtm.iot.september/september")
public class SeptemberController extends JeecgController<September, ISeptemberService> {
	@Autowired
	private ISeptemberService septemberService;

	/**
	 * 分页列表查询
	 *
	 * @param september
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "GPS历史轨迹-分页列表查询")
	@ApiOperation(value="GPS历史轨迹-分页列表查询", notes="GPS历史轨迹-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(September september,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<September> queryWrapper = QueryGenerator.initQueryWrapper(september, req.getParameterMap());
		Page<September> page = new Page<September>(pageNo, pageSize);
		IPage<September> pageList = septemberService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * list数组查询
	  * @param september
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "GPS历史轨迹-list数组查询")
	 @ApiOperation(value="GPS历史轨迹-list数组查询", notes="GPS历史轨迹-list数组查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(September september,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<September> queryWrapper = QueryGenerator.initQueryWrapper(september, req.getParameterMap());
		 List<September> pageList = septemberService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 * 添加
	 *
	 * @param september
	 * @return
	 */
	@AutoLog(value = "GPS历史轨迹-添加")
	@ApiOperation(value="GPS历史轨迹-添加", notes="GPS历史轨迹-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody September september) {
		septemberService.save(september);
		return Result.OK("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param september
	 * @return
	 */
	@AutoLog(value = "GPS历史轨迹-编辑")
	@ApiOperation(value="GPS历史轨迹-编辑", notes="GPS历史轨迹-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody September september) {
		septemberService.updateById(september);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "GPS历史轨迹-通过id删除")
	@ApiOperation(value="GPS历史轨迹-通过id删除", notes="GPS历史轨迹-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		septemberService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "GPS历史轨迹-批量删除")
	@ApiOperation(value="GPS历史轨迹-批量删除", notes="GPS历史轨迹-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.septemberService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "GPS历史轨迹-通过id查询")
	@ApiOperation(value="GPS历史轨迹-通过id查询", notes="GPS历史轨迹-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		September september = septemberService.getById(id);
		return Result.OK(september);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param september
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, September september) {
      return super.exportXls(request, september, September.class, "GPS历史轨迹");
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
      return super.importExcel(request, response, September.class);
  }

}
