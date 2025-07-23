package com.trtm.iot.switchmachinePhone.service;

import com.trtm.iot.switchmachinePhone.entity.SwitchmachinePhone;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 拌合站设备开关机配置表
 * @Author: jeecg-boot
 * @Date:   2022-08-22
 * @Version: V1.0
 */
public interface ISwitchmachinePhoneService extends IService<SwitchmachinePhone> {

    List<SwitchmachinePhone> getons(String shebeibianhao);
}
