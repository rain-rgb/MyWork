package com.trtm.sy.syztgl.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.syztgl.entity.SyRenyuanguanli;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.syztgl.service.ISyRenyuanguanliService;

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
 * @Description: 试验人员管理
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@Api(tags="试验人员管理")
@RestController
@RequestMapping("/syztgl/syRenyuanguanli")
@Slf4j
public class SyRenyuanguanliController extends JeecgController<SyRenyuanguanli, ISyRenyuanguanliService> {
	@Autowired
	private ISyRenyuanguanliService syRenyuanguanliService;
	
	/**
	 * 分页列表查询
	 *
	 * @param syRenyuanguanli
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "试验人员管理-分页列表查询")
	@ApiOperation(value="试验人员管理-分页列表查询", notes="试验人员管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyRenyuanguanli syRenyuanguanli,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,String sys_depart_orgcode) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode == null) {
			syRenyuanguanli.setSysorgcode(loginUser.getOrgCode() + "*");
		} else {
			syRenyuanguanli.setSysorgcode(sys_depart_orgcode + "*");
		}
		if(!StringUtils.isEmpty(syRenyuanguanli.getUsername())){
			syRenyuanguanli.setUsername(syRenyuanguanli.getUsername()+"*");
		}
		QueryWrapper<SyRenyuanguanli> queryWrapper = QueryGenerator.initQueryWrapper(syRenyuanguanli, req.getParameterMap());
		Page<SyRenyuanguanli> page = new Page<SyRenyuanguanli>(pageNo, pageSize);
		IPage<SyRenyuanguanli> pageList = syRenyuanguanliService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param syRenyuanguanli
	 * @return
	 */
	@AutoLog(value = "试验人员管理-添加")
	@ApiOperation(value="试验人员管理-添加", notes="试验人员管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyRenyuanguanli syRenyuanguanli) {
		syRenyuanguanliService.save(syRenyuanguanli);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syRenyuanguanli
	 * @return
	 */
	@AutoLog(value = "试验人员管理-编辑")
	@ApiOperation(value="试验人员管理-编辑", notes="试验人员管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyRenyuanguanli syRenyuanguanli) {
		syRenyuanguanliService.updateById(syRenyuanguanli);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试验人员管理-通过id删除")
	@ApiOperation(value="试验人员管理-通过id删除", notes="试验人员管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syRenyuanguanliService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "试验人员管理-批量删除")
	@ApiOperation(value="试验人员管理-批量删除", notes="试验人员管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syRenyuanguanliService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试验人员管理-通过id查询")
	@ApiOperation(value="试验人员管理-通过id查询", notes="试验人员管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyRenyuanguanli syRenyuanguanli = syRenyuanguanliService.getById(id);
		if(syRenyuanguanli==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syRenyuanguanli);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syRenyuanguanli
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyRenyuanguanli syRenyuanguanli) {
        return super.exportXls(request, syRenyuanguanli, SyRenyuanguanli.class, "试验人员管理");
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
        return super.importExcel(request, response, SyRenyuanguanli.class);
    }

}
