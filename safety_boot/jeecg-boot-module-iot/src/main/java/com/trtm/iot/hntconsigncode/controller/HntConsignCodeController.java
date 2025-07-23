package com.trtm.iot.hntconsigncode.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.hntconsign.entity.HntConsign;
import com.trtm.iot.hntconsign.service.IHntConsignService;
import com.trtm.sy.sydpssysample.entity.SyDpsSySample;
import com.trtm.sy.sydpssysample.service.ISyDpsSySampleService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hntconsigncode.entity.HntConsignCode;
import com.trtm.iot.hntconsigncode.service.IHntConsignCodeService;

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
 * @Description: 混凝土见证取样二维码表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
@Api(tags="混凝土见证取样二维码表信息")
@RestController
@RequestMapping("/hntconsigncode/hntConsignCode")
@Slf4j
public class HntConsignCodeController extends JeecgController<HntConsignCode, IHntConsignCodeService> {
	@Autowired
	private IHntConsignCodeService hntConsignCodeService;
	 @Autowired
	 private IHntConsignService hntConsignService;

	 @Autowired
	 private ISyDpsSySampleService syDpsSySampleService;

	/**
	 * 分页列表查询
	 *
	 * @param hntConsignCode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样二维码表信息-分页列表查询")
	@ApiOperation(value="混凝土见证取样二维码表信息-分页列表查询", notes="混凝土见证取样二维码表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HntConsignCode hntConsignCode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HntConsignCode> queryWrapper = QueryGenerator.initQueryWrapper(hntConsignCode, req.getParameterMap());
		Page<HntConsignCode> page = new Page<HntConsignCode>(pageNo, pageSize);
		IPage<HntConsignCode> pageList = hntConsignCodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "混凝土见证取样二维码表信息-查询")
	 @ApiOperation(value="混凝土见证取样二维码表信息-查询", notes="混凝土见证取样二维码表信息-查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(HntConsignCode hntConsignCode, HttpServletRequest req) {
		 QueryWrapper<HntConsignCode> queryWrapper = QueryGenerator.initQueryWrapper(hntConsignCode, req.getParameterMap());
		 List<HntConsignCode> pageList = hntConsignCodeService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param hntConsignCode
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样二维码表信息-添加")
	@ApiOperation(value="混凝土见证取样二维码表信息-添加", notes="混凝土见证取样二维码表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HntConsignCode hntConsignCode) {
		hntConsignCodeService.save(hntConsignCode);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hntConsignCode
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样二维码表信息-编辑")
	@ApiOperation(value="混凝土见证取样二维码表信息-编辑", notes="混凝土见证取样二维码表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HntConsignCode hntConsignCode) {
		hntConsignCodeService.updateById(hntConsignCode);
		return Result.OK("编辑成功!");
	}
	 /**
	  *  养护上架 （验证货架）
	  *
	  * @param hntConsignCode
	  * @return
	  */
	 @AutoLog(value = "混凝土见证取样二维码表信息-验证货架")
	 @ApiOperation(value="混凝土见证取样二维码表信息-验证货架", notes="混凝土见证取样二维码表信息-验证货架")
	 @PutMapping(value = "/upyanzhenghuojia")
	 public Result<?> upyanzhenghuojia(@RequestBody HntConsignCode hntConsignCode) {
		 String huojianname = hntConsignCode.getHuojianname();
		 String huojiacenshu = hntConsignCode.getHuojiacenshu();
		 String no = hntConsignCode.getNo();
		 LambdaQueryWrapper<HntConsignCode> lambdaQueryWrapper=new LambdaQueryWrapper<>();
		 lambdaQueryWrapper.eq(HntConsignCode::getHuojianname,huojianname);
		 lambdaQueryWrapper.eq(HntConsignCode::getHuojiacenshu,huojiacenshu);
		 lambdaQueryWrapper.eq(HntConsignCode::getNo,no);
		 lambdaQueryWrapper.eq(HntConsignCode::getCstatus,1);
		 HntConsignCode one = hntConsignCodeService.getOne(lambdaQueryWrapper);
		 if(one!=null){
			 return Result.error("货架上已经存在");
		 }else{
			 return Result.OK("可以上架！");
		 }
	 }



	 /**
	  *  养护上架
	  *
	  * @param hntConsignCode
	  * @return
	  */
	 @AutoLog(value = "混凝土见证取样二维码表信息-养护上架")
	 @ApiOperation(value="混凝土见证取样二维码表信息-养护上架", notes="混凝土见证取样二维码表信息-养护上架")
	 @PutMapping(value = "/edityhup")
	 public Result<?> edityhup(@RequestBody HntConsignCode hntConsignCode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String codeno = hntConsignCode.getCodeno();
		 String code = hntConsignCode.getCode();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 if(codeno!=null){
			 LambdaQueryWrapper<HntConsignCode> lambdaQueryWrapper=new LambdaQueryWrapper();
			 lambdaQueryWrapper.eq(HntConsignCode::getCodeno,codeno);
			 HntConsignCode one = hntConsignCodeService.getOne(lambdaQueryWrapper);
			 if(one!=null){
				 Integer id = one.getId();
				 HntConsignCode hntConsignCode1=new HntConsignCode();
				 hntConsignCode1.setId(id);
				 hntConsignCode1.setCstatus(1);
				 hntConsignCode1.setJyhdate(sdf.format(new Date()));
				 hntConsignCode1.setJyhr(loginUser.getUsername());
				 hntConsignCode1.setHuojianname(hntConsignCode.getHuojianname());
				 hntConsignCode1.setHuojiacenshu(hntConsignCode.getHuojiacenshu());
				 hntConsignCode1.setNo(hntConsignCode.getNo());
				 boolean b = hntConsignCodeService.updateById(hntConsignCode1);
				 if(b){
					 return Result.OK("上架成功！");
				 }
			 }else{
				 return Result.error("未找到该二维码的见证取样信息！");
			 }
		 }
		 if(code!=null){
			 LambdaQueryWrapper<HntConsignCode> lambdaQueryWrapper=new LambdaQueryWrapper();
			 lambdaQueryWrapper.eq(HntConsignCode::getCode,code);
			 HntConsignCode one = hntConsignCodeService.getOne(lambdaQueryWrapper);
			 if(one!=null){
				 Integer id = one.getId();
				 HntConsignCode hntConsignCode1=new HntConsignCode();
				 hntConsignCode1.setId(id);
				 hntConsignCode1.setCstatus(1);
				 hntConsignCode1.setJyhdate(sdf.format(new Date()));
				 hntConsignCode1.setJyhr(loginUser.getUsername());
				 hntConsignCode1.setHuojianname(hntConsignCode.getHuojianname());
				 hntConsignCode1.setHuojiacenshu(hntConsignCode.getHuojiacenshu());
				 hntConsignCode1.setNo(hntConsignCode.getNo());
				 boolean b = hntConsignCodeService.updateById(hntConsignCode1);
				 if(b){
					 return Result.OK("上架成功！");
				 }
			 }else{
				 return Result.error("未找到该二维码的见证取样信息！");
			 }
		 }
		 return Result.error("上架失败!");
	 }

	 /**
	  *  养护下架
	  *
	  * @param hntConsignCode
	  * @return
	  */
	 @AutoLog(value = "混凝土见证取样二维码表信息-养护下架")
	 @ApiOperation(value="混凝土见证取样二维码表信息-养护下架", notes="混凝土见证取样二维码表信息-养护下架")
	 @PutMapping(value = "/edityhdown")
	 public Result<?> edityhdown(@RequestBody HntConsignCode hntConsignCode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String codeno = hntConsignCode.getCodeno();
		 String code = hntConsignCode.getCode();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 if(codeno!=null){
			 LambdaQueryWrapper<HntConsignCode> lambdaQueryWrapper=new LambdaQueryWrapper();
			 lambdaQueryWrapper.eq(HntConsignCode::getCodeno,codeno);
			 lambdaQueryWrapper.eq(HntConsignCode::getCstatus,1);
			 HntConsignCode one = hntConsignCodeService.getOne(lambdaQueryWrapper);
			 if(one!=null){
				 Integer id = one.getId();
				 HntConsignCode hntConsignCode1=new HntConsignCode();
				 hntConsignCode1.setId(id);
				 hntConsignCode1.setCstatus(2);
				 hntConsignCode1.setCyhdate(sdf.format(new Date()));
				 hntConsignCode1.setCyhr(loginUser.getUsername());
				 boolean b = hntConsignCodeService.updateById(hntConsignCode1);
				 if(b){
					 insertSample(one.getWtbh());
					 return Result.OK("下架成功！");
				 }
			 }else{
				 return Result.error("未找到该二维码的见证取样信息！");
			 }
		 }
		 if(code!=null){
			 LambdaQueryWrapper<HntConsignCode> lambdaQueryWrapper=new LambdaQueryWrapper();
			 lambdaQueryWrapper.eq(HntConsignCode::getCode,code);
			 lambdaQueryWrapper.eq(HntConsignCode::getCstatus,1);
			 HntConsignCode one = hntConsignCodeService.getOne(lambdaQueryWrapper);
			 if(one!=null){
				 Integer id = one.getId();
				 HntConsignCode hntConsignCode1=new HntConsignCode();
				 hntConsignCode1.setId(id);
				 hntConsignCode1.setCstatus(2);
				 hntConsignCode1.setCyhdate(sdf.format(new Date()));
				 hntConsignCode1.setCyhr(loginUser.getUsername());
				 boolean b = hntConsignCodeService.updateById(hntConsignCode1);
				 if(b){
					 // 下架成功后将数据插入至sample表
					 insertSample(one.getWtbh());
					 return Result.OK("下架成功！");
				 }
			 }else{
				 return Result.error("未找到该二维码的见证取样信息！");
			 }
		 }
		 return Result.error("下架失败!");
	 }

	 private boolean  insertSample(String wtbh){

		 SyDpsSySample sampleByWtbh = syDpsSySampleService.getSampleByWtbh(wtbh);
		 if(sampleByWtbh != null){
			 QueryWrapper<HntConsign> queryWrapper = new QueryWrapper<>();
			 queryWrapper.eq("wtbh",wtbh);
			 HntConsign one = hntConsignService.getOne(queryWrapper);
			 HashMap sample = new HashMap<>();
			 sample.put("sampleName",one.getYpmc());
//		 sample.put("sampleNo",one.get自动生成);
			 sample.put("sampleGcbw",one.getSgbw());
			 sample.put("sampleDescribe",one.getYpms());
			 sample.put("sampleDate",one.getQyrq());
			 sample.put("sampleChanDi",one.getCdcm());
			 sample.put("sampleQuYangDiDian",one.getQydd());
			 sample.put("sampleQuYangRen",one.getQyrq());
			 sample.put("sampleQiangDuDengJi",one.getQddj());
			 sample.put("sampleJiaoBanFangShi",one.getJbfs());
			 sample.put("sampleHunNingTuZhongLei",one.getHntzl());
			 sample.put("orgCode",one.getOrgcode());
			 sample.put("sampleLingQi",one.getYplq());
			 sample.put("titCode",one.getSyxm());
			 sample.put("sampleShiYanZuShu",one.getSysuliang());
			 sample.put("phbtzdid",one.getPhbtzdid());
			 sample.put("phbtzdbh",one.getPhbtzdbh());
			 sample.put("wtbh",one.getWtbh());
			 sample.put("shikuaichicun",one.getChicun());
			 sample.put("titType",3);// 试块默认为3
			 syDpsSySampleService.add(sample);
		 }
		 return  true;
	 }
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样二维码表信息-通过id删除")
	@ApiOperation(value="混凝土见证取样二维码表信息-通过id删除", notes="混凝土见证取样二维码表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hntConsignCodeService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样二维码表信息-批量删除")
	@ApiOperation(value="混凝土见证取样二维码表信息-批量删除", notes="混凝土见证取样二维码表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hntConsignCodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土见证取样二维码表信息-通过id查询")
	@ApiOperation(value="混凝土见证取样二维码表信息-通过id查询", notes="混凝土见证取样二维码表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HntConsignCode hntConsignCode = hntConsignCodeService.getById(id);
		if(hntConsignCode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hntConsignCode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hntConsignCode
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HntConsignCode hntConsignCode) {
        return super.exportXls(request, hntConsignCode, HntConsignCode.class, "混凝土见证取样二维码表信息");
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
        return super.importExcel(request, response, HntConsignCode.class);
    }

}
