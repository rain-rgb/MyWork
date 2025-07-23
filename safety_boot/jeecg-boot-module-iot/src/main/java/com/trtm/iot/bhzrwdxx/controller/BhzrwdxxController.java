package com.trtm.iot.bhzrwdxx.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.service.IRenwudanScheduleService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzrwdxx.entity.Bhzrwdxx;
import com.trtm.iot.bhzrwdxx.service.IBhzrwdxxService;

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
 * @Description: bhzrwdxx
 * @Author: jeecg-boot
 * @Date: 2022-03-02
 * @Version: V1.0
 */
@Api(tags = "bhzrwdxx")
@RestController
@RequestMapping("/bhzrwdxx/bhzrwdxx")
@Slf4j
public class BhzrwdxxController extends JeecgController<Bhzrwdxx, IBhzrwdxxService> {
    @Autowired
    private IBhzrwdxxService bhzrwdxxService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IRenwudanScheduleService renwudanScheduleService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    private ISchedulingCarService schedulingCarService;

    /**
     * 分页列表查询
     *
     * @param bhzrwdxx
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhzrwdxx-分页列表查询")
    @ApiOperation(value = "bhzrwdxx-分页列表查询", notes = "bhzrwdxx-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Bhzrenwudan bhzrwdxx,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrwdxx.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            bhzrwdxx.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        bhzrwdxx.setConspos("*" + bhzrwdxx.getConspos() + "*");
        bhzrwdxx.setIsdel(0);
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrwdxx, req.getParameterMap());
        Page<Bhzrenwudan> page = new Page<Bhzrenwudan>(pageNo, pageSize);
        IPage<Bhzrenwudan> pageList = bhzrenwudanService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    @AutoLog(value = "bhzrwdxx-分页列表查询")
    @ApiOperation(value = "bhzrwdxx-分页列表查询", notes = "bhzrwdxx-分页列表查询")
    @GetMapping(value = "/listjzl")
    public Result<?> listjzl(Bhzrenwudan bhzrenwudan,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                             String sys_depart_orgcode,
                             HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            bhzrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        bhzrenwudan.setConspos("*" + bhzrenwudan.getConspos() + "*");
        bhzrenwudan.setIsdel(0);
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        Page<Bhzrenwudan> page = new Page<Bhzrenwudan>(pageNo, pageSize);
        IPage<Bhzrenwudan> pageList = bhzrenwudanService.page(page, queryWrapper);
        List<Bhzrwdxx> bhzrwdxxList = new ArrayList<>();
        for (Bhzrenwudan record : pageList.getRecords()) {
            Bhzrwdxx bhzrwdxx = new Bhzrwdxx();
            QueryWrapper<RenwudanSchedule> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("Code,EndTim,Starttim,shebei_no,sum( Metes ) AS Metes,Mete");
            queryWrapper1.eq("Code", record.getCode());
            queryWrapper1.likeRight("sys_org_code", record.getSysOrgCode());
//            queryWrapper1.last("group by Code,sys_org_code");
            RenwudanSchedule one = renwudanScheduleService.getOne(queryWrapper1);
            BeanUtils.copyProperties(record,bhzrwdxx);
            bhzrwdxx.setRwdcode(record.getCode());
            bhzrwdxx.setJzlsts(record.getStatus());
            if(one!=null){
                bhzrwdxx.setShebeiNo(one.getShebeiNo());
                bhzrwdxx.setStarttim(one.getStarttim());
                bhzrwdxx.setEndtim(one.getEndtim());
                bhzrwdxx.setMetes(one.getMetes());
                bhzrwdxx.setMete(one.getMete());
                String meteByCode = schedulingCarService.getMeteByCode(record.getCode());
                bhzrwdxx.setNote(meteByCode); // 运输方量
                // 生产方量大于任务放量默认设置 0:未审核,1:已审核,2:审核未配料,3:配料未生产,4:生产中,5:已完成,6:已滞后
//                if(one.getMete()<=one.getMetes()){
//                    bhzrwdxx.setJzlsts(5);
//                }

            }
            if(bhzrwdxx.getBfb() != null && bhzrwdxx.getBfb() >= 100){
                bhzrwdxx.setJzlsts(5);
            }
            bhzrwdxxList.add(bhzrwdxx);
        }
        Map map =new HashMap();
        map.put("pages",pageList.getPages());
        map.put("size",pageList.getSize());
        map.put("total",pageList.getTotal());
        map.put("current",pageList.getCurrent());
        map.put("records",bhzrwdxxList);
        return Result.OK(map);
    }

    @AutoLog(value = "bhzrwdxx-分页列表查询")
    @ApiOperation(value = "bhzrwdxx-分页列表查询", notes = "bhzrwdxx-分页列表查询")
    @GetMapping(value = "/listtj")
    public Result<?> queryPageListtj(Bhzrenwudan bhzrwdxx,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信

        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrwdxx.setSysOrgCode(sys_depart_orgcode + "*");
        }else{
            bhzrwdxx.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        bhzrwdxx.setConspos("*" + bhzrwdxx.getConspos() + "*");
        bhzrwdxx.setIsdel(0);
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrwdxx, req.getParameterMap());
        queryWrapper.select(" sum(case WHEN status = 0 then 1 else 0 END ) weishenhe, " +
                " sum(case WHEN status = 1 then 1 else 0 END ) shenhe, " +
                " sum(case WHEN status = 3 then 1 else 0 END) peiliao, " +
                " sum(case WHEN status = 4 then 1 else 0 END ) shenchan, " +
                " sum(case WHEN status = 6 then 1 else 0 END) zhihou, " +
                " sum(case WHEN status = 5 then 1 else 0 END) wancheng ");
        Map<String, Object> map = bhzrenwudanService.getMap(queryWrapper);
        return Result.OK(map);
    }

    @AutoLog(value = "bhzrwdxx-分页列表查询")
    @ApiOperation(value = "bhzrwdxx-分页列表查询", notes = "bhzrwdxx-分页列表查询")
    @GetMapping(value = "/rwdprocess")
    public Result<?> rwdprocess(Bhzrwdxx bhzrwdxx,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                String sys_depart_orgcode,
                                HttpServletRequest req) {
        ShebeiInfo selectshebeione = new ShebeiInfo();
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrwdxx.setSysOrgCode(sys_depart_orgcode + "*");
        }
        bhzrwdxx.setConspos("*" + bhzrwdxx.getConspos() + "*");
        bhzrwdxx.setIsdel(0);
        QueryWrapper<Bhzrwdxx> queryWrapper = QueryGenerator.initQueryWrapper(bhzrwdxx, req.getParameterMap());
        Page<Bhzrwdxx> page = new Page<Bhzrwdxx>(pageNo, pageSize);
        IPage<Bhzrwdxx> pageList = bhzrwdxxService.page(page, queryWrapper);
        List<Map<String, Object>> rwdprosess = new ArrayList<>();
        if (pageList.getSize() > 0) {
            Bhzrwdxx rwd = pageList.getRecords().get(0);
            Map<String, Object> qita = new HashMap<>();
            Map<String, Object> create = new HashMap<>();
            create.put("tile", "创建");
            Map<String, Object> shenhe = new HashMap<>();
            shenhe.put("tile", "审核");
            Map<String, Object> peiliao = new HashMap<>();
            peiliao.put("tile", "配料");
            Map<String, Object> shenchan = new HashMap<>();
            if (rwd.getJzlsts() == 4) {
                shenchan.put("tile", "生产已滞后");
            } else {
                shenchan.put("tile", "生产中");
            }
            Map<String, Object> wancheng = new HashMap<>();
            wancheng.put("tile", "已完成");

            create.put("time", rwd.getDattim());
            create.put("person", rwd.getCreateBy());
            create.put("content", "");
            create.put("status", 1);


            if (rwd.getJzlsts() > 0) {
                shenhe.put("time", null);
                shenhe.put("person", "");
                shenhe.put("content", "浇筑令已通过审核");
                shenhe.put("status", 1);
            } else {
                shenhe.put("status", 0);
            }
            if (rwd.getJzlsts() > 1) {
                QueryWrapper<Shigongphb> phb = new QueryWrapper<>();
                phb.eq("renwudan", rwd.getRwdcode());
                phb.eq("isdel", 0);
                List<Shigongphb> phblist = shigongphbService.list(phb);
                if (phblist.size() > 0) {
                    Shigongphb shigongphb = phblist.get(0);
                    peiliao.put("time", shigongphb.getDattim());
                    peiliao.put("person", shigongphb.getCreateBy());
                    peiliao.put("content", shigongphb.getCode());
                    peiliao.put("status", 1);

                }
            } else {
                peiliao.put("status", 0);
            }

            if (null != rwd.getShebeiNo()) {
                selectshebeione = shebeiInfoService.selectshebeione(rwd.getShebeiNo());
            }
            if (rwd.getJzlsts() > 2) {
                shenchan.put("time", rwd.getStarttim());
                shenchan.put("person", selectshebeione.getSbname());
                shenchan.put("content", "计划方量" + rwd.getMete() + "方,已完成" + rwd.getMetes() + "方");
                shenchan.put("status", 1);

            } else {
                shenchan.put("status", 0);
            }
            if (rwd.getJzlsts() == 5) {
                wancheng.put("time", rwd.getEndtim1());
                wancheng.put("person", selectshebeione.getSbname());
                wancheng.put("content", "");
                wancheng.put("status", 1);

            } else {
                wancheng.put("status", 0);
            }

            rwdprosess.add(wancheng);
            rwdprosess.add(shenchan);
            rwdprosess.add(peiliao);
            rwdprosess.add(shenhe);
            rwdprosess.add(create);
        }

        return Result.OK(rwdprosess);
    }
    @AutoLog(value = "bhzrwdxx-分页列表查询")
    @ApiOperation(value = "bhzrwdxx-分页列表查询", notes = "bhzrwdxx-分页列表查询")
    @GetMapping(value = "/rwdprocess2")
    public Result<?> rwdprocess2(Bhzrenwudan bhzrenwudan,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                String sys_depart_orgcode,
                                HttpServletRequest req) {
        ShebeiInfo selectshebeione = new ShebeiInfo();
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        bhzrenwudan.setConspos("*" + bhzrenwudan.getConspos() + "*");
        bhzrenwudan.setIsdel(0);
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        Page<Bhzrenwudan> page = new Page<Bhzrenwudan>(pageNo, pageSize);
        IPage<Bhzrenwudan> pageList = bhzrenwudanService.page(page, queryWrapper);
        List<Map<String, Object>> rwdprosess = new ArrayList<>();
        if (pageList.getSize() > 0) {
            Bhzrenwudan rwd = pageList.getRecords().get(0);
            Bhzrwdxx bhzrwdxx = new Bhzrwdxx();
            QueryWrapper<RenwudanSchedule> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("Code,EndTim,Starttim,shebei_no,sum( Metes ) AS Metes,Mete");
            queryWrapper1.eq("Code", rwd.getCode());
            queryWrapper1.eq("sys_org_code", rwd.getSysOrgCode());
//            queryWrapper1.last("group by Code,sys_org_code");
            RenwudanSchedule one = renwudanScheduleService.getOne(queryWrapper1);
            BeanUtils.copyProperties(rwd,bhzrwdxx);
            bhzrwdxx.setRwdcode(rwd.getCode());
            bhzrwdxx.setJzlsts(rwd.getStatus());
            if(one!=null){
                bhzrwdxx.setShebeiNo(one.getShebeiNo());
                bhzrwdxx.setStarttim(one.getStarttim());
                bhzrwdxx.setEndtim(one.getEndtim());
                bhzrwdxx.setMetes(one.getMetes());
                bhzrwdxx.setMete(one.getMete());
            }
            Map<String, Object> qita = new HashMap<>();
            Map<String, Object> create = new HashMap<>();
            create.put("tile", "创建");
            Map<String, Object> shenhe = new HashMap<>();
            shenhe.put("tile", "审核");
            Map<String, Object> peiliao = new HashMap<>();
            peiliao.put("tile", "配料");
            Map<String, Object> jlsh = new HashMap<>();
            jlsh.put("status", 0);
            jlsh.put("tile", "监理确认");

            Map<String, Object> shenchan = new HashMap<>();
            if (rwd.getStatus() == 6) {
                shenchan.put("tile", "生产已滞后");
            } else {
                shenchan.put("tile", "生产中");
            }
            Map<String, Object> wancheng = new HashMap<>();
            wancheng.put("tile", "已完成");

            create.put("time", bhzrwdxx.getDattim());
            create.put("person", bhzrwdxx.getCreateBy());
            create.put("content", "");
            create.put("status", 1);


            if (rwd.getStatus() > 0) {
                shenhe.put("time", rwd.getShshijian());
                shenhe.put("person", rwd.getShren());
                shenhe.put("content", "浇筑令已通过审核");
                shenhe.put("status", 1);
            } else {
                shenhe.put("status", 0);
            }
            if (rwd.getStatus() > 1) {
                QueryWrapper<Shigongphb> phb = new QueryWrapper<>();
                phb.eq("renwudan", rwd.getCode());
                phb.eq("isdel", 0);
                List<Shigongphb> phblist = shigongphbService.list(phb);
                if (phblist.size() > 0) {
                    Shigongphb shigongphb = phblist.get(0);
                    peiliao.put("time", shigongphb.getDattim());
                    peiliao.put("person", shigongphb.getCreateBy());
                    peiliao.put("content", shigongphb.getCode());
                    peiliao.put("status", 1);
                    if(shigongphb.getCheckStatus() != null ){
                        jlsh.put("content", "已审核");
                        jlsh.put("status", 1);
                        jlsh.put("time", shigongphb.getShshijian());
                        jlsh.put("person", shigongphb.getShren());
                    }


                }
            } else {
                peiliao.put("status", 0);
            }

            if (null != bhzrwdxx.getShebeiNo()) {
                selectshebeione = shebeiInfoService.selectshebeione(bhzrwdxx.getShebeiNo());
            }
            if (bhzrwdxx.getJzlsts() > 3) {
                shenchan.put("time", bhzrwdxx.getStarttim());
                shenchan.put("person", selectshebeione.getSbname());
                shenchan.put("content", "计划方量" + rwd.getMete() + "方,已完成" + bhzrwdxx.getMetes() + "方");
                shenchan.put("status", 1);

            } else {
                shenchan.put("status", 0);
            }
            if (bhzrwdxx.getJzlsts() == 5) {
                wancheng.put("time", bhzrwdxx.getEndtim1());
                wancheng.put("person", selectshebeione.getSbname());
                wancheng.put("content", "");
                wancheng.put("status", 1);

            } else {
                wancheng.put("status", 0);
            }

            rwdprosess.add(wancheng);
            rwdprosess.add(shenchan);
            rwdprosess.add(jlsh);
            rwdprosess.add(peiliao);
            rwdprosess.add(shenhe);
            rwdprosess.add(create);
        }

        return Result.OK(rwdprosess);
    }

    /**
     * 添加
     *
     * @param bhzrwdxx
     * @return
     */
    @AutoLog(value = "bhzrwdxx-添加")
    @ApiOperation(value = "bhzrwdxx-添加", notes = "bhzrwdxx-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Bhzrwdxx bhzrwdxx) {
        bhzrwdxxService.save(bhzrwdxx);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bhzrwdxx
     * @return
     */
    @AutoLog(value = "bhzrwdxx-编辑")
    @ApiOperation(value = "bhzrwdxx-编辑", notes = "bhzrwdxx-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Bhzrwdxx bhzrwdxx) {
        bhzrwdxxService.updateById(bhzrwdxx);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhzrwdxx-通过id删除")
    @ApiOperation(value = "bhzrwdxx-通过id删除", notes = "bhzrwdxx-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzrwdxxService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "bhzrwdxx-批量删除")
    @ApiOperation(value = "bhzrwdxx-批量删除", notes = "bhzrwdxx-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzrwdxxService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhzrwdxx-通过id查询")
    @ApiOperation(value = "bhzrwdxx-通过id查询", notes = "bhzrwdxx-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Bhzrwdxx bhzrwdxx = bhzrwdxxService.getById(id);
        if (bhzrwdxx == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzrwdxx);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzrwdxx
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Bhzrwdxx bhzrwdxx) {
        return super.exportXls(request, bhzrwdxx, Bhzrwdxx.class, "bhzrwdxx");
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
        return super.importExcel(request, response, Bhzrwdxx.class);
    }

}
