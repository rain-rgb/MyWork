package com.trtm.iot.hc_result_collect.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hc_result_collect.entity.HcResultCollect;
import com.trtm.iot.hc_result_collect.service.IHcResultCollectService;

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
 * @Description: 摊铺里程分布
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Api(tags="摊铺里程分布")
@RestController
@RequestMapping("/hc_result_collect/hcResultCollect")
@Slf4j
public class HcResultCollectController extends JeecgController<HcResultCollect, IHcResultCollectService> {
	@Autowired
	private IHcResultCollectService hcResultCollectService;
	
	/**
	 * 分页列表查询
	 *
	 * @param hcResultCollect
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "摊铺里程分布-分页列表查询")
	@ApiOperation(value="摊铺里程分布-分页列表查询", notes="摊铺里程分布-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcResultCollect hcResultCollect,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HcResultCollect> queryWrapper = QueryGenerator.initQueryWrapper(hcResultCollect, req.getParameterMap());
		Page<HcResultCollect> page = new Page<HcResultCollect>(pageNo, pageSize);
		IPage<HcResultCollect> pageList = hcResultCollectService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param hcResultCollect
	 * @return
	 */
	@AutoLog(value = "摊铺里程分布-添加")
	@ApiOperation(value="摊铺里程分布-添加", notes="摊铺里程分布-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcResultCollect hcResultCollect) {
		hcResultCollectService.save(hcResultCollect);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param hcResultCollect
	 * @return
	 */
	@AutoLog(value = "摊铺里程分布-编辑")
	@ApiOperation(value="摊铺里程分布-编辑", notes="摊铺里程分布-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcResultCollect hcResultCollect) {
		hcResultCollectService.updateById(hcResultCollect);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "摊铺里程分布-通过id删除")
	@ApiOperation(value="摊铺里程分布-通过id删除", notes="摊铺里程分布-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcResultCollectService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "摊铺里程分布-批量删除")
	@ApiOperation(value="摊铺里程分布-批量删除", notes="摊铺里程分布-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcResultCollectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "摊铺里程分布-通过id查询")
	@ApiOperation(value="摊铺里程分布-通过id查询", notes="摊铺里程分布-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcResultCollect hcResultCollect = hcResultCollectService.getById(id);
		if(hcResultCollect==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcResultCollect);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcResultCollect
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcResultCollect hcResultCollect) {
        return super.exportXls(request, hcResultCollect, HcResultCollect.class, "摊铺里程分布");
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
        return super.importExcel(request, response, HcResultCollect.class);
    }

}
