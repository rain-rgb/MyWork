package com.trtm.sy.wtgl.qywtd.controller;

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
import com.trtm.sy.wtgl.qywtd.entity.SyDpsYyQuyangjiludan;
import com.trtm.sy.wtgl.qywtd.service.ISyDpsYyQuyangjiludanService;

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
 * @Description: sy_dps_yy_quyangjiludan
 * @Author: jeecg-boot
 * @Date:   2023-03-20
 * @Version: V1.0
 */
@Api(tags="sy_dps_yy_quyangjiludan")
@RestController
@RequestMapping("/qywtd/syDpsYyQuyangjiludan")
@Slf4j
public class SyDpsYyQuyangjiludanController extends JeecgController<SyDpsYyQuyangjiludan, ISyDpsYyQuyangjiludanService> {
	@Autowired
	private ISyDpsYyQuyangjiludanService syDpsYyQuyangjiludanService;

	/**
	 * 分页列表查询
	 *
	 * @param syDpsYyQuyangjiludan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_quyangjiludan-分页列表查询")
	@ApiOperation(value="sy_dps_yy_quyangjiludan-分页列表查询", notes="sy_dps_yy_quyangjiludan-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDpsYyQuyangjiludan syDpsYyQuyangjiludan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyDpsYyQuyangjiludan> queryWrapper = QueryGenerator.initQueryWrapper(syDpsYyQuyangjiludan, req.getParameterMap());
		Page<SyDpsYyQuyangjiludan> page = new Page<SyDpsYyQuyangjiludan>(pageNo, pageSize);
		IPage<SyDpsYyQuyangjiludan> pageList = syDpsYyQuyangjiludanService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param syDpsYyQuyangjiludan
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_quyangjiludan-添加")
	@ApiOperation(value="sy_dps_yy_quyangjiludan-添加", notes="sy_dps_yy_quyangjiludan-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDpsYyQuyangjiludan syDpsYyQuyangjiludan) {
		syDpsYyQuyangjiludanService.save(syDpsYyQuyangjiludan);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param syDpsYyQuyangjiludan
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_quyangjiludan-编辑")
	@ApiOperation(value="sy_dps_yy_quyangjiludan-编辑", notes="sy_dps_yy_quyangjiludan-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDpsYyQuyangjiludan syDpsYyQuyangjiludan) {
		syDpsYyQuyangjiludanService.updateById(syDpsYyQuyangjiludan);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_quyangjiludan-通过id删除")
	@ApiOperation(value="sy_dps_yy_quyangjiludan-通过id删除", notes="sy_dps_yy_quyangjiludan-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDpsYyQuyangjiludanService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_quyangjiludan-批量删除")
	@ApiOperation(value="sy_dps_yy_quyangjiludan-批量删除", notes="sy_dps_yy_quyangjiludan-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDpsYyQuyangjiludanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_quyangjiludan-通过id查询")
	@ApiOperation(value="sy_dps_yy_quyangjiludan-通过id查询", notes="sy_dps_yy_quyangjiludan-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyDpsYyQuyangjiludan syDpsYyQuyangjiludan = syDpsYyQuyangjiludanService.getById(id);
		if(syDpsYyQuyangjiludan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDpsYyQuyangjiludan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syDpsYyQuyangjiludan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsYyQuyangjiludan syDpsYyQuyangjiludan) {
        return super.exportXls(request, syDpsYyQuyangjiludan, SyDpsYyQuyangjiludan.class, "sy_dps_yy_quyangjiludan");
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
        return super.importExcel(request, response, SyDpsYyQuyangjiludan.class);
    }





}
