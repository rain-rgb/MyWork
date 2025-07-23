package com.trtm.iot.hc_pavement_alarm.mapper;

import java.util.List;

import com.trtm.iot.hc_machine.entity.HcMachine;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 摊铺碾压预警
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
public interface HcPavementAlarmMapper extends BaseMapper<HcPavementAlarm> {

    @Select("select * from hc_machine where machine_id = #{machineid}")
    HcMachine getHcMachine(String machineid);
}
