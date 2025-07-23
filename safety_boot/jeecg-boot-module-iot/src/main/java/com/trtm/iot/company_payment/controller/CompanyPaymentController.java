package com.trtm.iot.company_payment.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.company_project.entity.CompanyProject;
import com.trtm.iot.company_project.service.ICompanyProjectService;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.company_payment.entity.CompanyPayment;
import com.trtm.iot.company_payment.service.ICompanyPaymentService;

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
 * @Description: 回款管理
 * @Author: jeecg-boot
 * @Date:   2025-04-02
 * @Version: V1.0
 */
@Api(tags="回款管理")
@RestController
@RequestMapping("/company_payment/companyPayment")
@Slf4j
public class CompanyPaymentController extends JeecgController<CompanyPayment, ICompanyPaymentService> {
	@Autowired
	private ICompanyPaymentService companyPaymentService;
	@Autowired
	private ICompanyProjectService companyProjectService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyPayment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "回款管理-分页列表查询")
	@ApiOperation(value="回款管理-分页列表查询", notes="回款管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyPayment companyPayment,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyPayment> queryWrapper = QueryGenerator.initQueryWrapper(companyPayment, req.getParameterMap());
		Page<CompanyPayment> page = new Page<CompanyPayment>(pageNo, pageSize);
		IPage<CompanyPayment> pageList = companyPaymentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyPayment
	 * @return
	 */
	@AutoLog(value = "回款管理-添加")
	@ApiOperation(value="回款管理-添加", notes="回款管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyPayment companyPayment) {
		companyPaymentService.saveMian(companyPayment);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyPayment
	 * @return
	 */
	@AutoLog(value = "回款管理-编辑")
	@ApiOperation(value="回款管理-编辑", notes="回款管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyPayment companyPayment) {
		Integer id = companyPayment.getId();
		CompanyPayment companyPaymentOld = companyPaymentService.getById(id);
		companyPaymentService.updateById(companyPayment);
		BigDecimal paymentAmountOld = companyPaymentOld.getPaymentAmount();
		BigDecimal paymentAmountNew = companyPayment.getPaymentAmount();
		BigDecimal amount1 = paymentAmountOld != null ? paymentAmountOld : BigDecimal.ZERO;
		BigDecimal amount2 = paymentAmountNew != null ? paymentAmountNew : BigDecimal.ZERO;
		if (companyPayment.getStatus() >0 && amount1.compareTo(amount2) != 0) {
			BigDecimal amount = amount2.subtract(amount1);
			companyPayment.setPaymentAmount(amount);
			companyProjectService.updateByPayment(companyPayment, 2);
		}

		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "回款管理-通过id删除")
	@ApiOperation(value="回款管理-通过id删除", notes="回款管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		CompanyPayment companyPayment = companyPaymentService.getById(id);
		companyPaymentService.removeById(id);
		companyProjectService.updateByPayment(companyPayment, 3);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "回款管理-批量删除")
	@ApiOperation(value="回款管理-批量删除", notes="回款管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyPaymentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "回款管理-通过id查询")
	@ApiOperation(value="回款管理-通过id查询", notes="回款管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyPayment companyPayment = companyPaymentService.getById(id);
		if(companyPayment==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(companyPayment);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyPayment
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyPayment companyPayment) {
        return super.exportXls(request, companyPayment, CompanyPayment.class, "回款管理");
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
        return super.importExcel(request, response, CompanyPayment.class);
    }

}
