package com.trtm.iot.hc_machine.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.devicepipepilehistorydataone.mapper.DevicePipepileHistorydataOneMapper;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.trtm.iot.hc_constructionresults.service.IHcConstructionresultsService;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_machine.service.IHcMachineService;

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
 * @Description: 机械设备
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Api(tags="机械设备")
@RestController
@RequestMapping("/hc_machine/hcMachine")
@Slf4j
public class HcMachineController extends JeecgController<HcMachine, IHcMachineService> {
	@Autowired
	private IHcMachineService hcMachineService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IHcConstructionresultsService constructionresultsService;
	 @Autowired
	 DevicePipepileHistorydataOneMapper devicePipepileHistorydataOneMapper;
	 @Autowired
	 IOpenapigpsdatavoService openapigpsdatavoService;
	 @Autowired
	 private IBhzPhoneService bhzPhoneService;
	/**
	 * 分页列表查询
	 *
	 * @param hcMachine
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "机械设备-分页列表查询")
	@ApiOperation(value="机械设备-分页列表查询", notes="机械设备-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HcMachine hcMachine,
								   String sys_depart_orgcode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		if(sys_depart_orgcode!=null){
			hcMachine.setOrgcode(sys_depart_orgcode+"*");//模糊查询
		}else{
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
			hcMachine.setOrgcode(loginUser.getOrgCode()+"*");//模糊查询
		}
		QueryWrapper<HcMachine> queryWrapper = QueryGenerator.initQueryWrapper(hcMachine, req.getParameterMap());
		Page<HcMachine> page = new Page<HcMachine>(pageNo, pageSize);
		IPage<HcMachine> pageList = hcMachineService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表曲线查询
	  *
	  * @param hcMachine
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "机械设备-分页列表查询")
	 @ApiOperation(value="机械设备-分页列表查询", notes="机械设备-分页列表查询")
	 @GetMapping(value = "/listqx")
	 public Result<?> queryPageListqx(HcMachine hcMachine,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<HcMachine> queryWrapper = QueryGenerator.initQueryWrapper(hcMachine, req.getParameterMap());
		 queryWrapper.eq("machineTypeCode",0);
		 Page<HcMachine> page = new Page<HcMachine>(pageNo, pageSize);
		 IPage<HcMachine> pageList = hcMachineService.page(page, queryWrapper);
		 for (HcMachine list :pageList.getRecords()){
			 String machineid = list.getMachineid();
			 QueryWrapper<Openapigpsdatavo> queryWrapper1 = new QueryWrapper<>();
			 queryWrapper1.select("MAX(gps_time) gps_time");
			 queryWrapper1.eq("machine_id",machineid);
			 queryWrapper1.orderByDesc("gps_time");
			 Openapigpsdatavo one = openapigpsdatavoService.getOne(queryWrapper1);
			 if (one != null){
				 Date gpsTime = one.getGpsTime();
				 list.setTmstarttime(gpsTime);
			 }
		 }
		 return Result.OK(pageList);
	 }
	 /**
	  * 分页列表查询沥青水稳
	  *
	  * @param hcMachine
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "机械设备-分页列表查询")
	 @ApiOperation(value="机械设备-分页列表查询", notes="机械设备-分页列表查询")
	 @GetMapping(value = "/listleb")
	 public Result<?> queryPageListleb(HcMachine hcMachine,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 String orgCode = hcMachine.getOrgcode();
		 List<Map<String, Object>> sj = new ArrayList<>();
		 List<Map<String, Object>> listMap = devicePipepileHistorydataOneMapper.selectSheBeiNo(orgCode);
		 for (Map<String, Object> map : listMap) {
			 String departName = map.get("depart_name").toString();
			 String orgCode1 = map.get("org_code").toString();
			 Map<String, Object> map1 = new HashMap<>();
			 map1.put("departName",departName);
			 Double lqsum = tjsum(orgCode1, 5);
			 Double swsum = tjsum(orgCode1, 12);
			 map1.put("lqsum",lqsum);
			 map1.put("swsum",swsum);
			 sj.add(map1);
		 }
		 return Result.OK(sj);
	 }

	 public Double tjsum(String orgCode1 ,int i){
		 QueryWrapper<HcMachine> queryWrapper = new QueryWrapper<>();
		 queryWrapper.likeRight("orgcode",orgCode1);
		 queryWrapper.eq("machinetypecode",i);
		 queryWrapper.groupBy("pjid,sectionid");

		 List<HcMachine> list = hcMachineService.list(queryWrapper);
		 Double sum = 0.0;
		 if (list.size() > 0){
			 for (HcMachine l :list){
				 String pjid = l.getPjid();
				 String machineid = l.getSectionid();
				 String j = constructionresultsService.selectbypjidmid(pjid,machineid);
				 if (j == null){
					 j = "0";
				 }
				 double v = Double.parseDouble(j);
				 sum+=v;
			 }
		 }
		 return sum;
	 }
	/**
	 *   添加
	 *
	 * @param hcMachine
	 * @return
	 */
	@AutoLog(value = "机械设备-添加")
	@ApiOperation(value="机械设备-添加", notes="机械设备-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HcMachine hcMachine) {
		hcMachineService.save(hcMachine);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param hcMachine
	 * @return
	 */
	@AutoLog(value = "机械设备-编辑")
	@ApiOperation(value="机械设备-编辑", notes="机械设备-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HcMachine hcMachine) {
		BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(hcMachine.getPhone());
		if (ObjectUtil.isNotEmpty(bhzPhone)){
			hcMachine.setPhone(bhzPhone.getPhones());
		}
		hcMachineService.updateById(hcMachine);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "机械设备-通过id删除")
	@ApiOperation(value="机械设备-通过id删除", notes="机械设备-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		hcMachineService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "机械设备-批量删除")
	@ApiOperation(value="机械设备-批量删除", notes="机械设备-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.hcMachineService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "机械设备-通过id查询")
	@ApiOperation(value="机械设备-通过id查询", notes="机械设备-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HcMachine hcMachine = hcMachineService.getById(id);
		if(hcMachine==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(hcMachine);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param hcMachine
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcMachine hcMachine) {
        return super.exportXls(request, hcMachine, HcMachine.class, "机械设备");
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
        return super.importExcel(request, response, HcMachine.class);
    }

}
