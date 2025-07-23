package com.trtm.iot.hclijinshibie.controller;

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
import com.trtm.iot.hclijinshibie.entity.HcLijinshibieReal;
import com.trtm.iot.hclijinshibie.service.IHcLijinshibieRealService;

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
 * @Description: hc_lijinshibie_real
 * @Author: jeecg-boot
 * @Date:   2024-11-25
 * @Version: V1.0
 */
@Api(tags="hc_lijinshibie_real")
@RestController
@RequestMapping("/hclijinshibie/hcLijinshibieReal")
@Slf4j
public class HcLijinshibieRealController extends JeecgController<HcLijinshibieReal, IHcLijinshibieRealService> {
	@Autowired
	private IHcLijinshibieRealService hcLijinshibieRealService;

	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param hcLijinshibieReal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_real-分页列表查询")
	@ApiOperation(value="hc_lijinshibie_real-分页列表查询", notes="hc_lijinshibie_real-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcLijinshibieReal hcLijinshibieReal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (hcLijinshibieReal.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				hcLijinshibieReal.setShebeino(shebei);
			} else {
				hcLijinshibieReal.setShebeino("空");
			}
		}
		QueryWrapper<HcLijinshibieReal> queryWrapper = QueryGenerator.initQueryWrapper(hcLijinshibieReal, req.getParameterMap());
		Page<HcLijinshibieReal> page = new Page<HcLijinshibieReal>(pageNo, pageSize);
		IPage<HcLijinshibieReal> pageList = hcLijinshibieRealService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param hcLijinshibieReal
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_real-添加")
	@ApiOperation(value="hc_lijinshibie_real-添加", notes="hc_lijinshibie_real-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcLijinshibieReal hcLijinshibieReal) {
		hcLijinshibieRealService.save(hcLijinshibieReal);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hcLijinshibieReal
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_real-编辑")
	@ApiOperation(value="hc_lijinshibie_real-编辑", notes="hc_lijinshibie_real-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcLijinshibieReal hcLijinshibieReal) {
		hcLijinshibieRealService.updateById(hcLijinshibieReal);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_real-通过id删除")
	@ApiOperation(value="hc_lijinshibie_real-通过id删除", notes="hc_lijinshibie_real-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcLijinshibieRealService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_real-批量删除")
	@ApiOperation(value="hc_lijinshibie_real-批量删除", notes="hc_lijinshibie_real-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcLijinshibieRealService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_real-通过id查询")
	@ApiOperation(value="hc_lijinshibie_real-通过id查询", notes="hc_lijinshibie_real-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcLijinshibieReal hcLijinshibieReal = hcLijinshibieRealService.getById(id);
		if(hcLijinshibieReal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcLijinshibieReal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcLijinshibieReal
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcLijinshibieReal hcLijinshibieReal) {
        return super.exportXls(request, hcLijinshibieReal, HcLijinshibieReal.class, "hc_lijinshibie_real");
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
        return super.importExcel(request, response, HcLijinshibieReal.class);
    }

}
