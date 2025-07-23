package com.trtm.iot.cunliang.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.cunliang.entity.BeammanagementProcedure;
import com.trtm.iot.cunliang.service.IBeammanagementProcedureService;
import com.trtm.iot.cunliang.vo.cunliangProcedureConfigPage;
import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.trtm.iot.zhiliangtaizuocfg.service.IZhiliangTaizuoCfgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//import static com.sun.tools.corba.se.idl.constExpr.Expression.one;

/**
 * @Description: 存梁工序表信息
 * @Author: jeecg-boot
 * @Date: 2021-08-18
 * @Version: V1.0
 */
@Api(tags = "存梁工序表信息")
@RestController
@RequestMapping("/cunliang/beammanagementProcedure")
@Slf4j
public class BeammanagementProcedureController extends JeecgController<BeammanagementProcedure, IBeammanagementProcedureService> {
    @Autowired
    private IBeammanagementProcedureService beammanagementProcedureService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;
    @Autowired
    private IZhiliangGongxuService zhiliangGongxuService;
    @Autowired
    private IZhiliangTaizuoCfgService zhiliangTaizuoCfgService;

    /**
     * 分页列表查询
     *
     * @param beammanagementProcedure
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-分页列表查询")
    @ApiOperation(value = "存梁工序表信息-分页列表查询", notes = "存梁工序表信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BeammanagementProcedure beammanagementProcedure,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (beammanagementProcedure.getShebeino() == null) {
            if (shebei != null) {
                beammanagementProcedure.setShebeino(shebei);
            }
        }
        QueryWrapper<BeammanagementProcedure> queryWrapper = QueryGenerator.initQueryWrapper(beammanagementProcedure, req.getParameterMap());
        Page<BeammanagementProcedure> page = new Page<BeammanagementProcedure>(pageNo, pageSize);
        IPage<BeammanagementProcedure> pageList = beammanagementProcedureService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 存梁时查询当前台座是否有梁存在
     *
     * @param zhiliangGongxu
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-台座验证")
    @ApiOperation(value = "存梁工序表信息-台座验证", notes = "存梁工序表信息-台座验证")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(ZhiliangGongxu zhiliangGongxu,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        QueryWrapper<ZhiliangGongxu> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangGongxu, req.getParameterMap());
        ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper);
        if (one != null) {
            return Result.error("当前台座上有梁存在！");
        } else {
            if (zhiliangGongxu.getLiangceng() == 1) {
                QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("shebeino", zhiliangGongxu.getShebeino());
                queryWrapper1.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                queryWrapper1.eq("dangqianceng", zhiliangGongxu.getLiangceng());
                queryWrapper1.eq("status", 0);
                CunliangProcedureConfig one1 = cunliangProcedureConfigService.getOne(queryWrapper1);
                if (one1 != null) {
                    return Result.OK("可以存入");
                }
            } else if (zhiliangGongxu.getLiangceng() == 2) {
                //首先判断第一层是否已经存梁
                QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("shebeino", zhiliangGongxu.getShebeino());
                queryWrapper1.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                queryWrapper1.eq("dangqianceng", 1);
                queryWrapper1.eq("status", 1);
                CunliangProcedureConfig one1 = cunliangProcedureConfigService.getOne(queryWrapper1);
                //首先判断第二层是否没有存梁
                QueryWrapper<CunliangProcedureConfig> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("shebeino", zhiliangGongxu.getShebeino());
                queryWrapper2.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                queryWrapper2.eq("dangqianceng", zhiliangGongxu.getLiangceng() == 2);
                queryWrapper2.eq("status1", 0);
                CunliangProcedureConfig one2 = cunliangProcedureConfigService.getOne(queryWrapper2);
                if (one1 != null && one2 != null) {
                    return Result.OK("可以存入");
                } else if (one2 == null) {
                    return Result.error("当前台座上有梁存在！");
                } else {
                    return Result.error("请先从第一层开始存梁");
                }
            }
        }
        return Result.error("当前台座上有梁存在！");
    }

    /**
     * 存梁时查询当前台座是否有梁存在
     *
     * @param zhiliangGongxu
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-台座验证")
    @ApiOperation(value = "存梁工序表信息-台座验证", notes = "存梁工序表信息-台座验证")
    @GetMapping(value = "/listTz")
    public Result<?> queryPageListTz(ZhiliangGongxu zhiliangGongxu,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        if (zhiliangGongxu.getStatus() == 1) {
            QueryWrapper<ZhiliangGongxu> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangGongxu, req.getParameterMap());
            ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper);
            if (one != null) {
                return Result.error("当前台座上有梁存在！");
            } else {
                if (zhiliangGongxu.getLiangceng() == 1) {
                    QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("shebeino", zhiliangGongxu.getShebeino());
                    queryWrapper1.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                    queryWrapper1.eq("dangqianceng", zhiliangGongxu.getLiangceng());
                    queryWrapper1.eq("status", 0);
                    CunliangProcedureConfig one1 = cunliangProcedureConfigService.getOne(queryWrapper1);
                    if (one1 != null) {
                        boolean b = cunquliangBianJi(zhiliangGongxu);
                        if (b){
                            return Result.OK("可以存入");
                        }else {
                            return Result.error("请先从上层开始移梁！");
                        }
                    }
                } else if (zhiliangGongxu.getLiangceng() == 2) {
                    //首先判断第一层是否已经存梁
                    QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("shebeino", zhiliangGongxu.getShebeino());
                    queryWrapper1.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                    queryWrapper1.eq("dangqianceng", 1);
                    queryWrapper1.eq("status", 1);
                    CunliangProcedureConfig one1 = cunliangProcedureConfigService.getOne(queryWrapper1);
                    //首先判断第二层是否没有存梁
                    QueryWrapper<CunliangProcedureConfig> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("shebeino", zhiliangGongxu.getShebeino());
                    queryWrapper2.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                    queryWrapper2.eq("dangqianceng", zhiliangGongxu.getLiangceng() == 2);
                    queryWrapper2.eq("status1", 0);
                    CunliangProcedureConfig one2 = cunliangProcedureConfigService.getOne(queryWrapper2);
                    if (one1 != null && one2 != null) {
                        boolean b = cunquliangBianJi(zhiliangGongxu);
                        if (b){
                            return Result.OK("可以存入");
                        }else {
                            return Result.error("请先从上层开始移梁！");
                        }
                    } else if (one2 == null) {
                        return Result.error("当前台座上有梁存在！");
                    } else {
                        return Result.error("请先从第一层开始存梁");
                    }
                }
            }
            return Result.error("当前台座上有梁存在！");
        } else if (zhiliangGongxu.getStatus() == 2) {
            zhiliangGongxu.setStatus(1);
            QueryWrapper<ZhiliangGongxu> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangGongxu, req.getParameterMap());
            ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper);
            if (one != null) {
                if (zhiliangGongxu.getLiangceng() == 1) {
                    //先判断第二层有没有梁 没有才能出场第一层
                    QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("shebeino", zhiliangGongxu.getShebeino());
                    queryWrapper1.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                    queryWrapper1.eq("dangqianceng", 2);
                    queryWrapper1.eq("status1", 1);
                    CunliangProcedureConfig one1 = cunliangProcedureConfigService.getOne(queryWrapper1);
                    //再判断第一层有没有梁
                    QueryWrapper<CunliangProcedureConfig> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("shebeino", zhiliangGongxu.getShebeino());
                    queryWrapper2.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                    queryWrapper2.eq("dangqianceng", zhiliangGongxu.getLiangceng());
                    CunliangProcedureConfig one2 = cunliangProcedureConfigService.getOne(queryWrapper2);
                    if (ObjectUtils.isEmpty(one1) && one2 != null) {
                        return Result.OK("可以出场梁");
                    }
                } else if (zhiliangGongxu.getLiangceng() == 2) {
                    QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("shebeino", zhiliangGongxu.getShebeino());
                    queryWrapper1.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                    queryWrapper1.eq("dangqianceng", zhiliangGongxu.getLiangceng());
                    CunliangProcedureConfig one1 = cunliangProcedureConfigService.getOne(queryWrapper1);
                    if (one1 != null) {
                        return Result.OK("可以出场梁");
                    }
                }
                return Result.error("请先从上层开始取梁!");
            } else {
                return Result.error("当前台座上没有梁存在！");
            }
        } else {
            return Result.error("请选择存梁或取梁！！！");
        }
    }


    public boolean cunquliangBianJi(ZhiliangGongxu zhiliangGongxu){
        QueryWrapper<ZhiliangGongxu> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("uuid",zhiliangGongxu.getUuid());
        queryWrapper3.eq("xuhao",7);
        ZhiliangGongxu one3 = zhiliangGongxuService.getOne(queryWrapper3);
        if (one3 != null){
            if (one3.getTaizuoname() != null){
                String taizuoname = one3.getTaizuoname();
                Integer liangceng = one3.getLiangceng();
                String cunlianghang = one3.getCunlianghang();
                String cunlianglie = one3.getCunlianglie();
                String shebeino = one3.getShebeino();
                if(!taizuoname.equals(zhiliangGongxu.getTaizuoname())){
                    //排除和之前存梁信息一样
                    if (!shebeino.equals(zhiliangGongxu.getShebeino()) || !taizuoname.equals(zhiliangGongxu.getTaizuoname()) || !liangceng.equals(zhiliangGongxu.getLiangceng())){
                        if (liangceng == 1){
                            //如果上层有梁，则不让移梁
                            QueryWrapper<CunliangProcedureConfig> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("shebeino",shebeino);
                            queryWrapper.eq("dangqianceng",2);
                            queryWrapper.eq("liangzuoname",taizuoname);
                            CunliangProcedureConfig one = cunliangProcedureConfigService.getOne(queryWrapper);
                            if (one.getStatus1() != 0){
                                return false;
                            }else {
                                QueryWrapper<CunliangProcedureConfig> queryWrapper2 = new QueryWrapper<>();
                                queryWrapper2.eq("shebeino",shebeino);
                                queryWrapper2.eq("liangzuoname",taizuoname);
                                List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper2);
                                if (list.size() > 0){
                                    for (CunliangProcedureConfig l :list){
                                        l.setStatus(0);
                                        cunliangProcedureConfigService.updateById(l);
                                    }
                                }
                            }
                        }else {
                            QueryWrapper<CunliangProcedureConfig> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("shebeino",shebeino);
                            queryWrapper.eq("liangzuoname",taizuoname);
                            List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper);
                            if (list.size() > 0){
                                for (CunliangProcedureConfig l :list){
                                    l.setStatus1(0);
                                    cunliangProcedureConfigService.updateById(l);
                                }
                            }
                        }
                    }
                }else {
                    return false;
                }

            }
        }
        return true;
    }
    /**
     * 出梁时查询当前台座是否有梁存在
     *
     * @param zhiliangGongxu
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-台座验证")
    @ApiOperation(value = "存梁工序表信息-台座验证", notes = "存梁工序表信息-台座验证")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(ZhiliangGongxu zhiliangGongxu,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        QueryWrapper<ZhiliangGongxu> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangGongxu, req.getParameterMap());
        ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper);
        if (one != null) {
            if (zhiliangGongxu.getLiangceng() == 1) {
                //先判断第二层有没有梁 没有才能出场第一层
                QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("shebeino", zhiliangGongxu.getShebeino());
                queryWrapper1.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                queryWrapper1.eq("dangqianceng", 2);
                queryWrapper1.eq("status1", 0);
                CunliangProcedureConfig one1 = cunliangProcedureConfigService.getOne(queryWrapper1);
                //再判断第一层有没有梁
                QueryWrapper<CunliangProcedureConfig> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("shebeino", zhiliangGongxu.getShebeino());
                queryWrapper2.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                queryWrapper2.eq("dangqianceng", zhiliangGongxu.getLiangceng());
                queryWrapper2.eq("status", 1);
                CunliangProcedureConfig one2 = cunliangProcedureConfigService.getOne(queryWrapper2);
                if (one1 != null && one2 != null) {
                    return Result.OK("可以出场梁");
                }
            } else if (zhiliangGongxu.getLiangceng() == 2) {
                QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("shebeino", zhiliangGongxu.getShebeino());
                queryWrapper1.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
                queryWrapper1.eq("dangqianceng", zhiliangGongxu.getLiangceng());
                queryWrapper1.eq("status1", 1);
                CunliangProcedureConfig one1 = cunliangProcedureConfigService.getOne(queryWrapper1);
                if (one1 != null) {
                    return Result.OK("可以出场梁");
                }
            }
            return Result.error("请先从上层开始取梁!");
        } else {
            return Result.error("当前台座上没有梁存在！");
        }
    }


    /**
     * 出梁时 查询对应数据
     *
     * @param zhiliangGongxu
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-台座验证")
    @ApiOperation(value = "存梁工序表信息-台座验证", notes = "存梁工序表信息-台座验证")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(ZhiliangGongxu zhiliangGongxu,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        QueryWrapper<ZhiliangGongxu> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangGongxu, req.getParameterMap());
        ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper);
        if (one != null) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(one.getShebeino());
            one.setRemark(selectshebeione.getSbname());
            return Result.OK(one);
        } else {
            return Result.error("当前台座上没有梁存在！");
        }
    }

    /**
     * 存梁区列表查询
     *
     * @param cunliangProcedureConfig
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-列表查询")
    @ApiOperation(value = "存梁工序表信息-列表查询", notes = "存梁工序表信息-列表查询")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(CunliangProcedureConfig cunliangProcedureConfig,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer sta, Integer sta1) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (cunliangProcedureConfig.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                cunliangProcedureConfig.setShebeino(shebei);
            } else {
                cunliangProcedureConfig.setShebeino("空");
            }
        }
        if (cunliangProcedureConfig.getLiangzuoname() != null) {
            cunliangProcedureConfig.setLiangzuoname("*" + cunliangProcedureConfig.getLiangzuoname() + "*");
        }
        QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
        if (sta != null && sta1 != null) {
            if (sta == 0 && sta1 == 0) {
                queryWrapper.eq("status", sta);
                queryWrapper.eq("status1", sta1);
                queryWrapper.groupBy("shebeino", "liangzuoname");
            } else if (sta == 1 || sta1 == 1) {
//                queryWrapper.eq("status", sta);
//                queryWrapper.or();
//                queryWrapper.eq("status1", sta1);
                queryWrapper.last("and (status=1 or status1=1) group by shebeino,liangzuoname");
            }
        } else {
            queryWrapper.groupBy("shebeino", "liangzuoname");
        }
        List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper);
        List list2 = new ArrayList();
        for (CunliangProcedureConfig procedureConfig : list) {
            cunliangProcedureConfigPage cunliangProcedureConfigPage = new cunliangProcedureConfigPage();
            cunliangProcedureConfigPage.setLiangzuoname(procedureConfig.getLiangzuoname());
            cunliangProcedureConfigPage.setShebeino(procedureConfig.getShebeino());
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shebeino", procedureConfig.getShebeino());
            queryWrapper1.eq("taizuoname", procedureConfig.getLiangzuoname());
            queryWrapper1.eq("status", 1);
            queryWrapper1.eq("isdel", 0);
            List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper1);
            if (list1.size() > 0) {
                for (ZhiliangGongxu zhiliangGongxu : list1) {
                    QueryWrapper<Zhiliangrenwudan> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("uuid", zhiliangGongxu.getUuid());
                    queryWrapper2.eq("isdel", 0);
                    Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper2);
                    if (one != null) {
                        zhiliangGongxu.setRemark(one.getConspos());
                        zhiliangGongxu.setTaizuoname(one.getCode());
                    }
                }
            }
            cunliangProcedureConfigPage.setZhiliangGongxuList(list1);
            list2.add(cunliangProcedureConfigPage);
        }
        return Result.OK(list2);
    }

    /**
     * @param cunliangProcedureConfig 查询当前用户下的所有台座
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-所有台座(数量)")
    @ApiOperation(value = "存梁工序表信息-所有台座", notes = "存梁工序表信息-所有台座(数量)")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList5(CunliangProcedureConfig cunliangProcedureConfig, HttpServletRequest req, Integer sta, Integer sta1) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (StrUtil.isBlank(cunliangProcedureConfig.getShebeino())) {
            if (!"null".equals(shebei)) {
                cunliangProcedureConfig.setShebeino(shebei);
            } else {
                cunliangProcedureConfig.setShebeino("空");
            }
        }
        if (StrUtil.isNotBlank(cunliangProcedureConfig.getLiangzuoname())) {
            cunliangProcedureConfig.setLiangzuoname("*" + cunliangProcedureConfig.getLiangzuoname() + "*");
        }
        QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
        if (sta != null && sta1 != null) {
            if (sta == 0 && sta1 == 0) {
                queryWrapper.eq("status", sta);
                queryWrapper.eq("status1", sta1);
                queryWrapper.groupBy("shebeino", "liangzuoname");
            } else if (sta == 1 || sta1 == 1) {
//                queryWrapper.eq("status", sta);
//                queryWrapper.or();
//                queryWrapper.eq("status1", sta1);
                queryWrapper.last("and (status=1 or status1=1) group by shebeino,liangzuoname");
            }
        } else {
            queryWrapper.groupBy("shebeino", "liangzuoname");
        }
        List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * @param
     * @param
     * @param
     * @return
     */
    @AutoLog(value = "存梁工序表信息-所有台座(数量)")
    @ApiOperation(value = "存梁工序表信息-所有台座", notes = "存梁工序表信息-所有台座(数量)")
    @GetMapping(value = "/listJh")
    public Result<?> queryPageListJh(String sys_depart_orgcode, Integer sta, Integer sta1, HttpServletRequest req) {
        CunliangProcedureConfig config = new CunliangProcedureConfig();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (config.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                config.setShebeino(shebei);
            } else {
                config.setShebeino("空");
            }
        }
        QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(config, req.getParameterMap());
        if (sta != null && sta1 != null) {
            if (sta == 0 && sta1 == 0) {
                queryWrapper.eq("status", sta);
                queryWrapper.eq("status1", sta1);
                queryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
            } else if (sta == 1 || sta1 == 1) {
                queryWrapper.eq("status", sta);
                queryWrapper.eq("status1", sta1);
                queryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
            }
        } else {
            queryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
        }
        List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper);

        int i = list.size();
        return Result.OK(i);
    }

    /**
     * 存梁区列表查询分页
     *
     * @param cunliangProcedureConfig
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-列表查询分页")
    @ApiOperation(value = "存梁工序表信息-列表查询分页", notes = "存梁工序表信息-列表查询分页")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList6(CunliangProcedureConfig cunliangProcedureConfig,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer sta, Integer sta1) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (StrUtil.isBlank(cunliangProcedureConfig.getShebeino())) {
            if (!"null".equals(shebei)) {
                cunliangProcedureConfig.setShebeino(shebei);
            } else {
                cunliangProcedureConfig.setShebeino("空");
            }
        }
        if (!StrUtil.isBlank(cunliangProcedureConfig.getLiangzuoname())) {
            cunliangProcedureConfig.setLiangzuoname("*" + cunliangProcedureConfig.getLiangzuoname() + "*");
        }
        QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
        if (sta != null && sta1 != null) {
            if (sta == 0 && sta1 == 0) {
                queryWrapper.eq("status", sta);
                queryWrapper.eq("status1", sta1);
                queryWrapper.groupBy("shebeino", "liangzuoname");
            } else if (sta == 1 || sta1 == 1) {
//                queryWrapper.eq("status", sta);
//                queryWrapper.or();
//                queryWrapper.eq("status1", sta1);
                queryWrapper.last("and (status=1 or status1=1) group by shebeino,liangzuoname");
            }
        } else {
            queryWrapper.groupBy("shebeino", "liangzuoname");
        }
        Page<CunliangProcedureConfig> page = new Page<CunliangProcedureConfig>(pageNo, pageSize);
        IPage<CunliangProcedureConfig> pageList = cunliangProcedureConfigService.page(page, queryWrapper);
        List<CunliangProcedureConfig> records = pageList.getRecords();
        List list2 = new ArrayList();
        for (CunliangProcedureConfig procedureConfig : records) {
            cunliangProcedureConfigPage cunliangProcedureConfigPage = new cunliangProcedureConfigPage();
            cunliangProcedureConfigPage.setLiangzuoname(procedureConfig.getLiangzuoname());
            cunliangProcedureConfigPage.setShebeino(procedureConfig.getShebeino());
            cunliangProcedureConfigPage.setSwitchsta(procedureConfig.getSwitchsta());
            cunliangProcedureConfigPage.setUuid(procedureConfig.getUuid());
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shebeino", procedureConfig.getShebeino());
            queryWrapper1.eq("taizuoname", procedureConfig.getLiangzuoname());
            queryWrapper1.eq("status", 1);
            queryWrapper1.eq("isdel", 0);
//            queryWrapper1.orderByDesc("id");
            List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper1);
            if (list1.size() > 0) {
                for (ZhiliangGongxu zhiliangGongxu : list1) {
                    QueryWrapper<Zhiliangrenwudan> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("uuid", zhiliangGongxu.getUuid());
                    queryWrapper2.eq("isdel", 0);
                    Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper2);
                    if (one != null) {
                        zhiliangGongxu.setRemark(one.getConspos());
                        zhiliangGongxu.setTaizuoname(one.getCode());
                    }
                }
            }
            cunliangProcedureConfigPage.setZhiliangGongxuList(list1);
            list2.add(cunliangProcedureConfigPage);
        }
        return Result.OK(list2);
    }

    /**
     * 存梁区列表查询
     *
     * @return
     */
    @AutoLog(value = "存梁工序表信息-列表查询")
    @ApiOperation(value = "存梁工序表信息-列表查询", notes = "存梁工序表信息-列表查询")
    @GetMapping(value = "/list7")
    public Result<?> queryPageList7(String uuid, CunliangProcedureConfig cunliangProcedureConfig, HttpServletRequest req) {
        List<CunliangProcedureConfig> list = null;
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (cunliangProcedureConfig.getLiangzuoname() != null) {
            cunliangProcedureConfig.setLiangzuoname(cunliangProcedureConfig.getLiangzuoname() + "*");
            if (StrUtil.isBlank(cunliangProcedureConfig.getShebeino())) {
                if (!"null".equals(shebei)) {
                    cunliangProcedureConfig.setShebeino(shebei);
                } else {
                    cunliangProcedureConfig.setShebeino("空");
                }
            }
        }
        if (uuid != null) {
            QueryWrapper<ZhiliangGongxu> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("uuid", uuid);
            queryWrapper3.eq("xuhao", 7);
            ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper3);

            QueryWrapper<CunliangProcedureConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebeino", one1.getShebeino());
            queryWrapper.eq("liangzuoname", one1.getTaizuoname());
            queryWrapper.groupBy("shebeino", "liangzuoname");
            list = cunliangProcedureConfigService.list(queryWrapper);
        } else {

            QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
            queryWrapper.groupBy("shebeino", "liangzuoname");
            list = cunliangProcedureConfigService.list(queryWrapper);
        }

        List list2 = new ArrayList();
        for (CunliangProcedureConfig procedureConfig : list) {
            cunliangProcedureConfigPage cunliangProcedureConfigPage = new cunliangProcedureConfigPage();
            cunliangProcedureConfigPage.setLiangzuoname(procedureConfig.getLiangzuoname());
            cunliangProcedureConfigPage.setShebeino(procedureConfig.getShebeino());
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shebeino", procedureConfig.getShebeino());
            queryWrapper1.eq("taizuoname", procedureConfig.getLiangzuoname());
            queryWrapper1.eq("status", 1);
            queryWrapper1.eq("isdel", 0);
            List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper1);
            if (list1.size() > 0) {
                for (ZhiliangGongxu zhiliangGongxu : list1) {
                    QueryWrapper<Zhiliangrenwudan> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("uuid", zhiliangGongxu.getUuid());
                    queryWrapper2.eq("isdel", 0);
                    Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper2);
                    if (one == null) {
                        continue;
                    }
                    zhiliangGongxu.setRemark(one.getConspos());
                    zhiliangGongxu.setTaizuoname(one.getCode());
                }
            }
            cunliangProcedureConfigPage.setZhiliangGongxuList(list1);
            list2.add(cunliangProcedureConfigPage);
        }
        return Result.OK(list2);
    }

    /**
     * 存梁区列表查询
     *
     * @return
     */
    @AutoLog(value = "存梁工序表信息-列表查询")
    @ApiOperation(value = "存梁工序表信息-列表查询", notes = "存梁工序表信息-列表查询")
    @GetMapping(value = "/listJxgs")
    public Result<?> queryPageListJxgs(String uuid, CunliangProcedureConfig cunliangProcedureConfig, HttpServletRequest req) {
        List<CunliangProcedureConfig> list = null;
        if (uuid != null) {
            QueryWrapper<ZhiliangGongxu> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("uuid", uuid);
            queryWrapper3.eq("xuhao", 7);
            ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper3);

            QueryWrapper<CunliangProcedureConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebeino", one1.getShebeino());
            queryWrapper.eq("liangzuoname", one1.getTaizuoname());
            queryWrapper.groupBy("shebeino", "liangzuoname");
            list = cunliangProcedureConfigService.list(queryWrapper);
        } else {
            if (cunliangProcedureConfig.getLiangzuoname() != null) {
                cunliangProcedureConfig.setLiangzuoname(cunliangProcedureConfig.getLiangzuoname() + "*");
            }
            QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
            queryWrapper.groupBy("shebeino", "liangzuoname");
            list = cunliangProcedureConfigService.list(queryWrapper);
        }

        List list2 = new ArrayList();
        for (CunliangProcedureConfig procedureConfig : list) {
            cunliangProcedureConfigPage cunliangProcedureConfigPage = new cunliangProcedureConfigPage();
            cunliangProcedureConfigPage.setLiangzuoname(procedureConfig.getLiangzuoname());
            cunliangProcedureConfigPage.setShebeino(procedureConfig.getShebeino());
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shebeino", procedureConfig.getShebeino());
            queryWrapper1.eq("taizuoname", procedureConfig.getLiangzuoname());
            queryWrapper1.eq("status", 1);
            queryWrapper1.eq("isdel", 0);
            List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper1);
            if (list1.size() > 0) {
                for (ZhiliangGongxu zhiliangGongxu : list1) {
                    QueryWrapper<Zhiliangrenwudan> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("uuid", zhiliangGongxu.getUuid());
                    queryWrapper2.eq("isdel", 0);
                    Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper2);
                    if (one == null) {
                        continue;
                    }
                    zhiliangGongxu.setRemark(one.getConspos());
                    zhiliangGongxu.setTaizuoname(one.getCode());
                }
            }
            cunliangProcedureConfigPage.setZhiliangGongxuList(list1);
            list2.add(cunliangProcedureConfigPage);
        }
        return Result.OK(list2);
    }

    /**
     * 存梁区列表查询
     *
     * @param cunliangProcedureConfig
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-列表查询")
    @ApiOperation(value = "存梁工序表信息-列表查询", notes = "存梁工序表信息-列表查询")
    @GetMapping(value = "/list8")
    public Result<?> queryPageList8(CunliangProcedureConfig cunliangProcedureConfig,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (StrUtil.isBlank(cunliangProcedureConfig.getShebeino())) {
            if (!"null".equals(shebei)) {
                cunliangProcedureConfig.setShebeino(shebei);
            } else {
                cunliangProcedureConfig.setShebeino("空");
            }
        }
        if (cunliangProcedureConfig.getLiangzuoname() != null) {
            cunliangProcedureConfig.setLiangzuoname(cunliangProcedureConfig.getLiangzuoname() + "*");
        }
        QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
        queryWrapper.groupBy("shebeino", "liangzuoname");
        List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper);
        List list2 = new ArrayList();
        for (CunliangProcedureConfig procedureConfig : list) {
            cunliangProcedureConfigPage cunliangProcedureConfigPage = new cunliangProcedureConfigPage();
            cunliangProcedureConfigPage.setLiangzuoname(procedureConfig.getLiangzuoname());
            cunliangProcedureConfigPage.setShebeino(procedureConfig.getShebeino());
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shebeino", procedureConfig.getShebeino());
            queryWrapper1.eq("taizuoname", procedureConfig.getLiangzuoname());
            queryWrapper1.eq("status", 1);
            queryWrapper1.eq("isdel", 0);
            List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper1);
            if (list1.size() > 0) {
                for (ZhiliangGongxu zhiliangGongxu : list1) {
                    QueryWrapper<Zhiliangrenwudan> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("uuid", zhiliangGongxu.getUuid());
                    queryWrapper2.eq("isdel", 0);
                    Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper2);
                    if (one == null) {
                        continue;
                    }
                    zhiliangGongxu.setRemark(one.getProjname());
                    zhiliangGongxu.setTaizuoname(one.getCode());
                }
            }
            cunliangProcedureConfigPage.setZhiliangGongxuList(list1);
            list2.add(cunliangProcedureConfigPage);
        }
        return Result.OK(list2);
    }

    /**
     * 存梁区列表查询
     *
     * @param cunliangProcedureConfig
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-列表查询")
    @ApiOperation(value = "存梁工序表信息-列表查询", notes = "存梁工序表信息-列表查询")
    @GetMapping(value = "/listhtx")
    public Result<?> queryPageListhtx(CunliangProcedureConfig cunliangProcedureConfig,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (StrUtil.isBlank(cunliangProcedureConfig.getShebeino())) {
            if (!"null".equals(shebei)) {
                cunliangProcedureConfig.setShebeino(shebei);
            } else {
                cunliangProcedureConfig.setShebeino("空");
            }
        }
        if (cunliangProcedureConfig.getSysOrgCode() != null){
            cunliangProcedureConfig.setSysOrgCode(cunliangProcedureConfig.getSysOrgCode() + "*");
        }
        QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
        queryWrapper.groupBy("shebeino", "liangzuoname");
        List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper);
        List list2 = new ArrayList();
        for (CunliangProcedureConfig procedureConfig : list) {
            cunliangProcedureConfigPage cunliangProcedureConfigPage = new cunliangProcedureConfigPage();
            cunliangProcedureConfigPage.setLiangzuoname(procedureConfig.getLiangzuoname());
            cunliangProcedureConfigPage.setShebeino(procedureConfig.getShebeino());
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shebeino", procedureConfig.getShebeino());
            queryWrapper1.eq("taizuoname", procedureConfig.getLiangzuoname());
            queryWrapper1.eq("status", 1);
            queryWrapper1.eq("isdel", 0);
            List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper1);
            if(list1.size() > 0){
                for (ZhiliangGongxu l :list1){
                    Zhiliangrenwudan selectuuid = zhiliangrenwudanService.selectuuid(l.getUuid());
                    if (selectuuid != null){
                        l.setPicurl(selectuuid.getCode());
                        list2.add(l);
                    }
                }
            }
        }
        return Result.OK(list2);
    }

    /**
     * 梁台座交换，没有校验接口
     *
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-列表查询")
    @ApiOperation(value = "存梁工序表信息-列表查询", notes = "存梁工序表信息-列表查询")
    @GetMapping(value = "/listhtxjh")
    public Result<?> queryPageListhtxjh(String uuid,String uid,
                                      HttpServletRequest req) {
        ZhiliangGongxu selectuuid = zhiliangGongxuService.selectuuid(uid, 7);
        ZhiliangGongxu selectuuid1 = zhiliangGongxuService.selectuuid(uuid, 7);
        String taizuoname = selectuuid.getTaizuoname();
        Integer liangceng = selectuuid.getLiangceng();
        String cunlianghang = selectuuid.getCunlianghang();
        String cunlianglie = selectuuid.getCunlianglie();
        String shebeino = selectuuid.getShebeino();

        selectuuid.setTaizuoname(selectuuid1.getTaizuoname());
        selectuuid.setLiangceng(selectuuid1.getLiangceng());
        selectuuid.setCunlianghang(selectuuid1.getCunlianghang());
        selectuuid.setCunlianglie(selectuuid1.getCunlianglie());
        selectuuid.setShebeino(selectuuid1.getShebeino());

        selectuuid1.setTaizuoname(taizuoname);
        selectuuid1.setLiangceng(liangceng);
        selectuuid1.setCunlianghang(cunlianghang);
        selectuuid1.setCunlianglie(cunlianglie);
        selectuuid1.setShebeino(shebeino);

        boolean b = zhiliangGongxuService.updateById(selectuuid);
        boolean b1 = zhiliangGongxuService.updateById(selectuuid1);
        return Result.OK("梁交换成功！！！");
    }
    /**
     * 存梁台座对应信息查询页面接口
     *
     * @param cunliangProcedureConfig
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "存梁工序表信息-列表查询")
    @ApiOperation(value = "存梁工序表信息-列表查询", notes = "存梁工序表信息-列表查询")
    @GetMapping(value = "/listym8")
    public Result<?> queryPageListym8(CunliangProcedureConfig cunliangProcedureConfig,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (StrUtil.isBlank(cunliangProcedureConfig.getShebeino())) {
            if (!"null".equals(shebei)) {
                cunliangProcedureConfig.setShebeino(shebei);
            } else {
                cunliangProcedureConfig.setShebeino("空");
            }
        }
        QueryWrapper<CunliangProcedureConfig> queryWrapper = QueryGenerator.initQueryWrapper(cunliangProcedureConfig, req.getParameterMap());
        Page<CunliangProcedureConfig> page = new Page<CunliangProcedureConfig>(pageNo, pageSize);
        IPage<CunliangProcedureConfig> pageList = cunliangProcedureConfigService.page(page, queryWrapper);
        List<CunliangProcedureConfig> list = pageList.getRecords();
        for (CunliangProcedureConfig procedureConfig : list) {
            procedureConfig.setCreateBy(null);
            procedureConfig.setUuid(null);
            procedureConfig.setCreateTime(null);
            procedureConfig.setCengshu(null);
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shebeino", procedureConfig.getShebeino());
            queryWrapper1.eq("taizuoname", procedureConfig.getLiangzuoname());
            queryWrapper1.eq("liangceng", procedureConfig.getDangqianceng());
            queryWrapper1.eq("status", 1);
            queryWrapper1.eq("isdel", 0);
            List<ZhiliangGongxu> list1 = zhiliangGongxuService.list(queryWrapper1);
            if (list1.size() > 0) {
                for (ZhiliangGongxu zhiliangGongxu : list1) {
                    QueryWrapper<Zhiliangrenwudan> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("uuid", zhiliangGongxu.getUuid());
                    queryWrapper2.eq("isdel", 0);
                    Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper2);
                    if (one == null) {
                        continue;
                    }

                    procedureConfig.setCreateBy(one.getConspos());
                    procedureConfig.setUuid(one.getCode());
                    procedureConfig.setCreateTime(zhiliangGongxu.getCuntime());
                    String days = getDays(zhiliangGongxu.getCuntime());
                    procedureConfig.setCengshu(days);
                    procedureConfig.setSwitchsta(1);//占用
                }
            } else {
                procedureConfig.setSwitchsta(0);//空闲
            }
        }
        return Result.OK(pageList);
    }

    public static String getDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long oldTime = cal.getTimeInMillis();
        long nowTime = System.currentTimeMillis();
        long days = (nowTime - oldTime) / (1000 * 60 * 60 * 24);//天数
        long hours = ((nowTime - oldTime) % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);//小时数
        long minutes = (((nowTime - oldTime) % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) / (1000 * 60);//分钟数
        long seconds = ((((nowTime - oldTime) % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) % (1000 * 60)) / 1000;//秒数
        return days + "天" + hours + "小时";
    }

    /**
     * 添加
     *
     * @param beammanagementProcedure
     * @return
     */
    @AutoLog(value = "存梁工序表信息-添加")
    @ApiOperation(value = "存梁工序表信息-添加", notes = "存梁工序表信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BeammanagementProcedure beammanagementProcedure) {
        beammanagementProcedureService.save(beammanagementProcedure);
        return Result.OK("添加成功！");
    }


    /**
     * 存梁接口
     *
     * @param zhiliangGongxu
     * @return
     */
    @AutoLog(value = "存梁工序表信息-存梁")
    @ApiOperation(value = "存梁工序表信息-存梁", notes = "存梁工序表信息-存梁")
    @PostMapping(value = "/add1")
    public Result<?> add1(@RequestBody ZhiliangGongxu zhiliangGongxu, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("shebeino", zhiliangGongxu.getShebeino());
        queryWrapper1.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
//			 queryWrapper1.eq("dangqianceng",beammanagementProcedure.getLiangceng());
//        queryWrapper1.eq("lianghang", beammanagementProcedure.getCunlianghang());
//        queryWrapper1.eq("lianglie", beammanagementProcedure.getCunlianglie());
        List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper1);
        if (list.size() > 0) {
            boolean b1 = false;
            String hang = null;
            String lie = null;
            for (CunliangProcedureConfig cunliangProcedureConfig : list) {
                hang = cunliangProcedureConfig.getLianghang();
                lie = cunliangProcedureConfig.getLianglie();
                CunliangProcedureConfig cunliangProcedureConfig1 = new CunliangProcedureConfig();
                cunliangProcedureConfig1.setId(cunliangProcedureConfig.getId());
                if (zhiliangGongxu.getLiangceng() == 1) {
                    cunliangProcedureConfig1.setStatus(1);
                }
                if (zhiliangGongxu.getLiangceng() == 2) {
                    cunliangProcedureConfig1.setStatus1(1);
                }
                b1 = cunliangProcedureConfigService.updateById(cunliangProcedureConfig1);
            }
            if (b1) {
                QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("uuid", zhiliangGongxu.getUuid());
                queryWrapper.eq("isdel", 0);
                Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
                if (one != null) {
                    zhiliangGongxu.setStatus(1);
                    zhiliangGongxu.setCunpeople(zhiliangGongxu.getResponsible());
                    if (zhiliangGongxu.getCuntime() == null) {
                        zhiliangGongxu.setCuntime(new Date());
                    } else {
                        zhiliangGongxu.setCuntime(zhiliangGongxu.getCuntime());
                    }
                    zhiliangGongxu.setCunlianghang(hang);
                    zhiliangGongxu.setCunlianglie(lie);
                    zhiliangGongxu.setId(zhiliangGongxu.getId());
                    zhiliangGongxuService.updateById(zhiliangGongxu);
                    ZhiliangTaizuoCfg zhiliangTaizuoCfg = zhiliangTaizuoCfgService.selectzltaizuos(one.getTaizuono());
                    if (null != zhiliangTaizuoCfg) {
                        zhiliangTaizuoCfg.setStatus(0);
                        zhiliangTaizuoCfg.setBeamno("");
                        zhiliangTaizuoCfg.setXuhao(0);
                        zhiliangTaizuoCfgService.updateById(zhiliangTaizuoCfg);
                    }
                    one.setCunliangstatus(2);
                    one.setXuhao(String.valueOf(zhiliangGongxu.getXuhao()));
                    zhiliangrenwudanService.updateById(one);
                    return Result.OK("存梁成功！");
                }
            }

        }
        return Result.error("存梁失败！");
    }

    /**
     * 取梁接口
     *
     * @param zhiliangGongxu
     * @return
     */
    @AutoLog(value = "存梁工序表信息-取梁")
    @ApiOperation(value = "存梁工序表信息-取梁", notes = "存梁工序表信息-取梁")
    @PostMapping(value = "/add2")
    public Result<?> add2(@RequestBody ZhiliangGongxu zhiliangGongxu, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        QueryWrapper<ZhiliangGongxu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", zhiliangGongxu.getUuid());
        queryWrapper.eq("xuhao", 7);
        queryWrapper.eq("isdel", 0);
        ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper);
        if (one != null) {
            QueryWrapper<CunliangProcedureConfig> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("shebeino", zhiliangGongxu.getShebeino());
            queryWrapper2.eq("liangzuoname", zhiliangGongxu.getTaizuoname());
//				 queryWrapper2.eq("dangqianceng",beammanagementProcedure.getLiangceng());
//            queryWrapper2.eq("lianghang", beammanagementProcedure.getCunlianghang());
//            queryWrapper2.eq("lianglie", beammanagementProcedure.getCunlianglie());
            List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper2);
            if (list.size() > 0) {
                boolean b1 = false;
                for (CunliangProcedureConfig cunliangProcedureConfig : list) {
                    CunliangProcedureConfig cunliangProcedureConfig1 = new CunliangProcedureConfig();
                    cunliangProcedureConfig1.setId(cunliangProcedureConfig.getId());
                    if (zhiliangGongxu.getLiangceng() == 1) {
                        cunliangProcedureConfig1.setStatus(0);
                    }
                    if (zhiliangGongxu.getLiangceng() == 2) {
                        cunliangProcedureConfig1.setStatus1(0);
                    }
                    b1 = cunliangProcedureConfigService.updateById(cunliangProcedureConfig1);
                }
                if (b1) {
                    QueryWrapper<Zhiliangrenwudan> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("uuid", zhiliangGongxu.getUuid());
                    queryWrapper1.eq("isdel", 0);
                    Zhiliangrenwudan one1 = zhiliangrenwudanService.getOne(queryWrapper1);
                    if (one1 != null) {
                        ZhiliangGongxu zhiliangGongxu1 = new ZhiliangGongxu();
                        zhiliangGongxu1.setId(zhiliangGongxu.getId());
                        zhiliangGongxu1.setStatus(2);
                        if (zhiliangGongxu.getChutime() == null) {
                            zhiliangGongxu1.setChutime(new Date());
                        } else {
                            zhiliangGongxu1.setChutime(zhiliangGongxu.getChutime());
                        }
                        zhiliangGongxu1.setChupeople(zhiliangGongxu.getResponsible());
                        zhiliangGongxuService.updateById(zhiliangGongxu1);
                        one1.setCunliangstatus(3);
                        one1.setXuhao(String.valueOf(zhiliangGongxu.getXuhao()));
                        zhiliangrenwudanService.updateById(one1);
                        return Result.OK("取梁成功！");
                    }

                }

            }
        }
        return Result.error("取梁失败！");
    }

    /**
     * 存梁取梁
     *
     * @param beammanagementProcedure
     * @return
     */
    @AutoLog(value = "存梁取梁")
    @ApiOperation(value = "存梁取梁", notes = "存梁取梁")
    @PostMapping(value = "/add3")
    public Result<?> add1(@RequestBody BeammanagementProcedure beammanagementProcedure) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        Zhiliangrenwudan zhiliangrenwudan = new Zhiliangrenwudan();
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", beammanagementProcedure.getUuid());
        Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
        if (one == null) {
            return Result.error("存梁/取梁确认失败!");
        } else {
            zhiliangrenwudan.setId(one.getId());
            zhiliangrenwudan.setCunliangstatus(beammanagementProcedure.getStatus());
            zhiliangrenwudanService.updateById(zhiliangrenwudan);
            beammanagementProcedure.setCuntime(new Date());
            beammanagementProcedure.setChupeople(loginUser.getUsername());
            beammanagementProcedureService.save(beammanagementProcedure);
            return Result.OK("存梁/取梁确认成功！");
        }
    }


    /**
     * 编辑
     *
     * @param beammanagementProcedure
     * @return
     */
    @AutoLog(value = "存梁工序表信息-编辑")
    @ApiOperation(value = "存梁工序表信息-编辑", notes = "存梁工序表信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BeammanagementProcedure beammanagementProcedure) {
        beammanagementProcedureService.updateById(beammanagementProcedure);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "存梁工序表信息-通过id删除")
    @ApiOperation(value = "存梁工序表信息-通过id删除", notes = "存梁工序表信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        beammanagementProcedureService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "存梁工序表信息-批量删除")
    @ApiOperation(value = "存梁工序表信息-批量删除", notes = "存梁工序表信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.beammanagementProcedureService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "存梁工序表信息-通过id查询")
    @ApiOperation(value = "存梁工序表信息-通过id查询", notes = "存梁工序表信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BeammanagementProcedure beammanagementProcedure = beammanagementProcedureService.getById(id);
        if (beammanagementProcedure == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(beammanagementProcedure);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param beammanagementProcedure
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BeammanagementProcedure beammanagementProcedure) {
        return super.exportXls(request, beammanagementProcedure, BeammanagementProcedure.class, "存梁工序表信息");
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
        return super.importExcel(request, response, BeammanagementProcedure.class);
    }

}
