package com.trtm.iot.rebarWzcailiaonamedictMan.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.json.JSONObject;
import com.trtm.iot.rebarComponent.entity.ComponentVo;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.Number0CaiLiao;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.Number0CaiLiaoVoS;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.RebarWzcailiaonamedictManVo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.RebarWzcailiaonamedictMan;
import com.trtm.iot.rebarWzcailiaonamedictMan.service.IRebarWzcailiaonamedictManService;

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
 * @Description: rebar_wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2024-12-10
 * @Version: V1.0
 */
@Api(tags="rebar_wzcailiaonamedict_man")
@RestController
@RequestMapping("/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan")
@Slf4j
public class RebarWzcailiaonamedictManController extends JeecgController<RebarWzcailiaonamedictMan, IRebarWzcailiaonamedictManService> {
	@Autowired
	private IRebarWzcailiaonamedictManService rebarWzcailiaonamedictManService;
	
	/**
	 * 分页列表查询
	 *
	 * @param rebarWzcailiaonamedictMan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "rebar_wzcailiaonamedict_man-分页列表查询")
	@ApiOperation(value="rebar_wzcailiaonamedict_man-分页列表查询", notes="rebar_wzcailiaonamedict_man-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RebarWzcailiaonamedictMan rebarWzcailiaonamedictMan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RebarWzcailiaonamedictMan> queryWrapper = QueryGenerator.initQueryWrapper(rebarWzcailiaonamedictMan, req.getParameterMap());
		Page<RebarWzcailiaonamedictMan> page = new Page<RebarWzcailiaonamedictMan>(pageNo, pageSize);
		IPage<RebarWzcailiaonamedictMan> pageList = rebarWzcailiaonamedictManService.page(page, queryWrapper);

		return Result.OK(pageList);
	}
	 @AutoLog(value = "原材料用量统计分页列表查询")
	 @ApiOperation(value = "原材料用量统计分页列表查询", notes = "原材料用量统计分页列表查询")
	 @GetMapping(value = "/rebarWzcailiaoList")
	 public Result<?> queryComponentPageList(
			 RebarWzcailiaonamedictManVo rebarWzcailiaonamedictMan,
			 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
			 String sys_depart_orgcode
	 ) {
		 String sys_depart_orgcodes = "1";
		 List<RebarWzcailiaonamedictManVo> rebarWzcailiaonamedictManVos = rebarWzcailiaonamedictManService.getRebarWzcailiaoList(rebarWzcailiaonamedictMan,pageNo,pageSize,sys_depart_orgcode,sys_depart_orgcodes);
		 Page<RebarWzcailiaonamedictManVo> result = new Page<>(pageNo, pageSize);
		 result.setTotal(rebarWzcailiaonamedictManVos.size());
		 result.setRecords(rebarWzcailiaonamedictManVos);
		 return Result.OK(result);
	 }

	 @AutoLog(value = "0号清单原材料-半成品加工页面")
	 @ApiOperation(value = "0号清单原材料", notes = "0号清单原材料")
	 @GetMapping(value = "/wzCailiao")
	 public Result<?> queryWzCailiao(
			 String wbsId,
			 String sysOrgCode,
			 String sysOrgCodes,
			 String token,
			 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
			 String sys_depart_orgcode
	 ) {
		 String sys_depart_orgcodes = "1";
		 List<Number0CaiLiao> back = rebarWzcailiaonamedictManService.getWzCailiao(wbsId,sysOrgCode,sysOrgCodes,token);
		 Page<RebarWzcailiaonamedictManVo> result = new Page<>(pageNo, pageSize);

		 return Result.OK(back);
	 }
	 //半成品加工添加
	 @AutoLog(value = "半成品加工添加")
	 @ApiOperation(value="半成品加工添加", notes="半成品加工添加")
	 @PostMapping(value = "/addWzCaiLiao")
	 public Result<?> addWzCaiLiao(@RequestBody Number0CaiLiaoVoS number0CaiLiaoVoS) {

		 boolean b = rebarWzcailiaonamedictManService.addWzCaiLiao(number0CaiLiaoVoS);
		 if (b){
			 return Result.OK("添加成功！");
		 }{
			 return Result.error("添加失败！");
		 }
		 //半成品添加时比如有两种材料,这俩材料组成一个新的构件,然后
		 //1.新构件,新任务编号加到任务构件中间表
		 //2.新构件带着两种材料添加到构件材料中间表
		 //3.看半成品材料数量是否改变,材料用量管理是否改变
		 //4.半成品加工后生成的构件会显示到构件管理，里面之前是可以编辑材料数量的，
		 // 编辑完下次选这个构件启动任务时，材料数量表数据要变的，变的话是否能超过这个部位的设计数量
	 }
	 //半成品加工添加
	 @AutoLog(value = "半成品加工添加构件,以及任务")
	 @ApiOperation(value="半成品加工添加构件,以及任务", notes="半成品加工添加构件,以及任务")
	 @PostMapping(value = "/addWzCaiLiaoComponentTask")
	 public Result<?> addWzCaiLiaoComponentTask(@RequestBody Number0CaiLiaoVoS number0CaiLiaoVoS) {

		 boolean b = rebarWzcailiaonamedictManService.addWzCaiLiaoComponentTask(number0CaiLiaoVoS);
		 if (b){
			 return Result.OK("添加成功！");
		 }{
			 return Result.error("添加失败！");
		 }
	}
	 @AutoLog(value = "获取token")
	 @ApiOperation(value = "获取token", notes = "获取token")
	 @GetMapping(value = "/getToken")
	 public Result<?> getToken() {
		 String token = rebarWzcailiaonamedictManService.getToken();

		 return Result.OK(token);
	 }
	
	/**
	 *   添加
	 *
	 * @param rebarWzcailiaonamedictMan
	 * @return
	 */
	@AutoLog(value = "rebar_wzcailiaonamedict_man-添加")
	@ApiOperation(value="rebar_wzcailiaonamedict_man-添加", notes="rebar_wzcailiaonamedict_man-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RebarWzcailiaonamedictMan rebarWzcailiaonamedictMan) {
		rebarWzcailiaonamedictManService.save(rebarWzcailiaonamedictMan);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param rebarWzcailiaonamedictMan
	 * @return
	 */
	@AutoLog(value = "rebar_wzcailiaonamedict_man-编辑")
	@ApiOperation(value="rebar_wzcailiaonamedict_man-编辑", notes="rebar_wzcailiaonamedict_man-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RebarWzcailiaonamedictMan rebarWzcailiaonamedictMan) {
		rebarWzcailiaonamedictManService.updateById(rebarWzcailiaonamedictMan);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_wzcailiaonamedict_man-通过id删除")
	@ApiOperation(value="rebar_wzcailiaonamedict_man-通过id删除", notes="rebar_wzcailiaonamedict_man-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		rebarWzcailiaonamedictManService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "rebar_wzcailiaonamedict_man-批量删除")
	@ApiOperation(value="rebar_wzcailiaonamedict_man-批量删除", notes="rebar_wzcailiaonamedict_man-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.rebarWzcailiaonamedictManService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_wzcailiaonamedict_man-通过id查询")
	@ApiOperation(value="rebar_wzcailiaonamedict_man-通过id查询", notes="rebar_wzcailiaonamedict_man-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RebarWzcailiaonamedictMan rebarWzcailiaonamedictMan = rebarWzcailiaonamedictManService.getById(id);
		if(rebarWzcailiaonamedictMan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(rebarWzcailiaonamedictMan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param rebarWzcailiaonamedictMan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RebarWzcailiaonamedictMan rebarWzcailiaonamedictMan) {
        return super.exportXls(request, rebarWzcailiaonamedictMan, RebarWzcailiaonamedictMan.class, "rebar_wzcailiaonamedict_man");
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
        return super.importExcel(request, response, RebarWzcailiaonamedictMan.class);
    }

}
