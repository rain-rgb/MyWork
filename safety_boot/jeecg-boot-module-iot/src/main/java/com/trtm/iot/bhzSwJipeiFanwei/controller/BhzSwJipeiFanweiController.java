package com.trtm.iot.bhzSwJipeiFanwei.controller;

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
import com.trtm.iot.bhzSwJipeiFanwei.entity.BhzSwJipeiFanwei;
import com.trtm.iot.bhzSwJipeiFanwei.service.IBhzSwJipeiFanweiService;

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
 * @Description: bhz_sw_jipei_fanwei
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
@Api(tags="bhz_sw_jipei_fanwei")
@RestController
@RequestMapping("/bhzSwJipeiFanwei/bhzSwJipeiFanwei")
@Slf4j
public class BhzSwJipeiFanweiController extends JeecgController<BhzSwJipeiFanwei, IBhzSwJipeiFanweiService> {
	@Autowired
	private IBhzSwJipeiFanweiService bhzSwJipeiFanweiService;

	/**
	 * 分页列表查询
	 *
	 * @param bhzSwJipeiFanwei
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_fanwei-分页列表查询")
	@ApiOperation(value="bhz_sw_jipei_fanwei-分页列表查询", notes="bhz_sw_jipei_fanwei-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzSwJipeiFanwei bhzSwJipeiFanwei,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzSwJipeiFanwei> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwJipeiFanwei, req.getParameterMap());
		Page<BhzSwJipeiFanwei> page = new Page<BhzSwJipeiFanwei>(pageNo, pageSize);
		IPage<BhzSwJipeiFanwei> pageList = bhzSwJipeiFanweiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param bhzSwJipeiFanwei
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "bhz_sw_jipei_fanwei-分页列表查询")
	 @ApiOperation(value="bhz_sw_jipei_fanwei-分页列表查询", notes="bhz_sw_jipei_fanwei-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(BhzSwJipeiFanwei bhzSwJipeiFanwei,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<BhzSwJipeiFanwei> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwJipeiFanwei, req.getParameterMap());
		 List<BhzSwJipeiFanwei> pageList = bhzSwJipeiFanweiService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param bhzSwJipeiFanwei
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_fanwei-添加")
	@ApiOperation(value="bhz_sw_jipei_fanwei-添加", notes="bhz_sw_jipei_fanwei-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzSwJipeiFanwei bhzSwJipeiFanwei) {
		bhzSwJipeiFanweiService.save(bhzSwJipeiFanwei);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param bhzSwJipeiFanwei
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_fanwei-编辑")
	@ApiOperation(value="bhz_sw_jipei_fanwei-编辑", notes="bhz_sw_jipei_fanwei-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzSwJipeiFanwei bhzSwJipeiFanwei) {
		bhzSwJipeiFanweiService.updateById(bhzSwJipeiFanwei);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_fanwei-通过id删除")
	@ApiOperation(value="bhz_sw_jipei_fanwei-通过id删除", notes="bhz_sw_jipei_fanwei-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzSwJipeiFanweiService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_fanwei-批量删除")
	@ApiOperation(value="bhz_sw_jipei_fanwei-批量删除", notes="bhz_sw_jipei_fanwei-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzSwJipeiFanweiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_sw_jipei_fanwei-通过id查询")
	@ApiOperation(value="bhz_sw_jipei_fanwei-通过id查询", notes="bhz_sw_jipei_fanwei-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzSwJipeiFanwei bhzSwJipeiFanwei = bhzSwJipeiFanweiService.getById(id);
		if(bhzSwJipeiFanwei==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzSwJipeiFanwei);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzSwJipeiFanwei
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzSwJipeiFanwei bhzSwJipeiFanwei) {
        return super.exportXls(request, bhzSwJipeiFanwei, BhzSwJipeiFanwei.class, "bhz_sw_jipei_fanwei");
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
        return super.importExcel(request, response, BhzSwJipeiFanwei.class);
    }

}
