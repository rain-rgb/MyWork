package com.trtm.iot.zhanglam.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import com.trtm.iot.zhanglam.vo.TrZhanglaMS;
import com.trtm.iot.zhanglam.vo.TrZhanglaXxbM;
import com.trtm.iot.zhanglam.vo.TrZhanglaXxbMCL;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglas.vo.TrZhanglaSVo;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;

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
 * @Description: 张拉信息主表
 * @Author: jeecg-boot
 * @Date: 2021-08-31
 * @Version: V1.0
 */
@Api(tags = "张拉信息主表")
@RestController
@RequestMapping("/zhanglam/trZhanglaM")
@Slf4j
public class TrZhanglaMController extends JeecgController<TrZhanglaM, ITrZhanglaMService> {
    @Autowired
    private ITrZhanglaMService trZhanglaMService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ITrZhanglaSService trZhanglaSService;
    @Autowired
    private ITrZhanglaXxbService trZhanglaXxbService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IZhanglaYajiangOverHandlerService zhanglaYajiangOverHandlerService;
    @Autowired
    private ITrYajiangMService trYajiangMService;
    @Autowired
    private ITrYajiangSService trYajiangSService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ITrZhanglaRenwudanService zhanglaRenwudanService;

    /**
     * 分页列表查询
     *
     * @param trZhanglaM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息主表-分页列表查询")
    @ApiOperation(value = "张拉信息主表-分页列表查询", notes = "张拉信息主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrZhanglaM trZhanglaM,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaM.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaM.setShebeibianhao(shebei);
            } else {
                trZhanglaM.setShebeibianhao("空");
            }
        }
        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        Page<TrZhanglaM> page = new Page<TrZhanglaM>(pageNo, pageSize);
        IPage<TrZhanglaM> pageList = trZhanglaMService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 张拉孔信息以及断面数据查询
     *
     * @param trZhanglaM
     * @param req
     * @return
     */
    @AutoLog(value = "张拉孔信息以及断面数据查询")
    @ApiOperation(value = "张拉孔信息以及断面数据查询", notes = "张拉孔信息以及断面数据查询")
    @GetMapping(value = "/listjxgs")
    public Result<?> queryPageListjxgs(TrZhanglaM trZhanglaM, HttpServletRequest req) {
        String code = trZhanglaM.getSyjid();
        QueryWrapper<TrZhanglaRenwudan> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("code",code);
        List<TrZhanglaRenwudan> list3 = zhanglaRenwudanService.list(queryWrapper2);
        if (list3.size() > 0){
            TrZhanglaRenwudan trZhanglaRenwudan = list3.get(0);
            QueryWrapper<TrZhanglaXxb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid",trZhanglaRenwudan.getUuid());
            List<TrZhanglaXxb> list = trZhanglaXxbService.list(queryWrapper);
            if (list.size() > 0){
                TrZhanglaXxb zhanglaXxb = list.get(0);
                String syjid = zhanglaXxb.getSyjid();
                return Result.OK(syjid);
            }
        }
        return Result.OK("请关联对应张拉任务单！！！");
    }

    /**
     * 张拉孔信息以及断面数据查询
     *
     * @param trZhanglaM
     * @param req
     * @return
     */
    @AutoLog(value = "张拉孔信息以及断面数据查询")
    @ApiOperation(value = "张拉孔信息以及断面数据查询", notes = "张拉孔信息以及断面数据查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(TrZhanglaM trZhanglaM, HttpServletRequest req) {

        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        List<TrZhanglaM> list = trZhanglaMService.list(queryWrapper);
        List list1 = new ArrayList();
        for (TrZhanglaM zhanglaM : list) {
            TrZhanglaMS trZhanglaMS = new TrZhanglaMS();
            trZhanglaMS.setTrZhanglaMList(zhanglaM);
            String fguid = zhanglaM.getFguid();
            QueryWrapper<TrZhanglaS> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("fguid", fguid);
            List<TrZhanglaS> list2 = trZhanglaSService.list(queryWrapper1);
            trZhanglaMS.setTrZhanglaSList(list2);
            trZhanglaMS.setFguid(fguid);
            trZhanglaMS.setGsbh(zhanglaM.getGsbh());
            trZhanglaMS.setId(zhanglaM.getId());
            trZhanglaMS.setHtl(zhanglaM.getHtl());
            trZhanglaMS.setJlsj(zhanglaM.getJlsj());
            trZhanglaMS.setLlscl(zhanglaM.getLlscl());
            trZhanglaMS.setSclper(zhanglaM.getSclper());
            trZhanglaMS.setSjzll(zhanglaM.getSjzll());
            trZhanglaMS.setZlsj(zhanglaM.getZlsj());
            trZhanglaMS.setZscl(zhanglaM.getZscl());
            trZhanglaMS.setZzlb(zhanglaM.getZzlb());
            list1.add(trZhanglaMS);
        }

        return Result.OK(list1);
    }

    /**
     * 张拉孔信息以及断面数据查询
     *
     * @param trZhanglaM
     * @param req
     * @return
     */
    @AutoLog(value = "张拉孔信息以及断面数据查询")
    @ApiOperation(value = "张拉孔信息以及断面数据查询", notes = "张拉孔信息以及断面数据查询")
    @GetMapping(value = "/listfz")
    public Result<?> queryPageListfz(TrZhanglaM trZhanglaM, HttpServletRequest req) {

        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        List<TrZhanglaM> list = trZhanglaMService.list(queryWrapper);
        List list1 = new ArrayList();
        QueryWrapper<TrZhanglaXxb> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("syjid", trZhanglaM.getSyjid());
        List<TrZhanglaXxb> list3 = trZhanglaXxbService.list(queryWrapper2);
        for (TrZhanglaXxb zhanglaXxb :list3){
            for (TrZhanglaM zhanglaM : list) {
                TrZhanglaMS trZhanglaMS = new TrZhanglaMS();
                trZhanglaMS.setTrZhanglaMList(zhanglaM);
                String fguid = zhanglaM.getFguid();
                QueryWrapper<TrZhanglaS> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("fguid", fguid);
                List<TrZhanglaS> list2 = trZhanglaSService.list(queryWrapper1);

                trZhanglaMS.setTrZhanglaSList(list2);
                trZhanglaMS.setFguid(fguid);
                trZhanglaMS.setGsbh(zhanglaM.getGsbh());
                trZhanglaMS.setId(zhanglaM.getId());
                trZhanglaMS.setHtl(zhanglaM.getHtl());
                trZhanglaMS.setJlsj(zhanglaM.getJlsj());
                trZhanglaMS.setLlscl(zhanglaM.getLlscl());
                trZhanglaMS.setSclper(zhanglaM.getSclper());
                trZhanglaMS.setSjzll(zhanglaM.getSjzll());
                trZhanglaMS.setZlsj(zhanglaM.getZlsj());
                trZhanglaMS.setZscl(zhanglaM.getZscl());
                trZhanglaMS.setZzlb(zhanglaM.getZzlb());

                trZhanglaMS.setUuid(zhanglaXxb.getUuid());//梁号
                trZhanglaMS.setSgsj(zhanglaXxb.getSgsj());//施工时间
                trZhanglaMS.setGsgs(zhanglaM.getGsgs());
                trZhanglaMS.setOverproofStatus(zhanglaXxb.getOverproofStatus());
                trZhanglaMS.setSnsjqd(zhanglaXxb.getSnsjqd());
                trZhanglaMS.setSnskqd(zhanglaXxb.getSnskqd());
                list1.add(trZhanglaMS);
            }
        }
        return Result.OK(list1);
    }

    @AutoLog(value = "张拉孔信息以及断面数据查询")
    @ApiOperation(value = "张拉孔信息以及断面数据查询", notes = "张拉孔信息以及断面数据查询")
    @GetMapping(value = "/list1s")
    public Result<?> queryPageList2(TrZhanglaM trZhanglaM, HttpServletRequest req) throws CloneNotSupportedException {

        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        queryWrapper.orderByAsc(trZhanglaM.getGsbh());
        List<TrZhanglaM> list = trZhanglaMService.list(queryWrapper);
        List list1 = new ArrayList();
        for (TrZhanglaM zhanglaM : list) {
            TrZhanglaSVo trZhanglaMS = new TrZhanglaSVo();
            BeanUtils.copyProperties(zhanglaM, trZhanglaMS);

            String fguid = zhanglaM.getFguid();
            QueryWrapper<TrZhanglaS> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("fguid", fguid);
            List<TrZhanglaS> list2 = trZhanglaSService.list(queryWrapper1);

            trZhanglaMS.setDh("1");
            TrZhanglaSVo trZhanglaMS1zl = (TrZhanglaSVo) trZhanglaMS.clone();
            trZhanglaMS1zl.setRecodePoint("张拉力(KN)");
            TrZhanglaSVo trZhanglaMS1yy = (TrZhanglaSVo) trZhanglaMS.clone();
            trZhanglaMS1yy.setRecodePoint("油压(Mpa)");
            TrZhanglaSVo trZhanglaMS1scl = (TrZhanglaSVo) trZhanglaMS.clone();
            trZhanglaMS1scl.setRecodePoint("伸长量(mm)");
            trZhanglaMS.setDh("2");
            TrZhanglaSVo trZhanglaMS2zl = (TrZhanglaSVo) trZhanglaMS.clone();
            trZhanglaMS2zl.setRecodePoint("张拉力(KN)");
            TrZhanglaSVo trZhanglaMS2yy = (TrZhanglaSVo) trZhanglaMS.clone();
            trZhanglaMS2yy.setRecodePoint("油压(Mpa)");
            TrZhanglaSVo trZhanglaMS2scl = (TrZhanglaSVo) trZhanglaMS.clone();
            trZhanglaMS2scl.setRecodePoint("伸长量(mm)");

            String chsj1 = "0";
            String chsj2 = "0";

            for (TrZhanglaS zhanglas : list2) {
                if(StringUtils.isNotBlank(zhanglas.getDh())){
                if((zhanglas.getDh().equals("1") || zhanglas.getDh().contains("A")) && StringUtils.isNotEmpty(zhanglas.getJdbfb()) ){
                            switch (zhanglas.getJdbfb()){
                                case "10":
                                case "15":
                                    trZhanglaMS1zl.setJdbfb10(zhanglas.getJdzll());
                                    trZhanglaMS1yy.setJdbfb10(zhanglas.getYbds());
                                    trZhanglaMS1scl.setJdbfb10(zhanglas.getJdscl());
                                    break;
                                case "20":
                                case "30":
                                    trZhanglaMS1zl.setJdbfb20(zhanglas.getJdzll());
                                    trZhanglaMS1yy.setJdbfb20(zhanglas.getYbds());
                                    trZhanglaMS1scl.setJdbfb20(zhanglas.getJdscl());
                                    break;
                                case "50":if(StringUtils.isNotBlank(trZhanglaMS1zl.getJdbfb50I())){
                                    trZhanglaMS1zl.setJdbfb50II(zhanglas.getJdzll());
                                    trZhanglaMS1yy.setJdbfb50II(zhanglas.getYbds());
                                    trZhanglaMS1scl.setJdbfb50II(zhanglas.getJdscl());
                                }else{
                                    trZhanglaMS1zl.setJdbfb50I(zhanglas.getJdzll());
                                    trZhanglaMS1yy.setJdbfb50I(zhanglas.getYbds());
                                    trZhanglaMS1scl.setJdbfb50I(zhanglas.getJdscl());
                                }
                                    break;
                                case "100":
                                    trZhanglaMS1zl.setJdbfb100(zhanglas.getJdzll());
                                    trZhanglaMS1yy.setJdbfb100(zhanglas.getYbds());
                                    trZhanglaMS1scl.setJdbfb100(zhanglas.getJdscl());

                                    break;
                            }
                            if(Integer.parseInt((StringUtils.isNotBlank(zhanglas.getChsj())?zhanglas.getChsj():"0"))>Integer.parseInt(chsj1)){
                                chsj1=zhanglas.getChsj();
                            }


                }else if((zhanglas.getDh().equals("2") || zhanglas.getDh().contains("B")) && StringUtils.isNotEmpty(zhanglas.getJdbfb()) ) {
                    switch (zhanglas.getJdbfb()){
                        case "10":
                        case "15":
                            trZhanglaMS2zl.setJdbfb10(zhanglas.getJdzll());
                            trZhanglaMS2yy.setJdbfb10(zhanglas.getYbds());
                            trZhanglaMS2scl.setJdbfb10(zhanglas.getJdscl());
                            break;
                        case "20":
                        case "30":
                            trZhanglaMS2zl.setJdbfb20(zhanglas.getJdzll());
                            trZhanglaMS2yy.setJdbfb20(zhanglas.getYbds());
                            trZhanglaMS2scl.setJdbfb20(zhanglas.getJdscl());
                            break;
                        case "50":if(StringUtils.isNotBlank(trZhanglaMS2zl.getJdbfb50I())){
                            trZhanglaMS2zl.setJdbfb50II(zhanglas.getJdzll());
                            trZhanglaMS2yy.setJdbfb50II(zhanglas.getYbds());
                            trZhanglaMS2scl.setJdbfb50II(zhanglas.getJdscl());
                        }else{
                            trZhanglaMS2zl.setJdbfb50I(zhanglas.getJdzll());
                            trZhanglaMS2yy.setJdbfb50I(zhanglas.getYbds());
                            trZhanglaMS2scl.setJdbfb50I(zhanglas.getJdscl());
                        }
                            break;
                        case "100":
                            trZhanglaMS2zl.setJdbfb100(zhanglas.getJdzll());
                            trZhanglaMS2yy.setJdbfb100(zhanglas.getYbds());
                            trZhanglaMS2scl.setJdbfb100(zhanglas.getJdscl());
                            break;
                    }
                    if(Integer.parseInt((StringUtils.isNotBlank(zhanglas.getChsj())?zhanglas.getChsj():"0"))>Integer.parseInt(chsj2)){
                        chsj2=zhanglas.getChsj();
                    }
                }}
            }
            trZhanglaMS1zl.setChsj(chsj1);
            trZhanglaMS1yy.setChsj(chsj1);
            trZhanglaMS1scl.setChsj(chsj1);
            trZhanglaMS2zl.setChsj(chsj2);
            trZhanglaMS2yy.setChsj(chsj2);
            trZhanglaMS2scl.setChsj(chsj2);

            list1.add(trZhanglaMS1zl);
            list1.add(trZhanglaMS1yy);
            list1.add(trZhanglaMS1scl);
            list1.add(trZhanglaMS2zl);
            list1.add(trZhanglaMS2yy);
            list1.add(trZhanglaMS2scl);
        }

        return Result.OK(list1);
    }




    /**
     * 不合格数据分页列表查询
     *
     * @param trZhanglaM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息主表-不合格数据分页列表查询")
    @ApiOperation(value = "张拉信息主表-不合格数据分页列表查询", notes = "张拉信息主表-不合格数据分页列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(TrZhanglaM trZhanglaM,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaM.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaM.setShebeibianhao(shebei);
            } else {
                trZhanglaM.setShebeibianhao("空");
            }
        }
        trZhanglaM.setHege("0,不合格");
        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        queryWrapper.groupBy("syjid");
        Page<TrZhanglaM> page = new Page<TrZhanglaM>(pageNo, pageSize);
        IPage<TrZhanglaM> pageList = trZhanglaMService.page(page, queryWrapper);
        Map map = new HashMap();
        List list1 = new ArrayList();

        List<TrZhanglaM> records = pageList.getRecords();
        for (TrZhanglaM trZhanglaM1 : records) {
            TrZhanglaXxbM trZhanglaXxbM = new TrZhanglaXxbM();
            String syjid = trZhanglaM1.getSyjid();
            String shebeibianhao = trZhanglaM1.getShebeibianhao();
            List<TrZhanglaXxb> trZhanglaXxbList = trZhanglaXxbService.selectxxbList(syjid);
            List<TrZhanglaM> zhanglaMList = trZhanglaMService.selectmList(syjid);
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeibianhao);
            trZhanglaXxbM.setSyjid(syjid);
            trZhanglaXxbM.setId(trZhanglaM1.getId());
            trZhanglaXxbM.setShebeibianhao(shebeiInfo.getSbname());
            trZhanglaXxbM.setGsgs(trZhanglaM1.getGsgs());
            trZhanglaXxbM.setTxml(trZhanglaM1.getTxml());
            trZhanglaXxbM.setHtl(trZhanglaM1.getHtl());
            trZhanglaXxbM.setZscl(trZhanglaM1.getZscl());
            trZhanglaXxbM.setLlscl(trZhanglaM1.getLlscl());
            trZhanglaXxbM.setYxpc(trZhanglaM1.getYxpc());
            trZhanglaXxbM.setSclper(trZhanglaM1.getSclper());
            trZhanglaXxbM.setHege(trZhanglaM1.getHege());
            trZhanglaXxbM.setYzlb(trZhanglaM1.getYzlb());
            trZhanglaXxbM.setCzlb(trZhanglaM1.getCzlb());
            trZhanglaXxbM.setZzlb(trZhanglaM1.getZzlb());
            trZhanglaXxbM.setTrZhanglaXxbList(trZhanglaXxbList);
            trZhanglaXxbM.setZhanglaMList(zhanglaMList);
            list1.add(trZhanglaXxbM);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list1);
        return Result.OK(map);
    }

    /**
     * 孔道数/不合格统计
     *
     * @param trZhanglaM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息主表-孔道数/不合格统计")
    @ApiOperation(value = "张拉信息主表-孔道数/不合格统计", notes = "张拉信息主表-孔道数/不合格统计")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(TrZhanglaM trZhanglaM,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaM.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaM.setShebeibianhao(shebei);
            } else {
                trZhanglaM.setShebeibianhao("空");
            }
        }
        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        queryWrapper.select("ifnull(count(1),0) as id");
        if (StrUtil.isNotBlank(trZhanglaM.getHege())) {
            queryWrapper.groupBy("syjid");
        }
        List<TrZhanglaM> pageList = trZhanglaMService.list(queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 张拉孔信息以及断面数据查询(疏港三标梁场大屏)
     *
     * @param trZhanglaS
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息子表-张拉孔信息以及断面数据查询(疏港三标梁场大屏)")
    @ApiOperation(value = "张拉信息子表-张拉孔信息以及断面数据查询(疏港三标梁场大屏)", notes = "张拉信息子表-张拉孔信息以及断面数据查询(疏港三标梁场大屏)")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(TrZhanglaS trZhanglaS,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        if (trZhanglaS.getFguid() != null) {
            trZhanglaS.setFguid(trZhanglaS.getFguid());
        }
        trZhanglaS.setJdbfb("100");
        QueryWrapper<TrZhanglaS> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaS, req.getParameterMap());
        Page<TrZhanglaS> page = new Page<TrZhanglaS>(pageNo, pageSize);
        IPage<TrZhanglaS> pageList = trZhanglaSService.page(page, queryWrapper);
        List list = new ArrayList<>();
        for (TrZhanglaS trZhanglaS1 : pageList.getRecords()) {
            TrZhanglaS trZhanglaS2 = new TrZhanglaS();
            QueryWrapper<TrZhanglaM> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("fguid", trZhanglaS1.getFguid());
            TrZhanglaM trZhanglaM = trZhanglaMService.getOne(queryWrapper1);
            if (trZhanglaM != null) {
                trZhanglaS2.setYbds(trZhanglaM.getLlscl());//设计伸长量
                trZhanglaS2.setJdscl(trZhanglaM.getSjzll());//设计张拉力
            }
            trZhanglaS2.setFguid(trZhanglaS1.getFguid());//孔号唯一id
            trZhanglaS2.setJdzll(trZhanglaS1.getJdzll());//(100%)阶段张拉力
            list.add(trZhanglaS2);
        }
        return Result.OK(list);
    }

    /**
     * 不合格数据分页列表查询
     *
     * @param trZhanglaM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @ApiOperation(value = "张拉信息主表-不合格数据分页列表查询处置-审核", notes = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @GetMapping(value = "/list5")
    @PermissionData(pageComponent = "zlyj/TrZhanglachaobiaoCLList")
    public Result<?> queryPageList5(TrZhanglaM trZhanglaM,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaM.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaM.setShebeibianhao(shebei);
            } else {
                trZhanglaM.setShebeibianhao("空");
            }
        }
        trZhanglaM.setHege("0,不合格");
        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        if (!loginUser.getOrgCode().equals("A05A01A12A13")){
            queryWrapper.notLike("shebeibianhao", "A05A01A12A13");
        }
        queryWrapper.groupBy("syjid");
        Page<TrZhanglaM> page = new Page<TrZhanglaM>(pageNo, pageSize);
        IPage<TrZhanglaM> pageList = trZhanglaMService.page(page, queryWrapper);
        Map map = new HashMap();
        List list1 = new ArrayList();
        List<TrZhanglaM> records = pageList.getRecords();
        if (records.size() > 0) {
            for (TrZhanglaM trZhanglaM1 : records) {
                TrZhanglaXxbMCL trZhanglaXxbM = new TrZhanglaXxbMCL();
                String syjid = trZhanglaM1.getSyjid();
                String shebeibianhao = trZhanglaM1.getShebeibianhao();
                List<TrZhanglaXxb> trZhanglaXxbList = trZhanglaXxbService.selectxxbList(syjid);
                List<TrZhanglaM> zhanglaMList = trZhanglaMService.selectmList(syjid);
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeibianhao);
                trZhanglaXxbM.setSyjid(syjid);
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
                if (zhanglaYajiangOverHandler == null) {
                    ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                    zhanglaYajiangOverHandler1.setOverproofStatus(0);
                    trZhanglaXxbM.setOverproofStatus(0);
                    trZhanglaXxbM.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
                } else {
                    trZhanglaXxbM.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                    trZhanglaXxbM.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
                }
                trZhanglaXxbM.setId(trZhanglaM1.getId());
                trZhanglaXxbM.setShebeibianhao(shebeiInfo.getSbname());
                trZhanglaXxbM.setGsgs(trZhanglaM1.getGsgs());
                trZhanglaXxbM.setTxml(trZhanglaM1.getTxml());
                trZhanglaXxbM.setHtl(trZhanglaM1.getHtl());
                trZhanglaXxbM.setZscl(trZhanglaM1.getZscl());
                trZhanglaXxbM.setLlscl(trZhanglaM1.getLlscl());
                trZhanglaXxbM.setYxpc(trZhanglaM1.getYxpc());
                trZhanglaXxbM.setSclper(trZhanglaM1.getSclper());
                trZhanglaXxbM.setHege(trZhanglaM1.getOverLevel());
                trZhanglaXxbM.setYzlb(trZhanglaM1.getYzlb());
                trZhanglaXxbM.setCzlb(trZhanglaM1.getCzlb());
                trZhanglaXxbM.setZzlb(trZhanglaM1.getZzlb());
                trZhanglaXxbM.setTrZhanglaXxbList(trZhanglaXxbList);
                trZhanglaXxbM.setZhanglaMList(zhanglaMList);
                list1.add(trZhanglaXxbM);
            }
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list1);
        return Result.OK(map);
    }

    @AutoLog(value = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @ApiOperation(value = "张拉信息主表-不合格数据分页列表查询处置-审核", notes = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @GetMapping(value = "/list547")
    @PermissionData(pageComponent = "zlyj/TrZhanglachaobiaoCLList")
    public Result<?> queryPageList547(TrZhanglaM trZhanglaM,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaM.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaM.setShebeibianhao(shebei);
            } else {
                trZhanglaM.setShebeibianhao("空");
            }
        }
        // trZhanglaM.setHege("0,不合格");
        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        queryWrapper.gt("over_level",0);
        queryWrapper.ne("overproof_status",20);
        queryWrapper.groupBy("syjid");
        Page<TrZhanglaM> page = new Page<TrZhanglaM>(pageNo, pageSize);
        IPage<TrZhanglaM> pageList = trZhanglaMService.page(page, queryWrapper);
        Map map = new HashMap();
        List list1 = new ArrayList();
        List<TrZhanglaM> records = pageList.getRecords();
        if (records.size() > 0) {
            for (TrZhanglaM trZhanglaM1 : records) {
                TrZhanglaXxbMCL trZhanglaXxbM = new TrZhanglaXxbMCL();
                String syjid = trZhanglaM1.getSyjid();
                String shebeibianhao = trZhanglaM1.getShebeibianhao();
                List<TrZhanglaXxb> trZhanglaXxbList = trZhanglaXxbService.selectxxbList(syjid);
                List<TrZhanglaM> zhanglaMList = trZhanglaMService.selectmList(syjid);
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeibianhao);
                trZhanglaXxbM.setSyjid(syjid);
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
                if (zhanglaYajiangOverHandler == null) {
                    ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                    zhanglaYajiangOverHandler1.setOverproofStatus(0);
                    trZhanglaXxbM.setOverproofStatus(0);
                    trZhanglaXxbM.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
                } else {
                    trZhanglaXxbM.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                    trZhanglaXxbM.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
                }
                trZhanglaXxbM.setId(trZhanglaM1.getId());
                trZhanglaXxbM.setShebeibianhao(shebeiInfo.getSbname());
                trZhanglaXxbM.setGsgs(trZhanglaM1.getGsgs());
                trZhanglaXxbM.setTxml(trZhanglaM1.getTxml());
                trZhanglaXxbM.setHtl(trZhanglaM1.getHtl());
                trZhanglaXxbM.setZscl(trZhanglaM1.getZscl());
                trZhanglaXxbM.setLlscl(trZhanglaM1.getLlscl());
                trZhanglaXxbM.setYxpc(trZhanglaM1.getYxpc());
                trZhanglaXxbM.setSclper(trZhanglaM1.getSclper());
                trZhanglaXxbM.setHege(trZhanglaM1.getOverLevel());
                trZhanglaXxbM.setYzlb(trZhanglaM1.getYzlb());
                trZhanglaXxbM.setCzlb(trZhanglaM1.getCzlb());
                trZhanglaXxbM.setZzlb(trZhanglaM1.getZzlb());
                trZhanglaXxbM.setTrZhanglaXxbList(trZhanglaXxbList);
                trZhanglaXxbM.setZhanglaMList(zhanglaMList);
                list1.add(trZhanglaXxbM);
            }
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list1);
        return Result.OK(map);
    }

    /**
     * 不合格数据分页列表查询
     *
     * @param trZhanglaM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @ApiOperation(value = "张拉信息主表-不合格数据分页列表查询处置-审核", notes = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList6(TrZhanglaM trZhanglaM,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaM.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaM.setShebeibianhao(shebei);
            } else {
                trZhanglaM.setShebeibianhao("空");
            }
        }
        trZhanglaM.setHege("0,不合格");
        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        if (!loginUser.getOrgCode().equals("A05A01A12A13")){
            queryWrapper.notLike("shebeibianhao", "A05A01A12A13");
        }
        queryWrapper.groupBy("syjid");
        Page<TrZhanglaM> page = new Page<TrZhanglaM>(pageNo, pageSize);
        IPage<TrZhanglaM> pageList = trZhanglaMService.page(page, queryWrapper);
        Map map = new HashMap();
        List list1 = new ArrayList();
        List<TrZhanglaM> records = pageList.getRecords();
        if (records.size() > 0) {
            for (TrZhanglaM trZhanglaM1 : records) {
                TrZhanglaXxbMCL trZhanglaXxbM = new TrZhanglaXxbMCL();
                String syjid = trZhanglaM1.getSyjid();
                String shebeibianhao = trZhanglaM1.getShebeibianhao();
                List<TrZhanglaXxb> trZhanglaXxbList = trZhanglaXxbService.selectxxbList(syjid);
                List<TrZhanglaM> zhanglaMList = trZhanglaMService.selectmList(syjid);
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeibianhao);
                trZhanglaXxbM.setSyjid(syjid);
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
                if (zhanglaYajiangOverHandler == null) {
                    ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                    zhanglaYajiangOverHandler1.setOverproofStatus(0);
                    trZhanglaXxbM.setOverproofStatus(0);
                    trZhanglaXxbM.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
                } else {
                    trZhanglaXxbM.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                    trZhanglaXxbM.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
                }
                trZhanglaXxbM.setId(trZhanglaM1.getId());
                trZhanglaXxbM.setShebeibianhao(shebeiInfo.getSbname());
                trZhanglaXxbM.setGsgs(trZhanglaM1.getGsgs());
                trZhanglaXxbM.setTxml(trZhanglaM1.getTxml());
                trZhanglaXxbM.setHtl(trZhanglaM1.getHtl());
                trZhanglaXxbM.setZscl(trZhanglaM1.getZscl());
                trZhanglaXxbM.setLlscl(trZhanglaM1.getLlscl());
                trZhanglaXxbM.setYxpc(trZhanglaM1.getYxpc());
                trZhanglaXxbM.setSclper(trZhanglaM1.getSclper());
                trZhanglaXxbM.setHege(trZhanglaM1.getOverLevel());
                trZhanglaXxbM.setYzlb(trZhanglaM1.getYzlb());
                trZhanglaXxbM.setCzlb(trZhanglaM1.getCzlb());
                trZhanglaXxbM.setZzlb(trZhanglaM1.getZzlb());
                trZhanglaXxbM.setTrZhanglaXxbList(trZhanglaXxbList);
                trZhanglaXxbM.setZhanglaMList(zhanglaMList);
                list1.add(trZhanglaXxbM);
            }
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list1);
        return Result.OK(map);
    }


    @AutoLog(value = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @ApiOperation(value = "张拉信息主表-不合格数据分页列表查询处置-审核", notes = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @GetMapping(value = "/list647")
    public Result<?> queryPageList647(TrZhanglaM trZhanglaM,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaM.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaM.setShebeibianhao(shebei);
            } else {
                trZhanglaM.setShebeibianhao("空");
            }
        }
        // trZhanglaM.setHege("0,不合格");
        QueryWrapper<TrZhanglaM> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaM, req.getParameterMap());
        queryWrapper.gt("over_level",0);
        queryWrapper.groupBy("syjid");
        Page<TrZhanglaM> page = new Page<TrZhanglaM>(pageNo, pageSize);
        IPage<TrZhanglaM> pageList = trZhanglaMService.page(page, queryWrapper);
        Map map = new HashMap();
        List list1 = new ArrayList();
        List<TrZhanglaM> records = pageList.getRecords();
        if (records.size() > 0) {
            for (TrZhanglaM trZhanglaM1 : records) {
                TrZhanglaXxbMCL trZhanglaXxbM = new TrZhanglaXxbMCL();
                String syjid = trZhanglaM1.getSyjid();
                String shebeibianhao = trZhanglaM1.getShebeibianhao();
                List<TrZhanglaXxb> trZhanglaXxbList = trZhanglaXxbService.selectxxbList(syjid);
                List<TrZhanglaM> zhanglaMList = trZhanglaMService.selectmList(syjid);
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeibianhao);
                trZhanglaXxbM.setSyjid(syjid);
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
                if (zhanglaYajiangOverHandler == null) {
                    ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                    zhanglaYajiangOverHandler1.setOverproofStatus(0);
                    trZhanglaXxbM.setOverproofStatus(0);
                    trZhanglaXxbM.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
                } else {
                    trZhanglaXxbM.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                    trZhanglaXxbM.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
                }
                trZhanglaXxbM.setId(trZhanglaM1.getId());
                trZhanglaXxbM.setShebeibianhao(shebeiInfo.getSbname());
                trZhanglaXxbM.setGsgs(trZhanglaM1.getGsgs());
                trZhanglaXxbM.setTxml(trZhanglaM1.getTxml());
                trZhanglaXxbM.setHtl(trZhanglaM1.getHtl());
                trZhanglaXxbM.setZscl(trZhanglaM1.getZscl());
                trZhanglaXxbM.setLlscl(trZhanglaM1.getLlscl());
                trZhanglaXxbM.setYxpc(trZhanglaM1.getYxpc());
                trZhanglaXxbM.setSclper(trZhanglaM1.getSclper());
                trZhanglaXxbM.setHege(trZhanglaM1.getOverLevel());
                trZhanglaXxbM.setYzlb(trZhanglaM1.getYzlb());
                trZhanglaXxbM.setCzlb(trZhanglaM1.getCzlb());
                trZhanglaXxbM.setZzlb(trZhanglaM1.getZzlb());
                trZhanglaXxbM.setTrZhanglaXxbList(trZhanglaXxbList);
                trZhanglaXxbM.setZhanglaMList(zhanglaMList);
                list1.add(trZhanglaXxbM);
            }
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list1);
        return Result.OK(map);
    }

    /**
     * 不合格数据分页列表查询
     *
     * @return
     */
    @AutoLog(value = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @ApiOperation(value = "张拉信息主表-不合格数据分页列表查询处置-审核", notes = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @GetMapping(value = "/listFb")
    public Result<?> queryPageListFb(String sysOrgCode) {
        List<String> shebeiList = shebeiInfoService.selectShebeiList(sysOrgCode, 9);

        QueryWrapper<TrZhanglaM> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("shebeibianhao", shebeiList);
        queryWrapper.groupBy("syjid");
        List<TrZhanglaM> list = trZhanglaMService.list(queryWrapper);

        QueryWrapper<TrZhanglaM> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("shebeibianhao", shebeiList);
        queryWrapper1.eq("hege", "0");
        queryWrapper1.groupBy("syjid");
        List<TrZhanglaM> list1 = trZhanglaMService.list(queryWrapper1);

        QueryWrapper<TrZhanglaM> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("shebeibianhao", shebeiList);
        queryWrapper2.eq("hege", "不合格");
        queryWrapper2.groupBy("syjid");
        List<TrZhanglaM> list2 = trZhanglaMService.list(queryWrapper2);
        Map map = new HashMap();
        map.put("sum", list.size());
        map.put("bhg", list1.size() + list2.size());

        return Result.OK(map);
    }

    /**
     * 不合格数据分页列表查询
     *
     * @return
     */
    @AutoLog(value = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @ApiOperation(value = "张拉信息主表-不合格数据分页列表查询处置-审核", notes = "张拉信息主表-不合格数据分页列表查询处置-审核")
    @GetMapping(value = "/listZt")
    public Result<?> queryPageListZt() {
        ArrayList<String> list5 = new ArrayList<>();
        list5.add("A05A01A05A01A01A01A01");
        list5.add("A05A01A05A01A01A01A02");
        list5.add("A05A01A05A01A01A01A03");
        list5.add("A05A01A05A01A01A01A04");
        List<String> shebeiList = new ArrayList<>();
        for (String l : list5) {
            List<String> shebeiList1 = shebeiInfoService.selectShebeiList(l, 9);
            for (String l1 : shebeiList1) {
                shebeiList.add(l1);
            }
        }

        QueryWrapper<TrZhanglaM> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("shebeibianhao", shebeiList);
        queryWrapper.groupBy("syjid");
        List<TrZhanglaM> list = trZhanglaMService.list(queryWrapper);

        QueryWrapper<TrZhanglaM> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("shebeibianhao", shebeiList);
        queryWrapper1.eq("hege", "0");
        queryWrapper1.groupBy("syjid");
        List<TrZhanglaM> list1 = trZhanglaMService.list(queryWrapper1);

        QueryWrapper<TrZhanglaM> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("shebeibianhao", shebeiList);
        queryWrapper2.eq("hege", "不合格");
        queryWrapper2.groupBy("syjid");
        List<TrZhanglaM> list2 = trZhanglaMService.list(queryWrapper2);
        Map map = new HashMap();
        map.put("sum", list.size());
        map.put("bhg", list1.size() + list2.size());

        return Result.OK(map);
    }

    /**
     * 张拉压浆根据省/市/项目统计预警数/不合格率/闭合率对外接口
     *
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    @AutoLog(value = "张拉信息主表-根据省/市/项目统计预警数/不合格率/闭合率对外接口")
    @ApiOperation(value = "张拉信息主表-根据省/市/项目统计预警数/不合格率/闭合率对外接口", notes = "张拉信息主表-根据省/市/项目统计预警数/不合格率/闭合率对外接口")
    @GetMapping(value = "/list7")
    public Result<?> queryPageList7(String sysOrgCode) {
        List<String> list = new ArrayList<>();
        List<ShebeiInfo> shebeilist = shebeiInfoService.arrayOneshebei(sysOrgCode);
        for (ShebeiInfo shebeiInfo : shebeilist) {
            list.add(shebeiInfo.getSbjno());
        }
        Map<String, Integer> map = new HashMap<>();
        List<String> list1 = new ArrayList<>();
        double zlsum = 0.0;
        double zlyjsum = 0.0;
        double zlbhsum = 0.0;
        double sum = 0.0;
        double yjsum = 0.0;
        double bhsum = 0.0;
        double zsum = 0.0;
        double zyjsum = 0.0;
        double zbhsum = 0.0;
        double zlyjlv = 0.0;
        double zlbhlv = 0.0;
        double hntsum = 0.0;
        double hutchaobiao = 0.0;
        double hntbhsum = 0.0;
        QueryWrapper<TrZhanglaM> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("syjid");
        queryWrapper.in("shebeibianhao", list);
        queryWrapper.groupBy("syjid");
        List<TrZhanglaM> one = trZhanglaMService.list(queryWrapper);
        zsum = one.size();
        QueryWrapper<TrZhanglaM> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("syjid");
        queryWrapper1.in("shebeibianhao", shebeilist);
        queryWrapper1.in("hege", "0", "不合格");
        queryWrapper1.groupBy("syjid");
        List<TrZhanglaM> one1 = trZhanglaMService.list(queryWrapper);
        zyjsum = one1.size();
        QueryWrapper<TrZhanglaM> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select("ifnull(count(1),0) as hege");
        queryWrapper2.in("shebeibianhao", shebeilist);
        queryWrapper2.in("hege", "0", "不合格");
        queryWrapper2.eq("overproof_status", 20);
        queryWrapper2.groupBy("syjid");
        List<TrZhanglaM> one2 = trZhanglaMService.list(queryWrapper);
        zbhsum = one2.size();
        QueryWrapper<TrYajiangM> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.in("yjsbbh", list);
        List<TrYajiangM> one3 = trYajiangMService.list(queryWrapper3);
        if (one3.size() > 0) {
            for (TrYajiangM trYajiangM : one3) {
                list1.add(trYajiangM.getSyjid());
            }
            QueryWrapper<TrYajiangS> queryWrapper4 = new QueryWrapper<>();
            queryWrapper4.in("syjid", list1);
            queryWrapper4.groupBy("syjid");
            List<TrYajiangS> one5 = trYajiangSService.list(queryWrapper4);
            sum = one5.size();
            QueryWrapper<TrYajiangS> queryWrapper5 = new QueryWrapper<>();
            queryWrapper5.in("syjid", list1);
            queryWrapper2.in("hege", "0", "不合格");
            queryWrapper5.groupBy("syjid");
            List<TrYajiangS> one6 = trYajiangSService.list(queryWrapper5);
            yjsum = one6.size();
            QueryWrapper<TrYajiangS> queryWrapper7 = new QueryWrapper<>();
            queryWrapper7.in("syjid", list1);
            queryWrapper2.in("hege", "0", "不合格");
            queryWrapper7.eq("overproof_status", 20);
            queryWrapper7.groupBy("syjid");
            List<TrYajiangS> one4 = trYajiangSService.list(queryWrapper7);
            bhsum = one4.size();
        }
        QueryWrapper<BhzCementStatistics> queryWrapper8 = new QueryWrapper<>();
        queryWrapper8.select("sum(all_dish) as all_dish,sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper8.in("shebei_no", list);
        BhzCementStatistics bhzCementStatisticsone = bhzCementStatisticsService.getOne(queryWrapper8);
        if (bhzCementStatisticsone != null) {
            hntsum = bhzCementStatisticsone.getAllDish();
            hutchaobiao = bhzCementStatisticsone.getAllOverproofDish();
        }
        QueryWrapper<BhzCementBase> queryWrapper9 = new QueryWrapper<>();
        queryWrapper9.select("count(*) as id");
        queryWrapper9.in("shebei_no", list);
        queryWrapper9.eq("overproof_status", 20);
        BhzCementBase bhzCementBaseone = bhzCementBaseService.getOne(queryWrapper9);
        if (bhzCementBaseone != null) {
            hntbhsum = bhzCementBaseone.getId();
        }
        zlsum = zsum + sum + hntsum;
        zlyjsum = zyjsum + yjsum + hutchaobiao;
        zlbhsum = zbhsum + bhsum + hntbhsum;
        if (zlsum != 0) {
            zlyjlv = (zlyjsum / zlsum) * 100;
        }
        if (zlyjsum != 0) {
            zlbhlv = (zlbhsum / zlyjsum) * 100;
        }
        map.put("zlyjsum", (int) zlyjsum);
        map.put("zlyjlv", (int) zlyjlv);
        map.put("zlbhlv", (int) zlbhlv);
        return Result.OK(map);
    }

    /**
     * 添加
     *
     * @param trZhanglaM
     * @return
     */
    @AutoLog(value = "张拉信息主表-添加")
    @ApiOperation(value = "张拉信息主表-添加", notes = "张拉信息主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrZhanglaM trZhanglaM) {
        trZhanglaMService.save(trZhanglaM);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trZhanglaM
     * @return
     */
    @AutoLog(value = "张拉信息主表-编辑")
    @ApiOperation(value = "张拉信息主表-编辑", notes = "张拉信息主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrZhanglaM trZhanglaM) {
        trZhanglaMService.updateById(trZhanglaM);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "张拉信息主表-通过id删除")
    @ApiOperation(value = "张拉信息主表-通过id删除", notes = "张拉信息主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trZhanglaMService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "张拉信息主表-批量删除")
    @ApiOperation(value = "张拉信息主表-批量删除", notes = "张拉信息主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trZhanglaMService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "张拉信息主表-通过id查询")
    @ApiOperation(value = "张拉信息主表-通过id查询", notes = "张拉信息主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrZhanglaM trZhanglaM = trZhanglaMService.getById(id);
        if (trZhanglaM == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trZhanglaM);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param trZhanglaM
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrZhanglaM trZhanglaM) {
        return super.exportXls(request, trZhanglaM, TrZhanglaM.class, "张拉信息主表");
    }

    /**
     * 张拉详情数据导出excel
     *
     * @param trZhanglaXxb
     * @param id
     * @param req
     * @return
     */
    @RequestMapping(value = "/export")
    public Result<?> export(TrZhanglaXxb trZhanglaXxb,
                            Integer id,
                            HttpServletRequest req) {
        List data = new ArrayList<>();

        QueryWrapper<TrZhanglaXxb> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaXxb, req.getParameterMap());
        queryWrapper.eq("id", id);
        TrZhanglaXxb one = trZhanglaXxbService.getOne(queryWrapper);

        List<TrZhanglaM> zhanglaMList = trZhanglaMService.selectmList(one.getSyjid());
        for (TrZhanglaM trzhanglaM : zhanglaMList) {

            String fguid = trzhanglaM.getFguid();

            List<TrZhanglaS> ZhanglaSList = trZhanglaSService.selelctList(one.getSyjid(), fguid);
            for (TrZhanglaS trZhanglaS : ZhanglaSList) {
                TrZhanglaMS trZhanglaMS = new TrZhanglaMS();

                trZhanglaMS.setGsbh(trzhanglaM.getGsbh());//孔号
                trZhanglaMS.setZlsj(trzhanglaM.getZlsj());//张拉时间
                trZhanglaMS.setSjzll(trzhanglaM.getSjzll());//设计张拉力
                trZhanglaMS.setZscl(trzhanglaM.getZscl());//总伸长量
                trZhanglaMS.setLlscl(trzhanglaM.getLlscl());//理论伸长量
                trZhanglaMS.setSclper(trzhanglaM.getSclper());//延长量误差
                trZhanglaMS.setZzlb(trzhanglaM.getZzlb());//超张拉百分比
                trZhanglaMS.setJlsj(trzhanglaM.getJlsj());//记录时间
                String hege = trzhanglaM.getHege();//合格
                switch (hege){
                    case "1":
                        trZhanglaMS.setHege("合格");
                        break;
                    case "0":
                        trZhanglaMS.setHege("不合格");
                        break;
                }

                trZhanglaMS.setDh(trZhanglaS.getDh());//张拉断面
                trZhanglaMS.setJdbfb(trZhanglaS.getJdbfb());//阶段行程
                trZhanglaMS.setJdzll(trZhanglaS.getJdzll());//张拉力
                trZhanglaMS.setChsj(trZhanglaS.getChsj());//持荷时间
                trZhanglaMS.setTrZhanglaSList(ZhanglaSList);
                data.add(trZhanglaMS);
            }
        }
        return Result.OKs(data);
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
        return super.importExcel(request, response, TrZhanglaM.class);
    }

    /**
     * 张拉当月超标统计
     *
     * @return
     */
    @RequestMapping(value = "/countList", method = RequestMethod.GET)
    public Result<?> countList() {
        return Result.OK(trZhanglaMService.countList());
    }
}
