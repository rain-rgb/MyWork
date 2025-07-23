package com.trtm.iot.hntconsign.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.api.utils.json.JSONObject;
import com.trtm.iot.hntconsign.vo.HntConsignPage;
import com.trtm.iot.hntconsigncode.entity.HntConsignCode;
import com.trtm.iot.hntconsigncode.service.IHntConsignCodeService;
import com.trtm.iot.qrcode.entity.Qrcode;
import com.trtm.iot.qrcode.service.IQrcodeService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.sy.sydpssysample.entity.SyDpsSySample;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import com.trtm.sy.sydpssysample.service.ISyDpsSySampleService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.hntconsign.entity.HntConsign;
import com.trtm.iot.hntconsign.service.IHntConsignService;

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
 * @Description: 混凝土见证取样表信息
 * @Author: jeecg-boot
 * @Date: 2021-07-02
 * @Version: V1.0
 */
@Api(tags = "混凝土见证取样表信息")
@RestController
@RequestMapping("/hntconsign/hntConsign")
@Slf4j
public class HntConsignController extends JeecgController<HntConsign, IHntConsignService> {
    @Autowired
    private IHntConsignService hntConsignService;
    @Autowired
    private IQrcodeService iQrcodeService;
    @Autowired
    private IHntConsignCodeService hntConsignCodeService;
    @Autowired
    private ISyDpsSySampleService syDpsSySampleService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    /**
     * 分页列表查询
     *
     * @param hntConsign
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-分页列表查询")
    @ApiOperation(value = "混凝土见证取样表信息-分页列表查询", notes = "混凝土见证取样表信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(HntConsign hntConsign,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            hntConsign.setOrgcode("*" + sys_depart_orgcode + "*");
        }
        hntConsign.setWtbh('*' + hntConsign.getWtbh() + '*');
        QueryWrapper<HntConsign> queryWrapper = QueryGenerator.initQueryWrapper(hntConsign, req.getParameterMap());
        Page<HntConsign> page = new Page<HntConsign>(pageNo, pageSize);
        IPage<HntConsign> pageList = hntConsignService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param hntConsign
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-分页列表查询")
    @ApiOperation(value = "混凝土见证取样表信息-分页列表查询", notes = "混凝土见证取样表信息-分页列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(HntConsign hntConsign,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            hntConsign.setOrgcode("*" + sys_depart_orgcode + "*");
        }
        hntConsign.setWtbh('*' + hntConsign.getWtbh() + '*');
        QueryWrapper<HntConsign> queryWrapper = QueryGenerator.initQueryWrapper(hntConsign, req.getParameterMap());
        Page<HntConsign> page = new Page<HntConsign>(pageNo, pageSize);
        IPage<HntConsign> pageList = hntConsignService.page(page, queryWrapper);
        List<HntConsign> records = pageList.getRecords();
        List records1 = new ArrayList<>();
        for (HntConsign record : records) {
            HntConsignPage hntConsignPage = new HntConsignPage();
            Integer id = record.getId();
            String wtid = record.getWtid();
            String wtbh = record.getWtbh();
            String ypmc = record.getYpmc();
            String cdcm = record.getCdcm();
            String sysl = record.getSysl();
            String qydd = record.getQydd();
            String ypms = record.getYpms();
            String qyrq = record.getQyrq();
            String qyr = record.getQyr();
            String phbtzdbh = record.getPhbtzdbh();
            String gcmc = record.getGcmc();
            String sgbw = record.getSgbw();
            Integer testtype = record.getTesttype();
            String syxm = record.getSyxm();
            String syxmmc = record.getSyxmmc();
            String sysuliang = record.getSysuliang();
            String qddj = record.getQddj();
            String hntzl = record.getHntzl();
            String jbfs = record.getJbfs();
            String yplq = record.getYplq();
            String sybz = record.getSybz();
            String sjzl = record.getSjzl();
            String gjzj = record.getGjzj();
            String gjzl = record.getGjzl();
            String wtdw = record.getWtdw();
            String sybh = record.getSybh();
            String sydw = record.getSydw();
            String ggzl = record.getGgzl();
            String ddsl = record.getDdsl();
            String jybz = record.getJybz();
            String ysyj = record.getYsyj();
            String syrq = record.getSyrq();
            String qyjzr = record.getQyjzr();
            String wtr = record.getWtr();
            String wtfzr = record.getWtfzr();
            String zzyb = record.getZzyb();
            String lldh = record.getLldh();
            String syr = record.getSyr();
            String yjbgrq = record.getYjbgrq();
            Integer mstatus = record.getMstatus();
            String orgcode = record.getOrgcode();
            String qrcode = record.getQrcode();
            String chicun = record.getChicun();
            Date starttime = record.getStarttime();
            Date endtime = record.getEndtime();
            List<HntConsignCode> selectcodelist = hntConsignCodeService.selectcodelist(wtid);
            if (selectcodelist.size() > 0) {
                hntConsignPage.setHntConsignCodeList(selectcodelist);
            }
            hntConsignPage.setId(id);
            hntConsignPage.setWtid(wtid);
            hntConsignPage.setWtbh(wtbh);
            hntConsignPage.setYpmc(ypmc);
            hntConsignPage.setCdcm(cdcm);
            hntConsignPage.setSysl(sysl);
            hntConsignPage.setQydd(qydd);
            hntConsignPage.setYpms(ypms);
            hntConsignPage.setQyrq(qyrq);
            hntConsignPage.setQyr(qyr);
            hntConsignPage.setPhbtzdbh(phbtzdbh);
            hntConsignPage.setGcmc(gcmc);
            hntConsignPage.setSgbw(sgbw);
            hntConsignPage.setTesttype(testtype);
            hntConsignPage.setSyxm(syxm);
            hntConsignPage.setSyxmmc(syxmmc);
            hntConsignPage.setSysuliang(sysuliang);
            hntConsignPage.setQddj(qddj);
            hntConsignPage.setHntzl(hntzl);
            hntConsignPage.setJbfs(jbfs);
            hntConsignPage.setYplq(yplq);
            hntConsignPage.setSybz(sybz);
            hntConsignPage.setSjzl(sjzl);
            hntConsignPage.setGjzj(gjzj);
            hntConsignPage.setGjzl(gjzl);
            hntConsignPage.setWtdw(wtdw);
            hntConsignPage.setSybh(sybh);
            hntConsignPage.setSydw(sydw);
            hntConsignPage.setGgzl(ggzl);
            hntConsignPage.setDdsl(ddsl);
            hntConsignPage.setJybz(jybz);
            hntConsignPage.setYsyj(ysyj);
            hntConsignPage.setSyrq(syrq);
            hntConsignPage.setQyjzr(qyjzr);
            hntConsignPage.setWtr(wtr);
            hntConsignPage.setWtfzr(wtfzr);
            hntConsignPage.setZzyb(zzyb);
            hntConsignPage.setLldh(lldh);
            hntConsignPage.setSyr(syr);
            hntConsignPage.setYjbgrq(yjbgrq);
            hntConsignPage.setMstatus(mstatus);
            hntConsignPage.setOrgcode(orgcode);
            hntConsignPage.setQrcode(qrcode);
            hntConsignPage.setChicun(chicun);
            hntConsignPage.setStarttime(starttime);
            hntConsignPage.setEndtime(endtime);
            records1.add(hntConsignPage);
        }
        return Result.OK(records1);
    }

    @AutoLog(value = "混凝土见证取样表信息-分页列表查询")
    @ApiOperation(value = "混凝土见证取样表信息-分页列表查询", notes = "混凝土见证取样表信息-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(HntConsign hntConsign, String sys_depart_orgcode, HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            hntConsign.setOrgcode("*" + sys_depart_orgcode + "*");
        }
        QueryWrapper<HntConsign> queryWrapper = QueryGenerator.initQueryWrapper(hntConsign, req.getParameterMap());
        List<HntConsign> pageList = hntConsignService.list(queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 试块已过期/可试验/养护中/已出库数量统计
     *
     * @param hntConsign
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-试块已过期/可试验/养护中/已出库数量统计")
    @ApiOperation(value = "混凝土见证取样表信息-试块已过期/可试验/养护中/已出库数量统计", notes = "混凝土见证取样表信息-试块已过期/可试验/养护中/已出库数量统计")
    @GetMapping(value = "/getlist3")
    public Result<?> getlist3(HntConsign hntConsign,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
        QueryWrapper<HntConsign> queryWrapper = QueryGenerator.initQueryWrapper(hntConsign, req.getParameterMap());
        queryWrapper.select("ifnull(count(*),0) as mstatus");
        queryWrapper.gt("starttime", new Date());
        queryWrapper.lt("endtime", new Date());
        queryWrapper.eq("projectid", hntConsign.getProjectid());
        HntConsign one = hntConsignService.getOne(queryWrapper);
        Map<Object, Object> map = new HashMap<>();
        int expiredsum = 0;//已过期
        int testablesum = 0;//可试验
        int maintenancesum = 0;//养护中
        int outmaintenancesum = 0;//已出库
        if (one != null) {
            testablesum = one.getMstatus();
        }
        QueryWrapper<HntConsign> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("ifnull(count(*),0) as mstatus");
        queryWrapper1.eq("projectid", hntConsign.getProjectid());
        queryWrapper1.lt("starttime", new Date());
        HntConsign one1 = hntConsignService.getOne(queryWrapper1);
        if (one1 != null) {
            expiredsum = one1.getMstatus();
        }
        QueryWrapper<HntConsign> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select("ifnull(count(*),0) as mstatus");
        queryWrapper2.last("a left join hnt_consign_code b on a.wtid = b.wtid where a.projectid = '" + hntConsign.getProjectid() + "' and cstatus = 1");
        HntConsign one2 = hntConsignService.getOne(queryWrapper2);
        if (one2 != null) {
            maintenancesum = one2.getMstatus();
        }
        QueryWrapper<HntConsign> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.select("ifnull(count(*),0) as mstatus");
        queryWrapper3.last("a left join hnt_consign_code b on a.wtid = b.wtid where a.projectid = '" + hntConsign.getProjectid() + "' and cstatus = 2");
        HntConsign one3 = hntConsignService.getOne(queryWrapper3);
        if (one3 != null) {
            outmaintenancesum = one3.getMstatus();
        }
        map.put("expiredsum", expiredsum);
        map.put("testablesum", testablesum);
        map.put("maintenancesum", maintenancesum);
        map.put("outmaintenancesum", outmaintenancesum);
        return Result.OK(map);
    }


    /**
     * 试块已过期/可试验/养护中/已出库数量统计
     *
     * @param hntConsign
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-试块已过期/可试验/养护中/已出库数量统计")
    @ApiOperation(value = "混凝土见证取样表信息-试块已过期/可试验/养护中/已出库数量统计", notes = "混凝土见证取样表信息-试块已过期/可试验/养护中/已出库数量统计")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(HntConsign hntConsign,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        QueryWrapper<HntConsign> queryWrapper = QueryGenerator.initQueryWrapper(hntConsign, req.getParameterMap());
        queryWrapper.select("ifnull(count(*),0) as mstatus");
        queryWrapper.gt("starttime", new Date());
        queryWrapper.lt("endtime", new Date());
        queryWrapper.likeRight("orgcode", hntConsign.getOrgcode());
        HntConsign one = hntConsignService.getOne(queryWrapper);
        Map<Object, Object> map = new HashMap<>();
        int expiredsum = 0;//已过期
        int testablesum = 0;//可试验
        int maintenancesum = 0;//养护中
        int outmaintenancesum = 0;//已出库
        if (one != null) {
            testablesum = one.getMstatus();
        }
        QueryWrapper<HntConsign> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("ifnull(count(*),0) as mstatus");
        queryWrapper1.likeRight("orgcode", hntConsign.getOrgcode());
        queryWrapper1.lt("starttime", new Date());
        HntConsign one1 = hntConsignService.getOne(queryWrapper1);
        if (one1 != null) {
            expiredsum = one1.getMstatus();
        }
        QueryWrapper<HntConsign> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select("ifnull(count(*),0) as mstatus");
        queryWrapper2.last("a left join hnt_consign_code b on a.wtid = b.wtid where a.orgcode = '" + hntConsign.getOrgcode() + "%' and cstatus = 1");
        HntConsign one2 = hntConsignService.getOne(queryWrapper2);
        if (one2 != null) {
            maintenancesum = one2.getMstatus();
        }
        QueryWrapper<HntConsign> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.select("ifnull(count(*),0) as mstatus");
        queryWrapper3.last("a left join hnt_consign_code b on a.wtid = b.wtid where a.orgcode = '" + hntConsign.getOrgcode() + "%' and cstatus = 2");
        HntConsign one3 = hntConsignService.getOne(queryWrapper3);
        if (one3 != null) {
            outmaintenancesum = one3.getMstatus();
        }
        map.put("expiredsum", expiredsum);
        map.put("testablesum", testablesum);
        map.put("maintenancesum", maintenancesum);
        map.put("outmaintenancesum", outmaintenancesum);
        return Result.OK(map);
    }

    /**
     * 添加
     *
     * @param hntConsign
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-添加")
    @ApiOperation(value = "混凝土见证取样表信息-添加", notes = "混凝土见证取样表信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody HntConsign hntConsign) {
        hntConsignService.save(hntConsign);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param hntConsign
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-添加")
    @ApiOperation(value = "混凝土见证取样表信息-添加", notes = "混凝土见证取样表信息-添加")
    @PostMapping(value = "/add1")
    public Result<?> add1(@RequestBody HntConsign hntConsign) {
        String uuid = null;//随机生成唯一码UUID
        try {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            uuid = UUID.randomUUID().toString();
            hntConsign.setWtid(uuid);
            hntConsign.setTs(String.valueOf(new Date().getTime()));//获取当前时间戳
            hntConsign.setQyr(loginUser.getRealname());
            Calendar nowTime = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (hntConsign.getYplq().equals("1")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 1);
                nowTime.add(Calendar.MINUTE, 15);
                hntConsign.setStarttime(nowTime.getTime());
            } else if (hntConsign.getYplq().equals("3")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 3);
                nowTime.add(Calendar.MINUTE, 72);
                hntConsign.setStarttime(nowTime.getTime());
            } else if (hntConsign.getYplq().equals("7")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 7);
                nowTime.add(Calendar.MINUTE, 105);
                hntConsign.setStarttime(nowTime.getTime());
            } else if (hntConsign.getYplq().equals("28")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 28);
                nowTime.add(Calendar.MINUTE, 420);
                hntConsign.setStarttime(nowTime.getTime());
            } else if (hntConsign.getYplq().equals("56")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 56);
                nowTime.add(Calendar.MINUTE, 840);
                hntConsign.setStarttime(nowTime.getTime());
            }

            if (hntConsign.getYplq().equals("1")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 1);
                nowTime.add(Calendar.MINUTE, -15);
                hntConsign.setEndtime(nowTime.getTime());
            } else if (hntConsign.getYplq().equals("3")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 3);
                nowTime.add(Calendar.MINUTE, -72);
                hntConsign.setEndtime(nowTime.getTime());
            } else if (hntConsign.getYplq().equals("7")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 7);
                nowTime.add(Calendar.MINUTE, -105);
                hntConsign.setEndtime(nowTime.getTime());
            } else if (hntConsign.getYplq().equals("28")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 28);
                nowTime.add(Calendar.MINUTE, -420);
                hntConsign.setEndtime(nowTime.getTime());
            } else if (hntConsign.getYplq().equals("56")) {
                nowTime.setTime(sdf.parse(hntConsign.getQyrq()));
                nowTime.add(Calendar.DATE, 56);
                nowTime.add(Calendar.MINUTE, -840);
                hntConsign.setEndtime(nowTime.getTime());
            }
            if (hntConsign.getQrcode() != null && hntConsign.getCdcm() != null) {
                String qrcode = hntConsign.getQrcode();
                String cdcm = hntConsign.getCdcm();
                String[] split = qrcode.split(",");
                String[] split1 = cdcm.split(",");

                for (int i = 0; i < split.length; i++) {
                    HntConsignCode hntConsignCodes = new HntConsignCode();
                    hntConsignCodes.setCodeno(split[i]);
                    hntConsignCodes.setWtid(hntConsign.getWtid());
                    hntConsignCodes.setWtbh(hntConsign.getWtbh());
                    hntConsignCodes.setCstatus(0);
                    hntConsignCodes.setQyr(loginUser.getRealname());
                    hntConsignCodes.setQydate(hntConsign.getQyrq());
                    hntConsignCodes.setCode(split1[i]);
                    hntConsignCodeService.save(hntConsignCodes);
                }
            }
            hntConsign.setCdcm(null);
            hntConsignService.save(hntConsign);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Result.OK("添加成功！");
    }

    /**
     * 见证取样 二维码验证
     *
     * @param code
     * @return
     */
    @AutoLog(value = "混凝土见证取样--验证二维码")
    @ApiOperation(value = "混凝土见证取样--验证二维码", notes = "混凝土见证取样--验证二维码")
    @PostMapping(value = "/qrcodeyanzheng")
    public Result<?> qrcodeyanzheng(String code) {
        if (code != null) {
            QueryWrapper<HntConsignCode> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("codeno", code);
            queryWrapper.eq("cstatus", 0);
            HntConsignCode hntConsignCode = hntConsignCodeService.getOne(queryWrapper);
            Qrcode qrcode = iQrcodeService.queryGetOne(code);
            if (qrcode != null) {
                if (hntConsignCode == null) {
                    return Result.OK("可以添加");
                } else {
                    return Result.error("已经使用过！");
                }
            } else {
                return Result.error("二维码库不存在该二维码！");
            }
        } else {
            return Result.error("没有传输该二维码编码！");
        }
    }


    /**
     * 养护上架 二维码验证(#后面的验证)
     *
     * @param code
     * @return
     */
    @AutoLog(value = "混凝土养护上架--验证二维码")
    @ApiOperation(value = "混凝土养护上架--验证二维码", notes = "混凝土养护上架--验证二维码")
    @PostMapping(value = "/yhyanzheng")
    public Result<?> yhyanzheng(String code) {
        if (code != null) {
            LambdaQueryWrapper<HntConsignCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(HntConsignCode::getCodeno, code);
            lambdaQueryWrapper.eq(HntConsignCode::getCstatus, 0);
            HntConsignCode one = hntConsignCodeService.getOne(lambdaQueryWrapper);
            Qrcode qrcode = iQrcodeService.queryGetOne(code);
            if (one != null && qrcode != null) {
                return Result.OK("可以上架");
            } else {
                if (one == null) {
                    return Result.error("未见证取样或该二维码已经开始养护！");
                }
                if (qrcode == null) {
                    return Result.error("二维码库不存在该二维码！");
                }
            }
        } else {
            return Result.error("没有传输该二维码编码！");
        }
        return Result.error("不可上架");
    }

    /**
     * 养护上架 二维码验证（#号前面的验证）
     *
     * @param code
     * @return
     */
    @AutoLog(value = "混凝土养护上架--验证二维码")
    @ApiOperation(value = "混凝土养护上架--验证二维码", notes = "混凝土养护上架--验证二维码")
    @PostMapping(value = "/yhyanzhengNo")
    public Result<?> yhyanzhengNo(String code) {
        if (code != null) {
            LambdaQueryWrapper<HntConsignCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(HntConsignCode::getCode, code);
            lambdaQueryWrapper.eq(HntConsignCode::getCstatus, 0);
            HntConsignCode one = hntConsignCodeService.getOne(lambdaQueryWrapper);
            if (one != null) {
                return Result.OK("可以上架");
            } else {
                return Result.error("未见证取样或该二维码已经开始养护！");
            }
        }
        return Result.error("可以上架");
    }


    /**
     * 养护下架获取取样信息
     *
     * @param codeno
     * @return
     */
    @AutoLog(value = "养护下架获取取样信息")
    @ApiOperation(value = "养护下架获取取样信息", notes = "养护下架获取取样信息")
    @PostMapping(value = "/yhdownqueryOne")
    public Result<?> yhdownqueryOne(String codeno, String code) {
        if (codeno != null) {//直接查询对应的 取样信息
            LambdaQueryWrapper<HntConsign> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.like(HntConsign::getQrcode, codeno);
            HntConsign one = hntConsignService.getOne(lambdaQueryWrapper);
            if (one != null) {
                return Result.OK(one);
            } else {
                return Result.error("没有该二维码信息！");
            }
        }
        if (code != null) {//先查询具体养护数据  再去查询对应的取样信息
            LambdaQueryWrapper<HntConsignCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(HntConsignCode::getCode, code);
            lambdaQueryWrapper.eq(HntConsignCode::getCstatus, 1);
            HntConsignCode one = hntConsignCodeService.getOne(lambdaQueryWrapper);
            if (one != null) {
                String codeno1 = one.getCodeno();
                LambdaQueryWrapper<HntConsign> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.like(HntConsign::getQrcode, codeno1);
                HntConsign ones = hntConsignService.getOne(lambdaQueryWrapper1);
                return Result.OK(ones);
            } else {
                return Result.error("没有该二维码信息！");
            }
        }
        return Result.error("查询错误");
    }


    /**
     * 编辑
     *
     * @param hntConsign
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-编辑")
    @ApiOperation(value = "混凝土见证取样表信息-编辑", notes = "混凝土见证取样表信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody HntConsign hntConsign) {
        hntConsignService.updateById(hntConsign);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-通过id删除")
    @ApiOperation(value = "混凝土见证取样表信息-通过id删除", notes = "混凝土见证取样表信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        hntConsignService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-批量删除")
    @ApiOperation(value = "混凝土见证取样表信息-批量删除", notes = "混凝土见证取样表信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.hntConsignService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "混凝土见证取样表信息-通过id查询")
    @ApiOperation(value = "混凝土见证取样表信息-通过id查询", notes = "混凝土见证取样表信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        HntConsign hntConsign = hntConsignService.getById(id);
        if (hntConsign == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(hntConsign);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param hntConsign
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HntConsign hntConsign) {
        return super.exportXls(request, hntConsign, HntConsign.class, "混凝土见证取样表信息");
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
        return super.importExcel(request, response, HntConsign.class);
    }

    /**
     * 甬台温南段试验统计
     * @param
     * @return
     */
    @PostMapping(value = "/dataCollect")
    public Result<?> dataCollect() {
        List sendList = new ArrayList();
        //获取甬台温南段标段
        List<SysDepart> sysDepartList = hntConsignService.getbiaoduan();
        for (SysDepart sysDepart : sysDepartList) {
            Map dateMap = new HashMap();
            //标段名
            dateMap.put("project",sysDepart.getDepartName());
            QueryWrapper<SyDpsSySample> hntConsignQueryWrapper = new QueryWrapper<>();
            //标段下总委托数
            hntConsignQueryWrapper.select("count(1) sampleIsDel");
            LambdaQueryWrapper<ShebeiInfo>  shebeiInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (sysDepart.getOrgType().equals("7")) {
                hntConsignQueryWrapper.likeRight("orgcode", sysDepart.getOrgCode());
                shebeiInfoLambdaQueryWrapper.likeRight(ShebeiInfo::getSysOrgCode, sysDepart.getOrgCode());
            }else {
                hntConsignQueryWrapper.eq("orgcode", sysDepart.getOrgCode());
                shebeiInfoLambdaQueryWrapper.eq(ShebeiInfo::getSysOrgCode, sysDepart.getOrgCode());
            }
            SyDpsSySample one = syDpsSySampleService.getOne(hntConsignQueryWrapper);
            int daiban = 0;//代办
            Integer zwts = one.getSampleisdel();//总委托数

            List<ShebeiInfo> arrayshebei = shebeiInfoService.list(shebeiInfoLambdaQueryWrapper);
            List<String> ids = new ArrayList<>();
            for (ShebeiInfo shebei : arrayshebei) {
                ids.add("'" + shebei.getSbjno() + "'");
            }
            String inClause = "";
            if (!ids.isEmpty()) {
                inClause = String.join(", ", ids);
            } else {
                // 如果列表为空，可以设置一个默认值，如 'null' 或者 '0'
                inClause = "'null'";
            }
            List<TSyjzb> getsyj = hntConsignService.getsyj(inClause);

            int hege = 0;
            int youxiao = 0;
            int buhege = 0;
            int wuxiao = 0;
            for (TSyjzb tSyjzb : getsyj) {
                String pdjg = tSyjzb.getPdjg();
                int lq = tSyjzb.getLq();
                if ("合格".equals(pdjg)){
                    hege = lq;
                }
                if ("有效".equals(pdjg)){
                    youxiao = lq;
                }
                if ("不合格".equals(pdjg)){
                    buhege = lq;
                }
                if ("无效".equals(pdjg)){
                    wuxiao = lq;
                }
            }
            //合格数、有效、无效、不合格
            dateMap.put("hege",hege);
            dateMap.put("youxiao",youxiao);
            dateMap.put("buhege",buhege);
            dateMap.put("wuxiao",wuxiao);
            int syjzs = hege+youxiao+buhege+wuxiao;
            if (zwts < syjzs){
                zwts = syjzs;
                daiban = 0;
            }else {
                daiban = zwts - syjzs;
            }
            //总数、待办、已完成
            dateMap.put("count",zwts);
            dateMap.put("waitcount",daiban);
            dateMap.put("comcount",syjzs);


            double hegelv = 0;
            if (syjzs != 0){
                int i = hege + youxiao;
                double i1 = (double)i / syjzs;
                hegelv = i1 * 100;
            }
            // 使用 DecimalFormat 格式化结果保留两位小数
            DecimalFormat df = new DecimalFormat("#.00");
            String formattedResult = df.format(hegelv);
            //合格率
            dateMap.put("hegelv",formattedResult);
            sendList.add(dateMap);
        }
        return Result.OK(sendList);
    }
}
