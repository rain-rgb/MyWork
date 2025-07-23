package com.trtm.iot.yclsl.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wbshebeidetail.entity.WbshebeiDetail;
import com.trtm.iot.wbshebeidetail.service.IWbshebeiDetailService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgbStatistics.entity.WzgbStatistics;
import com.trtm.iot.wzgbStatistics.service.IWzgbStatisticsService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.JypWztz;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.trtm.iot.wzycljinchanggbman.service.IWzycljinchanggbManService;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import com.trtm.iot.yclcl.service.IWzyclchuchanggbService;
import com.trtm.iot.yclsl.vo.WzycljinchanggbPush;
import com.trtm.iot.yclsl.vo.wzycljinchanggbPage;
import com.trtm.iot.yclsl.vo.wzycljinchanggbPages;
import com.xkcoding.http.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.config.sign.util.HttpUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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
 * @Description: 原材料收料主表
 * @Author: jeecg-boot
 * @Date: 2021-04-21
 * @Version: V1.0
 */
@Api(tags = "原材料收料主表")
@RestController
@RequestMapping("/yclsl/wzycljinchanggb")
@Slf4j
public class WzycljinchanggbController extends JeecgController<Wzycljinchanggb, IWzycljinchanggbService> {
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IWbshebeiDetailService wbshebeiDetailService;//发车单
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWzyclchuchanggbService wzyclchuchanggbService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IWzycljinchanggbManService wzycljinchanggbManService;


    /**
     * 分页列表查询
     *
     * @param wzycljinchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    /**
     * 分页列表查询
     *
     * @param wzycljinchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料收料主表-分页列表查询")
    @ApiOperation(value = "原材料收料主表-分页列表查询", notes = "原材料收料主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Wzycljinchanggb wzycljinchanggb,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }

        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        Page<Wzycljinchanggb> page = new Page<Wzycljinchanggb>(pageNo, pageSize);
        IPage<Wzycljinchanggb> pageList = wzycljinchanggbService.page(page, queryWrapper);
        List<Wzycljinchanggb> records = pageList.getRecords();
        for (Wzycljinchanggb wzycljinchanggb1 : records) {
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getCailiaono())) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzycljinchanggb1.setRengon(wzycljinchanggb1.getBeizhu());
                    wzycljinchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                }
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 中铁一局大屏
     *
     * @param wzycljinchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料收料主表-分页列表查询")
    @ApiOperation(value = "原材料收料主表-分页列表查询", notes = "原材料收料主表-分页列表查询")
    @GetMapping(value = "/listztdp")
    public Result<?> queryPageListztdp(Wzycljinchanggb wzycljinchanggb,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }

        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        Page<Wzycljinchanggb> page = new Page<Wzycljinchanggb>(pageNo, pageSize);
        IPage<Wzycljinchanggb> pageList = wzycljinchanggbService.page(page, queryWrapper);
        List<Wzycljinchanggb> records = pageList.getRecords();
        for (Wzycljinchanggb wzycljinchanggb1 : records) {
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getCailiaono())) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzycljinchanggb1.setCailiaono(wzcailiaonamedict.getCailiaoname());
                    wzycljinchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                }
            }
        }
        return Result.OK(pageList);
    }
    /**
     * 分页列表查询
     *
     * @param wzycljinchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料收料主表-分页列表查询")
    @ApiOperation(value = "原材料收料主表-分页列表查询", notes = "原材料收料主表-分页列表查询")
    @GetMapping(value = "/list47")
    public Result<?> queryPageList47(Wzycljinchanggb wzycljinchanggb,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }

        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        Page<Wzycljinchanggb> page = new Page<>(pageNo, pageSize);
        IPage<Wzycljinchanggb> pageList = wzycljinchanggbService.page(page, queryWrapper);

        WzycljinchanggbMan wzycljinchanggbMan = new WzycljinchanggbMan();
        BeanUtils.copyProperties(wzycljinchanggb, wzycljinchanggbMan);
        Page<WzycljinchanggbMan> page1 = new Page<>(pageNo, pageSize);
        QueryWrapper<WzycljinchanggbMan> queryWrapper1 = QueryGenerator.initQueryWrapper(wzycljinchanggbMan, req.getParameterMap());
        Page<WzycljinchanggbMan> manPage = wzycljinchanggbManService.page(page1, queryWrapper1);

        List<Wzycljinchanggb> gbList = new ArrayList<>();
        List<WzycljinchanggbMan> manList = manPage.getRecords();
        List<Wzycljinchanggb> records = pageList.getRecords();
        gbList.addAll(records);
        /*for (Wzycljinchanggb record : records) {
            gbList.add(record);
        }*/
        for (WzycljinchanggbMan man : manList) {
            man.setJingzhongt(new DecimalFormat("0.#####").format(Double.valueOf(man.getJingzhong())) + man.getGuobangleibie());
            Wzycljinchanggb jinchanggb = new Wzycljinchanggb();
            BeanUtils.copyProperties(man, jinchanggb);
            gbList.add(jinchanggb);
        }
        pageList.setRecords(gbList);
        pageList.setTotal(gbList.size());
        for (Wzycljinchanggb wzycljinchanggb1 : gbList) {
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getCailiaono())) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzycljinchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                }
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param wzycljinchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料收料主表-分页列表查询")
    @ApiOperation(value = "原材料收料主表-分页列表查询", notes = "原材料收料主表-分页列表查询")
    @GetMapping(value = "/listHb")
    public Result<?> queryPageListHb(Wzycljinchanggb wzycljinchanggb, Wzyclchuchanggb wzyclchuchanggb,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }

        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        Page<Wzycljinchanggb> page = new Page<Wzycljinchanggb>(pageNo, pageSize);
        IPage<Wzycljinchanggb> pageList = wzycljinchanggbService.page(page, queryWrapper);
        List<Wzycljinchanggb> records = pageList.getRecords();
        for (Wzycljinchanggb wzycljinchanggb1 : records) {
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getCailiaono())) {//获取材料名称和规格类型
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzycljinchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                    wzycljinchanggb1.setCailiaono(wzcailiaonamedict.getCailiaoname());
                }
            }
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getShebeibianhao())) {//获取设备名称
                wzycljinchanggb1.setShebeibianhao(shebeiInfoService.selectSbnameBySbno(wzycljinchanggb1.getShebeibianhao()));
            }
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getGongyingshangdanweibianma())) {//获取供应商名称
                Wzgongyingshang gongyongshang = wzgongyingshangService.selectnameone(wzycljinchanggb1.getGongyingshangdanweibianma());
                wzycljinchanggb1.setGongyingshangdanweibianma(gongyongshang.getGongyingshangname());
            }
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getLcbianhao())) {//获取料仓名称
                Wzliaocang wzliaocang = wzliaocangService.queryselectone(wzycljinchanggb1.getLcbianhao());
                wzycljinchanggb1.setLcbianhao(wzliaocang.getName());
            }
            if (wzycljinchanggb1.getCcbfpic() == null) {
                wzycljinchanggb1.setCcbfpic("");
            }
            if (wzycljinchanggb1.getCccppic() == null) {
                wzycljinchanggb1.setCccppic("");
            }
            if (wzycljinchanggb1.getCcgkpic() == null) {
                wzycljinchanggb1.setCcgkpic("");
            }
            if (wzycljinchanggb1.getCchcppic() == null) {
                wzycljinchanggb1.setCchcppic("");
            }
            if (wzycljinchanggb1.getJcbfpic() == null) {
                wzycljinchanggb1.setJcbfpic("");
            }
            if (wzycljinchanggb1.getJcgkpic() == null) {
                wzycljinchanggb1.setJcgkpic("");
            }
            if (wzycljinchanggb1.getJccppic() == null) {
                wzycljinchanggb1.setJccppic("");
            }
            if (wzycljinchanggb1.getJchcppic() == null) {
                wzycljinchanggb1.setJchcppic("");
            }

        }

        if (wzyclchuchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzyclchuchanggb.setShebeibianhao(shebei);
            } else {
                wzyclchuchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzyclchuchanggb> queryWrapper1 = QueryGenerator.initQueryWrapper(wzyclchuchanggb, req.getParameterMap());
        Page<Wzyclchuchanggb> page1 = new Page<Wzyclchuchanggb>(pageNo, pageSize);
        IPage<Wzyclchuchanggb> pageList1 = wzyclchuchanggbService.page(page1, queryWrapper1);
        List<Wzyclchuchanggb> records1 = pageList1.getRecords();
        for (Wzyclchuchanggb wzyclchuchanggb1 : records1) {
            if (StrUtil.isNotEmpty(wzyclchuchanggb1.getCailiaono())) {//获取材料名称和规格类型
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzyclchuchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzyclchuchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                    wzyclchuchanggb1.setCailiaono(wzcailiaonamedict.getCailiaoname());
                }
            }
            if (StrUtil.isNotEmpty(wzyclchuchanggb1.getShebeibianhao())) {//获取设备名称
                wzyclchuchanggb1.setShebeibianhao(shebeiInfoService.selectSbnameBySbno(wzyclchuchanggb1.getShebeibianhao()));
            }
            if (StrUtil.isNotEmpty(wzyclchuchanggb1.getLiaocangid())) {//获取收货单位名称
                wzyclchuchanggb1.setGongyingshangdanweibianma(wzyclchuchanggb1.getLiaocangid());
            }
            if (StrUtil.isNotEmpty(wzyclchuchanggb1.getLcbianhao())) {//获取料仓名称
                Wzliaocang wzliaocang = wzliaocangService.queryselectone(wzyclchuchanggb1.getLcbianhao());
                wzyclchuchanggb1.setLcbianhao(wzliaocang.getName());
            }
            if (wzyclchuchanggb1.getCcbfpic() == null) {
                wzyclchuchanggb1.setCcbfpic("");
            }
            if (wzyclchuchanggb1.getCccppic() == null) {
                wzyclchuchanggb1.setCccppic("");
            }
            if (wzyclchuchanggb1.getCcgkpic() == null) {
                wzyclchuchanggb1.setCcgkpic("");
            }
            if (wzyclchuchanggb1.getCchcppic() == null) {
                wzyclchuchanggb1.setCchcppic("");
            }
            if (wzyclchuchanggb1.getJcbfpic() == null) {
                wzyclchuchanggb1.setJcbfpic("");
            }
            if (wzyclchuchanggb1.getJcgkpic() == null) {
                wzyclchuchanggb1.setJcgkpic("");
            }
            if (wzyclchuchanggb1.getJccppic() == null) {
                wzyclchuchanggb1.setJccppic("");
            }
            if (wzyclchuchanggb1.getJchcppic() == null) {
                wzyclchuchanggb1.setJchcppic("");
            }
        }
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("jc", pageList);
        objectObjectHashMap.put("cc", pageList1);
        return Result.OK(objectObjectHashMap);
    }

    @AutoLog(value = "原材料收料主表-分页列表查询")
    @ApiOperation(value = "原材料收料主表-分页列表查询", notes = "原材料收料主表-分页列表查询")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(Wzycljinchanggb wzycljinchanggb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
//        queryWrapper.ne("JCGKPic", "");
        Page<Wzycljinchanggb> page = new Page<Wzycljinchanggb>(pageNo, pageSize);
        IPage<Wzycljinchanggb> pageList = wzycljinchanggbService.page(page, queryWrapper);
        List<Wzycljinchanggb> records = pageList.getRecords();
        for (Wzycljinchanggb wzycljinchanggb1 : records) {
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getCailiaono())) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzycljinchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                }
            }
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "原材料收料主表-分页列表查询")
    @ApiOperation(value = "原材料收料主表-分页列表查询", notes = "原材料收料主表-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(Wzycljinchanggb wzycljinchanggb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        List<Wzycljinchanggb> pageList = wzycljinchanggbService.list(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "原材料收料主表-分页列表查询")
    @ApiOperation(value = "原材料收料主表-分页列表查询", notes = "原材料收料主表-分页列表查询")
    @GetMapping(value = "/cailiaolist")
    public Result<?> queryPagecailiaoList(Wzycljinchanggb wzycljinchanggb,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req, String sysOrgCode) {
        List<String> list = new ArrayList<>();
        List<ShebeiInfo> shebeilist = shebeiInfoService.arrayOneshebei(sysOrgCode);
        for (ShebeiInfo shebeiInfo : shebeilist) {
            list.add(shebeiInfo.getSbjno());
        }
        QueryWrapper<Wzycljinchanggb> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(jingzhongT) as jingzhongT");
        queryWrapper.in("shebeibianhao", list);
        Wzycljinchanggb pageList = wzycljinchanggbService.getOne(queryWrapper);
        int jingzhongt = 0;
        Map<String, Integer> map = new HashMap<>();
        if (pageList != null) {
            jingzhongt = Integer.parseInt(pageList.getJingzhongt());
        }
        map.put("jingzhongt", jingzhongt);
        return Result.OK(map);
    }

    /**
     * 材料统计
     *
     * @param wzycljinchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzycljinchanggb-材料统计")
    @ApiOperation(value = "wzycljinchanggb-材料统计", notes = "wzycljinchanggb-材料统计")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(Wzycljinchanggb wzycljinchanggb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        queryWrapper.select("sum(jingzhongT) as jingzhongT,cailiaoNo,shebeibianhao,beizhu");
        queryWrapper.groupBy("cailiaoNo");
        List<Wzycljinchanggb> pageList = wzycljinchanggbService.list(queryWrapper);
        List records1 = new ArrayList<>();
        for (Wzycljinchanggb wzycljinchanggb1 : pageList) {
            Wzycljinchanggb wzycljinchanggb2 = new Wzycljinchanggb();
            String cailiaono = wzycljinchanggb1.getCailiaono();
            Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(cailiaono);
            if (wzcailiaonamedict != null) {
                wzycljinchanggb2.setCailiaono(wzcailiaonamedict.getCailiaoname());
                wzycljinchanggb2.setBeizhu(wzcailiaonamedict.getGuigexinghao());
            }else {
                wzycljinchanggb2.setCailiaono(wzycljinchanggb1.getCailiaono());
                wzycljinchanggb2.setBeizhu(wzycljinchanggb1.getBeizhu());
            }
            Double jingzhong = Double.valueOf(wzycljinchanggb1.getJingzhongt());
            String jingzhongt = String.format("%.2f", jingzhong);
            wzycljinchanggb2.setJingzhongt(jingzhongt);
            records1.add(wzycljinchanggb2);
        }
        return Result.OK(records1);
    }

    /**
     * 本月物料统计
     *
     * @param wzycljinchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzycljinchanggb-本月物料统计")
    @ApiOperation(value = "wzycljinchanggb-本月物料统计", notes = "wzycljinchanggb-本月物料统计")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(Wzycljinchanggb wzycljinchanggb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
        String day1 = format.format(c.getTime());
        Date parse = null;//本月第一天
        Date parse1 = null;//本月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String day2 = format.format(ca.getTime());
        try {
            parse = format.parse(day1);
            parse1 = format.parse(day2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        queryWrapper.select("sum(jingzhongT) as jingzhongT");
        queryWrapper.ge("jinchangshijian", parse);
        queryWrapper.le("jinchangshijian", parse1);
        List<Wzycljinchanggb> pageList = wzycljinchanggbService.list(queryWrapper);
        Map map = new HashMap();
        String jingzhongt = "0";
        for (Wzycljinchanggb wzycljinchanggb1 : pageList) {
            if (wzycljinchanggb1 != null) {
                jingzhongt = wzycljinchanggb1.getJingzhongt();
            }
        }
        map.put("jingzhongt", jingzhongt);
        return Result.OK(map);
    }

    /**
     * 分页列表查询
     *
     * @param wzycljinchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料收料主表-分页列表查询")
    @ApiOperation(value = "原材料收料主表-分页列表查询", notes = "原材料收料主表-分页列表查询")
    @GetMapping(value = "/liststa")
    public Result<?> queryPageListsta(Wzycljinchanggb wzycljinchanggb, Wzyclchuchanggb wzyclchuchanggb,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        queryWrapper.select("id,shebeibianhao,sum(jingzhongT) jingzhongT,sum(maozhongT) as maozhongT,sum(pizhongT) pizhongT,LiaoCangId,cailiaoNo,beizhu,count(1) istongji,gongyingshangdanweibianma,date_format(chuchangshijian, '%Y-%m-%d') chuchangshijian");
        queryWrapper.gt("jingzhongT", 0);
        queryWrapper.isNotNull("jingzhongT");
        queryWrapper.isNotNull("chuchangshijian");
        queryWrapper.groupBy("shebeibianhao", "gongyingshangdanweibianma", "cailiaoNo", "date_format(chuchangshijian, '%Y-%m-%d')");
        Page<Wzycljinchanggb> page = new Page<Wzycljinchanggb>(pageNo, pageSize);
        IPage<Wzycljinchanggb> pageList = wzycljinchanggbService.page(page, queryWrapper);
        List<Wzycljinchanggb> records = pageList.getRecords();
        if (wzyclchuchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzyclchuchanggb.setShebeibianhao(shebei);
            } else {
                wzyclchuchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzyclchuchanggb> queryWrapper1 = QueryGenerator.initQueryWrapper(wzyclchuchanggb, req.getParameterMap());
        queryWrapper1.select("id,shebeibianhao,sum(jingzhongT) jingzhongT,sum(maozhongT) as maozhongT,sum(pizhongT) pizhongT,LiaoCangId,cailiaoNo,beizhu,count(1) istongji,gongyingshangdanweibianma,date_format(chuchangshijian, '%Y-%m-%d') chuchangshijian");
        queryWrapper1.gt("jingzhongT", 0);
        queryWrapper1.isNotNull("jingzhongT");
        queryWrapper1.isNotNull("chuchangshijian");
        queryWrapper1.groupBy("shebeibianhao", "LiaoCangId", "cailiaoNo", "date_format(chuchangshijian, '%Y-%m-%d')");
        List<Wzyclchuchanggb> list = wzyclchuchanggbService.list(queryWrapper1);
        for (Wzyclchuchanggb wzyclchuchanggb1 : list) {
            Wzycljinchanggb wzycljinchanggb2 = new Wzycljinchanggb();
            BeanUtils.copyProperties(wzyclchuchanggb1, wzycljinchanggb2);
            records.add(wzycljinchanggb2);
        }
        for (Wzycljinchanggb wzycljinchanggb1 : records) {
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getCailiaono())) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzycljinchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                }
            }
            wzycljinchanggb1.setMaozhongt(String.format("%.2f", Double.parseDouble(wzycljinchanggb1.getMaozhongt())));
            wzycljinchanggb1.setPizhongt(String.format("%.2f", Double.parseDouble(wzycljinchanggb1.getPizhongt())));
            wzycljinchanggb1.setJingzhongt(String.format("%.2f", Double.parseDouble(wzycljinchanggb1.getJingzhongt())));

        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wzycljinchanggb
     * @return
     */
    @AutoLog(value = "原材料收料主表-添加")
    @ApiOperation(value = "原材料收料主表-添加", notes = "原材料收料主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Wzycljinchanggb wzycljinchanggb) {
        wzycljinchanggbService.save(wzycljinchanggb);
        return Result.OK("添加成功！");
    }

    /**
     * 原材料进场过磅对外开放添加数据接口
     *
     * @param wzycljinchanggb
     * @return
     */
    @AutoLog(value = "原材料收料主表-添加")
    @ApiOperation(value = "原材料收料主表-添加", notes = "原材料收料主表-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody Wzycljinchanggb wzycljinchanggb) {
        Wzycljinchanggb one = wzycljinchanggbService.selectByjinchuliaodanno(wzycljinchanggb.getJinchuliaodanno());
        if (one == null) {
            wzycljinchanggbService.save(wzycljinchanggb);
            return Result.OK("添加成功！");
        } else {
            return Result.error("添加失败！");
        }

    }

    /**
     * 原材料进场过磅对外开放添加数据接口(瑞仓内网)
     *
     * @param wzycljinchanggbPages
     * @return
     */
    @AutoLog(value = "原材料收料主表-添加")
    @ApiOperation(value = "原材料收料主表-添加", notes = "原材料收料主表-添加")
    @PostMapping(value = "/addOpens")
    public Result<?> addOpens(@RequestBody wzycljinchanggbPages wzycljinchanggbPages) {
        int code = wzycljinchanggbService.saveMain(wzycljinchanggbPages.getWzycljinchanggb(), wzycljinchanggbPages.getWzcailiaonamedict(), wzycljinchanggbPages.getWzgongyingshang(), wzycljinchanggbPages.getWzliaocang());
        if (code == 200) {
            return Result.OK("添加成功！");
        } else {
            return Result.error("添加失败！");
        }

    }

    /**
     * 编辑
     *
     * @param wzycljinchanggb
     * @return
     */
    @AutoLog(value = "原材料收料主表-编辑")
    @ApiOperation(value = "原材料收料主表-编辑", notes = "原材料收料主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Wzycljinchanggb wzycljinchanggb) {
        Wzycljinchanggb byId = wzycljinchanggbService.getById(wzycljinchanggb.getId());
        if (null != byId) {
            if (null != byId.getTaizhangid()) {
                Wztaizhang byId1 = wztaizhangService.getById(byId.getTaizhangid());
                if (null != byId1) {
                    if (StrUtil.isNotBlank(wzycljinchanggb.getJingzhongt())) {
                        double jinzhongt = Double.parseDouble(byId1.getJingzhongt()) - Double.parseDouble(byId.getJingzhongt()) + Double.parseDouble(wzycljinchanggb.getJingzhongt());
                        byId1.setJingzhongt(String.format("%.2f", jinzhongt));
                    }
                    if (StrUtil.isNotBlank(wzycljinchanggb.getPizhongt())) {
                        double pizhongt = Double.parseDouble(byId1.getPizhongt()) - Double.parseDouble(byId.getPizhongt()) + Double.parseDouble(wzycljinchanggb.getPizhongt());
                        byId1.setPizhongt(String.format("%.2f", pizhongt));
                    }
                    if (StrUtil.isNotBlank(wzycljinchanggb.getMaozhongt())) {
                        double maozhongt = Double.parseDouble(byId1.getMaozhongt()) - Double.parseDouble(byId.getMaozhongt()) + Double.parseDouble(wzycljinchanggb.getMaozhongt());
                        byId1.setMaozhongt(String.format("%.2f", maozhongt));
                    }
                    wztaizhangService.updateById(byId1);
                }
            }
            wzycljinchanggbService.updateById(wzycljinchanggb);
            return Result.OK("编辑成功!");
        }
        return Result.error("编辑失败!");
    }


    /**
     * 编辑
     *
     * @param wzycljinchanggb
     * @return
     */
    @AutoLog(value = "原材料收料主表-编辑")
    @ApiOperation(value = "原材料收料主表-编辑", notes = "原材料收料主表-编辑")
    @PutMapping(value = "/editts")
    public Result<?> editts(@RequestBody Wzycljinchanggb wzycljinchanggb) {
        wzycljinchanggb.setTaizhangtj(0);
        wzycljinchanggb.setBeizhu(null);
        wzycljinchanggbService.updateById(wzycljinchanggb);
        return Result.OK("编辑成功!");

    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "原材料收料主表-通过id删除")
    @ApiOperation(value = "原材料收料主表-通过id删除", notes = "原材料收料主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        Wzycljinchanggb byId = wzycljinchanggbService.getById(id);
        if (null != byId) {
            if (null != byId.getTaizhangid()) {
                Wztaizhang byId1 = wztaizhangService.getById(byId.getTaizhangid());
                if (null != byId1) {
                    double jinzhongt = Double.parseDouble(byId1.getJingzhongt()) - Double.parseDouble(byId.getJingzhongt());
                    double pizhongt = Double.parseDouble(byId1.getPizhongt()) - Double.parseDouble(byId.getPizhongt());
                    double maozhongt = Double.parseDouble(byId1.getMaozhongt()) - Double.parseDouble(byId.getMaozhongt());
                    byId1.setJingzhongt(String.format("%.2f", jinzhongt));
                    byId1.setPizhongt(String.format("%.2f", pizhongt));
                    byId1.setMaozhongt(String.format("%.2f", maozhongt));
                    wztaizhangService.updateById(byId1);
                }
            }
            wzycljinchanggbService.removeById(id);
            return Result.OK("删除成功!");
        }
        return Result.error("删除失败!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "原材料收料主表-批量删除")
    @ApiOperation(value = "原材料收料主表-批量删除", notes = "原材料收料主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzycljinchanggbService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "原材料收料主表-通过id查询")
    @ApiOperation(value = "原材料收料主表-通过id查询", notes = "原材料收料主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) Integer id) {
        Wzycljinchanggb wzycljinchanggb = wzycljinchanggbService.getById(id);
        if (wzycljinchanggb == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzycljinchanggb);
    }

    /**
     * 分页列表查询
     *
     * @param wzycljinchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料收料主表-查询明细")
    @ApiOperation(value = "原材料收料主表-分页列表查询", notes = "原材料收料主表-分页列表查询")
    @GetMapping(value = "/listInfo")
    public Result<?> queryMingxById(Wzycljinchanggb wzycljinchanggb,
                                    Integer id,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }

        QueryWrapper<Wzycljinchanggb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("taizhangid", id);
        List<Wzycljinchanggb> list = wzycljinchanggbService.list(queryWrapper);
        for (Wzycljinchanggb wzycljinchanggb1 : list) {
            if (StrUtil.isNotEmpty(wzycljinchanggb1.getCailiaono())) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzycljinchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                }
            }
        }
        return Result.OK(list);
    }

    @AutoLog(value = "原材料收料主表-根据id查询数据并且打印")
    @ApiOperation(value = "原材料收料主表-根据id查询数据并且打印", notes = "原材料收料主表-根据id查询数据并且打印")
    @GetMapping(value = "/lists")
    public Result<?> queryPageLists(Wzycljinchanggb wzycljinchanggb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer id) {
        if (id != null) {
            wzycljinchanggb.setId(id);
        }
        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, req.getParameterMap());
        Page<Wzycljinchanggb> page = new Page<Wzycljinchanggb>(pageNo, pageSize);
        IPage<Wzycljinchanggb> pageList = wzycljinchanggbService.page(page, queryWrapper);
        List<Wzycljinchanggb> records = pageList.getRecords();
        List data = new ArrayList<>();
        for (Wzycljinchanggb record : records) {//因打印格式需要的不同所以在此处理数据格式
            wzycljinchanggbPage wzycljinchanggbPage = new wzycljinchanggbPage();
            Integer id1 = record.getId();
            String cailiaono = record.getCailiaono();
            String jinchuliaodanno = record.getJinchuliaodanno();
            String kouzhong = record.getKouzhong();
            String sibangyuan = record.getSibangyuan();
            Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(cailiaono);
            if (wzcailiaonamedict != null) {
                String cailiaoname = wzcailiaonamedict.getCailiaoname();
                String guigexinghao = wzcailiaonamedict.getGuigexinghao();
                wzycljinchanggbPage.setCailiaoName(cailiaoname);
                wzycljinchanggbPage.setCailiaoguige(guigexinghao);

            }

            String beizhu = record.getBeizhu();
            String qianchepai = record.getQianchepai();
            String pici = record.getPici();
            String jingzhong = record.getJingzhong();
            String jinchangshijian = record.getJinchangshijian();
            String jingzhongt = record.getJingzhongt();
            String maozhong = record.getMaozhong();
            String maozhongt = record.getMaozhongt();
            String pizhong = record.getPizhong();
            String pizhongt = record.getPizhongt();
            String shebeibianhao = record.getShebeibianhao();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
            if (selectshebeione != null) {
                String sbname = selectshebeione.getSbname();
                wzycljinchanggbPage.setSbname(sbname);
            }

            String lcbianhao = record.getLcbianhao();
            Wzliaocang queryselectone = wzliaocangService.queryselectone(lcbianhao);
            if (queryselectone != null) {
                String Name = queryselectone.getName();
                wzycljinchanggbPage.setLcname(Name);

            }

            String chuchangshijian = record.getChuchangshijian();
            String gongyingshangdanweibianma = record.getGongyingshangdanweibianma();
            Wzgongyingshang querywzone = wzgongyingshangService.selectnameone(gongyingshangdanweibianma);
            if (querywzone != null) {
                String gongyingshangname = querywzone.getGongyingshangname();
                wzycljinchanggbPage.setGongyingshangname(gongyingshangname);
            }

            wzycljinchanggbPage.setId(id1);
            wzycljinchanggbPage.setJinchuliaodanno(jinchuliaodanno);
            wzycljinchanggbPage.setKouzhong(kouzhong);
            wzycljinchanggbPage.setSibangyuan(sibangyuan);
            wzycljinchanggbPage.setCailiaono(cailiaono);
            wzycljinchanggbPage.setBeizhu(beizhu);
            wzycljinchanggbPage.setShebeibianhao(shebeibianhao);
            wzycljinchanggbPage.setLcbianhao(lcbianhao);
            wzycljinchanggbPage.setChuchangshijian(chuchangshijian);
            wzycljinchanggbPage.setGongyingshangdanweibianma(gongyingshangdanweibianma);

            wzycljinchanggbPage.setQianchepai(qianchepai);
            wzycljinchanggbPage.setPici(pici);
            wzycljinchanggbPage.setJingzhong(jingzhong);
            wzycljinchanggbPage.setJingzhongt(jingzhongt);
            wzycljinchanggbPage.setMaozhong(maozhong);
            wzycljinchanggbPage.setMaozhongt(maozhongt);
            wzycljinchanggbPage.setPizhong(pizhong);
            wzycljinchanggbPage.setPizhongt(pizhongt);


            wzycljinchanggbPage.setJinchangshijian(jinchangshijian);
            data.add(wzycljinchanggbPage);
        }
        return Result.OKs(data);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzycljinchanggb
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzycljinchanggb wzycljinchanggb) {

        // Step.1 组装查询条件
        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<Wzycljinchanggb> pageList = wzycljinchanggbService.list(queryWrapper);
        List<Wzycljinchanggb> exportList = new ArrayList<>();

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId().toString())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }
        for (Wzycljinchanggb wzycljinchanggb1 : exportList) {
            String cailiaono = wzycljinchanggb1.getCailiaono();
            if (cailiaono != null) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(cailiaono);
                if (wzcailiaonamedict != null) {
                    String cailiaoname = wzcailiaonamedict.getCailiaoname();
                    if (cailiaoname != null) {
                        wzycljinchanggb1.setCailiaono(cailiaoname);
                        wzycljinchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                    }
                }
            }
            String lcbianhao = wzycljinchanggb1.getLcbianhao();
            if (lcbianhao != null) {
                Wzliaocang queryselectone = wzliaocangService.queryselectone(lcbianhao);
                if (queryselectone != null) {
                    String Name = queryselectone.getName();
                    if (Name != null) {
                        wzycljinchanggb1.setLcbianhao(Name);
                    }
                }
            }
            String gongyingshangdanweibianma = wzycljinchanggb1.getGongyingshangdanweibianma();
            if (gongyingshangdanweibianma != null) {
                Wzgongyingshang querywzone = wzgongyingshangService.selectnameone(gongyingshangdanweibianma);
                if (querywzone != null) {
                    String gongyingshangname = querywzone.getGongyingshangname();
                    if (gongyingshangname != null) {
                        wzycljinchanggb1.setGongyingshangdanweibianma(gongyingshangname);
                    }
                }
            }
        }
        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "原材料收料数据列表"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, Wzycljinchanggb.class);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams = new ExportParams("原材料收料数据列表" + "报表", "导出人:" + sysUser.getRealname(), "原材料收料数据列表");
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导出excel(原材料进出场数据统计-每天)
     *
     * @param request
     * @param wzycljinchanggb
     */
    @RequestMapping(value = "/exportXlsta")
    public ModelAndView exportXlsta(HttpServletRequest request, Wzycljinchanggb wzycljinchanggb, String chuchangshijian_begin, String chuchangshijian_end, Wzyclchuchanggb wzyclchuchanggb) throws ParseException {

        // Step.1 组装查询条件
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(sysUser.getId() + "change"));
        if (wzycljinchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggb.setShebeibianhao(shebei);
            } else {
                wzycljinchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzycljinchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggb, request.getParameterMap());
        queryWrapper.select("id,shebeibianhao,sum(jingzhongT) jingzhongT,sum(maozhongT) as maozhongT,sum(pizhongT) pizhongT,LiaoCangId,cailiaoNo,beizhu,count(1) istongji,gongyingshangdanweibianma,date_format(chuchangshijian, '%Y-%m-%d') chuchangshijian");
        queryWrapper.gt("jingzhongT", 0);
        queryWrapper.isNotNull("jingzhongT");
        queryWrapper.isNotNull("chuchangshijian");
        if (chuchangshijian_begin == null && null == chuchangshijian_end) {
            queryWrapper.ge("chuchangshijian", format1.format(new Date()) + " 00:00:00");
            queryWrapper.le("chuchangshijian", format1.format(new Date()) + " 23:59:59");
        }
        queryWrapper.groupBy("shebeibianhao", "gongyingshangdanweibianma", "cailiaoNo", "date_format(chuchangshijian, '%Y-%m-%d')");
        List<Wzycljinchanggb> pageList = wzycljinchanggbService.list(queryWrapper);
        if (wzyclchuchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzyclchuchanggb.setShebeibianhao(shebei);
            } else {
                wzyclchuchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzyclchuchanggb> queryWrapper1 = QueryGenerator.initQueryWrapper(wzyclchuchanggb, request.getParameterMap());
        queryWrapper1.select("id,shebeibianhao,sum(jingzhongT) jingzhongT,sum(maozhongT) as maozhongT,sum(pizhongT) pizhongT,LiaoCangId,cailiaoNo,beizhu,count(1) istongji,gongyingshangdanweibianma,date_format(chuchangshijian, '%Y-%m-%d') chuchangshijian");
        queryWrapper1.gt("jingzhongT", 0);
        queryWrapper1.isNotNull("jingzhongT");
        queryWrapper1.isNotNull("chuchangshijian");
        if (chuchangshijian_begin == null && null == chuchangshijian_end) {
            queryWrapper1.ge("chuchangshijian", format1.format(new Date()) + " 00:00:00");
            queryWrapper1.le("chuchangshijian", format1.format(new Date()) + " 23:59:59");
        }
        queryWrapper1.groupBy("shebeibianhao", "LiaoCangId", "cailiaoNo", "date_format(chuchangshijian, '%Y-%m-%d')");
        List<Wzyclchuchanggb> list = wzyclchuchanggbService.list(queryWrapper1);
        // Step.2 获取导出数据
        List<WzgbStatistics> exportList = new ArrayList<>();
        for (Wzyclchuchanggb wzyclchuchanggb1 : list) {
            Wzycljinchanggb wzycljinchanggb2 = new Wzycljinchanggb();
            BeanUtils.copyProperties(wzyclchuchanggb1, wzycljinchanggb2);
            pageList.add(wzycljinchanggb2);
        }
        // 过滤选中数据
        String selections = request.getParameter("selections");
//		 if (oConvertUtils.isNotEmpty(selections)) {
//			 List<String> selectionList = Arrays.asList(selections.split(","));
//			 exportList =pageList.stream().filter(item -> selectionList.contains(item.getId().toString())).collect(Collectors.toList());
//		 } else {
//			 exportList = pageList;
//		 }
        int i = 1;
        for (Wzycljinchanggb wzycljinchanggb1 : pageList) {
            WzgbStatistics wzgbStatistics = new WzgbStatistics();
            String cailiaono = wzycljinchanggb1.getCailiaono();
            if (cailiaono != null) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(cailiaono);
                if (wzcailiaonamedict != null) {
                    String cailiaoname = wzcailiaonamedict.getCailiaoname();
                    if (cailiaoname != null) {
                        wzgbStatistics.setCailiaono(cailiaoname);
                    }
                    wzgbStatistics.setGuigexinghao(wzcailiaonamedict.getGuigexinghao());
                }
            }
            String gongyingshangdanweibianma = wzycljinchanggb1.getGongyingshangdanweibianma();
            if (gongyingshangdanweibianma != null) {
                Wzgongyingshang querywzone = wzgongyingshangService.selectnameone(gongyingshangdanweibianma);
                if (querywzone != null) {
                    String gongyingshangname = querywzone.getGongyingshangname();
                    if (gongyingshangname != null) {
                        wzgbStatistics.setGongyingshangdanweibianma(gongyingshangname);
                    }
                }
            }
            wzgbStatistics.setCarsNumber(wzycljinchanggb1.getIstongji());
            wzgbStatistics.setJingzhongt(String.format("%.2f", Double.parseDouble(wzycljinchanggb1.getJingzhongt())));
            wzgbStatistics.setId(i++);
            wzgbStatistics.setMaozhongt(String.format("%.2f", Double.parseDouble(wzycljinchanggb1.getMaozhongt())));
            wzgbStatistics.setPizhongt(String.format("%.2f", Double.parseDouble(wzycljinchanggb1.getPizhongt())));
            wzgbStatistics.setLiaocangid(wzycljinchanggb1.getLiaocangid());
            wzgbStatistics.setStatisticsTime(wzycljinchanggb1.getChuchangshijian());
            exportList.add(wzgbStatistics);
        }
        if (chuchangshijian_begin == null && null == chuchangshijian_end) {
            chuchangshijian_begin = format1.format(new Date()) + " 00:00:00";
            chuchangshijian_end = format1.format(new Date()) + " 23:59:59";
        }
        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "称重记录统计表"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, WzgbStatistics.class);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams = new ExportParams("称重记录统计表", chuchangshijian_begin + "——" + chuchangshijian_end, "原材料进出场统计报表");
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
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
        return super.importExcel(request, response, Wzycljinchanggb.class);
    }

}
