package com.trtm.iot.wzgongyingshangman.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.trtm.iot.wzgongyingshangman.service.IWzgongyingshangManService;

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

import static org.jeecg.common.util.DateUtils.getTimestampten;

/**
 * @Description: wzgongyingshang_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
@Api(tags="wzgongyingshang_man")
@RestController
@RequestMapping("/wzgongyingshangman/wzgongyingshangMan")
@Slf4j
public class WzgongyingshangManController extends JeecgController<WzgongyingshangMan, IWzgongyingshangManService> {
	@Autowired
	private IWzgongyingshangManService wzgongyingshangManService;

	@Autowired
	private IWzliaocangService wzliaocangService;

	@Autowired
	 RedisUtil redisUtil;

	 public static final int MACHINE_ID = 1;
	/**
	 * 分页列表查询
	 *
	 * @param wzgongyingshangMan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wzgongyingshang_man-分页列表查询")
	@ApiOperation(value="wzgongyingshang_man-分页列表查询", notes="wzgongyingshang_man-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WzgongyingshangMan wzgongyingshangMan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			wzgongyingshangMan.setSysOrgCode(sys_depart_orgcode + "*");
		}else{
			wzgongyingshangMan.setSysOrgCode(loginUser.getOrgCode() + "*");
		}
		QueryWrapper<WzgongyingshangMan> queryWrapper = QueryGenerator.initQueryWrapper(wzgongyingshangMan, req.getParameterMap());
		Page<WzgongyingshangMan> page = new Page<WzgongyingshangMan>(pageNo, pageSize);
		IPage<WzgongyingshangMan> pageList = wzgongyingshangManService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param wzgongyingshangMan
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "wzgongyingshang_man-分页列表查询")
	 @ApiOperation(value="wzgongyingshang_man-分页列表查询", notes="wzgongyingshang_man-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(WzgongyingshangMan wzgongyingshangMan,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<WzgongyingshangMan> queryWrapper = QueryGenerator.initQueryWrapper(wzgongyingshangMan, req.getParameterMap());
		 List<WzgongyingshangMan> list = wzgongyingshangManService.list(queryWrapper);
		 return Result.OK(list);
	 }

//	/**
//	 *   添加
//	 *
//	 * @param wzgongyingshangMan
//	 * @return
//	 */
//	@AutoLog(value = "wzgongyingshang_man-添加")
//	@ApiOperation(value="wzgongyingshang_man-添加", notes="wzgongyingshang_man-添加")
//	@PostMapping(value = "/add")
//	public Result<?> add(@RequestBody WzgongyingshangMan wzgongyingshangMan) {
//		wzgongyingshangManService.save(wzgongyingshangMan);
//		return Result.OK("添加成功！");
//	}

	 /**
	  *   添加
	  *
	  * @param wzgongyingshangMan
	  * @return
	  */
	 @AutoLog(value = "wzgongyingshang_man-添加")
	 @ApiOperation(value="wzgongyingshang_man-添加", notes="wzgongyingshang_man-添加")
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody WzgongyingshangMan wzgongyingshangMan) {
		 if(StringUtils.isEmpty(wzgongyingshangMan.getSysOrgCode())){
			 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
			 wzgongyingshangMan.setDepartid(loginUser.getDepartIds().split(",")[0]);
		 }else{
			Map did = wzliaocangService.selectqueryone(wzgongyingshangMan.getSysOrgCode());
			 wzgongyingshangMan.setDepartid((String) did.get("id"));
		 }
		 String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
		 wzgongyingshangMan.setGuid(uuid);
		 Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
		 wzgongyingshangMan.setTs(ts);
		 wzgongyingshangManService.save(wzgongyingshangMan);
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param wzgongyingshangMan
	 * @return
	 */
	@AutoLog(value = "wzgongyingshang_man-编辑")
	@ApiOperation(value="wzgongyingshang_man-编辑", notes="wzgongyingshang_man-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WzgongyingshangMan wzgongyingshangMan) {
		Map selectqueryone = wzgongyingshangManService.selectqueryone(wzgongyingshangMan.getSysOrgCode());
		if(selectqueryone!=null){
			wzgongyingshangMan.setDepartid((String) selectqueryone.get("id"));
		}
		Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
		wzgongyingshangMan.setTs(ts);
		wzgongyingshangManService.updateById(wzgongyingshangMan);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzgongyingshang_man-通过id删除")
	@ApiOperation(value="wzgongyingshang_man-通过id删除", notes="wzgongyingshang_man-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzgongyingshangManService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wzgongyingshang_man-批量删除")
	@ApiOperation(value="wzgongyingshang_man-批量删除", notes="wzgongyingshang_man-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzgongyingshangManService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzgongyingshang_man-通过id查询")
	@ApiOperation(value="wzgongyingshang_man-通过id查询", notes="wzgongyingshang_man-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WzgongyingshangMan wzgongyingshangMan = wzgongyingshangManService.getById(id);
		if(wzgongyingshangMan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzgongyingshangMan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzgongyingshangMan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzgongyingshangMan wzgongyingshangMan) {
        return super.exportXls(request, wzgongyingshangMan, WzgongyingshangMan.class, "wzgongyingshang_man");
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
        return super.importExcel(request, response, WzgongyingshangMan.class);
    }

}
