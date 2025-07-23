package com.trtm.iot.shebeiinfo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.api.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.shebeiinfo.entity.ShebeiBase;
import com.trtm.iot.shebeiinfo.service.IShebeiBaseService;

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
 * @Description: shebei_base
 * @Author: jeecg-boot
 * @Date:   2024-11-13
 * @Version: V1.0
 */
@Api(tags="shebei_base")
@RestController
@RequestMapping("/shebeiinfo/shebeiBase")
@Slf4j
public class ShebeiBaseController extends JeecgController<ShebeiBase, IShebeiBaseService> {
	@Autowired
	private IShebeiBaseService shebeiBaseService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param shebeiBase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "shebei_base-分页列表查询")
	@ApiOperation(value="shebei_base-分页列表查询", notes="shebei_base-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ShebeiBase shebeiBase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		QueryWrapper<ShebeiBase> queryWrapper = QueryGenerator.initQueryWrapper(shebeiBase, req.getParameterMap());
		Page<ShebeiBase> page = new Page<ShebeiBase>(pageNo, pageSize);
		IPage<ShebeiBase> pageList = shebeiBaseService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param shebeiBase
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "shebei_base-分页列表查询")
	 @ApiOperation(value="shebei_base-分页列表查询", notes="shebei_base-分页列表查询")
	 @GetMapping(value = "/listsx")
	 public Result<?> queryPageListsx(ShebeiBase shebeiBase,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  String sys_depart_orgcode,HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if(StringUtils.isNotBlank(sys_depart_orgcode)){
			 shebeiBase.setSysOrgCode(sys_depart_orgcode+'*');
		 }else{
			 shebeiBase.setSysOrgCode(loginUser.getOrgCode()+'*');
		 }
		 QueryWrapper<ShebeiBase> queryWrapper = QueryGenerator.initQueryWrapper(shebeiBase, req.getParameterMap());
		 Page<ShebeiBase> page = new Page<ShebeiBase>(pageNo, pageSize);
		 IPage<ShebeiBase> pageList = shebeiBaseService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param shebeiBase
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "shebei_base-分页列表查询")
	 @ApiOperation(value="shebei_base-分页列表查询", notes="shebei_base-分页列表查询")
	 @GetMapping(value = "/listtj")
	 public Result<?> queryPageListtj(ShebeiBase shebeiBase,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  String sys_depart_orgcode,HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if(StringUtils.isNotBlank(sys_depart_orgcode)){
			 shebeiBase.setSysOrgCode(sys_depart_orgcode+'*');
		 }else{
			 shebeiBase.setSysOrgCode(loginUser.getOrgCode()+'*');
		 }
		 // 统计三个数 总数，进场数，退场数，故障数
		 QueryWrapper<ShebeiBase> queryWrapper = QueryGenerator.initQueryWrapper(shebeiBase, req.getParameterMap());
		 queryWrapper.select(" COUNT(*) AS allsum,\n" +
				 "    SUM(CASE WHEN jingchangshijian < CURDATE() AND (tuichangshijian IS NULL OR tuichangshijian > CURDATE()) THEN 1 ELSE 0 END) AS jinchangs,\n" +
				 "    SUM(CASE WHEN tuichangshijian < CURDATE() THEN 1 ELSE 0 END) AS tuichangs,\n" +
				 "    SUM(CASE WHEN shebeistatus = 1 THEN 1 ELSE 0 END) AS errocount");

		 Map<String, Object> map = shebeiBaseService.getMap(queryWrapper);
		 return Result.OK(map);
	 }

	/**
	 *   添加
	 *
	 * @param shebeiBase
	 * @return
	 */
	@AutoLog(value = "shebei_base-添加")
	@ApiOperation(value="shebei_base-添加", notes="shebei_base-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ShebeiBase shebeiBase) {
		shebeiBaseService.save(shebeiBase);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param shebeiBase
	 * @return
	 */
	@AutoLog(value = "shebei_base-编辑")
	@ApiOperation(value="shebei_base-编辑", notes="shebei_base-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ShebeiBase shebeiBase) {
		shebeiBaseService.updateById(shebeiBase);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "shebei_base-通过id删除")
	@ApiOperation(value="shebei_base-通过id删除", notes="shebei_base-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		shebeiBaseService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "shebei_base-批量删除")
	@ApiOperation(value="shebei_base-批量删除", notes="shebei_base-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.shebeiBaseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "shebei_base-通过id查询")
	@ApiOperation(value="shebei_base-通过id查询", notes="shebei_base-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ShebeiBase shebeiBase = shebeiBaseService.getById(id);
		if(shebeiBase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(shebeiBase);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param shebeiBase
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShebeiBase shebeiBase) {
        return super.exportXls(request, shebeiBase, ShebeiBase.class, "shebei_base");
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
        return super.importExcel(request, response, ShebeiBase.class);
    }

}
