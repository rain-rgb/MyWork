package org.jeecg.modules.system.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.gqpx_employee_training.entity.GqpxEmployeeTraining;
import com.trtm.iot.gqpx_employee_training.service.IGqpxEmployeeTrainingService;
import com.trtm.iot.gqpx_group.entity.GqpxGroup;
import com.trtm.iot.gqpx_group.service.IGqpxGroupService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.util.IdentityCardUtil;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.aspect.annotation.RateLimited;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.RateLimitExceededException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.vo.SysDepartUsersVO;
import org.jeecg.modules.system.vo.SysUserRoleVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.exceptions.JedisException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Api(tags = "用户表")
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysUserDepartService sysUserDepartService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private ISysDepartRoleUserService departRoleUserService;

    @Autowired
    private ISysDepartRoleService departRoleService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISysUserCarService sysUserCarService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private IGqpxGroupService gqpxGroupService;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    @Resource
    private BaseCommonService baseCommonService;

    @Autowired
    private com.trtm.iot.system.service.IShebeiInfoService IShebeiInfoService;
    @Autowired
    private com.trtm.iot.system.service.IShebeiInfoService shebeiInfoService;
    @Autowired
    private IGqpxEmployeeTrainingService gqpxEmployeeTrainingService;

    /**
     * 获取用户列表数据
     *
     * @param user
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @PermissionData(pageComponent = "system/UserList")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysUser>> queryPageList(SysUser user, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req, String sys_depart_orgcode) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            user.setOrgCode(sys_depart_orgcode + "*");
        }
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
        //TODO 外部模拟登陆临时账号，列表不显示
        queryWrapper.ne("username", "_reserve_user_external");
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);

        //批量查询用户的所属部门
        //step.1 先拿到全部的 useids
        //step.2 通过 useids，一次性查询用户的所属部门名字
        List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        if (userIds != null && userIds.size() > 0) {
            Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            pageList.getRecords().forEach(item -> {
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        return result;
    }

    /**
     * 登录时初始缓存当前用户下的拥有权限的所有设备
     *
     * @param ShebeiInfo
     * @param req
     * @return
     */
    @RequestMapping(value = "/list1", method = RequestMethod.POST)
    public Result<List<ShebeiInfo>> queryPageList1(ShebeiInfo ShebeiInfo, HttpServletRequest req) {
        Result<List<ShebeiInfo>> result = new Result<List<ShebeiInfo>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<SysDepart> selectqueryorgcode = sysDepartService.selectqueryorgcode(loginUser.getDepartIds());
        String join = null;
        if (selectqueryorgcode.size() > 0) {
            List<String> idList = selectqueryorgcode.stream().map(SysDepart::getOrgCode).collect(Collectors.toList());
            join = StringUtils.join(idList.toArray(), ",");
        }
        List<ShebeiInfo> pageList = IShebeiInfoService.arrayOneshebei(join);
        String shebei = null;
        List<String> idList = null;
        if (pageList.size() > 0) {
            idList = pageList.stream().map(com.trtm.iot.system.entity.ShebeiInfo::getSbjno).collect(Collectors.toList());
            shebei = StringUtils.join(idList.toArray(), ",");
        }
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        HashMap<Object, Object> objectObjectHashMap1 = new HashMap<>();
        objectObjectHashMap1.put("shebei", pageList);
        redisUtil.lSet(loginUser.getId() + "initial", objectObjectHashMap1);//存储的当前用户下的所拥有的设备信息
        redisUtil.set(loginUser.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备

        return result;
    }


    /**
     * 当前用户选择组织机构改变缓存下的拥有权限的所有设备
     *
     * @param
     * @param req
     * @return
     */
    @RequestMapping(value = "/getShebiCache", method = RequestMethod.GET)
    public Result<List<ShebeiInfo>> getShebiCache(String projectid, ShebeiInfo shebeiInfo, HttpServletRequest req) {
        Result<List<ShebeiInfo>> result = new Result<List<ShebeiInfo>>();
        LambdaQueryWrapper<ShebeiInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShebeiInfo::getShebeiStatus, 1).eq(ShebeiInfo::getProjectid, projectid);
        List<ShebeiInfo> pageList = IShebeiInfoService.list(lambdaQueryWrapper);
        String shebei = null;
        List<String> idList = null;
        if (pageList.size() > 0) {
            idList = pageList.stream().map(com.trtm.iot.system.entity.ShebeiInfo::getSbjno).collect(Collectors.toList());
            shebei = StringUtils.join(idList.toArray(), ",");
        }
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        redisUtil.set(projectid + "shebeilist", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        return result;
    }


    @RequestMapping(value = "/list2Jt", method = RequestMethod.GET)
    public Result<List<ShebeiInfo>> queryPageList2Jt(String projectid, String sectionid, ShebeiInfo ShebeiInfo, HttpServletRequest req) {

        String sys_depart_orgcode = "A09";
        SysDepart depart = sysDepartService.queryoneByproject(projectid, sectionid);
        if (ObjectUtil.isNotNull(depart)) {
            sys_depart_orgcode = depart.getOrgCode();
        }

        Result<List<ShebeiInfo>> result = new Result<List<ShebeiInfo>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<ShebeiInfo> pageList = null;
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            pageList = IShebeiInfoService.arrayOneshebei(sys_depart_orgcode);
        } else {
            pageList = IShebeiInfoService.arrayOneshebei(loginUser.getOrgCode());
        }
        String shebei = null;
        List<String> idList = null;
        if (pageList.size() > 0) {
            idList = pageList.stream().map(com.trtm.iot.system.entity.ShebeiInfo::getSbjno).collect(Collectors.toList());
            shebei = StringUtils.join(idList.toArray(), ",");
        }
        result.setSuccess(true);
        //  result.setResult(pageList);
        result.setMessage(sys_depart_orgcode);
        log.info(pageList.toString());
        redisUtil.set(loginUser.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        return result;
    }


    /**
     * 当前用户选择组织机构改变缓存下的拥有权限的所有设备
     *
     * @param ShebeiInfo
     * @param req
     * @return
     */
    @RequestMapping(value = "/list2", method = RequestMethod.GET)
    public Result<List<ShebeiInfo>> queryPageList2(String sys_depart_orgcode, ShebeiInfo ShebeiInfo, HttpServletRequest req) {
        Result<List<ShebeiInfo>> result = new Result<List<ShebeiInfo>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<ShebeiInfo> pageList = null;
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            pageList = IShebeiInfoService.arrayOneshebei(sys_depart_orgcode);
        } else {
            pageList = IShebeiInfoService.arrayOneshebei(loginUser.getOrgCode());
        }
        String shebei = null;
        List<String> idList = null;
        if (pageList.size() > 0) {
            idList = pageList.stream().map(com.trtm.iot.system.entity.ShebeiInfo::getSbjno).collect(Collectors.toList());
            shebei = StringUtils.join(idList.toArray(), ",");
        }
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        redisUtil.set(loginUser.getId() + "change", shebei);//根据当前用户选择组织机构随时去改变的用户权限下的设备
        return result;
    }

    /**
     * 当前用户获取当前设备列表
     *
     * @param ShebeiInfo
     * @param req
     * @return
     */
    @RequestMapping(value = "/list3", method = RequestMethod.GET)
    public Result<List<ShebeiInfo>> queryPageList3(String sys_depart_orgcode, ShebeiInfo ShebeiInfo, HttpServletRequest req, String sbtypes) {
        Result<List<ShebeiInfo>> result = new Result<List<ShebeiInfo>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<ShebeiInfo> pageList = null;
        String sysorgcode = null;
        if (!org.springframework.util.StringUtils.isEmpty(sys_depart_orgcode)) {
            sysorgcode = sys_depart_orgcode;
        } else {
            sysorgcode = loginUser.getOrgCode();
        }
        if (StringUtil.isNotEmpty(sbtypes)) {
            String[] split = sbtypes.split(",");
            List list = new ArrayList();
            for (int i = 0; i < split.length; i++) {
                list.add(split[i]);
            }
            pageList = IShebeiInfoService.usershebeiList(sysorgcode, list);
        } else {
            pageList = IShebeiInfoService.usershebeiLists(sysorgcode);
        }
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        return result;
    }


    @RequestMapping(value = "/list3sname", method = RequestMethod.GET)
    public Result<List<ShebeiInfo>> queryPageList3sname(String sys_depart_orgcode, ShebeiInfo ShebeiInfo, HttpServletRequest req, String sbtypes) {
        Result<List<ShebeiInfo>> result = new Result<List<ShebeiInfo>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<ShebeiInfo> pageList = null;
        String sysorgcode = null;
        if (!org.springframework.util.StringUtils.isEmpty(sys_depart_orgcode)) {
            sysorgcode = sys_depart_orgcode;
        } else {
            sysorgcode = loginUser.getOrgCode();
        }
        if (StringUtil.isNotEmpty(sbtypes)) {
            String[] split = sbtypes.split(",");
            List list = new ArrayList();
            for (int i = 0; i < split.length; i++) {
                list.add(split[i]);
            }
            pageList = IShebeiInfoService.usershebeiListByname(sysorgcode, list, ShebeiInfo.getSbname());
        } else {
            pageList = IShebeiInfoService.usershebeiLists(sysorgcode, ShebeiInfo.getSbname());
        }
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        return result;
    }


    @RequestMapping(value = "/list3s", method = RequestMethod.GET)
    public Result<List<ShebeiInfo>> queryPageList3s(String sys_depart_orgcode, ShebeiInfo ShebeiInfo, HttpServletRequest req, String sbtypes) {
        Result<List<ShebeiInfo>> result = new Result<List<ShebeiInfo>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<ShebeiInfo> pageList = null;
        String sysorgcode = null;
        if (!org.springframework.util.StringUtils.isEmpty(sys_depart_orgcode)) {
            sysorgcode = sys_depart_orgcode;
        } else {
            sysorgcode = loginUser.getOrgCode();
        }
        if (StringUtil.isNotEmpty(sbtypes)) {
            String[] split = sbtypes.split(",");
            List list = new ArrayList();
            for (int i = 0; i < split.length; i++) {
                list.add(split[i]);
            }
            pageList = IShebeiInfoService.usershebeiListByname(sysorgcode, list);
        } else {
            pageList = IShebeiInfoService.usershebeiLists(sysorgcode);
        }
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        return result;
    }

    @RequestMapping(value = "/list3stj", method = RequestMethod.GET)
    public Result<?> queryPageList3stj(String sys_depart_orgcode, ShebeiInfo ShebeiInfo, HttpServletRequest req, String sbtypes) {

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        ShebeiInfo.setSbjno(shebei);
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(ShebeiInfo, req.getParameterMap());
        queryWrapper.select(" count(1) sbzs,  " +
                " sum( CASE WHEN `status` = 1 THEN 1 else 0 END)  sblxs, " +
                " sum( CASE WHEN `status` = 3 THEN 1 else 0 END)  sbzxs ");
        Map<String, Object> map = IShebeiInfoService.getMap(queryWrapper);

        return Result.OK(map);
    }


    /**
     * 当前用户获取当前设备列表
     *
     * @param shebeiInfo
     * @param req
     * @return
     */
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(ShebeiInfo shebeiInfo, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (StringUtil.isEmpty(shebeiInfo.getSysOrgCode())) {
            shebeiInfo.setSysOrgCode(loginUser.getOrgCode());
        }
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        if (shebeiInfo.getSysOrgCode().contains("A05A01A03A01A01A0")) {
            if ("A05A01A03A01A01A07A02".equals(shebeiInfo.getSysOrgCode())) {
                queryWrapper.likeRight("sbjno", "ydgsnhbh03");
            } else {
                queryWrapper.likeRight("sbjno", "A05A01A03A01A01A0");
            }
        }
        List<ShebeiInfo> pageList = IShebeiInfoService.list(queryWrapper);
        return Result.OK(pageList);
    }

    @GetMapping(value = "/QueryAppUsers")
    public Result<?> QueryAppUsers(SysUser user, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        user.setRytype(1);
        user.setOrgCode(loginUser.getOrgCode() + "*");
        String worktype = user.getWorktype();
        if (StringUtil.isNotEmpty(worktype)) {
            user.setWorktype("*" + worktype + "*");
        }
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
        IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);
        pageList.getRecords().forEach(item -> {
            String id = item.getId();
            int statuscolor = 0;
            List<GqpxEmployeeTraining> gqpxList = gqpxEmployeeTrainingService.lambdaQuery()
                    .eq(GqpxEmployeeTraining::getUserId, id)
                    .eq(GqpxEmployeeTraining::getTraningType, 1)
                    .eq(GqpxEmployeeTraining::getStatus, 1)
                    .list();
            if (gqpxList.size() > 0) {
                statuscolor = 2;
            }

            List<GqpxEmployeeTraining> zxpxList = gqpxEmployeeTrainingService.lambdaQuery()
                    .eq(GqpxEmployeeTraining::getUserId, id)
                    .eq(GqpxEmployeeTraining::getTraningType, 2)
                    .eq(GqpxEmployeeTraining::getStatus, 1)
                    .list();
            for (GqpxEmployeeTraining gqpxEmployeeTraining : zxpxList) {
                String expirationTime = gqpxEmployeeTraining.getExpirationTime();
                if (expirationTime != null) {
                    String[] split = expirationTime.split("-->");
                    LocalDate currentDate = LocalDate.now();

                    if (split.length > 0) {
                        String startTimeStr = split[0].trim().substring(0, 10);
                        LocalDate startTime = LocalDate.parse(startTimeStr); // 需要处理可能的异常，比如格式错误

                        LocalDate startMinusTwo = startTime.minusDays(2);
                        if (split.length > 1) {
                            String endTimeStr = split[1].trim().substring(0, 10);
                            LocalDate endTime = LocalDate.parse(endTimeStr);
                            LocalDate endPlusTwo = endTime.plusDays(2);
                            if (currentDate.isAfter(startMinusTwo) && currentDate.isBefore(endPlusTwo)) {
                                statuscolor = 1;
                            }
                        }
                    }
                }
            }
            item.setStatuscolor(statuscolor);
        });
        return Result.OK(pageList);
    }

    @GetMapping(value = "/QueryUserTree")
    public Result<?> QueryAppUsers(SysUser user) {
        String orgCode = user.getOrgCode();
        if (StringUtil.isEmpty(orgCode)) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            orgCode = loginUser.getOrgCode();
        }
        LambdaQueryWrapper<GqpxGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(GqpxGroup::getOrgcode, orgCode).orderByAsc(GqpxGroup::getName);
        List<GqpxGroup> list = gqpxGroupService.list(queryWrapper);
        List mapList = new ArrayList();

        for (GqpxGroup gqpxGroup : list) {
            Map map = new HashMap();
            map.put("name", gqpxGroup.getName());
            map.put("id", UUID.randomUUID());

            LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysUserLambdaQueryWrapper.likeRight(SysUser::getOrgCode, orgCode)
                    .eq(SysUser::getBanzu, gqpxGroup.getName())
                    .groupBy(SysUser::getWorktype);
            List<SysUser> sysUsers = sysUserService.list(sysUserLambdaQueryWrapper);

            List<Map<String, String>> worktypeList = new ArrayList<>();
            Set<String> worktypeSet = new HashSet<>();

            for (SysUser sysUser : sysUsers) {
                String worktype = sysUser.getWorktype();
                if (worktype != null && !worktype.isEmpty()) {
                    String[] split = worktype.split(",");
                    for (String s : split) {
                        String trimmedWorktype = s.trim();
                        if (worktypeSet.add(trimmedWorktype)) { // 如果添加成功，说明是唯一的
                            Map worktypeMap = new HashMap<>();
                            worktypeMap.put("name", trimmedWorktype);
                            worktypeMap.put("id", UUID.randomUUID());
                            LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>();
                            userQueryWrapper.likeRight(SysUser::getOrgCode, orgCode)
                                    .eq(SysUser::getBanzu, gqpxGroup.getName())
                                    .like(SysUser::getWorktype, trimmedWorktype);
                            List<SysUser> userList = sysUserService.list(userQueryWrapper);

                            List userMapList = new ArrayList<>();
                            for (SysUser user1 : userList) {
                                Map userMap = new HashMap<>();
                                userMap.put("name", user1.getRealname());
                                userMap.put("userid", user1.getId());
                                userMap.put("id", UUID.randomUUID());
                                userMapList.add(userMap);
                            }
                            worktypeMap.put("children", userMapList);

                            worktypeList.add(worktypeMap);
                        }
                    }
                }
            }
            map.put("children", worktypeList);
            mapList.add(map);
        }
        return Result.OK(mapList);
    }

    //@RequiresRoles({"admin"})
    //@RequiresPermissions("user:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysUser> add(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        String selectedRoles = jsonObject.getString("selectedroles");
        String selectedDeparts = jsonObject.getString("selecteddeparts");
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            String phone = user.getPhone();
            //判断手机号是否重复
            if (sysUserService.getUserByPhone(phone) != null) {
                result.setMessage("手机号已存在");
                result.setSuccess(false);
                return result;
            }
            user.setCreateTime(new Date());//设置创建时间
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(1);
            user.setDelFlag(CommonConstant.DEL_FLAG_0);
            // 保存用户走一个service 保证事务
            sysUserService.saveUser(user, selectedRoles, selectedDeparts);
            if (user.getHealthcertificate() != null && user.getInsurancecertificate() != null) {
                user.setStatus(1);
            }
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    //此接口提供其他项目传输用户用    //暂时已经废除   使用下面的方法  addUserDocking
    @PostMapping(value = "/addUser")
    public Result<?> addUser(@RequestBody SysUser sysUser) {
        if (sysUser.getUsername() != null && sysUser.getUsername().length() != 0) {
            SysUser queryone = sysUserService.queryone(sysUser.getUsername());
            if (queryone == null) {
                if (sysUser.getCreateBy().equals("瑞苍项目")) {  //瑞仓用户的权限判断
                    String post = sysUser.getPost();
                    SysDepart list = sysDepartService.querynameabbrList(post, "瑞苍");
                    if (list == null) {
                        sysUser.setDepartIds("a3d7d7dc6a51418a86d0fbf2cc22bcdb");//当前用户查不到所属组织机构时给他默认 瑞仓项目的组织机构
                        sysUser.setOrgCode("A05A01A05A01");//当前用户查不到所属组织机构时给他默认 瑞仓项目的权限
                    } else {
                        sysUser.setDepartIds(list.getId());
                        sysUser.setOrgCode(list.getOrgCode());
                    }
                }
                if (sysUser.getCreateBy().equals("柯桥项目")) {  //瑞仓用户的权限判断
                    String post = sysUser.getPost();
                    SysDepart list = sysDepartService.querynameabbrList(post, "柯桥");
                    if (list == null) {
                        sysUser.setDepartIds("53d95000e73040b28ef757a4d6b2fd47");//当前用户查不到所属组织机构时给他默认 柯桥项目的组织机构
                        sysUser.setOrgCode("A05A01A04A01");//当前用户查不到所属组织机构时给他默认 瑞仓项目的权限
                    } else {
                        sysUser.setDepartIds(list.getId());
                        sysUser.setOrgCode(list.getOrgCode());
                    }
                }
                String salt = oConvertUtils.randomGen(8);
                sysUser.setPassword("1234562021");//给到默认密码  其他项目通过单点登录接口去请求 获取token 用不到密码
                sysUser.setSalt(salt);
                sysUser.setCreateTime(new Date());//设置创建时间
                String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), sysUser.getPassword(), salt);
                sysUser.setPassword(passwordEncode);
                sysUser.setStatus(1);
                sysUser.setUserIdentity(2);
                sysUser.setDelFlag(CommonConstant.DEL_FLAG_0);
                boolean save = sysUserService.save(sysUser);
                if (save) {
                    SysUser userByName = sysUserService.getUserByName(sysUser.getUsername());
                    //给当前用户添加组织机构权限  用户id 关联 组织机构id
                    SysUserDepart sysUserDepart = new SysUserDepart(userByName.getId(), sysUser.getDepartIds());
                    sysUserDepartService.save(sysUserDepart);

                    //给当前用户添加 角色和 用户的关联
                    if (sysUser.getCreateBy().equals("瑞苍项目")) {
                        SysRole description = sysRoleService.getDescription(sysUser.getPost());
                        SysUserRole sysUserRole = new SysUserRole();
                        if (description != null) {
                            sysUserRole.setRoleId(description.getId());
                            sysUserRole.setUserId(userByName.getId());
                        } else {
                            sysUserRole.setRoleId("1419920598853976065");
                            sysUserRole.setUserId(userByName.getId());
                        }
                        sysUserRoleService.save(sysUserRole);
                    }
                    if (sysUser.getCreateBy().equals("柯桥项目")) {
                        SysRole description = sysRoleService.getDescription(sysUser.getPost());
                        SysUserRole sysUserRole = new SysUserRole();
                        if (description != null) {
                            sysUserRole.setRoleId(description.getId());
                            sysUserRole.setUserId(userByName.getId());
                        } else {
                            sysUserRole.setRoleId("1419930367866142721");
                            sysUserRole.setUserId(userByName.getId());
                        }
                        sysUserRoleService.save(sysUserRole);
                    }
                    return Result.OK("添加成功！");

                } else {
                    return Result.error("操作失败！");
                }
            } else {
                return Result.error("用户名已经存在");
            }

        } else {
            return Result.error("数据不完整");
        }
//        return result;
    }


    @PostMapping(value = "/addUserDocking")
    public Result<?> addUserDocking(@RequestBody SysUser sysUser) {
        if (sysUser.getUsername() != null && sysUser.getUsername().length() != 0) {
            SysUser queryone = sysUserService.queryone(sysUser.getUsername());
            if (queryone == null) {
                String post1 = sysUser.getPost();//角色名称描述
                String createBy = sysUser.getCreateBy();//这个是项目的全称/所属分部
                String[] split = createBy.split("/");
                String post = split[1];
                List<SysDepart> sysDeparts = sysDepartService.querydepart_name_abbrList(split[0]);
                String OrgCode = null;
                String Id = null;

                for (SysDepart sysDepart : sysDeparts) {
                    if (post.contains(sysDepart.getDepartName()) || post.equals(sysDepart.getDepartName())) {
                        OrgCode = sysDepart.getOrgCode();
                        Id = sysDepart.getId();
                    }
                }
                sysUser.setDepartIds(Id);
                sysUser.setOrgCode(OrgCode);
                String salt = oConvertUtils.randomGen(8);
                sysUser.setPassword("Cico@123");//给到默认密码  其他项目通过单点登录接口去请求 获取token 用不到密码
                sysUser.setSalt(salt);
                sysUser.setCreateTime(new Date());//设置创建时间
                String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), sysUser.getPassword(), salt);
                sysUser.setPassword(passwordEncode);
                sysUser.setStatus(1);
                sysUser.setUserIdentity(2);
                sysUser.setDelFlag(CommonConstant.DEL_FLAG_0);
                boolean save = sysUserService.save(sysUser);
                if (save) {
                    SysUser userByName = sysUserService.getUserByName(sysUser.getUsername());
                    //给当前用户添加组织机构权限  用户id 关联 组织机构id
                    SysUserDepart sysUserDepart = new SysUserDepart(userByName.getId(), sysUser.getDepartIds());
                    sysUserDepartService.save(sysUserDepart);

                    //给当前用户关联角色的权限  sys_role_permission  此表
                    SysRole description = sysRoleService.getDescription(sysUser.getPost());
                    SysUserRole sysUserRole = new SysUserRole();
                    if (description != null) {
                        sysUserRole.setRoleId(description.getId());
                        sysUserRole.setUserId(userByName.getId());
                    } else {
                        return Result.error("当前用户却角色不存在!");
                    }
                    sysUserRoleService.save(sysUserRole);
                }

                return Result.OK("添加成功!");
            } else {
                return Result.error("用户名已经存在!");
            }
        } else {
            return Result.error("用户信息不完整!");
        }
    }

    //@RequiresRoles({"admin"})
    //@RequiresPermissions("user:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Result<SysUser> edit(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        try {
            SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
            baseCommonService.addLog("编辑用户，id： " + jsonObject.getString("id"), CommonConstant.LOG_TYPE_2, 2);
            if (sysUser == null) {
                result.error500("未找到对应实体");
            } else {
                SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setUpdateTime(new Date());
                //String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
                user.setPassword(sysUser.getPassword());
                String roles = jsonObject.getString("selectedroles");
                String departs = jsonObject.getString("selecteddeparts");
                if (user.getHealthcertificate() != null && user.getInsurancecertificate() != null) {
                    user.setStatus(1);
                }
                // 修改用户走一个service 保证事务
                sysUserService.editUser(user, roles, departs);
                result.success("修改成功!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 删除用户
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        baseCommonService.addLog("删除用户，id： " + id, CommonConstant.LOG_TYPE_2, 3);
        this.sysUserService.deleteUser(id);
        return Result.ok("删除用户成功");
    }

    /**
     * 批量删除用户
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        baseCommonService.addLog("批量删除用户， ids： " + ids, CommonConstant.LOG_TYPE_2, 3);
        this.sysUserService.deleteBatchUsers(ids);
        return Result.ok("批量删除用户成功");
    }

    /**
     * 冻结&解冻用户
     *
     * @param jsonObject
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
    public Result<SysUser> frozenBatch(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (oConvertUtils.isNotEmpty(id)) {
                    this.sysUserService.update(new SysUser().setStatus(Integer.parseInt(status)),
                            new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId, id));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败" + e.getMessage());
        }
        result.success("操作成功!");
        return result;

    }

    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<SysUser> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysUser> result = new Result<SysUser>();
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysUser);
            result.setSuccess(true);
        }
        return result;
    }

    @RequestMapping(value = "/queryUserRole", method = RequestMethod.GET)
    public Result<List<String>> queryUserRole(@RequestParam(name = "userid", required = true) String userid) {
        Result<List<String>> result = new Result<>();
        List<String> list = new ArrayList<String>();
        List<SysUserRole> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userid));
        if (userRole == null || userRole.size() <= 0) {
            result.error500("未找到用户相关角色信息");
        } else {
            for (SysUserRole sysUserRole : userRole) {
                list.add(sysUserRole.getRoleId());
            }
            result.setSuccess(true);
            result.setResult(list);
        }
        return result;
    }


    /**
     * 校验用户账号是否唯一<br>
     * 可以校验其他 需要检验什么就传什么。。。
     *
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/checkOnlyUser", method = RequestMethod.GET)
    @RateLimited
    public Result<Boolean> checkOnlyUser(SysUser sysUser) {
        Result<Boolean> result = new Result<>();
        //如果此参数为false则程序发生异常
        result.setResult(true);
        try {
            //通过传入信息查询新的用户信息
            sysUser.setPassword(null);
            SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>(sysUser));
            if (user != null) {
                result.setSuccess(false);
                result.setMessage("用户账号已存在");
                return result;
            }

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改密码
     */
    //@RequiresRoles({"admin"})
    @ApiOperation(value = "管理员修改密码", notes = "作者：zhangqi")
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public Result<?> changePassword(@RequestBody SysUser sysUser) {
        SysUser u = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername()));
        if (u == null) {
            return Result.error("用户不存在！");
        }
        sysUser.setId(u.getId());
        return sysUserService.changePassword(sysUser);
    }

    /**
     * 查询指定用户和部门关联的数据
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userDepartList", method = RequestMethod.GET)
    public Result<List<DepartIdModel>> getUserDepartsList(@RequestParam(name = "userId", required = true) String userId) {
        Result<List<DepartIdModel>> result = new Result<>();
        try {
            List<DepartIdModel> depIdModelList = this.sysUserDepartService.queryDepartIdsOfUser(userId);
            if (depIdModelList != null && depIdModelList.size() > 0) {
                result.setSuccess(true);
                result.setMessage("查找成功");
                result.setResult(depIdModelList);
            } else {
                result.setSuccess(false);
                result.setMessage("查找失败");
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("查找过程中出现了异常: " + e.getMessage());
            return result;
        }

    }

    /**
     * 生成在添加用户情况下没有主键的问题,返回给前端,根据该id绑定部门数据
     *
     * @return
     */
    @RequestMapping(value = "/generateUserId", method = RequestMethod.GET)
    public Result<String> generateUserId() {
        Result<String> result = new Result<>();
        System.out.println("我执行了,生成用户ID==============================");
        String userId = UUID.randomUUID().toString().replace("-", "");
        result.setSuccess(true);
        result.setResult(userId);
        return result;
    }

    /**
     * 根据部门id查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryUserByDepId", method = RequestMethod.GET)
    public Result<List<SysUser>> queryUserByDepId(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "realname", required = false) String realname) {
        Result<List<SysUser>> result = new Result<>();
        //List<SysUser> userList = sysUserDepartService.queryUserByDepId(id);
        SysDepart sysDepart = sysDepartService.getById(id);
        List<SysUser> userList = sysUserDepartService.queryUserByDepCode(sysDepart.getOrgCode(), realname);

        //批量查询用户的所属部门
        //step.1 先拿到全部的 useids
        //step.2 通过 useids，一次性查询用户的所属部门名字
        List<String> userIds = userList.stream().map(SysUser::getId).collect(Collectors.toList());
        if (userIds != null && userIds.size() > 0) {
            Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            userList.forEach(item -> {
                //TODO 临时借用这个字段用于页面展示
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }

        try {
            result.setSuccess(true);
            result.setResult(userList);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            return result;
        }
    }

    /**
     * 用户选择组件 专用  根据用户账号或部门分页查询
     *
     * @param departId
     * @param username
     * @return
     */
    @RequestMapping(value = "/queryUserComponentData", method = RequestMethod.GET)
    public Result<IPage<SysUser>> queryUserComponentData(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "departId", required = false) String departId,
            @RequestParam(name = "realname", required = false) String realname,
            @RequestParam(name = "username", required = false) String username) {
        IPage<SysUser> pageList = sysUserDepartService.queryDepartUserPageList(departId, username, realname, pageSize, pageNo);
        return Result.OK(pageList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysUser
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysUser sysUser, HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //update-begin--Author:kangxiaolin  Date:20180825 for：[03]用户导出，如果选择数据则只导出相关数据--------------------
        String selections = request.getParameter("selections");
        if (!oConvertUtils.isEmpty(selections)) {
            queryWrapper.in("id", selections.split(","));
        }
        //update-end--Author:kangxiaolin  Date:20180825 for：[03]用户导出，如果选择数据则只导出相关数据----------------------
        List<SysUser> pageList = sysUserService.list(queryWrapper);

        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "用户列表");
        mv.addObject(NormalExcelConstants.CLASS, SysUser.class);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ExportParams exportParams = new ExportParams("用户列表数据", "导出人:" + user.getRealname(), "导出信息");
        exportParams.setImageBasePath(upLoadPath);
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
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
    //@RequiresRoles({"admin"})
    //@RequiresPermissions("user:import")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // 错误信息
        List<String> errorMessage = new ArrayList<>();
        int successLines = 0, errorLines = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SysUser> listSysUsers = ExcelImportUtil.importExcel(file.getInputStream(), SysUser.class, params);
                for (int i = 0; i < listSysUsers.size(); i++) {
                    SysUser sysUserExcel = listSysUsers.get(i);
                    if (StringUtils.isBlank(sysUserExcel.getPassword())) {
                        // 密码默认为 “123456”
                        sysUserExcel.setPassword("123456");
                    }
                    // 密码加密加盐
                    String salt = oConvertUtils.randomGen(8);
                    sysUserExcel.setSalt(salt);
                    String passwordEncode = PasswordUtil.encrypt(sysUserExcel.getUsername(), sysUserExcel.getPassword(), salt);
                    sysUserExcel.setPassword(passwordEncode);
                    try {
                        sysUserService.save(sysUserExcel);
                        successLines++;
                    } catch (Exception e) {
                        errorLines++;
                        String message = e.getMessage().toLowerCase();
                        int lineNumber = i + 1;
                        // 通过索引名判断出错信息
                        if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_USERNAME)) {
                            errorMessage.add("第 " + lineNumber + " 行：用户名已经存在，忽略导入。");
                        } else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_WORK_NO)) {
                            errorMessage.add("第 " + lineNumber + " 行：工号已经存在，忽略导入。");
                        } else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_PHONE)) {
                            errorMessage.add("第 " + lineNumber + " 行：手机号已经存在，忽略导入。");
                        } else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_EMAIL)) {
                            errorMessage.add("第 " + lineNumber + " 行：电子邮件已经存在，忽略导入。");
                        } else {
                            errorMessage.add("第 " + lineNumber + " 行：未知错误，忽略导入");
                            log.error(e.getMessage(), e);
                        }
                    }
                    // 批量将部门和用户信息建立关联关系
                    String departIds = sysUserExcel.getDepartIds();
                    if (StringUtils.isNotBlank(departIds)) {
                        String userId = sysUserExcel.getId();
                        String[] departIdArray = departIds.split(",");
                        List<SysUserDepart> userDepartList = new ArrayList<>(departIdArray.length);
                        for (String departId : departIdArray) {
                            userDepartList.add(new SysUserDepart(userId, departId));
                        }
                        sysUserDepartService.saveBatch(userDepartList);
                    }

                }
            } catch (Exception e) {
                errorMessage.add("发生异常：" + e.getMessage());
                log.error(e.getMessage(), e);
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return ImportExcelUtil.imporReturnRes(errorLines, successLines, errorMessage);
    }

    /**
     * @param userIds
     * @return
     * @功能：根据id 批量查询
     */
    @RequestMapping(value = "/queryByIds", method = RequestMethod.GET)
    public Result<Collection<SysUser>> queryByIds(@RequestParam String userIds) {
        Result<Collection<SysUser>> result = new Result<>();
        String[] userId = userIds.split(",");
        Collection<String> idList = Arrays.asList(userId);
        Collection<SysUser> userRole = sysUserService.listByIds(idList);
        result.setSuccess(true);
        result.setResult(userRole);
        return result;
    }

    /**
     * 首页用户重置密码
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public Result<?> updatePassword(@RequestBody JSONObject json) {
        String username = json.getString("username");
        String oldpassword = json.getString("oldpassword");
        String password = json.getString("password");
        String confirmpassword = json.getString("confirmpassword");
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (!sysUser.getUsername().equals(username)) {
            return Result.error("只允许修改自己的密码！");
        }
        SysUser user = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (user == null) {
            return Result.error("用户不存在！");
        }
        return sysUserService.resetPassword(username, oldpassword, password, confirmpassword);
    }

    @RequestMapping(value = "/userRoleList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> userRoleList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        String roleId = req.getParameter("roleId");
        String username = req.getParameter("username");
        IPage<SysUser> pageList = sysUserService.getUserByRoleId(page, roleId, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 给指定角色添加用户
     *
     * @param
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/addSysUserRole", method = RequestMethod.POST)
    public Result<String> addSysUserRole(@RequestBody SysUserRoleVO sysUserRoleVO) {
        Result<String> result = new Result<String>();
        try {
            String sysRoleId = sysUserRoleVO.getRoleId();
            for (String sysUserId : sysUserRoleVO.getUserIdList()) {
                SysUserRole sysUserRole = new SysUserRole(sysUserId, sysRoleId);
                QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
                queryWrapper.eq("role_id", sysRoleId).eq("user_id", sysUserId);
                SysUserRole one = sysUserRoleService.getOne(queryWrapper);
                if (one == null) {
                    sysUserRoleService.save(sysUserRole);
                }

            }
            result.setMessage("添加成功!");
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("出错了: " + e.getMessage());
            return result;
        }
    }

    /**
     * 删除指定角色的用户关系
     *
     * @param
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteUserRole", method = RequestMethod.DELETE)
    public Result<SysUserRole> deleteUserRole(@RequestParam(name = "roleId") String roleId,
                                              @RequestParam(name = "userId", required = true) String userId
    ) {
        Result<SysUserRole> result = new Result<SysUserRole>();
        try {
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
            queryWrapper.eq("role_id", roleId).eq("user_id", userId);
            sysUserRoleService.remove(queryWrapper);
            result.success("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 批量删除指定角色的用户关系
     *
     * @param
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteUserRoleBatch", method = RequestMethod.DELETE)
    public Result<SysUserRole> deleteUserRoleBatch(
            @RequestParam(name = "roleId") String roleId,
            @RequestParam(name = "userIds", required = true) String userIds) {
        Result<SysUserRole> result = new Result<SysUserRole>();
        try {
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
            queryWrapper.eq("role_id", roleId).in("user_id", Arrays.asList(userIds.split(",")));
            sysUserRoleService.remove(queryWrapper);
            result.success("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 部门用户列表
     */
    @RequestMapping(value = "/departUserList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> departUserList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        String depId = req.getParameter("depId");
        String username = req.getParameter("username");
        //根据部门ID查询,当前和下级所有的部门IDS
        List<String> subDepids = new ArrayList<>();
        //部门id为空时，查询我的部门下所有用户
        if (oConvertUtils.isEmpty(depId)) {
            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            int userIdentity = user.getUserIdentity() != null ? user.getUserIdentity() : CommonConstant.USER_IDENTITY_1;
            if (oConvertUtils.isNotEmpty(userIdentity) && userIdentity == CommonConstant.USER_IDENTITY_2) {
                subDepids = sysDepartService.getMySubDepIdsByDepId(user.getDepartIds());
            }
        } else {
            subDepids = sysDepartService.getSubDepIdsByDepId(depId);
        }
        if (subDepids != null && subDepids.size() > 0) {
            IPage<SysUser> pageList = sysUserService.getUserByDepIds(page, subDepids, username);
            //批量查询用户的所属部门
            //step.1 先拿到全部的 useids
            //step.2 通过 useids，一次性查询用户的所属部门名字
            List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
            if (userIds != null && userIds.size() > 0) {
                Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
                pageList.getRecords().forEach(item -> {
                    //批量查询用户的所属部门
                    item.setOrgCode(useDepNames.get(item.getId()));
                });
            }
            result.setSuccess(true);
            result.setResult(pageList);
        } else {
            result.setSuccess(true);
            result.setResult(null);
        }
        return result;
    }


    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     * 若某个用户包含多个部门，则会显示多条记录，可自行处理成单条记录
     */
    @GetMapping("/queryByOrgCode")
    public Result<?> queryByDepartId(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "orgCode") String orgCode,
            SysUser userParams
    ) {
        IPage<SysUserSysDepartModel> pageList = sysUserService.queryUserByOrgCode(orgCode, userParams, new Page(pageNo, pageSize));
        return Result.ok(pageList);
    }

    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     * 针对通讯录模块做的接口，将多个部门的用户合并成一条记录，并转成对前端友好的格式
     */
    @GetMapping("/queryByOrgCodeForAddressList")
    public Result<?> queryByOrgCodeForAddressList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "orgCode", required = false) String orgCode,
            SysUser userParams
    ) {
        IPage page = new Page(pageNo, pageSize);
        IPage<SysUserSysDepartModel> pageList = sysUserService.queryUserByOrgCode(orgCode, userParams, page);
        List<SysUserSysDepartModel> list = pageList.getRecords();

        // 记录所有出现过的 user, key = userId
        Map<String, JSONObject> hasUser = new HashMap<>(list.size());

        JSONArray resultJson = new JSONArray(list.size());

        for (SysUserSysDepartModel item : list) {
            String userId = item.getId();
            // userId
            JSONObject getModel = hasUser.get(userId);
            // 之前已存在过该用户，直接合并数据
            if (getModel != null) {
                String departName = getModel.get("departName").toString();
                getModel.put("departName", (departName + " | " + item.getDepartName()));
            } else {
                // 将用户对象转换为json格式，并将部门信息合并到 json 中
                JSONObject json = JSON.parseObject(JSON.toJSONString(item));
                json.remove("id");
                json.put("userId", userId);
                json.put("departId", item.getDepartId());
                json.put("departName", item.getDepartName());
//                json.put("avatar", item.getSysUser().getAvatar());
                resultJson.add(json);
                hasUser.put(userId, json);
            }
        }

        IPage<JSONObject> result = new Page<>(pageNo, pageSize, pageList.getTotal());
        result.setRecords(resultJson.toJavaList(JSONObject.class));
        return Result.ok(result);
    }

    /**
     * 给指定部门添加对应的用户
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/editSysDepartWithUser", method = RequestMethod.POST)
    public Result<String> editSysDepartWithUser(@RequestBody SysDepartUsersVO sysDepartUsersVO) {
        Result<String> result = new Result<String>();
        try {
            String sysDepId = sysDepartUsersVO.getDepId();
            for (String sysUserId : sysDepartUsersVO.getUserIdList()) {
                SysUserDepart sysUserDepart = new SysUserDepart(null, sysUserId, sysDepId);
                QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
                queryWrapper.eq("dep_id", sysDepId).eq("user_id", sysUserId);
                SysUserDepart one = sysUserDepartService.getOne(queryWrapper);
                if (one == null) {
                    sysUserDepartService.save(sysUserDepart);
                }
            }
            result.setMessage("添加成功!");
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("出错了: " + e.getMessage());
            return result;
        }
    }

    /**
     * 删除指定机构的用户关系
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteUserInDepart", method = RequestMethod.DELETE)
    public Result<SysUserDepart> deleteUserInDepart(@RequestParam(name = "depId") String depId,
                                                    @RequestParam(name = "userId", required = true) String userId
    ) {
        Result<SysUserDepart> result = new Result<SysUserDepart>();
        try {
            QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
            queryWrapper.eq("dep_id", depId).eq("user_id", userId);
            boolean b = sysUserDepartService.remove(queryWrapper);
            if (b) {
                List<SysDepartRole> sysDepartRoleList = departRoleService.list(new QueryWrapper<SysDepartRole>().eq("depart_id", depId));
                List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
                if (roleIds != null && roleIds.size() > 0) {
                    QueryWrapper<SysDepartRoleUser> query = new QueryWrapper<>();
                    query.eq("user_id", userId).in("drole_id", roleIds);
                    departRoleUserService.remove(query);
                }
                result.success("删除成功!");
            } else {
                result.error500("当前选中部门与用户无关联关系!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 批量删除指定机构的用户关系
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteUserInDepartBatch", method = RequestMethod.DELETE)
    public Result<SysUserDepart> deleteUserInDepartBatch(
            @RequestParam(name = "depId") String depId,
            @RequestParam(name = "userIds", required = true) String userIds) {
        Result<SysUserDepart> result = new Result<SysUserDepart>();
        try {
            QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
            queryWrapper.eq("dep_id", depId).in("user_id", Arrays.asList(userIds.split(",")));
            boolean b = sysUserDepartService.remove(queryWrapper);
            if (b) {
                departRoleUserService.removeDeptRoleUser(Arrays.asList(userIds.split(",")), depId);
            }
            result.success("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 查询当前用户的所有部门/当前部门编码
     *
     * @return
     */
    @RequestMapping(value = "/getCurrentUserDeparts", method = RequestMethod.GET)
    public Result<Map<String, Object>> getCurrentUserDeparts() {
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            List<SysDepart> list = this.sysDepartService.queryUserDeparts(sysUser.getId());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", list);
            map.put("orgCode", sysUser.getOrgCode());
            result.setSuccess(true);
            result.setResult(map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("查询失败！");
        }
        return result;
    }


    /**
     * 用户注册接口
     *
     * @param jsonObject
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<JSONObject> userRegister(@RequestBody JSONObject jsonObject, SysUser user) {
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = jsonObject.getString("phone");
        String smscode = jsonObject.getString("smscode");
        Object code = redisUtil.get(phone);
        String username = jsonObject.getString("username");
        //未设置用户名，则用手机号作为用户名
        if (oConvertUtils.isEmpty(username)) {
            username = phone;
        }
        //未设置密码，则随机生成一个密码
        String password = jsonObject.getString("password");
        if (oConvertUtils.isEmpty(password)) {
            password = RandomUtil.randomString(8);
        }
        String email = jsonObject.getString("email");
        SysUser sysUser1 = sysUserService.getUserByName(username);
        if (sysUser1 != null) {
            result.setMessage("用户名已注册");
            result.setSuccess(false);
            return result;
        }
        SysUser sysUser2 = sysUserService.getUserByPhone(phone);
        if (sysUser2 != null) {
            result.setMessage("该手机号已注册");
            result.setSuccess(false);
            return result;
        }

        if (oConvertUtils.isNotEmpty(email)) {
            SysUser sysUser3 = sysUserService.getUserByEmail(email);
            if (sysUser3 != null) {
                result.setMessage("邮箱已被注册");
                result.setSuccess(false);
                return result;
            }
        }
        if (null == code) {
            result.setMessage("手机验证码失效，请重新获取");
            result.setSuccess(false);
            return result;
        }
        if (!smscode.equals(code.toString())) {
            result.setMessage("手机验证码错误");
            result.setSuccess(false);
            return result;
        }

        try {
            user.setCreateTime(new Date());// 设置创建时间
            String salt = oConvertUtils.randomGen(8);
            String passwordEncode = PasswordUtil.encrypt(username, password, salt);
            user.setSalt(salt);
            user.setUsername(username);
            user.setRealname(username);
            user.setPassword(passwordEncode);
            user.setEmail(email);
            user.setPhone(phone);
            user.setStatus(CommonConstant.USER_UNFREEZE);
            user.setDelFlag(CommonConstant.DEL_FLAG_0);
            user.setActivitiSync(CommonConstant.ACT_SYNC_0);
            sysUserService.addUserWithRole(user, "ee8626f80f7c2619917b6236f3a7f02b");//默认临时角色 test
            result.success("注册成功");
        } catch (Exception e) {
            result.error500("注册失败");
        }
        return result;
    }

    @RequestMapping(value = "/WXregister", method = RequestMethod.POST)
    public Result<SysUser> WXregister(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        //String selectedRoles = jsonObject.getString("selectedroles");
        String chepais = jsonObject.getString("chepais");//获取当前司机的车牌信息
        String selectedDeparts = "c97f7f2177ab4e359e1713fa7bfcd01a";//固定组织机构权限
        String selectedRoles = "1511524842802466817"; //固定角色权限
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            user.setCreateTime(new Date());//设置创建时间
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(1);
            user.setDelFlag(CommonConstant.DEL_FLAG_0);
            user.setDepartIds("c97f7f2177ab4e359e1713fa7bfcd01a");
            sysUserService.addUserWithRole(user, selectedRoles);
            sysUserService.addUserWithDepart(user, selectedDeparts);
            SysUser userByName = sysUserService.getUserByName(user.getUsername());
            if (userByName != null) {
                String id = userByName.getId();
                String realname = userByName.getRealname();
                String[] split = chepais.split(",");
                for (int i = 0; i < split.length; i++) {
                    SysUserCar sysUserCar = new SysUserCar();
                    sysUserCar.setUserId(id);
                    sysUserCar.setRealname(realname);
                    sysUserCar.setVehicle(split[i]);
                    sysUserCarService.save(sysUserCar);
                }
            } else {
                result.error500("关联车牌失败请联系实施人员！");
            }
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

//	/**
//	 * 根据用户名或手机号查询用户信息
//	 * @param
//	 * @return
//	 */
//	@GetMapping("/querySysUser")
//	public Result<Map<String, Object>> querySysUser(SysUser sysUser) {
//		String phone = sysUser.getPhone();
//		String username = sysUser.getUsername();
//		Result<Map<String, Object>> result = new Result<Map<String, Object>>();
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (oConvertUtils.isNotEmpty(phone)) {
//			SysUser user = sysUserService.getUserByPhone(phone);
//			if(user!=null) {
//				map.put("username",user.getUsername());
//				map.put("phone",user.getPhone());
//				result.setSuccess(true);
//				result.setResult(map);
//				return result;
//			}
//		}
//		if (oConvertUtils.isNotEmpty(username)) {
//			SysUser user = sysUserService.getUserByName(username);
//			if(user!=null) {
//				map.put("username",user.getUsername());
//				map.put("phone",user.getPhone());
//				result.setSuccess(true);
//				result.setResult(map);
//				return result;
//			}
//		}
//		result.setSuccess(false);
//		result.setMessage("验证失败");
//		return result;
//	}

    /**
     * 用户手机号验证
     */
    @PostMapping("/phoneVerification")
    public Result<Map<String, String>> phoneVerification(@RequestBody JSONObject jsonObject) {
        Result<Map<String, String>> result = new Result<Map<String, String>>();
        String phone = jsonObject.getString("phone");
        String smscode = jsonObject.getString("smscode");
        Object code = redisUtil.get(phone);
        if (!smscode.equals(code)) {
            result.setMessage("手机验证码错误");
            result.setSuccess(false);
            return result;
        }
        //设置有效时间
        redisUtil.set(phone, smscode, 600);
        //新增查询用户名
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        query.eq(SysUser::getPhone, phone);
        SysUser user = sysUserService.getOne(query);
        Map<String, String> map = new HashMap<>();
        map.put("smscode", smscode);
        map.put("username", user.getUsername());
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }

    /**
     * 用户更改密码
     */
    @GetMapping("/passwordChange")
    public Result<SysUser> passwordChange(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "password") String password,
                                          @RequestParam(name = "smscode") String smscode,
                                          @RequestParam(name = "phone") String phone) {
        Result<SysUser> result = new Result<SysUser>();
        if (oConvertUtils.isEmpty(username) || oConvertUtils.isEmpty(password) || oConvertUtils.isEmpty(smscode) || oConvertUtils.isEmpty(phone)) {
            result.setMessage("重置密码失败！");
            result.setSuccess(false);
            return result;
        }

        SysUser sysUser = new SysUser();
        Object object = redisUtil.get(phone);
        if (null == object) {
            result.setMessage("短信验证码失效！");
            result.setSuccess(false);
            return result;
        }
        if (!smscode.equals(object.toString())) {
            result.setMessage("短信验证码不匹配！");
            result.setSuccess(false);
            return result;
        }
        sysUser = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username).eq(SysUser::getPhone, phone));
        if (sysUser == null) {
            result.setMessage("未找到用户！");
            result.setSuccess(false);
            return result;
        } else {
            String salt = oConvertUtils.randomGen(8);
            sysUser.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
            sysUser.setPassword(passwordEncode);
            this.sysUserService.updateById(sysUser);
            result.setSuccess(true);
            result.setMessage("密码重置完成！");
            return result;
        }
    }


    /**
     * 根据TOKEN获取用户的部分信息（返回的数据是可供表单设计器使用的数据）
     *
     * @return
     */
    @GetMapping("/getUserSectionInfoByToken")
    public Result<?> getUserSectionInfoByToken(HttpServletRequest request, @RequestParam(name = "token", required = false) String token) {
        try {
            String username = null;
            // 如果没有传递token，就从header中获取token并获取用户信息
            if (oConvertUtils.isEmpty(token)) {
                username = JwtUtil.getUserNameByToken(request);
            } else {
                username = JwtUtil.getUsername(token);
            }

            log.debug(" ------ 通过令牌获取部分用户信息，当前用户： " + username);

            // 根据用户名查询用户信息
            SysUser sysUser = sysUserService.getUserByName(username);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sysUserId", sysUser.getId());
            map.put("sysUserCode", sysUser.getUsername()); // 当前登录用户登录账号
            map.put("sysUserName", sysUser.getRealname()); // 当前登录用户真实名称
            map.put("sysOrgCode", sysUser.getOrgCode()); // 当前登录用户部门编号

            log.debug(" ------ 通过令牌获取部分用户信息，已获取的用户信息： " + map);

            return Result.ok(map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(500, "查询失败:" + e.getMessage());
        }
    }

    /**
     * 【APP端接口】获取用户列表  根据用户名和真实名 模糊匹配
     *
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/appUserList")
    public Result<?> appUserList(@RequestParam(name = "keyword", required = false) String keyword,
                                 @RequestParam(name = "username", required = false) String username,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(name = "syncFlow", required = false) String syncFlow) {
        try {
            //TODO 从查询效率上将不要用mp的封装的page分页查询 建议自己写分页语句
            LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<SysUser>();
            if (oConvertUtils.isNotEmpty(syncFlow)) {
                query.eq(SysUser::getActivitiSync, CommonConstant.ACT_SYNC_1);
            }
            query.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_0);
            if (oConvertUtils.isNotEmpty(username)) {
                if (username.contains(",")) {
                    query.in(SysUser::getUsername, username.split(","));
                } else {
                    query.eq(SysUser::getUsername, username);
                }
            } else {
                query.and(i -> i.like(SysUser::getUsername, keyword).or().like(SysUser::getRealname, keyword));
            }
            Page<SysUser> page = new Page<>(pageNo, pageSize);
            IPage<SysUser> res = this.sysUserService.page(page, query);
            return Result.ok(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(500, "查询失败:" + e.getMessage());
        }

    }

    /**
     * 获取被逻辑删除的用户列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<SysUser> logicDeletedUserList = sysUserService.queryLogicDeleted();
        if (logicDeletedUserList.size() > 0) {
            // 批量查询用户的所属部门
            // step.1 先拿到全部的 userIds
            List<String> userIds = logicDeletedUserList.stream().map(SysUser::getId).collect(Collectors.toList());
            // step.2 通过 userIds，一次性查询用户的所属部门名字
            Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            logicDeletedUserList.forEach(item -> item.setOrgCode(useDepNames.get(item.getId())));
        }
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的用户
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String userIds = jsonObject.getString("userIds");
        if (StringUtils.isNotBlank(userIds)) {
            SysUser updateUser = new SysUser();
            updateUser.setUpdateBy(JwtUtil.getUserNameByToken(request));
            updateUser.setUpdateTime(new Date());
            sysUserService.revertLogicDeleted(Arrays.asList(userIds.split(",")), updateUser);
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除用户
     *
     * @param userIds 被删除的用户ID，多个id用半角逗号分割
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("userIds") String userIds) {
        if (StringUtils.isNotBlank(userIds)) {
            sysUserService.removeLogicDeleted(Arrays.asList(userIds.split(",")));
        }
        return Result.ok("删除成功");
    }


    /**
     * 移动端修改用户信息
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/appEdit", method = RequestMethod.PUT)
    public Result<SysUser> appEdit(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        try {
            String username = JwtUtil.getUserNameByToken(request);
            SysUser sysUser = sysUserService.getUserByName(username);
            baseCommonService.addLog("移动端编辑用户，id： " + jsonObject.getString("id"), CommonConstant.LOG_TYPE_2, 2);
            String realname = jsonObject.getString("realname");
            String avatar = jsonObject.getString("avatar");
            String sex = jsonObject.getString("sex");
            String phone = jsonObject.getString("phone");
            String email = jsonObject.getString("email");
            Date birthday = jsonObject.getDate("birthday");
            SysUser userPhone = sysUserService.getUserByPhone(phone);
            if (sysUser == null) {
                result.error500("未找到对应用户!");
            } else {
                if (userPhone != null) {
                    String userPhonename = userPhone.getUsername();
                    if (!userPhonename.equals(username)) {
                        result.error500("手机号已存在!");
                        return result;
                    }
                }
                if (StringUtils.isNotBlank(realname)) {
                    sysUser.setRealname(realname);
                }
                if (StringUtils.isNotBlank(avatar)) {
                    sysUser.setAvatar(avatar);
                }
                if (StringUtils.isNotBlank(sex)) {
                    sysUser.setSex(Integer.parseInt(sex));
                }
                if (StringUtils.isNotBlank(phone)) {
                    sysUser.setPhone(phone);
                }
                if (StringUtils.isNotBlank(email)) {
                    sysUser.setEmail(email);
                }
                if (null != birthday) {
                    sysUser.setBirthday(birthday);
                }
                sysUser.setUpdateTime(new Date());
                sysUserService.updateById(sysUser);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败!");
        }
        return result;
    }

    /**
     * 移动端保存设备信息
     *
     * @param clientId
     * @return
     */
    @RequestMapping(value = "/saveClientId", method = RequestMethod.GET)
    public Result<SysUser> saveClientId(HttpServletRequest request, @RequestParam("clientId") String clientId) {
        Result<SysUser> result = new Result<SysUser>();
        try {
            String username = JwtUtil.getUserNameByToken(request);
            SysUser sysUser = sysUserService.getUserByName(username);
            if (sysUser == null) {
                result.error500("未找到对应用户!");
            } else {
                sysUser.setClientId(clientId);
                sysUserService.updateById(sysUser);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败!");
        }
        return result;
    }

    /**
     * 根据userid获取用户信息和部门员工信息
     *
     * @return Result
     */
    @GetMapping("/queryChildrenByUsername")
    public Result queryChildrenByUsername(@RequestParam("userId") String userId) {
        //获取用户信息
        Map<String, Object> map = new HashMap<String, Object>();
        SysUser sysUser = sysUserService.getById(userId);
        String username = sysUser.getUsername();
        Integer identity = sysUser.getUserIdentity();
        map.put("sysUser", sysUser);
        if (identity != null && identity == 2) {
            //获取部门用户信息
            String departIds = sysUser.getDepartIds();
            if (StringUtils.isNotBlank(departIds)) {
                List<String> departIdList = Arrays.asList(departIds.split(","));
                List<SysUser> childrenUser = sysUserService.queryByDepIds(departIdList, username);
                map.put("children", childrenUser);
            }
        }
        return Result.ok(map);
    }

    /**
     * 移动端查询部门用户信息
     *
     * @param departId
     * @return
     */
    @GetMapping("/appQueryByDepartId")
    public Result<List<SysUser>> appQueryByDepartId(@RequestParam(name = "departId", required = false) String departId) {
        Result<List<SysUser>> result = new Result<List<SysUser>>();
        List<String> list = new ArrayList<String>();
        list.add(departId);
        List<SysUser> childrenUser = sysUserService.queryByDepIds(list, null);
        result.setResult(childrenUser);
        return result;
    }

    /**
     * 移动端查询用户信息(通过用户名模糊查询)
     *
     * @param keyword
     * @return
     */
    @GetMapping("/appQueryUser")
    public Result<List<SysUser>> appQueryUser(@RequestParam(name = "keyword", required = false) String keyword) {
        Result<List<SysUser>> result = new Result<List<SysUser>>();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>();
        //TODO 外部模拟登陆临时账号，列表不显示
        queryWrapper.ne(SysUser::getUsername, "_reserve_user_external");
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.and(i -> i.like(SysUser::getUsername, keyword).or().like(SysUser::getRealname, keyword));
        }
        List<SysUser> list = sysUserService.list(queryWrapper);
        //批量查询用户的所属部门
        //step.1 先拿到全部的 useids
        //step.2 通过 useids，一次性查询用户的所属部门名字
        List<String> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());
        if (userIds != null && userIds.size() > 0) {
            Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            list.forEach(item -> {
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }
        result.setResult(list);
        return result;
    }

    /**
     * 根据用户名修改手机号
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/updateMobile", method = RequestMethod.PUT)
    public Result<?> changMobile(@RequestBody JSONObject json, HttpServletRequest request) {
        String smscode = json.getString("smscode");
        String phone = json.getString("phone");
        Result<SysUser> result = new Result<SysUser>();
        //获取登录用户名
        String username = JwtUtil.getUserNameByToken(request);
        if (oConvertUtils.isEmpty(username) || oConvertUtils.isEmpty(smscode) || oConvertUtils.isEmpty(phone)) {
            result.setMessage("修改手机号失败！");
            result.setSuccess(false);
            return result;
        }
        Object object = redisUtil.get(phone);
        if (null == object) {
            result.setMessage("短信验证码失效！");
            result.setSuccess(false);
            return result;
        }
        if (!smscode.equals(object.toString())) {
            result.setMessage("短信验证码不匹配！");
            result.setSuccess(false);
            return result;
        }
        SysUser user = sysUserService.getUserByName(username);
        if (user == null) {
            return Result.error("用户不存在！");
        }
        user.setPhone(phone);
        sysUserService.updateById(user);
        return Result.ok("手机号设置成功!");
    }


    /**
     * 根据对象里面的属性值作in查询 属性可能会变 用户组件用到
     *
     * @param sysUser
     * @return
     */
    @GetMapping("/getMultiUser")
    public List<SysUser> getMultiUser(SysUser sysUser) {
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, null);
        List<SysUser> ls = this.sysUserService.list(queryWrapper);
        for (SysUser user : ls) {
            user.setPassword(null);
            user.setSalt(null);
        }
        return ls;
    }

    @AutoLog(value = "岗前培训-查询工种")
    @ApiOperation(value = "岗前培训-查询工种", notes = "岗前培训-查询工种")
    @GetMapping(value = "/getUserByWorktype")
    public Result<?> getUserByWorktype(@RequestParam(name = "worktype", required = true) String worktype) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getWorktype, worktype);
        List<SysUser> list = sysUserService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "岗前培训-查询讲师/交警")
    @ApiOperation(value = "岗前培训-查询讲师/交警", notes = "岗前培训-查询讲师/交警")
    @GetMapping(value = "/getJsjjByType")
    public Result<?> getJsjjByType(@RequestParam(name = "worktype", required = true) String worktype) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        if (worktype.equals("6")) {
            queryWrapper.in(SysUserRole::getRoleId, "1889881845103083521","1868567039125569537");
        }
        if (worktype.equals("7")) {
            queryWrapper.eq(SysUserRole::getRoleId, "1889881937751064577");
        }
        List<SysUserRole> list = sysUserRoleService.list(queryWrapper);
        List userList = new ArrayList();
        for (SysUserRole sysUserRole : list) {
            String userId = sysUserRole.getUserId();
            LambdaQueryWrapper<SysUser> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(SysUser::getId, userId);
            SysUser sysUser = sysUserService.getOne(queryWrapper1);
            userList.add(sysUser);
        }

        return Result.OK(userList);
    }


    @AutoLog(value = "岗前培训-根据身份证获取信息")
    @ApiOperation(value = "岗前培训-根据身份证获取信息", notes = "岗前培训-根据身份证获取信息")
    @GetMapping(value = "/getXxByIdNumber")
    public Result<?> getXxByIdNumber(@RequestParam(name = "idNumber", required = true) String idNumber) {
        int sex = IdentityCardUtil.getSex1(idNumber);
        Long age = IdentityCardUtil.getAge(idNumber);
        java.time.LocalDate birthday = IdentityCardUtil.getBirthday(idNumber);
        String nativePlace = IdentityCardUtil.getNativePlace(idNumber);

        Map map = new HashMap();
        map.put("sex", sex);
        map.put("age", age);
        map.put("birthday", birthday);
        map.put("nativeplace", nativePlace);

        return Result.OK(map);
    }


}
