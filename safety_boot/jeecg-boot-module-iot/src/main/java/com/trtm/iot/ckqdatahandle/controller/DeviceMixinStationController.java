package com.trtm.iot.ckqdatahandle.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationDetails;
import com.trtm.iot.ckqdatahandle.mapper.DeviceMixinStationMapper;
import com.trtm.iot.ckqdatahandle.service.impl.DeviceMixinStationDetailsServiceImpl;
import com.trtm.iot.ckqdatahandle.service.impl.DeviceMixinStationServiceImpl;
import com.trtm.iot.ckqdatahandle.vo.SelectDeviceMixinStationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Api(tags = "搅拌站信息")
@RestController
@RequestMapping("/mixin/station")
@Slf4j
public class DeviceMixinStationController extends JeecgController<DeviceMixinStation, DeviceMixinStationServiceImpl> {

    @Autowired
    private DeviceMixinStationServiceImpl deviceMixinStationService;

    @Autowired
    private DeviceMixinStationDetailsServiceImpl deviceMixinStationDetailsService;


    @ApiOperation(value = "查询搅拌站信息")
    @GetMapping("/selectMixinStationListPage")
    public Result<IPage<DeviceMixinStation>> selectMixinStationListPage(SelectDeviceMixinStationVo selectDeviceMixinStationVo) {
        Result<IPage<DeviceMixinStation>> result = new Result<>();
        IPage<DeviceMixinStation> deviceMixinStationIPage = deviceMixinStationService.selectMixinStationListPage(selectDeviceMixinStationVo);
        result.setResult(deviceMixinStationIPage);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation(value = "查询搅拌站详情")
    @GetMapping("/selectMixinStationDetailsListPage")
    public Result<IPage<DeviceMixinStationDetails>> selectMixinStationDetailsListPage(SelectDeviceMixinStationVo selectDeviceMixinStationVo) {
        Result<IPage<DeviceMixinStationDetails>> result = new Result<>();
        IPage<DeviceMixinStationDetails> deviceMixinStationDetailsIPage = deviceMixinStationDetailsService.selectMixinStationDetailsListPage(selectDeviceMixinStationVo);
        result.setResult(deviceMixinStationDetailsIPage);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation(value = "查看详情")
    @GetMapping("/selectMixinStationDetails")
    public Result<Map<String,Object>> selectMixinStationDetails(@ApiParam(name = "sid", value = "sid", required = true) @RequestParam(name = "sid", required = true) String sid,
                                                                @ApiParam(name = "uploadTime", value = "uploadTime", required = true) @RequestParam(name = "uploadTime", required = true) String uploadTime) {
        Result<Map<String,Object>> result = new Result<>();
        result.setSuccess(true);
        result.setResult(deviceMixinStationDetailsService.selectMixinStationDetails(sid,uploadTime));
        return result;
    }


}
