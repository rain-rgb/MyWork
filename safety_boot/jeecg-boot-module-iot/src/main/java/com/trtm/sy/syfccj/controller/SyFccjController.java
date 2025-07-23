package com.trtm.sy.syfccj.controller;

import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wbshebeidetail.service.IWbshebeiDetailService;
import com.trtm.sy.syfccj.entity.SyFccj;
import com.trtm.sy.syfccj.service.ISyFccjService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 发车抽检
 * @Author: jeecg-boot
 * @Date:   2022-09-20
 * @Version: V1.0
 */
@Api(tags="发车抽检")
@RestController
@RequestMapping("/syfccj/syFccj")
@Slf4j
public class SyFccjController extends JeecgController<SyFccj, ISyFccjService> {
	@Autowired
	private ISyFccjService syFccjService;

	
	/**
	 * 分页列表查询
	 *
	 * @param syFccj
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "发车抽检-分页列表查询")
	@ApiOperation(value="发车抽检-分页列表查询", notes="发车抽检-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SyFccj syFccj,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SyFccj> queryWrapper = QueryGenerator.initQueryWrapper(syFccj, req.getParameterMap());
		Page<SyFccj> page = new Page<SyFccj>(pageNo, pageSize);
		IPage<SyFccj> pageList = syFccjService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param syFccj
	 * @return
	 */
	@AutoLog(value = "发车抽检-确认")
	@ApiOperation(value="发车抽检-确认", notes="发车抽检-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SyFccj syFccj) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String cjdw = loginUser.getPost();
		if(StringUtils.isEmpty(cjdw)){
			return Result.error("该用户未设置所属职务，无法抽检");
		}
		SyFccj byFcid = syFccjService.findByFcid(syFccj.getFcid(), cjdw);
		if(null == byFcid){
			syFccj.setQrp(loginUser.getUsername());
			syFccj.setQrtime(new Date());
			syFccj.setCjdw(cjdw);// 确认抽检所属机构（监理；指挥部）
			syFccjService.save(syFccj);
			return Result.OK("抽检确认成功！");
		}else{
			return Result.error("已被确认抽检，请勿重复");
		}

	}
	
	/**
	 *  编辑
	 *
	 * @param syFccj
	 * @return
	 */
	@AutoLog(value = "发车抽检-审核")
	@ApiOperation(value="发车抽检-编辑", notes="发车抽检-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SyFccj syFccj) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		syFccj.setCjp(loginUser.getUsername());
		syFccj.setCjtime(new Date());
		syFccjService.updateById(syFccj);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "发车抽检-通过id删除")
	@ApiOperation(value="发车抽检-通过id删除", notes="发车抽检-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		syFccjService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "发车抽检-批量删除")
	@ApiOperation(value="发车抽检-批量删除", notes="发车抽检-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.syFccjService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "发车抽检-通过id查询")
	@ApiOperation(value="发车抽检-通过id查询", notes="发车抽检-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SyFccj syFccj = syFccjService.getById(id);
		if(syFccj==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(syFccj);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param syFccj
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyFccj syFccj) {
        return super.exportXls(request, syFccj, SyFccj.class, "发车抽检");
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
        return super.importExcel(request, response, SyFccj.class);
    }

}
