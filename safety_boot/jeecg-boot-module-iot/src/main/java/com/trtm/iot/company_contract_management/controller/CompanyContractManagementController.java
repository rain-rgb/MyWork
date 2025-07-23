package com.trtm.iot.company_contract_management.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.company_customer.entity.CompanyCustomer;
import com.trtm.iot.company_customer.service.ICompanyCustomerService;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagement;
import com.trtm.iot.company_order_management.service.ICompanyOrderManagementService;
import com.trtm.iot.company_project.entity.CompanyProject;
import com.trtm.iot.company_project.service.ICompanyProjectService;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.company_contract_management.entity.CompanyContractManagement;
import com.trtm.iot.company_contract_management.service.ICompanyContractManagementService;

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
 * @Description: 合同管理
 * @Author: jeecg-boot
 * @Date: 2024-07-10
 * @Version: V1.0
 */
@Api(tags = "合同管理")
@RestController
@RequestMapping("/company_contract_management/companyContractManagement")
@Slf4j
public class CompanyContractManagementController extends JeecgController<CompanyContractManagement, ICompanyContractManagementService> {
    @Autowired
    private ICompanyContractManagementService companyContractManagementService;
    @Autowired
    private ICompanyOrderManagementService companyOrderManagementService;
    @Autowired
    private ICompanyProjectService companyProjectService;

    /**
     * 分页列表查询
     *
     * @param companyContractManagement
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "合同管理-分页列表查询")
    @ApiOperation(value = "合同管理-分页列表查询", notes = "合同管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(CompanyContractManagement companyContractManagement,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = " ", defaultValue = "100") Integer pageSize,
                                   HttpServletRequest req) {
		String sysOrgCode = companyContractManagement.getSysOrgCode();
		if (sysOrgCode!=null){
			companyContractManagement.setSysOrgCode(sysOrgCode+"*");
		}
        QueryWrapper<CompanyContractManagement> queryWrapper = QueryGenerator.initQueryWrapper(companyContractManagement, req.getParameterMap());
        Page<CompanyContractManagement> page = new Page<CompanyContractManagement>(pageNo, pageSize);
        IPage<CompanyContractManagement> pageList = companyContractManagementService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "合同管理-项目基本信息")
    @ApiOperation(value = "合同管理-项目基本信息", notes = "合同管理-项目基本信息")
    @GetMapping(value = "/jbxxlist")
    public Result<?> jbxxlist(String sysOrgCode) {
        QueryWrapper<CompanyContractManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("projectname,count(1) as number,COALESCE(SUM(totalamount), 0) AS totalamount,COALESCE(SUM(schedule), 0) AS schedule")
                .eq("sys_org_code", sysOrgCode);
        CompanyContractManagement one = companyContractManagementService.getOne(queryWrapper);

		QueryWrapper<CompanyOrderManagement> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("count(1) as ssxm")
				.eq("sys_org_code", sysOrgCode);
		CompanyOrderManagement one1 = companyOrderManagementService.getOne(queryWrapper1);

		one.setSsxm(one1.getSsxm());
		return Result.OK(one);
    }

    /**
     * 添加
     *
     * @param companyContractManagement
     * @return
     */
    @AutoLog(value = "合同管理-添加")
    @ApiOperation(value = "合同管理-添加", notes = "合同管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody CompanyContractManagement companyContractManagement) {
        companyContractManagementService.add(companyContractManagement);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param companyContractManagement
     * @return
     */
    @AutoLog(value = "合同管理-编辑")
    @ApiOperation(value = "合同管理-编辑", notes = "合同管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody CompanyContractManagement companyContractManagement) {
        //数据更改之前获取原数据
        String id = companyContractManagement.getId();
        CompanyContractManagement companyContractManagementOld = companyContractManagementService.getById(id);
        companyContractManagementService.updateById(companyContractManagement);

        //判断两次的合同金额是否相同
        BigDecimal totalamountOld = companyContractManagementOld.getTotalamount();
        BigDecimal totalamountNew = companyContractManagement.getTotalamount();
        BigDecimal amount1 = totalamountOld != null ? totalamountOld : BigDecimal.ZERO;
        BigDecimal amount2 = totalamountNew != null ? totalamountNew : BigDecimal.ZERO;
        //判断如果不相同，用totalamountNew-totalamountOld，修改项目信息
        if (amount1.compareTo(amount2) != 0) {
            BigDecimal totalamount = amount2.subtract(amount1);
            companyContractManagement.setTotalamount(totalamount);
            companyProjectService.updateByContract(companyContractManagement, 2);
        }
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同管理-通过id删除")
    @ApiOperation(value = "合同管理-通过id删除", notes = "合同管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        CompanyContractManagement companyContractManagement = companyContractManagementService.getById(id);
        companyContractManagementService.removeById(id);
        companyProjectService.updateByContract(companyContractManagement, 3);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "合同管理-批量删除")
    @ApiOperation(value = "合同管理-批量删除", notes = "合同管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyContractManagementService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同管理-通过id查询")
    @ApiOperation(value = "合同管理-通过id查询", notes = "合同管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyContractManagement companyContractManagement = companyContractManagementService.getById(id);
        if (companyContractManagement == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(companyContractManagement);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param companyContractManagement
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyContractManagement companyContractManagement) {
        return super.exportXls(request, companyContractManagement, CompanyContractManagement.class, "合同管理");
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
        return super.importExcel(request, response, CompanyContractManagement.class);
    }

}
