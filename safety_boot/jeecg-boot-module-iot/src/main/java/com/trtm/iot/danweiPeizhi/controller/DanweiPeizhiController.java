package com.trtm.iot.danweiPeizhi.controller;

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
import com.trtm.iot.danweiPeizhi.entity.DanweiPeizhi;
import com.trtm.iot.danweiPeizhi.service.IDanweiPeizhiService;

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
 * @Description: danwei_peizhi
 * @Author: jeecg-boot
 * @Date:   2022-11-08
 * @Version: V1.0
 */
@Api(tags="danwei_peizhi")
@RestController
@RequestMapping("/danweiPeizhi/danweiPeizhi")
@Slf4j
public class DanweiPeizhiController extends JeecgController<DanweiPeizhi, IDanweiPeizhiService> {
	@Autowired
	private IDanweiPeizhiService danweiPeizhiService;
	
	/**
	 * 分页列表查询
	 *
	 * @param danweiPeizhi
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "danwei_peizhi-分页列表查询")
	@ApiOperation(value="danwei_peizhi-分页列表查询", notes="danwei_peizhi-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DanweiPeizhi danweiPeizhi,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DanweiPeizhi> queryWrapper = QueryGenerator.initQueryWrapper(danweiPeizhi, req.getParameterMap());
		Page<DanweiPeizhi> page = new Page<DanweiPeizhi>(pageNo, pageSize);
		IPage<DanweiPeizhi> pageList = danweiPeizhiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param danweiPeizhi
	 * @return
	 */
	@AutoLog(value = "danwei_peizhi-添加")
	@ApiOperation(value="danwei_peizhi-添加", notes="danwei_peizhi-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DanweiPeizhi danweiPeizhi) {
		danweiPeizhiService.save(danweiPeizhi);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param danweiPeizhi
	 * @return
	 */
	@AutoLog(value = "danwei_peizhi-编辑")
	@ApiOperation(value="danwei_peizhi-编辑", notes="danwei_peizhi-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DanweiPeizhi danweiPeizhi) {
		danweiPeizhiService.updateById(danweiPeizhi);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "danwei_peizhi-通过id删除")
	@ApiOperation(value="danwei_peizhi-通过id删除", notes="danwei_peizhi-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		danweiPeizhiService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "danwei_peizhi-批量删除")
	@ApiOperation(value="danwei_peizhi-批量删除", notes="danwei_peizhi-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.danweiPeizhiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "danwei_peizhi-通过id查询")
	@ApiOperation(value="danwei_peizhi-通过id查询", notes="danwei_peizhi-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DanweiPeizhi danweiPeizhi = danweiPeizhiService.getById(id);
		if(danweiPeizhi==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(danweiPeizhi);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param danweiPeizhi
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DanweiPeizhi danweiPeizhi) {
        return super.exportXls(request, danweiPeizhi, DanweiPeizhi.class, "danwei_peizhi");
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
        return super.importExcel(request, response, DanweiPeizhi.class);
    }

}
