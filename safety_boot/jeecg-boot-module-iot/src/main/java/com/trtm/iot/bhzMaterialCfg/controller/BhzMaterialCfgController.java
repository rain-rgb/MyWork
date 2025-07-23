package com.trtm.iot.bhzMaterialCfg.controller;

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
import com.trtm.iot.bhzMaterialCfg.entity.BhzMaterialCfg;
import com.trtm.iot.bhzMaterialCfg.service.IBhzMaterialCfgService;

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
 * @Description: bhz_material_cfg
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Api(tags="bhz_material_cfg")
@RestController
@RequestMapping("/bhzMaterialCfg/bhzMaterialCfg")
@Slf4j
public class BhzMaterialCfgController extends JeecgController<BhzMaterialCfg, IBhzMaterialCfgService> {
	@Autowired
	private IBhzMaterialCfgService bhzMaterialCfgService;

	/**
	 * 分页列表查询
	 *
	 * @param bhzMaterialCfg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_material_cfg-分页列表查询")
	@ApiOperation(value="bhz_material_cfg-分页列表查询", notes="bhz_material_cfg-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzMaterialCfg bhzMaterialCfg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzMaterialCfg> queryWrapper = QueryGenerator.initQueryWrapper(bhzMaterialCfg, req.getParameterMap());
		Page<BhzMaterialCfg> page = new Page<BhzMaterialCfg>(pageNo, pageSize);
		IPage<BhzMaterialCfg> pageList = bhzMaterialCfgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 列表查询
	  *
	  * @param bhzMaterialCfg
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "bhz_material_cfg-分页列表查询")
	 @ApiOperation(value="bhz_material_cfg-分页列表查询", notes="bhz_material_cfg-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(BhzMaterialCfg bhzMaterialCfg,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<BhzMaterialCfg> queryWrapper = QueryGenerator.initQueryWrapper(bhzMaterialCfg, req.getParameterMap());
		 List<BhzMaterialCfg> pageList = bhzMaterialCfgService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param bhzMaterialCfg
	 * @return
	 */
	@AutoLog(value = "bhz_material_cfg-添加")
	@ApiOperation(value="bhz_material_cfg-添加", notes="bhz_material_cfg-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzMaterialCfg bhzMaterialCfg) {
		bhzMaterialCfgService.save(bhzMaterialCfg);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param bhzMaterialCfg
	 * @return
	 */
	@AutoLog(value = "bhz_material_cfg-编辑")
	@ApiOperation(value="bhz_material_cfg-编辑", notes="bhz_material_cfg-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzMaterialCfg bhzMaterialCfg) {
		bhzMaterialCfgService.updateById(bhzMaterialCfg);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_material_cfg-通过id删除")
	@ApiOperation(value="bhz_material_cfg-通过id删除", notes="bhz_material_cfg-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzMaterialCfgService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_material_cfg-批量删除")
	@ApiOperation(value="bhz_material_cfg-批量删除", notes="bhz_material_cfg-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzMaterialCfgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_material_cfg-通过id查询")
	@ApiOperation(value="bhz_material_cfg-通过id查询", notes="bhz_material_cfg-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzMaterialCfg bhzMaterialCfg = bhzMaterialCfgService.getById(id);
		if(bhzMaterialCfg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzMaterialCfg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzMaterialCfg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzMaterialCfg bhzMaterialCfg) {
        return super.exportXls(request, bhzMaterialCfg, BhzMaterialCfg.class, "bhz_material_cfg");
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
        return super.importExcel(request, response, BhzMaterialCfg.class);
    }

}
