package com.trtm.iot.staffbase.controller;

import java.text.SimpleDateFormat;
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

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.staffbase.entity.StaffBaseWork;
import com.trtm.iot.staffbase.service.IStaffBaseWorkService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.staffbase.entity.StaffWorkDetail;
import com.trtm.iot.staffbase.service.IStaffWorkDetailService;

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
 * @Description: staff_work_detail
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
@Api(tags="staff_work_detail")
@RestController
@RequestMapping("/staffbase/staffWorkDetail")
@Slf4j
public class StaffWorkDetailController extends JeecgController<StaffWorkDetail, IStaffWorkDetailService> {
	@Autowired
	private IStaffWorkDetailService staffWorkDetailService;
	 @Autowired
	 private IStaffBaseWorkService staffBaseWorkService;

	/**
	 * 分页列表查询
	 *
	 * @param staffWorkDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "staff_work_detail-考勤详细列表查询")
	@ApiOperation(value="staff_work_detail-考勤详细分页列表查询", notes="staff_work_detail-考勤详细分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StaffWorkDetail staffWorkDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StaffWorkDetail> queryWrapper = QueryGenerator.initQueryWrapper(staffWorkDetail, req.getParameterMap());
		Page<StaffWorkDetail> page = new Page<StaffWorkDetail>(pageNo, pageSize);
		IPage<StaffWorkDetail> pageList = staffWorkDetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param staffWorkDetail
	 * @return
	 */
	@AutoLog(value = "staff_work_detail-添加")
	@ApiOperation(value="staff_work_detail-添加", notes="staff_work_detail-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StaffWorkDetail staffWorkDetail) {
		staffWorkDetailService.save(staffWorkDetail);
		return Result.OK("添加成功！");
	}

	 @AutoLog(value = "staff_work_detail-上下班打卡")
	 @ApiOperation(value="staff_work_detail-上下班打卡", notes="staff_work_detail-上下班打卡")
	 @PostMapping(value = "/adddaka")
	 public Result<?> adddaka(@RequestBody StaffWorkDetail staffWorkDetail) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String strDateFormat = "yyyy-MM-dd";
		 SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		 String format = sdf.format(new Date());
		 staffWorkDetail.setDakatime(new Date());
		 //是否有考勤记录，有则更新，无则新增
		 QueryWrapper<StaffBaseWork> queryWrapperwork = new QueryWrapper<>();
		 queryWrapperwork.eq("create_by", loginUser.getUsername());
		 queryWrapperwork.eq("kaoqriq", format);
		 StaffBaseWork one1 = staffBaseWorkService.getOne(queryWrapperwork);
		 if(ObjectUtil.isEmpty(one1)){
			 one1.setSysOrgCode(loginUser.getOrgCode());
			 one1.setCreateBy(loginUser.getUsername());
			 one1.setXingming(loginUser.getRealname());
			 one1.setKaoqriq(new Date());
		 }
		 // 1 表示上班
		 if(staffWorkDetail.getType().equals("1")){
			 one1.setShangbantime(new Date());
		 }else if(staffWorkDetail.getType().equals("2")) {
			 // 2表示下班
			 one1.setXiabantime(new Date());
		 }
		 // 更新保存打卡记录
		 staffBaseWorkService.saveOrUpdate(one1);
		 staffWorkDetailService.save(staffWorkDetail);
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param staffWorkDetail
	 * @return
	 */
	@AutoLog(value = "staff_work_detail-编辑")
	@ApiOperation(value="staff_work_detail-编辑", notes="staff_work_detail-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StaffWorkDetail staffWorkDetail) {
		staffWorkDetailService.updateById(staffWorkDetail);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "staff_work_detail-通过id删除")
	@ApiOperation(value="staff_work_detail-通过id删除", notes="staff_work_detail-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		staffWorkDetailService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "staff_work_detail-批量删除")
	@ApiOperation(value="staff_work_detail-批量删除", notes="staff_work_detail-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.staffWorkDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "staff_work_detail-通过id查询")
	@ApiOperation(value="staff_work_detail-通过id查询", notes="staff_work_detail-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StaffWorkDetail staffWorkDetail = staffWorkDetailService.getById(id);
		if(staffWorkDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(staffWorkDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param staffWorkDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StaffWorkDetail staffWorkDetail) {
        return super.exportXls(request, staffWorkDetail, StaffWorkDetail.class, "staff_work_detail");
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
        return super.importExcel(request, response, StaffWorkDetail.class);
    }

}
