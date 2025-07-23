package com.trtm.iot.wztaizhang.controller;

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
import com.trtm.iot.wztaizhang.entity.WzyclConfig;
import com.trtm.iot.wztaizhang.service.IWzyclConfigService;

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
 * @Description: wzycl_config
 * @Author: jeecg-boot
 * @Date:   2024-08-12
 * @Version: V1.0
 */
@Api(tags="wzycl_config")
@RestController
@RequestMapping("/wztaizhang/wzyclConfig")
@Slf4j
public class WzyclConfigController extends JeecgController<WzyclConfig, IWzyclConfigService> {
	@Autowired
	private IWzyclConfigService wzyclConfigService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param wzyclConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wzycl_config-分页列表查询")
	@ApiOperation(value="wzycl_config-分页列表查询", notes="wzycl_config-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WzyclConfig wzyclConfig,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (wzyclConfig.getSbjno() == null) {
			if (!"null".equals(shebei)) {
				wzyclConfig.setSbjno(shebei);
			} else {
				wzyclConfig.setSbjno("空");
			}
		}
		QueryWrapper<WzyclConfig> queryWrapper = QueryGenerator.initQueryWrapper(wzyclConfig, req.getParameterMap());
		Page<WzyclConfig> page = new Page<WzyclConfig>(pageNo, pageSize);
		IPage<WzyclConfig> pageList = wzyclConfigService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wzyclConfig
	 * @return
	 */
	@AutoLog(value = "wzycl_config-添加")
	@ApiOperation(value="wzycl_config-添加", notes="wzycl_config-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WzyclConfig wzyclConfig) {
		wzyclConfigService.save(wzyclConfig);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wzyclConfig
	 * @return
	 */
	@AutoLog(value = "wzycl_config-编辑")
	@ApiOperation(value="wzycl_config-编辑", notes="wzycl_config-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WzyclConfig wzyclConfig) {
		wzyclConfigService.updateById(wzyclConfig);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzycl_config-通过id删除")
	@ApiOperation(value="wzycl_config-通过id删除", notes="wzycl_config-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzyclConfigService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wzycl_config-批量删除")
	@ApiOperation(value="wzycl_config-批量删除", notes="wzycl_config-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzyclConfigService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzycl_config-通过id查询")
	@ApiOperation(value="wzycl_config-通过id查询", notes="wzycl_config-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WzyclConfig wzyclConfig = wzyclConfigService.getById(id);
		if(wzyclConfig==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzyclConfig);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzyclConfig
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzyclConfig wzyclConfig) {
        return super.exportXls(request, wzyclConfig, WzyclConfig.class, "wzycl_config");
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
        return super.importExcel(request, response, WzyclConfig.class);
    }

}
