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

import com.trtm.iot.tadiao.entity.TadiaoHis;
import com.trtm.iot.tadiao.service.ITadiaoHisService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.tadiao.entity.Tadiao;
import com.trtm.iot.tadiao.service.ITadiaoService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: 塔吊
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
@Api(tags="塔吊")
@RestController
@RequestMapping("/tadiao/tadiao")
@Slf4j
public class TadiaoController extends JeecgController<Tadiao, ITadiaoService> {
	@Autowired
	private ITadiaoService tadiaoService;
	@Autowired
	private ITadiaoHisService tadiaoHisService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param tadiao
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "塔吊-分页列表查询")
	@ApiOperation(value="塔吊-分页列表查询", notes="塔吊-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Tadiao tadiao,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (tadiao.getDevicesn() == null) {
			if (!"null".equals(shebei)) {
				tadiao.setDevicesn(shebei);
			} else {
				tadiao.setDevicesn("空");
			}
		}
		QueryWrapper<Tadiao> queryWrapper = QueryGenerator.initQueryWrapper(tadiao, req.getParameterMap());
		Page<Tadiao> page = new Page<Tadiao>(pageNo, pageSize);
		IPage<Tadiao> pageList = tadiaoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param tadiao
	 * @return
	 */
	@AutoLog(value = "塔吊-添加")
	@ApiOperation(value="塔吊-添加", notes="塔吊-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Tadiao tadiao) {
		String devicesn = tadiao.getDevicesn();
		if (devicesn.isEmpty()){
			return Result.error("设备编码为空！");
		}
		QueryWrapper<Tadiao> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("devicesn",devicesn);

		Tadiao one = tadiaoService.getOne(queryWrapper);
		TadiaoHis tadiaoHis = new TadiaoHis();
		if (null!=one){
			tadiaoService.update(tadiao,queryWrapper);
		}else {
			tadiaoService.save(tadiao);
		}
		BeanUtils.copyProperties(tadiao,tadiaoHis);
		tadiaoHisService.save(tadiaoHis);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param tadiao
	 * @return
	 */
	@AutoLog(value = "塔吊-编辑")
	@ApiOperation(value="塔吊-编辑", notes="塔吊-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Tadiao tadiao) {
		tadiaoService.updateById(tadiao);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "塔吊-通过id删除")
	@ApiOperation(value="塔吊-通过id删除", notes="塔吊-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tadiaoService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "塔吊-批量删除")
	@ApiOperation(value="塔吊-批量删除", notes="塔吊-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tadiaoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "塔吊-通过id查询")
	@ApiOperation(value="塔吊-通过id查询", notes="塔吊-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Tadiao tadiao = tadiaoService.getById(id);
		if(tadiao==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(tadiao);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param tadiao
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Tadiao tadiao) {
        return super.exportXls(request, tadiao, Tadiao.class, "塔吊");
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
        return super.importExcel(request, response, Tadiao.class);
    }

}
