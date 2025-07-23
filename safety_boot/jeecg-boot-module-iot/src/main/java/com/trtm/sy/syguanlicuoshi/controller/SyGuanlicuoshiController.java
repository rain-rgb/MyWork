package com.trtm.sy.syguanlicuoshi.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.syguanlicuoshi.entity.SyGuanlicuoshi;
import com.trtm.sy.syguanlicuoshi.service.ISyGuanlicuoshiService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: sy_guanlicuoshi
 * @Author: jeecg-boot
 * @Date: 2022-09-07
 * @Version: V1.0
 */
@Api(tags = "sy_guanlicuoshi")
@RestController
@RequestMapping("/syguanlicuoshi/syGuanlicuoshi")
@Slf4j
public class SyGuanlicuoshiController extends JeecgController<SyGuanlicuoshi, ISyGuanlicuoshiService> {
    @Autowired
    private ISyGuanlicuoshiService syGuanlicuoshiService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    /**
     * 分页列表查询
     *
     * @param syGuanlicuoshi
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_guanlicuoshi-分页列表查询")
    @ApiOperation(value = "sy_guanlicuoshi-分页列表查询", notes = "sy_guanlicuoshi-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyGuanlicuoshi syGuanlicuoshi,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode == null) {
            syGuanlicuoshi.setComment(loginUser.getOrgCode() + "*");
        } else {
            syGuanlicuoshi.setComment(sys_depart_orgcode + "*");
        }
        QueryWrapper<SyGuanlicuoshi> queryWrapper = QueryGenerator.initQueryWrapper(syGuanlicuoshi, req.getParameterMap());
        Page<SyGuanlicuoshi> page = new Page<SyGuanlicuoshi>(pageNo, pageSize);
        IPage<SyGuanlicuoshi> pageList = syGuanlicuoshiService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param syGuanlicuoshi
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_guanlicuoshi-分页列表查询")
    @ApiOperation(value = "sy_guanlicuoshi-分页列表查询", notes = "sy_guanlicuoshi-分页列表查询")
    @GetMapping(value = "/list2")
    public Result<?> list2(SyGuanlicuoshi syGuanlicuoshi,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                           HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode == null) {
            syGuanlicuoshi.setComment(loginUser.getOrgCode() + "*");
        } else {
            syGuanlicuoshi.setComment(sys_depart_orgcode + "*");
        }
        QueryWrapper<SyGuanlicuoshi> queryWrapper = QueryGenerator.initQueryWrapper(syGuanlicuoshi, req.getParameterMap());
        queryWrapper.isNotNull("bzz_sg_dw");
        Page<SyGuanlicuoshi> page = new Page<SyGuanlicuoshi>(pageNo, pageSize);
        IPage<SyGuanlicuoshi> pageList = syGuanlicuoshiService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param syGuanlicuoshi
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_guanlicuoshi-分页列表查询")
    @ApiOperation(value = "sy_guanlicuoshi-分页列表查询", notes = "sy_guanlicuoshi-分页列表查询")
    @GetMapping(value = "/list3")
    public Result<?> list3(SyGuanlicuoshi syGuanlicuoshi,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                           HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode == null) {
            syGuanlicuoshi.setComment(loginUser.getOrgCode() + "*");
        } else {
            syGuanlicuoshi.setComment(sys_depart_orgcode + "*");
        }
        QueryWrapper<SyGuanlicuoshi> queryWrapper = QueryGenerator.initQueryWrapper(syGuanlicuoshi, req.getParameterMap());
        queryWrapper.isNotNull("bzz_jl_dw");
        Page<SyGuanlicuoshi> page = new Page<SyGuanlicuoshi>(pageNo, pageSize);
        IPage<SyGuanlicuoshi> pageList = syGuanlicuoshiService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 统计信息
     *
     * @return
     */
    @AutoLog(value = "sy_guanlicuoshi-统计信息")
    @ApiOperation(value = "sy_guanlicuoshi-统计信息", notes = "sy_guanlicuoshi-统计信息")
    @GetMapping(value = "/statisticByZzType")
    public Result<?> statisticByZzType(SyGuanlicuoshi syGuanlicuoshi,
                                       HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode == null) {
            syGuanlicuoshi.setComment(loginUser.getOrgCode() + "*");
        } else {
            syGuanlicuoshi.setComment(sys_depart_orgcode + "*");
        }
        //从本月起的前6个月
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM");
        String format2 = ft.format(new Date());
        List<String> monthList = new ArrayList<String>();
        int month = Integer.parseInt(format2.substring(5, 7));
        int year = Integer.parseInt(format2.substring(0, 4));
        for (int i = 5; i >= 0; i--) {
            if (month > 6) {
                if (month - i >= 10) {
                    monthList.add(year + "-" + (month - i));
                } else {
                    monthList.add(year + "-0" + (month - i));
                }
            } else {
                if (month - i <= 0) {
                    if (month - i + 12 >= 10) {
                        monthList.add((year - 1) + "-" + (month - i + 12));
                    } else {
                        monthList.add((year - 1) + "-0" + (month - i + 12));
                    }
                } else {
                    if (month - i >= 10) {
                        monthList.add((year) + "-" + (month - i));
                    } else {
                        monthList.add((year) + "-0" + (month - i));
                    }
                }
            }
        }

        List list = new ArrayList();
        for(String time : monthList) {
            Map map = new HashMap();
            for (int i = 0; i < 3; i++) {
                String name = "";
                if(i==0){
                    name="zero";
                }
                if(i==1){
                    name="one";
                }
                if(i==2){
                    name="two";
                }
                map.put("zzTime", time);
                QueryWrapper<SyGuanlicuoshi> queryWrapper = QueryGenerator.initQueryWrapper(syGuanlicuoshi, req.getParameterMap());
                queryWrapper.select("count(*) as id", "zz_time", "zz_type", "comment");
                queryWrapper.eq("(SELECT DATE_FORMAT(zz_time,'%Y-%m'))", time);
                queryWrapper.eq("zz_type", i);
                SyGuanlicuoshi one = syGuanlicuoshiService.getOne(queryWrapper);
                if (one.getId()==0) {
                    map.put(name+"Count",0);
                } else {
                    map.put(name+"Count",one.getId());
                }

            }
            list.add(map);
        }

        return Result.OK(list);

    }

    /**
     * 添加
     *
     * @param syGuanlicuoshi
     * @return
     */
    @AutoLog(value = "sy_guanlicuoshi-添加")
    @ApiOperation(value = "sy_guanlicuoshi-添加", notes = "sy_guanlicuoshi-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyGuanlicuoshi syGuanlicuoshi) {
        syGuanlicuoshiService.save(syGuanlicuoshi);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syGuanlicuoshi
     * @return
     */
    @AutoLog(value = "sy_guanlicuoshi-编辑")
    @ApiOperation(value = "sy_guanlicuoshi-编辑", notes = "sy_guanlicuoshi-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyGuanlicuoshi syGuanlicuoshi) {
        syGuanlicuoshiService.updateById(syGuanlicuoshi);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_guanlicuoshi-通过id删除")
    @ApiOperation(value = "sy_guanlicuoshi-通过id删除", notes = "sy_guanlicuoshi-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syGuanlicuoshiService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_guanlicuoshi-批量删除")
    @ApiOperation(value = "sy_guanlicuoshi-批量删除", notes = "sy_guanlicuoshi-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syGuanlicuoshiService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_guanlicuoshi-通过id查询")
    @ApiOperation(value = "sy_guanlicuoshi-通过id查询", notes = "sy_guanlicuoshi-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyGuanlicuoshi syGuanlicuoshi = syGuanlicuoshiService.getById(id);
        if (syGuanlicuoshi == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syGuanlicuoshi);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syGuanlicuoshi
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyGuanlicuoshi syGuanlicuoshi) {
        return super.exportXls(request, syGuanlicuoshi, SyGuanlicuoshi.class, "sy_guanlicuoshi");
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
        return super.importExcel(request, response, SyGuanlicuoshi.class);
    }

}
