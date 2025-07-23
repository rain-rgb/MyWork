package com.trtm.iot.zhengyang.controller;

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
import com.trtm.iot.zhengyang.entity.ZhengyangProcedure;
import com.trtm.iot.zhengyang.service.IZhengyangProcedureService;

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
 * @Description: 蒸养工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Api(tags="蒸养工序表信息")
@RestController
@RequestMapping("/zhengyang/zhengyangProcedure")
@Slf4j
public class ZhengyangProcedureController extends JeecgController<ZhengyangProcedure, IZhengyangProcedureService> {
	@Autowired
	private IZhengyangProcedureService zhengyangProcedureService;
	 @Autowired
	 private IZhiliangrenwudanService zhiliangrenwudanService;
	/**
	 * 分页列表查询
	 *
	 * @param zhengyangProcedure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "蒸养工序表信息-分页列表查询")
	@ApiOperation(value="蒸养工序表信息-分页列表查询", notes="蒸养工序表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZhengyangProcedure zhengyangProcedure,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZhengyangProcedure> queryWrapper = QueryGenerator.initQueryWrapper(zhengyangProcedure, req.getParameterMap());
		Page<ZhengyangProcedure> page = new Page<ZhengyangProcedure>(pageNo, pageSize);
		IPage<ZhengyangProcedure> pageList = zhengyangProcedureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param zhengyangProcedure
	 * @return
	 */
	@AutoLog(value = "蒸养工序表信息-添加")
	@ApiOperation(value="蒸养工序表信息-添加", notes="蒸养工序表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZhengyangProcedure zhengyangProcedure) {
		zhengyangProcedureService.save(zhengyangProcedure);
		return Result.OK("添加成功！");
	}


	 /**
	  *   蒸养确认
	  *
	  * @param zhengyangProcedure
	  * @return
	  */
	 @AutoLog(value = "蒸养工序表信息-蒸养确认")
	 @ApiOperation(value="蒸养工序表信息-蒸养确认", notes="蒸养工序表信息-蒸养确认")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody ZhengyangProcedure zhengyangProcedure) {
		 Zhiliangrenwudan zhiliangrenwudan=new Zhiliangrenwudan();
		 QueryWrapper<Zhiliangrenwudan> queryWrapper=new QueryWrapper<>();
		 queryWrapper.eq("uuid",zhengyangProcedure.getUuid());
		 Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
		 if(one==null){
			 return Result.error("蒸养确认失败!");
		 }else {
			 zhiliangrenwudan.setId(one.getId());
		 	if(zhengyangProcedure.getStatus1()!=null){
				zhiliangrenwudan.setZhengyangstatus1(zhengyangProcedure.getStatus1());
			}else if(zhengyangProcedure.getStatus2()!=null){
				zhiliangrenwudan.setZhengyangstatus2(zhengyangProcedure.getStatus2());
			}
			 zhiliangrenwudanService.updateById(zhiliangrenwudan);
			 zhengyangProcedureService.save(zhengyangProcedure);
			 return Result.OK("蒸养确认成功！");
		 }

	 }

	/**
	 *  编辑
	 *
	 * @param zhengyangProcedure
	 * @return
	 */
	@AutoLog(value = "蒸养工序表信息-编辑")
	@ApiOperation(value="蒸养工序表信息-编辑", notes="蒸养工序表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZhengyangProcedure zhengyangProcedure) {
		zhengyangProcedureService.updateById(zhengyangProcedure);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "蒸养工序表信息-通过id删除")
	@ApiOperation(value="蒸养工序表信息-通过id删除", notes="蒸养工序表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zhengyangProcedureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "蒸养工序表信息-批量删除")
	@ApiOperation(value="蒸养工序表信息-批量删除", notes="蒸养工序表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zhengyangProcedureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "蒸养工序表信息-通过id查询")
	@ApiOperation(value="蒸养工序表信息-通过id查询", notes="蒸养工序表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZhengyangProcedure zhengyangProcedure = zhengyangProcedureService.getById(id);
		if(zhengyangProcedure==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zhengyangProcedure);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zhengyangProcedure
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZhengyangProcedure zhengyangProcedure) {
        return super.exportXls(request, zhengyangProcedure, ZhengyangProcedure.class, "蒸养工序表信息");
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
        return super.importExcel(request, response, ZhengyangProcedure.class);
    }

}
