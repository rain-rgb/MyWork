package com.trtm.iot.company_invoice.controller;

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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.company_payment.entity.CompanyPayment;
import com.trtm.iot.company_payment.service.ICompanyPaymentService;
import com.trtm.iot.company_project.service.ICompanyProjectService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.company_invoice.entity.CompanyInvoice;
import com.trtm.iot.company_invoice.service.ICompanyInvoiceService;

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
 * @Description: 开票管理
 * @Author: jeecg-boot
 * @Date:   2025-04-02
 * @Version: V1.0
 */
@Api(tags="开票管理")
@RestController
@RequestMapping("/company_invoice/companyInvoice")
@Slf4j
public class CompanyInvoiceController extends JeecgController<CompanyInvoice, ICompanyInvoiceService> {
	@Autowired
	private ICompanyInvoiceService companyInvoiceService;
	@Autowired
	private ICompanyProjectService companyProjectService;
	@Autowired
	private ICompanyPaymentService companyPaymentService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyInvoice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "开票管理-分页列表查询")
	@ApiOperation(value="开票管理-分页列表查询", notes="开票管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyInvoice companyInvoice,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyInvoice> queryWrapper = QueryGenerator.initQueryWrapper(companyInvoice, req.getParameterMap());
		Page<CompanyInvoice> page = new Page<CompanyInvoice>(pageNo, pageSize);
		IPage<CompanyInvoice> pageList = companyInvoiceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyInvoice
	 * @return
	 */
	@AutoLog(value = "开票管理-添加")
	@ApiOperation(value="开票管理-添加", notes="开票管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyInvoice companyInvoice) {
		companyInvoiceService.saveAndCopyToPayment(companyInvoice);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyInvoice
	 * @return
	 */
	@AutoLog(value = "开票管理-编辑")
	@ApiOperation(value="开票管理-编辑", notes="开票管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyInvoice companyInvoice) {
		//获取原数据
		CompanyInvoice companyInvoiceOld = companyInvoiceService.getById(companyInvoice.getId());
		companyInvoiceService.updateMain(companyInvoice);

		BigDecimal amountOld = companyInvoiceOld.getAmount();
		BigDecimal amountNew = companyInvoice.getAmount();
		BigDecimal amount1 = amountOld != null ? amountOld : BigDecimal.ZERO;
		BigDecimal amount2 = amountNew != null ? amountNew : BigDecimal.ZERO;
		//判断如果不相同，用totalamountNew-totalamountOld，修改项目信息
		if (companyInvoice.getStatus()==1 && amount1.compareTo(amount2) != 0) {
			BigDecimal amount = amount2.subtract(amount1);
			companyInvoice.setAmount(amount);
			companyProjectService.updateByInvoice(companyInvoice, 2);
		}
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "开票管理-通过id删除")
	@ApiOperation(value="开票管理-通过id删除", notes="开票管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		CompanyInvoice companyInvoice = companyInvoiceService.getById(id);
		companyInvoiceService.removeById(id);
		String invoiceNumber = companyInvoice.getInvoiceNumber();
		LambdaQueryWrapper<CompanyPayment> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(CompanyPayment::getInvoiceNumber, invoiceNumber);
		CompanyPayment one = companyPaymentService.getOne(queryWrapper);
		if (companyInvoice.getStatus()==1) {
			companyProjectService.updateByInvoice(companyInvoice, 3);
		}
		if (one!=null&&one.getStatus()>0) {
			companyPaymentService.removeById(one.getId());
			companyProjectService.updateByPayment(one, 3);
		}
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "开票管理-批量删除")
	@ApiOperation(value="开票管理-批量删除", notes="开票管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyInvoiceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "开票管理-通过id查询")
	@ApiOperation(value="开票管理-通过id查询", notes="开票管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyInvoice companyInvoice = companyInvoiceService.getById(id);
		if(companyInvoice==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(companyInvoice);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyInvoice
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyInvoice companyInvoice) {
        return super.exportXls(request, companyInvoice, CompanyInvoice.class, "开票管理");
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
        return super.importExcel(request, response, CompanyInvoice.class);
    }

}
