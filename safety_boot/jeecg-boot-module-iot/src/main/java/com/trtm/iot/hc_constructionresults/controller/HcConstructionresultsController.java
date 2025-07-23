package com.trtm.iot.hc_constructionresults.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.puppycrawl.tools.checkstyle.grammars.javadoc.JavadocLexer;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresultsTPNY;
import com.trtm.iot.hc_constructionresults_ny.entity.HcConstructionresultsNy;
import com.trtm.iot.hc_constructionresults_ny.service.IHcConstructionresultsNyService;
import com.trtm.iot.hc_constructionresults_tp.entity.HcConstructionresultsTp;
import com.trtm.iot.hc_constructionresults_tp.service.IHcConstructionresultsTpService;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.projoverview.entity.ProjOverview;
import com.trtm.iot.projoverview.service.IProjOverviewService;
import com.trtm.iot.pushandreturn.entity.Pushandreturn;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import net.sf.saxon.expr.instruct.Copy;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.trtm.iot.hc_constructionresults.service.IHcConstructionresultsService;

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

import static com.puppycrawl.tools.checkstyle.grammars.javadoc.JavadocLexer.code;

/**
 * @Description: 施工成果
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Api(tags="施工成果")
@RestController
@RequestMapping("/hc_constructionresults/hcConstructionresults")
@Slf4j
public class HcConstructionresultsController extends JeecgController<HcConstructionresults, IHcConstructionresultsService> {
	@Autowired
	private IHcConstructionresultsService hcConstructionresultsService;
	 @Autowired
	 private IHcConstructionresultsTpService hcConstructionresultsTpService;
	 @Autowired
	 private IHcConstructionresultsNyService hcConstructionresultsNyService;
	 @Autowired
	 private IHcProjectService projectService;
	 @Autowired
	 private IHcSectionService sectionService;
	@Autowired
	private IProjOverviewService projOverviewService;
	@Autowired
	private IPushandreturnService pushandreturnService;

	private static final String appId = "SHGS";
	private static final String hcId = "STGS";//苏台
	private static final String pw = "123456789";
	private static final String url = "https://dp.huace.cn/digitalPlatform/open/api/token/get.shtml";
	private static final String tpwdurl = "https://dp.huace.cn/digitalPlatform/open/api/pavement/stationData.shtml";
	private static final String tplxurl = "https://dp.huace.cn/digitalPlatform/open/api/pavement/paveTempPass.shtml";//摊铺温度合格率
	private static final String tpsdurl = "https://dp.huace.cn/digitalPlatform/open/api/pavement/paveSpeedPass.shtml";//摊铺速度合格率
	private static final String zgwdurl = "https://dp.huace.cn/digitalPlatform/open/api/pavement/maxTempPass.shtml";//最高温度合格率
	private static final String lysdurl = "https://dp.huace.cn/digitalPlatform/open/api/pavement/rollingSpeedPass.shtml";//碾压速度合格率
	private static final String lybsurl = "https://dp.huace.cn/digitalPlatform/open/api/pavement/rollingTimesPass.shtml";//碾压遍数合格率
	private static final String mrqyurl = "https://dp.huace.cn/digitalPlatform/open/api/pavement/dailyUnderVoltageRate.shtml";//每日欠压率
	private static final String tfysurl = "https://dp.huace.cn/digitalPlatform/open/api/roadbed/workArea.shtml";//土方压实
	/**
	 * 调用isite平台接口
	 * 土方压实
	 */
	@AutoLog(value = "土方压实-分页列表查询")
	@ApiOperation(value="土方压实-分页列表查询", notes="土方压实-分页列表查询")
	@GetMapping(value = "/tfysurl")
	public Result<?> queryPagetfysurl(HcConstructionresults hcConstructionresults,Integer pageNo,Integer pageSize){

        // 获取token
		Map<String, Object> hctoken = projectService.selectByProjectid(hcConstructionresults.getProjectid());
		String token = "";
          if(hctoken == null){
			  JSONObject sendObject = JSONUtil.createObj();
			  sendObject.set("appId", hcId);
			  sendObject.set("pw", pw);
			  token = getCode(sendObject, url);
		  }else{
			   token = (String) hctoken.get("token");
		  }
//		JSONObject sendObject = JSONUtil.createObj();
//			  sendObject.set("appId", "YJFS");
//			  sendObject.set("pw", 123456789);
//			  token = getCode(sendObject, url);
//		System.out.println(token);
		JSONObject sendObject1 = JSONUtil.createObj();
		sendObject1.set("projectId", hcConstructionresults.getProjectid());
		sendObject1.set("sectionId", hcConstructionresults.getSectionid());
		sendObject1.set("pageNo", pageNo);
		sendObject1.set("pageSize", pageSize);
		sendObject1.set("startTime", hcConstructionresults.getBegintime());
		sendObject1.set("endTime", hcConstructionresults.getEndtime());
		JSONObject codety = getCodety(sendObject1, tfysurl, token);
		System.out.println(codety);
		JSONObject data = (JSONObject) codety.get("data");
		JSONArray records = (JSONArray) data.get("records");
		Page<Map<String, Object>> objectPage = new Page<Map<String, Object>>(pageNo,pageSize);
		objectPage.setSize(pageSize);
		objectPage.setTotal((Integer) data.get("rowCount"));
		objectPage.setCurrent(pageNo);
		List<Map<String, Object>> list = new ArrayList<>();
		for (Object record : records) {
			Map<String, Object> map = new HashMap<>();
			JSONObject object = new JSONObject(record);
			map.put("border",object.get("border").toString());
			map.put("endStation",object.get("endStation").toString());
			map.put("inspectStat",object.get("inspectStat").toString());
			map.put("blockName",object.get("blockName").toString());
			map.put("timesVibrateAvg",object.get("timesVibrateAvg").toString());
			map.put("alarmNum",object.get("alarmNum").toString());
			map.put("vibrateRatio",object.get("vibrateRatio").toString());
			map.put("workTime",object.get("workTime").toString());
			map.put("blockId",object.get("blockId").toString());
			map.put("qualityStat",object.get("qualityStat").toString());
			map.put("speedAvg",object.get("speedAvg").toString());
			map.put("thickAvg",object.get("thickAvg").toString());
			map.put("workArea",object.get("workArea").toString());
			map.put("workStat",object.get("workStat").toString());
			map.put("workMile",object.get("workMile").toString());
			map.put("timesAvg",object.get("timesAvg").toString());
			map.put("startStation",object.get("startStation").toString());
			map.put("startTime",object.get("startTime").toString());
			map.put("layerName",object.get("layerName").toString());
			map.put("endTime",object.get("endTime").toString());
			list.add(map);
		}
		objectPage.setRecords(list);
//		System.out.println(list);
//		System.out.println(objectPage);
		return Result.OK(objectPage);
	}

	/**
	 * 调用isite平台接口
	 * 逐桩摊铺温度 逐桩温度离析 逐桩摊铺速度
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/listisite")
	public Result<?> queryPageListisite(HcConstructionresults hcConstructionresults){

		// 获取token
		Map<String, Object> hctoken = projectService.selectByProjectid(hcConstructionresults.getProjectid());
		String token = "";
		if(hctoken == null){
			JSONObject sendObject = JSONUtil.createObj();
			sendObject.set("appId", hcId);
			sendObject.set("pw", pw);
			token = getCode(sendObject, url);
		}else{
			token = (String) hctoken.get("token");
		}
		JSONObject sendObject1 = JSONUtil.createObj();
		sendObject1.set("projectId", hcConstructionresults.getProjectid());
		sendObject1.set("sectionId", hcConstructionresults.getSectionid());
		sendObject1.set("startTime", hcConstructionresults.getBegintime());
		sendObject1.set("endTime", hcConstructionresults.getEndtime());
		JSONObject codety = getCodety(sendObject1, tpwdurl, token);
		System.out.println(codety);
		JSONObject data = (JSONObject) codety.get("data");
		JSONArray records = (JSONArray) data.get("records");

		return Result.OK(records);
	}

	/**
	 * 调用isite平台接口
	 * 摊铺温度合格率/摊铺速度合格率
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/listisitewdhgl")
	public Result<?> queryPageListisitewdhgl(HcConstructionresults hcConstructionresults,Integer type){

		// 获取token
		Map<String, Object> hctoken = projectService.selectByProjectid(hcConstructionresults.getProjectid());
		String token = "";
		if(hctoken == null){
			JSONObject sendObject = JSONUtil.createObj();
			sendObject.set("appId", hcId);
			sendObject.set("pw", pw);
			token = getCode(sendObject, url);
		}else{
			token = (String) hctoken.get("token");
		}
		JSONObject sendObject1 = JSONUtil.createObj();
		sendObject1.set("projectId", hcConstructionresults.getProjectid());
		sendObject1.set("sectionId", hcConstructionresults.getSectionid());
		sendObject1.set("startTime", hcConstructionresults.getBegintime());
		sendObject1.set("endTime", hcConstructionresults.getEndtime());
		JSONObject codety = null;
		if (type == 1){
			codety = getCodety(sendObject1, tplxurl, token);
		}else {
			codety = getCodety(sendObject1, tpsdurl, token);
		}
		JSONObject data = (JSONObject) codety.get("data");
		return Result.OK(data);
	}
	/**
	 * 调用isite平台接口
	 * 最高温度合格率/碾压速度合格率
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/listisitesdhgl")
	public Result<?> queryPageListisitesdhgl(HcConstructionresults hcConstructionresults,Integer type){

		// 获取token
		Map<String, Object> hctoken = projectService.selectByProjectid(hcConstructionresults.getProjectid());
		String token = "";
		if(hctoken == null){
			JSONObject sendObject = JSONUtil.createObj();
			sendObject.set("appId", hcId);
			sendObject.set("pw", pw);
			token = getCode(sendObject, url);
		}else{
			token = (String) hctoken.get("token");
		}
		JSONObject sendObject1 = JSONUtil.createObj();
		sendObject1.set("projectId", hcConstructionresults.getProjectid());
		sendObject1.set("sectionId", hcConstructionresults.getSectionid());
		sendObject1.set("startTime", hcConstructionresults.getBegintime());
		sendObject1.set("endTime", hcConstructionresults.getEndtime());
		JSONObject codety = null;
		if (type == 1){
			codety = getCodety(sendObject1, zgwdurl, token);
		}else {
			codety = getCodety(sendObject1, lysdurl, token);
		}
		JSONObject data = (JSONObject) codety.get("data");

		return Result.OK(data);
	}
	/**
	 * 调用isite平台接口
	 * 碾压遍数合格率
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/listisitelybs")
	public Result<?> queryPageListisitelybs(HcConstructionresults hcConstructionresults){

		// 获取token
		Map<String, Object> hctoken = projectService.selectByProjectid(hcConstructionresults.getProjectid());
		String token = "";
		if(hctoken == null){
			JSONObject sendObject = JSONUtil.createObj();
			sendObject.set("appId", hcId);
			sendObject.set("pw", pw);
			token = getCode(sendObject, url);
		}else{
			token = (String) hctoken.get("token");
		}
		JSONObject sendObject1 = JSONUtil.createObj();
		sendObject1.set("projectId", hcConstructionresults.getProjectid());
		sendObject1.set("sectionId", hcConstructionresults.getSectionid());
		sendObject1.set("startTime", hcConstructionresults.getBegintime());
		sendObject1.set("endTime", hcConstructionresults.getEndtime());
		JSONObject codety = getCodety(sendObject1, lybsurl, token);
		JSONObject data = (JSONObject) codety.get("data");

		return Result.OK(data);
	}
	/**
	 * 调用isite平台接口
	 * 每日欠压率
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/listisitemrqy")
	public Result<?> queryPageListisitemrqy(HcConstructionresults hcConstructionresults){

		// 获取token
		Map<String, Object> hctoken = projectService.selectByProjectid(hcConstructionresults.getProjectid());
		String token = "";
		if(hctoken == null){
			JSONObject sendObject = JSONUtil.createObj();
			sendObject.set("appId", hcId);
			sendObject.set("pw", pw);
			token = getCode(sendObject, url);
		}else{
			token = (String) hctoken.get("token");
		}
		JSONObject sendObject1 = JSONUtil.createObj();
		sendObject1.set("projectId", hcConstructionresults.getProjectid());
		sendObject1.set("sectionId", hcConstructionresults.getSectionid());
		sendObject1.set("startTime", hcConstructionresults.getBegintime());
		sendObject1.set("endTime", hcConstructionresults.getEndtime());
		JSONObject codety = getCodety(sendObject1, mrqyurl, token);
		JSONArray data = (JSONArray) codety.get("data");
		return Result.OK(data);
	}

	// 获取token
	public static String getCode(JSONObject sendObject, String url) {
		String body = HttpRequest.post(url)
				.header("Content-Type", "application/json")
				.body(String.valueOf(sendObject))
				.execute()
				.body();
		JSONObject jsonObject1 = new JSONObject(body);
		JSONObject jsonObject2 = (JSONObject) jsonObject1.get("data");
		return (String) jsonObject2.get("token");
	}
	// 统一接口调用
	public static JSONObject getCodety(JSONObject sendObject, String url,String token) {
		String body = HttpRequest.post(url)
				.header("token", token)
				.body(String.valueOf(sendObject))
				.execute()
				.body();
		JSONObject jsonObject1 = new JSONObject(body);
		return jsonObject1;
	}

	/**
	 * 分页列表查询
	 *
	 * @param hcConstructionresults
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/listfp")
	public Result<?> queryPageListfp(HcConstructionresults hcConstructionresults,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String code = loginUser.getOrgCode() ;
		if (StringUtils.isNotBlank(sys_depart_orgcode) ){
			code = sys_depart_orgcode;
		}

		String sectionlist = sectionService.listByOrgCode(code);
		if(StringUtils.isNotBlank(sectionlist) && StringUtils.isBlank(hcConstructionresults.getSectionid())  ){
			hcConstructionresults.setSectionid(sectionlist);
		}else if(StringUtils.isNotBlank(hcConstructionresults.getSectionid()) ){
		}else{
			hcConstructionresults.setSectionid("空");
		}
		QueryWrapper<HcConstructionresults> queryWrapper = QueryGenerator.initQueryWrapper(hcConstructionresults, req.getParameterMap());
		Page<HcConstructionresults> page = new Page<HcConstructionresults>(pageNo, pageSize);
		IPage<HcConstructionresults> pageList = hcConstructionresultsService.page(page, queryWrapper);
		for(HcConstructionresults one : pageList.getRecords()){
			one.setEndstation(getEndstation(one.getEndstation()));
			one.setStartstation(getStartstation(one.getStartstation()));
		}
		return Result.OK(pageList);
	}

	/**
	 * 分页列表查询
	 *
	 * @param hcConstructionresults
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcConstructionresults hcConstructionresults,
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
		IPage<HcConstructionresults> hcConstructionresultsIPage = new Page<>(pageNo,pageSize);
		List<HcConstructionresults> recor = new ArrayList<>();
		if (list1.size() > 0){
			for (String l :list1){
				//内嵌了ORDER BY id DESC，导致报错
				//	QueryWrapper<HcConstructionresults> queryWrapper = QueryGenerator.initQueryWrapper(hcConstructionresults, req.getParameterMap());
				QueryWrapper<HcConstructionresults> queryWrapper = new QueryWrapper<>(hcConstructionresults);
				if (date_begin != null){
					queryWrapper.gt("date",date_begin);
				}
				if (date_end != null){
					queryWrapper.lt("date",date_end);
				}
				queryWrapper.eq("projectId",l);
				queryWrapper.select("id,date,projectId,sectionId,layerName,offset,min(startStation) startStation,max(endStation) endStation,sum(workMileage) workMileage");
				queryWrapper.last("group by date,layerIndex,projectId,sectionId order by date desc");
				List<HcConstructionresults> records = hcConstructionresultsService.list(queryWrapper);
				if (records.size() > 0){
					for (HcConstructionresults record :records){
						String projectid = record.getProjectid();
						String sectionid = record.getSectionid();
						String startstation = record.getStartstation();
						String endstation = record.getEndstation();
						String startstation1 = getStartstation(startstation);
						String endstation1 = getEndstation(endstation);
						QueryWrapper<HcProject> queryWrapper1 = new QueryWrapper<>();
						queryWrapper1.eq("pjId",projectid);
						HcProject one = projectService.getOne(queryWrapper1);

						QueryWrapper<HcSection> queryWrapper2 = new QueryWrapper<>();
						queryWrapper2.eq("sectionId",sectionid);
						HcSection one1 = sectionService.getOne(queryWrapper2);

						if(one != null){
							record.setPjname(one.getPjname());
						}
						if(one1 != null){
							record.setSectionname(one1.getSectionname());
						}
						record.setStartstation(startstation1);
						record.setEndstation(endstation1);
					}
				}
				recor.addAll(records);
			}
		}
		IPage<HcConstructionresults> pageList = hcConstructionresultsIPage.setRecords(recor);
		return Result.OK(pageList);
	}

	/**
	 * 分页列表查询
	 *
	 * @param hcConstructionresults
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/listszxs")
	public Result<?> queryPageListszxs(HcConstructionresults hcConstructionresults,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="100") Integer pageSize,
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
		Page<HcConstructionresults> page = new Page<>();
		if (list1.size() > 0){
			//内嵌了ORDER BY id DESC，导致报错
			QueryWrapper<HcConstructionresults> queryWrapper = QueryGenerator.initQueryWrapper(hcConstructionresults, req.getParameterMap());
			queryWrapper.in("projectId",list1);
			Page<HcConstructionresults> objectPage = new Page<HcConstructionresults>(pageNo,pageSize);
			page = hcConstructionresultsService.page(objectPage, queryWrapper);
			List<HcConstructionresults> records = page.getRecords();
			if (records.size() > 0){
				for (HcConstructionresults record :records){
					String totaltimesratio = record.getTotaltimesratio();
					double v = Double.parseDouble(totaltimesratio);
					if (v > 0.9 && v <= 1){
						record.setTotaltimesratio("1");
					}else if (v > 0.8 && v <= 0.9){
						record.setTotaltimesratio("2");
					}else if (v > 0.7 && v <= 0.8){
						record.setTotaltimesratio("3");
					}else if (v > 0.6 && v <= 0.7){
						record.setTotaltimesratio("4");
					}else {
						record.setTotaltimesratio("5");
					}
				}
			}
		}
		return Result.OK(page);
	}

	/**
	 * 摊铺碱压统计 速度，温度，遍数
	 *
	 * @param hcConstructionresults
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/listTptj")
	public Result<?> queryPageListTptj(HcConstructionresults hcConstructionresults,
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
		ArrayList<Map<String, Double>> list2 = new ArrayList<>();
		Map<String, Double> map = new HashMap<>();
		if (list1.size() > 0){
			List<String> records = hcConstructionresultsService.selecid(date_begin,date_end,list1);
			if(records.size() > 0){
				QueryWrapper<HcConstructionresultsNy> queryWrapper = new QueryWrapper<>();
				queryWrapper.select("count(*) id,sum(tempRatio) tempRatio,sum(velocityRatio) velocityRatio,sum(timesRatio) timesRatio");
				queryWrapper.in("crid",records);
				HcConstructionresultsNy one = hcConstructionresultsNyService.getOne(queryWrapper);

				QueryWrapper<HcConstructionresultsTp> queryWrapper1 = new QueryWrapper<>();
				queryWrapper1.select("count(*) id,sum(velocityRatio) velocityRatio");
				queryWrapper1.in("crid",records);
				HcConstructionresultsTp one1 = hcConstructionresultsTpService.getOne(queryWrapper1);
				double v = Double.parseDouble(one.getVelocityratio());
				double v1 = Double.parseDouble(one1.getVelocityratio());
				double i = (double)(one.getId() + one1.getId());

				double v2 = (v + v1) * 100 / i;
				double v3 = Double.parseDouble(one.getTempratio()) * 100 / one.getId();
				double v4 = Double.parseDouble(one.getTimesratio()) * 100 / one.getId();
				map.put("sd", Double.valueOf(String.format("%.2f",v2)));
				map.put("wd", Double.valueOf(String.format("%.2f",v3)));
				map.put("ds",Double.valueOf(String.format("%.2f",v4)));
			}else {
				map.put("sd",0.0);
				map.put("wd",0.0);
				map.put("ds",0.0);
			}
		}
		list2.add(map);
		return Result.OK(list2);
	}

	/**
	 * 摊铺碱压统计 上1，中，下0
	 *
	 * @param hcConstructionresults
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "施工成果-分页列表查询")
	@ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	@GetMapping(value = "/listSzx")
	public Result<?> queryPageListSzx(HcConstructionresults hcConstructionresults,
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
		QueryWrapper<ProjOverview> queryWrapper1 = new QueryWrapper<>();
		queryWrapper1.likeRight("sys_org_code",sys_depart_orgcode);
		ProjOverview one1 = projOverviewService.getOne(queryWrapper1);
		QueryWrapper<HcSection> queryWrapper8 = QueryGenerator.initQueryWrapper(hcSection, req.getParameterMap());
		queryWrapper8.groupBy("pjId");
		List<HcSection> list = sectionService.list(queryWrapper8);
		List<String> list1 = new ArrayList<>();
		if (list.size() > 0){
			for (HcSection l :list){
				list1.add(l.getPjid());
			}
		}
		ArrayList<Map<String, Double>> list2 = new ArrayList<>();
		Map<String, Double> map = new HashMap<>();
		if (one1 != null){
			if (list1.size() > 0){
				QueryWrapper<HcConstructionresults> queryWrapper = new QueryWrapper<>();
				queryWrapper.select("sum(workMileage) workMileage");
				queryWrapper.like("layerName","上面层");
				if (date_begin != null){
					queryWrapper.gt("date",date_begin);
				}
				if (date_end != null){
					queryWrapper.lt("date",date_end);
				}
				queryWrapper.in("projectId",list1);
				HcConstructionresults one = hcConstructionresultsService.getOne(queryWrapper);

				QueryWrapper<HcConstructionresults> queryWrapper2 = new QueryWrapper<>();
				queryWrapper2.select("sum(workMileage) workMileage");
				queryWrapper2.like("layerName","中面层");
				if (date_begin != null){
					queryWrapper2.gt("date",date_begin);
				}
				if (date_end != null){
					queryWrapper2.lt("date",date_end);
				}
				queryWrapper2.in("projectId",list1);
				HcConstructionresults one2 = hcConstructionresultsService.getOne(queryWrapper2);
				QueryWrapper<HcConstructionresults> queryWrapper3 = new QueryWrapper<>();
				queryWrapper3.select("sum(workMileage) workMileage");
				queryWrapper3.like("layerName","下面层");
				if (date_begin != null){
					queryWrapper3.gt("date",date_begin);
				}
				if (date_end != null){
					queryWrapper3.lt("date",date_end);
				}
				queryWrapper3.in("projectId",list1);
				HcConstructionresults one3 = hcConstructionresultsService.getOne(queryWrapper3);
				Double percentStrss = 0.0;
				Double percentStrss1 = 0.0;
				Double percentStrss2 = 0.0;
				if (one != null){
					percentStrss = getPercentStrss(Double.parseDouble(one.getWorkmileage()), Double.parseDouble(one1.getIllustrate()));
					if (percentStrss > 100){
						percentStrss = 100.0;
					}
				}
				if (one2 != null){
					percentStrss1 = getPercentStrss(Double.parseDouble(one2.getWorkmileage()), Double.parseDouble(one1.getIllustrate()));
					if (percentStrss1 > 100){
						percentStrss1 = 100.0;
					}
				}
				if (one3 != null){
					percentStrss2 = getPercentStrss(Double.parseDouble(one3.getWorkmileage()), Double.parseDouble(one1.getIllustrate()));
					if (percentStrss2 > 100){
						percentStrss2 = 100.0;
					}
				}
				map.put("smc",percentStrss);
				map.put("zmc",percentStrss1);
				map.put("xmc",percentStrss2);
			}
		}else {
			map.put("smc",0.0);
			map.put("zmc",0.0);
			map.put("xmc",0.0);
		}
		list2.add(map);
		return Result.OK(list2);
	}
	/**
	 * 得到百分比的字符串，比如传参 1,3，返回33.33
	 */
	private Double getPercentStrss(double diff, double sum) {
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数
		if (sum == 0) {
			return 0.00;
		}
		double v = (diff * 100) / sum;
		Double str = v;

		return str;
	}

	 /**
	  * 摊铺详情接口
	  *
	  * @param hcConstructionresults
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "施工成果-分页列表查询")
	 @ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	 @GetMapping(value = "/listtp")
	 public Result<?> queryPageListtp(HcConstructionresults hcConstructionresults,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) throws InvocationTargetException, IllegalAccessException {
		 QueryWrapper<HcConstructionresults> queryWrapper = new QueryWrapper<>(hcConstructionresults);
		 List<HcConstructionresults> records = hcConstructionresultsService.list(queryWrapper);
		 List<HcConstructionresultsTPNY> records1 = new ArrayList<>();
		 if (records.size() > 0){
			 for (HcConstructionresults record :records){
				 String projectid = record.getProjectid();
				 String sectionid = record.getSectionid();
				 String startstation = record.getStartstation();
				 String endstation = record.getEndstation();
				 String startstation1 = getStartstation(startstation);
				 String endstation1 = getEndstation(endstation);
				 QueryWrapper<HcProject> queryWrapper1 = new QueryWrapper<>();
				 queryWrapper1.eq("pjId",projectid);
				 HcProject one = projectService.getOne(queryWrapper1);

				 QueryWrapper<HcSection> queryWrapper2 = new QueryWrapper<>();
				 queryWrapper2.eq("sectionId",sectionid);
				 HcSection one1 = sectionService.getOne(queryWrapper2);

				 record.setPjname(one.getPjname());
				 record.setSectionname(one1.getSectionname());
				 record.setStartstation(startstation1);
				 record.setEndstation(endstation1);
				 QueryWrapper<HcConstructionresultsTp> queryWrapper3 = new QueryWrapper<>();
				 queryWrapper3.eq("crid",record.getId());
				 List<HcConstructionresultsTp> list = hcConstructionresultsTpService.list(queryWrapper3);
				 HcConstructionresultsTPNY hcConstructionresultsTPNY = new HcConstructionresultsTPNY();
				 BeanUtils.copyProperties(record,hcConstructionresultsTPNY);
				 hcConstructionresultsTPNY.setPaver(list);
				 records1.add(hcConstructionresultsTPNY);
			 }
		 }
		 return Result.OK(records1);
	 }


	 /**
	  * 碾压详情接口
	  *
	  * @param hcConstructionresults
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "施工成果-分页列表查询")
	 @ApiOperation(value="施工成果-分页列表查询", notes="施工成果-分页列表查询")
	 @GetMapping(value = "/listny")
	 public Result<?> queryPageListny(HcConstructionresults hcConstructionresults,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) throws InvocationTargetException, IllegalAccessException {
		 QueryWrapper<HcConstructionresults> queryWrapper = new QueryWrapper<>(hcConstructionresults);
		 List<HcConstructionresults> records = hcConstructionresultsService.list(queryWrapper);
		 List<HcConstructionresultsTPNY> records1 = new ArrayList<>();
		 if (records.size() > 0){
			 for (HcConstructionresults record :records){
				 String projectid = record.getProjectid();
				 String sectionid = record.getSectionid();
				 String startstation = record.getStartstation();
				 String endstation = record.getEndstation();
				 String startstation1 = getStartstation(startstation);
				 String endstation1 = getEndstation(endstation);
				 QueryWrapper<HcProject> queryWrapper1 = new QueryWrapper<>();
				 queryWrapper1.eq("pjId",projectid);
				 HcProject one = projectService.getOne(queryWrapper1);

				 QueryWrapper<HcSection> queryWrapper2 = new QueryWrapper<>();
				 queryWrapper2.eq("sectionId",sectionid);
				 HcSection one1 = sectionService.getOne(queryWrapper2);

				 record.setPjname(one.getPjname());
				 record.setSectionname(one1.getSectionname());
				 record.setStartstation(startstation1);
				 record.setEndstation(endstation1);
				 QueryWrapper<HcConstructionresultsNy> queryWrapper3 = new QueryWrapper<>();
				 queryWrapper3.eq("crid",record.getId());
				 List<HcConstructionresultsNy> list = hcConstructionresultsNyService.list(queryWrapper3);
				 HcConstructionresultsTPNY hcConstructionresultsTPNY = new HcConstructionresultsTPNY();
				 BeanUtils.copyProperties(record,hcConstructionresultsTPNY);
				 hcConstructionresultsTPNY.setRollerList(list);
				 records1.add(hcConstructionresultsTPNY);
			 }
		 }
		 return Result.OK(records1);
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

	 public String getEndstation(String endstation){
		 String startsta = null;
		 if (endstation != null){
			 if(endstation.length() == 2){
				 startsta = "K0+0" + endstation;
			 }else if (endstation.length() == 3){
				 startsta = "K0+" + endstation;
			 }else if (endstation.length() > 3){
				 String substring = endstation.substring(0, endstation.length() - 3);
				 String substring1 = endstation.substring(endstation.length() - 3);
				 startsta = "K" + substring + "+" + substring1;
			 }
		 }
		 return startsta;
	 }

	/**
	 *   添加
	 *
	 * @param hcConstructionresults
	 * @return
	 */
	@AutoLog(value = "施工成果-添加")
	@ApiOperation(value="施工成果-添加", notes="施工成果-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcConstructionresults hcConstructionresults) {
		hcConstructionresultsService.save(hcConstructionresults);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hcConstructionresults
	 * @return
	 */
	@AutoLog(value = "施工成果-编辑")
	@ApiOperation(value="施工成果-编辑", notes="施工成果-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcConstructionresults hcConstructionresults) {
		hcConstructionresultsService.updateById(hcConstructionresults);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "施工成果-通过id删除")
	@ApiOperation(value="施工成果-通过id删除", notes="施工成果-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcConstructionresultsService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "施工成果-批量删除")
	@ApiOperation(value="施工成果-批量删除", notes="施工成果-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcConstructionresultsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 *  批量修改
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "施工成果-批量修改所属标段")
	@ApiOperation(value="施工成果-批量删除", notes="施工成果-批量修改所属标段")
	@GetMapping(value = "/editBatch")
	public Result<?> editBatch( String ids,String projectid,String sectionid) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		Pushandreturn log = new Pushandreturn();
		log.setReturnvalue(loginUser.getRealname());
		log.setPushname("批量修改所属标段");
		log.setPushjson("ids:"+ids+";projectid:"+projectid+";sectionid:"+sectionid);
	  pushandreturnService.save(log);
		this.hcConstructionresultsService.updateSection(ids,projectid,sectionid);
		return Result.OK("批量修改所属标段成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "施工成果-通过id查询")
	@ApiOperation(value="施工成果-通过id查询", notes="施工成果-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcConstructionresults hcConstructionresults = hcConstructionresultsService.getById(id);
		if(hcConstructionresults==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcConstructionresults);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcConstructionresults
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcConstructionresults hcConstructionresults) {
        return super.exportXls(request, hcConstructionresults, HcConstructionresults.class, "施工成果");
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
        return super.importExcel(request, response, HcConstructionresults.class);
    }

}
