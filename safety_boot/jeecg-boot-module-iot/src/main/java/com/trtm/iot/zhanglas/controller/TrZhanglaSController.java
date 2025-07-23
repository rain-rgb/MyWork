package com.trtm.iot.zhanglas.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.vo.TrZhanglaSList;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: 张拉信息子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
@Api(tags="张拉信息子表")
@RestController
@RequestMapping("/zhanglas/trZhanglaS")
@Slf4j
public class TrZhanglaSController extends JeecgController<TrZhanglaS, ITrZhanglaSService> {
	@Autowired
	private ITrZhanglaSService trZhanglaSService;
	 @Autowired
	 private ITrZhanglaMService trZhanglaMService;

	/**
	 * 分页列表查询
	 *
	 * @param trZhanglaS
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "张拉信息子表-分页列表查询")
	@ApiOperation(value="张拉信息子表-分页列表查询", notes="张拉信息子表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TrZhanglaS trZhanglaS,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrZhanglaS> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaS, req.getParameterMap());
		Page<TrZhanglaS> page = new Page<TrZhanglaS>(pageNo, pageSize);
		IPage<TrZhanglaS> pageList = trZhanglaSService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	/**
	 *   添加
	 *
	 * @param trZhanglaS
	 * @return
	 */
	@AutoLog(value = "张拉信息子表-添加")
	@ApiOperation(value="张拉信息子表-添加", notes="张拉信息子表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TrZhanglaS trZhanglaS) {
		QueryWrapper<TrZhanglaS> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("sid",trZhanglaS.getSid());
		TrZhanglaS one = trZhanglaSService.getOne(queryWrapper);
		if (one != null){
			trZhanglaS.setId(one.getId());
			trZhanglaSService.updateById(trZhanglaS);
		}else {
			trZhanglaSService.save(trZhanglaS);
		}
		return Result.OK("添加成功！");
	}


	 /**
	  *   添加
	  *
	  * @param trZhanglaSList
	  * @return
	  */
	 @AutoLog(value = "张拉信息子表-添加")
	 @ApiOperation(value="张拉信息子表-添加", notes="张拉信息子表-添加")
	 @PostMapping(value = "/addList")
	 public Result<?> addList(@RequestBody TrZhanglaSList trZhanglaSList) {
		 trZhanglaSService.saveMain(trZhanglaSList.getTrZhanglaS());
//		 try {
//			 elongateNum(trZhanglaSList.getTrZhanglaS());//计算伸长量
//		 } catch (Exception e) {
//			 e.printStackTrace();
//		 }
		 return Result.OK("添加成功！");
	 }

	 //计算伸长量
	 public void elongateNum(List<TrZhanglaS> trZhanglaS){
		 TrZhanglaS trZhanglaS1 = trZhanglaS.get(0);//一个孔道为一组上传，拿fguid，
		 String fguid = trZhanglaS1.getFguid();
		 String syjid = trZhanglaS1.getSyjid();

		 double sum = 0.0;
		 //查询刚刚上传的数据，是否为50，二次张拉
		 List<TrZhanglaS> trZhanglaS2 = trZhanglaSService.selelctLists(syjid, fguid, 50);
		 List<TrZhanglaS> trZhanglaS3 = trZhanglaSService.selelctList(syjid, fguid);

		 //计算功能
		 if (trZhanglaS2.size() > 0){
			if (trZhanglaS2.size() == 4){//
				double sum1 = 0.0;
				double sum2 = 0.0;
				//计算dh等于1的
				List<TrZhanglaS> trZhanglaS4 = trZhanglaSService.selelctListbybd(syjid, fguid, 1);
				if(trZhanglaS4.size() == 5){
					double l1 = 0.0;
					double l2 = 0.0;
					double l3 = 0.0;
					double l4 = 0.0;
					double l5 = 0.0;
					l1 = Double.parseDouble(trZhanglaS4.get(0).getJdscl());
					l2 = Double.parseDouble(trZhanglaS4.get(1).getJdscl());
					l3 = Double.parseDouble(trZhanglaS4.get(2).getJdscl());
					l4 = Double.parseDouble(trZhanglaS4.get(3).getJdscl());
					l5 = Double.parseDouble(trZhanglaS4.get(4).getJdscl());

					sum1 = (l3 + l2 - 2*l1) + (l5 - l4);
				}
				//计算dh等于2的
				List<TrZhanglaS> trZhanglaS5 = trZhanglaSService.selelctListbybd(syjid, fguid, 2);
				if(trZhanglaS5.size() == 5){
					double l1 = 0.0;
					double l2 = 0.0;
					double l3 = 0.0;
					double l4 = 0.0;
					double l5 = 0.0;
					l1 = Double.parseDouble(trZhanglaS5.get(0).getJdscl());
					l2 = Double.parseDouble(trZhanglaS5.get(1).getJdscl());
					l3 = Double.parseDouble(trZhanglaS5.get(2).getJdscl());
					l4 = Double.parseDouble(trZhanglaS5.get(3).getJdscl());
					l5 = Double.parseDouble(trZhanglaS5.get(4).getJdscl());

					sum1 = (l3 + l2 - 2*l1) + (l5 - l4);
				}
				sum = sum1 + sum2;
			}
		 }else {
			if (trZhanglaS3.size() == 6){//六条数据全，再计算
				double sum1 = 0.0;
				double sum2 = 0.0;
				//计算dh等于1的
				List<TrZhanglaS> trZhanglaS4 = trZhanglaSService.selelctListbybd(syjid, fguid, 1);
				for (TrZhanglaS zhanglaS :trZhanglaS4){
					double l1 = 0.0;
					double l2 = 0.0;
					double l3 = 0.0;
					if (zhanglaS.getJdbfb().equals("10")){
						l1 = Double.parseDouble(zhanglaS.getJdscl());
					}else if (zhanglaS.getJdbfb().equals("20")){
						l2 = Double.parseDouble(zhanglaS.getJdscl());
					}else if (zhanglaS.getJdbfb().equals("100")){
						l3 = Double.parseDouble(zhanglaS.getJdscl());
					}
					sum1 = l3 + l2 - 2*l1;
				}
				//计算dh等于2的
				List<TrZhanglaS> trZhanglaS5 = trZhanglaSService.selelctListbybd(syjid, fguid, 2);
				for (TrZhanglaS zhanglaS :trZhanglaS5){
					double l1 = 0.0;
					double l2 = 0.0;
					double l3 = 0.0;
					if (zhanglaS.getJdbfb().equals("10")){
						l1 = Double.parseDouble(zhanglaS.getJdscl());
					}else if (zhanglaS.getJdbfb().equals("20")){
						l2 = Double.parseDouble(zhanglaS.getJdscl());
					}else if (zhanglaS.getJdbfb().equals("100")){
						l3 = Double.parseDouble(zhanglaS.getJdscl());
					}
					sum2 = l3 + l2 - 2*l1;
				}
				sum = sum1 + sum2;
			}
		 }

		 //计算出来，放进去
		 QueryWrapper<TrZhanglaM> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("fguid",fguid);
		 TrZhanglaM one = trZhanglaMService.getOne(queryWrapper);
		 one.setScljs(sum);
		 trZhanglaMService.updateById(one);
	 }


	/**
	 *  编辑
	 *
	 * @param trZhanglaS
	 * @return
	 */
	@AutoLog(value = "张拉信息子表-编辑")
	@ApiOperation(value="张拉信息子表-编辑", notes="张拉信息子表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TrZhanglaS trZhanglaS) {
		trZhanglaSService.updateById(trZhanglaS);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉信息子表-通过id删除")
	@ApiOperation(value="张拉信息子表-通过id删除", notes="张拉信息子表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		trZhanglaSService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "张拉信息子表-批量删除")
	@ApiOperation(value="张拉信息子表-批量删除", notes="张拉信息子表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trZhanglaSService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "张拉信息子表-通过id查询")
	@ApiOperation(value="张拉信息子表-通过id查询", notes="张拉信息子表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TrZhanglaS trZhanglaS = trZhanglaSService.getById(id);
		if(trZhanglaS==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trZhanglaS);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trZhanglaS
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrZhanglaS trZhanglaS) {
        return super.exportXls(request, trZhanglaS, TrZhanglaS.class, "张拉信息子表");
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
        return super.importExcel(request, response, TrZhanglaS.class);
    }

}
