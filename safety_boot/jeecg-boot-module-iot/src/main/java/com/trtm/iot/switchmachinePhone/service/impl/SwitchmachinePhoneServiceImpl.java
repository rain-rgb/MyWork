package com.trtm.iot.switchmachinePhone.service.impl;

import com.trtm.iot.switchmachinePhone.entity.SwitchmachinePhone;
import com.trtm.iot.switchmachinePhone.mapper.SwitchmachinePhoneMapper;
import com.trtm.iot.switchmachinePhone.service.ISwitchmachinePhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 拌合站设备开关机配置表
 * @Author: jeecg-boot
 * @Date:   2022-08-22
 * @Version: V1.0
 */
@Service
public class SwitchmachinePhoneServiceImpl extends ServiceImpl<SwitchmachinePhoneMapper, SwitchmachinePhone> implements ISwitchmachinePhoneService {

    @Autowired SwitchmachinePhoneMapper switchmachinePhoneMapper;

    @Override
    public List<SwitchmachinePhone> getons(String shebeibianhao) {
        return switchmachinePhoneMapper.getons(shebeibianhao);
    }
}
