package com.trtm.iot.liangGongxuPeople.controller;

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
import com.trtm.iot.liangGongxuPeople.entity.LiangGongxuPeople;
import com.trtm.iot.liangGongxuPeople.service.ILiangGongxuPeopleService;

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
 * @Description: 自动工序负责人配置
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Api(tags="自动工序负责人配置")
@RestController
@RequestMapping("/liangGongxuPeople/liangGongxuPeople")
@Slf4j
public class LiangGongxuPeopleController extends JeecgController<LiangGongxuPeople, ILiangGongxuPeopleService> {
	@Autowired
	private ILiangGongxuPeopleService liangGongxuPeopleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param liangGongxuPeople
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "自动工序负责人配置-分页列表查询")
	@ApiOperation(value="自动工序负责人配置-分页列表查询", notes="自动工序负责人配置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(LiangGongxuPeople liangGongxuPeople,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		liangGongxuPeople.setSysOrgCode(loginUser.getOrgCode()+"*");
		QueryWrapper<LiangGongxuPeople> queryWrapper = QueryGenerator.initQueryWrapper(liangGongxuPeople, req.getParameterMap());
		Page<LiangGongxuPeople> page = new Page<LiangGongxuPeople>(pageNo, pageSize);
		IPage<LiangGongxuPeople> pageList = liangGongxuPeopleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param liangGongxuPeople
	 * @return
	 */
	@AutoLog(value = "自动工序负责人配置-添加")
	@ApiOperation(value="自动工序负责人配置-添加", notes="自动工序负责人配置-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody LiangGongxuPeople liangGongxuPeople) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		QueryWrapper<LiangGongxuPeople> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("xuhao",liangGongxuPeople.getXuhao());
		queryWrapper.eq("sys_org_code",loginUser.getOrgCode());
		List<LiangGongxuPeople> list = liangGongxuPeopleService.list(queryWrapper);
		if(list.size() != 0){
			return Result.error("该工序已有负责人，如需修改，请点击编辑！");
		}
		liangGongxuPeopleService.save(liangGongxuPeople);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param liangGongxuPeople
	 * @return
	 */
	@AutoLog(value = "自动工序负责人配置-编辑")
	@ApiOperation(value="自动工序负责人配置-编辑", notes="自动工序负责人配置-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody LiangGongxuPeople liangGongxuPeople) {
		liangGongxuPeopleService.updateById(liangGongxuPeople);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "自动工序负责人配置-通过id删除")
	@ApiOperation(value="自动工序负责人配置-通过id删除", notes="自动工序负责人配置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		liangGongxuPeopleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "自动工序负责人配置-批量删除")
	@ApiOperation(value="自动工序负责人配置-批量删除", notes="自动工序负责人配置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.liangGongxuPeopleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "自动工序负责人配置-通过id查询")
	@ApiOperation(value="自动工序负责人配置-通过id查询", notes="自动工序负责人配置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		LiangGongxuPeople liangGongxuPeople = liangGongxuPeopleService.getById(id);
		if(liangGongxuPeople==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(liangGongxuPeople);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param liangGongxuPeople
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LiangGongxuPeople liangGongxuPeople) {
        return super.exportXls(request, liangGongxuPeople, LiangGongxuPeople.class, "自动工序负责人配置");
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
        return super.importExcel(request, response, LiangGongxuPeople.class);
    }

}
