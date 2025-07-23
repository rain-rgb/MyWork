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

import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.jikeng.service.IJikengWeiyCfgService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.jikeng.entity.JikengShuiwei;
import com.trtm.iot.jikeng.service.IJikengShuiweiService;

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
 * @Description: jikeng_shuiwei
 * @Author: jeecg-boot
 * @Date:   2024-05-23
 * @Version: V1.0
 */
@Api(tags="jikeng_shuiwei")
@RestController
@RequestMapping("/jikeng/jikengShuiwei")
@Slf4j
public class JikengShuiweiController extends JeecgController<JikengShuiwei, IJikengShuiweiService> {
	@Autowired
	private IJikengShuiweiService jikengShuiweiService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;
	 @Autowired
	 private IJikengWeiyCfgService jikengWeiyCfgService;

	/**
	 * 分页列表查询
	 *
	 * @param jikengShuiwei
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "jikeng_shuiwei-分页列表查询")
	@ApiOperation(value="jikeng_shuiwei-分页列表查询", notes="jikeng_shuiwei-分页列表查询")
	@GetMapping(value = "/list")
	@PermissionData(pageComponent = "anquan/jikeng/JikengShuiweiList")
	public Result<?> queryPageList(JikengShuiwei jikengShuiwei,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (StringUtils.isBlank(jikengShuiwei.getDevsn()) ) {
			if (!"null" .equals(shebei)) {
				jikengShuiwei.setDevsn(shebei);
			} else {
				jikengShuiwei.setDevsn("空");
			}
		}
		QueryWrapper<JikengShuiwei> queryWrapper = QueryGenerator.initQueryWrapper(jikengShuiwei, req.getParameterMap());
		Page<JikengShuiwei> page = new Page<JikengShuiwei>(pageNo, pageSize);
		IPage<JikengShuiwei> pageList = jikengShuiweiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param jikengShuiwei
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "jikeng_shuiwei-分页列表查询")
	 @ApiOperation(value="jikeng_shuiwei-分页列表查询", notes="jikeng_shuiwei-分页列表查询")
	 @GetMapping(value = "/lists")
	 @PermissionData(pageComponent = "anquan/jikeng/JikengShuiweiList")
	 public Result<?> queryPageLists(JikengShuiwei jikengShuiwei,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 String sys_depart_orgcode,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebeis = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 String code = StringUtils.isBlank(sys_depart_orgcode)?loginUser.getOrgCode():sys_depart_orgcode;
		 if( StringUtils.isNotBlank(jikengShuiwei.getNote())){
			 List<ShebeiInfo> shebeiInfos = shebeiInfoService.shebeilist(Integer.parseInt(jikengShuiwei.getNote()), code);
			 if (shebeiInfos.size() > 0  ) {
				 shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
			 }else{
				 shebeis = "";
			 }
			 if (StringUtils.isBlank(jikengShuiwei.getDevsn())) {
				 if ("null".equals(shebeis) || StringUtils.isEmpty(shebeis)) {
					 shebeis = "空";
				 }

			 }else{
				 shebeis =  jikengShuiwei.getDevsn();
			 }
			 jikengShuiwei.setNote(null);
		 }

		 jikengShuiwei.setDevsn(shebeis);
		 QueryWrapper<JikengShuiwei> queryWrapper = QueryGenerator.initQueryWrapper(jikengShuiwei, req.getParameterMap());
		 Page<JikengShuiwei> page = new Page<JikengShuiwei>(pageNo, pageSize);
		 IPage<JikengShuiwei> pageList = jikengShuiweiService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }


	/**
	 *   添加
	 *
	 * @param jikengShuiwei
	 * @return
	 */
	@AutoLog(value = "jikeng_shuiwei-添加")
	@ApiOperation(value="jikeng_shuiwei-添加", notes="jikeng_shuiwei-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JikengShuiwei jikengShuiwei) {
		JSONObject json = new JSONObject();
		json.put("水位值",String.format("%.2f",Double.parseDouble( jikengShuiwei.getDepth()))+"cm");
		jikengWeiyCfgService.updateByShbeino(jikengShuiwei.getDevsn(),json.toJSONString());
		jikengShuiweiService.save(jikengShuiwei);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param jikengShuiwei
	 * @return
	 */
	@AutoLog(value = "jikeng_shuiwei-编辑")
	@ApiOperation(value="jikeng_shuiwei-编辑", notes="jikeng_shuiwei-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JikengShuiwei jikengShuiwei) {
		jikengShuiweiService.updateById(jikengShuiwei);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jikeng_shuiwei-通过id删除")
	@ApiOperation(value="jikeng_shuiwei-通过id删除", notes="jikeng_shuiwei-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		jikengShuiweiService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "jikeng_shuiwei-批量删除")
	@ApiOperation(value="jikeng_shuiwei-批量删除", notes="jikeng_shuiwei-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.jikengShuiweiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jikeng_shuiwei-通过id查询")
	@ApiOperation(value="jikeng_shuiwei-通过id查询", notes="jikeng_shuiwei-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		JikengShuiwei jikengShuiwei = jikengShuiweiService.getById(id);
		if(jikengShuiwei==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(jikengShuiwei);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param jikengShuiwei
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, JikengShuiwei jikengShuiwei) {
        return super.exportXls(request, jikengShuiwei, JikengShuiwei.class, "jikeng_shuiwei");
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
        return super.importExcel(request, response, JikengShuiwei.class);
    }

}
