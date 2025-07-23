package com.trtm.iot.zlpz.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zlpz.entity.ZlpzProject;
import com.trtm.iot.zlpz.service.IZlpzProjectService;

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
 * @Description: zlpz_project
 * @Author: jeecg-boot
 * @Date:   2023-03-20
 * @Version: V1.0
 */
@Api(tags="zlpz_project")
@RestController
@RequestMapping("/zlpz/zlpzProject")
@Slf4j
public class ZlpzProjectController extends JeecgController<ZlpzProject, IZlpzProjectService> {
	@Autowired
	private IZlpzProjectService zlpzProjectService;

	/**
	 * 分页列表查询
	 *
	 * @param zlpzProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "zlpz_project-分页列表查询")
	@ApiOperation(value="zlpz_project-分页列表查询", notes="zlpz_project-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZlpzProject zlpzProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZlpzProject> queryWrapper = QueryGenerator.initQueryWrapper(zlpzProject, req.getParameterMap());
		Page<ZlpzProject> page = new Page<ZlpzProject>(pageNo, pageSize);
		IPage<ZlpzProject> pageList = zlpzProjectService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	@GetMapping(value = "/list1")
	public Result<?> queryPageList1(ZlpzProject zlpzProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZlpzProject> queryWrapper = QueryGenerator.initQueryWrapper(zlpzProject, req.getParameterMap());
		List<ZlpzProject> pageList = zlpzProjectService.list(queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param zlpzProject
	 * @return
	 */
	@AutoLog(value = "zlpz_project-添加")
	@ApiOperation(value="zlpz_project-添加", notes="zlpz_project-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZlpzProject zlpzProject) {
		zlpzProjectService.save(zlpzProject);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param zlpzProject
	 * @return
	 */
	@AutoLog(value = "zlpz_project-编辑")
	@ApiOperation(value="zlpz_project-编辑", notes="zlpz_project-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZlpzProject zlpzProject) {
		zlpzProjectService.updateById(zlpzProject);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "zlpz_project-通过id删除")
	@ApiOperation(value="zlpz_project-通过id删除", notes="zlpz_project-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zlpzProjectService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "zlpz_project-批量删除")
	@ApiOperation(value="zlpz_project-批量删除", notes="zlpz_project-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zlpzProjectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "zlpz_project-通过id查询")
	@ApiOperation(value="zlpz_project-通过id查询", notes="zlpz_project-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZlpzProject zlpzProject = zlpzProjectService.getById(id);
		if(zlpzProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zlpzProject);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/querydata")
	public Result<?> querydata(@RequestParam(name="id",required=true) String id) {
		ZlpzProject zlpzProject = zlpzProjectService.getById(id);
		if(zlpzProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zlpzProject);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zlpzProject
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZlpzProject zlpzProject) {
        return super.exportXls(request, zlpzProject, ZlpzProject.class, "zlpz_project");
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
        return super.importExcel(request, response, ZlpzProject.class);
    }

}
