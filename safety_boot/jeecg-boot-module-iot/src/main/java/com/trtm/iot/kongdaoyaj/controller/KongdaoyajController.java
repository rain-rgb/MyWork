package com.trtm.iot.kongdaoyaj.controller;

import java.io.IOException;
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
import com.trtm.iot.kongdaoyaj.entity.Kongdaoyajs;
import com.trtm.iot.kongdaoyaj.entity.Kongdaoyaj;
import com.trtm.iot.kongdaoyaj.vo.KongdaoyajPage;
import com.trtm.iot.kongdaoyaj.service.IKongdaoyajService;
import com.trtm.iot.kongdaoyaj.service.IKongdaoyajsService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 孔道灌浆主表
 * @Author: jeecg-boot
 * @Date:   2024-06-25
 * @Version: V1.0
 */
@Api(tags="孔道灌浆主表")
@RestController
@RequestMapping("/kongdaoyaj/kongdaoyaj")
@Slf4j
public class KongdaoyajController {
	@Autowired
	private IKongdaoyajService kongdaoyajService;
	@Autowired
	private IKongdaoyajsService kongdaoyajsService;

	/**
	 * 分页列表查询
	 *
	 * @param kongdaoyaj
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "孔道灌浆主表-分页列表查询")
	@ApiOperation(value="孔道灌浆主表-分页列表查询", notes="孔道灌浆主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Kongdaoyaj kongdaoyaj,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Kongdaoyaj> queryWrapper = QueryGenerator.initQueryWrapper(kongdaoyaj, req.getParameterMap());
		Page<Kongdaoyaj> page = new Page<Kongdaoyaj>(pageNo, pageSize);
		IPage<Kongdaoyaj> pageList = kongdaoyajService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param kongdaoyajPage
	 * @return
	 */
	@AutoLog(value = "孔道灌浆主表-添加")
	@ApiOperation(value="孔道灌浆主表-添加", notes="孔道灌浆主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody KongdaoyajPage kongdaoyajPage) {
		Kongdaoyaj kongdaoyaj = new Kongdaoyaj();
		BeanUtils.copyProperties(kongdaoyajPage, kongdaoyaj);
		kongdaoyajService.saveMain(kongdaoyaj, kongdaoyajPage.getDetectData());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param kongdaoyajPage
	 * @return
	 */
	@AutoLog(value = "孔道灌浆主表-编辑")
	@ApiOperation(value="孔道灌浆主表-编辑", notes="孔道灌浆主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody KongdaoyajPage kongdaoyajPage) {
		Kongdaoyaj kongdaoyaj = new Kongdaoyaj();
		BeanUtils.copyProperties(kongdaoyajPage, kongdaoyaj);
		Kongdaoyaj kongdaoyajEntity = kongdaoyajService.getById(kongdaoyaj.getId());
		if(kongdaoyajEntity==null) {
			return Result.error("未找到对应数据");
		}
		kongdaoyajService.updateMain(kongdaoyaj, kongdaoyajPage.getDetectData());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "孔道灌浆主表-通过id删除")
	@ApiOperation(value="孔道灌浆主表-通过id删除", notes="孔道灌浆主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		kongdaoyajService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "孔道灌浆主表-批量删除")
	@ApiOperation(value="孔道灌浆主表-批量删除", notes="孔道灌浆主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kongdaoyajService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "孔道灌浆主表-通过id查询")
	@ApiOperation(value="孔道灌浆主表-通过id查询", notes="孔道灌浆主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Kongdaoyaj kongdaoyaj = kongdaoyajService.getById(id);
		if(kongdaoyaj==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kongdaoyaj);

	}

	/**
	 * 通过id查询
	 *
	 * @param uuid
	 * @return
	 */
	@AutoLog(value = "孔道灌浆子表通过主表ID查询")
	@ApiOperation(value="孔道灌浆子表主表ID查询", notes="孔道灌浆子表-通主表ID查询")
	@GetMapping(value = "/queryKongdaoyajsByMainId")
	public Result<?> queryKongdaoyajsListByMainId(@RequestParam(name="uuid",required=true) String uuid) {
		List<Kongdaoyajs> kongdaoyajsList = kongdaoyajsService.selectByMainId(uuid);
		return Result.OK(kongdaoyajsList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param kongdaoyaj
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Kongdaoyaj kongdaoyaj) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Kongdaoyaj> queryWrapper = QueryGenerator.initQueryWrapper(kongdaoyaj, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<Kongdaoyaj> queryList = kongdaoyajService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<Kongdaoyaj> kongdaoyajList = new ArrayList<Kongdaoyaj>();
      if(oConvertUtils.isEmpty(selections)) {
          kongdaoyajList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          kongdaoyajList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<KongdaoyajPage> pageList = new ArrayList<KongdaoyajPage>();
      for (Kongdaoyaj main : kongdaoyajList) {
          KongdaoyajPage vo = new KongdaoyajPage();
          BeanUtils.copyProperties(main, vo);
          List<Kongdaoyajs> kongdaoyajsList = kongdaoyajsService.selectByMainId(String.valueOf(main.getId()));
          vo.setDetectData(kongdaoyajsList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "孔道灌浆主表列表");
      mv.addObject(NormalExcelConstants.CLASS, KongdaoyajPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("孔道灌浆主表数据", "导出人:"+sysUser.getRealname(), "孔道灌浆主表"));
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
              List<KongdaoyajPage> list = ExcelImportUtil.importExcel(file.getInputStream(), KongdaoyajPage.class, params);
              for (KongdaoyajPage page : list) {
                  Kongdaoyaj po = new Kongdaoyaj();
                  BeanUtils.copyProperties(page, po);
                  kongdaoyajService.saveMain(po, page.getDetectData());
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
