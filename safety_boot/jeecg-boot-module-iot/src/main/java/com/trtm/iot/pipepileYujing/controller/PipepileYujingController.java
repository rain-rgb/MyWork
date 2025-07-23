package com.trtm.iot.pipepileYujing.controller;

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

import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.pipepileYujing.entity.PipepileYujing;
import com.trtm.iot.pipepileYujing.service.IPipepileYujingService;

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
 * @Description: 管桩预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-09-15
 * @Version: V1.0
 */
@Api(tags="管桩预警配置表")
@RestController
@RequestMapping("/pipepileYujing/pipepileYujing")
@Slf4j
public class PipepileYujingController extends JeecgController<PipepileYujing, IPipepileYujingService> {
	@Autowired
	private IPipepileYujingService pipepileYujingService;

	@Autowired
	private IDevicePipepileHistorydataOneService pipepileHistorydataOneService;

	 @Autowired
	 RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param pipepileYujing
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "管桩预警配置表-分页列表查询")
	@ApiOperation(value="管桩预警配置表-分页列表查询", notes="管桩预警配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PipepileYujing pipepileYujing,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (pipepileYujing.getShebeiNo() == null) {
			if (!"null".equals(shebei)) {
				pipepileYujing.setShebeiNo(shebei);
			}else {
				pipepileYujing.setShebeiNo("空");
			}
		}
		QueryWrapper<PipepileYujing> queryWrapper = QueryGenerator.initQueryWrapper(pipepileYujing, req.getParameterMap());
		Page<PipepileYujing> page = new Page<PipepileYujing>(pageNo, pageSize);
		IPage<PipepileYujing> pageList = pipepileYujingService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param pipepileYujing
	 * @return
	 */
	@AutoLog(value = "管桩预警配置表-添加")
	@ApiOperation(value="管桩预警配置表-添加", notes="管桩预警配置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PipepileYujing pipepileYujing) {
		pipepileYujingService.save(pipepileYujing);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pipepileYujing
	 * @return
	 */
	@AutoLog(value = "管桩预警配置表-编辑")
	@ApiOperation(value="管桩预警配置表-编辑", notes="管桩预警配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PipepileYujing pipepileYujing) {
		pipepileYujingService.updateById(pipepileYujing);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "管桩预警配置表-通过id删除")
	@ApiOperation(value="管桩预警配置表-通过id删除", notes="管桩预警配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pipepileYujingService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "管桩预警配置表-批量删除")
	@ApiOperation(value="管桩预警配置表-批量删除", notes="管桩预警配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pipepileYujingService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "管桩预警配置表-通过id查询")
	@ApiOperation(value="管桩预警配置表-通过id查询", notes="管桩预警配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PipepileYujing pipepileYujing = pipepileYujingService.getById(id);
		if(pipepileYujing==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(pipepileYujing);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pipepileYujing
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PipepileYujing pipepileYujing) {
        return super.exportXls(request, pipepileYujing, PipepileYujing.class, "管桩预警配置表");
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
        return super.importExcel(request, response, PipepileYujing.class);
    }

}
