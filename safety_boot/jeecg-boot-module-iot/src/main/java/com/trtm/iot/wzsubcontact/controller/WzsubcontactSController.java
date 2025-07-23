package com.trtm.iot.wzsubcontact.controller;

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
import com.trtm.iot.wzsubcontact.entity.WzsubcontactS;
import com.trtm.iot.wzsubcontact.service.IWzsubcontactSService;

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
 * @Description: wzsubcontact_s
 * @Author: jeecg-boot
 * @Date:   2023-10-16
 * @Version: V1.0
 */
@Api(tags="wzsubcontact_s")
@RestController
@RequestMapping("/wzsubcontact/wzsubcontactS")
@Slf4j
public class WzsubcontactSController extends JeecgController<WzsubcontactS, IWzsubcontactSService> {
	@Autowired
	private IWzsubcontactSService wzsubcontactSService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wzsubcontactS
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wzsubcontact_s-分页列表查询")
	@ApiOperation(value="wzsubcontact_s-分页列表查询", notes="wzsubcontact_s-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WzsubcontactS wzsubcontactS,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WzsubcontactS> queryWrapper = QueryGenerator.initQueryWrapper(wzsubcontactS, req.getParameterMap());
		Page<WzsubcontactS> page = new Page<WzsubcontactS>(pageNo, pageSize);
		IPage<WzsubcontactS> pageList = wzsubcontactSService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wzsubcontactS
	 * @return
	 */
	@AutoLog(value = "wzsubcontact_s-添加")
	@ApiOperation(value="wzsubcontact_s-添加", notes="wzsubcontact_s-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WzsubcontactS wzsubcontactS) {
		wzsubcontactSService.save(wzsubcontactS);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wzsubcontactS
	 * @return
	 */
	@AutoLog(value = "wzsubcontact_s-编辑")
	@ApiOperation(value="wzsubcontact_s-编辑", notes="wzsubcontact_s-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WzsubcontactS wzsubcontactS) {
		wzsubcontactSService.updateById(wzsubcontactS);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzsubcontact_s-通过id删除")
	@ApiOperation(value="wzsubcontact_s-通过id删除", notes="wzsubcontact_s-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzsubcontactSService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wzsubcontact_s-批量删除")
	@ApiOperation(value="wzsubcontact_s-批量删除", notes="wzsubcontact_s-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzsubcontactSService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzsubcontact_s-通过id查询")
	@ApiOperation(value="wzsubcontact_s-通过id查询", notes="wzsubcontact_s-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WzsubcontactS wzsubcontactS = wzsubcontactSService.getById(id);
		if(wzsubcontactS==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzsubcontactS);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzsubcontactS
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzsubcontactS wzsubcontactS) {
        return super.exportXls(request, wzsubcontactS, WzsubcontactS.class, "wzsubcontact_s");
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
        return super.importExcel(request, response, WzsubcontactS.class);
    }

}
