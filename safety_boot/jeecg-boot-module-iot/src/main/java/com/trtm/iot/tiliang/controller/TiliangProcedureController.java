package com.trtm.iot.tiliang.controller;

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
import com.trtm.iot.tiliang.entity.TiliangProcedure;
import com.trtm.iot.tiliang.service.ITiliangProcedureService;

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
 * @Description: 提梁工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Api(tags="提梁工序表信息")
@RestController
@RequestMapping("/tiliang/tiliangProcedure")
@Slf4j
public class TiliangProcedureController extends JeecgController<TiliangProcedure, ITiliangProcedureService> {
	@Autowired
	private ITiliangProcedureService tiliangProcedureService;
	 @Autowired
	 private IZhiliangrenwudanService zhiliangrenwudanService;
	/**
	 * 分页列表查询
	 *
	 * @param tiliangProcedure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "提梁工序表信息-分页列表查询")
	@ApiOperation(value="提梁工序表信息-分页列表查询", notes="提梁工序表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TiliangProcedure tiliangProcedure,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TiliangProcedure> queryWrapper = QueryGenerator.initQueryWrapper(tiliangProcedure, req.getParameterMap());
		Page<TiliangProcedure> page = new Page<TiliangProcedure>(pageNo, pageSize);
		IPage<TiliangProcedure> pageList = tiliangProcedureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param tiliangProcedure
	 * @return
	 */
	@AutoLog(value = "提梁工序表信息-添加")
	@ApiOperation(value="提梁工序表信息-添加", notes="提梁工序表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TiliangProcedure tiliangProcedure) {
		tiliangProcedureService.save(tiliangProcedure);
		return Result.OK("添加成功！");
	}

	 /**
	  *   提梁确认
	  *
	  * @param tiliangProcedure
	  * @return
	  */
	 @AutoLog(value = "提梁工序表信息-提梁确认")
	 @ApiOperation(value="提梁工序表信息-提梁确认", notes="提梁工序表信息-提梁确认")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody TiliangProcedure tiliangProcedure) {
		 Zhiliangrenwudan zhiliangrenwudan=new Zhiliangrenwudan();
		 QueryWrapper<Zhiliangrenwudan> queryWrapper=new QueryWrapper<>();
		 queryWrapper.eq("uuid",tiliangProcedure.getUuid());
		 Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
		 if(one==null){
			 return Result.error("提梁确认失败!");
		 }else {
			 zhiliangrenwudan.setId(one.getId());
			 zhiliangrenwudan.setTiliangstatus(2);
			 zhiliangrenwudanService.updateById(zhiliangrenwudan);
			 tiliangProcedure.setStatus(2);
			 tiliangProcedureService.save(tiliangProcedure);
			 return Result.OK("提梁确认成功！");
		 }

	 }

	/**
	 *  编辑
	 *
	 * @param tiliangProcedure
	 * @return
	 */
	@AutoLog(value = "提梁工序表信息-编辑")
	@ApiOperation(value="提梁工序表信息-编辑", notes="提梁工序表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TiliangProcedure tiliangProcedure) {
		tiliangProcedureService.updateById(tiliangProcedure);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "提梁工序表信息-通过id删除")
	@ApiOperation(value="提梁工序表信息-通过id删除", notes="提梁工序表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tiliangProcedureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "提梁工序表信息-批量删除")
	@ApiOperation(value="提梁工序表信息-批量删除", notes="提梁工序表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tiliangProcedureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "提梁工序表信息-通过id查询")
	@ApiOperation(value="提梁工序表信息-通过id查询", notes="提梁工序表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TiliangProcedure tiliangProcedure = tiliangProcedureService.getById(id);
		if(tiliangProcedure==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(tiliangProcedure);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param tiliangProcedure
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TiliangProcedure tiliangProcedure) {
        return super.exportXls(request, tiliangProcedure, TiliangProcedure.class, "提梁工序表信息");
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
        return super.importExcel(request, response, TiliangProcedure.class);
    }

}
