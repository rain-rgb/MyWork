package com.trtm.iot.yajiangss.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.yajiangss.vo.TrYaJiangSSList;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.yajiangss.entity.TrYajiangSS;
import com.trtm.iot.yajiangss.service.ITrYajiangSSService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 压浆过程表
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
@Api(tags="压浆过程表")
@RestController
@RequestMapping("/yajiangs/trYajiangSS")
@Slf4j
public class TrYajiangSSController extends JeecgController<TrYajiangSS, ITrYajiangSSService> {
	@Autowired
	private ITrYajiangSSService trYajiangSSService;

	/**
	 * 分页列表查询
	 *
	 * @param trYajiangSS
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "压浆过程表-分页列表查询")
	@ApiOperation(value="压浆过程表-分页列表查询", notes="压浆过程表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrYajiangSS trYajiangSS,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrYajiangSS> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangSS, req.getParameterMap());
		queryWrapper.orderByAsc("jlsj");
		Page<TrYajiangSS> page = new Page<TrYajiangSS>(pageNo, pageSize);
		if(StringUtils.isNotBlank(trYajiangSS.getHoleid())){
			IPage<TrYajiangSS> pageList = trYajiangSSService.page(page, queryWrapper);
			return Result.OK(pageList);
		}else{
			return Result.error("厂家未上传孔道号holeid");
		}


	}

	 /**
	  * 压浆过程数据通过holeid
	  *
	  * @param trYajiangSS
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "压浆过程数据通过holeid")
	 @ApiOperation(value="压浆过程数据通过holeid", notes="压浆过程数据通过holeid")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(TrYajiangSS trYajiangSS, HttpServletRequest req) {
		 QueryWrapper<TrYajiangSS> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangSS, req.getParameterMap());
		 if(StringUtils.isNotBlank(trYajiangSS.getHoleid())){
			 List<TrYajiangSS> list = trYajiangSSService.list(queryWrapper);
			 return Result.OK(list);
		 }else{
			 return Result.error("厂家未上传孔道号holeid");
		 }

	 }

	/**
	 *   添加
	 *
	 * @param trYajiangSS
	 * @return
	 */
	@AutoLog(value = "压浆过程表-添加")
	@ApiOperation(value="压浆过程表-添加", notes="压浆过程表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrYajiangSS trYajiangSS) {
		trYajiangSSService.save(trYajiangSS);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加
	  *
	  * @param trYajiangSS
	  * @return
	  */
	 @AutoLog(value = "压浆过程表-添加")
	 @ApiOperation(value="压浆过程表-添加", notes="压浆过程表-添加")
	 @PostMapping(value = "/addList")
	 public Result<?> addList(@RequestBody TrYaJiangSSList trYajiangSS) {
		 trYajiangSSService.saveMain(trYajiangSS.getTrYajiangSS());
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param trYajiangSS
	 * @return
	 */
	@AutoLog(value = "压浆过程表-编辑")
	@ApiOperation(value="压浆过程表-编辑", notes="压浆过程表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrYajiangSS trYajiangSS) {
		trYajiangSSService.updateById(trYajiangSS);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "压浆过程表-通过id删除")
	@ApiOperation(value="压浆过程表-通过id删除", notes="压浆过程表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trYajiangSSService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "压浆过程表-批量删除")
	@ApiOperation(value="压浆过程表-批量删除", notes="压浆过程表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trYajiangSSService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "压浆过程表-通过id查询")
	@ApiOperation(value="压浆过程表-通过id查询", notes="压浆过程表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrYajiangSS trYajiangSS = trYajiangSSService.getById(id);
		if(trYajiangSS==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trYajiangSS);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trYajiangSS
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrYajiangSS trYajiangSS) {
        return super.exportXls(request, trYajiangSS, TrYajiangSS.class, "压浆过程表");
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
        return super.importExcel(request, response, TrYajiangSS.class);
    }

}
