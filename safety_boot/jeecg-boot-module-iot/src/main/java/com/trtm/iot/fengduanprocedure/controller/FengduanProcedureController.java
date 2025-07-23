package com.trtm.iot.fengduanprocedure.controller;

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
import com.trtm.iot.fengduanprocedure.entity.FengduanProcedure;
import com.trtm.iot.fengduanprocedure.service.IFengduanProcedureService;

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
 * @Description: 封端工序
 * @Author: jeecg-boot
 * @Date:   2021-09-18
 * @Version: V1.0
 */
@Api(tags="封端工序")
@RestController
@RequestMapping("/fengduanprocedure/fengduanProcedure")
@Slf4j
public class FengduanProcedureController extends JeecgController<FengduanProcedure, IFengduanProcedureService> {
	@Autowired
	private IFengduanProcedureService fengduanProcedureService;
	 @Autowired
	 private IZhiliangrenwudanService zhiliangrenwudanService;
	/**
	 * 分页列表查询
	 *
	 * @param fengduanProcedure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "封端工序-分页列表查询")
	@ApiOperation(value="封端工序-分页列表查询", notes="封端工序-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FengduanProcedure fengduanProcedure,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FengduanProcedure> queryWrapper = QueryGenerator.initQueryWrapper(fengduanProcedure, req.getParameterMap());
		Page<FengduanProcedure> page = new Page<FengduanProcedure>(pageNo, pageSize);
		IPage<FengduanProcedure> pageList = fengduanProcedureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param fengduanProcedure
	 * @return
	 */
	@AutoLog(value = "封端工序-添加")
	@ApiOperation(value="封端工序-添加", notes="封端工序-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FengduanProcedure fengduanProcedure) {
		fengduanProcedureService.save(fengduanProcedure);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加
	  *
	  * @param fengduanProcedure
	  * @return
	  */
	 @AutoLog(value = "封端工序-添加")
	 @ApiOperation(value="封端工序-添加", notes="封端工序-添加")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody FengduanProcedure fengduanProcedure) {
		 Zhiliangrenwudan zhiliangrenwudan = new Zhiliangrenwudan();
		 QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("uuid", fengduanProcedure.getUuid());
		 Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
		 if (one == null) {
			 return Result.error("封端确认失败!");
		 } else {
			 zhiliangrenwudan.setId(one.getId());
			 zhiliangrenwudan.setFengduanstatus(2);
			 zhiliangrenwudanService.updateById(zhiliangrenwudan);
			 fengduanProcedure.setStarttime(one.getProductiontime());
			 fengduanProcedure.setStatus(2);
			 fengduanProcedureService.save(fengduanProcedure);
			 return Result.OK("封端确认成功!");
		 }
	 }


	/**
	 *  编辑
	 *
	 * @param fengduanProcedure
	 * @return
	 */
	@AutoLog(value = "封端工序-编辑")
	@ApiOperation(value="封端工序-编辑", notes="封端工序-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FengduanProcedure fengduanProcedure) {
		fengduanProcedureService.updateById(fengduanProcedure);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "封端工序-通过id删除")
	@ApiOperation(value="封端工序-通过id删除", notes="封端工序-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		fengduanProcedureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "封端工序-批量删除")
	@ApiOperation(value="封端工序-批量删除", notes="封端工序-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fengduanProcedureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "封端工序-通过id查询")
	@ApiOperation(value="封端工序-通过id查询", notes="封端工序-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FengduanProcedure fengduanProcedure = fengduanProcedureService.getById(id);
		if(fengduanProcedure==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fengduanProcedure);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fengduanProcedure
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FengduanProcedure fengduanProcedure) {
        return super.exportXls(request, fengduanProcedure, FengduanProcedure.class, "封端工序");
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
        return super.importExcel(request, response, FengduanProcedure.class);
    }

}
