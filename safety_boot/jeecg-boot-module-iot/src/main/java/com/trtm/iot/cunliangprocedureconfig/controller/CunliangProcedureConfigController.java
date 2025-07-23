package com.trtm.iot.cunliangprocedureconfig.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.trtm.iot.cunliangprocedureconfig.vo.CunliangVO;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;

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
 * @Description: 梁场台座管理表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Api(tags="梁场台座管理表信息")
@RestController
@RequestMapping("/cunliangprocedureconfig/cunliangProcedureConfig")
@Slf4j
public class CunliangProcedureConfigController extends JeecgController<CunliangProcedureConfig, ICunliangProcedureConfigService> {
	@Autowired
	private ICunliangProcedureConfigService cunliangProcedureConfigService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param cunliangProcedureConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "梁场台座管理表信息-分页列表查询")
	@ApiOperation(value="梁场台座管理表信息-分页列表查询", notes="梁场台座管理表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CunliangProcedureConfig cunliangProcedureConfig,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (cunliangProcedureConfig.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				cunliangProcedureConfig.setShebeino(shebei);
			}else {
				cunliangProcedureConfig.setShebeino("空");
			}
		}
		if (!StringUtil.isEmpty(cunliangProcedureConfig.getLiangzuoname())){
			cunliangProcedureConfig.setLiangzuoname("*"+cunliangProcedureConfig.getLiangzuoname()+"*");
		}
		QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
		Page<CunliangProcedureConfig> page = new Page<CunliangProcedureConfig>(pageNo, pageSize);
		IPage<CunliangProcedureConfig> pageList = cunliangProcedureConfigService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param cunliangProcedureConfig
	 * @return
	 */
	@AutoLog(value = "梁场台座管理表信息-添加")
	@ApiOperation(value="梁场台座管理表信息-添加", notes="梁场台座管理表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CunliangProcedureConfig cunliangProcedureConfig) {
		if (!StrUtil.isNotBlank(cunliangProcedureConfig.getUuid())){
			cunliangProcedureConfig.setSwitchsta(0);
		}
		cunliangProcedureConfigService.save(cunliangProcedureConfig);
		return Result.OK("添加成功！");
	}

	 /**
	  * 查询当前用户下的台座信息(根据当前用户下的设备权限查询)
	  *
	  * @param cunliangProcedureConfig
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "梁场台座管理表信息-台座信息")
	 @ApiOperation(value="梁场台座管理表信息-台座信息", notes="梁场台座管理表信息-台座信息")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(CunliangProcedureConfig cunliangProcedureConfig,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (cunliangProcedureConfig.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 cunliangProcedureConfig.setShebeino(shebei);
			 }else {
				 cunliangProcedureConfig.setShebeino("空");
			 }
		 }
		 QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
		 List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper);
		 return Result.OK(list);
	 }


	 /**
	  * 查询当前用户下的台座信息(根据当前用户下的设备权限查询)
	  *
	  * @param cunliangProcedureConfig
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "梁场台座管理表信息-台座信息")
	 @ApiOperation(value="梁场台座管理表信息-台座信息", notes="梁场台座管理表信息-台座信息")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(CunliangProcedureConfig cunliangProcedureConfig,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (cunliangProcedureConfig.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 cunliangProcedureConfig.setShebeino(shebei);
			 }else {
				 cunliangProcedureConfig.setShebeino("空");
			 }
		 }
		 QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
		 queryWrapper.groupBy("liangzuoname","shebeino");
		 List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper);
		 return Result.OK(list);
	 }

	/**
	 *  编辑
	 *
	 * @param cunliangProcedureConfig
	 * @return
	 */
	@AutoLog(value = "梁场台座管理表信息-编辑")
	@ApiOperation(value="梁场台座管理表信息-编辑", notes="梁场台座管理表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CunliangProcedureConfig cunliangProcedureConfig) {
		cunliangProcedureConfigService.updateById(cunliangProcedureConfig);
		return Result.OK("编辑成功!");
	}

	 /**
	  *  编辑
	  *
	  * @param cunliangProcedureConfig
	  * @return
	  */
	 @AutoLog(value = "梁场台座管理表信息-编辑")
	 @ApiOperation(value="梁场台座管理表信息-编辑", notes="梁场台座管理表信息-编辑")
	 @PutMapping(value = "/edit1")
	 public Result<?> edit1(@RequestBody CunliangProcedureConfig cunliangProcedureConfig) {
	 	if (!StrUtil.isNotBlank(cunliangProcedureConfig.getUuid())){
	 		cunliangProcedureConfig.setSwitchsta(0);
		}
	 	QueryWrapper<CunliangProcedureConfig> queryWrapper = new QueryWrapper<>();
	 	queryWrapper.eq("id",cunliangProcedureConfig.getId());
	 	queryWrapper.eq("status",0);
	 	queryWrapper.eq("status1",0);
	 	List<CunliangProcedureConfig> cunliangProcedureConfig1 = cunliangProcedureConfigService.list(queryWrapper);
	 	if (cunliangProcedureConfig1.size()> 0){
	 		cunliangProcedureConfigService.updateById(cunliangProcedureConfig);
	 		return Result.OK("编辑成功!");
	 	}else {
	 		return Result.error("该台座不可编辑!");
	 	}
	 }

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "梁场台座管理表信息-通过id删除")
	@ApiOperation(value="梁场台座管理表信息-通过id删除", notes="梁场台座管理表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) Integer id) {
		CunliangProcedureConfig cunliangProcedureConfig = cunliangProcedureConfigService.getById(id);
		if (cunliangProcedureConfig.getStatus() == 0 && cunliangProcedureConfig.getStatus1() == 0){
			cunliangProcedureConfigService.removeById(id);
			return Result.OK("删除成功!");
		}else {
			return Result.error("该台座不可删除!");
		}
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "梁场台座管理表信息-批量删除")
	@ApiOperation(value="梁场台座管理表信息-批量删除", notes="梁场台座管理表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cunliangProcedureConfigService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "梁场台座管理表信息-通过id查询")
	@ApiOperation(value="梁场台座管理表信息-通过id查询", notes="梁场台座管理表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CunliangProcedureConfig cunliangProcedureConfig = cunliangProcedureConfigService.getById(id);
		if(cunliangProcedureConfig==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cunliangProcedureConfig);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cunliangProcedureConfig
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CunliangProcedureConfig cunliangProcedureConfig) {
        return super.exportXls(request, cunliangProcedureConfig, CunliangProcedureConfig.class, "梁场台座管理表信息");
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
        return super.importExcel(request, response, CunliangProcedureConfig.class);
    }

	 /**
	  * 存梁统计
	  *
	  * @param
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "存梁统计")
	 @ApiOperation(value="存梁统计", notes="存梁统计")
	 @GetMapping(value = "/querytj")
	 public Result<?> querytj(CunliangProcedureConfig cunliangProcedureConfig,
							   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
							   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, String sys_depart_orgcode,
							   HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (cunliangProcedureConfig.getShebeino() == null) {
			 if (!"null".equals(shebei)) {
				 cunliangProcedureConfig.setShebeino(shebei);
			 } else {
				 cunliangProcedureConfig.setShebeino("空");
			 }
		 }

		 QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
		 queryWrapper.select("sum(status) status," + "sum(status1) status1");
		 queryWrapper.groupBy("liangzuoname");
		 List<CunliangProcedureConfig> pageList = cunliangProcedureConfigService.list(queryWrapper);
		 CunliangVO vo = new CunliangVO();
		 vo.setSum(pageList.size());
		 vo.setCunliang(0);

		 for(CunliangProcedureConfig cunliang : pageList){
			 if(cunliang.getStatus()==0 && cunliang.getStatus1()==0){
				 vo.setCunliang(vo.getCunliang()+1);
			 }
		 }

		 return Result.OK(vo);
	 }
}
