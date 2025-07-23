package com.trtm.iot.bhzcfg.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.api.utils.StringUtils;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzcfg.entity.BhzNotOrder;
import com.trtm.iot.bhzcfg.service.IBhzNotOrderService;

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
 * @Description: bhz_not_order
 * @Author: jeecg-boot
 * @Date:   2025-04-16
 * @Version: V1.0
 */
@Api(tags="bhz_not_order")
@RestController
@RequestMapping("/bhzcfg/bhzNotOrder")
@Slf4j
public class BhzNotOrderController extends JeecgController<BhzNotOrder, IBhzNotOrderService> {
	@Autowired
	private IBhzNotOrderService bhzNotOrderService;

	 @Autowired
	 private IBhzCementBaseService bhzCementBaseService;
	/**
	 * 分页列表查询
	 *
	 * @param bhzNotOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_not_order-分页列表查询")
	@ApiOperation(value="bhz_not_order-分页列表查询", notes="bhz_not_order-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzNotOrder bhzNotOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if(StringUtils.isBlank(sys_depart_orgcode)){
			bhzNotOrder.setSysOrgCode(loginUser.getOrgCode()+"*");
		}else{
			bhzNotOrder.setSysOrgCode(sys_depart_orgcode+"*");
		}
		QueryWrapper<BhzNotOrder> queryWrapper = QueryGenerator.initQueryWrapper(bhzNotOrder, req.getParameterMap());
		Page<BhzNotOrder> page = new Page<BhzNotOrder>(pageNo, pageSize);
		IPage<BhzNotOrder> pageList = bhzNotOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	// 查看未使用拌合站数据

	/**
	 *   添加
	 *
	 * @param bhzNotOrder
	 * @return
	 */
	@AutoLog(value = "bhz_not_order-添加")
	@ApiOperation(value="bhz_not_order-添加", notes="bhz_not_order-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzNotOrder bhzNotOrder) {
		bhzNotOrderService.save(bhzNotOrder);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param bhzNotOrder
	 * @return
	 */
	@AutoLog(value = "bhz_not_order-编辑")
	@ApiOperation(value="bhz_not_order-编辑", notes="bhz_not_order-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzNotOrder bhzNotOrder) {
		bhzNotOrderService.updateById(bhzNotOrder);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_not_order-通过id删除")
	@ApiOperation(value="bhz_not_order-通过id删除", notes="bhz_not_order-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzNotOrderService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_not_order-批量删除")
	@ApiOperation(value="bhz_not_order-批量删除", notes="bhz_not_order-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzNotOrderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_not_order-通过id查询")
	@ApiOperation(value="bhz_not_order-通过id查询", notes="bhz_not_order-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzNotOrder bhzNotOrder = bhzNotOrderService.getById(id);
		if(bhzNotOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzNotOrder);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzNotOrder
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzNotOrder bhzNotOrder) {
        return super.exportXls(request, bhzNotOrder, BhzNotOrder.class, "bhz_not_order");
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
        return super.importExcel(request, response, BhzNotOrder.class);
    }

}
