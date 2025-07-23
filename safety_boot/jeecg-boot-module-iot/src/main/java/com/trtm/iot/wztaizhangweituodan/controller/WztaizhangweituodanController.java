package com.trtm.iot.wztaizhangweituodan.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.wztaizhangweituodan.entity.Wztaizhangweituodan;
import com.trtm.iot.wztaizhangweituodan.service.IWztaizhangweituodanService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 原材料检验委托申请表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Api(tags="原材料检验委托申请表信息")
@RestController
@RequestMapping("/wztaizhangweituodan/wztaizhangweituodan")
@Slf4j
public class WztaizhangweituodanController extends JeecgController<Wztaizhangweituodan, IWztaizhangweituodanService> {
	@Autowired
	private IWztaizhangweituodanService wztaizhangweituodanService;
	 @Autowired
	 private RedisUtil redisUtil;
	
	/**
	 * 分页列表查询
	 *
	 * @param wztaizhangweituodan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "原材料检验委托申请表信息-分页列表查询")
	@ApiOperation(value="原材料检验委托申请表信息-分页列表查询", notes="原材料检验委托申请表信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Wztaizhangweituodan wztaizhangweituodan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (wztaizhangweituodan.getShebeibianhao() == null) {
			if (!"null".equals(shebei)) {
				wztaizhangweituodan.setShebeibianhao(shebei);
			}else {
				wztaizhangweituodan.setShebeibianhao("空");
			}
		}
		QueryWrapper<Wztaizhangweituodan> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhangweituodan, req.getParameterMap());
		Page<Wztaizhangweituodan> page = new Page<Wztaizhangweituodan>(pageNo, pageSize);
		IPage<Wztaizhangweituodan> pageList = wztaizhangweituodanService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wztaizhangweituodan
	 * @return
	 */
	@AutoLog(value = "原材料检验委托申请表信息-添加")
	@ApiOperation(value="原材料检验委托申请表信息-添加", notes="原材料检验委托申请表信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Wztaizhangweituodan wztaizhangweituodan) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		wztaizhangweituodan.setQuyangren(loginUser.getRealname());
		wztaizhangweituodan.setWeituoren(loginUser.getRealname());
		wztaizhangweituodan.setCreatetime(new Date());
		wztaizhangweituodan.setQydw(loginUser.getPost());
		wztaizhangweituodanService.save(wztaizhangweituodan);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wztaizhangweituodan
	 * @return
	 */
	@AutoLog(value = "原材料检验委托申请表信息-编辑")
	@ApiOperation(value="原材料检验委托申请表信息-编辑", notes="原材料检验委托申请表信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Wztaizhangweituodan wztaizhangweituodan) {
		wztaizhangweituodanService.updateById(wztaizhangweituodan);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原材料检验委托申请表信息-通过id删除")
	@ApiOperation(value="原材料检验委托申请表信息-通过id删除", notes="原材料检验委托申请表信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wztaizhangweituodanService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "原材料检验委托申请表信息-批量删除")
	@ApiOperation(value="原材料检验委托申请表信息-批量删除", notes="原材料检验委托申请表信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wztaizhangweituodanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原材料检验委托申请表信息-通过id查询")
	@ApiOperation(value="原材料检验委托申请表信息-通过id查询", notes="原材料检验委托申请表信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Wztaizhangweituodan wztaizhangweituodan = wztaizhangweituodanService.getById(id);
		if(wztaizhangweituodan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wztaizhangweituodan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wztaizhangweituodan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wztaizhangweituodan wztaizhangweituodan) {
        return super.exportXls(request, wztaizhangweituodan, Wztaizhangweituodan.class, "原材料检验委托申请表信息");
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
        return super.importExcel(request, response, Wztaizhangweituodan.class);
    }

}
