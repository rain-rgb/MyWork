package com.trtm.iot.syycjcdj.controller;

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
import com.trtm.iot.syycjcdj.entity.SyDpsYyYuancaijinchangdengji;
import com.trtm.iot.syycjcdj.service.ISyDpsYyYuancaijinchangdengjiService;

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
 * @Description: sy_dps_yy_yuancaijinchangdengji
 * @Author: jeecg-boot
 * @Date:   2023-03-09
 * @Version: V1.0
 */
@Api(tags="sy_dps_yy_yuancaijinchangdengji")
@RestController
@RequestMapping("/syycjcdj/syDpsYyYuancaijinchangdengji")
@Slf4j
public class SyDpsYyYuancaijinchangdengjiController extends JeecgController<SyDpsYyYuancaijinchangdengji, ISyDpsYyYuancaijinchangdengjiService> {
	@Autowired
	private ISyDpsYyYuancaijinchangdengjiService syDpsYyYuancaijinchangdengjiService;
	
	/**
	 * 分页列表查询
	 *
	 * @param syDpsYyYuancaijinchangdengji
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_yuancaijinchangdengji-分页列表查询")
	@ApiOperation(value="sy_dps_yy_yuancaijinchangdengji-分页列表查询", notes="sy_dps_yy_yuancaijinchangdengji-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyDpsYyYuancaijinchangdengji syDpsYyYuancaijinchangdengji,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyDpsYyYuancaijinchangdengji> queryWrapper = QueryGenerator.initQueryWrapper(syDpsYyYuancaijinchangdengji, req.getParameterMap());
		Page<SyDpsYyYuancaijinchangdengji> page = new Page<SyDpsYyYuancaijinchangdengji>(pageNo, pageSize);
		IPage<SyDpsYyYuancaijinchangdengji> pageList = syDpsYyYuancaijinchangdengjiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param syDpsYyYuancaijinchangdengji
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_yuancaijinchangdengji-添加")
	@ApiOperation(value="sy_dps_yy_yuancaijinchangdengji-添加", notes="sy_dps_yy_yuancaijinchangdengji-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyDpsYyYuancaijinchangdengji syDpsYyYuancaijinchangdengji) {
		syDpsYyYuancaijinchangdengjiService.save(syDpsYyYuancaijinchangdengji);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syDpsYyYuancaijinchangdengji
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_yuancaijinchangdengji-编辑")
	@ApiOperation(value="sy_dps_yy_yuancaijinchangdengji-编辑", notes="sy_dps_yy_yuancaijinchangdengji-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyDpsYyYuancaijinchangdengji syDpsYyYuancaijinchangdengji) {
		syDpsYyYuancaijinchangdengjiService.updateById(syDpsYyYuancaijinchangdengji);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_yuancaijinchangdengji-通过id删除")
	@ApiOperation(value="sy_dps_yy_yuancaijinchangdengji-通过id删除", notes="sy_dps_yy_yuancaijinchangdengji-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syDpsYyYuancaijinchangdengjiService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_yuancaijinchangdengji-批量删除")
	@ApiOperation(value="sy_dps_yy_yuancaijinchangdengji-批量删除", notes="sy_dps_yy_yuancaijinchangdengji-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syDpsYyYuancaijinchangdengjiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sy_dps_yy_yuancaijinchangdengji-通过id查询")
	@ApiOperation(value="sy_dps_yy_yuancaijinchangdengji-通过id查询", notes="sy_dps_yy_yuancaijinchangdengji-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyDpsYyYuancaijinchangdengji syDpsYyYuancaijinchangdengji = syDpsYyYuancaijinchangdengjiService.getById(id);
		if(syDpsYyYuancaijinchangdengji==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syDpsYyYuancaijinchangdengji);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syDpsYyYuancaijinchangdengji
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsYyYuancaijinchangdengji syDpsYyYuancaijinchangdengji) {
        return super.exportXls(request, syDpsYyYuancaijinchangdengji, SyDpsYyYuancaijinchangdengji.class, "sy_dps_yy_yuancaijinchangdengji");
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
        return super.importExcel(request, response, SyDpsYyYuancaijinchangdengji.class);
    }

}
