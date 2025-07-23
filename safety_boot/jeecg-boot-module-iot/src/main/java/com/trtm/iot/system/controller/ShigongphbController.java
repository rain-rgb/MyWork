package com.trtm.iot.system.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.vo.BhzCallCfgPage;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ExportVO;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IShigongphbService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 施工配合比
 * @Author: jeecg-boot
 * @Date: 2021-05-19
 * @Version: V1.0
 */
@Api(tags = "施工配合比")
@RestController
@RequestMapping("/system/shigongphb")
@Slf4j
public class ShigongphbController extends JeecgController<Shigongphb, IShigongphbService> {
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWztaizhangService wztaizhangService;

    /**
     * 料仓时间最大的一条
     * @return
     */
    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/listpld")
    public Result<?> queryPageListpld(String guid) {

        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("max(create_time) create_time");
        queryWrapper.eq("LCbianhao",guid);
        Wztaizhang one = wztaizhangService.getOne(queryWrapper);

        if(one == null){
            return Result.OKs(null);
        }
        QueryWrapper<Wztaizhang> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("create_time",one.getCreateTime());
        queryWrapper1.eq("LCbianhao",guid);
        Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
        return Result.OKs(one1.getPici());
    }



    /**
     * 料仓时间最大的一条
     * @return
     */
//    @AutoLog(value = "施工配合比-分页列表查询")
//    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
//    @GetMapping(value = "/listpld47")
//    public Result<?> queryPageListpld47(String guid) {
//
//        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("max(create_time) create_time");
//        queryWrapper.eq("LCbianhao",guid);
//        queryWrapper.exists("SELECT 1 FROM ycl_test_detail  td WHERE wztaizhang.pici = td.inspection_lot_number  and test_status =1 ");
//        Wztaizhang one = wztaizhangService.getOne(queryWrapper);
//
//        if(one == null){
//            return Result.OKs(null);
//        }
//        QueryWrapper<Wztaizhang> queryWrapper1 = new QueryWrapper<>();
//        queryWrapper1.eq("create_time",one.getCreateTime());
//        queryWrapper1.eq("LCbianhao",guid);
//        Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
//        return Result.OKs(one1.getPici());
//    }
    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/listpld47")
    public Result<?> queryPageListpld47(String guid) {
          // 查询关联料仓的最新3条数据
        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(" CONCAT_WS( '|',pici, \n" +
                "\n" +
                "( CASE WHEN jianyanstate = 0 THEN '未检验'\n" +
                " WHEN jianyanstate = 1 THEN '合格'\n" +
                " WHEN jianyanstate = 3 THEN '检验中'\n" +
                "\tELSE \"\" END )\n" +
                ")\n" +
                " as `text`,pici `value` , jianyanstate");

        queryWrapper.eq("LCbianhao",guid);
        queryWrapper.eq("jystatus",1);
        queryWrapper.orderByDesc("jinchangshijian");
        queryWrapper.last(" limit 5 ");
        List<Map<String, Object>> one = wztaizhangService.listMaps(queryWrapper);

        return Result.OKs(one);
    }



    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/listpld47s")
    public Result<?> queryPageListpld47s(String guid) {
        // 查询关联料仓 和检验批 关联表的最新5条数据
        List<Map> one = wztaizhangService.selectPiciBylc(guid);
        return Result.OKs(one);
    }

    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/listpld47s2")
    public Result<?> queryPageListpld47s2(String guid) {
        // 查询关联料仓 和检验批 关联表的最新5条数据
        List<Map> one = wztaizhangService.selectPiciBylc2(guid);
        return Result.OKs(one);
    }



    /**
     * 分页列表查询
     *
     * @param shigongphb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Shigongphb shigongphb,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shigongphb.getShebeibianhao() == null) {
            if (!"null" .equals(shebei)) {
                shigongphb.setShebeibianhao(shebei);
            } else {
                shigongphb.setShebeibianhao("空");
            }
        }
        shigongphb.setIsdel(0);
        shigongphb.setCode("*" + shigongphb.getCode() + "*");
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        Page<Shigongphb> page = new Page<Shigongphb>(pageNo, pageSize);
        IPage<Shigongphb> pageList = shigongphbService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param shigongphb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/sphblist")
    public Result<?> queryPagesphbList(Shigongphb shigongphb,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shigongphb.getShebeibianhao() == null) {
            if (!"null" .equals(shebei)) {
                shigongphb.setShebeibianhao(shebei);
            } else {
                shigongphb.setShebeibianhao("空");
            }
        }
        shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        queryWrapper.likeRight("code", "SPHB-");
        Page<Shigongphb> page = new Page<Shigongphb>(pageNo, pageSize);
        IPage<Shigongphb> pageList = shigongphbService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/getphb")
    public Result<?> getphb(Shigongphb shigongphb,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        queryWrapper.likeRight("code", "SPHB-");
        queryWrapper.orderByDesc("id");
        Page<Shigongphb> page = new Page<Shigongphb>(pageNo, pageSize);
        IPage<Shigongphb> pageList = shigongphbService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param shigongphb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/lists")
    public Result<?> queryPageLists(Shigongphb shigongphb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shigongphb.getShebeibianhao() == null) {
            if (!"null" .equals(shebei)) {
                shigongphb.setShebeibianhao(shebei);
            } else {
                shigongphb.setShebeibianhao("空");
            }
        }
        shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        queryWrapper.notLike("code", "SPHB-");
        Page<Shigongphb> page = new Page<Shigongphb>(pageNo, pageSize);
        IPage<Shigongphb> pageList = shigongphbService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/lists1")
    public Result<?> queryPageLists1(Shigongphb shigongphb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shigongphb.getShebeibianhao() == null) {
            if (!"null" .equals(shebei)) {
                shigongphb.setShebeibianhao(shebei);
            } else {
                shigongphb.setShebeibianhao("空");
            }
        }
        shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        queryWrapper.likeRight("code", "SPHB-");
        Page<Shigongphb> page = new Page<Shigongphb>(pageNo, pageSize);
        IPage<Shigongphb> pageList = shigongphbService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param shigongphb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/sphblists")
    public Result<?> queryPagesphbLists(Shigongphb shigongphb,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shigongphb.getShebeibianhao() == null) {
            if (!"null" .equals(shebei)) {
                shigongphb.setShebeibianhao(shebei);
            } else {
                shigongphb.setShebeibianhao("空");
            }
        }
        shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        queryWrapper.likeRight("code", "SPHB-");
        List<Shigongphb> pageList = shigongphbService.list(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "施工配合比-配比通知单")
    @ApiOperation(value = "施工配合比-配比通知单", notes = "施工配合比-配比通知单")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(Shigongphb shigongphb, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shigongphb.getShebeibianhao() == null) {
            if (!"null" .equals(shebei)) {
                shigongphb.setShebeibianhao(shebei);
            } else {
                shigongphb.setShebeibianhao("空");
            }
        }
        shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        queryWrapper.orderByDesc("id");
        List<Shigongphb> pageList = shigongphbService.list(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "施工配合比-配比通知单数据")
    @ApiOperation(value = "施工配合比-配比通知单数据", notes = "施工配合比-配比通知单数据")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(@RequestParam(name = "code") String code) {
        Result<Object> result = new Result<>();
        List<Shigongphb> shigongphbList = shigongphbService.queryones(code);
        Bhzrenwudan renwudan = bhzrenwudanService.queryselectone(shigongphbList.get(0).getRenwudan());
        result.setData(renwudan);
        result.setSuccess(true);
        result.setResult(shigongphbList);
        return result;
    }

    @AutoLog(value = "施工配合比-配比通知单数据")
    @ApiOperation(value = "施工配合比-配比通知单数据", notes = "施工配合比-配比通知单数据")
    @GetMapping(value = "/list2s")
    public Result<?> queryPageList2s(@RequestParam(name = "code") String code) {
        Result<Object> result = new Result<>();
        if(code.contains("RWD-TJ")){
            return queryPageList2syj(code);
        }
        List<Shigongphb> shigongphbList = shigongphbService.queryones(code);

        if (shigongphbList.size() > 0) {
            Bhzrenwudan renwudan = bhzrenwudanService.queryselectone(shigongphbList.get(0).getRenwudan());
            result.setData(renwudan);
            for (Shigongphb shigongphb : shigongphbList) {

                if (shigongphb.getLc1() != null) {
                    Wzliaocang loudouM1 = wzliaocangService.queryselectone(shigongphb.getLc1());
                    shigongphb.setLc1(loudouM1.getName());
                    shigongphb.setM1(loudouM1.getCailiaoname());
                }
                if (shigongphb.getLc2() != null) {
                    Wzliaocang loudouM2 = wzliaocangService.queryselectone(shigongphb.getLc2());
                    shigongphb.setLc2(loudouM2.getName());
                    shigongphb.setM2(loudouM2.getCailiaoname());
                }
                if (shigongphb.getLc3() != null) {
                    Wzliaocang loudouM3 = wzliaocangService.queryselectone(shigongphb.getLc3());
                    shigongphb.setLc3(loudouM3.getName());
                    shigongphb.setM3(loudouM3.getCailiaoname());
                }
                if (shigongphb.getLc4() != null) {
                    Wzliaocang loudouM4 = wzliaocangService.queryselectone(shigongphb.getLc4());
                    shigongphb.setLc4(loudouM4.getName());
                    shigongphb.setM4(loudouM4.getCailiaoname());
                }
                if (shigongphb.getLc5() != null) {
                    Wzliaocang loudouM5 = wzliaocangService.queryselectone(shigongphb.getLc5());
                    shigongphb.setLc5(loudouM5.getName());
                    shigongphb.setM5(loudouM5.getCailiaoname());
                }
                if (shigongphb.getLc6() != null) {
                    Wzliaocang loudouM6 = wzliaocangService.queryselectone(shigongphb.getLc6());
                    shigongphb.setLc6(loudouM6.getName());
                    shigongphb.setM6(loudouM6.getCailiaoname());
                }
                if (shigongphb.getLc7() != null) {
                    Wzliaocang loudouM7 = wzliaocangService.queryselectone(shigongphb.getLc7());
                    shigongphb.setLc7(loudouM7.getName());
                    shigongphb.setM7(loudouM7.getCailiaoname());
                }
                if (shigongphb.getLc8() != null) {
                    Wzliaocang loudouM8 = wzliaocangService.queryselectone(shigongphb.getLc8());
                    shigongphb.setLc8(loudouM8.getName());
                    shigongphb.setM8(loudouM8.getCailiaoname());
                }
                if (shigongphb.getLc9() != null) {
                    Wzliaocang loudouM9 = wzliaocangService.queryselectone(shigongphb.getLc9());
                    shigongphb.setLc9(loudouM9.getName());
                    shigongphb.setM9(loudouM9.getCailiaoname());
                }
                if (shigongphb.getLc10() != null) {
                    Wzliaocang loudouM10 = wzliaocangService.queryselectone(shigongphb.getLc10());
                    shigongphb.setLc10(loudouM10.getName());
                    shigongphb.setM10(loudouM10.getCailiaoname());
                }
                if (shigongphb.getLc11() != null) {
                    Wzliaocang loudouM11 = wzliaocangService.queryselectone(shigongphb.getLc11());
                    shigongphb.setLc11(loudouM11.getName());
                    shigongphb.setM11(loudouM11.getCailiaoname());
                }
                if (shigongphb.getLc12() != null) {
                    Wzliaocang loudouM12 = wzliaocangService.queryselectone(shigongphb.getLc12());
                    shigongphb.setLc12(loudouM12.getName());
                    shigongphb.setM12(loudouM12.getCailiaoname());
                }
                if (shigongphb.getLc13() != null) {
                    Wzliaocang loudouM13 = wzliaocangService.queryselectone(shigongphb.getLc13());
                    shigongphb.setLc13(loudouM13.getName());
                    shigongphb.setM13(loudouM13.getCailiaoname());
                }
            }
        }else{
            return result.error500("未获取到配料数据");
        }

        result.setSuccess(true);
        result.setResult(shigongphbList);

        return result;
    }


    @AutoLog(value = "施工配合比-配比通知单数据,云检")
    @ApiOperation(value = "施工配合比-配比通知单数据", notes = "施工配合比-配比通知单数据云检")
    @GetMapping(value = "/list2syj")
    public Result<?> queryPageList2syj(@RequestParam(name = "code") String code) {
        Result<Object> result = new Result<>();
        JSONObject sendObject = JSONUtil.createObj();
      //  LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
       // code = "RWD-SG06-20241213-0001";
        String companyId="";
        String machineId="";
        if(code.contains("RWD-TJ03") ){
             companyId="31fd450e-406e-4bb0-b290-4ba7987f102d";
             machineId="HYGS-YLQGS-TJ03-0101";
        }else if ( code.contains("RWD-TJ05") ){
             companyId="2626ca43-5936-4cbc-a407-c3596a27e8e5";
             machineId="YLQGS-TJ05-0101";
        }else if(code.contains("RWD-TJ01") ) {
            companyId="97f15bc4-eefa-478b-9414-d4330f268f68";
            machineId="TBHZ-YLQGS-TJ01-0101";
        }else if(code.contains("RWD-TJ06") ) {
            companyId="1df55319-9463-4c3b-a63c-928002e2d17b";
            machineId="YLQGS-TJ06-0101";
        }else {
            return result.error500("未匹配标段");
        }
        sendObject.set("taskNumber", code);// 任务单
        sendObject.set("companyId", companyId);// 义龙庆5标标段id:2626ca43-5936-4cbc-a407-c3596a27e8e5
        sendObject.set("machineId", machineId);// 获取 对应拌合站编号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String post = HttpRequest.post("http://1.14.103.201:8300/IOTManagement/Api/jzl/getOrder")
                .header("Content-Type", "application/json")
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        JSONObject postObject = new JSONObject(post);
        String scode = (String) postObject.get("errcode");
        if(! "0".equals(scode)){
            return result.error500(postObject.toString());
        }
        if(ObjectUtil.isNull(postObject.get("data"))){
            return result.error500("未获取到当前浇筑令的配料信息！");
        }
        JSONObject data = (JSONObject) postObject.get("data");
        Bhzrenwudan renwudan = new Bhzrenwudan();
        renwudan.setOrderno((String) data.get("id"));
        renwudan.setCode((String) data.get("jzlh"));// 任务单号
        renwudan.setConspos((String) data.get("gcbw"));// 施工部位
        renwudan.setProjname((String) data.get("gcmc"));// 工程名称
        renwudan.setMete( Double.parseDouble((String) data.get("scfl")) );//设计⽣产⽅量
        renwudan.setBetlev((String) data.get("qddj"));//强度等级
        renwudan.setLands((String) data.get("tld"));// 塌落度
        renwudan.setPour((String) data.get("jzfs"));//浇筑方式
        try {
            renwudan.setDattim(sdf.parse((String) data.get("cjsj")) );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Shigongphb phb = new Shigongphb();

        if(! data.get("sgpb").equals(null)){ phb.setCode((String) data.get("sgpb")); // 配合⽐编号
        }
        if(! data.get("jzlh").equals(null)){ phb.setRenwudan((String) data.get("jzlh")); // 任务单号
        }
        if(! data.get("gcbw").equals(null)){ phb.setConspos((String) data.get("gcbw")); // 施工部位
        }
        if(! data.get("gcmc").equals(null)){ phb.setProjname((String) data.get("gcmc")); // 工程名称
        }
        if(! data.get("scfl").equals(null)){ phb.setMete( Double.parseDouble((String) data.get("scfl"))); //设计⽣产⽅量
        }
        if(! data.get("qddj").equals(null)){ phb.setBetlev((String) data.get("qddj")); //强度等级
        }
        if(! data.get("tld").equals(null)){ phb.setLands((String) data.get("tld")); // 塌落度
        }
        if(! data.get("jzfs").equals(null)){ phb.setPour((String) data.get("jzfs")); //浇筑方式
        }
        if(! data.get("sn1location").equals(null)){ phb.setLc1((String) data.get("sn1location")); // 料仓(水泥)
        }
        if(! data.get("fmh1location").equals(null)){ phb.setLc2((String) data.get("fmh1location")); // 料仓(粉煤灰)
        }
        if(! data.get("kf1location").equals(null)){ phb.setLc3((String) data.get("kf1location")); // 料仓(矿粉)
        }
        if(! data.get("jzs1location").equals(null)){ phb.setLc4((String) data.get("jzs1location")); // 料仓(骨料1)
        }
        if(! data.get("ss1location").equals(null)){ phb.setLc5((String) data.get("ss1location")); // 料仓(骨料2)
        }
        if(! data.get("ss2location").equals(null)){ phb.setLc6((String) data.get("ss2location")); // 料仓(骨料3)
        }
        if(! data.get("ss3location").equals(null)){ phb.setLc7((String) data.get("ss3location")); // 料仓(骨料4)
        }
        if(! data.get("wjj1location").equals(null)){ phb.setLc8((String) data.get("wjj1location")); // 料仓(外加剂1)
        }
        if(! data.get("wjj2location").equals(null)){ phb.setLc9((String) data.get("wjj2location")); // 料仓(外加剂2)
        }
        if(! data.get("slocation").equals(null)){ phb.setLc11((String) data.get("slocation")); // 料仓(水)
        }
        if(! data.get("jzs2location").equals(null)){ phb.setLc12((String) data.get("jzs2location")); // 料仓(骨料5)
        }
        if(! data.get("sn1name").equals(null)){ phb.setM1((String) data.get("sn1name")); // 名称(水泥)
        }
        if(! data.get("fmh1name").equals(null)){ phb.setM2((String) data.get("fmh1name")); // 名称(粉煤灰)
        }
        if(! data.get("kf1name").equals(null)){ phb.setM3((String) data.get("kf1name")); // 名称(矿粉)
        }
        if(! data.get("jzs1name").equals(null)){ phb.setM4((String) data.get("jzs1name")); // 名称(骨料1)
        }
        if(! data.get("ss1name").equals(null)){ phb.setM5((String) data.get("ss1name")); // 名称(骨料2)
        }
        if(! data.get("ss2name").equals(null)){ phb.setM6((String) data.get("ss2name")); // 名称(骨料3)
        }
        if(! data.get("ss3name").equals(null)){ phb.setM7((String) data.get("ss3name")); // 名称(骨料4)
        }
        if(! data.get("wjj1name").equals(null)){ phb.setM8((String) data.get("wjj1name")); // 名称(外加剂1)
        }
        if(! data.get("wjj2name").equals(null)){ phb.setM9((String) data.get("wjj2name")); // 名称(外加剂2)
        }
        if(! data.get("sname").equals(null)){ phb.setM11((String) data.get("sname")); // 名称(水)
        }
        if(! data.get("jzs2name").equals(null)){ phb.setM12((String) data.get("jzs2name")); // 名称(骨料5)}
        }




        // 实际用量(水泥)
        if(! data.get("sn1").equals(null)){  phb.setU1(Double.parseDouble((String)  data.get("sn1"))); phb.setRu1(Double.parseDouble((String)  data.get("sn1"))); }

        if(! data.get("fmh1").equals(null)){  phb.setU2(Double.parseDouble((String) data.get("fmh1")));     phb.setRu2(Double.parseDouble((String) data.get("fmh1")));}
        if(! data.get("kf1").equals(null)){  phb.setU3(Double.parseDouble((String) data.get("kf1")));  phb.setRu3(Double.parseDouble((String)  data.get("kf1")));// 实际用量(矿粉)// 用量(矿粉)
        }
        if(! data.get("jzs1").equals(null)){  phb.setU4(Double.parseDouble((String) data.get("jzs1")));  phb.setRu4(Double.parseDouble((String)  data.get("jzs1")));// 实际用量(骨料1)// 用量(骨料1)
        }
        if(! data.get("ss1").equals(null)){  phb.setU5(Double.parseDouble((String) data.get("ss1")));  phb.setRu5(Double.parseDouble((String)  data.get("ss1")));// 实际用量(骨料2)// 用量(骨料2)
        }
        if(! data.get("ss2").equals(null)){  phb.setU6(Double.parseDouble((String) data.get("ss2")));  phb.setRu6(Double.parseDouble((String)  data.get("ss2")));// 实际用量(骨料3)// 用量(骨料3)
        }
        if(! data.get("ss3").equals(null)){  phb.setU7(Double.parseDouble((String) data.get("ss3")));  phb.setRu7(Double.parseDouble((String)  data.get("ss3")));// 实际用量(骨料4)// 用量(骨料4)
        }
        if(! data.get("wjj1").equals(null)){  phb.setU8(Double.parseDouble((String) data.get("wjj1")));  phb.setRu8(Double.parseDouble((String)  data.get("wjj1")));// 实际用量(外加剂1)// 用量(外加剂1)
        }
        if(! data.get("wjj2").equals(null)){  phb.setU9(Double.parseDouble((String) data.get("wjj2")));  phb.setRu9(Double.parseDouble((String)  data.get("wjj2")));// 实际用量(外加剂2)// 用量(外加剂2)
        }
        if(! data.get("s").equals(null)){  phb.setU11(Double.parseDouble((String) data.get("s")));  phb.setRu11(Double.parseDouble((String)  data.get("s")));// 实际用量(水)// 用量(水)
        }
        if(! data.get("jzs2").equals(null)){  phb.setU12(Double.parseDouble((String) data.get("jzs2")));  phb.setRu12(Double.parseDouble((String)  data.get("jzs2")));// 实际用量(骨料5)// 用量(骨料5)
        }
            List<Shigongphb> shigongphbList = new ArrayList<>();
        shigongphbList.add(phb);

       // Bhzrenwudan renwudan = bhzrenwudanService.queryselectone(shigongphbList.get(0).getRenwudan());


        Bhzrenwudan queryselectone = bhzrenwudanService.queryselectone(renwudan.getCode());
        if(ObjectUtil.isNull(queryselectone)){
            bhzrenwudanService.save(renwudan);
        }
        Shigongphb shigongphb = shigongphbService.queryoneCode(phb.getCode());
        if(ObjectUtil.isNull(shigongphb)){
            shigongphbService.save(phb);
        }


        result.setSuccess(true);
        result.setResult(shigongphbList);
        result.setData(renwudan);
        return result;
    }

    /**
     * 分页列表查询
     *
     * @param shigongphb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "施工配合比-配料单-检验批数据查询")
    @ApiOperation(value = "施工配合比-配料单-检验批数据查询", notes = "施工配合比-配料单-检验批数据查询")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList5(Shigongphb shigongphb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shigongphb.getShebeibianhao() == null) {
            if (!"null" .equals(shebei)) {
                shigongphb.setShebeibianhao(shebei);
            } else {
                shigongphb.setShebeibianhao("空");
            }
        }
        shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        Page<Shigongphb> page = new Page<Shigongphb>(pageNo, pageSize);
        IPage<Shigongphb> pageList = shigongphbService.page(page, queryWrapper);
        Shigongphb shigongphb1 = pageList.getRecords().get(0);
        List list = new ArrayList();
        if (!StringUtils.isEmpty(shigongphb1.getLc1())) {
            Wztaizhang getselectlcone = wztaizhangService.getselectlcone(shigongphb1.getLc1());
            if (getselectlcone != null) {
                list.add(getselectlcone);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc2())) {
            Wztaizhang getselectlcone1 = wztaizhangService.getselectlcone(shigongphb1.getLc2());
            if (getselectlcone1 != null) {
                list.add(getselectlcone1);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc3())) {
            Wztaizhang getselectlcone2 = wztaizhangService.getselectlcone(shigongphb1.getLc3());
            if (getselectlcone2 != null) {
                list.add(getselectlcone2);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc4())) {
            Wztaizhang getselectlcone3 = wztaizhangService.getselectlcone(shigongphb1.getLc4());
            if (getselectlcone3 != null) {
                list.add(getselectlcone3);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc5()) && !shigongphb1.getLc5().equals(shigongphb1.getLc4())) {
            Wztaizhang getselectlcone4 = wztaizhangService.getselectlcone(shigongphb1.getLc5());
            if (getselectlcone4 != null) {
                list.add(getselectlcone4);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc6()) && !shigongphb1.getLc5().equals(shigongphb1.getLc6())) {
            Wztaizhang getselectlcone5 = wztaizhangService.getselectlcone(shigongphb1.getLc6());
            if (getselectlcone5 != null) {
                list.add(getselectlcone5);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc7()) && !shigongphb1.getLc6().equals(shigongphb1.getLc7())) {
            Wztaizhang getselectlcone6 = wztaizhangService.getselectlcone(shigongphb1.getLc7());
            if (getselectlcone6 != null) {
                list.add(getselectlcone6);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc8())) {
            Wztaizhang getselectlcone7 = wztaizhangService.getselectlcone(shigongphb1.getLc8());
            if (getselectlcone7 != null) {
                list.add(getselectlcone7);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc9()) && !shigongphb1.getLc8().equals(shigongphb1.getLc9())) {
            Wztaizhang getselectlcone8 = wztaizhangService.getselectlcone(shigongphb1.getLc9());
            if (getselectlcone8 != null) {
                list.add(getselectlcone8);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc10()) && !shigongphb1.getLc9().equals(shigongphb1.getLc10())) {
            Wztaizhang getselectlcone9 = wztaizhangService.getselectlcone(shigongphb1.getLc10());
            if (getselectlcone9 != null) {
                list.add(getselectlcone9);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc11())) {
            Wztaizhang getselectlcone10 = wztaizhangService.getselectlcone(shigongphb1.getLc11());
            if (getselectlcone10 != null) {
                list.add(getselectlcone10);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc12()) && !shigongphb1.getLc7().equals(shigongphb1.getLc12())) {
            Wztaizhang getselectlcone11 = wztaizhangService.getselectlcone(shigongphb1.getLc12());
            if (getselectlcone11 != null) {
                list.add(getselectlcone11);
            }
        }
        if (!StringUtils.isEmpty(shigongphb1.getLc13())) {
            Wztaizhang getselectlcone12 = wztaizhangService.getselectlcone(shigongphb1.getLc13());
            if (getselectlcone12 != null) {
                list.add(getselectlcone12);
            }
        }
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setResult(pageList);
        r.setData(list);
        return r;
    }

    /**
     * 分页列表查询
     *
     * @param shigongphb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(Shigongphb shigongphb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer id) {
        if (id != null) {
            shigongphb.setId(id);
        }
        shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        Page<Shigongphb> page = new Page<Shigongphb>(pageNo, pageSize);
        IPage<Shigongphb> pageList = shigongphbService.page(page, queryWrapper);
        List<Shigongphb> shigongphbs = pageList.getRecords();
        List data = new ArrayList<>();
        for (Shigongphb shigongphb1 : shigongphbs) {
            String shebeibianhao = shigongphb1.getShebeibianhao();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
            Bhzrenwudan bhzrenwudan = bhzrenwudanService.selectstation(shigongphb1.getRenwudan());
            shigongphb1.setShebeibianhao(selectshebeione.getSbname());
            if (bhzrenwudan != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                shigongphb1.setM32(simpleDateFormat.format(bhzrenwudan.getBegtim()));
                shigongphb1.setM31(bhzrenwudan.getCreateBy());
            }
            data.add(shigongphb1);
        }
        return Result.OKs(data);
    }

    @AutoLog(value = "施工配合比-总数统计")
    @ApiOperation(value = "施工配合比-总数统计", notes = "施工配合比-总数统计")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(Shigongphb shigongphb, HttpServletRequest req, String sysOrgCode) {
//		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//		 String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//		 if (shigongphb.getShebeibianhao() == null) {
//			 if (!"null".equals(shebei)) {
//				 shigongphb.setShebeibianhao(shebei);
//			 }else {
//				 shigongphb.setShebeibianhao("空");
//			 }
//		 }
//		 List<ShebeiInfo> shebeiInfoList = shebeiInfoService.arrayOneshebei(sysOrgCode);
//		 List<String> shebeilist = new ArrayList<>();
//		 for (ShebeiInfo shebeiInfo:shebeiInfoList){
//			 shebeilist.add(shebeiInfo.getSbjno());
//		 }
//		 shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ifnull(count(*),0) as isdel");
        queryWrapper.last("a left join shebei_info b on a.shebeibianhao = b.sbjno where b.sys_org_code like '" + sysOrgCode + "%'and a.isdel = 0 and b.shebei_status = 1");
//		 queryWrapper.in("shebeibianhao",shebeilist);
        List<Shigongphb> pageList = shigongphbService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param shigongphb
     * @return
     */
    @AutoLog(value = "施工配合比-添加")
    @ApiOperation(value = "施工配合比-添加", notes = "施工配合比-添加")
    @PostMapping(value = "/add")//只提供对外
    public Result<?> add(@RequestBody Shigongphb shigongphb) {
        if (StringUtils.isEmpty(shigongphb.getSysOrgCode()))
            return Result.error("组织机构代码不能为空");
        if (StringUtils.isEmpty(shigongphb.getCode() ))
            return Result.error("配合比编号不能为空");
        if (StringUtils.isEmpty(shigongphb.getRenwudan() ))
            return Result.error("任务单号不能为空");
        if (StringUtils.isEmpty(shigongphb.getShebeibianhao() ))
            return Result.error("设备编号不能为空");
        log.info(String.format("add配料单数据接收："+shigongphb.toString()));
        QueryWrapper<Shigongphb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", shigongphb.getCode());
        queryWrapper.eq("shebeibianhao", shigongphb.getShebeibianhao()).last("limit 1");
//        queryWrapper.eq("station", shigongphb.getStation());
        Shigongphb one = shigongphbService.getOne(queryWrapper);
        if (one != null) {
            shigongphb.setId(one.getId());
            shigongphbService.updateById(shigongphb);
        } else {
            shigongphbService.save(shigongphb);
        }
        return Result.OK("添加成功！");
    }


    /**
     * 添加
     *
     * @param shigongphb
     * @return
     */
    @AutoLog(value = "施工配合比-添加")
    @ApiOperation(value = "施工配合比-添加", notes = "施工配合比-添加")
    @PostMapping(value = "/add1")
    public Result<?> add1(@RequestBody Shigongphb shigongphb) {
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sbjno",shigongphb.getShebeibianhao());
        ShebeiInfo shebeiinfo = shebeiInfoService.getOne(queryWrapper);
        shigongphb.setSysOrgCode(shebeiinfo.getSysOrgCode());
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
        shigongphb.setCode(DateUtils.codeFormat("SPHB-"));
        if (shigongphb.getDattim() == null) {
            shigongphb.setDattim(date);
        } else {
            shigongphb.setDattim(shigongphb.getDattim());
        }
        shigongphbService.save(shigongphb);
        String renwudan = shigongphb.getRenwudan();
        Bhzrenwudan bhzrenwudan = bhzrenwudanService.queryselectone(renwudan);
        if(bhzrenwudan != null){
            bhzrenwudan.setStatus(3);
            bhzrenwudan.setSysOrgCode(shebeiinfo.getSysOrgCode());
            bhzrenwudanService.updateById(bhzrenwudan);
        }

        return Result.OK("添加成功！");
    }

//    @AutoLog(value = "施工配合比-添加")
//    @ApiOperation(value = "施工配合比-添加", notes = "施工配合比-添加")
//    @PostMapping(value = "/add47")
//    public Result<?> add47(@RequestBody Shigongphb shigongphb) {
//        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("sbjno",shigongphb.getShebeibianhao());
//        ShebeiInfo shebeiinfo = shebeiInfoService.getOne(queryWrapper);
//        shigongphb.setSysOrgCode(shebeiinfo.getSysOrgCode());
//        Date date = new Date();
//        DateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
//        shigongphb.setCode(DateUtils.codeFormat("SPHB-"));
//        if (shigongphb.getDattim() == null) {
//            shigongphb.setDattim(date);
//        } else {
//            shigongphb.setDattim(shigongphb.getDattim());
//        }
//        shigongphbService.save(shigongphb);
//        String renwudan = shigongphb.getRenwudan();
//        Bhzrenwudan bhzrenwudan = bhzrenwudanService.queryselectone(renwudan);
//        bhzrenwudan.setStatus(3);
//        bhzrenwudan.setSysOrgCode(shebeiinfo.getSysOrgCode());
//        bhzrenwudanService.updateById(bhzrenwudan);
//        return Result.OK("添加成功！");
//    }

    /**
     * 编辑
     *
     * @param shigongphb
     * @return
     */
    @AutoLog(value = "施工配合比-编辑")
    @ApiOperation(value = "施工配合比-编辑", notes = "施工配合比-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Shigongphb shigongphb) {
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sbjno",shigongphb.getShebeibianhao());
        ShebeiInfo shebeiinfo = shebeiInfoService.getOne(queryWrapper);
        shigongphb.setSysOrgCode(shebeiinfo.getSysOrgCode());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if(!StringUtils.isEmpty(shigongphb.getCheckStatus()) && shigongphb.getCheckStatus() == 0){
            shigongphb.setShren(loginUser.getUsername());
            shigongphb.setShshijian(new Date());
        }else{
            shigongphb.setShren(null);
            shigongphb.setShshijian(null);
            shigongphb.setCheckStatus(null);
        }

        shigongphb.setTongJi(0);
        shigongphbService.updateById(shigongphb);

        String renwudan = shigongphb.getRenwudan();
        Bhzrenwudan bhzrenwudan = bhzrenwudanService.queryselectone(renwudan);
        if(bhzrenwudan != null ){
            if( bhzrenwudan.getStatus() == 2){
                bhzrenwudan.setStatus(3);
            }
            bhzrenwudan.setSysOrgCode(shebeiinfo.getSysOrgCode());
            bhzrenwudanService.updateById(bhzrenwudan);
        }
        return Result.OK("编辑成功!");
    }


    /**
     * 编辑
     *
     * @param shigongphb
     * @return
     */
//    @AutoLog(value = "施工配合比-编辑")
//    @ApiOperation(value = "施工配合比-编辑", notes = "施工配合比-编辑")
//    @PutMapping(value = "/edit47")
//    public Result<?> edit47(@RequestBody Shigongphb shigongphb) {
//        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("sbjno",shigongphb.getShebeibianhao());
//        ShebeiInfo shebeiinfo = shebeiInfoService.getOne(queryWrapper);
//        shigongphb.setSysOrgCode(shebeiinfo.getSysOrgCode());
//        shigongphbService.updateById(shigongphb);
//
//        String renwudan = shigongphb.getRenwudan();
//        Bhzrenwudan bhzrenwudan = bhzrenwudanService.queryselectone(renwudan);
//        bhzrenwudan.setSysOrgCode(shebeiinfo.getSysOrgCode());
//        bhzrenwudanService.updateById(bhzrenwudan);
//
//        return Result.OK("编辑成功!");
//    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "施工配合比-通过id删除")
    @ApiOperation(value = "施工配合比-通过id删除", notes = "施工配合比-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) Integer id, Shigongphb shigongphb) {
        shigongphb.setId(id);
        shigongphb.setIsdel(1);//删除方法是给isdel 加标记
        shigongphbService.updateById(shigongphb);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "施工配合比-批量删除")
    @ApiOperation(value = "施工配合比-批量删除", notes = "施工配合比-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.shigongphbService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "施工配合比-通过id查询")
    @ApiOperation(value = "施工配合比-通过id查询", notes = "施工配合比-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Shigongphb shigongphb = shigongphbService.getById(id);
        if (shigongphb == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(shigongphb);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param shigongphb
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Shigongphb shigongphb) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<Shigongphb> queryList = shigongphbService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<Shigongphb> shigongphbList = new ArrayList<Shigongphb>();
        if(oConvertUtils.isEmpty(selections)) {
            shigongphbList = queryList;
        }else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            shigongphbList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<ExportVO> pageList = new ArrayList<ExportVO>();
        for (Shigongphb shigongphb1 : shigongphbList) {
            ExportVO phb = new ExportVO();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shigongphb1.getShebeibianhao());
            phb.setShebeibianhao(selectshebeione.getSbname());
            phb.setConspos(shigongphb1.getConspos());
            phb.setCode(shigongphb1.getCode());
            phb.setRenwudan(shigongphb1.getRenwudan());
            phb.setJztime(shigongphb1.getJztime());
            phb.setBetlev(shigongphb1.getBetlev());
            pageList.add(phb);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "配料单数据列表");
        mv.addObject(NormalExcelConstants.CLASS, ExportVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("配料单数据列表", "导出人:"+sysUser.getRealname(), "配料单数据列表"));
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
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Shigongphb.class);
    }

    //    resetPush
    @AutoLog(value = "重置")
    @ApiOperation(value = "重置", notes = "重置")
    @GetMapping(value = "/resetPush")
    public Result<?> resetPush(TrYajiangM trYajiangM) {
        String msg = "推送状态重置成功！";
        Integer id = trYajiangM.getId();
        QueryWrapper<Shigongphb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Shigongphb one = shigongphbService.getOne(queryWrapper);
        one.setIszjzl(0);
        shigongphbService.updateById(one);
        return Result.OK(msg);
    }
}
