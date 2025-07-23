package com.trtm.iot.shebeiinfo.controller;

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
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.shebeiinfo.entity.ShebeiAddoil;
import com.trtm.iot.shebeiinfo.service.IShebeiAddoilService;

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
 * @Description: shebei_addoil
 * @Author: jeecg-boot
 * @Date:   2024-11-13
 * @Version: V1.0
 */
@Api(tags="shebei_addoil")
@RestController
@RequestMapping("/shebeiinfo/shebeiAddoil")
@Slf4j
public class ShebeiAddoilController extends JeecgController<ShebeiAddoil, IShebeiAddoilService> {
	@Autowired
	private IShebeiAddoilService shebeiAddoilService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param shebeiAddoil
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "shebei_addoil-运行设备加油历史记录")
	@ApiOperation(value="shebei_addoil-运行设备加油历史记录", notes="shebei_addoil-运行设备加油历史记录")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ShebeiAddoil shebeiAddoil,String sys_depart_orgcode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (shebeiAddoil.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				shebeiAddoil.setShebeino(shebei);
			} else {
				shebeiAddoil.setShebeino("空");
			}
		}
		QueryWrapper<ShebeiAddoil> queryWrapper = QueryGenerator.initQueryWrapper(shebeiAddoil, req.getParameterMap());
		Page<ShebeiAddoil> page = new Page<ShebeiAddoil>(pageNo, pageSize);
		IPage<ShebeiAddoil> pageList = shebeiAddoilService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param shebeiAddoil
	 * @return
	 */
	@AutoLog(value = "shebei_addoil-添加加油记录")
	@ApiOperation(value="shebei_addoil-添加加油记录", notes="shebei_addoil-添加加油记录")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ShebeiAddoil shebeiAddoil) {
		shebeiAddoilService.save(shebeiAddoil);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param shebeiAddoil
	 * @return
	 */
	@AutoLog(value = "shebei_addoil-编辑加油记录")
	@ApiOperation(value="shebei_addoil-编辑加油记录", notes="shebei_addoil-编辑加油记录")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ShebeiAddoil shebeiAddoil) {
		shebeiAddoilService.updateById(shebeiAddoil);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "shebei_addoil-通过id删除")
	@ApiOperation(value="shebei_addoil-通过id删除", notes="shebei_addoil-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		shebeiAddoilService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "shebei_addoil-批量删除")
	@ApiOperation(value="shebei_addoil-批量删除", notes="shebei_addoil-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.shebeiAddoilService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "shebei_addoil-通过id查询")
	@ApiOperation(value="shebei_addoil-通过id查询", notes="shebei_addoil-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ShebeiAddoil shebeiAddoil = shebeiAddoilService.getById(id);
		if(shebeiAddoil==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(shebeiAddoil);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param shebeiAddoil
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShebeiAddoil shebeiAddoil) {
        return super.exportXls(request, shebeiAddoil, ShebeiAddoil.class, "shebei_addoil");
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
        return super.importExcel(request, response, ShebeiAddoil.class);
    }

}
