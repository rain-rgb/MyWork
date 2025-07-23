package com.trtm.iot.tpny.controller;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.clgl.entity.FrontDeviceHistorydata;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;

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

import static com.trtm.iot.util.GPSUtil.*;
import static com.trtm.iot.util.GPSUtil.retain6;

/**
 * @Description: 摊铺碾压数据监测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Api(tags="摊铺碾压数据监测主表")
@RestController
@RequestMapping("/tpny/frontDeviceRealdata")
@Slf4j
public class FrontDeviceRealdataController extends JeecgController<FrontDeviceRealdata, IFrontDeviceRealdataService> {
	@Autowired
	private IFrontDeviceRealdataService frontDeviceRealdataService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IShebeiInfoService shebeiInfoService;
	/**
	 * 分页列表查询
	 *
	 * @param frontDeviceRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "摊铺碾压数据监测主表-分页列表查询")
	@ApiOperation(value="摊铺碾压数据监测主表-分页列表查询", notes="摊铺碾压数据监测主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FrontDeviceRealdata frontDeviceRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if(frontDeviceRealdata.getShebeiNo()==null) {
			if (shebei != null) {
				frontDeviceRealdata.setShebeiNo(shebei);
			}
		}
		QueryWrapper<FrontDeviceRealdata> queryWrapper = QueryGenerator.initQueryWrapper(frontDeviceRealdata, req.getParameterMap());
		queryWrapper.inSql("device_type","select device_type from front_device_realdata where device_type='A' or device_type='B'");
		Page<FrontDeviceRealdata> page = new Page<FrontDeviceRealdata>(pageNo, pageSize);
		IPage<FrontDeviceRealdata> pageList = frontDeviceRealdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 /**
	  * 分页列表查询
	  *
	  * @param frontDeviceRealdata
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "摊铺碾压数据监测主表-分页列表查询")
	 @ApiOperation(value="摊铺碾压数据监测主表-分页列表查询", notes="摊铺碾压数据监测主表-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(FrontDeviceRealdata frontDeviceRealdata,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 //查询到他的设备编号
		 if (frontDeviceRealdata.getShebeiNo() == null) {
			 if (!"null".equals(shebei)) {
				 frontDeviceRealdata.setShebeiNo(shebei);
			 }else {
				 frontDeviceRealdata.setShebeiNo("空");
			 }
		 }
		 QueryWrapper<FrontDeviceRealdata> queryWrapper = QueryGenerator.initQueryWrapper(frontDeviceRealdata, req.getParameterMap());
		 queryWrapper.inSql("device_type","select device_type from front_device_realdata where device_type='A' or device_type='B'");
		 List<FrontDeviceRealdata> pageList = frontDeviceRealdataService.list( queryWrapper);
		 for(FrontDeviceRealdata frontDeviceRealdata1 : pageList){
			 String shebeiNo = frontDeviceRealdata1.getShebeiNo();
			 ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
			 String sbname = selectshebeione.getSbname();
			 frontDeviceRealdata1.setShebeiNo(sbname);
			 if (frontDeviceRealdata1.getLongitude() != null && frontDeviceRealdata1.getLatitude() != null){
				 double lon = Double.parseDouble(String.valueOf(frontDeviceRealdata1.getLongitude()));
				 double lat = Double.parseDouble(String.valueOf(frontDeviceRealdata1.getLatitude()));
				 double lat1 = formatLnglat(lat);
				 double lon1 = formatLnglat(lon);
				 double [] zuobiao = gps84_To_Gcj02(lat1,lon1);
				 double lat2 = retain6(zuobiao[0]);
				 double lon2 = retain6(zuobiao[1]);
				 frontDeviceRealdata1.setLongitude(lon2);
				 frontDeviceRealdata1.setLatitude(lat2);
			 }
		 }
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param frontDeviceRealdata
	 * @return
	 */
	@AutoLog(value = "摊铺碾压数据监测主表-添加")
	@ApiOperation(value="摊铺碾压数据监测主表-添加", notes="摊铺碾压数据监测主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FrontDeviceRealdata frontDeviceRealdata) {
		frontDeviceRealdataService.save(frontDeviceRealdata);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param frontDeviceRealdata
	 * @return
	 */
	@AutoLog(value = "摊铺碾压数据监测主表-编辑")
	@ApiOperation(value="摊铺碾压数据监测主表-编辑", notes="摊铺碾压数据监测主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FrontDeviceRealdata frontDeviceRealdata) {
		frontDeviceRealdataService.updateById(frontDeviceRealdata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "摊铺碾压数据监测主表-通过id删除")
	@ApiOperation(value="摊铺碾压数据监测主表-通过id删除", notes="摊铺碾压数据监测主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		frontDeviceRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "摊铺碾压数据监测主表-批量删除")
	@ApiOperation(value="摊铺碾压数据监测主表-批量删除", notes="摊铺碾压数据监测主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.frontDeviceRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "摊铺碾压数据监测主表-通过id查询")
	@ApiOperation(value="摊铺碾压数据监测主表-通过id查询", notes="摊铺碾压数据监测主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FrontDeviceRealdata frontDeviceRealdata = frontDeviceRealdataService.getById(id);
		if(frontDeviceRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(frontDeviceRealdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param frontDeviceRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FrontDeviceRealdata frontDeviceRealdata) {
        return super.exportXls(request, frontDeviceRealdata, FrontDeviceRealdata.class, "摊铺碾压数据监测主表");
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
        return super.importExcel(request, response, FrontDeviceRealdata.class);
    }

}
