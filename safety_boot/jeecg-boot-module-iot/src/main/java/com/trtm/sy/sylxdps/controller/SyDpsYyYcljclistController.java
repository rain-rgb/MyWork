package com.trtm.sy.sylxdps.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.sylxdps.entity.SyDpsYyYcljclist;
import com.trtm.sy.sylxdps.service.ISyDpsYyYcljclistService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;

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
 * @Description: sy_dps_yy_ycljclist
 * @Author: jeecg-boot
 * @Date:   2023-01-30
 * @Version: V1.0
 */
@Api(tags="sy_dps_yy_ycljclist")
@RestController
@RequestMapping("/sylxdps/syDpsYyYcljclist")
@Slf4j
public class SyDpsYyYcljclistController extends JeecgController<SyDpsYyYcljclist, ISyDpsYyYcljclistService> {
	@Autowired
	private ISyDpsYyYcljclistService syDpsYyYcljclistService;

	/**
	 * 分页列表查询
	 *
	 * @param syDpsYyYcljclist
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_ycljclist-分页列表查询")
	@ApiOperation(value="sy_dps_yy_ycljclist-分页列表查询", notes="sy_dps_yy_ycljclist-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDpsYyYcljclist syDpsYyYcljclist,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyDpsYyYcljclist> queryWrapper = QueryGenerator.initQueryWrapper(syDpsYyYcljclist, req.getParameterMap());
		Page<SyDpsYyYcljclist> page = new Page<SyDpsYyYcljclist>(pageNo, pageSize);
		IPage<SyDpsYyYcljclist> pageList = syDpsYyYcljclistService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param syDpsYyYcljclist
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_ycljclist-添加")
	@ApiOperation(value="sy_dps_yy_ycljclist-添加", notes="sy_dps_yy_ycljclist-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDpsYyYcljclist syDpsYyYcljclist) {
		syDpsYyYcljclistService.save(syDpsYyYcljclist);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param syDpsYyYcljclist
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_ycljclist-编辑")
	@ApiOperation(value="sy_dps_yy_ycljclist-编辑", notes="sy_dps_yy_ycljclist-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDpsYyYcljclist syDpsYyYcljclist) {
		syDpsYyYcljclistService.updateById(syDpsYyYcljclist);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_ycljclist-通过id删除")
	@ApiOperation(value="sy_dps_yy_ycljclist-通过id删除", notes="sy_dps_yy_ycljclist-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDpsYyYcljclistService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_ycljclist-批量删除")
	@ApiOperation(value="sy_dps_yy_ycljclist-批量删除", notes="sy_dps_yy_ycljclist-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDpsYyYcljclistService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_ycljclist-通过id查询")
	@ApiOperation(value="sy_dps_yy_ycljclist-通过id查询", notes="sy_dps_yy_ycljclist-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyDpsYyYcljclist syDpsYyYcljclist = syDpsYyYcljclistService.getById(id);
		if(syDpsYyYcljclist==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDpsYyYcljclist);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syDpsYyYcljclist
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsYyYcljclist syDpsYyYcljclist) {
        return super.exportXls(request, syDpsYyYcljclist, SyDpsYyYcljclist.class, "sy_dps_yy_ycljclist");
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
        return super.importExcel(request, response, SyDpsYyYcljclist.class);
    }

}
