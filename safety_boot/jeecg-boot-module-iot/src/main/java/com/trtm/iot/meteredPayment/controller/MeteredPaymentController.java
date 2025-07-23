package com.trtm.iot.meteredPayment.controller;

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
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.meteredPayment.entity.MeteredPayment;
import com.trtm.iot.meteredPayment.service.IMeteredPaymentService;

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
 * @Description: 计量支付
 * @Author: jeecg-boot
 * @Date:   2022-07-14
 * @Version: V1.0
 */
@Api(tags="计量支付")
@RestController
@RequestMapping("/meteredPayment/meteredPayment")
@Slf4j
public class MeteredPaymentController extends JeecgController<MeteredPayment, IMeteredPaymentService> {
	@Autowired
	private IMeteredPaymentService meteredPaymentService;

	/**
	 * 分页列表查询
	 *
	 * @param meteredPayment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "计量支付-分页列表查询")
	@ApiOperation(value="计量支付-分页列表查询", notes="计量支付-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MeteredPayment meteredPayment,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			meteredPayment.setSysOrgCode(sys_depart_orgcode + "*");
		} else {
			meteredPayment.setSysOrgCode(loginUser.getOrgCode() + "*");
		}
		QueryWrapper<MeteredPayment> queryWrapper = QueryGenerator.initQueryWrapper(meteredPayment, req.getParameterMap());
		Page<MeteredPayment> page = new Page<MeteredPayment>(pageNo, pageSize);
		IPage<MeteredPayment> pageList = meteredPaymentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param meteredPayment
	 * @return
	 */
	@AutoLog(value = "计量支付-添加")
	@ApiOperation(value="计量支付-添加", notes="计量支付-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MeteredPayment meteredPayment) {
		meteredPaymentService.save(meteredPayment);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param meteredPayment
	 * @return
	 */
	@AutoLog(value = "计量支付-编辑")
	@ApiOperation(value="计量支付-编辑", notes="计量支付-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MeteredPayment meteredPayment) {
		meteredPaymentService.updateById(meteredPayment);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "计量支付-通过id删除")
	@ApiOperation(value="计量支付-通过id删除", notes="计量支付-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		meteredPaymentService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "计量支付-批量删除")
	@ApiOperation(value="计量支付-批量删除", notes="计量支付-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.meteredPaymentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "计量支付-通过id查询")
	@ApiOperation(value="计量支付-通过id查询", notes="计量支付-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MeteredPayment meteredPayment = meteredPaymentService.getById(id);
		if(meteredPayment==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(meteredPayment);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param meteredPayment
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MeteredPayment meteredPayment) {
        return super.exportXls(request, meteredPayment, MeteredPayment.class, "计量支付");
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
        return super.importExcel(request, response, MeteredPayment.class);
    }

}
