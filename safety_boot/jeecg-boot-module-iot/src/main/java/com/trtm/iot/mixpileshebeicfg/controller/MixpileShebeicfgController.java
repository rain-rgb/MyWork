package com.trtm.iot.mixpileshebeicfg.controller;

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
import com.trtm.iot.mixpileshebeicfg.entity.MixpileShebeicfg;
import com.trtm.iot.mixpileshebeicfg.service.IMixpileShebeicfgService;

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
 * @Description: 搅拌设备参数配置表
 * @Author: jeecg-boot
 * @Date:   2024-01-24
 * @Version: V1.0
 */
@Api(tags="搅拌设备参数配置表")
@RestController
@RequestMapping("/mixpileshebeicfg/mixpileShebeicfg")
@Slf4j
public class MixpileShebeicfgController extends JeecgController<MixpileShebeicfg, IMixpileShebeicfgService> {
	@Autowired
	private IMixpileShebeicfgService mixpileShebeicfgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param mixpileShebeicfg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "搅拌设备参数配置表-分页列表查询")
	@ApiOperation(value="搅拌设备参数配置表-分页列表查询", notes="搅拌设备参数配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MixpileShebeicfg mixpileShebeicfg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MixpileShebeicfg> queryWrapper = QueryGenerator.initQueryWrapper(mixpileShebeicfg, req.getParameterMap());
		Page<MixpileShebeicfg> page = new Page<MixpileShebeicfg>(pageNo, pageSize);
		IPage<MixpileShebeicfg> pageList = mixpileShebeicfgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param mixpileShebeicfg
	 * @return
	 */
	@AutoLog(value = "搅拌设备参数配置表-添加")
	@ApiOperation(value="搅拌设备参数配置表-添加", notes="搅拌设备参数配置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MixpileShebeicfg mixpileShebeicfg) {
		mixpileShebeicfgService.save(mixpileShebeicfg);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param mixpileShebeicfg
	 * @return
	 */
	@AutoLog(value = "搅拌设备参数配置表-编辑")
	@ApiOperation(value="搅拌设备参数配置表-编辑", notes="搅拌设备参数配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MixpileShebeicfg mixpileShebeicfg) {
		mixpileShebeicfgService.updateById(mixpileShebeicfg);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "搅拌设备参数配置表-通过id删除")
	@ApiOperation(value="搅拌设备参数配置表-通过id删除", notes="搅拌设备参数配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		mixpileShebeicfgService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "搅拌设备参数配置表-批量删除")
	@ApiOperation(value="搅拌设备参数配置表-批量删除", notes="搅拌设备参数配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.mixpileShebeicfgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "搅拌设备参数配置表-通过id查询")
	@ApiOperation(value="搅拌设备参数配置表-通过id查询", notes="搅拌设备参数配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MixpileShebeicfg mixpileShebeicfg = mixpileShebeicfgService.getById(id);
		if(mixpileShebeicfg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(mixpileShebeicfg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param mixpileShebeicfg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MixpileShebeicfg mixpileShebeicfg) {
        return super.exportXls(request, mixpileShebeicfg, MixpileShebeicfg.class, "搅拌设备参数配置表");
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
        return super.importExcel(request, response, MixpileShebeicfg.class);
    }

}
