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
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkBaseDetail;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkBaseDetailService;

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
 * @Description: anquan_fxfjgk_base_detail
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Api(tags="anquan_fxfjgk_base_detail")
@RestController
@RequestMapping("/anquanfxgk/anquanFxfjgkBaseDetail")
@Slf4j
public class AnquanFxfjgkBaseDetailController extends JeecgController<AnquanFxfjgkBaseDetail, IAnquanFxfjgkBaseDetailService> {
	@Autowired
	private IAnquanFxfjgkBaseDetailService anquanFxfjgkBaseDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param anquanFxfjgkBaseDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base_detail-分页列表查询")
	@ApiOperation(value="anquan_fxfjgk_base_detail-分页列表查询", notes="anquan_fxfjgk_base_detail-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AnquanFxfjgkBaseDetail anquanFxfjgkBaseDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		QueryWrapper<AnquanFxfjgkBaseDetail> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxfjgkBaseDetail, req.getParameterMap());
		Page<AnquanFxfjgkBaseDetail> page = new Page<AnquanFxfjgkBaseDetail>(pageNo, pageSize);
		IPage<AnquanFxfjgkBaseDetail> pageList = anquanFxfjgkBaseDetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param anquanFxfjgkBaseDetail
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base_detail-添加")
	@ApiOperation(value="anquan_fxfjgk_base_detail-添加", notes="anquan_fxfjgk_base_detail-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AnquanFxfjgkBaseDetail anquanFxfjgkBaseDetail) {
		anquanFxfjgkBaseDetailService.save(anquanFxfjgkBaseDetail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param anquanFxfjgkBaseDetail
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base_detail-编辑")
	@ApiOperation(value="anquan_fxfjgk_base_detail-编辑", notes="anquan_fxfjgk_base_detail-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AnquanFxfjgkBaseDetail anquanFxfjgkBaseDetail) {
		anquanFxfjgkBaseDetailService.updateById(anquanFxfjgkBaseDetail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base_detail-通过id删除")
	@ApiOperation(value="anquan_fxfjgk_base_detail-通过id删除", notes="anquan_fxfjgk_base_detail-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		anquanFxfjgkBaseDetailService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base_detail-批量删除")
	@ApiOperation(value="anquan_fxfjgk_base_detail-批量删除", notes="anquan_fxfjgk_base_detail-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.anquanFxfjgkBaseDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base_detail-通过id查询")
	@ApiOperation(value="anquan_fxfjgk_base_detail-通过id查询", notes="anquan_fxfjgk_base_detail-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AnquanFxfjgkBaseDetail anquanFxfjgkBaseDetail = anquanFxfjgkBaseDetailService.getById(id);
		if(anquanFxfjgkBaseDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(anquanFxfjgkBaseDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param anquanFxfjgkBaseDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AnquanFxfjgkBaseDetail anquanFxfjgkBaseDetail) {
        return super.exportXls(request, anquanFxfjgkBaseDetail, AnquanFxfjgkBaseDetail.class, "anquan_fxfjgk_base_detail");
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
        return super.importExcel(request, response, AnquanFxfjgkBaseDetail.class);
    }

}
