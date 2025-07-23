package com.trtm.sy.sylxdps.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.sylxdps.entity.SyDpsJcTestitemtype;
import com.trtm.sy.sylxdps.service.ISyDpsJcTestitemService;
import com.trtm.sy.sylxdps.service.ISyDpsJcTestitemtypeService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

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
 * @Description: sy_dps_jc_testitemtype
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Api(tags="sy_dps_jc_testitemtype")
@RestController
@RequestMapping("/sylxdps/syDpsJcTestitemtype")
@Slf4j
public class SyDpsJcTestitemtypeController extends JeecgController<SyDpsJcTestitemtype, ISyDpsJcTestitemtypeService> {
	@Autowired
	private ISyDpsJcTestitemtypeService syDpsJcTestitemtypeService;
	@Autowired
	private ISyDpsJcTestitemService testitemService;

	/**
	 * 分页列表查询
	 *
	 * @param syDpsJcTestitemtype
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_dps_jc_testitemtype-分页列表查询")
	@ApiOperation(value="sy_dps_jc_testitemtype-分页列表查询", notes="sy_dps_jc_testitemtype-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDpsJcTestitemtype syDpsJcTestitemtype,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyDpsJcTestitemtype> queryWrapper = QueryGenerator.initQueryWrapper(syDpsJcTestitemtype, req.getParameterMap());
		Page<SyDpsJcTestitemtype> page = new Page<SyDpsJcTestitemtype>(pageNo, pageSize);
		IPage<SyDpsJcTestitemtype> pageList = syDpsJcTestitemtypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param syDpsJcTestitemtype
	 * @return
	 */
	@AutoLog(value = "sy_dps_jc_testitemtype-添加")
	@ApiOperation(value="sy_dps_jc_testitemtype-添加", notes="sy_dps_jc_testitemtype-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDpsJcTestitemtype syDpsJcTestitemtype) {
		syDpsJcTestitemtypeService.save(syDpsJcTestitemtype);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param syDpsJcTestitemtype
	 * @return
	 */
	@AutoLog(value = "sy_dps_jc_testitemtype-编辑")
	@ApiOperation(value="sy_dps_jc_testitemtype-编辑", notes="sy_dps_jc_testitemtype-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDpsJcTestitemtype syDpsJcTestitemtype) {
		syDpsJcTestitemtypeService.updateById(syDpsJcTestitemtype);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_jc_testitemtype-通过id删除")
	@ApiOperation(value="sy_dps_jc_testitemtype-通过id删除", notes="sy_dps_jc_testitemtype-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDpsJcTestitemtypeService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_dps_jc_testitemtype-批量删除")
	@ApiOperation(value="sy_dps_jc_testitemtype-批量删除", notes="sy_dps_jc_testitemtype-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDpsJcTestitemtypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_jc_testitemtype-通过id查询")
	@ApiOperation(value="sy_dps_jc_testitemtype-通过id查询", notes="sy_dps_jc_testitemtype-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyDpsJcTestitemtype syDpsJcTestitemtype = syDpsJcTestitemtypeService.getById(id);
		if(syDpsJcTestitemtype==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDpsJcTestitemtype);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syDpsJcTestitemtype
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsJcTestitemtype syDpsJcTestitemtype) {
        return super.exportXls(request, syDpsJcTestitemtype, SyDpsJcTestitemtype.class, "sy_dps_jc_testitemtype");
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
        return super.importExcel(request, response, SyDpsJcTestitemtype.class);
    }

	 /**
	  * 查询试验类型接口
	  * @return
	  */
	@GetMapping("/queryItemType")
	public Result<?> queryItemType(String type, Boolean isAll){
		List<SyDpsJcTestitemtype> syDpsJcTestitemtype = syDpsJcTestitemtypeService.queryItemType(type, isAll);
		return Result.OK(syDpsJcTestitemtype);
	}

	 /**
	  * 查询试验类型报告
	  */
	@GetMapping("/getList/{titCode}")
	public Result<?> getList(@PathVariable String titCode){
		List<Map> map = testitemService.getList(titCode);
		return Result.OK(map);
	}

	 /**
	  * 委托表格试验类型
	  */
	 @GetMapping("/type/List")
	 public Result<?> typeList(HttpServletRequest request, HttpServletResponse response,
							   @RequestParam(value = "type", required = false) String type,
							   @RequestParam(value = "bg", required = false) String bg,
							   @RequestParam(value = "isAll", required = false) Boolean isAll) {
		 List<Map<String, Object>> list = testitemService.typeList(type, bg, isAll);
		 return Result.OK(list);
	 }




}
