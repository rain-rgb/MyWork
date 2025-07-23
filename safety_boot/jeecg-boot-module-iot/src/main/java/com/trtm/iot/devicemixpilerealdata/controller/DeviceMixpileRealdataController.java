package com.trtm.iot.devicemixpilerealdata.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.devicemixpilehistorydata.entity.DeviceMixpileHistorydata;
import com.trtm.iot.devicemixpilehistorydatapart.entity.DeviceMixpileHistorydataPart;
import com.trtm.iot.devicemixpilehistorydatapart.service.IDeviceMixpileHistorydataPartService;
import com.trtm.iot.devicemixpilerealdata.mapper.DeviceMixpileRealdataMapper;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.trtm.iot.devicemixpilerealdata.service.IDeviceMixpileRealdataService;

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

import static com.trtm.iot.util.GPSUtil.*;
import static com.trtm.iot.util.GPSUtil.retain6;

/**
 * @Description: device_mixpile_realdata
 * @Author: jeecg-boot
 * @Date:   2021-10-20
 * @Version: V1.0
 */
@Api(tags="device_mixpile_realdata")
@RestController
@RequestMapping("/devicemixpilerealdata/deviceMixpileRealdata")
@Slf4j
public class DeviceMixpileRealdataController extends JeecgController<DeviceMixpileRealdata, IDeviceMixpileRealdataService> {
	@Autowired
	private IDeviceMixpileRealdataService deviceMixpileRealdataService;
	@Autowired
	private IDeviceMixpileHistorydataPartService deviceMixpileHistorydataPartService;


	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param deviceMixpileRealdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "device_mixpile_realdata-分页列表查询")
	@ApiOperation(value="device_mixpile_realdata-分页列表查询", notes="device_mixpile_realdata-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceMixpileRealdata deviceMixpileRealdata,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String gropby,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (deviceMixpileRealdata.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				deviceMixpileRealdata.setShebeino(shebei);
			}else {
				deviceMixpileRealdata.setShebeino("空");
			}
		}
		QueryWrapper<DeviceMixpileHistorydataPart> permgrout = new QueryWrapper<>();
		QueryWrapper<DeviceMixpileRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileRealdata, req.getParameterMap());
		queryWrapper.select(" id," +
				"shebeino," +
				"designdep," +
				"curdep," +
				"x," +
				"y," +
				"elec," +
				"grouting," +
				"pileno," +
				"designratio," +
				"designgrout," +
				"cement," +
				"water," +
				"ratio," +
				"serial," +
				"username," +
				"datatime," +
				"LTDFloat," +
				"LGDFloat," +
				"flowlst," +
				"speed," +
				"mileage," +
				"piletype," +
				"press," +
				"flowmeter," +
				"worksta," +
				"gzcount, "+
				"( CASE WHEN left(shebeino,3) = 'A05' THEN 1 ELSE 0 END ) AS ordersb, " + //我们的设备优先展示
				"( CASE WHEN date_add( now(), INTERVAL - 5 MINUTE )> datatime THEN 1 ELSE 0 END ) AS ordersta ");
		if(queryWrapper.getExpression().getOrderBy().size()>0){
			queryWrapper.getExpression().getOrderBy().remove(0);
		}
		if(StringUtil.isNotEmpty(gropby)){
			queryWrapper.last(" and id in (SELECT max(id) FROM device_mixpile_realdata GROUP BY shebeino) order by ordersb,ordersta asc");
//			queryWrapper.groupBy(gropby);
		}
		Page<DeviceMixpileRealdata> page = new Page<DeviceMixpileRealdata>(pageNo, pageSize);
		IPage<DeviceMixpileRealdata> pageList = deviceMixpileRealdataService.page(page, queryWrapper);
		List<DeviceMixpileRealdata> records = pageList.getRecords();
		for (DeviceMixpileRealdata record : records) {
			if(record.getLgdfloat()!=null&&record.getLtdfloat()!=null) {
				Double lon = Double.valueOf(String.valueOf(record.getLgdfloat()));
				Double lat = Double.valueOf(String.valueOf(record.getLtdfloat()));
				Double lat1 = formatLnglat(lat);
				Double lon1 = formatLnglat(lon);
				double[] zuobiao = gps84_To_Gcj02(lat1, lon1);
				Double lat2 = retain6(zuobiao[0]);
				Double lon2 = retain6(zuobiao[1]);
				record.setLgdfloat(String.valueOf(lon2));
				record.setLtdfloat(String.valueOf(lat2));
			}
			if(!StringUtils.isEmpty(record.getShebeino()) && !StringUtils.isEmpty(record.getPileno())){
				permgrout.select(" part_beton*1000 permgrout ");
				String lastsql = " where id = (SELECT max(id) FROM device_mixpile_historydata_part where shebeino = '"+record.getShebeino()+"' and pile_no = '"+record.getPileno()+"')";
				permgrout.last(lastsql);
				Map<String, Object> map = deviceMixpileHistorydataPartService.getMap(permgrout);
				if( map != null){
				    record.setPermgrout(String.valueOf(map.get("permgrout")));
				}else {
					record.setPermgrout("0");
				}

			}else{
				record.setPermgrout("0");
			}

			if(record.getDatatime()!=null){
				Date datatime = record.getDatatime();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String format = df.format(new Date());
				String format1 = df.format(datatime);
				long NTime = 0;
				long OTime = 0;
				try {
					NTime = df.parse(format).getTime();
					//从对象中拿到时间
					OTime = df.parse(format1).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				long diff=(NTime-OTime)/1000/60;
				if(diff>5){
					record.setWorksta("3");//离线
				}
			}
		}
//		Collections.sort(records, (o1, o2) -> {
//			if(Integer.valueOf(o1.getWorksta()) >Integer.valueOf(o2.getWorksta()))return 1;
//			else return -1;
//		});
		return Result.OK(pageList);
	}



	/**
	 *   统计在线数
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "device_mixpile_realdata-统计在线数")
	@ApiOperation(value="device_mixpile_realdata-统计在线数", notes="device_mixpile_realdata-统计在线数")
	@GetMapping(value = "/online")
	public Result<?> online(DeviceMixpileRealdata deviceMixpileRealdata,
							@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
							@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String gropby,
							HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		//查询到他的设备编号
		if (deviceMixpileRealdata.getShebeino() == null) {
			if (!"null".equals(shebei)) {
				deviceMixpileRealdata.setShebeino(shebei);
			}else {
				deviceMixpileRealdata.setShebeino("空");
			}
		}
		QueryWrapper<DeviceMixpileRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileRealdata, req.getParameterMap());
		queryWrapper.select("count(DISTINCT(shebeino)) as onlinecount");
		queryWrapper.last("and datatime BETWEEN date_add( now(), INTERVAL - 5 MINUTE ) AND now()");
		Map<String,Object> map = deviceMixpileRealdataService.getMap(queryWrapper);
//		List<Map<String, Object>> count = deviceMixpileRealdataService.countOnline(sysOrgCode);
		return Result.OK(map);
	}


	/**
	 *   添加
	 *
	 * @param deviceMixpileRealdata
	 * @return
	 */
	@AutoLog(value = "device_mixpile_realdata-添加")
	@ApiOperation(value="device_mixpile_realdata-添加", notes="device_mixpile_realdata-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceMixpileRealdata deviceMixpileRealdata) {
		deviceMixpileRealdataService.save(deviceMixpileRealdata);
		return Result.OK("添加成功！");
	}


	@AutoLog(value = "device_mixpile_realdata-数据上传更新")
	@ApiOperation(value="device_mixpile_realdata-数据上传更新", notes="device_mixpile_realdata-数据上传更新")
	@PostMapping(value = "/addOther")
	public Result<?> addOther(@RequestBody DeviceMixpileRealdata deviceMixpileRealdata,HttpServletRequest req) {

		if(StringUtil.isNotEmpty(deviceMixpileRealdata.getShebeino())){
			DeviceMixpileRealdata realdata = new DeviceMixpileRealdata();
			realdata.setShebeino(deviceMixpileRealdata.getShebeino());
			QueryWrapper<DeviceMixpileRealdata> queryWrapper = QueryGenerator.initQueryWrapper(realdata, req.getParameterMap());
			List<DeviceMixpileRealdata> ones = deviceMixpileRealdataService.list(queryWrapper);
			if(ones.size()>0){
				deviceMixpileRealdata.setId(ones.get(0).getId());
				deviceMixpileRealdataService.updateById(deviceMixpileRealdata);
				return Result.OK("软基设备实时数据更新成功!",deviceMixpileRealdata);
			}else{
				deviceMixpileRealdataService.save(deviceMixpileRealdata);
				return Result.OK("软基设备实时数据上传成功！",deviceMixpileRealdata);
			}
		}else{
			return Result.error("设备编号为空请重新上传！",deviceMixpileRealdata);
		}


	}

	/**
	 *  编辑
	 *
	 * @param deviceMixpileRealdata
	 * @return
	 */
	@AutoLog(value = "device_mixpile_realdata-编辑")
	@ApiOperation(value="device_mixpile_realdata-编辑", notes="device_mixpile_realdata-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DeviceMixpileRealdata deviceMixpileRealdata) {
		deviceMixpileRealdataService.updateById(deviceMixpileRealdata);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_mixpile_realdata-通过id删除")
	@ApiOperation(value="device_mixpile_realdata-通过id删除", notes="device_mixpile_realdata-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceMixpileRealdataService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "device_mixpile_realdata-批量删除")
	@ApiOperation(value="device_mixpile_realdata-批量删除", notes="device_mixpile_realdata-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceMixpileRealdataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "device_mixpile_realdata-通过id查询")
	@ApiOperation(value="device_mixpile_realdata-通过id查询", notes="device_mixpile_realdata-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceMixpileRealdata deviceMixpileRealdata = deviceMixpileRealdataService.getById(id);
		if(deviceMixpileRealdata==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(deviceMixpileRealdata);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param deviceMixpileRealdata
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileRealdata deviceMixpileRealdata) {
        return super.exportXls(request, deviceMixpileRealdata, DeviceMixpileRealdata.class, "device_mixpile_realdata");
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
        return super.importExcel(request, response, DeviceMixpileRealdata.class);
    }

}
