package com.trtm.iot.lmjob.controller;

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
import com.trtm.iot.lmjob.entity.LmSwFiles;
import com.trtm.iot.lmjob.service.ILmSwFilesService;

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
 * @Description: lm_sw_files
 * @Author: jeecg-boot
 * @Date:   2023-11-15
 * @Version: V1.0
 */
@Api(tags="lm_sw_files")
@RestController
@RequestMapping("/lmjob/lmSwFiles")
@Slf4j
public class LmSwFilesController extends JeecgController<LmSwFiles, ILmSwFilesService> {
	@Autowired
	private ILmSwFilesService lmSwFilesService;

	/**
	 * 分页列表查询
	 *
	 * @param lmSwFiles
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "lm_sw_files-分页列表查询")
	@ApiOperation(value="lm_sw_files-分页列表查询", notes="lm_sw_files-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(LmSwFiles lmSwFiles,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LmSwFiles> queryWrapper = QueryGenerator.initQueryWrapper(lmSwFiles, req.getParameterMap());
		Page<LmSwFiles> page = new Page<LmSwFiles>(pageNo, pageSize);
		IPage<LmSwFiles> pageList = lmSwFilesService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param lmSwFiles
	 * @return
	 */
	@AutoLog(value = "lm_sw_files-添加")
	@ApiOperation(value="lm_sw_files-添加", notes="lm_sw_files-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody LmSwFiles lmSwFiles) {
		lmSwFilesService.save(lmSwFiles);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param lmSwFiles
	 * @return
	 */
	@AutoLog(value = "lm_sw_files-编辑")
	@ApiOperation(value="lm_sw_files-编辑", notes="lm_sw_files-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody LmSwFiles lmSwFiles) {
		lmSwFilesService.updateById(lmSwFiles);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "lm_sw_files-通过id删除")
	@ApiOperation(value="lm_sw_files-通过id删除", notes="lm_sw_files-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		lmSwFilesService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "lm_sw_files-批量删除")
	@ApiOperation(value="lm_sw_files-批量删除", notes="lm_sw_files-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.lmSwFilesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "lm_sw_files-通过id查询")
	@ApiOperation(value="lm_sw_files-通过id查询", notes="lm_sw_files-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		LmSwFiles lmSwFiles = lmSwFilesService.getById(id);
		if(lmSwFiles==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(lmSwFiles);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param lmSwFiles
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LmSwFiles lmSwFiles) {
        return super.exportXls(request, lmSwFiles, LmSwFiles.class, "lm_sw_files");
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
        return super.importExcel(request, response, LmSwFiles.class);
    }

}
