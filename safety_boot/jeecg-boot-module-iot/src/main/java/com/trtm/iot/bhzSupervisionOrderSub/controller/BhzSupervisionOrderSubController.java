package com.trtm.iot.bhzSupervisionOrderSub.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.bhzSupervisionOrder.entity.BhzSupervisionOrder;
import com.trtm.iot.bhzSupervisionOrder.service.IBhzSupervisionOrderService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzSupervisionOrderSub.entity.BhzSupervisionOrderSub;
import com.trtm.iot.bhzSupervisionOrderSub.service.IBhzSupervisionOrderSubService;

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
 * @Description: bhz_supervision_order_sub
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Api(tags="bhz_supervision_order_sub")
@RestController
@RequestMapping("/bhzSupervisionOrderSub/bhzSupervisionOrderSub")
@Slf4j
public class BhzSupervisionOrderSubController extends JeecgController<BhzSupervisionOrderSub, IBhzSupervisionOrderSubService> {
	@Autowired
	private IBhzSupervisionOrderSubService bhzSupervisionOrderSubService;
	@Autowired
	private IBhzSupervisionOrderService bhzSupervisionOrderService;

	/**
	 * 分页列表查询
	 *
	 * @param bhzSupervisionOrderSub
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_supervision_order_sub-分页列表查询")
	@ApiOperation(value="bhz_supervision_order_sub-分页列表查询", notes="bhz_supervision_order_sub-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzSupervisionOrderSub bhzSupervisionOrderSub,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzSupervisionOrderSub> queryWrapper = QueryGenerator.initQueryWrapper(bhzSupervisionOrderSub, req.getParameterMap());
		Page<BhzSupervisionOrderSub> page = new Page<BhzSupervisionOrderSub>(pageNo, pageSize);
		IPage<BhzSupervisionOrderSub> pageList = bhzSupervisionOrderSubService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param bhzSupervisionOrderSub
	 * @return
	 */
	@AutoLog(value = "bhz_supervision_order_sub-添加")
	@ApiOperation(value="bhz_supervision_order_sub-添加", notes="bhz_supervision_order_sub-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzSupervisionOrderSub bhzSupervisionOrderSub) {
		bhzSupervisionOrderSubService.save(bhzSupervisionOrderSub);
		return Result.OK("添加成功！");
	}


	/**
	 *  编辑
	 *
	 * @param bhzSupervisionOrderSub
	 * @return
	 */
	@AutoLog(value = "bhz_supervision_order_sub-编辑")
	@ApiOperation(value="bhz_supervision_order_sub-编辑", notes="bhz_supervision_order_sub-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzSupervisionOrderSub bhzSupervisionOrderSub) {
		bhzSupervisionOrderSubService.updateById(bhzSupervisionOrderSub);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_supervision_order_sub-通过id删除")
	@ApiOperation(value="bhz_supervision_order_sub-通过id删除", notes="bhz_supervision_order_sub-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzSupervisionOrderSubService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_supervision_order_sub-批量删除")
	@ApiOperation(value="bhz_supervision_order_sub-批量删除", notes="bhz_supervision_order_sub-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzSupervisionOrderSubService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_supervision_order_sub-通过id查询")
	@ApiOperation(value="bhz_supervision_order_sub-通过id查询", notes="bhz_supervision_order_sub-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzSupervisionOrderSub bhzSupervisionOrderSub = bhzSupervisionOrderSubService.getById(id);
		if(bhzSupervisionOrderSub==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzSupervisionOrderSub);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzSupervisionOrderSub
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzSupervisionOrderSub bhzSupervisionOrderSub) {
        return super.exportXls(request, bhzSupervisionOrderSub, BhzSupervisionOrderSub.class, "bhz_supervision_order_sub");
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
        return super.importExcel(request, response, BhzSupervisionOrderSub.class);
    }

}
