package com.trtm.iot.ztbhzjckh.controller;

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
import com.trtm.iot.ztbhzjckh.entity.ZtBhzjckh;
import com.trtm.iot.ztbhzjckh.service.IZtBhzjckhService;

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
 * @Description: zt_bhzjckh
 * @Author: jeecg-boot
 * @Date:   2023-11-03
 * @Version: V1.0
 */
@Api(tags="zt_bhzjckh")
@RestController
@RequestMapping("/ztbhzjckh/ztBhzjckh")
@Slf4j
public class ZtBhzjckhController extends JeecgController<ZtBhzjckh, IZtBhzjckhService> {
	@Autowired
	private IZtBhzjckhService ztBhzjckhService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ztBhzjckh
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "zt_bhzjckh-分页列表查询")
	@ApiOperation(value="zt_bhzjckh-分页列表查询", notes="zt_bhzjckh-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZtBhzjckh ztBhzjckh,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZtBhzjckh> queryWrapper = QueryGenerator.initQueryWrapper(ztBhzjckh, req.getParameterMap());
		Page<ZtBhzjckh> page = new Page<ZtBhzjckh>(pageNo, pageSize);
		IPage<ZtBhzjckh> pageList = ztBhzjckhService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ztBhzjckh
	 * @return
	 */
	@AutoLog(value = "zt_bhzjckh-添加")
	@ApiOperation(value="zt_bhzjckh-添加", notes="zt_bhzjckh-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZtBhzjckh ztBhzjckh) {
		ztBhzjckhService.save(ztBhzjckh);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ztBhzjckh
	 * @return
	 */
	@AutoLog(value = "zt_bhzjckh-编辑")
	@ApiOperation(value="zt_bhzjckh-编辑", notes="zt_bhzjckh-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZtBhzjckh ztBhzjckh) {
		ztBhzjckhService.updateById(ztBhzjckh);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "zt_bhzjckh-通过id删除")
	@ApiOperation(value="zt_bhzjckh-通过id删除", notes="zt_bhzjckh-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ztBhzjckhService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "zt_bhzjckh-批量删除")
	@ApiOperation(value="zt_bhzjckh-批量删除", notes="zt_bhzjckh-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ztBhzjckhService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "zt_bhzjckh-通过id查询")
	@ApiOperation(value="zt_bhzjckh-通过id查询", notes="zt_bhzjckh-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZtBhzjckh ztBhzjckh = ztBhzjckhService.getById(id);
		if(ztBhzjckh==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ztBhzjckh);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ztBhzjckh
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZtBhzjckh ztBhzjckh) {
        return super.exportXls(request, ztBhzjckh, ZtBhzjckh.class, "zt_bhzjckh");
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
        return super.importExcel(request, response, ZtBhzjckh.class);
    }

}
