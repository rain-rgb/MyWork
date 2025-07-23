package com.trtm.iot.wzliaocang.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzliaocang.entity.WzliaocangChange;
import com.trtm.iot.wzliaocang.service.IWzliaocangChangeService;
import com.trtm.iot.wzliaocang.util.WgWebapi;
import com.trtm.iot.wzliaocangcommand.entity.Wzliaocangcommand;
import com.trtm.iot.wzliaocangcommand.service.IWzliaocangcommandService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.jeecg.common.util.DateUtils.getTimestampten;


/**
 * @Description: 料仓配置表
 * @Author: jeecg-boot
 * @Date: 2021-05-07
 * @Version: V1.0
 */
@Api(tags = "料仓配置表")
@RestController
@RequestMapping("/wzliaocang/wzliaocang")
@Slf4j
public class WzliaocangController extends JeecgController<Wzliaocang, IWzliaocangService> {
    @Autowired
    private IWzliaocangService wzliaocangService;
    private String uuid;
    @Autowired
    private IWzliaocangcommandService wzliaocangcommandService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;

    @Autowired
    private IWzliaocangChangeService wzliaocangChangeService;


    @AutoLog(value = "料仓配置表-门禁命令")
    @ApiOperation(value = "料仓配置表-门禁命令", notes = "料仓配置表-门禁命令")
    @PostMapping(value = "/mengjin")
    public Result<?> mengjin(@RequestBody Wzliaocang wzliaocang,
                        String sys_depart_orgcode,
                        HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        WzliaocangChange wzliaocangChange = new WzliaocangChange();

        String back = HttpRequest.post("http://47.97.158.215:8015/rest/" + wzliaocang.getDanwei())
                .header("content-type", "text/plain")
                .body(wzliaocang.getLiaoweino())
                .execute()
                .body();

        if (back.equals("1")) {
            if(wzliaocang.getDanwei() .equals( "postCard")){
                String[] split = wzliaocang.getLiaoweino().split("\\|");
                wzliaocangChange.setLcWeight("未修改");
                wzliaocangChange.setLiaoweino(wzliaocang.getLiaoweino());
                wzliaocangChange.setLiaocangguid(wzliaocang.getGuid());
                wzliaocangChange.setRemark("门禁配置");
                wzliaocangChange.setUsepeople(loginUser.getRealname());
                wzliaocangChangeService.save(wzliaocangChange);
            }
            return Result.OK("请求成功");
        } else {
            return Result.error("请求失败" + back);
        }


    }

    @AutoLog(value = "料仓配置表-料位命令")
    @ApiOperation(value = "料仓配置表-料位命令", notes = "料仓配置表-料位命令")
    @PostMapping(value = "/lw")
    public Result<?> lw(@RequestBody Wzliaocang wzliaocang,
                        String sys_depart_orgcode,
                        HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        WzliaocangChange wzliaocangChange = new WzliaocangChange();

        String back = HttpRequest.post("http://dtu.traiot.cn:8029/rest/" + wzliaocang.getDanwei())
                .header("content-type", "text/plain")
                .body(wzliaocang.getLiaoweino())
                .execute()
                .body();

        if (back.equals("1")) {
            if(wzliaocang.getDanwei() .equals( "posttoStd")){
                String[] split = wzliaocang.getLiaoweino().split("\\|");
                List<Wzliaocang> wzliaocangs = wzliaocangService.lclistBylw(split[0]);
                wzliaocangChange.setLcWeight(split[1]);
                wzliaocangChange.setLiaoweino(split[0]);
                wzliaocangChange.setLiaocangguid(wzliaocangs.get(0).getGuid());
                wzliaocangChange.setRemark("校称成功");
                wzliaocangChange.setUsepeople(loginUser.getRealname());
                wzliaocangChangeService.save(wzliaocangChange);
            } else if (wzliaocang.getDanwei() .equals("posttoZero") ) {
                List<Wzliaocang> wzliaocangs = wzliaocangService.lclistBylw(wzliaocang.getLiaoweino());
                wzliaocangChange.setLiaocangguid(wzliaocangs.get(0).getGuid());
                wzliaocangChange.setLcWeight("0");
                wzliaocangChange.setLiaoweino(wzliaocang.getLiaoweino());
                wzliaocangChange.setRemark("校零成功");
                wzliaocangChange.setUsepeople(loginUser.getRealname());
                wzliaocangChangeService.save(wzliaocangChange);
            }
            return Result.OK("请求成功");
        } else {
            return Result.error("请求失败" + back);
        }


    }


    /**
     * 分页列表查询
     *
     * @param wzliaocang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "料仓配置表-分页列表查询")
    @ApiOperation(value = "料仓配置表-分页列表查询", notes = "料仓配置表-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "wzgl/wzliaocang/WzliaocangList")
    public Result<?> queryPageList(Wzliaocang wzliaocang,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzliaocang.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wzliaocang.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        wzliaocang.setName("*" + wzliaocang.getName() + "*");
        QueryWrapper<Wzliaocang> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocang, req.getParameterMap());
        Page<Wzliaocang> page = new Page<Wzliaocang>(pageNo, pageSize);
        IPage<Wzliaocang> pageList = wzliaocangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param wzliaocang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "料仓配置表-分页列表查询")
    @ApiOperation(value = "料仓配置表-分页列表查询", notes = "料仓配置表-分页列表查询")
    @GetMapping(value = "/lists")
    @PermissionData(pageComponent = "wzgl/wzliaocang/WzliaocangLists")
    public Result<?> findPageList(Wzliaocang wzliaocang,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                  HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzliaocang.setSysOrgCode(sys_depart_orgcode + "*");
        }
        wzliaocang.setName("*" + wzliaocang.getName() + "*");
        QueryWrapper<Wzliaocang> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocang, req.getParameterMap());
        Page<Wzliaocang> page = new Page<Wzliaocang>(pageNo, pageSize);
        IPage<Wzliaocang> pageList = wzliaocangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    @AutoLog(value = "料仓配置表-分页列表查询")
    @ApiOperation(value = "料仓配置表-分页列表查询", notes = "料仓配置表-分页列表查询")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList6(Wzliaocang wzliaocang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzliaocang.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        wzliaocang.setName("*" + wzliaocang.getName() + "*");
        QueryWrapper<Wzliaocang> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocang, req.getParameterMap());
        List<Wzliaocang> list = wzliaocangService.list(queryWrapper);
        return Result.OK(list);
    }
    @AutoLog(value = "料仓配置表-分页列表查询")
    @ApiOperation(value = "料仓配置表-分页列表查询", notes = "料仓配置表-分页列表查询")
    @GetMapping(value = "/ruKuList")
    @PermissionData(pageComponent = "wzgl/wzliaocang/WzliaocangLists")
    public Result<?> queryRuKuList(Wzliaocang wzliaocang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzliaocang.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        wzliaocang.setName("*" + wzliaocang.getName() + "*");
        QueryWrapper<Wzliaocang> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocang, req.getParameterMap());
        List<Wzliaocang> list = wzliaocangService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "料仓配置表-库存查询")
    @ApiOperation(value = "料仓配置表-库存查询", notes = "料仓配置表-库存查询")
    @GetMapping(value = "/guliaolist")
    public Result<?> guliaoPageList(Wzliaocang wzliaocang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzliaocang.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        wzliaocang.setName("*" + wzliaocang.getName() + "*");
        QueryWrapper<Wzliaocang> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocang, req.getParameterMap());
        List selectkucun = wzliaocangService.selectkucun(pageNo, pageSize);
        return Result.OK(selectkucun);
    }

    @AutoLog(value = "料仓配置表-查询")
    @ApiOperation(value = "料仓配置表-列表查询", notes = "料仓配置表-列表查询")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(Wzliaocang wzliaocang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzliaocang.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        wzliaocang.setName("*" + wzliaocang.getName() + "*");
        if (null != wzliaocang.getCode()) {
            wzliaocang.setCode(wzliaocang.getCode());
        }
        QueryWrapper<Wzliaocang> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocang, req.getParameterMap());
        List<Wzliaocang> pageList = wzliaocangService.list(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "料仓配置表-查询")
    @ApiOperation(value = "料仓配置表-列表查询", notes = "料仓配置表-列表查询")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList5(Wzliaocang wzliaocang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req, String shebeino) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzliaocang.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wzliaocang.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
//		String cailiaono = wzliaocang.getCailiaono();
//		String[] split = cailiaono.split(",");
//		List<String> cailiaonolist = new ArrayList<>();
//		Collections.addAll(cailiaonolist, split);
        if (wzliaocang.getCailiaono() != null) {
            wzliaocang.setCailiaono(wzliaocang.getCailiaono());
        }
        if (shebeino != null) {
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeino);
            if (shebeiInfo != null) {
                wzliaocang.setSysOrgCode(shebeiInfo.getSysOrgCode() + '*');
            }
        }
        QueryWrapper<Wzliaocang> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocang, req.getParameterMap());
        List<Wzliaocang> pageList = wzliaocangService.list(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "料仓配置表-关门")
    @ApiOperation(value = "料仓配置表-关门", notes = "料仓配置表-关门")
    @PutMapping(value = "/list2")
    public Result<?> queryPageList2(Wzliaocang wzliaocang) {
        Integer index = wzliaocang.getIndexs();
        String code = wzliaocang.getCode();
        String sysOrgCode = wzliaocang.getSysOrgCode();
        Wzliaocangcommand selectones = wzliaocangcommandService.selectone(index, code, sysOrgCode);
        if (selectones != null) {
            wzliaocangcommandService.updateone(selectones.getId(), index, code);
            return Result.OK("关门成功");
        } else {
            return Result.OK("未查到料仓命令记录");
        }
    }

    @AutoLog(value = "料仓配置表-开门")
    @ApiOperation(value = "料仓配置表-开门", notes = "料仓配置表-开门")
    @PutMapping(value = "/list3")
    public Result<?> queryPageList3(Wzliaocang wzliaocang) {
        Integer index = wzliaocang.getIndexs();
        String code = wzliaocang.getCode();
        String sysOrgCode = wzliaocang.getSysOrgCode();
        Wzliaocangcommand selectone = wzliaocangcommandService.selectone(index, code, sysOrgCode);
        if (selectone != null) {
            wzliaocangcommandService.updateone1(selectone.getId(), index, code);
            return Result.OK("开门成功");
        } else {
            return Result.OK("未查到料仓门禁记录");
        }
    }

    /**
     * 当前用户获取当前料仓配置列表
     *
     * @param Wzliaocang
     * @param req
     * @return
     */
    @RequestMapping(value = "/list1", method = RequestMethod.GET)
    public Result<List<Wzliaocang>> queryPageList3(String sys_depart_orgcode, Wzliaocang Wzliaocang, HttpServletRequest req) {
        Result<List<Wzliaocang>> result = new Result<List<Wzliaocang>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<Wzliaocang> pageList = wzliaocangService.userliaocangList(loginUser.getOrgCode());
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        return result;
    }

    /**
     * 当前用户获取当前料仓配置列表
     *
     * @param Wzliaocang
     * @param req
     * @return
     */
    @RequestMapping(value = "/list7", method = RequestMethod.GET)
    public Result<List<Wzliaocang>> queryPageList7(String sys_depart_orgcode, Wzliaocang Wzliaocang, HttpServletRequest req) {
        Result<List<Wzliaocang>> result = new Result<List<Wzliaocang>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String sysOrgCode = null;
        if (!StringUtils.isEmpty(sys_depart_orgcode)) {
            sysOrgCode = sys_depart_orgcode;
        } else {
            sysOrgCode = loginUser.getOrgCode();
        }
        List<Wzliaocang> pageList = wzliaocangService.userliaocangList(sysOrgCode);
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        return result;
    }


    @AutoLog(value = "料仓配置表-添加")
    @ApiOperation(value = "料仓配置表-添加", notes = "料仓配置表-添加")
    @PostMapping(value = "/openSN")
    public Result<?> openSN(@RequestBody Wzliaocang wzliaocang) {
        String message = "";
        if (wzliaocang.getInfraredFence() != null) {//合格，并关联栅栏
            JSONObject sendObject = new JSONObject();
            String post = null;
            if ("2".equals(wzliaocang.getLiaocangStatus()) || "4".equals(wzliaocang.getLiaocangStatus())) {
                message = "料仓状态为待检验和检验中时，仓门禁止开启";
            } else {
                sendObject.set("deviceId", wzliaocang.getInfraredFence());
                sendObject.set("command", "on");
                post = HttpRequest.post("http://101.37.166.105:8002/appCementSiloDoorLock/sendDeviceCommand")
                        .header("Content-Type", "application/json")
                        .body(String.valueOf(sendObject))
                        .execute()
                        .body();
                JSONObject postObject = new JSONObject(post);
                Object code = postObject.get("code");
                if ("0000".equals(String.valueOf(code))) {
                    log.info(String.format("发送设备指令成功" + DateUtils.now()));
                    message = "开门成功！";
                } else {
                    log.info(String.format("发送设备指令失败" + DateUtils.now()));
                    message = "开门失败！";
                }
            }

        } else {
            message = "未配置电子门禁！！！";
        }


        return Result.OK(message);
    }


    /**
     * 添加
     *
     * @param wzliaocang
     * @return
     */
    @AutoLog(value = "料仓配置表-添加")
    @ApiOperation(value = "料仓配置表-添加", notes = "料仓配置表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Wzliaocang wzliaocang) {
        if (wzliaocang.getInfraredFence() != null) {//合格，并关联栅栏
            JSONObject sendObject = new JSONObject();
            String post = null;
            if ("2".equals(wzliaocang.getLiaocangStatus()) || "4".equals(wzliaocang.getLiaocangStatus())) {
                sendObject.set("deviceId", wzliaocang.getInfraredFence());
                sendObject.set("command", "on");
                post = HttpRequest.post("http://101.37.166.105:8001/appInfraredVirtualGateway/sendDeviceCommand")
                        .header("Content-Type", "application/json")
                        .body(String.valueOf(sendObject))
                        .execute()
                        .body();
                JSONObject postObject = new JSONObject(post);
                Object code = postObject.get("code");
                if ("0000".equals(String.valueOf(code))) {
                    log.info(String.format("发送设备指令成功" + DateUtils.now()));
                } else {
                    log.info(String.format("发送设备指令失败" + DateUtils.now()));
                }
            }
        }

        Map selectqueryone = wzliaocangService.selectqueryone(wzliaocang.getSysOrgCode());
        if (selectqueryone != null) {
            wzliaocang.setDepartid(String.valueOf(selectqueryone.get("id")));
        }
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        wzliaocang.setGuid(uuid);
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        wzliaocang.setTs(ts);
        wzliaocang.setIfStatus(("2".equals(wzliaocang.getLiaocangStatus()) || "4".equals(wzliaocang.getLiaocangStatus())) ? 1 : 0);
        if (wzliaocang.getJinchangTime() == null || "".equals(wzliaocang.getJinchangTime())) {
            wzliaocang.setJinchangTime("/");
        }
        wzliaocangService.save(wzliaocang);
        return Result.OK("添加成功！");
    }

    /**
     * 料仓对外开放添加数据接口
     *
     * @param wzliaocang
     * @return
     */
    @AutoLog(value = "料仓配置表-添加")
    @ApiOperation(value = "料仓配置表-添加", notes = "料仓配置表-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody Wzliaocang wzliaocang) {
        QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("guid", wzliaocang.getGuid());
        Wzliaocang one = wzliaocangService.getOne(queryWrapper);
        if (one == null) {
            wzliaocangService.save(wzliaocang);
            return Result.OK("添加成功！");
        } else {
            return Result.error("添加失败！");
        }
    }

    @AutoLog(value = "料仓配置表-编辑")
    @ApiOperation(value = "料仓配置表-编辑", notes = "料仓配置表-编辑")
    @PostMapping(value = "/editWeight")
    public Result<?> editWeight(@RequestBody Wzliaocang wzliaocang) {
        String message = "";
        QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Infrared_fence",wzliaocang.getInfraredFence());
        List<Wzliaocang> list = wzliaocangService.list(queryWrapper);
        if(list.size()==1){
            wzliaocang.setId(list.get(0).getId());
            wzliaocangService.updateById(wzliaocang);
            message=list.get(0).getName()+"更新重量为"+wzliaocang.getWeight();
        }else{
            message = "未匹配对应料仓，请在平台确认是否配置正确";
        }
        return Result.OK("编辑成功!" + message, "");
    }

    /**
     * 编辑
     *
     * @param wzliaocang
     * @return
     */
    @AutoLog(value = "料仓配置表-编辑")
    @ApiOperation(value = "料仓配置表-编辑", notes = "料仓配置表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Wzliaocang wzliaocang) {
        String message = sendZl(wzliaocang);
        Map selectqueryone = wzliaocangService.selectqueryone(wzliaocang.getSysOrgCode());
        if (selectqueryone != null) {
            wzliaocang.setDepartid(String.valueOf(selectqueryone.get("id")));
        }
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        wzliaocang.setTs(ts);
        wzliaocang.setIfStatus(("2".equals(wzliaocang.getLiaocangStatus()) || "4".equals(wzliaocang.getLiaocangStatus())) ? 1 : 0);
        if (wzliaocang.getJinchangTime() == null || "".equals(wzliaocang.getJinchangTime())) {
            wzliaocang.setJianyanTime("/");
        }
        wzliaocangService.updateById(wzliaocang);
        return Result.OK("编辑成功!" + message, "");
    }

    // 根据设备编号厂家区分栅栏布防请求
    public String sendZl(Wzliaocang wzliaocang){
        String message = "";
        if (! StringUtils.isEmpty( wzliaocang.getInfraredFence())  ) {//合格，并关联栅栏
             String sbbh = wzliaocang.getInfraredFence();
            JSONObject sendObject = new JSONObject();
            if(sbbh.contains("R")){
                String post = null;
                if ("2".equals(wzliaocang.getLiaocangStatus()) || "4".equals(wzliaocang.getLiaocangStatus())) {
                    sendObject.set("deviceId", sbbh.replace("R",""));
                    sendObject.set("command", "on");
                    message = "布防-";
                } else {
                    sendObject.set("deviceId",  sbbh.replace("R",""));
                    sendObject.set("command", "off");
                    message = "撤防-";
                }
                post = HttpRequest.post("http://101.37.166.105:8001/appInfraredVirtualGateway/sendDeviceCommand")
                        .header("Content-Type", "application/json")
                        .body(String.valueOf(sendObject))
                        .execute()
                        .body();
                try {
                    JSONObject postObject = new JSONObject(post);
                    Object code = postObject.get("code");
                    if ("0000".equals(String.valueOf(code))) {
                        message =message+ "门禁开关指令发送成功";
                    } else {
                        message = message+ "门禁开关指令发送失败！！！";
                    }
                } catch (Exception e) {
                    message = message+ "门禁开关指令发送失败 ！！！";
                }
            }else{
                String command = "";
                if ("2".equals(wzliaocang.getLiaocangStatus()) || "4".equals(wzliaocang.getLiaocangStatus())) {
                    command="posttoOpen";
                    message = "布防-";
                } else {
                    command="posttoClose";
                    message = "撤防-";
                }
                String back = HttpRequest.post("http://dtu.traiot.cn:8029/rest/" + command)
                        .header("content-type", "text/plain")
                        .body(sbbh)
                        .execute()
                        .body();

                if (back.equals("1")) {
                     message =message+"指令发送成功！" ;
                } else {
                     message =message+"指令发送失败！！！" ;
                }
            }


        }
        return message;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "料仓配置表-通过id删除")
    @ApiOperation(value = "料仓配置表-通过id删除", notes = "料仓配置表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzliaocangService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "料仓配置表-批量删除")
    @ApiOperation(value = "料仓配置表-批量删除", notes = "料仓配置表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzliaocangService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "料仓配置表-通过id查询")
    @ApiOperation(value = "料仓配置表-通过id查询", notes = "料仓配置表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Wzliaocang wzliaocang = wzliaocangService.getById(id);
        if (wzliaocang == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzliaocang);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzliaocang
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzliaocang wzliaocang) {
        return super.exportXls(request, wzliaocang, Wzliaocang.class, "料仓配置表");
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
        return super.importExcel(request, response, Wzliaocang.class);
    }

    @GetMapping(value = "/getliaocangList")
    public Result<?> getliaocangList(Wzliaocang wzliaocang, @RequestParam(name = "shebeibianhao", required = true) String shebeibianhao,
                                     HttpServletRequest req) {
        String cailiaono = wzliaocang.getCailiaono();
        QueryWrapper<Wzcailiaonamedict> wzcailiaonamedictQueryWrapper1 = new QueryWrapper<>();
        wzcailiaonamedictQueryWrapper1.eq("cailiaoNo", cailiaono);
        Wzcailiaonamedict one1 = wzcailiaonamedictService.getOne(wzcailiaonamedictQueryWrapper1);
        String parentnode = one1.getNodetype();
        wzliaocang.setCailiaono(parentnode);

        QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
        shebeiInfoQueryWrapper.eq("sbjno", shebeibianhao);
        ShebeiInfo one = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
        String sysOrgCode = one.getSysOrgCode();
        wzliaocang.setSysOrgCode(sysOrgCode);

        QueryWrapper<Wzliaocang> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocang, req.getParameterMap());
        queryWrapper.select("name,guid");
        queryWrapper.eq("liaocang_status", "1");
        List<Wzliaocang> list = wzliaocangService.list(queryWrapper);
        return Result.OK(list);
    }

    @GetMapping("/getJYP")
    public Result<?> getJYP(@RequestParam("storageId") String guid) {
        List list = wzliaocangService.getJYP(guid);
        return Result.OK(list);
    }


    /**
     * 分页列表查询
     *
     * @param wzliaocang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "料仓配置表-状态查询")
    @ApiOperation(value = "料仓配置表-状态查询", notes = "料仓配置表-状态查询")
    @GetMapping(value = "/searchLCStatus")
    public Result<?> searchLCStatus(Wzliaocang wzliaocang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        QueryWrapper<Wzliaocang> queryWrapper = QueryGenerator.initQueryWrapper(wzliaocang, req.getParameterMap());
        Page<Wzliaocang> page = new Page<Wzliaocang>(pageNo, pageSize);
        IPage<Wzliaocang> pageList = wzliaocangService.page(page, queryWrapper);
        if (pageList.getRecords().size() == 0) {
            return Result.error("未匹配到对应料仓，请联系管理员进行配置");
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "料仓配置-微耕门禁远程开门")
    @ApiOperation(value = "料仓配置-微耕门禁远程开门", notes = "料仓配置-微耕门禁远程开门")
    @PostMapping(value = "/ceshi")
    public void wgYuanChengKaiMen(int sN) {

        String url = WgWebapi.testUrl;
//        int controllerSN = WgWebapi.testControllerSN;
        int controllerSN = sN;

        WgWebapi.logInfo("远程开门");
        String op = "远程开门";
        int doorno = 1;
        try {

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode webcmd = mapper.createObjectNode();
            webcmd.put("jsonrpc", "2.0");
            webcmd.put("method", "远程开门");
            ArrayNode params = webcmd.putArray("params");
            ObjectNode param = mapper.createObjectNode();
            param.put("设备序列号", controllerSN);
            param.put("门号", doorno);
            params.add(param);
            webcmd.put("id", 1001);


            String body = webcmd.toString();
            WgWebapi webapi = new WgWebapi();
            String strResult = webapi.PushToWebWithjson(url, body);

            if (strResult.length() == 0) {
                WgWebapi.logInfo("通信失败!");
            } else {
                WgWebapi.logInfo((new ObjectMapper()).readTree(strResult).toPrettyString()); //.toString());
                boolean bvalid = webapi.successIsTrue(strResult);
                if (bvalid) {
                    WgWebapi.logInfo(String.format("%d %s  %s ", controllerSN, op, "成功."));
                } else {
                    WgWebapi.logInfo(String.format("%d %s  %s ", controllerSN, op, "失败..."));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AutoLog(value = "料仓配置-微耕门禁远程开门设置门常开")
    @ApiOperation(value = "料仓配置-微耕门禁远程开门设置门常开", notes = "料仓配置-微耕门禁远程开门设置门常开")
    @PostMapping(value = "/doorOpen")
    public Result<?> wgDoorOpen(@RequestBody Wzliaocang wzliaocang) {
        String lcSN = wzliaocangService.getSnByGuid(wzliaocang.getGuid());
        String url = WgWebapi.testUrl;         //"http://localhost:61080/";
        int controllerSN = Integer.parseInt(lcSN);

        WgWebapi.logInfo("设置门控制参数");
        String op = "设置门控制参数";
        int doorno = 1;
        //String doorMode = "在线";
        String doorMode = "常开";
        //    doorMode = "常闭";
        int doordelay = 3;  //默认3秒, 可设置25秒

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode webcmd = mapper.createObjectNode();
            webcmd.put("jsonrpc", "2.0");
            webcmd.put("method", "设置门控制参数");
            ArrayNode params = webcmd.putArray("params");
            ObjectNode param = mapper.createObjectNode();
            param.put("设备序列号", controllerSN);
            param.put("门号", doorno);
            param.put("控制方式", doorMode);
            param.put("开门延时(秒)", doordelay);
            params.add(param);
            webcmd.put("id", 1004);

            String body = webcmd.toString();
            WgWebapi webapi = new WgWebapi();
            String strResult = webapi.PushToWebWithjson(url, body);

            if (strResult.length() == 0) {
                WgWebapi.logInfo("通信失败!");
                return Result.error("开门失败...");
            } else {
                WgWebapi.logInfo((new ObjectMapper()).readTree(strResult).toPrettyString()); //.toString());
                boolean bvalid = webapi.successIsTrue(strResult);
                if (bvalid) {
                    WgWebapi.logInfo(String.format("%d %s  %s ", controllerSN, op, "成功."));
                    return Result.OK("开门成功，当前状态为" + doorMode);
                } else {
                    WgWebapi.logInfo(String.format("%d %s  %s ", controllerSN, op, "失败..."));
                    return Result.error("开门失败...");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AutoLog(value = "料仓配置-微耕门禁远程开门设置门常闭")
    @ApiOperation(value = "料仓配置-微耕门禁远程开门设置门常闭", notes = "料仓配置-微耕门禁远程开门设置门常闭")
    @PostMapping(value = "/doorClose")
    public Result<?> wgDoorClose(@RequestBody Wzliaocang wzliaocang) {
        String lcSN = wzliaocangService.getSnByGuid(wzliaocang.getGuid());
        String url = WgWebapi.testUrl;
        int controllerSN = Integer.parseInt(lcSN);

        WgWebapi.logInfo("设置门控制参数");
        String op = "设置门控制参数";
        int doorno = 1;
        //String doorMode = "在线";
//        String doorMode = "常开";
        String doorMode = "常闭";
        int doordelay = 3;  //默认3秒, 可设置25秒

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode webcmd = mapper.createObjectNode();
            webcmd.put("jsonrpc", "2.0");
            webcmd.put("method", "设置门控制参数");
            ArrayNode params = webcmd.putArray("params");
            ObjectNode param = mapper.createObjectNode();
            param.put("设备序列号", controllerSN);
            param.put("门号", doorno);
            param.put("控制方式", doorMode);
            param.put("开门延时(秒)", doordelay);
            params.add(param);
            webcmd.put("id", 1004);

            String body = webcmd.toString();
            WgWebapi webapi = new WgWebapi();
            String strResult = webapi.PushToWebWithjson(url, body);

            if (strResult.length() == 0) {
                WgWebapi.logInfo("通信失败!");
                return Result.error("关门失败...");
            } else {
                WgWebapi.logInfo((new ObjectMapper()).readTree(strResult).toPrettyString()); //.toString());
                boolean bvalid = webapi.successIsTrue(strResult);
                if (bvalid) {
                    WgWebapi.logInfo(String.format("%d %s  %s ", controllerSN, op, "成功."));
                    return Result.OK("关门成功，当前状态为" + doorMode);
                } else {
                    WgWebapi.logInfo(String.format("%d %s  %s ", controllerSN, op, "失败..."));
                    return Result.error("关门失败...");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
