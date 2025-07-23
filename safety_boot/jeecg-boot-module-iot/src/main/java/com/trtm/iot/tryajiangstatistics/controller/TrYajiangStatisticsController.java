package com.trtm.iot.tryajiangstatistics.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tryajiangstatistics.vo.YjstatisticsVO;
import com.trtm.iot.trzhanglastatistics.entity.TrZhanglaStatistics;
import com.trtm.iot.trzhanglastatistics.vo.ZlstatisticsVO;
import com.trtm.iot.tsyjzbStatistics.entity.TSyjzbStatistics;
import com.trtm.iot.tsyjzbStatistics.vo.statisticsVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.tryajiangstatistics.entity.TrYajiangStatistics;
import com.trtm.iot.tryajiangstatistics.service.ITrYajiangStatisticsService;

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
 * @Description: 压浆统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-10
 * @Version: V1.0
 */
@Api(tags="压浆统计表")
@RestController
@RequestMapping("/tryajiangstatistics/trYajiangStatistics")
@Slf4j
public class TrYajiangStatisticsController extends JeecgController<TrYajiangStatistics, ITrYajiangStatisticsService> {
	@Autowired
	private ITrYajiangStatisticsService trYajiangStatisticsService;

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IShebeiInfoService shebeiInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param trYajiangStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "压浆统计表-分页列表查询")
	@ApiOperation(value="压浆统计表-分页列表查询", notes="压浆统计表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrYajiangStatistics trYajiangStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrYajiangStatistics> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangStatistics, req.getParameterMap());
		Page<TrYajiangStatistics> page = new Page<TrYajiangStatistics>(pageNo, pageSize);
		IPage<TrYajiangStatistics> pageList = trYajiangStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 压浆统计
	  *
	  * @param trYajiangStatistics
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "压浆统计表查询")
	 @ApiOperation(value="压浆统计表查询", notes="压浆统计查询")
	 @GetMapping(value = "/yjwarn")
	 public Result<?> queryList(TrYajiangStatistics trYajiangStatistics,
								HttpServletRequest req, String sys_depart_orgcode) {
		 YjstatisticsVO vo = syjwarn(trYajiangStatistics,req,10,sys_depart_orgcode);
		 return Result.OK(vo);
	 }
	 /**
	  * 压浆统计
	  *
	  * @param trYajiangStatistics
	  * @param req
	  * @return
	  */
	 public YjstatisticsVO syjwarn(TrYajiangStatistics trYajiangStatistics,
								 HttpServletRequest req,
								 Integer sbtype,
								 String orgCode
	 							 ) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 String[] split = shebei.split(",");
		 List<String> lists=new ArrayList();
		 for (int i = 0; i < split.length; i++) {
			 lists.add(split[i]);
		 }
		 List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(lists, sbtype);
		 String shebeis=null;
		 if(shebeiInfos.size()>0){
			 shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
		 }
		 if (trYajiangStatistics.getYjsbbh() == null) {
			 if ("null".equals(shebeis)) {
				 shebeis = "空";
			 }
			 trYajiangStatistics.setYjsbbh(shebeis);
		 }
		 QueryWrapper<TrYajiangStatistics> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangStatistics, req.getParameterMap());
		 queryWrapper.select("sum(tr_yajaing_oversum) as trYajaingOversum");
		 if(null!=orgCode){
			 queryWrapper.likeRight("sys_org_code",orgCode);
		 }
		 TrYajiangStatistics one = trYajiangStatisticsService.getOne(queryWrapper);

		 QueryWrapper<TrYajiangStatistics> queryWrapper2 = QueryGenerator.initQueryWrapper(trYajiangStatistics, req.getParameterMap());
		 queryWrapper2.select("sum(tr_yajaing_oversum) as trYajaingOversum");
		 queryWrapper2.lambda().apply(" DATE_FORMAT(yjsj,'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m') ");
		 if(null!=orgCode) {
			 queryWrapper2.likeRight("sys_org_code",orgCode);
		 }
		 TrYajiangStatistics one2 = trYajiangStatisticsService.getOne(queryWrapper2);
		 YjstatisticsVO vo = new YjstatisticsVO();
		 if(null == one || null == one.getTrYajaingOversum()){
			 vo.setOversum(0);
		 }else {
			 vo.setOversum(one.getTrYajaingOversum());
		 }
		 if(null == one2 || null == one2.getTrYajaingOversum()){
			 vo.setMonthOverSum(0);
		 }else {
			 vo.setMonthOverSum(one2.getTrYajaingOversum());
		 }
		 return vo;
	 }
	/**
	 *   添加
	 *
	 * @param trYajiangStatistics
	 * @return
	 */
	@AutoLog(value = "压浆统计表-添加")
	@ApiOperation(value="压浆统计表-添加", notes="压浆统计表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrYajiangStatistics trYajiangStatistics) {
		trYajiangStatisticsService.save(trYajiangStatistics);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param trYajiangStatistics
	 * @return
	 */
	@AutoLog(value = "压浆统计表-编辑")
	@ApiOperation(value="压浆统计表-编辑", notes="压浆统计表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrYajiangStatistics trYajiangStatistics) {
		trYajiangStatisticsService.updateById(trYajiangStatistics);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "压浆统计表-通过id删除")
	@ApiOperation(value="压浆统计表-通过id删除", notes="压浆统计表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trYajiangStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "压浆统计表-批量删除")
	@ApiOperation(value="压浆统计表-批量删除", notes="压浆统计表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trYajiangStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "压浆统计表-通过id查询")
	@ApiOperation(value="压浆统计表-通过id查询", notes="压浆统计表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrYajiangStatistics trYajiangStatistics = trYajiangStatisticsService.getById(id);
		if(trYajiangStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trYajiangStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trYajiangStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrYajiangStatistics trYajiangStatistics) {
        return super.exportXls(request, trYajiangStatistics, TrYajiangStatistics.class, "压浆统计表");
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
        return super.importExcel(request, response, TrYajiangStatistics.class);
    }

}
