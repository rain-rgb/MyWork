package com.trtm.sy.syjcxm.controller;

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
import com.trtm.sy.syjcxm.entity.SyDpsYyJcxm;
import com.trtm.sy.syjcxm.service.ISyDpsYyJcxmService;

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
 * @Description: sy_dps_yy_jcxm
 * @Author: jeecg-boot
 * @Date:   2023-06-28
 * @Version: V1.0
 */
@Api(tags="sy_dps_yy_jcxm")
@RestController
@RequestMapping("/syjcxm/syDpsYyJcxm")
@Slf4j
public class SyDpsYyJcxmController extends JeecgController<SyDpsYyJcxm, ISyDpsYyJcxmService> {
	@Autowired
	private ISyDpsYyJcxmService syDpsYyJcxmService;

	/**
	 * 分页列表查询
	 *
	 * @param syDpsYyJcxm
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_jcxm-分页列表查询")
	@ApiOperation(value="sy_dps_yy_jcxm-分页列表查询", notes="sy_dps_yy_jcxm-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDpsYyJcxm syDpsYyJcxm,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyDpsYyJcxm> queryWrapper = QueryGenerator.initQueryWrapper(syDpsYyJcxm, req.getParameterMap());
		Page<SyDpsYyJcxm> page = new Page<SyDpsYyJcxm>(pageNo, pageSize);
		IPage<SyDpsYyJcxm> pageList = syDpsYyJcxmService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param syDpsYyJcxm
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_jcxm-添加")
	@ApiOperation(value="sy_dps_yy_jcxm-添加", notes="sy_dps_yy_jcxm-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDpsYyJcxm syDpsYyJcxm) {
		syDpsYyJcxmService.save(syDpsYyJcxm);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param syDpsYyJcxm
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_jcxm-编辑")
	@ApiOperation(value="sy_dps_yy_jcxm-编辑", notes="sy_dps_yy_jcxm-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDpsYyJcxm syDpsYyJcxm) {
		syDpsYyJcxmService.updateById(syDpsYyJcxm);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_jcxm-通过id删除")
	@ApiOperation(value="sy_dps_yy_jcxm-通过id删除", notes="sy_dps_yy_jcxm-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDpsYyJcxmService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_jcxm-批量删除")
	@ApiOperation(value="sy_dps_yy_jcxm-批量删除", notes="sy_dps_yy_jcxm-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDpsYyJcxmService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_jcxm-通过id查询")
	@ApiOperation(value="sy_dps_yy_jcxm-通过id查询", notes="sy_dps_yy_jcxm-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyDpsYyJcxm syDpsYyJcxm = syDpsYyJcxmService.getById(id);
		if(syDpsYyJcxm==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDpsYyJcxm);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syDpsYyJcxm
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsYyJcxm syDpsYyJcxm) {
        return super.exportXls(request, syDpsYyJcxm, SyDpsYyJcxm.class, "sy_dps_yy_jcxm");
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
        return super.importExcel(request, response, SyDpsYyJcxm.class);
    }


	 /**
	  *  委托表格的检测项目
	  */
	 @GetMapping("/jcxmList")
	 public Result<?> getJcxm() {
		return syDpsYyJcxmService.getJcxm();
	 }

}
