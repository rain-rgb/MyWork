package com.trtm.iot.company_project.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.company_contract_management.entity.CompanyContractManagement;
import com.trtm.iot.company_contract_management.service.ICompanyContractManagementService;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagement;
import com.trtm.iot.company_order_management.service.ICompanyOrderManagementService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.company_project.entity.CompanyProject;
import com.trtm.iot.company_project.service.ICompanyProjectService;

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
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2025-03-10
 * @Version: V1.0
 */
@Api(tags="项目管理")
@RestController
@RequestMapping("/company_project/companyProject")
@Slf4j
public class CompanyProjectController extends JeecgController<CompanyProject, ICompanyProjectService> {
	@Autowired
	private ICompanyProjectService companyProjectService;
	@Autowired
	private ICompanyContractManagementService companyContractManagementService;
	@Autowired
	private ICompanyOrderManagementService companyOrderManagementService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目管理-分页列表查询")
	@ApiOperation(value="项目管理-分页列表查询", notes="项目管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyProject companyProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="100") Integer pageSize,
								   HttpServletRequest req, String sys_depart_orgcode) {
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {
			companyProject.setOrgcode(sys_depart_orgcode + "*");
		}
		if (companyProject.getProjectname() != null && companyProject.getProjectname().length() != 0) {
			companyProject.setProjectname("*" + companyProject.getProjectname() + "*");
		}
		QueryWrapper<CompanyProject> queryWrapper = QueryGenerator.initQueryWrapper(companyProject, req.getParameterMap());
		Page<CompanyProject> page = new Page<CompanyProject>(pageNo, pageSize);
		IPage<CompanyProject> pageList = companyProjectService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyProject
	 * @return
	 */
	@AutoLog(value = "项目管理-添加")
	@ApiOperation(value="项目管理-添加", notes="项目管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyProject companyProject) {
		companyProjectService.save(companyProject);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyProject
	 * @return
	 */
	@AutoLog(value = "项目管理-编辑")
	@ApiOperation(value="项目管理-编辑", notes="项目管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyProject companyProject) {
		companyProjectService.updateById(companyProject);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目管理-通过id删除")
	@ApiOperation(value="项目管理-通过id删除", notes="项目管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyProjectService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目管理-批量删除")
	@ApiOperation(value="项目管理-批量删除", notes="项目管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyProjectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目管理-通过id查询")
	@ApiOperation(value="项目管理-通过id查询", notes="项目管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyProject companyProject = companyProjectService.getById(id);
		if(companyProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(companyProject);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyProject
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyProject companyProject) {
        return super.exportXls(request, companyProject, CompanyProject.class, "项目管理");
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
        return super.importExcel(request, response, CompanyProject.class);
    }

	 @GetMapping(value = "/tj")
	 public Result<?> tj(CompanyProject companyProject, HttpServletRequest req, String sys_depart_orgcode) {
		if (sys_depart_orgcode == null) {
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
			sys_depart_orgcode = loginUser.getOrgCode();
		}
		QueryWrapper<CompanyProject> companyProjectLambdaQueryWrapper = new QueryWrapper<>();
		companyProjectLambdaQueryWrapper.select("count(1) as progresspercentage")
				.likeRight("orgCode", sys_depart_orgcode);
		CompanyProject one2 = companyProjectService.getOne(companyProjectLambdaQueryWrapper);
		// 项目数
		 BigDecimal progresspercentage = one2.getProgresspercentage();

		 QueryWrapper<CompanyContractManagement> queryWrapper = new QueryWrapper<>();
		 queryWrapper.select("count(1) as contractnumber,sum(totalamount) as totalamount")
				 .likeRight("sys_org_code", sys_depart_orgcode);
		 CompanyContractManagement one = companyContractManagementService.getOne(queryWrapper);
		 // 查询合同数量
		 String ordernumber = one.getContractnumber();
		 //查询合同金额
		 BigDecimal totalamount = one.getTotalamount();

		 QueryWrapper<CompanyOrderManagement> queryWrapper1 = new QueryWrapper<>();
		 queryWrapper1.select("count(1) as ordernumber,sum(totalamount) as totalamount")
				 .likeRight("sys_org_code", sys_depart_orgcode);
		 CompanyOrderManagement one1 = companyOrderManagementService.getOne(queryWrapper1);
		 // 查询订单数量
		 String ordernumber1 = one1.getOrdernumber();
		 //查询订单金额
		 BigDecimal totalamount1 = one1.getTotalamount();
		 Map map = new HashMap();
		 map.put("xms", progresspercentage);
		 map.put("hts", ordernumber);
		 map.put("htje", totalamount);
		 map.put("dds", ordernumber1);
		 map.put("ddje", totalamount1);
		 return Result.OK(map);
	 }
}
