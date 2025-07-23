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

import com.trtm.api.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.jikeng.entity.JikengAnquanZhuti;
import com.trtm.iot.jikeng.service.IJikengAnquanZhutiService;

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
 * @Description: jikeng_anquan_zhuti
 * @Author: jeecg-boot
 * @Date:   2025-03-20
 * @Version: V1.0
 */
@Api(tags="jikeng_anquan_zhuti")
@RestController
@RequestMapping("/jikeng/jikengAnquanZhuti")
@Slf4j
public class JikengAnquanZhutiController extends JeecgController<JikengAnquanZhuti, IJikengAnquanZhutiService> {
	@Autowired
	private IJikengAnquanZhutiService jikengAnquanZhutiService;

	/**
	 * 分页列表查询
	 *
	 * @param jikengAnquanZhuti
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "jikeng_anquan_zhuti-分页列表查询")
	@ApiOperation(value="jikeng_anquan_zhuti-分页列表查询", notes="jikeng_anquan_zhuti-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(JikengAnquanZhuti jikengAnquanZhuti,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if(StringUtils.isNotBlank(sys_depart_orgcode)){
			 jikengAnquanZhuti.setSysOrgCode(sys_depart_orgcode+"*");
		 }else{
			 jikengAnquanZhuti.setSysOrgCode(loginUser.getOrgCode()+"*");
		 }
		QueryWrapper<JikengAnquanZhuti> queryWrapper = QueryGenerator.initQueryWrapper(jikengAnquanZhuti, req.getParameterMap());
		Page<JikengAnquanZhuti> page = new Page<JikengAnquanZhuti>(pageNo, pageSize);
		IPage<JikengAnquanZhuti> pageList = jikengAnquanZhutiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "jikeng_anquan_zhuti-分页列表查询")
	 @ApiOperation(value="jikeng_anquan_zhuti-分页列表查询", notes="jikeng_anquan_zhuti-分页列表查询")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(JikengAnquanZhuti jikengAnquanZhuti,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="100") Integer pageSize,
									String sys_depart_orgcode,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//		 if(StringUtils.isNotBlank(sys_depart_orgcode)){
//			 jikengAnquanZhuti.setSysOrgCode(sys_depart_orgcode+"*");
//		 }else{
//			 jikengAnquanZhuti.setSysOrgCode(loginUser.getOrgCode()+"*");
//		 }
		 QueryWrapper<JikengAnquanZhuti> queryWrapper = new QueryWrapper<>();
		 queryWrapper.select(" t1.* ");
		 queryWrapper.last(" t1 " +
				 "INNER JOIN ( " +
				 "    SELECT cedian, MAX(datatime) AS max_datetime " +
				 "    FROM jikeng_anquan_zhuti " +
				 "    GROUP BY cedian " +
				 ") t2 ON t1.cedian = t2.cedian AND t1.datatime = t2.max_datetime " +
				 "ORDER BY t1.datatime  ");
		 Page<JikengAnquanZhuti> page = new Page<JikengAnquanZhuti>(pageNo, pageSize);
		 List<JikengAnquanZhuti> pageList = jikengAnquanZhutiService.list(queryWrapper);

		 return Result.OK(pageList);
	 }
	/**
	 *   添加
	 *
	 * @param jikengAnquanZhuti
	 * @return
	 */
	@AutoLog(value = "jikeng_anquan_zhuti-添加")
	@ApiOperation(value="jikeng_anquan_zhuti-添加", notes="jikeng_anquan_zhuti-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JikengAnquanZhuti jikengAnquanZhuti) {
		jikengAnquanZhutiService.save(jikengAnquanZhuti);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param jikengAnquanZhuti
	 * @return
	 */
	@AutoLog(value = "jikeng_anquan_zhuti-编辑")
	@ApiOperation(value="jikeng_anquan_zhuti-编辑", notes="jikeng_anquan_zhuti-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JikengAnquanZhuti jikengAnquanZhuti) {
		jikengAnquanZhutiService.updateById(jikengAnquanZhuti);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jikeng_anquan_zhuti-通过id删除")
	@ApiOperation(value="jikeng_anquan_zhuti-通过id删除", notes="jikeng_anquan_zhuti-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		jikengAnquanZhutiService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "jikeng_anquan_zhuti-批量删除")
	@ApiOperation(value="jikeng_anquan_zhuti-批量删除", notes="jikeng_anquan_zhuti-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.jikengAnquanZhutiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jikeng_anquan_zhuti-通过id查询")
	@ApiOperation(value="jikeng_anquan_zhuti-通过id查询", notes="jikeng_anquan_zhuti-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		JikengAnquanZhuti jikengAnquanZhuti = jikengAnquanZhutiService.getById(id);
		if(jikengAnquanZhuti==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(jikengAnquanZhuti);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param jikengAnquanZhuti
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, JikengAnquanZhuti jikengAnquanZhuti) {
        return super.exportXls(request, jikengAnquanZhuti, JikengAnquanZhuti.class, "jikeng_anquan_zhuti");
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
        return super.importExcel(request, response, JikengAnquanZhuti.class);
    }

}
