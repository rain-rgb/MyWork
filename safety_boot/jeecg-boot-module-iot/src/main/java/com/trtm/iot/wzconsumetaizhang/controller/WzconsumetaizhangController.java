package com.trtm.iot.wzconsumetaizhang.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.txw2.DatatypeWriter;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.vo.BhzCementStatisticsPage;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.wzconsumetaizhang.vo.WzconsumetaizhangPage;
import com.trtm.iot.wzconsumetaizhangdetail.entity.WzconsumetaizhangDetail;
import com.trtm.iot.wzconsumetaizhangdetail.service.IWzconsumetaizhangDetailService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzconsumetaizhang.entity.Wzconsumetaizhang;
import com.trtm.iot.wzconsumetaizhang.service.IWzconsumetaizhangService;

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
 * @Description: 物资原材料消耗台账主表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Api(tags="物资原材料消耗台账主表信息")
@RestController
@RequestMapping("/wzconsumetaizhang/wzconsumetaizhang")
@Slf4j
public class WzconsumetaizhangController extends JeecgController<Wzconsumetaizhang, IWzconsumetaizhangService> {
	@Autowired
	private IWzconsumetaizhangService wzconsumetaizhangService;
	 @Autowired
	 private IWzconsumetaizhangDetailService wzconsumetaizhangDetailService;
	/**
	 * 分页列表查询
	 *
	 * @param wzconsumetaizhang
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "物资原材料消耗台账主表信息-分页列表查询")
	@ApiOperation(value="物资原材料消耗台账主表信息-分页列表查询", notes="物资原材料消耗台账主表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Wzconsumetaizhang wzconsumetaizhang,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Wzconsumetaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wzconsumetaizhang, req.getParameterMap());
		Page<Wzconsumetaizhang> page = new Page<Wzconsumetaizhang>(pageNo, pageSize);
		IPage<Wzconsumetaizhang> pageList = wzconsumetaizhangService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wzconsumetaizhang
	 * @return
	 */
	@AutoLog(value = "物资原材料消耗台账主表信息-添加")
	@ApiOperation(value="物资原材料消耗台账主表信息-添加", notes="物资原材料消耗台账主表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Wzconsumetaizhang wzconsumetaizhang) {
		wzconsumetaizhangService.save(wzconsumetaizhang);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wzconsumetaizhang
	 * @return
	 */
	@AutoLog(value = "物资原材料消耗台账主表信息-编辑")
	@ApiOperation(value="物资原材料消耗台账主表信息-编辑", notes="物资原材料消耗台账主表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Wzconsumetaizhang wzconsumetaizhang) {
		wzconsumetaizhangService.updateById(wzconsumetaizhang);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物资原材料消耗台账主表信息-通过id删除")
	@ApiOperation(value="物资原材料消耗台账主表信息-通过id删除", notes="物资原材料消耗台账主表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzconsumetaizhangService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物资原材料消耗台账主表信息-批量删除")
	@ApiOperation(value="物资原材料消耗台账主表信息-批量删除", notes="物资原材料消耗台账主表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzconsumetaizhangService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物资原材料消耗台账主表信息-通过id查询")
	@ApiOperation(value="物资原材料消耗台账主表信息-通过id查询", notes="物资原材料消耗台账主表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Wzconsumetaizhang wzconsumetaizhang = wzconsumetaizhangService.getById(id);
		if(wzconsumetaizhang==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzconsumetaizhang);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzconsumetaizhang
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzconsumetaizhang wzconsumetaizhang) {
        return super.exportXls(request, wzconsumetaizhang, Wzconsumetaizhang.class, "物资原材料消耗台账主表信息");
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
        return super.importExcel(request, response, Wzconsumetaizhang.class);
    }

}
