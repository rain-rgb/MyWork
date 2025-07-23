package com.trtm.iot.memberList.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.memberList.entity.MemberList;
import com.trtm.iot.memberList.service.IMemberListService;

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
 * @Description: 在场成员名单
 * @Author: jeecg-boot
 * @Date:   2022-09-06
 * @Version: V1.0
 */
@Api(tags="在场成员名单")
@RestController
@RequestMapping("/memberList/memberList")
@Slf4j
public class MemberListController extends JeecgController<MemberList, IMemberListService> {
	@Autowired
	private IMemberListService memberListService;

	/**
	 * 分页列表查询
	 *
	 * @param memberList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "在场成员名单-分页列表查询")
	@ApiOperation(value="在场成员名单-分页列表查询", notes="在场成员名单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MemberList memberList,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			memberList.setSysOrgCode(sys_depart_orgcode + "*");
		}else {
			memberList.setSysOrgCode(loginUser.getOrgCode() + "*");
		}
		if (StrUtil.isNotBlank(memberList.getName())){
			memberList.setName("*"+memberList.getName()+"*");
		}
		QueryWrapper<MemberList> queryWrapper = QueryGenerator.initQueryWrapper(memberList, req.getParameterMap());
		Page<MemberList> page = new Page<MemberList>(pageNo, pageSize);
		IPage<MemberList> pageList = memberListService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param memberList
	 * @return
	 */
	@AutoLog(value = "在场成员名单-添加")
	@ApiOperation(value="在场成员名单-添加", notes="在场成员名单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MemberList memberList) {
		memberListService.save(memberList);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param memberList
	 * @return
	 */
	@AutoLog(value = "在场成员名单-编辑")
	@ApiOperation(value="在场成员名单-编辑", notes="在场成员名单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MemberList memberList) {
		memberListService.updateById(memberList);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "在场成员名单-通过id删除")
	@ApiOperation(value="在场成员名单-通过id删除", notes="在场成员名单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		memberListService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "在场成员名单-批量删除")
	@ApiOperation(value="在场成员名单-批量删除", notes="在场成员名单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.memberListService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "在场成员名单-通过id查询")
	@ApiOperation(value="在场成员名单-通过id查询", notes="在场成员名单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MemberList memberList = memberListService.getById(id);
		if(memberList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(memberList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param memberList
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MemberList memberList) {
        return super.exportXls(request, memberList, MemberList.class, "在场成员名单");
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
        return super.importExcel(request, response, MemberList.class);
    }

}
