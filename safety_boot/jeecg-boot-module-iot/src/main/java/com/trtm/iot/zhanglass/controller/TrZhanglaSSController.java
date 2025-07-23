package com.trtm.iot.zhanglass.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.zhanglass.vo.TrZhanglaSSList;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import com.trtm.iot.zhanglass.service.ITrZhanglaSSService;

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
 * @Description: 张拉过程子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
@Api(tags="张拉过程子表")
@RestController
@RequestMapping("/zhanglass/trZhanglaSS")
@Slf4j
public class TrZhanglaSSController extends JeecgController<TrZhanglaSS, ITrZhanglaSSService> {
	@Autowired
	private ITrZhanglaSSService trZhanglaSSService;

	/**
	 * 分页列表查询
	 *
	 * @param trZhanglaSS
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "张拉过程子表-分页列表查询")
	@ApiOperation(value="张拉过程子表-分页列表查询", notes="张拉过程子表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrZhanglaSS trZhanglaSS,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrZhanglaSS> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaSS, req.getParameterMap());
		Page<TrZhanglaSS> page = new Page<TrZhanglaSS>(pageNo, pageSize);
		IPage<TrZhanglaSS> pageList = trZhanglaSSService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param trZhanglaSS
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "张拉过程子表-分页列表查询")
	 @ApiOperation(value="张拉过程子表-分页列表查询", notes="张拉过程子表-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(TrZhanglaSS trZhanglaSS,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
	 	if(trZhanglaSS.getShebeibianhao()!=null){
			trZhanglaSS.setShebeibianhao(trZhanglaSS.getShebeibianhao());
		}
	 	if(StringUtils.isNotBlank(trZhanglaSS.getHoleid())){
			trZhanglaSS.setHoleid(trZhanglaSS.getHoleid());
		}else{
			return Result.error("厂家未上传孔道号数据");
		}
		 QueryWrapper<TrZhanglaSS> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaSS, req.getParameterMap());
		 queryWrapper.orderByAsc("jlsj");
		 Page<TrZhanglaSS> page = new Page<TrZhanglaSS>(pageNo, pageSize);
		 IPage<TrZhanglaSS> pageList = trZhanglaSSService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param trZhanglaSS
	 * @return
	 */
	@AutoLog(value = "张拉过程子表-添加")
	@ApiOperation(value="张拉过程子表-添加", notes="张拉过程子表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrZhanglaSS trZhanglaSS) {
		QueryWrapper<TrZhanglaSS> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ssid",trZhanglaSS.getSsid());
		TrZhanglaSS one = trZhanglaSSService.getOne(queryWrapper);
		if (one != null){
			trZhanglaSS.setId(one.getId());
			trZhanglaSSService.updateById(trZhanglaSS);
		}else {
			trZhanglaSSService.save(trZhanglaSS);
		}
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加(对外开放)
	  *
	  * @param trZhanglaSSList
	  * @return
	  */
	 @AutoLog(value = "张拉过程子表-添加(对外开放)")
	 @ApiOperation(value="张拉过程子表-添加(对外开放)", notes="张拉过程子表-添加(对外开放)")
	 @PostMapping(value = "/addList")
	 public Result<?> addList(@RequestBody TrZhanglaSSList trZhanglaSSList) {
		 trZhanglaSSService.saveBatch(trZhanglaSSList.getTrZhanglaSS());
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param trZhanglaSS
	 * @return
	 */
	@AutoLog(value = "张拉过程子表-编辑")
	@ApiOperation(value="张拉过程子表-编辑", notes="张拉过程子表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrZhanglaSS trZhanglaSS) {
		trZhanglaSSService.updateById(trZhanglaSS);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉过程子表-通过id删除")
	@ApiOperation(value="张拉过程子表-通过id删除", notes="张拉过程子表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trZhanglaSSService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "张拉过程子表-批量删除")
	@ApiOperation(value="张拉过程子表-批量删除", notes="张拉过程子表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trZhanglaSSService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉过程子表-通过id查询")
	@ApiOperation(value="张拉过程子表-通过id查询", notes="张拉过程子表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrZhanglaSS trZhanglaSS = trZhanglaSSService.getById(id);
		if(trZhanglaSS==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trZhanglaSS);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trZhanglaSS
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrZhanglaSS trZhanglaSS) {
        return super.exportXls(request, trZhanglaSS, TrZhanglaSS.class, "张拉过程子表");
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
        return super.importExcel(request, response, TrZhanglaSS.class);
    }

}
