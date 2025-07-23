package com.trtm.iot.clkhistory.controller;

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
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.clkhistory.entity.FrontDischargeholeData;
import com.trtm.iot.clkhistory.service.IFrontDischargeholeDataService;

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
 * @Description: 出料口测温历史数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-06
 * @Version: V1.0
 */
@Api(tags="出料口测温历史数据表")
@RestController
@RequestMapping("/clkhistory/frontDischargeholeData")
@Slf4j
public class FrontDischargeholeDataController extends JeecgController<FrontDischargeholeData, IFrontDischargeholeDataService> {
	@Autowired
	private IFrontDischargeholeDataService frontDischargeholeDataService;
	 @Autowired
	 private RedisUtil redisUtil;

	 /**
	 * 分页列表查询
	 *
	 * @param frontDischargeholeData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "出料口测温历史数据表-分页列表查询")
	@ApiOperation(value="出料口测温历史数据表-分页列表查询", notes="出料口测温历史数据表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FrontDischargeholeData frontDischargeholeData,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if(frontDischargeholeData.getSensorid()==null){
			if (!"null".equals(shebei)) {
				frontDischargeholeData.setSensorid(shebei);
			}else{
				frontDischargeholeData.setSensorid("空");
			}
		}
		QueryWrapper<FrontDischargeholeData> queryWrapper = QueryGenerator.initQueryWrapper(frontDischargeholeData, req.getParameterMap());
		Page<FrontDischargeholeData> page = new Page<FrontDischargeholeData>(pageNo, pageSize);
		IPage<FrontDischargeholeData> pageList = frontDischargeholeDataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param frontDischargeholeData
	 * @return
	 */
	@AutoLog(value = "出料口测温历史数据表-添加")
	@ApiOperation(value="出料口测温历史数据表-添加", notes="出料口测温历史数据表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FrontDischargeholeData frontDischargeholeData) {
		frontDischargeholeDataService.save(frontDischargeholeData);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param frontDischargeholeData
	 * @return
	 */
	@AutoLog(value = "出料口测温历史数据表-编辑")
	@ApiOperation(value="出料口测温历史数据表-编辑", notes="出料口测温历史数据表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FrontDischargeholeData frontDischargeholeData) {
		frontDischargeholeDataService.updateById(frontDischargeholeData);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "出料口测温历史数据表-通过id删除")
	@ApiOperation(value="出料口测温历史数据表-通过id删除", notes="出料口测温历史数据表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		frontDischargeholeDataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "出料口测温历史数据表-批量删除")
	@ApiOperation(value="出料口测温历史数据表-批量删除", notes="出料口测温历史数据表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.frontDischargeholeDataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "出料口测温历史数据表-通过id查询")
	@ApiOperation(value="出料口测温历史数据表-通过id查询", notes="出料口测温历史数据表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FrontDischargeholeData frontDischargeholeData = frontDischargeholeDataService.getById(id);
		if(frontDischargeholeData==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(frontDischargeholeData);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param frontDischargeholeData
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FrontDischargeholeData frontDischargeholeData) {
        return super.exportXls(request, frontDischargeholeData, FrontDischargeholeData.class, "出料口测温历史数据表");
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
        return super.importExcel(request, response, FrontDischargeholeData.class);
    }

}
