package com.trtm.sy.syshrwd.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.sy.syshrwd.entity.Syshrwd;
import com.trtm.sy.syshrwd.service.ISyshrwdService;

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
 * @Description: 试验收货任务单
 * @Author: jeecg-boot
 * @Date:   2022-09-08
 * @Version: V1.0
 */
@Api(tags="试验收货任务单")
@RestController
@RequestMapping("/syshrwd/syshrwd")
@Slf4j
public class SyshrwdController extends JeecgController<Syshrwd, ISyshrwdService> {
	@Autowired
	private ISyshrwdService syshrwdService;

	 @Autowired
	 private ISysMessageCoreService sysMessageService;
	
	/**
	 * 分页列表查询
	 *
	 * @param syshrwd
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "试验收货任务单-分页列表查询")
	@ApiOperation(value="试验收货任务单-分页列表查询", notes="试验收货任务单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Syshrwd syshrwd,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req ,String sys_depart_orgcode) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode == null) {
			syshrwd.setMudidi(loginUser.getOrgCode() + "*");
		} else {
			syshrwd.setMudidi(sys_depart_orgcode + "*");
		}
		QueryWrapper<Syshrwd> queryWrapper = QueryGenerator.initQueryWrapper(syshrwd, req.getParameterMap());
		Page<Syshrwd> page = new Page<Syshrwd>(pageNo, pageSize);
		IPage<Syshrwd> pageList = syshrwdService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param syshrwd
	 * @return
	 */
	@AutoLog(value = "试验收货任务单-添加")
	@ApiOperation(value="试验收货任务单-添加", notes="试验收货任务单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Syshrwd syshrwd) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		syshrwd.setStatus("4");
		syshrwd.setCreateby(loginUser.getUsername());
		syshrwd.setCaeatetime(new Date());
		syshrwd.setCreatephone(loginUser.getPhone());
		SimpleDateFormat sdf1 = new SimpleDateFormat("YYYYMMddhhmmss");
		String Shrwd = "SHRWBH"+ sdf1.format(new Date());
		syshrwd.setShrwd(Shrwd);
		syshrwd.setGuid(String.valueOf(UUID.randomUUID()));
		syshrwdService.save(syshrwd);

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param syshrwd
	 * @return
	 */
	@AutoLog(value = "试验收货任务单-编辑")
	@ApiOperation(value="试验收货任务单-编辑", notes="试验收货任务单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Syshrwd syshrwd) {
		syshrwdService.updateById(syshrwd);
		if(null != syshrwd.getIsmsg() && syshrwd.getIsmsg()){
			SysMessageCore sysMessageCore = new SysMessageCore();
			sysMessageCore.setEsTitle("送货任务");
			sysMessageCore.setEsType("1");
			sysMessageCore.setEsSendStatus("0");
			sysMessageCore.setEsReceiver(syshrwd.getPhone());
			JSONObject obj = new JSONObject();
			obj.put("sbname",syshrwd.getMudidi());
			obj.put("time", new Date());
			obj.put("content", "送货单（"+syshrwd.getShrwd()+"）已修改,请前往App确认");
			sysMessageCore.setEsContent(obj.toString());
			sysMessageService.save(sysMessageCore);
		}
		return Result.OK("编辑成功!");
	}


	 /**
	  *  审核
	  *
	  * @param syshrwd
	  * @return
	  */
	 @AutoLog(value = "试验收货任务单-监理审核")
	 @ApiOperation(value="试验收货任务单-编辑", notes="试验收货任务单-编辑")
	 @PutMapping(value = "/jledit")
	 public Result<?> jledit(@RequestBody Syshrwd syshrwd) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 syshrwd.setStatus("0");
		 syshrwd.setJlcheckp(loginUser.getUsername());// 监理确认人
		 syshrwd.setJlchecktime(new Date());// 监理确认时间
		 syshrwd.setJlphone(loginUser.getPhone());
		 syshrwdService.updateById(syshrwd);
		 if(null != syshrwd.getIsmsg() && syshrwd.getIsmsg()){
			 SysMessageCore sysMessageCore = new SysMessageCore();
			 sysMessageCore.setEsTitle("送货任务");
			 sysMessageCore.setEsType("1");
			 sysMessageCore.setEsSendStatus("0");
			 sysMessageCore.setEsReceiver(syshrwd.getPhone());
			 JSONObject obj = new JSONObject();
			 obj.put("sbname", syshrwd.getMudidi());
			 obj.put("time", new Date());
			 obj.put("content", "新送货单（"+syshrwd.getShrwd()+"）已下发,请前往App确认");
			 sysMessageCore.setEsContent(obj.toString());
			 sysMessageService.save(sysMessageCore);
		 }
		 return Result.OK("监理审核成功!");
	 }



	 /**
	  *  编辑
	  *
	  * @param syshrwd
	  * @return
	  */
	 @AutoLog(value = "试验收货任务单-厂家确认回函")
	 @ApiOperation(value="试验收货任务单-编辑", notes="试验收货任务单-编辑")
	 @PutMapping(value = "/cjedit")
	 public Result<?> cjedit(@RequestBody Syshrwd syshrwd) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 syshrwd.setStatus("1");
		 syshrwd.setOkby(loginUser.getUsername());// 确认人
		 syshrwd.setOktime(new Date());// 确认时间
		 syshrwdService.updateById(syshrwd);
		 return Result.OK("厂家确认成功!");
	 }


	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试验收货任务单-通过id删除")
	@ApiOperation(value="试验收货任务单-通过id删除", notes="试验收货任务单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syshrwdService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "试验收货任务单-批量删除")
	@ApiOperation(value="试验收货任务单-批量删除", notes="试验收货任务单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syshrwdService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试验收货任务单-通过id查询")
	@ApiOperation(value="试验收货任务单-通过id查询", notes="试验收货任务单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Syshrwd syshrwd = syshrwdService.getById(id);
		if(syshrwd==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syshrwd);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syshrwd
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Syshrwd syshrwd) {
        return super.exportXls(request, syshrwd, Syshrwd.class, "试验收货任务单");
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
        return super.importExcel(request, response, Syshrwd.class);
    }

}
