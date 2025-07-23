package com.trtm.iot.achievementslist.controller;

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
import com.trtm.iot.achievementslist.entity.AchievementsList;
import com.trtm.iot.achievementslist.service.IAchievementsListService;

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
 * @Description: 成果清单
 * @Author: jeecg-boot
 * @Date:   2022-07-12
 * @Version: V1.0
 */
@Api(tags="成果清单")
@RestController
@RequestMapping("/achievementslist/achievementsList")
@Slf4j
public class AchievementsListController extends JeecgController<AchievementsList, IAchievementsListService> {
	@Autowired
	private IAchievementsListService achievementsListService;

	/**
	 * 分页列表查询
	 *
	 * @param achievementsList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "成果清单-分页列表查询")
	@ApiOperation(value="成果清单-分页列表查询", notes="成果清单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AchievementsList achievementsList,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			achievementsList.setSysOrgCode(sys_depart_orgcode + "*");
		} else {
			achievementsList.setSysOrgCode(loginUser.getOrgCode() + "*");
		}
		QueryWrapper<AchievementsList> queryWrapper = QueryGenerator.initQueryWrapper(achievementsList, req.getParameterMap());
		Page<AchievementsList> page = new Page<AchievementsList>(pageNo, pageSize);
		IPage<AchievementsList> pageList = achievementsListService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 统计查询
	  *
	  * @param achievementsList
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "成果清单-统计查询（按类型）")
	 @ApiOperation(value="成果清单-统计查询（按类型）", notes="成果清单-统计查询（按类型）")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(AchievementsList achievementsList,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			 achievementsList.setSysOrgCode(sys_depart_orgcode + "*");
		 } else {
			 achievementsList.setSysOrgCode(loginUser.getOrgCode() + "*");
		 }
		 QueryWrapper<AchievementsList> queryWrapper = QueryGenerator.initQueryWrapper(achievementsList, req.getParameterMap());
		 queryWrapper.select("sum(outcome_num) outcome_num,sum(outcome_type) outcome_type,sum(outcome_unit) outcome_unit");
		 List<AchievementsList> pageList = achievementsListService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param achievementsList
	 * @return
	 */
	@AutoLog(value = "成果清单-添加")
	@ApiOperation(value="成果清单-添加", notes="成果清单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AchievementsList achievementsList) {
		achievementsListService.save(achievementsList);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param achievementsList
	 * @return
	 */
	@AutoLog(value = "成果清单-编辑")
	@ApiOperation(value="成果清单-编辑", notes="成果清单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AchievementsList achievementsList) {
		achievementsListService.updateById(achievementsList);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "成果清单-通过id删除")
	@ApiOperation(value="成果清单-通过id删除", notes="成果清单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		achievementsListService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "成果清单-批量删除")
	@ApiOperation(value="成果清单-批量删除", notes="成果清单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.achievementsListService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "成果清单-通过id查询")
	@ApiOperation(value="成果清单-通过id查询", notes="成果清单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AchievementsList achievementsList = achievementsListService.getById(id);
		if(achievementsList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(achievementsList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param achievementsList
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AchievementsList achievementsList) {
        return super.exportXls(request, achievementsList, AchievementsList.class, "成果清单");
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
        return super.importExcel(request, response, AchievementsList.class);
    }

}
