package com.trtm.iot.car.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.bhzrenwudancar.entity.BhzrenwudanCar;
import com.trtm.iot.bhzrenwudancar.entity.BhzrenwudanCarVO;
import com.trtm.iot.bhzrenwudancar.entity.Bhzrenwudangxkh;
import com.trtm.iot.car.entity.GetCarDetailVo;
import com.trtm.iot.car.entity.SchedulingCardcVo;
import com.trtm.iot.car.vo.SchedulingCarVO;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.service.IRenwudanScheduleService;
import com.trtm.iot.suchingcarpeiz.entity.SuchingCarpeiz;
import com.trtm.iot.suchingcarpeiz.service.ISuchingCarpeizService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import com.trtm.iot.ztbhzgonxutime.entity.ZtBhzGonxutime;
import com.trtm.iot.ztbhzjckh.entity.ZtBhzjckh;
import com.trtm.iot.ztbhzkhdf.entity.ZtBhzkhdf;
import com.trtm.iot.ztbhzkhdf.entity.ZtBhzkhdfVO;
import com.trtm.iot.ztbhzpeisongtime.entity.ZtBhzPeisongtime;
import com.trtm.iot.ztbhzpeisongtime.service.IZtBhzPeisongtimeService;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import net.sf.saxon.functions.Floor;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.service.ISchedulingCarService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 调度车辆
 * @Author: jeecg-boot
 * @Date: 2021-05-20
 * @Version: V1.0
 */
@Api(tags = "调度车辆")
@RestController
@RequestMapping("/car/schedulingCar")
@Slf4j
public class SchedulingCarController extends JeecgController<SchedulingCar, ISchedulingCarService> {
    @Autowired
    private ISchedulingCarService schedulingCarService;
    @Autowired
    private IRenwudanScheduleService scheduleService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzrenwudanService taskService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IZtBhzPeisongtimeService bhzPeisongtimeService;

    @Autowired
    private IBhzPhoneService bhzPhoneService;

    @Autowired
    private ISuchingCarpeizService suchingCarpeizService;

    /**
     * 分页列表查询
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "调度车辆-分页列表查询")
    @ApiOperation(value = "调度车辆-分页列表查询", notes = "调度车辆-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "car/SchedulingCarList")
    public Result<?> queryPageList(SchedulingCar schedulingCar,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_orgcode) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            schedulingCar.setSysOrgCode(sys_depart_orgcode + "*");
        }
        schedulingCar.setDriver("*" + schedulingCar.getDriver() + "*");
        QueryWrapper<SchedulingCar> queryWrapper = QueryGenerator.initQueryWrapper(schedulingCar, req.getParameterMap());
        Page<SchedulingCar> page = new Page<SchedulingCar>(pageNo, pageSize);
        IPage<SchedulingCar> pageList = schedulingCarService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
    @AutoLog(value = "调度车辆-分页列表查询")
    @ApiOperation(value = "调度车辆-分页列表查询", notes = "调度车辆-分页列表查询")
    @GetMapping(value = "/list47")
    public Result<?> queryPageList47(SchedulingCar schedulingCar,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_orgcode) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            schedulingCar.setSysOrgCode(sys_depart_orgcode + "*");
        }
        schedulingCar.setDriver("*" + schedulingCar.getDriver() + "*");
        QueryWrapper<SchedulingCar> queryWrapper = QueryGenerator.initQueryWrapper(schedulingCar, req.getParameterMap());
        Page<SchedulingCar> page = new Page<SchedulingCar>(pageNo, pageSize);
        IPage<SchedulingCar> pageList = schedulingCarService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 十五局浇筑令发车详情统计单打印及导出
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listjzl")
    public Result<?> queryPagelistjzl(SchedulingCar schedulingCar,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req, String sys_depart_orgcode) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            schedulingCar.setSysOrgCode(sys_depart_orgcode + "*");
        }
        schedulingCar.setDriver("*" + schedulingCar.getDriver() + "*");
        QueryWrapper<SchedulingCar> queryWrapper = QueryGenerator.initQueryWrapper(schedulingCar, req.getParameterMap());
        Page<SchedulingCar> page = new Page<>(pageNo, pageSize);
        Page<SchedulingCar> page1 = schedulingCarService.page(page, queryWrapper);
        List<SchedulingCar> data = page1.getRecords();
        if (data.size() > 0) {
            for (SchedulingCar record : data) {
                System.out.println(record.getCode());
                String orgcode = sys_depart_orgcode +"%";
                List<Bhzrenwudan> bhzrenwudan1 = bhzrenwudanService.selectrenwudanone(record.getCode(), record.getStation());
                Map<String,Object> map = scheduleService.selectmetes(record.getCode(), record.getStation(),orgcode);
                if (bhzrenwudan1.size() > 0) {
                    Bhzrenwudan bhzrenwudan = bhzrenwudan1.get(0);
                    record.setMorrec(bhzrenwudan.getBetlev());//强度等级
                    record.setMormete(bhzrenwudan.getMete());//计划方量
                    record.setFlag(bhzrenwudan.getProjname());//工程名称
                    record.setNote(bhzrenwudan.getConspos());//施工部位
                    if (map != null) {
                        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione((String) map.get("shebei_no"));
                        record.setTransportVolume((Double) map.get("Metes"));//生产方量
                        record.setCode(selectshebeione.getSbname());//搅拌机号
                    }
                }else {
                    if (map != null){
                        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione((String) map.get("shebei_no"));
                        record.setMorrec((String) map.get("BetLev"));//强度等级
                        record.setMormete((Double) map.get("Mete"));//计划方量
                        record.setFlag((String) map.get("Projectname"));//工程名称
                        record.setNote((String) map.get("ConsPos"));//施工部位
                        record.setTransportVolume((Double) map.get("Metes"));//生产方量
                        record.setCode(selectshebeione.getSbname());//搅拌机号
                    }else {
                        Map<String,Object> map2 = new HashMap<>();
                        List<RenwudanSchedule>  renwudanSchedules = scheduleService.selectmetesd(record.getCode(),orgcode);
                        if (renwudanSchedules.size() > 0){
                            RenwudanSchedule renwudanSchedule = renwudanSchedules.get(0);
                            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(renwudanSchedule.getShebeiNo());
                            map2.put("sys_org_code",renwudanSchedule.getSysOrgCode());
                            map2.put("Metes",renwudanSchedule.getMetes());
                            map2.put("BetLev",renwudanSchedule.getBetlev());
                            map2.put("Mete",renwudanSchedule.getMete());
                            map2.put("Projectname",renwudanSchedule.getProjectname());
                            map2.put("ConsPos",renwudanSchedule.getConspos());
                            record.setMorrec((String) map2.get("BetLev"));//强度等级
                            record.setMormete((Double) map2.get("Mete"));//计划方量
                            record.setFlag((String) map2.get("Projectname"));//工程名称
                            record.setNote((String) map2.get("ConsPos"));//施工部位
                            record.setTransportVolume((Double) map2.get("Metes"));//生产方量
                            record.setCode(selectshebeione.getSbname());//搅拌机号
                        }
                    }
                }
            }
        }
        return Result.OK(page1);
    }



    /**
     * 十五局浇筑令发车详情统计单打印及导出
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listjzlxq")
    public Result<?> queryPagelistjzlxq(SchedulingCar schedulingCar,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req, String sys_depart_orgcode) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            schedulingCar.setSysOrgCode(sys_depart_orgcode + "*");
        }
        schedulingCar.setDriver("*" + schedulingCar.getDriver() + "*");
        QueryWrapper<SchedulingCar> queryWrapper = QueryGenerator.initQueryWrapper(schedulingCar, req.getParameterMap());
        List<SchedulingCar> data1 = schedulingCarService.list(queryWrapper);
        List<SchedulingCardcVo> data = new ArrayList<>();
        if (data1.size() > 0) {
            for (SchedulingCar record : data1) {
                SchedulingCardcVo schedulingCardcVo = new SchedulingCardcVo();
                schedulingCardcVo.setShuini(0.0);//水泥
                schedulingCardcVo.setFmh(0.0);//粉煤灰
                schedulingCardcVo.setSa(0.0);//机制砂
                schedulingCardcVo.setSso(0.0);//碎石1
                schedulingCardcVo.setSst(0.0);//碎石2
                schedulingCardcVo.setSsf(0.0);//碎石3
                schedulingCardcVo.setWjj(0.0);//外加剂
                schedulingCardcVo.setSlj(0.0);//速凝剂
                schedulingCardcVo.setWater(0.0);//水
                schedulingCardcVo.setShuini1(0.0);//水泥
                schedulingCardcVo.setFmh1(0.0);//粉煤灰
                schedulingCardcVo.setSa1(0.0);//机制砂
                schedulingCardcVo.setSso1(0.0);//碎石1
                schedulingCardcVo.setSst1(0.0);//碎石2
                schedulingCardcVo.setSsf1(0.0);//碎石3
                schedulingCardcVo.setWjj1(0.0);//外加剂
                schedulingCardcVo.setSlj1(0.0);//速凝剂
                schedulingCardcVo.setWater1(0.0);//水
                schedulingCardcVo.setBumate(0);
                schedulingCardcVo.setBuyunju(0.0);

                String orgcode = sys_depart_orgcode +"%";
                Map<String,Object> map = new HashMap<>();
                Map<String,Object> map1 = null;
                try {
                    map1 = scheduleService.selectmetes(record.getCode(), record.getStation(),orgcode);
                } catch (Exception e) {
                    System.out.println(record.getCode()+"|"+record.getStation()+"|"+orgcode);
                    e.printStackTrace();
                }
                if (map1 != null){
                    map = map1;
                }else {
                    Map<String,Object> map2 = new HashMap<>();
                    List<RenwudanSchedule>  renwudanSchedules = scheduleService.selectmetesd(record.getCode(),orgcode);
                    if (renwudanSchedules.size() > 0){
                        RenwudanSchedule renwudanSchedule = renwudanSchedules.get(0);
                        map2.put("sys_org_code",renwudanSchedule.getSysOrgCode());
                        map2.put("Metes",renwudanSchedule.getMetes());
                        map2.put("BetLev",renwudanSchedule.getBetlev());
                        map2.put("Mete",renwudanSchedule.getMete());
                        map2.put("Projectname",renwudanSchedule.getProjectname());
                        map2.put("ConsPos",renwudanSchedule.getConspos());
                        map2.put("shebei_no",renwudanSchedule.getShebeiNo());
                    }
                    map = map2;
                }
                if (map.size() == 0){
                    continue;
                }
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione((String) map.get("shebei_no"));
                record.setTransportVolume((Double) map.get("Metes"));//生产方量
                schedulingCardcVo.setYunju(0.0);
                List<Bhzrenwudan> bhzrenwudan1 = bhzrenwudanService.selectrenwudanone(record.getCode(), record.getStation());
                if (bhzrenwudan1.size() > 0) {
                    Bhzrenwudan bhzrenwudan = bhzrenwudan1.get(0);
                    record.setMorrec(bhzrenwudan.getBetlev());//强度等级
                    record.setMormete(bhzrenwudan.getMete());//计划方量
                    record.setFlag(bhzrenwudan.getProjname());//工程名称
                    record.setNote(bhzrenwudan.getConspos());//施工部位
                    if (bhzrenwudan.getConspos().contains("临建")&&!bhzrenwudan.getBetlev().contains("喷射")){
                        schedulingCardcVo.setXinzhen("普通混凝土");
                    }else {
                        if (bhzrenwudan.getBetlev().contains("喷射")){
                            schedulingCardcVo.setXinzhen("喷射混凝土");
                        }else {
                            if (bhzrenwudan.getConspos().contains("水沟")||bhzrenwudan.getConspos().contains("挡墙")||bhzrenwudan.getConspos().contains("框架")){
                                schedulingCardcVo.setXinzhen("附属砼");
                            }else if (!bhzrenwudan.getBetlev().contains("喷射")&&!bhzrenwudan.getConspos().contains("CFG")){
                                schedulingCardcVo.setXinzhen("普通混凝土");
                            }else if (!bhzrenwudan.getBetlev().contains("喷射")&&bhzrenwudan.getConspos().contains("CFG")){
                                schedulingCardcVo.setXinzhen("地基处理混凝土");
                            }else {
                                schedulingCardcVo.setXinzhen("喷射混凝土");
                            }
                        }
                    }
//                    schedulingCardcVo.setKehu(bhzrenwudan.getCustomer());//客户名称
//                    System.out.println(bhzrenwudan.getCustomer());
//                    if (bhzrenwudan.getCustomer() != null && bhzrenwudan.getCustomer().length() > 3){
//                        schedulingCardcVo.setGonq(bhzrenwudan.getCustomer().substring(0,3));//工区
//                    }else {
//                        schedulingCardcVo.setGonq("--");//工区
//                    }
                    record.setLineName(bhzrenwudan.getCustomer());//客户名称
                    schedulingCardcVo.setKehu(record.getLineName());//客户名称
                    System.out.println(record.getLineName());
                    if (record.getLineName() != null && record.getLineName().length() >= 3){
                        schedulingCardcVo.setGonq(record.getLineName().substring(0,3));//工区
                    }else {
                        schedulingCardcVo.setGonq("--");//工区
                    }
                    schedulingCardcVo.setYunju(bhzrenwudan.getDistance());//运输距离
                }else {
                    record.setMorrec((String) map.get("BetLev"));//强度等级
                    record.setMormete((Double) map.get("Mete"));//计划方量
                    record.setFlag((String) map.get("Projectname"));//工程名称
                    record.setNote((String) map.get("ConsPos"));//施工部位
                    System.out.println(map);
                    if (((String) map.get("ConsPos")).contains("临建")&&!((String) map.get("BetLev")).contains("喷射")){
                        schedulingCardcVo.setXinzhen("普通混凝土");
                    }else {
                        if (((String) map.get("BetLev")).contains("喷射")){
                            schedulingCardcVo.setXinzhen("喷射混凝土");
                        }else {
                            if (((String) map.get("ConsPos")).contains("水沟")|| ((String) map.get("ConsPos")).contains("挡墙")|| ((String) map.get("ConsPos")).contains("框架")){
                                schedulingCardcVo.setXinzhen("附属砼");
                            }else if (!((String) map.get("ConsPos")).contains("喷射")&&!((String) map.get("ConsPos")).contains("CFG")){
                                schedulingCardcVo.setXinzhen("普通混凝土");
                            }else if (!((String) map.get("ConsPos")).contains("喷射")&& ((String) map.get("ConsPos")).contains("CFG")){
                                schedulingCardcVo.setXinzhen("地基处理混凝土");
                            }else {
                                schedulingCardcVo.setXinzhen("喷射混凝土");
                            }
                        }
                    }
                    schedulingCardcVo.setKehu(record.getLineName());//客户名称
                    System.out.println(record.getLineName());
                    if (record.getLineName() != null && record.getLineName().length() >= 3){
                        schedulingCardcVo.setGonq(record.getLineName().substring(0,3));//工区
                    }else {
                        schedulingCardcVo.setGonq("--");//工区
                    }
                }
                //copy
                BeanUtils.copyProperties(record, schedulingCardcVo);
                if (schedulingCardcVo.getYunju() == 0.0){
                    schedulingCardcVo.setYunju((double)record.getYunju());//运输距离
                }
                if (selectshebeione != null) {
                    schedulingCardcVo.setShebei(selectshebeione.getSbname());//搅拌机号
                }
                List<Shigongphb> shigongphb1 = shigongphbService.queryoneRenwudanbyStation(record.getCode(),record.getRecipe(),record.getStation());
                if (shigongphb1.size() > 0) {
                    Shigongphb shigongphb = shigongphb1.get(0);
                    if (shigongphb.getRu1() != null) {
                        schedulingCardcVo.setShuini(shigongphb.getRu1());//水泥
                        schedulingCardcVo.setShuini1(Double.parseDouble(String.format("%.2f",(shigongphb.getRu1() * schedulingCardcVo.getProdmete()/1000))));//水泥
                    }
                    if (shigongphb.getRu2() != null) {
                        schedulingCardcVo.setFmh(shigongphb.getRu2());//粉煤灰
                        schedulingCardcVo.setFmh1(Double.parseDouble(String.format("%.2f",(schedulingCardcVo.getFmh() * schedulingCardcVo.getProdmete()/1000))));//粉煤灰
                    }
                    if (shigongphb.getRu4() != null) {
                        schedulingCardcVo.setSa(shigongphb.getRu4());//机制砂
                        schedulingCardcVo.setSa1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSa() * schedulingCardcVo.getProdmete()/1000)));//机制砂
                    }
                    if (shigongphb.getRu5() != null) {
                        schedulingCardcVo.setSso(shigongphb.getRu5());//碎石1
                        schedulingCardcVo.setSso1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSso() * schedulingCardcVo.getProdmete()/1000)));//碎石1
                    }
                    if (shigongphb.getRu6() != null) {
                        schedulingCardcVo.setSst(shigongphb.getRu6());//碎石2
                        schedulingCardcVo.setSst1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSst() * schedulingCardcVo.getProdmete()/1000)));//碎石2
                    }
                    if (shigongphb.getRu7() != null) {
                        schedulingCardcVo.setSsf(shigongphb.getRu7());//碎石3
                        schedulingCardcVo.setSsf1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSsf() * schedulingCardcVo.getProdmete()/1000)));//碎石3
                    }
                    if (shigongphb.getRu8() != null) {
                        schedulingCardcVo.setWjj(shigongphb.getRu8());//外加剂
                        schedulingCardcVo.setWjj1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getWjj() * schedulingCardcVo.getProdmete()/1000)));//外加剂
                    }
                    if (shigongphb.getRu9() != null) {
                        schedulingCardcVo.setSlj(shigongphb.getRu9());//速凝剂
                        schedulingCardcVo.setSlj1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSlj() * schedulingCardcVo.getProdmete()/1000)));//速凝剂
                    }
                    if (shigongphb.getRu11() != null) {
                        schedulingCardcVo.setWater(shigongphb.getRu11());//水prodmete
                        schedulingCardcVo.setWater1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getWater() * schedulingCardcVo.getProdmete()/1000)));//水
                    }
                    if (schedulingCardcVo.getTotmete() < 6) {
                        schedulingCardcVo.setBumate((int) (6 - schedulingCardcVo.getTotmete()));//补方量
                    }
                    if (schedulingCardcVo.getYunju() > 5) {
                        schedulingCardcVo.setBuyunju((schedulingCardcVo.getYunju() - 5));//补运距
                    }
                }

                schedulingCardcVo.setBuyunjugcl((int) (schedulingCardcVo.getYunju() * schedulingCardcVo.getTotmete()));//补运距工程两
                data.add(schedulingCardcVo);
            }
        }
        return Result.OKs(data);
    }

//    /** todo
//     * 十五局浇筑令发车详情统计单打印及导出
//     *
//     * @param schedulingCar
//     * @param pageNo
//     * @param pageSize
//     * @param req
//     * @return
//     */
//    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
//    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
//    @GetMapping(value = "/listjzlxqg")
//    public Result<?> queryPagelistjzlxqg(SchedulingCar schedulingCar,
//                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//                                        HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) {
//        if (sys_depart_orgcode != null){
//            sys_depart_orgcode = sys_depart_orgcode +"%";
//        }
//        List<SchedulingCardcVo> schedulingCard = schedulingCarService.selectallziduan(sys_depart_orgcode,dattim_begin,dattim_end);
//        if (data1.size() > 0) {
//            for (SchedulingCar record : data1) {
//                SchedulingCardcVo schedulingCardcVo = new SchedulingCardcVo();
//                schedulingCardcVo.setShuini(0.0);//水泥
//                schedulingCardcVo.setFmh(0.0);//粉煤灰
//                schedulingCardcVo.setSa(0.0);//机制砂
//                schedulingCardcVo.setSso(0.0);//碎石1
//                schedulingCardcVo.setSst(0.0);//碎石2
//                schedulingCardcVo.setSsf(0.0);//碎石3
//                schedulingCardcVo.setWjj(0.0);//外加剂
//                schedulingCardcVo.setSlj(0.0);//速凝剂
//                schedulingCardcVo.setWater(0.0);//水
//                schedulingCardcVo.setShuini1(0.0);//水泥
//                schedulingCardcVo.setFmh1(0.0);//粉煤灰
//                schedulingCardcVo.setSa1(0.0);//机制砂
//                schedulingCardcVo.setSso1(0.0);//碎石1
//                schedulingCardcVo.setSst1(0.0);//碎石2
//                schedulingCardcVo.setSsf1(0.0);//碎石3
//                schedulingCardcVo.setWjj1(0.0);//外加剂
//                schedulingCardcVo.setSlj1(0.0);//速凝剂
//                schedulingCardcVo.setWater1(0.0);//水
//                schedulingCardcVo.setBumate(0);
//                schedulingCardcVo.setBuyunju(0.0);
//
//                Map<String,Object> map = scheduleService.selectmetes(record.getCode(), record.getStation());
//                if (map != null) {
//                    ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione((String) map.get("shebei_no"));
//                    record.setTransportVolume((Double) map.get("Metes"));//生产方量
//                    schedulingCardcVo.setYunju(0.0);
//                    Bhzrenwudan bhzrenwudan = bhzrenwudanService.selectrenwudanone(record.getCode(), record.getStation());
//                    if (bhzrenwudan != null) {
//                        record.setMorrec(bhzrenwudan.getBetlev());//强度等级
//                        record.setMormete(bhzrenwudan.getMete());//计划方量
//                        record.setFlag(bhzrenwudan.getProjname());//工程名称
//                        record.setNote(bhzrenwudan.getConspos());//施工部位
//                        if (bhzrenwudan.getConspos().contains("水沟")||bhzrenwudan.getConspos().contains("挡墙")||bhzrenwudan.getConspos().contains("框架")){
//                            schedulingCardcVo.setXinzhen("附属砼");
//                        }else if (!bhzrenwudan.getBetlev().contains("喷射")&&!bhzrenwudan.getConspos().contains("xfg")){
//                            schedulingCardcVo.setXinzhen("普通混凝土");
//                        }else if (!bhzrenwudan.getBetlev().contains("喷射")&&bhzrenwudan.getConspos().contains("xfg")){
//                            schedulingCardcVo.setXinzhen("地基处理混凝土");
//                        }else {
//                            schedulingCardcVo.setXinzhen("喷射混凝土");
//                        }
//                        schedulingCardcVo.setKehu(bhzrenwudan.getCustomer());//客户名称
//                        if (bhzrenwudan.getCustomer() != null && bhzrenwudan.getCustomer().length() > 3){
//                            schedulingCardcVo.setGonq(bhzrenwudan.getCustomer().substring(0,3));//工区
//                        }else {
//                            schedulingCardcVo.setGonq("--");//工区
//                        }
//                        schedulingCardcVo.setYunju(bhzrenwudan.getDistance());//运输距离
//                    }
//                    //copy
//                    BeanUtils.copyProperties(record, schedulingCardcVo);
//                    if (selectshebeione != null) {
//                        schedulingCardcVo.setShebei(selectshebeione.getSbname());//搅拌机号
//                    }
//                    Shigongphb shigongphb = shigongphbService.queryoneRenwudanbyStation(record.getCode(),record.getRecipe(),record.getStation());
//                    if (shigongphb != null) {
//                        schedulingCardcVo.setRecipe1(shigongphb.getCode1());
//                        if (shigongphb.getRu1() != null) {
//                            schedulingCardcVo.setShuini(shigongphb.getRu1());//水泥
//                            schedulingCardcVo.setShuini1(Double.parseDouble(String.format("%.2f",(shigongphb.getRu1() * schedulingCardcVo.getTotmete()/1000))));//水泥
//                        }
//                        if (shigongphb.getRu2() != null) {
//                            schedulingCardcVo.setFmh(shigongphb.getRu2());//粉煤灰
//                            schedulingCardcVo.setFmh1(Double.parseDouble(String.format("%.2f",(schedulingCardcVo.getFmh() * schedulingCardcVo.getTotmete()/1000))));//粉煤灰
//                        }
//                        if (shigongphb.getRu4() != null) {
//                            schedulingCardcVo.setSa(shigongphb.getRu4());//机制砂
//                            schedulingCardcVo.setSa1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSa() * schedulingCardcVo.getTotmete()/1000)));//机制砂
//                        }
//                        if (shigongphb.getRu5() != null) {
//                            schedulingCardcVo.setSso(shigongphb.getRu5());//碎石1
//                            schedulingCardcVo.setSso1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSso() * schedulingCardcVo.getTotmete()/1000)));//碎石1
//                        }
//                        if (shigongphb.getRu6() != null) {
//                            schedulingCardcVo.setSst(shigongphb.getRu6());//碎石2
//                            schedulingCardcVo.setSst1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSst() * schedulingCardcVo.getTotmete()/1000)));//碎石2
//                        }
//                        if (shigongphb.getRu7() != null) {
//                            schedulingCardcVo.setSsf(shigongphb.getRu7());//碎石3
//                            schedulingCardcVo.setSsf1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSsf() * schedulingCardcVo.getTotmete()/1000)));//碎石3
//                        }
//                        if (shigongphb.getRu8() != null) {
//                            schedulingCardcVo.setWjj(shigongphb.getRu8());//外加剂
//                            schedulingCardcVo.setWjj1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getWjj() * schedulingCardcVo.getTotmete()/1000)));//外加剂
//                        }
//                        if (shigongphb.getRu9() != null) {
//                            schedulingCardcVo.setSlj(shigongphb.getRu9());//速凝剂
//                            schedulingCardcVo.setSlj1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getSlj() * schedulingCardcVo.getTotmete()/1000)));//速凝剂
//                        }
//                        if (shigongphb.getRu11() != null) {
//                            schedulingCardcVo.setWater(shigongphb.getRu11());//水
//                            schedulingCardcVo.setWater1(Double.parseDouble(String.format("%.2f",schedulingCardcVo.getWater() * schedulingCardcVo.getTotmete()/1000)));//水
//                        }
//                        if (schedulingCardcVo.getTotmete() < 6) {
//                            schedulingCardcVo.setBumate((int) (6 - schedulingCardcVo.getTotmete()));//补方量
//                        }
//                        if (schedulingCardcVo.getYunju() > 5) {
//                            schedulingCardcVo.setBuyunju((schedulingCardcVo.getYunju() - 5));//补运距
//                        }
//                    }
//                    schedulingCardcVo.setBuyunjugcl((int) (schedulingCardcVo.getYunju() * schedulingCardcVo.getTotmete()));//补运距工程两
//                }
//                data.add(schedulingCardcVo);
//            }
//        }
//        return Result.OKs(data);
//    }

    /**
     * 中铁十五局浇筑令发车详情统计单打印及导出
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listyaf")
    public Result<?> queryPagelistyaf(SchedulingCar schedulingCar,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            sys_depart_orgcode = sys_depart_orgcode +"%";
        }
        Integer pageno = (pageNo-1)*pageSize;
        if(dattim_begin == null && dattim_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            dattim_begin = f.format(calendar.getTime());
            dattim_end = f.format(date.getTime());
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy MMdd hhmmss");
        Page<BhzrenwudanCar> page = new Page<>(pageNo, pageSize);
        Page<BhzrenwudanCar>  pageList = schedulingCarService.selecerenwudan(page,sys_depart_orgcode,dattim_begin,dattim_end,pageSize,pageno);
        List<BhzrenwudanCar> records = pageList.getRecords();
        if (records.size() > 0){
            for (BhzrenwudanCar record:records){
                if (record.getConspos() != null){
                    String conspos = record.getConspos();
                    //单位工程
                    int i = conspos.indexOf("|");
                    String substring1 = conspos.substring(i+1);
                    int j = substring1.indexOf("|");
                    String substring2 = substring1.substring(0,j);
                    record.setProjname(substring2);
                    //分部工程
                    String substring = substring1.substring(j+1);
                    int j1 = substring.indexOf("|");
                    String substring3 = substring.substring(0,j1);
                    record.setConspos(substring3);
                }
                record.setDattimShij(record.getDattim());
                record.setUpdateBy("混凝土");
                if(record.getErpid() != 0){
                    Date dattim = record.getDattim();
                    String format = dateFormat.format(dattim);
                    String substring = format.substring(0, 9);
                    record.setDanhao(substring +" "+record.getErpid());
                }else {
                    record.setDanhao("0");
                }
            }
        }
        return Result.OK(pageList);
    }

    /**
     * APP
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listyafapp")
    public Result<?> queryPagelistyafapp(SchedulingCar schedulingCar,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            schedulingCar.setSysOrgCode(sys_depart_orgcode + "*");
        }
        schedulingCar.setDriver("*" + schedulingCar.getDriver() + "*");
        QueryWrapper<SchedulingCar> queryWrapper = QueryGenerator.initQueryWrapper(schedulingCar, req.getParameterMap());
        Page<SchedulingCar> page = new Page<SchedulingCar>(pageNo, pageSize);
        IPage<SchedulingCar> pageList = schedulingCarService.page(page, queryWrapper);
        IPage<BhzrenwudanCar> objectIPage = new Page<BhzrenwudanCar>();
        List<SchedulingCar> records1 = pageList.getRecords();
        ArrayList<BhzrenwudanCar> list = new ArrayList<>();
        if (records1.size() > 0){
            for (SchedulingCar records :records1){
                BhzrenwudanCar bhzrenwudanCar = new BhzrenwudanCar();

                List<Bhzrenwudan> bhzrenwudan1 = bhzrenwudanService.selectrenwudanone(records.getCode(), records.getStation());
                if (bhzrenwudan1.size() > 0){
                    Bhzrenwudan bhzrenwudan = bhzrenwudan1.get(0);
                    BeanUtils.copyProperties(bhzrenwudan, bhzrenwudanCar);
                }
                BeanUtils.copyProperties(records, bhzrenwudanCar);
                bhzrenwudanCar.setBegtim(records.getDattim());
                list.add(bhzrenwudanCar);
            }
        }
        objectIPage.setRecords(list);
        return Result.OK(objectIPage);
    }
    /**
     * 中铁十五局浇筑令发车详情统计单打印及导出
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listyafcph")
    public Result<?> queryPagelistyafcph(SchedulingCar schedulingCar,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req, String vehicle, String sys_depart_orgcode) {
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("sys_org_code",sys_depart_orgcode);
        queryWrapper.eq("sbname",vehicle);
        ShebeiInfo one = shebeiInfoService.getOne(queryWrapper);
        return Result.OK(one);
    }
    /**
     * 中铁十五局浇筑令发车详情统计单打印及导出
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listyafdc")
    public Result<?> queryPagelistyafdc(SchedulingCar schedulingCar,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) throws ParseException {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            sys_depart_orgcode = sys_depart_orgcode +"%";
        }
        if(dattim_begin == null && dattim_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            dattim_begin = f.format(calendar.getTime());
            dattim_end = f.format(date.getTime());
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy MMdd hhmmss");
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df1=new SimpleDateFormat("yyyy年MM月dd日");
        List<BhzrenwudanCarVO>  pageList = schedulingCarService.selecerenwudandc(sys_depart_orgcode,dattim_begin,dattim_end);
        if (pageList.size() > 0){
            for (BhzrenwudanCarVO record:pageList){
                record.setDattimShij(record.getDattim());
                record.setUpdateBy("混凝土");
                record.setDattim_begin(df.parse(dattim_begin));
                record.setDattim_end(df.parse(dattim_end));
                String format = df1.format(record.getDattim_begin());
                String format1 = df1.format(record.getDattim_end());
                record.setTimepj(format+"至"+format1);
                if(record.getErpid() != 0){
                    Date dattim = record.getDattim();
                    String format4 = dateFormat.format(dattim);
                    String substring = format4.substring(0, 9);
                    record.setDanhao(substring +" "+record.getErpid());
                }else {
                    record.setDanhao("0");
                }
            }
        }
        return Result.OKs(pageList);
    }

    /**
     * 中铁一局配送考核
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listztps")
    public Result<?> queryPagelistztps(SchedulingCar schedulingCar,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) throws ParseException {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            sys_depart_orgcode = sys_depart_orgcode +"%";
        }
        //初始查询本月的
        if(dattim_begin == null && dattim_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            dattim_begin = f.format(calendar.getTime());
            dattim_end = f.format(date.getTime());
        }
        List<ZtBhzkhdf>  list = schedulingCarService.selecerenwudanpsfz(sys_depart_orgcode,dattim_begin,dattim_end);
        if (list.size() > 0){
            for (ZtBhzkhdf l :list){
                Integer aboveew = 0;
                Integer abovees = 0;
                Integer aboveys = 0;
                Integer abovehg = 0;
                Integer abovejs = 0;
                Integer abovejes = 0;
                Integer abovejss = 0;
                Integer abovejls = 0;
                Integer abovebhg = 0;
                Integer scores = 0;
                System.out.println(l.getProjname());
                ZtBhzPeisongtime ztBhzPeisongtime = schedulingCarService.ztyjsb(l.getProjname());
                if (ztBhzPeisongtime != null){
                    List<BhzrenwudanCar>  pageList = schedulingCarService.selecerenwudanps(sys_depart_orgcode,dattim_begin,dattim_end,l.getTaiz(),l.getProjname());
                    if (pageList.size() > 0){
                        for (BhzrenwudanCar page:pageList){
                            //发车时间
                            page.setDattimShij(page.getDattim());
                            Date dattimShij = page.getDattimShij();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm");
                            String format = simpleDateFormat.format(dattimShij);
                            Date parse = simpleDateFormat.parse(format);
                            //到工作面时间
                            Date dgzmsj = page.getDgzmsj();
                            if (parse != null && dgzmsj != null){
                                long time = parse.getTime();
                                long time1 = dgzmsj.getTime();
                                long l1 = time1 - time;//到工作面时间 - 发车时间
                                long minutes = 0;
                                if (l1 > 0){
                                    minutes = TimeUnit.MILLISECONDS.toMinutes(l1);
                                }else {
                                    minutes = TimeUnit.MILLISECONDS.toMinutes((3600000 + l1));
                                }
                                String bqassessmenttime = ztBhzPeisongtime.getBqassessmenttime();
                                long l2 = Long.parseLong(bqassessmenttime) - minutes;
                                if (l2 > 25){
                                    aboveew ++;
                                }else if (l2 > 20){
                                    abovees ++;
                                }else if (l2 > 10){
                                    aboveys ++;
                                }else if (l2 > 0){
                                    abovehg ++;
                                }else if (l2 > -10){
                                    abovejs ++;
                                }else if (l2 > -20){
                                    abovejes ++;
                                }else if (l2 > -40){
                                    abovejss ++;
                                }else if (l2 > -60){
                                    abovejls ++;
                                }else{
                                    abovebhg ++;
                                }
                            }
                        }
                    }
                }else {
                    log.info("该单位工程没有配置工点时间表！！！");
                }
                //分数
                scores =aboveew*(-45)+abovees*10+aboveys*40+abovehg*100+abovejs*95+abovejes*40+abovejss*(-20)+abovejls*(-40)+abovebhg*(-60);
                l.setAboveew(aboveew);
                l.setAbovees(abovees);
                l.setAboveys(aboveys);
                l.setAbovehg(abovehg);
                l.setAbovejs(abovejs);
                l.setAbovejes(abovejes);
                l.setAbovejss(abovejss);
                l.setAbovejls(abovejls);
                l.setAbovebhg(abovebhg);
                l.setScores(scores);
            }
        }
        return Result.OK(list);
    }

    /**
     * 中铁一局配送考核
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listztpsfk")
    public Result<?> queryPagelistztpsfk(SchedulingCar schedulingCar,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) throws ParseException {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            sys_depart_orgcode = sys_depart_orgcode +"%";
        }
        if(dattim_begin == null && dattim_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            dattim_begin = f.format(calendar.getTime());
            dattim_end = f.format(date.getTime());
        }
        List<ZtBhzkhdf>  list = schedulingCarService.selecerenwudanpsfzdc(sys_depart_orgcode,dattim_begin,dattim_end);
        List<Bhzrenwudangxkh>  list1 = new ArrayList<>();
        if (list.size() > 0){
            for (ZtBhzkhdf l :list){
                Bhzrenwudangxkh bhzrenwudangxkh = new Bhzrenwudangxkh();
                bhzrenwudangxkh.setJfje(0);
                bhzrenwudangxkh.setPsjc(0);
                bhzrenwudangxkh.setConspos(l.getConspos());
                bhzrenwudangxkh.setCustomer(l.getCustomer());
                bhzrenwudangxkh.setProjadr("混凝土");
                bhzrenwudangxkh.setDanwei("分钟");
                bhzrenwudangxkh.setProjname(l.getProjname());
                System.out.println(l.getProjname());
                ZtBhzPeisongtime ztBhzPeisongtime = schedulingCarService.ztyjsb(l.getProjname());
                if (ztBhzPeisongtime != null){
                    List<BhzrenwudanCar>  pageList = schedulingCarService.selecerenwudanpsdc(sys_depart_orgcode,dattim_begin,dattim_end,l.getTaiz(),l.getProjname(),l.getConspos());
                    if (pageList.size() > 0){
                        for (BhzrenwudanCar page:pageList){
                            //发车时间
                            page.setDattimShij(page.getDattim());
                            Date dattimShij = page.getDattimShij();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm");
                            String format = simpleDateFormat.format(dattimShij);
                            Date parse = simpleDateFormat.parse(format);
                            //到工作面时间
                            Date dgzmsj = page.getDgzmsj();
                            if (parse != null && dgzmsj != null){
                                long time = parse.getTime();
                                long time1 = dgzmsj.getTime();
                                long l1 = time1 - time;//到工作面时间 - 发车时间
                                long minutes = 0;
                                if (l1 > 0){
                                    minutes = TimeUnit.MILLISECONDS.toMinutes(l1);
                                }else {
                                    minutes = TimeUnit.MILLISECONDS.toMinutes((3600000 + l1));
                                }
                                String bqassessmenttime = ztBhzPeisongtime.getBqassessmenttime();
                                long l2 = Long.parseLong(bqassessmenttime) - minutes;
                                bhzrenwudangxkh.setPsjc(bhzrenwudangxkh.getPsjc()+l2);
                            }
                        }
                    }
                }else {
                    log.info("该单位工程没有配置工点时间表！！！");
                }
                //分数
                bhzrenwudangxkh.setJfje(bhzrenwudangxkh.getPsjc()*5);
                list1.add(bhzrenwudangxkh);
            }

        }
        return Result.OKs(list1);
    }
    /**
     * 中铁一局配送考核
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listztpss")
    public Result<?> queryPagelistztpss(SchedulingCar schedulingCar,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) throws ParseException {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            sys_depart_orgcode = sys_depart_orgcode +"%";
        }
        if(dattim_begin == null && dattim_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            dattim_begin = f.format(calendar.getTime());
            dattim_end = f.format(date.getTime());
        }
        List<ZtBhzkhdf>  list = schedulingCarService.selecerenwudanpsfz(sys_depart_orgcode,dattim_begin,dattim_end);
        List<ZtBhzkhdfVO>  list1 = new ArrayList<>();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df1=new SimpleDateFormat("yyyy年MM月dd日");
        if (list.size() > 0){
            for (ZtBhzkhdf l :list){
                Integer aboveew = 0;
                Integer abovees = 0;
                Integer aboveys = 0;
                Integer abovehg = 0;
                Integer abovejs = 0;
                Integer abovejes = 0;
                Integer abovejss = 0;
                Integer abovejls = 0;
                Integer abovebhg = 0;
                Integer scores = 0;
                System.out.println(l.getProjname());
                ZtBhzPeisongtime ztBhzPeisongtime = schedulingCarService.ztyjsb(l.getProjname());
                if (ztBhzPeisongtime != null){
                    List<BhzrenwudanCar>  pageList = schedulingCarService.selecerenwudanps(sys_depart_orgcode,dattim_begin,dattim_end,l.getTaiz(),l.getProjname());
                    if (pageList.size() > 0){
                        for (BhzrenwudanCar page:pageList){
                            //发车时间
                            page.setDattimShij(page.getDattim());
                            Date dattimShij = page.getDattimShij();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm");
                            String format = simpleDateFormat.format(dattimShij);
                            Date parse = simpleDateFormat.parse(format);
                            //到工作面时间
                            Date dgzmsj = page.getDgzmsj();
                            if (parse != null && dgzmsj != null){
                                long time = parse.getTime();
                                long time1 = dgzmsj.getTime();
                                long l1 = time1 - time;//到工作面时间 - 发车时间
                                long minutes = 0;
                                if (l1 > 0){
                                    minutes = TimeUnit.MILLISECONDS.toMinutes(l1);
                                }else {
                                    minutes = TimeUnit.MILLISECONDS.toMinutes((3600000 + l1));
                                }
                                String bqassessmenttime = ztBhzPeisongtime.getBqassessmenttime();
                                long l2 = Long.parseLong(bqassessmenttime) - minutes;
                                if (l2 > 25){
                                    aboveew ++;
                                }else if (l2 > 20){
                                    abovees ++;
                                }else if (l2 > 10){
                                    aboveys ++;
                                }else if (l2 > 0){
                                    abovehg ++;
                                }else if (l2 > -10){
                                    abovejs ++;
                                }else if (l2 > -20){
                                    abovejes ++;
                                }else if (l2 > -40){
                                    abovejss ++;
                                }else if (l2 > -60){
                                    abovejls ++;
                                }else{
                                    abovebhg ++;
                                }
                            }
                        }
                    }
                }else {
                    log.info("该单位工程没有配置工点时间表！！！");
                }
                //分数
                scores =aboveew*(-45)+abovees*10+aboveys*40+abovehg*100+abovejs*95+abovejes*40+abovejss*(-20)+abovejls*(-40)+abovebhg*(-60);
                l.setAboveew(aboveew);
                l.setAbovees(abovees);
                l.setAboveys(aboveys);
                l.setAbovehg(abovehg);
                l.setAbovejs(abovejs);
                l.setAbovejes(abovejes);
                l.setAbovejss(abovejss);
                l.setAbovejls(abovejls);
                l.setAbovebhg(abovebhg);
                l.setScores(scores);
                ZtBhzkhdfVO ztBhzkhdfVO = new ZtBhzkhdfVO();

                BeanUtils.copyProperties(l,ztBhzkhdfVO);
                ztBhzkhdfVO.setDattim_begin(df.parse(dattim_begin));
                ztBhzkhdfVO.setDattim_end(df.parse(dattim_end));
                String format = df1.format(ztBhzkhdfVO.getDattim_begin());
                String format1 = df1.format(ztBhzkhdfVO.getDattim_end());
                ztBhzkhdfVO.setTimepj(format+"至"+format1);
                list1.add(ztBhzkhdfVO);
            }
        }
        return Result.OKs(list1);
    }
    /**
     * 中铁一局工序考核
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listztgx")
    public Result<?> queryPagelistztgx(SchedulingCar schedulingCar,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) throws ParseException {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            sys_depart_orgcode = sys_depart_orgcode +"%";
        }
        if(dattim_begin == null && dattim_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            dattim_begin = f.format(calendar.getTime());
            dattim_end = f.format(date.getTime());
        }

        List<ZtBhzjckh>  list = schedulingCarService.selecerenwudanpsgx(sys_depart_orgcode,dattim_begin,dattim_end);
        if (list.size() > 0){
            for (ZtBhzjckh l :list){
                Integer aboveew = 0;
                Integer abovees = 0;
                Integer aboveys = 0;
                Integer abovehg = 0;
                Integer abovejs = 0;
                Integer abovejes = 0;
                Integer abovejss = 0;
                Integer abovejls = 0;
                Integer abovebhg = 0;
                Integer scores = 0;
                System.out.println(l.getProjname());
                ZtBhzGonxutime ztBhzPeisongtime = schedulingCarService.ztgxsb(l.getConspos());
                if (ztBhzPeisongtime != null){
                    List<BhzrenwudanCar>  pageList = schedulingCarService.selecerenwudangx(sys_depart_orgcode,dattim_begin,dattim_end,l.getTaiz(),l.getConspos());
                    if (pageList.size() > 0){
                        for (BhzrenwudanCar page:pageList){
                            String xlsj = page.getXlsj();//卸料时间
                            String xwsj = page.getXwsj();//卸完时间
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if (xlsj != null && xwsj != null){
                                Date parse = simpleDateFormat.parse(xlsj);//卸料时间
                                Date dgzmsj = simpleDateFormat.parse(xwsj);//卸完时间
                                long time = parse.getTime();
                                long time1 = dgzmsj.getTime();
                                long l1 = time1 - time;//到工作面时间 - 发车时间
                                long minutes = 0;
                                if (l1 > 0){
                                    minutes = TimeUnit.MILLISECONDS.toMinutes(l1);
                                }else {
                                    minutes = TimeUnit.MILLISECONDS.toMinutes((3600000 + l1));
                                }
                                String bqassessmenttime = ztBhzPeisongtime.getAssessmenttime();
                                long l2 = Long.parseLong(bqassessmenttime) - minutes;
                                if (l2 > 25){
                                    aboveew ++;
                                }else if (l2 > 20){
                                    abovees ++;
                                }else if (l2 > 10){
                                    aboveys ++;
                                }else if (l2 > 0){
                                    abovehg ++;
                                }else if (l2 > -10){
                                    abovejs ++;
                                }else if (l2 > -20){
                                    abovejes ++;
                                }else if (l2 > -40){
                                    abovejss ++;
                                }else if (l2 > -60){
                                    abovejls ++;
                                }else{
                                    abovebhg ++;
                                }
                            }
                        }
                    }
                }else {
                    log.info("该单位工程没有配置工点时间表！！！");
                }
                //分数
                scores =aboveew*(65)+abovees*80+aboveys*90+abovehg*100+abovejs*95+abovejes*75+abovejss*(50)+abovejls*(-40)+abovebhg*(-60);
                l.setAboveew(aboveew);
                l.setAbovees(abovees);
                l.setAboveys(aboveys);
                l.setAbovehg(abovehg);
                l.setAbovejs(abovejs);
                l.setAbovejes(abovejes);
                l.setAbovejss(abovejss);
                l.setAbovejls(abovejls);
                l.setAbovebhg(abovebhg);
                l.setScores(scores);
            }
        }
        return Result.OK(list);
    }

    /**
     * 中铁一局工序考核导出
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listztgxdc")
    public Result<?> queryPagelistztgxdc(SchedulingCar schedulingCar,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) throws ParseException {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            sys_depart_orgcode = sys_depart_orgcode +"%";
        }
        if(dattim_begin == null && dattim_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            dattim_begin = f.format(calendar.getTime());
            dattim_end = f.format(date.getTime());
        }

        List<ZtBhzjckh>  list = schedulingCarService.selecerenwudanpsgx(sys_depart_orgcode,dattim_begin,dattim_end);
        List<ZtBhzkhdfVO>  list1 = new ArrayList<>();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df1=new SimpleDateFormat("yyyy年MM月dd日");
        if (list.size() > 0){
            for (ZtBhzjckh l :list){
                Integer aboveew = 0;
                Integer abovees = 0;
                Integer aboveys = 0;
                Integer abovehg = 0;
                Integer abovejs = 0;
                Integer abovejes = 0;
                Integer abovejss = 0;
                Integer abovejls = 0;
                Integer abovebhg = 0;
                Integer scores = 0;
                System.out.println(l.getProjname());
                ZtBhzGonxutime ztBhzPeisongtime = schedulingCarService.ztgxsb(l.getConspos());
                if (ztBhzPeisongtime != null){
                    List<BhzrenwudanCar>  pageList = schedulingCarService.selecerenwudangx(sys_depart_orgcode,dattim_begin,dattim_end,l.getTaiz(),l.getConspos());
                    if (pageList.size() > 0){
                        for (BhzrenwudanCar page:pageList){
                            String xlsj = page.getXlsj();//卸料时间
                            String xwsj = page.getXwsj();//卸完时间
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if (xlsj != null && xwsj != null){
                                Date parse = simpleDateFormat.parse(xlsj);//卸料时间
                                Date dgzmsj = simpleDateFormat.parse(xwsj);//卸完时间
                                long time = parse.getTime();
                                long time1 = dgzmsj.getTime();
                                long l1 = time1 - time;//到工作面时间 - 发车时间
                                long minutes = 0;
                                if (l1 > 0){
                                    minutes = TimeUnit.MILLISECONDS.toMinutes(l1);
                                }else {
                                    minutes = TimeUnit.MILLISECONDS.toMinutes((3600000 + l1));
                                }
                                String bqassessmenttime = ztBhzPeisongtime.getAssessmenttime();
                                long l2 = Long.parseLong(bqassessmenttime) - minutes;
                                if (l2 > 25){
                                    aboveew ++;
                                }else if (l2 > 20){
                                    abovees ++;
                                }else if (l2 > 10){
                                    aboveys ++;
                                }else if (l2 > 0){
                                    abovehg ++;
                                }else if (l2 > -10){
                                    abovejs ++;
                                }else if (l2 > -20){
                                    abovejes ++;
                                }else if (l2 > -40){
                                    abovejss ++;
                                }else if (l2 > -60){
                                    abovejls ++;
                                }else{
                                    abovebhg ++;
                                }
                            }
                        }
                    }
                }else {
                    log.info("该单位工程没有配置工点时间表！！！");
                }
                //分数
                scores =aboveew*(65)+abovees*80+aboveys*90+abovehg*100+abovejs*95+abovejes*75+abovejss*(50)+abovejls*(-40)+abovebhg*(-60);
                l.setAboveew(aboveew);
                l.setAbovees(abovees);
                l.setAboveys(aboveys);
                l.setAbovehg(abovehg);
                l.setAbovejs(abovejs);
                l.setAbovejes(abovejes);
                l.setAbovejss(abovejss);
                l.setAbovejls(abovejls);
                l.setAbovebhg(abovebhg);
                l.setScores(scores);
                ZtBhzkhdfVO ztBhzkhdfVO = new ZtBhzkhdfVO();

                BeanUtils.copyProperties(l,ztBhzkhdfVO);
                ztBhzkhdfVO.setDattim_begin(df.parse(dattim_begin));
                ztBhzkhdfVO.setDattim_end(df.parse(dattim_end));
                String format = df1.format(ztBhzkhdfVO.getDattim_begin());
                String format1 = df1.format(ztBhzkhdfVO.getDattim_end());
                ztBhzkhdfVO.setTimepj(format+"至"+format1);
                list1.add(ztBhzkhdfVO);
            }
        }
        return Result.OKs(list1);
    }

    /**
     * 中铁一局工序考核超时罚款导出
     *
     * @param schedulingCar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "十五局浇筑令发车详情统计单打印及导出")
    @ApiOperation(value = "十五局浇筑令发车详情统计单打印及导出", notes = "十五局浇筑令发车详情统计单打印及导出")
    @GetMapping(value = "/listztgxfk")
    public Result<?> queryPagelistztgxfk(SchedulingCar schedulingCar,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req, String sys_depart_orgcode, String dattim_begin, String dattim_end) throws ParseException {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            sys_depart_orgcode = sys_depart_orgcode +"%";
        }
        if(dattim_begin == null && dattim_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            dattim_begin = f.format(calendar.getTime());
            dattim_end = f.format(date.getTime());
        }

        List<ZtBhzjckh>  list = schedulingCarService.selecerenwudanpsgxdc(sys_depart_orgcode,dattim_begin,dattim_end);
        List<Bhzrenwudangxkh>  list1 = new ArrayList<>();
        if (list.size() > 0){
            for (ZtBhzjckh l :list){
                Bhzrenwudangxkh bhzrenwudangxkh = new Bhzrenwudangxkh();
                bhzrenwudangxkh.setJfje(0);
                bhzrenwudangxkh.setPsjc(0);
                bhzrenwudangxkh.setConspos(l.getConspos());
                bhzrenwudangxkh.setCustomer(l.getCustomer());
                bhzrenwudangxkh.setProjadr("混凝土");
                bhzrenwudangxkh.setDanwei("分钟");
                bhzrenwudangxkh.setProjname(l.getProjname());
                ZtBhzGonxutime ztBhzPeisongtime = schedulingCarService.ztgxsb(l.getConspos());
                if (ztBhzPeisongtime != null){
                    List<BhzrenwudanCar>  pageList = schedulingCarService.selecerenwudanpsdc(sys_depart_orgcode,dattim_begin,dattim_end,l.getTaiz(),l.getProjname(),l.getConspos());
                    if (pageList.size() > 0){
                        for (BhzrenwudanCar page:pageList){
                            String xlsj = page.getXlsj();//卸料时间
                            String xwsj = page.getXwsj();//卸完时间
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm");
                            if (xlsj != null && xwsj != null){
                                Date parse = simpleDateFormat.parse(xlsj);//卸料时间
                                Date dgzmsj = simpleDateFormat.parse(xwsj);//卸完时间
                                long time = parse.getTime();
                                long time1 = dgzmsj.getTime();
                                long l1 = time1 - time;//到工作面时间 - 发车时间
                                long minutes = 0;
                                if (l1 > 0){
                                    minutes = TimeUnit.MILLISECONDS.toMinutes(l1);
                                }else {
                                    minutes = TimeUnit.MILLISECONDS.toMinutes((3600000 + l1));
                                }
                                String bqassessmenttime = ztBhzPeisongtime.getAssessmenttime();
                                long l2 = Long.parseLong(bqassessmenttime) - minutes;
                                bhzrenwudangxkh.setPsjc(bhzrenwudangxkh.getPsjc()+l2);
                            }
                        }
                    }
                }else {
                    log.info("该单位工程没有配置工点时间表！！！");
                }
                bhzrenwudangxkh.setJfje(bhzrenwudangxkh.getPsjc()*5);
                list1.add(bhzrenwudangxkh);
            }
        }
        return Result.OKs(list1);
    }
    /**
     * 添加
     *
     * @param schedulingCar
     * @return
     */
    @AutoLog(value = "调度车辆-添加")
    @ApiOperation(value = "调度车辆-添加", notes = "调度车辆-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SchedulingCar schedulingCar) {
        List<SchedulingCar> cars = schedulingCarService.getSchedulingCarByDanHao(schedulingCar.getDanhao());
        if(StringUtils.isNotBlank(schedulingCar.getCode()) && StringUtils.isBlank(schedulingCar.getSiteName())){
            Bhzrenwudan queryselectone = bhzrenwudanService.queryselectone(schedulingCar.getCode());
            if(ObjectUtil.isNotNull(queryselectone)){
                schedulingCar.setSiteName(queryselectone.getConspos());
            }
        }else if(StringUtils.isNotBlank(schedulingCar.getRecipe()) && StringUtils.isBlank(schedulingCar.getSiteName()) ){
            Shigongphb shigongphb = shigongphbService.queryoneCode(schedulingCar.getRecipe());
            if(ObjectUtil.isNotNull(shigongphb)){
                schedulingCar.setSiteName(shigongphb.getConspos());
            }
        }
        if (cars != null && cars.size() > 0) {
            schedulingCar.setId(cars.get(0).getId());
            schedulingCarService.updateById(schedulingCar);
            return Result.OK("修改成功！");
        } else {
            // 合兴高铁
//            if (schedulingCar.getFlag().equals("1Rtnw") || schedulingCar.getFlag().equals("1RBtnw")) {
//                schedulingCar.setSysOrgCode("A05A03A01A01");
//            }
            schedulingCarService.save(schedulingCar);
            return Result.OK("添加成功！");
        }

    }

    /**
     * 添加
     *
     * @param schedulingCar
     * @return
     */
    @AutoLog(value = "调度车辆-添加")
    @ApiOperation(value = "调度车辆-添加", notes = "调度车辆-添加")
    @GetMapping(value = "/querysum")
    public Result<?> querysum(SchedulingCar schedulingCar, HttpServletRequest req, String sys_depart_orgcode) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            schedulingCar.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        QueryWrapper<SchedulingCar> queryWrapper = QueryGenerator.initQueryWrapper(schedulingCar, req.getParameterMap());
        List<SchedulingCar> list = schedulingCarService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 编辑
     *
     * @param schedulingCar
     * @return
     */
    @AutoLog(value = "调度车辆-编辑")
    @ApiOperation(value = "调度车辆-编辑", notes = "调度车辆-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody SchedulingCar schedulingCar) {
        SchedulingCar byId = schedulingCarService.getById(schedulingCar.getId());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if(schedulingCar.getStatus()!=null && schedulingCar.getStatus()==1){
            schedulingCar.setReceiver(loginUser.getRealname());
        }
         if (byId.getStatus1() == 3 && schedulingCar.getStatus()==1 ) { // 已经超时
            QueryWrapper<ShebeiInfo> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.likeRight("sys_org_code", byId.getSysOrgCode());
            queryWrapper1.eq("sbname", byId.getVehicle());
            ShebeiInfo one = shebeiInfoService.getOne(queryWrapper1);
            if( one != null){
                QueryWrapper<SuchingCarpeiz> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("imei", one.getSbjno());
                SuchingCarpeiz one1 = suchingCarpeizService.getOne(queryWrapper2);
                if (one1 != null) {
                    long ms = 1;
                    String msg = "";
                    int ast = 1;// 运输中
                    // 预警时间间隔计算
                    if (one1.getUnit() == 1) { // 小时
                        ms = (long) (1000 * 60 * 60 * Double.parseDouble(one1.getRemark()));
                    } else if (one1.getUnit() == 0) { // 分钟
                        ms = (long) (1000 * 60 * Double.parseDouble(one1.getRemark()));
                    } else {// 天
                        ms = (long) (1000 * 60 * 60 * 24 * Double.parseDouble(one1.getRemark()));
                    }

                    if((schedulingCar.getQianshoutime().getTime()-byId.getDattim().getTime()) > ms){
                        msg ="驾驶员：" +byId.getDriver()+" 运输超规定时间" +(schedulingCar.getQianshoutime().getTime()-byId.getDattim().getTime())/(1000*60)+"分";
                        schedulingCar.setStatus1(3);
                        if (one1.getIsCall() == 0){
                            try {
                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format2 = dateFormat.format(byId.getDattim());
                                fsyjxx(one1,format2,byId.getSiteName().split("\\|")[0]+one.getSbname()+"车",msg);
                            } catch (ClientException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }

        }
        schedulingCarService.updateById(schedulingCar);
        return Result.OK("编辑成功!");
    }

    public void fsyjxx(SuchingCarpeiz one1,String product_datetime,String getSbjno,String overReason) throws ClientException {
        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(one1.getZhydPhones());
        JSONObject obj = new JSONObject();
        obj.put("sbname", getSbjno);
        obj.put("time", product_datetime);
        obj.put("content", overReason);

        String esTitle = "混凝土运输车签收超时预警";
        String esType = "1";//短信类型  1短信
        String esReceiver = bhzPhone.getPhones();//手机号
        String esContent =(obj.toString());//短信内容
        String esSendStatus=("0");//推送状态0未推送
        int esSendNum=0;//推送总次数
        String remark=(getSbjno);
        Date createTime=new Date();

//        boolean b = DySmsHelper.sendSms(one1.getZhydPhones(), obj, DySmsEnum.SMS_CB_CODE);
//        if (b){
//            sysMessage3.setEsSendNum(1);
//            log.info("拌合站超时预警短信发送成功"+obj);
//        }else {
//            sysMessage3.setEsSendNum(2);
//            log.info("拌合站超时预警短信发送失败"+obj);
//        }
        schedulingCarService.saveOverMsg(esTitle,esType,esReceiver,esContent,esSendStatus,esSendNum,remark,createTime);
    }

    /**
     * 编辑
     *
     * @param status,id
     * @return
     */
    @AutoLog(value = "调度车辆-编辑")
    @ApiOperation(value = "调度车辆-编辑", notes = "调度车辆-编辑")
    @PutMapping(value = "/editCar")
    public Result<?> edit(Integer status, Integer id) {
        if (id != null) {
            SchedulingCar byId = schedulingCarService.getById(id);
            if (byId != null) {
                SchedulingCar schedulingCar = new SchedulingCar();
                schedulingCar.setId(id);
                schedulingCar.setStatus(status);
                schedulingCarService.updateById(schedulingCar);
            } else {
                return Result.OK("签收失败!");
            }

        } else {
            return Result.OK("传递参数错误!");
        }
        return Result.OK("签收成功成功!");
    }



    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "调度车辆-通过id删除")
    @ApiOperation(value = "调度车辆-通过id删除", notes = "调度车辆-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        schedulingCarService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "调度车辆-批量删除")
    @ApiOperation(value = "调度车辆-批量删除", notes = "调度车辆-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.schedulingCarService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "调度车辆-通过id查询")
    @ApiOperation(value = "调度车辆-通过id查询", notes = "调度车辆-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SchedulingCar schedulingCar = schedulingCarService.getById(id);
        if (schedulingCar == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(schedulingCar);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param schedulingCar
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SchedulingCar schedulingCar) {
        return super.exportXls(request, schedulingCar, SchedulingCar.class, "调度车辆");
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
        return super.importExcel(request, response, SchedulingCar.class);
    }

    /**
     * 根据任务单编号查询运输车生产详情（包含拌合站主表信息）
     *
     * @param code
     * @return ResponseValue<SchedulingCarRespVO>
     */
    @GetMapping("/getCarAndMixingStationData")
    @ApiOperation(value = "运输车生产详情", notes = "作者：lis1")
    @ApiImplicitParam(paramType = "path", name = "Code", value = "任务单编号", required = true, dataType = "String")
    public Result<GetCarDetailVo> getCarAndMixingStationData(@RequestParam("code") String code) {
        log.info("[] 查询单条记录：bussId {}", code);
        GetCarDetailVo data = schedulingCarService.getCarAndMixingStationData(code);
        return Result.OK(data);
    }

    /**
     * 根据发车单号查询拌合站数据
     *
     * @param clientNo 发车单号
     * @return
     */
    @AutoLog(value = "发车单单号查询-拌合站数据")
    @ApiOperation(value = "发车单单号查询-拌合站数据", notes = "发车单单号查询-拌合站数据")
    @GetMapping(value = "/getBhzBaseByCarDH")
    public Result<?> getBhzCementBaseByCarDanHao(String clientNo) {
        List<BhzCementBase> bhzCementBases = schedulingCarService.getBhzByDanHao(clientNo);
        return Result.OK(bhzCementBases);
    }
    /**
     * 根据发车单号查询拌合站数据
     *
     * @param phb 发车单号
     * @return
     */
    @AutoLog(value = "发车单单号查询-拌合站数据")
    @ApiOperation(value = "发车单单号查询-拌合站数据", notes = "发车单单号查询-拌合站数据")
    @GetMapping(value = "/getBhzBaseByPHB")
    public Result<?> getBhzCementBaseByPHB(String phb) {
        List<BhzCementBase> bhzCementBases = schedulingCarService.getBhzByPHB(phb);
        return Result.OK(bhzCementBases);
    }


    /**
     * 打印十五局混凝土发货单
     *
     * @param id
     */
    @RequestMapping(value = "/printData")
    public Result<?> printData(Integer id) {
        ArrayList<SchedulingCarVO> data = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        SchedulingCarVO schedulingCarVO = new SchedulingCarVO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        QueryWrapper<SchedulingCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        SchedulingCar one = schedulingCarService.getOne(queryWrapper);

        String datTime = sdf.format(one.getDattim());
        schedulingCarVO.setDattim(datTime);
        map.put("生产日期", datTime);
        schedulingCarVO.setCode(one.getCode());//任务单号
        map.put("任务单号", one.getCode());
        schedulingCarVO.setDriver(one.getDriver());//驾驶员
        map.put("驾驶员", one.getDriver());
        schedulingCarVO.setVehicle(one.getVehicle());//运输车号
        map.put("运输车号", one.getVehicle());
        schedulingCarVO.setTotmete(one.getTotmete());//累计方量
        map.put("累计方量", String.valueOf(one.getTotmete()));
        schedulingCarVO.setTotvehs(one.getTotvehs());//累计车次
        map.put("累计车次", String.valueOf(one.getTotvehs()));
        schedulingCarVO.setProdmete(one.getProdmete());//生产方量
        map.put("生产方量", String.valueOf(one.getProdmete()));
        List<RenwudanSchedule> queryone = scheduleService.queryoness(one.getCode(), one.getStation());
        if (ObjectUtil.isNotEmpty(queryone)) {
            RenwudanSchedule renwudanSchedule = queryone.get(0);
            SysDepart sysDepart = schedulingCarService.getSysDepart(renwudanSchedule.getSysOrgCode());
            String fcdName = schedulingCarService.getFcdName(sysDepart);
            schedulingCarVO.setFcdname(fcdName);//发车单名称
            map.put("发车单", fcdName);
            schedulingCarVO.setZhanno(sysDepart.getDepartName());//拌合站号
            map.put("搅拌站号", sysDepart.getDepartName());
            schedulingCarVO.setBetlev(renwudanSchedule.getBetlev());//强度等级
            map.put("强度等级", renwudanSchedule.getBetlev());
            schedulingCarVO.setMormete(renwudanSchedule.getMete());//计划方量
            map.put("计划方量", String.valueOf(renwudanSchedule.getMete()));
            schedulingCarVO.setProjname(renwudanSchedule.getProjectname());//工程名称
            map.put("工程名称", renwudanSchedule.getProjectname());
            schedulingCarVO.setConspos(renwudanSchedule.getConspos());//施工部位
            map.put("施工部位", renwudanSchedule.getConspos());
            schedulingCarVO.setLands(renwudanSchedule.getLands());//塌落度
            map.put("塌落度", renwudanSchedule.getLands());
        }
        List<Bhzrenwudan> bhzrenwudanList = bhzrenwudanService.queryRWD(one.getCode(), one.getStation());
        if (ObjectUtil.isNotEmpty(bhzrenwudanList)) {
            Bhzrenwudan bhzrenwudan = bhzrenwudanList.get(0);
            schedulingCarVO.setCustomer(bhzrenwudan.getCustomer());//客户名称
            map.put("客户名称", bhzrenwudan.getCustomer());
            schedulingCarVO.setDistance(bhzrenwudan.getDistance());//运输距离
            map.put("运输距离", String.valueOf(bhzrenwudan.getDistance()));
        }
        List<BhzCementBase> bhzCementBaseList = bhzCementBaseService.queryByPhb(one.getCode());
        if (ObjectUtil.isNotEmpty(bhzCementBaseList)) {
            BhzCementBase bhzCementBase = bhzCementBaseList.get(0);
            schedulingCarVO.setHandler(bhzCementBase.getHandlers());//操作员
            map.put("操作员", bhzCementBase.getHandlers());
        }
        List<Map<String, String>> maps = new ArrayList<>();
        maps.add(map);
        schedulingCarVO.setInfo(maps);
        data.add(schedulingCarVO);
        return Result.OKs(data);
    }


}
