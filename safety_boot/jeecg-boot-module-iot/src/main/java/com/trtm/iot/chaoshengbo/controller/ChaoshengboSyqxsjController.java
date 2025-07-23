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
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyqxsj;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSyqxsjService;

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
 * @Description: chaoshengbo_syqxsj
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Api(tags="chaoshengbo_syqxsj")
@RestController
@RequestMapping("/chaoshengbo/chaoshengboSyqxsj")
@Slf4j
public class ChaoshengboSyqxsjController extends JeecgController<ChaoshengboSyqxsj, IChaoshengboSyqxsjService> {
	@Autowired
	private IChaoshengboSyqxsjService chaoshengboSyqxsjService;

	/**
	 * 分页列表查询
	 *
	 * @param chaoshengboSyqxsj
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syqxsj-分页列表查询")
	@ApiOperation(value="chaoshengbo_syqxsj-分页列表查询", notes="chaoshengbo_syqxsj-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ChaoshengboSyqxsj chaoshengboSyqxsj,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ChaoshengboSyqxsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSyqxsj, req.getParameterMap());
		Page<ChaoshengboSyqxsj> page = new Page<ChaoshengboSyqxsj>(pageNo, pageSize);
		IPage<ChaoshengboSyqxsj> pageList = chaoshengboSyqxsjService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param chaoshengboSyqxsj
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syqxsj-添加")
	@ApiOperation(value="chaoshengbo_syqxsj-添加", notes="chaoshengbo_syqxsj-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ChaoshengboSyqxsj chaoshengboSyqxsj) {
		chaoshengboSyqxsjService.save(chaoshengboSyqxsj);
		return Result.OK("添加成功！");
	}

	 /**
	  *   chaoshengbo_syqxs添加数据对外开放
	  *
	  * @param chaoshengboSyqxsj
	  * @return
	  */
	 @AutoLog(value = "chaoshengbo_syqxsj-添加")
	 @ApiOperation(value="chaoshengbo_syqxsj-添加", notes="chaoshengbo_syqxsj-添加")
	 @PostMapping(value = "/addOpen")
	 public Result<?> addOpen(@RequestBody ChaoshengboSyqxsj chaoshengboSyqxsj) {
		 chaoshengboSyqxsjService.save(chaoshengboSyqxsj);
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param chaoshengboSyqxsj
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syqxsj-编辑")
	@ApiOperation(value="chaoshengbo_syqxsj-编辑", notes="chaoshengbo_syqxsj-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ChaoshengboSyqxsj chaoshengboSyqxsj) {
		chaoshengboSyqxsjService.updateById(chaoshengboSyqxsj);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syqxsj-通过id删除")
	@ApiOperation(value="chaoshengbo_syqxsj-通过id删除", notes="chaoshengbo_syqxsj-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		chaoshengboSyqxsjService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syqxsj-批量删除")
	@ApiOperation(value="chaoshengbo_syqxsj-批量删除", notes="chaoshengbo_syqxsj-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.chaoshengboSyqxsjService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syqxsj-通过id查询")
	@ApiOperation(value="chaoshengbo_syqxsj-通过id查询", notes="chaoshengbo_syqxsj-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ChaoshengboSyqxsj chaoshengboSyqxsj = chaoshengboSyqxsjService.getById(id);
		if(chaoshengboSyqxsj==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(chaoshengboSyqxsj);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param chaoshengboSyqxsj
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ChaoshengboSyqxsj chaoshengboSyqxsj) {
        return super.exportXls(request, chaoshengboSyqxsj, ChaoshengboSyqxsj.class, "chaoshengbo_syqxsj");
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
        return super.importExcel(request, response, ChaoshengboSyqxsj.class);
    }

}
