package com.trtm.iot.anquanfxgk.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkBaseDetailService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkBase;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkBaseService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: anquan_fxfjgk_base
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Api(tags="anquan_fxfjgk_base")
@RestController
@RequestMapping("/anquanfxgk/anquanFxfjgkBase")
@Slf4j
public class AnquanFxfjgkBaseController extends JeecgController<AnquanFxfjgkBase, IAnquanFxfjgkBaseService> {
	@Autowired
	private IAnquanFxfjgkBaseService anquanFxfjgkBaseService;
	 @Autowired
	 private IAnquanFxfjgkBaseDetailService anquanFxfjgkBaseDetailService;

	/**
	 * 分页列表查询
	 *
	 * @param anquanFxfjgkBase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base-分页列表查询")
	@ApiOperation(value="anquan_fxfjgk_base-分页列表查询", notes="anquan_fxfjgk_base-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AnquanFxfjgkBase anquanFxfjgkBase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		anquanFxfjgkBase.setSysOrgCode(sys_depart_orgcode+"*");
		QueryWrapper<AnquanFxfjgkBase> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxfjgkBase, req.getParameterMap());
		Page<AnquanFxfjgkBase> page = new Page<AnquanFxfjgkBase>(pageNo, pageSize);
		IPage<AnquanFxfjgkBase> pageList = anquanFxfjgkBaseService.page(page, queryWrapper);

//		for(AnquanFxfjgkBase record :pageList.getRecords()){
//			record.setDetailList(anquanFxfjgkBaseDetailService.getDetailList(record.getBaseGuid()));
//		}
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param anquanFxfjgkBase
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base-添加")
	@ApiOperation(value="anquan_fxfjgk_base-添加", notes="anquan_fxfjgk_base-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AnquanFxfjgkBase anquanFxfjgkBase) {

		String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
		anquanFxfjgkBase.setBaseGuid(uuid);
//		if(anquanFxfjgkBase.getDetailList().size()>0){
//			for(AnquanFxfjgkBaseDetail detail : anquanFxfjgkBase.getDetailList()){
//				detail.setBaseGuid(uuid);
//			}
//			anquanFxfjgkBaseDetailService.saveOrUpdateBatch(anquanFxfjgkBase.getDetailList());
//		}
		anquanFxfjgkBaseService.save(anquanFxfjgkBase);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param anquanFxfjgkBase
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base-编辑")
	@ApiOperation(value="anquan_fxfjgk_base-编辑", notes="anquan_fxfjgk_base-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AnquanFxfjgkBase anquanFxfjgkBase) {
//		if(anquanFxfjgkBase.getDetailList().size()>0){
//			anquanFxfjgkBaseDetailService.saveOrUpdateBatch(anquanFxfjgkBase.getDetailList());
//		}
		anquanFxfjgkBaseService.updateById(anquanFxfjgkBase);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base-通过id删除")
	@ApiOperation(value="anquan_fxfjgk_base-通过id删除", notes="anquan_fxfjgk_base-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		anquanFxfjgkBaseService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base-批量删除")
	@ApiOperation(value="anquan_fxfjgk_base-批量删除", notes="anquan_fxfjgk_base-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.anquanFxfjgkBaseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_base-通过id查询")
	@ApiOperation(value="anquan_fxfjgk_base-通过id查询", notes="anquan_fxfjgk_base-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AnquanFxfjgkBase anquanFxfjgkBase = anquanFxfjgkBaseService.getById(id);
		if(anquanFxfjgkBase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(anquanFxfjgkBase);
	}

     /**
      * 审批
      *
      * @param id
      * @return
      */
     @AutoLog(value = "审批")
     @ApiOperation(value="审批", notes="审批")
     @PostMapping(value = "/approval")
     public Result<?> approval(@RequestBody AnquanFxfjgkBase anquan) {
         anquanFxfjgkBaseService.modifyFxfjgkBaseApprovalStatus(anquan);
         return Result.OK("编辑成功!");
     }


    /**
    * 导出excel
    *
    * @param request
    * @param anquanFxfjgkBase
    */
//	@AutoLog(value = "anquan_fxfjgk_base导出")
//	@ApiOperation(value="anquan_fxfjgk_base导出", notes="anquan_fxfjgk_base导出")
    @RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, AnquanFxfjgkBase anquanFxfjgkBase) {
        return super.exportXls(request, anquanFxfjgkBase, AnquanFxfjgkBase.class, "风险分级管控清单表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
//	@AutoLog(value = "anquan_fxfjgk_base导入")
//	@ApiOperation(value="anquan_fxfjgk_base导入", notes="anquan_fxfjgk_base导入")
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
				List<AnquanFxfjgkBase> list = ExcelImportUtil.importExcel(file.getInputStream(), AnquanFxfjgkBase.class, params);

				for (AnquanFxfjgkBase i:list){
					String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
					i.setBaseGuid(uuid);
					i.setSysOrgCode(loginUser.getOrgCode());
				}
				//update-begin-author:taoyan date:20190528 for:批量插入数据
				long start = System.currentTimeMillis();
				anquanFxfjgkBaseService.saveBatch(list);
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
//        return super.importExcel(request, response, AnquanFxfjgkBase.class);
    }


	/**
	 * 导出excelApi
	 *
	 * @param request
	 * @param anquanFxfjgkBase
	 */
	@AutoLog(value = "anquan_fxfjgk_base导出Api")
	@ApiOperation(value="anquan_fxfjgk_base导出Api", notes="anquan_fxfjgk_base导出Api")
	@GetMapping(value = "/exportXlsApi")
	public Result<?> exportXlsApi(HttpServletRequest request,AnquanFxfjgkBase anquanFxfjgkBase) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		anquanFxfjgkBase.setSysOrgCode(loginUser.getOrgCode());
		LambdaQueryWrapper<AnquanFxfjgkBase> queryWrapper =new LambdaQueryWrapper<>();
		queryWrapper.like(anquanFxfjgkBase.getSoDidian()!=null,AnquanFxfjgkBase::getSoDidian,anquanFxfjgkBase.getSoDidian())
					.eq(anquanFxfjgkBase.getRiskLevel()!=null,AnquanFxfjgkBase::getRiskLevel,anquanFxfjgkBase.getRiskLevel())
					.in(anquanFxfjgkBase.getRiskLevel()!=null,AnquanFxfjgkBase::getRiskLevel,anquanFxfjgkBase.getRiskLevel())
				.likeRight(anquanFxfjgkBase.getSysOrgCode()!=null,AnquanFxfjgkBase::getSysOrgCode,anquanFxfjgkBase.getSysOrgCode());

		List<AnquanFxfjgkBase> pageList = anquanFxfjgkBaseService.list(queryWrapper);

//		for(AnquanFxfjgkBase record :pageList){
//			record.setDetailList(anquanFxfjgkBaseDetailService.getDetailList(record.getBaseGuid()));
//		}
		return Result.OKs(pageList);
	}

 }
