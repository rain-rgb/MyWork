package com.trtm.iot.comparisonrecord.controller;

import java.util.Arrays;
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
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.comparisonrecord.entity.ComparisonRecord;
import com.trtm.iot.comparisonrecord.service.IComparisonRecordService;

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
 * @Description: 评比记录表
 * @Author: jeecg-boot
 * @Date:   2022-07-12
 * @Version: V1.0
 */
@Api(tags="评比记录表")
@RestController
@RequestMapping("/comparisonrecord/comparisonRecord")
@Slf4j
public class ComparisonRecordController extends JeecgController<ComparisonRecord, IComparisonRecordService> {
	@Autowired
	private IComparisonRecordService comparisonRecordService;

	/**
	 * 分页列表查询
	 *
	 * @param comparisonRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "评比记录表-分页列表查询")
	@ApiOperation(value="评比记录表-分页列表查询", notes="评比记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ComparisonRecord comparisonRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			comparisonRecord.setSysOrgCode(sys_depart_orgcode + "*");
		} else {
			comparisonRecord.setSysOrgCode(loginUser.getOrgCode() + "*");
		}
		QueryWrapper<ComparisonRecord> queryWrapper = QueryGenerator.initQueryWrapper(comparisonRecord, req.getParameterMap());
		Page<ComparisonRecord> page = new Page<ComparisonRecord>(pageNo, pageSize);
		IPage<ComparisonRecord> pageList = comparisonRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param comparisonRecord
	 * @return
	 */
	@AutoLog(value = "评比记录表-添加")
	@ApiOperation(value="评比记录表-添加", notes="评比记录表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ComparisonRecord comparisonRecord) {
		comparisonRecordService.save(comparisonRecord);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param comparisonRecord
	 * @return
	 */
	@AutoLog(value = "评比记录表-编辑")
	@ApiOperation(value="评比记录表-编辑", notes="评比记录表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ComparisonRecord comparisonRecord) {
		comparisonRecordService.updateById(comparisonRecord);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "评比记录表-通过id删除")
	@ApiOperation(value="评比记录表-通过id删除", notes="评比记录表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		comparisonRecordService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "评比记录表-批量删除")
	@ApiOperation(value="评比记录表-批量删除", notes="评比记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.comparisonRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "评比记录表-通过id查询")
	@ApiOperation(value="评比记录表-通过id查询", notes="评比记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ComparisonRecord comparisonRecord = comparisonRecordService.getById(id);
		if(comparisonRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(comparisonRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param comparisonRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ComparisonRecord comparisonRecord) {
        return super.exportXls(request, comparisonRecord, ComparisonRecord.class, "评比记录表");
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
        return super.importExcel(request, response, ComparisonRecord.class);
    }

}
