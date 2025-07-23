package com.trtm.sy.syRegistered.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.sy.syRegistered.entity.SyRegistered;
import com.trtm.sy.syRegistered.service.ISyRegisteredService;

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
 * @Description: sy_registered
 * @Author: jeecg-boot
 * @Date:   2022-09-22
 * @Version: V1.0
 */
@Api(tags="sy_registered")
@RestController
@RequestMapping("/syRegistered/syRegistered")
@Slf4j
public class SyRegisteredController extends JeecgController<SyRegistered, ISyRegisteredService> {
	@Autowired
	private ISyRegisteredService syRegisteredService;
	
	/**
	 * 分页列表查询
	 *
	 * @param syRegistered
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_registered-分页列表查询")
	@ApiOperation(value="sy_registered-分页列表查询", notes="sy_registered-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyRegistered syRegistered,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req, String sys_depart_orgcode) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode == null) {
			syRegistered.setSysorgcode(loginUser.getOrgCode() + "*");
		} else {
			syRegistered.setSysorgcode(sys_depart_orgcode + "*");
		}
		QueryWrapper<SyRegistered> queryWrapper = QueryGenerator.initQueryWrapper(syRegistered, req.getParameterMap());
		Page<SyRegistered> page = new Page<SyRegistered>(pageNo, pageSize);
		IPage<SyRegistered> pageList = syRegisteredService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  *   修改闭合状态
	  *
	  * @param syRegistered
	  * @return
	  */
	 @AutoLog(value = "sy_registered-修改闭合状态")
	 @ApiOperation(value="sy_registered-修改闭合状态", notes="sy_registered-修改闭合状态")
	 @PostMapping(value = "/changestatus")
	 public Result<?> changestatus(@RequestBody SyRegistered syRegistered) {
	 	syRegistered.setClosestatus(1);
		 syRegisteredService.updateById(syRegistered);
		 return Result.OK("修改闭合状态！");
	 }
	
	/**
	 *   添加
	 *
	 * @param syRegistered
	 * @return
	 */
	@AutoLog(value = "sy_registered-添加")
	@ApiOperation(value="sy_registered-添加", notes="sy_registered-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyRegistered syRegistered) {
		syRegisteredService.save(syRegistered);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syRegistered
	 * @return
	 */
	@AutoLog(value = "sy_registered-编辑")
	@ApiOperation(value="sy_registered-编辑", notes="sy_registered-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyRegistered syRegistered) {
		syRegisteredService.updateById(syRegistered);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_registered-通过id删除")
	@ApiOperation(value="sy_registered-通过id删除", notes="sy_registered-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syRegisteredService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_registered-批量删除")
	@ApiOperation(value="sy_registered-批量删除", notes="sy_registered-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syRegisteredService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_registered-通过id查询")
	@ApiOperation(value="sy_registered-通过id查询", notes="sy_registered-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyRegistered syRegistered = syRegisteredService.getById(id);
		if(syRegistered==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syRegistered);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syRegistered
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyRegistered syRegistered) {
        return super.exportXls(request, syRegistered, SyRegistered.class, "sy_registered");
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
        return super.importExcel(request, response, SyRegistered.class);
    }

}
