package com.trtm.iot.jiaozhu.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.jiaozhu.entity.PouringProcedure;
import com.trtm.iot.jiaozhu.service.IPouringProcedureService;

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
 * @Description: 混凝土浇筑工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Api(tags="混凝土浇筑工序表信息")
@RestController
@RequestMapping("/jiaozhu/pouringProcedure")
@Slf4j
public class PouringProcedureController extends JeecgController<PouringProcedure, IPouringProcedureService> {
	@Autowired
	private IPouringProcedureService pouringProcedureService;
	 @Autowired
	 private IZhiliangrenwudanService zhiliangrenwudanService;
	/**
	 * 分页列表查询
	 *
	 * @param pouringProcedure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "混凝土浇筑工序表信息-分页列表查询")
	@ApiOperation(value="混凝土浇筑工序表信息-分页列表查询", notes="混凝土浇筑工序表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PouringProcedure pouringProcedure,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PouringProcedure> queryWrapper = QueryGenerator.initQueryWrapper(pouringProcedure, req.getParameterMap());
		Page<PouringProcedure> page = new Page<PouringProcedure>(pageNo, pageSize);
		IPage<PouringProcedure> pageList = pouringProcedureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param pouringProcedure
	 * @return
	 */
	@AutoLog(value = "混凝土浇筑工序表信息-添加")
	@ApiOperation(value="混凝土浇筑工序表信息-添加", notes="混凝土浇筑工序表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PouringProcedure pouringProcedure) {
		pouringProcedureService.save(pouringProcedure);
		return Result.OK("添加成功！");
	}

	 /**
	  *   混凝土浇筑确认
	  *
	  * @param pouringProcedure
	  * @return
	  */
	 @AutoLog(value = "混凝土浇筑工序表信息-混凝土浇筑确认")
	 @ApiOperation(value="混凝土浇筑工序表信息-混凝土浇筑确认", notes="混凝土浇筑工序表信息-混凝土浇筑确认")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody PouringProcedure pouringProcedure) {
		 Zhiliangrenwudan zhiliangrenwudan=new Zhiliangrenwudan();
		 QueryWrapper<Zhiliangrenwudan> queryWrapper=new QueryWrapper<>();
		 queryWrapper.eq("uuid",pouringProcedure.getUuid());
		 Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
		 if(one==null){
			 return Result.error("混凝土浇筑确认失败!");
		 }else{
			 zhiliangrenwudan.setId(one.getId());
			 zhiliangrenwudan.setJiaozhustatus(2);
			 zhiliangrenwudanService.updateById(zhiliangrenwudan);
			 //pouringProcedure.setStarttime(one.getProductiontime());
			 pouringProcedure.setStatus(2);
			 pouringProcedureService.save(pouringProcedure);
			 return Result.OK("混凝土浇筑确认成功！");
		 }
	 }

	/**
	 *  编辑
	 *
	 * @param pouringProcedure
	 * @return
	 */
	@AutoLog(value = "混凝土浇筑工序表信息-编辑")
	@ApiOperation(value="混凝土浇筑工序表信息-编辑", notes="混凝土浇筑工序表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PouringProcedure pouringProcedure) {
		pouringProcedureService.updateById(pouringProcedure);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土浇筑工序表信息-通过id删除")
	@ApiOperation(value="混凝土浇筑工序表信息-通过id删除", notes="混凝土浇筑工序表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pouringProcedureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "混凝土浇筑工序表信息-批量删除")
	@ApiOperation(value="混凝土浇筑工序表信息-批量删除", notes="混凝土浇筑工序表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pouringProcedureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土浇筑工序表信息-通过id查询")
	@ApiOperation(value="混凝土浇筑工序表信息-通过id查询", notes="混凝土浇筑工序表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PouringProcedure pouringProcedure = pouringProcedureService.getById(id);
		if(pouringProcedure==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(pouringProcedure);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pouringProcedure
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PouringProcedure pouringProcedure) {
        return super.exportXls(request, pouringProcedure, PouringProcedure.class, "混凝土浇筑工序表信息");
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
        return super.importExcel(request, response, PouringProcedure.class);
    }

}
