package com.trtm.iot.skip_car.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.skip_car.entity.SkipCarTem;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.skip_car.entity.SkipCar;
import com.trtm.iot.skip_car.service.ISkipCarService;

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
 * @Description: 运输温度
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
@Api(tags="运输温度")
@RestController
@RequestMapping("/skip_car/skipCar")
@Slf4j
public class SkipCarController extends JeecgController<SkipCar, ISkipCarService> {
	@Autowired
	private ISkipCarService skipCarService;

	/**
	 * 分页列表查询
	 *
	 * @param skipCar
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "运输温度-分页列表查询")
	@ApiOperation(value="运输温度-分页列表查询", notes="运输温度-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SkipCar skipCar,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SkipCar> queryWrapper = QueryGenerator.initQueryWrapper(skipCar, req.getParameterMap());
		Page<SkipCar> page = new Page<SkipCar>(pageNo, pageSize);
		IPage<SkipCar> pageList = skipCarService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param skipCar
	 * @return
	 */
	@AutoLog(value = "运输温度-添加")
	@ApiOperation(value="运输温度-添加", notes="运输温度-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SkipCar skipCar) {
		skipCarService.save(skipCar);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param skipCar
	 * @return
	 */
	@AutoLog(value = "运输温度-编辑")
	@ApiOperation(value="运输温度-编辑", notes="运输温度-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SkipCar skipCar) {
		skipCarService.updateById(skipCar);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运输温度-通过id删除")
	@ApiOperation(value="运输温度-通过id删除", notes="运输温度-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		skipCarService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "运输温度-批量删除")
	@ApiOperation(value="运输温度-批量删除", notes="运输温度-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.skipCarService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运输温度-通过id查询")
	@ApiOperation(value="运输温度-通过id查询", notes="运输温度-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SkipCar skipCar = skipCarService.getById(id);
		if(skipCar==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(skipCar);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param skipCar
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkipCar skipCar) {
        return super.exportXls(request, skipCar, SkipCar.class, "运输温度");
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
        return super.importExcel(request, response, SkipCar.class);
    }

	 @RequestMapping(value = "/getTem", method = RequestMethod.POST)
	 public Result<?> getTem(HttpServletRequest request) {
		 String carNumber = request.getParameter("carNumber");
		 String date = request.getParameter("date");
		 List<SkipCarTem> skipCarList = skipCarService.getTem(carNumber,date);
		 return Result.OK("请求成功！", skipCarList);
	 }
}
