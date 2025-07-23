package com.trtm.iot.shebeiinfo.controller;

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

import com.trtm.api.utils.StringUtils;
import com.trtm.iot.shebeiinfo.entity.ShebeiBase;
import com.trtm.iot.shebeiinfo.service.IShebeiBaseService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.shebeiinfo.entity.ShebeiOverhaul;
import com.trtm.iot.shebeiinfo.service.IShebeiOverhaulService;

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
 * @Description: shebei_overhaul
 * @Author: jeecg-boot
 * @Date:   2024-11-13
 * @Version: V1.0
 */
@Api(tags="shebei_overhaul")
@RestController
@RequestMapping("/shebeiinfo/shebeiOverhaul")
@Slf4j
public class ShebeiOverhaulController extends JeecgController<ShebeiOverhaul, IShebeiOverhaulService> {
	@Autowired
	private IShebeiOverhaulService shebeiOverhaulService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IShebeiBaseService shebeiBaseService;

	/**
	 * 分页列表查询
	 *
	 * @param shebeiOverhaul
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "shebei_overhaul-设备巡检分页列表查询")
	@ApiOperation(value="shebei_overhaul-设备巡检分页列表查询", notes="shebei_overhaul-设备巡检分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ShebeiOverhaul shebeiOverhaul, String sys_depart_orgcode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (shebeiOverhaul.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				shebeiOverhaul.setShebeino(shebei);
			} else {
				shebeiOverhaul.setShebeino("空");
			}
		}
		QueryWrapper<ShebeiOverhaul> queryWrapper = QueryGenerator.initQueryWrapper(shebeiOverhaul, req.getParameterMap());
		Page<ShebeiOverhaul> page = new Page<ShebeiOverhaul>(pageNo, pageSize);
		IPage<ShebeiOverhaul> pageList = shebeiOverhaulService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 @AutoLog(value = "shebei_overhaul-设备巡检分页列表查询")
	 @ApiOperation(value="shebei_overhaul-设备巡检分页列表查询", notes="shebei_overhaul-设备巡检分页列表查询")
	 @GetMapping(value = "/listsx")
	 public Result<?> queryPageListsx(ShebeiOverhaul shebeiOverhaul, String sys_depart_orgcode,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if(StringUtils.isNotBlank(sys_depart_orgcode)){
			 shebeiOverhaul.setSysOrgCode(sys_depart_orgcode+'*');
		 }else{
			 shebeiOverhaul.setSysOrgCode(loginUser.getOrgCode()+'*');
		 }
		 QueryWrapper<ShebeiOverhaul> queryWrapper = QueryGenerator.initQueryWrapper(shebeiOverhaul, req.getParameterMap());
		 Page<ShebeiOverhaul> page = new Page<ShebeiOverhaul>(pageNo, pageSize);
		 IPage<ShebeiOverhaul> pageList = shebeiOverhaulService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	/**
	 *   添加
	 *
	 * @param shebeiOverhaul
	 * @return
	 */
	@AutoLog(value = "shebei_overhaul-设备巡检添加")
	@ApiOperation(value="shebei_overhaul-设备巡检添加", notes="shebei_overhaul-设备巡检添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ShebeiOverhaul shebeiOverhaul) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		shebeiOverhaul = svup(shebeiOverhaul);
		ShebeiBase one = shebeiBaseService.getByShebeino(shebeiOverhaul.getShebeino());
		one.setLasttime(new Date());
		// 已维修设备状态改为正常
		if("true".equals(shebeiOverhaul.getNote())){
			one.setShebeistatus(0);
		}else{
			one.setShebeistatus(shebeiOverhaul.getZhengchang());
		}

		one.setJiancharen(loginUser.getRealname());
		shebeiBaseService.updateById(one);
		shebeiOverhaulService.save(shebeiOverhaul);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param shebeiOverhaul
	 * @return
	 */
	@AutoLog(value = "shebei_overhaul-设备巡检编辑")
	@ApiOperation(value="shebei_overhaul-设备巡检编辑", notes="shebei_overhaul-设备巡检编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ShebeiOverhaul shebeiOverhaul) {
		shebeiOverhaul = svup(shebeiOverhaul);
		ShebeiBase one = shebeiBaseService.getByShebeino(shebeiOverhaul.getShebeino());
		one.setLasttime(new Date());
		// 已维修设备状态改为正常
		if("true".equals(shebeiOverhaul.getNote())){
			one.setShebeistatus(0);
		}else{
			one.setShebeistatus(shebeiOverhaul.getZhengchang());
		}
		shebeiBaseService.updateById(one);
		shebeiOverhaulService.updateById(shebeiOverhaul);
		return Result.OK("编辑成功!");
	}

	 ShebeiOverhaul svup(ShebeiOverhaul shebeiOverhaul){
		if(StringUtils.isNotBlank(shebeiOverhaul.getNote())){
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
			shebeiOverhaul.setChuliren(loginUser.getUsername());
			shebeiOverhaul.setChulitime(new Date());
//			if("true".equals(shebeiOverhaul.getNote()) ){
//				shebeiOverhaul.setZhengchang(20);
//			}else{
//				shebeiOverhaul.setZhengchang(30);
//			}
		}

		return  shebeiOverhaul;
	 }
	 /**
	  *  编辑
	  *
	  * @param shebeiOverhaul
	  * @return
	  */
	 @AutoLog(value = "shebei_overhaul-设备故障维修登记")
	 @ApiOperation(value="shebei_overhaul-设备故障维修登记", notes="shebei_overhaul-设备故障维修登记")
	 @PutMapping(value = "/edithandle")
	 public Result<?> edithandle(@RequestBody ShebeiOverhaul shebeiOverhaul) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 shebeiOverhaul.setChuliren(loginUser.getUsername());
		 shebeiOverhaul.setChulitime(new Date());
		 shebeiOverhaulService.updateById(shebeiOverhaul);
		 return Result.OK("编辑成功!");
	 }
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "shebei_overhaul-通过id删除")
	@ApiOperation(value="shebei_overhaul-通过id删除", notes="shebei_overhaul-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		shebeiOverhaulService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "shebei_overhaul-批量删除")
	@ApiOperation(value="shebei_overhaul-批量删除", notes="shebei_overhaul-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.shebeiOverhaulService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "shebei_overhaul-通过id查询")
	@ApiOperation(value="shebei_overhaul-通过id查询", notes="shebei_overhaul-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ShebeiOverhaul shebeiOverhaul = shebeiOverhaulService.getById(id);
		if(shebeiOverhaul==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(shebeiOverhaul);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param shebeiOverhaul
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShebeiOverhaul shebeiOverhaul) {
        return super.exportXls(request, shebeiOverhaul, ShebeiOverhaul.class, "shebei_overhaul");
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
        return super.importExcel(request, response, ShebeiOverhaul.class);
    }

}
