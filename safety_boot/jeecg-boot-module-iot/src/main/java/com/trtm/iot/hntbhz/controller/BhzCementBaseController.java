package com.trtm.iot.hntbhz.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Key;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.qos.logback.core.pattern.parser.SimpleKeywordNode;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.car.vo.SchedulingCarVO;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.hntbhz.entity.*;
import com.trtm.iot.hntbhz.mapper.BhzCementBaseMapper;
import com.trtm.iot.hntbhz.vo.*;
import com.trtm.iot.lqbhz.entity.BhzLqWarnVO;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.service.IRenwudanScheduleService;
import com.trtm.iot.swbhz.entity.BhzSwWarnVO;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.entity.WZCaiLiao;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import com.trtm.iot.util.materialeUtil;
import com.trtm.iot.wzcailiaonamedictall.entity.WzcailiaonamedictAll;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wzliaocangcfg.entity.Wzliaocangcfg;
import com.trtm.iot.wzliaocangcfg.service.IWzliaocangcfgService;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.ztbhzjckh.entity.ZtBhzjckh;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import com.xkcoding.http.util.StringUtil;
import io.swagger.models.auth.In;
import lombok.Value;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.RedisUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 拌合站主表
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Api(tags = "拌合站主表")
@RestController
@RequestMapping("/hntbhz/bhzCementBase")
@Slf4j
public class BhzCementBaseController extends JeecgController<BhzCementBase, IBhzCementBaseService> {
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IShigongphbService phbService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private BhzCementBaseMapper bhzCementBaseMapper;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private IBhzCementDetailStatisticsService bhzCementDetailStatisticsService;
    @Autowired
    private IWzliaocangService stockBinService;
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;

    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IRenwudanScheduleService renwudanScheduleService;

    /**
     * 分页列表查询金华导出
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listbhzyj")
    public Result<?> queryPageListbhzyj(BhzCementBase bhzCementBase,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String format = df.format(day);
        //查询不合格数
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.select("count(over_level) over_level");
        queryWrapper.ne("over_level",0);
        BhzCementBase one = bhzCementBaseService.getOne(queryWrapper);

        //查询闭合数
        QueryWrapper<BhzCementBase> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper1.select("count(overproof_status) overproof_status");
        queryWrapper1.eq("overproof_status",20);
        queryWrapper1.ne("over_level",0);
        BhzCementBase one1 = bhzCementBaseService.getOne(queryWrapper1);

        //查询今天的超标数据
        QueryWrapper<BhzCementBase> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper3.likeRight("product_datetime",format);
        queryWrapper3.ne("over_level",0);
        List<BhzCementBase> list = bhzCementBaseService.list(queryWrapper3);
        for (BhzCementBase l :list){
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(l.getShebeiNo());
            l.setShebeiNo(selectshebeione.getSbname());
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("over",one.getOverLevel());
        map.put("overproof",one1.getOverproofStatus());
        map.put("detail",list);
        return Result.OKs(map);
    }

    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageList(BhzCementBase bhzCementBase,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        } else if (Objects.equals(bhzCementBase.getShebeiNo(), "A05A07A01A01A02A01_BHZ_1223")) {
            bhzCementBase.setProjectName("盐城快速路及基础设施项目");
        } else if (Objects.equals(bhzCementBase.getShebeiNo(), "A05A07A01A01A02A02_BHZ_1173")) {
            bhzCementBase.setProjectName("G204国道阜宁至亭湖段YFTH2标");
        }
        if (StringUtils.isNotBlank(bhzCementBase.getPoureLocation())) {
            bhzCementBase.setPoureLocation("*" + bhzCementBase.getPoureLocation() + "*");
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询金华导出
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listjhdc")
    public Result<?> queryPageListjhdc(BhzCementBase bhzCementBase,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        if (StringUtils.isNotBlank(bhzCementBase.getPoureLocation())) {
            bhzCementBase.setPoureLocation("*" + bhzCementBase.getPoureLocation() + "*");
        }
        //先分组查询
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.select("shebei_no,project_name,poure_location,strength_rank,sum(estimate_number) estimate_number,product_datetime");
        queryWrapper.groupBy("shebei_no,project_name,poure_location");
        List<BhzCementBase> list = bhzCementBaseService.list(queryWrapper);
        List<BhzCementJhdcVO> data = new ArrayList<>();
        if (list.size() > 0){
            for (BhzCementBase l :list){
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(l.getShebeiNo());
                BhzCementBase cementBase = new BhzCementBase();
                cementBase.setShebeiNo(l.getShebeiNo());
                cementBase.setProjectName(l.getProjectName());
                cementBase.setPoureLocation(l.getPoureLocation());
                //查询每组的batchNo
                QueryWrapper<BhzCementBase> queryWrapper2 = QueryGenerator.initQueryWrapper(cementBase, req.getParameterMap());
                List<BhzCementBase> list1 = bhzCementBaseService.list(queryWrapper2);
                List<String> list2 = new ArrayList<>();
                if (list1.size() > 0){
                    for (BhzCementBase l1:list1){
                        list2.add(l1.getBatchNo());
                    }
                }
                //计算材料消耗
                QueryWrapper<BhzCementDetail> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.select("materiale_name,sum(reality_number) reality_number,sum(theory_number) theory_number");
                queryWrapper1.in("batch_no",list2);
                queryWrapper1.groupBy("materiale_name");
                List<BhzCementDetail> list3 = bhzCementDetailService.list(queryWrapper1);
                if (list3.size() > 0){
                    for (BhzCementDetail bhzCementDetail:list3){
                        BhzCementJhdcVO bhzCementJhdcVO = new BhzCementJhdcVO();
                        bhzCementJhdcVO.setShebeiNo(selectshebeione.getSbname());
                        if (l.getProjectName() != null){
                            bhzCementJhdcVO.setProjectName(l.getProjectName());
                        }else {
                            bhzCementJhdcVO.setProjectName("");
                        }
                        bhzCementJhdcVO.setPoureLocation(l.getPoureLocation());
                        bhzCementJhdcVO.setEstimateNumber(l.getEstimateNumber());
                        bhzCementJhdcVO.setStrengthRank(l.getStrengthRank());
                        bhzCementJhdcVO.setProductDatetime(l.getProductDatetime());

                        bhzCementJhdcVO.setMaterialeName(bhzCementDetail.getMaterialeName());
                        bhzCementJhdcVO.setRealityNumber(bhzCementDetail.getRealityNumber());
                        bhzCementJhdcVO.setTheoryNumber(bhzCementDetail.getTheoryNumber());
                        data.add(bhzCementJhdcVO);
                    }
                }
            }
        }
        return Result.OKs(data);
    }

    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listByWorkno")
    public Result<?> listByWorkno(BhzCementBase bhzCementBase,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  String sys_depart_orgcode,
                                  HttpServletRequest req) {
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listByWorkno1")
    public Result<?> listByWorkno1(BhzCementBase bhzCementBase,
                                   String workNos,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if (bhzCementBase.getFormulaNo() == null) {
            if (!"null".equals(workNos)) {
                bhzCementBase.setFormulaNo(workNos);
            } else {
                bhzCementBase.setFormulaNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listrc")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> listrc(BhzCementBase bhzCementBase,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        if (bhzCementBase.getPoureLocation() == null) {
            queryWrapper.ne("poure_location", "临建");
        }
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listrc3")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseListNotUse")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> listrc3(BhzCementBase bhzCementBase,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listCo")
   // @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseListNotUse")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> listCo(BhzCementBase bhzCementBase,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                             HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.select(" count(1) coo");
        queryWrapper.last(
                 " AND renwudanstatus IN ( 28, 30 )  "+ // 查找 未审批或未填报的生产数据
                "    AND product_datetime >= DATE_SUB(CURDATE(), INTERVAL 5 DAY) " +
                "    AND product_datetime < DATE_SUB(CURDATE(), INTERVAL 1 DAY)  ");// -- 2天前到5天前，不包括昨天

        Map<String, Object> map = bhzCementBaseService.getMap(queryWrapper);

        return Result.OK(map);
    }


    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listJh")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> listJh(BhzCementBase bhzCementBase,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format4 = ft.format(new Date());
        bhzCementBase.setIsdel(0);
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.isNotNull("over_reason");
        queryWrapper.last(" and product_datetime like '" + format4 + "%'");
        List<BhzCementBase> list = bhzCementBaseService.list(queryWrapper);
        if (list.size() == 0) {
            bhzCementBase.setIssend(0);
            list.add(bhzCementBase);
            return Result.OK(list);
        }
        for (BhzCementBase l : list) {
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(l.getShebeiNo());
            l.setShebeiNo(sbjwd.getSbname());
            l.setIssend(list.size());
        }
        return Result.OK(list);
    }

    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listtj")
//    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageListtj(BhzCementStatistics bhzCementStatistics,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select(" ifnull(sum(all_dish),0) shengchanpan, " +
                " ifnull(sum(estimate_number),0) fangliang, " +
                " ifnull(sum(primary_dish),0) chu, " +
                " ifnull(sum(middle_dish),0) zhong, " +
                " ifnull(sum(advanced_dish),0) gao");

        Map<String, Object> map = bhzCementStatisticsService.getMap(queryWrapper);

        return Result.OK(map);
    }


    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/listtjgbqddj")
    public Result<?> listtjgbqddj(BhzCementBase bhzCementBase,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.select("strength_rank,count(1) AS panhao,\n" +
                        "    SUM(estimate_number) AS estimate_number,\n" +
                        "    (SELECT COUNT(1) \n" +
                        "     FROM bhz_cement_base b2 \n" +
                        "     WHERE b2.batch_no = bhz_cement_base.batch_no AND b2.over_level = 1) AS overLevel,\n" +
                        "\t\t \n" +
                        "    (SELECT COUNT(1) \n" +
                        "     FROM bhz_cement_base b2 \n" +
                        "     WHERE b2.batch_no = bhz_cement_base.batch_no AND b2.over_level = 2) AS alertstate,\n" +
                        "\t\t \n" +
                        "    (SELECT COUNT(1) \n" +
                        "     FROM bhz_cement_base b2 \n" +
                        "     WHERE b2.batch_no = bhz_cement_base.batch_no AND b2.over_level = 3) AS timeOverLevel")
                .groupBy("strength_rank");

        List<BhzCementBase> list = bhzCementBaseService.list(queryWrapper);
        BigDecimal estimateNumber = BigDecimal.ZERO;
        for (BhzCementBase cementBase : list) {
            if (cementBase.getEstimateNumber() != null) {
                // 将 double 类型的值转换为 BigDecimal 并进行累加
                BigDecimal currentEstimate = BigDecimal.valueOf(cementBase.getEstimateNumber());
                estimateNumber = estimateNumber.add(currentEstimate);
            }
        }
        Map map = new HashMap();
        map.put("estimateNumber",estimateNumber);
        map.put("list",list);
        return Result.OK(map);
    }


    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-超标分页列表查询")
    @ApiOperation(value = "拌合站主表-超标分页列表查询", notes = "拌合站主表-超标分页列表查询")
    @GetMapping(value = "/list1")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseCBList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageList1(BhzCementBase bhzCementBase,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-超标分页列表查询带处置审核信息")
    @ApiOperation(value = "拌合站主表-超标分页列表带处置审核信息查询", notes = "拌合站主表-超标分页列表带处置审核信息查询")
    @GetMapping(value = "/list2")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseCBList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageList2(BhzCementBase bhzCementBase,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        List<Object> records = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        bhzCementBase.setIsdel(0);
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.gt("over_level", 0);
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        double max = 0.0;
        for (BhzCementBase record : pageList.getRecords()) {
            BhzCementBasePage bhzCementBasePage = new BhzCementBasePage();
            BeanUtils.copyProperties(record, bhzCementBasePage);
            String batchNo = record.getBatchNo();
            List<BhzCementDetail> details = bhzCementDetailService.selectcementlist(batchNo);
            if (details.size() > 0) {
                bhzCementBasePage.setBhzCementDetailList(details);
                max = 0.0;
                for (BhzCementDetail bhzCementDetail : details) {
                    if (bhzCementDetail.getMaterialeOverLevel() > 0) {
                        double abs = Math.abs(bhzCementDetail.getOverValue());
                        max = max > abs ? max : bhzCementDetail.getOverValue();
                    }
                }
                bhzCementBasePage.setBhzCementDetailList(details);
            }
            bhzCementBasePage.setAdditiveVariety(max + "%");
            ShebeiInfo device = shebeiInfoService.selectshebeione(record.getShebeiNo());
            if (device != null) {
                bhzCementBasePage.setShebeiNo(device.getSbname());
            }
            BhzCementOverHandler list = bhzCementOverHandlerService.selectlist(batchNo);
            if (list == null) {
                BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                bhzCementOverHandler.setOverproofStatus(0);
                bhzCementBasePage.setBhzCementOverHandler(bhzCementOverHandler);
            } else {
                bhzCementBasePage.setBhzCementOverHandler(list);
            }
            records.add(bhzCementBasePage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records);
        return Result.OK(map);
    }


    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-超标分页列表查询带处置审核信息")
    @ApiOperation(value = "拌合站主表-超标分页列表带处置审核信息查询", notes = "拌合站主表-超标分页列表带处置审核信息查询")
    @GetMapping(value = "/listCall")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseCallList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageListCall(BhzCementBase bhzCementBase,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        List<Object> records = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        bhzCementBase.setIsdel(0);
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.gt("over_level", 0);
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        double max = 0.0;
        for (BhzCementBase record : pageList.getRecords()) {
            BhzCementBaseCallPage bhzCementBaseCallPage = new BhzCementBaseCallPage();
            BeanUtils.copyProperties(record, bhzCementBaseCallPage);
            String batchNo1 = record.getBatchNo();
            String sbjno = record.getShebeiNo();
            List<BhzCementDetail> details = bhzCementDetailService.selectcementlist(batchNo1);
            if (details.size() > 0) {
                bhzCementBaseCallPage.setBhzCementDetailList(details);
                max = 0.0;
                for (BhzCementDetail bhzCementDetail : details) {
                    if (bhzCementDetail.getMaterialeOverLevel() > 0) {
                        double abs = Math.abs(bhzCementDetail.getOverValue());
                        max = max > abs ? max : bhzCementDetail.getOverValue();
                    }
                }
                bhzCementBaseCallPage.setBhzCementDetailList(details);
            }
            bhzCementBaseCallPage.setAdditiveVariety(max + "%");
            ShebeiInfo device = shebeiInfoService.selectshebeione(record.getShebeiNo());
            if (device != null) {
                bhzCementBaseCallPage.setShebeiNo(device.getSbname());
            }
            BhzCallCfg selectbhzcallone = bhzCallCfgService.selectbhzcallone(sbjno);
            BhzCementOverHandler list = bhzCementOverHandlerService.selectlist(batchNo1);
            if (list == null || selectbhzcallone == null) {
                BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                bhzCementOverHandler.setOverproofStatus(0);
                BhzCallCfg bhzCallCfg = new BhzCallCfg();
                bhzCementBaseCallPage.setBhzCallCfg(bhzCallCfg);
                bhzCementBaseCallPage.setBhzCementOverHandler(bhzCementOverHandler);
            } else {
                bhzCementBaseCallPage.setBhzCallCfg(selectbhzcallone);
                bhzCementBaseCallPage.setBhzCementOverHandler(list);
            }
            records.add(bhzCementBaseCallPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records);
        return Result.OK(map);
    }

    @AutoLog(value = "拌合站主表-超标分页列表查询带处置审核信息")
    @ApiOperation(value = "拌合站主表-超标分页列表带处置审核信息查询", notes = "拌合站主表-超标分页列表带处置审核信息查询")
    @GetMapping(value = "/list22")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseCBList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageList22(BhzCementBase bhzCementBase,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        bhzCementBase.setIsdel(0);
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.gt("over_level", 0);
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        for (BhzCementBase record : pageList.getRecords()) {
            BhzCementBasePageNew bhzCementBasePage = new BhzCementBasePageNew();
            BeanUtils.copyProperties(record, bhzCementBasePage);
            String batchNo = record.getBatchNo();
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(batchNo);
            if (selectcementlist.size() > 0) {
                bhzCementBasePage.setBhzCementDetailList(selectcementlist);
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeiNo());
            if (selectshebeione != null) {
                bhzCementBasePage.setShebeiNo(selectshebeione.getSbname());
            }
            Page<BhzCementOverHandler> selectlist = bhzCementOverHandlerService.selectlist1(batchNo);
            if (selectlist != null) {
//                Page<BhzCementOverHandler> bhzCementOverHandlers = new Page<>();
//                BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
//                bhzCementOverHandler.setOverproofStatus(0);
//                List<BhzCementOverHandler> ts = new ArrayList<BhzCementOverHandler>();
//                ts.add(bhzCementOverHandler);
//                bhzCementOverHandlers.setRecords(ts);
//                bhzCementBasePage.setBhzCementOverHandler(bhzCementOverHandlers);
                bhzCementBasePage.setBhzCementOverHandler(selectlist);
            }
            records1.add(bhzCementBasePage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }


    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-超标分页列表查询带处置审核信息")
    @ApiOperation(value = "拌合站主表-超标分页列表带处置审核信息查询", notes = "拌合站主表-超标分页列表带处置审核信息查询")
    @GetMapping(value = "/listrc2")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseCBList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> listrc2(BhzCementBase bhzCementBase,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                             HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        if (bhzCementBase.getPoureLocation() == null) {
            queryWrapper.ne("poure_location", "临建");
        }
        queryWrapper.gt("over_level", 0);
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        for (BhzCementBase record : pageList.getRecords()) {
            BhzCementBasePage bhzCementBasePage = new BhzCementBasePage();
            BeanUtils.copyProperties(record, bhzCementBasePage);
            String batchNo = record.getBatchNo();
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(batchNo);
            if (selectcementlist.size() > 0) {
                bhzCementBasePage.setBhzCementDetailList(selectcementlist);
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeiNo());
            if (selectshebeione != null) {
                bhzCementBasePage.setShebeiNo(selectshebeione.getSbname());
            }
            BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(batchNo);
            if (selectlist == null) {
                BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                bhzCementOverHandler.setOverproofStatus(0);
                bhzCementBasePage.setBhzCementOverHandler(bhzCementOverHandler);
            } else {
                bhzCementBasePage.setBhzCementOverHandler(selectlist);
            }
            records1.add(bhzCementBasePage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 未处理数据分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土主表-未处理数据分页列表查询")
    @ApiOperation(value = "混凝土主表-未处理数据分页列表查询", notes = "混凝土主表-未处理数据分页列表查询")
    @GetMapping(value = "/list12")
    public Result<?> queryPageList12(BhzCementBase bhzCementBase,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.gt("over_level", 0);
        queryWrapper.lt("overproof_status", 20);
        Page<BhzCementBase> page = new Page<>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 超标数据分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土主表-超标数据分页列表查询")
    @ApiOperation(value = "混凝土主表-超标数据分页列表查询", notes = "混凝土主表-超标数据分页列表查询")
    @GetMapping(value = "/chaobiaolist")
    public Result<?> queryPageListchaobiao(BhzCementBase bhzCementBase,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.gt("over_level", 0);
        Page<BhzCementBase> page = new Page<>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-超标分页列表查询带处置审核信息")
    @ApiOperation(value = "拌合站主表-超标分页列表带处置审核信息查询", notes = "拌合站主表-超标分页列表带处置审核信息查询")
    @GetMapping(value = "/listczsh")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzHntCbCxtBaseList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageListlistczsh(BhzCementBase bhzCementBase,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                           HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        bhzCementBase.setIsdel(0);
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.gt("over_level", 0);
        queryWrapper.ne("overproof_status", 20);
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        List<BhzCementBase> records = pageList.getRecords();
        List records1 = new ArrayList<>();
        Double max = 0.0;
        for (BhzCementBase record : records) {
            BhzCementBasePage bhzCementBasePage = new BhzCementBasePage();
            BeanUtils.copyProperties(record, bhzCementBasePage);
            String batchNo = record.getBatchNo();
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(batchNo);
            if (selectcementlist.size() > 0) {
                max = 0.0;
                try {
                    for (BhzCementDetail bhzCementDetail : selectcementlist) {
                        if (bhzCementDetail.getMaterialeOverLevel() > 0) {
                            double abs = Math.abs(bhzCementDetail.getOverValue());
                            max = max > abs ? max : bhzCementDetail.getOverValue();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bhzCementBasePage.setBhzCementDetailList(selectcementlist);
            }
//            if (max == 0.0) {
//                continue;
//            }
            bhzCementBasePage.setAdditiveVariety(max + "%");
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(record.getShebeiNo());
            if (selectshebeione != null) {
                bhzCementBasePage.setShebeiNo(selectshebeione.getSbname());
            }
            BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(batchNo);
            if (selectlist == null) {
                BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                bhzCementOverHandler.setOverproofStatus(0);
                bhzCementBasePage.setBhzCementOverHandler(bhzCementOverHandler);
            } else {
                bhzCementBasePage.setBhzCementOverHandler(selectlist);
            }
            records1.add(bhzCementBasePage);
        }
        Map map = new HashMap<>();
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-用量查询")
    @ApiOperation(value = "拌合站主表-用量查询", notes = "拌合站主表-用量查询")
    @GetMapping(value = "/list3")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseYLList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageList3(BhzCementBase bhzCementBase,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        List<BhzCementBase> records = pageList.getRecords();
        List records1 = new ArrayList<>();
        Map map = new HashMap();
        for (BhzCementBase record : records) {
            BhzCementBasePage bhzCementBasePage = new BhzCementBasePage();
            String batchNo = record.getBatchNo();
            Integer id = record.getId();
            Integer overlevel = record.getOverLevel();
            String poureLocation = record.getPoureLocation();
            Date productDatetime = record.getProductDatetime();
            String projectName = record.getProjectName();
            String shebeiNo = record.getShebeiNo();
            String jobLocation = record.getJobLocation();
            String strengthRank = record.getStrengthRank();
            Double estimateNumber = record.getEstimateNumber();
            String slump = record.getSlump();
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(batchNo);
            if (selectcementlist.size() > 0) {
                bhzCementBasePage.setBhzCementDetailList(selectcementlist);
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            bhzCementBasePage.setShebeiNo(selectshebeione.getSbname());
            bhzCementBasePage.setOverLevel(overlevel);
            bhzCementBasePage.setPoureLocation(poureLocation);
            bhzCementBasePage.setProductDatetime(productDatetime);
            bhzCementBasePage.setProjectName(projectName);
            bhzCementBasePage.setJobLocation(jobLocation);
            bhzCementBasePage.setStrengthRank(strengthRank);
            bhzCementBasePage.setEstimateNumber(estimateNumber);
            bhzCementBasePage.setSlump(slump);
            bhzCementBasePage.setId(id);
            bhzCementBasePage.setBatchNo(batchNo);
            records1.add(bhzCementBasePage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 中铁一局材料消耗
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-用量查询")
    @ApiOperation(value = "拌合站主表-用量查询", notes = "拌合站主表-用量查询")
    @GetMapping(value = "/listclxh")
    public Result<?> queryPageListclxh(BhzCementBase bhzCementBase,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req, String productDatetime_begin, String productDatetime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] bytes = shebei.split(",");
        List<String> strings = new ArrayList<>(bytes.length);
        Collections.addAll(strings, bytes);
        System.out.println(strings);

        if(productDatetime_begin == null && productDatetime_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            productDatetime_begin = f.format(calendar.getTime());
            productDatetime_end = f.format(date.getTime());
        }
        List<BhzCementBase>  bhzCementBases = bhzCementBaseService.selecerenwudanpsgx(strings,productDatetime_begin,productDatetime_end);
        List<BhzCementBaseZtVO>  bhzCementBaseZtVOs = new ArrayList<>();
        if (bhzCementBases.size() > 0){
            for (BhzCementBase cementBase :bhzCementBases){
                BhzCementBaseZtVO bhzCementBaseZtVO = new BhzCementBaseZtVO();
                bhzCementBaseZtVO.setId(cementBase.getId());
                ShebeiInfo sbjwd = shebeiInfoService.SBJWD(cementBase.getShebeiNo());
                bhzCementBaseZtVO.setShebeiNo(sbjwd.getSbname());
                bhzCementBaseZtVO.setEstimateNumber(cementBase.getEstimateNumber());
                //单独设备在这段时间的方量
                List<String> stringList = bhzCementBaseService.selecerenw(cementBase.getShebeiNo(),productDatetime_begin,productDatetime_end);
                if (stringList.size() > 0) {
                    QueryWrapper<BhzCementDetail> queryWrapper = new QueryWrapper<>();
                    queryWrapper.select("materiale_type,materiale_id,materiale_name,sum(reality_number) reality_number,sum(theory_number) theory_number,error_value");
                    queryWrapper.in("batch_no",stringList);
                    queryWrapper.groupBy("materiale_type");
                    List<BhzCementDetail> list = bhzCementDetailService.list(queryWrapper);
                    System.out.println(list);
                    if (list.size() > 0){
                        for (BhzCementDetail l :list){
                            if (l.getMaterialeName().contains("10-20mm碎石")){
                                bhzCementBaseZtVO.setRealitysesuis(l.getRealityNumber());
                                bhzCementBaseZtVO.setTheorysesuis(l.getTheoryNumber());
                            }else if (l.getMaterialeName().contains("5-10mm碎石")){
                                bhzCementBaseZtVO.setRealitywssuis(l.getRealityNumber());
                                bhzCementBaseZtVO.setTheorywssuis(l.getTheoryNumber());
                            }else if (l.getMaterialeName().contains("河沙")){
                                bhzCementBaseZtVO.setRealityhesha(l.getRealityNumber());
                                bhzCementBaseZtVO.setTheoryhesha(l.getTheoryNumber());
                            }else if (l.getMaterialeName().contains("水泥")){
                                bhzCementBaseZtVO.setRealityshuini(l.getRealityNumber());
                                bhzCementBaseZtVO.setTheoryshuini(l.getTheoryNumber());
                            }else if (l.getMaterialeName().contains("粉煤灰")){
                                bhzCementBaseZtVO.setRealityfenmeihui(l.getRealityNumber());
                                bhzCementBaseZtVO.setTheoryfenmeihui(l.getTheoryNumber());
                            }else if (l.getMaterialeName().contains("减水剂")){
                                bhzCementBaseZtVO.setRealityjianshuiji(l.getRealityNumber());
                                bhzCementBaseZtVO.setTheoryjianshuiji(l.getTheoryNumber());
                            }else {
                                bhzCementBaseZtVO.setRealityshui(l.getRealityNumber());
                                bhzCementBaseZtVO.setTheoryshui(l.getTheoryNumber());
                            }
                        }
                    }
                }
                bhzCementBaseZtVOs.add(bhzCementBaseZtVO);
            }
        }else {
            return Result.error("该时间段内，没有生产方量！！！");
        }
        return Result.OK(bhzCementBaseZtVOs);
    }

    /**
     * 中铁一局材料消耗导出
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-用量查询")
    @ApiOperation(value = "拌合站主表-用量查询", notes = "拌合站主表-用量查询")
    @GetMapping(value = "/listclxhdc")
    public Result<?> queryPageListclxhdc(BhzCementBase bhzCementBase,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req, String productDatetime_begin, String productDatetime_end) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] bytes = shebei.split(",");
        List<String> strings = new ArrayList<>(bytes.length);
        Collections.addAll(strings, bytes);
        System.out.println(strings);

        if(productDatetime_begin == null && productDatetime_end == null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            productDatetime_begin = f.format(calendar.getTime());
            productDatetime_end = f.format(date.getTime());
        }
        ArrayList<BhzCementBaseZtVO> data = new ArrayList<>();
        BhzCementBaseZtVO bhzCementBaseZtVO = new BhzCementBaseZtVO();
        List<String> stringList = bhzCementBaseService.selecerenws(strings,productDatetime_begin,productDatetime_end);
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df1=new SimpleDateFormat("yyyy年MM月dd日");
        if (stringList.size() > 0) {
            QueryWrapper<BhzCementDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("materiale_type,materiale_id,materiale_name,sum(reality_number) reality_number,sum(theory_number) theory_number,error_value");
            queryWrapper.in("batch_no",stringList);
            queryWrapper.groupBy("materiale_type");
            List<BhzCementDetail> list = bhzCementDetailService.list(queryWrapper);
            System.out.println(list);
            if (list.size() > 0){
                for (BhzCementDetail l :list){
                    if (l.getMaterialeName().contains("10-20mm碎石")){
                        bhzCementBaseZtVO.setRealitysesuis(l.getRealityNumber());
                        bhzCementBaseZtVO.setTheorysesuis(l.getTheoryNumber());
                    }else if (l.getMaterialeName().contains("5-10mm碎石")){
                        bhzCementBaseZtVO.setRealitywssuis(l.getRealityNumber());
                        bhzCementBaseZtVO.setTheorywssuis(l.getTheoryNumber());
                    }else if (l.getMaterialeName().contains("河沙")){
                        bhzCementBaseZtVO.setRealityhesha(l.getRealityNumber());
                        bhzCementBaseZtVO.setTheoryhesha(l.getTheoryNumber());
                    }else if (l.getMaterialeName().contains("水泥")){
                        bhzCementBaseZtVO.setRealityshuini(l.getRealityNumber());
                        bhzCementBaseZtVO.setTheoryshuini(l.getTheoryNumber());
                    }else if (l.getMaterialeName().contains("粉煤灰")){
                        bhzCementBaseZtVO.setRealityfenmeihui(l.getRealityNumber());
                        bhzCementBaseZtVO.setTheoryfenmeihui(l.getTheoryNumber());
                    }else if (l.getMaterialeName().contains("减水剂")){
                        bhzCementBaseZtVO.setRealityjianshuiji(l.getRealityNumber());
                        bhzCementBaseZtVO.setTheoryjianshuiji(l.getTheoryNumber());
                    }else {
                        bhzCementBaseZtVO.setRealityshui(l.getRealityNumber());
                        bhzCementBaseZtVO.setTheoryshui(l.getTheoryNumber());
                    }
                }
            }
        }
        bhzCementBaseZtVO.setProductDatetime_begin(df.parse(productDatetime_begin));
        bhzCementBaseZtVO.setProductDatetime_end(df.parse(productDatetime_end));
        String format = df1.format(bhzCementBaseZtVO.getProductDatetime_begin());
        String format1 = df1.format(bhzCementBaseZtVO.getProductDatetime_end());
        bhzCementBaseZtVO.setTimepj(format+"至"+format1);
        bhzCementBaseZtVO.setTheoryt1(0.0);
        bhzCementBaseZtVO.setTheoryt2(0.0);
        bhzCementBaseZtVO.setTheoryt3(0.0);
        bhzCementBaseZtVO.setTheoryt4(0.0);
        bhzCementBaseZtVO.setRealityt1(0.0);
        bhzCementBaseZtVO.setRealityt2(0.0);
        bhzCementBaseZtVO.setRealityt3(0.0);
        bhzCementBaseZtVO.setRealityt4(0.0);
        data.add(bhzCementBaseZtVO);
        return Result.OKs(data);
    }
    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-用量查询")
    @ApiOperation(value = "拌合站主表-用量查询", notes = "拌合站主表-用量查询")
    @GetMapping(value = "/list3car")
    public Result<?> queryPageList3Car(BhzCementBase bhzCementBase,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req) {

        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        List<BhzCementBase> records = pageList.getRecords();
        List records1 = new ArrayList<>();
        Map map = new HashMap();
        for (BhzCementBase record : records) {
            BhzCementBaseCarPage bhzCementBasePage = new BhzCementBaseCarPage();
            String batchNo = record.getBatchNo();
            Integer id = record.getId();
            Integer overlevel = record.getOverLevel();
            String poureLocation = record.getPoureLocation();
            Date productDatetime = record.getProductDatetime();
            String projectName = record.getProjectName();

            String jobLocation = record.getJobLocation();
            String strengthRank = record.getStrengthRank();
            Double estimateNumber = record.getEstimateNumber();
            String slump = record.getSlump();
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(batchNo);
            if (selectcementlist.size() > 0) {
                bhzCementBasePage.setBhzCementDetailList(selectcementlist);
            }
            // 发车单内暂时不需要设备名称
//            String shebeiNo = record.getShebeiNo();
//            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
//            bhzCementBasePage.setShebeiNo(selectshebeione.getSbname());
            bhzCementBasePage.setOverLevel(overlevel);
            bhzCementBasePage.setPoureLocation(poureLocation);
            bhzCementBasePage.setProductDatetime(productDatetime);
            bhzCementBasePage.setProjectName(projectName);
            bhzCementBasePage.setJobLocation(jobLocation);
            bhzCementBasePage.setStrengthRank(strengthRank);
            bhzCementBasePage.setEstimateNumber(estimateNumber);
            bhzCementBasePage.setSlump(slump);
            bhzCementBasePage.setId(id);
            bhzCementBasePage.setBatchNo(batchNo);
            records1.add(bhzCementBasePage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }


    /**
     * 混凝土拌合站首页统计总数以及合格超标数据以及本月超标率
     *
     * @param bhzCementStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站首页统计")
    @ApiOperation(value = "混凝土拌合站首页统计", notes = "混凝土拌合站首页统计")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList5(BhzCementStatistics bhzCementStatistics, BhzCementBase bhzCementBase, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
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
//        try {
//            parse = format.parse(day1);
//            parse1 = format.parse(day2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzCementStatistics.setShebeiNo(shebei);
        } else {
            bhzCementStatistics.setShebeiNo("空");
        }
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzCementBase.setShebeiNo(shebei);
        } else {
            bhzCementBase.setShebeiNo("空");
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_dish) as all_dish", "sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper.ge("statistics_time", day1);
        queryWrapper.le("statistics_time", day2);
        BhzCementStatistics bhzCementStatisticsList = bhzCementStatisticsService.getOne(queryWrapper);
        QueryWrapper<BhzCementStatistics> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper1.select("sum(all_dish) as all_dish", "sum(all_overproof_dish) as all_overproof_dish");
        BhzCementStatistics bhzCementStatisticsList1 = bhzCementStatisticsService.getOne(queryWrapper1);
        QueryWrapper<BhzCementBase> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper2.select("count(*) as id");
        queryWrapper2.eq("overproof_status", 20);
        queryWrapper2.ge("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day1);
        queryWrapper2.le("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day2);
        BhzCementBase one = bhzCementBaseService.getOne(queryWrapper2);
        Double hntsum = 0.0;
        Double hntcb = 0.0;
        Double hntcblv = 0.0;
        Double hntysum = 0.0;
        Double hntycb = 0.0;
        Double hntycblv = 0.0;
        double hntbhY = 0.0;
        double hntbhlvY = 0.0;
        if (bhzCementStatisticsList != null) {
            hntysum = hntysum + bhzCementStatisticsList.getAllDish();
            hntycb = hntycb + bhzCementStatisticsList.getAllOverproofDish();
        }
        if (bhzCementStatisticsList1 != null) {
            hntsum = hntsum + bhzCementStatisticsList1.getAllDish();
            hntcb = hntcb + bhzCementStatisticsList1.getAllOverproofDish();
        }
        if (one != null) {
            hntbhY = one.getId();
        }
        Double huncblv = (hntcb / hntsum) * 100;//总的超标率
        Double hunylv = (hntycb / hntysum) * 100;//当前月的超标率
        if (hntycb != 0) {
            hntbhlvY = (hntbhY / hntycb) * 100;//当前月闭合率
        }
        if (huncblv > 0) {
            BigDecimal b = new BigDecimal(huncblv);
            hntcblv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if (hunylv > 0) {
            BigDecimal b1 = new BigDecimal(hunylv);
            hntycblv = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        map.put("hntcblv", hntcblv);
        map.put("hntsum", hntsum);
        map.put("hntcb", hntcb);
        map.put("hntycblv", hntycblv);
        map.put("hntbhlvY", hntbhlvY);
        return Result.OK(map);
    }

    /**
     * 混凝土拌合站首页中间部分月统计
     *
     * @param bhzCementStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站首页中间部分月统计")
    @ApiOperation(value = "混凝土拌合站首页中间部分月统计", notes = "混凝土拌合站首页中间部分月统计")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList6(BhzCementStatistics bhzCementStatistics, HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzCementStatistics.setShebeiNo(shebei);
        } else {
            bhzCementStatistics.setShebeiNo("空");
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_dish) as all_dish", "statistics_time");
        if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
            try {
                Date parse = ft.parse(statisticsTime_begin);
                Date parse1 = ft.parse(statisticsTime_end);
                queryWrapper.ge("statistics_time", parse);
                queryWrapper.le("statistics_time", parse1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            queryWrapper.last(" GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
        } else {
            queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
        }

        List<BhzCementStatistics> bhzCementStatisticsList = bhzCementStatisticsService.list(queryWrapper);
        List list = new ArrayList();
        for (BhzCementStatistics statistics : bhzCementStatisticsList) {
            Map map = new HashMap();
            Date statisticsTime = statistics.getStatisticsTime();
            Integer allDish = statistics.getAllDish();
            String format3 = format.format(statisticsTime);
            map.put("statisticsTime", format3);
            map.put("allDish", allDish);
            list.add(map);
        }
        return Result.OK(list);
    }


    /**
     * 混凝土拌合站统计图
     *
     * @param bhzCementStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-超标盘数/方量", notes = "混凝土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList7(BhzCementStatistics bhzCementStatistics, HttpServletRequest req,
                                    Integer date) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
        if (bhzCementStatistics.getShebeiNo() != null) {
            queryWrapper.in("shebei_no", bhzCementStatistics.getShebeiNo());
        } else {
            queryWrapper.in("shebei_no", shebeilist);
        }
        if (date != null) {
            if (date == 0) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
                queryWrapper.last(" GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y'))");
            } else if (date == 1) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1 as istongji");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1)");
            } else if (date == 2) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
            } else if (date == 3) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "DATE_FORMAT(statistics_time,'第%u周') as project_name");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y%u'))");
            } else if (date == 4) {
                // 按天统计
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "DATE_FORMAT(statistics_time,'%Y-%m-%d') as project_name");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY DATE_FORMAT(statistics_time,'%Y-%m-%d')");
            }
        } else {
            queryWrapper.orderByDesc("statistics_time");
            queryWrapper.last("limit 7");
            queryWrapper.groupBy("statistics_time", "statistics_time");
        }
        List<BhzCementStatistics> bhzCementStatisticsList = bhzCementStatisticsService.list(queryWrapper);
        List list = new ArrayList();
        for (BhzCementStatistics statistics : bhzCementStatisticsList) {
            Map map = new HashMap();
            String format3 = "";
            Date statisticsTime = statistics.getStatisticsTime();
            Integer allDish = statistics.getAllDish();
            Integer allDallOverproofDishish = statistics.getAllOverproofDish();
            Integer primaryDish = statistics.getPrimaryDish();
            Integer middleDish = statistics.getMiddleDish();
            Integer advancedDish = statistics.getAdvancedDish();
            Double estimateNumber = statistics.getEstimateNumber();
            Integer hegeDish = allDish - allDallOverproofDishish;
            if (date != null) {
                if (date == 0) {
                    format3 = format1.format(statisticsTime);
                } else if (date == 1) {
                    format3 = String.valueOf(statistics.getIstongji());
                } else if (date == 3) {
                    format3 = statistics.getProjectName();
                }else if (date == 4) {
                    format3 = statistics.getProjectName();
                } else {
                    format3 = format.format(statisticsTime);
                }
            } else {
                format3 = ft.format(statisticsTime);
            }
            map.put("statisticsTime", format3);
            map.put("primaryDish", primaryDish);
            map.put("middleDish", middleDish);
            map.put("advancedDish", advancedDish);
            map.put("estimateNumber", estimateNumber);
            map.put("hegeDish", hegeDish);
            map.put("date", date);
            list.add(map);
        }
        return Result.OK(list);
    }


    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-合格率", notes = "混凝土拌合站统计-合格率")
    @GetMapping(value = "/list7")
    public Result<?> queryPageList8(BhzCementStatistics bhzCementStatistics, HttpServletRequest req, Integer date) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM");
        String format2 = format1.format(new Date());
        String format3 = ft1.format(new Date());
        String format4 = ft.format(new Date());
        Map map = new HashMap();
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
        if (bhzCementStatistics.getShebeiNo() != null) {
            queryWrapper.in("shebei_no", bhzCementStatistics.getShebeiNo());
        } else {
            queryWrapper.in("shebei_no", shebeilist);
        }
        queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number");
        if (date != null) {
            if (date == 0) {//当前年
                queryWrapper.last("and statistics_time like '" + format2 + "%'");
            } else if (date == 1) {//当前季
                queryWrapper.last("and QUARTER(statistics_time) = QUARTER(now())");
            } else if (date == 2) {//当前月
                queryWrapper.last("and statistics_time like '" + format3 + "%'");
            } else if (date == 3) {//当前周
                queryWrapper.last("and YEARWEEK(date_format(statistics_time,'%Y-%m-%d')) = YEARWEEK(now())");
            }
        } else {//当天
            queryWrapper.last("and statistics_time like '" + format4 + "%'");
        }
        List<BhzCementStatistics> bhzCementStatisticsList = bhzCementStatisticsService.list(queryWrapper);
        int hntsum = 0;
        int hntcb = 0;
        int prisum = 0;
        int midsum = 0;
        int advsum = 0;
        int hegesum = 0;
        for (BhzCementStatistics statistics : bhzCementStatisticsList) {
            if (statistics != null) {
                prisum = statistics.getPrimaryDish();//初级超标总盘数
                midsum = statistics.getMiddleDish();//中级超标总盘数
                advsum = statistics.getAdvancedDish();//高级超标总盘数
                hntsum = statistics.getAllDish();//总盘数
                hntcb = statistics.getAllOverproofDish();//超标总盘数
                hegesum = hntsum - hntcb;//合格总盘数
            }
        }
        map.put("prisum", prisum);
        map.put("midsum", midsum);
        map.put("advsum", advsum);
        map.put("hegesum", hegesum);
        map.put("date", date);
        return Result.OK(map);
    }

    /**
     * 沥青拌合站首页中间部分月产能统计
     *
     * @param statistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "砼拌合站首页月产能统计")
    @ApiOperation(value = "砼拌合站首页月产能统计", notes = "砼拌合站首页月产能统计")
    @GetMapping(value = "/list11")
    public Result<?> queryPageList11(BhzCementStatistics statistics, HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String currentYear = format1.format(new Date());
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            statistics.setShebeiNo(shebei);
        } else {
            statistics.setShebeiNo("空");
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(statistics, req.getParameterMap());
        queryWrapper.select(
                "ifnull(sum(all_dish),0) as all_dish", "ifnull(sum(primary_dish),0) as primary_dish",
                "ifnull(sum(middle_dish),0) as middle_dish", "ifnull(sum(advanced_dish),0) as advanced_dish",
                "ifnull(sum(estimate_number),0) as estimate_number", "ifnull(sum(all_overproof_dish),0) as all_overproof_dish",
                "statistics_time");
        if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
            try {
                Date parse = ft.parse(statisticsTime_begin);
                Date parse1 = ft.parse(statisticsTime_end);
                queryWrapper.ge("statistics_time", parse);
                queryWrapper.le("statistics_time", parse1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        queryWrapper.last("and statistics_time like '" + currentYear + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
        List<BhzCementStatistics> bhzCementStatisticsList = bhzCementStatisticsService.list(queryWrapper);
        List list = new ArrayList<>();

        for (BhzCementStatistics s : bhzCementStatisticsList) {
            Map map = new HashMap<>();
            Date statisticsTime = s.getStatisticsTime();
            double allDish = s.getAllDish();
            double primaryDish = s.getPrimaryDish();
            double middleDish = s.getMiddleDish();
            double advancedDish = s.getAdvancedDish();
            double allOverproofDish = s.getAllOverproofDish();
            double hegeDish = allDish - allOverproofDish;
            double estimateNumber = s.getEstimateNumber();
            double primarylv = Double.parseDouble(String.format("%.2f", (primaryDish / allDish) * 100));
            double middlelv = Double.parseDouble(String.format("%.2f", (middleDish / allDish) * 100));
            double advancedlv = Double.parseDouble(String.format("%.2f", (advancedDish / allDish) * 100));
            double hegelv = Double.parseDouble(String.format("%.2f", (hegeDish / allDish) * 100));
            String format3 = format.format(statisticsTime);
            map.put("statisticsTime", format3);
            map.put("estimateNumber", estimateNumber);
            map.put("primarylv", primarylv);
            map.put("middlelv", middlelv);
            map.put("advancedlv", advancedlv);
            map.put("hegelv", hegelv);
            list.add(map);
        }
        return Result.OK(list);
    }

    /**
     * 砼拌合站首页统计本月超标率/合格率/处置率/中间部分饼图
     *
     * @param bhzCementStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "沥青拌合站首页统计本月超标率/合格率/处置率/中间部分饼图")
    @ApiOperation(value = "沥青拌合站首页统计本月超标率/合格率/处置率/中间部分饼图", notes = "沥青拌合站首页统计本月超标率/合格率/处置率/中间部分饼图")
    @GetMapping(value = "/pieChart")
    public Result<?> queryPagePieChart(BhzCementStatistics bhzCementStatistics, HttpServletRequest req,
                                       String statisticsTime_begin, String statisticsTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String device = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
        String firstDay = format.format(c.getTime());
        Date parseFirstDay = null;//本月第一天
        Date parseLastDay = null;//本月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastDay = format.format(ca.getTime());
//        try {
//            parseFirstDay = format.parse(firstDay);
//            parseLastDay = format.parse(lastDay);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //查询到他的设备编号
        if (device != null) {
            bhzCementStatistics.setShebeiNo(device);
        } else {
            bhzCementStatistics.setShebeiNo("0");
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("ifnull(sum(primary_dish),0) as primary_dish,ifnull(sum(middle_dish),0) as middle_dish," +
                "ifnull(sum(advanced_dish),0) as advanced_dish,ifnull(sum(all_dish),0) as all_dish," +
                "ifnull(sum(all_handle_dish),0) as all_handle_dish,ifnull(sum(all_overproof_dish),0) as all_overproof_dish," +
                "ifnull(sum(primary_handle),0) as primary_handle,ifnull(sum(middle_handle),0) as middle_handle," +
                "ifnull(sum(advanced_handle),0) as advanced_handle");
        if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
            try {
                Date parse2 = format.parse(statisticsTime_begin);
                Date parse3 = format.parse(statisticsTime_end);
                queryWrapper.ge("statistics_time", parse2);
                queryWrapper.le("statistics_time", parse3);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            queryWrapper.ge("statistics_time", firstDay);
            queryWrapper.le("statistics_time", lastDay);
        }
        List<BhzCementStatistics> list = bhzCementStatisticsService.list(queryWrapper);
        double count = 0.0;//总盘数
        double primaryCount = 0.0;//初级超标盘数
        double middleCount = 0.0;//中级超标盘数
        double advanceCount = 0.0;//高级超标盘数
        double qualifiedCount = 0.0;//合格盘数
        double overProofCount = 0.0;//超标总盘数

        double handleCount = 0.0;//超标已处置盘数
        double untreatedCount = 0.0;//超标未处置盘数
        double handleCent = 0.0;//处置率
        double untreatedCent = 0.0;//未处置率

        double primaryCent = 0.0;//初级超标率
        double middleCent = 0.0;//中级超标率
        double advancedCent = 0.0;//高级超标率
        double qualifiedCent = 0.0;//合格率

        for (BhzCementStatistics bhzCementStatistics1 : list) {
            count = bhzCementStatistics1.getAllDish();
            primaryCount = bhzCementStatistics1.getPrimaryDish();
            middleCount = bhzCementStatistics1.getMiddleDish();
            advanceCount = bhzCementStatistics1.getAdvancedDish();
            overProofCount = bhzCementStatistics1.getAllOverproofDish();
            qualifiedCount = count - overProofCount;
            handleCount = bhzCementStatistics1.getAllHandleDish();
            untreatedCount = overProofCount - handleCount;
            if (count != 0) {
                primaryCent = Double.parseDouble(String.format("%.2f", (primaryCount / count) * 100));
                middleCent = Double.parseDouble(String.format("%.2f", (middleCount / count) * 100));
                advancedCent = Double.parseDouble(String.format("%.2f", (advanceCount / count) * 100));
                qualifiedCent = Double.parseDouble(String.format("%.2f", (qualifiedCount / count) * 100));
            }
            if (overProofCount != 0) {
                handleCent = Double.parseDouble(String.format("%.2f", (handleCount / overProofCount) * 100));
                untreatedCent = Double.parseDouble(String.format("%.2f", (untreatedCount / overProofCount) * 100));
            }
        }
        map.put("primaryCent", primaryCent);
        map.put("middleCent", middleCent);
        map.put("advancedCent", advancedCent);
        map.put("qualifiedCent", qualifiedCent);
        map.put("handleCent", handleCent);
        map.put("untreatedCent", untreatedCent);
        map.put("primaryCount", primaryCount);
        map.put("middleCount", middleCount);
        map.put("advanceCount", advanceCount);
        map.put("qualifiedCount", qualifiedCount);
        return Result.OK(map);
    }

    /**
     * 沥青拌合站首页超标等级盘数/未处置/已处置盘数
     *
     * @param bhzCementBase
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @ApiOperation(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数", notes = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @GetMapping(value = "/titleDataSta")
    public Result<?> getTitleDataSta(BhzCementBase bhzCementBase, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String device = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
        //查询到他的设备编号
        if (!"null".equals(device)) {
            bhzCementBase.setShebeiNo(device);
        } else {
            bhzCementBase.setShebeiNo("空");
        }

//        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
//        queryWrapper.select("ifnull(sum(primary_dish),0) as primary_dish,ifnull(sum(middle_dish),0) as middle_dish," +
//                "ifnull(sum(advanced_dish),0) as advanced_dish,ifnull(sum(all_dish),0) as all_dish," +
//                "ifnull(sum(all_handle_dish),0) as all_handle_dish,ifnull(sum(all_overproof_dish),0) as all_overproof_dish," +
//                "ifnull(sum(primary_handle),0) as primary_handle,ifnull(sum(middle_handle),0) as middle_handle," +
//                "ifnull(sum(advanced_handle),0) as advanced_handle");
//        List<BhzCementStatistics> bhzLqStatisticsList = bhzCementStatisticsService.list(queryWrapper);


        String[] split = device.split(",");
        List<String> List = Arrays.asList(split);
        String shebeilist = null;
        for (int i = 0; i < List.size(); i++) {
            if (i == List.size() - 1) {
                shebeilist = shebeilist + "," + "'" + List.get(i) + "'";
            } else if (i == 0) {
                shebeilist = "'" + List.get(i) + "'";
            } else {
                shebeilist = shebeilist + "," + "'" + List.get(i) + "'";
            }
        }
        List<BhzCementBaseRC> bhzCementBaseList = bhzCementBaseService.selectTongjiData(shebeilist);
        double count = 0.0;//总盘数
        double primaryCount = 0.0;//初级超标盘数
        double middleCount = 0.0;//中级超标盘数
        double advanceCount = 0.0;//高级超标盘数
        double qualifiedCount = 0.0;//合格盘数
        double overProofCount = 0.0;//超标总盘数

        double handleCount = 0.0;//超标已处置盘数
        double untreatedCount = 0.0;//超标未处置盘数
        double handleCent = 0.0;//处置率
        double untreatedCent = 0.0;//未处置率

        double primaryCent = 0.0;//初级超标率
        double middleCent = 0.0;//中级超标率
        double advancedCent = 0.0;//高级超标率
        double qualifiedCent = 0.0;//合格率

        double primaryHandle = 0.0;//初级超标处置盘数
        double middleHandle = 0.0;//中级超标处置盘数
        double advanceHandle = 0.0;//高级超标处置盘数
        double primaryHandleCent = 0.0;//初级超标处置率
        double middleHandleCent = 0.0;//中级超标处置率
        double advanceHandleCent = 0.0;//高级超标处置率
        double primaryNotHandle = 0.0;//初级超标未处置
        double middleNotHandle = 0.0;//中级超标未处置
        double advanceNotHandle = 0.0;//高级超标未处置

        for (BhzCementBaseRC bhzCS : bhzCementBaseList) {
            count = bhzCS.getAllDish();
            primaryCount = bhzCS.getPrimaryCount();
            middleCount = bhzCS.getMiddleCount();
            advanceCount = bhzCS.getAdvancedCount();
            double v = primaryCount + middleCount + advanceCount;
            overProofCount = v;
            qualifiedCount = count - overProofCount;
            handleCount = bhzCS.getAllHandleDish();
            untreatedCount = bhzCS.getAllNotHandleDish();
            primaryNotHandle = bhzCS.getPrimaryNotHandle();
            middleNotHandle = bhzCS.getMiddleNotHandle();
            advanceNotHandle = bhzCS.getAdvanceNotHandle();
            if (count != 0) {
                primaryCent = Double.parseDouble(String.format("%.2f", (primaryCount / count) * 100));
                middleCent = Double.parseDouble(String.format("%.2f", (middleCount / count) * 100));
                advancedCent = Double.parseDouble(String.format("%.2f", (advanceCount / count) * 100));
                qualifiedCent = Double.parseDouble(String.format("%.2f", (qualifiedCount / count) * 100));
            }
//            primaryHandle = bhzCS.getPrimaryHandle();
//            middleHandle = bhzCS.getMiddleHandle();
//            advanceHandle = bhzCS.getAdvancedHandle();
//            if (overProofCount != 0) {
//                primaryHandleCent = Double.parseDouble(String.format("%.2f", (primaryHandle / overProofCount) * 100));
//                middleHandleCent = Double.parseDouble(String.format("%.2f", (middleHandle / overProofCount) * 100));
//                advanceHandleCent = Double.parseDouble(String.format("%.2f", (advanceHandle / overProofCount) * 100));
//                handleCent = Double.parseDouble(String.format("%.2f", (handleCount / overProofCount) * 100));
//                untreatedCent = Double.parseDouble(String.format("%.2f", (untreatedCount / overProofCount) * 100));
//
//            }
        }
        QueryWrapper<BhzCementBase> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper1.select("count(1) AS jiaozhustatus ,over_level ");
        queryWrapper1.gt("overproof_status", 0);
        queryWrapper1.gt("over_level", 0);
        queryWrapper1.groupBy("over_level");
        List<BhzCementBase> list = bhzCementBaseService.list(queryWrapper1);
        for (BhzCementBase bhzCementBase1 : list) {
            if (bhzCementBase1.getOverLevel() == 1) {
                primaryHandle = Double.valueOf(bhzCementBase1.getJiaozhustatus());
            }
            if (bhzCementBase1.getOverLevel() == 2) {
                middleHandle = Double.valueOf(bhzCementBase1.getJiaozhustatus());
            }
            if (bhzCementBase1.getOverLevel() == 3) {
                advanceHandle = Double.valueOf(bhzCementBase1.getJiaozhustatus());
            }
        }

        map.put("primaryCount", primaryCount);
        map.put("middleCount", middleCount);
        map.put("advanceCount", advanceCount);
        map.put("qualifiedCount", qualifiedCount);
        map.put("handleCount", handleCount);
        map.put("untreatedCount", untreatedCount);
        map.put("primaryCent", primaryCent);
        map.put("middleCent", middleCent);
        map.put("advancedCent", advancedCent);
        map.put("qualifiedCent", qualifiedCent);
        map.put("primaryHandle", primaryHandle);
        map.put("middleHandle", middleHandle);
        map.put("advanceHandle", advanceHandle);
        map.put("primaryHandleCent", primaryHandleCent);
        map.put("middleHandleCent", middleHandleCent);
        map.put("advanceHandleCent", advanceHandleCent);
        map.put("handleCent", handleCent);
        map.put("untreatedCent", untreatedCent);
        map.put("primaryNotHandle", primaryNotHandle);
        map.put("middleNotHandle", middleNotHandle);
        map.put("advanceNotHandle", advanceNotHandle);
        return Result.OK(map);
    }

    /**
     * 沥青拌合站首页超标等级盘数/未处置/已处置盘数
     *
     * @param bhzCementBase
     * @param
     * @param
     * @return
     */
    @AutoLog(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @ApiOperation(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数", notes = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @GetMapping(value = "/titleDataStaZt")
    public Result<?> getTitleDataStaFbZt(BhzCementBase bhzCementBase) {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("A05A01A05A01A01A01A01");
        list1.add("A05A01A05A01A01A01A02");
        list1.add("A05A01A05A01A01A01A03");
        list1.add("A05A01A05A01A01A01A04");
        List<String> shebeiList = new ArrayList<>();
        for (String l : list1) {
            List<String> shebeiList1 = shebeiInfoService.selectShebeiList(l, 0);
            for (String l1 : shebeiList1) {
                shebeiList.add(l1);
            }
        }


        Map map = new HashMap();
        QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ifnull(sum(primary_dish),0) as primary_dish,ifnull(sum(middle_dish),0) as middle_dish," +
                "ifnull(sum(advanced_dish),0) as advanced_dish,ifnull(sum(all_dish),0) as all_dish," +
                "ifnull(sum(all_handle_dish),0) as all_handle_dish,ifnull(sum(all_overproof_dish),0) as all_overproof_dish," +
                "ifnull(sum(primary_handle),0) as primary_handle,ifnull(sum(middle_handle),0) as middle_handle," +
                "ifnull(sum(advanced_handle),0) as advanced_handle");
        queryWrapper.in("shebei_no", shebeiList);
        List<BhzCementStatistics> bhzLqStatisticsList = bhzCementStatisticsService.list(queryWrapper);

        double count = 0.0;//总盘数
        double primaryCount = 0.0;//初级超标盘数
        double middleCount = 0.0;//中级超标盘数
        double advanceCount = 0.0;//高级超标盘数
        double qualifiedCount = 0.0;//合格盘数
        double overProofCount = 0.0;//超标总盘数

        double handleCount = 0.0;//超标已处置盘数
        double untreatedCount = 0.0;//超标未处置盘数
        double handleCent = 0.0;//处置率
        double untreatedCent = 0.0;//未处置率

        double primaryCent = 0.0;//初级超标率
        double middleCent = 0.0;//中级超标率
        double advancedCent = 0.0;//高级超标率
        double qualifiedCent = 0.0;//合格率

        double primaryHandle = 0.0;//初级超标处置盘数
        double middleHandle = 0.0;//中级超标处置盘数
        double advanceHandle = 0.0;//高级超标处置盘数
        double primaryHandleCent = 0.0;//初级超标处置率
        double middleHandleCent = 0.0;//中级超标处置率
        double advanceHandleCent = 0.0;//高级超标处置率

        for (BhzCementStatistics bhzCS : bhzLqStatisticsList) {
            count = bhzCS.getAllDish();
            primaryCount = bhzCS.getPrimaryDish();
            middleCount = bhzCS.getMiddleDish();
            advanceCount = bhzCS.getAdvancedDish();
            overProofCount = bhzCS.getAllOverproofDish();
            qualifiedCount = count - overProofCount;
            handleCount = bhzCS.getAllHandleDish();
            untreatedCount = overProofCount - handleCount;
            if (count != 0) {
                primaryCent = Double.parseDouble(String.format("%.2f", (primaryCount / count) * 100));
                middleCent = Double.parseDouble(String.format("%.2f", (middleCount / count) * 100));
                advancedCent = Double.parseDouble(String.format("%.2f", (advanceCount / count) * 100));
                qualifiedCent = Double.parseDouble(String.format("%.2f", (qualifiedCount / count) * 100));
            }
//            primaryHandle = bhzCS.getPrimaryHandle();
//            middleHandle = bhzCS.getMiddleHandle();
//            advanceHandle = bhzCS.getAdvancedHandle();
//            if (overProofCount != 0) {
//                primaryHandleCent = Double.parseDouble(String.format("%.2f", (primaryHandle / overProofCount) * 100));
//                middleHandleCent = Double.parseDouble(String.format("%.2f", (middleHandle / overProofCount) * 100));
//                advanceHandleCent = Double.parseDouble(String.format("%.2f", (advanceHandle / overProofCount) * 100));
//                handleCent = Double.parseDouble(String.format("%.2f", (handleCount / overProofCount) * 100));
//                untreatedCent = Double.parseDouble(String.format("%.2f", (untreatedCount / overProofCount) * 100));
//
//            }
        }
        QueryWrapper<BhzCementBase> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("count(1) AS jiaozhustatus ,over_level ");
        queryWrapper1.gt("overproof_status", 0);
        queryWrapper1.gt("over_level", 0);
        queryWrapper1.groupBy("over_level");
        List<BhzCementBase> list = bhzCementBaseService.list(queryWrapper1);
        for (BhzCementBase l : list) {
            if (bhzCementBase.getOverLevel() == null) {
                continue;
            }
            if (bhzCementBase.getOverLevel() == 1) {
                primaryHandle = Double.valueOf(l.getJiaozhustatus());
            }
            if (bhzCementBase.getOverLevel() == 2) {
                middleHandle = Double.valueOf(l.getJiaozhustatus());
            }
            if (bhzCementBase.getOverLevel() == 3) {
                advanceHandle = Double.valueOf(l.getJiaozhustatus());
            }
        }

        map.put("primaryCount", primaryCount);
        map.put("middleCount", middleCount);
        map.put("advanceCount", advanceCount);
        map.put("qualifiedCount", qualifiedCount);
        map.put("handleCount", handleCount);
        map.put("untreatedCount", untreatedCount);
        map.put("primaryCent", primaryCent);
        map.put("middleCent", middleCent);
        map.put("advancedCent", advancedCent);
        map.put("qualifiedCent", qualifiedCent);
        map.put("primaryHandle", primaryHandle);
        map.put("middleHandle", middleHandle);
        map.put("advanceHandle", advanceHandle);
        map.put("primaryHandleCent", primaryHandleCent);
        map.put("middleHandleCent", middleHandleCent);
        map.put("advanceHandleCent", advanceHandleCent);
        map.put("handleCent", handleCent);
        map.put("untreatedCent", untreatedCent);
        return Result.OK(map);
    }

    /**
     * 沥青拌合站首页超标等级盘数/未处置/已处置盘数
     *
     * @param bhzCementBase
     * @param
     * @param
     * @return
     */
    @AutoLog(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @ApiOperation(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数", notes = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @GetMapping(value = "/titleDataStaFb")
    public Result<?> getTitleDataStaFb(BhzCementBase bhzCementBase) {
        if (bhzCementBase.getSysOrgCode() == null) {
            return Result.error("请输入标段");
        }
        List<String> shebeiList = shebeiInfoService.selectShebeiList(bhzCementBase.getSysOrgCode(), 0);

        Map map = new HashMap();
        QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ifnull(sum(primary_dish),0) as primary_dish,ifnull(sum(middle_dish),0) as middle_dish," +
                "ifnull(sum(advanced_dish),0) as advanced_dish,ifnull(sum(all_dish),0) as all_dish," +
                "ifnull(sum(all_handle_dish),0) as all_handle_dish,ifnull(sum(all_overproof_dish),0) as all_overproof_dish," +
                "ifnull(sum(primary_handle),0) as primary_handle,ifnull(sum(middle_handle),0) as middle_handle," +
                "ifnull(sum(advanced_handle),0) as advanced_handle");
        queryWrapper.in("shebei_no", shebeiList);
        List<BhzCementStatistics> bhzLqStatisticsList = bhzCementStatisticsService.list(queryWrapper);

        double count = 0.0;//总盘数
        double primaryCount = 0.0;//初级超标盘数
        double middleCount = 0.0;//中级超标盘数
        double advanceCount = 0.0;//高级超标盘数
        double qualifiedCount = 0.0;//合格盘数
        double overProofCount = 0.0;//超标总盘数

        double handleCount = 0.0;//超标已处置盘数
        double untreatedCount = 0.0;//超标未处置盘数
        double handleCent = 0.0;//处置率
        double untreatedCent = 0.0;//未处置率

        double primaryCent = 0.0;//初级超标率
        double middleCent = 0.0;//中级超标率
        double advancedCent = 0.0;//高级超标率
        double qualifiedCent = 0.0;//合格率

        double primaryHandle = 0.0;//初级超标处置盘数
        double middleHandle = 0.0;//中级超标处置盘数
        double advanceHandle = 0.0;//高级超标处置盘数
        double primaryHandleCent = 0.0;//初级超标处置率
        double middleHandleCent = 0.0;//中级超标处置率
        double advanceHandleCent = 0.0;//高级超标处置率

        for (BhzCementStatistics bhzCS : bhzLqStatisticsList) {
            count = bhzCS.getAllDish();
            primaryCount = bhzCS.getPrimaryDish();
            middleCount = bhzCS.getMiddleDish();
            advanceCount = bhzCS.getAdvancedDish();
            overProofCount = bhzCS.getAllOverproofDish();
            qualifiedCount = count - overProofCount;
            handleCount = bhzCS.getAllHandleDish();
            untreatedCount = overProofCount - handleCount;
            if (count != 0) {
                primaryCent = Double.parseDouble(String.format("%.2f", (primaryCount / count) * 100));
                middleCent = Double.parseDouble(String.format("%.2f", (middleCount / count) * 100));
                advancedCent = Double.parseDouble(String.format("%.2f", (advanceCount / count) * 100));
                qualifiedCent = Double.parseDouble(String.format("%.2f", (qualifiedCount / count) * 100));
            }
//            primaryHandle = bhzCS.getPrimaryHandle();
//            middleHandle = bhzCS.getMiddleHandle();
//            advanceHandle = bhzCS.getAdvancedHandle();
//            if (overProofCount != 0) {
//                primaryHandleCent = Double.parseDouble(String.format("%.2f", (primaryHandle / overProofCount) * 100));
//                middleHandleCent = Double.parseDouble(String.format("%.2f", (middleHandle / overProofCount) * 100));
//                advanceHandleCent = Double.parseDouble(String.format("%.2f", (advanceHandle / overProofCount) * 100));
//                handleCent = Double.parseDouble(String.format("%.2f", (handleCount / overProofCount) * 100));
//                untreatedCent = Double.parseDouble(String.format("%.2f", (untreatedCount / overProofCount) * 100));
//
//            }
        }
        QueryWrapper<BhzCementBase> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("count(1) AS jiaozhustatus ,over_level ");
        queryWrapper1.gt("overproof_status", 0);
        queryWrapper1.gt("over_level", 0);
        queryWrapper1.groupBy("over_level");
        List<BhzCementBase> list = bhzCementBaseService.list(queryWrapper1);
        for (BhzCementBase l : list) {
            if (bhzCementBase.getOverLevel() == null) {
                continue;
            }
            if (bhzCementBase.getOverLevel() == 1) {
                primaryHandle = Double.valueOf(l.getJiaozhustatus());
            }
            if (bhzCementBase.getOverLevel() == 2) {
                middleHandle = Double.valueOf(l.getJiaozhustatus());
            }
            if (bhzCementBase.getOverLevel() == 3) {
                advanceHandle = Double.valueOf(l.getJiaozhustatus());
            }
        }

        map.put("primaryCount", primaryCount);
        map.put("middleCount", middleCount);
        map.put("advanceCount", advanceCount);
        map.put("qualifiedCount", qualifiedCount);
        map.put("handleCount", handleCount);
        map.put("untreatedCount", untreatedCount);
        map.put("primaryCent", primaryCent);
        map.put("middleCent", middleCent);
        map.put("advancedCent", advancedCent);
        map.put("qualifiedCent", qualifiedCent);
        map.put("primaryHandle", primaryHandle);
        map.put("middleHandle", middleHandle);
        map.put("advanceHandle", advanceHandle);
        map.put("primaryHandleCent", primaryHandleCent);
        map.put("middleHandleCent", middleHandleCent);
        map.put("advanceHandleCent", advanceHandleCent);
        map.put("handleCent", handleCent);
        map.put("untreatedCent", untreatedCent);
        return Result.OK(map);
    }

    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-原材料消耗统计", notes = "混凝土拌合站统计-原材料消耗统计")
    @GetMapping(value = "/ycllist")
    public Result<?> yclPageList(BhzCementBase bhzCementBase,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                 HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
//		 QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
//		 Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
//		 IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
//		 List<BhzCementBase> list = pageList.getRecords();
        List<Map> bhzCementBaseList = bhzCementBaseService.ycltjlist(pageNo, pageSize);
        return Result.OK(bhzCementBaseList);
    }


    @AutoLog(value = "混凝土拌合站超标处置TOP按超标等级统计数量")
    @ApiOperation(value = "混凝土拌合站超标处置TOP按等级统计数量", notes = "混凝土拌合站超标处置TOP按等级统计数量")
    @GetMapping(value = "/djcount")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseCBList,bhz/hntbhz/BhzCementBaseCallList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> djcount(BhzCementBase bhzCementBase,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                             HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        bhzCementBase.setIsdel(0);
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());

        queryWrapper.select("over_level,COUNT(1) AS status");
        queryWrapper.gt("over_level", 0);
        queryWrapper.groupBy("over_level");

        List<BhzCementBase> list = bhzCementBaseService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "混凝土拌合站超标处置TOP按处置统计数量")
    @ApiOperation(value = "混凝土拌合站超标处置TOP按等级统计数量", notes = "混凝土拌合站超标处置TOP按等级统计数量")
    @GetMapping(value = "/czcount")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseCBList,bhz/hntbhz/BhzCementBaseCallList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> czcount(BhzCementBase bhzCementBase,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                             HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        bhzCementBase.setIsdel(0);
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.select("overproof_status,COUNT(1) AS status");
        queryWrapper.gt("over_level", 0);
//        queryWrapper.ne("overproof_status", 20);
        queryWrapper.groupBy("overproof_status");

        List<BhzCementBase> list = bhzCementBaseService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 根据传输 设备编号  格式"'xx','xx'"
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param sys_depart_orgcode
     * @param req
     * @param shebeilist
     * @return
     */
    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-原材料消耗统计", notes = "混凝土拌合站统计-原材料消耗统计")
    @GetMapping(value = "/ycllist2")
    public Result<?> yclPageList2(BhzCementBase bhzCementBase,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                  HttpServletRequest req, String shebeilist) {
        List<Map> bhzCementBaseList = bhzCementBaseService.ycltjlists(shebeilist, pageNo, pageSize);
        return Result.OK(bhzCementBaseList);
    }

    /**
     * 根据传输 设备编号  格式"'xx','xx'"  统计表统计
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param sys_depart_orgcode
     * @param req
     * @param shebeilist
     * @return
     */
    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-原材料消耗统计", notes = "混凝土拌合站统计-原材料消耗统计")
    @GetMapping(value = "/ycllist3")
    public Result<?> yclPageList3(BhzCementBase bhzCementBase,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                  HttpServletRequest req, String shebeilist, String orgCode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (StrUtil.isBlank(orgCode)) {
            orgCode = loginUser.getOrgCode();
        }
        if (StrUtil.isBlank(shebeilist)) {
            shebeilist = "";
            List<ShebeiInfo> lists = shebeiInfoService.shebeilist(0, orgCode);
            if (lists.size() > 0) {
                for (ShebeiInfo shebeiInfo : lists) {
                    if (orgCode.contains("A05A01A03A01A01A0")) {
                        if (shebeiInfo.getSbjno().contains("A05A01A03A01A01A0") || shebeiInfo.getSbjno().contains("ydgsnhbh03")) {
                            if ("".equals(shebeilist)) {
                                shebeilist = "'" + shebeiInfo.getSbjno() + "'";
                            } else {
                                shebeilist = shebeilist + "," + "'" + shebeiInfo.getSbjno() + "'";
                            }
                        }
                    } else {
                        if ("".equals(shebeilist)) {
                            shebeilist = "'" + shebeiInfo.getSbjno() + "'";
                        } else {
                            shebeilist = shebeilist + "," + "'" + shebeiInfo.getSbjno() + "'";
                        }
                    }
                }
            }
        }
        List<Map> bhzCementBaseList = bhzCementBaseService.ycltjliststatic(shebeilist, pageNo, pageSize);
        for (int i = 0; i < bhzCementBaseList.size(); i++) {
            Map map = bhzCementBaseList.get(i);
            if ((Integer) map.get("materiale_type") == null) {
                String s = String.valueOf(map.get("materiale_name"));
                int i1 = materialeUtil.lqCailiaotype(s);
                map.put("materiale_type", i1);
                bhzCementBaseList.set(i, map);
            }
            if ((Integer) map.get("materiale_type") == 3) {
                if (String.valueOf(map.get("materiale_name")).contains("沙") || String.valueOf(map.get("materiale_name")).contains("砂")) {
                    map.put("materiale_name", "骨料");
                    bhzCementBaseList.set(i, map);
                }
            }
        }
        return Result.OK(bhzCementBaseList);
    }

//	 @AutoLog(value = "拌合站主表-原材料消耗统计1")
//	 @ApiOperation(value="拌合站主表-原材料消耗统计1", notes="拌合站主表-原材料消耗统计1")
//	 @GetMapping(value = "/ycllist1")
//	 @PermissionData(pageComponent="bhz/hntbhz/BhzCementBaseList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
//	 public Result<?> ycllist1PageList(BhzCementBase bhzCementBase,
//									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize, String sys_depart_orgcode,
//									HttpServletRequest req) {
//		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//		 String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//		 if (bhzCementBase.getShebeiNo() == null) {
//			 if (shebei != null) {
//				 bhzCementBase.setShebeiNo(shebei);
//			 }
//		 }
//		 QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
//		 Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
//		 IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
//		 List<BhzCementBase> list = pageList.getRecords();
//		 Map map = new HashMap();
//		 List list1 = new ArrayList();
//		 for (BhzCementBase bhzCementBase1 : list) {
//			 String batchNo = bhzCementBase1.getBatchNo();
//			 List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(batchNo);
//			 for (BhzCementDetail bhzCementDetail : selectcementlist) {
//				 BhzCementDetail bhzCementDetail2 = new BhzCementDetail();
//				 Integer materialeType = bhzCementDetail.getMaterialeType();
//				 String materialeName = bhzCementDetail.getMaterialeName();
//				 Double realityNumber = bhzCementDetail.getRealityNumber();
//				 bhzCementDetail2.setMaterialeType(materialeType);
//				 bhzCementDetail2.setMaterialeName(materialeName);
//				 bhzCementDetail2.setRealityNumber(realityNumber);
//				 list1.add(bhzCementDetail2);
//			 }
//		 }
//		 return Result.OK(list1);
//	 }


    /**
     * 混凝土拌合站根据省/市/项目统计盘数/合格数/不合格数对外接口
     *
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    @AutoLog(value = "混凝土拌合站根据省/市/项目统计盘数/合格数/不合格数")
    @ApiOperation(value = "混凝土拌合站根据省/市/项目统计盘数/合格数/不合格数", notes = "混凝土拌合站根据省/市/项目统计盘数/合格数/不合格数")
    @GetMapping(value = "/departtbhzlist")
    public Result<?> queryPageListdepart(String sysOrgCode) {
        List<String> list = new ArrayList<>();
        List<ShebeiInfo> shebeilist = shebeiInfoService.arrayOneshebei(sysOrgCode);
        for (ShebeiInfo shebeiInfo : shebeilist) {
            list.add(shebeiInfo.getSbjno());
        }
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(all_dish) as all_dish,sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper.in("shebei_no", list);
        List<BhzCementStatistics> bhzCementStatisticsList = bhzCementStatisticsService.list(queryWrapper);
        double hntsum = 0.0;
        double hutchaobiao = 0.0;
        double hnuthegelv = 0.0;
        double hntchaobiaolv = 0.0;
        double hegesum = 0.0;
        for (BhzCementStatistics statistics : bhzCementStatisticsList) {
            if (statistics != null) {
                hntsum = statistics.getAllDish();
                hutchaobiao = statistics.getAllOverproofDish();
                hegesum = hntsum - hutchaobiao;
                if (hntsum != 0) {
                    hnuthegelv = (hegesum / hntsum) * 100;
                    hntchaobiaolv = (hutchaobiao / hntsum) * 100;
                }
            }
        }
        map.put("hntsum", (int) hntsum);
        map.put("hnuthegelv", hnuthegelv);
        map.put("hutchaobiao", (int) hutchaobiao);
        map.put("hntchaobiaolv", hntchaobiaolv);
        return Result.OK(map);
    }

    /**
     * 混凝土拌合站统计图
     *
     * @param bhzCementStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-总产能/本年度/当前季度/当前月/当前周/当天", notes = "混凝土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/tbhcnlist")
    public Result<?> querytbhcnPageList(BhzCementStatistics bhzCementStatistics, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzCementStatistics.setShebeiNo(shebei);
        } else {
            bhzCementStatistics.setShebeiNo("空");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        String format3 = format.format(new Date());
        String format4 = ft.format(new Date());
        double estimateNumberAll = 0.0;
        double estimateNumberY = 0.0;
        double estimateNumberQ = 0.0;
        double estimateNumberM = 0.0;
        double estimateNumberW = 0.0;
        double estimateNumberD = 0.0;
        Map<String, Double> map = new HashMap<>();
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(estimate_number) as estimate_number");
        BhzCementStatistics one = bhzCementStatisticsService.getOne(queryWrapper);
        if (one != null) {
            estimateNumberAll = one.getEstimateNumber();//总产能(金华的+ 14103.32)//18857.32
        }
        QueryWrapper<BhzCementStatistics> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper1.select("sum(estimate_number) as estimate_number");
        queryWrapper1.last(" and statistics_time like '" + format2 + "%'");
        BhzCementStatistics one1 = bhzCementStatisticsService.getOne(queryWrapper1);
        if (one1 != null) {
            estimateNumberY = one1.getEstimateNumber();//当前年
        }
        QueryWrapper<BhzCementStatistics> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper2.select("sum(estimate_number) as estimate_number");
        queryWrapper2.last(" and statistics_time like '" + format2 + "%' and QUARTER(statistics_time) = QUARTER(now())");
        BhzCementStatistics one2 = bhzCementStatisticsService.getOne(queryWrapper2);
        if (one2 != null) {
            estimateNumberQ = one2.getEstimateNumber();//当前季
        }
        QueryWrapper<BhzCementStatistics> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper3.select("sum(estimate_number) as estimate_number");
        queryWrapper3.last(" and statistics_time like '" + format3 + "%'");
        BhzCementStatistics one3 = bhzCementStatisticsService.getOne(queryWrapper3);
        if (one3 != null) {
            estimateNumberM = one3.getEstimateNumber();//当前月
        }
        QueryWrapper<BhzCementStatistics> queryWrapper4 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper4.select("sum(estimate_number) as estimate_number");
        queryWrapper4.last("and year(statistics_time) = year(now()) and week(statistics_time,1) = week(now(),1)");
        BhzCementStatistics one4 = bhzCementStatisticsService.getOne(queryWrapper4);
        if (one4 != null) {
            estimateNumberW = one4.getEstimateNumber();//当前周
        }
        QueryWrapper<BhzCementStatistics> queryWrapper5 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper5.select("sum(estimate_number) as estimate_number");
        queryWrapper5.last(" and statistics_time like '" + format4 + "%'");
//        queryWrapper5.last(" and YEARWEEK(date_format(statistics_time,'%Y-%m-%d'),1) = YEARWEEK(now())");
        BhzCementStatistics one5 = bhzCementStatisticsService.getOne(queryWrapper5);
        if (one5 != null) {
            estimateNumberD = one5.getEstimateNumber();//当天
        }
        map.put("estimateNumberAll", estimateNumberAll);
        map.put("estimateNumberY", estimateNumberY);
        map.put("estimateNumberQ", estimateNumberQ);
        map.put("estimateNumberM", estimateNumberM);
        map.put("estimateNumberW", estimateNumberW);
        map.put("estimateNumberD", estimateNumberD);
        return Result.OK(map);
    }

    /**
     * 混凝土拌合站统计图
     *
     * @param bhzCementBase
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-总产能/本年度/当前季度/当前月/当前周/当天", notes = "混凝土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/tbhcnlist1")
    public Result<?> querytbhcnPageList1(BhzCementBase bhzCementBase, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzCementBase.setShebeiNo(shebei);
        } else {
            bhzCementBase.setShebeiNo("空");
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format4 = ft.format(new Date());
        double estimateNumberD = 0.0;

        QueryWrapper<BhzCementBase> queryWrapper5 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper5.select("sum(estimate_number) as estimate_number");
        queryWrapper5.last(" and product_datetime like '" + format4 + "%'");
//        queryWrapper5.last(" and YEARWEEK(date_format(statistics_time,'%Y-%m-%d'),1) = YEARWEEK(now())");
        BhzCementBase one5 = bhzCementBaseService.getOne(queryWrapper5);
        if (one5 != null) {
            estimateNumberD = one5.getEstimateNumber();//当天
        }
        return Result.OK(estimateNumberD);
    }


    /**
     * 混凝土拌合站统计图
     *
     * @param bhzCementStatistics
     * @param
     * @param
     * @param
     * @return
     */
    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-总产能/本年度/当前季度/当前月/当前周/当天", notes = "混凝土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/tbhcnlists")
    public Result<?> querytbhcnsPageList(BhzCementStatistics bhzCementStatistics, String sysOrgCode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
//        if (!"null".equals(shebei)) {
//            bhzCementStatistics.setShebeiNo(shebei);
//        } else {
//            bhzCementStatistics.setShebeiNo("空");
//        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        String format3 = format.format(new Date());
        String format4 = ft.format(new Date());
        double estimateNumberAll = 0.0;
        double estimateNumberY = 0.0;
        double estimateNumberQ = 0.0;
        double estimateNumberM = 0.0;
        double estimateNumberW = 0.0;
        double estimateNumberD = 0.0;
        Map<String, Double> map = new HashMap<>();
        QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isBlank(sysOrgCode)) {
            sysOrgCode = loginUser.getOrgCode();
        }
        List<ShebeiInfo> lists = shebeiInfoService.shebeilist(0, sysOrgCode);
        List<String> shebeis = new ArrayList<>();
        if (lists.size() > 0) {
            for (ShebeiInfo shebeiInfo : lists) {
                if (sysOrgCode.contains("A05A01A03A01A01A0")) {
                    if (shebeiInfo.getSbjno().contains("A05A01A03A01A01A0") || shebeiInfo.getSbjno().contains("ydgsnhbh03")) {
                        shebeis.add(shebeiInfo.getSbjno());
                    }
                } else {
                    shebeis.add(shebeiInfo.getSbjno());
                }
            }
        }
        queryWrapper.select("sum(estimate_number) as estimate_number");
        if (shebeis.size() > 0) {
            queryWrapper.in("shebei_no", shebeis);
        } else {
            queryWrapper.eq("shebei_no", "空");
        }
        BhzCementStatistics one = bhzCementStatisticsService.getOne(queryWrapper);
        if (one != null) {
            estimateNumberAll = one.getEstimateNumber();//总产能
        }
        QueryWrapper<BhzCementStatistics> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("sum(estimate_number) as estimate_number");
        if (shebeis.size() > 0) {
            queryWrapper1.in("shebei_no", shebeis);
        } else {
            queryWrapper1.eq("shebei_no", "空");
        }
        queryWrapper1.likeRight("statistics_time", format2);
        BhzCementStatistics one1 = bhzCementStatisticsService.getOne(queryWrapper1);
        if (one1 != null) {
            estimateNumberY = one1.getEstimateNumber();//当前年
        }
        QueryWrapper<BhzCementStatistics> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select("sum(estimate_number) as estimate_number");
        if (shebeis.size() > 0) {
            queryWrapper2.in("shebei_no", shebeis);
        } else {
            queryWrapper2.eq("shebei_no", "空");
        }
        queryWrapper2.last(" and statistics_time like '" + format2 + "%' and QUARTER(statistics_time) = QUARTER(now())");
        BhzCementStatistics one2 = bhzCementStatisticsService.getOne(queryWrapper2);
        if (one2 != null) {
            estimateNumberQ = one2.getEstimateNumber();//当前季
        }
        QueryWrapper<BhzCementStatistics> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.select("sum(estimate_number) as estimate_number");
        if (shebeis.size() > 0) {
            queryWrapper3.in("shebei_no", shebeis);
        } else {
            queryWrapper3.eq("shebei_no", "空");
        }
        queryWrapper3.last(" and statistics_time like '" + format3 + "%'");
        BhzCementStatistics one3 = bhzCementStatisticsService.getOne(queryWrapper3);
        if (one3 != null) {
            estimateNumberM = one3.getEstimateNumber();//当前月
        }
        QueryWrapper<BhzCementStatistics> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.select("sum(estimate_number) as estimate_number");
        if (shebeis.size() > 0) {
            queryWrapper4.in("shebei_no", shebeis);
        } else {
            queryWrapper4.eq("shebei_no", "空");
        }
        queryWrapper4.last("  and statistics_time like '" + format2 + "%' and YEARWEEK(date_format(statistics_time,'%Y-%m-%d')) = YEARWEEK(now())");
        BhzCementStatistics one4 = bhzCementStatisticsService.getOne(queryWrapper4);
        if (one4 != null) {
            estimateNumberW = one4.getEstimateNumber();//当前周
        }
        QueryWrapper<BhzCementStatistics> queryWrapper5 = new QueryWrapper<>();
        queryWrapper5.select("sum(estimate_number) as estimate_number");
        if (shebeis.size() > 0) {
            queryWrapper5.in("shebei_no", shebeis);
        } else {
            queryWrapper5.eq("shebei_no", "空");
        }
        queryWrapper5.last(" and statistics_time like '" + format4 + "%'");
        BhzCementStatistics one5 = bhzCementStatisticsService.getOne(queryWrapper5);
        if (one5 != null) {
            estimateNumberD = one5.getEstimateNumber();//当天
        }
        map.put("estimateNumberAll", estimateNumberAll);
        map.put("estimateNumberY", estimateNumberY);
        map.put("estimateNumberQ", estimateNumberQ);
        map.put("estimateNumberM", estimateNumberM);
        map.put("estimateNumberW", estimateNumberW);
        map.put("estimateNumberD", estimateNumberD);
        return Result.OK(map);
    }

//    /**
//     * 混凝土拌合站统计图
//     *
//     * @param bhzCementStatistics
//     * @param
//     * @param
//     * @param
//     * @return
//     */
//    @AutoLog(value = "混凝土拌合站统计")
//    @ApiOperation(value = "混凝土拌合站统计-总产能/本年度/当前季度/当前月/当前周/当天", notes = "混凝土拌合站统计-超标盘数/方量")
//    @GetMapping(value = "/tbhcnlists1")
//    public Result<?> querytbhcnsPageList1(BhzCementBase bhzCementBase,String sysOrgCode) {
////        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
////        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        //查询到他的设备编号
////        if (!"null".equals(shebei)) {
////            bhzCementBase.setShebeiNo(shebei);
////        } else {
////            bhzCementBase.setShebeiNo("空");
////        }
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
//        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
//        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
//        String format2 = format1.format(new Date());
//        String format3 = format.format(new Date());
//        String format4 = ft.format(new Date());
//        double estimateNumberAll = 0.0;
//        double estimateNumberY = 0.0;
//        double estimateNumberQ = 0.0;
//        double estimateNumberM = 0.0;
//        double estimateNumberW = 0.0;
//        double estimateNumberD = 0.0;
//        Map<String, Double> map = new HashMap<>();
//        QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("sum(estimate_number) as estimate_number");
//        queryWrapper.last("a join shebei_info b on a.shebei_no = b.sbjno where b.sys_org_code = '"+sysOrgCode+"' ");
//        BhzCementBase one = bhzCementBaseService.getOne(queryWrapper);
//        if (one != null) {
//            estimateNumberAll = one.getEstimateNumber();//总产能
//        }
//        QueryWrapper<BhzCementBase> queryWrapper1 = new QueryWrapper<>();
//        queryWrapper1.select("sum(estimate_number) as estimate_number");
//        queryWrapper1.last("a join shebei_info b on a.shebei_no = b.sbjno where b.sys_org_code = '"+sysOrgCode+"' and product_datetime like '" + format2 + "%'");
//        BhzCementBase one1 = bhzCementBaseService.getOne(queryWrapper1);
//        if (one1 != null) {
//            estimateNumberY = one1.getEstimateNumber();//当前年
//        }
//        QueryWrapper<BhzCementBase> queryWrapper2 = new QueryWrapper<>();
//        queryWrapper2.select("sum(estimate_number) as estimate_number");
//        queryWrapper2.last(" a join shebei_info b on a.shebei_no = b.sbjno where b.sys_org_code = '"+sysOrgCode+"' and product_datetime like '" + format2 + "%' and QUARTER(product_datetime) = QUARTER(now())");
//        BhzCementBase one2 = bhzCementBaseService.getOne(queryWrapper2);
//        if (one2 != null) {
//            estimateNumberQ = one2.getEstimateNumber();//当前季
//        }
//        QueryWrapper<BhzCementBase> queryWrapper3 = new QueryWrapper<>();
//        queryWrapper3.select("sum(estimate_number) as estimate_number");
//        queryWrapper3.last("a join shebei_info b on a.shebei_no = b.sbjno where b.sys_org_code = '"+sysOrgCode+"' and product_datetime like '" + format3 + "%'");
//        BhzCementBase one3 = bhzCementBaseService.getOne(queryWrapper3);
//        if (one3 != null) {
//            estimateNumberM = one3.getEstimateNumber();//当前月
//        }
//        QueryWrapper<BhzCementBase> queryWrapper4 = new QueryWrapper<>();
//        queryWrapper4.select("sum(estimate_number) as estimate_number");
//        queryWrapper4.last(" a join shebei_info b on a.shebei_no = b.sbjno where b.sys_org_code = '"+sysOrgCode+"' and product_datetime like '" + format2 + "%' and YEARWEEK(date_format(product_datetime,'%Y-%m-%d')) = YEARWEEK(now())");
//        BhzCementBase one4 = bhzCementBaseService.getOne(queryWrapper4);
//        if (one4 != null) {
//            estimateNumberW = one4.getEstimateNumber();//当前周
//        }
//        QueryWrapper<BhzCementBase> queryWrapper5 = new QueryWrapper<>();
//        queryWrapper5.select("sum(estimate_number) as estimate_number");
//        queryWrapper5.last(" a join shebei_info b on a.shebei_no = b.sbjno where b.sys_org_code = '"+sysOrgCode+"' and product_datetime like '" + format4 + "%'");
//        BhzCementBase one5 = bhzCementBaseService.getOne(queryWrapper5);
//        if (one5 != null) {
//            estimateNumberD = one5.getEstimateNumber();//当天
//        }
//        map.put("estimateNumberAll", estimateNumberAll);
//        map.put("estimateNumberY", estimateNumberY);
//        map.put("estimateNumberQ", estimateNumberQ);
//        map.put("estimateNumberM", estimateNumberM);
//        map.put("estimateNumberW", estimateNumberW);
//        map.put("estimateNumberD", estimateNumberD);
//        return Result.OK(map);
//    }

    /**
     * 混凝土拌合站今日/累计预警统计
     *
     * @param bhzCementStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站今日/累计预警统计")
    @ApiOperation(value = "混凝土拌合站今日/累计预警统计", notes = "混凝土拌合站今日/累计预警统计")
    @GetMapping(value = "/liststa")
    public Result<?> queryPagestaList(BhzCementStatistics bhzCementStatistics, HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format = ft.format(new Date());
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzCementStatistics.setShebeiNo(shebei);
        } else {
            bhzCementStatistics.setShebeiNo("空");
        }
        Map map = new HashMap();
        int yjdishDay = 0;
        int yjdishSum = 0;
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_overproof_dish) as all_overproof_dish");
        BhzCementStatistics statistics = bhzCementStatisticsService.getOne(queryWrapper);
        if (statistics != null) {
            yjdishSum = statistics.getAllOverproofDish();
        }
        QueryWrapper<BhzCementStatistics> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper1.select("sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper1.likeRight("statistics_time", format);
        BhzCementStatistics statisticss = bhzCementStatisticsService.getOne(queryWrapper1);
        if (statisticss != null) {
            yjdishDay = statisticss.getAllOverproofDish();
        }
        map.put("yjdishDay", yjdishDay);
        map.put("yjdishSum", yjdishSum);
        return Result.OK(map);
    }

    /**
     * 添加
     *
     * @param bhzCementBasePage
     * @return
     */
    @AutoLog(value = "拌合站主表-添加")
    @ApiOperation(value = "拌合站主表-添加", notes = "拌合站主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzCementBasePage bhzCementBasePage) {
        if (bhzCementBasePage.getShebeiNo() == null)
            return Result.error("设备编号不能为空");
        BhzCementBase bhzCementBase = new BhzCementBase();
        BeanUtils.copyProperties(bhzCementBasePage, bhzCementBase);
        bhzCementBaseService.saveMain(bhzCementBase, bhzCementBasePage.getBhzCementDetailList());
        log.info(String.format("拌合站生产数据add：" + bhzCementBasePage));
        return Result.OK("添加成功！");
    }

    /**
     * 添加 对外开放(目前针对义东平台)
     *
     * @param bhzCementBasePage
     * @return
     */
    @AutoLog(value = "拌合站主表-添加")
    @ApiOperation(value = "拌合站主表-添加", notes = "拌合站主表-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody BhzCementBasePage bhzCementBasePage) {
        BhzCementBase bhzCementBase = new BhzCementBase();
        BeanUtils.copyProperties(bhzCementBasePage, bhzCementBase);
        bhzCementBaseService.saveMains(bhzCementBase, bhzCementBasePage.getBhzCementDetailList(), bhzCementBasePage.getBhzCementOverHandler());
        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param bhzCementBasePage
     * @return
     */
    @AutoLog(value = "拌合站主表-编辑")
    @ApiOperation(value = "拌合站主表-编辑", notes = "拌合站主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzCementBasePage bhzCementBasePage) {
        BhzCementBase bhzCementBase = new BhzCementBase();
        BeanUtils.copyProperties(bhzCementBasePage, bhzCementBase);
        BhzCementBase bhzCementBaseEntity = bhzCementBaseService.getById(bhzCementBase.getId());
        if (bhzCementBaseEntity == null) {
            return Result.error("未找到对应数据");
        }
        bhzCementBaseService.updateMain(bhzCementBase, bhzCementBasePage.getBhzCementDetailList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "拌合站主表-通过id删除")
    @ApiOperation(value = "拌合站主表-通过id删除", notes = "拌合站主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzCementBaseService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @returntbhcnlists1
     */
    @AutoLog(value = "拌合站主表-批量删除")
    @ApiOperation(value = "拌合站主表-批量删除", notes = "拌合站主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzCementBaseService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    public void addshenpi(String baseid,String note,String handleperson,int station){

        BhzCementOverHandler handle = bhzCementOverHandlerService.selectlist(baseid);
        if(Objects.isNull(handle)){
            // 填报原因
            handle = new BhzCementOverHandler();
            handle.setBaseid(baseid);
            handle.setProblemReasons(note);
            handle.setHandlePerson(handleperson);
            handle.setHandleTime(new Date());
            handle.setOverproofStatus(station);
        }else{
            // 审批
            handle.setRemark(note);
            handle.setApprovalPerson(handleperson);
            handle.setSupervisorHandleTime(new Date());
            handle.setOverproofStatus(station);
        }
        bhzCementOverHandlerService.saveOrUpdate(handle);
    }
    /**
     * 批量更新
     *
     * @param
     * @returntbhcnlists1
     */
    @AutoLog(value = "拌合站主表-批量挂载任务单")
    @ApiOperation(value = "拌合站主表-批量挂载任务单", notes = "拌合站主表-批量挂载任务单")
    @PostMapping(value = "/updateBatch")
    public Result<?> updateBatchById(@RequestBody(required = true) UpdateBatchById update) {
        List<BhzCementBase> bhzs = new ArrayList<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String handlename = String.valueOf(loginUser.getUsername());
        RenwudanSchedule rws = renwudanScheduleService.queryones(update.getRwdcode(), update.getSbjno());
        Bhzrenwudan rwd = bhzrenwudanService.queryselectone(update.getRwdcode());
        Boolean rwexcit = (rws != null);
        Double metes = (rwexcit) ? rws.getMetes() : 0.0;
        Integer allcount = (rwexcit) ? rws.getDishCount() : 0;

        for (BhzCementBase bhz : update.getIds()) {
//             BhzCementBase bhz = new BhzCementBase();
//             bhz.setId(id);
          //  bhz.setPoureLocation(rwd.getConspos());
            bhz.setRenwudanstatus(28);// 挂载数据状态为28
            bhz.setWorkNo(update.getRwdcode().equals("RWD-20250429-1900143")?"":update.getRwdcode());
            bhz.setFormulaNo(update.getPhbcode().equals("SPHB-20250429-1900983")?"":update.getPhbcode());
            bhzs.add(bhz);

            // 生产方量累加
            metes = metes + bhz.getEstimateNumber();
            // 盘数累加
            allcount = allcount + 1;
            // 0公用 1 第一生产线  2 第二生产线）
            // 批量追加填报信息
            addshenpi(bhz.getId().toString(), update.getNote(), handlename,28);
        }
        if (rwexcit) {
            Double bfb = metes / rws.getMete() * 100; // 重新计算浇筑令百分比
            rws.setBfb(bfb > 100.00 ? 100 : bfb);
            rws.setDishCount(allcount);
            rws.setMetes((double) (Math.round(metes * 100) / 100));
            renwudanScheduleService.updateById(rws);
        } else {

            RenwudanSchedule renwudanSchedule = new RenwudanSchedule();
            renwudanSchedule.setCode(update.getRwdcode());
            Double bfb = metes / rwd.getMete() * 100;
            renwudanSchedule.setBfb(bfb > 100.00 ? 100 : bfb);
            renwudanSchedule.setShebeiNo(update.getSbjno());
            renwudanSchedule.setStation(update.getStation());
            renwudanSchedule.setProjectname(rwd.getProjname());
            renwudanSchedule.setConspos(rwd.getConspos());
            renwudanSchedule.setPour(rwd.getPour());
//             renwudanSchedule.setStarttim(productDatetime);
            renwudanSchedule.setBetlev(rwd.getBetlev());
            renwudanSchedule.setLands(rwd.getLands());
            renwudanSchedule.setMete(rwd.getMete());
            renwudanSchedule.setMetes((double) (Math.round(metes * 100) / 100));
            renwudanSchedule.setSysOrgCode(rwd.getSysOrgCode());
            renwudanSchedule.setSysDepartProject(rwd.getProjgrade());
            renwudanSchedule.setDishCount(allcount);

            renwudanScheduleService.save(renwudanSchedule);
        }

        this.bhzCementBaseService.updateBatchById(bhzs);
        return Result.OK("批量修改成功！");
    }

    @AutoLog(value = "拌合站主表-批量挂载任务单审批")
    @ApiOperation(value = "拌合站主表-批量挂载任务单审批", notes = "拌合站主表-批量挂载任务单")
    @PostMapping(value = "/updateBatchSP")
    public Result<?> updateBatchByIdSP(@RequestBody(required = true) UpdateBatchById update) {
        List<BhzCementBase> bhzs = new ArrayList<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String handlename = String.valueOf(loginUser.getUsername());
        for (BhzCementBase bhz : update.getIds()) {
            bhz.setRenwudanstatus(update.getStation());// 挂载数据状态为28
            bhzs.add(bhz);
            addshenpi(bhz.getId().toString(), update.getNote(), handlename,update.getStation());
        }

        this.bhzCementBaseService.updateBatchById(bhzs);
        return Result.OK("批量修改成功！");
    }
    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "拌合站主表-通过id查询")
    @ApiOperation(value = "拌合站主表-通过id查询", notes = "拌合站主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzCementBase bhzCementBase = bhzCementBaseService.getById(id);
        if (bhzCementBase == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzCementBase);

    }

    /**
     * 曹宅项目拌合站数据迁移
     *
     * @param bhzCementBasePage
     * @return
     */
    @AutoLog(value = "拌合站数据-添加")
    @ApiOperation(value = "拌合站数据-添加", notes = "拌合站数据-添加")
    @PostMapping(value = "/addCZ")
    public Result<?> addCZ(@RequestBody BhzCementBasePage bhzCementBasePage) {
        BhzCementBase bhzCementBase = new BhzCementBase();
        BeanUtils.copyProperties(bhzCementBasePage, bhzCementBase);
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sbname", bhzCementBase.getShebeiNo());
        ShebeiInfo shebeiOne = shebeiInfoService.getOne(queryWrapper);
        bhzCementBasePage.setShebeiNo(shebeiOne.getSbjno());
        List<BhzCementDetail> bhzCementDetailList = bhzCementBasePage.getBhzCementDetailList();
        for (BhzCementDetail bhzCementDetail : bhzCementDetailList) {
            bhzCementDetail.setShebeino(shebeiOne.getSbjno());
        }
        bhzCementBaseService.saveMains(bhzCementBase, bhzCementBasePage.getBhzCementDetailList(), bhzCementBasePage.getBhzCementOverHandler());
        return Result.OK("添加成功！");
    }

    /**
     * 通过id查询
     *
     * @param
     * @return
     */
    @AutoLog(value = "拌合站子表材料信息通过主表ID查询")
    @ApiOperation(value = "拌合站子表材料信息主表ID查询", notes = "拌合站子表材料信息-通主表ID查询")
    @GetMapping(value = "/queryBhzCementDetailByMainId")
    public Result<?> queryBhzCementDetailListByMainId(BhzCementDetail bhzCementDetailList, HttpServletRequest req,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize,
                                                      @RequestParam(name = "batchNo", required = true) String batchNo) {
        QueryWrapper<BhzCementDetail> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementDetailList, req.getParameterMap());
        if (StringUtils.isNotBlank(batchNo)) {
            bhzCementDetailList.setBatchNo(batchNo);
        }
        Page<BhzCementDetail> page = new Page<BhzCementDetail>(pageNo, pageSize);
        IPage<BhzCementDetail> pageList = bhzCementDetailService.page(page, queryWrapper);
//        List<BhzCementDetail> pageList = bhzCementDetailService.list(queryWrapper);
        //List<BhzCementDetail> bhzCementDetailList = bhzCementDetailService.selectByMainId(id);
        return Result.OK(pageList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzCementBase
     */


//    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(HttpServletRequest request, BhzCementBase bhzCementBase) {
//        return super.exportXls(request, bhzCementBase, BhzCementBase.class, "拌合站生产数据");
//    }
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzCementBase bhzCementBase) {
        // Step.1 组装查询条件查询数据
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

//        List<BhzCementBase> queryList = bhzCementBaseService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<BhzCementBase> bhzCementBaseList = new ArrayList<BhzCementBase>();
        if (oConvertUtils.isEmpty(selections)) {
            String shebei = String.valueOf(redisUtil.get(sysUser.getId() + "change"));
            if (bhzCementBase.getShebeiNo() == null) {
                if (!"null".equals(shebei)) {
                    bhzCementBase.setShebeiNo(shebei);
                } else {
                    bhzCementBase.setShebeiNo("空");
                }
            }
            if (StringUtils.isNotBlank(bhzCementBase.getPoureLocation())) {
                bhzCementBase.setPoureLocation("*" + bhzCementBase.getPoureLocation() + "*");
            }
            //Step.2 获取导出数据
            QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, request.getParameterMap());
            Page<BhzCementBase> page = new Page<BhzCementBase>(1, 2500);
            IPage<BhzCementBase> pageList1 = bhzCementBaseService.page(page, queryWrapper);
            List<BhzCementBase> queryList = pageList1.getRecords();

            bhzCementBaseList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            bhzCementBaseList = bhzCementBaseService.listByIds(selectionList);
//            bhzCementBaseList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }
        // Step.3 组装pageList
//        List<BhzCementBasePage> pageList = new ArrayList<BhzCementBasePage>();
//        for (BhzCementBase main : bhzCementBaseList) {
//            BhzCementBasePage vo = new BhzCementBasePage();
//            BeanUtils.copyProperties(main, vo);
//            List<BhzCementDetail> bhzCementDetailList = bhzCementDetailService.selectByMainId(main.getBatchNo());
//            vo.setBhzCementDetailList(bhzCementDetailList);
//            pageList.add(vo);
//        }
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "拌合站主表列表");
        mv.addObject(NormalExcelConstants.CLASS, BhzCementBase.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拌合站主表数据", "导出人:" + sysUser.getRealname(), "拌合站主表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, bhzCementBaseList);
        return mv;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzCementBase
     */
    @RequestMapping(value = "/exportXlsCB")//超标数据导出
    public ModelAndView exportXlsCB(HttpServletRequest request, BhzCementBase bhzCementBase, String sys_depart_orgcode) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, request.getParameterMap());
        queryWrapper.ne("over_level", 0);
        queryWrapper.eq("alertstate", 1);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //Step.2 获取导出数据
        List<BhzCementBase> queryList = bhzCementBaseService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "混凝土拌合站超标数据列表");
        mv.addObject(NormalExcelConstants.CLASS, BhzCementBase.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("混凝土拌合站超标数据", "导出人:" + sysUser.getRealname(), "拌合站主表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, queryList);
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<BhzCementBasePage> list = ExcelImportUtil.importExcel(file.getInputStream(), BhzCementBasePage.class, params);
                for (BhzCementBasePage page : list) {
                    BhzCementBase po = new BhzCementBase();
                    BeanUtils.copyProperties(page, po);
                    bhzCementBaseService.saveMain(po, page.getBhzCementDetailList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

    /**
     * 实时快报
     *
     * @return
     */
    @GetMapping(value = "/bulletin")
    public Result<?> bulletin() {
        List<Map<String, Object>> all = new ArrayList<>();
        Map<String, Object> bulletin = bhzCementBaseService.bulletin();
        all.add(bulletin);
        return Result.OK(all);
    }

    /**
     * 拌合站超标率
     *
     * @return
     */
    @GetMapping(value = "/bhzcbv")
    public Result<?> bhzcbv(Integer result) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);

        List<Map> bulletin = bhzCementBaseService.bhzcbv(result, shebeilist);

        return Result.OK(bulletin);
    }

    /**
     * 砼材料超标统计
     *
     * @return
     */
    @GetMapping(value = "/bhzcailiaoCount")
    public Result<?> bhzcailiaoCount(Integer result) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);

        List<Map> bulletin = bhzCementBaseService.bhzcailiaoCount(result, shebeilist);

        return Result.OK(bulletin);
    }


    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-超标分页列表查询")
    @ApiOperation(value = "拌合站主表-超标分页列表查询", notes = "拌合站主表-超标分页列表查询")
    @GetMapping(value = "/listyubh")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseCBList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> listyubh(BhzCementBase bhzCementBase,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                              HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }

        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        queryWrapper.select("over_level,COUNT(1)  AS id");
        queryWrapper.gt("over_level", 0);
//        queryWrapper.like("shebei_no","rc");
        queryWrapper.groupBy("over_level");
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        List<BhzCementBase> records = pageList.getRecords();

        QueryWrapper<BhzCementBase> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper1.select("over_level,COUNT(1)  AS id");
        queryWrapper1.eq("overproof_status", 20);
//        queryWrapper1.like("shebei_no","rc");
        queryWrapper1.groupBy("over_level");
        IPage<BhzCementBase> pageList1 = bhzCementBaseService.page(page, queryWrapper1);
        List<BhzCementBase> records1 = pageList1.getRecords();

        QueryWrapper<BhzCementBase> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper3.select("over_level,COUNT(1)  AS id");
        queryWrapper3.ge("overproof_status", 10);
//        queryWrapper1.like("shebei_no","rc");
        queryWrapper3.groupBy("over_level");
        IPage<BhzCementBase> pageList3 = bhzCementBaseService.page(page, queryWrapper3);
        List<BhzCementBase> records3 = pageList3.getRecords();

        QueryWrapper<BhzCementBase> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page2 = new Page<BhzCementBase>(pageNo, pageSize);
        queryWrapper2.select("COUNT(1)  AS id");
        BhzCementBase pageList2 = bhzCementBaseService.getOne(queryWrapper2);

        Map map = new HashMap();
        Double cjchaobiaolv = 0.0;
        Double zjchaobiaolv = 0.0;
        Double gjchaobiaolv = 0.0;
        Double cjbihelv = 0.0;
        Double zjbihelv = 0.0;
        Double gjbihelv = 0.0;
        Double cjczlv = 0.0;
        Double zjczlv = 0.0;
        Double gjczlv = 0.0;
        Double cjchao = 0.0;
        Double zjchao = 0.0;
        Double gjchao = 0.0;
        Double cjbh = 0.0;
        Double zjbh = 0.0;
        Double gjbh = 0.0;
        Double cjcz = 0.0;
        Double zjcz = 0.0;
        Double gjcz = 0.0;

        Integer zongshu = pageList2.getId();
        for (BhzCementBase bhzCementBase1 : records) {
            if (bhzCementBase1.getOverLevel() == 1) {
                cjchao = Double.valueOf(bhzCementBase1.getId());
                cjchaobiaolv = (cjchao / zongshu) * 100;
                BigDecimal b = new BigDecimal(cjchaobiaolv);
                cjchaobiaolv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            if (bhzCementBase1.getOverLevel() == 2) {
                zjchao = Double.valueOf(bhzCementBase1.getId());
                zjchaobiaolv = (zjchao / zongshu) * 100;
                BigDecimal b = new BigDecimal(zjchaobiaolv);
                zjchaobiaolv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            if (bhzCementBase1.getOverLevel() == 3) {
                gjchao = Double.valueOf(bhzCementBase1.getId());
                gjchaobiaolv = (gjchao / zongshu) * 100;
                BigDecimal b = new BigDecimal(gjchaobiaolv);
                gjchaobiaolv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }

        for (BhzCementBase bhzCementBase1 : records1) {
            if (bhzCementBase1.getOverLevel() == 1) {
                cjbh = Double.valueOf(bhzCementBase1.getId());
                cjbihelv = (cjbh / cjchao) * 100;
                BigDecimal b = new BigDecimal(cjbihelv);
                cjbihelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            if (bhzCementBase1.getOverLevel() == 2) {
                zjbh = Double.valueOf(bhzCementBase1.getId());
                zjbihelv = (zjbh / zjchao) * 100;
                BigDecimal b = new BigDecimal(zjbihelv);
                zjbihelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            if (bhzCementBase1.getOverLevel() == 3) {
                gjbh = Double.valueOf(bhzCementBase1.getId());
                gjbihelv = (gjbh / gjchao) * 100;
                BigDecimal b = new BigDecimal(gjbihelv);
                gjbihelv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }

        for (BhzCementBase bhzCementBase1 : records3) {
            if (bhzCementBase1.getOverLevel() == 1) {
                cjcz = Double.valueOf(bhzCementBase1.getId());
                cjczlv = (cjcz / cjchao) * 100;
                BigDecimal b = new BigDecimal(cjczlv);
                cjczlv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            if (bhzCementBase1.getOverLevel() == 2) {
                zjcz = Double.valueOf(bhzCementBase1.getId());
                zjczlv = (zjcz / zjchao) * 100;
                BigDecimal b = new BigDecimal(zjczlv);
                zjczlv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            if (bhzCementBase1.getOverLevel() == 3) {
                gjcz = Double.valueOf(bhzCementBase1.getId());
                gjczlv = (gjcz / gjchao) * 100;
                BigDecimal b = new BigDecimal(gjczlv);
                gjczlv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        map.put("cjchaolv", cjchaobiaolv);
        map.put("zjchaolv", zjchaobiaolv);
        map.put("gjchaolv", gjchaobiaolv);
        map.put("cjbihelv", cjbihelv);
        map.put("zjbihelv", zjbihelv);
        map.put("gjbihelv", gjbihelv);
        map.put("cjczlv", cjczlv);
        map.put("zjczlv", zjczlv);
        map.put("gjczlv", gjczlv);
        map.put("cjchao", cjchao);
        map.put("zjchao", zjchao);
        map.put("gjchao", gjchao);
        map.put("cjbh", cjbh);
        map.put("zjbh", zjbh);
        map.put("gjbh", gjbh);
        map.put("cjcz", cjcz);
        map.put("zjcz", zjcz);
        map.put("gjcz", gjcz);

        return Result.OK(map);
    }

    @GetMapping(value = "/queryList")
    public Result<?> queryList(BhzCementBase bhzCementBase,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                               HttpServletRequest req, String date) {

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
        bhzCementBase.setShebeiNo(substring);
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        if (date != null) {
            queryWrapper.gt("product_datetime", date);
        }
        queryWrapper.orderByAsc("product_datetime");
        Page<BhzCementBase> page = new Page<>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        for (BhzCementBase bhzCementBase1 : pageList.getRecords()) {
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper1 = new QueryWrapper<>();
            shebeiInfoQueryWrapper1.eq("sbjno", bhzCementBase1.getShebeiNo());
            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfoQueryWrapper1);
            bhzCementBase1.setShebeiNo(one.getSbname());
        }
        return Result.OK(pageList.getRecords());
    }

    @GetMapping(value = "/queryxqList")
    public Result<?> queryxqList(BhzCementBase bhzCementBase,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                 HttpServletRequest req, String date) {

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
        bhzCementBase.setShebeiNo(substring);
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        if (date != null) {
            queryWrapper.gt("product_datetime", date);
        }
        queryWrapper.orderByAsc("product_datetime");
        Page<BhzCementBase> page = new Page<>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        List sendList = new ArrayList();
        for (BhzCementBase bhzCementBase1 : pageList.getRecords()) {
            BhzCementBaseAndDetail bhzCementBaseAndDetail = new BhzCementBaseAndDetail();
            BeanUtils.copyProperties(bhzCementBase1, bhzCementBaseAndDetail);

            QueryWrapper<BhzCementDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("batch_no", bhzCementBase1.getBatchNo());
            List<BhzCementDetail> list1 = bhzCementDetailService.list(queryWrapper1);
            bhzCementBaseAndDetail.setBhzCementDetailList(list1);
            sendList.add(bhzCementBaseAndDetail);
        }
        return Result.OK(sendList);
    }

    @ApiOperation(value = "混凝土用量导出", notes = "")
    @GetMapping(value = "/exportCementUsage")
    public ModelAndView exportCementUsage(@RequestParam(value = "shebeiNo", required = false) String shebeiNo,
                                          @RequestParam(value = "strengthRank", required = false) String strengthRank,
                                          @RequestParam(value = "productDatetime_begin", required = false) String start,
                                          @RequestParam(value = "productDatetime_end", required = false) String end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<Map<String, Object>> list = null;
        if (shebeiNo != null) {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("shebei_no", "project_name as projectName", "strength_rank as strength", "sum(estimate_number) as totalCount", "product_datetime as productTime")
                    .eq("shebei_no", shebeiNo)
                    .ge(start != null, "product_datetime", start)
                    .le(end != null, "product_datetime", end)
                    .eq(StringUtils.isNotBlank(strengthRank),"strength_rank",strengthRank)
                    .groupBy("project_name", "strength_rank")
                    .last(",LEFT (product_datetime,10)");

            list = bhzCementBaseMapper.selectMaps(queryWrapper);
        } else {
            String device = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
            List<String> deviceList = new ArrayList<>();
            try {
                String[] devices = device.split(",");
                if (device != null && devices.length > 0) {
                    for (String str : devices
                    ) {
                        deviceList.add(str);
                    }
                }
            } catch (Exception e) {
                log.error("混凝土用量导出异常：" + e.getMessage());
                e.printStackTrace();
            }
            list = bhzCementBaseService.getList(start, end, deviceList,strengthRank);
        }
        List<CementUsageVo> voList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Map item : list) {
                CementUsageVo cementUsageVo = new CementUsageVo();
                Object sbjno = item.get("shebei_no");
                String sbname = bhzCementBaseService.getSbname(String.valueOf(sbjno));
                cementUsageVo.setShebeiNo(sbname);
                if (item.get("projectName") != null && !"".equals(item.get("projectName"))) {
                    cementUsageVo.setProjectName(item.get("projectName").toString());
                }
                if (item.get("strength") != null && !"".equals(item.get("strength"))) {
                    cementUsageVo.setStrength(item.get("strength").toString());
                }
                DecimalFormat df = new DecimalFormat("#.##");
                String totalCount = df.format(Double.parseDouble(item.get("totalCount").toString()));
                cementUsageVo.setTotalCount(Double.parseDouble(totalCount));
                cementUsageVo.setProductTime(item.get("productTime").toString().substring(0, 10));
                voList.add(cementUsageVo);
            }

        }
        ExportParams exportParams = new ExportParams("混凝土用量统计表", "混凝土用量统计表");
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        try {
            mv.addObject(NormalExcelConstants.FILE_NAME, "混凝土用量统计表");
            mv.addObject(NormalExcelConstants.PARAMS, exportParams);
            mv.addObject(NormalExcelConstants.DATA_LIST, voList);
            mv.addObject(NormalExcelConstants.CLASS, CementUsageVo.class);
        } catch (Exception exception) {
            System.out.println("混凝土用量导出异常:" + exception.getMessage());
            log.error("(" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ") " + "混凝土用量导出异常：" + exception.getMessage());
        }
        return mv;
    }


    @AutoLog(value = "拌合站主表-砼拌合预警")
    @ApiOperation(value = "拌合站主表-砼拌合预警", notes = "拌合站主表-砼拌合预警")
    @GetMapping(value = "/hntbhzWarn")
    public Result<?> hntbhzWarn(String orgCode) {
        if (orgCode == "" || orgCode == null) {
            orgCode = "A";
        }
        BhzCementWarnVO bhzCementWarnVO = bhzCementBaseService.selectWranCount(orgCode);
        Map<Object, Object> map = new HashMap<>();
        //时间超标
        if (bhzCementWarnVO.getTimeCount() == 0) {
            map.put("timeBiheLv", "-");
        } else if (bhzCementWarnVO.getTimeCount() > 0 && bhzCementWarnVO.getTimeBiheCount() == 0) {
            map.put("timeBiheLv", 0);
        } else {
            double i = (double) bhzCementWarnVO.getTimeBiheCount() / (double) bhzCementWarnVO.getTimeCount() * 100;
            map.put("timeBiheLv", (double) Math.round(i * 100) / 100);
        }
        //初级超标
        if (bhzCementWarnVO.getPrimaryCount() == 0) {
            map.put("primaryBiheLv", "-");
        } else if (bhzCementWarnVO.getPrimaryCount() > 0 && bhzCementWarnVO.getPrimaryBiheCount() == 0) {
            map.put("primaryBiheLv", 0);
        } else {
            double i = (double) bhzCementWarnVO.getPrimaryBiheCount() / (double) bhzCementWarnVO.getPrimaryCount() * 100;
            map.put("primaryBiheLv", (double) Math.round(i * 100) / 100);
        }
        //中级超标
        if (bhzCementWarnVO.getMiddleCount() == 0) {
            map.put("middleBiheLv", "-");
        } else if (bhzCementWarnVO.getMiddleCount() > 0 && bhzCementWarnVO.getMiddleBiheCount() == 0) {
            map.put("middleBiheLv", 0);
        } else {
            double i = (double) bhzCementWarnVO.getMiddleBiheCount() / (double) bhzCementWarnVO.getMiddleCount() * 100;
            map.put("middleBiheLv", (double) Math.round(i * 100) / 100);
        }

        //高级超标
        if (bhzCementWarnVO.getAdvancedCount() == 0) {
            map.put("advancedBiheLv", "-");
        } else if (bhzCementWarnVO.getAdvancedCount() > 0 && bhzCementWarnVO.getAdvancedBiheCount() == 0) {
            map.put("advancedBiheLv", 0);
        } else {
            double i = (double) bhzCementWarnVO.getAdvancedBiheCount() / (double) bhzCementWarnVO.getAdvancedCount() * 100;
            map.put("advancedBiheLv", (double) Math.round(i * 100) / 100);
        }


        map.put("warnCount", bhzCementWarnVO.getWarnCount());
        map.put("primaryCount", bhzCementWarnVO.getPrimaryCount());
        map.put("middleCount", bhzCementWarnVO.getMiddleCount());
        map.put("advancedCount", bhzCementWarnVO.getAdvancedCount());
        map.put("timeCount", bhzCementWarnVO.getTimeCount());
        map.put("inTempCount", 0);
        map.put("inTempBihelv", "-");
        map.put("outTempCount", 0);
        map.put("outTempBihelv", "-");
        return Result.OK(map);


    }

    @AutoLog(value = "拌合站主表-砼拌合预警详情(项目)")
    @ApiOperation(value = "拌合站主表-砼拌合预警(项目)", notes = "拌合站主表-砼拌合预警(项目)")
    @GetMapping(value = "/bhzWarnbycode")
    public Result<?> hntbhzWarnbycode(@RequestParam String bhzType,
                                      String orgCode) {
        if (orgCode == "" || orgCode == null) {
            orgCode = "A";
        }
        List<BhzCementWarnVO> bhzCementWarnVO = new ArrayList<>();
        if (bhzType.equals("hntbhz")) {
            bhzCementWarnVO = bhzCementBaseService.selectWranCountByorgcde(orgCode);
        } else if (bhzType.equals("lqbhz")) {
            bhzCementWarnVO = bhzLqBasesService.selectWranCountByorgcde(orgCode);
        } else if (bhzType.equals("swbhz")) {
            bhzCementWarnVO = bhzSwBasesService.selectWranCountByorgcde(orgCode);
        } else {
            return Result.error("拌合站类型不对");
        }
        for (BhzCementWarnVO vo : bhzCementWarnVO) {
            //公司名称
            String companyName = bhzCementBaseMapper.selectbyorgcode(vo.getSysOrgCode(), 3);
            if (companyName != null) {
                vo.setCompanyName(companyName);
            }
            //总预警数
            int allWarnDish = vo.getAdvancedCount() + vo.getPrimaryCount() + vo.getMiddleCount();

            //总体闭合率
            if (allWarnDish == 0) {
                vo.setBiheLv((double) 100);
            } else if (allWarnDish > 0 && vo.getBiheCount() == 0) {
                vo.setBiheLv((double) 0);
            } else {
                vo.setBiheLv(((double) vo.getBiheCount() / (double) allWarnDish));//总体闭合率
            }

            //高级预警率
            if (allWarnDish == 0) {
                vo.setAdvancedWarnLv((double) 100);
            } else if (allWarnDish > 0 && vo.getAdvancedCount() == 0) {
                vo.setAdvancedWarnLv((double) 0);
            } else {
                vo.setAdvancedWarnLv(((double) vo.getAdvancedCount() / (double) vo.getAllDish()));//高级预警率
            }
            //总体预警率
            if (vo.getAllDish() == 0) {
                vo.setAllWarnLv((double) 100);
            } else if (vo.getAllDish() > 0 && allWarnDish == 0) {
                vo.setAllWarnLv((double) 0);
            } else {
                vo.setAllWarnLv(((double) allWarnDish / (double) vo.getAllDish()));//总体预警率
            }


        }

        return Result.OK(bhzCementWarnVO);
    }

    @AutoLog(value = "拌合站主表-砼拌合预警详情(设备)")
    @ApiOperation(value = "拌合站主表-砼拌合预警(设备)", notes = "拌合站主表-砼拌合预警(设备)")
    @GetMapping(value = "/hntbhzWarnbyshebeino")
    public Result<?> hntbhzWarnbyshebeino(@RequestParam String sysOrgCode,
                                          @RequestParam String bhzType) {

        List<BhzCementWarnVO> bhzCementWarnVO1 = new ArrayList<>();
        Map map = new HashMap();
        if (bhzType.equals("hntbhz")) {
//            bhzCementWarnVO = bhzCementBaseService.selectWranCountByshebeino(sysOrgCode);
            bhzCementWarnVO1 = bhzCementBaseService.selectBiaoduanByshebeino(sysOrgCode);
        } else if (bhzType.equals("lqbhz")) {
//            bhzCementWarnVO = bhzLqBasesService.selectWranCountByshebeino(sysOrgCode);
            bhzCementWarnVO1 = bhzLqBasesService.selectBiaoduanByshebeino(sysOrgCode);
        } else if (bhzType.equals("swbhz")) {
//            bhzCementWarnVO = bhzSwBasesService.selectWranCountByshebeino(sysOrgCode);
            bhzCementWarnVO1 = bhzSwBasesService.selectBiaoduanByshebeino(sysOrgCode);
        } else {
            return Result.error("拌合站类型不对");
        }
        List<BhzBiaoduanVO> bhzBiaoduanVOList = new ArrayList<>();
        Integer allCount = 0;
        Integer allWarnCount = 0;
        if (bhzCementWarnVO1 != null && bhzCementWarnVO1.size() > 0) {
            for (BhzCementWarnVO vo1 : bhzCementWarnVO1) {
                BhzBiaoduanVO bhzBiaoduanVO = new BhzBiaoduanVO();
                if (vo1.getSectionName() != null) {
                    bhzBiaoduanVO.setName(vo1.getSectionName());
                }
                if (vo1.getAllDish() != null) {
                    allCount = allCount + vo1.getAllDish();
                    bhzBiaoduanVO.setCount(vo1.getAllDish());
                }
                if (vo1.getAllWarnDish() != null) {
                    allWarnCount = allWarnCount + vo1.getAllWarnDish();
                }


                Float chaobiaolv = 0.0F;
                //标段总数和超标率
                if (vo1.getAllDish() == 0) {
                    chaobiaolv = Float.valueOf(100);
                } else if (vo1.getAllDish() > 0 && vo1.getAllWarnDish() == 0) {
                    chaobiaolv = Float.valueOf(0);
                } else {
                    chaobiaolv = ((float) vo1.getAllWarnDish() / (float) vo1.getAllDish());
                    if (chaobiaolv != 0) {
                        chaobiaolv = Math.round(chaobiaolv * 10000) / 10000f;
                        chaobiaolv = chaobiaolv * 100;
                    }
                }
                bhzBiaoduanVO.setChaobiaoLv(chaobiaolv);

                if (vo1.getSysOrgCode() != null) {
                    List<BhzCementWarnVO> bhzCementWarnVO = sectionRecords(bhzType, vo1.getSysOrgCode(), 7);
                    if (bhzCementWarnVO != null && bhzCementWarnVO.size() > 0) {
                        bhzBiaoduanVO.setRecords(bhzCementWarnVO);
                    }
                }
                bhzBiaoduanVOList.add(bhzBiaoduanVO);
            }
        }

        Float allchaobiaolv = 0.0f;
        if (allCount == 0) {
            allchaobiaolv = Float.valueOf(100);
        } else if (allCount > 0 && allWarnCount == 0) {
            allchaobiaolv = Float.valueOf(0);
        } else {
            allchaobiaolv = ((float) allWarnCount / (float) allCount);
            if (allchaobiaolv != 0) {
                allchaobiaolv = Math.round(allchaobiaolv * 10000) / 10000f;
                allchaobiaolv = allchaobiaolv * 100;
            }

        }

        BhzBiaoduanVO bhzBiaoduanVO = new BhzBiaoduanVO();
        bhzBiaoduanVO.setName("总数");
        bhzBiaoduanVO.setCount(allCount);
        bhzBiaoduanVO.setChaobiaoLv(allchaobiaolv);
        List<BhzCementWarnVO> bhzCementWarnVO = sectionRecords(bhzType, sysOrgCode, 4);
        if (bhzCementWarnVO != null && bhzCementWarnVO.size() > 0) {
            bhzBiaoduanVO.setRecords(bhzCementWarnVO);
        }
        bhzBiaoduanVOList.add(0, bhzBiaoduanVO);
        map.put("sectionList", bhzBiaoduanVOList);

        return Result.OK(bhzBiaoduanVOList);
    }

    /*
     *查询sysOrgCode下的所有设备数据
     */
    private List<BhzCementWarnVO> sectionRecords(String bhzType, String sysOrgCode, int i) {
        List<BhzCementWarnVO> bhzCementWarnVO = new ArrayList<>();
        if (bhzType.equals("hntbhz")) {
            bhzCementWarnVO = bhzCementBaseService.selectWranCountByshebeino(sysOrgCode, i);
//                    bhzCementWarnVO1 = bhzCementBaseService.selectBiaoduanByshebeino(sysOrgCode);
        } else if (bhzType.equals("lqbhz")) {
            bhzCementWarnVO = bhzLqBasesService.selectWranCountByshebeino(sysOrgCode, i);
//                    bhzCementWarnVO1 = bhzLqBasesService.selectBiaoduanByshebeino(sysOrgCode);
        } else if (bhzType.equals("swbhz")) {
            bhzCementWarnVO = bhzSwBasesService.selectWranCountByshebeino(sysOrgCode, i);
//                    bhzCementWarnVO1 = bhzSwBasesService.selectBiaoduanByshebeino(sysOrgCode);
        }

        for (BhzCementWarnVO vo : bhzCementWarnVO) {
            //公司名称
            String companyName = bhzCementBaseMapper.selectbyorgcode(vo.getSysOrgCode(), 3);
            if (companyName != null) {
                vo.setCompanyName(companyName);
            }
            //项目名称
            String projectName = bhzCementBaseMapper.selectbyorgcode(vo.getSysOrgCode(), 4);
            if (projectName != null) {
                vo.setProjectName(projectName);
            }
            //标段名称
            String sectionName = bhzCementBaseMapper.selectbysysorgcode(vo.getSysOrgCode());
            if (sectionName != null) {
                vo.setSectionName(sectionName);
            }
//            //设备名称
//            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(vo.getShebeiNo());
//            if (selectshebeione != null && selectshebeione.getSbname() != null) {
//                vo.setShebeiName(selectshebeione.getSbname());
//
//            }
            //总体闭合率
            if (vo.getAllWarnDish() == 0) {
                vo.setBiheLv((double) 100);
            } else if (vo.getAllWarnDish() > 0 && vo.getBiheCount() == 0) {
                vo.setBiheLv((double) 0);
            } else {
                double num = (double) vo.getBiheCount() / (double) vo.getAllWarnDish();
                vo.setBiheLv(num);//总体闭合率
            }

            //总体预警率
            if (vo.getAllDish() == 0) {
                vo.setAllWarnLv((double) 100);
            } else if (vo.getAllDish() > 0 && vo.getAllWarnDish() == 0) {
                vo.setAllWarnLv((double) 0);
            } else {
                double num = (double) vo.getAllWarnDish() / (double) vo.getAllDish();
                vo.setAllWarnLv(num);//总体闭合率
            }
        }
        return bhzCementWarnVO;
    }

    @AutoLog(value = "拌合站主表-拌合站预警")
    @ApiOperation(value = "拌合站主表-拌合站预警", notes = "拌合站主表-拌合站预警")
    @GetMapping(value = "/bhzWarn")
    public Result<?> bhzWarn(String orgCode) {
        if (orgCode == "" || orgCode == null) {
            orgCode = "A";
        }
        Map<Object, Object> map = new HashMap<>();
        BhzCementWarnVO bhzCementWarnVO = bhzCementBaseMapper.selectWranCount(orgCode);
        BhzLqWarnVO bhzLqWarnVO = bhzLqBasesService.selectWranCount(orgCode);
        BhzSwWarnVO bhzSwWarnVO = bhzSwBasesService.selectWranCount(orgCode);
        Integer allCount = 0;
        Integer allWarnCount = 0;
        Integer allBiheCount = 0;
        if (bhzCementWarnVO != null) {
            allCount = allCount + bhzCementWarnVO.getWarnCount();
            allWarnCount = allWarnCount + bhzCementWarnVO.getMiddleCount() + bhzCementWarnVO.getPrimaryCount() + bhzCementWarnVO.getAdvancedCount();
            allBiheCount = allBiheCount + bhzCementWarnVO.getPrimaryBiheCount() + bhzCementWarnVO.getMiddleBiheCount() + bhzCementWarnVO.getAdvancedBiheCount();

        }
        if (bhzLqWarnVO != null) {
            allCount = allCount + bhzLqWarnVO.getWarnCount();
            allWarnCount = allWarnCount + bhzLqWarnVO.getMiddleCount() + bhzLqWarnVO.getPrimaryCount() + bhzLqWarnVO.getAdvancedCount();
            allBiheCount = allBiheCount + bhzLqWarnVO.getPrimaryBiheCount() + bhzLqWarnVO.getMiddleBiheCount() + bhzLqWarnVO.getAdvancedBiheCount();

        }
        if (bhzSwWarnVO != null) {
            allCount = allCount + bhzSwWarnVO.getWarnCount();
            allWarnCount = allWarnCount + bhzSwWarnVO.getMiddleCount() + bhzSwWarnVO.getPrimaryCount() + bhzSwWarnVO.getAdvancedCount();
            allBiheCount = allBiheCount + bhzSwWarnVO.getPrimaryBiheCount() + bhzSwWarnVO.getMiddleBiheCount() + bhzSwWarnVO.getAdvancedBiheCount();
        }
        float hegeLv;
        float biheLv;
        if (allCount == 0) {
            hegeLv = 100;
        } else if (allCount > 0 && (allCount - allWarnCount) == 0) {
            hegeLv = 0;
        } else {
            hegeLv = ((float) (allCount - allWarnCount)) / ((float) allCount);
        }
        if (allWarnCount == 0) {
            biheLv = 100;
        } else if (allWarnCount > 0 && allBiheCount == 0) {
            biheLv = 0;
        } else {
            biheLv = ((float) allBiheCount) / ((float) allWarnCount);
        }
        map.put("hegeLv", hegeLv);
        map.put("biheLv", biheLv);

        return Result.OK(map);
    }

    @AutoLog(value = "拌合站主表-砼超标统计")
    @ApiOperation(value = "拌合站主表-砼超标统计", notes = "拌合站主表-砼超标统计")
    @GetMapping(value = "/cbtongji")
    public Result<?> cbtongji(
            BhzCementStatistics bhzCementStatistics,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            String sys_depart_orgcode,
            Integer type,
            HttpServletRequest req) {
        if (bhzCementStatistics.getShebeiNo() != null) {
            type = 3;
        }

        if (type == 1) {

            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            if (sys_depart_orgcode == null) {
                sys_depart_orgcode = loginUser.getOrgCode();
            }
            List<BhzCementBaseCbTongji> CbTongjiList = new ArrayList<>();
            Map map = new HashMap();
            //查询到他的设备编号

            List<String> shebeiNo = bhzCementBaseService.selectshebeiByCode(sys_depart_orgcode);
            String shebeiList = null;
            for (String s : shebeiNo) {
                if (s != null) {
                    shebeiList = shebeiList + "," + s;
                }

            }
            if (!"null".equals(shebeiList)) {
                bhzCementStatistics.setShebeiNo(shebeiList);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
            BhzCementBaseCbTongji bhzCementBaseCbTongji1 = cbTongji(bhzCementStatistics, req, sys_depart_orgcode);
            CbTongjiList.add(bhzCementBaseCbTongji1);
            return Result.ok(CbTongjiList);
        } else if (type == 2) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            if (sys_depart_orgcode == null) {
                sys_depart_orgcode = loginUser.getOrgCode();
            }
            List<BhzCementBaseCbTongji> CbTongjiList = new ArrayList<>();
            List<BhzCementBaseCbTongji> baseCbTongjiList = bhzCementBaseService.selectshebeiBybiaoduan(sys_depart_orgcode);
            for (BhzCementBaseCbTongji baseCbTongji : baseCbTongjiList) {

                String shebeiNo = baseCbTongji.getShebeiNo();
                if (!"null".equals(shebeiNo)) {
                    bhzCementStatistics.setShebeiNo(shebeiNo);
                } else {
                    bhzCementStatistics.setShebeiNo("空");
                }
                BhzCementBaseCbTongji cbTongji = cbTongji(bhzCementStatistics, req, sys_depart_orgcode);
                if (cbTongji != null) {
                    String nameByCode = bhzCementBaseService.selectNameByCode(baseCbTongji.getOrgCode());
                    String name = baseCbTongji.getName();
                    if (nameByCode != null) {
                        name = nameByCode + "-" + baseCbTongji.getName();
                    }
                    cbTongji.setName(name);
                    CbTongjiList.add(cbTongji);
                }
            }
            return Result.ok(CbTongjiList);
        } else if (type == 3) {
            List<BhzCementBaseCbTongji> baseCbTongjiList = new ArrayList<>();
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            if (sys_depart_orgcode == null) {
                sys_depart_orgcode = loginUser.getOrgCode();
            }
            Map map = new HashMap();
            if (bhzCementStatistics.getShebeiNo() == null) {
                //查询到他的设备编号
                List<String> shebeiNo = bhzCementBaseService.selectshebeiByCode(sys_depart_orgcode);
                String shebeiList = null;
                for (String s : shebeiNo) {
                    if (s != null) {
                        shebeiList = shebeiList + "," + s;
                    }

                }
                if (!"null".equals(shebeiList)) {
                    bhzCementStatistics.setShebeiNo(shebeiList);
                } else {
                    bhzCementStatistics.setShebeiNo("空");
                }
            }

            QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
            queryWrapper.select("sum(all_dish) all_dish," +
                    "sum(primary_dish) primary_dish," +
                    "sum(middle_dish) middle_dish," +
                    "sum(advanced_dish) advanced_dish," +
                    "sum(primary_handle) primary_handle," +
                    "sum(middle_handle) middle_handle," +
                    "sum(estimate_number) estimate_number," +
                    "sum(advanced_handle) advanced_handle," +
                    "shebei_no");
            String statisticsTime_begin = req.getParameter("statisticsTime_begin");
            String statisticsTime_end = req.getParameter("statisticsTime_end");
            if (statisticsTime_begin == null && statisticsTime_end == null) {
                Calendar cale = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                cale.add(Calendar.MONTH, -1);
//            cale.set(Calendar.DAY_OF_MONTH, 1);
                statisticsTime_begin = format.format(cale.getTime());
                statisticsTime_end = format.format(new Date());
                queryWrapper.gt("statistics_time", statisticsTime_begin);
                queryWrapper.lt("statistics_time", statisticsTime_end);
            }
            queryWrapper.groupBy("shebei_no");
//            Page<BhzCementStatistics> page = new Page<BhzCementStatistics>(pageNo, pageSize);
            List<BhzCementStatistics> bhzCementStatisticsList = bhzCementStatisticsService.list(queryWrapper);
            for (BhzCementStatistics one : bhzCementStatisticsList) {
                BhzCementBaseCbTongji bhzCementBaseCbTongji = new BhzCementBaseCbTongji();
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(one.getShebeiNo());
                if (selectshebeione != null) {
                    bhzCementBaseCbTongji.setName(selectshebeione.getSbname());
                }
                bhzCementBaseCbTongji.setAllCount(one.getAllDish());
                bhzCementBaseCbTongji.setZongchangliang(one.getEstimateNumber());
                //初级
                bhzCementBaseCbTongji.setPrimaryCount(one.getPrimaryDish());
                if (one.getAllDish() == 0) {
                    bhzCementBaseCbTongji.setPrimaryWarnLv((double) 100);
                } else if (one.getAllDish() > 0 && one.getPrimaryDish() == 0) {
                    bhzCementBaseCbTongji.setPrimaryWarnLv((double) 0);
                } else {
                    double i = (double) one.getPrimaryDish() / (double) one.getAllDish();
                    BigDecimal b = new BigDecimal(i * 100);
                    i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    bhzCementBaseCbTongji.setPrimaryWarnLv(i);
                }
                bhzCementBaseCbTongji.setPrimaryHandleCount(one.getPrimaryHandle());
                if (one.getPrimaryDish() == 0) {
                    bhzCementBaseCbTongji.setPrimaryHandleLv((double) 100);
                } else if (one.getPrimaryDish() > 0 && one.getPrimaryHandle() == 0) {
                    bhzCementBaseCbTongji.setPrimaryHandleLv((double) 0);
                } else {
                    double i = (double) one.getPrimaryHandle() / (double) one.getPrimaryDish();
                    BigDecimal b = new BigDecimal(i * 100);
                    i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    bhzCementBaseCbTongji.setPrimaryHandleLv(i);

                }

                //中级
                bhzCementBaseCbTongji.setMiddleCount(one.getMiddleDish());
                if (one.getAllDish() == 0) {
                    bhzCementBaseCbTongji.setMiddleWarnLv((double) 100);
                } else if (one.getAllDish() > 0 && one.getMiddleDish() == 0) {
                    bhzCementBaseCbTongji.setMiddleWarnLv((double) 0);
                } else {
                    double i = (double) one.getMiddleDish() / (double) one.getAllDish();
                    BigDecimal b = new BigDecimal(i * 100);
                    i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    bhzCementBaseCbTongji.setMiddleWarnLv(i);
                }

                bhzCementBaseCbTongji.setMiddleHandleCount(one.getMiddleHandle());
                if (one.getMiddleDish() == 0) {
                    bhzCementBaseCbTongji.setMiddleHandleLv((double) 100);
                } else if (one.getMiddleDish() > 0 && one.getMiddleHandle() == 0) {
                    bhzCementBaseCbTongji.setMiddleHandleLv((double) 0);
                } else {
                    double i = (double) one.getMiddleHandle() / (double) one.getMiddleDish();
                    BigDecimal b = new BigDecimal(i * 100);
                    i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    bhzCementBaseCbTongji.setMiddleHandleLv(i);
                }
                //高级
                bhzCementBaseCbTongji.setAdvancedCount(one.getAdvancedDish());
                if (one.getAllDish() == 0) {
                    bhzCementBaseCbTongji.setAdvancedWarnLv((double) 100);
                } else if (one.getAllDish() > 0 && one.getAdvancedDish() == 0) {
                    bhzCementBaseCbTongji.setAdvancedWarnLv((double) 0);
                } else {
                    double i = (double) one.getAdvancedDish() / (double) one.getAllDish();
                    BigDecimal b = new BigDecimal(i * 100);
                    i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    bhzCementBaseCbTongji.setAdvancedWarnLv(i);
                }

                bhzCementBaseCbTongji.setAdvancedHandleCount(one.getAdvancedHandle());
                if (one.getAdvancedDish() == 0) {
                    bhzCementBaseCbTongji.setAdvancedHandleLv((double) 100);
                } else if (one.getAdvancedDish() > 0 && one.getAdvancedHandle() == 0) {
                    bhzCementBaseCbTongji.setAdvancedHandleLv((double) 0);
                } else {
                    double i = (double) one.getAdvancedHandle() / (double) one.getAdvancedDish();
                    BigDecimal b = new BigDecimal(i * 100);
                    i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    bhzCementBaseCbTongji.setAdvancedHandleLv(i);
                }
                baseCbTongjiList.add(bhzCementBaseCbTongji);
            }
//            map.put("result", baseCbTongjiList);
//            map.put("pageNo", bhzCementStatisticsList.getCurrent());
//            map.put("pageSize", bhzCementStatisticsList.getSize());
//            map.put("total", bhzCementStatisticsList.getTotal());
            return Result.ok(baseCbTongjiList);
        }
        return Result.error("错误");
    }

    //统计数据
    public BhzCementBaseCbTongji cbTongji(BhzCementStatistics bhzCementStatistics, HttpServletRequest req,
                                          String sys_depart_orgcode) {
        BhzCementBaseCbTongji bhzCementBaseCbTongji = new BhzCementBaseCbTongji();
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_dish) all_dish," +
                "sum(primary_dish) primary_dish," +
                "sum(middle_dish) middle_dish," +
                "sum(advanced_dish) advanced_dish," +
                "sum(primary_handle) primary_handle," +
                "sum(middle_handle) middle_handle," +
                "sum(estimate_number) estimate_number," +
                "sum(advanced_handle) advanced_handle");
        String statisticsTime_begin = req.getParameter("statisticsTime_begin");
        String statisticsTime_end = req.getParameter("statisticsTime_end");
        if (statisticsTime_begin == null && statisticsTime_end == null) {
            Calendar cale = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            cale.add(Calendar.MONTH, -1);
//            cale.set(Calendar.DAY_OF_MONTH, 1);
            statisticsTime_begin = format.format(cale.getTime());
            statisticsTime_end = format.format(new Date());
            queryWrapper.gt("statistics_time", statisticsTime_begin);
            queryWrapper.lt("statistics_time", statisticsTime_end);
        }
        BhzCementStatistics one = bhzCementStatisticsService.getOne(queryWrapper);
        if (one == null) {
            return null;
        }
        String selectbysysorgcode = bhzCementBaseMapper.selectbysysorgcode(sys_depart_orgcode);
        if (selectbysysorgcode != null) {
            bhzCementBaseCbTongji.setName(selectbysysorgcode);
        }
        bhzCementBaseCbTongji.setAllCount(one.getAllDish());
        bhzCementBaseCbTongji.setZongchangliang(one.getEstimateNumber());
        //初级
        bhzCementBaseCbTongji.setPrimaryCount(one.getPrimaryDish());
        if (one.getAllDish() == 0) {
            bhzCementBaseCbTongji.setPrimaryWarnLv((double) 100);
        } else if (one.getAllDish() > 0 && one.getPrimaryDish() == 0) {
            bhzCementBaseCbTongji.setPrimaryWarnLv((double) 0);
        } else {
            double i = (double) one.getPrimaryDish() / (double) one.getAllDish();
            BigDecimal b = new BigDecimal(i * 100);
            i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            bhzCementBaseCbTongji.setPrimaryWarnLv(i);
        }
        bhzCementBaseCbTongji.setPrimaryHandleCount(one.getPrimaryHandle());
        if (one.getPrimaryDish() == 0) {
            bhzCementBaseCbTongji.setPrimaryHandleLv((double) 100);
        } else if (one.getPrimaryDish() > 0 && one.getPrimaryHandle() == 0) {
            bhzCementBaseCbTongji.setPrimaryHandleLv((double) 0);
        } else {
            double i = (double) one.getPrimaryHandle() / (double) one.getPrimaryDish();
            BigDecimal b = new BigDecimal(i * 100);
            i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            bhzCementBaseCbTongji.setPrimaryHandleLv(i);

        }

        //中级
        bhzCementBaseCbTongji.setMiddleCount(one.getMiddleDish());
        if (one.getAllDish() == 0) {
            bhzCementBaseCbTongji.setMiddleWarnLv((double) 100);
        } else if (one.getAllDish() > 0 && one.getMiddleDish() == 0) {
            bhzCementBaseCbTongji.setMiddleWarnLv((double) 0);
        } else {
            double i = (double) one.getMiddleDish() / (double) one.getAllDish();
            BigDecimal b = new BigDecimal(i * 100);
            i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            bhzCementBaseCbTongji.setMiddleWarnLv(i);
        }

        bhzCementBaseCbTongji.setMiddleHandleCount(one.getMiddleHandle());
        if (one.getMiddleDish() == 0) {
            bhzCementBaseCbTongji.setMiddleHandleLv((double) 100);
        } else if (one.getMiddleDish() > 0 && one.getMiddleHandle() == 0) {
            bhzCementBaseCbTongji.setMiddleHandleLv((double) 0);
        } else {
            double i = (double) one.getMiddleHandle() / (double) one.getMiddleDish();
            BigDecimal b = new BigDecimal(i * 100);
            i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            bhzCementBaseCbTongji.setMiddleHandleLv(i);
        }
        //高级
        bhzCementBaseCbTongji.setAdvancedCount(one.getAdvancedDish());
        if (one.getAllDish() == 0) {
            bhzCementBaseCbTongji.setAdvancedWarnLv((double) 100);
        } else if (one.getAllDish() > 0 && one.getAdvancedDish() == 0) {
            bhzCementBaseCbTongji.setAdvancedWarnLv((double) 0);
        } else {
            double i = (double) one.getAdvancedDish() / (double) one.getAllDish();
            BigDecimal b = new BigDecimal(i * 100);
            i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            bhzCementBaseCbTongji.setAdvancedWarnLv(i);
        }

        bhzCementBaseCbTongji.setAdvancedHandleCount(one.getAdvancedHandle());
        if (one.getAdvancedDish() == 0) {
            bhzCementBaseCbTongji.setAdvancedHandleLv((double) 100);
        } else if (one.getAdvancedDish() > 0 && one.getAdvancedHandle() == 0) {
            bhzCementBaseCbTongji.setAdvancedHandleLv((double) 0);
        } else {
            double i = (double) one.getAdvancedHandle() / (double) one.getAdvancedDish();
            BigDecimal b = new BigDecimal(i * 100);
            i = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            bhzCementBaseCbTongji.setAdvancedHandleLv(i);
        }


        return bhzCementBaseCbTongji;
    }

    @AutoLog(value = "拌合站主表-砼材料超标统计")
    @ApiOperation(value = "拌合站主表-砼材料超标统计", notes = "拌合站主表-砼材料超标统计")
    @GetMapping(value = "/cbdetailtongji")
    public Result<?> cbdetailtongji(
            com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics bhzCementDetailStatistics,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            String sys_depart_orgcode,
            Integer type,
            HttpServletRequest req) {
        String statisticsTime_begin = req.getParameter("statisticsTime_begin");
        String statisticsTime_end = req.getParameter("statisticsTime_end");
        if (statisticsTime_begin == null && statisticsTime_end == null) {
            Calendar cale = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            cale.add(Calendar.MONTH, -1);
//            cale.set(Calendar.DAY_OF_MONTH, 1);
            statisticsTime_begin = format.format(cale.getTime());
            statisticsTime_end = format.format(new Date());
        }
        if (bhzCementDetailStatistics.getShebeiNo() != null) {
            type = 3;
        }
        if (type == 3) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            if (sys_depart_orgcode == null) {
                sys_depart_orgcode = loginUser.getOrgCode();
            }
            List<BhzCementDetailCbTongji> cbTongjiList = new ArrayList<>();
            Map map = new HashMap();
            String shebeiNo = null;
            if (bhzCementDetailStatistics.getShebeiNo() == null) {
                //查询到他的设备编号
                List<String> stringList = bhzCementBaseService.selectshebeiByCode(sys_depart_orgcode);
                for (String s : stringList) {
                    if (s != null) {
                        shebeiNo = shebeiNo + "," + s;
                    }

                }

            } else {
                shebeiNo = bhzCementDetailStatistics.getShebeiNo();
            }
            List<String> deviceList = new ArrayList<>();
            String[] devices = shebeiNo.split(",");
            if (shebeiNo != null && devices.length > 0) {
                for (String str : devices
                ) {
                    deviceList.add(str);
                }
            }
//            QueryWrapper<com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementDetailStatistics, req.getParameterMap());
//            queryWrapper.select("sum(primary_number) as primary_number,sum(middle_number) as middle_number,sum(advanced_number)as advanced_number,materiale_type,shebei_no");
//            if (req.getParameter("statisticsTime_begin") == null && req.getParameter("statisticsTime_end") == null) {
//                queryWrapper.gt("statistics_time", statisticsTime_begin);
//                queryWrapper.lt("statistics_time", statisticsTime_end);
//            }
//            queryWrapper.groupBy("shebei_no,materiale_type");

//            List<BhzCementDetailStatistics> detailStatistics = bhzCementDetailStatisticsService.list(queryWrapper);
            List<BhzCementDetailStatistics> detailStatistics = bhzCementDetailStatisticsService.selectShebeiDetail(statisticsTime_begin, statisticsTime_end, deviceList);
            if (detailStatistics != null) {
                cbTongjiList = cbDetailData(detailStatistics, req, sys_depart_orgcode);
            }

            return Result.ok(cbTongjiList);
        } else if (type == 1) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            if (sys_depart_orgcode == null) {
                sys_depart_orgcode = loginUser.getOrgCode();
            }
            String device = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
            Map map = new HashMap();
            //查询到他的设备编号
            List<String> shebeiNo = bhzCementBaseService.selectshebeiByCode(sys_depart_orgcode);
            String shebeiList = null;
            for (String s : shebeiNo) {
                if (s != null) {
                    shebeiList = shebeiList + "," + s;
                }

            }
            if (!"null".equals(shebeiList)) {
                bhzCementDetailStatistics.setShebeiNo(shebeiList);
            } else {
                bhzCementDetailStatistics.setShebeiNo("空");
            }

            QueryWrapper<com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementDetailStatistics, req.getParameterMap());
            queryWrapper.select("sum(primary_number) as primary_number,sum(middle_number) as middle_number,sum(advanced_number)as advanced_number,materiale_type,group_concat(shebei_no) as shebei_no");
            if (req.getParameter("statisticsTime_begin") == null && req.getParameter("statisticsTime_end") == null) {
                queryWrapper.gt("statistics_time", statisticsTime_begin);
                queryWrapper.lt("statistics_time", statisticsTime_end);
            }
            queryWrapper.groupBy("materiale_type");
            List<BhzCementDetailStatistics> detailStatistics = bhzCementDetailStatisticsService.list(queryWrapper);
            List<BhzCementDetailCbTongji> cbTongjiList = AllcbDetailData(detailStatistics, req, sys_depart_orgcode, shebeiList);
            return Result.ok(cbTongjiList);
        } else if (type == 2) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            if (sys_depart_orgcode == null) {
                sys_depart_orgcode = loginUser.getOrgCode();
            }
            List<BhzCementDetailCbTongji> cbTongjiLists = new ArrayList<>();
            List<BhzCementBaseCbTongji> baseCbTongjiList = bhzCementBaseService.selectshebeiBybiaoduan(sys_depart_orgcode);
            for (BhzCementBaseCbTongji baseCbTongji : baseCbTongjiList) {

                String shebeiNo = baseCbTongji.getShebeiNo();
                if (!"null".equals(shebeiNo)) {
                    bhzCementDetailStatistics.setShebeiNo(shebeiNo);
                } else {
                    bhzCementDetailStatistics.setShebeiNo("空");
                }
                QueryWrapper<com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementDetailStatistics, req.getParameterMap());
                queryWrapper.select("sum(primary_number) as primary_number,sum(middle_number) as middle_number,sum(advanced_number)as advanced_number,materiale_type,group_concat(shebei_no) as shebei_no");
                if (req.getParameter("statisticsTime_begin") == null && req.getParameter("statisticsTime_end") == null) {
                    queryWrapper.gt("statistics_time", statisticsTime_begin);
                    queryWrapper.lt("statistics_time", statisticsTime_end);
                }
                queryWrapper.groupBy("materiale_type");
                List<BhzCementDetailStatistics> detailStatistics = bhzCementDetailStatisticsService.list(queryWrapper);
                if (detailStatistics == null || detailStatistics.size() == 0) {
                    continue;
                }
                List<BhzCementDetailCbTongji> cbTongjiList = AllcbDetailData(detailStatistics, req, sys_depart_orgcode, shebeiNo);
                for (BhzCementDetailCbTongji bhzCementDetailCbTongji : cbTongjiList) {
                    String nameByCode = bhzCementBaseService.selectNameByCode(baseCbTongji.getOrgCode());
                    String name = baseCbTongji.getName();
                    if (nameByCode != null) {
                        name = nameByCode + "-" + baseCbTongji.getName();
                    }
                    bhzCementDetailCbTongji.setShebeiName(name);
                }
                cbTongjiLists.addAll(cbTongjiList);
            }

            return Result.ok(cbTongjiLists);
        }
        return null;

    }

    //匹配实体类与数据库返回数据
    public BhzCementDetailCbTongji match(Integer number, Integer materialeType, BhzCementDetailCbTongji cbTongji) {
        if (materialeType == 1) {
            cbTongji.setXiguliao(number);
        }
        if (materialeType == 2) {
            cbTongji.setXiguliao_advanced(number);
        }
        if (materialeType == 3) {
            cbTongji.setXiguliao_middle(number);
        }
        if (materialeType == 4) {
            cbTongji.setXiguliao_primary(number);
        }
        if (materialeType == 5) {
            cbTongji.setWater(number);
        }
        if (materialeType == 6) {
            cbTongji.setShuini(number);
        }
        if (materialeType == 7) {
            cbTongji.setKuangfen(number);
        }
        if (materialeType == 8) {
            cbTongji.setFenmeihui(number);
        }
        if (materialeType == 9) {
            cbTongji.setWaijiaji(number);
        }
        if (materialeType == 10) {
            cbTongji.setQita(number);
        }
        return cbTongji;
    }

    //设备材料统计数据
    public List<BhzCementDetailCbTongji> cbDetailData(List<BhzCementDetailStatistics> detailStatistics, HttpServletRequest req, String sys_depart_orgcode) {
        List<BhzCementDetailCbTongji> cbTongjiList = new ArrayList<>();
        String shebeiNo = null;
        BhzCementDetailCbTongji primary = new BhzCementDetailCbTongji();
        BhzCementDetailCbTongji middle = new BhzCementDetailCbTongji();
        BhzCementDetailCbTongji advanced = new BhzCementDetailCbTongji();
        for (BhzCementDetailStatistics detailStatistic : detailStatistics) {
            if (detailStatistic.getMaterialeType() != null) {
                if (detailStatistics.get(detailStatistics.size() - 1) == detailStatistic) {
                    BhzCementStatistics bhzCementStatistics = new BhzCementStatistics();
                    bhzCementStatistics.setShebeiNo(shebeiNo);
                    BhzCementBaseCbTongji bhzCementBaseCbTongji = cbTongji(bhzCementStatistics, req, sys_depart_orgcode);
                    if (bhzCementBaseCbTongji != null) {
                        int i = bhzCementBaseCbTongji.getAdvancedCount() + bhzCementBaseCbTongji.getMiddleCount() + bhzCementBaseCbTongji.getPrimaryCount();
                        if (bhzCementBaseCbTongji.getAllCount() == 0) {
                            primary.setChaobiaolv((double) 100);
                            middle.setChaobiaolv((double) 100);
                            advanced.setChaobiaolv((double) 100);
                        } else if (bhzCementBaseCbTongji.getAllCount() > 0 && i == 0) {
                            primary.setChaobiaolv((double) 0);
                            middle.setChaobiaolv((double) 0);
                            advanced.setChaobiaolv((double) 0);
                        } else {
                            double num = (double) i / (double) bhzCementBaseCbTongji.getAllCount();
                            primary.setChaobiaolv((double) Math.round(num * 100) / 100);
                            middle.setChaobiaolv((double) Math.round(num * 100) / 100);
                            advanced.setChaobiaolv((double) Math.round(num * 100) / 100);
                        }
                        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
                        if (selectshebeione != null) {
                            primary.setShebeiName(selectshebeione.getSbname());
                            middle.setShebeiName(selectshebeione.getSbname());
                            advanced.setShebeiName(selectshebeione.getSbname());
                        }
                        primary.setAllDish(bhzCementBaseCbTongji.getAllCount());
                        middle.setAllDish(bhzCementBaseCbTongji.getAllCount());
                        advanced.setAllDish(bhzCementBaseCbTongji.getAllCount());
                        primary.setAllOverDish(i);
                        middle.setAllOverDish(i);
                        advanced.setAllOverDish(i);
                        primary.setZongchangliang(bhzCementBaseCbTongji.getZongchangliang());
                        middle.setZongchangliang(bhzCementBaseCbTongji.getZongchangliang());
                        advanced.setZongchangliang(bhzCementBaseCbTongji.getZongchangliang());

                    }
                    cbTongjiList.add(primary);
                    cbTongjiList.add(middle);
                    cbTongjiList.add(advanced);
                    continue;

                } else if (!detailStatistic.getShebeiNo().equals(shebeiNo)) {
                    if (shebeiNo != null) {
                        BhzCementStatistics bhzCementStatistics = new BhzCementStatistics();
                        bhzCementStatistics.setShebeiNo(shebeiNo);
                        BhzCementBaseCbTongji bhzCementBaseCbTongji = cbTongji(bhzCementStatistics, req, sys_depart_orgcode);
                        if (bhzCementBaseCbTongji != null) {
                            int i = bhzCementBaseCbTongji.getAdvancedCount() + bhzCementBaseCbTongji.getMiddleCount() + bhzCementBaseCbTongji.getPrimaryCount();
                            if (bhzCementBaseCbTongji.getAllCount() == 0) {
                                primary.setChaobiaolv((double) 100);
                                middle.setChaobiaolv((double) 100);
                                advanced.setChaobiaolv((double) 100);
                            } else if (bhzCementBaseCbTongji.getAllCount() > 0 && i == 0) {
                                primary.setChaobiaolv((double) 0);
                                middle.setChaobiaolv((double) 0);
                                advanced.setChaobiaolv((double) 0);
                            } else {
                                double num = (double) i / (double) bhzCementBaseCbTongji.getAllCount();
                                primary.setChaobiaolv((double) Math.round(num * 100) / 100);
                                middle.setChaobiaolv((double) Math.round(num * 100) / 100);
                                advanced.setChaobiaolv((double) Math.round(num * 100) / 100);
                            }
                            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
                            if (selectshebeione != null) {
                                primary.setShebeiName(selectshebeione.getSbname());
                                middle.setShebeiName(selectshebeione.getSbname());
                                advanced.setShebeiName(selectshebeione.getSbname());
                            }
                            primary.setAllDish(bhzCementBaseCbTongji.getAllCount());
                            middle.setAllDish(bhzCementBaseCbTongji.getAllCount());
                            advanced.setAllDish(bhzCementBaseCbTongji.getAllCount());
                            primary.setAllOverDish(i);
                            middle.setAllOverDish(i);
                            advanced.setAllOverDish(i);
                            primary.setZongchangliang(bhzCementBaseCbTongji.getZongchangliang());
                            middle.setZongchangliang(bhzCementBaseCbTongji.getZongchangliang());
                            advanced.setZongchangliang(bhzCementBaseCbTongji.getZongchangliang());

                        }
                        cbTongjiList.add(primary);
                        cbTongjiList.add(middle);
                        cbTongjiList.add(advanced);
                        shebeiNo = detailStatistic.getShebeiNo();
                        primary = new BhzCementDetailCbTongji();
                        primary.setLevel(1);
                        middle = new BhzCementDetailCbTongji();
                        middle.setLevel(2);
                        advanced = new BhzCementDetailCbTongji();
                        advanced.setLevel(3);
                    } else {
                        shebeiNo = detailStatistic.getShebeiNo();
                        primary = new BhzCementDetailCbTongji();
                        primary.setLevel(1);
                        middle = new BhzCementDetailCbTongji();
                        middle.setLevel(2);
                        advanced = new BhzCementDetailCbTongji();
                        advanced.setLevel(3);
                    }
                }
                BhzCementDetailCbTongji primarymatch = match(detailStatistic.getPrimaryNumber(), detailStatistic.getMaterialeType(), primary);
                BhzCementDetailCbTongji middlematch = match(detailStatistic.getMiddleNumber(), detailStatistic.getMaterialeType(), middle);
                BhzCementDetailCbTongji advancedmatch = match(detailStatistic.getAdvancedNumber(), detailStatistic.getMaterialeType(), advanced);
                BeanUtils.copyProperties(primary, primarymatch);
                BeanUtils.copyProperties(middle, middlematch);
                BeanUtils.copyProperties(advanced, advancedmatch);

            }
        }
        return cbTongjiList;
    }

    //全体、标段统计数据
    public List<BhzCementDetailCbTongji> AllcbDetailData(List<BhzCementDetailStatistics> detailStatistics, HttpServletRequest req, String sys_depart_orgcode, String shebeiNo) {
        BhzCementDetailCbTongji primary = new BhzCementDetailCbTongji();
        primary.setLevel(1);
        BhzCementDetailCbTongji middle = new BhzCementDetailCbTongji();
        middle.setLevel(2);
        BhzCementDetailCbTongji advanced = new BhzCementDetailCbTongji();
        advanced.setLevel(3);
        List<BhzCementDetailCbTongji> cbTongjiList = new ArrayList<>();
        for (BhzCementDetailStatistics detailStatistic : detailStatistics) {
            if (detailStatistic.getMaterialeType() != null) {
                BhzCementDetailCbTongji primarymatch = match(detailStatistic.getPrimaryNumber(), detailStatistic.getMaterialeType(), primary);
                BhzCementDetailCbTongji middlematch = match(detailStatistic.getMiddleNumber(), detailStatistic.getMaterialeType(), middle);
                BhzCementDetailCbTongji advancedmatch = match(detailStatistic.getAdvancedNumber(), detailStatistic.getMaterialeType(), advanced);
                BeanUtils.copyProperties(primary, primarymatch);
                BeanUtils.copyProperties(middle, middlematch);
                BeanUtils.copyProperties(advanced, advancedmatch);
            }
        }
        BhzCementStatistics bhzCementStatistics = new BhzCementStatistics();
        bhzCementStatistics.setShebeiNo(shebeiNo);
        BhzCementBaseCbTongji bhzCementBaseCbTongji = cbTongji(bhzCementStatistics, req, sys_depart_orgcode);
        if (bhzCementBaseCbTongji != null) {
            int i = bhzCementBaseCbTongji.getAdvancedCount() + bhzCementBaseCbTongji.getMiddleCount() + bhzCementBaseCbTongji.getPrimaryCount();
            if (bhzCementBaseCbTongji.getAllCount() == 0) {
                primary.setChaobiaolv((double) 100);
                middle.setChaobiaolv((double) 100);
                advanced.setChaobiaolv((double) 100);
            } else if (bhzCementBaseCbTongji.getAllCount() > 0 && i == 0) {
                primary.setChaobiaolv((double) 0);
                middle.setChaobiaolv((double) 0);
                advanced.setChaobiaolv((double) 0);
            } else {
                double num = (double) i / (double) bhzCementBaseCbTongji.getAllCount();
                primary.setChaobiaolv((double) Math.round(num * 100) / 100);
                middle.setChaobiaolv((double) Math.round(num * 100) / 100);
                advanced.setChaobiaolv((double) Math.round(num * 100) / 100);
            }
            primary.setShebeiName(bhzCementBaseCbTongji.getName());
            middle.setShebeiName(bhzCementBaseCbTongji.getName());
            advanced.setShebeiName(bhzCementBaseCbTongji.getName());
            primary.setAllDish(bhzCementBaseCbTongji.getAllCount());
            middle.setAllDish(bhzCementBaseCbTongji.getAllCount());
            advanced.setAllDish(bhzCementBaseCbTongji.getAllCount());
            primary.setAllOverDish(i);
            middle.setAllOverDish(i);
            advanced.setAllOverDish(i);
            primary.setZongchangliang(bhzCementBaseCbTongji.getZongchangliang());
            middle.setZongchangliang(bhzCementBaseCbTongji.getZongchangliang());
            advanced.setZongchangliang(bhzCementBaseCbTongji.getZongchangliang());
        }
        cbTongjiList.add(primary);
        cbTongjiList.add(middle);
        cbTongjiList.add(advanced);
        return cbTongjiList;
    }

    /**
     * 混凝土拌合站首页统计总数以及合格超标数据以及本月超标率
     *
     * @param bhzCementBase
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站首页统计")
    @ApiOperation(value = "混凝土拌合站首页统计", notes = "混凝土拌合站首页统计")
    @GetMapping(value = "/TongjiData")
    public Result<?> TongjiData(BhzCementBase bhzCementBase, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
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
//        try {
//            parse = format.parse(day1);
//            parse1 = format.parse(day2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzCementBase.setShebeiNo(shebei);
        } else {
            bhzCementBase.setShebeiNo("空");
        }
        //总数
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.select("count(*) as id");
        BhzCementBase bhzCementBase1 = bhzCementBaseService.getOne(queryWrapper);
        //超标总数
        QueryWrapper<BhzCementBase> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper1.select("count(*) as id");
        queryWrapper1.gt("over_level", 0);
        BhzCementBase bhzCementBase2 = bhzCementBaseService.getOne(queryWrapper1);
        //月超标总数
        QueryWrapper<BhzCementBase> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper2.select("count(*) as id");
        queryWrapper2.gt("over_level", 0);
        queryWrapper2.ge("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day1);
        queryWrapper2.le("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day2);
        BhzCementBase bhzCementBase3 = bhzCementBaseService.getOne(queryWrapper2);
        //月闭合数
        QueryWrapper<BhzCementBase> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper3.select("count(*) as id");
        queryWrapper3.gt("over_level", 0);
        queryWrapper2.eq("overproof_status", 20);
        queryWrapper3.ge("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day1);
        queryWrapper3.le("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day2);
        BhzCementBase bhzCementBase4 = bhzCementBaseService.getOne(queryWrapper3);
        //月总数
        QueryWrapper<BhzCementBase> queryWrapper4 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper4.select("count(*) as id");
        queryWrapper4.ge("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day1);
        queryWrapper4.le("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day2);
        BhzCementBase bhzCementBase5 = bhzCementBaseService.getOne(queryWrapper4);
        Integer hntsum = 0;
        Integer hntcb = 0;
        Double hntcblv = 0.0;
        Integer hntysum = 0;
        Integer hntycb = 0;
        Double hntycblv = 0.0;
        double hntbhY = 0.0;
        double hntbhlvY = 0.0;
        if (bhzCementBase1 != null) {
            hntsum = bhzCementBase1.getId();
        }
        if (bhzCementBase2 != null) {
            hntcb = bhzCementBase2.getId();
        }
        if (bhzCementBase5 != null) {
            hntysum = bhzCementBase5.getId();
        }
        if (bhzCementBase3 != null) {
            hntycb = bhzCementBase3.getId();
        }


        Double huncblv = ((double) hntcb / (double) hntsum) * 100;//总的超标率
        Double hunylv = ((double) hntycb / (double) hntysum) * 100;//当前月的超标率

        if (queryWrapper4 != null) {
            Integer id = bhzCementBase4.getId();
            if (hntycb != 0) {
                hntbhlvY = ((double) id / (double) hntycb) * 100;//当前月闭合率
            }
        }

        if (huncblv > 0) {
            BigDecimal b = new BigDecimal(huncblv);
            hntcblv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if (hunylv > 0) {
            BigDecimal b1 = new BigDecimal(hunylv);
            hntycblv = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        map.put("hntcblv", hntcblv);
        map.put("hntsum", hntsum);
        map.put("hntcb", hntcb);
        map.put("hntycblv", hntycblv);
        map.put("hntbhlvY", hntbhlvY);
        return Result.OK(map);
    }

    /**
     * 混凝土拌合站详情表关联料仓查询
     *
     * @return batchNo=5dcddd3e-d243-4c0f-aea7-fe874abaae5d
     */
    @AutoLog(value = "混凝土拌合站详情表关联料仓查询")
    @ApiOperation(value = "混凝土拌合站详情表关联料仓查询", notes = "混凝土拌合站详情表关联料仓查询")
    @GetMapping(value = "/cementDetailAndStockBin")
    public Result<?> cementDetailAndStockBin(BhzCementDetail cementDetail, HttpServletRequest req) {
        String batchNo = cementDetail.getBatchNo();
        WZCaiLiao[] wzCaiLiaos = new WZCaiLiao[0];
        QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_no", batchNo);
        BhzCementBase cement = bhzCementBaseService.getOne(queryWrapper);
        String formulaNo = cement.getFormulaNo();
        String device = cement.getShebeiNo();

        // 获取配合比
        Shigongphb phb = phbService.getByFormulaNo(formulaNo, device);
        if(phb != null){
           wzCaiLiaos = arrCailiao(phb);
        }else{
            List<Shigongphb> queryones = phbService.queryones(formulaNo);
            if(queryones.size()>0){
                wzCaiLiaos = arrCailiao(queryones.get(0));
            }
        }
        // 获取拌合站子表材料
        QueryWrapper<BhzCementDetail> initQueryWrapper = QueryGenerator.initQueryWrapper(cementDetail, req.getParameterMap());
        List<BhzCementDetail> materials = bhzCementDetailService.list(initQueryWrapper);
        if (materials != null && materials.size() > 0 && wzCaiLiaos.length>1) {
            for (BhzCementDetail material : materials) {
                for( WZCaiLiao caiLiao : wzCaiLiaos){
                    // 进行对应
                    if(caiLiao.getGuige().equals(material.getMaterialeName())){
                        QueryWrapper<Wzliaocang> lc = new QueryWrapper<>();
                        lc.eq("guid",caiLiao.getLcbh());
                        Wzliaocang one = stockBinService.getOne(lc);
                        if(one!=null){
                            material.setBinName(one.getName());
                        }else{
                            material.setBinName(caiLiao.getLcbh());
                        }
                        material.setBatch(caiLiao.getPici());
                    }

                }
            }
        }
        return Result.OK(materials);
    }



    public WZCaiLiao[] arrCailiao(Shigongphb phb){
        if(phb != null){
            WZCaiLiao m1 = new WZCaiLiao();
            m1.setName("水泥1");
            m1.setGuige(phb.getM1());
            m1.setLcbh(phb.getLc1());
            m1.setPici(phb.getPici1());
            m1.setLlyongliang(String.format("%.2f",(phb.getU1()!= null?phb.getU1():0)));
            m1.setSjyongliang(String.format("%.2f",phb.getRu1()!= null?phb.getRu1():0));


            WZCaiLiao m13 = new WZCaiLiao();
            m13.setName("水泥2");
            m13.setGuige(phb.getM13());
            m13.setLcbh(phb.getLc13());
            m13.setPici(phb.getPici13());
            m13.setLlyongliang(String.format("%.2f",(phb.getU13()!= null?phb.getU13():0)));
            m13.setSjyongliang(String.format("%.2f",(phb.getRu13()!= null?phb.getRu13():0)));
//              m13.setHslv(String.valueOf(phb.getMc13()));
//              m13.setHsliang(String.valueOf(phb.getMcl13()));


            WZCaiLiao m2 = new WZCaiLiao();
            m2.setName("粉煤灰");
            m2.setGuige(phb.getM2());
            m2.setLcbh(phb.getLc2());
            m2.setPici(phb.getPici2());
            m2.setLlyongliang(String.format("%.2f",phb.getU2()!= null?phb.getU2():0));
            m2.setSjyongliang(String.format("%.2f",(phb.getRu2()!= null?phb.getRu2():0)));


            WZCaiLiao m3 = new WZCaiLiao();
            m3.setName("矿粉");
            m3.setGuige(phb.getM3());
            m3.setLcbh(phb.getLc3());
            m3.setPici(phb.getPici3());
            m3.setLlyongliang(String.format("%.2f",phb.getU3()!= null?phb.getU3():0));
            m3.setSjyongliang(String.format("%.2f",(phb.getRu3()!= null?phb.getRu3():0)));


            WZCaiLiao m4 = new WZCaiLiao();
            m4.setName("骨料1");
            m4.setGuige(phb.getM4());
            m4.setLcbh(phb.getLc4());
            m4.setPici(phb.getPici4());
            m4.setLlyongliang(String.format("%.2f",(phb.getU4()!= null?phb.getU4():0)));
            m4.setSjyongliang(String.format("%.2f",(phb.getRu4()!= null?phb.getRu4():0)));
            m4.setHslv( String.format("%.2f", phb.getMc4()!= null?phb.getMc4():0));
            m4.setHsliang(String.valueOf(Math.round( phb.getMcl4()!= null?phb.getMcl4():0)));

            WZCaiLiao m5 = new WZCaiLiao();
            m5.setName("骨料2");
            m5.setGuige(phb.getM5());
            m5.setLcbh(phb.getLc5());
            m5.setPici(phb.getPici5());
            m5.setLlyongliang(String.format("%.2f",(phb.getU5()!= null?phb.getU5():0)));
            m5.setSjyongliang(String.format("%.2f",(phb.getRu5()!= null?phb.getRu5():0)));
            m5.setHslv(String.format("%.2f", phb.getMc5()!= null?phb.getMc5():0));
            m5.setHsliang(String.valueOf(Math.round( phb.getMcl5()!= null?phb.getMcl5():0)));

            WZCaiLiao m6 = new WZCaiLiao();
            m6.setName("骨料3");
            m6.setGuige(phb.getM6());
            m6.setLcbh(phb.getLc6());
            m6.setPici(phb.getPici6());
            m6.setLlyongliang(String.format("%.2f",(phb.getU6()!= null?phb.getU6():0)));
            m6.setSjyongliang(String.format("%.2f",(phb.getRu6()!= null?phb.getRu6():0)));
            m6.setHslv(String.format("%.2f", phb.getMc6()!= null?phb.getMc6():0));
            m6.setHsliang(String.valueOf(Math.round( phb.getMcl6()!= null?phb.getMcl6():0)));

            WZCaiLiao m7 = new WZCaiLiao();
            m7.setName("骨料4");
            m7.setGuige(phb.getM7());
            m7.setLcbh(phb.getLc7());
            m7.setPici(phb.getPici7());
            m7.setLlyongliang(String.format("%.2f",(phb.getU7()!= null?phb.getU7():0)));
            m7.setSjyongliang(String.format("%.2f",(phb.getRu7()!= null?phb.getRu7():0)));
            m7.setHslv(String.format("%.2f", phb.getMc7()!= null?phb.getMc7():0));
            m7.setHsliang(String.valueOf(Math.round( phb.getMcl7()!= null?phb.getMcl7():0)));

            WZCaiLiao m12 = new WZCaiLiao();
            m12.setName("骨料5");
            m12.setGuige(phb.getM12());
            m12.setLcbh(phb.getLc12());
            m12.setPici(phb.getPici12());
            m12.setLlyongliang(String.format("%.2f",(phb.getU12()!= null?phb.getU12():0)));
            m12.setSjyongliang(String.format("%.2f",(phb.getRu12()!= null?phb.getRu12():0)));
            m12.setHslv(String.format("%.2f", phb.getMc12()!= null?phb.getMc12():0));
            m12.setHsliang(String.valueOf(Math.round( phb.getMcl12()!= null?phb.getMcl12():0)));


            WZCaiLiao m8 = new WZCaiLiao();
            m8.setName("外加剂1");
            m8.setGuige(phb.getM8());
            m8.setLcbh(phb.getLc8());
            m8.setPici(phb.getPici8());
            m8.setLlyongliang(String.valueOf(phb.getU8()!= null?phb.getU8():0));
            m8.setSjyongliang(String.valueOf(phb.getRu8()!= null?phb.getRu8():0));
            m8.setHslv(String.format("%.2f", phb.getMc8()!= null?phb.getMc8():0));
            m8.setHsliang(String.format("%.2f", phb.getMcl8()!= null?phb.getMcl8():0));

            WZCaiLiao m9 = new WZCaiLiao();
            m9.setName("外加剂2");
            m9.setGuige(phb.getM9());
            m9.setLcbh(phb.getLc9());
            m9.setPici(phb.getPici9());
            m9.setLlyongliang(String.valueOf(phb.getU9()!= null?phb.getU9():0));
            m9.setSjyongliang(String.valueOf(phb.getRu9()!= null?phb.getRu9():0));
            m9.setHslv(String.format("%.2f", phb.getMc9()!= null?phb.getMc9():0));
            m9.setHsliang(String.format("%.2f", phb.getMcl9()!= null?phb.getMcl9():0));

            WZCaiLiao m10 = new WZCaiLiao();
            m10.setName("外加剂3");
            m10.setGuige(phb.getM10());
            m10.setLcbh(phb.getLc10());
            m10.setPici(phb.getPici10());
            m10.setLlyongliang(String.valueOf(phb.getU10()!= null?phb.getU10():0));
            m10.setSjyongliang(String.valueOf(phb.getRu10()!= null?phb.getRu10():0));
            m10.setHslv(String.format("%.2f", phb.getMc10()!= null?phb.getMc10():0));
            m10.setHsliang(String.format("%.2f", phb.getMcl10()!= null?phb.getMcl10():0));

            WZCaiLiao m11 = new WZCaiLiao();
            m11.setName("水");
            m11.setGuige(phb.getM11());
            m11.setLcbh(phb.getLc11());
            m11.setPici(phb.getPici11());
            m11.setLlyongliang(String.valueOf(Math.round(phb.getU11()!= null?phb.getU11():0)));
            m11.setSjyongliang(String.valueOf(Math.round(phb.getRu11()!= null?phb.getRu11():0)));

            WZCaiLiao cailiao[] = {m1,m13,m2 ,m3 ,m4 ,m5 ,m6 ,m7 ,m12,m8 ,m9 ,m10,m11};
            List<WZCaiLiao> tmp = new ArrayList<WZCaiLiao>();
            for (WZCaiLiao str : cailiao) {
                if (StringUtil.isNotEmpty(str.getGuige())) {
                    tmp.add(str);
                }
            }
            cailiao = tmp.toArray(new WZCaiLiao[0]);
            return cailiao;

        }
        return new WZCaiLiao[0];
    }


    @AutoLog(value = "砼拌合站首页标段产能统计")
    @ApiOperation(value = "砼拌合站首页标段产能统计", notes = "砼拌合站首页标段产能统计")
    @GetMapping(value = "/listbybd")
    public Result<?> queryPageListbybd(BhzCementStatistics statistics, HttpServletRequest req, String sys_depart_orgcode, String statisticsTime_begin, String statisticsTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode == null) {
            sys_depart_orgcode = loginUser.getOrgCode();
        }
        List list = new ArrayList<>();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        List<BhzCementBaseCbTongji> baseCbTongjiList = bhzCementBaseService.selectshebeiBybiaoduan(sys_depart_orgcode);
        for (BhzCementBaseCbTongji bhzCementBaseCbTongji : baseCbTongjiList) {
            Map map = new HashMap();
            if (bhzCementBaseCbTongji.getShebeiNo() != null) {
                statistics.setShebeiNo(bhzCementBaseCbTongji.getShebeiNo());
            }
            QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(statistics, req.getParameterMap());
            queryWrapper.select(
                    "ifnull(sum(all_dish),0) as all_dish", "ifnull(sum(primary_dish),0) as primary_dish",
                    "ifnull(sum(middle_dish),0) as middle_dish", "ifnull(sum(advanced_dish),0) as advanced_dish",
                    "ifnull(sum(estimate_number),0) as estimate_number", "ifnull(sum(all_overproof_dish),0) as all_overproof_dish",
                    "statistics_time");
            if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
                try {
                    Date parse = ft.parse(statisticsTime_begin);
                    Date parse1 = ft.parse(statisticsTime_end);
                    queryWrapper.ge("statistics_time", parse);
                    queryWrapper.le("statistics_time", parse1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            BhzCementStatistics one = bhzCementStatisticsService.getOne(queryWrapper);
            if (one.getAllDish() == 0) {
                continue;
            }
            double allDish = one.getAllDish();
            double primaryDish = one.getPrimaryDish();
            double middleDish = one.getMiddleDish();
            double advancedDish = one.getAdvancedDish();
            double estimateNumber = one.getEstimateNumber();
            double allOverproofDish = one.getAllOverproofDish();
            double hegeDish = allDish - allOverproofDish;
            double primarylv = Double.parseDouble(String.format("%.2f", (primaryDish / allDish) * 100));
            double middlelv = Double.parseDouble(String.format("%.2f", (middleDish / allDish) * 100));
            double advancedlv = Double.parseDouble(String.format("%.2f", (advancedDish / allDish) * 100));
            double hegelv = Double.parseDouble(String.format("%.2f", (hegeDish / allDish) * 100));
            String nameByCode = bhzCementBaseService.selectNameByCode(bhzCementBaseCbTongji.getOrgCode());
            String Name = bhzCementBaseCbTongji.getName();
            if (nameByCode != null) {
                Name = nameByCode + "-" + bhzCementBaseCbTongji.getName();
            }
            map.put("name", Name);
            map.put("estimateNumber", estimateNumber);
            map.put("primarylv", primarylv);
            map.put("middlelv", middlelv);
            map.put("advancedlv", advancedlv);
            map.put("hegelv", hegelv);
            list.add(map);
        }
        return Result.ok(list);
    }

    @AutoLog(value = "砼拌合站首页设备产能统计")
    @ApiOperation(value = "砼拌合站首页设备产能统计", notes = "砼拌合站首页设备产能统计")
    @GetMapping(value = "/listbysb")
    public Result<?> queryPageListbysb(BhzCementStatistics statistics, HttpServletRequest req, String sys_depart_orgcode, String statisticsTime_begin, String statisticsTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode == null) {
            sys_depart_orgcode = loginUser.getOrgCode();
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        List list = new ArrayList<>();
        //查询到他的设备编号
        List<String> shebeiNo = bhzCementBaseService.selectshebeiByCode(sys_depart_orgcode);
        String shebeiList = null;
        for (String s : shebeiNo) {
            if (s != null) {
                shebeiList = shebeiList + "," + s;
            }
        }
        if (shebeiList != null) {
            statistics.setShebeiNo(shebeiList);
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(statistics, req.getParameterMap());
        queryWrapper.select(
                "ifnull(sum(all_dish),0) as all_dish", "ifnull(sum(primary_dish),0) as primary_dish",
                "ifnull(sum(middle_dish),0) as middle_dish", "ifnull(sum(advanced_dish),0) as advanced_dish",
                "ifnull(sum(estimate_number),0) as estimate_number", "ifnull(sum(all_overproof_dish),0) as all_overproof_dish",
                "shebei_no");
        if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
            try {
                Date parse = ft.parse(statisticsTime_begin);
                Date parse1 = ft.parse(statisticsTime_end);
                queryWrapper.ge("statistics_time", parse);
                queryWrapper.le("statistics_time", parse1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        queryWrapper.groupBy("shebei_no");
        List<BhzCementStatistics> bhzCementStatistics = bhzCementStatisticsService.list(queryWrapper);
        for (BhzCementStatistics one : bhzCementStatistics) {
            if (one.getAllDish() == 0) {
                continue;
            }
            Map map = new HashMap();
            double allDish = one.getAllDish();
            double primaryDish = one.getPrimaryDish();
            double middleDish = one.getMiddleDish();
            double advancedDish = one.getAdvancedDish();
            double estimateNumber = one.getEstimateNumber();
            double allOverproofDish = one.getAllOverproofDish();
            double hegeDish = allDish - allOverproofDish;
            double primarylv = Double.parseDouble(String.format("%.2f", (primaryDish / allDish) * 100));
            double middlelv = Double.parseDouble(String.format("%.2f", (middleDish / allDish) * 100));
            double advancedlv = Double.parseDouble(String.format("%.2f", (advancedDish / allDish) * 100));
            double hegelv = Double.parseDouble(String.format("%.2f", (hegeDish / allDish) * 100));
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(one.getShebeiNo());
            map.put("name", selectshebeione.getSbname());
            map.put("estimateNumber", estimateNumber);
            map.put("primarylv", primarylv);
            map.put("middlelv", middlelv);
            map.put("advancedlv", advancedlv);
            map.put("hegelv", hegelv);
            list.add(map);
        }

        return Result.ok(list);
    }

    @AutoLog(value = "砼拌合站生产情况分析")
    @ApiOperation(value = "砼拌合站生产情况分析", notes = "砼拌合站生产情况分析")
    @GetMapping(value = "/list15")
    public Result<?> queryPageList15(BhzCementStatistics statistics, HttpServletRequest req, String sys_depart_orgcode, String statisticsTime_begin, String statisticsTime_end) {
        List list = new ArrayList<>();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        List<BhzCementBaseCbTongji> baseCbTongjiList = bhzCementBaseService.selectshebeiBybiaoduan(sys_depart_orgcode);
        for (BhzCementBaseCbTongji bhzCementBaseCbTongji : baseCbTongjiList) {
            Map map = new HashMap();
            if (bhzCementBaseCbTongji.getShebeiNo() != null) {
                statistics.setShebeiNo(bhzCementBaseCbTongji.getShebeiNo());
            }
            QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(statistics, req.getParameterMap());
            queryWrapper.select(
                    "ifnull(sum(all_dish),0) as all_dish",
                    "ifnull(sum(estimate_number),0) as estimate_number", "ifnull(sum(all_overproof_dish),0) as all_overproof_dish",
                    "statistics_time");
            if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
                try {
                    Date parse = ft.parse(statisticsTime_begin);
                    Date parse1 = ft.parse(statisticsTime_end);
                    queryWrapper.ge("statistics_time", parse);
                    queryWrapper.le("statistics_time", parse1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            BhzCementStatistics one = bhzCementStatisticsService.getOne(queryWrapper);
            if (one.getAllDish() == 0) {
                continue;
            } else if (one.getAllDish() > 0 && one.getAllOverproofDish() == 0) {
                map.put("cblv", 0);
            } else {
                double i = (double) one.getAllOverproofDish() / (double) one.getAllDish();
                map.put("cblv", (double) Math.round(i * 100) / 100);
            }
            String nameByCode = bhzCementBaseService.selectNameByCode(bhzCementBaseCbTongji.getOrgCode());
            String Name = bhzCementBaseCbTongji.getName();
            if (nameByCode != null) {
                Name = nameByCode + "-" + bhzCementBaseCbTongji.getName();
            }
            List<ShebeiInfo> shebeiInfoList = shebeiInfoService.shebeilist(0, bhzCementBaseCbTongji.getOrgCode());
            int length = shebeiInfoList.size();
            map.put("shebeiNum", length);
            map.put("estimateNumber", one.getEstimateNumber());
            map.put("allDish", one.getAllDish());
            map.put("cbNum", one.getAllOverproofDish());
            map.put("name", Name);
            map.put("sjbNum", 0);
            map.put("sjbLv", 0);

            list.add(map);
        }
        return Result.ok(list);
    }

    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/productionList")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseListRC")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> productionList(BhzCementBase bhzCementBase,
                                    String sys_depart_project,
                                    Integer isBiHe,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (sys_depart_project != null) {
            shebei = bhzCementBaseMapper.selectSBByCode(sys_depart_project);
        }
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        if (isBiHe != null) {
            if (isBiHe == 1) {
                queryWrapper.eq("overproof_status", "20");
            } else if (isBiHe == 0) {
                queryWrapper.ne("overproof_status", "20");
                queryWrapper.ne("over_level", 0);
            }
        }
        queryWrapper.eq("isdel", 0);
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        List<BhzCementBase> records = pageList.getRecords();
        List<Map> mapList = new LinkedList<>();
        for (BhzCementBase record : records) {
            Map map = new HashMap();
            String shebeiNo = record.getShebeiNo();
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
            shebeiInfoQueryWrapper.eq("sbjno", shebeiNo);
            ShebeiInfo shebeiInfo = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
            String sbname = shebeiInfo.getSbname();
            String sysOrgCode = shebeiInfo.getSysOrgCode();
            String departName = bhzCementBaseMapper.selectbysysorgcode(sysOrgCode);
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.selectByBatchNo(record.getBatchNo());
            StringBuilder overParameter = new StringBuilder();
            StringBuilder overRatio = new StringBuilder();
            for (BhzCementDetail bhzCementDetail : bhzCementDetails) {
                if (bhzCementDetail.getMaterialeOverLevel() != null && bhzCementDetail.getMaterialeOverLevel() != 0) {
                    if ("".equals(overParameter.toString())) {
                        overParameter.append(bhzCementDetail.getMaterialeName());
                    } else {
                        overParameter.append("/").append(bhzCementDetail.getMaterialeName());
                    }
                    if ("".equals(overRatio.toString())) {
                        overRatio.append(bhzCementDetail.getOverValue()).append("%");
                    } else {
                        overRatio.append("/").append(bhzCementDetail.getOverValue()).append("%");
                    }
                }
            }
            if (record.getOverproofStatus() == 20) {
                BhzCementOverHandler bhzCementOverHandler = bhzCementOverHandlerService.selectlist(record.getBatchNo());
                if (bhzCementOverHandler != null) {
                    if (record.getOverLevel() == 1 && bhzCementOverHandler.getHandleTime() != null) {
                        map.put("bhTime", bhzCementOverHandler.getHandleTime());
                    } else if (record.getOverLevel() == 2 && bhzCementOverHandler.getSupervisorHandleTime() != null) {
                        map.put("bhTime", bhzCementOverHandler.getSupervisorHandleTime());
                    } else if (record.getOverLevel() == 3 && bhzCementOverHandler.getHandleTime() != null) {
                        map.put("bhTime", bhzCementOverHandler.getHandleTime());
                    }
                }
            }
            map.put("batchNo", record.getBatchNo());
            map.put("formulaNo", record.getFormulaNo());
            map.put("departName", departName);
            map.put("shebeiNo", sbname);
            map.put("productDatetime", record.getProductDatetime());
            map.put("projectName", record.getProjectName());
            map.put("poureLocation", record.getPoureLocation());
            map.put("strengthRank", record.getStrengthRank());
            map.put("overLevel", record.getOverLevel());
            map.put("overParameter", overParameter);
            map.put("overRatio", overRatio);
            map.put("overproofStatus", record.getOverproofStatus());
            map.put("collectDatetime", record.getCollectDatetime());
            // 是否正确使用浇筑令
            map.put("isjzl", record.getRenwudanstatus());

            mapList.add(map);
        }
        Map resultMap = new HashMap();
        resultMap.put("current", pageList.getCurrent());
        resultMap.put("size", pageList.getSize());
        resultMap.put("total", pageList.getTotal());
        resultMap.put("records", mapList);
        return Result.OK(resultMap);
    }


    /**
     * 混凝土拌合站首页统计总数以及合格超标数据以及本月超标率
     *
     * @param bhzCementBase
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站首页统计")
    @ApiOperation(value = "混凝土拌合站首页统计", notes = "混凝土拌合站首页统计")
    @GetMapping(value = "/TongjiData2")
    public Result<?> TongjiData2(BhzCementBase bhzCementBase, HttpServletRequest req, Integer type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String productDatetime_begin = null;
        String productDatetime_end = sdf.format(calendar.getTime());
        if (type == 1) {
            calendar.set(Calendar.DAY_OF_YEAR, 1);//1:本年第一天
            productDatetime_begin = sdf.format(calendar.getTime());
        } else if (type == 2) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
            productDatetime_begin = sdf.format(calendar.getTime());
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
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
//        try {
//            parse = format.parse(day1);
//            parse1 = format.parse(day2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzCementBase.setShebeiNo(shebei);
        } else {
            bhzCementBase.setShebeiNo("空");
        }
        //总数
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper.select("count(id) as id");
        if (productDatetime_begin != null) {
            queryWrapper.ge("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", productDatetime_begin);
            queryWrapper.le("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", productDatetime_end);
        }
        BhzCementBase bhzCementBase1 = bhzCementBaseService.getOne(queryWrapper);
        //超标总数
        QueryWrapper<BhzCementBase> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper1.select("count(id) as id");
        queryWrapper1.gt("over_level", 0);
        if (productDatetime_begin != null) {
            queryWrapper1.ge("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", productDatetime_begin);
            queryWrapper1.le("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", productDatetime_end);
        }
        BhzCementBase bhzCementBase2 = bhzCementBaseService.getOne(queryWrapper1);
        //月超标总数
        QueryWrapper<BhzCementBase> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper2.select("count(id) as id");
        queryWrapper2.gt("over_level", 0);
        queryWrapper2.ge("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day1);
        queryWrapper2.le("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day2);
        BhzCementBase bhzCementBase3 = bhzCementBaseService.getOne(queryWrapper2);
        //月闭合数
        QueryWrapper<BhzCementBase> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper3.select("count(id) as id");
        queryWrapper3.gt("over_level", 0);
        queryWrapper3.eq("overproof_status", 20);
        queryWrapper3.ge("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day1);
        queryWrapper3.le("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day2);
        BhzCementBase bhzCementBase4 = bhzCementBaseService.getOne(queryWrapper3);
        //月总数
        QueryWrapper<BhzCementBase> queryWrapper4 = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        queryWrapper4.select("count(id) as id");
        queryWrapper4.ge("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day1);
        queryWrapper4.le("(DATE_FORMAT(product_datetime,'%Y-%m-%d'))", day2);
        BhzCementBase bhzCementBase5 = bhzCementBaseService.getOne(queryWrapper4);
        Integer hntsum = 0;
        Integer hntcb = 0;
        Double hntcblv = 0.0;
        Integer hntysum = 0;
        Integer hntycb = 0;
        Double hntycblv = 0.0;
        double hntbhY = 0.0;
        double hntbhlvY = 0.0;
        if (bhzCementBase1 != null) {
            hntsum = bhzCementBase1.getId();
        }
        if (bhzCementBase2 != null) {
            hntcb = bhzCementBase2.getId();
        }
        if (bhzCementBase5 != null) {
            hntysum = bhzCementBase5.getId();
        }
        if (bhzCementBase3 != null) {
            hntycb = bhzCementBase3.getId();
        }


        Double huncblv = ((double) hntcb / (double) hntsum) * 100;//总的超标率
        Double hunylv = ((double) hntycb / (double) hntysum) * 100;//当前月的超标率

        if (queryWrapper4 != null) {
            Integer id = bhzCementBase4.getId();
            if (hntycb != 0) {
                hntbhlvY = ((double) id / (double) hntycb) * 100;//当前月闭合率
            }
        }

        if (huncblv > 0) {
            BigDecimal b = new BigDecimal(huncblv);
            hntcblv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if (hunylv > 0) {
            BigDecimal b1 = new BigDecimal(hunylv);
            hntycblv = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        map.put("hntcblv", hntcblv);
        map.put("hntsum", hntsum);
        map.put("hntcb", hntcb);
        map.put("hntycblv", hntycblv);
        map.put("hntbhlvY", hntbhlvY);
        return Result.OK(map);
    }

    /**
     * 混凝土拌合站首页中间部分月统计
     *
     * @param bhzCementStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站首页中间部分月统计")
    @ApiOperation(value = "混凝土拌合站首页中间部分月统计", notes = "混凝土拌合站首页中间部分月统计")
    @GetMapping(value = "/listMid")
    public Result<?> listMid(BhzCementStatistics bhzCementStatistics, HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end, Integer type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if (type == 1) {
            statisticsTime_end = sdf.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_YEAR, 1);//1:本年第一天
            statisticsTime_begin = sdf.format(calendar.getTime());
        } else if (type == 2) {
            statisticsTime_end = sdf.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
            statisticsTime_begin = sdf.format(calendar.getTime());
        } else if (type == 3) {
            statisticsTime_end = sdf.format(calendar.getTime());
            statisticsTime_begin = "0000-00-00";
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzCementStatistics.setShebeiNo(shebei);
        } else {
            bhzCementStatistics.setShebeiNo("空");
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_dish) as all_dish", "statistics_time");
        if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
            try {
                Date parse = ft.parse(statisticsTime_begin);
                Date parse1 = ft.parse(statisticsTime_end);
                queryWrapper.ge("statistics_time", statisticsTime_begin);
                queryWrapper.le("statistics_time", statisticsTime_end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            queryWrapper.last(" GROUP BY (SELECT DATE_FORMAT(statistics_time,'%m'))  ORDER BY (SELECT DATE_FORMAT(statistics_time,'%m'))");
        } else {
            queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%m'))");
        }

        List<BhzCementStatistics> bhzCementStatisticsList = bhzCementStatisticsService.list(queryWrapper);
        List list = new ArrayList();
        for (BhzCementStatistics statistics : bhzCementStatisticsList) {
            Map map = new HashMap();
            Date statisticsTime = statistics.getStatisticsTime();
            Integer allDish = statistics.getAllDish();
            String format3 = format.format(statisticsTime);
            map.put("statisticsTime", format3);
            map.put("allDish", allDish);
            list.add(map);
        }
        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param bhzCementBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/bhTimeList")
    @PermissionData(pageComponent = "bhz/hntbhz/BhzCementBaseBhTimeList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> bhTimeList(BhzCementBase bhzCementBase,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementBase.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        if (StringUtils.isNotBlank(bhzCementBase.getPoureLocation())) {
            bhzCementBase.setPoureLocation("*" + bhzCementBase.getPoureLocation() + "*");
        }
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
        Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
        IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    @AutoLog(value = "拌合站主表-浙高建混凝土拌合")
    @ApiOperation(value = "拌合站主表-浙高建混凝土拌合", notes = "拌合站主表-浙高建混凝土拌合")
    @GetMapping(value = "/ZGJHntBh")
    public Result<?> ZGJHntBh(BhzCementStatistics bhzCementStatistics,
                              HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }

        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_dish) as allDish,sum(all_overproof_dish) as allOverproofDish,shebei_no");
        queryWrapper.last("group by shebei_no");
        List<BhzCementStatistics> list = bhzCementStatisticsService.list(queryWrapper);
        List<Map> mapList = new LinkedList<>();
        for (BhzCementStatistics cementStatistics : list) {
            Map map = new HashMap();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(cementStatistics.getShebeiNo());
            if (selectshebeione == null) {
                continue;
            }
            map.put("shebeiName", selectshebeione.getSbname());
            map.put("allCount", cementStatistics.getAllDish());
            map.put("yjCount", cementStatistics.getAllOverproofDish());
            mapList.add(map);
        }
        return Result.OK(mapList);
    }

    /**
     * 混凝土拌合站材料消耗使用导出
     *
     * @param bhzCementBase
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-原材料用量导出")
    @ApiOperation(value = "拌合站主表-原材料用量导出", notes = "拌合站主表-原材料用量导出")
    @GetMapping(value = "/listUseInfo")
    public Result<?> listUseInfo(BhzCementBase bhzCementBase,//设备和时间
                                 @RequestParam(value = "shebeiNo", required = false) String shebeino,
                                 @RequestParam(value = "productDatetime_begin", required = false) String start,
                                 @RequestParam(value = "productDatetime_end", required = false) String end,
                                 HttpServletRequest req) {
        List data = new ArrayList<>();
        bhzCementBase.setShebeiNo(shebeino);
        if (ObjectUtil.isEmpty(end)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end = simpleDateFormat.format(date);
        }
        Integer id = 1;
        List<BhzCementTongJi> bhzCementTongJis = bhzCementBaseService.selectCaiLiaoUse(shebeino, start, end);
        for (BhzCementTongJi bhzCementTongJi : bhzCementTongJis) {
            BhzCementTongJi tongJiOne = new BhzCementTongJi();
            tongJiOne.setId(id);
            tongJiOne.setShebeiName(bhzCementTongJi.getShebeiName());
            tongJiOne.setCailiaoName(bhzCementTongJi.getCailiaoName());
            tongJiOne.setLilunUse(bhzCementTongJi.getLilunUse());
            tongJiOne.setRealUse(bhzCementTongJi.getRealUse());
            data.add(tongJiOne);
            id++;
        }
        return Result.OKs(data);
    }

    @AutoLog(value = "拌合站主表-原材料用量导出")
    @ApiOperation(value = "拌合站主表-原材料用量导出", notes = "拌合站主表-原材料用量导出")
    @GetMapping(value = "/listUseInfo1")
    public Result<?> listUseInfo1(BhzCementBase bhzCementBase,//设备和时间
                                  @RequestParam(value = "productDatetime_begin", required = false) String start,
                                  @RequestParam(value = "productDatetime_end", required = false) String end,
                                  HttpServletRequest req) {
        List data = new ArrayList<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (ObjectUtil.isEmpty(bhzCementBase.getShebeiNo())) {
            if (!"null".equals(shebei)) {
                bhzCementBase.setShebeiNo(shebei);
            } else {
                bhzCementBase.setShebeiNo("空");
            }
        }
        if (ObjectUtil.isEmpty(end)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end = simpleDateFormat.format(date);
        }
        List<String> shebeiList = new ArrayList<>();
        String[] split = bhzCementBase.getShebeiNo().split(",");
        shebeiList.addAll(Arrays.asList(split));

        Integer id = 1;
        for (String shebeiOne : shebeiList) {
            List<BhzCementTongJi> bhzCementTongJis = bhzCementBaseService.selectCaiLiaoUse(shebeiOne, start, end);
            for (int i = 0; i < bhzCementTongJis.size(); i++) {
                BhzCementTongJi tongJiOne = new BhzCementTongJi();
                tongJiOne.setId(id);
                tongJiOne.setShebeiName(bhzCementTongJis.get(i).getShebeiName());
                tongJiOne.setCailiaoName(bhzCementTongJis.get(i).getCailiaoName());
                tongJiOne.setLilunUse(bhzCementTongJis.get(i).getLilunUse());
                tongJiOne.setRealUse(bhzCementTongJis.get(i).getRealUse());
                data.add(tongJiOne);
                id++;
            }
        }
        return Result.OKs(data);
    }

    @AutoLog(value = "拌合站主表-原材料用量导出")
    @ApiOperation(value = "拌合站主表-原材料用量导出", notes = "拌合站主表-原材料用量导出")
    @GetMapping(value = "/listUseInfoDate")
    public Result<?> listUseInfoDate(@RequestParam(value = "productDatetime_begin", required = false) String start,
                                     @RequestParam(value = "productDatetime_end", required = false) String end) {
        List data = new ArrayList<>();
        if (ObjectUtil.isEmpty(end) || "undefined".equals(end)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end = simpleDateFormat.format(date);
        }
        BhzCementTongJi one = new BhzCementTongJi();
        one.setShebeiName(start + "~" + end);
        data.add(one);
        return Result.OKs(data);
    }

    @AutoLog(value = "拌合站主表-生产数据导出")
    @ApiOperation(value = "拌合站主表-生产数据导出", notes = "拌合站主表-生产数据导出")
    @RequestMapping(value = "/listProduce")//生产数据导出
    public ModelAndView listProduce(HttpServletRequest request,
                                    BhzCementBase bhzCementBase,
                                    @RequestParam(value = "productDatetime_begin", required = false) String start,
                                    @RequestParam(value = "productDatetime_end", required = false) String end,
                                    String sys_depart_orgcode) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //Step.2 获取导出数据
        List<BhzCementBase> queryList = bhzCementBaseService.list(queryWrapper);
        for (BhzCementBase cementBase : queryList) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(cementBase.getShebeiNo());
            cementBase.setShebeiNo(selectshebeione.getSbname());
        }
        if (ObjectUtil.isEmpty(end) || "undefined".equals(end)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end = simpleDateFormat.format(date);
        }
        // 过滤选中数据
        String selections = request.getParameter("selections");
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "拌合站生产数据列表");
        mv.addObject(NormalExcelConstants.CLASS, BhzCementBase.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拌合站生产数据列表", "时间范围:" + start + "~" + end, "拌合站主表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, queryList);
        return mv;
    }

    @AutoLog(value = "bhz_cement_base-强度等级查询")
    @ApiOperation(value = "bhz_cement_base-强度等级查询", notes = "bhz_cement_base-强度等级查询")
    @GetMapping(value = "/getQddjList")
    public Result<?> getQddjList(BhzCementBase bhzCementBase,
                                 String sys_depart_orgcode,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 HttpServletRequest req) {
        List<Map<String, Object>> pageList = bhzCementBaseService.getQddj(sys_depart_orgcode);
        for (int i = 0; i < pageList.size(); i++) {
            if (ObjectUtil.isEmpty(pageList.get(i))) {
                pageList.remove(i);
            }
        }
        return Result.OK(pageList);
    }

}
