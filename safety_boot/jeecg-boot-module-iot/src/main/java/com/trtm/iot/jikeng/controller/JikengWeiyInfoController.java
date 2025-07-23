package com.trtm.iot.jikeng.controller;

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
import com.trtm.iot.jikeng.entity.JikengWeiyInfo;
import com.trtm.iot.jikeng.service.IJikengWeiyInfoService;

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
 * @Description: jikeng_weiy_info
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@Api(tags="jikeng_weiy_info")
@RestController
@RequestMapping("/jikeng/jikengWeiyInfo")
@Slf4j
public class JikengWeiyInfoController extends JeecgController<JikengWeiyInfo, IJikengWeiyInfoService> {
	@Autowired
	private IJikengWeiyInfoService jikengWeiyInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param jikengWeiyInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_info-分页列表查询")
	@ApiOperation(value="jikeng_weiy_info-分页列表查询", notes="jikeng_weiy_info-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(JikengWeiyInfo jikengWeiyInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<JikengWeiyInfo> queryWrapper = QueryGenerator.initQueryWrapper(jikengWeiyInfo, req.getParameterMap());
		Page<JikengWeiyInfo> page = new Page<JikengWeiyInfo>(pageNo, pageSize);
		IPage<JikengWeiyInfo> pageList = jikengWeiyInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param jikengWeiyInfo
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_info-添加")
	@ApiOperation(value="jikeng_weiy_info-添加", notes="jikeng_weiy_info-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JikengWeiyInfo jikengWeiyInfo) {
		jikengWeiyInfoService.save(jikengWeiyInfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jikengWeiyInfo
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_info-编辑")
	@ApiOperation(value="jikeng_weiy_info-编辑", notes="jikeng_weiy_info-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JikengWeiyInfo jikengWeiyInfo) {
		jikengWeiyInfoService.updateById(jikengWeiyInfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_info-通过id删除")
	@ApiOperation(value="jikeng_weiy_info-通过id删除", notes="jikeng_weiy_info-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		jikengWeiyInfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_info-批量删除")
	@ApiOperation(value="jikeng_weiy_info-批量删除", notes="jikeng_weiy_info-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.jikengWeiyInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_info-通过id查询")
	@ApiOperation(value="jikeng_weiy_info-通过id查询", notes="jikeng_weiy_info-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		JikengWeiyInfo jikengWeiyInfo = jikengWeiyInfoService.getById(id);
		if(jikengWeiyInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(jikengWeiyInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param jikengWeiyInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, JikengWeiyInfo jikengWeiyInfo) {
        return super.exportXls(request, jikengWeiyInfo, JikengWeiyInfo.class, "jikeng_weiy_info");
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
        return super.importExcel(request, response, JikengWeiyInfo.class);
    }

}
