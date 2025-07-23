package com.trtm.iot.wzliaocang.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzliaocang.entity.WzliaocangChange;
import com.trtm.iot.wzliaocang.service.IWzliaocangChangeService;

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
 * @Description: wzliaocang_change
 * @Author: jeecg-boot
 * @Date:   2024-05-21
 * @Version: V1.0
 */
@Api(tags="wzliaocang_change")
@RestController
@RequestMapping("/wzliaocang/wzliaocangChange")
@Slf4j
public class WzliaocangChangeController extends JeecgController<WzliaocangChange, IWzliaocangChangeService> {
	@Autowired
	private IWzliaocangChangeService wzliaocangChangeService;
	 @Autowired
	 private IWzliaocangService wzliaocangService;
	 @Autowired
	 private IPushandreturnService pushandreturnService;

	/**
	 * 分页列表查询
	 *
	 * @param wzliaocangChange
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wzliaocang_change-分页列表查询")
	@ApiOperation(value="wzliaocang_change-分页列表查询", notes="wzliaocang_change-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WzliaocangChange wzliaocangChange,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WzliaocangChange> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocangChange, req.getParameterMap());
		Page<WzliaocangChange> page = new Page<WzliaocangChange>(pageNo, pageSize);
		IPage<WzliaocangChange> pageList = wzliaocangChangeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wzliaocangChange
	 * @return
	 */
	@AutoLog(value = "wzliaocang_change-添加")
	@ApiOperation(value="wzliaocang_change-添加", notes="wzliaocang_change-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WzliaocangChange wzliaocangChange) {
		wzliaocangChangeService.save(wzliaocangChange);
		return Result.OK("添加成功！");
	}

	 @AutoLog(value = "wzliaocang_change-添加")
	 @ApiOperation(value="wzliaocang_change-添加", notes="wzliaocang_change-添加")
	 @PostMapping(value = "/wzliaocangChange", consumes = "text/plain")
	 public Result<?> wzliaocangChange(@RequestBody String text) {
		String msg = "";
		 String[] split = text.split("\r\n");
		 String weight = split[1].split(",")[0].substring(3);
		 String lwno = split[0].startsWith("\ufeff")?split[0].substring(1):split[0];
		 List<Wzliaocang> wzliaocangs = wzliaocangService.lclistBylw(lwno);
		 if(wzliaocangs.size()>0){
			 for(Wzliaocang lc :wzliaocangs){
				 WzliaocangChange wzliaocangChange = new WzliaocangChange();
				 wzliaocangChange.setLiaoweino(lc.getLiaoweino());
				 wzliaocangChange.setLiaocangguid(lc.getGuid());
				 wzliaocangChange.setLcWeight(weight);
				 wzliaocangChange.setUsepeople("设备测量");
				 wzliaocangChange.setRemark("料位重量上传");
				 wzliaocangChangeService.save(wzliaocangChange);
				 wzliaocangService.updateWeightBylcNo(lc.getGuid(),weight);
			 }
			 msg = lwno+"重量"+weight+"修改成功！";


		 }else{
			 msg = lwno+"暂无匹配对应料仓";

		 }
		 // 把数据记录表内
		// pushandreturnService.saveData(778,text,"料位数据修改",msg);
		 return Result.OK(msg);
	 }

	/**
	 *  编辑
	 *
	 * @param wzliaocangChange
	 * @return
	 */
	@AutoLog(value = "wzliaocang_change-编辑")
	@ApiOperation(value="wzliaocang_change-编辑", notes="wzliaocang_change-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WzliaocangChange wzliaocangChange) {
		wzliaocangChangeService.updateById(wzliaocangChange);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzliaocang_change-通过id删除")
	@ApiOperation(value="wzliaocang_change-通过id删除", notes="wzliaocang_change-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzliaocangChangeService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wzliaocang_change-批量删除")
	@ApiOperation(value="wzliaocang_change-批量删除", notes="wzliaocang_change-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzliaocangChangeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wzliaocang_change-通过id查询")
	@ApiOperation(value="wzliaocang_change-通过id查询", notes="wzliaocang_change-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WzliaocangChange wzliaocangChange = wzliaocangChangeService.getById(id);
		if(wzliaocangChange==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzliaocangChange);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzliaocangChange
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzliaocangChange wzliaocangChange) {
        return super.exportXls(request, wzliaocangChange, WzliaocangChange.class, "wzliaocang_change");
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
        return super.importExcel(request, response, WzliaocangChange.class);
    }

}
