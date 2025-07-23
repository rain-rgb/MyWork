package com.trtm.iot.rebarComponentMaterial.controller;

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
import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.trtm.iot.rebarComponentMaterial.service.IRebarComponentMaterialService;

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
 * @Description: rebar_component_material
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Api(tags="rebar_component_material")
@RestController
@RequestMapping("/rebarComponentMaterial/rebarComponentMaterial")
@Slf4j
public class RebarComponentMaterialController extends JeecgController<RebarComponentMaterial, IRebarComponentMaterialService> {
	@Autowired
	private IRebarComponentMaterialService rebarComponentMaterialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param rebarComponentMaterial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "rebar_component_material-分页列表查询")
	@ApiOperation(value="rebar_component_material-分页列表查询", notes="rebar_component_material-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RebarComponentMaterial rebarComponentMaterial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RebarComponentMaterial> queryWrapper = QueryGenerator.initQueryWrapper(rebarComponentMaterial, req.getParameterMap());
		Page<RebarComponentMaterial> page = new Page<RebarComponentMaterial>(pageNo, pageSize);
		IPage<RebarComponentMaterial> pageList = rebarComponentMaterialService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param rebarComponentMaterial
	 * @return
	 */
	@AutoLog(value = "rebar_component_material-添加")
	@ApiOperation(value="rebar_component_material-添加", notes="rebar_component_material-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RebarComponentMaterial rebarComponentMaterial) {
		rebarComponentMaterialService.save(rebarComponentMaterial);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param rebarComponentMaterial
	 * @return
	 */
	@AutoLog(value = "rebar_component_material-编辑")
	@ApiOperation(value="rebar_component_material-编辑", notes="rebar_component_material-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RebarComponentMaterial rebarComponentMaterial) {
		rebarComponentMaterialService.updateById(rebarComponentMaterial);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_component_material-通过id删除")
	@ApiOperation(value="rebar_component_material-通过id删除", notes="rebar_component_material-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		rebarComponentMaterialService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "rebar_component_material-批量删除")
	@ApiOperation(value="rebar_component_material-批量删除", notes="rebar_component_material-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.rebarComponentMaterialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_component_material-通过id查询")
	@ApiOperation(value="rebar_component_material-通过id查询", notes="rebar_component_material-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RebarComponentMaterial rebarComponentMaterial = rebarComponentMaterialService.getById(id);
		if(rebarComponentMaterial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(rebarComponentMaterial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param rebarComponentMaterial
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RebarComponentMaterial rebarComponentMaterial) {
        return super.exportXls(request, rebarComponentMaterial, RebarComponentMaterial.class, "rebar_component_material");
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
        return super.importExcel(request, response, RebarComponentMaterial.class);
    }

}
