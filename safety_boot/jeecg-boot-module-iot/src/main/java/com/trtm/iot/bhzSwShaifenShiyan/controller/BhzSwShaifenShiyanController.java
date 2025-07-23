package com.trtm.iot.bhzSwShaifenShiyan.controller;

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
import com.trtm.iot.bhzSwShaifenShiyan.entity.BhzSwShaifenShiyan;
import com.trtm.iot.bhzSwShaifenShiyan.service.IBhzSwShaifenShiyanService;

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
 * @Description: bhz_sw_shaifen_shiyan
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
@Api(tags="bhz_sw_shaifen_shiyan")
@RestController
@RequestMapping("/bhzSwShaifenShiyan/bhzSwShaifenShiyan")
@Slf4j
public class BhzSwShaifenShiyanController extends JeecgController<BhzSwShaifenShiyan, IBhzSwShaifenShiyanService> {
	@Autowired
	private IBhzSwShaifenShiyanService bhzSwShaifenShiyanService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bhzSwShaifenShiyan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_sw_shaifen_shiyan-分页列表查询")
	@ApiOperation(value="bhz_sw_shaifen_shiyan-分页列表查询", notes="bhz_sw_shaifen_shiyan-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzSwShaifenShiyan bhzSwShaifenShiyan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzSwShaifenShiyan> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwShaifenShiyan, req.getParameterMap());
		Page<BhzSwShaifenShiyan> page = new Page<BhzSwShaifenShiyan>(pageNo, pageSize);
		IPage<BhzSwShaifenShiyan> pageList = bhzSwShaifenShiyanService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bhzSwShaifenShiyan
	 * @return
	 */
	@AutoLog(value = "bhz_sw_shaifen_shiyan-添加")
	@ApiOperation(value="bhz_sw_shaifen_shiyan-添加", notes="bhz_sw_shaifen_shiyan-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzSwShaifenShiyan bhzSwShaifenShiyan) {
		bhzSwShaifenShiyanService.save(bhzSwShaifenShiyan);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bhzSwShaifenShiyan
	 * @return
	 */
	@AutoLog(value = "bhz_sw_shaifen_shiyan-编辑")
	@ApiOperation(value="bhz_sw_shaifen_shiyan-编辑", notes="bhz_sw_shaifen_shiyan-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzSwShaifenShiyan bhzSwShaifenShiyan) {
		bhzSwShaifenShiyanService.updateById(bhzSwShaifenShiyan);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_sw_shaifen_shiyan-通过id删除")
	@ApiOperation(value="bhz_sw_shaifen_shiyan-通过id删除", notes="bhz_sw_shaifen_shiyan-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzSwShaifenShiyanService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_sw_shaifen_shiyan-批量删除")
	@ApiOperation(value="bhz_sw_shaifen_shiyan-批量删除", notes="bhz_sw_shaifen_shiyan-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzSwShaifenShiyanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_sw_shaifen_shiyan-通过id查询")
	@ApiOperation(value="bhz_sw_shaifen_shiyan-通过id查询", notes="bhz_sw_shaifen_shiyan-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzSwShaifenShiyan bhzSwShaifenShiyan = bhzSwShaifenShiyanService.getById(id);
		if(bhzSwShaifenShiyan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzSwShaifenShiyan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzSwShaifenShiyan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzSwShaifenShiyan bhzSwShaifenShiyan) {
        return super.exportXls(request, bhzSwShaifenShiyan, BhzSwShaifenShiyan.class, "bhz_sw_shaifen_shiyan");
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
        return super.importExcel(request, response, BhzSwShaifenShiyan.class);
    }

}
