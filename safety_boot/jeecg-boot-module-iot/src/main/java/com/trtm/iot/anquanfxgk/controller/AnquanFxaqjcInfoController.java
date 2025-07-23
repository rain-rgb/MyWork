package com.trtm.iot.anquanfxgk.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkBase;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkBaseService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.anquanfxgk.entity.AnquanFxaqjcInfo;
import com.trtm.iot.anquanfxgk.service.IAnquanFxaqjcInfoService;

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
 * @Description: anquan_fxaqjc_info
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Api(tags="anquan_fxaqjc_info")
@RestController
@RequestMapping("/anquanfxgk/anquanFxaqjcInfo")
@Slf4j
public class AnquanFxaqjcInfoController extends JeecgController<AnquanFxaqjcInfo, IAnquanFxaqjcInfoService> {
	@Autowired
	private IAnquanFxaqjcInfoService anquanFxaqjcInfoService;
	@Autowired
	private IAnquanFxfjgkBaseService anquanFxfjgkBaseService;

	/**
	 * 分页列表查询
	 *
	 * @param anquanFxaqjcInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_info-分页列表查询")
	@ApiOperation(value="anquan_fxaqjc_info-分页列表查询", notes="anquan_fxaqjc_info-分页列表查询")
	@GetMapping(value = "/list")
	@PermissionData(pageComponent ="anquan/safety/SuperviseCheckList,anquan/safety/HazardCleared")
			public Result<?> queryPageList(AnquanFxaqjcInfo anquanFxaqjcInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		anquanFxaqjcInfo.setSysOrgCode(sys_depart_orgcode+"*");
		QueryWrapper<AnquanFxaqjcInfo> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxaqjcInfo, req.getParameterMap());
		Page<AnquanFxaqjcInfo> page = new Page<AnquanFxaqjcInfo>(pageNo, pageSize);
		IPage<AnquanFxaqjcInfo> pageList = anquanFxaqjcInfoService.page(page, queryWrapper);
		for (AnquanFxaqjcInfo item:pageList.getRecords()){
			if (item.getManageLocationid()!=null){
                AnquanFxfjgkBase anquanFxfjgkBaseByManage = anquanFxfjgkBaseService.getAnquanFxfjgkBaseByManage(item.getManageLocationid());
                item.setAnquanFxfjgkBase(anquanFxfjgkBaseByManage);
            }
		}

		return Result.OK(pageList);
	}

	 @AutoLog(value = "anquan_fxaqjc_info-分页列表查询")
	 @ApiOperation(value="anquan_fxaqjc_info-分页列表查询", notes="anquan_fxaqjc_info-分页列表查询")
	 @GetMapping(value = "/list2")
	 // @PermissionData(pageComponent ="anquan/safety/HazardCleared")
	 public Result<?> queryPageList2(AnquanFxaqjcInfo anquanFxaqjcInfo,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									String sys_depart_orgcode,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 anquanFxaqjcInfo.setSysOrgCode(sys_depart_orgcode+"*");
		 QueryWrapper<AnquanFxaqjcInfo> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxaqjcInfo, req.getParameterMap());
		 queryWrapper.ne("problem_type","⼀般问题");
		 Page<AnquanFxaqjcInfo> page = new Page<AnquanFxaqjcInfo>(pageNo, pageSize);
		 IPage<AnquanFxaqjcInfo> pageList = anquanFxaqjcInfoService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	 @AutoLog(value = "anquan_fxaqjc_info-分页列表查询")
	 @ApiOperation(value="anquan_fxaqjc_info-分页列表查询", notes="anquan_fxaqjc_info-分页列表查询")
	 @GetMapping(value = "/list3")
	//  @PermissionData(pageComponent ="anquan/safety/HazardCleared")
	 public Result<?> queryPageList3(AnquanFxaqjcInfo anquanFxaqjcInfo,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 String sys_depart_orgcode,
									 HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 anquanFxaqjcInfo.setSysOrgCode(sys_depart_orgcode+"*");
		 QueryWrapper<AnquanFxaqjcInfo> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxaqjcInfo, req.getParameterMap());
		 queryWrapper.eq("problem_type","重⼤隐患");
		 Page<AnquanFxaqjcInfo> page = new Page<AnquanFxaqjcInfo>(pageNo, pageSize);
		 IPage<AnquanFxaqjcInfo> pageList = anquanFxaqjcInfoService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param anquanFxaqjcInfo
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_info-添加")
	@ApiOperation(value="anquan_fxaqjc_info-添加", notes="anquan_fxaqjc_info-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AnquanFxaqjcInfo anquanFxaqjcInfo) throws IOException {
        System.out.println("anquanFxaqjcInfo = " + anquanFxaqjcInfo.getIsCheckImg());
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		SimpleDateFormat df=new SimpleDateFormat("yyMMdd-HHmmss");
		SimpleDateFormat df1=new SimpleDateFormat("yyyy年MM月dd日");
		anquanFxaqjcInfo.setZlsNo("WTYH-"+df.format(new Date()));
		anquanFxaqjcInfo.setCheckPeople(loginUser.getUsername());
		anquanFxaqjcInfo.setCheckTime(df1.format(new Date()));
		if (StringUtils.isNotBlank(anquanFxaqjcInfo.getIsCheckImg())){
			String fileUrl=base64ToUrl(anquanFxaqjcInfo.getIsCheckImg());
			if (StringUtils.isNotBlank(fileUrl)){
				anquanFxaqjcInfo.setCheckImg(fileUrl);
			}
		}
		anquanFxaqjcInfoService.save(anquanFxaqjcInfo);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param anquanFxaqjcInfo
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_info-编辑")
	@ApiOperation(value="anquan_fxaqjc_info-编辑", notes="anquan_fxaqjc_info-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AnquanFxaqjcInfo anquanFxaqjcInfo) throws IOException {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		SimpleDateFormat df1=new SimpleDateFormat("yyyy年MM月dd日");
		String msg = "编辑";
		if(anquanFxaqjcInfo.getHandlestatus() == 20){
           //整改
			anquanFxaqjcInfo.setProcedure2People(loginUser.getRealname());
			anquanFxaqjcInfo.setProcedure2Time(df1.format(new Date()));
			msg = "整改";
		}else if (anquanFxaqjcInfo.getHandlestatus() == 30){
			anquanFxaqjcInfo.setProcedure1People(loginUser.getRealname());
			anquanFxaqjcInfo.setProcedure1Time(df1.format(new Date()));
			msg = "督办审核";
		}else if(anquanFxaqjcInfo.getHandlestatus() == 40){
          //销号
			anquanFxaqjcInfo.setProcedure3People(loginUser.getRealname());
			anquanFxaqjcInfo.setProcedure3Time(df1.format(new Date()));
			msg = "销号";
		}
		if (StringUtils.isNotBlank(anquanFxaqjcInfo.getIsCheckImg())){
			String fileUrl=base64ToUrl(anquanFxaqjcInfo.getIsCheckImg());
			if (StringUtils.isNotBlank(fileUrl)){
				anquanFxaqjcInfo.setCheckImg(fileUrl);
			}
		}
		anquanFxaqjcInfoService.updateById(anquanFxaqjcInfo);
		return Result.OK("编辑成功!");
	}



	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_info-通过id删除")
	@ApiOperation(value="anquan_fxaqjc_info-通过id删除", notes="anquan_fxaqjc_info-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		anquanFxaqjcInfoService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_info-批量删除")
	@ApiOperation(value="anquan_fxaqjc_info-批量删除", notes="anquan_fxaqjc_info-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.anquanFxaqjcInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxaqjc_info-通过id查询")
	@ApiOperation(value="anquan_fxaqjc_info-通过id查询", notes="anquan_fxaqjc_info-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AnquanFxaqjcInfo anquanFxaqjcInfo = anquanFxaqjcInfoService.getById(id);
		if(anquanFxaqjcInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(anquanFxaqjcInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param anquanFxaqjcInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AnquanFxaqjcInfo anquanFxaqjcInfo) {
        return super.exportXls(request, anquanFxaqjcInfo, AnquanFxaqjcInfo.class, "anquan_fxaqjc_info");
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
        return super.importExcel(request, response, AnquanFxaqjcInfo.class);
    }


	 /**
	  * 导出excelApi
	  *
	  * @param request
	  * @param AnquanFxrwzkXinxi
	  */
	 @AutoLog(value = "anquan_fxaqjc_info导出Api")
	 @ApiOperation(value="anquan_fxaqjc_info导出Api", notes="anquan_fxaqjc_info导出Api")
	 @GetMapping(value = "/exportXlsApi")
	 public Result<?> exportXlsApi(HttpServletRequest request,AnquanFxaqjcInfo anquanFxaqjcInfo) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 anquanFxaqjcInfo.setSysOrgCode(loginUser.getOrgCode());
//		 LambdaQueryWrapper<AnquanFxaqjcInfo> queryWrapper =new LambdaQueryWrapper<>();
//		 queryWrapper
//				 .like(anquanFxaqjcInfo.getManageLocation()!=null,AnquanFxaqjcInfo::getManageLocation,anquanFxaqjcInfo.getManageLocation())
//				 .eq(anquanFxaqjcInfo.getProblemType()!=null,AnquanFxaqjcInfo::getProblemType,anquanFxaqjcInfo.getProblemType())
//				 .likeRight(anquanFxaqjcInfo.getSysOrgCode()!=null,AnquanFxaqjcInfo::getSysOrgCode,anquanFxaqjcInfo.getSysOrgCode());
		 QueryWrapper<AnquanFxaqjcInfo> queryWrapper=new QueryWrapper<>();
		 if (anquanFxaqjcInfo.getManageLocation()!=null){
			 queryWrapper.like("manage_location","%"+anquanFxaqjcInfo.getManageLocation());
		 }
		 if (anquanFxaqjcInfo.getSysOrgCode()!=null){
			 queryWrapper.likeRight("sys_org_code",anquanFxaqjcInfo.getSysOrgCode());
		 }
		 if (anquanFxaqjcInfo.getProblemType()!=null){
             String[] split =anquanFxaqjcInfo.getProblemType().split(",");
             List<String> list=new ArrayList<>();
             for (int i = 0; i < split.length; i++) {
                 list.add(split[i]);
             }
             queryWrapper.in("problem_type",list);
		 }

		 List<AnquanFxaqjcInfo> pageList = anquanFxaqjcInfoService.list(queryWrapper);

		 for (AnquanFxaqjcInfo item:pageList){
			 if (item.getManageLocationid()!=null){
				 AnquanFxfjgkBase anquanFxfjgkBaseByManage = anquanFxfjgkBaseService.getAnquanFxfjgkBaseByManage(item.getManageLocationid());
				 item.setAnquanFxfjgkBase(anquanFxfjgkBaseByManage);
			 }
		 }

		 return Result.OKs(pageList);
	 }


     private String base64ToUrl(String str) throws IOException {
	 	String url;
		 int i = str.lastIndexOf(",");
		 str=str.substring(i+1,str.length());
		 File file=new File("temp.png");
		 byte[] bytes=Base64.getDecoder().decode(str);
		 try (FileOutputStream fos=new FileOutputStream(file)){
			 fos.write(bytes);
			 fos.flush();
			 System.out.println("file = " + file.getAbsolutePath());
			 Map map = new HashMap();
			 map.put("file",file);
			 String post = HttpRequest.post("http://z.traiot.cn/jeecg-boot/sys/common/upload")
					 .header("Content-Type", "multipart/form-data")
					 .form(map)
					 .execute()
					 .body();
			 cn.hutool.json.JSONObject jsonObject1 = new cn.hutool.json.JSONObject(post);
			 Boolean success = (Boolean) jsonObject1.get("success");
			 if(success){
				 url= jsonObject1.get("message").toString();
			 }else {
				 throw new JeecgBootException("pdf生成失败,联系管理员");
			 }

		 }
         return url;
     }



}
