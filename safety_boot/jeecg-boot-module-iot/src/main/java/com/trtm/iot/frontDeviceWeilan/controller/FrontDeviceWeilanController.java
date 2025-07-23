package com.trtm.iot.frontDeviceWeilan.controller;

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
import com.trtm.iot.frontDeviceWeilan.entity.FrontDeviceWeilan;
import com.trtm.iot.frontDeviceWeilan.service.IFrontDeviceWeilanService;

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
 * @Description: 运输车电子围栏数据
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
@Api(tags="运输车电子围栏数据")
@RestController
@RequestMapping("/frontDeviceWeilan/frontDeviceWeilan")
@Slf4j
public class FrontDeviceWeilanController extends JeecgController<FrontDeviceWeilan, IFrontDeviceWeilanService> {
	@Autowired
	private IFrontDeviceWeilanService frontDeviceWeilanService;

	/**
	 * 分页列表查询
	 *
	 * @param frontDeviceWeilan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "运输车电子围栏数据-分页列表查询")
	@ApiOperation(value="运输车电子围栏数据-分页列表查询", notes="运输车电子围栏数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FrontDeviceWeilan frontDeviceWeilan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FrontDeviceWeilan> queryWrapper = QueryGenerator.initQueryWrapper(frontDeviceWeilan, req.getParameterMap());
		Page<FrontDeviceWeilan> page = new Page<FrontDeviceWeilan>(pageNo, pageSize);
		IPage<FrontDeviceWeilan> pageList = frontDeviceWeilanService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "运输车电子围栏数据-分页列表查询")
	 @ApiOperation(value="运输车电子围栏数据-分页列表查询", notes="运输车电子围栏数据-分页列表查询")
	 @GetMapping(value = "/listGroutByDepart")
	 public Result<?> listGroutByDepart(FrontDeviceWeilan frontDeviceWeilan,String sys_depart_orgcode,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String code = loginUser.getOrgCode();
		 if(StringUtils.isNotBlank(sys_depart_orgcode)){
			 code = sys_depart_orgcode;
		 }
		 QueryWrapper<FrontDeviceWeilan> queryWrapper = QueryGenerator.initQueryWrapper(frontDeviceWeilan, req.getParameterMap());
		 queryWrapper.select(" w.shebei_no,w.lat,w.lng,w.center,w.radius,sb.sys_org_code createBy ,count(1) gfid,d.depart_name name ,d.depart_name_abbr tname ");
		 queryWrapper.last("  w  LEFT JOIN shebei_info  sb on " +
				 " w.shebei_no = sb.sbjno  LEFT JOIN sys_depart d  " +
				 " on  sb.sys_org_code = d.org_code where sb.sys_org_code LIKE  CONCAT('"+ code+"','%') GROUP BY sb.sys_org_code");
		 Page<FrontDeviceWeilan> page = new Page<FrontDeviceWeilan>(pageNo, pageSize);
		 IPage<FrontDeviceWeilan> pageList = frontDeviceWeilanService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }


	 /**
	  * 列表查询
	  *
	  * @param frontDeviceWeilan
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "运输车电子围栏数据-列表查询")
	 @ApiOperation(value="运输车电子围栏数据-列表查询", notes="运输车电子围栏数据-列表查询")
	 @GetMapping(value = "/querylist")
	 public Result<?> queryPageList1(FrontDeviceWeilan frontDeviceWeilan, HttpServletRequest req) {
		 QueryWrapper<FrontDeviceWeilan> queryWrapper = QueryGenerator.initQueryWrapper(frontDeviceWeilan, req.getParameterMap());
		 List<FrontDeviceWeilan> list = frontDeviceWeilanService.list(queryWrapper);
		 return Result.OK(list);
	 }


	/**
	 *   添加
	 *
	 * @param frontDeviceWeilan
	 * @return
	 */
	@AutoLog(value = "运输车电子围栏数据-添加")
	@ApiOperation(value="运输车电子围栏数据-添加", notes="运输车电子围栏数据-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FrontDeviceWeilan frontDeviceWeilan) {
		QueryWrapper<FrontDeviceWeilan> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("shebei_no",frontDeviceWeilan.getShebeiNo());
		FrontDeviceWeilan one = frontDeviceWeilanService.getOne(queryWrapper);
		frontDeviceWeilan.setIsdel(0);
		if(one!=null){
			boolean b = frontDeviceWeilanService.update(frontDeviceWeilan,queryWrapper);
			if(b==true){
				return Result.OK("电子围栏修改成功!");
			}else{
				return Result.error("电子围栏修改失败!");
			}

		}
		frontDeviceWeilanService.save(frontDeviceWeilan);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param frontDeviceWeilan
	 * @return
	 */
	@AutoLog(value = "运输车电子围栏数据-编辑")
	@ApiOperation(value="运输车电子围栏数据-编辑", notes="运输车电子围栏数据-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FrontDeviceWeilan frontDeviceWeilan) {
		frontDeviceWeilanService.updateById(frontDeviceWeilan);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运输车电子围栏数据-通过id删除")
	@ApiOperation(value="运输车电子围栏数据-通过id删除", notes="运输车电子围栏数据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		frontDeviceWeilanService.removeById(id);
		return Result.OK("删除成功!");
	}

	 /**
	  *   通过id编辑修改删除状态
	  *
	  * @param frontDeviceWeilan
	  * @return
	  */
	 @AutoLog(value = "运输车电子围栏数据-通过id编辑修改删除状态")
	 @ApiOperation(value="运输车电子围栏数据-通过id编辑修改删除状态", notes="运输车电子围栏数据-通过id编辑修改删除状态")
	 @PutMapping(value = "/isdel")
	 public Result<?> deleteisdel(@RequestBody FrontDeviceWeilan frontDeviceWeilan) {
		 frontDeviceWeilan.setIsdel(1);
		 frontDeviceWeilanService.updateById(frontDeviceWeilan);
		 return Result.OK("删除成功!");
	 }

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "运输车电子围栏数据-批量删除")
	@ApiOperation(value="运输车电子围栏数据-批量删除", notes="运输车电子围栏数据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.frontDeviceWeilanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运输车电子围栏数据-通过id查询")
	@ApiOperation(value="运输车电子围栏数据-通过id查询", notes="运输车电子围栏数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FrontDeviceWeilan frontDeviceWeilan = frontDeviceWeilanService.getById(id);
		if(frontDeviceWeilan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(frontDeviceWeilan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param frontDeviceWeilan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FrontDeviceWeilan frontDeviceWeilan) {
        return super.exportXls(request, frontDeviceWeilan, FrontDeviceWeilan.class, "运输车电子围栏数据");
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
        return super.importExcel(request, response, FrontDeviceWeilan.class);
    }

}
