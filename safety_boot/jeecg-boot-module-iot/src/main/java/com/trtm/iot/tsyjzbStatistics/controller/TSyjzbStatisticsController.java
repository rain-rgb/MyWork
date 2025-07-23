package com.trtm.iot.tsyjzbStatistics.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tsyjzbStatistics.vo.statisticsVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.tsyjzbStatistics.entity.TSyjzbStatistics;
import com.trtm.iot.tsyjzbStatistics.service.ITSyjzbStatisticsService;

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
 * @Description: t_syjzb_statistics
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
@Api(tags="t_syjzb_statistics")
@RestController
@RequestMapping("/tsyjzbStatistics/tSyjzbStatistics")
@Slf4j
public class TSyjzbStatisticsController extends JeecgController<TSyjzbStatistics, ITSyjzbStatisticsService> {
	@Autowired
	private ITSyjzbStatisticsService tSyjzbStatisticsService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param tSyjzbStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "t_syjzb_statistics-分页列表查询")
	@ApiOperation(value="t_syjzb_statistics-分页列表查询", notes="t_syjzb_statistics-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TSyjzbStatistics tSyjzbStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TSyjzbStatistics> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzbStatistics, req.getParameterMap());
		Page<TSyjzbStatistics> page = new Page<TSyjzbStatistics>(pageNo, pageSize);
		IPage<TSyjzbStatistics> pageList = tSyjzbStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param tSyjzbStatistics
	 * @return
	 */
	@AutoLog(value = "t_syjzb_statistics-添加")
	@ApiOperation(value="t_syjzb_statistics-添加", notes="t_syjzb_statistics-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TSyjzbStatistics tSyjzbStatistics) {
		tSyjzbStatisticsService.save(tSyjzbStatistics);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param tSyjzbStatistics
	 * @return
	 */
	@AutoLog(value = "t_syjzb_statistics-编辑")
	@ApiOperation(value="t_syjzb_statistics-编辑", notes="t_syjzb_statistics-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TSyjzbStatistics tSyjzbStatistics) {
		tSyjzbStatisticsService.updateById(tSyjzbStatistics);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "t_syjzb_statistics-通过id删除")
	@ApiOperation(value="t_syjzb_statistics-通过id删除", notes="t_syjzb_statistics-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tSyjzbStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "t_syjzb_statistics-批量删除")
	@ApiOperation(value="t_syjzb_statistics-批量删除", notes="t_syjzb_statistics-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tSyjzbStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "t_syjzb_statistics-通过id查询")
	@ApiOperation(value="t_syjzb_statistics-通过id查询", notes="t_syjzb_statistics-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TSyjzbStatistics tSyjzbStatistics = tSyjzbStatisticsService.getById(id);
		if(tSyjzbStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(tSyjzbStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param tSyjzbStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TSyjzbStatistics tSyjzbStatistics) {
        return super.exportXls(request, tSyjzbStatistics, TSyjzbStatistics.class, "t_syjzb_statistics");
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
        return super.importExcel(request, response, TSyjzbStatistics.class);
    }

	 @AutoLog(value = "yljwarn-压力机预警查询")
	 @ApiOperation(value = "yljwarn-压力机预警查询", notes = "yljwarn-压力机预警查询")
	 @GetMapping("yljwarn")
	 public Result<?> yljwarn(TSyjzbStatistics tSyjzbStatistics,
							  HttpServletRequest req,
							  String sys_depart_orgcode){
    	statisticsVO vo = syjwarn(tSyjzbStatistics,req,4,sys_depart_orgcode);
    	return Result.OK(vo);
	 }
	 @AutoLog(value = "wnjwarn-万能机预警查询")
	 @ApiOperation(value = "wnjwarn-万能机预警查询", notes = "wnjwarn-万能机预警查询")
	 @GetMapping("wnjwarn")
	 public Result<?> wnjwarn(TSyjzbStatistics tSyjzbStatistics,
							  HttpServletRequest req,
							  String sys_depart_orgcode){
		 statisticsVO vo = syjwarn(tSyjzbStatistics,req,3,sys_depart_orgcode);
		 return Result.OK(vo);
	 }
	 @AutoLog(value = "jqjwarn-架桥机预警查询")
	 @ApiOperation(value = "jqjwarn-架桥机预警查询", notes = "jqjwarn-架桥机预警查询")
	 @GetMapping("jqjwarn")
	 public Result<?> jqjwarn(TSyjzbStatistics tSyjzbStatistics,
							  HttpServletRequest req,
							  String sys_depart_orgcode){
		 statisticsVO vo = syjwarn(tSyjzbStatistics,req,23,sys_depart_orgcode);
		 return Result.OK(vo);
	 }

	/*
	* 试验机预警统计查询
	*/
	 public statisticsVO syjwarn(TSyjzbStatistics tSyjzbStatistics,
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
		 if (tSyjzbStatistics.getSbbh() == null) {
			 if ("null".equals(shebeis)) {
				 shebeis = "空";
			 }
			 tSyjzbStatistics.setSbbh(shebeis);
		 }
		 QueryWrapper<TSyjzbStatistics> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzbStatistics, req.getParameterMap());
		 queryWrapper.select("sum(all_overproof_dish) as allOverproofDish");
		 if(null!=orgCode){
		 	queryWrapper.likeRight("sys_org_code",orgCode);
		 }
		 TSyjzbStatistics one = tSyjzbStatisticsService.getOne(queryWrapper);

		 QueryWrapper<TSyjzbStatistics> queryWrapper2 = QueryGenerator.initQueryWrapper(tSyjzbStatistics, req.getParameterMap());
		 queryWrapper2.select("sum(all_overproof_dish) as allOverproofDish");
		 queryWrapper2.lambda().apply(" DATE_FORMAT(SYRQ,'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m') ");
		 if(null!=orgCode) {
			 queryWrapper2.likeRight("sys_org_code",orgCode);
		 }
			 TSyjzbStatistics one2 = tSyjzbStatisticsService.getOne(queryWrapper2);
		 statisticsVO vo =new statisticsVO();
		 if(null==one ||null == one.getAllOverproofDish()){
//				 one.setAllOverproofDish(0);
			 vo.setAllWarn(0);
		 }else{
			 vo.setAllWarn(one.getAllOverproofDish());
		 }
		 if(null==one2 || null == one2.getAllOverproofDish() ){
//				 one2.setAllOverproofDish(0);
			 vo.setMWarn(0);
		 }else{
			 vo.setMWarn(one2.getAllOverproofDish());
		 }
		 return vo;
	 }

}
