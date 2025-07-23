package com.trtm.sy.wtgl.qyd.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.wtgl.qyd.entity.SyDpsYyQydlistview;
import com.trtm.sy.wtgl.qyd.service.ISyDpsYyQydlistviewService;
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
 * @Description: sy_dps_yy_qydlistview
 * @Author: jeecg-boot
 * @Date:   2023-08-25
 * @Version: V1.0
 */
@Api(tags="sy_dps_yy_qydlistview")
@RestController
@RequestMapping("/qyd/syDpsYyQydlistview")
@Slf4j
public class SyDpsYyQydlistviewController extends JeecgController<SyDpsYyQydlistview, ISyDpsYyQydlistviewService> {
	@Autowired
	private ISyDpsYyQydlistviewService syDpsYyQydlistviewService;

	/**
	 * 分页列表查询
	 *
	 * @param syDpsYyQydlistview
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_qydlistview-分页列表查询")
	@ApiOperation(value="sy_dps_yy_qydlistview-分页列表查询", notes="sy_dps_yy_qydlistview-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDpsYyQydlistview syDpsYyQydlistview,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyDpsYyQydlistview> queryWrapper = QueryGenerator.initQueryWrapper(syDpsYyQydlistview, req.getParameterMap());
		Page<SyDpsYyQydlistview> page = new Page<SyDpsYyQydlistview>(pageNo, pageSize);
		IPage<SyDpsYyQydlistview> pageList = syDpsYyQydlistviewService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param syDpsYyQydlistview
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_qydlistview-添加")
	@ApiOperation(value="sy_dps_yy_qydlistview-添加", notes="sy_dps_yy_qydlistview-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDpsYyQydlistview syDpsYyQydlistview) {
		syDpsYyQydlistviewService.save(syDpsYyQydlistview);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param syDpsYyQydlistview
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_qydlistview-编辑")
	@ApiOperation(value="sy_dps_yy_qydlistview-编辑", notes="sy_dps_yy_qydlistview-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDpsYyQydlistview syDpsYyQydlistview) {
		syDpsYyQydlistviewService.updateById(syDpsYyQydlistview);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_qydlistview-通过id删除")
	@ApiOperation(value="sy_dps_yy_qydlistview-通过id删除", notes="sy_dps_yy_qydlistview-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDpsYyQydlistviewService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_qydlistview-批量删除")
	@ApiOperation(value="sy_dps_yy_qydlistview-批量删除", notes="sy_dps_yy_qydlistview-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDpsYyQydlistviewService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_qydlistview-通过id查询")
	@ApiOperation(value="sy_dps_yy_qydlistview-通过id查询", notes="sy_dps_yy_qydlistview-通过id查询")
	@GetMapping(value = "/getById")
	public Result<?> getById(@RequestParam(name="id",required=true) String id) {
		SyDpsYyQydlistview syDpsYyQydlistview = syDpsYyQydlistviewService.getById(id);
		if(syDpsYyQydlistview==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDpsYyQydlistview);
	}

	 /**
	  * 通过id查询取样记录详情
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "")
	 @ApiOperation(value="通过id查询取样记录详情", notes="")
	 @GetMapping(value = "/queryById")
	 public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		 return Result.OK(syDpsYyQydlistviewService.queryById(id));
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param syDpsYyQydlistview
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsYyQydlistview syDpsYyQydlistview) {
        return super.exportXls(request, syDpsYyQydlistview, SyDpsYyQydlistview.class, "sy_dps_yy_qydlistview");
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
        return super.importExcel(request, response, SyDpsYyQydlistview.class);
    }

}
