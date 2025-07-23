package com.trtm.iot.trkongdaogjdxm.controller;

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
import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxkdxx;
import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxs;
import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxm;
import com.trtm.iot.trkongdaogjdxm.vo.TrKongdaogjDxmPage;
import com.trtm.iot.trkongdaogjdxm.service.ITrKongdaogjDxmService;
import com.trtm.iot.trkongdaogjdxm.service.ITrKongdaogjDxkdxxService;
import com.trtm.iot.trkongdaogjdxm.service.ITrKongdaogjDxsService;
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
 * @Description: 孔道灌浆（定性检测）主表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@Api(tags="孔道灌浆（定性检测）主表")
@RestController
@RequestMapping("/trkongdaogjdxm/trKongdaogjDxm")
@Slf4j
public class TrKongdaogjDxmController {
	@Autowired
	private ITrKongdaogjDxmService trKongdaogjDxmService;
	@Autowired
	private ITrKongdaogjDxkdxxService trKongdaogjDxkdxxService;
	@Autowired
	private ITrKongdaogjDxsService trKongdaogjDxsService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param trKongdaogjDxm
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定性检测）主表-分页列表查询")
	@ApiOperation(value="孔道灌浆（定性检测）主表-分页列表查询", notes="孔道灌浆（定性检测）主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrKongdaogjDxm trKongdaogjDxm,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {

		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (trKongdaogjDxm.getMachineid() == null) {
			if (!"null".equals(shebei)) {
				trKongdaogjDxm.setMachineid(shebei);
			} else {
				trKongdaogjDxm.setMachineid("空");
			}

		}
		QueryWrapper<TrKongdaogjDxm> queryWrapper = QueryGenerator.initQueryWrapper(trKongdaogjDxm, req.getParameterMap());
		Page<TrKongdaogjDxm> page = new Page<TrKongdaogjDxm>(pageNo, pageSize);
		IPage<TrKongdaogjDxm> pageList = trKongdaogjDxmService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param trKongdaogjDxmPage
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定性检测）主表-添加")
	@ApiOperation(value="孔道灌浆（定性检测）主表-添加", notes="孔道灌浆（定性检测）主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrKongdaogjDxmPage trKongdaogjDxmPage) {
		TrKongdaogjDxm trKongdaogjDxm = new TrKongdaogjDxm();
		BeanUtils.copyProperties(trKongdaogjDxmPage, trKongdaogjDxm);
		trKongdaogjDxmService.saveMain(trKongdaogjDxm, trKongdaogjDxmPage.getTrKongdaogjDxkdxxList(),trKongdaogjDxmPage.getTrKongdaogjDxsList());
		return Result.OK("添加成功！");
	}

	/**
	 *   添加
	 *
	 * @param trKongdaogjDxmPage
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定性检测）主表-添加")
	@ApiOperation(value="孔道灌浆（定性检测）主表-添加", notes="孔道灌浆（定性检测）主表-添加")
	@PostMapping(value = "/addOpen")
	public Result<?> addOpen(@RequestBody TrKongdaogjDxmPage trKongdaogjDxmPage) {
		TrKongdaogjDxm trKongdaogjDxm = new TrKongdaogjDxm();
		BeanUtils.copyProperties(trKongdaogjDxmPage, trKongdaogjDxm);
		trKongdaogjDxmService.saveMain(trKongdaogjDxm, trKongdaogjDxmPage.getTrKongdaogjDxkdxxList(),trKongdaogjDxmPage.getTrKongdaogjDxsList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param trKongdaogjDxmPage
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定性检测）主表-编辑")
	@ApiOperation(value="孔道灌浆（定性检测）主表-编辑", notes="孔道灌浆（定性检测）主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrKongdaogjDxmPage trKongdaogjDxmPage) {
		TrKongdaogjDxm trKongdaogjDxm = new TrKongdaogjDxm();
		BeanUtils.copyProperties(trKongdaogjDxmPage, trKongdaogjDxm);
		TrKongdaogjDxm trKongdaogjDxmEntity = trKongdaogjDxmService.getById(trKongdaogjDxm.getId());
		if(trKongdaogjDxmEntity==null) {
			return Result.error("未找到对应数据");
		}
		trKongdaogjDxmService.updateMain(trKongdaogjDxm, trKongdaogjDxmPage.getTrKongdaogjDxkdxxList(),trKongdaogjDxmPage.getTrKongdaogjDxsList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定性检测）主表-通过id删除")
	@ApiOperation(value="孔道灌浆（定性检测）主表-通过id删除", notes="孔道灌浆（定性检测）主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trKongdaogjDxmService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定性检测）主表-批量删除")
	@ApiOperation(value="孔道灌浆（定性检测）主表-批量删除", notes="孔道灌浆（定性检测）主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trKongdaogjDxmService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定性检测）主表-通过id查询")
	@ApiOperation(value="孔道灌浆（定性检测）主表-通过id查询", notes="孔道灌浆（定性检测）主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrKongdaogjDxm trKongdaogjDxm = trKongdaogjDxmService.getById(id);
		if(trKongdaogjDxm==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trKongdaogjDxm);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定性检测）孔道信息表通过主表ID查询")
	@ApiOperation(value="孔道灌浆（定性检测）孔道信息表主表ID查询", notes="孔道灌浆（定性检测）孔道信息表-通主表ID查询")
	@GetMapping(value = "/queryTrKongdaogjDxkdxxByMainId")
	public Result<?> queryTrKongdaogjDxkdxxListByMainId(@RequestParam(name="id",required=true) String id) {
		List<TrKongdaogjDxkdxx> trKongdaogjDxkdxxList = trKongdaogjDxkdxxService.selectByMainId(id);
		return Result.OK(trKongdaogjDxkdxxList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定性检测）子表通过主表ID查询")
	@ApiOperation(value="孔道灌浆（定性检测）子表主表ID查询", notes="孔道灌浆（定性检测）子表-通主表ID查询")
	@GetMapping(value = "/queryTrKongdaogjDxsByMainId")
	public Result<?> queryTrKongdaogjDxsListByMainId(@RequestParam(name="id",required=true) String id) {
		List<TrKongdaogjDxs> trKongdaogjDxsList = trKongdaogjDxsService.selectByMainId(id);
		return Result.OK(trKongdaogjDxsList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trKongdaogjDxm
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrKongdaogjDxm trKongdaogjDxm) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<TrKongdaogjDxm> queryWrapper = QueryGenerator.initQueryWrapper(trKongdaogjDxm, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<TrKongdaogjDxm> queryList = trKongdaogjDxmService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<TrKongdaogjDxm> trKongdaogjDxmList = new ArrayList<TrKongdaogjDxm>();
      if(oConvertUtils.isEmpty(selections)) {
          trKongdaogjDxmList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          trKongdaogjDxmList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<TrKongdaogjDxmPage> pageList = new ArrayList<TrKongdaogjDxmPage>();
      for (TrKongdaogjDxm main : trKongdaogjDxmList) {
          TrKongdaogjDxmPage vo = new TrKongdaogjDxmPage();
          BeanUtils.copyProperties(main, vo);
          List<TrKongdaogjDxkdxx> trKongdaogjDxkdxxList = trKongdaogjDxkdxxService.selectByMainId(String.valueOf(main.getId()));
          vo.setTrKongdaogjDxkdxxList(trKongdaogjDxkdxxList);
          List<TrKongdaogjDxs> trKongdaogjDxsList = trKongdaogjDxsService.selectByMainId(String.valueOf(main.getId()));
          vo.setTrKongdaogjDxsList(trKongdaogjDxsList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "孔道灌浆（定性检测）主表列表");
      mv.addObject(NormalExcelConstants.CLASS, TrKongdaogjDxmPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("孔道灌浆（定性检测）主表数据", "导出人:"+sysUser.getRealname(), "孔道灌浆（定性检测）主表"));
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
              List<TrKongdaogjDxmPage> list = ExcelImportUtil.importExcel(file.getInputStream(), TrKongdaogjDxmPage.class, params);
              for (TrKongdaogjDxmPage page : list) {
                  TrKongdaogjDxm po = new TrKongdaogjDxm();
                  BeanUtils.copyProperties(page, po);
                  trKongdaogjDxmService.saveMain(po, page.getTrKongdaogjDxkdxxList(),page.getTrKongdaogjDxsList());
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
