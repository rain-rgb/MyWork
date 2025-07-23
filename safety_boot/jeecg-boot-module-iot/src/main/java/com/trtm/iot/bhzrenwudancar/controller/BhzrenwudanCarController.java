package com.trtm.iot.bhzrenwudancar.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.service.IBhzrenwudanService;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.bhzrenwudancar.entity.BhzrenwudanCar;
import com.trtm.iot.bhzrenwudancar.service.IBhzrenwudanCarService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 中铁一局拌合站生产流向
 * @Author: jeecg-boot
 * @Date:   2023-10-24
 * @Version: V1.0
 */
@Api(tags="中铁一局拌合站生产流向")
@RestController
@RequestMapping("/bhzrenwudancar/bhzrenwudanCar")
@Slf4j
public class BhzrenwudanCarController extends JeecgController<BhzrenwudanCar, IBhzrenwudanCarService> {
	@Autowired
	private IBhzrenwudanCarService bhzrenwudanCarService;
	 @Value("${jeecg.path.upload}")
	 private String upLoadPath;
	 @Autowired
	 private ISchedulingCarService schedulingCarService;
	 @Autowired
	 private IBhzrenwudanService bhzrenwudanService;
	/**
	 * 分页列表查询
	 *
	 * @param bhzrenwudanCar
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "中铁一局拌合站生产流向-分页列表查询")
	@ApiOperation(value="中铁一局拌合站生产流向-分页列表查询", notes="中铁一局拌合站生产流向-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzrenwudanCar bhzrenwudanCar,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BhzrenwudanCar> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudanCar, req.getParameterMap());
		Page<BhzrenwudanCar> page = new Page<BhzrenwudanCar>(pageNo, pageSize);
		IPage<BhzrenwudanCar> pageList = bhzrenwudanCarService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bhzrenwudanCar
	 * @return
	 */
	@AutoLog(value = "中铁一局拌合站生产流向-添加")
	@ApiOperation(value="中铁一局拌合站生产流向-添加", notes="中铁一局拌合站生产流向-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzrenwudanCar bhzrenwudanCar) {
		bhzrenwudanCarService.save(bhzrenwudanCar);
		return Result.OK("添加成功！");
	}
	
//	/**
//	 *  编辑
//	 *
//	 * @param bhzrenwudanCar
//	 * @return
//	 */
//	@AutoLog(value = "中铁一局拌合站生产流向-编辑")
//	@ApiOperation(value="中铁一局拌合站生产流向-编辑", notes="中铁一局拌合站生产流向-编辑")
//	@PutMapping(value = "/edit")
//	public Result<?> edit(@RequestBody BhzrenwudanCar bhzrenwudanCar) {
//		bhzrenwudanCarService.updateById(bhzrenwudanCar);
//		return Result.OK("编辑成功!");
//	}

	 /**
	  *  编辑
	  *
	  * @param bhzrenwudanCar
	  * @return
	  */
	 @AutoLog(value = "中铁一局拌合站生产流向-编辑")
	 @ApiOperation(value="中铁一局拌合站生产流向-编辑", notes="中铁一局拌合站生产流向-编辑")
	 @PutMapping(value = "/edit")
	 public Result<?> edit(@RequestBody BhzrenwudanCar bhzrenwudanCar) {
		 //先更新发车单
		 QueryWrapper<SchedulingCar> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("id",bhzrenwudanCar.getId());
		 SchedulingCar schedulingCar = schedulingCarService.getOne(queryWrapper);
		 schedulingCar.setRecipe(bhzrenwudanCar.getRecipe());
		 schedulingCar.setProdmete(bhzrenwudanCar.getProdmete());
		 schedulingCar.setDgzmsj(bhzrenwudanCar.getDgzmsj());
		 schedulingCar.setXlsj(bhzrenwudanCar.getXlsj());
		 schedulingCar.setXwsj(bhzrenwudanCar.getXwsj());
		 schedulingCar.setZq(bhzrenwudanCar.getZq());
		 schedulingCar.setTaiz(bhzrenwudanCar.getTaiz());
		 schedulingCarService.updateById(schedulingCar);

		 //更新任务单
		 QueryWrapper<Bhzrenwudan> queryWrapper1 = new QueryWrapper<>();
		 queryWrapper1.eq("ID",bhzrenwudanCar.getBhzid());
		 Bhzrenwudan bhzrenwudan = bhzrenwudanService.getOne(queryWrapper1);
		 bhzrenwudan.setCustomer(bhzrenwudanCar.getCustomer());
		 bhzrenwudan.setNote(bhzrenwudanCar.getNote());
		 bhzrenwudanService.updateById(bhzrenwudan);
		 return Result.OK("编辑成功!");
	 }

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "中铁一局拌合站生产流向-通过id删除")
	@ApiOperation(value="中铁一局拌合站生产流向-通过id删除", notes="中铁一局拌合站生产流向-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzrenwudanCarService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "中铁一局拌合站生产流向-批量删除")
	@ApiOperation(value="中铁一局拌合站生产流向-批量删除", notes="中铁一局拌合站生产流向-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzrenwudanCarService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "中铁一局拌合站生产流向-通过id查询")
	@ApiOperation(value="中铁一局拌合站生产流向-通过id查询", notes="中铁一局拌合站生产流向-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzrenwudanCar bhzrenwudanCar = bhzrenwudanCarService.getById(id);
		if(bhzrenwudanCar==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzrenwudanCar);
	}

    /**
    * 导出excel
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request,String sys_depart_orgcode) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String dattim_begin1 = Arrays.toString(parameterMap.get("dattim_begin"));
		String dattim_end1 = Arrays.toString(parameterMap.get("dattim_end"));
		String dattim_begin = dattim_begin1.substring(1,dattim_begin1.length()-1);
		String dattim_end = dattim_end1.substring(1,dattim_end1.length()-1);
		if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
			sys_depart_orgcode = sys_depart_orgcode +"%";
		}
        return exportXlss(request,sys_depart_orgcode, dattim_begin,dattim_end, BhzrenwudanCar.class, "拌合站生产流向台账");
    }

	 /**
	  * 导出excel
	  */
	 protected ModelAndView exportXlss(HttpServletRequest request,String sys_depart_orgcode, String dattim_begin, String dattim_end, Class<BhzrenwudanCar> clazz, String title) {

		 List<BhzrenwudanCar> pageList = schedulingCarService.selecerenwudans(sys_depart_orgcode,dattim_begin,dattim_end);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 List<BhzrenwudanCar> exportList = null;
		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }
		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, clazz);
		 //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
		 ExportParams  exportParams=new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
		 exportParams.setImageBasePath(upLoadPath);
		 //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
		 mv.addObject(NormalExcelConstants.PARAMS,exportParams);
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
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
        return super.importExcel(request, response, BhzrenwudanCar.class);
    }

}
