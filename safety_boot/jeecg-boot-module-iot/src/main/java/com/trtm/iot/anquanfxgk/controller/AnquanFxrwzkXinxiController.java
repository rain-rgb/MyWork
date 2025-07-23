package com.trtm.iot.anquanfxgk.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkBase;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.anquanfxgk.entity.AnquanFxrwzkXinxi;
import com.trtm.iot.anquanfxgk.service.IAnquanFxrwzkXinxiService;

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
 * @Description: anquan_fxrwzk_xinxi
 * @Author: jeecg-boot
 * @Date:   2024-08-08
 * @Version: V1.0
 */
@Api(tags="anquan_fxrwzk_xinxi")
@RestController
@RequestMapping("/anquanfxgk/anquanFxrwzkXinxi")
@Slf4j
public class AnquanFxrwzkXinxiController extends JeecgController<AnquanFxrwzkXinxi, IAnquanFxrwzkXinxiService> {
	@Autowired
	private IAnquanFxrwzkXinxiService anquanFxrwzkXinxiService;
	
	/**
	 * 分页列表查询
	 *
	 * @param anquanFxrwzkXinxi
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "anquan_fxrwzk_xinxi-分页列表查询")
	@ApiOperation(value="anquan_fxrwzk_xinxi-分页列表查询", notes="anquan_fxrwzk_xinxi-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AnquanFxrwzkXinxi anquanFxrwzkXinxi,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		anquanFxrwzkXinxi.setSysOrgCode(loginUser.getOrgCode()+"*");
		QueryWrapper<AnquanFxrwzkXinxi> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxrwzkXinxi, req.getParameterMap());
		Page<AnquanFxrwzkXinxi> page = new Page<AnquanFxrwzkXinxi>(pageNo, pageSize);
		IPage<AnquanFxrwzkXinxi> pageList = anquanFxrwzkXinxiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param anquanFxrwzkXinxi
	 * @return
	 */
	@AutoLog(value = "anquan_fxrwzk_xinxi-添加")
	@ApiOperation(value="anquan_fxrwzk_xinxi-添加", notes="anquan_fxrwzk_xinxi-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AnquanFxrwzkXinxi anquanFxrwzkXinxi) {
		anquanFxrwzkXinxiService.save(anquanFxrwzkXinxi);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param anquanFxrwzkXinxi
	 * @return
	 */
	@AutoLog(value = "anquan_fxrwzk_xinxi-编辑")
	@ApiOperation(value="anquan_fxrwzk_xinxi-编辑", notes="anquan_fxrwzk_xinxi-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AnquanFxrwzkXinxi anquanFxrwzkXinxi) {
		anquanFxrwzkXinxiService.updateById(anquanFxrwzkXinxi);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxrwzk_xinxi-通过id删除")
	@ApiOperation(value="anquan_fxrwzk_xinxi-通过id删除", notes="anquan_fxrwzk_xinxi-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		anquanFxrwzkXinxiService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "anquan_fxrwzk_xinxi-批量删除")
	@ApiOperation(value="anquan_fxrwzk_xinxi-批量删除", notes="anquan_fxrwzk_xinxi-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.anquanFxrwzkXinxiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxrwzk_xinxi-通过id查询")
	@ApiOperation(value="anquan_fxrwzk_xinxi-通过id查询", notes="anquan_fxrwzk_xinxi-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AnquanFxrwzkXinxi anquanFxrwzkXinxi = anquanFxrwzkXinxiService.getById(id);
		if(anquanFxrwzkXinxi==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(anquanFxrwzkXinxi);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param anquanFxrwzkXinxi
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AnquanFxrwzkXinxi anquanFxrwzkXinxi) {
        return super.exportXls(request, anquanFxrwzkXinxi, AnquanFxrwzkXinxi.class, "（单位）建设项目安全风险管控任务展开表");
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
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			params.setHeadRows(1);
			params.setStartRows(1);
			params.setNeedSave(true);
			try {
				List<AnquanFxrwzkXinxi> list = ExcelImportUtil.importExcel(file.getInputStream(), AnquanFxrwzkXinxi.class, params);
				for (AnquanFxrwzkXinxi i:list){
					String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
					i.setBaseGuid(uuid);
					i.setSysOrgCode(loginUser.getOrgCode());
				}
				//update-begin-author:taoyan date:20190528 for:批量插入数据
				long start = System.currentTimeMillis();
				anquanFxrwzkXinxiService.saveBatch(list);
				//400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
				//1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
				log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				//update-end-author:taoyan date:20190528 for:批量插入数据
				return Result.ok("文件导入成功！数据行数：" + list.size());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败:" + e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.error("文件导入失败！");
//        return super.importExcel(request, response, AnquanFxrwzkXinxi.class);
    }

	 /**
	  * 导出excelApi
	  *
	  * @param request
	  * @param AnquanFxrwzkXinxi
	  */
	 @AutoLog(value = "anquan_fxrwzk_xinxi导出Api")
	 @ApiOperation(value="anquan_fxrwzk_xinxi导出Api", notes="anquan_fxrwzk_xinxi导出Api")
	 @GetMapping(value = "/exportXlsApi")
	 public Result<?> exportXlsApi(HttpServletRequest request,AnquanFxrwzkXinxi anquanFxrwzkXinxi) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 anquanFxrwzkXinxi.setSysOrgCode(loginUser.getOrgCode());
		 LambdaQueryWrapper<AnquanFxrwzkXinxi> queryWrapper =new LambdaQueryWrapper<>();
		 queryWrapper
				 .likeRight(anquanFxrwzkXinxi.getSysOrgCode()!=null,AnquanFxrwzkXinxi::getSysOrgCode,anquanFxrwzkXinxi.getSysOrgCode());

		 List<AnquanFxrwzkXinxi> pageList = anquanFxrwzkXinxiService.list(queryWrapper);

//		for(AnquanFxfjgkBase record :pageList){
//			record.setDetailList(anquanFxfjgkBaseDetailService.getDetailList(record.getBaseGuid()));
//		}
		 return Result.OKs(pageList);
	 }


}
