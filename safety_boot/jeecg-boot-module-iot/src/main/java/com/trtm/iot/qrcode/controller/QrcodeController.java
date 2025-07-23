package com.trtm.iot.qrcode.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.qrcode.entity.Qrcode;
import com.trtm.iot.qrcode.service.IQrcodeService;

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
 * @Description: 二维码数据表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-10
 * @Version: V1.0
 */
@Api(tags="二维码数据表信息")
@RestController
@RequestMapping("/qrcode/qrcode")
@Slf4j
public class QrcodeController extends JeecgController<Qrcode, IQrcodeService> {
	@Autowired
	private IQrcodeService qrcodeService;
	 @Autowired
	 private ISysConfigService sysConfigService;//定时任务事务层
	
	/**
	 * 分页列表查询
	 *
	 * @param qrcode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "二维码数据表信息-分页列表查询")
	@ApiOperation(value="二维码数据表信息-分页列表查询", notes="二维码数据表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Qrcode qrcode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Qrcode> queryWrapper = QueryGenerator.initQueryWrapper(qrcode, req.getParameterMap());
		Page<Qrcode> page = new Page<Qrcode>(pageNo, pageSize);
		IPage<Qrcode> pageList = qrcodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "二维码数据表信息-分页列表查询")
	 @ApiOperation(value="二维码数据表信息-分页列表查询", notes="二维码数据表信息-分页列表查询")
	 @GetMapping(value = "/getQrcod")
	 public Result<?> getQrcod(Qrcode qrcode, @RequestParam(name="len", defaultValue="1") Integer len,
									HttpServletRequest req) {

		 //根据定时任务配置表获取一条配置信息
		 SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(1001);
		 Integer id = selectsysconfigone.getCurid();
		 Integer minNumber = Integer.valueOf(selectsysconfigone.getIsworking());
		 Integer maxNumber = minNumber+len;
		 if(maxNumber>200){
			 id = id+1;
			 minNumber = 0;
			 maxNumber = len;
		 }
		 String datas ="";
		 for(int i= 1 ; i <= len ; i ++){
			 Qrcode one = qrcodeService.getQRlistByidN(id,minNumber+i,minNumber);
			 if(ObjectUtil.isNotNull(one)){
				 datas = one.getId()+"-"+one.getNumber()+"#"+one.getUuid() +";"+datas;
			 }else{
				 Result.error("未查询到资源："+id+"-"+maxNumber);
			 }
		 }
		 datas = datas.substring(0,datas.length()-1);
		 sysConfigService.updateSysConfigs(1001,id,maxNumber);
		 return Result.OKs(datas);
	 }
	
	/**
	 *   添加
	 *
	 * @param qrcode
	 * @return
	 */
	@AutoLog(value = "二维码数据表信息-添加")
	@ApiOperation(value="二维码数据表信息-添加", notes="二维码数据表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Qrcode qrcode) {
		qrcodeService.save(qrcode);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param qrcode
	 * @return
	 */
	@AutoLog(value = "二维码数据表信息-编辑")
	@ApiOperation(value="二维码数据表信息-编辑", notes="二维码数据表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Qrcode qrcode) {
		qrcodeService.updateById(qrcode);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "二维码数据表信息-通过id删除")
	@ApiOperation(value="二维码数据表信息-通过id删除", notes="二维码数据表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		qrcodeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "二维码数据表信息-批量删除")
	@ApiOperation(value="二维码数据表信息-批量删除", notes="二维码数据表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.qrcodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "二维码数据表信息-通过id查询")
	@ApiOperation(value="二维码数据表信息-通过id查询", notes="二维码数据表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Qrcode qrcode = qrcodeService.getById(id);
		if(qrcode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(qrcode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param qrcode
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Qrcode qrcode) {
        return super.exportXls(request, qrcode, Qrcode.class, "二维码数据表信息");
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
        return super.importExcel(request, response, Qrcode.class);
    }

}
