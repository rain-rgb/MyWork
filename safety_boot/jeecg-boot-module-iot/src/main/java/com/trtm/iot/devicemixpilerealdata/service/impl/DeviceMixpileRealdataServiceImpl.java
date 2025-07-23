package com.trtm.iot.devicemixpilerealdata.service.impl;

import com.trtm.iot.deviceMixpileHistorydataOne.mapper.DeviceMixpileHistorydataOneMapper;
import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.trtm.iot.devicemixpilerealdata.mapper.DeviceMixpileRealdataMapper;
import com.trtm.iot.devicemixpilerealdata.service.IDeviceMixpileRealdataService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: device_mixpile_realdata
 * @Author: jeecg-boot
 * @Date:   2021-10-20
 * @Version: V1.0
 */
@Service
public class DeviceMixpileRealdataServiceImpl extends ServiceImpl<DeviceMixpileRealdataMapper, DeviceMixpileRealdata> implements IDeviceMixpileRealdataService {

    @Resource
    DeviceMixpileRealdataMapper deviceMixpileRealdataMapper;

    @Override
    public List<Map<String, Object>> countOnline(String sysOrgCode) {
        return deviceMixpileRealdataMapper.countOnline(sysOrgCode);
    }

    @Override
    public DeviceMixpileRealdata queryone(String shebeiNo) {
        return deviceMixpileRealdataMapper.queryone(shebeiNo);
    }

    @Override
    public List<DeviceMixpileRealdata> queryones(String shebeiNo) {
        return deviceMixpileRealdataMapper.queryones(shebeiNo);
    }

    @Override
    public List<DeviceMixpileRealdata> listByshebei(String shebeilist) {

        return deviceMixpileRealdataMapper.listByshebei(shebeilist);
    }
}
