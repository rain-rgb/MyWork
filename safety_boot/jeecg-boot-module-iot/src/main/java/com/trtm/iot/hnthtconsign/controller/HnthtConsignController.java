package com.trtm.iot.hnthtconsign.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.api.utils.StringUtils;
import com.trtm.iot.system.entity.Shigongphb;
import org.apache.poi.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;

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
 * @Description: 检测试验任务单下发信息表
 * @Author: jeecg-boot
 * @Date:   2021-07-09
 * @Version: V1.0
 */
@Api(tags="检测试验任务单下发信息表")
@RestController
@RequestMapping("/hnthtconsign/hnthtConsign")
@Slf4j
public class HnthtConsignController extends JeecgController<HnthtConsign, IHnthtConsignService> {
	@Autowired
	private IHnthtConsignService hnthtConsignService;

	/**
	 * 分页列表查询
	 *
	 * @param hnthtConsign
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "检测试验任务单下发信息表-分页列表查询")
	@ApiOperation(value="检测试验任务单下发信息表-分页列表查询", notes="检测试验任务单下发信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HnthtConsign hnthtConsign,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			hnthtConsign.setSysOrgCode(sys_depart_orgcode + "*");
		}else {
			hnthtConsign.setSysOrgCode(loginUser.getOrgCode() + "*");
		}
		hnthtConsign.setProjectname("*" + hnthtConsign.getProjectname() + "*");
		hnthtConsign.setUuid("*" + hnthtConsign.getUuid() + "*");
		hnthtConsign.setSgbwname("*" + hnthtConsign.getSgbwname() + "*");
		hnthtConsign.setIsdel(0);
		QueryWrapper<HnthtConsign> queryWrapper = QueryGenerator.initQueryWrapper(hnthtConsign, req.getParameterMap());
		Page<HnthtConsign> page = new Page<HnthtConsign>(pageNo, pageSize);
		IPage<HnthtConsign> pageList = hnthtConsignService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "检测试验任务单下发信息表-厂家获取检测试验任务单数据")
	 @ApiOperation(value="检测试验任务单下发信息表-厂家获取检测试验任务单数据", notes="检测试验任务单下发信息表-厂家获取检测试验任务单数据")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList2(HnthtConsign hnthtConsign,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
									 HttpServletRequest req) {
		if (hnthtConsign.getStatus() == null){
			hnthtConsign.setStatus("0");
		}
		hnthtConsign.setIsdel(0);
		QueryWrapper<HnthtConsign> queryWrapper = QueryGenerator.initQueryWrapper(hnthtConsign, req.getParameterMap());
		queryWrapper.orderByDesc("id");
		List<HnthtConsign> pageList = hnthtConsignService.list(queryWrapper);
		return Result.OK(pageList);
	 }

	 /**
	  *  检测试验任务单下发添加与编辑详见 SysDepartprojectController
	  */

	 /**
	  *   检测任务单下发对外开放添加数据
	  *
	  * @param hnthtConsign
	  * @return
	  */
	 @AutoLog(value = "检测试验任务单下发信息表-添加")
	 @ApiOperation(value="检测试验任务单下发信息表-添加", notes="检测试验任务单下发信息表-添加")
	 @PostMapping(value = "/addOpen")
	 public Result<?> addOpen(@RequestBody HnthtConsign hnthtConsign) {
		 String uuid = hnthtConsign.getUuid();
		 QueryWrapper<HnthtConsign> hnthtConsignQueryWrapper = new QueryWrapper<>();
		 hnthtConsignQueryWrapper.eq("uuid", uuid);
		 if(StringUtils.isNotEmpty(hnthtConsign.getSgbwguid())){
			 hnthtConsign.setTreeid(hnthtConsign.getSgbwguid());
		 }
		 List<HnthtConsign> list = hnthtConsignService.list(hnthtConsignQueryWrapper);
		 if (list.size()>0){
			 hnthtConsignService.remove(hnthtConsignQueryWrapper);
		 }

		 hnthtConsignService.save(hnthtConsign);
		 return Result.OK("添加成功！");
	 }

	/**
	 *   添加
	 *
	 * @param hnthtConsign
	 * @return
	 */
	@AutoLog(value = "检测试验任务单下发信息表-添加")
	@ApiOperation(value="检测试验任务单下发信息表-添加", notes="检测试验任务单下发信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HnthtConsign hnthtConsign) {
		hnthtConsignService.save(hnthtConsign);
		return Result.OK("添加成功！");
	}
	/**
	 *  编辑
	 *
	 * @param hnthtConsign
	 * @return
	 */
	@AutoLog(value = "检测试验任务单下发信息表-编辑")
	@ApiOperation(value="检测试验任务单下发信息表-编辑", notes="检测试验任务单下发信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HnthtConsign hnthtConsign) {
		hnthtConsignService.updateById(hnthtConsign);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检测试验任务单下发信息表-通过id删除")
	@ApiOperation(value="检测试验任务单下发信息表-通过id删除", notes="检测试验任务单下发信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) Integer id) {
		HnthtConsign hnthtConsign = hnthtConsignService.getById(id);
		hnthtConsign.setIsdel(1);
		hnthtConsignService.updateById(hnthtConsign);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "检测试验任务单下发信息表-批量删除")
	@ApiOperation(value="检测试验任务单下发信息表-批量删除", notes="检测试验任务单下发信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hnthtConsignService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检测试验任务单下发信息表-通过id查询")
	@ApiOperation(value="检测试验任务单下发信息表-通过id查询", notes="检测试验任务单下发信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HnthtConsign hnthtConsign = hnthtConsignService.getById(id);
		if(hnthtConsign==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hnthtConsign);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hnthtConsign
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HnthtConsign hnthtConsign) {
        return super.exportXls(request, hnthtConsign, HnthtConsign.class, "检测试验任务单下发信息表");
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
        return super.importExcel(request, response, HnthtConsign.class);
    }

}
