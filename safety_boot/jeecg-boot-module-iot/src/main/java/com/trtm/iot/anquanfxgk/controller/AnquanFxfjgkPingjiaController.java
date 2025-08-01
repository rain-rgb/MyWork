package com.trtm.iot.anquanfxgk.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkPingjiaDetail;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkXuncha;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkPingjiaDetailService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkPingjia;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkPingjiaService;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: anquan_fxfjgk_pingjia
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
@Api(tags="anquan_fxfjgk_pingjia")
@RestController
@RequestMapping("/anquanfxgk/anquanFxfjgkPingjia")
@Slf4j
public class AnquanFxfjgkPingjiaController extends JeecgController<AnquanFxfjgkPingjia, IAnquanFxfjgkPingjiaService> {
	@Autowired
	private IAnquanFxfjgkPingjiaService anquanFxfjgkPingjiaService;
	@Autowired
	private IAnquanFxfjgkPingjiaDetailService detailService;


	/**
	 * 分页列表查询
	 *
	 * @param anquanFxfjgkPingjia
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_pingjia-分页列表查询")
	@ApiOperation(value="anquan_fxfjgk_pingjia-分页列表查询", notes="anquan_fxfjgk_pingjia-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AnquanFxfjgkPingjia anquanFxfjgkPingjia,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AnquanFxfjgkPingjia> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxfjgkPingjia, req.getParameterMap());
		Page<AnquanFxfjgkPingjia> page = new Page<AnquanFxfjgkPingjia>(pageNo, pageSize);
		IPage<AnquanFxfjgkPingjia> pageList = anquanFxfjgkPingjiaService.page(page, queryWrapper);
		for (AnquanFxfjgkPingjia item:page.getRecords()){
            List<AnquanFxfjgkPingjiaDetail> anquanFxfjgkPingjiaDetail = detailService.getAnquanFxfjgkPingjiaDetail(item.getBaseGuid());
            item.setAnquanFxfjgkPingjiaDetails(anquanFxfjgkPingjiaDetail);
        }

		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param anquanFxfjgkPingjia
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_pingjia-添加")
	@ApiOperation(value="anquan_fxfjgk_pingjia-添加", notes="anquan_fxfjgk_pingjia-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AnquanFxfjgkPingjia anquanFxfjgkPingjia) {

		anquanFxfjgkPingjiaService.saveAnquanFxfjgkPingjia(anquanFxfjgkPingjia);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param anquanFxfjgkPingjia
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_pingjia-编辑")
	@ApiOperation(value="anquan_fxfjgk_pingjia-编辑", notes="anquan_fxfjgk_pingjia-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AnquanFxfjgkPingjia anquanFxfjgkPingjia) {
		anquanFxfjgkPingjiaService.updateAnquanFxfjgkPingjia(anquanFxfjgkPingjia);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_pingjia-通过id删除")
	@ApiOperation(value="anquan_fxfjgk_pingjia-通过id删除", notes="anquan_fxfjgk_pingjia-通过id删除")
	@DeleteMapping(value = "/delete")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
	    //删除明细表
        AnquanFxfjgkPingjia byId = anquanFxfjgkPingjiaService.getById(id);
        detailService.deleteAnquanFxfjgkPingjiaDetailByParent(byId.getBaseGuid());
        //删除主表
		anquanFxfjgkPingjiaService.removeById(id);
        return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_pingjia-批量删除")
	@ApiOperation(value="anquan_fxfjgk_pingjia-批量删除", notes="anquan_fxfjgk_pingjia-批量删除")
	@DeleteMapping(value = "/deleteBatch")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
	    //删除明细表数据
        String[] split = ids.split(",");
        List<String> list=new ArrayList<>();
        for (String s : split) {
            list.add(s);
        }
        List<AnquanFxfjgkPingjia> anquanFxfjgkPingjias = anquanFxfjgkPingjiaService.listByIds(list);
        anquanFxfjgkPingjias.forEach(item->detailService.deleteAnquanFxfjgkPingjiaDetailByParent(item.getBaseGuid()));
        //删除主表
        this.anquanFxfjgkPingjiaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_pingjia-通过id查询")
	@ApiOperation(value="anquan_fxfjgk_pingjia-通过id查询", notes="anquan_fxfjgk_pingjia-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AnquanFxfjgkPingjia anquanFxfjgkPingjia = anquanFxfjgkPingjiaService.getById(id);
		if(anquanFxfjgkPingjia==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(anquanFxfjgkPingjia);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param anquanFxfjgkPingjia
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AnquanFxfjgkPingjia anquanFxfjgkPingjia) {
        return super.exportXls(request, anquanFxfjgkPingjia, AnquanFxfjgkPingjia.class, "anquan_fxfjgk_pingjia");
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
        return super.importExcel(request, response, AnquanFxfjgkPingjia.class);
    }

	 /**
	  * 导出excelApi
	  *
	  * @param request
	  * @param anquanFxfjgkBase
	  */
	 @AutoLog(value = "anquan_fxfjgk_pingjia导出Api")
	 @ApiOperation(value="anquan_fxfjgk_pingjia导出Api", notes="anquan_fxfjgk_pingjia导出Api")
	 @GetMapping(value = "/exportXlsApi")
	 public Result<?> exportXlsApi(HttpServletRequest request,AnquanFxfjgkPingjia anquanFxfjgkPingjia) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 anquanFxfjgkPingjia.setSysOrgCode(loginUser.getOrgCode());
		 LambdaQueryWrapper<AnquanFxfjgkPingjia> queryWrapper =new LambdaQueryWrapper<>();
		 queryWrapper.likeRight(anquanFxfjgkPingjia.getSysOrgCode()!=null,AnquanFxfjgkPingjia::getSysOrgCode,anquanFxfjgkPingjia.getSysOrgCode());
		 List<AnquanFxfjgkPingjia> pageList = anquanFxfjgkPingjiaService.list(queryWrapper);
		 for (AnquanFxfjgkPingjia item:pageList){
			 List<AnquanFxfjgkPingjiaDetail> anquanFxfjgkPingjiaDetail = detailService.getAnquanFxfjgkPingjiaDetail(item.getBaseGuid());
			 item.setAnquanFxfjgkPingjiaDetails(anquanFxfjgkPingjiaDetail);
		 }
		 return Result.OKs(pageList);
	 }




}
