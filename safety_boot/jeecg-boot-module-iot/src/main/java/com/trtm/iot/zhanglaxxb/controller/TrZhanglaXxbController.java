package com.trtm.iot.zhanglaxxb.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.vo.BhzCementBasePage;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxbS;
import com.trtm.iot.zhanglaxxb.vo.Trzhanglaxxbmsss;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 张拉信息表
 * @Author: jeecg-boot
 * @Date: 2021-09-01
 * @Version: V1.0
 */
@Api(tags = "张拉信息表")
@RestController
@RequestMapping("/zhanglaxxb/trZhanglaXxb")
@Slf4j
public class TrZhanglaXxbController extends JeecgController<TrZhanglaXxb, ITrZhanglaXxbService> {
    @Autowired
    private ITrZhanglaXxbService trZhanglaXxbService;
    @Autowired
    private ITrZhanglaSService trZhanglaSService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrZhanglaMService trZhanglaMService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ITrZhanglaRenwudanService zhanglaRenwudanService;

    @Autowired
    private IZhanglaYajiangOverHandlerService zhanglaYajiangOverHandlerService;

    /**
     * 分页列表查询
     *
     * @param trZhanglaXxb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息表-分页列表查询")
    @ApiOperation(value = "张拉信息表-分页列表查询", notes = "张拉信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrZhanglaXxb trZhanglaXxb,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaXxb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaXxb.setShebeibianhao(shebei);
            } else {
                trZhanglaXxb.setShebeibianhao("空");
            }
        }
        if (trZhanglaXxb.getGjbh() != null){
            trZhanglaXxb.setGjbh("*"+trZhanglaXxb.getGjbh()+"*");
        }
        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        if (!loginUser.getOrgCode().equals("A05A01A12A13")){
            queryWrapper.notLike("shebeibianhao", "A05A01A12A13");
        }
        Page<TrZhanglaXxb> page = new Page<TrZhanglaXxb>(pageNo, pageSize);
        IPage<TrZhanglaXxb> pageList = trZhanglaXxbService.page(page, queryWrapper);
        for (TrZhanglaXxb xxb : pageList.getRecords()) {
            QueryWrapper<TrZhanglaM> tWrapper = new QueryWrapper<TrZhanglaM>();
            tWrapper.eq("hege", "0");// 不合格
            tWrapper.eq("syjid", xxb.getSyjid());// 试验机
            int count = trZhanglaMService.count(tWrapper);
            if (count > 0) {
                // 设置不合格
                xxb.setHege("0");
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 2023、07、20
     *
     * @param trZhanglaXxb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息表-分页列表查询")
    @ApiOperation(value = "张拉信息表-分页列表查询", notes = "张拉信息表-分页列表查询")
    @GetMapping(value = "/listxq")
    public Result<?> queryPageListxq(TrZhanglaXxb trZhanglaXxb,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaXxb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaXxb.setShebeibianhao(shebei);
            } else {
                trZhanglaXxb.setShebeibianhao("空");
            }
        }
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String format = formatter.format(date);

        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        queryWrapper.likeRight("sgsj",format);
        List<TrZhanglaXxb> list = trZhanglaXxbService.list(queryWrapper);//今日张拉数

        QueryWrapper<TrZhanglaXxb> queryWrapper1 = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        List<TrZhanglaXxb> list1 = trZhanglaXxbService.list(queryWrapper1);//累计张拉数

        TrZhanglaRenwudan trZhanglaRenwudan = new TrZhanglaRenwudan();
        trZhanglaRenwudan.setShebeibianh(shebei);
        QueryWrapper<TrZhanglaRenwudan> queryWrapper2 = QueryGenerator.initQueryWrapper(trZhanglaRenwudan, req.getParameterMap());
        queryWrapper2.eq("wcstatus",0);
        List<TrZhanglaRenwudan> list2 = zhanglaRenwudanService.list(queryWrapper2);//待张拉数

        TrZhanglaM zhanglaM = new TrZhanglaM();
        zhanglaM.setShebeibianhao(shebei);
        QueryWrapper<TrZhanglaM> queryWrapper3 = QueryGenerator.initQueryWrapper(zhanglaM, req.getParameterMap());
        List<TrZhanglaM> list3 = trZhanglaMService.list(queryWrapper3);//张拉总次数

        Map<String, Integer> map = new HashMap<>();
        map.put("jrzls",list.size());
        map.put("ljzll",list1.size());
        map.put("dzll",list2.size());
        map.put("zlzcs",list3.size());
        return Result.OK(map);
    }

    /**
     * 2023、07、20
     *
     * @param trZhanglaXxb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息表-分页列表查询")
    @ApiOperation(value = "张拉信息表-分页列表查询", notes = "张拉信息表-分页列表查询")
    @GetMapping(value = "/listhgl")
    public Result<?> queryPageListhgl(TrZhanglaXxb trZhanglaXxb,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaXxb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaXxb.setShebeibianhao(shebei);
            } else {
                trZhanglaXxb.setShebeibianhao("空");
            }
        }
        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        List<TrZhanglaXxb> list = trZhanglaXxbService.list(queryWrapper);
        List<String> strings = new ArrayList<>();
        if (list.size() > 0){
            for (TrZhanglaXxb s :list){
                strings.add(s.getSyjid());
            }
        }
        TrZhanglaM zhanglaM = new TrZhanglaM();
        QueryWrapper<TrZhanglaM> queryWrapper1 = QueryGenerator.initQueryWrapper(zhanglaM, req.getParameterMap());
        queryWrapper1.eq("hege", "0");// 不合格
        queryWrapper1.in("syjid",strings);
        queryWrapper1.groupBy("syjid");
        List<TrZhanglaM> list1 = trZhanglaMService.list(queryWrapper1);

        Map<Object, Object> map = new HashMap<>();
        map.put("hege",list.size()-list1.size());
        map.put("buhege",list1.size());
        return Result.OK(map);
    }
    /**
     * 分页列表查询
     *
     * @param trZhanglaXxb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息表-分页列表查询")
    @ApiOperation(value = "张拉信息表-分页列表查询", notes = "张拉信息表-分页列表查询")
    @GetMapping(value = "/list47")
    public Result<?> queryPageList47(TrZhanglaXxb trZhanglaXxb,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaXxb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaXxb.setShebeibianhao(shebei);
            } else {
                trZhanglaXxb.setShebeibianhao("空");
            }
        }
        trZhanglaXxb.setGjbh("*" + trZhanglaXxb.getGjbh() + "*");
        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        Page<TrZhanglaXxb> page = new Page<TrZhanglaXxb>(pageNo, pageSize);
        IPage<TrZhanglaXxb> pageList = trZhanglaXxbService.page(page, queryWrapper);
        for (TrZhanglaXxb xxb : pageList.getRecords()) {
            try {
                if (xxb.getShebeibianhao().equals("hzzl20240518")){//湖州基地1号张拉机显示全部部位
                    TrZhanglaRenwudan selectone = zhanglaRenwudanService.selectone(xxb.getUuid());
                    xxb.setGjbh(selectone.getSgbwname());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(xxb.getShebeibianhao());
            String projectName = bhzCementBaseService.selectbyorgcode(selectshebeione.getSysOrgCode(), 7);
            if (projectName != null) {
                xxb.setGcmc(projectName);
            }else{
                String projectName1 = bhzCementBaseService.selectbyorgcode(selectshebeione.getSysOrgCode(), 8);
                xxb.setGcmc(projectName1);
            }
            QueryWrapper<TrZhanglaM> tWrapper = new QueryWrapper<TrZhanglaM>();
            tWrapper.eq("hege", "0");// 不合格
            tWrapper.eq("syjid", xxb.getSyjid());// 试验机
            int count = trZhanglaMService.count(tWrapper);
            if (count > 0) {
                // 设置不合格
                xxb.setHege("0");
            }
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "张拉信息表-分页列表查询")
    @ApiOperation(value = "张拉信息表-分页列表查询", notes = "张拉信息表-分页列表查询")
    @GetMapping(value = "/list47NoUse")
    public Result<?> queryPageList47NoUse(TrZhanglaXxb trZhanglaXxb,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaXxb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaXxb.setShebeibianhao(shebei);
            } else {
                trZhanglaXxb.setShebeibianhao("空");
            }
        }
        trZhanglaXxb.setGjbh("*" + trZhanglaXxb.getGjbh() + "*");

        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        queryWrapper.notLike("uuid","ZL-RWD").or().isNotNull("bindrwd");// 开始数据不是
        Page<TrZhanglaXxb> page = new Page<TrZhanglaXxb>(pageNo, pageSize);
        IPage<TrZhanglaXxb> pageList = trZhanglaXxbService.page(page, queryWrapper);
        for (TrZhanglaXxb xxb : pageList.getRecords()) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(xxb.getShebeibianhao());
            String projectName = bhzCementBaseService.selectbyorgcode(selectshebeione.getSysOrgCode(), 7);
            if (projectName != null) {
                xxb.setGcmc(projectName);
            }else{
                String projectName1 = bhzCementBaseService.selectbyorgcode(selectshebeione.getSysOrgCode(), 8);
                xxb.setGcmc(projectName1);
            }
            QueryWrapper<TrZhanglaM> tWrapper = new QueryWrapper<TrZhanglaM>();
            tWrapper.eq("hege", "0");// 不合格
            tWrapper.eq("syjid", xxb.getSyjid());// 试验机
            int count = trZhanglaMService.count(tWrapper);
            if (count > 0) {
                // 设置不合格
                xxb.setHege("0");
            }
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "张拉信息表-分页列表查询")
    @ApiOperation(value = "张拉信息表-分页列表查询", notes = "张拉信息表-分页列表查询")
    @PostMapping(value = "/updateUse")
    public Result<?> batchUpdateUse( @RequestBody TrZhanglaXxb trZhanglaXxb ,
                                          HttpServletRequest req) {
        String msg = "";
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String baseid = trZhanglaXxb.getShebeibianhao()+trZhanglaXxb.getId();
        ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = zhanglaYajiangOverHandlerService.selectoneMessage(baseid);
        if(ObjectUtil.isNotNull(zhanglaYajiangOverHandler1)){
            // zhanglaYajiangOverHandler1.setId(zhanglaYajiangOverHandler1.getId());
            zhanglaYajiangOverHandler1.setHeadquartersApproval(trZhanglaXxb.getMemo());// 审批意见
            zhanglaYajiangOverHandler1.setSupervisorHandleTime(new Date());
            zhanglaYajiangOverHandler1.setApprovalPerson(loginUser.getRealname());
            msg = "审批通过";
        }else{
            zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
            zhanglaYajiangOverHandler1.setBaseid(baseid);
            zhanglaYajiangOverHandler1.setHandleResult(trZhanglaXxb.getMemo());// 说明原因
            zhanglaYajiangOverHandler1.setHoleid(trZhanglaXxb.getUuid());
            zhanglaYajiangOverHandler1.setHandleWay(trZhanglaXxb.getGjbh());// 关联部位
            zhanglaYajiangOverHandler1.setHandleTime(new Date());
            zhanglaYajiangOverHandler1.setHandlePerson(loginUser.getRealname());
            msg = "说明填报成功，待审批";
        }
        zhanglaYajiangOverHandler1.setOverproofStatus(trZhanglaXxb.getBindrwd());
        zhanglaYajiangOverHandlerService.saveOrUpdate(zhanglaYajiangOverHandler1);
        trZhanglaXxb.setMemo("");
        // 更新主表关联任务单编号，修改状态为已填报
        trZhanglaXxbService.updateById(trZhanglaXxb);
        return Result.OK(msg);
    }


    /**
     * 张拉压浆次数统计/张拉完工数统计
     *
     * @param trZhanglaXxb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息表-张拉压浆次数统计/张拉完工数统计")
    @ApiOperation(value = "张拉信息表-张拉压浆次数统计/张拉完工数统计", notes = "张拉信息表-张拉压浆次数统计/张拉完工数统计")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(TrZhanglaXxb trZhanglaXxb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaXxb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaXxb.setShebeibianhao(shebei);
            } else {
                trZhanglaXxb.setShebeibianhao("空");
            }
        }
        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        queryWrapper.select("count(1) as id");
        List<TrZhanglaXxb> pageList = trZhanglaXxbService.list(queryWrapper);
        Map map = new HashMap();
        int sumzl = 0;
        for (TrZhanglaXxb trZhanglaXxb1 : pageList) {
            if (trZhanglaXxb1 != null) {
                sumzl = trZhanglaXxb1.getId();
            }
        }
        map.put("sumzl", sumzl);
        return Result.OK(map);
    }

    /**
     * 看板
     *
     * @param trZhanglaXxb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息表-分页列表查询")
    @ApiOperation(value = "张拉信息表-分页列表查询", notes = "张拉信息表-分页列表查询")
    @GetMapping(value = "/listrt")
    public Result<?> queryPageListrt(TrZhanglaXxb trZhanglaXxb,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaXxb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaXxb.setShebeibianhao(shebei);
            } else {
                trZhanglaXxb.setShebeibianhao("空");
            }
        }
        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        List<TrZhanglaXxb> list = trZhanglaXxbService.list(queryWrapper);

//		 QueryWrapper<TrZhanglaXxb> queryWrapper1 = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
//		 queryWrapper1.eq("hege",0);
//		 List<TrZhanglaXxb> list1 = trZhanglaXxbService.list(queryWrapper1);
        Map<String, Integer> map = new HashMap<>();
        map.put("zs", list.size());
        map.put("hgs", list.size());

        return Result.OK(map);
    }

    /**
     * 列表查询
     *
     * @param trZhanglaXxb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息表-列表查询")
    @ApiOperation(value = "张拉信息表-列表查询", notes = "张拉信息表-列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(TrZhanglaXxb trZhanglaXxb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        List<TrZhanglaXxb> list = trZhanglaXxbService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 列表查询
     *
     * @param trZhanglaXxb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息表-列表查询")
    @ApiOperation(value = "张拉信息表-列表查询", notes = "张拉信息表-列表查询")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(TrZhanglaXxb trZhanglaXxb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaXxb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaXxb.setShebeibianhao(shebei);
            } else {
                trZhanglaXxb.setShebeibianhao("空");
            }
        }
        if (StrUtil.isNotBlank(trZhanglaXxb.getGjbh())) {
            trZhanglaXxb.setGjbh("*" + trZhanglaXxb.getGjbh() + "*");
        }
        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        List<TrZhanglaXxb> list = trZhanglaXxbService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param trZhanglaXxb
     * @return
     */
    @AutoLog(value = "张拉信息表-添加")
    @ApiOperation(value = "张拉信息表-添加", notes = "张拉信息表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrZhanglaXxb trZhanglaXxb) {
        trZhanglaXxbService.save(trZhanglaXxb);
        return Result.OK("添加成功！");
    }


    /**
     * 添加
     *
     * @param
     * @return
     */
    @AutoLog(value = "张拉完整信息表-添加接口")
    @ApiOperation(value = "张拉完整信息表-添加接口", notes = "张拉信息表-添加接口")
    @PostMapping(value = "/addZhangla")
    public Result<?> addZhangla(@RequestBody Trzhanglaxxbmsss trzhanglaxxbmsss) {
        TrZhanglaXxb trZhanglaXxb = new TrZhanglaXxb();
        BeanUtils.copyProperties(trzhanglaxxbmsss, trZhanglaXxb);
        trZhanglaXxbService.saveMain(trZhanglaXxb, trzhanglaxxbmsss.getZhanglam());
        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param trZhanglaXxb
     * @return
     */
    @AutoLog(value = "张拉信息表-编辑")
    @ApiOperation(value = "张拉信息表-编辑", notes = "张拉信息表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrZhanglaXxb trZhanglaXxb) {
        trZhanglaXxbService.updateById(trZhanglaXxb);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "张拉信息表-通过id删除")
    @ApiOperation(value = "张拉信息表-通过id删除", notes = "张拉信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trZhanglaXxbService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "张拉信息表-批量删除")
    @ApiOperation(value = "张拉信息表-批量删除", notes = "张拉信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trZhanglaXxbService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "张拉信息表-通过id查询")
    @ApiOperation(value = "张拉信息表-通过id查询", notes = "张拉信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrZhanglaXxb trZhanglaXxb = trZhanglaXxbService.getById(id);
        if (trZhanglaXxb == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trZhanglaXxb);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trZhanglaXxb
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrZhanglaXxb trZhanglaXxb) {
        return super.exportXls(request, trZhanglaXxb, TrZhanglaXxb.class, "张拉信息表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TrZhanglaXxb.class);
    }

    @GetMapping(value = "/queryList")
    public Result<?> queryList(TrZhanglaXxb trZhanglaXxb,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                               HttpServletRequest req, String sys_depart_orgcode, String date) {
        QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
        shebeiInfoQueryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
        shebeiInfoQueryWrapper.eq("sbtype",9);
        List<ShebeiInfo> list = shebeiInfoService.list(shebeiInfoQueryWrapper);

        String shebei = "";
        String substring = "";
        for (ShebeiInfo shebeiInfo : list) {
            shebei += shebeiInfo.getSbjno() + ",";
        }
        if (shebei.length() > 0) {
            substring = shebei.substring(0, shebei.length() - 1);
        }
        trZhanglaXxb.setShebeibianhao(substring);
        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        if (date != null) {
            queryWrapper.gt("sgsj", date);
        }
        queryWrapper.orderByAsc("sgsj");
        Page<TrZhanglaXxb> page = new Page<>(pageNo, pageSize);

        List<Object> records1 = new ArrayList<>();
        Page<TrZhanglaXxb> page1 = trZhanglaXxbService.page(page, queryWrapper);
        for (TrZhanglaXxb zhanglaXxb : page1.getRecords()) {
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper1 = new QueryWrapper<>();
            shebeiInfoQueryWrapper1.eq("sbjno", zhanglaXxb.getShebeibianhao());
            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfoQueryWrapper1);
            zhanglaXxb.setShebeibianhao(one.getSbname());

            TrZhanglaXxbS trZhanglaXxbS = new TrZhanglaXxbS();
            BeanUtils.copyProperties(zhanglaXxb, trZhanglaXxbS);
            QueryWrapper<TrZhanglaS> trZhanglaSQueryWrapper = new QueryWrapper<>();
            trZhanglaSQueryWrapper.eq("syjid", zhanglaXxb.getSyjid());
            List<TrZhanglaS> list1 = trZhanglaSService.list(trZhanglaSQueryWrapper);
            trZhanglaXxbS.setZhanglaSList(list1);
            records1.add(trZhanglaXxbS);
        }

        return Result.OK(records1);
    }


    @GetMapping(value = "/queryListrc")
    public Result<?> queryListrc(TrZhanglaXxb trZhanglaXxb,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                               HttpServletRequest req, String sys_depart_orgcode, String date) {
        QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
        shebeiInfoQueryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
        shebeiInfoQueryWrapper.eq("sbtype",9);
        List<ShebeiInfo> list = shebeiInfoService.list(shebeiInfoQueryWrapper);

        List<String> list12 = new ArrayList<>();
        for (ShebeiInfo shebeiInfo : list) {
            list12.add(shebeiInfo.getSbjno());
        }

        List<Object> records1 = new ArrayList<>();
        List<TrZhanglaXxb> list3 = trZhanglaXxbService.selectListss(list12,date);
        for (TrZhanglaXxb zhanglaXxb : list3) {
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper1 = new QueryWrapper<>();
            shebeiInfoQueryWrapper1.eq("sbjno", zhanglaXxb.getShebeibianhao());
            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfoQueryWrapper1);
            zhanglaXxb.setShebeibianhao(one.getSbname());

            TrZhanglaXxbS trZhanglaXxbS = new TrZhanglaXxbS();
            BeanUtils.copyProperties(zhanglaXxb, trZhanglaXxbS);
            QueryWrapper<TrZhanglaS> trZhanglaSQueryWrapper = new QueryWrapper<>();
            trZhanglaSQueryWrapper.eq("syjid", zhanglaXxb.getSyjid());
            List<TrZhanglaS> list1 = trZhanglaSService.list(trZhanglaSQueryWrapper);
            trZhanglaXxbS.setZhanglaSList(list1);
            records1.add(trZhanglaXxbS);
        }

        return Result.OK(records1);
    }

    @GetMapping(value = "/getPop")
    public Result<?> getPop() {


        return Result.OK();
    }

    //    resetPush
    @AutoLog(value = "重置")
    @ApiOperation(value = "重置", notes = "重置")
    @GetMapping(value = "/resetPush")
    public Result<?> resetPush(TrYajiangM trYajiangM) {
        String msg = "推送状态重置成功！";
        Integer id = trYajiangM.getId();
        QueryWrapper<TrZhanglaXxb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        TrZhanglaXxb one = trZhanglaXxbService.getOne(queryWrapper);
        one.setIssend(0);
        trZhanglaXxbService.updateById(one);
        return Result.OK(msg);
    }
}
