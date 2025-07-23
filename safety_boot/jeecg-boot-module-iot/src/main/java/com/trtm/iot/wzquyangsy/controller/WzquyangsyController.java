package com.trtm.iot.wzquyangsy.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzquyangsy.entity.Wzquyangsy;
import com.trtm.iot.wzquyangsy.service.IWzquyangsyService;

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
 * @Description: 原材料物资取样表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Api(tags="原材料物资取样表信息")
@RestController
@RequestMapping("/wzquyangsy/wzquyangsy")
@Slf4j
public class WzquyangsyController extends JeecgController<Wzquyangsy, IWzquyangsyService> {
	@Autowired
	private IWzquyangsyService wzquyangsyService;
	 @Autowired
	 private IWztaizhangService wztaizhangService;

	/**
	 * 分页列表查询
	 *
	 * @param wzquyangsy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "原材料物资取样表信息-分页列表查询")
	@ApiOperation(value="原材料物资取样表信息-分页列表查询", notes="原材料物资取样表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Wzquyangsy wzquyangsy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Wzquyangsy> queryWrapper = QueryGenerator.initQueryWrapper(wzquyangsy, req.getParameterMap());
		Page<Wzquyangsy> page = new Page<Wzquyangsy>(pageNo, pageSize);
		IPage<Wzquyangsy> pageList = wzquyangsyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wzquyangsy
	 * @return
	 */
	@AutoLog(value = "原材料物资取样表信息-添加")
	@ApiOperation(value="原材料物资取样表信息-添加", notes="原材料物资取样表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Wzquyangsy wzquyangsy) {
		wzquyangsyService.save(wzquyangsy);
		return Result.OK("添加成功！");
	}
	 /**
	  *   添加
	  *
	  * @param wzquyangsy
	  * @return
	  */
	 @AutoLog(value = "原材料物资取样表信息-添加")
	 @ApiOperation(value="原材料物资取样表信息-添加", notes="原材料物资取样表信息-添加")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody Wzquyangsy wzquyangsy) {
		 Date date=new Date();
		 DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 wzquyangsy.setQuyangshijian(format.format(date));
		 wzquyangsyService.save(wzquyangsy);
		 Wztaizhang tz = new Wztaizhang();
		 tz.setId(wzquyangsy.getTaizhangid());
		 tz.setIsquyang(1);
		 wztaizhangService.updateById(tz);
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param wzquyangsy
	 * @return
	 */
	@AutoLog(value = "原材料物资取样表信息-编辑")
	@ApiOperation(value="原材料物资取样表信息-编辑", notes="原材料物资取样表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Wzquyangsy wzquyangsy) {
		wzquyangsyService.updateById(wzquyangsy);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原材料物资取样表信息-通过id删除")
	@ApiOperation(value="原材料物资取样表信息-通过id删除", notes="原材料物资取样表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzquyangsyService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "原材料物资取样表信息-批量删除")
	@ApiOperation(value="原材料物资取样表信息-批量删除", notes="原材料物资取样表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzquyangsyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原材料物资取样表信息-通过id查询")
	@ApiOperation(value="原材料物资取样表信息-通过id查询", notes="原材料物资取样表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Wzquyangsy wzquyangsy = wzquyangsyService.getById(id);
		if(wzquyangsy==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzquyangsy);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzquyangsy
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzquyangsy wzquyangsy) {
        return super.exportXls(request, wzquyangsy, Wzquyangsy.class, "原材料物资取样表信息");
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
        return super.importExcel(request, response, Wzquyangsy.class);
    }

}
