package com.trtm.iot.devicemixpileupdata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicemixpileupdata.entity.DeviceMixpileUpdata;
import com.trtm.iot.devicemixpileupdata.service.IDeviceMixpileUpdataService;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: device_mixpile_updata
 * @Author: jeecg-boot
 * @Date:   2021-12-14
 * @Version: V1.0
 */
@Api(tags="device_mixpile_updata")
@RestController
@RequestMapping("/devicemixpileupdata/deviceMixpileUpdata")
@Slf4j
public class DeviceMixpileUpdataController extends JeecgController<DeviceMixpileUpdata, IDeviceMixpileUpdataService> {
	@Autowired
	private IDeviceMixpileUpdataService deviceMixpileUpdataService;

	 @Autowired
	 private RedisUtil redisUtil;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceMixpileUpdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_mixpile_updata-分页列表查询")
	@ApiOperation(value="device_mixpile_updata-分页列表查询", notes="device_mixpile_updata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceMixpileUpdata deviceMixpileUpdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (deviceMixpileUpdata.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				deviceMixpileUpdata.setShebeino(shebei);
			} else {
				deviceMixpileUpdata.setShebeino("空");
			}
		}
		QueryWrapper<DeviceMixpileUpdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileUpdata, req.getParameterMap());
		Page<DeviceMixpileUpdata> page = new Page<DeviceMixpileUpdata>(pageNo, pageSize);
		IPage<DeviceMixpileUpdata> pageList = deviceMixpileUpdataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceMixpileUpdata
	 * @return
	 */
	@AutoLog(value = "device_mixpile_updata-添加")
	@ApiOperation(value="device_mixpile_updata-添加", notes="device_mixpile_updata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceMixpileUpdata deviceMixpileUpdata) {
		deviceMixpileUpdataService.save(deviceMixpileUpdata);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加
	  *
	  * @param deviceMixpileUpdata
	  * @return
	  */
	 @AutoLog(value = "device_mixpile_updata-添加")
	 @ApiOperation(value="device_mixpile_updata-添加", notes="device_mixpile_updata-添加")
	 @PostMapping(value = "/addOther")
	 public Result<?> addOther(@RequestBody DeviceMixpileUpdata deviceMixpileUpdata) {
	 	if(StringUtil.isNotEmpty(deviceMixpileUpdata.getShebeino())){
			deviceMixpileUpdataService.save(deviceMixpileUpdata);
			return Result.OK("添加成功！");
		}else{
			return Result.error("设备编号为空请重新上传！",deviceMixpileUpdata);
		}

	 }
	
	/**
	 *  编辑
	 *
	 * @param deviceMixpileUpdata
	 * @return
	 */
	@AutoLog(value = "device_mixpile_updata-编辑")
	@ApiOperation(value="device_mixpile_updata-编辑", notes="device_mixpile_updata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceMixpileUpdata deviceMixpileUpdata) {
		deviceMixpileUpdataService.updateById(deviceMixpileUpdata);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_mixpile_updata-通过id删除")
	@ApiOperation(value="device_mixpile_updata-通过id删除", notes="device_mixpile_updata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceMixpileUpdataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_mixpile_updata-批量删除")
	@ApiOperation(value="device_mixpile_updata-批量删除", notes="device_mixpile_updata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceMixpileUpdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_mixpile_updata-通过id查询")
	@ApiOperation(value="device_mixpile_updata-通过id查询", notes="device_mixpile_updata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceMixpileUpdata deviceMixpileUpdata = deviceMixpileUpdataService.getById(id);
		if(deviceMixpileUpdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceMixpileUpdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceMixpileUpdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileUpdata deviceMixpileUpdata) {
        return super.exportXls(request, deviceMixpileUpdata, DeviceMixpileUpdata.class, "device_mixpile_updata");
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
        return super.importExcel(request, response, DeviceMixpileUpdata.class);
    }

}
