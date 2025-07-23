package com.trtm.iot.ljyadata.controller;

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
import com.trtm.iot.ljyadata.entity.Ljyadata;
import com.trtm.iot.ljyadata.service.ILjyadataService;

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
 * @Description: ljyadata
 * @Author: jeecg-boot
 * @Date:   2022-12-12
 * @Version: V1.0
 */
@Api(tags="ljyadata")
@RestController
@RequestMapping("/ljyadata/ljyadata")
@Slf4j
public class LjyadataController extends JeecgController<Ljyadata, ILjyadataService> {
	@Autowired
	private ILjyadataService ljyadataService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param ljyadata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "ljyadata-分页列表查询")
	@ApiOperation(value="ljyadata-分页列表查询", notes="ljyadata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Ljyadata ljyadata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (StringUtils.isBlank(ljyadata.getRollerid())) {
			if (!"null".equals(shebei)) {
				ljyadata.setRollerid(shebei);
			} else {
				ljyadata.setRollerid("空");
			}
		}
		QueryWrapper<Ljyadata> queryWrapper = QueryGenerator.initQueryWrapper(ljyadata, req.getParameterMap());
		Page<Ljyadata> page = new Page<Ljyadata>(pageNo, pageSize);
		IPage<Ljyadata> pageList = ljyadataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param ljyadata
	 * @return
	 */
	@AutoLog(value = "ljyadata-添加")
	@ApiOperation(value="ljyadata-添加", notes="ljyadata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Ljyadata ljyadata) {
		ljyadataService.save(ljyadata);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param ljyadata
	 * @return
	 */
	@AutoLog(value = "ljyadata-编辑")
	@ApiOperation(value="ljyadata-编辑", notes="ljyadata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Ljyadata ljyadata) {
		ljyadataService.updateById(ljyadata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ljyadata-通过id删除")
	@ApiOperation(value="ljyadata-通过id删除", notes="ljyadata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ljyadataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ljyadata-批量删除")
	@ApiOperation(value="ljyadata-批量删除", notes="ljyadata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ljyadataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ljyadata-通过id查询")
	@ApiOperation(value="ljyadata-通过id查询", notes="ljyadata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Ljyadata ljyadata = ljyadataService.getById(id);
		if(ljyadata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ljyadata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ljyadata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Ljyadata ljyadata) {
        return super.exportXls(request, ljyadata, Ljyadata.class, "ljyadata");
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
        return super.importExcel(request, response, Ljyadata.class);
    }

}
