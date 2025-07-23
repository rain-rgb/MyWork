package com.trtm.iot.hc_machine_onlinerecord.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.hc_machine_onlinerecord.entity.HcMachineOnlinerecord;

import java.util.List;

/**
 * @Description: 设备上下线
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
public interface IHcMachineOnlinerecordService extends IService<HcMachineOnlinerecord> {

    List<HcMachineOnlinerecord> findByStarttimeAndEndtime(String starttime, String endtime, String sectionid);
}
