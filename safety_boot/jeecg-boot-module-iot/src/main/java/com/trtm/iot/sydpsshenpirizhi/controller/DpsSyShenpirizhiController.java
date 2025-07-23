package com.trtm.iot.sydpsshenpirizhi.controller;

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
import com.trtm.iot.sydpsshenpirizhi.entity.DpsSyShenpirizhi;
import com.trtm.iot.sydpsshenpirizhi.service.IDpsSyShenpirizhiService;

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
 * @Description: dps_sy_shenpirizhi
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Api(tags="dps_sy_shenpirizhi")
@RestController
@RequestMapping("/sydpsshenpirizhi/dpsSyShenpirizhi")
@Slf4j
public class DpsSyShenpirizhiController extends JeecgController<DpsSyShenpirizhi, IDpsSyShenpirizhiService> {
	@Autowired
	private IDpsSyShenpirizhiService dpsSyShenpirizhiService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dpsSyShenpirizhi
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "dps_sy_shenpirizhi-分页列表查询")
	@ApiOperation(value="dps_sy_shenpirizhi-分页列表查询", notes="dps_sy_shenpirizhi-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DpsSyShenpirizhi dpsSyShenpirizhi,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DpsSyShenpirizhi> queryWrapper = QueryGenerator.initQueryWrapper(dpsSyShenpirizhi, req.getParameterMap());
		Page<DpsSyShenpirizhi> page = new Page<DpsSyShenpirizhi>(pageNo, pageSize);
		IPage<DpsSyShenpirizhi> pageList = dpsSyShenpirizhiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param dpsSyShenpirizhi
	 * @return
	 */
	@AutoLog(value = "dps_sy_shenpirizhi-添加")
	@ApiOperation(value="dps_sy_shenpirizhi-添加", notes="dps_sy_shenpirizhi-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DpsSyShenpirizhi dpsSyShenpirizhi) {
		dpsSyShenpirizhiService.save(dpsSyShenpirizhi);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param dpsSyShenpirizhi
	 * @return
	 */
	@AutoLog(value = "dps_sy_shenpirizhi-编辑")
	@ApiOperation(value="dps_sy_shenpirizhi-编辑", notes="dps_sy_shenpirizhi-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DpsSyShenpirizhi dpsSyShenpirizhi) {
		dpsSyShenpirizhiService.updateById(dpsSyShenpirizhi);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "dps_sy_shenpirizhi-通过id删除")
	@ApiOperation(value="dps_sy_shenpirizhi-通过id删除", notes="dps_sy_shenpirizhi-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dpsSyShenpirizhiService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "dps_sy_shenpirizhi-批量删除")
	@ApiOperation(value="dps_sy_shenpirizhi-批量删除", notes="dps_sy_shenpirizhi-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dpsSyShenpirizhiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "dps_sy_shenpirizhi-通过id查询")
	@ApiOperation(value="dps_sy_shenpirizhi-通过id查询", notes="dps_sy_shenpirizhi-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DpsSyShenpirizhi dpsSyShenpirizhi = dpsSyShenpirizhiService.getById(id);
		if(dpsSyShenpirizhi==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(dpsSyShenpirizhi);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dpsSyShenpirizhi
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DpsSyShenpirizhi dpsSyShenpirizhi) {
        return super.exportXls(request, dpsSyShenpirizhi, DpsSyShenpirizhi.class, "dps_sy_shenpirizhi");
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
        return super.importExcel(request, response, DpsSyShenpirizhi.class);
    }

}
