package com.trtm.iot.hclijinshibie.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.hctfysworkareaoverhandler.entity.HcTfysworkareaOverHandler;
import com.trtm.iot.hctfysworkareaoverhandler.service.IHcTfysworkareaOverHandlerService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hclijinshibie.entity.HcLijinshibieHistroy;
import com.trtm.iot.hclijinshibie.service.IHcLijinshibieHistroyService;

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
 * @Description: hc_lijinshibie_histroy
 * @Author: jeecg-boot
 * @Date:   2024-11-25
 * @Version: V1.0
 */
@Api(tags="hc_lijinshibie_histroy")
@RestController
@RequestMapping("/hclijinshibie/hcLijinshibieHistroy")
@Slf4j
public class HcLijinshibieHistroyController extends JeecgController<HcLijinshibieHistroy, IHcLijinshibieHistroyService> {
	@Autowired
	private IHcLijinshibieHistroyService hcLijinshibieHistroyService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IHcTfysworkareaOverHandlerService hcTfysworkareaOverHandlerService;

	/**
	 * 分页列表查询
	 *
	 * @param hcLijinshibieHistroy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_histroy-分页列表查询")
	@ApiOperation(value="hc_lijinshibie_histroy-分页列表查询", notes="hc_lijinshibie_histroy-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcLijinshibieHistroy hcLijinshibieHistroy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (hcLijinshibieHistroy.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				hcLijinshibieHistroy.setShebeino(shebei);
			} else {
				hcLijinshibieHistroy.setShebeino("空");
			}
		}
		QueryWrapper<HcLijinshibieHistroy> queryWrapper = QueryGenerator.initQueryWrapper(hcLijinshibieHistroy, req.getParameterMap());
		Page<HcLijinshibieHistroy> page = new Page<HcLijinshibieHistroy>(pageNo, pageSize);
		IPage<HcLijinshibieHistroy> pageList = hcLijinshibieHistroyService.page(page, queryWrapper);
		for(HcLijinshibieHistroy item : pageList.getRecords() ){
			HcTfysworkareaOverHandler selectone = hcTfysworkareaOverHandlerService.selectone("LJSB" + item.getId().toString());
			if(ObjectUtil.isNotNull(selectone)){
				item.setHandle(selectone);
			}else{
				item.setHandle(new HcTfysworkareaOverHandler());
			}
		}
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param hcLijinshibieHistroy
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_histroy-添加")
	@ApiOperation(value="hc_lijinshibie_histroy-添加", notes="hc_lijinshibie_histroy-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcLijinshibieHistroy hcLijinshibieHistroy) {
		hcLijinshibieHistroyService.save(hcLijinshibieHistroy);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hcLijinshibieHistroy
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_histroy-编辑")
	@ApiOperation(value="hc_lijinshibie_histroy-编辑", notes="hc_lijinshibie_histroy-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcLijinshibieHistroy hcLijinshibieHistroy) {
		hcLijinshibieHistroyService.updateById(hcLijinshibieHistroy);
		return Result.OK("编辑成功!");
	}

	 /**
	  *  编辑
	  *
	  * @param hcLijinshibieHistroy
	  * @return
	  */
	 @AutoLog(value = "hc_lijinshibie_histroy-编辑")
	 @ApiOperation(value="hc_lijinshibie_histroy-编辑", notes="hc_lijinshibie_histroy-编辑")
	 @PutMapping(value = "/editHandle")
	 public Result<?> editHandle(@RequestBody HcLijinshibieHistroy hcLijinshibieHistroy) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 if(ObjectUtil.isNotNull(hcLijinshibieHistroy.getHandle())){
			 if(hcLijinshibieHistroy.getHandle().getOverproofStatus() == 10){
				 hcLijinshibieHistroy.getHandle().setHandlePerson(loginUser.getRealname());
				 hcLijinshibieHistroy.getHandle().setHandleTime( new Date());
			 } else if (hcLijinshibieHistroy.getHandle().getOverproofStatus() == 30 || hcLijinshibieHistroy.getHandle().getOverproofStatus() == 40 ) {
				 hcLijinshibieHistroy.getHandle().setApprovalPerson(loginUser.getRealname());
				 hcLijinshibieHistroy.getHandle().setSupervisorHandleTime( new Date());
			 }else if (hcLijinshibieHistroy.getHandle().getOverproofStatus() == 20 || hcLijinshibieHistroy.getHandle().getOverproofStatus() == 60){
				 hcLijinshibieHistroy.getHandle().setHeadquartersPerson(loginUser.getRealname());
				 hcLijinshibieHistroy.getHandle().setHeadquartersHandleTime( new Date());
			 }
			 hcTfysworkareaOverHandlerService.saveOrUpdate(hcLijinshibieHistroy.getHandle());
		 }
		 hcLijinshibieHistroyService.updateById(hcLijinshibieHistroy);
		 return Result.OK("编辑成功!");
	 }

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_histroy-通过id删除")
	@ApiOperation(value="hc_lijinshibie_histroy-通过id删除", notes="hc_lijinshibie_histroy-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcLijinshibieHistroyService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_histroy-批量删除")
	@ApiOperation(value="hc_lijinshibie_histroy-批量删除", notes="hc_lijinshibie_histroy-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcLijinshibieHistroyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "hc_lijinshibie_histroy-通过id查询")
	@ApiOperation(value="hc_lijinshibie_histroy-通过id查询", notes="hc_lijinshibie_histroy-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcLijinshibieHistroy hcLijinshibieHistroy = hcLijinshibieHistroyService.getById(id);
		if(hcLijinshibieHistroy==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcLijinshibieHistroy);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcLijinshibieHistroy
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcLijinshibieHistroy hcLijinshibieHistroy) {
        return super.exportXls(request, hcLijinshibieHistroy, HcLijinshibieHistroy.class, "hc_lijinshibie_histroy");
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
        return super.importExcel(request, response, HcLijinshibieHistroy.class);
    }

}
