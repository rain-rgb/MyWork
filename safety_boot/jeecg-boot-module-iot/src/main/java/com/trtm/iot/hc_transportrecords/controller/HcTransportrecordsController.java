package com.trtm.iot.hc_transportrecords.controller;

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

import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsYSC;
import com.trtm.iot.hc_truck.entity.HcTruck;
import com.trtm.iot.hc_truck.service.IHcTruckService;
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
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsPave;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.trtm.iot.hc_transportrecords.vo.HcTransportrecordsPage;
import com.trtm.iot.hc_transportrecords.service.IHcTransportrecordsService;
import com.trtm.iot.hc_transportrecords.service.IHcTransportrecordsPaveService;
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
 * @Description: 运输数据
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
@Api(tags="运输数据")
@RestController
@RequestMapping("/hc_transportrecords/hcTransportrecords")
@Slf4j
public class HcTransportrecordsController {
	@Autowired
	private IHcTransportrecordsService hcTransportrecordsService;
	@Autowired
	private IHcTransportrecordsPaveService hcTransportrecordsPaveService;
	 @Autowired
	 private IHcTruckService truckService;

	/**
	 * 分页列表查询
	 *
	 * @param hcTransportrecords
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "运输数据-分页列表查询")
	@ApiOperation(value="运输数据-分页列表查询", notes="运输数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcTransportrecords hcTransportrecords,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HcTransportrecords> queryWrapper = QueryGenerator.initQueryWrapper(hcTransportrecords, req.getParameterMap());
		Page<HcTransportrecords> page = new Page<HcTransportrecords>(pageNo, pageSize);
		IPage<HcTransportrecords> pageList = hcTransportrecordsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 运输车查询接口
	  *
	  * @param hcTransportrecords
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "运输数据-分页列表查询")
	 @ApiOperation(value="运输数据-分页列表查询", notes="运输数据-分页列表查询")
	 @GetMapping(value = "/listysc")
	 public Result<?> queryPageListysc(HcTransportrecords hcTransportrecords,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									String date,
									HttpServletRequest req) {
		 QueryWrapper<HcTransportrecords> queryWrapper = QueryGenerator.initQueryWrapper(hcTransportrecords, req.getParameterMap());
		 if (date != null){
			 queryWrapper.likeRight("outStationTime",date);
		 }
		 Page<HcTransportrecords> page = new Page<HcTransportrecords>(pageNo, pageSize);
		 IPage<HcTransportrecords> pageList = hcTransportrecordsService.page(page, queryWrapper);
		 List<HcTransportrecordsYSC> pageList1 = new ArrayList<>();
		 List<HcTransportrecords> records = pageList.getRecords();
		 if (records.size() > 0){
			 for (HcTransportrecords record :records){
				 String truckid = record.getTruckid();
				 QueryWrapper<HcTruck> queryWrapper1 = new QueryWrapper<>();
				 queryWrapper1.eq("truckId",truckid);
				 List<HcTruck> list = truckService.list(queryWrapper1);
				 HcTransportrecordsYSC hcTransportrecordsYSC = new HcTransportrecordsYSC();
				 if (list.size() > 0){
					 HcTruck hcTruck = list.get(0);
					 hcTransportrecordsYSC.setHcTruck(hcTruck);
				 }
				 BeanUtils.copyProperties(record,hcTransportrecordsYSC);
				 pageList1.add(hcTransportrecordsYSC);
			 }
		 }
		 return Result.OK(pageList1);
	 }
	/**
	 *   添加
	 *
	 * @param hcTransportrecordsPage
	 * @return
	 */
	@AutoLog(value = "运输数据-添加")
	@ApiOperation(value="运输数据-添加", notes="运输数据-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcTransportrecordsPage hcTransportrecordsPage) {
		HcTransportrecords hcTransportrecords = new HcTransportrecords();
		BeanUtils.copyProperties(hcTransportrecordsPage, hcTransportrecords);
		hcTransportrecordsService.saveMain(hcTransportrecords, hcTransportrecordsPage.getPaveList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hcTransportrecordsPage
	 * @return
	 */
	@AutoLog(value = "运输数据-编辑")
	@ApiOperation(value="运输数据-编辑", notes="运输数据-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcTransportrecordsPage hcTransportrecordsPage) {
		HcTransportrecords hcTransportrecords = new HcTransportrecords();
		BeanUtils.copyProperties(hcTransportrecordsPage, hcTransportrecords);
		HcTransportrecords hcTransportrecordsEntity = hcTransportrecordsService.getById(hcTransportrecords.getId());
		if(hcTransportrecordsEntity==null) {
			return Result.error("未找到对应数据");
		}
		hcTransportrecordsService.updateMain(hcTransportrecords, hcTransportrecordsPage.getPaveList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运输数据-通过id删除")
	@ApiOperation(value="运输数据-通过id删除", notes="运输数据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcTransportrecordsService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "运输数据-批量删除")
	@ApiOperation(value="运输数据-批量删除", notes="运输数据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcTransportrecordsService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运输数据-通过id查询")
	@ApiOperation(value="运输数据-通过id查询", notes="运输数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcTransportrecords hcTransportrecords = hcTransportrecordsService.getById(id);
		if(hcTransportrecords==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcTransportrecords);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运输数据子表通过主表ID查询")
	@ApiOperation(value="运输数据子表主表ID查询", notes="运输数据子表-通主表ID查询")
	@GetMapping(value = "/queryHcTransportrecordsPaveByMainId")
	public Result<?> queryHcTransportrecordsPaveListByMainId(@RequestParam(name="id",required=true) String id) {
		List<HcTransportrecordsPave> hcTransportrecordsPaveList = hcTransportrecordsPaveService.selectByMainId(id);
		return Result.OK(hcTransportrecordsPaveList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcTransportrecords
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcTransportrecords hcTransportrecords) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<HcTransportrecords> queryWrapper = QueryGenerator.initQueryWrapper(hcTransportrecords, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<HcTransportrecords> queryList = hcTransportrecordsService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<HcTransportrecords> hcTransportrecordsList = new ArrayList<HcTransportrecords>();
      if(oConvertUtils.isEmpty(selections)) {
          hcTransportrecordsList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          hcTransportrecordsList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<HcTransportrecordsPage> pageList = new ArrayList<HcTransportrecordsPage>();
      for (HcTransportrecords main : hcTransportrecordsList) {
          HcTransportrecordsPage vo = new HcTransportrecordsPage();
          BeanUtils.copyProperties(main, vo);
          List<HcTransportrecordsPave> hcTransportrecordsPaveList = hcTransportrecordsPaveService.selectByMainId(String.valueOf(main.getId()));
          vo.setPaveList(hcTransportrecordsPaveList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "运输数据列表");
      mv.addObject(NormalExcelConstants.CLASS, HcTransportrecordsPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("运输数据数据", "导出人:"+sysUser.getRealname(), "运输数据"));
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
              List<HcTransportrecordsPage> list = ExcelImportUtil.importExcel(file.getInputStream(), HcTransportrecordsPage.class, params);
              for (HcTransportrecordsPage page : list) {
                  HcTransportrecords po = new HcTransportrecords();
                  BeanUtils.copyProperties(page, po);
                  hcTransportrecordsService.saveMain(po, page.getPaveList());
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
