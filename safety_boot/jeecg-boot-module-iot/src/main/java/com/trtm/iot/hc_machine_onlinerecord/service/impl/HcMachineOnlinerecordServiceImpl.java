package com.trtm.iot.hc_machine_onlinerecord.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.hc_machine_onlinerecord.entity.HcMachineOnlinerecord;
import com.trtm.iot.hc_machine_onlinerecord.mapper.HcMachineOnlinerecordMapper;
import com.trtm.iot.hc_machine_onlinerecord.service.IHcMachineOnlinerecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 设备上下线
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Service
public class HcMachineOnlinerecordServiceImpl extends ServiceImpl<HcMachineOnlinerecordMapper, HcMachineOnlinerecord> implements IHcMachineOnlinerecordService {
    @Autowired
    HcMachineOnlinerecordMapper hcMachineOnlinerecordMapper;
    @Override
    public List<HcMachineOnlinerecord> findByStarttimeAndEndtime(String starttime, String endtime, String sectionid) {
        return hcMachineOnlinerecordMapper.findByStarttimeAndEndtime(starttime,endtime, sectionid);
    }
}
