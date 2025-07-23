package com.trtm.sy.sycspz.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.sycspz.entity.SyMParamCube;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.sycspz.service.ISyMParamCubeService;

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
 * @Description: sy_m_param_cube
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
@Api(tags="sy_m_param_cube")
@RestController
@RequestMapping("/sycspz/syMParamCube")
@Slf4j
public class SyMParamCubeController extends JeecgController<SyMParamCube, ISyMParamCubeService> {
	@Autowired
	private ISyMParamCubeService syMParamCubeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param syMParamCube
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_m_param_cube-分页列表查询")
	@ApiOperation(value="sy_m_param_cube-分页列表查询", notes="sy_m_param_cube-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyMParamCube syMParamCube,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyMParamCube> queryWrapper = QueryGenerator.initQueryWrapper(syMParamCube, req.getParameterMap());
		Page<SyMParamCube> page = new Page<SyMParamCube>(pageNo, pageSize);
		IPage<SyMParamCube> pageList = syMParamCubeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param syMParamCube
	 * @return
	 */
	@AutoLog(value = "sy_m_param_cube-添加")
	@ApiOperation(value="sy_m_param_cube-添加", notes="sy_m_param_cube-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyMParamCube syMParamCube) {
		syMParamCubeService.save(syMParamCube);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syMParamCube
	 * @return
	 */
	@AutoLog(value = "sy_m_param_cube-编辑")
	@ApiOperation(value="sy_m_param_cube-编辑", notes="sy_m_param_cube-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyMParamCube syMParamCube) {
		syMParamCubeService.updateById(syMParamCube);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_m_param_cube-通过id删除")
	@ApiOperation(value="sy_m_param_cube-通过id删除", notes="sy_m_param_cube-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syMParamCubeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_m_param_cube-批量删除")
	@ApiOperation(value="sy_m_param_cube-批量删除", notes="sy_m_param_cube-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syMParamCubeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_m_param_cube-通过id查询")
	@ApiOperation(value="sy_m_param_cube-通过id查询", notes="sy_m_param_cube-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyMParamCube syMParamCube = syMParamCubeService.getById(id);
		if(syMParamCube==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syMParamCube);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syMParamCube
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyMParamCube syMParamCube) {
        return super.exportXls(request, syMParamCube, SyMParamCube.class, "sy_m_param_cube");
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
        return super.importExcel(request, response, SyMParamCube.class);
    }

}
