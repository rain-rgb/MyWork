package com.trtm.iot.wzpandian.controller;

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

import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzpandian.entity.Wzpandian;
import com.trtm.iot.wzpandian.service.IWzpandianService;

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
 * @Description: wzpandian
 * @Author: jeecg-boot
 * @Date:   2023-12-08
 * @Version: V1.0
 */
@Api(tags="wzpandian")
@RestController
@RequestMapping("/wzpandian/wzpandian")
@Slf4j
public class WzpandianController extends JeecgController<Wzpandian, IWzpandianService> {
	@Autowired
	private IWzpandianService wzpandianService;
	 @Autowired
	 private IWzliaocangService wzliaocangService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wzpandian
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wzpandian-分页列表查询")
	@ApiOperation(value="wzpandian-分页列表查询", notes="wzpandian-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Wzpandian wzpandian,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Wzpandian> queryWrapper = QueryGenerator.initQueryWrapper(wzpandian, req.getParameterMap());
		Page<Wzpandian> page = new Page<Wzpandian>(pageNo, pageSize);
		IPage<Wzpandian> pageList = wzpandianService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wzpandian
	 * @return
	 */
	@AutoLog(value = "wzpandian-添加")
	@ApiOperation(value="wzpandian-添加", notes="wzpandian-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Wzpandian wzpandian) {
		wzpandianService.save(wzpandian);
		return Result.OK("添加成功！");
	}
	 /**
	  *   扫码添加
	  *
	  * @param wzpandian
	  * @return
	  */
	 @AutoLog(value = "扫码添加")
	 @ApiOperation(value="扫码添加", notes="扫码添加")
	 @PostMapping(value = "/saoMaAdd")
	 public Result<?> saoMaAdd(@RequestBody Wzpandian wzpandian,String sys_depart_orgcode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			 wzpandian.setSysOrgCode(sys_depart_orgcode + "*");
		 } else {
			 wzpandian.setSysOrgCode(loginUser.getOrgCode() + "*");
		 }
		 wzpandian.setSysOrgCode(loginUser.getOrgCode());
		 //查当时仓库数量
		 String liaocangno = wzpandian.getLiaocangno();

		 Wzliaocang wzliaocang = wzliaocangService.getById(liaocangno);
		 if (wzliaocang!=null){
			 Double ljjinchang = wzliaocang.getLjjinchang();
			 Double ljshiyong = wzliaocang.getLjshiyong();
			 wzpandian.setLiaocangnum(ljjinchang-ljshiyong);
		 }
		 wzpandian.setCreatepersonl(loginUser.getRealname());
		 Date date = new Date();
		 wzpandian.setCreatetime(date);
		 wzpandianService.save(wzpandian);
		 return Result.OK("扫码添加成功！");
	 }
	
	/**
	 *  编辑
	 *
	 * @param wzpandian
	 * @return
	 */
	@AutoLog(value = "wzpandian-编辑")
	@ApiOperation(value="wzpandian-编辑", notes="wzpandian-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Wzpandian wzpandian) {
		wzpandianService.updateById(wzpandian);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzpandian-通过id删除")
	@ApiOperation(value="wzpandian-通过id删除", notes="wzpandian-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzpandianService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wzpandian-批量删除")
	@ApiOperation(value="wzpandian-批量删除", notes="wzpandian-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzpandianService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzpandian-通过id查询")
	@ApiOperation(value="wzpandian-通过id查询", notes="wzpandian-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Wzpandian wzpandian = wzpandianService.getById(id);
		if(wzpandian==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzpandian);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzpandian
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzpandian wzpandian) {
        return super.exportXls(request, wzpandian, Wzpandian.class, "wzpandian");
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
        return super.importExcel(request, response, Wzpandian.class);
    }

}
