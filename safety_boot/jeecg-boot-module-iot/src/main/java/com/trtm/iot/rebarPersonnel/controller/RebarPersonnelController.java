package com.trtm.iot.rebarPersonnel.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.rebarComponent.vo.MaterialVo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.rebarPersonnel.entity.RebarPersonnel;
import com.trtm.iot.rebarPersonnel.service.IRebarPersonnelService;

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
 * @Description: rebar_personnel
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
@Api(tags="rebar_personnel")
@RestController
@RequestMapping("/rebarPersonnel/rebarPersonnel")
@Slf4j
public class RebarPersonnelController extends JeecgController<RebarPersonnel, IRebarPersonnelService> {
	@Autowired
	private IRebarPersonnelService rebarPersonnelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param rebarPersonnel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "rebar_personnel-分页列表查询")
	@ApiOperation(value="rebar_personnel-分页列表查询", notes="rebar_personnel-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RebarPersonnel rebarPersonnel,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RebarPersonnel> queryWrapper = QueryGenerator.initQueryWrapper(rebarPersonnel, req.getParameterMap());
		Page<RebarPersonnel> page = new Page<RebarPersonnel>(pageNo, pageSize);
		IPage<RebarPersonnel> pageList = rebarPersonnelService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	  * 根据加工厂查询班组长
	  *
	  * @param rebarPersonnel
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "根据加工厂查询班组长")
	 @ApiOperation(value="根据加工厂查询班组长", notes="根据加工厂查询班组长询")
	 @GetMapping(value = "/getLeadTeamByFactory")
	 public Result<?> getLeadTeamByFactory(RebarPersonnel rebarPersonnel,
									String factoryId,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 List<RebarPersonnel> rebarPersonnelList = rebarPersonnelService.getLeadTeamByFactory(rebarPersonnel,factoryId,pageNo,pageSize,req);
		 Page<Object> page = new Page<>(pageNo, pageSize);
		 Page<RebarPersonnel> result =
				 new Page<>(pageNo, pageSize, page.getTotal());
		 result.setTotal(rebarPersonnelList.size());
		 result.setRecords(rebarPersonnelList);
		 return Result.OK(result);
	 }
	/**
	 *   添加
	 *
	 * @param rebarPersonnel
	 * @return
	 */
	@AutoLog(value = "rebar_personnel-添加")
	@ApiOperation(value="rebar_personnel-添加", notes="rebar_personnel-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RebarPersonnel rebarPersonnel) {
		rebarPersonnelService.save(rebarPersonnel);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param rebarPersonnel
	 * @return
	 */
	@AutoLog(value = "rebar_personnel-编辑")
	@ApiOperation(value="rebar_personnel-编辑", notes="rebar_personnel-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RebarPersonnel rebarPersonnel) {
		rebarPersonnelService.updateById(rebarPersonnel);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_personnel-通过id删除")
	@ApiOperation(value="rebar_personnel-通过id删除", notes="rebar_personnel-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		rebarPersonnelService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "rebar_personnel-批量删除")
	@ApiOperation(value="rebar_personnel-批量删除", notes="rebar_personnel-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.rebarPersonnelService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_personnel-通过id查询")
	@ApiOperation(value="rebar_personnel-通过id查询", notes="rebar_personnel-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RebarPersonnel rebarPersonnel = rebarPersonnelService.getById(id);
		if(rebarPersonnel==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(rebarPersonnel);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param rebarPersonnel
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RebarPersonnel rebarPersonnel) {
        return super.exportXls(request, rebarPersonnel, RebarPersonnel.class, "rebar_personnel");
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
        return super.importExcel(request, response, RebarPersonnel.class);
    }

}
