package com.trtm.iot.hc_machine_onlinerecord.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.hc_machine_onlinerecord.entity.HcMachineOnlinerecord;

import java.util.List;

/**
 * @Description: 设备上下线
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
public interface HcMachineOnlinerecordMapper extends BaseMapper<HcMachineOnlinerecord> {

    List<HcMachineOnlinerecord> findByStarttimeAndEndtime(String starttime, String endtime, String sectionid);
}
