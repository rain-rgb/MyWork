package com.trtm.sy.sycspz.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.sycspz.entity.SyMParamBoxqty;
import com.trtm.sy.sycspz.service.ISyMParamBoxqtyService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: sy_m_param_boxqty
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
@Api(tags="sy_m_param_boxqty")
@RestController
@RequestMapping("/sycspz/syMParamBoxqty")
@Slf4j
public class SyMParamBoxqtyController extends JeecgController<SyMParamBoxqty, ISyMParamBoxqtyService> {
	@Autowired
	private ISyMParamBoxqtyService syMParamBoxqtyService;


	 @RequestMapping(value = "/mparamraboxqtyList", method = RequestMethod.GET)
	 public Result<?> mparamraboxqtyList(HttpServletRequest request, HttpServletResponse response,
										 @RequestParam(value = "offset", required = false) Integer offset,
										 @RequestParam(value = "limit", required = false) Integer limit,
										 @RequestParam(value = "boxno", required = false) String boxno) {
		 IPage<Map<String, Object>> list = syMParamBoxqtyService.mparamraboxqtyList(offset,limit,boxno,request,response);
		 return Result.OK(list);
	 }


	 @RequestMapping(value = "/importExcel", consumes = "multipart/form-data", method = RequestMethod.POST)
	 @ResponseBody
	 public Result<?>  importExcel(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws Exception {
		 syMParamBoxqtyService.importExcel(request,response,file);
		 return Result.OK();
	 }
	
	/**
	 * 分页列表查询
	 *
	 * @param syMParamBoxqty
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_m_param_boxqty-分页列表查询")
	@ApiOperation(value="sy_m_param_boxqty-分页列表查询", notes="sy_m_param_boxqty-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyMParamBoxqty syMParamBoxqty,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyMParamBoxqty> queryWrapper = QueryGenerator.initQueryWrapper(syMParamBoxqty, req.getParameterMap());
		Page<SyMParamBoxqty> page = new Page<SyMParamBoxqty>(pageNo, pageSize);
		IPage<SyMParamBoxqty> pageList = syMParamBoxqtyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param syMParamBoxqty
	 * @return
	 */
	@AutoLog(value = "sy_m_param_boxqty-添加")
	@ApiOperation(value="sy_m_param_boxqty-添加", notes="sy_m_param_boxqty-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyMParamBoxqty syMParamBoxqty) {
		syMParamBoxqtyService.save(syMParamBoxqty);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syMParamBoxqty
	 * @return
	 */
	@AutoLog(value = "sy_m_param_boxqty-编辑")
	@ApiOperation(value="sy_m_param_boxqty-编辑", notes="sy_m_param_boxqty-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyMParamBoxqty syMParamBoxqty) {
		syMParamBoxqtyService.updateById(syMParamBoxqty);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_m_param_boxqty-通过id删除")
	@ApiOperation(value="sy_m_param_boxqty-通过id删除", notes="sy_m_param_boxqty-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syMParamBoxqtyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_m_param_boxqty-批量删除")
	@ApiOperation(value="sy_m_param_boxqty-批量删除", notes="sy_m_param_boxqty-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syMParamBoxqtyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_m_param_boxqty-通过id查询")
	@ApiOperation(value="sy_m_param_boxqty-通过id查询", notes="sy_m_param_boxqty-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyMParamBoxqty syMParamBoxqty = syMParamBoxqtyService.getById(id);
		if(syMParamBoxqty==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syMParamBoxqty);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syMParamBoxqty
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyMParamBoxqty syMParamBoxqty) {
        return super.exportXls(request, syMParamBoxqty, SyMParamBoxqty.class, "sy_m_param_boxqty");
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
        return super.importExcel(request, response, SyMParamBoxqty.class);
    }

}
