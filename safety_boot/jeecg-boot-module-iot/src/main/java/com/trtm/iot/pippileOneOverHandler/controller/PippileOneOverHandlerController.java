package com.trtm.iot.pippileOneOverHandler.controller;

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

import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.pippileOneOverHandler.entity.PippileOneOverHandler;
import com.trtm.iot.pippileOneOverHandler.service.IPippileOneOverHandlerService;

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
 * @Description: pippile_one_over_handler
 * @Author: jeecg-boot
 * @Date:   2022-11-23
 * @Version: V1.0
 */
@Api(tags="pippile_one_over_handler")
@RestController
@RequestMapping("/pippileOneOverHandler/pippileOneOverHandler")
@Slf4j
public class PippileOneOverHandlerController extends JeecgController<PippileOneOverHandler, IPippileOneOverHandlerService> {
	@Autowired
	private IPippileOneOverHandlerService pippileOneOverHandlerService;
	@Autowired
	 IDevicePipepileHistorydataOneService pipepileHistorydataOneService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pippileOneOverHandler
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "pippile_one_over_handler-分页列表查询")
	@ApiOperation(value="pippile_one_over_handler-分页列表查询", notes="pippile_one_over_handler-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PippileOneOverHandler pippileOneOverHandler,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PippileOneOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(pippileOneOverHandler, req.getParameterMap());
		Page<PippileOneOverHandler> page = new Page<PippileOneOverHandler>(pageNo, pageSize);
		IPage<PippileOneOverHandler> pageList = pippileOneOverHandlerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pippileOneOverHandler
	 * @return
	 */
	@AutoLog(value = "pippile_one_over_handler-添加")
	@ApiOperation(value="pippile_one_over_handler-添加", notes="pippile_one_over_handler-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PippileOneOverHandler pippileOneOverHandler) {
		pippileOneOverHandlerService.save(pippileOneOverHandler);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加
	  *
	  * @return
	  */
	 @AutoLog(value = "软基超标处理-处置")
	 @ApiOperation(value="软基超标处理-处置", notes="软基超标处理-添加")
	 @PostMapping(value = "/handle")
	 public Result<?> handle(@RequestBody PippileOneOverHandler pippileOneOverHandler) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pippileOneOverHandler.setHandleTime(new Date());
		 pippileOneOverHandler.setSupervisorHandleTime(new Date());
		 if(pippileOneOverHandler.getOverproofStatus() == 20){
			 pippileOneOverHandler.setApprovalPerson(String.valueOf(loginUser.getRealname()));
		 }else {
			 pippileOneOverHandler.setHandlePerson(String.valueOf(loginUser.getRealname()));
		 }
		 QueryWrapper<PippileOneOverHandler> queryWrapper = new QueryWrapper();
		 queryWrapper.eq("baseId",pippileOneOverHandler.getBaseid());
		 List<PippileOneOverHandler> one = pippileOneOverHandlerService.list(queryWrapper);
		 if(one.size() == 0){
			 pippileOneOverHandlerService.save(pippileOneOverHandler);
		 }else{
			 pippileOneOverHandler.setId(one.get(0).getId());
			 pippileOneOverHandlerService.updateById(pippileOneOverHandler);
		 }


		 // 主表状态更新
		 DevicePipepileHistorydataOne devicePipepileHistorydataOne = new DevicePipepileHistorydataOne();
		 devicePipepileHistorydataOne.setId(Integer.valueOf(pippileOneOverHandler.getBaseid()));
		 devicePipepileHistorydataOne.setOverproofStatus(pippileOneOverHandler.getOverproofStatus());
		 pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
		 return Result.OK("处置成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param pippileOneOverHandler
	 * @return
	 */
	@AutoLog(value = "pippile_one_over_handler-编辑")
	@ApiOperation(value="pippile_one_over_handler-编辑", notes="pippile_one_over_handler-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PippileOneOverHandler pippileOneOverHandler) {
		pippileOneOverHandler.setSupervisorHandleTime(new Date());
		pippileOneOverHandlerService.updateById(pippileOneOverHandler);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pippile_one_over_handler-通过id删除")
	@ApiOperation(value="pippile_one_over_handler-通过id删除", notes="pippile_one_over_handler-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pippileOneOverHandlerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "pippile_one_over_handler-批量删除")
	@ApiOperation(value="pippile_one_over_handler-批量删除", notes="pippile_one_over_handler-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pippileOneOverHandlerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pippile_one_over_handler-通过id查询")
	@ApiOperation(value="pippile_one_over_handler-通过id查询", notes="pippile_one_over_handler-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PippileOneOverHandler pippileOneOverHandler = pippileOneOverHandlerService.getById(id);
		if(pippileOneOverHandler==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(pippileOneOverHandler);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pippileOneOverHandler
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PippileOneOverHandler pippileOneOverHandler) {
        return super.exportXls(request, pippileOneOverHandler, PippileOneOverHandler.class, "pippile_one_over_handler");
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
        return super.importExcel(request, response, PippileOneOverHandler.class);
    }

}
