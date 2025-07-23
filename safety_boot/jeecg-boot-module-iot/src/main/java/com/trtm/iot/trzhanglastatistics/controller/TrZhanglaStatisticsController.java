package com.trtm.iot.trzhanglastatistics.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglastatistics.vo.ZlstatisticsVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.trzhanglastatistics.entity.TrZhanglaStatistics;
import com.trtm.iot.trzhanglastatistics.service.ITrZhanglaStatisticsService;

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
 * @Description: 张拉统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
@Api(tags="张拉统计表")
@RestController
@RequestMapping("/trzhanglastatistics/trZhanglaStatistics")
@Slf4j
public class TrZhanglaStatisticsController extends JeecgController<TrZhanglaStatistics, ITrZhanglaStatisticsService> {
	@Autowired
	private ITrZhanglaStatisticsService trZhanglaStatisticsService;

	@Autowired
	private IShebeiInfoService shebeiInfoService;

	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param trZhanglaStatistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "张拉统计表-分页列表查询")
	@ApiOperation(value="张拉统计表-分页列表查询", notes="张拉统计表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrZhanglaStatistics trZhanglaStatistics,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrZhanglaStatistics> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaStatistics, req.getParameterMap());
		Page<TrZhanglaStatistics> page = new Page<TrZhanglaStatistics>(pageNo, pageSize);
		IPage<TrZhanglaStatistics> pageList = trZhanglaStatisticsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 张拉统计
	  *
	  * @param trZhanglaStatistics
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "张拉统计表查询")
	 @ApiOperation(value="张拉统计表查询", notes="张拉统计查询")
	 @GetMapping(value = "/zlwarn")
	 public Result<?> queryList(TrZhanglaStatistics trZhanglaStatistics,
									HttpServletRequest req,String sys_depart_orgcode) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 String[] split = shebei.split(",");
		 List<String> lists = new ArrayList();
		 for (int i = 0;i < split.length; i++){
			 lists.add(split[i]);
		 }
		 List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(lists, 9);
		 String shebeis = null;
		 if(shebeiInfos.size()>0){
			 shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
		 }
		 if (trZhanglaStatistics.getShebeibianhao() == null) {
			 if ("null".equals(shebeis)) {
				 shebeis = "空";
			 }else {
				 trZhanglaStatistics.setShebeibianhao(shebeis);
			 }
		 }

		 QueryWrapper<TrZhanglaStatistics> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaStatistics, req.getParameterMap());
		 queryWrapper.select("sum(tr_zhangla_oversum) as trZhanglaOversum");
		 if(null!=sys_depart_orgcode){
			 queryWrapper.likeRight("sys_org_code",sys_depart_orgcode);
		 }
		 TrZhanglaStatistics one = trZhanglaStatisticsService.getOne(queryWrapper);

		 QueryWrapper<TrZhanglaStatistics> queryWrapper2 = QueryGenerator.initQueryWrapper(trZhanglaStatistics, req.getParameterMap());
		 queryWrapper2.select("sum(tr_zhangla_oversum) as trZhanglaOversum");
		 queryWrapper2.lambda().apply(" DATE_FORMAT(sqsj,'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m')");
		 if(null!=sys_depart_orgcode){
			 queryWrapper.likeRight("sys_org_code",sys_depart_orgcode);
		 }
		 TrZhanglaStatistics one2 = trZhanglaStatisticsService.getOne(queryWrapper2);

		 ZlstatisticsVO vo = new ZlstatisticsVO();
		 if(null == one || null == one.getTrZhanglaOversum()){
			vo.setOversum(0);
		 }else {
			 vo.setOversum(one.getTrZhanglaOversum());
		 }
		 if(null == one2 || null == one2.getTrZhanglaOversum()){
			 vo.setMonthOverSum(0);
		 }else {
			 vo.setMonthOverSum(one2.getTrZhanglaOversum());
		 }
		 return Result.OK(vo);
	 }

	/**
	 *   添加
	 *
	 * @param trZhanglaStatistics
	 * @return
	 */
	@AutoLog(value = "张拉统计表-添加")
	@ApiOperation(value="张拉统计表-添加", notes="张拉统计表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrZhanglaStatistics trZhanglaStatistics) {
		trZhanglaStatisticsService.save(trZhanglaStatistics);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param trZhanglaStatistics
	 * @return
	 */
	@AutoLog(value = "张拉统计表-编辑")
	@ApiOperation(value="张拉统计表-编辑", notes="张拉统计表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrZhanglaStatistics trZhanglaStatistics) {
		trZhanglaStatisticsService.updateById(trZhanglaStatistics);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉统计表-通过id删除")
	@ApiOperation(value="张拉统计表-通过id删除", notes="张拉统计表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trZhanglaStatisticsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "张拉统计表-批量删除")
	@ApiOperation(value="张拉统计表-批量删除", notes="张拉统计表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trZhanglaStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉统计表-通过id查询")
	@ApiOperation(value="张拉统计表-通过id查询", notes="张拉统计表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrZhanglaStatistics trZhanglaStatistics = trZhanglaStatisticsService.getById(id);
		if(trZhanglaStatistics==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trZhanglaStatistics);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trZhanglaStatistics
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrZhanglaStatistics trZhanglaStatistics) {
        return super.exportXls(request, trZhanglaStatistics, TrZhanglaStatistics.class, "张拉统计表");
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
        return super.importExcel(request, response, TrZhanglaStatistics.class);
    }

}
