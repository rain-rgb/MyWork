package com.trtm.iot.lowstrain_m.controller;

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
import com.trtm.iot.lowstrain_m.entity.LowstrainS;
import com.trtm.iot.lowstrain_m.entity.LowstrainM;
import com.trtm.iot.lowstrain_m.vo.LowstrainMPage;
import com.trtm.iot.lowstrain_m.service.ILowstrainMService;
import com.trtm.iot.lowstrain_m.service.ILowstrainSService;
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
 * @Description: 低应变主表
 * @Author: jeecg-boot
 * @Date:   2024-08-14
 * @Version: V1.0
 */
@Api(tags="低应变主表")
@RestController
@RequestMapping("/lowstrain_m/lowstrainM")
@Slf4j
public class LowstrainMController {
	@Autowired
	private ILowstrainMService lowstrainMService;
	@Autowired
	private ILowstrainSService lowstrainSService;

	/**
	 * 分页列表查询
	 *
	 * @param lowstrainM
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "低应变主表-分页列表查询")
	@ApiOperation(value="低应变主表-分页列表查询", notes="低应变主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(LowstrainM lowstrainM,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LowstrainM> queryWrapper = QueryGenerator.initQueryWrapper(lowstrainM, req.getParameterMap());
		Page<LowstrainM> page = new Page<LowstrainM>(pageNo, pageSize);
		IPage<LowstrainM> pageList = lowstrainMService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param lowstrainMPage
	 * @return
	 */
	@AutoLog(value = "低应变主表-添加")
	@ApiOperation(value="低应变主表-添加", notes="低应变主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody LowstrainMPage lowstrainMPage) {
		LowstrainM lowstrainM = new LowstrainM();
		BeanUtils.copyProperties(lowstrainMPage, lowstrainM);
		lowstrainMService.saveMain(lowstrainM, lowstrainMPage.getLowstrainSList());
		return Result.OK("添加成功！");
	}

	@AutoLog(value = "低应变主表-添加")
	@ApiOperation(value="低应变主表-添加", notes="低应变主表-添加")
	@PostMapping(value = "/addOpen")
	public Result<?> addOpen(@RequestBody LowstrainMPage lowstrainMPage) {
		LowstrainM lowstrainM = new LowstrainM();
		BeanUtils.copyProperties(lowstrainMPage, lowstrainM);
		lowstrainMService.saveMain(lowstrainM, lowstrainMPage.getLowstrainSList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param lowstrainMPage
	 * @return
	 */
	@AutoLog(value = "低应变主表-编辑")
	@ApiOperation(value="低应变主表-编辑", notes="低应变主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody LowstrainMPage lowstrainMPage) {
		LowstrainM lowstrainM = new LowstrainM();
		BeanUtils.copyProperties(lowstrainMPage, lowstrainM);
		LowstrainM lowstrainMEntity = lowstrainMService.getById(lowstrainM.getId());
		if(lowstrainMEntity==null) {
			return Result.error("未找到对应数据");
		}
		lowstrainMService.updateMain(lowstrainM, lowstrainMPage.getLowstrainSList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "低应变主表-通过id删除")
	@ApiOperation(value="低应变主表-通过id删除", notes="低应变主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		lowstrainMService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "低应变主表-批量删除")
	@ApiOperation(value="低应变主表-批量删除", notes="低应变主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.lowstrainMService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "低应变主表-通过id查询")
	@ApiOperation(value="低应变主表-通过id查询", notes="低应变主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		LowstrainM lowstrainM = lowstrainMService.getById(id);
		if(lowstrainM==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(lowstrainM);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "低应变子表通过主表ID查询")
	@ApiOperation(value="低应变子表主表ID查询", notes="低应变子表-通主表ID查询")
	@GetMapping(value = "/queryLowstrainSByMainId")
	public Result<?> queryLowstrainSListByMainId(@RequestParam(name="id",required=true) String id) {
		List<LowstrainS> lowstrainSList = lowstrainSService.selectByMainId(id);
		return Result.OK(lowstrainSList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param lowstrainM
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LowstrainM lowstrainM) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<LowstrainM> queryWrapper = QueryGenerator.initQueryWrapper(lowstrainM, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<LowstrainM> queryList = lowstrainMService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<LowstrainM> lowstrainMList = new ArrayList<LowstrainM>();
      if(oConvertUtils.isEmpty(selections)) {
          lowstrainMList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          lowstrainMList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<LowstrainMPage> pageList = new ArrayList<LowstrainMPage>();
      for (LowstrainM main : lowstrainMList) {
          LowstrainMPage vo = new LowstrainMPage();
          BeanUtils.copyProperties(main, vo);
          List<LowstrainS> lowstrainSList = lowstrainSService.selectByMainId(main.getId());
          vo.setLowstrainSList(lowstrainSList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "低应变主表列表");
      mv.addObject(NormalExcelConstants.CLASS, LowstrainMPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("低应变主表数据", "导出人:"+sysUser.getRealname(), "低应变主表"));
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
              List<LowstrainMPage> list = ExcelImportUtil.importExcel(file.getInputStream(), LowstrainMPage.class, params);
              for (LowstrainMPage page : list) {
                  LowstrainM po = new LowstrainM();
                  BeanUtils.copyProperties(page, po);
                  lowstrainMService.saveMain(po, page.getLowstrainSList());
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
