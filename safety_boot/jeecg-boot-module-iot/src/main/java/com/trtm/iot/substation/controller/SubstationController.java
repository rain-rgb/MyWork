package com.trtm.iot.substation.controller;

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
import com.trtm.iot.substation.entity.Substation;
import com.trtm.iot.substation.service.ISubstationService;

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
 * @Description: substation
 * @Author: jeecg-boot
 * @Date:   2025-05-13
 * @Version: V1.0
 */
@Api(tags="substation")
@RestController
@RequestMapping("/substation/substation")
@Slf4j
public class SubstationController extends JeecgController<Substation, ISubstationService> {
	@Autowired
	private ISubstationService substationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param substation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "substation-分页列表查询")
	@ApiOperation(value="substation-分页列表查询", notes="substation-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Substation substation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Substation> queryWrapper = QueryGenerator.initQueryWrapper(substation, req.getParameterMap());
		Page<Substation> page = new Page<Substation>(pageNo, pageSize);
		IPage<Substation> pageList = substationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param substation
	 * @return
	 */
	@AutoLog(value = "substation-添加")
	@ApiOperation(value="substation-添加", notes="substation-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Substation substation) {
		substationService.save(substation);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param substation
	 * @return
	 */
	@AutoLog(value = "substation-编辑")
	@ApiOperation(value="substation-编辑", notes="substation-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Substation substation) {
		substationService.updateById(substation);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "substation-通过id删除")
	@ApiOperation(value="substation-通过id删除", notes="substation-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		substationService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "substation-批量删除")
	@ApiOperation(value="substation-批量删除", notes="substation-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.substationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "substation-通过id查询")
	@ApiOperation(value="substation-通过id查询", notes="substation-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Substation substation = substationService.getById(id);
		if(substation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(substation);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param substation
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Substation substation) {
        return super.exportXls(request, substation, Substation.class, "substation");
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
        return super.importExcel(request, response, Substation.class);
    }

}
