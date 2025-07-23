package com.trtm.iot.hc_project.controller;

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

import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdata;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;

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
 * @Description: 华测获取项目
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Api(tags="华测获取项目")
@RestController
@RequestMapping("/hc_project/hcProject")
@Slf4j
public class HcProjectController extends JeecgController<HcProject, IHcProjectService> {
	@Autowired
	private IHcProjectService hcProjectService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param hcProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "华测获取项目-分页列表查询")
	@ApiOperation(value="华测获取项目-分页列表查询", notes="华测获取项目-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcProject hcProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="20") Integer pageSize,
								   String orgCode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (orgCode == null){
			orgCode = loginUser.getOrgCode();
		}
		hcProject.setOrgcode(orgCode+"*");
		QueryWrapper<HcProject> queryWrapper = QueryGenerator.initQueryWrapper(hcProject, req.getParameterMap());
		Page<HcProject> page = new Page<HcProject>(pageNo, pageSize);
		IPage<HcProject> pageList = hcProjectService.page(page, queryWrapper);
		List<HcProject> records = pageList.getRecords();

		if (records.size() == 0){
			HcProject project1 = new HcProject();
			QueryWrapper<HcProject> queryWrapper2 = QueryGenerator.initQueryWrapper(project1, req.getParameterMap());
			Page<HcProject> page2 = new Page<HcProject>(pageNo, pageSize);
			IPage<HcProject> pageList2 = hcProjectService.page(page2, queryWrapper2);
			List<HcProject> records1 = pageList2.getRecords();
			List<HcProject> records2 = new ArrayList<>();
			if (records1.size() > 0){
				for (HcProject project :records1){
					if (orgCode.contains(project.getOrgcode())){
						records2.add(project);
					}
				}
			}
			pageList.setRecords(records2);
		}
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param hcProject
	 * @return
	 */
	@AutoLog(value = "华测获取项目-添加")
	@ApiOperation(value="华测获取项目-添加", notes="华测获取项目-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcProject hcProject) {
		hcProjectService.save(hcProject);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param hcProject
	 * @return
	 */
	@AutoLog(value = "华测获取项目-编辑")
	@ApiOperation(value="华测获取项目-编辑", notes="华测获取项目-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcProject hcProject) {
		hcProjectService.updateById(hcProject);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "华测获取项目-通过id删除")
	@ApiOperation(value="华测获取项目-通过id删除", notes="华测获取项目-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcProjectService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "华测获取项目-批量删除")
	@ApiOperation(value="华测获取项目-批量删除", notes="华测获取项目-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcProjectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "华测获取项目-通过id查询")
	@ApiOperation(value="华测获取项目-通过id查询", notes="华测获取项目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcProject hcProject = hcProjectService.getById(id);
		if(hcProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcProject);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcProject
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcProject hcProject) {
        return super.exportXls(request, hcProject, HcProject.class, "华测获取项目");
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
        return super.importExcel(request, response, HcProject.class);
    }

}
