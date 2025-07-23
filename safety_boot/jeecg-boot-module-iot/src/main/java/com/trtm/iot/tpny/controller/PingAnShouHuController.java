package com.trtm.iot.tpny.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.trtm.iot.util.GPSUtil.*;

/**
 * @Description: 摊铺碾压数据监测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */

@RestController
@RequestMapping("/pingan")
@Slf4j
public class PingAnShouHuController extends JeecgController<FrontDeviceRealdata, IFrontDeviceRealdataService> {

	/**
	 * 平安守护跳转
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/find")
	public String index1() {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String phone = loginUser.getPhone() + "yf_sgms";
		String base64encodedString = Base64.getEncoder().encodeToString(phone.getBytes(StandardCharsets.UTF_8));
		String url = "https://yf.zhinengjianshe.com/sgms/thirdLogin?certificate="+base64encodedString+"&userName="+loginUser.getPhone()+"";
		return url;
	}

	@GetMapping("/findjlzf")
	public String index2() {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String phone = loginUser.getPhone() + "yf_sgms";
		String base64encodedString = Base64.getEncoder().encodeToString(phone.getBytes(StandardCharsets.UTF_8));
		String url = "https://yf.zhinengjianshe.com/sgms/thirdLogin?certificate="+base64encodedString+"&userName="+loginUser.getPhone()+"";
		return url;
	}

	/**
	 * 平安守护跳转
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/findDangAn")
	public String index3() throws UnsupportedEncodingException {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String urlTypeName = URLEncoder.encode(loginUser.getRealname(),"utf-8");
		String url = "http://101.132.158.8:61845/Home/Login?certificate=342db3816be3156fd76cf8019476dba0&userName="+urlTypeName+"";
		return url;
	}

}
