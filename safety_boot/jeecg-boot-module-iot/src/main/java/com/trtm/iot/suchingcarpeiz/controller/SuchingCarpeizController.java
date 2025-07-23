package com.trtm.iot.suchingcarpeiz.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.suchingcarpeiz.entity.SuchingCarpeiz;
import com.trtm.iot.suchingcarpeiz.service.ISuchingCarpeizService;

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

import static com.trtm.iot.util.GPSUtil.gps84_To_Gcj02;

/**
 * @Description: suching_carpeiz
 * @Author: jeecg-boot
 * @Date:   2023-11-10
 * @Version: V1.0
 */
@Api(tags="suching_carpeiz")
@RestController
@RequestMapping("/suchingcarpeiz/suchingCarpeiz")
@Slf4j
public class SuchingCarpeizController extends JeecgController<SuchingCarpeiz, ISuchingCarpeizService> {
	@Autowired
	private ISuchingCarpeizService suchingCarpeizService;

	 public static String URL = "http://www.iotlock.vip/mt-api/";
	 public static String USERNAME = "hzgx";
	 public static String PASSWORD = "dc483e80a7a0bd9ef71d8cf973673924";

	 @GetMapping(value = "/listcl")
	 public Result<?> queryPageListcl(String sn,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  String start_time,String end_time,HttpServletRequest req) {
		 String url = URL + "device/position/record/apisearch";
		 String url2 = URL + "mongo/apisearch";
         Boolean oneM = false;
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 try {
			 Date starttime = format.parse(start_time);
			 // 大于2天 为false 用接口2
			 Long times = new Date().getTime() - starttime.getTime();
			 oneM = times/24/3600/1000 > 2 ? false : true;
		 } catch (ParseException e) {
			 throw new RuntimeException(e);
		 }

		 JSONObject sendObject = JSONUtil.createObj();
		 sendObject.set("username", USERNAME);
		 sendObject.set("password", PASSWORD);
		 sendObject.set("pageNum", pageNo);
		 sendObject.set("pageSize", pageSize);
		 sendObject.set("sn", sn);
		 sendObject.set("start_time", start_time);
		 sendObject.set("end_time", end_time);
		 JSONObject code = new JSONObject();
		 if(oneM){
			  code = getCode(sendObject, url);
		 }else{
			  code = getCode(sendObject, url2);
		 }

		 Object data = code.get("data");
		 JSONObject jsonObject = new JSONObject(data);
		 Object list = jsonObject.get("list");
		 JSONArray jsonObject1 = new JSONArray(list);
		 System.out.println(jsonObject1);
		 List<HashMap<String, Object>> list1 = new ArrayList<>();
		 for (Object object :jsonObject1){
			 JSONObject o = new JSONObject(object);
			 String create_time = parseCreateTime((JSONObject)o.get("create_time")) ;
			 String device_name = (String) o.get("device_name");
			 double[] gd = gps84_To_Gcj02( new Double((String) o.get("lat")),new Double((String) o.get("lon")));
			 String lat = String.valueOf(gd[0]);
			 String lon = String.valueOf(gd[1]);
			 HashMap<String, Object> map = new HashMap<>();
			 map.put("create_time",create_time);
			 map.put("device_name",device_name);
			 map.put("lat",lat);
			 map.put("lon",lon);
			 list1.add(map);
		 }
		 return Result.OK(list1);
	 }

	public  String parseCreateTime(JSONObject rootObject) {

		// 提取日期部分
		JSONObject dateObject = rootObject.getJSONObject("date");
		int year = dateObject.getInt("year");
		int month = dateObject.getInt("month");
		int day = dateObject.getInt("day");

		// 提取时间部分
		JSONObject timeObject = rootObject.getJSONObject("time");
		int hour = timeObject.getInt("hour");
		int minute = timeObject.getInt("minute");
		int second = timeObject.getInt("second");

		// 格式化为字符串
		return String.format("%04d-%02d-%02d %02d:%02d:%02d",
				year, month, day, hour, minute, second);
	}



	 public static JSONObject getCode(JSONObject sendObject, String url) {
		 String body = HttpRequest.post(url)
				 .header("Content-Type", "application/json")
				 .body(String.valueOf(sendObject))
				 .execute()
				 .body();
		 System.out.println(body);
		 System.out.println(new JSONObject(body));
		 return new JSONObject(body);
	 }
	/**
	 * 分页列表查询
	 *
	 * @param suchingCarpeiz
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "suching_carpeiz-分页列表查询")
	@ApiOperation(value="suching_carpeiz-分页列表查询", notes="suching_carpeiz-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SuchingCarpeiz suchingCarpeiz,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SuchingCarpeiz> queryWrapper = QueryGenerator.initQueryWrapper(suchingCarpeiz, req.getParameterMap());
		Page<SuchingCarpeiz> page = new Page<SuchingCarpeiz>(pageNo, pageSize);
		IPage<SuchingCarpeiz> pageList = suchingCarpeizService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param suchingCarpeiz
	 * @return
	 */
	@AutoLog(value = "suching_carpeiz-添加")
	@ApiOperation(value="suching_carpeiz-添加", notes="suching_carpeiz-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SuchingCarpeiz suchingCarpeiz) {
		suchingCarpeizService.save(suchingCarpeiz);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param suchingCarpeiz
	 * @return
	 */
	@AutoLog(value = "suching_carpeiz-编辑")
	@ApiOperation(value="suching_carpeiz-编辑", notes="suching_carpeiz-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SuchingCarpeiz suchingCarpeiz) {
		suchingCarpeizService.updateById(suchingCarpeiz);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "suching_carpeiz-通过id删除")
	@ApiOperation(value="suching_carpeiz-通过id删除", notes="suching_carpeiz-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		suchingCarpeizService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "suching_carpeiz-批量删除")
	@ApiOperation(value="suching_carpeiz-批量删除", notes="suching_carpeiz-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.suchingCarpeizService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "suching_carpeiz-通过id查询")
	@ApiOperation(value="suching_carpeiz-通过id查询", notes="suching_carpeiz-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SuchingCarpeiz suchingCarpeiz = suchingCarpeizService.getById(id);
		if(suchingCarpeiz==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(suchingCarpeiz);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param suchingCarpeiz
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SuchingCarpeiz suchingCarpeiz) {
        return super.exportXls(request, suchingCarpeiz, SuchingCarpeiz.class, "suching_carpeiz");
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
        return super.importExcel(request, response, SuchingCarpeiz.class);
    }

	 /**
	  * 百度坐标转换高德坐标
	  *
	  * @param bd_lon 经度
	  * @param bd_lat 纬度
	  * @return
	  */
	 public static Map<String, Double> bd2gd(double bd_lon, double bd_lat) {
		 double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
		 double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		 double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		 double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		 double gd_lon = z * Math.cos(theta);
		 double gd_lat = z * Math.sin(theta);
		 Map<String, Double> data = new HashMap<>();
		 data.put("lon", gd_lon);
		 data.put("lat", gd_lat);
		 return data;
	 }

}
