package com.trtm.iot.ztwzjincslsjb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.ztwzjincslsjb.entity.ZtwzjincslsjbVo;
import com.trtm.iot.ztwzjinmaterial.entity.Ztwzjinmaterial;
import com.trtm.iot.ztwzjinmaterial.service.IZtwzjinmaterialService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.ztwzjincslsjb.entity.Ztwzjincslsjb;
import com.trtm.iot.ztwzjincslsjb.service.IZtwzjincslsjbService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: 中铁一局现场收料数据
 * @Author: jeecg-boot
 * @Date:   2024-04-03
 * @Version: V1.0
 */
@Api(tags="中铁一局现场收料数据")
@RestController
@RequestMapping("/ztwzjincslsjb/ztwzjincslsjb")
@Slf4j
public class ZtwzjincslsjbController extends JeecgController<Ztwzjincslsjb, IZtwzjincslsjbService> {
	@Autowired
	private IZtwzjincslsjbService ztwzjincslsjbService;
	 @Autowired
	 private IZtwzjinmaterialService ztwzjinmaterialService;
	/**
	 * 分页列表查询
	 *
	 * @param ztwzjincslsjb
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "中铁一局现场收料数据-分页列表查询")
	@ApiOperation(value="中铁一局现场收料数据-分页列表查询", notes="中铁一局现场收料数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Ztwzjincslsjb ztwzjincslsjb,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Ztwzjincslsjb> queryWrapper = QueryGenerator.initQueryWrapper(ztwzjincslsjb, req.getParameterMap());
		Page<Ztwzjincslsjb> page = new Page<Ztwzjincslsjb>(pageNo, pageSize);
		IPage<Ztwzjincslsjb> pageList = ztwzjincslsjbService.page(page, queryWrapper);
		List<Ztwzjincslsjb> records = pageList.getRecords();

		IPage<ZtwzjincslsjbVo> objectIPage = new Page<ZtwzjincslsjbVo>(pageNo,pageSize);
		objectIPage.setPages(pageList.getPages());
		objectIPage.setCurrent(pageList.getCurrent());
		objectIPage.setSize(pageList.getSize());
		objectIPage.setTotal(pageList.getTotal());
		ArrayList<ZtwzjincslsjbVo> list = new ArrayList<>();
		for (Ztwzjincslsjb record :records){
			String orderdate = record.getOrderdate();
			String substring = orderdate.substring(0, 4);
			String substring1 = orderdate.substring(5, 7);
			record.setOrderdate(substring + substring1);
			ZtwzjincslsjbVo ztwzjincslsjbVo = new ZtwzjincslsjbVo();

			String oriorderid = record.getOriorderid();
			QueryWrapper<Ztwzjinmaterial> queryWrapper1 = new QueryWrapper<>();
			queryWrapper1.eq("oriorderid",oriorderid);
			Ztwzjinmaterial one = ztwzjinmaterialService.getOne(queryWrapper1);

			BeanUtils.copyProperties(record,ztwzjincslsjbVo);
			BeanUtils.copyProperties(one,ztwzjincslsjbVo);
			list.add(ztwzjincslsjbVo);
		}
		objectIPage.setRecords(list);
		return Result.OK(objectIPage);
	}
	
	/**
	 *   添加
	 *
	 * @param ztwzjincslsjb
	 * @return
	 */
	@AutoLog(value = "中铁一局现场收料数据-添加")
	@ApiOperation(value="中铁一局现场收料数据-添加", notes="中铁一局现场收料数据-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Ztwzjincslsjb ztwzjincslsjb) {
		ztwzjincslsjbService.save(ztwzjincslsjb);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ztwzjincslsjb
	 * @return
	 */
	@AutoLog(value = "中铁一局现场收料数据-编辑")
	@ApiOperation(value="中铁一局现场收料数据-编辑", notes="中铁一局现场收料数据-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Ztwzjincslsjb ztwzjincslsjb) {
		ztwzjincslsjbService.updateById(ztwzjincslsjb);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "中铁一局现场收料数据-通过id删除")
	@ApiOperation(value="中铁一局现场收料数据-通过id删除", notes="中铁一局现场收料数据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ztwzjincslsjbService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "中铁一局现场收料数据-批量删除")
	@ApiOperation(value="中铁一局现场收料数据-批量删除", notes="中铁一局现场收料数据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ztwzjincslsjbService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "中铁一局现场收料数据-通过id查询")
	@ApiOperation(value="中铁一局现场收料数据-通过id查询", notes="中铁一局现场收料数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Ztwzjincslsjb ztwzjincslsjb = ztwzjincslsjbService.getById(id);
		if(ztwzjincslsjb==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ztwzjincslsjb);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ztwzjincslsjb
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Ztwzjincslsjb ztwzjincslsjb) {
        return super.exportXls(request, ztwzjincslsjb, Ztwzjincslsjb.class, "中铁一局现场收料数据");
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
        return super.importExcel(request, response, Ztwzjincslsjb.class);
    }

}
