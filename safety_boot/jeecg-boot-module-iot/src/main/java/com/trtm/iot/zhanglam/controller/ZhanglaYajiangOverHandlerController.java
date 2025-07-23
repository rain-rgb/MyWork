package com.trtm.iot.zhanglam.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;

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
 * @Description: zhangla_yajiang_over_handler
 * @Author: jeecg-boot
 * @Date: 2021-12-27
 * @Version: V1.0
 */
@Api(tags = "zhangla_yajiang_over_handler")
@RestController
@RequestMapping("/zhanglam/zhanglaYajiangOverHandler")
@Slf4j
public class ZhanglaYajiangOverHandlerController extends JeecgController<ZhanglaYajiangOverHandler, IZhanglaYajiangOverHandlerService> {
    @Autowired
    private IZhanglaYajiangOverHandlerService zhanglaYajiangOverHandlerService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private ITrZhanglaXxbService trZhanglaXxbService;
    @Autowired
    private ITrYajiangMService trYajiangMService;
    @Autowired
    private ITrZhanglaMService trZhanglaMService;
    @Autowired
    private ITrYajiangSService trYajiangSService;

    /**
     * 分页列表查询
     *
     * @param zhanglaYajiangOverHandler
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "zhangla_yajiang_over_handler-分页列表查询")
    @ApiOperation(value = "zhangla_yajiang_over_handler-分页列表查询", notes = "zhangla_yajiang_over_handler-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ZhanglaYajiangOverHandler zhanglaYajiangOverHandler,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ZhanglaYajiangOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(zhanglaYajiangOverHandler, req.getParameterMap());
        Page<ZhanglaYajiangOverHandler> page = new Page<ZhanglaYajiangOverHandler>(pageNo, pageSize);
        IPage<ZhanglaYajiangOverHandler> pageList = zhanglaYajiangOverHandlerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 添加修改超标处置信息-张拉
     *
     * @param zhanglaYajiangOverHandler
     * @return
     */
    @AutoLog(value = "添加修改超标处置信息-添加")
    @ApiOperation(value = "添加修改超标处置信息-添加", notes = "添加修改超标处置信息-添加")
    @PostMapping(value = "/addOverHander")
    public Result<?> addOverHander(@RequestBody ZhanglaYajiangOverHandler zhanglaYajiangOverHandler) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String baseid = zhanglaYajiangOverHandler.getBaseid();
        if (baseid != null) {
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = zhanglaYajiangOverHandlerService.selectoneMessage(baseid);
            if (zhanglaYajiangOverHandler1 != null) {
                zhanglaYajiangOverHandler.setId(zhanglaYajiangOverHandler1.getId());
                zhanglaYajiangOverHandler.setHandleTime(new Date());
                zhanglaYajiangOverHandler.setHandlePerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.updateById(zhanglaYajiangOverHandler);
                zhanglaMService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            } else {
                zhanglaYajiangOverHandler.setHandleTime(new Date());
                zhanglaYajiangOverHandler.setHandlePerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.save(zhanglaYajiangOverHandler);
                zhanglaMService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            }
            return Result.OK("处置成功！");
        } else {
            return Result.error("处置失败！");
        }
    }

    /**
     * 添加修改超标审核信息-张拉
     *
     * @param zhanglaYajiangOverHandler
     * @return
     */
    @AutoLog(value = "添加修改超标审核信息-添加")
    @ApiOperation(value = "添加修改超标审核信息-添加", notes = "添加修改超标审核信息-添加")
    @PostMapping(value = "/addOverHanderSH")
    public Result<?> addOverHanderSH(@RequestBody ZhanglaYajiangOverHandler zhanglaYajiangOverHandler) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String baseid = zhanglaYajiangOverHandler.getBaseid();
        if (baseid != null) {
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = zhanglaYajiangOverHandlerService.selectoneMessage(baseid);
            if (zhanglaYajiangOverHandler1 != null) {
                zhanglaYajiangOverHandler.setId(zhanglaYajiangOverHandler1.getId());
                zhanglaYajiangOverHandler.setSupervisorHandleTime(new Date());
                zhanglaYajiangOverHandler.setApprovalPerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.updateById(zhanglaYajiangOverHandler);
                zhanglaMService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            } else {
                zhanglaYajiangOverHandler.setSupervisorHandleTime(new Date());
                zhanglaYajiangOverHandler.setApprovalPerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.save(zhanglaYajiangOverHandler);
                zhanglaMService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            }
            return Result.OK("审核成功！");
        } else {
            return Result.error("审核失败！");
        }
    }

    /**
     * 添加修改超标驳回信息-张拉
     *
     * @param zhanglaYajiangOverHandler
     * @return
     */
    @AutoLog(value = "添加修改超标驳回信息-添加")
    @ApiOperation(value = "添加修改超标驳回信息-添加", notes = "添加修改超标驳回信息-添加")
    @PostMapping(value = "/addOverHanderBH")
    public Result<?> addOverHanderBH(@RequestBody ZhanglaYajiangOverHandler zhanglaYajiangOverHandler) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String baseid = zhanglaYajiangOverHandler.getBaseid();
        if (baseid != null) {
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = zhanglaYajiangOverHandlerService.selectoneMessage(baseid);
            if (zhanglaYajiangOverHandler1 != null) {
                zhanglaYajiangOverHandler.setId(zhanglaYajiangOverHandler1.getId());
                zhanglaYajiangOverHandler.setSupervisorHandleTime(new Date());
                zhanglaYajiangOverHandler.setApprovalPerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.updateById(zhanglaYajiangOverHandler);
                zhanglaMService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            } else {
                zhanglaYajiangOverHandler.setSupervisorHandleTime(new Date());
                zhanglaYajiangOverHandler.setApprovalPerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.save(zhanglaYajiangOverHandler);
                zhanglaMService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            }
            return Result.OK("驳回成功！");
        } else {
            return Result.error("驳回失败！");
        }
    }


    /**
     * 添加修改超标处置信息-压浆
     *
     * @param zhanglaYajiangOverHandler
     * @return
     */
    @AutoLog(value = "添加修改超标处置信息-添加")
    @ApiOperation(value = "添加修改超标处置信息-添加", notes = "添加修改超标处置信息-添加")
    @PostMapping(value = "/addOverHanderYaJ")
    public Result<?> addOverHanderYaJ(@RequestBody ZhanglaYajiangOverHandler zhanglaYajiangOverHandler) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String baseid = zhanglaYajiangOverHandler.getBaseid();
        if (baseid != null) {
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = zhanglaYajiangOverHandlerService.selectoneMessage(baseid);
            if (zhanglaYajiangOverHandler1 != null) {
                zhanglaYajiangOverHandler.setId(zhanglaYajiangOverHandler1.getId());
                zhanglaYajiangOverHandler.setHandleTime(new Date());
                zhanglaYajiangOverHandler.setHandlePerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.updateById(zhanglaYajiangOverHandler);
                yajiangSService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            } else {
                zhanglaYajiangOverHandler.setHandleTime(new Date());
                zhanglaYajiangOverHandler.setHandlePerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.save(zhanglaYajiangOverHandler);
                yajiangSService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            }
            return Result.OK("处置成功！");
        } else {
            return Result.error("处置失败！");
        }
    }

    /**
     * 添加修改超标审核信息-压浆
     *
     * @param zhanglaYajiangOverHandler
     * @return
     */
    @AutoLog(value = "添加修改超标审核信息-添加")
    @ApiOperation(value = "添加修改超标审核信息-添加", notes = "添加修改超标审核信息-添加")
    @PostMapping(value = "/addOverHanderYJSH")
    public Result<?> addOverHanderYJSH(@RequestBody ZhanglaYajiangOverHandler zhanglaYajiangOverHandler) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String baseid = zhanglaYajiangOverHandler.getBaseid();
        if (baseid != null) {
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = zhanglaYajiangOverHandlerService.selectoneMessage(baseid);
            if (zhanglaYajiangOverHandler1 != null) {
                zhanglaYajiangOverHandler.setId(zhanglaYajiangOverHandler1.getId());
                zhanglaYajiangOverHandler.setSupervisorHandleTime(new Date());
                zhanglaYajiangOverHandler.setApprovalPerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.updateById(zhanglaYajiangOverHandler);
                yajiangSService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            } else {
                zhanglaYajiangOverHandler.setSupervisorHandleTime(new Date());
                zhanglaYajiangOverHandler.setApprovalPerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.save(zhanglaYajiangOverHandler);
                yajiangSService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            }
            return Result.OK("审核成功！");
        } else {
            return Result.error("审核失败！");
        }
    }

    /**
     * 添加修改超标驳回信息-压浆
     *
     * @param zhanglaYajiangOverHandler
     * @return
     */
    @AutoLog(value = "添加修改超标驳回信息-添加")
    @ApiOperation(value = "添加修改超标驳回信息-添加", notes = "添加修改超标驳回信息-添加")
    @PostMapping(value = "/addOverHanderYJBH")
    public Result<?> addOverHanderYJBH(@RequestBody ZhanglaYajiangOverHandler zhanglaYajiangOverHandler) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String baseid = zhanglaYajiangOverHandler.getBaseid();
        if (baseid != null) {
            ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = zhanglaYajiangOverHandlerService.selectoneMessage(baseid);
            if (zhanglaYajiangOverHandler1 != null) {
                zhanglaYajiangOverHandler.setId(zhanglaYajiangOverHandler1.getId());
                zhanglaYajiangOverHandler.setSupervisorHandleTime(new Date());
                zhanglaYajiangOverHandler.setApprovalPerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.updateById(zhanglaYajiangOverHandler);
                yajiangSService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            } else {
                zhanglaYajiangOverHandler.setSupervisorHandleTime(new Date());
                zhanglaYajiangOverHandler.setApprovalPerson(loginUser.getRealname());
                zhanglaYajiangOverHandlerService.save(zhanglaYajiangOverHandler);
                yajiangSService.updateoverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus(), baseid);
            }
            return Result.OK("驳回成功！");
        } else {
            return Result.error("驳回失败！");
        }
    }

    /**
     * 添加
     *
     * @param zhanglaYajiangOverHandler
     * @return
     */
    @AutoLog(value = "zhangla_yajiang_over_handler-添加")
    @ApiOperation(value = "zhangla_yajiang_over_handler-添加", notes = "zhangla_yajiang_over_handler-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ZhanglaYajiangOverHandler zhanglaYajiangOverHandler) {
        zhanglaYajiangOverHandlerService.save(zhanglaYajiangOverHandler);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param zhanglaYajiangOverHandler
     * @return
     */
    @AutoLog(value = "zhangla_yajiang_over_handler-编辑")
    @ApiOperation(value = "zhangla_yajiang_over_handler-编辑", notes = "zhangla_yajiang_over_handler-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ZhanglaYajiangOverHandler zhanglaYajiangOverHandler) {
        zhanglaYajiangOverHandlerService.updateById(zhanglaYajiangOverHandler);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "zhangla_yajiang_over_handler-通过id删除")
    @ApiOperation(value = "zhangla_yajiang_over_handler-通过id删除", notes = "zhangla_yajiang_over_handler-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        zhanglaYajiangOverHandlerService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "zhangla_yajiang_over_handler-批量删除")
    @ApiOperation(value = "zhangla_yajiang_over_handler-批量删除", notes = "zhangla_yajiang_over_handler-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.zhanglaYajiangOverHandlerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "zhangla_yajiang_over_handler-通过id查询")
    @ApiOperation(value = "zhangla_yajiang_over_handler-通过id查询", notes = "zhangla_yajiang_over_handler-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.getById(id);
        if (zhanglaYajiangOverHandler == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(zhanglaYajiangOverHandler);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param zhanglaYajiangOverHandler
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZhanglaYajiangOverHandler zhanglaYajiangOverHandler) {
        return super.exportXls(request, zhanglaYajiangOverHandler, ZhanglaYajiangOverHandler.class, "zhangla_yajiang_over_handler");
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
        return super.importExcel(request, response, ZhanglaYajiangOverHandler.class);
    }

    /**
     * 根据id添加或者修改处置信息
     */
    @AutoLog(value = "拌合站处置信息添加或者修改")
    @ApiOperation(value = "拌合站处置信息添加或者修改", notes = "拌合站处置信息添加或者修改")
    @GetMapping(value = "/HBZCZAddOrUpdate")
    public Result<?> HBZCZAddOrUpdate(ZhanglaYajiangOverHandler zhanglaYajiangOverHandler,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String people = String.valueOf(loginUser.getRealname());
        String zhbyj = request.getParameter("zhbyj");   //指挥部审批
        String zhbbh = request.getParameter("zhbbh");   //指挥部驳回
        String zhbjg = request.getParameter("zhbjg");   //指挥部结果
        String spyj = request.getParameter("spyj");   //监理审批
        String jlbh = request.getParameter("jlbh");   //监理驳回
        String spjg = request.getParameter("spjg");   //审批结果
        String wtyy = request.getParameter("wtyy");  //问题原因
        String clfs = request.getParameter("clfs");  //处理方式
        String cljg = request.getParameter("cljg");  //处理结果
        String bizPath = request.getParameter("fileList");  //图片
        String baseid = request.getParameter("baseid");   //id
        String holeid = request.getParameter("holeid");  //孔道id
        int type = Integer.parseInt(request.getParameter("type"));//0：张拉；1：压浆
        int level = Integer.parseInt(request.getParameter("level"));//等级
        int status = Integer.parseInt(request.getParameter("status"));//状态值
        Integer i = null;
        if (type == 0) {//张拉
            LambdaQueryWrapper<TrZhanglaXxb> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(TrZhanglaXxb::getSyjid, baseid);
            List<TrZhanglaXxb> trZhanglaXxbs = trZhanglaXxbService.list(lambdaQueryWrapper);
            for (TrZhanglaXxb trZhanglaXxb : trZhanglaXxbs) {
                TrZhanglaXxb trZhanglaXxb1 = new TrZhanglaXxb();
                trZhanglaXxb1.setId(trZhanglaXxb.getId());
                if (level == 1) {
                    if (status == 10) {
                        trZhanglaXxb1.setOverproofStatus(20);
                        i = zhanglaYajiangOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, baseid, bizPath, people, 20,holeid);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 2) {
                    if (status == 10) {//处置
                        trZhanglaXxb1.setOverproofStatus(10);
                        i = zhanglaYajiangOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, baseid, bizPath, people, 10, holeid);
                    } else if (status == 30) {//监理驳回
                        trZhanglaXxb1.setOverproofStatus(30);
                        i = zhanglaYajiangOverHandlerService.supervisorBohui(jlbh, baseid, people, 30,holeid);
                    } else if (status == 40) {//监理审核
                        trZhanglaXxb1.setOverproofStatus(20);
                        i = zhanglaYajiangOverHandlerService.supervisorAddOrUpdate(spyj, spjg, baseid, bizPath, people, 20,holeid);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 3) {
                    if (status == 10) {//处置
                        trZhanglaXxb1.setOverproofStatus(10);
                        i = zhanglaYajiangOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, baseid, bizPath, people, 10, holeid);
                    } else if (status == 30) {//监理驳回
                        trZhanglaXxb1.setOverproofStatus(30);
                        i = zhanglaYajiangOverHandlerService.supervisorBohui(jlbh, baseid, people, 30, holeid);
                    } else if (status == 40) {//监理审核
                        trZhanglaXxb1.setOverproofStatus(40);
                        i = zhanglaYajiangOverHandlerService.supervisorAddOrUpdate(spyj, spjg, baseid, bizPath, people, 40, holeid);
                    } else if (status == 50) {//指挥部审核
                        trZhanglaXxb1.setOverproofStatus(20);
                        i = zhanglaYajiangOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, baseid, bizPath, people, 20,holeid);
                    } else if (status == 60) {//指挥部驳回
                        trZhanglaXxb1.setOverproofStatus(60);
                        i = zhanglaYajiangOverHandlerService.headquartersBohui(zhbbh, baseid, people, 60,holeid);
                    } else {
                        return Result.error("错误");
                    }
                }
                trZhanglaXxbService.updateById(trZhanglaXxb1);
                Integer status1 = trZhanglaXxb1.getOverproofStatus();
                LambdaQueryWrapper<TrZhanglaM> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TrZhanglaM::getSyjid,baseid);
                List<TrZhanglaM> list = trZhanglaMService.list(queryWrapper);
                if(list.size()>0){
                    for (TrZhanglaM trZhanglaM : list) {
                        trZhanglaM.setOverproofStatus(10);
                    }
                    trZhanglaMService.updateBatchById(list);
                }

            }
        } else if (type == 1) {//压浆
            if (level == 1) {
                LambdaQueryWrapper<TrYajiangM> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(TrYajiangM::getSyjid, baseid);
                List<TrYajiangM> trYajiangMS = trYajiangMService.list(lambdaQueryWrapper);
                TrYajiangM trYajiangM1 = new TrYajiangM();
                for (TrYajiangM trYajiangM : trYajiangMS) {
                    trYajiangM1.setId(trYajiangM.getId());
                    if (status == 10) {//处置
                        trYajiangM1.setOverproofStatus(10);
                        i = zhanglaYajiangOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, baseid, bizPath, people, 10, holeid);
                    } else if (status == 30) {//监理驳回
                        trYajiangM1.setOverproofStatus(30);
                        i = zhanglaYajiangOverHandlerService.supervisorBohui(jlbh, baseid, people, 30, holeid);
                    } else if (status == 40) {//监理审核
                        trYajiangM1.setOverproofStatus(40);
                        i = zhanglaYajiangOverHandlerService.supervisorAddOrUpdate(spyj, spjg, baseid, bizPath, people, 40, holeid);
                    } else if (status == 50) {//指挥部审核
                        trYajiangM1.setOverproofStatus(20);
                        i = zhanglaYajiangOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, baseid, bizPath, people, 20, holeid);
                    } else if (status == 60) {//指挥部驳回
                        trYajiangM1.setOverproofStatus(60);
                        i = zhanglaYajiangOverHandlerService.headquartersBohui(zhbbh, baseid, people, 60, holeid);
                    } else {
                        return Result.error("错误");
                    }
                    trYajiangMService.updateById(trYajiangM1);
                }
                Integer overproofStatus = trYajiangM1.getOverproofStatus();
                LambdaQueryWrapper<TrYajiangS> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TrYajiangS::getSyjid,baseid);
                List<TrYajiangS> list = trYajiangSService.list(queryWrapper);
                if(list.size()>0){
                    for (TrYajiangS trYajiangS : list) {
                        trYajiangS.setOverproofStatus(overproofStatus);
                    }
                    trYajiangSService.updateBatchById(list);
                }
            }
        }

        return Result.OK(i);
    }

    /**
     * 根据syjid查询孔道id
     */
    @AutoLog(value = "拌合站处置信息添加或者修改")
    @ApiOperation(value = "拌合站处置信息添加或者修改", notes = "拌合站处置信息添加或者修改")
    @GetMapping(value = "/holeidList")
    public Result<?> holeidList(String syjid,Integer type,Integer status) {
        if(type==0){
            //张拉
            QueryWrapper<TrZhanglaM> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid",syjid);
            queryWrapper.eq("hege","0").or().eq("hege","不合格").eq("syjid",syjid);
            if(status!=null){
                queryWrapper.eq("overproof_status",status);

            }
            queryWrapper.last("GROUP BY gsbh ");
            List<TrZhanglaM> list = trZhanglaMService.list(queryWrapper);
            return Result.OK(list);
        }else if(type ==1) {
            //压浆
            QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid",syjid);
            queryWrapper.eq("hege","0").or().eq("syjid",syjid).eq("hege","不合格");
            queryWrapper.last("GROUP BY kongdao");
            List<TrYajiangS> list = trYajiangSService.list(queryWrapper);
            for (TrYajiangS l :list){
                if (l.getIsOverLevel() == null){
                    l.setIsOverLevel(1);
                }
            }
            return Result.OK(list);
        }
        return Result.OK();

    }


    /**
     * 根据syjid查询孔道id
     */
    @AutoLog(value = "拌合站处置信息添加或者修改")
    @ApiOperation(value = "拌合站处置信息添加或者修改", notes = "拌合站处置信息添加或者修改")
    @GetMapping(value = "/holeidList47")
    public Result<?> holeidList47(String syjid,Integer type,Integer status) {
        if(type==0){
            //张拉
            QueryWrapper<TrZhanglaM> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid",syjid);
            queryWrapper.gt("over_level","0").eq("syjid",syjid);
//            if(status!=null){
//                queryWrapper.eq("overproof_status",status);
//
//            }
            queryWrapper.last("GROUP BY gsbh ");
            List<TrZhanglaM> list = trZhanglaMService.list(queryWrapper);
            return Result.OK(list);
        }else if(type ==1) {
            //压浆
            QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid",syjid);
            queryWrapper.eq("is_over_level","1").eq("syjid",syjid);
            queryWrapper.last("GROUP BY kongdao");
            List<TrYajiangS> list = trYajiangSService.list(queryWrapper);
            for (TrYajiangS l :list){
                if (l.getIsOverLevel() == null){
                    l.setIsOverLevel(1);
                }
            }
            return Result.OK(list);
        }
        return Result.OK();

    }
}
