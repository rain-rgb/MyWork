package com.trtm.iot.bhzlqjipeifanwei.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzlqjipeifanwei.entity.BhzLqJipeiFanwei;
import com.trtm.iot.bhzlqjipeifanwei.service.IBhzLqJipeiFanweiService;

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
 * @Description: 沥青级配范围配置表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
@Api(tags="沥青级配范围配置表")
@RestController
@RequestMapping("/bhzlqjipeifanwei/bhzLqJipeiFanwei")
@Slf4j
public class BhzLqJipeiFanweiController extends JeecgController<BhzLqJipeiFanwei, IBhzLqJipeiFanweiService> {
	@Autowired
	private IBhzLqJipeiFanweiService bhzLqJipeiFanweiService;

	/**
	 * 分页列表查询
	 *
	 * @param bhzLqJipeiFanwei
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "沥青级配范围配置表-分页列表查询")
	@ApiOperation(value="沥青级配范围配置表-分页列表查询", notes="沥青级配范围配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzLqJipeiFanwei bhzLqJipeiFanwei,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzLqJipeiFanwei> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqJipeiFanwei, req.getParameterMap());
		Page<BhzLqJipeiFanwei> page = new Page<BhzLqJipeiFanwei>(pageNo, pageSize);
		IPage<BhzLqJipeiFanwei> pageList = bhzLqJipeiFanweiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param bhzLqJipeiFanwei
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "沥青级配范围配置表-分页列表查询")
	 @ApiOperation(value="沥青级配范围配置表-分页列表查询", notes="沥青级配范围配置表-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(BhzLqJipeiFanwei bhzLqJipeiFanwei,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<BhzLqJipeiFanwei> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqJipeiFanwei, req.getParameterMap());
		 List<BhzLqJipeiFanwei> pageList = bhzLqJipeiFanweiService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param bhzLqJipeiFanwei
	 * @return
	 */
	@AutoLog(value = "沥青级配范围配置表-添加")
	@ApiOperation(value="沥青级配范围配置表-添加", notes="沥青级配范围配置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzLqJipeiFanwei bhzLqJipeiFanwei) {
		String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
		Date date = new Date();
		bhzLqJipeiFanwei.setUuid(uuid);
		bhzLqJipeiFanwei.setCreattime(date);
		bhzLqJipeiFanweiService.save(bhzLqJipeiFanwei);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param bhzLqJipeiFanwei
	 * @return
	 */
	@AutoLog(value = "沥青级配范围配置表-编辑")
	@ApiOperation(value="沥青级配范围配置表-编辑", notes="沥青级配范围配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzLqJipeiFanwei bhzLqJipeiFanwei) {
		bhzLqJipeiFanweiService.updateById(bhzLqJipeiFanwei);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "沥青级配范围配置表-通过id删除")
	@ApiOperation(value="沥青级配范围配置表-通过id删除", notes="沥青级配范围配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzLqJipeiFanweiService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "沥青级配范围配置表-批量删除")
	@ApiOperation(value="沥青级配范围配置表-批量删除", notes="沥青级配范围配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzLqJipeiFanweiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "沥青级配范围配置表-通过id查询")
	@ApiOperation(value="沥青级配范围配置表-通过id查询", notes="沥青级配范围配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzLqJipeiFanwei bhzLqJipeiFanwei = bhzLqJipeiFanweiService.getById(id);
		if(bhzLqJipeiFanwei==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzLqJipeiFanwei);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzLqJipeiFanwei
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzLqJipeiFanwei bhzLqJipeiFanwei) {
        return super.exportXls(request, bhzLqJipeiFanwei, BhzLqJipeiFanwei.class, "沥青级配范围配置表");
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
        return super.importExcel(request, response, BhzLqJipeiFanwei.class);
    }

}
