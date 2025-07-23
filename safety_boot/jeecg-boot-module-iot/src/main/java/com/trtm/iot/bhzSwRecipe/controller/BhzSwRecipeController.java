package com.trtm.iot.bhzSwRecipe.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.bhzSwPhbZibiao.entity.BhzSwPhbZibiao;
import com.trtm.iot.bhzSwRecipe.vo.BhzSwRecipePage;
import com.trtm.iot.bhzrecipe.entity.BhzRecipe;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqRecipe;
import com.trtm.iot.lqbhzrecipe.vo.BhzLqRecipePage;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzSwRecipe.entity.BhzSwRecipe;
import com.trtm.iot.bhzSwRecipe.service.IBhzSwRecipeService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: 水稳理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Api(tags="水稳理论配合比主表")
@RestController
@RequestMapping("/bhzSwRecipe/bhzSwRecipe")
@Slf4j
public class BhzSwRecipeController extends JeecgController<BhzSwRecipe, IBhzSwRecipeService> {
	@Autowired
	private IBhzSwRecipeService bhzSwRecipeService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param bhzSwRecipe
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比主表-分页列表查询")
	@ApiOperation(value="水稳理论配合比主表-分页列表查询", notes="水稳理论配合比主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzSwRecipe bhzSwRecipe,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (bhzSwRecipe.getShebeibianhao() == null) {
			if (!"null".equals(shebei)) {
				bhzSwRecipe.setShebeibianhao(shebei);
			}else {
				bhzSwRecipe.setShebeibianhao("空");
			}
		}
		QueryWrapper<BhzSwRecipe> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwRecipe, req.getParameterMap());
		Page<BhzSwRecipe> page = new Page<BhzSwRecipe>(pageNo, pageSize);
		IPage<BhzSwRecipe> pageList = bhzSwRecipeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	/**
	 *   添加
	 *
	 * @param bhzSwRecipe
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比主表-添加")
	@ApiOperation(value="水稳理论配合比主表-添加", notes="水稳理论配合比主表-添加")
	@PostMapping(value = "/add1")
	public Result<?> add1(@RequestBody BhzSwRecipe bhzSwRecipe) {
		bhzSwRecipeService.save(bhzSwRecipe);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加
	  *
	  * @param bhzSwRecipePage
	  * @return
	  */
	 @AutoLog(value = "水稳理论配合比主表-添加")
	 @ApiOperation(value="水稳理论配合比主表-添加", notes="水稳理论配合比主表-添加")
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody BhzSwRecipePage bhzSwRecipePage) {
		 BhzSwRecipe bhzSwRecipe=new BhzSwRecipe();
		 String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
		 bhzSwRecipePage.setZhuziid(uuid);
		 Date date=new Date();
		 DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 bhzSwRecipePage.setLlshijian(date);
		 BeanUtils.copyProperties(bhzSwRecipePage, bhzSwRecipe);
		 bhzSwRecipeService.saveMain(bhzSwRecipe, bhzSwRecipePage.getBhzSwPhbZibiaoList());
		 return Result.OK("添加成功！");
	 }


	 /**
	 *  编辑
	 *
	 * @param bhzSwRecipePage
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比主表-编辑")
	@ApiOperation(value="水稳理论配合比主表-编辑", notes="水稳理论配合比主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzSwRecipePage bhzSwRecipePage) {

		BhzSwRecipe bhzSwRecipe=new BhzSwRecipe();
		BeanUtils.copyProperties(bhzSwRecipePage, bhzSwRecipe);
		BhzSwRecipe byId = bhzSwRecipeService.getById(bhzSwRecipe.getId());
		if(byId==null) {
			return Result.error("未找到对应数据");
		}
		bhzSwRecipeService.updateMain(bhzSwRecipe, bhzSwRecipePage.getBhzSwPhbZibiaoList());
		return Result.OK("编辑成功!");
//		bhzSwRecipeService.updateById(bhzSwRecipe);
//		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比主表-通过id删除")
	@ApiOperation(value="水稳理论配合比主表-通过id删除", notes="水稳理论配合比主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzSwRecipeService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比主表-批量删除")
	@ApiOperation(value="水稳理论配合比主表-批量删除", notes="水稳理论配合比主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzSwRecipeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "水稳理论配合比主表-通过id查询")
	@ApiOperation(value="水稳理论配合比主表-通过id查询", notes="水稳理论配合比主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzSwRecipe bhzSwRecipe = bhzSwRecipeService.getById(id);
		if(bhzSwRecipe==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzSwRecipe);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzSwRecipe
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzSwRecipe bhzSwRecipe) {
        return super.exportXls(request, bhzSwRecipe, BhzSwRecipe.class, "水稳理论配合比主表");
    }

    /**
    *	通过excel导入数据
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BhzSwRecipe.class);
    }

}
