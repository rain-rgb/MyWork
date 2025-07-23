package com.trtm.sy.syyuanliaoquyangweigui.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.syyuanliaoquyangweigui.entity.SyYuanliaoquyangweigui;
import com.trtm.sy.syyuanliaoquyangweigui.service.ISyYuanliaoquyangweiguiService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: sy_yuanliaoquyangweigui
 * @Author: jeecg-boot
 * @Date:   2022-09-14
 * @Version: V1.0
 */
@Api(tags="sy_yuanliaoquyangweigui")
@RestController
@RequestMapping("/syyuanliaoquyangweigui/syYuanliaoquyangweigui")
@Slf4j
public class SyYuanliaoquyangweiguiController extends JeecgController<SyYuanliaoquyangweigui, ISyYuanliaoquyangweiguiService> {
	@Autowired
	private ISyYuanliaoquyangweiguiService syYuanliaoquyangweiguiService;
	
	/**
	 * 分页列表查询
	 *
	 * @param syYuanliaoquyangweigui
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_yuanliaoquyangweigui-分页列表查询")
	@ApiOperation(value="sy_yuanliaoquyangweigui-分页列表查询", notes="sy_yuanliaoquyangweigui-分页列表查询")
	@GetMapping(value = "/list1")
	public Result<?> queryPageList(SyYuanliaoquyangweigui syYuanliaoquyangweigui,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req, String sys_depart_orgcode) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode == null) {
			syYuanliaoquyangweigui.setComment(loginUser.getOrgCode() + "*");
		} else {
			syYuanliaoquyangweigui.setComment(sys_depart_orgcode + "*");
		}
		QueryWrapper<SyYuanliaoquyangweigui> queryWrapper = QueryGenerator.initQueryWrapper(syYuanliaoquyangweigui, req.getParameterMap());

		Page<SyYuanliaoquyangweigui> page = new Page<SyYuanliaoquyangweigui>(pageNo, pageSize);
		IPage<SyYuanliaoquyangweigui> pageList = syYuanliaoquyangweiguiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param syYuanliaoquyangweigui
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "sy_yuanliaoquyangweigui-分页列表查询")
	 @ApiOperation(value="sy_yuanliaoquyangweigui-分页列表查询", notes="sy_yuanliaoquyangweigui-分页列表查询")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(SyYuanliaoquyangweigui syYuanliaoquyangweigui,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req, String sys_depart_orgcode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if (sys_depart_orgcode == null) {
			 syYuanliaoquyangweigui.setComment(loginUser.getOrgCode() + "*");
		 } else {
			 syYuanliaoquyangweigui.setComment(sys_depart_orgcode + "*");
		 }
		 QueryWrapper<SyYuanliaoquyangweigui> queryWrapper = QueryGenerator.initQueryWrapper(syYuanliaoquyangweigui, req.getParameterMap());
		 queryWrapper.isNotNull("bzz_sg_dw");
		 Page<SyYuanliaoquyangweigui> page = new Page<SyYuanliaoquyangweigui>(pageNo, pageSize);
		 IPage<SyYuanliaoquyangweigui> pageList = syYuanliaoquyangweiguiService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	 /**
	  * 分页列表查询
	  *
	  * @param syYuanliaoquyangweigui
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "sy_yuanliaoquyangweigui-分页列表查询")
	 @ApiOperation(value="sy_yuanliaoquyangweigui-分页列表查询", notes="sy_yuanliaoquyangweigui-分页列表查询")
	 @GetMapping(value = "/list3")
	 public Result<?> queryPageList3(SyYuanliaoquyangweigui syYuanliaoquyangweigui,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req, String sys_depart_orgcode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if (sys_depart_orgcode == null) {
			 syYuanliaoquyangweigui.setComment(loginUser.getOrgCode() + "*");
		 } else {
			 syYuanliaoquyangweigui.setComment(sys_depart_orgcode + "*");
		 }
		 QueryWrapper<SyYuanliaoquyangweigui> queryWrapper = QueryGenerator.initQueryWrapper(syYuanliaoquyangweigui, req.getParameterMap());
		 queryWrapper.isNotNull("bzz_jl_dw");
		 Page<SyYuanliaoquyangweigui> page = new Page<SyYuanliaoquyangweigui>(pageNo, pageSize);
		 IPage<SyYuanliaoquyangweigui> pageList = syYuanliaoquyangweiguiService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param syYuanliaoquyangweigui
	 * @return
	 */
	@AutoLog(value = "sy_yuanliaoquyangweigui-添加")
	@ApiOperation(value="sy_yuanliaoquyangweigui-添加", notes="sy_yuanliaoquyangweigui-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyYuanliaoquyangweigui syYuanliaoquyangweigui) {
		syYuanliaoquyangweiguiService.save(syYuanliaoquyangweigui);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syYuanliaoquyangweigui
	 * @return
	 */
	@AutoLog(value = "sy_yuanliaoquyangweigui-编辑")
	@ApiOperation(value="sy_yuanliaoquyangweigui-编辑", notes="sy_yuanliaoquyangweigui-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyYuanliaoquyangweigui syYuanliaoquyangweigui) {
		syYuanliaoquyangweiguiService.updateById(syYuanliaoquyangweigui);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_yuanliaoquyangweigui-通过id删除")
	@ApiOperation(value="sy_yuanliaoquyangweigui-通过id删除", notes="sy_yuanliaoquyangweigui-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syYuanliaoquyangweiguiService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_yuanliaoquyangweigui-批量删除")
	@ApiOperation(value="sy_yuanliaoquyangweigui-批量删除", notes="sy_yuanliaoquyangweigui-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syYuanliaoquyangweiguiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_yuanliaoquyangweigui-通过id查询")
	@ApiOperation(value="sy_yuanliaoquyangweigui-通过id查询", notes="sy_yuanliaoquyangweigui-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyYuanliaoquyangweigui syYuanliaoquyangweigui = syYuanliaoquyangweiguiService.getById(id);
		if(syYuanliaoquyangweigui==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syYuanliaoquyangweigui);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syYuanliaoquyangweigui
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyYuanliaoquyangweigui syYuanliaoquyangweigui) {
        return super.exportXls(request, syYuanliaoquyangweigui, SyYuanliaoquyangweigui.class, "sy_yuanliaoquyangweigui");
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
        return super.importExcel(request, response, SyYuanliaoquyangweigui.class);
    }

}
