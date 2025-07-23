package com.trtm.iot.lmjob.controller;

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
import com.trtm.iot.lmjob.entity.LmSyInfo;
import com.trtm.iot.lmjob.service.ILmSyInfoService;

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
 * @Description: lm_sy_info
 * @Author: jeecg-boot
 * @Date:   2024-07-04
 * @Version: V1.0
 */
@Api(tags="lm_sy_info")
@RestController
@RequestMapping("/lmjob/lmSyInfo")
@Slf4j
public class LmSyInfoController extends JeecgController<LmSyInfo, ILmSyInfoService> {
	@Autowired
	private ILmSyInfoService lmSyInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param lmSyInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "lm_sy_info-分页列表查询")
	@ApiOperation(value="lm_sy_info-分页列表查询", notes="lm_sy_info-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(LmSyInfo lmSyInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LmSyInfo> queryWrapper = QueryGenerator.initQueryWrapper(lmSyInfo, req.getParameterMap());
		Page<LmSyInfo> page = new Page<LmSyInfo>(pageNo, pageSize);
		IPage<LmSyInfo> pageList = lmSyInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param lmSyInfo
	 * @return
	 */
	@AutoLog(value = "lm_sy_info-添加")
	@ApiOperation(value="lm_sy_info-添加", notes="lm_sy_info-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody LmSyInfo lmSyInfo) {
		lmSyInfoService.save(lmSyInfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param lmSyInfo
	 * @return
	 */
	@AutoLog(value = "lm_sy_info-编辑")
	@ApiOperation(value="lm_sy_info-编辑", notes="lm_sy_info-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody LmSyInfo lmSyInfo) {
		lmSyInfoService.updateById(lmSyInfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "lm_sy_info-通过id删除")
	@ApiOperation(value="lm_sy_info-通过id删除", notes="lm_sy_info-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		lmSyInfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "lm_sy_info-批量删除")
	@ApiOperation(value="lm_sy_info-批量删除", notes="lm_sy_info-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.lmSyInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "lm_sy_info-通过id查询")
	@ApiOperation(value="lm_sy_info-通过id查询", notes="lm_sy_info-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		LmSyInfo lmSyInfo = lmSyInfoService.getById(id);
		if(lmSyInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(lmSyInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param lmSyInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LmSyInfo lmSyInfo) {
        return super.exportXls(request, lmSyInfo, LmSyInfo.class, "lm_sy_info");
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
        return super.importExcel(request, response, LmSyInfo.class);
    }

}
