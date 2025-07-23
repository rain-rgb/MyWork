package com.trtm.iot.productionValuelistMon.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import com.trtm.iot.productionValuelistMon.entity.ProductionValuelistMon;
import com.trtm.iot.productionValuelistMon.service.IProductionValuelistMonService;

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
 * @Description: 产值进度清单
 * @Author: jeecg-boot
 * @Date:   2022-09-01
 * @Version: V1.0
 */
@Api(tags="产值进度清单")
@RestController
@RequestMapping("/productionValuelistMon/productionValuelistMon")
@Slf4j
public class ProductionValuelistMonController extends JeecgController<ProductionValuelistMon, IProductionValuelistMonService> {
	@Autowired
	private IProductionValuelistMonService productionValuelistMonService;

	/**
	 * 分页列表查询
	 *
	 * @param productionValuelistMon
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "产值进度清单-分页列表查询")
	@ApiOperation(value="产值进度清单-分页列表查询", notes="产值进度清单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ProductionValuelistMon productionValuelistMon,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			productionValuelistMon.setSysOrgCode(sys_depart_orgcode + "*");
		} else {
			productionValuelistMon.setSysOrgCode(loginUser.getOrgCode() + "*");
		}
		QueryWrapper<ProductionValuelistMon> queryWrapper = QueryGenerator.initQueryWrapper(productionValuelistMon, req.getParameterMap());
		Page<ProductionValuelistMon> page = new Page<ProductionValuelistMon>(pageNo, pageSize);
		IPage<ProductionValuelistMon> pageList = productionValuelistMonService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param productionValuelistMon
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "产值进度清单-分页列表查询")
	 @ApiOperation(value="产值进度清单-分页列表查询", notes="产值进度清单-分页列表查询")
	 @GetMapping(value = "/liststa")
	 public Result<?> queryPagestaList(ProductionValuelistMon productionValuelistMon,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									HttpServletRequest req,Integer date) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			 productionValuelistMon.setSysOrgCode(sys_depart_orgcode + "*");
		 } else {
			 productionValuelistMon.setSysOrgCode(loginUser.getOrgCode() + "*");
		 }
		 SimpleDateFormat format = new SimpleDateFormat("yyyy");
		 String format1 = format.format(new Date());
		 QueryWrapper<ProductionValuelistMon> queryWrapper = QueryGenerator.initQueryWrapper(productionValuelistMon, req.getParameterMap());
		 if (date == 0) {//本年
			 queryWrapper.select("sum(actual_outvalue) actual_outvalue,sum(planned_outvalue) planned_outvalue,DATE_FORMAT(record_time,'%Y') uuid");
			 queryWrapper.last("and record_time like '" + format1 + "%' GROUP BY (SELECT DATE_FORMAT(record_time,'%Y')) ");
		 } else if (date == 1) {//按月
			 queryWrapper.select("sum(actual_outvalue) actual_outvalue,sum(planned_outvalue) planned_outvalue,DATE_FORMAT(record_time,'%m') uuid");
			 queryWrapper.last("and record_time like '" + format1 + "%' GROUP BY (SELECT DATE_FORMAT(record_time,'%Y-%m')) ");
		 } else if (date == 3) {//按季
			 queryWrapper.select("sum(actual_outvalue) actual_outvalue,sum(planned_outvalue) planned_outvalue,FLOOR((DATE_FORMAT(record_time,'%m')-1)/3)+1 uuid");
			 queryWrapper.last("and record_time like '" + format1 + "%' GROUP BY (SELECT FLOOR((DATE_FORMAT(record_time,'%m')-1)/3)+1)");
		 }
		 List<ProductionValuelistMon> pageList = productionValuelistMonService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param productionValuelistMon
	 * @return
	 */
	@AutoLog(value = "产值进度清单-添加")
	@ApiOperation(value="产值进度清单-添加", notes="产值进度清单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProductionValuelistMon productionValuelistMon) {
		productionValuelistMonService.save(productionValuelistMon);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param productionValuelistMon
	 * @return
	 */
	@AutoLog(value = "产值进度清单-编辑")
	@ApiOperation(value="产值进度清单-编辑", notes="产值进度清单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProductionValuelistMon productionValuelistMon) {
		productionValuelistMonService.updateById(productionValuelistMon);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产值进度清单-通过id删除")
	@ApiOperation(value="产值进度清单-通过id删除", notes="产值进度清单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		productionValuelistMonService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "产值进度清单-批量删除")
	@ApiOperation(value="产值进度清单-批量删除", notes="产值进度清单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.productionValuelistMonService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产值进度清单-通过id查询")
	@ApiOperation(value="产值进度清单-通过id查询", notes="产值进度清单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ProductionValuelistMon productionValuelistMon = productionValuelistMonService.getById(id);
		if(productionValuelistMon==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(productionValuelistMon);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param productionValuelistMon
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProductionValuelistMon productionValuelistMon) {
        return super.exportXls(request, productionValuelistMon, ProductionValuelistMon.class, "产值进度清单");
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
        return super.importExcel(request, response, ProductionValuelistMon.class);
    }

}
