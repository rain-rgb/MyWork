package com.trtm.iot.wzdepart.controller;

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
import com.trtm.iot.wzdepart.entity.Wzdepart;
import com.trtm.iot.wzdepart.service.IWzdepartService;

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
 * @Description: wzdepart
 * @Author: jeecg-boot
 * @Date:   2023-10-20
 * @Version: V1.0
 */
@Api(tags="wzdepart")
@RestController
@RequestMapping("/wzdepart/wzdepart")
@Slf4j
public class WzdepartController extends JeecgController<Wzdepart, IWzdepartService> {
	@Autowired
	private IWzdepartService wzdepartService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wzdepart
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wzdepart-分页列表查询")
	@ApiOperation(value="wzdepart-分页列表查询", notes="wzdepart-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Wzdepart wzdepart,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Wzdepart> queryWrapper = QueryGenerator.initQueryWrapper(wzdepart, req.getParameterMap());
		Page<Wzdepart> page = new Page<Wzdepart>(pageNo, pageSize);
		IPage<Wzdepart> pageList = wzdepartService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wzdepart
	 * @return
	 */
	@AutoLog(value = "wzdepart-添加")
	@ApiOperation(value="wzdepart-添加", notes="wzdepart-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Wzdepart wzdepart) {
		wzdepartService.save(wzdepart);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wzdepart
	 * @return
	 */
	@AutoLog(value = "wzdepart-编辑")
	@ApiOperation(value="wzdepart-编辑", notes="wzdepart-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Wzdepart wzdepart) {
		wzdepartService.updateById(wzdepart);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzdepart-通过id删除")
	@ApiOperation(value="wzdepart-通过id删除", notes="wzdepart-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzdepartService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wzdepart-批量删除")
	@ApiOperation(value="wzdepart-批量删除", notes="wzdepart-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzdepartService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzdepart-通过id查询")
	@ApiOperation(value="wzdepart-通过id查询", notes="wzdepart-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Wzdepart wzdepart = wzdepartService.getById(id);
		if(wzdepart==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzdepart);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzdepart
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzdepart wzdepart) {
        return super.exportXls(request, wzdepart, Wzdepart.class, "wzdepart");
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
        return super.importExcel(request, response, Wzdepart.class);
    }

}
