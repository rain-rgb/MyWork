package com.trtm.iot.devicetunnelpositiondatareal.service;

import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicetunnelpositiondatareal.entity.DeviceTunnelPositiondataReal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.devicetunnelpositiondatareal.entity.DeviceTunnelPositiondataRealDto;

import java.util.List;

/**
 * @Description: 人员定位实时表
 * @Author: jeecg-boot
 * @Date:   2021-09-27
 * @Version: V1.0
 */
public interface IDeviceTunnelPositiondataRealService extends IService<DeviceTunnelPositiondataReal> {

    List<DeviceTunnelPositiondataRealDto> select(String orgCode,String jzwz,String sbtype);

    List<DeviceTunnelPositiondataReal> list(QueryWrapper<DeviceTunnelPositiondataRealDto> queryWrapper);

    List<DeviceTunnelPositiondataReal> queryJiXinList(String shebeino);

    List<DeviceTunnelPositiondataReal> queryJiXinBanZuList(String shebeino);

    List<DeviceTunnelPositiondataReal> querylistAttendance(String shebeino);
}
