package com.trtm.iot.hntconsignpic.controller;

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
import com.trtm.iot.hntconsignpic.entity.HntConsignPic;
import com.trtm.iot.hntconsignpic.service.IHntConsignPicService;

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
 * @Description: 混凝土见证取样样品图表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
@Api(tags="混凝土见证取样样品图表信息")
@RestController
@RequestMapping("/hntconsignpic/hntConsignPic")
@Slf4j
public class HntConsignPicController extends JeecgController<HntConsignPic, IHntConsignPicService> {
	@Autowired
	private IHntConsignPicService hntConsignPicService;

	/**
	 * 分页列表查询
	 *
	 * @param hntConsignPic
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样样品图表信息-分页列表查询")
	@ApiOperation(value="混凝土见证取样样品图表信息-分页列表查询", notes="混凝土见证取样样品图表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HntConsignPic hntConsignPic,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HntConsignPic> queryWrapper = QueryGenerator.initQueryWrapper(hntConsignPic, req.getParameterMap());
		Page<HntConsignPic> page = new Page<HntConsignPic>(pageNo, pageSize);
		IPage<HntConsignPic> pageList = hntConsignPicService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 @AutoLog(value = "混凝土见证取样样品图表信息-查询")
	 @ApiOperation(value="混凝土见证取样样品图表信息-查询", notes="混凝土见证取样样品图表信息-查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(HntConsignPic hntConsignPic, HttpServletRequest req) {
		 QueryWrapper<HntConsignPic> queryWrapper = QueryGenerator.initQueryWrapper(hntConsignPic, req.getParameterMap());
		 List<HntConsignPic> pageList = hntConsignPicService.list(queryWrapper);
		 return Result.OK(pageList);
	 }
	/**
	 *   添加
	 *
	 * @param hntConsignPic
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样样品图表信息-添加")
	@ApiOperation(value="混凝土见证取样样品图表信息-添加", notes="混凝土见证取样样品图表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HntConsignPic hntConsignPic) {
		hntConsignPicService.save(hntConsignPic);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hntConsignPic
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样样品图表信息-编辑")
	@ApiOperation(value="混凝土见证取样样品图表信息-编辑", notes="混凝土见证取样样品图表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HntConsignPic hntConsignPic) {
		hntConsignPicService.updateById(hntConsignPic);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样样品图表信息-通过id删除")
	@ApiOperation(value="混凝土见证取样样品图表信息-通过id删除", notes="混凝土见证取样样品图表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hntConsignPicService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样样品图表信息-批量删除")
	@ApiOperation(value="混凝土见证取样样品图表信息-批量删除", notes="混凝土见证取样样品图表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hntConsignPicService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样样品图表信息-通过id查询")
	@ApiOperation(value="混凝土见证取样样品图表信息-通过id查询", notes="混凝土见证取样样品图表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HntConsignPic hntConsignPic = hntConsignPicService.getById(id);
		if(hntConsignPic==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hntConsignPic);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hntConsignPic
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HntConsignPic hntConsignPic) {
        return super.exportXls(request, hntConsignPic, HntConsignPic.class, "混凝土见证取样样品图表信息");
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
        return super.importExcel(request, response, HntConsignPic.class);
    }

}
