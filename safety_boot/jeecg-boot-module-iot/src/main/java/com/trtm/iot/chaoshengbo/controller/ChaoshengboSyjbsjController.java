package com.trtm.iot.chaoshengbo.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSybltsj;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSybsj;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyjbsjs;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSybltsjService;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSybsjService;
import com.trtm.iot.chaoshengbo.vo.ChaoshengboSyjbsjPage;
import com.trtm.iot.kongjingbasicinfo.entity.KongjingBasicinfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyjbsj;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSyjbsjService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: chaoshengbo_syjbsj
 * @Author: jeecg-boot
 * @Date: 2021-04-08
 * @Version: V1.0
 */
@Api(tags = "chaoshengbo_syjbsj")
@RestController
@RequestMapping("/chaoshengbo/chaoshengboSyjbsj")
@Slf4j
public class ChaoshengboSyjbsjController extends JeecgController<ChaoshengboSyjbsj, IChaoshengboSyjbsjService> {
    @Autowired
    private IChaoshengboSyjbsjService chaoshengboSyjbsjService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IChaoshengboSyjbsjService iChaoshengboSyjbsjService;
    @Autowired
    private IChaoshengboSybltsjService chaoshengboSybltsjService;
    @Autowired
    private IChaoshengboSybsjService iChaoshengboSybsjService;
    @Autowired
    private IChaoshengboSybsjService sybsjService;

    /**
     * 分页列表查询
     *
     * @param chaoshengboSyjbsj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-分页列表查询")
    @ApiOperation(value = "chaoshengbo_syjbsj-分页列表查询", notes = "chaoshengbo_syjbsj-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ChaoshengboSyjbsj chaoshengboSyjbsj,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (chaoshengboSyjbsj.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                chaoshengboSyjbsj.setShebeino(shebei);
            } else {
                chaoshengboSyjbsj.setShebeino("空");
            }
        }
        chaoshengboSyjbsj.setSgbw("*" + chaoshengboSyjbsj.getSgbw() + "*");
        QueryWrapper<ChaoshengboSyjbsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSyjbsj, req.getParameterMap());
        Page<ChaoshengboSyjbsj> page = new Page<ChaoshengboSyjbsj>(pageNo, pageSize);
        IPage<ChaoshengboSyjbsj> pageList = chaoshengboSyjbsjService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 查询详情
     *
     * @param chaoshengboSyjbsj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-查询详情")
    @ApiOperation(value = "chaoshengbo_syjbsj-查询详情", notes = "chaoshengbo_syjbsj-查询详情")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList2(ChaoshengboSyjbsj chaoshengboSyjbsj,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (chaoshengboSyjbsj.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                chaoshengboSyjbsj.setShebeino(shebei);
            } else {
                chaoshengboSyjbsj.setShebeino("空");
            }
        }
        QueryWrapper<ChaoshengboSyjbsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSyjbsj, req.getParameterMap());
        List<ChaoshengboSyjbsj> pageList = chaoshengboSyjbsjService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 查询详情
     *
     * @param chaoshengboSyjbsj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-查询详情")
    @ApiOperation(value = "chaoshengbo_syjbsj-查询详情", notes = "chaoshengbo_syjbsj-查询详情")
    @GetMapping(value = "/baseCurve")
    public Result<?> queryPageList3(ChaoshengboSyjbsj chaoshengboSyjbsj,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (chaoshengboSyjbsj.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                chaoshengboSyjbsj.setShebeino(shebei);
            } else {
                chaoshengboSyjbsj.setShebeino("空");
            }
        }
        QueryWrapper<ChaoshengboSyjbsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSyjbsj, req.getParameterMap());
        Page<ChaoshengboSyjbsj> page = new Page<ChaoshengboSyjbsj>(pageNo, pageSize);
        IPage<ChaoshengboSyjbsj> pageList = chaoshengboSyjbsjService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 查询详情曲线2
     *
     * @param chaoshengboSyjbsj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-查询详情")
    @ApiOperation(value = "chaoshengbo_syjbsj-查询详情", notes = "chaoshengbo_syjbsj-查询详情")
    @GetMapping(value = "/baseCurve1")
    public Result<?> queryPageList4(ChaoshengboSyjbsj chaoshengboSyjbsj,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (chaoshengboSyjbsj.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                chaoshengboSyjbsj.setShebeino(shebei);
            } else {
                chaoshengboSyjbsj.setShebeino("空");
            }
        }
        QueryWrapper<ChaoshengboSyjbsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSyjbsj, req.getParameterMap());
        Page<ChaoshengboSyjbsj> page = new Page<ChaoshengboSyjbsj>(pageNo, pageSize);
        IPage<ChaoshengboSyjbsj> pageList = chaoshengboSyjbsjService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 查询详情曲线3
     *
     * @param chaoshengboSyjbsj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-查询详情")
    @ApiOperation(value = "chaoshengbo_syjbsj-查询详情", notes = "chaoshengbo_syjbsj-查询详情")
    @GetMapping(value = "/baseCurve2")
    public Result<?> queryPageList5(ChaoshengboSyjbsj chaoshengboSyjbsj,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (chaoshengboSyjbsj.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                chaoshengboSyjbsj.setShebeino(shebei);
            } else {
                chaoshengboSyjbsj.setShebeino("空");
            }
        }
        QueryWrapper<ChaoshengboSyjbsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSyjbsj, req.getParameterMap());
        Page<ChaoshengboSyjbsj> page = new Page<ChaoshengboSyjbsj>(pageNo, pageSize);
        IPage<ChaoshengboSyjbsj> pageList = chaoshengboSyjbsjService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 查询波列图
     *
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-查询波列图")
    @ApiOperation(value = "chaoshengbo_syjbsj-查询波列图", notes = "chaoshengbo_syjbsj-查询波列图")
    @GetMapping(value = "/baseCurve3")
    public Result<?> queryThewaveTrainchart(ChaoshengboSybltsj chaoshengboSybltsj, HttpServletRequest request) {
        QueryWrapper<ChaoshengboSybltsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSybltsj, request.getParameterMap());
        List<ChaoshengboSybltsj> pageList = chaoshengboSybltsjService.list(queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 查询剖面数据
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-查询剖面")
    @ApiOperation(value = "chaoshengbo_syjbsj-查询剖面", notes = "chaoshengbo_syjbsj-查询剖面")
    @GetMapping(value = "/baseCurve4")
    public Result<?> queryPageList6(ChaoshengboSybsj chaoshengboSybsj,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        QueryWrapper<ChaoshengboSybsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSybsj, req.getParameterMap());
        List<ChaoshengboSybsj> pageList = sybsjService.list(queryWrapper);
        if (pageList.size() > 0) {
        }
        return Result.OK(pageList);
    }


    /**
     * 添加
     *
     * @param chaoshengboSyjbsj
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-添加")
    @ApiOperation(value = "chaoshengbo_syjbsj-添加", notes = "chaoshengbo_syjbsj-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ChaoshengboSyjbsj chaoshengboSyjbsj) {
        chaoshengboSyjbsjService.save(chaoshengboSyjbsj);
        return Result.OK("添加成功！");
    }

    /**
     * chaoshengbo_syjbsj添加数据对外开放
     *
     * @param chaoshengboSyjbsjPage
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-添加")
    @ApiOperation(value = "chaoshengbo_syjbsj-添加", notes = "chaoshengbo_syjbsj-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody ChaoshengboSyjbsjPage chaoshengboSyjbsjPage) {
        ChaoshengboSyjbsjs chaoshengboSyjbsj = new ChaoshengboSyjbsjs();
        BeanUtils.copyProperties(chaoshengboSyjbsjPage, chaoshengboSyjbsj);
        int code = chaoshengboSyjbsjService.saveMain(chaoshengboSyjbsj, chaoshengboSyjbsjPage.getChaoshengboSybltsjList(), chaoshengboSyjbsjPage.getChaoshengboSybsjList(), chaoshengboSyjbsjPage.getChaoshengboSydsjList(), chaoshengboSyjbsjPage.getChaoshengboSyjsbList(), chaoshengboSyjbsjPage.getChaoshengboSyljzsList(), chaoshengboSyjbsjPage.getChaoshengboSyqxsjList());
        if (code == 200) {
            return Result.OK("添加成功！");
        } else {
            return Result.error("添加失败！");
        }
    }

    /**
     * 编辑
     *
     * @param chaoshengboSyjbsj
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-编辑")
    @ApiOperation(value = "chaoshengbo_syjbsj-编辑", notes = "chaoshengbo_syjbsj-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ChaoshengboSyjbsj chaoshengboSyjbsj) {
        chaoshengboSyjbsjService.updateById(chaoshengboSyjbsj);
        return Result.OK("编辑成功!");
    }

    /**
     * 编辑
     *
     * @param chaoshengboSyjbsj
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-编辑")
    @ApiOperation(value = "chaoshengbo_syjbsj-编辑", notes = "chaoshengbo_syjbsj-编辑")
    @PutMapping(value = "/editById")
    public Result<?> editById(@RequestBody ChaoshengboSyjbsj chaoshengboSyjbsj) {
        Integer id = chaoshengboSyjbsj.getId();
        QueryWrapper<ChaoshengboSyjbsj> query = new QueryWrapper<>();
        query.eq("id", id);
        ChaoshengboSyjbsj one = chaoshengboSyjbsjService.getOne(query);

        Integer level = chaoshengboSyjbsj.getLevel();
        if (ObjectUtil.isEmpty(chaoshengboSyjbsj.getLevel())) {
            level = one.getLevel();
        }
        String file = chaoshengboSyjbsj.getFile();
        if (ObjectUtil.isEmpty(chaoshengboSyjbsj.getFile())) {
            file = one.getFile();
        }
        String fileInfo = chaoshengboSyjbsj.getFileInfo();
        if (ObjectUtil.isEmpty(chaoshengboSyjbsj.getFileInfo())) {
            fileInfo = one.getFileInfo();
        }
        chaoshengboSyjbsjService.updateByIdToYb(level, file, fileInfo, id);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-通过id删除")
    @ApiOperation(value = "chaoshengbo_syjbsj-通过id删除", notes = "chaoshengbo_syjbsj-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        chaoshengboSyjbsjService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-批量删除")
    @ApiOperation(value = "chaoshengbo_syjbsj-批量删除", notes = "chaoshengbo_syjbsj-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.chaoshengboSyjbsjService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-通过id查询")
    @ApiOperation(value = "chaoshengbo_syjbsj-通过id查询", notes = "chaoshengbo_syjbsj-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ChaoshengboSyjbsj chaoshengboSyjbsj = chaoshengboSyjbsjService.getById(id);
        if (chaoshengboSyjbsj == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(chaoshengboSyjbsj);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param chaoshengboSyjbsj
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ChaoshengboSyjbsj chaoshengboSyjbsj) {
        return super.exportXls(request, chaoshengboSyjbsj, ChaoshengboSyjbsj.class, "chaoshengbo_syjbsj");
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
        return super.importExcel(request, response, ChaoshengboSyjbsj.class);
    }

    /**
     * 桩基各评级数
     *
     * @param chaoshengboSyjbsj
     * @param date
     * @return
     */
    @AutoLog(value = "桩基各评级数")
    @ApiOperation(value = "桩基各评级数", notes = "桩基各评级数")
    @GetMapping(value = "/statisticsZJData")
    public Result<?> statisticsZJData(ChaoshengboSyjbsj chaoshengboSyjbsj,
                                    Integer date) {
        String shebei = null;
        if (ObjectUtil.isEmpty(chaoshengboSyjbsj.getShebeino())) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        } else {
            shebei = chaoshengboSyjbsj.getShebeino();
        }
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        Map map = chaoshengboSyjbsjService.statisticsLevelZB(shebeilist, date);
        return Result.OK(map);
    }

    /**
     * 桩基各评级数统计
     *
     * @param chaoshengboSyjbsj
     * @param date
     * @return
     */
    @AutoLog(value = "桩基各评级数统计")
    @ApiOperation(value = "桩基各评级数统计", notes = "桩基各评级数统计")
    @GetMapping(value = "/getListZJStatistics")
    public Result<?> getListZJStatistics(ChaoshengboSyjbsj chaoshengboSyjbsj,
                                         Integer date) {
        String shebei = null;
        if (ObjectUtil.isEmpty(chaoshengboSyjbsj.getShebeino())) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        } else {
            shebei = chaoshengboSyjbsj.getShebeino();
        }
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        List list = chaoshengboSyjbsjService.tongjiZJLevel(shebeilist, date);
        return Result.OK(list);
    }

}
