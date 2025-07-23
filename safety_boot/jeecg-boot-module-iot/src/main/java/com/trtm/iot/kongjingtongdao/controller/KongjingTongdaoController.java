package com.trtm.iot.kongjingtongdao.controller;

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
import com.trtm.iot.kongjingtongdao.entity.KongjingTongdao;
import com.trtm.iot.kongjingtongdao.service.IKongjingTongdaoService;

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
 * @Description: 通道参数信息表
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
@Api(tags="通道参数信息表")
@RestController
@RequestMapping("/kongjingtongdao/kongjingTongdao")
@Slf4j
public class KongjingTongdaoController extends JeecgController<KongjingTongdao, IKongjingTongdaoService> {
	@Autowired
	private IKongjingTongdaoService kongjingTongdaoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param kongjingTongdao
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "通道参数信息表-分页列表查询")
	@ApiOperation(value="通道参数信息表-分页列表查询", notes="通道参数信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(KongjingTongdao kongjingTongdao,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<KongjingTongdao> queryWrapper = QueryGenerator.initQueryWrapper(kongjingTongdao, req.getParameterMap());
		Page<KongjingTongdao> page = new Page<KongjingTongdao>(pageNo, pageSize);
		IPage<KongjingTongdao> pageList = kongjingTongdaoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param kongjingTongdao
	 * @return
	 */
	@AutoLog(value = "通道参数信息表-添加")
	@ApiOperation(value="通道参数信息表-添加", notes="通道参数信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody KongjingTongdao kongjingTongdao) {
		kongjingTongdaoService.save(kongjingTongdao);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param kongjingTongdao
	 * @return
	 */
	@AutoLog(value = "通道参数信息表-编辑")
	@ApiOperation(value="通道参数信息表-编辑", notes="通道参数信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody KongjingTongdao kongjingTongdao) {
		kongjingTongdaoService.updateById(kongjingTongdao);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "通道参数信息表-通过id删除")
	@ApiOperation(value="通道参数信息表-通过id删除", notes="通道参数信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		kongjingTongdaoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "通道参数信息表-批量删除")
	@ApiOperation(value="通道参数信息表-批量删除", notes="通道参数信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kongjingTongdaoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "通道参数信息表-通过id查询")
	@ApiOperation(value="通道参数信息表-通过id查询", notes="通道参数信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		KongjingTongdao kongjingTongdao = kongjingTongdaoService.getById(id);
		if(kongjingTongdao==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kongjingTongdao);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param kongjingTongdao
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, KongjingTongdao kongjingTongdao) {
        return super.exportXls(request, kongjingTongdao, KongjingTongdao.class, "通道参数信息表");
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
        return super.importExcel(request, response, KongjingTongdao.class);
    }

}
