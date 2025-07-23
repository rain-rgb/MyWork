package com.trtm.iot.wztaizhangstatic.controller;

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
import com.trtm.iot.wztaizhangstatic.entity.WztaizhangStatic;
import com.trtm.iot.wztaizhangstatic.service.IWztaizhangStaticService;

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
 * @Description: wztaizhang_static
 * @Author: jeecg-boot
 * @Date:   2024-05-16
 * @Version: V1.0
 */
@Api(tags="wztaizhang_static")
@RestController
@RequestMapping("/wztaizhangstatic/wztaizhangStatic")
@Slf4j
public class WztaizhangStaticController extends JeecgController<WztaizhangStatic, IWztaizhangStaticService> {
	@Autowired
	private IWztaizhangStaticService wztaizhangStaticService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wztaizhangStatic
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wztaizhang_static-分页列表查询")
	@ApiOperation(value="wztaizhang_static-分页列表查询", notes="wztaizhang_static-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WztaizhangStatic wztaizhangStatic,String sys_depart_orgcode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		if (sys_depart_orgcode != null){
			wztaizhangStatic.setSysOrgCode(sys_depart_orgcode +"*");
		}
		QueryWrapper<WztaizhangStatic> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhangStatic, req.getParameterMap());
		Page<WztaizhangStatic> page = new Page<WztaizhangStatic>(pageNo, pageSize);
		IPage<WztaizhangStatic> pageList = wztaizhangStaticService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wztaizhangStatic
	 * @return
	 */
	@AutoLog(value = "wztaizhang_static-添加")
	@ApiOperation(value="wztaizhang_static-添加", notes="wztaizhang_static-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WztaizhangStatic wztaizhangStatic) {
		wztaizhangStaticService.save(wztaizhangStatic);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wztaizhangStatic
	 * @return
	 */
	@AutoLog(value = "wztaizhang_static-编辑")
	@ApiOperation(value="wztaizhang_static-编辑", notes="wztaizhang_static-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WztaizhangStatic wztaizhangStatic) {
		wztaizhangStaticService.updateById(wztaizhangStatic);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wztaizhang_static-通过id删除")
	@ApiOperation(value="wztaizhang_static-通过id删除", notes="wztaizhang_static-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wztaizhangStaticService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wztaizhang_static-批量删除")
	@ApiOperation(value="wztaizhang_static-批量删除", notes="wztaizhang_static-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wztaizhangStaticService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wztaizhang_static-通过id查询")
	@ApiOperation(value="wztaizhang_static-通过id查询", notes="wztaizhang_static-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WztaizhangStatic wztaizhangStatic = wztaizhangStaticService.getById(id);
		if(wztaizhangStatic==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wztaizhangStatic);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wztaizhangStatic
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WztaizhangStatic wztaizhangStatic) {
        return super.exportXls(request, wztaizhangStatic, WztaizhangStatic.class, "wztaizhang_static");
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
        return super.importExcel(request, response, WztaizhangStatic.class);
    }

}
