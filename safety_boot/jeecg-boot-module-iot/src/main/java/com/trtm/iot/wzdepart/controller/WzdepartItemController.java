package com.trtm.iot.wzdepart.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.wzdepart.entity.WzdepartItem;
import com.trtm.iot.wzdepart.service.IWzdepartItemService;

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
 * @Description: wzdepart_item
 * @Author: jeecg-boot
 * @Date:   2023-10-20
 * @Version: V1.0
 */
@Api(tags="wzdepart_item")
@RestController
@RequestMapping("/wzdepart/wzdepartItem")
@Slf4j
public class WzdepartItemController extends JeecgController<WzdepartItem, IWzdepartItemService> {
	@Autowired
	private IWzdepartItemService wzdepartItemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wzdepartItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */

	@AutoLog(value = "wzdepart_item-分页列表查询")
	@ApiOperation(value="wzdepart_item-分页列表查询", notes="wzdepart_item-分页列表查询")
	@GetMapping(value = "/listType")
	public Result<?> queryPageListType(WzdepartItem wzdepartItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WzdepartItem> queryWrapper = new QueryWrapper<>();
		if(StringUtils.isNotBlank(wzdepartItem.getFbgc())){
			// 查询分项工程
			queryWrapper.eq("fbgc",wzdepartItem.getFbgc());
			queryWrapper.groupBy("fxgc");
		}else{
			// 查询分部工程
			queryWrapper.groupBy("fbgc");
		}
		Page<WzdepartItem> page = new Page<WzdepartItem>(pageNo, pageSize);
		List<WzdepartItem> pageList = wzdepartItemService.list(queryWrapper);
		return Result.OK(pageList);
	}

	@AutoLog(value = "wzdepart_item-分页列表查询")
	@ApiOperation(value="wzdepart_item-分页列表查询", notes="wzdepart_item-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WzdepartItem wzdepartItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WzdepartItem> queryWrapper = QueryGenerator.initQueryWrapper(wzdepartItem, req.getParameterMap());
		Page<WzdepartItem> page = new Page<WzdepartItem>(pageNo, pageSize);
		IPage<WzdepartItem> pageList = wzdepartItemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wzdepartItem
	 * @return
	 */
	@AutoLog(value = "wzdepart_item-添加")
	@ApiOperation(value="wzdepart_item-添加", notes="wzdepart_item-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WzdepartItem wzdepartItem) {
		wzdepartItemService.save(wzdepartItem);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wzdepartItem
	 * @return
	 */
	@AutoLog(value = "wzdepart_item-编辑")
	@ApiOperation(value="wzdepart_item-编辑", notes="wzdepart_item-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WzdepartItem wzdepartItem) {
		wzdepartItemService.updateById(wzdepartItem);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzdepart_item-通过id删除")
	@ApiOperation(value="wzdepart_item-通过id删除", notes="wzdepart_item-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzdepartItemService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wzdepart_item-批量删除")
	@ApiOperation(value="wzdepart_item-批量删除", notes="wzdepart_item-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzdepartItemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzdepart_item-通过id查询")
	@ApiOperation(value="wzdepart_item-通过id查询", notes="wzdepart_item-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WzdepartItem wzdepartItem = wzdepartItemService.getById(id);
		if(wzdepartItem==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzdepartItem);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzdepartItem
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzdepartItem wzdepartItem) {
        return super.exportXls(request, wzdepartItem, WzdepartItem.class, "wzdepart_item");
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
        return super.importExcel(request, response, WzdepartItem.class);
    }

}
