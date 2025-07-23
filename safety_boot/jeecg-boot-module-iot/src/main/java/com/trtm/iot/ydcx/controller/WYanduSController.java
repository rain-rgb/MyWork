package com.trtm.iot.ydcx.controller;

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
import com.trtm.iot.ydcx.entity.WYanduS;
import com.trtm.iot.ydcx.service.IWYanduSService;

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
 * @Description: w_yandu_s
 * @Author: jeecg-boot
 * @Date:   2022-12-09
 * @Version: V1.0
 */
@Api(tags="w_yandu_s")
@RestController
@RequestMapping("/ydcx/wYanduS")
@Slf4j
public class WYanduSController extends JeecgController<WYanduS, IWYanduSService> {
	@Autowired
	private IWYanduSService wYanduSService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wYanduS
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "w_yandu_s-分页列表查询")
	@ApiOperation(value="w_yandu_s-分页列表查询", notes="w_yandu_s-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WYanduS wYanduS,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WYanduS> queryWrapper = QueryGenerator.initQueryWrapper(wYanduS, req.getParameterMap());
		Page<WYanduS> page = new Page<WYanduS>(pageNo, pageSize);
		IPage<WYanduS> pageList = wYanduSService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wYanduS
	 * @return
	 */
	@AutoLog(value = "w_yandu_s-添加")
	@ApiOperation(value="w_yandu_s-添加", notes="w_yandu_s-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WYanduS wYanduS) {
		wYanduSService.save(wYanduS);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wYanduS
	 * @return
	 */
	@AutoLog(value = "w_yandu_s-编辑")
	@ApiOperation(value="w_yandu_s-编辑", notes="w_yandu_s-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WYanduS wYanduS) {
		wYanduSService.updateById(wYanduS);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "w_yandu_s-通过id删除")
	@ApiOperation(value="w_yandu_s-通过id删除", notes="w_yandu_s-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wYanduSService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "w_yandu_s-批量删除")
	@ApiOperation(value="w_yandu_s-批量删除", notes="w_yandu_s-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wYanduSService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "w_yandu_s-通过id查询")
	@ApiOperation(value="w_yandu_s-通过id查询", notes="w_yandu_s-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WYanduS wYanduS = wYanduSService.getById(id);
		if(wYanduS==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wYanduS);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wYanduS
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WYanduS wYanduS) {
        return super.exportXls(request, wYanduS, WYanduS.class, "w_yandu_s");
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
        return super.importExcel(request, response, WYanduS.class);
    }

}
