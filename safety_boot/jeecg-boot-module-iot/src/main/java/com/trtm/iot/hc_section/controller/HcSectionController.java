package com.trtm.iot.hc_section.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;

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
 * @Description: 华测获取标段
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Api(tags="华测获取标段")
@RestController
@RequestMapping("/hc_section/hcSection")
@Slf4j
public class HcSectionController extends JeecgController<HcSection, IHcSectionService> {
	@Autowired
	private IHcSectionService hcSectionService;
	 @Autowired
	 private IHcProjectService hcProjectService;

	/**
	 * 分页列表查询
	 *
	 * @param hcSection
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "华测获取标段-分页列表查询")
	@ApiOperation(value="华测获取标段-分页列表查询", notes="华测获取标段-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcSection hcSection,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="100") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (hcSection.getOrgcode() == null){
			hcSection.setOrgcode(loginUser.getOrgCode()+"*");
		}else {
			hcSection.setOrgcode(hcSection.getOrgcode()+"*");
		}
		QueryWrapper<HcSection> queryWrapper = QueryGenerator.initQueryWrapper(hcSection, req.getParameterMap());
		Page<HcSection> page = new Page<HcSection>(pageNo, pageSize);
		IPage<HcSection> pageList = hcSectionService.page(page, queryWrapper);
		List<HcSection> records = pageList.getRecords();
		if (records.size() == 0){
			HcSection project1 = new HcSection();
			QueryWrapper<HcSection> queryWrapper2 = QueryGenerator.initQueryWrapper(project1, req.getParameterMap());
			Page<HcSection> page2 = new Page<HcSection>(pageNo, pageSize);
			IPage<HcSection> pageList2 = hcSectionService.page(page2, queryWrapper2);
			List<HcSection> records1 = pageList2.getRecords();
			List<HcSection> records2 = new ArrayList<>();
			if (records1.size() > 0){
				for (HcSection project :records1){
					if (hcSection.getOrgcode().contains(project.getOrgcode())){
						records2.add(project);
					}
				}
			}
			pageList.setRecords(records2);
		}
		return Result.OK(pageList);
	}

	 /**
	  * 如果项目没有，则标段反查项目
	  *
	  * @param hcSection
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "华测获取标段-分页列表查询")
	 @ApiOperation(value="华测获取标段-分页列表查询", notes="华测获取标段-分页列表查询")
	 @GetMapping(value = "/listls")
	 public Result<?> queryPageListls(HcSection hcSection,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if (hcSection.getOrgcode() != null){
			 hcSection.setOrgcode(loginUser.getOrgCode()+"*");
		 }
		 QueryWrapper<HcSection> queryWrapper = QueryGenerator.initQueryWrapper(hcSection, req.getParameterMap());
		 queryWrapper.groupBy("pjId");
		 List<HcSection> list = hcSectionService.list(queryWrapper);
		 List<String> list1 = new ArrayList<>();
		 if (list.size() > 0){
			 for (HcSection l :list){
				 list1.add(l.getPjid());
			 }
		 }
		 QueryWrapper<HcProject> queryWrapper1 = new QueryWrapper<>();
		 queryWrapper1.in("pjId",list1);
		 Page<HcProject> page = new Page<HcProject>(pageNo, pageSize);
		 IPage<HcProject> pageList = hcProjectService.page(page, queryWrapper1);
		 return Result.OK(pageList);
	 }
	/**
	 *   添加
	 *
	 * @param hcSection
	 * @return
	 */
	@AutoLog(value = "华测获取标段-添加")
	@ApiOperation(value="华测获取标段-添加", notes="华测获取标段-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcSection hcSection) {
		hcSectionService.save(hcSection);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hcSection
	 * @return
	 */
	@AutoLog(value = "华测获取标段-编辑")
	@ApiOperation(value="华测获取标段-编辑", notes="华测获取标段-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcSection hcSection) {
		hcSectionService.updateById(hcSection);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "华测获取标段-通过id删除")
	@ApiOperation(value="华测获取标段-通过id删除", notes="华测获取标段-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcSectionService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "华测获取标段-批量删除")
	@ApiOperation(value="华测获取标段-批量删除", notes="华测获取标段-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcSectionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "华测获取标段-通过id查询")
	@ApiOperation(value="华测获取标段-通过id查询", notes="华测获取标段-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcSection hcSection = hcSectionService.getById(id);
		if(hcSection==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcSection);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcSection
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcSection hcSection) {
        return super.exportXls(request, hcSection, HcSection.class, "华测获取标段");
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
        return super.importExcel(request, response, HcSection.class);
    }

}
