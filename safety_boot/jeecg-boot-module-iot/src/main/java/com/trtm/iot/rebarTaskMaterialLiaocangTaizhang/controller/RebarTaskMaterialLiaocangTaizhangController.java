package com.trtm.iot.rebarTaskMaterialLiaocangTaizhang.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.rebarComponent.vo.MaterialVo;
import com.trtm.iot.rebarTaskChecklist.service.IRebarTaskChecklistService;
import com.trtm.iot.yclud.entity.YclUsageDetail;
import com.trtm.iot.yclud.service.IYclUsageDetailService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.rebarTaskMaterialLiaocangTaizhang.entity.RebarTaskMaterialLiaocangTaizhang;
import com.trtm.iot.rebarTaskMaterialLiaocangTaizhang.service.IRebarTaskMaterialLiaocangTaizhangService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: rebar_task_material_liaocang_taizhang
 * @Author: jeecg-boot
 * @Date:   2023-07-17
 * @Version: V1.0
 */
@Api(tags="rebar_task_material_liaocang_taizhang")
@RestController
@RequestMapping("/rebarTaskMaterialLiaocangTaizhang/rebarTaskMaterialLiaocangTaizhang")
@Slf4j
public class RebarTaskMaterialLiaocangTaizhangController extends JeecgController<RebarTaskMaterialLiaocangTaizhang, IRebarTaskMaterialLiaocangTaizhangService> {
	@Autowired
	private IRebarTaskMaterialLiaocangTaizhangService rebarTaskMaterialLiaocangTaizhangService;
	
	/**
	 * 分页列表查询
	 *
	 * @param rebarTaskMaterialLiaocangTaizhang
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "rebar_task_material_liaocang_taizhang-分页列表查询")
	@ApiOperation(value="rebar_task_material_liaocang_taizhang-分页列表查询", notes="rebar_task_material_liaocang_taizhang-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RebarTaskMaterialLiaocangTaizhang rebarTaskMaterialLiaocangTaizhang,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RebarTaskMaterialLiaocangTaizhang> queryWrapper = QueryGenerator.initQueryWrapper(rebarTaskMaterialLiaocangTaizhang, req.getParameterMap());
		Page<RebarTaskMaterialLiaocangTaizhang> page = new Page<RebarTaskMaterialLiaocangTaizhang>(pageNo, pageSize);
		IPage<RebarTaskMaterialLiaocangTaizhang> pageList = rebarTaskMaterialLiaocangTaizhangService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param rebarTaskMaterialLiaocangTaizhang
	 * @return
	 */
	@AutoLog(value = "rebar_task_material_liaocang_taizhang-添加")
	@ApiOperation(value="rebar_task_material_liaocang_taizhang-添加", notes="rebar_task_material_liaocang_taizhang-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RebarTaskMaterialLiaocangTaizhang rebarTaskMaterialLiaocangTaizhang) {
		rebarTaskMaterialLiaocangTaizhangService.save(rebarTaskMaterialLiaocangTaizhang);
		return Result.OK("添加成功！");
	}
	 @Autowired
	 private IRebarTaskChecklistService rebarTaskChecklistService;
	 @Autowired
	 private IYclUsageDetailService yclUsageDetailService;
	 /**
	  *   批量添加任务材料料仓检验批Id
	  *
	  * @param list
	  * @return
	  */
	 @AutoLog(value = "批量添加任务材料料仓检验批Id")
	 @ApiOperation(value="批量添加任务材料料仓检验批Id", notes="领料按钮")
	 @PostMapping(value = "/adds")
	 @Transactional
	 public Result<?> add(@RequestBody List<MaterialVo> list) {
		Boolean save = false;
		String taskId = "";
		 for (MaterialVo materialVo : list) {
			 RebarTaskMaterialLiaocangTaizhang rebarTaskMaterialLiaocangTaizhang = new RebarTaskMaterialLiaocangTaizhang();
			 BeanUtils.copyProperties(materialVo,rebarTaskMaterialLiaocangTaizhang);
			 taskId = materialVo.getTaskId();
//			 save = rebarTaskMaterialLiaocangTaizhangService.save(rebarTaskMaterialLiaocangTaizhang);
			 List<YclUsageDetail> yclUsageDetails = materialVo.getYclUsageDetail();
			 if (yclUsageDetails!=null){
				 String liaocangId = materialVo.getLiaocangId();
				 String orgCodes = materialVo.getOrgCodes();
				 for (YclUsageDetail yclUsageDetail : yclUsageDetails) {
					 yclUsageDetail.setStorageId(liaocangId);
					 yclUsageDetail.setCode(orgCodes);
					 yclUsageDetailService.addRebarCaiLiao(yclUsageDetail);
				 }
				 save = true;
			 }else {
				 return Result.error("未领用材料!");
			 }

		 }
		 if (save){
			 boolean b = rebarTaskChecklistService.updateTaskStatus(taskId);
			 System.out.println("b = " + b);
		 }
		 return Result.OK("添加成功！");
	 }
	
	/**
	 *  编辑
	 *
	 * @param rebarTaskMaterialLiaocangTaizhang
	 * @return
	 */
	@AutoLog(value = "rebar_task_material_liaocang_taizhang-编辑")
	@ApiOperation(value="rebar_task_material_liaocang_taizhang-编辑", notes="rebar_task_material_liaocang_taizhang-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RebarTaskMaterialLiaocangTaizhang rebarTaskMaterialLiaocangTaizhang) {
		rebarTaskMaterialLiaocangTaizhangService.updateById(rebarTaskMaterialLiaocangTaizhang);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_task_material_liaocang_taizhang-通过id删除")
	@ApiOperation(value="rebar_task_material_liaocang_taizhang-通过id删除", notes="rebar_task_material_liaocang_taizhang-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		rebarTaskMaterialLiaocangTaizhangService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "rebar_task_material_liaocang_taizhang-批量删除")
	@ApiOperation(value="rebar_task_material_liaocang_taizhang-批量删除", notes="rebar_task_material_liaocang_taizhang-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.rebarTaskMaterialLiaocangTaizhangService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_task_material_liaocang_taizhang-通过id查询")
	@ApiOperation(value="rebar_task_material_liaocang_taizhang-通过id查询", notes="rebar_task_material_liaocang_taizhang-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RebarTaskMaterialLiaocangTaizhang rebarTaskMaterialLiaocangTaizhang = rebarTaskMaterialLiaocangTaizhangService.getById(id);
		if(rebarTaskMaterialLiaocangTaizhang==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(rebarTaskMaterialLiaocangTaizhang);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param rebarTaskMaterialLiaocangTaizhang
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RebarTaskMaterialLiaocangTaizhang rebarTaskMaterialLiaocangTaizhang) {
        return super.exportXls(request, rebarTaskMaterialLiaocangTaizhang, RebarTaskMaterialLiaocangTaizhang.class, "rebar_task_material_liaocang_taizhang");
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
        return super.importExcel(request, response, RebarTaskMaterialLiaocangTaizhang.class);
    }

}
