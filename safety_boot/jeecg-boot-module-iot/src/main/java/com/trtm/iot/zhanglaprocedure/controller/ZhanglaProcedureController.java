package com.trtm.iot.zhanglaprocedure.controller;

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
import com.trtm.iot.zhanglaprocedure.entity.ZhanglaProcedure;
import com.trtm.iot.zhanglaprocedure.service.IZhanglaProcedureService;

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
 * @Description: 张拉工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Api(tags="张拉工序表信息")
@RestController
@RequestMapping("/zhanglaprocedure/zhanglaProcedure")
@Slf4j
public class ZhanglaProcedureController extends JeecgController<ZhanglaProcedure, IZhanglaProcedureService> {
	@Autowired
	private IZhanglaProcedureService zhanglaProcedureService;
	 @Autowired
	 private IZhiliangrenwudanService zhiliangrenwudanService;
	/**
	 * 分页列表查询
	 *
	 * @param zhanglaProcedure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "张拉工序表信息-分页列表查询")
	@ApiOperation(value="张拉工序表信息-分页列表查询", notes="张拉工序表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZhanglaProcedure zhanglaProcedure,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZhanglaProcedure> queryWrapper = QueryGenerator.initQueryWrapper(zhanglaProcedure, req.getParameterMap());
		Page<ZhanglaProcedure> page = new Page<ZhanglaProcedure>(pageNo, pageSize);
		IPage<ZhanglaProcedure> pageList = zhanglaProcedureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param zhanglaProcedure
	 * @return
	 */
	@AutoLog(value = "张拉工序表信息-添加")
	@ApiOperation(value="张拉工序表信息-添加", notes="张拉工序表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZhanglaProcedure zhanglaProcedure) {
		zhanglaProcedureService.save(zhanglaProcedure);
		return Result.OK("添加成功！");
	}


	 /**
	  *   张拉确认
	  *
	  * @param zhanglaProcedure
	  * @return
	  */
	 @AutoLog(value = "张拉工序表信息-张拉确认")
	 @ApiOperation(value="张拉工序表信息-张拉确认", notes="张拉工序表信息-张拉确认")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody ZhanglaProcedure zhanglaProcedure) {
		 Zhiliangrenwudan zhiliangrenwudan=new Zhiliangrenwudan();
		 QueryWrapper<Zhiliangrenwudan> queryWrapper=new QueryWrapper<>();
		 queryWrapper.eq("uuid",zhanglaProcedure.getUuid());
		 Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
		 if(one==null){
			 return Result.error("张拉确认失败!");
		 }else {
			 zhiliangrenwudan.setId(one.getId());
			 zhiliangrenwudan.setZhanglastatus(2);
			 zhiliangrenwudanService.updateById(zhiliangrenwudan);
			 zhanglaProcedure.setStatus(2);
			 zhanglaProcedureService.save(zhanglaProcedure);
			 return Result.OK("张拉确认成功！");
		 }
	 }

	/**
	 *  编辑
	 *
	 * @param zhanglaProcedure
	 * @return
	 */
	@AutoLog(value = "张拉工序表信息-编辑")
	@ApiOperation(value="张拉工序表信息-编辑", notes="张拉工序表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZhanglaProcedure zhanglaProcedure) {
		zhanglaProcedureService.updateById(zhanglaProcedure);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉工序表信息-通过id删除")
	@ApiOperation(value="张拉工序表信息-通过id删除", notes="张拉工序表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zhanglaProcedureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "张拉工序表信息-批量删除")
	@ApiOperation(value="张拉工序表信息-批量删除", notes="张拉工序表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zhanglaProcedureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉工序表信息-通过id查询")
	@ApiOperation(value="张拉工序表信息-通过id查询", notes="张拉工序表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZhanglaProcedure zhanglaProcedure = zhanglaProcedureService.getById(id);
		if(zhanglaProcedure==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zhanglaProcedure);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zhanglaProcedure
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZhanglaProcedure zhanglaProcedure) {
        return super.exportXls(request, zhanglaProcedure, ZhanglaProcedure.class, "张拉工序表信息");
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
        return super.importExcel(request, response, ZhanglaProcedure.class);
    }

}
