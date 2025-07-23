package com.trtm.iot.wbsquality.controller;

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
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wbsquality.entity.WbsYinbigongcheng;
import com.trtm.iot.wbsquality.service.IWbsYinbigongchengService;

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
 * @Description: wbs_yinbigongcheng
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@Api(tags="wbs_yinbigongcheng")
@RestController
@RequestMapping("/wbsquality/wbsYinbigongcheng")
@Slf4j
public class WbsYinbigongchengController extends JeecgController<WbsYinbigongcheng, IWbsYinbigongchengService> {
	@Autowired
	private IWbsYinbigongchengService wbsYinbigongchengService;

	/**
	 * 分页列表查询
	 *
	 * @param wbsYinbigongcheng
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wbs_yinbigongcheng-分页列表查询")
	@ApiOperation(value="wbs_yinbigongcheng-分页列表查询", notes="wbs_yinbigongcheng-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WbsYinbigongcheng wbsYinbigongcheng,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {

		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息

		if (StringUtils.isNotBlank(sys_depart_orgcode)) {  //如果想要全局组织机构控制所显示的数据要加上这个
			wbsYinbigongcheng.setSysOrgCode(sys_depart_orgcode + "*");
		}else{
			wbsYinbigongcheng.setSysOrgCode(loginUser.getOrgCode() + "*");
		}

		QueryWrapper<WbsYinbigongcheng> queryWrapper = QueryGenerator.initQueryWrapper(wbsYinbigongcheng, req.getParameterMap());
		Page<WbsYinbigongcheng> page = new Page<WbsYinbigongcheng>(pageNo, pageSize);
		IPage<WbsYinbigongcheng> pageList = wbsYinbigongchengService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wbsYinbigongcheng
	 * @return
	 */
	@AutoLog(value = "wbs_yinbigongcheng-添加")
	@ApiOperation(value="wbs_yinbigongcheng-添加", notes="wbs_yinbigongcheng-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WbsYinbigongcheng wbsYinbigongcheng) {
		wbsYinbigongchengService.save(wbsYinbigongcheng);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wbsYinbigongcheng
	 * @return
	 */
	@AutoLog(value = "wbs_yinbigongcheng-编辑")
	@ApiOperation(value="wbs_yinbigongcheng-编辑", notes="wbs_yinbigongcheng-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WbsYinbigongcheng wbsYinbigongcheng) {
		wbsYinbigongchengService.updateById(wbsYinbigongcheng);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wbs_yinbigongcheng-通过id删除")
	@ApiOperation(value="wbs_yinbigongcheng-通过id删除", notes="wbs_yinbigongcheng-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wbsYinbigongchengService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wbs_yinbigongcheng-批量删除")
	@ApiOperation(value="wbs_yinbigongcheng-批量删除", notes="wbs_yinbigongcheng-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wbsYinbigongchengService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wbs_yinbigongcheng-通过id查询")
	@ApiOperation(value="wbs_yinbigongcheng-通过id查询", notes="wbs_yinbigongcheng-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WbsYinbigongcheng wbsYinbigongcheng = wbsYinbigongchengService.getById(id);
		if(wbsYinbigongcheng==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wbsYinbigongcheng);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wbsYinbigongcheng
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WbsYinbigongcheng wbsYinbigongcheng) {
        return super.exportXls(request, wbsYinbigongcheng, WbsYinbigongcheng.class, "wbs_yinbigongcheng");
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
        return super.importExcel(request, response, WbsYinbigongcheng.class);
    }

}
