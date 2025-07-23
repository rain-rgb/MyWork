package com.trtm.iot.securitytrain.controller;

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
import com.trtm.iot.securitytrain.entity.SecurityTrain;
import com.trtm.iot.securitytrain.service.ISecurityTrainService;

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
 * @Description: security_train
 * @Author: jeecg-boot
 * @Date:   2024-08-22
 * @Version: V1.0
 */
@Api(tags="security_train")
@RestController
@RequestMapping("/securitytrain/securityTrain")
@Slf4j
public class SecurityTrainController extends JeecgController<SecurityTrain, ISecurityTrainService> {
	@Autowired
	private ISecurityTrainService securityTrainService;

	/**
	 * 分页列表查询
	 *
	 * @param securityTrain
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "security_train-分页列表查询")
	@ApiOperation(value="security_train-分页列表查询", notes="security_train-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SecurityTrain securityTrain,String sys_depart_orgcode ,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信
		if(StringUtils.isNotBlank(sys_depart_orgcode)){
			securityTrain.setSysOrgCode(sys_depart_orgcode+'*');
		}else{
			securityTrain.setSysOrgCode(loginUser.getOrgCode());
		}
		QueryWrapper<SecurityTrain> queryWrapper = QueryGenerator.initQueryWrapper(securityTrain, req.getParameterMap());
		Page<SecurityTrain> page = new Page<SecurityTrain>(pageNo, pageSize);
		IPage<SecurityTrain> pageList = securityTrainService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param securityTrain
	 * @return
	 */
	@AutoLog(value = "security_train-添加")
	@ApiOperation(value="security_train-添加", notes="security_train-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SecurityTrain securityTrain) {
		securityTrainService.save(securityTrain);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param securityTrain
	 * @return
	 */
	@AutoLog(value = "security_train-编辑")
	@ApiOperation(value="security_train-编辑", notes="security_train-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SecurityTrain securityTrain) {
		securityTrainService.updateById(securityTrain);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "security_train-通过id删除")
	@ApiOperation(value="security_train-通过id删除", notes="security_train-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		securityTrainService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "security_train-批量删除")
	@ApiOperation(value="security_train-批量删除", notes="security_train-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.securityTrainService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "security_train-通过id查询")
	@ApiOperation(value="security_train-通过id查询", notes="security_train-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SecurityTrain securityTrain = securityTrainService.getById(id);
		if(securityTrain==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(securityTrain);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param securityTrain
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SecurityTrain securityTrain) {
        return super.exportXls(request, securityTrain, SecurityTrain.class, "security_train");
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
        return super.importExcel(request, response, SecurityTrain.class);
    }

}
