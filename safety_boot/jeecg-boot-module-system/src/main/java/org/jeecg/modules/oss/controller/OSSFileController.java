package org.jeecg.modules.oss.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.api.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.oss.entity.OSSFile;
import org.jeecg.modules.oss.service.IOSSFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/sys/oss/file")
public class OSSFileController {

	@Autowired
	private IOSSFileService ossFileService;

	@ResponseBody
	@GetMapping("/list")
	public Result<IPage<OSSFile>> queryPageList(OSSFile file,String sys_depart_orgcode,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<OSSFile>> result = new Result<>();
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信
		if(StringUtils.isNotBlank(sys_depart_orgcode)){
			file.setSysOrgCode(sys_depart_orgcode+'*');
		}else{
			file.setSysOrgCode(loginUser.getOrgCode());
		}
		QueryWrapper<OSSFile> queryWrapper = QueryGenerator.initQueryWrapper(file, req.getParameterMap());
		Page<OSSFile> page = new Page<>(pageNo, pageSize);
		IPage<OSSFile> pageList = ossFileService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	@ResponseBody
	@PostMapping("/upload")
	//@RequiresRoles("admin")
	public Result upload(@RequestParam("file") MultipartFile multipartFile) {
		Result result = new Result();
		try {
			ossFileService.upload(multipartFile);
			result.success("上传成功！");
		}
		catch (Exception ex) {
			log.info(ex.getMessage(), ex);
			result.error500("上传失败");
		}
		return result;
	}

	@ResponseBody
	@GetMapping(value = "/add")
	public Result<?> add(OSSFile file , HttpServletRequest request, HttpServletResponse response){
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		boolean save = ossFileService.savees(file);
		return Result.OK(save);
	}

	@ResponseBody
	@DeleteMapping("/delete")
	public Result delete(@RequestParam(name = "id") String id) {
		Result result = new Result();
		OSSFile file = ossFileService.getById(id);
		if (file == null) {
			result.error500("未找到对应实体");
		}
		else {
			boolean ok = ossFileService.delete(file);
			if (ok) {
				result.success("删除成功!");
			}
		}
		return result;
	}

	/**
	 * 通过id查询.
	 */
	@ResponseBody
	@GetMapping("/queryById")
	public Result<OSSFile> queryById(@RequestParam(name = "id") String id) {
		Result<OSSFile> result = new Result<>();
		OSSFile file = ossFileService.getById(id);
		if (file == null) {
			result.error500("未找到对应实体");
		}
		else {
			result.setResult(file);
			result.setSuccess(true);
		}
		return result;
	}

}
