package com.trtm.sy.syztgl.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.syztgl.entity.SyDanweiguanli;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.syztgl.service.ISyDanweiguanliService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 试验单位管理
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@Api(tags="试验单位管理")
@RestController
@RequestMapping("/syztgl/syDanweiguanli")
@Slf4j
public class SyDanweiguanliController extends JeecgController<SyDanweiguanli, ISyDanweiguanliService> {
	@Autowired
	private ISyDanweiguanliService syDanweiguanliService;
	
	/**
	 * 分页列表查询
	 *
	 * @param syDanweiguanli
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "试验单位管理-分页列表查询")
	@ApiOperation(value="试验单位管理-分页列表查询", notes="试验单位管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDanweiguanli syDanweiguanli,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req ,String sys_depart_orgcode) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode == null) {
			syDanweiguanli.setSysorgcode(loginUser.getOrgCode() + "*");
		} else {
			syDanweiguanli.setSysorgcode(sys_depart_orgcode + "*");
		}
		if(!StringUtils.isEmpty(syDanweiguanli.getDwname())){
			syDanweiguanli.setDwname(syDanweiguanli.getDwname()+"*");
		}
		QueryWrapper<SyDanweiguanli> queryWrapper = QueryGenerator.initQueryWrapper(syDanweiguanli, req.getParameterMap());
		Page<SyDanweiguanli> page = new Page<SyDanweiguanli>(pageNo, pageSize);
		IPage<SyDanweiguanli> pageList = syDanweiguanliService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param syDanweiguanli
	 * @return
	 */
	@AutoLog(value = "试验单位管理-添加")
	@ApiOperation(value="试验单位管理-添加", notes="试验单位管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDanweiguanli syDanweiguanli) {
		syDanweiguanliService.save(syDanweiguanli);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syDanweiguanli
	 * @return
	 */
	@AutoLog(value = "试验单位管理-编辑")
	@ApiOperation(value="试验单位管理-编辑", notes="试验单位管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDanweiguanli syDanweiguanli) {
		syDanweiguanliService.updateById(syDanweiguanli);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试验单位管理-通过id删除")
	@ApiOperation(value="试验单位管理-通过id删除", notes="试验单位管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDanweiguanliService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "试验单位管理-批量删除")
	@ApiOperation(value="试验单位管理-批量删除", notes="试验单位管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDanweiguanliService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试验单位管理-通过id查询")
	@ApiOperation(value="试验单位管理-通过id查询", notes="试验单位管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyDanweiguanli syDanweiguanli = syDanweiguanliService.getById(id);
		if(syDanweiguanli==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDanweiguanli);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syDanweiguanli
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDanweiguanli syDanweiguanli) {
        return super.exportXls(request, syDanweiguanli, SyDanweiguanli.class, "试验单位管理");
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
        return super.importExcel(request, response, SyDanweiguanli.class);
    }

}
