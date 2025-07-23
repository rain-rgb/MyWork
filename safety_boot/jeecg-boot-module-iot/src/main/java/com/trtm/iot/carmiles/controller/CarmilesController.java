package com.trtm.iot.carmiles.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.trtm.iot.system.entity.ShebeiInfo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
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
 * @Description: 车辆里程
 * @Author: jeecg-boot
 * @Date:   2022-03-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="车辆里程")
@RestController
@RequestMapping("/carmiles/carmiles")
public class CarmilesController extends JeecgController<Carmiles, ICarmilesService> {
	@Autowired
	private ICarmilesService carmilesService;
	
	/**
	 * 分页列表查询
	 *
	 * @param carmiles
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "车辆里程-分页列表查询")
	@ApiOperation(value="车辆里程-分页列表查询", notes="车辆里程-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Carmiles carmiles,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Carmiles> queryWrapper = QueryGenerator.initQueryWrapper(carmiles, req.getParameterMap());
		Page<Carmiles> page = new Page<Carmiles>(pageNo, pageSize);
		IPage<Carmiles> pageList = carmilesService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	 * 添加
	 *
	 * @param carmiles
	 * @return
	 */
	@AutoLog(value = "车辆里程-添加")
	@ApiOperation(value="车辆里程-添加", notes="车辆里程-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Carmiles carmiles) {
		carmilesService.save(carmiles);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param carmiles
	 * @return
	 */
	@AutoLog(value = "车辆里程-编辑")
	@ApiOperation(value="车辆里程-编辑", notes="车辆里程-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Carmiles carmiles) {
		carmilesService.updateById(carmiles);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车辆里程-通过id删除")
	@ApiOperation(value="车辆里程-通过id删除", notes="车辆里程-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		carmilesService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "车辆里程-批量删除")
	@ApiOperation(value="车辆里程-批量删除", notes="车辆里程-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.carmilesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车辆里程-通过id查询")
	@ApiOperation(value="车辆里程-通过id查询", notes="车辆里程-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Carmiles carmiles = carmilesService.getById(id);
		return Result.OK(carmiles);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param carmiles
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, Carmiles carmiles) {
      return super.exportXls(request, carmiles, Carmiles.class, "车辆里程");
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
      return super.importExcel(request, response, Carmiles.class);
  }

}
