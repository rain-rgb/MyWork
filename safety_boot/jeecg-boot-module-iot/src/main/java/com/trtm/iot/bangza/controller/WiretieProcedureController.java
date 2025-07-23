package com.trtm.iot.bangza.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bangza.entity.WiretieProcedure;
import com.trtm.iot.bangza.service.IWiretieProcedureService;

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
 * @Description: 钢筋绑扎工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Api(tags="钢筋绑扎工序表信息")
@RestController
@RequestMapping("/bangza/wiretieProcedure")
@Slf4j
public class WiretieProcedureController extends JeecgController<WiretieProcedure, IWiretieProcedureService> {
	@Autowired
	private IWiretieProcedureService wiretieProcedureService;
	 @Autowired
	 private IZhiliangrenwudanService zhiliangrenwudanService;
	/**
	 * 分页列表查询
	 *
	 * @param wiretieProcedure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "钢筋绑扎工序表信息-分页列表查询")
	@ApiOperation(value="钢筋绑扎工序表信息-分页列表查询", notes="钢筋绑扎工序表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WiretieProcedure wiretieProcedure,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WiretieProcedure> queryWrapper = QueryGenerator.initQueryWrapper(wiretieProcedure, req.getParameterMap());
		Page<WiretieProcedure> page = new Page<WiretieProcedure>(pageNo, pageSize);
		IPage<WiretieProcedure> pageList = wiretieProcedureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wiretieProcedure
	 * @return
	 */
	@AutoLog(value = "钢筋绑扎工序表信息-添加")
	@ApiOperation(value="钢筋绑扎工序表信息-添加", notes="钢筋绑扎工序表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WiretieProcedure wiretieProcedure) {
		wiretieProcedureService.save(wiretieProcedure);
		return Result.OK("添加成功！");
	}

	 /**
	  *   钢筋绑扎确认
	  *
	  * @param wiretieProcedure
	  * @return
	  */
	 @AutoLog(value = "钢筋绑扎工序表信息-钢筋绑扎确认")
	 @ApiOperation(value="钢筋绑扎工序表信息-钢筋绑扎确认", notes="钢筋绑扎工序表信息-钢筋绑扎确认")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody WiretieProcedure wiretieProcedure) {
		 Zhiliangrenwudan zhiliangrenwudan=new Zhiliangrenwudan();
		 QueryWrapper<Zhiliangrenwudan> queryWrapper=new QueryWrapper<>();
		 queryWrapper.eq("uuid",wiretieProcedure.getUuid());
		 Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
		 if(one==null){
			 return Result.error("钢筋绑扎确认失败!");
		 }else{
			 zhiliangrenwudan.setId(one.getId());
			 zhiliangrenwudan.setWiretiestatus(2);
			 zhiliangrenwudanService.updateById(zhiliangrenwudan);
			 wiretieProcedure.setStarttime(one.getProductiontime());
			 wiretieProcedure.setStatus(2);
			 wiretieProcedureService.save(wiretieProcedure);
			 return Result.OK("钢筋绑扎确认成功!");
		 }
	 }



	/**
	 *  编辑
	 *
	 * @param wiretieProcedure
	 * @return
	 */
	@AutoLog(value = "钢筋绑扎工序表信息-编辑")
	@ApiOperation(value="钢筋绑扎工序表信息-编辑", notes="钢筋绑扎工序表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WiretieProcedure wiretieProcedure) {
		wiretieProcedureService.updateById(wiretieProcedure);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "钢筋绑扎工序表信息-通过id删除")
	@ApiOperation(value="钢筋绑扎工序表信息-通过id删除", notes="钢筋绑扎工序表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wiretieProcedureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "钢筋绑扎工序表信息-批量删除")
	@ApiOperation(value="钢筋绑扎工序表信息-批量删除", notes="钢筋绑扎工序表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wiretieProcedureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "钢筋绑扎工序表信息-通过id查询")
	@ApiOperation(value="钢筋绑扎工序表信息-通过id查询", notes="钢筋绑扎工序表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WiretieProcedure wiretieProcedure = wiretieProcedureService.getById(id);
		if(wiretieProcedure==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wiretieProcedure);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wiretieProcedure
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WiretieProcedure wiretieProcedure) {
        return super.exportXls(request, wiretieProcedure, WiretieProcedure.class, "钢筋绑扎工序表信息");
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
        return super.importExcel(request, response, WiretieProcedure.class);
    }

}
