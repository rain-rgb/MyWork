package com.trtm.iot.hc_tfstationdetail.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkarea;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkareaxq;
import com.trtm.iot.hc_tfysworkarea.service.IHcTfysworkareaService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hc_tfstationdetail.entity.HcTfstationdetail;
import com.trtm.iot.hc_tfstationdetail.service.IHcTfstationdetailService;

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
 * @Description: 土方工作区逐桩详情
 * @Author: jeecg-boot
 * @Date:   2023-10-10
 * @Version: V1.0
 */
@Api(tags="土方工作区逐桩详情")
@RestController
@RequestMapping("/hc_tfstationdetail/hcTfstationdetail")
@Slf4j
public class HcTfstationdetailController extends JeecgController<HcTfstationdetail, IHcTfstationdetailService> {
	@Autowired
	private IHcTfstationdetailService hcTfstationdetailService;
	 @Autowired
	 private IHcTfysworkareaService hcTfysworkareaService;
	 @Autowired
	 private IHcProjectService projectService;
	 @Autowired
	 private IHcSectionService sectionService;

	/**
	 * 分页列表查询
	 *
	 * @param hcTfstationdetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "土方工作区逐桩详情-分页列表查询")
	@ApiOperation(value="土方工作区逐桩详情-分页列表查询", notes="土方工作区逐桩详情-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcTfstationdetail hcTfstationdetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {

		QueryWrapper<HcTfstationdetail> queryWrapper = QueryGenerator.initQueryWrapper(hcTfstationdetail, req.getParameterMap());
		Page<HcTfstationdetail> page = new Page<HcTfstationdetail>(pageNo, pageSize);
		IPage<HcTfstationdetail> pageList = hcTfstationdetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "土方工作区逐桩详情-分页列表查询")
	 @ApiOperation(value="土方工作区逐桩详情-分页列表查询", notes="土方工作区逐桩详情-分页列表查询")
	 @GetMapping(value = "/listxq")
	 public Result<?> queryPageListxq(HcTfysworkarea hcTfysworkarea,String stast,String starttimes,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<HcTfysworkarea> queryWrapper2 = new QueryWrapper<>();
		 queryWrapper2.eq("sectionid",stast);
		 queryWrapper2.likeRight("starttime",starttimes);
		 List<HcTfysworkarea> list = hcTfysworkareaService.list(queryWrapper2);
		 List<String> list1 = new ArrayList<>();
		 for (HcTfysworkarea l :list){
			 list1.add(l.getBlockid());
		 }
		 QueryWrapper<HcSection> queryWrapper1 = new QueryWrapper<>();
		 queryWrapper1.eq("sectionId",stast);
		 HcSection one = sectionService.getOne(queryWrapper1);
		 QueryWrapper<HcTfstationdetail> queryWrapper = new QueryWrapper<>();
		 queryWrapper.ge("station",Integer.parseInt(hcTfysworkarea.getStartstation()));
		 queryWrapper.le("station",Integer.parseInt(hcTfysworkarea.getEndstation()));
		 queryWrapper.in("blockid",list1);
		 queryWrapper.orderByAsc("station");
		 Page<HcTfstationdetail> page = new Page<HcTfstationdetail>(pageNo, pageSize);
		 IPage<HcTfstationdetail> pageList = hcTfstationdetailService.page(page, queryWrapper);
		 for (HcTfstationdetail pag :pageList.getRecords()){
			 if (one.getSectionname().equals("第SG01标段")){
				 pag.setHetong("SG01");
			 }else if (one.getSectionname().equals("第SG02标段")){
				 pag.setHetong("SG02");
			 }else {
				 pag.setHetong("");
			 }
			 pag.setBlockid(one.getSectionbuildercompanyname());
			 pag.setSectionid(one.getSectionsupcompanyname());
		 }
		 return Result.OK(pageList);
	 }
	 /**
	  * 分页列表查询
	  * @return
	  */
	 @AutoLog(value = "土方工作区逐桩详情-分页列表查询")
	 @ApiOperation(value="土方工作区逐桩详情-分页列表查询", notes="土方工作区逐桩详情-分页列表查询")
	 @GetMapping(value = "/listbg")
	 public Result<?> queryPageListbg(String id,String blockId) {
		 QueryWrapper<HcTfysworkarea> queryWrapper1 = new QueryWrapper<>();
		 queryWrapper1.eq("id",id);
		 queryWrapper1.eq("blockId",blockId);
		 HcTfysworkarea one = hcTfysworkareaService.getOne(queryWrapper1);
		 HcTfysworkareaxq hcTfysworkareaxq = new HcTfysworkareaxq();
		 if (one != null){
			 QueryWrapper<HcProject> queryWrapper = new QueryWrapper<>();
			 queryWrapper.eq("pjId",one.getProjectid());
			 HcProject one1 = projectService.getOne(queryWrapper);

			 QueryWrapper<HcSection> queryWrapper2 = new QueryWrapper<>();
			 queryWrapper2.eq("sectionId",one.getSectionid());
			 HcSection one2 = sectionService.getOne(queryWrapper2);
			 one.setProjectid(one1.getPjname());//工程名称
			 one.setSectionid(one2.getSectionname());//标段名称
			 BeanUtils.copyProperties(one,hcTfysworkareaxq);
		 }

		 double i = 0;
		 double j = 0;
		 double x = 0;
		 double y = 0;
		 hcTfysworkareaxq.setI(0.0);//碾压遍数小于五的
		 hcTfysworkareaxq.setJ(0.0);//碾压遍数等于五的
		 hcTfysworkareaxq.setN(0.0);//碾压遍数大于五的

		 hcTfysworkareaxq.setX(0.0);//振碾遍数小于五的
		 hcTfysworkareaxq.setY(0.0);//振碾遍数等于五的
		 hcTfysworkareaxq.setZ(0.0);//振碾遍数大于五的
		 List<HcTfstationdetail> selectlistbybaserid = hcTfstationdetailService.selectlistbybaserid(hcTfysworkareaxq.getBlockid());
		 if (selectlistbybaserid.size() > 0){
			 int size = selectlistbybaserid.size();
			 hcTfysworkareaxq.setQualitystat(selectlistbybaserid.get(0).getAvgheight());//开始高程
			 hcTfysworkareaxq.setInspectstat(selectlistbybaserid.get(selectlistbybaserid.size()-1).getAvgheight());//结束高程

			 for (HcTfstationdetail hcTfstationdetail :selectlistbybaserid){
				 String avgtimes = hcTfstationdetail.getAvgtimes();
				 String avgvibratetimes = hcTfstationdetail.getAvgvibratetimes();
				 double i1 = Double.parseDouble(avgtimes);
				 double i2 = Double.parseDouble(avgvibratetimes);
				 if (i1 < 5){
					i++;
				 }else if (i1 == 5){
					j++;
				 }

				 if (i2 < 5){
					 x++;
				 }else if (i2 == 5){
					 y++;
				 }
			 }

			 hcTfysworkareaxq.setI(i/size*100);//碾压遍数小于五的
			 hcTfysworkareaxq.setJ(j/size*100);//碾压遍数等于五的
			 hcTfysworkareaxq.setN((1-i/size-j/size)*100);//碾压遍数大于五的

			 hcTfysworkareaxq.setX(x/size*100);//振碾遍数小于五的
			 hcTfysworkareaxq.setY(y/size*100);//振碾遍数等于五的
			 hcTfysworkareaxq.setZ((1-x/size-y/size)*100);//振碾遍数大于五的
		 }

		 hcTfysworkareaxq.setHcTfstationdetail(selectlistbybaserid);
		 return Result.OK(hcTfysworkareaxq);
	 }

	/**
	 *   添加
	 *
	 * @param hcTfstationdetail
	 * @return
	 */
	@AutoLog(value = "土方工作区逐桩详情-添加")
	@ApiOperation(value="土方工作区逐桩详情-添加", notes="土方工作区逐桩详情-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcTfstationdetail hcTfstationdetail) {
		hcTfstationdetailService.save(hcTfstationdetail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param hcTfstationdetail
	 * @return
	 */
	@AutoLog(value = "土方工作区逐桩详情-编辑")
	@ApiOperation(value="土方工作区逐桩详情-编辑", notes="土方工作区逐桩详情-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcTfstationdetail hcTfstationdetail) {
		hcTfstationdetailService.updateById(hcTfstationdetail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "土方工作区逐桩详情-通过id删除")
	@ApiOperation(value="土方工作区逐桩详情-通过id删除", notes="土方工作区逐桩详情-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcTfstationdetailService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "土方工作区逐桩详情-批量删除")
	@ApiOperation(value="土方工作区逐桩详情-批量删除", notes="土方工作区逐桩详情-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcTfstationdetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "土方工作区逐桩详情-通过id查询")
	@ApiOperation(value="土方工作区逐桩详情-通过id查询", notes="土方工作区逐桩详情-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcTfstationdetail hcTfstationdetail = hcTfstationdetailService.getById(id);
		if(hcTfstationdetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcTfstationdetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcTfstationdetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcTfstationdetail hcTfstationdetail) {
        return super.exportXls(request, hcTfstationdetail, HcTfstationdetail.class, "土方工作区逐桩详情");
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
        return super.importExcel(request, response, HcTfstationdetail.class);
    }

}
