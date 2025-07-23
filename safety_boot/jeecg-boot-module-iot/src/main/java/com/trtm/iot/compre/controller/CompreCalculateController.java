package com.trtm.iot.compre.controller;

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
import com.trtm.iot.compre.entity.CompreCalculate;
import com.trtm.iot.compre.service.ICompreCalculateService;

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
 * @Description: compre_calculate
 * @Author: jeecg-boot
 * @Date:   2024-06-07
 * @Version: V1.0
 */
@Api(tags="compre_calculate")
@RestController
@RequestMapping("/compre/compreCalculate")
@Slf4j
public class CompreCalculateController extends JeecgController<CompreCalculate, ICompreCalculateService> {
	@Autowired
	private ICompreCalculateService compreCalculateService;
	
	/**
	 * 分页列表查询
	 *
	 * @param compreCalculate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "compre_calculate-分页列表查询")
	@ApiOperation(value="compre_calculate-分页列表查询", notes="compre_calculate-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompreCalculate compreCalculate,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		compreCalculate.setSysOrgCode(sys_depart_orgcode+"*");
		QueryWrapper<CompreCalculate> queryWrapper = QueryGenerator.initQueryWrapper(compreCalculate, req.getParameterMap());
		Page<CompreCalculate> page = new Page<CompreCalculate>(pageNo, pageSize);
		IPage<CompreCalculate> pageList = compreCalculateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param compreCalculate
	 * @return
	 */
	@AutoLog(value = "compre_calculate-添加")
	@ApiOperation(value="compre_calculate-添加", notes="compre_calculate-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompreCalculate compreCalculate) {
		compreCalculateService.save(compreCalculate);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param compreCalculate
	 * @return
	 */
	@AutoLog(value = "compre_calculate-编辑")
	@ApiOperation(value="compre_calculate-编辑", notes="compre_calculate-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompreCalculate compreCalculate) {
		compreCalculateService.updateById(compreCalculate);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "compre_calculate-通过id删除")
	@ApiOperation(value="compre_calculate-通过id删除", notes="compre_calculate-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		compreCalculateService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "compre_calculate-批量删除")
	@ApiOperation(value="compre_calculate-批量删除", notes="compre_calculate-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.compreCalculateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "compre_calculate-通过id查询")
	@ApiOperation(value="compre_calculate-通过id查询", notes="compre_calculate-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompreCalculate compreCalculate = compreCalculateService.getById(id);
		if(compreCalculate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(compreCalculate);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param compreCalculate
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompreCalculate compreCalculate) {
        return super.exportXls(request, compreCalculate, CompreCalculate.class, "compre_calculate");
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
        return super.importExcel(request, response, CompreCalculate.class);
    }

}
