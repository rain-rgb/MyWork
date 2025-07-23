package com.trtm.iot.wzliaocang.controller;

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

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzliaocang.entity.WzliaocangXz;
import com.trtm.iot.wzliaocang.service.IWzliaocangXzService;

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
 * @Description: wzliaocang_xz
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
@Api(tags="wzliaocang_xz")
@RestController
@RequestMapping("/wzliaocang/wzliaocangXz")
@Slf4j
public class WzliaocangXzController extends JeecgController<WzliaocangXz, IWzliaocangXzService> {
	@Autowired
	private IWzliaocangXzService wzliaocangXzService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wzliaocangXz
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wzliaocang_xz-分页列表查询")
	@ApiOperation(value="wzliaocang_xz-分页列表查询", notes="wzliaocang_xz-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WzliaocangXz wzliaocangXz,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WzliaocangXz> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocangXz, req.getParameterMap());
		Page<WzliaocangXz> page = new Page<WzliaocangXz>(pageNo, pageSize);
		IPage<WzliaocangXz> pageList = wzliaocangXzService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wzliaocangXz
	 * @return
	 */
	@AutoLog(value = "wzliaocang_xz-添加")
	@ApiOperation(value="wzliaocang_xz-添加", notes="wzliaocang_xz-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WzliaocangXz wzliaocangXz) {
		wzliaocangXzService.add(wzliaocangXz);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wzliaocangXz
	 * @return
	 */
	@AutoLog(value = "wzliaocang_xz-编辑")
	@ApiOperation(value="wzliaocang_xz-编辑", notes="wzliaocang_xz-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WzliaocangXz wzliaocangXz) {
		wzliaocangXzService.updateById(wzliaocangXz);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzliaocang_xz-通过id删除")
	@ApiOperation(value="wzliaocang_xz-通过id删除", notes="wzliaocang_xz-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzliaocangXzService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wzliaocang_xz-批量删除")
	@ApiOperation(value="wzliaocang_xz-批量删除", notes="wzliaocang_xz-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzliaocangXzService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzliaocang_xz-通过id查询")
	@ApiOperation(value="wzliaocang_xz-通过id查询", notes="wzliaocang_xz-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WzliaocangXz wzliaocangXz = wzliaocangXzService.getById(id);
		if(wzliaocangXz==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzliaocangXz);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzliaocangXz
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzliaocangXz wzliaocangXz) {
        return super.exportXls(request, wzliaocangXz, WzliaocangXz.class, "wzliaocang_xz");
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
        return super.importExcel(request, response, WzliaocangXz.class);
    }

}
