package com.trtm.iot.zhiliangqrcode.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zhiliangqrcode.entity.Zhiliangqrcode;
import com.trtm.iot.zhiliangqrcode.service.IZhiliangqrcodeService;

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
 * @Description: zhiliangqrcode
 * @Author: jeecg-boot
 * @Date:   2022-08-10
 * @Version: V1.0
 */
@Api(tags="zhiliangqrcode")
@RestController
@RequestMapping("/zhiliangqrcode/zhiliangqrcode")
@Slf4j
public class ZhiliangqrcodeController extends JeecgController<Zhiliangqrcode, IZhiliangqrcodeService> {
	@Autowired
	private IZhiliangqrcodeService zhiliangqrcodeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param zhiliangqrcode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "zhiliangqrcode-分页列表查询")
	@ApiOperation(value="zhiliangqrcode-分页列表查询", notes="zhiliangqrcode-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Zhiliangqrcode zhiliangqrcode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Zhiliangqrcode> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangqrcode, req.getParameterMap());
		Page<Zhiliangqrcode> page = new Page<Zhiliangqrcode>(pageNo, pageSize);
		IPage<Zhiliangqrcode> pageList = zhiliangqrcodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param zhiliangqrcode
	 * @return
	 */
	@AutoLog(value = "zhiliangqrcode-添加")
	@ApiOperation(value="zhiliangqrcode-添加", notes="zhiliangqrcode-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Zhiliangqrcode zhiliangqrcode) {
		zhiliangqrcodeService.save(zhiliangqrcode);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param zhiliangqrcode
	 * @return
	 */
	@AutoLog(value = "zhiliangqrcode-编辑")
	@ApiOperation(value="zhiliangqrcode-编辑", notes="zhiliangqrcode-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Zhiliangqrcode zhiliangqrcode) {
		zhiliangqrcodeService.updateById(zhiliangqrcode);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "zhiliangqrcode-通过id删除")
	@ApiOperation(value="zhiliangqrcode-通过id删除", notes="zhiliangqrcode-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zhiliangqrcodeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "zhiliangqrcode-批量删除")
	@ApiOperation(value="zhiliangqrcode-批量删除", notes="zhiliangqrcode-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zhiliangqrcodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "zhiliangqrcode-通过id查询")
	@ApiOperation(value="zhiliangqrcode-通过id查询", notes="zhiliangqrcode-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Zhiliangqrcode zhiliangqrcode = zhiliangqrcodeService.getById(id);
		if(zhiliangqrcode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zhiliangqrcode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zhiliangqrcode
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Zhiliangqrcode zhiliangqrcode) {
        return super.exportXls(request, zhiliangqrcode, Zhiliangqrcode.class, "zhiliangqrcode");
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
        return super.importExcel(request, response, Zhiliangqrcode.class);
    }

}
