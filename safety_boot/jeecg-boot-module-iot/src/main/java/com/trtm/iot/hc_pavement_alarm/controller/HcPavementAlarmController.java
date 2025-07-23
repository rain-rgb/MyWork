package com.trtm.iot.hc_pavement_alarm.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.trtm.iot.hc_pavement_alarm.service.IHcPavementAlarmService;

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
 * @Description: 摊铺碾压预警
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Api(tags="摊铺碾压预警")
@RestController
@RequestMapping("/hc_pavement_alarm/hcPavementAlarm")
@Slf4j
public class HcPavementAlarmController extends JeecgController<HcPavementAlarm, IHcPavementAlarmService> {
	@Autowired
	private IHcPavementAlarmService hcPavementAlarmService;
	 @Autowired
	 private IHcProjectService projectService;
	 @Autowired
	 private IHcSectionService sectionService;
	/**
	 * 分页列表查询
	 *
	 * @param hcPavementAlarm
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "摊铺碾压预警-分页列表查询")
	@ApiOperation(value="摊铺碾压预警-分页列表查询", notes="摊铺碾压预警-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcPavementAlarm hcPavementAlarm,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HcPavementAlarm> queryWrapper = QueryGenerator.initQueryWrapper(hcPavementAlarm, req.getParameterMap());
		Page<HcPavementAlarm> page = new Page<HcPavementAlarm>(pageNo, pageSize);
		IPage<HcPavementAlarm> pageList = hcPavementAlarmService.page(page, queryWrapper);
		if (pageList.getRecords().size() > 0){
			for (HcPavementAlarm record :pageList.getRecords()){
				String projectid = record.getProjectid();
				String sectionid = record.getSectionid();
				String startstation = record.getRoadstation();
				String endstation1 = getStartstation(startstation);
				QueryWrapper<HcProject> queryWrapper1 = new QueryWrapper<>();
				queryWrapper1.eq("pjId",projectid);
				HcProject one = projectService.getOne(queryWrapper1);

				QueryWrapper<HcSection> queryWrapper2 = new QueryWrapper<>();
				queryWrapper2.eq("sectionId",sectionid);
				HcSection one1 = sectionService.getOne(queryWrapper2);

				if (one != null){
					record.setPjname(one.getPjname());
				}
				if (one1 != null){
					record.setSectionname(one1.getSectionname());
				}
				record.setRoadstation(endstation1);
			}
		}
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param hcPavementAlarm
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "摊铺碾压预警-分页列表查询")
	 @ApiOperation(value="摊铺碾压预警-分页列表查询", notes="摊铺碾压预警-分页列表查询")
	 @GetMapping(value = "/listyj")
	 public Result<?> queryPageListyj(HcPavementAlarm hcPavementAlarm,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									String date_begin,String date_end,
									String sys_depart_orgcode,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 HcSection hcSection = new HcSection();
		 if (sys_depart_orgcode == null){
			 hcSection.setOrgcode(loginUser.getOrgCode()+"*");
		 }else {
			 hcSection.setOrgcode(sys_depart_orgcode+"*");
		 }
		 QueryWrapper<HcSection> queryWrapper8 = QueryGenerator.initQueryWrapper(hcSection, req.getParameterMap());
		 queryWrapper8.groupBy("pjId");
		 List<HcSection> list = sectionService.list(queryWrapper8);
		 List<String> list1 = new ArrayList<>();
		 if (list.size() > 0){
			 for (HcSection l :list){
				 list1.add(l.getPjid());
			 }
		 }
		 List<HcPavementAlarm>  list2 = new ArrayList<>();
		 HashMap<String, Object> map = new HashMap<>();
		 if (list1.size() > 0){
			 QueryWrapper<HcPavementAlarm> queryWrapper1 = QueryGenerator.initQueryWrapper(hcPavementAlarm, req.getParameterMap());
			 if (date_begin != null){
				 queryWrapper1.gt("dataTime",date_begin);
			 }
			 if (date_end != null){
				 queryWrapper1.lt("dataTime",date_end);
			 }
			 queryWrapper1.eq("overproof_status",10);
			 queryWrapper1.in("projectId",list1);
			 List<HcPavementAlarm> list3 = hcPavementAlarmService.list(queryWrapper1);

			 QueryWrapper<HcPavementAlarm> queryWrapper = QueryGenerator.initQueryWrapper(hcPavementAlarm, req.getParameterMap());
			 if (date_begin != null){
				 queryWrapper.gt("dataTime",date_begin);
			 }
			 if (date_end != null){
				 queryWrapper.lt("dataTime",date_end);
			 }
			 queryWrapper.in("projectId",list1);
			 list2 = hcPavementAlarmService.list(queryWrapper);
			 map.put("cbs",list2.size());
			 map.put("ycz",list3.size());
			 map.put("xiangq",list2);
		 }
		 return Result.OK(map);
	 }

	 public String getStartstation(String startstation){
		 String startsta = null;
		 if (startstation != null){
			 if(startstation.length() == 2){
				 startsta = "K0+0" + startstation;
			 }else if (startstation.length() == 3){
				 startsta = "K0+" + startstation;
			 }else if (startstation.length() > 3){
				 String substring = startstation.substring(0, startstation.length() - 3);
				 String substring1 = startstation.substring(startstation.length() - 3);
				 startsta = "K" + substring + "+" + substring1;
			 }
		 }
		 return startsta;
	 }
	 /**
	  * 分页列表查询
	  *
	  * @param hcPavementAlarm
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "摊铺碾压预警-分页列表查询")
	 @ApiOperation(value="摊铺碾压预警-分页列表查询", notes="摊铺碾压预警-分页列表查询")
	 @GetMapping(value = "/cblist")
	 public Result<?> cblist(HcPavementAlarm hcPavementAlarm,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<HcPavementAlarm> queryWrapper = QueryGenerator.initQueryWrapper(hcPavementAlarm, req.getParameterMap());
		 queryWrapper.gt("alarmLevel",0);
		 Page<HcPavementAlarm> page = new Page<HcPavementAlarm>(pageNo, pageSize);
		 IPage<HcPavementAlarm> pageList = hcPavementAlarmService.page(page, queryWrapper);
		 if (pageList.getRecords().size() > 0){
			 for (HcPavementAlarm record :pageList.getRecords()){
				 String projectid = record.getProjectid();
				 String sectionid = record.getSectionid();
				 String startstation = record.getRoadstation();
				 String endstation1 = getStartstation(startstation);
				 QueryWrapper<HcProject> queryWrapper1 = new QueryWrapper<>();
				 queryWrapper1.eq("pjId",projectid);
				 HcProject one = projectService.getOne(queryWrapper1);

				 QueryWrapper<HcSection> queryWrapper2 = new QueryWrapper<>();
				 queryWrapper2.eq("sectionId",sectionid);
				 HcSection one1 = sectionService.getOne(queryWrapper2);

				 if (one != null){
					 record.setPjname(one.getPjname());
				 }
				 if (one1 != null){
					 record.setSectionname(one1.getSectionname());
				 }
				 record.setRoadstation(endstation1);
			 }
		 }
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param hcPavementAlarm
	 * @return
	 */
	@AutoLog(value = "摊铺碾压预警-添加")
	@ApiOperation(value="摊铺碾压预警-添加", notes="摊铺碾压预警-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcPavementAlarm hcPavementAlarm) {
		hcPavementAlarmService.save(hcPavementAlarm);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hcPavementAlarm
	 * @return
	 */
	@AutoLog(value = "摊铺碾压预警-编辑")
	@ApiOperation(value="摊铺碾压预警-编辑", notes="摊铺碾压预警-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcPavementAlarm hcPavementAlarm) {
		hcPavementAlarmService.updateById(hcPavementAlarm);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "摊铺碾压预警-通过id删除")
	@ApiOperation(value="摊铺碾压预警-通过id删除", notes="摊铺碾压预警-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcPavementAlarmService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "摊铺碾压预警-批量删除")
	@ApiOperation(value="摊铺碾压预警-批量删除", notes="摊铺碾压预警-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcPavementAlarmService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "摊铺碾压预警-通过id查询")
	@ApiOperation(value="摊铺碾压预警-通过id查询", notes="摊铺碾压预警-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcPavementAlarm hcPavementAlarm = hcPavementAlarmService.getById(id);
		if(hcPavementAlarm==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcPavementAlarm);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcPavementAlarm
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcPavementAlarm hcPavementAlarm) {
        return super.exportXls(request, hcPavementAlarm, HcPavementAlarm.class, "摊铺碾压预警");
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
        return super.importExcel(request, response, HcPavementAlarm.class);
    }

}
