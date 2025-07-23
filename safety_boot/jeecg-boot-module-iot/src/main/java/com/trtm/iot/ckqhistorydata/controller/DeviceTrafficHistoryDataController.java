/**
 * @ClassName DeviceTrafficHistoryDataController.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月22日 12:01:00
 */
package com.trtm.iot.ckqhistorydata.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPump;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationDetails;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpVo;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import com.trtm.iot.ckqhistorydata.mapper.DeviceTrafficHistoryDataMapper;
import com.trtm.iot.ckqhistorydata.service.IDeviceTrafficHistoryDataService;
import com.trtm.iot.ckqhistorydata.vo.DeviceTrafficHistoryDataVo;
import com.trtm.iot.ckqhistorydata.vo.LookDeviceTrafficHistoryDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "注浆泵实时数据")
@RestController
@RequestMapping("grouting/pump/realTime/data")
@Slf4j
public class DeviceTrafficHistoryDataController {

    @Autowired
    private DeviceTrafficHistoryDataMapper deviceTrafficHistoryDataMapper;


    @GetMapping("/selectGroutingPumpPage")
    @ApiOperation("注浆泵实时数据")
    public Map<String, Object> selectGroutingPumpPage(@ApiParam(name = "sid", value = "sid", required = true) @RequestParam(name = "sid", required = true) String sid) {
        Map<String, Object> result = new HashMap<>();
        List<LookDeviceTrafficHistoryDataVo> lookDeviceTrafficHistoryDataVoList = new ArrayList<>();
        if ("/usr/plcnet/BP6_1_STA/edge/u".equals(sid)) {
            List<String> fmList = getFMList();
            for (String fm : fmList) {
                LookDeviceTrafficHistoryDataVo lookDeviceTrafficHistoryDataVo = deviceTrafficHistoryDataMapper.selectGroutingPumpRealTimeData("6_1", fm);
                if (null != lookDeviceTrafficHistoryDataVo) {
                    lookDeviceTrafficHistoryDataVoList.add(lookDeviceTrafficHistoryDataVo);
                }
            }
        } else {
            List<String> fmList = getFMList();
            for (String fm : fmList) {
                LookDeviceTrafficHistoryDataVo lookDeviceTrafficHistoryDataVo = deviceTrafficHistoryDataMapper.selectGroutingPumpRealTimeData(sid.replace("STA", "PUMP"), fm);
                if (null != lookDeviceTrafficHistoryDataVo) {
                    lookDeviceTrafficHistoryDataVoList.add(lookDeviceTrafficHistoryDataVo);
                }
            }
        }
        if (lookDeviceTrafficHistoryDataVoList.size() > 0) {
            result.put("deviceTrafficHistoryData", lookDeviceTrafficHistoryDataVoList);
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }

    public static List<String> getFMList() {
        List<String> list = new ArrayList<>();
        list.add("FM1");
        list.add("FM2");
        list.add("FM3");
        list.add("FM4");
        list.add("FM5");
        list.add("FM6");
        list.add("FM7");
        list.add("FM8");
        list.add("FM10");
        list.add("FM11");
        return list;

    }

}


