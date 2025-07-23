package com.trtm.iot.anquanfxgk.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.api.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkXuncha;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkXunchaService;

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
 * @Description: anquan_fxfjgk_xuncha
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
@Api(tags="anquan_fxfjgk_xuncha")
@RestController
@RequestMapping("/anquanfxgk/anquanFxfjgkXuncha")
@Slf4j
public class AnquanFxfjgkXunchaController extends JeecgController<AnquanFxfjgkXuncha, IAnquanFxfjgkXunchaService> {
	@Autowired
	private IAnquanFxfjgkXunchaService anquanFxfjgkXunchaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param anquanFxfjgkXuncha
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_xuncha-分页列表查询")
	@ApiOperation(value="anquan_fxfjgk_xuncha-分页列表查询", notes="anquan_fxfjgk_xuncha-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AnquanFxfjgkXuncha anquanFxfjgkXuncha,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AnquanFxfjgkXuncha> queryWrapper = QueryGenerator.initQueryWrapper(anquanFxfjgkXuncha, req.getParameterMap());
		Page<AnquanFxfjgkXuncha> page = new Page<AnquanFxfjgkXuncha>(pageNo, pageSize);
		IPage<AnquanFxfjgkXuncha> pageList = anquanFxfjgkXunchaService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param anquanFxfjgkXuncha
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_xuncha-添加")
	@ApiOperation(value="anquan_fxfjgk_xuncha-添加", notes="anquan_fxfjgk_xuncha-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AnquanFxfjgkXuncha anquanFxfjgkXuncha) throws IOException {
		if (StringUtils.isNotBlank(anquanFxfjgkXuncha.getIsRummagerSign())){
			String fileUrl=base64ToUrl(anquanFxfjgkXuncha.getIsRummagerSign());
			if (StringUtils.isNotBlank(fileUrl)){
				anquanFxfjgkXuncha.setRummagerSign(fileUrl);
			}
		}
		anquanFxfjgkXunchaService.save(anquanFxfjgkXuncha);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param anquanFxfjgkXuncha
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_xuncha-编辑")
	@ApiOperation(value="anquan_fxfjgk_xuncha-编辑", notes="anquan_fxfjgk_xuncha-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AnquanFxfjgkXuncha anquanFxfjgkXuncha) throws IOException {
		if (StringUtils.isNotBlank(anquanFxfjgkXuncha.getIsRummagerSign())){
			String fileUrl=base64ToUrl(anquanFxfjgkXuncha.getIsRummagerSign());
			if (StringUtils.isNotBlank(fileUrl)){
				anquanFxfjgkXuncha.setRummagerSign(fileUrl);
			}
		}
		anquanFxfjgkXunchaService.updateById(anquanFxfjgkXuncha);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_xuncha-通过id删除")
	@ApiOperation(value="anquan_fxfjgk_xuncha-通过id删除", notes="anquan_fxfjgk_xuncha-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		anquanFxfjgkXunchaService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_xuncha-批量删除")
	@ApiOperation(value="anquan_fxfjgk_xuncha-批量删除", notes="anquan_fxfjgk_xuncha-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.anquanFxfjgkXunchaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "anquan_fxfjgk_xuncha-通过id查询")
	@ApiOperation(value="anquan_fxfjgk_xuncha-通过id查询", notes="anquan_fxfjgk_xuncha-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AnquanFxfjgkXuncha anquanFxfjgkXuncha = anquanFxfjgkXunchaService.getById(id);
		if(anquanFxfjgkXuncha==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(anquanFxfjgkXuncha);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param anquanFxfjgkXuncha
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AnquanFxfjgkXuncha anquanFxfjgkXuncha) {
        return super.exportXls(request, anquanFxfjgkXuncha, AnquanFxfjgkXuncha.class, "anquan_fxfjgk_xuncha");
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
        return super.importExcel(request, response, AnquanFxfjgkXuncha.class);
    }


	 /**
	  * 导出excelApi
	  *
	  * @param request
	  * @param anquanFxfjgkBase
	  */
	 @AutoLog(value = "anquan_fxfjgk_xuncha导出Api")
	 @ApiOperation(value="anquan_fxfjgk_xuncha导出Api", notes="anquan_fxfjgk_xuncha导出Api")
	 @GetMapping(value = "/exportXlsApi")
	 public Result<?> exportXlsApi(HttpServletRequest request,AnquanFxfjgkXuncha anquanFxfjgkXuncha) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 anquanFxfjgkXuncha.setSysOrgCode(loginUser.getOrgCode());
		 LambdaQueryWrapper<AnquanFxfjgkXuncha> queryWrapper =new LambdaQueryWrapper<>();
		 		queryWrapper.likeRight(anquanFxfjgkXuncha.getSysOrgCode()!=null,AnquanFxfjgkXuncha::getSysOrgCode,anquanFxfjgkXuncha.getSysOrgCode());
		 List<AnquanFxfjgkXuncha> pageList = anquanFxfjgkXunchaService.list(queryWrapper);

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
