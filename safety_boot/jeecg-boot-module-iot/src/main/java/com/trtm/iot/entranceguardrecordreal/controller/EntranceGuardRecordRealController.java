package com.trtm.iot.entranceguardrecordreal.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.trtm.iot.attachlist.entity.Attachlist;
import com.trtm.iot.attachlist.service.IAttachlistService;

import com.trtm.iot.devicetunnelpositiondatareal.entity.DeviceTunnelPositiondataReal;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardType;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardTypeService;
import com.trtm.iot.entranceguardrecordreal.vo.peopleMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.entranceguardrecordreal.entity.EntranceGuardRecordReal;
import com.trtm.iot.entranceguardrecordreal.service.IEntranceGuardRecordRealService;

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
 * @Description: 门禁考勤实时表
 * @Author: jeecg-boot
 * @Date: 2021-09-27
 * @Version: V1.0
 */
@Api(tags = "门禁考勤实时表")
@RestController
@RequestMapping("/entranceguardrecordreal/entranceGuardRecordReal")
@Slf4j
public class EntranceGuardRecordRealController extends JeecgController<EntranceGuardRecordReal, IEntranceGuardRecordRealService> {
    @Autowired
    private IEntranceGuardRecordRealService entranceGuardRecordRealService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IEntranceGuardTypeService entranceGuardTypeService;
    @Autowired
    private IAttachlistService attachlistService;
    /**
     * 济新-门禁考勤
     *
     * @param entranceGuardRecordReal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "济新-门禁考勤")
    @ApiOperation(value = "济新-门禁考勤", notes = "济新-门禁考勤")
    @GetMapping(value = "/queryJiXinList")
    public Result<?> queryJiXinList(EntranceGuardRecordReal entranceGuardRecordReal, EntranceGuardType entranceGuardType,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        List<EntranceGuardRecordReal> entranceGuardRecordReals = entranceGuardRecordRealService.queryJiXinList(entranceGuardRecordReal.getShebeino());
        return Result.OK(entranceGuardRecordReals);
    }
    /**
     * 济新-门禁考勤
     *
     * @param entranceGuardRecordReal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "济新-门禁考勤")
    @ApiOperation(value = "济新-门禁考勤", notes = "济新-门禁考勤")
    @GetMapping(value = "/queryJiXinBanZuList")
    public Result<?> queryJiXinBanZuList(EntranceGuardRecordReal entranceGuardRecordReal, EntranceGuardType entranceGuardType,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        List<EntranceGuardRecordReal> entranceGuardRecordReals = entranceGuardRecordRealService.queryJiXinBanZuList(entranceGuardRecordReal.getShebeino());
        return Result.OK(entranceGuardRecordReals);
    }
    /**
     * 济新门禁考勤-当天出勤人数
     *
     * @param entranceGuardRecordReal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "济新-门禁考勤-当天出勤人数")
    @ApiOperation(value="济新-门禁考勤-当天出勤人数", notes="济新门禁考勤-当天出勤人数")
    @GetMapping(value = "/queryJiXinListAttendance")
    public Result<?> queryJiXinListAttendance(EntranceGuardRecordReal entranceGuardRecordReal,
                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                         HttpServletRequest req) {
        //根据设备区分权限代码
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (shebei != null) {
                entranceGuardRecordReal.setShebeino(shebei);
            }
        }
//        if (deviceTunnelPositiondataReal.getJzwz() != null) {
//            deviceTunnelPositiondataReal.setJzwz("*" + deviceTunnelPositiondataReal.getJzwz() + "*");
//        }
//        if (deviceTunnelPositiondataReal.getJz() != null) {
//            deviceTunnelPositiondataReal.setJz("*" + deviceTunnelPositiondataReal.getJz() + "*");
//        }
        List<EntranceGuardRecordReal> list = entranceGuardRecordRealService.querylistAttendance(entranceGuardRecordReal.getShebeino());
        return Result.OK(list);
    }


    /**
     * 分页列表查询
     *
     * @param entranceGuardRecordReal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-分页列表查询")
    @ApiOperation(value = "门禁考勤实时表-分页列表查询", notes = "门禁考勤实时表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EntranceGuardRecordReal entranceGuardRecordReal, EntranceGuardType entranceGuardType,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecordReal.setDoorid(doorid);
        }
        entranceGuardRecordReal.setNames("*" + entranceGuardRecordReal.getNames() + "*");
        QueryWrapper<EntranceGuardRecordReal> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecordReal, req.getParameterMap());
        Page<EntranceGuardRecordReal> page = new Page<EntranceGuardRecordReal>(pageNo, pageSize);
        IPage<EntranceGuardRecordReal> pageList = entranceGuardRecordRealService.page(page, queryWrapper);
        for (EntranceGuardRecordReal entranceGuardRecordReal1 : page.getRecords()) {
            if (!StrUtil.isBlank(entranceGuardRecordReal1.getPic())) {
                QueryWrapper<Attachlist> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("objectid", entranceGuardRecordReal1.getPic());
                Attachlist attachlist = attachlistService.getOne(queryWrapper1);
                if (attachlist != null) {
                    entranceGuardRecordReal1.setPic("http://web.traiot.cn/docs" + attachlist.getRelativepath());
                } else {
                    entranceGuardRecordReal1.setPic("");
                }
            }
        }
        return Result.OK(pageList);
    }
    /**
     * 根据条件查询列表
     * <p>
     * * @param entranceGuardRecord
     *
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-出勤数-总人数")
    @ApiOperation(value = "门禁考勤表信息-出勤数-总人数", notes = "门禁考勤表信息-出勤数-总人数")
    @GetMapping(value = "/querylistAttendance")
    public Result<?> querylistAttendance(EntranceGuardRecordReal entranceGuardRecordReal, HttpServletRequest req, EntranceGuardType entranceGuardType, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecordReal.setDoorid(doorid);
        }
        //根据doorid查到对应的shebeino
        String doorid1 = entranceGuardRecordReal.getDoorid();
        entranceGuardRecordRealService.list1(doorid1);
        QueryWrapper<EntranceGuardRecordReal> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecordReal, req.getParameterMap());
        queryWrapper.select("count( DISTINCT (Names)) as isopen");
        List<EntranceGuardRecordReal> list = entranceGuardRecordRealService.list(queryWrapper);

        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param entranceGuardRecordReal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-分页列表查询")
    @ApiOperation(value = "门禁考勤实时表-分页列表查询", notes = "门禁考勤实时表-分页列表查询")
    @GetMapping(value = "/list2")
    @PermissionData(pageComponent = "anquan/mjkq/EntranceGuardRecordRealListDingTalk")
    public Result<?> queryPageList2(EntranceGuardRecordReal entranceGuardRecordReal, EntranceGuardType entranceGuardType,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String device = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(device)) {
                entranceGuardRecordReal.setShebeino(device);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        entranceGuardRecordReal.setNames("*" + entranceGuardRecordReal.getNames() + "*");
        QueryWrapper<EntranceGuardRecordReal> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecordReal, req.getParameterMap());
        queryWrapper.orderByDesc("OpenTime");
        Page<EntranceGuardRecordReal> page = new Page<EntranceGuardRecordReal>(pageNo, pageSize);
        IPage<EntranceGuardRecordReal> pageList = entranceGuardRecordRealService.page(page, queryWrapper);
        List<EntranceGuardRecordReal> records = pageList.getRecords();
        if (records != null && records.size() > 0) {
            for (EntranceGuardRecordReal item : records) {
                Date openTime = item.getOpentime();
                if (item.getAdddate() != null && openTime != null) {
                    String openTimeStr = new SimpleDateFormat("yyyy-MM-dd").format(openTime);
                    String endTimeStr = new SimpleDateFormat("yyyy-MM-dd").format(item.getAdddate());
                    if (!openTimeStr.equals(endTimeStr)) {
                        item.setAdddate(null);
                    }else if (item.getAdddate().before(openTime)) {
                        item.setAdddate(null);
                    }
                }
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param entranceGuardRecordReal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-分页列表查询")
    @ApiOperation(value = "门禁考勤实时表-分页列表查询", notes = "门禁考勤实时表-分页列表查询")
    @GetMapping(value = "/list4")
    @PermissionData(pageComponent = "anquan/mjkq/EntranceGuardRecordRealListDingTalk")
    public Result<?> queryPageList4(EntranceGuardRecordReal entranceGuardRecordReal, EntranceGuardType entranceGuardType,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        peopleMessage peopleMessage = new peopleMessage();
        peopleMessage.setName("魏巍");
        peopleMessage.setJob("技术人员");
        peopleMessage.setPic("在岗");
        peopleMessage.setPhone("18658530015");
        peopleMessage peopleMessage1 = new peopleMessage();
        peopleMessage1.setName("张洪伟");
        peopleMessage1.setJob("技术人员");
        peopleMessage1.setPic("在岗");
        peopleMessage1.setPhone("17823147251");
        List<peopleMessage> list = new ArrayList<>();
        list.add(peopleMessage);
        list.add(peopleMessage1);
        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param entranceGuardRecordReal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-分页列表查询")
    @ApiOperation(value = "门禁考勤实时表-分页列表查询", notes = "门禁考勤实时表-分页列表查询")
    @GetMapping(value = "/list3")
    @PermissionData(pageComponent = "anquan/mjkq/EntranceGuardRecordRealListDingTalk")
    public Result<?> queryPageList3(EntranceGuardRecordReal entranceGuardRecordReal, EntranceGuardType entranceGuardType,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        EntranceGuardRecordReal l = entranceGuardRecordRealService.listMaxOpentime(shebeis);
        return Result.OK(l);
    }

    /**
     * 根据条件查询列表
     * <p>
     * * @param entranceGuardRecord
     *
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-list信息")
    @ApiOperation(value = "门禁考勤表信息-list信息", notes = "门禁考勤表信息-list信息")
    @GetMapping(value = "/querylist")
    public Result<?> queryPageList1(EntranceGuardRecordReal entranceGuardRecordReal, EntranceGuardType entranceGuardType, HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecordReal.setDoorid(doorid);
        }
        QueryWrapper<EntranceGuardRecordReal> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecordReal, req.getParameterMap());
        queryWrapper.select("distinct names,recordid,types,openid,opentime,doorid,adminid,pic,adddate,isoffline,capturepic,isopen,serialno,isupload,uploaddate,shebeino,departname");
        List<EntranceGuardRecordReal> list = entranceGuardRecordRealService.list(queryWrapper);
        for (EntranceGuardRecordReal entranceGuardRecordReal1 : list) {
            if (!StrUtil.isBlank(entranceGuardRecordReal1.getPic())) {
                QueryWrapper<Attachlist> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("objectid", entranceGuardRecordReal1.getPic());
                Attachlist attachlist = attachlistService.getOne(queryWrapper1);
                if (attachlist != null) {
                    entranceGuardRecordReal1.setPic("http://web.traiot.cn/docs" + attachlist.getRelativepath());
                } else {
                    entranceGuardRecordReal1.setPic("");
                }
            }
        }
        return Result.OK(list);
    }
    /**
     * 根据条件查询列表
     * <p>
     * * @param entranceGuardRecord
     *
     * @param req
     * @return
     */
    @AutoLog(value = "天神殿门禁考勤表信息-list信息")
    @ApiOperation(value = "天神殿门禁考勤表信息-list信息", notes = "天神殿门禁考勤表信息-list信息")
    @GetMapping(value = "/queryEntranceGuardRecordRealList")
    public Result<?> queryEntranceGuardRecordRealList(EntranceGuardRecordReal entranceGuardRecordReal, EntranceGuardType entranceGuardType, HttpServletRequest req, Integer type) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecordReal.setDoorid(doorid);
        }

        String opentime_begin = req.getParameter("opentime_begin");
        //Date parse = SimpleDateFormat.parse(opentime_begin);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = formatter.parse(opentime_begin);

        List<EntranceGuardRecordReal> EntranceGuardRecordRealList = entranceGuardRecordRealService.getEntranceGuardRecordRealList(entranceGuardRecordReal.getShebeino(), parse);
        for (EntranceGuardRecordReal guardRecordReal : EntranceGuardRecordRealList) {
            if (!StrUtil.isBlank(guardRecordReal.getPic())) {
                QueryWrapper<Attachlist> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("objectid", guardRecordReal.getPic());
                Attachlist attachlist = attachlistService.getOne(queryWrapper1);
                if (attachlist != null) {
                    guardRecordReal.setPic("http://web.traiot.cn/docs" + attachlist.getRelativepath());
                } else {
                    guardRecordReal.setPic("");
                }
            }
        }

        return Result.OK(EntranceGuardRecordRealList);
    }




    /**
     * 根据条件查询列表
     * <p>
     * * @param entranceGuardRecord
     *
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-list信息")
    @ApiOperation(value = "门禁考勤表信息-list信息", notes = "门禁考勤表信息-list信息（姓名去重）")
    @GetMapping(value = "/querylist1")
    public Result<?> queryPageList2(EntranceGuardRecordReal entranceGuardRecordReal, EntranceGuardType entranceGuardType, HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecordReal.setDoorid(doorid);
        }
        QueryWrapper<EntranceGuardRecordReal> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecordReal, req.getParameterMap());
        queryWrapper.select("names,recordid,types,openid,opentime,doorid,adminid,pic,adddate,isoffline,capturepic,isopen,serialno,isupload,uploaddate,shebeino,departname");
        queryWrapper.isNotNull("Names");
        queryWrapper.last("and opentime in (select max(opentime) from entrance_guard_record_real group by Names)  group by Names");
        List<EntranceGuardRecordReal> list = entranceGuardRecordRealService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 根据条件查询列表
     * <p>
     * * @param entranceGuardRecord
     *
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-班组")
    @ApiOperation(value = "门禁考勤表信息-班组", notes = "门禁考勤表信息-班组")
    @GetMapping(value = "/querylistDepart")
    public Result<?> querylistDepart(EntranceGuardRecordReal entranceGuardRecordReal, HttpServletRequest req, EntranceGuardType entranceGuardType, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecordReal.setDoorid(doorid);
        }
        QueryWrapper<EntranceGuardRecordReal> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecordReal, req.getParameterMap());
        queryWrapper.select("count( DISTINCT (Names)) as isopen", "DepartName");
        queryWrapper.last("GROUP BY DepartName");
        List<EntranceGuardRecordReal> list = entranceGuardRecordRealService.list(queryWrapper);
        return Result.OK(list);
    }
    /**
     * 根据条件查询列表
     * <p>
     * * @param entranceGuardRecord
     *
     * @param req
     * @return
     */
    @AutoLog(value = "天神殿门禁考勤表信息-班组")
    @ApiOperation(value = "天神殿门禁考勤表信息-班组", notes = "天神殿门禁考勤表信息-班组")
    @GetMapping(value = "/queryTianShenDianlistDepart")
    public Result<?> queryTianShenDianlistDepart(EntranceGuardRecordReal entranceGuardRecordReal, HttpServletRequest req, EntranceGuardType entranceGuardType, Integer type) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecordReal.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecordReal.setShebeino(shebei);
            } else {
                entranceGuardRecordReal.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecordReal.setDoorid(doorid);
        }

        String opentime_begin = req.getParameter("opentime_begin");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = formatter.parse(opentime_begin);
        List<EntranceGuardRecordReal> banZu = entranceGuardRecordRealService.getBanZu(entranceGuardRecordReal.getShebeino(),parse);
        return Result.OK(banZu);
    }

    /**
     * 添加
     *
     * @param entranceGuardRecordReal
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-添加")
    @ApiOperation(value = "门禁考勤实时表-添加", notes = "门禁考勤实时表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody EntranceGuardRecordReal entranceGuardRecordReal) {
        entranceGuardRecordRealService.save(entranceGuardRecordReal);
        return Result.OK("添加成功！");
    }
    /**
     * 添加(对外)
     *
     * @param entranceGuardRecordReal
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-添加")
    @ApiOperation(value = "门禁考勤实时表-添加", notes = "门禁考勤实时表-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody EntranceGuardRecordReal entranceGuardRecordReal) {
        entranceGuardRecordRealService.save(entranceGuardRecordReal);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entranceGuardRecordReal
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-编辑")
    @ApiOperation(value = "门禁考勤实时表-编辑", notes = "门禁考勤实时表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EntranceGuardRecordReal entranceGuardRecordReal) {
        entranceGuardRecordRealService.updateById(entranceGuardRecordReal);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-通过id删除")
    @ApiOperation(value = "门禁考勤实时表-通过id删除", notes = "门禁考勤实时表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        entranceGuardRecordRealService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-批量删除")
    @ApiOperation(value = "门禁考勤实时表-批量删除", notes = "门禁考勤实时表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.entranceGuardRecordRealService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "门禁考勤实时表-通过id查询")
    @ApiOperation(value = "门禁考勤实时表-通过id查询", notes = "门禁考勤实时表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        EntranceGuardRecordReal entranceGuardRecordReal = entranceGuardRecordRealService.getById(id);
        if (entranceGuardRecordReal == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(entranceGuardRecordReal);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param entranceGuardRecordReal
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EntranceGuardRecordReal entranceGuardRecordReal) {
        return super.exportXls(request, entranceGuardRecordReal, EntranceGuardRecordReal.class, "门禁考勤实时表");
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
        return super.importExcel(request, response, EntranceGuardRecordReal.class);
    }

}
