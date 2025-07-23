package com.trtm.iot.ztbhzkhdf.controller;

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
import com.trtm.iot.ztbhzkhdf.entity.ZtBhzkhdf;
import com.trtm.iot.ztbhzkhdf.service.IZtBhzkhdfService;

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
 * @Description: 混凝土配送考核得分表
 * @Author: jeecg-boot
 * @Date:   2023-11-02
 * @Version: V1.0
 */
@Api(tags="混凝土配送考核得分表")
@RestController
@RequestMapping("/ztbhzkhdf/ztBhzkhdf")
@Slf4j
public class ZtBhzkhdfController extends JeecgController<ZtBhzkhdf, IZtBhzkhdfService> {
	@Autowired
	private IZtBhzkhdfService ztBhzkhdfService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ztBhzkhdf
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "混凝土配送考核得分表-分页列表查询")
	@ApiOperation(value="混凝土配送考核得分表-分页列表查询", notes="混凝土配送考核得分表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZtBhzkhdf ztBhzkhdf,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZtBhzkhdf> queryWrapper = QueryGenerator.initQueryWrapper(ztBhzkhdf, req.getParameterMap());
		Page<ZtBhzkhdf> page = new Page<ZtBhzkhdf>(pageNo, pageSize);
		IPage<ZtBhzkhdf> pageList = ztBhzkhdfService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ztBhzkhdf
	 * @return
	 */
	@AutoLog(value = "混凝土配送考核得分表-添加")
	@ApiOperation(value="混凝土配送考核得分表-添加", notes="混凝土配送考核得分表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZtBhzkhdf ztBhzkhdf) {
		ztBhzkhdfService.save(ztBhzkhdf);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ztBhzkhdf
	 * @return
	 */
	@AutoLog(value = "混凝土配送考核得分表-编辑")
	@ApiOperation(value="混凝土配送考核得分表-编辑", notes="混凝土配送考核得分表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZtBhzkhdf ztBhzkhdf) {
		ztBhzkhdfService.updateById(ztBhzkhdf);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土配送考核得分表-通过id删除")
	@ApiOperation(value="混凝土配送考核得分表-通过id删除", notes="混凝土配送考核得分表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ztBhzkhdfService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "混凝土配送考核得分表-批量删除")
	@ApiOperation(value="混凝土配送考核得分表-批量删除", notes="混凝土配送考核得分表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ztBhzkhdfService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土配送考核得分表-通过id查询")
	@ApiOperation(value="混凝土配送考核得分表-通过id查询", notes="混凝土配送考核得分表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZtBhzkhdf ztBhzkhdf = ztBhzkhdfService.getById(id);
		if(ztBhzkhdf==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ztBhzkhdf);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ztBhzkhdf
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZtBhzkhdf ztBhzkhdf) {
        return super.exportXls(request, ztBhzkhdf, ZtBhzkhdf.class, "混凝土配送考核得分表");
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
        return super.importExcel(request, response, ZtBhzkhdf.class);
    }

}
