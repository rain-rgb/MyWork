package com.trtm.iot.lqbhzrecipe.controller;

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
import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqPhbZibiaoService;

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
 * @Description: bhz_lq_phb_zibiao
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Api(tags="bhz_lq_phb_zibiao")
@RestController
@RequestMapping("/lqbhzrecipe/bhzLqPhbZibiao")
@Slf4j
public class BhzLqPhbZibiaoController extends JeecgController<BhzLqPhbZibiao, IBhzLqPhbZibiaoService> {
	@Autowired
	private IBhzLqPhbZibiaoService bhzLqPhbZibiaoService;

	/**
	 * 分页列表查询
	 *
	 * @param bhzLqPhbZibiao
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_lq_phb_zibiao-分页列表查询")
	@ApiOperation(value="bhz_lq_phb_zibiao-分页列表查询", notes="bhz_lq_phb_zibiao-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzLqPhbZibiao bhzLqPhbZibiao,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzLqPhbZibiao> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqPhbZibiao, req.getParameterMap());
		Page<BhzLqPhbZibiao> page = new Page<BhzLqPhbZibiao>(pageNo, pageSize);
		IPage<BhzLqPhbZibiao> pageList = bhzLqPhbZibiaoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param bhzLqPhbZibiao
	 * @return
	 */
	@AutoLog(value = "bhz_lq_phb_zibiao-添加")
	@ApiOperation(value="bhz_lq_phb_zibiao-添加", notes="bhz_lq_phb_zibiao-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzLqPhbZibiao bhzLqPhbZibiao) {
		bhzLqPhbZibiaoService.save(bhzLqPhbZibiao);
		return Result.OK("添加成功！");
	}
	 /**
	  *   添加
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "bhz_lq_phb_zibiao-添加")
	 @ApiOperation(value="bhz_lq_phb_zibiao-添加", notes="bhz_lq_phb_zibiao-添加")
	 @GetMapping(value = "/list1")
	 public Result<?> list1(@RequestParam(name="zhuziid",required=true) String zhuziid) {
	 	QueryWrapper<BhzLqPhbZibiao> queryWrapper=new QueryWrapper<>();
		 queryWrapper.eq("zhuziid",zhuziid);
		 List<BhzLqPhbZibiao> list = bhzLqPhbZibiaoService.list(queryWrapper);
		 return Result.OK(list);
	 }


	/**
	 *  编辑
	 *
	 * @param bhzLqPhbZibiao
	 * @return
	 */
	@AutoLog(value = "bhz_lq_phb_zibiao-编辑")
	@ApiOperation(value="bhz_lq_phb_zibiao-编辑", notes="bhz_lq_phb_zibiao-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzLqPhbZibiao bhzLqPhbZibiao) {
		bhzLqPhbZibiaoService.updateById(bhzLqPhbZibiao);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_lq_phb_zibiao-通过id删除")
	@ApiOperation(value="bhz_lq_phb_zibiao-通过id删除", notes="bhz_lq_phb_zibiao-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzLqPhbZibiaoService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_lq_phb_zibiao-批量删除")
	@ApiOperation(value="bhz_lq_phb_zibiao-批量删除", notes="bhz_lq_phb_zibiao-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzLqPhbZibiaoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_lq_phb_zibiao-通过id查询")
	@ApiOperation(value="bhz_lq_phb_zibiao-通过id查询", notes="bhz_lq_phb_zibiao-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzLqPhbZibiao bhzLqPhbZibiao = bhzLqPhbZibiaoService.getById(id);
		if(bhzLqPhbZibiao==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzLqPhbZibiao);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzLqPhbZibiao
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzLqPhbZibiao bhzLqPhbZibiao) {
        return super.exportXls(request, bhzLqPhbZibiao, BhzLqPhbZibiao.class, "bhz_lq_phb_zibiao");
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
        return super.importExcel(request, response, BhzLqPhbZibiao.class);
    }

}
