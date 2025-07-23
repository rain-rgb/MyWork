package com.trtm.iot.hc_token.controller;

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
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;

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
 * @Description: token
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Api(tags="token")
@RestController
@RequestMapping("/hc_token/hcToken")
@Slf4j
public class HcTokenController extends JeecgController<HcToken, IHcTokenService> {
	@Autowired
	private IHcTokenService hcTokenService;
	
	/**
	 * 分页列表查询
	 *
	 * @param hcToken
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "token-分页列表查询")
	@ApiOperation(value="token-分页列表查询", notes="token-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcToken hcToken,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HcToken> queryWrapper = QueryGenerator.initQueryWrapper(hcToken, req.getParameterMap());
		Page<HcToken> page = new Page<HcToken>(pageNo, pageSize);
		IPage<HcToken> pageList = hcTokenService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param hcToken
	 * @return
	 */
	@AutoLog(value = "token-添加")
	@ApiOperation(value="token-添加", notes="token-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcToken hcToken) {
		hcTokenService.save(hcToken);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param hcToken
	 * @return
	 */
	@AutoLog(value = "token-编辑")
	@ApiOperation(value="token-编辑", notes="token-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcToken hcToken) {
		hcTokenService.updateById(hcToken);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "token-通过id删除")
	@ApiOperation(value="token-通过id删除", notes="token-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcTokenService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "token-批量删除")
	@ApiOperation(value="token-批量删除", notes="token-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcTokenService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "token-通过id查询")
	@ApiOperation(value="token-通过id查询", notes="token-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcToken hcToken = hcTokenService.getById(id);
		if(hcToken==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcToken);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcToken
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcToken hcToken) {
        return super.exportXls(request, hcToken, HcToken.class, "token");
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
        return super.importExcel(request, response, HcToken.class);
    }

}
