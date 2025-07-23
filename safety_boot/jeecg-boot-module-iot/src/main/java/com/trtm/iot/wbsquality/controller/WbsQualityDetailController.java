package com.trtm.iot.wbsquality.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wbsquality.service.IWbsQualityService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.wbsquality.entity.WbsQualityDetail;
import com.trtm.iot.wbsquality.service.IWbsQualityDetailService;

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
 * @Description: wbs_quality_detail
 * @Author: jeecg-boot
 * @Date:   2024-11-27
 * @Version: V1.0
 */
@Api(tags="wbs_quality_detail")
@RestController
@RequestMapping("/wbsquality/wbsQualityDetail")
@Slf4j
public class WbsQualityDetailController extends JeecgController<WbsQualityDetail, IWbsQualityDetailService> {
	@Autowired
	private IWbsQualityDetailService wbsQualityDetailService;
	 @Autowired
	 private IWbsQualityService wbsQualityService;

	/**
	 * 分页列表查询
	 *
	 * @param wbsQualityDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wbs_quality_detail-分页列表查询")
	@ApiOperation(value="wbs_quality_detail-分页列表查询", notes="wbs_quality_detail-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WbsQualityDetail wbsQualityDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WbsQualityDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbsQualityDetail, req.getParameterMap());
		Page<WbsQualityDetail> page = new Page<WbsQualityDetail>(pageNo, pageSize);
		IPage<WbsQualityDetail> pageList = wbsQualityDetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "wbs_quality_detail-分页列表查询")
	 @ApiOperation(value="wbs_quality_detail-分页列表查询", notes="wbs_quality_detail-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(WbsQualityDetail wbsQualityDetail,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<WbsQualityDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbsQualityDetail, req.getParameterMap());
		//  Page<WbsQualityDetail> page = new Page<WbsQualityDetail>(pageNo, pageSize);
		 List<WbsQualityDetail> pageList = wbsQualityDetailService.list(queryWrapper);
		 if(pageList.size() == 0){
			 // 如果没查到对应表记录责自动生成
			 String s = wbsQualityDetailService.saveBatchBywbsid(wbsQualityDetail.getWbsid(), wbsQualityDetail.getTreeid(), wbsQualityDetail.getTemplateid());
		      if(s.equals("保存表数据成功")){
				  pageList = wbsQualityDetailService.list(queryWrapper);
			  }else{
				  return Result.error(s);
			  }
		 }
		 // 查到表记录则返回
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param wbsQualityDetail
	 * @return
	 */
	@AutoLog(value = "wbs_quality_detail-添加")
	@ApiOperation(value="wbs_quality_detail-添加", notes="wbs_quality_detail-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WbsQualityDetail wbsQualityDetail) {
		wbsQualityDetailService.save(wbsQualityDetail);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wbsQualityDetail
	 * @return
	 */
	@AutoLog(value = "wbs_quality_detail-编辑")
	@ApiOperation(value="wbs_quality_detail-编辑", notes="wbs_quality_detail-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WbsQualityDetail wbsQualityDetail) {
		wbsQualityDetailService.updateById(wbsQualityDetail);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wbs_quality_detail-通过id删除")
	@ApiOperation(value="wbs_quality_detail-通过id删除", notes="wbs_quality_detail-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wbsQualityDetailService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wbs_quality_detail-批量删除")
	@ApiOperation(value="wbs_quality_detail-批量删除", notes="wbs_quality_detail-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wbsQualityDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wbs_quality_detail-通过id查询")
	@ApiOperation(value="wbs_quality_detail-通过id查询", notes="wbs_quality_detail-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WbsQualityDetail wbsQualityDetail = wbsQualityDetailService.getById(id);
		if(wbsQualityDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wbsQualityDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wbsQualityDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WbsQualityDetail wbsQualityDetail) {
        return super.exportXls(request, wbsQualityDetail, WbsQualityDetail.class, "wbs_quality_detail");
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
        return super.importExcel(request, response, WbsQualityDetail.class);
    }

}
