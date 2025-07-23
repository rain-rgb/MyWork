package com.trtm.iot.largesbconfigure.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.shebeiinfo.entity.SheBeiInfo;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.largesbconfigure.entity.LargesbHistory;
import com.trtm.iot.largesbconfigure.service.ILargesbHistoryService;

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
 * @Description: largesb_history
 * @Author: jeecg-boot
 * @Date:   2023-09-14
 * @Version: V1.0
 */
@Api(tags="largesb_history")
@RestController
@RequestMapping("/largesbconfigure/largesbHistory")
@Slf4j
public class LargesbHistoryController extends JeecgController<LargesbHistory, ILargesbHistoryService> {
	@Autowired
	private ILargesbHistoryService largesbHistoryService;
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param largesbHistory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "largesb_history-分页列表查询")
	@ApiOperation(value="largesb_history-分页列表查询", notes="largesb_history-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(LargesbHistory largesbHistory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LargesbHistory> queryWrapper = QueryGenerator.initQueryWrapper(largesbHistory, req.getParameterMap());
		Page<LargesbHistory> page = new Page<LargesbHistory>(pageNo, pageSize);
		IPage<LargesbHistory> pageList = largesbHistoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * yujing
	  * @return
	  */
	 @AutoLog(value = "largesb_history-分页列表查询")
	 @ApiOperation(value="largesb_history-分页列表查询", notes="largesb_history-分页列表查询")
	 @GetMapping(value = "/listyj")
	 public Result<?> queryPageListyj(LargesbHistory largesbHistory,
									  String sys_depart_orgcode,
									String sbtypes,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 List<Map> list3 = new ArrayList<>();
		 int sbzs = 0;
		 if (sys_depart_orgcode != null){
			 QueryWrapper<ShebeiInfo> queryWrapper1 = new QueryWrapper<>();
			 queryWrapper1.eq("sbtype",sbtypes);
			 queryWrapper1.likeRight("sys_org_code",sys_depart_orgcode);
			 List<ShebeiInfo> list = shebeiInfoService.list(queryWrapper1);
			 List<String> list1 = new ArrayList<>();
			 if (list.size() > 0){
				 for (ShebeiInfo shebeiInfo :list){
					 list1.add(shebeiInfo.getSbjno());
				 }
				 QueryWrapper<LargesbHistory> queryWrapper = new QueryWrapper<>();
				 queryWrapper.select("count(yujing_info) yujing_info,shebei_no,sblx");
				 queryWrapper.in("shebei_no",list1);
				 queryWrapper.groupBy("shebei_no");
				 List<LargesbHistory> list2 = largesbHistoryService.list(queryWrapper);
				 if (list2.size() > 0){
					 for (LargesbHistory largesbHistory1 :list2){
						 HashMap<String, Object> map = new HashMap<>();
						 sbzs = sbzs + Integer.parseInt(largesbHistory1.getYujingInfo());
						 ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(largesbHistory1.getShebeiNo());
						 map.put("sbname",selectshebeione.getSbname());
						 map.put("shebei_no",largesbHistory1.getShebeiNo());
						 map.put("sblx",largesbHistory1.getSblx());
						 map.put("ljyjsl",largesbHistory1.getYujingInfo());
						 list3.add(map);
					 }
				 }
			 }
		 }
		 String s = String.valueOf(sbzs);
		 return Result.OK(s,list3);
	 }
	
	/**
	 *   添加
	 *
	 * @param largesbHistory
	 * @return
	 */
	@AutoLog(value = "largesb_history-添加")
	@ApiOperation(value="largesb_history-添加", notes="largesb_history-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody LargesbHistory largesbHistory) {
		largesbHistoryService.save(largesbHistory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param largesbHistory
	 * @return
	 */
	@AutoLog(value = "largesb_history-编辑")
	@ApiOperation(value="largesb_history-编辑", notes="largesb_history-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody LargesbHistory largesbHistory) {
		largesbHistoryService.updateById(largesbHistory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "largesb_history-通过id删除")
	@ApiOperation(value="largesb_history-通过id删除", notes="largesb_history-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		largesbHistoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "largesb_history-批量删除")
	@ApiOperation(value="largesb_history-批量删除", notes="largesb_history-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.largesbHistoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "largesb_history-通过id查询")
	@ApiOperation(value="largesb_history-通过id查询", notes="largesb_history-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		LargesbHistory largesbHistory = largesbHistoryService.getById(id);
		if(largesbHistory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(largesbHistory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param largesbHistory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LargesbHistory largesbHistory) {
        return super.exportXls(request, largesbHistory, LargesbHistory.class, "largesb_history");
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
        return super.importExcel(request, response, LargesbHistory.class);
    }

}
