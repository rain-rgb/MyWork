package com.trtm.iot.staffbase.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.api.utils.StringUtils;
import com.trtm.iot.staffbase.service.IStaffBaseWorkService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.staffbase.entity.StaffBaseInfo;
import com.trtm.iot.staffbase.service.IStaffBaseInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: staff_base_info
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
@Api(tags="staff_base_info")
@RestController
@RequestMapping("/staffbase/staffBaseInfo")
@Slf4j
public class StaffBaseInfoController extends JeecgController<StaffBaseInfo, IStaffBaseInfoService> {
	@Autowired
	private IStaffBaseInfoService staffBaseInfoService;
	 @Autowired
	 private RedisUtil redisUtil;

	 @Autowired
	 private IStaffBaseWorkService staffBaseWorkService;
	/**
	 * 分页列表查询
	 *
	 * @param staffBaseInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "staff_base_info-分页列表查询")
	@ApiOperation(value="staff_base_info-分页列表查询", notes="staff_base_info-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StaffBaseInfo staffBaseInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if(StringUtils.isNotBlank(sys_depart_orgcode)){
			staffBaseInfo.setSysOrgCode(sys_depart_orgcode+"*");
		}else{
			staffBaseInfo.setSysOrgCode(loginUser.getOrgCode()+"*");
		}
		QueryWrapper<StaffBaseInfo> queryWrapper = QueryGenerator.initQueryWrapper(staffBaseInfo, req.getParameterMap());
		Page<StaffBaseInfo> page = new Page<StaffBaseInfo>(pageNo, pageSize);
		IPage<StaffBaseInfo> pageList = staffBaseInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "staff_base_info-按班组统计")
	 @ApiOperation(value="staff_base_info-按班组统计人数", notes="staff_base_info-按班组统计")
	 @GetMapping(value = "/queryByBanzu")
	 public Result<?> queryByBanzu(StaffBaseInfo staffBaseInfo,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									String sys_depart_orgcode,
									HttpServletRequest req) {
		 Result<List<Map<String, Object>>> result = new Result<>();
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 QueryWrapper<StaffBaseInfo> queryWrapper = new QueryWrapper<>();
		 queryWrapper.select("banzu ,count(1) renshu ");
		 queryWrapper.last("WHERE sys_org_code LIKE '"+loginUser.getOrgCode()+"%' AND banzu IS NOT NULL AND jintuichangzhuangtai=2 GROUP BY banzu");
		 List<Map<String, Object>> maps = staffBaseInfoService.listMaps(queryWrapper);
		 result.setResult(maps);
		 QueryWrapper<StaffBaseInfo> queryWrapper2 = new QueryWrapper<>();
		 queryWrapper2.select(" renyuantype ,count(1) renshu ");
		 queryWrapper2.last(" WHERE sys_org_code LIKE '"+loginUser.getOrgCode()+"%' AND renyuantype IS NOT NULL AND jintuichangzhuangtai=2 GROUP BY renyuantype");
		 List<Map<String, Object>> maps2 = staffBaseInfoService.listMaps(queryWrapper2);
		 result.setData(maps2);
		 return result;
	 }

//	 @AutoLog(value = "staff_base_info-按班组统计")
//	 @ApiOperation(value="staff_base_info-按班组统计人数", notes="staff_base_info-按班组统计")
//	 @GetMapping(value = "/queryrenshu")
//	 public Result<?> queryrenshu(StaffBaseInfo staffBaseInfo,
//								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//								   String sys_depart_orgcode,
//								   HttpServletRequest req) {
//		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//		 QueryWrapper<StaffBaseInfo> queryWrapper = new QueryWrapper<>();
//		 queryWrapper.select("banzu ,count(1) renshu ");
//		 queryWrapper.last("WHERE sys_org_code LIKE '"+loginUser.getOrgCode()+"%' AND banzu IS NOT NULL AND jintuichangzhuangtai=2 GROUP BY banzu");
//		 List<Map<String, Object>> maps = staffBaseInfoService.listMaps(queryWrapper);
//
//
//
//		 return Result.OK(maps);
//	 }

	 @AutoLog(value = "staff_base_info-看板统计")
	 @ApiOperation(value="staff_base_info-按班组统计人数", notes="staff_base_info-按班组统计")
	 @GetMapping(value = "/queryByKB")
	 public Result<?> queryByKB(StaffBaseInfo staffBaseInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		 Result<List<Map<String, Object>>> result = new Result<>();
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if(StringUtils.isBlank(sys_depart_orgcode)){
			 staffBaseInfo.setSysOrgCode(loginUser.getOrgCode()+"*");
		 }else {
			 staffBaseInfo.setSysOrgCode(sys_depart_orgcode+"*");
		 }
		 QueryWrapper<StaffBaseInfo> queryWrapper = QueryGenerator.initQueryWrapper(staffBaseInfo, req.getParameterMap());
		 queryWrapper.select("count(1) allpeople, \n" +
				 "FLOOR(count(CASE WHEN jintuichangzhuangtai = 2 THEN 1  END) ) zaibian,\n" +
				 "FLOOR(count(CASE WHEN jintuichangzhuangtai = 3 THEN 1  END) ) tuichang,\n" +
				 "FLOOR(count(CASE WHEN shifoubixudaogang = 1 THEN 1  END) ) kaoqin,\n" +
				 "FLOOR(count(CASE WHEN jintuichangzhuangtai = 2 && renyuantype = '实施人员'  THEN 1  END) ) shishi,\n" +
				 "FLOOR(count(CASE WHEN jintuichangzhuangtai = 2 && renyuantype = '管理人员' THEN 1  END) ) guanli");

		 List<Map<String, Object>> maps = staffBaseInfoService.listMaps(queryWrapper);
		 result.setResult(maps);

		 return result;
	 }


	 /**
	 *   添加
	 *
	 * @param staffBaseInfo
	 * @return
	 */
	@AutoLog(value = "staff_base_info-添加")
	@ApiOperation(value="staff_base_info-添加", notes="staff_base_info-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StaffBaseInfo staffBaseInfo) {
		staffBaseInfoService.save(staffBaseInfo);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param staffBaseInfo
	 * @return
	 */
	@AutoLog(value = "staff_base_info-编辑")
	@ApiOperation(value="staff_base_info-编辑", notes="staff_base_info-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StaffBaseInfo staffBaseInfo) {
		// 设置推送状态为未推送
		staffBaseInfo.setPushstatus(0);
		staffBaseInfoService.updateById(staffBaseInfo);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "staff_base_info-通过id删除")
	@ApiOperation(value="staff_base_info-通过id删除", notes="staff_base_info-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		staffBaseInfoService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "staff_base_info-批量删除")
	@ApiOperation(value="staff_base_info-批量删除", notes="staff_base_info-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.staffBaseInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "staff_base_info-通过id查询")
	@ApiOperation(value="staff_base_info-通过id查询", notes="staff_base_info-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StaffBaseInfo staffBaseInfo = staffBaseInfoService.getById(id);
		if(staffBaseInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(staffBaseInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param staffBaseInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StaffBaseInfo staffBaseInfo) {
        return super.exportXls(request, staffBaseInfo, StaffBaseInfo.class, "staff_base_info");
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
        return super.importExcel(request, response, StaffBaseInfo.class);
    }

}
