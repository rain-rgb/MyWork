package com.trtm.iot.yajiangm.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import com.trtm.iot.yajiangm.vo.YaJiangmsss;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.yajiangss.entity.TrYajiangSS;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxbS;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;

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
 * @Description: 压浆主表信息
 * @Author: jeecg-boot
 * @Date: 2021-09-06
 * @Version: V1.0
 */
@Api(tags = "压浆主表信息")
@RestController
@RequestMapping("/yajiangm/trYajiangM")
@Slf4j
public class TrYajiangMController extends JeecgController<TrYajiangM, ITrYajiangMService> {
    @Autowired
    private ITrYajiangMService trYajiangMService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ITrYajiangSService trYajiangSService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ITrYajiangRenwudanService trYajiangRenwudanService;
    @Autowired
    private IZhanglaYajiangOverHandlerService zhanglaYajiangOverHandlerService;

    @AutoLog(value = "压浆-分页列表查询")
    @ApiOperation(value = "压浆主表-分页列表查询", notes = "压浆主表-分页列表查询")
    @GetMapping(value = "/listCo")
    // @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseListNotUse")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> listCo(TrYajiangM trYajiangM,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trYajiangM.getYjsbbh() == null) {
            if (!"null".equals(shebei)) {
                trYajiangM.setYjsbbh(shebei);
            } else {
                trYajiangM.setYjsbbh("空");
            }
        }
        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        queryWrapper.select(" count(1) coo");

        queryWrapper.last(
                " AND renwudanstatus IN ( 28, 30 )  "+ // 查找 未审批或未填报的生产数据
                        "    AND product_datetime >= DATE_SUB(CURDATE(), INTERVAL 5 DAY) " +
                        "    AND product_datetime < DATE_SUB(CURDATE(), INTERVAL 1 DAY)  ");// -- 2天前到5天前，不包括昨天

        Map<String, Object> map = trYajiangMService.getMap(queryWrapper);

        return Result.OK(map);
    }

    /**
     * 分页列表查询
     *
     * @param trYajiangM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "压浆主表信息-分页列表查询")
    @ApiOperation(value = "压浆主表信息-分页列表查询", notes = "压浆主表信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrYajiangM trYajiangM,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trYajiangM.getYjsbbh() == null) {
            if (!"null".equals(shebei)) {
                trYajiangM.setYjsbbh(shebei);
            } else {
                trYajiangM.setYjsbbh("空");
            }
        }
        if (trYajiangM.getLianghao() != null){
            trYajiangM.setLianghao("*"+trYajiangM.getLianghao()+"*");
        }
        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        Page<TrYajiangM> page = new Page<TrYajiangM>(pageNo, pageSize);
        IPage<TrYajiangM> pageList = trYajiangMService.page(page, queryWrapper);
        for (TrYajiangM record : pageList.getRecords()) {
            TrYajiangRenwudan selectone = trYajiangRenwudanService.selectone(record.getUuid());
            if (selectone != null) {
                record.setLianghao(selectone.getSgbwname());
            }
            List<TrYajiangS> trYajiangS = trYajiangSService.selectListTrYajiangS(record.getSyjid());
            String isHege = "1";
            if (trYajiangS.size() > 0) {
                for (TrYajiangS trYajiang : trYajiangS) {
                    if (trYajiang.getHege().equals("0")) {
                        isHege = "0";
                    } else if (trYajiang.getHege().isEmpty()) {
                        isHege = null;
                    }
                }
            }
            record.setIsHege(isHege);
        }
        return Result.OK(pageList);
    }

    //永康删除zhanglas表数据方法
    @GetMapping(value = "/listjh")
    public void Testjh(){
        String shebei ="ykS316yj01";
        List<String> strings = trYajiangMService.selectListsb(shebei);
        QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("syjid",strings);
        System.out.println(strings);
        List<TrYajiangS> list = trYajiangSService.list(queryWrapper);
        for (TrYajiangS l :list){
            trYajiangSService.removeById(l.getId());
        }
    }
    /**
     * 看板
     *
     * @param trYajiangM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "压浆主表信息-分页列表查询")
    @ApiOperation(value = "压浆主表信息-分页列表查询", notes = "压浆主表信息-分页列表查询")
    @GetMapping(value = "/listrt")
    public Result<?> queryPageListrt(TrYajiangM trYajiangM,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trYajiangM.getYjsbbh() == null) {
            if (!"null".equals(shebei)) {
                trYajiangM.setYjsbbh(shebei);
            } else {
                trYajiangM.setYjsbbh("空");
            }
        }
        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        List<TrYajiangM> list = trYajiangMService.list(queryWrapper);
        Map<String, Integer> map = new HashMap<>();
        map.put("zs", list.size());
        map.put("hgs", list.size());
        return Result.OK(map);
    }

    /**
     * 分页列表查询
     *
     * @param trYajiangM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "压浆主表信息-分页列表查询")
    @ApiOperation(value = "压浆主表信息-分页列表查询", notes = "压浆主表信息-分页列表查询")
    @GetMapping(value = "/list47")
    public Result<?> queryPageList47(TrYajiangM trYajiangM,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trYajiangM.getYjsbbh() == null) {
            if (!"null".equals(shebei)) {
                trYajiangM.setYjsbbh(shebei);
            } else {
                trYajiangM.setYjsbbh("空");
            }
        }
        trYajiangM.setLblx("*" + trYajiangM.getLblx() + "*");
        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        Page<TrYajiangM> page = new Page<TrYajiangM>(pageNo, pageSize);
        IPage<TrYajiangM> pageList = trYajiangMService.page(page, queryWrapper);
        for (TrYajiangM record : pageList.getRecords()) {
            if(record.getUuid() != null && record.getYjsbbh().equals("dq2023122801")){
                TrYajiangRenwudan selectone = trYajiangRenwudanService.selectone(record.getUuid());
                if (selectone != null){
                    int i = selectone.getSgbwname().lastIndexOf("|");
                    record.setLianghao(selectone.getSgbwname().substring(i+1));
                }
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getYjsbbh());
            String projectName = bhzCementBaseService.selectbyorgcode(selectshebeione.getSysOrgCode(), 7);
            if (projectName != null) {
                record.setGcmc(projectName);
            }
            List<TrYajiangS> trYajiangS = trYajiangSService.selectListTrYajiangS(record.getSyjid());
            String isHege = "1";
            if (trYajiangS.size() > 0) {
                for (TrYajiangS trYajiang : trYajiangS) {
                    if (trYajiang.getHege().equals("0")) {
                        isHege = "0";
                    } else if (trYajiang.getHege().isEmpty()) {
                        isHege = null;
                    }
                }
            }
            record.setIsHege(isHege);
        }
        return Result.OK(pageList);
    }


    @AutoLog(value = "压浆主表信息-分页列表查询")
    @ApiOperation(value = "压浆主表信息-分页列表查询", notes = "压浆主表信息-分页列表查询")
    @GetMapping(value = "/list47NoUse")
    public Result<?> queryPagelist47NoUse(TrYajiangM trYajiangM,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trYajiangM.getYjsbbh() == null) {
            if (!"null".equals(shebei)) {
                trYajiangM.setYjsbbh(shebei);
            } else {
                trYajiangM.setYjsbbh("空");
            }
        }
        trYajiangM.setLblx("*" + trYajiangM.getLblx() + "*");

        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        queryWrapper.notLike("uuid","YJ-RWD").or().isNotNull("bindrwd");
        Page<TrYajiangM> page = new Page<TrYajiangM>(pageNo, pageSize);
        IPage<TrYajiangM> pageList = trYajiangMService.page(page, queryWrapper);
        for (TrYajiangM record : pageList.getRecords()) {

            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getYjsbbh());
            String projectName = bhzCementBaseService.selectbyorgcode(selectshebeione.getSysOrgCode(), 7);
            if (projectName != null) {
                record.setGcmc(projectName);
            }
            List<TrYajiangS> trYajiangS = trYajiangSService.selectListTrYajiangS(record.getSyjid());
            String isHege = "1";
            if (trYajiangS.size() > 0) {
                for (TrYajiangS trYajiang : trYajiangS) {
                    if (trYajiang.getHege().equals("0")) {
                        isHege = "0";
                    } else if (trYajiang.getHege().isEmpty()) {
                        isHege = null;
                    }
                }
            }
            record.setIsHege(isHege);
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "张拉信息表-分页列表查询")
    @ApiOperation(value = "张拉信息表-分页列表查询", notes = "张拉信息表-分页列表查询")
    @PostMapping(value = "/updateUse")
    public Result<?> batchUpdateUse( @RequestBody TrYajiangM trYajiangM ,
                                     HttpServletRequest req) {
        String msg = "";
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String baseid = trYajiangM.getYjsbbh()+trYajiangM.getId();
        ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = zhanglaYajiangOverHandlerService.selectoneMessage(baseid);
        if(ObjectUtil.isNotNull(zhanglaYajiangOverHandler1)){
            // zhanglaYajiangOverHandler1.setId(zhanglaYajiangOverHandler1.getId());
            zhanglaYajiangOverHandler1.setHeadquartersApproval(trYajiangM.getMemo());// 审批意见
            zhanglaYajiangOverHandler1.setSupervisorHandleTime(new Date());
            zhanglaYajiangOverHandler1.setApprovalPerson(loginUser.getRealname());
            msg = "审批通过";
        }else{
            zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
            zhanglaYajiangOverHandler1.setBaseid(baseid);
            zhanglaYajiangOverHandler1.setHandleResult(trYajiangM.getMemo());// 说明原因
            zhanglaYajiangOverHandler1.setHandleWay(trYajiangM.getSgbw());// 关联部位
            zhanglaYajiangOverHandler1.setHoleid(trYajiangM.getUuid());
            zhanglaYajiangOverHandler1.setHandleTime(new Date());
            zhanglaYajiangOverHandler1.setHandlePerson(loginUser.getRealname());
            msg = "说明填报成功，待审批";
        }
        zhanglaYajiangOverHandler1.setOverproofStatus(trYajiangM.getBindrwd());
        zhanglaYajiangOverHandlerService.saveOrUpdate(zhanglaYajiangOverHandler1);
        trYajiangM.setMemo(null);
        // 更新主表关联任务单编号，修改状态为已填报
        trYajiangMService.updateById(trYajiangM);
        return Result.OK(msg);
    }
    @AutoLog(value = "压浆主表信息-分页列表查询")
    @ApiOperation(value = "压浆主表信息-分页列表查询", notes = "压浆主表信息-分页列表查询")
    @GetMapping(value = "/list47s")
    public Result<?> queryPageList47s(TrYajiangM trYajiangM,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trYajiangM.getYjsbbh() == null) {
            if (!"null".equals(shebei)) {
                trYajiangM.setYjsbbh(shebei);
            } else {
                trYajiangM.setYjsbbh("空");
            }
        }
        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        Page<TrYajiangM> page = new Page<TrYajiangM>(pageNo, pageSize);
        IPage<TrYajiangM> pageList = trYajiangMService.page(page, queryWrapper);
        for (TrYajiangM record : pageList.getRecords()) {
            QueryWrapper<ZhanglaYajiangOverHandler> handlerWrapper = new QueryWrapper<ZhanglaYajiangOverHandler>();
            handlerWrapper.eq("baseId", record.getSyjid());
            List<ZhanglaYajiangOverHandler> list = zhanglaYajiangOverHandlerService.list(handlerWrapper);
            record.setHandler(list);
        }
        return Result.OK(pageList);
    }


    /**
     * 压浆次数
     *
     * @param trYajiangM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "压浆主表信息-压浆次数")
    @ApiOperation(value = "压浆主表信息-压浆次数", notes = "压浆主表信息-压浆次数")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(TrYajiangM trYajiangM,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trYajiangM.getYjsbbh() == null) {
            if (!"null".equals(shebei)) {
                trYajiangM.setYjsbbh(shebei);
            } else {
                trYajiangM.setYjsbbh("空");
            }
        }
        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        queryWrapper.select("count(1) as id");
        List<TrYajiangM> pageList = trYajiangMService.list(queryWrapper);
        Map map = new HashMap();
        int sumyj = 0;
        for (TrYajiangM trYajiangM1 : pageList) {
            if (trYajiangM1 != null) {
                sumyj = trYajiangM1.getId();
            }
        }
        map.put("sumyj", sumyj);
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
        List<String> shebeiList = shebeiInfoService.selectShebeiList(sysOrgCode, 10);

        QueryWrapper<TrYajiangM> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("yjsbbh", shebeiList);
        List<TrYajiangM> list = trYajiangMService.list(queryWrapper);

        ArrayList<String> list1 = new ArrayList<>();
        if (list.size() == 0) {
            return Result.error("该标段设备尚未有压浆数据！！！");
        }
        for (TrYajiangM l : list) {
            list1.add(l.getSyjid());
        }
        QueryWrapper<TrYajiangS> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("syjid", list1);
        queryWrapper1.eq("hege", "0");
        queryWrapper1.groupBy("syjid");
        List<TrYajiangS> list2 = trYajiangSService.list(queryWrapper1);

        QueryWrapper<TrYajiangS> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("syjid", list1);
        queryWrapper2.eq("hege", "不合格");
        queryWrapper2.groupBy("syjid");
        List<TrYajiangS> list3 = trYajiangSService.list(queryWrapper2);
        Map map = new HashMap();
        map.put("sum", list.size());
        map.put("bhg", list2.size() + list3.size());

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
    public Result<?> queryPageListZt(String sysOrgCode) {
        ArrayList<String> list5 = new ArrayList<>();
        list5.add("A05A01A05A01A01A01A01");
        list5.add("A05A01A05A01A01A01A02");
        list5.add("A05A01A05A01A01A01A03");
        list5.add("A05A01A05A01A01A01A04");
        List<String> shebeiList = new ArrayList<>();
        for (String l : list5) {
            List<String> shebeiList1 = shebeiInfoService.selectShebeiList(l, 10);
            for (String l1 : shebeiList1) {
                shebeiList.add(l1);
            }
        }

        QueryWrapper<TrYajiangM> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("yjsbbh", shebeiList);
        List<TrYajiangM> list = trYajiangMService.list(queryWrapper);

        ArrayList<String> list1 = new ArrayList<>();
        if (list.size() == 0) {
            return Result.error("尚未有压浆数据！！！");
        }
        for (TrYajiangM l : list) {
            list1.add(l.getSyjid());
        }
        QueryWrapper<TrYajiangS> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("syjid", list1);
        queryWrapper1.eq("hege", "0");
        queryWrapper1.groupBy("syjid");
        List<TrYajiangS> list2 = trYajiangSService.list(queryWrapper1);

        QueryWrapper<TrYajiangS> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("syjid", list1);
        queryWrapper2.eq("hege", "不合格");
        queryWrapper2.groupBy("syjid");
        List<TrYajiangS> list3 = trYajiangSService.list(queryWrapper2);
        Map map = new HashMap();
        map.put("sum", list.size());
        map.put("bhg", list2.size() + list3.size());

        return Result.OK(map);
    }

    /**
     * 列表查询-根据条件查询
     *
     * @param trYajiangM
     * @param req
     * @return
     */
    @AutoLog(value = "压浆主表信息-列表查询")
    @ApiOperation(value = "压浆主表信息-列表查询", notes = "压浆主表信息-列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(TrYajiangM trYajiangM, HttpServletRequest req) {
        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        List<TrYajiangM> list = trYajiangMService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param trYajiangM
     * @return
     */
    @AutoLog(value = "压浆主表信息-添加")
    @ApiOperation(value = "压浆主表信息-添加", notes = "压浆主表信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrYajiangM trYajiangM) {
        trYajiangMService.save(trYajiangM);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param yaJiangmsss
     * @return
     */
    @AutoLog(value = "压浆主表信息-添加")
    @ApiOperation(value = "压浆主表信息-添加", notes = "压浆主表信息-添加")
    @PostMapping(value = "/addYaJiang")
    public Result<?> addYaJiang(@RequestBody YaJiangmsss yaJiangmsss) {
        TrYajiangM trYajiangM = new TrYajiangM();
        BeanUtils.copyProperties(yaJiangmsss, trYajiangM);
        trYajiangMService.saveMain(trYajiangM, yaJiangmsss.getYajiangs());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trYajiangM
     * @return
     */
    @AutoLog(value = "压浆主表信息-编辑")
    @ApiOperation(value = "压浆主表信息-编辑", notes = "压浆主表信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrYajiangM trYajiangM) {
        trYajiangMService.updateById(trYajiangM);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "压浆主表信息-通过id删除")
    @ApiOperation(value = "压浆主表信息-通过id删除", notes = "压浆主表信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trYajiangMService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "压浆主表信息-批量删除")
    @ApiOperation(value = "压浆主表信息-批量删除", notes = "压浆主表信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trYajiangMService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "压浆主表信息-通过id查询")
    @ApiOperation(value = "压浆主表信息-通过id查询", notes = "压浆主表信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrYajiangM trYajiangM = trYajiangMService.getById(id);
        if (trYajiangM == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trYajiangM);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trYajiangM
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrYajiangM trYajiangM) {
        return super.exportXls(request, trYajiangM, TrYajiangM.class, "压浆主表信息");
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
        return super.importExcel(request, response, TrYajiangM.class);
    }

    /**
     * 压浆当月统计超标
     *
     * @return
     */
    @RequestMapping(value = "/countList", method = RequestMethod.GET)
    public Result<?> countList() {
        return Result.OK(trYajiangMService.countList());
    }

    @GetMapping(value = "/queryList")
    public Result<?> queryList(TrYajiangM trYajiangM,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                               HttpServletRequest req, String sys_depart_orgcode, String date) {
        QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
        shebeiInfoQueryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
        List<ShebeiInfo> list = shebeiInfoService.list(shebeiInfoQueryWrapper);

        String shebei = "";
        String substring = "";
        for (ShebeiInfo shebeiInfo : list) {
            shebei += shebeiInfo.getSbjno() + ",";
        }
        if (shebei.length() > 0) {
            substring = shebei.substring(0, shebei.length() - 1);
        }
        trYajiangM.setYjsbbh(substring);
        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        if (date != null) {
            queryWrapper.gt("yjsj", date);
        }
        queryWrapper.orderByAsc("yjsj");
        Page<TrYajiangM> page = new Page<>(pageNo, pageSize);

        Page<TrYajiangM> page1 = trYajiangMService.page(page, queryWrapper);
        for (TrYajiangM trYajiangM1 : page1.getRecords()) {
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper1 = new QueryWrapper<>();
            shebeiInfoQueryWrapper1.eq("sbjno", trYajiangM1.getYjsbbh());
            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfoQueryWrapper1);
            trYajiangM1.setYjsbbh(one.getSbname());
        }

        return Result.OK(page1.getRecords());
    }


    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "压浆信息表-分页列表查询")
    @ApiOperation(value = "压浆信息表-分页列表查询", notes = "压浆信息表-分页列表查询")
    @GetMapping(value = "/newlist")
    public Result<?> newlist(TrYajiangM trYajiangM,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                             Integer isBiHe,
                             HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trYajiangM.getYjsbbh() == null) {
            if (!"null".equals(shebei)) {
                trYajiangM.setYjsbbh(shebei);
            } else {
                trYajiangM.setYjsbbh("空");
            }
        }
        QueryWrapper<TrYajiangM> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangM, req.getParameterMap());
        if (isBiHe != null) {
            if (isBiHe == 1) {
                queryWrapper.eq("overproof_status", "20");
            } else if (isBiHe == 0) {
                queryWrapper.ne("overproof_status", "20");
                queryWrapper.ne("is_over_level", 0);
            }
        }
        Page<TrYajiangM> page = new Page<TrYajiangM>(pageNo, pageSize);
        IPage<TrYajiangM> pageList = trYajiangMService.page(page, queryWrapper);
        List<Map> mapList = new ArrayList<>();
        for (TrYajiangM m : pageList.getRecords()) {

            List<TrYajiangS> trYajiangS = trYajiangSService.selectListTrYajiangS(m.getSyjid());
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(m.getYjsbbh());
            String departName = bhzCementBaseService.selectbyorgcode(selectshebeione.getSysOrgCode(), 7);
            for (int i = 0; i < 3; i++) {
                Map map = new HashMap();
                map.put("gcbw", m.getLianghao());
                if (selectshebeione != null) {
                    map.put("departName", departName);
                    map.put("shebeiNo", selectshebeione.getSbname());
                }
                if (trYajiangS.size() > i) {
                    map.put("kongdao", trYajiangS.get(i).getKongdao());
                    map.put("yajiangsj", trYajiangS.get(i).getYajiangsj());
                    map.put("jinjiangshu", trYajiangS.get(i).getJinjiangshu());
                    map.put("jinjiangyal", trYajiangS.get(i).getJinjiangyal());
                    map.put("hege", trYajiangS.get(i).getHege());
                    map.put("overproof_status", trYajiangS.get(i).getOverproofStatus());
                    TrYajiangRenwudan selectone = trYajiangRenwudanService.selectone(trYajiangM.getUuid());
                    if (selectone != null) {
                        map.put("sjyl", selectone.getSjyl());
                        map.put("lljl", selectone.getLljl());
                    } else {
                        map.put("sjyl", null);
                        map.put("lljl", null);
                    }
                    if (trYajiangS.get(i).getOverproofStatus() == 20) {
                        ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(trYajiangM.getSyjid());
                        if (zhanglaYajiangOverHandler != null) {
                            map.put("bhtime", zhanglaYajiangOverHandler.getHandleTime());
                        } else {
                            map.put("bhtime", null);
                        }
                    } else {
                        map.put("bhtime", null);
                    }
                } else {
                    map.put("kongdao", null);
                    map.put("yajiangsj", null);
                    map.put("jinjiangshu", null);
                    map.put("jinjiangyal", null);
                    map.put("hege", null);
                    map.put("overproof_status", null);
                    map.put("sjyl", null);
                    map.put("lljl", null);
                    map.put("bhtime", null);

                }
                mapList.add(map);
            }
        }
        Map resultMap = new HashMap();
        resultMap.put("current", pageList.getCurrent());
        resultMap.put("size", pageList.getSize());
        resultMap.put("total", pageList.getTotal());
        resultMap.put("records", mapList);
        return Result.OK(resultMap);
    }

//    resetPush
    @AutoLog(value = "重置")
    @ApiOperation(value = "重置", notes = "重置")
    @GetMapping(value = "/resetPush")
    public Result<?> resetPush(TrYajiangM trYajiangM) {
        String msg = "推送状态重置成功！";
        Integer id = trYajiangM.getId();
        QueryWrapper<TrYajiangM> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        TrYajiangM one = trYajiangMService.getOne(queryWrapper);
        one.setIsmt(0);
        trYajiangMService.updateById(one);
        return Result.OK(msg);
    }

}
