package com.trtm.iot.wzgongyingshang.controller;

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

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;

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
 * @Description: 物资供应商主表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
@Api(tags="物资供应商主表")
@RestController
@RequestMapping("/wzgongyingshang/wzgongyingshang")
@Slf4j
public class WzgongyingshangController extends JeecgController<Wzgongyingshang, IWzgongyingshangService> {
	@Autowired
	private IWzgongyingshangService wzgongyingshangService;

	/**
	 * 分页列表查询
	 *
	 * @param wzgongyingshang
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "物资供应商主表-分页列表查询")
	@ApiOperation(value="物资供应商主表-分页列表查询", notes="物资供应商主表-分页列表查询")
	@GetMapping(value = "/list")
	@PermissionData(pageComponent = "wzgl/wzgongyingshang/WzgongyingshangList")
	public Result<?> queryPageList(Wzgongyingshang wzgongyingshang,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			wzgongyingshang.setSysOrgCode("*" + sys_depart_orgcode + "*");
		}else{
			wzgongyingshang.setSysOrgCode(loginUser.getOrgCode() + "*");
		}
		wzgongyingshang.setGongyingcailiaoname("*" + wzgongyingshang.getGongyingshangname() + "*");
		QueryWrapper<Wzgongyingshang> queryWrapper = QueryGenerator.initQueryWrapper(wzgongyingshang, req.getParameterMap());
		Page<Wzgongyingshang> page = new Page<Wzgongyingshang>(pageNo, pageSize);
		IPage<Wzgongyingshang> pageList = wzgongyingshangService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	@AutoLog(value = "物资供应商主表-分页列表查询")
	@ApiOperation(value="物资供应商主表-分页列表查询", notes="物资供应商主表-分页列表查询")
	@GetMapping(value = "/list1")
	public Result<?> queryPageList1(Wzgongyingshang wzgongyingshang,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		wzgongyingshang.setSysOrgCode(loginUser.getOrgCode() + "*");
		wzgongyingshang.setIselocks(1);
		QueryWrapper<Wzgongyingshang> queryWrapper = QueryGenerator.initQueryWrapper(wzgongyingshang, req.getParameterMap());
		List<Wzgongyingshang> list = wzgongyingshangService.list(queryWrapper);
		return Result.OK(list);
	}

	/**
	 *   添加
	 *
	 * @param wzgongyingshang
	 * @return
	 */
	@AutoLog(value = "物资供应商主表-添加")
	@ApiOperation(value="物资供应商主表-添加", notes="物资供应商主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Wzgongyingshang wzgongyingshang) {
		String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
		wzgongyingshang.setGuid(uuid);
		Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
		wzgongyingshang.setTs(ts);
		wzgongyingshangService.save(wzgongyingshang);
		return Result.OK("添加成功！");
	}

	/**
	 *   物资供应商对外开放添加数据接口
	 *
	 * @param wzgongyingshang
	 * @return
	 */
	@AutoLog(value = "物资供应商主表-添加")
	@ApiOperation(value="物资供应商主表-添加", notes="物资供应商主表-添加")
	@PostMapping(value = "/addOpen")
	public Result<?> addOpen(@RequestBody Wzgongyingshang wzgongyingshang) {
		QueryWrapper<Wzgongyingshang> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("guid",wzgongyingshang.getGuid());
		Wzgongyingshang one = wzgongyingshangService.getOne(queryWrapper);
		if (one==null) {
			wzgongyingshangService.save(wzgongyingshang);
			return Result.OK("添加成功！");
		}else {
			return Result.error("添加失败！");
		}
	}

	/**
	 *  编辑
	 *
	 * @param wzgongyingshang
	 * @return
	 */
	@AutoLog(value = "物资供应商主表-编辑")
	@ApiOperation(value="物资供应商主表-编辑", notes="物资供应商主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Wzgongyingshang wzgongyingshang) {
		Map selectqueryone = wzgongyingshangService.selectqueryone(wzgongyingshang.getSysOrgCode());
		if(selectqueryone!=null){
			wzgongyingshang.setDepartid((String) selectqueryone.get("id"));
		}
		Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
		wzgongyingshang.setTs(ts);
		wzgongyingshangService.updateById(wzgongyingshang);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物资供应商主表-通过id删除")
	@ApiOperation(value="物资供应商主表-通过id删除", notes="物资供应商主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzgongyingshangService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物资供应商主表-批量删除")
	@ApiOperation(value="物资供应商主表-批量删除", notes="物资供应商主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzgongyingshangService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物资供应商主表-通过id查询")
	@ApiOperation(value="物资供应商主表-通过id查询", notes="物资供应商主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Wzgongyingshang wzgongyingshang = wzgongyingshangService.getById(id);
		if(wzgongyingshang==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzgongyingshang);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzgongyingshang
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzgongyingshang wzgongyingshang) {
        return super.exportXls(request, wzgongyingshang, Wzgongyingshang.class, "物资供应商主表");
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
        return super.importExcel(request, response, Wzgongyingshang.class);
    }

	/**
	 * 查询所有的供应商和供应商编码
	 * @return
	 */

	@ApiOperation(value="查询所有的供应商和供应商编码", notes="")
	@GetMapping(value = "/queryList")
	public Result<?> queryList(String sysOrgCode) {
		List<Map> listMap = wzgongyingshangService.queryList(sysOrgCode);
		return Result.OK(listMap);
	}

}
