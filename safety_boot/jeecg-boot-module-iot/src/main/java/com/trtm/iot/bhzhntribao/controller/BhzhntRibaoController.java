package com.trtm.iot.bhzhntribao.controller;

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
import com.trtm.iot.bhzhntribao.entity.BhzhntRibao;
import com.trtm.iot.bhzhntribao.service.IBhzhntRibaoService;

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
 * @Description: bhzhnt_ribao
 * @Author: jeecg-boot
 * @Date:   2023-10-24
 * @Version: V1.0
 */
@Api(tags="bhzhnt_ribao")
@RestController
@RequestMapping("/bhzhntribao/bhzhntRibao")
@Slf4j
public class BhzhntRibaoController extends JeecgController<BhzhntRibao, IBhzhntRibaoService> {
	@Autowired
	private IBhzhntRibaoService bhzhntRibaoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bhzhntRibao
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhzhnt_ribao-分页列表查询")
	@ApiOperation(value="bhzhnt_ribao-分页列表查询", notes="bhzhnt_ribao-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzhntRibao bhzhntRibao,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzhntRibao> queryWrapper = QueryGenerator.initQueryWrapper(bhzhntRibao, req.getParameterMap());
		Page<BhzhntRibao> page = new Page<BhzhntRibao>(pageNo, pageSize);
		IPage<BhzhntRibao> pageList = bhzhntRibaoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bhzhntRibao
	 * @return
	 */
	@AutoLog(value = "bhzhnt_ribao-添加")
	@ApiOperation(value="bhzhnt_ribao-添加", notes="bhzhnt_ribao-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzhntRibao bhzhntRibao) {
		bhzhntRibaoService.save(bhzhntRibao);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bhzhntRibao
	 * @return
	 */
	@AutoLog(value = "bhzhnt_ribao-编辑")
	@ApiOperation(value="bhzhnt_ribao-编辑", notes="bhzhnt_ribao-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzhntRibao bhzhntRibao) {
		bhzhntRibaoService.updateById(bhzhntRibao);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhzhnt_ribao-通过id删除")
	@ApiOperation(value="bhzhnt_ribao-通过id删除", notes="bhzhnt_ribao-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzhntRibaoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhzhnt_ribao-批量删除")
	@ApiOperation(value="bhzhnt_ribao-批量删除", notes="bhzhnt_ribao-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzhntRibaoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhzhnt_ribao-通过id查询")
	@ApiOperation(value="bhzhnt_ribao-通过id查询", notes="bhzhnt_ribao-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzhntRibao bhzhntRibao = bhzhntRibaoService.getById(id);
		if(bhzhntRibao==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzhntRibao);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzhntRibao
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzhntRibao bhzhntRibao) {
        return super.exportXls(request, bhzhntRibao, BhzhntRibao.class, "bhzhnt_ribao");
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
        return super.importExcel(request, response, BhzhntRibao.class);
    }

}
