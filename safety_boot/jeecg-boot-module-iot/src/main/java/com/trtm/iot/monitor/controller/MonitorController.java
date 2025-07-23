package com.trtm.iot.monitor.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.monitor.tool.ArtemisTool;
import com.trtm.iot.monitor.tool.HttpRequestTool;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.monitor.entity.Monitor;
import com.trtm.iot.monitor.service.IMonitorService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: monitor
 * @Author: jeecg-boot
 * @Date:   2021-12-16
 * @Version: V1.0
 */
@Api(tags="monitor")
@RestController
@RequestMapping("/monitor/monitor")
@Slf4j
public class MonitorController extends JeecgController<Monitor, IMonitorService> {
	@Autowired
	private IMonitorService monitorService;

	@Autowired
	private ArtemisTool artemisTool;

	/**
	 * 分页列表查询
	 *
	 * @param monitor
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "monitor-分页列表查询")
	@ApiOperation(value="monitor-分页列表查询", notes="monitor-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Monitor monitor,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (monitor.getOrgcode() != null){
			monitor.setOrgcode(monitor.getOrgcode()+"*");
		}else if(StringUtils.isNotBlank(sys_depart_orgcode)){
			monitor.setOrgcode(sys_depart_orgcode+"*");
		}else{
			monitor.setOrgcode(loginUser.getOrgCode()+"*");
		}
		monitor.setIsdel(0);
		QueryWrapper<Monitor> queryWrapper = QueryGenerator.initQueryWrapper(monitor, req.getParameterMap());
		Page<Monitor> page = new Page<Monitor>(pageNo, pageSize);
		IPage<Monitor> pageList = monitorService.page(page, queryWrapper);
		List<Monitor> records = pageList.getRecords();
		// 摄像头调取视频优化-- 管桩app用
		if(StringUtils.isNotBlank(monitor.getRemark()) && records.size()>0 && records.get(0).getCamtype() == 1 ){
			try {
				String video = (String) ArtemisTool.getPreviewURLs(records.get(0).getVerificationcode(),"hls");
				records.get(0).setVaddress(video);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return Result.OK(pageList);
	}

	 @AutoLog(value = "monitor-视频接入")
	 @ApiOperation(value="monitor-分页列表查询", notes="monitor-视频接入")
	 @GetMapping(value = "/getVideoLive")
	 public Result<?> getVideoLive(Monitor monitor,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									HttpServletRequest req) throws Exception {
		 if (monitor.getOrgcode() != null){
			 monitor.setOrgcode(monitor.getOrgcode()+"*");
		 }
		 monitor.setIsdel(0);
		 QueryWrapper<Monitor> queryWrapper = QueryGenerator.initQueryWrapper(monitor, req.getParameterMap());
		 Page<Monitor> page = new Page<Monitor>(pageNo, pageSize);
		 IPage<Monitor> pageList = monitorService.page(page, queryWrapper);
		 if(pageList.getRecords().size()>0){
			 Monitor monitor1 = pageList.getRecords().get(0);
			 if(monitor1.getCamtype() == 0){
				 String token =getVideoToken ();
				 return Result.OK("0","http://47.97.173.113:9271/VideoMonitor?id="+monitor1.getId()+"&token="+token);
			 }else if(monitor1.getCamtype() == 1){
				 String video = (String) ArtemisTool.getPreviewURLs(monitor1.getVerificationcode(),"hls");
				 monitor1.setVaddress(video);
				 monitorService.updateById(monitor);
				 return Result.OK("1",video);
			 }

		 }

		 return Result.OK("该设备未接入视频监控","该设备未接入视频监控");

	 }


	 @AutoLog(value = "monitor-云台接入")
	 @ApiOperation(value="monitor-分页列表查询", notes="monitor-云台接入")
	 @GetMapping(value = "/getControlling")
	 public Result<?> getControlling(String cameraCode, String command,Integer action,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) throws Exception {

		 Object controlling = ArtemisTool.controlling(cameraCode, command, action);
		 return Result.OK("1");

	 }


	 /**
	  * 分页列表查询
	  *
	  * @param monitor
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "monitor-分页列表查询")
	 @ApiOperation(value="monitor-分页列表查询", notes="monitor-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(Monitor monitor,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			 monitor.setOrgcode(sys_depart_orgcode + "*");
		 }else {
			 monitor.setOrgcode(loginUser.getOrgCode() + "*");
		 }
		 monitor.setIsdel(0);
		 QueryWrapper<Monitor> queryWrapper = QueryGenerator.initQueryWrapper(monitor, req.getParameterMap());
		 Page<Monitor> page = new Page<Monitor>(pageNo, pageSize);
		 IPage<Monitor> pageList = monitorService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 @AutoLog(value = "monitor-分页列表查询")
	 @ApiOperation(value="monitor-分页列表查询", notes="monitor-分页列表查询")
	 @GetMapping(value = "/queryList")
	 public Result<?> queryList(Monitor monitor,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<Monitor> queryWrapper = new QueryWrapper();
		 queryWrapper.likeRight("orgcode",monitor.getOrgcode());
		 if(monitor.getUsetype() != null){
			 queryWrapper.likeRight("usetype",monitor.getUsetype());
		 }
		 queryWrapper.eq("isdel",0);
		 List<Monitor> monitorList = monitorService.list(queryWrapper);
//		 Page<Monitor> page = new Page<Monitor>(pageNo, pageSize);
//		 IPage<Monitor> pageList = monitorService.page(page, queryWrapper);
		 return Result.OK(monitorList);
	 }

	 @GetMapping(value = "/getcount")
	 public Result<?> getcount(Monitor monitor) {
		 QueryWrapper<Monitor> queryWrapper = new QueryWrapper();
		 queryWrapper.select(" count( id ) count , " +
				 " sum(workstate) closecount, "+
				 "  LEFT ( usetype, 1 ) usetype ");
		 queryWrapper.likeRight("orgcode",monitor.getOrgcode());
		 queryWrapper.last("group by LEFT ( usetype, 1 ) ");
		 List<Map<String, Object>> countInfo = monitorService.listMaps(queryWrapper);

		 return Result.OK(countInfo);
	 }

	/**
	 *   添加
	 *
	 * @param monitor
	 * @return
	 */
	@AutoLog(value = "monitor-添加")
	@ApiOperation(value="monitor-添加", notes="monitor-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Monitor monitor) {
		monitorService.save(monitor);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param monitor
	 * @return
	 */
	@AutoLog(value = "monitor-编辑")
	@ApiOperation(value="monitor-编辑", notes="monitor-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Monitor monitor) {
		monitorService.updateById(monitor);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id物理删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "monitor-通过id删除")
	@ApiOperation(value="monitor-通过id删除", notes="monitor-通过id删除")
	@DeleteMapping(value = "/deleteid")
	public Result<?> deleteid(@RequestParam(name="id",required=true) String id) {
		monitorService.removeById(id);
		return Result.OK("删除成功!");
	}
	 /**
	  *   通过id逻辑删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "monitor-通过id删除")
	 @ApiOperation(value="monitor-通过id删除", notes="monitor-通过id删除")
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		 Monitor monitor = monitorService.getById(id);
		 monitor.setIsdel(1);
		 monitorService.updateById(monitor);
		 return Result.OK("删除成功!");
	 }

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "monitor-批量删除")
	@ApiOperation(value="monitor-批量删除", notes="monitor-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.monitorService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "monitor-通过id查询")
	@ApiOperation(value="monitor-通过id查询", notes="monitor-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Monitor monitor = monitorService.getById(id);
		if(monitor==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(monitor);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param monitor
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Monitor monitor) {
        return super.exportXls(request, monitor, Monitor.class, "monitor");
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
        return super.importExcel(request, response, Monitor.class);
    }

	 private String getVideoToken() {
		 Map<String, String> param = new HashMap<>();
		 param.put("apiid", "1");
		 param.put("apisceret", "CB093DD1D932456C9D33B2E25CD9CFF5");
		 com.alibaba.fastjson.JSONObject sr = HttpRequestTool.sendPost("http://js.traiot.cn:8081/dataprovider/gettoken", param);
		 com.alibaba.fastjson.JSONObject data = com.alibaba.fastjson.JSONObject.parseObject(sr.getString("data"));
		 String token = data.getString("token");
		 return token;
	 }

}
