package com.trtm.iot.dianzhiweilan.controller;

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
import com.trtm.iot.dianzhiweilan.entity.Dianzhiweilan;
import com.trtm.iot.dianzhiweilan.service.IDianzhiweilanService;

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
 * @Description: 新电子围栏表
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
@Api(tags="新电子围栏表")
@RestController
@RequestMapping("/dianzhiweilan/dianzhiweilan")
@Slf4j
public class DianzhiweilanController extends JeecgController<Dianzhiweilan, IDianzhiweilanService> {
	@Autowired
	private IDianzhiweilanService dianzhiweilanService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param dianzhiweilan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "新电子围栏表-分页列表查询")
	@ApiOperation(value="新电子围栏表-分页列表查询", notes="新电子围栏表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Dianzhiweilan dianzhiweilan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (dianzhiweilan.getShebeibianhao() == null) {
			if (!"null".equals(shebei)) {
				dianzhiweilan.setShebeibianhao(shebei);
			}else {
				dianzhiweilan.setShebeibianhao("空");
			}
		}
		QueryWrapper<Dianzhiweilan> queryWrapper = QueryGenerator.initQueryWrapper(dianzhiweilan, req.getParameterMap());
		Page<Dianzhiweilan> page = new Page<Dianzhiweilan>(pageNo, pageSize);
		IPage<Dianzhiweilan> pageList = dianzhiweilanService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 2023.09.05
	  *
	  * @param dianzhiweilan
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "新电子围栏表-分页列表查询")
	 @ApiOperation(value="新电子围栏表-分页列表查询", notes="新电子围栏表-分页列表查询")
	 @GetMapping(value = "/listxsr")
	 public Result<?> queryPageListxsr(Dianzhiweilan dianzhiweilan,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (dianzhiweilan.getShebeibianhao() == null) {
			 if (!"null".equals(shebei)) {
				 dianzhiweilan.setShebeibianhao(shebei);
			 }else {
				 dianzhiweilan.setShebeibianhao("空");
			 }
		 }
		 QueryWrapper<Dianzhiweilan> queryWrapper = QueryGenerator.initQueryWrapper(dianzhiweilan, req.getParameterMap());
		 List<Dianzhiweilan> list = dianzhiweilanService.list(queryWrapper);
		 return Result.OK(list);
	 }
	 /**
	  * 分页列表查询
	  *
	  * @param dianzhiweilan
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "新电子围栏表-分页列表查询")
	 @ApiOperation(value="新电子围栏表-分页列表查询", notes="新电子围栏表-分页列表查询")
	 @GetMapping(value = "/listwl")
	 public Result<?> queryPageListwl(Dianzhiweilan dianzhiweilan,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (dianzhiweilan.getShebeibianhao() == null) {
			 if (!"null".equals(shebei)) {
				 dianzhiweilan.setShebeibianhao(shebei);
			 }else {
				 dianzhiweilan.setShebeibianhao("空");
			 }
		 }
		 QueryWrapper<Dianzhiweilan> queryWrapper = QueryGenerator.initQueryWrapper(dianzhiweilan, req.getParameterMap());
		 List<Dianzhiweilan> list = dianzhiweilanService.list(queryWrapper);
		 Dianzhiweilan dianzhiweilan1 = new Dianzhiweilan();
		 if (list.size() > 0){
			 dianzhiweilan1 = list.get(0);
		 }
		 QueryWrapper<Dianzhiweilan> queryWrapper1 = new QueryWrapper<>();
		 queryWrapper1.eq("shebeibianhao",dianzhiweilan1.getShebeibianhao());
		 List<Dianzhiweilan> list1 = dianzhiweilanService.list(queryWrapper1);
		 return Result.OK(list1);
	 }
	/**
	 *   添加
	 *
	 * @param dianzhiweilan
	 * @return
	 */
	@AutoLog(value = "新电子围栏表-添加")
	@ApiOperation(value="新电子围栏表-添加", notes="新电子围栏表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Dianzhiweilan dianzhiweilan) {
		dianzhiweilanService.save(dianzhiweilan);
		return Result.OK("添加成功！");
	}

	 /**
	  *   批量添加
	  *
	  * @param dianzhiweilanlist
	  * @return
	  */
	 @AutoLog(value = "新电子围栏表-添加")
	 @ApiOperation(value="新电子围栏表-添加", notes="新电子围栏表-添加")
	 @PostMapping(value = "/addbatch")
	 public Result<?> addbatch(@RequestBody List<Dianzhiweilan> dianzhiweilanlist) {
		 dianzhiweilanService.saveBatch(dianzhiweilanlist);
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param dianzhiweilan
	 * @return
	 */
	@AutoLog(value = "新电子围栏表-编辑")
	@ApiOperation(value="新电子围栏表-编辑", notes="新电子围栏表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Dianzhiweilan dianzhiweilan) {
		dianzhiweilanService.updateById(dianzhiweilan);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @return
	 */
	@AutoLog(value = "新电子围栏表-通过id删除")
	@ApiOperation(value="新电子围栏表-通过id删除", notes="新电子围栏表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(String shebeibianhao,Integer stutis) {
		QueryWrapper<Dianzhiweilan> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("shebeibianhao",shebeibianhao);
		queryWrapper.eq("stutis",stutis);
		dianzhiweilanService.remove(queryWrapper);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "新电子围栏表-批量删除")
	@ApiOperation(value="新电子围栏表-批量删除", notes="新电子围栏表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dianzhiweilanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "新电子围栏表-通过id查询")
	@ApiOperation(value="新电子围栏表-通过id查询", notes="新电子围栏表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Dianzhiweilan dianzhiweilan = dianzhiweilanService.getById(id);
		if(dianzhiweilan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(dianzhiweilan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dianzhiweilan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Dianzhiweilan dianzhiweilan) {
        return super.exportXls(request, dianzhiweilan, Dianzhiweilan.class, "新电子围栏表");
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
        return super.importExcel(request, response, Dianzhiweilan.class);
    }

}
