package com.trtm.iot.virtualgateway.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.virtualgateway.entity.VirtualGatewayDoor;
import com.trtm.iot.virtualgateway.service.IVirtualGatewayDoorService;

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
 * @Description: virtual_gateway_door
 * @Author: jeecg-boot
 * @Date:   2023-07-07
 * @Version: V1.0
 */
@Api(tags="virtual_gateway_door")
@RestController
@RequestMapping("/virtualgateway/virtualGatewayDoor")
@Slf4j
public class VirtualGatewayDoorController extends JeecgController<VirtualGatewayDoor, IVirtualGatewayDoorService> {
	@Autowired
	private IVirtualGatewayDoorService virtualGatewayDoorService;
	 @Autowired
	 private IWzliaocangService wzliaocangService;
	
	/**
	 * 分页列表查询
	 *
	 * @param virtualGatewayDoor
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "virtual_gateway_door-分页列表查询")
	@ApiOperation(value="virtual_gateway_door-分页列表查询", notes="virtual_gateway_door-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(VirtualGatewayDoor virtualGatewayDoor,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<VirtualGatewayDoor> queryWrapper = QueryGenerator.initQueryWrapper(virtualGatewayDoor, req.getParameterMap());
		Page<VirtualGatewayDoor> page = new Page<VirtualGatewayDoor>(pageNo, pageSize);
		IPage<VirtualGatewayDoor> pageList = virtualGatewayDoorService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	/**
	 *   添加
	 *
	 * @param virtualGatewayDoor
	 * @return
	 */
	@AutoLog(value = "virtual_gateway_door-添加")
	@ApiOperation(value="virtual_gateway_door-添加", notes="virtual_gateway_door-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody VirtualGatewayDoor virtualGatewayDoor) {
		virtualGatewayDoorService.save(virtualGatewayDoor);
		return Result.OK("添加成功！");
	}

	 @AutoLog(value = "virtual_gateway_door-控制开关")
	 @ApiOperation(value="virtual_gateway_door-控制开关", notes="virtual_gateway_door-控制开关")
	 @GetMapping(value = "/onOff")
	 public Result<?> onOff(@RequestBody VirtualGatewayDoor virtualGatewayDoor) {
		 JSONObject sendObject = new JSONObject();
		 QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("Infrared_fence",virtualGatewayDoor.getDeviceId());
		 List<Wzliaocang> list = wzliaocangService.list(queryWrapper);
        String message ="";
		 if(list.size()>0){
			 Wzliaocang wzliaocang =list.get(0);

		 if ("2".equals(wzliaocang.getLiaocangStatus()) || "4".equals(wzliaocang.getLiaocangStatus())) {
			 message = "料仓状态为检验中或待检验不允许开料仓";
		 }else{
			 sendObject.set("deviceId", wzliaocang.getInfraredFence());
			 sendObject.set("command", "on");
			 String post = HttpRequest.post("http://101.37.166.105:8001/appInfraredVirtualGateway/sendDeviceCommand")
					 .header("Content-Type", "application/json")
					 .body(String.valueOf(sendObject))
					 .execute()
					 .body();

			 JSONObject postObject = new JSONObject(post);
			 Object code = postObject.get("code");
			 if ("0000".equals(String.valueOf(code))) {
				 message  = "开仓成功";
			 } else {
				 message  = "开仓失败";
			 }
		 }
	 }else{
			 message = "设备未在料仓进行安装";
		 }
		 return Result.OK(message);
	 }
	
	/**
	 *  编辑
	 *
	 * @param virtualGatewayDoor
	 * @return
	 */
	@AutoLog(value = "virtual_gateway_door-编辑")
	@ApiOperation(value="virtual_gateway_door-编辑", notes="virtual_gateway_door-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody VirtualGatewayDoor virtualGatewayDoor) {
		virtualGatewayDoorService.updateById(virtualGatewayDoor);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "virtual_gateway_door-通过id删除")
	@ApiOperation(value="virtual_gateway_door-通过id删除", notes="virtual_gateway_door-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		virtualGatewayDoorService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "virtual_gateway_door-批量删除")
	@ApiOperation(value="virtual_gateway_door-批量删除", notes="virtual_gateway_door-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.virtualGatewayDoorService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "virtual_gateway_door-通过id查询")
	@ApiOperation(value="virtual_gateway_door-通过id查询", notes="virtual_gateway_door-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		VirtualGatewayDoor virtualGatewayDoor = virtualGatewayDoorService.getById(id);
		if(virtualGatewayDoor==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(virtualGatewayDoor);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param virtualGatewayDoor
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VirtualGatewayDoor virtualGatewayDoor) {
        return super.exportXls(request, virtualGatewayDoor, VirtualGatewayDoor.class, "virtual_gateway_door");
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
        return super.importExcel(request, response, VirtualGatewayDoor.class);
    }

}
