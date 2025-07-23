package com.trtm.iot.cofferdam.controller;

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
import com.trtm.iot.cofferdam.entity.CofferdamRealdata;
import com.trtm.iot.cofferdam.service.ICofferdamRealdataService;

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
 * @Description: cofferdam_realdata
 * @Author: jeecg-boot
 * @Date:   2024-12-21
 * @Version: V1.0
 */
@Api(tags="cofferdam_realdata")
@RestController
@RequestMapping("/cofferdam/cofferdamRealdata")
@Slf4j
public class CofferdamRealdataController extends JeecgController<CofferdamRealdata, ICofferdamRealdataService> {
	@Autowired
	private ICofferdamRealdataService cofferdamRealdataService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cofferdamRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "cofferdam_realdata-分页列表查询")
	@ApiOperation(value="cofferdam_realdata-分页列表查询", notes="cofferdam_realdata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CofferdamRealdata cofferdamRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CofferdamRealdata> queryWrapper = QueryGenerator.initQueryWrapper(cofferdamRealdata, req.getParameterMap());
		Page<CofferdamRealdata> page = new Page<CofferdamRealdata>(pageNo, pageSize);
		IPage<CofferdamRealdata> pageList = cofferdamRealdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cofferdamRealdata
	 * @return
	 */
	@AutoLog(value = "cofferdam_realdata-添加")
	@ApiOperation(value="cofferdam_realdata-添加", notes="cofferdam_realdata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CofferdamRealdata cofferdamRealdata) {
		cofferdamRealdataService.save(cofferdamRealdata);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cofferdamRealdata
	 * @return
	 */
	@AutoLog(value = "cofferdam_realdata-编辑")
	@ApiOperation(value="cofferdam_realdata-编辑", notes="cofferdam_realdata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CofferdamRealdata cofferdamRealdata) {
		cofferdamRealdataService.updateById(cofferdamRealdata);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "cofferdam_realdata-通过id删除")
	@ApiOperation(value="cofferdam_realdata-通过id删除", notes="cofferdam_realdata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		cofferdamRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "cofferdam_realdata-批量删除")
	@ApiOperation(value="cofferdam_realdata-批量删除", notes="cofferdam_realdata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cofferdamRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "cofferdam_realdata-通过id查询")
	@ApiOperation(value="cofferdam_realdata-通过id查询", notes="cofferdam_realdata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CofferdamRealdata cofferdamRealdata = cofferdamRealdataService.getById(id);
		if(cofferdamRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cofferdamRealdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cofferdamRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CofferdamRealdata cofferdamRealdata) {
        return super.exportXls(request, cofferdamRealdata, CofferdamRealdata.class, "cofferdam_realdata");
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
        return super.importExcel(request, response, CofferdamRealdata.class);
    }

}
