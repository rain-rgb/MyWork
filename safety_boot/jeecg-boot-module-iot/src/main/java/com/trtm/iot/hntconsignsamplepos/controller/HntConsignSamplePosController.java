package com.trtm.iot.hntconsignsamplepos.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hntconsignsamplepos.entity.HntConsignSamplePos;
import com.trtm.iot.hntconsignsamplepos.service.IHntConsignSamplePosService;

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
 * @Description: 混凝土见证取样货架表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
@Api(tags="混凝土见证取样货架表信息")
@RestController
@RequestMapping("/hntconsignsamplepos/hntConsignSamplePos")
@Slf4j
public class HntConsignSamplePosController extends JeecgController<HntConsignSamplePos, IHntConsignSamplePosService> {
	@Autowired
	private IHntConsignSamplePosService hntConsignSamplePosService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param hntConsignSamplePos
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样货架表信息-分页列表查询")
	@ApiOperation(value="混凝土见证取样货架表信息-分页列表查询", notes="混凝土见证取样货架表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HntConsignSamplePos hntConsignSamplePos,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
//		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
//			hntConsignSamplePos.setSysOrgCode(sys_depart_orgcode + "*");
//		}
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (hntConsignSamplePos.getTemperatureid() == null) {
			if (!"null".equals(shebei)) {
				hntConsignSamplePos.setTemperatureid(shebei);
			}else {
				hntConsignSamplePos.setTemperatureid("空");
			}
		}
		QueryWrapper<HntConsignSamplePos> queryWrapper = QueryGenerator.initQueryWrapper(hntConsignSamplePos, req.getParameterMap());
		Page<HntConsignSamplePos> page = new Page<HntConsignSamplePos>(pageNo, pageSize);
		IPage<HntConsignSamplePos> pageList = hntConsignSamplePosService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param hntConsignSamplePos
	  * @param
	  * @param
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "混凝土见证取样货架表信息-查询")
	 @ApiOperation(value="混凝土见证取样货架表信息-查询", notes="混凝土见证取样货架表信息-查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(HntConsignSamplePos hntConsignSamplePos,String sys_depart_orgcode,
									HttpServletRequest req) {
//		 if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {//如果想要全局组织机构控制所显示的数据要加上这个
//			 hntConsignSamplePos.setSysOrgCode(sys_depart_orgcode + "*");
//		 }
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (hntConsignSamplePos.getTemperatureid() == null) {
			 if (!"null".equals(shebei)) {
				 hntConsignSamplePos.setTemperatureid(shebei);
			 }else {
				 hntConsignSamplePos.setTemperatureid("空");
			 }
		 }
		 QueryWrapper<HntConsignSamplePos> queryWrapper = QueryGenerator.initQueryWrapper(hntConsignSamplePos, req.getParameterMap());
		 List<HntConsignSamplePos> pageList = hntConsignSamplePosService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param hntConsignSamplePos
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样货架表信息-添加")
	@ApiOperation(value="混凝土见证取样货架表信息-添加", notes="混凝土见证取样货架表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HntConsignSamplePos hntConsignSamplePos) {
		hntConsignSamplePosService.save(hntConsignSamplePos);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hntConsignSamplePos
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样货架表信息-编辑")
	@ApiOperation(value="混凝土见证取样货架表信息-编辑", notes="混凝土见证取样货架表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HntConsignSamplePos hntConsignSamplePos) {
		hntConsignSamplePosService.updateById(hntConsignSamplePos);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样货架表信息-通过id删除")
	@ApiOperation(value="混凝土见证取样货架表信息-通过id删除", notes="混凝土见证取样货架表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hntConsignSamplePosService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样货架表信息-批量删除")
	@ApiOperation(value="混凝土见证取样货架表信息-批量删除", notes="混凝土见证取样货架表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hntConsignSamplePosService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样货架表信息-通过id查询")
	@ApiOperation(value="混凝土见证取样货架表信息-通过id查询", notes="混凝土见证取样货架表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HntConsignSamplePos hntConsignSamplePos = hntConsignSamplePosService.getById(id);
		if(hntConsignSamplePos==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hntConsignSamplePos);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hntConsignSamplePos
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HntConsignSamplePos hntConsignSamplePos) {
        return super.exportXls(request, hntConsignSamplePos, HntConsignSamplePos.class, "混凝土见证取样货架表信息");
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
        return super.importExcel(request, response, HntConsignSamplePos.class);
    }

}
