package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wzyclgbjilu.entity.Wzyclgbjilu;
import com.trtm.iot.wzyclgbjilu.service.IWzyclgbjiluService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysUserCar;
import org.jeecg.modules.system.service.ISysUserCarService;

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

 /**
 * @Description: 司机用户关联车牌
 * @Author: jeecg-boot
 * @Date:   2021-06-25
 * @Version: V1.0
 */
@Api(tags="司机用户关联车牌")
@RestController
@RequestMapping("/system/sysUserCar")
@Slf4j
public class SysUserCarController extends JeecgController<SysUserCar, ISysUserCarService> {
	@Autowired
	private ISysUserCarService sysUserCarService;
	 @Autowired
	 private IWzycljinchanggbService wzycljinchanggbService;
	 @Autowired
	 private IWzyclgbjiluService wzyclgbjiluService;
	/**
	 * 分页列表查询
	 *
	 * @param sysUserCar
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "司机用户关联车牌-分页列表查询")
	@ApiOperation(value="司机用户关联车牌-分页列表查询", notes="司机用户关联车牌-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysUserCar sysUserCar,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysUserCar> queryWrapper = QueryGenerator.initQueryWrapper(sysUserCar, req.getParameterMap());
		Page<SysUserCar> page = new Page<SysUserCar>(pageNo, pageSize);
		IPage<SysUserCar> pageList = sysUserCarService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param wzycljinchanggb
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "原材料收料主表-查询")
	 @ApiOperation(value="原材料收料主表-查询", notes="原材料收料主表-查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(Wzycljinchanggb wzycljinchanggb,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req, @RequestParam(name = "userid") String userid ) {
		 List<SysUserCar> selectsysusercar = sysUserCarService.selectsysusercar(userid);
		 String shebei="无";
		 if(selectsysusercar.size()>0){
			 shebei="";
			 for (SysUserCar sysUserCar : selectsysusercar) {
				 if(shebei.equals("")){
					 shebei=""+sysUserCar.getVehicle()+"";
				 }else{
					 shebei = shebei + "," + sysUserCar.getVehicle()+"";
				 }
			 }
		 }
		 if (shebei != null){
		 	wzycljinchanggb.setQianchepai(shebei);
		 }
		 QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
		 Page<Wzycljinchanggb> page = new Page<Wzycljinchanggb>(pageNo, pageSize);
		 IPage<Wzycljinchanggb> pageList = wzycljinchanggbService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 根据车牌列表查询
	  *
	  * @param wzyclgbjilu
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "物资过磅数据记录表-根据车牌列表查询")
	 @ApiOperation(value="物资过磅数据记录表-根据车牌列表查询", notes="物资过磅数据记录表-根据车牌列表查询")
	 @GetMapping(value = "/list3")
	 public Result<?> queryPageList1(Wzyclgbjilu wzyclgbjilu,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req, @RequestParam(name = "userid") String userid) {
		 List<SysUserCar> selectsysusercar = sysUserCarService.selectsysusercar(userid);
		 String shebei="无";
		 if(selectsysusercar.size()>0){
			 shebei="";
			 for (SysUserCar sysUserCar : selectsysusercar) {
				 if(shebei.equals("")){
					 shebei=sysUserCar.getVehicle();
				 }else{
					 shebei = shebei + "," + sysUserCar.getVehicle();
				 }
			 }
		 }
		 wzyclgbjilu.setQianchepai(shebei);
		 wzyclgbjilu.setStatus("0");
		 QueryWrapper<Wzyclgbjilu> queryWrapper = QueryGenerator.initQueryWrapper(wzyclgbjilu, req.getParameterMap());
		 Page<Wzyclgbjilu> page = new Page<Wzyclgbjilu>(pageNo, pageSize);
		 IPage<Wzyclgbjilu> pageList = wzyclgbjiluService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  *
	  * @param sysUserCar
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "司机用户关联车牌-列表查询")
	 @ApiOperation(value="司机用户关联车牌-列表查询", notes="司机用户关联车牌-列表查询")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(SysUserCar sysUserCar,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
		 QueryWrapper<SysUserCar> queryWrapper = QueryGenerator.initQueryWrapper(sysUserCar, req.getParameterMap());
		 List<SysUserCar> list = sysUserCarService.list(queryWrapper);
		 return Result.OK(list);
	 }

	/**
	 *   添加
	 *
	 * @param sysUserCar
	 * @return
	 */
	@AutoLog(value = "司机用户关联车牌-添加")
	@ApiOperation(value="司机用户关联车牌-添加", notes="司机用户关联车牌-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysUserCar sysUserCar) {
		sysUserCarService.save(sysUserCar);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param sysUserCar
	 * @return
	 */
	@AutoLog(value = "司机用户关联车牌-编辑")
	@ApiOperation(value="司机用户关联车牌-编辑", notes="司机用户关联车牌-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysUserCar sysUserCar) {
		sysUserCarService.updateById(sysUserCar);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "司机用户关联车牌-通过id删除")
	@ApiOperation(value="司机用户关联车牌-通过id删除", notes="司机用户关联车牌-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysUserCarService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "司机用户关联车牌-批量删除")
	@ApiOperation(value="司机用户关联车牌-批量删除", notes="司机用户关联车牌-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysUserCarService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "司机用户关联车牌-通过id查询")
	@ApiOperation(value="司机用户关联车牌-通过id查询", notes="司机用户关联车牌-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysUserCar sysUserCar = sysUserCarService.getById(id);
		if(sysUserCar==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysUserCar);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysUserCar
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysUserCar sysUserCar) {
        return super.exportXls(request, sysUserCar, SysUserCar.class, "司机用户关联车牌");
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
        return super.importExcel(request, response, SysUserCar.class);
    }

}
