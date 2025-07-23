package com.trtm.iot.chaoshengbo.controller;

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
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyljzs;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSyljzsService;

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
 * @Description: chaoshengbo_syljzs
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Api(tags="chaoshengbo_syljzs")
@RestController
@RequestMapping("/chaoshengbo/chaoshengboSyljzs")
@Slf4j
public class ChaoshengboSyljzsController extends JeecgController<ChaoshengboSyljzs, IChaoshengboSyljzsService> {
	@Autowired
	private IChaoshengboSyljzsService chaoshengboSyljzsService;

	/**
	 * 分页列表查询
	 *
	 * @param chaoshengboSyljzs
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syljzs-分页列表查询")
	@ApiOperation(value="chaoshengbo_syljzs-分页列表查询", notes="chaoshengbo_syljzs-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ChaoshengboSyljzs chaoshengboSyljzs,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ChaoshengboSyljzs> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSyljzs, req.getParameterMap());
		Page<ChaoshengboSyljzs> page = new Page<ChaoshengboSyljzs>(pageNo, pageSize);
		IPage<ChaoshengboSyljzs> pageList = chaoshengboSyljzsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param chaoshengboSyljzs
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syljzs-添加")
	@ApiOperation(value="chaoshengbo_syljzs-添加", notes="chaoshengbo_syljzs-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ChaoshengboSyljzs chaoshengboSyljzs) {
		chaoshengboSyljzsService.save(chaoshengboSyljzs);
		return Result.OK("添加成功！");
	}

	 /**
	  *   chaoshengbo_syljzs添加数据对外开放
	  *
	  * @param chaoshengboSyljzs
	  * @return
	  */
	 @AutoLog(value = "chaoshengbo_syljzs-添加")
	 @ApiOperation(value="chaoshengbo_syljzs-添加", notes="chaoshengbo_syljzs-添加")
	 @PostMapping(value = "/addOpen")
	 public Result<?> addOpen(@RequestBody ChaoshengboSyljzs chaoshengboSyljzs) {
		 chaoshengboSyljzsService.save(chaoshengboSyljzs);
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param chaoshengboSyljzs
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syljzs-编辑")
	@ApiOperation(value="chaoshengbo_syljzs-编辑", notes="chaoshengbo_syljzs-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ChaoshengboSyljzs chaoshengboSyljzs) {
		chaoshengboSyljzsService.updateById(chaoshengboSyljzs);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syljzs-通过id删除")
	@ApiOperation(value="chaoshengbo_syljzs-通过id删除", notes="chaoshengbo_syljzs-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		chaoshengboSyljzsService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syljzs-批量删除")
	@ApiOperation(value="chaoshengbo_syljzs-批量删除", notes="chaoshengbo_syljzs-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.chaoshengboSyljzsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syljzs-通过id查询")
	@ApiOperation(value="chaoshengbo_syljzs-通过id查询", notes="chaoshengbo_syljzs-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ChaoshengboSyljzs chaoshengboSyljzs = chaoshengboSyljzsService.getById(id);
		if(chaoshengboSyljzs==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(chaoshengboSyljzs);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param chaoshengboSyljzs
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ChaoshengboSyljzs chaoshengboSyljzs) {
        return super.exportXls(request, chaoshengboSyljzs, ChaoshengboSyljzs.class, "chaoshengbo_syljzs");
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
        return super.importExcel(request, response, ChaoshengboSyljzs.class);
    }

}
