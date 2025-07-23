package com.trtm.iot.wztaizhang.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.devicepipepilehistorydataone.mapper.DevicePipepileHistorydataOneMapper;
import com.trtm.iot.hntbhz.mapper.BhzCementBaseMapper;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.trtm.iot.wzgongyingshangman.service.IWzgongyingshangManService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.WztaizhangLc;
import com.trtm.iot.wztaizhang.service.IWztaizhangLcService;
import com.trtm.iot.wztaizhang.vo.*;
import com.trtm.iot.ycltd.entity.YclTestDetail;
import com.trtm.iot.ycltd.service.IYclTestDetailService;
import com.trtm.iot.ycltesttaizhang.service.IYclTestTaizhangService;
import com.trtm.iot.yclud.entity.YclUsageDetail;
import com.trtm.iot.yclud.entity.YclUsageDetailVO;
import com.trtm.iot.yclud.service.IYclUsageDetailService;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;

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
 * @Description: wztaizhang
 * @Author: jeecg-boot
 * @Date: 2021-06-18
 * @Version: V1.0
 */
@Api(tags = "wztaizhang")
@RestController
@RequestMapping("/wztaizhang/wztaizhang")
@Slf4j
public class WztaizhangController extends JeecgController<Wztaizhang, IWztaizhangService> {
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWzcailiaonamedictManService wzcailiaonamedictManService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private BhzCementBaseMapper bhzCementBaseMapper;
    @Autowired
    private IYclTestDetailService yclTestDetailService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzgongyingshangManService wzgongyingshangManService;
    @Autowired
    private IYclUsageDetailService yclUsageDetailService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    DevicePipepileHistorydataOneMapper devicePipepileHistorydataOneMapper;
    @Autowired
    private IYclTestTaizhangService yclTestTaizhangService;


    /**
     * 分页列表查询(按设备等计算)
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-分页列表查询")
    @ApiOperation(value = "wztaizhang-分页列表查询", notes = "wztaizhang-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Wztaizhang wztaizhang,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wztaizhang.setRuleway(0);
        //查询到他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());

        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "wztaizhang-分页列表查询")
    @ApiOperation(value = "wztaizhang-分页列表查询", notes = "wztaizhang-分页列表查询")
    @GetMapping(value = "/listJz")
    public Result<?> queryPageListJz(Wztaizhang wztaizhang,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   String sys_depart_orgcode,
                                   HttpServletRequest req) {

        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());

        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询(按项目等计算)
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-分页列表查询")
    @ApiOperation(value = "wztaizhang-分页列表查询", notes = "wztaizhang-分页列表查询")
    @GetMapping(value = "/lists")
    public Result<?> queryPageLists(Wztaizhang wztaizhang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wztaizhang.setRuleway(1);
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wztaizhang.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wztaizhang.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "wztaizhang-分页列表查询")
    @ApiOperation(value = "wztaizhang-分页列表查询", notes = "wztaizhang-分页列表查询")
    @PermissionData(pageComponent = "zlgl/yclsl/Wzycljinchanggb1OList")
    @GetMapping(value = "/listsp")
    public Result<?> queryPageListsp(Wztaizhang wztaizhang,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));

        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }

        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wztaizhang.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wztaizhang.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        List<Wztaizhang> records = pageList.getRecords();
        for (Wztaizhang record : records) {
            String sumUse = yclUsageDetailService.getSumUse(record.getPici());
            if (ObjectUtil.isNotEmpty(sumUse)) {
                //使用量不为null
                double use = Double.parseDouble(String.format("%.2f", Double.parseDouble(sumUse)));//使用量
                double kucun = Double.parseDouble(record.getJingzhongt()) - use;//库存=数量-使用量
                record.setMaozhongt(String.format("%.2f", use) + record.getDanwei());
                record.setPizhongt(String.format("%.2f", kucun) + record.getDanwei());
            } else {
                //使用量为null
                record.setMaozhongt("0" + record.getDanwei());
                record.setPizhongt(record.getJingzhongt() + record.getDanwei());
            }
        }
        return Result.OK(pageList);
    }
    @AutoLog(value = "点击料仓查检验批")
    @ApiOperation(value = "点击料仓查检验批-钢筋加工厂用", notes = "wztaizhang-分页列表查询")
    @PermissionData(pageComponent = "rebar/taskManagement")
    @GetMapping(value = "/listRebar")
    public Result<?> queryPageListRebar(Wztaizhang wztaizhang,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));

        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }

        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wztaizhang.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wztaizhang.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        List<Wztaizhang> records = pageList.getRecords();
        for (Wztaizhang record : records) {
            String sumUse = yclUsageDetailService.getSumUse(record.getPici());
            if (ObjectUtil.isNotEmpty(sumUse)) {
                //使用量不为null
                double use = Double.parseDouble(String.format("%.2f", Double.parseDouble(sumUse)));//使用量
                double kucun = Double.parseDouble(record.getJingzhongt()) - use;//库存=数量-使用量
                record.setMaozhongt(String.format("%.2f", use) + record.getDanwei());
                record.setPizhongt(String.format("%.2f", kucun) + record.getDanwei());
            } else {
                //使用量为null
                record.setMaozhongt("0" + record.getDanwei());
                record.setPizhongt(record.getJingzhongt() + record.getDanwei());
            }
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "wztaizhang-分页列表查询")
    @ApiOperation(value = "wztaizhang-分页列表查询", notes = "wztaizhang-分页列表查询")
    @PermissionData(pageComponent = "zlgl/wztaizhang/wztaizhangnew/WztaizhangLists1")
    @GetMapping(value = "/listsp2")
    public Result<?> queryPageListsp2(Wztaizhang wztaizhang,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));

        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }

        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wztaizhang.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wztaizhang.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        List<Wztaizhang> records = pageList.getRecords();
        for (Wztaizhang record : records) {
            String sumUse = yclUsageDetailService.getSumUse(record.getPici());
            if (ObjectUtil.isNotEmpty(sumUse)) {
                //使用量不为null
                double use = Double.parseDouble(String.format("%.2f", Double.parseDouble(sumUse)));//使用量
                double kucun = Double.parseDouble(record.getJingzhongt()) - use;//库存=数量-使用量
                record.setMaozhongt(String.format("%.2f", use) + record.getDanwei());
                record.setPizhongt(String.format("%.2f", kucun) + record.getDanwei());
            } else {
                //使用量为null
                record.setMaozhongt("0" + record.getDanwei());
                record.setPizhongt(record.getJingzhongt() + record.getDanwei());
            }
        }
        return Result.OK(pageList);
    }
    /**
     * 物资取样查询(按设备等计算)
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-物资取样查询")
    @ApiOperation(value = "wztaizhang-物资取样查询", notes = "wztaizhang-物资取样查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(Wztaizhang wztaizhang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wztaizhang.setRuleway(0);
        //查询到他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }
        wztaizhang.setIsfinish(1);
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 物资取样查询(按项目等计算)
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-物资取样查询")
    @ApiOperation(value = "wztaizhang-物资取样查询", notes = "wztaizhang-物资取样查询")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList6(Wztaizhang wztaizhang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wztaizhang.setRuleway(1);
        //查询到他的设备编号
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wztaizhang.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wztaizhang.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        wztaizhang.setIsfinish(1);
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 委托单申请查询
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-委托单申请查询")
    @ApiOperation(value = "wztaizhang-委托单申请查询", notes = "wztaizhang-委托单申请查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(Wztaizhang wztaizhang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }
        wztaizhang.setIsfinish(1);
        wztaizhang.setIsquyang(1);
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 检验批次查询
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-检验批次查询")
    @ApiOperation(value = "wztaizhang-检验批次查询", notes = "wztaizhang-检验批次查询")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(Wztaizhang wztaizhang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }
        wztaizhang.setIsfinish(1);
        wztaizhang.setIsquyang(1);
        wztaizhang.setJianyanstate(1);
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        List<Wztaizhang> pageList = wztaizhangService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 材料统计
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-材料统计")
    @ApiOperation(value = "wztaizhang-材料统计", notes = "wztaizhang-材料统计")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(Wztaizhang wztaizhang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper.select("sum(jingzhongT) as jingzhongT,cailiaoNo,shebeibianhao");
        queryWrapper.groupBy("cailiaoNo");
        List<Wztaizhang> pageList = wztaizhangService.list(queryWrapper);
        List records1 = new ArrayList<>();
        for (Wztaizhang wztaizhang1 : pageList) {
            Wztaizhang wztaizhang2 = new Wztaizhang();
            String cailiaono = wztaizhang1.getCailiaono();
            Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(cailiaono);
            String jingzhongt = wztaizhang1.getJingzhongt();
            wztaizhang2.setCailiaono(wzcailiaonamedict.getCailiaoname());
            wztaizhang2.setJingzhongt(jingzhongt);
            records1.add(wztaizhang2);
        }
        return Result.OK(records1);
    }


    /**
     * 集团标准版看板检验批统计
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-统计")
    @ApiOperation(value = "wztaizhang-统计", notes = "集团标准版看板检验批统计")
    @GetMapping(value = "/getlist5")
    public Result<?> getlist5(Wztaizhang wztaizhang, String projectid,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
//        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(projectid + "shebeilist"));
        //查询到他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper.select("sum(case when isfinish=1 then 1 else 0 end) as finish," +
                "sum(case when isfinish=0 then 1 else 0 end) as unfinish," +
                "sum(case when jianyanstate=1 then 1 else 0 end) as hege," +
                "sum(case when jianyanstate=2 then 1 else 0 end) as buhege");
        Map<String, Object> map = wztaizhangService.getMap(queryWrapper);
        return Result.OK(map);
    }


    /**
     * 统计
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-统计")
    @ApiOperation(value = "wztaizhang-统计", notes = "wztaizhang-统计")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList5(Wztaizhang wztaizhang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper.select("sum(case when isfinish=1 then 1 else 0 end) as finish," +
                "sum(case when isfinish=0 then 1 else 0 end) as unfinish," +
                "sum(case when jianyanstate=1 then 1 else 0 end) as hege," +
                "sum(case when jianyanstate=2 then 1 else 0 end) as buhege");
        Map<String, Object> map = wztaizhangService.getMap(queryWrapper);
        return Result.OK(map);
    }

    /**
     * 原材不合格清单统计
     *
     * @param wztaizhang
     * @param orgCode
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-不合格统计")
    @ApiOperation(value = "wztaizhang-不合格统计", notes = "wztaizhang-不合格统计")
    @GetMapping(value = "/list7")
    public Result<?> queryStatistics(Wztaizhang wztaizhang,
                                     String orgCode,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId()));
        //查询他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }

        List<String> nodetypeList = null;
        if (orgCode != null) {
            nodetypeList = wzcailiaonamedictService.queryNodetypeList1(orgCode);
        } else {
            nodetypeList = wzcailiaonamedictService.queryNodetypeList2();
        }

        ArrayList<WztaizhangVo> data = new ArrayList<>();
        Wzcailiaonamedict wzcailiaonamedict = new Wzcailiaonamedict();
        for (String nodetype : nodetypeList) {
            List<String> lists = new ArrayList<>();//lists
            if (nodetype != null) {
                QueryWrapper<Wzcailiaonamedict> queryWrapper = QueryGenerator.initQueryWrapper(wzcailiaonamedict, req.getParameterMap());
                queryWrapper.select("cailiaono");
                queryWrapper.eq("nodetype", nodetype);
//                queryWrapper.orderByDesc( "nodetype");
                List<Wzcailiaonamedict> list = wzcailiaonamedictService.list(queryWrapper);
                for (Wzcailiaonamedict l : list) {
                    lists.add(l.getCailiaono());//lists的元素是根据wzcailiaonamedict的nodetype分类的cailiaono
                }

                //获取总量
                QueryWrapper<Wztaizhang> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.select("cast(sum(jingzhongT) as signed) as jingzhongT,count(pici) as pici");
                queryWrapper1.in("cailiaono", lists);
//                queryWrapper1.groupBy("nodetype");
                Wztaizhang one = wztaizhangService.getOne(queryWrapper1);

                //获取不合格量
                QueryWrapper<Wztaizhang> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.select("cast(sum(jingzhongT) as signed) as maozhongT,count(jianyanstate) as pizhongT");
//                queryWrapper2.eq("jianyanstate", 2);
                queryWrapper2.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
                queryWrapper2.in("cailiaono", lists);
//                queryWrapper2.groupBy(nodetype);
                Wztaizhang two = wztaizhangService.getOne(queryWrapper2);

                //获取闭合量
                QueryWrapper<Wztaizhang> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.select("count(overproof_status) as overproofStatus");
                queryWrapper3.eq("overproof_status", 20);
                queryWrapper3.in("cailiaono", lists);
                Wztaizhang three = wztaizhangService.getOne(queryWrapper3);

                //判断sql是否查询到数据，如果没有对应数据是为null，赋值给0
                if (one.getJingzhongt() == null) {
                    one.setJingzhongt("0");
                }
                if (one.getPici() == null) {
                    one.setPici("0");
                }
                if (two.getMaozhongt() == null) {
                    two.setMaozhongt("0");
                }
                if (two.getPizhongt() == null) {
                    two.setPizhongt("0");
                }
                if (three.getOverproofStatus() == null) {
                    three.setOverproofStatus(0);
                }
                //求不合格占比
                String bhgJingzhongZb = wztaizhangService.getPercentStr(Integer.parseInt(two.getMaozhongt()), Integer.parseInt(one.getJingzhongt()));//不合格数量占比
                String bhgPiciZb = wztaizhangService.getPercentStr(Integer.parseInt(two.getPizhongt()), Integer.parseInt(one.getPici()));//不合格批次占比
                //求闭合率
                String bhl = "";
                if (two.getPizhongt().equals("0")) {
                    bhl = "-";
                } else {
                    bhl = wztaizhangService.getPercentStr(three.getOverproofStatus(), Integer.parseInt(one.getPici()));
                }

                WztaizhangVo wztaizhangVo = new WztaizhangVo();
                switch (nodetype) {
                    case "1":
                        wztaizhangVo.setTypeName("细集料");
                        break;
                    case "2":
                        wztaizhangVo.setTypeName("粗集料");
                        break;
                    case "5":
                        wztaizhangVo.setTypeName("水");
                        break;
                    case "6":
                        wztaizhangVo.setTypeName("水泥");
                        break;
                    case "7":
                        wztaizhangVo.setTypeName("矿粉");
                        break;
                    case "8":
                        wztaizhangVo.setTypeName("粉煤灰");
                        break;
                    case "9":
                        wztaizhangVo.setTypeName("外加剂");
                        break;
                    case "10":
                        wztaizhangVo.setTypeName("其他");
                        break;
                    case "11":
                        wztaizhangVo.setTypeName("沥青");
                        break;
                    case "101":
                        wztaizhangVo.setTypeName("钢筋原材");
                        break;
                    case "102":
                        wztaizhangVo.setTypeName("钢绞线");
                        break;
                    case "103":
                        wztaizhangVo.setTypeName("钢管");
                        break;
                    case "104":
                        wztaizhangVo.setTypeName("钢筋机械连接接头");
                        break;
                    case "105":
                        wztaizhangVo.setTypeName("钢立柱");
                        break;
                    case "106":
                        wztaizhangVo.setTypeName("支座");
                        break;
                    case "107":
                        wztaizhangVo.setTypeName("护栏");
                        break;
                    case "108":
                        wztaizhangVo.setTypeName("锚夹具");
                        break;
                    case "109":
                        wztaizhangVo.setTypeName("中空锚杆");
                        break;
                    case "110":
                        wztaizhangVo.setTypeName("波纹管");
                        break;
                    case "111":
                        wztaizhangVo.setTypeName("防水板");
                        break;
                    case "112":
                        wztaizhangVo.setTypeName("止水带");
                        break;
                    case "113":
                        wztaizhangVo.setTypeName("土工布");
                        break;
                }

                wztaizhangVo.setOrgCode(orgCode);
                wztaizhangVo.setNodetype(nodetype);
                wztaizhangVo.setJingzhongt(one.getJingzhongt());
                wztaizhangVo.setBhgJingzhong(two.getMaozhongt());
                wztaizhangVo.setBhgJingzhongZb(bhgJingzhongZb);
                wztaizhangVo.setPici(one.getPici());
                wztaizhangVo.setBhgPici(two.getPizhongt());
                wztaizhangVo.setBhgPiciZb(bhgPiciZb);
                wztaizhangVo.setBhl(bhl);
                data.add(wztaizhangVo);
            }

        }
        String type = "钢筋原材,钢绞线,中空锚杆,钢筋机械连接接头,钢管,波纹管,防水板,止水带,土工布";
        String[] types = type.split(",");

        for (int i = 0; i < types.length; i++) {
            WztaizhangVo wztaizhangVo1 = new WztaizhangVo();
            wztaizhangVo1.setTypeName(types[i]);
            wztaizhangVo1.setOrgCode(orgCode);
            wztaizhangVo1.setNodetype("200" + i);
            wztaizhangVo1.setJingzhongt("0");
            wztaizhangVo1.setBhgJingzhong("0");
            wztaizhangVo1.setBhgJingzhongZb("0.00%");
            wztaizhangVo1.setPici("0");
            wztaizhangVo1.setBhgPici("0");
            wztaizhangVo1.setBhgPiciZb("0.00%");
            wztaizhangVo1.setBhl("-");
            data.add(wztaizhangVo1);
        }
        return Result.OK(data);
    }

    /**
     * 项目原材料类型不合格率统计
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @param nodetype
     * @return
     */
    @AutoLog(value = "wztaizhang-材料类型合格柱状图接口")
    @ApiOperation(value = "wztaizhang-材料类型合格柱状图接口", notes = "wztaizhang-材料类型合格柱状图接口")
    @GetMapping(value = "/list8")
    public Result<?> queryCailiaoDetails(Wztaizhang wztaizhang,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req,
                                         String nodetype) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId()));
        //查询他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }

        List<String> gsOrgcodeList = wztaizhangService.getOrgCodeList("3");//公司级别的orgcode
        List<String> projOrgCodeList = wztaizhangService.getOrgCodeList("4");//项目级别的orgcode
        List<WztaizhangVo> wztaizhangvoList = new ArrayList<>();
        for (String projOrgCode : projOrgCodeList) {
            WztaizhangVo wztaizhangVo = new WztaizhangVo();
            String departName = wztaizhangService.getDepartName(projOrgCode);//获取项目名称
            for (String gsOrgcode : gsOrgcodeList) {
                if (projOrgCode.indexOf(gsOrgcode) != -1) {//projOrgcode包含gsOrgcode，即项目有所属公司
                    String gsDepartName = wztaizhangService.getDepartName(gsOrgcode);//获取公司名称
                    wztaizhangVo.setGsName(gsDepartName);
                    break;
                } else {
                    wztaizhangVo.setGsName("null");
                }
            }

            int sumPici = 0;
            int sumBhgPici = 0;
            String piciNum = wztaizhangService.selectPici(projOrgCode, nodetype);//根据设备编号查找该设备下原材料的总批次数
            if (piciNum == null) {
                piciNum = "0";
            }
            int pici = Integer.parseInt(piciNum);
            if (pici != 0) {
                sumPici += pici;
            }
            String bhgPiciNum = wztaizhangService.selectBhgPici(projOrgCode, nodetype);//根据设备编号查找该设备下原材料的不合格批次数
            if (bhgPiciNum == null) {
                bhgPiciNum = "0";
            }
            int bhgPici = Integer.parseInt(bhgPiciNum);
            if (bhgPici != 0) {
                sumBhgPici += bhgPici;
            }
            String bhgPicizb = wztaizhangService.getPercentStr(sumBhgPici, sumPici);//不合格批次占比
            if (sumPici == 0) {
                wztaizhangVo.setPici("0");
            } else {
                wztaizhangVo.setPici(String.valueOf(sumPici));
            }
            if (sumBhgPici == 0) {
                wztaizhangVo.setBhgPici("0");
            } else {
                wztaizhangVo.setBhgPici(String.valueOf(sumBhgPici));
            }
            wztaizhangVo.setOrgCode(projOrgCode);
            wztaizhangVo.setNodetype(nodetype);
            switch (nodetype) {
                case "1":
                    wztaizhangVo.setTypeName("细集料");
                    break;
                case "2":
                    wztaizhangVo.setTypeName("粗集料");
                    break;
                case "5":
                    wztaizhangVo.setTypeName("水");
                    break;
                case "6":
                    wztaizhangVo.setTypeName("水泥");
                    break;
                case "7":
                    wztaizhangVo.setTypeName("矿粉");
                    break;
                case "8":
                    wztaizhangVo.setTypeName("粉煤灰");
                    break;
                case "9":
                    wztaizhangVo.setTypeName("外加剂");
                    break;
                case "10":
                    wztaizhangVo.setTypeName("其他");
                    break;
                case "11":
                    wztaizhangVo.setTypeName("沥青");
                    break;
                case "101":
                    wztaizhangVo.setTypeName("钢筋原材");
                    break;
                case "102":
                    wztaizhangVo.setTypeName("钢绞线");
                    break;
                case "103":
                    wztaizhangVo.setTypeName("钢管");
                    break;
                case "104":
                    wztaizhangVo.setTypeName("钢筋机械连接接头");
                    break;
                case "105":
                    wztaizhangVo.setTypeName("钢立柱");
                    break;
                case "106":
                    wztaizhangVo.setTypeName("支座");
                    break;
                case "107":
                    wztaizhangVo.setTypeName("护栏");
                    break;
                case "108":
                    wztaizhangVo.setTypeName("锚夹具");
                    break;
                case "109":
                    wztaizhangVo.setTypeName("中空锚杆");
                    break;
                case "110":
                    wztaizhangVo.setTypeName("波纹管");
                    break;
                case "111":
                    wztaizhangVo.setTypeName("防水板");
                    break;
                case "112":
                    wztaizhangVo.setTypeName("止水带");
                    break;
                case "113":
                    wztaizhangVo.setTypeName("土工布");
                    break;
            }
            wztaizhangVo.setDepartName(departName);
            wztaizhangVo.setBhgPiciZb(bhgPicizb);
            wztaizhangvoList.add(wztaizhangVo);
        }
        return Result.OK(wztaizhangvoList);
    }

    /**
     * 原材料不合格清单
     *
     * @param wztaizhang
     * @param nodetype
     * @param orgcode
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-原材料不合格清单")
    @ApiOperation(value = "wztaizhang-原材料不合格清单", notes = "wztaizhang-原材料不合格清单")
    @GetMapping(value = "/selectBhgInf")
    public Result<?> selectBhgInfo(Wztaizhang wztaizhang,
                                   String nodetype,
                                   String orgcode,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        List<WztaizhangBdVo> wztaizhangBdVoList = new ArrayList<>();
        WztaizhangBdVo wztaizhangBdVo = new WztaizhangBdVo();//存放项目下的总不合格数据
        List<WztaizhangThreeVo> wztaizhangThreeVoList = new ArrayList<>();
        int xiangmuCount = Integer.parseInt(wztaizhangService.selectBhgPici(orgcode, nodetype));//项目下的总不合格批次数

        List<String> projOrgcodeList = wztaizhangService.selectOrgcode("4", orgcode);//通过公司orgcode查到项目列表
        if (projOrgcodeList.size() != 0) {
            for (String projOrgcode : projOrgcodeList) {
                String gsName = wztaizhangService.selectGsName(projOrgcode);//获取公司名称
                String projName = wztaizhangService.getDepartName(projOrgcode);//获取项目名称
                List<String> bdOrgcodeList = wztaizhangService.selectOrgcode("7", projOrgcode);//通过项目orgcode查到该项目下的标段列表
                if (bdOrgcodeList.size() != 0) {
                    for (String bdOrgcode : bdOrgcodeList) {
                        WztaizhangBdVo wztaizhangBdVo1 = new WztaizhangBdVo();
                        String bdName = wztaizhangService.getDepartName(bdOrgcode);//根据标段orgcode查询标段名字
                        List<Wztaizhang> bhgList = wztaizhangService.selectBhgPiciByBd(nodetype, bdOrgcode);//查找标段下的该种材料类型的所有不合格pici
                        ArrayList<WztaizhangThreeVo> wztaizhangThreeVoList1 = new ArrayList<>();
                        if (bhgList.size() != 0) {
                            int bdCount = bhgList.size();
                            for (Wztaizhang bhgWzt : bhgList) {
                                WztaizhangThreeVo wztaizhangThreeVo = new WztaizhangThreeVo();//总
                                WztaizhangThreeVo wztaizhangThreeVo1 = new WztaizhangThreeVo();//分
                                if (bhgWzt.getCailiaono() != null) {
                                    String nodetype1 = wztaizhangService.selectNodetypeByCailiaono(bhgWzt.getCailiaono());
                                    switch (nodetype1) {
                                        case "1":
                                            wztaizhangThreeVo.setTypeName("细集料");
                                            break;
                                        case "2":
                                            wztaizhangThreeVo.setTypeName("粗集料");
                                            break;
                                        case "5":
                                            wztaizhangThreeVo.setTypeName("水");
                                            break;
                                        case "6":
                                            wztaizhangThreeVo.setTypeName("水泥");
                                            break;
                                        case "7":
                                            wztaizhangThreeVo.setTypeName("矿粉");
                                            break;
                                        case "8":
                                            wztaizhangThreeVo.setTypeName("粉煤灰");
                                            break;
                                        case "9":
                                            wztaizhangThreeVo.setTypeName("外加剂");
                                            break;
                                        case "10":
                                            wztaizhangThreeVo.setTypeName("其他");
                                            break;
                                        case "11":
                                            wztaizhangThreeVo.setTypeName("沥青");
                                            break;
                                        case "101":
                                            wztaizhangThreeVo.setTypeName("钢筋原材");
                                            break;
                                        case "102":
                                            wztaizhangThreeVo.setTypeName("钢绞线");
                                            break;
                                        case "103":
                                            wztaizhangThreeVo.setTypeName("钢管");
                                            break;
                                        case "104":
                                            wztaizhangThreeVo.setTypeName("钢筋机械连接接头");
                                            break;
                                        case "105":
                                            wztaizhangThreeVo.setTypeName("钢立柱");
                                            break;
                                        case "106":
                                            wztaizhangThreeVo.setTypeName("支座");
                                            break;
                                        case "107":
                                            wztaizhangThreeVo.setTypeName("护栏");
                                            break;
                                        case "108":
                                            wztaizhangThreeVo.setTypeName("锚夹具");
                                            break;
                                        case "109":
                                            wztaizhangThreeVo.setTypeName("中空锚杆");
                                            break;
                                        case "110":
                                            wztaizhangThreeVo.setTypeName("波纹管");
                                            break;
                                        case "111":
                                            wztaizhangThreeVo.setTypeName("防水板");
                                            break;
                                        case "112":
                                            wztaizhangThreeVo.setTypeName("止水带");
                                            break;
                                        case "113":
                                            wztaizhangThreeVo.setTypeName("土工布");
                                            break;
                                    }
                                    String cailiaoName = wztaizhangService.selectCailiaoName(bhgWzt.getCailiaono());
                                    wztaizhangThreeVo.setCailiaono(cailiaoName);
                                }
                                wztaizhangThreeVo.setId(bhgWzt.getId());
                                wztaizhangThreeVo.setGsName(gsName);
                                wztaizhangThreeVo.setProjName(projName);
                                wztaizhangThreeVo.setBdName(bdName);
                                wztaizhangThreeVo.setGuigexinghao(bhgWzt.getGuigexinghao());
                                wztaizhangThreeVo.setPici(bhgWzt.getPici());
                                wztaizhangThreeVo.setJinchangshijian(bhgWzt.getJinchangshijian());
                                wztaizhangThreeVo.setJingzhongt(bhgWzt.getJingzhongt());
                                wztaizhangThreeVo.setCreateTime(bhgWzt.getCreateTime());
                                wztaizhangThreeVo.setOverproofStatus(bhgWzt.getOverproofStatus());
                                wztaizhangThreeVoList.add(wztaizhangThreeVo);
                                wztaizhangBdVo.setRecords(wztaizhangThreeVoList);

                                if (bhgWzt.getSysOrgCode().equals(bdOrgcode)) {
                                    if (bhgWzt.getCailiaono() != null) {
                                        String nodetype2 = wztaizhangService.selectNodetypeByCailiaono(bhgWzt.getCailiaono());
                                        switch (nodetype2) {
                                            case "1":
                                                wztaizhangThreeVo1.setTypeName("细集料");
                                                break;
                                            case "2":
                                                wztaizhangThreeVo1.setTypeName("粗集料");
                                                break;
                                            case "5":
                                                wztaizhangThreeVo1.setTypeName("水");
                                                break;
                                            case "6":
                                                wztaizhangThreeVo1.setTypeName("水泥");
                                                break;
                                            case "7":
                                                wztaizhangThreeVo1.setTypeName("矿粉");
                                                break;
                                            case "8":
                                                wztaizhangThreeVo1.setTypeName("粉煤灰");
                                                break;
                                            case "9":
                                                wztaizhangThreeVo1.setTypeName("外加剂");
                                                break;
                                            case "10":
                                                wztaizhangThreeVo1.setTypeName("其他");
                                                break;
                                            case "11":
                                                wztaizhangThreeVo1.setTypeName("沥青");
                                                break;
                                            case "101":
                                                wztaizhangThreeVo1.setTypeName("钢筋原材");
                                                break;
                                            case "102":
                                                wztaizhangThreeVo1.setTypeName("钢绞线");
                                                break;
                                            case "103":
                                                wztaizhangThreeVo1.setTypeName("钢管");
                                                break;
                                            case "104":
                                                wztaizhangThreeVo1.setTypeName("钢筋机械连接接头");
                                                break;
                                            case "105":
                                                wztaizhangThreeVo1.setTypeName("钢立柱");
                                                break;
                                            case "106":
                                                wztaizhangThreeVo1.setTypeName("支座");
                                                break;
                                            case "107":
                                                wztaizhangThreeVo1.setTypeName("护栏");
                                                break;
                                            case "108":
                                                wztaizhangThreeVo1.setTypeName("锚夹具");
                                                break;
                                            case "109":
                                                wztaizhangThreeVo1.setTypeName("中空锚杆");
                                                break;
                                            case "110":
                                                wztaizhangThreeVo1.setTypeName("波纹管");
                                                break;
                                            case "111":
                                                wztaizhangThreeVo1.setTypeName("防水板");
                                                break;
                                            case "112":
                                                wztaizhangThreeVo1.setTypeName("止水带");
                                                break;
                                            case "113":
                                                wztaizhangThreeVo1.setTypeName("土工布");
                                                break;
                                        }
                                        String cailiaoName = wztaizhangService.selectCailiaoName(bhgWzt.getCailiaono());
                                        wztaizhangThreeVo1.setCailiaono(cailiaoName);
                                    }
                                    wztaizhangThreeVo1.setId(bhgWzt.getId());
                                    wztaizhangThreeVo1.setGsName(gsName);
                                    wztaizhangThreeVo1.setProjName(projName);
                                    wztaizhangThreeVo1.setBdName(bdName);
                                    wztaizhangThreeVo1.setGuigexinghao(bhgWzt.getGuigexinghao());
                                    wztaizhangThreeVo1.setPici(bhgWzt.getPici());
                                    wztaizhangThreeVo1.setJinchangshijian(bhgWzt.getJinchangshijian());
                                    wztaizhangThreeVo1.setJingzhongt(bhgWzt.getJingzhongt());
                                    wztaizhangThreeVo1.setCreateTime(bhgWzt.getCreateTime());
                                    wztaizhangThreeVo1.setOverproofStatus(bhgWzt.getOverproofStatus());
                                    wztaizhangThreeVoList1.add(wztaizhangThreeVo1);
                                }
                            }
                            wztaizhangBdVo1.setName(bdName);
                            wztaizhangBdVo1.setCount(bdCount);
                            wztaizhangBdVo1.setRecords(wztaizhangThreeVoList1);
                            wztaizhangBdVoList.add(wztaizhangBdVo1);
                        } else {
                            continue;
                        }
                    }
                }
            }
            wztaizhangBdVo.setName("总数");
            wztaizhangBdVo.setCount(xiangmuCount);
            wztaizhangBdVo.setRecords(wztaizhangThreeVoList);
            wztaizhangBdVoList.add(0, wztaizhangBdVo);
        }
        return Result.OK(wztaizhangBdVoList);
    }

    /**
     * 原材料总合格率、闭合率
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-原材料总合格率、闭合率")
    @ApiOperation(value = "wztaizhang-原材料总合格率、闭合率", notes = "wztaizhang-原材料总合格率、闭合率")
    @GetMapping(value = "/queryPassrate")
    public Result<?> queryCailiaoPassrate(Wztaizhang wztaizhang,
                                          String orgCode,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId()));
        //查询他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }

        String bhgPici = "";
        String pici = "";
        Integer overproofStatus = 0;
        if (orgCode != null) {
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("count(jianyanstate) as jianyanstate");
//            queryWrapper.eq("jianyanstate", 2);
            queryWrapper.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
            queryWrapper.likeRight("sys_org_code", orgCode);
            Wztaizhang one = wztaizhangService.getOne(queryWrapper);
            bhgPici = String.valueOf(one.getJianyanstate());//不合格批次数

            QueryWrapper<Wztaizhang> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("count(pici) as pici");
            queryWrapper1.likeRight("sys_org_code", orgCode);
            Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
            pici = one1.getPici();//总批次数

            QueryWrapper<Wztaizhang> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.select("count(overproof_status) as overproofStatus");
            queryWrapper2.eq("overproof_status", 20);
            queryWrapper2.likeRight("sys_org_code", orgCode);
            Wztaizhang one2 = wztaizhangService.getOne(queryWrapper2);
            overproofStatus = one2.getOverproofStatus();//闭合数
        } else {
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("count(jianyanstate) as jianyanstate");
//            queryWrapper.eq("jianyanstate", 2);
            queryWrapper.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
            Wztaizhang one = wztaizhangService.getOne(queryWrapper);
            bhgPici = String.valueOf(one.getJianyanstate());//不合格批次数

            QueryWrapper<Wztaizhang> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("count(pici) as pici");
            Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
            pici = one1.getPici();//总批次数

            QueryWrapper<Wztaizhang> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.select("count(overproof_status) as overproofStatus");
            queryWrapper2.eq("overproof_status", 20);
            Wztaizhang one2 = wztaizhangService.getOne(queryWrapper2);
            overproofStatus = one2.getOverproofStatus();//闭合数
        }

        int zpici = Integer.valueOf(pici).intValue();
        int zbhgpici = Integer.valueOf(bhgPici).intValue();

        String passrate = wztaizhangService.getPercentStr(Integer.valueOf(zpici - zbhgpici), Integer.parseInt(pici));//合格批次占比
        String bhl = wztaizhangService.getPercentStr(overproofStatus, Integer.parseInt(pici));//闭合率

        WztaizhangVo wztaizhangVo = new WztaizhangVo();
        wztaizhangVo.setPici(pici);
        wztaizhangVo.setBhgPici(bhgPici);
        wztaizhangVo.setPassrate(passrate);
        wztaizhangVo.setBhl(bhl);

        return Result.OK(wztaizhangVo);
    }

    /**
     * 添加
     *
     * @param wztaizhang
     * @return
     */
    @AutoLog(value = "wztaizhang-添加")
    @ApiOperation(value = "wztaizhang-添加", notes = "wztaizhang-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Wztaizhang wztaizhang) {

        // 如果料仓编号不为空 则修改料仓状态
        if (StringUtils.isNotBlank(wztaizhang.getLcbianhao())) {
            QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("guid", wztaizhang.getLcbianhao());
            Wzliaocang one = wzliaocangService.getOne(queryWrapper);
            // 4 为待检验
            one.setPici(wztaizhang.getPici());//批次编号
            one.setPicizhong(wztaizhang.getJingzhongt());// 净重
            one.setKucun(wztaizhang.getJingzhongt());
            one.setLiaocangStatus("4");
            one.setJinchangTime(wztaizhang.getJinchangshijian());
            wzliaocangService.updateById(one);
            // 新增进场数据后修改状态为关门，并修改净重
          //  upstatus(one,false);

        }
        // 进料时同步数据到wztaizhang_lc


        wztaizhangService.save(wztaizhang);
        return Result.OK("添加成功！");
    }

    // udp协议更新双控料仓重量并关闭仓门
    public void upstatus( Wzliaocang one,Boolean door){

        if(StringUtils.isNotBlank(one.getBhzbh())){
            // 修改后重量
            String pram = "line"+one.getPicurls()+"_silo"+one.getBhzbh();
            Integer zhong =(int) (Double.parseDouble(one.getKucun()) *10);// 单位0.1t    放大10位广播，例如15.6t  广播为156
            String get = HttpRequest.get("http://121.40.127.185:8606/update/weight?"+pram+"="+zhong)
                    .execute()
                    .body();
//            JSONObject jsonObject = new JSONObject(get);
//            String result = jsonObject.get("Result").toString();
           // 修改舱门
            String get2 = HttpRequest.get("http://121.40.127.185:8606/update/door?"+pram+"="+door)
                    .execute()
                    .body();
            // 发送重量广播
            String get3 = HttpRequest.get("http://121.40.127.185:8606/broadcast/weight?"+pram+"="+door)
                    .execute()
                    .body();
            // 舱门关闭广播
            String get4 = HttpRequest.get("http://121.40.127.185:8606/broadcast/door?"+pram+"="+door)
                    .execute()
                    .body();
        }
    }

    /**
     * 编辑
     *
     * @param wztaizhang
     * @return
     */
    @AutoLog(value = "wztaizhang-编辑")
    @ApiOperation(value = "wztaizhang-编辑", notes = "wztaizhang-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Wztaizhang wztaizhang) {
        // 监理审核通过 修改料仓 状态为 合格
        if (wztaizhang.getJianyanstate() == 4 && StringUtils.isNotBlank(wztaizhang.getLcbianhao())) {
            QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("guid", wztaizhang.getLcbianhao());
            Wzliaocang one = wzliaocangService.getOne(queryWrapper);
            // 3为合格
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            one.setJianyanTime(time);
            one.setLiaocangStatus("3");
            wzliaocangService.updateById(one);
            // 审核通过后修改料仓门打开
          //  upstatus(one,true);

        }
        wztaizhangService.updateById(wztaizhang);
        return Result.OK("编辑成功!");
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wztaizhang-通过id删除")
    @ApiOperation(value = "wztaizhang-通过id删除", notes = "wztaizhang-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wztaizhangService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wztaizhang-批量删除")
    @ApiOperation(value = "wztaizhang-批量删除", notes = "wztaizhang-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wztaizhangService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wztaizhang-通过id查询")
    @ApiOperation(value = "wztaizhang-通过id查询", notes = "wztaizhang-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Wztaizhang wztaizhang = wztaizhangService.getById(id);
        if (wztaizhang == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wztaizhang);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wztaizhang
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wztaizhang wztaizhang) {
        return super.exportXls(request, wztaizhang, Wztaizhang.class, "wztaizhang");
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
        return super.importExcel(request, response, Wztaizhang.class);
    }

//    /**
//     * 重点材料
//     *
//     * @return
//     */
//    @RequestMapping(value = "/zdCailiao", method = RequestMethod.GET)
//    public Result<?> zdCailiao() {
//        return Result.OK(wztaizhangService.zhongdianCailiao());
//    }
//
//    @AutoLog(value = "wztaizhang-看板一级接口查询")
//    @ApiOperation(value = "wztaizhang-看板一级接口查询", notes = "wztaizhang-看板一级接口查询")
//    @GetMapping(value = "/queryList")
//    public Result<?> queryList(
//            String sys_depart_orgcode,
//            Integer type,
//            Integer timeType,
//            HttpServletRequest req) {
//        String nodeType = null;
//        Wztaizhang taizhang = new Wztaizhang();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar c = Calendar.getInstance();
//        String jinchangshijian_begin = null;
//        String jinchangshijian_end = format.format(c.getTime());
//        if (timeType == 0) {
//            c.set(Calendar.DAY_OF_YEAR, 1);//1:本年第一天
//            jinchangshijian_begin = format.format(c.getTime());
//        } else if (timeType == 1) {
//            c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
//            jinchangshijian_begin = format.format(c.getTime());
//        }
//        if (sys_depart_orgcode == null) {
//            sys_depart_orgcode = "A";
//        }
//        Map<Object, Object> map = new HashMap<>();
//
//        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
//        queryWrapper.eq("jianyanstate", 2);
//        queryWrapper.ne("nodetype", 11);
//        queryWrapper.gt("jinchangshijian", jinchangshijian_begin);
//        queryWrapper.lt("jinchangshijian", jinchangshijian_end);
//        queryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
//        List<Wztaizhang> wztaizhangList = wztaizhangService.list(queryWrapper);
//        List<WztaizhangRCVO> rcvoList = new LinkedList<>();
//        for (Wztaizhang wztaizhang : wztaizhangList) {
//            WztaizhangRCVO wztaizhangRCVO = new WztaizhangRCVO();
//            String projectName = bhzCementBaseMapper.selectbyorgcode(wztaizhang.getSysOrgCode(), 4);
//            if (wztaizhang.getGblx() == 0) {
//                Wzcailiaonamedict wzcailiaonamedict1 = wzcailiaonamedictService.queryselectone1(wztaizhang.getCailiaono());
//                if (wzcailiaonamedict1 != null) {
//                    if (wzcailiaonamedict1.getCailiaoname() != null) {
//                        wztaizhangRCVO.setProductName(wzcailiaonamedict1.getCailiaoname());
//                    }
//                    if (wzcailiaonamedict1.getChandi() != null) {
//                        wztaizhangRCVO.setLocalName(wzcailiaonamedict1.getChandi());
//                    }
//                } else {
//                    continue;
//                }
//            } else if (wztaizhang.getGblx() == 1) {
//                WzcailiaonamedictMan wzcailiaonamedict = wzcailiaonamedictManService.queryselectone1(wztaizhang.getCailiaono());
//                if (wzcailiaonamedict != null) {
//                    if (wzcailiaonamedict.getCailiaoname() != null) {
//                        wztaizhangRCVO.setProductName(wzcailiaonamedict.getCailiaoname());
//                    }
//                    if (wzcailiaonamedict.getChandi() != null) {
//                        wztaizhangRCVO.setLocalName(wzcailiaonamedict.getChandi());
//                    }
//                } else {
//                    continue;
//                }
//            }
//
//            wztaizhangRCVO.setProjectName(projectName);
//
//            YclTestDetail yclTestDetail = yclTestDetailService.getByILN(wztaizhang.getPici());
//            if (yclTestDetail != null) {
//                wztaizhangRCVO.setBuhegeName(yclTestDetail.getConclusion());
//            }
//            if (wztaizhang.getGblx() == 0) {
//                Wzgongyingshang wzgongyingshang = wzgongyingshangService.selectnameone(wztaizhang.getGongyingshangdanweibianma());
//                if (wzgongyingshang != null) {
//                    wztaizhangRCVO.setProducerName(wzgongyingshang.getGongyingshangname());
//                } else {
//                    wztaizhangRCVO.setProducerName(null);
//                }
//            } else if (wztaizhang.getGblx() == 1) {
//                WzgongyingshangMan wzgongyingshang = wzgongyingshangManService.selectnameone(wztaizhang.getGongyingshangdanweibianma());
//                if (wzgongyingshang != null) {
//                    wztaizhangRCVO.setProducerName(wzgongyingshang.getGongyingshangname());
//                } else {
//                    wztaizhangRCVO.setProducerName(null);
//                }
//            }
//            rcvoList.add(wztaizhangRCVO);
//        }
//        QueryWrapper<Wztaizhang> queryWrapper1 = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
//        queryWrapper1.select("count(1) AS jianyanstate");
//        queryWrapper1.gt("jinchangshijian", jinchangshijian_begin);
//        queryWrapper1.lt("jinchangshijian", jinchangshijian_end);
//        queryWrapper1.likeRight("sys_org_code", sys_depart_orgcode);
//        Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
//
//        QueryWrapper<Wztaizhang> queryWrapper2 = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
//        queryWrapper2.select("count(1) AS jianyanstate");
//        queryWrapper2.eq("jianyanstate", 1);
//        queryWrapper2.gt("jinchangshijian", jinchangshijian_begin);
//        queryWrapper2.lt("jinchangshijian", jinchangshijian_end);
//        queryWrapper2.likeRight("sys_org_code", sys_depart_orgcode);
//        Wztaizhang one2 = wztaizhangService.getOne(queryWrapper2);
//
//        QueryWrapper<Wztaizhang> queryWrapper3 = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
//        queryWrapper3.select("count(1) AS jianyanstate");
//        queryWrapper3.eq("jianyanstate", 2);
//        queryWrapper3.gt("jinchangshijian", jinchangshijian_begin);
//        queryWrapper3.lt("jinchangshijian", jinchangshijian_end);
//        queryWrapper3.likeRight("sys_org_code", sys_depart_orgcode);
//        Wztaizhang one3 = wztaizhangService.getOne(queryWrapper3);
//
//        QueryWrapper<Wztaizhang> queryWrapper4 = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
//        queryWrapper4.select("count(1) AS jianyanstate");
//        queryWrapper4.ne("jianyanstate", 0);
//        queryWrapper4.gt("jinchangshijian", jinchangshijian_begin);
//        queryWrapper4.lt("jinchangshijian", jinchangshijian_end);
//        queryWrapper4.likeRight("sys_org_code", sys_depart_orgcode);
//        Wztaizhang one4 = wztaizhangService.getOne(queryWrapper4);
//
//        Double hegelv = 0.00D;
//        if (((double) one2.getJianyanstate() + (double) one3.getJianyanstate()) == 0) {
//            hegelv = Double.valueOf(100);
//        } else if (((double) one2.getJianyanstate() + (double) one3.getJianyanstate()) > 0 && one2.getJianyanstate() == 0) {
//            hegelv = Double.valueOf(0);
//        } else {
//            hegelv = ((double) one2.getJianyanstate() / ((double) one2.getJianyanstate() + (double) one3.getJianyanstate()));
//            BigDecimal b = new BigDecimal(hegelv * 100);
//            hegelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        }
//        map.put("pcjc", one4.getJianyanstate());
//        map.put("hgNum", one2.getJianyanstate());
//        map.put("bhgNum", one3.getJianyanstate());
//        map.put("hegelv", hegelv);
//        map.put("detail", rcvoList);
//
//        return Result.OK(map);
//    }

    /**
     * 按照标段分组
     *
     * @param sys_depart_orgcode
     * @param type
     * @param timeType
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-看板一级接口查询")
    @ApiOperation(value = "wztaizhang-看板一级接口查询", notes = "wztaizhang-看板一级接口查询")
    @GetMapping(value = "/queryList")
    public Result<?> queryList(
            String sys_depart_orgcode,
            Integer type,
            Integer timeType,
            HttpServletRequest req) {
        String nodeType = null;
        Wztaizhang taizhang = new Wztaizhang();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String jinchangshijian_begin = null;
        // 追加闭合数
        double bhs = 0.0;
        String date = LocalDate.now().toString();
        String[] split = date.split("-");
        String jinchangshijian_end = format.format(c.getTime());
        if (timeType == 0) {
            c.set(Calendar.DAY_OF_YEAR, 1);//1:本年第一天
            jinchangshijian_begin = format2.format(c.getTime());
            bhs = yclTestTaizhangService.getBhCount(split[0] , sys_depart_orgcode);
        } else if (timeType == 1) {
            c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
            jinchangshijian_begin = format2.format(c.getTime());
            bhs = yclTestTaizhangService.getBhCount(split[0] + "-" + split[1], sys_depart_orgcode);
        }else {
            bhs = yclTestTaizhangService.getBhCount("" , sys_depart_orgcode);
        }
        if (sys_depart_orgcode == null) {
            sys_depart_orgcode = "A";
        }
        Map<Object, Object> map = new HashMap<>();
        List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(sys_depart_orgcode);
        List<WztaizhangRCVO> rcvoList = new LinkedList<>();
        for (Map<String, Object> listOrgCode : listOrgCodes) {
            String orgCode2 = listOrgCode.get("org_code").toString();
            String departName1 = listOrgCode.get("depart_name_abbr").toString();
            String departName2 = listOrgCode.get("depart_name").toString();
            QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
//            queryWrapper.eq("jianyanstate", 2);

            queryWrapper.ne("nodetype", 11);
            queryWrapper.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
            queryWrapper.ge(StringUtils.isNotBlank(jinchangshijian_begin),"jinchangshijian", jinchangshijian_begin);
            queryWrapper.le("jinchangshijian", jinchangshijian_end);
            queryWrapper.likeRight("sys_org_code", orgCode2);
            List<Wztaizhang> wztaizhangList = wztaizhangService.list(queryWrapper);
            for (Wztaizhang wztaizhang : wztaizhangList) {
                WztaizhangRCVO wztaizhangRCVO = new WztaizhangRCVO();
                if (wztaizhang.getGblx() == 0) {
                    Wzcailiaonamedict wzcailiaonamedict1 = wzcailiaonamedictService.queryselectone1(wztaizhang.getCailiaono());
                    if (wzcailiaonamedict1 != null) {
                        if (wzcailiaonamedict1.getCailiaoname() != null) {
                            wztaizhangRCVO.setProductName(wzcailiaonamedict1.getCailiaoname());
                        }
                        if (wzcailiaonamedict1.getChandi() != null) {
                            wztaizhangRCVO.setLocalName(wzcailiaonamedict1.getChandi());
                        }
                    } else {
                        continue;
                    }
                } else if (wztaizhang.getGblx() == 1) {
                    WzcailiaonamedictMan wzcailiaonamedict = wzcailiaonamedictManService.queryselectone1(wztaizhang.getCailiaono());
                    if (wzcailiaonamedict != null) {
                        if (wzcailiaonamedict.getCailiaoname() != null) {
                            wztaizhangRCVO.setProductName(wzcailiaonamedict.getCailiaoname());
                        }
                        if (wzcailiaonamedict.getChandi() != null) {
                            wztaizhangRCVO.setLocalName(wzcailiaonamedict.getChandi());
                        }
                    } else {
                        continue;
                    }
                }

                wztaizhangRCVO.setSectionName(departName2);
                wztaizhangRCVO.setProjectName(departName1);

                YclTestDetail yclTestDetail = yclTestDetailService.getByILNBHG(wztaizhang.getPici());
                if (yclTestDetail != null) {
                    wztaizhangRCVO.setBuhegeName(yclTestDetail.getConclusion());
                }
                if (wztaizhang.getGblx() == 0) {
                    Wzgongyingshang wzgongyingshang = wzgongyingshangService.selectnameone(wztaizhang.getGongyingshangdanweibianma());
                    if (wzgongyingshang != null) {
                        wztaizhangRCVO.setProducerName(wzgongyingshang.getGongyingshangname());
                    } else {
                        wztaizhangRCVO.setProducerName(null);
                    }
                } else if (wztaizhang.getGblx() == 1) {
                    WzgongyingshangMan wzgongyingshang = wzgongyingshangManService.selectnameone(wztaizhang.getGongyingshangdanweibianma());
                    if (wzgongyingshang != null) {
                        wztaizhangRCVO.setProducerName(wzgongyingshang.getGongyingshangname());
                    } else {
                        wztaizhangRCVO.setProducerName(null);
                    }
                }
                rcvoList.add(wztaizhangRCVO);
            }
        }
//        QueryWrapper<Wztaizhang> queryWrapper1 = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
//        queryWrapper1.select("count(1) AS jianyanstate");
//        queryWrapper1.ge(StringUtils.isNotBlank(jinchangshijian_begin),"jinchangshijian", jinchangshijian_begin);
//        queryWrapper1.le("jinchangshijian", jinchangshijian_end);
//        queryWrapper1.likeRight("sys_org_code", sys_depart_orgcode);
//        Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
//
//        QueryWrapper<Wztaizhang> queryWrapper2 = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
//        queryWrapper2.select("count(1) AS jianyanstate");
//
//        queryWrapper2.ge(StringUtils.isNotBlank(jinchangshijian_begin),"jinchangshijian", jinchangshijian_begin);
//        queryWrapper2.le("jinchangshijian", jinchangshijian_end);
//        queryWrapper2.likeRight("sys_org_code", sys_depart_orgcode);
//        queryWrapper2.and(wrapper -> wrapper.eq("jianyanstate", 1).or().eq("choujianstate", 1));
//        Wztaizhang one2 = wztaizhangService.getOne(queryWrapper2);

//        QueryWrapper<Wztaizhang> queryWrapper3 = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
//        queryWrapper3.select("count(1) AS jianyanstate");
//        queryWrapper3.ge(StringUtils.isNotBlank(jinchangshijian_begin),"jinchangshijian", jinchangshijian_begin);
//        queryWrapper3.le("jinchangshijian", jinchangshijian_end);
//        queryWrapper3.likeRight("sys_org_code", sys_depart_orgcode);
//        queryWrapper3.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
//        Wztaizhang one3 = wztaizhangService.getOne(queryWrapper3);

        // 完成试验批次数
        QueryWrapper<Wztaizhang> queryWrapper4 = QueryGenerator.initQueryWrapper(taizhang, req.getParameterMap());
        queryWrapper4.select(" SUM( if(jianyanstate = 2 OR  choujianstate = 2 ,1,0) ) choujianstate,\n" +
                "       SUM(if ( jianyanstate in (1,2)  OR  choujianstate in (1,2) ,1,0 )  ) jianyanstate ");
         queryWrapper4.ge(StringUtils.isNotBlank(jinchangshijian_begin),"jinchangshijian", jinchangshijian_begin);
        queryWrapper4.le("jinchangshijian", jinchangshijian_end);
        queryWrapper4.likeRight("sys_org_code", sys_depart_orgcode);
        Wztaizhang one4 = wztaizhangService.getOne(queryWrapper4);
        if(ObjectUtil.isEmpty(one4)){
            one4 = new Wztaizhang();
            one4.setJianyanstate(0);
            one4.setChoujianstate(0);
        }

        Double hegelv = 0.00D;
        Integer hegesum = one4.getJianyanstate() - one4.getChoujianstate();
        if (((double) hegesum + (double) one4.getChoujianstate()) == 0) {
            hegelv = Double.valueOf(100);
        } else if (((double) hegesum+ (double) one4.getChoujianstate()) > 0 && hegesum == 0) {
            hegelv = Double.valueOf(0);
        } else {
            hegelv = ((double) hegesum / (double) one4.getJianyanstate());
            BigDecimal b = new BigDecimal(hegelv * 100);
            hegelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        map.put("pcjc", one4.getJianyanstate() );
        map.put("hgNum", hegesum );
        map.put("bhgNum", one4.getChoujianstate());
        map.put("hegelv", hegelv);
        map.put("detail", rcvoList);
        DecimalFormat df = new DecimalFormat("0.00");
        if(bhs>0 && one4.getChoujianstate() >0){
            map.put("bhlv", df.format((bhs/ (double)one4.getChoujianstate())*100)+"%" );
        }else{
            map.put("bhlv", "-%" );
        }

        return Result.OK(map);
    }

    @AutoLog(value = "wztaizhang-原材及产品合格率")
    @ApiOperation(value = "wztaizhang-原材及产品合格率", notes = "wztaizhang-原材及产品合格率")
    @GetMapping(value = "/queryList2")
    public Result<?> queryList2(Wztaizhang wztaizhang,
                                Integer timeType,
                                Integer type,
                                String sys_depart_orgcode,
                                HttpServletRequest req) {
        if (sys_depart_orgcode == null) {
            sys_depart_orgcode = "A";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String jinchangshijian_begin = null;
        String jinchangshijian_end = format.format(c.getTime());
        if (timeType == 1) {
            c.set(Calendar.DAY_OF_YEAR, 1);//1:本年第一天
            jinchangshijian_begin = format.format(c.getTime());
        } else if (timeType == 0) {
            c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
            jinchangshijian_begin = format.format(c.getTime());
        }
        Map<Object, Object> map = new HashMap();
        //noteType 100前是原材，100后是产品
//        List<String> CPList = wzcailiaonamedictManService.selectListByYCCP(100, 0, "A");
//        List<String> CPList2 = wzcailiaonamedictService.selectListByYCCP(100, 0, "A");
//        if (CPList2.size() > 0) {
//            CPList.addAll(CPList2);
//        }
//        List<String> YCList = wzcailiaonamedictManService.selectListByYCCP(0, 100, "A");
//        List<String> YCList2 = wzcailiaonamedictService.selectListByYCCP(0, 100, "A");
//        if (YCList2.size() > 0) {
//            YCList.addAll(YCList2);
//        }

        //原材不合格数
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper.select("count(1) AS jianyanstate");
//        queryWrapper.eq("jianyanstate", 2);
        queryWrapper.le("nodetype", 100);
        queryWrapper.ne("nodetype", 11);
        queryWrapper.gt("jinchangshijian", jinchangshijian_begin);
        queryWrapper.lt("jinchangshijian", jinchangshijian_end);
        queryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
        queryWrapper.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
        Wztaizhang one = wztaizhangService.getOne(queryWrapper);
        Integer ycbheNum = one.getJianyanstate();
        //原材合格数
        QueryWrapper<Wztaizhang> queryWrapper1 = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper1.select("count(1) AS jianyanstate");
//        queryWrapper1.eq("jianyanstate", 1);

        queryWrapper1.le("nodetype", 100);
        queryWrapper1.ne("nodetype", 11);
        queryWrapper1.gt("jinchangshijian", jinchangshijian_begin);
        queryWrapper1.lt("jinchangshijian", jinchangshijian_end);
        queryWrapper1.likeRight("sys_org_code", sys_depart_orgcode);
        queryWrapper1.and(wrapper -> wrapper.eq("jianyanstate", 1).or().eq("choujianstate", 1));
        Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
        Integer ycHegeNum = one1.getJianyanstate();
        Double ychegelv = 0.00D;
        if ((ycHegeNum + ycbheNum) == 0) {
            ychegelv = Double.valueOf(100);
        } else if ((ycHegeNum + ycbheNum) > 0 && ycHegeNum == 0) {
            ychegelv = Double.valueOf(0);
        } else {
            ychegelv = ((double) ycHegeNum / (double) (ycHegeNum + ycbheNum));
            BigDecimal b = new BigDecimal(ychegelv * 100);
            ychegelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //产品不合格数

        QueryWrapper<Wztaizhang> queryWrapper2 = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper2.select("count(1) AS jianyanstate");
//        queryWrapper2.eq("jianyanstate", 2);
        queryWrapper2.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
        queryWrapper2.ge("nodetype", 100);
        queryWrapper2.gt("jinchangshijian", jinchangshijian_begin);
        queryWrapper2.lt("jinchangshijian", jinchangshijian_end);
        queryWrapper2.likeRight("sys_org_code", sys_depart_orgcode);
        Wztaizhang one2 = wztaizhangService.getOne(queryWrapper2);
        Integer cpbhgSum = one2.getJianyanstate();
        //产品合格数
        QueryWrapper<Wztaizhang> queryWrapper3 = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper3.select("count(1) AS jianyanstate");
//        queryWrapper3.eq("jianyanstate", 1);

        queryWrapper3.ne("choujianstate", 2);
        queryWrapper3.ge("nodetype", 100);
        queryWrapper3.and(wrapper -> wrapper.eq("jianyanstate", 1).or().eq("choujianstate", 1));
//        queryWrapper3.gt("cailiaoNo", 100);
        queryWrapper3.gt("jinchangshijian", jinchangshijian_begin);
        queryWrapper3.lt("jinchangshijian", jinchangshijian_end);
        queryWrapper3.likeRight("sys_org_code", sys_depart_orgcode);
        Wztaizhang one3 = wztaizhangService.getOne(queryWrapper3);
        Integer cpHegeNum = one3.getJianyanstate();
        Double cphegelv = 0.00D;
        if (cpbhgSum + cpHegeNum == 0) {
            cphegelv = Double.valueOf(100);
        } else if (cpbhgSum + cpHegeNum > 0 && cpHegeNum == 0) {
            cphegelv = Double.valueOf(0);
        } else {
            cphegelv = ((double) cpHegeNum / (double) (cpbhgSum + cpHegeNum));
            BigDecimal b = new BigDecimal(cphegelv * 100);
            cphegelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
//        List<String> cailiaoNoList = wzcailiaonamedictManService.selectListByYCCP(0, 0, "A");
//        if (type == 1) {
//            cailiaoNoList = CPList;
//        } else if (type == 0) {
//            cailiaoNoList = YCList;
//        }
        Integer ycrule = 0;
        Integer cprule = 0;
        if (type == 1) {
            cprule = 100;
        } else if (type == 0) {
            ycrule = 100;
        }
        List<Map> mapList2 = new LinkedList<>();
        List<Map> mapList = wztaizhangService.selectmapList(jinchangshijian_begin, jinchangshijian_end, cprule, ycrule);
        if (mapList.size() > 0 && mapList != null) {
            for (Map map1 : mapList) {
                String nodetype = map1.get("nodetype").toString();
                String hege = map1.get("hege").toString();
                String name = wztaizhangService.selectBydict(nodetype);
                if (name != null) {
                    map1.put("cailiaoName", name);
                } else {
                    continue;
                }
                if (!hege.equals(0)) {
                    mapList2.add(map1);
                }
            }
        }
        map.put("cailiao", ychegelv);
        map.put("product", cphegelv);
        map.put("list", mapList2);
        return Result.OK(map);
    }

    @AutoLog(value = "wztaizhang-原材及产品曝光台")
    @ApiOperation(value = "wztaizhang-原材及产品曝光台", notes = "wztaizhang-原材及产品曝光台")
    @GetMapping(value = "/queryList3")
    public Result<?> queryList3(
            String sys_depart_orgcode,
            Integer type,
            HttpServletRequest req) {
        if (sys_depart_orgcode == null) {
            sys_depart_orgcode = "A";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String jinchangshijian_begin = null;
        String jinchangshijian_end = format.format(c.getTime());
        if (type == 1) {
            c.set(Calendar.DAY_OF_YEAR, 1);//1:本年第一天
            jinchangshijian_begin = format.format(c.getTime());
        } else if (type == 0) {
            c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
            jinchangshijian_begin = format.format(c.getTime());
        }
        //0：原材；1：产品
//        List<String> cailiaoNoList = wzcailiaonamedictManService.selectListByYCCP(100, 0, "A");
//        List<String> cailiaoNoList2 = wzcailiaonamedictService.selectListByYCCP(100, 0, "A");
//        if (cailiaoNoList2.size() > 0) {
//            cailiaoNoList.addAll(cailiaoNoList2);
//        }

        List<Map> mapList = new LinkedList<>();
        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("nodetype", 100);
        queryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
        queryWrapper.gt("jinchangshijian", jinchangshijian_begin);
        queryWrapper.lt("jinchangshijian", jinchangshijian_end);
        queryWrapper.orderByDesc("jinchangshijian");
        queryWrapper.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
        List<Wztaizhang> list = wztaizhangService.list(queryWrapper);
        for (Wztaizhang wztaizhang1 : list) {
            Map map = new HashMap();
            if (wztaizhang1.getNodetype() != null) {
                String name = wztaizhangService.selectBydict(wztaizhang1.getNodetype());
                if (name != null) {
                    map.put("productName", name);
                } else {
                    continue;
                }
            } else {
                continue;
            }
            map.put("guigexinghao", wztaizhang1.getGuigexinghao());
            if (wztaizhang1.getGblx() == 0) {
                Wzgongyingshang wzgongyingshang1 = wzgongyingshangService.selectnameone(wztaizhang1.getGongyingshangdanweibianma());
                if (wzgongyingshang1 != null) {
                    map.put("producerName", wzgongyingshang1.getGongyingshangname());
                } else {
                    map.put("producerName", null);
                }
            } else if (wztaizhang1.getGblx() == 1) {
                WzgongyingshangMan wzgongyingshang = wzgongyingshangManService.selectnameone(wztaizhang1.getGongyingshangdanweibianma());
                if (wzgongyingshang != null) {
                    map.put("producerName", wzgongyingshang.getGongyingshangname());
                } else {
                    map.put("producerName", null);
                }
            }

            map.put("overproofStatus", wztaizhang1.getOverproofStatus());

            String projectName = bhzCementBaseMapper.selectbyorgcode2(wztaizhang1.getSysOrgCode(), 4);
            if (projectName != null) {
                map.put("projectName", projectName);
            } else {
                map.put("projectName", null);
            }
            String biaoduanName = bhzCementBaseMapper.selectbyorgcode2(wztaizhang1.getSysOrgCode(), 7);
            if (biaoduanName != null) {
                map.put("biaoduanName", biaoduanName);
            } else {
                map.put("biaoduanName", null);
            }
            YclTestDetail yclTestDetail = yclTestDetailService.getByILNBHG(wztaizhang1.getPici());
            if (yclTestDetail != null) {
                map.put("warnTime", format1.format(yclTestDetail.getTestTime()) );
                map.put("reason", yclTestDetail.getConclusion());
            } else {
                map.put("warnTime", null);
                map.put("reason", null);
            }
            mapList.add(map);
        }

        return Result.OK(mapList);
    }

    @AutoLog(value = "wztaizhang-原材及产品不合格信息")
    @ApiOperation(value = "wztaizhang-原材及产品不合格信息", notes = "wztaizhang-原材及产品不合格信息")
    @GetMapping(value = "/queryList4")
    public Result<?> queryList4(Wztaizhang wztaizhang,
                                String sys_depart_orgcode,
                                Integer type,
                                HttpServletRequest req) {


        //cailiaoNo 100前是原材，100后是产品
        if (sys_depart_orgcode == null) {
            sys_depart_orgcode = "A";
        }

        List<Object> ll = (List<Object>) redisUtil.get("wztaizhang-wztaizhang-queryList4-type="+type+":"+sys_depart_orgcode);
        if(ObjectUtil.isNotNull(ll) && ll.size()>0){
            return Result.OK(ll);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        String jinchangshijian_begin = null;
        String jinchangshijian_end = format.format(c.getTime());
        if (type == 1) {
            c.set(Calendar.DAY_OF_YEAR, 1);//1:本年第一天
            jinchangshijian_begin = format.format(c.getTime());
        } else if (type == 0) {
            c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
            jinchangshijian_begin = format.format(c.getTime());
        }
        List<Map> mapList = new LinkedList<>();

        //0：原材；1：产品
//        List<String> cailiaoNoList = wzcailiaonamedictManService.selectListByYCCP(0, 100, "A");
//        List<String> cailiaoNoList2 = wzcailiaonamedictService.selectListByYCCP(100, 0, "A");
//        if (cailiaoNoList2.size() > 0) {
//            cailiaoNoList.addAll(cailiaoNoList2);
//        }

        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("nodetype", 100);
        queryWrapper.ne("nodetype", 11);
        queryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
        queryWrapper.ge("jinchangshijian", jinchangshijian_begin);
        queryWrapper.le("jinchangshijian", jinchangshijian_end);
        queryWrapper.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
        List<Wztaizhang> list = wztaizhangService.list(queryWrapper);
        for (Wztaizhang wztaizhang1 : list) {
            Map map = new HashMap();
            if (wztaizhang1.getNodetype() != null) {
                String name = wztaizhangService.selectBydict(wztaizhang1.getNodetype());
                if (name != null) {
                    map.put("productName", name);
                } else {
                    continue;
                }
            } else {
                continue;
            }
            map.put("guigexinghao", wztaizhang1.getGuigexinghao());
            if (wztaizhang1.getGblx() == 0) {
                Wzgongyingshang wzgongyingshang1 = wzgongyingshangService.selectnameone(wztaizhang1.getGongyingshangdanweibianma());
                if (wzgongyingshang1 != null) {
                    map.put("producerName", wzgongyingshang1.getGongyingshangname());
                } else {
                    map.put("producerName", null);
                }
            } else if (wztaizhang1.getGblx() == 1) {
                WzgongyingshangMan wzgongyingshang = wzgongyingshangManService.selectnameone(wztaizhang1.getGongyingshangdanweibianma());
                if (wzgongyingshang != null) {
                    map.put("producerName", wzgongyingshang.getGongyingshangname());
                } else {
                    map.put("producerName", null);
                }
            }

            map.put("overproofStatus", wztaizhang1.getOverproofStatus());

            String projectName = bhzCementBaseMapper.selectbyorgcode2(wztaizhang1.getSysOrgCode(), 4);
            if (projectName != null) {
                map.put("projectName", projectName);
            } else {
                map.put("projectName", null);
            }
            String biaoduanName = bhzCementBaseMapper.selectbyorgcode2(wztaizhang1.getSysOrgCode(), 7);
            if (biaoduanName != null) {
                map.put("biaoduanName", biaoduanName);
            } else {
                map.put("biaoduanName", null);
            }
            YclTestDetail yclTestDetail = yclTestDetailService.getByILNBHG(wztaizhang1.getPici());
            if (yclTestDetail != null) {
                map.put("warnTime", yclTestDetail.getTestTime());
                map.put("reason", yclTestDetail.getConclusion());
            } else {
                map.put("warnTime", null);
                map.put("reason", null);
            }
            mapList.add(map);
        }
        redisUtil.set("wztaizhang-wztaizhang-queryList4-type="+type+":"+sys_depart_orgcode, mapList, 500);
        return Result.OK(mapList);
    }

    @AutoLog(value = "wztaizhang-原材及产品合格率概况")
    @ApiOperation(value = "wztaizhang-原材及产品合格率概况", notes = "wztaizhang-原材及产品合格率概况")
    @GetMapping(value = "/queryList5")
    public Result<?> queryList5(
            String sys_depart_orgcode,
            HttpServletRequest req) {
        List<Map> result = new LinkedList<>();
        Integer ycrule = 100;
        Integer cprule = 100;

        //原材
        List<Map> mapList = wztaizhangService.selectByYearList(ycrule, 0);
        //产品
        List<Map> mapList2 = wztaizhangService.selectByYearList(0, cprule);
        //总体
        List<Map> mapList3 = wztaizhangService.selectByYearList(0, 0);
        for (int i = 0; i < 5; i++) {

            Double ychegelv = 0.00D;
            Map map = new HashMap<>();
            map.put("Year", mapList3.get(i).get("itemYear"));
            Number allCount = (Number) mapList3.get(i).get("count");
            Number hegeCount = (Number) mapList3.get(i).get("hegeCount");
            Number ycCount = (Number) mapList.get(i).get("count");
            Number ychegeCount = (Number) mapList.get(i).get("hegeCount");
            Number cpCount = (Number) mapList2.get(i).get("count");
            Number cphegeCount = (Number) mapList2.get(i).get("hegeCount");
            if (ycCount.intValue() == 0) {
                ychegelv = Double.valueOf(100);
            } else if (ycCount.intValue() > 0 && ychegeCount.intValue() == 0) {
                ychegelv = Double.valueOf(0);
            } else {
                ychegelv = ((double) ychegeCount.intValue() / (double) ycCount.intValue());
                BigDecimal b = new BigDecimal(ychegelv * 100);
                ychegelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            map.put("ychegelv", ychegelv);

            Double cphegelv = 0.00D;
            if (cpCount.intValue() == 0) {
                cphegelv = Double.valueOf(100);
            } else if (cpCount.intValue() > 0 && cphegeCount.intValue() == 0) {
                cphegelv = Double.valueOf(0);
            } else {
                cphegelv = ((double) cphegeCount.intValue() / (double) cpCount.intValue());
                BigDecimal b = new BigDecimal(cphegelv * 100);
                cphegelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            map.put("cphegelv", cphegelv);

            Double allhegelv = 0.00D;
            if (allCount.intValue() == 0) {
                allhegelv = Double.valueOf(100);
            } else if (allCount.intValue() > 0 && hegeCount.intValue() == 0) {
                allhegelv = Double.valueOf(0);
            } else {
                allhegelv = ((double) hegeCount.intValue() / (double) allCount.intValue());
                BigDecimal b = new BigDecimal(allhegelv * 100);
                allhegelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            map.put("allhegelv", allhegelv);

            result.add(map);
        }

        return Result.OK(result);
    }

    @AutoLog(value = "wztaizhang-原材及产品弹窗")
    @ApiOperation(value = "wztaizhang-原材及产品弹窗", notes = "wztaizhang-原材及产品弹窗")
    @GetMapping(value = "/queryList6")
    public Result<?> queryList6(String cailiaoName,
                                String sys_org_code,
                                String beginTime,
                                String endTime) {
        //0：原材；1：产品
//        String cailiaoNo = null;
//        if(type == 0){
//            cailiaoNo = wzcailiaonamedictService.selectByYCCP(ycrule,0);
//        }else if(type == 1){
//            cailiaoNo = wzcailiaonamedictService.selectByYCCP(0,cprule);
//        }
        if (sys_org_code == null) {
            sys_org_code = "A";
        }
        //原材
        List<WztaizhangKBVO> result = new LinkedList<>();
        List<Map> mapList = wztaizhangService.selectByProject(sys_org_code);
        for (Map map : mapList) {
            String code = map.get("org_code").toString();
            WztaizhangKBVO kbvo = wztaizhangService.selectNumByCode(code, beginTime, endTime, cailiaoName);
//            if(kbvo.getAllPici() == 0){
//                continue;
//            }
            kbvo.setProjectName(map.get("depart_name").toString());
            WztaizhangKBVO wztaizhangKBVO = countLv(kbvo);
            List<Map> maps = wztaizhangService.selectBiaoduanBycode(code);
            List<WztaizhangKBVO> wztaizhangKBVOList = new LinkedList<>();
            for (Map map1 : maps) {
                String BDCode = map1.get("org_code").toString();
                WztaizhangKBVO kbvo1 = wztaizhangService.selectNumByCode(BDCode, beginTime, endTime, cailiaoName);
//                if(kbvo1.getAllPici() == 0){
//                    continue;
//                }
                kbvo1.setProjectName(map1.get("depart_name").toString());
                WztaizhangKBVO wztaizhangKBVO1 = countLv(kbvo1);
                wztaizhangKBVOList.add(wztaizhangKBVO1);
            }
            wztaizhangKBVO.setBiaoduanList(wztaizhangKBVOList);
            result.add(wztaizhangKBVO);
        }
        return Result.OK(result);
    }

    public WztaizhangKBVO countLv(WztaizhangKBVO kbvo) {

        Double hegelv = 0.00D;
        if ((kbvo.getUnPici() + kbvo.getHegePici()) == 0) {
            hegelv = Double.valueOf(100);
        } else if ((kbvo.getUnPici() + kbvo.getHegePici()) > 0 && kbvo.getHegePici() == 0) {
            hegelv = Double.valueOf(0);
        } else {
            hegelv = ((double) kbvo.getHegePici() / (double) (kbvo.getUnPici() + kbvo.getHegePici()));
            BigDecimal b = new BigDecimal(hegelv * 100);
            hegelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        kbvo.setHegelv(hegelv);

        Double unPiciLv = 0.00D;
        if (kbvo.getAllPici() == 0) {
            unPiciLv = Double.valueOf(100);
        } else if (kbvo.getAllPici() > 0 && kbvo.getUnPici() == 0) {
            unPiciLv = Double.valueOf(0);
        } else {
            unPiciLv = ((double) kbvo.getUnPici() / (double) kbvo.getAllPici());
            BigDecimal b = new BigDecimal(unPiciLv * 100);
            unPiciLv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        kbvo.setUnPiciLv(unPiciLv);

        Double unWeightLv = 0.00D;
        if (kbvo.getAllWeight() == 0) {
            unWeightLv = Double.valueOf(100);
        } else if (kbvo.getAllWeight() > 0 && kbvo.getUnWeight() == 0) {
            unWeightLv = Double.valueOf(0);
        } else {
            unWeightLv = ((double) kbvo.getUnWeight() / (double) kbvo.getAllWeight());
            BigDecimal b = new BigDecimal(unWeightLv * 100);
            unWeightLv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        kbvo.setUnWeightLv(unWeightLv);
        return kbvo;
    }

    @AutoLog(value = "wztaizhang-原材及产品不合格信息/曝光台 弹窗")
    @ApiOperation(value = "wztaizhang-原材及产品不合格信息/曝光台 弹窗", notes = "wztaizhang-原材及产品不合格信息/曝光台 弹窗")
    @GetMapping(value = "/queryList7")
    public Result<?> queryList7(Wztaizhang wztaizhang,
                                String sys_org_code,
                                String beginTime,
                                String endTime,
                                Integer type,
                                HttpServletRequest req) {
        //cailiaoNo 100前是原材，100后是产品
        if (sys_org_code == null) {
            sys_org_code = "A";
        }
        //0：原材；1：产品
//        List<String> cailiaoNoList = wzcailiaonamedictManService.selectListByYCCP(0, 0, sys_org_code);
//        if (type == 1) {
//            cailiaoNoList = wzcailiaonamedictManService.selectListByYCCP(100, 0, sys_org_code);
//            ;
//        } else if (type == 0) {
//            cailiaoNoList = wzcailiaonamedictManService.selectListByYCCP(0, 100, sys_org_code);
//            ;
//        }
        List<Map> mapList = new LinkedList<>();
        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("jianyanstate", 2);
        queryWrapper.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
        if (type == 1) {
            queryWrapper.ge("nodetype", 100);
        } else if (type == 0) {
            queryWrapper.le("nodetype", 100);
            queryWrapper.ne("nodetype", 11);
        }
        queryWrapper.likeRight("sys_org_code", sys_org_code);
        queryWrapper.ge("jinchangshijian", beginTime);
        queryWrapper.le("jinchangshijian", endTime);
        queryWrapper.orderByDesc("jinchangshijian");
        List<Wztaizhang> list = wztaizhangService.list(queryWrapper);
        for (Wztaizhang wztaizhang1 : list) {
            Map map = new HashMap();
            if (wztaizhang1.getNodetype() != null) {
                String name = wztaizhangService.selectBydict(wztaizhang1.getNodetype());
                if (name != null) {
                    map.put("productName", name);
                } else {
                    continue;
                }
            } else {
                continue;
            }
            map.put("guigexinghao", wztaizhang1.getGuigexinghao());
            if (wztaizhang1.getGblx() == 0) {
                Wzgongyingshang wzgongyingshang1 = wzgongyingshangService.selectnameone(wztaizhang1.getGongyingshangdanweibianma());
                if (wzgongyingshang1 != null) {
                    map.put("producerName", wzgongyingshang1.getGongyingshangname());
                } else {
                    map.put("producerName", null);
                }
            } else if (wztaizhang1.getGblx() == 1) {
                WzgongyingshangMan wzgongyingshang = wzgongyingshangManService.selectnameone(wztaizhang1.getGongyingshangdanweibianma());
                if (wzgongyingshang != null) {
                    map.put("producerName", wzgongyingshang.getGongyingshangname());
                } else {
                    map.put("producerName", null);
                }
            }

            map.put("overproofStatus", wztaizhang1.getOverproofStatus());

            String projectName = bhzCementBaseMapper.selectbyorgcode2(wztaizhang1.getSysOrgCode(), 4);
            if (projectName != null) {
                map.put("projectName", projectName);
            } else {
                map.put("projectName", null);
            }
            String biaoduanName = bhzCementBaseMapper.selectbyorgcode2(wztaizhang1.getSysOrgCode(), 7);
            if (biaoduanName != null) {
                map.put("biaoduanName", biaoduanName);
            } else {
                map.put("biaoduanName", null);
            }
            YclTestDetail yclTestDetail = yclTestDetailService.getByILNBHG(wztaizhang1.getPici());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (yclTestDetail != null) {
                map.put("warnTime",format.format(yclTestDetail.getTestTime()) );
                map.put("reason", yclTestDetail.getConclusion());
            } else {
                continue;
            }
            mapList.add(map);
        }

        return Result.OK(mapList);
    }

    @AutoLog(value = "wztaizhang-原材及产品弹窗(左)")
    @ApiOperation(value = "wztaizhang-原材及产品弹窗(左)", notes = "wztaizhang-原材及产品弹窗(左)")
    @GetMapping(value = "/queryList8")
    public Result<?> queryList8(String sys_org_code,
                                String beginTime,
                                String endTime,
                                Integer timeType,
                                String cailiaoName,
                                Integer type) {
        //cailiaoNo 100前是原材，100后是产品
        if (sys_org_code == null) {
            sys_org_code = "A";
        }
        //0：原材；1：产品
        Integer ycrule = 0;
        Integer cprule = 0;
        if (type == 1) {
            cprule = 100;
        } else if (type == 0) {
            ycrule = 100;
        }
        //noteType 100前是原材，100后是产品

        List<String> stringList = null;
//        if (type == 1) {
//            stringList = wzcailiaonamedictManService.selectListByYCCP(100, 0, "A");
//            ;
//        } else if (type == 0) {
//            stringList = wzcailiaonamedictManService.selectListByYCCP(0, 100, "A");
//            ;
//        }
        if (cailiaoName != null) {
            stringList = wztaizhangService.selectByDictValue(cailiaoName);
        }
        Calendar calendarTemp = Calendar.getInstance();
        int monthTemp = calendarTemp.get(Calendar.MONTH) + 1;
        List<Map> mapList = wztaizhangService.selectByProject(sys_org_code);
        for (Map map : mapList) {
            String code = map.get("org_code").toString();
            Double proCount = wztaizhangService.selectCountByPro(code, beginTime, endTime, cprule, ycrule, stringList);

            BigDecimal b = new BigDecimal(proCount * 100);
            map.put("ProLv", b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            if (b.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            if (timeType == 1) {
                List<Map> sonList = wztaizhangService.selectMonthByCode(code, beginTime, endTime, cprule, ycrule, stringList);
                for (Map map1 : sonList) {
                    BigDecimal hegeLv = (BigDecimal) map1.get("hegeLv");
                    BigDecimal b1 = new BigDecimal(100);
                    double value = b1.multiply(hegeLv).doubleValue();
                    if (value == 0) {
                        value = 100;
                    }
                    map1.put("hegeLv", value);
                }
                List<Map> mapList1 = new LinkedList<>();
                for (int i = 0; i < monthTemp; i++) {
                    mapList1.add(sonList.get(i));
                }
                map.put("sonList", mapList1);
            } else if (timeType == 0) {
                List<Map> sonList = wztaizhangService.selectMLvByCode(code, beginTime, endTime, cprule, ycrule, stringList);
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH) + 1;
                for (Map map1 : sonList) {
                    map1.put("month", month + "月");
                    BigDecimal hegeLv = (BigDecimal) map1.get("hegeLv");
                    BigDecimal b1 = new BigDecimal(100);
                    double value = b1.multiply(hegeLv).doubleValue();
                    if (value == 0) {
                        value = 100;
                    }
                    map1.put("hegeLv", value);
                }
                map.put("sonList", sonList);
            }
        }
        Collections.sort(mapList, new Comparator<Map>() {
            @Override
            public int compare(Map map1, Map map2) {
                Double JM = (Double) map1.get("ProLv");
                Double JM1 = (Double) map2.get("ProLv");
                return JM1.compareTo(JM);
            }
        });
        for (Map map : mapList) {
            Double hegeLv = (Double) map.get("ProLv");
            if (hegeLv == 0) {
                map.put("ProLv", "-");
            }
        }
        return Result.OK(mapList);
    }


    @AutoLog(value = "wztaizhang-原材及产品弹窗(右)")
    @ApiOperation(value = "wztaizhang-原材及产品弹窗(右)", notes = "wztaizhang-原材及产品弹窗(右)")
    @GetMapping(value = "/queryList9")
    public Result<?> queryList9(String sys_org_code,
                                String beginTime,
                                String endTime,
                                Integer timeType,
                                Integer type) {
        //cailiaoNo 100前是原材，100后是产品
        if (sys_org_code == null) {
            sys_org_code = "A";
        }
        //0：原材；1：产品
        Integer ycrule = 0;
        Integer cprule = 0;
        if (type == 1) {
            cprule = 100;
        } else if (type == 0) {
            ycrule = 100;
        }
        Calendar calendarTemp = Calendar.getInstance();
        int monthTemp = calendarTemp.get(Calendar.MONTH) + 1;
        List<Map> resultList = new LinkedList<>();
        List<Map> mapList = wztaizhangService.selectDictList(cprule, ycrule);
        for (Map map : mapList) {
            String cailiaoNo = map.get("cailiaoNo").toString();
            String[] split = cailiaoNo.split(",");
            List<String> stringList = Arrays.asList(split);
            Double CLCount = wztaizhangService.selectCountByCl(stringList, sys_org_code, beginTime, endTime);

            BigDecimal decimal = new BigDecimal(CLCount * 100);
            map.put("cailiaoLv", decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            if (timeType == 1) {
                List<Map> sonList = wztaizhangService.selectMonthByCode(sys_org_code, beginTime, endTime, cprule, ycrule, stringList);
                for (Map map1 : sonList) {
                    BigDecimal hegeLv = (BigDecimal) map1.get("hegeLv");
                    BigDecimal b1 = new BigDecimal(100);
                    double value = b1.multiply(hegeLv).doubleValue();
                    if (value == 0) {
                        value = 100;
                    }
                    map1.put("hegeLv", value);
                }
                List<Map> mapList1 = new LinkedList<>();
                for (int i = 0; i < monthTemp; i++) {
                    mapList1.add(sonList.get(i));
                }
                map.put("sonList", mapList1);
            } else if (timeType == 0) {
                List<Map> sonList = wztaizhangService.selectMLvByCode(sys_org_code, beginTime, endTime, cprule, ycrule, stringList);
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH) + 1;
                for (Map map1 : sonList) {
                    map1.put("month", month + "月");
                    BigDecimal hegeLv = (BigDecimal) map1.get("hegeLv");
                    BigDecimal b1 = new BigDecimal(100);
                    double value = b1.multiply(hegeLv).doubleValue();
                    if (value == 0) {
                        value = 100;
                    }
                    map1.put("hegeLv", value);
                }
                map.put("sonList", sonList);
            }
            if (decimal.compareTo(BigDecimal.ZERO) != 0) {
                resultList.add(map);
            }
        }
        Collections.sort(resultList, new Comparator<Map>() {
            @Override
            public int compare(Map map1, Map map2) {
                Double JM = (Double) map1.get("cailiaoLv");
                Double JM1 = (Double) map2.get("cailiaoLv");
                return JM1.compareTo(JM);
            }
        });
        return Result.OK(resultList);
    }

    @AutoLog(value = "wztaizhang-原材及产品名称列表")
    @ApiOperation(value = "wztaizhang-原材及产品名称列表", notes = "wztaizhang-原材及产品名称列表")
    @GetMapping(value = "/queryList10")
    public Result<?> queryList10(String sys_org_code,
                                 Integer type) {
        //cailiaoNo 100前是原材，100后是产品
        if (sys_org_code == null) {
            sys_org_code = "A";
        }
        //0：原材；1：产品
        Integer ycrule = 0;
        Integer cprule = 0;
        if (type == 1) {
            cprule = 100;
        } else if (type == 0) {
            ycrule = 100;
        }
        List<Map> mapList = wztaizhangService.selectDictList(cprule, ycrule);
        if (type == 0) {
            for (int i = 0; i < mapList.size(); i++) {
                if (mapList.get(i).get("cailiaoName") != null) {
                    String cailiaoName = mapList.get(i).get("cailiaoName").toString();
                    if (cailiaoName.equals("水泥")) {
                        Map map = new HashMap();
                        map = mapList.get(0);
                        mapList.set(0, mapList.get(i));
                        mapList.set(i, map);
                        break;
                    }
                }
            }
        } else if (type == 1) {
            for (int i = 0; i < mapList.size(); i++) {
                if (mapList.get(i).get("cailiaoName") != null) {
                    String cailiaoName = mapList.get(i).get("cailiaoName").toString();
                    if (cailiaoName.equals("中空锚杆")) {
                        Map map = new HashMap();
                        map = mapList.get(0);
                        mapList.set(0, mapList.get(i));
                        mapList.set(i, map);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < mapList.size(); i++) {
                if (mapList.get(i).get("cailiaoName") != null) {
                    String cailiaoName = mapList.get(i).get("cailiaoName").toString();
                    if (cailiaoName.equals("水泥")) {
                        Map map = new HashMap();
                        map = mapList.get(0);
                        mapList.set(0, mapList.get(i));
                        mapList.set(i, map);
                        break;
                    }
                }
            }
        }

        return Result.ok(mapList);
    }

    @AutoLog(value = "wztaizhang-项目级原材及产品")
    @ApiOperation(value = "wztaizhang-项目级原材及产品", notes = "wztaizhang-项目级原材及产品")
    @GetMapping(value = "/queryList11")
    public Result<?> queryList11(String orgCode,
                                 Integer type) {
        if (orgCode == null) {
            orgCode = "A";
        }
        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(jianyanstate) as jianyanstate");
//        queryWrapper.eq("jianyanstate", 2);
        queryWrapper.likeRight("sys_org_code", orgCode);
        queryWrapper.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
        Wztaizhang one = wztaizhangService.getOne(queryWrapper);
        String bhgPici = String.valueOf(one.getJianyanstate());//不合格批次数

        QueryWrapper<Wztaizhang> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("count(pici) as pici");
        queryWrapper1.ne("jianyanstate", 0);
        queryWrapper1.ne("jianyanstate", 3);
        queryWrapper1.likeRight("sys_org_code", orgCode);
        Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
        String pici = one1.getPici();//总批次数

        QueryWrapper<Wztaizhang> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select("count(overproof_status) as overproofStatus");
//        queryWrapper2.eq("jianyanstate", 2);
        queryWrapper2.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
        queryWrapper2.eq("overproof_status", 20);
        queryWrapper2.likeRight("sys_org_code", orgCode);
        Wztaizhang one2 = wztaizhangService.getOne(queryWrapper2);
        Integer overproofStatus = one2.getOverproofStatus();//闭合数

        QueryWrapper<Wztaizhang> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.select("count(jianyanstate) as jianyanstate");
//        queryWrapper3.eq("jianyanstate", 1);
        queryWrapper3.likeRight("sys_org_code", orgCode);
        queryWrapper3.and(wrapper -> wrapper.eq("jianyanstate", 1).or().eq("choujianstate", 1));
        Wztaizhang one3 = wztaizhangService.getOne(queryWrapper3);
        String hgPici = String.valueOf(one3.getJianyanstate());//合格批次数
        //合格批次占比
        double passrate = 0.0D;
        if (Double.parseDouble(hgPici) == 0) {
            passrate = Double.valueOf(100);
        } else if (Double.parseDouble(hgPici) > 0 && (Double.parseDouble(hgPici) + Double.parseDouble(bhgPici)) == 0) {
            passrate = Double.valueOf(0);
        } else {
            passrate = Double.parseDouble(hgPici) / (Double.parseDouble(hgPici) + Double.parseDouble(bhgPici));
            BigDecimal b = new BigDecimal(passrate * 100);
            passrate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        //闭合率
        double bhl = 0.0D;
        if (Double.parseDouble(bhgPici) == 0) {
            bhl = Double.valueOf(100);
        } else if (Double.parseDouble(bhgPici) > 0 && Double.valueOf(overproofStatus) == 0) {
            bhl = Double.valueOf(0);
        } else {
            bhl = Double.valueOf(overproofStatus) / Double.parseDouble(bhgPici);
            BigDecimal b1 = new BigDecimal(bhl * 100);
            bhl = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }


        Map map = new HashMap();
        map.put("passrate", passrate);
        map.put("bhl", bhl);
        return Result.ok(map);
    }

    @AutoLog(value = "wztaizhang-项目级原材及产品合格率")
    @ApiOperation(value = "wztaizhang-项目级原材及产品合格率", notes = "wztaizhang-项目级原材及产品合格率")
    @GetMapping(value = "/queryList12")
    public Result<?> queryList12(String cailiaoName,
                                 String sys_org_code,
                                 String beginTime,
                                 String endTime) {
        if (sys_org_code == null) {
            sys_org_code = "A";
        }
        if (beginTime == null) {
            beginTime = "0000-00-00 00:00:00";
        }
        if (endTime == null) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            endTime = sdf.format(date);
        }
        List<String> cailiaoNo = null;
        if (cailiaoName != null) {
            cailiaoNo = wztaizhangService.selectByDictValue(cailiaoName);
        }
        List<Map> maps = wztaizhangService.selectBiaoduanBycode(sys_org_code);
        List<WztaizhangKBVO> wztaizhangKBVOList = new LinkedList<>();
        for (Map map1 : maps) {
            String BDCode = map1.get("org_code").toString();
            WztaizhangKBVO kbvo1 = new WztaizhangKBVO();
            if (cailiaoNo != null) {
                kbvo1 = wztaizhangService.selectNumByCode(BDCode, beginTime, endTime, cailiaoNo.get(0));
            } else {
                kbvo1 = wztaizhangService.selectNumByCode(BDCode, beginTime, endTime, null);
            }

//                if(kbvo1.getAllPici() == 0){
//                    continue;
//                }
            kbvo1.setProjectName(map1.get("depart_name").toString());
            WztaizhangKBVO wztaizhangKBVO1 = countLv(kbvo1);
            wztaizhangKBVOList.add(wztaizhangKBVO1);
        }
        return Result.ok(wztaizhangKBVOList);
    }

    @AutoLog(value = "wztaizhang-项目级原材及产品首页")
    @ApiOperation(value = "wztaizhang-项目级原材及产品首页", notes = "wztaizhang-项目级原材及产品首页")
    @GetMapping(value = "/queryList13")
    public Result<?> queryList13(
            String sys_org_code,
            String beginTime,
            String endTime,
            String cailiaoName) {
        if (sys_org_code == null) {
            sys_org_code = "A";
        }
        if (beginTime == null) {
            beginTime = "0000-00-00 00:00:00";
        }
        if (endTime == null) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            endTime = sdf.format(date);
        }
        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(jianyanstate) as jianyanstate");
        queryWrapper.eq("jianyanstate", 2);
        queryWrapper.likeRight("sys_org_code", sys_org_code);
        queryWrapper.gt("jinchangshijian", beginTime);
        queryWrapper.le("jinchangshijian", endTime);
        Wztaizhang one = wztaizhangService.getOne(queryWrapper);
        String bhgPici = String.valueOf(one.getJianyanstate());//不合格批次数

        QueryWrapper<Wztaizhang> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("count(pici) as pici");
        queryWrapper1.likeRight("sys_org_code", sys_org_code);
        queryWrapper1.gt("jinchangshijian", beginTime);
        queryWrapper1.le("jinchangshijian", endTime);
        Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
        String pici = one1.getPici();//总批次数

        QueryWrapper<Wztaizhang> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select("count(overproof_status) as overproofStatus");
        queryWrapper2.eq("jianyanstate", 2);
        queryWrapper2.eq("overproof_status", 20);
        queryWrapper2.likeRight("sys_org_code", sys_org_code);
        queryWrapper2.gt("jinchangshijian", beginTime);
        queryWrapper2.le("jinchangshijian", endTime);
        Wztaizhang one2 = wztaizhangService.getOne(queryWrapper2);
        Integer overproofStatus = one2.getOverproofStatus();//闭合数

        QueryWrapper<Wztaizhang> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.select("count(jianyanstate) as jianyanstate");
//        queryWrapper3.eq("jianyanstate", 1);
        queryWrapper3.likeRight("sys_org_code", sys_org_code);
        queryWrapper3.and(wrapper -> wrapper.eq("jianyanstate", 1).or().eq("choujianstate", 1));
        queryWrapper3.gt("jinchangshijian", beginTime);
        queryWrapper3.le("jinchangshijian", endTime);
        Wztaizhang one3 = wztaizhangService.getOne(queryWrapper3);
        String hgPici = String.valueOf(one3.getJianyanstate());//合格批次数

        QueryWrapper<Wztaizhang> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.select("count(jianyanstate) as jianyanstate");
        queryWrapper4.ne("jianyanstate", 0);
        queryWrapper4.likeRight("sys_org_code", sys_org_code);
        queryWrapper4.gt("jinchangshijian", beginTime);
        queryWrapper4.le("jinchangshijian", endTime);
        Wztaizhang one4 = wztaizhangService.getOne(queryWrapper4);
        String yjyPici = String.valueOf(one4.getJianyanstate());//已检验批次数

        //合格批次占比
        double passrate = 0.0D;
        if (Double.parseDouble(hgPici) == 0) {
            passrate = Double.valueOf(100);
        } else if (Double.parseDouble(hgPici) > 0 && (Double.parseDouble(hgPici) + Double.parseDouble(bhgPici)) == 0) {
            passrate = Double.valueOf(0);
        } else {
            passrate = Double.parseDouble(hgPici) / (Double.parseDouble(hgPici) + Double.parseDouble(bhgPici));
            BigDecimal b = new BigDecimal(passrate * 100);
            passrate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        //闭合率
        double bhl = 0.0D;
        if (Double.parseDouble(bhgPici) != 0) {
            bhl = Double.valueOf(overproofStatus) / Double.parseDouble(bhgPici);
            BigDecimal b1 = new BigDecimal(bhl * 100);
            bhl = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        Map map = new HashMap();
        map.put("passrate", passrate);
        map.put("bhl", bhl);
        map.put("allCount", Integer.parseInt(pici));
        map.put("bhgCount", Integer.parseInt(bhgPici));


        List<String> cailiaoNo = null;
        if (cailiaoName != null) {
            cailiaoNo = wztaizhangService.selectByDictValue(cailiaoName);
        }
//        QueryWrapper<WzcailiaonamedictMan> wzcailiaonamedictQueryWrapper = new QueryWrapper<>();
//        wzcailiaonamedictQueryWrapper.eq("cailiaoName", cailiaoName);
//        wzcailiaonamedictQueryWrapper.likeRight("sys_org_code", sys_org_code);
//        List<WzcailiaonamedictMan> list = wzcailiaonamedictManService.list(wzcailiaonamedictQueryWrapper);
//        List<String> cailiaoNo = new LinkedList<>();
//        for (WzcailiaonamedictMan wzcailiaonamedict : list) {
//            cailiaoNo.add(wzcailiaonamedict.getCailiaono());
//        }
        List<Map> mapList = wztaizhangService.selectBiaoduanBycode(sys_org_code);
        for (Map map1 : mapList) {
            if (cailiaoNo != null) {
                QueryWrapper<Wztaizhang> wztaizhangQueryWrapper = new QueryWrapper<>();
                wztaizhangQueryWrapper.select("count(id) as id");
                wztaizhangQueryWrapper.eq("jianyanstate", 2);
                wztaizhangQueryWrapper.likeRight("sys_org_code", map1.get("org_code").toString());
                wztaizhangQueryWrapper.gt("jinchangshijian", beginTime);
                wztaizhangQueryWrapper.lt("jinchangshijian", endTime);
                wztaizhangQueryWrapper.in("nodetype", cailiaoNo);
                Wztaizhang wztaizhangOne = wztaizhangService.getOne(wztaizhangQueryWrapper);
                Integer bhgCount = wztaizhangOne.getId();//不合格批次数
//
//            QueryWrapper<Wztaizhang> wztaizhangQueryWrapper2 = new QueryWrapper<>();
//            wztaizhangQueryWrapper2.select("count(id) as id");
//            wztaizhangQueryWrapper2.eq("jianyanstate", 2);
//            wztaizhangQueryWrapper2.eq("overproof_status", 20);
//            wztaizhangQueryWrapper2.likeRight("sys_org_code", map1.get("org_code").toString());
//            wztaizhangQueryWrapper2.gt("jinchangshijian", beginTime);
//            wztaizhangQueryWrapper2.lt("jinchangshijian", endTime);
//            wztaizhangQueryWrapper2.in("cailiaono",cailiaoNo);
//            Wztaizhang wztaizhangOne2 = wztaizhangService.getOne(wztaizhangQueryWrapper2);
//            Integer bhCount = (wztaizhangOne2.getId());//闭合批次数

                QueryWrapper<Wztaizhang> wztaizhangQueryWrapper3 = new QueryWrapper<>();
                wztaizhangQueryWrapper3.select("count(id) as id");
                wztaizhangQueryWrapper3.ne("jianyanstate", 0);
                wztaizhangQueryWrapper3.likeRight("sys_org_code", map1.get("org_code").toString());
                wztaizhangQueryWrapper3.gt("jinchangshijian", beginTime);
                wztaizhangQueryWrapper3.lt("jinchangshijian", endTime);
                wztaizhangQueryWrapper3.in("nodetype", cailiaoNo);
                Wztaizhang wztaizhangOne3 = wztaizhangService.getOne(wztaizhangQueryWrapper3);
                Integer jyCount = (wztaizhangOne3.getId());//检验批次数

                QueryWrapper<Wztaizhang> wztaizhangQueryWrapper4 = new QueryWrapper<>();
                wztaizhangQueryWrapper4.select("count(id) as id");
//                wztaizhangQueryWrapper4.eq("jianyanstate", 1);

                wztaizhangQueryWrapper4.likeRight("sys_org_code", map1.get("org_code").toString());
                queryWrapper4.and(wrapper -> wrapper.eq("jianyanstate", 1).or().eq("choujianstate", 1));
                wztaizhangQueryWrapper4.gt("jinchangshijian", beginTime);
                wztaizhangQueryWrapper4.lt("jinchangshijian", endTime);
                wztaizhangQueryWrapper4.in("nodetype", cailiaoNo);
                Wztaizhang wztaizhangOne4 = wztaizhangService.getOne(wztaizhangQueryWrapper4);
                Integer hgCount = wztaizhangOne4.getId();//合格批次数

                QueryWrapper<Wztaizhang> wztaizhangQueryWrapper5 = new QueryWrapper<>();
                wztaizhangQueryWrapper5.select("count(id) as id");
                wztaizhangQueryWrapper5.likeRight("sys_org_code", map1.get("org_code").toString());
                wztaizhangQueryWrapper5.gt("jinchangshijian", beginTime);
                wztaizhangQueryWrapper5.lt("jinchangshijian", endTime);
                wztaizhangQueryWrapper5.in("nodetype", cailiaoNo);
                Wztaizhang wztaizhangOne5 = wztaizhangService.getOne(wztaizhangQueryWrapper5);
                Integer allCount = wztaizhangOne5.getId();//总批次数

                //闭合率

                double bhl2 = 0.0D;
                if (Double.valueOf(hgCount) == 0) {
                    bhl2 = Double.valueOf(100);
                } else if (Double.valueOf(hgPici) > 0 && (Double.valueOf(hgCount) + Double.valueOf(bhgCount)) == 0) {
                    bhl2 = Double.valueOf(0);
                } else {
                    passrate = Double.valueOf(hgCount) / (Double.valueOf(hgCount) + Double.valueOf(bhgCount));
                    BigDecimal b = new BigDecimal(passrate * 100);
                    bhl2 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }

                map1.put("bhl", bhl2);
                map1.put("useCount", 0);
                map1.put("allCount", allCount);
                map1.put("jyCount", jyCount);
            } else {
                map1.put("bhl", 0);
                map1.put("useCount", 0);
                map1.put("allCount", 0);
                map1.put("jyCount", 0);
            }
//
        }
        map.put("BDList", mapList);
        return Result.ok(map);
    }

    @AutoLog(value = "wztaizhang-项目级原材及产品首页")
    @ApiOperation(value = "wztaizhang-项目级原材及产品首页", notes = "wztaizhang-项目级原材及产品首页")
    @GetMapping(value = "/queryList14")
    public Result<?> queryList14(String org_code,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 String cailiaoName,
                                 HttpServletRequest req) {
        List<Map> resultList = new ArrayList<>();
        Map result = new HashMap();
        Wztaizhang wztaizhang = new Wztaizhang();
        List<String> cailiaoNo = null;
        if (cailiaoName != null) {
            cailiaoNo = wztaizhangService.selectByDictValue(cailiaoName);
        }
        if (cailiaoNo != null) {
            wztaizhang.setNodetype(cailiaoNo.get(0));
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper.likeRight("sys_org_code", org_code);
        queryWrapper.orderByDesc("jinchangshijian");
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        for (Wztaizhang record : pageList.getRecords()) {
            Map map = new HashMap();
            map.put("guigexinghao", record.getGuigexinghao());
            map.put("pici", record.getPici());
            map.put("jinchangshijian", record.getJinchangshijian());
            map.put("count", record.getJingzhongt());
            List<String> gongyingshang = wztaizhangService.selectByGongyingshang(record.getGongyingshangdanweibianma());
            if (gongyingshang.size() > 0) {
                map.put("gongyingshang", gongyingshang.get(0));
            } else {
                map.put("gongyingshang", null);
            }
            YclTestDetail byILN = yclTestDetailService.getByILN(record.getPici());
            if (byILN != null) {
                map.put("testTime", byILN.getTestTime());
                map.put("reportUrl", byILN.getReport());
            } else {
                map.put("testTime", null);
                map.put("reportUrl", null);
            }
            resultList.add(map);
        }
        result.put("current", pageList.getCurrent());
        result.put("pages", pageList.getPages());
        result.put("size", pageList.getSize());
        result.put("total", pageList.getTotal());
        result.put("record", resultList);
        return Result.OK(result);
    }

    @AutoLog(value = "wztaizhang-浙高建原材生产")
    @ApiOperation(value = "wztaizhang-浙高建原材生产", notes = "wztaizhang-浙高建原材生产")
    @GetMapping(value = "/YCProduct")
    public Result<?> YCProduct(
            String SysOrgCode,
            HttpServletRequest req
    ) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        //查询到他的设备编号
//        if (wztaizhang.getShebeibianhao() == null) {
//            if (!"null".equals(shebei)) {
//                wztaizhang.setShebeibianhao(shebei);
//            } else {
//                wztaizhang.setShebeibianhao("空");
//            }
//        }
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.MONTH, -1);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = sdf.format(date);
        String beginTime = sdf.format(calendar.getTime());
        if (SysOrgCode == null) {
            SysOrgCode = loginUser.getOrgCode();
        }
        Wztaizhang wztaizhang = new Wztaizhang();
        //原材名称列表
        List<String> cailiaoNoList = wzcailiaonamedictManService.selectListByYCCP(100, 0, SysOrgCode);
        if (cailiaoNoList.size() == 0) {
            Map result = new HashMap();
            result.put("yjSum", 0);
            result.put("bhCount", 0);
            result.put("bhl", 0);
            result.put("records", 0);
            return Result.ok(result);
        }

        //近三十天预警总数
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper.select("count(id) as id");
        queryWrapper.eq("jianyanstate", 2);
        queryWrapper.likeRight("sys_org_code", SysOrgCode);
        queryWrapper.gt("jinchangshijian", beginTime);
        queryWrapper.lt("jinchangshijian", endTime);
        queryWrapper.in("cailiaono", cailiaoNoList);
        Wztaizhang one = wztaizhangService.getOne(queryWrapper);
        Integer yjSum = one.getId();

        //已闭合数
        QueryWrapper<Wztaizhang> queryWrapper2 = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper2.select("count(id) as id");
        queryWrapper2.eq("jianyanstate", 2);
        queryWrapper2.likeRight("sys_org_code", SysOrgCode);
        queryWrapper2.eq("overproof_status", 20);
        queryWrapper2.gt("jinchangshijian", beginTime);
        queryWrapper2.lt("jinchangshijian", endTime);
        queryWrapper2.in("cailiaono", cailiaoNoList);
        Wztaizhang two = wztaizhangService.getOne(queryWrapper2);
        Integer bhCount = two.getId();

        //闭合率
        double bhl = 0.0D;
        if (yjSum == 0) {
            bhl = Double.valueOf(100);
        } else if (yjSum > 0 && bhCount == 0) {
            bhl = Double.valueOf(0);
        } else {
            bhl = Double.valueOf(bhCount) / Double.valueOf(yjSum);
            BigDecimal b1 = new BigDecimal(bhl * 100);
            bhl = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //预警记录
        QueryWrapper<Wztaizhang> queryWrapper3 = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper3.eq("jianyanstate", 2);
        queryWrapper3.likeRight("sys_org_code", SysOrgCode);
        queryWrapper3.gt("jinchangshijian", beginTime);
        queryWrapper3.lt("jinchangshijian", endTime);
        queryWrapper3.in("cailiaono", cailiaoNoList);
        List<Wztaizhang> list = wztaizhangService.list(queryWrapper3);
        List<Map> mapList = new LinkedList<>();
        for (Wztaizhang wztaizhang1 : list) {
            Map map = new HashMap();
            YclTestDetail yclTestDetail = yclTestDetailService.getByILN(wztaizhang1.getPici());
            if (yclTestDetail != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(yclTestDetail.getTestTime());
                map.put("warnTime", format);
                map.put("reason", yclTestDetail.getConclusion());
            } else {
                continue;
            }
            map.put("overproofStatus", wztaizhang1.getOverproofStatus());
            String projectName = bhzCementBaseMapper.selectbyorgcode2(wztaizhang1.getSysOrgCode(), 4);
            if (projectName != null) {
                map.put("projectName", projectName);
            } else {
                map.put("projectName", projectName);
            }
            map.put("id", wztaizhang1.getId());
            map.put("shebeibianhao", wztaizhang1.getShebeibianhao());
            map.put("sysOrgCode", wztaizhang1.getSysOrgCode());
            mapList.add(map);
        }
        Map result = new HashMap();
        result.put("yjSum", yjSum);
        result.put("bhCount", bhCount);
        result.put("bhl", bhl);
        result.put("records", mapList);
        return Result.ok(result);
    }

    @AutoLog(value = "wztaizhang-分页列表查询")
    @ApiOperation(value = "wztaizhang-分页列表查询", notes = "wztaizhang-分页列表查询")
    @GetMapping(value = "/listByCode")
    public Result<?> listByCode(@RequestParam("rwdcode") String rwdcode) {

        QueryWrapper<YclUsageDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("renwudan", rwdcode);
        List<YclUsageDetail> list = yclUsageDetailService.list(queryWrapper);
        List<Wztaizhang> list1 = new ArrayList<>();
        for (YclUsageDetail yclUsageDetail : list) {
            String inspectionLotNumber = yclUsageDetail.getInspectionLotNumber();
            if (StringUtils.isNotBlank(inspectionLotNumber)) {
                QueryWrapper<Wztaizhang> wrapper = new QueryWrapper<>();
                wrapper.eq("pici", inspectionLotNumber);
                List<Wztaizhang> wztaizhangs = wztaizhangService.list(wrapper);
                if (wztaizhangs.size() > 0) {
                    for (Wztaizhang wztaizhang : wztaizhangs) {
                        String shebeibianhao = wztaizhang.getShebeibianhao();
                        String cailiaono = wztaizhang.getCailiaono();
                        String lcbianhao = wztaizhang.getLcbianhao();
                        if (shebeibianhao != null) {
                            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
                            if (selectshebeione != null) {
                                wztaizhang.setShebeibianhao(selectshebeione.getSbname());
                            }
                        }
                        if (cailiaono != null) {
                            Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(cailiaono);
                            if (wzcailiaonamedict != null) {
                                wztaizhang.setCailiaono(wzcailiaonamedict.getCailiaoname());
                            }
                        }
                        if (lcbianhao != null) {
                            QueryWrapper<Wzliaocang> wzliaocangQueryWrapper = new QueryWrapper<>();
                            wzliaocangQueryWrapper.eq("guid", lcbianhao);
                            Wzliaocang wzliaocang = wzliaocangService.getOne(wzliaocangQueryWrapper);
                            if (wzliaocang != null) {
                                wztaizhang.setLcbianhao(wzliaocang.getCailiaoname());
                            }
                        }
                    }
                    list1.addAll(wztaizhangs);
                }
            }
        }
        return Result.OK(list1);
    }

    /**
     * 瑞苍隧道看板-原材产品统计
     *
     * @param
     * @param type    原材类型(1：原材 2：产品)
     * @return
     */
    @AutoLog(value = "wztaizhang-瑞苍隧道看板-原材产品统计")
    @ApiOperation(value = "wztaizhang-瑞苍隧道看板-原材产品统计", notes = "wztaizhang-瑞苍隧道看板-原材产品统计")
    @GetMapping(value = "/getTj")
    public Result<?> getTj(String wbsId, Integer type) {
        String wbsOrgCode = wztaizhangService.getWbsOrgCode(wbsId);
        List<String> nodetypeList = wztaizhangService.getNodetypeByOrgCode(wbsOrgCode);
        String sysOrgCode = new String(wbsOrgCode);

        String ycStr0 = "";//原材类型
        String cpStr0 = "";//产品类型
        for (String nodetype : nodetypeList) {
            if (nodetype != null) {
                if (nodetype.length() > 0 && nodetype.length() < 3) {
                    ycStr0 = nodetype + "," + ycStr0;
                } else if (nodetype.length() == 3) {
                    cpStr0 = nodetype + "," + cpStr0;
                }
            }
        }
        String ycStr = ycStr0.substring(0, ycStr0.length() - 1);
        String cpStr = cpStr0.substring(0, cpStr0.length() - 1);
        Map<Object, Object> map = new HashMap<>();
        List<Wztaizhang> listPici = null;
        List<Wztaizhang> listBhg = null;
        if (type == 1) {//原材
            listPici = wztaizhangService.SXListYCSD(ycStr, sysOrgCode);//筛选进场批次
            listBhg = wztaizhangService.SXBhgListYCSD(ycStr, sysOrgCode);//筛选不合格批次
        } else {//产品
            listPici = wztaizhangService.SXListCPSD(cpStr, sysOrgCode);//筛选进场批次
            listBhg = wztaizhangService.SXBhgListCPSD(cpStr, sysOrgCode);//筛选不合格批次
        }
        if (listPici.size() == 0) {
            map.put("hglv", "-");
        } else {
            String percentStr = wztaizhangService.getPercentStr((listPici.size() - listBhg.size()), listPici.size());
            map.put("hglv", percentStr);
        }
        map.put("jcpc", listPici.size());
        map.put("bhgpc", listBhg.size());
        map.put("bhgInfo", listBhg);
        return Result.OK(map);
    }


    @AutoLog(value = "wztaizhang-分页列表查询")
    @ApiOperation(value = "wztaizhang-分页列表查询", notes = "wztaizhang-分页列表查询")
    @GetMapping(value = "/queryBhgInfo")
    public Result<?> queryBhgInfo(String orgCode, Integer type,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        Wztaizhang wztaizhang = new Wztaizhang();
        List<String> nodetypeList = wztaizhangService.getNodetypeByOrgCode(orgCode);

        String ycStr0 = "";//原材类型
        String cpStr0 = "";//产品类型
        for (String nodetype : nodetypeList) {
            if (nodetype != null) {
                if (nodetype.length() > 0 && nodetype.length() < 3) {
                    ycStr0 = nodetype + "," + ycStr0;
                } else if (nodetype.length() == 3) {
                    cpStr0 = nodetype + "," + cpStr0;
                }
            }
        }
        wztaizhang.setSysOrgCode(orgCode+"*");
        String ycStr = ycStr0.substring(0, ycStr0.length() - 1);
        String cpStr = cpStr0.substring(0, cpStr0.length() - 1);
        if (type == 1) {//原材
            wztaizhang.setNodetype(ycStr);
           }else{//产品
            wztaizhang.setNodetype(cpStr);
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        queryWrapper.and(wrapper -> wrapper.eq("jianyanstate", 2).or().eq("choujianstate", 2));
        queryWrapper.exists("  SELECT 1 " +
                "             FROM ycl_usage_detail ud " +
                "             WHERE wztaizhang.pici = ud.inspection_lot_number ");
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 瑞苍梁场看板-原材产品统计
     *
     * @param orgCode 组织机构编码
     * @param type    原材类型(1：原材 2：产品)
     * @return
     */
    @AutoLog(value = "wztaizhang-瑞苍梁场看板-原材产品统计")
    @ApiOperation(value = "wztaizhang-瑞苍梁场看板-原材产品统计", notes = "wztaizhang-瑞苍梁场看板-原材产品统计")
    @GetMapping(value = "/getTjLC")
    public Result<?> getTjLC(String orgCode, Integer type) {
        List<String> nodetypeList = wztaizhangService.getNodetypeByOrgCode(orgCode);
        String sysOrgCode = new String(orgCode);

        String ycStr0 = "";//原材类型
        String cpStr0 = "";//产品类型
        for (String nodetype : nodetypeList) {
            if (nodetype != null) {
                if (nodetype.length() > 0 && nodetype.length() < 3) {
                    ycStr0 = nodetype + "," + ycStr0;
                } else if (nodetype.length() == 3) {
                    cpStr0 = nodetype + "," + cpStr0;
                }
            }
        }
        String ycStr = ycStr0.substring(0, ycStr0.length() - 1);
        String cpStr = cpStr0.substring(0, cpStr0.length() - 1);
        Map<Object, Object> map = new HashMap<>();
        List<Wztaizhang> listPici = null;
        List<Wztaizhang> listBhg = null;
        if (type == 1) {//原材
            listPici = wztaizhangService.SXListYCLC(ycStr, sysOrgCode);//筛选进场批次
            listBhg = wztaizhangService.SXBhgListYCLC(ycStr, sysOrgCode);//筛选不合格批次
        } else {//产品
            listPici = wztaizhangService.SXListCPLC(cpStr, sysOrgCode);//筛选进场批次
            listBhg = wztaizhangService.SXBhgListCPLC(cpStr, sysOrgCode);//筛选不合格批次
        }
        if (listPici.size() == 0) {
            map.put("hglv", "-");
        } else {
            String percentStr = wztaizhangService.getPercentStr((listPici.size() - listBhg.size()), listPici.size());
            map.put("hglv", percentStr);
        }
        map.put("jcpc", listPici.size());
        map.put("bhgpc", listBhg.size());
        map.put("bhgInfo", listBhg);
        return Result.OK(map);
    }

    /**
     * 瑞苍智慧梁场看板-材料进场量、检验量、使用量、库存量
     *
     * @param orgCode 组织机构编码
     * @param type    原材类型(1：原材 2：产品)
     * @return
     */
    @AutoLog(value = "wztaizhang-瑞苍智慧梁场看板-材料进场量、检验量、使用量、库存量")
    @ApiOperation(value = "wztaizhang-瑞苍智慧梁场看板-材料进场量、检验量、使用量、库存量", notes = "wztaizhang-瑞苍智慧梁场看板-材料进场量、检验量、使用量、库存量")
    @GetMapping(value = "/getTjInfo")
    public Result<?> getTjInfo(String orgCode, String wbsId, Integer type) {
        try {
            List<String> nodetypeList = null;
            String sysOrgCode = null;
            if (orgCode != null) {
                nodetypeList = wztaizhangService.getNodetypeByOrgCode(orgCode);
                sysOrgCode = new String(orgCode);
            }
            if (wbsId != null) {
                String wbsOrgCode = wztaizhangService.getWbsOrgCode(wbsId);
                nodetypeList = wztaizhangService.getNodetypeByOrgCode(wbsOrgCode);
                sysOrgCode = new String(wbsOrgCode);
            }
            List<String> ycStr = new ArrayList<>();//原材类型
            List<String> cpStr = new ArrayList<>();//产品类型
            for (String nodetype : nodetypeList) {
                if (nodetype != null) {
                    if (nodetype.length() > 0 && nodetype.length() < 3) {
                        ycStr.add(nodetype);
                    } else if (nodetype.length() == 3) {
                        cpStr.add(nodetype);
                    }
                }
            }

            if (type == 1) {//原材
                List<Map> mapList = new ArrayList<>();
                for (String nodetype : ycStr) {
                    if ("7".equals(nodetype) || "8".equals(nodetype) || "9".equals(nodetype) || "10".equals(nodetype)) {
                        continue;
                    }
                    Map<Object, Object> map = new LinkedHashMap<>();
                    String name = wztaizhangService.getNodetypeName(nodetype);

                    String jc = wztaizhangService.selectJCL(sysOrgCode, nodetype);
                    Double sumJc = 0.0;//进场数量
                    sumJc = Double.parseDouble(jc);
                    String uses = wztaizhangService.selectUSES(sysOrgCode, nodetype);
                    Double usesT = 0.0;
                    if (ObjectUtil.isEmpty(uses)) {
                        usesT = 0.0;
                    } else {
                        usesT = (Double.parseDouble(uses) * 0.001);
                    }
                    String jy = wztaizhangService.selectJYL(sysOrgCode, nodetype, 0);
                    Double sumJy = 0.0;//检验量
                    if (ObjectUtil.isEmpty(jy)) {
                        sumJy = 0.0;
                    } else {
                        sumJy = Double.parseDouble(jy);
                    }

                    if (name == null || "0.0".equals(String.format("%.1f", sumJc))) {
                        continue;
                    }
                    map.put("cllx", name);
                    map.put("cllxbh", nodetype);
                    map.put("dw", "吨");
                    map.put("jcl", String.format("%.1f", sumJc));
                    map.put("jyl", String.format("%.1f", sumJy));
                    map.put("syl", String.format("%.1f", usesT));
                    map.put("kcl", String.format("%.1f", (sumJc - usesT)));

                    mapList.add(map);
                }
                return Result.OK(mapList);
            } else {//产品
                List<Map> mapList = new ArrayList<>();
                for (String nodetype : cpStr) {
                    Map<Object, Object> map = new LinkedHashMap<>();
                    String name = wztaizhangService.getNodetypeName(nodetype);

                    String jc = wztaizhangService.selectJCL(sysOrgCode, nodetype);
                    Double sumJc = 0.0;//进场数量
                    sumJc = Double.parseDouble(jc);
                    String uses = wztaizhangService.selectUSES(sysOrgCode, nodetype);
                    Double usesT = 0.0;//使用量
                    if (ObjectUtil.isEmpty(uses)) {
                        usesT = 0.0;
                    } else {
                        usesT = (Double.parseDouble(uses) * 0.001);
                    }

                    String jy = wztaizhangService.selectJYL(sysOrgCode, nodetype, 0);
                    Double sumJy = 0.0;//检验量
                    if (ObjectUtil.isEmpty(jy)) {
                        sumJy = 0.0;
                    } else {
                        sumJy = Double.parseDouble(jy);
                    }

                    if (name == null || "0.0".equals(String.format("%.1f", sumJc))) {
                        continue;
                    }
                    map.put("cllx", name);
                    map.put("cllxbh", nodetype);
                    map.put("dw", "个");
                    map.put("jcl", String.format("%.1f", sumJc));
                    map.put("jyl", String.format("%.1f", sumJy));
                    map.put("syl", String.format("%.1f", usesT));
                    map.put("kcl", String.format("%.1f", (sumJc - usesT)));

                    mapList.add(map);
                }
                return Result.OK(mapList);
            }
        } catch (Exception e) {
            return Result.error("暂无数据");
        }
    }

    /**
     * 物料管理-根据材料编号,料仓,供应商查批次
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "物料管理-根据材料编号,料仓,供应商查批次")
    @ApiOperation(value = "物料管理-根据材料编号,料仓,供应商查批次", notes = "物料管理-根据材料编号,料仓,供应商查批次")
    @GetMapping(value = "/getCaiLiaoLists")
    public Result<?> getCaiLiaoLists(Wztaizhang wztaizhang,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //1检验状态
//        wztaizhang.setJianyanstate(1);
//        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
//            wztaizhang.setSysOrgCode(sys_depart_orgcode + "*");
//        } else {
//            wztaizhang.setSysOrgCode(loginUser.getOrgCode() + "*");
//        }
        //1检验状态
        Integer jianyanstate = wztaizhang.getJianyanstate();
        //2材料编号
        String cailiaono = wztaizhang.getCailiaono();
        //3料仓编号
        String lcbianhao = wztaizhang.getLcbianhao();
        //4供应商编号
        String gongyingshangdanweibianma = wztaizhang.getGongyingshangdanweibianma();
        //5组织机构
        String orgCode = loginUser.getOrgCode();
        List<Wztaizhang> wztaizhangList = wztaizhangService.getPiciByStateCailiaoLiaocangGongyingshangNo(jianyanstate, cailiaono, lcbianhao, gongyingshangdanweibianma, orgCode);
//        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
//        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
//        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(wztaizhangList);
    }

    /**
     * 库存查询
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "库存查询")
    @ApiOperation(value = "库存查询", notes = "库存查询")
    @GetMapping(value = "/getCaiLiaoKuCunLists")
    public Result<?> getCaiLiaoKuCunLists(Wztaizhang wztaizhang,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                          HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //1检验状态
//        wztaizhang.setJianyanstate(1);
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wztaizhang.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wztaizhang.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
//        WztaizhangKCVo wztaizhangKCVo = wztaizhangService.getCaiLiaoKuCunPiCiLists(wztaizhang.getSysOrgCode());
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
}
















