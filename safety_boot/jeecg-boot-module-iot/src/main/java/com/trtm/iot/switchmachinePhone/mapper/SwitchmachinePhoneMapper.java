package com.trtm.iot.switchmachinePhone.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.switchmachinePhone.entity.SwitchmachinePhone;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 拌合站设备开关机配置表
 * @Author: jeecg-boot
 * @Date:   2022-08-22
 * @Version: V1.0
 */
public interface SwitchmachinePhoneMapper extends BaseMapper<SwitchmachinePhone> {

    List<SwitchmachinePhone> getons(String shebeibianhao);
}
