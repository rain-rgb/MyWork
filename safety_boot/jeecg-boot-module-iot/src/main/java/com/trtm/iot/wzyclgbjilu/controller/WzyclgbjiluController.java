package com.trtm.iot.wzyclgbjilu.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzyclgbjilu.entity.Wzyclgbjilu;
import com.trtm.iot.wzyclgbjilu.service.IWzyclgbjiluService;

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
 * @Description: 物资过磅数据记录表
 * @Author: jeecg-boot
 * @Date:   2022-09-05
 * @Version: V1.0
 */
@Api(tags="物资过磅数据记录表")
@RestController
@RequestMapping("/wzyclgbjilu/wzyclgbjilu")
@Slf4j
public class WzyclgbjiluController extends JeecgController<Wzyclgbjilu, IWzyclgbjiluService> {
	@Autowired
	private IWzyclgbjiluService wzyclgbjiluService;
	 @Autowired
	 private IWzycljinchanggbService wzycljinchanggbService;
	 @Autowired
	 private IWzliaocangService wzliaocangService;

	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param wzyclgbjilu
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "物资过磅数据记录表-分页列表查询")
	@ApiOperation(value="物资过磅数据记录表-分页列表查询", notes="物资过磅数据记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Wzyclgbjilu wzyclgbjilu,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if (wzyclgbjilu.getShebeibianhao() == null) {
			if (!"null".equals(shebei)) {
				wzyclgbjilu.setShebeibianhao(shebei);
			}else {
				wzyclgbjilu.setShebeibianhao("空");
			}
		}
		QueryWrapper<Wzyclgbjilu> queryWrapper = QueryGenerator.initQueryWrapper(wzyclgbjilu, req.getParameterMap());
		Page<Wzyclgbjilu> page = new Page<Wzyclgbjilu>(pageNo, pageSize);
		IPage<Wzyclgbjilu> pageList = wzyclgbjiluService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param wzyclgbjilu
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "物资过磅数据记录表-分页列表查询")
	 @ApiOperation(value="物资过磅数据记录表-分页列表查询", notes="物资过磅数据记录表-分页列表查询")
	 @GetMapping(value = "/list1")
	 public Result<?> queryPageList1(Wzyclgbjilu wzyclgbjilu,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<Wzyclgbjilu> queryWrapper = QueryGenerator.initQueryWrapper(wzyclgbjilu, req.getParameterMap());
		 Page<Wzyclgbjilu> page = new Page<Wzyclgbjilu>(pageNo, pageSize);
		 IPage<Wzyclgbjilu> pageList = wzyclgbjiluService.page(page, queryWrapper);
		 for (Wzyclgbjilu wzyclgbjilu1 : pageList.getRecords()){
			 String lcbianhao = wzyclgbjilu1.getLcbianhao();
			 if (lcbianhao!=null){
			 	QueryWrapper<Wzliaocang> wzliaocangQueryWrapper = new QueryWrapper<>();
			 	wzliaocangQueryWrapper.eq("guid",lcbianhao);
				 Wzliaocang one = wzliaocangService.getOne(wzliaocangQueryWrapper);
				 wzyclgbjilu1.setLcbianhao(one.getName());
			 }
		 }
		 return Result.OK(pageList);
	 }



	/**
	 *   添加
	 *
	 * @param wzyclgbjilu
	 * @return
	 */
	@AutoLog(value = "物资过磅数据记录表-添加")
	@ApiOperation(value="物资过磅数据记录表-添加", notes="物资过磅数据记录表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Wzyclgbjilu wzyclgbjilu) {
		wzyclgbjiluService.save(wzyclgbjilu);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wzyclgbjilu
	 * @return
	 */
	@AutoLog(value = "物资过磅数据记录表-编辑")
	@ApiOperation(value="物资过磅数据记录表-编辑", notes="物资过磅数据记录表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Wzyclgbjilu wzyclgbjilu) {
		wzyclgbjiluService.updateById(wzyclgbjilu);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物资过磅数据记录表-通过id删除")
	@ApiOperation(value="物资过磅数据记录表-通过id删除", notes="物资过磅数据记录表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wzyclgbjiluService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物资过磅数据记录表-批量删除")
	@ApiOperation(value="物资过磅数据记录表-批量删除", notes="物资过磅数据记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wzyclgbjiluService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物资过磅数据记录表-通过id查询")
	@ApiOperation(value="物资过磅数据记录表-通过id查询", notes="物资过磅数据记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Wzyclgbjilu wzyclgbjilu = wzyclgbjiluService.getById(id);
		if(wzyclgbjilu==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wzyclgbjilu);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wzyclgbjilu
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzyclgbjilu wzyclgbjilu) {
        return super.exportXls(request, wzyclgbjilu, Wzyclgbjilu.class, "物资过磅数据记录表");
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
        return super.importExcel(request, response, Wzyclgbjilu.class);
    }

}
