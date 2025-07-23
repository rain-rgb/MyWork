package com.trtm.iot.safetyhelmet.controller;

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

import cn.hutool.core.util.StrUtil;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.safetyhelmet.entity.SafetyHelmetAlert;
import com.trtm.iot.safetyhelmet.service.ISafetyHelmetAlertService;

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
 * @Description: 安全帽数据
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
@Api(tags="安全帽数据")
@RestController
@RequestMapping("/safetyhelmet/safetyHelmetAlert")
@Slf4j
public class SafetyHelmetAlertController extends JeecgController<SafetyHelmetAlert, ISafetyHelmetAlertService> {
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;
	@Autowired
	private ISafetyHelmetAlertService safetyHelmetAlertService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param safetyHelmetAlert
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "安全帽数据-分页列表查询")
	@ApiOperation(value="安全帽数据-分页列表查询", notes="安全帽数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SafetyHelmetAlert safetyHelmetAlert,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (safetyHelmetAlert.getImgtype() == null) {
			if (!"null".equals(shebei)) {
				safetyHelmetAlert.setImgtype(shebei);
			}else {
				safetyHelmetAlert.setImgtype("空");
			}
		}
		QueryWrapper<SafetyHelmetAlert> queryWrapper = QueryGenerator.initQueryWrapper(safetyHelmetAlert, req.getParameterMap());
		Page<SafetyHelmetAlert> page = new Page<SafetyHelmetAlert>(pageNo, pageSize);
		IPage<SafetyHelmetAlert> pageList = safetyHelmetAlertService.page(page, queryWrapper);
		List<SafetyHelmetAlert> records = pageList.getRecords();
		for (SafetyHelmetAlert safetyHelmetAlert1 : records){
			String filename = safetyHelmetAlert1.getFilename();
			int index = filename.indexOf("h", 5);
			String result = filename.substring(index + 5);
			safetyHelmetAlert1.setFilename(result);
		}
		return Result.OK(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param safetyHelmetAlert
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "安全帽数据-列表查询")
	 @ApiOperation(value="安全帽数据-列表查询", notes="安全帽数据-列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(SafetyHelmetAlert safetyHelmetAlert,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (safetyHelmetAlert.getImgtype() == null) {
			 if (!"null".equals(shebei)) {
				 safetyHelmetAlert.setImgtype(shebei);
			 }else {
				 safetyHelmetAlert.setImgtype("空");
			 }
		 }
		 QueryWrapper<SafetyHelmetAlert> queryWrapper = QueryGenerator.initQueryWrapper(safetyHelmetAlert, req.getParameterMap());
		 List<SafetyHelmetAlert> pageList = safetyHelmetAlertService.list(queryWrapper);
		 for (SafetyHelmetAlert safetyHelmetAlert1 : pageList){
			 String filename = safetyHelmetAlert1.getFilename();
			 int index = filename.indexOf("h", 5);
			 String result = filename.substring(index + 5);
			 safetyHelmetAlert1.setFilename(result);
		 }
		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param safetyHelmetAlert
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "安全帽数据-分页列表查询")
	 @ApiOperation(value="安全帽数据-分页列表查询", notes="安全帽数据-分页列表查询")
	 @GetMapping(value = "/liststa")
	 public Result<?> queryPageListsta(SafetyHelmetAlert safetyHelmetAlert,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										HttpServletRequest req,String orgCode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//		 if (safetyHelmetAlert.getImgtype() == null) {
//			 if (!"null".equals(shebei)) {
//				 safetyHelmetAlert.setImgtype(shebei);
//			 }else {
//				 safetyHelmetAlert.setImgtype("空");
//			 }
//		 }
		 if (StrUtil.isBlank(orgCode)) {
			 orgCode = loginUser.getOrgCode();
		 }
		 List<ShebeiInfo> lists = shebeiInfoService.shebeilist(40, orgCode);
		 List<String> shebeis = new ArrayList<>();
		 if (lists.size() > 0) {
			 for (ShebeiInfo shebeiInfo : lists) {
					 shebeis.add(shebeiInfo.getSbjno());
			 }
		 }
		 QueryWrapper<SafetyHelmetAlert> queryWrapper = QueryGenerator.initQueryWrapper(safetyHelmetAlert, req.getParameterMap());
		 if (shebeis.size()>0) {
			 queryWrapper.in("imgtype", shebeis);
		 }else {
			 queryWrapper.eq("imgtype", "空");
		 }
		 Page<SafetyHelmetAlert> page = new Page<SafetyHelmetAlert>(pageNo, pageSize);
		 IPage<SafetyHelmetAlert> pageList = safetyHelmetAlertService.page(page, queryWrapper);
		 List<SafetyHelmetAlert> records = pageList.getRecords();
		 for (SafetyHelmetAlert safetyHelmetAlert1 : records){
			 String filename = safetyHelmetAlert1.getFilename();
			 int index = filename.indexOf("h", 5);
			 String result = filename.substring(index + 5);
			 safetyHelmetAlert1.setFilename(result);
		 }
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param safetyHelmetAlert
	 * @return
	 */
	@AutoLog(value = "安全帽数据-添加")
	@ApiOperation(value="安全帽数据-添加", notes="安全帽数据-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SafetyHelmetAlert safetyHelmetAlert) {
		safetyHelmetAlertService.save(safetyHelmetAlert);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param safetyHelmetAlert
	 * @return
	 */
	@AutoLog(value = "安全帽数据-编辑")
	@ApiOperation(value="安全帽数据-编辑", notes="安全帽数据-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SafetyHelmetAlert safetyHelmetAlert) {
		safetyHelmetAlertService.updateById(safetyHelmetAlert);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "安全帽数据-通过id删除")
	@ApiOperation(value="安全帽数据-通过id删除", notes="安全帽数据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		safetyHelmetAlertService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "安全帽数据-批量删除")
	@ApiOperation(value="安全帽数据-批量删除", notes="安全帽数据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.safetyHelmetAlertService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "安全帽数据-通过id查询")
	@ApiOperation(value="安全帽数据-通过id查询", notes="安全帽数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SafetyHelmetAlert safetyHelmetAlert = safetyHelmetAlertService.getById(id);
		if(safetyHelmetAlert==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(safetyHelmetAlert);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param safetyHelmetAlert
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SafetyHelmetAlert safetyHelmetAlert) {
        return super.exportXls(request, safetyHelmetAlert, SafetyHelmetAlert.class, "安全帽数据");
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
        return super.importExcel(request, response, SafetyHelmetAlert.class);
    }

}
