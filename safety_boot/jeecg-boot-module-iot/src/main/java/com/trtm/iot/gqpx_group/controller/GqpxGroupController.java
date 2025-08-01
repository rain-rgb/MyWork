package com.trtm.iot.gqpx_group.controller;

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
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.gqpx_group.entity.GqpxGroup;
import com.trtm.iot.gqpx_group.service.IGqpxGroupService;

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
 * @Description: 岗前培训-班组
 * @Author: jeecg-boot
 * @Date:   2025-02-13
 * @Version: V1.0
 */
@Api(tags="岗前培训-班组")
@RestController
@RequestMapping("/gqpx_group/gqpxGroup")
@Slf4j
public class GqpxGroupController extends JeecgController<GqpxGroup, IGqpxGroupService> {
	@Autowired
	private IGqpxGroupService gqpxGroupService;
	
	/**
	 * 分页列表查询
	 *
	 * @param gqpxGroup
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "岗前培训-班组-分页列表查询")
	@ApiOperation(value="岗前培训-班组-分页列表查询", notes="岗前培训-班组-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GqpxGroup gqpxGroup,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String orgcode = gqpxGroup.getOrgcode();
		if (orgcode != null){
			gqpxGroup.setOrgcode(orgcode+"*");
		}else {
			gqpxGroup.setOrgcode(loginUser.getOrgCode()+"*");
		}
		QueryWrapper<GqpxGroup> queryWrapper = QueryGenerator.initQueryWrapper(gqpxGroup, req.getParameterMap());
		Page<GqpxGroup> page = new Page<GqpxGroup>(pageNo, pageSize);
		IPage<GqpxGroup> pageList = gqpxGroupService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param gqpxGroup
	 * @return
	 */
	@AutoLog(value = "岗前培训-班组-添加")
	@ApiOperation(value="岗前培训-班组-添加", notes="岗前培训-班组-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GqpxGroup gqpxGroup) {
		gqpxGroupService.save(gqpxGroup);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param gqpxGroup
	 * @return
	 */
	@AutoLog(value = "岗前培训-班组-编辑")
	@ApiOperation(value="岗前培训-班组-编辑", notes="岗前培训-班组-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GqpxGroup gqpxGroup) {
		gqpxGroupService.updateById(gqpxGroup);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "岗前培训-班组-通过id删除")
	@ApiOperation(value="岗前培训-班组-通过id删除", notes="岗前培训-班组-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		gqpxGroupService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "岗前培训-班组-批量删除")
	@ApiOperation(value="岗前培训-班组-批量删除", notes="岗前培训-班组-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gqpxGroupService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "岗前培训-班组-通过id查询")
	@ApiOperation(value="岗前培训-班组-通过id查询", notes="岗前培训-班组-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		GqpxGroup gqpxGroup = gqpxGroupService.getById(id);
		if(gqpxGroup==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(gqpxGroup);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gqpxGroup
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GqpxGroup gqpxGroup) {
        return super.exportXls(request, gqpxGroup, GqpxGroup.class, "岗前培训-班组");
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
        return super.importExcel(request, response, GqpxGroup.class);
    }

}
