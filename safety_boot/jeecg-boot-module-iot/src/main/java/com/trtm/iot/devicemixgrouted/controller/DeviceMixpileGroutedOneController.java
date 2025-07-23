package com.trtm.iot.devicemixgrouted.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.devicemixgrouted.entity.DeviceMixpileGroutedPart;
import com.trtm.iot.devicemixgrouted.service.IDeviceMixpileGroutedPartService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.devicemixgrouted.entity.DeviceMixpileGroutedOne;
import com.trtm.iot.devicemixgrouted.service.IDeviceMixpileGroutedOneService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 灌注桩详情
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Api(tags="灌注桩详情")
@RestController
@RequestMapping("/grouted/deviceMixpileGroutedOne")
@Slf4j
public class DeviceMixpileGroutedOneController extends JeecgController<DeviceMixpileGroutedOne, IDeviceMixpileGroutedOneService> {
	@Autowired
	private IDeviceMixpileGroutedOneService deviceMixpileGroutedOneService;

	 @Autowired
	 private RedisUtil redisUtil;

	 @Autowired
	 private IDeviceMixpileGroutedPartService deviceMixpileGroutedPartService;
	/**
	 * 分页列表查询
	 *
	 * @param deviceMixpileGroutedOne
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "灌注桩详情-分页列表查询")
	@ApiOperation(value="灌注桩详情-分页列表查询", notes="灌注桩详情-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceMixpileGroutedOne deviceMixpileGroutedOne,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (StringUtils.isBlank(deviceMixpileGroutedOne.getSbbh())) {
			if (!"null".equals(shebei)) {
				deviceMixpileGroutedOne.setSbbh(shebei);
			} else {
				deviceMixpileGroutedOne.setSbbh("空");
			}
		}
		QueryWrapper<DeviceMixpileGroutedOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileGroutedOne, req.getParameterMap());
		Page<DeviceMixpileGroutedOne> page = new Page<DeviceMixpileGroutedOne>(pageNo, pageSize);
		IPage<DeviceMixpileGroutedOne> pageList = deviceMixpileGroutedOneService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param deviceMixpileGroutedOne
	 * @return
	 */
	@AutoLog(value = "灌注桩详情-添加")
	@ApiOperation(value="灌注桩详情-添加", notes="灌注桩详情-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceMixpileGroutedOne deviceMixpileGroutedOne) {
		deviceMixpileGroutedOneService.save(deviceMixpileGroutedOne);
		return Result.OK("添加成功！");
	}

	 @PostMapping(value = "/addGroutedOpen")
	 public Result<?> addGroutedOpen(@RequestBody DeviceMixpileGroutedOne deviceMixpileGroutedOne) {
		 String msg = "添加成功！";
		// deviceMixpileGroutedOneService.save(deviceMixpileGroutedOne);
		 DeviceMixpileGroutedOne One = deviceMixpileGroutedOneService.getByUuid(deviceMixpileGroutedOne.getUuid());
		 if(ObjectUtil.isNotNull(One)){
			 deviceMixpileGroutedOne.setId(One.getId());
			 deviceMixpileGroutedOneService.updateById(deviceMixpileGroutedOne);
			 msg = "编辑成功";
		 }else{
			 // 追加主表数据
			 deviceMixpileGroutedOneService.save(deviceMixpileGroutedOne);
		 }
		 return Result.OK(msg);
	 }



	 @AutoLog(value = "灌注桩详情-添加")
	 @ApiOperation(value="灌注桩详情-添加", notes="灌注桩详情-添加")
	 @PostMapping(value = "/addGrouteds")
	 public Result<?> addGrouteds(@RequestBody DeviceMixpileGroutedOne deviceMixpileGroutedOne) {
		String msg = "添加成功！";
		 DeviceMixpileGroutedOne One = deviceMixpileGroutedOneService.getByUuid(deviceMixpileGroutedOne.getUuid());
		 if(ObjectUtil.isNotNull(One)){
			 deviceMixpileGroutedOne.setId(One.getId());
			 deviceMixpileGroutedOneService.updateById(deviceMixpileGroutedOne);
			 QueryWrapper<DeviceMixpileGroutedPart> queryWrapper = new QueryWrapper<>();
			 queryWrapper.eq("oneuuid",deviceMixpileGroutedOne.getUuid());
			 // 移除子表数据
			 if(deviceMixpileGroutedOne.getParts().size()>0){
				 deviceMixpileGroutedPartService.remove(queryWrapper);
			 }

			 msg ="修改成功 ！";
		 }else {
			 // 追加主表数据
			 deviceMixpileGroutedOneService.save(deviceMixpileGroutedOne);
		 }

		 if(deviceMixpileGroutedOne.getParts().size()>0){
			 // 追加子表数据
			 deviceMixpileGroutedPartService.saveBatch(deviceMixpileGroutedOne.getParts());
		 }

		 return Result.OK(msg);
	 }
	
	/**
	 *  编辑
	 *
	 * @param deviceMixpileGroutedOne
	 * @return
	 */
	@AutoLog(value = "灌注桩详情-编辑")
	@ApiOperation(value="灌注桩详情-编辑", notes="灌注桩详情-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceMixpileGroutedOne deviceMixpileGroutedOne) {
		deviceMixpileGroutedOneService.updateById(deviceMixpileGroutedOne);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "灌注桩详情-通过id删除")
	@ApiOperation(value="灌注桩详情-通过id删除", notes="灌注桩详情-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceMixpileGroutedOneService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "灌注桩详情-批量删除")
	@ApiOperation(value="灌注桩详情-批量删除", notes="灌注桩详情-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceMixpileGroutedOneService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "灌注桩详情-通过id查询")
	@ApiOperation(value="灌注桩详情-通过id查询", notes="灌注桩详情-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceMixpileGroutedOne deviceMixpileGroutedOne = deviceMixpileGroutedOneService.getById(id);
		if(deviceMixpileGroutedOne==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceMixpileGroutedOne);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceMixpileGroutedOne
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileGroutedOne deviceMixpileGroutedOne) {
        return super.exportXls(request, deviceMixpileGroutedOne, DeviceMixpileGroutedOne.class, "灌注桩详情");
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
        return super.importExcel(request, response, DeviceMixpileGroutedOne.class);
    }

}
