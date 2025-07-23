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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.sy.sylxdps.entity.SyDpsSyTableheader;
import com.trtm.sy.sylxdps.service.ISyDpsSyTableheaderService;

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
 * @Description: sy_dps_sy_tableheader
 * @Author: jeecg-boot
 * @Date:   2023-02-10
 * @Version: V1.0
 */
@Api(tags="sy_dps_sy_tableheader")
@RestController
@RequestMapping("/sylxdps/syDpsSyTableheader")
@Slf4j
public class SyDpsSyTableheaderController extends JeecgController<SyDpsSyTableheader, ISyDpsSyTableheaderService> {
	@Autowired
	private ISyDpsSyTableheaderService syDpsSyTableheaderService;

	/**
	 * 分页列表查询
	 *
	 * @param syDpsSyTableheader
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_tableheader-分页列表查询")
	@ApiOperation(value="sy_dps_sy_tableheader-分页列表查询", notes="sy_dps_sy_tableheader-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDpsSyTableheader syDpsSyTableheader,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyDpsSyTableheader> queryWrapper = QueryGenerator.initQueryWrapper(syDpsSyTableheader, req.getParameterMap());
		Page<SyDpsSyTableheader> page = new Page<SyDpsSyTableheader>(pageNo, pageSize);
		IPage<SyDpsSyTableheader> pageList = syDpsSyTableheaderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param syDpsSyTableheader
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_tableheader-添加")
	@ApiOperation(value="sy_dps_sy_tableheader-添加", notes="sy_dps_sy_tableheader-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDpsSyTableheader syDpsSyTableheader) {
		syDpsSyTableheaderService.save(syDpsSyTableheader);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param syDpsSyTableheader
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_tableheader-编辑")
	@ApiOperation(value="sy_dps_sy_tableheader-编辑", notes="sy_dps_sy_tableheader-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDpsSyTableheader syDpsSyTableheader) {
		syDpsSyTableheaderService.updateById(syDpsSyTableheader);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_tableheader-通过id删除")
	@ApiOperation(value="sy_dps_sy_tableheader-通过id删除", notes="sy_dps_sy_tableheader-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDpsSyTableheaderService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_tableheader-批量删除")
	@ApiOperation(value="sy_dps_sy_tableheader-批量删除", notes="sy_dps_sy_tableheader-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDpsSyTableheaderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_sy_tableheader-通过id查询")
	@ApiOperation(value="sy_dps_sy_tableheader-通过id查询", notes="sy_dps_sy_tableheader-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyDpsSyTableheader syDpsSyTableheader = syDpsSyTableheaderService.getById(id);
		if(syDpsSyTableheader==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDpsSyTableheader);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syDpsSyTableheader
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsSyTableheader syDpsSyTableheader) {
        return super.exportXls(request, syDpsSyTableheader, SyDpsSyTableheader.class, "sy_dps_sy_tableheader");
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
        return super.importExcel(request, response, SyDpsSyTableheader.class);
    }

}
