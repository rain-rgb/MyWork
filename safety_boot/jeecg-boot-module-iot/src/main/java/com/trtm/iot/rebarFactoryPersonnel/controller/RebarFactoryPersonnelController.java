package com.trtm.iot.rebarFactoryPersonnel.controller;

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
import com.trtm.iot.rebarFactoryPersonnel.entity.RebarFactoryPersonnel;
import com.trtm.iot.rebarFactoryPersonnel.service.IRebarFactoryPersonnelService;

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
 * @Description: rebar_factory_personnel
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
@Api(tags="rebar_factory_personnel")
@RestController
@RequestMapping("/rebarFactoryPersonnel/rebarFactoryPersonnel")
@Slf4j
public class RebarFactoryPersonnelController extends JeecgController<RebarFactoryPersonnel, IRebarFactoryPersonnelService> {
	@Autowired
	private IRebarFactoryPersonnelService rebarFactoryPersonnelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param rebarFactoryPersonnel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "rebar_factory_personnel-分页列表查询")
	@ApiOperation(value="rebar_factory_personnel-分页列表查询", notes="rebar_factory_personnel-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RebarFactoryPersonnel rebarFactoryPersonnel,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RebarFactoryPersonnel> queryWrapper = QueryGenerator.initQueryWrapper(rebarFactoryPersonnel, req.getParameterMap());
		Page<RebarFactoryPersonnel> page = new Page<RebarFactoryPersonnel>(pageNo, pageSize);
		IPage<RebarFactoryPersonnel> pageList = rebarFactoryPersonnelService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param rebarFactoryPersonnel
	 * @return
	 */
	@AutoLog(value = "rebar_factory_personnel-添加")
	@ApiOperation(value="rebar_factory_personnel-添加", notes="rebar_factory_personnel-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RebarFactoryPersonnel rebarFactoryPersonnel) {
		rebarFactoryPersonnelService.save(rebarFactoryPersonnel);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param rebarFactoryPersonnel
	 * @return
	 */
	@AutoLog(value = "rebar_factory_personnel-编辑")
	@ApiOperation(value="rebar_factory_personnel-编辑", notes="rebar_factory_personnel-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RebarFactoryPersonnel rebarFactoryPersonnel) {
		rebarFactoryPersonnelService.updateById(rebarFactoryPersonnel);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_factory_personnel-通过id删除")
	@ApiOperation(value="rebar_factory_personnel-通过id删除", notes="rebar_factory_personnel-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		rebarFactoryPersonnelService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "rebar_factory_personnel-批量删除")
	@ApiOperation(value="rebar_factory_personnel-批量删除", notes="rebar_factory_personnel-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.rebarFactoryPersonnelService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_factory_personnel-通过id查询")
	@ApiOperation(value="rebar_factory_personnel-通过id查询", notes="rebar_factory_personnel-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RebarFactoryPersonnel rebarFactoryPersonnel = rebarFactoryPersonnelService.getById(id);
		if(rebarFactoryPersonnel==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(rebarFactoryPersonnel);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param rebarFactoryPersonnel
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RebarFactoryPersonnel rebarFactoryPersonnel) {
        return super.exportXls(request, rebarFactoryPersonnel, RebarFactoryPersonnel.class, "rebar_factory_personnel");
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
        return super.importExcel(request, response, RebarFactoryPersonnel.class);
    }

}
