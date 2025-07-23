package com.trtm.sy.sycspz.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.sycspz.service.ISyMParamRatioService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.sycspz.entity.SyMParamRatio;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: sy_m_param_ratio
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
@Api(tags="sy_m_param_ratio")
@RestController
@RequestMapping("/sycspz/syMParamRatio")
@Slf4j
public class SyMParamRatioController extends JeecgController<SyMParamRatio, ISyMParamRatioService> {
	@Autowired
	private ISyMParamRatioService syMParamRatioService;


	 @RequestMapping(value = "/mparamratioList", method = RequestMethod.GET)
	 public Result<?> mparamratioList(HttpServletRequest request, HttpServletResponse response,
									  @RequestParam(value = "offset", required = false) Integer offset,
									  @RequestParam(value = "limit", required = false) Integer limit,
									  @RequestParam(value = "GatherUse", required = false) String GatherUse,
									  @RequestParam(value = "MixType", required = false) String MixType) {
		 IPage<Map<String, Object>> list =  syMParamRatioService.mparamratioList( offset, limit, GatherUse, MixType);
		 return Result.OK(list);
	 }
	/**
	 * 分页列表查询
	 *
	 * @param syMParamRatio
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_m_param_ratio-分页列表查询")
	@ApiOperation(value="sy_m_param_ratio-分页列表查询", notes="sy_m_param_ratio-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyMParamRatio syMParamRatio,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyMParamRatio> queryWrapper = QueryGenerator.initQueryWrapper(syMParamRatio, req.getParameterMap());
		Page<SyMParamRatio> page = new Page<SyMParamRatio>(pageNo, pageSize);
		IPage<SyMParamRatio> pageList = syMParamRatioService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param syMParamRatio
	 * @return
	 */
	@AutoLog(value = "sy_m_param_ratio-添加")
	@ApiOperation(value="sy_m_param_ratio-添加", notes="sy_m_param_ratio-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyMParamRatio syMParamRatio) {
		syMParamRatioService.save(syMParamRatio);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syMParamRatio
	 * @return
	 */
	@AutoLog(value = "sy_m_param_ratio-编辑")
	@ApiOperation(value="sy_m_param_ratio-编辑", notes="sy_m_param_ratio-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyMParamRatio syMParamRatio) {
		syMParamRatioService.updateById(syMParamRatio);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_m_param_ratio-通过id删除")
	@ApiOperation(value="sy_m_param_ratio-通过id删除", notes="sy_m_param_ratio-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syMParamRatioService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_m_param_ratio-批量删除")
	@ApiOperation(value="sy_m_param_ratio-批量删除", notes="sy_m_param_ratio-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syMParamRatioService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_m_param_ratio-通过id查询")
	@ApiOperation(value="sy_m_param_ratio-通过id查询", notes="sy_m_param_ratio-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyMParamRatio syMParamRatio = syMParamRatioService.getById(id);
		if(syMParamRatio==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syMParamRatio);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syMParamRatio
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyMParamRatio syMParamRatio) {
        return super.exportXls(request, syMParamRatio, SyMParamRatio.class, "sy_m_param_ratio");
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
        return super.importExcel(request, response, SyMParamRatio.class);
    }

}
