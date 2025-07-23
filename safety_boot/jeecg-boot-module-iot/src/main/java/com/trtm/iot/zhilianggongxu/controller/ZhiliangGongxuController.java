package com.trtm.iot.zhilianggongxu.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.cunliang.entity.BeammanagementProcedure;
import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.trtm.iot.zhiliangtaizuocfg.service.IZhiliangTaizuoCfgService;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.jeecg.common.util.DateUtils.differHours;

/**
 * @Description: 制梁工序表信息
 * @Author: jeecg-boot
 * @Date: 2021-09-22
 * @Version: V1.0
 */
@Api(tags = "制梁工序表信息")
@RestController
@RequestMapping("/zhilianggongxu/zhiliangGongxu")
@Slf4j
public class ZhiliangGongxuController extends JeecgController<ZhiliangGongxu, IZhiliangGongxuService> {
    @Autowired
    private IZhiliangGongxuService zhiliangGongxuService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IZhiliangTaizuoCfgService zhiliangTaizuoCfgService;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;

    /**
     * 分页列表查询
     *
     * @param zhiliangGongxu
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁工序表信息-分页列表查询")
    @ApiOperation(value = "制梁工序表信息-分页列表查询", notes = "制梁工序表信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ZhiliangGongxu zhiliangGongxu,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        zhiliangGongxu.setIsdel(0);
        QueryWrapper<ZhiliangGongxu> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangGongxu, req.getParameterMap());
//        queryWrapper.ne("status",2);
        Page<ZhiliangGongxu> page = new Page<ZhiliangGongxu>(pageNo, pageSize);
        IPage<ZhiliangGongxu> pageList = zhiliangGongxuService.page(page, queryWrapper);
//        List<ZhiliangGongxu> records = pageList.getRecords();
//        List<ZhiliangGongxu> list = new ArrayList<>();
//        String person = "工序自动确认";
//        boolean boo1 = false;
//        ZhiliangTaizuoCfg zhiliangTaizuoCfg =null;
//        Zhiliangrenwudan zhiliangrenwudan1 = zhiliangrenwudanService.selectuuid(zhiliangGongxu.getUuid());
//        if (null != zhiliangrenwudan1) {
//            if (!StringUtils.isEmpty(zhiliangrenwudan1.getTaizuono())) {
//                zhiliangTaizuoCfg = zhiliangTaizuoCfgService.selectzltaizuo(zhiliangrenwudan1.getTaizuono());
//                if (null != zhiliangTaizuoCfg) {
//                    boo1 = true;
//                }
//            }
//        }
//        for (ZhiliangGongxu zhiliangGongxu1 : records) {
//            list.add(zhiliangGongxu1);
//            List<ZhiliangGongxu> list1 = new ArrayList<>();
//            int i = list.indexOf(zhiliangGongxu1);
//            boolean boo = false;
//            int status = 0;
//            if (i == 0 && boo1) {
//                if (zhiliangGongxu1.getStatus() == 0) {
//                    long df = (new Date().getTime() - zhiliangrenwudan1.getProductiontime().getTime()) / (1000 * 60 * 60 * 24);
//                    if (df > 0 && !StringUtils.isEmpty(zhiliangGongxu1.getRemark())) {
//                        if (df > Integer.parseInt(zhiliangGongxu1.getRemark())) {
//                            status = 2;
//                        } else {
//                            status = 1;
//                        }
//                    }
//                }
//            }
//            if (i > 0) {
//                if (zhiliangGongxu1.getXuhao() != 7 && zhiliangGongxu1.getStatus() == 0) {
//                    List<ZhiliangGongxu> zhiliangGongxuList = subIndex(list, i);
//                    if (zhiliangGongxuList.size() > 0) {
//                        for (ZhiliangGongxu zhiliangGongxu2 : zhiliangGongxuList) {
//                            int i1 = zhiliangGongxuList.indexOf(zhiliangGongxu2);
//                            if (zhiliangGongxu.getXuhao()!=8) {
//                                if (i1 == (i - 1)) {
//                                    ZhiliangGongxu zhiliangGongxu3 = zhiliangGongxuService.selectuuid(zhiliangGongxu1.getUuid(), zhiliangGongxu2.getXuhao());
//                                    if (null != zhiliangGongxu3) {
//                                        if (StringUtil.isNotEmpty(String.valueOf(zhiliangGongxu3.getFinishtime())) && !"null".equals(String.valueOf(zhiliangGongxu3.getFinishtime()))) {
//                                            long df = (new Date().getTime() - zhiliangGongxu3.getFinishtime().getTime()) / (1000 * 60 * 60 * 24);
//                                            if (!StringUtils.isEmpty(zhiliangGongxu1.getRemark())) {
//                                                if (df > 0 && df < Long.parseLong(zhiliangGongxu1.getRemark())) {
//                                                    status = 1;
//                                                }
//                                            }
//                                            if (zhiliangGongxu2.getXuhao() == 2 && zhiliangGongxu2.getStatus() == 0) {
//                                                if (zhiliangrenwudan1 != null) {
//                                                    Bhzrenwudan bhzrenwudan = bhzrenwudanService.selectorderno(zhiliangrenwudan1.getCode());
//                                                    long date = new Date().getTime();
//                                                    if (null != bhzrenwudan) {
//                                                        if (StringUtil.isNotEmpty(String.valueOf(bhzrenwudan.getEndtim())) && !"null".equals(String.valueOf(bhzrenwudan.getEndtim()))) {
//                                                            long date1 = bhzrenwudan.getEndtim().getTime();
//                                                            if (date > date1) {
//                                                                status = 2;
//                                                            }
//                                                        } else {
//                                                            if (df > 1) {
//                                                                status = 2;
//                                                            }
//                                                        }
//                                                    } else {
//                                                        if (!StringUtils.isEmpty(zhiliangGongxu1.getRemark())) {
//                                                            if (df > Long.parseLong(zhiliangGongxu1.getRemark())) {
//                                                                status = 2;
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            } else {
//                                                if (!StringUtils.isEmpty(zhiliangGongxu1.getRemark())) {
//                                                    if (df > Long.parseLong(zhiliangGongxu1.getRemark())) {
//                                                        status = 2;
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if (status > 0) {
//                zhiliangGongxu1.setStatus(status);
//                if (status == 2) {
//                    zhiliangGongxu1.setFinishtime(new Date());
//                    zhiliangGongxu1.setResponsible(person);
//                }
//                zhiliangGongxuService.updateById(zhiliangGongxu1);
//                boo = true;
//            }
//            if (boo && boo1) {
//                zhiliangTaizuoCfg.setStatus(1);
//                zhiliangTaizuoCfg.setXuhao(zhiliangGongxu1.getXuhao());
//                zhiliangTaizuoCfgService.updateById(zhiliangTaizuoCfg);
//            }
//        }
        return Result.OK(pageList);
    }

    private List<ZhiliangGongxu> subIndex(List<ZhiliangGongxu> list, int i) {
        List<ZhiliangGongxu> list1 = new ArrayList<>();
        for (ZhiliangGongxu zhiliangGongxu : list) {
            if (list.indexOf(zhiliangGongxu) <= i) {
                list1.add(zhiliangGongxu);
            }
        }
        return list1;
    }

    /**
     * 分页列表查询
     *
     * @param zhiliangGongxu
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁工序表信息-分页列表查询")
    @ApiOperation(value = "制梁工序表信息-分页列表查询", notes = "制梁工序表信息-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(ZhiliangGongxu zhiliangGongxu,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        zhiliangGongxu.setIsdel(0);
        QueryWrapper<ZhiliangGongxu> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangGongxu, req.getParameterMap());

        Page<ZhiliangGongxu> page = new Page<ZhiliangGongxu>(pageNo, pageSize);
        IPage<ZhiliangGongxu> pageList = zhiliangGongxuService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param zhiliangGongxu
     * @return
     */
    @AutoLog(value = "制梁工序表信息-添加")
    @ApiOperation(value = "制梁工序表信息-添加", notes = "制梁工序表信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ZhiliangGongxu zhiliangGongxu) {
        zhiliangGongxuService.save(zhiliangGongxu);
        return Result.OK("添加成功！");
    }


    /**
     * 制梁工序确认
     *
     * @param zhiliangGongxu
     * @return
     */
    @AutoLog(value = "制梁工序确认")
    @ApiOperation(value = "制梁工序确认", notes = "制梁工序确认")
    @PostMapping(value = "/gongxuedit")
    public Result<?> add1(@RequestBody ZhiliangGongxu zhiliangGongxu) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", zhiliangGongxu.getUuid());
        queryWrapper.eq("isdel", 0);
        Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
        if (one == null) {
            return Result.error("制梁工序确认失败!");
        } else {
            if (!StringUtils.isEmpty(one.getTaizuono())) {
                ZhiliangTaizuoCfg zhiliangTaizuoCfg = null;
                if (zhiliangGongxu.getXuhao()==1) {
                    zhiliangTaizuoCfg = zhiliangTaizuoCfgService.selectzltaizuo(one.getTaizuono());
                }else {
                    zhiliangTaizuoCfg = zhiliangTaizuoCfgService.selectzltaizuos(one.getTaizuono());
                }
                if (null != zhiliangTaizuoCfg) {
                    if (zhiliangGongxu.getXuhao()!=8) {
                        zhiliangTaizuoCfg.setStatus(1);
                        zhiliangTaizuoCfg.setXuhao(zhiliangGongxu.getXuhao());
                        zhiliangTaizuoCfg.setBeamno(one.getCode());
                    }
                    zhiliangTaizuoCfgService.updateById(zhiliangTaizuoCfg);
                    zhiliangGongxu.setStatus(2);
                    zhiliangGongxu.setId(zhiliangGongxu.getId());
                    one.setXuhao(String.valueOf(zhiliangGongxu.getXuhao()));
                    one.setCunliangstatus(1);
                    zhiliangrenwudanService.updateById(one);
                    zhiliangGongxuService.updateById(zhiliangGongxu);
                    return Result.OK("制梁工序确认成功！");
                }else {
                    return Result.error(one.getTaizuono()+"在生产中");
                }
            }else {
                zhiliangGongxu.setStatus(2);
                zhiliangGongxu.setId(zhiliangGongxu.getId());
                one.setXuhao(String.valueOf(zhiliangGongxu.getXuhao()));
                one.setCunliangstatus(1);
                zhiliangrenwudanService.updateById(one);
                zhiliangGongxuService.updateById(zhiliangGongxu);
            }
            return Result.OK("制梁工序确认成功！");
        }
    }


    /**
     * 制梁工序确认
     *
     * @param zhiliangGongxu
     * @return
     */
    @AutoLog(value = "制梁工序确认")
    @ApiOperation(value = "制梁工序确认", notes = "制梁工序确认")
    @PostMapping(value = "/gongxuGdit")
    public Result<?> addGdit(@RequestBody ZhiliangGongxu zhiliangGongxu) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", zhiliangGongxu.getUuid());
        queryWrapper.eq("isdel", 0);
        Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
        QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("uuid", zhiliangGongxu.getUuid());
        queryWrapper1.eq("xuhao", zhiliangGongxu.getXuhao());
//        zhiliangGongxu.setFinishtime(null);
        ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper1);
        zhiliangGongxu.setId(one1.getId());
        if (one == null) {
            return Result.error("制梁工序确认失败!");
        } else {
            if (!StringUtils.isEmpty(one.getTaizuono())) {
                ZhiliangTaizuoCfg zhiliangTaizuoCfg = null;
                if (zhiliangGongxu.getXuhao()==1) {
                    zhiliangTaizuoCfg = zhiliangTaizuoCfgService.selectzltaizuo(one.getTaizuono());
                }else {
                    zhiliangTaizuoCfg = zhiliangTaizuoCfgService.selectzltaizuos(one.getTaizuono());
                }
                if (null != zhiliangTaizuoCfg) {
                    if (zhiliangGongxu.getXuhao()!=8) {
                        zhiliangTaizuoCfg.setStatus(1);
                        zhiliangTaizuoCfg.setXuhao(zhiliangGongxu.getXuhao());
                        zhiliangTaizuoCfg.setBeamno(one.getCode());
                    }
                    one.setXuhao(String.valueOf(zhiliangGongxu.getXuhao()));
                    if(zhiliangGongxu.getXuhao() == 7){
                        if(zhiliangGongxu.getStatus() == 2){
                            one.setCunliangstatus(3);
                            zhiliangTaizuoCfg.setStatus(0);
                            if(zhiliangGongxu.getLiangceng() == 1){
                                cunliangProcedureConfigService.updatedata(zhiliangGongxu.getShebeino(),zhiliangGongxu.getTaizuoname());
                            }else {
                                cunliangProcedureConfigService.updatedatas(zhiliangGongxu.getShebeino(),zhiliangGongxu.getTaizuoname());
                            }
                        } else if(zhiliangGongxu.getStatus() != 2 && zhiliangGongxu.getLiangceng() == 1){
                            one.setCunliangstatus(2);
                            zhiliangTaizuoCfg.setStatus(0);
                            cunliangProcedureConfigService.updateStatus(zhiliangGongxu.getShebeino(),zhiliangGongxu.getTaizuoname());
                        } else {
                            one.setCunliangstatus(2);
                            zhiliangTaizuoCfg.setStatus(0);
                            cunliangProcedureConfigService.updateStatus1(zhiliangGongxu.getShebeino(),zhiliangGongxu.getTaizuoname());
                        }
                    }
                    one.setCunliangstatus(1);
                    zhiliangTaizuoCfgService.updateById(zhiliangTaizuoCfg);
                    zhiliangrenwudanService.updateById(one);
                    zhiliangGongxuService.updateById(zhiliangGongxu);
                    return Result.OK("制梁工序确认成功！");
                }else {
                    return Result.error(one.getTaizuono()+"在生产中");
                }
            }else {
                one.setXuhao(String.valueOf(zhiliangGongxu.getXuhao()));
                if(zhiliangGongxu.getXuhao() == 7){
                    if(zhiliangGongxu.getStatus() == 2){
                        one.setCunliangstatus(3);
                        if(zhiliangGongxu.getLiangceng() == 1){
                            cunliangProcedureConfigService.updatedata(zhiliangGongxu.getShebeino(),zhiliangGongxu.getTaizuoname());
                        }else {
                            cunliangProcedureConfigService.updatedatas(zhiliangGongxu.getShebeino(),zhiliangGongxu.getTaizuoname());
                        }
                    } else if(zhiliangGongxu.getStatus() != 2 && zhiliangGongxu.getLiangceng() == 1){
                        one.setCunliangstatus(2);
                        cunliangProcedureConfigService.updateStatus(zhiliangGongxu.getShebeino(),zhiliangGongxu.getTaizuoname());
                    } else {
                        one.setCunliangstatus(2);
                        cunliangProcedureConfigService.updateStatus1(zhiliangGongxu.getShebeino(),zhiliangGongxu.getTaizuoname());
                    }
                }
                one.setCunliangstatus(1);
                zhiliangrenwudanService.updateById(one);
                zhiliangGongxuService.updateById(zhiliangGongxu);
            }
            return Result.OK("制梁工序确认成功！");
        }
    }

    /**
     * 制梁工序回退
     *
     * @param zhiliangGongxu
     * @return
     */
    @AutoLog(value = "制梁工序回退")
    @ApiOperation(value = "制梁工序回退", notes = "制梁工序回退")
    @PostMapping(value = "/gongxuGdit1")
    public Result<?> addGdit1(@RequestBody ZhiliangGongxu zhiliangGongxu) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", zhiliangGongxu.getUuid());
        queryWrapper.eq("isdel", 0);
        Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
        QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("uuid", zhiliangGongxu.getUuid());
        queryWrapper1.eq("xuhao", zhiliangGongxu.getXuhao());
//        zhiliangGongxu.setFinishtime(null);
        ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper1);
        if(zhiliangGongxu.getXuhao() == 7){
            zhiliangGongxu.setId(one1.getId());
            zhiliangGongxu.setFinishtime(null);
            zhiliangGongxu.setChutime(null);
            zhiliangGongxu.setChutime(null);
            zhiliangGongxu.setChupeople("");
            zhiliangGongxu.setCunpeople("");
            zhiliangGongxu.setLiangceng(0);
            zhiliangGongxu.setCunlianghang("");
            zhiliangGongxu.setCunlianglie("");
            zhiliangGongxu.setShebeino("");
            zhiliangGongxu.setTaizuoname("");
            zhiliangGongxu.setStatus(0);
            zhiliangGongxu.setResponsible("");
            //回退存梁台座状态
            if (one1.getTaizuoname() != null){
                QueryWrapper<CunliangProcedureConfig> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("liangzuoname",one1.getTaizuoname());
                queryWrapper2.eq("dangqianceng",one1.getLiangceng());
                queryWrapper2.eq("shebeino",one1.getShebeino());
                CunliangProcedureConfig one2 = cunliangProcedureConfigService.getOne(queryWrapper2);
                if (one1.getLiangceng() == 1){
                    one2.setStatus(0);
                }else {
                    one2.setStatus1(0);
                }
                cunliangProcedureConfigService.updateById(one2);
            }
        }
        QueryWrapper<ZhiliangGongxu> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("uuid", zhiliangGongxu.getUuid());
        List<Integer> list = new ArrayList<>();
        List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper2);
        for (ZhiliangGongxu l :list1){
            list.add(l.getXuhao());
        }
        zhiliangGongxu.setId(one1.getId());
        zhiliangGongxu.setFinishtime(null);
        zhiliangGongxu.setStatus(0);
        zhiliangGongxu.setResponsible("");

        if (one == null) {
            return Result.error("回滚失败!");
        } else {
            if (!StringUtils.isEmpty(one.getTaizuono())) {
                ZhiliangTaizuoCfg zhiliangTaizuoCfg = zhiliangTaizuoCfgService.selectzltaizuo(one.getTaizuono());
                if (null != zhiliangTaizuoCfg) {
                    zhiliangTaizuoCfg.setXuhao(list.get(list.indexOf(zhiliangGongxu.getXuhao())-1));
                    zhiliangTaizuoCfgService.updateById(zhiliangTaizuoCfg);
                    one.setXuhao(String.valueOf(list.get(list.indexOf(zhiliangGongxu.getXuhao())-1)));
                    one.setCunliangstatus(1);
                    zhiliangrenwudanService.updateById(one);
                    zhiliangGongxuService.updateById(zhiliangGongxu);
                    return Result.OK("回滚成功！");
                }else {
                    return Result.error(one.getTaizuono()+"在生产中");
                }
            }else {
                one.setXuhao(String.valueOf(list.get(list.indexOf(zhiliangGongxu.getXuhao())-1)));
                one.setCunliangstatus(1);
                zhiliangrenwudanService.updateById(one);
                zhiliangGongxuService.updateById(zhiliangGongxu);
            }
            return Result.OK("回滚成功！");
        }
    }
    /**
     * 编辑
     *
     * @param zhiliangGongxu
     * @return
     */
    @AutoLog(value = "制梁工序表信息-编辑")
    @ApiOperation(value = "制梁工序表信息-编辑", notes = "制梁工序表信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ZhiliangGongxu zhiliangGongxu) {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(11);
        list.add(2);
        list.add(4);
        list.add(5);
        list.add(10);
        list.add(7);
        QueryWrapper<ZhiliangGongxu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid",zhiliangGongxu.getUuid());
        queryWrapper.eq("xuhao",list.get(list.indexOf(zhiliangGongxu.getXuhao())-1));
        queryWrapper.ne("status",2);
        List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper);
        if (list1.size() != 0){
            return Result.error("请先确认上一步工序是否完成!");
        }

        Zhiliangrenwudan zhiliangrenwudan = zhiliangrenwudanService.selectuuid(zhiliangGongxu.getUuid());
        zhiliangrenwudan.setXuhao(zhiliangGongxu.getXuhao().toString());
        if (zhiliangGongxu.getResponsible() == null){
            zhiliangGongxu.setResponsible(zhiliangGongxu.getCreateBy());
        }
        zhiliangrenwudanService.updateById(zhiliangrenwudan);
        zhiliangGongxuService.updateById(zhiliangGongxu);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "制梁工序表信息-通过id删除")
    @ApiOperation(value = "制梁工序表信息-通过id删除", notes = "制梁工序表信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        zhiliangGongxuService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "制梁工序表信息-批量删除")
    @ApiOperation(value = "制梁工序表信息-批量删除", notes = "制梁工序表信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.zhiliangGongxuService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "制梁工序表信息-通过id查询")
    @ApiOperation(value = "制梁工序表信息-通过id查询", notes = "制梁工序表信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ZhiliangGongxu zhiliangGongxu = zhiliangGongxuService.getById(id);
        if (zhiliangGongxu == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(zhiliangGongxu);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param zhiliangGongxu
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZhiliangGongxu zhiliangGongxu) {
        return super.exportXls(request, zhiliangGongxu, ZhiliangGongxu.class, "制梁工序表信息");
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
        return super.importExcel(request, response, ZhiliangGongxu.class);
    }

}
