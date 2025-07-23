package com.trtm.iot.bhzSwPhbZibiao.controller;

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
import com.trtm.iot.bhzSwPhbZibiao.entity.BhzSwPhbZibiao;
import com.trtm.iot.bhzSwPhbZibiao.service.IBhzSwPhbZibiaoService;

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
 * @Description: 水稳理论配合比子表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Api(tags="水稳理论配合比子表")
@RestController
@RequestMapping("/bhzSwPhbZibiao/bhzSwPhbZibiao")
@Slf4j
public class BhzSwPhbZibiaoController extends JeecgController<BhzSwPhbZibiao, IBhzSwPhbZibiaoService> {
	@Autowired
	private IBhzSwPhbZibiaoService bhzSwPhbZibiaoService;

	/**
	 * 分页列表查询
	 *
	 * @param bhzSwPhbZibiao
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比子表-分页列表查询")
	@ApiOperation(value="水稳理论配合比子表-分页列表查询", notes="水稳理论配合比子表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzSwPhbZibiao bhzSwPhbZibiao,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzSwPhbZibiao> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwPhbZibiao, req.getParameterMap());
		Page<BhzSwPhbZibiao> page = new Page<BhzSwPhbZibiao>(pageNo, pageSize);
		IPage<BhzSwPhbZibiao> pageList = bhzSwPhbZibiaoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param bhzSwPhbZibiao
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比子表-添加")
	@ApiOperation(value="水稳理论配合比子表-添加", notes="水稳理论配合比子表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzSwPhbZibiao bhzSwPhbZibiao) {
		bhzSwPhbZibiaoService.save(bhzSwPhbZibiao);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param bhzSwPhbZibiao
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比子表-编辑")
	@ApiOperation(value="水稳理论配合比子表-编辑", notes="水稳理论配合比子表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzSwPhbZibiao bhzSwPhbZibiao) {
		bhzSwPhbZibiaoService.updateById(bhzSwPhbZibiao);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比子表-通过id删除")
	@ApiOperation(value="水稳理论配合比子表-通过id删除", notes="水稳理论配合比子表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzSwPhbZibiaoService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比子表-批量删除")
	@ApiOperation(value="水稳理论配合比子表-批量删除", notes="水稳理论配合比子表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzSwPhbZibiaoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比子表-通过id查询")
	@ApiOperation(value="水稳理论配合比子表-通过id查询", notes="水稳理论配合比子表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzSwPhbZibiao bhzSwPhbZibiao = bhzSwPhbZibiaoService.getById(id);
		if(bhzSwPhbZibiao==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzSwPhbZibiao);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzSwPhbZibiao
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzSwPhbZibiao bhzSwPhbZibiao) {
        return super.exportXls(request, bhzSwPhbZibiao, BhzSwPhbZibiao.class, "水稳理论配合比子表");
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
        return super.importExcel(request, response, BhzSwPhbZibiao.class);
    }

	 /**
	  *	添加
	  * @param
	  * @return
	  */
	 @AutoLog(value = "bhz_sw_phb_zibiao-添加")
	 @ApiOperation(value="bhz_sw_phb_zibiao-添加", notes="bhz_sw_phb_zibiao-添加")
	 @GetMapping(value = "/list1")
	 public Result<?> list(@RequestParam("zhuziid")String zhuziid){
		 QueryWrapper<BhzSwPhbZibiao> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("zhuziid",zhuziid);
		 return Result.OK(bhzSwPhbZibiaoService.list(queryWrapper));
	 }
}
