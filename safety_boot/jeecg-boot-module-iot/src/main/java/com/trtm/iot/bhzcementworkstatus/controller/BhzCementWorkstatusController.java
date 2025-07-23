package com.trtm.iot.bhzcementworkstatus.controller;

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
import com.trtm.iot.bhzcementworkstatus.entity.BhzCementWorkstatus;
import com.trtm.iot.bhzcementworkstatus.service.IBhzCementWorkstatusService;

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
 * @Description: 砼拌合站工作状态表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-20
 * @Version: V1.0
 */
@Api(tags="砼拌合站工作状态表信息")
@RestController
@RequestMapping("/bhzcementworkstatus/bhzCementWorkstatus")
@Slf4j
public class BhzCementWorkstatusController extends JeecgController<BhzCementWorkstatus, IBhzCementWorkstatusService> {
	@Autowired
	private IBhzCementWorkstatusService bhzCementWorkstatusService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param bhzCementWorkstatus
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "砼拌合站工作状态表信息-分页列表查询")
	@ApiOperation(value="砼拌合站工作状态表信息-分页列表查询", notes="砼拌合站工作状态表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzCementWorkstatus bhzCementWorkstatus,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if(bhzCementWorkstatus.getShebeiNo()==null){
			if(!"null".equals(shebei)){
				bhzCementWorkstatus.setShebeiNo(shebei);
			}else {
				bhzCementWorkstatus.setShebeiNo("空");
			}
		}
		QueryWrapper<BhzCementWorkstatus> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementWorkstatus, req.getParameterMap());
		Page<BhzCementWorkstatus> page = new Page<BhzCementWorkstatus>(pageNo, pageSize);
		IPage<BhzCementWorkstatus> pageList = bhzCementWorkstatusService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param bhzCementWorkstatus
	 * @return
	 */
	@AutoLog(value = "砼拌合站工作状态表信息-添加")
	@ApiOperation(value="砼拌合站工作状态表信息-添加", notes="砼拌合站工作状态表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzCementWorkstatus bhzCementWorkstatus) {
		bhzCementWorkstatusService.save(bhzCementWorkstatus);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param bhzCementWorkstatus
	 * @return
	 */
	@AutoLog(value = "砼拌合站工作状态表信息-编辑")
	@ApiOperation(value="砼拌合站工作状态表信息-编辑", notes="砼拌合站工作状态表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzCementWorkstatus bhzCementWorkstatus) {
		bhzCementWorkstatusService.updateById(bhzCementWorkstatus);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "砼拌合站工作状态表信息-通过id删除")
	@ApiOperation(value="砼拌合站工作状态表信息-通过id删除", notes="砼拌合站工作状态表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzCementWorkstatusService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "砼拌合站工作状态表信息-批量删除")
	@ApiOperation(value="砼拌合站工作状态表信息-批量删除", notes="砼拌合站工作状态表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzCementWorkstatusService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "砼拌合站工作状态表信息-通过id查询")
	@ApiOperation(value="砼拌合站工作状态表信息-通过id查询", notes="砼拌合站工作状态表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzCementWorkstatus bhzCementWorkstatus = bhzCementWorkstatusService.getById(id);
		if(bhzCementWorkstatus==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzCementWorkstatus);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzCementWorkstatus
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzCementWorkstatus bhzCementWorkstatus) {
        return super.exportXls(request, bhzCementWorkstatus, BhzCementWorkstatus.class, "砼拌合站工作状态表信息");
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
        return super.importExcel(request, response, BhzCementWorkstatus.class);
    }

}
