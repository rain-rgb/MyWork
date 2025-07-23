package com.trtm.sy.syfccj.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.syfccj.entity.SyFccjlistInfo;
import com.trtm.sy.syfccj.service.ISyFccjlistInfoService;

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
 * @Description: 发车抽检信息
 * @Author: jeecg-boot
 * @Date:   2022-09-21
 * @Version: V1.0
 */
@Api(tags="发车抽检信息")
@RestController
@RequestMapping("/syfccjlistinfo/syFccjlistInfo")
@Slf4j
public class SyFccjlistInfoController extends JeecgController<SyFccjlistInfo, ISyFccjlistInfoService> {
	@Autowired
	private ISyFccjlistInfoService syFccjlistInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param syFccjlistInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "发车抽检信息-分页列表查询")
	@ApiOperation(value="发车抽检信息-分页列表查询", notes="发车抽检信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyFccjlistInfo syFccjlistInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		syFccjlistInfo.setUserdepartid(loginUser.getOrgCode() + "*");
		QueryWrapper<SyFccjlistInfo> queryWrapper = QueryGenerator.initQueryWrapper(syFccjlistInfo, req.getParameterMap());
		Page<SyFccjlistInfo> page = new Page<SyFccjlistInfo>(pageNo, pageSize);
		IPage<SyFccjlistInfo> pageList = syFccjlistInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param syFccjlistInfo
	 * @return
	 */
	@AutoLog(value = "发车抽检信息-添加")
	@ApiOperation(value="发车抽检信息-添加", notes="发车抽检信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyFccjlistInfo syFccjlistInfo) {
		syFccjlistInfoService.save(syFccjlistInfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syFccjlistInfo
	 * @return
	 */
	@AutoLog(value = "发车抽检信息-编辑")
	@ApiOperation(value="发车抽检信息-编辑", notes="发车抽检信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyFccjlistInfo syFccjlistInfo) {
		syFccjlistInfoService.updateById(syFccjlistInfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "发车抽检信息-通过id删除")
	@ApiOperation(value="发车抽检信息-通过id删除", notes="发车抽检信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syFccjlistInfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "发车抽检信息-批量删除")
	@ApiOperation(value="发车抽检信息-批量删除", notes="发车抽检信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syFccjlistInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "发车抽检信息-通过id查询")
	@ApiOperation(value="发车抽检信息-通过id查询", notes="发车抽检信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyFccjlistInfo syFccjlistInfo = syFccjlistInfoService.getById(id);
		if(syFccjlistInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syFccjlistInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syFccjlistInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyFccjlistInfo syFccjlistInfo) {
        return super.exportXls(request, syFccjlistInfo, SyFccjlistInfo.class, "发车抽检信息");
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
        return super.importExcel(request, response, SyFccjlistInfo.class);
    }

}
