package com.trtm.iot.gscsClockinDetail.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.trtm.iot.gscsClockinDetail.entity.GscsClockinDetail;
import com.trtm.iot.gscsClockinDetail.service.IGscsClockinDetailService;

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
 * @Description: 班组安全管控系统打卡详情表
 * @Author: jeecg-boot
 * @Date:   2022-01-25
 * @Version: V1.0
 */
@Api(tags="班组安全管控系统打卡详情表")
@RestController
@RequestMapping("/gscsClockinDetail/gscsClockinDetail")
@Slf4j
public class GscsClockinDetailController extends JeecgController<GscsClockinDetail, IGscsClockinDetailService> {
	@Autowired
	private IGscsClockinDetailService gscsClockinDetailService;

	/**
	 * 分页列表查询
	 *
	 * @param gscsClockinDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "班组安全管控系统打卡详情表-分页列表查询")
	@ApiOperation(value="班组安全管控系统打卡详情表-分页列表查询", notes="班组安全管控系统打卡详情表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GscsClockinDetail gscsClockinDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GscsClockinDetail> queryWrapper = QueryGenerator.initQueryWrapper(gscsClockinDetail, req.getParameterMap());
		Page<GscsClockinDetail> page = new Page<GscsClockinDetail>(pageNo, pageSize);
		IPage<GscsClockinDetail> pageList = gscsClockinDetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param gscsClockinDetail
	 * @return
	 */
	@AutoLog(value = "班组安全管控系统打卡详情表-添加")
	@ApiOperation(value="班组安全管控系统打卡详情表-添加", notes="班组安全管控系统打卡详情表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GscsClockinDetail gscsClockinDetail) {
		Date date = new Date();
		String strDateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		String today = sdf.format(date);
		QueryWrapper<GscsClockinDetail> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("phone",gscsClockinDetail.getPhone())
					.eq("clock_in_time",today);
		GscsClockinDetail clockinDetail = gscsClockinDetailService.getOne(queryWrapper);
		if (clockinDetail!=null) {
			return Result.error("当日已经完成健康打卡，请勿重复提交");
		}
		gscsClockinDetail.setClockInTime(date);
		gscsClockinDetailService.save(gscsClockinDetail);
		return Result.OK("打卡成功！");
	}

	/**
	 *  编辑
	 *
	 * @param gscsClockinDetail
	 * @return
	 */
	@AutoLog(value = "班组安全管控系统打卡详情表-编辑")
	@ApiOperation(value="班组安全管控系统打卡详情表-编辑", notes="班组安全管控系统打卡详情表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GscsClockinDetail gscsClockinDetail) {
		Date date = new Date();
		gscsClockinDetail.setClockInTime(date);
		gscsClockinDetailService.updateById(gscsClockinDetail);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "班组安全管控系统打卡详情表-通过id删除")
	@ApiOperation(value="班组安全管控系统打卡详情表-通过id删除", notes="班组安全管控系统打卡详情表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		gscsClockinDetailService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "班组安全管控系统打卡详情表-批量删除")
	@ApiOperation(value="班组安全管控系统打卡详情表-批量删除", notes="班组安全管控系统打卡详情表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gscsClockinDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "班组安全管控系统打卡详情表-通过id查询")
	@ApiOperation(value="班组安全管控系统打卡详情表-通过id查询", notes="班组安全管控系统打卡详情表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		GscsClockinDetail gscsClockinDetail = gscsClockinDetailService.getById(id);
		if(gscsClockinDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(gscsClockinDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gscsClockinDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GscsClockinDetail gscsClockinDetail) {
        return super.exportXls(request, gscsClockinDetail, GscsClockinDetail.class, "班组安全管控系统打卡详情表");
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
        return super.importExcel(request, response, GscsClockinDetail.class);
    }

}
