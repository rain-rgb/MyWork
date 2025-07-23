package com.trtm.iot.bhzSupervisionOrder.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import com.trtm.api.SignController;
import com.trtm.iot.bhzSupervisionOrder.vo.BhzSupervisionOrderVO;
import com.trtm.iot.bhzSupervisionOrderSub.entity.BhzSupervisionOrderSub;
import com.trtm.iot.bhzSupervisionOrderSub.service.IBhzSupervisionOrderSubService;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzSupervisionOrder.entity.BhzSupervisionOrder;
import com.trtm.iot.bhzSupervisionOrder.service.IBhzSupervisionOrderService;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @Description: bhz_supervision_order
 * @Author: jeecg-boot
 * @Date: 2023-02-08
 * @Version: V1.0
 */
@Api(tags = "bhz_supervision_order")
@RestController
@RequestMapping("/bhzSupervisionOrder/bhzSupervisionOrder")
@Slf4j
public class BhzSupervisionOrderController extends JeecgController<BhzSupervisionOrder, IBhzSupervisionOrderService> {
    @Autowired
    private IBhzSupervisionOrderService bhzSupervisionOrderService;
    @Autowired
    private IBhzSupervisionOrderSubService bhzSupervisionOrderSubService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    /**
     * 分页列表查询
     *
     * @param bhzSupervisionOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_supervision_order-分页列表查询")
    @ApiOperation(value = "bhz_supervision_order-分页列表查询", notes = "bhz_supervision_order-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzSupervisionOrder bhzSupervisionOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询他的设备编号
        if (bhzSupervisionOrder.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzSupervisionOrder.setShebeiNo(shebei);
            } else {
                bhzSupervisionOrder.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzSupervisionOrder> queryWrapper = QueryGenerator.initQueryWrapper(bhzSupervisionOrder, req.getParameterMap());
        Page<BhzSupervisionOrder> page = new Page<BhzSupervisionOrder>(pageNo, pageSize);
        IPage<BhzSupervisionOrder> pageList = bhzSupervisionOrderService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
    /**
     * 分页列表查询
     *
     * @param bhzSupervisionOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_supervision_order-分页列表查询")
    @ApiOperation(value = "bhz_supervision_order-分页列表查询", notes = "bhz_supervision_order-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(BhzSupervisionOrder bhzSupervisionOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询他的设备编号
        if (bhzSupervisionOrder.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzSupervisionOrder.setShebeiNo(shebei);
            } else {
                bhzSupervisionOrder.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzSupervisionOrder> queryWrapper = QueryGenerator.initQueryWrapper(bhzSupervisionOrder, req.getParameterMap());
        Page<BhzSupervisionOrder> page = new Page<BhzSupervisionOrder>(pageNo, pageSize);
        IPage<BhzSupervisionOrder> pageList = bhzSupervisionOrderService.page(page, queryWrapper);
        List<BhzSupervisionOrderVO> voList = new ArrayList<>();
        for (BhzSupervisionOrder record : page.getRecords()) {
            BhzSupervisionOrderVO vo = new BhzSupervisionOrderVO();

            BeanUtil.copyProperties(record,vo);
            QueryWrapper<BhzSupervisionOrderSub> subQueryWrapper = new QueryWrapper<>();
            subQueryWrapper.eq("batch_no",record.getBatchNo());
            subQueryWrapper.orderByAsc("status");
            List<BhzSupervisionOrderSub> list = bhzSupervisionOrderSubService.list(subQueryWrapper);
            vo.setBhzSupervisionOrderSubList(list);
            voList.add(vo);
        }
        IPage<BhzSupervisionOrderVO> result = new Page<>();
        BeanUtil.copyProperties(pageList,result);
        result.setRecords(voList);
        return Result.OK(result);
    }

    /**
     * 添加
     *
     * @param bhzSupervisionOrder
     * @return
     */
    @AutoLog(value = "bhz_supervision_order-添加")
    @ApiOperation(value = "bhz_supervision_order-添加", notes = "bhz_supervision_order-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzSupervisionOrder bhzSupervisionOrder) {
        bhzSupervisionOrderService.save(bhzSupervisionOrder);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bhzSupervisionOrder
     * @return
     */
    @AutoLog(value = "bhz_supervision_order-编辑")
    @ApiOperation(value = "bhz_supervision_order-编辑", notes = "bhz_supervision_order-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzSupervisionOrder bhzSupervisionOrder) {
        bhzSupervisionOrderService.updateById(bhzSupervisionOrder);
        return Result.OK("编辑成功!");
    }



    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_supervision_order-通过id删除")
    @ApiOperation(value = "bhz_supervision_order-通过id删除", notes = "bhz_supervision_order-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzSupervisionOrderService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "bhz_supervision_order-批量删除")
    @ApiOperation(value = "bhz_supervision_order-批量删除", notes = "bhz_supervision_order-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzSupervisionOrderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_supervision_order-通过id查询")
    @ApiOperation(value = "bhz_supervision_order-通过id查询", notes = "bhz_supervision_order-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzSupervisionOrder bhzSupervisionOrder = bhzSupervisionOrderService.getById(id);
        if (bhzSupervisionOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzSupervisionOrder);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzSupervisionOrder
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzSupervisionOrder bhzSupervisionOrder) {
        return super.exportXls(request, bhzSupervisionOrder, BhzSupervisionOrder.class, "bhz_supervision_order");
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
        return super.importExcel(request, response, BhzSupervisionOrder.class);
    }

    /**
     * 积木报表-物联网监理单数据集接口
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/apiData")
    public Result<?> apiDataJ(Integer id) {
        List<Object> data = new ArrayList<>();
        QueryWrapper<BhzSupervisionOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        BhzSupervisionOrder one = bhzSupervisionOrderService.getOne(queryWrapper);
        if (one != null) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(one.getShebeiNo());
            one.setShebeiNo(selectshebeione.getSbname());
            data.add(one);
            return Result.OKs(data);
        } else {
            return Result.error("数据为空!");
        }
    }

    /**
     * 积木报表-物联网监理单数据集接口
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/apiData2")
    public Result<?> apiData2(Integer id) {
        QueryWrapper<BhzSupervisionOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        BhzSupervisionOrder one = bhzSupervisionOrderService.getOne(queryWrapper);
        if (one != null) {
            BhzSupervisionOrderVO vo = new BhzSupervisionOrderVO();
            BeanUtil.copyProperties(one,vo);
            QueryWrapper<BhzSupervisionOrderSub> subQueryWrapper = new QueryWrapper<>();
            subQueryWrapper.eq("batch_no",one.getBatchNo());
            subQueryWrapper.orderByAsc("status");
            List<BhzSupervisionOrderSub> list = bhzSupervisionOrderSubService.list(subQueryWrapper);
            vo.setBhzSupervisionOrderSubList(list);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(one.getShebeiNo());
            vo.setShebeiNo(selectshebeione.getSbname());
            vo.setShebeiNos(selectshebeione.getSbjno());
            return Result.OKs(vo);
        } else {
            return Result.error("数据为空!");
        }
    }
    @GetMapping(value = "/sign")
    public Result<?> sign(Integer status, String batchNo,HttpServletResponse httpServletResponse) throws Exception {
        status = status + 1;
        //校验当前用户是否与设置用户一致
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId();
        String username = loginUser.getUsername();
        boolean match = matchUserName(username, batchNo, status);
        if(!match){
            throw new JeecgBootException("当前用户无权签署");
        }
        QueryWrapper<BhzSupervisionOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_no", batchNo);
        BhzSupervisionOrder one = bhzSupervisionOrderService.getOne(queryWrapper);
        if(StringUtils.isNotBlank(one.getContractid())){
            String pageurl = SignController.pageurl(one.getContractid(), loginUser.getPhone());
            return Result.OK(pageurl);
        }else{
            return Result.error("当前指令单未进行确认，无法签署，请先确认！");
        }


    }
    @GetMapping(value = "/sign2")
    public void sign2(Integer status, String batchNo, HttpServletResponse response) throws Exception {
        status = status + 1;
        //校验当前用户是否与设置用户一致
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId();
        String username = loginUser.getUsername();
        boolean match = matchUserName(username, batchNo, status);
        if(!match){
            throw new JeecgBootException("当前用户无权签署");
        }
        QueryWrapper<BhzSupervisionOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_no", batchNo);
        BhzSupervisionOrder one = bhzSupervisionOrderService.getOne(queryWrapper);
        String pageurl = SignController.pageurl(one.getContractid(), loginUser.getPhone());
        // 设置重定向状态码和目标URL
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", pageurl);

    }
    //校验用户
    public boolean matchUserName(String loginUserName,String batchNo,Integer status){
        QueryWrapper<BhzSupervisionOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_no", batchNo);
        BhzSupervisionOrder one = bhzSupervisionOrderService.getOne(queryWrapper);
        String shebeiNo = one.getShebeiNo();
        QueryWrapper<BhzCallCfg> callCfgQueryWrapper = new QueryWrapper<>();
        callCfgQueryWrapper.eq("bhjno",shebeiNo);
        BhzCallCfg cfg = bhzCallCfgService.getOne(callCfgQueryWrapper);
        String name ="";
        if(status == 1){
            name = cfg.getShperson();
        }else if(status == 2){
            name = cfg.getSupervisorPerson();
        }else if(status ==3){
            name = cfg.getCzperson();
        }
        if(name.equals(loginUserName)){
            return true;
        }
        return false;
    }
}
