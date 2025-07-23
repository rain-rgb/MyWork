package com.trtm.iot.hc_datalinkage.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkagePave;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkage;
import com.trtm.iot.hc_datalinkage.vo.HcDatalinkagePage;
import com.trtm.iot.hc_datalinkage.service.IHcDatalinkageService;
import com.trtm.iot.hc_datalinkage.service.IHcDatalinkagePaveService;
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
 * @Description: 数据联动（运输信息）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
@Api(tags="数据联动（运输信息）")
@RestController
@RequestMapping("/hc_datalinkage/hcDatalinkage")
@Slf4j
public class HcDatalinkageController {
	@Autowired
	private IHcDatalinkageService hcDatalinkageService;
	@Autowired
	private IHcDatalinkagePaveService hcDatalinkagePaveService;

	/**
	 * 分页列表查询
	 *
	 * @param hcDatalinkage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "数据联动（运输信息）-分页列表查询")
	@ApiOperation(value="数据联动（运输信息）-分页列表查询", notes="数据联动（运输信息）-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcDatalinkage hcDatalinkage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HcDatalinkage> queryWrapper = QueryGenerator.initQueryWrapper(hcDatalinkage, req.getParameterMap());
		Page<HcDatalinkage> page = new Page<HcDatalinkage>(pageNo, pageSize);
		IPage<HcDatalinkage> pageList = hcDatalinkageService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param hcDatalinkagePage
	 * @return
	 */
	@AutoLog(value = "数据联动（运输信息）-添加")
	@ApiOperation(value="数据联动（运输信息）-添加", notes="数据联动（运输信息）-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcDatalinkagePage hcDatalinkagePage) {
		HcDatalinkage hcDatalinkage = new HcDatalinkage();
		BeanUtils.copyProperties(hcDatalinkagePage, hcDatalinkage);
		hcDatalinkageService.saveMain(hcDatalinkage, hcDatalinkagePage.getPaveList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hcDatalinkagePage
	 * @return
	 */
	@AutoLog(value = "数据联动（运输信息）-编辑")
	@ApiOperation(value="数据联动（运输信息）-编辑", notes="数据联动（运输信息）-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcDatalinkagePage hcDatalinkagePage) {
		HcDatalinkage hcDatalinkage = new HcDatalinkage();
		BeanUtils.copyProperties(hcDatalinkagePage, hcDatalinkage);
		HcDatalinkage hcDatalinkageEntity = hcDatalinkageService.getById(hcDatalinkage.getId());
		if(hcDatalinkageEntity==null) {
			return Result.error("未找到对应数据");
		}
		hcDatalinkageService.updateMain(hcDatalinkage, hcDatalinkagePage.getPaveList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据联动（运输信息）-通过id删除")
	@ApiOperation(value="数据联动（运输信息）-通过id删除", notes="数据联动（运输信息）-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcDatalinkageService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "数据联动（运输信息）-批量删除")
	@ApiOperation(value="数据联动（运输信息）-批量删除", notes="数据联动（运输信息）-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcDatalinkageService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据联动（运输信息）-通过id查询")
	@ApiOperation(value="数据联动（运输信息）-通过id查询", notes="数据联动（运输信息）-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcDatalinkage hcDatalinkage = hcDatalinkageService.getById(id);
		if(hcDatalinkage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcDatalinkage);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据联动（摊铺数据）通过主表ID查询")
	@ApiOperation(value="数据联动（摊铺数据）主表ID查询", notes="数据联动（摊铺数据）-通主表ID查询")
	@GetMapping(value = "/queryHcDatalinkagePaveByMainId")
	public Result<?> queryHcDatalinkagePaveListByMainId(@RequestParam(name="id",required=true) String id) {
		List<HcDatalinkagePave> hcDatalinkagePaveList = hcDatalinkagePaveService.selectByMainId(id);
		return Result.OK(hcDatalinkagePaveList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcDatalinkage
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcDatalinkage hcDatalinkage) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<HcDatalinkage> queryWrapper = QueryGenerator.initQueryWrapper(hcDatalinkage, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<HcDatalinkage> queryList = hcDatalinkageService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<HcDatalinkage> hcDatalinkageList = new ArrayList<HcDatalinkage>();
      if(oConvertUtils.isEmpty(selections)) {
          hcDatalinkageList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          hcDatalinkageList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<HcDatalinkagePage> pageList = new ArrayList<HcDatalinkagePage>();
      for (HcDatalinkage main : hcDatalinkageList) {
          HcDatalinkagePage vo = new HcDatalinkagePage();
          BeanUtils.copyProperties(main, vo);
          List<HcDatalinkagePave> hcDatalinkagePaveList = hcDatalinkagePaveService.selectByMainId(String.valueOf(main.getId()));
          vo.setPaveList(hcDatalinkagePaveList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "数据联动（运输信息）列表");
      mv.addObject(NormalExcelConstants.CLASS, HcDatalinkagePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("数据联动（运输信息）数据", "导出人:"+sysUser.getRealname(), "数据联动（运输信息）"));
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
              List<HcDatalinkagePage> list = ExcelImportUtil.importExcel(file.getInputStream(), HcDatalinkagePage.class, params);
              for (HcDatalinkagePage page : list) {
                  HcDatalinkage po = new HcDatalinkage();
                  BeanUtils.copyProperties(page, po);
                  hcDatalinkageService.saveMain(po, page.getPaveList());
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
