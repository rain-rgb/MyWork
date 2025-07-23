package com.trtm.iot.hctfysworkareaoverhandler.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.trtm.iot.hc_pavement_alarm.service.IHcPavementAlarmService;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkarea;
import com.trtm.iot.hc_tfysworkarea.service.IHcTfysworkareaService;
import com.trtm.iot.hctfysworkareaoverhandler.entity.HcTfysworkareaOverHandlerVo;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hctfysworkareaoverhandler.entity.HcTfysworkareaOverHandler;
import com.trtm.iot.hctfysworkareaoverhandler.service.IHcTfysworkareaOverHandlerService;

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
 * @Description: hc_tfysworkarea_over_handler
 * @Author: jeecg-boot
 * @Date:   2023-12-06
 * @Version: V1.0
 */
@Api(tags="hc_tfysworkarea_over_handler")
@RestController
@RequestMapping("/hctfysworkareaoverhandler/hcTfysworkareaOverHandler")
@Slf4j
public class HcTfysworkareaOverHandlerController extends JeecgController<HcTfysworkareaOverHandler, IHcTfysworkareaOverHandlerService> {
	@Autowired
	private IHcTfysworkareaOverHandlerService hcTfysworkareaOverHandlerService;
	 @Autowired
	 private IHcTfysworkareaService hcTfysworkareaService;
	 @Autowired
	 private IHcPavementAlarmService hcPavementAlarmService;
	/**
	 * 分页列表查询
	 *
	 * @param hcTfysworkareaOverHandler
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "hc_tfysworkarea_over_handler-分页列表查询")
	@ApiOperation(value="hc_tfysworkarea_over_handler-分页列表查询", notes="hc_tfysworkarea_over_handler-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcTfysworkareaOverHandler hcTfysworkareaOverHandler,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HcTfysworkareaOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(hcTfysworkareaOverHandler, req.getParameterMap());
		Page<HcTfysworkareaOverHandler> page = new Page<HcTfysworkareaOverHandler>(pageNo, pageSize);
		IPage<HcTfysworkareaOverHandler> pageList = hcTfysworkareaOverHandlerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param hcTfysworkareaOverHandler
	 * @return
	 */
	@AutoLog(value = "hc_tfysworkarea_over_handler-添加")
	@ApiOperation(value="hc_tfysworkarea_over_handler-添加", notes="hc_tfysworkarea_over_handler-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcTfysworkareaOverHandler hcTfysworkareaOverHandler) {
		hcTfysworkareaOverHandlerService.save(hcTfysworkareaOverHandler);
		return Result.OK("添加成功！");
	}

	 /**
	  * 试验机处置审核驳回
	  *
	  * @param hcTfysworkareaOverHandler
	  * @return
	  */
	 @AutoLog(value = "试验机处置审核表-处置审核驳回")
	 @ApiOperation(value = "试验机处置审核表-处置审核驳回", notes = "试验机处置审核表-处置审核驳回")
	 @GetMapping(value = "/deallist")
	 public Result<?> dealList(HcTfysworkareaOverHandler hcTfysworkareaOverHandler, Integer flag, HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 HcTfysworkareaOverHandler selectone = hcTfysworkareaOverHandlerService.selectone(hcTfysworkareaOverHandler.getBaseid());
		 HcTfysworkareaOverHandler hcTfysworkareaOverHandler1 = new HcTfysworkareaOverHandler();
		 BeanUtils.copyProperties(hcTfysworkareaOverHandler, hcTfysworkareaOverHandler1);
		 if (selectone == null) {
			 hcTfysworkareaOverHandler1.setHandlePerson(loginUser.getRealname());
			 hcTfysworkareaOverHandler1.setHandleTime(new Date());
			 hcTfysworkareaOverHandler1.setOverproofStatus(10);
			 hcTfysworkareaOverHandlerService.save(hcTfysworkareaOverHandler1);
		 } else {
			 hcTfysworkareaOverHandler1.setId(selectone.getId());
			 if (flag == 2) {
				 hcTfysworkareaOverHandler1.setHandlePerson(loginUser.getRealname());
				 hcTfysworkareaOverHandler1.setHandleTime(new Date());
				 hcTfysworkareaOverHandler1.setOverproofStatus(10);
			 } else if (flag == 3) {
				 hcTfysworkareaOverHandler1.setOverproofStatus(30);
			 } else {
				 hcTfysworkareaOverHandler1.setApprovalPerson(loginUser.getRealname());
				 hcTfysworkareaOverHandler1.setSupervisorHandleTime(new Date());
				 hcTfysworkareaOverHandler1.setOverproofStatus(20);
			 }
			 hcTfysworkareaOverHandlerService.updateById(hcTfysworkareaOverHandler1);
		 }
		 QueryWrapper<HcTfysworkarea> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("blockId", hcTfysworkareaOverHandler.getBaseid());
		 List<HcTfysworkarea> list = hcTfysworkareaService.list(queryWrapper);
		 if (list.size()>0) {
			 HcTfysworkarea tSyjzb = list.get(0);
			 HcPavementAlarm hcPavementAlarm = new HcPavementAlarm();
			 hcPavementAlarm.setId(tSyjzb.getId());
			 if (flag == 2) {
				 hcPavementAlarm.setOverproofStatus(10);
				 tSyjzb.setOverproofStatus(10);
			 } else if (flag == 3) {
				 hcPavementAlarm.setOverproofStatus(30);
				 tSyjzb.setOverproofStatus(30);
			 } else {
				 hcPavementAlarm.setOverproofStatus(20);
				 tSyjzb.setOverproofStatus(20);
			 }
			 hcTfysworkareaService.updateById(tSyjzb);
			 hcPavementAlarmService.updateById(hcPavementAlarm);
			 return Result.OK("处理成功!");
		 } else {
			 return Result.error("未找到对应数据");
		 }
	 }

	 /**
	  * 试验机处置审核驳回
	  *
	  * @param hcTfysworkareaOverHandler
	  * @return
	  */
	 @AutoLog(value = "试验机处置审核表-处置审核驳回")
	 @ApiOperation(value = "试验机处置审核表-处置审核驳回", notes = "试验机处置审核表-处置审核驳回")
	 @PostMapping(value = "/deallist1")
	 public Result<?> dealList1(@RequestBody HcTfysworkareaOverHandlerVo hcTfysworkareaOverHandler,HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 HcTfysworkareaOverHandler selectone = hcTfysworkareaOverHandlerService.selectone(hcTfysworkareaOverHandler.getBaseid());
		 HcTfysworkareaOverHandler hcTfysworkareaOverHandler1 = new HcTfysworkareaOverHandler();
		 BeanUtils.copyProperties(hcTfysworkareaOverHandler, hcTfysworkareaOverHandler1);

		 QueryWrapper<HcTfysworkarea> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("blockId", hcTfysworkareaOverHandler.getBaseid());
		 List<HcTfysworkarea> list = hcTfysworkareaService.list(queryWrapper);
		 HcPavementAlarm hcPavementAlarm = new HcPavementAlarm();
		 HcTfysworkarea tSyjzb = new HcTfysworkarea();
		 if (list.size()>0) {
			 tSyjzb = list.get(0);
			 hcPavementAlarm.setId(tSyjzb.getId());
		 }
		 if (hcTfysworkareaOverHandler.getCzblockid() != null){
			 tSyjzb.setCzblockid(hcTfysworkareaOverHandler.getCzblockid());
		 }
		 if (selectone == null) {//如果第一次处置
			 hcTfysworkareaOverHandler1.setHandlePerson(loginUser.getRealname());
			 hcTfysworkareaOverHandler1.setHandleTime(new Date());
			 if (hcTfysworkareaOverHandler.getStandard() == 1){
				 hcTfysworkareaOverHandler1.setOverproofStatus(20);
				 hcPavementAlarm.setOverproofStatus(20);
				 tSyjzb.setOverproofStatus(20);
			 }else {
				 hcTfysworkareaOverHandler1.setOverproofStatus(10);
				 hcPavementAlarm.setOverproofStatus(10);
				 tSyjzb.setOverproofStatus(10);
			 }
			 hcTfysworkareaOverHandlerService.save(hcTfysworkareaOverHandler1);
		 } else {
			 hcTfysworkareaOverHandler1.setId(selectone.getId());
			 if (hcTfysworkareaOverHandler.getStandard() == 2){
				 hcTfysworkareaOverHandler1.setApprovalPerson(loginUser.getRealname());
				 hcTfysworkareaOverHandler1.setSupervisorHandleTime(new Date());
				 if (hcTfysworkareaOverHandler.getStatus() == 30) {//监理驳回
					 hcTfysworkareaOverHandler1.setOverproofStatus(30);
					 hcPavementAlarm.setOverproofStatus(30);
					 tSyjzb.setOverproofStatus(30);
				 } else if (hcTfysworkareaOverHandler.getStatus() == 40) {//监理审核
					 hcTfysworkareaOverHandler1.setOverproofStatus(20);
					 hcPavementAlarm.setOverproofStatus(20);
					 tSyjzb.setOverproofStatus(20);
				 } else {
					 return Result.error("错误");
				 }
			 }else {
				 hcTfysworkareaOverHandler1.setHandlePerson(loginUser.getRealname());
				 hcTfysworkareaOverHandler1.setHeadquartersHandleTime(new Date());
				 if (hcTfysworkareaOverHandler.getStatus() == 30) {//监理驳回
					 hcTfysworkareaOverHandler1.setOverproofStatus(30);
					 hcPavementAlarm.setOverproofStatus(30);
					 tSyjzb.setOverproofStatus(30);
				 } else if (hcTfysworkareaOverHandler.getStatus() == 40) {//监理审核
					 hcTfysworkareaOverHandler1.setOverproofStatus(40);
					 hcPavementAlarm.setOverproofStatus(40);
					 tSyjzb.setOverproofStatus(40);
				 } else if (hcTfysworkareaOverHandler.getStatus() == 50) {//指挥部审核
					 hcTfysworkareaOverHandler1.setOverproofStatus(20);
					 hcPavementAlarm.setOverproofStatus(20);
					 tSyjzb.setOverproofStatus(20);
				 } else if (hcTfysworkareaOverHandler.getStatus() == 60) {//指挥部驳回
					 hcTfysworkareaOverHandler1.setOverproofStatus(60);
					 hcPavementAlarm.setOverproofStatus(60);
					 tSyjzb.setOverproofStatus(60);
				 } else {
					 return Result.error("错误");
				 }
			 }
			 hcTfysworkareaOverHandlerService.updateById(hcTfysworkareaOverHandler1);
		 }
		 hcTfysworkareaService.updateById(tSyjzb);
		 hcPavementAlarmService.updateById(hcPavementAlarm);
		 return Result.OK("处理成功!");
	 }

	/**
	 *  编辑
	 *
	 * @param hcTfysworkareaOverHandler
	 * @return
	 */
	@AutoLog(value = "hc_tfysworkarea_over_handler-编辑")
	@ApiOperation(value="hc_tfysworkarea_over_handler-编辑", notes="hc_tfysworkarea_over_handler-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcTfysworkareaOverHandler hcTfysworkareaOverHandler) {
		hcTfysworkareaOverHandlerService.updateById(hcTfysworkareaOverHandler);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "hc_tfysworkarea_over_handler-通过id删除")
	@ApiOperation(value="hc_tfysworkarea_over_handler-通过id删除", notes="hc_tfysworkarea_over_handler-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcTfysworkareaOverHandlerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "hc_tfysworkarea_over_handler-批量删除")
	@ApiOperation(value="hc_tfysworkarea_over_handler-批量删除", notes="hc_tfysworkarea_over_handler-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcTfysworkareaOverHandlerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "hc_tfysworkarea_over_handler-通过id查询")
	@ApiOperation(value="hc_tfysworkarea_over_handler-通过id查询", notes="hc_tfysworkarea_over_handler-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcTfysworkareaOverHandler hcTfysworkareaOverHandler = hcTfysworkareaOverHandlerService.getById(id);
		if(hcTfysworkareaOverHandler==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcTfysworkareaOverHandler);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcTfysworkareaOverHandler
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcTfysworkareaOverHandler hcTfysworkareaOverHandler) {
        return super.exportXls(request, hcTfysworkareaOverHandler, HcTfysworkareaOverHandler.class, "hc_tfysworkarea_over_handler");
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
        return super.importExcel(request, response, HcTfysworkareaOverHandler.class);
    }

}
