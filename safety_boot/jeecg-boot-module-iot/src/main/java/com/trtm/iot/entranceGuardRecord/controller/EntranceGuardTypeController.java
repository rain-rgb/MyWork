package com.trtm.iot.entranceGuardRecord.controller;

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
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardType;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardTypeService;

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
 * @Description: entrance_guard_type
 * @Author: jeecg-boot
 * @Date:   2021-07-06
 * @Version: V1.0
 */
@Api(tags="entrance_guard_type")
@RestController
@RequestMapping("/entranceGuardRecord/entranceGuardType")
@Slf4j
public class EntranceGuardTypeController extends JeecgController<EntranceGuardType, IEntranceGuardTypeService> {
	@Autowired
	private IEntranceGuardTypeService entranceGuardTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param entranceGuardType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "entrance_guard_type-分页列表查询")
	@ApiOperation(value="entrance_guard_type-分页列表查询", notes="entrance_guard_type-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(EntranceGuardType entranceGuardType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
		Page<EntranceGuardType> page = new Page<EntranceGuardType>(pageNo, pageSize);
		IPage<EntranceGuardType> pageList = entranceGuardTypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param entranceGuardType
	 * @return
	 */
	@AutoLog(value = "entrance_guard_type-添加")
	@ApiOperation(value="entrance_guard_type-添加", notes="entrance_guard_type-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody EntranceGuardType entranceGuardType) {
		entranceGuardTypeService.save(entranceGuardType);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param entranceGuardType
	 * @return
	 */
	@AutoLog(value = "entrance_guard_type-编辑")
	@ApiOperation(value="entrance_guard_type-编辑", notes="entrance_guard_type-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody EntranceGuardType entranceGuardType) {
		entranceGuardTypeService.updateById(entranceGuardType);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "entrance_guard_type-通过id删除")
	@ApiOperation(value="entrance_guard_type-通过id删除", notes="entrance_guard_type-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		entranceGuardTypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "entrance_guard_type-批量删除")
	@ApiOperation(value="entrance_guard_type-批量删除", notes="entrance_guard_type-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.entranceGuardTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "entrance_guard_type-通过id查询")
	@ApiOperation(value="entrance_guard_type-通过id查询", notes="entrance_guard_type-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		EntranceGuardType entranceGuardType = entranceGuardTypeService.getById(id);
		if(entranceGuardType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(entranceGuardType);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param entranceGuardType
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EntranceGuardType entranceGuardType) {
        return super.exportXls(request, entranceGuardType, EntranceGuardType.class, "entrance_guard_type");
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
        return super.importExcel(request, response, EntranceGuardType.class);
    }

}
