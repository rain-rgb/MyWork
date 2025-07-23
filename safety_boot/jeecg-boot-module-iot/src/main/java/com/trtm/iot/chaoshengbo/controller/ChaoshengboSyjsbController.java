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
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyjsb;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSyjsbService;

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
 * @Description: chaoshengbo_syjsb
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Api(tags="chaoshengbo_syjsb")
@RestController
@RequestMapping("/chaoshengbo/chaoshengboSyjsb")
@Slf4j
public class ChaoshengboSyjsbController extends JeecgController<ChaoshengboSyjsb, IChaoshengboSyjsbService> {
	@Autowired
	private IChaoshengboSyjsbService chaoshengboSyjsbService;

	/**
	 * 分页列表查询
	 *
	 * @param chaoshengboSyjsb
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syjsb-分页列表查询")
	@ApiOperation(value="chaoshengbo_syjsb-分页列表查询", notes="chaoshengbo_syjsb-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ChaoshengboSyjsb chaoshengboSyjsb,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ChaoshengboSyjsb> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSyjsb, req.getParameterMap());
		Page<ChaoshengboSyjsb> page = new Page<ChaoshengboSyjsb>(pageNo, pageSize);
		IPage<ChaoshengboSyjsb> pageList = chaoshengboSyjsbService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param chaoshengboSyjsb
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syjsb-添加")
	@ApiOperation(value="chaoshengbo_syjsb-添加", notes="chaoshengbo_syjsb-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ChaoshengboSyjsb chaoshengboSyjsb) {
		chaoshengboSyjsbService.save(chaoshengboSyjsb);
		return Result.OK("添加成功！");
	}

	 /**
	  *   chaoshengbo_syjsb添加数据对外开放
	  *
	  * @param chaoshengboSyjsb
	  * @return
	  */
	 @AutoLog(value = "chaoshengbo_syjsb-添加")
	 @ApiOperation(value="chaoshengbo_syjsb-添加", notes="chaoshengbo_syjsb-添加")
	 @PostMapping(value = "/addOpen")
	 public Result<?> addOpen(@RequestBody ChaoshengboSyjsb chaoshengboSyjsb) {
		 chaoshengboSyjsbService.save(chaoshengboSyjsb);
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param chaoshengboSyjsb
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syjsb-编辑")
	@ApiOperation(value="chaoshengbo_syjsb-编辑", notes="chaoshengbo_syjsb-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ChaoshengboSyjsb chaoshengboSyjsb) {
		chaoshengboSyjsbService.updateById(chaoshengboSyjsb);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syjsb-通过id删除")
	@ApiOperation(value="chaoshengbo_syjsb-通过id删除", notes="chaoshengbo_syjsb-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		chaoshengboSyjsbService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syjsb-批量删除")
	@ApiOperation(value="chaoshengbo_syjsb-批量删除", notes="chaoshengbo_syjsb-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.chaoshengboSyjsbService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "chaoshengbo_syjsb-通过id查询")
	@ApiOperation(value="chaoshengbo_syjsb-通过id查询", notes="chaoshengbo_syjsb-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ChaoshengboSyjsb chaoshengboSyjsb = chaoshengboSyjsbService.getById(id);
		if(chaoshengboSyjsb==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(chaoshengboSyjsb);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param chaoshengboSyjsb
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ChaoshengboSyjsb chaoshengboSyjsb) {
        return super.exportXls(request, chaoshengboSyjsb, ChaoshengboSyjsb.class, "chaoshengbo_syjsb");
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
        return super.importExcel(request, response, ChaoshengboSyjsb.class);
    }

}
