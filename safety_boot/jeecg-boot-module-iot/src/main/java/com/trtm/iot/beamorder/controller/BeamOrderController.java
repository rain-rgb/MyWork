package com.trtm.iot.beamorder.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.beamorder.entity.BeamOrder;
import com.trtm.iot.beamorder.service.IBeamOrderService;

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

/**
 * @Description: 粱订单信息表
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
@Api(tags = "粱订单信息表")
@RestController
@RequestMapping("/beamorder/beamOrder")
@Slf4j
public class BeamOrderController extends JeecgController<BeamOrder, IBeamOrderService> {
    @Autowired
    private IBeamOrderService beamOrderService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;

    /**
     * 分页列表查询
     *
     * @param beamOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "粱订单信息表-分页列表查询")
    @ApiOperation(value = "粱订单信息表-分页列表查询", notes = "粱订单信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BeamOrder beamOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_orgcode,Integer statuuss) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null) {
            beamOrder.setSysOrgCode(sys_depart_orgcode + "*");//右模糊查询
        } else {
            beamOrder.setSysOrgCode(loginUser.getOrgCode() + "*");//右模糊查询
        }
        QueryWrapper<BeamOrder> queryWrapper = QueryGenerator.initQueryWrapper(beamOrder, req.getParameterMap());
        Page<BeamOrder> page = new Page<BeamOrder>(pageNo, pageSize);
        IPage<BeamOrder> pageList = beamOrderService.page(page, queryWrapper);
        List<BeamOrder> records = pageList.getRecords();//BeamOrder记录
        List<BeamOrder> list = new ArrayList<>();
        for (BeamOrder beamOrder1 : records) {
            double beamnum = 0.0;
            if (beamOrder1.getBeamNum() != null) {
                beamnum = beamOrder1.getBeamNum();
            }
            QueryWrapper<Zhiliangrenwudan> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("count(Code) as Code");
            queryWrapper1.eq("oderno", beamOrder1.getOrderno());
            queryWrapper1.eq("sfhg","合格");
            Zhiliangrenwudan zhiliangrenwudan1 = zhiliangrenwudanService.getOne(queryWrapper1);
            double code = Double.parseDouble(zhiliangrenwudan1.getCode());
            double orderProgress = 0.0;
            long endtime = beamOrder1.getDeliveryDate().getTime();
            long date = new Date().getTime();
            if (beamnum != 0) {
                orderProgress = code / beamnum * 100;
                if (orderProgress >= 100) {
                    if (statuuss!=null && statuuss!=2){
                        list.add(beamOrder1);
                    }
                    beamOrder1.setOrderStatus(2);
                } else if (orderProgress == 0) {
                    if (statuuss!=null && statuuss!=0){
                        list.add(beamOrder1);
                    }
                    beamOrder1.setOrderStatus(0);
                } else {
                    if (endtime >= date) {
                        if (statuuss!=null && statuuss!=1){
                            list.add(beamOrder1);
                        }
                        beamOrder1.setOrderStatus(1);
                    } else {
                        if (statuuss!=null && statuuss!=3){
                            list.add(beamOrder1);
                        }
                        beamOrder1.setOrderStatus(3);
                    }
                }
            }
            beamOrder1.setOrderProgress(String.format("%.2f", orderProgress));
        }
        records.removeAll(list);
        return Result.OK(pageList);
    }

    @AutoLog(value = "粱订单信息表-分页列表查询")
    @ApiOperation(value = "粱订单信息表-分页列表查询", notes = "粱订单信息表-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(BeamOrder beamOrder,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null) {
            beamOrder.setSysOrgCode(sys_depart_orgcode + "*");//右模糊查询
        } else {
            beamOrder.setSysOrgCode(loginUser.getOrgCode() + "*");//右模糊查询
        }
        QueryWrapper<BeamOrder> queryWrapper = QueryGenerator.initQueryWrapper(beamOrder, req.getParameterMap());
        List<BeamOrder> pageList = beamOrderService.list(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "粱订单信息表-订单进度统计")
    @ApiOperation(value = "粱订单信息表-订单进度统计", notes = "粱订单信息表-订单进度统计")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(BeamOrder beamOrder,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null) {
            beamOrder.setSysOrgCode(sys_depart_orgcode + "*");//右模糊查询
        } else {
            beamOrder.setSysOrgCode(loginUser.getOrgCode() + "*");//右模糊查询
        }
        QueryWrapper<BeamOrder> queryWrapper = QueryGenerator.initQueryWrapper(beamOrder, req.getParameterMap());
        List<BeamOrder> pageList = beamOrderService.list(queryWrapper);
        int notstarted = 0;
        int inproduction = 0;
        int paid = 0;
        for (BeamOrder beamOrder1 : pageList) {
            QueryWrapper<Zhiliangrenwudan> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("oderno", beamOrder1.getOrderno());
            queryWrapper1.eq("sfhg","合格");
            List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper1);
            if (list.size() == 0) {
                notstarted += 1;
            } else if (list.size() < beamOrder1.getBeamNum()) {
                inproduction += 1;
            } else {
                paid += 1;
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("notstarted",notstarted);
        map.put("inproduction",inproduction);
        map.put("paid",paid);
        return Result.OK(map);
    }

    /**
     * 添加
     *
     * @param beamOrder
     * @return
     */
    @AutoLog(value = "粱订单信息表-添加")
    @ApiOperation(value = "粱订单信息表-添加", notes = "粱订单信息表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BeamOrder beamOrder) {
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        beamOrder.setUuid(uuid);
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        if (StringUtils.isEmpty(beamOrder.getOrderno())) {
            beamOrder.setOrderno(format.format(date));
        }
        beamOrderService.save(beamOrder);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param beamOrder
     * @return
     */
    @AutoLog(value = "粱订单信息表-编辑")
    @ApiOperation(value = "粱订单信息表-编辑", notes = "粱订单信息表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BeamOrder beamOrder) {
        beamOrderService.updateById(beamOrder);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "粱订单信息表-通过id删除")
    @ApiOperation(value = "粱订单信息表-通过id删除", notes = "粱订单信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) Integer id) {
        BeamOrder beamOrder = beamOrderService.getById(id);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oderno",beamOrder.getOrderno());
        List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.list(queryWrapper);
        if (zhiliangrenwudanList.size()>0){
            return Result.error("已创建制梁任务单, 禁止删除!");
        }else {
            beamOrderService.removeById(id);
            return Result.OK("删除成功!");
        }

    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "粱订单信息表-批量删除")
    @ApiOperation(value = "粱订单信息表-批量删除", notes = "粱订单信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.beamOrderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "粱订单信息表-通过id查询")
    @ApiOperation(value = "粱订单信息表-通过id查询", notes = "粱订单信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BeamOrder beamOrder = beamOrderService.getById(id);
        if (beamOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(beamOrder);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param beamOrder
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BeamOrder beamOrder) {
        return super.exportXls(request, beamOrder, BeamOrder.class, "粱订单信息表");
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
        return super.importExcel(request, response, BeamOrder.class);
    }

}
