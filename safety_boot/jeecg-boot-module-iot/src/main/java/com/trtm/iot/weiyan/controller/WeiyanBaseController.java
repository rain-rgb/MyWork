package com.trtm.iot.weiyan.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.weiyan.entity.WeiyanBase;
import com.trtm.iot.weiyan.service.IWeiyanBaseService;

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
 * @Description: 围岩量测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Api(tags="围岩量测主表")
@RestController
@RequestMapping("/weiyan/weiyanBase")
@Slf4j
public class WeiyanBaseController extends JeecgController<WeiyanBase, IWeiyanBaseService> {
	@Autowired
	private IWeiyanBaseService weiyanBaseService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param weiyanBase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "围岩量测主表-分页列表查询")
	@ApiOperation(value="围岩量测主表-分页列表查询", notes="围岩量测主表-分页列表查询")
	@GetMapping(value = "/list")
//	@PermissionData(pageComponent="weiyan/WeiyanBaseList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
	public Result<?> queryPageList(WeiyanBase weiyanBase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (weiyanBase.getShebeiNo() == null) {
			if (!"null".equals(shebei)) {
				weiyanBase.setShebeiNo(shebei);
			}else {
				weiyanBase.setShebeiNo("空");
			}
		}
		//查询满足条件的id
		List<Integer> resultList = new ArrayList<>();
		QueryWrapper<WeiyanBase> queryWrapper2 = QueryGenerator.initQueryWrapper(weiyanBase, req.getParameterMap());
		queryWrapper2.select("MAX( id ) AS id");
		queryWrapper2.groupBy("section_name");
		List<WeiyanBase> idlist = weiyanBaseService.list(queryWrapper2);
		idlist .forEach(item->{
			resultList.add(item.getId());
		});
		weiyanBase.setSectionName("*" + weiyanBase.getSectionName() + "*");
		QueryWrapper<WeiyanBase> queryWrapper = QueryGenerator.initQueryWrapper(weiyanBase, req.getParameterMap());
		if(resultList.size()>0){
			queryWrapper.in("id",resultList);
		}
		Page<WeiyanBase> page = new Page<WeiyanBase>(pageNo, pageSize);
		IPage<WeiyanBase> pageList= weiyanBaseService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @ApiOperation(value="围岩量测主表-测量数据/变形曲线", notes="围岩量测主表-测量数据/变形曲线")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(WeiyanBase weiyanBase,HttpServletRequest req ) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if(weiyanBase.getShebeiNo()==null){
			 if (shebei != null) {
				 weiyanBase.setShebeiNo(shebei);
			 }
		 }
		 QueryWrapper<WeiyanBase> queryWrapper = QueryGenerator.initQueryWrapper(weiyanBase, req.getParameterMap());
		 List<WeiyanBase> pageList = weiyanBaseService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	 @ApiOperation(value="围岩量测主表-测点查询", notes="围岩量测主表-测点查询")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList3(WeiyanBase weiyanBase,HttpServletRequest req ) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if(weiyanBase.getShebeiNo()==null){
			 if (shebei != null) {
				 weiyanBase.setShebeiNo(shebei);
			 }
		 }
		 QueryWrapper<WeiyanBase> queryWrapper = QueryGenerator.initQueryWrapper(weiyanBase, req.getParameterMap());
		 queryWrapper.select("distinct(measuring_point) as measuring_point ");
		 List<WeiyanBase> pageList = weiyanBaseService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param weiyanBase
	 * @return
	 */
	@AutoLog(value = "围岩量测主表-添加")
	@ApiOperation(value="围岩量测主表-添加", notes="围岩量测主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WeiyanBase weiyanBase) {
		weiyanBaseService.save(weiyanBase);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param weiyanBase
	 * @return
	 */
	@AutoLog(value = "围岩量测主表-编辑")
	@ApiOperation(value="围岩量测主表-编辑", notes="围岩量测主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WeiyanBase weiyanBase) {
		weiyanBaseService.updateById(weiyanBase);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "围岩量测主表-通过id删除")
	@ApiOperation(value="围岩量测主表-通过id删除", notes="围岩量测主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		weiyanBaseService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "围岩量测主表-批量删除")
	@ApiOperation(value="围岩量测主表-批量删除", notes="围岩量测主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.weiyanBaseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "围岩量测主表-通过id查询")
	@ApiOperation(value="围岩量测主表-通过id查询", notes="围岩量测主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WeiyanBase weiyanBase = weiyanBaseService.getById(id);
		if(weiyanBase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(weiyanBase);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param weiyanBase
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WeiyanBase weiyanBase) {
        return super.exportXls(request, weiyanBase, WeiyanBase.class, "围岩量测主表");
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
        return super.importExcel(request, response, WeiyanBase.class);
    }

	 /**
	  * 隧道围岩终端上传接口
	  *
	  */
	 @RequestMapping(value = "/wallRockBase", produces = "application/json; charset=utf-8",method = RequestMethod.POST)
	 @ResponseBody
	 public String wallRockBase(HttpServletRequest request) throws Exception {
		 byte buffer[] =WeiyanBaseController.getRequestPostBytes(request);
		 String json = new String(buffer, request.getCharacterEncoding());
		 String result = weiyanBaseService.weiyanSave(json);
		 return result;
	 }
	 /**
	  * 获取 post 请求的 byte[] 数组
	  * @throws IOException
	  */
	 public static byte[] getRequestPostBytes(HttpServletRequest request)
			 throws IOException {
		 int contentLength = request.getContentLength();
		 if(contentLength<0){
			 return null;
		 }
		 byte buffer[] = new byte[contentLength];
		 for (int i = 0; i < contentLength;) {

			 int readlen = request.getInputStream().read(buffer, i,
					 contentLength - i);
			 if (readlen == -1) {
				 break;
			 }
			 i += readlen;
		 }
		 return buffer;
	 }
}
