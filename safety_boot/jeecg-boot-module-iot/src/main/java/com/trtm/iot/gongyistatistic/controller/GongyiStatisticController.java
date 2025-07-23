package com.trtm.iot.gongyistatistic.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.gongyistatistic.entity.GongyiStatistic;
import com.trtm.iot.gongyistatistic.service.IGongyiStatisticService;

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
 * @Description: gongyi_statistic
 * @Author: jeecg-boot
 * @Date:   2023-03-06
 * @Version: V1.0
 */
@Api(tags="gongyi_statistic")
@RestController
@RequestMapping("/gongyistatistic/gongyiStatistic")
@Slf4j
public class GongyiStatisticController extends JeecgController<GongyiStatistic, IGongyiStatisticService> {
	@Autowired
	private IGongyiStatisticService gongyiStatisticService;

	/**
	 * 分页列表查询
	 *
	 * @param gongyiStatistic
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "gongyi_statistic-分页列表查询")
	@ApiOperation(value="gongyi_statistic-分页列表查询", notes="gongyi_statistic-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GongyiStatistic gongyiStatistic,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GongyiStatistic> queryWrapper = QueryGenerator.initQueryWrapper(gongyiStatistic, req.getParameterMap());
		Page<GongyiStatistic> page = new Page<GongyiStatistic>(pageNo, pageSize);
		IPage<GongyiStatistic> pageList = gongyiStatisticService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 高信新增功能
	  * @param gongyiStatistic
	  * @return
	  */
	 @AutoLog(value = "gongyi_statistic-分页列表查询")
	 @ApiOperation(value="gongyi_statistic-分页列表查询", notes="gongyi_statistic-分页列表查询")
	 @GetMapping(value = "/listGx")
	 public Result<?> queryPageListGx(GongyiStatistic gongyiStatistic) {

		 List<Map<String, Object>> sj = new ArrayList<>();
		 if (gongyiStatistic.getProntzhi() != null && gongyiStatistic.getProntTime() != null){
			 List<GongyiStatistic> countSum = gongyiStatisticService.selectCountSum(gongyiStatistic.getProntzhi(),gongyiStatistic.getProntTime());
			 if (countSum.size() > 0){
				 String yujinglv = null;
				 for (GongyiStatistic gongyi :countSum){
					 Map<String, Object> map1 = new HashMap<>();
					 String orgcodeName = gongyi.getOrgcodeName();
//					 if (!orgcodeName.equals("瑞苍")){
//						 continue;
//					 }
					 String zongshu = gongyi.getZongshu();
					 String yujings = gongyi.getYujings();
					 String bihes = gongyi.getBihes();

					 if (zongshu.equals("0.0")){
						 yujinglv = "-";
					 } else if (yujings.equals("0.0")){
						 yujinglv = "0.00";
					 }else {
						 yujinglv = getPercentStrss((int) Double.parseDouble(yujings),(int) Double.parseDouble(zongshu));//预警率
					 }
					 map1.put("orgcodeName", orgcodeName);
					 map1.put("yujinglv", yujinglv);

					 //统计表详情数据
					 List<Map<String, Object>> sj1 = new ArrayList<>();
					 List<GongyiStatistic> orgcodesum = gongyiStatisticService.selectByOrgcodeName(gongyiStatistic.getProntzhi(),orgcodeName,gongyiStatistic.getProntTime());
					 for(GongyiStatistic o :orgcodesum){
						 Map<String, Object> map2 = new HashMap<>();
						 String prontTime = o.getProntTime();
						 String zongshu1 = o.getZongshu();
						 String yujings1 = o.getYujings();
						 if (zongshu1.equals("0.0")){
							 yujinglv = "-";
						 } else if (yujings1.equals("0.0")){
							 yujinglv = "0.00";
						 }else {
							 yujinglv = getPercentStrss((int) Double.parseDouble(yujings1),(int) Double.parseDouble(zongshu1));//预警率
						 }
						 map2.put("prontTime", prontTime);
						 map2.put("yujinglv", yujinglv);
						 sj1.add(map2);
					 }
					 map1.put("detail", sj1);
					 sj.add(map1);
				 }
			 }
		 }
		 return Result.OK(sj);
	 }


	 /**
	  * 高信新增功能2
	  * @param gongyiStatistic
	  * @return
	  */
	 @AutoLog(value = "gongyi_statistic-分页列表查询")
	 @ApiOperation(value="gongyi_statistic-分页列表查询", notes="gongyi_statistic-分页列表查询")
	 @GetMapping(value = "/listGxgy")
	 public Result<?> queryPageListGxgy(GongyiStatistic gongyiStatistic) {

		 List<Map<String, Object>> sj = new ArrayList<>();
		 if (gongyiStatistic.getSysOrgCode() != null && gongyiStatistic.getProntTime() != null){
			 List<GongyiStatistic> countSum = gongyiStatisticService.selectCountSums(gongyiStatistic.getSysOrgCode(),gongyiStatistic.getProntTime());
			 if (countSum.size() > 0){
				 String yujinglv = null;
				 for (GongyiStatistic gongyi :countSum){
					 Map<String, Object> map1 = new HashMap<>();
					 Integer prontzhi = gongyi.getProntzhi();
					 String shebeiNo = gongyi.getShebeiNo();
					 String zongshu = gongyi.getZongshu();
					 String yujings = gongyi.getYujings();
					 String bihes = gongyi.getBihes();

					 if (zongshu.equals("0.0")){
						 yujinglv = "-";
					 } else if (yujings.equals("0.0")){
						 yujinglv = "0.0";
					 }else {
						 yujinglv = getPercentStrss((int) Double.parseDouble(yujings),(int) Double.parseDouble(zongshu));//预警率
					 }
					 map1.put("prontzhi", shebeiNo);
					 map1.put("yujinglv", yujinglv);

					 //统计表详情数据
					 List<Map<String, Object>> sj1 = new ArrayList<>();
					 List<GongyiStatistic> orgcodesum = gongyiStatisticService.selectByOrgcodeNames(prontzhi,gongyiStatistic.getSysOrgCode(),gongyiStatistic.getProntTime());
					 for(GongyiStatistic o :orgcodesum){
						 Map<String, Object> map2 = new HashMap<>();
						 String prontTime = o.getProntTime();
						 String zongshu1 = o.getZongshu();
						 String yujings1 = o.getYujings();
						 if (zongshu1.equals("0.0")){
							 yujinglv = "-";
						 } else if (yujings1.equals("0.0")){
							 yujinglv = "0.0";
						 }else {
							 yujinglv = getPercentStrss((int) Double.parseDouble(yujings1),(int) Double.parseDouble(zongshu1));//预警率
						 }
						 map2.put("prontTime", prontTime);
						 map2.put("yujinglv", yujinglv);
						 sj1.add(map2);
					 }
					 map1.put("detail", sj1);
					 sj.add(map1);
				 }
			 }
		 }
		 return Result.OK(sj);
	 }

	 /**
	  * 高信新增功能2 项目级
	  * @param gongyiStatistic
	  * @return
	  */
	 @AutoLog(value = "gongyi_statistic-分页列表查询")
	 @ApiOperation(value="gongyi_statistic-分页列表查询", notes="gongyi_statistic-分页列表查询")
	 @GetMapping(value = "/listGxgyxmj")
	 public Result<?> queryPageListGxgyxmj(GongyiStatistic gongyiStatistic) {
		 List<Map<String, Object>> sj = new ArrayList<>();
		 if (gongyiStatistic.getSysOrgCode() != null){
			 List<GongyiStatistic> countSum = gongyiStatisticService.selectCountSumss(gongyiStatistic.getSysOrgCode());
			 if (countSum.size() > 0){
				 Double yujinglv = 0.00;
				 Double bihelv = 0.00;
				 for (GongyiStatistic gongyi :countSum){
					 Map<String, Object> map1 = new HashMap<>();
					 String zongshu = gongyi.getZongshu();
					 String yujings = gongyi.getYujings();
					 String bihes = gongyi.getBihes();

					 if (zongshu.equals("0.0")){
						 yujinglv = 0.00;
					 } else if (yujings.equals("0.0")){
						 yujinglv = 0.00;
					 }else {
						 yujinglv = getPercentStrs((int) Double.parseDouble(yujings),(int) Double.parseDouble(zongshu));//预警率
						 bihelv = getPercentStrs((int) Double.parseDouble(bihes),(int) Double.parseDouble(yujings));//预警率
						 if (yujinglv > 100){
							 yujinglv = 100.00;
						 }
						 if (bihelv > 100){
							 bihelv = 100.00;
						 }
					 }
					 map1.put("yujinglv", yujinglv);
					 map1.put("bihelv", bihelv);

					 sj.add(map1);
				 }
			 }
		 }
		 return Result.OK(sj);
	 }
	 /**
	  * 得到百分比的字符串，比如传参 1,3，返回33.33
	  */
	 private Double getPercentStrs(Integer diff, Integer sum) {
		 DecimalFormat df = new DecimalFormat("0.00");//格式化小数
		 Double d = 0.00;
		 if (sum == 0) {
			 return d;
		 }
		 float num = (float) (diff * 100) / sum;
		 String str = df.format(num);
		 double v = Double.parseDouble(str);
		 return v;
	 }

	 /**
	  * 得到百分比的字符串，比如传参 1,3，返回33.33
	  */
	 private String getPercentStrss(Integer diff, Integer sum) {
		 DecimalFormat df = new DecimalFormat("0.0");//格式化小数
		 String d = "-";
		 if (sum == 0) {
			 return d;
		 }
		 float num = (float) (diff * 100) / sum;
		 String str = df.format(num);
		 return str;
	 }
	/**
	 *   添加
	 *
	 * @param gongyiStatistic
	 * @return
	 */
	@AutoLog(value = "gongyi_statistic-添加")
	@ApiOperation(value="gongyi_statistic-添加", notes="gongyi_statistic-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GongyiStatistic gongyiStatistic) {
		gongyiStatisticService.save(gongyiStatistic);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param gongyiStatistic
	 * @return
	 */
	@AutoLog(value = "gongyi_statistic-编辑")
	@ApiOperation(value="gongyi_statistic-编辑", notes="gongyi_statistic-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GongyiStatistic gongyiStatistic) {
		gongyiStatisticService.updateById(gongyiStatistic);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "gongyi_statistic-通过id删除")
	@ApiOperation(value="gongyi_statistic-通过id删除", notes="gongyi_statistic-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		gongyiStatisticService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "gongyi_statistic-批量删除")
	@ApiOperation(value="gongyi_statistic-批量删除", notes="gongyi_statistic-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gongyiStatisticService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "gongyi_statistic-通过id查询")
	@ApiOperation(value="gongyi_statistic-通过id查询", notes="gongyi_statistic-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		GongyiStatistic gongyiStatistic = gongyiStatisticService.getById(id);
		if(gongyiStatistic==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(gongyiStatistic);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gongyiStatistic
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GongyiStatistic gongyiStatistic) {
        return super.exportXls(request, gongyiStatistic, GongyiStatistic.class, "gongyi_statistic");
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
        return super.importExcel(request, response, GongyiStatistic.class);
    }

}
