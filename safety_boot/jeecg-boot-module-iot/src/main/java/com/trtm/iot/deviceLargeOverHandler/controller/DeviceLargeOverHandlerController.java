package com.trtm.iot.deviceLargeOverHandler.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.devicehistorystatic.service.IDevicehistoryStaticService;
import com.trtm.iot.devicetunnelenvironmentdata.entity.DeviceTunnelEnvironmentdata;
import com.trtm.iot.devicetunnelenvironmentdata.service.IDeviceTunnelEnvironmentdataService;
import com.trtm.iot.gualan.entity.GualanBase;
import com.trtm.iot.gualan.service.IGualanBaseService;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.deviceLargeOverHandler.entity.DeviceLargeOverHandler;
import com.trtm.iot.deviceLargeOverHandler.service.IDeviceLargeOverHandlerService;

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
 * @Description: device_large_over_handler
 * @Author: jeecg-boot
 * @Date: 2022-10-24
 * @Version: V1.0
 */
@Api(tags = "device_large_over_handler")
@RestController
@RequestMapping("/deviceLargeOverHandler/deviceLargeOverHandler")
@Slf4j
public class DeviceLargeOverHandlerController extends JeecgController<DeviceLargeOverHandler, IDeviceLargeOverHandlerService> {
    @Autowired
    private IDeviceLargeOverHandlerService deviceLargeOverHandlerService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceElectricHistorydataService deviceElectricHistorydataService;
    @Autowired
    private IDeviceTunnelEnvironmentdataService deviceTunnelEnvironmentdataService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;
    @Autowired
    private IGualanBaseService gualanBaseService;
    @Autowired
    private IDevicehistoryService devicehistoryService;


    /**
     * 分页列表查询
     *
     * @param deviceLargeOverHandler
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_large_over_handler-分页列表查询")
    @ApiOperation(value = "device_large_over_handler-分页列表查询", notes = "device_large_over_handler-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceLargeOverHandler deviceLargeOverHandler,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> lists = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            lists.add(split[i]);
        }
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(lists, 4);
        String shebeis = null;
        if (shebeiInfos.size() > 0) {
            shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
        }
        if (deviceLargeOverHandler.getShebeiNo() == null) {
            if ("null".equals(shebeis)) {
                shebeis = "空";
            }
            deviceLargeOverHandler.setShebeiNo(shebeis);
        }
        QueryWrapper<DeviceLargeOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(deviceLargeOverHandler, req.getParameterMap());
        Page<DeviceLargeOverHandler> page = new Page<DeviceLargeOverHandler>(pageNo, pageSize);
        IPage<DeviceLargeOverHandler> pageList = deviceLargeOverHandlerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param deviceLargeOverHandler
     * @return
     */
    @AutoLog(value = "device_large_over_handler-处置审核驳回")
    @ApiOperation(value = "device_large_over_handler-处置审核驳回", notes = "device_large_over_handler-处置审核驳回")
    @GetMapping(value = "/deal")
    public Result<?> deal(DeviceLargeOverHandler deviceLargeOverHandler, Integer flag) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        DeviceLargeOverHandler selectOne = deviceLargeOverHandlerService.selectOne(deviceLargeOverHandler.getBaseid());
        DeviceLargeOverHandler deviceLargeOverHandler1 = new DeviceLargeOverHandler();
        String baseid = deviceLargeOverHandler.getBaseid();
        String[] split = baseid.split(",");
        List<String> baseIdList = Arrays.asList(split);
        if (null == selectOne) {
            deviceLargeOverHandler.setHandlePerson(loginUser.getRealname());
            deviceLargeOverHandler.setHandleTime(new Date());
            deviceLargeOverHandler.setOverproofStatus(10);
            if (null == deviceLargeOverHandler.getShebeiType() && null != deviceLargeOverHandler.getShebeiNo()) {
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceLargeOverHandler.getShebeiNo());
                String sbtype = selectshebeione.getSbtype().toString();
                deviceLargeOverHandler.setShebeiType(sbtype);

            }
            deviceLargeOverHandlerService.save(deviceLargeOverHandler);
        } else {
            deviceLargeOverHandler1.setId(selectOne.getId());
            if (flag == 2) {
                deviceLargeOverHandler1.setHandlePerson(loginUser.getRealname());
                deviceLargeOverHandler1.setHandleTime(new Date());
                deviceLargeOverHandler1.setOverproofStatus(10);
            } else if (flag == 3) {
                deviceLargeOverHandler1.setOverproofStatus(30);
            } else {
                deviceLargeOverHandler1.setApprovalPerson(loginUser.getRealname());
                deviceLargeOverHandler1.setSupervisorHandleTime(new Date());
                deviceLargeOverHandler1.setOverproofStatus(20);
            }
            deviceLargeOverHandlerService.updateById(deviceLargeOverHandler1);
        }
        String shebeiType = deviceLargeOverHandler.getShebeiType();
        if (shebeiType.equals("17")) {
            QueryWrapper<DeviceElectricHistorydata> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", baseIdList);
            List<DeviceElectricHistorydata> list = deviceElectricHistorydataService.list(queryWrapper);
            if (null != list || list.size() > 0) {
                for (DeviceElectricHistorydata historydata : list) {
                    DeviceElectricHistorydata data = new DeviceElectricHistorydata();
                    data.setId(historydata.getId());
                    if (flag == 2) {
                        data.setOverproofStatus(10);
                    } else if (flag == 3) {
                        data.setOverproofStatus(30);
                    } else {
                        data.setOverproofStatus(20);
                    }
                    deviceElectricHistorydataService.updateById(data);
                    return Result.OK("处理成功!");
                }
            } else {
                return Result.error("未找到对应数据");
            }

        } else if (shebeiType.equals("20")) {
            QueryWrapper<DeviceTunnelEnvironmentdata> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", baseIdList);
            List<DeviceTunnelEnvironmentdata> list = deviceTunnelEnvironmentdataService.list(queryWrapper);
            if (null != list || list.size() > 0) {
                for (DeviceTunnelEnvironmentdata historydata : list) {
                    DeviceTunnelEnvironmentdata data = new DeviceTunnelEnvironmentdata();
                    data.setId(historydata.getId());
                    if (flag == 2) {
                        data.setOverproofStatus(10);
                    } else if (flag == 3) {
                        data.setOverproofStatus(30);
                    } else {
                        data.setOverproofStatus(20);
                    }
                    deviceTunnelEnvironmentdataService.updateById(data);
                    return Result.OK("处理成功!");
                }
            } else {
                return Result.error("未找到对应数据");
            }
        } else if (shebeiType.equals("2") || shebeiType.equals("3") || shebeiType.equals("50")) {
            QueryWrapper<DeviceCraneHistorydata> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", baseIdList);
            List<DeviceCraneHistorydata> list = deviceCraneHistorydataService.list(queryWrapper);
            if (null != list || list.size() > 0) {
                for (DeviceCraneHistorydata historydata : list) {
                    DeviceCraneHistorydata data = new DeviceCraneHistorydata();
                    data.setId(historydata.getId());
                    if (flag == 2) {
                        data.setOverproofStatus(10);
                    } else if (flag == 3) {
                        data.setOverproofStatus(30);
                    } else {
                        data.setOverproofStatus(20);
                    }
                    deviceCraneHistorydataService.updateById(data);
                    return Result.OK("处理成功!");

                }
            } else {
                return Result.error("未找到对应数据");
            }
        } else if (shebeiType.equals("1")) {
            QueryWrapper<GualanBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", baseIdList);
            List<GualanBase> list = gualanBaseService.list(queryWrapper);
            if (null != list || list.size() > 0) {
                for (GualanBase historydata : list) {
                    GualanBase data = new GualanBase();
                    data.setId(historydata.getId());
                    if (flag == 2) {
                        data.setOverproofStatus(10);
                    } else if (flag == 3) {
                        data.setOverproofStatus(30);
                    } else {
                        data.setOverproofStatus(20);
                    }
                    gualanBaseService.updateById(data);
                    return Result.OK("处理成功!");
                }

            } else {
                return Result.error("未找到对应数据");
            }
        }else if (shebeiType.equals("15")) {
            QueryWrapper<Devicehistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", baseIdList);
            List<Devicehistory> list = devicehistoryService.list(queryWrapper);
            if ( null != list || list.size() > 0) {
                for (Devicehistory historydata : list) {
                    Devicehistory data = new Devicehistory();
                    data.setId(historydata.getId());
                    if (flag == 2) {
                        data.setOverproofStatus(10);
                    } else if (flag == 3) {
                        data.setOverproofStatus(30);
                    } else {
                        data.setOverproofStatus(20);
                    }
                    devicehistoryService.updateById(data);
                    return Result.OK("处理成功!");
                }

            } else {
                return Result.error("未找到对应数据");
            }
        }
        return null;
    }


    /**
     * 添加
     *
     * @param deviceLargeOverHandler
     * @return
     */
    @AutoLog(value = "device_large_over_handler-添加")
    @ApiOperation(value = "device_large_over_handler-添加", notes = "device_large_over_handler-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceLargeOverHandler deviceLargeOverHandler) {
        deviceLargeOverHandlerService.save(deviceLargeOverHandler);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceLargeOverHandler
     * @return
     */
    @AutoLog(value = "device_large_over_handler-编辑")
    @ApiOperation(value = "device_large_over_handler-编辑", notes = "device_large_over_handler-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceLargeOverHandler deviceLargeOverHandler) {
        deviceLargeOverHandlerService.updateById(deviceLargeOverHandler);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_large_over_handler-通过id删除")
    @ApiOperation(value = "device_large_over_handler-通过id删除", notes = "device_large_over_handler-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceLargeOverHandlerService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "device_large_over_handler-批量删除")
    @ApiOperation(value = "device_large_over_handler-批量删除", notes = "device_large_over_handler-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceLargeOverHandlerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_large_over_handler-通过id查询")
    @ApiOperation(value = "device_large_over_handler-通过id查询", notes = "device_large_over_handler-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceLargeOverHandler deviceLargeOverHandler = deviceLargeOverHandlerService.getById(id);
        if (deviceLargeOverHandler == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceLargeOverHandler);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceLargeOverHandler
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceLargeOverHandler deviceLargeOverHandler) {
        return super.exportXls(request, deviceLargeOverHandler, DeviceLargeOverHandler.class, "device_large_over_handler");
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
        return super.importExcel(request, response, DeviceLargeOverHandler.class);
    }

}
