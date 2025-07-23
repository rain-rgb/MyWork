package com.trtm.iot.tadiao.controller;

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
import com.trtm.iot.tadiao.entity.TadiaoHis;
import com.trtm.iot.tadiao.service.ITadiaoHisService;

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
 * @Description: 塔吊历史数据
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
@Api(tags="塔吊历史数据")
@RestController
@RequestMapping("/tadiao/tadiaoHis")
@Slf4j
public class TadiaoHisController extends JeecgController<TadiaoHis, ITadiaoHisService> {
	@Autowired
	private ITadiaoHisService tadiaoHisService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param tadiaoHis
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "塔吊历史数据-分页列表查询")
	@ApiOperation(value="塔吊历史数据-分页列表查询", notes="塔吊历史数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TadiaoHis tadiaoHis,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (tadiaoHis.getDevicesn() == null) {
			if ("null".equals(shebei) ) {
				shebei = "空";
			}
			tadiaoHis.setDevicesn(shebei);
		}
		QueryWrapper<TadiaoHis> queryWrapper = QueryGenerator.initQueryWrapper(tadiaoHis, req.getParameterMap());
		Page<TadiaoHis> page = new Page<TadiaoHis>(pageNo, pageSize);
		IPage<TadiaoHis> pageList = tadiaoHisService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param tadiaoHis
	 * @return
	 */
	@AutoLog(value = "塔吊历史数据-添加")
	@ApiOperation(value="塔吊历史数据-添加", notes="塔吊历史数据-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TadiaoHis tadiaoHis) {
		tadiaoHisService.save(tadiaoHis);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param tadiaoHis
	 * @return
	 */
	@AutoLog(value = "塔吊历史数据-编辑")
	@ApiOperation(value="塔吊历史数据-编辑", notes="塔吊历史数据-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TadiaoHis tadiaoHis) {
		tadiaoHisService.updateById(tadiaoHis);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "塔吊历史数据-通过id删除")
	@ApiOperation(value="塔吊历史数据-通过id删除", notes="塔吊历史数据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tadiaoHisService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "塔吊历史数据-批量删除")
	@ApiOperation(value="塔吊历史数据-批量删除", notes="塔吊历史数据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tadiaoHisService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "塔吊历史数据-通过id查询")
	@ApiOperation(value="塔吊历史数据-通过id查询", notes="塔吊历史数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TadiaoHis tadiaoHis = tadiaoHisService.getById(id);
		if(tadiaoHis==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(tadiaoHis);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param tadiaoHis
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TadiaoHis tadiaoHis) {
        return super.exportXls(request, tadiaoHis, TadiaoHis.class, "塔吊历史数据");
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
        return super.importExcel(request, response, TadiaoHis.class);
    }

}
