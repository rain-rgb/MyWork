package com.trtm.iot.zhydreal.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wbshebeihistory.entity.WbshebeiHistory;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zhydreal.entity.DeviceElectricRealdata;
import com.trtm.iot.zhydreal.service.IDeviceElectricRealdataService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: 智慧用电实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-16
 * @Version: V1.0
 */
@Api(tags="智慧用电实时数据表")
@RestController
@RequestMapping("/zhydreal/deviceElectricRealdata")
@Slf4j
public class DeviceElectricRealdataController extends JeecgController<DeviceElectricRealdata, IDeviceElectricRealdataService> {
	@Autowired
	private IDeviceElectricRealdataService deviceElectricRealdataService;
	 @Autowired
	 private IDeviceElectricHistorydataService deviceElectricHistorydataService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param deviceElectricRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "智慧用电实时数据表-分页列表查询")
	@ApiOperation(value="智慧用电实时数据表-分页列表查询", notes="智慧用电实时数据表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceElectricRealdata deviceElectricRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (deviceElectricRealdata.getImei() == null) {
			if (!"null".equals(shebei)) {
				deviceElectricRealdata.setImei(shebei);
			}else {
				deviceElectricRealdata.setImei("空");
			}
		}
		QueryWrapper<DeviceElectricRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceElectricRealdata, req.getParameterMap());
		Page<DeviceElectricRealdata> page = new Page<DeviceElectricRealdata>(pageNo, pageSize);
		IPage<DeviceElectricRealdata> pageList = deviceElectricRealdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param deviceElectricRealdata
	 * @return
	 */
	@AutoLog(value = "智慧用电实时数据表-添加")
	@ApiOperation(value="智慧用电实时数据表-添加", notes="智慧用电实时数据表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceElectricRealdata deviceElectricRealdata) {
		deviceElectricRealdataService.save(deviceElectricRealdata);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加(对外开放)
	  *
	  * @param deviceElectricRealdata
	  * @return
	  */
	 @AutoLog(value = "智慧用电实时数据表-添加")
	 @ApiOperation(value="智慧用电实时数据表-添加", notes="智慧用电实时数据表-添加")
	 @PostMapping(value = "/addOpen")
	 public Result<?> addOpen(@RequestBody DeviceElectricRealdata deviceElectricRealdata) {
	 	if (!StringUtil.isEmpty(deviceElectricRealdata.getImei())){
	 		QueryWrapper<DeviceElectricRealdata> queryWrapper = new QueryWrapper<>();
	 		queryWrapper.eq("imei",deviceElectricRealdata.getImei());
			DeviceElectricRealdata one = deviceElectricRealdataService.getOne(queryWrapper);
			if (one!=null) {
				deviceElectricRealdata.setId(one.getId());
				deviceElectricRealdataService.updateById(deviceElectricRealdata);
			}else {
				deviceElectricRealdataService.save(deviceElectricRealdata);
			}
			DeviceElectricHistorydata deviceElectricHistorydata = new DeviceElectricHistorydata();
			BeanUtils.copyProperties(deviceElectricRealdata, deviceElectricHistorydata);
			deviceElectricHistorydataService.save(deviceElectricHistorydata);
			return  Result.OK("添加成功！");
		}else {
			return Result.OK("获取数据失败！");
		}
	 }

	/**
	 *  编辑
	 *
	 * @param deviceElectricRealdata
	 * @return
	 */
	@AutoLog(value = "智慧用电实时数据表-编辑")
	@ApiOperation(value="智慧用电实时数据表-编辑", notes="智慧用电实时数据表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceElectricRealdata deviceElectricRealdata) {
		deviceElectricRealdataService.updateById(deviceElectricRealdata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "智慧用电实时数据表-通过id删除")
	@ApiOperation(value="智慧用电实时数据表-通过id删除", notes="智慧用电实时数据表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceElectricRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "智慧用电实时数据表-批量删除")
	@ApiOperation(value="智慧用电实时数据表-批量删除", notes="智慧用电实时数据表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceElectricRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "智慧用电实时数据表-通过id查询")
	@ApiOperation(value="智慧用电实时数据表-通过id查询", notes="智慧用电实时数据表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceElectricRealdata deviceElectricRealdata = deviceElectricRealdataService.getById(id);
		if(deviceElectricRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceElectricRealdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceElectricRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceElectricRealdata deviceElectricRealdata) {
        return super.exportXls(request, deviceElectricRealdata, DeviceElectricRealdata.class, "智慧用电实时数据表");
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
        return super.importExcel(request, response, DeviceElectricRealdata.class);
    }

}
