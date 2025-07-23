package com.trtm.sy.formtoprint.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.formtoprint.entity.FormToPrint;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.formtoprint.entity.DpsJcTestitemtype;
import com.trtm.sy.formtoprint.service.IDpsJcTestitemtypeService;

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
 * @Description: 表单打印
 * @Author: jeecg-boot
 * @Date:   2022-09-05
 * @Version: V1.0
 */
@Api(tags="表单打印")
@RestController
@RequestMapping("/FormToPrint/dpsJcTestitemtype")
@Slf4j
public class DpsJcTestitemtypeController extends JeecgController<DpsJcTestitemtype, IDpsJcTestitemtypeService> {
	@Autowired
	private IDpsJcTestitemtypeService dpsJcTestitemtypeService;


	 @AutoLog(value = "表单打印-树形列表")
	 @ApiOperation(value="表单打印-树形列表", notes="表单打印-树形列表")
	 @GetMapping(value = "/formTreeList")
	 public Result<?> formTreeList() {
		 List<FormToPrint> list = dpsJcTestitemtypeService.getFormTreeList();
		 return Result.OK(list);
	 }

	/**
	 * 分页列表查询
	 *
	 * @param dpsJcTestitemtype
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "表单打印-分页列表查询")
	@ApiOperation(value="表单打印-分页列表查询", notes="表单打印-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DpsJcTestitemtype dpsJcTestitemtype,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DpsJcTestitemtype> queryWrapper = QueryGenerator.initQueryWrapper(dpsJcTestitemtype, req.getParameterMap());
		Page<DpsJcTestitemtype> page = new Page<DpsJcTestitemtype>(pageNo, pageSize);
		IPage<DpsJcTestitemtype> pageList = dpsJcTestitemtypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param dpsJcTestitemtype
	 * @return
	 */
	@AutoLog(value = "表单打印-添加")
	@ApiOperation(value="表单打印-添加", notes="表单打印-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DpsJcTestitemtype dpsJcTestitemtype) {
		dpsJcTestitemtypeService.save(dpsJcTestitemtype);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param dpsJcTestitemtype
	 * @return
	 */
	@AutoLog(value = "表单打印-编辑")
	@ApiOperation(value="表单打印-编辑", notes="表单打印-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DpsJcTestitemtype dpsJcTestitemtype) {
		dpsJcTestitemtypeService.updateById(dpsJcTestitemtype);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "表单打印-通过id删除")
	@ApiOperation(value="表单打印-通过id删除", notes="表单打印-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dpsJcTestitemtypeService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "表单打印-批量删除")
	@ApiOperation(value="表单打印-批量删除", notes="表单打印-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dpsJcTestitemtypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "表单打印-通过id查询")
	@ApiOperation(value="表单打印-通过id查询", notes="表单打印-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DpsJcTestitemtype dpsJcTestitemtype = dpsJcTestitemtypeService.getById(id);
		if(dpsJcTestitemtype==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(dpsJcTestitemtype);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dpsJcTestitemtype
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DpsJcTestitemtype dpsJcTestitemtype) {
        return super.exportXls(request, dpsJcTestitemtype, DpsJcTestitemtype.class, "表单打印");
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
        return super.importExcel(request, response, DpsJcTestitemtype.class);
    }

}
