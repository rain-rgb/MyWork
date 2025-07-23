package com.trtm.iot.liangjiasheguanli.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.liangjiasheguanli.entity.LiangJiasheguanli;
import com.trtm.iot.liangjiasheguanli.service.ILiangJiasheguanliService;

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
 * @Description: 预支梁架设管理表
 * @Author: jeecg-boot
 * @Date:   2023-07-05
 * @Version: V1.0
 */
@Api(tags="预支梁架设管理表")
@RestController
@RequestMapping("/liangjiasheguanli/liangJiasheguanli")
@Slf4j
public class LiangJiasheguanliController extends JeecgController<LiangJiasheguanli, ILiangJiasheguanliService> {
	@Autowired
	private ILiangJiasheguanliService liangJiasheguanliService;
	
	/**
	 * 分页列表查询
	 *
	 * @param liangJiasheguanli
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "预支梁架设管理表-分页列表查询")
	@ApiOperation(value="预支梁架设管理表-分页列表查询", notes="预支梁架设管理表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(LiangJiasheguanli liangJiasheguanli,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LiangJiasheguanli> queryWrapper = QueryGenerator.initQueryWrapper(liangJiasheguanli, req.getParameterMap());
		Page<LiangJiasheguanli> page = new Page<LiangJiasheguanli>(pageNo, pageSize);
		IPage<LiangJiasheguanli> pageList = liangJiasheguanliService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  *   添加
	  *
	  * @param liangJiasheguanli
	  * @return
	  */
	 @AutoLog(value = "预支梁架设管理表-添加")
	 @ApiOperation(value="预支梁架设管理表-添加", notes="预支梁架设管理表-添加")
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody LiangJiasheguanli liangJiasheguanli) {
		 liangJiasheguanliService.save(liangJiasheguanli);
		 return Result.OK("添加成功！");
	 }

	/**
	 *   添加
	 *
	 * @param liangJiasheguanli
	 * @return
	 */
	@AutoLog(value = "预支梁架设管理表-添加")
	@ApiOperation(value="预支梁架设管理表-添加", notes="预支梁架设管理表-添加")
	@PostMapping(value = "/addzk")
	public Result<?> addzk(@RequestBody LiangJiasheguanli liangJiasheguanli) {
		if (liangJiasheguanli.getCode()!=null){
			QueryWrapper<LiangJiasheguanli> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("code",liangJiasheguanli.getCode());
			LiangJiasheguanli one = liangJiasheguanliService.getOne(queryWrapper);
			if(one != null){
				one.setJiashestutis(liangJiasheguanli.getJiashestutis());
				one.setJihuaTime(liangJiasheguanli.getJihuaTime());
				one.setShijiTime(liangJiasheguanli.getShijiTime());
				one.setJindustutis(liangJiasheguanli.getJindustutis());
				one.setJiashebanzu(liangJiasheguanli.getJiashebanzu());
				one.setFuzheren(liangJiasheguanli.getFuzheren());
				liangJiasheguanliService.updateById(one);
			}else {
				return Result.error("添加失败！");
			}
		}
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param liangJiasheguanli
	 * @return
	 */
	@AutoLog(value = "预支梁架设管理表-编辑")
	@ApiOperation(value="预支梁架设管理表-编辑", notes="预支梁架设管理表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody LiangJiasheguanli liangJiasheguanli) {
		liangJiasheguanliService.updateById(liangJiasheguanli);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "预支梁架设管理表-通过id删除")
	@ApiOperation(value="预支梁架设管理表-通过id删除", notes="预支梁架设管理表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		liangJiasheguanliService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "预支梁架设管理表-批量删除")
	@ApiOperation(value="预支梁架设管理表-批量删除", notes="预支梁架设管理表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.liangJiasheguanliService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "预支梁架设管理表-通过id查询")
	@ApiOperation(value="预支梁架设管理表-通过id查询", notes="预支梁架设管理表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		LiangJiasheguanli liangJiasheguanli = liangJiasheguanliService.getById(id);
		if(liangJiasheguanli==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(liangJiasheguanli);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param liangJiasheguanli
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LiangJiasheguanli liangJiasheguanli) {
        return super.exportXls(request, liangJiasheguanli, LiangJiasheguanli.class, "预支梁架设管理表");
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
        return super.importExcel(request, response, LiangJiasheguanli.class);
    }

}
