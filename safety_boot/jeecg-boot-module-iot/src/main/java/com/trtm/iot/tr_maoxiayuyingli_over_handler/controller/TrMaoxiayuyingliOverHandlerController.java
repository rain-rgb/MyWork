package com.trtm.iot.tr_maoxiayuyingli_over_handler.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliM;
import com.trtm.iot.trmaoxiayuyinglim.service.ITrMaoxiayuyingliMService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.tr_maoxiayuyingli_over_handler.entity.TrMaoxiayuyingliOverHandler;
import com.trtm.iot.tr_maoxiayuyingli_over_handler.service.ITrMaoxiayuyingliOverHandlerService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 锚下预应力张拉处置
 * @Author: jeecg-boot
 * @Date: 2024-06-06
 * @Version: V1.0
 */
@Api(tags = "锚下预应力张拉处置")
@RestController
@RequestMapping("/tr_maoxiayuyingli_over_handler/trMaoxiayuyingliOverHandler")
@Slf4j
public class TrMaoxiayuyingliOverHandlerController extends JeecgController<TrMaoxiayuyingliOverHandler, ITrMaoxiayuyingliOverHandlerService> {
    @Autowired
    private ITrMaoxiayuyingliOverHandlerService trMaoxiayuyingliOverHandlerService;
    @Autowired
    private ITrMaoxiayuyingliMService trMaoxiayuyingliMService;

    /**
     * 分页列表查询
     *
     * @param trMaoxiayuyingliOverHandler
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "锚下预应力张拉处置-分页列表查询")
    @ApiOperation(value = "锚下预应力张拉处置-分页列表查询", notes = "锚下预应力张拉处置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrMaoxiayuyingliOverHandler trMaoxiayuyingliOverHandler,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrMaoxiayuyingliOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(trMaoxiayuyingliOverHandler, req.getParameterMap());
        Page<TrMaoxiayuyingliOverHandler> page = new Page<TrMaoxiayuyingliOverHandler>(pageNo, pageSize);
        IPage<TrMaoxiayuyingliOverHandler> pageList = trMaoxiayuyingliOverHandlerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trMaoxiayuyingliOverHandler
     * @return
     */
    @AutoLog(value = "锚下预应力张拉处置-添加")
    @ApiOperation(value = "锚下预应力张拉处置-添加", notes = "锚下预应力张拉处置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrMaoxiayuyingliOverHandler trMaoxiayuyingliOverHandler) {
        trMaoxiayuyingliOverHandlerService.save(trMaoxiayuyingliOverHandler);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trMaoxiayuyingliOverHandler
     * @return
     */
    @AutoLog(value = "锚下预应力张拉处置-编辑")
    @ApiOperation(value = "锚下预应力张拉处置-编辑", notes = "锚下预应力张拉处置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrMaoxiayuyingliOverHandler trMaoxiayuyingliOverHandler) {
        trMaoxiayuyingliOverHandlerService.updateById(trMaoxiayuyingliOverHandler);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "锚下预应力张拉处置-通过id删除")
    @ApiOperation(value = "锚下预应力张拉处置-通过id删除", notes = "锚下预应力张拉处置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trMaoxiayuyingliOverHandlerService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "锚下预应力张拉处置-批量删除")
    @ApiOperation(value = "锚下预应力张拉处置-批量删除", notes = "锚下预应力张拉处置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trMaoxiayuyingliOverHandlerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "锚下预应力张拉处置-通过id查询")
    @ApiOperation(value = "锚下预应力张拉处置-通过id查询", notes = "锚下预应力张拉处置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrMaoxiayuyingliOverHandler trMaoxiayuyingliOverHandler = trMaoxiayuyingliOverHandlerService.getById(id);
        if (trMaoxiayuyingliOverHandler == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trMaoxiayuyingliOverHandler);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trMaoxiayuyingliOverHandler
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrMaoxiayuyingliOverHandler trMaoxiayuyingliOverHandler) {
        return super.exportXls(request, trMaoxiayuyingliOverHandler, TrMaoxiayuyingliOverHandler.class, "锚下预应力张拉处置");
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
        return super.importExcel(request, response, TrMaoxiayuyingliOverHandler.class);
    }

    /**
     * 添加
     *
     * @param trMaoxiayuyingliOverHandler
     * @return
     */
    @AutoLog(value = "软基超标处理-处置")
    @ApiOperation(value = "软基超标处理-处置", notes = "软基超标处理-添加")
    @PostMapping(value = "/handle")
    public Result<?> handle(@RequestBody TrMaoxiayuyingliOverHandler trMaoxiayuyingliOverHandler) {
        Integer id = trMaoxiayuyingliOverHandler.getId();
        String baseid = trMaoxiayuyingliOverHandler.getBaseid();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        trMaoxiayuyingliOverHandler.setHandleTime(new Date());
        trMaoxiayuyingliOverHandler.setHandlePerson(String.valueOf(loginUser.getRealname()));
        trMaoxiayuyingliOverHandler.setBaseid(baseid);
        QueryWrapper<TrMaoxiayuyingliOverHandler> queryWrapper = new QueryWrapper();
        queryWrapper.eq("baseId", baseid);
        List<TrMaoxiayuyingliOverHandler> one = trMaoxiayuyingliOverHandlerService.list(queryWrapper);
        if (one.size() == 0) {
            trMaoxiayuyingliOverHandlerService.save(trMaoxiayuyingliOverHandler);
        } else {
            trMaoxiayuyingliOverHandler.setId(one.get(0).getId());
            trMaoxiayuyingliOverHandlerService.updateById(trMaoxiayuyingliOverHandler);
        }
        // 主表状态更新
        TrMaoxiayuyingliM trMaoxiayuyingliM = new TrMaoxiayuyingliM();
        trMaoxiayuyingliM.setId(id);
        trMaoxiayuyingliM.setOverproofStatus(trMaoxiayuyingliOverHandler.getOverproofStatus());
        trMaoxiayuyingliMService.updateById(trMaoxiayuyingliM);

        return Result.OK("处置成功！");
    }


    /**
     * 编辑
     *
     * @param trMaoxiayuyingliOverHandler
     * @return
     */
    @AutoLog(value = "软基超标处理")
    @ApiOperation(value = "软基超标处理-编辑", notes = "软基超标处理-编辑")
    @PutMapping(value = "/editNext")
    public Result<?> editNext(@RequestBody TrMaoxiayuyingliOverHandler trMaoxiayuyingliOverHandler) {
        List<Integer> baseIdList = new ArrayList<>();
        String[] split = trMaoxiayuyingliOverHandler.getFilePath3().split(",");
        for (int i = 0; i < split.length; i++) {
            baseIdList.add(Integer.parseInt(split[i]));
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        trMaoxiayuyingliOverHandler.setSupervisorHandleTime(new Date());
        trMaoxiayuyingliOverHandler.setApprovalPerson(String.valueOf(loginUser.getUsername()));
        for (Integer baseId : baseIdList) {
            QueryWrapper<TrMaoxiayuyingliOverHandler> queryWrapper = new QueryWrapper();
            queryWrapper.eq("baseId", baseId);
            TrMaoxiayuyingliOverHandler one = trMaoxiayuyingliOverHandlerService.getOne(queryWrapper);
            trMaoxiayuyingliOverHandler.setId(one.getId());
            TrMaoxiayuyingliM trMaoxiayuyingliM = new TrMaoxiayuyingliM();
            trMaoxiayuyingliM.setId(baseId);
            if (trMaoxiayuyingliOverHandler.getOverproofStatus() == 10) {
                trMaoxiayuyingliM.setOverproofStatus(10);
            } else if (trMaoxiayuyingliOverHandler.getOverproofStatus() == 20) {
                trMaoxiayuyingliM.setOverproofStatus(20);
            } else if (trMaoxiayuyingliOverHandler.getOverproofStatus() == 30) {
                trMaoxiayuyingliM.setOverproofStatus(30);
            } else {
                trMaoxiayuyingliM.setOverproofStatus(10);
            }
            trMaoxiayuyingliOverHandlerService.updateById(trMaoxiayuyingliOverHandler);
            trMaoxiayuyingliMService.updateById(trMaoxiayuyingliM);
        }
        return Result.OK("编辑成功!");
    }
}
