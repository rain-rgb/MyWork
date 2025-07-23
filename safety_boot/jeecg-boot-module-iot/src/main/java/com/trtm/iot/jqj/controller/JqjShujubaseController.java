package com.trtm.iot.jqj.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.jqj.entity.JqjShujubase;
import com.trtm.iot.jqj.service.IJqjShujubaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: jqj_shujubase
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Api(tags="jqj_shujubase")
@RestController
@RequestMapping("/jqj/jqjShujubase")
@Slf4j
public class JqjShujubaseController extends JeecgController<JqjShujubase, IJqjShujubaseService> {
	@Autowired
	private IJqjShujubaseService jqjShujubaseService;
	@Autowired
	private RedisUtil redisUtil;


	/**
	 * 分页列表查询
	 *
	 * @param jqjShujubase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "jqj_shujubase-分页列表查询")
	@ApiOperation(value="jqj_shujubase-分页列表查询", notes="jqj_shujubase-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(JqjShujubase jqjShujubase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if(jqjShujubase.getDeviceCode()==null){
			if(shebei!=null){
				jqjShujubase.setDeviceCode(shebei);
			}
		}
		QueryWrapper<JqjShujubase> queryWrapper = QueryGenerator.initQueryWrapper(jqjShujubase, req.getParameterMap());
		Page<JqjShujubase> page = new Page<JqjShujubase>(pageNo, pageSize);
		IPage<JqjShujubase> pageList = jqjShujubaseService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	/**
	 *   添加
	 *
	 * @param jqjShujubase
	 * @return
	 */
	@AutoLog(value = "jqj_shujubase-添加")
	@ApiOperation(value="jqj_shujubase-添加", notes="jqj_shujubase-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JqjShujubase jqjShujubase) {
		jqjShujubaseService.save(jqjShujubase);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param jqjShujubase
	 * @return
	 */
	@AutoLog(value = "jqj_shujubase-编辑")
	@ApiOperation(value="jqj_shujubase-编辑", notes="jqj_shujubase-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JqjShujubase jqjShujubase) {
		jqjShujubaseService.updateById(jqjShujubase);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jqj_shujubase-通过id删除")
	@ApiOperation(value="jqj_shujubase-通过id删除", notes="jqj_shujubase-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		jqjShujubaseService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "jqj_shujubase-批量删除")
	@ApiOperation(value="jqj_shujubase-批量删除", notes="jqj_shujubase-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.jqjShujubaseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "jqj_shujubase-通过id查询")
	@ApiOperation(value="jqj_shujubase-通过id查询", notes="jqj_shujubase-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		JqjShujubase jqjShujubase = jqjShujubaseService.getById(id);
		if(jqjShujubase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(jqjShujubase);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param jqjShujubase
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, JqjShujubase jqjShujubase) {
        return super.exportXls(request, jqjShujubase, JqjShujubase.class, "jqj_shujubase");
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
        return super.importExcel(request, response, JqjShujubase.class);
    }

}
