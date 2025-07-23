package com.trtm.iot.anquanfxgk.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.anquanfxgk.entity.AnquanFxaqjcHandle;
import com.trtm.iot.anquanfxgk.service.IAnquanFxaqjcHandleService;

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
 * @Description: anquan_fxaqjc_handle
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Api(tags="anquan_fxaqjc_handle")
@RestController
@RequestMapping("/anquanfxgk/anquanFxaqjcHandle")
@Slf4j
public class AnquanFxaqjcHandleController extends JeecgController<AnquanFxaqjcHandle, IAnquanFxaqjcHandleService> {
	@Autowired
	private IAnquanFxaqjcHandleService anquanFxaqjcHandleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param anquanFxaqjcHandle
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_handle-分页列表查询")
	@ApiOperation(value="anquan_fxaqjc_handle-分页列表查询", notes="anquan_fxaqjc_handle-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AnquanFxaqjcHandle anquanFxaqjcHandle,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		QueryWrapper<AnquanFxaqjcHandle> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxaqjcHandle, req.getParameterMap());
		Page<AnquanFxaqjcHandle> page = new Page<AnquanFxaqjcHandle>(pageNo, pageSize);
		IPage<AnquanFxaqjcHandle> pageList = anquanFxaqjcHandleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param anquanFxaqjcHandle
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_handle-添加")
	@ApiOperation(value="anquan_fxaqjc_handle-添加", notes="anquan_fxaqjc_handle-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AnquanFxaqjcHandle anquanFxaqjcHandle) {
		anquanFxaqjcHandleService.save(anquanFxaqjcHandle);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param anquanFxaqjcHandle
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_handle-编辑")
	@ApiOperation(value="anquan_fxaqjc_handle-编辑", notes="anquan_fxaqjc_handle-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AnquanFxaqjcHandle anquanFxaqjcHandle) {
		anquanFxaqjcHandleService.updateById(anquanFxaqjcHandle);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_handle-通过id删除")
	@ApiOperation(value="anquan_fxaqjc_handle-通过id删除", notes="anquan_fxaqjc_handle-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		anquanFxaqjcHandleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_handle-批量删除")
	@ApiOperation(value="anquan_fxaqjc_handle-批量删除", notes="anquan_fxaqjc_handle-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.anquanFxaqjcHandleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_handle-通过id查询")
	@ApiOperation(value="anquan_fxaqjc_handle-通过id查询", notes="anquan_fxaqjc_handle-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AnquanFxaqjcHandle anquanFxaqjcHandle = anquanFxaqjcHandleService.getById(id);
		if(anquanFxaqjcHandle==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(anquanFxaqjcHandle);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param anquanFxaqjcHandle
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AnquanFxaqjcHandle anquanFxaqjcHandle) {
        return super.exportXls(request, anquanFxaqjcHandle, AnquanFxaqjcHandle.class, "anquan_fxaqjc_handle");
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
        return super.importExcel(request, response, AnquanFxaqjcHandle.class);
    }

}
