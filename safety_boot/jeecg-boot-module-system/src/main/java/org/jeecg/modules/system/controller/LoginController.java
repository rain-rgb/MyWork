package org.jeecg.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.appfunction.entity.Appfunction;
import com.trtm.iot.appfunction.service.IAppfunctionService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tokenrecode.entity.Tokenrecode;
import com.trtm.iot.tokenrecode.service.ITokenrecodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.common.util.encryption.EncryptedString;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.RandImageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "用户登录")
@Slf4j
public class LoginController {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private ISysLogService logService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private com.trtm.iot.system.service.IShebeiInfoService IShebeiInfoService;
    @Resource
    private BaseCommonService baseCommonService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private IAppfunctionService appfunctionService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private ITokenrecodeService tokenrecodeService;


    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    @ApiOperation("永久token")
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    public Result<JSONObject> getToken(@RequestBody SysLoginModel sysLoginModel) {
        Result<JSONObject> result = new Result<JSONObject>();
        Tokenrecode tokenrecode = new Tokenrecode();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }
        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
//            redisUtil.del(realKey);
            return result;
        }
        //用户登录信息
//        userInfo(sysUser, result);

        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 90);
        JSONObject obj = new JSONObject();
        obj.put("token", token);
        obj.put("key", CommonConstant.PREFIX_USER_TOKEN + token);
        result.setResult(obj);
        result.success("登录成功");
        tokenrecode.setSysOrgCode(sysUser.getOrgCode());
        tokenrecode.setUsername(username);
        tokenrecode.setRediskey(CommonConstant.PREFIX_USER_TOKEN + token);
        tokenrecode.setRedisvalue(token);
        tokenrecodeService.save(tokenrecode);
        return result;

    }


    @ApiOperation("获取短信验证码")
    @SneakyThrows
    @RequestMapping(value = "/getSmsCode", method = RequestMethod.POST)
    public Result<JSONObject> getLoginSms(@RequestBody SysLoginModel sysLoginModel){
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = sysLoginModel.getUsername();
        String smsKey = sysLoginModel.getPassword();

        //1. 校验手机号是否存在
        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getPhone, phone);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        if (sysUser == null) {
            result.error500("手机号未注册，请联系管理员注册");
            return result;
        }
        // 生成验证码
        String smsVerifyCode = getSmsVerifyCode();
        String realKey = "login:verify_code:"+sysUser.getId();
        JSONObject code = new JSONObject();
        code.put("code",smsVerifyCode);
        String existedSmsCode = (String) redisUtil.get(realKey);
        //如果验证码已经存在时
        if (StringUtils.isNotEmpty(existedSmsCode)) {
            Long expireTime = redisUtil.getExpire(realKey);
            long lastTime = 60 * 3 - expireTime;
            //三分钟内验证码有效，1分钟到3分钟之间，用户可以继续输入验证码，也可以重新获取验证码，新的验证码将覆盖旧的
            if(lastTime > 60 && expireTime >0){
                //调用第三方平台发短信，只有短信发送成功了，才能将短信验证码保存到redis
                DySmsHelper.sendSms(phone,code, DySmsEnum.SMS_KEY_CODE);
                redisUtil.set(realKey, smsVerifyCode, 60 * 3);
                System.out.println("短信验证码:" + smsVerifyCode);
            }
            //一分钟之内不得多次获取验证码
            if(lastTime < 60){
                return  result.error500("操作过于频繁，请一分钟之后再次点击发送");
            }
        }else {
            //调用notify服务发送短信,只有notify的短信发送成功了，才能将短信验证码保存到redis
            DySmsHelper.sendSms(phone,code, DySmsEnum.SMS_KEY_CODE);
            System.out.println("短信验证码:" + smsVerifyCode);
            redisUtil.set(realKey, smsVerifyCode, 60 * 3);
        }

        result.ok("发送成功，请稍后");
        return result;
    }

    /**
     * 随机获取6位短信数字验证码
     *
     * @return
     */
    public static String getSmsVerifyCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;
    }

    @ApiOperation("短信登录接口")
    @RequestMapping(value = "/loginSms", method = RequestMethod.POST)
    public Result<JSONObject> loginSms(@RequestBody SysLoginModel sysLoginModel) {
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = sysLoginModel.getUsername();
        String smsKey = sysLoginModel.getPassword();

        //1. 校验手机号是否存在
        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        SysUser sysUser = sysUserService.getUserByPhone(phone);
        if (sysUser == null) {
            result.error500("手机号未注册，请联系管理员注册");
            return result;
        }

        String captcha = sysLoginModel.getCaptcha();
        if (captcha == null) {
            result.error500("请输入短信验证码");
            return result;
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = "login:verify_code:"+sysUser.getId();
        Object checkCode = redisUtil.get(realKey);
        //当进入登录页时，有一定几率出现验证码错误 #1714
        //update-end-author:taoyan date:20190828 for:校验验证码
        if (checkCode == null || !checkCode.toString().equals(lowerCaseCaptcha)) {
            result.error500("验证码错误或已过期，请重新获取");
            redisUtil.del(realKey);
            return result;
        }
        //update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }
        //2. 校验用户名或密码是否正确
//        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
//        String syspassword = sysUser.getPassword();
//        if (!syspassword.equals(userpassword)) {
//            result.error500("用户名或密码错误");
//            redisUtil.del(realKey);
//            return result;
//        }

        //用户登录信息
        userInfo(sysUser, result);
        //update-begin--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        baseCommonService.addLog("用户名: " + sysUser.getUsername() + ",登录成功！", CommonConstant.LOG_TYPE_1, null, loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        redisUtil.del(realKey);
        return result;
    }


	@AutoLog(value = "登录接口")
	@ApiOperation(value="登录接口", notes="登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
        //前端密码加密，后端进行密码解密
        //password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

        //update-begin-author:taoyan date:20190828 for:校验验证码
        String captcha = sysLoginModel.getCaptcha();
        if (captcha == null) {
            result.error500("验证码无效");
            return result;
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCaptcha + sysLoginModel.getCheckKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        //当进入登录页时，有一定几率出现验证码错误 #1714
        if (checkCode == null || !checkCode.toString().equals(lowerCaseCaptcha)) {
            result.error500("验证码错误,点击验证码刷新");
            redisUtil.del(realKey);
            return result;
        }
        //update-end-author:taoyan date:20190828 for:校验验证码

        //1. 校验用户是否有效
        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        //update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            redisUtil.del(realKey);
            return result;
        }

        //用户登录信息
        userInfo(sysUser, result);
        //update-begin--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null, loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        redisUtil.del(realKey);
        return result;
    }

    @ApiOperation("登录接口目前演示用不需要传密码")
    @RequestMapping(value = "/loginsso", method = RequestMethod.POST)
    public Result<JSONObject> login1(@RequestBody SysLoginModel sysLoginModel) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        //String password = sysLoginModel.getPassword();
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
        //前端密码加密，后端进行密码解密
        //password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

        //update-begin-author:taoyan date:20190828 for:校验验证码
//		String captcha = sysLoginModel.getCaptcha();
//		if(captcha==null){
//			result.error500("验证码无效");
//			return result;
//		}
//		String lowerCaseCaptcha = captcha.toLowerCase();
//		String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+sysLoginModel.getCheckKey(), "utf-8");
//		Object checkCode = redisUtil.get(realKey);
//		//当进入登录页时，有一定几率出现验证码错误 #1714
//		if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
//			result.error500("验证码错误");
//			return result;
//		}
        //update-end-author:taoyan date:20190828 for:校验验证码

        //1. 校验用户是否有效
        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        //update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
//		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
//		String syspassword = sysUser.getPassword();
//		if (!syspassword.equals(userpassword)) {
//			result.error500("用户名或密码错误");
//			return result;
//		}
        //判断用户是否存在
        SysUser queryone = sysUserService.queryone(username);
        if (queryone == null) {
            result.error500("该用户不存在,或已删除");
            return result;
        }
        if (queryone.getOrgCode() != null) {
            List<ShebeiInfo> shebeiInfos = IShebeiInfoService.arrayOneshebei(queryone.getOrgCode());
            String shebei = "无";
            if (shebeiInfos.size() > 0) {
                shebei = "";
                for (com.trtm.iot.system.entity.ShebeiInfo shebeiInfo : shebeiInfos) {
                    if (shebei.equals("")) {
                        shebei = "" + shebeiInfo.getSbjno() + "";
                    } else {
                        shebei = shebei + "," + shebeiInfo.getSbjno() + "";
                    }
                }
            }
            redisUtil.set(queryone.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        } else {
            String shebei = "无";
            redisUtil.set(queryone.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        }
        //用户登录信息
        userInfo(sysUser, result);
        //update-begin--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null, loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        return result;
    }



    @ApiOperation("集团标准版用户对接")
    @RequestMapping(value = "/loginsso13", method = RequestMethod.POST)
    public Result<JSONObject> login13( HttpServletRequest request,HttpServletResponse response) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = request.getHeader("Authorization");
        //String password = sysLoginModel.getPassword();
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
        //前端密码加密，后端进行密码解密
        //password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

        //update-begin-author:taoyan date:20190828 for:校验验证码
//		String captcha = sysLoginModel.getCaptcha();
//		if(captcha==null){
//			result.error500("验证码无效");
//			return result;
//		}
//		String lowerCaseCaptcha = captcha.toLowerCase();
//		String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+sysLoginModel.getCheckKey(), "utf-8");
//		Object checkCode = redisUtil.get(realKey);
//		//当进入登录页时，有一定几率出现验证码错误 #1714
//		if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
//			result.error500("验证码错误");
//			return result;
//		}
        //update-end-author:taoyan date:20190828 for:校验验证码

        //1. 校验用户是否有效
        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false

        if (request.getParameterMap().containsKey("token") ) {
            response.addHeader("Content-Security-Policy", "frame-ancestors{{https://yggc.cncico.com:1080}}");
        }

//        String username1 = getAuthenticationToken(request.getParameter("token"));
        if(username.split(" ").length>1){
            String username1 = getAuthenticationToken(username.split(" ")[1]);
            if(StringUtils.isNotBlank(username1)){
                username = username1;
            }else{
                username="集团用户";
            }
        }else{
            username="集团用户";
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        //update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
//		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
//		String syspassword = sysUser.getPassword();
//		if (!syspassword.equals(userpassword)) {
//			result.error500("用户名或密码错误");
//			return result;
//		}
        //判断用户是否存在
        SysUser queryone = sysUserService.queryone(username);
        if (queryone == null) {
//            result.error500("该用户不存在,或已删除");
//            return result;
            queryone = sysUserService.queryone("集团用户");
        }
        if (queryone.getOrgCode() != null) {
            List<ShebeiInfo> shebeiInfos = IShebeiInfoService.arrayOneshebei(queryone.getOrgCode());
            String shebei = "无";
            if (shebeiInfos.size() > 0) {
                shebei = "";
                for (com.trtm.iot.system.entity.ShebeiInfo shebeiInfo : shebeiInfos) {
                    if (shebei.equals("")) {
                        shebei = "" + shebeiInfo.getSbjno() + "";
                    } else {
                        shebei = shebei + "," + shebeiInfo.getSbjno() + "";
                    }
                }
            }
            redisUtil.set(queryone.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        } else {
            String shebei = "无";
            redisUtil.set(queryone.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        }
        //用户登录信息
        userInfo(sysUser, result);
        //update-begin--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null, loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        return result;
    }

    @ApiOperation("集团标准版用户对接")
    @RequestMapping(value = "/loginsso12", method = RequestMethod.POST)
    public Result<JSONObject> login2(@RequestBody SysLoginModel sysLoginModel, HttpServletRequest request,HttpServletResponse response) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        //String password = sysLoginModel.getPassword();
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
        //前端密码加密，后端进行密码解密
        //password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

        //update-begin-author:taoyan date:20190828 for:校验验证码
//		String captcha = sysLoginModel.getCaptcha();
//		if(captcha==null){
//			result.error500("验证码无效");
//			return result;
//		}
//		String lowerCaseCaptcha = captcha.toLowerCase();
//		String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+sysLoginModel.getCheckKey(), "utf-8");
//		Object checkCode = redisUtil.get(realKey);
//		//当进入登录页时，有一定几率出现验证码错误 #1714
//		if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
//			result.error500("验证码错误");
//			return result;
//		}
        //update-end-author:taoyan date:20190828 for:校验验证码

        //1. 校验用户是否有效
        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false

        if (request.getParameterMap().containsKey("token") ) {
            response.addHeader("Content-Security-Policy", "frame-ancestors{{https://yggc.cncico.com:1080}}");
        }

//        String username1 = getAuthenticationToken(request.getParameter("token"));
        if(username.split(" ").length>1){
            String username1 = getAuthenticationToken(username.split(" ")[1]);
            if(StringUtils.isNotBlank(username1)){
                username = username1;
            }else{
                username="集团用户";
            }
        }else{
            username="集团用户";
        }

            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        //update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
//		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
//		String syspassword = sysUser.getPassword();
//		if (!syspassword.equals(userpassword)) {
//			result.error500("用户名或密码错误");
//			return result;
//		}
        //判断用户是否存在
        SysUser queryone = sysUserService.queryone(username);
        if (queryone == null) {
            result.error500("该用户不存在,或已删除");
            return result;
        }
        if (queryone.getOrgCode() != null) {
            List<ShebeiInfo> shebeiInfos = IShebeiInfoService.arrayOneshebei(queryone.getOrgCode());
            String shebei = "无";
            if (shebeiInfos.size() > 0) {
                shebei = "";
                for (com.trtm.iot.system.entity.ShebeiInfo shebeiInfo : shebeiInfos) {
                    if (shebei.equals("")) {
                        shebei = "" + shebeiInfo.getSbjno() + "";
                    } else {
                        shebei = shebei + "," + shebeiInfo.getSbjno() + "";
                    }
                }
            }
            redisUtil.set(queryone.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        } else {
            String shebei = "无";
            redisUtil.set(queryone.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        }
        //用户登录信息
        userInfo(sysUser, result);
        //update-begin--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null, loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        return result;
    }


    @RequestMapping(value = "/jtbzbtz")
    public void jtbzbtz(HttpServletRequest request, HttpServletResponse response, String token) throws IOException {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = "";
        if (request.getParameterMap().containsKey("token") ) {
            response.addHeader("Content-Security-Policy", "frame-ancestors{{https://yggc.cncico.com:1080}}");
        }
        if(token.split(" ").length>1){
            username = getAuthenticationToken(token.split(" ")[1]);
            if(StringUtils.isEmpty(username)){
                username="集团用户";
            }
        }else{
            username="集团用户";
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        //update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return ;
        }

        //判断用户是否存在
        SysUser queryone = sysUserService.queryone(username);
        if (queryone == null) {
            result.error500("该用户不存在,或已删除");
            return ;
        }
        if (queryone.getOrgCode() != null) {
            List<ShebeiInfo> shebeiInfos = IShebeiInfoService.arrayOneshebei(queryone.getOrgCode());
            String shebei = "无";
            if (shebeiInfos.size() > 0) {
                shebei = "";
                for (com.trtm.iot.system.entity.ShebeiInfo shebeiInfo : shebeiInfos) {
                    if (shebei.equals("")) {
                        shebei = "" + shebeiInfo.getSbjno() + "";
                    } else {
                        shebei = shebei + "," + shebeiInfo.getSbjno() + "";
                    }
                }
            }
            redisUtil.set(queryone.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        } else {
            String shebei = "无";
            redisUtil.set(queryone.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        }
        //用户登录信息
        Result<JSONObject> jsonObjectResult = userInfo(sysUser, result);


        response.sendRedirect("http://47.96.161.157/dashboard/analysis?token="+jsonObjectResult.getResult().get("token"));
    }

    public String getAuthenticationToken(String token) {
        if (StringUtils.isNotBlank(token)) {
            HttpHeaders header = new HttpHeaders();
            header.set("Authorization", String.format("%s %s", "Bearer", token));
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, header);
            Object tokenEntity = restTemplate.exchange("https://yggc.cncico.com:1080/api/auth/oauth/check_token?token=" + token, HttpMethod.GET, httpEntity, Object.class);
            if (tokenEntity instanceof ResponseEntity  && ((ResponseEntity) tokenEntity).getStatusCode() == HttpStatus.OK) {
                Map resultMap = (Map) ((ResponseEntity) tokenEntity).getBody();
                if (!resultMap.containsKey("active") || (Boolean) resultMap.get("active") != true) {
                    return null;
                }
            }

            Object userEntity = restTemplate.exchange("https://yggc.cncico.com:1080/api/auth/users/action/current",HttpMethod.GET, httpEntity, Object.class);
            if (userEntity instanceof ResponseEntity && ((ResponseEntity) userEntity).getStatusCode() == HttpStatus.OK) {
                // 验证返回数据第一层封装是否符合要求
                Map userRequestMap = (Map)  ((ResponseEntity) userEntity).getBody();
                if (userRequestMap.containsKey("code") && userRequestMap.get("code").equals("00000")) {
                    Map userMap = (Map) userRequestMap.get("model");
                    // 得到手机号
                    String phone = (String) userMap.get("loginName");
                    String userName = (String) userMap.get("userName");
                    if(phone != null){
                        return phone;
                    }else{
                        return userName;
                    }

                }
            }
        }
        return  null;
    }



    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout")
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        //用户退出逻辑
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if (oConvertUtils.isEmpty(token)) {
            return Result.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        LoginUser sysUser = sysBaseAPI.getUserByName(username);
        if (sysUser != null) {
            //update-begin--Author:wangshuai  Date:20200714  for：登出日志没有记录人员
            baseCommonService.addLog("用户名: " + sysUser.getRealname() + ",退出成功！", CommonConstant.LOG_TYPE_1, null, sysUser);
            //update-end--Author:wangshuai  Date:20200714  for：登出日志没有记录人员
            log.info(" 用户名:  " + sysUser.getRealname() + ",退出成功！ ");
            //清空用户登录Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户登录Shiro权限缓存
            redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
            //调用shiro的logout
            SecurityUtils.getSubject().logout();
            return Result.ok("退出登录成功！");
        } else {
            return Result.error("Token无效!");
        }
    }

    /**
     * 获取访问量
     *
     * @return
     */
    @GetMapping("loginfo")
    public Result<JSONObject> loginfo() {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        //update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
        // 获取一天的开始和结束时间
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
        // 获取系统访问记录
        Long totalVisitCount = logService.findTotalVisitCount();
        obj.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = logService.findTodayVisitCount(dayStart, dayEnd);
        obj.put("todayVisitCount", todayVisitCount);
        Long todayIp = logService.findTodayIp(dayStart, dayEnd);
        //update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
        obj.put("todayIp", todayIp);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 获取访问量
     *
     * @return
     */
    @GetMapping("visitInfo")
    public Result<List<Map<String, Object>>> visitInfo() {
        Result<List<Map<String, Object>>> result = new Result<List<Map<String, Object>>>();
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        List<Map<String, Object>> list = logService.findVisitCount(dayStart, dayEnd);
        result.setResult(oConvertUtils.toLowerCasePageList(list));
        return result;
    }


    /**
     * 登陆成功选择用户当前部门
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
    public Result<JSONObject> selectDepart(@RequestBody SysUser user) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = user.getUsername();
        if (oConvertUtils.isEmpty(username)) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            username = sysUser.getUsername();
        }
        String orgCode = user.getOrgCode();
        this.sysUserService.updateUserDepart(username, orgCode);
        SysUser sysUser = sysUserService.getUserByName(username);
        JSONObject obj = new JSONObject();
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        return result;
    }

    /**
     * 登陆成功选择用户当前部门
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/selectDeparts", method = RequestMethod.PUT)
    public Result<JSONObject> selectDeparts(@RequestBody SysUser user) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = user.getUsername();
        if (oConvertUtils.isEmpty(username)) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            username = sysUser.getUsername();
        }
        String orgCode = user.getOrgCode();
        String departName = null;
        if (!StrUtil.isBlank(orgCode)) {
            QueryWrapper<SysDepart> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("org_code", orgCode);
            SysDepart sysDepart = sysDepartService.getOne(queryWrapper3);
            departName = sysDepart.getDepartName();
        }
        this.sysUserService.updateUserDepart(username, orgCode);
        SysUser sysUser = sysUserService.getUserByName(username);
        sysUser.setTelephone(departName);
        JSONObject obj = new JSONObject();
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        return result;
    }

    /**
     * 短信登录接口
     *
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/sms")
    public Result<String> sms(@RequestBody JSONObject jsonObject) {
        Result<String> result = new Result<String>();
        String mobile = jsonObject.get("mobile").toString();
        //手机号模式 登录模式: "2"  注册模式: "1"
        String smsmode = jsonObject.get("smsmode").toString();
        log.info(mobile);
        if (oConvertUtils.isEmpty(mobile)) {
            result.setMessage("手机号不允许为空！");
            result.setSuccess(false);
            return result;
        }
        Object object = redisUtil.get(mobile);
        if (object != null) {
            result.setMessage("验证码10分钟内，仍然有效！");
            result.setSuccess(false);
            return result;
        }

        //随机数
        String captcha = RandomUtil.randomNumbers(6);
        JSONObject obj = new JSONObject();
        obj.put("code", captcha);
        try {
            boolean b = false;
            //注册模板
            if (CommonConstant.SMS_TPL_TYPE_1.equals(smsmode)) {
                SysUser sysUser = sysUserService.getUserByPhone(mobile);
                if (sysUser != null) {
                    result.error500(" 手机号已经注册，请直接登录！");
                    baseCommonService.addLog("手机号已经注册，请直接登录！", CommonConstant.LOG_TYPE_1, null);
                    return result;
                }
                b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.REGISTER_TEMPLATE_CODE);
            } else {
                //登录模式，校验用户有效性
                SysUser sysUser = sysUserService.getUserByPhone(mobile);
                result = sysUserService.checkUserIsEffective(sysUser);
                if (!result.isSuccess()) {
                    String message = result.getMessage();
                    if ("该用户不存在，请注册".equals(message)) {
                        result.error500("该用户不存在或未绑定手机号");
                    }
                    return result;
                }

                /**
                 * smsmode 短信模板方式  0 .登录模板、1.注册模板、2.忘记密码模板
                 */
                if (CommonConstant.SMS_TPL_TYPE_0.equals(smsmode)) {
                    //登录模板
                    b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.LOGIN_TEMPLATE_CODE);
                } else if (CommonConstant.SMS_TPL_TYPE_2.equals(smsmode)) {
                    //忘记密码模板
                    b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
                }
            }

            if (b == false) {
                result.setMessage("短信验证码发送失败,请稍后重试");
                result.setSuccess(false);
                return result;
            }
            //验证码10分钟内有效
            redisUtil.set(mobile, captcha, 600);
            //update-begin--Author:scott  Date:20190812 for：issues#391
            //result.setResult(captcha);
            //update-end--Author:scott  Date:20190812 for：issues#391
            result.setSuccess(true);

        } catch (ClientException e) {
            e.printStackTrace();
            result.error500(" 短信接口未配置，请联系管理员！");
            return result;
        }
        return result;
    }


    /**
     * 手机号登录接口
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation("手机号登录接口")
    @PostMapping("/phoneLogin")
    public Result<JSONObject> phoneLogin(@RequestBody JSONObject jsonObject) {
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = jsonObject.getString("mobile");

        //校验用户有效性
        SysUser sysUser = sysUserService.getUserByPhone(phone);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        String smscode = jsonObject.getString("captcha");
        Object code = redisUtil.get(phone);
        if (!smscode.equals(code)) {
            result.setMessage("手机验证码错误");
            return result;
        }
        //用户信息
        userInfo(sysUser, result);
        //添加日志
        baseCommonService.addLog("用户名: " + sysUser.getUsername() + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

        return result;
    }


    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);

        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
        obj.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            obj.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            obj.put("multi_depart", 1);
        } else {
            //查询当前是否有登录部门
            // update-begin--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            SysUser sysUserById = sysUserService.getById(sysUser.getId());
            if (oConvertUtils.isEmpty(sysUserById.getOrgCode())) {
                sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            }
            // update-end--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            obj.put("multi_depart", 2);
        }
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 获取加密字符串
     *
     * @return
     */
    @GetMapping(value = "/getEncryptedString")
    public Result<Map<String, String>> getEncryptedString() {
        Result<Map<String, String>> result = new Result<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", EncryptedString.key);
        map.put("iv", EncryptedString.iv);
        result.setResult(map);
        return result;
    }


    /**
     * 后台生成图形验证码 ：有效
     *
     * @param response
     * @param key
     */
    @ApiOperation("获取验证码")
    @GetMapping(value = "/randomImage/{key}")
    public Result<String> randomImage(HttpServletResponse response, @PathVariable String key) {
        Result<String> res = new Result<String>();
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCode + key, "utf-8");
            redisUtil.set(realKey, lowerCaseCode, 60);
            String base64 = RandImageUtil.generate(code);
            res.setSuccess(true);
            res.setResult(base64);
        } catch (Exception e) {
            res.error500("获取验证码出错" + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    /**
     * app登录
     *
     * @param sysLoginModel
     * @return
     * @throws Exception
     */
    @ApiOperation("app登录")
    @RequestMapping(value = "/mLogin", method = RequestMethod.POST)
    public Result<JSONObject> mLogin(@RequestBody SysLoginModel sysLoginModel) throws Exception {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();

        //1. 校验用户是否有效
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);

        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

        String orgCode = sysUser.getOrgCode();
        if (oConvertUtils.isEmpty(orgCode)) {
            //如果当前用户无选择部门 查看部门关联信息
            List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
            if (departs == null || departs.size() == 0) {
                result.error500("用户暂未归属部门,不可登录!");
                return result;
            }
            orgCode = departs.get(0).getOrgCode();
            sysUser.setOrgCode(orgCode);
            this.sysUserService.updateUserDepart(username, orgCode);
        }
        JSONObject obj = new JSONObject();
        JSONObject obj1 = new JSONObject();
        //用户登录信息
        obj.put("userInfo", sysUser);

        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置超时时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        String id = sysUser.getId();
        LambdaQueryWrapper<SysUserRole> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(SysUserRole::getUserId, id);
        SysUserRole one = sysUserRoleService.getOne(queryWrapper1);

        LambdaQueryWrapper<Appfunction> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Appfunction::getRoleids, one.getRoleId());
        List<Appfunction> list = appfunctionService.list(queryWrapper2);
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        List list4 = new ArrayList();
        for (Appfunction appfunction : list) {
            String appfunction1 = appfunction.getAppfunction();
            if (appfunction1.startsWith("0")) {
                list1.add(appfunction1);
            } else if (appfunction1.startsWith("1")) {
                list2.add(appfunction1);
            } else if (appfunction1.startsWith("2")) {
                list3.add(appfunction1);
            } else if (appfunction1.startsWith("3")) {
                list4.add(appfunction1);
            }
        }


        obj1.put("us", list1);
        obj1.put("os", list2);
        obj1.put("sp", list3);
        obj1.put("fq", list4);
        //token 信息
        obj.put("token", token);
        obj.put("appfunction", obj1);
        result.setResult(obj);
        result.setSuccess(true);
        result.setCode(200);
        baseCommonService.addLog("用户名: " + username + ",登录成功[移动端]！", CommonConstant.LOG_TYPE_1, null);
        return result;
    }

    /**
     * app登录
     *
     * @param sysLoginModel
     * @return
     * @throws Exception
     */
    @ApiOperation("app登录")
    @RequestMapping(value = "/mLogins", method = RequestMethod.POST)
    public Result<JSONObject> mLogins(@RequestBody SysLoginModel sysLoginModel) throws Exception {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();

        //1. 校验用户是否有效
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);

        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }
        String departName = null;
        String orgCode = sysUser.getOrgCode();
        if (!StrUtil.isBlank(orgCode)) {
            QueryWrapper<SysDepart> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("org_code", orgCode);
            SysDepart sysDepart = sysDepartService.getOne(queryWrapper3);
            departName = sysDepart.getDepartName();
        }
        if (oConvertUtils.isEmpty(orgCode)) {
            //如果当前用户无选择部门 查看部门关联信息
            List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
            if (departs == null || departs.size() == 0) {
                result.error500("用户暂未归属部门,不可登录!");
                return result;
            }
            orgCode = departs.get(0).getOrgCode();
            departName = departs.get(0).getDepartName();
            sysUser.setOrgCode(orgCode);
            this.sysUserService.updateUserDepart(username, orgCode);
        }
        JSONObject obj = new JSONObject();
        JSONObject obj1 = new JSONObject();
        //用户登录信息
        sysUser.setTelephone(departName);
        obj.put("userInfo", sysUser);
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置超时时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        String id = sysUser.getId();
        LambdaQueryWrapper<SysUserRole> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(SysUserRole::getUserId, id);
        SysUserRole one = sysUserRoleService.getOne(queryWrapper1);

        LambdaQueryWrapper<Appfunction> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Appfunction::getRoleids, one.getRoleId());
        List<Appfunction> list = appfunctionService.list(queryWrapper2);
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        List list4 = new ArrayList();
        for (Appfunction appfunction : list) {
            String appfunction1 = appfunction.getAppfunction();
            if (appfunction1.startsWith("0")) {
                list1.add(appfunction1);
            } else if (appfunction1.startsWith("1")) {
                list2.add(appfunction1);
            } else if (appfunction1.startsWith("2")) {
                list3.add(appfunction1);
            } else if (appfunction1.startsWith("3")) {
                list4.add(appfunction1);
            }
        }


        obj1.put("us", list1);
        obj1.put("os", list2);
        obj1.put("sp", list3);
        obj1.put("fq", list4);
        //token 信息
        obj.put("token", token);
        obj.put("appfunction", obj1);
        result.setResult(obj);
        result.setSuccess(true);
        result.setCode(200);
        //用户登录信息
        userInfo(sysUser, result);
        //update-begin--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        baseCommonService.addLog("用户名: " + username + ",登录成功[移动端]！", CommonConstant.LOG_TYPE_1, null);
        return result;
    }



    @ApiOperation("app登录--集团token对接")
    @RequestMapping(value = "/mLoginss", method = RequestMethod.POST)
    public Result<JSONObject> mLoginss(@RequestBody SysLoginModel sysLoginModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        if (request.getParameterMap().containsKey("token") ) {
            response.addHeader("Content-Security-Policy", "frame-ancestors{{https://yggc.cncico.com:1080}}");
        }

        if(username.split(" ").length>1){
            String username1 = getAuthenticationToken(username.split(" ")[1]);
            if(StringUtils.isNotBlank(username1)){
                username = username1;
            }else{
                username="集团用户";
            }
        }else{
            username="集团用户";
        }

//        String password = sysLoginModel.getPassword();

        //1. 校验用户是否有效
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);

        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        String syspassword = sysUser.getPassword();

//        //2. 校验用户名或密码是否正确
//        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
//        String syspassword = sysUser.getPassword();
//        if (!syspassword.equals(userpassword)) {
//            result.error500("用户名或密码错误");
//            return result;
//        }

        //判断用户是否存在
        SysUser queryone = sysUserService.queryone(username);
        if (queryone == null) {
            result.error500("该用户不存在,或已删除");
            return result;
        }

        String departName = null;
        String orgCode = sysUser.getOrgCode();
        if (!StrUtil.isBlank(orgCode)) {
            QueryWrapper<SysDepart> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("org_code", orgCode);
            SysDepart sysDepart = sysDepartService.getOne(queryWrapper3);
            departName = sysDepart.getDepartName();
        }
        if (oConvertUtils.isEmpty(orgCode)) {
            //如果当前用户无选择部门 查看部门关联信息
            List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
            if (departs == null || departs.size() == 0) {
                result.error500("用户暂未归属部门,不可登录!");
                return result;
            }
            orgCode = departs.get(0).getOrgCode();
            departName = departs.get(0).getDepartName();
            sysUser.setOrgCode(orgCode);
            this.sysUserService.updateUserDepart(username, orgCode);
        }
        JSONObject obj = new JSONObject();
        JSONObject obj1 = new JSONObject();
        //用户登录信息
        sysUser.setTelephone(departName);
        obj.put("userInfo", sysUser);
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置超时时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        String id = sysUser.getId();
        LambdaQueryWrapper<SysUserRole> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(SysUserRole::getUserId, id);
        SysUserRole one = sysUserRoleService.getOne(queryWrapper1);

        LambdaQueryWrapper<Appfunction> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Appfunction::getRoleids, one.getRoleId());
        List<Appfunction> list = appfunctionService.list(queryWrapper2);
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        List list4 = new ArrayList();
        for (Appfunction appfunction : list) {
            String appfunction1 = appfunction.getAppfunction();
            if (appfunction1.startsWith("0")) {
                list1.add(appfunction1);
            } else if (appfunction1.startsWith("1")) {
                list2.add(appfunction1);
            } else if (appfunction1.startsWith("2")) {
                list3.add(appfunction1);
            } else if (appfunction1.startsWith("3")) {
                list4.add(appfunction1);
            }
        }


        obj1.put("us", list1);
        obj1.put("os", list2);
        obj1.put("sp", list3);
        obj1.put("fq", list4);
        //token 信息
        obj.put("token", token);
        obj.put("appfunction", obj1);
        result.setResult(obj);
        result.setSuccess(true);
        result.setCode(200);
        //用户登录信息
        userInfo(sysUser, result);
        //update-begin--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        baseCommonService.addLog("用户名: " + username + ",登录成功[移动端]！", CommonConstant.LOG_TYPE_1, null);
        return result;
    }


    /**
     * 图形验证码
     *
     * @param sysLoginModel
     * @return
     */
    @RequestMapping(value = "/checkCaptcha", method = RequestMethod.POST)
    public Result<?> checkCaptcha(@RequestBody SysLoginModel sysLoginModel) {
        String captcha = sysLoginModel.getCaptcha();
        String checkKey = sysLoginModel.getCheckKey();
        if (captcha == null) {
            return Result.error("验证码无效");
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCaptcha + checkKey, "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.equals(lowerCaseCaptcha)) {
            return Result.error("验证码错误");
        }
        return Result.ok();
    }

}
