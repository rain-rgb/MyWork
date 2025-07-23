package com.trtm.iot.ydcx.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.rhdcx.entity.WRuanhuadianM;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.ydcx.entity.WYanduM;
import com.trtm.iot.ydcx.entity.WYanduS;
import com.trtm.iot.ydcx.service.IWYanduMService;
import com.trtm.iot.ydcx.service.IWYanduSService;
import com.trtm.iot.ydcx.vo.WYanduMvo;
import com.trtm.iot.zhenru.entity.WZhenruduM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 延度主表
 * @Author: jeecg-boot
 * @Date: 2021-04-26
 * @Version: V1.0
 */
@Api(tags = "延度主表")
@RestController
@RequestMapping("/yd/wYanduM")
@Slf4j
public class WYanduMController extends JeecgController<WYanduM, IWYanduMService> {
    @Autowired
    private IWYanduMService wYanduMService;
    @Autowired
    private IWYanduSService wYanduSService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    /**
     * 分页列表查询
     *
     * @param wYanduM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "延度主表-分页列表查询")
    @ApiOperation(value = "延度主表-分页列表查询", notes = "延度主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WYanduM wYanduM,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wYanduM.getFsbbh() == null) {
            if (!"null".equals(shebei)) {
                wYanduM.setFsbbh(shebei);
            } else {
                wYanduM.setFsbbh("空");
            }
        }
        QueryWrapper<WYanduM> queryWrapper = QueryGenerator.initQueryWrapper(wYanduM, req.getParameterMap());
        Page<WYanduM> page = new Page<WYanduM>(pageNo, pageSize);
        IPage<WYanduM> pageList = wYanduMService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param wYanduM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "延度主表-分页列表查询")
    @ApiOperation(value = "延度主表-分页列表查询", notes = "延度主表-分页列表查询")
    @GetMapping(value = "/listcb")
    public Result<?> listcb(WYanduM wYanduM,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wYanduM.getFsbbh() == null) {
            if (!"null".equals(shebei)) {
                wYanduM.setFsbbh(shebei);
            } else {
                wYanduM.setFsbbh("空");
            }
        }
        QueryWrapper<WYanduM> queryWrapper = QueryGenerator.initQueryWrapper(wYanduM, req.getParameterMap());
        queryWrapper.eq("isQualified", 1);
        Page<WYanduM> page = new Page<WYanduM>(pageNo, pageSize);
        IPage<WYanduM> pageList = wYanduMService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wYanduM
     * @return
     */
    @AutoLog(value = "延度主表-添加")
    @ApiOperation(value = "延度主表-添加", notes = "延度主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WYanduM wYanduM) {
        wYanduMService.save(wYanduM);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wYanduM
     * @return
     */
    @AutoLog(value = "延度主表-编辑")
    @ApiOperation(value = "延度主表-编辑", notes = "延度主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WYanduM wYanduM) {
        wYanduMService.updateById(wYanduM);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "延度主表-通过id删除")
    @ApiOperation(value = "延度主表-通过id删除", notes = "延度主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wYanduMService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "延度主表-批量删除")
    @ApiOperation(value = "延度主表-批量删除", notes = "延度主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wYanduMService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "延度主表-通过id查询")
    @ApiOperation(value = "延度主表-通过id查询", notes = "延度主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WYanduM wYanduM = wYanduMService.getById(id);
        if (wYanduM == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wYanduM);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wYanduM
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WYanduM wYanduM) {
        return super.exportXls(request, wYanduM, WYanduM.class, "延度主表");
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
        return super.importExcel(request, response, WYanduM.class);
    }

    /**
     * 延度数据接收
     *
     * @param args
     * @return
     */
    @AutoLog(value = "延度数据接收")
    @ApiOperation(value = "延度数据接收", notes = "延度数据接收")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody WYanduMvo args) {
        WYanduM t = new WYanduM();
        BeanUtils.copyProperties(args, t);
        t.setSubmittime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String samplename = t.getSamplename();
        BigDecimal ydavg = t.getYdavg();
        if (StringUtils.isNotBlank(samplename)) {
            if (samplename.contains("改性") && samplename.contains("老化")) {
                if (ydavg.compareTo(new BigDecimal(150)) >= 0) {
                    t.setIsqualified("合格");
                }else {
                    t.setIsqualified("不合格");
                }
            }

            if (samplename.contains("基性") && samplename.contains("老化")) {
                if (ydavg.compareTo(new BigDecimal(60)) >= 0) {
                    t.setIsqualified("合格");
                } else {
                    t.setIsqualified("不合格");
                }
            }
        }
        if (StringUtils.isNotBlank(t.getSyjid())) {
            QueryWrapper<WYanduM> queryWrapper = new QueryWrapper<WYanduM>();
            queryWrapper.eq("syjid", t.getSyjid());
            QueryWrapper<WYanduS> queryWrappers = new QueryWrapper<WYanduS>();
            queryWrappers.eq("syjid", t.getSyjid());
            List<WYanduM> list = wYanduMService.list(queryWrapper);
            boolean save1 = false;
            if (list.size() > 0) {
                wYanduSService.remove(queryWrappers);
                boolean update = wYanduMService.update(t, queryWrapper);
                save1 = wYanduSService.saveBatch(args.getYanduss());
                if (update) {
                    return Result.OK("更新成功！");
                } else {
                    return Result.error(201, "更新失败！");
                }
            } else {
                boolean save = wYanduMService.save(t);
                save1 = wYanduSService.saveBatch(args.getYanduss());
                if (save) {
                    return Result.OK("添加成功！");
                } else {
                    return Result.error(201, "数据添加失败！");
                }
            }
        } else {
            return Result.error(201, "syjid不可为空！");
        }
    }

    @AutoLog(value = "路面看板（试验检测）水稳合格率")
    @ApiOperation(value = "路面看板（试验检测）水稳合格率", notes = "路面看板（试验检测）水稳合格率")
    @GetMapping(value = "/SYSwHeGeLv")
    public Result<?> SYSwHeGeLv(String sys_org_code, Integer type, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_org_code == null) {
            sys_org_code = loginUser.getOrgCode();
        }
        if (type == 0) {
            double w_ruanhuadian_m = swHeGeLv("w_ruanhuadian_m", 5, sys_org_code);
            double w_wendingdu_m = swHeGeLv("w_wendingdu_m", 8, sys_org_code);
            double w_yandu_m = swHeGeLv("w_yandu_m", 7, sys_org_code);
            double w_zhenrudu_m = swHeGeLv("w_zhenrudu_m", 6, sys_org_code);
            Map result = new HashMap();
            result.put("ruanhuadian", w_ruanhuadian_m);
            result.put("wendingdu", w_wendingdu_m);
            result.put("yandu", w_yandu_m);
            result.put("zhenrudu", w_zhenrudu_m);
            return Result.OK(result);
        }
        return Result.OK("该抽检状态下没有数据");


    }

    public double swHeGeLv(String tableName, Integer SbType, String sys_org_code) {
        List<String> shebeiList = new ArrayList<>();
        QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
        shebeiInfoQueryWrapper.likeRight("sys_org_code", sys_org_code);
        shebeiInfoQueryWrapper.eq("sbtype", SbType);
        List<ShebeiInfo> shebei = shebeiInfoService.list(shebeiInfoQueryWrapper);
        if (shebei != null || shebei.size() != 0) {
            for (ShebeiInfo shebeiInfo : shebei) {
                shebeiList.add(shebeiInfo.getSbjno());
            }
        }
        Map map = wYanduMService.selectSYSwHeGeLv(tableName, shebeiList);
        int bhgDish = Integer.parseInt(map.get("bhgDish").toString());
        int hgDish = Integer.parseInt(map.get("hgDish").toString());
        int allDish = bhgDish + hgDish;
        //合格率
        double hgLv = 0.0;
        if (allDish == 0) {
            hgLv = 100.0;
        } else if (allDish > 0 && hgDish == 0) {
            hgLv = (double) 0;
        } else {
            hgLv = ((double) hgDish / (double) allDish);
            BigDecimal b = new BigDecimal(hgLv * 100);
            hgLv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return hgLv;
    }
}
