package com.trtm.iot.bhzcfg.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.util.RedisUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.vo.BhzCallCfgPage;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 拌合站超标配置
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@Api(tags="拌合站超标配置")
@RestController
@RequestMapping("/bhzcfg/bhzCallCfg")
@Slf4j
public class BhzCallCfgController {
	@Autowired
	private IBhzCallCfgService bhzCallCfgService;
	@Autowired
	private IBhzChaobiaoCfgService bhzChaobiaoCfgService;
	 @Autowired
	 private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param bhzCallCfg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "拌合站超标配置-分页列表查询")
	@ApiOperation(value="拌合站超标配置-分页列表查询", notes="拌合站超标配置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BhzCallCfg bhzCallCfg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String sys_depart_orgcode,
								   HttpServletRequest req) {
		if(sys_depart_orgcode!=null){
			bhzCallCfg.setSysOrgCode(sys_depart_orgcode+"*");//前模糊查询
		}else{
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
			bhzCallCfg.setSysOrgCode(loginUser.getOrgCode()+"*");//前模糊查询
		}
		QueryWrapper<BhzCallCfg> queryWrapper = QueryGenerator.initQueryWrapper(bhzCallCfg, req.getParameterMap());
		Page<BhzCallCfg> page = new Page<BhzCallCfg>(pageNo, pageSize);
		IPage<BhzCallCfg> pageList = bhzCallCfgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param bhzCallCfgPage
	 * @return
	 */
	@AutoLog(value = "拌合站超标配置-添加")
	@ApiOperation(value="拌合站超标配置-添加", notes="拌合站超标配置-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BhzCallCfgPage bhzCallCfgPage) {
		BhzCallCfg bhzCallCfg = new BhzCallCfg();
		BeanUtils.copyProperties(bhzCallCfgPage, bhzCallCfg);
		bhzCallCfgService.saveMain(bhzCallCfg, bhzCallCfgPage.getBhzChaobiaoCfgList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param bhzCallCfgPage
	 * @return
	 */
	@AutoLog(value = "拌合站超标配置-编辑")
	@ApiOperation(value="拌合站超标配置-编辑", notes="拌合站超标配置-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BhzCallCfgPage bhzCallCfgPage) {
		BhzCallCfg bhzCallCfg = new BhzCallCfg();
		BeanUtils.copyProperties(bhzCallCfgPage, bhzCallCfg);
		BhzCallCfg bhzCallCfgEntity = bhzCallCfgService.getById(bhzCallCfg.getId());
		if(bhzCallCfgEntity==null) {
			return Result.error("未找到对应数据");
		}
		bhzCallCfgService.updateMain(bhzCallCfg, bhzCallCfgPage.getBhzChaobiaoCfgList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拌合站超标配置-通过id删除")
	@ApiOperation(value="拌合站超标配置-通过id删除", notes="拌合站超标配置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bhzCallCfgService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "拌合站超标配置-批量删除")
	@ApiOperation(value="拌合站超标配置-批量删除", notes="拌合站超标配置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bhzCallCfgService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拌合站超标配置-通过id查询")
	@ApiOperation(value="拌合站超标配置-通过id查询", notes="拌合站超标配置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BhzCallCfg bhzCallCfg = bhzCallCfgService.getById(id);
		if(bhzCallCfg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bhzCallCfg);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "拌合站超标配置子表通过主表ID查询")
	@ApiOperation(value="拌合站超标配置子表主表ID查询", notes="拌合站超标配置子表-通主表ID查询")
	@GetMapping(value = "/queryBhzChaobiaoCfgByMainId")
	public Result<?> queryBhzChaobiaoCfgListByMainId(@RequestParam(name="id",required=true) String id) {
		List<BhzChaobiaoCfg> bhzChaobiaoCfgList = bhzChaobiaoCfgService.selectByMainIds(id);
		return Result.OK(bhzChaobiaoCfgList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bhzCallCfg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzCallCfg bhzCallCfg) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<BhzCallCfg> queryWrapper = QueryGenerator.initQueryWrapper(bhzCallCfg, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<BhzCallCfg> queryList = bhzCallCfgService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<BhzCallCfg> bhzCallCfgList = new ArrayList<BhzCallCfg>();
      if(oConvertUtils.isEmpty(selections)) {
          bhzCallCfgList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          bhzCallCfgList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<BhzCallCfgPage> pageList = new ArrayList<BhzCallCfgPage>();
      for (BhzCallCfg main : bhzCallCfgList) {
          BhzCallCfgPage vo = new BhzCallCfgPage();
          BeanUtils.copyProperties(main, vo);
          List<BhzChaobiaoCfg> bhzChaobiaoCfgList = bhzChaobiaoCfgService.selectByMainIds(main.getUid());
          vo.setBhzChaobiaoCfgList(bhzChaobiaoCfgList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "拌合站超标配置列表");
      mv.addObject(NormalExcelConstants.CLASS, BhzCallCfgPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拌合站超标配置数据", "导出人:"+sysUser.getRealname(), "拌合站超标配置"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<BhzCallCfgPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BhzCallCfgPage.class, params);
              for (BhzCallCfgPage page : list) {
                  BhzCallCfg po = new BhzCallCfg();
                  BeanUtils.copyProperties(page, po);
                  bhzCallCfgService.saveMain(po, page.getBhzChaobiaoCfgList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
