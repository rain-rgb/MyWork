package com.trtm.iot.bhzcailiaocbtj.controller;

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
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzcailiaocbtj.entity.BhzCailiaoCbtj;
import com.trtm.iot.bhzcailiaocbtj.service.IBhzCailiaoCbtjService;

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
 * @Description: bhz_cailiao_cbtj
 * @Author: jeecg-boot
 * @Date:   2022-12-06
 * @Version: V1.0
 */
@Api(tags="bhz_cailiao_cbtj")
@RestController
@RequestMapping("/bhzcailiaocbtj/bhzCailiaoCbtj")
@Slf4j
public class BhzCailiaoCbtjController extends JeecgController<BhzCailiaoCbtj, IBhzCailiaoCbtjService> {
	@Autowired
	private IBhzCailiaoCbtjService bhzCailiaoCbtjService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param bhzCailiaoCbtj
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bhz_cailiao_cbtj-分页列表查询")
	@ApiOperation(value="bhz_cailiao_cbtj-分页列表查询", notes="bhz_cailiao_cbtj-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzCailiaoCbtj bhzCailiaoCbtj,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if(bhzCailiaoCbtj.getShebeiNo()==null){
			if (shebei != null) {
				bhzCailiaoCbtj.setShebeiNo(shebei);
			}
		}
		QueryWrapper<BhzCailiaoCbtj> queryWrapper = QueryGenerator.initQueryWrapper(bhzCailiaoCbtj, req.getParameterMap());
		Page<BhzCailiaoCbtj> page = new Page<BhzCailiaoCbtj>(pageNo, pageSize);
		IPage<BhzCailiaoCbtj> pageList = bhzCailiaoCbtjService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bhzCailiaoCbtj
	 * @return
	 */
	@AutoLog(value = "bhz_cailiao_cbtj-添加")
	@ApiOperation(value="bhz_cailiao_cbtj-添加", notes="bhz_cailiao_cbtj-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzCailiaoCbtj bhzCailiaoCbtj) {
		bhzCailiaoCbtjService.save(bhzCailiaoCbtj);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bhzCailiaoCbtj
	 * @return
	 */
	@AutoLog(value = "bhz_cailiao_cbtj-编辑")
	@ApiOperation(value="bhz_cailiao_cbtj-编辑", notes="bhz_cailiao_cbtj-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzCailiaoCbtj bhzCailiaoCbtj) {
		bhzCailiaoCbtjService.updateById(bhzCailiaoCbtj);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_cailiao_cbtj-通过id删除")
	@ApiOperation(value="bhz_cailiao_cbtj-通过id删除", notes="bhz_cailiao_cbtj-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzCailiaoCbtjService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bhz_cailiao_cbtj-批量删除")
	@ApiOperation(value="bhz_cailiao_cbtj-批量删除", notes="bhz_cailiao_cbtj-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzCailiaoCbtjService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bhz_cailiao_cbtj-通过id查询")
	@ApiOperation(value="bhz_cailiao_cbtj-通过id查询", notes="bhz_cailiao_cbtj-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzCailiaoCbtj bhzCailiaoCbtj = bhzCailiaoCbtjService.getById(id);
		if(bhzCailiaoCbtj==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzCailiaoCbtj);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzCailiaoCbtj
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzCailiaoCbtj bhzCailiaoCbtj) {
        return super.exportXls(request, bhzCailiaoCbtj, BhzCailiaoCbtj.class, "bhz_cailiao_cbtj");
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
        return super.importExcel(request, response, BhzCailiaoCbtj.class);
    }

}
