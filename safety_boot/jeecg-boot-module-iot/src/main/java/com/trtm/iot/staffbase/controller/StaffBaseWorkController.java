package com.trtm.iot.staffbase.controller;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.api.utils.StringUtils;
import com.trtm.iot.staffbase.entity.StaffBaseSalary;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.staffbase.entity.StaffBaseWork;
import com.trtm.iot.staffbase.service.IStaffBaseWorkService;

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
 * @Description: staff_base_work
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
@Api(tags="staff_base_work")
@RestController
@RequestMapping("/staffbase/staffBaseWork")
@Slf4j
public class StaffBaseWorkController extends JeecgController<StaffBaseWork, IStaffBaseWorkService> {
	@Autowired
	private IStaffBaseWorkService staffBaseWorkService;

	/**
	 * 分页列表查询
	 *
	 * @param staffBaseWork
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "staff_base_work-分页列表查询")
	@ApiOperation(value="staff_base_work-分页列表查询", notes="staff_base_work-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StaffBaseWork staffBaseWork,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if(StringUtils.isNotBlank(sys_depart_orgcode)){
			staffBaseWork.setSysOrgCode(sys_depart_orgcode+"*");
		}else {
			staffBaseWork.setSysOrgCode(loginUser.getOrgCode()+"*");
		}
		QueryWrapper<StaffBaseWork> queryWrapper = QueryGenerator.initQueryWrapper(staffBaseWork, req.getParameterMap());
		Page<StaffBaseWork> page = new Page<StaffBaseWork>(pageNo, pageSize);
		IPage<StaffBaseWork> pageList = staffBaseWorkService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "staff_base_work-统计当天考勤人数")
	 @ApiOperation(value="staff_base_work-统计当天考勤人数", notes="统计当天考勤人数")
	 @GetMapping(value = "/todayKaoqing")
	 public Result<?> todayKaoqing(StaffBaseWork staffBaseWork,
								   HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 QueryWrapper<StaffBaseWork> queryWrapper =new QueryWrapper<>();
		 queryWrapper.select(" renyuantype,count(1) todaypeople");
		 queryWrapper.likeRight("sys_org_code",loginUser.getOrgCode());
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 queryWrapper.eq("kaoqriq",sdf.format(new Date()));
		 queryWrapper.groupBy("renyuantype");
		 List<Map<String, Object>> maps = staffBaseWorkService.listMaps(queryWrapper);

		 return Result.OK(maps);
	 }

	 @AutoLog(value = "staff_base_work-统计工时")
	 @ApiOperation(value="staff_base_work-统计工时", notes="staff_base_work-统计工时")
	 @GetMapping(value = "/tjgongshitj")
	 public Result<?> queryGongshi(StaffBaseWork staffBaseWork,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 QueryWrapper<StaffBaseWork> queryWrapper =new QueryWrapper<>();
		 queryWrapper.select(" xingming, " +
				 " biaoduan, " +
				 " zhiwu, " +
				 " banzu,  " +// -- 总在岗时长（按所有记录的上班时间和下班时间计算）
				 " CONCAT(FLOOR(SUM(CASE " + //-- 如果一天只打一次卡，则认为工时为8小时
				 " WHEN  shangbantime is NULL OR  xiabantime IS NULL THEN  8 * 3600  " + // -- 否则按实际时间差计算
				 " ELSE TIMESTAMPDIFF( SECOND, shangbantime, xiabantime )  END  ) / 3600  ), '小时'  " +
				 " ) AS total_hours, " + // 当月在岗时长（按当月记录的上班时间和下班时间计算）
				 " CONCAT( FLOOR(SUM( CASE WHEN   MONTH ( shangbantime ) = MONTH ( CURDATE())  AND (  shangbantime is NULL OR  xiabantime IS NULL ) THEN  8 * 3600 " +
				 "  WHEN MONTH ( shangbantime ) = MONTH (  CURDATE()) THEN  TIMESTAMPDIFF( SECOND, shangbantime, xiabantime ) ELSE 0   END   ) / 3600   ), '小时'  \t) \n" +
				 "  AS current_month_hours ");
		 queryWrapper.last(" WHERE  \n" +
				"sys_org_code like '"+loginUser.getOrgCode()+"%'  " +
				 " GROUP BY \n" +
				 "     xingming\n" +
				 " ORDER BY \n" +
				 "    total_hours DESC");
		 List<Map<String, Object>> maps = staffBaseWorkService.listMaps(queryWrapper);

		 return Result.OK(maps);
	 }

	 @AutoLog(value = "staff_base_work-统计月度工时和考勤天数")
	 @ApiOperation(value="staff_base_work-统计月度工时和考勤天数", notes="staff_base_work-统计工时")
	 @GetMapping(value = "/MothonHours")
	 public Result<?> queryGongshiMothon(StaffBaseWork staffBaseWork,
										 String sys_depart_orgcode,
                                   String mothon,
										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if(StringUtils.isBlank(sys_depart_orgcode)){
			 sys_depart_orgcode=loginUser.getOrgCode();
		 }
		 if(StringUtils.isBlank(staffBaseWork.getNote())){
			 Date day=new Date();
			 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			 String format = df.format(day);
			 staffBaseWork.setNote(format);
		 }
		 QueryWrapper<StaffBaseWork> queryWrapper =new QueryWrapper<>();
		 queryWrapper.select(" xingming AS xingming, id, \n" +
				 "    shenfenz, \n" +
						"'"+ staffBaseWork.getNote()+"'" +"as note,"+
				 "    sys_org_code, \n" +
				 "    banzu, \n" +
				 "    COUNT(1) AS shifoubixudaogang,  \n" +
				 "    FLOOR(SUM(CASE WHEN kaoqriq LIKE CONCAT('"+staffBaseWork.getNote()+"', '%')  AND (  shangbantime is NULL OR  xiabantime IS NULL ) THEN  8 * 3600" +
				 "     WHEN kaoqriq LIKE CONCAT('"+staffBaseWork.getNote()+"', '%') THEN TIMESTAMPDIFF(SECOND, shangbantime, xiabantime)  ELSE 0 END) / 3600) AS jintuichangzhuangtai");
		 queryWrapper.likeRight("sys_org_code",sys_depart_orgcode);
		 queryWrapper.likeRight("kaoqriq",staffBaseWork.getNote());
		 queryWrapper.like(StringUtils.isNotBlank(staffBaseWork.getXingming()),"xingming",staffBaseWork.getXingming());
		 queryWrapper.like(StringUtils.isNotBlank(staffBaseWork.getBanzu()),"banzu",staffBaseWork.getBanzu());
		 queryWrapper.groupBy("xingming"," shenfenz");
		 Page<StaffBaseWork> page = new Page<StaffBaseWork>(pageNo, pageSize);
		 IPage<StaffBaseWork> pageList = staffBaseWorkService.page(page, queryWrapper);

//		 List<StaffBaseSalary> mothonHours = staffBaseWorkService.getMothonHours(sys_depart_orgcode, mothon,pageNo,pageSize);

//		 return Result.OK(maps);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param staffBaseWork
	 * @return
	 */
	@AutoLog(value = "staff_base_work-添加")
	@ApiOperation(value="staff_base_work-添加", notes="staff_base_work-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StaffBaseWork staffBaseWork) {
		staffBaseWorkService.save(staffBaseWork);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param staffBaseWork
	 * @return
	 */
	@AutoLog(value = "staff_base_work-编辑")
	@ApiOperation(value="staff_base_work-编辑", notes="staff_base_work-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StaffBaseWork staffBaseWork) {
		staffBaseWorkService.updateById(staffBaseWork);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "staff_base_work-通过id删除")
	@ApiOperation(value="staff_base_work-通过id删除", notes="staff_base_work-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		staffBaseWorkService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "staff_base_work-批量删除")
	@ApiOperation(value="staff_base_work-批量删除", notes="staff_base_work-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.staffBaseWorkService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "staff_base_work-通过id查询")
	@ApiOperation(value="staff_base_work-通过id查询", notes="staff_base_work-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StaffBaseWork staffBaseWork = staffBaseWorkService.getById(id);
		if(staffBaseWork==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(staffBaseWork);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param staffBaseWork
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StaffBaseWork staffBaseWork) {
        return super.exportXls(request, staffBaseWork, StaffBaseWork.class, "staff_base_work");
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
        return super.importExcel(request, response, StaffBaseWork.class);
    }

}
