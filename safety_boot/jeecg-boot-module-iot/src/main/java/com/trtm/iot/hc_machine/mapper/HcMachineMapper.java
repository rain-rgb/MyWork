package com.trtm.iot.hc_machine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 机械设备
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
public interface HcMachineMapper extends BaseMapper<HcMachine> {

    @Select("select * from hc_machine where (machineTypeCode = 5 or machineTypeCode = 12) and tmEndTime = '3000-01-01 00:00:00' and orgcode like concat(#{orgCode},'%')")
    List<HcMachine> fingsum(String orgCode);

    @Select("select * from hc_machine where (machineTypeCode = 3 or machineTypeCode = 4 or machineTypeCode = 11) and tmEndTime = '3000-01-01 00:00:00' and orgcode like concat(#{orgCode},'%')")
    List<HcMachine> fingsum1(String orgCode);
}
