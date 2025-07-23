package com.trtm.iot.jtwbs.controller;

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
import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.trtm.iot.jtwbs.service.IJtwbsService;

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
 * @Description: jtwbs
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Api(tags="jtwbs")
@RestController
@RequestMapping("/jtwbs/jtwbs")
@Slf4j
public class JtwbsController extends JeecgController<Jtwbs, IJtwbsService> {
	@Autowired
	private IJtwbsService jtwbsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param jtwbs
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "jtwbs-分页列表查询")
	@ApiOperation(value="jtwbs-分页列表查询", notes="jtwbs-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Jtwbs jtwbs,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Jtwbs> queryWrapper = QueryGenerator.initQueryWrapper(jtwbs, req.getParameterMap());
		Page<Jtwbs> page = new Page<Jtwbs>(pageNo, pageSize);
		IPage<Jtwbs> pageList = jtwbsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param jtwbs
	 * @return
	 */
	@AutoLog(value = "jtwbs-添加")
	@ApiOperation(value="jtwbs-添加", notes="jtwbs-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Jtwbs jtwbs) {
		jtwbsService.save(jtwbs);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jtwbs
	 * @return
	 */
	@AutoLog(value = "jtwbs-编辑")
	@ApiOperation(value="jtwbs-编辑", notes="jtwbs-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Jtwbs jtwbs) {
		jtwbsService.updateById(jtwbs);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jtwbs-通过id删除")
	@ApiOperation(value="jtwbs-通过id删除", notes="jtwbs-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		jtwbsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "jtwbs-批量删除")
	@ApiOperation(value="jtwbs-批量删除", notes="jtwbs-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.jtwbsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jtwbs-通过id查询")
	@ApiOperation(value="jtwbs-通过id查询", notes="jtwbs-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Jtwbs jtwbs = jtwbsService.getById(id);
		if(jtwbs==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(jtwbs);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param jtwbs
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Jtwbs jtwbs) {
        return super.exportXls(request, jtwbs, Jtwbs.class, "jtwbs");
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
        return super.importExcel(request, response, Jtwbs.class);
    }

}
