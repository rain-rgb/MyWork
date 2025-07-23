package com.trtm.iot.anquanfxgk.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.anquanfxgk.entity.AnquanFxyhChecks;
import com.trtm.iot.anquanfxgk.service.IAnquanFxyhChecksService;

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
 * @Description: anquan_fxyh_checks
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Api(tags="anquan_fxyh_checks")
@RestController
@RequestMapping("/anquanfxgk/anquanFxyhChecks")
@Slf4j
public class AnquanFxyhChecksController extends JeecgController<AnquanFxyhChecks, IAnquanFxyhChecksService> {
	@Autowired
	private IAnquanFxyhChecksService anquanFxyhChecksService;
	
	/**
	 * 分页列表查询
	 *
	 * @param anquanFxyhChecks
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "anquan_fxyh_checks-分页列表查询")
	@ApiOperation(value="anquan_fxyh_checks-分页列表查询", notes="anquan_fxyh_checks-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AnquanFxyhChecks anquanFxyhChecks,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		QueryWrapper<AnquanFxyhChecks> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxyhChecks, req.getParameterMap());
		Page<AnquanFxyhChecks> page = new Page<AnquanFxyhChecks>(pageNo, pageSize);
		IPage<AnquanFxyhChecks> pageList = anquanFxyhChecksService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param anquanFxyhChecks
	 * @return
	 */
	@AutoLog(value = "anquan_fxyh_checks-添加")
	@ApiOperation(value="anquan_fxyh_checks-添加", notes="anquan_fxyh_checks-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AnquanFxyhChecks anquanFxyhChecks) {
		anquanFxyhChecksService.save(anquanFxyhChecks);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param anquanFxyhChecks
	 * @return
	 */
	@AutoLog(value = "anquan_fxyh_checks-编辑")
	@ApiOperation(value="anquan_fxyh_checks-编辑", notes="anquan_fxyh_checks-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AnquanFxyhChecks anquanFxyhChecks) {
		anquanFxyhChecksService.updateById(anquanFxyhChecks);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxyh_checks-通过id删除")
	@ApiOperation(value="anquan_fxyh_checks-通过id删除", notes="anquan_fxyh_checks-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		anquanFxyhChecksService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "anquan_fxyh_checks-批量删除")
	@ApiOperation(value="anquan_fxyh_checks-批量删除", notes="anquan_fxyh_checks-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.anquanFxyhChecksService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxyh_checks-通过id查询")
	@ApiOperation(value="anquan_fxyh_checks-通过id查询", notes="anquan_fxyh_checks-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AnquanFxyhChecks anquanFxyhChecks = anquanFxyhChecksService.getById(id);
		if(anquanFxyhChecks==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(anquanFxyhChecks);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param anquanFxyhChecks
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AnquanFxyhChecks anquanFxyhChecks) {
        return super.exportXls(request, anquanFxyhChecks, AnquanFxyhChecks.class, "anquan_fxyh_checks");
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
        return super.importExcel(request, response, AnquanFxyhChecks.class);
    }

}
