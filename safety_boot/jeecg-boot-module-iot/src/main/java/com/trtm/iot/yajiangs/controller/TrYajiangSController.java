package com.trtm.iot.yajiangs.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangm.vo.YaJiangmsss;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.vo.TrYajiangSvo;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;

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
 * @Description: 压浆实时数据表
 * @Author: jeecg-boot
 * @Date: 2021-09-06
 * @Version: V1.0
 */
@Api(tags = "压浆实时数据表")
@RestController
@RequestMapping("/yajiangs/trYajiangS")
@Slf4j
public class TrYajiangSController extends JeecgController<TrYajiangS, ITrYajiangSService> {
    @Autowired
    private ITrYajiangSService trYajiangSService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ITrYajiangMService trYajiangMService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrYajiangRenwudanService yajiangRenwudanService;
    @Autowired
    private IZhanglaYajiangOverHandlerService zhanglaYajiangOverHandlerService;

    /**
     * 分页列表查询
     *
     * @param trYajiangS
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "压浆实时数据表-分页列表查询")
    @ApiOperation(value = "压浆实时数据表-分页列表查询", notes = "压浆实时数据表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrYajiangS trYajiangS,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrYajiangS> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangS, req.getParameterMap());
        Page<TrYajiangS> page = new Page<TrYajiangS>(pageNo, pageSize);
        IPage<TrYajiangS> pageList = trYajiangSService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 根据主表syjid查询对应的数据
     *
     * @param trYajiangS
     * @param req
     * @return
     */
    @AutoLog(value = "根据主表syjid查询对应的数据")
    @ApiOperation(value = "根据主表syjid查询对应的数据", notes = "根据主表syjid查询对应的数据")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(TrYajiangS trYajiangS, HttpServletRequest req) {
        QueryWrapper<TrYajiangS> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangS, req.getParameterMap());
        List<TrYajiangS> list = trYajiangSService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 根据主表syjid查询对应的数据
     *
     * @param trYajiangS
     * @param req
     * @return
     */
    @AutoLog(value = "根据主表syjid查询对应的数据")
    @ApiOperation(value = "根据主表syjid查询对应的数据", notes = "根据主表syjid查询对应的数据")
    @GetMapping(value = "/listjxgs")
    public Result<?> queryPageListjxgs(TrYajiangS trYajiangS, HttpServletRequest req) {
        String code = trYajiangS.getSyjid();
        QueryWrapper<TrYajiangRenwudan> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("code", code);
        List<TrYajiangRenwudan> list1 = yajiangRenwudanService.list(queryWrapper1);
        if (list1.size() > 0) {
            TrYajiangRenwudan trYajiangRenwudan = list1.get(0);
            QueryWrapper<TrYajiangM> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("uuid", trYajiangRenwudan.getUuid());
            List<TrYajiangM> list2 = trYajiangMService.list(queryWrapper2);
            if (list2.size() > 0) {
                TrYajiangM yajiangM = list2.get(0);
                String syjid = yajiangM.getSyjid();
                return Result.OK(syjid);
            }
        }
        return Result.OK("请关联对应压浆任务单！！！");
    }

    @AutoLog(value = "根据主表syjid查询对应的数据")
    @ApiOperation(value = "根据主表syjid查询对应的数据", notes = "根据主表syjid查询对应的数据")
    @GetMapping(value = "/listfz")
    public Result<?> queryPageListfz(TrYajiangS trYajiangS, HttpServletRequest req) {
        try {
            QueryWrapper<TrYajiangS> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangS, req.getParameterMap());
            List<TrYajiangS> list = trYajiangSService.list(queryWrapper);

            System.out.println(trYajiangS.getSyjid());
            TrYajiangM one = trYajiangMService.selectceslll(trYajiangS.getSyjid());

            ArrayList<TrYajiangSvo> list1 = new ArrayList<>();
            if (one != null) {
                QueryWrapper<TrYajiangRenwudan> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("uuid", one.getUuid());
                TrYajiangRenwudan one1 = yajiangRenwudanService.getOne(queryWrapper2);
                if (one1 != null) {
                    for (TrYajiangS l : list) {
                        TrYajiangSvo trYajiangSvo = new TrYajiangSvo();
                        trYajiangSvo.setUuid(one.getUuid());
                        trYajiangSvo.setZlsj(l.getZkyl());
                        trYajiangSvo.setYjsj(l.getYajiangsj());
                        trYajiangSvo.setYjwd(one.getYjwd());
                        trYajiangSvo.setShuijiaobi(l.getShuijiaobi());
                        trYajiangSvo.setYjd(one.getYjd());
                        trYajiangSvo.setLljl(one1.getLljl());
                        trYajiangSvo.setSjjl(l.getJinjiangshu());
                        trYajiangSvo.setSjmd(one1.getSjmd());
                        trYajiangSvo.setSjmdm(one.getSjmd());
                        trYajiangSvo.setJinjiangyal(l.getJinjiangyal());
                        trYajiangSvo.setWysj(one.getWysj());
                        trYajiangSvo.setIsOverLevel(one.getIsOverLevel());
                        trYajiangSvo.setOverproofStatus(one.getOverproofStatus());
                        list1.add(trYajiangSvo);
                    }
                }
            }
            return Result.OK(list1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("报错了！");
        }
    }

    /**
     * 超标的数据
     *
     * @param trYajiangS
     * @param req
     * @return
     */
    @AutoLog(value = "超标的数据")
    @ApiOperation(value = "超标的数据", notes = "超标的数据")
    @GetMapping(value = "/list2")
    @PermissionData(pageComponent = "zlyj/TrYajiangchaobiaoCLList")
    public Result<?> queryPageList2(TrYajiangS trYajiangS,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String shebeibianhao = req.getParameter("shebeibianhao");
        IPage<TrYajiangS> pageList = trYajiangSService.selectListPage(pageNo, pageSize, shebeis, shebeibianhao);
        List<TrYajiangS> records = pageList.getRecords();
        List list = new ArrayList();
        Map map = new HashMap();
        for (TrYajiangS record : records) {
            YaJiangmsss yaJiangmsss = new YaJiangmsss();
            String syjid = record.getSyjid();
            TrYajiangM trYajiangM = trYajiangMService.selectgetOne(syjid);
            QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid", syjid);
            List<TrYajiangS> list1 = trYajiangSService.list(queryWrapper);
            String yjsbbh = trYajiangM.getYjsbbh();
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(yjsbbh);
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
            if (zhanglaYajiangOverHandler == null) {
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                zhanglaYajiangOverHandler1.setOverproofStatus(0);
                yaJiangmsss.setOverproofStatus(0);
                yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
            } else {
                yaJiangmsss.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
            }
            yaJiangmsss.setSyjid(syjid);
            yaJiangmsss.setSgdw(trYajiangM.getSgdw());
            yaJiangmsss.setGcmc(trYajiangM.getGcmc());
            yaJiangmsss.setYjsj(trYajiangM.getYjsj());
            yaJiangmsss.setSgbw(trYajiangM.getSgbw());
            yaJiangmsss.setGjjg(trYajiangM.getGjjg());
            yaJiangmsss.setQw(trYajiangM.getQw());
            yaJiangmsss.setSw(trYajiangM.getSw());
            yaJiangmsss.setShuijiaobi(trYajiangM.getShuijiaobi());
            yaJiangmsss.setYjwd(trYajiangM.getYjwd());
            yaJiangmsss.setYjsbbh(shebeiInfo.getSbname());
            yaJiangmsss.setZlsj(trYajiangM.getZlsj());
            yaJiangmsss.setKongdaoshu(trYajiangM.getKongdaoshu());
            yaJiangmsss.setLianghao(trYajiangM.getLianghao());
            yaJiangmsss.setOverReason(trYajiangM.getOverReason());
            yaJiangmsss.setYajiangs(list1);
            list.add(yaJiangmsss);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list);
        return Result.OK(map);
    }

    /**
     * 超标的数据
     *
     * @param trYajiangS
     * @param req
     * @return
     */
    @AutoLog(value = "超标的数据")
    @ApiOperation(value = "超标的数据", notes = "超标的数据")
    @GetMapping(value = "/list2s")
    @PermissionData(pageComponent = "zlyj/TrYajiangchaobiaoCLList")
    public Result<?> queryPageList2s(TrYajiangS trYajiangS,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {

        QueryWrapper<TrYajiangS> queryWrappers = QueryGenerator.initQueryWrapper(trYajiangS, req.getParameterMap());
        Page<TrYajiangS> page = new Page<TrYajiangS>(pageNo, pageSize);
        IPage<TrYajiangS> pageList = trYajiangSService.page(page, queryWrappers);
        List<TrYajiangS> records = pageList.getRecords();
        List list = new ArrayList();
        Map map = new HashMap();
        for (TrYajiangS record : records) {
            YaJiangmsss yaJiangmsss = new YaJiangmsss();
            String syjid = record.getSyjid();
            TrYajiangM trYajiangM = trYajiangMService.selectgetOne(syjid);
            QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid", syjid);
            queryWrapper.eq("is_over_level", 1);
            List<TrYajiangS> list1 = trYajiangSService.list(queryWrapper);
            String yjsbbh = trYajiangM.getYjsbbh();
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(yjsbbh);
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
            if (zhanglaYajiangOverHandler == null) {
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                zhanglaYajiangOverHandler1.setOverproofStatus(0);
                yaJiangmsss.setOverproofStatus(0);
                yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
            } else {
                yaJiangmsss.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
            }
            yaJiangmsss.setSyjid(syjid);
            yaJiangmsss.setSgdw(trYajiangM.getSgdw());
            yaJiangmsss.setGcmc(trYajiangM.getGcmc());
            yaJiangmsss.setYjsj(trYajiangM.getYjsj());
            yaJiangmsss.setSgbw(trYajiangM.getSgbw());
            yaJiangmsss.setGjjg(trYajiangM.getGjjg());
            yaJiangmsss.setQw(trYajiangM.getQw());
            yaJiangmsss.setSw(trYajiangM.getSw());
            yaJiangmsss.setShuijiaobi(trYajiangM.getShuijiaobi());
            yaJiangmsss.setYjwd(trYajiangM.getYjwd());
            yaJiangmsss.setYjsbbh(shebeiInfo.getSbname());
            yaJiangmsss.setZlsj(trYajiangM.getZlsj());
            yaJiangmsss.setKongdaoshu(trYajiangM.getKongdaoshu());
            yaJiangmsss.setLianghao(trYajiangM.getLianghao());
            yaJiangmsss.setOverReason(trYajiangM.getOverReason());
            yaJiangmsss.setYajiangs(list1);
            list.add(yaJiangmsss);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list);
        return Result.OK(map);
    }


    /**
     * 超标的数据
     *
     * @param trYajiangS
     * @param req
     * @return
     */
    @AutoLog(value = "超标的数据")
    @ApiOperation(value = "超标的数据", notes = "超标的数据")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(TrYajiangS trYajiangS,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String shebeibianhao = req.getParameter("shebeibianhao");
        IPage<TrYajiangS> pageList = trYajiangSService.selectChaobiaoListPage1(pageNo, pageSize, shebeis, shebeibianhao, null);
        List<TrYajiangS> records = pageList.getRecords();
        List list = new ArrayList();
        Map map = new HashMap();
        for (TrYajiangS record : records) {
            if (record.getOverproofStatus() != 20) {
                YaJiangmsss yaJiangmsss = new YaJiangmsss();
                String syjid = record.getSyjid();
                TrYajiangM trYajiangM = trYajiangMService.selectgetOne(syjid);
                if(ObjectUtil.isEmpty(trYajiangM) ){continue;}
                QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("syjid", syjid);
                List<TrYajiangS> list1 = trYajiangSService.list(queryWrapper);
                String yjsbbh = trYajiangM.getYjsbbh();
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(yjsbbh);
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
                if (zhanglaYajiangOverHandler == null) {
                    ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                    zhanglaYajiangOverHandler1.setOverproofStatus(0);
                    yaJiangmsss.setOverproofStatus(0);
                    yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
                } else {
                    yaJiangmsss.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                    yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
                }
                yaJiangmsss.setId(trYajiangM.getId());
                yaJiangmsss.setSyjid(syjid);
                yaJiangmsss.setSgdw(trYajiangM.getSgdw());
                yaJiangmsss.setGcmc(trYajiangM.getGcmc());
                yaJiangmsss.setYjsj(trYajiangM.getYjsj());
                yaJiangmsss.setSgbw(trYajiangM.getSgbw());
                yaJiangmsss.setGjjg(trYajiangM.getGjjg());
                yaJiangmsss.setQw(trYajiangM.getQw());
                yaJiangmsss.setSw(trYajiangM.getSw());
                yaJiangmsss.setShuijiaobi(trYajiangM.getShuijiaobi());
                yaJiangmsss.setYjwd(trYajiangM.getYjwd());
                yaJiangmsss.setYjsbbh(shebeiInfo.getSbname());
                yaJiangmsss.setZlsj(trYajiangM.getZlsj());
                yaJiangmsss.setKongdaoshu(trYajiangM.getKongdaoshu());
                yaJiangmsss.setOverReason(trYajiangM.getOverReason());
                yaJiangmsss.setLianghao(trYajiangM.getLianghao());
                yaJiangmsss.setYajiangs(list1);
                list.add(yaJiangmsss);
            }
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list);
        return Result.OK(map);
    }
    /**
     * 超标的数据
     *
     * @param trYajiangS
     * @param req
     * @return
     */
    @AutoLog(value = "超标的数据")
    @ApiOperation(value = "超标的数据", notes = "超标的数据")
    @GetMapping(value = "/list34")
    public Result<?> queryPageList34(TrYajiangS trYajiangS,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String shebeibianhao = req.getParameter("shebeibianhao");
        IPage<TrYajiangS> pageList = trYajiangSService.selectChaobiaoListPage1(pageNo, pageSize, shebeis, shebeibianhao, 20);
        List<TrYajiangS> records = pageList.getRecords();
        List list = new ArrayList();
        Map map = new HashMap();
        for (TrYajiangS record : records) {
            YaJiangmsss yaJiangmsss = new YaJiangmsss();
            String syjid = record.getSyjid();
            TrYajiangM trYajiangM = trYajiangMService.selectgetOne(syjid);
            QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid", syjid);
            List<TrYajiangS> list1 = trYajiangSService.list(queryWrapper);
            String yjsbbh = trYajiangM.getYjsbbh();
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(yjsbbh);
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
            if (zhanglaYajiangOverHandler == null) {
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                zhanglaYajiangOverHandler1.setOverproofStatus(0);
                yaJiangmsss.setOverproofStatus(0);
                yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
            } else {
                yaJiangmsss.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
            }
            yaJiangmsss.setId(trYajiangM.getId());
            yaJiangmsss.setSyjid(syjid);
            yaJiangmsss.setSgdw(trYajiangM.getSgdw());
            yaJiangmsss.setGcmc(trYajiangM.getGcmc());
            yaJiangmsss.setYjsj(trYajiangM.getYjsj());
            yaJiangmsss.setSgbw(trYajiangM.getSgbw());
            yaJiangmsss.setGjjg(trYajiangM.getGjjg());
            yaJiangmsss.setQw(trYajiangM.getQw());
            yaJiangmsss.setSw(trYajiangM.getSw());
            yaJiangmsss.setShuijiaobi(trYajiangM.getShuijiaobi());
            yaJiangmsss.setYjwd(trYajiangM.getYjwd());
            yaJiangmsss.setYjsbbh(shebeiInfo.getSbname());
            yaJiangmsss.setZlsj(trYajiangM.getZlsj());
            yaJiangmsss.setKongdaoshu(trYajiangM.getKongdaoshu());
            yaJiangmsss.setOverReason(trYajiangM.getOverReason());
            yaJiangmsss.setLianghao(trYajiangM.getLianghao());
            yaJiangmsss.setYajiangs(list1);
            list.add(yaJiangmsss);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list);
        return Result.OK(map);
    }
    /**
     * 超标的数据
     *
     * @param trYajiangS
     * @param req
     * @return
     */
    @AutoLog(value = "超标的数据")
    @ApiOperation(value = "超标的数据", notes = "超标的数据")
    @GetMapping(value = "/lists3")
    public Result<?> queryPageLists3(TrYajiangS trYajiangS,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String shebeibianhao = req.getParameter("shebeibianhao");
        IPage<TrYajiangS> pageList = trYajiangSService.selectChaobiaoListPage1(pageNo, pageSize, shebeis, shebeibianhao, trYajiangS.getOverproofStatus());
        List<TrYajiangS> records = pageList.getRecords();
        List list = new ArrayList();
        Map map = new HashMap();
        for (TrYajiangS record : records) {
            YaJiangmsss yaJiangmsss = new YaJiangmsss();
            String syjid = record.getSyjid();
            TrYajiangM trYajiangM = trYajiangMService.selectgetOne(syjid);
            QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid", syjid);
            List<TrYajiangS> list1 = trYajiangSService.list(queryWrapper);
            String yjsbbh = trYajiangM.getYjsbbh();
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(yjsbbh);
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
            if (zhanglaYajiangOverHandler == null) {
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                zhanglaYajiangOverHandler1.setOverproofStatus(0);
                yaJiangmsss.setOverproofStatus(0);
                yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
            } else {
                yaJiangmsss.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
            }
            yaJiangmsss.setId(trYajiangM.getId());
            yaJiangmsss.setSyjid(syjid);
            yaJiangmsss.setSgdw(trYajiangM.getSgdw());
            yaJiangmsss.setGcmc(trYajiangM.getGcmc());
            yaJiangmsss.setYjsj(trYajiangM.getYjsj());
            yaJiangmsss.setSgbw(trYajiangM.getSgbw());
            yaJiangmsss.setGjjg(trYajiangM.getGjjg());
            yaJiangmsss.setQw(trYajiangM.getQw());
            yaJiangmsss.setSw(trYajiangM.getSw());
            yaJiangmsss.setShuijiaobi(trYajiangM.getShuijiaobi());
            yaJiangmsss.setYjwd(trYajiangM.getYjwd());
            yaJiangmsss.setYjsbbh(shebeiInfo.getSbname());
            yaJiangmsss.setZlsj(trYajiangM.getZlsj());
            yaJiangmsss.setKongdaoshu(trYajiangM.getKongdaoshu());
            yaJiangmsss.setOverReason(trYajiangM.getOverReason());
            yaJiangmsss.setLianghao(trYajiangM.getLianghao());
            yaJiangmsss.setYajiangs(list1);
            list.add(yaJiangmsss);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list);
        return Result.OK(map);
    }
//
//    /**
//     * 根据省/市/项目统计预警数/不合格率/闭合率对外接口(张拉压浆合在一起计算该接口暂时不用)
//     *
//     * @param
//     * @param
//     * @return
//     */
//    @AutoLog(value = "根据省/市/项目统计预警数/不合格率/闭合率对外接口")
//    @ApiOperation(value = "根据省/市/项目统计预警数/不合格率/闭合率对外接口", notes = "根据省/市/项目统计预警数/不合格率/闭合率对外接口")
//    @GetMapping(value = "/list4")
//    public Result<?> queryPageList4(String sysOrgCode) {
//        List<String> list = new ArrayList<>();
//        List<ShebeiInfo> shebeilist = shebeiInfoService.arrayOneshebei(sysOrgCode);
//        for (ShebeiInfo shebeiInfo : shebeilist) {
//            list.add(shebeiInfo.getSbjno());
//        }
//        Map<String, Integer> map = new HashMap<>();
//        double sum = 0.0;
//        double yjsum = 0.0;
//        double bhsum = 0.0;
//        double yjlv = 0.0;
//        double bhlv = 0.0;
//        List<String> list1 = new ArrayList<>();
//        QueryWrapper<TrYajiangM> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("yjsbbh", list);
//        List<TrYajiangM> one = trYajiangMService.list(queryWrapper);
//        if (one.size() > 0) {
//            for (TrYajiangM trYajiangM : one) {
//                list1.add(trYajiangM.getSyjid());
//            }
//            QueryWrapper<TrYajiangS> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.in("syjid", list1);
//            queryWrapper1.groupBy("syjid");
//            List<TrYajiangS> one2 = trYajiangSService.list(queryWrapper1);
//            sum = one2.size();
//            QueryWrapper<TrYajiangS> queryWrapper2 = new QueryWrapper<>();
//            queryWrapper1.in("syjid", list1);
//            queryWrapper2.eq("hege", "0");
//            queryWrapper2.groupBy("syjid");
//            List<TrYajiangS> one3 = trYajiangSService.list(queryWrapper2);
//            yjsum = one3.size();
//            QueryWrapper<TrYajiangS> queryWrapper3 = new QueryWrapper<>();
//            queryWrapper1.in("syjid", list1);
//            queryWrapper3.eq("hege", "0");
//            queryWrapper3.eq("overproof_status", 20);
//            queryWrapper3.groupBy("syjid");
//            List<TrYajiangS> one4 = trYajiangSService.list(queryWrapper3);
//            ;
//            bhsum = one4.size();
//            if (sum != 0) {
//                yjlv = (yjsum / sum) * 100;
//            }
//            if (yjsum != 0) {
//                bhlv = (bhsum / yjsum) * 100;
//            }
//        }
//        map.put("yjsum", (int) yjsum);
//        map.put("yjlv", (int) yjlv);
//        map.put("bhlv", (int) bhlv);
//        return Result.OK(map);
//    }

    /**
     * 添加
     *
     * @param trYajiangS
     * @return
     */
    @AutoLog(value = "压浆实时数据表-添加")
    @ApiOperation(value = "压浆实时数据表-添加", notes = "压浆实时数据表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrYajiangS trYajiangS) {
        trYajiangSService.save(trYajiangS);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trYajiangS
     * @return
     */
    @AutoLog(value = "压浆实时数据表-编辑")
    @ApiOperation(value = "压浆实时数据表-编辑", notes = "压浆实时数据表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrYajiangS trYajiangS) {
        trYajiangSService.updateById(trYajiangS);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "压浆实时数据表-通过id删除")
    @ApiOperation(value = "压浆实时数据表-通过id删除", notes = "压浆实时数据表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trYajiangSService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "压浆实时数据表-批量删除")
    @ApiOperation(value = "压浆实时数据表-批量删除", notes = "压浆实时数据表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trYajiangSService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "压浆实时数据表-通过id查询")
    @ApiOperation(value = "压浆实时数据表-通过id查询", notes = "压浆实时数据表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrYajiangS trYajiangS = trYajiangSService.getById(id);
        if (trYajiangS == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trYajiangS);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param trYajiangS
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrYajiangS trYajiangS) {
        return super.exportXls(request, trYajiangS, TrYajiangS.class, "压浆实时数据表");
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
        return super.importExcel(request, response, TrYajiangS.class);
    }

}
