package com.trtm.iot.dingcfg.controller;

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
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.dingcfg.entity.DingCfg;
import com.trtm.iot.dingcfg.service.IDingCfgService;

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
 * @Description: 钉钉考勤机配置表
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
@Api(tags="钉钉考勤机配置表")
@RestController
@RequestMapping("/dingcfg/dingCfg")
@Slf4j
public class DingCfgController extends JeecgController<DingCfg, IDingCfgService> {
	@Autowired
	private IDingCfgService dingCfgService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param dingCfg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "钉钉考勤机配置表-分页列表查询")
	@ApiOperation(value="钉钉考勤机配置表-分页列表查询", notes="钉钉考勤机配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DingCfg dingCfg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (dingCfg.getDevicesn() == null) {
			if (!"null".equals(shebei)) {
				dingCfg.setDevicesn(shebei);
			} else {
				dingCfg.setDevicesn("空");
			}
		}
		QueryWrapper<DingCfg> queryWrapper = QueryGenerator.initQueryWrapper(dingCfg, req.getParameterMap());
		Page<DingCfg> page = new Page<DingCfg>(pageNo, pageSize);
		IPage<DingCfg> pageList = dingCfgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param dingCfg
	 * @return
	 */
	@AutoLog(value = "钉钉考勤机配置表-添加")
	@ApiOperation(value="钉钉考勤机配置表-添加", notes="钉钉考勤机配置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DingCfg dingCfg) {
		dingCfgService.save(dingCfg);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param dingCfg
	 * @return
	 */
	@AutoLog(value = "钉钉考勤机配置表-编辑")
	@ApiOperation(value="钉钉考勤机配置表-编辑", notes="钉钉考勤机配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DingCfg dingCfg) {
		dingCfgService.updateById(dingCfg);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "钉钉考勤机配置表-通过id删除")
	@ApiOperation(value="钉钉考勤机配置表-通过id删除", notes="钉钉考勤机配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dingCfgService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "钉钉考勤机配置表-批量删除")
	@ApiOperation(value="钉钉考勤机配置表-批量删除", notes="钉钉考勤机配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dingCfgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "钉钉考勤机配置表-通过id查询")
	@ApiOperation(value="钉钉考勤机配置表-通过id查询", notes="钉钉考勤机配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DingCfg dingCfg = dingCfgService.getById(id);
		if(dingCfg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(dingCfg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dingCfg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DingCfg dingCfg) {
        return super.exportXls(request, dingCfg, DingCfg.class, "钉钉考勤机配置表");
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
        return super.importExcel(request, response, DingCfg.class);
    }

}
