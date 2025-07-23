package com.trtm.iot.staffbase.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.api.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.staffbase.entity.StaffBaseSalary;
import com.trtm.iot.staffbase.service.IStaffBaseSalaryService;

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
 * @Description: staff_base_salary
 * @Author: jeecg-boot
 * @Date:   2025-02-25
 * @Version: V1.0
 */
@Api(tags="staff_base_salary")
@RestController
@RequestMapping("/staffbase/staffBaseSalary")
@Slf4j
public class StaffBaseSalaryController extends JeecgController<StaffBaseSalary, IStaffBaseSalaryService> {
	@Autowired
	private IStaffBaseSalaryService staffBaseSalaryService;

	/**
	 * 分页列表查询
	 *
	 * @param staffBaseSalary
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "staff_base_salary-分页列表查询")
	@ApiOperation(value="staff_base_salary-分页列表查询", notes="staff_base_salary-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StaffBaseSalary staffBaseSalary,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if(StringUtils.isNotBlank(sys_depart_orgcode)){
			staffBaseSalary.setSysOrgCode(sys_depart_orgcode+"*");
		}else {
			staffBaseSalary.setSysOrgCode(loginUser.getOrgCode()+"*");
		}
		QueryWrapper<StaffBaseSalary> queryWrapper = QueryGenerator.initQueryWrapper(staffBaseSalary, req.getParameterMap());
		Page<StaffBaseSalary> page = new Page<StaffBaseSalary>(pageNo, pageSize);
		IPage<StaffBaseSalary> pageList = staffBaseSalaryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	/**
	 *   添加
	 *
	 * @param staffBaseSalary
	 * @return
	 */
	@AutoLog(value = "staff_base_salary-添加")
	@ApiOperation(value="staff_base_salary-添加", notes="staff_base_salary-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StaffBaseSalary staffBaseSalary) {
		staffBaseSalaryService.save(staffBaseSalary);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param staffBaseSalary
	 * @return
	 */
	@AutoLog(value = "staff_base_salary-编辑")
	@ApiOperation(value="staff_base_salary-编辑", notes="staff_base_salary-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StaffBaseSalary staffBaseSalary) {
		staffBaseSalaryService.updateById(staffBaseSalary);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "staff_base_salary-通过id删除")
	@ApiOperation(value="staff_base_salary-通过id删除", notes="staff_base_salary-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		staffBaseSalaryService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "staff_base_salary-批量删除")
	@ApiOperation(value="staff_base_salary-批量删除", notes="staff_base_salary-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.staffBaseSalaryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "staff_base_salary-通过id查询")
	@ApiOperation(value="staff_base_salary-通过id查询", notes="staff_base_salary-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StaffBaseSalary staffBaseSalary = staffBaseSalaryService.getById(id);
		if(staffBaseSalary==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(staffBaseSalary);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param staffBaseSalary
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StaffBaseSalary staffBaseSalary) {
        return super.exportXls(request, staffBaseSalary, StaffBaseSalary.class, "staff_base_salary");
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
        return super.importExcel(request, response, StaffBaseSalary.class);
    }

}
