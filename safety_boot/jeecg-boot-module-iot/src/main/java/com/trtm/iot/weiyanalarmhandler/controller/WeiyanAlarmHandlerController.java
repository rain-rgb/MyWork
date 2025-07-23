package com.trtm.iot.weiyanalarmhandler.controller;

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
import com.trtm.iot.weiyanalarmhandler.entity.WeiyanAlarmHandler;
import com.trtm.iot.weiyanalarmhandler.service.IWeiyanAlarmHandlerService;

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
 * @Description: 监控量测报警处置表
 * @Author: jeecg-boot
 * @Date:   2022-07-06
 * @Version: V1.0
 */
@Api(tags="监控量测报警处置表")
@RestController
@RequestMapping("/weiyanalarmhandler/weiyanAlarmHandler")
@Slf4j
public class WeiyanAlarmHandlerController extends JeecgController<WeiyanAlarmHandler, IWeiyanAlarmHandlerService> {
	@Autowired
	private IWeiyanAlarmHandlerService weiyanAlarmHandlerService;
	
	/**
	 * 分页列表查询
	 *
	 * @param weiyanAlarmHandler
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "监控量测报警处置表-分页列表查询")
	@ApiOperation(value="监控量测报警处置表-分页列表查询", notes="监控量测报警处置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WeiyanAlarmHandler weiyanAlarmHandler,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WeiyanAlarmHandler> queryWrapper = QueryGenerator.initQueryWrapper(weiyanAlarmHandler, req.getParameterMap());
		Page<WeiyanAlarmHandler> page = new Page<WeiyanAlarmHandler>(pageNo, pageSize);
		IPage<WeiyanAlarmHandler> pageList = weiyanAlarmHandlerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param weiyanAlarmHandler
	 * @return
	 */
	@AutoLog(value = "监控量测报警处置表-添加")
	@ApiOperation(value="监控量测报警处置表-添加", notes="监控量测报警处置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WeiyanAlarmHandler weiyanAlarmHandler) {
		weiyanAlarmHandlerService.save(weiyanAlarmHandler);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param weiyanAlarmHandler
	 * @return
	 */
	@AutoLog(value = "监控量测报警处置表-编辑")
	@ApiOperation(value="监控量测报警处置表-编辑", notes="监控量测报警处置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WeiyanAlarmHandler weiyanAlarmHandler) {
		weiyanAlarmHandlerService.updateById(weiyanAlarmHandler);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监控量测报警处置表-通过id删除")
	@ApiOperation(value="监控量测报警处置表-通过id删除", notes="监控量测报警处置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		weiyanAlarmHandlerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "监控量测报警处置表-批量删除")
	@ApiOperation(value="监控量测报警处置表-批量删除", notes="监控量测报警处置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.weiyanAlarmHandlerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监控量测报警处置表-通过id查询")
	@ApiOperation(value="监控量测报警处置表-通过id查询", notes="监控量测报警处置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WeiyanAlarmHandler weiyanAlarmHandler = weiyanAlarmHandlerService.getById(id);
		if(weiyanAlarmHandler==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(weiyanAlarmHandler);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param weiyanAlarmHandler
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WeiyanAlarmHandler weiyanAlarmHandler) {
        return super.exportXls(request, weiyanAlarmHandler, WeiyanAlarmHandler.class, "监控量测报警处置表");
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
        return super.importExcel(request, response, WeiyanAlarmHandler.class);
    }

}
