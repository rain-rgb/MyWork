package com.trtm.iot.devicemixpileoneoverhandler.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicehammeringhistorydataone.entity.DeviceHammeringHistorydataOne;
import com.trtm.iot.devicehammeringhistorydataone.service.IDeviceHammeringHistorydataOneService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.trtm.iot.devicemixpileoneoverhandler.service.IMixpileOneOverHandlerService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 软基超标处理
 * @Author: jeecg-boot
 * @Date: 2021-12-31
 * @Version: V1.0
 */
@Api(tags = "软基超标处理")
@RestController
@RequestMapping("/mixpileoneoverhandler/mixpileOneOverHandler")
@Slf4j
public class MixpileOneOverHandlerController extends JeecgController<MixpileOneOverHandler, IMixpileOneOverHandlerService> {
    @Autowired
    private IMixpileOneOverHandlerService mixpileOneOverHandlerService;
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    private IDeviceHammeringHistorydataOneService hammeringHistorydataOneService;
    /**
     * 分页列表查询
     *
     * @param mixpileOneOverHandler
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "软基超标处理-分页列表查询")
    @ApiOperation(value = "软基超标处理-分页列表查询", notes = "软基超标处理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MixpileOneOverHandler mixpileOneOverHandler,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MixpileOneOverHandler> queryWrapper = QueryGenerator.initQueryWrapper(mixpileOneOverHandler, req.getParameterMap());
        Page<MixpileOneOverHandler> page = new Page<MixpileOneOverHandler>(pageNo, pageSize);
        IPage<MixpileOneOverHandler> pageList = mixpileOneOverHandlerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param mixpileOneOverHandler
     * @return
     */
    @AutoLog(value = "软基超标处理-添加")
    @ApiOperation(value = "软基超标处理-添加", notes = "软基超标处理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MixpileOneOverHandler mixpileOneOverHandler) {
        mixpileOneOverHandlerService.save(mixpileOneOverHandler);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param mixpileOneOverHandler
     * @return
     */
    @AutoLog(value = "软基超标处理-处置")
    @ApiOperation(value = "软基超标处理-处置", notes = "软基超标处理-添加")
    @PostMapping(value = "/handle")
    public Result<?> handle(@RequestBody MixpileOneOverHandler mixpileOneOverHandler) {
        List<Integer> idList = new ArrayList<>();
        String[] split = mixpileOneOverHandler.getFilePath3().split(",");
        for (int i = 0; i < split.length; i++) {
            idList.add(Integer.parseInt(split[i]));
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mixpileOneOverHandler.setHandleTime(new Date());
        mixpileOneOverHandler.setHandlePerson(String.valueOf(loginUser.getRealname()));
        for (Integer id : idList) {
            mixpileOneOverHandler.setBaseid(String.valueOf(id));
            QueryWrapper<MixpileOneOverHandler> queryWrapper = new QueryWrapper();
            queryWrapper.eq("baseId", id);
            List<MixpileOneOverHandler> one = mixpileOneOverHandlerService.list(queryWrapper);
            if (one.size() == 0) {
                mixpileOneOverHandlerService.save(mixpileOneOverHandler);
            } else {
                mixpileOneOverHandler.setId(one.get(0).getId());
                mixpileOneOverHandlerService.updateById(mixpileOneOverHandler);
            }
            // 主表状态更新
            DeviceMixpileHistorydataOne deviceMixpileHistorydataOne = new DeviceMixpileHistorydataOne();
            deviceMixpileHistorydataOne.setId(id);
            deviceMixpileHistorydataOne.setHandlestate(mixpileOneOverHandler.getOverproofStatus());
            deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne);
        }
        return Result.OK("处置成功！");
    }


    /**
     * 编辑
     *
     * @param mixpileOneOverHandler
     * @return
     */
    @AutoLog(value = "软基超标处理")
    @ApiOperation(value = "软基超标处理-编辑", notes = "软基超标处理-编辑")
    @PutMapping(value = "/editNext")
    public Result<?> editNext(@RequestBody MixpileOneOverHandler mixpileOneOverHandler) {
        List<Integer> baseIdList = new ArrayList<>();
        String[] split = mixpileOneOverHandler.getFilePath3().split(",");
        for (int i = 0; i < split.length; i++) {
            baseIdList.add(Integer.parseInt(split[i]));
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mixpileOneOverHandler.setSupervisorHandleTime(new Date());
        mixpileOneOverHandler.setApprovalPerson(String.valueOf(loginUser.getRealname()));
        for (Integer baseId : baseIdList) {
            QueryWrapper<MixpileOneOverHandler> queryWrapper = new QueryWrapper();
            queryWrapper.eq("baseId", baseId);
            MixpileOneOverHandler one = mixpileOneOverHandlerService.getOne(queryWrapper);
            mixpileOneOverHandler.setId(one.getId());
            DeviceMixpileHistorydataOne deviceMixpileHistorydataOne = new DeviceMixpileHistorydataOne();
            deviceMixpileHistorydataOne.setId(baseId);
            if (mixpileOneOverHandler.getOverproofStatus() == 10) {
                deviceMixpileHistorydataOne.setHandlestate(10);
            } else if (mixpileOneOverHandler.getOverproofStatus() == 20) {
                deviceMixpileHistorydataOne.setHandlestate(20);
            } else if (mixpileOneOverHandler.getOverproofStatus() == 30) {
                deviceMixpileHistorydataOne.setHandlestate(30);
            } else {
                deviceMixpileHistorydataOne.setHandlestate(10);
            }
            mixpileOneOverHandlerService.updateById(mixpileOneOverHandler);
            deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne);
        }
        return Result.OK("编辑成功!");
    }

    /**
     * 编辑
     *
     * @param mixpileOneOverHandler
     * @return
     */
    @AutoLog(value = "软基超标处理")
    @ApiOperation(value = "软基超标处理-编辑", notes = "软基超标处理-编辑")
    @PutMapping(value = "/editNextcj")
    public Result<?> editNextcj(@RequestBody MixpileOneOverHandler mixpileOneOverHandler) {
        List<Integer> baseIdList = new ArrayList<>();
        String[] split = mixpileOneOverHandler.getFilePath3().split(",");
        for (int i = 0; i < split.length; i++) {
            baseIdList.add(Integer.parseInt(split[i]));
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mixpileOneOverHandler.setSupervisorHandleTime(new Date());
        mixpileOneOverHandler.setApprovalPerson(String.valueOf(loginUser.getRealname()));
        for (Integer baseId : baseIdList) {
            QueryWrapper<MixpileOneOverHandler> queryWrapper = new QueryWrapper();
            queryWrapper.eq("baseId", baseId);
            MixpileOneOverHandler one = mixpileOneOverHandlerService.getOne(queryWrapper);
            mixpileOneOverHandler.setId(one.getId());

            DeviceHammeringHistorydataOne deviceHammeringHistorydataOne = new DeviceHammeringHistorydataOne();
            deviceHammeringHistorydataOne.setId(baseId);
            if (mixpileOneOverHandler.getOverproofStatus() == 10) {
                deviceHammeringHistorydataOne.setOverproofStatus(10);
            } else if (mixpileOneOverHandler.getOverproofStatus() == 20) {
                deviceHammeringHistorydataOne.setOverproofStatus(20);
            } else if (mixpileOneOverHandler.getOverproofStatus() == 30) {
                deviceHammeringHistorydataOne.setOverproofStatus(30);
            } else {
                deviceHammeringHistorydataOne.setOverproofStatus(10);
            }
            mixpileOneOverHandlerService.updateById(mixpileOneOverHandler);
            hammeringHistorydataOneService.updateById(deviceHammeringHistorydataOne);
        }
        return Result.OK("编辑成功!");
    }

    /**
     * 编辑
     *
     * @param mixpileOneOverHandler
     * @return
     */
    @AutoLog(value = "软基超标处理-编辑")
    @ApiOperation(value = "软基超标处理-编辑", notes = "软基超标处理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody MixpileOneOverHandler mixpileOneOverHandler) {
        mixpileOneOverHandlerService.updateById(mixpileOneOverHandler);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "软基超标处理-通过id删除")
    @ApiOperation(value = "软基超标处理-通过id删除", notes = "软基超标处理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        mixpileOneOverHandlerService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "软基超标处理-批量删除")
    @ApiOperation(value = "软基超标处理-批量删除", notes = "软基超标处理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.mixpileOneOverHandlerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "软基超标处理-通过id查询")
    @ApiOperation(value = "软基超标处理-通过id查询", notes = "软基超标处理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MixpileOneOverHandler mixpileOneOverHandler = mixpileOneOverHandlerService.getById(id);
        if (mixpileOneOverHandler == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mixpileOneOverHandler);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mixpileOneOverHandler
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MixpileOneOverHandler mixpileOneOverHandler) {
        return super.exportXls(request, mixpileOneOverHandler, MixpileOneOverHandler.class, "软基超标处理");
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
        return super.importExcel(request, response, MixpileOneOverHandler.class);
    }

}
