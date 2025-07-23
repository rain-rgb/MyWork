/**
 * @ClassName DeviceMixinStationUpdateLogController.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年08月02日 15:14:00
 */
package com.trtm.iot.ckqdatahandle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationUpdateLog;
import com.trtm.iot.ckqdatahandle.service.IDeviceMixinStationUpdateLogService;
import com.trtm.iot.ckqdatahandle.vo.SelectDeviceMixinStationUpdateLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "搅拌站信息修正记录")
@RestController
@RequestMapping("/mixin/station/update/log")
@Slf4j
public class DeviceMixinStationUpdateLogController {

    @Autowired
    private IDeviceMixinStationUpdateLogService deviceMixinStationUpdateLogService;

    @ApiOperation(value = "查询搅拌站修正记录")
    @GetMapping("/selectMixinStationUpdateLogPage")
    public Result<IPage<DeviceMixinStationUpdateLog>>selectMixinStationUpdateLogPage(SelectDeviceMixinStationUpdateLogVo selectDeviceMixinStationUpdateLogVo){
        Result<IPage<DeviceMixinStationUpdateLog>> result = new Result<>();
        IPage<DeviceMixinStationUpdateLog> deviceMixinStationUpdateLogIPage = deviceMixinStationUpdateLogService.selectMixinStationUpdateLogPage(selectDeviceMixinStationUpdateLogVo);
        result.setResult(deviceMixinStationUpdateLogIPage);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation(value = "保存搅拌站配料信息修正记录")
    @GetMapping("/saveMixinStationInfoUpdateLog")
    public Result<Boolean> mixinStationInfoUpdate(@ApiParam(name = "id", value = "id", required = true) @RequestParam(name = "id", required = true) Integer id,
                                                  @ApiParam(name = "batchingProductionUpdate", value = "修正值", required = false) @RequestParam(name = "batchingProductionUpdate", required = false) String batchingProductionUpdate,
                                                  @ApiParam(name = "groutingTotalUpdate", value = "修正值", required = false) @RequestParam(name = "groutingTotalUpdate", required = false) String groutingTotalUpdate) {
        return deviceMixinStationUpdateLogService.saveMixinStationInfoUpdateLog(id,batchingProductionUpdate,groutingTotalUpdate);
    }
}


