package com.trtm.iot.wzyclHandler.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.impl.WztaizhangServiceImpl;
import com.trtm.iot.ycltesttaizhang.service.IYclTestTaizhangService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzyclHandler.entity.WzyclHandler;
import com.trtm.iot.wzyclHandler.service.IWzyclHandlerService;

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

/**
 * @Description: wzycl_handler
 * @Author: jeecg-boot
 * @Date: 2022-11-21
 * @Version: V1.0
 */
@Api(tags = "wzycl_handler")
@RestController
@RequestMapping("/wzyclHandler/wzyclHandler")
@Slf4j
public class WzyclHandlerController extends JeecgController<WzyclHandler, IWzyclHandlerService> {

    @Autowired
    private IWzyclHandlerService wzyclHandlerService;

    @Autowired
    private WztaizhangServiceImpl wztaizhangService;

    @Autowired
    private IYclTestTaizhangService yclTestTaizhangService;

    /**
     * 分页列表查询
     *
     * @param wzyclHandler
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzycl_handler-分页列表查询")
    @ApiOperation(value = "wzycl_handler-分页列表查询", notes = "wzycl_handler-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WzyclHandler wzyclHandler,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WzyclHandler> queryWrapper = QueryGenerator.initQueryWrapper(wzyclHandler, req.getParameterMap());
        Page<WzyclHandler> page = new Page<WzyclHandler>(pageNo, pageSize);
        IPage<WzyclHandler> pageList = wzyclHandlerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "wzycl_handler-处置详情")
    @ApiOperation(value = "wzycl_handler-处置详情", notes = "wzycl_handler-处置详情")
    @GetMapping(value = "/handlerInf")
    public Result<?> selectHandlerInfByPici(WzyclHandler wzyclHandler,
                                            String baseId,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                            HttpServletRequest req) {
        QueryWrapper<WzyclHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("baseId", baseId);
        List<WzyclHandler> list = wzyclHandlerService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "wzycl_handler-处置情况图片")
    @ApiOperation(value = "wzycl_handler-处置情况图片", notes = "wzycl_handler-处置情况图片")
    @GetMapping(value = "/listPic")
    public Result<?> selectPicList(WzyclHandler wzyclHandler,
                                   String baseId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        QueryWrapper<WzyclHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("file_path2");
        queryWrapper.eq("baseId", baseId);
        WzyclHandler one = wzyclHandlerService.getOne(queryWrapper);

        String filePath2 = one.getFilePath2();
        String[] filePathArr = filePath2.split(",");
        List<String> picArr = new ArrayList<>();
        for (String file : filePathArr) {
            picArr.add(file);
        }
        return Result.OK(picArr);
    }

    /**
     * 根据id 原材检验批处置信息添加或修改
     *
     * @param wzyclHandler
     * @param request
     * @return
     */
    @AutoLog(value = "原材检验批处置信息添加或修改")
    @ApiOperation(value = "原材检验批处置信息添加或修改", notes = "原材检验批处置信息添加或修改")
    @GetMapping(value = "/handlerInfoAddOrUpdate")
    public Result<?> handlerInfo(WzyclHandler wzyclHandler,
                                 HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String czperson = String.valueOf(loginUser.getUsername());
        String wtyy = request.getParameter("problemReasons");//问题原因
        String czfs = request.getParameter("handlerWay");//处置方式
        String czjg = request.getParameter("handlerResult");//处置结果
        String czfile = request.getParameter("filePath2");//处置附件
        String baseid = request.getParameter("baseid");//id

        LambdaQueryWrapper<WzyclHandler> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WzyclHandler::getId, baseid);
        List<WzyclHandler> list = wzyclHandlerService.list(lambdaQueryWrapper);

        for (WzyclHandler wzycl : list) {
            WzyclHandler wzyclHandler1 = new WzyclHandler();
            wzyclHandler1.setId(wzycl.getId());
            wzyclHandler1.setOverproofStatus(10);
            wzyclHandlerService.updateById(wzyclHandler1);
        }
        int i = wzyclHandlerService.handlerInfoAddOrUpdate(baseid, wtyy, czfs, czjg, czperson, czfile);
        return Result.OK(i);
    }

    /**
     * 根据id 原材检验批审核信息添加或修改
     *
     * @param wzyclHandler
     * @param request
     * @return
     */
    @AutoLog(value = "原材检验批审批信息添加或修改")
    @ApiOperation(value = "原材检验批审批信息添加或修改", notes = "原材检验批审批信息添加或修改")
    @GetMapping(value = "/superviseInfoAddOrUpdate")
    public Result<?> superviseInfo(WzyclHandler wzyclHandler,
                                   HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String spperson = String.valueOf(loginUser.getUsername());
        String spyj = request.getParameter("spyj");//审批意见
        String spjg = request.getParameter("spjg");//审批结果
        String spfile = request.getParameter("filePath");//审批附件
        String baseid = request.getParameter("baseid");//id

        LambdaQueryWrapper<WzyclHandler> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WzyclHandler::getId, baseid);
        List<WzyclHandler> list = wzyclHandlerService.list(lambdaQueryWrapper);

        for (WzyclHandler wzycl : list) {
            WzyclHandler wzyclHandler1 = new WzyclHandler();
            wzyclHandler1.setId(wzycl.getId());
            wzyclHandler1.setOverproofStatus(20);
            wzyclHandlerService.updateById(wzyclHandler1);
        }
        int i = wzyclHandlerService.superviseInfoAddOrUpdate(baseid, spyj, spjg, spperson, spfile);
        return Result.OK(i);
    }

    /**
     * 根据id 原材检验批审核信息监理驳回
     *
     * @param wzyclHandler
     * @param request
     * @return
     */
    @AutoLog(value = "原材检验批审核信息监理驳回")
    @ApiOperation(value = "原材检验批审核信息监理驳回", notes = "原材检验批审核信息监理驳回")
    @GetMapping(value = "/rejected")
    public Result<?> rejected(WzyclHandler wzyclHandler,
                              HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String jianli = String.valueOf(loginUser.getUsername());

        QueryWrapper<WzyclHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("baseid", wzyclHandler.getBaseid());
        WzyclHandler wzyclHandlers = wzyclHandlerService.getOne(queryWrapper);
        if (wzyclHandlers == null) {
            return Result.error("未找到对应数据");
        } else {
            WzyclHandler wzyclHandler1 = new WzyclHandler();
            wzyclHandler1.setId(wzyclHandlers.getId());
            wzyclHandler1.setOverproofStatus(30);
            wzyclHandler1.setApprovalPerson(jianli);
            wzyclHandler1.setSupervisorHandleTime(new Date());
            wzyclHandler1.setRemark(wzyclHandler.getRemark());
            wzyclHandlerService.updateById(wzyclHandler1);

            LambdaQueryWrapper<Wztaizhang> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Wztaizhang::getId, wzyclHandlers.getBaseid());
            List<Wztaizhang> list = wztaizhangService.list(lambdaQueryWrapper);
            for (Wztaizhang wztaizhang : list) {
                Wztaizhang wztaizhang1 = new Wztaizhang();
                wztaizhang1.setId(wztaizhang.getId());
                wztaizhang1.setOverproofStatus(30);
                wztaizhangService.updateById(wztaizhang1);
            }
            return Result.OK("监理驳回成功!");
        }
    }

    /**
     * 添加
     *
     * @param wzyclHandler
     * @return
     */
    @AutoLog(value = "wzycl_handler-添加")
    @ApiOperation(value = "wzycl_handler-添加", notes = "wzycl_handler-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WzyclHandler wzyclHandler) {
        wzyclHandlerService.save(wzyclHandler);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "wzycl_handler-添加")
    @ApiOperation(value = "wzycl_handler-添加", notes = "wzycl_handler-添加")
    @PostMapping(value = "/addRectifyInfo")
    public Result<?> addRectifyInfo(@RequestBody WzyclHandler wzyclHandler) {
        Wztaizhang byPici = wztaizhangService.getByPici(wzyclHandler.getRemark());
        if (ObjectUtil.isEmpty(byPici)) {
            return Result.error("!未匹配批次号:" + wzyclHandler.getRemark());
        }
        //修改台账处置状态
        Integer overproofStatus = wzyclHandler.getOverproofStatus();
        byPici.setZgzt(String.valueOf(overproofStatus));
        byPici.setOverproofStatus(overproofStatus);
        wztaizhangService.updateById(byPici);
        //修改ycl_test_taizhang表的处置状态
        yclTestTaizhangService.updateZgztByPici(String.valueOf(overproofStatus), wzyclHandler.getRemark());
        //根据处置状态添加处置表信息

        LambdaQueryWrapper<WzyclHandler> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WzyclHandler::getRemark, wzyclHandler.getRemark());
        WzyclHandler wzyclHandlerdata = wzyclHandlerService.getOne(lambdaQueryWrapper);

        if (wzyclHandlerdata != null) {

            switch (overproofStatus) {
                case 10:
                    wzyclHandlerdata.setOverproofStatus(10);
                    wzyclHandlerdata.setHandlePerson(wzyclHandler.getHandlePerson());
                    wzyclHandlerdata.setHandleWay(wzyclHandler.getHandleWay());
                    wzyclHandlerdata.setHandleTime(wzyclHandler.getHandleTime());
                    wzyclHandlerdata.setHandleResult(wzyclHandler.getHandleResult());
                    wzyclHandlerdata.setHandlePerson(wzyclHandler.getHandlePerson());
                    wzyclHandlerdata.setFilePath(wzyclHandler.getFilePath());
                    break;
                case 20://监理审核
                    wzyclHandlerdata.setOverproofStatus(20);
                    wzyclHandlerdata.setSupervisorApproval(wzyclHandler.getSupervisorApproval());
                    wzyclHandlerdata.setSupervisorHandleTime(wzyclHandler.getSupervisorHandleTime());
                    wzyclHandlerdata.setSupervisorResult(wzyclHandler.getSupervisorResult());
                    wzyclHandlerdata.setApprovalPerson(wzyclHandler.getApprovalPerson());
                    break;
                case 30://监理驳回
                    wzyclHandlerdata.setOverproofStatus(30);
                    wzyclHandlerdata.setSupervisorApproval(wzyclHandler.getSupervisorApproval());
                    wzyclHandlerdata.setSupervisorHandleTime(wzyclHandler.getSupervisorHandleTime());
                    wzyclHandlerdata.setSupervisorResult(wzyclHandler.getSupervisorResult());
                    wzyclHandlerdata.setApprovalPerson(wzyclHandler.getApprovalPerson());
                    wzyclHandlerdata.setSupervisorRemark(wzyclHandler.getSupervisorRemark());
                    break;
                case 40://指挥部审核
                    wzyclHandlerdata.setOverproofStatus(40);
                    wzyclHandlerdata.setHeadquartersApproval(wzyclHandler.getHeadquartersApproval());
                    wzyclHandlerdata.setHeadquartersHandleTime(wzyclHandler.getHeadquartersHandleTime());
                    wzyclHandlerdata.setHeadquartersResult(wzyclHandler.getHeadquartersResult());
                    wzyclHandlerdata.setHeadquartersPerson(wzyclHandler.getHeadquartersPerson());
                    break;
                case 50://指挥部驳回
                    wzyclHandlerdata.setOverproofStatus(50);
                    wzyclHandlerdata.setHeadquartersApproval(wzyclHandler.getHeadquartersApproval());
                    wzyclHandlerdata.setHeadquartersHandleTime(wzyclHandler.getHeadquartersHandleTime());
                    wzyclHandlerdata.setHeadquartersResult(wzyclHandler.getHeadquartersResult());
                    wzyclHandlerdata.setHeadquartersPerson(wzyclHandler.getHeadquartersPerson());
                    wzyclHandlerdata.setHeadquartersRemark(wzyclHandler.getHeadquartersRemark());
                    break;
                default:
                    return Result.error("未知处置状态！");
            }
            wzyclHandlerService.updateById(wzyclHandlerdata);

        } else {
            wzyclHandler.setBaseid(String.valueOf(byPici.getId()));
            wzyclHandlerService.save(wzyclHandler);
        }

        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wzyclHandler
     * @return
     */
    @AutoLog(value = "wzycl_handler-编辑")
    @ApiOperation(value = "wzycl_handler-编辑", notes = "wzycl_handler-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WzyclHandler wzyclHandler) {
        wzyclHandlerService.updateById(wzyclHandler);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzycl_handler-通过id删除")
    @ApiOperation(value = "wzycl_handler-通过id删除", notes = "wzycl_handler-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzyclHandlerService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wzycl_handler-批量删除")
    @ApiOperation(value = "wzycl_handler-批量删除", notes = "wzycl_handler-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzyclHandlerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzycl_handler-通过id查询")
    @ApiOperation(value = "wzycl_handler-通过id查询", notes = "wzycl_handler-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WzyclHandler wzyclHandler = wzyclHandlerService.getById(id);
        if (wzyclHandler == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzyclHandler);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzyclHandler
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzyclHandler wzyclHandler) {
        return super.exportXls(request, wzyclHandler, WzyclHandler.class, "wzycl_handler");
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
        return super.importExcel(request, response, WzyclHandler.class);
    }

}
