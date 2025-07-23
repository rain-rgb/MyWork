package com.trtm.iot.gualanreal.controller;

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
import com.trtm.iot.gualanreal.entity.GualanBaseReal;
import com.trtm.iot.gualanreal.service.IGualanBaseRealService;

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
 * @Description: 挂篮实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-11-16
 * @Version: V1.0
 */
@Api(tags="挂篮实时数据表")
@RestController
@RequestMapping("/gualanreal/gualanBaseReal")
@Slf4j
public class GualanBaseRealController extends JeecgController<GualanBaseReal, IGualanBaseRealService> {
	@Autowired
	private IGualanBaseRealService gualanBaseRealService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param gualanBaseReal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "挂篮实时数据表-分页列表查询")
	@ApiOperation(value="挂篮实时数据表-分页列表查询", notes="挂篮实时数据表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GualanBaseReal gualanBaseReal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (gualanBaseReal.getShebeibanhao() == null) {
			if (!"null".equals(shebei)) {
				gualanBaseReal.setShebeibanhao(shebei);
			}else {
				gualanBaseReal.setShebeibanhao("空");
			}
		}
		QueryWrapper<GualanBaseReal> queryWrapper = QueryGenerator.initQueryWrapper(gualanBaseReal, req.getParameterMap());
		Page<GualanBaseReal> page = new Page<GualanBaseReal>(pageNo, pageSize);
		IPage<GualanBaseReal> pageList = gualanBaseRealService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param gualanBaseReal
	 * @return
	 */
	@AutoLog(value = "挂篮实时数据表-添加")
	@ApiOperation(value="挂篮实时数据表-添加", notes="挂篮实时数据表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GualanBaseReal gualanBaseReal) {
		gualanBaseRealService.save(gualanBaseReal);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param gualanBaseReal
	 * @return
	 */
	@AutoLog(value = "挂篮实时数据表-编辑")
	@ApiOperation(value="挂篮实时数据表-编辑", notes="挂篮实时数据表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GualanBaseReal gualanBaseReal) {
		gualanBaseRealService.updateById(gualanBaseReal);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "挂篮实时数据表-通过id删除")
	@ApiOperation(value="挂篮实时数据表-通过id删除", notes="挂篮实时数据表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		gualanBaseRealService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "挂篮实时数据表-批量删除")
	@ApiOperation(value="挂篮实时数据表-批量删除", notes="挂篮实时数据表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gualanBaseRealService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "挂篮实时数据表-通过id查询")
	@ApiOperation(value="挂篮实时数据表-通过id查询", notes="挂篮实时数据表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		GualanBaseReal gualanBaseReal = gualanBaseRealService.getById(id);
		if(gualanBaseReal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(gualanBaseReal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gualanBaseReal
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GualanBaseReal gualanBaseReal) {
        return super.exportXls(request, gualanBaseReal, GualanBaseReal.class, "挂篮实时数据表");
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
        return super.importExcel(request, response, GualanBaseReal.class);
    }

}
