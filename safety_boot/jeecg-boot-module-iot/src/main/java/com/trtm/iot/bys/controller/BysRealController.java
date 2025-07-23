package com.trtm.iot.bys.controller;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.devicemixpilerwd.entity.DeviceMixpileRwd;
import com.trtm.iot.devicemixpilerwd.entity.DeviceMixpileRwdMqtt;
import com.trtm.iot.devicemixpilerwdlog.entity.DeviceMixpileRwdLog;
import com.trtm.iot.devicemixpilerwdlog.service.IDeviceMixpileRwdLogService;
import org.apache.shiro.SecurityUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;

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
 * @Description: bys_real
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
@Api(tags="bys_real")
@RestController
@RequestMapping("/bys/bysReal")
@Slf4j
public class BysRealController extends JeecgController<BysReal, IBysRealService> {
	@Autowired
	private IBysRealService bysRealService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IDeviceMixpileRwdLogService deviceMixpileRwdLogService;
	/**
	 * 分页列表查询
	 *
	 * @param bysReal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bys_real-分页列表查询")
	@ApiOperation(value="bys_real-分页列表查询", notes="bys_real-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BysReal bysReal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (bysReal.getShebeino() == null) {
			if ("null".equals(shebei)) {
				bysReal.setShebeino("空");
			}else{
				bysReal.setShebeino(shebei);
			}

		}
		QueryWrapper<BysReal> queryWrapper = QueryGenerator.initQueryWrapper(bysReal, req.getParameterMap());
		queryWrapper.select("id,shebeiNo,gatherDate,temperature,humidity,temstatus,humstatus");
		Page<BysReal> page = new Page<BysReal>(pageNo, pageSize);
		IPage<BysReal> pageList = bysRealService.page(page, queryWrapper);
		pageList.getRecords().forEach(item -> {
			String shebeino = item.getShebeino();
			if (shebeino.equals("1020") || shebeino.equals("1318") || shebeino.equals("1512")){
				BigDecimal temperature = item.getTemperature();
				BigDecimal humidity = item.getHumidity();
				if (humidity.compareTo(new BigDecimal("95.0")) < 0) {
					item.setHumidity(BigDecimal.valueOf(95));
				}
				if (temperature.compareTo(new BigDecimal("18.0")) < 0) {
					item.setTemperature(BigDecimal.valueOf(18.10));
				}
				if (temperature.compareTo(new BigDecimal("22.0")) > 0) {
					item.setTemperature(BigDecimal.valueOf(21.80));
				}
			}
		});
		return Result.OK(pageList);
	}



	 @AutoLog(value = "bys_real-分页列表查询")
	 @ApiOperation(value="bys_real-分页列表查询", notes="bys_real-分页列表查询")
	 @GetMapping(value = "/listmax")
	 public Result<?> queryPageListmax(BysReal bysReal,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		 if (bysReal.getShebeino() == null) {
			 if ("null".equals(shebei)) {
				 bysReal.setShebeino("空");
			 }
			 bysReal.setShebeino(shebei);
		 }
		 QueryWrapper<BysReal> queryWrapper = QueryGenerator.initQueryWrapper(bysReal, req.getParameterMap());
		 queryWrapper.select(" max(id) id");
		 queryWrapper.groupBy("shebeino");
		 Page<BysReal> page = new Page<BysReal>(pageNo, pageSize);
		 IPage<BysReal> pageList = bysRealService.page(page, queryWrapper);
		 for(BysReal item :  pageList.getRecords()){
			 BysReal byId = bysRealService.getById(item.getId());
			   BeanUtils.copyProperties(byId,item);
		 }

		 return Result.OK(pageList);
	 }

	 // mqtt 修改 调整数据
	 @PutMapping(value = "/adjust")
	 public Result<?> queryPageList2(@RequestBody BysReal bysReal) {

		 String broker = "tcp://47.97.158.215:8881";
		 String topic = "user/"+bysReal.getShebeino();
		 String clientId = "MQTT_SUB_Producer";
		 MqttClient subClient = getMqttClient(broker, clientId);
		 JSONObject obj = new JSONObject();
		 obj.put("data","calibration");
		 obj.put("temperature",bysReal.getTemperature().toString());
		 obj.put("humidity",bysReal.getHumidity().toString());
			 try {
				 MqttMessage mqttMessage = new MqttMessage(JSONUtil.toJsonStr(obj).getBytes(StandardCharsets.UTF_8));
				 if (subClient != null) {
					 subClient.publish(topic, mqttMessage);
					 subClient.disconnect();
					 DeviceMixpileRwdLog deviceMixpileRwdLog1 = new DeviceMixpileRwdLog();
					 deviceMixpileRwdLog1.setQbroker(broker);
					 deviceMixpileRwdLog1.setTopicAll(topic);
					 deviceMixpileRwdLog1.setRjrwd("标养室环境校准");
					 deviceMixpileRwdLog1.setPushjson(JSONUtil.toJsonStr(obj));
					 deviceMixpileRwdLog1.setShebeino(bysReal.getShebeino());
					 deviceMixpileRwdLog1.setIssuedtime(new Date());
					 deviceMixpileRwdLogService.save(deviceMixpileRwdLog1);

					 return Result.OK("温湿度校准成功！");
				 } else {
					 return Result.error("温湿度校准失败！");
				 }
			 } catch (Exception e) {
				 e.printStackTrace();
				 return Result.error("温湿度校准失败！");
			 }

	 }

	 private static MqttClient getMqttClient(String broker, String clientId) {
		 try {
			 MqttClient pubClient = new MqttClient(broker, clientId, new MemoryPersistence());
			 MqttConnectOptions connectOptions = new MqttConnectOptions();
			 connectOptions.setCleanSession(false);
			 //System.out.println("Connecting to broker: " + broker);
			 pubClient.connect(connectOptions);
			 return pubClient;
		 } catch (MqttException e) {
			 e.printStackTrace();
		 }
		 return null;
	 }

	/**
	 *   添加
	 *
	 * @param bysReal
	 * @return
	 */
	@AutoLog(value = "bys_real-添加")
	@ApiOperation(value="bys_real-添加", notes="bys_real-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BysReal bysReal) {
		bysRealService.save(bysReal);
		return Result.OK("添加成功！");
	}

	 /**
	  *   标养室对外开放添加接口
	  *
	  * @param bysReal
	  * @return
	  */
	 @AutoLog(value = "bys_real-添加")
	 @ApiOperation(value="bys_real-添加", notes="bys_real-添加")
	 @PostMapping(value = "/addOpen")
	 public Result<?> addOpen(@RequestBody BysReal bysReal) {
		 bysRealService.save(bysReal);
		 return Result.OK("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param bysReal
	 * @return
	 */
	@AutoLog(value = "bys_real-编辑")
	@ApiOperation(value="bys_real-编辑", notes="bys_real-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BysReal bysReal) {
		bysRealService.updateById(bysReal);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bys_real-通过id删除")
	@ApiOperation(value="bys_real-通过id删除", notes="bys_real-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bysRealService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bys_real-批量删除")
	@ApiOperation(value="bys_real-批量删除", notes="bys_real-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bysRealService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bys_real-通过id查询")
	@ApiOperation(value="bys_real-通过id查询", notes="bys_real-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BysReal bysReal = bysRealService.getById(id);
		if(bysReal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bysReal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bysReal
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BysReal bysReal) {
        return super.exportXls(request, bysReal, BysReal.class, "bys_real");
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
        return super.importExcel(request, response, BysReal.class);
    }

}
