package com.trtm.iot.bhzrecipe.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhydcfg.entity.DeviceElectricCfg;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.trtm.iot.bhzrecipe.entity.BhzRecipe;
import com.trtm.iot.bhzrecipe.vo.BhzRecipePage;
import com.trtm.iot.bhzrecipe.service.IBhzRecipeService;
import com.trtm.iot.bhzrecipe.service.IBhzRecipedetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 砼拌合站理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
@Api(tags="砼拌合站理论配合比主表")
@RestController
@RequestMapping("/bhzrecipe/bhzRecipe")
@Slf4j
public class BhzRecipeController {
	@Autowired
	private IBhzRecipeService bhzRecipeService;
	@Autowired
	private IBhzRecipedetailService bhzRecipedetailService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;
	/**
	 * 分页列表查询
	 *
	 * @param bhzRecipe
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "砼拌合站理论配合比主表-分页列表查询")
	@ApiOperation(value="砼拌合站理论配合比主表-分页列表查询", notes="砼拌合站理论配合比主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzRecipe bhzRecipe,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (bhzRecipe.getBhjno() == null) {
			if (!"null".equals(shebei)) {
				bhzRecipe.setBhjno(shebei);
			}else {
				bhzRecipe.setBhjno("空");
			}
		}
		bhzRecipe.setIsdel(0);
		QueryWrapper<BhzRecipe> queryWrapper = QueryGenerator.initQueryWrapper(bhzRecipe, req.getParameterMap());
		Page<BhzRecipe> page = new Page<BhzRecipe>(pageNo, pageSize);
		IPage<BhzRecipe> pageList = bhzRecipeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 @AutoLog(value = "砼拌合站理论配合比主表-理论配合比号查询")
	 @ApiOperation(value="砼拌合站理论配合比主表-理论配合比号查询", notes="砼拌合站理论配合比主表-理论配合比号查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(BhzRecipe bhzRecipe,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if(bhzRecipe.getBhjno()==null){
			 if (!"null".equals(shebei)) {
				 bhzRecipe.setBhjno(shebei);
			 }else {
				 bhzRecipe.setBhjno("空");
			 }
		 }
		 bhzRecipe.setIsdel(0);
		 QueryWrapper<BhzRecipe> queryWrapper = QueryGenerator.initQueryWrapper(bhzRecipe, req.getParameterMap());
		 queryWrapper.orderByDesc("id");
		 List<BhzRecipe> pageList = bhzRecipeService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	 @AutoLog(value = "砼拌合站理论配合比主表-理论配合比号查询")
	 @ApiOperation(value="砼拌合站理论配合比主表-理论配合比号查询", notes="砼拌合站理论配合比主表-理论配合比号查询")
	 @GetMapping(value = "/list3")
	 public Result<?> queryPageList3(BhzRecipe bhzRecipe,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if(bhzRecipe.getBhjno()==null){
			 if (!"null".equals(shebei)) {
				 bhzRecipe.setBhjno(shebei);
			 }else {
				 bhzRecipe.setBhjno("空");
			 }
		 }
		 bhzRecipe.setIsdel(0);
		 if (StringUtils.isBlank(bhzRecipe.getCode())) {
			 bhzRecipe.setCode("LPHB-" + "*");
		 }
		 QueryWrapper<BhzRecipe> queryWrapper = QueryGenerator.initQueryWrapper(bhzRecipe, req.getParameterMap());
		 queryWrapper.orderByDesc("id");
		 List<BhzRecipe> pageList = bhzRecipeService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 总数统计
	  *
	  * @param bhzRecipe
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "砼拌合站理论配合比主表-总数统计")
	 @ApiOperation(value="砼拌合站理论配合比主表-总数统计", notes="砼拌合站理论配合比主表-总数统计")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(BhzRecipe bhzRecipe,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req,String sysOrgCode) {
//		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//		 if (bhzRecipe.getBhjno() == null) {
//			 if (!"null".equals(shebei)) {
//				 bhzRecipe.setBhjno(shebei);
//			 }else {
//				 bhzRecipe.setBhjno("空");
//			 }
//		 }
		 QueryWrapper<BhzRecipe> queryWrapper = QueryGenerator.initQueryWrapper(bhzRecipe, req.getParameterMap());
		 queryWrapper.select("ifnull(count(*),0) as isdel");
		 queryWrapper.last("a left join shebei_info b on a.bhjno = b.sbjno where b.sys_org_code like '"+sysOrgCode+"%'and a.isdel = 0 and b.shebei_status = 1");
		 List<BhzRecipe> pageList = bhzRecipeService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param bhzRecipePage
	 * @return
	 */
	@AutoLog(value = "砼拌合站理论配合比主表-添加")
	@ApiOperation(value="砼拌合站理论配合比主表-添加", notes="砼拌合站理论配合比主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzRecipePage bhzRecipePage) {
		BhzRecipe bhzRecipe = new BhzRecipe();
		String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
		bhzRecipePage.setUuid(uuid);
		Date date=new Date();
		if(StringUtils.isBlank(bhzRecipePage.getCode())){
			DateFormat format=new SimpleDateFormat("yyyyMMdd-HHmmss");
			bhzRecipePage.setCode(DateUtils.codeFormat("LPHB-"));
		}

		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		bhzRecipePage.setCreatedate(format1.format(date));
		if (bhzRecipePage.getMixlast() == null){
			bhzRecipePage.setMixlast(120);
		}else {
			bhzRecipePage.setMixlast(bhzRecipePage.getMixlast());
		}
		BeanUtils.copyProperties(bhzRecipePage, bhzRecipe);
		bhzRecipeService.saveMain(bhzRecipe, bhzRecipePage.getBhzRecipedetailList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param bhzRecipePage
	 * @return
	 */
	@AutoLog(value = "砼拌合站理论配合比主表-编辑")
	@ApiOperation(value="砼拌合站理论配合比主表-编辑", notes="砼拌合站理论配合比主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzRecipePage bhzRecipePage) {
		BhzRecipe bhzRecipe = new BhzRecipe();
		BeanUtils.copyProperties(bhzRecipePage, bhzRecipe);
		BhzRecipe bhzRecipeEntity = bhzRecipeService.getById(bhzRecipe.getId());
		if(bhzRecipeEntity==null) {
			return Result.error("未找到对应数据");
		}
		bhzRecipeService.updateMain(bhzRecipe, bhzRecipePage.getBhzRecipedetailList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "砼拌合站理论配合比主表-通过id删除")
	@ApiOperation(value="砼拌合站理论配合比主表-通过id删除", notes="砼拌合站理论配合比主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		BhzRecipe bhzRecipe = bhzRecipeService.getById(id);
		bhzRecipe.setIsdel(1);
		bhzRecipeService.updateById(bhzRecipe);
		bhzRecipedetailService.updateone(bhzRecipe.getUuid());
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "砼拌合站理论配合比主表-批量删除")
	@ApiOperation(value="砼拌合站理论配合比主表-批量删除", notes="砼拌合站理论配合比主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzRecipeService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "砼拌合站理论配合比主表-通过id查询")
	@ApiOperation(value="砼拌合站理论配合比主表-通过id查询", notes="砼拌合站理论配合比主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzRecipe bhzRecipe = bhzRecipeService.getById(id);
		if(bhzRecipe==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzRecipe);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "砼拌合站理论配合比子表通过主表ID查询")
	@ApiOperation(value="砼拌合站理论配合比子表主表ID查询", notes="砼拌合站理论配合比子表-通主表ID查询")
	@GetMapping(value = "/queryBhzRecipedetailByMainId")
	public Result<?> queryBhzRecipedetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<BhzRecipedetail> bhzRecipedetailList = bhzRecipedetailService.selectByMainId(id);
		return Result.OK(bhzRecipedetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzRecipe
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzRecipe bhzRecipe) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<BhzRecipe> queryWrapper = QueryGenerator.initQueryWrapper(bhzRecipe, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<BhzRecipe> queryList = bhzRecipeService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<BhzRecipe> bhzRecipeList = new ArrayList<BhzRecipe>();
      if(oConvertUtils.isEmpty(selections)) {
          bhzRecipeList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          bhzRecipeList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<BhzRecipePage> pageList = new ArrayList<BhzRecipePage>();
      for (BhzRecipe main : bhzRecipeList) {
          BhzRecipePage vo = new BhzRecipePage();
          BeanUtils.copyProperties(main, vo);
          List<BhzRecipedetail> bhzRecipedetailList = bhzRecipedetailService.selectByMainId(main.getUuid());
          vo.setBhzRecipedetailList(bhzRecipedetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "砼拌合站理论配合比主表列表");
      mv.addObject(NormalExcelConstants.CLASS, BhzRecipePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("砼拌合站理论配合比主表数据", "导出人:"+sysUser.getRealname(), "砼拌合站理论配合比主表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<BhzRecipePage> list = ExcelImportUtil.importExcel(file.getInputStream(), BhzRecipePage.class, params);
              for (BhzRecipePage page : list) {
                  BhzRecipe po = new BhzRecipe();
                  BeanUtils.copyProperties(page, po);
                  bhzRecipeService.saveMain(po, page.getBhzRecipedetailList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
