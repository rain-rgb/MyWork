package com.trtm.iot.trliangchanginfo.controller;

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
import com.trtm.iot.trliangchanginfo.entity.TrLiangchangInfo;
import com.trtm.iot.trliangchanginfo.service.ITrLiangchangInfoService;

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
 * @Description: tr_liangchang_info
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Api(tags="tr_liangchang_info")
@RestController
@RequestMapping("/trliangchanginfo/trLiangchangInfo")
@Slf4j
public class TrLiangchangInfoController extends JeecgController<TrLiangchangInfo, ITrLiangchangInfoService> {
	@Autowired
	private ITrLiangchangInfoService trLiangchangInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param trLiangchangInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "tr_liangchang_info-分页列表查询")
	@ApiOperation(value="tr_liangchang_info-分页列表查询", notes="tr_liangchang_info-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrLiangchangInfo trLiangchangInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrLiangchangInfo> queryWrapper = QueryGenerator.initQueryWrapper(trLiangchangInfo, req.getParameterMap());
		Page<TrLiangchangInfo> page = new Page<TrLiangchangInfo>(pageNo, pageSize);
		IPage<TrLiangchangInfo> pageList = trLiangchangInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param trLiangchangInfo
	 * @return
	 */
	@AutoLog(value = "tr_liangchang_info-添加")
	@ApiOperation(value="tr_liangchang_info-添加", notes="tr_liangchang_info-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrLiangchangInfo trLiangchangInfo) {
		trLiangchangInfoService.save(trLiangchangInfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param trLiangchangInfo
	 * @return
	 */
	@AutoLog(value = "tr_liangchang_info-编辑")
	@ApiOperation(value="tr_liangchang_info-编辑", notes="tr_liangchang_info-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrLiangchangInfo trLiangchangInfo) {
		trLiangchangInfoService.updateById(trLiangchangInfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "tr_liangchang_info-通过id删除")
	@ApiOperation(value="tr_liangchang_info-通过id删除", notes="tr_liangchang_info-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trLiangchangInfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "tr_liangchang_info-批量删除")
	@ApiOperation(value="tr_liangchang_info-批量删除", notes="tr_liangchang_info-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trLiangchangInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "tr_liangchang_info-通过id查询")
	@ApiOperation(value="tr_liangchang_info-通过id查询", notes="tr_liangchang_info-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrLiangchangInfo trLiangchangInfo = trLiangchangInfoService.getById(id);
		if(trLiangchangInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trLiangchangInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trLiangchangInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrLiangchangInfo trLiangchangInfo) {
        return super.exportXls(request, trLiangchangInfo, TrLiangchangInfo.class, "tr_liangchang_info");
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
        return super.importExcel(request, response, TrLiangchangInfo.class);
    }

}
