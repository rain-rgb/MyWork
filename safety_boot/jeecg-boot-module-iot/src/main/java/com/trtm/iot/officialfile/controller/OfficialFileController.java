package com.trtm.iot.officialfile.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.officialfile.entity.OfficialFile;
import com.trtm.iot.officialfile.service.IOfficialFileService;

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
 * @Description: 公文信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
@Api(tags="公文信息表")
@RestController
@RequestMapping("/officialfile/officialFile")
@Slf4j
public class OfficialFileController extends JeecgController<OfficialFile, IOfficialFileService> {
	@Autowired
	private IOfficialFileService officialFileService;

	/**
	 * 上级来文分页列表查询
	 *
	 * @param officialFile
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "公文信息表-上级来文分页列表查询")
	@ApiOperation(value="公文信息表-上级来文分页列表查询", notes="公文信息表-上级来文分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OfficialFile officialFile,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		if(sys_depart_orgcode!=null){
			officialFile.setSysOrgCode(sys_depart_orgcode+"*");//右模糊查询
		}
		officialFile.setFilestyle(1);//上级来文
		QueryWrapper<OfficialFile> queryWrapper = QueryGenerator.initQueryWrapper(officialFile, req.getParameterMap());
		Page<OfficialFile> page = new Page<OfficialFile>(pageNo, pageSize);
		IPage<OfficialFile> pageList = officialFileService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 项目办文件分页列表查询
	  *
	  * @param officialFile
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "公文信息表-项目办文件分页列表查询")
	 @ApiOperation(value="公文信息表-项目办文件分页列表查询", notes="公文信息表-项目办文件分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(OfficialFile officialFile,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									HttpServletRequest req) {
		 if(sys_depart_orgcode!=null){
			 officialFile.setSysOrgCode(sys_depart_orgcode+"*");//右模糊查询
		 }
		 officialFile.setFilestyle(2);//项目办文件
		 QueryWrapper<OfficialFile> queryWrapper = QueryGenerator.initQueryWrapper(officialFile, req.getParameterMap());
		 Page<OfficialFile> page = new Page<OfficialFile>(pageNo, pageSize);
		 IPage<OfficialFile> pageList = officialFileService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 施工单位文件分页列表查询
	  *
	  * @param officialFile
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "公文信息表-施工单位文件分页列表查询")
	 @ApiOperation(value="公文信息表-施工单位文件分页列表查询", notes="公文信息表-施工单位文件分页列表查询")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(OfficialFile officialFile,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									HttpServletRequest req) {
		 if(sys_depart_orgcode!=null){
			 officialFile.setSysOrgCode(sys_depart_orgcode+"*");//右模糊查询
		 }
		 officialFile.setFilestyle(3);//施工单位文件
		 QueryWrapper<OfficialFile> queryWrapper = QueryGenerator.initQueryWrapper(officialFile, req.getParameterMap());
		 Page<OfficialFile> page = new Page<OfficialFile>(pageNo, pageSize);
		 IPage<OfficialFile> pageList = officialFileService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 地方文件分页列表查询
	  *
	  * @param officialFile
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "公文信息表-地方文件分页列表查询")
	 @ApiOperation(value="公文信息表-地方文件分页列表查询", notes="公文信息表-地方文件分页列表查询")
	 @GetMapping(value = "/list3")
	 public Result<?> queryPageList3(OfficialFile officialFile,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									HttpServletRequest req) {
		 if(sys_depart_orgcode!=null){
			 officialFile.setSysOrgCode(sys_depart_orgcode+"*");//右模糊查询
		 }
		 officialFile.setFilestyle(4);//地方文件
		 QueryWrapper<OfficialFile> queryWrapper = QueryGenerator.initQueryWrapper(officialFile, req.getParameterMap());
		 Page<OfficialFile> page = new Page<OfficialFile>(pageNo, pageSize);
		 IPage<OfficialFile> pageList = officialFileService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 监理文件分页列表查询
	  *
	  * @param officialFile
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "公文信息表-监理文件分页列表查询")
	 @ApiOperation(value="公文信息表-监理文件分页列表查询", notes="公文信息表-监理文件分页列表查询")
	 @GetMapping(value = "/list4")
	 public Result<?> queryPageList4(OfficialFile officialFile,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									HttpServletRequest req) {
		 if(sys_depart_orgcode!=null){
			 officialFile.setSysOrgCode(sys_depart_orgcode+"*");//右模糊查询
		 }
		 officialFile.setFilestyle(5);//监理文件
		 QueryWrapper<OfficialFile> queryWrapper = QueryGenerator.initQueryWrapper(officialFile, req.getParameterMap());
		 Page<OfficialFile> page = new Page<OfficialFile>(pageNo, pageSize);
		 IPage<OfficialFile> pageList = officialFileService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 其他档案分页列表查询
	  *
	  * @param officialFile
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "公文信息表-其他档案分页列表查询")
	 @ApiOperation(value="公文信息表-其他档案分页列表查询", notes="公文信息表-其他档案分页列表查询")
	 @GetMapping(value = "/list5")
	 public Result<?> queryPageList5(OfficialFile officialFile,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									 HttpServletRequest req) {
		 if(sys_depart_orgcode!=null){
			 officialFile.setSysOrgCode(sys_depart_orgcode+"*");//右模糊查询
		 }
		 officialFile.setFilestyle(6);//其他档案
		 QueryWrapper<OfficialFile> queryWrapper = QueryGenerator.initQueryWrapper(officialFile, req.getParameterMap());
		 Page<OfficialFile> page = new Page<OfficialFile>(pageNo, pageSize);
		 IPage<OfficialFile> pageList = officialFileService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 大事记分页列表查询
	  *
	  * @param officialFile
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "公文信息表-大事记分页列表查询")
	 @ApiOperation(value="公文信息表-大事记分页列表查询", notes="公文信息表-大事记分页列表查询")
	 @GetMapping(value = "/list6")
	 public Result<?> queryPageList6(OfficialFile officialFile,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									 HttpServletRequest req) {
		 if(sys_depart_orgcode!=null){
			 officialFile.setSysOrgCode(sys_depart_orgcode+"*");//右模糊查询
		 }
		 officialFile.setFilestyle(7);//大事记
		 QueryWrapper<OfficialFile> queryWrapper = QueryGenerator.initQueryWrapper(officialFile, req.getParameterMap());
		 Page<OfficialFile> page = new Page<OfficialFile>(pageNo, pageSize);
		 IPage<OfficialFile> pageList = officialFileService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 大事记
	  *
	  * @param officialFile
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "公文信息表-大事记查询")
	 @ApiOperation(value="公文信息表-大事记查询", notes="公文信息表-大事记查询")
	 @GetMapping(value = "/list7")
	 public Result<?> queryPageList7(OfficialFile officialFile,
									 String sys_depart_orgcode,
									 HttpServletRequest req) {
		 if(sys_depart_orgcode!=null){
			 officialFile.setSysOrgCode(sys_depart_orgcode+"*");//右模糊查询
		 }
		 officialFile.setFilestyle(7);//大事记
		 QueryWrapper<OfficialFile> queryWrapper = QueryGenerator.initQueryWrapper(officialFile, req.getParameterMap());

		 List<OfficialFile> list = officialFileService.list(queryWrapper);
		 return Result.OK(list);
	 }

	/**
	 *   添加
	 *
	 * @param officialFile
	 * @return
	 */
	@AutoLog(value = "公文信息表-添加")
	@ApiOperation(value="公文信息表-添加", notes="公文信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OfficialFile officialFile) {
		officialFileService.save(officialFile);
		return Result.OK("添加成功！");
	}

	 /**
	  *   上级来文添加
	  *
	  * @param officialFile
	  * @return
	  */
	 @AutoLog(value = "公文信息表-上级来文添加")
	 @ApiOperation(value="公文信息表-上级来文添加", notes="公文信息表-上级来文添加")
	 @PostMapping(value = "/add1")
	 public Result<?> add1(@RequestBody OfficialFile officialFile) {
		 officialFile.setFilestyle(1);//公文类型：1-上级公文
		 officialFileService.save(officialFile);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   项目办文件添加
	  *
	  * @param officialFile
	  * @return
	  */
	 @AutoLog(value = "公文信息表-项目办文件添加")
	 @ApiOperation(value="公文信息表-项目办文件添加", notes="公文信息表-项目办文件添加")
	 @PostMapping(value = "/add2")
	 public Result<?> add2(@RequestBody OfficialFile officialFile) {
		 officialFile.setFilestyle(2);//公文类型：2-项目办文件
		 officialFileService.save(officialFile);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   施工单位文件添加
	  *
	  * @param officialFile
	  * @return
	  */
	 @AutoLog(value = "公文信息表-施工单位文件添加")
	 @ApiOperation(value="公文信息表-施工单位文件添加", notes="公文信息表-施工单位文件添加")
	 @PostMapping(value = "/add3")
	 public Result<?> add3(@RequestBody OfficialFile officialFile) {
		 officialFile.setFilestyle(3);//公文类型：3-施工单位文件
		 officialFileService.save(officialFile);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   地方文件添加
	  *
	  * @param officialFile
	  * @return
	  */
	 @AutoLog(value = "公文信息表-地方文件添加")
	 @ApiOperation(value="公文信息表-地方文件添加", notes="公文信息表-地方文件添加")
	 @PostMapping(value = "/add4")
	 public Result<?> add4(@RequestBody OfficialFile officialFile) {
		 officialFile.setFilestyle(4);//公文类型：4-地方文件
		 officialFileService.save(officialFile);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   监理文件添加
	  *
	  * @param officialFile
	  * @return
	  */
	 @AutoLog(value = "公文信息表-监理文件添加")
	 @ApiOperation(value="公文信息表-监理文件添加", notes="公文信息表-监理文件添加")
	 @PostMapping(value = "/add5")
	 public Result<?> add5(@RequestBody OfficialFile officialFile) {
		 officialFile.setFilestyle(5);//公文类型：5-监理文件
		 officialFileService.save(officialFile);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   其他档案添加
	  *
	  * @param officialFile
	  * @return
	  */
	 @AutoLog(value = "公文信息表-其他档案添加")
	 @ApiOperation(value="公文信息表-其他档案添加", notes="公文信息表-其他档案添加")
	 @PostMapping(value = "/add6")
	 public Result<?> add6(@RequestBody OfficialFile officialFile) {
		 officialFile.setFilestyle(6);//公文类型：5-其他档案
		 officialFileService.save(officialFile);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   大事记添加
	  *
	  * @param officialFile
	  * @return
	  */
	 @AutoLog(value = "公文信息表-大事记添加")
	 @ApiOperation(value="公文信息表-大事记添加", notes="公文信息表-大事记添加")
	 @PostMapping(value = "/add7")
	 public Result<?> add7(@RequestBody OfficialFile officialFile) {
		 officialFile.setFilestyle(7);//公文类型：5-大事记
		 if(officialFile.getUrl().contains(".mp4") || officialFile.getUrl().contains(".avi") || officialFile.getUrl().contains(".wmv") || officialFile.getUrl().contains(".ram") ){
			 officialFile.setFiletype(1);// 视频格式
		 }else{
			 officialFile.setFiletype(0);
		 }
		 officialFileService.save(officialFile);
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param officialFile
	 * @return
	 */
	@AutoLog(value = "公文信息表-编辑")
	@ApiOperation(value="公文信息表-编辑", notes="公文信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OfficialFile officialFile) {
		officialFileService.updateById(officialFile);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "公文信息表-通过id删除")
	@ApiOperation(value="公文信息表-通过id删除", notes="公文信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		officialFileService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "公文信息表-批量删除")
	@ApiOperation(value="公文信息表-批量删除", notes="公文信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.officialFileService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "公文信息表-通过id查询")
	@ApiOperation(value="公文信息表-通过id查询", notes="公文信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OfficialFile officialFile = officialFileService.getById(id);
		if(officialFile==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(officialFile);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param officialFile
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OfficialFile officialFile) {
        return super.exportXls(request, officialFile, OfficialFile.class, "公文信息表");
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
        return super.importExcel(request, response, OfficialFile.class);
    }

	 /**
	  * 大事记
	  * @param request
	  * @param response
	  * @return
	  */
//	 @RequestMapping(value = "/events", method = RequestMethod.GET)
//	 public Result<?> events(HttpServletRequest request, HttpServletResponse response) {
//		 return Result.OK(officialFileService.events());
//	 }

	 /**
	  * 大事记分页列表查询
	  *
	  * @param officialFile
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "公文信息表-大事记分页列表查询")
	 @ApiOperation(value="公文信息表-大事记分页列表查询", notes="公文信息表-大事记分页列表查询")
	 @GetMapping(value = "/events")
	 public Result<?> events(OfficialFile officialFile,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="5") Integer pageSize,String sys_depart_orgcode,
									 HttpServletRequest req) {
		 if(sys_depart_orgcode!=null){
			 officialFile.setSysOrgCode(sys_depart_orgcode+"*");//右模糊查询
		 }
		 officialFile.setFilestyle(7);//大事记
		 QueryWrapper<OfficialFile> queryWrapper = QueryGenerator.initQueryWrapper(officialFile, req.getParameterMap());
		 queryWrapper.orderByDesc("filetime");
		 Page<OfficialFile> page = new Page<OfficialFile>(pageNo, pageSize);
		 IPage<OfficialFile> pageList = officialFileService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }


	 /**
	  * 工程档案
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping(value = "/archive", method = RequestMethod.GET)
	 public Result<?> archive(HttpServletRequest request, HttpServletResponse response) {
		 return Result.OK(officialFileService.archive());
	 }

}
