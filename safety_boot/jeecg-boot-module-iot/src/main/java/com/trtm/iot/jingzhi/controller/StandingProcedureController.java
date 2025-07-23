package com.trtm.iot.jingzhi.controller;

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
import com.trtm.iot.jingzhi.entity.StandingProcedure;
import com.trtm.iot.jingzhi.service.IStandingProcedureService;

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
 * @Description: 收面静置工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Api(tags="收面静置工序表信息")
@RestController
@RequestMapping("/jingzhi/standingProcedure")
@Slf4j
public class StandingProcedureController extends JeecgController<StandingProcedure, IStandingProcedureService> {
	@Autowired
	private IStandingProcedureService standingProcedureService;
	 @Autowired
	 private IZhiliangrenwudanService zhiliangrenwudanService;
	/**
	 * 分页列表查询
	 *
	 * @param standingProcedure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "收面静置工序表信息-分页列表查询")
	@ApiOperation(value="收面静置工序表信息-分页列表查询", notes="收面静置工序表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StandingProcedure standingProcedure,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StandingProcedure> queryWrapper = QueryGenerator.initQueryWrapper(standingProcedure, req.getParameterMap());
		Page<StandingProcedure> page = new Page<StandingProcedure>(pageNo, pageSize);
		IPage<StandingProcedure> pageList = standingProcedureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param standingProcedure
	 * @return
	 */
	@AutoLog(value = "收面静置工序表信息-添加")
	@ApiOperation(value="收面静置工序表信息-添加", notes="收面静置工序表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StandingProcedure standingProcedure) {
		standingProcedureService.save(standingProcedure);
		return Result.OK("添加成功！");
	}


	 /**
	  *   收面静置确认
	  *
	  * @param standingProcedure
	  * @return
	  */
	 @AutoLog(value = "收面静置工序表信息-收面静置确认")
	 @ApiOperation(value="收面静置工序表信息-收面静置确认", notes="收面静置工序表信息-收面静置确认")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody StandingProcedure standingProcedure) {
		 Zhiliangrenwudan zhiliangrenwudan=new Zhiliangrenwudan();
		 QueryWrapper<Zhiliangrenwudan> queryWrapper=new QueryWrapper<>();
		 queryWrapper.eq("uuid",standingProcedure.getUuid());
		 Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
		 if(one==null){
			 return Result.error("收面静置确认失败!");
		 }else {
			 zhiliangrenwudan.setId(one.getId());
			 zhiliangrenwudan.setJingzhistatus(2);
			 zhiliangrenwudanService.updateById(zhiliangrenwudan);
			 standingProcedure.setStatus(2);
			 standingProcedureService.save(standingProcedure);
			 return Result.OK("收面静置确认成功！");
		 }

	 }

	/**
	 *  编辑
	 *
	 * @param standingProcedure
	 * @return
	 */
	@AutoLog(value = "收面静置工序表信息-编辑")
	@ApiOperation(value="收面静置工序表信息-编辑", notes="收面静置工序表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StandingProcedure standingProcedure) {
		standingProcedureService.updateById(standingProcedure);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "收面静置工序表信息-通过id删除")
	@ApiOperation(value="收面静置工序表信息-通过id删除", notes="收面静置工序表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		standingProcedureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "收面静置工序表信息-批量删除")
	@ApiOperation(value="收面静置工序表信息-批量删除", notes="收面静置工序表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.standingProcedureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "收面静置工序表信息-通过id查询")
	@ApiOperation(value="收面静置工序表信息-通过id查询", notes="收面静置工序表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StandingProcedure standingProcedure = standingProcedureService.getById(id);
		if(standingProcedure==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(standingProcedure);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param standingProcedure
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StandingProcedure standingProcedure) {
        return super.exportXls(request, standingProcedure, StandingProcedure.class, "收面静置工序表信息");
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
        return super.importExcel(request, response, StandingProcedure.class);
    }

}
