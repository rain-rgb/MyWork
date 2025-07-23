package com.trtm.iot.gualan.controller;

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
import com.trtm.iot.gualan.entity.GualanBase;
import com.trtm.iot.gualan.service.IGualanBaseService;

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
 * @Description: 挂篮数据主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Api(tags="挂篮数据主表")
@RestController
@RequestMapping("/gualan/gualanBase")
@Slf4j
public class GualanBaseController extends JeecgController<GualanBase, IGualanBaseService> {
	@Autowired
	private IGualanBaseService gualanBaseService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param gualanBase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "挂篮数据主表-分页列表查询")
	@ApiOperation(value="挂篮数据主表-分页列表查询", notes="挂篮数据主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GualanBase gualanBase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (gualanBase.getShebeibanhao() == null) {
			if (!"null".equals(shebei)) {
				gualanBase.setShebeibanhao(shebei);
			}else {
				gualanBase.setShebeibanhao("空");
			}
		}
		QueryWrapper<GualanBase> queryWrapper = QueryGenerator.initQueryWrapper(gualanBase, req.getParameterMap());
		Page<GualanBase> page = new Page<GualanBase>(pageNo, pageSize);
		IPage<GualanBase> pageList = gualanBaseService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 /**
	  * 分页列表查询
	  *
	  * @param gualanBase
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "挂篮数据主表-分页列表查询")
	 @ApiOperation(value="挂篮数据主表-分页列表查询", notes="挂篮数据主表-分页列表查询")
	 @GetMapping(value = "/listOpen")
	 public Result<?> listOpen(GualanBase gualanBase,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<GualanBase> queryWrapper = QueryGenerator.initQueryWrapper(gualanBase, req.getParameterMap());
		 Page<GualanBase> page = new Page<GualanBase>(pageNo, pageSize);
		 IPage<GualanBase> pageList = gualanBaseService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param gualanBase
	 * @return
	 */
	@AutoLog(value = "挂篮数据主表-添加")
	@ApiOperation(value="挂篮数据主表-添加", notes="挂篮数据主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GualanBase gualanBase) {
		gualanBaseService.save(gualanBase);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param gualanBase
	 * @return
	 */
	@AutoLog(value = "挂篮数据主表-编辑")
	@ApiOperation(value="挂篮数据主表-编辑", notes="挂篮数据主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GualanBase gualanBase) {
		gualanBaseService.updateById(gualanBase);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "挂篮数据主表-通过id删除")
	@ApiOperation(value="挂篮数据主表-通过id删除", notes="挂篮数据主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		gualanBaseService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "挂篮数据主表-批量删除")
	@ApiOperation(value="挂篮数据主表-批量删除", notes="挂篮数据主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gualanBaseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "挂篮数据主表-通过id查询")
	@ApiOperation(value="挂篮数据主表-通过id查询", notes="挂篮数据主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		GualanBase gualanBase = gualanBaseService.getById(id);
		if(gualanBase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(gualanBase);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gualanBase
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GualanBase gualanBase) {
        return super.exportXls(request, gualanBase, GualanBase.class, "挂篮数据主表");
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
        return super.importExcel(request, response, GualanBase.class);
    }

}
