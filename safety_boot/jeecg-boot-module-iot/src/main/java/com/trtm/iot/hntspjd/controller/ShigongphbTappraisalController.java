package com.trtm.iot.hntspjd.controller;

import java.util.Arrays;
import java.util.Date;
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
import com.trtm.iot.hntspjd.entity.ShigongphbTappraisal;
import com.trtm.iot.hntspjd.service.IShigongphbTappraisalService;

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
 * @Description: 混凝土首盘鉴定表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
@Api(tags="混凝土首盘鉴定表信息")
@RestController
@RequestMapping("/hntspjd/shigongphbTappraisal")
@Slf4j
public class ShigongphbTappraisalController extends JeecgController<ShigongphbTappraisal, IShigongphbTappraisalService> {
	@Autowired
	private IShigongphbTappraisalService shigongphbTappraisalService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param shigongphbTappraisal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "混凝土首盘鉴定表信息-分页列表查询")
	@ApiOperation(value="混凝土首盘鉴定表信息-分页列表查询", notes="混凝土首盘鉴定表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ShigongphbTappraisal shigongphbTappraisal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (shigongphbTappraisal.getShebeibianhao() == null) {
			if (!"null".equals(shebei)) {
				shigongphbTappraisal.setShebeibianhao(shebei);
			}else {
				shigongphbTappraisal.setShebeibianhao("空");
			}
		}
		QueryWrapper<ShigongphbTappraisal> queryWrapper = QueryGenerator.initQueryWrapper(shigongphbTappraisal, req.getParameterMap());
		Page<ShigongphbTappraisal> page = new Page<ShigongphbTappraisal>(pageNo, pageSize);
		IPage<ShigongphbTappraisal> pageList = shigongphbTappraisalService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param shigongphbTappraisal
	 * @return
	 */
	@AutoLog(value = "混凝土首盘鉴定表信息-添加")
	@ApiOperation(value="混凝土首盘鉴定表信息-添加", notes="混凝土首盘鉴定表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ShigongphbTappraisal shigongphbTappraisal) {
		if (shigongphbTappraisal.getIdentificationtime() == null){
			shigongphbTappraisal.setIdentificationtime(new Date());
		}else {
			shigongphbTappraisal.setIdentificationtime(shigongphbTappraisal.getIdentificationtime());
		}
		shigongphbTappraisalService.save(shigongphbTappraisal);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param shigongphbTappraisal
	 * @return
	 */
	@AutoLog(value = "混凝土首盘鉴定表信息-编辑")
	@ApiOperation(value="混凝土首盘鉴定表信息-编辑", notes="混凝土首盘鉴定表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ShigongphbTappraisal shigongphbTappraisal) {
		shigongphbTappraisalService.updateById(shigongphbTappraisal);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土首盘鉴定表信息-通过id删除")
	@ApiOperation(value="混凝土首盘鉴定表信息-通过id删除", notes="混凝土首盘鉴定表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		shigongphbTappraisalService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "混凝土首盘鉴定表信息-批量删除")
	@ApiOperation(value="混凝土首盘鉴定表信息-批量删除", notes="混凝土首盘鉴定表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.shigongphbTappraisalService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土首盘鉴定表信息-通过id查询")
	@ApiOperation(value="混凝土首盘鉴定表信息-通过id查询", notes="混凝土首盘鉴定表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ShigongphbTappraisal shigongphbTappraisal = shigongphbTappraisalService.getById(id);
		if(shigongphbTappraisal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(shigongphbTappraisal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param shigongphbTappraisal
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShigongphbTappraisal shigongphbTappraisal) {
        return super.exportXls(request, shigongphbTappraisal, ShigongphbTappraisal.class, "混凝土首盘鉴定表信息");
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
        return super.importExcel(request, response, ShigongphbTappraisal.class);
    }

}
