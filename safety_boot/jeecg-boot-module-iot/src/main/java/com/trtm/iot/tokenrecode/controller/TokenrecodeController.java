package com.trtm.iot.tokenrecode.controller;

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
import com.trtm.iot.tokenrecode.entity.Tokenrecode;
import com.trtm.iot.tokenrecode.service.ITokenrecodeService;

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
 * @Description: tokenrecode
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Api(tags="tokenrecode")
@RestController
@RequestMapping("/tokenrecode/tokenrecode")
@Slf4j
public class TokenrecodeController extends JeecgController<Tokenrecode, ITokenrecodeService> {
	@Autowired
	private ITokenrecodeService tokenrecodeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param tokenrecode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "tokenrecode-分页列表查询")
	@ApiOperation(value="tokenrecode-分页列表查询", notes="tokenrecode-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Tokenrecode tokenrecode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Tokenrecode> queryWrapper = QueryGenerator.initQueryWrapper(tokenrecode, req.getParameterMap());
		Page<Tokenrecode> page = new Page<Tokenrecode>(pageNo, pageSize);
		IPage<Tokenrecode> pageList = tokenrecodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param tokenrecode
	 * @return
	 */
	@AutoLog(value = "tokenrecode-添加")
	@ApiOperation(value="tokenrecode-添加", notes="tokenrecode-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Tokenrecode tokenrecode) {
		tokenrecodeService.save(tokenrecode);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param tokenrecode
	 * @return
	 */
	@AutoLog(value = "tokenrecode-编辑")
	@ApiOperation(value="tokenrecode-编辑", notes="tokenrecode-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Tokenrecode tokenrecode) {
		tokenrecodeService.updateById(tokenrecode);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "tokenrecode-通过id删除")
	@ApiOperation(value="tokenrecode-通过id删除", notes="tokenrecode-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tokenrecodeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "tokenrecode-批量删除")
	@ApiOperation(value="tokenrecode-批量删除", notes="tokenrecode-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tokenrecodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "tokenrecode-通过id查询")
	@ApiOperation(value="tokenrecode-通过id查询", notes="tokenrecode-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Tokenrecode tokenrecode = tokenrecodeService.getById(id);
		if(tokenrecode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(tokenrecode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param tokenrecode
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Tokenrecode tokenrecode) {
        return super.exportXls(request, tokenrecode, Tokenrecode.class, "tokenrecode");
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
        return super.importExcel(request, response, Tokenrecode.class);
    }

}
