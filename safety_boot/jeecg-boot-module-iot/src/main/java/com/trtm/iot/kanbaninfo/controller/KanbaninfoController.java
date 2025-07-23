package com.trtm.iot.kanbaninfo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.kanbaninfo.entity.Kanbaninfo;
import com.trtm.iot.kanbaninfo.service.IKanbaninfoService;

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
 * @Description: kanbaninfo
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Api(tags="kanbaninfo")
@RestController
@RequestMapping("/kanbaninfo/kanbaninfo")
@Slf4j
public class KanbaninfoController extends JeecgController<Kanbaninfo, IKanbaninfoService> {
	@Autowired
	private IKanbaninfoService kanbaninfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param kanbaninfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "kanbaninfo-分页列表查询")
	@ApiOperation(value="kanbaninfo-分页列表查询", notes="kanbaninfo-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Kanbaninfo kanbaninfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,String sys_depart_orgcode) {
		if(StringUtils.isNotBlank(sys_depart_orgcode)) kanbaninfo.setSysOrgCode(sys_depart_orgcode);
		QueryWrapper<Kanbaninfo> queryWrapper = QueryGenerator.initQueryWrapper(kanbaninfo, req.getParameterMap());
		Page<Kanbaninfo> page = new Page<Kanbaninfo>(pageNo, pageSize);
		IPage<Kanbaninfo> pageList = kanbaninfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param kanbaninfo
	 * @return
	 */
	@AutoLog(value = "kanbaninfo-添加")
	@ApiOperation(value="kanbaninfo-添加", notes="kanbaninfo-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Kanbaninfo kanbaninfo) {
		kanbaninfoService.save(kanbaninfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param kanbaninfo
	 * @return
	 */
	@AutoLog(value = "kanbaninfo-编辑")
	@ApiOperation(value="kanbaninfo-编辑", notes="kanbaninfo-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Kanbaninfo kanbaninfo) {
		kanbaninfoService.updateById(kanbaninfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "kanbaninfo-通过id删除")
	@ApiOperation(value="kanbaninfo-通过id删除", notes="kanbaninfo-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		kanbaninfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "kanbaninfo-批量删除")
	@ApiOperation(value="kanbaninfo-批量删除", notes="kanbaninfo-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kanbaninfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "kanbaninfo-通过id查询")
	@ApiOperation(value="kanbaninfo-通过id查询", notes="kanbaninfo-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Kanbaninfo kanbaninfo = kanbaninfoService.getById(id);
		if(kanbaninfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kanbaninfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param kanbaninfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Kanbaninfo kanbaninfo) {
        return super.exportXls(request, kanbaninfo, Kanbaninfo.class, "kanbaninfo");
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
        return super.importExcel(request, response, Kanbaninfo.class);
    }

}
