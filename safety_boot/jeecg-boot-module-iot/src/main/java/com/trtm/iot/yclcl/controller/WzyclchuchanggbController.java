package com.trtm.iot.yclcl.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.trtm.iot.yclcl.vo.wzyclchuchanggbPage;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.vo.wzycljinchanggbPage;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import com.trtm.iot.yclcl.service.IWzyclchuchanggbService;

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
 * @Description: 原材料出料主表
 * @Author: jeecg-boot
 * @Date: 2021-05-26
 * @Version: V1.0
 */
@Api(tags = "原材料出料主表")
@RestController
@RequestMapping("/yclcl/wzyclchuchanggb")
@Slf4j
public class WzyclchuchanggbController extends JeecgController<Wzyclchuchanggb, IWzyclchuchanggbService> {
    @Autowired
    private IWzyclchuchanggbService wzyclchuchanggbService;
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

    /**
     * 分页列表查询
     *
     * @param wzyclchuchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzyclchuchanggb-分页列表查询")
    @ApiOperation(value = "wzyclchuchanggb-分页列表查询", notes = "wzyclchuchanggb-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Wzyclchuchanggb wzyclchuchanggb,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzyclchuchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzyclchuchanggb.setShebeibianhao(shebei);
            } else {
                wzyclchuchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzyclchuchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzyclchuchanggb, req.getParameterMap());
        Page<Wzyclchuchanggb> page = new Page<Wzyclchuchanggb>(pageNo, pageSize);
        IPage<Wzyclchuchanggb> pageList = wzyclchuchanggbService.page(page, queryWrapper);
        List<Wzyclchuchanggb> records = pageList.getRecords();
        for (Wzyclchuchanggb wzyclchuchanggb1 : records) {
            if (StrUtil.isNotEmpty(wzyclchuchanggb1.getCailiaono())) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzyclchuchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzyclchuchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                }
            }
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "wzyclchuchanggb-分页列表查询")
    @ApiOperation(value = "wzyclchuchanggb-分页列表查询", notes = "wzyclchuchanggb-分页列表查询")
    @GetMapping(value = "/list4")
    public Result<?> list4(Wzyclchuchanggb wzyclchuchanggb,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                           HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzyclchuchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzyclchuchanggb.setShebeibianhao(shebei);
            } else {
                wzyclchuchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzyclchuchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzyclchuchanggb, req.getParameterMap());
//        queryWrapper.ne("JCGKPic", "");
        Page<Wzyclchuchanggb> page = new Page<Wzyclchuchanggb>(pageNo, pageSize);
        IPage<Wzyclchuchanggb> pageList = wzyclchuchanggbService.page(page, queryWrapper);
        List<Wzyclchuchanggb> records = pageList.getRecords();
        for (Wzyclchuchanggb wzyclchuchanggb1 : records) {
            if (StrUtil.isNotEmpty(wzyclchuchanggb1.getCailiaono())) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzyclchuchanggb1.getCailiaono());
                if (wzcailiaonamedict != null) {
                    wzyclchuchanggb1.setBeizhu(wzcailiaonamedict.getGuigexinghao());
                }
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 列表查询
     *
     * @param wzyclchuchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzyclchuchanggb-列表查询")
    @ApiOperation(value = "wzyclchuchanggb-列表查询", notes = "wzyclchuchanggb-列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(Wzyclchuchanggb wzyclchuchanggb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzyclchuchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzyclchuchanggb.setShebeibianhao(shebei);
            } else {
                wzyclchuchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzyclchuchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzyclchuchanggb, req.getParameterMap());
        List<Wzyclchuchanggb> list = wzyclchuchanggbService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "原材料出料主表-根据id查询数据并且打印")
    @ApiOperation(value = "原材料出料主表-根据id查询数据并且打印", notes = "原材料出料主表-根据id查询数据并且打印")
    @GetMapping(value = "/lists")
    public Result<?> queryPageLists(Wzyclchuchanggb wzyclchuchanggb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer id) {
        if (id != null) {
            wzyclchuchanggb.setId(id);
        }
        QueryWrapper<Wzyclchuchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzyclchuchanggb, req.getParameterMap());
        Page<Wzyclchuchanggb> page = new Page<Wzyclchuchanggb>(pageNo, pageSize);
        IPage<Wzyclchuchanggb> pageList = wzyclchuchanggbService.page(page, queryWrapper);
        List<Wzyclchuchanggb> records = pageList.getRecords();
        List data = new ArrayList<>();
        for (Wzyclchuchanggb record : records) {//因打印格式需要的不同所以在此处理数据格式
            wzyclchuchanggbPage wzyclchuchanggbPage = new wzyclchuchanggbPage();
            Integer id1 = record.getId();
            String cailiaono = record.getCailiaono();
            String jinchuliaodanno = record.getJinchuliaodanno();
            String kouzhong = record.getKouzhong();
            String sibangyuan = record.getSibangyuan();
            Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(cailiaono);
            if (wzcailiaonamedict != null) {
                String cailiaoname = wzcailiaonamedict.getCailiaoname();
                String guigexinghao = wzcailiaonamedict.getGuigexinghao();
                wzyclchuchanggbPage.setCailiaoName(cailiaoname);
                wzyclchuchanggbPage.setCailiaoguige(guigexinghao);
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
            String sbname = selectshebeione.getSbname();
            String lcbianhao = record.getLcbianhao();
            Wzliaocang queryselectone = wzliaocangService.queryselectone(lcbianhao);
            String Name = null;
            if (queryselectone != null) {
                Name = queryselectone.getName();
            } else {
                Name = "无";
            }
            String chuchangshijian = record.getChuchangshijian();
            String gongyingshangdanweibianma = record.getGongyingshangdanweibianma();
            Wzgongyingshang querywzone = wzgongyingshangService.selectnameone(gongyingshangdanweibianma);
            String gongyingshangname = null;
            if (querywzone != null) {
                gongyingshangname = querywzone.getGongyingshangname();
            } else {
                gongyingshangname = "无";
            }
            wzyclchuchanggbPage.setId(id1);
            wzyclchuchanggbPage.setJinchuliaodanno(jinchuliaodanno);
            wzyclchuchanggbPage.setKouzhong(kouzhong);
            wzyclchuchanggbPage.setSibangyuan(sibangyuan);
            wzyclchuchanggbPage.setCailiaono(cailiaono);
            wzyclchuchanggbPage.setBeizhu(beizhu);
            wzyclchuchanggbPage.setShebeibianhao(shebeibianhao);
            wzyclchuchanggbPage.setSbname(sbname);
            wzyclchuchanggbPage.setLcbianhao(lcbianhao);
            wzyclchuchanggbPage.setChuchangshijian(chuchangshijian);
            wzyclchuchanggbPage.setGongyingshangdanweibianma(gongyingshangdanweibianma);
            wzyclchuchanggbPage.setGongyingshangname(gongyingshangname);
            wzyclchuchanggbPage.setQianchepai(qianchepai);
            wzyclchuchanggbPage.setPici(pici);
            wzyclchuchanggbPage.setJingzhong(jingzhong);
            wzyclchuchanggbPage.setJingzhongt(jingzhongt);
            wzyclchuchanggbPage.setMaozhong(maozhong);
            wzyclchuchanggbPage.setMaozhongt(maozhongt);
            wzyclchuchanggbPage.setPizhong(pizhong);
            wzyclchuchanggbPage.setPizhongt(pizhongt);
            wzyclchuchanggbPage.setLcname(Name);
            wzyclchuchanggbPage.setJinchangshijian(jinchangshijian);
            data.add(wzyclchuchanggbPage);
        }
        return Result.OKs(data);
    }

    /**
     * 原材料出料数据统计
     *
     * @param wzyclchuchanggb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzyclchuchanggb-原材料出料数据统计")
    @ApiOperation(value = "wzyclchuchanggb-原材料出料数据统计", notes = "wzyclchuchanggb-原材料出料数据统计")
    @GetMapping(value = "list2")
    public Result<?> queryPageList2(Wzyclchuchanggb wzyclchuchanggb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (wzyclchuchanggb.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzyclchuchanggb.setShebeibianhao(shebei);
            } else {
                wzyclchuchanggb.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wzyclchuchanggb> queryWrapper = QueryGenerator.initQueryWrapper(wzyclchuchanggb, req.getParameterMap());
        queryWrapper.select("sum(jingzhongt) as jingzhongT,cailiaono,shebeibianhao");
        queryWrapper.groupBy("cailiaono");
        List<Wzyclchuchanggb> pageList = wzyclchuchanggbService.list(queryWrapper);
        List records1 = new ArrayList<>();
        for (Wzyclchuchanggb wzyclchuchanggb1 : pageList) {
            Wzyclchuchanggb wzyclchuchanggb2 = new Wzyclchuchanggb();
            String cailiaono = wzyclchuchanggb1.getCailiaono();
            Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(cailiaono);
            if (wzcailiaonamedict != null) {
                wzyclchuchanggb2.setCailiaono(wzcailiaonamedict.getCailiaoname());
                wzyclchuchanggb2.setBeizhu(wzcailiaonamedict.getGuigexinghao());
            }
            Double jingzhong = Double.valueOf(wzyclchuchanggb1.getJingzhongt());
            String jingzhongt = String.format("%.2f", jingzhong);
            wzyclchuchanggb2.setJingzhongt(jingzhongt);
            records1.add(wzyclchuchanggb2);
        }
        return Result.OK(records1);
    }

    /**
     * 添加
     *
     * @param wzyclchuchanggb
     * @return
     */
    @AutoLog(value = "wzyclchuchanggb-添加")
    @ApiOperation(value = "wzyclchuchanggb-添加", notes = "wzyclchuchanggb-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Wzyclchuchanggb wzyclchuchanggb) {
        wzyclchuchanggbService.save(wzyclchuchanggb);
        return Result.OK("添加成功！");
    }

    /**
     * 原材料出场过磅对外开放添加数据接口
     *
     * @param wzyclchuchanggb
     * @return
     */
    @AutoLog(value = "wzyclchuchanggb-添加")
    @ApiOperation(value = "wzyclchuchanggb-添加", notes = "wzyclchuchanggb-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody Wzyclchuchanggb wzyclchuchanggb) {
        QueryWrapper<Wzyclchuchanggb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("jinchuliaodanNo", wzyclchuchanggb.getJinchuliaodanno());
        Wzyclchuchanggb one = wzyclchuchanggbService.getOne(queryWrapper);
        if (one == null) {
            wzyclchuchanggbService.save(wzyclchuchanggb);
            return Result.OK("添加成功！");
        } else {
            wzyclchuchanggb.setId(one.getId());
            wzyclchuchanggbService.updateById(wzyclchuchanggb);
            return Result.OK("添加成功！");
        }
    }

    /**
     * 编辑
     *
     * @param wzyclchuchanggb
     * @return
     */
    @AutoLog(value = "wzyclchuchanggb-编辑")
    @ApiOperation(value = "wzyclchuchanggb-编辑", notes = "wzyclchuchanggb-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Wzyclchuchanggb wzyclchuchanggb) {
        wzyclchuchanggbService.updateById(wzyclchuchanggb);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzyclchuchanggb-通过id删除")
    @ApiOperation(value = "wzyclchuchanggb-通过id删除", notes = "wzyclchuchanggb-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzyclchuchanggbService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wzyclchuchanggb-批量删除")
    @ApiOperation(value = "wzyclchuchanggb-批量删除", notes = "wzyclchuchanggb-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzyclchuchanggbService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzyclchuchanggb-通过id查询")
    @ApiOperation(value = "wzyclchuchanggb-通过id查询", notes = "wzyclchuchanggb-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Wzyclchuchanggb wzyclchuchanggb = wzyclchuchanggbService.getById(id);
        if (wzyclchuchanggb == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzyclchuchanggb);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzyclchuchanggb
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzyclchuchanggb wzyclchuchanggb) {
        return super.exportXls(request, wzyclchuchanggb, Wzyclchuchanggb.class, "wzyclchuchanggb");
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
        return super.importExcel(request, response, Wzyclchuchanggb.class);
    }
    /**
     * 出场批量添加
     *
     * @return
     */
    @AutoLog(value = "批量添加")
    @ApiOperation(value = "批量添加", notes = "批量添加")
    @PostMapping(value = "/ListAdd")
    public Result<?> ListAdd(@RequestBody List<Wzyclchuchanggb> list) {
        System.out.println("list = " + list);
        wzyclchuchanggbService.saveListWzycljinchanggb(list);
        return Result.OK("添加成功！");

    }

}
