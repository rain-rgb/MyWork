package com.trtm.iot.company_order_management.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.company_contract_management.entity.CompanyContractManagement;
import com.trtm.iot.company_contract_management.service.ICompanyContractManagementService;
import com.trtm.iot.company_project.entity.CompanyProject;
import com.trtm.iot.company_project.service.ICompanyProjectService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagementS;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagement;
import com.trtm.iot.company_order_management.vo.CompanyOrderManagementPage;
import com.trtm.iot.company_order_management.service.ICompanyOrderManagementService;
import com.trtm.iot.company_order_management.service.ICompanyOrderManagementSService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date:   2024-07-01
 * @Version: V1.0
 */
@Api(tags="订单管理")
@RestController
@RequestMapping("/company_order_management/companyOrderManagement")
@Slf4j
public class CompanyOrderManagementController {
	@Autowired
	private ICompanyOrderManagementService companyOrderManagementService;
	@Autowired
	private ICompanyOrderManagementSService companyOrderManagementSService;
	@Autowired
	private ICompanyContractManagementService ICompanyContractManagementService;
	 @Autowired
	 private ICompanyProjectService companyProjectService;

	/**
	 * 分页列表查询
	 *
	 * @param companyOrderManagement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "订单管理-分页列表查询")
	@ApiOperation(value="订单管理-分页列表查询", notes="订单管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyOrderManagement companyOrderManagement,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String sysOrgCode = companyOrderManagement.getSysOrgCode();
		if (sysOrgCode!=null){
			companyOrderManagement.setSysOrgCode(sysOrgCode+"*");
		}
		QueryWrapper<CompanyOrderManagement> queryWrapper = QueryGenerator.initQueryWrapper(companyOrderManagement, req.getParameterMap());
		Page<CompanyOrderManagement> page = new Page<CompanyOrderManagement>(pageNo, pageSize);
		IPage<CompanyOrderManagement> pageList = companyOrderManagementService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(CompanyContractManagement companyOrderManagement, HttpServletRequest req, String sysOrgCode) {
		 if (!oConvertUtils.isEmpty(sysOrgCode)) {
			 companyOrderManagement.setSysOrgCode(sysOrgCode+"*");
		 }
		 QueryWrapper<CompanyContractManagement> queryWrapper = QueryGenerator.initQueryWrapper(companyOrderManagement, req.getParameterMap());
		 List<CompanyContractManagement> pageList = ICompanyContractManagementService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param companyOrderManagementPage
	 * @return
	 */
	@AutoLog(value = "订单管理-添加")
	@ApiOperation(value="订单管理-添加", notes="订单管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyOrderManagementPage companyOrderManagementPage) {
		CompanyOrderManagement companyOrderManagement = new CompanyOrderManagement();
		BeanUtils.copyProperties(companyOrderManagementPage, companyOrderManagement);
		companyOrderManagementService.saveMain(companyOrderManagement, companyOrderManagementPage.getCompanyOrderManagementSList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param companyOrderManagementPage
	 * @return
	 */
	@AutoLog(value = "订单管理-编辑")
	@ApiOperation(value="订单管理-编辑", notes="订单管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyOrderManagementPage companyOrderManagementPage) {
		CompanyOrderManagement companyOrderManagement = new CompanyOrderManagement();
		BeanUtils.copyProperties(companyOrderManagementPage, companyOrderManagement);
		CompanyOrderManagement companyOrderManagementEntity = companyOrderManagementService.getById(companyOrderManagement.getId());
		if(companyOrderManagementEntity==null) {
			return Result.error("未找到对应数据");
		}
		companyOrderManagementService.updateMain(companyOrderManagement, companyOrderManagementPage.getCompanyOrderManagementSList());
		return Result.OK("编辑成功!");
	}

	/**
	 *  施工记录
	 *
	 * @param companyOrderManagementPage
	 * @return
	 */
	@AutoLog(value = "订单管理-施工记录")
	@ApiOperation(value="订单管理-施工记录", notes="订单管理-施工记录")
	@PutMapping(value = "/editsg")
	public Result<?> editsg(@RequestBody CompanyOrderManagementPage companyOrderManagementPage) {
		companyOrderManagementService.editsg(companyOrderManagementPage);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单管理-通过id删除")
	@ApiOperation(value="订单管理-通过id删除", notes="订单管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		CompanyOrderManagement companyOrderManagement = companyOrderManagementService.getById(id);

		companyOrderManagementService.delMain(id);
		//修改项目信息
		companyProjectService.updateByOrders(companyOrderManagement,3);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "订单管理-批量删除")
	@ApiOperation(value="订单管理-批量删除", notes="订单管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyOrderManagementService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单管理-通过id查询")
	@ApiOperation(value="订单管理-通过id查询", notes="订单管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyOrderManagement companyOrderManagement = companyOrderManagementService.getById(id);
		if(companyOrderManagement==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(companyOrderManagement);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单管理-通过id查询")
	@ApiOperation(value="订单管理-通过id查询", notes="订单管理-通过id查询")
	@GetMapping(value = "/queryByContractnumber")
	public Result<?> queryByContractnumber(@RequestParam(name="contractnumber",required=true) String contractnumber) {

		List<CompanyOrderManagementPage> companyOrderManagementPage = companyOrderManagementService.getByContractnumber(contractnumber);
		if(companyOrderManagementPage==null) {
			return Result.error("未找到对应数据");
		}
		for (CompanyOrderManagementPage orderManagementPage : companyOrderManagementPage) {
			String ordernumber = orderManagementPage.getOrdernumber();
			List<CompanyOrderManagementS> companyOrderManagementSList = companyOrderManagementSService.selectByMainId(ordernumber);
			orderManagementPage.setCompanyOrderManagementSList(companyOrderManagementSList);
		}
		return Result.OK(companyOrderManagementPage);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单管理子表通过主表ID查询")
	@ApiOperation(value="订单管理子表主表ID查询", notes="订单管理子表-通主表ID查询")
	@GetMapping(value = "/queryCompanyOrderManagementSByMainId")
	public Result<?> queryCompanyOrderManagementSListByMainId(@RequestParam(name="ordernumber",required=true) String ordernumber) {
		List<CompanyOrderManagementS> companyOrderManagementSList = companyOrderManagementSService.selectByMainId(ordernumber);
		return Result.OK(companyOrderManagementSList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyOrderManagement
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyOrderManagement companyOrderManagement) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<CompanyOrderManagement> queryWrapper = QueryGenerator.initQueryWrapper(companyOrderManagement, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<CompanyOrderManagement> queryList = companyOrderManagementService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<CompanyOrderManagement> companyOrderManagementList = new ArrayList<CompanyOrderManagement>();
      if(oConvertUtils.isEmpty(selections)) {
          companyOrderManagementList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          companyOrderManagementList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<CompanyOrderManagementPage> pageList = new ArrayList<CompanyOrderManagementPage>();
      for (CompanyOrderManagement main : companyOrderManagementList) {
          CompanyOrderManagementPage vo = new CompanyOrderManagementPage();
          BeanUtils.copyProperties(main, vo);
          List<CompanyOrderManagementS> companyOrderManagementSList = companyOrderManagementSService.selectByMainId(main.getOrdernumber());
          vo.setCompanyOrderManagementSList(companyOrderManagementSList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "订单管理列表");
      mv.addObject(NormalExcelConstants.CLASS, CompanyOrderManagementPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("订单管理数据", "导出人:"+sysUser.getRealname(), "订单管理"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<CompanyOrderManagementPage> list = ExcelImportUtil.importExcel(file.getInputStream(), CompanyOrderManagementPage.class, params);
              for (CompanyOrderManagementPage page : list) {
                  CompanyOrderManagement po = new CompanyOrderManagement();
                  BeanUtils.copyProperties(page, po);
                  companyOrderManagementService.saveMain(po, page.getCompanyOrderManagementSList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
