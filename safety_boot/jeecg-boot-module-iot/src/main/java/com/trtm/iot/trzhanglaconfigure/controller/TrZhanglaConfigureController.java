package com.trtm.iot.trzhanglaconfigure.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.trzhanglaconfigure.entity.TrZhanglaConfigure;
import com.trtm.iot.trzhanglaconfigure.service.ITrZhanglaConfigureService;

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
 * @Description: 张拉预警配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
@Api(tags="张拉预警配置表")
@RestController
@RequestMapping("/trzhanglaconfigure/trZhanglaConfigure")
@Slf4j
public class TrZhanglaConfigureController extends JeecgController<TrZhanglaConfigure, ITrZhanglaConfigureService> {
	@Autowired
	private ITrZhanglaConfigureService trZhanglaConfigureService;
	@Autowired
	private IBhzPhoneService bhzPhoneService;
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param trZhanglaConfigure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "张拉预警配置表-分页列表查询")
	@ApiOperation(value="张拉预警配置表-分页列表查询", notes="张拉预警配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrZhanglaConfigure trZhanglaConfigure,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (trZhanglaConfigure.getShebeiNo() == null) {
			if (!"null".equals(shebei)) {
				trZhanglaConfigure.setShebeiNo(shebei);
			} else {
				trZhanglaConfigure.setShebeiNo("空");
			}
		}
		QueryWrapper<TrZhanglaConfigure> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaConfigure, req.getParameterMap());
		Page<TrZhanglaConfigure> page = new Page<TrZhanglaConfigure>(pageNo, pageSize);
		IPage<TrZhanglaConfigure> pageList = trZhanglaConfigureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param trZhanglaConfigure
	 * @return
	 */
	@AutoLog(value = "张拉预警配置表-添加")
	@ApiOperation(value="张拉预警配置表-添加", notes="张拉预警配置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrZhanglaConfigure trZhanglaConfigure) {

		if(StringUtils.isNotBlank(trZhanglaConfigure.getNames())){
			BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(trZhanglaConfigure.getNames());
			trZhanglaConfigure.setNames(bhzPhone.getNames());
			trZhanglaConfigure.setPhones(bhzPhone.getPhones());
		}

		String shebeiNo = trZhanglaConfigure.getShebeiNo();
		QueryWrapper<TrZhanglaConfigure> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("shebei_no",shebeiNo);
		TrZhanglaConfigure one = trZhanglaConfigureService.getOne(queryWrapper);
		if (one == null){
			trZhanglaConfigureService.save(trZhanglaConfigure);
		}else {
			trZhanglaConfigure.setId(one.getId());
			trZhanglaConfigureService.updateById(trZhanglaConfigure);
		}
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param trZhanglaConfigure
	 * @return
	 */
	@AutoLog(value = "张拉预警配置表-编辑")
	@ApiOperation(value="张拉预警配置表-编辑", notes="张拉预警配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrZhanglaConfigure trZhanglaConfigure) {
		if(StringUtils.isNotBlank(trZhanglaConfigure.getNames())){
			BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(trZhanglaConfigure.getNames());
			trZhanglaConfigure.setNames(bhzPhone.getNames());
			trZhanglaConfigure.setPhones(bhzPhone.getPhones());
		}
		trZhanglaConfigureService.updateById(trZhanglaConfigure);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉预警配置表-通过id删除")
	@ApiOperation(value="张拉预警配置表-通过id删除", notes="张拉预警配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trZhanglaConfigureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "张拉预警配置表-批量删除")
	@ApiOperation(value="张拉预警配置表-批量删除", notes="张拉预警配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trZhanglaConfigureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉预警配置表-通过id查询")
	@ApiOperation(value="张拉预警配置表-通过id查询", notes="张拉预警配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrZhanglaConfigure trZhanglaConfigure = trZhanglaConfigureService.getById(id);
		if(trZhanglaConfigure==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trZhanglaConfigure);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trZhanglaConfigure
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrZhanglaConfigure trZhanglaConfigure) {
        return super.exportXls(request, trZhanglaConfigure, TrZhanglaConfigure.class, "张拉预警配置表");
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
        return super.importExcel(request, response, TrZhanglaConfigure.class);
    }

}
