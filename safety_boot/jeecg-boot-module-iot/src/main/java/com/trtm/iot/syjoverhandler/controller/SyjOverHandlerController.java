package com.trtm.iot.syjoverhandler.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.trtm.iot.hc_pavement_alarm.service.IHcPavementAlarmService;
import com.trtm.iot.mxesy.entity.WendingD;
import com.trtm.iot.mxesy.service.IWendingDService;
import com.trtm.iot.rhdcx.entity.WRuanhuadianM;
import com.trtm.iot.rhdcx.service.IWRuanhuadianMService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import com.trtm.iot.syjoverhandler.service.ISyjOverHandlerService;
import com.trtm.iot.whwgpy.entity.WHwgpy;
import com.trtm.iot.whwgpy.service.IWHwgpyService;
import com.trtm.iot.ydcx.entity.WYanduM;
import com.trtm.iot.ydcx.service.IWYanduMService;
import com.trtm.iot.zhenru.entity.WZhenruduM;
import com.trtm.iot.zhenru.service.IWZhenruduMService;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 试验机处置审核表
 * @Author: jeecg-boot
 * @Date: 2021-12-30
 * @Version: V1.0
 */
@Api(tags = "试验机处置审核表")
@RestController
@RequestMapping("/syjoverhandler/syjOverHandler")
@Slf4j
public class SyjOverHandlerController extends JeecgController<SyjOverHandler, ISyjOverHandlerService> {
    @Autowired
    private ISyjOverHandlerService syjOverHandlerService;
    @Autowired
    private ITSyjzbService tSyjzbService;
    @Autowired
    private IWRuanhuadianMService iwRuanhuadianMService;
    @Autowired
    private IWZhenruduMService iwZhenruduMService;
    @Autowired
    private IWendingDService wendingDService;
    @Autowired
    private IWYanduMService iwYanduMService;
    @Autowired
    private IHcPavementAlarmService hcPavementAlarmService;
    @Autowired
    private IWHwgpyService hwgpyService;
    @Autowired
    private ITSyjzbService syjzbService;

    /**
     * 分页列表查询
     *
     * @param syjOverHandler
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "试验机处置审核表-分页列表查询")
    @ApiOperation(value = "试验机处置审核表-分页列表查询", notes = "试验机处置审核表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyjOverHandler syjOverHandler,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyjOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(syjOverHandler, req.getParameterMap());
        Page<SyjOverHandler> page = new Page<SyjOverHandler>(pageNo, pageSize);
        IPage<SyjOverHandler> pageList = syjOverHandlerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syjOverHandler
     * @return
     */
    @AutoLog(value = "试验机处置审核表-添加")
    @ApiOperation(value = "试验机处置审核表-添加", notes = "试验机处置审核表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyjOverHandler syjOverHandler) {
        syjOverHandlerService.save(syjOverHandler);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param syjOverHandler
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "试验机处置审核表-添加")
    @ApiOperation(value = "试验机处置审核表-添加", notes = "试验机处置审核表-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody SyjOverHandler syjOverHandler) {
        // 委托编号
        String ypbh = syjOverHandler.getYpbh();
        String sjbh = syjOverHandler.getSjbh();
        String lq = syjOverHandler.getLq();
        Integer overproofStatus = syjOverHandler.getOverproofStatus();
        // 验证输入是否为空或不合法
        if (StringUtils.isBlank(ypbh)) {
            return Result.error("委托编号不能为空");
        }

        QueryWrapper<TSyjzb> syjzbQueryWrapper = new QueryWrapper<>();

        syjzbQueryWrapper.eq("WTBH", ypbh);

        if (StringUtil.isNotEmpty(sjbh)) {
            syjzbQueryWrapper.eq("SJBH", sjbh);
        }
        if (StringUtil.isNotEmpty(lq)) {
            syjzbQueryWrapper.eq("LQ", lq);
        }
        List<TSyjzb> syjzbList = syjzbService.list(syjzbQueryWrapper);

        // 批量更新
        for (TSyjzb tSyjzb : syjzbList) {
            tSyjzb.setOverproofStatus(overproofStatus);
            tSyjzbService.updateById(tSyjzb);

            LambdaQueryWrapper<SyjOverHandler> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SyjOverHandler::getBaseid, tSyjzb.getSyjid());
            SyjOverHandler syjOverHandler1 = syjOverHandlerService.getOne(queryWrapper);

            if (syjOverHandler1 != null) {
                switch (overproofStatus) {
                    case 10://处置
                        syjOverHandler1.setOverproofStatus(10);
                        syjOverHandler1.setHandlePerson(syjOverHandler.getHandlePerson());
                        syjOverHandler1.setHandleWay(syjOverHandler.getHandleWay());
                        syjOverHandler1.setHandleTime(syjOverHandler.getHandleTime());
                        syjOverHandler1.setHandleResult(syjOverHandler.getHandleResult());
                        syjOverHandler1.setHandlePerson(syjOverHandler.getHandlePerson());
                        syjOverHandler1.setFilePath(syjOverHandler.getFilePath());
                        break;

                    case 20://监理审核
                        syjOverHandler1.setOverproofStatus(20);
                        syjOverHandler1.setSupervisorApproval(syjOverHandler.getSupervisorApproval());
                        syjOverHandler1.setSupervisorHandleTime(syjOverHandler.getSupervisorHandleTime());
                        syjOverHandler1.setSupervisorResult(syjOverHandler.getSupervisorResult());
                        syjOverHandler1.setApprovalPerson(syjOverHandler.getApprovalPerson());
                        break;

                    case 30://监理驳回
                        syjOverHandler1.setOverproofStatus(30);
                        syjOverHandler1.setSupervisorApproval(syjOverHandler.getSupervisorApproval());
                        syjOverHandler1.setSupervisorHandleTime(syjOverHandler.getSupervisorHandleTime());
                        syjOverHandler1.setSupervisorResult(syjOverHandler.getSupervisorResult());
                        syjOverHandler1.setApprovalPerson(syjOverHandler.getApprovalPerson());
                        syjOverHandler1.setRemark(syjOverHandler.getRemark());
                        break;

                    case 40://指挥部审核
                        syjOverHandler1.setOverproofStatus(40);
                        syjOverHandler1.setHeadquartersApproval(syjOverHandler.getHeadquartersApproval());
                        syjOverHandler1.setHeadquartersHandleTime(syjOverHandler.getHeadquartersHandleTime());
                        syjOverHandler1.setHeadquartersResult(syjOverHandler.getHeadquartersResult());
                        syjOverHandler1.setHeadquartersPerson(syjOverHandler.getHeadquartersPerson());
                        syjOverHandler1.setHeadquartersRemark(syjOverHandler.getHeadquartersRemark());
                        break;

                    case 50://指挥部驳回
                        syjOverHandler1.setOverproofStatus(50);
                        syjOverHandler1.setHeadquartersApproval(syjOverHandler.getHeadquartersApproval());
                        syjOverHandler1.setHeadquartersHandleTime(syjOverHandler.getHeadquartersHandleTime());
                        syjOverHandler1.setHeadquartersResult(syjOverHandler.getHeadquartersResult());
                        syjOverHandler1.setHeadquartersPerson(syjOverHandler.getHeadquartersPerson());
                        syjOverHandler1.setHeadquartersRemark(syjOverHandler.getHeadquartersRemark());
                        break;

                    default:
                        return Result.error("未知处置状态！");
                }
                syjOverHandlerService.updateById(syjOverHandler1);
            } else {
                syjOverHandler.setBaseid(tSyjzb.getSyjid());
                syjOverHandlerService.save(syjOverHandler);
            }

        }

        return Result.OK("添加成功！");
    }

    public static void replaceEmptyStringsWithNull(Object obj) {
        if (obj == null) {
            return;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 允许对私有属性进行访问
            try {
                Object value = field.get(obj);
                // 如果是字符串且为空，则替换为 null
                if (value instanceof String && ((String) value).isEmpty()) {
                    field.set(obj, null);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // 处理异常
            }
        }
    }

    /**
     * 编辑
     *
     * @param syjOverHandler
     * @return
     */
    @AutoLog(value = "试验机处置审核表-编辑")
    @ApiOperation(value = "试验机处置审核表-编辑", notes = "试验机处置审核表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyjOverHandler syjOverHandler) {
        syjOverHandlerService.updateById(syjOverHandler);
        return Result.OK("编辑成功!");
    }

    /**
     * 试验机处置审核驳回
     *
     * @param syjOverHandler
     * @return
     */
    @AutoLog(value = "试验机处置审核表-处置审核驳回")
    @ApiOperation(value = "试验机处置审核表-处置审核驳回", notes = "试验机处置审核表-处置审核驳回")
    @GetMapping(value = "/deallist")
    @Transactional
    public Result<?> dealList(SyjOverHandler syjOverHandler, Integer flag, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        SyjOverHandler selectone = syjOverHandlerService.selectone(syjOverHandler.getBaseid());
        SyjOverHandler syjOverHandler1 = new SyjOverHandler();
        BeanUtils.copyProperties(syjOverHandler, syjOverHandler1);
        if (selectone == null) {
            syjOverHandler1.setHandlePerson(loginUser.getRealname());
            syjOverHandler1.setHandleTime(new Date());
            syjOverHandler1.setOverproofStatus(10);
            syjOverHandlerService.save(syjOverHandler1);
        } else {
            syjOverHandler1.setId(selectone.getId());
            if (flag == 2) {
                syjOverHandler1.setHandlePerson(loginUser.getRealname());
                syjOverHandler1.setHandleTime(new Date());
                syjOverHandler1.setOverproofStatus(10);
            } else if (flag == 3) {
                syjOverHandler1.setOverproofStatus(30);
            } else {
                syjOverHandler1.setApprovalPerson(loginUser.getRealname());
                syjOverHandler1.setSupervisorHandleTime(new Date());
                syjOverHandler1.setOverproofStatus(20);
            }
            syjOverHandlerService.updateById(syjOverHandler1);
        }
        QueryWrapper<TSyjzb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("SYJID", syjOverHandler.getBaseid());
        TSyjzb tSyjzb = tSyjzbService.getOne(queryWrapper);
        if (tSyjzb != null) {
            TSyjzb tSyjzb1 = new TSyjzb();
            tSyjzb1.setId(tSyjzb.getId());
            if (flag == 2) {
                tSyjzb1.setOverproofStatus(10);
            } else if (flag == 3) {
                tSyjzb1.setOverproofStatus(30);
            } else {
                tSyjzb1.setOverproofStatus(20);
            }
            tSyjzbService.updateById(tSyjzb1);
            return Result.OK("处理成功!");
        } else {
            return Result.error("未找到对应数据");
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "试验机处置审核表-通过id删除")
    @ApiOperation(value = "试验机处置审核表-通过id删除", notes = "试验机处置审核表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syjOverHandlerService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "试验机处置审核表-批量删除")
    @ApiOperation(value = "试验机处置审核表-批量删除", notes = "试验机处置审核表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syjOverHandlerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "试验机处置审核表-通过id查询")
    @ApiOperation(value = "试验机处置审核表-通过id查询", notes = "试验机处置审核表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyjOverHandler syjOverHandler = syjOverHandlerService.getById(id);
        if (syjOverHandler == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syjOverHandler);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syjOverHandler
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyjOverHandler syjOverHandler) {
        return super.exportXls(request, syjOverHandler, SyjOverHandler.class, "试验机处置审核表");
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
        return super.importExcel(request, response, SyjOverHandler.class);
    }

    @AutoLog(value = "试验机处置审核")
    @ApiOperation(value = "试验机处置审核", notes = "试验机处置审核")
    @GetMapping(value = "/HBZCZAddOrUpdate")
    public Result<?> HBZCZAddOrUpdate(HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String people = String.valueOf(loginUser.getRealname());
        String zhbyj = request.getParameter("zhbyj");   //指挥部意见
        String zhbbh = request.getParameter("zhbbh");   //指挥部驳回
        String zhbjg = request.getParameter("zhbjg");   //指挥部结果
        String spyj = request.getParameter("spyj");   //审批意见
        String jlbh = request.getParameter("jlbh");   //监理驳回
        String spjg = request.getParameter("spjg");   //审批结果
        String wtyy = request.getParameter("wtyy");  //问题原因
        String clfs = request.getParameter("clfs");  //处理方式
        String cljg = request.getParameter("cljg");  //处理结果
        String syjbatch = request.getParameter("syjbatch");   //id
        String bizPath = request.getParameter("fileList");  //图片
        int syj = Integer.parseInt(request.getParameter("syj"));
//        int level = Integer.parseInt(request.getParameter("level"));
        int status = Integer.parseInt(request.getParameter("status"));
        Integer i = null;
        String tableName = "";
        String fieldName = "SYJID";
        if (syj == 0) {//延度试验
            QueryWrapper<WYanduM> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("SYJID", syjbatch);
            List<WYanduM> list = iwYanduMService.list(queryWrapper);
            if (list.size() == 0) {
                return Result.error("未查询到主表数据");
            }
            tableName = "w_yandu_m";

        } else if (syj == 1) {//针入度试验
            QueryWrapper<WZhenruduM> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("SYJID", syjbatch);
            List<WZhenruduM> list = iwZhenruduMService.list(queryWrapper);
            if (list.size() == 0) {
                return Result.error("未查询到主表数据");
            }
            tableName = "w_zhenrudu_m";
        } else if (syj == 2) {//软化点试验
            QueryWrapper<WRuanhuadianM> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("SYJID", syjbatch);
            List<WRuanhuadianM> list = iwRuanhuadianMService.list(queryWrapper);
            if (list.size() == 0) {
                return Result.error("未查询到主表数据");
            }
            tableName = "w_ruanhuadian_m";
        } else if (syj == 3) {//马歇尔试验
            QueryWrapper<WendingD> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("SYJID", syjbatch);
            List<WendingD> list = wendingDService.list(queryWrapper);
            if (list.size() == 0) {
                return Result.error("未查询到主表数据");
            }
            tableName = "w_wendingdu_m";
        } else if (syj == 4) {//摊铺碾压
            QueryWrapper<HcPavementAlarm> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", syjbatch);
            List<HcPavementAlarm> list = hcPavementAlarmService.list(queryWrapper);
            if (list.size() == 0) {
                return Result.error("未查询到主表数据");
            }
            tableName = "hc_pavement_alarm";
            fieldName = "id";
        } else if (syj == 5) {//红外光谱仪
            QueryWrapper<WHwgpy> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", syjbatch);
            List<WHwgpy> list = hwgpyService.list(queryWrapper);
            if (list.size() == 0) {
                return Result.error("未查询到主表数据");
            }
            tableName = "w_hwgpy";
            fieldName = "id";
        }
        if (status == 10) {//处置
            i = syjOverHandlerService.syjCZAddOrUpDate(wtyy, clfs, cljg, syjbatch, bizPath, people, 10);
        } else if (status == 30) {//监理驳回
            i = syjOverHandlerService.supervisorBohui(jlbh, syjbatch, people, 30);
        } else if (status == 40) {//监理审核
            i = syjOverHandlerService.supervisorAddOrUpdate(spyj, spjg, syjbatch, bizPath, people, 40);
        } else if (status == 50) {//指挥部审核
            i = syjOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, syjbatch, bizPath, people, 20);
            status = 20;
        } else if (status == 60) {//指挥部驳回
            i = syjOverHandlerService.headquartersBohui(zhbbh, syjbatch, people, 60);
        } else {
            return Result.error("错误");
        }
        if (i == 1) {
            Boolean b = syjOverHandlerService.updateTableField(tableName, fieldName, status, syjbatch);
            if (b) {
                return Result.OK("处置审核审批成功");
            }
        }
        return Result.error("处置审核审批失败");
    }


}
