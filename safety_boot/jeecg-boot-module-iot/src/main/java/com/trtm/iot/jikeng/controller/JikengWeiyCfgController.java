package com.trtm.iot.jikeng.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.jikeng.entity.JikengWeiyCfg;
import com.trtm.iot.jikeng.service.IJikengWeiyCfgService;

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
 * @Description: jikeng_weiy_cfg
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@Api(tags="jikeng_weiy_cfg")
@RestController
@RequestMapping("/jikeng/jikengWeiyCfg")
@Slf4j
public class JikengWeiyCfgController extends JeecgController<JikengWeiyCfg, IJikengWeiyCfgService> {
	@Autowired
	private IJikengWeiyCfgService jikengWeiyCfgService;

	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param jikengWeiyCfg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_cfg-分页列表查询")
	@ApiOperation(value="jikeng_weiy_cfg-分页列表查询", notes="jikeng_weiy_cfg-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(JikengWeiyCfg jikengWeiyCfg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (StringUtils.isBlank(jikengWeiyCfg.getShebeino()) ) {
			if (!"null" .equals(shebei)) {
				jikengWeiyCfg.setShebeino(shebei);
			} else {
				jikengWeiyCfg.setShebeino("空");
			}
		}
		QueryWrapper<JikengWeiyCfg> queryWrapper = QueryGenerator.initQueryWrapper(jikengWeiyCfg, req.getParameterMap());
		Page<JikengWeiyCfg> page = new Page<JikengWeiyCfg>(pageNo, pageSize);
		IPage<JikengWeiyCfg> pageList = jikengWeiyCfgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param jikengWeiyCfg
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_cfg-添加")
	@ApiOperation(value="jikeng_weiy_cfg-添加", notes="jikeng_weiy_cfg-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JikengWeiyCfg jikengWeiyCfg) {
		jikengWeiyCfgService.save(jikengWeiyCfg);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param jikengWeiyCfg
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_cfg-编辑")
	@ApiOperation(value="jikeng_weiy_cfg-编辑", notes="jikeng_weiy_cfg-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JikengWeiyCfg jikengWeiyCfg) {
		jikengWeiyCfgService.updateById(jikengWeiyCfg);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_cfg-通过id删除")
	@ApiOperation(value="jikeng_weiy_cfg-通过id删除", notes="jikeng_weiy_cfg-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		jikengWeiyCfgService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_cfg-批量删除")
	@ApiOperation(value="jikeng_weiy_cfg-批量删除", notes="jikeng_weiy_cfg-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.jikengWeiyCfgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jikeng_weiy_cfg-通过id查询")
	@ApiOperation(value="jikeng_weiy_cfg-通过id查询", notes="jikeng_weiy_cfg-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		JikengWeiyCfg jikengWeiyCfg = jikengWeiyCfgService.getById(id);
		if(jikengWeiyCfg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(jikengWeiyCfg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param jikengWeiyCfg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, JikengWeiyCfg jikengWeiyCfg) {
        return super.exportXls(request, jikengWeiyCfg, JikengWeiyCfg.class, "jikeng_weiy_cfg");
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
        return super.importExcel(request, response, JikengWeiyCfg.class);
    }

}
