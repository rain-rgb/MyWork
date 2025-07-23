package com.trtm.iot.ckqdatahandle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPump;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPumpDay;
import com.trtm.iot.ckqdatahandle.service.impl.DeviceGroutingPumpDayServiceImpl;
import com.trtm.iot.ckqdatahandle.service.impl.DeviceGroutingPumpServiceImpl;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpDayVo;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "注浆泵信息")
@RestController
@RequestMapping("/grouting/pump")
@Slf4j
public class DeviceGroutingPumpController {

    @Autowired
    private DeviceGroutingPumpServiceImpl deviceGroutingPumpService;

    @Autowired
    private DeviceGroutingPumpDayServiceImpl deviceGroutingPumpDayService;


    @GetMapping("/selectGroutingPumpListPage")
    @ApiOperation("注浆泵信息")
    public Result<IPage<DeviceGroutingPump>> selectGroutingPumpListPage(DeviceGroutingPumpVo DeviceGroutingPumpVo) {
        Result<IPage<DeviceGroutingPump>> result = new Result<>();
        IPage<DeviceGroutingPump> deviceGroutingPumpIPage = deviceGroutingPumpService.selectDeviceGroutingPumpListPage(DeviceGroutingPumpVo);
        result.setResult(deviceGroutingPumpIPage);
        result.setSuccess(true);
        return result;
    }

    @GetMapping("/selectGroutingPumpDayListPage")
    @ApiOperation("注浆泵日信息")
    public Result<IPage<DeviceGroutingPumpDay>> selectGroutingPumpDayListPage(DeviceGroutingPumpVo DeviceGroutingPumpVo) {
        Result<IPage<DeviceGroutingPumpDay>> result = new Result<>();
        IPage<DeviceGroutingPumpDay> deviceGroutingPumpIPage = deviceGroutingPumpDayService.selectDeviceGroutingPumpDayListPage(DeviceGroutingPumpVo);
        result.setResult(deviceGroutingPumpIPage);
        result.setSuccess(true);
        return result;
    }

    @GetMapping("/selectDeviceGroutingPumpDayVoList")
    @ApiOperation("注浆泵每日信息")
    public List<DeviceGroutingPumpDayVo> selectDeviceGroutingPumpDayVoList(@ApiParam(name = "time", value = "time", required = false) @RequestParam(name = "time", required = false) String time) {
        List<DeviceGroutingPumpDayVo> deviceGroutingPumpDayVos = deviceGroutingPumpDayService.selectDeviceGroutingPumpDayVoList(time);
        return deviceGroutingPumpDayVos;
    }


}
