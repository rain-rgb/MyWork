package com.trtm.iot.yj.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.yj.entity.TrYajiang;
import com.trtm.iot.yj.service.ITrYajiangService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: tr_yajiang
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@Api(tags="tr_yajiang")
@RestController
@RequestMapping("/zl/YaJiang")
@Slf4j
public class TrYajiangController extends JeecgController<TrYajiang, ITrYajiangService> {
	@Autowired
	private ITrYajiangService trYajiangService;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param trYajiang
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "tr_yajiang-分页列表查询")
	@ApiOperation(value="tr_yajiang-分页列表查询", notes="tr_yajiang-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrYajiang trYajiang,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId()+"change"));
		if(trYajiang.getDeviceno()==null){
			if(shebei!=null){
				trYajiang.setDeviceno(shebei);
			}
		}
		QueryWrapper<TrYajiang> queryWrapper = QueryGenerator.initQueryWrapper(trYajiang, req.getParameterMap());
		Page<TrYajiang> page = new Page<TrYajiang>(pageNo, pageSize);
		IPage<TrYajiang> pageList = trYajiangService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param trYajiang
	 * @return
	 */
	@AutoLog(value = "tr_yajiang-添加")
	@ApiOperation(value="tr_yajiang-添加", notes="tr_yajiang-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrYajiang trYajiang) {
		trYajiangService.save(trYajiang);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param trYajiang
	 * @return
	 */
	@AutoLog(value = "tr_yajiang-编辑")
	@ApiOperation(value="tr_yajiang-编辑", notes="tr_yajiang-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrYajiang trYajiang) {
		trYajiangService.updateById(trYajiang);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "tr_yajiang-通过id删除")
	@ApiOperation(value="tr_yajiang-通过id删除", notes="tr_yajiang-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trYajiangService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "tr_yajiang-批量删除")
	@ApiOperation(value="tr_yajiang-批量删除", notes="tr_yajiang-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trYajiangService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "tr_yajiang-通过id查询")
	@ApiOperation(value="tr_yajiang-通过id查询", notes="tr_yajiang-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrYajiang trYajiang = trYajiangService.getById(id);
		if(trYajiang==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trYajiang);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trYajiang
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrYajiang trYajiang) {
        return super.exportXls(request, trYajiang, TrYajiang.class, "tr_yajiang");
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
        return super.importExcel(request, response, TrYajiang.class);
    }

}
