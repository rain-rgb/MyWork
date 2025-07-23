package com.trtm.iot.trhnthts.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.trhnthts.vo.TrHnthtPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.trtm.iot.trhnthts.service.ITrHnthtSService;

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
 * @Description: 混凝土回弹子表
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
@Api(tags="混凝土回弹子表")
@RestController
@RequestMapping("/trhnthts/trHnthtS")
@Slf4j
public class TrHnthtSController extends JeecgController<TrHnthtS, ITrHnthtSService> {
	@Autowired
	private ITrHnthtSService trHnthtSService;

	/**
	 * 分页列表查询
	 *
	 * @param trHnthtS
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "混凝土回弹子表-分页列表查询")
	@ApiOperation(value="混凝土回弹子表-分页列表查询", notes="混凝土回弹子表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrHnthtS trHnthtS,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrHnthtS> queryWrapper = QueryGenerator.initQueryWrapper(trHnthtS, req.getParameterMap());
		Page<TrHnthtS> page = new Page<TrHnthtS>(pageNo, pageSize);
		IPage<TrHnthtS> pageList = trHnthtSService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param trHnthtS
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "混凝土回弹子表-分页列表查询")
	 @ApiOperation(value="混凝土回弹子表-分页列表查询", notes="混凝土回弹子表-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(TrHnthtS trHnthtS,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<TrHnthtS> queryWrapper = QueryGenerator.initQueryWrapper(trHnthtS, req.getParameterMap());
		 Page<TrHnthtS> page = new Page<TrHnthtS>(pageNo, pageSize);
		 IPage<TrHnthtS> pageList = trHnthtSService.page(page, queryWrapper);
		 List<TrHnthtS> records = pageList.getRecords();
		 List lists=new ArrayList();
		 Map map=new HashMap();
		 for (TrHnthtS record : records) {
			 TrHnthtPage hnthtPage=new TrHnthtPage();
			 List list=new ArrayList();
			 hnthtPage.setAverage(record.getAverage());
			 hnthtPage.setCalangle(record.getCalangle());
			 hnthtPage.setCalsurface(record.getCalsurface());
			 hnthtPage.setCarbonization(record.getCarbonization());
			 hnthtPage.setCarbonize(record.getCarbonize());
			 hnthtPage.setFlagcarbonization(record.getFlagcarbonization());
			 hnthtPage.setId(record.getId());
			 hnthtPage.setIspumping(record.getIspumping());
			 hnthtPage.setNumber(record.getNumber());
			 hnthtPage.setPouringsurface(record.getPouringsurface());
			 hnthtPage.setStandardid(record.getStandardid());
			 hnthtPage.setStrcar(record.getStrcar());
			 hnthtPage.setTestid(record.getTestid());
			 hnthtPage.setSurface(record.getSurface());
			 hnthtPage.setCarbonizetwo(record.getCarbonizetwo());
			 hnthtPage.setCarbonizethree(record.getCarbonizethree());
			 String number = record.getNumber();
			 String[] split = number.split(",");
			 for (String s : split) {
				 list.add(s);
			 }
			 hnthtPage.setCequlist(list);
			 lists.add(hnthtPage);

		 }
		 map.put("current", pageList.getCurrent());
		 map.put("pages", pageList.getPages());
		 map.put("size", pageList.getSize());
		 map.put("total", pageList.getTotal());
		 map.put("records", lists);
		 return Result.OK(map);
	 }

	/**
	 *   添加
	 *
	 * @param trHnthtS
	 * @return
	 */
	@AutoLog(value = "混凝土回弹子表-添加")
	@ApiOperation(value="混凝土回弹子表-添加", notes="混凝土回弹子表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrHnthtS trHnthtS) {
		trHnthtSService.save(trHnthtS);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param trHnthtS
	 * @return
	 */
	@AutoLog(value = "混凝土回弹子表-编辑")
	@ApiOperation(value="混凝土回弹子表-编辑", notes="混凝土回弹子表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrHnthtS trHnthtS) {
		trHnthtSService.updateById(trHnthtS);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土回弹子表-通过id删除")
	@ApiOperation(value="混凝土回弹子表-通过id删除", notes="混凝土回弹子表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trHnthtSService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "混凝土回弹子表-批量删除")
	@ApiOperation(value="混凝土回弹子表-批量删除", notes="混凝土回弹子表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trHnthtSService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "混凝土回弹子表-通过id查询")
	@ApiOperation(value="混凝土回弹子表-通过id查询", notes="混凝土回弹子表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrHnthtS trHnthtS = trHnthtSService.getById(id);
		if(trHnthtS==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trHnthtS);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trHnthtS
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrHnthtS trHnthtS) {
        return super.exportXls(request, trHnthtS, TrHnthtS.class, "混凝土回弹子表");
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
        return super.importExcel(request, response, TrHnthtS.class);
    }

}
