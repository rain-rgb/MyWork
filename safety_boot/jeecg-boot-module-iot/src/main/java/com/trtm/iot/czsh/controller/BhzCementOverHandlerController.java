package com.trtm.iot.czsh.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.devicehammeringhistorydataone.entity.DeviceHammeringHistorydataOne;
import com.trtm.iot.devicehammeringhistorydataone.service.IDeviceHammeringHistorydataOneService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.trtm.iot.lqbhzStatistics.service.IBhzLqStatisticsService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhzStatistics.entity.BhzSwStatistics;
import com.trtm.iot.swbhzStatistics.service.IBhzSwStatisticsService;
import com.trtm.iot.syj.service.IFWangnjService;
import org.jeecg.common.util.RedisUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.vo.BhzCementOverHandlerPage;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 拌合站处置审核信息
 * @Author: jeecg-boot
 * @Date: 2021-03-19
 * @Version: V1.0
 */
@Api(tags = "拌合站处置审核信息")
@RestController
@RequestMapping("/czsh/bhzCementOverHandler")
@Slf4j
public class BhzCementOverHandlerController {
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IBhzLqStatisticsService bhzLqStatisticsService;
    @Autowired
    private IBhzSwStatisticsService bhzSwStatisticsService;
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private IDeviceHammeringHistorydataOneService hammeringHistorydataOneService;

    @Autowired
    private ISchedulingCarService schedulingCarService;
    /**
     * 通过id添加或者修改
     */
    @AutoLog(value = "拌合站审核信息添加或者修改")
    @ApiOperation(value = "拌合站审核信息添加或者修改", notes = "拌合站审核信息添加或者修改")
    @GetMapping(value = "/SHAddOrUpdate")
    public Result<?> shenHe(BhzCementOverHandler bhzCementOverHandler,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shenpiren = String.valueOf(loginUser.getRealname());
        String spyj = request.getParameter("spyj");   //审批意见
        String spjg = request.getParameter("spjg");   //审批结果
        String hntbatch = request.getParameter("hntbatch");   //id
        String bizPath = request.getParameter("fileList");
        Integer bhz = Integer.valueOf(request.getParameter("bhz"));
        if (bhz == 0) {
            LambdaQueryWrapper<BhzCementBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzCementBase::getBatchNo, hntbatch);
            List<BhzCementBase> list = bhzCementBaseService.list(lambdaQueryWrapper);
            for (BhzCementBase bhzCementBase : list) {
                BhzCementBase bhzCementBase1 = new BhzCementBase();
                bhzCementBase1.setId(bhzCementBase.getId());
                bhzCementBase1.setOverproofStatus(20);
                bhzCementBaseService.updateById(bhzCementBase1);
            }
        }
        if (bhz == 1) {
            LambdaQueryWrapper<BhzLqBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzLqBases::getGuid, hntbatch);
            List<BhzLqBases> list = bhzLqBasesService.list(lambdaQueryWrapper);
            for (BhzLqBases bhzLqBases : list) {
                BhzLqBases bhzLqBases1 = new BhzLqBases();
                bhzLqBases1.setId(bhzLqBases.getId());
                bhzLqBases1.setOverproofStatus(20);
                bhzLqBasesService.updateById(bhzLqBases1);
            }
        }
        if (bhz == 2) {
//            LambdaQueryWrapper<BhzSwBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(BhzSwBases::getGuid, hntbatch);
//            List<BhzSwBases> list = bhzSwBasesService.list(lambdaQueryWrapper);
            List<BhzSwBases> list = bhzSwBasesService.selectByGuid(hntbatch);
            for (BhzSwBases bhzSwBases : list) {
                BhzSwBases bhzSwBases1 = new BhzSwBases();
                bhzSwBases1.setGuid(bhzSwBases.getGuid());
                bhzSwBases1.setOverproofStatus(20);
                bhzSwBasesService.updateByGuid(bhzSwBases1);
            }
        }
        int i = bhzCementOverHandlerService.shenheAddOrUpdate(spyj, spjg, hntbatch, bizPath, shenpiren);
        return Result.OK(i);
    }

    /**
     * 通过id添加或者修改 锤击桩
     */
    @AutoLog(value = "拌合站审核信息添加或者修改")
    @ApiOperation(value = "拌合站审核信息添加或者修改", notes = "拌合站审核信息添加或者修改")
    @GetMapping(value = "/SHAddOrUpdatecj")
    public Result<?> shenHecj(BhzCementOverHandler bhzCementOverHandler,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shenpiren = String.valueOf(loginUser.getRealname());
        String spyj = request.getParameter("spyj");   //审批意见
        String spjg = request.getParameter("spjg");   //审批结果
        String hntbatch = request.getParameter("hntbatch");   //id
        String bizPath = request.getParameter("fileList");
        Integer bhz = Integer.valueOf(request.getParameter("bhz"));
        if (bhz == 0) {
            LambdaQueryWrapper<DeviceHammeringHistorydataOne> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DeviceHammeringHistorydataOne::getId, hntbatch);
            List<DeviceHammeringHistorydataOne> list = hammeringHistorydataOneService.list(lambdaQueryWrapper);
            for (DeviceHammeringHistorydataOne bhzCementBase : list) {
                DeviceHammeringHistorydataOne deviceHammeringHistorydataOne = new DeviceHammeringHistorydataOne();
                deviceHammeringHistorydataOne.setId(bhzCementBase.getId());
                deviceHammeringHistorydataOne.setOverproofStatus(20);
                hammeringHistorydataOneService.updateById(deviceHammeringHistorydataOne);
            }
        }
        int i = bhzCementOverHandlerService.shenheAddOrUpdate(spyj, spjg, hntbatch, bizPath, shenpiren);
        return Result.OK(i);
    }
    /**
     * 根据id添加或者修改处置信息
     */
    @AutoLog(value = "拌合站处置信息添加或者修改")
    @ApiOperation(value = "拌合站处置信息添加或者修改", notes = "拌合站处置信息添加或者修改")
    @GetMapping(value = "/CZAddOrUpdate")
    public Result<?> chuZhi(BhzCementOverHandler bhzCementOverHandler,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String chuzhiren = String.valueOf(loginUser.getRealname());
        String wtyy = request.getParameter("wtyy");  //问题原因
        String clfs = request.getParameter("clfs");  //处理方式
        String cljg = request.getParameter("cljg");  //处理结果
        String hntbatch = request.getParameter("hntbatch");   //id
        int bhz = Integer.parseInt(request.getParameter("bhz"));
        if (bhz == 0) {
            LambdaQueryWrapper<BhzCementBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzCementBase::getBatchNo, hntbatch);
            List<BhzCementBase> list = bhzCementBaseService.list(lambdaQueryWrapper);
            for (BhzCementBase bhzCementBase : list) {
                BhzCementBase bhzCementBase1 = new BhzCementBase();
                bhzCementBase1.setId(bhzCementBase.getId());
                bhzCementBase1.setOverproofStatus(10);
                bhzCementBaseService.updateById(bhzCementBase1);
            }
        }
        if (bhz == 1) {
            LambdaQueryWrapper<BhzLqBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzLqBases::getGuid, hntbatch);
            List<BhzLqBases> list = bhzLqBasesService.list(lambdaQueryWrapper);
            for (BhzLqBases bhzLqBases : list) {
                BhzLqBases bhzLqBases1 = new BhzLqBases();
                bhzLqBases1.setId(bhzLqBases.getId());
                bhzLqBases1.setOverproofStatus(10);
                bhzLqBasesService.updateById(bhzLqBases1);
            }
        }
        if (bhz == 2) {
//            LambdaQueryWrapper<BhzSwBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(BhzSwBases::getGuid, hntbatch);
            List<BhzSwBases> list = bhzSwBasesService.selectByGuid(hntbatch);
            for (BhzSwBases bhzSwBases : list) {
                BhzSwBases bhzSwBases1 = new BhzSwBases();
                bhzSwBases1.setGuid(bhzSwBases.getGuid());
                bhzSwBases1.setOverproofStatus(10);
                bhzSwBasesService.updateByGuid(bhzSwBases1);
            }
        }
        String bizPath = request.getParameter("fileList");  //图片
        int i = bhzCementOverHandlerService.chuZhiAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, chuzhiren);
        return Result.OK(i);
    }

    /**
     * 根据id添加或者修改处置信息
     */
    @AutoLog(value = "拌合站处置信息添加或者修改")
    @ApiOperation(value = "拌合站处置信息添加或者修改", notes = "拌合站处置信息添加或者修改")
    @GetMapping(value = "/CZAddOrUpdatecj")
    public Result<?> chuZhicj(BhzCementOverHandler bhzCementOverHandler,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String chuzhiren = String.valueOf(loginUser.getRealname());
        String wtyy = request.getParameter("wtyy");  //问题原因
        String clfs = request.getParameter("clfs");  //处理方式
        String cljg = request.getParameter("cljg");  //处理结果
        String hntbatch = request.getParameter("hntbatch");   //id
        int bhz = Integer.parseInt(request.getParameter("bhz"));
        if (bhz == 0) {
            LambdaQueryWrapper<DeviceHammeringHistorydataOne> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DeviceHammeringHistorydataOne::getId, hntbatch);
            List<DeviceHammeringHistorydataOne> list = hammeringHistorydataOneService.list(lambdaQueryWrapper);
            for (DeviceHammeringHistorydataOne bhzCementBase : list) {
                DeviceHammeringHistorydataOne deviceHammeringHistorydataOne = new DeviceHammeringHistorydataOne();
                deviceHammeringHistorydataOne.setId(bhzCementBase.getId());
                deviceHammeringHistorydataOne.setOverproofStatus(10);
                hammeringHistorydataOneService.updateById(deviceHammeringHistorydataOne);
            }
        }
        String bizPath = request.getParameter("fileList");  //图片
        int i = bhzCementOverHandlerService.chuZhiAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, chuzhiren);
        return Result.OK(i);
    }
    /**
     * 监理驳回
     *
     * @param bhzCementOverHandler
     * @return
     */
    @AutoLog(value = "拌合站处置审核信息-监理驳回")
    @ApiOperation(value = "拌合站处置审核信息-监理驳回", notes = "拌合站处置审核信息-监理驳回")
    @GetMapping(value = "/bohuiedit")
    public Result<?> edit1(BhzCementOverHandler bhzCementOverHandler, Integer bhz, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String jianli = String.valueOf(loginUser.getUsername());
        BhzCementOverHandler bhzCementOverHandlerEntity = bhzCementOverHandlerService.selectlist(bhzCementOverHandler.getBaseid());
        if (bhzCementOverHandlerEntity == null) {
            return Result.error("未找到对应数据");
        } else {
            BhzCementOverHandler bhzCementOverHandler1 = new BhzCementOverHandler();
            bhzCementOverHandler1.setId(bhzCementOverHandlerEntity.getId());
            bhzCementOverHandler1.setOverproofStatus(30);
            bhzCementOverHandler1.setApprovalPerson(jianli);
            bhzCementOverHandler1.setSupervisorHandleTime(new Date());
            bhzCementOverHandler1.setRemark(bhzCementOverHandler.getRemark());
            bhzCementOverHandlerService.updateById(bhzCementOverHandler1);
            if (bhz == 0) {
                LambdaQueryWrapper<BhzCementBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(BhzCementBase::getBatchNo, bhzCementOverHandlerEntity.getBaseid());
                List<BhzCementBase> list = bhzCementBaseService.list(lambdaQueryWrapper);
                for (BhzCementBase bhzCementBase : list) {
                    BhzCementBase bhzCementBase1 = new BhzCementBase();
                    bhzCementBase1.setId(bhzCementBase.getId());
                    bhzCementBase1.setOverproofStatus(30);
                    bhzCementBaseService.updateById(bhzCementBase1);
                }
            }
            if (bhz == 1) {
                LambdaQueryWrapper<BhzLqBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(BhzLqBases::getGuid, bhzCementOverHandlerEntity.getBaseid());
                List<BhzLqBases> list = bhzLqBasesService.list(lambdaQueryWrapper);
                for (BhzLqBases bhzLqBases : list) {
                    BhzLqBases bhzLqBases1 = new BhzLqBases();
                    bhzLqBases1.setId(bhzLqBases.getId());
                    bhzLqBases1.setOverproofStatus(30);
                    bhzLqBasesService.updateById(bhzLqBases1);
                }
            }
            if (bhz == 2) {
                LambdaQueryWrapper<BhzSwBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(BhzSwBases::getGuid, bhzCementOverHandler.getBaseid());
                List<BhzSwBases> list = bhzSwBasesService.list(lambdaQueryWrapper);
                for (BhzSwBases bhzSwBases : list) {
                    BhzSwBases bhzSwBases1 = new BhzSwBases();
                    bhzSwBases1.setGuid(bhzSwBases.getGuid());
                    bhzSwBases1.setOverproofStatus(30);
                    bhzSwBasesService.updateByGuid(bhzSwBases1);
                }
            }
            return Result.OK("驳回成功!");
        }
    }

    /**
     * 监理驳回
     *
     * @param bhzCementOverHandler
     * @return
     */
    @AutoLog(value = "拌合站处置审核信息-监理驳回")
    @ApiOperation(value = "拌合站处置审核信息-监理驳回", notes = "拌合站处置审核信息-监理驳回")
    @GetMapping(value = "/bohuieditcj")
    public Result<?> edit1cj(BhzCementOverHandler bhzCementOverHandler, Integer bhz, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String jianli = String.valueOf(loginUser.getUsername());
        BhzCementOverHandler bhzCementOverHandlerEntity = bhzCementOverHandlerService.selectlist(bhzCementOverHandler.getBaseid());
        if (bhzCementOverHandlerEntity == null) {
            return Result.error("未找到对应数据");
        } else {
            BhzCementOverHandler bhzCementOverHandler1 = new BhzCementOverHandler();
            bhzCementOverHandler1.setId(bhzCementOverHandlerEntity.getId());
            bhzCementOverHandler1.setOverproofStatus(30);
            bhzCementOverHandler1.setApprovalPerson(jianli);
            bhzCementOverHandler1.setSupervisorHandleTime(new Date());
            bhzCementOverHandler1.setRemark(bhzCementOverHandler.getRemark());
            bhzCementOverHandlerService.updateById(bhzCementOverHandler1);
            if (bhz == 0) {
                LambdaQueryWrapper<DeviceHammeringHistorydataOne> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(DeviceHammeringHistorydataOne::getId, bhzCementOverHandlerEntity.getBaseid());
                List<DeviceHammeringHistorydataOne> list = hammeringHistorydataOneService.list(lambdaQueryWrapper);
                for (DeviceHammeringHistorydataOne bhzCementBase : list) {
                    DeviceHammeringHistorydataOne deviceHammeringHistorydataOne = new DeviceHammeringHistorydataOne();
                    deviceHammeringHistorydataOne.setId(bhzCementBase.getId());
                    deviceHammeringHistorydataOne.setOverproofStatus(30);
                    hammeringHistorydataOneService.updateById(deviceHammeringHistorydataOne);
                }
            }
            return Result.OK("驳回成功!");
        }
    }
    /**
     * 分页列表查询
     *
     * @param bhzCementOverHandler
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站处置审核信息-分页列表查询")
    @ApiOperation(value = "拌合站处置审核信息-分页列表查询", notes = "拌合站处置审核信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzCementOverHandler bhzCementOverHandler,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BhzCementOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementOverHandler, req.getParameterMap());
        Page<BhzCementOverHandler> page = new Page<BhzCementOverHandler>(pageNo, pageSize);
        IPage<BhzCementOverHandler> pageList = bhzCementOverHandlerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param bhzCementOverHandler
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站处置审核信息流程形式")
    @ApiOperation(value = "拌合站处置审核信息流程形式", notes = "拌合站处置审核信息流程形式")
    @GetMapping(value = "/handlelist")
    public Result<?> handlelist(BhzCementOverHandler bhzCementOverHandler,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                HttpServletRequest req) {
        List<Map<String, Object>> handleprosess = new ArrayList<>();
        // 拌合站数据
        LambdaQueryWrapper<BhzCementBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BhzCementBase::getBatchNo, bhzCementOverHandler.getBaseid());
        List<BhzCementBase> list = bhzCementBaseService.list(lambdaQueryWrapper);

        Map<String, Object> yujin = new HashMap<>();
        yujin.put("tile", "预警");
        Map<String, Object> chuzhi = new HashMap<>();
        chuzhi.put("tile", "处置");
        Map<String, Object> bohui = new HashMap<>();
        bohui.put("tile", "驳回");
        Map<String, Object> checked = new HashMap<>();
        checked.put("tile", "审核");
        Map<String, Object> wancheng = new HashMap<>();
        wancheng.put("tile", "指挥部审批");

        if (null != list && list.size() > 0) {
            BhzCementBase bhz = list.get(0);
            if (bhz.getOverLevel() > 0) {
                String reason = "";
                if (bhz.getOverLevel() == 1) {
                    reason = "初级超标";
                } else if (bhz.getOverLevel() == 2) {
                    reason = "中级超标";
                } else if (bhz.getOverLevel() == 3) {
                    reason = "高级超标";
                }



                yujin.put("time", bhz.getProductDatetime());
                yujin.put("person", "");
                yujin.put("way", "");
                yujin.put("reason", reason + ":" + bhz.getOverReason() == null ? "" : bhz.getOverReason());
                yujin.put("file", "");
                yujin.put("overproofStatus",bhz.getOverproofStatus());


                // 处置信息
                QueryWrapper<BhzCementOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementOverHandler, req.getParameterMap());
                Page<BhzCementOverHandler> page = new Page<BhzCementOverHandler>(pageNo, pageSize);
                IPage<BhzCementOverHandler> pageList = bhzCementOverHandlerService.page(page, queryWrapper);
                BhzCementOverHandler handle = new BhzCementOverHandler();
                if (pageList.getRecords().size() > 0) {
                    handle = pageList.getRecords().get(0);
                }

                wancheng.put("time", handle.getHeadquartersHandleTime());
                wancheng.put("person", handle.getHeadquartersApproval());
                wancheng.put("way", handle.getHandleResult());
                wancheng.put("reason", handle.getRemark());
                wancheng.put("file", handle.getFilePath3());
                handleprosess.add(wancheng);

                chuzhi.put("time", handle.getHandleTime());
                chuzhi.put("person", handle.getHandlePerson());
                chuzhi.put("way", handle.getHandleWay());
                chuzhi.put("reason", handle.getHandleResult());
                chuzhi.put("file", handle.getFilePath2());

                checked.put("time", handle.getSupervisorHandleTime());
                checked.put("person", handle.getApprovalPerson());
                checked.put("way", handle.getSupervisorApproval());
                checked.put("reason", handle.getSupervisorResult());
                checked.put("file", handle.getFilePath());
                if (handle.getOverproofStatus() != null && handle.getOverproofStatus() == 30) {

                    bohui.put("time", handle.getSupervisorHandleTime());
                    bohui.put("person", handle.getApprovalPerson());
                    bohui.put("way", "");
                    bohui.put("reason", handle.getRemark());
                    bohui.put("file", "");
                    handleprosess.add(bohui);
                }else{
                    handleprosess.add(checked);
                }


                handleprosess.add(chuzhi);
                handleprosess.add(yujin);
            }

        }


        return Result.OK(handleprosess);
    }


    /**
     * 添加
     *
     * @param bhzCementOverHandler
     * @return
     */
    @AutoLog(value = "混凝土发车单处置审核信息-处置审核")
    @ApiOperation(value = "混凝土发车处置审核信息-添加", notes = "混凝土发车处置审核信息-添加")
    @PostMapping(value = "/carHandle")
    public Result<?> carHandle(@RequestBody BhzCementOverHandler bhzCementOverHandler) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String handlename = String.valueOf(loginUser.getUsername());
        // 混凝土发车单
        bhzCementOverHandler.setHandletype(4);
        // 处置
        switch (bhzCementOverHandler.getOverproofStatus()) {
            case 10://处置
                bhzCementOverHandler.setHandlePerson(handlename);
                bhzCementOverHandler.setHandleTime(new Date());
                break;
            case 30://监理驳回
                bhzCementOverHandler.setApprovalPerson(handlename);
                bhzCementOverHandler.setSupervisorHandleTime(new Date());
                break;
            case 40://监理审核
                bhzCementOverHandler.setApprovalPerson(handlename);
                bhzCementOverHandler.setSupervisorHandleTime(new Date());
                break;
            case 60://指挥部驳回
                bhzCementOverHandler.setHeadquartersPerson(handlename);
                bhzCementOverHandler.setHeadquartersHandleTime(new Date());
                break;
            case 20://闭合
                bhzCementOverHandler.setHeadquartersPerson(handlename);
                bhzCementOverHandler.setHeadquartersHandleTime(new Date());
                break;

            default:
                break;
        }
        BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(bhzCementOverHandler.getBaseid());
        if(null == selectlist){
            bhzCementOverHandlerService.saveOrUpdate(bhzCementOverHandler);
        }else{
            bhzCementOverHandler.setId(selectlist.getId());
            bhzCementOverHandlerService.saveOrUpdate(bhzCementOverHandler);
        }


        SchedulingCar car = new SchedulingCar();
        car.setId(Integer.valueOf(bhzCementOverHandler.getBaseid()));
        car.setOverhandle(bhzCementOverHandler.getOverproofStatus());
         schedulingCarService.updateById(car);

        return Result.OK("操作成功！");
    }

    /**
     * 添加
     *
     * @param bhzCementOverHandlerPage
     * @return
     */
    @AutoLog(value = "拌合站处置审核信息-添加")
    @ApiOperation(value = "拌合站处置审核信息-添加", notes = "拌合站处置审核信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzCementOverHandlerPage bhzCementOverHandlerPage) {
        BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
        BeanUtils.copyProperties(bhzCementOverHandlerPage, bhzCementOverHandler);
        bhzCementOverHandlerService.saveMain(bhzCementOverHandler);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "拌合站处置审核信息-添加")
    @ApiOperation(value = "拌合站处置审核信息-添加", notes = "拌合站处置审核信息-添加")
    @PostMapping(value = "/addOrUpdate")
    public Result<?> addOrUpdate(@RequestBody BhzCementOverHandler handle) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String handlename = String.valueOf(loginUser.getUsername());

        if(handle.getOverproofStatus() == 28){
            handle.setHandlePerson(handlename);
            handle.setHandleTime(new Date());
        }else if(handle.getOverproofStatus() == 29){
            handle.setApprovalPerson(handlename);
            handle.setSupervisorHandleTime(new Date());
        }
        bhzCementOverHandlerService.saveOrUpdate(handle);
        bhzCementBaseService.updatehntbhzrenwustatus(handle.getShebeiNo(), handle.getOverproofStatus());
        return Result.OK("成功！");
    }
    /**
     * 编辑
     *
     * @param bhzCementOverHandlerPage
     * @return
     */
    @AutoLog(value = "拌合站处置审核信息-编辑")
    @ApiOperation(value = "拌合站处置审核信息-编辑", notes = "拌合站处置审核信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzCementOverHandlerPage bhzCementOverHandlerPage) {
        BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
        BeanUtils.copyProperties(bhzCementOverHandlerPage, bhzCementOverHandler);
        BhzCementOverHandler bhzCementOverHandlerEntity = bhzCementOverHandlerService.getById(bhzCementOverHandler.getId());
        if (bhzCementOverHandlerEntity == null) {
            return Result.error("未找到对应数据");
        }
        bhzCementOverHandlerService.updateMain(bhzCementOverHandler);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "拌合站处置审核信息-通过id删除")
    @ApiOperation(value = "拌合站处置审核信息-通过id删除", notes = "拌合站处置审核信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzCementOverHandlerService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "拌合站处置审核信息-批量删除")
    @ApiOperation(value = "拌合站处置审核信息-批量删除", notes = "拌合站处置审核信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzCementOverHandlerService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "拌合站处置审核信息-通过id查询")
    @ApiOperation(value = "拌合站处置审核信息-通过id查询", notes = "拌合站处置审核信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzCementOverHandler bhzCementOverHandler = bhzCementOverHandlerService.getById(id);
        if (bhzCementOverHandler == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzCementOverHandler);

    }


    /**
     * 导出excel
     *
     * @param request
     * @param bhzCementOverHandler
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzCementOverHandler bhzCementOverHandler) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<BhzCementOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementOverHandler, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<BhzCementOverHandler> queryList = bhzCementOverHandlerService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<BhzCementOverHandler> bhzCementOverHandlerList = new ArrayList<BhzCementOverHandler>();
        if (oConvertUtils.isEmpty(selections)) {
            bhzCementOverHandlerList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            bhzCementOverHandlerList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<BhzCementOverHandlerPage> pageList = new ArrayList<BhzCementOverHandlerPage>();
        for (BhzCementOverHandler main : bhzCementOverHandlerList) {
            BhzCementOverHandlerPage vo = new BhzCementOverHandlerPage();
            BeanUtils.copyProperties(main, vo);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "拌合站处置审核信息列表");
        mv.addObject(NormalExcelConstants.CLASS, BhzCementOverHandlerPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拌合站处置审核信息数据", "导出人:" + sysUser.getRealname(), "拌合站处置审核信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<BhzCementOverHandlerPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BhzCementOverHandlerPage.class, params);
                for (BhzCementOverHandlerPage page : list) {
                    BhzCementOverHandler po = new BhzCementOverHandler();
                    BeanUtils.copyProperties(page, po);
                    bhzCementOverHandlerService.saveMain(po);
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

    /**
     * 根据id添加或者修改处置信息
     */
    @AutoLog(value = "拌合站处置信息添加或者修改")
    @ApiOperation(value = "拌合站处置信息添加或者修改", notes = "拌合站处置信息添加或者修改")
    @GetMapping(value = "/HBZCZAddOrUpdate")
    public Result<?> HBZCZAddOrUpdate(BhzCementOverHandler bhzCementOverHandler,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws ParseException {
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
        String hntbatch = request.getParameter("hntbatch");   //id
        String bizPath = request.getParameter("fileList");  //图片
        int bhz = Integer.parseInt(request.getParameter("bhz"));
        int level = Integer.parseInt(request.getParameter("level"));
        int status = Integer.parseInt(request.getParameter("status"));
        Integer i = null;
        if (bhz == 0) {
            LambdaQueryWrapper<BhzCementBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzCementBase::getBatchNo, hntbatch);
            List<BhzCementBase> list = bhzCementBaseService.list(lambdaQueryWrapper);
            for (BhzCementBase bhzCementBase : list) {
                BhzCementBase bhzCementBase1 = new BhzCementBase();
                bhzCementBase1.setId(bhzCementBase.getId());
                if (level == 1) {
                    if (status == 10) {
                        bhzCementBase1.setOverproofStatus(20);
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 20);
                        hntStatistic(bhzCementBase);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 2) {
                    if (status == 10) {//处置
                        bhzCementBase1.setOverproofStatus(10);
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzCementBase1.setOverproofStatus(30);
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzCementBase1.setOverproofStatus(20);
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 20);
                        hntStatistic(bhzCementBase);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 3) {
                    if (status == 10) {//处置
                        bhzCementBase1.setOverproofStatus(10);
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzCementBase1.setOverproofStatus(30);
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzCementBase1.setOverproofStatus(40);
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 40);
                    } else if (status == 50) {//指挥部审核
                        bhzCementBase1.setOverproofStatus(20);
                        i = bhzCementOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, hntbatch, bizPath, people, 20);
                        hntStatistic(bhzCementBase);
                    } else if (status == 60) {//指挥部驳回
                        bhzCementBase1.setOverproofStatus(60);
                        i = bhzCementOverHandlerService.headquartersBohui(zhbbh, hntbatch, people, 60);
                    } else {
                        return Result.error("错误");
                    }
                }
                bhzCementBaseService.updateById(bhzCementBase1);
            }
        }
        if (bhz == 1) {
            LambdaQueryWrapper<BhzLqBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzLqBases::getGuid, hntbatch);
            List<BhzLqBases> list = bhzLqBasesService.list(lambdaQueryWrapper);
            for (BhzLqBases bhzLqBases : list) {
                BhzLqBases bhzLqBases1 = new BhzLqBases();
                bhzLqBases1.setId(bhzLqBases.getId());
                if (level == 1) {
                    if (status == 10) {
                        bhzLqBases1.setOverproofStatus(20);
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 20);
                        lqStatistic(bhzLqBases);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 2) {
                    if (status == 10) {//处置
                        bhzLqBases1.setOverproofStatus(10);
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzLqBases1.setOverproofStatus(30);
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzLqBases1.setOverproofStatus(20);
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 20);
                        lqStatistic(bhzLqBases);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 3) {
                    if (status == 10) {//处置
                        bhzLqBases1.setOverproofStatus(10);
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzLqBases1.setOverproofStatus(30);
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzLqBases1.setOverproofStatus(40);
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 40);
                    } else if (status == 50) {//指挥部审核
                        bhzLqBases1.setOverproofStatus(20);
                        i = bhzCementOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, hntbatch, bizPath, people, 20);
                        lqStatistic(bhzLqBases);
                    } else if (status == 60) {//指挥部驳回
                        bhzLqBases1.setOverproofStatus(60);
                        i = bhzCementOverHandlerService.headquartersBohui(zhbbh, hntbatch, people, 60);
                    } else {
                        return Result.error("错误");
                    }
                }
                bhzLqBasesService.updateById(bhzLqBases1);
            }
        }
        if (bhz == 2) {
            LambdaQueryWrapper<BhzSwBases> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BhzSwBases::getGuid, hntbatch);
            List<BhzSwBases> list = bhzSwBasesService.list(lambdaQueryWrapper);
            for (BhzSwBases bhzSwBases : list) {
                BhzSwBases bhzSwBases1 = new BhzSwBases();
                bhzSwBases1.setGuid(bhzSwBases.getGuid());
                if (level == 1) {
                    if (status == 10) {
                        bhzSwBases1.setOverproofStatus(20);
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 20);
                        swStatistic(bhzSwBases);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 2) {
                    if (status == 10) {//处置
                        bhzSwBases1.setOverproofStatus(10);
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzSwBases1.setOverproofStatus(30);
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzSwBases1.setOverproofStatus(20);
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 20);
                        swStatistic(bhzSwBases);
                    } else {
                        return Result.error("错误");
                    }
                } else if (level == 3) {
                    if (status == 10) {//处置
                        bhzSwBases1.setOverproofStatus(10);
                        i = bhzCementOverHandlerService.BhzCZAddOrUpDate(wtyy, clfs, cljg, hntbatch, bizPath, people, 10);
                    } else if (status == 30) {//监理驳回
                        bhzSwBases1.setOverproofStatus(30);
                        i = bhzCementOverHandlerService.supervisorBohui(jlbh, hntbatch, people, 30);
                    } else if (status == 40) {//监理审核
                        bhzSwBases1.setOverproofStatus(40);
                        i = bhzCementOverHandlerService.supervisorAddOrUpdate(spyj, spjg, hntbatch, bizPath, people, 40);
                    } else if (status == 50) {//指挥部审核
                        bhzSwBases1.setOverproofStatus(20);
                        i = bhzCementOverHandlerService.headquartersAddOrUpdate(zhbyj, zhbjg, hntbatch, bizPath, people, 20);
                        swStatistic(bhzSwBases);
                    } else if (status == 60) {//指挥部驳回
                        bhzSwBases1.setOverproofStatus(60);
                        i = bhzCementOverHandlerService.headquartersBohui(zhbbh, hntbatch, people, 60);
                    } else {
                        return Result.error("错误");
                    }
                }
                bhzSwBasesService.updateByGuid(bhzSwBases1);
            }
        }

        return Result.OK(i);
    }
    public void lqStatistic(BhzLqBases bhzLqBases) throws ParseException {
        String shebeibianhao = bhzLqBases.getShebeibianhao();
        String projectName = bhzLqBases.getProjectName();
        String poureLocation = bhzLqBases.getPoureLocation();
        String jobLocation = bhzLqBases.getJobLocation();
        String formulaNo = bhzLqBases.getFormulaNo();
        String strengthRank = bhzLqBases.getStrengthRank();
        String chuliaoshijian = bhzLqBases.getChuliaoshijian();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = ft.parse(chuliaoshijian);
        BhzLqStatistics bhzLqStatistics = bhzLqStatisticsService.selectbyStatistic(parse,shebeibianhao,projectName,jobLocation,formulaNo,strengthRank,poureLocation);
        if(bhzLqStatistics != null){
            if(bhzLqStatistics.getBiheDish()==null){
                bhzLqStatistics.setBiheDish(1);
            }else{
                bhzLqStatistics.setBiheDish(bhzLqStatistics.getBiheDish()+1);
            }
            bhzLqStatisticsService.updateById(bhzLqStatistics);
        }

    }
    public void swStatistic(BhzSwBases bhzSwBases) throws ParseException {
        String shebeibianhao = bhzSwBases.getShebeibianhao();
        String projectName = bhzSwBases.getProjectName();
        String jobLocation = bhzSwBases.getJobLocation();
        String poureLocation = bhzSwBases.getPoureLocation();
        String formulaNo = bhzSwBases.getFormulaNo();
        String strengthRank = bhzSwBases.getStrengthRank();
        String chuliaoshijian = bhzSwBases.getChuliaoshijian();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = ft.parse(chuliaoshijian);
        BhzSwStatistics bhzSwStatistics = bhzSwStatisticsService.selectbyStatistic(parse,shebeibianhao,projectName,jobLocation,formulaNo,strengthRank,poureLocation);
        if(bhzSwStatistics != null){
            if(bhzSwStatistics.getBiheDish()==null){
                bhzSwStatistics.setBiheDish(1);
            }else{
                bhzSwStatistics.setBiheDish(bhzSwStatistics.getBiheDish()+1);
            }
            bhzSwStatisticsService.updateById(bhzSwStatistics);
        }

    }
    public void hntStatistic(BhzCementBase bhzCementBase) throws ParseException {
        Date productDatetime = bhzCementBase.getProductDatetime();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format = ft.format(productDatetime);
        Date parse = ft.parse(format);
        String projectName = bhzCementBase.getProjectName();
        String poureLocation = bhzCementBase.getPoureLocation();
        String jobLocation = bhzCementBase.getJobLocation();
        String formulaNo = bhzCementBase.getFormulaNo();
        String strengthRank = bhzCementBase.getStrengthRank();
        String shebeiNo = bhzCementBase.getShebeiNo();
        //查询统计表中匹配的数据
        BhzCementStatistics selectlimit = bhzCementStatisticsService.selectlimit(parse, projectName, poureLocation, jobLocation, formulaNo, strengthRank, shebeiNo);
        if(selectlimit != null){
            if(selectlimit.getBiheDish()==null){
                selectlimit.setBiheDish(1);
            }else{
                selectlimit.setBiheDish(selectlimit.getBiheDish()+1);
            }
            bhzCementStatisticsService.updateById(selectlimit);
        }

    }
}
